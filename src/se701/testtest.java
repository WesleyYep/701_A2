package se701;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class testtest {

	public static void main(String[] args){ 
		sampleMethod(); 
	} 

 	public static void sampleMethod() {
//		Map states = new HashMap {
//		    "AL" : "Alabama",
//		    "AK" : "Alaska",
//		    "AZ" : "Arizona", 
//		    "WY" : "Wyoming"
//		}; 
 		Map<String, String> states = new HashMap<String, String>();
 		states.put("AL", "Alabama");
 		states.put("AK", "Alaska");
 		states.put("AZ", "Arizona");
 		states.put("WY", "Wyoming");
	 		
		for(Entry<String, String> state : states.entrySet()){
			System.out.println(state.getKey() + " " + state.getValue());
		}
	}

}
