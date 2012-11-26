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

package com.raygroupintl.vista.tools;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.raygroupintl.m.parsetree.filter.BasicSourcedFanoutFilter;
import com.raygroupintl.m.parsetree.filter.ExcludeFilemanCallFanoutFilter;
import com.raygroupintl.m.parsetree.filter.ExcludeNonPkgCallFanoutFilter;
import com.raygroupintl.m.parsetree.filter.ExcludeNonRtnFanoutFilter;
import com.raygroupintl.m.parsetree.filter.PercentRoutineFanoutFilter;
import com.raygroupintl.m.parsetree.filter.SourcedFanoutFilter;
import com.raygroupintl.vista.repository.RepositoryInfo;

public abstract class EntryInfoTool extends Tool {
	public EntryInfoTool(CLIParams params) {
		super(params);
	}
	
	protected int getFanoutFlag() {
		try {
			int result = Integer.parseInt(this.params.flag);
			if (result < 0) return 0;
			if (result > 3) return 3;
			return result;
		} catch(Throwable t) {
		}
		return 0;
	}
	
	protected SourcedFanoutFilter getFilter(RepositoryInfo ri) {
		int flag = this.getFanoutFlag();
		switch (flag) {
			case 1:
				return new ExcludeFilemanCallFanoutFilter(ri);
			case 2:
				return new ExcludeNonPkgCallFanoutFilter(ri);
			case 3:
				return new ExcludeNonRtnFanoutFilter();
			default:
				PercentRoutineFanoutFilter filter = new PercentRoutineFanoutFilter();
				return new BasicSourcedFanoutFilter(filter);
		}
	}
	
	protected List<String> getEntries() {
		if (this.params.inputFile != null) {
			try {
				Path path = Paths.get(this.params.inputFile);
				Scanner scanner = new Scanner(path);
				List<String> result = new ArrayList<String>();
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					result.add(line);
				}		
				scanner.close();
				return result;
			} catch (IOException e) {
				MRALogger.logError("Unable to open file " + this.params.inputFile);
				return null;
			}
		} else {
			return this.params.entries;
		}			
	}
}
