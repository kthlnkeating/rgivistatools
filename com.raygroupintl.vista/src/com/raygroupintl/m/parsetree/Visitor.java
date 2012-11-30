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

package com.raygroupintl.m.parsetree;

public class Visitor {
	protected void visitErrorNode(ErrorNode error) {		
	}
	
	protected void visitIntegerLiteral(IntegerLiteral literal) {		
	}
		
	protected void visitStringLiteral(StringLiteral literal) {		
	}
		
	protected void passStringLiteral(StringLiteral literal, int index) {		
	}
			
	protected void visitNumberLiteral(NumberLiteral literal) {		
	}
		
	protected void passNumberLiteral(NumberLiteral literal, int index) {		
	}
			
	private void visitAdditionalNodeHolder(AdditionalNodeHolder nodeHolder) {
		Node addlNode = nodeHolder.getAdditionalNode();
		if (addlNode != null) {
			addlNode.accept(this);
		}
	}
	
	protected void visitPostConditional(PostConditional pc) {
		this.visitAdditionalNodeHolder(pc);
	}
	
	protected void setLocal(Local local, Node rhs) {		
	}
	
	protected void mergeLocal(Local local, Node rhs) {		
	}
	
	protected void killLocal(Local local) {		
	}
	
	protected void newLocal(Local local) {		
	}
	
	protected void passLocalByVal(Local local, int index) {		
	}
	
	protected void passLocalByRef(Local local, int index) {		
	}

	protected void visitLocal(Local local) {
		local.acceptSubNodes(this);
	}
	
	
	protected void visitGlobal(Global global) {
		global.acceptSubNodes(this);
	}
	
	protected void visitNakedGlobal(NakedGlobal global) {
		global.acceptSubNodes(this);
	}
	
	protected void visitSSVN(StructuredSystemVariable ssvn) {
		ssvn.acceptSubNodes(this);
	}
	
	protected void visitIntrinsicFunction(IntrinsicFunction intrinsicFunction) {
		intrinsicFunction.acceptSubNodes(this);
	}
	
	protected void visitIntrinsicVariable(IntrinsicVariable intrinsicVariable) {
	}
	
	protected void visitActualList(ActualList actualList) {
		actualList.acceptSubNodes(this);
	}
	
	protected void visitIndirection(Indirection indirection) {
		this.visitAdditionalNodeHolder(indirection);
	}
		
	protected void visitIndirectFanoutLabel(IndirectFanoutLabel label) {
		this.visitAdditionalNodeHolder(label);
	}
		
	protected void visitFanoutLabel(FanoutLabel label) {
		this.visitAdditionalNodeHolder(label);
	}
		
	protected void visitIndirectFanoutRoutine(IndirectFanoutRoutine routine) {
		this.visitAdditionalNodeHolder(routine);
	}
		
	protected void visitEnvironmentFanoutRoutine(EnvironmentFanoutRoutine routine) {
		this.visitAdditionalNodeHolder(routine);
	}
		
	protected void visitFanoutRoutine(FanoutRoutine routine) {
		this.visitAdditionalNodeHolder(routine);
	}
		
	protected void visitAtomicCommand(AtomicCommand atomicCommand) {
		this.visitAdditionalNodeHolder(atomicCommand);
	}
	
	
	protected void visitForLoop(ForLoop forLoop) {
		forLoop.acceptSubNodes(this);
	}
	

	protected void visitExternalDo(ExternalDo externalDo) {
		this.visitAtomicCommand(externalDo);
	}
	
	protected void visitAtomicDo(AtomicDo atomicDo) {
		this.visitAtomicCommand(atomicDo);
	}
	
	protected void visitAtomicGoto(AtomicGoto atomicGoto) {
		this.visitAtomicCommand(atomicGoto);
	}
	
	
	protected void visitIndirectAtomicSet(SetCmdNodes.IndirectAtomicSet indirectAtomicSet) {
		indirectAtomicSet.acceptSubNodes(this);
	}
	
	protected void visitMultiAtomicSet(SetCmdNodes.MultiAtomicSet multiAtomicSet) {
		multiAtomicSet.acceptSubNodes(this);
	}
	
	protected void visitAtomicSet(SetCmdNodes.AtomicSet atomicSet) {
		atomicSet.acceptSubNodes(this);
	}
	
	protected void visitSet(SetCmdNodes.SetCmd setCmd) {
		setCmd.acceptSubNodes(this);
	}
	
	
	protected void visitAtomicOpenCmd(OpenCloseUseCmdNodes.AtomicOpenCmd atomicOpenCmd) {
		this.visitAtomicCommand(atomicOpenCmd);
	}
	
	protected void visitAtomicCloseCmd(OpenCloseUseCmdNodes.AtomicCloseCmd atomicCloseCmd) {
		this.visitAtomicCommand(atomicCloseCmd);
	}
	
