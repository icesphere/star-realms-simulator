package starrealmssimulator.cards.bases.outposts.starempire;

import starrealmssimulator.model.*;

public class SpaceStation extends Outpost implements ScrappableCard, AlliableCard
{
    public SpaceStation()
    {
        name = "Space Station";
        faction = Faction.STAR_EMPIRE;
        cost = 4;
        set = CardSet.CORE;
        shield = 4;
        text = "Add 2 Combat; Ally: Add 2 Combat; Scrap: Add 4 Trade";
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(2);
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(2);
    }

    @Override
    public void cardScrapped(Player player) {
        player.addTrade(4);
    }

    @Override
    public int getTradeWhenScrapped() {
        return 4;
    }
}
