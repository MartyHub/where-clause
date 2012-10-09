package com.bnpparibas.eqd.prs.where.builder.context;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bnpparibas.eqd.prs.where.builder.FormatContext;

public class DefaultFormatContext implements FormatContext {

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private final DecimalFormat numberFormat;

	public DefaultFormatContext() {
		numberFormat = new DecimalFormat();

		DecimalFormatSymbols symbols = numberFormat.getDecimalFormatSymbols();

		symbols.setDecimalSeparator('.');

		numberFormat.setDecimalFormatSymbols(symbols);
		numberFormat.setDecimalSeparatorAlwaysShown(false);
		numberFormat.setGroupingUsed(false);
	}

	@Override
	public String format(Object value) {
		if (value == null) {
			return "null";
		} else if (value instanceof Date) {
			return dateFormat.format(value);
		} else if (value instanceof Number) {
			return numberFormat.format(value);
		} else {
			return value.toString();
		}
	}

	@Override
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	@Override
	public NumberFormat getNumberFormat() {
		return numberFormat;
	}
}
