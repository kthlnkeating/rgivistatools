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

package com.raygroupintl.output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Terminal {
	public abstract boolean start();
	
	public abstract void stop();
	
	public abstract boolean write(String data);
	
	public abstract boolean writeEOL();

	public boolean writeEOL(String data) {
		if (this.write(data)) {
			return this.writeEOL();
		}
		return false;
	}
	
	public void writeFormatted(String title, int count, TerminalFormatter tf) {
		this.write(tf.startList(title));
		this.write(String.valueOf(count));
		this.writeEOL();		
	}

	public void writeFormatted(String title, String[] dataArray, TerminalFormatter tf) {
		this.write(tf.startList(title));
		if ((dataArray == null) || (dataArray.length == 0)) {
			this.write("--");
		} else {
			for (String data : dataArray) {
				this.write(tf.addToList(data));					
			}
		}
		this.writeEOL();		
	}

	public void writeFormatted(String title, Collection<String> dataList, TerminalFormatter tf) {
		this.write(tf.startList(title));
		if (dataList.size() > 0) {
			for (String data : dataList) {
				this.write(tf.addToList(data));
			}
		} else {
			this.write("--");
		}
		this.writeEOL();		
	}

	public void writeSortedFormatted(String title, Collection<String> dataList, TerminalFormatter tf) {
		List<String> sorted = new ArrayList<String>(dataList);
		Collections.sort(sorted);		
		this.writeFormatted(title, sorted, tf);
	}
}
