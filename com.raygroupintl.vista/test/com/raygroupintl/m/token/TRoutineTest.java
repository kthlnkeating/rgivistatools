package com.raygroupintl.m.token;

import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.ErrorVisitor;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.token.TLine;
import com.raygroupintl.m.token.TRoutine;
import com.raygroupintl.vista.struct.MLocationedError;
import com.raygroupintl.vista.struct.MRoutineContent;

public class TRoutineTest {
	private TRoutine getRoutineToken(String fileName, MVersion version) {
		TFRoutine tf = TFRoutine.getInstance(version);
		InputStream is = this.getClass().getResourceAsStream(fileName);
		MRoutineContent content = MRoutineContent.getInstance(fileName.split(".m")[0], is);
		TRoutine r = tf.tokenize(content);
		return r;
	}
	
	private List<MLocationedError> getErrors(String fileName, MVersion version) {
		TRoutine token = this.getRoutineToken(fileName, version);
		Routine r = token.getNode();
		ErrorVisitor v = new ErrorVisitor();
		List<MLocationedError> result = v.visitErrors(r);
		return result;
	}
	
	public void testBeautify(MVersion version) {
		TRoutine original = this.getRoutineToken("resource/BEAT0SRC.m", version);
		TRoutine source = this.getRoutineToken("resource/BEAT0SRC.m", version);
		TRoutine result = this.getRoutineToken("resource/BEAT0RST.m", version);
		
		source.beautify();
		List<TLine> originalLines = original.asList();
		List<TLine> sourceLines = source.asList();
		List<TLine> resultLines = result.asList();
		int n = resultLines.size();
		Assert.assertEquals( originalLines.size(), resultLines.size());
		Assert.assertEquals(sourceLines.size(), resultLines.size());
		for (int i=1; i<n; ++i) {
			String sourceLineValue = sourceLines.get(i).getStringValue();
			String resultLineValue = resultLines.get(i).getStringValue();
			Assert.assertEquals(sourceLineValue, resultLineValue);
			if ((i > 4) && (i < 12)) {
				Assert.assertFalse(sourceLineValue.equals(originalLines.get(i).getStringValue()));				
			}
		}
	}
	
	@Test
	public void testBeautify() {
		testBeautify(MVersion.CACHE);
		testBeautify(MVersion.ANSI_STD_95);
	}
		
	public void testNonErrorFiles(MVersion version) {
		String fileName = "resource/XRGITST0.m";
		List<MLocationedError> result = this.getErrors(fileName, version);
		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testNonErrorFiles() {
		testNonErrorFiles(MVersion.CACHE);
		testNonErrorFiles(MVersion.ANSI_STD_95);
	}
	
	private void testErrTest0Error(MLocationedError error, String expectedTag, int expectedOffset) {
		LineLocation location = error.getLocation();
		Assert.assertEquals(expectedTag, location.getTag());
		Assert.assertEquals(expectedOffset, location.getOffset());		
	}
	
	private void testErrTest0(MVersion version) {
		String fileName = "resource/ERRTEST0.m";
		List<MLocationedError> result = this.getErrors(fileName, version);
		Assert.assertEquals(3, result.size());
		testErrTest0Error(result.get(0), "MULTIPLY", 2);
		testErrTest0Error(result.get(1), "MAIN", 3);
		testErrTest0Error(result.get(2), "MAIN", 5);
	}

	@Test
	public void testErrTest0() {
		testErrTest0(MVersion.CACHE);
		testErrTest0(MVersion.ANSI_STD_95);		
	}	
}
