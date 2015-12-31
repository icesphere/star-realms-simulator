package starrealmssimulator.cards.events;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Event;
import starrealmssimulator.model.Player;

public class BlackHole extends Event {
    public BlackHole() {
        name = "Black Hole";
        set = CardSet.CRISIS_EVENTS;
        text = "Each player may discard up to two cards. For each card less than two that a player discards, that player loses 4 Authority";
    }

    @Override
    public void handleEvent(Player player) {
        player.handleBlackHole();
        player.getOpponent().handleBlackHole();
    }
}
