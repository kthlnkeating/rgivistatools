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

package com.pwc.us.rgi.m.parsetree.filter;

import com.pwc.us.rgi.m.parsetree.data.EntryId;
import com.pwc.us.rgi.struct.Filter;
import com.pwc.us.rgi.vista.repository.RepositoryInfo;
import com.pwc.us.rgi.vista.repository.VistaPackage;

public class ExcludeFilemanCallFanoutFilter implements Filter<EntryId> {
	private String sourcePackagePrefix;
	private RepositoryInfo repositoryInfo;
	
	public ExcludeFilemanCallFanoutFilter(RepositoryInfo repositoryInfo, String sourcePackageString) {
		this.repositoryInfo = repositoryInfo;
		this.sourcePackagePrefix = sourcePackageString;
	}
	
	@Override
	public boolean isValid(EntryId input) {
		if (input != null) {
			String routineName = input.getRoutineName();
			if (routineName == null) return true;
			VistaPackage vp = this.repositoryInfo.getPackageFromRoutineName(routineName);
			String prefix = vp.getDefaultPrefix();
			if (prefix.equals("DI") || (prefix.equals("XU"))) {
				return this.sourcePackagePrefix.equals(prefix);
			} else {
				return true;
			}
		}
		return false;
	}
}
