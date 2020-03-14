package com.ivik.passwordmanager;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class PasswordView extends LinearLayout {
    private Account account;

    public PasswordView(Context context) {
        super(context);
        init();
    }

    public PasswordView(Context context, Account account) {
        super(context);
        this.account = account;
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

    public void bindAccount(Account account, int order) {
        TextView accountView = findViewById(R.id.account);
        TextView password = findViewById(R.id.password);

        accountView.setText(account.getUsername());
        password.setText(account.getPassword());
    }

    public void onRemove() {
        // todo: delete from shared preferences
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
