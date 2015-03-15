package com.foodit.test.sample.entities;

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
		// No support for BigDecimal
		private double total;

		public Key<MenuItem> getId() {
			return this.id;
		}

		public void setId(Key<MenuItem> id) {
			this.id = id;
		}

		public double getTotal() {
			return this.total;
		}

		public void setTotal(double total) {
			this.total = total;
		}

	}

	// couldn't filter count if this was a Key
	@Index
	private String restaurant;

	@Id
	private long id;

	// No support for BigDecimal
	// best I could find was com.googlecode.objectify.annotation.Translate
	// VERY tedious.
	// we'll live with the imprecisions.
	private double totalValue;
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

	public double getTotalValue() {
		return this.totalValue;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}