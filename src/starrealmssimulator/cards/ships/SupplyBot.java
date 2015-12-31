package starrealmssimulator.cards.ships;

import starrealmssimulator.model.*;

public class SupplyBot extends Ship implements AlliableCard
{
    public SupplyBot()
    {
        name = "Supply Bot";
        faction = Faction.MACHINE_CULT;
        cost = 3;
        set = CardSet.CORE;
        text = "Add 2 Trade; You may scrap a card in your hand or discard pile; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
        player.scrapCard();
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(2);
    }
}
