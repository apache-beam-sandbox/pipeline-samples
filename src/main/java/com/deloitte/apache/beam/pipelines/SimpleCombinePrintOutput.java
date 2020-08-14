package com.deloitte.apache.beam.pipelines;

import java.io.Serializable;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;

public class SimpleCombinePrintOutput {

	public static void main(String[] args) {

		PipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().create();

		Pipeline pipeline = Pipeline.create(options);

		PCollection<KV<String, Integer>> input = pipeline
				.apply(Create.of(KV.of("a", 1), KV.of("b", 2), KV.of("a", 3), KV.of("c", 4)))
				.apply(Combine.perKey(new Summer()::sum));

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

	private static class Summer implements Serializable {
		private static final long serialVersionUID = 1L;

		public int sum(Iterable<Integer> integers) {
			int sum = 0;
			for (int i : integers) {
				sum += i;
			}
			return sum;
		}
	}

}
