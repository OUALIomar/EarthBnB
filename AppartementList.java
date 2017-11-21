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
public class AppartementList implements DataLists <Appartement>{
	  @XmlElementWrapper(name = "appartements")
      @XmlElement(name = "appartement", type=Appartement.class)

	  private  ArrayList<Appartement> appartements;

	@Override
	public void setList(ArrayList<Appartement> liste) {
	
		this.appartements=liste;
		
	}

	@Override
	public ArrayList<Appartement> getList() {
		
		return this.appartements;
	}

	@Override
	public synchronized void ajouter(Appartement element) {
			
		try {
			this.appartements.add(element);
			ManageData.marshalingData(AppartementList.class, this);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public synchronized void retirer(int index) {
		try {
			this.appartements.remove(index);
			ManageData.marshalingData(AppartementList.class, this);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}
}