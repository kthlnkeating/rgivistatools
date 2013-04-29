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

package com.raygroupintl.m.tool.assumedvariables;

import java.util.HashSet;
import java.util.Set;

import com.raygroupintl.m.parsetree.Local;
import com.raygroupintl.m.parsetree.Node;
import com.raygroupintl.m.parsetree.OpenCloseUseCmdNodes;
import com.raygroupintl.m.parsetree.data.BlockData;
import com.raygroupintl.m.parsetree.data.CallArgument;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.visitor.BlockRecorder;

public class AssumedVariablesRecorder extends BlockRecorder<AssumedVariablesBlockData> {
	private boolean underDeviceParameter;
	
	public void reset() {
		this.underDeviceParameter = false;
		super.reset();
	}
	
	private void addOutput(Local local) {
		AssumedVariablesBlockData d = this.getCurrentBlockAttachedObject();
		if (d != null) {
			d.addLocal(local);	
		}
	}
	
	@Override
	protected void postUpdateFanout(EntryId fanout, CallArgument[] callArguments) {
	}
	
	@Override
	protected void visitDeviceParameters(OpenCloseUseCmdNodes.DeviceParameters deviceParameters) {
		boolean current = this.underDeviceParameter;
		this.underDeviceParameter = true;
		super.visitDeviceParameters(deviceParameters);
		this.underDeviceParameter = current;
	}
		
	@Override
	protected void setLocal(Local local, Node rhs) {
		AssumedVariablesBlockData d = this.getCurrentBlockAttachedObject();
		if (d != null) {
			this.addOutput(local);
		}
	}
	
	@Override
	protected void mergeLocal(Local local, Node rhs) {
		this.addOutput(local);
	}
	
	@Override
	protected void killLocal(Local local) {		
		this.addOutput(local);
	}
	
	@Override
	protected void newLocal(Local local) {
		int i = this.getIndex();
		AssumedVariablesBlockData d = this.getCurrentBlockAttachedObject();
		if (d != null) d.addNewed(i, local);
	}
	
	private static Set<String> DEVICE_PARAMS = new HashSet<String>();
	static {
		DEVICE_PARAMS.add("LINE");
		DEVICE_PARAMS.add("NOLINE");
		DEVICE_PARAMS.add("VT");
		DEVICE_PARAMS.add("NOESCAPE");
		DEVICE_PARAMS.add("ESCAPE");
	}
	
	
	private boolean isDeviceParameter(Local local) {
		String name = local.getName().toString();
		if (DEVICE_PARAMS.contains(name)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void visitLocal(Local local) {
		if ((! this.underDeviceParameter) || (! isDeviceParameter(local))) { 
			super.visitLocal(local);
			AssumedVariablesBlockData d = this.getCurrentBlockAttachedObject();
			if (d != null) d.addLocal(local);
		}
	}

	@Override
	protected void passLocalByVal(Local local, int index) {		
		AssumedVariablesBlockData d = this.getCurrentBlockAttachedObject();
		if (d != null) d.addLocal(local);
	}
	
	@Override
	protected void passLocalByRef(Local local, int index) {
		AssumedVariablesBlockData d = this.getCurrentBlockAttachedObject();
		if (d != null) d.addLocal(local);
		super.passLocalByRef(local, index);
	}

	@Override
	protected BlockData<AssumedVariablesBlockData> getNewBlockData(EntryId entryId, String[] params) {
		BlockData<AssumedVariablesBlockData> result = new BlockData<AssumedVariablesBlockData>(entryId, new AssumedVariablesBlockData());
		result.getAttachedObject().setFormals(params);
		return result;
	}
}
