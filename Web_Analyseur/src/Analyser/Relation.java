package Analyser;
import java.util.ArrayList;
import java.util.HashMap;

public class Relation {
	public static ArrayList<String> types_de_relations = new ArrayList<String>();
	public static HashMap<String, ArrayList<String>> typePatrons = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, Integer> patronNbrTerms = new HashMap<String, Integer>(); 
	private String type;//Partitative,méronymie...
	private String term1;//Premier terme lié par la relation.
	private String term2;//Deuxième terme lié par la relation.
	 
	public static ArrayList<String> getTypes_de_relations() {
		return types_de_relations;
	}

	
	
	public Relation(String type, String term1, String term2) {
		this.type = type;
		this.term1 = term1;
		this.term2 = term2;
		if (!types_de_relations.contains(type)) {
			types_de_relations.add(type);
		}
	}
	
	
	//Getters
	public String getTerm1() {
		return term1;
	}
	public String getTerm2() {
		return term2;
	}
	public String getType() {
		return type;
	}
	
	//Setters
	public void setTerm1(String term1) {
		this.term1 = term1;
	}
	public void setTerm2(String term2) {
		this.term2 = term2;
	}
	public void setType(String type) {
		this.type = type;
	}
}
