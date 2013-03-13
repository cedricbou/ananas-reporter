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
package com.emo.ananas.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;

import au.com.bytecode.opencsv.CSVWriter;

public class SimpleQueryToCsv implements CsvGenerator {

	private final String request;
	private final DataSource ds;

	public SimpleQueryToCsv(final String request, final DataSource ds) {
		this.request = request;
		this.ds = ds;
	}

	public File generate() throws IOException {

		final File exportFile = new File(new File(
				System.getProperty("java.io.tmpdir")), "ananas-export-"
				+ UUID.randomUUID().toString() + ".csv");

		CSVWriter writer = new CSVWriter(new FileWriter(exportFile), ';');
		final Handle h = DBI.open(ds);

		try {
			boolean writeHeader = true;

			final Query<Map<String, Object>> query = h.createQuery(request);

			final Iterator<Map<String, Object>> it = query.iterator();

			final MapToCsvLine mapper = new MapToCsvLine();

			while (it.hasNext()) {
				final Map<String, Object> res = it.next();

				if (writeHeader) {
					writer.writeNext(mapper.header(res));
					writeHeader = false;
				}

				writer.writeNext(mapper.line(res));
			}
		} catch (Exception e) {
			throw new IOException("failed to generate csv file from query", e);
			// TODO: Log error;

		} finally {
			h.close();
			writer.close();
		}

		return exportFile;
	}
}
