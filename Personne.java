package Devoir;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public abstract class Personne {

	private String mailaddr;
	
	
	@XmlTransient
	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
			Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,6})$",Pattern.CASE_INSENSITIVE);

	
	public Personne() {
	
	}

	public Personne(String mailaddr) throws WrongMailAddressException {
	
		if(!isValidMailAddr(mailaddr))
			throw new WrongMailAddressException("Mail erroné");
		
		this.mailaddr=mailaddr;
		
	}

	@Override
	public String toString() {
		return  this.mailaddr;
	}


	public String getMailaddr() {
		return mailaddr;
	}
	
	
	public  static Boolean isValidMailAddr(String mail){
		
		return VALID_EMAIL_ADDRESS_REGEX.matcher(mail).find();
		
	}
	
	
	//fonction de calcul d'empreinte MD5
	public static String generateMD5(String password){
    
        MessageDigest md;
        String generatedpass="";
		try {
		md = MessageDigest.getInstance("MD5");

        md.update(password.getBytes());

        byte[] bytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        generatedpass=sb.toString();
        
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return generatedpass;
		
	}
	
}


