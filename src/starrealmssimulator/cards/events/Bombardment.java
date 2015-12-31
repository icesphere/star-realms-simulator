package starrealmssimulator.cards.events;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Event;
import starrealmssimulator.model.Player;

public class Bombardment extends Event {
    public Bombardment() {
        name = "Bombardment";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player either destroys a base they control or loses 6 Authority";
    }

    @Override
    public void handleEvent(Player player) {
        player.handleBombardment();
        player.getOpponent().handleBombardment();
    }
}