	protected void visitAtomicUseCmd(OpenCloseUseCmdNodes.AtomicUseCmd atomicUseCmd) {
		this.visitAtomicCommand(atomicUseCmd);
	}
		
	protected void visitOpenCmd(OpenCloseUseCmdNodes.OpenCmd openCmd) {
		openCmd.acceptSubNodes(this);
	}

	protected void visitCloseCmd(OpenCloseUseCmdNodes.CloseCmd closeCmd) {
		closeCmd.acceptSubNodes(this);
	}

	protected void visitUseCmd(OpenCloseUseCmdNodes.UseCmd useCmd) {
		useCmd.acceptSubNodes(this);
	}

	protected void visitDeviceParameters(OpenCloseUseCmdNodes.DeviceParameters deviceParameters) {
		this.visitAdditionalNodeHolder(deviceParameters);
	}
		
	
	protected void visitQuit(QuitCmd quitCmd) {
		quitCmd.acceptSubNodes(this);
	}

	protected void visitReadCmd(ReadCmd readCmd) {
		readCmd.acceptSubNodes(this);
	}

	
	protected void visitWriteCmd(WriteCmd writeCmd) {
		writeCmd.acceptSubNodes(this);
	}

	
	protected void visitXecuteCmd(XecuteCmd xecuteCmd) {
		xecuteCmd.acceptSubNodes(this);
	}

		
	protected void visitIf(IfCmd ifCmd) {
		ifCmd.acceptSubNodes(this);
	}

	
	protected void visitElse(ElseCmd elseCmd) {
		elseCmd.acceptSubNodes(this);
	}

	
	protected void visitIndirectAtomicMerge(MergeCmdNodes.IndirectAtomicMerge indirectAtomicMerge) {
		indirectAtomicMerge.acceptSubNodes(this);
	}
	
	protected void visitAtomicMerge(MergeCmdNodes.AtomicMerge atomicMerge) {
		atomicMerge.acceptSubNodes(this);
	}
	
	protected void visitMerge(MergeCmdNodes.MergeCmd mergeCmd) {
		mergeCmd.acceptSubNodes(this);
	}
	
		
	protected void visitAtomicNew(NewCmdNodes.AtomicNew atomicNew) {
		atomicNew.acceptSubNodes(this);
	}
	
	protected void visitAllNewCmd(NewCmdNodes.AllNewCmd atomicNew) {
		atomicNew.acceptSubNodes(this);
	}
	
	protected void visitExclusiveAtomicNew(NewCmdNodes.ExclusiveAtomicNew atomicNew) {
		atomicNew.acceptSubNodes(this);
	}
	
	protected void visitNew(NewCmdNodes.NewCmd newCmd) {
		newCmd.acceptSubNodes(this);
	}
	
	
	protected void visitAtomicKill(KillCmdNodes.AtomicKill atomicKill) {
		atomicKill.acceptSubNodes(this);
	}
	
	protected void visitAllKillCmd(KillCmdNodes.AllKillCmd atomicKill) {
		atomicKill.acceptSubNodes(this);
	}
	
	protected void visitExclusiveAtomicKill(KillCmdNodes.ExclusiveAtomicKill atomicKill) {
		atomicKill.acceptSubNodes(this);
	}
	
	protected void visitKill(KillCmdNodes.KillCmd killCmd) {
		killCmd.acceptSubNodes(this);
	}
	
	protected void visitCacheClassMethod(CacheClassMethod ccm) {
		this.visitAdditionalNodeHolder(ccm);
	}

	protected void visitCacheSystemCall(CacheSystemCall csc) {
		this.visitAdditionalNodeHolder(csc);
	}

	protected void visitCacheObjectDoRoutine(CacheObjectDoRoutine codr) {
		this.visitAdditionalNodeHolder(codr);
	}

	
	protected void visitExtrinsic(Extrinsic extrinsic) {
		this.visitAdditionalNodeHolder(extrinsic);
	}

	protected void visitObjectMethodCall(ObjectMethodCall omc) {
		this.visitAdditionalNodeHolder(omc);
	}

	protected void visitDo(Do d) {
		d.acceptSubNodes(this);
	}
	
	protected void visitGoto(Goto g) {
		g.acceptSubNodes(this);
	}
	
	protected void visitGenericCommand(GenericCommand g) {
		g.acceptSubNodes(this);
	}
	
	protected void visitLine(Line line) {
		line.acceptSubNodes(this);
	}
	
	protected void visitEntry(Entry entry) {
		entry.acceptSubNodes(this);
	}
		
	protected void visitEntryList(EntryList entryList) {
		entryList.acceptSubNodes(this);
	}
		
	protected void visitDoBlock(DoBlock doBlock) {
		doBlock.acceptSubNodes(this);
	}
	
	protected void visitRoutine(Routine routine) {
		routine.acceptSubNodes(this);
	}
}
