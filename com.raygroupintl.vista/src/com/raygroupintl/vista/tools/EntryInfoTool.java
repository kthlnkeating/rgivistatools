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

package com.raygroupintl.vista.tools;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.m.parsetree.data.BlocksInSerialRoutine;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.filter.ExcludeFilemanCallFanoutFilter;
import com.raygroupintl.m.parsetree.filter.ExcludeNonPkgCallFanoutFilter;
import com.raygroupintl.m.parsetree.filter.ExcludeNonRtnFanoutFilter;
import com.raygroupintl.m.parsetree.filter.PercentRoutineFanoutFilter;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackage;

public abstract class EntryInfoTool extends Tool {
	private static Map<String, String> REPLACEMENT_ROUTINES = new HashMap<String, String>();
	static {
		REPLACEMENT_ROUTINES.put("%ZOSV", "ZOSVONT");
		REPLACEMENT_ROUTINES.put("%ZIS4", "ZIS4ONT");
		REPLACEMENT_ROUTINES.put("%ZISF", "ZISFONT");
		REPLACEMENT_ROUTINES.put("%ZISH", "ZISHONT");
		REPLACEMENT_ROUTINES.put("%XUCI", "ZISHONT");

		REPLACEMENT_ROUTINES.put("%ZISTCPS", "ZISTCPS");
		REPLACEMENT_ROUTINES.put("%ZTMDCL", "ZTMDCL");
		
		REPLACEMENT_ROUTINES.put("%ZOSVKR", "ZOSVKRO");
		REPLACEMENT_ROUTINES.put("%ZOSVKSE", "ZOSVKSOE");
		REPLACEMENT_ROUTINES.put("%ZOSVKSS", "ZOSVKSOS");
		REPLACEMENT_ROUTINES.put("%ZOSVKSD", "ZOSVKSD");

		REPLACEMENT_ROUTINES.put("%ZTLOAD", "ZTLOAD");
		REPLACEMENT_ROUTINES.put("%ZTLOAD1", "ZTLOAD1");
		REPLACEMENT_ROUTINES.put("%ZTLOAD2", "ZTLOAD2");
		REPLACEMENT_ROUTINES.put("%ZTLOAD3", "ZTLOAD3");
		REPLACEMENT_ROUTINES.put("%ZTLOAD4", "ZTLOAD4");
		REPLACEMENT_ROUTINES.put("%ZTLOAD5", "ZTLOAD5");
		REPLACEMENT_ROUTINES.put("%ZTLOAD6", "ZTLOAD6");
		REPLACEMENT_ROUTINES.put("%ZTLOAD7", "ZTLOAD7");
		
		REPLACEMENT_ROUTINES.put("%ZTM", "ZTM");
		REPLACEMENT_ROUTINES.put("%ZTM0", "ZTM0");
		REPLACEMENT_ROUTINES.put("%ZTM1", "ZTM1");
		REPLACEMENT_ROUTINES.put("%ZTM2", "ZTM2");
		REPLACEMENT_ROUTINES.put("%ZTM3", "ZTM3");
		REPLACEMENT_ROUTINES.put("%ZTM4", "ZTM4");
		REPLACEMENT_ROUTINES.put("%ZTM5", "ZTM5");
		REPLACEMENT_ROUTINES.put("%ZTM6", "ZTM6");
		
		REPLACEMENT_ROUTINES.put("%ZTMS", "ZTMS");
		REPLACEMENT_ROUTINES.put("%ZTMS0", "ZTMS0");
		REPLACEMENT_ROUTINES.put("%ZTMS1", "ZTMS1");
		REPLACEMENT_ROUTINES.put("%ZTMS2", "ZTMS2");
		REPLACEMENT_ROUTINES.put("%ZTMS3", "ZTMS3");
		REPLACEMENT_ROUTINES.put("%ZTMS4", "ZTMS4");
		REPLACEMENT_ROUTINES.put("%ZTMS5", "ZTMS5");
		REPLACEMENT_ROUTINES.put("%ZTMS7", "ZTMS7");
		REPLACEMENT_ROUTINES.put("%ZTMSH", "ZTMSH");

		REPLACEMENT_ROUTINES.put("%DT", "DIDT");
		REPLACEMENT_ROUTINES.put("%DTC", "DIDTC");
		REPLACEMENT_ROUTINES.put("%RCR", "DIRCR");

		REPLACEMENT_ROUTINES.put("%ZTER", "ZTER");
		REPLACEMENT_ROUTINES.put("%ZTER1", "ZTER1");

		REPLACEMENT_ROUTINES.put("%ZTPP", "ZTPP");
		REPLACEMENT_ROUTINES.put("%ZTP1", "ZTP1");
		REPLACEMENT_ROUTINES.put("%ZTPTCH", "ZTPTCH");
		REPLACEMENT_ROUTINES.put("%ZTRDE", "ZTRDE");
		REPLACEMENT_ROUTINES.put("%ZTMOVE", "ZTMOVE");
		
		REPLACEMENT_ROUTINES.put("%ZIS", "ZIS");
		REPLACEMENT_ROUTINES.put("%ZIS1", "ZIS1");
		REPLACEMENT_ROUTINES.put("%ZIS2", "ZIS2");
		REPLACEMENT_ROUTINES.put("%ZIS3", "ZIS3");
		REPLACEMENT_ROUTINES.put("%ZIS5", "ZIS5");
		REPLACEMENT_ROUTINES.put("%ZIS6", "ZIS6");
		REPLACEMENT_ROUTINES.put("%ZIS7", "ZIS7");
		REPLACEMENT_ROUTINES.put("%ZISC", "ZISC");
		REPLACEMENT_ROUTINES.put("%ZISP", "ZISP");
		REPLACEMENT_ROUTINES.put("%ZISS", "ZISS");
		REPLACEMENT_ROUTINES.put("%ZISS1", "ZISS1");
		REPLACEMENT_ROUTINES.put("%ZISS2", "ZISS2");
		REPLACEMENT_ROUTINES.put("%ZISTCP", "ZISTCP");
		REPLACEMENT_ROUTINES.put("%ZISUTL", "ZISUTL");
	}
	
