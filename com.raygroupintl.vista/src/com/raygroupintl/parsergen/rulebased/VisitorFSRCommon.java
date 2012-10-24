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

package com.raygroupintl.parsergen.rulebased;

import com.raygroupintl.parser.Token;

public abstract class VisitorFSRCommon<T extends Token> {
	public void visitDefault(FactorySupplyRule<T> fsr) {		
	}
	
	public void visitChar(FSRChar<T> fsr) {	
		this.visitDefault(fsr);
	}
	
	public void visitChoice(FSRChoice<T> fsr) {		
		this.visitDefault(fsr);
	}
	
	public void visitConst(FSRConst<T> fsr) {		
		this.visitDefault(fsr);
	}
	
	public void visitCopy(FSRCopy<T> fsr) {		
		this.visitDefault(fsr);
	}

	public void visitCustom(FSRCustom<T> fsr) {		
		this.visitDefault(fsr);
	}

	public void visitDelimitedList(FSRDelimitedList<T> fsr) {		
		this.visitDefault(fsr);
	}

	public void visitEnclosedDelimitedList(FSREnclosedDelimitedList<T> fsr) {		
		this.visitDefault(fsr);
	}

	public void visitForkedSequence(FSRForkedSequence<T> fsr) {		
		this.visitDefault(fsr);
	}

	public void visitList(FSRList<T> fsr) {		
		this.visitDefault(fsr);
	}

	public void visitSequence(FSRSequence<T> fsr) {		
		this.visitDefault(fsr);
	}

	public void visitString(FSRString<T> fsr) {		
		this.visitDefault(fsr);
	}

	public void visitStringSequence(FSRStringSequence<T> fsr) {		
		this.visitDefault(fsr);
	}
}