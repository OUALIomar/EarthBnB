package Devoir;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Location {
	
	
	
	private Locataire locataire;
	private Appartement appartement;
	private Date datefin;
	
	public Location() {
		super();
		
	}



	public Location( Loueur loueur, Locataire locataire, Appartement appartement,Date datefin) {
	
	
		this.locataire = locataire;
		this.appartement = appartement;
		this.datefin=datefin;
	}



	public Locataire getLocataire() {
		return this.locataire;
	}

	public Appartement getAppartement() {
		return this.appartement;
	}
	
	public Date getDateFin() {
		return this.datefin;
	}
	
}
