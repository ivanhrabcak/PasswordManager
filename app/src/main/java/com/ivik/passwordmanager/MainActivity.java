package com.ivik.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;

    public boolean isFirstTimeLaunch() {
        boolean isFirstTimeLaunch = getSharedPreferences("data", MODE_PRIVATE).getBoolean("isFirstTimeLaunch", true);
        return isFirstTimeLaunch;
    }

    public void setFirstTimeLaunch(boolean value) {
        editor.putBoolean("isFirstTimeLaunch", value);
        editor.apply();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //CreatePopup("Test", "OK");
        this.editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        boolean value = getSharedPreferences("data", MODE_PRIVATE).getBoolean("isFirstTimeLaunch", true);
        if (value == true) {
            CreatePopup("true", "OK");
        }
        else {
            CreatePopup("false", "OK");
        }
        setFirstTimeLaunch(false);
    }

    @Override
    protected void onClose()
}
