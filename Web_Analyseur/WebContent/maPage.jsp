<%@ page import = "Analyser.*" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
  <input type="text" name="inputText" value="La fille a des boucles_d'oreilles">
  <br><br><br>
  <input type="submit" value="Submit">
  <br><br><br><br><br>
  
<%
		if (request.getParameter("inputText")!=null)
		{
			
			Principale.fetchPatrons (session.getServletContext().getRealPath("/WEB-INF/Patterns.txt"));
			Analyseur analyseurDeTest=new Analyseur();
			analyseurDeTest.setText(request.getParameter("inputText"));
			analyseurDeTest.analyser();
			for (Relation relation : analyseurDeTest.getRelations_trouvees()) {
				out.println("Relation : "+relation.getType()+"("
											+relation.getTerm1()+","+relation.getTerm2()+")");
				}
			
		}
%>  
</form> 
</body>
</html>