package starrealmssimulator.cards.heroes;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Hero;
import starrealmssimulator.model.Player;

public class RamPilot extends Hero {
    public RamPilot() {
        name = "Ram Pilot";
        set = CardSet.CRISIS_HEROES;
        cost = 1;
        text = "Scrap: Add 2 Combat. Until end of turn, you may use all of your Blob Ally abilities";
    }

    @Override
    public void cardScraped(Player player) {
        player.addCombat(2);
        player.blobAlliedUntilEndOfTurn();
    }
}
