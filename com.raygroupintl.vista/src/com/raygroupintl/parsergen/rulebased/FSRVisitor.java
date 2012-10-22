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

public interface FSRVisitor<T extends Token> {
	void addChar(FSRChar<T> fsr);
	void addChoice(FSRChoice<T> fsr);
	void addConst(FSRConst<T> fsr);
	void addCopy(FSRCopy<T> copy);
	void addCustom(FSRCustom<T> fsr);
	void addDelimitedList(FSRDelimitedList<T> fsr);
	void addEnclosedDelimitedList(FSREnclosedDelimitedList<T> fsr);
	void addForkedSequence(FSRForkedSequence<T> fsr);
	void addList(FSRList<T> fsr);
	void addSequence(FSRSequence<T> fsr);
	void addString(FSRString<T> fsr);
	void addStringSequence(FSRStringSequence<T> fsr);
}
