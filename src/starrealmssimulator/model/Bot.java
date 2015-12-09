package starrealmssimulator.model;

import starrealmssimulator.cards.*;
import starrealmssimulator.gambits.*;

import java.util.*;

import static java.util.stream.Collectors.toList;

public abstract class Bot extends Player {
    public Bot() {
        super();
    }

    @Override
    public String getPlayerName() {
        return "Bot";
    }

    protected Comparator<Card> discardScoreDescending = (c1, c2) -> Integer.compare(getDiscardCardScore(c2), getDiscardCardScore(c1));
    protected Comparator<Card> scrapScoreDescending = (c1, c2) -> Integer.compare(getScrapCardScore(c2), getScrapCardScore(c1));
    protected Comparator<Card> scrapForBenefitScoreDescending = (c1, c2) -> Integer.compare(getScrapForBenefitScore(c2), getScrapForBenefitScore(c1));
    protected Comparator<Base> useBaseScoreDescending = (b1, b2) -> Integer.compare(getUseBaseScore(b2), getUseBaseScore(b1));
    protected Comparator<Card> playCardScoreDescending = (c1, c2) -> Integer.compare(getPlayCardScore(c2), getPlayCardScore(c1));
    protected Comparator<Card> cardToBuyScoreDescending = (c1, c2) -> Integer.compare(getBuyCardScore(c2), getBuyCardScore(c1));
    protected Comparator<Base> destroyBaseScoreDescending = (c1, c2) -> Integer.compare(getDestroyBaseScore(c2), getDestroyBaseScore(c1));
    protected Comparator<Base> attackBaseScoreDescending = (c1, c2) -> Integer.compare(getAttackBaseScore(c2), getAttackBaseScore(c1));
    protected Comparator<Card> copyShipScoreDescending = (c1, c2) -> Integer.compare(getCopyShipScore((Ship) c2), getCopyShipScore((Ship) c1));
    protected Comparator<Card> scrapCardFromTradeRowScoreDescending = (c1, c2) -> Integer.compare(getScrapCardFromTradeRowScore(c2), getScrapCardFromTradeRowScore(c1));
    protected Comparator<Card> cardToTopOfDeckScoreDescending = (c1, c2) -> Integer.compare(getCardToTopOfDeckScore(c2), getCardToTopOfDeckScore(c1));
    protected Comparator<Base> returnBaseToHandScoreDescending = (c1, c2) -> Integer.compare(getReturnBaseToHandScore(c2), getReturnBaseToHandScore(c1));
    protected Comparator<Gambit> useGambitScoreDescending = (g1, g2) -> Integer.compare(getUseGambitScore(g2), getUseGambitScore(g1));

