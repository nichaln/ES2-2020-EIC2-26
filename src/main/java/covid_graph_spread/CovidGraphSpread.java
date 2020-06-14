package covid_graph_spread;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import app.Utils;
import app.Utils.fileInformation;
/**
 * 
 * @author hapgr@iscte-iul.pt
 * 
 * */

public class CovidGraphSpread {
	
	Utils fileInformations;
	
	/**
	 * Escreve o html da tabela num ficheiro
	 */
	public void writeHTML() {
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try {
		    fWriter = new FileWriter(System.getProperty("user.home")+"\\Wordpress\\html\\wp-admin\\webSpreadTable.html");
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
		    		"td {text-align: center;}\r\n"+
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
	
	/**
	 * Método para ir buscar todas as linhas que serão adicionadas à tabela dependendo dos ficheiros existentes no repositorio git
	 * @return String das linhas da tabela
	 */
	public String getLines() {
		fileInformations = new Utils();
		List<fileInformation> files = fileInformations.getInformations();
		String codigoHtml = "";
		for (int i = 0; i<files.size(); i++) {
			String[] tagNameParse = files.get(i).getTagName().split("/");
			String linhaDaTabela = "  <tr>\r\n" + 
	    		   	"<td>" + files.get(i).getTimestamp() +"</td>\r\n" + 
	    		"    <td>"+ files.get(i).getFileName() +"</td>\r\n" + 
	    		"    <td>"+ tagNameParse[tagNameParse.length - 1] +"</td>\r\n" +
	    		"    <td>"+ files.get(i).getTagDescription() +"</td>\r\n" +  
	    		"    <td>"+
	    				"<a href=http://visualdataweb.de/webvowl/#iri=https://github.com/vbasto-iscte/ESII1920/raw/"+tagNameParse[tagNameParse.length - 1]+"/covid19spreading.rdf>" + 
	    					"Vizualize data"+
	    				"</a>" +
	    			"</tr>\r\n";
			codigoHtml += " " + linhaDaTabela;
		}
		return codigoHtml;
	}
	
	public static void main(String[] args) {
		CovidGraphSpread test = new CovidGraphSpread();
		test.writeHTML();
		System.out.println("Já escrevi o HTML CovidGraphSpread");
	}

}