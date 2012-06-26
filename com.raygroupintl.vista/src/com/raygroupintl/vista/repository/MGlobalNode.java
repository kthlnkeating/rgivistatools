//---------------------------------------------------------------------------
// Copyright 2012 Ray Group International
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//---------------------------------------------------------------------------

package com.raygroupintl.vista.repository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.Transformer;

public class MGlobalNode {
	private final static Logger LOGGER = Logger.getLogger(MGlobalNode.class.getName());

	private String name;
	private String value;
	private Map<String, MGlobalNode> subScripts;

	public MGlobalNode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public MGlobalNode getNode(String... subscripts) {
		MGlobalNode valueHolder = this;
		for (int i=0; i<subscripts.length-1; ++i) {
			if (valueHolder.subScripts == null) return null;
			valueHolder = valueHolder.subScripts.get(subscripts[i]);
			if (valueHolder == null) return null;
		}
		return valueHolder;
	}
	
	public String getValue(String... subscripts) {
		MGlobalNode valueHolder = this.getNode(subscripts);
		if (valueHolder == null) {
			return null;
		} else {
			return valueHolder.value;
		}
	}
	
	public <T> List<T> getValues(Filter<String> keyFilter, Transformer<String, T> transformer) {
		List<T> result = null;
		if (this.subScripts != null) {
			Set<String> names = this.subScripts.keySet();
			for (String name : names) {
				if (keyFilter.isValid(name)) {
					MGlobalNode subNode = this.subScripts.get(name);
					String value = subNode.value;
					T transformedValue = transformer.transform(value);
					if (transformedValue != null) {
						if (result == null) {
							result = new ArrayList<T>();
						}
						result.add(transformedValue);
					}
				}
			}			
		}
		return result;
	}
	
	public void setValue(String... values) {
		MGlobalNode valueHolder = this;
		for (int i=0; i<values.length-1; ++i) {
			String value = values[i];
			if (valueHolder.subScripts == null) {
				valueHolder.subScripts = new TreeMap<String, MGlobalNode>();
			}
			MGlobalNode nextValueHolder = valueHolder.subScripts.get(value);
			if (nextValueHolder == null) {
				nextValueHolder = new MGlobalNode(value);
				valueHolder.subScripts.put(value, nextValueHolder);
			}
			valueHolder = nextValueHolder;
		}
		valueHolder.value = values[values.length-1];
	}
	
	private String getWithNoQuote(String value) {
		if (value.charAt(0) == '"') {
			return value.substring(0, value.length()-1);
		} else {
			return value;
		}
	}
	
	private void read(String name, String[] indices, String value) {
		String[] fullValues = new String[indices.length + 2];
		fullValues[0] = getWithNoQuote(name);
		for (int i=0; i<indices.length; ++i) {
			String index = getWithNoQuote(indices[i]);
			fullValues[i+1] = index;
		}
		fullValues[indices.length+1] = getWithNoQuote(value);
		this.setValue(fullValues);
	}
	
	public void read(String fileName) {
		try {
			Path path = Paths.get(fileName);
			Scanner scanner = new Scanner(path);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if ((line.length() > 0) && line.charAt(0) == '^') {
					String[] nodesAndValue = line.split("=");
					String node = nodesAndValue[0];
					String value = nodesAndValue[1];
					String[] nameAndIndices = node.split("(");
					String name = nameAndIndices[0].substring(1);
					String indicesWithComma = nameAndIndices[1].substring(0, nameAndIndices[1].length()-1);
					String[] indices = indicesWithComma.split(",");
					this.read(name, indices, value);
				}			
			}
			scanner.close();
		} catch (IOException exception) {
			LOGGER.log(Level.SEVERE, "Unable to read global node from file " + fileName);
		}
	}
}
