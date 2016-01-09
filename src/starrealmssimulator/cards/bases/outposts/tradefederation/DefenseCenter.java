package starrealmssimulator.cards.bases.outposts.tradefederation;

import starrealmssimulator.model.*;

public class DefenseCenter extends Outpost implements AlliableCard
{
    public DefenseCenter()
    {
        name = "Defense Center";
        faction = Faction.TRADE_FEDERATION;
        cost = 5;
        set = CardSet.CORE;
        shield = 5;
        text = "Add 3 Authority OR Add 2 Combat; Ally: Add 2 Combat";
    }

    @Override
    public void baseUsed(Player player)
    {
        Choice choice1 = new Choice(1, "Add 3 Authority");
        Choice choice2 = new Choice(1, "Add 2 Combat");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void choiceMade(int choice, Player player)
    {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 3 Authority");
            player.addAuthority(3);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 2 Combat");
            player.addCombat(2);
        }
    }

    @Override
    public void cardAllied(Player player)
    {
        player.addCombat(2);
    }
}
