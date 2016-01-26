package starrealmssimulator.bots;

import starrealmssimulator.cards.ships.DoNotBuyCard;
import starrealmssimulator.model.Bot;
import starrealmssimulator.model.BotStrategy;
import starrealmssimulator.model.Card;
import starrealmssimulator.model.CardToBuySimulationResults;
import starrealmssimulator.service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimulatorBot extends Bot {
    BotStrategy strategy = null;

    private GameService gameService;

    public SimulatorBot(GameService gameService) {
        super();
        this.gameService = gameService;
    }

    @Override
    public List<Card> getCardsToBuy() {
        if (strategy != null) {
            return super.getCardsToBuy();
        }

        List<Card> cards = new ArrayList<>();

        Map<Card, CardToBuySimulationResults> results = gameService.simulateBestCardToBuy(gameService.getGameStateFromGame(getGame()), 10);

        if (!results.isEmpty()) {
            Card bestCardToBuy = null;

            float bestWinPercentage = 0;

            for (Card card : results.keySet()) {
                float winPercentage = results.get(card).getWinPercentage();
                if (getTrade() >= card.getCost() && winPercentage > bestWinPercentage) {
                    bestCardToBuy = card;
                    bestWinPercentage = winPercentage;
                }
            }

            if (!(bestCardToBuy instanceof DoNotBuyCard)) {
                cards.add(bestCardToBuy);
            }
        }

        return cards;
    }

    @Override
    public int getBuyCardScore(Card card) {
        if (card == null) {
            return 0;
        }
        if (strategy != null) {
            return strategy.getBuyCardScore(card, this);
        } else {
            return card.getCost() * card.getCost();
        }
    }

    public void setStrategy(BotStrategy strategy) {
        this.strategy = strategy;
    }
}