    @Override
    public void takeTurn() {

        List<Gambit> everyTurnGambits = getEveryTurnGambits();
        for (Gambit gambit : everyTurnGambits) {
            getGame().gameLog("Active gambit " + gambit.getName());
            ((EveryTurnGambit) gambit).everyTurnAbility(this);
        }

        boolean endTurn = false;

        while (!endTurn) {
            endTurn = true;

            List<Card> cardsToScrapForBenefit = new ArrayList<>();

            for (Card card : getInPlay()) {
                if (card.isScrapable()) {
                    if (shouldScrapCard(card)) {
                        cardsToScrapForBenefit.add(card);
                    }
                }
            }

            if (!cardsToScrapForBenefit.isEmpty()) {
                endTurn = false;
                List<Card> sortedCardsToScrapForBenefit = cardsToScrapForBenefit.stream().sorted(scrapForBenefitScoreDescending).collect(toList());
                for (Card card : sortedCardsToScrapForBenefit) {
                    this.scrapCardInPlayForBenefit(card);
                }
            }

            List<Base> unusedBasesAndOutposts = getUnusedBasesAndOutposts();

            if (!unusedBasesAndOutposts.isEmpty()) {
                List<Base> sortedBases = unusedBasesAndOutposts.stream().sorted(useBaseScoreDescending).collect(toList());
                for (Base sortedBase : sortedBases) {
                    if (sortedBase.useBase(this)) {
                        endTurn = false;
                    }
                }
            }

            List<Gambit> scrappableGambits = getScrappableGambits();
            if (!scrappableGambits.isEmpty()) {
                List<Gambit> sortedGambits = scrappableGambits.stream().sorted(useGambitScoreDescending).collect(toList());
                for (Gambit gambit : sortedGambits) {
                    if (getUseGambitScore(gambit) > 0) {
                        ((ScrappableGambit) gambit).scrapGambit(this);
                        gambitScrapped(gambit);
                    }
                }
            }

            if (!getHand().isEmpty()) {
                endTurn = false;
                while (!getHand().isEmpty()) {
                    List<Card> sortedCards = getHand().stream()
                            .sorted(playCardScoreDescending).collect(toList());
                    Card card = sortedCards.get(0);
                    playCard(card);
                    if (card instanceof AlliableCard && useAllyAfterPlay(card)) {
                        useAlliedAbility((AlliableCard) card);
                    }
                    if (card.isBase() && useBaseAfterPlay((Base) card)) {
                        ((Base) card).useBase(this);
                    }
                }
            }

            for (Card card : getInPlay()) {
                if (card instanceof AlliableCard) {
                    if (useAlliedAbility((AlliableCard) card)) {
                        endTurn = false;
                    }
                }
            }

            if (getTrade() > 0) {
                List<Card> cardsToBuy = getCardsToBuy();
                if (!cardsToBuy.isEmpty()) {
                    endTurn = false;
                    for (Card card : cardsToBuy) {
                        this.buyCard(card);
                    }
                }
            }
        }

        applyCombatAndEndTurn();
    }

    public boolean useAllyAfterPlay(Card card) {
        if (card instanceof PatrolMech) {
            return true;
        }

        return false;
    }

    public boolean useBaseAfterPlay(Base base) {
        if (base instanceof RecyclingStation) {
            return true;
        }

        if (base instanceof BrainWorld) {
            return true;
        }

        return false;
    }

    private boolean shouldScrapCard(Card card) {
        return getScrapForBenefitScore(card) > 0;
    }

    public void applyCombatAndEndTurn() {
        getGame().gameLog("Combat: " + getCombat());
        if (getCombat() > 0 && !getOpponent().getOutposts().isEmpty()) {
            List<Outpost> sortedOutposts = getOpponent().getOutposts().stream().sorted(attackBaseScoreDescending).collect(toList());
            for (Outpost outpost : sortedOutposts) {
                if (getAttackBaseScore(outpost) > 0 && getCombat() >= outpost.getShield()) {
                    destroyOpponentBase(outpost);
                }
            }
        }

        if (getCombat() >= getOpponent().getAuthority()) {
            attackOpponentWithRemainingCombat();
        }

        if (getCombat() > 0 && !getOpponent().getBases().isEmpty()) {
            List<Base> sortedBases = getOpponent().getBases().stream().sorted(attackBaseScoreDescending).collect(toList());
            for (Base base : sortedBases) {
                if (getAttackBaseScore(base) > 0 && getCombat() >= base.getShield()) {
                    destroyOpponentBase(base);
                }
            }
        }

        if (getCombat() > 0 && getOpponent().getOutposts().isEmpty()) {
            attackOpponentWithRemainingCombat();
        }

        endTurn();
    }

