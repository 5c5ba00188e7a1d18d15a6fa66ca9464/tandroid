package org.telegram.messenger.browser;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.net.IDN;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.CustomTabsCopyReceiver;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.ShareBroadcastReceiver;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.browser.Browser;
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
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheetTabs;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.web.RestrictedDomainsList;

/* loaded from: classes3.dex */
public abstract class Browser {
    private static WeakReference currentCustomTabsActivity;
    private static CustomTabsClient customTabsClient;
    private static WeakReference customTabsCurrentSession;
    private static String customTabsPackageToBind;
    private static CustomTabsServiceConnection customTabsServiceConnection;
    private static CustomTabsSession customTabsSession;
    private static Pattern domainPattern;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class NavigationCallback extends CustomTabsCallback {
        private NavigationCallback() {
        }

        @Override // org.telegram.messenger.support.customtabs.CustomTabsCallback
        public void onNavigationEvent(int i, Bundle bundle) {
        }
    }

    /* loaded from: classes3.dex */
    public static class Progress {
        private Runnable onCancelListener;
        private Runnable onEndListener;

        public void cancel() {
            cancel(false);
        }

        public void cancel(boolean z) {
            Runnable runnable = this.onCancelListener;
            if (runnable != null) {
                runnable.run();
            }
            end(z);
        }

        public void end() {
            end(false);
        }

        public void end(boolean z) {
            Runnable runnable = this.onEndListener;
            if (runnable != null) {
                runnable.run();
            }
        }

        public void init() {
        }

        public void onCancel(Runnable runnable) {
            this.onCancelListener = runnable;
        }

        public void onEnd(Runnable runnable) {
            this.onEndListener = runnable;
        }
    }

