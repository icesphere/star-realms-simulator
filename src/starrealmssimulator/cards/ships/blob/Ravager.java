package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class Ravager extends Ship
{
    public Ravager()
    {
        name = "Ravager";
        faction = Faction.BLOB;
        cost = 3;
        set = CardSet.COLONY_WARS;
        text = "Add 6 Combat; You may scrap up to two cards that are currently in the trade row";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(6);
        player.scrapCardsInTradeRow(2);
    }
}
