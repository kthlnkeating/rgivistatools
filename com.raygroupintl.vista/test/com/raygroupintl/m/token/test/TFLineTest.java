package com.raygroupintl.m.token.test;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.fnds.IToken;
import com.raygroupintl.m.token.MVersion;
import com.raygroupintl.m.token.TFLine;
import com.raygroupintl.vista.struct.MError;

public class TFLineTest {
	private IToken lineTest(MVersion version, String line, boolean errorAsWell) {
		TFLine f = TFLine.getInstance(version);
		IToken t = f.tokenize(line, 0);
		String r = t.getStringValue();
		Assert.assertEquals(line, r);	
		if (errorAsWell) {
			List<MError> error = t.getErrors();
			int numErrors = error == null ? 0 : error.size();
			Assert.assertEquals(0, numErrors);	
		}
		return t;
	}

	private void lineErrorTest(MVersion version, String line) {
		TFLine f = TFLine.getInstance(version);
		IToken t = f.tokenize(line, 0);
		String r = t.getStringValue();
		Assert.assertEquals(line, r);	
		List<MError> error = t.getErrors();
		int numErrors = error == null ? 0 : error.size();
		Assert.assertTrue(numErrors > 0);	
	}

	private void noErrorTest(MVersion version, String lineUnderTest) {
		TFLine f = TFLine.getInstance(version);
		IToken line = f.tokenize(lineUnderTest, 0);
		Assert.assertFalse("Unexpected error", line.hasError());
		Assert.assertFalse("Unexpected fatal error", line.hasFatalError());				
	}
	
	private IToken lineTest(MVersion version, String line) {
		return lineTest(version, line, true);
	}

