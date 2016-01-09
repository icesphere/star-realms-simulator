package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class Corvette extends Ship implements AlliableCard
{
    public Corvette()
    {
        name = "Corvette";
        faction = Faction.STAR_EMPIRE;
        cost = 2;
        set = CardSet.CORE;
        text = "Add 1 Combat; Draw a card; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(1);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player)
    {
        player.addCombat(2);
    }
}
