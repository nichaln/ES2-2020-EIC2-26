package covid_sci_discoveries;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

import com.itextpdf.text.Element;

import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DateType;
import pl.edu.icm.cermine.metadata.model.DocumentAuthor;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;
import pl.edu.icm.cermine.tools.timeout.TimeoutException;

public class covidSciDiscoveries {
	
	private URL[] sites;
	
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
	
	/*Metodo para escrever uma linha na tabela html da maneira antiga
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
		}*/
	
	/*FUNCIONA!
	public void extractWithCermine() throws MalformedURLException, IOException, AnalysisException {
		initializeSites();
		for (int i = 0; i<sites.length; i++) {
			ContentExtractor extractor = new ContentExtractor();
			InputStream is = sites[i];	
			extractor.setPDF(is);
			DocumentMetadata result = extractor.getMetadata();
			System.out.println("Article Tile: " + result.getTitle());
			System.out.println( "Journal Name: " + result.getJournal());
			System.out.print( "Authors: ");
			List<DocumentAuthor> autors = result.getAuthors();
			for (int j = 0; j<autors.size(); j++) {
				if (j<autors.size()-1)
					System.out.print(autors.get(j).getName() + " , ");
				else {
					System.out.print(autors.get(j).getName() + ".");
					System.out.println();}
			}
			System.out.println( "Publication Year: " + (result.getDate(DateType.PUBLISHED).getYear()));
			System.out.println("_______________________________________________________");
		}
	}*/
	
	//Metodo para escrever linhas no html usando o CERMINE
	public String getLine(int i) throws IOException, TimeoutException, AnalysisException {
		initializeSites();	
		ContentExtractor extractor = new ContentExtractor();
		URL urls = sites[i];
		InputStream is = urls.openStream();
		extractor.setPDF(is);
		DocumentMetadata result = extractor.getMetadata();
		ArrayList<String> autorLine = createAutorLine(result.getAuthors());
		return "  <tr>\r\n" + 
	    		   	"<td>" +
	    		 		"<a href="+sites[i].toString()+">" + 
	    		 			result.getTitle() +
	    		 		"</a>" +
	    		 	"</td>\r\n" + 
	    		"    <td>"+ result.getJournal() +"</td>\r\n" + 
	    		"    <td>"+ result.getDate(DateType.PUBLISHED).getYear() +"</td>\r\n" +
	    		"    <td>"+ autorLine.toString() +"</td>\r\n" +  
	    		"  </tr>\r\n";
	}
	
	
	//Necessário para escrever o nome de todos os autores no getLine
	public ArrayList<String> createAutorLine(List<DocumentAuthor> autors) {
		ArrayList<String> autorNames = new ArrayList<String>();
		for (int i = 0; i<autors.size(); i++) {
			autorNames.add(autors.get(i).getName());
		}
		return autorNames;
	}	
	
	//Metodo para inicializar os documentos pdf
	public void initializeSites() throws MalformedURLException, IOException {
		sites = new URL[4];
		sites[0] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42509?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%271-s2.0-S1755436517301135-main.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200611T120000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200611%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=4057a1332e5a205735a590e11cab988ea7d5a932d6e3cd127ba7933129f67deb");
		sites[1] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42528?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%27biology-09-00094.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200611T120000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200611%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=e8188cd2d6f6901259fc607a6bee254d0eef0c235e8301bf826800ad8e259227");
		sites[2] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42550?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%27178-1-53.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200611T120000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200611%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=e5db745a478b5dd90dbfa2e2da8c82ee270f5427700299ae1bb7beb0e3aea3bd");
		sites[3] = new URL("https://learn-eu-central-1-prod-fleet01-xythos.s3.eu-central-1.amazonaws.com/5eb046c2a3d01/42552?response-cache-control=private%2C%20max-age%3D21600&response-content-disposition=inline%3B%20filename%2A%3DUTF-8%27%27biology-09-00097.pdf&response-content-type=application%2Fpdf&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200611T120000Z&X-Amz-SignedHeaders=host&X-Amz-Expires=21600&X-Amz-Credential=AKIAZH6WM4PLYI3L4QWN%2F20200611%2Feu-central-1%2Fs3%2Faws4_request&X-Amz-Signature=6bdfe65dbdcf4c604f6440b489e9487ff167c3eef1e22252a134a78089472c70");
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
		//test.extractWithCermine();
		test.writeHTML();
	}
	

}
