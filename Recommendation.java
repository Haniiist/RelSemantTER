import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;

public class Recommendation {
	HashMap<String, Integer> positions;
	HashMap<String, Double> stats;
	HashMap<String, Double> finances;
	HashMap<String, String> reqPosition;
	HashMap<String, String> reqStats;
	
	ArrayList<String> positionList;
	ArrayList<String> statsListe;
	ArrayList<String> carcsListe;
	ArrayList<String> playerTypeList;
	HashMap<Integer, String> comparator;

	String Prefixe;
	String Triples;
	String Filter;

	
	public Recommendation (String path) throws IOException{
		BufferedReader buffer = Files.newBufferedReader(Paths.get(path), Charset.forName("ISO-8859-1"));
		//positions
		positions = new HashMap<String,Integer>();
		buffer.readLine();
		for(int i=1;i<=12;i++) {
			String[] v = buffer.readLine().split(":");
			if (Integer.parseInt(v[1])<2)
				positions.put(v[0], Integer.parseInt(v[1]));
		}
		//stats
		stats = new HashMap<String,Double>();
		buffer.readLine();
		for(int k=1;k<=4;k++) {
			String[] v = buffer.readLine().split(":");
			stats.put(v[0], Double.parseDouble(v[1]));
		}
		finances = new HashMap<String,Double>();
		buffer.readLine();
		for(int k=1;k<=2;k++) {
			String[] v = buffer.readLine().split(":");
			finances.put(v[0], Double.parseDouble(v[1]));
		}
		
		positionList = new ArrayList<String>();
		playerTypeList = new ArrayList<String>();
		statsListe = new ArrayList<String>();
		carcsListe = new ArrayList<String>();
		comparator = new HashMap<Integer, String>();

		
		positionList.add("Goalie");
		positionList.add("CenterBack");
		positionList.add("LeftBack");
		positionList.add("RightBack");
		positionList.add("DefendingMidfielder");
		positionList.add("CentralMidfielder");
		positionList.add("AttackingMidfielder");
		positionList.add("LeftMidfielder");
		positionList.add("RightMidfielder");
		positionList.add("LeftWinger");
		positionList.add("RightWinger");
		positionList.add("Striker");
		
		carcsListe.add("height");
		carcsListe.add("weight");
		carcsListe.add("age");


		
		statsListe.add("playerApps");
		statsListe.add("playerAssists");
		statsListe.add("playerDribblesPerGame");
		statsListe.add("playerGoals");
		statsListe.add("playerInterceptionPerGame");
		statsListe.add("playerMarketValue");
		statsListe.add("playerPassesPerGame");
		statsListe.add("playerPassSuccess");
		statsListe.add("playerShotsPerGame");
		statsListe.add("playerTacklesPerGame");
		
		playerTypeList.add("AssistMaker");
		playerTypeList.add("Distributor");
		playerTypeList.add("FreeAgent");
		playerTypeList.add("GoalScorer");
		playerTypeList.add("Interceptor");
		playerTypeList.add("ProneToInjuries");
		playerTypeList.add("PlayerWithEndingContract");
		playerTypeList.add("PromisingPlayer");
		playerTypeList.add("Shooter");
		playerTypeList.add("Starter");
		playerTypeList.add("StrongDefender");
		playerTypeList.add("TopDefender");
		playerTypeList.add("Aggressive");
		playerTypeList.add("Versatile");
		
		comparator.put(1,"<");
		comparator.put(2,">");
		comparator.put(3,">=");
		comparator.put(4,"<=");
		comparator.put(5,"=");
		comparator.put(6,"!=");



		
		Prefixe = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+"PREFIX owl: <http://www.w3.org/2002/07/owl#> "
				+"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+"PREFIX dbo: <http://dbpedia.org/ontology/> "
				+"PREFIX dbpedia: <http://dbpedia.org/resource/> "
				+"PREFIX foot: <http://www.semanticweb.org/msebih/ontologies/football#> "
				+"SELECT * Where { ";
		Triples = " ";
		Filter = " ";
	}
	
