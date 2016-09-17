package com.vcorsi.myfinder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Uses dynamic programming to find a solution to the Accounting Dilemma
 * problem. Given a set of values V and a target sum S the solution complexity is
 * O(|V| * unscaledValue(S)). 
 * For details see https://en.wikipedia.org/wiki/Subset_sum_problem 
 * 
 * @author vladimiro
 *
 */
class DPFinder implements Finder {
	
	/**
	 * Used to denote an element of the matrix M used for the solution of the problem.
	 * M[i,j] denotes an element describing if there is a subset of the set composed of the (1st, .. i-th) 
	 * values that satisfies the sum S(j) correspondent to the j-th column. 
	 * S(j) = sum.scale() * (min(values).scale() + j)
	 * 
	 * @author vladimiro
	 *
	 */
	static class Struct{
		public Struct(short code, int curValIdx, int prevSumIdx) {
			this.code = code;
			this.curValIdx = curValIdx;
			this.prevSumIdx = prevSumIdx;
		}
		short code;
		int curValIdx;
		int prevSumIdx;
	}
	
	/**
	 * There's no subset
	 */
	private static final short NO = 0;
	/**
	 * the subset only contains the i-th element
	 */
	private static final short YES_ME = 1;
	/**
	 * there is a subset including the i-th element and others
	 */
	private static final short YES_ME_WITH_PREVIOUS = 2;
	/**
	 * there is a subset including other values than i-th
	 */
	private static final short YES_PREVIOUS_ONLY = 3;
	
	private final BigDecimal[] values;
	private final BigDecimal sum;
	private Struct[][] array;
	private final BigDecimal minValue;
	private final int numCols;
	private final int minValueSize;
	private final int numRows;
	
	DPFinder(final BigDecimal[] values, final BigDecimal sum) {
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
		this.values = values.clone();
		this.sum = sum;
		this.minValue = Collections.min(Arrays.asList(values));
		this.minValueSize = minValue.unscaledValue().intValue();
		this.numCols = sum.unscaledValue().intValue() + 1 - minValueSize;
		this.numRows = values.length;
	}
	
	@Override
	public BigDecimal[] find() {
		BigDecimal valueSum = BigDecimal.ZERO;
		for(BigDecimal d : values){
			valueSum = valueSum.add(d);
		}
		if(sum.compareTo(valueSum) > 0){
		    return new BigDecimal[0];	
		}
		if(sum.compareTo(valueSum) == 0){
			return values;
		}
		
		final Struct subsetStruct = valueSubsetIdx();
		if(subsetStruct == null){
			return new BigDecimal[0];
		}
		
		// a subset exists!
		final List<BigDecimal> resultList = new ArrayList<>();
		int curValIdx = subsetStruct.curValIdx;
		int curSumIdx = numCols - 1;
		while(true){
			final Struct curStruct = array[curValIdx][curSumIdx];
			if(curStruct.code == YES_ME){
				//search is over
				resultList.add(values[curValIdx]);
				break;
			}
			if(curStruct.code == YES_ME_WITH_PREVIOUS){
				//we add the current value and seek for the next one
				resultList.add(values[curValIdx]);
				curValIdx --;
				curSumIdx = curStruct.prevSumIdx;
				continue;
			}
			if(curStruct.code == YES_PREVIOUS_ONLY){
				//we skip the current value and seek for the next one
				curValIdx --;
				curSumIdx = curStruct.prevSumIdx;
				continue;
			}
		}
		
		return resultList.toArray(new BigDecimal[resultList.size()]);
	}
	
	/**
	 * calculates the matrix and returns the matrix element, if any, from which we can extract the subset
	 * of values satisfying the sum.
	 * 
	 * @return a matrix element or null
	 */
	private Struct valueSubsetIdx(){
		array = new Struct[numRows][numCols];
		
		for (int j = 0; j < numCols; j++) {
			final BigDecimal curSum = BigDecimal.valueOf(1, sum.scale()).multiply(BigDecimal.valueOf(j + minValueSize));
			final boolean valueMatches = values[0].equals(curSum);
			final Struct s = new Struct(valueMatches ? YES_ME : NO, 0, -1);
			if(j == numCols -1 && valueMatches){
				return s;
			}
			array[0][j] = s; 
		}
		
		for (int i = 1; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				final BigDecimal curSum = BigDecimal.valueOf(1, sum.scale()).multiply(BigDecimal.valueOf(j + minValueSize));
				final BigDecimal curValue = values[i];
				final boolean valueMatches = curValue.equals(curSum);
				if(valueMatches){
					array[i][j] =  new Struct(YES_ME,i , -1);
					if(j == numCols - 1){
						return array[i][j];
					}
					continue;
				}
				if(array[i - 1][j].code != NO){
					array[i][j] = new Struct(YES_PREVIOUS_ONLY, i, j);
					if(j == numCols - 1){
						return array[i][j];
					}
					continue;
				}
				final BigDecimal prevSum = curSum.subtract(curValue);
				if(prevSum.compareTo(minValue) >= 0){
					final int prevSumIdx = getIdx(prevSum);
					if(array[i - 1][prevSumIdx].code != NO){
						array[i][j] = new Struct(YES_ME_WITH_PREVIOUS, i, prevSumIdx);
						if(j == numCols - 1){
							return array[i][j];
						}
						continue;
					}
				}
				array[i][j] = new Struct(NO, i, -1);
			}
		}
		return null;
	}

	private int getIdx(BigDecimal prevSum) {
		return prevSum.unscaledValue().intValue() - minValueSize;
	}

}
