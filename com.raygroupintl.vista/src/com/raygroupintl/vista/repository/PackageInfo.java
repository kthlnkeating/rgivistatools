package com.raygroupintl.vista.repository;

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
		this.files.add(file);
	}
	
	public void addPrefix(String prefix) {
		this.prefixes.add(prefix);
	}
	
	public List<FileInfo> getFiles() {
		return Collections.unmodifiableList(this.files);
	}
	
	public List<String> getPrefixes() {
		return Collections.unmodifiableList(this.prefixes);
	}
}
