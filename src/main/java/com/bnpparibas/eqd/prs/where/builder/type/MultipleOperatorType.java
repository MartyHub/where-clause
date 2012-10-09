package com.bnpparibas.eqd.prs.where.builder.type;

public enum MultipleOperatorType {

	IN("in"), NOT_IN("not in");

	private final String symbol;

	MultipleOperatorType(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}
}
