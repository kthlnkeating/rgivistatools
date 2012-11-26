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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.data.AdditiveDataAggregator;
import com.raygroupintl.m.parsetree.data.BasicCodeInfo;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.m.parsetree.data.BlocksInSerialRoutine;
import com.raygroupintl.m.parsetree.data.CodeInfo;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.m.parsetree.filter.BasicSourcedFanoutFilter;
import com.raygroupintl.m.parsetree.filter.SourcedFanoutFilter;
import com.raygroupintl.m.parsetree.visitor.EntryCodeInfoRecorder;
import com.raygroupintl.m.parsetree.visitor.EntryCodeInfoRecorderFactory;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.PassFilter;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class EntryCodeInfoTool extends EntryInfoTool {	
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
	
	public static class EntryCodeInfo {
		public String[] formals;
		public Set<String> assumedLocals;
		public BasicCodeInfo otherCodeInfo;
	
		public EntryCodeInfo(String[] formals, Set<String> assumedLocals, BasicCodeInfo otherCodeInfo) {
			this.formals = formals;
			this.assumedLocals = assumedLocals;
			this.otherCodeInfo = otherCodeInfo;
		}
		
		public void write(Terminal t, TerminalFormatter tf) {
			t.writeFormatted("FORMAL", this.formals, tf);
			
			List<String> assumedLocalsSorted = new ArrayList<String>(this.assumedLocals);
			Collections.sort(assumedLocalsSorted);			
			t.writeFormatted("ASSUMED", assumedLocalsSorted, tf);
			
			t.writeFormatted("GLBS", this.otherCodeInfo.getGlobals(), tf);
			t.writeFormatted("READ" , this.otherCodeInfo.getReadCount(), tf);
			t.writeFormatted("WRITE", this.otherCodeInfo.getWriteCount(), tf);
			t.writeFormatted("EXEC", this.otherCodeInfo.getExecuteCount(), tf);
			t.writeFormatted("IND", this.otherCodeInfo.getIndirectionCount(), tf);
			t.writeFormatted("FMGLBS", this.otherCodeInfo.getFilemanGlobals(), tf);
			t.writeFormatted("FMCALLS", this.otherCodeInfo.getFilemanCalls(), tf);
			t.writeEOL();			
		}
	}
	
	private class Writer {		
		private BlocksSupply<CodeInfo> blocksSupply;
		private SourcedFanoutFilter filter = new BasicSourcedFanoutFilter(new PassFilter<EntryId>());
		
		public Writer(BlocksSupply<CodeInfo> blocksSupply) {
			this.blocksSupply = blocksSupply;
		}
	
		public void setFilter(SourcedFanoutFilter filter) {
			this.filter = filter;
		}
		
		private void write(Terminal t, TerminalFormatter tf, EntryId entryId) {
			t.writeEOL(" " + entryId.toString2());
			tf.setTab(12);
			
			EntryCodeInfo result = this.getEntryCodeInfo(entryId);
			if (result == null) {
				t.writeEOL("  ERROR: Invalid entry point");
				return;
			} 
			result.write(t, tf);
		}
		
		public EntryCodeInfo getEntryCodeInfo(EntryId entryId) {
			Block<CodeInfo> lb = this.blocksSupply.getBlock(entryId);
			if (lb == null) {
				return null;
			} 			
			RecursiveDataAggregator<Set<String>, CodeInfo> ala = new RecursiveDataAggregator<Set<String>, CodeInfo>(lb, this.blocksSupply);
			Set<String> assumedLocals = ala.get(store, this.filter);
			
			AdditiveDataAggregator<BasicCodeInfo, CodeInfo> bcia = new AdditiveDataAggregator<BasicCodeInfo, CodeInfo>(lb, this.blocksSupply);
			BasicCodeInfo apiData = bcia.get(this.filter);
	
			return new EntryCodeInfo(lb.getAttachedObject().getFormals(), assumedLocals, apiData);
		}
		
		public void writeEntries(FileWrapper fr, TerminalFormatter tf, List<String> entries) {
			if (fr.start()) {
				for (String entry : entries) {
					EntryId entryId = EntryId.getInstance(entry);
					this.write(fr, tf, entryId);
				}
				fr.stop();
			}
		}
	}
	
	public EntryCodeInfoTool(CLIParams params) {
		super(params);
	}
	
	DataStore<Set<String>> store = new DataStore<Set<String>>();
	
	protected void run(List<String> entries, RepositoryInfo ri, FileWrapper fr, BlocksSupply<CodeInfo> blocks) {
		EntryCodeInfoTool.Writer apiw = new EntryCodeInfoTool.Writer(blocks);
		SourcedFanoutFilter filter = this.getFilter(ri);
		apiw.setFilter(filter);
		TerminalFormatter tf = new TerminalFormatter();
		apiw.writeEntries(fr, tf, entries);
	}
	
	@Override
	public void run() {
		FileWrapper fr = this.getOutputFile();
		if (fr != null) {
			RepositoryInfo ri = this.getRepositoryInfo();
			if (ri != null) {			
				List<String> entries = this.getEntries();
				if (entries == null) return;
				if ((this.params.parseTreeDirectory == null) || this.params.parseTreeDirectory.isEmpty()) {
					BlocksSupply<CodeInfo> blocks = BlocksInMap.getInstance(new EntryCodeInfoRecorder(ri), ri, REPLACEMENT_ROUTINES);
					this.run(entries, ri, fr, blocks);
				} else {
					BlocksSupply<CodeInfo> blocks = new BlocksInSerialRoutine<CodeInfo>(this.params.parseTreeDirectory, new EntryCodeInfoRecorderFactory(ri), REPLACEMENT_ROUTINES);
					this.run(entries, ri, fr, blocks);
				}
			}
		}
	}
}
