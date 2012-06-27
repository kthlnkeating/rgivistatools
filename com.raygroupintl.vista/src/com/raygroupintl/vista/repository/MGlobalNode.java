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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.stringlib.MComparator;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.Transformer;

public class MGlobalNode {
	private static class NodeComparator implements Comparator<MGlobalNode> {
		private MComparator nameComparator = new MComparator();
		
		@Override
		public int compare(MGlobalNode o1, MGlobalNode o2) {
			String n1 = o1.getName();
			String n2 = o2.getName();
			return nameComparator.compare(n1, n2);
		}
	}
	
	private final static Logger LOGGER = Logger.getLogger(MGlobalNode.class.getName());

	private String name;
	private String value;
	private Map<String, MGlobalNode> subNodes;
	
	private NodeComparator nodeComparator = new NodeComparator();

	public MGlobalNode() {
	}
	
	public MGlobalNode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name == null ? "" : this.name;
	}
	
	public MGlobalNode getNode(String... subscripts) {
		MGlobalNode valueHolder = this;
		for (int i=0; i<subscripts.length; ++i) {
			if (valueHolder.subNodes == null) return null;
			valueHolder = valueHolder.subNodes.get(subscripts[i]);
			if (valueHolder == null) return null;
		}
		return valueHolder;
	}
	
	public String getValuePiece(int index) {
		if (this.value != null) {
			String[] pieces = this.value.split("\\^");
			if (index < pieces.length) {
				return pieces[index];
			}
		}
		return null;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getValue(String... subscripts) {
		MGlobalNode valueHolder = this.getNode(subscripts);
		if (valueHolder == null) {
			return null;
		} else {
			return valueHolder.value;
		}
	}
	
	public <T> List<T> getValues(Filter<MGlobalNode> filter, Transformer<MGlobalNode, T> transformer) {
		List<T> result = null;
		if (this.subNodes != null) {
			List<MGlobalNode> validNodes = null;
			for (MGlobalNode node : this.subNodes.values()) {
				if (filter.isValid(node)) {
					if (validNodes == null) {
						validNodes = new ArrayList<MGlobalNode>();
					}
					validNodes.add(node);
				}
			}
			if (validNodes != null) {
				Collections.sort(validNodes, this.nodeComparator);
				for (MGlobalNode subNode : validNodes) {
					T transformedValue = transformer.transform(subNode);
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
			if (valueHolder.subNodes == null) {
				valueHolder.subNodes = new HashMap<String, MGlobalNode>();
			}
			MGlobalNode nextValueHolder = valueHolder.subNodes.get(value);
			if (nextValueHolder == null) {
				nextValueHolder = new MGlobalNode(value);
				valueHolder.subNodes.put(value, nextValueHolder);
			}
			valueHolder = nextValueHolder;
		}
		valueHolder.value = values[values.length-1];
	}
	
	private String getWithNoQuote(String value) {
		if ((! value.isEmpty()) && (value.charAt(0) == '"')) {
			if (value.length() < 2) {
				return "";
			} else {
				return value.substring(1, value.length()-1);
			}
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
	
	public void read(Path path) {
		try {
			Scanner scanner = new Scanner(path);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if ((line.length() > 0) && line.charAt(0) == '^') {
					String[] nodesAndValue = line.split("=");
					String node = nodesAndValue[0];
					String value = nodesAndValue[1];
					String[] nameAndIndices = node.split("\\(");
					String name = nameAndIndices[0].substring(1);
					String indicesWithComma = nameAndIndices[1].substring(0, nameAndIndices[1].length()-1);
					String[] indices = indicesWithComma.split(",");
					this.read(name, indices, value);
				}			
			}
			scanner.close();
		} catch (IOException exception) {
			LOGGER.log(Level.SEVERE, "Unable to read global node from file " + path.toString());
		}
	}
}
