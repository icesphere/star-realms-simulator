package starrealmssimulator.model;

import org.apache.commons.lang3.StringUtils;
import starrealmssimulator.cards.ships.Explorer;
import starrealmssimulator.service.GameService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameStateGame implements GameStateHolder {
    private GameState gameState;
    private GameService gameService;

    boolean playerGoesFirst;

    public GameStateGame(GameState gameState, GameService gameService) {
        this.gameState = gameState;
        this.gameService = gameService;
    }

    @Override
    public List<Card> getTradeRow() {
        Game game = getGameInstance();
        gameState.tradeRow = game.getCardsAsString(game.getTradeRow());
        return game.getTradeRow();
    }

    @Override
    public boolean playerIsCurrentPlayerForThisGameInstance() {
        return playerGoesFirst;
    }

    @Override
    public Game getGameInstance() {
        Game game = new Game();

        playerGoesFirst = gameState.determineCurrentPlayer();

        List<Card> deck = new ArrayList<>();
        if (gameState.determineIncludeBaseSet()) {
            deck.addAll(gameService.getBaseSetDeck());
            game.getCardSets().add(CardSet.CORE);
        }
        if (gameState.determineIncludeColonyWars()) {
            deck.addAll(gameService.getColonyWarsDeck());
            game.getCardSets().add(CardSet.COLONY_WARS);
        }
        if (gameState.determineIncludeYearOnePromos()) {
            deck.addAll(gameService.getYear1PromoCards());
            game.getCardSets().add(CardSet.PROMO_YEAR_1);
        }
        if (gameState.determineIncludeCrisisBasesAndBattleships()) {
            deck.addAll(gameService.getCrisisBasesAndBattleships());
            game.getCardSets().add(CardSet.CRISIS_BASES_AND_BATTLESHIPS);
        }
        if (gameState.determineIncludeCrisisEvents()) {
            deck.addAll(gameService.getCrisisEvents());
            game.getCardSets().add(CardSet.CRISIS_EVENTS);
        }
        if (gameState.determineIncludeCrisisFleetsAndFortresses()) {
            deck.addAll(gameService.getCrisisFleetsAndFortresses());
            game.getCardSets().add(CardSet.CRISIS_FLEETS_AND_FORTRESSES);
        }
        if (gameState.determineIncludeCrisisHeroes()) {
            deck.addAll(gameService.getCrisisHeroes());
            game.getCardSets().add(CardSet.CRISIS_HEROES);
        }

        game.setDeck(deck);

        game.setExplorer(new Explorer());

        Player player = gameService.getBotFromBotName(gameState.bot);

        Player opponent = gameService.getBotFromBotName(gameState.opponentBot);

        game.gameLog("Setting up cards for Player");
        player.setGame(game);
        player.setOpponent(opponent);
        player.setShuffles(gameState.shuffles);
        player.setAuthority(gameState.authority);
        if (gameState.hand.isEmpty() && gameState.deck.isEmpty() && gameState.discard.isEmpty()) {
            player.getDeck().addAll(gameService.getStartingCards());
            Collections.shuffle(player.getDeck());
            if (playerGoesFirst && gameState.turn == 1) {
                player.drawCards(3);
            } else {
                player.drawCards(5);
            }
        } else {
            player.getDeck().addAll(gameService.getCardsFromCardNames(gameState.deck));
            Collections.shuffle(player.getDeck());
            if (gameState.hand.isEmpty()) {
                if (playerGoesFirst && gameState.turn == 1) {
                    player.drawCards(3);
                } else {
                    player.drawCards(5);
                }
            } else {
                player.getHand().addAll(gameService.getCardsFromCardNames(gameState.hand));
            }
            player.getDiscard().addAll(gameService.getCardsFromCardNames(gameState.discard));
        }
        player.getBases().addAll(gameService.getBasesFromCardNames(gameState.basesInPlay));

        //partial turn info
        player.getInPlay().addAll(gameService.getCardsFromCardNames(gameState.inPlay));
        player.getPlayed().addAll(gameService.getCardsFromCardNames(gameState.played));
        player.setCombat(gameState.combat);
        player.setTrade(gameState.trade);
        player.setNextShipToTopOfDeck(gameState.nextShipToTopOfDeck);
        player.setNextShipOrBaseToTopOfDeck(gameState.nextShipOrBaseToTopOfDeck);
        player.setNextShipOrBaseToHand(gameState.nextShipOrBaseToHand);
        player.setNextBaseToHand(gameState.nextBaseToHand);
        player.setAllShipsAddOneCombat(gameState.allShipsAddOneCombat);
        player.setAllFactionsAllied(gameState.allFactionsAllied);
        player.setPreventFirstDamage(gameState.preventFirstDamage);

        game.gameLog("-------------------------");
        game.gameLog("Setting up cards for Opponent");
        opponent.setGame(game);
        opponent.setOpponent(player);
        opponent.setShuffles(gameState.opponentShuffles);
        opponent.setAuthority(gameState.opponentAuthority);
        if (gameState.opponentHandAndDeck.isEmpty() && gameState.opponentDiscard.isEmpty()) {
            opponent.getDeck().addAll(gameService.getStartingCards());
        } else {
            opponent.getDeck().addAll(gameService.getCardsFromCardNames(gameState.opponentHandAndDeck));
            opponent.getDiscard().addAll(gameService.getCardsFromCardNames(gameState.opponentDiscard));
        }
        Collections.shuffle(opponent.getDeck());
        if (!playerGoesFirst && gameState.turn == 1) {
            opponent.drawCards(3);
        } else {
            opponent.drawCards(5);
        }
        opponent.getBases().addAll(gameService.getBasesFromCardNames(gameState.opponentBasesInPlay));

        if (gameState.determineIncludeGambits()) {
            player.getGambits().addAll(gameService.getGambitsFromGambitNames(gameState.gambits));
            opponent.getGambits().addAll(gameService.getGambitsFromGambitNames(gameState.opponentGambits));
            game.getCardSets().add(CardSet.GAMBITS);
        }

        if (gameState.determineIncludeCrisisHeroes()) {
            player.getHeroes().addAll(gameService.getHeroesFromHeroNames(gameState.heroesInPlay));
            opponent.getHeroes().addAll(gameService.getHeroesFromHeroNames(gameState.opponentHeroesInPlay));
        }

        game.getDeck().removeAll(player.getAllCards());
        game.getDeck().removeAll(opponent.getAllCards());
        game.getDeck().removeAll(game.getTradeRowCardsScrapped());

        Collections.shuffle(game.getDeck());

        List<Player> players = new ArrayList<>(2);
        players.add(player);
        players.add(opponent);

        game.setPlayers(players);

        game.setTurn(gameState.turn);

        if (!playerGoesFirst) {
            game.setCurrentPlayerIndex(1);
        }

        if (StringUtils.isEmpty(gameState.tradeRow)) {
            game.gameLog("-------------------------");
            game.addCardsToTradeRow(5);
        } else {
            List<Card> tradeRowCards = gameService.getCardsFromCardNames(gameState.tradeRow);
            game.getDeck().removeAll(tradeRowCards);
            for (Card card : tradeRowCards) {
                game.addCardToTradeRow(card);
            }
            if (tradeRowCards.size() < 5) {
                game.addCardsToTradeRow(5 - tradeRowCards.size());
            }
        }

        return game;
    }

    @Override
    public Player getPlayerInstance() {
        return gameService.getBotFromBotName(gameState.bot);
    }

    @Override
    public Player getOpponentInstance() {
        return gameService.getBotFromBotName(gameState.opponentBot);
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }
}
