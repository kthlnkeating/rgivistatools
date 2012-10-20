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

package com.raygroupintl.parser;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.charlib.Predicate;
import com.raygroupintl.parsergen.ObjectSupply;

public class TFString extends TokenFactory {
	private Predicate predicate;
	private Constructor<? extends Token> constructor;
	
	public TFString(String name, Predicate predicate) {
		super(name);
		this.predicate = predicate;
	}
		
	@Override
	public Token tokenizeOnly(Text text, ObjectSupply objectSupply) {
		TextPiece p = text.extractPiece(this.predicate);
		return this.convertString(p, objectSupply);
	}

	public void setStringTargetType(Class<? extends Token> cls, Class<TextPiece> textPieceCls) {
		this.constructor = this.getConstructor(cls, textPieceCls);		
	}

	public Token convertString(TextPiece p, ObjectSupply objectSupply) {
		if (p == null) {
			return null;
		} else if (this.constructor == null) {
			return objectSupply.newString(p);
		} else {
			try {
				return this.constructor.newInstance(p);						
			} catch (Throwable t) {
				String clsName =  this.getClass().getName();
				Logger.getLogger(clsName).log(Level.SEVERE, "Unable to instantiate " + clsName + ".", t);			
			}
			return null;
		}
	}
}
