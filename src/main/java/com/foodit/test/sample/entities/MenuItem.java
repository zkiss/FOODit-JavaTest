package com.foodit.test.sample.entities;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class MenuItem {

	@Parent
	private Key<RestaurantData> restaurant;

	@Id
	// reason for sloppyness: bad data
	// String because there are menu items with the value 0 which is rejected by objectify
	private String id;

	private String name;
	private String category;

	private int orderCount;

	public Key<RestaurantData> getRestaurant() {
		return this.restaurant;
	}

	public void setRestaurant(Key<RestaurantData> restaurant) {
		this.restaurant = restaurant;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Number of times this item was ordered during it's lifetime
	 *
	 * @return order count
	 */
	public int getOrderCount() {
		return this.orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public void incOrderCount() {
		this.orderCount++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
		result = (prime * result) + ((this.restaurant == null) ? 0 : this.restaurant.hashCode());
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
		MenuItem other = (MenuItem) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.restaurant == null) {
			if (other.restaurant != null) {
				return false;
			}
		} else if (!this.restaurant.equals(other.restaurant)) {
			return false;
		}
		return true;
	}

}
