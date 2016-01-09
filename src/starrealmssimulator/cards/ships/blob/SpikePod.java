package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class SpikePod extends Ship implements ScrappableCard
{
    public SpikePod()
    {
        name = "Spike Pod";
        faction = Faction.BLOB;
        cost = 1;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        text = "Add 3 Combat; You may scrap up to two cards currently in the Trade Row; Scrap: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(3);
        player.scrapCardsInTradeRow(2);
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(2);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 2;
    }
}
