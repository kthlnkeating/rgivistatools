package com.raygroupintl.vista.repository;

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

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class VistaPackage {   //extends RoutinePackage {
	public static class FileInfo {
		private String number;
		private String name;

		public FileInfo(String number, String name) {
			this.number = number;
			this.name = name;
		}
		
		public String getNumber() {
			return number;
		}
		
		public String getName() {
			return this.name;
		}
	}
		
	private String packageName;
	private String directoryName;
	private List<String> prefixes;
	private List<FileInfo> files;
	
	public VistaPackage(String packageName, String directoryName) {
		this.packageName = packageName;
		this.directoryName = directoryName;
	}
	
	//@Override
	public String getPackageName() {
		return this.packageName;
	}
	
	public String getDirectoryName() {
		return this.directoryName;
	}
	
	public void addFile(String number, String name) {
		FileInfo file = new FileInfo(number, name);
		if (this.files == null) {
			this.files = new ArrayList<FileInfo>();
		}
		this.files.add(file);
	}
	
	public void addPrefix(String prefix) {
		if (this.prefixes == null) {
			this.prefixes = new ArrayList<String>();
		}
		this.prefixes.add(prefix);
	}
	
	public List<FileInfo> getFiles() {
		if (this.prefixes == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(this.files);
		}
	}
	
	public List<String> getPrefixes() {
		if (this.prefixes == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(this.prefixes);
		}
	}
	
	@Override
	public boolean equals(Object rhs) {
		if ((rhs != null) && (rhs instanceof VistaPackage)) {	
			VistaPackage p = (VistaPackage) rhs;
			return this.packageName.equals(p.packageName) && (this.directoryName == p.directoryName); 
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		String hashString = this.packageName + "\n" + this.directoryName;
		int result = hashString.hashCode(); 
		return result;
	}
	
	public abstract Path getPath();
	
	public List<Path> getRoutineFilePaths() throws IOException {
		Path packagePath = this.getPath();
		FileSupply fs = new FileSupply();
		fs.addPath(packagePath);
		List<Path> paths = fs.getFiles();
		return paths;		
	}
}
