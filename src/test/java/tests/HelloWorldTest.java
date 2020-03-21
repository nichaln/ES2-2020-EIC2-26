package tests;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import app.HelloWorld;

public class HelloWorldTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testMain() {
		HelloWorld hw = new HelloWorld();
	}

}
