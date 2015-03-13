package com.emo.ananas.configs;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

public class AnanasConfig {

	public final PathsConfig paths;
	public final EmailerConfig emailer;
	public final Map<String, DataSourceConfig> datasources = new HashMap<String, DataSourceConfig>();
	public final Map<String, ReportConfig> reports = new HashMap<String, ReportConfig>();

	public AnanasConfig(final Config config) {
		ConfigUtils.raise(ConfigUtils.require(config, "emailer", "datasources",
				"reports"));

		if (config.hasPath("paths")) {
			this.paths = new PathsConfig(config.getConfig("paths"));
		} else {
			this.paths = new PathsConfig();
		}
		this.emailer = new EmailerConfig(config.getConfig("emailer"));

		final Config dsConfig = config.getConfig("datasources");

		dsConfig.root().entrySet().forEach(new Consumer<Entry<String, ConfigValue>>() {
			public void accept(Entry<String, ConfigValue> e) {
				datasources.put(
						e.getKey(),
						new DataSourceConfig(dsConfig.getConfig(e.getKey()), e
								.getKey()));
			};
		});

		final Config reportConfig = config.getConfig("reports");

		reportConfig.root().entrySet().forEach(
				new Consumer<Entry<String, ConfigValue>>() {
					public void accept(Entry<String, ConfigValue> e) {
						reports.put(
								e.getKey(),
								new ReportConfig(reportConfig.getConfig(e
										.getKey()), e.getKey()));
					};
				});
	}
}
