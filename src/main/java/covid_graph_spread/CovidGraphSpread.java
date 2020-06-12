package covid_graph_spread;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.Utils;
import app.Utils.fileInformation;
import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DateType;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;
import pl.edu.icm.cermine.tools.timeout.TimeoutException;

public class CovidGraphSpread {
	
	Utils informations = new Utils();
	
	ArrayList<String> lines = new ArrayList<String>();
	
	public void writeHTML() {
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try {
		    fWriter = new FileWriter("C:\\Users\\katsa\\git\\ES2-2020-EIC2-26\\src\\main\\java\\covid_graph_spread\\webSpreadTable.html");
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
		    		"    <th>File timestamp</th>\r\n" +
		    		"    <th>File Name</th>\r\n" + 
		    		"    <th>File Tag</th>\r\n" + 
		    		"    <th>Tag Description</th>\r\n" + 
		    		"    <th>Spread Visualization Link</th>\r\n" +
		    		"  </tr>\r\n" + 
		    		getLines() +
		    		"</table>\r\n" + 
		    		"</body>\r\n" + 
		    		"</html>");
		    writer.close();
		    
		} catch (Exception e) {
		  //catch any exceptions here
		}
	}
	
	public String getLines() {
		List<fileInformation> files = informations.getInformations();
		String codigoHtml = "";
		for (int i = 0; i<files.size(); i++) {
			String linha = "  <tr>\r\n" + 
	    		   	"<td>" + files.get(i).getTimestamp() +"</td>\r\n" + 
	    		"    <td>"+ files.get(i).getFileName() +"</td>\r\n" + 
	    		"    <td>"+ files.get(i).getTagName() +"</td>\r\n" +
	    		"    <td>"+ files.get(i).getTagDescription() +"</td>\r\n" +  
	    		"    <td> por completar </td>\r\n" + 
	    		"  </tr>\r\n";
			codigoHtml += " " + linha;
		}
		return codigoHtml;
	}
	
	public static void main(String[] args) {
		CovidGraphSpread test = new CovidGraphSpread();
		test.writeHTML();
		System.out.println(test.getLines());
	}

}