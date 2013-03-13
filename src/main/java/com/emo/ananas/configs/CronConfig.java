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

import org.springframework.scheduling.support.CronTrigger;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

public class CronConfig {

	public final CronTrigger trigger;
	
	public CronConfig(final Config config) {
		Preconditions.checkArgument(config.hasPath("cron"), "report must have a cron expression in report.cron");
		this.trigger = new CronTrigger(config.getString("cron"));
	}
}
