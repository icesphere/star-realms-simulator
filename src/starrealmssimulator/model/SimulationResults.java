package starrealmssimulator.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationResults {
    private float winPercentage;

    private float averageNumTurns;

    private int totalGamesCounted;

    private Map<Integer, Integer> playerAverageAuthorityByTurn;
    private Map<Integer, Integer> opponentAverageAuthorityByTurn;

    private String winGameLog;
    private String lossGameLog;

    private LinkedHashMap<String, Integer> playerWinDifferentialByCardsAtEndOfGame;
    private LinkedHashMap<String, Integer> opponentWinDifferentialByCardsAtEndOfGame;

    private LinkedHashMap<String, Float> playerWinPercentageByFirstDeckCard;
    private LinkedHashMap<String, Float> opponentWinPercentageByFirstDeckCard;

    private Map<String, Integer> playerTotalGamesByFirstDeckCard;
    private Map<String, Integer> opponentTotalGamesByFirstDeckCard;

    public float getWinPercentage() {
        return winPercentage;
    }

    public void setWinPercentage(float winPercentage) {
        this.winPercentage = winPercentage;
    }

    public float getAverageNumTurns() {
        return averageNumTurns;
    }

    public void setAverageNumTurns(float averageNumTurns) {
        this.averageNumTurns = averageNumTurns;
    }

    public Map<Integer, Integer> getPlayerAverageAuthorityByTurn() {
        return playerAverageAuthorityByTurn;
    }

    public void setPlayerAverageAuthorityByTurn(Map<Integer, Integer> playerAverageAuthorityByTurn) {
        this.playerAverageAuthorityByTurn = playerAverageAuthorityByTurn;
    }

    public Map<Integer, Integer> getOpponentAverageAuthorityByTurn() {
        return opponentAverageAuthorityByTurn;
    }

    public void setOpponentAverageAuthorityByTurn(Map<Integer, Integer> opponentAverageAuthorityByTurn) {
        this.opponentAverageAuthorityByTurn = opponentAverageAuthorityByTurn;
    }

    public String getWinGameLog() {
        return winGameLog;
    }

    public void setWinGameLog(String winGameLog) {
        this.winGameLog = winGameLog;
    }

    public String getLossGameLog() {
        return lossGameLog;
    }

    public void setLossGameLog(String lossGameLog) {
        this.lossGameLog = lossGameLog;
    }

    public int getTotalGamesCounted() {
        return totalGamesCounted;
    }

    public void setTotalGamesCounted(int totalGamesCounted) {
        this.totalGamesCounted = totalGamesCounted;
    }

    public LinkedHashMap<String, Integer> getPlayerWinDifferentialByCardsAtEndOfGame() {
        return playerWinDifferentialByCardsAtEndOfGame;
    }

    public void setPlayerWinDifferentialByCardsAtEndOfGame(LinkedHashMap<String, Integer> playerWinDifferentialByCardsAtEndOfGame) {
        this.playerWinDifferentialByCardsAtEndOfGame = playerWinDifferentialByCardsAtEndOfGame;
    }

    public LinkedHashMap<String, Integer> getOpponentWinDifferentialByCardsAtEndOfGame() {
        return opponentWinDifferentialByCardsAtEndOfGame;
    }

    public void setOpponentWinDifferentialByCardsAtEndOfGame(LinkedHashMap<String, Integer> opponentWinDifferentialByCardsAtEndOfGame) {
        this.opponentWinDifferentialByCardsAtEndOfGame = opponentWinDifferentialByCardsAtEndOfGame;
    }

    public LinkedHashMap<String, Float> getPlayerWinPercentageByFirstDeckCard() {
        return playerWinPercentageByFirstDeckCard;
    }

    public void setPlayerWinPercentageByFirstDeckCard(LinkedHashMap<String, Float> playerWinPercentageByFirstDeckCard) {
        this.playerWinPercentageByFirstDeckCard = playerWinPercentageByFirstDeckCard;
    }

    public LinkedHashMap<String, Float> getOpponentWinPercentageByFirstDeckCard() {
        return opponentWinPercentageByFirstDeckCard;
    }

    public void setOpponentWinPercentageByFirstDeckCard(LinkedHashMap<String, Float> opponentWinPercentageByFirstDeckCard) {
        this.opponentWinPercentageByFirstDeckCard = opponentWinPercentageByFirstDeckCard;
    }

    public Map<String, Integer> getPlayerTotalGamesByFirstDeckCard() {
        return playerTotalGamesByFirstDeckCard;
    }

    public void setPlayerTotalGamesByFirstDeckCard(Map<String, Integer> playerTotalGamesByFirstDeckCard) {
        this.playerTotalGamesByFirstDeckCard = playerTotalGamesByFirstDeckCard;
    }

    public Map<String, Integer> getOpponentTotalGamesByFirstDeckCard() {
        return opponentTotalGamesByFirstDeckCard;
    }

    public void setOpponentTotalGamesByFirstDeckCard(Map<String, Integer> opponentTotalGamesByFirstDeckCard) {
        this.opponentTotalGamesByFirstDeckCard = opponentTotalGamesByFirstDeckCard;
    }
}
