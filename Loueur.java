package Devoir;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Loueur extends Personne {
	
	
	
	public Loueur( ) {
		super();
	}

	
	public Loueur( String mailaddr) {
		super( mailaddr);
		// TODO Auto-generated constructor stub
	}

}
