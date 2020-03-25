package com.ivik.passwordmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;

public class PasswordView extends LinearLayout {
    private Account account;

    public PasswordView(Context context) {
        super(context);
        init();
    }

    public PasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void bindAccount(Account account) {
        final TextView accountView = findViewById(R.id.account);
        final TextView passwordView = findViewById(R.id.password);
        final TextView webpageView = findViewById(R.id.webpage);

        accountView.setText(account.getUsername());
        passwordView.setText(account.getPassword());
        webpageView.setText(account.getWebpage());


        accountView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("username", accountView.getText());
                clipboardManager.setPrimaryClip(clip);
                Snackbar snackbar = Snackbar.make(v, "Username has been copied to clipboard", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        passwordView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("password", passwordView.getText());
                clipboardManager.setPrimaryClip(clip);
                Snackbar snackbar = Snackbar.make(v, "Password has been copied to clipboard", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });
    }

    private void init() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
