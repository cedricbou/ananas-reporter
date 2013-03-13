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

import javax.sql.DataSource;

import com.emo.ananas.datasources.DataSourceFactory;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

public class BaseConfig {

	private final DataSourceFactory factory;
	private final String dataSourceName;
	
	public BaseConfig(final Config config, final DataSourceFactory factory) {
		this.factory = factory;
		Preconditions.checkArgument(config.hasPath("ds"), "reports.{name}.ds must contain a valid datasource name in config");
		this.dataSourceName = config.getString("ds");
	}
	
	public DataSource dataSource() {
		return factory.build(dataSourceName);
	}
}
