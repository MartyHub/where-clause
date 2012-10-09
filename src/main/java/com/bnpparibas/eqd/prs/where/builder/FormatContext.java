package com.bnpparibas.eqd.prs.where.builder;

import java.text.DateFormat;
import java.text.NumberFormat;

public interface FormatContext {

	String format(Object value);

	DateFormat getDateFormat();

	NumberFormat getNumberFormat();
}
