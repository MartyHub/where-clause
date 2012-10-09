package com.bnpparibas.eqd.prs.where.builder.context;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import com.bnpparibas.eqd.prs.where.builder.EvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.context.SqlEvaluationContext.ClauseParameter;
import com.bnpparibas.eqd.prs.where.builder.operation.BinaryOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.MultipleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.NoValueOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.SimpleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.StringOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.querydsl.SimpleExpressionOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.sql.SqlOperation;
import com.google.common.base.Joiner;

public class SqlEvaluationContext implements EvaluationContext,
		Iterable<ClauseParameter> {

	public static class ClauseParameter {

		private final String name;

		private final Object value;

		private ClauseParameter(String name, Object value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public Object getValue() {
			return value;
		}
	}

	private final Deque<SqlOperation> properties = new ArrayDeque<SqlOperation>(
			1);

	private final List<String> clauses = new ArrayList<String>();

	private final List<ClauseParameter> parameters = new ArrayList<ClauseParameter>();

	private int index = 0;

	@Override
	public void visit(NoValueOperation operation) {
		operation.evaluate(this);
	}

	@Override
	public void visit(SimpleOperation<?> operation) {
		operation.evaluate(this);
	}

	@Override
	public void visit(StringOperation operation) {
		operation.evaluate(this);
	}

	@Override
	public void visit(MultipleOperation<?> operation) {
		operation.evaluate(this);
	}

	@Override
	public void visit(BinaryOperation operation) {
		operation.evaluate(this);
	}

	@Override
	public void visit(SqlOperation operation) {
		operation.evaluate(this);
	}

	@Override
	public void visit(SimpleExpressionOperation<?> operation) {
		throw new IllegalArgumentException(
				"SqlEvaluationContext can't handle JpaOperation");
	}

	public void push(SqlOperation property) {
		properties.addLast(property);
	}

	public SqlOperation current() {
		return properties.getLast();
	}

	public void pop() {
		properties.removeLast();
	}

	public void appendCurrentProperty() {
		SqlOperation operation = current();
		String alias = operation.getAlias();
		StringBuilder sb = new StringBuilder();

		if (alias != null) {
			sb.append(alias);
			sb.append('.');
		}

		sb.append(operation.getColumn());

		append(sb.toString());
	}

	public void append(String clause) {
		clauses.add(clause);
	}

	public void addParameter(Object value) {
		SqlOperation operation = current();

		if (value != null) {
			Class<?> valueType = value.getClass();

			if (valueType.isArray()) {
				valueType = valueType.getComponentType();
			}

			Class<?> type = operation.getType();

			if (!type.isAssignableFrom(valueType)) {
				throw new IllegalArgumentException("Value type <"
						+ valueType.getName() + "> must be assignable from <"
						+ type.getName() + "> for <" + operation + ">");
			}
		}

		String name = operation.getColumn() + index;

		append(":" + name);

		parameters.add(new ClauseParameter(name, value));

		++index;
	}

	public String getClause() {
		return Joiner.on(' ').skipNulls().join(clauses);
	}

	@Override
	public Iterator<ClauseParameter> iterator() {
		return parameters.iterator();
	}
}
