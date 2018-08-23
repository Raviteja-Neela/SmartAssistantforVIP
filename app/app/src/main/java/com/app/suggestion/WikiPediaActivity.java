package com.app.suggestion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WikiPediaActivity extends BaseActivity implements MaterialSearchBar.OnSearchActionListener {


    TextView title;
    TextView content;
    private List<String> lastSearches;

    private MaterialSearchBar searchBar;

    @Override
    public void gotCommand(String command) {
        if (command.toLowerCase().equals("close"))
            finish();
        else if (command.toLowerCase().equals("stop"))
            stop();
        else
            new OkHttpNetworking(command).execute(null, null, null);

    }

    public void stopSpeech(View view) {
        stop();
    }

    Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        openingmessage = "Here you go, wikipedia opened, let me know what you want to read";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_pedia);
        title = findViewById(R.id.title);
        stop = findViewById(R.id.stop);
        content = findViewById(R.id.content);
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Search WIKI");
        searchBar.setSpeechMode(true);
        searchBar.setTextHintColor(getColor(R.color.app_primary));
        searchBar.setOnSearchActionListener(this);


        listenAgain();
    }

    public void onClick(View view) {

    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {


        new OkHttpNetworking(text.toString()).execute(null, null, null);
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }


    public class OkHttpNetworking extends AsyncTask<String, Void, String> {

        private String textToProcess;
        String output = "";
        ProgressDialog progressDialog;


        public OkHttpNetworking(String textToProcess) {
            this.textToProcess = textToProcess;
        }


        public String callNamedERAPI(String extractedText) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, extractedText);
            Request request = new Request.Builder()
                    .url("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=" + textToProcess)
                    .post(body)
                    .addHeader("cache-control", "no-cache")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                JSONObject Jobject = new JSONObject(response.body().string());
                JSONObject jsonObject = Jobject.getJSONObject("query").getJSONObject("pages");
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String pageId = keys.next();

                    JSONObject pagesData = jsonObject.getJSONObject(pageId);
                    if (pagesData.has("extract")) {
                        output = pagesData.getString("extract");
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
                return "";
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
            return output;
        }

        @Override
        protected String doInBackground(String... strings) {
            return callNamedERAPI(textToProcess);
        }

        @Override
        protected void onPreExecute() {

            progressDialog = showLoader();
        }


        @Override
        protected void onPostExecute(String result) {

            progressDialog.dismiss();
            title.setText(textToProcess);
            content.setText(output);
            stop.setVisibility(View.VISIBLE);
            speak(output);
            listenAgain();
            final Handler h = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    //       droidSpeech.startDroidSpeechRecognition();
                }
            };

            h.postDelayed(r, 1000);


            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(WikiPediaActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
