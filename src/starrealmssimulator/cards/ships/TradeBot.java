package starrealmssimulator.cards.ships;

import starrealmssimulator.model.*;

public class TradeBot extends Ship implements AlliableCard
{
    public TradeBot()
    {
        name = "Trade Bot";
        faction = Faction.MACHINE_CULT;
        cost = 1;
        set = CardSet.CORE;
        text = "Add 1 Trade; You may scrap a card in your hand or discard pile; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(1);
        player.scrapCard();
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(2);
    }
}