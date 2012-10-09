package com.bnpparibas.eqd.prs.where.builder.callback;

import java.util.ArrayList;
import java.util.List;

import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.OperationCallback;
import com.bnpparibas.eqd.prs.where.builder.operation.sql.SqlOperation;
import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class SqlOperationCallback implements OperationCallback {

	private final ListMultimap<Operation, String> intend = ArrayListMultimap
			.create();

	private final List<String> joins = new ArrayList<String>();

	public SqlOperationCallback intend(String type, String sourceTable,
			String sourceAlias, String sourceColumn, String targetTable,
			SqlOperation operation, String targetColumn) {
		String join = Joiner.on(' ').join(type, targetTable,
				operation.getAlias(), "on", sourceAlias + "." + sourceColumn,
				"=", operation.getAlias() + "." + targetColumn);

		return intend(operation, join);
	}

	public SqlOperationCallback intend(Operation operation, String join) {
		intend.put(operation, join);

		return this;
	}

	@Override
	public void process(Operation operation) {
		for (String join : intend.removeAll(operation)) {
			addJoin(join);
		}
	}

	public SqlOperationCallback addJoin(String join) {
		if (!joins.contains(join)) {
			joins.add(join);
		}

		return this;
	}

	public String build() {
		return Joiner.on(' ').join(joins);
	}
}
