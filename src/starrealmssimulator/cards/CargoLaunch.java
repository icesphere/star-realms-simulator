package starrealmssimulator.cards;

import starrealmssimulator.model.*;

public class CargoLaunch extends Ship implements ScrapableCard
{
    public CargoLaunch()
    {
        name = "Cargo Launch";
        faction = Faction.STAR_EMPIRE;
        cost = 1;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        text = "Draw a card; Scrap: Add 1 Trade";
    }

    @Override
    public void cardPlayed(Player player) {
        player.drawCard();
    }

    @Override
    public void cardScraped(Player player) {
        player.addTrade(1);
    }

    @Override
    public int getTradeWhenScrapped() {
        return 1;
    }
}