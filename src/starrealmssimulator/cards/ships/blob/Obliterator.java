package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class Obliterator extends Ship implements AlliableCard
{
    public Obliterator()
    {
        name = "Obliterator";
        faction = Faction.BLOB;
        cost = 6;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        text = "Add 7 Combat; If your opponent has two or more bases in play, gain 6 Combat; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(7);
        if (player.getOpponent().getBases().size() >= 2) {
            player.addCombat(6);
        }
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }
}
