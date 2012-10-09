package com.bnpparibas.eqd.prs.where.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class DateBuilder {

	private final StringBuilder sb = new StringBuilder(10);

	public DateBuilder(String year) {
		sb.append(year);
	}

	public boolean setMonth(String month) {
		sb.append('-');
		sb.append(month);

		return true;
	}

	public boolean setDay(String day) {
		sb.append('-');
		sb.append(day);

		return true;
	}

	public Date toDate(DateFormat isoDateFormat) {
		try {
			return isoDateFormat.parse(sb.toString());
		} catch (ParseException pe) {
			throw new IllegalArgumentException("Failed to parse Date <"
					+ sb.toString() + "> with pattern <yyyy-MM-dd>", pe);
		}
	}
}
