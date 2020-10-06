package com.deloitte.apache.beam.pipelines.clientorder;

import java.io.Serializable;
import java.util.Objects;
import org.apache.beam.sdk.schemas.JavaBeanSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/** Describes an order header. */
@DefaultSchema(JavaBeanSchema.class)
public class OrderHeader implements Serializable {
	private int id;
	private int customerId;

	public OrderHeader(int id, int customerId) {
		this.id = id;
		this.customerId = customerId;
	}

	public OrderHeader() {
	}

	public int getId() {
		return id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		OrderHeader order = (OrderHeader) o;
		return id == order.id && customerId == order.customerId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, customerId);
	}

	@Override
	public String toString() {
		return "OrderHeader [id=" + id + ", customerId=" + customerId + "]";
	}
}
