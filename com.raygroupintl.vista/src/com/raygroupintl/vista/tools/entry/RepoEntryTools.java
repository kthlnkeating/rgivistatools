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

package com.raygroupintl.vista.tools.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.IndexedFanout;
import com.raygroupintl.m.struct.CodeLocation;
import com.raygroupintl.m.tool.CommonToolParams;
import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.SavedParsedTrees;
import com.raygroupintl.m.tool.entry.AccumulatingBlocksSupply;
import com.raygroupintl.m.tool.entry.Block;
import com.raygroupintl.m.tool.entry.BlocksSupply;
import com.raygroupintl.m.tool.entry.MEntryToolResult;
import com.raygroupintl.m.tool.entry.RecursionSpecification;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariables;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesTool;
import com.raygroupintl.m.tool.entry.assumedvariables.AssumedVariablesToolParams;
import com.raygroupintl.m.tool.entry.basiccodeinfo.BasicCodeInfoTR;
import com.raygroupintl.m.tool.entry.basiccodeinfo.BasicCodeInfoToolParams;
import com.raygroupintl.m.tool.entry.basiccodeinfo.CodeInfo;
import com.raygroupintl.m.tool.entry.basiccodeinfo.CodeLocations;
import com.raygroupintl.m.tool.entry.fanout.EntryFanouts;
import com.raygroupintl.m.tool.entry.fanout.FanoutTool;
import com.raygroupintl.m.tool.entry.localassignment.LocalAssignmentTool;
import com.raygroupintl.m.tool.entry.localassignment.LocalAssignmentToolParams;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.FilterFactory;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.RepositoryVisitor;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.VistaPackages;
import com.raygroupintl.vista.tools.CLIParams;
import com.raygroupintl.vista.tools.CLIParamsAdapter;
import com.raygroupintl.vista.tools.MRALogger;
import com.raygroupintl.vista.tools.RepositoryTools;
import com.raygroupintl.vista.tools.Tool;
import com.raygroupintl.vista.tools.Tools;
import com.raygroupintl.vista.tools.entryfanin.BlocksInSerialFanouts;
import com.raygroupintl.vista.tools.entryfanin.EntryFaninAccumulator;
import com.raygroupintl.vista.tools.entryfanin.EntryFanins;
import com.raygroupintl.vista.tools.entryfanin.FaninMark;
import com.raygroupintl.vista.tools.entryfanin.MarkedAsFaninBRF;

public class RepoEntryTools extends Tools {
	private static abstract class EntryInfoToolBase<U extends MEntryToolResult> extends Tool {		
		public EntryInfoToolBase(CLIParams params) {
			super(params);
		}
		
		protected abstract List<U> getResult(RepositoryInfo ri, List<EntryId> entries);
		
		protected void write(EntryId entryId, U result, Terminal t, TerminalFormatter tf, boolean skipEmpty) {			
			if (result.isValid()) {
				if ((! skipEmpty) || (! result.isEmpty())) {
					t.writeEOL(" " + entryId.toString2());		
					this.write(result, t, tf);
				}
			} else {
				t.writeEOL(" " + entryId.toString2());		
				t.writeEOL("  ERROR: Invalid entry point");
				return;
			}
			
		}
				
		protected abstract void write(U result, Terminal t, TerminalFormatter tf);

		private boolean skipEmpty() {
			List<String> outputFlags = this.params.outputFlags;
			for (String outputFlag : outputFlags) {
				if (outputFlag.equals("ignorenodata")) {
					return true;
				}
			}
			return false;			
		}
		
		@Override
		public void run() {
			FileWrapper fr = this.getOutputFile();
			if (fr != null) {
				RepositoryInfo ri = this.getRepositoryInfo();
				if (ri != null) {			
					List<EntryId> entries = this.getEntries();
					if (entries != null) {
						List<U> resultList = this.getResult(ri, entries);		
						if (fr.start()) {
							TerminalFormatter tf = new TerminalFormatter();
							tf.setTab(12);
							int n = entries.size();
							for (int i=0; i<n; ++i) {
								this.write(entries.get(i), resultList.get(i), fr, tf, this.skipEmpty());
							}
							fr.stop();
						}
					}
				}
			}
		}
	}

	private static abstract class EntryInfoTool<T, U extends MEntryToolResult> extends EntryInfoToolBase<U> {	
		public EntryInfoTool(CLIParams params) {
			super(params);
		}
		
		protected abstract List<U> getResult(List<EntryId> entries);
		
		@Override
		public List<U> getResult(RepositoryInfo ri, List<EntryId> entries) {
			return this.getResult(entries);
		}
	}

	private static class EntryCodeInfoTool extends EntryInfoToolBase<EntryCodeInfo> {	
		public EntryCodeInfoTool(CLIParams params) {
			super(params);
		}
		
