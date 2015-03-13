package com.emo.ananas.configs;

import com.typesafe.config.Config;

public class EmailerConfig {

	public final String smtp;
	
	public EmailerConfig(final Config config) {
		ConfigUtils.raise(ConfigUtils.require(config, "smtp"));
		
		this.smtp = config.getString("smtp");
	}
}
