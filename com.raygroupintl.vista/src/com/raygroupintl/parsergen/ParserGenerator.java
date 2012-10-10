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

package com.raygroupintl.parsergen;


public abstract class ParserGenerator {
	protected abstract TokenFactoryStore getStore();
	
	public <T> T generate(Class<T> cls) throws ParseException {
		try {
			T target = cls.newInstance();
			TokenFactoryStore store = this.getStore();
			store.add(target);
			store.addAssumed();
			store.update(cls);
			return target;
		} catch (IllegalAccessException iae) {
			throw new ParseException(iae);
		} catch (InstantiationException ine) {
			throw new ParseException(ine);
		} catch (ClassNotFoundException cnf) {
			throw new ParseException(cnf);
		} catch (NoSuchMethodException nsm) {
			throw new ParseException(nsm);			
		}
	}
}
