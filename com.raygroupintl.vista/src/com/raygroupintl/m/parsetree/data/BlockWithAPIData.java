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

package com.raygroupintl.m.parsetree.data;

public class BlockWithAPIData extends Block {
	private BlockStaticAPIData staticData = new BlockStaticAPIData();
	
	public BlockWithAPIData(int index, EntryId entryId, Blocks siblings) {
		super(index, entryId, siblings);
	}

	public BlockStaticAPIData getStaticData() {
		return this.staticData;
	}
		
	@Override
	public APIData getInitialAccumulativeData() {
		return this.staticData.getDynamicData();
	}
		
	@Override
	public APIData getInitialAdditiveData() {
		return new APIData();
	}
		
	@Override
	public void mergeAccumulativeToAdditive(APIData target, APIData source) {
		target.setAssumeds(source);
	}

	@Override
	public int mergeAccumulative(APIData target, APIData source, int sourceIndex) {
		return target.mergeAccumulative(this.getStaticData(), source, sourceIndex);
	}
	
	@Override
	public void mergeAdditiveTo(APIData target) {
		target.mergeAdditive(this.getStaticData());		
	}
}
