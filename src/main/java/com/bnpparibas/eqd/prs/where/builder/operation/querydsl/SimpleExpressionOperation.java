package com.bnpparibas.eqd.prs.where.builder.operation.querydsl;

import com.bnpparibas.eqd.prs.where.builder.EvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.FormatContext;
import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.context.QueryDslEvaluationContext;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.SimpleExpression;

public abstract class SimpleExpressionOperation<T> implements Operation {

	protected final SimpleExpression<T> property;

	protected SimpleExpressionOperation(SimpleExpression<T> property) {
		this.property = property;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public final void evaluate(EvaluationContext context) {
		context.visit(this);
	}

	public final void evaluate(QueryDslEvaluationContext context) {
		context.visit(this);
	}

	public final Predicate isNull() {
		return property.isNull();
	}

	public final Predicate isNotNull() {
		return property.isNotNull();
	}

	public final Predicate eq(T value) {
		return property.eq(value);
	}

	public final Predicate ne(T value) {
		return property.ne(value);
	}

	public final Predicate in(T[] values) {
		return property.in(values);
	}

	public final Predicate notIn(T[] values) {
		return property.notIn(values);
	}

	public Predicate isTrue() {
		throw new UnsupportedOperationException();
	}

	public Predicate isFalse() {
		throw new UnsupportedOperationException();
	}

	public Predicate like(T value) {
		throw new UnsupportedOperationException();
	}

	public Predicate notLike(T value) {
		throw new UnsupportedOperationException();
	}

	public Predicate isEmpty() {
		throw new UnsupportedOperationException();
	}

	public Predicate isNotEmpty() {
		throw new UnsupportedOperationException();
	}

	public Predicate lt(T value) {
		throw new UnsupportedOperationException();
	}

	public Predicate loe(T value) {
		throw new UnsupportedOperationException();
	}

	public Predicate gt(T value) {
		throw new UnsupportedOperationException();
	}

	public Predicate goe(T value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final String toPrettyString(FormatContext context) {
		return toString();
	}

	@Override
	public final String toString() {
		return property.toString();
	}
}
