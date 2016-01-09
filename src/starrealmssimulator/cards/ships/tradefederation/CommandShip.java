package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class CommandShip extends Ship implements AlliableCard
{
    public CommandShip()
    {
        name = "Command Ship";
        faction = Faction.TRADE_FEDERATION;
        cost = 8;
        set = CardSet.CORE;
        text = "Add 4 Authority; Add 5 Combat; Draw 2 Cards; Ally: You may destroy target base";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addAuthority(4);
        player.addCombat(5);
        player.drawCards(2);
    }

    @Override
    public void cardAllied(Player player)
    {
        player.destroyTargetBase();
    }
}
