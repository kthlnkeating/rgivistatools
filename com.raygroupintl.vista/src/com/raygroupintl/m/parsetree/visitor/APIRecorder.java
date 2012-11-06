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

import com.raygroupintl.m.parsetree.Global;
import com.raygroupintl.m.parsetree.Indirection;
import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.OpenCloseUseCmdNodes;
import com.raygroupintl.m.parsetree.ReadCmd;
import com.raygroupintl.m.parsetree.WriteCmd;
import com.raygroupintl.m.parsetree.XecuteCmd;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.BlockCodeInfo;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.CallArgumentType;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackage;

public class APIRecorder extends BlockRecorder<BlockCodeInfo> {
	private RepositoryInfo repositoryInfo;
	private boolean underDeviceParameter;
	
	public APIRecorder(RepositoryInfo ri) {
		this.repositoryInfo = ri;
	}

	protected void reset() {
		this.underDeviceParameter = false;
		super.reset();
	}
	
	private void addOutput(Local local) {
		int i = this.incrementIndex();
		Block<BlockCodeInfo> cb = this.getCurrentBlock();
		if (! cb.isClosed()) cb.getData().addLocal(i, local);
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
	
	protected void updateFanout(EntryId fanout) {
		Block<BlockCodeInfo> cb = this.getCurrentBlock();
		int i = this.incrementIndex();
		CallArgument[] callArguments = this.getLastArguments();
		cb.addFanout(i, fanout, callArguments);
		String rn = this.getCurrentRoutineName();
		if ((callArguments != null) && (callArguments.length > 0) && ! inFilemanRoutine(rn, true)) {
			CallArgument ca = callArguments[0];
			if (ca != null) {
				CallArgumentType caType = ca.getType();
				if ((caType == CallArgumentType.STRING_LITERAL) || (caType == CallArgumentType.NUMBER_LITERAL)) {
					String routineName = fanout.getRoutineName();						
					if ((routineName != null) && (routineName.length() > 1) && inFilemanRoutine(routineName, false)) {
						String cleanValue = removeDoubleQuote(ca.getValue());
						if (cleanValue.length() > 0 && validate(cleanValue)) {
							String value = fanout.toString() + "(" + cleanValue;
							if (! cb.isClosed()) cb.getData().addFilemanCalls(value);
						}
					}
				}
			}
		}		
	}
	
	@Override
	protected void visitReadCmd(ReadCmd readCmd) {
		super.visitReadCmd(readCmd);
		Block<BlockCodeInfo> cb = this.getCurrentBlock();
		if (! cb.isClosed()) cb.getData().incrementRead();
	}

	
	@Override
	protected void visitWriteCmd(WriteCmd writeCmd) {
		super.visitWriteCmd(writeCmd);
		Block<BlockCodeInfo> cb = this.getCurrentBlock();
		if (! cb.isClosed()) cb.getData().incrementWrite();
	}

	
	@Override
	protected void visitXecuteCmd(XecuteCmd xecuteCmd) {
		super.visitXecuteCmd(xecuteCmd);
		Block<BlockCodeInfo> cb = this.getCurrentBlock();
		if (! cb.isClosed()) cb.getData().incrementExecute();
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
		Block<BlockCodeInfo> cb = this.getCurrentBlock();
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
								if (! cb.isClosed()) cb.getData().addFilemanGlobal(result);
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
		Block<BlockCodeInfo> cb = this.getCurrentBlock();
		if (! cb.isClosed()) cb.getData().addNewed(i, local);
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
			Block<BlockCodeInfo> cb = this.getCurrentBlock();
			if (! cb.isClosed()) cb.getData().addLocal(i, local);
		}
	}

	@Override
	protected void passLocalByVal(Local local, int index) {		
		int i = this.incrementIndex();
		Block<BlockCodeInfo> cb = this.getCurrentBlock();
		if (! cb.isClosed()) cb.getData().addLocal(i, local);
	}
	
	@Override
	protected void passLocalByRef(Local local, int index) {
		int i = this.incrementIndex();
		Block<BlockCodeInfo> cb = this.getCurrentBlock();
		if (! cb.isClosed()) cb.getData().addLocal(i, local);
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
		Block<BlockCodeInfo> cb = this.getCurrentBlock();
		if (! cb.isClosed()) cb.getData().addGlobal(name);		
	}
	
	@Override
	protected void visitIndirection(Indirection indirection) {
		Block<BlockCodeInfo> cb = this.getCurrentBlock();
		if (! cb.isClosed()) cb.getData().incrementIndirection();
		super.visitIndirection(indirection);
	}

	@Override
	protected Block<BlockCodeInfo> getNewBlock(int index, EntryId entryId, Blocks<BlockCodeInfo> blocks, String[] params) {
		Block<BlockCodeInfo> result = new Block<BlockCodeInfo>(index, entryId, blocks, new BlockCodeInfo());
		result.getData().setFormals(params);
		return result;
	}
}
