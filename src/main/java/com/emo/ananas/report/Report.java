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
package com.emo.ananas.report;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Report implements Runnable {

	private final String reportName;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final EmailReporter reporter;
	private final List<ExcelGenerator> generators;
	
	public Report(
		final String reportName, 
		final List<ExcelGenerator> generators,
		final EmailReporter reporter) {

		this.generators = generators;
		this.reporter = reporter;
		this.reportName = reportName;
	}
	
	@Override
	public void run() {
		logger.info("executing report {} for {}", reportName);
		
		final List<File> reportings = new LinkedList<File>();
		
		for(final ExcelGenerator gen : generators) {
			try {
			reportings.add(gen.generate());
			} catch(Exception e) {
				logger.error("Failed to generate reporting file in report {}", reportName, e);
			}
		}
		
		reporter.report(reportName, reportings);
	}
}
