package Devoir;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType (name = "Type")
@XmlEnum (String.class)
public  enum Type {
	@XmlEnumValue("duplex") duplex ("duplex"),
	@XmlEnumValue("loft") loft ("loft"),
	@XmlEnumValue("chambre") chambre("chambre"),
	@XmlEnumValue("autre") autre("autre");
	
	private final String type;
	Type(String type){
		this.type=type;
	}
	
	public String toString(){
	    return type;
	  }
		
}