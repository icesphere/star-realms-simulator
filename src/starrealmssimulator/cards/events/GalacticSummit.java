package starrealmssimulator.cards.events;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Event;
import starrealmssimulator.model.Player;

public class GalacticSummit extends Event {
    public GalacticSummit() {
        name = "Galactic Summit";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player gains 7 Authority";
    }

    @Override
    public void handleEvent(Player player) {
        player.addAuthority(7);
        player.getOpponent().addAuthority(7);
    }
}
