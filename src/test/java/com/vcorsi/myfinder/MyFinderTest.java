package com.vcorsi.myfinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * @author vladimiro
 * 
 */
public class MyFinderTest {

	@Test
	public void testTrivialSolution() {
		final BigDecimal[] values = new BigDecimal[] { BigDecimal.valueOf(10),
				BigDecimal.valueOf(20), BigDecimal.valueOf(30),
				BigDecimal.valueOf(40), BigDecimal.valueOf(50) };
		final Finder finder1 = FinderFactory.myFinder(values, BigDecimal
				.valueOf(70));

		final List<BigDecimal> l = Arrays.asList(finder1.find());
		assertFalse(l.isEmpty());

		final Finder finder2 = FinderFactory.myFinder(values, BigDecimal
				.valueOf(72));
		final List<BigDecimal> l2 = Arrays.asList(finder2.find());
		assertTrue(l2.isEmpty());
	}

	@Test
	public void testDecimalSolution() {
		final BigDecimal[] values = new BigDecimal[] {
				new BigDecimal(new BigInteger("1012"), 2),
				new BigDecimal(new BigInteger("2010"), 2),
				new BigDecimal(new BigInteger("5065"), 2),
				new BigDecimal(new BigInteger("3048"), 2),
				new BigDecimal(new BigInteger("4009"), 2) };
		final Finder finder = FinderFactory.myFinder(values, new BigDecimal(
				new BigInteger("4060"), 2));

		final List<BigDecimal> l = Arrays.asList(finder.find());
		assertTrue(l.contains(new BigDecimal(new BigInteger("1012"), 2)));
		assertTrue(l.contains(new BigDecimal(new BigInteger("3048"), 2)));
		assertEquals(l.size(), 2);
	}

	@Test
	public void testDecimalSolutionConsistingOfMoreValues() {
		final BigDecimal[] values = new BigDecimal[] {
				new BigDecimal(new BigInteger("1012"), 2),
				new BigDecimal(new BigInteger("2010"), 2),
				new BigDecimal(new BigInteger("5065"), 2),
				new BigDecimal(new BigInteger("3048"), 2),
				new BigDecimal(new BigInteger("4009"), 2) };
		final Finder finder = FinderFactory.myFinder(values, new BigDecimal(
				new BigInteger("6070"), 2));

		final List<BigDecimal> l = Arrays.asList(finder.find());
		assertTrue(l.contains(new BigDecimal(new BigInteger("1012"), 2)));
		assertTrue(l.contains(new BigDecimal(new BigInteger("3048"), 2)));
		assertTrue(l.contains(new BigDecimal(new BigInteger("2010"), 2)));
		assertEquals(l.size(), 3);
	}

	@Test
	public void testWithNoInputValues() {
		final BigDecimal[] values = new BigDecimal[] {};
		try {
			FinderFactory.myFinder(values, new BigDecimal(
					new BigInteger("6070"), 2));
			fail();
		} catch (IllegalArgumentException e) {
			// ok, empty array is not a valid input
		}
	}

	@Test
	public void testWithNegativeInputValues() {
		final BigDecimal[] values = new BigDecimal[] {
				new BigDecimal(new BigInteger("1012"), 2),
				new BigDecimal(new BigInteger("2010"), 2),
				new BigDecimal(new BigInteger("5065"), 2),
				new BigDecimal(new BigInteger("-3048"), 2),
				new BigDecimal(new BigInteger("4009"), 2) };
		try {
			FinderFactory.myFinder(values, new BigDecimal(
					new BigInteger("6070"), 2));
			fail();
		} catch (IllegalArgumentException e) {
			// ok, array with negative values is not a valid input
		}
	}

	@Test
	public void testZeroSumS() {
		final BigDecimal[] values = new BigDecimal[] { BigDecimal.valueOf(10),
				BigDecimal.valueOf(20), BigDecimal.valueOf(30),
				BigDecimal.valueOf(40), BigDecimal.ZERO };
		final Finder finder = FinderFactory.myFinder(values, BigDecimal.ZERO);
		final List<BigDecimal> l = Arrays.asList(finder.find());
		assertTrue(l.contains(BigDecimal.ZERO));
	}

}
