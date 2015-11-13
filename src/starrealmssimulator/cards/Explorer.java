package starrealmssimulator.cards;

import starrealmssimulator.model.*;

public class Explorer extends Ship implements ScrapableCard
{
    public Explorer()
    {
        name = "Explorer";
        faction = Faction.UNALIGNED;
        cost = 2;
        set = CardSet.CORE;
        text = "Add 2 Trade; Scrap: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addTrade(2);
    }

    @Override
    public void cardScraped(Player player)
    {
        player.addCombat(2);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 2;
    }
}
