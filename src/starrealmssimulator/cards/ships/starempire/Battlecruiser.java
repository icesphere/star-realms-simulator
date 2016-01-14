package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class Battlecruiser extends Ship implements ScrappableCard, AlliableCard
{
    public Battlecruiser()
    {
        name = "Battlecruiser";
        faction = Faction.STAR_EMPIRE;
        cost = 6;
        set = CardSet.CORE;
        text = "Add 5 Combat; Draw a card; Ally: Target Opponent discards a card; Scrap: Draw a card. You may destroy target base.";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(5);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player)
    {
        player.opponentDiscardsCard();
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.drawCard();
        player.destroyTargetBase();
    }

    @Override
    public boolean canDestroyBasedWhenScrapped() {
        return true;
    }
}
