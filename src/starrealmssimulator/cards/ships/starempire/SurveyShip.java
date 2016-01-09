package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class SurveyShip extends Ship implements ScrappableCard
{
    public SurveyShip()
    {
        name = "Survey Ship";
        faction = Faction.STAR_EMPIRE;
        cost = 3;
        set = CardSet.CORE;
        text = "Add 1 Trade; Draw a card; Scrap: Target Opponent discards a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(1);
        player.drawCard();
    }

    @Override
    public void cardScrapped(Player player) {
        player.opponentDiscardsCard();
    }
}
