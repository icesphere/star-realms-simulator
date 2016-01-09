package starrealmssimulator.cards.bases.outposts.starempire;

import starrealmssimulator.model.*;

public class FighterBase extends Outpost implements AlliableCard
{
    public FighterBase()
    {
        name = "Fighter Base";
        faction = Faction.STAR_EMPIRE;
        cost = 3;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        shield = 5;
        text = "Ally: Target opponent discards a card";
    }

    @Override
    public void cardPlayed(Player player) {
    }

    @Override
    public void baseUsed(Player player) {

    }

    @Override
    public void cardAllied(Player player) {
        player.opponentDiscardsCard();
    }
}
