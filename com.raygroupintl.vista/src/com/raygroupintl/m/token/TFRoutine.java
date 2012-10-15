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

package com.raygroupintl.m.token;

import java.io.IOException;
import java.nio.file.Path;

import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.parser.StringPieceImpl;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parsergen.ObjectSupply;

public class TFRoutine {
	private TokenFactory tfLine;
	private ObjectSupply mAdapterSupply = new MObjectSupply();
	
	public TFRoutine(MTFSupply supply) {
		this.tfLine = supply.line;
	}
	
	public static MLine recoverFromError(String line, SyntaxErrorException e) {
		Token error = new MSyntaxError(0, new StringPieceImpl(line), 0);
		MSequence result = new MSequence(5);
		result.addToken(error);
		for (int i=0; i<4; ++i) result.addToken(null);
		return new MLine(result);
	}
	
	public MRoutine tokenize(MRoutineContent content) {
		String name = content.getName();
		MRoutine result = new MRoutine(name);
		int index = 0;
		String tagName = "";
		for (String line : content.getLines()) {
			MLine tokens = null;
			try {
				Text text = new Text(line);
				tokens = (MLine) this.tfLine.tokenize(text, this.mAdapterSupply);
			} catch (SyntaxErrorException e) {
				tokens = recoverFromError(line, e);
			}
			String lineTagName = tokens.getTag();
			if (lineTagName != null) {
				tagName = lineTagName;
				index = 0;
			}
			tokens.setIdentifier(tagName, index);
			result.add(tokens);
			++index;
		}		
		return result;
	}
	
	public MRoutine tokenize(Path path) throws IOException,  SyntaxErrorException {
		MRoutineContent content = MRoutineContent.getInstance(path);					
		MRoutine r = this.tokenize(content);
		return r;
	}
}
