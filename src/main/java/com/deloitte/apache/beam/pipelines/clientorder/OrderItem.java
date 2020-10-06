package com.deloitte.apache.beam.pipelines.clientorder;

import java.io.Serializable;
import java.util.Objects;
import org.apache.beam.sdk.schemas.JavaBeanSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;

/** Describes an order item. */
@DefaultSchema(JavaBeanSchema.class)
public class OrderItem implements Serializable {
	
	private int id;
	private int customerId;
	private String gpn;
	
	public OrderItem(int id, int customerId, String gpn) {
		this.id = id;
		this.customerId = customerId;
		this.gpn = gpn;
	}

	public OrderItem() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getGpn() {
		return gpn;
	}

	public void setGpn(String gpn) {
		this.gpn = gpn;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		OrderItem orderItem = (OrderItem) o;
		return id == orderItem.id && customerId == orderItem.customerId && gpn == orderItem.gpn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, customerId);
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", customerId=" + customerId + ", gpn=" + gpn + "]";
	}

	
}
