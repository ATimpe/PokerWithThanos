package com.arthurtimpe.pokerwiththanos;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    MediaPlayer musicPlayer;

    Deck deck = new Deck();
    Pot pot = new Pot();
    ArrayList<Hand> playerList = new ArrayList<Hand>();
    Flop flop;
    int pokerState = 0; //The place in which the game is currently in
                                //0: Before the game has started
                                //1: After the cards are delt and players get the first round of betting
                                //2: After the first 3 cards in the flop are revealed
                                //3: After the next card is revealed (the Turn)
                                //4: After the final card is revealed (the River)
                                //5: After all final bets are placed and the winner of the round is declared

    int playerTurn = 0;
    int player1Score;
    int player2Score;
    boolean isEvent = false;
    int playerBet;
    boolean startingBet = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerList.add(new Hand(getIntent().getStringExtra("SET_NAME"), getIntent().getIntExtra("SET_MONEY", 10000)));
        playerList.add(new Hand("Thanos", getIntent().getIntExtra("SET_MONEY", 10000)));

        Button callBtn = (Button) findViewById(R.id.button_call);
        final Button betBtn = (Button) findViewById(R.id.button_bet);
        Button foldBtn = (Button) findViewById(R.id.button_fold);

        updateText();

        pokerStart();

        playMusic();

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pokerState == 0 || pokerState == 5 || isEvent || playerTurn == 1) {
                    return;
                }

                Call(0);
            }
        });

        betBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pokerState == 0 || pokerState == 5 || isEvent || playerTurn == 1) {
                    return;
                }

                betMessage();
            }
        });

        foldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pokerState == 0 || pokerState == 5 || isEvent || playerTurn == 1) {
                    return;
                }

                Fold(0);
            }
        });
    }



    //POKER STATE EVENTS

    void pokerStart() {
        deck = playerList.get(0).setHand(deck);
        deck = playerList.get(1).setHand(deck);

        showHand(0);

        flop = new Flop(deck.pullCard(), deck.pullCard(), deck.pullCard(), deck.pullCard(), deck.pullCard());

        moneyToPot(pot.getDealerButton(), pot.getBlind_low());
        pot.passDealerButton();
        moneyToPot(pot.getDealerButton(), pot.getBlind_high());
        pot.passDealerButton();
        updateText();

        iv = (ImageView) findViewById(R.id.card_thanos1);
        iv.setImageResource(R.drawable.red_back);
        iv = (ImageView) findViewById(R.id.card_thanos2);
        iv.setImageResource(R.drawable.red_back);

        playerTurn = pot.getDealerButton();
        pokerState++;
        if (playerTurn == 1) {
            thanosTurn();
        }
    }

    void pokerEnd() {
        showHand(1);
        player1Score = playerList.get(0).determineHand(flop, 5);
        player2Score = playerList.get(1).determineHand(flop, 5);

        if (player1Score > player2Score) {
            giveWinnings(0);
        } else if (player1Score < player2Score) {
            giveWinnings(1);
        } else {
            giveWinnings(-1);
        }

        pokerState = 5;
    }

    void thanosTurn() {
        if (playerTurn != 1) {
            return;
        }

        int callChance;
        int betChance;

        switch(pokerState) {
            case 1:
                if (playerList.get(1).getCard1().getSuit() == playerList.get(1).getCard2().getSuit()) {
                    callChance = 60;
                    betChance = 40;
                } else if (playerList.get(1).getCard1().getFace() == playerList.get(1).getCard2().getFace()) {
                    callChance = 70;
                    betChance = 30;
                } else {
                    callChance = 80;
                    betChance = 10;
                }
                break;

            case 2:
                callChance = 40 - (4 * playerList.get(0).determineHand(flop.getFlop(), 3));
                betChance = 40 + (6 * playerList.get(0).determineHand(flop.getFlop(), 3));
                break;

            case 3:
                callChance = 40 - (4 * playerList.get(0).determineHand(flop.getTurn(), 4));
                betChance = 40 + (6 * playerList.get(0).determineHand(flop.getTurn(), 4));
                break;

            case 4:
                callChance = 40 - (4 * playerList.get(0).determineHand(flop, 5));
                betChance = 40 + (6 * playerList.get(0).determineHand(flop, 5));
                break;

            default:
                callChance = 60;
                betChance = 20;
                break;
        }

        if (pot.getCurrent_bet() > 1000 && 100 - betChance - callChance > 25) { // If they user bets a lot and the chance of folding is more than 25, it increaces odds of folding
            callChance -= 10;
            betChance -= 10;
        }

        if (playerList.get(0).getPlayerMoney() == 0) {
            callChance += betChance / 2;
            betChance = 0;
        }

        int choice = (int) (Math.random() * 100);

        if (choice < callChance) {
            Call(1);
        } else if (choice >= callChance && choice < betChance + callChance) {
            if (startingBet) {
                Bet(1, 100);
            } else {
                Raise(1, pot.getCurrent_bet());
            }
        } else if (choice >= betChance + callChance) {
            Fold(1);
        }


        updateText();
    }

    void nextPokerTurn() {
        playerTurn = getOtherPlayer(playerTurn);
        thanosTurn();
    }

    void nextPokerState() {
        startingBet = true;
        Button b = (Button) findViewById(R.id.button_bet);
        b.setText(R.string.button_text_bet);
        b = (Button) findViewById(R.id.button_call);
        b.setText(R.string.button_text_call);
        pokerState++;
        pot.passDealerButton();
        pot.setCurrent_bet(0);
        playerTurn = pot.getDealerButton();
        resetPlayerParamaters(false);
        updateText();

        switch (pokerState) {
            case 2:
                showFlop();
                break;

            case 3:
                showTurn();
                break;

            case 4:
                showRiver();
                break;

            case 5:
                pokerEnd();
                return;
        }

        if (playerTurn == 1) {
            thanosTurn();
        }
    }

    void nextPokerRound() {
        deck.shuffleDeck();
        Button b = (Button) findViewById(R.id.button_bet);
        b.setText(R.string.button_text_bet);
        pot.resetPot();
        pokerState = 0;
        startingBet = true;
        pot.passDealerButtonRound();
        resetPlayerParamaters(false);
        showStart();
        pokerStart();
    }



    //BUTTON EVENTS

    void Bet(int betterId, int current_bet) {
        startingBet = false;
        Button b = (Button) findViewById(R.id.button_bet);
        b.setText(R.string.button_text_raise);
        pot.setCurrent_bet(current_bet);
        moneyToPot(betterId, current_bet);
        playerList.get(betterId).setCurrentPlayerBet(current_bet);
        playerList.get(betterId).setPlayerState("Bet");
        playerList.get(getOtherPlayer(betterId)).setPlayerState("Under");

        actionList(playerList.get(betterId).getPlayerName() + " has bet " + current_bet + "...", "turn");
    }

    void Call(int betterId) {
        boolean check = false;
        String action;

        moneyToPot(betterId, pot.getCurrent_bet() - playerList.get(betterId).getCurrentPlayerBet());
        playerList.get(betterId).setCurrentPlayerBet(pot.getCurrent_bet());
        playerList.get(betterId).setPlayerState("Bet");
        check = playerList.get(betterId).getPlayerState().equals("Bet") && playerList.get(getOtherPlayer(betterId)).getPlayerState().equals("Bet");
        if (check) {
            action = "state";
        } else {
            action = "turn";
        }

        if (playerList.get(0).getPlayerMoney() <= 0 || playerList.get(1).getPlayerMoney() <= 0) {
            action = "all in";
        }

        actionList(playerList.get(betterId).getPlayerName() + " has called...", action);
    }

    void Raise(int betterId, int raise) {
        pot.setCurrent_bet(pot.getCurrent_bet() + raise);
        if (playerList.get(betterId).getCurrentPlayerBet() < pot.getCurrent_bet()) {
            moneyToPot(betterId, pot.getCurrent_bet() - playerList.get(betterId).getCurrentPlayerBet());
            playerList.get(betterId).setCurrentPlayerBet(pot.getCurrent_bet() - playerList.get(betterId).getCurrentPlayerBet());
        }
        moneyToPot(betterId, raise);
        playerList.get(betterId).setCurrentPlayerBet(pot.getCurrent_bet());
        playerList.get(betterId).setPlayerState("Bet");
        playerList.get(getOtherPlayer(betterId)).setPlayerState("Under");

        actionList(playerList.get(betterId).getPlayerName() + " has raised " + raise, "turn");
    }

    void Fold(final int foldId) {
        actionList(playerList.get(foldId).getPlayerName() + " has folded...", "none");
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                giveWinnings(getOtherPlayer(foldId));
            }
        }.start();
    }



    //GRAPHIC CONTROLERS

    void showStart() {
        iv = (ImageView) findViewById(R.id.card_flop1);
        iv.setImageResource(R.drawable.red_back);

        iv = (ImageView) findViewById(R.id.card_flop2);
        iv.setImageResource(R.drawable.red_back);

        iv = (ImageView) findViewById(R.id.card_flop3);
        iv.setImageResource(R.drawable.red_back);

        iv = (ImageView) findViewById(R.id.card_flop4);
        iv.setImageResource(R.drawable.red_back);

        iv = (ImageView) findViewById(R.id.card_flop5);
        iv.setImageResource(R.drawable.red_back);
    }

    void showFlop() {
        iv = (ImageView) findViewById(R.id.card_flop1);
        iv.setImageResource(flop.getCard1().getImageId());

        iv = (ImageView) findViewById(R.id.card_flop2);
        iv.setImageResource(flop.getCard2().getImageId());

        iv = (ImageView) findViewById(R.id.card_flop3);
        iv.setImageResource(flop.getCard3().getImageId());

        iv = (ImageView) findViewById(R.id.card_flop4);
        iv.setImageResource(R.drawable.red_back);

        iv = (ImageView) findViewById(R.id.card_flop5);
        iv.setImageResource(R.drawable.red_back);
    }

    void showTurn() {
        iv = (ImageView) findViewById(R.id.card_flop1);
        iv.setImageResource(flop.getCard1().getImageId());

        iv = (ImageView) findViewById(R.id.card_flop2);
        iv.setImageResource(flop.getCard2().getImageId());

        iv = (ImageView) findViewById(R.id.card_flop3);
        iv.setImageResource(flop.getCard3().getImageId());

        iv = (ImageView) findViewById(R.id.card_flop4);
        iv.setImageResource(flop.getCard4().getImageId());

        iv = (ImageView) findViewById(R.id.card_flop5);
        iv.setImageResource(R.drawable.red_back);
    }

    void showRiver() {
        iv = (ImageView) findViewById(R.id.card_flop1);
        iv.setImageResource(flop.getCard1().getImageId());

        iv = (ImageView) findViewById(R.id.card_flop2);
        iv.setImageResource(flop.getCard2().getImageId());

        iv = (ImageView) findViewById(R.id.card_flop3);
        iv.setImageResource(flop.getCard3().getImageId());

        iv = (ImageView) findViewById(R.id.card_flop4);
        iv.setImageResource(flop.getCard4().getImageId());

        iv = (ImageView) findViewById(R.id.card_flop5);
        iv.setImageResource(flop.getCard5().getImageId());
    }

    void showHand(int handId) {
        if (handId == 0) {
            iv = (ImageView) findViewById(R.id.card_player1);
        } else {
            iv = (ImageView) findViewById(R.id.card_thanos1);
        }

        iv.setImageResource(playerList.get(handId).getCard1().getImageId());

        if (handId == 0) {
            iv = (ImageView) findViewById(R.id.card_player2);
        } else {
            iv = (ImageView) findViewById(R.id.card_thanos2);
        }

        iv.setImageResource(playerList.get(handId).getCard2().getImageId());
    }

    void updateText() {
        changeTextView(R.id.playerMoneyCount, "Player Money: $" + playerList.get(0).getPlayerMoney());
        changeTextView(R.id.thanosMoneyCount, "Thanos Money: $" + playerList.get(1).getPlayerMoney());
        changeTextView(R.id.potMoneyCount, "Current Pot: $" + pot.getCurrent_pot());
        changeTextView(R.id.potBet, "Current Bet: $" + pot.getCurrent_bet());
    }

    public void changeTextView(int viewId, String newText) {
        TextView playerMoneyText = (TextView)findViewById(viewId);
        playerMoneyText.setText(newText);
    }

    void setIsEvent (boolean isEvent) {
        this.isEvent = isEvent;
    }



    //VARIABLE SETTERS

    void moneyToPot(int betterId, int amount) {
        playerList.get(betterId).giveMoney(amount * -1);
        pot.setCurrent_pot(pot.getCurrent_pot() + amount);
        updateText();
    }

    void giveWinnings(int winner) {
        if (playerList.get(0).getPlayerMoney() <= 0) {
            actionList(playerList.get(1).getPlayerName() + " has one the game!", "game");
            return;
        } else if (playerList.get(1).getPlayerMoney() <= 0) {
            actionList(playerList.get(0).getPlayerName() + " has one the game!", "game");
            return;
        }

        if (winner >= 0) {
            playerList.get(winner).giveMoney(pot.getCurrent_pot());
            updateText();
            actionList(playerList.get(winner).getPlayerName() + " has one the round!", "round");
        } else {
            playerList.get(0).giveMoney(pot.getCurrent_pot() / 2);
            playerList.get(1).giveMoney(pot.getCurrent_pot() / 2);
            updateText();
            actionList("It's a draw...", "round");
        }
    }

    void actionList(String action, final String nextAction) {
        makeToast(action);
        setIsEvent(true);

        new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) { }

            public void onFinish() {
                setIsEvent(false);

                if (nextAction.equals("turn")) {
                    nextPokerTurn();
                }

                if (nextAction.equals("state")) {
                    nextPokerState();
                }

                if (nextAction.equals("round")) {
                    nextPokerRound();
                }

                if (nextAction.equals("all in")) {
                    pokerEnd();
                }

                if (nextAction.equals("game")) {
                    endGame();
                }
            }
        }.start();
    }



    //VARIABLE GETTERS

    int getOtherPlayer(int playerId) {
        if (playerId == 0) {
            return 1;
        } else {
            return 0;
        }
    }



    //MISC

    void playMusic() {
        int randSong = (int) (Math.random() * 8);
        int songId = R.raw.mus1;
        int songLen = 308000;

        switch (randSong) {
            case 0:
                songId = R.raw.mus1;
                songLen = 308000;
                break;

            case 1:
                songId = R.raw.mus2;
                songLen = 252000;
                break;

            case 2:
                songId = R.raw.mus3;
                songLen = 360000;
                break;

            case 3:
                songId = R.raw.mus4;
                songLen = 408000;
                break;

            case 4:
                songId = R.raw.mus5;
                songLen = 406000;
                break;

            case 5:
                songId = R.raw.mus6;
                songLen = 429000;
                break;

            case 6:
                songId = R.raw.mus7;
                songLen = 300000;
                break;

            case 7:
                songId = R.raw.mus8;
                songLen = 137000;
                break;

        }

        musicPlayer = MediaPlayer.create(MainActivity.this, songId);
        musicPlayer.start();
        new CountDownTimer(songLen, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                playMusic();
            }
        }.start();
    }

    public void betMessage() {
        final AlertDialog.Builder betAlert = new AlertDialog.Builder(MainActivity.this);

        final SeekBar betBar = new SeekBar(this);
        final TextView betVal = new TextView (this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final int BAR_MIN;

        if (startingBet) {
            BAR_MIN = 1;
        } else {
            BAR_MIN = pot.getCurrent_bet() / 100;
        }

        if (playerList.get(0).getPlayerMoney() <= playerList.get(1).getPlayerMoney()) {
            betBar.setMax((playerList.get(0).getPlayerMoney() - BAR_MIN) / 100);
        } else if (playerList.get(0).getPlayerMoney() > playerList.get(1).getPlayerMoney()) {
            betBar.setMax((playerList.get(1).getPlayerMoney() - BAR_MIN) / 100);
        }

        betBar.setKeyProgressIncrement(1);

        betVal.setText("    $" + String.valueOf(BAR_MIN * 100));
        playerBet = BAR_MIN * 100;

        layout.addView(betBar);
        layout.addView(betVal);

        betAlert.setView(layout);

        betAlert.setMessage("How much would you like to bet?\n")
                .setPositiveButton(R.string.button_text_bet, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (startingBet) {
                            Bet(0, playerBet);
                        } else {
                            Raise(0, playerBet);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        betBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                betVal.setText("    $" + String.valueOf((progress + BAR_MIN) * 100));
                playerBet = (progress + BAR_MIN) * 100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AlertDialog goodAlert = betAlert.create();
        goodAlert.show();


    }

    void makeToast(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

    void resetPlayerParamaters(boolean resetMoney) {
        for (int i = 0; i < 2; i++) {
            playerList.get(i).setPlayerState("Wait");
            playerList.get(i).setCurrentPlayerBet(0);
            if (resetMoney) {
                playerList.get(i).setPlayerMoney(10000);
            }
        }
    }

    void endGame() {
        playerList.remove(1);
        playerList.remove(0);
        startActivity(new Intent(MainActivity.this, MenuActivity.class));
    }
}
