package com.arthurtimpe.pokerwiththanos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class betInput extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String watchedAction = "android.intent.action.ACTION_POWER_DISCONNECTED";

    }
}
