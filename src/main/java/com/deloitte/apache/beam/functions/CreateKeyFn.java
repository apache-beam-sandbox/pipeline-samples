package com.deloitte.apache.beam.functions;

import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;

public class CreateKeyFn extends SimpleFunction<KV<String, Long>, KV<String, KV<String, Long>>> {

	private static final long serialVersionUID = 1L;

	@Override
	public KV<String, KV<String, Long>> apply(KV<String, Long> input) {
		return KV.of("sort", KV.of(input.getKey().toLowerCase(), input.getValue()));
	}
}
