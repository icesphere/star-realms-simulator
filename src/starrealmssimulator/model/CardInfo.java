package starrealmssimulator.model;

import java.util.HashMap;
import java.util.Map;

public class CardInfo {
    Map<String, CardRules> infoMap = new HashMap<>();

    public void addInfoValue(String key, CardRules cardRules) {
        infoMap.put(key, cardRules);
    }

    public CardRules getInfoCardRules(String key) {
        if (!infoMap.containsKey(key)) {
            return null;
        }

        return infoMap.get(key);
    }
}
