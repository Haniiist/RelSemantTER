package Analyseur;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Lemmatisation extends TextClass{

	//String oldText;
	//String newText;
	static HashMap<String, ArrayList<String>> map;

	public Lemmatisation () throws IOException {
		createDico("C:\\Users\\TOSHIBA\\workspace\\Samdi\\dico.txt.txt");
		oldText=new String();
		newText=new String();
	}

	public Lemmatisation (TextClass tc) {
		oldText=tc.newText;
		try {
			createDico("dico.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newText=lemmatizeText();
	}

	public static void createDico (String filePath) throws IOException {
		map = new HashMap<String, ArrayList<String>>();
		String line;
		BufferedReader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
		while ((line = reader.readLine()) != null)
		{
			String[] parts = line.split("	");
			if (parts.length >= 2)
			{
				String key = parts[0];
				String value = parts[1];

				String deux = parts[1]+"	"+parts[2];

				if (map.keySet().contains(parts[0])) {
					map.get(parts[0]).add(deux);
				}else {
					ArrayList<String> values = new ArrayList<String>();
					values.add(deux);
					map.put(key, values);
				}

			} else {
				System.out.println(line);
			}
		}
	}

	/*public String lemmatizeVerbsOnly() {
	}*/

	public String lemmatizeText() {
		String str = new String(oldText);
		String[] s = str.split("\\s");
		String res = new String();
		for (int i=0; i<s.length;i++) {
			if (howManyLemmes(s[i])==1 && getPos(s[i]).equals("Ver")) {
				if(!s[i].toLowerCase().equals("est") && !s[i].toLowerCase().equals("sont") && !s[i].toLowerCase().equals("a")){
					//System.out.println("  s[i] "+(s[i]));

					//System.out.println("  Lemma "+getLemme(s[i]));
					s[i]=getLemme(s[i]);
				}
			}
			res=res+s[i]+" ";
		}
		return res;
	}

	public String lemmatizeText(String str) {
		String[] s = str.split("\\s|[,.?!:;\\(\\)]+");
		String res = new String();
		for (int i=0; i<s.length;i++) {
				res=res+lemmatize(s[i])+" ";
			
		}
		res=res.replaceAll("\\s+", " ");

		return res;
	}
	
	public String lemmatizeTextPostMc(String str) {
		String[] s = str.split("\\s|[,.?!:;\\(\\)]+");
		String res = new String();
		for (int i=0; i<s.length;i++) {
				res=res+lemmatize(s[i])+" ";
			
		}
		res=res.replaceAll("\\s+", " ");
		res=res.replaceAll(" une | des | le | la | les | l' ", " un ");

		return res;
	}

	public String  lemmatize (String mot) {
		ArrayList<String> tab = map.get(mot);
		if (tab!=null){ 
			if (tab.size()==1){
				if (tab.get(0).split("	")[1].substring(0, 3).equals("Ver"));
				return tab.get(0).split("\\s")[0];
			}
		}
		return mot;
	}

	public int howManyLemmes (String mot) {
		int count=0;
		if (map.keySet().contains(mot))
			for (String key : map.keySet()){
				if(key.equals(mot))
					for (String str : map.get(mot)){
						count++;
					}
			}
		return count;
	}



	public String  getLemme (String mot) {
		return ((map.get(mot).get(0)).split("\\s"))[0];
	}



	/*public void  getLemmes(String mot) {
		if (!map.keySet().contains(mot)) String res = mot;ystem.out.println("Le mot n'existe pas");
		else {
			for (String key : map.keySet()){
				if(key.equals(mot))
					for (String str : map.get(mot)){
						String[] tab = str.split("	");
						//return tab[0];
						System.out.println("La source du mot "+mot+" est "+tab[0]);
					}
			}
		}
	}*/


	//isPluriel isMasculin isFeminin isNom isVerb isAdj isAmbigu (plus d'une correspondance)
	//getLemme("bois") ==> boire
	//getPos("bois") ==> verb ou nom ou adj pro det 
	//getLemme("tapis","Nom") ==> tapis         getLemme("tapis","Ver") ==> tapir
	// penser Ã  dÃ©clencher des exceptions dans les cas ambigus 
	// pour spliter sur plusieurs caractÃ¨res split(":|+")   "Nom:Mas+SG"  ==>   [0]="Nom" [1]="Mas" [2]="SG"

	public String  detecter(String mot,String s) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			for (String key : map.keySet()){
				if(key.equals(mot))
					for (String str : map.get(mot)){
						String[] tab = str.split("	");
						if (tab[1].contains(s)) return tab[0];
					}
			}
		}
		return null;
	}

	public String getPos(String mot) {
		if (map.keySet().contains(mot)){
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				return tab[1].substring(0, 3);
			}
		}
		return null;
	}

	public boolean isPluriel(String mot) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("PL")) return true;
			}
		}
		return false;
	}

	public boolean isAdj(String mot) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("Adj")) return true;
			}
		}
		return false;
	}


	public boolean isVerb(String mot) throws Exception{
		if (!map.keySet().contains(mot)) System.out.println("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("Ver")) return true;
			}
		}
		return false;
	}

	public boolean isMasculin(String mot) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("Mas")) return true;
			}
		}
		return false;
	}


	public boolean isNom(String mot) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("Nom")) return true;
			}
		}
		return false;
	}


	public boolean isFeminin(String mot) throws Exception{
		if (!map.keySet().contains(mot)) throw new Exception ("Le mot n'existe pas");
		else {
			for (String str : map.get(mot)){
				String[] tab = str.split("	");
				if (tab[1].contains("Fem")) return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws Exception{

		Lemmatisation lm = new Lemmatisation();

		File file = new File("Text.txt");
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		String str = new String(data, "UTF-8");
		str=str.replaceAll("'","' ");
		System.out.println(lm.lemmatizeTextPostMc(str));
		

	}

}
