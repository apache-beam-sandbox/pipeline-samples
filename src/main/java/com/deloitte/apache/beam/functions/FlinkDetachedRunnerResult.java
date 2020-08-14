package com.deloitte.apache.beam.functions;

import java.io.IOException;

import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.metrics.MetricResults;
import org.joda.time.Duration;

public class FlinkDetachedRunnerResult implements PipelineResult {

	FlinkDetachedRunnerResult() {
	}

	@Override
	public State getState() {
		return State.UNKNOWN;
	}

	@Override
	public MetricResults metrics() {
		throw new UnsupportedOperationException("The FlinkRunner does not currently support metrics.");
	}

	@Override
	public State cancel() throws IOException {
		throw new UnsupportedOperationException("Cancelling is not yet supported.");
	}

	@Override
	public State waitUntilFinish() {
		return State.UNKNOWN;
	}

	@Override
	public State waitUntilFinish(Duration duration) {
		return State.UNKNOWN;
	}

	@Override
	public String toString() {
		return "FlinkDetachedRunnerResult{}";
	}
}
