package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class StealthNeedle extends Ship
{
    private Card cardBeingCopied;

    public StealthNeedle()
    {
        name = "Stealth Needle";
        faction = Faction.MACHINE_CULT;
        cost = 4;
        set = CardSet.CORE;
        text = "Copy another ship you've played this turn. Stealth Needle has that ship's faction in addition to Machine Cult";
    }

    @Override
    public void cardPlayed(Player player) {
        player.copyShip(this);
    }

    public Card getCardBeingCopied() {
        return cardBeingCopied;
    }

    public void setCardBeingCopied(Card cardBeingCopied) {
        this.cardBeingCopied = cardBeingCopied;
    }

    @Override
    public boolean isAlly(Card card) {
        return super.isAlly(card) || cardBeingCopied != null && super.isAlly(cardBeingCopied);
    }

    @Override
    public boolean isBlob() {
        return cardBeingCopied != null && cardBeingCopied.isBlob();
    }

    @Override
    public boolean isTradeFederation() {
        return cardBeingCopied != null && cardBeingCopied.isTradeFederation();
    }

    @Override
    public boolean isStarEmpire() {
        return cardBeingCopied != null && cardBeingCopied.isStarEmpire();
    }
}
