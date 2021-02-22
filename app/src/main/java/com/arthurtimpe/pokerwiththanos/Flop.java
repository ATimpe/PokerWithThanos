package com.arthurtimpe.pokerwiththanos;

import android.content.Context;

public class Flop {
    Card Card1;
    Card Card2;
    Card Card3;
    Card Card4;
    Card Card5;

    public Flop(Card card1, Card card2, Card card3, Card card4, Card card5) {
        this.setMiddle(card1, card2, card3, card4, card5);
    }

    public Flop(Card card1, Card card2, Card card3, Card card4) {
        Card1 = card1;
        Card2 = card2;
        Card3 = card3;
        Card4 = card4;
    }

    public Flop(Card card1, Card card2, Card card3) {
        Card1 = card1;
        Card2 = card2;
        Card3 = card3;
    }

    public Card getCard1() {
        return Card1;
    }

    public Card getCard2() {
        return Card2;
    }

    public Card getCard3() {
        return Card3;
    }

    public Card getCard4() {
        return Card4;
    }

    public Card getCard5() {
        return Card5;
    }

    public Flop getFlop() {
        Flop f = new Flop(Card1, Card2, Card3);
        return f;
    }

    public Flop getTurn() {
        Flop f = new Flop(Card1, Card2, Card3, Card4);
        return f;
    }

    void setMiddle(Card card1, Card card2, Card card3, Card card4, Card card5) {
        Card1 = card1;
        Card2 = card2;
        Card3 = card3;
        Card4 = card4;
        Card5 = card5;
    }
}
