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

import java.util.List;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.emo.ananas.configs.CronConfig;
import com.emo.ananas.configs.BaseConfig;
import com.emo.ananas.configs.EmailConfig;
import com.emo.ananas.configs.EmailSenderConfig;
import com.emo.ananas.configs.QueryConfig;
import com.emo.ananas.datasources.DataSourceFactory;
import com.emo.ananas.report.Report;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class App {

	public static void main(String[] args) throws InterruptedException {
		final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		
		Config config = ConfigFactory.load();
		
		Preconditions.checkArgument(config.hasPath("emailer"), "expected an 'emailer' object in config");
		final Config emailerConfig = config.getConfig("emailer");
		Preconditions.checkArgument(emailerConfig.hasPath("smtp"), "expected emailer.smtp in config");
		
		EmailSenderConfig emailSenderConfig = new EmailSenderConfig(emailerConfig.getString("smtp"));
	
		Preconditions.checkArgument(config.hasPath("reports"), "expected reports object in config");
		final Config reportsConfig = config.getConfig("reports");
		Preconditions.checkArgument(reportsConfig.hasPath("actives"), "expected reports.actives with array of active declared reports in config");
		final List<String> activeReports = reportsConfig.getStringList("actives");

		final DataSourceFactory factory = new DataSourceFactory(config.getConfig("datasources"));
		
		for(final String activeReport : activeReports) {
			Preconditions.checkArgument(reportsConfig.hasPath(activeReport), "expected reports." + activeReport + " in config, because it is declared in actives report list");
		}
		
		scheduler.initialize();
		
		for(final String activeReport : activeReports) {
			final Config reportConfig = reportsConfig.getConfig(activeReport);
			final CronConfig cronConfig = new CronConfig(reportConfig);
			final EmailConfig emailConfig = new EmailConfig(reportConfig);
			final BaseConfig dataSource = new BaseConfig(reportConfig, factory);
			final QueryConfig queryConfig = new QueryConfig(reportConfig, dataSource.dataSource());
			final Report report = new Report(activeReport, scheduler, emailSenderConfig, emailConfig, cronConfig, queryConfig);
			report.schedule();
		}
	
		while(true) {
			Thread.sleep(60000);
		}
	}
}
