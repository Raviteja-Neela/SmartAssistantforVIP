

package com.app.suggestion.tweetui;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.app.suggestion.BaseActivity;
import com.app.suggestion.R;
import com.app.suggestion.tweetcomposer.BaseFragment;
import com.app.suggestion.util.Constants;
import com.twitter.sdk.android.core.models.Tweet;

/**
 * TimelinesActivity pages between different timeline Fragments.
 */
public class TimelinesActivity extends BaseActivity {
    private static final int PAGE_SEARCH = 0;
    private static final int PAGE_USER = 0;
    private static final int PAGE_USER_RECYCLER_VIEW = 1;
    private static final int PAGE_COLLECTION = 2;
    private static final int PAGE_LIST = 3;
    static BaseFragment listFragment = null;

    @Override
    public void gotCommand(String result) {
        result = result.toLowerCase();
        final ViewPager viewPager = findViewById(R.id.pager);

        if (result.contains(Constants.INSTRUCTIONS)) {
            speak(" one developer , two news, three popular,four latest");
            listenAgain();
        } else if (result.contains("developer")) {


            viewPager.setCurrentItem(PAGE_USER);
        } else if (result.contains("news")) {

            viewPager.setCurrentItem(PAGE_USER_RECYCLER_VIEW);
        } else if (result.contains("popular")) {

            viewPager.setCurrentItem(PAGE_COLLECTION);
        } else if (result.contains("latest")) {

            viewPager.setCurrentItem(PAGE_LIST);
        } else {
            speak("not found try again");
            listenAgain();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        final FragmentManager fm = getSupportFragmentManager();
        final FragmentPagerAdapter pagerAdapter = new TimelinePagerAdapter(fm, getResources(), this);
        final ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        listenAgain();

    }

    public void stopSpeech(View view) {
        stop();
    }


    public static class TimelinePagerAdapter extends FragmentPagerAdapter {
        // titles for timeline fragments, in order
        private static final int[] PAGE_TITLE_RES_IDS = {

                R.string.user_timeline_title,
                R.string.user_recycler_view_timeline_title,
                R.string.collection_timeline_title,
                R.string.list_timeline_title
        };
        private Resources resources;
        private BaseActivity baseActivity;

        public TimelinePagerAdapter(FragmentManager fm, Resources resources, BaseActivity baseActivity) {
            super(fm);
            this.resources = resources;
            this.baseActivity = baseActivity;
        }


        @Override
        public Fragment getItem(int position) {
            System.out.println("POSITION" + position);
            switch (position) {

                case PAGE_USER:
                    listFragment = UserTimelineFragment.newInstance();
                    break;
                case PAGE_USER_RECYCLER_VIEW:
                    return UserTimelineRecyclerViewFragment.newInstance();
                case PAGE_COLLECTION:
                    listFragment = CollectionTimelineFragment.newInstance();
                case PAGE_LIST:
                    listFragment = ListTimelineFragment.newInstance();

                default:
                    throw new IllegalStateException("Unexpected Fragment page item requested.");
            }


            System.out.println("****" + listFragment.adapter);
            // if (listFragment.adapter != null) {
            final Handler h = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    if (listFragment.adapter != null && listFragment.adapter.getCount() > 0) {
                        baseActivity.droidSpeech.closeDroidSpeechOperations();
                        System.out.println("****" + listFragment.adapter.getCount());
                        String textToRead = "";
                        for (int i = 0; i < listFragment.adapter.getCount(); i++) {
                            Tweet tweet = listFragment.adapter.getItem(i);
                            textToRead = textToRead + tweet.text;
                        }
                        baseActivity.speak(textToRead);

                        h.removeCallbacks(this);
                    } else {
                        h.postDelayed(this, 1000);
                    }
                }
            };
            h.postDelayed(r, 1000);
            //}
            return listFragment;
        }

        @Override
        public int getCount() {
            return PAGE_TITLE_RES_IDS.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
//                case PAGE_SEARCH:
//                    return resources.getString(PAGE_TITLE_RES_IDS[PAGE_SEARCH]);
                case PAGE_USER:
                    return resources.getString(PAGE_TITLE_RES_IDS[PAGE_USER]);
                case PAGE_USER_RECYCLER_VIEW:
                    return resources.getString(PAGE_TITLE_RES_IDS[PAGE_USER_RECYCLER_VIEW]);
                case PAGE_COLLECTION:
                    return resources.getString(PAGE_TITLE_RES_IDS[PAGE_COLLECTION]);
                case PAGE_LIST:
                    return resources.getString(PAGE_TITLE_RES_IDS[PAGE_LIST]);
                default:
                    throw new IllegalStateException("Unexpected Fragment page title requested.");
            }
        }
    }
}
