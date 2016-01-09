package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class CargoPod extends Ship implements AlliableCard, ScrappableCard
{
    public CargoPod()
    {
        name = "Cargo Pod";
        faction = Faction.BLOB;
        cost = 3;
        set = CardSet.COLONY_WARS;
        text = "Add 3 Trade; Ally: Add 3 Combat; Scrap: Add 3 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(3);
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(3);
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(3);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 3;
    }
}
