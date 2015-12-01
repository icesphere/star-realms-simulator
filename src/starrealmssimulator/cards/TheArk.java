package starrealmssimulator.cards;

import starrealmssimulator.model.*;

public class TheArk extends Ship implements ScrapableCard
{
    public TheArk()
    {
        name = "The Ark";
        faction = Faction.MACHINE_CULT;
        cost = 7;
        set = CardSet.PROMO_YEAR_1;
        text = "Add 5 Combat; Scrap up to two cards in your hand and/or discard pile. Draw a card for each card scrapped this way; Scrap: Destroy target base";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);
        player.scrapToDrawCards(2);
    }

    @Override
    public void cardScraped(Player player) {
        player.destroyTargetBase();
    }
}
