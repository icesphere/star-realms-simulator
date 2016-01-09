package starrealmssimulator.cards.bases.outposts.starempire;

import starrealmssimulator.model.*;

public class ImperialPalace extends Outpost implements AlliableCard
{
    public ImperialPalace()
    {
        name = "Imperial Palace";
        faction = Faction.STAR_EMPIRE;
        cost = 7;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Draw a card. Target opponent discards a card. Ally: Add 4 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.drawCard();
        player.opponentDiscardsCard();
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(4);
    }
}
