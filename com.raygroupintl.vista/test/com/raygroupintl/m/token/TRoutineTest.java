package com.raygroupintl.m.token;

import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.m.parsetree.Routine;
import com.raygroupintl.m.parsetree.visitor.ErrorVisitor;
import com.raygroupintl.m.struct.LineLocation;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.token.TLine;
import com.raygroupintl.m.token.TRoutine;
import com.raygroupintl.vista.struct.MLocationedError;
import com.raygroupintl.vista.struct.MRoutineContent;

public class TRoutineTest {
	private static TokenFactory fStd95;
	private static TokenFactory fCache;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MTFSupply tfsStd95 = MTFSupply.getInstance(MVersion.ANSI_STD_95);
		fStd95 = tfsStd95.line;
		
		MTFSupply tfsCache = MTFSupply.getInstance(MVersion.CACHE);
		fCache = tfsCache.line;
	}

	private TRoutine getRoutineToken(String fileName, MVersion version) {
		TFRoutine tf = TFRoutine.getInstance(version);
		InputStream is = this.getClass().getResourceAsStream(fileName);
		MRoutineContent content = MRoutineContent.getInstance(fileName.split(".m")[0], is);
		TRoutine r = tf.tokenize(content);
		return r;
	}
	
	public void testBeautify(TokenFactory f) {
		String[] lines = new String[]{
				"IBDF14 ;ALB/CJM - AICS LIST CLINIC SETUP ; JUL 20,1993",
				" ;;3.0;AUTOMATED INFO COLLECTION SYS;;APR 24, 1997",
				" ;",
				"SETUPS ; -- Lists forms/reports defined in print manager clinic setup",
				" ;",
				"% N CLINIC,SETUP,NODE,COND,INTRFACE,PAGE,IBQUIT,IBHDT,X,Y,FORM,REPORT,NAME,VAUTD,DIVIS,NEWDIV,CNT,MULTI",
				" W !!,\"AICS Print Manager Clinic Setup Report\",!!",
				" S IBQUIT=0",
				" D DIVIS G:IBQUIT EXIT",
				" D DEVICE G:IBQUIT EXIT",
				" D DQ",
				" G EXIT",
				" Q",
				" ;"};
		TRoutine r = new TRoutine("XRGITST0.m");
		for (int i=0; i<lines.length; ++i) {
			String line = lines[i];
			try {
				TLine l = (TLine) f.tokenize(line, 0);
				r.add(l);
			} catch(SyntaxErrorException e) {
				fail("Exception: " + e.getMessage());
			}
		}
		r.beautify();		
		String[] result = new String[]{
				"IBDF14 ;ALB/CJM - AICS LIST CLINIC SETUP ; JUL 20,1993",
				" ;;3.0;AUTOMATED INFO COLLECTION SYS;;APR 24, 1997",
				" ;",
				"SETUPS ; -- Lists forms/reports defined in print manager clinic setup",
				" ;",
				"% NEW CLINIC,SETUP,NODE,COND,INTRFACE,PAGE,IBQUIT,IBHDT,X,Y,FORM,REPORT,NAME,VAUTD,DIVIS,NEWDIV,CNT,MULTI",
				" WRITE !!,\"AICS Print Manager Clinic Setup Report\",!!",
				" SET IBQUIT=0",
				" DO DIVIS GOTO:IBQUIT EXIT",
				" DO DEVICE GOTO:IBQUIT EXIT",
				" DO DQ",
				" GOTO EXIT",
				" QUIT",
				" ;"};
		int index = 0;
		for (Token line : r.asList()) {
			String expected = result[index];
			String actual = line.getStringValue();
			Assert.assertEquals(expected, actual);
			++index;
		}		
	}
	
	@Test
	public void testBeautify() {
		testBeautify(fCache);
		testBeautify(fStd95);
	}
		
	public void testNonErrorFiles(MVersion version) {
		String fileName = "resource/XRGITST0.m";
		InputStream is = this.getClass().getResourceAsStream(fileName);
		TFRoutine tf = TFRoutine.getInstance(version);
		MRoutineContent content = MRoutineContent.getInstance(fileName.split(".m")[0], is);
		TRoutine r = tf.tokenize(content);
		Assert.assertFalse("Unexpected error: " + fileName, r.hasError() || r.hasFatalError());	
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
		TRoutine token = this.getRoutineToken(fileName, version);
		Routine r = token.getNode();
		ErrorVisitor v = new ErrorVisitor();
		List<MLocationedError> result = v.visitErrors(r);
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