		@Override
		public List<EntryCodeInfo> getResult(RepositoryInfo ri, List<EntryId> entries) {
			AssumedVariablesToolParams p = CLIParamsAdapter.toAssumedVariablesToolParams(this.params);
			BasicCodeInfoToolParams p2 = CLIParamsAdapter.toBasicCodeInfoToolParams(this.params, ri);
			EntryCodeInfoAccumulator a = new EntryCodeInfoAccumulator(p, p2);
			return a.getResult(entries);			
		}

		@Override
		protected void write(EntryCodeInfo result, Terminal t, TerminalFormatter tf) {
			String[] f = result.getFormals();
			AssumedVariables av = result.getAssumedVariablesTR();
			BasicCodeInfoTR ci = result.getBasicCodeInfoTR();
			t.writeFormatted("FORMAL", f, tf);
			t.writeSortedFormatted("ASSUMED", av.toSet(), tf);
			ci.writeInfo(t, tf);
			t.writeEOL();
		}	
	}

	private static class EntryAssumedVarTool extends EntryInfoTool<CodeInfo, AssumedVariables> {	
		public EntryAssumedVarTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected List<AssumedVariables> getResult(List<EntryId> entries) {
			AssumedVariablesToolParams params = CLIParamsAdapter.toAssumedVariablesToolParams(this.params);
			AssumedVariablesTool a = new AssumedVariablesTool(params);
			return a.getResult(entries);			
		}
		
		@Override
		protected void write(AssumedVariables result, Terminal t, TerminalFormatter tf) {
			t.writeSortedFormatted("ASSUMED", result.toSet(), tf);
		}
	}

	private static class EntryLocalAssignmentTool extends EntryInfoTool<CodeLocations, CodeLocations> {	
		public EntryLocalAssignmentTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected List<CodeLocations> getResult(List<EntryId> entries) {
			LocalAssignmentToolParams params = CLIParamsAdapter.toLocalAssignmentToolParams(this.params);
			LocalAssignmentTool a = new LocalAssignmentTool(params);
			return a.getResult(entries);			
		}

		@Override
		protected void write(CodeLocations result, Terminal t, TerminalFormatter tf) {
			List<CodeLocation> cl = result.getCodeLocations();
			if (cl == null) {
				t.writeEOL("  --");				
			} else {
				for (CodeLocation c : cl) {
					t.writeEOL("  " + c.toString());
				}
			}
		}	
	}

	private static class EntryFanoutTool extends EntryInfoTool<Void, EntryFanouts> {	
		public EntryFanoutTool(CLIParams params) {
			super(params);
		}
		
		@Override
		protected List<EntryFanouts> getResult(List<EntryId> entries) {
			CommonToolParams params = CLIParamsAdapter.toCommonToolParams(this.params);
			FanoutTool a = new FanoutTool(params);
			return a.getResult(entries);			
		}
		
		@Override
		protected void write(EntryFanouts result, Terminal t, TerminalFormatter tf) {
			Set<EntryId> r = result.getFanouts();
			if (r == null) {
				String em = result.getErrorMsg();
				if (em == null) {
					t.writeEOL("  --");				
				} else {
					t.write("  ");
					t.writeEOL(em);
				}
			} else {
				for (EntryId f : r) {
					t.writeEOL("  " + f.toString2());
				}
			}
		}	
	}

	private static class EntryFanin extends EntryInfoToolBase<EntryFanins> {		
		public EntryFanin(CLIParams params) {
			super(params);
		}
		
		private EntryFaninAccumulator getSupply(EntryId entryId, RepositoryInfo ri) {
			String method = this.params.getMethod("routinefile");
			if (method.equalsIgnoreCase("fanoutfile")) {
				BlocksSupply<Block<IndexedFanout, FaninMark>> blocksSupply = new BlocksInSerialFanouts(entryId, this.params.parseTreeDirectory);		
				return new EntryFaninAccumulator(blocksSupply, false);
			} else {
				ParseTreeSupply pts = new SavedParsedTrees(params.parseTreeDirectory);
				AccumulatingBlocksSupply<IndexedFanout, FaninMark> bs = new AccumulatingBlocksSupply<IndexedFanout, FaninMark>(pts, new MarkedAsFaninBRF(entryId));
				return new EntryFaninAccumulator(bs, true);
			}
		}
		
		@Override
		public List<EntryFanins> getResult(final RepositoryInfo ri, List<EntryId> entries) {
			List<EntryFanins> resultList = new ArrayList<EntryFanins>();
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
		
		@Override
		protected void write(EntryFanins result, Terminal t, TerminalFormatter tf) {
			Set<EntryId> starts = result.getFaninEntries();
			for (EntryId start : starts) {
				Set<EntryId> nextUps = result.getFaninNextEntries(start);
				for (EntryId nextUp : nextUps) {
					t.write("   " + start.toString2() + " thru ");
					t.writeEOL(nextUp.toString2());
				}
			}	
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
		tools.put("quittype", new MemberFactory() {				
			@Override
			public Tool getInstance(CLIParams params) {
				return null;  //new ReturnTypeTool(params);
			}
		});		
	}
}
