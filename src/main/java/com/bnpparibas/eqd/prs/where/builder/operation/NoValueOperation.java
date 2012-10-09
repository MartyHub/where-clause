package com.bnpparibas.eqd.prs.where.builder.operation;

import com.bnpparibas.eqd.prs.where.builder.EvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.FormatContext;
import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.context.QueryDslEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.context.SqlEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.operation.querydsl.SimpleExpressionOperation;
import com.bnpparibas.eqd.prs.where.builder.type.NoValueOperatorType;
import com.mysema.query.types.Predicate;

public class NoValueOperation implements Operation {

	private final NoValueOperatorType operator;

	public NoValueOperation(NoValueOperatorType operator) {
		this.operator = operator;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	public NoValueOperatorType getOperator() {
		return operator;
	}

	@Override
	public void evaluate(EvaluationContext context) {
		context.visit(this);
	}

	public void evaluate(SqlEvaluationContext context) {
		switch (operator) {
		case IS_EMPTY:
		case IS_NOT_EMPTY:
			throw new IllegalArgumentException("Don't know how to handle <"
					+ operator + ">");
		default:
			context.appendCurrentProperty();
			context.append(operator.getSymbol());
		}
	}

	public Predicate evaluate(QueryDslEvaluationContext context) {
		SimpleExpressionOperation<?> current = context.current();

		switch (operator) {
		case IS_NULL:
			return current.isNull();
		case IS_NOT_NULL:
			return current.isNotNull();
		case IS_TRUE:
			return current.isTrue();
		case IS_FALSE:
			return current.isFalse();
		case IS_EMPTY:
			return current.isEmpty();
		case IS_NOT_EMPTY:
			return current.isNotEmpty();
		default:
			throw new IllegalArgumentException("Don't know how to handle <"
					+ operator + ">");
		}
	}

	@Override
	public String toPrettyString(FormatContext context) {
		return toString();
	}

	@Override
	public String toString() {
		return operator.getSymbol();
	}
}
