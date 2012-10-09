package com.bnpparibas.eqd.prs.where.builder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.bnpparibas.eqd.prs.where.builder.context.SqlEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.context.SqlEvaluationContext.ClauseParameter;
import com.bnpparibas.eqd.prs.where.builder.operation.BinaryOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.MultipleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.SimpleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.sql.SqlOperation;
import com.bnpparibas.eqd.prs.where.builder.type.BinaryOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.MultipleOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.SimpleOperatorType;

public class SqlOperationsTest {

	@Test
	public void simple() {
		SqlOperation propertyOperation = new SqlOperation(
				createSimpleOperation(), "myAlias", "myColumn", Number.class);
		SqlEvaluationContext context = new SqlEvaluationContext();

		propertyOperation.evaluate(context);

		String clause = context.getClause();

		assertNotNull(clause);
		assertEquals("myAlias.myColumn = :myColumn0", clause);

		checkParameterNames(context, 1, "myColumn0");
		checkParameterValues(context, 1, Integer.valueOf(1));
	}

	@Test
	public void multiple() {
		SqlOperation propertyOperation = new SqlOperation(
				createMultipleOperation(), null, "myProperty", Number.class);
		SqlEvaluationContext context = new SqlEvaluationContext();

		propertyOperation.evaluate(context);

		String clause = context.getClause();

		assertNotNull(clause);
		assertEquals("myProperty in ( :myProperty0 )", clause);

		checkParameterNames(context, 1, "myProperty0");
		checkParameterValues(
				context,
				1,
				(Object) new Integer[] { Integer.valueOf(3), Integer.valueOf(5) });
	}

	@Test
	public void binaryExpression() {
		SqlOperation propertyOperation = new SqlOperation(
				new BinaryOperation(BinaryOperatorType.OR,
						createSimpleOperation(), createMultipleOperation()),
				null, "myProperty", Number.class);
		SqlEvaluationContext context = new SqlEvaluationContext();

		propertyOperation.evaluate(context);

		String clause = context.getClause();

		assertNotNull(clause);
		assertEquals(
				"( myProperty = :myProperty0 or myProperty in ( :myProperty1 ) )",
				clause);

		checkParameterNames(context, 2, "myProperty0", "myProperty1");
		checkParameterValues(context, 2, Integer.valueOf(1), new Integer[] {
				Integer.valueOf(3), Integer.valueOf(5) });
	}

	private Operation createSimpleOperation() {
		return new SimpleOperation<Integer>(SimpleOperatorType.EQ,
				Integer.valueOf(1));
	}

	private Operation createMultipleOperation() {
		return MultipleOperation.build(MultipleOperatorType.IN,
				Integer.valueOf(3), Integer.valueOf(5));
	}

	private void checkParameterNames(Iterable<ClauseParameter> parameters,
			final int expected, String... names) {
		int result = 0;

		for (ClauseParameter parameter : parameters) {
			assertEquals(names[result], parameter.getName());

			++result;
		}

		assertEquals(expected, result);
	}

	private void checkParameterValues(Iterable<ClauseParameter> parameters,
			final int expected, Object... values) {
		int result = 0;

		for (ClauseParameter parameter : parameters) {
			final Object object = values[result];

			if (object.getClass().isArray()) {
				assertArrayEquals((Object[]) object,
						(Object[]) parameter.getValue());
			} else {
				assertEquals(object, parameter.getValue());
			}

			++result;
		}

		assertEquals(expected, result);
	}
}
