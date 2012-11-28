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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raygroupintl.m.parsetree.Global;
import com.raygroupintl.m.parsetree.Indirection;
import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.OpenCloseUseCmdNodes;
import com.raygroupintl.m.parsetree.ReadCmd;
import com.raygroupintl.m.parsetree.WriteCmd;
import com.raygroupintl.m.parsetree.XecuteCmd;
import com.raygroupintl.m.parsetree.data.AdditiveDataAggregator;
import com.raygroupintl.m.parsetree.data.AdditiveDataHandler;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.CallArgumentType;
import com.raygroupintl.m.parsetree.data.DataStore;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.data.RecursiveDataAggregator;
import com.raygroupintl.m.parsetree.data.RecursiveDataHandler;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.m.parsetree.visitor.BlockRecorderFactory;
import com.raygroupintl.output.Terminal;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.tools.fnds.ToolResult;

public class EntryCodeInfoTool extends EntryInfoTool {	
	public static class BasicCodeInfo {
		private Set<String> globals;
		private Set<String> filemanGlobals = new HashSet<String>();
		private Set<String> filemanCalls = new HashSet<String>();
		
		private int indirectionCount;
		private int writeCount;
		private int readCount;
		private int executeCount;
			
		public void mergeGlobals(Set<String> globals) {
			if (this.globals == null) {
				this.globals = new HashSet<String>(globals);	
			} else {
				this.globals.addAll(globals);		
			}
		}
		
		public void mergeFilemanGlobals(Set<String> filemanGlobals) {
			if (this.filemanGlobals == null) {
				this.filemanGlobals = new HashSet<String>(filemanGlobals);	
			} else {
				this.filemanGlobals.addAll(filemanGlobals);		
			}
		}
		
		public void mergeFilemanCalls(Set<String> filemanCalls) {
			if (this.filemanCalls == null) {
				this.filemanCalls = new HashSet<String>(filemanCalls);	
			} else {
				this.filemanCalls.addAll(filemanCalls);		
			}
		}
		
		private List<String> getIO(Set<String> source) {
			if (source == null) {
				return Collections.emptyList();
			} else {
				List<String> result = new ArrayList<String>(source);
				Collections.sort(result);
				return result;
			}
			
		}
		
		public void incrementIndirectionCount(int count) {
			this.indirectionCount += count;
		}
		
		public void incrementWriteCount(int count) {
			this.writeCount += count;
		}
		
		public void incrementReadCount(int count) {
			this.readCount += count;
		}
		
		public void incrementExecuteCount(int count) {
			this.executeCount += count;
		}
		
		public List<String> getGlobals() {
			return getIO(this.globals);
		}
		
		public List<String> getFilemanGlobals() {
			return getIO(this.filemanGlobals);
		}
		
		public List<String> getFilemanCalls() {
			return getIO(this.filemanCalls);
		}
		
		public int getIndirectionCount() {
			return this.indirectionCount;
		}
		
		public int getWriteCount() {
			return this.writeCount;
		}
		
		public int getReadCount() {
			return this.readCount;
		}
		
		public int getExecuteCount() {
			return this.executeCount;
		}
	}

	public static class CodeInfo implements RecursiveDataHandler<Set<String>>, AdditiveDataHandler<BasicCodeInfo> {
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
	
	public static class EntryCodeInfo implements ToolResult {
		public EntryId entryId;
		public String[] formals;
		public Set<String> assumedLocals;
		public BasicCodeInfo otherCodeInfo;
	
		public EntryCodeInfo(EntryId entryId, String[] formals, Set<String> assumedLocals, BasicCodeInfo otherCodeInfo) {
			this.entryId = entryId;
			this.formals = formals;
			this.assumedLocals = assumedLocals;
			this.otherCodeInfo = otherCodeInfo;
		}
		
