package starrealmssimulator.model;

import java.util.List;

public interface GameStateHolder {
    public List<Card> getTradeRow();

    public boolean playerIsCurrentPlayer();

    public Game getGameInstance();

    public Player getPlayerInstance();

    public Player getOpponentInstance();
}
