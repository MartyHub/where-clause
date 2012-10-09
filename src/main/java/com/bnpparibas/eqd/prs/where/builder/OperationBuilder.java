package com.bnpparibas.eqd.prs.where.builder;

import com.bnpparibas.eqd.prs.where.builder.operation.BinaryOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.MultipleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.NoValueOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.SimpleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.StringOperation;
import com.bnpparibas.eqd.prs.where.builder.type.BinaryOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.MultipleOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.NoValueOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.SimpleOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.StringOperatorType;

public final class OperationBuilder {

	public static final OperationBuilder IsNull() {
		return new OperationBuilder(new NoValueOperation(
				NoValueOperatorType.IS_NULL));
	}

	public static final OperationBuilder IsNotNull() {
		return new OperationBuilder(new NoValueOperation(
				NoValueOperatorType.IS_NOT_NULL));
	}

	public static final OperationBuilder IsTrue() {
		return new OperationBuilder(new NoValueOperation(
				NoValueOperatorType.IS_TRUE));
	}

	public static final OperationBuilder IsFalse() {
		return new OperationBuilder(new NoValueOperation(
				NoValueOperatorType.IS_FALSE));
	}

	public static final OperationBuilder IsEmpty() {
		return new OperationBuilder(new NoValueOperation(
				NoValueOperatorType.IS_EMPTY));
	}

	public static final OperationBuilder IsNotEmpty() {
		return new OperationBuilder(new NoValueOperation(
				NoValueOperatorType.IS_NOT_EMPTY));
	}

	public static final <T> OperationBuilder In(T... values) {
		return new OperationBuilder(MultipleOperation.build(
				MultipleOperatorType.IN, values));
	}

	public static final <T> OperationBuilder NotIn(T... values) {
		return new OperationBuilder(MultipleOperation.build(
				MultipleOperatorType.NOT_IN, values));
	}

	public static final OperationBuilder Like(String value) {
		return new OperationBuilder(new StringOperation(
				StringOperatorType.LIKE, value));
	}

	public static final OperationBuilder NotLike(String value) {
		return new OperationBuilder(new StringOperation(
				StringOperatorType.NOT_LIKE, value));
	}

	public static final <T> OperationBuilder Eq(T value) {
		return new OperationBuilder(new SimpleOperation<T>(
				SimpleOperatorType.EQ, value));
	}

	public static final <T> OperationBuilder Ne(T value) {
		return new OperationBuilder(new SimpleOperation<T>(
				SimpleOperatorType.NE, value));
	}

	public static final <T> OperationBuilder Lt(T value) {
		return new OperationBuilder(new SimpleOperation<T>(
				SimpleOperatorType.LT, value));
	}

	public static final <T> OperationBuilder Gt(T value) {
		return new OperationBuilder(new SimpleOperation<T>(
				SimpleOperatorType.GT, value));
	}

	public static final <T> OperationBuilder Loe(T value) {
		return new OperationBuilder(new SimpleOperation<T>(
				SimpleOperatorType.LOE, value));
	}

	public static final <T> OperationBuilder Goe(T value) {
		return new OperationBuilder(new SimpleOperation<T>(
				SimpleOperatorType.GOE, value));
	}

	private Operation current;

	private OperationBuilder(Operation start) {
		this.current = start;
	}

	public final Operation build() {
		return current;
	}

	public final OperationBuilder And(OperationBuilder builder) {
		current = new BinaryOperation(BinaryOperatorType.AND, current,
				builder.build());

		return this;
	}

	public final OperationBuilder Or(OperationBuilder builder) {
		current = new BinaryOperation(BinaryOperatorType.OR, current,
				builder.build());

		return this;
	}
}
