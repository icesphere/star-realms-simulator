package starrealmssimulator.model;

import starrealmssimulator.cards.ships.Scout;
import starrealmssimulator.cards.ships.Viper;

import java.util.Objects;

public abstract class Card {
    protected String name;
    protected Faction faction;
    protected int cost;
    protected CardSet set;
    protected String text;
    protected int shield;
    protected boolean alliedAbilityUsed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public CardSet getSet() {
        return set;
    }

    public void setSet(CardSet set) {
        this.set = set;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public boolean isScrappable() {
        return this instanceof ScrappableCard;
    }

    public boolean isBase() {
        return this instanceof Base;
    }

    public boolean isOutpost() {
        return this instanceof Outpost;
    }

    public boolean isShip() {
        return this instanceof Ship;
    }

    public boolean isBlob() {
        return this.faction == Faction.BLOB;
    }

    public boolean isTradeFederation() {
        return this.faction == Faction.TRADE_FEDERATION;
    }

    public boolean isMachineCult() {
        return this.faction == Faction.MACHINE_CULT;
    }

    public boolean isStarEmpire() {
        return this.faction == Faction.STAR_EMPIRE;
    }

    public boolean isStarterCard() {
        return this instanceof Scout || this instanceof Viper;
    }

    public abstract void cardPlayed(Player player);

    public boolean isAlliedAbilityUsed() {
        return alliedAbilityUsed;
    }

    public void setAlliedAbilityUsed(boolean alliedAbilityUsed) {
        this.alliedAbilityUsed = alliedAbilityUsed;
    }

    public void choiceMade(int choice, Player player) {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }

        final Card other = (Card) obj;
        return Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    public boolean isAlly(Card card) {
        return card.getFaction() == this.getFaction();
    }

    public int getCombatWhenScrapped() {
        return 0;
    }

    public boolean canDestroyBasedWhenScrapped() {
        return false;
    }

    public int getAuthorityWhenScrapped() {
        return 0;
    }

    public int getTradeWhenScrapped() {
        return 0;
    }
}
