package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.MVersion;
import com.raygroupintl.vista.mtoken.TFCommand;

public class TCommandSetTest {
	private void test(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "S X=$$MG^XMBGRP(\"RCCPC STATEMENTS\",0,.5,1,\"\",.DES,1)");
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
		TFCommonTest.validCheck(f, "S XT4=\"I 1\"   ");
		TFCommonTest.validCheck(f, "S IOP=IOP_\";255\",%ZIS=\"\"");
		TFCommonTest.validCheck(f, "S X=$I(^HLCS(870,DP,P),$S($G(Z):-1,1:1))");
		TFCommonTest.validCheck(f, "S ^$W(\"ZISGTRM\",\"VISIBLE\")=1");
	}

	@Test
	public void test() {
		test(MVersion.CACHE);
		test(MVersion.ANSI_STD_95);
	}
}
