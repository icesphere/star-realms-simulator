package starrealmssimulator.cards.bases.blob;

import starrealmssimulator.model.*;

public class StellarReef extends Base implements ScrappableCard
{
    public StellarReef()
    {
        name = "Stellar Reef";
        faction = Faction.BLOB;
        cost = 2;
        set = CardSet.COLONY_WARS;
        shield = 3;
        text = "Add 1 Trade. Scrap: Add 3 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(1);
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.addCombat(3);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 3;
    }
}
