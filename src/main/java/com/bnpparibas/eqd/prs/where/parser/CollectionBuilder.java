package com.bnpparibas.eqd.prs.where.parser;

import java.util.ArrayList;
import java.util.List;

import com.bnpparibas.eqd.prs.where.builder.operation.MultipleOperation;
import com.bnpparibas.eqd.prs.where.builder.type.MultipleOperatorType;

public class CollectionBuilder {

	private final List<Object> list = new ArrayList<Object>();

	private SupportedType supportedType;

	public boolean start(Object value) {
		Class<?> valueType = value.getClass();

		for (SupportedType supportedType : SupportedType.values()) {
			if (supportedType.getType().isAssignableFrom(valueType)) {
				this.supportedType = supportedType;
				this.list.add(value);

				return true;
			}
		}

		throw new IllegalArgumentException("Don't know how to handle type <"
				+ valueType.getName() + "> for <" + value + ">");
	}

	public boolean add(Object value) {
		Class<?> valueType = value.getClass();

		if (!supportedType.getType().isAssignableFrom(valueType)) {
			throw new IllegalArgumentException("Can't mix <"
					+ valueType.getName() + "> with <"
					+ supportedType.getType() + "> for <" + value + ">");
		}

		this.list.add(value);

		return true;
	}

	public MultipleOperation<?> toOperation(MultipleOperatorType operator) {
		return supportedType.build(operator, list);
	}
}
