package com.deloitte.apache.beam.pipelines;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.extensions.sorter.BufferedExternalSorter;
import org.apache.beam.sdk.extensions.sorter.SortValues;
import org.apache.beam.sdk.io.FileBasedSink.FilenamePolicy;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.metrics.GaugeResult;
import org.apache.beam.sdk.metrics.MetricNameFilter;
import org.apache.beam.sdk.metrics.MetricQueryResults;
import org.apache.beam.sdk.metrics.MetricResult;
import org.apache.beam.sdk.metrics.MetricsFilter;
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
import com.deloitte.apache.beam.functions.MetricsCounterFn;
import com.deloitte.apache.beam.functions.MetricsGaugeFn;
import com.deloitte.apache.beam.functions.ResultsAsKeyValueFn;
import org.apache.log4j.BasicConfigurator;

public class GroupedWordCount {

	public static void main(String[] args) {

		BasicConfigurator.configure();

		PipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().create();

		Pipeline pipeline = Pipeline.create(options);

		BufferedExternalSorter.Options bufferedOptions = BufferedExternalSorter.options();

		pipeline.apply("TextWithWords", Create.of("one two three four five six seven").withCoder(StringUtf8Coder.of()))

				.apply("Extract Words", ParDo.of(new ExtractWordsFn()))
				.apply("Word Counter Metric", ParDo.of(new MetricsCounterFn()))
				.apply("Max Word Length Metric", ParDo.of(new MetricsGaugeFn()))	
				.apply("Count Words", Count.perElement())
				.apply("Create a Key Value List of words and counts", ParDo.of(new ResultsAsKeyValueFn()))
				.apply("Create Keys", MapElements.via(new CreateKeyFn())).apply("Group by Words", GroupByKey.create())
				.apply("Buffered Sort", SortValues.create(bufferedOptions))
				.apply("FormatResults", MapElements.via(new FormatAsTextFn()))
				.apply("WriteCounts", TextIO.write().withNumShards(1).to("/tmp/word-count.txt"));

		org.apache.beam.sdk.PipelineResult result = pipeline.run();

		System.out.println("---------- FINISHED -------------");
		MetricQueryResults metrics = (MetricQueryResults) result.metrics().queryMetrics(
					MetricsFilter.builder()
					 			 .addNameFilter(MetricNameFilter.named("deloitte", "WordCounter"))
					 			 .addNameFilter(MetricNameFilter.named("deloitte", "WordLength"))
					 			 .build());

		for (MetricResult<Long> counter : metrics.getCounters()) {
			System.out.println(counter.getName() + ":" + counter.getCommitted());
		}
		for (MetricResult<GaugeResult> gauge : metrics.getGauges()) {
			System.out.println(gauge.getName() + ":" + gauge.getAttempted());
		}
		

		// pipeline.run().waitUntilFinish();
	}
}
