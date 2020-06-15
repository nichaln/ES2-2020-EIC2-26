package tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import covid_sci_discoveries.CovidSciDiscoveries;

public class CovidSciDiscoveriesTest {
	
	

	@Test
	public void testWriteHTML() {
		CovidSciDiscoveries test = new CovidSciDiscoveries(System.getProperty("user.home")+"\\wordpress\\Parte3Repositorio",System.getProperty("user.home")+"\\wordpress\\html\\wp-admin\\Parte3HTML.html");
		test.writeHTML();
	}
	
	@Test
	public void testListDocumentsURL() {
		CovidSciDiscoveries test = new CovidSciDiscoveries(System.getProperty("user.home")+"\\wordpress\\Parte3Repositorio",System.getProperty("user.home")+"\\wordpress\\html\\wp-admin\\Parte3HTML.html");
		test.listDocumentsURL();
	}
	
	@Test
	public void testlistHtmlURL() {
		CovidSciDiscoveries test = new CovidSciDiscoveries(System.getProperty("user.home")+"\\wordpress\\Parte3Repositorio",System.getProperty("user.home")+"\\wordpress\\html\\wp-admin\\Parte3HTML.html");
		test.listHtmlURL();
	}
	@Test
	public void testAddURLs() {
		CovidSciDiscoveries test = new CovidSciDiscoveries(System.getProperty("user.home")+"\\wordpress\\Parte3Repositorio",System.getProperty("user.home")+"\\wordpress\\html\\wp-admin\\Parte3HTML.html");
		test.addURLs(test.listHtmlURL());
	}
	@Test
	public void testRemoveURLs() {
		CovidSciDiscoveries test = new CovidSciDiscoveries(System.getProperty("user.home")+"\\wordpress\\Parte3Repositorio",System.getProperty("user.home")+"\\wordpress\\html\\wp-admin\\Parte3HTML.html");
		test.removeURLs(test.listHtmlURL());
	}
	
	

}
