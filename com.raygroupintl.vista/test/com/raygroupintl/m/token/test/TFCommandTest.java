package com.raygroupintl.m.token.test;

import org.junit.Test;

import com.raygroupintl.fnds.ITokenFactory;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFCommand;

public class TFCommandTest {
	private void testBreak(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "B");
		TFCommonTest.validCheck(f, "B   ");
		TFCommonTest.validCheck(f, "B \"+13^TAG\"");
		TFCommonTest.validCheck(f, "B \"+13^TAG\""     );
	}

	@Test
	public void testBreak() {
		testBreak(MVersion.CACHE);
		testBreak(MVersion.ANSI_STD_95);
	}

	private void testDo(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "D SENDMSG^XMXAPI(.5,RCSUBJ,XMBODY,.XMTO,,.XMZ)");
		TFCommonTest.validCheck(f, "D SET^IBCSC5A(BILLDA,.ARRXS,)");
		TFCommonTest.validCheck(f, "D ^%ZIS");
		TFCommonTest.validCheck(f, "D WRITE(IO,G)");
		TFCommonTest.validCheck(f, "D WRAPPER(@(\"PSBTAB\"_(FLD-1))+1,((@(\"PSBTAB\"_(FLD))-(@(\"PSBTAB\"_(FLD-1))+1))),PSBVAL)");
		if (version == MVersion.CACHE) {
			TFCommonTest.validCheck(f, "DO $system.Status.DecomposeStatus(%objlasterror,.XOBLERR)");
		}
	}

	@Test
	public void testDo() {
		testDo(MVersion.CACHE);
		testDo(MVersion.ANSI_STD_95);
	}

	private void testFor(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "F FLD=1:1:$L(LST,\",\")");
		TFCommonTest.validCheck(f, "F STAT=42,16");
	}

	@Test
	public void testFor() {
		testFor(MVersion.CACHE);
		testFor(MVersion.ANSI_STD_95);
	}

	private void testGoto(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "G:POP H^XUS");
		TFCommonTest.validCheck(f, "G:POP H^[ENV]XUS:POP");
		TFCommonTest.validCheck(f, "G:POP H+3^[ENV]XUS:POP");
		TFCommonTest.validCheck(f, "G H+3");
		TFCommonTest.validCheck(f, "G ^XUS:POP");
		TFCommonTest.validCheck(f, "G H+3,^XUS:POP");
		TFCommonTest.validCheck(f, "G ^XUS");
		TFCommonTest.validCheck(f, "G @A");
		TFCommonTest.validCheck(f, "G @A^@B");
		TFCommonTest.validCheck(f, "G @A:P");
		TFCommonTest.validCheck(f, "G ^@B");
		TFCommonTest.validCheck(f, "G ^@B:P");
		TFCommonTest.errorCheck(f, "G ^");
	}

	@Test
	public void testGoto() {
		testGoto(MVersion.CACHE);
		testGoto(MVersion.ANSI_STD_95);
	}

	private void testHaltHang(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "H 3");
		TFCommonTest.validCheck(f, "H");
	}

	@Test
	public void testHaltHang() {
		testHaltHang(MVersion.CACHE);
		testHaltHang(MVersion.ANSI_STD_95);
	}

	private void testIf(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "I $L($T(NTRTMSG^HDISVAP))");
		TFCommonTest.validCheck(f, "I @CLIN@(0)=0");
		TFCommonTest.validCheck(f, "I @CLIN@(0)");
		TFCommonTest.validCheck(f, "I $P(LA7XFORM,\"^\")?1.N,LA7VAL?1(1N.E,1\".\".E)");
		TFCommonTest.validCheck(f, "I $D(@G)#10");
	}
	
	@Test
	public void testIf() {
		testIf(MVersion.CACHE);
		testIf(MVersion.ANSI_STD_95);
	}

	private void testJob(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "JOB CHILDNT^XOBVTCPL():(:4:XOBIO:XOBIO):10");
		TFCommonTest.validCheck(f, "J LISTENER^XOBVTCPL(XOBPORT,$GET(XOBCFG))::5");
		TFCommonTest.validCheck(f, "JOB CHILDNT^XOBVTCPL(A,.B):(:4:XOBIO:XOBIO):10");
		TFCommonTest.validCheck(f, "JOB CHILDNT+3^XOBVTCPL:(:4:XOBIO:XOBIO):10");
		TFCommonTest.validCheck(f, "JOB CHILDNT+3^XOBVTCPL:5");
		TFCommonTest.validCheck(f, "JOB CHILDNT:5");
		TFCommonTest.validCheck(f, "JOB CHILDNT:(A:B:C):5");
		TFCommonTest.validCheck(f, "JOB CHILDNT:(::B:C):5");
		TFCommonTest.validCheck(f, "JOB @A^@A:(::B:C):5");
		TFCommonTest.validCheck(f, "J ^XMRONT");
		TFCommonTest.validCheck(f, "J ^XMRONT::5");
	}
	
	@Test
	public void testJob() {
		testJob(MVersion.CACHE);
		testJob(MVersion.ANSI_STD_95);
	}

	private void testKill(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "K A");
		TFCommonTest.validCheck(f, "K A,B,@C,D");
		TFCommonTest.validCheck(f, "K @A");
		TFCommonTest.validCheck(f, "K @A,@C");
		TFCommonTest.validCheck(f, "K (A,B),D,(R,E)");
		TFCommonTest.validCheck(f, "K A,B");
		TFCommonTest.validCheck(f, "K CC,DD,EE");
		TFCommonTest.validCheck(f, "K");
		TFCommonTest.validCheck(f, "K ^XY");
		TFCommonTest.validCheck(f, "K ^XY,^Z(\"D\")");
		TFCommonTest.validCheck(f, "K:A=1 ^XY,^Z(\"D\")");
		TFCommonTest.validCheck(f, "K:A=1 ^XY,D");
		TFCommonTest.validCheck(f, "K ^XY,D,ZZ(\"A\",\"FF\"),TRE");
		TFCommonTest.validCheck(f, "K (A)");
		TFCommonTest.validCheck(f, "K (A,B)");
		TFCommonTest.validCheck(f, "K %ZIS");
		TFCommonTest.errorCheck(f, "K (^A)");
		TFCommonTest.errorCheck(f, "K (A,^A)");
		TFCommonTest.errorCheck(f, "K (A(25))");
		TFCommonTest.errorCheck(f, "K (B,A(3,2))");
		TFCommonTest.errorCheck(f, "K ()");
		TFCommonTest.errorCheck(f, "K (,A)");
		TFCommonTest.errorCheck(f, "K (D,,Y)");
		TFCommonTest.errorCheck(f, "K CC,DD,EE,");
		TFCommonTest.errorCheck(f, "K CC,,EE");
	}

	@Test
	public void testKill() {
		testKill(MVersion.CACHE);
		testKill(MVersion.ANSI_STD_95);
	}

	private void testLock(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "L -^PRCA(430,+$G(PRCABN),0)");
		TFCommonTest.validCheck(f, "L +^PRCA(430,DA,0):0");		
		TFCommonTest.validCheck(f, "L -^PRCA(430,+$G(PRCABN),0),+^PRCA:0");
		TFCommonTest.validCheck(f, "L +(^LR(LRDFN,\"MI\",LRIDT)):0");
		TFCommonTest.validCheck(f, "L +(^LRO(68,LRAA,1,LRAD,1,LRAN))");
		TFCommonTest.validCheck(f, "L +(^LR(LRDFN,\"MI\",LRIDT),^LRO(68,LRAA,1,LRAD,1,LRAN)):0");
		TFCommonTest.validCheck(f, "L -(^LR(LRDFN,\"MI\",LRIDT),^LRO(68,LRAA,1,LRAD,1,LRAN))");
		TFCommonTest.validCheck(f, "L +PSX(550.1):3");	
	}

	@Test
	public void testLock() {
		testLock(MVersion.CACHE);
		testLock(MVersion.ANSI_STD_95);
	}

	public void testRead(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "R !,\"Select DEBTOR NAME or BILL NUMBER: \",X:DTIME");
		TFCommonTest.validCheck(f, "R !,\"ANSWER= \",@YSR1:300");
	}

	@Test
	public void testRead() {
		testRead(MVersion.CACHE);
		testRead(MVersion.ANSI_STD_95);
	}

	private void testQuit(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "Q @SCLIST@(0)>0");
	}

	@Test
	public void testQuit() {
		testQuit(MVersion.CACHE);
		testQuit(MVersion.ANSI_STD_95);
	}

	private void testSet(MVersion version) {
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
	public void testSet() {
		testSet(MVersion.CACHE);
		testSet(MVersion.ANSI_STD_95);
	}

	private void testOpen(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "O:$G(LOGICAL)]\"\" HLCSTATE(\"DEVICE\"):(TCPDEV:BLOCKSIZE=512):HLCSTATE(\"OPEN TIMEOUT\")");
		TFCommonTest.validCheck(f, "OPEN XOBIO:(:XOBPORT:\"AT\"):30");
	}	
	
	@Test
	public void testOpen() {
		testOpen(MVersion.CACHE);
		testOpen(MVersion.ANSI_STD_95);
	}

	private void testUse(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "U IO");
		TFCommonTest.validCheck(f, "U A:B");
		TFCommonTest.validCheck(f, "U $I:(64)");
		TFCommonTest.validCheck(f, "U $I:(0::::64)");
		TFCommonTest.validCheck(f, "U $I:(VT=1:ESCAPE=1)");
		TFCommonTest.validCheck(f, "U $I:(:\"CT\")");
		TFCommonTest.validCheck(f, "U $I:(ESCAPE)");
		TFCommonTest.validCheck(f, "U 56::\"TCP\"");
	}

	@Test
	public void testUse() {
		testUse(MVersion.CACHE);
		testUse(MVersion.ANSI_STD_95);
	}

	private void testView(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "V -1:1");
	}
	
	@Test
	public void testView() {
		testView(MVersion.CACHE);
		testView(MVersion.ANSI_STD_95);
	}

	private void testWrite(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "W !!,^YTT(601,YSTEST,\"G\",L,1,1,0)");
		TFCommonTest.validCheck(f, "W !,$S($D(ZTSK):\"REQUEST QUEUED TASK=\"_ZTSK,1:\"REQUEST CANCELLED\")");
		TFCommonTest.validCheck(f, "W:$O(^DVB(396.4,OLDA,\"RES\",LINE))]\"\"&('+$G(DVBGUI)) !!,\"Exam Results Continued\",!!");
		TFCommonTest.validCheck(f, "W /LISTEN(1)");
	}
	
	@Test
	public void testWrite() {
		testWrite(MVersion.CACHE);
		testWrite(MVersion.ANSI_STD_95);
	}

	private void testXecute(MVersion version) {
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "X ^%ZOSF(\"TYPE-AHEAD\"),^%ZOSF(\"LABOFF\")");
	}
	
	@Test
	public void testXecute() {
		testXecute(MVersion.CACHE);
		testXecute(MVersion.ANSI_STD_95);
	}

	private void testZ(MVersion version) {
		TFCommand.addCommand("ZB");
		ITokenFactory f = TFCommand.getInstance(version);
		TFCommonTest.validCheck(f, "ZB ZB0^HLOQUE:\"N\":1:\"S RET=0\"");
	}

	@Test
	public void testZ() {
		testZ(MVersion.CACHE);
		testZ(MVersion.ANSI_STD_95);
	}
}

