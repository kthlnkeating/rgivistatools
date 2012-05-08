package com.raygroupintl.m.token.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.m.token.MTFSupply;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFCommand;

public class TFCommandTest {	
	private static TFCommand fStd95;
	private static TFCommand fCache;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MTFSupply tfsStd95 = MTFSupply.getInstance(MVersion.ANSI_STD_95);
		fStd95 = tfsStd95.command;
		
		MTFSupply tfsCache = MTFSupply.getInstance(MVersion.CACHE);
		fCache =  tfsCache.command;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		fStd95 = null;
		fCache = null;
	}
		
	private void testBreak(TFCommand f) {
		TFCommonTest.validCheckNS(f, "B");
		TFCommonTest.validCheckNS(f, "B   ");
		TFCommonTest.validCheckNS(f, "B \"+13^TAG\"");
		TFCommonTest.validCheckNS(f, "B \"+13^TAG\""     );
	}

	@Test
	public void testBreak() {
		testBreak(fCache);
		testBreak(fStd95);
	}

	private void testDo(TFCommand f) {
		TFCommonTest.validCheckNS(f, "D SENDMSG^XMXAPI(.5,RCSUBJ,XMBODY,.XMTO,,.XMZ)");
		TFCommonTest.validCheckNS(f, "D SET^IBCSC5A(BILLDA,.ARRXS,)");
		TFCommonTest.validCheckNS(f, "D ^%ZIS");
		TFCommonTest.validCheckNS(f, "D WRITE(IO,G)");
		TFCommonTest.validCheckNS(f, "D WRAPPER(@(\"PSBTAB\"_(FLD-1))+1,((@(\"PSBTAB\"_(FLD))-(@(\"PSBTAB\"_(FLD-1))+1))),PSBVAL)");
	}

	@Test
	public void testDo() {
		testDo(fCache);
		testDo(fStd95);
		TFCommonTest.validCheckNS(fCache, "DO $system.Status.DecomposeStatus(%objlasterror,.XOBLERR)");
	}

	private void testFor(TFCommand f) {
		TFCommonTest.validCheckNS(f, "F FLD=1:1:$L(LST,\",\")");
		TFCommonTest.validCheckNS(f, "F STAT=42,16");
	}

	@Test
	public void testFor() {
		testFor(fCache);
		testFor(fStd95);
	}

	private void testGoto(TFCommand f) {
		TFCommonTest.validCheckNS(f, "G:POP H^XUS");
		TFCommonTest.validCheckNS(f, "G:POP H^[ENV]XUS:POP");
		TFCommonTest.validCheckNS(f, "G:POP H+3^[ENV]XUS:POP");
		TFCommonTest.validCheckNS(f, "G H+3");
		TFCommonTest.validCheckNS(f, "G ^XUS:POP");
		TFCommonTest.validCheckNS(f, "G H+3,^XUS:POP");
		TFCommonTest.validCheckNS(f, "G ^XUS");
		TFCommonTest.validCheckNS(f, "G @A");
		TFCommonTest.validCheckNS(f, "G @A^@B");
		TFCommonTest.validCheckNS(f, "G @A:P");
		TFCommonTest.validCheckNS(f, "G ^@B");
		TFCommonTest.validCheckNS(f, "G ^@B:P");
		TFCommonTest.errorCheck(f, "G ^");
		TFCommonTest.validCheckNS(f, "G 0^DIE17");
	}

	@Test
	public void testGoto() {
		testGoto(fCache);
		testGoto(fStd95);
	}

	private void testHaltHang(TFCommand f) {
		TFCommonTest.validCheckNS(f, "H 3");
		TFCommonTest.validCheckNS(f, "H");
	}

	@Test
	public void testHaltHang() {
		testHaltHang(fCache);
		testHaltHang(fStd95);
	}

	private void testIf(TFCommand f) {
		TFCommonTest.validCheckNS(f, "I $L($T(NTRTMSG^HDISVAP))");
		TFCommonTest.validCheckNS(f, "I @CLIN@(0)=0");
		TFCommonTest.validCheckNS(f, "I @CLIN@(0)");
		TFCommonTest.validCheckNS(f, "I $P(LA7XFORM,\"^\")?1.N,LA7VAL?1(1N.E,1\".\".E)");
		TFCommonTest.validCheckNS(f, "I $D(@G)#10");
	}
	
	@Test
	public void testIf() {
		testIf(fCache);
		testIf(fStd95);
	}

	private void testJob(TFCommand f) {
		TFCommonTest.validCheckNS(f, "JOB CHILDNT^XOBVTCPL():(:4:XOBIO:XOBIO):10");
		TFCommonTest.validCheckNS(f, "J LISTENER^XOBVTCPL(XOBPORT,$GET(XOBCFG))::5");
		TFCommonTest.validCheckNS(f, "JOB CHILDNT^XOBVTCPL(A,.B):(:4:XOBIO:XOBIO):10");
		TFCommonTest.validCheckNS(f, "JOB CHILDNT+3^XOBVTCPL:(:4:XOBIO:XOBIO):10");
		TFCommonTest.validCheckNS(f, "JOB CHILDNT+3^XOBVTCPL:5");
		TFCommonTest.validCheckNS(f, "JOB CHILDNT:5");
		TFCommonTest.validCheckNS(f, "JOB CHILDNT:(A:B:C):5");
		TFCommonTest.validCheckNS(f, "JOB CHILDNT:(::B:C):5");
		TFCommonTest.validCheckNS(f, "JOB @A^@A:(::B:C):5");
		TFCommonTest.validCheckNS(f, "J ^XMRONT");
		TFCommonTest.validCheckNS(f, "J ^XMRONT::5");
	}
	
	@Test
	public void testJob() {
		testJob(fCache);
		testJob(fStd95);
	}

	private void testKill(TFCommand f) {
		TFCommonTest.validCheckNS(f, "K A");
		TFCommonTest.validCheckNS(f, "K A,B,@C,D");
		TFCommonTest.validCheckNS(f, "K @A");
		TFCommonTest.validCheckNS(f, "K @A,@C");
		TFCommonTest.validCheckNS(f, "K (A,B),D,(R,E)");
		TFCommonTest.validCheckNS(f, "K A,B");
		TFCommonTest.validCheckNS(f, "K CC,DD,EE");
		TFCommonTest.validCheckNS(f, "K");
		TFCommonTest.validCheckNS(f, "K ^XY");
		TFCommonTest.validCheckNS(f, "K ^XY,^Z(\"D\")");
		TFCommonTest.validCheckNS(f, "K:A=1 ^XY,^Z(\"D\")");
		TFCommonTest.validCheckNS(f, "K:A=1 ^XY,D");
		TFCommonTest.validCheckNS(f, "K ^XY,D,ZZ(\"A\",\"FF\"),TRE");
		TFCommonTest.validCheckNS(f, "K (A)");
		TFCommonTest.validCheckNS(f, "K (A,B)");
		TFCommonTest.validCheckNS(f, "K %ZIS");
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
		testKill(fCache);
		testKill(fStd95);
	}

	private void testLock(TFCommand f) {
		TFCommonTest.validCheckNS(f, "L -^PRCA(430,+$G(PRCABN),0)");
		TFCommonTest.validCheckNS(f, "L +^PRCA(430,DA,0):0");		
		TFCommonTest.validCheckNS(f, "L -^PRCA(430,+$G(PRCABN),0),+^PRCA:0");
		TFCommonTest.validCheckNS(f, "L +(^LR(LRDFN,\"MI\",LRIDT)):0");
		TFCommonTest.validCheckNS(f, "L +(^LRO(68,LRAA,1,LRAD,1,LRAN))");
		TFCommonTest.validCheckNS(f, "L +(^LR(LRDFN,\"MI\",LRIDT),^LRO(68,LRAA,1,LRAD,1,LRAN)):0");
		TFCommonTest.validCheckNS(f, "L -(^LR(LRDFN,\"MI\",LRIDT),^LRO(68,LRAA,1,LRAD,1,LRAN))");
		TFCommonTest.validCheckNS(f, "L +PSX(550.1):3");	
	}

	@Test
	public void testLock() {
		testLock(fCache);
		testLock(fStd95);
	}

	public void testRead(TFCommand f) {
		TFCommonTest.validCheckNS(f, "R !,\"Select DEBTOR NAME or BILL NUMBER: \",X:DTIME");
		TFCommonTest.validCheckNS(f, "R !,\"ANSWER= \",@YSR1:300");
	}

	@Test
	public void testRead() {
		testRead(fCache);
		testRead(fStd95);
	}

	private void testQuit(TFCommand f) {
		TFCommonTest.validCheckNS(f, "Q @SCLIST@(0)>0");
	}

	@Test
	public void testQuit() {
		testQuit(fCache);
		testQuit(fStd95);
	}

	private void testSet(TFCommand f) {
		TFCommonTest.validCheckNS(f, "S A=B");
		TFCommonTest.validCheckNS(f, "S X=$$MG^XMBGRP(\"RCCPC STATEMENTS\",0,.5,1,\"\",.DES,1)");
		TFCommonTest.validCheckNS(f, "S @^%ZOSF(\"TRAP\")");
		TFCommonTest.validCheckNS(f, "S X=\"ERROR^PRCAHV\",@^%ZOSF(\"TRAP\")");
		TFCommonTest.validCheckNS(f, "S A=B,@C=D");
		TFCommonTest.validCheckNS(f, "S @A,$E(V,\",\",2)=\"DE\"");
		TFCommonTest.validCheckNS(f, "S @A=@C");
		TFCommonTest.validCheckNS(f, "S $X=5,$Y=3,(B,C,D)=(A=B)");
		TFCommonTest.validCheckNS(f, "S A=B,C=F,D=YSH");
		TFCommonTest.validCheckNS(f, "S @A=\"S\"");
		TFCommonTest.validCheckNS(f, "S @H@(0)=3");
		TFCommonTest.validCheckNS(f, "S XT4=\"I 1\"   ");
		TFCommonTest.validCheckNS(f, "S IOP=IOP_\";255\",%ZIS=\"\"");
		TFCommonTest.validCheckNS(f, "S X=$I(^HLCS(870,DP,P),$S($G(Z):-1,1:1))");
		TFCommonTest.validCheckNS(f, "S ^$W(\"ZISGTRM\",\"VISIBLE\")=1");
	}

	@Test
	public void testSet() {
		testSet(fCache);
		testSet(fStd95);
	}

	private void testOpen(TFCommand f) {
		TFCommonTest.validCheckNS(f, "O:$G(LOGICAL)]\"\" HLCSTATE(\"DEVICE\"):(TCPDEV:BLOCKSIZE=512):HLCSTATE(\"OPEN TIMEOUT\")");
		TFCommonTest.validCheckNS(f, "OPEN XOBIO:(:XOBPORT:\"AT\"):30");
	}	
	
	@Test
	public void testOpen() {
		testOpen(fCache);
		testOpen(fStd95);
	}

	private void testUse(TFCommand f) {
		TFCommonTest.validCheckNS(f, "U IO");
		TFCommonTest.validCheckNS(f, "U A:B");
		TFCommonTest.validCheckNS(f, "U $I:(64)");
		TFCommonTest.validCheckNS(f, "U $I:(0::::64)");
		TFCommonTest.validCheckNS(f, "U $I:(VT=1:ESCAPE=1)");
		TFCommonTest.validCheckNS(f, "U $I:(:\"CT\")");
		TFCommonTest.validCheckNS(f, "U $I:(ESCAPE)");
		TFCommonTest.validCheckNS(f, "U 56::\"TCP\"");
	}

	@Test
	public void testUse() {
		testUse(fCache);
		testUse(fStd95);
	}

	private void testView(TFCommand f) {
		TFCommonTest.validCheckNS(f, "V -1:1");
	}
	
	@Test
	public void testView() {
		testView(fCache);
		testView(fStd95);
	}

	private void testWrite(TFCommand f) {
		TFCommonTest.validCheckNS(f, "W !!,^YTT(601,YSTEST,\"G\",L,1,1,0)");
		TFCommonTest.validCheckNS(f, "W !,$S($D(ZTSK):\"REQUEST QUEUED TASK=\"_ZTSK,1:\"REQUEST CANCELLED\")");
		TFCommonTest.validCheckNS(f, "W:$O(^DVB(396.4,OLDA,\"RES\",LINE))]\"\"&('+$G(DVBGUI)) !!,\"Exam Results Continued\",!!");
		TFCommonTest.validCheckNS(f, "W /LISTEN(1)");
	}
	
	@Test
	public void testWrite() {
		testWrite(fCache);
		testWrite(fStd95);
	}

	private void testXecute(TFCommand f) {
		TFCommonTest.validCheckNS(f, "X ^%ZOSF(\"TYPE-AHEAD\"),^%ZOSF(\"LABOFF\")");
	}
	
	@Test
	public void testXecute() {
		testXecute(fCache);
		testXecute(fStd95);
	}

	private void testZ(TFCommand f) {
		TFCommonTest.validCheckNS(f, "ZB ZB0^HLOQUE:\"N\":1:\"S RET=0\"");
	}

	@Test
	public void testZ() {
		testZ(fCache);
		testZ(fStd95);
	}
}

