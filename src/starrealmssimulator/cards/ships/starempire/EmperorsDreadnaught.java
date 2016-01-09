package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class EmperorsDreadnaught extends Ship
{
    public EmperorsDreadnaught()
    {
        name = "Emperor's Dreadnaught";
        faction = Faction.STAR_EMPIRE;
        cost = 8;
        set = CardSet.COLONY_WARS;
        text = "Add 8 Combat; Draw a card; Target opponent discards a card; When you acquire this card, if you've played a Star Empire card this turn, you may put this card directly into your hand";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(8);
        player.drawCard();
        player.opponentDiscardsCard();
    }
}