	public static void performSPARQLQuery(Model model, String queryString) {

        //System.out.println(queryString);
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            ResultSetFormatter.out(System.out, results, query);
        }

    }
	
	public static int menu() {

        int selection;
        Scanner input = new Scanner(System.in);

        /***************************************************/

        System.out.println("Veuillez choisir des critères pour la recherche de joueurs ");
        System.out.println("==============================================");       
        System.out.println("|   MENU SELECTION                           |");
        System.out.println("==============================================");
        System.out.println("|   Options:                                 |");
        System.out.println("|   1  - Par position                        |");
        System.out.println("|   2  - Par statistiques individuelles      |");
        System.out.println("|   3  - Par carctéristiques individuelles   |");
        System.out.println("|   4  - Par type de joueurs                 |");
        System.out.println("|   5  - Lister joueurs prometteurs          |");
        System.out.println("|   6  - Lister clubs en crise financiere    |");
        System.out.println("|   7  - Lister joueurs en fin de contrat    |");
        System.out.println("|   8  - Lister joueurs libres               |");
        System.out.println("|   9  - Lister joueurs bléssés à répétition |");
        System.out.println("|   10 - Lister joueurs extra-communautaires |");
        System.out.println("|   11 - Lister joueurs polyvalents          |");
        System.out.println("|   12 - Quit                                |");
        System.out.println("==============================================");

        selection = input.nextInt();
        return selection;    
    }
	
	public void printPosition(){
		
		for(int i=0;i<positionList.size();i++){
	        System.out.println(""+i+" - "+positionList.get(i));
		}
	}
	
		public void printPlayerType(){
		
		for(int i=0;i<playerTypeList.size();i++){
	        System.out.println(""+i+" - "+playerTypeList.get(i));
		}
	}
	
	public void printStats(){
		
		for(int i=0;i<statsListe.size();i++){
	        System.out.println(""+i+" - "+statsListe.get(i));
		}
	}
	
	public void printCarcs(){
		
		for(int i=0;i<carcsListe.size();i++){
	        System.out.println(""+i+" - "+carcsListe.get(i));
		}
	}
	
	public void printCompare(){
		
		for(int i : comparator.keySet()){
	        System.out.println(""+i+" - "+comparator.get(i));
		}
	}
	
	public void recherche(){
		int menu;
		menu = menu();
        Scanner input2 = new Scanner(System.in);
		switch (menu) {
	    case 1:
	      	{	  
	      		String position;
	      		String rposition = null;
	      		System.out.println("Veuillez choisir une position");
	      		printPosition();
	      		position = positionList.get(input2.nextInt());
	      		rposition = " ?subject foot:position foot:"+position+" .";
	      		Triples+=rposition;
	      		recherche();
	      		break;
	    	}
	    case 2:
	    	{
	    		String stats;
				String rstats = null;
				System.out.println("Veuillez choisir une statistique");
				printStats();
				stats = statsListe.get(input2.nextInt());
				rstats = " ?subject foot:playerStatistics ?statistics ."
						+ " ?statistics foot:"+stats+" ?"+stats+" .";
				Triples+=rstats;
				System.out.println("Veuillez choisir un opérande de comparaison");
				printCompare();
				int cmp = input2.nextInt();
				System.out.println("Veuillez choisir une valeur");
				String value = input2.next();
				Filter+= " FILTER ( ?"+stats+" "+comparator.get(cmp)+" "+value+" )";
	      		recherche();
				break;
	    	}
	    case 3:
	    	{
	    		String carcs;
	    		String rcarcs = null;
				System.out.println("Veuillez choisir une statistique");
				printCarcs();
				carcs = carcsListe.get(input2.nextInt());
				rcarcs = " ?subject foot:"+carcs +" ?"+carcs+" .";
				Triples+=rcarcs;	
				System.out.println("Veuillez choisir un opérande de comparaison");
				printCompare();
				int cmp = input2.nextInt();
				System.out.println("Veuillez choisir une valeur");
				String value = input2.next();
				Filter+= " FILTER ( ?"+carcs+" "+comparator.get(cmp)+" "+value+" )";
	      		recherche();
				break;
	    	}
		
	    case 4:
    	{
    		String playerType;
    		String rplayerType = null;
			System.out.println("Veuillez choisir un type de joueurs");
			printPlayerType();
			playerType = playerTypeList.get(input2.nextInt());
			rplayerType = " ?subject rdf:type  foot:"+playerType +" .";
			Triples+=rplayerType;	
      		recherche();
			break;
    	}
	    case 5:
		      {
		    	  Triples+=" ?subject rdf:type  foot:PromisingPlayer.";	
		      	  recherche();
				  break;
		      }
	    case 6:
	    		{
	    			Triples+=" ?subject dbo:currentTeam  ?team .";
	    			Triples+=" ?team rdf:type  foot:TeamInNeedOfSelling .";	
	    			recherche();
	    			break;
	    		}
	    case 7:
	    		{
	    			Triples+=" ?subject foot:contract  ?contract .";
	    			Triples+=" ?contract foot:yearsLeft  ?years.";
	    			Filter+=" FILTER( ?years <= 1.0) .";	
	    			recherche();
	    			break;

	    		}
	    
	    case 8:
	    		{
	    			Triples+=" ?subject rdf:type  foot:FreeAgent.";	
	    			recherche();
	    			break;
	    		}
	    		
	    case 9:
				{
					Triples+=" ?subject rdf:type  foot:ProneToInjuries.";	
					recherche();
					break;
				}
	    case 10:
				{
					Triples+=" ?subject foot:fromContinent  ?Continent . ?subject dbo:nationality ?Pays . ";
					Filter+= " FILTER (?Continent != dbpedia:Europe) . ";	
					recherche();
					break;
				}
	    case 11:
		{
			Triples+=" ?subject rdf:type  foot:Versatile.";	
			recherche();
			break;
		}
	    case 12:
				{
					break;
				}
		}

	}
	
	private void requetesPosition(){
		reqPosition = new HashMap<String,String>();
		String s = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+"PREFIX owl: <http://www.w3.org/2002/07/owl#> "
				+"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+"PREFIX dbo: <http://dbpedia.org/ontology/> "
				+"PREFIX dbpedia: <http://dbpedia.org/resource/> "
				+"PREFIX foot: <http://www.semanticweb.org/msebih/ontologies/football#> "
				+"SELECT ?subject ?valeur ?salaire WHERE { ?subject rdf:type foot:Player; foot:playerMarketValue ?valeur; "
																+"foot:position foot:object; "
																+"foot:contract ?contrat. "
																+"?contrat foot:weeklySalary ?salaire. "
																+"?subject foot:fromContinent  ?Continent "
																+"FILTER (?salaire<="+finances.get("maxPlayerSalary")+" && ?valeur<="+finances.get("maxPlayerTransfer")+")"
																+ "} ORDER BY DESC(?Continent)";
		for (String str : positions.keySet()){
			String s2 = new String(s);
			s2=s2.replaceAll("object", str);
			reqPosition.put(str,s2);
		}
	}
	
	private void requetesStats(){
		reqStats = new HashMap<String,String>();
		String s = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+"PREFIX owl: <http://www.w3.org/2002/07/owl#> "
				+"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+"PREFIX dbo: <http://dbpedia.org/ontology/> "
				+"PREFIX dbpedia: <http://dbpedia.org/resource/> "
				+"PREFIX foot: <http://www.semanticweb.org/msebih/ontologies/football#> ";
		if (stats.get("teamGoals")<40){ 
			String s2 = new String(s);
			s2+=" SELECT DISTINCT ?subject ?valeur ?salaire"
				+" WHERE {  {?subject rdf:type foot:AssistMaker} UNION {?subject rdf:type foot:GoalScorer}" 
				+" ?subject foot:playerMarketValue ?valeur;"  
                         +" foot:contract ?contrat." 
                +" ?contrat foot:weeklySalary ?salaire ."
				+"?subject foot:fromContinent  ?Continent "
                +" FILTER NOT EXISTS { ?subject rdf:type foot:proneToInjuries}"
                +" FILTER (?salaire<="+finances.get("maxPlayerSalary")+" && ?valeur<="+finances.get("maxPlayerTransfer")+")}"
                +" ORDER BY DESC(?Continent) ";
			reqStats.put("Votre équipe a marqué "+stats.get("teamGoals")+" buts la saison dernière. Ce nombre est inférieur à la moyenne des buts marqués par les équipes du championnat.\nNous vous recommandons de recruter des joueurs offensifs et décisifs, c'est à dire des joueurs qui créent des occasions de buts et des buteurs.\nVoici les joueurs qui correspondent à votre budget (coût du transfers et salaire du joueur) : ",s2);
		}
		if (stats.get("teamConcededGoals")>55){ 
			String s2 = new String(s);
			s2+=" SELECT DISTINCT ?subject ?valeur ?salaire"
				+" WHERE { ?subject rdf:type foot:TopDefender;" 
				         +" foot:playerMarketValue ?valeur;"  
                         +" foot:contract ?contrat." 
                +" ?contrat foot:weeklySalary ?salaire."
				+"?subject foot:fromContinent  ?Continent "
                +" FILTER NOT EXISTS { ?subject rdf:type foot:proneToInjuries}"
                +" FILTER (?salaire<="+finances.get("maxPlayerSalary")+" && ?valeur<="+finances.get("maxPlayerTransfer")+")}"
                +" ORDER BY DESC (?Continent)";
			reqStats.put("Votre équipe a encaissé "+stats.get("teamConcededGoals")+" buts la saison dernière. Ce nombre est supérieur à la moyenne des buts encaissés par les équipes du championnat.\nNous vous recommandons de recruter de bons défenseurs pour donner une meilleure solidité à votre défense.\nVoici les joueurs qui correspondent à votre budget (coût du transfers et salaire du joueur) : ",s2);
		}
		if (stats.get("avgPossession")<47.5){ 
			String s2 = new String(s);
			s2+=" SELECT DISTINCT ?subject ?valeur ?salaire"
				+" WHERE {  {?subject rdf:type foot:Interceptor} UNION {?subject rdf:type foot:Distributor}" 
				+" ?subject foot:playerMarketValue ?valeur;"
				         +" foot:position ?position;"
                         +" foot:contract ?contrat." 
                +" ?contrat foot:weeklySalary ?salaire."
                + "?position rdf:type foot:Midfielder."
				+"?subject foot:fromContinent  ?Continent ."
                +" FILTER NOT EXISTS { ?subject rdf:type foot:proneToInjuries}"
                +" FILTER (?salaire<="+finances.get("maxPlayerSalary")+" && ?valeur<="+finances.get("maxPlayerTransfer")+")}"
                +" ORDER BY DESC (?Continent)";
			reqStats.put("Votre équipe a enregistré une possession de balle de "+stats.get("avgPossession")+"% la saison dernière. Ce nombre est inférieur à la moyenne de possession des équipes du championnat.\nNous vous recommandons de recruter des milieux de terrains qui sont de bons récupérateurs ou de bons passeurs.\nVoici les joueurs qui correspondent à votre budget (coût du transfers et salaire du joueur) : ",s2);
		}
		if (stats.get("avgPassSuccess")<75){ 
			String s2 = new String(s);
			s2+=" SELECT DISTINCT ?subject ?valeur ?salaire"
				+" WHERE {?subject rdf:type foot:Distributor;" 
				         +" foot:playerMarketValue ?valeur;"
                         +" foot:contract ?contrat." 
                +" ?contrat foot:weeklySalary ?salaire."
				+"?subject foot:fromContinent  ?Continent ."
                +" FILTER NOT EXISTS { ?subject rdf:type foot:proneToInjuries}"
                +" FILTER (?salaire<="+finances.get("maxPlayerSalary")+" && ?valeur<="+finances.get("maxPlayerTransfer")+")}"
                +" ORDER BY DESC (?Continent)";
			reqStats.put("Votre équipe a enregistré un pourcentage de passes réussies de "+stats.get("avgPassSuccess")+"% la saison dernière. Ce nombre est inférieur au pourcentage moyen des équipes du championnat.\nNous vous recommandons de recruter des joueurs ayant un pourcentage élevé de passes réussies.\nVoici les joueurs qui correspondent à votre budget (coût du transfers et salaire du joueur) : ",s2);
		}
	}
	
	public static void main (String[] args) throws IOException {
		Recommendation r = new Recommendation("infoTeam.txt");
		
		r.requetesPosition();
		r.requetesStats();
		
		String onto_file = System.getProperty("user.dir") + "/football.owl";
        Model model = RDFDataMgr.loadModel(onto_file);
        
        for (String str : r.reqPosition.keySet()){
        	System.out.println("Votre équipe n'a que "+r.positions.get(str)+" joueurs au poste de "+str+".\nNous vous recommandons de recruter des joueurs à ce poste.\nVoici les joueurs qui correspondent à votre budget (coût du transfers et salaire du joueur) : ");
        	performSPARQLQuery(model, r.reqPosition.get(str));
        	System.out.println();
        }
        
        
        for (String str : r.reqStats.keySet()){
        	System.out.println(str);
        	performSPARQLQuery(model, r.reqStats.get(str));
        	System.out.println();
        }
        
        r.recherche();
    	performSPARQLQuery(model, r.Prefixe+r.Triples+r.Filter+" }");


        
	}
}
