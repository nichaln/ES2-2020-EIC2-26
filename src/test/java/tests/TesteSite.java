package tests;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TesteSite {
	static WebDriver driver;
	static Properties p;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			p = new Properties();
			p.load(new FileInputStream("seleniumTest.ini"));

		} catch (Exception e) {
			System.out.println("Error reading seleniumTest.ini file " + e);
			return;
		}

		System.setProperty("webdriver.chrome.driver", "C://Users//nicha//Desktop//chromedriver.exe");
		ChromeOptions chrome = new ChromeOptions();
//		chrome.setHeadless(true);
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
//		 driver.close(); 
//		 driver.quit();    
	}

	@Test
	public void test() {
		String type;
		
		int j = 1;
		while (p.getProperty("type" + j) != null) {
			type = p.getProperty("type" + j);
			System.out.println(type);
			if (type.equals("link")) {
				processLink("type" + j);
			}
			if (type.equals("form")) {
				processForm("type" + j);
			}
			j++;
		}
	}

	private void processLink(String type) {
		String url = p.getProperty(type + "_url");
		String pagename = p.getProperty(type + "_pagename");
		driver.get(url);
		if (driver.getTitle().equals(pagename)) {
			System.out.println("all good");
		} else { // send email erro a aceder à pagina
			String erro = "Erro ao aceder à página: \"" + url + "\"";
			System.out.println(erro);
		}
	}

	private void processForm(String type) {
		String url = p.getProperty("type2_url");
		driver.get(url);
		WebElement elem = null;
		String[] inputs = p.getProperty("type2_inputs").split(",");
		for (int i = 0; i < inputs.length; i++) {
			elem = driver.findElement(By.name(inputs[i]));
			elem.sendKeys(p.getProperty("type2_" + inputs[i]));
		}
		elem.submit();
	}

}
