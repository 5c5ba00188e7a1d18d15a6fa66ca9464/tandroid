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
import java.util.regex.Matcher;
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
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_messages_getWebPagePreview;
import org.telegram.tgnet.TLRPC$TL_webPage;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes3.dex */
public class Browser {
    private static WeakReference<Activity> currentCustomTabsActivity;
    private static CustomTabsClient customTabsClient;
    private static String customTabsPackageToBind;
    private static CustomTabsServiceConnection customTabsServiceConnection;
    private static CustomTabsSession customTabsSession;

    private static void setCurrentSession(CustomTabsSession customTabsSession2) {
        new WeakReference(customTabsSession2);
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
        Activity activity2 = weakReference == null ? null : weakReference.get();
        if (activity2 != null && activity2 != activity) {
            unbindCustomTabsService(activity2);
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

    public static void unbindCustomTabsService(Activity activity) {
        if (customTabsServiceConnection == null) {
            return;
        }
        WeakReference<Activity> weakReference = currentCustomTabsActivity;
        if ((weakReference == null ? null : weakReference.get()) == activity) {
            currentCustomTabsActivity.clear();
        }
        try {
            activity.unbindService(customTabsServiceConnection);
        } catch (Exception unused) {
        }
        customTabsClient = null;
        customTabsSession = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class NavigationCallback extends CustomTabsCallback {
        @Override // org.telegram.messenger.support.customtabs.CustomTabsCallback
        public void onNavigationEvent(int i, Bundle bundle) {
        }

        private NavigationCallback() {
        }
    }

    public static void openUrl(Context context, String str) {
        if (str == null) {
            return;
        }
        openUrl(context, Uri.parse(str), true);
    }

    public static void openUrl(Context context, Uri uri) {
        openUrl(context, uri, true);
    }

    public static void openUrl(Context context, String str, boolean z) {
        if (context == null || str == null) {
            return;
        }
        openUrl(context, Uri.parse(str), z);
    }

    public static void openUrl(Context context, Uri uri, boolean z) {
        openUrl(context, uri, z, true);
    }

    public static void openUrl(Context context, String str, boolean z, boolean z2) {
        openUrl(context, Uri.parse(str), z, z2);
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

    public static String extractUsername(String str) {
        if (str != null && !TextUtils.isEmpty(str)) {
            if (str.startsWith("@")) {
                return str.substring(1);
            }
            if (str.startsWith("t.me/")) {
                return str.substring(5);
            }
            if (str.startsWith("http://t.me/")) {
                return str.substring(12);
            }
            if (str.startsWith("https://t.me/")) {
                return str.substring(13);
            }
            Matcher matcher = LaunchActivity.PREFIX_T_ME_PATTERN.matcher(str);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    public static boolean urlMustNotHaveConfirmation(String str) {
        return isTelegraphUrl(str, false, true) || str.matches("^(https://)?t\\.me/iv\\??(/.*|$)") || str.matches("^(https://)?telegram\\.org/(blog|tour)(/.*|$)") || str.matches("^(https://)?fragment\\.com(/.*|$)");
    }

    /* loaded from: classes3.dex */
    public static class Progress {
        private Runnable onCancelListener;

        public void end(boolean z) {
            throw null;
        }

        public void init() {
            throw null;
        }

        public void end() {
            end(false);
        }

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

        public void onCancel(Runnable runnable) {
            this.onCancelListener = runnable;
        }
    }

    public static void openUrl(Context context, Uri uri, boolean z, boolean z2) {
        openUrl(context, uri, z, z2, false, null);
    }

    public static void openUrl(Context context, Uri uri, boolean z, boolean z2, Progress progress) {
        openUrl(context, uri, z, z2, false, progress);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(15:39|40|41|(11:45|46|(5:50|(2:52|53)(1:55)|54|47|48)|56|57|(3:59|(4:62|(2:63|(1:1)(2:65|(3:68|69|70)(1:67)))|71|60)|73)(3:96|(4:99|(2:105|106)(1:103)|104|97)|107)|74|(3:76|(3:79|80|77)|81)|83|84|(2:89|(2:91|92)(2:94|95)))|112|56|57|(0)(0)|74|(0)|83|84|(0)|89|(0)(0)) */
    /* JADX WARN: Can't wrap try/catch for region: R(20:4|(3:142|143|(5:155|156|157|158|(2:160|161)(2:162|163)))|6|7|8|(1:10)(1:139)|11|(12:131|132|133|16|17|(9:19|(1:21)|22|23|(1:25)|26|(1:28)(1:32)|(1:30)|31)|(15:39|40|41|(11:45|46|(5:50|(2:52|53)(1:55)|54|47|48)|56|57|(3:59|(4:62|(2:63|(1:1)(2:65|(3:68|69|70)(1:67)))|71|60)|73)(3:96|(4:99|(2:105|106)(1:103)|104|97)|107)|74|(3:76|(3:79|80|77)|81)|83|84|(2:89|(2:91|92)(2:94|95)))|112|56|57|(0)(0)|74|(0)|83|84|(0)|89|(0)(0))|114|115|(1:117)|118|(2:124|125)(2:122|123))|15|16|17|(0)|(17:34|37|39|40|41|(14:43|45|46|(2:47|48)|56|57|(0)(0)|74|(0)|83|84|(0)|89|(0)(0))|112|56|57|(0)(0)|74|(0)|83|84|(0)|89|(0)(0))|114|115|(0)|118|(1:120)|124|125) */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x0355, code lost:
        r0 = e;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x028d  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x02c6 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x02da A[Catch: Exception -> 0x0355, TryCatch #4 {Exception -> 0x0355, blocks: (B:42:0x00e4, B:44:0x0100, B:46:0x012f, B:47:0x0135, B:51:0x013e, B:52:0x0156, B:54:0x015e, B:57:0x0189, B:58:0x019b, B:55:0x0173, B:60:0x01b2, B:63:0x01b8, B:109:0x02c2, B:112:0x02c8, B:114:0x02ce, B:116:0x02da, B:118:0x02ea), top: B:150:0x00e4 }] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x02ea A[Catch: Exception -> 0x0355, TRY_LEAVE, TryCatch #4 {Exception -> 0x0355, blocks: (B:42:0x00e4, B:44:0x0100, B:46:0x012f, B:47:0x0135, B:51:0x013e, B:52:0x0156, B:54:0x015e, B:57:0x0189, B:58:0x019b, B:55:0x0173, B:60:0x01b2, B:63:0x01b8, B:109:0x02c2, B:112:0x02c8, B:114:0x02ce, B:116:0x02da, B:118:0x02ea), top: B:150:0x00e4 }] */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0364 A[Catch: Exception -> 0x039d, TryCatch #1 {Exception -> 0x039d, blocks: (B:125:0x035d, B:127:0x0364, B:128:0x0376, B:130:0x0387, B:132:0x038b, B:133:0x0399), top: B:145:0x035d }] */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0387 A[Catch: Exception -> 0x039d, TryCatch #1 {Exception -> 0x039d, blocks: (B:125:0x035d, B:127:0x0364, B:128:0x0376, B:130:0x0387, B:132:0x038b, B:133:0x0399), top: B:145:0x035d }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0100 A[Catch: Exception -> 0x0355, TryCatch #4 {Exception -> 0x0355, blocks: (B:42:0x00e4, B:44:0x0100, B:46:0x012f, B:47:0x0135, B:51:0x013e, B:52:0x0156, B:54:0x015e, B:57:0x0189, B:58:0x019b, B:55:0x0173, B:60:0x01b2, B:63:0x01b8, B:109:0x02c2, B:112:0x02c8, B:114:0x02ce, B:116:0x02da, B:118:0x02ea), top: B:150:0x00e4 }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x01eb A[Catch: Exception -> 0x0215, TryCatch #3 {Exception -> 0x0215, blocks: (B:72:0x01e5, B:74:0x01eb, B:76:0x01fb), top: B:148:0x01e5 }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x024e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void openUrl(final Context context, final Uri uri, final boolean z, boolean z2, boolean z3, final Progress progress) {
        Uri uri2;
        Intent intent;
        String lowerCase;
        String hostAuthority;
        String[] strArr;
        List<ResolveInfo> queryIntentActivities;
        int i;
        if (context == null || uri == null) {
            return;
        }
        final int i2 = UserConfig.selectedAccount;
        boolean[] zArr = {false};
        boolean isInternalUri = isInternalUri(uri, zArr);
        if (z2) {
            try {
                String hostAuthority2 = AndroidUtilities.getHostAuthority(uri);
                if (UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser() != null && (isTelegraphUrl(hostAuthority2, true) || ("telegram.org".equalsIgnoreCase(hostAuthority2) && (uri.toString().toLowerCase().contains("telegram.org/faq") || uri.toString().toLowerCase().contains("telegram.org/privacy") || uri.toString().toLowerCase().contains("telegram.org/blog"))))) {
                    final AlertDialog[] alertDialogArr = {new AlertDialog(context, 3)};
                    TLRPC$TL_messages_getWebPagePreview tLRPC$TL_messages_getWebPagePreview = new TLRPC$TL_messages_getWebPagePreview();
                    tLRPC$TL_messages_getWebPagePreview.message = uri.toString();
                    try {
                        final int sendRequest = ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tLRPC$TL_messages_getWebPagePreview, new RequestDelegate() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda3
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                Browser.lambda$openUrl$1(Browser.Progress.this, alertDialogArr, i2, uri, context, z, tLObject, tLRPC$TL_error);
                            }
                        });
                        if (progress != null) {
                            progress.init();
                            return;
                        } else {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda2
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
        try {
            try {
                lowerCase = uri.getScheme() != null ? uri.getScheme().toLowerCase() : "";
            } catch (Exception e) {
                e = e;
                uri2 = uri;
                FileLog.e(e);
                intent = new Intent("android.intent.action.VIEW", uri2);
                if (isInternalUri) {
                }
                intent.putExtra("create_new_tab", true);
                intent.putExtra("com.android.browser.application_id", context.getPackageName());
                if (!isInternalUri) {
                }
                context.startActivity(intent);
                return;
            }
            if ("http".equals(lowerCase) || "https".equals(lowerCase)) {
                try {
                    uri2 = uri.normalizeScheme();
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                hostAuthority = AndroidUtilities.getHostAuthority(uri2.toString().toLowerCase());
                if (AccountInstance.getInstance(i2).getMessagesController().autologinDomains.contains(hostAuthority)) {
                    String str = "autologin_token=" + URLEncoder.encode(AccountInstance.getInstance(UserConfig.selectedAccount).getMessagesController().autologinToken, "UTF-8");
                    String uri3 = uri2.toString();
                    int indexOf = uri3.indexOf("://");
                    if (indexOf >= 0) {
                        uri3 = uri3.substring(indexOf + 3);
                    }
                    String encodedFragment = uri2.getEncodedFragment();
                    if (encodedFragment != null) {
                        uri3 = uri3.substring(0, uri3.indexOf("#" + encodedFragment));
                    }
                    String str2 = uri3.indexOf(63) >= 0 ? uri3 + "&" + str : uri3 + "?" + str;
                    if (encodedFragment != null) {
                        str2 = str2 + "#" + encodedFragment;
                    }
                    uri2 = Uri.parse("https://" + str2);
                }
                if (z && SharedConfig.customTabs && !isInternalUri && !lowerCase.equals("tel")) {
                    List<ResolveInfo> list = null;
                    try {
                        queryIntentActivities = context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com")), 0);
                    } catch (Exception unused3) {
                    }
                    if (queryIntentActivities != null && !queryIntentActivities.isEmpty()) {
                        strArr = new String[queryIntentActivities.size()];
                        for (i = 0; i < queryIntentActivities.size(); i++) {
                            try {
                                strArr[i] = queryIntentActivities.get(i).activityInfo.packageName;
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("default browser name = " + strArr[i]);
                                }
                            } catch (Exception unused4) {
                            }
                        }
                        list = context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", uri2), 0);
                        if (strArr == null) {
                            int i3 = 0;
                            while (i3 < list.size()) {
                                int i4 = 0;
                                while (true) {
                                    if (i4 >= strArr.length) {
                                        break;
                                    } else if (strArr[i4].equals(list.get(i3).activityInfo.packageName)) {
                                        list.remove(i3);
                                        i3--;
                                        break;
                                    } else {
                                        i4++;
                                    }
                                }
                                i3++;
                            }
                        } else {
                            int i5 = 0;
                            while (i5 < list.size()) {
                                if (list.get(i5).activityInfo.packageName.toLowerCase().contains("browser") || list.get(i5).activityInfo.packageName.toLowerCase().contains("chrome")) {
                                    list.remove(i5);
                                    i5--;
                                }
                                i5++;
                            }
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            for (int i6 = 0; i6 < list.size(); i6++) {
                                FileLog.d("device has " + list.get(i6).activityInfo.packageName + " to open " + uri2.toString());
                            }
                        }
                        if (!zArr[0] || list == null || list.isEmpty()) {
                            if (!MessagesController.getInstance(i2).authDomains.contains(hostAuthority)) {
                                Intent intent2 = new Intent("android.intent.action.VIEW", uri2);
                                intent2.addFlags(268435456);
                                ApplicationLoader.applicationContext.startActivity(intent2);
                                return;
                            }
                            Intent intent3 = new Intent(ApplicationLoader.applicationContext, ShareBroadcastReceiver.class);
                            intent3.setAction("android.intent.action.SEND");
                            PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, new Intent(ApplicationLoader.applicationContext, CustomTabsCopyReceiver.class), 167772160);
                            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
                            builder.addMenuItem(LocaleController.getString("CopyLink", R.string.CopyLink), broadcast);
                            builder.setToolbarColor(Theme.getColor(Theme.key_actionBarBrowser));
                            builder.setShowTitle(true);
                            builder.setActionButton(BitmapFactory.decodeResource(context.getResources(), R.drawable.msg_filled_shareout), LocaleController.getString("ShareFile", R.string.ShareFile), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, intent3, ConnectionsManager.FileTypeVideo), true);
                            CustomTabsIntent build = builder.build();
                            build.setUseNewTask();
                            build.launchUrl(context, uri2);
                            return;
                        }
                    }
                    strArr = null;
                    list = context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", uri2), 0);
                    if (strArr == null) {
                    }
                    if (BuildVars.LOGS_ENABLED) {
                    }
                    if (!zArr[0]) {
                    }
                    if (!MessagesController.getInstance(i2).authDomains.contains(hostAuthority)) {
                    }
                }
                intent = new Intent("android.intent.action.VIEW", uri2);
                if (isInternalUri) {
                    intent.setComponent(new ComponentName(context.getPackageName(), LaunchActivity.class.getName()));
                }
                intent.putExtra("create_new_tab", true);
                intent.putExtra("com.android.browser.application_id", context.getPackageName());
                if (!isInternalUri && (context instanceof LaunchActivity)) {
                    intent.putExtra("force_not_internal_apps", z3);
                    ((LaunchActivity) context).onNewIntent(intent, progress);
                    return;
                }
                context.startActivity(intent);
                return;
            }
            intent = new Intent("android.intent.action.VIEW", uri2);
            if (isInternalUri) {
            }
            intent.putExtra("create_new_tab", true);
            intent.putExtra("com.android.browser.application_id", context.getPackageName());
            if (!isInternalUri) {
            }
            context.startActivity(intent);
            return;
        } catch (Exception e3) {
            FileLog.e(e3);
            return;
        }
        uri2 = uri;
        hostAuthority = AndroidUtilities.getHostAuthority(uri2.toString().toLowerCase());
        if (AccountInstance.getInstance(i2).getMessagesController().autologinDomains.contains(hostAuthority)) {
        }
        if (z) {
            List<ResolveInfo> list2 = null;
            queryIntentActivities = context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse("http://www.google.com")), 0);
            if (queryIntentActivities != null) {
                strArr = new String[queryIntentActivities.size()];
                while (i < queryIntentActivities.size()) {
                }
                list2 = context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", uri2), 0);
                if (strArr == null) {
                }
                if (BuildVars.LOGS_ENABLED) {
                }
                if (!zArr[0]) {
                }
                if (!MessagesController.getInstance(i2).authDomains.contains(hostAuthority)) {
                }
            }
            strArr = null;
            list2 = context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", uri2), 0);
            if (strArr == null) {
            }
            if (BuildVars.LOGS_ENABLED) {
            }
            if (!zArr[0]) {
            }
            if (!MessagesController.getInstance(i2).authDomains.contains(hostAuthority)) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openUrl$1(final Progress progress, final AlertDialog[] alertDialogArr, final int i, final Uri uri, final Context context, final boolean z, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Browser.lambda$openUrl$0(Browser.Progress.this, alertDialogArr, tLObject, i, uri, context, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:16:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
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
        boolean z2 = true;
        if (tLObject instanceof TLRPC$TL_messageMediaWebPage) {
            TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage = (TLRPC$TL_messageMediaWebPage) tLObject;
            TLRPC$WebPage tLRPC$WebPage = tLRPC$TL_messageMediaWebPage.webpage;
            if ((tLRPC$WebPage instanceof TLRPC$TL_webPage) && tLRPC$WebPage.cached_page != null) {
                NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.openArticle, tLRPC$TL_messageMediaWebPage.webpage, uri.toString());
                if (z2) {
                    openUrl(context, uri, z, false);
                    return;
                }
                return;
            }
        }
        z2 = false;
        if (z2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openUrl$3(AlertDialog[] alertDialogArr, final int i) {
        if (alertDialogArr[0] == null) {
            return;
        }
        try {
            alertDialogArr[0].setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    Browser.lambda$openUrl$2(i, dialogInterface);
                }
            });
            alertDialogArr[0].show();
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$openUrl$2(int i, DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(UserConfig.selectedAccount).cancelRequest(i, true);
    }

    public static boolean isInternalUrl(String str, boolean[] zArr) {
        return isInternalUri(Uri.parse(str), false, zArr);
    }

    public static boolean isInternalUrl(String str, boolean z, boolean[] zArr) {
        return isInternalUri(Uri.parse(str), z, zArr);
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

    public static boolean isTMe(String str) {
        try {
            return TextUtils.equals(AndroidUtilities.getHostAuthority(str), MessagesController.getInstance(UserConfig.selectedAccount).linkPrefix);
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    public static boolean isInternalUri(Uri uri, boolean[] zArr) {
        return isInternalUri(uri, false, zArr);
    }

    public static boolean isInternalUri(Uri uri, boolean z, boolean[] zArr) {
        String str;
        String str2;
        String hostAuthority = AndroidUtilities.getHostAuthority(uri);
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
            lowerCase = host != null ? host.toLowerCase() : "";
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
        } else if ("tg".equals(uri.getScheme())) {
            return true;
        } else {
            if ("telegram.dog".equals(lowerCase)) {
                String path = uri.getPath();
                if (path != null && path.length() > 1) {
                    if (z) {
                        return true;
                    }
                    String lowerCase2 = path.substring(1).toLowerCase();
                    if (lowerCase2.startsWith("blog") || lowerCase2.equals("iv") || lowerCase2.startsWith("faq") || lowerCase2.equals("apps") || lowerCase2.startsWith("s/")) {
                        if (zArr != null) {
                            zArr[0] = true;
                        }
                        return false;
                    }
                    return true;
                }
            } else if ("telegram.me".equals(lowerCase) || "t.me".equals(lowerCase)) {
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
            } else if ("telegram.org".equals(lowerCase) && uri.getPath() != null && uri.getPath().startsWith("/blog/")) {
                return true;
            } else {
                if (z && (lowerCase.endsWith("telegram.org") || lowerCase.endsWith("telegra.ph") || lowerCase.endsWith("telesco.pe"))) {
                    return true;
                }
            }
            return false;
        }
    }

    public static String replaceHostname(Uri uri, String str) {
        String scheme = uri.getScheme();
        String userInfo = uri.getUserInfo();
        int port = uri.getPort();
        String path = uri.getPath();
        String query = uri.getQuery();
        String fragment = uri.getFragment();
        StringBuilder sb = new StringBuilder();
        sb.append(scheme);
        sb.append("://");
        if (userInfo != null) {
            sb.append(userInfo);
            sb.append("@");
        }
        sb.append(str);
        if (port != -1) {
            sb.append(":");
            sb.append(port);
        }
        sb.append(path);
        if (query != null) {
            sb.append("?");
            sb.append(query);
        }
        if (fragment != null) {
            sb.append("#");
            sb.append(fragment);
        }
        return sb.toString();
    }
}
