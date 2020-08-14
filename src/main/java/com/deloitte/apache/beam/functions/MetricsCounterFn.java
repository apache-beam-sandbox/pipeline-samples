package com.deloitte.apache.beam.functions;

import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;

public class MetricsCounterFn extends DoFn<String, String> {

	private final Counter counter = Metrics.counter("deloitte", "WordCounter");

	@ProcessElement
	public void processElement(ProcessContext context) {

		String element = context.element();
		counter.inc();

	}

}
