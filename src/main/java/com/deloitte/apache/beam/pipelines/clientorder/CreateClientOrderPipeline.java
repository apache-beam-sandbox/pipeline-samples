package com.deloitte.apache.beam.pipelines.clientorder;

import java.util.LinkedList;
import java.util.List;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.Distinct;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.WithKeys;
import org.apache.beam.sdk.transforms.join.CoGbkResult;
import org.apache.beam.sdk.transforms.join.CoGroupByKey;
import org.apache.beam.sdk.transforms.join.KeyedPCollectionTuple;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TupleTag;

public class CreateClientOrderPipeline {

	/*
	 * Pipeline that creates a paired Master-Detail object of 
	 * (Master) ClientOrder
	 * (Detail) (One to One) OrderHeader
	 * (Detail) (One to Many) OrderItems
	 * based on a key.
	 */
	public static void main(String[] args) {

		
		PipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().create();

		Pipeline pipeline = Pipeline.create(options);

		// create 4 order headers, with distinct id, and customer id combination
		OrderHeader orderHeaderOne = new OrderHeader(1, 1);
		OrderHeader orderHeaderTwo = new OrderHeader(1, 2);
		OrderHeader orderHeaderThree = new OrderHeader(2, 2);
		
		// and one duplicate header
		OrderHeader duplicateHeader = new OrderHeader(2, 2);
		PCollection<OrderHeader> orderHeaderCollection = pipeline
				.apply(Create.of(orderHeaderOne, orderHeaderOne, orderHeaderTwo,duplicateHeader))
				.apply(Distinct.<OrderHeader>create());
		
		// create 4 order items -line items, with  id's and customer id's that should match
		// their respective headers
		
		OrderItem orderItemOne = new OrderItem(1, 1, "abc-123");
		OrderItem orderItemTwo = new OrderItem(2, 2, "def-456");
		OrderItem orderItemThree = new OrderItem(2, 2, "ghi-789");
		OrderItem orderItemFour = new OrderItem(1, 2, "jkl-123");
		
		PCollection<OrderItem> orderItemCollection = pipeline
				.apply(Create.of(orderItemOne, orderItemTwo, orderItemThree,orderItemFour));
		
        // create a KV using the id, and customer id to form a composite key 
		PCollection<KV<String, OrderHeader>> orderHeaderKV = orderHeaderCollection
				.apply(WithKeys.of(new SerializableFunction<OrderHeader, String>() {
					public String apply(OrderHeader orderHeader) {
						
						// create a composite string key that appends the id and the customer id
						StringBuffer sb = new StringBuffer();
						sb.append(orderHeader.getId()+"");
						sb.append(orderHeader.getCustomerId()+"");
						return sb.toString();
					}
				}));

		PCollection<KV<String, OrderItem>> orderItemKV = orderItemCollection
				.apply(WithKeys.of(new SerializableFunction<OrderItem, String>() {
					public String apply(OrderItem orderItem) {
						
						// same logic as orderheader,
						// create a composite string key that appends the id and the customer id
						StringBuffer sb = new StringBuffer();
						sb.append(orderItem.getId()+"");
						sb.append(orderItem.getCustomerId()+"");
						return sb.toString();
					}
				}));

		orderHeaderKV.apply("debug", ParDo.of(new PrintDoFn<>("order headers -> ")));
		orderItemKV.apply("debug", ParDo.of(new PrintDoFn<>("order items -> ")));
		
		final TupleTag<OrderHeader> headerTag = new TupleTag<>();
		final TupleTag<OrderItem> itemTag = new TupleTag<>();

		KeyedPCollectionTuple.of(headerTag, orderHeaderKV).and(itemTag, orderItemKV)
				.apply(CoGroupByKey.<String>create())

				.apply(ParDo.of(new DoFn<KV<String, CoGbkResult>, ClientOrder>() {
					@ProcessElement
					public void processElement(ProcessContext processContext) {

						ClientOrder clientOrder = new ClientOrder();
						clientOrder.setSource("test");
						List<OrderItem> orderItems = new LinkedList<>();
						KV<String, CoGbkResult> element = processContext.element();

						Iterable<OrderItem> tag1Val = element.getValue().getAll(itemTag);
						for (OrderItem val : tag1Val) {
							orderItems.add(val);
						}
						
						clientOrder.setOrderItems(orderItems);
						clientOrder.setItemCount(orderItems.size());
						
						Iterable<OrderHeader> tag2Val = element.getValue().getAll(headerTag);
						for (OrderHeader val : tag2Val) {
							clientOrder.setOrderHeader(val);
						}
						processContext.output(clientOrder);
					}
				})).apply("debug", ParDo.of(new PrintDoFn<>("client order ->  ")));

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
}
