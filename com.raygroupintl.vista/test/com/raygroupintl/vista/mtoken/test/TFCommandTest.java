package com.raygroupintl.vista.mtoken.test;

import org.junit.Test;

import com.raygroupintl.vista.fnds.ITokenFactory;
import com.raygroupintl.vista.mtoken.TFCommand;

public class TFCommandTest {
	@Test
	public void testDo() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "D SENDMSG^XMXAPI(.5,RCSUBJ,XMBODY,.XMTO,,.XMZ)");
		TFCommonTest.validCheck(f, "D SET^IBCSC5A(BILLDA,.ARRXS,)");
		TFCommonTest.validCheck(f, "D ^%ZIS");
		TFCommonTest.validCheck(f, "D WRITE(IO,G)");
		TFCommonTest.validCheck(f, "D WRAPPER(@(\"PSBTAB\"_(FLD-1))+1,((@(\"PSBTAB\"_(FLD))-(@(\"PSBTAB\"_(FLD-1))+1))),PSBVAL)");		
	}

	@Test
	public void testFor() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "F FLD=1:1:$L(LST,\",\")");
		TFCommonTest.validCheck(f, "F STAT=42,16");
	}

	@Test
	public void testGoto() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "G:POP H^XUS");
	}

	@Test
	public void testIf() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "I $L($T(NTRTMSG^HDISVAP))");
		TFCommonTest.validCheck(f, "I @CLIN@(0)=0");
		TFCommonTest.validCheck(f, "I @CLIN@(0)");
		TFCommonTest.validCheck(f, "I $P(LA7XFORM,\"^\")?1.N,LA7VAL?1(1N.E,1\".\".E)");
		TFCommonTest.validCheck(f, "I $D(@G)#10");
	}
	
	@Test
	public void testLock() {
		ITokenFactory f = new TFCommand();
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
	public void testOpen() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "O:$G(LOGICAL)]\"\" HLCSTATE(\"DEVICE\"):(TCPDEV:BLOCKSIZE=512):HLCSTATE(\"OPEN TIMEOUT\")");
	}	
	
	@Test
	public void testWrite() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "W !!,^YTT(601,YSTEST,\"G\",L,1,1,0)");
		TFCommonTest.validCheck(f, "W !,$S($D(ZTSK):\"REQUEST QUEUED TASK=\"_ZTSK,1:\"REQUEST CANCELLED\")");
		TFCommonTest.validCheck(f, "W:$O(^DVB(396.4,OLDA,\"RES\",LINE))]\"\"&('+$G(DVBGUI)) !!,\"Exam Results Continued\",!!");
	}
	
	@Test
	public void testUse() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "U IO");
		TFCommonTest.validCheck(f, "U $I:(0::::64)");
		TFCommonTest.validCheck(f, "U $I:(VT=1:ESCAPE=1)");
		TFCommonTest.validCheck(f, "U $I:(:\"CT\")");
		TFCommonTest.validCheck(f, "U $I:(ESCAPE)");
	}

	@Test
	public void testXecute() {
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "X ^%ZOSF(\"TYPE-AHEAD\"),^%ZOSF(\"LABOFF\")");
	}
	
	@Test
	public void testZ() {
		TFCommand.addCommand("ZB");
		ITokenFactory f = new TFCommand();
		TFCommonTest.validCheck(f, "ZB ZB0^HLOQUE:\"N\":1:\"S RET=0\"");
	}
}

