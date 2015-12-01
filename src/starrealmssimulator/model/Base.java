package starrealmssimulator.model;

public abstract class Base extends Card {
    protected boolean used;

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    @Override
    public void cardPlayed(Player player) {

    }

    public boolean useBase(Player player) {
        if (baseCanBeUsed(player)) {
            this.used = true;
            this.baseUsed(player);
            return true;
        }
        return false;
    }

    public abstract void baseUsed(Player player);

    public boolean baseCanBeUsed(Player player) {
        return true;
    }
}
