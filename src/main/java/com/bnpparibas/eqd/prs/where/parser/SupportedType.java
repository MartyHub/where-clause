package com.bnpparibas.eqd.prs.where.parser;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;

import com.bnpparibas.eqd.prs.where.builder.operation.MultipleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.SimpleOperation;
import com.bnpparibas.eqd.prs.where.builder.type.MultipleOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.SimpleOperatorType;

public enum SupportedType {

	DATE(Date.class), NUMBER(Number.class), BOOLEAN(Boolean.class), STRING(
			String.class);

	public static SimpleOperation<?> build(SimpleOperatorType operator,
			Object value) {
		Class<?> valueType = value.getClass();

		for (SupportedType supportedType : SupportedType.values()) {
			final Class<?> type = supportedType.getType();

			if (type.isAssignableFrom(valueType)) {
				return new SimpleOperation<Object>(operator, type.cast(value));
			}
		}

		throw new IllegalArgumentException("Don't know how to handle type <"
				+ valueType.getName() + "> for <" + value + ">");
	}

	private final Class<?> type;

	SupportedType(Class<?> type) {
		this.type = type;
	}

	public Class<?> getType() {
		return type;
	}

	public MultipleOperation<?> build(MultipleOperatorType operator,
			List<Object> list) {
		Object array = Array.newInstance(type, list.size());
		int index = 0;

		for (Object o : list) {
			Array.set(array, index, o);

			++index;
		}

		return new MultipleOperation<Object>(operator, (Object[]) array);
	}
}
