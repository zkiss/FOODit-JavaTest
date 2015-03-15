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

	// couldn't filter count if this was a @Parent Key
	// docs are a bit vague on what and how you can filter on with filterKey
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) (this.id ^ (this.id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Order other = (Order) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

}
