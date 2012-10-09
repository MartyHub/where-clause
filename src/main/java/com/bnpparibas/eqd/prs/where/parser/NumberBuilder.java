package com.bnpparibas.eqd.prs.where.parser;

public class NumberBuilder {

	private final StringBuilder integer = new StringBuilder();

	private final StringBuilder decimal = new StringBuilder();

	public boolean negative() {
		integer.append('-');

		return true;
	}

	public boolean appendInteger(String value) {
		integer.append(value);

		return true;
	}

	public boolean appendDecimal(String value) {
		decimal.append(value);

		return true;
	}

	public Number toNumber() {
		try {
			if (decimal.length() == 0) {
				return new Integer(integer.toString());
			} else {
				integer.append(decimal);

				return new Double(integer.toString());
			}
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Failed to convert <"
					+ integer.toString() + "> to Number", nfe);
		}
	}
}
