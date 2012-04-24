package com.raygroupintl.vista.repository.test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import com.raygroupintl.vista.repository.FileSupply;
import com.raygroupintl.vista.repository.PackageInfo;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class GeneralTest {
	@Test
	public void test() {
		String root = FileSupply.getRoot();
		Assert.assertTrue("No location for repository root is specified.", (root != null) && (root.length() > 0));

		try {
			RepositoryInfo ri = RepositoryInfo.getInstance(root);
			PackageInfo pi = ri.getPackage("PROBLEM LIST");
			Assert.assertNotNull(pi);
			String dirname = pi.getDirectoryName();
			FileSupply fs = new FileSupply();
			fs.addPackage(dirname);
			List<Path> paths = fs.getFiles();
			Assert.assertTrue("No Problem List file found.", paths.size() > 0);
			for (Path path : paths) {
				String fileName = path.getFileName().toString();
				PackageInfo pib = ri.getPackageFromRoutineName(fileName);
				Assert.assertEquals(pi, pib);
			}			
		} catch (IOException e) {
			Assert.fail("IO Exception.  Repository info file likely inexistent.");
		}
	}
}
