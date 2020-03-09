package com.ivik.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.content.Context.MODE_PRIVATE;

public class PasswordRecyclerView extends LinearLayout {
    private boolean locked;
    private PasswordManager passwordManager;

    public PasswordRecyclerView(Context context) {
        super(context);
        init();
    }

    public PasswordRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasswordRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PasswordRecyclerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public static String encryptString(String input) // Copied
    {
        if (input == null || input.equals("")) {
            return " ";
        }
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isPasswordCorrect(String hashedPassword) {
        String hash = encryptString(hashedPassword);
        String expectedPasswordHash = getContext().getSharedPreferences("data", MODE_PRIVATE).getString("passwordHash", "");
        System.out.println(expectedPasswordHash);
        System.out.println(hash);

        if (hash.equals(expectedPasswordHash)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void CreatePopup(String popupText, String popupButtonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(popupText)
                .setPositiveButton(popupButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    public void askForPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Password?");

        final String[] password = new String[1];

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String unencryptedPassword = input.getText().toString();
                password[0] = encryptString(unencryptedPassword);
                if (!(isPasswordCorrect(unencryptedPassword))) {
                    CreatePopup("Wrong!", "OK");
                    askForPassword();
                    return;
                }
                locked = false;
                passwordManager = new PasswordManager(input.getText().toString());
            }
        });

        builder.show();
    }

    public void init() {
        locked = true;
        askForPassword();
        while (locked != false) {}
        // TODO: load passwords and show them in a recycler layout
    }
}
