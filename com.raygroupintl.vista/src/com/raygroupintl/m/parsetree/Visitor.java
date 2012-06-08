package com.raygroupintl.m.parsetree;

public class Visitor {
	protected void visitErrorNode(ErrorNode error) {		
	}
	
	protected void visitIntegerLiteral(IntegerLiteral literal) {		
	}
	
	
	protected void visitNodes(Nodes nodes) {
		for (Node node : nodes.getNodes()) {
			node.accept(this);
		}
	}
	
	private void visitMultiCommand(MultiCommand command) {
		Node postCondition = command.getPostCondition();
		if (postCondition != null) {
			postCondition.accept(this);
		}
		Node argument = command.getArgument();
		if (argument != null) {
			argument.accept(this);
		}
	}
	
	private void visitAdditionalNodeHolder(AdditionalNodeHolder nodeHolder) {
		Node addlNode = nodeHolder.getAdditionalNode();
		if (addlNode != null) {
			addlNode.accept(this);
		}
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
	
	protected void visitForBlock(ForBlock forBlock) {
		this.visitNodes(forBlock);
	}
	
	protected void visitDoBlock(DoBlock doBlock) {
		Node postCondition = doBlock.getPostCondition();
		if (postCondition != null) {
			postCondition.accept(this);
		}
		this.visitNodes(doBlock);
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
	
	protected void visitExtrinsic(Extrinsic extrinsic) {
		this.visitAdditionalNodeHolder(extrinsic);
	}

	protected void visitDo(Do d) {
		this.visitMultiCommand(d);
	}
	
	protected void visitGoto(Goto g) {
		this.visitMultiCommand(g);
	}
	
	protected void visitGenericCommand(GenericCommand g) {
		this.visitMultiCommand(g);
	}
	
	protected void visitLine(Line line) {
		this.visitNodes(line);
	}
	
	protected void visitEntryTag(EntryTag entry) {
		this.visitNodes(entry);
	}
		
	protected void visitRoutine(Routine routine) {
		this.visitNodes(routine);
	}

	protected void visitRoutinePackage(RoutinePackage routinePackage) {
		this.visitNodes(routinePackage);
	}
}
