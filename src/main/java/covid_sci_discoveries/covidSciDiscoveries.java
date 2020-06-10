package covid_sci_discoveries;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

public class covidSciDiscoveries {
	
	private InputStream[] sites;
	
	public void readPDF() throws IOException {
		
		initializeSites();
		//InputStream is = new URL ("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42509?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%271-s2.0-S1755436517301135-main.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200610T150000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200610%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=8fb59a8ed8f9fb25f041cccca802022c396d5a792fa8b78523d6b683ac2219a4").openStream();
		
		for (int i = 0; i<sites.length; i++) {
			
		InputStream is = sites[i];	
		PDDocument document = PDDocument.load(is);
		
		PDDocumentInformation info = document.getDocumentInformation();
		
		System.out.println("----------------------------"+i+"-----------------------------");
		
		System.out.println( "Article Title = " + info.getTitle() );
		System.out.println( "Creator = " + info.getCreator() );
		System.out.println( "Authors = " + info.getAuthor() );
		System.out.println( "Creation Date = " + (info.getCreationDate().getTime().getYear() + 1900));
		
		//System.out.println("----------------------------------------------------------");
		
		//is.close();
		document.close();
		}
		
	}
	
	public void initializeSites() throws MalformedURLException, IOException {
		sites = new InputStream[4];
		sites[0] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42509?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%271-s2.0-S1755436517301135-main.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200610T150000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200610%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=8fb59a8ed8f9fb25f041cccca802022c396d5a792fa8b78523d6b683ac2219a4").openStream();
		sites[1] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42550?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%27178-1-53.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200610T150000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200610%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=3d56953441b99dbf52a0530f7f8bfa5eb4dca70037c783bbced67532748fb84a").openStream();
		sites[2] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42552?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%27biology-09-00097.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200610T150000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200610%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=90ab74e89769475b58bc7f3231a707a85b261ecef02d0110e7f8ebf8ceb4e1b1").openStream();
		sites[3] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42528?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%27biology-09-00094.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200610T150000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200610%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=66c5738b1d1b31f4c0b64cc1617bdaa59210727be18c31132f46d21f9583ed1d").openStream();
	}
	
	public static void main (String[]args) {
		try {
			covidSciDiscoveries test = new covidSciDiscoveries();
			test.readPDF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
