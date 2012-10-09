package com.bnpparibas.eqd.prs.where.builder.operation.querydsl;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.NumberExpression;

public class NumberExpressionOperation<T extends Number & Comparable<?>>
		extends SimpleExpressionOperation<T> {

	public NumberExpressionOperation(NumberExpression<T> property) {
		super(property);
	}

	public NumberExpression<T> getProperty() {
		return (NumberExpression<T>) property;
	}

	@Override
	public Predicate lt(T value) {
		return getProperty().lt(value);
	}

	@Override
	public Predicate gt(T value) {
		return getProperty().gt(value);
	}

	@Override
	public Predicate loe(T value) {
		return getProperty().loe(value);
	}

	@Override
	public Predicate goe(T value) {
		return getProperty().goe(value);
	}
}
