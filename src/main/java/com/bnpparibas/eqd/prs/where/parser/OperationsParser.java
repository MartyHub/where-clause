package com.bnpparibas.eqd.prs.where.parser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.annotation.concurrent.NotThreadSafe;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.Var;

import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.operation.BinaryOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.MultipleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.NoValueOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.SimpleOperation;
import com.bnpparibas.eqd.prs.where.builder.operation.StringOperation;
import com.bnpparibas.eqd.prs.where.builder.type.BinaryOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.MultipleOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.NoValueOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.SimpleOperatorType;
import com.bnpparibas.eqd.prs.where.builder.type.StringOperatorType;

@NotThreadSafe
public class OperationsParser extends BaseParser<Object> {

	final DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	Rule EqualOperator() {
		return FirstOf(Ch('='), String("!="));
	}

	Rule CompareOperator() {
		return FirstOf(String("<="), Ch('<'), String(">="), Ch('>'));
	}

	Rule Digit() {
		return CharRange('0', '9');
	}

	Rule FourDigits() {
		return Sequence(Digit(), Digit(), Digit(), Digit());
	}

	Rule TwoDigits() {
		return Sequence(Digit(), Digit());
	}

	Rule Date() {
		Var<DateBuilder> v = new Var<DateBuilder>();

		return Sequence(FourDigits(), v.set(new DateBuilder(match())), Ch('-'),
				TwoDigits(), v.get().setMonth(match()), Ch('-'), TwoDigits(), v
						.get().setDay(match()),
				push(v.get().toDate(isoDateFormat)));
	}

	Rule Negative() {
		return Sequence(Ch('-'), OptionalWhitespace());
	}

	Rule Integer() {
		return OneOrMore(Digit());
	}

	Rule Decimal() {
		return Sequence(Ch('.'), Integer());
	}

	Rule Number() {
		Var<NumberBuilder> v = new Var<NumberBuilder>(new NumberBuilder());

		return Sequence(Optional(Negative(), v.get().negative()), Integer(), v
				.get().appendInteger(match()),
				Optional(Decimal(), v.get().appendDecimal(match())), push(v
						.get().toNumber()));
	}

	Rule True() {
		return Sequence(String("true"), push(Boolean.TRUE));
	}

	Rule False() {
		return Sequence(String("false"), push(Boolean.FALSE));
	}

	Rule String() {
		return Sequence(OneOrMore(NoneOf(" ,()")), push(match()));
	}

	Rule Value() {
		return FirstOf(Date(), Number(), String());
	}

	Rule ValueWithBoolean() {
		return FirstOf(True(), False(), Value());
	}

	Rule Whitespace() {
		return OneOrMore(Ch(' '));
	}

	Rule OptionalWhitespace() {
		return ZeroOrMore(Ch(' '));
	}

	Rule NotNullOperator() {
		return Sequence(String("not"), Whitespace(), String("null"),
				push(NoValueOperatorType.IS_NOT_NULL));
	}

	Rule NullOperator() {
		return Sequence(String("null"), push(NoValueOperatorType.IS_NULL));
	}

	Rule TrueOperator() {
		return Sequence(String("true"), push(NoValueOperatorType.IS_TRUE));
	}

	Rule FalseOperator() {
		return Sequence(String("false"), push(NoValueOperatorType.IS_FALSE));
	}

	Rule NotEmptyOperator() {
		return Sequence(String("not"), Whitespace(), String("empty"),
				push(NoValueOperatorType.IS_NOT_EMPTY));
	}

	Rule EmptyOperator() {
		return Sequence(String("empty"), push(NoValueOperatorType.IS_EMPTY));
	}

	Rule NoValueOperation() {
		return Sequence(
				String("is"),
				Whitespace(),
				FirstOf(NotNullOperator(), NullOperator(), TrueOperator(),
						FalseOperator(), NotEmptyOperator(), EmptyOperator()),
				pushNoValueOperation());
	}

