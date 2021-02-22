package com.arthurtimpe.pokerwiththanos;

import android.content.res.Resources;
import java.lang.*;

public class Card {
    Integer face;
    char suit;

    public Card() {
        face = 0;
        suit = ' ';
    }

    public Card(Integer set_face, char set_suit) {
        face = set_face;
        suit = set_suit;
    }

    Integer getFace() {
        return face;
    }

    char getSuit() {
        return suit;
    }

    void printCard() {
        if (face > 10) {
            switch(face) {
                case 11:
                    System.out.print("Jack");
                    break;
                case 12:
                    System.out.print("Queen");
                    break;
                case 13:
                    System.out.print("King");
                    break;
                case 14:
                    System.out.print("Ace");
                    break;
            }
        } else {
            System.out.print(face);
        }

        System.out.print(" of ");

        switch(suit) {
            case 'C':
                System.out.print("Clubs");
                break;

            case 'D':
                System.out.print("Diamonds");
                break;

            case 'H':
                System.out.print("Hearts");
                break;

            case 'S':
                System.out.print("Spades");
                break;
        }
    }

    int getImageId() {
        switch(face) {
            case 2:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_2c;

                    case 'D':
                        return R.drawable.crd_2d;

                    case 'H':
                        return R.drawable.crd_2h;

                    case 'S':
                        return R.drawable.crd_2s;
                }
                break;

            case 3:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_3c;

                    case 'D':
                        return R.drawable.crd_3d;

                    case 'H':
                        return R.drawable.crd_3h;

                    case 'S':
                        return R.drawable.crd_3s;
                }
                break;

            case 4:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_4c;

                    case 'D':
                        return R.drawable.crd_4d;

                    case 'H':
                        return R.drawable.crd_4h;

                    case 'S':
                        return R.drawable.crd_4s;
                }
                break;

            case 5:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_5c;

                    case 'D':
                        return R.drawable.crd_5d;

                    case 'H':
                        return R.drawable.crd_5h;

                    case 'S':
                        return R.drawable.crd_5s;
                }
                break;

            case 6:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_6c;

                    case 'D':
                        return R.drawable.crd_6d;

                    case 'H':
                        return R.drawable.crd_6h;

                    case 'S':
                        return R.drawable.crd_6s;
                }
                break;

            case 7:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_7c;

                    case 'D':
                        return R.drawable.crd_7d;

                    case 'H':
                        return R.drawable.crd_7h;

                    case 'S':
                        return R.drawable.crd_7s;
                }
                break;

            case 8:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_8c;

                    case 'D':
                        return R.drawable.crd_8d;

                    case 'H':
                        return R.drawable.crd_8h;

                    case 'S':
                        return R.drawable.crd_8s;
                }
                break;

            case 9:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_9c;

                    case 'D':
                        return R.drawable.crd_9d;

                    case 'H':
                        return R.drawable.crd_9h;

                    case 'S':
                        return R.drawable.crd_9s;
                }
                break;

            case 10:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_10c;

                    case 'D':
                        return R.drawable.crd_10d;

                    case 'H':
                        return R.drawable.crd_10h;

                    case 'S':
                        return R.drawable.crd_10s;
                }
                break;

            case 11:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_jc;

                    case 'D':
                        return R.drawable.crd_jd;

                    case 'H':
                        return R.drawable.crd_jh;

                    case 'S':
                        return R.drawable.crd_js;
                }
                break;

            case 12:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_qc;

                    case 'D':
                        return R.drawable.crd_qd;

                    case 'H':
                        return R.drawable.crd_qh;

                    case 'S':
                        return R.drawable.crd_qs;
                }
                break;

            case 13:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_kc;

                    case 'D':
                        return R.drawable.crd_kd;

                    case 'H':
                        return R.drawable.crd_kh;

                    case 'S':
                        return R.drawable.crd_ks;
                }
                break;

            case 14:
                switch(suit) {
                    case 'C':
                        return R.drawable.crd_ac;

                    case 'D':
                        return R.drawable.crd_ad;

                    case 'H':
                        return R.drawable.crd_ah;

                    case 'S':
                        return R.drawable.crd_as;
                }
                break;
        }

        return R.drawable.red_back;
    }
}