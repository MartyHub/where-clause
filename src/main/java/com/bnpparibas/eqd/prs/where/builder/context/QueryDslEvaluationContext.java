package com.bnpparibas.eqd.prs.where.builder.context;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import com.bnpparibas.eqd.prs.where.builder.EvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.operation.BinaryOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.MultipleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.NoValueOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.SimpleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.StringOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.querydsl.BooleanExpressionOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.querydsl.ComparableExpressionOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.querydsl.NumberExpressionOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.querydsl.SimpleExpressionOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.querydsl.StringExpressionOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.sql.SqlOperation;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;

public class QueryDslEvaluationContext implements EvaluationContext, Iterable<Predicate> {

	private final Deque<Predicate> predicates = new ArrayDeque<Predicate>();

	private SimpleExpressionOperation<?> operation;

	@Override
	public void visit(NoValueOperation operation) {
		append(operation.evaluate(this));
	}

	@Override
	public void visit(SimpleOperation<?> operation) {
		append(operation.evaluate(this));
	}

	@Override
	public void visit(StringOperation operation) {
		append(operation.evaluate(this));
	}

	@Override
	public void visit(MultipleOperation<?> operation) {
		append(operation.evaluate(this));
	}

	@Override
	public void visit(BinaryOperation operation) {
		operation.getOperation1().evaluate(this);
		operation.getOperation2().evaluate(this);

		switch (operation.getOperator()) {
		case AND:
			Predicate right = popLastPredicate();
			Predicate left = popLastPredicate();

			append(ExpressionUtils.and(left, right));

			break;
		case OR:
			right = popLastPredicate();
			left = popLastPredicate();

			append(ExpressionUtils.or(left, right));

			break;
		default:
			throw new IllegalArgumentException("Don't know how to handle <" + operation.getOperator() + ">");
		}
	}

	@Override
	public void visit(SimpleExpressionOperation<?> operation) {
		operation.evaluate(this);
	}

	@Override
	public void visit(SqlOperation operation) {
		throw new IllegalArgumentException("QueryDslEvaluationContext can't handle SqlOperation");
	}

	public void setExpression(BooleanExpression expression) {
		this.operation = new BooleanExpressionOperation(expression);
	}

	public <T extends Comparable<T>> void setExpression(ComparableExpression<T> expression) {
		this.operation = new ComparableExpressionOperation<T>(expression);
	}

	public <T extends Number & Comparable<?>> void setExpression(NumberExpression<T> expression) {
		this.operation = new NumberExpressionOperation<T>(expression);
	}

	public void setExpression(StringExpression expression) {
		this.operation = new StringExpressionOperation(expression);
	}

	@SuppressWarnings("unchecked")
	public <T> SimpleExpressionOperation<T> current() {
		return (SimpleExpressionOperation<T>) operation;
	}

	public void unsetExpression() {
		this.operation = null;
	}

	@Override
	public Iterator<Predicate> iterator() {
		return predicates.iterator();
	}

	private void append(Predicate predicate) {
		predicates.add(predicate);
	}

	private Predicate popLastPredicate() {
		return predicates.removeLast();
	}
}
