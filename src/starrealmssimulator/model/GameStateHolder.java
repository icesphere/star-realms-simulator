package starrealmssimulator.model;

import java.util.List;

public interface GameStateHolder {
    public List<Card> getTradeRow();

    public boolean playerIsCurrentPlayerForThisGameInstance();

    public Game getGameInstance();

    public Player getPlayerInstance();

    public Player getOpponentInstance();

    public GameState getGameState();
}
