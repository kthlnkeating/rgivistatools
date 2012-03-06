package com.raygroupintl.vista.mtoken.test;

import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.util.Scanner;

import org.junit.Test;

import com.raygroupintl.vista.fnds.IFileAction;
import com.raygroupintl.vista.mtoken.Line;
import com.raygroupintl.vista.repository.MFileVisitor;

import junit.framework.Assert;

public class LineTest1 {
	@Test
	public void testAll() {
		IFileAction action = new IFileAction() {			
			@Override
			public void handle(Path path) {
				try {
					Scanner scanner = new Scanner(path);
					int index = 0;
					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						Line tokens = Line.getInstance(line);
						String readLine = tokens.getStringValue();
						String msg = path.getFileName().toString() + " Line " +  String.valueOf(index);
						Assert.assertEquals("Different: " + msg, line, readLine);
						++index;
					}
				} catch(Throwable t) {
					fail("Exception: " + t.getMessage());
				}
			}
		};
		try {
			MFileVisitor v = new MFileVisitor(action);
			v.addVistAFOIA();
			v.run();
		} catch (Throwable t) {
			fail("Exception: " + t.getMessage());			
		}
	}
}
