package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TFCommand;

public class TCommandSetTest {
	@Test
	public void test() {
		ITokenFactory f = new TFCommand();
		//TFCommonTest.validCheck(f, "S X=$$MG^XMBGRP(\"RCCPC STATEMENTS\",0,.5,1,\"\",.DES,1)");
		TFCommonTest.validCheck(f, "S @^%ZOSF(\"TRAP\")");
		TFCommonTest.validCheck(f, "S X=\"ERROR^PRCAHV\",@^%ZOSF(\"TRAP\")");
		TFCommonTest.validCheck(f, "S A=B");
		TFCommonTest.validCheck(f, "S A=B,@C=D");
		TFCommonTest.validCheck(f, "S @A,$E(V,\",\",2)=\"DE\"");
		TFCommonTest.validCheck(f, "S @A=@C");
		TFCommonTest.validCheck(f, "S $X=5,$Y=3,(B,C,D)=(A=B)");
		TFCommonTest.validCheck(f, "S A=B,C=F,D=YSH");
		TFCommonTest.validCheck(f, "S @A=\"S\"");
		TFCommonTest.validCheck(f, "S @H@(0)=3");
	}
}
