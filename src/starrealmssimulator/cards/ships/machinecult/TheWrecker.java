package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class TheWrecker extends Ship implements AlliableCard
{
    public TheWrecker()
    {
        name = "The Wrecker";
        faction = Faction.MACHINE_CULT;
        cost = 7;
        set = CardSet.COLONY_WARS;
        text = "Add 6 Combat; You may scrap up to two cards in your hand and/or discard pile; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(6);
        player.scrapCardsFromHandOrDiscard(2);
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }
}
