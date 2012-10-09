package com.bnpparibas.eqd.prs.where.builder.operation;

import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.FormatContext;

public abstract class OperationDecorator implements Operation {

	protected final Operation delegate;

	public OperationDecorator(Operation delegate) {
		this.delegate = delegate;
	}

	@Override
	public boolean isValid() {
		return delegate.isValid();
	}

	@Override
	public String toPrettyString(FormatContext context) {
		return delegate.toPrettyString(context);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
}
