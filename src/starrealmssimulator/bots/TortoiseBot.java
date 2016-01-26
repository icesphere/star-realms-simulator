package starrealmssimulator.bots;

import starrealmssimulator.bots.strategies.DefenseVelocityStrategy;
import starrealmssimulator.model.Bot;
import starrealmssimulator.model.BotStrategy;
import starrealmssimulator.model.Card;

public class TortoiseBot extends Bot {
    BotStrategy strategy = new DefenseVelocityStrategy();

    @Override
    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }
}
