package starrealmssimulator.cards.heroes;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Hero;
import starrealmssimulator.model.Player;

public class BlobOverlord extends Hero {
    public BlobOverlord() {
        name = "Blob Overlord";
        set = CardSet.CRISIS_HEROES;
        cost = 2;
        text = "Scrap: Add 4 Combat. Until end of turn, you may use all of your Blob Ally abilities";
    }

    @Override
    public void cardScraped(Player player) {
        player.addCombat(4);
        player.blobAlliedUntilEndOfTurn();
    }
}
