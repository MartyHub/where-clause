package com.bnpparibas.eqd.prs.where.builder.operation.querydsl;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

public class BooleanExpressionOperation extends
		ComparableExpressionOperation<Boolean> {

	public BooleanExpressionOperation(BooleanExpression property) {
		super(property);
	}

	@Override
	public BooleanExpression getProperty() {
		return (BooleanExpression) property;
	}

	@Override
	public Predicate isTrue() {
		return getProperty().isTrue();
	}

	@Override
	public Predicate isFalse() {
		return getProperty().isFalse();
	}
}
