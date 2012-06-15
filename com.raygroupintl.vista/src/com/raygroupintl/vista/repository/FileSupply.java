package com.raygroupintl.vista.repository;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileSupply {
	private List<Path> paths;
	
	private class VistAFileVisitor extends SimpleFileVisitor<Path> {
		private List<Path> store = new ArrayList<Path>();
		
		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attr) {
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
			String vistaFOIARoot = RepositoryInfo.getLocation();
			if (vistaFOIARoot != null) {
				path = Paths.get(vistaFOIARoot, path.toString());
			}
		}		
		if (this.paths == null) {
			this.paths = new ArrayList<Path>();
		}
		this.paths.add(path);
	}
	
	public void addPath(String path) {
		Path p = Paths.get(path);
		this.addPath(p);
	}
	
	public List<Path> getFiles() throws IOException {
		VistAFileVisitor visitor = this.new VistAFileVisitor();
		if (this.paths == null) {
			String vistaFOIARoot = RepositoryInfo.getLocation();
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
}
