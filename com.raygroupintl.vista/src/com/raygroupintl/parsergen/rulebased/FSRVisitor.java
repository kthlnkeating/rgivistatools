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

public interface FSRVisitor {
	void addChar(FSRChar fsr);
	void addChoice(FSRChoice fsr);
	void addConst(FSRConst fsr);
	void addCopy(FSRCopy copy);
	void addCustom(FSRCustom fsr);
	void addDelimitedList(FSRDelimitedList fsr);
	void addEnclosedDelimitedList(FSREnclosedDelimitedList fsr);
	void addForkedSequence(FSRForkedSequence fsr);
	void addList(FSRList fsr);
	void addSequence(FSRSequence fsr);
	void addString(FSRString fsr);
	void addStringSequence(FSRStringSequence fsr);
}
