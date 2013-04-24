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

package com.raygroupintl.m.tool.assumedvariables;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.data.AdditiveDataHandler;
import com.raygroupintl.m.parsetree.data.RecursiveDataHandler;
import com.raygroupintl.vista.tools.entryinfo.BasicCodeInfo;

public class AssumedVariablesData implements RecursiveDataHandler<Set<String>>, AdditiveDataHandler<BasicCodeInfo> {
	private String[] formals;
	private Map<String, Integer> formalsMap;
	private Map<String, Integer> newedLocals = new HashMap<String, Integer>();
	private Set<String> assumedLocals = new HashSet<String>();
	private Set<String> globals = new HashSet<String>();
	private Set<String> filemanGlobals = new HashSet<String>();
	private Set<String> filemanCalls = new HashSet<String>();
	
	private int indirectionCount;
	private int writeCount;
	private int readCount;
	private int executeCount;
	
	public void setFormals(String[] formals) {
		this.formals = formals;
		if (formals != null) {
			this.formalsMap = new HashMap<String, Integer>(formals.length*2);
			int index = 0;
			for (String formal : formals) {
				formalsMap.put(formal, index);
				++index;
			}
		} else {
			this.formalsMap=null;
		}
	}
	
	public String[] getFormals() {
		return this.formals;
	}
	
	public void addNewed(int index, Local local) {
		String label = local.getName().toString();
		if (! this.newedLocals.containsKey(label)) {
			this.newedLocals.put(label, index);
		}
	}		
	
	public void addLocal(int index, Local local) {
		String label = local.getName().toString();
		if ((this.formalsMap == null) || (! this.formalsMap.containsKey(label))) {
			if (! this.newedLocals.containsKey(label)) {
				this.assumedLocals.add(label);
			}
		}
	}
	
	public void addGlobal(String value) {
		this.globals.add(value);
	}

	public Set<String> getGlobals() {
		return this.globals;
	}
	
	public void addFilemanGlobal(String value) {
		this.filemanGlobals.add(value);
	}

	public Set<String> getFilemanGlobals() {
		return this.filemanGlobals;
	}
	
	public void addFilemanCalls(String value) {
		this.filemanCalls.add(value);
	}

	public Set<String> getFilemanCalls() {
		return this.filemanCalls;
	}
	
	public Integer getAsFormal(String name) {
		if (this.formalsMap != null) {
			return this.formalsMap.get(name);			
		} else {
			return null;
		}
	}
	
	public boolean isNewed(String name, int sourceIndex) {
		Integer index = this.newedLocals.get(name);
		if (index == null) {
			return false;
		} else if (index.intValue() > sourceIndex) {
			return false;
		}
		return true;
	}
	
	public boolean isDefined(String name, int sourceIndex) {
		if ((this.formalsMap != null) && (this.formalsMap.containsKey(name))) {
			return true;
		}
		return this.isNewed(name, sourceIndex);
	}
	
	public Map<String, Integer> getNewedLocals() {
		return this.newedLocals;
	}
	
	public Set<String> getAssumedLocals() {
		return this.assumedLocals;
	}
	
	public void incrementIndirection() {
		++this.indirectionCount;
	}
	
	public int getIndirectionCount() {
		return this.indirectionCount;
	}
	
	public void incrementWrite() {
		++this.writeCount;
	}
	
	public int getWriteCount() {
		return this.writeCount;
	}
	
	public void incrementRead() {
		++this.readCount;
	}
	
	public int getReadCount() {
		return this.readCount;
	}
	
	public void incrementExecute() {
		++this.executeCount;
	}
	
	public int getExecuteCount() {
		return this.executeCount;
	}

	@Override
	public BasicCodeInfo getNewInstance() {
		return new BasicCodeInfo();
	}

	@Override
	public void update(BasicCodeInfo target) {
		target.mergeGlobals(this.getGlobals());
		target.mergeFilemanGlobals(this.getFilemanGlobals());
		target.mergeFilemanCalls(this.getFilemanCalls());
		
		target.incrementIndirectionCount(this.getIndirectionCount());
		target.incrementReadCount(this.getReadCount());
		target.incrementWriteCount(this.getWriteCount());
		target.incrementExecuteCount(this.getExecuteCount());		
	}
	
	@Override
	public Set<String> getLocalCopy()  {
		return new HashSet<>(this.getAssumedLocals());		
	}
	
	@Override
	public int update(Set<String> target, Set<String> source, int sourceIndex) {
		int result = 0;
		for (String name : source) {
			if (! this.isDefined(name, sourceIndex)) {
				if (! target.contains(name)) {
					target.add(name);
					++result;
				}
			}
		}
		return result;
	}
}
