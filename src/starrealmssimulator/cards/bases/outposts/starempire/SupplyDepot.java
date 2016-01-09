package starrealmssimulator.cards.bases.outposts.starempire;

import starrealmssimulator.model.*;

import java.util.List;

public class SupplyDepot extends Outpost implements AlliableCard
{
    public SupplyDepot()
    {
        name = "Supply Depot";
        faction = Faction.STAR_EMPIRE;
        cost = 6;
        set = CardSet.COLONY_WARS;
        shield = 5;
        text = "Discard up to two cards. Gain 2 Trade or 2 Combat for each card discarded this way. Ally: Draw a card.";
    }

    @Override
    public void baseUsed(Player player) {
        List<Card> cards = player.getCardsToDiscardForSupplyDepot();
        player.discardCards(cards);

        for (Card ignored : cards) {
            Choice choice1 = new Choice(1, "Add 2 Trade");
            Choice choice2 = new Choice(2, "Add 2 Combat");

            player.makeChoice(this, choice1, choice2);
        }
    }

    @Override
    public void choiceMade(int choice, Player player) {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 2 Trade");
            player.addTrade(2);
        } else if (choice == 2) {
            player.getGame().gameLog("Chose Add 2 Combat");
            player.addCombat(2);
        }
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }
}
