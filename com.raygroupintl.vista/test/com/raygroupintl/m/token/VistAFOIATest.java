package com.raygroupintl.m.token;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.Test;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.ErrorRecorder;
import com.raygroupintl.m.struct.MError;
import com.raygroupintl.m.struct.ObjectInRoutine;
import com.raygroupintl.m.struct.MRoutineContent;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.token.MLine;
import com.raygroupintl.m.token.MRoutine;
import com.raygroupintl.parsergen.ParseException;
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
				MRoutine r = tf.tokenize(content);
				List<String> lines = content.getLines();
				List<MLine> results = r.asList();
				int count = results.size();
				Assert.assertEquals(lines.size(), count);
				for (int i=0; i<count; ++i) {
					String line = lines.get(i);
					MLine result = results.get(i);
					String readLine = result.toValue().toString();
					String msg = path.getFileName().toString() + " Line " +  String.valueOf(i);
					Assert.assertEquals("Different: " + msg, line, readLine);
				}
				ErrorRecorder ev = new ErrorRecorder(exemptions);
				ev.setOnlyFatal(true);
				Routine routine = r.getNode();
				List<ObjectInRoutine<MError>> errors = ev.visitErrors(routine);
				Assert.assertEquals(errors.size(), 0);						
			}			
		} catch (ParseException e) {
			fail("Exception: " + e.getMessage());			
		} catch (IOException e) {
			fail("Exception: " + e.getMessage());			
		}
	}
}
