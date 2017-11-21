package Devoir;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.timer.TimerMBean;
import javax.xml.bind.JAXBException;

//Classe interne serveur
class Serveur implements Runnable{
	 private int port;
	 private ServerSocket serveur;
	 private ExecutorService pool;
	 public  static LocataireList locataireslist;
	 public  static LoueurList loueurslist ;
	 public  static AppartementList appartementslist;
	 public  static LocationList locationslist ;
	 public  static ComptesMap comptesmap ;
	
	 public Serveur(int port, ExecutorService pool){
		 this.port = port;
	        try {
	            this.serveur = new ServerSocket(port);
	            this.pool = pool;
	            //Chargement des données sur chaque serveur (admin et simple)
	            locataireslist = (LocataireList) ManageData.unMarshalingData(LocataireList.class);
				loueurslist = (LoueurList) ManageData.unMarshalingData(LoueurList.class);
				appartementslist = (AppartementList) ManageData.unMarshalingData(AppartementList.class);
				locationslist = (LocationList) ManageData.unMarshalingData(LocationList.class);
				comptesmap = (ComptesMap) ManageData.unMarshalingData(ComptesMap.class);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }

	@Override
	public void run() {
	    try {
            while (true) {
                pool.execute(new Esclave(serveur.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
}

// TimerTask de mise à jour de la liste des locations selon les date de debut et de fin
class Tache extends TimerTask {

	@Override
	public void run() {
		//PARCOURS DE LA LISTE DES LOCATIONS
		for (Location loc : Serveur.locationslist.getList()){
			//RECUPERATION DE L'INDEX DE LA LOCATION
			int index=Serveur.locationslist.getList().indexOf(loc);
			//RECUPERATION DE LA DATE DU JOUR
			Date today=new Date();
			//SI DATE FIN DE LOCATION EGALÉ OU DEPASSÉ 
			if (loc.getDateFin().compareTo(today)<=0){
				//SUPPRIMER LA LOCATION DE LA LISTE
				Serveur.locationslist.retirer(index);
				//AJOUT DE L'APPART AUX APPARTS DISPO
				Serveur.appartementslist.ajouter(loc.getAppartement());
			}
		}
		
	}	
}

public class Maitre {
	
	public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(Constantes.POOLSIZE);
        //LANCEMENT DU SERVEUR CLIENT ORDINAIRE
        Thread client = new Thread(new Serveur(Constantes.PORT_CLIENT, pool));
        //LANCEMENT DU SERVEUR CLIENT ADMIN
        Thread administarateur = new Thread(new Serveur(Constantes.PORT_ADMIN, pool));
        client.start();
        administarateur.start();
    
        System.out.println("Server à l'ecoute");
        
        Timer scheduler = new Timer();
       	
        //Definition de la date de mise à à jour periodique des locations 23H:59m:59s:999ms
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND,999);
        Date time = calendar.getTime();
        
        //Delai de 24 heures en millisecondes 
        long delay=1000 * 60 * 60 * 24;
        
        //lancment de la tache periodique 24H À PARTIR DU TIME DEFINI 
        scheduler.schedule(new Tache(), time, delay);
       }
	


}
