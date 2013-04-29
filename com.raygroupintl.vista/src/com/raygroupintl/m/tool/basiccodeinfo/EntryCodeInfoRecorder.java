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

package com.raygroupintl.m.tool.basiccodeinfo;

import com.raygroupintl.m.parsetree.Global;
import com.raygroupintl.m.parsetree.Indirection;
import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.ReadCmd;
import com.raygroupintl.m.parsetree.WriteCmd;
import com.raygroupintl.m.parsetree.XecuteCmd;
import com.raygroupintl.m.parsetree.data.BlockData;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.CallArgumentType;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;
import com.raygroupintl.vista.repository.RepositoryInfo;
import com.raygroupintl.vista.repository.VistaPackage;

public class EntryCodeInfoRecorder extends BlockRecorder<CodeInfo> {
	private RepositoryInfo repositoryInfo;
	
	public EntryCodeInfoRecorder(RepositoryInfo ri) {
		this.repositoryInfo = ri;
	}

	public void reset() {
		super.reset();
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
	protected void setLocal(Local local, Node rhs) {
		CodeInfo d = this.getCurrentBlockAttachedObject();
		if (d != null) {
			String rn = this.getCurrentRoutineName();
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
	protected BlockData<CodeInfo> getNewBlockData(EntryId entryId, String[] params) {
		BlockData<CodeInfo> result = new BlockData<CodeInfo>(entryId, new CodeInfo());
		result.getAttachedObject().setFormals(params);
		return result;
	}
}
