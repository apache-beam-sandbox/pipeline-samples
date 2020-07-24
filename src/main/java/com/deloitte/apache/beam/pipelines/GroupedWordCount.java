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
        
        BufferedExternalSorter.Options bufferedOptions = BufferedExternalSorter.options();
        Pipeline pipeline = Pipeline.create(options);
        
        pipeline.apply("ReadLines",
                Create.of("this is a new day of the week, the next day there will be another day,"
                		+ "and after that day, and a day, and a day, and a day, and a day,"
                		+ "and a day, another week !"
                		+ "the week prior, there were a series of days, the days turn into weeks,"
                		+ "how time just goes when one thinks of days and weeks")
                .withCoder(StringUtf8Coder.of()))
                .apply(ParDo.of(new ExtractWordsFn()))
                .apply(Count.perElement())
                .apply(ParDo.of(new ResultsAsKeyValueFn()))
                .apply("CreateKey", MapElements.via(new CreateKeyFn()))
                .apply(GroupByKey.create())
                .apply(SortValues.create(bufferedOptions))
                .apply("FormatResults", MapElements.via(new FormatAsTextFn()))
                .apply("WriteCounts", TextIO.write().withNumShards(1).to("grouped-word-count"));
        
        pipeline.run().waitUntilFinish();
    }
}
