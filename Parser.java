import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser {
	String text;
	ArrayList<String> linksWiki;
	ArrayList<String> bold;
	ArrayList<String> italic;
	ArrayList<String> boldItalic;
	ArrayList<String> deleted;
	ArrayList<String> sectionTitles;
	ArrayList<String> sectionTexts;
	
	public Parser(){
		text = new String();
		linksWiki = new ArrayList<String>();
		bold = new ArrayList<String>();
		italic = new ArrayList<String>();
		boldItalic = new ArrayList<String>();
		deleted = new ArrayList<String>();
		sectionTitles = new ArrayList<String>();
		sectionTexts = new ArrayList<String>();
	}

	
	public static void main(String[] args) throws Exception {
		
		File file = new File("/home/smehdi254/Desktop/Kraya/TER/Articles/S/Syndrome 3C.txt");
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		String str = new String(data, "UTF-8");
		
		Parser c = new Parser();
		
		Pattern pattern = Pattern.compile("\\{\\{((.)+?)\\}\\}");

	        Matcher matcher = pattern.matcher(str);
	        while (matcher.find()) {
	            String group = matcher.group(0);
            		c.deleted.add(group);
	        }
	        str=matcher.replaceAll("");
	        
        	pattern = Pattern.compile("\\{\\{((.|\n)+?)\\}\\}|\\[\\[Catégorie:((.)+?)\\]\\]|\\[http((.)+?)\\]");
	        matcher = pattern.matcher(str);
	        while (matcher.find()) {
	            String group = matcher.group(0);
	            c.deleted.add(group);
	        }
	        str=matcher.replaceAll("");
	        
	        pattern = Pattern.compile("\\[\\[((.)+?)\\]\\]");
	        matcher = pattern.matcher(str);
        	while (matcher.find()) {
	            String group = matcher.group(1);
	            c.linksWiki.add(group);
	        }
	        str=str.replaceAll("\\[\\[","");
	        str=str.replaceAll("\\]\\]","");
        
        	pattern = Pattern.compile("''((.)+)''");
        	matcher = pattern.matcher(str);
	        while (matcher.find()) {
	            String group = matcher.group(1);
	            if (group.charAt(0)!='\'') {if (group.length()>1) c.italic.add(group);}
	            else if (group.charAt(1)!='\'') {group = group.substring(1, group.length()-1); if (group.length()>1) c.bold.add(group);}
	            else {group = group.substring(3,group.length()-3); if (group.length()>1) c.boldItalic.add(group);}
	        }
        	str=str.replaceAll("('){2,}","");
        
	        pattern = Pattern.compile("==((.)+)==");
	        matcher = pattern.matcher(str);
        	while (matcher.find()) {
	        	String group = matcher.group(1).trim();
	            c.sectionTitles.add(group);
	        }
	        
        	pattern = Pattern.compile("((.|\n)+?)\n==");
	        matcher = pattern.matcher(str);
        	while (matcher.find()) {
	        	String group = matcher.group(1).trim();
	            c.sectionTexts.add(group);
	        }
	        
	        str=str.replaceAll("(\\h){2,}"," ");
	        str=str.replaceAll("(\n){3,}","\n\n");
	        str=str.trim();

	}

}

