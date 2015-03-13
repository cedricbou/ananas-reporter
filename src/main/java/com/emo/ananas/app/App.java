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
package com.emo.ananas.app;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.emo.ananas.configs.AnanasConfig;
import com.emo.ananas.configs.ConfigUtils;
import com.emo.ananas.configs.ReportConfig;
import com.emo.ananas.datasources.DataSourceFactory;
import com.emo.ananas.datasources.DataSourcesFactory;
import com.emo.ananas.datasources.PathFactory;
import com.emo.ananas.report.EmailReporter;
import com.emo.ananas.report.ExcelGenerator;
import com.emo.ananas.report.Report;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.typesafe.config.ConfigFactory;

public class App {

	public static void main(String[] args) throws InterruptedException {
		Preconditions.checkArgument(args.length > 0, "expected : configuration file as argument");
		
		final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		final DataSourceFactory dsFactory = new DataSourceFactory();
		
		final AnanasConfig config = new AnanasConfig(ConfigFactory.parseFile(new File(args[0])).withFallback(ConfigFactory.parseString("datasources { _demo: { url:\"jdbc:hsqldb:mem\", user: \"sa\", password: \"\", driver: \"org.hsqldb.jdbc.JDBCDriver\" } }")));
		
		final PathFactory templateFactory = new PathFactory(config.paths.templates);
		final DataSourcesFactory dssFactory = new DataSourcesFactory(config.datasources, dsFactory);
				
		scheduler.initialize();

		try(final Connection con = dsFactory.build(config.datasources.get("_demo")).getConnection()) {
			ScriptUtils.executeSqlScript(con, new EncodedResource(new ClassPathResource("demo.sql")));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		for(final String reportName : config.reports.keySet()) {
			final ReportConfig reportConfig = config.reports.get(reportName);
			final CronTrigger trigger = new CronTrigger(reportConfig.cron);
			
			final List<ExcelGenerator> generators = Lists.transform(reportConfig.templates, new Function<String, ExcelGenerator>() {
				@Override
				public ExcelGenerator apply(String tpl) {
					return new ExcelGenerator(dssFactory.build(reportConfig.datasources), new File(config.paths.workspace), templateFactory.build(tpl));
				}});
			
			final Report report = new Report(reportName,
				generators,
				new EmailReporter(
						config.emailer.smtp,
						reportConfig.to, 
						reportConfig.from, 
						ConfigUtils.format(reportConfig.subject)));
			
			scheduler.schedule(report, trigger);
		}
	
		while(true) {
			Thread.sleep(60000);
		}
	}
}
