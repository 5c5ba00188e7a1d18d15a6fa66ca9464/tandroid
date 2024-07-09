package org.telegram.messenger.browser;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.support.customtabs.CustomTabsClient;
import org.telegram.messenger.support.customtabs.CustomTabsServiceConnection;
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
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.LaunchActivity;
/* loaded from: classes3.dex */
public class Browser {
    private static WeakReference<Activity> currentCustomTabsActivity;
    private static CustomTabsClient customTabsClient;
    private static String customTabsPackageToBind;
    private static CustomTabsServiceConnection customTabsServiceConnection;

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
        openUrl(context, uri, z, z2, false, null, null);
    }

    public static void openUrl(Context context, Uri uri, boolean z, boolean z2, Progress progress) {
        openUrl(context, uri, z, z2, false, progress, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x00e3, code lost:
        if ("https".equals(r0) != false) goto L100;
     */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0246 A[Catch: Exception -> 0x027a, TryCatch #6 {Exception -> 0x027a, blocks: (B:82:0x01de, B:84:0x01e4, B:89:0x01f5, B:92:0x0208, B:93:0x021a, B:95:0x0220, B:97:0x0225, B:99:0x0229, B:101:0x022f, B:104:0x0246, B:106:0x024a, B:112:0x025e, B:115:0x0264, B:117:0x026a, B:119:0x0276, B:102:0x0235, B:90:0x01ff, B:108:0x0258), top: B:137:0x01de, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x010e A[Catch: Exception -> 0x01d4, TryCatch #8 {Exception -> 0x01d4, blocks: (B:50:0x00f2, B:52:0x010e, B:56:0x0140, B:58:0x014d, B:59:0x0153, B:63:0x015d), top: B:140:0x00f2 }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01d2  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01e4 A[Catch: Exception -> 0x027a, TryCatch #6 {Exception -> 0x027a, blocks: (B:82:0x01de, B:84:0x01e4, B:89:0x01f5, B:92:0x0208, B:93:0x021a, B:95:0x0220, B:97:0x0225, B:99:0x0229, B:101:0x022f, B:104:0x0246, B:106:0x024a, B:112:0x025e, B:115:0x0264, B:117:0x026a, B:119:0x0276, B:102:0x0235, B:90:0x01ff, B:108:0x0258), top: B:137:0x01de, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01f5 A[Catch: Exception -> 0x027a, TryCatch #6 {Exception -> 0x027a, blocks: (B:82:0x01de, B:84:0x01e4, B:89:0x01f5, B:92:0x0208, B:93:0x021a, B:95:0x0220, B:97:0x0225, B:99:0x0229, B:101:0x022f, B:104:0x0246, B:106:0x024a, B:112:0x025e, B:115:0x0264, B:117:0x026a, B:119:0x0276, B:102:0x0235, B:90:0x01ff, B:108:0x0258), top: B:137:0x01de, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01ff A[Catch: Exception -> 0x027a, TryCatch #6 {Exception -> 0x027a, blocks: (B:82:0x01de, B:84:0x01e4, B:89:0x01f5, B:92:0x0208, B:93:0x021a, B:95:0x0220, B:97:0x0225, B:99:0x0229, B:101:0x022f, B:104:0x0246, B:106:0x024a, B:112:0x025e, B:115:0x0264, B:117:0x026a, B:119:0x0276, B:102:0x0235, B:90:0x01ff, B:108:0x0258), top: B:137:0x01de, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0208 A[Catch: Exception -> 0x027a, TryCatch #6 {Exception -> 0x027a, blocks: (B:82:0x01de, B:84:0x01e4, B:89:0x01f5, B:92:0x0208, B:93:0x021a, B:95:0x0220, B:97:0x0225, B:99:0x0229, B:101:0x022f, B:104:0x0246, B:106:0x024a, B:112:0x025e, B:115:0x0264, B:117:0x026a, B:119:0x0276, B:102:0x0235, B:90:0x01ff, B:108:0x0258), top: B:137:0x01de, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0220 A[Catch: Exception -> 0x027a, TryCatch #6 {Exception -> 0x027a, blocks: (B:82:0x01de, B:84:0x01e4, B:89:0x01f5, B:92:0x0208, B:93:0x021a, B:95:0x0220, B:97:0x0225, B:99:0x0229, B:101:0x022f, B:104:0x0246, B:106:0x024a, B:112:0x025e, B:115:0x0264, B:117:0x026a, B:119:0x0276, B:102:0x0235, B:90:0x01ff, B:108:0x0258), top: B:137:0x01de, inners: #3 }] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0225 A[Catch: Exception -> 0x027a, TryCatch #6 {Exception -> 0x027a, blocks: (B:82:0x01de, B:84:0x01e4, B:89:0x01f5, B:92:0x0208, B:93:0x021a, B:95:0x0220, B:97:0x0225, B:99:0x0229, B:101:0x022f, B:104:0x0246, B:106:0x024a, B:112:0x025e, B:115:0x0264, B:117:0x026a, B:119:0x0276, B:102:0x0235, B:90:0x01ff, B:108:0x0258), top: B:137:0x01de, inners: #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void openUrl(final Context context, final Uri uri, boolean z, boolean z2, boolean z3, final Progress progress, String str) {
        final boolean z4;
        boolean z5;
        Uri uri2;
        boolean z6;
        Intent intent;
        BaseFragment safeLastFragment;
        String lowerCase;
        String str2;
        if (context == null || uri == null) {
            return;
        }
        final int i = UserConfig.selectedAccount;
        boolean isInternalUri = isInternalUri(uri, new boolean[]{false});
        String browserPackageName = getBrowserPackageName(str);
        if (browserPackageName != null) {
            z5 = false;
            z4 = false;
        } else {
            z4 = z;
            z5 = z2;
        }
        if (z5) {
            try {
                String hostAuthority = AndroidUtilities.getHostAuthority(uri);
                if (UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser() != null && (isTelegraphUrl(hostAuthority, true) || ("telegram.org".equalsIgnoreCase(hostAuthority) && (uri.toString().toLowerCase().contains("telegram.org/faq") || uri.toString().toLowerCase().contains("telegram.org/privacy") || uri.toString().toLowerCase().contains("telegram.org/blog"))))) {
                    final AlertDialog[] alertDialogArr = {new AlertDialog(context, 3)};
                    TLRPC$TL_messages_getWebPagePreview tLRPC$TL_messages_getWebPagePreview = new TLRPC$TL_messages_getWebPagePreview();
                    tLRPC$TL_messages_getWebPagePreview.message = uri.toString();
                    try {
                        final int sendRequest = ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(tLRPC$TL_messages_getWebPagePreview, new RequestDelegate() { // from class: org.telegram.messenger.browser.Browser$$ExternalSyntheticLambda3
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                Browser.lambda$openUrl$1(Browser.Progress.this, alertDialogArr, i, uri, context, z4, tLObject, tLRPC$TL_error);
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
                if (uri.getScheme() != null) {
                    try {
                        lowerCase = uri.getScheme().toLowerCase();
                    } catch (Exception e) {
                        e = e;
                        uri2 = uri;
                        FileLog.e(e);
                        if (uri2.getScheme() == null) {
                        }
                        if (!z6) {
                        }
                        if (isInternalUri) {
                        }
                        if (!TextUtils.isEmpty(browserPackageName)) {
                        }
                        if (z6) {
                        }
                        intent.putExtra("create_new_tab", true);
                        intent.putExtra("com.android.browser.application_id", context.getPackageName());
                        if (!isInternalUri) {
                        }
                        try {
                            context.startActivity(intent);
                        } catch (Exception e2) {
                            if (SharedConfig.customTabs && !z6 && (safeLastFragment = LaunchActivity.getSafeLastFragment()) != null) {
                                safeLastFragment.createArticleViewer().open(uri2.toString());
                                return;
                            } else {
                                FileLog.e(e2);
                                return;
                            }
                        }
                    }
                } else {
                    lowerCase = "";
                }
                if (!"http".equals(lowerCase)) {
                }
                try {
                    uri2 = uri.normalizeScheme();
                } catch (Exception e3) {
                    FileLog.e(e3);
                    uri2 = uri;
                    if (!AccountInstance.getInstance(i).getMessagesController().autologinDomains.contains(AndroidUtilities.getHostAuthority(uri2.toString().toLowerCase()))) {
                    }
                    if (uri2.getScheme() == null) {
                    }
                    if (!z6) {
                    }
                    if (isInternalUri) {
                    }
                    if (!TextUtils.isEmpty(browserPackageName)) {
                    }
                    if (z6) {
                    }
                    intent.putExtra("create_new_tab", true);
                    intent.putExtra("com.android.browser.application_id", context.getPackageName());
                    if (!isInternalUri) {
                    }
                    context.startActivity(intent);
                }
            } catch (Exception e4) {
                e = e4;
                uri2 = uri;
                FileLog.e(e);
                if (uri2.getScheme() == null) {
                }
                if (!z6) {
                }
                if (isInternalUri) {
                }
                if (!TextUtils.isEmpty(browserPackageName)) {
                }
                if (z6) {
                }
                intent.putExtra("create_new_tab", true);
                intent.putExtra("com.android.browser.application_id", context.getPackageName());
                if (!isInternalUri) {
                }
                context.startActivity(intent);
            }
            try {
                if (!AccountInstance.getInstance(i).getMessagesController().autologinDomains.contains(AndroidUtilities.getHostAuthority(uri2.toString().toLowerCase()))) {
                    String str3 = "autologin_token=" + URLEncoder.encode(AccountInstance.getInstance(UserConfig.selectedAccount).getMessagesController().autologinToken, "UTF-8");
                    String uri3 = uri2.toString();
                    int indexOf = uri3.indexOf("://");
                    if (indexOf >= 0 && indexOf <= 5 && !uri3.substring(0, indexOf).contains(".")) {
                        uri3 = uri3.substring(indexOf + 3);
                    }
                    String encodedFragment = uri2.getEncodedFragment();
                    if (encodedFragment != null) {
                        try {
                            uri3 = uri3.substring(0, uri3.indexOf("#" + encodedFragment));
                        } catch (Exception e5) {
                            e = e5;
                            FileLog.e(e);
                            if (uri2.getScheme() == null) {
                            }
                            if (!z6) {
                            }
                            if (isInternalUri) {
                            }
                            if (!TextUtils.isEmpty(browserPackageName)) {
                            }
                            if (z6) {
                            }
                            intent.putExtra("create_new_tab", true);
                            intent.putExtra("com.android.browser.application_id", context.getPackageName());
                            if (!isInternalUri) {
                            }
                            context.startActivity(intent);
                        }
                    }
                    if (uri3.indexOf(63) >= 0) {
                        str2 = uri3 + "&" + str3;
                    } else {
                        str2 = uri3 + "?" + str3;
                    }
                    if (encodedFragment != null) {
                        str2 = str2 + "#" + encodedFragment;
                    }
                    uri2 = Uri.parse("https://" + str2);
                }
            } catch (Exception e6) {
                e = e6;
                FileLog.e(e);
                if (uri2.getScheme() == null) {
                }
                if (!z6) {
                }
                if (isInternalUri) {
                }
                if (!TextUtils.isEmpty(browserPackageName)) {
                }
                if (z6) {
                }
                intent.putExtra("create_new_tab", true);
                intent.putExtra("com.android.browser.application_id", context.getPackageName());
                if (!isInternalUri) {
                }
                context.startActivity(intent);
            }
            z6 = uri2.getScheme() == null && uri2.getScheme().equalsIgnoreCase("intent");
            if (!z6) {
                intent = Intent.parseUri(uri2.toString(), 1);
            } else {
                intent = new Intent("android.intent.action.VIEW", uri2);
            }
            if (isInternalUri) {
                intent.setComponent(new ComponentName(context.getPackageName(), LaunchActivity.class.getName()));
            }
            if (!TextUtils.isEmpty(browserPackageName)) {
                intent.setPackage(browserPackageName);
            }
            if (z6 && SharedConfig.customTabs && Build.VERSION.SDK_INT >= 30) {
                intent.addFlags(1024);
            } else {
                intent.putExtra("create_new_tab", true);
                intent.putExtra("com.android.browser.application_id", context.getPackageName());
            }
            if (!isInternalUri && (context instanceof LaunchActivity)) {
                intent.putExtra("force_not_internal_apps", z3);
                ((LaunchActivity) context).onNewIntent(intent, progress);
                return;
            }
            context.startActivity(intent);
        } catch (Exception e7) {
            FileLog.e(e7);
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
