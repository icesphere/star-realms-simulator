package starrealmssimulator.cards.bases.outposts.machinecult;

import starrealmssimulator.model.*;

public class BorderFort extends Outpost implements AlliableCard
{
    public BorderFort()
    {
        name = "Border Fort";
        faction = Faction.MACHINE_CULT;
        cost = 4;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        shield = 5;
        text = "Add 1 Trade OR Add 2 Combat; Ally: Scrap a card in your hand or discard pile";
    }

    @Override
    public void baseUsed(Player player) {
        Choice choice1 = new Choice(1, "Add 1 Trade");
        Choice choice2 = new Choice(2, "Add 2 Combat");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void choiceMade(int choice, Player player) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 1 Trade");
            player.addTrade(1);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 2 Combat");
            player.addCombat(2);
        }
    }

    public void cardAllied(Player player) {
        player.scrapCardFromHandOrDiscard();
    }
}