    public List<Card> getCardsToBuy() {
        List<Card> cardsToBuy = new ArrayList<>();

        getGame().gameLog("Determining cards to buy. Current Trade: " + getTrade());

        List<Card> cards = new ArrayList<>(getGame().getTradeRow());
        cards.add(getGame().getExplorer());
        cards.add(getGame().getExplorer());

        List<Card> sortedCards = cards.stream().filter(c -> getTrade() >= c.getCost()).sorted(cardToBuyScoreDescending).collect(toList());
        if (!sortedCards.isEmpty() && getBuyCardScore(sortedCards.get(0)) > 0) {
            Card cardWithHighestBuyScore = sortedCards.get(0);

            getGame().gameLog("Card with highest buy score: " + cardWithHighestBuyScore.getName());

            if (sortedCards.size() > 2) {
                Map<Card, Integer> cardToBuyScoreMap = new HashMap<>();

                for (Card card : cards) {
                    if (!cardToBuyScoreMap.containsKey(card)) {
                        cardToBuyScoreMap.put(card, getBuyCardScore(card));
                    }
                }

                List<List<Card>> twoCardsList = new ArrayList<>();

                for (int i = 1; i < sortedCards.size() - 1; i++) {
                    Card cardToCompareAgainst = sortedCards.get(i);
                    for (int j = i + 1; j < sortedCards.size(); j++) {
                        if (addTwoCardListIfEnoughTrade(twoCardsList, cardToCompareAgainst, sortedCards.get(j))) {
                            break;
                        }
                    }
                }

                for (List<Card> cardList : twoCardsList) {
                    int totalBuyScore = 0;
                    totalBuyScore += cardToBuyScoreMap.get(cardList.get(0));
                    totalBuyScore += cardToBuyScoreMap.get(cardList.get(1));

                    getGame().gameLog("Buy score for " + cardWithHighestBuyScore.getName() + ": " + cardToBuyScoreMap.get(cardWithHighestBuyScore));
                    getGame().gameLog("Buy score for " + getGame().getCardsAsString(cardList) + ": " + totalBuyScore);

                    if (totalBuyScore > cardToBuyScoreMap.get(cardWithHighestBuyScore)) {
                        return cardList;
                    }
                }
            }

            cardsToBuy.add(cardWithHighestBuyScore);
        }

        return cardsToBuy;
    }

    private boolean addTwoCardListIfEnoughTrade(List<List<Card>> twoCardList, Card card1, Card card2) {
        if ((card1.getCost() + card2.getCost()) <= getTrade()) {
            List<Card> cards = new ArrayList<>(2);
            cards.add(card1);
            cards.add(card2);
            twoCardList.add(cards);
            return true;
        }
        return false;
    }

    public int getBuyCardScore(Card card) {
        return card.getCost();
    }

    public int getUseBaseScore(Base card) {
        if (card instanceof RecyclingStation) {
            return 100;
        } else if (card instanceof MachineBase) {
            return 90;
        } else if (card instanceof Junkyard) {
            return 80;
        } else if (card instanceof BrainWorld) {
            return 70;
        } else if (card instanceof BlobWorld) {
            return 10;
        } else {
            return 20;
        }
    }

    public int getPlayCardScore(Card card) {
        if (card instanceof FleetHQ) {
            return 100;
        } else if (card instanceof RecyclingStation) {
            return 90;
        } else if (card instanceof SupplyBot) {
            return 80;
        } else if (card instanceof TradeBot) {
            return 70;
        } else if (card instanceof MissileBot) {
            return 60;
        } else if (card instanceof PatrolMech) {
            return 50;
        } else if (card instanceof BrainWorld) {
            return 40;
        } else if (card instanceof Explorer) {
            return 10;
        } else if (card instanceof StealthNeedle) {
            return 8;
        } else if (card instanceof Scout) {
            return 6;
        } else if (card instanceof Viper) {
            return 4;
        } else {
            return 20;
        }
    }

