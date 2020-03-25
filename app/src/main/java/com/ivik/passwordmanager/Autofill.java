package com.ivik.passwordmanager;

import android.app.Service;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.service.autofill.AutofillService;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveRequest;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Autofill extends AutofillService {
    private PasswordManager passwordManager;
    private String userKey;

    public Autofill(PasswordManager passwordManager, String userKey) {
        this.passwordManager = passwordManager;
        this.userKey = userKey;
    }

    @Override
    public void onFillRequest(@NonNull FillRequest request, @NonNull CancellationSignal cancellationSignal, @NonNull FillCallback callback) {
        List<FillContext> contexts = request.getFillContexts();
        AssistStructure assistStructure = contexts.get(contexts.size() - 1).getStructure();
        ParsedStructure parsedStructure = ParsedStructure.parseStructure(assistStructure, passwordManager, userKey);
        if (parsedStructure == null) {
            callback.onSuccess(null);
        }
        else {
            Account account = parsedStructure.getAccount();

            RemoteViews usernamePresentation = new RemoteViews(getPackageName(), android.R.layout.simple_list_item_1);
            usernamePresentation.setTextViewText(android.R.id.text1, account.getUsername());
            RemoteViews passwordPresentation = new RemoteViews(getPackageName(), android.R.layout.simple_list_item_1);
            passwordPresentation.setTextViewText(android.R.id.text1, account.getPassword());


            FillResponse fillResponse = new FillResponse.Builder()
                    .addDataset(new Dataset.Builder()
                            .setValue(parsedStructure.getUsernameWindowNode().getRootViewNode().getAutofillId(),
                                    AutofillValue.forText(account.getUsername()), usernamePresentation)
                            .setValue(parsedStructure.getPasswordWindowNode().getRootViewNode().getAutofillId(),
                                    AutofillValue.forText(account.getPassword()), passwordPresentation)
                            .build())
                    .build();

            callback.onSuccess(fillResponse);
        }
    }

    @Override
    public void onSaveRequest(@NonNull SaveRequest request, @NonNull SaveCallback callback) {

    }
}
