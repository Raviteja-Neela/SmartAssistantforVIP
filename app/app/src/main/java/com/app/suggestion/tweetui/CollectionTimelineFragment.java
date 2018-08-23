

package com.app.suggestion.tweetui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.suggestion.R;
import com.app.suggestion.tweetcomposer.BaseFragment;
import com.twitter.sdk.android.tweetui.CollectionTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

/**
 * CollectionTimelineFragment demonstrates a TimelineListAdapter with a CollectionTimeline.
 */
public class CollectionTimelineFragment extends BaseFragment {

    public static CollectionTimelineFragment newInstance() {
        return new CollectionTimelineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CollectionTimeline collectionTimeline = new CollectionTimeline.Builder()
                .id(659110687482839040L).build();
        adapter = new TweetTimelineListAdapter(getActivity(),
                collectionTimeline);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tweetui_timeline, container, false);
    }
}


