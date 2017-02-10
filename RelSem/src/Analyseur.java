import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyseur {
	String filePath;
	private String text;	
	private ArrayList<Relation> Relations_trouvees = new ArrayList<Relation>();
	
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
				Pattern ExpReg= Pattern.compile("\\s([A-Za-z_éàè]+)\\s"+patron.replace(" ", "\\s")+"\\s([A-Za-z_éàè]+).*");
				Matcher matcher = ExpReg.matcher(this.text);
				if (matcher.find()){
					System.out.println("Un patron de "+type+" est détecté :"+patron);
					Relations_trouvees.add(new Relation(type, matcher.group(1), matcher.group(2)));
				}
			}
		}
	}
	//Prétraitements
	public void pretraitement(){
		this.lemmatisation();
		this.mots_composes();
	}
	//Nettoyage du contenu téléchargé (HTML ou autre).
	public void Parser(){
		
	}
	//Remplacement des espaces par des underscores.
	public void mots_composes(){
		
	}
	//Mise des verbes conjugués à l'infinitif.
	public void lemmatisation(){
		
	}
	public ArrayList<Relation> getRelations_trouvees() {
		return Relations_trouvees;
	}
}
