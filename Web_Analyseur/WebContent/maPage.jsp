<%@ page import = "Analyser.*" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Web Analyser</title>
</head>
<body>
<h2>Démo du méchanisme d'extraction</h2>
  <br><br><br>
<form action="maPage.jsp">
  Entrez le texte à analyser :<br><br>
  <textarea rows="6" cols="50" name="inputText">
<%if (request.getParameter("inputText")==null){%>
Le rastafarisme est une sorte de philosophie.
Le lapin est un animal.
La fièvre est causée par faible_immunité.
La fille a des boucles_d'oreilles.
Le lapin a des oreilles.
<% } 
   else
   		{
	   		out.println(request.getParameter("inputText"));
   		}
%>
</textarea>
  <br><br><br>
  <input type="submit" value="Extraire relations sémantiques">
  <br><br><br>
  
<%
		if (request.getParameter("inputText")!=null)
		{
			
			Principale.fetchPatrons (session.getServletContext().getRealPath("/WEB-INF/Patterns.txt"));
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
</body>
</html>