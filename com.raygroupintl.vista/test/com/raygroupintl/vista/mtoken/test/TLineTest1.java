package com.raygroupintl.vista.mtoken.test;

import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

import com.raygroupintl.vista.fnds.IFileAction;
import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFRoutine;
import com.raygroupintl.vista.mtoken.TLine;
import com.raygroupintl.vista.mtoken.TRoutine;
import com.raygroupintl.vista.repository.MFileVisitor;
import com.raygroupintl.vista.struct.MLineLocation;
import com.raygroupintl.vista.struct.MLocationedError;
import com.raygroupintl.vista.struct.MRoutineContent;
import com.raygroupintl.vista.tools.ErrorExemptions;
import com.raygroupintl.vista.tools.MRoutineAnalyzer;

import junit.framework.Assert;

public class TLineTest1 {
	private final static Logger LOGGER = Logger.getLogger(MRoutineAnalyzer.class.getName());

	@Test
	public void testAll() {
		final TFRoutine tf = TFRoutine.getInstance(MVersion.CACHE);
		final ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
		IFileAction action = new IFileAction() {			
			@Override
			public void handle(Path path) {
				try {
					MRoutineContent content = MRoutineContent.getInstance(path); 
					TRoutine r = tf.tokenize(content);
					List<String> lines = content.getLines();
					List<TLine> results = r.asList();
					int count = results.size();
					Assert.assertEquals(lines.size(), count);
					//if (! path.getFileName().toString().equals("ZIS4ONT.m")) return;
					LOGGER.info(path.getFileName().toString());
					for (int i=0; i<count; ++i) {
						//LOGGER.info(">>> " + line);
						String line = lines.get(i);
						TLine result = results.get(i);
						String readLine = result.getStringValue();
						String msg = path.getFileName().toString() + " Line " +  String.valueOf(i);
						Assert.assertEquals("Different: " + msg, line, readLine);
					}
					String name = r.getName();
					if (! exemptions.containsRoutine(name)) {
						Set<MLineLocation> locations = exemptions.getLines(name);
						List<MLocationedError> errors = r.getErrors(locations);
						Assert.assertEquals(errors.size(), 0);						
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
