
package com.app.suggestion;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.app.suggestion.tweetcomposer.TweetComposerMainActivity;
import com.app.suggestion.tweetui.TweetUiMainActivity;
import com.app.suggestion.twittercore.TwitterCoreMainActivity;
import com.app.suggestion.util.Constants;


public class TwitterSampleActivity extends BaseActivity {

    @Override
    public void gotCommand(String result) {
        result = result.toLowerCase();
        if (result.contains(Constants.INSTRUCTIONS)) {
            speak(" one compose , two Tweets");
            listenAgain();
        } else if (result.contains("login")) {

            Button button = findViewById(R.id.login);
            button.performClick();
        } else if (result.contains("compose")) {

            Button button = findViewById(R.id.compose);
            button.performClick();
        } else if (result.contains("tweet")) {

            Button button = findViewById(R.id.tweets);
            button.performClick();
        }
        else{
            speak("not found try again");
            listenAgain();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        openingmessage = "Here you go, Twitter opened, say instructions to read instructions";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_activity);
        listenAgain();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.twitter_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onTwitterCore(View view) {
        startActivity(new Intent(this, TwitterCoreMainActivity.class));
    }

    public void onTweetComposer(View view) {
        startActivity(new Intent(this, TweetComposerMainActivity.class));
    }

    public void onTweetUi(View view) {
        startActivity(new Intent(this, TweetUiMainActivity.class));
    }
}
