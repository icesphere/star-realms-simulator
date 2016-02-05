package starrealmssimulator.bots;

import starrealmssimulator.bots.strategies.EconomyStrategy;
import starrealmssimulator.model.Bot;
import starrealmssimulator.model.BotStrategy;
import starrealmssimulator.model.Card;

public class EconomyBot extends Bot {
    BotStrategy strategy = new EconomyStrategy();

    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }
}
