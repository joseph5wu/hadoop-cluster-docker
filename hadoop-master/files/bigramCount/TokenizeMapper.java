package bigramCount;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TokenizeMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private final Text word = new Text();
    private static String prev = null;

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);
        while (tokenizer.hasMoreTokens()) {
            // first iteration
            if (prev == null) {
                prev = tokenizer.nextToken();
                continue;
            }
            
            // second iteration and onwards
            String curr = tokenizer.nextToken();
            word.set(prev + " " + curr);
            context.write(word, one);
            
            prev = curr;
        }
    }
}
