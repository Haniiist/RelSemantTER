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
	
	static void fetchPatrons (String filePath) throws IOException{

		BufferedReader buffer = Files.newBufferedReader(Paths.get(filePath), Charset.forName("ISO-8859-1"));
		String tmp;
		String type = null;
		String patron;
		Pattern ExpRegPatron = Pattern.compile("\\s([A-Za-z\\s�]+)\\s");
		Pattern ExpRegType = Pattern.compile("([A-Za-zs���]+) [:]");
		Matcher matcherPatron ;
		Matcher matcherType ;
		while ((tmp=buffer.readLine()) != null) {
			matcherPatron = ExpRegPatron.matcher(tmp);
			matcherType = ExpRegType.matcher(tmp);
			if (matcherType.find()){
				type=matcherType.group(1);
				System.out.println("Type de relation ajout� : "+type);
				if (!Relation.types_de_relations.contains(type)) {
					Relation.types_de_relations.add(type);
					Relation.typePatrons.put(type, new ArrayList<String>());
				}
			}
			if (matcherPatron.find()){
				patron=matcherPatron.group(1);
				if (!Relation.types_de_relations.contains(type)) {
					System.err.println("Type de relation inconnu pour le patron : "+patron);
				}
				else if (!Relation.typePatrons.get(type).contains(patron)) {
					Relation.typePatrons.get(type).add(patron);
				}
				else {
					System.out.println("Patron d�j� d�finit ---> " +patron);
				}
				
			}
		
		}
	
	System.out.println("Types de relations d�finis: "+Relation.types_de_relations);
	System.out.println("Patrons d�finis: "+Relation.typePatrons.values());
	
	}
	
	
}
