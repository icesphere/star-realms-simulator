package starrealmssimulator.cards.events;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Event;
import starrealmssimulator.model.Player;

public class Supernova extends Event {
    public Supernova() {
        name = "Supernova";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player loses 5 Authority; Scrap all cards in the trade row";
    }

    @Override
    public void handleEvent(Player player) {
        player.reduceAuthority(5);
        player.getOpponent().reduceAuthority(5);
        player.getGame().scrapAllCardsFromTradeRow();
    }
}
