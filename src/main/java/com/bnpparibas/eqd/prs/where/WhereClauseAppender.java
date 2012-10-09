package com.bnpparibas.eqd.prs.where;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.concurrent.NotThreadSafe;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.ErrorLocatingParseRunner;
import org.parboiled.support.ParsingResult;

import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.OperationBuilder;
import com.bnpparibas.eqd.prs.where.builder.context.QueryDslEvaluationContext;
import com.bnpparibas.eqd.prs.where.parser.OperationsParser;
import com.google.common.collect.Iterables;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;

@NotThreadSafe
public class WhereClauseAppender {

	private final Set<JoinAppender<?>> usedJoins = new HashSet<JoinAppender<?>>();

	private final QueryDslEvaluationContext context = new QueryDslEvaluationContext();

	private final ErrorLocatingParseRunner<Object> runner;

	private final JPAQuery query;

	public WhereClauseAppender(JPAQuery query) {
		this.query = query;

		OperationsParser parser = Parboiled.createParser(OperationsParser.class);

		runner = new ErrorLocatingParseRunner<Object>(parser.Expression());
	}

	public <P> JoinAppender<P> innerJoin(final Path<? extends Collection<P>> target, final Path<P> alias) {
		return new JoinAppender<P>(target, alias) {

			@Override
			void join(JPAQuery query) {
				query.innerJoin(target, alias);
			}
		};
	}

	public <P> JoinAppender<P> leftJoin(final Path<? extends Collection<P>> target, final Path<P> alias) {
		return new JoinAppender<P>(target, alias) {

			@Override
			void join(JPAQuery query) {
				query.leftJoin(target, alias);
			}
		};
	}

	public <P> JoinAppender<P> rightJoin(final Path<? extends Collection<P>> target, final Path<P> alias) {
		return new JoinAppender<P>(target, alias) {

			@Override
			void join(JPAQuery query) {
				query.rightJoin(target, alias);
			}
		};
	}

	public JPAQuery append() {
		return query.where(getPredicates());
	}

	public Predicate[] getPredicates() {
		return Iterables.toArray(context, Predicate.class);
	}

	public WhereClauseAppender search(BooleanExpression expression, String search, JoinAppender<?>... joins) {
		return search(expression, parse(search), joins);
	}

	public WhereClauseAppender search(BooleanExpression expression, OperationBuilder builder,
			JoinAppender<?>... joins) {
		context.setExpression(expression);

		return doSearch(builder.build(), joins);
	}

	public WhereClauseAppender search(BooleanExpression expression, Operation operation,
			JoinAppender<?>... joins) {
		context.setExpression(expression);

		return doSearch(operation, joins);
	}

	public <T extends Number & Comparable<?>> WhereClauseAppender search(NumberExpression<T> expression,
			String search, JoinAppender<?>... joins) {
		return search(expression, parse(search), joins);
	}

	public <T extends Number & Comparable<?>> WhereClauseAppender search(NumberExpression<T> expression,
			OperationBuilder builder, JoinAppender<?>... joins) {
		context.setExpression(expression);

		return doSearch(builder.build(), joins);
	}

	public <T extends Number & Comparable<?>> WhereClauseAppender search(NumberExpression<T> expression,
			Operation operation, JoinAppender<?>... joins) {
		context.setExpression(expression);

		return doSearch(operation, joins);
	}

	public <T extends Comparable<T>> WhereClauseAppender search(ComparableExpression<T> expression,
			String search, JoinAppender<?>... joins) {
		return search(expression, parse(search), joins);
	}

	public <T extends Comparable<T>> WhereClauseAppender search(ComparableExpression<T> expression,
			OperationBuilder builder, JoinAppender<?>... joins) {
		context.setExpression(expression);

		return doSearch(builder.build(), joins);
	}

	public <T extends Comparable<T>> WhereClauseAppender search(ComparableExpression<T> expression,
			Operation operation, JoinAppender<?>... joins) {
		context.setExpression(expression);

		return doSearch(operation, joins);
	}

	public WhereClauseAppender search(StringExpression expression, String search, JoinAppender<?>... joins) {
		return search(expression, parse(search), joins);
	}

	public WhereClauseAppender search(StringExpression expression, OperationBuilder builder,
			JoinAppender<?>... joins) {
		context.setExpression(expression);

		return doSearch(builder.build(), joins);
	}

	public WhereClauseAppender search(StringExpression expression, Operation operation,
			JoinAppender<?>... joins) {
		context.setExpression(expression);

		return doSearch(operation, joins);
	}

	private Operation parse(String s) {
		String safe = trimToEmpty(s);

		if (safe.length() > 0) {
			ParsingResult<Object> result = runner.run(s);

			if (!result.matched) {
				throw new IllegalArgumentException("Failed to parse <" + safe + ">");
			}

			if (result.valueStack.size() != 1) {
				throw new IllegalArgumentException("Parser error");
			}

			return (Operation) result.valueStack.pop();
		}

		return null;
	}

	private String trimToEmpty(String s) {
		if (s == null) {
			return "";
		} else {
			return s.trim();
		}
	}

	private WhereClauseAppender doSearch(Operation operation, JoinAppender<?>... joins) {
		try {
			if (operation != null && operation.isValid()) {
				join(joins);

				operation.evaluate(context);
			}

			return this;
		} finally {
			context.unsetExpression();
		}
	}

	private void join(JoinAppender<?>... joins) {
		if (joins != null) {
			for (JoinAppender<?> join : joins) {
				join(join);
			}
		}
	}

	private void join(JoinAppender<?> join) {
		if (!usedJoins.contains(join)) {
			join.join(query);

			usedJoins.add(join);
		}
	}
}
