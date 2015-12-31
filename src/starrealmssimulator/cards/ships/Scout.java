package starrealmssimulator.cards.ships;

import starrealmssimulator.model.*;

public class Scout extends Ship
{
    public Scout()
    {
        name = "Scout";
        faction = Faction.UNALIGNED;
        cost = 0;
        set = CardSet.CORE;
        text = "Add 1 Trade";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(1);
    }
}
