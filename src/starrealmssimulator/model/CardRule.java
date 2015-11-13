package starrealmssimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardRule {
    private Map<String, String> ruleMap = new HashMap<>();

    private List<String> ruleKeys = new ArrayList<>();

    private String score;

    private int choice;

    public void addRuleValue(String key, String value) {
        if (key.equalsIgnoreCase("score")) {
            score = value;
        } else if (key.equalsIgnoreCase("choice")) {
            choice = Integer.parseInt(value);
        } else {
            ruleKeys.add(key);
        }
        ruleMap.put(key, value);
    }

    public String getRuleValue(String key) {
        return ruleMap.get(key);
    }

    public String getScore() {
        return score;
    }

    public int getChoice() {
        return choice;
    }

    public List<String> getRuleKeys() {
        return ruleKeys;
    }
}
