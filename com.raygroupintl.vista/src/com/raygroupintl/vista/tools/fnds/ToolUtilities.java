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

package com.raygroupintl.vista.tools.fnds;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToolUtilities {
	private final static Logger LOGGER = Logger.getLogger(ToolUtilities.class.getName());

	public static Object readSerializedRoutineObject(String directory, String routineName, String extension) {
		File file = new File(directory, routineName + extension);
		if (! file.isFile()) {
			LOGGER.log(Level.WARNING, "File for routine " + routineName + " (" + file.toString() + ") does not exist.");
			return null;
		}		
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object result = ois.readObject();
			ois.close();
			return result;
		} catch(IOException | ClassNotFoundException ioException) {
			String msg = "Unable to read object from file " + file.toString();
			LOGGER.log(Level.SEVERE, msg, ioException);
			return null;
		}		
	}

}
