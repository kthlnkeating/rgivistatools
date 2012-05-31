package com.raygroupintl.vista.repository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.parser.annotation.ParseException;
import com.raygroupintl.vista.repository.FileSupply;
import com.raygroupintl.vista.repository.VistaPackage;
import com.raygroupintl.vista.repository.RepositoryInfo;

public class GeneralTest {
	@Test
	public void test() {
		String root = RepositoryInfo.getLocation();
		Assert.assertTrue("No location for repository root is specified.", (root != null) && (root.length() > 0));

		try {
			MTFSupply supply = MTFSupply.getInstance(MVersion.CACHE);
			TFRoutine tf = new TFRoutine(supply);
			RepositoryInfo ri = RepositoryInfo.getInstance(root, tf);
			VistaPackage pi = ri.getPackage("PROBLEM LIST");
			Assert.assertNotNull(pi);
			Path packagePath = ri.getPackagePath("PROBLEM LIST");			
			FileSupply fs = new FileSupply();
			fs.addPath(packagePath);
			List<Path> paths = fs.getFiles();
			Assert.assertTrue("No Problem List file found.", paths.size() > 0);
			for (Path path : paths) {
				String fileName = path.getFileName().toString();
				VistaPackage pib = ri.getPackageFromRoutineName(fileName);
				Assert.assertEquals(pi, pib);
			}			
		} catch (IOException | ParseException e) {
			Assert.fail("IO Exception.  Repository info file likely inexistent.");
		}
	}
}
