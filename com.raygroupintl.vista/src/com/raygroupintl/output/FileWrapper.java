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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.raygroupintl.vista.repository.visitor.FanoutWriter;

public class FileWrapper {
	private final static Logger LOGGER = Logger.getLogger(FanoutWriter.class.getName());

	private String outputFileName;

	private FileOutputStream os;
	private String eol = Utility.getEOL();

	public FileWrapper(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	
	public boolean start() {
		try {
			File file = new File(this.outputFileName);
			this.os = new FileOutputStream(file);
			return true;
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Unable to open file " + this.outputFileName);						
			return false;
		}
	}
	
	public void stop() {
		try {
			if (this.os != null) {
				this.os.close();
				this.os = null;
			} else {
				LOGGER.log(Level.SEVERE, "Attempt to stop an unstarted file writing session for " + this.outputFileName);									
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error closing the file " + this.outputFileName);						
		}
	}

	public boolean write(String data) {
		try {
			if (this.os != null) {
				this.os.write(data.getBytes());
				return true;
			} else {
				LOGGER.log(Level.SEVERE, "Attempt to write unopened file " + this.outputFileName);												
			}
		}  catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error writing to file " + this.outputFileName);						
		}
		return false;
	}
	
	public boolean writeEOL() {
		return this.write(this.eol);
	}
}
