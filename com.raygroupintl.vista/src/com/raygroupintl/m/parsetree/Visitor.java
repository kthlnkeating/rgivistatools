package com.raygroupintl.m.parsetree;

public class Visitor {
	private <T extends Node> void visitBlock(Block<T> block) {
		for (T node : block.getNodes()) {
			node.accept(this);
		}
	}

	protected void visitErrorNode(ErrorNode error) {		
	}
	
	protected void visitExpression(Expression expr) {
	}
	
	protected void visitCompoundExpression(CompoundExpression compoundExpression) {
		for (Expression expr : compoundExpression.getSubExpressions()) {
			expr.accept(this);
		}
	}
	
	protected void visitAtomicCommand(AtomicCommand command) {
		Expression postCondExpr = command.getPostcondition();
		if (postCondExpr != null) {
			postCondExpr.accept(this);
		}
		ExpressionArray arguments = command.getArguments();
		if (arguments != null) {
			arguments.accept(this);
		}
	}
	
	protected void visitMultiCommand(MultiCommand<?> command) {
		Expression postCondExpr = command.getPostcondition();
		if (postCondExpr != null) {
			postCondExpr.accept(this);
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
