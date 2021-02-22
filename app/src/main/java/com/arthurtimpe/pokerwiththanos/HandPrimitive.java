package com.arthurtimpe.pokerwiththanos;

import java.util.ArrayList;

public abstract class HandPrimitive {
    ArrayList<Card> subtract3Kind(ArrayList<Card> newHand) {
        for (int i = 0; i < newHand.size() - 2; i++) {
            if (newHand.get(i).getFace() == newHand.get(i + 1).getFace() && newHand.get(i).getFace() == newHand.get(i + 2).getFace()) {
                newHand.remove(i);
                newHand.remove(i);
                newHand.remove(i);
                return newHand;
            }
        }

        return newHand;
    }

    ArrayList<Card> subtractPair(ArrayList<Card> newHand) {
        for (int i = 0; i < newHand.size() - 1; i++) {
            if (newHand.get(i).getFace() == newHand.get(i + 1).getFace()) {
                newHand.remove(i);
                newHand.remove(i);
                return newHand;
            }
        }

        return newHand;
    }
}
