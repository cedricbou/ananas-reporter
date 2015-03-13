package com.emo.ananas.datasources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.emo.ananas.configs.DataSourceConfig;

public class DataSourcesFactory {

	private final Map<String, DataSourceConfig> configs;
	private final DataSourceFactory factory;

	public DataSourcesFactory(final Map<String, DataSourceConfig> configs,
			final DataSourceFactory factory) {
		this.configs = configs;
		this.factory = factory;
	}

	public Map<String, DataSource> build(final List<String> ds) {
		final Map<String, DataSource> dss = new HashMap<String, DataSource>();
		for (final String d : ds) {
			dss.put(d, factory.build(configs.get(d)));
		}
		return dss;
	}
}
