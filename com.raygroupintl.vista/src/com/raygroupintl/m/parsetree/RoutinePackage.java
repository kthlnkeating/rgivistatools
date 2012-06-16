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

package com.raygroupintl.m.parsetree;

import java.nio.file.Path;
import java.util.List;

public abstract class RoutinePackage extends BasicNode {
	public void acceptSubNodes(Visitor visitor) {
		RoutineFactory rf = this.getRoutineFactory();
		for (Path path : this.getPaths()) {
			Node node = rf.getNode(path);
			node.accept(visitor);
		}
	}
	
	public abstract boolean contains(String routineName);
	
	public abstract List<Path> getPaths();
	
	public abstract RoutineFactory getRoutineFactory();
	
	public abstract String getPackageName();
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visitRoutinePackage(this);
	}
}
