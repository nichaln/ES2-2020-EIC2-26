package covid_sci_discoveries;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;
import pl.edu.icm.cermine.metadata.model.DateType;
import pl.edu.icm.cermine.metadata.model.DocumentAuthor;
import pl.edu.icm.cermine.metadata.model.DocumentMetadata;

public class CovidSciDiscoveries {
	
	private String pathPDF; //localizacao dos documentos PDF;
	private String pathHTML; //localizacao do ficheiro HTML;

	//Construtor
	public CovidSciDiscoveries(String pathPDF, String pathHTML) {
		this.pathPDF = pathPDF;
		this.pathHTML = pathHTML;
	}
	//Metodo que devia correr quando alguem atualiza a diretoria
	private void run() { //se ja tiver ficheiro HTML
		File fileHTML = new File(pathHTML);
		if(fileHTML.exists() && !fileHTML.isDirectory()) {
			List<URL> listURL = listDocumentsURL();//URLs presentes no diretorio 
			List<URL> htmlURL = listHtmlURL();//URLs presentes no HTML
			List<URL> addListURL = new ArrayList<URL>();//URLs que devem ser adicionados
			List<URL> removeListURL = new ArrayList<URL>();//URLS que devem ser removidos

			for(URL u : listURL) { //percorer os documentos da diretoria e ver se nao falta nehum no html
				if(!htmlURL.contains(u))
					addListURL.add(u);
			}
			
			for(URL u : htmlURL) {//percrer o html e ver os documentos e ver se algum esta a mais que foi removido da diretoria
				if(!listURL.contains(u))
					removeListURL.add(u);
			}
			
			if(!addListURL.isEmpty())
				addURLs(addListURL);//adicionar URLs em falta ao ficheiro HTML
			if(!removeListURL.isEmpty())
				removeURLs(removeListURL);//retirar URLs a mais que estao no ficheiro HTML
			System.out.println("ficheiro atualizado!!!");
			
		}
		else {
			writeHTML();
			System.out.println("ficheiro escrevido");
		}
	}
	
	//Metodo para listar os documentos PDF que se encontram no caminho path 
	//(String path) especifica onde os documentos PDF estao guardados
	private List<URL> listDocumentsURL() {
		List<URL> listURL = new ArrayList<URL>();//lista que guarda os URL de todos os documentos pdf
		File directory = new File(pathPDF); // especificacao do path
			File[] fList = directory.listFiles();	
			for (File file : fList) {
				if (file.isFile()) {
					if (file.getName().endsWith(".pdf")) {//se o ficheiro for um documento PDF
						try {
							URL url;
							url = file.getAbsoluteFile().toURI().toURL();//obtencao do url
							listURL.add(url); //adicao do URL a lista
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			return listURL;
	}
		
	//Metodo para listar os documentos PDF que se encontram num fiehrio HTML, utilizando o JSOUP
	private List<URL> listHtmlURL() {
		List<URL> htmlURL = new ArrayList<URL>();//lista que guarda os URLs dos documentos do HTML
			
		File input = new File(pathHTML);
		try {
			Document htmlFile = Jsoup.parse(input, "UTF-8");
			Elements listA = htmlFile.select("a");
			for (Element a : listA) {
				htmlURL.add(new URL(a.attr("href")));
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return htmlURL;
	}
		
	//Metodo auxiliar ao run
	private void addURLs(List<URL> addListURL) {
		File input = new File(pathHTML);
		try {
			Document htmlFile = Jsoup.parse(input, "UTF-8");
			Element table = htmlFile.select("table").first();
			for(URL u : addListURL) {
				table.select("tbody").last().append(generateLine(u));
			}
			FileUtils.writeStringToFile(input,htmlFile.outerHtml(), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	//Metodo auxiliar ao run
	private void removeURLs(List<URL> removeListURL) {
		File input = new File(pathHTML);
		try {
			Document htmlFile = Jsoup.parse(input, "UTF-8");
			Element table = htmlFile.select("table").first();
			for(URL u : removeListURL) {
				table.select("a[href="+u+"]").first().parentNode().parentNode().remove();
			}
			FileUtils.writeStringToFile(input,htmlFile.outerHtml(), "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	//Metodo que gera o codigo HTML todo para no ficheiro webTable.html
	//(String path) especifica onde o ficheiro HTML vai ficar guardado e o nome do mesmo
	private void writeHTML() {
		try {
			FileWriter fWriter = new FileWriter(new File(pathHTML));
			BufferedWriter writer = new BufferedWriter(fWriter);
		
			writer.write(
				"<!DOCTYPE html>\r\n" + 
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
			    "<tr>\r\n" + 
			    "<th>Article Title</th>\r\n" + 
			    "<th>Journal Name</th>\r\n" + 
			    "<th>Publication Year</th>\r\n" + 
			    "<th>Authors</th>\r\n" + 
			    "</tr>\r\n" + 
			    writeLinesHTML() + 
			    "</table>\r\n" + 
			    "</body>\r\n" + 
			    "</html>");
			writer.close();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	//Metodo auxiliar writeHTML para escrever todas as linhas no HTML como uma string
	private String writeLinesHTML() {
		List<URL> listURL = listDocumentsURL();
		String s = "";
		for(int i = 0;i<listURL.size();i++) {
			s+=generateLine(listURL.get(i));
		}
		return s;
	}
	
	//Metodo para escrever uma linha no HTML usando o CERMINE
	private String generateLine(URL url)  {
		String resultString = "Problema ao extrair informacao do URL:"+url.toString()+";";
		try {
			InputStream input = url.openStream();
			ContentExtractor extractor = new ContentExtractor();
			extractor.setPDF(input);//extracao dos dados do documento PDF
			DocumentMetadata result = extractor.getMetadata();//Title,Journal,Date
			List<String> autorLine = generateAutorLine(result.getAuthors());//lista com todos os nomes de autores
			resultString = 
							"<tr>\r\n" + 
			   				"<td>" +
			   				"<a href="+url.toString()+">" + 
			   				result.getTitle() +
			   				"</a>" +
			   				"</td>\r\n" + 
			   				//"<div id="+listURL.get(i)+">" +
			   				"<td>" + result.getJournal() +"</td>\r\n" + 
			   				"<td>" + result.getDate(DateType.PUBLISHED).getYear() +"</td>\r\n" +
			   				"<td>" + autorLine.toString() +"</td>\r\n" +  
			   				"</tr>\r\n";
			   				
		} catch (AnalysisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultString;
	}
		
	//Metodo auxiliar ao getLine para escrever o nome de todos os autores
	private List<String> generateAutorLine(List<DocumentAuthor> autors) {
		List<String> autorNames = new ArrayList<String>();
		for(DocumentAuthor autor:autors) {
			autorNames.add(autor.getName());
		}
		return autorNames;
	}	

	public static void main (String[]args) {
		
	
		CovidSciDiscoveries test = new CovidSciDiscoveries("\\C:\\Users\\L3g4c\\Desktop\\pdfs","C:\\Users\\L3g4c\\git\\ES2-2020-EIC2-26\\Parte3HTML.html");
		test.writeHTML();
		System.out.println("HTML foi escrito com sucesso");
		
		while(true) {
			try {
				System.out.println("running ...");
				Thread.sleep(20000);
				test.run();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		CovidSciDiscoveries test = new CovidSciDiscoveries(System.getProperty("user.home") + "/Desktop",System.getProperty("user.home") + "/Desktop");
		test.run();
		**/
	}
	
}
