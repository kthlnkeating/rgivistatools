package com.raygroupintl.m.token.test;

import static org.junit.Assert.fail;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.bnf.SyntaxErrorException;
import com.raygroupintl.bnf.Token;
import com.raygroupintl.bnf.TokenFactory;
import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFRoutine;
import com.raygroupintl.m.token.TLine;
import com.raygroupintl.m.token.TRoutine;
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
		try {
			TRoutine r = tf.tokenize(content);
			Assert.assertFalse("Unexpected error: " + fileName, r.hasError() || r.hasFatalError());	
		} catch(SyntaxErrorException e) {
			fail("Exception: " + e.getMessage());
		}
	}

	@Test
	public void testNonErrorFiles() {
		testNonErrorFiles(MVersion.CACHE);
		testNonErrorFiles(MVersion.ANSI_STD_95);
	}
}
