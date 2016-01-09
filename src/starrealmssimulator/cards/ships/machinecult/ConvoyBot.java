package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class ConvoyBot extends Ship implements AlliableCard
{
    public ConvoyBot()
    {
        name = "Convoy Bot";
        faction = Faction.MACHINE_CULT;
        cost = 3;
        set = CardSet.COLONY_WARS;
        text = "Add 4 Combat; You may scrap a card in your hand or discard pile; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(4);
        player.scrapCardFromHandOrDiscard();
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(2);
    }
}
