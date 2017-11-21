package Devoir;


import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ComptesMap {
	
	@XmlElementWrapper(name = "comptes")
	private ConcurrentHashMap<String, String> comptes;
 
    public ConcurrentHashMap<String, String> getComptesMap() {
        return this.comptes;
    }
 
    public void setComptesMap(ConcurrentHashMap<String, String> comptes) {
        this.comptes= comptes;
    }
    
    
	public void ajouter(String login,String mdp) {
		
		try {
			this.comptes.put(login, mdp);
			ManageData.marshalingData(ComptesMap.class, this);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}


}