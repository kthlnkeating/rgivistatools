package com.raygroupintl.vista.mtoken.test;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.vista.mtoken.Line;
import com.raygroupintl.vista.struct.MError;


public class LineTest {

	private Line lineTest(String line, boolean errorAsWell) {
		Line t = Line.getInstance(line);
		String r = t.getStringValue();
		Assert.assertEquals(line, r);	
		if (errorAsWell) {
			List<MError> error = t.getErrors();
			int numErrors = error == null ? 0 : error.size();
			Assert.assertEquals(0, numErrors);	
		}
		return t;
	}
	
	private Line lineTest(String line) {
		return lineTest(line, true);
	}
	
	@Test
	public void testBasic() {
		lineTest(" S A=A+1  F  S B=$O(^F(B)) Q:B=\"\"   S ^F(B,A)=5");
		lineTest(" S $E(A)=J+1 Q:B=\"\"\"YYY\"\"\"  Q:B=\"\"\"XXX\"\"\"");
		lineTest(" I '$D(USERPRT),(STATUS'=\"c\") Q ;not individual & not complete");
		lineTest("PRCA219P ;ALB/RRG - REPORT LIKELY BILLS TO PRINT;;");
		lineTest(" I $$DEVICE() D ENTER");
		lineTest("DEVICE() ;");
		lineTest(" ESTART", false);
	}

	@Test
	public void testBeautify() {
		Line l = lineTest(" S @A=\"S\"  S @H@(0)=3");
		l.beautify();
		String expected = " SET @A=\"S\"  SET @H@(0)=3";
		String actual = l.getStringValue();
		Assert.assertEquals(expected, actual);
	}
}
