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

package com.raygroupintl.m.tool;

import java.util.Arrays;
import java.util.List;

import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;

public class RecursionSpecification {
	private static class DoBlockFanoutFilter implements Filter<EntryId> {
		@Override
		public boolean isValid(EntryId input) {
			String label = input.getLabelOrDefault();		
			return label.charAt(0) == ':';
		}		
	}
		
	private static class InRoutineFanoutFilter implements Filter<EntryId> {
		private String routineUnderTestName;
		
		public InRoutineFanoutFilter(EntryId entryUnderTest) {
			this.routineUnderTestName = entryUnderTest.getRoutineName();		
		}
		
		@Override
		public boolean isValid(EntryId input) {
			if (input != null) {
				String routineName = input.getRoutineName();
				if (routineName == null) return true;
				return this.routineUnderTestName.equals(routineName);
			}
			return false;
		}
	}
	
	private static class NamespaceBasedFanoutFilter implements Filter<EntryId> {
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
		public boolean isValid(EntryId input) {
			String routineName = input.getRoutineName();
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

	public FilterFactory<EntryId, EntryId> getFanoutFilterFactory() {
		switch (this.depth) {
		case ALL:
			return new FilterFactory<EntryId, EntryId>() {
				@Override
				public Filter<EntryId> getFilter(EntryId parameter) {
					return new NamespaceBasedFanoutFilter(RecursionSpecification.this.includedFanoutNamespaces, RecursionSpecification.this.excludedFanoutNamespaces, RecursionSpecification.this.excludedFanoutExceptionNamespaces);
				}
			};
		case ROUTINE:
			return new FilterFactory<EntryId, EntryId>() {
				@Override
				public Filter<EntryId> getFilter(EntryId entryUnderTest) {
					return new InRoutineFanoutFilter(entryUnderTest);
				}
			};
		default: // LABEL
			return new FilterFactory<EntryId, EntryId>() {
				@Override
				public Filter<EntryId> getFilter(EntryId entryUnderTest) {
					return new DoBlockFanoutFilter();
				}
			};
		}
	}
}
