package com.bnpparibas.eqd.prs.where.builder.type;

import java.util.HashMap;
import java.util.Map;

public enum SimpleOperatorType {

	EQ("="), NE("!="), GT(">"), LT("<"), GOE(">="), LOE("<=");

	private static Map<String, SimpleOperatorType> CACHE = new HashMap<String, SimpleOperatorType>();

	static {
		for (SimpleOperatorType operator : SimpleOperatorType.values()) {
			CACHE.put(operator.getSymbol(), operator);
		}
	}

	public static SimpleOperatorType fromSymbol(String symbol) {
		return CACHE.get(symbol);
	}

	private final String symbol;

	SimpleOperatorType(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}
}
