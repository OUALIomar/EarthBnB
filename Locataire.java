package Devoir;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)

public class Locataire extends Personne {
	
	
	
	public Locataire() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Locataire( String mailaddr) {
		super(mailaddr);
		// TODO Auto-generated constructor stub
	}
	
	public static void initListLocataires(){
		
		

	}

}
