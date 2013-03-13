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
package com.emo.ananas.csv;

import java.util.Arrays;
import java.util.Map;

public class MapToCsvLine {

	private static final String[] EMPTY_STRING_ARRAY = new String[] {};

	private String[] orderedKeys = null;
	
	public MapToCsvLine() {
		
	}
	
	public String[] header(final Map<String, Object> sample) {
		if(orderedKeys != null) {
			return orderedKeys;
		}
		
		final String[] keys = sample.keySet().toArray(EMPTY_STRING_ARRAY);
		Arrays.sort(keys);
		this.orderedKeys = keys;
		
		return orderedKeys;
	}
	
	public String[] line(final Map<String, Object> item) {
		if(orderedKeys == null) {
			header(item);
		}
		
		final String[] values = new String[orderedKeys.length];
		
		int i = 0;
		for(final String key : orderedKeys) {
			final Object value = item.get(key);
			values[i++] = (value != null)?value.toString():"null";
		}
		
		return values;
	}
}
