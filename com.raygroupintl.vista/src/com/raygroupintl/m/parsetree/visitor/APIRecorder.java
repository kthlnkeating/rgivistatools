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

package com.raygroupintl.m.parsetree.visitor;

import java.util.HashSet;
import java.util.Set;

import com.raygroupintl.m.parsetree.DoBlock;
import com.raygroupintl.m.parsetree.ElseCmd;
import com.raygroupintl.m.parsetree.Entry;
import com.raygroupintl.m.parsetree.ForLoop;
import com.raygroupintl.m.parsetree.Global;
import com.raygroupintl.m.parsetree.IfCmd;
import com.raygroupintl.m.parsetree.Indirection;
import com.raygroupintl.m.parsetree.Line;
import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.OpenCloseUseCmdNodes;
import com.raygroupintl.m.parsetree.QuitCmd;
import com.raygroupintl.m.parsetree.ReadCmd;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.WriteCmd;
import com.raygroupintl.m.parsetree.XecuteCmd;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlockWithAPIData;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.CallArgumentType;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackage;

public class APIRecorder extends FanoutRecorder {
	private RepositoryInfo repositoryInfo;
	
	private Blocks currentBlocks;
	
	private BlockWithAPIData currentBlock;
	private String currentRoutineName;
	private int inDoBlock;
	
	private int index;
	
	private int underCondition;
	private int underFor;
	private boolean underDeviceParameter;
	
	private Set<Integer> doBlockHash = new HashSet<Integer>();

	public APIRecorder(RepositoryInfo ri) {
		this.repositoryInfo = ri;
	}
		
	private void reset() {
		this.currentBlocks = null;
		this.currentBlock = null;
		this.currentRoutineName = null;
		this.index = 0;		
		this.inDoBlock = 0;
		this.underCondition = 0;
		this.underFor = 0;
		this.underDeviceParameter = false;
	}
	
	private void addOutput(Local local) {
		++this.index;
		if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().addLocal(index, local);
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
	protected void setLocal(Local local, Node rhs) {
		this.addOutput(local);
		if ((rhs != null) && ! inFilemanRoutine(this.currentRoutineName, true)) {
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
								if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().addFilemanGlobal(result);
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
		++this.index;
		if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().addNewed(this.index, local);
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
			++this.index;
			if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().addLocal(index, local);
		}
	}

	protected void passLocalByVal(Local local, int index) {		
		++this.index;
		if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().addLocal(index, local);
	}
	
	@Override
	protected void passLocalByRef(Local local, int index) {
		++this.index;
		if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().addLocal(index, local);
		super.passLocalByRef(local, index);
	}

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
		if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().addGlobal(name);		
	}
	
	protected void updateFanout(boolean isGoto, boolean conditional) {
		EntryId fanout = this.getLastFanout();
		boolean shouldClose = isGoto && (! conditional) && (this.underCondition < 1);
		if (fanout != null) {
			++this.index;
			CallArgument[] callArguments = this.getLastArguments();
			this.currentBlock.addFanout(index, fanout, callArguments);
			if ((callArguments != null) && (callArguments.length > 0) && ! inFilemanRoutine(this.currentRoutineName, true)) {
				CallArgument ca = callArguments[0];
				if (ca != null) {
					CallArgumentType caType = ca.getType();
					if ((caType == CallArgumentType.STRING_LITERAL) || (caType == CallArgumentType.NUMBER_LITERAL)) {
						String routineName = fanout.getRoutineName();						
						if ((routineName != null) && (routineName.length() > 1) && inFilemanRoutine(routineName, false)) {
							String cleanValue = removeDoubleQuote(ca.getValue());
							if (cleanValue.length() > 0 && validate(cleanValue)) {
								String value = fanout.toString() + "(" + cleanValue;
								if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().addFilemanCalls(value);
							}
						}
					}
				}
			}
		} 
		if (shouldClose) {
			this.currentBlock.close();
		}
	}

	@Override
	protected void visitDeviceParameters(OpenCloseUseCmdNodes.DeviceParameters deviceParameters) {
		boolean current = this.underDeviceParameter;
		this.underDeviceParameter = true;
		super.visitDeviceParameters(deviceParameters);
		this.underDeviceParameter = current;
	}
		
