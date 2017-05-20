package Analyseur;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Principale {

	public static void main(String[] args) throws IOException {
		fetchPatrons ("Patterns.txt");
		Analyseur analyseurDeTest=new Analyseur("D:\\TER\\Articles\\Articles\\D\\Dépression (psychiatrie).txt");
		//Analyseur analyseurDeTest=new Analyseur("Text.txt");
		analyseurDeTest.analyserParMc();
		for (Relation relation : analyseurDeTest.getRelations_trouvees()) {
			System.out.println("Relation : "+relation.getType()+"("
											+relation.getTerm1()+","+relation.getTerm2()+")");
		}
		/*fetchPatrons ("Patterns.txt");
		Analyseur analyseurDeTest=new Analyseur("/auto_home/msebih/Articles/D/Dépression (psychiatrie).txt");
		//Analyseur analyseurDeTest=new Analyseur("Text.txt");
		analyseurDeTest.analyser();
		for (Relation relation : analyseurDeTest.getRelations_trouvees()) {
			System.out.println("Relation : "+relation.getType()+"("
											+relation.getTerm1()+","+relation.getTerm2()+")");
		}
		fetchPatrons ("Patterns.txt");
		Analyseur analyseurDeTest=new Analyseur("/auto_home/msebih/Articles/D/Dépression (psychiatrie).txt");
		//Analyseur analyseurDeTest=new Analyseur("Text.txt");
		analyseurDeTest.analyser();
		for (Relation relation : analyseurDeTest.getRelations_trouvees()) {
			System.out.println("Relation : "+relation.getType()+"("
											+relation.getTerm1()+","+relation.getTerm2()+")");
		}
		fetchPatrons ("Patterns.txt");
		Analyseur analyseurDeTest=new Analyseur("/auto_home/msebih/Articles/D/Dépression (psychiatrie).txt");
		//Analyseur analyseurDeTest=new Analyseur("Text.txt");
		analyseurDeTest.analyser();
		for (Relation relation : analyseurDeTest.getRelations_trouvees()) {
			System.out.println("Relation : "+relation.getType()+"("
											+relation.getTerm1()+","+relation.getTerm2()+")");
		}*/
	}
	
	public static void fetchPatrons (String filePath) throws IOException{

		BufferedReader buffer = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
		String tmp;
		String tmc = "none";
		String tmpComp;
		String type = null;
		String patron;
		int nbrTerms = 0;
		String carAccentues=",àâäçèéêëîïôöùûüÀÂÄÇÈÉÊËÎÏÔÖÙÛÜ\\-";
		Pattern ExpRegPatron = Pattern.compile("\\s([A-Za-z\\s_'$"+carAccentues+"]+)\\s([$].*)");
		Pattern ExpRegType = Pattern.compile("([A-Za-z/"+carAccentues+"]+) [:]");
		Pattern ExpRegNbrTerms = Pattern.compile("\\$[A-Za-z]");
		Matcher matcherPatron ;
		Matcher matcherType ;
		Matcher matcherNbrTerms ;
		while ((tmpComp=buffer.readLine()) != null) {
			if (tmpComp.contains("-->")) {
				tmp=tmpComp.split("-->")[0].trim();
				tmc=tmpComp.split("-->")[1].trim();
			}
			else {
				tmp=tmpComp;
				tmc="none";
			}
			matcherPatron = ExpRegPatron.matcher(tmp);
			matcherType = ExpRegType.matcher(tmp);
			matcherNbrTerms = ExpRegNbrTerms.matcher(tmp);
			if (matcherType.find()){
				type=matcherType.group(1);
				//System.out.println("Type de relation ajouté: "+type);
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
					if (matcherPatron.group(2).contains("$Post")) {
						patron += "$Post";
						nbrTerms--;
					}
					Relation.typePatrons.get(type).add(patron);
					Relation.patronNbrTerms.put(patron,new Integer (nbrTerms));
					if (!tmc.equals("none")) {
						Relation.patronConstraint.put(type+" : "+patron,tmc);
					}
					
				}
				else {
					//System.out.println("Patron déja définit ---> " +patron);
				}
				
			}
		
		}
	
	//System.out.println("Types de relations définis: "+Relation.types_de_relations);
	//System.out.println("Patrons définis: "+Relation.typePatrons.values());
	//System.out.println("Patrons avec contraintes: "+Relation.patronConstraint);
	
	}
	
	
}
