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

import com.raygroupintl.vista.fnds.IFileAction;

public class MFileVisitor extends SimpleFileVisitor<Path> {
	private IFileAction action;
	private List<Path> paths;
	
	public MFileVisitor(IFileAction action) {
		this.action = action;
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
		if (this.action != null) {
			if (attr.isRegularFile()) {
				Path leaf = file.getFileName();
				if (leaf != null) {
					String name = leaf.toString();
					if (name.endsWith(".m")) {
						this.action.handle(file);
					}
				}
			}
			return FileVisitResult.CONTINUE;
		}	
		return FileVisitResult.TERMINATE;
	}

	public void addPath(Path path) {
		if (this.paths == null) {
			this.paths = new ArrayList<Path>();
		}
		this.paths.add(path);
	}
	
	public void addPath(String path) {
		Path p = Paths.get(path);
		this.addPath(p);
	}
	
	public void addVistAFOIA() {
		String vistaFOIARoot = System.getenv("VistA-FOIA");
		Path p = Paths.get(vistaFOIARoot);
		this.addPath(p);
	}

	public void run()  throws IOException {
		if (this.paths != null) for (Path path : this.paths) {
			Files.walkFileTree(path, this);
		}
	}
}
