package com.bnpparibas.eqd.prs.where.builder.type;

public enum StringOperatorType {

	LIKE("like"), NOT_LIKE("not like");

	private final String symbol;

	StringOperatorType(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}
}
