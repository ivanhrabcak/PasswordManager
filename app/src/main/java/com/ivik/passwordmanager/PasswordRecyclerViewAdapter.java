package com.ivik.passwordmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordRecyclerViewAdapter  extends RecyclerView.Adapter<PasswordRecyclerViewAdapter.ViewHolder> {
    private List<Account> accounts;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public PasswordView passwordView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            passwordView = (PasswordView) itemView;
        }
    }

    public PasswordRecyclerViewAdapter(@NonNull List<Account> accounts) {
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("lmao");
        LayoutInflater.from(parent.getContext()).inflate(R.layout.password_view, parent);
        PasswordRecyclerViewAdapter.ViewHolder viewHolder = new ViewHolder(parent);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println("bound view holder");
        holder.passwordView.bindAccount(accounts.get(position));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }
}
