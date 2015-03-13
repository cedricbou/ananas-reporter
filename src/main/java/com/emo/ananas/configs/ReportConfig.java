package com.emo.ananas.configs;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.typesafe.config.Config;

public class ReportConfig {

	public final List<String> datasources;
	public final String cron;
	public final List<String> templates;
	public final List<String> to;
	public final String from;
	public final String subject;
	
	
	public ReportConfig(final Config config, final String name) {
		final String message =  "in report '" + name + "'";
		ConfigUtils.raise(
			ImmutableList.<String>builder()
				.addAll(ConfigUtils.requireThis(config, "datasources", message))
				.addAll(ConfigUtils.requireThis(config, "cron", message))
				.addAll(ConfigUtils.requireThis(config, "templates", message))
				.addAll(ConfigUtils.requireThis(config, "to", message))
				.addAll(ConfigUtils.requireThis(config, "from", message))
				.addAll(ConfigUtils.requireThis(config, "subject", message)).build());
		
		this.datasources = config.getStringList("datasources");
		this.cron = config.getString("cron");
		this.templates = config.getStringList("templates");
		this.to = config.getStringList("to");
		this.from = config.getString("from");
		this.subject = config.getString("subject");

	}
}
