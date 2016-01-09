package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class AgingBattleship extends Ship implements ScrappableCard, AlliableCard
{
    public AgingBattleship()
    {
        name = "AgingBattleship";
        faction = Faction.STAR_EMPIRE;
        cost = 5;
        set = CardSet.COLONY_WARS;
        text = "Add 5 Combat; Ally: Draw a card; Scrap: Add 2 Combat; Draw two cards";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(2);
        player.drawCards(2);
    }
}
