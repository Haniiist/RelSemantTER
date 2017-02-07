- Page web publique : 
	
   - Entrée :
      - Chaîne de caractères (Page ou phrase).
	  - Eventuellement : Proposition de schémas.
   - Sortie : 
	   - Relations.

- Patrons : 

   - Manuellement.
   - Page web privée avec mot de passe. (L'utilisateur pourra ajouter des schémas).

- Jeuxdemots : 
   - Récupérer la liste des mots de jeuxdemots.
   - [API Requester](https://drive.google.com/file/d/0BzdfugCnDDsIOE04X2Voc1hyXzQ/view).
	

- Extraire connaissances de relations : 
	- ($x est mort de $y)---->($y caractéristique : mortel).


- Contraintes sémantiques : 

   - Enlever l'ambiguité d'une relation exple : 
			
			La robe de la fille.
			La roue de la voiture. 
			possessive ou partitative ??

			Contraintes : si $x est un vêtement et $y personne alors possessive.....

						  si $x ou $y polysémiques ---> désambiguation : poids du sens ciblé.
					  
- Wikipédia :

   - Stocker dans plusieurs dossiers.

- Prétraitements : 

	- Lemmatisation : 
	   - Transformation exple : forme verbale ---> infinitif.
	   - Transformation exple : pluriel ---> singulier.
	   - Si ambiguité ---> POSTagging.
	   - Dico : Formes conjugé de être ou avoir par exple ---> lem (être ou avoir).

	- Nettoyage : 

	   - Virer les balises.

	- Mots composés : 

	   - Source dico jeuxdemots.
	   - Lemmatisation
	   - fenêtre glissante, prendre le mot le plus grand.
	   - Remplacer les espaces par des underscores.


