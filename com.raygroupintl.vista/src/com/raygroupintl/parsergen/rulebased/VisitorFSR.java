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

public interface VisitorFSR<T extends Token> {
	void visitChar(FSRChar<T> fsr);
	void visitChoice(FSRChoice<T> fsr);
	void visitConst(FSRConst<T> fsr);
	void visitCopy(FSRCopy<T> fsr);
	void visitCustom(FSRCustom<T> fsr);
	void visitDelimitedList(FSRDelimitedList<T> fsr);
	void visitEnclosedDelimitedList(FSREnclosedDelimitedList<T> fsr);
	void visitForkedSequence(FSRForkedSequence<T> fsr);
	void visitList(FSRList<T> fsr);
	void visitSequence(FSRSequence<T> fsr);
	void visitString(FSRString<T> fsr);
	void visitStringSequence(FSRStringSequence<T> fsr);
}
