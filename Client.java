package Devoir;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	
	
	public static void main(String[] args) {
		try {
			//DEFINITION DES CHAINES ET DES BUFFER UTILISÉ POUR LA COMMUNICATION
			StringBuffer buffer=new StringBuffer();
			String req="";
			String rep="";
			Scanner clavier= new Scanner(System.in);
			Console console= System.console();
			String mail="";
			String pass="";
			
			System.out.println("|-------------------------------------------------|");
	        System.out.println("|                      BIENVENUE                  |");
	        System.out.println("|-------------------------------------------------|");
	        System.out.println("|         Choisissez le mode de connexion :       |");
	        System.out.println("|              1- Utilisateur simple              |");
	        System.out.println("|              2- Administrateur                  |");
	        System.out.println("|-------------------------------------------------|");
	        req=clavier.nextLine();
			clearTerminal();
			/*-------------------------CONNEXION CLIENT ORDINAIRE------------------- */
			if (req.equals("1")){
				Socket socket=new Socket(InetAddress.getLocalHost(),Constantes.PORT_CLIENT);
				PrintWriter out= new PrintWriter( socket.getOutputStream());
			    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    
			    //ENVOI DE L'INFORMATION À L'ESCLAVE POUR TRAITER LES REQUETES CLIENT ORDINAIRE
			    out.println(Constantes.PORT_CLIENT);
			    out.flush();
			    
				System.out.println("Vous êtes? \n 0)Inscription \n 1)Loueur \n 2)Locataire \n");
				req=clavier.nextLine();
				clearTerminal();
				
				/*--------------------INSCRIPTION OU CONNEXION----------------------*/
				switch(req){
				//inscription
				case "0":{

						System.out.println("==============================================================================");
						System.out.println("|                      			Inscription 		                         |");
						System.out.println("==============================================================================");
						System.out.println("Saisissez votre mail SVP");
						mail=clavier.nextLine();
						System.out.println("Saisissez votre mot de passe");
						pass= new String(console.readPassword());
						//ENVOI DE LA REQUETE D'INSCRIPTION AVEC LE MAIL ET LE MOT DE PASSE
						out.println("0 "+mail+" "+pass);
						out.flush();
						clearTerminal();
						//ATTENTE DE CONFIRMATION
						System.out.println(in.readLine());

				};
				break;
				
				//Connexion loueur
				case "1":{
					//Recuperation des infos du loueurs
					System.out.println("Saisissez votre mail SVP");
					mail=clavier.nextLine();
					System.out.println("Saisissez votre mot de passe");
					pass= new String(console.readPassword());
					out.println("1 "+mail+" "+pass);
					//ENVOI DE LA REQUETE DE CONNEXION AVEC LE MAIL ET LE MOT DE PASSE
					out.flush();
					clearTerminal();
					rep=in.readLine();
					
					//LOGIN OU MOT DE PASSE FOURNI FAUX
					if (rep.equals(Constantes.LOGIN_NO_OK)) {System.out.println("Mail ou mot de passe ivalide"); return ;}
					
					System.out.println("==============================================================================");
					System.out.println("|                      		     	MENU		                             |");
					System.out.println("==============================================================================");
					System.out.println("| Options:                                                                   |");
					System.out.println("|        1. Afficher la liste des appartements déja proposé en location      |");
					System.out.println("|        2. Afficher la liste de ses locataires                        |");
					System.out.println("|        3. Afficher le locataire d'une appartement                       |");
					System.out.println("|        4. Proposer une nouvelle location                                   |");
					System.out.println("|        5. Supprimer une location                                           |");
					System.out.println("==============================================================================");
					
				// LECTEUR DE LA REQUETE DU LOUEUR
				req=clavier.nextLine();
				
				switch (req) {
					//LISTE DES APPART PROPOSÉS
					case "1":{
						out.println(Constantes.REQ_LOU_LOCATIONS);
						out.flush();
						//RECEPTION DE LA LISTE
						System.out.println("Adresse \t \t \t NbPieces \t Loyer \t Type \t Loueur");
						rep=in.readLine();
						System.out.println(	Constantes.reformatString(rep));
						
					};
					break;
					//LISTE DES LOCATAIRES DES LOCATIONS PROPOSÉS PAR LE LOUEUR
					case "2":{
						out.println(Constantes.REQ_LOU_LOCATAIRES);
						out.flush();
						rep=in.readLine();
						System.out.println(	Constantes.reformatString(rep));
					};
					break;
					//lOCATAIRE D'UN APPART
					case "3":{
						out.println(Constantes.REQ_LOU_LOC_APPART);
						out.flush();
						//RECEPTION DE LA LISTE DES APPART PROPOSÉS PAR LE LOUEUR
						rep=in.readLine();
						//VERIFICATION SI LA LISTE N'EST PAS VIDE
						if (!rep.equals(Constantes.LIST_VIDE)){
							System.out.println(	Constantes.reformatString(rep));	
							System.out.println("choisissez une location pour afficher le locataire ");
							req=clavier.nextLine();
							out.println(req);
							out.flush();
							String ch =in.readLine();
							System.out.println(ch);
						}
						else{
							System.out.println("Vous n'avez pas de locations");
						}
					};
					break;
					
					//PROPOSITION D'UNE NOUVELLE LOCATION
					case "4":{
						out.println(Constantes.REQ_LOU_NEW_LOCATION);
						out.flush();
						System.out.println("veuillez indiquer  le nom de la rue ");
						req=clavier.nextLine();
						//FORMATAGE DU NOM DE LA RUE SANS ESPACES POUR POUVOIR UN SPLIT SUR "espace" SUR L'ESCLAVE
						String reqsansspc=req.replaceAll("\\s","-");
						buffer.append(reqsansspc + " ");
						System.out.println("veuillez indiquer  le code postal de la commune ");
						req=clavier.nextLine();
						buffer.append(req + " ");
										
						System.out.println("veuillez indiquer le nombre de pièces ");
						req=clavier.nextLine();
						buffer.append(req + " ");
						System.out.println("veuillez preciser le loyer mensuel ");
						req=clavier.nextLine();
						buffer.append(req + " ");
						System.out.println("===========================================");
						System.out.println("|veuillez choisir le type de l'appartement|");
						System.out.println("===========================================");
						System.out.println("|Les types                                |");
						System.out.println("|	1.duplex                              |");
						System.out.println("|	2.loft                                |");
						System.out.println("|	3.chambre                             |");
						System.out.println("|	4.autre                               |");
						System.out.println("===========================================");
						req=clavier.nextLine();
						switch (req) {
						case "1":
							buffer.append("duplex ");

							break;
						case "2":
							buffer.append("loft ");
							
							break;
						case "3":
							buffer.append("chambre ");
		
						break;
						case "4":
							buffer.append("autre ");
		
						break;
						default:
							break;
						}
						
						
					//ENVOI DU BUFFER DE DETAILS DE LA NOUVELLES LOCATIONS
					out.println (buffer.toString());	
					out.flush();	
					System.out.println(in.readLine());
					};
					break;
					//SUPPRESSION D'UNE LOCATION
					case "5":{
						out.println(Constantes.REQ_LOU_SUP_LOCATION);
						out.flush();
						//RECEPTION DE LA LISTE DES APPART PROPOSÉS PAR LE LOUEUR
						rep=in.readLine();
						//VERIFICATION SI LA LISTE N'EST PAS VIDE
						if (!rep.equals(Constantes.LIST_VIDE)){
							System.out.println(	Constantes.reformatString(rep));
							System.out.println(" veuillez choisir un nemero d'appartement a supprimer ");
							req=clavier.nextLine();
							out.println(req);
							out.flush();
							String ch =in.readLine();
							System.out.println(ch);
						}
						else{
							System.out.println("Vous n'avez pas de locations");
						}
					};
					break;
					
					//CHOIX INNEXISTANT
					default: System.out.println("Requete inexistatnte: Ressaisizez votre requete");
						break;
				}
				
				
				
				
				
				
				};
				break;
				
				case "2":{
					//Recuperation des identifiants du locataire 
					System.out.println("Saisissez votre mail SVP");
					mail=clavier.nextLine();
					System.out.println("Saisissez votre mot de passe");
					pass= new String(console.readPassword());
					out.println("2 "+mail+" "+pass);
					out.flush();
					clearTerminal();
					rep=in.readLine();
					//LOGIN OU MOT DE PASSE FOURNI FAUX
					if (rep.equals(Constantes.LOGIN_NO_OK)) {System.out.println("Mail ou mot de passe ivalide"); return ;}
					
					
					System.out.println("=============================================");
					System.out.println("|   Options :                               |");
					System.out.println("|       1.Lister les locations disponibles  |");
					System.out.println("|       2.Reserver un appartement           |");
					System.out.println("=============================================");
					req=clavier.nextLine();
					clearTerminal();
					
						switch(req){
							//Liste des locations
							case "1":{
								System.out.println("=============================================");
								System.out.println("| option d'affichage:                       |");
								System.out.println("|   1.Aucun                                 |");										
								System.out.println("|   2.Selon un plafond de loyer             |");
								System.out.println("|   3.Selon nombre de piéces                |");
								System.out.println("=============================================");
								req=clavier.nextLine();
								clearTerminal();
								//CRITERES DE LISTE
										switch(req){
											//Liste complete
											case "1":{
												out.println(Constantes.REQ_LOC_LIST_ALL);
												out.flush();
												System.out.println("Adresse \t \t \t NbPieces \t Loyer \t Type \t Loueur");
												rep=in.readLine();
												System.out.println(	Constantes.reformatString(rep));
											}
											break;
											
											//Plafond loyer
											case "2":{
												out.println(Constantes.REQ_LOC_LIST_MONTANT);
												out.flush();
												System.out.println("Quel est le plafond de loyer?");
												req=clavier.nextLine();
												out.println(req);
												out.flush();
												System.out.println("Adresse \t \t \t NbPieces \t Loyer \t Type \t Loueur");
												rep=in.readLine();
												System.out.println(	Constantes.reformatString(rep));
												
											};
											break;
											
											//Nombre de pieces 
											case "3":{
												out.println(Constantes.REQ_LOC_LIST_PIECES);
												out.flush();
												System.out.println("Quel est le nombre de pieces?");
												req=clavier.nextLine();
												out.println(req);
												out.flush();
												System.out.println("Adresse \t \t \t NbPieces \t Loyer \t Type \t Loueur");
												rep=in.readLine();
												System.out.println(	Constantes.reformatString(rep));
												};
											break;
											
										}
										
							};
							break;
								
							//Reservation	
							case "2":{
								out.println(Constantes.REQ_LOC_RESERVER);
								out.flush();
								rep=in.readLine();
								//VERIFICATION SI LA LISTE N'EST PAS VIDE
								if (!rep.equals(Constantes.LIST_VIDE)){
									System.out.println(Constantes.reformatString(rep));
									System.out.println("Pour reserver saisissez le numero correspondant à l'appartement dans la liste");
									req=clavier.nextLine();
									out.println(req);
									out.flush();
									System.out.println("Saisir une date de fin de location sous format dd-mm-aaaa");
									req=clavier.nextLine();
									out.println(req);
									out.flush();
									System.out.println(in.readLine());
								}
								else{
									System.out.println("Il n'ya aucun apparetement à louer");
								}
							};
							
							break;
						}
				};
				break;
				
				default:System.out.println("Requete inexistatnte: Ressaisizez votre requete");
				}
			    
			}
			/*-------------------------CONNEXION CLIENT ADMIN------------------- */
			else if (req.equals("2")){
				Socket socket=new Socket(InetAddress.getLocalHost(),Constantes.PORT_ADMIN);
				PrintWriter out= new PrintWriter( socket.getOutputStream());
			    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    out.println(Constantes.PORT_ADMIN);
			    out.flush();
			    //RECUPERATION DES identifiant de l ADMIN POUR LA CONNEXION
				System.out.println("Saisissez le mail administrateur");
				mail=clavier.nextLine();
				System.out.println("Saisissez le mot de passe");
				pass= new String(console.readPassword());
				out.println(mail+" "+pass);
				out.flush();
				clearTerminal();
				rep=in.readLine();
				
				//LOGIN OU MOT DE PASSE FOURNI FAUX
				if (rep.equals(Constantes.LOGIN_NO_OK)) {System.out.println("Mail ou mot de passe ivalide"); return ;}	
				//RECUPERATION DE LA LISTE DE TOUT LES APPARTEMENTS LIBRES
	            rep=in.readLine();
	        	//VERIFICATION SI LA LISTE N'EST PAS VIDE
	            if (!rep.equals(Constantes.LIST_VIDE)){
					System.out.println("|---------------------------------------------------------------|");
			        System.out.println("|                     MENU ADMINISTRATEUR                       |");
			        System.out.println("|---------------------------------------------------------------|");
			        System.out.println("|         Veuillez indiquer l'indice de l'appartement           |");
			        System.out.println("|                          A SUPPRIMER                          |");
			        System.out.println("|---------------------------------------------------------------|");
			        System.out.println("Adresse \t \t \t NbPieces \t Loyer \t Type \t Loueur");		
				    System.out.println(Constantes.reformatString(rep));
					req=clavier.nextLine();
					out.println(req);
					out.flush();
					System.out.println(in.readLine());
	            }
	            //LISTE DES APPARTEMENTS LIBRES VIDES
	            else{
	            	System.out.println("Il n'ya pas de location à supprimer pour le moment");
	            }
			    
			}
			else
			{
			System.out.println("CHOIX INNEXISTANT");	
			} 
			
			

			
		} catch (UnknownHostException e) {
			System.out.println("Client:Hote distante inconnue");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Client:Erreur E/S");
			e.printStackTrace();
		}

	}
	public static void clearTerminal(){
		 System.out.print(" \033[H\033[2J");  
		 System.out.flush();
	}
	
}
