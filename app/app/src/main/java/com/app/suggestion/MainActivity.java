package com.app.suggestion;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.suggestion.adapter.ListAdapter;
import com.app.suggestion.model.CoinData;
import com.app.suggestion.model.Data;
import com.app.suggestion.util.Constants;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;


import net.gotev.speech.Speech;
import net.gotev.speech.TextToSpeechCallback;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends BaseActivity {


    private SwipeMenuListView listView;
    private ArrayList<Data> dataArrayList;
    private ListAdapter listAdapter;
    private Data data;

    private ImageButton button;
    private Button speak;
    private TextView text;
    private EditText textToSpeech;
    private LinearLayout linearLayout;

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void gotCommand(String result) {
        result = result.toLowerCase();
        if (result.contains("one") || result.contains("1") || result.contains("visiting")) {
            Intent intent = new Intent(MainActivity.this, VisitingCardReaderActivity.class);

            startActivity(intent);
            //  finish();
        } else if (result.contains("instructions")) {
            activityClicked(null);

        } else if (result.contains("wikipedia")) {
            Intent intent = new Intent(MainActivity.this, WikiPediaActivity.class);

            startActivity(intent);

        } else if (result.contains("coin")) {
            Intent intent = new Intent(MainActivity.this, CoinApiActivity.class);

            startActivity(intent);

        } else if (result.contains("twitter")) {
            Intent intent = new Intent(MainActivity.this, TwitterSampleActivity.class);

            startActivity(intent);

        } else if (result.contains("image")) {
            Intent intent = new Intent(MainActivity.this, ImageReaderActivity.class);

            startActivity(intent);

        } else {
            speak("Sorry try again");
            System.out.println("GOT RESULT" + result);

            listenAgain();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (SwipeMenuListView) findViewById(R.id.listview);
        dataArrayList = new ArrayList<>();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        button = (ImageButton) findViewById(R.id.button);
        //    button.setOnClickListener(view -> onButtonClick());

        speak = (Button) findViewById(R.id.speak);
        //    speak.setOnClickListener(view -> onButtonClick());

        text = (TextView) findViewById(R.id.text);
//        progress = (SpeechProgressView) findViewById(R.id.progress);
//
//        int[] colors = {
//                ContextCompat.getColor(this, android.R.color.black),
//                ContextCompat.getColor(this, android.R.color.darker_gray),
//                ContextCompat.getColor(this, android.R.color.black),
//                ContextCompat.getColor(this, android.R.color.holo_orange_dark),
//                ContextCompat.getColor(this, android.R.color.holo_red_dark)
//        };
//        progress.setColors(colors);

        dataArrayList.add(data = new Data("Visiting Card", "Scan your visiting card and save contacts directly to phone", Constants.VISITINGCARD));
        dataArrayList.add(data = new Data("Coin Data", "Get recent information about current currency details", Constants.COINDATA));
        dataArrayList.add(data = new Data("Twitter", "Read twitter data ", Constants.TWITTER));
        dataArrayList.add(data = new Data("WikiPedia", "Read wikipedia data ", Constants.WIKIPEDIA));
        dataArrayList.add(data = new Data("Image Reader", "Read Image data ", Constants.IMAGEREADER));


        listAdapter = new ListAdapter(this, dataArrayList);
        listView.setAdapter(listAdapter);

        listView.setMenuCreator(creator);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Data data = dataArrayList.get(position);
                Intent intent = null;
                if (data.getTag().equals(Constants.VISITINGCARD))
                    intent = new Intent(MainActivity.this, VisitingCardReaderActivity.class);
                else if (data.getTag().equals(Constants.VISITINGCARD))
                    intent = new Intent(MainActivity.this, VisitingCardReaderActivity.class);
                else if (data.getTag().equals(Constants.TWITTER))
                    intent = new Intent(MainActivity.this, TwitterSampleActivity.class);
                else if (data.getTag().equals(Constants.WIKIPEDIA))
                    intent = new Intent(MainActivity.this, WikiPediaActivity.class);
                else if (data.getTag().equals(Constants.COINDATA))
                    intent = new Intent(MainActivity.this, CoinApiActivity.class);
                else if (data.getTag().equals(Constants.IMAGEREADER))
                    intent = new Intent(MainActivity.this, ImageReaderActivity.class);

                if (intent != null)
                    startActivity(intent);
            }

        });


//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//
//            }
//        });

//        if (!isMyServiceRunning(SpeechService.class)) {
//            startService(new Intent(this, SpeechService.class));
//        }
        // onButtonClick();

    }


    SwipeMenuCreator creator = new SwipeMenuCreator() {

        @Override
        public void create(SwipeMenu menu) {


            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(
                    getApplicationContext());
            // set item background
            deleteItem.setBackground(new ColorDrawable(Color.parseColor("#F45557")));
            // set item width
            deleteItem.setWidth(150);

            deleteItem.setTitle("Delete");
            deleteItem.setTitleColor(Color.WHITE);
            deleteItem.setTitleSize(15);

            // add to menu
            menu.addMenuItem(deleteItem);
        }
    };

    public void activityClicked(View view) {

        String text = "one  " + dataArrayList.get(0).getName() + " two  " + dataArrayList.get(1).getName() + "three " + dataArrayList.get(2).getName() + "four " + dataArrayList.get(3).getName()+ "five " + dataArrayList.get(4).getName();

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onDone(String utteranceId) {
                System.out.println("FINISHED");
                // Log.d("MainActivity", "TTS finished");
            }

            @Override
            public void onError(String utteranceId) {
            }

            @Override
            public void onStart(String utteranceId) {
            }
        });
        speak(text);
        final Handler h = new Handler();


        Runnable r = new Runnable() {

            public void run() {

                if (!tts.isSpeaking()) {
                    onButtonClick();
                    h.removeCallbacks(this);

                } else {
                    h.postDelayed(this, 1000);
                }
            }
        };

        h.postDelayed(r, 1000);
        /*
        Speech.getInstance().say("one  " + dataArrayList.get(0).getName() + " two  " + dataArrayList.get(1).getName() + "three " + dataArrayList.get(2).getName() + "four " + dataArrayList.get(3).getName(), new TextToSpeechCallback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onCompleted() {

                onButtonClick();
            }

            @Override
            public void onError() {
            }
        });

        onButtonClick();
*/
    }


    @Override
    public void onResume() {
        super.onResume();
        TextView hint = findViewById(R.id.hint);

        speak(hint.getText().toString());
        listenAgain();
    }


}
