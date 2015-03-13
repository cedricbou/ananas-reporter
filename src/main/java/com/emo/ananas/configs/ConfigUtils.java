package com.emo.ananas.configs;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.typesafe.config.Config;

public class ConfigUtils {

	public static void raise(final List<String> errors) {
		if (errors.size() > 0) {
			throw new RuntimeException(Joiner.on("\n").join(
					Lists.transform(errors, new Function<String, String>() {
						@Override
						public String apply(String arg0) {
							return "[ERROR] " + arg0;
						}
					})));
		}
	}
	
	public static final List<String> require(final Config config, final String... fields) {
		final ImmutableList.Builder<String> errors = ImmutableList.<String>builder();
		
		for(final String field : fields) {
			if(!config.hasPath(field)) {
				errors.add(field + " element must be present in configuration");
			}
		}
		
		return errors.build();
	}
	
	public static final List<String> requireThis(final Config config, final String field, final String message) {
		final ImmutableList.Builder<String> errors = ImmutableList.<String>builder();
		
		if(!config.hasPath(field)) {
			errors.add(field + " element must be present in configuration : " + message);
		}
		
		return errors.build();
	}
	
	public static String format(final String template) {
		final Pattern pattern = Pattern.compile("\\%d\\{([^\\}]+)\\}");
		final Matcher matcher = pattern.matcher(template);
		
	    final DateTime dt = new DateTime();

		final StringBuffer sb = new StringBuffer();

		while (matcher.find()) {
		    final String datePattern = matcher.group(1);

			final DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);
			final String formattedDate = fmt.print(dt);

			matcher.appendReplacement(sb, formattedDate);
		}
		
		matcher.appendTail(sb);
		
		return sb.toString();
	}

}
