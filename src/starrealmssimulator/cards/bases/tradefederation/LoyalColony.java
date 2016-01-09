package starrealmssimulator.cards.bases.tradefederation;

import starrealmssimulator.model.Base;
import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Faction;
import starrealmssimulator.model.Player;

public class LoyalColony extends Base
{
    public LoyalColony()
    {
        name = "Loyal Colony";
        faction = Faction.TRADE_FEDERATION;
        cost = 7;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Add 3 Trade. Add 3 Combat. Add 3 Authority.";
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(3);
        player.addCombat(3);
        player.addAuthority(3);
    }
}
