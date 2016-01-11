package starrealmssimulator.cards.bases.blob;

import starrealmssimulator.model.*;

public class BlobWorld extends Base
{
    public BlobWorld()
    {
        name = "Blob World";
        faction = Faction.BLOB;
        cost = 8;
        set = CardSet.CORE;
        shield = 7;
        text = "Add 5 Combat OR Draw a card for each Blob card that you've played this turn";
    }

    @Override
    public void baseUsed(Player player)
    {
        Choice choice1 = new Choice(1, "Add 5 Combat");
        Choice choice2 = new Choice(1, "Draw a card for each Blob card that you've played this turn");
        player.makeChoice(this, choice1, choice2);
    }

    @Override
    public void choiceMade(int choice, Player player)
    {
        if (choice == 1) {
            player.getGame().gameLog("Chose Add 5 Combat");
            player.addCombat(5);
        } else {
            player.getGame().gameLog("Chose Draw a card for each Blob card that you've played this turn");
            int numBlobCardsPlayedThisTurn = player.getNumBlobCardsPlayedThisTurn();
            player.drawCards(numBlobCardsPlayedThisTurn);
        }
    }
}
