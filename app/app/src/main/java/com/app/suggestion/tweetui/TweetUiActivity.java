

package com.app.suggestion.tweetui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.app.suggestion.BaseActivity;
import com.app.suggestion.R;

/**
 * TweetUiActivity is a BaseActivity which creates a single fragment.
 */
public abstract class TweetUiActivity extends BaseActivity {

    abstract int getLayout();

    // Builder to create the Fragment added to the Activity's container
    abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, createFragment())
                    .commit();
        }
        setContentView(getLayout());
    }
}
