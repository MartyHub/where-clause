package com.bnpparibas.eqd.prs.where.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ErrorLocatingParseRunner;
import org.parboiled.parserunners.ParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.ValueStack;

import com.bnpparibas.eqd.prs.where.builder.FormatContext;
import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.context.DefaultFormatContext;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class OperationsParserTest {

	private OperationsParser parser;

	private ParseRunner<Object> runner;

	private Function<ParseError, String> errorFunction;

	private FormatContext formatContext;

	@Before
	public void init() {
		this.parser = Parboiled.createParser(OperationsParser.class);
		this.runner = new ErrorLocatingParseRunner<Object>(parser.Expression());
		this.errorFunction = new Function<ParseError, String>() {

			@Override
			public String apply(@Nullable ParseError input) {
				if (input != null) {
					return input.getErrorMessage();
				} else {
					return null;
				}
			}
		};
		this.formatContext = new DefaultFormatContext();
	}

	@Test
	public void parserTest() {
		nullTest();
		booleanTest();
		emptyTest();
		likeTest();
		inIntegerTest();
		inDecimalTest();
		inStringTest();
		inDateTest();
		comparatorIntegerTest();
		comparatorDateTest();
		andTest();
		orTest();
		complexTest();
	}

	private void nullTest() {
		checkOneValue(checkOk("is null"), "is null");
		checkOneValue(checkOk("is not null"), "is not null");
	}

	private void booleanTest() {
		checkOneValue(checkOk("is true"), "is true");
		checkOneValue(checkOk("is false"), "is false");
		checkOneValue(checkOk("= true"), "= true");
		checkOneValue(checkOk("= false"), "= false");
		checkOneValue(checkOk("!= true"), "!= true");
		checkOneValue(checkOk("!= false"), "!= false");
	}

	private void emptyTest() {
		checkOneValue(checkOk("is empty"), "is empty");
		checkOneValue(checkOk("is not empty"), "is not empty");
	}

	private void likeTest() {
		checkOneValue(checkOk("like youpi"), "like youpi");
		checkOneValue(checkOk("not like youpi"), "not like youpi");
	}

	private void inIntegerTest() {
		checkOneValue(checkOk("in(1,2)"), "in 1,2");
		checkOneValue(checkOk("in ( -1 , - 2 )"), "in -1,-2");
		checkOneValue(checkOk("not in(11,22)"), "not in 11,22");
		checkOneValue(checkOk("not in ( - 11 , -22 )"), "not in -11,-22");
	}

	private void inDecimalTest() {
		checkOneValue(checkOk("in(11.1,22.2)"), "in 11.1,22.2");
		checkOneValue(checkOk("in ( -11.1 , - 22.2 )"), "in -11.1,-22.2");
		checkOneValue(checkOk("not in(11.1,22.2)"), "not in 11.1,22.2");
		checkOneValue(checkOk("not in ( - 11.1 , -22.2 )"),
				"not in -11.1,-22.2");
	}

	private void inStringTest() {
		checkOneValue(checkOk("in(youpi1,youpi2)"), "in youpi1,youpi2");
		checkOneValue(checkOk("in ( youpi1 , youpi2 )"), "in youpi1,youpi2");
		checkOneValue(checkOk("not in(youpi1,youpi2)"), "not in youpi1,youpi2");
		checkOneValue(checkOk("not in ( youpi1 , youpi2 )"),
				"not in youpi1,youpi2");
	}

	private void inDateTest() {
		checkOneValue(checkOk("in(2012-10-06,2012-10-07)"),
				"in 2012-10-06,2012-10-07");
		checkOneValue(checkOk("in ( 2012-10-06 , 2012-10-07 )"),
				"in 2012-10-06,2012-10-07");
		checkOneValue(checkOk("not in(2012-10-06,2012-10-07)"),
				"not in 2012-10-06,2012-10-07");
		checkOneValue(checkOk("not in ( 2012-10-06 , 2012-10-07 )"),
				"not in 2012-10-06,2012-10-07");
	}

	private void comparatorIntegerTest() {
		checkOneValue(checkOk("<1"), "< 1");
		checkOneValue(checkOk("< - 123.456"), "< -123.456");
	}

	private void comparatorDateTest() {
		checkOneValue(checkOk("<2012-10-06"), "< 2012-10-06");
		checkOneValue(checkOk("< 2012-10-06"), "< 2012-10-06");
	}

	private void andTest() {
		checkOneValue(checkOk(">=2012-09-01 and <2012-10-01"),
				"(>= 2012-09-01 and < 2012-10-01)");
		checkOneValue(checkOk(">= 2012-09-01 and < 2012-10-01"),
				"(>= 2012-09-01 and < 2012-10-01)");

		checkOneValue(checkOk("like y% and like %i and like %ou%"),
				"((like y% and like %i) and like %ou%)");
	}

	private void orTest() {
		checkOneValue(checkOk("=1 or =2 or >10"), "((= 1 or = 2) or > 10)");
	}

	private void complexTest() {
		checkOneValue(checkOk("like y% and (like %ou% or like %i)"),
				"(like y% and (like %ou% or like %i))");
	}

	private ValueStack<Object> checkOk(String input) {
		ParsingResult<Object> result = runner.run(input);

		assertTrue(
				Joiner.on('\n')
						.skipNulls()
						.join(input,
								Iterables.transform(result.parseErrors,
										errorFunction)), result.matched);

		return result.valueStack;
	}

	private void checkOneValue(ValueStack<Object> stack, String expected) {
		assertNotNull(stack);
		assertEquals(1, stack.size());

		Object peek = stack.peek();

		assertTrue(peek instanceof Operation);
		assertEquals(expected, ((Operation) peek).toPrettyString(formatContext));
	}
}
