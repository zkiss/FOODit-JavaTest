package com.foodit.test.sample.entities;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.googlecode.objectify.annotation.Embed;

/**
 * Stores a {@link BigDecimal}
 */
@Embed
// No support for BigDecimal
// best I could find was com.googlecode.objectify.annotation.Translate - VERY tedious.
// reason for sloppyness: no time to figure out the correct way
public class OfyBigDecimal {

	private byte[] bytes;
	private int scale;

	public OfyBigDecimal() {
		// for ofy
	}

	public OfyBigDecimal(BigDecimal bd) {
		this.bytes = bd.unscaledValue().toByteArray();
		this.scale = bd.scale();
	}

	public BigDecimal getValue() {
		return new BigDecimal(new BigInteger(bytes), scale);
	}

}
