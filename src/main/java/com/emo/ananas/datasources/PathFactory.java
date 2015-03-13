package com.emo.ananas.datasources;

import java.io.File;

public class PathFactory {

	private final File base;
	
	public PathFactory(final String base) {
		this.base = new File(base);
	}
	
	public File build(final String file) {
		return new File(base, file);
	}
}
