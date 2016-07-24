package com.vcorsi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.vcorsi.Format;

/**
 * @author vladimiro
 *
 */
public class FormatTest {

	@Test
	public void testParse() throws Exception{
		final Format p = new Format();
		final String line1 = "0.00";
		final BigDecimal val1 = p.parse(line1);
		assertEquals(val1.compareTo(BigDecimal.ZERO), 0);
		
		final String line2 = "1000.1";
		try{
			p.parse(line2);
			fail();
		}catch(IllegalArgumentException e){
			//ok, we need 2 decimals
		}
		final String line3 = ".1";
		try{
			p.parse(line3);
			fail();
		}catch(IllegalArgumentException e){
			//ok, we need at least a digit before "."
		}
		final String line4 = "10234356777449.98";
		final BigDecimal val4 = p.parse(line4);
		assertEquals(val4.compareTo(new BigDecimal(new BigInteger("1023435677744998"), 2)), 0);
	}
	
	@Test
	public void testFormat() throws Exception{
		final Format p = new Format(); 
		final String val1 = p.format(new BigDecimal(new BigInteger("12346633878976"), 2));
		assertEquals(val1, "123466338789.76");
		final String val2 = p.format(new BigDecimal(new BigInteger("12346633878900"), 2));
		assertEquals(val2, "123466338789.00");
		final String val3 = p.format(new BigDecimal(new BigInteger("123466338789"), 0));
		assertEquals(val3, "123466338789.00");
	}
	
	@Test
	public void testFormatAndParse(){
		final Format p = new Format();
		final BigDecimal bd = new BigDecimal(new BigInteger("12346633878976"), 2);
		final String val1 = p.format(bd);
		assertEquals(p.parse(val1).compareTo(bd), 0);
		
	}

}
