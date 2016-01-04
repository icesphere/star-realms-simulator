package starrealmssimulator.cards.heroes;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Faction;
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
    public Faction getAlliedFaction() {
        return Faction.BLOB;
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(2);
        player.blobAlliedUntilEndOfTurn();
    }

    @Override
    public int getCombatWhenScrapped() {
        return 2;
    }
}
