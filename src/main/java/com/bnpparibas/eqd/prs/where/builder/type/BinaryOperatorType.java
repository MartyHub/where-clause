package com.bnpparibas.eqd.prs.where.builder.type;

public enum BinaryOperatorType {

	AND("and"), OR("or");

	private final String symbol;

	BinaryOperatorType(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}
}
