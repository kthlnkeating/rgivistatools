package com.raygroupintl.vista.mtoken.test;

import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Logger;

import org.junit.Test;

import com.raygroupintl.vista.fnds.IFileAction;
import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFLine;
import com.raygroupintl.vista.repository.MFileVisitor;
import com.raygroupintl.vista.tools.MRoutineAnalyzer;

import junit.framework.Assert;

public class TLineTest1 {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	@Test
	public void testAll() {
		IFileAction action = new IFileAction() {			
			@Override
			public void handle(Path path) {
				try {
					Scanner scanner = new Scanner(path);
					int index = 0;
					//if (! path.getFileName().toString().equals("ZIS4ONT.m")) return;
					LOGGER.info(path.getFileName().toString());
					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						//LOGGER.info(">>> " + line);
						TFLine f = TFLine.getInstance(MVersion.CACHE);
						IToken tokens = f.tokenize(line, 0);
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
