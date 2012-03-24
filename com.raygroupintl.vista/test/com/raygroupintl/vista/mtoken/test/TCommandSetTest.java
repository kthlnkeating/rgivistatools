package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.IToken;
import com.raygroupintl.vista.mtoken.command.TCommandSet;

public class TCommandSetTest {
	private void testCommon(String v) {
		TCommandSet o = new TCommandSet("S");
		IToken t = o.getArgument(v, 0);
		TFCommonTest.validCheck(t, v);		
	}
	
	@Test
	public void test() {
		this.testCommon("A=B");
		this.testCommon("A=B,@C=D");
		this.testCommon("@A,$E(V,\",\",2)=\"DE\"");
		this.testCommon("@A=@C");
		this.testCommon("$X=5,$Y=3,(B,C,D)=(A=B)");
		this.testCommon("A=B,C=F,D=YSH");
		this.testCommon("@A=\"S\"");
		this.testCommon("@H@(0)=3");
	}
}
