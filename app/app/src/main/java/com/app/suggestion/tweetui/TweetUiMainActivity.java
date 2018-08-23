

package com.app.suggestion.tweetui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.Button;

import com.app.suggestion.BaseActivity;
import com.app.suggestion.R;
import com.app.suggestion.util.Constants;

public class TweetUiMainActivity extends BaseActivity {

    @Override
    public void gotCommand(String result) {
        result = result.toLowerCase();
        if (result.contains(Constants.INSTRUCTIONS)) {
            speak(" one search by id , two timeline");
            listenAgain();
        } else if (result.contains("search")) {

            Button button = findViewById(R.id.button_tweet_preview_activity);
            button.performClick();
        } else if (result.contains("timeline")) {

            Button button = findViewById(R.id.button_timelines_activity);
            button.performClick();
        } else {
            speak("not found try again");
            listenAgain();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweetui_activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.kit_tweetui);
        }

        final Button xmlTweetButton = findViewById(R.id.button_xml_tweet_activity);
        xmlTweetButton.setOnClickListener(v -> startActivity(new Intent(TweetUiMainActivity.this, XmlTweetActivity.class)));

        final Button tweetActivityButton = findViewById(R.id.button_tweet_activity);
        tweetActivityButton.setOnClickListener(v -> startActivity(new Intent(TweetUiMainActivity.this, TweetActivity.class)));

        final Button unqiueTweetButton = findViewById(R.id.button_unique_tweet_activity);
        unqiueTweetButton.setOnClickListener(v -> startActivity(new Intent(TweetUiMainActivity.this, UniqueTweetActivity.class)));

        final Button tweetListButton = findViewById(R.id.button_fixed_timeline_activity);
        tweetListButton.setOnClickListener(v -> startActivity(new Intent(TweetUiMainActivity.this, FixedTimelineActivity.class)));

        final Button timelineButton = findViewById(R.id.button_refresh_timeline_activity);
        timelineButton.setOnClickListener(v -> startActivity(new Intent(TweetUiMainActivity.this, TimelineActivity.class)));

        final Button timelinesButton = findViewById(R.id.button_timelines_activity);
        timelinesButton.setOnClickListener(view -> startActivity(new Intent(TweetUiMainActivity.this, TimelinesActivity.class)));

        final Button tweetSelectorButton = findViewById(
                R.id.button_tweet_preview_activity);
        tweetSelectorButton.setOnClickListener(v -> startActivity(new Intent(TweetUiMainActivity.this, TweetPreviewActivity.class)));

        final Button tweetPojoButton = findViewById(
                R.id.button_tweet_pojo_activity);
        tweetPojoButton.setOnClickListener(v -> startActivity(new Intent(TweetUiMainActivity.this, TweetPojoActivity.class)));

    }
}
