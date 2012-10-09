package com.bnpparibas.eqd.prs.where.builder;

import com.bnpparibas.eqd.prs.where.builder.operation.BinaryOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.MultipleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.NoValueOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.SimpleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.StringOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.querydsl.SimpleExpressionOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.sql.SqlOperation;

public interface OperationVisitor {

	void visit(NoValueOperation operation);

	void visit(SimpleOperation<?> operation);

	void visit(StringOperation operation);

	void visit(MultipleOperation<?> operation);

	void visit(BinaryOperation operation);

	void visit(SqlOperation operation);

	void visit(SimpleExpressionOperation<?> operation);
}
