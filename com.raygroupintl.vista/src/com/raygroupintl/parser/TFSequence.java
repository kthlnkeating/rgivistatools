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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.parsergen.ObjectSupply;

public class TFSequence extends TokenFactory {
	public enum ValidateResult {
		CONTINUE, BREAK, NULL_RESULT
	}

	private final static class RequiredFlags {
		private List<Boolean> flags;
		private int firstRequired = Integer.MAX_VALUE;
		private int lastRequired = Integer.MIN_VALUE;

		public RequiredFlags() {
			this.flags = new ArrayList<Boolean>();
		}

		public RequiredFlags(int size) {
			this.flags = new ArrayList<Boolean>(size);
		}

		public void add(boolean b) {
			int n = this.flags.size();			
			this.flags.add(b);
			if (b) {
				if (this.firstRequired == Integer.MAX_VALUE) {
					this.firstRequired = n;
				}
				this.lastRequired = n;
			}
		}
		
		public int getFirstRequiredIndex() {
			return this.firstRequired;
		}
		
		public int getLastRequiredIndex() {
			return this.lastRequired;
		}
		
		public boolean isRequired(int i) {
			return this.flags.get(i);
		}		
	}
	
	private List<TokenFactory> factories = new ArrayList<TokenFactory>();
	private RequiredFlags requiredFlags = new RequiredFlags();

	private Constructor<? extends CompositeToken> constructor;
	
	public TFSequence(String name) {		
		super(name);
	}
	
	public TFSequence(String name, int length) {		
		super(name);
		this.factories = new ArrayList<TokenFactory>(length);
		this.requiredFlags = new RequiredFlags(length);
	}
	
	public void reset(int length) {
		this.factories = new ArrayList<TokenFactory>(length);
		this.requiredFlags = new RequiredFlags(length);		
	}
	
	public void add(TokenFactory tf, boolean required) {
		this.factories.add(tf);
		this.requiredFlags.add(required);
	}
	
	@Override
	public int getSequenceCount() {
		return this.factories.size();
	}
		
	protected ValidateResult validateNull(int seqIndex, SequenceOfTokens foundTokens, boolean noException) throws SyntaxErrorException {
		int firstRequired = this.requiredFlags.getFirstRequiredIndex();
		int lastRequired = this.requiredFlags.getLastRequiredIndex();
		
		if ((seqIndex < firstRequired) || (seqIndex > lastRequired)) {
			return ValidateResult.CONTINUE;
		}		
		if (seqIndex == firstRequired) {
			if (noException) return ValidateResult.NULL_RESULT;
			if (! foundTokens.isAllNull()) {
				throw new SyntaxErrorException();
			}
			return ValidateResult.NULL_RESULT;
		}
		if (this.requiredFlags.isRequired(seqIndex)) {
			if (noException) return ValidateResult.NULL_RESULT;
			throw new SyntaxErrorException();
		} else {
			return ValidateResult.CONTINUE;
		}
	}
	
	protected boolean validateEnd(int seqIndex, SequenceOfTokens foundTokens, boolean noException) throws SyntaxErrorException {
		if (seqIndex < this.requiredFlags.getLastRequiredIndex()) {
			if (noException) return false;
			throw new SyntaxErrorException();
		}
		return true;
	}
	
	@Override
	public final CompositeToken tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		if (text.onChar()) {
			int length = this.factories.size();
			SequenceOfTokens foundTokens = new SequenceOfTokens(length);
			foundTokens = this.tokenizeCommon(text, objectSupply, 0, foundTokens, false);
			return this.convertSequence(foundTokens, objectSupply);
		}		
		return null;
	}
	
	public void setSequenceTargetType(Class<? extends CompositeToken> cls) {
		this.constructor = this.getConstructor(cls, Tokens.class);
		
	}
	
	public CompositeToken convertSequence(SequenceOfTokens compositeToken, ObjectSupply objectSupply) {
		if (compositeToken == null) return null;
		if (this.constructor == null) {
			return objectSupply.newSequence(compositeToken);
		} else {
			try {
				return this.constructor.newInstance(compositeToken);						
			} catch (Throwable t) {
				String clsName =  this.getClass().getName();
				Logger.getLogger(clsName).log(Level.SEVERE, "Unable to instantiate " + clsName + ".", t);			
			}
			return null;
		}
	}
	
	final SequenceOfTokens tokenizeCommon(Text text, ObjectSupply objectSupply, int firstSeqIndex, SequenceOfTokens foundTokens, boolean noException) throws SyntaxErrorException {
		int factoryCount = this.factories.size();
		for (int i=firstSeqIndex; i<factoryCount; ++i) {
			TokenFactory factory = this.factories.get(i);
			Token token = null;
			try {
				token = factory.tokenize(text, objectSupply);				
			} catch (SyntaxErrorException e) {
				if (noException) return null;
				throw e;
			}
			
			if (token == null) {
				ValidateResult vr = this.validateNull(i, foundTokens, noException);
				if (vr == ValidateResult.BREAK) break;
				if (vr == ValidateResult.NULL_RESULT) return null;
			}

			foundTokens.addToken(token);
			if (token != null) {				
				if (text.onEndOfText() && (i < factoryCount-1)) {
					if (! this.validateEnd(i, foundTokens, noException)) return null;
					break;
				}
			}
		}
		if (foundTokens.isAllNull()) return null;
		return foundTokens;
	}
	
	public TokenFactory getFactory(int index) {
		return this.factories.get(index);
	}
}
