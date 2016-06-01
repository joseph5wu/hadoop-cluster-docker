package wordCount;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.LinkedList;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCount {
    final static String OUTPUT_FILENAME = "/part-r-00000";
    final static String BIGRAM_OUT = "/bigram_out";
    final static String REVBIGRAM_OUT = "/revBigram_out";
    
    public static void main(String[] args) throws Exception {
        int res = 0;
        
        res = ToolRunner.run(new Configuration(), new BigramGen(), args);
        if (res == 1) System.exit(res);
        
        res = ToolRunner.run(new Configuration(), new ReverseMap(), args);
        if (res == 1) System.exit(res);

        analyzeAndPrint(args);
    }

    public static void analyzeAndPrint(String[] args) throws Exception {
        List<Integer> countList = new LinkedList<Integer>();
        List<String> bigramList = new LinkedList<String>();

        // read sorted bi-gram from HDFS
        Path pt = new Path(args[1] + WordCount.REVBIGRAM_OUT + WordCount.OUTPUT_FILENAME);
        FileSystem fs = FileSystem.get(new Configuration());
        BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(pt)));
        
        String line = br.readLine();
        if (line == null) {
            throw new Exception("Sorted bi-gram file has zero size");
        }

        StringTokenizer tokenizer = null;
        System.out.println("\nPrinting sorted bi-grams from HDFS...");
        while (line != null) {
            tokenizer = new StringTokenizer(line);
            countList.add(Integer.parseInt(tokenizer.nextToken()));
            bigramList.add(tokenizer.nextToken() + " " + tokenizer.nextToken());

            System.out.println(line);
            line = br.readLine();
        }

        // printing total bi-gram count
        float sum = 0;
        for (int x : countList) {
            sum += x;
        }
        System.out.println("\n(1) Total bi-gram count is " + (int)sum);
        System.out.println("(2) The most common bi-gram is \"" + bigramList.get(0) + 
            "\", which appears " + countList.get(0) + " times");

        sum *= 0.1;
        int i = 0;
        for (; sum > 0 && i < countList.size(); i++) {
            sum -= countList.get(i);
        }
        System.out.println("(3) We need " + i + " bi-grams to add up to 10% of all bi-grams");

        br.close();
        fs.close();        
    }
}

class BigramGen extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
 
        Configuration conf = this.getConf();
        
        Job job = Job.getInstance(conf);
        job.setJarByClass(WordCount.class);
 
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        job.setMapperClass(TokenizeMapper.class);
        job.setCombinerClass(CollectReducer.class);
        job.setReducerClass(CollectReducer.class);
        
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]+WordCount.BIGRAM_OUT));
 
        return job.waitForCompletion(true) ? 0 : 1;
    }
}

class ReverseMap extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
 
        Configuration conf = this.getConf();
        
        Job job = Job.getInstance(conf);
        job.setJarByClass(WordCount.class);
 
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);
        
        job.setSortComparatorClass(LongWritable.DecreasingComparator.class);

        job.setMapperClass(RevMapper.class);
        job.setCombinerClass(PseudoReducer.class);
        job.setReducerClass(PseudoReducer.class);
        
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        
        FileInputFormat.addInputPath(job, new Path(args[1]+WordCount.BIGRAM_OUT+WordCount.OUTPUT_FILENAME));
        FileOutputFormat.setOutputPath(job, new Path(args[1]+WordCount.REVBIGRAM_OUT));
 
        return job.waitForCompletion(true) ? 0 : 1;
    }
}