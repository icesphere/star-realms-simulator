package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class HeavyCruiser extends Ship implements AlliableCard
{
    public HeavyCruiser()
    {
        name = "Heavy Cruiser";
        faction = Faction.STAR_EMPIRE;
        cost = 5;
        set = CardSet.COLONY_WARS;
        text = "Add 4 Combat; Draw a card; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(4);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player)
    {
        player.drawCard();
    }
}
