package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class DefenseBot extends Ship
{
    public DefenseBot()
    {
        name = "Defense Bot";
        faction = Faction.MACHINE_CULT;
        cost = 2;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        text = "Add 1 Combat; You may scrap a card in your hand or discard pile. If you control two or more bases, gain 8 Combat.";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(1);
        player.scrapCardFromHandOrDiscard();
        if (player.getBases().size() >= 2) {
            player.addCombat(8);
        }
    }
}
