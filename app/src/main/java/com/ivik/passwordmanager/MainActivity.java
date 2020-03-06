package com.ivik.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;

    public boolean isFirstTimeLaunch() {
        boolean isFirstTimeLaunch = getSharedPreferences("data", MODE_PRIVATE).getBoolean("isFirstTimeLaunch", true);
        return isFirstTimeLaunch;
    }

    public void setFirstTimeLaunch(boolean value) {
        editor.putBoolean("isFirstTimeLaunch", value);
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

    public String askForPassword() throws InterruptedException {
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
                password[0] = encryptString(unencryptedPassword);
            }
        });
        
        builder.show();

        return password[0];
    }

    public boolean isPasswordCorrect(String hashedPassword) {
        String hash = encryptString(hashedPassword);
        String expectedPasswordHash = getSharedPreferences("data", MODE_PRIVATE).getString("passwordHash", "");

        if (hash == expectedPasswordHash) {
            return true;
        }
        else {
            return false;
        }
    }

    public static String encryptString(String input) // Copied
    {
        if (input == null || input.equals("")) {
            return "";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (false) {
            editor = getSharedPreferences("data", MODE_PRIVATE).edit();
            boolean value = getSharedPreferences("data", MODE_PRIVATE).getBoolean("isFirstTimeLaunch", true);

            if (value == true) {
                CreatePopup("true", "OK");
            }
            else {
                CreatePopup("false", "OK");
            }
            setFirstTimeLaunch(false);
        }
        else {
            while (true) {
                String encryptedPassword = null;
                try {
                    encryptedPassword = askForPassword();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isPasswordCorrect(encryptedPassword)) {
                    CreatePopup("Good password!", "OK");
                    break;
                } else {
                    CreatePopup("Wrong password!", "OK");
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.apply();
    }
}
