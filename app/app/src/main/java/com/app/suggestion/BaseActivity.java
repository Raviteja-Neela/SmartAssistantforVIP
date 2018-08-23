package com.app.suggestion;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;
import com.vikramezhil.droidspeech.OnDSPermissionsListener;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public abstract class BaseActivity extends AppCompatActivity implements OnDSListener, OnDSPermissionsListener {
    private ProgressDialog progressBar;
    public DroidSpeech droidSpeech;


    protected String openingmessage = "Here you go, it's opened";

    public abstract void gotCommand(String command);

    public TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        System.out.println("NEW INSTANCE CRATED");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        droidSpeech = new DroidSpeech(this, getFragmentManager());
        droidSpeech.setOnDroidSpeechListener(this);
        droidSpeech.setShowRecognitionProgressView(true);
        //  droidSpeech.setOneStepResultVerify(true);
        droidSpeech.setRecognitionProgressMsgColor(Color.WHITE);
        droidSpeech.setOneStepVerifyConfirmTextColor(Color.WHITE);
        droidSpeech.setOneStepVerifyRetryTextColor(Color.WHITE);
        droidSpeech.setListeningMsg("Listening");

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                    speak(openingmessage);
                }

            }

        });

        final Context context = this;
        final GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                stop();
                listenAgain();
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                stop();
                listenAgain();

            }
        };

        final GestureDetector detector = new GestureDetector(listener);

        detector.setOnDoubleTapListener(listener);
        detector.setIsLongpressEnabled(true);
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
    }

    public void stop() {
        System.out.println("Stopping..");
        tts.stop();
    }

    public void speak(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void onButtonClick() {

        stop();
        System.out.println("stop and close");
//        try {
//
//            droidSpeech.closeDroidSpeechOperations();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //droidSpeech.

        try {
            droidSpeech.startDroidSpeechRecognition();

        } catch (Exception e) {
            droidSpeech.closeDroidSpeechOperations();
        }
    }

    public void listenAgain() {
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
    }

    @Override
    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages) {
        System.out.println("Current speech language = " + currentSpeechLanguage);
        System.out.println("Supported speech languages = " + supportedSpeechLanguages.toString());

        droidSpeech.setPreferredLanguage("en-US");


    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue) {
        // System.out.println(TAG, "Rms change value = " + rmsChangedValue);
    }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult) {
        System.out.println("Live speech result = " + liveSpeechResult);
        //   gotCommand(liveSpeechResult);
        //   droidSpeech.closeDroidSpeechOperations();
        //     droidSpeech.startDroidSpeechRecognition();
    }

    @Override
    public void onDroidSpeechFinalResult(String finalSpeechResult) {
        // Setting the final speech result
        //  this.finalSpeechResult.setText(finalSpeechResult);

        System.out.println("FINAL RESULT" + finalSpeechResult);
        gotCommand(finalSpeechResult);
        droidSpeech.closeDroidSpeechOperations();
        if (droidSpeech.getContinuousSpeechRecognition()) {
            int[] colorPallets1 = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA};
            int[] colorPallets2 = new int[]{Color.YELLOW, Color.RED, Color.CYAN, Color.BLUE, Color.GREEN};

            // Setting random color pallets to the recognition progress view
            droidSpeech.setRecognitionProgressViewColors(new Random().nextInt(2) == 0 ? colorPallets1 : colorPallets2);
        } else {

        }
        //droidSpeech.startDroidSpeechRecognition();

    }

    @Override
    public void onDroidSpeechClosedByUser() {

    }

    @Override
    public void onDroidSpeechError(String errorMsg) {
        // Speech error
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();

        droidSpeech.closeDroidSpeechOperations();

    }

    // MARK: DroidSpeechPermissionsListener Method

    @Override
    public void onDroidSpeechAudioPermissionStatus(boolean audioPermissionGiven, String errorMsgIfAny) {
        System.out.println("AUDIO PERMISSION STATTS");
        if (audioPermissionGiven) {
            // droidSpeech.startDroidSpeechRecognition();
            onButtonClick();
        } else {
            if (errorMsgIfAny != null) {
                // Permissions error
                Toast.makeText(this, errorMsgIfAny, Toast.LENGTH_LONG).show();
            }

            //    droidSpeech.closeDroidSpeechOperations();

        }
    }

    public ProgressDialog showLoader() {
        progressBar = ProgressDialog.show(this, "",
                "Please wait", true);
        return progressBar;
    }

    public void cancelLoader() {

        progressBar.dismiss();
    }

    public void onDestroy() {

        super.onDestroy();
        droidSpeech.closeDroidSpeechOperations();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.speech) {
            onButtonClick();
            return true;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}