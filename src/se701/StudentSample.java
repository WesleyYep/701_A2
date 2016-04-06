package se701;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StudentSample {

    public static void main(String[] args) {
        sampleMethod();
    }

    public static void sampleMethod() {
        Map<Integer, Character> states = new HashMap<Integer, Character>();
        states.put(34, 'c');
        states.put(21234, 's');
        states.put(213, 's');
        states.put(523, 'f');

        for (Entry<Integer, Character> state : states.entrySet()) {
            System.out.println(state.getKey() + " " + state.getValue());
        }
        Map<String, String> moreStates = new HashMap<String, String>();
        moreStates.put("AL", "Alabama");
        moreStates.put("AK", "Alaska");
        moreStates.put("AZ", "Arizona");
        moreStates.put("WY", "Wyoming");

        for (Entry<String, String> state : moreStates.entrySet()) {
            System.out.println(state.getKey() + " " + state.getValue());
        }
    }
}
