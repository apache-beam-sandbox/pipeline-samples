package com.deloitte.apache.beam.pipelines;

import java.util.Arrays;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.AfterPane;
import org.apache.beam.sdk.transforms.windowing.AfterWatermark;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.joda.time.Duration;

public class TriggerWindow {

	public static void main(String[] args) {

		PipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().create();

		Pipeline pipeline = Pipeline.create(options);
		
		PCollection<KV<String, String>> input = pipeline
				.apply(Create.of(Arrays.asList(KV.of("a", "100"), 
											   KV.of("b", "200"),
											   KV.of("c", "300")
											   )))
				.apply(Window.<KV<String, String>>configure().discardingFiredPanes()
						.triggering(AfterWatermark.pastEndOfWindow().withEarlyFirings(AfterPane.elementCountAtLeast(1)))
						.withAllowedLateness(Duration.millis(10)));

		input.apply("debug", ParDo.of(new PrintDoFn<>("Output")));

		pipeline.run().waitUntilFinish();
	}

	/**
	 * A simple DoFn that prints message to System.out
	 */
	private static class PrintDoFn<T> extends DoFn<T, Void> {
		private static final long serialVersionUID = 1L;
		private final String tag;

		public PrintDoFn(String tag) {
			this.tag = tag;
		}

		@ProcessElement
		public void processElement(ProcessContext c) {
			System.out.println(tag + ":" + c.element());
		}
	}

}
