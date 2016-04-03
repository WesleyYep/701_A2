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

        for (Entry<String, String> state : states.entrySet()) {
            System.out.println(state.getKey() + " " + state.getValue());
        }
    }
}
