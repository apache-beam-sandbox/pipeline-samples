package com.deloitte.apache.beam.pipelines;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.extensions.sorter.BufferedExternalSorter;
import org.apache.beam.sdk.extensions.sorter.SortValues;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;

import com.deloitte.apache.beam.functions.CreateKeyFn;
import com.deloitte.apache.beam.functions.ExtractWordsFn;
import com.deloitte.apache.beam.functions.FormatAsTextFn;
import com.deloitte.apache.beam.functions.ResultsAsKeyValueFn;
import org.apache.log4j.BasicConfigurator;

public class GroupedWordCount {
    
	public static void main(String[] args) {
		
		BasicConfigurator.configure();
        
		PipelineOptions options = PipelineOptionsFactory.fromArgs(args)
        		.withValidation()
        		.create();
		
		Pipeline pipeline = Pipeline.create(options);
        
		BufferedExternalSorter.Options bufferedOptions = BufferedExternalSorter.options();
        
        pipeline.apply("TextWithWords",
                Create.of("this is a new day of the week, the next day there will be another day,"
                		+ "and after that day, and a day, and a day, and a day, and a day,"
                		+ "and a day, another week !"
                		+ "the week prior, there were a series of days, the days turn into weeks,"
                		+ "how time just goes by when one thinks of days and weeks")
                .withCoder(StringUtf8Coder.of()))
                
        		.apply("Extract Words",ParDo.of(new ExtractWordsFn()))
                .apply("Count Words",Count.perElement())
                .apply("Create a Key Value List of words and counts", ParDo.of(new ResultsAsKeyValueFn()))
                .apply("Create Keys", MapElements.via(new CreateKeyFn()))
                .apply("Group by Words",GroupByKey.create())
                .apply("Buffered Sort",SortValues.create(bufferedOptions))
                
                .apply("FormatResults", MapElements.via(new FormatAsTextFn()))
                .apply("WriteCounts", TextIO.write().withNumShards(1)
                .to("grouped-word-count.txt"));
        
        pipeline.run().waitUntilFinish();
    }
}