	public EntryInfoTool(CLIParams params) {
		super(params);
	}
	
	protected int getFanoutFlag() {
		try {
			int result = Integer.parseInt(this.params.flag);
			if (result < 0) return 0;
			if (result > 3) return 3;
			return result;
		} catch(Throwable t) {
		}
		return 0;
	}
	
	protected Filter<EntryId> getFilter(RepositoryInfo ri, EntryId entryId) {
		int flag = this.getFanoutFlag();
		switch (flag) {
			case 1: {
				String routineName = entryId.getRoutineName();
				VistaPackage vp = ri.getPackageFromRoutineName(routineName);
				return new ExcludeFilemanCallFanoutFilter(ri, vp.getDefaultPrefix());
			}
			case 2: {
				String routineName = entryId.getRoutineName();
				if (routineName == null) {
					return new ExcludeNonPkgCallFanoutFilter(ri, null);
				} else {
					VistaPackage vp = ri.getPackageFromRoutineName(routineName);
					return new ExcludeNonPkgCallFanoutFilter(ri, vp.getDefaultPrefix());
				}
			}
			case 3:
				return new ExcludeNonRtnFanoutFilter(entryId);
			default:
				return new PercentRoutineFanoutFilter();
		}
	}
	
	private List<String> getEntriesInString() {
		if (this.params.inputFile != null) {
			try {
				Path path = Paths.get(this.params.inputFile);
				Scanner scanner = new Scanner(path);
				List<String> result = new ArrayList<String>();
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					result.add(line);
				}		
				scanner.close();
				return result;
			} catch (IOException e) {
				MRALogger.logError("Unable to open file " + this.params.inputFile);
				return null;
			}
		} else {
			return this.params.entries;
		}			
	}
	
	private List<EntryId> getEntries() {
		List<String> entriesInString = this.getEntriesInString();
		if (entriesInString != null) {
			List<EntryId> result = new ArrayList<EntryId>(entriesInString.size());
			for (String entryInString : entriesInString) {
				EntryId entryId = EntryId.getInstance(entryInString);
				result.add(entryId);
			}
			return result;
		}
		return null;
	}
		
	public void writeEntries(FileWrapper fr, List<ToolResult> resultList) {
		if (fr.start()) {
			TerminalFormatter tf = new TerminalFormatter();
			tf.setTab(12);
			for (ToolResult result : resultList) {
				result.write(fr, tf);
			}
			fr.stop();
		}
	}
		
	protected <T> BlocksSupply<T> getBlocksSupply(RepositoryInfo ri, BlockRecorderFactory<T> f) {
		if ((this.params.parseTreeDirectory == null) || this.params.parseTreeDirectory.isEmpty()) {
			return BlocksInMap.getInstance(f.getRecorder(), ri, REPLACEMENT_ROUTINES);
		} else {
			return new BlocksInSerialRoutine<T>(this.params.parseTreeDirectory, f, REPLACEMENT_ROUTINES);
		}		
	}

	protected abstract List<ToolResult> getResult(RepositoryInfo ri, List<EntryId> entries);
	
	@Override
	public void run() {
		FileWrapper fr = this.getOutputFile();
		if (fr != null) {
			RepositoryInfo ri = this.getRepositoryInfo();
			if (ri != null) {			
				List<EntryId> entries = this.getEntries();
				if (entries != null) {
					List<ToolResult> resultList = this.getResult(ri, entries);		
					this.writeEntries(fr, resultList);
				}
			}
		}
	}
}
