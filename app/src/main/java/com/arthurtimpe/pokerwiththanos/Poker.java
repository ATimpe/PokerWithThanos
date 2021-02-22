package com.arthurtimpe.pokerwiththanos;

public class Poker {

    public void main(String[] args) {
        Deck deck = new Deck();
        Hand Hand1 = new Hand();
        Hand Hand2 = new Hand("Thanos");
        Pot pot = new Pot();
        int player1Score;
        int player2Score;

        System.out.println("The cards are being dealt...");
        System.out.println();
        deck = Hand1.setHand(deck);
        deck = Hand2.setHand(deck);

        System.out.println("Currently, you have...");
        System.out.println();
        Hand1.printHand();
        System.out.println();

        Flop flop = new Flop(deck.pullCard(), deck.pullCard(), deck.pullCard(), deck.pullCard(), deck.pullCard());

        //flop.showFlop();

        /*

        System.out.println("The flop is...");
        flop.showFlop();

        System.out.println("The turn is...");
        flop.showTurn();

        System.out.println("The river is...");
        flop.showRiver();

        player1Score = Hand1.determineHand(flop);
        player2Score = Hand2.determineHand(flop);

        if (player1Score > player2Score) {
            System.out.println(Hand1.getPlayerName() + " wins the hand!");
            giveWinnings(Hand1, Hand2, pot);
        } else if (player1Score < player2Score) {
            System.out.println(Hand2.getPlayerName() + " wins the hand!");
        } else {
            System.out.println("It's a tie.");
        }
        */
    }

    /*
    static void giveWinnings(Hand winHand, Hand loseHand, Pot pot) {
        winHand.setPlayerMoney(winHand.getPlayerMoney() + pot.getPot());
        loseHand.setPlayerMoney(loseHand.getPlayerMoney() - pot.getPot());
        if (loseHand.getPlayerMoney() < 0) {
            endGame(winHand);
        }

        pot.resetPot();
    }
    */

    static void endGame(Hand winHand) {
        System.out.println(winHand.getPlayerName() + "has won!");
    }
}
