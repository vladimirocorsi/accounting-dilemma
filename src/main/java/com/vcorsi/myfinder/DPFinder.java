package com.vcorsi.myfinder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses dynamic programming to find a solution to the Accounting Dilemma
 * problem.
 * 
 * @author vladimiro
 *
 */
class DPFinder implements Finder {
	
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
	
	private static final short NO = 0;
	private static final short YES_ME = 1;
	private static final short YES_ME_WITH_PREVIOUS = 2;
	private static final short YES_PREVIOUS_ONLY = 3;
	
	private final BigDecimal[] values;
	private final BigDecimal sum;
	private Struct[][] array;
	
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
		
		final List<BigDecimal> resultList = new ArrayList<>();
		int curValIdx = subsetStruct.curValIdx;
		int curSumIdx = getSumArraySize() - 1;
		while(true){
			final Struct curStruct = array[curValIdx][curSumIdx];
			if(curStruct.code == YES_ME){
				resultList.add(values[curValIdx]);
				break;
			}
			if(curStruct.code == YES_ME_WITH_PREVIOUS){
				resultList.add(values[curValIdx]);
				curValIdx --;
				curSumIdx = curStruct.prevSumIdx;
				continue;
			}
			if(curStruct.code == YES_PREVIOUS_ONLY){
				curValIdx --;
				curSumIdx = curStruct.prevSumIdx;
				continue;
			}
		}
		
		return resultList.toArray(new BigDecimal[resultList.size()]);
	}
	
	private Struct valueSubsetIdx(){
		final int sizeValues = values.length;
		final int sizeSum = getSumArraySize();
		array = new Struct[sizeValues][sizeSum];
		
		for (int j = 0; j < sizeSum; j++) {
			final BigDecimal curSum = BigDecimal.valueOf(1, sum.scale()).multiply(BigDecimal.valueOf(j));
			final boolean valueMatches = values[0].equals(curSum);
			final Struct s = new Struct(valueMatches ? YES_ME : NO, 0, -1);
			if(j == sizeSum -1 && valueMatches){
				return s;
			}
			array[0][j] = s; 
		}
		
		for (int i = 1; i < sizeValues; i++) {
			for (int j = 0; j < sizeSum; j++) {
				final BigDecimal curSum = BigDecimal.valueOf(1, sum.scale()).multiply(BigDecimal.valueOf(j));
				final BigDecimal curValue = values[i];
				final boolean valueMatches = curValue.equals(curSum);
				if(valueMatches){
					array[i][j] =  new Struct(YES_ME,i , -1);
					if(j == sizeSum - 1){
						return array[i][j];
					}
					continue;
				}
				if(array[i - 1][j].code != NO){
					array[i][j] = new Struct(YES_PREVIOUS_ONLY, i, j);
					if(j == sizeSum - 1){
						return array[i][j];
					}
					continue;
				}
				final BigDecimal prevSum = curSum.subtract(curValue);
				if(prevSum.compareTo(BigDecimal.ZERO) > 0){
					final int prevSumIdx = prevSum.unscaledValue().intValue();
					if(array[i - 1][prevSumIdx].code != NO){
						array[i][j] = new Struct(YES_ME_WITH_PREVIOUS, i, prevSumIdx);
						if(j == sizeSum - 1){
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

	private int getSumArraySize() {
		return sum.unscaledValue().intValue() + 1;
	}

}
