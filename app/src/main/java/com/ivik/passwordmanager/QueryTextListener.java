package com.ivik.passwordmanager;

import android.widget.Adapter;
import android.widget.SearchView;

public class QueryTextListener implements SearchView.OnQueryTextListener {
    private PasswordRecyclerViewAdapter adapter;

    public QueryTextListener(PasswordRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }
}
