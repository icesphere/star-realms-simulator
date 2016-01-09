package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class BattleBot extends Ship implements AlliableCard
{
    public BattleBot()
    {
        name = "Battle Bot";
        faction = Faction.MACHINE_CULT;
        cost = 1;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Combat; You may scrap a card in your hand; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(2);
        player.scrapCardFromHand(true);
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(2);
    }
}
