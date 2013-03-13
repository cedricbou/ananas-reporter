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

import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

public class DataSourceConfig {

	private final String url;
	
	private final String user;
	private final String password;
	
	private final Class<Driver> driverClass;
	
	@SuppressWarnings("unchecked")
	public DataSourceConfig(final Config config) {
		Preconditions.checkArgument(config.hasPath("url"), "expected datasources.{name}.url with valid jdbc url in config");
		
		this.url = config.getString("url");
		
		if(config.hasPath("user") || config.hasPath("password")) {
			Preconditions.checkArgument(config.hasPath("user") || config.hasPath("password"), "datasources.{name}.user and datasources.{name}.password must both be specified in config");
			user = config.getString("user");
			password = config.getString("password");
		}
		else {
			user = null;
			password = null;
		}

		if(config.hasPath("driver")) {
			try {
				driverClass = (Class<Driver>)Class.forName(config.getString("driver"));
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("specify a valid driver class in datasources.{name}.driver, class not found : " + config.getString("driver"), e);
			}
		}
		else {
			driverClass = null;
		}
	}
	
	public DataSource build() {
		if(user != null && driverClass != null) {
			final SimpleDriverDataSource ds = new SimpleDriverDataSource();
			ds.setUrl(url);
			ds.setDriverClass(driverClass);
			ds.setUsername(user);
			ds.setPassword(password);
			
			return ds;
		}
		else if(user != null && driverClass == null) {
			return new DriverManagerDataSource(url, user, password);
		}
		else if(user == null & driverClass != null) {
			final SimpleDriverDataSource ds = new SimpleDriverDataSource();
			ds.setUrl(url);
			ds.setDriverClass(driverClass);
			
			return ds;
		}
		else {
			return new DriverManagerDataSource(url);
		}
	}
}
