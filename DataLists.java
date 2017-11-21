package Devoir;

import java.util.ArrayList;
import java.util.List;

public interface DataLists <V> {
		
		public  void setList( ArrayList<V> liste);
	
		public   ArrayList<V> getList() ;
	  
	  
		public  void ajouter(V element);
		
		public  void retirer(int element);
		
		
		
}
