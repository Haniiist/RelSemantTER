package Analyseur;

// ajouter la méthode qui trouve les mots composés à l'aide des tableaux de l'objet parser s'ils ne sont pas trouvés
//avec findMC() et appeler cette méthode dans le constructeur

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.*;

import RequeterRezo.RequeterRezo;
import RequeterRezo.Mot;
import RequeterRezo.RequeterRezo.TupleRelationTerme;

public class MotsComposes extends TextClass {
	
	ArrayList<Mot> motsTrouves;
	
	
	public MotsComposes(){
		oldText = new String();
		newText = new String();
		motsTrouves = new ArrayList<Mot>();
	}
	
	public MotsComposes(TextClass p){
		this.oldText = new String(p.newText);	
		motsTrouves = new ArrayList<Mot>();
		this.newText=findMC();
		System.out.println(" Analyser mot composes "+this.newText);

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

	public String findMC () {
		
		String str = new String(oldText);
        String[] s = str.split("\\s");
		int i =0;
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
				int bool=0;
				if (!Arrays.asList(new String[]{" ",".  ",". ",".","La","de la",".","Elle","par","une","un"}).contains(chaine_mots_compose)) {
					Mot compound_word = requeterRezo(chaine_mots_compose);

					if(compound_word !=null){
						bool=1;
						System.out.println(compound_word);
						motsTrouves.add(compound_word);
						String compound_word_underscore = compound_word.getNom().toString();
						compound_word_underscore=compound_word_underscore.replaceAll("\\s", "_");
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
		return str;	
	}

}
	
	/*public static void main(String[] args) throws Exception {
		
		
        	
        	
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
						Mot compound_word = requeterRezo(chaine_mots_compose);

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

}*/

