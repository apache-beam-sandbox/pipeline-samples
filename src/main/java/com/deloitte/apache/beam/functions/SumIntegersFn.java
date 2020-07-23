package com.deloitte.apache.beam.functions;

import org.apache.beam.sdk.transforms.SerializableFunction;

public class SumIntegersFn implements SerializableFunction<Iterable<Integer>, Integer> {
	
	private static final long serialVersionUID = 1L;

	@Override
	public Integer apply(Iterable<Integer> input) {
		Integer sum = 0;
		for (Integer item : input) {
			sum += item;
		}

		return sum;
	}
}