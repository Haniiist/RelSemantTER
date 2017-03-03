package Analyser;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspWriter;

public class Analyseur {
	String carAccentues="ûâàéèêîô";
	String motFr ="[A-Za-z_"+carAccentues+"']";
	String filePath;
	private String text;	
	private ArrayList<Relation> Relations_trouvees = new ArrayList<Relation>();
	
	public Analyseur (){
		
	}
	public Analyseur(String filePath) throws IOException {
		this.filePath = filePath;
		BufferedReader buffer = Files.newBufferedReader(Paths.get(filePath), Charset.forName("ISO-8859-1"));
		String tmp;
		while ((tmp=buffer.readLine()) != null) {
			text=text+"\\n"+tmp;
		}
	}
	
	public void analyser(){
		this.pretraitement();
		for (String type : Relation.types_de_relations) {
			for (String patron : Relation.typePatrons.get(type)) {
				// Construction de la Regex pour l'extraction des termes
				String strExpReg = "";
				for (int i = 0; i < patron.split("\\$").length; i++) {
					strExpReg+="("+motFr+"+)\\s"
							+patron.split("\\$")[i].replace(" ", "\\s")+"\\s";
				}
				strExpReg+="("+motFr+"+)";
				
				Pattern ExpReg= Pattern.compile(strExpReg);
				Matcher matcher = ExpReg.matcher(this.text);
				while (matcher.find()){
					for (int i = 2; i <= Relation.patronNbrTerms.get(patron); i++) {
						// Test si il n'y a pas confusion entre patrons
						if (unique(type,matcher.group(1),patron,matcher.group(i),matcher.group())) {
							// Test d'ambiguité et désambiguation (par contraintes sémantiques)
							if (!isAmbigu(patron) || type.equals(this.desambiguation(type,patron,matcher.group(1), matcher.group(i)))) {
								System.out.println("Un patron de "+type+" est détecté :"+patron);
								Relations_trouvees.add(new Relation(type, matcher.group(1), matcher.group(i),matcher.group()));
							}
						}
					}
					
				}
			}
		}
	}
	
	private boolean unique(String type, String term1, String patron, String term2, String contexte) {
		/*
		 * Méthode évitant la confusion entre patrons.
		 */
		for (ArrayList<String> Set : Relation.typePatrons.values()) {
			for (String pattern : Set) {
				// Cas 1 : Patron inclut dans un autre --> Interdire la duplicaion. 
				if (!patron.equals(pattern) && this.foundRelation(new Relation(type,term1,term2,contexte) )){
					return false;
				}
				// Cas 2 : [(Terme1+Patron) ou (Patron+Terme2)] est un patron --> Interdire la création d'une relation. 
				if (pattern.startsWith(patron+" "+term2) || pattern.startsWith(term1+" "+patron)){
					return false;
				}
			}
		}
		return true;
	}
	private String desambiguation(String inputType, String patron, String term1, String term2) {
		/* 
		 * C'est ici que seront les contraintes sémantiques.
		 * Doit retourner le type de relation.
		 * Utilise l'API de jeuxdemots pour vérifier les contraintes sur les termes. 
		 * 
		 * */
		String type = inputType;
		if (patron.equals("a des")) {
			// Simulation de contraintes sémantiques sur le patron "a des" (A titre d'exemple).
			if (term1.equals("lapin")) {
				type = "Holonymie";
			}
			
			if (term1.equals("fille")) {
				type = "Possession";
			}
		}
		return type;
		
		
		
	}
	
	private boolean isAmbigu(String patron) {
		/*
		 * Liste de patrons qui créent une ambiguité / prêtent à confusion.
		 */
		if (Arrays.asList(new String[] {"a des"}).contains(patron)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public void pretraitement(){
		/*
		 * Prétraitements. 
		 */
		this.parser();
		this.lemmatisation();
		this.mots_composes();
	}
	
	
	public void parser(){
		/*
		 * Nettoyage du contenu téléchargé (HTML ou autre). 
		 */
	}
	
	
	public void mots_composes(){
		/*
		 * Remplacement des espaces par des underscores. 
		 */
	}
	
	
	public void lemmatisation(){
		/*
		 * Mise des verbes conjugués à l'infinitif... 	
		 */
	}
	
	//Getters
	public ArrayList<Relation> getRelations_trouvees() {
		return Relations_trouvees;
	}
	
	//Setters
	public void setText(String text) {
		this.text = text;
	}
	
	
	public boolean foundRelation(Relation relation){
		/*
		 * Vérifie si une relation a déjà été trouvée. 
		 */
		for (Relation relation_trouvee: Relations_trouvees) {
			if (relation_trouvee.equals(relation)){
					return true ; 
				}
		}
		return false;
		
	}
	
	public void displayResults(JspWriter out) throws IOException{
		/*
		 * Affiche la liste des relations trouvées. 
		 */
		out.println("Relations extraites :<br><br>");
		for (Relation relation : this.getRelations_trouvees()) {
			out.println("-"+relation.getType()+"("
										+relation.getTerm1()+","+relation.getTerm2()+")<br>");////Contexte : "+relation.getContexte()+"<br><br>");
			}
	}
}
	