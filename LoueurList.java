package Devoir;




import java.util.ArrayList;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LoueurList implements DataLists <Loueur>{
	  @XmlElementWrapper(name = "loueurs")
      @XmlElement(name = "loueur", type=Loueur.class)

	  private  ArrayList<Loueur> loueurs;

	@Override
	public void setList(ArrayList<Loueur> liste) {
	
		this.loueurs=liste;
		
	}

	@Override
	public ArrayList<Loueur> getList() {
		
		return this.loueurs;
	}

	@Override
	public synchronized void ajouter(Loueur element) {
			
		try {
			this.loueurs.add(element);
			ManageData.marshalingData(LoueurList.class, this);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public synchronized void retirer(int index) {
		try {
			this.loueurs.remove(index);
			ManageData.marshalingData(LoueurList.class, this);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}
	




}
