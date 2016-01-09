package starrealmssimulator.cards.bases.outposts.starempire;

import starrealmssimulator.model.*;

public class RoyalRedoubt extends Outpost implements AlliableCard
{
    public RoyalRedoubt()
    {
        name = "Royal Redoubt";
        faction = Faction.STAR_EMPIRE;
        cost = 6;
        set = CardSet.CORE;
        shield = 6;
        text = "Add 3 Combat; Ally: Target opponent discards a card";
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
        player.opponentDiscardsCard();
    }
}
