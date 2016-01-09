package starrealmssimulator.cards.bases.outposts.starempire;

import starrealmssimulator.model.*;

public class WarWorld extends Outpost implements AlliableCard
{
    public WarWorld()
    {
        name = "War World";
        faction = Faction.STAR_EMPIRE;
        cost = 5;
        set = CardSet.CORE;
        shield = 4;
        text = "Add 3 Combat; Ally: Add 4 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(3);
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(4);
    }
}
