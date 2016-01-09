package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class ImperialFrigate extends Ship implements ScrappableCard, AlliableCard
{
    public ImperialFrigate()
    {
        name = "Imperial Frigate";
        faction = Faction.STAR_EMPIRE;
        cost = 3;
        set = CardSet.CORE;
        text = "Add 4 Combat; Target Opponent discards a card; Ally: Add 2 Combat; Scrap: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(4);
        player.opponentDiscardsCard();
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(2);
    }

    @Override
    public void cardScrapped(Player player) {
        player.drawCard();
    }
}
