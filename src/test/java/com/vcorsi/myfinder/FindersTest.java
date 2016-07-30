package com.vcorsi.myfinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author vladimiro
 * 
 */
public class FindersTest {
	
	private static FinderSupplier dpSupplier;
	private static FinderSupplier subsetSupplier;

	@BeforeClass
	public static void init() {
		dpSupplier = new FinderSupplier() {

			@Override
			public Finder get(BigDecimal[] duePayments, BigDecimal sum) {
				return FinderFactory.dpFinder(duePayments, sum);
			}
		};
		
		subsetSupplier = new FinderSupplier() {

			@Override
			public Finder get(BigDecimal[] duePayments, BigDecimal sum) {
				return FinderFactory.subsetFinder(duePayments, sum);
			}
		};
	}

	@Test
	public void testTrivialSolution(){
		doTestTrivialSolution(dpSupplier);
		doTestTrivialSolution(subsetSupplier);
	}
	
	private void doTestTrivialSolution(FinderSupplier supplier) {
		final BigDecimal[] values = new BigDecimal[] { BigDecimal.valueOf(10),
				BigDecimal.valueOf(20), BigDecimal.valueOf(30),
				BigDecimal.valueOf(40), BigDecimal.valueOf(50) };
		final Finder finder1 = supplier.get(values, BigDecimal
				.valueOf(70));

		final List<BigDecimal> l = Arrays.asList(finder1.find());
		assertFalse(l.isEmpty());

		final Finder finder2 = supplier.get(values, BigDecimal
				.valueOf(72));
		final List<BigDecimal> l2 = Arrays.asList(finder2.find());
		assertTrue(l2.isEmpty());
	}

	@Test
	public void TestDecimalSolution(){
		doTestDecimalSolution(dpSupplier);
		doTestDecimalSolution(subsetSupplier);
	}
	
	private void doTestDecimalSolution(FinderSupplier supplier) {
		final BigDecimal[] values = new BigDecimal[] {
				new BigDecimal(new BigInteger("1012"), 2),
				new BigDecimal(new BigInteger("2010"), 2),
				new BigDecimal(new BigInteger("5065"), 2),
				new BigDecimal(new BigInteger("3048"), 2),
				new BigDecimal(new BigInteger("4009"), 2) };
		final Finder finder = supplier.get(values, new BigDecimal(
				new BigInteger("4060"), 2));

		final List<BigDecimal> l = Arrays.asList(finder.find());
		assertTrue(l.contains(new BigDecimal(new BigInteger("1012"), 2)));
		assertTrue(l.contains(new BigDecimal(new BigInteger("3048"), 2)));
		assertEquals(l.size(), 2);
	}

	@Test
	public void testDecimalSolutionConsistingOfMoreValues(){
		doTestDecimalSolutionConsistingOfMoreValues(dpSupplier);
		doTestDecimalSolutionConsistingOfMoreValues(subsetSupplier);
	}
	
	private void doTestDecimalSolutionConsistingOfMoreValues(FinderSupplier supplier) {
		final BigDecimal[] values = new BigDecimal[] {
				new BigDecimal(new BigInteger("1012"), 2),
				new BigDecimal(new BigInteger("2010"), 2),
				new BigDecimal(new BigInteger("5065"), 2),
				new BigDecimal(new BigInteger("3048"), 2),
				new BigDecimal(new BigInteger("4009"), 2) };
		final Finder finder = supplier.get(values, new BigDecimal(
				new BigInteger("6070"), 2));

		final List<BigDecimal> l = Arrays.asList(finder.find());
		assertTrue(l.contains(new BigDecimal(new BigInteger("1012"), 2)));
		assertTrue(l.contains(new BigDecimal(new BigInteger("3048"), 2)));
		assertTrue(l.contains(new BigDecimal(new BigInteger("2010"), 2)));
		assertEquals(l.size(), 3);
	}

	@Test
	public void TestWithNoInputValues(){
		doTestWithNoInputValues(dpSupplier);
		doTestWithNoInputValues(subsetSupplier);
	}
	
	private void doTestWithNoInputValues(FinderSupplier supplier) {
		final BigDecimal[] values = new BigDecimal[] {};
		try {
			supplier.get(values, new BigDecimal(
					new BigInteger("6070"), 2));
			fail();
		} catch (IllegalArgumentException e) {
			// ok, empty array is not a valid input
		}
	}

	@Test
	public void testWithNegativeInputValues(){
		doTestWithNegativeInputValues(dpSupplier);
		doTestWithNegativeInputValues(subsetSupplier);
	}
	private void doTestWithNegativeInputValues(FinderSupplier supplier) {
		final BigDecimal[] values = new BigDecimal[] {
				new BigDecimal(new BigInteger("1012"), 2),
				new BigDecimal(new BigInteger("2010"), 2),
				new BigDecimal(new BigInteger("5065"), 2),
				new BigDecimal(new BigInteger("-3048"), 2),
				new BigDecimal(new BigInteger("4009"), 2) };
		try {
			supplier.get(values, new BigDecimal(
					new BigInteger("6070"), 2));
			fail();
		} catch (IllegalArgumentException e) {
			// ok, array with negative values is not a valid input
		}
	}

	@Test
	public void testdoTestZeroSum(){
		doTestZeroSum(dpSupplier);
		doTestZeroSum(subsetSupplier);
	}
	
	private void doTestZeroSum(FinderSupplier supplier) {
		final BigDecimal[] values = new BigDecimal[] { BigDecimal.valueOf(10),
				BigDecimal.valueOf(20), BigDecimal.valueOf(30),
				BigDecimal.valueOf(40), BigDecimal.ZERO };
		final Finder finder = supplier.get(values, BigDecimal.ZERO);
		final List<BigDecimal> l = Arrays.asList(finder.find());
		assertTrue(l.contains(BigDecimal.ZERO));
	}

}
