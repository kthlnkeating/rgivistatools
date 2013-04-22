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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlocksInMap;
import com.raygroupintl.m.parsetree.data.BlocksInSerialRoutine;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.m.tool.RecursionSpecification;
import com.raygroupintl.m.tool.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.tools.entry.CodeLocations;
import com.raygroupintl.vista.tools.entry.EntryCodeLocations;
import com.raygroupintl.vista.tools.entry.LocalAssignmentAccumulator;
import com.raygroupintl.vista.tools.entry.LocalAssignmentRecorder;
import com.raygroupintl.vista.tools.entryfanin.BlocksInSerialFanouts;
import com.raygroupintl.vista.tools.entryfanin.EntryFaninAccumulator;
import com.raygroupintl.vista.tools.entryfanin.EntryFanins;
import com.raygroupintl.vista.tools.entryfanin.FaninMark;
import com.raygroupintl.vista.tools.entryfanin.MarkedAsFaninBRF;
import com.raygroupintl.vista.tools.entryfanout.EntryFanouts;
import com.raygroupintl.vista.tools.entryinfo.Accumulator;
import com.raygroupintl.vista.tools.entryinfo.AssumedVariablesTR;
import com.raygroupintl.vista.tools.entryinfo.CodeInfo;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfo;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfoAccumulator;
import com.raygroupintl.vista.tools.entryinfo.EntryCodeInfoRecorder;
import com.raygroupintl.vista.tools.entryinfo.FanoutAccumulator;
import com.raygroupintl.vista.tools.entryinfo.VoidBlockRecorder;
import com.raygroupintl.vista.tools.fnds.ToolResult;
import com.raygroupintl.vista.tools.fnds.ToolResultCollection;

public class RepoEntryTools extends Tools {
	private static abstract class EntryInfoToolBase extends Tool {		
		public EntryInfoToolBase(CLIParams params) {
			super(params);
		}
		
		protected <T> BlocksSupply<Block<T>> getBlocksSupply(RepositoryInfo ri, BlockRecorderFactory<T> f) {
			if ((this.params.parseTreeDirectory == null) || this.params.parseTreeDirectory.isEmpty()) {
				return BlocksInMap.getInstance(f.getRecorder(), ri);
			} else {
				return new BlocksInSerialRoutine<T>(this.params.parseTreeDirectory, f);
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
		
		protected abstract Accumulator<U, T> getAccumulator(BlocksSupply<Block<T>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory);
		
		protected abstract BlockRecorderFactory<T> getBlockRecorderFactory(final RepositoryInfo ri);
		
		@Override
		public ToolResult getResult(final RepositoryInfo ri, List<EntryId> entries) {
			BlockRecorderFactory<T> rf = this.getBlockRecorderFactory(ri);
			BlocksSupply<Block<T>> blocksSupply = this.getBlocksSupply(ri, rf);
			RecursionSpecification rs = CLIParamsAdapter.toRecursionSpecification(this.params);
			FilterFactory<EntryId, EntryId> filterFactory = rs.getFanoutFilterFactory();
			Accumulator<U, T> a = this.getAccumulator(blocksSupply, filterFactory);
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
		protected Accumulator<EntryCodeInfo, CodeInfo> getAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
			return new EntryCodeInfoAccumulator(blocksSupply, filterFactory);			
		}

		@Override
		protected BlockRecorderFactory<CodeInfo> getBlockRecorderFactory(final RepositoryInfo ri) {
			return new EntryCodeInfoRecorderFactory(ri);	
		}
	}

	private static class EntryAssumedVarTool extends EntryInfoTool<CodeInfo, AssumedVariablesTR> {	
		public EntryAssumedVarTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected Accumulator<AssumedVariablesTR, CodeInfo> getAccumulator(BlocksSupply<Block<CodeInfo>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {			
			return new AssumedVariablesTool(blocksSupply, filterFactory, this.params.getAssumedVariablesFlags());			
		}

		@Override
		protected BlockRecorderFactory<CodeInfo> getBlockRecorderFactory(final RepositoryInfo ri) {
			return new EntryCodeInfoRecorderFactory(ri);	
		}
	}

	private static class EntryLocalAssignmentRecorderFactory implements BlockRecorderFactory<CodeLocations> {
		private Set<String> localsUnderTest;
		
		public EntryLocalAssignmentRecorderFactory(Set<String> localsUnderTest) {
			this.localsUnderTest = localsUnderTest;
		}
		
		@Override
		public BlockRecorder<CodeLocations> getRecorder() {
			return new LocalAssignmentRecorder(this.localsUnderTest);
		}
	}

	private static class EntryLocalAssignmentTool extends EntryInfoTool<CodeLocations, EntryCodeLocations> {	
		public EntryLocalAssignmentTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected Accumulator<EntryCodeLocations, CodeLocations> getAccumulator(BlocksSupply<Block<CodeLocations>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
			return new LocalAssignmentAccumulator(blocksSupply, filterFactory);			
		}

		@Override
		protected BlockRecorderFactory<CodeLocations> getBlockRecorderFactory(final RepositoryInfo ri) {
			Set<String> locals = new HashSet<String>();
			locals.add("SDCLN");
			return new EntryLocalAssignmentRecorderFactory(locals);	
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
		protected Accumulator<EntryFanouts, Void> getAccumulator(BlocksSupply<Block<Void>> blocksSupply, FilterFactory<EntryId, EntryId> filterFactory) {
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
				BlocksSupply<Block<FaninMark>> blocksSupply = new BlocksInSerialFanouts(entryId, this.params.parseTreeDirectory);		
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
				RecursionSpecification rs = CLIParamsAdapter.toRecursionSpecification(this.params);
				FilterFactory<EntryId, EntryId> filterFactory = rs.getFanoutFilterFactory();
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
		tools.put("entryassumedvar", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryAssumedVarTool(params);
			}
		});
		tools.put("entrylocalassignment", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return new EntryLocalAssignmentTool(params);
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
