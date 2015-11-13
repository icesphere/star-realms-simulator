package starrealmssimulator.cards;

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
        player.addCombat(3);
    }

    @Override
    public void baseUsed(Player player) {

    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(4);
    }
}
