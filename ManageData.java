package Devoir;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class ManageData {

	public static void marshalingData(Class classe,Object list) throws JAXBException 
	{
		//RECUPERATION nomPACKAGE+nomDeLaClasseAppelante
		String name=classe.getName();
		//INITIALISATION DU CONTEXTE AVEC LA CLASSE APPELANTE
	    JAXBContext jaxbContext = JAXBContext.newInstance(classe);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	    //Marshalling de la liste dans un fichier XML
	    jaxbMarshaller.marshal(list, new File(Constantes.PATH_XML+name+".xml"));
	}
	
	
	public static Object unMarshalingData(Class classe) throws JAXBException 
	{
		String name=classe.getName();
	    JAXBContext jaxbContext = JAXBContext.newInstance(classe);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	     
	    //Recuperation de la liste sauvegardé dans notre fichier XML
	    Object los = jaxbUnmarshaller.unmarshal( new File(Constantes.PATH_XML+name+".xml") );
	     
	    return los;
	}
	
}
