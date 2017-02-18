<%@ page import = "Analyser.*" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Web Analyser</title>
</head>
<body>
<div style="margin: 0 auto; width: 43%"><h2>Démo du méchanisme d'extraction</h2></div>
<div style="margin: 0 auto; width: 50%">
  <br><br><br>
<form action="maPage.jsp">
  Entrez le texte à analyser :<br><br>
  <textarea rows="15" cols="50" name="inputText"><%if (request.getParameter("inputText")==null){%>
La grippe se caractérise par une fièvre dûe à un virus.
La grippe fait partie des infections_virales_saisonnières.
Le cancer est de la famille des maladies_multifactorielles.
Le sida est une maladie_infectieuse dûe à un virus constitué d'une enveloppe_proteique et d'un matériel_génétique. 
Le rastafarisme est une sorte de philosophie.
Le lapin est un animal.
La fièvre est causée par agent_étranger.
La fille a des boucles_d'oreilles.
Le lapin a des oreilles.
La fille est une bonne_cuisinière.
<% } 
   else
   		{
	   		out.println(request.getParameter("inputText"));
   		}
%></textarea>
  <br><br><br>
  <div style="margin: 0 auto; width: 50%">
  <input type="submit" value="Extraire relations sémantiques">
  </div>
  <br><br><br>
  
<%
		if (request.getParameter("inputText")!=null)
		{
			if (Relation.types_de_relations.isEmpty()){
				
				Principale.fetchPatrons (session.getServletContext().getRealPath("/WEB-INF/Patterns.txt"));
			
			}
			Analyseur analyseurDeTest=new Analyseur();
			analyseurDeTest.setText(request.getParameter("inputText"));
			analyseurDeTest.analyser();
			out.println("Relations extraites :<br><br>");
			for (Relation relation : analyseurDeTest.getRelations_trouvees()) {
				out.println(relation.getType()+"("
											+relation.getTerm1()+","+relation.getTerm2()+")<br>");
				}
			
		}
%>  
</form> 
</div>
</body>
</html>