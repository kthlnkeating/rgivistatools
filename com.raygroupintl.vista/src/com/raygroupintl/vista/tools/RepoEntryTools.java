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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.m.parsetree.data.BlocksInSerialRoutine;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.tools.entryfanin.BlocksInSerialFanouts;
import com.raygroupintl.vista.tools.entryfanin.EntryFaninAccumulator;
import com.raygroupintl.vista.tools.entryfanin.EntryFanins;
import com.raygroupintl.vista.tools.entryfanin.FaninMark;
import com.raygroupintl.vista.tools.entryfanin.MarkedAsFaninBRF;
import com.raygroupintl.vista.tools.entryfanout.EntryFanouts;
import com.raygroupintl.vista.tools.entryinfo.Accumulator;
import com.raygroupintl.vista.tools.entryinfo.CodeInfo;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfo;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfoAccumulator;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfoRecorder;
import com.raygroupintl.vista.tools.entryinfo.FanoutAccumulator;
import com.raygroupintl.vista.tools.entryinfo.VoidBlockRecorder;
import com.raygroupintl.vista.tools.fnds.ToolResult;
import com.raygroupintl.vista.tools.fnds.ToolResultCollection;

public class RepoEntryTools extends Tools {
	public static Map<String, String> REPLACEMENT_ROUTINES = new HashMap<String, String>();
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

	private static abstract class EntryInfoToolBase extends Tool {		
		public EntryInfoToolBase(CLIParams params) {
			super(params);
		}
		
		protected <T> BlocksSupply<Block<T>> getBlocksSupply(RepositoryInfo ri, BlockRecorderFactory<T> f) {
			if ((this.params.parseTreeDirectory == null) || this.params.parseTreeDirectory.isEmpty()) {
				return BlocksInMap.getInstance(f.getRecorder(), ri, REPLACEMENT_ROUTINES);
			} else {
				return new BlocksInSerialRoutine<T>(this.params.parseTreeDirectory, f, REPLACEMENT_ROUTINES);
			}		
		}

