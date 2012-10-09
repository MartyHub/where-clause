package com.bnpparibas.eqd.prs.where.builder.operation;

import com.bnpparibas.eqd.prs.where.builder.EvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.FormatContext;
import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.context.QueryDslEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.context.SqlEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.operation.querydsl.SimpleExpressionOperation;
import com.bnpparibas.eqd.prs.where.builder.type.SimpleOperatorType;
import com.mysema.query.types.Predicate;

public class SimpleOperation<T> implements Operation {

	private final SimpleOperatorType operator;

	private final T value;

	public SimpleOperation(SimpleOperatorType operator, T value) {
		this.operator = operator;
		this.value = value;
	}

	public SimpleOperatorType getOperator() {
		return operator;
	}

	@Override
	public boolean isValid() {
		return value != null;
	}

	public T getValue() {
		return value;
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
		SimpleExpressionOperation<T> current = context.current();

		switch (operator) {
		case EQ:
			return current.eq(value);
		case NE:
			return current.ne(value);
		case LT:
			return current.lt(value);
		case GT:
			return current.gt(value);
		case LOE:
			return current.loe(value);
		case GOE:
			return current.goe(value);
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