	Rule NotInOperator() {
		return Sequence(String("not"), Whitespace(), String("in"),
				push(MultipleOperatorType.NOT_IN));
	}

	Rule InOperator() {
		return Sequence(String("in"), push(MultipleOperatorType.IN));
	}

	Rule Collection(Var<CollectionBuilder> v) {
		return Sequence(
				Value(),
				v.get().start(pop()),
				ZeroOrMore(Sequence(OptionalWhitespace(), Ch(','),
						OptionalWhitespace(), Value(), v.get().add(pop()))));
	}

	Rule In() {
		Var<CollectionBuilder> v = new Var<CollectionBuilder>();

		return Sequence(FirstOf(NotInOperator(), InOperator()),
				v.set(new CollectionBuilder()), OptionalWhitespace(), Ch('('),
				OptionalWhitespace(), Collection(v), OptionalWhitespace(),
				Ch(')'), pushMultipleOperation(v.get()));
	}

	Rule NotLikeOperator() {
		return Sequence(String("not"), Whitespace(), String("like"),
				push(StringOperatorType.NOT_LIKE));
	}

	Rule LikeOperator() {
		return Sequence(String("like"), push(StringOperatorType.LIKE));
	}

	Rule Like() {
		return Sequence(FirstOf(NotLikeOperator(), LikeOperator()),
				Whitespace(), String(), pushStringOperation());
	}

	Rule Equal() {
		return Sequence(EqualOperator(), pushSimpleOperatorType(),
				OptionalWhitespace(), ValueWithBoolean(), pushSimpleOperation());
	}

	Rule Compare() {
		return Sequence(CompareOperator(), pushSimpleOperatorType(),
				OptionalWhitespace(), Value(), pushSimpleOperation());
	}

	Rule ConditionRightHandSide() {
		return FirstOf(Equal(), Compare(), NoValueOperation(), Like(), In());
	}

	Rule Condition() {
		return FirstOf(
				Sequence(Ch('('), OptionalWhitespace(), Or(),
						OptionalWhitespace(), Ch(')')),
				ConditionRightHandSide());
	}

	Rule And() {
		return Sequence(
				Condition(),
				ZeroOrMore(Sequence(Whitespace(), String("and"), Whitespace(),
						Condition(),
						pushBinaryOperation(BinaryOperatorType.AND))));
	}

	Rule Or() {
		return Sequence(
				And(),
				ZeroOrMore(Sequence(Whitespace(), String("or"), Whitespace(),
						And(), pushBinaryOperation(BinaryOperatorType.OR))));
	}

	public Rule Expression() {
		return Sequence(Or(), EOI);
	}

	boolean pushSimpleOperatorType() {
		String value = match();
		SimpleOperatorType operator = SimpleOperatorType.fromSymbol(value);

		return push(operator);
	}

	boolean pushMultipleOperation(CollectionBuilder builder) {
		MultipleOperatorType operator = (MultipleOperatorType) pop();
		MultipleOperation<?> operation = builder.toOperation(operator);

		return push(operation);
	}

	boolean pushStringOperation() {
		String value = (String) pop();
		StringOperatorType operator = (StringOperatorType) pop();
		StringOperation operation = new StringOperation(operator, value);

		return push(operation);
	}

	boolean pushSimpleOperation() {
		Object value = pop();
		SimpleOperatorType operator = (SimpleOperatorType) pop();
		SimpleOperation<?> operation = SupportedType.build(operator, value);

		return push(operation);
	}

	boolean pushNoValueOperation() {
		NoValueOperatorType operator = (NoValueOperatorType) pop();
		NoValueOperation operation = new NoValueOperation(operator);

		return push(operation);
	}

	boolean pushBinaryOperation(BinaryOperatorType operator) {
		Operation operation2 = (Operation) pop();
		Operation operation1 = (Operation) pop();
		BinaryOperation operation = new BinaryOperation(
				operator, operation1, operation2);

		return push(operation);
	}
}
