package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class ColonySeedShip extends Ship
{
    public ColonySeedShip()
    {
        name = "Colony Seed Ship";
        faction = Faction.TRADE_FEDERATION;
        cost = 5;
        set = CardSet.COLONY_WARS;
        text = "Add 3 Trade; Add 3 Combat; Add 3 Authority; When you acquire this card, if you've played a Trade Federation card this turn, you may put this card directly into your hand";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addTrade(3);
        player.addCombat(3);
        player.addAuthority(3);
    }
}
