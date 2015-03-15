package com.foodit.test.sample.math;

import java.math.MathContext;
import java.math.RoundingMode;

public class FinancialMathContext {

	/**
	 * The math context commonly used in the financial sector when processing payments. Money is handled up to 2 decimal
	 * places with traditional rounding.
	 */
	// my assumption is that numbers don't have more decimal places than 2 in the json files.
	public static final MathContext FINANCIAL = new MathContext(2, RoundingMode.HALF_UP);

}
