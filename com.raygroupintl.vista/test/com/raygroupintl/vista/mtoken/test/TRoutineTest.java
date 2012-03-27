package com.raygroupintl.vista.mtoken.test;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.mtoken.TFLine;
import com.raygroupintl.vista.mtoken.TLine;
import com.raygroupintl.vista.mtoken.TRoutine;

public class TRoutineTest {

	@Test
	public void testBeautify() {
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
			TFLine f = TFLine.getInstance();
			TLine l = (TLine) f.tokenize(line, 0);
			r.add(l);
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
		for (IToken line : r.asList()) {
			String expected = result[index];
			String actual = line.getStringValue();
			Assert.assertEquals(expected, actual);
			++index;
		}		
	}
	
	@Test
	public void testNonErrorFiles() {
		String fileName = "resource/XRGITST0.m";
		InputStream is = this.getClass().getResourceAsStream(fileName);
		TRoutine r = TRoutine.getInstance("XRGITST0.m", is);
		Assert.assertFalse("Unexpected error: " + fileName, r.hasError() || r.hasFatalError());	
	}	
}
