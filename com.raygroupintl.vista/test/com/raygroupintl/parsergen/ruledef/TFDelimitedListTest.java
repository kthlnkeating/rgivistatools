package com.raygroupintl.parsergen.ruledef;

import static org.junit.Assert.fail;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.raygroupintl.charlib.CharPredicate;
import com.raygroupintl.charlib.CharRangePredicate;
import com.raygroupintl.parser.TextPiece;
import com.raygroupintl.parser.SyntaxErrorException;
import com.raygroupintl.parser.TFCharacter;
import com.raygroupintl.parser.TFDelimitedList;
import com.raygroupintl.parser.TFString;
import com.raygroupintl.parser.Text;
import com.raygroupintl.parser.Token;
import com.raygroupintl.parser.TokenFactory;
import com.raygroupintl.parser.Tokens;
import com.raygroupintl.parsergen.ObjectSupply;
import com.raygroupintl.parsergen.ruledef.DefaultObjectSupply;

public class TFDelimitedListTest {
	private static ObjectSupply objectSupply;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		objectSupply = new DefaultObjectSupply();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		objectSupply = null;
	}

	public static void validTokenCheck(TextPiece t, String v) {
		Assert.assertEquals(v, t.toString());
		Assert.assertEquals(v.length(), t.length());		
	}
	
	public static void validCheckBasic(TFDelimitedList f, String v, String expected, String[] iteratorResults) {
		Text text = new Text(v);
		try {
			Tokens t = (Tokens) f.tokenize(text, objectSupply);
			validTokenCheck(t.toValue(), expected);
			int index = 0;
			for (Token tit : t.toLogicalIterable()) {
				validTokenCheck(tit.toValue(), iteratorResults[index]);
				++index;
			}
			Assert.assertEquals(iteratorResults.length, index);
			Assert.assertEquals(index, t.size());			
		} catch (SyntaxErrorException e) {
			fail("Unexpected xception: " + e.getMessage());
		}
	}

	public static void validCheck(TFDelimitedList f, String v, String addl, String[] iteratorResults) {
		validCheckBasic(f, v, v, iteratorResults);
		validCheckBasic(f, v + addl, v, iteratorResults);
	}

	public static void errorCheck(TFDelimitedList f, String v, int errorLocation) {
		Text text = new Text(v);
		try {
			f.tokenize(text, objectSupply);
			fail("Expected exception did not fire.");							
		} catch (SyntaxErrorException e) {
			Assert.assertEquals(errorLocation, text.getIndex());
		}
	}

	public static void nullCheck(TFDelimitedList f, String v) {
		Text text = new Text(v);
		try {
			Token t = f.tokenize(text, objectSupply);
			Assert.assertNull(t);
		} catch(SyntaxErrorException e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	public void test() {
		TokenFactory delimiter = new TFCharacter(",", new CharPredicate(','));
		TokenFactory element = new TFString("e", new CharRangePredicate('a', 'z'));
		
		TFDelimitedList dl = new TFDelimitedList("dl");
		try {
			dl.tokenize(new Text("a"), objectSupply);
			fail("Expected exception did not fire.");							
		} catch (IllegalStateException e) {
		} catch (SyntaxErrorException se) {
			fail("Unexpected exception: " + se.getMessage());			
		}
		
		dl.set(element, delimiter);
		validCheck(dl, "a", ")", new String[]{"a"});
		validCheck(dl, "a,b", ")", new String[]{"a", "b"});
		validCheck(dl, "a,b,c", ")", new String[]{"a", "b", "c"});
		errorCheck(dl, "a,", 2);
		errorCheck(dl, "a,B", 2);
		errorCheck(dl, "a,B,c", 2);
		errorCheck(dl, "a,b,C", 4);
		errorCheck(dl, "a,,c", 2);
		errorCheck(dl, "a,b,", 4);
		nullCheck(dl, "A");
		nullCheck(dl, "");
		nullCheck(dl, " ");
		nullCheck(dl, ",");
		
		dl.set(element, delimiter, true);
		validCheck(dl, "a", ")", new String[]{"a"});
		validCheck(dl, "a,b", ")", new String[]{"a", "b"});
		validCheck(dl, "a,b,c", ")", new String[]{"a", "b", "c"});
		validCheck(dl, "a,,c", ")", new String[]{"a", "", "c"});
		validCheck(dl, "a,b,", ")", new String[]{"a", "b", ""});
		validCheck(dl, ",", ")", new String[]{"", ""});		
		nullCheck(dl, "A");
		nullCheck(dl, "");
		nullCheck(dl, " ");
	}
}
