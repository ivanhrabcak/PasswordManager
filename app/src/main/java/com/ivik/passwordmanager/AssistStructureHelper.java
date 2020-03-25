package com.ivik.passwordmanager;

import android.app.assist.AssistStructure;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class AssistStructureHelper {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isPassword(AssistStructure.ViewNode node) { // :/
        return (!(node.getIdEntry().toLowerCase().contains("search")) &&
                !(node.getHint().toLowerCase().contains("search")) &&
                (node.getHint().toLowerCase().contains("password") &&
                        !(node.getHint().toLowerCase()).contains("confirm")));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isUsername(AssistStructure.ViewNode node) {
        return (!(node.getIdEntry().toLowerCase().contains("search")) &&
                !(node.getHint().toLowerCase().contains("search")) &&
                (node.getHint().toLowerCase().contains("username") &&
                node.getHint().contains("user")) &&
                !(node.getHint()).contains("password"));
    }
}
