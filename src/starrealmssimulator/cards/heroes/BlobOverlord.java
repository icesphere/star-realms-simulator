package starrealmssimulator.cards.heroes;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Faction;
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
    public Faction getAlliedFaction() {
        return Faction.BLOB;
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(4);
        player.blobAlliedUntilEndOfTurn();
    }

    @Override
    public int getCombatWhenScrapped() {
        return 4;
    }
}
