package com.vcorsi.myfinder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;

/**
 * Searches the space of all possible subsets to find a
 * solution to the Accounting Dilemma problem.
 * 
 * @author vladimiro
 * 
 */
class SubsetFinder implements Finder {

	private final BigDecimal[] sortedValues;
	private final BigDecimal sum;

	/**
	 * @param values
	 *            A non empty array of non negative numbers.
	 * @throws IllegalArgumentException
	 */
	SubsetFinder(final BigDecimal[] values, final BigDecimal sum) {
		if (values == null) {
			throw new NullPointerException("Values cannot be null");
		}
		if (sum.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Sum cannot be negative");
		}
		if (values.length == 0) {
			throw new IllegalArgumentException("Values cannot be empty");
		}
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null || values[i].compareTo(BigDecimal.ZERO) < 0) {
				throw new IllegalArgumentException("Invalid values");
			}
		}
		final BigDecimal[] sorted = values.clone();
		Arrays.sort(sorted, Collections.reverseOrder());
		sortedValues = sorted;
		this.sum = sum;
	}

	/**
	 * @param sum
	 *            A non negative value.
	 * @return An array of values, chosen between the ones given in constructor,
	 *         corresponding to a solution of the problem. If no solution is
	 *         found an empty array is returned.
	 * @throws IllegalArgumentException
	 */
	@Override
	public BigDecimal[] find() {
		BigDecimal valueSum = BigDecimal.ZERO;
		for(BigDecimal d : sortedValues){
			valueSum = valueSum.add(d);
		}
		if(sum.compareTo(valueSum) > 0){
		    return new BigDecimal[0];	
		}
		if(sum.compareTo(valueSum) == 0){
			return sortedValues;
		}
		
		final int[] idxes = find(0, sum);
		final BigDecimal[] result = new BigDecimal[idxes.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = sortedValues[idxes[i]];
		}
		return result;
	}

	/**
	 * @param start
	 *            index where to start the search
	 * @param sum
	 *            sum to be satisfied
	 * @return array of indexes of values satisfying the sum
	 */
	private int[] find(int start, BigDecimal sum) {
		assert (sum.compareTo(BigDecimal.ZERO) >= 0);
		assert (start < sortedValues.length);
		final BigDecimal startVal = sortedValues[start];
		if (startVal.compareTo(sum) > 0) {
			if (start < sortedValues.length - 1) {
				// too big, search for a solution without start
				return find(start + 1, sum);
			} else {
				// start is last index, no solution: base step for recursion.
				return new int[] {};
			}
		}
		if (startVal.compareTo(sum) == 0) {
			// we have a solution. Base step for recursion.
			return new int[] { start };
		}
		final int numOfElements = sortedValues.length - start;
		final BigDecimal average = sum.divide(
				BigDecimal.valueOf(numOfElements), 2, RoundingMode.DOWN);
		if (startVal.compareTo(average) < 0) {
			// Since values are sorted it's impossible to reach sum.
			// When we are at last element, average equals to sum: so this is a base
			// step for our recursion.
			assert (numOfElements != 1 || average.compareTo(sum) == 0);
			return new int[] {};
		}
		// ok, it is possible to have a solution including start and there are
		// still values to visit
		final int[] remainingIndexes = find(start + 1, sum.subtract(startVal));
		if (remainingIndexes.length == 0) {
			// no solution including start, we search without start
			final int[] remainingIndexesWithout = find(start + 1, sum);
			return remainingIndexesWithout;
		} else {
			// solution found
			final int[] result = Arrays.copyOf(remainingIndexes,
					remainingIndexes.length + 1);
			result[remainingIndexes.length] = start;
			return result;
		}
	}

}
