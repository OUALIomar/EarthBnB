package Devoir;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Adresse {
	

	private String rue;
	private int codepostal;
	
	public Adresse()  {
		super();			
	}

	public Adresse(String rue,int codepostal) throws WrongNumberOfDigitsException {
		this.rue=rue;
		
		if (!checkCodePostal(codepostal))
			throw new WrongNumberOfDigitsException("Code postal erroné");
		
		this.codepostal=codepostal;				
	}

	// getters
	public String getRue() {
		return rue;
	}
	

	public int getCodepostal() {
		return codepostal;
	}
	
	public String toString(){
		return this.rue+","+this.codepostal;
		
	}
	
	//fonction qui verifie le code postale on utilisant un logarithme de base 10
	private Boolean checkCodePostal(int cp){
	   return (int)(Math.log10(cp)+1)==5;
	}


}
