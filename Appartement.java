package Devoir;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Appartement {
	//Classe encapsulant la liste des appartement
	
	
	private Adresse adresse ;
	private int nbpiece;
	private int loyermensuel ;
	private Type type;
	private Loueur loueur;
	
	
	public Appartement ()  {
		super();			
	}
	
	//la liste des appartements gerés par l'agence
	
	
	public Appartement(Adresse adresse, int nbpiece, int loyermensuel, Type type, Loueur loueur) throws NegativeArgumentException {
		if (nbpiece<=0)
			throw new NegativeArgumentException("le nombre de piece doit étre stictement positif");
		this.nbpiece = nbpiece;
		if (loyermensuel<=0) 
			throw new NegativeArgumentException("le nombre de piece doit étre stictement positif");
		this.loyermensuel = loyermensuel;
		this.adresse = adresse;
		this.type = type;
		this.loueur = loueur;
	}

	public Adresse getAdresse() {
		return adresse;
	}

	public int getNbpiece() {
		return nbpiece;
	}

	public int getLoyermensuel() {
		return loyermensuel;
	}

	public Type getType() {
		return type;
	}

	public Loueur getLouer() {
		return loueur;
	}
	

	
	public String toString(){
		return 
				this.adresse.toString()+"\t \t "+this.nbpiece+"\t \t "+this.loyermensuel+"\t"+this.type.toString()+"\t"+this.loueur.toString();
	}

	
}
