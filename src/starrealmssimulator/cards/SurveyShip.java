package starrealmssimulator.cards;

import starrealmssimulator.model.*;

public class SurveyShip extends Ship implements ScrapableCard
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
    public void cardScraped(Player player) {
        player.opponentDiscardsCard();
    }
}
