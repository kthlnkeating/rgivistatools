package com.raygroupintl.vista.struct.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.raygroupintl.vista.struct.MLineLocation;

public class MLineLocationTest {	
	@Test
	public void testHashCode() {
		MLineLocation p = new MLineLocation("BEGIN", 3);
		Set<MLineLocation> c = new HashSet<MLineLocation>();
		c.add(p);
		Assert.assertTrue(c.contains(new MLineLocation("BEGIN", 3)));
		Assert.assertFalse(c.contains(new MLineLocation("BEGIN", 4)));
		Assert.assertFalse(c.contains(new MLineLocation("BEGINX", 3)));
		Assert.assertTrue(c.contains(new MLineLocation("BEGIN", 3)));
		c.add(new MLineLocation("BEGIN", 3));
		Assert.assertEquals(1, c.size());
		c.add(new MLineLocation("ARV", 2));
		Assert.assertEquals(2, c.size());
	}
		
	@Test
	public void testEquals() {
		MLineLocation lhs0 = new MLineLocation("BEGIN", 3);
		MLineLocation rhs00 = new MLineLocation("BEGIN", 3);
		Assert.assertEquals(lhs0, rhs00);
		MLineLocation rhs01 = new MLineLocation("BEGIN", 2);
		Assert.assertFalse(lhs0.equals(rhs01));
		MLineLocation rhs02 = new MLineLocation("BEGINX", 3);
		Assert.assertFalse(lhs0.equals(rhs02));
	}
}
