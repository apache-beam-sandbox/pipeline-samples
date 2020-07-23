package com.deloitte.apache.beam.functions;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;

public class FormatAsTextFn extends SimpleFunction<KV<String, Iterable<KV<String, Long>>>, String> {

	private static final long serialVersionUID = 1L;

	@Override
	public String apply(KV<String, Iterable<KV<String, Long>>> input) {
		return StreamSupport.stream(input.getValue().spliterator(), false)
				.map(value -> String.format("%20s: %s", value.getKey(), value.getValue()))
				.collect(Collectors.joining(String.format("%n")));
	}
}
