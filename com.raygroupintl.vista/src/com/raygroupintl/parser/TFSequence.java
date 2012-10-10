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
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.parsergen.ObjectSupply;

public class TFSequence extends TokenFactory {
	public enum ValidateResult {
		CONTINUE, BREAK, NULL_RESULT
	}

	private final static class RequiredFlags {
		private boolean[] flags;
		private int firstRequired = Integer.MAX_VALUE;
		private int lastRequired = Integer.MIN_VALUE;

		public RequiredFlags() {
			this(0);
		}

		public RequiredFlags(int size) {
			flags = new boolean[size];
		}

		public void set(boolean[] flags) {
			this.flags = flags;
			this.firstRequired = Integer.MAX_VALUE;			
			this.lastRequired = Integer.MIN_VALUE;			
			int index = 0;
			for (boolean b : flags) {
				if (b) {
					if (this.firstRequired == Integer.MAX_VALUE) {
						this.firstRequired = index;
					}
					this.lastRequired = index;
				}
				++index;
			}		
		}
		
		public int getFirstRequiredIndex() {
			return this.firstRequired;
		}
		
		public int getLastRequiredIndex() {
			return this.lastRequired;
		}
		
		public boolean isRequired(int i) {
			return this.flags[i];
		}		
	}
	
	private TokenFactory[] factories = {};
	private RequiredFlags requiredFlags = new RequiredFlags();

	private Constructor<? extends CompositeToken> constructor;
	private Constructor<? extends CompositeToken> constructor2;
	
	public TFSequence(String name) {		
		super(name);
	}
	
	public TFSequence(String name, TokenFactory... factories) {
		this(name);
		this.factories = factories;
		this.requiredFlags = new RequiredFlags(factories.length);
	}
		
	public void setFactories(TokenFactory[] factories, boolean[] requiredFlags) {
		if (requiredFlags.length != factories.length) throw new IllegalArgumentException();
		this.factories = factories;
		this.requiredFlags.set(requiredFlags);
	}

	public void setRequiredFlags(boolean... requiredFlags) {
		if (requiredFlags.length != this.factories.length) throw new IllegalArgumentException();
		this.requiredFlags.set(requiredFlags);
	}
	
	@Override
	public int getSequenceCount() {
		return this.factories.length;
	}
	
	protected ValidateResult validateNull(int seqIndex, CompositeToken foundTokens, boolean noException) throws SyntaxErrorException {
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
	
	protected boolean validateEnd(int seqIndex, CompositeToken foundTokens, boolean noException) throws SyntaxErrorException {
		if (seqIndex < this.requiredFlags.getLastRequiredIndex()) {
			if (noException) return false;
			throw new SyntaxErrorException();
		}
		return true;
	}
	
	@Override
	public final CompositeToken tokenizeOnly(Text text, ObjectSupply objectSupply) throws SyntaxErrorException {
		if (text.onChar()) {
			int length = this.factories.length;
			try {
				CompositeToken foundTokens = this.constructor != null ? this.constructor.newInstance(length) : objectSupply.newSequence(length);
				return this.tokenizeCommon(text, objectSupply, 0, foundTokens, false);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException t) {
				String clsName =  this.getClass().getName();
				Logger.getLogger(clsName).log(Level.SEVERE, "Unable to instantiate " + clsName + ".", t);
			}
		}		
		return null;
	}
	
	@Override
	public void setSequenceTargetType(Class<? extends CompositeToken> cls) {
		this.constructor = this.getConstructor(cls, Integer.TYPE);
		this.constructor2 = this.getConstructor(cls, TokenStore.class);
		
	}
	
	public CompositeToken convertSequence(CompositeToken compositeToken) {
		if (this.constructor2 == null) return compositeToken;
		try {
			return this.constructor2.newInstance(compositeToken);						
		} catch (Throwable t) {
			String clsName =  this.getClass().getName();
			Logger.getLogger(clsName).log(Level.SEVERE, "Unable to instantiate " + clsName + ".", t);			
		}
		return null;
	}
	
	final CompositeToken tokenizeCommon(Text text, ObjectSupply objectSupply, int firstSeqIndex, CompositeToken foundTokens, boolean noException) throws SyntaxErrorException {
		int factoryCount = this.factories.length;
		for (int i=firstSeqIndex; i<factoryCount; ++i) {
			TokenFactory factory = this.factories[i];
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
		return this.factories[index];
	}
}
