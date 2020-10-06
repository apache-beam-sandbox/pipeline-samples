package com.deloitte.apache.beam.pipelines.clientorder;

import java.util.List;

import org.apache.beam.sdk.schemas.JavaBeanSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/** Describes a client order. */
@DefaultSchema(JavaBeanSchema.class)
public class ClientOrder {
	
	public ClientOrder(String source, OrderHeader orderHeader, List<OrderItem> orderItems) {
		super();
		this.source = source;
		this.orderHeader = orderHeader;
		this.orderItems = orderItems;
	}
	public ClientOrder() {
		
	}
	private String source;
	private OrderHeader orderHeader;	
	private List<OrderItem> orderItems;
	private int itemCount;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public OrderHeader getOrderHeader() {
		return orderHeader;
	}
	public void setOrderHeader(OrderHeader orderHeader) {
		this.orderHeader = orderHeader;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + itemCount;
		result = prime * result + ((orderHeader == null) ? 0 : orderHeader.hashCode());
		result = prime * result + ((orderItems == null) ? 0 : orderItems.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientOrder other = (ClientOrder) obj;
		if (itemCount != other.itemCount)
			return false;
		if (orderHeader == null) {
			if (other.orderHeader != null)
				return false;
		} else if (!orderHeader.equals(other.orderHeader))
			return false;
		if (orderItems == null) {
			if (other.orderItems != null)
				return false;
		} else if (!orderItems.equals(other.orderItems))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ClientOrder [source=" + source + ", itemCount=" + itemCount + ", orderHeader=" + orderHeader
				+ ", orderItems=" + orderItems + "]";
	}
	
}
