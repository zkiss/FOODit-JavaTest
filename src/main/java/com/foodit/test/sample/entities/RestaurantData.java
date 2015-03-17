package com.foodit.test.sample.entities;

import java.math.BigDecimal;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class RestaurantData {

	@Id
	private String id;

	// Couldn't find a way to serialize BigDecimal.
	/*
	 * This relies on an optimistic locking mechanism in place which objectify claims to have.
	 */
	/*
	 * Don't particularly like sales being here. It could have been put in a RestaurantStats entity as well but then
	 * there would be an implicit assumption that RestaurantData and RestaurantStats pairs have the same primary key.
	 * Maybe you can put an @Id on a Key attribute? Didn't have time to test every hypothesis.
	 */
	private OfyBigDecimal sales;

	public RestaurantData() {
	}

	public RestaurantData(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getSales() {
		return this.sales.getValue();
	}

	public void setSales(BigDecimal sales) {
		this.sales = new OfyBigDecimal(sales);
	}

	public void incSales(BigDecimal increment) {
		if (sales == null) {
			sales = new OfyBigDecimal(increment);
		} else {
			sales = new OfyBigDecimal(sales.getValue().add(increment));
		}
	}

}
