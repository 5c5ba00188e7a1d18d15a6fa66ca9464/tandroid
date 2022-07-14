package org.telegram.messenger.browser;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.List;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.CustomTabsCopyReceiver;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.ShareBroadcastReceiver;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.support.customtabs.CustomTabsCallback;
import org.telegram.messenger.support.customtabs.CustomTabsClient;
import org.telegram.messenger.support.customtabs.CustomTabsIntent;
import org.telegram.messenger.support.customtabs.CustomTabsServiceConnection;
import org.telegram.messenger.support.customtabs.CustomTabsSession;
import org.telegram.messenger.support.customtabsclient.shared.CustomTabsHelper;
import org.telegram.messenger.support.customtabsclient.shared.ServiceConnection;
import org.telegram.messenger.support.customtabsclient.shared.ServiceConnectionCallback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes4.dex */
public class Browser {
    private static WeakReference<Activity> currentCustomTabsActivity;
    private static CustomTabsClient customTabsClient;
    private static WeakReference<CustomTabsSession> customTabsCurrentSession;
    private static String customTabsPackageToBind;
    private static CustomTabsServiceConnection customTabsServiceConnection;
    private static CustomTabsSession customTabsSession;

    private static CustomTabsSession getCurrentSession() {
        WeakReference<CustomTabsSession> weakReference = customTabsCurrentSession;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    private static void setCurrentSession(CustomTabsSession session) {
        customTabsCurrentSession = new WeakReference<>(session);
    }

    private static CustomTabsSession getSession() {
        CustomTabsClient customTabsClient2 = customTabsClient;
        if (customTabsClient2 == null) {
            customTabsSession = null;
        } else if (customTabsSession == null) {
            CustomTabsSession newSession = customTabsClient2.newSession(new NavigationCallback());
            customTabsSession = newSession;
            setCurrentSession(newSession);
        }
        return customTabsSession;
    }

    public static void bindCustomTabsService(Activity activity) {
        WeakReference<Activity> weakReference = currentCustomTabsActivity;
        Activity currentActivity = weakReference == null ? null : weakReference.get();
        if (currentActivity != null && currentActivity != activity) {
            unbindCustomTabsService(currentActivity);
        }
        if (customTabsClient != null) {
            return;
        }
        currentCustomTabsActivity = new WeakReference<>(activity);
        try {
            if (TextUtils.isEmpty(customTabsPackageToBind)) {
                String packageNameToUse = CustomTabsHelper.getPackageNameToUse(activity);
                customTabsPackageToBind = packageNameToUse;
                if (packageNameToUse == null) {
                    return;
                }
            }
            ServiceConnection serviceConnection = new ServiceConnection(new ServiceConnectionCallback() { // from class: org.telegram.messenger.browser.Browser.1
                @Override // org.telegram.messenger.support.customtabsclient.shared.ServiceConnectionCallback
                public void onServiceConnected(CustomTabsClient client) {
                    CustomTabsClient unused = Browser.customTabsClient = client;
                    if (SharedConfig.customTabs && Browser.customTabsClient != null) {
                        try {
                            Browser.customTabsClient.warmup(0L);
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                }

                @Override // org.telegram.messenger.support.customtabsclient.shared.ServiceConnectionCallback
                public void onServiceDisconnected() {
                    CustomTabsClient unused = Browser.customTabsClient = null;
                }
            });
            customTabsServiceConnection = serviceConnection;
            if (!CustomTabsClient.bindCustomTabsService(activity, customTabsPackageToBind, serviceConnection)) {
                customTabsServiceConnection = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static void unbindCustomTabsService(Activity activity) {
        if (customTabsServiceConnection == null) {
            return;
        }
        WeakReference<Activity> weakReference = currentCustomTabsActivity;
        Activity currentActivity = weakReference == null ? null : weakReference.get();
        if (currentActivity == activity) {
            currentCustomTabsActivity.clear();
        }
        try {
            activity.unbindService(customTabsServiceConnection);
        } catch (Exception e) {
        }
        customTabsClient = null;
        customTabsSession = null;
    }

    /* loaded from: classes4.dex */
    public static class NavigationCallback extends CustomTabsCallback {
        private NavigationCallback() {
        }

        @Override // org.telegram.messenger.support.customtabs.CustomTabsCallback
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
        }
    }

    public static void openUrl(Context context, String url) {
        if (url == null) {
            return;
        }
        openUrl(context, Uri.parse(url), true);
    }

    public static void openUrl(Context context, Uri uri) {
        openUrl(context, uri, true);
    }

    public static void openUrl(Context context, String url, boolean allowCustom) {
        if (context == null || url == null) {
            return;
        }
        openUrl(context, Uri.parse(url), allowCustom);
    }

    public static void openUrl(Context context, Uri uri, boolean allowCustom) {
        openUrl(context, uri, allowCustom, true);
    }

    public static void openUrl(Context context, String url, boolean allowCustom, boolean tryTelegraph) {
        openUrl(context, Uri.parse(url), allowCustom, tryTelegraph);
    }

    public static boolean isTelegraphUrl(String url, boolean equals) {
        return isTelegraphUrl(url, equals, false);
    }

    public static boolean isTelegraphUrl(String url, boolean equals, boolean forceHttps) {
        if (equals) {
            return url.equals("telegra.ph") || url.equals("te.legra.ph") || url.equals("graph.org");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("^(https");
        sb.append(forceHttps ? "" : "?");
        sb.append("://)?(te\\.?legra\\.ph|graph\\.org).*");
        return url.matches(sb.toString());
    }

    public static boolean urlMustNotHaveConfirmation(String url) {
        return isTelegraphUrl(url, false, true) || url.matches("^(https://)?t\\.me/iv\\??.*") || url.matches("^(https://)?telegram\\.org/(blog|tour)/?.*");
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x0332 A[Catch: Exception -> 0x0357, TryCatch #3 {Exception -> 0x0357, blocks: (B:116:0x032b, B:118:0x0332, B:119:0x0344), top: B:130:0x032b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void openUrl(final Context context, final Uri uri, final boolean allowCustom, boolean tryTelegraph) {
        Exception e;
        String host;
        String scheme;
        Uri uri2 = uri;
        if (context == null || uri2 == null) {
            return;
        }
        final int currentAccount = UserConfig.selectedAccount;
        boolean[] forceBrowser = {false};
        boolean internalUri = isInternalUri(uri2, forceBrowser);
        if (tryTelegraph) {
            try {
                if (!isTelegraphUrl(uri.getHost().toLowerCase(), true) && !uri.toString().toLowerCase().contains("telegram.org/faq") && !uri.toString().toLowerCase().contains("telegram.org/privacy")) {
                }
                final AlertDialog[] progressDialog = {new AlertDialog(context, 3)};
                TLRPC.TL_messages_getWebPagePreview req = new TLRPC.TL_messages_getWebPagePreview();
                req.message = uri.toString();
                final int reqId = ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda3
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                Browser.lambda$openUrl$0(r1, tLObject, r3, r4, r5, r6);
                            }
                        });
                    }
                });
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Browser.lambda$openUrl$3(progressDialog, reqId);
                    }
                }, 1000L);
                return;
            } catch (Exception e2) {
            }
        }
        try {
            host = "";
            scheme = uri.getScheme() != null ? uri.getScheme().toLowerCase() : host;
            if ("http".equals(scheme) || "https".equals(scheme)) {
                try {
                    uri2 = uri.normalizeScheme();
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
            }
        } catch (Exception e4) {
            e = e4;
        }
        try {
            try {
                if (uri2.getHost() != null) {
                    host = uri2.getHost().toLowerCase();
                }
                if (AccountInstance.getInstance(currentAccount).getMessagesController().autologinDomains.contains(host)) {
                    String token = "autologin_token=" + URLEncoder.encode(AccountInstance.getInstance(UserConfig.selectedAccount).getMessagesController().autologinToken, "UTF-8");
                    String url = uri2.toString();
                    int idx = url.indexOf("://");
                    String path = idx >= 0 ? url.substring(idx + 3) : url;
                    String fragment = uri2.getEncodedFragment();
                    String finalPath = fragment == null ? path : path.substring(0, path.indexOf("#" + fragment));
                    String finalPath2 = finalPath.indexOf(63) >= 0 ? finalPath + "&" + token : finalPath + "?" + token;
                    if (fragment != null) {
                        finalPath2 = finalPath2 + "#" + fragment;
                    }
                    uri2 = Uri.parse("https://" + finalPath2);
                }
                if (allowCustom && SharedConfig.customTabs && !internalUri && !scheme.equals("tel")) {
                    String[] browserPackageNames = null;
                    try {
                        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com"));
                        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(browserIntent, 0);
                        if (list != null && !list.isEmpty()) {
                            browserPackageNames = new String[list.size()];
                            for (int a = 0; a < list.size(); a++) {
                                browserPackageNames[a] = list.get(a).activityInfo.packageName;
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("default browser name = " + browserPackageNames[a]);
                                }
                            }
                        }
                    } catch (Exception e5) {
                    }
                    List<ResolveInfo> allActivities = null;
                    try {
                        Intent viewIntent = new Intent("android.intent.action.VIEW", uri2);
                        allActivities = context.getPackageManager().queryIntentActivities(viewIntent, 0);
                        if (browserPackageNames != null) {
                            int a2 = 0;
                            while (a2 < allActivities.size()) {
                                int b = 0;
                                while (true) {
                                    if (b < browserPackageNames.length) {
                                        if (!browserPackageNames[b].equals(allActivities.get(a2).activityInfo.packageName)) {
                                            b++;
                                        } else {
                                            allActivities.remove(a2);
                                            a2--;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                a2++;
                            }
                        } else {
                            int a3 = 0;
                            while (a3 < allActivities.size()) {
                                if (allActivities.get(a3).activityInfo.packageName.toLowerCase().contains("browser") || allActivities.get(a3).activityInfo.packageName.toLowerCase().contains("chrome")) {
                                    allActivities.remove(a3);
                                    a3--;
                                }
                                a3++;
                            }
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            for (int a4 = 0; a4 < allActivities.size(); a4++) {
                                FileLog.d("device has " + allActivities.get(a4).activityInfo.packageName + " to open " + uri2.toString());
                            }
                        }
                    } catch (Exception e6) {
                    }
                    if (forceBrowser[0] || allActivities == null || allActivities.isEmpty()) {
                        Intent share = new Intent(ApplicationLoader.applicationContext, ShareBroadcastReceiver.class);
                        share.setAction("android.intent.action.SEND");
                        PendingIntent copy = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, new Intent(ApplicationLoader.applicationContext, CustomTabsCopyReceiver.class), 134217728);
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
                        builder.addMenuItem(LocaleController.getString("CopyLink", R.string.CopyLink), copy);
                        builder.setToolbarColor(Theme.getColor(Theme.key_actionBarBrowser));
                        builder.setShowTitle(true);
                        builder.setActionButton(BitmapFactory.decodeResource(context.getResources(), R.drawable.msg_filled_shareout), LocaleController.getString("ShareFile", R.string.ShareFile), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, share, 0), true);
                        CustomTabsIntent intent = builder.build();
                        intent.setUseNewTask();
                        intent.launchUrl(context, uri2);
                        return;
                    }
                }
            } catch (Exception e7) {
                e = e7;
                FileLog.e(e);
                Intent intent2 = new Intent("android.intent.action.VIEW", uri2);
                if (internalUri) {
                }
                intent2.putExtra("create_new_tab", true);
                intent2.putExtra("com.android.browser.application_id", context.getPackageName());
                context.startActivity(intent2);
            }
            Intent intent22 = new Intent("android.intent.action.VIEW", uri2);
            if (internalUri) {
                ComponentName componentName = new ComponentName(context.getPackageName(), LaunchActivity.class.getName());
                intent22.setComponent(componentName);
            }
            intent22.putExtra("create_new_tab", true);
            intent22.putExtra("com.android.browser.application_id", context.getPackageName());
            context.startActivity(intent22);
        } catch (Exception e8) {
            FileLog.e(e8);
        }
    }

    public static /* synthetic */ void lambda$openUrl$0(AlertDialog[] progressDialog, TLObject response, int currentAccount, Uri finalUri, Context context, boolean allowCustom) {
        try {
            progressDialog[0].dismiss();
        } catch (Throwable th) {
        }
        progressDialog[0] = null;
        boolean ok = false;
        if (response instanceof TLRPC.TL_messageMediaWebPage) {
            TLRPC.TL_messageMediaWebPage webPage = (TLRPC.TL_messageMediaWebPage) response;
            if ((webPage.webpage instanceof TLRPC.TL_webPage) && webPage.webpage.cached_page != null) {
                NotificationCenter.getInstance(currentAccount).postNotificationName(NotificationCenter.openArticle, webPage.webpage, finalUri.toString());
                ok = true;
            }
        }
        if (!ok) {
            openUrl(context, finalUri, allowCustom, false);
        }
    }

    public static /* synthetic */ void lambda$openUrl$3(AlertDialog[] progressDialog, final int reqId) {
        if (progressDialog[0] == null) {
            return;
        }
        try {
            progressDialog[0].setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    ConnectionsManager.getInstance(UserConfig.selectedAccount).cancelRequest(reqId, true);
                }
            });
            progressDialog[0].show();
        } catch (Exception e) {
        }
    }

    public static boolean isInternalUrl(String url, boolean[] forceBrowser) {
        return isInternalUri(Uri.parse(url), false, forceBrowser);
    }

    public static boolean isInternalUrl(String url, boolean all, boolean[] forceBrowser) {
        return isInternalUri(Uri.parse(url), all, forceBrowser);
    }

    public static boolean isPassportUrl(String url) {
        String url2;
        if (url == null) {
            return false;
        }
        try {
            url2 = url.toLowerCase();
        } catch (Throwable th) {
        }
        if (!url2.startsWith("tg:passport") && !url2.startsWith("tg://passport") && !url2.startsWith("tg:secureid")) {
            if (url2.contains("resolve")) {
                if (url2.contains("domain=telegrampassport")) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean isInternalUri(Uri uri, boolean[] forceBrowser) {
        return isInternalUri(uri, false, forceBrowser);
    }

    public static boolean isInternalUri(Uri uri, boolean all, boolean[] forceBrowser) {
        String host = uri.getHost();
        String host2 = host != null ? host.toLowerCase() : "";
        if ("ton".equals(uri.getScheme())) {
            try {
                Intent viewIntent = new Intent("android.intent.action.VIEW", uri);
                List<ResolveInfo> allActivities = ApplicationLoader.applicationContext.getPackageManager().queryIntentActivities(viewIntent, 0);
                if (allActivities != null) {
                    if (allActivities.size() > 1) {
                        return false;
                    }
                }
            } catch (Exception e) {
            }
            return true;
        } else if ("tg".equals(uri.getScheme())) {
            return true;
        } else {
            if ("telegram.dog".equals(host2)) {
                String path = uri.getPath();
                if (path != null && path.length() > 1) {
                    if (all) {
                        return true;
                    }
                    String path2 = path.substring(1).toLowerCase();
                    if (!path2.startsWith("blog") && !path2.equals("iv") && !path2.startsWith("faq") && !path2.equals("apps") && !path2.startsWith("s/")) {
                        return true;
                    }
                    if (forceBrowser != null) {
                        forceBrowser[0] = true;
                    }
                    return false;
                }
            } else if ("telegram.me".equals(host2) || "t.me".equals(host2)) {
                String path3 = uri.getPath();
                if (path3 != null && path3.length() > 1) {
                    if (all) {
                        return true;
                    }
                    String path4 = path3.substring(1).toLowerCase();
                    if (!path4.equals("iv") && !path4.startsWith("s/")) {
                        return true;
                    }
                    if (forceBrowser != null) {
                        forceBrowser[0] = true;
                    }
                    return false;
                }
            } else if (all && (host2.endsWith("telegram.org") || host2.endsWith("telegra.ph") || host2.endsWith("telesco.pe"))) {
                return true;
            }
            return false;
        }
    }
}
