package org.jabref.gui;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class FinderManager {
	
	private String title;
	private String searched_for = "";
	private String parseurl = "";
	private Document html_doc;
	private Elements gs_article_results;
	private HashMap<String, String> csvDataBase;
	
	public FinderManager(String title) throws IOException {
		System.out.println(title);
		this.title = title;
		this.searched_for = title.replaceAll(" ", "+");
		this.parseurl = "http://scholar.google.co.uk/scholar?hl=en&q="+searched_for;
		//this.parseurl = "http://scholar.google.co.uk/scholar?hl=en&q="+searched_for;
		this.html_doc = Jsoup.connect(parseurl).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").referrer("http://www.google.com").get();
		this.csvDataBase = new HashMap<>();
		getCSVDatabase();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private void getCSVDatabase() {
		Path currentRelativePath = Paths.get("");
	    String currentPath = currentRelativePath.toAbsolutePath().toString();
	    String csvFile = currentPath + "/factor/snip_proceedings.csv";

	    Reader in;
	    Iterable<CSVRecord> records = null;
	    try
	    {
	        in = new FileReader(csvFile);
	        records = CSVFormat.EXCEL.withHeader().parse(in); // header will be ignored
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }

	    for (CSVRecord record : records) {
	        String line = "";
	        for ( int i=0; i < record.size(); i++)
	        {
	            if ( line == "" )
	                line = line.concat(record.get(i));
	            else
	                line = line.concat("," + record.get(i));
	        }
	        String[] vet = line.split(";");
	        //System.out.println("Title: " + vet[1] + "Impact factor: " + vet[51]);
	        if(!vet[51].isEmpty())
	        	this.csvDataBase.put(vet[1].toLowerCase(), vet[51]);
	        //System.out.println("read line: " + line);
	    }
	}

	public String searchForImpactFactor() {
		String value = csvDataBase.get(title.toLowerCase());
		if(value == null)
			return "";
		else
			return value;
	}
	
	public String searchForCitations() {
		gs_article_results = html_doc.select("#gs_ccl .gs_r .gs_ri");
		String citations = "";
		
		if(!gs_article_results.isEmpty()) {
			String title = gs_article_results.get(0).select(".gs_rt a").text();
		
			System.out.println("title returned: " + title);
			System.out.println("original title : " + this.title);
			
			if(title.toLowerCase().equals(this.title.toLowerCase()))
				citations = gs_article_results.get(0).select(".gs_fl a").text().split(" ")[2];
			return citations;
		}
		return citations;	
	}

}
