package starrealmssimulator.bots;

import starrealmssimulator.model.Bot;
import starrealmssimulator.model.Card;

public class SmartBot extends Bot {

    @Override
    public int getBuyCardScore(Card card) {
        //todo

        return card.getCost();
    }
}
