package com.foodit.test.sample.entities;

import java.math.BigDecimal;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class Order {

	@Embed
	public static class Item {
		/*
		 * Originally I had price in here as well. I ended up not using it but I did not refactor the code to use a
		 * Key<MenuItem> list instead of this wrapper class.
		 *
		 * Originally wanted to have the price here because in case of a price change this is the accurate amount the
		 * user has spent on this item.
		 */
		private Key<MenuItem> id;

		public Key<MenuItem> getId() {
			return this.id;
		}

		public void setId(Key<MenuItem> id) {
			this.id = id;
		}
	}

	@Parent
	private Key<RestaurantData> restaurant;

	@Id
	private long id;

	private OfyBigDecimal totalValue;
	private List<Item> items;

	public Key<RestaurantData> getRestaurant() {
		return this.restaurant;
	}

	public void setRestaurant(Key<RestaurantData> restaurant) {
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
