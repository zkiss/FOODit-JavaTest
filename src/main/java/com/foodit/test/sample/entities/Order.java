package com.foodit.test.sample.entities;

import java.math.BigDecimal;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Order {

	@Embed
	public static class Item {
		private Key<MenuItem> id;
		private OfyBigDecimal total;

		public Key<MenuItem> getId() {
			return this.id;
		}

		public void setId(Key<MenuItem> id) {
			this.id = id;
		}

		public BigDecimal getTotal() {
			return this.total.getValue();
		}

		public void setTotal(BigDecimal total) {
			this.total = new OfyBigDecimal(total);
		}

	}

	// couldn't filter count if this was a Key
	/*
	 * the lack of Key object here is not necessarily bad, but since Objectify explicitly supports references it seems
	 * semantically wrong not using it when what we are dealing with here is actually an entity relation.
	 */
	@Index
	private String restaurant;

	@Id
	private long id;

	private OfyBigDecimal totalValue;
	private List<Item> items;

	public String getRestaurant() {
		return this.restaurant;
	}

	public void setRestaurant(String restaurant) {
		this.restaurant = restaurant;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getTotalValue() {
		return this.totalValue.getValue();
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = new OfyBigDecimal(totalValue);
	}

	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
