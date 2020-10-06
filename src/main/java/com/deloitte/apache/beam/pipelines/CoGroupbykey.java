package com.deloitte.apache.beam.pipelines;

import java.util.Arrays;
import java.util.List;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.join.CoGbkResult;
import org.apache.beam.sdk.transforms.join.CoGroupByKey;
import org.apache.beam.sdk.transforms.join.KeyedPCollectionTuple;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TupleTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoGroupbykey {
	static final Logger log = LoggerFactory.getLogger(CoGroupbykey.class);

	public static void main(String args[]) {

		Pipeline p = Pipeline.create(PipelineOptionsFactory.fromArgs(args).withValidation().create());

		final List<KV<String, String>> emailsList = Arrays.asList(KV.of("amy", "amy@example.com"),
				KV.of("carl", "carl@example.com"), KV.of("julia", "julia@example.com"),
				KV.of("carl", "carl@email.com"));

		final List<KV<String, String>> phonesList = Arrays.asList(KV.of("amy", "111-222-3333"),
				KV.of("james", "222-333-4444"), KV.of("amy", "333-444-5555"), KV.of("carl", "444-555-6666"));

		PCollection<KV<String, String>> emails = p.apply("CreateEmails", Create.of(emailsList));
		PCollection<KV<String, String>> phones = p.apply("CreatePhones", Create.of(phonesList));

		final TupleTag<String> emailsTag = new TupleTag<>();
		final TupleTag<String> phonesTag = new TupleTag<>();

		PCollection<KV<String, CoGbkResult>> results = KeyedPCollectionTuple.of(emailsTag, emails)
				.and(phonesTag, phones).apply(CoGroupByKey.create());

		PCollection<String> contactLines = results.apply(ParDo.of(new DoFn<KV<String, CoGbkResult>, String>() {

			@ProcessElement
			public void processElement(ProcessContext c) {
				KV<String, CoGbkResult> e = c.element();
				String key = e.getKey();
				CoGbkResult result = e.getValue();
				Iterable<String> allStrings = result.getAll(emailsTag);
				Iterable<String> allStrings2 = result.getAll(phonesTag);
				c.output(key + ";" + allStrings + " ; " + allStrings2);

			}
		}));

		contactLines.apply("debug", ParDo.of(new PrintDoFn<>("output ")));

		p.run().waitUntilFinish();
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
