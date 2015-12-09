package starrealmssimulator.model;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    public static final boolean SHOW_GAME_LOG = false;

    private int turn;

    private List<Player> players;

    private List<Card> deck;

    private List<Card> tradeRow = new ArrayList<>();

    private Card explorer;

    private List<Card> scrapped = new ArrayList<>();

    private int currentPlayerIndex;

    private boolean gameOver;

    public int getTurn()
    {
        return turn;
    }

    public List<Player> getPlayers()
    {
        return players;
    }

    public void setPlayers(List<Player> players)
    {
        this.players = players;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public List<Card> getTradeRow() {
        return tradeRow;
    }

    public void setTradeRow(List<Card> tradeRow) {
        this.tradeRow = tradeRow;
    }

    public Card getExplorer() {
        return explorer;
    }

    public void setExplorer(Card explorer) {
        this.explorer = explorer;
    }

    public List<Card> getScrapped() {
        return scrapped;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void addCardToTradeRow() {
        addCardsToTradeRow(1);
    }

    public void addCardsToTradeRow(int cards) {
        for (int i = 0; i < cards; i++) {
            if (!deck.isEmpty()) {
                tradeRow.add(deck.remove(0));
            }
        }
        gameLog("Trade Row: " + getCardsAsString(tradeRow));
    }

    public void startGame() {
        currentPlayerIndex = 0;
        turn = 1;
        players.get(0).drawCards(3);
        players.get(1).drawCards(5);
        gameLog("Player 1: " + players.get(0).getPlayerName());
        gameLog("Player 2: " + players.get(1).getPlayerName());
    }

    public String getCardsAsString(List cards) {
        String cardString = "";
        boolean first = true;
        for (Object card : cards) {
            if (!first) {
                cardString += ", ";
            } else {
                first = false;
            }
            cardString += ((Card) card).getName();
        }
        return cardString;
    }

    public void turnEnded() {
        gameLog("End of turn " + turn);
        for (Player player : players) {
            gameLog(player.getPlayerName() + "'s authority: " + player.getAuthority());
        }
        gameLog("---");

        for (Player player : players) {
            if (player.getAuthority() <= 0) {
                gameOver();
                return;
            }
        }

        if (currentPlayerIndex == players.size() - 1) {
            currentPlayerIndex = 0;
        } else {
            currentPlayerIndex++;
        }

        turn++;
    }

    private void gameOver() {
        gameOver = true;
        gameLog("-----------------------------");
        gameLog("Game over");
        gameLog("Turns: " + turn);
        for (Player player : players) {
            String playerName = player.getPlayerName();
            gameLog(playerName + "'s authority: " + player.getAuthority());
        }
        for (Player player : players) {
            gameLog("----");
            String playerName = player.getPlayerName();
            gameLog(playerName + "'s cards: ");
            player.getAllCards().forEach(c -> gameLog(c.getName()));
        }
    }

    public Player getWinner() {
        return players.stream().max((p1, p2) -> Integer.compare(p1.getAuthority(), p2.getAuthority())).get();
    }

    public Player getLoser() {
        return players.stream().min((p1, p2) -> Integer.compare(p1.getAuthority(), p2.getAuthority())).get();
    }

    public void gameLog(String log) {
        if (SHOW_GAME_LOG) {
            System.out.println(log);
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }
}
