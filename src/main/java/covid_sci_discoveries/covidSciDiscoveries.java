package covid_sci_discoveries;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

public class covidSciDiscoveries {
	
	private InputStream[] sites;
	
	/*public void readPDF() throws IOException {
		
		initializeSites();	
		for (int i = 0; i<sites.length; i++) {
			
		InputStream is = sites[i];	
		PDDocument document = PDDocument.load(is);
		
		PDDocumentInformation info = document.getDocumentInformation();
		
		System.out.println("----------------------------"+i+"----------------------------");
		
		System.out.println( "Article Title = " + info.getTitle() );
		System.out.println( "Creator = " + info.getCreator() );
		System.out.println( "Authors = " + info.getAuthor() );
		System.out.println( "Creation Date = " + (info.getCreationDate().getTime().getYear() + 1900));
		
		//System.out.println("----------------------------------------------------------");
		
		//is.close();
		document.close();
		}
		
	}*/
	
	//Metodo para escrever uma linha na tabela html
	public String getLine(int i) throws IOException {
		initializeSites();	
		InputStream is = sites[i];
		PDDocument document = PDDocument.load(is);
		PDDocumentInformation info = document.getDocumentInformation();
		document.close();
		return "  <tr>\r\n" + 
	    		"    <td>"+ info.getTitle() +"</td>\r\n" + 
	    		"    <td>"+ info.getCreator() +"</td>\r\n" + 
	    		"    <td>"+ (info.getCreationDate().getTime().getYear() +1900) +"</td>\r\n" +
	    		"    <td>"+ info.getAuthor() +"</td>\r\n" +  
	    		"  </tr>\r\n";
	}
	
	//Metodo para inicializar os documentos pdf
	public void initializeSites() throws MalformedURLException, IOException {
		sites = new InputStream[4];
		sites[0] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42509?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%271-s2.0-S1755436517301135-main.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200610T150000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200610%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=8fb59a8ed8f9fb25f041cccca802022c396d5a792fa8b78523d6b683ac2219a4").openStream();
		sites[1] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42550?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%27178-1-53.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200610T150000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200610%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=3d56953441b99dbf52a0530f7f8bfa5eb4dca70037c783bbced67532748fb84a").openStream();
		sites[2] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42552?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%27biology-09-00097.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200610T150000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200610%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=90ab74e89769475b58bc7f3231a707a85b261ecef02d0110e7f8ebf8ceb4e1b1").openStream();
		sites[3] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42528?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%27biology-09-00094.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200610T150000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200610%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=66c5738b1d1b31f4c0b64cc1617bdaa59210727be18c31132f46d21f9583ed1d").openStream();
	}
	
	//Metodo que escreve o html todo para o ficheiro webTable.html
	public void writeHTML() {
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try {
			System.out.println(getLine(0));
		    fWriter = new FileWriter("C:\\Users\\katsa\\git\\ES2-2020-EIC2-26\\src\\main\\java\\covid_sci_discoveries\\webTable.html");
		    writer = new BufferedWriter(fWriter);
		    writer.write("<!DOCTYPE html>\r\n" + 
		    		"<html>\r\n" + 
		    		"<head>\r\n" + 
		    		"<meta charset=\"ISO-8859-1\">\r\n" + 
		    		"<title>COVID discoveries</title>\r\n" + 
		    		"</head>\r\n" + 
		    		"\r\n" + 
		    		"<style>\r\n" + 
		    		"table, th, td {\r\n" + 
		    		"  border: 1px solid black;\r\n" + 
		    		"}\r\n" + 
		    		"\r\n" + 
		    		"</style>\r\n" + 
		    		"<body>\r\n" + 
		    		"\r\n" + 
		    		"<table style=\"width:60%\">\r\n" + 
		    		"  <tr>\r\n" + 
		    		"    <th>Article Title</th>\r\n" + 
		    		"    <th>Journal Name</th>\r\n" + 
		    		"    <th>Publication Year</th>\r\n" + 
		    		"    <th>Authors</th>\r\n" + 
		    		"  </tr>\r\n" + 
		    		getLine(0) + getLine(1) + getLine(2) + getLine(3) +
		    		"</table>\r\n" + 
		    		"</body>\r\n" + 
		    		"</html>");
		    writer.close();
		    
		} catch (Exception e) {
		  //catch any exceptions here
		}
	}
	
	public static void main (String[]args) {
		covidSciDiscoveries test = new covidSciDiscoveries();
		//test.readPDF();
		test.writeHTML();
	}
	

}
