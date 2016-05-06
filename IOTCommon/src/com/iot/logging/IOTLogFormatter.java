/**
 * Licensed Materials - Property of IOT
 * 

 */
package com.iot.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * 
 */
public class IOTLogFormatter extends Formatter {

	/**
	 * Over ridden method extending from Formatter
	 * 
	 * @return Formatted String
	 */
	@Override
	public String format(LogRecord record) {

		// Format Log message as per Power Model Logging Guidelines
		StringBuffer buf = new StringBuffer();
		try {
			// Append Time
			buf.append("\n");
			// format Date
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:S");
			String formatedDate = dateFormat.format(new Date(record.getMillis()));
			buf.append(formatedDate);

			buf.append("\t");
			String msg = record.getMessage();
			if (msg != null) {
				int index = msg.indexOf("~|");
				if (index > 0) {
					buf.append(msg.substring(0, index));

				}
			}

			// Get transaction id at param[0]
			buf.append("\t");
			Object[] params = record.getParameters();
			if (params != null) {
				if (params.length > 1) {
					if (params[1] != null) {
						buf.append(params[1]);
					} else {
						buf.append("");
					}
				}
			}

			// Get ThreadId
			buf.append("\t");
			buf.append(record.getThreadID());

			// Append Log level
			buf.append("\t");
			buf.append(record.getLevel());

			// Append class name
			buf.append("\t");
			buf.append(record.getSourceClassName());

			// Append method name
			buf.append("\t");
			buf.append(record.getSourceMethodName());

			buf.append("\t");
			// Append log message
			buf.append(msg);

			// Append Throwable stacktrace
			buf.append("\t");
			if (params != null) {
				if (params.length > 0) {
					// convert stackstrace into string
					if (params[0] instanceof Throwable) {
						Throwable throwable = (Throwable) params[0];
						if (throwable.getStackTrace() != null) {
							// append stacktrace
							StackTraceElement[] ste = throwable.getStackTrace();
							buf.append("\n");
							buf.append("\t");
							buf.append(throwable.getLocalizedMessage());
							for (int i = 0; i < ste.length; i++) {
								buf.append("\n");
								buf.append("\t");
								buf.append(ste[i]);
							}
						}
					}
				}
			}
		} catch (Exception exception) {
			System.out.println("Logging Formatter Exception " + exception);
		}
		return buf.toString();
	}

}
