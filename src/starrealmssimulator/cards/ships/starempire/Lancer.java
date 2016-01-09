package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class Lancer extends Ship implements AlliableCard
{
    public Lancer()
    {
        name = "Lancer";
        faction = Faction.STAR_EMPIRE;
        cost = 2;
        set = CardSet.COLONY_WARS;
        text = "Add 4 Combat; If an opponent controls a base, gain an additional 2 Combat; Ally: Target Opponent discards a card";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(4);
        if (!player.getOpponent().getBases().isEmpty()) {
            player.addCombat(2);
        }
    }

    @Override
    public void cardAllied(Player player)
    {
        player.opponentDiscardsCard();
    }
}
