package tests;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TesteSite {
	private static final int SLEEP_AFTER_URL = 550;
	private static WebDriver driver;
	private static Properties p;
	private static String email;
	private static String messageContent;
	private static LinkedList<UrlStatus> urlsStatus;
	
	public class UrlStatus{
		public String url;
		public String status;
		
		public UrlStatus(String url) {
			this.url=url;
		}
		
		public void setStatus(String status) {
			this.status=status;
		}
		
		public String getUrl() {
			return url;
		}
		
		public String getStatus() {
			return status;
		}
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			p = new Properties();
			p.loadFromXML(new FileInputStream("seleniumTest.xml"));

		} catch (Exception e) {
			System.out.println("Error reading seleniumTest.xml file " + e);
			return;
		}
		email = p.getProperty("email");
		urlsStatus = new LinkedList<UrlStatus>();
		messageContent = "";
		System.setProperty("webdriver.chrome.driver", "C://Users//nicha//Desktop//chromedriver.exe");
		ChromeOptions chrome = new ChromeOptions();
//		chrome.setHeadless(true);
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		 driver.close(); 
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
//		sendEmail();
		writeHtmlFile();
		clearInbox();
	}

	private void processLink(String type) {
		String url = p.getProperty(type + "_url");
		String pagename = p.getProperty(type + "_pagename");
		driver.get(url);
		
		UrlStatus urlstatus = new UrlStatus(url);
		
		if (driver.getTitle().equals(pagename)) {
			System.out.println("all good");
			urlstatus.setStatus("<b style=\"color:#00ff00\">OK</b>");
		} else { // send email erro a aceder à pagina
			messageContent+="\n Erro ao aceder à pagina: \""+ url + "\". Nome da página esperado: "+ "\""+pagename+"\", Nome obtido: " +
			"\""+ driver.getTitle()+"\".\n";
			System.out.println(messageContent);
			urlstatus.setStatus("<b style=\"color:#ff0000\">DOWN</b>");
		}
		urlsStatus.add(urlstatus);
	}

	private void processForm(String type) {
		String url = p.getProperty(type+"_url");
		String resultPage = p.getProperty(type+"_pagename");
		System.out.println(resultPage);
		UrlStatus urlstatus = new UrlStatus(url);
		boolean stop=false;
		driver.get(url);
		
		try {
			Thread.sleep(SLEEP_AFTER_URL);
		} catch (InterruptedException e) {
		}
		
		WebElement elem = null;
		String[] inputs = p.getProperty(type+"_inputs").split(",");
		try {
		for (int i = 0; i < inputs.length; i++) {
			if(driver.findElement(By.name(inputs[i])) != null){
				elem = driver.findElement(By.name(inputs[i]));
				elem.sendKeys(p.getProperty(type+"_" + inputs[i]));
			}
		}
			elem.submit();
		}catch(NoSuchElementException e) {
			stop=true;
		}
		
		System.out.println(driver.getTitle());
		if (driver.getTitle().equals(resultPage) && !stop) {
			System.out.println("Sucesso");
			urlstatus.setStatus("<b style=\"color:#00ff00\">OK</b>");
		}else {
			messageContent+="\n Erro ao aceder à pagina: \""+ url + "\". Nome da página esperado: "+ "\""+resultPage+"\", Nome obtido: " +
					"\""+ driver.getTitle()+"\". Numero de inputs introduzidos: "+ inputs.length + ".\n";
			System.out.println(messageContent);
			urlstatus.setStatus("<b style=\"color:#ff0000\">DOWN</b>");
		}
		if(p.getProperty(type+"_check_email") != null) {
			UrlStatus us = new UrlStatus(url+" --- EMAIL AVAILABILITY ");
			try {
				System.out.println("Aguardando a receção do email!");
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(check_email_received(p.getProperty(type+"check_email"), p.getProperty(type+"_check_subject"))) {
				us.setStatus("<b style=\"color:#00ff00\">EMAIL OK</b>");
			}else {
				us.setStatus("<b style=\"color:#ff0000\">EMAIL DOWN</b>");
				messageContent+="\n Email não recebido! Url: "+ url+ "\n";
			}
			urlsStatus.add(us);
		}
		urlsStatus.add(urlstatus);
	}
	
	private static boolean check_email_received(String email,String emailTitle) {
		try {
            Properties properties = new Properties();
            properties.put("mail.pop3.starttls.enable", "true");
            properties.put("mail.pop3.host", "pop.sapo.pt");
            properties.put("mail.pop3.port", "995");
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("pop3s");
            store.connect("pop.sapo.pt", "es2-2020-eic2-26@sapo.pt", "passwordES2020");
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            Flags seen = new Flags(Flags.Flag.RECENT);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
//          Message[] messages = emailFolder.getMessages();
            Message[] messages = emailFolder.search(unseenFlagTerm);
            System.out.println(messages.length);
            Message message = messages[messages.length - 1];
            
            String titleSeen = message.getSubject();
            emailFolder.close(false);
            store.close();
            return titleSeen.equals(emailTitle);

        } catch (NoSuchProviderException e) {
        } catch (MessagingException e) {
        } catch (Exception e) {
        }
        return false;
	}

	private static void sendEmail() {
		if (!messageContent.equals("")) {
			String to = email;
			String from = "es2-2020-eic2-26@sapo.pt";
			final String username = "es2-2020-eic2-26@sapo.pt";
			final String password = "passwordES2020";

			Properties properties = new Properties();
			properties.put("mail.smtp.host", "smtp.sapo.pt");
			properties.put("mail.smtp.port", "587");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");

			Session session = Session.getInstance(properties, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
			try {
				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				message.setSubject("Erros no website");
				message.setText(messageContent);
				Transport.send(message);
				System.out.println("Mensagem enviada com sucesso");
			} catch (MessagingException mex) {
				mex.printStackTrace();
			}
		}else {
			System.out.println("Sem mensagem de erro a enviar!");
		}
	}
	
	public void writeHtmlFile() {
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		try {
		    fWriter = new FileWriter("C:\\Users\\nicha\\wordpress2\\html\\wp-admin\\urlsanalytics.html");
		    writer = new BufferedWriter(fWriter);
		    String html = "<!DOCTYPE html>\r\n"+
				    		"<html>\r\n" + 
				    		"<head>\r\n" + 
				    		"<meta charset=\"ISO-8859-1\">\r\n" + 
				    		"<title>Links Avalability</title>\r\n" + 
				    		"</head>\r\n" + 
				    		"\r\n" +
				    		"<style>\r\n"+
				    		"table {\r\n"+
				    		"  font-family: arial, sans-serif;\r\n"+
				    		"  border-collapse: collapse;\r\n"+
				    		"  width: 100%;\r\n"+
				    		"}\r\n"+
				    		"td, th {\r\n"+
				    		"  border: 1px solid #dddddd;\r\n"+
				    		"  text-align: left;\r\n"+
				    		"  padding: 8px;\r\n"+
				    		"}\r\n"+

				    		"tr:nth-child(even) {\r\n"+
				    		"  background-color: #dddddd;\r\n"+
				    		"}\r\n"+
				    		"</style>\r\n"+
				    		"</head>\r\n"+
				    		"<body>\r\n"+
				    		"<h2>Links Availability</h2>\r\n"+
				    		"<table>\r\n"+
				    		"	<tr>\r\n" + 
				    		"    <th>Link</th>\r\n" + 
				    		"    <th>Status</th>\r\n" +  
				    		"	</tr>\r\n";
		    for(UrlStatus us : urlsStatus) {
		    	html+="<tr>\r\n"+
		    		  "<td>"+us.getUrl()+"</td>\r\n"+
		    		  "<td>"+us.getStatus()+"</td>\r\n"+
		    		  "</tr>\r\n";
		    }
		    			  
		    html+="</table>\r\n"+
		          "<p><b style=\"color:Blue\">Last Check: "+dateFormat.format(date)+"<b></p>\r\n"+
		    	  "</body>\r\n"+
		    	  "</html>\r\n";
		    writer.write(html);
		    writer.close();
		} catch (Exception e) {
		}
	}
	
	private void clearInbox() {
		try 
	      {
	         // get the session object
	         Properties properties = new Properties();
	         properties.put("mail.store.protocol", "pop3");
	         properties.put("mail.pop3s.host", "pop.sapo.pt");
	         properties.put("mail.pop3s.port", "995");
	         properties.put("mail.pop3.starttls.enable", "true");
	         Session emailSession = Session.getDefaultInstance(properties);
	         Store store = emailSession.getStore("pop3s");

	         store.connect("pop.sapo.pt", "es2-2020-eic2-26@sapo.pt", "passwordES2020");

	         Folder emailFolder = store.getFolder("INBOX");
	         emailFolder.open(Folder.READ_WRITE);

	         Message[] messages = emailFolder.getMessages();
	         System.out.println("messages.length---" + messages.length);
	         int counter = 0;
	         for (int i = 0; i < messages.length; i++) {
	        	 Message message = messages[i];
	        	 message.setFlag(Flags.Flag.DELETED, true);
	        	 counter++;
	         }
	         System.out.println("Mensagens apagadas:" + counter);
	         emailFolder.close(true);
	         store.close();

	      } catch (NoSuchProviderException e) {
	         e.printStackTrace();
	      } catch (MessagingException e) {
	         e.printStackTrace();
	      }
	}

}
