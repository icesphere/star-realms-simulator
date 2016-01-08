package starrealmssimulator.cards.events;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Event;
import starrealmssimulator.model.Player;

public class Comet extends Event {
    public Comet() {
        name = "Comet";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player may scrap up to two cards in their hand or discard pile";
    }

    @Override
    public void handleEvent(Player player) {
        player.scrapCardsFromHandOrDiscard(2);
        player.getOpponent().scrapCardsFromHandOrDiscard(2);
    }
}
