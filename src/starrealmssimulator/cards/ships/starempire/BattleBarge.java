package starrealmssimulator.cards.ships.starempire;

import starrealmssimulator.model.*;

public class BattleBarge extends Ship
{
    public BattleBarge()
    {
        name = "Battle Barge";
        faction = Faction.STAR_EMPIRE;
        cost = 4;
        set = CardSet.PROMO_YEAR_1;
        text = "Add 5 Combat; Target opponent discards a card; If you have two or more bases in play, gain 3 Combat and you may return target base from play to its owner's hand.";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);
        player.opponentDiscardsCard();
        if (player.getBases().size() >= 2) {
            player.addCombat(3);
            player.returnTargetBaseToHand();
        }
    }
}
