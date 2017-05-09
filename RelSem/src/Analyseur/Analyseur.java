package Analyseur;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Analyseur {
	String carAccentues=",àâäçèéêëîïôöùûüÀÂÄÇÈÉÊËÎÏÔÖÙÛÜ\\-";
	String motFr ="[A-Za-z0-9_"+carAccentues+"']";
	String filePath;
	private String text;	
	private ArrayList<Relation> Relations_trouvees = new ArrayList<Relation>();
	Parser p;
	Lemmatisation lm;
	MotsComposes mc;

	
	public Analyseur (){
		
	}
	public Analyseur(String filePath) throws IOException {
		this.filePath = filePath;
		BufferedReader buffer = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
		String tmp;
		text = new String();
		while ((tmp=buffer.readLine()) != null) {
			text=text+"\n"+tmp;
		}
		
	}
	
	public void analyser() throws IOException{
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
							// Test d'ambiguitï¿½ et dï¿½sambiguation (par contraintes sï¿½mantiques)
							if (!isAmbigu(patron) || type.equals(this.desambiguation(type,patron,matcher.group(1), matcher.group(i)))) {
								/*System.out.println("Un patron de "+type+" est détecté :"+patron);
								System.out.println("1 "+type+" est détecté :"+matcher.group(1));
								System.out.println("i "+type+" est détecté :"+matcher.group(i));
								System.out.println("0"+type+" est détecté :"+matcher.group());*/
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
		 * Mï¿½thode ï¿½vitant la confusion entre patrons.
		 */
		for (ArrayList<String> Set : Relation.typePatrons.values()) {
			for (String pattern : Set) {
				// Cas 1 : Patron inclut dans un autre --> Interdire la duplicaion. 
				if (!patron.equals(pattern) && this.foundRelation(new Relation(type,term1,term2,contexte) )){
					return false;
				}
				// Cas 2 : [(Terme1+Patron) ou (Patron+Terme2)] est un patron --> Interdire la crï¿½ation d'une relation. 
				if (pattern.startsWith(patron+" "+term2) || pattern.startsWith(term1+" "+patron)){
					return false;
				}
			}
		}
		return true;
	}
	private String desambiguation(String inputType, String patron, String term1, String term2) {
		/* 
		 * C'est ici que seront les contraintes sï¿½mantiques.
		 * Doit retourner le type de relation.
		 * Utilise l'API de jeuxdemots pour vï¿½rifier les contraintes sur les termes. 
		 * 
		 * */
		String type = inputType;
		if (patron.equals("a des")) {
			// Simulation de contraintes sï¿½mantiques sur le patron "a des" (A titre d'exemple).
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
		 * Liste de patrons qui crï¿½ent une ambiguitï¿½ / prï¿½tent ï¿½ confusion.
		 */
		if (Arrays.asList(new String[] {"a des"}).contains(patron)) {
			return true;
		}
		else {
			return false;
		}
	}

	
	public void pretraitement() throws IOException{
		/*
		 * PrÃ©traitements. 
		 */
		this.parser();
		//this.mots_composes(p);
		//this.lemmatisation(p);
		this.mots_composes(p);
		System.out.println(" mots composes "+mc.newText);
		this.text = new String(mc.newText);


	}
	
	
	public void parser(){
		/*
		 * Nettoyage du contenu tÃ©lÃ©chargÃ© (HTML ou autre). 
		 */
		
		/*
		 * Probleme a rï¿½gler : le texte commence par null/n, essayer de l'enlver du texte
		 */
		p = new Parser(text);
		//System.out.println(" Analyser Parser "+p.newText);

	}
	
	
	public void mots_composes(TextClass TIp) throws IOException{
		/*
		 * Remplacement des espaces par des underscores. 
		 */
		
		mc = new MotsComposes(TIp);
	}
	
	
	public void lemmatisation(TextClass TIp){
		/*
		 * Mise des verbes conjuguÃ©s Ã  l'infinitif... 	
		 */
		
		lm = new Lemmatisation(TIp);
		//System.out.println(" Analyser Lemmatisation "+lm.newText);

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
		 * VÃ©rifie si une relation a dÃ©jÃ  Ã©tÃ© trouvÃ©e. 
		 */
		for (Relation relation_trouvee: Relations_trouvees) {
			if (relation_trouvee.equals(relation)){
					return true ; 
				}
		}
		return false;
		
	}
	
	public void displayResults() throws IOException{
		/*
		 * Affiche la liste des relations trouvÃ©es. 
		 */
		System.out.println("Relations extraites :<br><br>");
		for (Relation relation : this.getRelations_trouvees()) {
			System.out.println("-"+relation.getType()+"("
										+relation.getTerm1()+","+relation.getTerm2()+")<br>");////Contexte : "+relation.getContexte()+"<br><br>");
			}
	}
}
	