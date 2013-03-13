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
package com.emo.ananas.executors;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emo.ananas.csv.CsvGenerator;
import com.emo.ananas.reporters.EmailReporter;

public class QueryReportExecutor implements Runnable {

	private final EmailReporter reporter;
	private final String reportName;
	private final CsvGenerator generator;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public QueryReportExecutor(final String reportName,
			final CsvGenerator generator, final EmailReporter reporter) {
		this.reporter = reporter;
		this.reportName = reportName;
		this.generator = generator;
	}

	public void run() {
		try {
			logger.info("running report {}", reportName);
			reporter.report(reportName, generator.generate());
		} catch (IOException io) {
			logger.error("failed to report {}", reportName, io);
		}
	}
}
