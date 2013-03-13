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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.emo.ananas.configs.CronConfig;
import com.emo.ananas.configs.EmailConfig;
import com.emo.ananas.configs.EmailSenderConfig;
import com.emo.ananas.configs.QueryConfig;
import com.emo.ananas.executors.QueryReportExecutor;
import com.emo.ananas.reporters.EmailReporter;

public class Report {

	private final QueryReportExecutor executor;
	private final CronConfig cron;
	private final ThreadPoolTaskScheduler scheduler;
	
	private final String reportName;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Report(final String reportName, final ThreadPoolTaskScheduler scheduler, final EmailSenderConfig emailSender, final EmailConfig email, final CronConfig cron, final QueryConfig query) {
		
		final EmailReporter reporter = new EmailReporter(emailSender, email);
		
		this.executor = new QueryReportExecutor(reportName, query.generator(), reporter);
		this.cron = cron;
		this.scheduler = scheduler;
		this.reportName = reportName;
	}
	
	public void schedule() {
		logger.info("sheduling report {} for {}", reportName, cron.trigger.getExpression());
		scheduler.schedule(executor, cron.trigger);
		
	}
}
