package starrealmssimulator.cards.events;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Event;
import starrealmssimulator.model.Player;

public class Quasar extends Event {
    public Quasar() {
        name = "Quasar";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player draws two cards";
    }

    @Override
    public void handleEvent(Player player) {
        player.drawCards(2);
        player.getOpponent().drawCards(2);
    }
}