    public int getDestroyBaseScore(Base card) {
        int opponentStarterCards = getOpponent().countCardsByType(getOpponent().getAllCards(), Card::isStarterCard);

        if (card instanceof BrainWorld) {
            if (opponentStarterCards >= 8) {
                return 12;
            } else if (opponentStarterCards >= 6) {
                return 11;
            } else if (opponentStarterCards >= 4) {
                return 10;
            }
        } else if (card instanceof MachineBase) {
            if (opponentStarterCards >= 8) {
                return 11;
            } else if (opponentStarterCards >= 6) {
                return 10;
            } else if (opponentStarterCards >= 4) {
                return 9;
            }
        } else if (card instanceof Junkyard) {
            if (opponentStarterCards >= 8) {
                return 8;
            } else if (opponentStarterCards >= 6) {
                return 7;
            }
        } else if (card instanceof BlobWorld) {
            int opponentBlobCards = getOpponent().countCardsByType(getOpponent().getAllCards(), Card::isBlob);
            if (opponentBlobCards >= 6) {
                return 10;
            } else if (opponentBlobCards >= 3) {
                return 9;
            }
        } else if (card instanceof RecyclingStation) {
            if (opponentStarterCards >= 8) {
                return 6;
            } else if (opponentStarterCards >= 5) {
                return 5;
            }
        }

        return card.getCost();
    }

    public int getAttackBaseScore(Base card) {
        return getDestroyBaseScore(card);
    }

    public int getCopyShipScore(Ship card) {
        if (card instanceof StealthNeedle) {
            return 0;
        }
        return getBuyCardScore(card);
    }

    public int getScrapCardFromTradeRowScore(Card card) {
        return 0;
    }

    public int getCardToTopOfDeckScore(Card card) {
        return getBuyCardScore(card);
    }

    public int getReturnBaseToHandScore(Base card) {
        return getBuyCardScore(card);
    }

    public int getDiscardCardScore(Card card) {
        if (card instanceof Viper) {
            return 100;
        } else if (card instanceof Scout) {
            return 90;
        } else if (card instanceof Explorer) {
            return 80;
        }

        return 20 - card.getCost();
    }

    public int getScrapCardScore(Card card) {
        if (card instanceof Viper) {
            return 100;
        } else if (card instanceof Scout) {
            return 90;
        } else if (card instanceof Explorer) {
            return 80;
        }

        return 20 - card.getCost();
    }

    public int getScrapForBenefitScore(Card card) {
        int authority = getAuthority();
        int opponentAuthority = getOpponent().getAuthority();

        int numOpponentOutposts = getOpponent().getOutposts().size();

        int deck = getCurrentDeckNumber();

        if (numOpponentOutposts == 0 && card.getCombatWhenScrapped() >= opponentAuthority) {
            return 10;
        }

        if (card instanceof Battlecruiser || card instanceof TheArk) {
            if (numOpponentOutposts > 0 && getCombat() >= opponentAuthority) {
                return 10;
            }
        }

        if (card instanceof ImperialFrigate) {
            if (opponentAuthority <= 10 || authority <= 10) {
                return 5;
            }
        }

        if (card instanceof PortOfCall) {
            if (numOpponentOutposts > 0 && getCombat() >= opponentAuthority) {
                return 10;
            } else if (authority >= 20 && opponentAuthority <= 10) {
                return 5;
            }
        }

        if (card instanceof SurveyShip) {
            if (getOpponent().getHand().size() <= 4 || opponentAuthority <= 10 || authority <= 10) {
                return 5;
            }
        }

        if (card instanceof BlobWheel || card instanceof Ram || card instanceof SpaceStation) {
            int buyScoreIncrease = getBuyScoreIncrease(card.getTradeWhenScrapped());
            if ((deck < 3 && buyScoreIncrease >= 20) || buyScoreIncrease >= 40) {
                return 5;
            }
        }

        if (card instanceof Explorer) {
            if (deck > 2) {
                return 5;
            }
            if (getHand().isEmpty() && getUnusedBasesAndOutposts().isEmpty() && getCombat() < getSmallestOutpostShield() && (getCombat() + 2) >= getSmallestOutpostShield()) {
                return 5;
            }
        }

        return 0;
    }

