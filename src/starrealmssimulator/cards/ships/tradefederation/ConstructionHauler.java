package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class ConstructionHauler extends Ship implements AlliableCard
{
    public ConstructionHauler()
    {
        name = "Construction Hauler";
        faction = Faction.TRADE_FEDERATION;
        cost = 6;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        text = "Add 3 Authority; Add 2 Trade; Draw a Card; Ally: You may put the next base you acquire this turn directly into play";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addAuthority(3);
        player.addTrade(2);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player) {
        player.nextBaseToHand();
    }
}
