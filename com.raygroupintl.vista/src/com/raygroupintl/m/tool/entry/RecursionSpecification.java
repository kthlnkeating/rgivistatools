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

package com.raygroupintl.m.tool.entry;

import java.util.Arrays;
import java.util.List;

import com.raygroupintl.m.parsetree.data.Fanout;
import com.raygroupintl.m.parsetree.data.FanoutType;
import com.raygroupintl.struct.Filter;

public class RecursionSpecification {
	private static class InLabelFanoutFilter implements Filter<Fanout> {
		@Override
		public boolean isValid(Fanout input) {
			return false;
		}		
	}
		
	private static class InEntryFanoutFilter implements Filter<Fanout> {
		@Override
		public boolean isValid(Fanout input) {			
			return input.getType() == FanoutType.ASSUMED_GOTO;
		}		
	}
		
	private static class InRoutineFanoutFilter implements Filter<Fanout> {
		@Override
		public boolean isValid(Fanout input) {
			if (input != null) {
				String routineName = input.getEntryId().getRoutineName();
				if (routineName == null) return true;
			}
			return false;
		}
	}
	
	private static class NamespaceBasedFanoutFilter implements Filter<Fanout> {
		private String[] includeNamespaces;
		private String[] excludeNamespaces;
		private String[] excludeExceptionNamespaces;

		public NamespaceBasedFanoutFilter(String[] includeNamespaces, String[] excludeNamespaces, String[] excludeExceptionNamespaces) {
			this.includeNamespaces = includeNamespaces;
			this.excludeNamespaces = excludeNamespaces;
			this.excludeExceptionNamespaces = excludeExceptionNamespaces;
		}
		
		private static boolean checkNamespace(String routineName, String namespace) {
			int n = routineName.length();
			int m = namespace.length();
			if (n < m) return false;
			int count = Math.min(n, m);
			for (int i=0; i<count; ++i) {
				if (routineName.charAt(i) != namespace.charAt(i)) return false;
			}
			return true;
		}
		
		private static boolean checkNamespace(String routineName, String[] namespaces) {
			for (int i=0; i<namespaces.length; ++i) {
				if (checkNamespace(routineName, namespaces[i])) {
					return true;
				} 
			}
			return false;
		}
		
		@Override
		public boolean isValid(Fanout input) {
			String routineName = input.getEntryId().getRoutineName();
			if ((routineName == null) || (routineName.isEmpty())) return true;
			if (this.includeNamespaces != null) {
				boolean b = checkNamespace(routineName, this.includeNamespaces);
				if (!b) return false;
			}
			if (this.excludeNamespaces != null) {
				boolean b = checkNamespace(routineName, this.excludeNamespaces);
				if (b) {
					if (this.excludeExceptionNamespaces == null) return false;
					return checkNamespace(routineName, this.excludeExceptionNamespaces);
				}
			}
			return true;
		}
	}

	private RecursionDepth depth = RecursionDepth.LABEL;

	private String[] includedFanoutNamespaces;
	private String[] excludedFanoutNamespaces;
	private String[] excludedFanoutExceptionNamespaces;

	public void setDepth(RecursionDepth depth) {
		this.depth = depth;
	}

	public void setIncludedFanoutNamespaces(String[] namespaces) {
		this.includedFanoutNamespaces = namespaces;
	}

	public void setExcludedFanoutNamespaces(String[] namespaces) {
		this.excludedFanoutNamespaces = namespaces;
	}
	
	public void setExcludedFanoutExceptionNamespaces(String[] namespaces) {
		this.excludedFanoutExceptionNamespaces = namespaces;
	}

	private String[] addNamespaces(String[] original, List<String> additional) {
		if ((additional != null) && (additional.size() > 0)) {
			if (original == null) {
				return additional.toArray(new String[0]);				
			} else {
				int n = original.length;
				int m = additional.size();
				String[] result = Arrays.copyOf(original, n+m);
				for (String namespace : additional) {
					result[n] = namespace;
					++n;
				}
				return result;
			}
		}
		return original;
	}
	
	public void addIncludedFanoutNamespaces(List<String> namespaces) {
		this.includedFanoutNamespaces = addNamespaces(this.includedFanoutNamespaces, namespaces);
	}
	
	public void addExcludedFanoutNamespaces(List<String> namespaces) {
		this.excludedFanoutNamespaces = addNamespaces(this.excludedFanoutNamespaces, namespaces);		
	}

	public void addExcludedFanoutExceptionNamespaces(List<String> namespaces) {
		this.excludedFanoutExceptionNamespaces = addNamespaces(this.excludedFanoutExceptionNamespaces, namespaces);		
	}

	public Filter<Fanout> getFanoutFilter() {
		switch (this.depth) {
		case ALL:
			return new NamespaceBasedFanoutFilter(RecursionSpecification.this.includedFanoutNamespaces, RecursionSpecification.this.excludedFanoutNamespaces, RecursionSpecification.this.excludedFanoutExceptionNamespaces);
		case ROUTINE:
			return new InRoutineFanoutFilter();
		case ENTRY:
			return new InEntryFanoutFilter();
		default: // LABEL
			return new InLabelFanoutFilter();
		}
	}
}
