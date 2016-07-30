package com.vcorsi.myfinder;

import java.math.BigDecimal;

/**
 * Provides {@link Finder} to JUnit tests.
 * 
 * @author vladimiro
 *
 */
public interface FinderSupplier {
	
	Finder get(BigDecimal[] duePayments, BigDecimal sum);

}
