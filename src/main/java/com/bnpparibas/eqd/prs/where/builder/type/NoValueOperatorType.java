package com.bnpparibas.eqd.prs.where.builder.type;

public enum NoValueOperatorType {

	IS_NULL("is null"), IS_NOT_NULL("is not null"), IS_TRUE("is true"), IS_FALSE(
			"is false"), IS_EMPTY("is empty"), IS_NOT_EMPTY("is not empty");

	private final String symbol;

	NoValueOperatorType(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}
}
