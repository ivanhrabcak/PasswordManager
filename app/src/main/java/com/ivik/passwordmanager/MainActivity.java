package com.ivik.passwordmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tozny.crypto.android.AesCbcWithIntegrity;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private PasswordManager passwordManager;
    private String userKey;
    private List<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (SharedPreferencesHelper.isFirstTimeLaunch(this)) {
            askForNewPassword();
        }
        else {
            askForPassword();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("onPause");
        System.out.println(passwordManager.getJsonData());
        SharedPreferencesHelper.putString(this, "jsonData", passwordManager.getJsonData());
    }

    public static void CreatePopup(Context context, String popupText, String popupButtonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(popupText)
                .setPositiveButton(popupButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    public void addNewAccount() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LinearLayout linearLayout = new LinearLayout(this);
        LayoutInflater.from(this).inflate(R.layout.add_account_dialog, linearLayout);

        final EditText usernameInput = linearLayout.findViewById(R.id.username_edittext);
        final EditText passwordInput = linearLayout.findViewById(R.id.password_edittext);

        builder.setView(linearLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String unencryptedPassword = passwordInput.getText().toString();
                String unencryptedUsername = usernameInput.getText().toString();

                Account newAccount = new Account(unencryptedPassword, unencryptedUsername);
                passwordManager.addAccount(newAccount);
                accounts.add(newAccount);
                RecyclerView recyclerView = linearLayout.findViewById(R.id.passwords);
                recyclerView.getAdapter().notifyDataSetChanged();
                System.out.println(passwordManager.getJSONObject());

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.show();
    }

    public void askForPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password?");
        final String[] password = new String[1];
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String unencryptedPassword = input.getText().toString();

                password[0] = passwordManager.hashString(unencryptedPassword);

                if (SharedPreferencesHelper.getString(getApplicationContext(), "passwordHash").equals(unencryptedPassword)) {
                    CreatePopup(getApplicationContext(), "Wrong!", "OK");
                    askForPassword();
                    return;
                }

                userKey = input.getText().toString();
                passwordManager = new PasswordManager(userKey);
                loadViews();
            }
        });
        builder.show();
    }

    public void askForNewPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New password?");

        final String[] password = new String[1];

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String unencryptedPassword = input.getText().toString();
                password[0] = PasswordManager.hashString(unencryptedPassword);
                SharedPreferencesHelper.putString(getApplicationContext(), "passwordHash", password[0]);
                passwordManager = new PasswordManager(userKey);
                loadViews();
            }
        });

        builder.show();

    }

    public void loadViews() {
        if (SharedPreferencesHelper.getSharedPreferences(this).getString("jsonData", null) != null) {
            try {
                passwordManager.parseAccountsFromJsonData(SharedPreferencesHelper.getString(this, "jsonData"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            System.out.println("Failed to load saved passwords.");
        }
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAccount();
            }
        });
        accounts = passwordManager.getPasswords(userKey);
        System.out.println(accounts);

        RecyclerView recyclerView = findViewById(R.id.passwords);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        PasswordRecyclerViewAdapter adapter = new PasswordRecyclerViewAdapter(accounts, passwordManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


}