    public int getChoice(Card card) {
        int deck = getCurrentDeckNumber();
        int opponentAuthority = getOpponent().getAuthority();

        if (card instanceof BarterWorld) {
            if (deck < 3) {
                return 2;
            }
        } else if (card instanceof BlobWorld) {
            int blobCardsPlayed = countCardsByType(getPlayed(), Card::isBlob);
            if (blobCardsPlayed >= 2 && opponentAuthority > 5) {
                return 2;
            }
        } else if (card instanceof DefenseCenter) {
            if (opponentAuthority <= 2) {
                return 2;
            }
            if (opponentAuthority < 10 && getAuthority() > 10) {
                return 2;
            }
        } else if (card instanceof PatrolMech) {
            if (deck > 2) {
                return 2;
            }
        } else if (card instanceof RecyclingStation) {
            int buyScoreIncrease = getBuyScoreIncrease(1);
            if (deck <= 3 && buyScoreIncrease >= 20) {
                return 1;
            } else {
                return 2;
            }
        } else if (card instanceof TradingPost) {
            if (deck <= 2) {
                return 1;
            }
            int buyScoreIncrease = getBuyScoreIncrease(1);
            if (getAuthority() >= 20 && buyScoreIncrease >= 20) {
                return 1;
            } else {
                return 2;
            }
        } else if (card instanceof Starmarket) {
            if (deck <= 2) {
                return 2;
            }
        }

        return 1;
    }

    @Override
    public int discardCards(int cards, boolean optional) {
        int cardsDiscarded = 0;

        if (!getHand().isEmpty()) {
            if (cards > getHand().size()) {
                cards = getHand().size();
            }
            List<Card> sortedCards = getHand().stream().sorted(discardScoreDescending).collect(toList());
            for (int i = 0; i < cards; i++) {
                Card card = sortedCards.get(i);
                int score = getDiscardCardScore(card);
                if (getHand().isEmpty() || (optional && score < 20)) {
                    break;
                } else {
                    discardCard(card);
                    cardsDiscarded++;
                }
            }
        }

        return cardsDiscarded;
    }

    @Override
    public void discardAndDrawCards(int cards) {
        int cardsDiscarded = discardCards(cards, true);
        drawCards(cardsDiscarded);
    }

    @Override
    public Ship getShipToCopy() {
        List<Card> sortedCards = getInPlay().stream().filter(Card::isShip).sorted(copyShipScoreDescending).collect(toList());

        Ship shipToCopy = (Ship) sortedCards.get(0);

        if (getCopyShipScore(shipToCopy) > 0) {
            return shipToCopy;
        }

        return null;
    }

    @Override
    public Card getCardToScrapFromHand() {
        List<Card> sortedCards = getHand().stream().sorted(scrapScoreDescending).collect(toList());
        return sortedCards.get(0);
    }

    @Override
    public void makeChoice(Card card, Choice... choices) {
        card.choiceMade(getChoice(card), this);
    }

    @Override
    public Card chooseFreeShipToPutOnTopOfDeck() {
        List<Card> sortedCards = getGame().getTradeRow().stream().filter(Card::isShip).sorted(cardToTopOfDeckScoreDescending).collect(toList());

        if (!sortedCards.isEmpty()) {
            Card cardToTop = sortedCards.get(0);

            if (getCardToTopOfDeckScore(cardToTop) > 0) {
                return cardToTop;
            }
        }

        return null;
    }

    @Override
    public List<Card> chooseCardsToScrapInTradeRow(int cards) {
        List<Card> sortedCards = getGame().getTradeRow().stream().sorted(scrapCardFromTradeRowScoreDescending).collect(toList());

        List<Card> cardsToScrap = new ArrayList<>();

        for (Card card : sortedCards) {
            if (cardsToScrap.size() >= cards) {
                break;
            }
            cardsToScrap.add(card);
        }

        return cardsToScrap;
    }

