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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.raygroupintl.m.parsetree.data.APIData;
import com.raygroupintl.m.parsetree.data.Block;
import com.raygroupintl.m.parsetree.data.Blocks;
import com.raygroupintl.m.parsetree.data.BlocksSupply;
import com.raygroupintl.m.parsetree.data.EntryId;
import com.raygroupintl.output.FileWrapper;
import com.raygroupintl.output.TerminalFormatter;
import com.raygroupintl.struct.Filter;
import com.raygroupintl.struct.PassFilter;

public class APIWriter {
	private FileWrapper fileWrapper;
	private BlocksSupply blocksSupply;
	private TerminalFormatter tf = new TerminalFormatter();
	private Map<String, String> replacementRoutines;
	private Filter<EntryId> filter = new PassFilter<EntryId>();
	
	public APIWriter(FileWrapper fileWrapper, BlocksSupply blocksSupply, Map<String, String> replacementRoutines) {
		this.fileWrapper = fileWrapper;
		this.blocksSupply = blocksSupply;
		this.replacementRoutines = replacementRoutines;
	}

	public void setFilter(Filter<EntryId> filter) {
		this.filter = filter;
	}
	
	private void write(String[] linePieces, int pieceIndex, String title) {
		this.fileWrapper.write(this.tf.startList(title));
		boolean has = false;
		if (linePieces.length > pieceIndex) {
			String piece = linePieces[pieceIndex];
			if ((piece != null) && (! piece.isEmpty())) {
				String[] sources = piece.split("\\,");
				Arrays.sort(sources);
 			    for (String source : sources) {
					this.fileWrapper.write(this.tf.addToList(source));
					has = true;
				}
			}
		}
		if (! has) {
			this.fileWrapper.write(this.tf.addToList("--"));			
		}
		this.fileWrapper.writeEOL();		
	}
	
	private void writeAPIData(List<String> dataList, String title) {
		this.fileWrapper.write(this.tf.startList(title));
		for (String data : dataList) {
			this.fileWrapper.write(this.tf.addToList(data));
		}
		this.fileWrapper.writeEOL();		
	}
	
	private void write(EntryId entryId, String[] linePieces) {
		String routineName = entryId.getRoutineName();
		this.fileWrapper.writeEOL(" " + entryId.toString());
		this.tf.setTab(21);
		this.write(linePieces, 1, "CALLING PACKAGES ");
		this.write(linePieces, 3, "CALLING RPC's    ");
		this.write(linePieces, 2, "CALLING OPTIONS  ");
		this.tf.setTab(12);
		Blocks rbs = this.blocksSupply.getBlocks(routineName);
		if (rbs == null) {
			this.fileWrapper.writeEOL(this.tf.titled("ERROR", "Routine " + routineName + " is missing."));
		} else {
			String label = entryId.getLabelOrDefault();
			Block lb = rbs.get(label);
			if (lb == null) {
				this.fileWrapper.writeEOL(this.tf.titled("ERROR", "Tag " + entryId.toString() + " is missing."));
			} else {
				String[] formals = lb.getFormals();
				this.fileWrapper.write(this.tf.startList("FORMAL"));
				if ((formals == null) || (formals.length == 0)) {
					this.fileWrapper.write("--");
				} else {
					for (String formal : formals) {
						this.fileWrapper.write(this.tf.addToList(formal));					
					}
				}
				this.fileWrapper.writeEOL();

				APIData apiData = lb.getAPIData(this.blocksSupply, this.filter, this.replacementRoutines);
				//this.writeAPIData(apiData.getInputs(), "INPUT");
				//this.writeAPIData(apiData.getOutputs(), "OUTPUT");
				this.writeAPIData(apiData.getAssumed(), "ASSUMED");
				this.writeAPIData(apiData.getGlobals(), "GLBS");
			}
		}				
		this.fileWrapper.writeEOL();
		this.fileWrapper.writeEOL();		
	}
	
	public void auxWrite(String fanInFileName) {		
		try {
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
						String routineName = pieces.length > 1 ? pieces[1] : null;
						EntryId entryId = new EntryId(routineName, label);
						this.write(entryId, linePieces);
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
		
	public void writeEntry(String entryIdValue) {
		if (this.fileWrapper.start()) {
			EntryId entryId = EntryId.getInstance(entryIdValue);
			String[] input = new String[]{entryIdValue, "", "", ""};
			this.write(entryId, input);
			this.fileWrapper.stop();
		}
	}

	public void writeEntries(List<String> entries) {
		if (this.fileWrapper.start()) {
			for (String entry : entries) {
				EntryId entryId = EntryId.getInstance(entry);
				String[] input = new String[]{entry, "", "", ""};
				this.write(entryId, input);
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
