package Analyser;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Principale {

	public static void main(String[] args) throws IOException {
		fetchPatrons ("Patterns.txt");
		Analyseur analyseurDeTest=new Analyseur("Text.txt");
		analyseurDeTest.analyser();
		for (Relation relation : analyseurDeTest.getRelations_trouvees()) {
			System.out.println("Relation : "+relation.getType()+"("
											+relation.getTerm1()+","+relation.getTerm2()+")");
		}
	}
	
	public static void fetchPatrons (String filePath) throws IOException{

		BufferedReader buffer = Files.newBufferedReader(Paths.get(filePath), Charset.forName("ISO-8859-1"));
		String tmp;
		String type = null;
		String patron;
		int nbrTerms = 0;
		Pattern ExpRegPatron = Pattern.compile("\\s([A-Za-z\\sé'û$à]+)\\s[$]");
		Pattern ExpRegType = Pattern.compile("([A-Za-z/éàè]+) [:]");
		Pattern ExpRegNbrTerms = Pattern.compile("\\$[A-Za-z]");
		Matcher matcherPatron ;
		Matcher matcherType ;
		Matcher matcherNbrTerms ;
		while ((tmp=buffer.readLine()) != null) {
			matcherPatron = ExpRegPatron.matcher(tmp);
			matcherType = ExpRegType.matcher(tmp);
			matcherNbrTerms = ExpRegNbrTerms.matcher(tmp);
			if (matcherType.find()){
				type=matcherType.group(1);
				System.out.println("Type de relation ajouté : "+type);
				if (!Relation.types_de_relations.contains(type)) {
					Relation.types_de_relations.add(type);
					Relation.typePatrons.put(type, new ArrayList<String>());
				}
			}
			if (matcherPatron.find()){
				nbrTerms = 0;
				patron=matcherPatron.group(1);
				while (matcherNbrTerms.find()) {
					nbrTerms++;
				}
				if (!Relation.types_de_relations.contains(type)) {
					System.err.println("Type de relation inconnu pour le patron : "+patron);
				}
				else if (!Relation.typePatrons.get(type).contains(patron)) {
					patron = patron.replaceAll("\\s\\$[A-Za-z]+\\s", "\\$");
					Relation.typePatrons.get(type).add(patron);
					Relation.patronNbrTerms.put(patron,new Integer (nbrTerms));
				}
				else {
					System.out.println("Patron déjà définit ---> " +patron);
				}
				
			}
		
		}
	
	System.out.println("Types de relations définis: "+Relation.types_de_relations);
	System.out.println("Patrons définis: "+Relation.typePatrons.values());
	
	}
	
	
}
