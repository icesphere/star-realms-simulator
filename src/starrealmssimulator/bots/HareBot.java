package starrealmssimulator.bots;

import starrealmssimulator.bots.strategies.AttackVelocityStrategy;
import starrealmssimulator.model.Bot;
import starrealmssimulator.model.BotStrategy;
import starrealmssimulator.model.Card;

public class HareBot extends Bot {
    BotStrategy strategy = new AttackVelocityStrategy();

    @Override
    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }
}
