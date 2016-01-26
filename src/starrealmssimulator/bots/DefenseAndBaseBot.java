package starrealmssimulator.bots;

import starrealmssimulator.bots.strategies.DefenseStrategy;
import starrealmssimulator.model.Bot;
import starrealmssimulator.model.BotStrategy;
import starrealmssimulator.model.Card;

public class DefenseAndBaseBot extends Bot {
    BotStrategy strategy = new DefenseStrategy();

    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }
}
