package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class PatrolBot extends Ship implements AlliableCard
{
    public PatrolBot()
    {
        name = "Patrol Bot";
        faction = Faction.MACHINE_CULT;
        cost = 2;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        text = "Add 2 Trade OR Add 4 Combat; Ally: You may scrap a card in your hand or discard pile";
    }

    @Override
    public void cardPlayed(Player player) {
        Choice choice1 = new Choice(1, "Add 2 Trade");
        Choice choice2 = new Choice(2, "Add 4 Combat");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void choiceMade(int choice, Player player) {
        player.getGame().gameLog("Chose Add 2 Trade");
        if (choice == 1) {
            player.addTrade(2);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 4 Combat");
            player.addCombat(4);
        }
    }

    @Override
    public void cardAllied(Player player) {
        player.scrapCardFromHandOrDiscard();
    }
}
