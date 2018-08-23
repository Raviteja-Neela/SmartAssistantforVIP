

package com.app.suggestion.tweetui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.suggestion.BuildConfig;
import com.app.suggestion.R;
import com.app.suggestion.tweetcomposer.BaseFragment;
import com.app.suggestion.twittercore.TwitterCoreMainActivity;
import com.app.suggestion.util.Constants;
import com.mopub.nativeads.MoPubAdAdapter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.mopub.TwitterMoPubAdAdapter;
import com.twitter.sdk.android.mopub.TwitterStaticNativeAdRenderer;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

/**
 * UserTimelineFragment demonstrates a TimelineListAdapter with a UserTimeline.
 */
public class UserTimelineFragment extends BaseFragment {

    public static UserTimelineFragment newInstance() {
        return new UserTimelineFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // launch the app login activity when a guest user tries to favorite a Tweet
        final Callback<Tweet> actionCallback = new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                // Intentionally blank
                System.out.println("TWEETS" + result.data.text);
            }

            @Override
            public void failure(TwitterException exception) {
                if (exception instanceof TwitterAuthException) {
                    startActivity(TwitterCoreMainActivity.newIntent(getActivity()));
                }
            }
        };


        final UserTimeline userTimeline = new UserTimeline.Builder().screenName("twitterdev").build();
         adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                .setOnActionCallback(actionCallback)
                .build();

        moPubAdAdapter = new TwitterMoPubAdAdapter(getActivity(), adapter);

        final TwitterStaticNativeAdRenderer adRenderer = new TwitterStaticNativeAdRenderer();
        moPubAdAdapter.registerAdRenderer(adRenderer);
        moPubAdAdapter.loadAds(Constants.MOPUB_AD_UNIT_ID);

        setListAdapter(moPubAdAdapter);
        System.out.println("****" + moPubAdAdapter.areAllItemsEnabled());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,


                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tweetui_timeline, container, false);
    }

    @Override
    public void onDestroy() {
        // You must call this or the ad adapter may cause a memory leak
        moPubAdAdapter.destroy();
        super.onDestroy();
    }
}
