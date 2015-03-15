package com.foodit.test.sample.entities;

import java.math.BigDecimal;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class RestaurantData {

	@Id
	private String id;
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
