package com.emo.ananas.csv;

import java.io.File;

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
public class PagedQueryToCsv implements CsvGenerator {

	private final int pageSize = 5000;
	
	public File generate() {
/*
		final File exportFile = new File(new File(
				System.getProperty("java.io.tmpdir")), "ananas-export-"
				+ UUID.randomUUID().toString());

		final CSVWriter writer = new CSVWriter(new FileWriter(exportFile), ';');

		boolean writeHeader = true;

		final long pages = executor.countPages(perPage, values);

		for (int i = 1; i <= pages; ++i) {
			final List<?> items = executor.pagedQuery(i, perPage, values);

			if (items != null && items.size() > 0) {
				if (writeHeader) {
					writer.writeNext(JsonUtils.getRootFieldNames(items.get(0)));
					writeHeader = false;
				}

				for (final Object item : items) {
					final String[] itemProps = JsonUtils
							.getRootValuesAsString(item);
					writer.writeNext(itemProps);
				}
			}
		}

		writer.close();
		
		return exportFile;
*/
		return null;
	}
}
