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
	
	private RepositoryInfo() {		
	}
	
	private void addPackage(PackageInfo packageInfo) {
		this.packages.add(packageInfo);
		this.packagesByName.put(packageInfo.getPackageName(), packageInfo);
	}
	
	public PackageInfo getPackage(String packageName) {
		return this.packagesByName.get(packageName);
	}
	
	public static RepositoryInfo getInstance(Scanner scanner) throws IOException {
		RepositoryInfo r = new RepositoryInfo();
		PackageInfo packageInfo = null;
		scanner.nextLine();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] pieces = line.split(",");
			if (pieces[0].length() > 0) {
				packageInfo = new PackageInfo(pieces[0], pieces[1]);
				r.addPackage(packageInfo);
			}
			if (pieces[2].length() > 0) {
				packageInfo.addPrefix(pieces[2]);
			}
			if (pieces[3].length() > 0) {
				packageInfo.addFile(pieces[3], pieces[4]);
			}
		}
		return r;		
	}
	
	public static RepositoryInfo getInstance(String vistaFOIARoot) throws IOException {
		Path path = Paths.get(vistaFOIARoot);
		Scanner scanner = new Scanner(path);
		RepositoryInfo r = getInstance(scanner);
		scanner.close();
		return r;
	}

	public static RepositoryInfo getInstance() throws IOException {
		String vistaFOIARoot = System.getenv("VistA-FOIA");
		return getInstance(vistaFOIARoot);
	}
}
