package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class ImperialFighter extends Ship implements AlliableCard
{
    public ImperialFighter()
    {
        name = "Imperial Fighter";
        faction = Faction.STAR_EMPIRE;
        cost = 1;
        set = CardSet.CORE;
        text = "Add 2 Combat; Target Opponent discards a card; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(2);
        player.opponentDiscardsCard();
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(2);
    }
}
