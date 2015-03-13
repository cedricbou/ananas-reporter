package com.emo.ananas.configs;

import com.typesafe.config.Config;

public class PathsConfig {

	public final String templates;
	public final String workspace;
	
	public PathsConfig(final Config config) {
		// ConfigUtils.raise(ConfigUtils.require(config, "templates", "workspace"));
		
		this.templates = config.hasPath("templates")?config.getString("templates"):"./templates";
		this.workspace = config.hasPath("workspace")?config.getString("workspace"):"./workspace";
	}
	
	protected PathsConfig() {
		this.templates = "./templates";
		this.workspace = "./workspace";
	}
}
