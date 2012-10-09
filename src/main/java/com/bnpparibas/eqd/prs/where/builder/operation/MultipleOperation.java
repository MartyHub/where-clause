package com.bnpparibas.eqd.prs.where.builder.operation;

import java.util.Arrays;

import javax.annotation.Nullable;

import com.bnpparibas.eqd.prs.where.builder.EvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.FormatContext;
import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.context.QueryDslEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.context.SqlEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.operation.querydsl.SimpleExpressionOperation;
import com.bnpparibas.eqd.prs.where.builder.type.MultipleOperatorType;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.mysema.query.types.Predicate;

public class MultipleOperation<T> implements Operation {

	public static <T> MultipleOperation<T> build(MultipleOperatorType operator,
			T... values) {
		return new MultipleOperation<T>(operator, values);
	}

	private final MultipleOperatorType operator;

	private final T[] values;

	public MultipleOperation(MultipleOperatorType operator, T[] values) {
		this.operator = operator;
		this.values = values;
	}

	@Override
	public boolean isValid() {
		return values != null && values.length > 0;
	}

	@Override
	public void evaluate(EvaluationContext context) {
		context.visit(this);
	}

	public void evaluate(SqlEvaluationContext context) {
		context.appendCurrentProperty();
		context.append(operator.getSymbol());
		context.append("(");
		context.addParameter(values);
		context.append(")");
	}

	public Predicate evaluate(QueryDslEvaluationContext context) {
		SimpleExpressionOperation<T> current = context.current();

		switch (operator) {
		case IN:
			return current.in(values);
		case NOT_IN:
			return current.notIn(values);
		default:
			throw new IllegalArgumentException("Don't know how to handle <"
					+ operator + ">");
		}
	}

	@Override
	public String toPrettyString(final FormatContext context) {
		Function<T, String> toStringFunction = new Function<T, String>() {

			@Override
			public String apply(@Nullable T input) {
				return context.format(input);
			}
		};
		Iterable<String> iterable = Iterables.transform(Arrays.asList(values),
				toStringFunction);

		return operator.getSymbol() + " " + Joiner.on(',').join(iterable);
	}

	@Override
	public String toString() {
		return operator.getSymbol() + " " + Joiner.on(',').join(values);
	}
}