		@Override
		public void write(Terminal t, TerminalFormatter tf) {
			t.writeEOL(" " + this.entryId.toString2());		
			if ((this.formals == null) && (this.assumedLocals == null) && (this.otherCodeInfo == null)) {
				t.writeEOL("  ERROR: Invalid entry point");
				return;
			} else {
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
	}
	
	public static class EntryCodeInfoRecorder extends BlockRecorder<CodeInfo> {
		private RepositoryInfo repositoryInfo;
		private boolean underDeviceParameter;
		
		public EntryCodeInfoRecorder(RepositoryInfo ri) {
			this.repositoryInfo = ri;
		}
	
		public void reset() {
			this.underDeviceParameter = false;
			super.reset();
		}
		
		private void addOutput(Local local) {
			int i = this.incrementIndex();
			CodeInfo d = this.getCurrentBlockAttachedObject();
			if (d != null) {
				d.addLocal(i, local);	
			}
		}
		
		private static String removeDoubleQuote(String input) {
			if (input.charAt(0) != '"') {
				return input;
			}
			return input.substring(1, input.length()-1);
		}
		
		private static boolean validate(String input) {
			int dotCount = 0;
			for (int i=0; i<input.length(); ++i) {
				char ch = input.charAt(i);
				if (ch == '.') {
					++dotCount;
					if (dotCount > 1) return false;
				} else if (! Character.isDigit(ch)) {
					return false;
				}
			}
			return true;
		}
		
		private boolean inFilemanRoutine(String routineName, boolean kernalToo) {
			VistaPackage pkg = this.repositoryInfo == null ? null : this.repositoryInfo.getPackageFromRoutineName(routineName);
			if (pkg == null) {			
				char ch0 = routineName.charAt(0);
				if (ch0 == 'D') {
					char ch1 = routineName.charAt(1);
					if ((ch1 == 'I') || (ch1 == 'M') || (ch1 == 'D')) {
						return true;
					}
				}
				return false;
			} else {
				String name = pkg.getPackageName();
				return name.equalsIgnoreCase("VA FILEMAN") || (kernalToo && name.equalsIgnoreCase("KERNEL"));
			}
		}
		
		@Override
		protected void postUpdateFanout(EntryId fanout, CallArgument[] callArguments) {
			String rn = this.getCurrentRoutineName();
			CodeInfo d = this.getCurrentBlockAttachedObject();
			if ((d != null) && (callArguments != null) && (callArguments.length > 0) && ! inFilemanRoutine(rn, true)) {
				CallArgument ca = callArguments[0];
				if (ca != null) {
					CallArgumentType caType = ca.getType();
					if ((caType == CallArgumentType.STRING_LITERAL) || (caType == CallArgumentType.NUMBER_LITERAL)) {
						String routineName = fanout.getRoutineName();						
						if ((routineName != null) && (routineName.length() > 1) && inFilemanRoutine(routineName, false)) {
							String cleanValue = removeDoubleQuote(ca.getValue());
							if (cleanValue.length() > 0 && validate(cleanValue)) {
								String value = fanout.toString() + "(" + cleanValue;
								d.addFilemanCalls(value);
							}
						}
					}
				}
			}		
		}
		
		@Override
		protected void visitReadCmd(ReadCmd readCmd) {
			super.visitReadCmd(readCmd);
			CodeInfo d = this.getCurrentBlockAttachedObject();
			if (d != null) d.incrementRead();
		}
	
		
		@Override
		protected void visitWriteCmd(WriteCmd writeCmd) {
			super.visitWriteCmd(writeCmd);
			CodeInfo d = this.getCurrentBlockAttachedObject();
			if (d != null) d.incrementWrite();
		}
	
		
		@Override
		protected void visitXecuteCmd(XecuteCmd xecuteCmd) {
			super.visitXecuteCmd(xecuteCmd);
			CodeInfo d = this.getCurrentBlockAttachedObject();
			if (d != null) d.incrementExecute();
		}
	
		@Override
		protected void visitDeviceParameters(OpenCloseUseCmdNodes.DeviceParameters deviceParameters) {
			boolean current = this.underDeviceParameter;
			this.underDeviceParameter = true;
			super.visitDeviceParameters(deviceParameters);
			this.underDeviceParameter = current;
		}
			
		@Override
		protected void setLocal(Local local, Node rhs) {
			CodeInfo d = this.getCurrentBlockAttachedObject();
			if (d != null) {
				String rn = this.getCurrentRoutineName();
				this.addOutput(local);
				if ((rhs != null) && ! inFilemanRoutine(rn, true)) {
					String rhsAsConst = rhs.getAsConstExpr();
					if (rhsAsConst != null) {
						String name = local.getName().toString();
						if (name.startsWith("DI") && (name.length() == 3)) {
							char ch = name.charAt(2);
							if ((ch == 'E') || (ch == 'K') || (ch == 'C')) {
								rhsAsConst = removeDoubleQuote(rhsAsConst);
								if ((rhsAsConst.length() > 0) && (rhsAsConst.charAt(0) == '^')) {
									String[] namePieces = rhsAsConst.split("\\(");
									if ((namePieces[0].length() > 0)) {
										String result = namePieces[0] + "(";
										if ((namePieces.length > 1) && (namePieces[1] != null) && (namePieces[1].length() > 0)) {
											String[] subscripts = namePieces[1].split("\\,");
											if ((subscripts.length > 0) && (subscripts[0].length() > 0) && validate(subscripts[0])) {
												result += subscripts[0];									
											}
										}
										d.addFilemanGlobal(result);
									}
								}
							}
						}
					}
				}
			}
		}
		
		@Override
		protected void mergeLocal(Local local, Node rhs) {
			this.addOutput(local);
		}
		
		@Override
		protected void killLocal(Local local) {		
			this.addOutput(local);
		}
		
		@Override
		protected void newLocal(Local local) {
			int i = this.incrementIndex();
			CodeInfo d = this.getCurrentBlockAttachedObject();
			if (d != null) d.addNewed(i, local);
		}
		
		private static Set<String> DEVICE_PARAMS = new HashSet<String>();
		static {
			DEVICE_PARAMS.add("LINE");
			DEVICE_PARAMS.add("NOLINE");
			DEVICE_PARAMS.add("VT");
			DEVICE_PARAMS.add("NOESCAPE");
			DEVICE_PARAMS.add("ESCAPE");
		}
		
		
		private boolean isDeviceParameter(Local local) {
			String name = local.getName().toString();
			if (DEVICE_PARAMS.contains(name)) {
				return true;
			} else {
				return false;
			}
		}
		
		@Override
		protected void visitLocal(Local local) {
			if ((! this.underDeviceParameter) || (! isDeviceParameter(local))) { 
				super.visitLocal(local);
				int i = this.incrementIndex();
				CodeInfo d = this.getCurrentBlockAttachedObject();
				if (d != null) d.addLocal(i, local);
			}
		}
	
		@Override
		protected void passLocalByVal(Local local, int index) {		
			int i = this.incrementIndex();
			CodeInfo d = this.getCurrentBlockAttachedObject();
			if (d != null) d.addLocal(i, local);
		}
		
		@Override
		protected void passLocalByRef(Local local, int index) {
			int i = this.incrementIndex();
			CodeInfo d = this.getCurrentBlockAttachedObject();
			if (d != null) d.addLocal(i, local);
			super.passLocalByRef(local, index);
		}
	
		@Override
		protected void visitGlobal(Global global) {
			super.visitGlobal(global);
			String name = '^' + global.getName().toString();
			Node subscript = global.getSubscript(0);
			if (subscript != null) {
				name += '(';
				String constValue = subscript.getAsConstExpr();
				if (constValue != null) {
					name += constValue;
				}
			}
			CodeInfo d = this.getCurrentBlockAttachedObject();
			if (d != null) d.addGlobal(name);		
		}
		
		@Override
		protected void visitIndirection(Indirection indirection) {
			CodeInfo d = this.getCurrentBlockAttachedObject();
			if (d != null) d.incrementIndirection();
			super.visitIndirection(indirection);
		}
	
		@Override
		protected Block<CodeInfo> getNewBlock(int index, EntryId entryId, Blocks<CodeInfo> blocks, String[] params) {
			Block<CodeInfo> result = new Block<CodeInfo>(index, entryId, blocks, new CodeInfo());
			result.getAttachedObject().setFormals(params);
			return result;
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
	
	public EntryCodeInfoTool(CLIParams params) {
		super(params);
	}
	
	public ToolResult getResult(DataStore<Set<String>> store, BlocksSupply<CodeInfo> blocksSupply, EntryId entryId, Filter<EntryId> filter) {
		Block<CodeInfo> b = blocksSupply.getBlock(entryId);
		if (b != null) {
			RecursiveDataAggregator<Set<String>, CodeInfo> ala = new RecursiveDataAggregator<Set<String>, CodeInfo>(b, blocksSupply);
			Set<String> assumedLocals = ala.get(store, filter);
			AdditiveDataAggregator<BasicCodeInfo, CodeInfo> bcia = new AdditiveDataAggregator<BasicCodeInfo, CodeInfo>(b, blocksSupply);
			BasicCodeInfo apiData = bcia.get(filter);
			return new EntryCodeInfo(entryId, b.getAttachedObject().getFormals(), assumedLocals, apiData);
		} else {
			return new EntryCodeInfo(entryId, null, null, null);
		}
	}
	
	public List<ToolResult> getResult(RepositoryInfo ri, List<EntryId> entries) {
		DataStore<Set<String>> store = new DataStore<Set<String>>();					
		BlocksSupply<CodeInfo> blocksSupply = this.getBlocksSupply(ri, new EntryCodeInfoRecorderFactory(ri));
		List<ToolResult> resultList = new ArrayList<ToolResult>();
		for (EntryId entryId : entries) {
			Filter<EntryId> filter = this.getFilter(ri, entryId);
			ToolResult result = this.getResult(store, blocksSupply, entryId, filter);
			resultList.add(result);
		}
		return resultList;
	}
}
