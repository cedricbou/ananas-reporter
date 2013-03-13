/*
 * Copyright 2013 Cédric Boufflers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package com.emo.ananas.configs;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

public class EmailConfig {

	public final List<String> to;
	public final String from;
	public final String subject;
	
	public EmailConfig(final Config config) {
		Preconditions.checkArgument(config.hasPath("to"), "report must have a report.to defined");
		Preconditions.checkArgument(config.getStringList("to").size() >= 1, "report must have at least one recipient as an array in report.to");
		Preconditions.checkArgument(config.hasPath("from"), "report must have a sender in report.from");
		Preconditions.checkArgument(config.hasPath("subject"), "report must have a subject, it can contain a date format as defined by JodaTime");
		
		final String rawSubject = config.getString("subject");
		
		final Pattern pattern = Pattern.compile("\\%d\\{([^\\}]+)\\}");
		final Matcher matcher = pattern.matcher(rawSubject);
		
	    final DateTime dt = new DateTime();

		final StringBuffer sb = new StringBuffer();

		while (matcher.find()) {
		    final String datePattern = matcher.group(1);

			final DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);
			final String formattedDate = fmt.print(dt);

			matcher.appendReplacement(sb, formattedDate);
		}
		matcher.appendTail(sb);

		this.to = Collections.unmodifiableList(config.getStringList("to"));
		this.from = config.getString("from");
		this.subject = sb.toString();
	}
	
	@Override
	public String toString() {
		return "email from " + from + " to " + Joiner.on(", ").join(to) + " with subject " + subject;
	}
}
