package com.bnpparibas.eqd.prs.where.builder;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bnpparibas.eqd.prs.where.builder.context.QueryDslEvaluationContext;
import com.bnpparibas.eqd.prs.where.builder.operation.BinaryOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.MultipleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.SimpleOperation;
import com.bnpparibas.eqd.prs.where.builder.type.BinaryOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.MultipleOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.SimpleOperatorType;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.NumberPath;

public class QueryDslOperationsTest {

	private QueryDslEvaluationContext context;

	@Before
	public void init() {
		context = new QueryDslEvaluationContext();

		context.setExpression(new NumberPath<Integer>(Integer.class,
				"myProperty"));
	}

	@After
	public void clean() {
		context.unsetExpression();
	}

	@Test
	public void simple() {
		createSimpleOperation().evaluate(context);

		checkPredicates(context, "myProperty = 1");
	}

	@Test
	public void multiple() {
		createMultipleOperation().evaluate(context);

		checkPredicates(context, "myProperty in [3, 5]");
	}

	@Test
	public void binaryExpression() {
		new BinaryOperation(BinaryOperatorType.OR,
				createSimpleOperation(), createMultipleOperation())
				.evaluate(context);

		checkPredicates(context, "myProperty = 1 || myProperty in [3, 5]");
	}

	private Operation createSimpleOperation() {
		return new SimpleOperation<Integer>(SimpleOperatorType.EQ,
				Integer.valueOf(1));
	}

	private Operation createMultipleOperation() {
		return MultipleOperation.build(MultipleOperatorType.IN,
				Integer.valueOf(3), Integer.valueOf(5));
	}

	private void checkPredicates(Iterable<Predicate> predicates,
			String... values) {
		int result = 0;
		int expected = values.length;

		for (Predicate predicate : predicates) {
			assertEquals(values[result], predicate.toString());

			++result;
		}

		assertEquals(expected, result);
	}
}
