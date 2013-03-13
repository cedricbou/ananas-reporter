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

import com.emo.ananas.csv.CsvGenerator;
import com.emo.ananas.csv.SimpleQueryToCsv;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

public class QueryConfig {

	public final String query;
	
	private final DataSource ds;
	
	public QueryConfig(final Config config, final DataSource ds) {
		Preconditions.checkArgument(config.hasPath("query"), "report.{name}.query must contain a valid SQL query in config");
		this.query = config.getString("query");
		this.ds = ds;
	}
	
	public CsvGenerator generator() {
		return new SimpleQueryToCsv(query, ds);
	}
}
