package com.raygroupintl.parsergen.rulebased;

import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TypeAwareTFStore;

public class FSRVisitingTFStore<T extends Token> extends TypeAwareTFStore<T> implements VisitorFSR<T> {
	@Override
	public void visitChar(FSRChar<T> fsr) {		
	}

	@Override
	public void visitChoice(FSRChoice<T> fsr) {		
	}

	@Override
	public void visitConst(FSRConst<T> fsr) {		
	}

	@Override
	public void visitCopy(FSRCopy<T> copy) {		
	}

	@Override
	public void visitCustom(FSRCustom<T> fsr) {		
	}

	@Override
	public void visitDelimitedList(FSRDelimitedList<T> fsr) {		
	}

	@Override
	public void visitEnclosedDelimitedList(FSREnclosedDelimitedList<T> fsr) {		
	}

	@Override
	public void visitForkedSequence(FSRForkedSequence<T> fsr) {		
	}

	@Override
	public void visitList(FSRList<T> fsr) {		
	}

	@Override
	public void visitSequence(FSRSequence<T> fsr) {		
	}

	@Override
	public void visitString(FSRString<T> fsr) {		
	}
}
