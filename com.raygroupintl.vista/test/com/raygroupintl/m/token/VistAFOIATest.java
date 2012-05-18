package com.raygroupintl.m.token;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.raygroupintl.m.parsetree.visitor.ErrorRecorder;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.ObjectInRoutine;
import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.token.TLine;
import com.raygroupintl.m.token.TRoutine;
import com.raygroupintl.parser.annotation.ParseException;
import com.raygroupintl.vista.repository.FileSupply;
import com.raygroupintl.vista.tools.ErrorExemptions;

import junit.framework.Assert;

public class VistAFOIATest {
	@Test
	public void testAll() {
		try {
			MTFSupply m = MTFSupply.getInstance(MVersion.CACHE);
			final TFRoutine tf = TFRoutine.getInstance(m);
			final ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
			//String rrr = null;
			//int index = 0;

			List<Path> paths = FileSupply.getAllMFiles();
			for (Path path : paths) {
				//String n = path.getFileName().toString().split(".m")[0];
				//if (! n.equals("PRCAATR")) continue;
				//byte[] b = Files.readAllBytes(path);
				//String text = new String(b);
				//rrr = path.getFileName().toString();
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
					//System.out.print("   " + String.valueOf(i) + '\n');
					//System.out.print(line + '\n');
					TLine result = results.get(i);
					String readLine = result.getStringValue();
					String msg = path.getFileName().toString() + " Line " +  String.valueOf(i);
					Assert.assertEquals("Different: " + msg, line, readLine);
				}
				String name = r.getName();
				ErrorRecorder ev = new ErrorRecorder();
				if (! exemptions.containsRoutine(name)) {
					Set<LineLocation> locations = exemptions.getLines(name);
					List<ObjectInRoutine<MError>> errors = ev.visitErrors(r.getNode(), locations);
					Assert.assertEquals(errors.size(), 0);						
				}	
				//++index;
			}
		} catch (ParseException e) {
			fail("Exception: " + e.getMessage());			
		} catch (IOException e) {
			fail("Exception: " + e.getMessage());			
		}
	}
}
