package com.emo.ananas.config;

import static org.junit.Assert.*;

import org.junit.Test;

import com.emo.ananas.configs.AnanasConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class LoadConfigTest {

	private final static Config FILE_WITH_NO_PATHS = ConfigFactory.parseResources("config.no.paths");
	private final static Config FILE_WITH_PATHS = ConfigFactory.parseResources("config.with.paths");

	
	@Test
	public void defaultPaths() {
		final AnanasConfig config = new AnanasConfig(FILE_WITH_NO_PATHS);
		assertEquals("./templates", config.paths.templates);
		assertEquals("./workspace", config.paths.workspace);
	}

	@Test
	public void configuredPaths() {
		final AnanasConfig config = new AnanasConfig(FILE_WITH_PATHS);
		assertEquals("/tmp/mytemplates", config.paths.templates);
		assertEquals("/tmp/myworkspace", config.paths.workspace);
	}

	@Test
	public void configuredEmailer() {
		final AnanasConfig config = new AnanasConfig(FILE_WITH_PATHS);
		assertEquals("smtp.azerty.fr", config.emailer.smtp);
	}
	
	@Test
	public void configuredDatasources() {
		final AnanasConfig config = new AnanasConfig(FILE_WITH_PATHS);
		assertEquals("johndoe", config.datasources.get("ds1").user);
		assertEquals("azerty09", config.datasources.get("ds1").password);
		assertEquals("jdbc:mysql://localhost/base1", config.datasources.get("ds1").url);
		assertTrue(config.datasources.get("ds1").driverClass != null);
	}
	
	@Test
	public void configuredReport() {
		final AnanasConfig config = new AnanasConfig(FILE_WITH_PATHS);
		
		assertArrayEquals(
			new String[] {"ds1", "ds2"}, 
			config.reports.get("myreport").datasources.toArray(new String[] {}));
		
		assertEquals("* */10 * * * ?", config.reports.get("myreport").cron);
		
	}
	
	
}
