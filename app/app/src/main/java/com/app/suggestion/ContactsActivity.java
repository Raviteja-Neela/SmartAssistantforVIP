package com.app.suggestion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ContactsActivity extends BaseActivity {

    private EditText phoneText;
    private EditText nameText;
    private EditText emailText;

    private static final int CONTACTS_PERMISSION = 5;

    private Button contactsButton;

    private String name;
    private String email;
    private String phone;

    @Override
    public void gotCommand(String result) {
        result = result.toLowerCase();
        if (result.contains("close")) {
            finish();
        }
        if (result.contains("no")) {
            finish();
        } else if (result.contains("yes")) {
            Button contactsButton = (Button) findViewById(R.id.contactBtn);
            contactsButton.performClick();
        } else if (result.contains("phone")) {

            String phoneNum = result.replaceAll("edit", "").replaceAll("phone", "").trim();
            EditText editText = findViewById(R.id.phoneText);
            editText.setText(phoneNum);
            speak("replaced Say yes to save, edit to edit, no to  ignore");


        } else if (result.contains("email")) {
            String email1 = result.replaceAll("edit", "").replaceAll("email", "").trim();
            EditText editText = findViewById(R.id.emailText);
            editText.setText(email1);
            speak("replaced Say yes to save, edit to edit, no to  ignore");


        } else if (result.contains("name")) {

            String name1 = result.replaceAll("edit", "").replaceAll("name", "").trim();
            EditText editText = findViewById(R.id.nameText);
            editText.setText(name1);
            speak("replaced Say yes to save, edit to edit, no to  ignore");

        } else {
            listenAgain();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        phoneText = (EditText) findViewById(R.id.phoneText);
        nameText = (EditText) findViewById(R.id.nameText);
        emailText = (EditText) findViewById(R.id.emailText);
        contactsButton = (Button) findViewById(R.id.contactBtn);

        try {
            phone = getIntent().getStringExtra("phoneNumber");
            email = getIntent().getStringExtra("email");
            name = getIntent().getStringExtra("name");

        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(ContactsActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }

        phoneText.setText(phone);
        nameText.setText(name);
        emailText.setText(email);
        speak("Phone Number : " + phone + " Name " + name + " Email : " + email + " Say yes to save, edit to edit, no to  ignore");

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startContactsActivityIntent();
            }
        });
        listenAgain();
    }


    /**
     * Starts the contacts intent and requests permission to write to contacts if permission doesn't exist
     */
    public void startContactsActivityIntent() {
        String[] permissions = {"android.permission.WRITE_CONTACTS"};
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, CONTACTS_PERMISSION);
        } else {
            if (intent.resolveActivity(getPackageManager()) != null) {
                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                startActivity(intent);
            }
        }
    }
}
