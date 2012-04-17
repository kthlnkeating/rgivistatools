package com.raygroupintl.vista.tools.test;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.raygroupintl.vista.struct.MLineLocation;
import com.raygroupintl.vista.tools.ErrorExemptions;

public class ErrorExemptionsTest {
	@Test
	public void test() {
		ErrorExemptions exemptions = new ErrorExemptions();
		exemptions.addLine("ANRVRRL", "BEGIN", 3);
		exemptions.addLine("ANRVRRL", "A1R", 2);
		Set<MLineLocation> locations = exemptions.getLines("ANRVRRL");
		Assert.assertTrue(locations.contains(new MLineLocation("BEGIN", 3)));		
		Assert.assertFalse(locations.contains(new MLineLocation("BEGINX", 3)));		
		Assert.assertFalse(locations.contains(new MLineLocation("BEGIN", 4)));		
	}

}
