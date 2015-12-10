package starrealmssimulator.bots;

import starrealmssimulator.model.Bot;
import starrealmssimulator.model.Card;

public class RandomBot extends Bot {

    @Override
    public int getBuyCardScore(Card card) {
        return 1;
    }
}
