import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapred.lib.*;
import org.apache.hadoop.mapred.*;

public class ChainWordCountDriver extends Configured implements Tool { 

    public int run(String[] args) throws Exception {
        JobConf conf = new JobConf(getConf(), ChainWordCountDriver.class);
        conf.setJobName("wordcount");
        
        //Job job = Job.getInstance(conf, "wordcount");

        //Setting the input and output path 
        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));

        //Considering the input and output as text file set the input & output format to TextInputFormat 
        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        JobConf mapAConf = new JobConf(false);
        ChainMapper.addMapper(conf, TokenizerMapper.class, LongWritable.class, Text.class, Text.class, IntWritable.class, true, mapAConf);      
        
        //addMapper will take global conf object and mapper class ,input and output type for this mapper and output key/value have to be sent by value or by reference and localJObconf specific to this call
        JobConf mapBConf = new JobConf(false);
        ChainMapper.addMapper(conf, UpperCaserMapper.class, Text.class, IntWritable.class, Text.class, IntWritable.class, true, mapBConf);

        JobConf reduceConf = new JobConf(false);
        ChainReducer.setReducer(conf, WordCountReducer.class, Text.class, IntWritable.class, Text.class, IntWritable.class, true, reduceConf);

        JobConf mapCConf = new JobConf(false);
        ChainReducer.addMapper(conf, LastMapper.class, Text.class, IntWritable.class, Text.class, IntWritable.class, true, mapCConf);
        
        JobClient.runJob(conf);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new ChainWordCountDriver(), args);
        System.exit(res);
    }
} 

//TokenizerMapper  -  Parse the input file record for every token
class TokenizerMapper extends MapReduceBase implements Mapper<LongWritable, Text,Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(LongWritable key, Text value,OutputCollector output,Reporter reporter) throws IOException {
        String line = value.toString();
        System.out.println("Line:"+line);
        StringTokenizer itr = new StringTokenizer(line);
        while (itr.hasMoreTokens()) {
            word.set(itr.nextToken());
            output.collect(word, one);
        }
    }
}

//UpperCaserMapper - It will uppercase the passed token from TokenizerMapper
class UpperCaserMapper extends MapReduceBase implements Mapper<Text, IntWritable,Text, IntWritable> {

    public void map(Text key, IntWritable value,OutputCollector output,Reporter reporter) throws IOException {
        String word = key.toString().toUpperCase();
        System.out.println("Upper Case:"+word);
        output.collect(new Text(word), value);    
    }
}

//WordCountReducer - is doing nothing special just writing the key in the context 
class WordCountReducer extends MapReduceBase implements Reducer<Text, IntWritable,Text, IntWritable> {

    public void reduce(Text key, Iterator values,OutputCollector output, Reporter reporter) throws IOException {
        int sum = 0;
        output.collect(key, new IntWritable(sum));
    }
}

//LastMapper - will spilt the record sent from reducer and write into the final output file 
class LastMapper extends MapReduceBase implements Mapper<Text, IntWritable,Text, IntWritable> {
    
    public void map(Text key, IntWritable value,OutputCollector output,Reporter reporter) throws IOException {
        String[] word = key.toString().split(",");
        System.out.println("Upper Case:"+word);
        output.collect(new Text(word[0]), new Text(word[1]));    
    }
}