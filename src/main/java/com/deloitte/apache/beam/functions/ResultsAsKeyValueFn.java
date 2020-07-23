package com.deloitte.apache.beam.functions;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

public class ResultsAsKeyValueFn extends DoFn<KV<String, Long>, KV<String, Long>> {

	private static final long serialVersionUID = 1L;

	@ProcessElement
	public void processElement(ProcessContext c) {
		KV<String, Long> element = c.element();
		if (element.getKey().length() > 2) {
			c.output(element);
		}
	}

}
