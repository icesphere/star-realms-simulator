package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class BattlePod extends Ship implements AlliableCard
{
    public BattlePod()
    {
        name = "Battle Pod";
        faction = Faction.BLOB;
        cost = 2;
        set = CardSet.CORE;
        text = "Add 4 Combat; You may scrap a card in the trade row; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(4);
        player.scrapCardInTradeRow();
    }

    @Override
    public void cardAllied(Player player)
    {
        player.addCombat(2);
    }
}