    @Override
    public List<List<Card>> getCardsToOptionallyScrapFromDiscardOrHand(int cards) {
        List<List<Card>> cardsToScrap = new ArrayList<>();

        List<Card> cardsToScrapFromDiscard = new ArrayList<>();
        List<Card> cardsToScrapFromHand = new ArrayList<>();

        if (!getDiscard().isEmpty()) {
            List<Card> sortedDiscardCards = getDiscard().stream().sorted(scrapScoreDescending).collect(toList());

            for (int i = 0; i < cards; i++) {
                if (sortedDiscardCards.size() <= i) {
                    break;
                }
                Card card = sortedDiscardCards.get(i);
                int score = getScrapCardScore(card);
                if (score < 20) {
                    break;
                } else {
                    cardsToScrapFromDiscard.add(card);
                    if(cardsToScrapFromDiscard.size() == cards) {
                        break;
                    }
                }
            }
        }

        if (!getHand().isEmpty() && cardsToScrapFromDiscard.size() < cards) {
            List<Card> sortedHandCards = getHand().stream().sorted(scrapScoreDescending).collect(toList());

            for (int i = 0; i < cards; i++) {
                if (sortedHandCards.size() <= i) {
                    break;
                }
                Card card = sortedHandCards.get(i);
                int score = getScrapCardScore(card);
                if (score < 20) {
                    break;
                } else {
                    cardsToScrapFromHand.add(card);
                    if (cardsToScrapFromDiscard.size() + cardsToScrapFromHand.size() == cards) {
                        break;
                    }
                }
            }
        }

        cardsToScrap.add(cardsToScrapFromDiscard);
        cardsToScrap.add(cardsToScrapFromHand);

        return cardsToScrap;
    }

    @Override
    public Base chooseTargetBaseToDestroy() {
        if (!getOpponent().getOutposts().isEmpty()) {
            List<Base> sortedOutposts = getOpponent().getOutposts().stream().sorted(destroyBaseScoreDescending).collect(toList());
            Base baseToDestroy = sortedOutposts.get(0);
            if (getDestroyBaseScore(baseToDestroy) > 0) {
                return baseToDestroy;
            }
        } else if (!getOpponent().getBases().isEmpty()) {
            List<Base> sortedBases = getOpponent().getBases().stream().sorted(destroyBaseScoreDescending).collect(toList());
            Base baseToDestroy = sortedBases.get(0);
            if (getDestroyBaseScore(baseToDestroy) > 0) {
                return baseToDestroy;
            }
        }

        return null;
    }

    public int getBuyScoreIncrease(int extraTrade) {
        int cardToBuyScore = 0;

        List<Card> cardsToBuy = getCardsToBuy();
        if (!cardsToBuy.isEmpty()) {
            for (Card cardToBuy : cardsToBuy) {
                cardToBuyScore += getBuyCardScore(cardToBuy);
            }
        }

        List<Card> sortedCards = getGame().getTradeRow().stream().filter(c -> getTrade() + extraTrade >= c.getCost()).sorted(cardToBuyScoreDescending).collect(toList());
        if (!sortedCards.isEmpty()) {
            int bestCardScore = getBuyCardScore(sortedCards.get(0));
            return bestCardScore - cardToBuyScore;
        }

        return 0;
    }

    @Override
    public Base chooseBaseToReturnToHand() {
        //todo include opponent's bases

        List<Base> sortedBases = getBases().stream().sorted(returnBaseToHandScoreDescending).collect(toList());

        if (!sortedBases.isEmpty()) {
            Base baseToReturnToHand = sortedBases.get(0);

            if (getReturnBaseToHandScore(baseToReturnToHand) > 0) {
                return baseToReturnToHand;
            }
        }

        return null;
    }

