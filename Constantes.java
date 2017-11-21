package Devoir;

public class Constantes {

	public final static int PORT_ADMIN=33333;
	public final static int PORT_CLIENT=44444;
	public final static int POOLSIZE=10;
	public final static int PORTSSL=50000;
	
	//separateur d'objet de la liste pour le reformatage à la reception
	public final static String SEPARATEUR=" $ ";
	
	//chemin vers les fichiers XML
	public final static String PATH_XML="/home/moh/";
	
	//Reponses predefinies du serveur
	public final static String LOGIN_NO_OK="LOGIN_NO_OK";
	public final static String LOGIN_OK="LOGIN_OK";
	public final static String LIST_VIDE="LIST_VIDE";
	
	
	//CODES REQUETES LOCATAIRES "REQ_LOC....."
	public final static String REQ_LOC_LIST_ALL="REQ_LOC_LIST_ALL";
	public final static String REQ_LOC_LIST_MONTANT="REQ_LOC_LIST_MONTANT";
	public final static String REQ_LOC_LIST_PIECES="REQ_LOC_LIST_PIECES";
	public final static String REQ_LOC_RESERVER="REQ_LOC_RESERVER";
	
	//CODES REQUETES LOUEURS "REQ_LOU....."
	public final static String REQ_LOU_LOCATIONS ="REQ_LOU_LOCATIONS";
	public final static String REQ_LOU_LOCATAIRES="REQ_LOU_LOCATAIRES";
	public final static String REQ_LOU_LOC_APPART="REQ_LOU_LOC_APPART";
	public final static String REQ_LOU_NEW_LOCATION="REQ_LOU_NEW_LOCATION";
	public final static String REQ_LOU_SUP_LOCATION="REQ_LOU_SUP_LOCATION";
	
	
	//fonction de reformatage de chaine de caracteres: remplace les elements $ par des sauts de lignes
	public static String reformatString(String str){

		return str.replaceAll("\\$","\n");
	}
}
