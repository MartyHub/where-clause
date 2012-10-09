package com.bnpparibas.eqd.prs.where.builder.operation;

import com.bnpparibas.eqd.prs.where.builder.EvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.FormatContext;
import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.context.SqlEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.type.BinaryOperatorType;

public class BinaryOperation implements Operation {

	private final BinaryOperatorType operator;

	private final Operation operation1;

	private final Operation operation2;

	public BinaryOperation(BinaryOperatorType operator, Operation operation1, Operation operation2) {
		this.operator = operator;
		this.operation1 = operation1;
		this.operation2 = operation2;
	}

	@Override
	public boolean isValid() {
		final boolean result = operation1.isValid();

		if (result != operation2.isValid()) {
			throw new IllegalArgumentException("Both operation must be in the same state");
		}

		return result;
	}

	public BinaryOperatorType getOperator() {
		return operator;
	}

	public Operation getOperation1() {
		return operation1;
	}

	public Operation getOperation2() {
		return operation2;
	}

	@Override
	public void evaluate(EvaluationContext context) {
		context.visit(this);
	}

	public void evaluate(SqlEvaluationContext context) {
		context.append("(");
		operation1.evaluate(context);

		context.append(operator.getSymbol());

		operation2.evaluate(context);
		context.append(")");
	}

	@Override
	public String toPrettyString(FormatContext context) {
		return "(" + operation1.toPrettyString(context) + " " + operator.getSymbol() + " "
				+ operation2.toPrettyString(context) + ")";
	}

	@Override
	public String toString() {
		return "(" + operation1 + " " + operator.getSymbol() + " " + operation2 + ")";
	}
}
