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

package com.raygroupintl.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import com.raygroupintl.struct.Filter;

public class IOUtil {
	private static class DirectoryFlattener extends SimpleFileVisitor<Path> {
		private Path destDirPath;
		private Filter<Path> filter;
		
		public DirectoryFlattener(Path destDirPath, Filter<Path> filter) {
			this.destDirPath = destDirPath;
			this.filter = filter;
		}
		
		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attr) throws IOException {
			boolean valid = this.filter.isValid(path);
			if (attr.isDirectory()) {
				return valid ? FileVisitResult.CONTINUE : FileVisitResult.SKIP_SUBTREE;
			}
			if (valid && attr.isRegularFile()) {
				Path leaf = path.getFileName();
				Path destinationPath = Paths.get(this.destDirPath.toString(), leaf.toString());
				Files.copy(path, destinationPath);
			}	
			return FileVisitResult.CONTINUE;
		}
	}
	
	private static Path getDirectoryPath(String directory) throws IOException {
		try {
			Path path = Paths.get(directory);
			if (! Files.isDirectory(path)) throw new IOException(directory + " is not a directory");
			return path;
		} catch (InvalidPathException e) {
			throw new IOException(e);
		}		
	}
	
	public static void flattenDirectory(String rootDirectory, String destinationDirectory, Filter<Path> pathFilter) throws IOException {
		try {
			Path rootPath = IOUtil.getDirectoryPath(rootDirectory);
			Path destinationPath = IOUtil.getDirectoryPath(destinationDirectory);
			DirectoryFlattener df = new DirectoryFlattener(destinationPath, pathFilter);
			Files.walkFileTree(rootPath, df);
		} catch (InvalidPathException e) {
			throw new IOException(e);
		}
	}
}
