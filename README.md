# EarthBnB
EarthBnB is a JAVA distributed application, enabling users to rent appart

#### ReadME in French ####

**Important**

	>LE MOT DE PASSE DE L'ADMIN EST "ADMIN" ET SON MAIL "earthbnb@gmail.com"
	>ON NE PEUT PAS ACCEDER AUX ESPACES LOUEURS OU LOCATAIRES QU'APRES INSCRIPTION ET CETTE INSCRIPTION VOUS DONNES ACCES AUX DEUX ESPACES
	>LE CHEMIN DES FICHIERS XML (INITIALEMENT VIDE JUSTE AVEC LE FORMAT ADÉQUAT) DOIT ETRE DEFINI DANS LA CLASSE CONSTANTES,ATTRIBUT "PATH_XML"
	>LES CLASSES SE TROUVE DANS UN PACKAGE NOMMÉ "DEVOIR"

LES CLASSES:

	------------------
	La Class CONSTANTES :
		definit tout les constantes de l'application. 
		-Les ports de connexion le poolsize et les codes des requettes.
		-La constante separateur ayant pour valeur "$" servira à separer 
		 les objets de nos listes pour les reformater avec des sauts de lignes
		 sur le client.
		-Le chemin du dossier ou se trouve les fichiers XML utiliser lors 
		 du marshalling et unmarshalling JAXB est definis dans la constante:
		 "PATH_XML"
	-----------------------
	La classes Maitre :
		est elle qui lance le serveur et ses deux  threads  
		en ecoute sur des ports differents.
		Thread client -> port_client pour les client ordinaire
		thread administrateur -> port-admin pour l'admin.
		Une fois les threads lancés, le maitre defini un objet Timer et soumet
		la Tache (qui implemente TimerTask) et qui servira à mettre à jour les données (i.e  retirer les locations
		arriver à echeance et les reinserer dans les appartements libres) et ce chaque 24h à 23h:59m:59s:999ms
	
	 
	-----------------------

	la classe Esclave:
		Delegué par le serveur pour traiter les requetes des clients.
		Contient different plusieurs niveau de traitments:
		Requetes client ordinaires
			>Inscription : servira à creer un compte puis lors de l'utilisation on aura acces à l'espace loueur et locataire.
			>Utilisation en tant que loueur
			>Utilisation en tant que locataire
		Requetes admin
			>L'admin ne s'inscrit il a son login qui est le mail utilisé pour confirmer les locations et un mot de passe
			 defini au prealable

	-------------------

	la classe Client:
		Le client aura le choix de lancer le serveur qu'il desire utiliser.
		Utilisateur ordinaire -> on ouvre la connexion sur le port_client.
		Administrateur -> on ouvre une connexion sur le port_admin.
		------------------------------
		les utilisateur ordinaire peuvent s'inscrire, se connecter comme
		loueur ou locataire une fois inscrit.
		L'administrateur peut supprimer tout location sans restriction. 
		------------------------------
		On a pu utiliser la fonctionnalité Java mail vus en cours pour 
		confirmer les reservation par mails.
	-------------------------------

	La classe Personne:
		Est une classe abstraite qui definie les attributs et constructeurs des classe filles (Loueur,Locataire)
		mais qui n'est pas instanciable.
		Elle define aussi la methode qui calclule les empreintes MD5 des mot de passe qui seront stocké.

	----------------------
	La classe ComptesMap:
		Est une class qui contient une Map qui vas servir à stocker les login des utilisateurs inscrits
		ainsi que les empreintes MD5 de leurs mot de passe.
		La methode ajouter dans cet classe n'est pas definie comme synchronized parce que nous avons utiliser
		ConcurentHashMap qui est thread-safe.

	----------------------
	L'interface DataList
		Cette interface servira à definir les service que nos listes de données devront fournir (setList,getList,ajout,retirer)
		les classes: (AppartementList,LoueurList,LocataireList,LocationList) implementent cette interface.

	---------------------
	Dans les classes AppartementList,LoueurList,LocataireList,LocationList:
	chaque ajout ou retrait d'elements dans la liste entraine une sauvegarde dans les fichiers XML respectifs à chacun des objets.
	Ces methodes sont toutes "Synchronized"

	--------------------
	La classe ManageData:
		Definit deux methodes statique , une pour le marshalling de nos objets listes en fichiers XML, et un marshalling pour les restaurer
		à chaque lancement de l'application.
		La constante PATH_XML est utilisé pour retrouver le chemin des fichier de sauvegarde,et le nom des fichier est recuperer via le namespace
		(nomPackage.nomClasse.xml) de la classe.

	-----------------------------
	La classe SendMail
		Initialise toute les proprietés necessaires pour se connecter sur la boite GMAIL de notre application (en s'appuyant sur le SMTP de GOOGLE)
		et definie aussi la methode envoyer pour envoyer des des mails.
