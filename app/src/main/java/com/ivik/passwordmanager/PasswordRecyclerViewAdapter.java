package com.ivik.passwordmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordRecyclerViewAdapter  extends RecyclerView.Adapter<PasswordRecyclerViewAdapter.ViewHolder> {
    private List<Account> accounts;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public PasswordView passwordView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            System.out.println("viewholder");
            passwordView = (PasswordView) itemView;
            Button deleteButton = passwordView.findViewById(R.id.delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(getAdapterPosition());
                    passwordView.onRemove();
                }
            });
        }
    }

    public PasswordRecyclerViewAdapter(@NonNull List<Account> accounts) {
        this.accounts = accounts;
    }

    public void removeAt(int position) {
        accounts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("lmao");
        PasswordView pv = (PasswordView) LayoutInflater.from(parent.getContext()).inflate(R.layout.password_view, parent, false);

        PasswordRecyclerViewAdapter.ViewHolder viewHolder = new ViewHolder(pv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        System.out.println("bound view holder");
        holder.passwordView.bindAccount(accounts.get(position), position);
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }
}

