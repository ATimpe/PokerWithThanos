package com.arthurtimpe.pokerwiththanos;

public class Deck {
    Card[][] MyDeck = new Card[13][4];
    boolean[][] MyDeckDrawn = new boolean[13][4];

    public Deck() {
        char cur_suit = 'C';

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 4; j++) {

                if (j == 0) {
                    cur_suit = 'C';
                }
                if (j == 1) {
                    cur_suit = 'D';
                }
                if (j == 2) {
                    cur_suit = 'H';
                }
                if (j == 3) {
                    cur_suit = 'S';
                }
                MyDeck[i][j] = new Card(i + 2, cur_suit);
                MyDeckDrawn[i][j] = false;
            }
        }
    }

    Card pullCard() {
        int suit_index;
        int face_index;

        while (true) {
            suit_index = (int) (Math.random() * 4);
            face_index = (int) (Math.random() * 13);

            if (!MyDeckDrawn[face_index][suit_index]) {
                break;
            }
        }

        MyDeckDrawn[face_index][suit_index] = true;
        return MyDeck[face_index][suit_index];
    }

    void shuffleDeck() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 4; j++) {
                MyDeckDrawn[i][j] = false;
            }
        }
    }
}
