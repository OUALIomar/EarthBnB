package Devoir;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Predicate;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import Devoir.Type;


public class Esclave implements Runnable {
	private final Socket socket;
	private  SendMail servermsg;
	
	private Locataire locataire;
	private Loueur loueur;
	private String mail="";
	private String req="";

	public Esclave(Socket socket) throws IOException , UnknownHostException{
		//INITIALISATION DE LA SOCKET ET LA CLASSE POUR MANIPULER LES MAILS
		this.socket=socket;
		this.servermsg=new SendMail();
	}

	@Override
	public void run() {
		
	StringBuilder chaine=new StringBuilder();
	String lecteur="";
	
	try {
		BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		//LECTURE DU MODE (ADMINISTRATEUR OU ORDINAIRE) 
		lecteur=in.readLine();
		//TRAITEMENTS DES REQUETES CLIENT ORDINAIRE
		if (Integer.parseInt(lecteur)==Constantes.PORT_CLIENT){
					
				/*recuperation du TYPE D'UTILISATTION (inscription,loueur,locataire) du mail et du MOT DE PASSE	*/
				lecteur=in.readLine();
				chaine.append(lecteur);		
				String cmd[]=chaine.toString().split(" ");
					
				boolean trouve=false;
				boolean existeLoc=false;
						
				switch (cmd[0]){
				//inscription 
				case "0":{
					//VALIDATION DU MAIL
					if(!Personne.isValidMailAddr(cmd[1])){
						out.println("Le mail que vous avez fourni est malformé");
						out.flush();
						throw new WrongMailAddressException("Mail erroné");
					}
					//controle si un compte à ce nom EXISTE DEJA
					for (HashMap.Entry <String, String> entry : Serveur.comptesmap.getComptesMap().entrySet() ) {
						if(entry.getKey().equals(cmd[1])) {trouve=true;break;}
						}
					//COMPTE EXISTATNT
					if(trouve) {
						out.println("Un compte à ce nom existe deja");
						out.flush();
					}
					//CREATION DU COMPTE
					else{
					//UTILISATION DE LA FONCTION QUI GENERE L'EMPREINTE MD5 POUR SAUVEGARDER LE MOT DE PASSE
					Serveur.comptesmap.ajouter(cmd[1],Personne.generateMD5(cmd[2]));
					out.println("Bravo! vous étes à present inscrit");
					out.flush();
					}
				
				};
				break;
		
				/***************Loueur*******************/
				case "1":{
				
					//verification login et mot de passe
					for (HashMap.Entry <String, String> entry : Serveur.comptesmap.getComptesMap().entrySet() ) {
						if(entry.getKey().equals(cmd[1])  && entry.getValue().equals(Personne.generateMD5(cmd[2]))) {trouve=true;break;}
						}
					//UTILISATEUR À SAISI LES BON id
					if (trouve){
						//CREATION D'UN OBJET LOUEUR
						this.loueur=new Loueur(cmd[1]);
						
						//RECHERCHE DU LOUEUR DANS LA LISTE
						for(Loueur lo : Serveur.loueurslist.getList()){
							if (lo.getMailaddr().equals(cmd[1])){
								existeLoc=true;
								break;
							}				
						}
						
						//PREMIERE CONNEXION: ajout dans la liste des loueurs	
						if (!existeLoc) {
							Serveur.loueurslist.ajouter(this.loueur);		
						}
						//CONNEXION OK
						out.println(Constantes.LOGIN_OK);
						out.flush();
					}
					else 
					//Compte innexistant
					{
						out.println(Constantes.LOGIN_NO_OK);
						out.flush();
					return;}
				
				};
				break;
				
		
				/***************Locataire*******************/
				case "2":{
					
					//verification login et mot de passe
					for (HashMap.Entry <String, String> entry : Serveur.comptesmap.getComptesMap().entrySet() ) {
						if(entry.getKey().equals(cmd[1])  && entry.getValue().equals(Personne.generateMD5(cmd[2]))) {trouve=true;break;}
						}
					//UTILISATEUR À SAISI LES BON id
					if (trouve){
						this.locataire=new Locataire(cmd[1]);
						
		
						for(Locataire lo : Serveur.locataireslist.getList()){
							if (lo.getMailaddr().equals(cmd[1])){
								existeLoc=true;
								break;
							}				
						}
						
						//premiere connexion: ajout dans la liste des Locataires
						if (!existeLoc) { Serveur.locataireslist.ajouter(this.locataire);}	
						out.println(Constantes.LOGIN_OK);
						out.flush();
					}
					else
						//Compte innexistant
					{
						out.println(Constantes.LOGIN_NO_OK);
						out.flush();
					return;}
				
				};
				break;
				
				}
				
				/*--------------Recuperation de la requete de l'utilisateur--------------*/
				
			req=in.readLine();
			StringBuilder rep=new StringBuilder();
			switch (req){
				//LISTE DES APPART PROPOSÉS PAR UN LOUEUR
				case Constantes.REQ_LOU_LOCATIONS:{
					//RECUPERATION DES APPART DU LOUEUR EN UTILISANT SON MAIL
					Predicate <Appartement> isLoueur= appart ->appart.getLouer().getMailaddr().equals(this.loueur.getMailaddr());  
					Serveur.appartementslist.getList().stream().filter(isLoueur).forEach(loc -> rep.append(loc.toString()+Constantes.SEPARATEUR));
					out.println(rep.toString());
					out.flush();
				};
					break;
					
				//LISTE DES LOCATAIRES DES LOCATIONS PROPOSÉS PAR UN LOUEUR
				case Constantes.REQ_LOU_LOCATAIRES:{
					//RECUPERATION DES LOCATIONS DU LOUEUR EN UTILISANT SON MAIL
					Predicate <Location> ismyLocataire= locat ->locat.getAppartement().getLouer().getMailaddr().equals(this.loueur.getMailaddr());  
					Serveur.locationslist.getList().stream().filter(ismyLocataire).forEach(loc -> rep.append(loc.getLocataire().toString()+Constantes.SEPARATEUR));
					out.println(rep.toString());
					out.flush();
				}
				break;
				
				//lOCATAIRE D'UN APPART
				case Constantes.REQ_LOU_LOC_APPART:{
					//RECUPERATION DES LOCATIONS DU LOUEUR EN UTILISANT SON MAIL
					Predicate <Location> ismyLocataire= locat ->locat.getAppartement().getLouer().getMailaddr().equals(this.loueur.getMailaddr());  
					Serveur.locationslist.getList().stream().filter(ismyLocataire).forEach(loc -> rep.append(Serveur.locationslist.getList().indexOf(loc) +" "+ loc.getAppartement().toString()+Constantes.SEPARATEUR));
					//VERIFICATION SI LA LISTE N'EST PAS VIDE
					if (!rep.toString().isEmpty()){
						//ENVOI DE LISTE DES LOCATION DU LOUEURS
						out.println(rep.toString());
						out.flush();
						//RECEPTION DE L'INDEX DE LA LOCATION
						req=in.readLine();
						
						//TRY SI L'INDEX FOURNI NE DEPASSE PAS LA TAILLE DE LA LISTE SINON CATCH IndexOutOfBoundsException
						try{
							//TEST SI CE NUMERO DE LOCATION APPARTIENT VRAIMENT AU LOEUR
							if(Serveur.locationslist.getList().get(Integer.parseInt(req)).getAppartement().getLouer().getMailaddr().equals(this.loueur.getMailaddr())){
								Location loc = Serveur.locationslist.getList().get(Integer.parseInt(req));
								out.println(loc.getLocataire().toString());
								out.flush();
							}else{
								out.println("Vous n'avez pas acces à cette location");
								out.flush();
							}
						}catch (IndexOutOfBoundsException e){
							out.println("Ce numero de location n'existe pas");
							out.flush();
						}
					}
					else
					{
						out.println(Constantes.LIST_VIDE);
						out.flush();
					}
				};
					break;
					
				//PROPOSITION D'UNE NOUVELLE LOCATION
				case Constantes.REQ_LOU_NEW_LOCATION:{
					
					lecteur=in.readLine();
					String locat[]=lecteur.toString().split(" ");
					//reformatage du nom de la rue EN REMPLAÇANT LES - PAS DES "espace"
					locat[0]=locat[0].replaceAll("-", " ");
					try{
						Adresse adresse=new Adresse(locat[0],Integer.parseInt(locat[1]));
						Appartement appart= new Appartement(adresse, Integer.parseInt(locat[2]), Integer.parseInt(locat[3]), Type.valueOf(locat[4]), this.loueur);
						Serveur.appartementslist.ajouter(appart);
						
					}
					catch (WrongNumberOfDigitsException e1)
					{
						out.println("code postal malformé");
						out.flush();
					}
					catch(NegativeArgumentException e2){
						out.println("nombre de pieces ou montant loyer negatif");
						out.flush();
					}
			
		
					
				};
					break;
				
				//SUPPRESSION D'UNE LOCATION
				case Constantes.REQ_LOU_SUP_LOCATION:{
					//RECUPERATION DES APPARTS DU LOUEUR EN UTILISANT SON MAIL
					Predicate <Appartement> isLoueur= appart ->appart.getLouer().getMailaddr().equals(this.loueur.getMailaddr());  
					Serveur.appartementslist.getList().stream().filter(isLoueur).forEach(loc -> rep.append(Serveur.appartementslist.getList().indexOf(loc)+"  " +loc.toString()+Constantes.SEPARATEUR));
					//VERIFICATION SI LA LISTE N'EST PAS VIDE
					if (!rep.toString().isEmpty()){
						//ENVOI DE LISTE DES APPARTS DU LOUEUR
						out.println(rep.toString());
						out.flush();
						//RECEPTION DE L'INDEX DE L'APPART
						req=in.readLine();
						//TRY SI L'INDEX FOURNI NE DEPASSE PAS LA TAILLE DE LA LISTE SINON CATCH IndexOutOfBoundsException
							try{
								//TEST SI CE NUMERO D'APPART APPARTIENT VRAIMENT AU LOEUR
								if(Serveur.appartementslist.getList().get(Integer.parseInt(req)).getLouer().getMailaddr().equals(this.loueur.getMailaddr())){
									Serveur.appartementslist.retirer(Integer.parseInt(req));
								}
								else{
									
									out.println("Vous n'avez pas acces à cette location");
									out.flush();
								}
							}catch (IndexOutOfBoundsException e){
							out.println("Ce numero de location n'existe pas");
							out.flush();
							}
						out.println("Location supprimé");
						out.flush();
					}
					else
					{
						out.println(Constantes.LIST_VIDE);
						out.flush();
					}
								
				};
					break;
					
				/*===================================Requetes Locataires=========================================*/
			  	//Requete d'affichage sans criteres
				case Constantes.REQ_LOC_LIST_ALL:{
					Serveur.appartementslist.getList().forEach(appart -> rep.append(appart.toString()+Constantes.SEPARATEUR));
					out.println(rep.toString());
					out.flush();	
				};
					break;
					
				//Requete d'affichage avec contrainte sur le loyer
				case Constantes.REQ_LOC_LIST_MONTANT:{
					//montant lu dans le buffer "req"
					req=in.readLine();
					Predicate <Appartement> plafond= ap -> ap.getLoyermensuel()<=Integer.parseInt(req); 
					Serveur.appartementslist.getList().stream().filter(plafond).forEach(appart -> rep.append(appart.toString()+Constantes.SEPARATEUR));
					out.println(rep.toString());
					out.flush();
				};
					break;
				
				//Requete d'affichage avec contrainte sur le nombre de chambres	
				case Constantes.REQ_LOC_LIST_PIECES:{
					req=in.readLine();
					Predicate <Appartement> nbpieces= ap -> ap.getNbpiece()==Integer.parseInt(req);
					Serveur.appartementslist.getList().stream().filter(nbpieces).forEach(appart -> rep.append(appart.toString()+Constantes.SEPARATEUR));
					out.println(rep.toString());
					out.flush();
				};
					break;
				case Constantes.REQ_LOC_RESERVER:{
					Serveur.appartementslist.getList().forEach(appart -> rep.append(Serveur.appartementslist.getList().indexOf(appart)+"      "+appart.toString()+Constantes.SEPARATEUR));
					if (!rep.toString().isEmpty()){
						out.println(rep.toString());
						out.flush();
						String numappart=in.readLine();
						try{
							Appartement appart=Serveur.appartementslist.getList().get(Integer.parseInt(numappart));
							if(!appart.getLouer().getMailaddr().equals(this.locataire.getMailaddr())){
								req=in.readLine();
								
								//formatage de la date reçu
								DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
								Date datefin = dateFormat.parse(req);
							
								//ajout à la liste des locations
								Location location=new Location(appart.getLouer(), this.locataire, appart,datefin);
								Serveur.locationslist.ajouter(location);
								
								//SUPPRESSION DE LA LISTE DES APPRTEMENT LIBRE
								Serveur.appartementslist.retirer(Integer.parseInt(numappart));		
								
								//envoi mail au loueur
								this.servermsg.envoyer(appart.getLouer().getMailaddr(), appart.toString(), this.locataire.getMailaddr());
								//envoi mail au locataire
								this.servermsg.envoyer(this.locataire.getMailaddr(), appart.toString(), this.locataire.getMailaddr());
								
								out.println("Reservé! verifiez vos mails pour les details de la reservation");
								out.flush();
								}
							else{
								out.println("Cet appartement est le votre vous ne pouvez pas le louer");
								out.flush();
							}
							
			
						}catch (IndexOutOfBoundsException e){
							out.println("Ce numero de location n'existe pas");
							out.flush();
						} catch (AddressException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						out.println(Constantes.LIST_VIDE);
						out.flush();
					}
			
				};
					break;	
			}
			
		}
		//TRAITEMENTS DES REQUETES CLIENT ADMIN
		else if (Integer.parseInt(lecteur)==Constantes.PORT_ADMIN){
			lecteur=in.readLine();
			chaine.append(lecteur);
			String cmd[]=chaine.toString().split(" ");
			//21232f297a57a5a743894a0e4a801fc3= L'EMPREINTE MD5 DU MOT DE PASSE "admin"
			//earthbnb@gmail.com MAIL ADMINISTRATEUR
			if (cmd[0].equals("earthbnb@gmail.com") && Personne.generateMD5(cmd[1]).equals("21232f297a57a5a743894a0e4a801fc3")){
				out.println(Constantes.LOGIN_OK);
				out.flush();
				StringBuilder rep=new StringBuilder();
				//LISTER TOUT LES APPARTEMENT LIBRES DE TOUT LE MONDE
				Serveur.appartementslist.getList().forEach(appart -> rep.append(Serveur.appartementslist.getList().indexOf(appart)+"      "+appart.toString()+Constantes.SEPARATEUR));
				//VERIFICATION SI LA LISTE N'EST PAS VIDE
				if (!rep.toString().isEmpty()){
					out.println(rep.toString());
					out.flush();
					req=in.readLine();
					Serveur.appartementslist.retirer(Integer.parseInt(req));
					out.println("Appartement "+rep+ " supprimé de la liste");
					out.flush();
					
				}else{
					out.println(Constantes.LIST_VIDE);
					out.flush();
				}
			}
			//CONNEXION REFUSÉE
			else{
				out.println(Constantes.LOGIN_NO_OK);
				out.flush();
				return;
			}
		}
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}

}
