package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class Falcon extends Ship implements ScrappableCard
{
    public Falcon()
    {
        name = "Falcon";
        faction = Faction.STAR_EMPIRE;
        cost = 3;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Combat; Draw a card; Scrap: Target Opponent discards a card";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(2);
        player.drawCard();
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.opponentDiscardsCard();
    }
}
