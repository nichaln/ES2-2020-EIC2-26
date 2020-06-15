package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import covid_graph_spread.CovidGraphSpread;

public class CovidGraphSpreadTest {
	
	@Test
	public void testWriteHTML() {
		CovidGraphSpread test = new CovidGraphSpread();
		test.writeHTML();
	}

	@Test
	public void testGetLines() {
		fail("Not yet implemented");
	}

	@Test
	public void testMain() {
		CovidGraphSpread.main(null);
	}

}
