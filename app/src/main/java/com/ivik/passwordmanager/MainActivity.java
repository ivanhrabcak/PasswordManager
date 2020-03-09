package com.ivik.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

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



//    public void setNewPassword() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("New password?");
//
//        final String[] password = new String[1];
//
//        final EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        builder.setView(input);
//
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String unencryptedPassword = input.getText().toString();
//                password[0] = encryptString(unencryptedPassword);
//                changePassword(password[0]);
//            }
//        });
//
//        builder.show();
//    }





    private void changePassword(String encryptedPassword) {
        editor.putString("passwordHash", encryptedPassword);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LinearLayout passwordRecyclerView = new PasswordRecyclerView(this);
//        getLayoutInflater().inflate(R.layout.password_recycler_view, passwordRecyclerView);
    }

    @Override
    protected void onStop() {
        super.onStop();

        editor.apply();
    }
}
