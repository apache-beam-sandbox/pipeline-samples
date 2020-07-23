package com.deloitee.apache.beam.pipelines;

import java.util.Arrays;
import java.util.List;

import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.testing.ValidatesRunner;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.deloitte.apache.beam.functions.ExtractWordsFn;
import com.deloitte.apache.beam.functions.SimpleWordCountFormatAsTextFn;

@RunWith(JUnit4.class)
public class GroupedWordCountTest {

	static final String[] WORDS_ARRAY = new String[] { "hi there", "hi", "hi sue bob", "hi sue", "", "bob hi" };

	static final List<String> WORDS = Arrays.asList(WORDS_ARRAY);

	static final String[] COUNTS_ARRAY = new String[] { "hi: 5", "there: 1", "sue: 2", "bob: 2" };
	
	@Rule
	public TestPipeline testPipeLine = TestPipeline.create();

	
	@Test
	public void testExtractWordsFn() throws Exception {
		List<String> words = Arrays.asList(" some  input  words ", " ", " cool ", " foo", " bar");
		PCollection<String> output = testPipeLine
				.apply(Create.of(words).withCoder(StringUtf8Coder.of()))
				.apply(ParDo.of(new ExtractWordsFn()));
		
		PAssert.that(output).containsInAnyOrder("some", "input", "words", "cool", "foo", "bar");
		testPipeLine.run().waitUntilFinish();
	}

	
	
	@Test
	@Category(ValidatesRunner.class)
	public void testCountWords() throws Exception {
		PCollection<String> input = testPipeLine
										.apply(Create.of(WORDS).withCoder(StringUtf8Coder.of()));

		PCollection<String> output = input
										.apply(ParDo.of(new ExtractWordsFn()))
										.apply(Count.perElement())
										.apply(MapElements.via(new SimpleWordCountFormatAsTextFn()));
		
		PAssert.that(output).containsInAnyOrder(COUNTS_ARRAY);
		testPipeLine.run().waitUntilFinish();
	}

}
