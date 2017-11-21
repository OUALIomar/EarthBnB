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
public class LocataireList implements DataLists <Locataire>{
	  @XmlElementWrapper(name = "locataires")
      @XmlElement(name = "locataire", type=Locataire.class)

	  private  ArrayList<Locataire> locataires;

	@Override
	public void setList(ArrayList<Locataire> liste) {
	
		this.locataires=liste;
		
	}

	@Override
	public ArrayList<Locataire> getList() {
		
		return this.locataires;
	}

	@Override
	public synchronized void ajouter(Locataire element) {
			
		try {
			this.locataires.add(element);
			ManageData.marshalingData(LocataireList.class, this);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public synchronized void retirer(int index) {
		try {
			this.locataires.remove(index);
			ManageData.marshalingData(LocataireList.class, this);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}