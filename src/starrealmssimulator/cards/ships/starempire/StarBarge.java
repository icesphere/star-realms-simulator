package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class StarBarge extends Ship implements AlliableCard
{
    public StarBarge()
    {
        name = "Star Barge";
        faction = Faction.STAR_EMPIRE;
        cost = 1;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Trade; Ally: Add 2 Combat; Target Opponent discards a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(2);
        player.opponentDiscardsCard();
    }
}