		protected abstract ToolResult getResult(RepositoryInfo ri, List<EntryId> entries);
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {			
					List<EntryId> entries = this.getEntries();
					if (entries != null) {
						ToolResult resultList = this.getResult(ri, entries);		
						if (fr.start()) {
							TerminalFormatter tf = new TerminalFormatter();
							tf.setTab(12);
							resultList.write(fr, tf);
							fr.stop();
						}
					}
				}
			}
		}
	}

	private static abstract class EntryInfoTool<T, U extends ToolResult> extends EntryInfoToolBase {	
		public EntryInfoTool(CLIParams params) {
			super(params);
		}
		
		protected abstract Accumulator<U> getAccumulator(BlocksSupply<Block<T>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory);
		
		protected abstract BlockRecorderFactory<T> getBlockRecorderFactory(final RepositoryInfo ri);
		
		@Override
		public ToolResult getResult(final RepositoryInfo ri, List<EntryId> entries) {
			BlockRecorderFactory<T> rf = this.getBlockRecorderFactory(ri);
			BlocksSupply<Block<T>> blocksSupply = this.getBlocksSupply(ri, rf);
			FilterFactory<EntryId, EntryId> filterFactory = new FilterFactory<EntryId, EntryId>() {
				@Override
				public Filter<EntryId> getFilter(EntryId parameter) {
					return EntryInfoTool.this.getFilter(ri, parameter);
				}
			}; 
			Accumulator<U> a = this.getAccumulator(blocksSupply, filterFactory);
			for (EntryId entryId : entries) {
				a.addEntry(entryId);
			}
			return a.getResult();
		}
	}

	private static class EntryCodeInfoRecorderFactory implements BlockRecorderFactory<CodeInfo> {
		private RepositoryInfo repositoryInfo;
		
		public EntryCodeInfoRecorderFactory(RepositoryInfo repositoryInfo) {
			this.repositoryInfo = repositoryInfo;
		}
		
		@Override
		public BlockRecorder<CodeInfo> getRecorder() {
			return new EntryCodeInfoRecorder(this.repositoryInfo);
		}
	}

	private static class EntryCodeInfoTool extends EntryInfoTool<CodeInfo, EntryCodeInfo> {	
		public EntryCodeInfoTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected Accumulator<EntryCodeInfo> getAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
			return new EntryCodeInfoAccumulator(blocksSupply, filterFactory);			
		}

		@Override
		protected BlockRecorderFactory<CodeInfo> getBlockRecorderFactory(final RepositoryInfo ri) {
			return new EntryCodeInfoRecorderFactory(ri);	
		}
	}

	private static class EntryFanoutRecorderFactory implements BlockRecorderFactory<Void> {
		@Override
		public BlockRecorder<Void> getRecorder() {
			return new VoidBlockRecorder();
		}
	}

	private static class EntryFanoutTool extends EntryInfoTool<Void, EntryFanouts> {	
		public EntryFanoutTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected Accumulator<EntryFanouts> getAccumulator(BlocksSupply<Block<Void>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
			return new FanoutAccumulator(blocksSupply, filterFactory);			
		}

		@Override
		protected BlockRecorderFactory<Void> getBlockRecorderFactory(final RepositoryInfo ri) {
			return new EntryFanoutRecorderFactory();	
		}
	}

	private static class EntryFanin extends EntryInfoToolBase {		
		public EntryFanin(CLIParams params) {
			super(params);
		}
		
		private EntryFaninAccumulator getSupply(EntryId entryId, RepositoryInfo ri) {
			String method = this.params.getMethod("routinefile");
			if (method.equalsIgnoreCase("fanoutfile")) {
				BlocksSupply<Block<FaninMark>> blocksSupply = new BlocksInSerialFanouts(entryId, this.params.parseTreeDirectory, REPLACEMENT_ROUTINES);		
				return new EntryFaninAccumulator(entryId, blocksSupply, false);
			} else {
				BlocksSupply<Block<FaninMark>> blocksSupply = this.getBlocksSupply(ri, new MarkedAsFaninBRF(entryId));		
				return new EntryFaninAccumulator(entryId, blocksSupply, true);
			}
		}
		
		@Override
		public ToolResultCollection<EntryFanins> getResult(final RepositoryInfo ri, List<EntryId> entries) {
			ToolResultCollection<EntryFanins> resultList = new ToolResultCollection<EntryFanins>();
			for (EntryId entryId : entries) {
				final EntryFaninAccumulator efit = this.getSupply(entryId, ri);
				FilterFactory<EntryId, EntryId> filterFactory = new FilterFactory<EntryId, EntryId>() {
					@Override
					public Filter<EntryId> getFilter(EntryId parameter) {
						return EntryFanin.this.getFilter(ri, parameter);
					}
				}; 
				efit.setFilterFactory(filterFactory);
				RepositoryVisitor rv = new RepositoryVisitor() {
					int packageCount;
					@Override
					protected void visitRoutine(Routine routine) {
						efit.addRoutine(routine);
					}
					@Override
					protected void visitVistaPackage(VistaPackage routinePackage) {
						++this.packageCount;
						MRALogger.logInfo(RepositoryTools.class, String.valueOf(this.packageCount) + ". " + routinePackage.getPackageName() + "...writing");
						super.visitVistaPackage(routinePackage);
						MRALogger.logInfo(RepositoryTools.class, "..done.\n");
					}
				};
				VistaPackages vps = this.getVistaPackages(ri);
				vps.accept(rv);
				EntryFanins result = efit.getResult();
				resultList.add(result);
			}
			return resultList;
		}
	}
	
	@Override
	protected void updateTools(Map<String, MemberFactory> tools) {
		tools.put("entryinfo", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryCodeInfoTool(params);
			}
		});
		tools.put("entryfanout", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryFanoutTool(params);
			}
		});
		tools.put("entryfanin", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryFanin(params);
			}
		});
	}
}
