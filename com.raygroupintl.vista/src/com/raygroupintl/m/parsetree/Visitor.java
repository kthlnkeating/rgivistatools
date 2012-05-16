package com.raygroupintl.m.parsetree;

public class Visitor {
	private <T extends Node> void visitBlock(Block<T> block) {
		for (T node : block.getNodes()) {
			node.accept(this);
		}
	}

	protected void visitErrorNode(ErrorNode error) {		
	}
	
	protected void visitNodes(Nodes compoundExpression) {
		for (Node node : compoundExpression.getNodes()) {
			node.accept(this);
		}
	}
	
	protected void visitAtomicCommand(AtomicCommand command) {
		Nodes postCondExpr = command.getPostcondition();
		if (postCondExpr != null) {
			postCondExpr.accept(this);
		}
		NodeArray arguments = command.getArguments();
		if (arguments != null) {
			arguments.accept(this);
		}
	}
	
	protected void visitMultiCommand(MultiCommand<?> command) {
		Node postCondition = command.getPostCondition();
		if (postCondition != null) {
			postCondition.accept(this);
		}
		this.visitBlock(command);
	}
	
	protected void visitForBlock(ForBlock forBlock) {
		this.visitBlock(forBlock);
	}
	
	protected void visitDoBlock(DoBlock doBlock) {
		this.visitBlock(doBlock);
	}
	
	protected void visitLine(Line line) {
		this.visitBlock(line);
	}
	
	protected void visitEntryTag(EntryTag entry) {
		this.visitBlock(entry);
	}
		
	protected void visitRoutine(Routine routine) {
		this.visitBlock(routine);
	}
}