    public static String IDN_toUnicode(String str) {
        try {
            str = IDN.toASCII(str, 1);
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (!isPunycodeAllowed(str)) {
            return str;
        }
        try {
            return IDN.toUnicode(str, 1);
        } catch (Exception e2) {
            FileLog.e(e2);
            return str;
        }
    }

    public static void bindCustomTabsService(Activity activity) {
        WeakReference weakReference = currentCustomTabsActivity;
        Activity activity2 = weakReference == null ? null : (Activity) weakReference.get();
        if (activity2 != null && activity2 != activity) {
            unbindCustomTabsService(activity2);
        }
        if (customTabsClient != null) {
            return;
        }
        currentCustomTabsActivity = new WeakReference(activity);
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
                public void onServiceConnected(CustomTabsClient customTabsClient2) {
                    CustomTabsClient unused = Browser.customTabsClient = customTabsClient2;
                    if (!SharedConfig.customTabs || Browser.customTabsClient == null) {
                        return;
                    }
                    try {
                        Browser.customTabsClient.warmup(0L);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }

                @Override // org.telegram.messenger.support.customtabsclient.shared.ServiceConnectionCallback
                public void onServiceDisconnected() {
                    CustomTabsClient unused = Browser.customTabsClient = null;
                }
            });
            customTabsServiceConnection = serviceConnection;
            if (CustomTabsClient.bindCustomTabsService(activity, customTabsPackageToBind, serviceConnection)) {
                return;
            }
            customTabsServiceConnection = null;
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static String extractUsername(String str) {
        int i;
        if (str != null && !TextUtils.isEmpty(str)) {
            if (str.startsWith("@")) {
                return str.substring(1);
            }
            if (str.startsWith("t.me/")) {
                i = 5;
            } else if (str.startsWith("http://t.me/")) {
                i = 12;
            } else if (str.startsWith("https://t.me/")) {
                i = 13;
            } else {
                Matcher matcher = LaunchActivity.PREFIX_T_ME_PATTERN.matcher(str);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
            return str.substring(i);
        }
        return null;
    }

    public static String getBrowserPackageName(String str) {
        if (str == null) {
            return null;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case -1973822757:
                if (str.equals("brave-browser")) {
                    c = 0;
                    break;
                }
                break;
            case -1451156338:
                if (str.equals("google-chrome")) {
                    c = 1;
                    break;
                }
                break;
            case -1361128838:
                if (str.equals("chrome")) {
                    c = 2;
                    break;
                }
                break;
            case -1270430916:
                if (str.equals("microsoft-edge")) {
                    c = 3;
                    break;
                }
                break;
            case -1249474382:
                if (str.equals("tor-browser")) {
                    c = 4;
                    break;
                }
                break;
            case -1051190859:
                if (str.equals("duckduckgo-browser")) {
                    c = 5;
                    break;
                }
                break;
            case -849452327:
                if (str.equals("firefox")) {
                    c = 6;
                    break;
                }
                break;
            case -329108395:
                if (str.equals("samsung-browser")) {
                    c = 7;
                    break;
                }
                break;
            case -220816629:
                if (str.equals("kiwi-browser")) {
                    c = '\b';
                    break;
                }
                break;
            case -61272559:
                if (str.equals("opera-mini")) {
                    c = '\t';
                    break;
                }
                break;
            case 3726:
                if (str.equals("uc")) {
                    c = '\n';
                    break;
                }
                break;
            case 115031:
                if (str.equals("tor")) {
                    c = 11;
                    break;
                }
                break;
            case 3108285:
                if (str.equals("edge")) {
                    c = '\f';
                    break;
                }
                break;
            case 3292336:
                if (str.equals("kiwi")) {
                    c = '\r';
                    break;
                }
                break;
            case 93998208:
                if (str.equals("brave")) {
                    c = 14;
                    break;
                }
                break;
            case 105948115:
                if (str.equals("opera")) {
                    c = 15;
                    break;
                }
                break;
            case 469285011:
                if (str.equals("vivaldi")) {
                    c = 16;
                    break;
                }
                break;
            case 557649660:
                if (str.equals("mozilla-firefox")) {
                    c = 17;
                    break;
                }
                break;
            case 696911194:
                if (str.equals("duckduckgo")) {
                    c = 18;
                    break;
                }
                break;
            case 1117815790:
                if (str.equals("vivaldi-browser")) {
                    c = 19;
                    break;
                }
                break;
            case 1201385193:
                if (str.equals("uc-browser")) {
                    c = 20;
                    break;
                }
                break;
            case 1864941562:
                if (str.equals("samsung")) {
                    c = 21;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 14:
                return "com.brave.browser";
            case 1:
            case 2:
                return "com.android.chrome";
            case 3:
            case '\f':
                return "com.microsoft.emmx";
            case 4:
            case 11:
                return "org.torproject.torbrowser";
            case 5:
            case 18:
                return "com.duckduckgo.mobile.android";
            case 6:
            case 17:
                return "org.mozilla.firefox";
            case 7:
            case 21:
                return "com.sec.android.app.sbrowser";
            case '\b':
            case '\r':
                return "com.kiwibrowser.browser";
            case '\t':
                return "com.opera.mini.native";
            case '\n':
            case 20:
                return "com.UCMobile.intl";
            case 15:
                return "com.opera.browser";
            case 16:
            case 19:
                return "com.vivaldi.browser";
            default:
                return null;
        }
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

    /* JADX WARN: Can't wrap try/catch for region: R(11:5|(2:6|7)|(11:11|12|(5:16|(2:18|19)(1:21)|20|13|14)|22|23|24|25|(2:(4:29|(2:30|(1:1)(2:32|(3:35|36|37)(1:34)))|38|27)|40)(2:(4:58|(2:60|61)(1:63)|62|56)|64)|41|(3:43|(3:46|47|44)|48)|(1:54)(1:53))|69|23|24|25|(0)(0)|41|(0)|(2:51|54)(1:55)) */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006e A[Catch: Exception -> 0x0090, LOOP:1: B:27:0x006e->B:38:0x0095, LOOP_START, PHI: r8
      0x006e: PHI (r8v12 int) = (r8v3 int), (r8v14 int) binds: [B:26:0x006c, B:38:0x0095] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {Exception -> 0x0090, blocks: (B:25:0x005a, B:27:0x006e, B:30:0x0075, B:32:0x0078, B:36:0x008a, B:34:0x0092, B:38:0x0095, B:41:0x00b8, B:44:0x00bd, B:46:0x00c3, B:56:0x0097, B:58:0x009d, B:60:0x00b1, B:62:0x00b6), top: B:24:0x005a }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0097 A[Catch: Exception -> 0x0090, LOOP:4: B:56:0x0097->B:62:0x00b6, LOOP_START, PHI: r8
      0x0097: PHI (r8v4 int) = (r8v3 int), (r8v6 int) binds: [B:26:0x006c, B:62:0x00b6] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {Exception -> 0x0090, blocks: (B:25:0x005a, B:27:0x006e, B:30:0x0075, B:32:0x0078, B:36:0x008a, B:34:0x0092, B:38:0x0095, B:41:0x00b8, B:44:0x00bd, B:46:0x00c3, B:56:0x0097, B:58:0x009d, B:60:0x00b1, B:62:0x00b6), top: B:24:0x005a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean hasAppToOpen(Context context, String str) {
        String[] strArr;
        List<ResolveInfo> queryIntentActivities;
        if (str == null) {
            return false;
        }
        List<ResolveInfo> list = null;
        try {
            queryIntentActivities = context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com")), 0);
        } catch (Exception unused) {
        }
        if (queryIntentActivities != null && !queryIntentActivities.isEmpty()) {
            strArr = new String[queryIntentActivities.size()];
            for (int i = 0; i < queryIntentActivities.size(); i++) {
                try {
                    strArr[i] = queryIntentActivities.get(i).activityInfo.packageName;
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("default browser name = " + strArr[i]);
                    }
                } catch (Exception unused2) {
                }
            }
            list = context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse(str)), 0);
            int i2 = 0;
            if (strArr == null) {
                while (i2 < list.size()) {
                    int i3 = 0;
                    while (true) {
                        if (i3 >= strArr.length) {
                            break;
                        }
                        if (strArr[i3].equals(list.get(i2).activityInfo.packageName)) {
                            list.remove(i2);
                            i2--;
                            break;
                        }
                        i3++;
                    }
                    i2++;
                }
            } else {
                while (i2 < list.size()) {
                    if (isBrowserPackageName(list.get(i2).activityInfo.packageName.toLowerCase())) {
                        list.remove(i2);
                        i2--;
                    }
                    i2++;
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                for (int i4 = 0; i4 < list.size(); i4++) {
                    FileLog.d("device has " + list.get(i4).activityInfo.packageName + " to open " + str);
                }
            }
            return (list == null || list.isEmpty()) ? false : true;
        }
        strArr = null;
        list = context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse(str)), 0);
        int i22 = 0;
        if (strArr == null) {
        }
        if (BuildVars.LOGS_ENABLED) {
        }
        if (list == null) {
            return false;
        }
    }

    public static boolean isBrowserPackageName(String str) {
        return str != null && (str.contains("browser") || str.contains("chrome") || str.contains("firefox") || "com.microsoft.emmx".equals(str) || "com.opera.mini.native".equals(str) || "com.duckduckgo.mobile.android".equals(str) || "com.UCMobile.intl".equals(str));
    }

    public static boolean isInternalUri(Uri uri, boolean z, boolean[] zArr) {
        String str;
        String str2;
        String hostAuthority = AndroidUtilities.getHostAuthority(uri);
        String str3 = "";
        String lowerCase = hostAuthority != null ? hostAuthority.toLowerCase() : "";
        if (MessagesController.getInstance(UserConfig.selectedAccount).authDomains.contains(lowerCase)) {
            if (zArr != null) {
                zArr[0] = true;
            }
            return false;
        }
        Matcher matcher = LaunchActivity.PREFIX_T_ME_PATTERN.matcher(lowerCase);
        if (matcher.find()) {
            StringBuilder sb = new StringBuilder();
            sb.append("https://t.me/");
            sb.append(matcher.group(1));
            if (TextUtils.isEmpty(uri.getPath())) {
                str = "";
            } else {
                str = "/" + uri.getPath();
            }
            sb.append(str);
            if (TextUtils.isEmpty(uri.getQuery())) {
                str2 = "";
            } else {
                str2 = "?" + uri.getQuery();
            }
            sb.append(str2);
            uri = Uri.parse(sb.toString());
            String host = uri.getHost();
            if (host != null) {
                str3 = host.toLowerCase();
            }
        } else {
            str3 = lowerCase;
        }
        if ("ton".equals(uri.getScheme())) {
            try {
                List<ResolveInfo> queryIntentActivities = ApplicationLoader.applicationContext.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", uri), 0);
                if (queryIntentActivities != null) {
                    if (queryIntentActivities.size() >= 1) {
                        return false;
                    }
                }
            } catch (Exception unused) {
            }
            return true;
        }
        if ("tg".equals(uri.getScheme())) {
            return true;
        }
        if ("telegram.dog".equals(str3)) {
            String path = uri.getPath();
            if (path != null && path.length() > 1) {
                if (z) {
                    return true;
                }
                String lowerCase2 = path.substring(1).toLowerCase();
                if (!lowerCase2.startsWith("blog") && !lowerCase2.equals("iv") && !lowerCase2.startsWith("faq") && !lowerCase2.equals("apps") && !lowerCase2.startsWith("s/")) {
                    return true;
                }
                if (zArr != null) {
                    zArr[0] = true;
                }
                return false;
            }
        } else if ("telegram.me".equals(str3) || "t.me".equals(str3)) {
            String path2 = uri.getPath();
            if (path2 != null && path2.length() > 1) {
                if (z) {
                    return true;
                }
                String lowerCase3 = path2.substring(1).toLowerCase();
                if (!lowerCase3.equals("iv") && !lowerCase3.startsWith("s/")) {
                    return true;
                }
                if (zArr != null) {
                    zArr[0] = true;
                }
            }
        } else {
            if ("telegram.org".equals(str3) && uri.getPath() != null && uri.getPath().startsWith("/blog/")) {
                return true;
            }
            if (z && (str3.endsWith("telegram.org") || str3.endsWith("telegra.ph") || str3.endsWith("telesco.pe"))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInternalUri(Uri uri, boolean[] zArr) {
        return isInternalUri(uri, false, zArr);
    }

    public static boolean isInternalUrl(String str, boolean z, boolean[] zArr) {
        return isInternalUri(Uri.parse(str), z, zArr);
    }

    public static boolean isInternalUrl(String str, boolean[] zArr) {
        return isInternalUri(Uri.parse(str), false, zArr);
    }

    public static boolean isPassportUrl(String str) {
        String lowerCase;
        if (str == null) {
            return false;
        }
        try {
            lowerCase = str.toLowerCase();
        } catch (Throwable unused) {
        }
        if (lowerCase.startsWith("tg:passport") || lowerCase.startsWith("tg://passport") || lowerCase.startsWith("tg:secureid")) {
            return true;
        }
        if (lowerCase.contains("resolve")) {
            if (lowerCase.contains("domain=telegrampassport")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPunycodeAllowed(String str) {
        if (str == null) {
            return true;
        }
        String[] split = str.split("\\.");
        if (split.length <= 0) {
            return true;
        }
        return split[split.length - 1].startsWith("xn--");
    }

    public static boolean isTMe(String str) {
        try {
            return TextUtils.equals(AndroidUtilities.getHostAuthority(str), MessagesController.getInstance(UserConfig.selectedAccount).linkPrefix);
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    public static boolean isTelegraphUrl(String str, boolean z) {
        return isTelegraphUrl(str, z, false);
    }

    public static boolean isTelegraphUrl(String str, boolean z, boolean z2) {
        if (z) {
            return str.equals("telegra.ph") || str.equals("te.legra.ph") || str.equals("graph.org");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("^(https");
        sb.append(z2 ? "" : "?");
        sb.append("://)?(te\\.?legra\\.ph|graph\\.org)(/.*|$)");
        return str.matches(sb.toString());
    }

    public static boolean isTonsite(String str) {
        String hostAuthority = AndroidUtilities.getHostAuthority(str, true);
        if (hostAuthority != null && (hostAuthority.endsWith(".ton") || hostAuthority.endsWith(".adnl"))) {
            return true;
        }
        Uri parse = Uri.parse(str);
        return parse.getScheme() != null && parse.getScheme().equalsIgnoreCase("tonsite");
    }

    public static boolean isTonsitePunycode(String str) {
        Matcher matcher;
        if (domainPattern == null) {
            domainPattern = Pattern.compile("^[a-zA-Z0-9\\-\\_\\.]+\\.[a-zA-Z0-9\\-\\_]+$");
        }
        String hostAuthority = AndroidUtilities.getHostAuthority(str, true);
        if (hostAuthority == null || !(hostAuthority.endsWith(".ton") || hostAuthority.endsWith(".adnl"))) {
            Uri parse = Uri.parse(str);
            if (parse.getScheme() == null || !parse.getScheme().equalsIgnoreCase("tonsite")) {
                return false;
            }
            matcher = domainPattern.matcher(parse.getScheme());
        } else {
            matcher = domainPattern.matcher(hostAuthority);
        }
        return !matcher.matches();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openUrl$0(Progress progress, AlertDialog[] alertDialogArr, TLObject tLObject, int i, Uri uri, Context context, boolean z) {
        if (progress != null) {
            progress.end();
        } else {
            try {
                alertDialogArr[0].dismiss();
            } catch (Throwable unused) {
            }
            alertDialogArr[0] = null;
        }
        if (tLObject instanceof TLRPC.TL_messageMediaWebPage) {
            TLRPC.TL_messageMediaWebPage tL_messageMediaWebPage = (TLRPC.TL_messageMediaWebPage) tLObject;
            TLRPC.WebPage webPage = tL_messageMediaWebPage.webpage;
            if ((webPage instanceof TLRPC.TL_webPage) && webPage.cached_page != null) {
                NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.openArticle, tL_messageMediaWebPage.webpage, uri.toString());
                return;
            }
        }
        openUrl(context, uri, z, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openUrl$1(final Progress progress, final AlertDialog[] alertDialogArr, final int i, final Uri uri, final Context context, final boolean z, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                Browser.lambda$openUrl$0(Browser.Progress.this, alertDialogArr, tLObject, i, uri, context, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openUrl$2(int i, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(UserConfig.selectedAccount).cancelRequest(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openUrl$3(AlertDialog[] alertDialogArr, final int i) {
        AlertDialog alertDialog = alertDialogArr[0];
        if (alertDialog == null) {
            return;
        }
        try {
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    Browser.lambda$openUrl$2(i, dialogInterface);
                }
            });
            alertDialogArr[0].show();
        } catch (Exception unused) {
        }
    }

    public static boolean openAsInternalIntent(Context context, String str) {
        return openAsInternalIntent(context, str, false, false, null);
    }

    public static boolean openAsInternalIntent(Context context, String str, boolean z, boolean z2, Progress progress) {
        LaunchActivity launchActivity;
        if (str == null) {
            return false;
        }
        if (AndroidUtilities.findActivity(context) instanceof LaunchActivity) {
            launchActivity = (LaunchActivity) AndroidUtilities.findActivity(context);
        } else {
            launchActivity = LaunchActivity.instance;
            if (launchActivity == null) {
                return false;
            }
        }
        if (launchActivity == null) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.setComponent(new ComponentName(context.getPackageName(), LaunchActivity.class.getName()));
        intent.putExtra("create_new_tab", true);
        intent.putExtra("com.android.browser.application_id", context.getPackageName());
        intent.putExtra("force_not_internal_apps", z);
        intent.putExtra("force_request", z2);
        launchActivity.onNewIntent(intent, progress);
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x007c A[Catch: Exception -> 0x0020, ActivityNotFoundException -> 0x0023, TryCatch #2 {ActivityNotFoundException -> 0x0023, Exception -> 0x0020, blocks: (B:6:0x0004, B:8:0x000a, B:11:0x0013, B:14:0x002a, B:16:0x0030, B:17:0x003d, B:20:0x004e, B:22:0x005f, B:24:0x0065, B:32:0x007c, B:34:0x008e, B:36:0x0094, B:37:0x00b2, B:41:0x00ab, B:44:0x0085, B:46:0x004a, B:47:0x0039, B:48:0x0026), top: B:5:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0085 A[Catch: Exception -> 0x0020, ActivityNotFoundException -> 0x0023, TryCatch #2 {ActivityNotFoundException -> 0x0023, Exception -> 0x0020, blocks: (B:6:0x0004, B:8:0x000a, B:11:0x0013, B:14:0x002a, B:16:0x0030, B:17:0x003d, B:20:0x004e, B:22:0x005f, B:24:0x0065, B:32:0x007c, B:34:0x008e, B:36:0x0094, B:37:0x00b2, B:41:0x00ab, B:44:0x0085, B:46:0x004a, B:47:0x0039, B:48:0x0026), top: B:5:0x0004 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean openInExternalApp(Context context, String str, boolean z) {
        boolean z2;
        if (str == null) {
            return false;
        }
        try {
            if (!isTonsite(str) && !isInternalUrl(str, null)) {
                Uri parse = Uri.parse(str);
                String replace = replace(parse, parse.getScheme() == null ? "https" : parse.getScheme(), null, parse.getHost() != null ? parse.getHost().toLowerCase() : parse.getHost(), TextUtils.isEmpty(parse.getPath()) ? "/" : parse.getPath());
                Uri parse2 = Uri.parse(replace);
                if (!replace.startsWith("intent://") && (parse2.getScheme() == null || !parse2.getScheme().equalsIgnoreCase("intent"))) {
                    z2 = false;
                    if (!z2 && !z) {
                        return false;
                    }
                    Intent parseUri = !z2 ? Intent.parseUri(parse2.toString(), 1) : new Intent("android.intent.action.VIEW", parse2);
                    if (z2 && Build.VERSION.SDK_INT >= 30) {
                        parseUri.addCategory("android.intent.category.BROWSABLE");
                        parseUri.addCategory("android.intent.category.DEFAULT");
                        parseUri.addFlags(268435456);
                        parseUri.addFlags(1024);
                    } else if (!z2 && !hasAppToOpen(context, replace)) {
                        return false;
                    }
                    context.startActivity(parseUri);
                    return true;
                }
                z2 = true;
                if (!z2) {
                }
                if (!z2) {
                }
                if (z2) {
                }
                if (!z2) {
                    return false;
                }
                context.startActivity(parseUri);
                return true;
            }
            return false;
        } catch (ActivityNotFoundException e) {
            FileLog.e((Throwable) e, false);
            return false;
        } catch (Exception e2) {
            FileLog.e(e2);
            return false;
        }
    }

    public static boolean openInExternalBrowser(Context context, String str, boolean z) {
        return openInExternalBrowser(context, str, z, null);
    }

    public static boolean openInExternalBrowser(Context context, String str, boolean z, String str2) {
        if (str == null) {
            return false;
        }
        try {
            Uri parse = Uri.parse(str);
            boolean z2 = parse.getScheme() != null && parse.getScheme().equalsIgnoreCase("intent");
            if (z2 && !z) {
                return false;
            }
            Intent parseUri = z2 ? Intent.parseUri(parse.toString(), 1) : new Intent("android.intent.action.VIEW", parse);
            if (!TextUtils.isEmpty(str2)) {
                parseUri.setPackage(str2);
            }
            parseUri.putExtra("create_new_tab", true);
            parseUri.putExtra("com.android.browser.application_id", context.getPackageName());
            context.startActivity(parseUri);
            return true;
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    public static boolean openInTelegramBrowser(Context context, String str, Progress progress) {
        BottomSheetTabs bottomSheetTabs;
        LaunchActivity launchActivity = LaunchActivity.instance;
        if (launchActivity != null && (bottomSheetTabs = launchActivity.getBottomSheetTabs()) != null && bottomSheetTabs.tryReopenTab(str) != null) {
            return true;
        }
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null && (safeLastFragment.getParentLayout() instanceof ActionBarLayout)) {
            safeLastFragment = ((ActionBarLayout) safeLastFragment.getParentLayout()).getSheetFragment();
        }
        if (safeLastFragment == null) {
            return false;
        }
        safeLastFragment.createArticleViewer(false).open(str, progress);
        return true;
    }

    public static void openUrl(Context context, Uri uri) {
        openUrl(context, uri, true);
    }

    public static void openUrl(Context context, Uri uri, boolean z) {
        openUrl(context, uri, z, true);
    }

    public static void openUrl(Context context, Uri uri, boolean z, boolean z2) {
        openUrl(context, uri, z, z2, false, null, null, false, true, false);
    }

    public static void openUrl(Context context, Uri uri, boolean z, boolean z2, Progress progress) {
        openUrl(context, uri, z, z2, false, progress, null, false, true, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x00f5, code lost:
    
        if ("https".equals(r1) != false) goto L156;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x02fa  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x02aa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0120 A[Catch: Exception -> 0x0166, TryCatch #3 {Exception -> 0x0166, blocks: (B:21:0x0104, B:23:0x0120, B:27:0x0152, B:29:0x015f, B:30:0x0169, B:33:0x0172, B:34:0x018a, B:36:0x0192, B:37:0x019f, B:38:0x01b1, B:40:0x01b7, B:41:0x01c9, B:42:0x01a3, B:44:0x01e0, B:46:0x01e4, B:49:0x01ea, B:51:0x01f3, B:53:0x01fd, B:55:0x0202, B:57:0x020c, B:60:0x021a, B:62:0x0226, B:65:0x0238), top: B:20:0x0104 }] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0303 A[Catch: Exception -> 0x02eb, TRY_ENTER, TryCatch #2 {Exception -> 0x02eb, blocks: (B:103:0x02aa, B:105:0x02ae, B:107:0x02b4, B:109:0x02c3, B:111:0x02c9, B:113:0x02d3, B:115:0x02dd, B:77:0x02fb, B:80:0x0303, B:82:0x030c, B:84:0x0310, B:87:0x031e, B:89:0x0328, B:91:0x032e, B:93:0x0338, B:95:0x034d, B:96:0x0351, B:99:0x0359, B:74:0x02ee), top: B:102:0x02aa }] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x030c A[Catch: Exception -> 0x02eb, TryCatch #2 {Exception -> 0x02eb, blocks: (B:103:0x02aa, B:105:0x02ae, B:107:0x02b4, B:109:0x02c3, B:111:0x02c9, B:113:0x02d3, B:115:0x02dd, B:77:0x02fb, B:80:0x0303, B:82:0x030c, B:84:0x0310, B:87:0x031e, B:89:0x0328, B:91:0x032e, B:93:0x0338, B:95:0x034d, B:96:0x0351, B:99:0x0359, B:74:0x02ee), top: B:102:0x02aa }] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x031e A[Catch: Exception -> 0x02eb, TryCatch #2 {Exception -> 0x02eb, blocks: (B:103:0x02aa, B:105:0x02ae, B:107:0x02b4, B:109:0x02c3, B:111:0x02c9, B:113:0x02d3, B:115:0x02dd, B:77:0x02fb, B:80:0x0303, B:82:0x030c, B:84:0x0310, B:87:0x031e, B:89:0x0328, B:91:0x032e, B:93:0x0338, B:95:0x034d, B:96:0x0351, B:99:0x0359, B:74:0x02ee), top: B:102:0x02aa }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0359 A[Catch: Exception -> 0x02eb, TRY_LEAVE, TryCatch #2 {Exception -> 0x02eb, blocks: (B:103:0x02aa, B:105:0x02ae, B:107:0x02b4, B:109:0x02c3, B:111:0x02c9, B:113:0x02d3, B:115:0x02dd, B:77:0x02fb, B:80:0x0303, B:82:0x030c, B:84:0x0310, B:87:0x031e, B:89:0x0328, B:91:0x032e, B:93:0x0338, B:95:0x034d, B:96:0x0351, B:99:0x0359, B:74:0x02ee), top: B:102:0x02aa }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void openUrl(final Context context, final Uri uri, boolean z, boolean z2, boolean z3, final Progress progress, String str, boolean z4, boolean z5, boolean z6) {
        boolean z7;
        boolean z8;
        String str2;
        boolean[] zArr;
        Uri uri2;
        boolean z9;
        LaunchActivity launchActivity;
        String lowerCase;
        String str3;
        String hostAuthority;
        StringBuilder sb;
        if (context == null || uri == null) {
            return;
        }
        final int i = UserConfig.selectedAccount;
        boolean[] zArr2 = {false};
        boolean isInternalUri = isInternalUri(uri, zArr2);
        String browserPackageName = getBrowserPackageName(str);
        if (browserPackageName != null) {
            z8 = false;
            z7 = false;
        } else {
            z7 = z;
            z8 = z2;
        }
        if (z8) {
            try {
                String hostAuthority2 = AndroidUtilities.getHostAuthority(uri);
                if (UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser() != null) {
                    if (!isTelegraphUrl(hostAuthority2, true)) {
                        if ("telegram.org".equalsIgnoreCase(hostAuthority2)) {
                            if (!uri.toString().toLowerCase().contains("telegram.org/faq")) {
                                if (!uri.toString().toLowerCase().contains("telegram.org/privacy")) {
                                    if (uri.toString().toLowerCase().contains("telegram.org/blog")) {
                                    }
                                }
                            }
                        }
                    }
                    final AlertDialog[] alertDialogArr = {new AlertDialog(context, 3)};
                    TLRPC.TL_messages_getWebPagePreview tL_messages_getWebPagePreview = new TLRPC.TL_messages_getWebPagePreview();
                    tL_messages_getWebPagePreview.message = uri.toString();
                    str2 = browserPackageName;
                    zArr = zArr2;
                    final boolean z10 = z7;
                    try {
                        final int sendRequest = ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tL_messages_getWebPagePreview, new RequestDelegate() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda0
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                Browser.lambda$openUrl$1(Browser.Progress.this, alertDialogArr, i, uri, context, z10, tLObject, tL_error);
                            }
                        });
                        if (progress != null) {
                            progress.init();
                            return;
                        } else {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    Browser.lambda$openUrl$3(alertDialogArr, sendRequest);
                                }
                            }, 1000L);
                            return;
                        }
                    } catch (Exception unused) {
                    }
                }
            } catch (Exception unused2) {
            }
        }
        str2 = browserPackageName;
        zArr = zArr2;
        try {
            if (uri.getScheme() != null) {
                try {
                    lowerCase = uri.getScheme().toLowerCase();
                } catch (Exception e) {
                    e = e;
                    uri2 = uri;
                    FileLog.e(e);
                    if (z5) {
                    }
                    if (!isTonsite(uri2.toString())) {
                    }
                    z9 = true;
                    if (uri2.getScheme() != null) {
                    }
                    if (!isInternalUri) {
                    }
                    if (z9) {
                    }
                }
            } else {
                lowerCase = "";
            }
            str3 = lowerCase;
            if (!"http".equals(str3)) {
            }
            try {
                uri2 = uri.normalizeScheme();
            } catch (Exception e2) {
                FileLog.e(e2);
                uri2 = uri;
                hostAuthority = AndroidUtilities.getHostAuthority(uri2.toString().toLowerCase());
                if (AccountInstance.getInstance(i).getMessagesController().autologinDomains.contains(hostAuthority)) {
                }
                if (z7) {
                }
                if (z5) {
                }
                if (!isTonsite(uri2.toString())) {
                }
                z9 = true;
                if (uri2.getScheme() != null) {
                }
                if (!isInternalUri) {
                }
                if (z9) {
                }
            }
            try {
                hostAuthority = AndroidUtilities.getHostAuthority(uri2.toString().toLowerCase());
                if (AccountInstance.getInstance(i).getMessagesController().autologinDomains.contains(hostAuthority)) {
                    String str4 = "autologin_token=" + URLEncoder.encode(AccountInstance.getInstance(UserConfig.selectedAccount).getMessagesController().autologinToken, "UTF-8");
                    String uri3 = uri2.toString();
                    int indexOf = uri3.indexOf("://");
                    if (indexOf >= 0 && indexOf <= 5 && !uri3.substring(0, indexOf).contains(".")) {
                        uri3 = uri3.substring(indexOf + 3);
                    }
                    String encodedFragment = uri2.getEncodedFragment();
                    if (encodedFragment != null) {
                        uri3 = uri3.substring(0, uri3.indexOf("#" + encodedFragment));
                    }
                    if (uri3.indexOf(63) >= 0) {
                        sb = new StringBuilder();
                        sb.append(uri3);
                        sb.append("&");
                    } else {
                        sb = new StringBuilder();
                        sb.append(uri3);
                        sb.append("?");
                    }
                    sb.append(str4);
                    String sb2 = sb.toString();
                    if (encodedFragment != null) {
                        sb2 = sb2 + "#" + encodedFragment;
                    }
                    uri2 = Uri.parse("https://" + sb2);
                }
            } catch (Exception e3) {
                e = e3;
                FileLog.e(e);
                if (z5) {
                }
                if (!isTonsite(uri2.toString())) {
                }
                z9 = true;
                if (uri2.getScheme() != null) {
                }
                if (!isInternalUri) {
                }
                if (z9) {
                }
            }
        } catch (Exception e4) {
            e = e4;
            uri2 = uri;
            FileLog.e(e);
            if (z5) {
            }
            if (!isTonsite(uri2.toString())) {
            }
            z9 = true;
            if (uri2.getScheme() != null) {
            }
            if (!isInternalUri) {
            }
            if (z9) {
            }
        }
        if (z7 || SharedConfig.inappBrowser || !SharedConfig.customTabs || isInternalUri || str3.equals("tel") || isTonsite(uri2.toString()) || (!zArr[0] && openInExternalApp(context, uri2.toString(), false) && hasAppToOpen(context, uri2.toString()))) {
            if (z5) {
                try {
                    if (SharedConfig.inappBrowser) {
                        if (TextUtils.isEmpty(str2)) {
                            if (!RestrictedDomainsList.getInstance().isRestricted(AndroidUtilities.getHostAuthority(uri2, true))) {
                                if (uri2.getScheme() != null && !"https".equals(uri2.getScheme()) && !"http".equals(uri2.getScheme()) && !"tonsite".equals(uri2.getScheme())) {
                                }
                                z9 = true;
                                if (uri2.getScheme() != null) {
                                    uri2.getScheme().equalsIgnoreCase("intent");
                                }
                                if (!isInternalUri && (launchActivity = LaunchActivity.instance) != null) {
                                    openAsInternalIntent(launchActivity, uri2.toString(), z3, z6, progress);
                                    return;
                                }
                                if (z9) {
                                    openInExternalBrowser(context, uri2.toString(), z4, str2);
                                    return;
                                }
                                if (openInExternalApp(context, uri2.toString(), z4)) {
                                    return;
                                }
                                if (uri2.getScheme() != null && uri2.getScheme().equalsIgnoreCase("intent")) {
                                    String stringExtra = Intent.parseUri(uri2.toString(), 1).getStringExtra("browser_fallback_url");
                                    if (!TextUtils.isEmpty(stringExtra)) {
                                        uri2 = Uri.parse(stringExtra);
                                    }
                                }
                                openInTelegramBrowser(context, uri2.toString(), progress);
                                return;
                            }
                        }
                    }
                } catch (Exception e5) {
                    FileLog.e(e5);
                    return;
                }
            }
            if (!isTonsite(uri2.toString())) {
                z9 = false;
                if (uri2.getScheme() != null) {
                }
                if (!isInternalUri) {
                }
                if (z9) {
                }
            }
            z9 = true;
            if (uri2.getScheme() != null) {
            }
            if (!isInternalUri) {
            }
            if (z9) {
            }
        }
        if (MessagesController.getInstance(i).authDomains.contains(hostAuthority)) {
            Intent intent = new Intent("android.intent.action.VIEW", uri2);
            intent.addFlags(268435456);
            ApplicationLoader.applicationContext.startActivity(intent);
            return;
        }
        Intent intent2 = new Intent(ApplicationLoader.applicationContext, (Class<?>) ShareBroadcastReceiver.class);
        intent2.setAction("android.intent.action.SEND");
        PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, new Intent(ApplicationLoader.applicationContext, (Class<?>) CustomTabsCopyReceiver.class), 167772160);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.addMenuItem(LocaleController.getString(R.string.CopyLink), broadcast);
        builder.setToolbarColor(Theme.getColor(Theme.key_actionBarBrowser));
        builder.setShowTitle(true);
        try {
            builder.setActionButton(BitmapFactory.decodeResource(context.getResources(), R.drawable.msg_filled_shareout), LocaleController.getString(R.string.ShareFile), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, intent2, ConnectionsManager.FileTypeVideo), true);
            CustomTabsIntent build = builder.build();
            build.setUseNewTask();
            build.launchUrl(context, uri2);
        } catch (Exception e6) {
            e = e6;
            FileLog.e(e);
            if (z5) {
            }
            if (!isTonsite(uri2.toString())) {
            }
            z9 = true;
            if (uri2.getScheme() != null) {
            }
            if (!isInternalUri) {
            }
            if (z9) {
            }
        }
    }

    public static void openUrl(Context context, String str) {
        if (str == null) {
            return;
        }
        openUrl(context, Uri.parse(str), true);
    }

    public static void openUrl(Context context, String str, boolean z) {
        if (context == null || str == null) {
            return;
        }
        openUrl(context, Uri.parse(str), z);
    }

    public static void openUrl(Context context, String str, boolean z, boolean z2) {
        openUrl(context, Uri.parse(str), z, z2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x002e, code lost:
    
        if (android.text.TextUtils.isEmpty(r4) == false) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String replace(Uri uri, String str, String str2, String str3, String str4) {
        StringBuilder sb = new StringBuilder();
        if (str == null) {
            str = uri.getScheme();
        }
        if (str != null) {
            sb.append(str);
            sb.append("://");
        }
        if (str2 == null) {
            if (uri.getUserInfo() != null) {
                str2 = uri.getUserInfo();
                sb.append(str2);
                sb.append("@");
            }
        }
        if (str3 != null) {
            sb.append(str3);
        } else if (uri.getHost() != null) {
            sb.append(uri.getHost());
        }
        if (uri.getPort() != -1) {
            sb.append(":");
            sb.append(uri.getPort());
        }
        if (str4 != null) {
            sb.append(str4);
        } else {
            sb.append(uri.getPath());
        }
        if (uri.getQuery() != null) {
            sb.append("?");
            sb.append(uri.getQuery());
        }
        if (uri.getFragment() != null) {
            sb.append("#");
            sb.append(uri.getFragment());
        }
        return sb.toString();
    }

    public static String replaceHostname(Uri uri, String str, String str2) {
        return replace(uri, str2, null, str, null);
    }

    private static void setCurrentSession(CustomTabsSession customTabsSession2) {
        customTabsCurrentSession = new WeakReference(customTabsSession2);
    }

    public static void unbindCustomTabsService(Activity activity) {
        if (customTabsServiceConnection == null) {
            return;
        }
        WeakReference weakReference = currentCustomTabsActivity;
        if ((weakReference == null ? null : (Activity) weakReference.get()) == activity) {
            currentCustomTabsActivity.clear();
        }
        try {
            activity.unbindService(customTabsServiceConnection);
        } catch (Exception unused) {
        }
        customTabsClient = null;
        customTabsSession = null;
    }

    public static boolean urlMustNotHaveConfirmation(String str) {
        return isTelegraphUrl(str, false, true) || str.matches("^(https://)?t\\.me/iv\\??(/.*|$)") || str.matches("^(https://)?telegram\\.org/(blog|tour)(/.*|$)") || str.matches("^(https://)?fragment\\.com(/.*|$)");
    }
}
