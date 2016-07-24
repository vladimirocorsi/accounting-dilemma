package com.vcorsi.myfinder;

import java.math.BigDecimal;

/**
 * This class allows to instantiate Finder objects
 * 
 * @author vladimiro
 * 
 */
public class FinderFactory {

	/**
	 * @param duePayments
	 *            values representing due payments. A non empty array of non
	 *            negative numbers.
	 * @param sum non negative
	 * @return a Finder object to solve the Accounting Dilemma.
	 * @throws IllegalArgumentException
	 */
	public static Finder myFinder(final BigDecimal[] duePayments, BigDecimal sum) {
		return new DPFinder(duePayments, sum);
	}

}
