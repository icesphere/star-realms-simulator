package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class Parasite extends Ship implements AlliableCard
{
    public Parasite()
    {
        name = "Parasite";
        faction = Faction.BLOB;
        cost = 5;
        set = CardSet.COLONY_WARS;
        text = "Add 6 Combat OR Acquire a card of six or less for free; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player)
    {
        Choice choice1 = new Choice(1, "Add 6 Combat");
        Choice choice2 = new Choice(2, "Acquire a card of six or less for free");

        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void choiceMade(int choice, Player player) {
        player.getGame().gameLog("Chose Add 6 Combat");
        if (choice == 1) {
            player.addCombat(6);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Acquire a card of six or less for free");
            player.acquireFreeCard(6);
        }
    }

    @Override
    public void cardAllied(Player player)
    {
        player.drawCard();
    }
}
