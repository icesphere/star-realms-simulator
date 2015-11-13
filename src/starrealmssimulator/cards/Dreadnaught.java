package starrealmssimulator.cards;

import starrealmssimulator.model.*;

public class Dreadnaught extends Ship implements ScrapableCard
{
    public Dreadnaught()
    {
        name = "Dreadnaught";
        faction = Faction.STAR_EMPIRE;
        cost = 7;
        set = CardSet.CORE;
        text = "Add 7 Combat; Draw a card; Scrap: Add 5 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(7);
        player.drawCard();
    }

    @Override
    public void cardScraped(Player player)
    {
        player.addCombat(5);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 5;
    }
}
