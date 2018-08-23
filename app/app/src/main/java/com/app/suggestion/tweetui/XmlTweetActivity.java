

package com.app.suggestion.tweetui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.suggestion.R;

public class XmlTweetActivity extends TweetUiActivity {

    @Override
    public void gotCommand(String command) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.xml_tweets_activity);
        }
    }

    @Override
    int getLayout() {
        return R.layout.activity_frame;
    }

    @Override
    Fragment createFragment() {
        return XmlTweetFragment.newInstance();
    }

    /**
     * UI fragment showing XML Tweet views for automated ui testing
     */
    public static class XmlTweetFragment extends Fragment {

        public static XmlTweetFragment newInstance() {
            return new XmlTweetFragment();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.tweetui_fragment_xml_tweet, container, false);
        }
    }
}
