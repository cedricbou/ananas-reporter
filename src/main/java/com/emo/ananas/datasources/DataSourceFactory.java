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
package com.emo.ananas.datasources;

import javax.sql.DataSource;

import com.emo.ananas.configs.DataSourceConfig;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

public class DataSourceFactory {

	private final Config config;
	
	public DataSourceFactory(final Config config) {
		this.config = config;
	}
	
	public DataSource build(final String name) {
		Preconditions.checkArgument(config.hasPath(name), "expected datasources." + name + " to be defined in config");
		final Config config = this.config.getConfig(name);
	
		final DataSourceConfig dsConfig = new DataSourceConfig(config);
	
		return dsConfig.build();
	}
}
