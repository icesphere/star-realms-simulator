package starrealmssimulator.bots;

import starrealmssimulator.bots.strategies.AttackStrategy;
import starrealmssimulator.model.Bot;
import starrealmssimulator.model.BotStrategy;
import starrealmssimulator.model.Card;

public class AttackBot extends Bot {
    BotStrategy strategy = new AttackStrategy();

    public int getBuyCardScore(Card card) {
        return strategy.getBuyCardScore(card, this);
    }
}
