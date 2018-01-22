package test.chessclub.com.icc;

import static org.junit.Assert.*;

import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import chessclub.com.icc.parser.ICCParser;
import chessclub.com.icc.parser.ParseException;

public class TestParser {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStart() throws Exception {
	      testVariables v = new testVariables();
	      v.addVar("one", 1);
	      v.addVar("two",2);
	      v.addVar("three",3);
	      v.addVar("twentynine", 29);
	      v.addVar("seventyseven", 77);
	      v.addVar("hello", "world");
	      v.addVar("idjit", "world");
	      v.addVar("initial", 12);
	      v.addVar("increment", 6);
	      try {
	    	  try
	    	  {
	    		  (new ICCParser(new StringReader("one=2&three"))).booleanRule(v);
	    	  } catch(ParseException e) {
	    		  System.out.println(e.currentToken.image);
	    		  System.out.println(e.currentToken.image.length());
	    		  System.out.println("Parse error at column "+(e.currentToken.beginColumn+e.currentToken.image.length()));
	    		  System.out.println("Last good token was "+e.currentToken.image);
	    	  }
			assertEquals(true, (new ICCParser(new StringReader("initial+increment*2/3>=15")).booleanRule(v)));
			assertEquals(false, (new ICCParser(new StringReader("one=2")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("one=1")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("three=1+2")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("three=1+two")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("three=one+two")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("three=one+2")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("twentynine=three+4*5+6")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("twentynine=three+(4*5)+6")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("seventyseven=(three+4)*(5+6)")).booleanRule(v)));
			// I'm not supporting backwards checks like this at this time.
//			assertEquals(false, (new ICCParser(new StringReader("2=one")).booleanRule(v)));
//			assertEquals(true, (new ICCParser(new StringReader("1=one")).booleanRule(v)));
			assertEquals(false, (new ICCParser(new StringReader("three=3&one=2")).booleanRule(v)));
			// I'm not supporting backwards checks like this at this time.
//			assertEquals(false, (new ICCParser(new StringReader("3=three&2=one")).booleanRule(v)));
			assertEquals(false, (new ICCParser(new StringReader("one=2&three=3")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("three=3&one=1")).booleanRule(v)));
			assertEquals(false, (new ICCParser(new StringReader("one=1&one=4")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("hello=\"world\"")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("hello=\"WoRlD\"")).booleanRule(v)));
			// I'm not supporting backwards checks like this at this time.
//			assertEquals(true, (new ICCParser(new StringReader("\"world\"=hello")).booleanRule(v)));
//			assertEquals(true, (new ICCParser(new StringReader("\"world\"=\"world\"")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("hello=hello")).booleanRule(v)));
			assertEquals(true, (new ICCParser(new StringReader("hello=idjit")).booleanRule(v)));

			assertEquals(2, (new ICCParser(new StringReader("2")).intRule(v)));
			assertEquals(1, (new ICCParser(new StringReader("1")).intRule(v)));
			assertEquals(3, (new ICCParser(new StringReader("1+2")).intRule(v)));
			assertEquals(3, (new ICCParser(new StringReader("1+two")).intRule(v)));
			assertEquals(3, (new ICCParser(new StringReader("one+two")).intRule(v)));
			assertEquals(3, (new ICCParser(new StringReader("one+2")).intRule(v)));
			assertEquals(29, (new ICCParser(new StringReader("three+4*5+6")).intRule(v)));
			assertEquals(29, (new ICCParser(new StringReader("three+(4*5)+6")).intRule(v)));
			assertEquals(77, (new ICCParser(new StringReader("(three+4)*(5+6)")).intRule(v)));
		} catch (ParseException e) {
			fail("ParseException: "+e);
		}
	}

}
