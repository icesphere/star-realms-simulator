package starrealmssimulator.cards.bases.outposts.machinecult;

import starrealmssimulator.model.*;

public class FrontierStation extends Outpost
{
    public FrontierStation()
    {
        name = "Frontier Station";
        faction = Faction.MACHINE_CULT;
        cost = 6;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Add 2 Trade OR Add 3 Combat";
    }

    @Override
    public void baseUsed(Player player)
    {
        Choice choice1 = new Choice(1, "Add 2 Trade");
        Choice choice2 = new Choice(1, "Add 3 Combat");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void choiceMade(int choice, Player player)
    {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 2 Trade");
            player.addTrade(2);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 3 Combat");
            player.addCombat(3);
        }
    }
}
