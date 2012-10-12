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

import java.util.HashMap;
import java.util.Map;

public class TypeAwareTFStore {
	private Map<String, TFCharacter> characters;
	private Map<String, TFString> strings;
	private Map<String, TFChoice> choices;
	private Map<String, TFConstant> constants;
	private Map<String, TFSequence> sequences;
	private Map<String, TFList> lists;
	private Map<String, TFDelimitedList> delimitedList;
	private Map<String, TFForkedSequence> forkedSequences;
	
	public void putCharacter(String name, TFCharacter tf) {
		if (this.characters == null) {
			this.characters = new HashMap<String, TFCharacter>();
		}
		this.characters.put(name, tf);
	}
	
	public void putString(String name, TFString tf) {
		if (this.strings == null) {
			this.strings = new HashMap<String, TFString>();
		}
		this.strings.put(name, tf);
	}
	
	public void putChoice(String name, TFChoice tf) {
		if (this.choices == null) {
			this.choices = new HashMap<String, TFChoice>();
		}
		this.choices.put(name, tf);
	}
	
	public void putConstant(String name, TFConstant tf) {
		if (this.constants == null) {
			this.constants = new HashMap<String, TFConstant>();
		}
		this.constants.put(name, tf);
	}
	
	public void putSequence(String name, TFSequence tf) {
		if (this.sequences == null) {
			this.sequences = new HashMap<String, TFSequence>();
		}
		this.sequences.put(name, tf);
	}
	
	public void putList(String name, TFList tf) {
		if (this.lists == null) {
			this.lists = new HashMap<String, TFList>();
		}
		this.lists.put(name, tf);
	}
	
	public void putDelimitedList(String name, TFDelimitedList tf) {
		if (this.delimitedList == null) {
			this.delimitedList = new HashMap<String, TFDelimitedList>();
		}
		this.delimitedList.put(name, tf);
	}
	
	public void putForkedSequence(String name, TFForkedSequence tf) {
		if (this.forkedSequences == null) {
			this.forkedSequences = new HashMap<String, TFForkedSequence>();
		}
		this.forkedSequences.put(name, tf);
	}
	
	public TFCharacter getCharacter(String name) {
		if (this.characters == null) {
			return null;
		}
		return this.characters.get(name);
	}
	
	public TFString getString(String name) {
		if (this.strings == null) {
			return null;
		}
		return this.strings.get(name);
	}
	
	public TFChoice getChoice(String name) {
		if (this.choices == null) {
			return null;
		}
		return this.choices.get(name);
	}
	
	public TFConstant getConstant(String name) {
		if (this.constants == null) {
			return null;
		}
		return this.constants.get(name);
	}
	
	public TFSequence getSequence(String name) {
		if (this.sequences == null) {
			return null;
		}
		return this.sequences.get(name);
	}
	
	public TFList getList(String name) {
		if (this.lists == null) {
			return null;
		}
		return this.lists.get(name);
	}
	
	public TFDelimitedList geDelimitedList(String name) {
		if (this.delimitedList == null) {
			return null;
		}
		return this.delimitedList.get(name);
	}
	
	public TFForkedSequence getForkedSequence(String name) {
		if (this.forkedSequences == null) {
			return null;
		}
		return this.forkedSequences.get(name);
	}
}
