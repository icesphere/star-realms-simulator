package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class Mothership extends Ship implements AlliableCard
{
    public Mothership()
    {
        name = "Mothership";
        faction = Faction.BLOB;
        cost = 7;
        set = CardSet.CORE;
        text = "Add 6 Combat; Draw a card; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(6);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }
}
