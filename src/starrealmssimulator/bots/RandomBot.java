package starrealmssimulator.bots;

import starrealmssimulator.model.Bot;
import starrealmssimulator.model.Card;

public class RandomBot extends Bot {
    @Override
    public String getPlayerName() {
        return "RandomBot";
    }

    @Override
    public int getBuyCardScore(Card card) {
        return 1;
    }
}
