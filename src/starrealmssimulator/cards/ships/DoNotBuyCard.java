package starrealmssimulator.cards.ships;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Faction;
import starrealmssimulator.model.Player;
import starrealmssimulator.model.Ship;

public class DoNotBuyCard extends Ship
{
    public DoNotBuyCard()
    {
        name = "Do Not Buy Card";
        faction = Faction.UNALIGNED;
        cost = 0;
        set = CardSet.CORE;
        text = "This is a placeholder to show simulation results of not buying card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(1);
    }
}
