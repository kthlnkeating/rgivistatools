package com.raygroupintl.vista.repository;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileSupply {
	private static final String ROOT_ENV = "VistA-FOIA";
	
	private List<Path> paths;
	private Set<Path> inclusionFilter;
	
	private class VistAFileVisitor extends SimpleFileVisitor<Path> {
		private List<Path> store = new ArrayList<Path>();
		
		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attr) throws IOException {
			Set<Path> filter = FileSupply.this.inclusionFilter;
			if ((filter != null) && (! filter.contains(path))) {
				return FileVisitResult.SKIP_SUBTREE;
			}		
			if (attr.isRegularFile()) {
				Path leaf = path.getFileName();
				if (leaf != null) {
					String name = leaf.toString();
					if (name.endsWith(".m")) {
						this.store.add(path);
					}
				}
			}	
			return FileVisitResult.CONTINUE;
		}

		public List<Path> getFiles() {
			return this.store;
		}
	}
	
	public void addPath(Path path) {
		if (path.getRoot() == null) {
			String vistaFOIARoot = System.getenv(ROOT_ENV);
			if (vistaFOIARoot != null) {
				path = Paths.get(vistaFOIARoot, path.toString());
			}
		}		
		if (this.paths == null) {
			this.paths = new ArrayList<Path>();
		}
		this.paths.add(path);
	}
	
	public void addInclusionFilter(Path path) {
		if (this.inclusionFilter == null) {
			this.inclusionFilter = new HashSet<Path>();
		}
		this.inclusionFilter.add(path);
	}
		
	public void addPath(String path) {
		Path p = Paths.get(path);
		this.addPath(p);
	}
	
	public void addPackageFilter(String packageName) {
		String vistaFOIARoot = System.getenv(ROOT_ENV);
		if (vistaFOIARoot == null) {
			vistaFOIARoot = "";
		}
		Path path = Paths.get(vistaFOIARoot, "Packages", packageName);
		this.addInclusionFilter(path);
	}
	
	public void addPackageFilters(List<String> packageNames) {
		for (String packageName : packageNames) {
			this.addPackageFilter(packageName);
		}
	}

	public List<Path> getFiles() throws IOException {
		VistAFileVisitor visitor = this.new VistAFileVisitor();
		if (this.paths == null) {
			String vistaFOIARoot = System.getenv(ROOT_ENV);
			Path path = Paths.get(vistaFOIARoot);
			Files.walkFileTree(path, visitor);
		} else {			
			for (Path path : this.paths) {
				Files.walkFileTree(path, visitor);
			}
		}
		return visitor.getFiles();
	}
	
	public static List<Path> getAllMFiles() throws IOException {
		FileSupply s = new FileSupply();
		List<Path> paths = s.getFiles();
		return paths;
	}

	public static List<Path> getAllMFiles(Collection<String> packageNames) throws IOException {
		FileSupply s = new FileSupply();
		for (String packageName : packageNames) {
			s.addPackageFilter(packageName);
		}
		List<Path> paths = s.getFiles();
		return paths;
	}

	public static List<Path> getAllMFiles(String... packageNames) throws IOException {
		List<String> asList = Arrays.asList(packageNames);
		return FileSupply.getAllMFiles(asList);
	}
}
