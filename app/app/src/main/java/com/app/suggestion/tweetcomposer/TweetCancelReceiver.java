

package com.app.suggestion.tweetcomposer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TweetCancelReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TweetCancelReceiver", "User cancelled compose tweet");
    }
}

