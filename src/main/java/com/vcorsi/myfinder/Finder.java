package com.vcorsi.myfinder;

import java.math.BigDecimal;

/**
 * An interface to solve the Accounting Dilemma.
 * 
 * @author vladimiro
 * 
 */
public interface Finder {

	/**
	 * @return a solution: array of due payments whose sum is the bankTransfer.
	 * @throws IllegalArgumentException
	 */
	BigDecimal[] find();

}
