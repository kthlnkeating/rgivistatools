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

package com.raygroupintl.vista.repository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.raygroupintl.m.parsetree.RoutineFactory;

public class RepositoryInfo {
	public static class PackageInRepository extends VistaPackage {
		public RoutineFactory rf;

		public PackageInRepository(String packageName, String directoryName, RoutineFactory rf) {
			super(packageName, directoryName);
			this.rf = rf;
		}
		
		@Override
		public RoutineFactory getRoutineFactory() {
			return this.rf;
		}
		
		@Override
		public Path getPath() {
			String vistaFOIARoot = RepositoryInfo.getLocation();
			if (vistaFOIARoot == null) {
				vistaFOIARoot = "";
			}
			String dir = this.getDirectoryName();
			Path path = Paths.get(vistaFOIARoot, "Packages", dir);
			return path;			
		}
		
		public boolean contains(String routineName) {
			List<String> prefixes = this.getPrefixes();
			for (String prefix : prefixes) {
				if (routineName.startsWith(prefix)) {
					return true;
				}
			}
			return false;
		}		
	}
	
	private List<PackageInRepository> packages = new ArrayList<PackageInRepository>();
	private Map<String, PackageInRepository> packagesByName = new HashMap<String, PackageInRepository>();
	private TreeMap<String, PackageInRepository> packagesByPrefix = new TreeMap<String, PackageInRepository>();
	private RoutineFactory rf;	
	
	private RepositoryInfo(RoutineFactory rf) {
		this.rf = rf;
	}
	
	private void addPackage(PackageInRepository packageInfo) {
		this.packages.add(packageInfo);
		this.packagesByName.put(packageInfo.getPackageName(), packageInfo);
		for (String prefix : packageInfo.getPrefixes()) {
			this.packagesByPrefix.put(prefix, packageInfo);
		}
	}
	
	public static String getLocation() {
		return System.getenv("VistA-FOIA");
	}
	
	public PackageInRepository getPackage(String packageName) {
		return this.packagesByName.get(packageName);
	}
	
	public List<PackageInRepository> getPackages(List<String> packageNames) {
		List<PackageInRepository> result = new ArrayList<PackageInRepository>(packageNames.size());
		for (String name : packageNames) {
			PackageInRepository p = this.getPackage(name);
			result.add(p);
		}
		return result;
	}
	
	public List<PackageInRepository> getAllPackages() {
		Collection<PackageInRepository> pis = this.packagesByPrefix.values();
		List<PackageInRepository> result = new ArrayList<PackageInRepository>(pis.size());
		for (PackageInRepository pi : pis) {
			result.add(pi);
		}
		return result;
	}
	
	public Path getPackagePath(String packageName) {
		String vistaFOIARoot = RepositoryInfo.getLocation();
		if (vistaFOIARoot == null) {
			vistaFOIARoot = "";
		}
		VistaPackage pi = this.getPackage(packageName);
		String dir = pi.getDirectoryName();
		Path path = Paths.get(vistaFOIARoot, "Packages", dir);
		return path;
	}
	
	public VistaPackage getPackageFromPrefix(String prefix) {
		return this.packagesByPrefix.get(prefix);
	}
	
	public VistaPackage getPackageFromRoutineName(String routineName) {
		VistaPackage result = null;
		int n = routineName.length();
		for (int i=0; i<n; ++i) {
			String prefix = routineName.substring(0, i);
			VistaPackage pi = this.packagesByPrefix.get(prefix);
			if ((pi == null) && (result != null)) {
				return result;
			}
			result = pi;
		}
		if (result == null) {
			result = new PackageInRepository("UNCATEGORIZED", "Uncategorized", this.rf);
		}
		return result;
	}
	
	public static RepositoryInfo getInstance(Scanner scanner, RoutineFactory rf) throws IOException {
		RepositoryInfo r = new RepositoryInfo(rf);
		PackageInRepository packageInfo = null;
		scanner.nextLine();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] pieces = line.split(",");
			int n = pieces.length;
			if (pieces[0].length() > 0) {
				packageInfo = new PackageInRepository(pieces[0], pieces[1], rf);
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
	
	public static RepositoryInfo getInstance(String root, RoutineFactory rf) throws IOException {
		Path path = Paths.get(root, "Packages.csv");
		Scanner scanner = new Scanner(path);
		RepositoryInfo r = getInstance(scanner, rf);
		scanner.close();
		return r;
	}

	public static RepositoryInfo getInstance(RoutineFactory rf) throws IOException {
		String root = RepositoryInfo.getLocation();
		return getInstance(root, rf);
	}
}
