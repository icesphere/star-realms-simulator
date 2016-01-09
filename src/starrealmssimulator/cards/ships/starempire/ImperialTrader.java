package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class ImperialTrader extends Ship implements AlliableCard
{
    public ImperialTrader()
    {
        name = "Imperial Trader";
        faction = Faction.STAR_EMPIRE;
        cost = 5;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        text = "Add 3 Trade; Draw a card; Ally: Add 4 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(3);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(4);
    }
}
