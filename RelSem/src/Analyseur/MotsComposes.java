package Analyseur;

// ajouter la mÃ©thode qui trouve les mots composÃ©s Ã  l'aide des tableaux de l'objet parser s'ils ne sont pas trouvÃ©s
//avec findMC() et appeler cette mÃ©thode dans le constructeur

import java.io.*;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

import RequeterRezo.RequeterRezo;
import RequeterRezo.Mot;
import RequeterRezo.RequeterRezo.TupleRelationTerme;

public class MotsComposes extends TextClass {

	ArrayList<String> motsTrouves;
	HashSet<String> wordList;




	public MotsComposes() throws IOException{
		oldText = new String();
		newText = new String();
		motsTrouves = new ArrayList<String>();
		wordList = new HashSet<String>();
		createWordList("jdm-mc.txt");

	}

	public MotsComposes(TextClass p) throws IOException{
		this.oldText = new String(p.newText);	
		wordList = new HashSet<String>();
		createWordList("jdm-mc.txt");
		motsTrouves = new ArrayList<String>();
		this.newText=findMC();
		//System.out.println(" Analyser mot composes "+this.newText);


	}
    
	public void createWordList (String filePath) throws IOException{

		    String line;
		    BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
		    while ((line = reader.readLine()) != null)
		    {
		        wordList.add(line.substring(0, line.length()-1));
		    }
	}
	
	public static Mot requeterRezo(String s){

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

	public Boolean lookUp(String word) throws FileNotFoundException{
		return (wordList.contains(word));
	}

	public String findMC () throws FileNotFoundException {

		String str = (new String(oldText));
		String[] avoid = {"du","la","des","les","un","une","par","elle","il","mais","ou","est","et","donc","or","ni","car",
				"","_","de"," ","à"};
		String[] phrase = str.split("[.|;|,|==|\\n|?|!|:|(|)|\\[|\\]|«|»|“|”|\"]+");
		for(int j=0;j<phrase.length;j++){
			int i =0;
			int decalage = 8;
			String[] s = phrase[j].toString().split("(\\s)+");
			Boolean found = false;
			while(s.length>i){
				String chaine_mots_compose = new String();
				while((s.length>i) && 
						Arrays.asList(avoid).contains(s[i].toLowerCase())){
					i++;
				}
				int fenetre =i;
				int k=0;
				while(k<decalage  && k+fenetre<s.length){
					if(!s[k+fenetre].equals("")){
						chaine_mots_compose= chaine_mots_compose+s[k+fenetre]+" ";
					}
					k++;
				}
				if(chaine_mots_compose.length()>0)
					chaine_mots_compose = chaine_mots_compose.substring(0, chaine_mots_compose.length()-1);
				if(motsTrouves.contains(chaine_mots_compose)){
					i = i+(chaine_mots_compose.split("\\s")).length;
					decalage=8;
					String compound_word_underscore = new String(chaine_mots_compose);
					compound_word_underscore=compound_word_underscore.replaceAll("\\s", "_");
				}
				else{
					found = lookUp(chaine_mots_compose);
					if(found){
						motsTrouves.add(chaine_mots_compose);
						String compound_word_underscore = new String(chaine_mots_compose);
						for (String b : motsTrouves){
							if (chaine_mots_compose.contains(b) && chaine_mots_compose.length()>b.length() ){
								String c = b.replace(" ","_");
								chaine_mots_compose = chaine_mots_compose.replace(b,c);
							}
						}
						i = i+(chaine_mots_compose.split("\\s")).length;
						decalage=8;
						compound_word_underscore=compound_word_underscore.replaceAll("\\s", "_");
						str = str.replace(chaine_mots_compose, compound_word_underscore);
					}  
					else if(decalage==2){
						i++;
						decalage=8;
					}
					else 
						decalage--;
				}
			}
		}
	      for(String s5 : motsTrouves)
	    	  System.out.println("***************"+s5);
		return str;	
	}
	public static void main(String[] args) throws Exception {
      System.out.println((new MotsComposes()).lookUp("radiographie du thorax"));
	}
}