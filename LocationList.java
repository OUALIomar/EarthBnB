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
public class LocationList implements DataLists <Location>{
	  @XmlElementWrapper(name = "locations")
      @XmlElement(name = "location", type=Location.class)

	  private  ArrayList<Location> locations;

	@Override
	public void setList(ArrayList<Location> liste) {
	
		this.locations=liste;
		
	}

	@Override
	public ArrayList<Location> getList() {
		
		return this.locations;
	}

	@Override
	public synchronized void ajouter(Location element) {
			
		try {
			this.locations.add(element);
			ManageData.marshalingData(LocationList.class, this);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public synchronized void retirer(int index) {
		try {
			this.locations.remove(index);
			ManageData.marshalingData(LocationList.class, this);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
		
		