package com.ivik.passwordmanager;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class AddNewAccountDialog {
    public interface OnSuccessListener {
        void OnAccountAdded(Account account);
    }

    public static void show(@NonNull Context context, final OnSuccessListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LinearLayout linearLayout = new LinearLayout(context);
        LayoutInflater.from(context).inflate(R.layout.add_account_dialog, linearLayout);

        final EditText usernameInput = linearLayout.findViewById(R.id.username_edittext);
        final EditText passwordInput = linearLayout.findViewById(R.id.password_edittext);
        final EditText webpageInput = linearLayout.findViewById(R.id.webpage_edittext);
        final EditText appInput = linearLayout.findViewById(R.id.application_edittext);

        builder.setView(linearLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String unencryptedPassword = passwordInput.getText().toString();
                String unencryptedUsername = usernameInput.getText().toString();
                String unencryptedWebpage = webpageInput.getText().toString();
                String unencryptedApp = appInput.getText().toString();

                Account newAccount = new Account(unencryptedPassword, unencryptedUsername, unencryptedWebpage, unencryptedApp);
                listener.OnAccountAdded(newAccount);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.show();
    }
}
