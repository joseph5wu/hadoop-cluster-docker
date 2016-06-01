package wordCount;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RevMapper extends Mapper<LongWritable, Text, LongWritable, Text> {
    private final Text bigram = new Text();

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);

        while (tokenizer.hasMoreTokens()) {
            
            String gram1 = tokenizer.nextToken();
            String gram2 = tokenizer.nextToken();
            long count = Long.parseLong(tokenizer.nextToken());
            
            bigram.set(gram1 + " " + gram2);

            context.write(new LongWritable(count), bigram);
        }
    }
}
