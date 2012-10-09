package com.bnpparibas.eqd.prs.where.builder.operation.sql;

import com.bnpparibas.eqd.prs.where.builder.EvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.FormatContext;
import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.OperationCallback;
import com.bnpparibas.eqd.prs.where.builder.context.SqlEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.operation.OperationDecorator;

public class SqlOperation extends OperationDecorator {

	private final String alias;

	private final String column;

	private final Class<?> type;

	private OperationCallback callback;

	public SqlOperation(Operation delegate, String alias, String column,
			Class<?> type) {
		this(delegate, alias, column, type, null);
	}

	public SqlOperation(Operation delegate, String alias, String column,
			Class<?> type, OperationCallback callback) {
		super(delegate);

		this.alias = alias;
		this.column = column;
		this.type = type;
		this.callback = callback;
	}

	public String getAlias() {
		return alias;
	}

	public String getColumn() {
		return column;
	}

	public Class<?> getType() {
		return type;
	}

	@Override
	public void evaluate(EvaluationContext context) {
		context.visit(this);
	}

	public void evaluate(SqlEvaluationContext context) {
		context.push(this);
		delegate.evaluate(context);
		context.pop();

		processCallback();
	}

	@Override
	public String toPrettyString(FormatContext context) {
		return column + " " + super.toPrettyString(context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((column == null) ? 0 : column.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		SqlOperation other = (SqlOperation) obj;

		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;

		if (column == null) {
			if (other.column != null)
				return false;
		} else if (!column.equals(other.column))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return column + " " + super.toString();
	}

	private void processCallback() {
		if (callback != null) {
			callback.process(this);

			callback = null;
		}
	}
}
