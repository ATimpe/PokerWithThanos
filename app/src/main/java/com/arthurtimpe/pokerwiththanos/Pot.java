package com.arthurtimpe.pokerwiththanos;

public class Pot {
    int blind_low;
    int current_bet;
    int current_pot;
    int dealerButton = 0; // The player who has the dealer button will always be the first to bet
    int dealerButtonRound = 0; // Changes every round rather than every turn
    boolean check = false;

    Pot (){
        blind_low = 100;
        current_bet = 0;
        current_pot = 0;
    }

    public int getBlind_low() {
        return blind_low;
    }

    public int getBlind_high() {
        return blind_low * 2;
    }

    public void setBlind_low(int blind_low) {
        this.blind_low = blind_low;
    }

    public int getCurrent_bet() {
        return current_bet;
    }

    public void setCurrent_bet(int current_bet) {
        this.current_bet = current_bet;
    }

    public int getCurrent_pot() {
        return current_pot;
    }

    public void setCurrent_pot(int current_pot) {
        this.current_pot = current_pot;
    }

    public int getDealerButton() {
        return dealerButton;
    }

    public void setDealerButton(int dealerButton) {
        this.dealerButton = dealerButton;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    void raiseBlind() {
        blind_low += 100;
    }

    void resetPot() {
        current_bet = 0;
        current_pot = 0;
        check = false;
    }

    void passDealerButton() {
        dealerButton++;
        if (dealerButton >= 2) {
            dealerButton -= 2;
        }
    }

    void passDealerButtonRound() {
        dealerButtonRound++;
        if (dealerButtonRound >= 2) {
            dealerButtonRound -= 2;
        }

        dealerButton = dealerButtonRound;
    }
}
