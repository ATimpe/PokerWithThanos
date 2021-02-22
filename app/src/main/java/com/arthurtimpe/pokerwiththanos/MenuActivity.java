package com.arthurtimpe.pokerwiththanos;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    int setMoney = 10000;
    String setName = "Average Joe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Intent intent = new Intent(this, MainActivity.class);

        Button btnStart = (Button) findViewById(R.id.button_start);
        Button btnSetMoney = (Button) findViewById(R.id.button_player_money);
        Button btnSetName = (Button) findViewById(R.id.button_player_name);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("SET_NAME", setName);
                intent.putExtra("SET_MONEY", setMoney);
                startActivity(intent);
            }
        });

        btnSetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moneyChangeAlert();
            }
        });

        btnSetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameChangeAlert();
            }
        });
    }

    void moneyChangeAlert() {
        final AlertDialog.Builder betAlert = new AlertDialog.Builder(this);

        final SeekBar betBar = new SeekBar(this);
        final TextView betVal = new TextView (this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final int BET_MIN = 50;
        final int oldMoney = setMoney;

        betBar.setKeyProgressIncrement(1);
        betBar.setMax(200 - BET_MIN);

        betVal.setText("    $" + String.valueOf(BET_MIN * 100));

        layout.addView(betBar);
        layout.addView(betVal);

        betAlert.setView(layout);

        betAlert.setMessage("How much money should each player start with?\n")
                .setPositiveButton(R.string.button_set, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setMoney = oldMoney;
                    }
                });

        betBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                betVal.setText("    $" + String.valueOf((progress + BET_MIN) * 100));
                setMoney = (progress + BET_MIN) * 100;
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

    void nameChangeAlert() {
        final AlertDialog.Builder betAlert = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        betAlert.setView(input);

        betAlert.setMessage("What is your name?\n")
                .setPositiveButton(R.string.button_set, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setName = input.getText().toString();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog goodAlert = betAlert.create();
        goodAlert.show();
    }
}
