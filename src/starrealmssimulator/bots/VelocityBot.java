package starrealmssimulator.bots;

import starrealmssimulator.bots.strategies.VelocityStrategy;
import starrealmssimulator.model.Bot;
import starrealmssimulator.model.BotStrategy;
import starrealmssimulator.model.Card;

public class VelocityBot extends Bot {
    BotStrategy strategy = new VelocityStrategy();

    @Override
    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }
}