	protected void visitQuit(QuitCmd quitCmd) {
		quitCmd.acceptSubNodes(this);
		boolean quitConditional = (quitCmd.getPostCondition() != null);
		if ((! quitConditional) && (this.underFor < 1) && (this.underCondition < 1)) {
			this.currentBlock.close();			
		}
	}

	@Override
	protected void visitReadCmd(ReadCmd readCmd) {
		super.visitReadCmd(readCmd);
		if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().incrementRead();
	}

	
	@Override
	protected void visitWriteCmd(WriteCmd writeCmd) {
		super.visitWriteCmd(writeCmd);
		if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().incrementWrite();
	}

	
	@Override
	protected void visitXecuteCmd(XecuteCmd xecuteCmd) {
		super.visitXecuteCmd(xecuteCmd);
		if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().incrementExecute();
	}

	protected void visitForLoop(ForLoop forLoop) {
		++this.underFor;
		super.visitForLoop(forLoop);
		--this.underFor;
	}
	
	protected void visitIf(IfCmd ifCmd) {
		++this.underCondition;
		super.visitIf(ifCmd);
		--this.underCondition;
	}
	
	protected void visitElse(ElseCmd elseCmd) {
		++this.underCondition;
		super.visitElse(elseCmd);
		--this.underCondition;
	}

	private EntryId getEntryId(String tag) {
		if ((tag == null) || tag.isEmpty()) {
			if (this.inDoBlock > 0) {
				return new EntryId(this.currentRoutineName, ":" + String.valueOf(this.index));
			} else {
				return new EntryId(this.currentRoutineName, this.currentRoutineName);
			}
		} else {
			return new EntryId(this.currentRoutineName, tag);
		}
	}
	
	protected void visitEntry(Entry entry) {
		Block lastBlock = this.currentBlock;
		++this.index;
		String tag = entry.getName();
		EntryId entryId = this.getEntryId(tag);		
		this.currentBlock = new BlockWithAPIData(this.index, entryId, this.currentBlocks);
		if (lastBlock == null) {
			this.currentBlocks.setFirst(this.currentBlock);
			if ((tag != null) && (! tag.isEmpty())) {
				this.currentBlocks.put(tag, this.currentBlock);
			}
		} else {
			if ((tag == null) || tag.isEmpty()) {
				tag = ":" + String.valueOf(this.index);
			}
			this.currentBlocks.put(tag, this.currentBlock);
			EntryId defaultGoto = new EntryId(null, tag);
			lastBlock.addFanout(this.index, defaultGoto, null);
			lastBlock.close();
		}
		String[] params = entry.getParameters();
		this.currentBlock.getStaticData().setFormals(params);
		++this.index;
		super.visitEntry(entry);	
	}
			
	protected void visitIndirection(Indirection indirection) {
		if (! this.currentBlock.isClosed()) this.currentBlock.getStaticData().incrementIndirection();
		super.visitIndirection(indirection);
	}

	protected void visitDoBlock(DoBlock doBlock) {
		int id = doBlock.getUniqueId();
		if (! this.doBlockHash.contains(id)) {
			doBlockHash.add(id);
			++this.inDoBlock;
			doBlock.acceptPostCondition(this);
			Blocks lastBlocks = this.currentBlocks;
			BlockWithAPIData lastBlock = this.currentBlock;
			this.currentBlock = null;
			this.currentBlocks = new Blocks(lastBlocks);
			doBlock.acceptEntryList(this);
			BlockWithAPIData firstBlock = this.currentBlocks.getFirstBlock();
			this.currentBlocks = lastBlocks;
			this.currentBlock = lastBlock;
			String tag = ":" + String.valueOf(this.index);
			EntryId defaultDo = new EntryId(null, tag);		
			lastBlock.addFanout(this.index, defaultDo, null);
			this.currentBlocks.put(tag, firstBlock);
			--this.inDoBlock;
		}
	}
	
	public Blocks getBlocks() {
		return this.currentBlocks;
	}
	
	@Override
	protected void visitLine(Line line) {
		if (this.currentBlock.getLineLocation() == null) {
			String tag = line.getTag();
			int index = line.getIndex();
			this.currentBlock.setLineLocation(new LineLocation(tag, index));
		}
		super.visitLine(line);
	}

	protected void visitRoutine(Routine routine) {
		this.reset();
		this.currentBlocks = new Blocks();
		this.currentRoutineName = routine.getName();
		super.visitRoutine(routine);
	}
}
