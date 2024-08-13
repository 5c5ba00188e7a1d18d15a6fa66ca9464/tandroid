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
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_messages_getWebPagePreview;
import org.telegram.tgnet.TLRPC$TL_webPage;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheetTabs;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.web.RestrictedDomainsList;
/* loaded from: classes3.dex */
public class Browser {
    private static WeakReference<Activity> currentCustomTabsActivity;
    private static CustomTabsClient customTabsClient;
    private static String customTabsPackageToBind;
    private static CustomTabsServiceConnection customTabsServiceConnection;
    private static CustomTabsSession customTabsSession;
    private static Pattern domainPattern;

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
        private Runnable onEndListener;

        public void init() {
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

        public void onEnd(Runnable runnable) {
            this.onEndListener = runnable;
        }
    }

    public static void openUrl(Context context, Uri uri, boolean z, boolean z2) {
        openUrl(context, uri, z, z2, false, null, null, false);
    }

    public static void openUrl(Context context, Uri uri, boolean z, boolean z2, Progress progress) {
        openUrl(context, uri, z, z2, false, progress, null, false);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(15:4|(1:6)(1:138)|7|(3:113|114|(5:126|127|128|129|(2:131|132)(2:133|134)))|9|10|11|(1:13)(1:110)|14|(10:(15:102|103|104|19|20|(9:22|(1:28)|29|30|(1:32)|33|(1:35)(1:39)|(1:37)|38)|(2:56|(2:58|59)(2:61|62))|63|64|(1:96)(2:68|(4:70|(1:78)|79|(1:(2:86|(2:88|89)(1:90))(2:91|92))(2:83|84)))|93|(3:95|(1:81)|(0)(0))|79|(0)|(0)(0))|63|64|(1:66)|96|93|(0)|79|(0)|(0)(0))|18|19|20|(0)|(7:41|43|46|48|50|56|(0)(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0294, code lost:
        r0 = e;
     */
    /* JADX WARN: Removed duplicated region for block: B:113:0x02ea  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x02ed A[Catch: Exception -> 0x0322, TryCatch #5 {Exception -> 0x0322, blocks: (B:94:0x029c, B:96:0x02a0, B:98:0x02a6, B:100:0x02b5, B:102:0x02bb, B:104:0x02c5, B:106:0x02cf, B:115:0x02ed, B:117:0x02f1, B:119:0x0302, B:121:0x030e, B:122:0x0316, B:110:0x02de), top: B:137:0x029c }] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0302 A[Catch: Exception -> 0x0322, TryCatch #5 {Exception -> 0x0322, blocks: (B:94:0x029c, B:96:0x02a0, B:98:0x02a6, B:100:0x02b5, B:102:0x02bb, B:104:0x02c5, B:106:0x02cf, B:115:0x02ed, B:117:0x02f1, B:119:0x0302, B:121:0x030e, B:122:0x0316, B:110:0x02de), top: B:137:0x029c }] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0316 A[Catch: Exception -> 0x0322, TRY_LEAVE, TryCatch #5 {Exception -> 0x0322, blocks: (B:94:0x029c, B:96:0x02a0, B:98:0x02a6, B:100:0x02b5, B:102:0x02bb, B:104:0x02c5, B:106:0x02cf, B:115:0x02ed, B:117:0x02f1, B:119:0x0302, B:121:0x030e, B:122:0x0316, B:110:0x02de), top: B:137:0x029c }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x011a A[Catch: Exception -> 0x0294, TryCatch #3 {Exception -> 0x0294, blocks: (B:46:0x00fe, B:48:0x011a, B:52:0x014c, B:54:0x0158, B:55:0x015e, B:59:0x0167, B:60:0x017e, B:62:0x0186, B:65:0x01b1, B:66:0x01c3, B:63:0x019b, B:68:0x01da, B:70:0x01de, B:73:0x01e4, B:75:0x01ed, B:77:0x01f7, B:79:0x01fb, B:81:0x0205, B:83:0x020f, B:85:0x021b, B:87:0x022d), top: B:135:0x00fe }] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x021b A[Catch: Exception -> 0x0294, TryCatch #3 {Exception -> 0x0294, blocks: (B:46:0x00fe, B:48:0x011a, B:52:0x014c, B:54:0x0158, B:55:0x015e, B:59:0x0167, B:60:0x017e, B:62:0x0186, B:65:0x01b1, B:66:0x01c3, B:63:0x019b, B:68:0x01da, B:70:0x01de, B:73:0x01e4, B:75:0x01ed, B:77:0x01f7, B:79:0x01fb, B:81:0x0205, B:83:0x020f, B:85:0x021b, B:87:0x022d), top: B:135:0x00fe }] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x022d A[Catch: Exception -> 0x0294, TRY_LEAVE, TryCatch #3 {Exception -> 0x0294, blocks: (B:46:0x00fe, B:48:0x011a, B:52:0x014c, B:54:0x0158, B:55:0x015e, B:59:0x0167, B:60:0x017e, B:62:0x0186, B:65:0x01b1, B:66:0x01c3, B:63:0x019b, B:68:0x01da, B:70:0x01de, B:73:0x01e4, B:75:0x01ed, B:77:0x01f7, B:79:0x01fb, B:81:0x0205, B:83:0x020f, B:85:0x021b, B:87:0x022d), top: B:135:0x00fe }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x02a0 A[Catch: Exception -> 0x0322, TryCatch #5 {Exception -> 0x0322, blocks: (B:94:0x029c, B:96:0x02a0, B:98:0x02a6, B:100:0x02b5, B:102:0x02bb, B:104:0x02c5, B:106:0x02cf, B:115:0x02ed, B:117:0x02f1, B:119:0x0302, B:121:0x030e, B:122:0x0316, B:110:0x02de), top: B:137:0x029c }] */
    /* JADX WARN: Type inference failed for: r11v1 */
    /* JADX WARN: Type inference failed for: r11v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r11v3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void openUrl(final Context context, final Uri uri, boolean z, boolean z2, boolean z3, final Progress progress, String str, boolean z4) {
        boolean z5;
        boolean z6;
        String str2;
        ?? r11;
        boolean[] zArr;
        Uri uri2;
        boolean z7;
        String lowerCase;
        String hostAuthority;
        String str3;
        if (context == null || uri == null) {
            return;
        }
        final int i = UserConfig.selectedAccount;
        boolean[] zArr2 = {false};
        boolean isInternalUri = isInternalUri(uri, zArr2);
        String browserPackageName = getBrowserPackageName(str);
        if (browserPackageName != null) {
            z6 = false;
            z5 = false;
        } else {
            z5 = z;
            z6 = z2;
        }
        if (z6) {
            try {
                String hostAuthority2 = AndroidUtilities.getHostAuthority(uri);
                if (UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser() != null && (isTelegraphUrl(hostAuthority2, true) || ("telegram.org".equalsIgnoreCase(hostAuthority2) && (uri.toString().toLowerCase().contains("telegram.org/faq") || uri.toString().toLowerCase().contains("telegram.org/privacy") || uri.toString().toLowerCase().contains("telegram.org/blog"))))) {
                    final AlertDialog[] alertDialogArr = {new AlertDialog(context, 3)};
                    TLRPC$TL_messages_getWebPagePreview tLRPC$TL_messages_getWebPagePreview = new TLRPC$TL_messages_getWebPagePreview();
                    tLRPC$TL_messages_getWebPagePreview.message = uri.toString();
                    str2 = browserPackageName;
                    r11 = 0;
                    zArr = zArr2;
                    final boolean z8 = z5;
                    try {
                        final int sendRequest = ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tLRPC$TL_messages_getWebPagePreview, new RequestDelegate() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda3
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                Browser.lambda$openUrl$1(Browser.Progress.this, alertDialogArr, i, uri, context, z8, tLObject, tLRPC$TL_error);
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
        str2 = browserPackageName;
        zArr = zArr2;
        r11 = 0;
        try {
            lowerCase = uri.getScheme() != null ? uri.getScheme().toLowerCase() : "";
        } catch (Exception e) {
            e = e;
            uri2 = uri;
            FileLog.e(e);
            if (!SharedConfig.inappBrowser) {
            }
            if (!isTonsite(uri2.toString())) {
            }
            z7 = true;
            if (!isInternalUri) {
            }
            if (!z7) {
            }
        }
        try {
            if ("http".equals(lowerCase) || "https".equals(lowerCase)) {
                try {
                    uri2 = uri.normalizeScheme();
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
                hostAuthority = AndroidUtilities.getHostAuthority(uri2.toString().toLowerCase());
                if (AccountInstance.getInstance(i).getMessagesController().autologinDomains.contains(hostAuthority)) {
                    String str4 = "autologin_token=" + URLEncoder.encode(AccountInstance.getInstance(UserConfig.selectedAccount).getMessagesController().autologinToken, "UTF-8");
                    String uri3 = uri2.toString();
                    int indexOf = uri3.indexOf("://");
                    if (indexOf >= 0 && indexOf <= 5 && !uri3.substring(r11, indexOf).contains(".")) {
                        uri3 = uri3.substring(indexOf + 3);
                    }
                    String encodedFragment = uri2.getEncodedFragment();
                    if (encodedFragment != null) {
                        uri3 = uri3.substring(r11, uri3.indexOf("#" + encodedFragment));
                    }
                    if (uri3.indexOf(63) >= 0) {
                        str3 = uri3 + "&" + str4;
                    } else {
                        str3 = uri3 + "?" + str4;
                    }
                    if (encodedFragment != null) {
                        str3 = str3 + "#" + encodedFragment;
                    }
                    uri2 = Uri.parse("https://" + str3);
                }
                if (z5 && !SharedConfig.inappBrowser && SharedConfig.customTabs && !isInternalUri && !lowerCase.equals("tel") && !isTonsite(uri2.toString()) && (zArr[r11] || !openInExternalApp(context, uri2.toString(), r11) || !hasAppToOpen(context, uri2.toString()))) {
                    if (!MessagesController.getInstance(i).authDomains.contains(hostAuthority)) {
                        Intent intent = new Intent("android.intent.action.VIEW", uri2);
                        intent.addFlags(268435456);
                        ApplicationLoader.applicationContext.startActivity(intent);
                        return;
                    }
                    Intent intent2 = new Intent(ApplicationLoader.applicationContext, ShareBroadcastReceiver.class);
                    intent2.setAction("android.intent.action.SEND");
                    PendingIntent broadcast = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, r11, new Intent(ApplicationLoader.applicationContext, CustomTabsCopyReceiver.class), 167772160);
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
                    builder.addMenuItem(LocaleController.getString(R.string.CopyLink), broadcast);
                    builder.setToolbarColor(Theme.getColor(Theme.key_actionBarBrowser));
                    builder.setShowTitle(true);
                    builder.setActionButton(BitmapFactory.decodeResource(context.getResources(), R.drawable.msg_filled_shareout), LocaleController.getString("ShareFile", R.string.ShareFile), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, r11, intent2, ConnectionsManager.FileTypeVideo), true);
                    CustomTabsIntent build = builder.build();
                    build.setUseNewTask();
                    build.launchUrl(context, uri2);
                    return;
                }
                if (!SharedConfig.inappBrowser && TextUtils.isEmpty(str2)) {
                    if (!RestrictedDomainsList.getInstance().isRestricted(AndroidUtilities.getHostAuthority(uri2, true))) {
                        if (uri2.getScheme() != null && !"https".equals(uri2.getScheme()) && !"http".equals(uri2.getScheme()) && !"tonsite".equals(uri2.getScheme())) {
                        }
                        z7 = true;
                        if (!isInternalUri && LaunchActivity.instance != null) {
                            LaunchActivity.dismissAllWeb();
                            openAsInternalIntent(LaunchActivity.instance, uri2.toString(), z3, progress);
                            return;
                        } else if (!z7) {
                            if (openInExternalApp(context, uri2.toString(), z4)) {
                                return;
                            }
                            openInTelegramBrowser(context, uri2.toString(), progress);
                            return;
                        } else {
                            openInExternalBrowser(context, uri2.toString(), z4, str2);
                            return;
                        }
                    }
                }
                if (!isTonsite(uri2.toString())) {
                    z7 = false;
                    if (!isInternalUri) {
                    }
                    if (!z7) {
                    }
                }
                z7 = true;
                if (!isInternalUri) {
                }
                if (!z7) {
                }
            }
            if (!SharedConfig.inappBrowser) {
            }
            if (!isTonsite(uri2.toString())) {
            }
            z7 = true;
            if (!isInternalUri) {
            }
            if (!z7) {
            }
        } catch (Exception e3) {
            FileLog.e(e3);
            return;
        }
        uri2 = uri;
        hostAuthority = AndroidUtilities.getHostAuthority(uri2.toString().toLowerCase());
        if (AccountInstance.getInstance(i).getMessagesController().autologinDomains.contains(hostAuthority)) {
        }
        if (z5) {
            if (!MessagesController.getInstance(i).authDomains.contains(hostAuthority)) {
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

    public static boolean openAsInternalIntent(Context context, String str) {
        return openAsInternalIntent(context, str, false, null);
    }

    public static boolean openAsInternalIntent(Context context, String str, boolean z, Progress progress) {
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
        launchActivity.onNewIntent(intent, progress);
        return true;
    }

    public static boolean openInTelegramBrowser(Context context, String str, Progress progress) {
        BottomSheetTabs bottomSheetTabs;
        LaunchActivity launchActivity = LaunchActivity.instance;
        if (launchActivity == null || (bottomSheetTabs = launchActivity.getBottomSheetTabs()) == null || bottomSheetTabs.tryReopenTab(str) == null) {
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
        return true;
    }

    public static boolean openInExternalBrowser(Context context, String str, boolean z) {
        return openInExternalBrowser(context, str, z, null);
    }

    public static boolean openInExternalBrowser(Context context, String str, boolean z, String str2) {
        Intent intent;
        if (str == null) {
            return false;
        }
        try {
            Uri parse = Uri.parse(str);
            boolean z2 = parse.getScheme() != null && parse.getScheme().equalsIgnoreCase("intent");
            if (!z2 || z) {
                if (z2) {
                    intent = Intent.parseUri(parse.toString(), 1);
                } else {
                    intent = new Intent("android.intent.action.VIEW", parse);
                }
                if (!TextUtils.isEmpty(str2)) {
                    intent.setPackage(str2);
                }
                intent.putExtra("create_new_tab", true);
                intent.putExtra("com.android.browser.application_id", context.getPackageName());
                context.startActivity(intent);
                return true;
            }
            return false;
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    public static boolean isTonsite(String str) {
        String hostAuthority = AndroidUtilities.getHostAuthority(str, true);
        if (hostAuthority == null || !(hostAuthority.endsWith(".ton") || hostAuthority.endsWith(".adnl"))) {
            Uri parse = Uri.parse(str);
            return parse.getScheme() != null && parse.getScheme().equalsIgnoreCase("tonsite");
        }
        return true;
    }

    public static boolean isTonsitePunycode(String str) {
        if (domainPattern == null) {
            domainPattern = Pattern.compile("^[a-zA-Z0-9\\-\\_\\.]+\\.[a-zA-Z0-9\\-\\_]+$");
        }
        String hostAuthority = AndroidUtilities.getHostAuthority(str, true);
        if (hostAuthority != null && (hostAuthority.endsWith(".ton") || hostAuthority.endsWith(".adnl"))) {
            return !domainPattern.matcher(hostAuthority).matches();
        }
        Uri parse = Uri.parse(str);
        if (parse.getScheme() == null || !parse.getScheme().equalsIgnoreCase("tonsite")) {
            return false;
        }
        return !domainPattern.matcher(parse.getScheme()).matches();
    }

    public static boolean openInExternalApp(Context context, String str, boolean z) {
        Intent intent;
        if (str == null) {
            return false;
        }
        try {
            if (!isTonsite(str) && !isInternalUrl(str, null)) {
                Uri parse = Uri.parse(str);
                String replace = replace(parse, parse.getScheme() == null ? "https" : parse.getScheme(), parse.getHost() != null ? parse.getHost().toLowerCase() : parse.getHost(), TextUtils.isEmpty(parse.getPath()) ? "/" : parse.getPath());
                Uri parse2 = Uri.parse(replace);
                boolean z2 = parse2.getScheme() != null && parse2.getScheme().equalsIgnoreCase("intent");
                if (!z2 || z) {
                    if (z2) {
                        intent = Intent.parseUri(parse2.toString(), 1);
                    } else {
                        intent = new Intent("android.intent.action.VIEW", parse2);
                    }
                    if (Build.VERSION.SDK_INT >= 30) {
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.addFlags(268435456);
                        intent.addFlags(1024);
                    } else if (!hasAppToOpen(context, replace)) {
                        return false;
                    }
                    context.startActivity(intent);
                    return true;
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            FileLog.e(e);
            return false;
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(11:5|(2:6|7)|(11:11|12|(5:16|(2:18|19)(1:21)|20|13|14)|22|23|24|25|(3:27|(4:30|(2:31|(1:1)(2:33|(3:36|37|38)(1:35)))|39|28)|41)(3:57|(4:60|(2:62|63)(1:65)|64|58)|66)|42|(3:44|(3:47|48|45)|49)|(1:55)(1:54))|71|23|24|25|(0)(0)|42|(0)|(2:52|55)(1:56)) */
    /* JADX WARN: Removed duplicated region for block: B:22:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00bb  */
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
            if (strArr == null) {
                int i2 = 0;
                while (i2 < list.size()) {
                    int i3 = 0;
                    while (true) {
                        if (i3 >= strArr.length) {
                            break;
                        } else if (strArr[i3].equals(list.get(i2).activityInfo.packageName)) {
                            list.remove(i2);
                            i2--;
                            break;
                        } else {
                            i3++;
                        }
                    }
                    i2++;
                }
            } else {
                int i4 = 0;
                while (i4 < list.size()) {
                    if (isBrowserPackageName(list.get(i4).activityInfo.packageName.toLowerCase())) {
                        list.remove(i4);
                        i4--;
                    }
                    i4++;
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                for (int i5 = 0; i5 < list.size(); i5++) {
                    FileLog.d("device has " + list.get(i5).activityInfo.packageName + " to open " + str);
                }
            }
            return (list == null || list.isEmpty()) ? false : true;
        }
        strArr = null;
        list = context.getPackageManager().queryIntentActivities(new Intent("android.intent.action.VIEW", Uri.parse(str)), 0);
        if (strArr == null) {
        }
        if (BuildVars.LOGS_ENABLED) {
        }
        if (list == null) {
            return false;
        }
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

    public static boolean isBrowserPackageName(String str) {
        return str != null && (str.contains("browser") || str.contains("chrome") || str.contains("firefox") || "com.microsoft.emmx".equals(str) || "com.opera.mini.native".equals(str) || "com.duckduckgo.mobile.android".equals(str) || "com.UCMobile.intl".equals(str));
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

    public static String IDN_toUnicode(String str) {
        try {
            str = IDN.toASCII(str, 1);
        } catch (Exception e) {
            FileLog.e(e);
        }
        if (isPunycodeAllowed(str)) {
            try {
                return IDN.toUnicode(str, 1);
            } catch (Exception e2) {
                FileLog.e(e2);
                return str;
            }
        }
        return str;
    }

    public static String replaceHostname(Uri uri, String str, String str2) {
        return replace(uri, str2, str, null);
    }

    public static String replace(Uri uri, String str, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        if (str == null) {
            str = uri.getScheme();
        }
        if (str != null) {
            sb.append(str);
            sb.append("://");
        }
        if (uri.getUserInfo() != null) {
            sb.append(uri.getUserInfo());
            sb.append("@");
        }
        if (str2 == null) {
            if (uri.getHost() != null) {
                sb.append(uri.getHost());
            }
        } else {
            sb.append(str2);
        }
        if (uri.getPort() != -1) {
            sb.append(":");
            sb.append(uri.getPort());
        }
        if (str3 != null) {
            sb.append(str3);
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
}
