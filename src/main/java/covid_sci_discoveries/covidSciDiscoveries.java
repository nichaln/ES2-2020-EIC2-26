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
	
	public static void readPDF() throws IOException {
		
		InputStream is = new URL ("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42509?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%271-s2.0-S1755436517301135-main.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200610T150000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200610%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=8fb59a8ed8f9fb25f041cccca802022c396d5a792fa8b78523d6b683ac2219a4").openStream();
		
		PDDocument document = PDDocument.load(is);
		
		PDDocumentInformation info = document.getDocumentInformation();
		
		System.out.println( "Article Title = " + info.getTitle() );
		System.out.println( "Creator = " + info.getCreator() );
		System.out.println( "Authors = " + info.getAuthor() );
		System.out.println( "Creation Date = " + (info.getCreationDate().getTime().getYear() + 1900));
		
		System.out.println("--------------------------------------------------------_");
		
		
		
		
		is.close();
		
	}
	
	public static void main (String[]args) {
		try {
			readPDF();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
