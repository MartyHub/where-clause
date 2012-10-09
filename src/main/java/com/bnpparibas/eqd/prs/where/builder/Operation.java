package com.bnpparibas.eqd.prs.where.builder;

public interface Operation {

	boolean isValid();

	void evaluate(EvaluationContext context);

	String toPrettyString(FormatContext context);
}
