package se701;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StudentSample {

    public static void main(String[] args) {
        sampleMethod();
    }

    public static void sampleMethod() {
        Map<String, String> states = new HashMap<String, String>();
        states.put("AL", "Alabama");
        states.put("AK", "Alaska");
        states.put("AZ", "Arizona");
        states.put("WY", "Wyoming");

        for (Entry<String, String> state : states.entrySet()) {
            System.out.println(state.getKey() + " " + state.getValue());
        }
    }
}
