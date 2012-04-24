package com.raygroupintl.vista.mtoken.test;

import static org.junit.Assert.fail;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFRoutine;
import com.raygroupintl.vista.mtoken.TLine;
import com.raygroupintl.vista.mtoken.TRoutine;
import com.raygroupintl.vista.repository.MFileVisitor;
import com.raygroupintl.vista.struct.MLineLocation;
import com.raygroupintl.vista.struct.MLocationedError;
import com.raygroupintl.vista.struct.MRoutineContent;
import com.raygroupintl.vista.tools.ErrorExemptions;

import junit.framework.Assert;

public class VistAFOIATest {
	@Test
	public void testAll() {
		final TFRoutine tf = TFRoutine.getInstance(MVersion.CACHE);
		final ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
		try {
			MFileVisitor v = new MFileVisitor();
			v.addVistAFOIA();
			List<Path> paths = v.getFiles();
			for (Path path : paths) {
				//if (! path.toString().endsWith("PRCAUDT.m")) return;
				//byte[] b = Files.readAllBytes(path);
				//String text = new String(b);
				//String n = path.getFileName().toString().split(".m")[0];
				//System.out.print(n + '\n');
				//TRoutine r = tf.tokenize(n, text, 0);
				//String tokenValue = r.getStringValue();					
				//Assert.assertEquals("Different: " + n, text, tokenValue);					
				MRoutineContent content = MRoutineContent.getInstance(path); 
				TRoutine r = tf.tokenize(content);
				List<String> lines = content.getLines();
				List<TLine> results = r.asList();
				int count = results.size();
				Assert.assertEquals(lines.size(), count);
				for (int i=0; i<count; ++i) {
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
			}
		} catch (Throwable t) {
			fail("Exception: " + t.getMessage());			
		}
	}
}
