package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class Megahauler extends Ship implements AlliableCard
{
    public Megahauler()
    {
        name = "Megahauler";
        faction = Faction.TRADE_FEDERATION;
        cost = 7;
        set = CardSet.PROMO_YEAR_1;
        text = "Add 5 Authority; Acquire any ship without paying its cost and put it on top of your deck; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addAuthority(5);
        player.acquireFreeShipAndPutOnTopOfDeck();
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }
}
