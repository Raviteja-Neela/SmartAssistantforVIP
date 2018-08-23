package com.app.suggestion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.suggestion.adapter.CoinApiAdapter;
import com.app.suggestion.model.CoinData;
import com.mancj.materialsearchbar.MaterialSearchBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CoinApiActivity extends BaseActivity implements MaterialSearchBar.OnSearchActionListener {

    private List<String> lastSearches;
    Map<String, CoinData> coinDataMap = new HashMap<>();

    private MaterialSearchBar searchBar;
    private List<CoinData> coinDataList;
    ListView listView;
    CoinApiAdapter adapter;

    String currentShowing = "";

    @Override

    public void gotCommand(String command) {

        if (command.equals("close")) {
            finish();
        } else {
            command = command.toLowerCase();
            CoinData coinData = coinDataMap.get(command);
            if (coinData != null) {
                String data = coinData.getCurrency() + "PRICE : " + coinData.getPrice() + " Last Updated : " + coinData.getTime() + " Total Supply :" + coinData.getMax_supply() + " Circulating Supply : " + coinData.getCirculating_supply();
                //data =data+" 7 days ago "+ coinData.getPercent_change_7d()+" 24 hours ago "+coinData.getPercent_change_24h()+" 1 hour ago "+coinData.getPercent_change_1h();
                currentShowing = command;
                TextView bitcoindata = findViewById(R.id.bitcoindata);
                TextView showingCurrenty = findViewById(R.id.showingCurrenty);
                showingCurrenty.setText(coinData.getCurrency() + " current rate");
                bitcoindata.setText(coinData.getPrice());

                speak(data);


            } else if (command.contains("seven") || command.contains("week")) {
                if (!currentShowing.trim().equals("")) {
                    coinData = coinDataMap.get(currentShowing);
                    String data = coinData.getCurrency() + " 7 days ago " + coinData.getPercent_change_7d() + " percentage ";
                    if (coinData.getPercent_change_7d() < 0) {
                        data += "increased";
                    } else {
                        data += "decreased";
                    }
                    speak(data);

                }
            } else if (command.contains("seven") || command.contains("week")) {
                if (!currentShowing.trim().equals("")) {
                    coinData = coinDataMap.get(currentShowing);
                    String data = coinData.getCurrency() + " 7 days ago " + coinData.getPercent_change_7d() + " percentage ";
                    if (coinData.getPercent_change_7d() < 0) {
                        data += "increased";
                    } else {
                        data += "decreased";
                    }
                    speak(data);

                }
            } else if (command.contains("one") || command.contains("hour")) {
                if (!currentShowing.trim().equals("")) {
                    coinData = coinDataMap.get(currentShowing);
                    String data = coinData.getCurrency() + " one hour ago " + coinData.getPercent_change_1h() + " percentage ";
                    if (coinData.getPercent_change_1h() < 0) {
                        data += "increased";
                    } else {
                        data += "decreased";
                    }
                    speak(data);

                }
            } else if (command.contains("twenty") || command.contains("four")) {
                if (!currentShowing.trim().equals("")) {
                    coinData = coinDataMap.get(currentShowing);
                    String data = coinData.getCurrency() + " twenty four hours ago " + coinData.getPercent_change_24h() + " percentage ";
                    if (coinData.getPercent_change_24h() < 0) {
                        data += "increased";
                    } else {
                        data += "declined";
                    }
                    speak(data);

                }
            } else {
                speak("Can not find that currenty, try again");
            }
            listenAgain();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        openingmessage = "Here you go, Coin api opened, let me know what you want to read";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_api);
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Coin API");
        searchBar.setSpeechMode(true);
        searchBar.setTextHintColor(getColor(R.color.app_primary));
        searchBar.setOnSearchActionListener(this);
        coinDataList = new ArrayList<>();
        listView = findViewById(R.id.listView);
        adapter = new CoinApiAdapter(this, coinDataList);
        listView.setAdapter(adapter);
        new CoinApiActivity.OkHttpNetworking("").execute(null, null, null);

    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {


        new CoinApiActivity.OkHttpNetworking(text.toString()).execute(null, null, null);
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
                    .url("https://api.coinmarketcap.com/v2/ticker/")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                JSONObject Jobject = new JSONObject(response.body().string());
                JSONObject jsonObject = Jobject.getJSONObject("data");
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String pageId = keys.next();

                    JSONObject pagesData = jsonObject.getJSONObject(pageId);

                    CoinData coinData = new CoinData();
                    coinData.setCurrency(pagesData.getString("name"));
                    coinData.setPrice(pagesData.getJSONObject("quotes").getJSONObject("USD").getString("price"));

                    coinData.setPercent_change_1h(pagesData.getJSONObject("quotes").getJSONObject("USD").getDouble("percent_change_1h"));
                    coinData.setPercent_change_7d(pagesData.getJSONObject("quotes").getJSONObject("USD").getDouble("percent_change_7d"));
                    coinData.setPercent_change_24h(pagesData.getJSONObject("quotes").getJSONObject("USD").getDouble("percent_change_24h"));

                    if (pagesData.get("circulating_supply") != null)
                        coinData.setCirculating_supply("" + pagesData.getDouble("circulating_supply"));
                    if (!pagesData.isNull("max_supply"))
                        coinData.setMax_supply("" + pagesData.getDouble("max_supply"));

                    long unixSeconds = pagesData.getLong("last_updated");
                    Date date = new java.util.Date(unixSeconds * 1000L);
                    SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM-dd HH:mm");
                    String formattedDate = sdf.format(date);
                    System.out.println(formattedDate);

                    coinData.setTime(formattedDate);
                    System.out.println(coinData.getCurrency().toLowerCase());
                    coinDataMap.put(coinData.getCurrency().toLowerCase(), coinData);
                    coinDataList.add(coinData);
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
            adapter.notifyDataSetChanged();

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(CoinApiActivity.this);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            TextView bitcoindata = findViewById(R.id.bitcoindata);
            bitcoindata.setText(coinDataMap.get("bitcoin").getPrice() + " USD");
            listenAgain();

        }
    }
}
