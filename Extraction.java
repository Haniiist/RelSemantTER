
import org.wikipedia.Wiki;

import java.io.*;
import java.util.*;

public class Extraction {

        public static void main(String[] args) throws Exception {
                // TODO Auto-generated method stub
                Wiki frWiki = new Wiki("fr.wikipedia.org");
        	frWiki.setMaxLag(-1);
	        String path = "/auto_home/msebih/wiki/"; //chemin où seront stockés les dossiers contenant les articles 
	        String title;
	        for (String page : frWiki.getCategoryMembers("Portail:Médecine/Articles liés")) {
	                title=page.replace("/", "|"); //remplace le caractère "/" par "|" dans les titres des articles car cela posait problème lors de la création des fichiers ("/" était interprété comme une indication de chemin)
	                new File(path+title.substring(0, 1)).mkdirs();
	                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
        	        new FileOutputStream(path+title.substring(0,1)+"/"+title+".txt"), "utf-8"))) {
                        	writer.write(frWiki.getPageText(page));
	                }
        	}	
        }

}


