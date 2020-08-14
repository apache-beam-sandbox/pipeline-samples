package com.deloitte.apache.beam.functions;

import org.apache.beam.sdk.metrics.Gauge;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.transforms.DoFn;

public class MetricsGaugeFn extends DoFn<String, String> {

	private final Gauge gauge = Metrics.gauge("deloitte", "WordLength");

	@ProcessElement
	public void processElement(ProcessContext context) {
		String element = context.element();
		gauge.set(element.length());
	}
}
