package com.ivik.passwordmanager;

import android.app.assist.AssistStructure;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;

public class ParsedStructure {
    private AssistStructure.WindowNode usernameWindowNode;
    private AssistStructure.WindowNode passwordWindowNode;
    private Account account;
    public static List<String> browsers = Arrays.asList(new String[] { // borrowed
                "org.mozilla.firefox",
                "org.mozilla.firefox_beta",
                "com.microsoft.emmx",
                "com.android.chrome",
                "com.chrome.beta",
                "com.android.browser",
                "com.brave.browser",
                "com.opera.browser",
                "com.opera.browser.beta",
                "com.opera.mini.native",
                "com.chrome.dev",
                "com.chrome.canary",
                "com.google.android.apps.chrome",
                "com.google.android.apps.chrome_dev",
                "com.yandex.browser",
                "com.sec.android.app.sbrowser",
                "com.sec.android.app.sbrowser.beta",
                "org.codeaurora.swe.browser",
                "com.amazon.cloud9",
                "mark.via.gp",
                "org.bromite.bromite",
                "org.chromium.chrome",
                "com.kiwibrowser.browser",
                "com.ecosia.android",
                "com.opera.mini.native.beta",
                "org.mozilla.fennec_aurora",
                "org.mozilla.fennec_fdroid",
                "com.qwant.liberty",
                "com.opera.touch",
                "org.mozilla.fenix",
                "org.mozilla.fenix.nightly",
                "org.mozilla.reference.browser",
                "org.mozilla.rocket",
                "org.torproject.torbrowser",
                "com.vivaldi.browser"});

    public ParsedStructure(AssistStructure.WindowNode usernameWindowNode, AssistStructure.WindowNode passwordWindowNode) {
        this.usernameWindowNode = usernameWindowNode;
        this.passwordWindowNode = passwordWindowNode;
    }

    public ParsedStructure(Account account) {
        usernameWindowNode = null;
        passwordWindowNode = null;
        this.account = account;
    }

    public ParsedStructure() {
        usernameWindowNode = null;
        passwordWindowNode = null;
        account = null;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static boolean isBrowser(String packageName) {
        return browsers.contains(packageName);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getWebPage(AssistStructure assistStructure) {
        for (int i = 0; i < assistStructure.getWindowNodeCount(); i ++) {
            AssistStructure.WindowNode windowNode = assistStructure.getWindowNodeAt(i);
            AssistStructure.ViewNode viewNode = windowNode.getRootViewNode();
            String hint = viewNode.getHint();
            if (hint.toLowerCase().contains("search") || hint.toLowerCase().contains("web")) {
                return viewNode.getText().toString();
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static ParsedStructure parseStructure(AssistStructure assistStructure, PasswordManager passwordManager, String userKey) {
        String packageName = assistStructure.getActivityComponent().getPackageName();
        boolean isBrowser = isBrowser(packageName);
        Account account;
        if (isBrowser) {
            String webpage = getWebPage(assistStructure);
            account = passwordManager.getDataForWebpage(webpage, userKey);
        }
        else {
            account = new Account(1, "1234", "john", "page", "com.google.android.gms");//passwordManager.getDataForApplication(packageName, userKey);
        }
        ParsedStructure parsedStructure = new ParsedStructure(account);
        for (int i = 0; i < assistStructure.getWindowNodeCount(); i++) {
            AssistStructure.WindowNode windowNode = assistStructure.getWindowNodeAt(i);
            AssistStructure.ViewNode viewNode = windowNode.getRootViewNode();
            if (AssistStructureHelper.isPassword(viewNode)) {
                parsedStructure.setPasswordWindowNode(windowNode);
            }
            else if (AssistStructureHelper.isUsername(viewNode)) {
                parsedStructure.setUsernameWindowNode(windowNode);
            }
        }
        if (parsedStructure.getUsernameWindowNode() == null || parsedStructure.getPasswordWindowNode() == null
        || parsedStructure.getAccount() == null) {
            return null;
        }
        return parsedStructure;
    }

    public AssistStructure.WindowNode getUsernameWindowNode() {
        return usernameWindowNode;
    }

    public void setUsernameWindowNode(AssistStructure.WindowNode usernameWindowNode) {
        this.usernameWindowNode = usernameWindowNode;
    }

    public AssistStructure.WindowNode getPasswordWindowNode() {
        return passwordWindowNode;
    }

    public void setPasswordWindowNode(AssistStructure.WindowNode passwordWindowNode) {
        this.passwordWindowNode = passwordWindowNode;
    }
}
