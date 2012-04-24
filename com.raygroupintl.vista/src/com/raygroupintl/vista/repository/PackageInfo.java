package com.raygroupintl.vista.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackageInfo {
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
	
	public PackageInfo(String packageName, String directoryName) {
		this.packageName = packageName;
		this.directoryName = directoryName;
	}
	
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
		if ((rhs != null) && (rhs instanceof PackageInfo)) {	
			PackageInfo p = (PackageInfo) rhs;
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
}