    @Override
    public Faction chooseFactionForCard(Card card) {
        //todo create rules

        List<Card> cards = new ArrayList<>(getInPlay());
        cards.addAll(getHand());

        Map<Faction, Integer> factionCounts = new HashMap<>(4);

        factionCounts.put(Faction.BLOB, countCardsByType(cards, Card::isBlob));
        factionCounts.put(Faction.STAR_EMPIRE, countCardsByType(cards, Card::isStarEmpire));
        factionCounts.put(Faction.TRADE_FEDERATION, countCardsByType(cards, Card::isTradeFederation));
        factionCounts.put(Faction.MACHINE_CULT, countCardsByType(cards, Card::isMachineCult));

        Faction factionWithLeastCards = null;
        int lowestFactionCount = 100;

        for (Faction faction : factionCounts.keySet()) {
            if (factionCounts.get(faction) <= lowestFactionCount) {
                factionWithLeastCards = faction;
            }
        }

        return factionWithLeastCards;
    }

    @Override
    public Card chooseCardFromDiscardToAddToTopOfDeck() {
        List<Card> sortedCards = getDiscard().stream().sorted(cardToBuyScoreDescending).collect(toList());

        Card card = sortedCards.get(0);
        if (getBuyCardScore(card) > 0) {
            return card;
        }

        return null;
    }

    @Override
    public Card chooseFreeCardToAcquire(int maxCost) {
        List<Card> sortedCards = getGame().getTradeRow().stream().filter(c -> c.getCost() <= maxCost).sorted(cardToBuyScoreDescending).collect(toList());

        Card card = sortedCards.get(0);
        if (getBuyCardScore(card) > 0) {
            return card;
        }

        return null;
    }

    public int getUseGambitScore(Gambit gambit) {
        int starterCardsInPlay = countCardsByType(getInPlay(), Card::isStarterCard);

        if (gambit instanceof BoldRaid) {
            if (!getOpponent().getBases().isEmpty()) {
                return 12;
            } else if (starterCardsInPlay >= 3 && getHand().size() == 0 && getDeck().size() == 0) {
                return 4;
            }
        } else if (gambit instanceof EnergyShield) {
            if (starterCardsInPlay >= 3 && getHand().size() == 0 && getDeck().size() == 0) {
                return 6;
            }
        } else if (gambit instanceof PoliticalManeuver) {
            if (getHand().isEmpty() && getBuyScoreIncrease(2) >= 30) {
                return 2;
            }
        } else if (gambit instanceof RiseToPower) {
            if (starterCardsInPlay >= 3 && getHand().size() == 0 && getDeck().size() == 0) {
                return 18;
            } else if (getBuyScoreIncrease(1) >= 20) {
                return 14;
            }
        } else if (gambit instanceof SalvageOperation) {
            if (!getDiscard().isEmpty()) {
                List<Card> sortedCards = getDiscard().stream().sorted(cardToBuyScoreDescending).collect(toList());
                if (getBuyCardScore(sortedCards.get(0)) >= 30) {
                    return 24;
                }
            }
        } else if (gambit instanceof SmugglingRun) {
            List<Card> sortedCards = getGame().getTradeRow().stream().filter(c -> c.getCost() <= 4).sorted(cardToBuyScoreDescending).collect(toList());
            if (!sortedCards.isEmpty()) {
                if (getBuyCardScore(sortedCards.get(0)) >= 30) {
                    return 22;
                }
            }
        } else if (gambit instanceof SurpriseAssault) {
            if (getHand().isEmpty()) {
                if (getOpponent().getAuthority() <= 8) {
                    return 16;
                } else if (getCombat() < getOpponent().getBiggestOutpostShield()) {
                    return 1;
                } else if (getCombat() < getOpponent().getBiggestBaseShield()) {
                    return 1;
                }
            }
        } else if (gambit instanceof UnlikelyAlliance) {
            if (starterCardsInPlay >= 3 && getHand().size() == 0 && getDeck().size() <= 1) {
                return 20;
            } else if (getGame().getTurn() >= 5) {
                return 8;
            }
        }

        return 0;
    }
}
