package com.bnpparibas.eqd.prs.where.builder.operation.querydsl;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.StringExpression;

public class StringExpressionOperation extends
		ComparableExpressionOperation<String> {

	public StringExpressionOperation(StringExpression property) {
		super(property);
	}

	@Override
	public StringExpression getProperty() {
		return (StringExpression) property;
	}

	@Override
	public Predicate like(String value) {
		return getProperty().like(value);
	}

	@Override
	public Predicate notLike(String value) {
		return getProperty().notLike(value);
	}

	@Override
	public Predicate isEmpty() {
		return getProperty().isEmpty();
	}

	@Override
	public Predicate isNotEmpty() {
		return getProperty().isNotEmpty();
	}
}
