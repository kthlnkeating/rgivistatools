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

package com.raygroupintl.vista.repository.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.EntryIdWithSource;
import com.raygroupintl.m.parsetree.visitor.FanInRecorder;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;

public class OptionWriter extends RepositoryVisitor {
	private RepositoryInfo repositoryInfo;
	private FileWrapper fileWrapper;
	private FanInRecorder faninRecorder;
	
	private static class EntryIdSource {
		public Set<String> packages;
		public Set<String> options;
		public Set<String> rpcs;
		
		private static void write(FileWrapper fw, Set<String> sources) {
			if ((sources != null) && (sources.size() > 0)) {
				boolean first = true;
				for (String source : sources) {
					if (! first) {
						fw.write(",");
					}
					fw.write(source);
					first = false;
				}
			} 
		}
		
		public void addOption(String optionName) {
			if (this.options == null) {
				this.options = new HashSet<String>();
			}
			this.options.add(optionName);
		}
		
		public void setPackages(Set<String> packages) {
			this.packages = packages;
		}
		
		public void write(FileWrapper fw) {
			write(fw, this.packages);
			fw.write("|");
			write(fw, this.options);
			fw.write("|");
			write(fw, this.rpcs);			
		}		
	}
	
	private static class EntryIdWithSources implements Comparable<EntryIdWithSources> {
		public EntryId entryId;
		public EntryIdSource sources;
		
		public EntryIdWithSources(EntryId entryId, EntryIdSource source) {
			this.entryId = entryId;
			this.sources = source;
		}
		
		@Override
		public int compareTo(EntryIdWithSources rhs) {
			return this.entryId.compareTo(rhs.entryId);
		}		
	}
	
	public OptionWriter(RepositoryInfo repositoryInfo, FileWrapper fileWrapper) {
		this.repositoryInfo = repositoryInfo;
		this.fileWrapper = fileWrapper;
	}
		
	@Override
	protected void visitVistaPackage(VistaPackage routinePackage) {
		Filter<EntryId> filter = routinePackage.getPackageFanoutFilter();
		this.faninRecorder.setFilter(filter);
		this.faninRecorder.setCurrentPackagePrefix(routinePackage.getPackageName());
		super.visitVistaPackage(routinePackage);
	}

	public void visitRoutine(Routine routine) {
		routine.accept(this.faninRecorder);
	}
	
	protected void visitRoutinePackages(VistaPackages rps) {
		this.faninRecorder = new FanInRecorder();
		List<VistaPackage> packages = this.repositoryInfo.getAllPackages();
		for (VistaPackage p : packages) {
			p.accept(this);
		}
		Map<EntryId, Set<String>> codeFanins = this.faninRecorder.getFanIns();
		
		Map<EntryId, EntryIdSource> fanins = new HashMap<EntryId, EntryIdSource>();
		Set<EntryId> codeFaninsEIs = codeFanins.keySet();
		for (EntryId ei : codeFaninsEIs) {
			Set<String> packageNames = codeFanins.get(ei);
			EntryIdSource source = new EntryIdSource();
			source.setPackages(packageNames);
			fanins.put(ei, source);
		}
		codeFanins = null;
				
		List<EntryIdWithSource> entryIdss = this.repositoryInfo.getOptionEntryPoints();
		for (EntryIdWithSource eiws : entryIdss) {
			EntryId ei = eiws.getEntryId();
			EntryIdSource eis = fanins.get(ei);
			if (eis == null) {
				eis = new EntryIdSource();
				fanins.put(ei, eis);
			}
			eis.addOption(eiws.getSource());
		}
				
		Map<String, List<EntryIdWithSources>> faninsByPackage = new HashMap<String, List<EntryIdWithSources>>();
		for (VistaPackage p : packages) {
			String name = p.getPackageName();
			faninsByPackage.put(name, new ArrayList<EntryIdWithSources>());
		}
		faninsByPackage.put("UNCATEGORIZED", new ArrayList<EntryIdWithSources>());
		Set<EntryId> faninEntryIds = fanins.keySet();
		for (EntryId f : faninEntryIds) {
			String routineName = f.getRoutineName();
			if (routineName == null) continue;
			if (routineName.isEmpty()) continue;
			VistaPackage p = this.repositoryInfo.getPackageFromRoutineName(routineName);
			String packageName = p.getPackageName();
			List<EntryIdWithSources> entryIds = faninsByPackage.get(packageName);
			EntryIdSource source = fanins.get(f);
			EntryIdWithSources fws = new EntryIdWithSources(f, source);
			entryIds.add(fws);
		}
		if (this.fileWrapper.start()) {
			for (VistaPackage p : packages) {
				String name = p.getPackageName();
				this.fileWrapper.writeEOL(name + ":");
				List<EntryIdWithSources> fs = faninsByPackage.get(name);
				Collections.sort(fs);
				for (EntryIdWithSources f : fs) {
					this.fileWrapper.write(f.entryId.toString());
					this.fileWrapper.write("|");
					f.sources.write(this.fileWrapper);
					this.fileWrapper.writeEOL();
				}
			}
			this.fileWrapper.stop();
		}
	}
}