package com.bnpparibas.eqd.prs.where.test;

import static com.bnpparibas.eqd.prs.where.builder.OperationBuilder.Eq;
import static com.bnpparibas.eqd.prs.where.builder.OperationBuilder.Goe;
import static com.bnpparibas.eqd.prs.where.builder.OperationBuilder.In;
import static com.bnpparibas.eqd.prs.where.builder.OperationBuilder.IsEmpty;
import static com.bnpparibas.eqd.prs.where.builder.OperationBuilder.IsNull;
import static com.bnpparibas.eqd.prs.where.builder.OperationBuilder.IsTrue;
import static com.bnpparibas.eqd.prs.where.builder.OperationBuilder.Like;
import static com.bnpparibas.eqd.prs.where.builder.OperationBuilder.Lt;
import static com.bnpparibas.eqd.prs.where.builder.OperationBuilder.Ne;
import static com.bnpparibas.eqd.prs.where.builder.OperationBuilder.NotIn;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.bnpparibas.eqd.prs.where.WhereClauseAppender;
import com.bnpparibas.eqd.prs.where.builder.Operation;
import com.bnpparibas.eqd.prs.where.builder.context.DefaultFormatContext;
import com.mysema.query.jpa.impl.JPAQuery;

public class SearchTest {

	private Date date;

	private Search search;

	@Before
	public void init() {
		date = new Date();

		search = new Search();

		search.setBooleanColumn("is true");
		search.setDateColumn(">= " + new DefaultFormatContext().getDateFormat().format(date));
		search.setDoubleColumn("(< 10 or >= 123.456 or in (12, 13.14)) and not in (5.5, 555)");
		search.setStringColumn("like youpi% and != youpi0 or is empty");
		search.setLegType("in (USER, GROUP) or is null");
	}

	@Test
	public void test() {
		WhereClauseAppender appender = null;

		appender = new WhereClauseAppender(new JPAQuery().from(QRootEntity.rootEntity));

		appender.search(QRootEntity.rootEntity.booleanColumn, search.getBooleanColumn());
		appender.search(QRootEntity.rootEntity.dateColumn, search.getDateColumn());
		appender.search(QRootEntity.rootEntity.doubleColumn, search.getDoubleColumn());
		appender.search(QRootEntity.rootEntity.stringColumn, search.getStringColumn());
		appender.search(QLegEntity.legEntity.type, search.getLegType(),
				appender.innerJoin(QRootEntity.rootEntity.legs, QLegEntity.legEntity),
				appender.innerJoin(QRootEntity.rootEntity.legs, QLegEntity.legEntity));

		JPAQuery query1 = appender.append();

		System.out.println(query1);

		appender = new WhereClauseAppender(new JPAQuery().from(QRootEntity.rootEntity));

		appender.search(QRootEntity.rootEntity.booleanColumn, IsTrue());
		appender.search(QRootEntity.rootEntity.dateColumn, Goe(date));
		appender.search(QRootEntity.rootEntity.doubleColumn,
				Lt(10).Or(Goe(123.456)).Or(In(12d, 13.14)).And(NotIn(5.5, 555d)));
		appender.search(QRootEntity.rootEntity.stringColumn, Like("youpi%").And(Ne("youpi0")).Or(IsEmpty()));
		appender.search(QLegEntity.legEntity.type, In(LegType.GROUP, LegType.USER).Or(IsNull()),
				appender.innerJoin(QRootEntity.rootEntity.legs, QLegEntity.legEntity),
				appender.innerJoin(QRootEntity.rootEntity.legs, QLegEntity.legEntity));

		JPAQuery query2 = appender.append();

		System.out.println(query2);

		assertEquals(query2.toString(), query1.toString());
	}

	@Test
	public void testVoid() {
		WhereClauseAppender appender = null;

		appender = new WhereClauseAppender(new JPAQuery().from(QRootEntity.rootEntity));

		final String expected = appender.append().toString();

		appender.search(QRootEntity.rootEntity.booleanColumn, (String) null);
		appender.search(QRootEntity.rootEntity.dateColumn, "");
		appender.search(QRootEntity.rootEntity.doubleColumn, " ");
		appender.search(QRootEntity.rootEntity.stringColumn, "");
		appender.search(QLegEntity.legEntity.type, "",
				appender.innerJoin(QRootEntity.rootEntity.legs, QLegEntity.legEntity),
				appender.innerJoin(QRootEntity.rootEntity.legs, QLegEntity.legEntity));

		JPAQuery query1 = appender.append();

		System.out.println(query1);

		assertEquals(expected, query1.toString());

		appender = new WhereClauseAppender(new JPAQuery().from(QRootEntity.rootEntity));

		appender.search(QRootEntity.rootEntity.booleanColumn, (Operation) null);
		appender.search(QRootEntity.rootEntity.dateColumn, Eq(null).And(Eq(null)));
		appender.search(QRootEntity.rootEntity.doubleColumn, In(new Object[0]));
		appender.search(QRootEntity.rootEntity.stringColumn, Like(null));
		appender.search(QLegEntity.legEntity.type, Eq(null),
				appender.innerJoin(QRootEntity.rootEntity.legs, QLegEntity.legEntity),
				appender.innerJoin(QRootEntity.rootEntity.legs, QLegEntity.legEntity));

		JPAQuery query2 = appender.append();

		System.out.println(query2);

		assertEquals(expected, query2.toString());
	}
}
