package com.arthurtimpe.pokerwiththanos;

import java.util.ArrayList;

public class Hand extends HandPrimitive{
    Card Card1;
    Card Card2;
    String playerName;
    int playerMoney;
    String playerState = "Wait"; // List of player states:
                                // "Wait": When the player has yet to bet anything in the round
                                // "Bet": When the player's current bet matches that of the table's current bet
                                // "Under": When the player's current bet is less than the table's current bet
    int currentPlayerBet = 0;

    public Hand() {
        Card1 = new Card();
        Card2 = new Card();
        playerName = "Average Joe";
        playerMoney = 10000;
    }

    public Hand(String playerName, int playerMoney) {
        this.playerName = playerName;
        this.playerMoney = playerMoney;
    }

    public Hand(String playerName) {
        Card1 = new Card();
        Card2 = new Card();
        this.playerName = playerName;
        playerMoney = 10000;
    }

    public Hand(int playerMoney) {
        Card1 = new Card();
        Card2 = new Card();
        playerName = "Average Joe";
        this.playerMoney = playerMoney;
    }

    public Card getCard1() {
        return Card1;
    }

    public Card getCard2() {
        return Card2;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerMoney() {
        return playerMoney;
    }

    public String getPlayerState() {
        return playerState;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerMoney(int playerMoney) {
        this.playerMoney = playerMoney;
    }

    public void giveMoney(int playerMoney) {
        this.playerMoney += playerMoney;
    }

    public void setPlayerState(String playerState) {
        this.playerState = playerState;
    }

    public int getCurrentPlayerBet() {
        return currentPlayerBet;
    }

    public void setCurrentPlayerBet(int currentPlayerBet) {
        this.currentPlayerBet = currentPlayerBet;
    }

    Deck setHand(Deck myDeck) {
        Card1 = myDeck.pullCard();
        Card2 = myDeck.pullCard();
        return myDeck;
    }

    void printHand() {
        Card1.printCard();
        System.out.print(" and ");

        if (Card1.getFace() == 8 || Card1.getFace() == 14) {
            System.out.print("an ");
        } else {
            System.out.print("a ");
        }

        Card2.printCard();
        System.out.println();
    }

    Integer determineHand(Flop flop, int flopSize) {

        ArrayList<Card> newHand = new ArrayList();
        newHand.add(Card1);
        newHand.add(Card2);
        newHand.add(flop.getCard1());
        newHand.add(flop.getCard2());
        newHand.add(flop.getCard3());
        if (flopSize > 3) {
            newHand.add(flop.getCard4());
        }
        if (flopSize > 4) {
            newHand.add(flop.getCard5());
        }

        newHand = orderHand(newHand, flopSize + 2);

        // Test for Royal Flush
        if (testRoyal(new ArrayList(newHand))) {
            System.out.println(playerName + " has a Royal Flush!");
            return 9;
        }

        // Test for Straight Flush
        if (testSFlush(new ArrayList(newHand))) {
            System.out.println(playerName + " has a Straight Flush!");
            return 8;
        }

        // Test for Four of a Kind
        if (test4Kind(new ArrayList(newHand))) {
            System.out.println(playerName + " has a 4 of a Kind!");
            return 7;
        }

        // Test for Full House
        if (testFullHouse(new ArrayList(newHand))) {
            System.out.println(playerName + " has a Full House!");
            return 6;
        }

        // Test for Flush
        if (testFlush(new ArrayList(newHand), 'C')) { // NOTE: ALWAYS DECLARE nextSuit AS 'C'
            System.out.println(playerName + " has a Flush!");
            return 5;
        }

        // Test for Straight
        if (testStraight(new ArrayList(newHand))) {
            System.out.println(playerName + " has a Straight!");
            return 4;
        }

        // Test for Three of a Kind
        if (test3Kind(new ArrayList(newHand))) {
            System.out.println(playerName + " has a 3 of a Kind.");
            return 3;
        }

        // Test for Two Pair
        if (test2Pair(new ArrayList(newHand))) {
            System.out.println(playerName + " has a 2 Pair");
            return 2;
        }

        // Test for One Pair
        if (testPair(new ArrayList(newHand), true)) {
            return 1;
        }

        // Return High Card
        if (highCard(newHand).getFace() < 11) {
            System.out.println(playerName + " has " + highCard(newHand).getFace() + " High");
        } else if (highCard(newHand).getFace() == 11) {
            System.out.println(playerName + " has Jack High");
        } else if (highCard(newHand).getFace() == 12) {
            System.out.println(playerName + " has Queen High");
        } else if (highCard(newHand).getFace() == 13) {
            System.out.println(playerName + " has King High");
        } else {
            System.out.println(playerName + " has Ace High");
        }
        return 0;
    }

    ArrayList<Card> orderHand(ArrayList<Card> curHand, int flopSize) {
        int low = 15;
        int low_index = 0;

        ArrayList<Card> newHand = new ArrayList();
        for (int i = 0; i < flopSize; i++) {
            for (int j = 0; j < curHand.size(); j++) {
                if (curHand.get(j).getFace() < low) {
                    low = curHand.get(j).getFace();
                    low_index = j;
                }
            }

            newHand.add(curHand.get(low_index));
            low = 15;
            curHand.remove(low_index);
        }

        return newHand;
    }

    boolean testRoyal(ArrayList<Card> newHand) {
        for (int i = 0; i < newHand.size() - 4; i++) {
            if (getCardFace(newHand, i) == 10) {
                if (testSFlush(newHand)) {
                    return true;
                }
            }
        }

        return false;
    }

    boolean testSFlush(ArrayList<Card> newHand) {
        for (int i = 0; i < newHand.size() - 4; i++) {
            if (cardFaceMatch(newHand, i, i + 1)
                    && cardSuitMatch(newHand, i, i + 1)) {

                if (cardFaceMatch(newHand, i, i + 2)
                        && cardSuitMatch(newHand, i, i + 2)) {

                    if (cardFaceMatch(newHand, i, i + 3)
                            && cardSuitMatch(newHand, i, i + 3)) {

                        if (cardFaceMatch(newHand, i, i + 4)
                                && cardSuitMatch(newHand, i, i + 4)) {

                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    boolean test4Kind(ArrayList<Card> newHand) {
        for (int i = 0; i < newHand.size() - 3; i++) {
            if (cardFaceMatch(newHand, i, i + 1)
                    && cardFaceMatch(newHand, i, i + 2)
                    && cardFaceMatch(newHand, i, i + 3)) {
                return true;
            }
        }

        return false;
    }

    boolean testFullHouse(ArrayList<Card> newHand) {
        if (test3Kind(newHand)) {
            newHand = subtract3Kind(newHand);
        } else {
            return false;
        }

        return testPair(newHand, false);
    }

    boolean testFlush(ArrayList<Card> newHand, char nextSuit) { // NOTE: ALWAYS DECLARE nextSuit AS 'C'
        int suitCount = 0;

        for (int i = 0; i < newHand.size(); i++) {
            if (cardSuitMatch(newHand, i, nextSuit)) {
                suitCount++;
            }

            if (suitCount >= 5) {
                return true;
            }
        }

        if (nextSuit == 'C') {
            return testFlush(newHand, 'D');
        }

        if (nextSuit == 'D') {
            return testFlush(newHand, 'H');
        }

        if (nextSuit == 'H') {
            return testFlush(newHand, 'S');
        }

        return false;
    }

    boolean testStraight(ArrayList<Card> newHand) {
        for (int i = 0; i < newHand.size() - 1; i++) {
            if (cardFaceMatch(newHand, i, i + 1)) {
                newHand.remove(i);
                i--;
            }
        }

        for (int i = 0; i < newHand.size() - 4; i++) {
            if (getCardFace(newHand, i) == getCardFace(newHand, i + 1) + 1
                    && getCardFace(newHand, i) == getCardFace(newHand, i + 2) + 2
                    && getCardFace(newHand, i) == getCardFace(newHand, i + 3) + 3
                    && getCardFace(newHand, i) == getCardFace(newHand, i + 4) + 4) {
                return true;
            }
        }

        return false;
    }

    boolean test3Kind(ArrayList<Card> newHand) {
        for (int i = 0; i < newHand.size() - 2; i++) {
            if (cardFaceMatch(newHand, i, i + 1)
                    && cardFaceMatch(newHand, i, i + 2)) {
                return true;
            }
        }

        return false;
    }

    boolean test2Pair(ArrayList<Card> newHand) {
        if (!testPair(newHand, false)) {
            return false;
        }

        newHand = subtractPair(newHand);

        return testPair(newHand, false);
    }

    boolean testPair(ArrayList<Card> newHand, boolean print) {
        for (int i = newHand.size() - 1; i > 0; i--) {
            if (cardFaceMatch(newHand, i, i - 1)) {
                if (print) {
                    if (getCardFace(newHand, i) < 11) {
                        System.out.println(playerName + " has a pair of " + getCardFace(newHand, i) + "'s");
                    } else if (getCardFace(newHand, i) == 11) {
                        System.out.println(playerName + " has a pair of Jacks");
                    } else if (getCardFace(newHand, i) == 12) {
                        System.out.println(playerName + " has a pair of Queens");
                    } else if (getCardFace(newHand, i) == 13) {
                        System.out.println(playerName + " has a pair of Kings");
                    } else {
                        System.out.println(playerName + " has a pair of Aces");
                    }
                }

                return true;
            }
        }

        return false;
    }

    public Card highCard(ArrayList<Card> newHand) {
        int highFace = 0;
        int highIndex = 0;

        for (int i = 0; i < newHand.size(); i++) {
            if (getCardFace(newHand, i) > highFace) {
                highFace = getCardFace(newHand, i);
                highIndex = i;
            }
        }

        return newHand.get(highIndex);
    }

    int getCardFace(ArrayList<Card> newHand, int i) {
        return newHand.get(i).getFace();
    }
    char getCardSuit(ArrayList<Card> newHand, int i) {
        return newHand.get(i).getSuit();
    }
    boolean cardFaceMatch(ArrayList<Card> newHand, int i, int j) { return getCardFace(newHand, i) == getCardFace(newHand, j); }
    boolean cardSuitMatch(ArrayList<Card> newHand, int i, int j) { return getCardSuit(newHand, i) == getCardSuit(newHand, j); }

    boolean cardSuitMatch(ArrayList<Card> newHand, int i, char j) { return getCardSuit(newHand, i) == j; }
}