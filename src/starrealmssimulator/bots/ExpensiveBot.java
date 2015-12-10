package starrealmssimulator.bots;

import starrealmssimulator.model.Bot;
import starrealmssimulator.model.Card;

public class ExpensiveBot extends Bot {
    @Override
    public int getBuyCardScore(Card card) {
        return card.getCost() * card.getCost();
    }
}
