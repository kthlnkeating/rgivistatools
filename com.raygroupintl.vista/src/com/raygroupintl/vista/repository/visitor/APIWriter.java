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

package com.raygroupintl.vista.repository.visitor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.raygroupintl.m.parsetree.data.APIData;
import com.raygroupintl.m.parsetree.data.APIDataStore;
import com.raygroupintl.m.parsetree.data.BlockWithAPIData;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.m.parsetree.filter.BasicSourcedFanoutFilter;
import com.raygroupintl.m.parsetree.filter.SourcedFanoutFilter;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.PassFilter;

public class APIWriter {	
	private FileWrapper fileWrapper;
	private BlocksSupply blocksSupply;
	private TerminalFormatter tf = new TerminalFormatter();
	private Map<String, String> replacementRoutines;
	private SourcedFanoutFilter filter = new BasicSourcedFanoutFilter(new PassFilter<EntryId>());
	
	public APIWriter(FileWrapper fileWrapper, BlocksSupply blocksSupply, Map<String, String> replacementRoutines) {
		this.fileWrapper = fileWrapper;
		this.blocksSupply = blocksSupply;
		this.replacementRoutines = replacementRoutines;
	}

	public void setFilter(SourcedFanoutFilter filter) {
		this.filter = filter;
	}
	
	private void writeAPIData(List<String> dataList, String title) {
		this.fileWrapper.write(this.tf.startList(title));
		if (dataList.size() > 0) {
			for (String data : dataList) {
				this.fileWrapper.write(this.tf.addToList(data));
			}
		} else {
			this.fileWrapper.write("--");
		}
		this.fileWrapper.writeEOL();		
	}
	
	private void writeAPIData(int count, String title) {
		this.fileWrapper.write(this.tf.startList(title));
		this.fileWrapper.write(String.valueOf(count));
		this.fileWrapper.writeEOL();		
	}
	
	private void write(EntryId entryId, APIDataStore store, String[] linePieces) {
		String routineName = entryId.getRoutineName();
		this.fileWrapper.writeEOL(" " + entryId.toString2());
		this.tf.setTab(12);
		Blocks rbs = this.blocksSupply.getBlocks(routineName);
		if (rbs == null) {
			this.fileWrapper.writeEOL("  ERROR: Invalid entry point");
		} else {
			String label = entryId.getLabelOrDefault();
			BlockWithAPIData lb = rbs.get(label);
			if (lb == null) {
				this.fileWrapper.writeEOL("  ERROR: Invalid entry point");
			} else {
				String[] formals = lb.getStaticData().getFormals();
				this.fileWrapper.write(this.tf.startList("FORMAL"));
				if ((formals == null) || (formals.length == 0)) {
					this.fileWrapper.write("--");
				} else {
					for (String formal : formals) {
						this.fileWrapper.write(this.tf.addToList(formal));					
					}
				}
				this.fileWrapper.writeEOL();

				APIData apiData = lb.getAPIData(this.blocksSupply, store, this.filter, this.replacementRoutines);
				this.writeAPIData(apiData.getAssumed(), "ASSUMED");
				this.writeAPIData(apiData.getGlobals(), "GLBS");
				this.writeAPIData(apiData.getReadCount(), "READ");
				this.writeAPIData(apiData.getWriteCount(), "WRITE");
				this.writeAPIData(apiData.getExecuteCount(), "EXEC");
				this.writeAPIData(apiData.getIndirectionCount(), "IND");
				this.writeAPIData(apiData.getFilemanGlobals(), "FMGLBS");
				this.writeAPIData(apiData.getFilemanCalls(), "FMCALLS");
			}
		}				
		this.fileWrapper.writeEOL();
	}
	
	public void auxWrite(String fanInFileName) {		
		try {
			APIDataStore store = new APIDataStore();
			int packageCount = 0;
			Path path = Paths.get(fanInFileName);
			Scanner scanner = new Scanner(path);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.endsWith(":")) {
					++packageCount;
					String name = line.substring(0, line.length()-1);
					this.fileWrapper.writeEOL();
					this.fileWrapper.writeEOL("--------------------------------------------------------------");
					this.fileWrapper.writeEOL();
					this.fileWrapper.writeEOL(String.valueOf(packageCount) + ". PACKAGE NAME: " + name);
					this.fileWrapper.writeEOL();
				} else {
					String[] linePieces = line.split("\\|");
					String[] pieces = linePieces[0].split("\\^");
					if ((pieces != null) && (pieces.length > 0)) {
						String label = pieces[0];
						if ((label != null) && (label.isEmpty())) label = null;
						String routineName = pieces.length > 1 ? pieces[1] : null;
						EntryId entryId = new EntryId(routineName, label);
						this.write(entryId, store, linePieces);
					}
				}
			}		
			if (packageCount > 0) {
				this.fileWrapper.writeEOL();
				this.fileWrapper.writeEOL("--------------------------------------------------------------");
			}
			scanner.close();
		} catch (IOException e) {
			this.fileWrapper.writeEOL("Unable to open file " + fanInFileName);
		}
	}
		
	public void writeEntries(List<String> entries) {
		if (this.fileWrapper.start()) {
			APIDataStore store = new APIDataStore();
			for (String entry : entries) {
				EntryId entryId = EntryId.getInstance(entry);
				String[] input = new String[]{entry, "", "", ""};
				this.write(entryId, store, input);
			}
			this.fileWrapper.stop();
		}
	}

	public void write(String fanInFileName) {
		if (this.fileWrapper.start()) {
			this.auxWrite(fanInFileName);
			this.fileWrapper.stop();
		}
	}
}
