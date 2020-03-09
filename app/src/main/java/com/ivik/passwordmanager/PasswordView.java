package com.ivik.passwordmanager;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.w3c.dom.Text;

import java.util.ArrayList;

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

    private void init() {
        TextView account = findViewById(R.id.account);
        TextView password = findViewById(R.id.password);

        account.setText(this.account.getUsername());
        password.setText(this.account.getPassword());
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
