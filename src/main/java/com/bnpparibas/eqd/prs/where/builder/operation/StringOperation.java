package com.bnpparibas.eqd.prs.where.builder.operation;

import com.bnpparibas.eqd.prs.where.builder.EvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.FormatContext;
import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.context.QueryDslEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.context.SqlEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.operation.querydsl.SimpleExpressionOperation;
import com.bnpparibas.eqd.prs.where.builder.type.StringOperatorType;
import com.mysema.query.types.Predicate;

public class StringOperation implements Operation {

	private final StringOperatorType operator;

	private final String value;

	public StringOperation(StringOperatorType operator, String value) {
		this.operator = operator;
		this.value = value;
	}

	@Override
	public boolean isValid() {
		return value != null;
	}

	@Override
	public void evaluate(EvaluationContext context) {
		context.visit(this);
	}

	public void evaluate(SqlEvaluationContext context) {
		context.appendCurrentProperty();
		context.append(operator.getSymbol());
		context.addParameter(value);
	}

	public Predicate evaluate(QueryDslEvaluationContext context) {
		SimpleExpressionOperation<String> current = context.current();

		switch (operator) {
		case LIKE:
			return current.like(value);
		case NOT_LIKE:
			return current.notLike(value);
		default:
			throw new IllegalArgumentException("Don't know how to handle <"
					+ operator + ">");
		}
	}

	@Override
	public String toPrettyString(FormatContext context) {
		return operator.getSymbol() + " " + context.format(value);
	}

	@Override
	public String toString() {
		return operator.getSymbol() + " " + value;
	}
}
