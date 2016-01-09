package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class CustomsFrigate extends Ship implements AlliableCard, ScrappableCard
{
    public CustomsFrigate()
    {
        name = "Customs Frigate";
        faction = Faction.TRADE_FEDERATION;
        cost = 4;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        text = "You may acquire a ship of cost four or less and put it on top of your deck; Ally: Add 4 Combat; Scrap: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.acquireFreeCardToTopOfDeck(4);
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(4);
    }

    @Override
    public void cardScrapped(Player player) {
        player.drawCard();
    }
}
