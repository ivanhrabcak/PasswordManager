package com.ivik.passwordmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
    private SearchView searchView;

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

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            System.out.println(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_button);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            this.searchView = searchView;
            searchView.setIconifiedByDefault(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPause() {
        super.onPause();
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
                RecyclerView recyclerView = findViewById(R.id.passwords);
                recyclerView.getAdapter().notifyDataSetChanged();

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

                if (!(SharedPreferencesHelper.getString(getApplicationContext(), "passwordHash").equals(password[0]))) {
                    //CreatePopup(getApplicationContext(), "Wrong!", "OK");
                    askForPassword();
                    return;
                }

                userKey = input.getText().toString();
                passwordManager = new PasswordManager(userKey, getApplicationContext());
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
                passwordManager = new PasswordManager(userKey, getApplicationContext());
                loadViews();
            }
        });

        builder.show();

    }

    public void loadViews() {
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAccount();
            }
        });
        accounts = passwordManager.getPasswords(userKey);

        RecyclerView recyclerView = findViewById(R.id.passwords);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        PasswordRecyclerViewAdapter adapter = new PasswordRecyclerViewAdapter(accounts, passwordManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        searchView.setOnQueryTextListener(new QueryTextListener(adapter));


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


}
