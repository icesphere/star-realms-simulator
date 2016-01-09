package starrealmssimulator.cards.bases.tradefederation;

import starrealmssimulator.model.*;

public class Starmarket extends Base
{
    public Starmarket()
    {
        name = "Starmarket";
        faction = Faction.TRADE_FEDERATION;
        cost = 4;
        set = CardSet.PROMO_YEAR_1;
        shield = 6;
        text = "If you played a base this turn (including this one), gain 5 Authority OR 3 Trade";
    }

    @Override
    public void baseUsed(Player player) {
        Choice choice1 = new Choice(1, "Add 5 Authority");
        Choice choice2 = new Choice(2, "Add 3 Trade");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void choiceMade(int choice, Player player) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 5 Authority");
            player.addAuthority(5);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 3 Trade");
            player.addTrade(3);
        }
    }

    @Override
    public boolean baseCanBeUsed(Player player) {
        return player.basePlayedThisTurn();
    }
}
