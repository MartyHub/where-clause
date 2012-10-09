package com.bnpparibas.eqd.prs.where.builder.operation.querydsl;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.ComparableExpression;

public class ComparableExpressionOperation<T extends Comparable<T>> extends
		SimpleExpressionOperation<T> {

	public ComparableExpressionOperation(ComparableExpression<T> property) {
		super(property);
	}

	public ComparableExpression<T> getProperty() {
		return (ComparableExpression<T>) property;
	}

	@Override
	public final Predicate lt(T value) {
		return getProperty().lt(value);
	}

	@Override
	public final Predicate gt(T value) {
		return getProperty().gt(value);
	}

	@Override
	public final Predicate loe(T value) {
		return getProperty().loe(value);
	}

	@Override
	public final Predicate goe(T value) {
		return getProperty().goe(value);
	}
}
