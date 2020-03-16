package com.ivik.passwordmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.content.ClipData;
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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void addNewAccount() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a new account:");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText usernameInput = new EditText(this);
        final EditText passwordInput = new EditText(this);

        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        usernameInput.setInputType(InputType.TYPE_CLASS_TEXT);

        usernameInput.setHint("Username");
        passwordInput.setHint("Password");

        linearLayout.addView(usernameInput);
        linearLayout.addView(passwordInput);
        AlertDialog alertDialog = null;
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String unencryptedPassword = passwordInput.getText().toString();
                String unencryptedUsername = usernameInput.getText().toString();

                Account newAccount = new Account(unencryptedPassword, unencryptedUsername);
                passwordManager.addAccount(newAccount);
                accounts.add(newAccount);
                RecyclerView recyclerView = findViewById(R.id.passwords);
                recyclerView.getAdapter().notifyDataSetChanged();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.account_add_button) {
            addNewAccount();
        }
        return super.onOptionsItemSelected(item);
    }

    private void askForPassword() {
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
                    CreatePopup("Wrong!", "OK");
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

    public void CreatePopup(String popupText, String popupButtonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(popupText)
                .setPositiveButton(popupButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    private void loadViews() {
        accounts = passwordManager.getPasswords(userKey);
        Account testAccount = new Account("1234", "ivik");
        accounts.add(testAccount);

        System.out.println("wtf");

        RecyclerView recyclerView = findViewById(R.id.passwords);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        accounts.add(new Account("1234", "asldkasdkl"));
        accounts.add(new Account("123123", "asdasclasckja"));
        accounts.add(new Account("12351245123", "askjdsc mnAXL;KASLD"));
        accounts.add(new Account("1234", "asldkasdkl"));
        accounts.add(new Account("123123", "asdasclasckja"));
        accounts.add(new Account("12351245123", "askjdsc mnAXL;KASLD"));
        accounts.add(new Account("1234", "asldkasdkl"));
        accounts.add(new Account("123123", "asdasclasckja"));
        accounts.add(new Account("12351245123", "askjdsc mnAXL;KASLD"));
        accounts.add(new Account("1234", "asldkasdkl"));
        accounts.add(new Account("123123", "asdasclasckja"));
        accounts.add(new Account("12351245123", "askjdsc mnAXL;KASLD"));
        accounts.add(new Account("1234", "asldkasdkl"));
        accounts.add(new Account("123123", "asdasclasckja"));
        accounts.add(new Account("12351245123", "askjdsc mnAXL;KASLD"));

        PasswordRecyclerViewAdapter adapter = new PasswordRecyclerViewAdapter(accounts);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void askForNewPassword() {
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
}
