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

    public void useBase(Player player) {
        this.used = true;
        this.baseUsed(player);
    }

    public abstract void baseUsed(Player player);
}
