package com.raygroupintl.vista.repository;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MFileVisitor extends SimpleFileVisitor<Path> {
	private List<Path> paths;
	private Set<Path> inclusionFilter;
	private List<Path> store;
	
	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attr) throws IOException {
		if ((this.inclusionFilter != null) && (! this.inclusionFilter.contains(path))) {
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

	public void addPath(Path path) {
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
		String vistaFOIARoot = System.getenv("VistA-FOIA");
		Path path = Paths.get(vistaFOIARoot, "Packages", packageName);
		this.addInclusionFilter(path);
	}
	
	public void addPackageFilters(List<String> packageNames) {
		for (String packageName : packageNames) {
			this.addPackageFilter(packageName);
		}
	}

	public void addVistAFOIA() {
		String vistaFOIARoot = System.getenv("VistA-FOIA");
		Path p = Paths.get(vistaFOIARoot);
		this.addPath(p);
	}
	
	public List<Path> getFiles() throws IOException {
		this.store = new ArrayList<Path>();
		this.run();
		return this.store;		
	}

	private void run() throws IOException {
		if (this.paths != null) {
			for (Path path : this.paths) {
				Files.walkFileTree(path, this);
			}
		}
	}
}
