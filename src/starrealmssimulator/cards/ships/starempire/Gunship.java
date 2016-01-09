package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class Gunship extends Ship implements ScrappableCard
{
    public Gunship()
    {
        name = "Gunship";
        faction = Faction.STAR_EMPIRE;
        cost = 4;
        set = CardSet.COLONY_WARS;
        text = "Add 5 Combat; Target Opponent discards a card; Scrap: Add 4 Trade";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);
        player.opponentDiscardsCard();
    }

    @Override
    public void cardScrapped(Player player) {
        player.addTrade(4);
    }

    @Override
    public int getTradeWhenScrapped() {
        return 4;
    }
}
