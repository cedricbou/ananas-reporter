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

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.emo.ananas.configs.DataSourceConfig;

public class DataSourceFactory {
	
	public DataSourceFactory() {
	}
	
	public DataSource build(final DataSourceConfig config) {
		if(config.user != null && config.driverClass != null) {
			final SimpleDriverDataSource ds = new SimpleDriverDataSource();
			ds.setUrl(config.url);
			ds.setDriverClass(config.driverClass);
			ds.setUsername(config.user);
			ds.setPassword(config.password);
			
			return ds;
		}
		else if(config.user != null && config.driverClass == null) {
			return new DriverManagerDataSource(config.url, config.user, config.password);
		}
		else if(config.user == null & config.driverClass != null) {
			final SimpleDriverDataSource ds = new SimpleDriverDataSource();
			ds.setUrl(config.url);
			ds.setDriverClass(config.driverClass);
			
			return ds;
		}
		else {
			return new DriverManagerDataSource(config.url);
		}	
	}
}
