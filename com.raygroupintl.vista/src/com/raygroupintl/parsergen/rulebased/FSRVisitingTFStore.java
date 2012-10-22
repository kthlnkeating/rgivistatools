package com.raygroupintl.parsergen.rulebased;

import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TypeAwareTFStore;

public class FSRVisitingTFStore<T extends Token> extends TypeAwareTFStore<T> implements FSRVisitor<T> {
	@Override
	public void addChar(FSRChar<T> fsr) {		
	}

	@Override
	public void addChoice(FSRChoice<T> fsr) {		
	}

	@Override
	public void addConst(FSRConst<T> fsr) {		
	}

	@Override
	public void addCopy(FSRCopy<T> copy) {		
	}

	@Override
	public void addCustom(FSRCustom<T> fsr) {		
	}

	@Override
	public void addDelimitedList(FSRDelimitedList<T> fsr) {		
	}

	@Override
	public void addEnclosedDelimitedList(FSREnclosedDelimitedList<T> fsr) {		
	}

	@Override
	public void addForkedSequence(FSRForkedSequence<T> fsr) {		
	}

	@Override
	public void addList(FSRList<T> fsr) {		
	}

	@Override
	public void addSequence(FSRSequence<T> fsr) {		
	}

	@Override
	public void addString(FSRString<T> fsr) {		
	}

	@Override
	public void addStringSequence(FSRStringSequence<T> fsr) {		
	}
}
