

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.*;

import RequeterRezo.RequeterRezo;
import RequeterRezo.Mot;
import RequeterRezo.RequeterRezo.TupleRelationTerme;

public class Parser {
	String text;
	ArrayList<String> linksWiki;
	ArrayList<String> bold;
	ArrayList<String> italic;
	ArrayList<String> boldItalic;
	ArrayList<String> deleted;
	ArrayList<String> sectionTitles;
	ArrayList<String> sectionTexts;
	ArrayList<Mot>    motsCompose;
	
	public Parser(){
		text = new String();
		linksWiki = new ArrayList<String>();
		bold = new ArrayList<String>();
		italic = new ArrayList<String>();
		boldItalic = new ArrayList<String>();
		deleted = new ArrayList<String>();
		sectionTitles = new ArrayList<String>();
		sectionTexts = new ArrayList<String>();
		motsCompose = new ArrayList<Mot>();
	}
	
public static Mot RequterRezo(String s){
		
		RequeterRezo r = new RequeterRezo();
		Mot m = null;
		try {
			m = r.requete(s);
			//System.out.println(m);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
		
	}

	
	public static void main(String[] args) throws Exception {
		
		File file = new File("D:/TER/Articles/Articles/R/Radiomucite.txt");
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		String str = new String(data, "UTF-8");
		
		Parser c = new Parser();
		
		Pattern pattern = Pattern.compile("\\{\\{Lang\\|\\w\\w\\|((.)+?)\\}\\}");
        	Matcher matcher = pattern.matcher(str);
        	while (matcher.find()) {
	        	String group = matcher.group(0);
	        	String group1 = matcher.group(1);
	        	str=str.replace(group, group1);
	        	group=group.replace(group1, "");
	            c.deleted.add(group);
	        }
        
	        pattern = Pattern.compile("\\{\\{\\w\\w\\}\\}");
	        matcher = pattern.matcher(str);
	        while (matcher.find()) {
	            String group = matcher.group(0);
	            c.deleted.add(group);
        	}
        	str=matcher.replaceAll("");
	        
	        pattern = Pattern.compile("\\{\\{(.)+?\\}\\}");
	        matcher = pattern.matcher(str);
	        while (matcher.find()) {
	            String group = matcher.group(0);
	            c.deleted.add(group);
	        }
	        str=matcher.replaceAll("");
	        
        	pattern = Pattern.compile("<ref([^>])+?\\/>");
	        matcher = pattern.matcher(str);
	        while (matcher.find()) {
	            String group = matcher.group(0);
	            c.deleted.add(group);
	        }
	        str=matcher.replaceAll("");
	        
        
	        pattern = Pattern.compile("(<ref(.|\n)*?\\/ref>)+?|\\[\\[Catégorie:((.)+?)\\]\\]|\\[\\[Fichier:((.)+?)\\]\\]\n|\\[http((.)+?)\\]|<!--(.)?-->");
        	matcher = pattern.matcher(str);
	        while (matcher.find()) {
	            String group = matcher.group(0);
	            c.deleted.add(group);
	        }
	        str=matcher.replaceAll("");
        
	        pattern = Pattern.compile("\\[\\[([^|]+?)\\]\\]");
	        matcher = pattern.matcher(str);
	        while (matcher.find()) {
	        	String group = matcher.group(0);
	        	String group1 = matcher.group(1);
	        	str=str.replace(group, group1);
	            	c.linksWiki.add(group1);            
        	}
        
        	pattern = Pattern.compile("\\[\\[((.)+?\\|((.)+?))\\]\\]");
        	matcher = pattern.matcher(str);
        	while (matcher.find()) {
	        	String group = matcher.group(0);
        		String group1 = matcher.group(3);
	        	str=str.replace(group, group1);
	        	group=group.replace(group1, "");
	                c.deleted.add(group);
	        	c.linksWiki.add(group1);
        	}
        
        
        	str=str.replaceAll("<br(\\s)*?/>","\n");
        
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
        	str=str.replace("."," .");
        	str=str.replace(","," ,");

        	str=str.trim();
        	String s1 = "" ;
	        s1= s1+(str);
	        String[] s = s1.split("\\s");
			int i =0;
			//System.out.println("---------------------"+s1+"--------------------------");


			int decalage = 6;
				while(s.length>i){
					String chaine_mots_compose = "";
					int fenetre =i;
					int k=0;
					while(k<decalage && !s[k].equals("") && k+fenetre<s.length){
						
						if(chaine_mots_compose.equals("")) 
							chaine_mots_compose = s[k+fenetre];
						else 
							chaine_mots_compose= chaine_mots_compose+" "+s[k+fenetre];
						k++;	
					}
					System.out.println("---------------------"+chaine_mots_compose+"--------------------------");
					int bool=0;
					if (!Arrays.asList(new String[]{" ",".  ",". ",".","La","de la",".","Elle"}).contains(chaine_mots_compose)) {
						Mot compound_word = RequterRezo(chaine_mots_compose);

						if(compound_word !=null){
							bool=1;
							c.motsCompose.add(compound_word );
							String compound_word_underscore = compound_word.getNom().toString();
							System.out.println("**********"+compound_word_underscore+"*************");

							compound_word_underscore=compound_word_underscore.replaceAll("\\s", "_");
							System.out.println("**********"+compound_word_underscore+"*************");
							str = str.replace(chaine_mots_compose, compound_word_underscore);
						}
	                    
					}	
					if(bool ==1 || decalage==2){
                    	i++;
                    	decalage=6;
                    }
                    else 
                    	decalage--;

				}
				String s2 = ""+s1;
				ArrayList<String> copyMots = new ArrayList<String>();
				for (int a=0;a<c.motsCompose.size();a++){
					String chaine  =c.motsCompose.get(a).getNom().toString();
					chaine=chaine.replaceAll("\\s", "_");
					System.out.println("**********"+chaine+"*************");
				}
				
				//System.out.println("---------------------"+str+"--------------------------");
	}

}

