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
	 * @return a Finder object to solve the Accounting Dilemma. Chooses between {@link DPFinder} 
	 * and {@link SubsetFinder} based on the structure of input: if
	 * <code>Math.pow(2, numOfPayments) > numOfPayments * sum.unscaledValue().longValue()</code> then
	 * {@link DPFinder} is used.
	 * @throws IllegalArgumentException
	 */
	public static Finder finder(final BigDecimal[] duePayments, final BigDecimal sum) {
		final int numOfPayments = duePayments.length;
		if(Math.pow(2, numOfPayments) > numOfPayments * sum.unscaledValue().longValue()){
			return new DPFinder(duePayments, sum);
		}else{
			return new SubsetFinder(duePayments, sum);
		}
	}
	
	public static SubsetFinder subsetFinder(final BigDecimal[] duePayments, final BigDecimal sum){
		return new SubsetFinder(duePayments, sum);
	}
	
	public static DPFinder dpFinder(final BigDecimal[] duePayments, final BigDecimal sum){
		return new DPFinder(duePayments, sum);
	}

}
