package bigramCount;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class PseudoReducer extends Reducer<LongWritable, Text, LongWritable, Text> {
        
    public void reduce(LongWritable key, Iterable<Text> values, Context context)
      throws IOException, InterruptedException {
        Text oneGram = null;
        for (Text val : values) {
            oneGram = val;
            context.write(key, oneGram);
        }
    }
}
