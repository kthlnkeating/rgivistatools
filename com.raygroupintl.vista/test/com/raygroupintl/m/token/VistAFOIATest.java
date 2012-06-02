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
			final TFRoutine tf = new TFRoutine(m);
			final ErrorExemptions exemptions = ErrorExemptions.getVistAFOIAInstance();
			List<Path> paths = FileSupply.getAllMFiles();
			for (Path path : paths) {
				MRoutineContent content = MRoutineContent.getInstance(path); 
				TRoutine r = tf.tokenize(content);
				List<String> lines = content.getLines();
				List<TLine> results = r.asList();
				int count = results.size();
				Assert.assertEquals(lines.size(), count);
				for (int i=0; i<count; ++i) {
					String line = lines.get(i);
					TLine result = results.get(i);
					String readLine = result.toValue().toString();
					String msg = path.getFileName().toString() + " Line " +  String.valueOf(i);
					Assert.assertEquals("Different: " + msg, line, readLine);
				}
				String name = r.getName();
				if (! exemptions.containsRoutine(name)) {
					Set<LineLocation> locations = exemptions.getLines(name);
					ErrorRecorder ev = new ErrorRecorder(locations);
					List<ObjectInRoutine<MError>> errors = ev.visitErrors(r.getNode());
					Assert.assertEquals(errors.size(), 0);						
				}	
			}
		} catch (ParseException e) {
			fail("Exception: " + e.getMessage());			
		} catch (IOException e) {
			fail("Exception: " + e.getMessage());			
		}
	}
}
