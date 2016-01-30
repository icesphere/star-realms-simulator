package starrealmssimulator.bots;

import starrealmssimulator.bots.strategies.VelocityStrategy;
import starrealmssimulator.cards.ships.DoNotBuyCard;
import starrealmssimulator.model.Bot;
import starrealmssimulator.model.BotStrategy;
import starrealmssimulator.model.Card;
import starrealmssimulator.model.CardToBuySimulationResults;
import starrealmssimulator.service.GameService;

import java.text.DecimalFormat;
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

        Map<Card, CardToBuySimulationResults> results = gameService.simulateBestCardToBuy(gameService.getGameStateFromGame(getGame()), 5);

        if (!results.isEmpty()) {
            Card bestCardToBuy = null;

            float bestWinPercentage = 0;

            for (Card card : results.keySet()) {
                float winPercentage = results.get(card).getWinPercentage();
                if (winPercentage > bestWinPercentage) {
                    bestCardToBuy = card;
                    bestWinPercentage = winPercentage;
                }
            }

            setStrategy(new VelocityStrategy());

            setCardToBuyThisTurn(bestCardToBuy);

            DecimalFormat f = new DecimalFormat("##.00");

            if (bestCardToBuy != null) {
                getGame().gameLog("Best card to buy this turn: " + bestCardToBuy.getName());
                getGame().gameLog("Win % with best card: " + f.format(bestWinPercentage) + "%");
            }
        }

        List<Card> cards = super.getCardsToBuy();
        setStrategy(null);
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
