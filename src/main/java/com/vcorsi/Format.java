package com.vcorsi;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Class to format and parse BigDecimal according to the constraints of the
 * application: 2 decimal digits with a dot as decimal separator. E.g. 555555.55
 * 
 * @author vladimiro
 * 
 */
class Format {

	private final DecimalFormat decimalFormat;

	Format() {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		String pattern = "#0.00";
		this.decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
	}

	/**
	 * @param line
	 *            String representation of a number. Must have 2 decimal digits.
	 *            The "." character is the decimal separator. E.g. 555555.55
	 * @return the parsed number
	 * @throws IllegalArgumentException
	 */
	BigDecimal parse(final String line) {
		if (line == null) {
			throw new NullPointerException("line cannot be null");
		}
		final String[] array = line.split("\\.");
		if (array.length != 2) {
			throw new IllegalArgumentException("Invalid line format");
		}
		if (array[1].length() != 2) {
			throw new IllegalArgumentException("Invalid line format");
		}
		try {
			final BigDecimal result = new BigDecimal(new BigInteger(
					array[0].concat(array[1])), 2);
			return result;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid line format", e);
		}
	}

	/**
	 * @param value
	 *            The value to be formatted according to the specified rules
	 * @return a String representation of the numeric value.
	 */
	String format(final BigDecimal value) {
		if (value == null) {
			throw new NullPointerException("value cannot be null");
		}
		return decimalFormat.format(value);
	}

}
