//---------------------------------------------------------------------------
// Copyright 2013 PwC
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

package com.pwc.us.rgi.m.tool;

import com.pwc.us.rgi.m.parsetree.data.Fanout;
import com.pwc.us.rgi.m.parsetree.visitor.BlockRecorderFactory;
import com.pwc.us.rgi.m.tool.entry.AccumulatingBlocksSupply;
import com.pwc.us.rgi.m.tool.entry.BlockData;
import com.pwc.us.rgi.m.tool.entry.BlocksSupply;
import com.pwc.us.rgi.m.tool.entry.RecursionSpecification;
import com.pwc.us.rgi.struct.Filter;

public class CommonToolParams {
	private ParseTreeSupply parseTreeSupply;
	private RecursionSpecification recursionSpec = new RecursionSpecification();

	public CommonToolParams(ParseTreeSupply parseTreeSupply) {
		this.parseTreeSupply = parseTreeSupply;		
	}
	
	public ParseTreeSupply getParseTreeSupply() {
		return this.parseTreeSupply;
	}
	
	public void setRecursionSpecification(RecursionSpecification recursionSpec) {
		this.recursionSpec = recursionSpec;
	}
	
	public RecursionSpecification getRecursionSpecification() {
		return this.recursionSpec;
	}

	public Filter<Fanout> getFanoutFilter() {
		return this.recursionSpec.getFanoutFilter();
	}
	
	public <F extends Fanout, T extends BlockData<F>> BlocksSupply<F, T> getBlocksSupply(BlockRecorderFactory<F, T> f) {
		return new AccumulatingBlocksSupply<F, T>(this.parseTreeSupply, f);
	}
}
