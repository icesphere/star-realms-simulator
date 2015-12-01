package starrealmssimulator.model;

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

    @Override
    public void takeTurn() {
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
                List<Base> sortedBases = getUnusedBasesAndOutposts().stream().sorted(useBaseScoreDescending).collect(toList());
                for (Base sortedBase : sortedBases) {
                    if (sortedBase.useBase(this)) {
                        endTurn = false;
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

    public abstract boolean useAllyAfterPlay(Card card);

    public abstract boolean useBaseAfterPlay(Base base);

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

    public abstract int getBuyCardScore(Card card);

    public abstract int getUseBaseScore(Base card);

    public abstract int getPlayCardScore(Card card);

    public abstract int getDestroyBaseScore(Base card);

    public abstract int getAttackBaseScore(Base card);

    public abstract int getCopyShipScore(Ship card);

    public abstract int getScrapCardFromTradeRowScore(Card card);

    public abstract int getCardToTopOfDeckScore(Card card);

    public abstract int getReturnBaseToHandScore(Base card);

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

    public abstract int getDiscardCardScore(Card card);

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

    public abstract int getScrapCardScore(Card card);

    public abstract int getScrapForBenefitScore(Card card);

    public abstract int getChoice(Card card);

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

    public int getBuyScoreIncrease(Card card) {
        int cardToBuyScore = 0;

        List<Card> cardsToBuy = getCardsToBuy();
        if (!cardsToBuy.isEmpty()) {
            for (Card cardToBuy : cardsToBuy) {
                cardToBuyScore += getBuyCardScore(cardToBuy);
            }
        }

        List<Card> sortedCards = getGame().getTradeRow().stream().filter(c -> getTrade() + card.getTradeWhenScrapped() >= c.getCost()).sorted(cardToBuyScoreDescending).collect(toList());
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

        Faction factionWithMostCards = null;
        int highestFactionCount = 0;

        for (Faction faction : factionCounts.keySet()) {
            if (factionCounts.get(faction) >= highestFactionCount) {
                factionWithMostCards = faction;
            }
        }

        return factionWithMostCards;
    }
}
