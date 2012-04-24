package com.raygroupintl.vista.repository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RepositoryInfo {
	private List<PackageInfo> packages = new ArrayList<PackageInfo>();
	private Map<String, PackageInfo> packagesByName = new HashMap<String, PackageInfo>();
	private Map<String, PackageInfo> packagesByPrefix = new HashMap<String, PackageInfo>();
	
	private RepositoryInfo() {		
	}
	
	private void addPackage(PackageInfo packageInfo) {
		this.packages.add(packageInfo);
		this.packagesByName.put(packageInfo.getPackageName(), packageInfo);
		for (String prefix : packageInfo.getPrefixes()) {
			this.packagesByPrefix.put(prefix, packageInfo);
		}
	}
	
	public PackageInfo getPackage(String packageName) {
		return this.packagesByName.get(packageName);
	}
	
	public PackageInfo getPackageFromPrefix(String prefix) {
		return this.packagesByPrefix.get(prefix);
	}
	
	public PackageInfo getPackageFromRoutineName(String routineName) {
		PackageInfo result = null;
		int n = routineName.length();
		for (int i=0; i<n; ++i) {
			String prefix = routineName.substring(0, i);
			PackageInfo pi = this.packagesByPrefix.get(prefix);
			if ((pi == null) && (result != null)) {
				return result;
			}
			result = pi;
		}
		if (result == null) {
			result = new PackageInfo("UNCATEGORIZED", "Uncategorized");
		}
		return result;
	}
	
	public static RepositoryInfo getInstance(Scanner scanner) throws IOException {
		RepositoryInfo r = new RepositoryInfo();
		PackageInfo packageInfo = null;
		scanner.nextLine();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] pieces = line.split(",");
			int n = pieces.length;
			if (pieces[0].length() > 0) {
				packageInfo = new PackageInfo(pieces[0], pieces[1]);
				if ((n > 2) && (pieces[2].length() > 0)) {
					packageInfo.addPrefix(pieces[2]);
				}
				if ((n > 3) && (pieces[3].length() > 0)) {
					packageInfo.addFile(pieces[3], pieces[4]);
				}
				r.addPackage(packageInfo);
			}
		}
		return r;		
	}
	
	public static RepositoryInfo getInstance(String root) throws IOException {
		Path path = Paths.get(root, "Packages.csv");
		Scanner scanner = new Scanner(path);
		RepositoryInfo r = getInstance(scanner);
		scanner.close();
		return r;
	}

	public static RepositoryInfo getInstance() throws IOException {
		String root = FileSupply.getRoot();
		return getInstance(root);
	}
}
