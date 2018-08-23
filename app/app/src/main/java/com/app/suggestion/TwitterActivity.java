package com.app.suggestion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TwitterActivity extends BaseActivity {

    @Override
    public void gotCommand(String command) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
    }
}
