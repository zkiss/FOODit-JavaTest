package com.foodit.test.sample.controller.message;

import java.math.BigDecimal;

public class SalesAmount {

	private final BigDecimal salesAmount;

	public SalesAmount(BigDecimal salesAmount) {
		this.salesAmount = salesAmount;
	}

	public BigDecimal getSalesAmount() {
		return this.salesAmount;
	}

}
