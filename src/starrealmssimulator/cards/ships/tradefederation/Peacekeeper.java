package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class Peacekeeper extends Ship implements AlliableCard
{
    public Peacekeeper()
    {
        name = "Peacekeeper";
        faction = Faction.TRADE_FEDERATION;
        cost = 6;
        set = CardSet.COLONY_WARS;
        text = "Add 6 Combat; Add 6 Authority; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(6);
        player.addAuthority(6);
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }
}
