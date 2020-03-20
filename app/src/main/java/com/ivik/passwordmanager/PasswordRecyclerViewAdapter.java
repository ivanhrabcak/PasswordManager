package com.ivik.passwordmanager;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PasswordRecyclerViewAdapter extends RecyclerView.Adapter<PasswordRecyclerViewAdapter.ViewHolder> implements Filterable {
    private List<Account> accounts;
    private PasswordManager passwordManager;
    private List<Account> unfilteredAccounts;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public PasswordView passwordView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            System.out.println("viewholder");
            passwordView = (PasswordView) itemView;
        }
    }

    public PasswordRecyclerViewAdapter(@NonNull List<Account> accounts, PasswordManager passwordManager) {
        this.accounts = accounts;
        this.unfilteredAccounts = accounts;
        this.passwordManager = passwordManager;
    }

    public void removeAt(int position) {
        passwordManager.removeAccount(accounts.get(position));
        accounts.remove(accounts.get(position));
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PasswordView pv = (PasswordView) LayoutInflater.from(parent.getContext()).inflate(R.layout.password_view, parent, false);
        PasswordRecyclerViewAdapter.ViewHolder viewHolder = new ViewHolder(pv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.passwordView.bindAccount(accounts.get(position));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filterResults.values = unfilteredAccounts;
                }
                else {
                    List<Account> filteredAccounts = new ArrayList<>();
                    for (Account account : accounts) {
                        if (account.getUsername().contains(charString) || account.getPassword().contains(charString)) {
                            filteredAccounts.add(account);
                        }
                    }
                    filterResults.values = filteredAccounts;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                accounts = (ArrayList<Account>) results.values;

                notifyDataSetChanged();
            }
        };
    }
}

