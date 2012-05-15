package com.raygroupintl.bnf;

import static org.junit.Assert.fail;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

import com.raygroupintl.charlib.CharPredicate;
import com.raygroupintl.charlib.CharRangePredicate;

public class TFDelimitedListTest {
	public static void validTokenCheck(Token t, String v) {
		Assert.assertEquals(v, t.getStringValue());
		Assert.assertEquals(v.length(), t.getStringSize());		
	}
	
	public static void validCheckBasic(TFDelimitedList f, String v, String expected, String[] iteratorResults) {
		Text text = new Text(v);
		try {
			TDelimitedList t = (TDelimitedList) f.tokenize(text);
			validTokenCheck(t, expected);
			int index = 0;
			Iterator<Token> it = null;
			for (it = t.iterator(); it.hasNext(); ++index) {
				Token tit = it.next();
				validTokenCheck(tit, iteratorResults[index]);
			}
			Assert.assertEquals(iteratorResults.length, index);
			Assert.assertFalse(it.hasNext());			
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
			f.tokenize(text);
			fail("Expected exception did not fire.");							
		} catch (SyntaxErrorException e) {
			Assert.assertEquals(errorLocation, text.getIndex());
		}
	}

	public static void nullCheck(TFDelimitedList f, String v) {
		Text text = new Text(v);
		try {
			Token t = f.tokenize(text);
			Assert.assertNull(t);
		} catch(SyntaxErrorException e) {
			fail("Unexpected exception: " + e.getMessage());
		}
	}

	@Test
	public void test() {
		TokenFactory delimiter = new TFCharacter(",", new CharPredicate(','), new DefaultCharacterAdapter());
		TokenFactory element = new TFString("e", new CharRangePredicate('a', 'z'), new DefaultStringAdapter());
		
		TFDelimitedList dl = new TFDelimitedList("dl");
		try {
			dl.tokenize(new Text("a"));
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