	public void testBasic(MVersion version) {
		lineTest(version, " S A=A+1  F  S B=$O(^F(B)) Q:B=\"\"   S ^F(B,A)=5");
		lineTest(version, " S $E(A)=J+1 Q:B=\"\"\"YYY\"\"\"  Q:B=\"\"\"XXX\"\"\"");
		lineTest(version, " I '$D(USERPRT),(STATUS'=\"c\") Q ;not individual & not complete");
		lineTest(version, "PRCA219P ;ALB/RRG - REPORT LIKELY BILLS TO PRINT;;");
		lineTest(version, " I $$DEVICE() D ENTER");
		lineTest(version, "DEVICE() ;");
		lineTest(version, " SET @A=\"S\"  SET @H@(0)=3");
		lineTest(version, " I Y>0 S DEBT=$P($G(^PRCA(430,Y,0)),\"^\",9) I DEBT S PRCADB=$P($G(^RCD(340,DEBT,0)),\"^\"),^DISV(DUZ,\"^PRCA(430,\")=Y,$P(DEBT,\"^\",2)=$$NAM^RCFN01(DEBT) D COMP,EN1^PRCAATR(Y) G:$D(DTOUT) Q G ASK");
		lineTest(version, " S ^DISV(DUZ,\"^RCD(340,\")=+Y,PRCADB=$P(Y,\"^\",2),DEBT=+Y_\"^\"_$P(@(\"^\"_$P(PRCADB,\";\",2)_+PRCADB_\",0)\"),\"^\")", false);
		lineTest(version, " D DIVIS G:IBQUIT EXIT");
		lineTest(version, " D DQ");
		lineTest(version, " .F FLD=1:1:$L(LST,\",\") Q:$P(LST,\",\",FLD)']\"\"  D @(+$P(LST,\",\",FLD)) Q:$G(PSODIR(\"DFLG\"))!($G(PSODIR(\"QFLG\")))");
		lineTest(version, " ESTART", false);
		lineTest(version, " D ^%ZISC");
		lineTest(version, " U IO D @PRCARN D ^%ZISC K %ZIS Q");
		lineTest(version, " S K=7 F L=2:1:4 S K=K-1 D:IOST?1\"C-\".E WAIT^YSUTL:$Y+4>IOSL Q:YSLFT  W !!,^YTT(601,YSTEST,\"G\",L,1,1,0) D CK");	
		lineTest(version, " D ##class(%XML.TextReader).ParseStream(RESTOBJ.HttpResponse.Data,.AREADER)", false);
		lineTest(version, " S CT=CT+1,^TMP(\"RCXM_344.5\",$J,CT)=\"This message is sent to alert you to conditions regarding this \"_RCTYP_\".\",CT=CT+1,^TMP(\"RCXM_344.5\",$J,CT)=\" \"");
		lineTest(version, " S CT=CT+1,^TMP(\"RCXM_344.5\",$J,CT)=\"The following electronic \"_RCTYP_\" was received at your site.\",CT=CT+1,^TMP(\"RCXM_344.5\",$J,CT)=\"It was received on: \"_$$FMTE^XLFDT($$NOW^XLFDT(),2)_\" in mail msg # \"_RCXMG_\".\"");
		lineTest(version, " S Z=0 F  S Z=$O(@RCVAR@(Z)) Q:'Z  I $D(@RCVAR@(Z,\"*\")) S CT=CT+1,^TMP(\"RCXM_344.5\",$J,CT)=@RCVAR@(Z,\"\")");
		lineTest(version, " L -@TASKNODE@(\"T\",0)");
		lineTest(version, " . X \"S RC=\"_@RULENODE@(1)");
		lineTest(version, " Q $D(@HANDLE@(\"Pr\",\"Handle\",CHILD))");
		lineTest(version, " .S BREAK=0 F  Q:BREAK||READER.EOF||'READER.Read()  D", false);
		lineTest(version, " F STAT=42,16 F  S BILLN=$O(^PRCA(430,\"AC\",STAT,BILLN)) Q:'BILLN  I $$ACCK(BILLN) D");
		lineTest(version, " S PRCANODE=.11 S:$P(Y,\";\",2)=\"DIC(4,\" PRCANODE=1 S PRCANODE=\"^\"_$P(Y,\";\",2)_+$P(Y,\"^\",2)_\",\"_PRCANODE_\")\",PRCANODE=$G(@PRCANODE)");
		lineTest(version, " . D SENDMSG^XMXAPI(.5,RCSUBJ,XMBODY,.XMTO,,.XMZ)");
		lineTest(version, "CONT F XT1=1:1 S XT2=$T(ROU+XT1) Q:XT2=\"\"  S X=$P(XT2,\" \",1),XT3=$P(XT2,\";\",3) X XT4 I $T W !,X X ^%ZOSF(\"TEST\") S:'$T XT3=0 X:XT3 ^%ZOSF(\"RSUM\") W ?10,$S('XT3:\"Routine not in UCI\",XT3'=Y:\"Calculated \"_$C(7)_Y_\", off by \"_(Y-XT3),1:\"ok\")");
		lineTest(version, " S XT4=\"I 1\",X=$T(+3) W !!,\"Checksum routine created on \",$P(X,\";\",4),\" by KERNEL V\",$P(X,\";\",3),!");
		lineTest(version, " S IOP=IOP_\";255\",%ZIS=\"\" D ^%ZIS G:POP H^XUS U IO X ^%ZOSF(\"TYPE-AHEAD\"),^%ZOSF(\"LABOFF\") S X=0 X ^%ZOSF(\"RM\")");
		lineTest(version, " I $D(@G)#10 D WRITE(IO,G)");
		lineTest(version, " .I $Y>(IOSL-9) D UP^DVBCRPR1,NEXT,HDR^DVBCRPR1 W:$O(^DVB(396.4,OLDA,\"RES\",LINE))]\"\"&('+$G(DVBGUI)) !!,\"Exam Results Continued\",!!");
		lineTest(version, " S Y=$$FPS^RCAMFN01($S($G(LDT)>0:$E(LDT,1,5),1:$E(DT,1,5))_$TR($J($$PST^RCAMFN01(DEB),2),\" \",0),$S(+$E($G(LDT),6,7)>$$STD^RCCPCFN:2,1:1)) D DD^%D");
		lineTest(version, " S A=1 H  ");
		lineErrorTest(version, " S A=4  K ,CC,EE");
	}
	
	@Test
	public void testBasic() {
		testBasic(MVersion.CACHE);
		testBasic(MVersion.ANSI_STD_95);
	}
	
	public void testNoErrors(MVersion version) {
		noErrorTest(version, " G @(\"TAG\"_B):C'>3");
		noErrorTest(version, " G @A^@B");
		noErrorTest(version, " G TAG3:A=3,@(\"TAG\"_B):C'>3,@A^@B");
		noErrorTest(version, " D COMP,EN1^PRCAATR(Y) G:$D(DTOUT) Q G ASK");
		noErrorTest(version, " D @(+$P(LST,\",\",FLD))");
	}

	@Test
	public void testNoErrors() {
		testNoErrors(MVersion.CACHE);
		testNoErrors(MVersion.ANSI_STD_95);
	}
	
	public void testBeautify(MVersion version) {
		IToken l = lineTest(version, " S @A=\"S\"  S @H@(0)=3");
		l.beautify();
		String expected = " SET @A=\"S\"  SET @H@(0)=3";
		String actual = l.getStringValue();
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testBeautify() {
		testBeautify(MVersion.CACHE);
		testBeautify(MVersion.ANSI_STD_95);
	}
}
