package com.raygroupintl.m.parsetree;

public class Visitor {
	protected void visitErrorNode(ErrorNode error) {		
	}
	
	protected void visitIntegerLiteral(IntegerLiteral literal) {		
	}
		
	protected <T extends Node> void visitNodes(Nodes<T> nodes) {
		for (Node node : nodes.getNodes()) {
			if (node != null) node.accept(this);
		}
	}
	
	private void visitAdditionalNodeHolder(AdditionalNodeHolder nodeHolder) {
		Node addlNode = nodeHolder.getAdditionalNode();
		if (addlNode != null) {
			addlNode.accept(this);
		}
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
		this.visitNodes(actualList);
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
	
		
	protected void visitQuit(QuitCmd quitCmd) {
		quitCmd.acceptSubNodes(this);
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
	
	
	protected void visitExtrinsic(Extrinsic extrinsic) {
		this.visitAdditionalNodeHolder(extrinsic);
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
	
	protected void visitBlock(EntryList block) {
		this.visitNodes(block);		
	}
	
	protected void visitDoBlock(DoBlock doBlock) {
		Node postCondition = doBlock.getPostCondition();
		if (postCondition != null) {
			postCondition.accept(this);
		}
		EntryList entryList = doBlock.getEntryList();
		if (entryList != null) {			
			this.visitNodes(entryList);
		}
	}
	
	protected void visitEntryTag(Entry entry) {
		this.visitNodes(entry);
	}
		
	protected void visitRoutine(Routine routine) {
		EntryList entryList = routine.getEntryList();
		this.visitNodes(entryList);
	}

	protected void visitRoutinePackage(RoutinePackage routinePackage) {
		this.visitNodes(routinePackage);
	}
}
