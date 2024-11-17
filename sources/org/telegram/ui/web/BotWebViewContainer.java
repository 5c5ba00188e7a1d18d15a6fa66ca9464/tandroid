package org.telegram.ui.web;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import androidx.core.graphics.ColorUtils;
import androidx.core.util.Consumer;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.IDN;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MrzRecognizer;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_bots;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.BottomSheetTabs;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ArticleViewer;
import org.telegram.ui.CameraScanActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedFileDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Paint.Views.LinkPreview;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.telegram.ui.Stories.recorder.StoryRecorder;
import org.telegram.ui.WrappedResourceProvider;
import org.telegram.ui.bots.BotBiometry;
import org.telegram.ui.bots.BotDownloads;
import org.telegram.ui.bots.BotLocation;
import org.telegram.ui.bots.BotSensors;
import org.telegram.ui.bots.BotShareSheet;
import org.telegram.ui.bots.BotWebViewSheet;
import org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout;
import org.telegram.ui.bots.SetupEmojiStatusSheet;
import org.telegram.ui.bots.WebViewRequestProps;
import org.telegram.ui.web.BotWebViewContainer;
import org.telegram.ui.web.BrowserHistory;
import org.telegram.ui.web.WebMetadataCache;

/* loaded from: classes5.dex */
public abstract class BotWebViewContainer extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    public static boolean firstWebView = true;
    private static HashMap rotatedTONHosts;
    private static int tags;
    private BotBiometry biometry;
    private long blockedDialogsUntil;
    public final boolean bot;
    private TLRPC.User botUser;
    private BotWebViewProxy botWebViewProxy;
    private String buttonData;
    private BottomSheet cameraBottomSheet;
    private int currentAccount;
    private AlertDialog currentDialog;
    private String currentPaymentSlug;
    private Delegate delegate;
    private int dialogSequentialOpenTimes;
    private BotDownloads downloads;
    private final CellFlickerDrawable flickerDrawable;
    private BackupImageView flickerView;
    private int flickerViewColor;
    private boolean flickerViewColorOverriden;
    private SvgHelper.SvgDrawable flickerViewDrawable;
    private int forceHeight;
    private boolean hasQRPending;
    private boolean hasUserPermissions;
    private boolean isBackButtonVisible;
    private boolean isFlickeringCenter;
    private boolean isPageLoaded;
    private boolean isRequestingPageOpen;
    private boolean isSettingsButtonVisible;
    private boolean isViewPortByMeasureSuppressed;
    private boolean keyboardFocusable;
    private int lastButtonColor;
    private String lastButtonText;
    private int lastButtonTextColor;
    private long lastClickMs;
    private long lastDialogClosed;
    private long lastDialogCooldownTime;
    private int lastDialogType;
    private boolean lastExpanded;
    private final Rect lastInsets;
    private int lastInsetsTopMargin;
    private long lastPostStoryMs;
    private String lastQrText;
    private int lastSecondaryButtonColor;
    private String lastSecondaryButtonPosition;
    private String lastSecondaryButtonText;
    private int lastSecondaryButtonTextColor;
    private int lastViewportHeightReported;
    private boolean lastViewportIsExpanded;
    private boolean lastViewportStateStable;
    private BotLocation location;
    private ValueCallback mFilePathCallback;
    private String mUrl;
    private final Runnable notifyLocationChecked;
    private Runnable onCloseListener;
    private Runnable onPermissionsRequestResultCallback;
    private MyWebView opener;
    private Activity parentActivity;
    private boolean preserving;
    private Theme.ResourcesProvider resourcesProvider;
    private String secondaryButtonData;
    private BotSensors sensors;
    private int shownDialogsCount;
    private final int tag;
    private float viewPortHeightOffset;
    private boolean wasFocusable;
    private WebViewRequestProps wasOpenedByBot;
    private boolean wasOpenedByLinkIntent;
    private MyWebView webView;
    private boolean webViewNotAvailable;
    private TextView webViewNotAvailableText;
    private Consumer webViewProgressListener;
    private WebViewProxy webViewProxy;
    private WebViewScrollListener webViewScrollListener;

    public static class BotWebViewProxy {
        public BotWebViewContainer container;

        public BotWebViewProxy(BotWebViewContainer botWebViewContainer) {
            this.container = botWebViewContainer;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$postEvent$0(String str, String str2) {
            try {
                BotWebViewContainer botWebViewContainer = this.container;
                if (botWebViewContainer == null) {
                    return;
                }
                botWebViewContainer.onEventReceived(this, str, str2);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @JavascriptInterface
        public void postEvent(final String str, final String str2) {
            try {
                if (this.container == null) {
                    FileLog.d("webviewproxy.postEvent: no container");
                } else {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$BotWebViewProxy$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            BotWebViewContainer.BotWebViewProxy.this.lambda$postEvent$0(str, str2);
                        }
                    });
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        public void setContainer(BotWebViewContainer botWebViewContainer) {
            this.container = botWebViewContainer;
        }
    }

    public interface Delegate {

        public abstract /* synthetic */ class -CC {
            public static BotSensors $default$getBotSensors(Delegate delegate) {
                return null;
            }

            public static boolean $default$isClipboardAvailable(Delegate delegate) {
                return false;
            }

            public static void $default$onEmojiStatusGranted(Delegate delegate, boolean z) {
            }

            public static void $default$onEmojiStatusSet(Delegate delegate, TLRPC.Document document) {
            }

            public static String $default$onFullscreenRequested(Delegate delegate, boolean z) {
                return "UNSUPPORTED";
            }

            public static void $default$onLocationGranted(Delegate delegate, boolean z) {
            }

            public static void $default$onOpenBackFromTabs(Delegate delegate) {
            }

            public static void $default$onOrientationLockChanged(Delegate delegate, boolean z) {
            }

            public static void $default$onSendWebViewData(Delegate delegate, String str) {
            }

            public static void $default$onSharedTo(Delegate delegate, ArrayList arrayList) {
            }

            public static void $default$onWebAppBackgroundChanged(Delegate delegate, boolean z, int i) {
            }

            public static void $default$onWebAppReady(Delegate delegate) {
            }

            public static void $default$onWebAppSetNavigationBarColor(Delegate delegate, int i) {
            }
        }

        BotSensors getBotSensors();

        boolean isClipboardAvailable();

        void onCloseRequested(Runnable runnable);

        void onCloseToTabs();

        void onEmojiStatusGranted(boolean z);

        void onEmojiStatusSet(TLRPC.Document document);

        String onFullscreenRequested(boolean z);

        void onInstantClose();

        void onLocationGranted(boolean z);

        void onOpenBackFromTabs();

        void onOrientationLockChanged(boolean z);

        void onSendWebViewData(String str);

        void onSetBackButtonVisible(boolean z);

        void onSetSettingsButtonVisible(boolean z);

        void onSetupMainButton(boolean z, boolean z2, String str, int i, int i2, boolean z3, boolean z4);

        void onSetupSecondaryButton(boolean z, boolean z2, String str, int i, int i2, boolean z3, boolean z4, String str2);

        void onSharedTo(ArrayList arrayList);

        void onWebAppBackgroundChanged(boolean z, int i);

        void onWebAppExpand();

        void onWebAppOpenInvoice(TLRPC.InputInvoice inputInvoice, String str, TLObject tLObject);

        void onWebAppReady();

        void onWebAppSetActionBarColor(int i, int i2, boolean z);

        void onWebAppSetBackgroundColor(int i);

        void onWebAppSetNavigationBarColor(int i);

        void onWebAppSetupClosingBehavior(boolean z);

        void onWebAppSwipingBehavior(boolean z);

        void onWebAppSwitchInlineQuery(TLRPC.User user, String str, List list);
    }

    public static class MyWebView extends WebView {
        public final boolean bot;
        private BotWebViewContainer botWebViewContainer;
        private BrowserHistory.Entry currentHistoryEntry;
        private BottomSheet currentSheet;
        private String currentUrl;
        public boolean dangerousUrl;
        public boolean errorShown;
        public String errorShownAt;
        public boolean injectedJS;
        private boolean isPageLoaded;
        public int lastActionBarColor;
        public boolean lastActionBarColorGot;
        public int lastBackgroundColor;
        public boolean lastBackgroundColorGot;
        public Bitmap lastFavicon;
        public boolean lastFaviconGot;
        private String lastFaviconUrl;
        private HashMap lastFavicons;
        public String lastSiteName;
        public String lastTitle;
        public boolean lastTitleGot;
        private String lastUrl;
        private Runnable onCloseListener;
        private String openedByUrl;
        public MyWebView opener;
        private int prevScrollX;
        private int prevScrollY;
        private int searchCount;
        private int searchIndex;
        private Runnable searchListener;
        private boolean searchLoading;
        private final int tag;
        public String urlFallback;
        private WebViewScrollListener webViewScrollListener;
        private Runnable whenPageLoaded;

        class 1 implements View.OnLongClickListener {
            1() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onLongClick$0(String str, DialogInterface dialogInterface, int i) {
                if (i != 0) {
                    if (i != 1) {
                        if (i == 2) {
                            AndroidUtilities.addToClipboard(str);
                            if (MyWebView.this.botWebViewContainer != null) {
                                MyWebView.this.botWebViewContainer.showLinkCopiedBulletin();
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                        intent.putExtra("create_new_tab", true);
                        intent.putExtra("com.android.browser.application_id", MyWebView.this.getContext().getPackageName());
                        MyWebView.this.getContext().startActivity(intent);
                        return;
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
                MyWebView.this.loadUrl(str);
            }

            /* JADX INFO: Access modifiers changed from: private */
            /* JADX WARN: Can't wrap try/catch for region: R(11:0|1|2|3|(5:7|9|10|11|12)|17|9|10|11|12|(2:(1:21)|(0))) */
            /* JADX WARN: Code restructure failed: missing block: B:15:0x0041, code lost:
            
                r4 = e;
             */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public /* synthetic */ void lambda$onLongClick$1(final String str) {
                String str2;
                Uri parse;
                BottomSheet.Builder builder = new BottomSheet.Builder(MyWebView.this.getContext(), false, null);
                try {
                    parse = Uri.parse(str);
                } catch (Exception e) {
                    try {
                        FileLog.e((Throwable) e, false);
                    } catch (Exception e2) {
                        e = e2;
                        str2 = str;
                        FileLog.e(e);
                        builder.setTitleMultipleLines(true);
                        builder.setTitle(str2);
                        builder.setItems(new CharSequence[]{LocaleController.getString(R.string.OpenInTelegramBrowser), LocaleController.getString(R.string.OpenInSystemBrowser), LocaleController.getString(R.string.Copy)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda3
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                BotWebViewContainer.MyWebView.1.this.lambda$onLongClick$0(str, dialogInterface, i);
                            }
                        });
                        MyWebView.this.currentSheet = builder.show();
                    }
                }
                if (parse != null && !parse.getScheme().equalsIgnoreCase("data")) {
                    str2 = Browser.replaceHostname(parse, Browser.IDN_toUnicode(parse.getHost()), null);
                    str2 = URLDecoder.decode(str2.replaceAll("\\+", "%2b"), "UTF-8");
                    builder.setTitleMultipleLines(true);
                    builder.setTitle(str2);
                    builder.setItems(new CharSequence[]{LocaleController.getString(R.string.OpenInTelegramBrowser), LocaleController.getString(R.string.OpenInSystemBrowser), LocaleController.getString(R.string.Copy)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda3
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            BotWebViewContainer.MyWebView.1.this.lambda$onLongClick$0(str, dialogInterface, i);
                        }
                    });
                    MyWebView.this.currentSheet = builder.show();
                }
                str2 = str;
                str2 = URLDecoder.decode(str2.replaceAll("\\+", "%2b"), "UTF-8");
                builder.setTitleMultipleLines(true);
                builder.setTitle(str2);
                builder.setItems(new CharSequence[]{LocaleController.getString(R.string.OpenInTelegramBrowser), LocaleController.getString(R.string.OpenInSystemBrowser), LocaleController.getString(R.string.Copy)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda3
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        BotWebViewContainer.MyWebView.1.this.lambda$onLongClick$0(str, dialogInterface, i);
                    }
                });
                MyWebView.this.currentSheet = builder.show();
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onLongClick$2(String str, DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                        intent.putExtra("create_new_tab", true);
                        intent.putExtra("com.android.browser.application_id", MyWebView.this.getContext().getPackageName());
                        MyWebView.this.getContext().startActivity(intent);
                        return;
                    } catch (Exception e) {
                        FileLog.e(e);
                        MyWebView.this.loadUrl(str);
                        return;
                    }
                }
                if (i != 1) {
                    if (i == 2) {
                        AndroidUtilities.addToClipboard(str);
                        if (MyWebView.this.botWebViewContainer != null) {
                            MyWebView.this.botWebViewContainer.showLinkCopiedBulletin();
                            return;
                        }
                        return;
                    }
                    return;
                }
                try {
                    String guessFileName = URLUtil.guessFileName(str, null, "image/*");
                    if (guessFileName == null) {
                        guessFileName = "image.png";
                    }
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                    request.setMimeType("image/*");
                    request.setDescription(LocaleController.getString(R.string.WebDownloading));
                    request.setNotificationVisibility(1);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, guessFileName);
                    DownloadManager downloadManager = (DownloadManager) MyWebView.this.getContext().getSystemService("download");
                    if (downloadManager != null) {
                        downloadManager.enqueue(request);
                    }
                    if (MyWebView.this.botWebViewContainer != null) {
                        BulletinFactory.of(MyWebView.this.botWebViewContainer, MyWebView.this.botWebViewContainer.resourcesProvider).createSimpleBulletin(R.raw.ic_download, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.WebDownloadingFile, guessFileName))).show(true);
                    }
                } catch (Exception e2) {
                    FileLog.e(e2);
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onLongClick$3(final String str) {
                String str2;
                BottomSheet.Builder builder = new BottomSheet.Builder(MyWebView.this.getContext(), false, null);
                try {
                    Uri parse = Uri.parse(str);
                    str2 = Browser.replaceHostname(parse, Browser.IDN_toUnicode(parse.getHost()), null);
                } catch (Exception e) {
                    try {
                        FileLog.e((Throwable) e, false);
                        str2 = str;
                    } catch (Exception e2) {
                        e = e2;
                        str2 = str;
                        FileLog.e(e);
                        builder.setTitleMultipleLines(true);
                        builder.setTitle(str2);
                        builder.setItems(new CharSequence[]{LocaleController.getString(R.string.OpenInSystemBrowser), LocaleController.getString(R.string.AccActionDownload), LocaleController.getString(R.string.CopyLink)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda2
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                BotWebViewContainer.MyWebView.1.this.lambda$onLongClick$2(str, dialogInterface, i);
                            }
                        });
                        MyWebView.this.currentSheet = builder.show();
                    }
                }
                try {
                    str2 = URLDecoder.decode(str2.replaceAll("\\+", "%2b"), "UTF-8");
                } catch (Exception e3) {
                    e = e3;
                    FileLog.e(e);
                    builder.setTitleMultipleLines(true);
                    builder.setTitle(str2);
                    builder.setItems(new CharSequence[]{LocaleController.getString(R.string.OpenInSystemBrowser), LocaleController.getString(R.string.AccActionDownload), LocaleController.getString(R.string.CopyLink)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda2
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            BotWebViewContainer.MyWebView.1.this.lambda$onLongClick$2(str, dialogInterface, i);
                        }
                    });
                    MyWebView.this.currentSheet = builder.show();
                }
                builder.setTitleMultipleLines(true);
                builder.setTitle(str2);
                builder.setItems(new CharSequence[]{LocaleController.getString(R.string.OpenInSystemBrowser), LocaleController.getString(R.string.AccActionDownload), LocaleController.getString(R.string.CopyLink)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda2
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        BotWebViewContainer.MyWebView.1.this.lambda$onLongClick$2(str, dialogInterface, i);
                    }
                });
                MyWebView.this.currentSheet = builder.show();
            }

            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                Runnable runnable;
                WebView.HitTestResult hitTestResult = MyWebView.this.getHitTestResult();
                if (hitTestResult.getType() == 7) {
                    final String extra = hitTestResult.getExtra();
                    runnable = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            BotWebViewContainer.MyWebView.1.this.lambda$onLongClick$1(extra);
                        }
                    };
                } else {
                    if (hitTestResult.getType() != 5) {
                        return false;
                    }
                    final String extra2 = hitTestResult.getExtra();
                    runnable = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            BotWebViewContainer.MyWebView.1.this.lambda$onLongClick$3(extra2);
                        }
                    };
                }
                AndroidUtilities.runOnUIThread(runnable);
                return true;
            }
        }

        class 2 extends WebViewClient {
            private boolean firstRequest = true;
            private final Runnable resetErrorRunnable = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.MyWebView.2.this.lambda$$3();
                }
            };
            final /* synthetic */ boolean val$bot;
            final /* synthetic */ Context val$context;

            2(boolean z, Context context) {
                this.val$bot = z;
                this.val$context = context;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$$3() {
                if (MyWebView.this.botWebViewContainer != null) {
                    BotWebViewContainer botWebViewContainer = MyWebView.this.botWebViewContainer;
                    MyWebView.this.errorShown = false;
                    botWebViewContainer.onErrorShown(false, 0, null);
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onRenderProcessGone$1() {
                Browser.openUrl(MyWebView.this.getContext(), "https://play.google.com/store/apps/details?id=com.google.android.webview");
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onRenderProcessGone$2(DialogInterface dialogInterface) {
                if (MyWebView.this.botWebViewContainer == null || MyWebView.this.botWebViewContainer.delegate == null) {
                    return;
                }
                MyWebView.this.botWebViewContainer.delegate.onCloseRequested(null);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$shouldInterceptRequest$0() {
                if (MyWebView.this.botWebViewContainer != null) {
                    MyWebView.this.botWebViewContainer.onURLChanged(MyWebView.this.urlFallback, !r1.canGoBack(), !MyWebView.this.canGoForward());
                }
            }

            @Override // android.webkit.WebViewClient
            public void doUpdateVisitedHistory(WebView webView, String str, boolean z) {
                if (!this.val$bot && (MyWebView.this.currentHistoryEntry == null || !TextUtils.equals(MyWebView.this.currentHistoryEntry.url, str))) {
                    MyWebView.this.currentHistoryEntry = new BrowserHistory.Entry();
                    MyWebView.this.currentHistoryEntry.id = Utilities.fastRandom.nextLong();
                    MyWebView.this.currentHistoryEntry.time = System.currentTimeMillis();
                    MyWebView.this.currentHistoryEntry.url = BotWebViewContainer.magic2tonsite(MyWebView.this.getUrl());
                    MyWebView.this.currentHistoryEntry.meta = WebMetadataCache.WebMetadata.from(MyWebView.this);
                    BrowserHistory.pushHistory(MyWebView.this.currentHistoryEntry);
                }
                MyWebView.this.d("doUpdateVisitedHistory " + str + " " + z);
                if (MyWebView.this.botWebViewContainer != null) {
                    BotWebViewContainer botWebViewContainer = MyWebView.this.botWebViewContainer;
                    MyWebView myWebView = MyWebView.this;
                    botWebViewContainer.onURLChanged(myWebView.dangerousUrl ? myWebView.urlFallback : str, !myWebView.canGoBack(), !MyWebView.this.canGoForward());
                }
                super.doUpdateVisitedHistory(webView, str, z);
            }

            @Override // android.webkit.WebViewClient
            public void onPageCommitVisible(WebView webView, String str) {
                MyWebView myWebView;
                String replace;
                if (MyWebView.this.whenPageLoaded != null) {
                    Runnable runnable = MyWebView.this.whenPageLoaded;
                    MyWebView.this.whenPageLoaded = null;
                    runnable.run();
                }
                MyWebView.this.d("onPageCommitVisible " + str);
                if (this.val$bot) {
                    myWebView = MyWebView.this;
                    myWebView.injectedJS = true;
                    replace = AndroidUtilities.readRes(R.raw.webview_app_ext).replace("$DEBUG$", "" + BuildVars.DEBUG_VERSION);
                } else {
                    MyWebView myWebView2 = MyWebView.this;
                    myWebView2.injectedJS = true;
                    myWebView2.evaluateJS(AndroidUtilities.readRes(R.raw.webview_ext).replace("$DEBUG$", "" + BuildVars.DEBUG_VERSION));
                    myWebView = MyWebView.this;
                    replace = AndroidUtilities.readRes(R.raw.webview_share);
                }
                myWebView.evaluateJS(replace);
                super.onPageCommitVisible(webView, str);
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                boolean z;
                MyWebView myWebView;
                String replace;
                MyWebView.this.isPageLoaded = true;
                if (MyWebView.this.whenPageLoaded != null) {
                    Runnable runnable = MyWebView.this.whenPageLoaded;
                    MyWebView.this.whenPageLoaded = null;
                    runnable.run();
                    z = false;
                } else {
                    z = true;
                }
                MyWebView.this.d("onPageFinished");
                if (MyWebView.this.botWebViewContainer != null) {
                    MyWebView.this.botWebViewContainer.setPageLoaded(str, z);
                } else {
                    MyWebView.this.d("onPageFinished: no container");
                }
                if (this.val$bot) {
                    myWebView = MyWebView.this;
                    myWebView.injectedJS = true;
                    replace = AndroidUtilities.readRes(R.raw.webview_app_ext).replace("$DEBUG$", "" + BuildVars.DEBUG_VERSION);
                } else {
                    MyWebView myWebView2 = MyWebView.this;
                    myWebView2.injectedJS = true;
                    myWebView2.evaluateJS(AndroidUtilities.readRes(R.raw.webview_ext).replace("$DEBUG$", "" + BuildVars.DEBUG_VERSION));
                    myWebView = MyWebView.this;
                    replace = AndroidUtilities.readRes(R.raw.webview_share);
                }
                myWebView.evaluateJS(replace);
                MyWebView.this.saveHistory();
                if (MyWebView.this.botWebViewContainer != null) {
                    BotWebViewContainer botWebViewContainer = MyWebView.this.botWebViewContainer;
                    MyWebView myWebView3 = MyWebView.this;
                    botWebViewContainer.onURLChanged(myWebView3.dangerousUrl ? myWebView3.urlFallback : myWebView3.getUrl(), !MyWebView.this.canGoBack(), true ^ MyWebView.this.canGoForward());
                }
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                String str2;
                MyWebView.this.getSettings().setMediaPlaybackRequiresUserGesture(true);
                if (MyWebView.this.currentSheet != null) {
                    MyWebView.this.currentSheet.dismiss();
                    MyWebView.this.currentSheet = null;
                }
                MyWebView.this.currentHistoryEntry = null;
                MyWebView.this.currentUrl = str;
                MyWebView myWebView = MyWebView.this;
                myWebView.lastSiteName = null;
                myWebView.lastActionBarColorGot = false;
                myWebView.lastBackgroundColorGot = false;
                myWebView.lastFaviconGot = false;
                myWebView.d("onPageStarted " + str);
                if (MyWebView.this.botWebViewContainer != null) {
                    MyWebView myWebView2 = MyWebView.this;
                    if (myWebView2.errorShown && ((str2 = myWebView2.errorShownAt) == null || !TextUtils.equals(str2, str))) {
                        AndroidUtilities.runOnUIThread(this.resetErrorRunnable, 40L);
                    }
                }
                if (MyWebView.this.botWebViewContainer != null) {
                    BotWebViewContainer botWebViewContainer = MyWebView.this.botWebViewContainer;
                    MyWebView myWebView3 = MyWebView.this;
                    botWebViewContainer.onURLChanged(myWebView3.dangerousUrl ? myWebView3.urlFallback : str, !myWebView3.canGoBack(), true ^ MyWebView.this.canGoForward());
                }
                super.onPageStarted(webView, str, bitmap);
                MyWebView.this.injectedJS = false;
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, int i, String str, String str2) {
                MyWebView.this.d("onReceivedError: " + i + " " + str + " url=" + str2);
                if (Build.VERSION.SDK_INT < 23 && MyWebView.this.botWebViewContainer != null) {
                    AndroidUtilities.cancelRunOnUIThread(this.resetErrorRunnable);
                    MyWebView myWebView = MyWebView.this;
                    myWebView.lastSiteName = null;
                    myWebView.lastActionBarColorGot = false;
                    myWebView.lastBackgroundColorGot = false;
                    myWebView.lastFaviconGot = false;
                    myWebView.lastTitleGot = false;
                    myWebView.errorShownAt = myWebView.getUrl();
                    BotWebViewContainer botWebViewContainer = MyWebView.this.botWebViewContainer;
                    MyWebView.this.lastTitle = null;
                    botWebViewContainer.onTitleChanged(null);
                    BotWebViewContainer botWebViewContainer2 = MyWebView.this.botWebViewContainer;
                    MyWebView.this.lastFavicon = null;
                    botWebViewContainer2.onFaviconChanged(null);
                    BotWebViewContainer botWebViewContainer3 = MyWebView.this.botWebViewContainer;
                    MyWebView.this.errorShown = true;
                    botWebViewContainer3.onErrorShown(true, i, str);
                }
                super.onReceivedError(webView, i, str, str2);
            }

            /* JADX WARN: Code restructure failed: missing block: B:7:0x003a, code lost:
            
                if (r0 != false) goto L9;
             */
            /* JADX WARN: Removed duplicated region for block: B:15:0x0099  */
            @Override // android.webkit.WebViewClient
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                int errorCode;
                CharSequence description;
                String url;
                int errorCode2;
                CharSequence description2;
                CharSequence description3;
                Uri url2;
                Uri url3;
                boolean isForMainFrame;
                if (Build.VERSION.SDK_INT >= 23) {
                    MyWebView myWebView = MyWebView.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onReceivedError: ");
                    errorCode = webResourceError.getErrorCode();
                    sb.append(errorCode);
                    sb.append(" ");
                    description = webResourceError.getDescription();
                    sb.append((Object) description);
                    myWebView.d(sb.toString());
                    if (MyWebView.this.botWebViewContainer != null) {
                        if (webResourceRequest != null) {
                            isForMainFrame = webResourceRequest.isForMainFrame();
                        }
                        AndroidUtilities.cancelRunOnUIThread(this.resetErrorRunnable);
                        MyWebView myWebView2 = MyWebView.this;
                        String str = null;
                        myWebView2.lastSiteName = null;
                        myWebView2.lastActionBarColorGot = false;
                        myWebView2.lastBackgroundColorGot = false;
                        myWebView2.lastFaviconGot = false;
                        myWebView2.lastTitleGot = false;
                        if (webResourceRequest != null) {
                            url2 = webResourceRequest.getUrl();
                            if (url2 != null) {
                                url3 = webResourceRequest.getUrl();
                                url = url3.toString();
                                myWebView2.errorShownAt = url;
                                BotWebViewContainer botWebViewContainer = MyWebView.this.botWebViewContainer;
                                MyWebView.this.lastTitle = null;
                                botWebViewContainer.onTitleChanged(null);
                                BotWebViewContainer botWebViewContainer2 = MyWebView.this.botWebViewContainer;
                                MyWebView.this.lastFavicon = null;
                                botWebViewContainer2.onFaviconChanged(null);
                                BotWebViewContainer botWebViewContainer3 = MyWebView.this.botWebViewContainer;
                                MyWebView.this.errorShown = true;
                                errorCode2 = webResourceError.getErrorCode();
                                description2 = webResourceError.getDescription();
                                if (description2 != null) {
                                    description3 = webResourceError.getDescription();
                                    str = description3.toString();
                                }
                                botWebViewContainer3.onErrorShown(true, errorCode2, str);
                            }
                        }
                        url = MyWebView.this.getUrl();
                        myWebView2.errorShownAt = url;
                        BotWebViewContainer botWebViewContainer4 = MyWebView.this.botWebViewContainer;
                        MyWebView.this.lastTitle = null;
                        botWebViewContainer4.onTitleChanged(null);
                        BotWebViewContainer botWebViewContainer22 = MyWebView.this.botWebViewContainer;
                        MyWebView.this.lastFavicon = null;
                        botWebViewContainer22.onFaviconChanged(null);
                        BotWebViewContainer botWebViewContainer32 = MyWebView.this.botWebViewContainer;
                        MyWebView.this.errorShown = true;
                        errorCode2 = webResourceError.getErrorCode();
                        description2 = webResourceError.getDescription();
                        if (description2 != null) {
                        }
                        botWebViewContainer32.onErrorShown(true, errorCode2, str);
                    }
                }
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                int statusCode;
                Integer valueOf;
                String url;
                int statusCode2;
                String reasonPhrase;
                Uri url2;
                Uri url3;
                boolean isForMainFrame;
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                if (Build.VERSION.SDK_INT >= 21) {
                    MyWebView myWebView = MyWebView.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onReceivedHttpError: statusCode=");
                    if (webResourceResponse == null) {
                        valueOf = null;
                    } else {
                        statusCode = webResourceResponse.getStatusCode();
                        valueOf = Integer.valueOf(statusCode);
                    }
                    sb.append(valueOf);
                    sb.append(" request=");
                    sb.append(webResourceRequest == null ? null : webResourceRequest.getUrl());
                    myWebView.d(sb.toString());
                    if (MyWebView.this.botWebViewContainer != null) {
                        if (webResourceRequest != null) {
                            isForMainFrame = webResourceRequest.isForMainFrame();
                            if (!isForMainFrame) {
                                return;
                            }
                        }
                        if (webResourceResponse == null || !TextUtils.isEmpty(webResourceResponse.getMimeType())) {
                            return;
                        }
                        AndroidUtilities.cancelRunOnUIThread(this.resetErrorRunnable);
                        MyWebView myWebView2 = MyWebView.this;
                        myWebView2.lastSiteName = null;
                        myWebView2.lastActionBarColorGot = false;
                        myWebView2.lastBackgroundColorGot = false;
                        myWebView2.lastFaviconGot = false;
                        myWebView2.lastTitleGot = false;
                        if (webResourceRequest != null) {
                            url2 = webResourceRequest.getUrl();
                            if (url2 != null) {
                                url3 = webResourceRequest.getUrl();
                                url = url3.toString();
                                myWebView2.errorShownAt = url;
                                BotWebViewContainer botWebViewContainer = MyWebView.this.botWebViewContainer;
                                MyWebView.this.lastTitle = null;
                                botWebViewContainer.onTitleChanged(null);
                                BotWebViewContainer botWebViewContainer2 = MyWebView.this.botWebViewContainer;
                                MyWebView.this.lastFavicon = null;
                                botWebViewContainer2.onFaviconChanged(null);
                                BotWebViewContainer botWebViewContainer3 = MyWebView.this.botWebViewContainer;
                                MyWebView.this.errorShown = true;
                                statusCode2 = webResourceResponse.getStatusCode();
                                reasonPhrase = webResourceResponse.getReasonPhrase();
                                botWebViewContainer3.onErrorShown(true, statusCode2, reasonPhrase);
                            }
                        }
                        url = MyWebView.this.getUrl();
                        myWebView2.errorShownAt = url;
                        BotWebViewContainer botWebViewContainer4 = MyWebView.this.botWebViewContainer;
                        MyWebView.this.lastTitle = null;
                        botWebViewContainer4.onTitleChanged(null);
                        BotWebViewContainer botWebViewContainer22 = MyWebView.this.botWebViewContainer;
                        MyWebView.this.lastFavicon = null;
                        botWebViewContainer22.onFaviconChanged(null);
                        BotWebViewContainer botWebViewContainer32 = MyWebView.this.botWebViewContainer;
                        MyWebView.this.errorShown = true;
                        statusCode2 = webResourceResponse.getStatusCode();
                        reasonPhrase = webResourceResponse.getReasonPhrase();
                        botWebViewContainer32.onErrorShown(true, statusCode2, reasonPhrase);
                    }
                }
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                MyWebView myWebView = MyWebView.this;
                StringBuilder sb = new StringBuilder();
                sb.append("onReceivedSslError: error=");
                sb.append(sslError);
                sb.append(" url=");
                sb.append(sslError == null ? null : sslError.getUrl());
                myWebView.d(sb.toString());
                sslErrorHandler.cancel();
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }

            @Override // android.webkit.WebViewClient
            public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
                MyWebView myWebView;
                String str;
                int rendererPriorityAtExit;
                Integer valueOf;
                boolean didCrash;
                Boolean valueOf2;
                if (Build.VERSION.SDK_INT >= 26) {
                    myWebView = MyWebView.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onRenderProcessGone priority=");
                    if (renderProcessGoneDetail == null) {
                        valueOf = null;
                    } else {
                        rendererPriorityAtExit = renderProcessGoneDetail.rendererPriorityAtExit();
                        valueOf = Integer.valueOf(rendererPriorityAtExit);
                    }
                    sb.append(valueOf);
                    sb.append(" didCrash=");
                    if (renderProcessGoneDetail == null) {
                        valueOf2 = null;
                    } else {
                        didCrash = renderProcessGoneDetail.didCrash();
                        valueOf2 = Boolean.valueOf(didCrash);
                    }
                    sb.append(valueOf2);
                    str = sb.toString();
                } else {
                    myWebView = MyWebView.this;
                    str = "onRenderProcessGone";
                }
                myWebView.d(str);
                if (!AndroidUtilities.isSafeToShow(MyWebView.this.getContext())) {
                    return true;
                }
                new AlertDialog.Builder(MyWebView.this.getContext(), MyWebView.this.botWebViewContainer == null ? null : MyWebView.this.botWebViewContainer.resourcesProvider).setTitle(LocaleController.getString(R.string.ChromeCrashTitle)).setMessage(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.ChromeCrashMessage), new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        BotWebViewContainer.MyWebView.2.this.lambda$onRenderProcessGone$1();
                    }
                })).setPositiveButton(LocaleController.getString(R.string.OK), null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda10
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        BotWebViewContainer.MyWebView.2.this.lambda$onRenderProcessGone$2(dialogInterface);
                    }
                }).show();
                return true;
            }

            @Override // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                Uri url;
                String method;
                Map requestHeaders;
                int i;
                String method2;
                Uri url2;
                Map requestHeaders2;
                Uri url3;
                if (Build.VERSION.SDK_INT >= 21) {
                    MyWebView myWebView = MyWebView.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("shouldInterceptRequest ");
                    HttpURLConnection httpURLConnection = null;
                    sb.append(webResourceRequest == null ? null : webResourceRequest.getUrl());
                    myWebView.d(sb.toString());
                    if (webResourceRequest != null) {
                        url3 = webResourceRequest.getUrl();
                        if (BotWebViewContainer.isTonsite(url3)) {
                            MyWebView.this.d("proxying ton");
                            this.firstRequest = false;
                            return BotWebViewContainer.proxyTON(webResourceRequest);
                        }
                    }
                    if (!this.val$bot && MyWebView.this.opener != null && this.firstRequest) {
                        try {
                            url = webResourceRequest.getUrl();
                            HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(url.toString()).openConnection();
                            try {
                                method = webResourceRequest.getMethod();
                                httpURLConnection2.setRequestMethod(method);
                                requestHeaders = webResourceRequest.getRequestHeaders();
                                if (requestHeaders != null) {
                                    requestHeaders2 = webResourceRequest.getRequestHeaders();
                                    for (Map.Entry entry : requestHeaders2.entrySet()) {
                                        httpURLConnection2.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                                    }
                                }
                                httpURLConnection2.connect();
                                HashMap hashMap = new HashMap();
                                Iterator<Map.Entry<String, List<String>>> it = httpURLConnection2.getHeaderFields().entrySet().iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    Map.Entry<String, List<String>> next = it.next();
                                    String key = next.getKey();
                                    if (key != null) {
                                        hashMap.put(key, TextUtils.join(", ", next.getValue()));
                                        if (!MyWebView.this.dangerousUrl && ("cross-origin-resource-policy".equals(key.toLowerCase()) || "cross-origin-embedder-policy".equals(key.toLowerCase()))) {
                                            Iterator<String> it2 = next.getValue().iterator();
                                            while (true) {
                                                if (!it2.hasNext()) {
                                                    break;
                                                }
                                                String next2 = it2.next();
                                                if (next2 != null && !"unsafe-none".equals(next2.toLowerCase()) && !"same-site".equals(next2.toLowerCase())) {
                                                    MyWebView myWebView2 = MyWebView.this;
                                                    StringBuilder sb2 = new StringBuilder();
                                                    sb2.append("<!> dangerous header CORS policy: ");
                                                    sb2.append(key);
                                                    sb2.append(": ");
                                                    sb2.append(next2);
                                                    sb2.append(" from ");
                                                    method2 = webResourceRequest.getMethod();
                                                    sb2.append(method2);
                                                    sb2.append(" ");
                                                    url2 = webResourceRequest.getUrl();
                                                    sb2.append(url2);
                                                    myWebView2.d(sb2.toString());
                                                    MyWebView.this.dangerousUrl = true;
                                                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda8
                                                        @Override // java.lang.Runnable
                                                        public final void run() {
                                                            BotWebViewContainer.MyWebView.2.this.lambda$shouldInterceptRequest$0();
                                                        }
                                                    });
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                String contentType = httpURLConnection2.getContentType();
                                String contentEncoding = httpURLConnection2.getContentEncoding();
                                if (contentType.indexOf("; ") >= 0) {
                                    String[] split = contentType.split("; ");
                                    if (!TextUtils.isEmpty(split[0])) {
                                        contentType = split[0];
                                    }
                                    for (i = 1; i < split.length; i++) {
                                        if (split[i].startsWith("charset=")) {
                                            contentEncoding = split[i].substring(8);
                                        }
                                    }
                                }
                                this.firstRequest = false;
                                return new WebResourceResponse(contentType, contentEncoding, httpURLConnection2.getResponseCode(), httpURLConnection2.getResponseMessage(), hashMap, httpURLConnection2.getInputStream());
                            } catch (Exception e) {
                                e = e;
                                httpURLConnection = httpURLConnection2;
                                FileLog.e(e);
                                if (httpURLConnection != null) {
                                    httpURLConnection.disconnect();
                                }
                                this.firstRequest = false;
                                return super.shouldInterceptRequest(webView, webResourceRequest);
                            }
                        } catch (Exception e2) {
                            e = e2;
                        }
                    }
                }
                this.firstRequest = false;
                return super.shouldInterceptRequest(webView, webResourceRequest);
            }

            @Override // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
                MyWebView.this.d("shouldInterceptRequest " + str);
                if (!BotWebViewContainer.isTonsite(str)) {
                    return super.shouldInterceptRequest(webView, str);
                }
                MyWebView.this.d("proxying ton");
                return BotWebViewContainer.proxyTON("GET", str, null);
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (str == null || str.trim().startsWith("sms:")) {
                    return false;
                }
                if (str.trim().startsWith("tel:")) {
                    MyWebView myWebView = MyWebView.this;
                    if (myWebView.opener != null) {
                        if (myWebView.botWebViewContainer.delegate != null) {
                            MyWebView.this.botWebViewContainer.delegate.onInstantClose();
                        } else if (MyWebView.this.onCloseListener != null) {
                            MyWebView.this.onCloseListener.run();
                            MyWebView.this.onCloseListener = null;
                        }
                    }
                    Browser.openUrl(this.val$context, str);
                    return true;
                }
                Uri parse = Uri.parse(str);
                if (!this.val$bot) {
                    if (Browser.openInExternalApp(this.val$context, str, true)) {
                        MyWebView.this.d("shouldOverrideUrlLoading(" + str + ") = true (openInExternalBrowser)");
                        if (!MyWebView.this.isPageLoaded && !MyWebView.this.canGoBack()) {
                            if (MyWebView.this.botWebViewContainer.delegate != null) {
                                MyWebView.this.botWebViewContainer.delegate.onInstantClose();
                            } else if (MyWebView.this.onCloseListener != null) {
                                MyWebView.this.onCloseListener.run();
                                MyWebView.this.onCloseListener = null;
                            }
                        }
                        return true;
                    }
                    if (str.startsWith("intent://") || (parse != null && parse.getScheme() != null && parse.getScheme().equalsIgnoreCase("intent"))) {
                        try {
                            String stringExtra = Intent.parseUri(parse.toString(), 1).getStringExtra("browser_fallback_url");
                            if (!TextUtils.isEmpty(stringExtra)) {
                                MyWebView.this.loadUrl(stringExtra);
                                return true;
                            }
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                    }
                    if (parse != null && parse.getScheme() != null && !"https".equals(parse.getScheme()) && !"http".equals(parse.getScheme()) && !"tonsite".equals(parse.getScheme())) {
                        MyWebView.this.d("shouldOverrideUrlLoading(" + str + ") = true (browser open)");
                        Browser.openUrl(MyWebView.this.getContext(), parse);
                        return true;
                    }
                }
                if (MyWebView.this.botWebViewContainer == null || !Browser.isInternalUri(parse, null)) {
                    if (parse != null) {
                        MyWebView.this.currentUrl = parse.toString();
                    }
                    MyWebView.this.d("shouldOverrideUrlLoading(" + str + ") = false");
                    return false;
                }
                if (!this.val$bot && "1".equals(parse.getQueryParameter("embed")) && "t.me".equals(parse.getAuthority())) {
                    return false;
                }
                if (MessagesController.getInstance(MyWebView.this.botWebViewContainer.currentAccount).webAppAllowedProtocols != null && MessagesController.getInstance(MyWebView.this.botWebViewContainer.currentAccount).webAppAllowedProtocols.contains(parse.getScheme())) {
                    MyWebView myWebView2 = MyWebView.this;
                    if (myWebView2.opener != null) {
                        if (myWebView2.botWebViewContainer.delegate != null) {
                            MyWebView.this.botWebViewContainer.delegate.onInstantClose();
                        } else if (MyWebView.this.onCloseListener != null) {
                            MyWebView.this.onCloseListener.run();
                            MyWebView.this.onCloseListener = null;
                        }
                        if (MyWebView.this.opener.botWebViewContainer != null && MyWebView.this.opener.botWebViewContainer.delegate != null) {
                            MyWebView.this.opener.botWebViewContainer.delegate.onCloseToTabs();
                        }
                    }
                    MyWebView.this.botWebViewContainer.onOpenUri(parse);
                }
                MyWebView.this.d("shouldOverrideUrlLoading(" + str + ") = true");
                return true;
            }
        }

        class 3 extends WebChromeClient {
            private Dialog lastPermissionsDialog;
            final /* synthetic */ boolean val$bot;

            class 1 extends WebViewClient {
                final /* synthetic */ WebView val$newWebView;

                1(WebView webView) {
                    this.val$newWebView = webView;
                }

                /* JADX INFO: Access modifiers changed from: private */
                public /* synthetic */ void lambda$onRenderProcessGone$0() {
                    Browser.openUrl(MyWebView.this.getContext(), "https://play.google.com/store/apps/details?id=com.google.android.webview");
                }

                /* JADX INFO: Access modifiers changed from: private */
                public /* synthetic */ void lambda$onRenderProcessGone$1(DialogInterface dialogInterface) {
                    if (MyWebView.this.botWebViewContainer.delegate != null) {
                        MyWebView.this.botWebViewContainer.delegate.onCloseRequested(null);
                    }
                }

                @Override // android.webkit.WebViewClient
                public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
                    MyWebView myWebView;
                    String str;
                    int rendererPriorityAtExit;
                    Integer valueOf;
                    boolean didCrash;
                    Boolean valueOf2;
                    if (Build.VERSION.SDK_INT >= 26) {
                        myWebView = MyWebView.this;
                        StringBuilder sb = new StringBuilder();
                        sb.append("newWebView.onRenderProcessGone priority=");
                        if (renderProcessGoneDetail == null) {
                            valueOf = null;
                        } else {
                            rendererPriorityAtExit = renderProcessGoneDetail.rendererPriorityAtExit();
                            valueOf = Integer.valueOf(rendererPriorityAtExit);
                        }
                        sb.append(valueOf);
                        sb.append(" didCrash=");
                        if (renderProcessGoneDetail == null) {
                            valueOf2 = null;
                        } else {
                            didCrash = renderProcessGoneDetail.didCrash();
                            valueOf2 = Boolean.valueOf(didCrash);
                        }
                        sb.append(valueOf2);
                        str = sb.toString();
                    } else {
                        myWebView = MyWebView.this;
                        str = "newWebView.onRenderProcessGone";
                    }
                    myWebView.d(str);
                    if (!AndroidUtilities.isSafeToShow(MyWebView.this.getContext())) {
                        return true;
                    }
                    new AlertDialog.Builder(MyWebView.this.getContext(), MyWebView.this.botWebViewContainer == null ? null : MyWebView.this.botWebViewContainer.resourcesProvider).setTitle(LocaleController.getString(R.string.ChromeCrashTitle)).setMessage(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.ChromeCrashMessage), new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            BotWebViewContainer.MyWebView.3.1.this.lambda$onRenderProcessGone$0();
                        }
                    })).setPositiveButton(LocaleController.getString(R.string.OK), null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$1$$ExternalSyntheticLambda1
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            BotWebViewContainer.MyWebView.3.1.this.lambda$onRenderProcessGone$1(dialogInterface);
                        }
                    }).show();
                    return true;
                }

                @Override // android.webkit.WebViewClient
                public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                    if (MyWebView.this.botWebViewContainer == null) {
                        return true;
                    }
                    MyWebView.this.botWebViewContainer.onOpenUri(Uri.parse(str));
                    this.val$newWebView.destroy();
                    return true;
                }
            }

            3(boolean z) {
                this.val$bot = z;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onGeolocationPermissionsShowPrompt$0(GeolocationPermissions.Callback callback, String str, Boolean bool) {
                callback.invoke(str, bool.booleanValue(), false);
                if (bool.booleanValue()) {
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onGeolocationPermissionsShowPrompt$1(final GeolocationPermissions.Callback callback, final String str, Boolean bool) {
                if (this.lastPermissionsDialog != null) {
                    this.lastPermissionsDialog = null;
                    if (bool.booleanValue()) {
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda8
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.3.this.lambda$onGeolocationPermissionsShowPrompt$0(callback, str, (Boolean) obj);
                            }
                        });
                    } else {
                        callback.invoke(str, false, false);
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onPermissionRequest$2(PermissionRequest permissionRequest, String str, Boolean bool) {
                if (!bool.booleanValue()) {
                    permissionRequest.deny();
                } else {
                    permissionRequest.grant(new String[]{str});
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onPermissionRequest$3(final PermissionRequest permissionRequest, final String str, Boolean bool) {
                if (this.lastPermissionsDialog != null) {
                    this.lastPermissionsDialog = null;
                    if (bool.booleanValue()) {
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.RECORD_AUDIO"}, new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda10
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.3.this.lambda$onPermissionRequest$2(permissionRequest, str, (Boolean) obj);
                            }
                        });
                    } else {
                        permissionRequest.deny();
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onPermissionRequest$4(PermissionRequest permissionRequest, String str, Boolean bool) {
                if (!bool.booleanValue()) {
                    permissionRequest.deny();
                } else {
                    permissionRequest.grant(new String[]{str});
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onPermissionRequest$5(final PermissionRequest permissionRequest, final String str, Boolean bool) {
                if (this.lastPermissionsDialog != null) {
                    this.lastPermissionsDialog = null;
                    if (bool.booleanValue()) {
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.CAMERA"}, new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda9
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.3.this.lambda$onPermissionRequest$4(permissionRequest, str, (Boolean) obj);
                            }
                        });
                    } else {
                        permissionRequest.deny();
                    }
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onPermissionRequest$6(PermissionRequest permissionRequest, String[] strArr, Boolean bool) {
                if (!bool.booleanValue()) {
                    permissionRequest.deny();
                } else {
                    permissionRequest.grant(new String[]{strArr[0], strArr[1]});
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onPermissionRequest$7(final PermissionRequest permissionRequest, final String[] strArr, Boolean bool) {
                if (this.lastPermissionsDialog != null) {
                    this.lastPermissionsDialog = null;
                    if (bool.booleanValue()) {
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO"}, new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda11
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.3.this.lambda$onPermissionRequest$6(permissionRequest, strArr, (Boolean) obj);
                            }
                        });
                    } else {
                        permissionRequest.deny();
                    }
                }
            }

            @Override // android.webkit.WebChromeClient
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
            }

            @Override // android.webkit.WebChromeClient
            public void onCloseWindow(WebView webView) {
                MyWebView.this.d("onCloseWindow " + webView);
                if (MyWebView.this.botWebViewContainer != null && MyWebView.this.botWebViewContainer.delegate != null) {
                    MyWebView.this.botWebViewContainer.delegate.onCloseRequested(null);
                } else if (MyWebView.this.onCloseListener != null) {
                    MyWebView.this.onCloseListener.run();
                    MyWebView.this.onCloseListener = null;
                }
                super.onCloseWindow(webView);
            }

            @Override // android.webkit.WebChromeClient
            public boolean onCreateWindow(WebView webView, boolean z, boolean z2, Message message) {
                BaseFragment safeLastFragment;
                MyWebView.this.d("onCreateWindow isDialog=" + z + " isUserGesture=" + z2 + " resultMsg=" + message);
                String url = MyWebView.this.getUrl();
                if (!SharedConfig.inappBrowser) {
                    WebView webView2 = new WebView(webView.getContext());
                    webView2.setWebViewClient(new 1(webView2));
                    ((WebView.WebViewTransport) message.obj).setWebView(webView2);
                } else {
                    if (MyWebView.this.botWebViewContainer == null || (safeLastFragment = LaunchActivity.getSafeLastFragment()) == null) {
                        return false;
                    }
                    if (safeLastFragment.getParentLayout() instanceof ActionBarLayout) {
                        safeLastFragment = ((ActionBarLayout) safeLastFragment.getParentLayout()).getSheetFragment();
                    }
                    ArticleViewer createArticleViewer = safeLastFragment.createArticleViewer(true);
                    createArticleViewer.setOpener(MyWebView.this);
                    createArticleViewer.open((String) null);
                    MyWebView lastWebView = createArticleViewer.getLastWebView();
                    if (!TextUtils.isEmpty(url)) {
                        lastWebView.urlFallback = url;
                    }
                    MyWebView.this.d("onCreateWindow: newWebView=" + lastWebView);
                    if (lastWebView == null) {
                        createArticleViewer.close(true, true);
                        return false;
                    }
                    ((WebView.WebViewTransport) message.obj).setWebView(lastWebView);
                }
                message.sendToTarget();
                return true;
            }

            @Override // android.webkit.WebChromeClient
            public void onGeolocationPermissionsHidePrompt() {
                if (this.lastPermissionsDialog == null) {
                    MyWebView.this.d("onGeolocationPermissionsHidePrompt: no dialog");
                    return;
                }
                MyWebView.this.d("onGeolocationPermissionsHidePrompt: dialog.dismiss");
                this.lastPermissionsDialog.dismiss();
                this.lastPermissionsDialog = null;
            }

            @Override // android.webkit.WebChromeClient
            public void onGeolocationPermissionsShowPrompt(final String str, final GeolocationPermissions.Callback callback) {
                if (MyWebView.this.botWebViewContainer == null || MyWebView.this.botWebViewContainer.parentActivity == null) {
                    MyWebView.this.d("onGeolocationPermissionsShowPrompt: no container");
                    callback.invoke(str, false, false);
                    return;
                }
                MyWebView.this.d("onGeolocationPermissionsShowPrompt " + str);
                String userName = this.val$bot ? UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser) : AndroidUtilities.getHostAuthority(MyWebView.this.getUrl());
                Dialog createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(MyWebView.this.botWebViewContainer.parentActivity, MyWebView.this.botWebViewContainer.resourcesProvider, new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, R.raw.permission_request_location, LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestGeolocationPermission : R.string.WebViewRequestGeolocationPermission, userName), LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestGeolocationPermissionWithHint : R.string.WebViewRequestGeolocationPermissionWithHint, userName), new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda7
                    @Override // androidx.core.util.Consumer
                    public final void accept(Object obj) {
                        BotWebViewContainer.MyWebView.3.this.lambda$onGeolocationPermissionsShowPrompt$1(callback, str, (Boolean) obj);
                    }
                });
                this.lastPermissionsDialog = createWebViewPermissionsRequestDialog;
                createWebViewPermissionsRequestDialog.show();
            }

            @Override // android.webkit.WebChromeClient
            public void onPermissionRequest(final PermissionRequest permissionRequest) {
                final String[] resources;
                Dialog createWebViewPermissionsRequestDialog;
                Dialog dialog = this.lastPermissionsDialog;
                if (dialog != null) {
                    dialog.dismiss();
                    this.lastPermissionsDialog = null;
                }
                if (MyWebView.this.botWebViewContainer == null) {
                    MyWebView.this.d("onPermissionRequest: no container");
                    permissionRequest.deny();
                    return;
                }
                MyWebView.this.d("onPermissionRequest " + permissionRequest);
                String userName = this.val$bot ? UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser) : AndroidUtilities.getHostAuthority(MyWebView.this.getUrl());
                resources = permissionRequest.getResources();
                if (resources.length == 1) {
                    final String str = resources[0];
                    if (MyWebView.this.botWebViewContainer.parentActivity == null) {
                        permissionRequest.deny();
                        return;
                    }
                    str.hashCode();
                    if (str.equals("android.webkit.resource.VIDEO_CAPTURE")) {
                        createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(MyWebView.this.botWebViewContainer.parentActivity, MyWebView.this.botWebViewContainer.resourcesProvider, new String[]{"android.permission.CAMERA"}, R.raw.permission_request_camera, LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestCameraPermission : R.string.WebViewRequestCameraPermission, userName), LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestCameraPermissionWithHint : R.string.WebViewRequestCameraPermissionWithHint, userName), new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda5
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.3.this.lambda$onPermissionRequest$5(permissionRequest, str, (Boolean) obj);
                            }
                        });
                    } else if (!str.equals("android.webkit.resource.AUDIO_CAPTURE")) {
                        return;
                    } else {
                        createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(MyWebView.this.botWebViewContainer.parentActivity, MyWebView.this.botWebViewContainer.resourcesProvider, new String[]{"android.permission.RECORD_AUDIO"}, R.raw.permission_request_microphone, LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestMicrophonePermission : R.string.WebViewRequestMicrophonePermission, userName), LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestMicrophonePermissionWithHint : R.string.WebViewRequestMicrophonePermissionWithHint, userName), new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda4
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.3.this.lambda$onPermissionRequest$3(permissionRequest, str, (Boolean) obj);
                            }
                        });
                    }
                } else {
                    if (resources.length != 2) {
                        return;
                    }
                    if (!"android.webkit.resource.AUDIO_CAPTURE".equals(resources[0]) && !"android.webkit.resource.VIDEO_CAPTURE".equals(resources[0])) {
                        return;
                    }
                    if (!"android.webkit.resource.AUDIO_CAPTURE".equals(resources[1]) && !"android.webkit.resource.VIDEO_CAPTURE".equals(resources[1])) {
                        return;
                    } else {
                        createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(MyWebView.this.botWebViewContainer.parentActivity, MyWebView.this.botWebViewContainer.resourcesProvider, new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO"}, R.raw.permission_request_camera, LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestCameraMicPermission : R.string.WebViewRequestCameraMicPermission, userName), LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestCameraMicPermissionWithHint : R.string.WebViewRequestCameraMicPermissionWithHint, userName), new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda6
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.3.this.lambda$onPermissionRequest$7(permissionRequest, resources, (Boolean) obj);
                            }
                        });
                    }
                }
                this.lastPermissionsDialog = createWebViewPermissionsRequestDialog;
                createWebViewPermissionsRequestDialog.show();
            }

            @Override // android.webkit.WebChromeClient
            public void onPermissionRequestCanceled(PermissionRequest permissionRequest) {
                if (this.lastPermissionsDialog == null) {
                    MyWebView.this.d("onPermissionRequestCanceled: no dialog");
                    return;
                }
                MyWebView.this.d("onPermissionRequestCanceled: dialog.dismiss");
                this.lastPermissionsDialog.dismiss();
                this.lastPermissionsDialog = null;
            }

            @Override // android.webkit.WebChromeClient
            public void onProgressChanged(WebView webView, int i) {
                if (MyWebView.this.botWebViewContainer == null || MyWebView.this.botWebViewContainer.webViewProgressListener == null) {
                    MyWebView.this.d("onProgressChanged " + i + "%: no container");
                    return;
                }
                MyWebView.this.d("onProgressChanged " + i + "%");
                MyWebView.this.botWebViewContainer.webViewProgressListener.accept(Float.valueOf(((float) i) / 100.0f));
            }

            @Override // android.webkit.WebChromeClient
            public void onReceivedIcon(WebView webView, Bitmap bitmap) {
                String str;
                MyWebView myWebView = MyWebView.this;
                StringBuilder sb = new StringBuilder();
                sb.append("onReceivedIcon favicon=");
                if (bitmap == null) {
                    str = "null";
                } else {
                    str = bitmap.getWidth() + "x" + bitmap.getHeight();
                }
                sb.append(str);
                myWebView.d(sb.toString());
                if (bitmap != null && (!TextUtils.equals(MyWebView.this.getUrl(), MyWebView.this.lastFaviconUrl) || MyWebView.this.lastFavicon == null || bitmap.getWidth() > MyWebView.this.lastFavicon.getWidth())) {
                    MyWebView myWebView2 = MyWebView.this;
                    myWebView2.lastFavicon = bitmap;
                    myWebView2.lastFaviconUrl = myWebView2.getUrl();
                    MyWebView myWebView3 = MyWebView.this;
                    myWebView3.lastFaviconGot = true;
                    myWebView3.saveHistory();
                }
                Bitmap bitmap2 = (Bitmap) MyWebView.this.lastFavicons.get(MyWebView.this.getUrl());
                if (bitmap != null && (bitmap2 == null || bitmap2.getWidth() < bitmap.getWidth())) {
                    MyWebView.this.lastFavicons.put(MyWebView.this.getUrl(), bitmap);
                }
                if (MyWebView.this.botWebViewContainer != null) {
                    MyWebView.this.botWebViewContainer.onFaviconChanged(bitmap);
                }
                super.onReceivedIcon(webView, bitmap);
            }

            @Override // android.webkit.WebChromeClient
            public void onReceivedTitle(WebView webView, String str) {
                MyWebView.this.d("onReceivedTitle title=" + str);
                MyWebView myWebView = MyWebView.this;
                if (!myWebView.errorShown) {
                    myWebView.lastTitleGot = true;
                    myWebView.lastTitle = str;
                }
                if (myWebView.botWebViewContainer != null) {
                    MyWebView.this.botWebViewContainer.onTitleChanged(str);
                }
                super.onReceivedTitle(webView, str);
            }

            @Override // android.webkit.WebChromeClient
            public void onReceivedTouchIconUrl(WebView webView, String str, boolean z) {
                MyWebView.this.d("onReceivedTouchIconUrl url=" + str + " precomposed=" + z);
                super.onReceivedTouchIconUrl(webView, str, z);
            }

            @Override // android.webkit.WebChromeClient
            public boolean onShowFileChooser(WebView webView, ValueCallback valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                Intent createChooser;
                MyWebView myWebView;
                String str;
                Activity findActivity = AndroidUtilities.findActivity(MyWebView.this.getContext());
                if (findActivity == null) {
                    myWebView = MyWebView.this;
                    str = "onShowFileChooser: no activity, false";
                } else {
                    if (MyWebView.this.botWebViewContainer != null) {
                        if (MyWebView.this.botWebViewContainer.mFilePathCallback != null) {
                            MyWebView.this.botWebViewContainer.mFilePathCallback.onReceiveValue(null);
                        }
                        MyWebView.this.botWebViewContainer.mFilePathCallback = valueCallback;
                        if (Build.VERSION.SDK_INT >= 21) {
                            createChooser = fileChooserParams.createIntent();
                        } else {
                            Intent intent = new Intent("android.intent.action.GET_CONTENT");
                            intent.addCategory("android.intent.category.OPENABLE");
                            intent.setType("*/*");
                            createChooser = Intent.createChooser(intent, LocaleController.getString(R.string.BotWebViewFileChooserTitle));
                        }
                        findActivity.startActivityForResult(createChooser, 3000);
                        MyWebView.this.d("onShowFileChooser: true");
                        return true;
                    }
                    myWebView = MyWebView.this;
                    str = "onShowFileChooser: no container, false";
                }
                myWebView.d(str);
                return false;
            }
        }

        class 5 implements DownloadListener {
            5() {
            }

            private String getFilename(String str, String str2, String str3) {
                try {
                    String str4 = Uri.parse(str).getPathSegments().get(r0.size() - 1);
                    int lastIndexOf = str4.lastIndexOf(".");
                    if (lastIndexOf > 0) {
                        if (!TextUtils.isEmpty(str4.substring(lastIndexOf + 1))) {
                            return str4;
                        }
                    }
                } catch (Exception unused) {
                }
                return URLUtil.guessFileName(str, str2, str3);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onDownloadStart$0(String str, String str2, String str3, String str4) {
                try {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
                    request.setMimeType(str2);
                    request.addRequestHeader("User-Agent", str3);
                    request.setDescription(LocaleController.getString(R.string.WebDownloading));
                    request.setTitle(str4);
                    request.setNotificationVisibility(1);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, str4);
                    DownloadManager downloadManager = (DownloadManager) MyWebView.this.getContext().getSystemService("download");
                    if (downloadManager != null) {
                        downloadManager.enqueue(request);
                    }
                    if (MyWebView.this.botWebViewContainer != null) {
                        BulletinFactory.of(MyWebView.this.botWebViewContainer, MyWebView.this.botWebViewContainer.resourcesProvider).createSimpleBulletin(R.raw.ic_download, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.WebDownloadingFile, str4))).show(true);
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }

            @Override // android.webkit.DownloadListener
            public void onDownloadStart(final String str, final String str2, String str3, final String str4, long j) {
                MyWebView.this.d("onDownloadStart " + str + " " + str2 + " " + str3 + " " + str4 + " " + j);
                try {
                    if (str.startsWith("blob:")) {
                        return;
                    }
                    final String filename = getFilename(str, str3, str4);
                    final Runnable runnable = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$5$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            BotWebViewContainer.MyWebView.5.this.lambda$onDownloadStart$0(str, str4, str2, filename);
                        }
                    };
                    if (DownloadController.getInstance(UserConfig.selectedAccount).canDownloadMedia(8, j)) {
                        runnable.run();
                        return;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyWebView.this.getContext());
                    builder.setTitle(LocaleController.getString(R.string.WebDownloadAlertTitle));
                    builder.setMessage(AndroidUtilities.replaceTags(j > 0 ? LocaleController.formatString(R.string.WebDownloadAlertInfoWithSize, filename, AndroidUtilities.formatFileSize(j)) : LocaleController.formatString(R.string.WebDownloadAlertInfo, filename)));
                    builder.setPositiveButton(LocaleController.getString(R.string.WebDownloadAlertYes), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$5$$ExternalSyntheticLambda1
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            runnable.run();
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                    TextView textView = (TextView) builder.show().getButton(-2);
                    if (textView != null) {
                        textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }

        public MyWebView(Context context, boolean z) {
            super(context);
            this.tag = BotWebViewContainer.access$1508();
            this.urlFallback = "about:blank";
            this.lastFavicons = new HashMap();
            this.bot = z;
            d("created new webview " + this);
            setOnLongClickListener(new 1());
            setWebViewClient(new 2(z, context));
            setWebChromeClient(new 3(z));
            setFindListener(new WebView.FindListener() { // from class: org.telegram.ui.web.BotWebViewContainer.MyWebView.4
                @Override // android.webkit.WebView.FindListener
                public void onFindResultReceived(int i, int i2, boolean z2) {
                    MyWebView.this.searchIndex = i;
                    MyWebView.this.searchCount = i2;
                    MyWebView.this.searchLoading = !z2;
                    if (MyWebView.this.searchListener != null) {
                        MyWebView.this.searchListener.run();
                    }
                }
            });
            if (z) {
                return;
            }
            setDownloadListener(new 5());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$evaluateJS$1(String str) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void saveHistory() {
            if (this.bot) {
                return;
            }
            WebMetadataCache.WebMetadata from = WebMetadataCache.WebMetadata.from(this);
            WebMetadataCache.getInstance().save(from);
            BrowserHistory.Entry entry = this.currentHistoryEntry;
            if (entry == null || from == null) {
                return;
            }
            entry.meta = from;
            BrowserHistory.pushHistory(entry);
        }

        public boolean applyCachedMeta(WebMetadataCache.WebMetadata webMetadata) {
            boolean z = false;
            if (webMetadata == null) {
                return false;
            }
            BotWebViewContainer botWebViewContainer = this.botWebViewContainer;
            if (botWebViewContainer != null && botWebViewContainer.delegate != null) {
                if (webMetadata.actionBarColor != 0) {
                    this.botWebViewContainer.delegate.onWebAppBackgroundChanged(true, webMetadata.actionBarColor);
                    this.lastActionBarColorGot = true;
                }
                int i = webMetadata.backgroundColor;
                if (i != 0) {
                    this.botWebViewContainer.delegate.onWebAppBackgroundChanged(false, webMetadata.backgroundColor);
                    this.lastBackgroundColorGot = true;
                } else {
                    i = -1;
                }
                Bitmap bitmap = webMetadata.favicon;
                if (bitmap != null) {
                    BotWebViewContainer botWebViewContainer2 = this.botWebViewContainer;
                    this.lastFavicon = bitmap;
                    botWebViewContainer2.onFaviconChanged(bitmap);
                    this.lastFaviconGot = true;
                }
                if (!TextUtils.isEmpty(webMetadata.sitename)) {
                    String str = webMetadata.sitename;
                    this.lastSiteName = str;
                    BotWebViewContainer botWebViewContainer3 = this.botWebViewContainer;
                    this.lastTitle = str;
                    botWebViewContainer3.onTitleChanged(str);
                    z = true;
                }
                if (SharedConfig.adaptableColorInBrowser) {
                    setBackgroundColor(i);
                }
            }
            if (!z) {
                setTitle(null);
                BotWebViewContainer botWebViewContainer4 = this.botWebViewContainer;
                if (botWebViewContainer4 != null) {
                    botWebViewContainer4.onTitleChanged(null);
                }
            }
            return true;
        }

        @Override // android.webkit.WebView
        public boolean canGoBack() {
            return super.canGoBack();
        }

        public void checkCachedMetaProperties(String str) {
            if (this.bot) {
                return;
            }
            applyCachedMeta(WebMetadataCache.getInstance().get(AndroidUtilities.getHostAuthority(str, true)));
        }

        @Override // android.webkit.WebView
        public void clearHistory() {
            d("clearHistory");
            super.clearHistory();
        }

        public void d(String str) {
            FileLog.d("[webview] #" + this.tag + " " + str);
        }

        @Override // android.webkit.WebView
        public void destroy() {
            d("destroy");
            super.destroy();
        }

        @Override // android.webkit.WebView, android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            super.draw(canvas);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            return super.drawChild(canvas, view, j);
        }

        public void evaluateJS(String str) {
            evaluateJavascript(str, new ValueCallback() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$$ExternalSyntheticLambda0
                @Override // android.webkit.ValueCallback
                public final void onReceiveValue(Object obj) {
                    BotWebViewContainer.MyWebView.lambda$evaluateJS$1((String) obj);
                }
            });
        }

        @Override // android.webkit.WebView
        public Bitmap getFavicon() {
            if (this.errorShown) {
                return null;
            }
            return this.lastFavicon;
        }

        public Bitmap getFavicon(String str) {
            return (Bitmap) this.lastFavicons.get(str);
        }

        public String getOpenURL() {
            return this.openedByUrl;
        }

        public float getScrollProgress() {
            float max = Math.max(1, computeVerticalScrollRange() - computeVerticalScrollExtent());
            if (max <= getHeight()) {
                return 0.0f;
            }
            return Utilities.clamp01(getScrollY() / max);
        }

        public int getSearchCount() {
            return this.searchCount;
        }

        public int getSearchIndex() {
            return this.searchIndex;
        }

        @Override // android.webkit.WebView
        public String getTitle() {
            return this.lastTitle;
        }

        @Override // android.webkit.WebView
        public String getUrl() {
            if (this.dangerousUrl) {
                return this.urlFallback;
            }
            String url = super.getUrl();
            this.lastUrl = url;
            return url;
        }

        @Override // android.webkit.WebView
        public void goBack() {
            d("goBack");
            super.goBack();
        }

        @Override // android.webkit.WebView
        public void goForward() {
            d("goForward");
            super.goForward();
        }

        public boolean isPageLoaded() {
            return this.isPageLoaded;
        }

        public boolean isUrlDangerous() {
            return this.dangerousUrl;
        }

        @Override // android.webkit.WebView
        public void loadData(String str, String str2, String str3) {
            this.openedByUrl = null;
            d("loadData " + str + " " + str2 + " " + str3);
            super.loadData(str, str2, str3);
        }

        @Override // android.webkit.WebView
        public void loadDataWithBaseURL(String str, String str2, String str3, String str4, String str5) {
            this.openedByUrl = null;
            d("loadDataWithBaseURL " + str + " " + str2 + " " + str3 + " " + str4 + " " + str5);
            super.loadDataWithBaseURL(str, str2, str3, str4, str5);
        }

        @Override // android.webkit.WebView
        public void loadUrl(String str) {
            BottomSheet bottomSheet = this.currentSheet;
            if (bottomSheet != null) {
                bottomSheet.dismiss();
                this.currentSheet = null;
            }
            checkCachedMetaProperties(str);
            this.openedByUrl = str;
            String str2 = BotWebViewContainer.tonsite2magic(str);
            this.currentUrl = str2;
            d("loadUrl " + str2);
            super.loadUrl(str2);
            BotWebViewContainer botWebViewContainer = this.botWebViewContainer;
            if (botWebViewContainer != null) {
                if (this.dangerousUrl) {
                    str2 = this.urlFallback;
                }
                botWebViewContainer.onURLChanged(str2, !canGoBack(), !canGoForward());
            }
        }

        @Override // android.webkit.WebView
        public void loadUrl(String str, Map map) {
            BottomSheet bottomSheet = this.currentSheet;
            if (bottomSheet != null) {
                bottomSheet.dismiss();
                this.currentSheet = null;
            }
            checkCachedMetaProperties(str);
            this.openedByUrl = str;
            String str2 = BotWebViewContainer.tonsite2magic(str);
            this.currentUrl = str2;
            d("loadUrl " + str2 + " " + map);
            super.loadUrl(str2, (Map<String, String>) map);
            BotWebViewContainer botWebViewContainer = this.botWebViewContainer;
            if (botWebViewContainer != null) {
                if (this.dangerousUrl) {
                    str2 = this.urlFallback;
                }
                botWebViewContainer.onURLChanged(str2, !canGoBack(), !canGoForward());
            }
        }

        public void loadUrl(String str, WebMetadataCache.WebMetadata webMetadata) {
            BottomSheet bottomSheet = this.currentSheet;
            if (bottomSheet != null) {
                bottomSheet.dismiss();
                this.currentSheet = null;
            }
            applyCachedMeta(webMetadata);
            this.openedByUrl = str;
            String str2 = BotWebViewContainer.tonsite2magic(str);
            this.currentUrl = str2;
            d("loadUrl " + str2 + " with cached meta");
            super.loadUrl(str2);
            BotWebViewContainer botWebViewContainer = this.botWebViewContainer;
            if (botWebViewContainer != null) {
                if (this.dangerousUrl) {
                    str2 = this.urlFallback;
                }
                botWebViewContainer.onURLChanged(str2, !canGoBack(), !canGoForward());
            }
        }

        @Override // android.webkit.WebView, android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            d("attached");
            AndroidUtilities.checkAndroidTheme(getContext(), true);
            super.onAttachedToWindow();
        }

        @Override // android.webkit.WebView, android.view.View
        public boolean onCheckIsTextEditor() {
            BotWebViewContainer botWebViewContainer = this.botWebViewContainer;
            if (botWebViewContainer == null) {
                d("onCheckIsTextEditor: no container");
                return false;
            }
            boolean isFocusable = botWebViewContainer.isFocusable();
            d("onCheckIsTextEditor: " + isFocusable);
            return isFocusable;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            d("detached");
            AndroidUtilities.checkAndroidTheme(getContext(), false);
            super.onDetachedFromWindow();
        }

        @Override // android.webkit.WebView, android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

        @Override // android.webkit.WebView, android.widget.AbsoluteLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824));
        }

        @Override // android.webkit.WebView
        public void onPause() {
            d("onPause");
            super.onPause();
        }

        @Override // android.webkit.WebView
        public void onResume() {
            d("onResume");
            super.onResume();
        }

        @Override // android.webkit.WebView, android.view.View
        protected void onScrollChanged(int i, int i2, int i3, int i4) {
            super.onScrollChanged(i, i2, i3, i4);
            WebViewScrollListener webViewScrollListener = this.webViewScrollListener;
            if (webViewScrollListener != null) {
                webViewScrollListener.onWebViewScrolled(this, getScrollX() - this.prevScrollX, getScrollY() - this.prevScrollY);
            }
            this.prevScrollX = getScrollX();
            this.prevScrollY = getScrollY();
        }

        @Override // android.webkit.WebView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                this.botWebViewContainer.lastClickMs = System.currentTimeMillis();
                getSettings().setMediaPlaybackRequiresUserGesture(false);
            }
            return super.onTouchEvent(motionEvent);
        }

        @Override // android.webkit.WebView
        public void pauseTimers() {
            d("pauseTimers");
            super.pauseTimers();
        }

        @Override // android.webkit.WebView
        public void postUrl(String str, byte[] bArr) {
            d("postUrl " + str + " " + bArr);
            super.postUrl(str, bArr);
        }

        @Override // android.webkit.WebView
        public void reload() {
            if (Build.VERSION.SDK_INT >= 21) {
                CookieManager.getInstance().flush();
            }
            d("reload");
            super.reload();
        }

        @Override // android.webkit.WebView
        public void resumeTimers() {
            d("resumeTimers");
            super.resumeTimers();
        }

        public void search(String str, Runnable runnable) {
            this.searchLoading = true;
            this.searchListener = runnable;
            findAllAsync(str);
        }

        public void setCloseListener(Runnable runnable) {
            this.onCloseListener = runnable;
        }

        public void setContainers(BotWebViewContainer botWebViewContainer, WebViewScrollListener webViewScrollListener) {
            d("setContainers(" + botWebViewContainer + ", " + webViewScrollListener + ")");
            boolean z = this.botWebViewContainer == null && botWebViewContainer != null;
            this.botWebViewContainer = botWebViewContainer;
            this.webViewScrollListener = webViewScrollListener;
            if (z) {
                evaluateJS("window.__tg__postBackgroundChange()");
            }
        }

        @Override // android.view.View
        public void setFocusable(int i) {
            d("setFocusable " + i);
            super.setFocusable(i);
        }

        @Override // android.view.View
        public void setFocusable(boolean z) {
            d("setFocusable " + z);
            super.setFocusable(z);
        }

        @Override // android.view.View
        public void setFocusableInTouchMode(boolean z) {
            d("setFocusableInTouchMode " + z);
            super.setFocusableInTouchMode(z);
        }

        @Override // android.view.View
        public void setFocusedByDefault(boolean z) {
            d("setFocusedByDefault " + z);
            super.setFocusedByDefault(z);
        }

        public void setScrollProgress(float f) {
            setScrollY((int) (f * Math.max(1, computeVerticalScrollRange() - computeVerticalScrollExtent())));
        }

        @Override // android.view.View
        public void setScrollX(int i) {
            super.setScrollX(i);
            this.prevScrollX = i;
        }

        @Override // android.view.View
        public void setScrollY(int i) {
            super.setScrollY(i);
            this.prevScrollY = i;
        }

        public void setTitle(String str) {
            this.lastTitle = str;
        }

        @Override // android.webkit.WebView
        public void stopLoading() {
            d("stopLoading");
            super.stopLoading();
        }

        @Override // android.view.View
        public void stopNestedScroll() {
            d("stopNestedScroll");
            super.stopNestedScroll();
        }
    }

    public static final class PopupButton {
        public String id;
        public String text;
        public int textColorKey;

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        public PopupButton(JSONObject jSONObject) {
            int i;
            String string;
            char c = 65535;
            this.textColorKey = -1;
            this.id = jSONObject.getString("id");
            String string2 = jSONObject.getString("type");
            switch (string2.hashCode()) {
                case -1829997182:
                    if (string2.equals("destructive")) {
                        c = 5;
                        break;
                    }
                    break;
                case -1367724422:
                    if (string2.equals("cancel")) {
                        c = 4;
                        break;
                    }
                    break;
                case 3548:
                    if (string2.equals("ok")) {
                        c = 2;
                        break;
                    }
                    break;
                case 94756344:
                    if (string2.equals("close")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1544803905:
                    if (string2.equals("default")) {
                        c = 1;
                        break;
                    }
                    break;
            }
            if (c == 2) {
                i = R.string.OK;
            } else if (c == 3) {
                i = R.string.Close;
            } else {
                if (c != 4) {
                    if (c == 5) {
                        this.textColorKey = Theme.key_text_RedBold;
                    }
                    string = jSONObject.getString("text");
                    this.text = string;
                }
                i = R.string.Cancel;
            }
            string = LocaleController.getString(i);
            this.text = string;
        }
    }

    public static class WebViewProxy {
        public BotWebViewContainer container;
        public final MyWebView webView;

        public WebViewProxy(MyWebView myWebView, BotWebViewContainer botWebViewContainer) {
            this.webView = myWebView;
            this.container = botWebViewContainer;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$post$0(String str, String str2) {
            BotWebViewContainer botWebViewContainer = this.container;
            if (botWebViewContainer == null) {
                return;
            }
            botWebViewContainer.onWebEventReceived(str, str2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resolveShare$1(Boolean bool) {
            MyWebView myWebView = this.webView;
            StringBuilder sb = new StringBuilder();
            sb.append("window.navigator.__share__receive(");
            sb.append(bool.booleanValue() ? "" : "'abort'");
            sb.append(")");
            myWebView.evaluateJS(sb.toString());
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:33:0x007c  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x0083  */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0091  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x00b1  */
        /* JADX WARN: Removed duplicated region for block: B:87:0x0154  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$resolveShare$2(String str, byte[] bArr, String str2, String str3) {
            String str4;
            String str5;
            String str6;
            String str7;
            JSONObject jSONObject;
            LaunchActivity launchActivity;
            if (this.container == null) {
                return;
            }
            if (System.currentTimeMillis() - this.container.lastClickMs > 10000) {
                this.webView.evaluateJS("window.navigator.__share__receive(\"security\")");
                return;
            }
            this.container.lastClickMs = 0L;
            Context context = this.webView.getContext();
            Activity findActivity = AndroidUtilities.findActivity(context);
            if (findActivity == null && (launchActivity = LaunchActivity.instance) != null) {
                findActivity = launchActivity;
            }
            if (context == null || findActivity == null || !(findActivity instanceof LaunchActivity) || findActivity.isFinishing() || !this.webView.isAttachedToWindow()) {
                this.webView.evaluateJS("window.navigator.__share__receive(\"security\")");
                return;
            }
            LaunchActivity launchActivity2 = (LaunchActivity) findActivity;
            File file = null;
            try {
                jSONObject = new JSONObject(str);
                str4 = jSONObject.optString("url", null);
                try {
                    str5 = jSONObject.optString("text", null);
                } catch (Exception e) {
                    e = e;
                    str5 = null;
                }
            } catch (Exception e2) {
                e = e2;
                str4 = null;
                str5 = null;
            }
            try {
                str6 = jSONObject.optString("title", null);
            } catch (Exception e3) {
                e = e3;
                FileLog.e(e);
                str6 = null;
                StringBuilder sb = new StringBuilder();
                if (str6 != null) {
                }
                if (str5 != null) {
                }
                if (str4 != null) {
                }
                Intent intent = new Intent("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", sb.toString());
                if (bArr == null) {
                }
                launchActivity2.whenWebviewShareAPIDone(new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        BotWebViewContainer.WebViewProxy.this.lambda$resolveShare$1((Boolean) obj);
                    }
                });
                launchActivity2.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.ShareFile)), 521);
            }
            StringBuilder sb2 = new StringBuilder();
            if (str6 != null) {
                sb2.append(str6);
            }
            if (str5 != null) {
                if (sb2.length() > 0) {
                    sb2.append("\n");
                }
                sb2.append(str5);
            }
            if (str4 != null) {
                if (sb2.length() > 0) {
                    sb2.append("\n");
                }
                sb2.append(str4);
            }
            Intent intent2 = new Intent("android.intent.action.SEND");
            intent2.putExtra("android.intent.extra.TEXT", sb2.toString());
            if (bArr == null) {
                int i = 0;
                while (true) {
                    if (file == null || file.exists()) {
                        File directory = FileLoader.getDirectory(4);
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(FileLoader.fixFileName(str2 == null ? "file" : str2));
                        if (i > 0) {
                            str7 = " (" + i + ")";
                        } else {
                            str7 = "";
                        }
                        sb3.append(str7);
                        file = new File(directory, sb3.toString());
                        i++;
                    } else {
                        try {
                            break;
                        } catch (Exception e4) {
                            FileLog.e(e4);
                        }
                    }
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bArr);
                fileOutputStream.close();
                try {
                    if (str3 == null) {
                        intent2.setType("text/plain");
                    } else {
                        intent2.setType(str3);
                    }
                    if (str2 != null) {
                        intent2.putExtra("android.intent.extra.TITLE", str2);
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        try {
                            intent2.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(launchActivity2, ApplicationLoader.getApplicationId() + ".provider", file));
                            intent2.setFlags(1);
                        } catch (Exception unused) {
                        }
                    }
                    intent2.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                } catch (Exception e5) {
                    FileLog.e(e5);
                }
            } else {
                intent2.setType("text/plain");
            }
            launchActivity2.whenWebviewShareAPIDone(new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda2
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    BotWebViewContainer.WebViewProxy.this.lambda$resolveShare$1((Boolean) obj);
                }
            });
            launchActivity2.startActivityForResult(Intent.createChooser(intent2, LocaleController.getString(R.string.ShareFile)), 521);
        }

        @JavascriptInterface
        public void post(final String str, final String str2) {
            if (this.container == null) {
                return;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.WebViewProxy.this.lambda$post$0(str, str2);
                }
            });
        }

        @JavascriptInterface
        public void resolveShare(final String str, final byte[] bArr, final String str2, final String str3) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.WebViewProxy.this.lambda$resolveShare$2(str, bArr, str2, str3);
                }
            });
        }

        public void setContainer(BotWebViewContainer botWebViewContainer) {
            this.container = botWebViewContainer;
        }
    }

    public interface WebViewScrollListener {
        void onWebViewScrolled(WebView webView, int i, int i2);
    }

    public BotWebViewContainer(Context context, Theme.ResourcesProvider resourcesProvider, int i, boolean z) {
        super(context);
        CellFlickerDrawable cellFlickerDrawable = new CellFlickerDrawable();
        this.flickerDrawable = cellFlickerDrawable;
        int i2 = Theme.key_featuredStickers_addButton;
        this.lastButtonColor = getColor(i2);
        int i3 = Theme.key_featuredStickers_buttonText;
        this.lastButtonTextColor = getColor(i3);
        this.lastButtonText = "";
        this.lastSecondaryButtonColor = getColor(i2);
        this.lastSecondaryButtonTextColor = getColor(i3);
        this.lastSecondaryButtonText = "";
        this.lastSecondaryButtonPosition = "";
        this.currentAccount = UserConfig.selectedAccount;
        this.forceHeight = -1;
        this.lastInsets = new Rect(0, 0, 0, 0);
        this.lastInsetsTopMargin = 0;
        this.notifyLocationChecked = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$new$47();
            }
        };
        this.lastDialogType = -1;
        this.shownDialogsCount = 0;
        int i4 = tags;
        tags = i4 + 1;
        this.tag = i4;
        this.bot = z;
        this.resourcesProvider = resourcesProvider;
        d("created new webview container");
        if (context instanceof Activity) {
            this.parentActivity = (Activity) context;
        }
        cellFlickerDrawable.drawFrame = false;
        cellFlickerDrawable.setColors(i, NotificationCenter.recordStopped, NotificationCenter.groupPackUpdated);
        BackupImageView backupImageView = new BackupImageView(context) { // from class: org.telegram.ui.web.BotWebViewContainer.1

            class 1 extends ImageReceiver {
                1(View view) {
                    super(view);
                }

                /* JADX INFO: Access modifiers changed from: private */
                public /* synthetic */ void lambda$setImageBitmapByKey$0(ValueAnimator valueAnimator) {
                    ((BackupImageView) 1.this).imageReceiver.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    invalidate();
                }

                @Override // org.telegram.messenger.ImageReceiver
                protected boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z, int i2) {
                    boolean imageBitmapByKey = super.setImageBitmapByKey(drawable, str, i, z, i2);
                    ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(300L);
                    duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.web.BotWebViewContainer$1$1$$ExternalSyntheticLambda0
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            BotWebViewContainer.1.1.this.lambda$setImageBitmapByKey$0(valueAnimator);
                        }
                    });
                    duration.start();
                    return imageBitmapByKey;
                }
            }

            {
                this.imageReceiver = new 1(this);
            }

            @Override // org.telegram.ui.Components.BackupImageView, android.view.View
            protected void onDraw(Canvas canvas) {
                if (BotWebViewContainer.this.isFlickeringCenter) {
                    super.onDraw(canvas);
                    return;
                }
                if (this.imageReceiver.getDrawable() != null) {
                    this.imageReceiver.setImageCoords(0.0f, 0.0f, getWidth(), r0.getIntrinsicHeight() * (getWidth() / r0.getIntrinsicWidth()));
                    this.imageReceiver.draw(canvas);
                }
            }
        };
        this.flickerView = backupImageView;
        int color = getColor(Theme.key_bot_loadingIcon);
        this.flickerViewColor = color;
        backupImageView.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        this.flickerView.getImageReceiver().setAspectFit(true);
        addView(this.flickerView, LayoutHelper.createFrame(-1, -2, 48));
        TextView textView = new TextView(context);
        this.webViewNotAvailableText = textView;
        textView.setText(LocaleController.getString(R.string.BotWebViewNotAvailablePlaceholder));
        this.webViewNotAvailableText.setTextColor(getColor(Theme.key_windowBackgroundWhiteGrayText));
        this.webViewNotAvailableText.setTextSize(1, 15.0f);
        this.webViewNotAvailableText.setGravity(17);
        this.webViewNotAvailableText.setVisibility(8);
        int dp = AndroidUtilities.dp(16.0f);
        this.webViewNotAvailableText.setPadding(dp, dp, dp, dp);
        addView(this.webViewNotAvailableText, LayoutHelper.createFrame(-1, -2, 17));
        setFocusable(false);
    }

    static /* synthetic */ int access$1508() {
        int i = tags;
        tags = i + 1;
        return i;
    }

    private JSONObject buildThemeParams() {
        try {
            JSONObject makeThemeParams = BotWebViewSheet.makeThemeParams(this.resourcesProvider);
            if (makeThemeParams != null) {
                return new JSONObject().put("theme_params", makeThemeParams);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return new JSONObject();
    }

    private static String capitalizeFirst(String str) {
        if (str == null) {
            return "";
        }
        if (str.length() <= 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private boolean checkPermissions(String[] strArr) {
        int checkSelfPermission;
        for (String str : strArr) {
            checkSelfPermission = getContext().checkSelfPermission(str);
            if (checkSelfPermission != 0) {
                return false;
            }
        }
        return true;
    }

    private void createBiometry() {
        if (this.botUser == null) {
            return;
        }
        BotBiometry botBiometry = this.biometry;
        if (botBiometry == null) {
            this.biometry = BotBiometry.get(getContext(), this.currentAccount, this.botUser.id);
        } else {
            botBiometry.load();
        }
    }

    private void error(String str) {
        BulletinFactory.of(this, this.resourcesProvider).createSimpleBulletin(R.raw.error, str).show();
    }

    private int getColor(int i) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        return resourcesProvider != null ? resourcesProvider.getColor(i) : Theme.getColor(i);
    }

    public static int getMainButtonRippleColor(int i) {
        return ColorUtils.calculateLuminance(i) >= 0.30000001192092896d ? 301989888 : 385875967;
    }

    public static Drawable getMainButtonRippleDrawable(int i) {
        return Theme.createSelectorWithBackgroundDrawable(i, getMainButtonRippleColor(i));
    }

    private boolean ignoreDialog(int i) {
        if (this.currentDialog != null) {
            return true;
        }
        if (this.blockedDialogsUntil > 0 && System.currentTimeMillis() < this.blockedDialogsUntil) {
            return true;
        }
        if (this.lastDialogType != i || this.shownDialogsCount <= 3) {
            return false;
        }
        this.blockedDialogsUntil = System.currentTimeMillis() + 3000;
        this.shownDialogsCount = 0;
        return true;
    }

    public static boolean isTonsite(Uri uri) {
        if ("tonsite".equals(uri.getScheme())) {
            return true;
        }
        String authority = uri.getAuthority();
        if (authority == null && uri.getScheme() == null) {
            authority = Uri.parse("http://" + uri.toString()).getAuthority();
        }
        return authority != null && (authority.endsWith(".ton") || authority.endsWith(".adnl"));
    }

    public static boolean isTonsite(String str) {
        return str != null && isTonsite(Uri.parse(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$evaluateJs$3(boolean z, String str) {
        if (z) {
            checkCreateWebView();
        }
        MyWebView myWebView = this.webView;
        if (myWebView == null) {
            return;
        }
        myWebView.evaluateJS(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadUrl$2(String str) {
        this.isPageLoaded = false;
        this.lastClickMs = 0L;
        this.hasUserPermissions = false;
        this.mUrl = str;
        checkCreateWebView();
        MyWebView myWebView = this.webView;
        if (myWebView != null) {
            myWebView.onResume();
            this.webView.loadUrl(str);
        }
        updateKeyboardFocusable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$47() {
        notifyEvent("location_checked", this.location.checkObject());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$notifyEvent$4(MyWebView myWebView, String str, JSONObject jSONObject) {
        myWebView.evaluateJS("window.Telegram.WebView.receiveEvent('" + str + "', " + jSONObject + ");");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$10(final String str, final TLRPC.TL_inputInvoiceSlug tL_inputInvoiceSlug, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$9(tL_error, str, tL_inputInvoiceSlug, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$11(TLObject tLObject, String[] strArr, TLRPC.TL_error tL_error, DialogInterface dialogInterface) {
        if (tLObject != null) {
            strArr[0] = "allowed";
            if (tLObject instanceof TLRPC.Updates) {
                MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC.Updates) tLObject, false);
            }
        }
        if (tL_error != null) {
            unknownError(tL_error.text);
        }
        dialogInterface.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$12(final String[] strArr, final DialogInterface dialogInterface, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda50
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$11(tLObject, strArr, tL_error, dialogInterface);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$13(final String[] strArr, final DialogInterface dialogInterface, int i) {
        TL_bots.allowSendMessage allowsendmessage = new TL_bots.allowSendMessage();
        allowsendmessage.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botUser);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(allowsendmessage, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda49
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                BotWebViewContainer.this.lambda$onEventReceived$12(strArr, dialogInterface, tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onEventReceived$15(String[] strArr, int i, MyWebView myWebView) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", strArr[0]);
            notifyEvent(i, myWebView, "write_access_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$16(TLObject tLObject, final int i, final MyWebView myWebView, TLRPC.TL_error tL_error) {
        if (!(tLObject instanceof TLRPC.TL_boolTrue)) {
            if (tL_error != null) {
                unknownError(tL_error.text);
                return;
            } else {
                final String[] strArr = {"cancelled"};
                showDialog(3, new AlertDialog.Builder(getContext()).setTitle(LocaleController.getString(R.string.BotWebViewRequestWriteTitle)).setMessage(LocaleController.getString(R.string.BotWebViewRequestWriteMessage)).setPositiveButton(LocaleController.getString(R.string.BotWebViewRequestAllow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda43
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        BotWebViewContainer.this.lambda$onEventReceived$13(strArr, dialogInterface, i2);
                    }
                }).setNegativeButton(LocaleController.getString(R.string.BotWebViewRequestDontAllow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda44
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        dialogInterface.dismiss();
                    }
                }).create(), new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda45
                    @Override // java.lang.Runnable
                    public final void run() {
                        BotWebViewContainer.lambda$onEventReceived$15(strArr, i, myWebView);
                    }
                });
                return;
            }
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", "allowed");
            notifyEvent(i, myWebView, "write_access_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$17(final int i, final MyWebView myWebView, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$16(tLObject, i, myWebView, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$18(String str, TLObject tLObject, TLRPC.TL_error tL_error, int i, MyWebView myWebView) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("req_id", str);
            if (tLObject instanceof TLRPC.TL_dataJSON) {
                jSONObject.put("result", new JSONTokener(((TLRPC.TL_dataJSON) tLObject).data).nextValue());
            } else if (tL_error != null) {
                jSONObject.put("error", tL_error.text);
            }
            notifyEvent(i, myWebView, "custom_method_invoked", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
            unknownError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$19(final String str, final int i, final MyWebView myWebView, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$18(str, tLObject, tL_error, i, myWebView);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$20(int i, MyWebView myWebView) {
        SendMessagesHelper.getInstance(this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(UserConfig.getInstance(this.currentAccount).getCurrentUser(), this.botUser.id, (MessageObject) null, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0));
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", "sent");
            notifyEvent(i, myWebView, "phone_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$21(String[] strArr, boolean z, final int i, final MyWebView myWebView, DialogInterface dialogInterface, int i2) {
        strArr[0] = null;
        dialogInterface.dismiss();
        int i3 = this.currentAccount;
        if (z) {
            MessagesController.getInstance(i3).unblockPeer(this.botUser.id, new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.this.lambda$onEventReceived$20(i, myWebView);
                }
            });
            return;
        }
        SendMessagesHelper.getInstance(i3).sendMessage(SendMessagesHelper.SendMessageParams.of(UserConfig.getInstance(this.currentAccount).getCurrentUser(), this.botUser.id, (MessageObject) null, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0));
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", "sent");
            notifyEvent(i, myWebView, "phone_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onEventReceived$23(String[] strArr, int i, MyWebView myWebView) {
        if (strArr[0] == null) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", strArr[0]);
            notifyEvent(i, myWebView, "phone_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$24() {
        BotBiometry botBiometry = this.biometry;
        botBiometry.access_requested = true;
        botBiometry.save();
        notifyBiometryReceived();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$25(Boolean bool, String str) {
        if (bool.booleanValue()) {
            BotBiometry botBiometry = this.biometry;
            botBiometry.access_granted = true;
            botBiometry.save();
        }
        notifyBiometryReceived();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$26(Runnable[] runnableArr, DialogInterface dialogInterface, int i) {
        if (runnableArr[0] != null) {
            runnableArr[0] = null;
        }
        BotBiometry botBiometry = this.biometry;
        botBiometry.access_requested = true;
        botBiometry.save();
        this.biometry.requestToken(null, new Utilities.Callback2() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda31
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                BotWebViewContainer.this.lambda$onEventReceived$25((Boolean) obj, (String) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$27(Runnable[] runnableArr, DialogInterface dialogInterface, int i) {
        if (runnableArr[0] != null) {
            runnableArr[0] = null;
        }
        BotBiometry botBiometry = this.biometry;
        botBiometry.access_requested = true;
        botBiometry.disabled = true;
        botBiometry.save();
        notifyBiometryReceived();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onEventReceived$28(Runnable[] runnableArr, DialogInterface dialogInterface) {
        Runnable runnable = runnableArr[0];
        if (runnable != null) {
            runnable.run();
            runnableArr[0] = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$29(Boolean bool, String str) {
        if (bool.booleanValue()) {
            this.biometry.access_granted = true;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", bool.booleanValue() ? "authorized" : "failed");
            jSONObject.put("token", str);
            notifyEvent("biometry_auth_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$30(String str, Boolean bool) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", bool.booleanValue() ? TextUtils.isEmpty(str) ? "removed" : "updated" : "failed");
            notifyEvent("biometry_token_updated", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$31(int[] iArr, File file, AlertDialog alertDialog, String str, String str2, String str3) {
        StoryRecorder.SourceView sourceView;
        StoryEntry fromPhotoShoot;
        File file2;
        File file3;
        if (iArr[4] > 0) {
            int i = iArr[1];
            int i2 = iArr[2];
            int photoSize = i > AndroidUtilities.getPhotoSize() ? AndroidUtilities.getPhotoSize() : i;
            int photoSize2 = i2 > AndroidUtilities.getPhotoSize() ? AndroidUtilities.getPhotoSize() : i2;
            File makeCacheFile = StoryEntry.makeCacheFile(UserConfig.selectedAccount, "jpg");
            AnimatedFileDrawable animatedFileDrawable = new AnimatedFileDrawable(file, true, 0L, 0, null, null, null, 0L, UserConfig.selectedAccount, true, photoSize, photoSize2, null);
            sourceView = null;
            Bitmap firstFrame = animatedFileDrawable.getFirstFrame(null);
            animatedFileDrawable.recycle();
            if (firstFrame != null) {
                try {
                    file3 = makeCacheFile;
                    firstFrame.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file3));
                } catch (Exception e) {
                    FileLog.e(e);
                    file2 = null;
                }
            } else {
                file3 = makeCacheFile;
            }
            file2 = file3;
            fromPhotoShoot = StoryEntry.fromVideoShoot(file, file2 == null ? null : file2.getAbsolutePath(), iArr[4]);
            fromPhotoShoot.width = i;
            fromPhotoShoot.height = i2;
            fromPhotoShoot.setupMatrix();
        } else {
            sourceView = null;
            fromPhotoShoot = StoryEntry.fromPhotoShoot(file, ((Integer) AndroidUtilities.getImageOrientation(file).first).intValue());
        }
        if (fromPhotoShoot.width <= 0 || fromPhotoShoot.height <= 0) {
            alertDialog.dismissUnless(500L);
            return;
        }
        if (str != null) {
            fromPhotoShoot.caption = str;
        }
        if (!TextUtils.isEmpty(str2) && UserConfig.getInstance(this.currentAccount).isPremium()) {
            if (fromPhotoShoot.mediaEntities == null) {
                fromPhotoShoot.mediaEntities = new ArrayList();
            }
            VideoEditedInfo.MediaEntity mediaEntity = new VideoEditedInfo.MediaEntity();
            mediaEntity.type = (byte) 7;
            mediaEntity.subType = (byte) -1;
            mediaEntity.color = -1;
            LinkPreview.WebPagePreview webPagePreview = new LinkPreview.WebPagePreview();
            mediaEntity.linkSettings = webPagePreview;
            webPagePreview.url = str2;
            if (str3 != null) {
                webPagePreview.flags |= 2;
                webPagePreview.name = str3;
            }
            fromPhotoShoot.mediaEntities.add(mediaEntity);
        }
        StoryRecorder.getInstance(this.parentActivity, UserConfig.selectedAccount).openRepost(sourceView, fromPhotoShoot);
        alertDialog.dismissUnless(500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onEventReceived$32(File file, int[] iArr, Runnable runnable) {
        AnimatedFileDrawable.getVideoInfo(file.getAbsolutePath(), iArr);
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$33(final File file, final AlertDialog alertDialog, final String str, final String str2, final String str3) {
        if (file == null) {
            alertDialog.dismissUnless(500L);
            return;
        }
        final int[] iArr = new int[11];
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$31(iArr, file, alertDialog, str, str2, str3);
            }
        };
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.lambda$onEventReceived$32(file, iArr, runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$34(final AlertDialog alertDialog, final String str, final String str2, final String str3, final File file) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$33(file, alertDialog, str, str2, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$35(Boolean bool) {
        if (bool.booleanValue()) {
            notifyEvent("home_screen_added", null);
        } else {
            notifyEvent("home_screen_failed", obj("error", "UNSUPPORTED"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$36(String str, TLRPC.Document document) {
        if (str != null) {
            notifyEvent("emoji_status_failed", obj("error", str));
            return;
        }
        notifyEvent("emoji_status_set", null);
        Delegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onEmojiStatusSet(document);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$37(Boolean bool, String str) {
        Delegate delegate;
        notifyEmojiStatusAccess(str);
        if (bool.booleanValue() && "allowed".equalsIgnoreCase(str) && (delegate = this.delegate) != null) {
            delegate.onEmojiStatusGranted(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$38(JSONObject jSONObject) {
        notifyEvent("location_requested", jSONObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$39(Boolean bool, Boolean bool2) {
        if (this.delegate != null && bool.booleanValue()) {
            this.delegate.onLocationGranted(bool2.booleanValue());
        }
        this.location.requestObject(new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda39
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                BotWebViewContainer.this.lambda$onEventReceived$38((JSONObject) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$40(JSONObject jSONObject) {
        notifyEvent("location_requested", jSONObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$41(String str, String str2, Boolean bool) {
        String str3;
        if (bool.booleanValue()) {
            this.downloads.download(str, str2);
            str3 = "downloading";
        } else {
            str3 = "cancelled";
        }
        notifyEvent("file_download_requested", obj("status", str3));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$42(TLObject tLObject, final String str, final String str2) {
        if (tLObject instanceof TLRPC.TL_boolTrue) {
            BotDownloads.showAlert(getContext(), str, str2, UserObject.getUserName(this.botUser), new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda48
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    BotWebViewContainer.this.lambda$onEventReceived$41(str, str2, (Boolean) obj);
                }
            });
        } else {
            notifyEvent("file_download_requested", obj("status", "cancelled"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$43(final String str, final String str2, final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda38
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$42(tLObject, str, str2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$44() {
        Delegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onCloseToTabs();
        }
        LaunchActivity.dismissAllWeb();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onEventReceived$45(BotWebViewProxy botWebViewProxy, ArrayList arrayList) {
        BotWebViewContainer botWebViewContainer;
        Delegate delegate;
        if (botWebViewProxy == null || (botWebViewContainer = botWebViewProxy.container) == null || (delegate = botWebViewContainer.delegate) == null) {
            return;
        }
        delegate.onSharedTo(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$46(final BotWebViewProxy botWebViewProxy, String str, final ArrayList arrayList) {
        if (!TextUtils.isEmpty(str)) {
            notifyEvent("prepared_message_failed", obj("error", str));
            return;
        }
        notifyEvent("prepared_message_sent", null);
        Delegate delegate = this.delegate;
        if (delegate != null) {
            delegate.onOpenBackFromTabs();
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.lambda$onEventReceived$45(BotWebViewContainer.BotWebViewProxy.this, arrayList);
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$5(PopupButton popupButton, AtomicBoolean atomicBoolean, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        try {
            this.lastClickMs = System.currentTimeMillis();
            notifyEvent("popup_closed", new JSONObject().put("button_id", popupButton.id));
            atomicBoolean.set(true);
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$6(PopupButton popupButton, AtomicBoolean atomicBoolean, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        try {
            this.lastClickMs = System.currentTimeMillis();
            notifyEvent("popup_closed", new JSONObject().put("button_id", popupButton.id));
            atomicBoolean.set(true);
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$7(PopupButton popupButton, AtomicBoolean atomicBoolean, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        try {
            this.lastClickMs = System.currentTimeMillis();
            notifyEvent("popup_closed", new JSONObject().put("button_id", popupButton.id));
            atomicBoolean.set(true);
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$8(AtomicBoolean atomicBoolean, DialogInterface dialogInterface) {
        if (!atomicBoolean.get()) {
            notifyEvent("popup_closed", new JSONObject());
        }
        this.currentDialog = null;
        this.lastDialogClosed = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$9(TLRPC.TL_error tL_error, String str, TLRPC.TL_inputInvoiceSlug tL_inputInvoiceSlug, TLObject tLObject) {
        if (tL_error != null) {
            onInvoiceStatusUpdate(str, "failed");
        } else {
            this.delegate.onWebAppOpenInvoice(tL_inputInvoiceSlug, str, tLObject);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reload$1() {
        if (this.isSettingsButtonVisible) {
            this.isSettingsButtonVisible = false;
            Delegate delegate = this.delegate;
            if (delegate != null) {
                delegate.onSetSettingsButtonVisible(false);
            }
        }
        checkCreateWebView();
        this.isPageLoaded = false;
        this.lastClickMs = 0L;
        this.hasUserPermissions = false;
        MyWebView myWebView = this.webView;
        if (myWebView != null) {
            myWebView.onResume();
            this.webView.reload();
        }
        updateKeyboardFocusable();
        BotSensors botSensors = this.sensors;
        if (botSensors != null) {
            botSensors.stopAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runWithPermissions$0(Consumer consumer, String[] strArr) {
        consumer.accept(Boolean.valueOf(checkPermissions(strArr)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showDialog$48(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
        this.currentDialog = null;
    }

    public static String magic2tonsite(String str) {
        String hostAuthority;
        String str2;
        if (rotatedTONHosts == null || str == null || (hostAuthority = AndroidUtilities.getHostAuthority(str)) == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(".");
        sb.append(MessagesController.getInstance(UserConfig.selectedAccount).tonProxyAddress);
        return (hostAuthority.endsWith(sb.toString()) && (str2 = (String) rotatedTONHosts.get(hostAuthority)) != null) ? Browser.replace(Uri.parse(str), "tonsite", null, str2, null) : str;
    }

    private void notifyBiometryReceived() {
        if (this.botUser == null) {
            return;
        }
        createBiometry();
        BotBiometry botBiometry = this.biometry;
        if (botBiometry == null) {
            return;
        }
        try {
            notifyEvent("biometry_info_received", botBiometry.getStatus());
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private static void notifyEvent(int i, final MyWebView myWebView, final String str, final JSONObject jSONObject) {
        if (myWebView == null) {
            return;
        }
        NotificationCenter.getInstance(i).doOnIdle(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.lambda$notifyEvent$4(BotWebViewContainer.MyWebView.this, str, jSONObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyEvent(String str, JSONObject jSONObject) {
        d("notifyEvent " + str);
        evaluateJs("window.Telegram.WebView.receiveEvent('" + str + "', " + jSONObject + ");", false);
    }

    private void notifyEvent_fast(String str, String str2) {
        evaluateJs("window.Telegram.WebView.receiveEvent('" + str + "', " + str2 + ");", false);
    }

    private static JSONObject obj(String str, Object obj) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(str, obj);
            return jSONObject;
        } catch (Exception unused) {
            return null;
        }
    }

    private static JSONObject obj(String str, Object obj, String str2, Object obj2, String str3, Object obj3, String str4, Object obj4) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(str, obj);
            jSONObject.put(str2, obj2);
            jSONObject.put(str3, obj3);
            jSONObject.put(str4, obj4);
            return jSONObject;
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0614  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x06d3 A[Catch: Exception -> 0x03e1, TRY_LEAVE, TryCatch #15 {Exception -> 0x03e1, blocks: (B:31:0x03c8, B:35:0x03ef, B:37:0x03fe, B:39:0x0404, B:42:0x040d, B:53:0x0447, B:56:0x043e, B:58:0x0442, B:59:0x0422, B:62:0x042c, B:65:0x0455, B:67:0x0464, B:68:0x0471, B:70:0x0475, B:73:0x046d, B:108:0x0558, B:110:0x0572, B:113:0x057b, B:115:0x0581, B:116:0x058c, B:118:0x0592, B:120:0x05a1, B:124:0x05b2, B:128:0x05bf, B:132:0x059e, B:133:0x058a, B:135:0x05d4, B:150:0x06d3, B:153:0x061c, B:155:0x0621, B:169:0x0664, B:170:0x0667, B:171:0x066a, B:172:0x063b, B:175:0x0645, B:178:0x064f, B:181:0x066d, B:182:0x0677, B:195:0x06bd, B:196:0x06c1, B:197:0x06c5, B:198:0x06c9, B:199:0x06cd, B:200:0x067b, B:203:0x0685, B:206:0x068f, B:209:0x0699, B:212:0x06a3, B:215:0x05f3, B:218:0x05fd, B:221:0x0607, B:509:0x0e13, B:511:0x0e2d, B:514:0x0e38, B:516:0x0e3e, B:517:0x0e49, B:519:0x0e4f, B:521:0x0e5d, B:525:0x0e6a, B:529:0x0e7b, B:531:0x0e81, B:534:0x0e8c, B:536:0x0e86, B:539:0x0e5a, B:540:0x0e47, B:654:0x1106, B:656:0x1123, B:658:0x1135), top: B:13:0x0367 }] */
    /* JADX WARN: Removed duplicated region for block: B:164:0x065c  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x066a A[Catch: Exception -> 0x03e1, TryCatch #15 {Exception -> 0x03e1, blocks: (B:31:0x03c8, B:35:0x03ef, B:37:0x03fe, B:39:0x0404, B:42:0x040d, B:53:0x0447, B:56:0x043e, B:58:0x0442, B:59:0x0422, B:62:0x042c, B:65:0x0455, B:67:0x0464, B:68:0x0471, B:70:0x0475, B:73:0x046d, B:108:0x0558, B:110:0x0572, B:113:0x057b, B:115:0x0581, B:116:0x058c, B:118:0x0592, B:120:0x05a1, B:124:0x05b2, B:128:0x05bf, B:132:0x059e, B:133:0x058a, B:135:0x05d4, B:150:0x06d3, B:153:0x061c, B:155:0x0621, B:169:0x0664, B:170:0x0667, B:171:0x066a, B:172:0x063b, B:175:0x0645, B:178:0x064f, B:181:0x066d, B:182:0x0677, B:195:0x06bd, B:196:0x06c1, B:197:0x06c5, B:198:0x06c9, B:199:0x06cd, B:200:0x067b, B:203:0x0685, B:206:0x068f, B:209:0x0699, B:212:0x06a3, B:215:0x05f3, B:218:0x05fd, B:221:0x0607, B:509:0x0e13, B:511:0x0e2d, B:514:0x0e38, B:516:0x0e3e, B:517:0x0e49, B:519:0x0e4f, B:521:0x0e5d, B:525:0x0e6a, B:529:0x0e7b, B:531:0x0e81, B:534:0x0e8c, B:536:0x0e86, B:539:0x0e5a, B:540:0x0e47, B:654:0x1106, B:656:0x1123, B:658:0x1135), top: B:13:0x0367 }] */
    /* JADX WARN: Removed duplicated region for block: B:181:0x066d A[Catch: Exception -> 0x03e1, TryCatch #15 {Exception -> 0x03e1, blocks: (B:31:0x03c8, B:35:0x03ef, B:37:0x03fe, B:39:0x0404, B:42:0x040d, B:53:0x0447, B:56:0x043e, B:58:0x0442, B:59:0x0422, B:62:0x042c, B:65:0x0455, B:67:0x0464, B:68:0x0471, B:70:0x0475, B:73:0x046d, B:108:0x0558, B:110:0x0572, B:113:0x057b, B:115:0x0581, B:116:0x058c, B:118:0x0592, B:120:0x05a1, B:124:0x05b2, B:128:0x05bf, B:132:0x059e, B:133:0x058a, B:135:0x05d4, B:150:0x06d3, B:153:0x061c, B:155:0x0621, B:169:0x0664, B:170:0x0667, B:171:0x066a, B:172:0x063b, B:175:0x0645, B:178:0x064f, B:181:0x066d, B:182:0x0677, B:195:0x06bd, B:196:0x06c1, B:197:0x06c5, B:198:0x06c9, B:199:0x06cd, B:200:0x067b, B:203:0x0685, B:206:0x068f, B:209:0x0699, B:212:0x06a3, B:215:0x05f3, B:218:0x05fd, B:221:0x0607, B:509:0x0e13, B:511:0x0e2d, B:514:0x0e38, B:516:0x0e3e, B:517:0x0e49, B:519:0x0e4f, B:521:0x0e5d, B:525:0x0e6a, B:529:0x0e7b, B:531:0x0e81, B:534:0x0e8c, B:536:0x0e86, B:539:0x0e5a, B:540:0x0e47, B:654:0x1106, B:656:0x1123, B:658:0x1135), top: B:13:0x0367 }] */
    /* JADX WARN: Removed duplicated region for block: B:393:0x0b21 A[ADDED_TO_REGION, REMOVE, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:394:0x0b22  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0439  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0447 A[Catch: Exception -> 0x03e1, TryCatch #15 {Exception -> 0x03e1, blocks: (B:31:0x03c8, B:35:0x03ef, B:37:0x03fe, B:39:0x0404, B:42:0x040d, B:53:0x0447, B:56:0x043e, B:58:0x0442, B:59:0x0422, B:62:0x042c, B:65:0x0455, B:67:0x0464, B:68:0x0471, B:70:0x0475, B:73:0x046d, B:108:0x0558, B:110:0x0572, B:113:0x057b, B:115:0x0581, B:116:0x058c, B:118:0x0592, B:120:0x05a1, B:124:0x05b2, B:128:0x05bf, B:132:0x059e, B:133:0x058a, B:135:0x05d4, B:150:0x06d3, B:153:0x061c, B:155:0x0621, B:169:0x0664, B:170:0x0667, B:171:0x066a, B:172:0x063b, B:175:0x0645, B:178:0x064f, B:181:0x066d, B:182:0x0677, B:195:0x06bd, B:196:0x06c1, B:197:0x06c5, B:198:0x06c9, B:199:0x06cd, B:200:0x067b, B:203:0x0685, B:206:0x068f, B:209:0x0699, B:212:0x06a3, B:215:0x05f3, B:218:0x05fd, B:221:0x0607, B:509:0x0e13, B:511:0x0e2d, B:514:0x0e38, B:516:0x0e3e, B:517:0x0e49, B:519:0x0e4f, B:521:0x0e5d, B:525:0x0e6a, B:529:0x0e7b, B:531:0x0e81, B:534:0x0e8c, B:536:0x0e86, B:539:0x0e5a, B:540:0x0e47, B:654:0x1106, B:656:0x1123, B:658:0x1135), top: B:13:0x0367 }] */
    /* JADX WARN: Removed duplicated region for block: B:556:0x0ee1  */
    /* JADX WARN: Removed duplicated region for block: B:558:0x0eed  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0442 A[Catch: Exception -> 0x03e1, TryCatch #15 {Exception -> 0x03e1, blocks: (B:31:0x03c8, B:35:0x03ef, B:37:0x03fe, B:39:0x0404, B:42:0x040d, B:53:0x0447, B:56:0x043e, B:58:0x0442, B:59:0x0422, B:62:0x042c, B:65:0x0455, B:67:0x0464, B:68:0x0471, B:70:0x0475, B:73:0x046d, B:108:0x0558, B:110:0x0572, B:113:0x057b, B:115:0x0581, B:116:0x058c, B:118:0x0592, B:120:0x05a1, B:124:0x05b2, B:128:0x05bf, B:132:0x059e, B:133:0x058a, B:135:0x05d4, B:150:0x06d3, B:153:0x061c, B:155:0x0621, B:169:0x0664, B:170:0x0667, B:171:0x066a, B:172:0x063b, B:175:0x0645, B:178:0x064f, B:181:0x066d, B:182:0x0677, B:195:0x06bd, B:196:0x06c1, B:197:0x06c5, B:198:0x06c9, B:199:0x06cd, B:200:0x067b, B:203:0x0685, B:206:0x068f, B:209:0x0699, B:212:0x06a3, B:215:0x05f3, B:218:0x05fd, B:221:0x0607, B:509:0x0e13, B:511:0x0e2d, B:514:0x0e38, B:516:0x0e3e, B:517:0x0e49, B:519:0x0e4f, B:521:0x0e5d, B:525:0x0e6a, B:529:0x0e7b, B:531:0x0e81, B:534:0x0e8c, B:536:0x0e86, B:539:0x0e5a, B:540:0x0e47, B:654:0x1106, B:656:0x1123, B:658:0x1135), top: B:13:0x0367 }] */
    /* JADX WARN: Removed duplicated region for block: B:694:0x0bdb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onEventReceived(final BotWebViewProxy botWebViewProxy, String str, String str2) {
        char c;
        boolean z;
        LaunchActivity launchActivity;
        boolean z2;
        boolean z3;
        boolean z4;
        JSONArray jSONArray;
        BottomSheet bottomSheet;
        RequestDelegate requestDelegate;
        TextView textView;
        TextView textView2;
        TextView textView3;
        long j;
        long j2;
        int i;
        TLRPC.User user;
        JSONObject jSONObject;
        String str3;
        boolean z5;
        int checkSelfPermission;
        Delegate delegate;
        boolean z6;
        boolean z7;
        Object obj;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        String str9;
        final String str10;
        final String str11;
        final String str12;
        Object obj2;
        String str13;
        JSONObject put;
        JSONObject obj3;
        String str14;
        String str15;
        String str16;
        String str17;
        String onFullscreenRequested;
        JSONObject obj4;
        char c2;
        char c3;
        BotWebViewVibrationEffect botWebViewVibrationEffect;
        char c4;
        String str18;
        boolean z8;
        long j3;
        boolean z9;
        char c5;
        int i2;
        int i3;
        ConnectionsManager connectionsManager;
        TL_bots.checkDownloadFileParams checkdownloadfileparams;
        if (this.bot) {
            if (this.webView == null || this.delegate == null) {
                d("onEventReceived " + str + ": no webview or delegate!");
                return;
            }
            d("onEventReceived " + str);
            str.hashCode();
            switch (str.hashCode()) {
                case -2016939055:
                    if (str.equals("web_app_invoke_custom_method")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -1898902656:
                    if (str.equals("web_app_close_scan_qr_popup")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -1736707758:
                    if (str.equals("web_app_biometry_get_info")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -1717314938:
                    if (str.equals("web_app_open_link")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case -1715704462:
                    if (str.equals("web_app_request_file_download")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -1693280352:
                    if (str.equals("web_app_open_popup")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case -1390641887:
                    if (str.equals("web_app_open_invoice")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case -1385387727:
                    if (str.equals("web_app_set_emoji_status")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case -1353432696:
                    if (str.equals("web_app_setup_secondary_button")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case -1341039673:
                    if (str.equals("web_app_setup_closing_behavior")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case -1309122684:
                    if (str.equals("web_app_open_scan_qr_popup")) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case -1263619595:
                    if (str.equals("web_app_request_phone")) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case -1259935152:
                    if (str.equals("web_app_request_theme")) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                case -1183558219:
                    if (str.equals("web_app_check_location")) {
                        c = '\r';
                        break;
                    }
                    c = 65535;
                    break;
                case -1093591555:
                    if (str.equals("web_app_biometry_open_settings")) {
                        c = 14;
                        break;
                    }
                    c = 65535;
                    break;
                case -921083201:
                    if (str.equals("web_app_request_viewport")) {
                        c = 15;
                        break;
                    }
                    c = 65535;
                    break;
                case -907261345:
                    if (str.equals("web_app_request_emoji_status_access")) {
                        c = 16;
                        break;
                    }
                    c = 65535;
                    break;
                case -620103109:
                    if (str.equals("web_app_stop_device_orientation")) {
                        c = 17;
                        break;
                    }
                    c = 65535;
                    break;
                case -512688845:
                    if (str.equals("web_app_biometry_request_auth")) {
                        c = 18;
                        break;
                    }
                    c = 65535;
                    break;
                case -498118340:
                    if (str.equals("web_app_toggle_orientation_lock")) {
                        c = 19;
                        break;
                    }
                    c = 65535;
                    break;
                case -474676372:
                    if (str.equals("web_app_allow_scroll")) {
                        c = 20;
                        break;
                    }
                    c = 65535;
                    break;
                case -439770054:
                    if (str.equals("web_app_open_tg_link")) {
                        c = 21;
                        break;
                    }
                    c = 65535;
                    break;
                case -244584646:
                    if (str.equals("web_app_share_to_story")) {
                        c = 22;
                        break;
                    }
                    c = 65535;
                    break;
                case -216725042:
                    if (str.equals("web_app_request_location")) {
                        c = 23;
                        break;
                    }
                    c = 65535;
                    break;
                case -111186465:
                    if (str.equals("web_app_start_gyroscope")) {
                        c = 24;
                        break;
                    }
                    c = 65535;
                    break;
                case -71726289:
                    if (str.equals("web_app_close")) {
                        c = 25;
                        break;
                    }
                    c = 65535;
                    break;
                case -58095910:
                    if (str.equals("web_app_ready")) {
                        c = 26;
                        break;
                    }
                    c = 65535;
                    break;
                case 22015443:
                    if (str.equals("web_app_read_text_from_clipboard")) {
                        c = 27;
                        break;
                    }
                    c = 65535;
                    break;
                case 189207985:
                    if (str.equals("web_app_stop_gyroscope")) {
                        c = 28;
                        break;
                    }
                    c = 65535;
                    break;
                case 420328489:
                    if (str.equals("web_app_start_accelerometer")) {
                        c = 29;
                        break;
                    }
                    c = 65535;
                    break;
                case 475603707:
                    if (str.equals("web_app_stop_accelerometer")) {
                        c = 30;
                        break;
                    }
                    c = 65535;
                    break;
                case 622108947:
                    if (str.equals("web_app_send_prepared_message")) {
                        c = 31;
                        break;
                    }
                    c = 65535;
                    break;
                case 668142772:
                    if (str.equals("web_app_data_send")) {
                        c = ' ';
                        break;
                    }
                    c = 65535;
                    break;
                case 671811520:
                    if (str.equals("web_app_request_content_safe_area")) {
                        c = '!';
                        break;
                    }
                    c = 65535;
                    break;
                case 721956751:
                    if (str.equals("web_app_add_to_home_screen")) {
                        c = '\"';
                        break;
                    }
                    c = 65535;
                    break;
                case 748864404:
                    if (str.equals("web_app_request_fullscreen")) {
                        c = '#';
                        break;
                    }
                    c = 65535;
                    break;
                case 751292356:
                    if (str.equals("web_app_switch_inline_query")) {
                        c = '$';
                        break;
                    }
                    c = 65535;
                    break;
                case 796110323:
                    if (str.equals("web_app_exit_fullscreen")) {
                        c = '%';
                        break;
                    }
                    c = 65535;
                    break;
                case 909476449:
                    if (str.equals("web_app_open_location_settings")) {
                        c = '&';
                        break;
                    }
                    c = 65535;
                    break;
                case 1011447167:
                    if (str.equals("web_app_setup_back_button")) {
                        c = '\'';
                        break;
                    }
                    c = 65535;
                    break;
                case 1210129967:
                    if (str.equals("web_app_biometry_request_access")) {
                        c = '(';
                        break;
                    }
                    c = 65535;
                    break;
                case 1273834781:
                    if (str.equals("web_app_trigger_haptic_feedback")) {
                        c = ')';
                        break;
                    }
                    c = 65535;
                    break;
                case 1398490221:
                    if (str.equals("web_app_setup_main_button")) {
                        c = '*';
                        break;
                    }
                    c = 65535;
                    break;
                case 1453051298:
                    if (str.equals("web_app_setup_swipe_behavior")) {
                        c = '+';
                        break;
                    }
                    c = 65535;
                    break;
                case 1455972419:
                    if (str.equals("web_app_setup_settings_button")) {
                        c = ',';
                        break;
                    }
                    c = 65535;
                    break;
                case 1495787980:
                    if (str.equals("web_app_check_home_screen")) {
                        c = '-';
                        break;
                    }
                    c = 65535;
                    break;
                case 1812395469:
                    if (str.equals("web_app_start_device_orientation")) {
                        c = '.';
                        break;
                    }
                    c = 65535;
                    break;
                case 1882780382:
                    if (str.equals("web_app_biometry_update_token")) {
                        c = '/';
                        break;
                    }
                    c = 65535;
                    break;
                case 1899078473:
                    if (str.equals("web_app_set_bottom_bar_color")) {
                        c = '0';
                        break;
                    }
                    c = 65535;
                    break;
                case 1917103703:
                    if (str.equals("web_app_set_header_color")) {
                        c = '1';
                        break;
                    }
                    c = 65535;
                    break;
                case 1937068806:
                    if (str.equals("web_app_request_safe_area")) {
                        c = '2';
                        break;
                    }
                    c = 65535;
                    break;
                case 2001330488:
                    if (str.equals("web_app_set_background_color")) {
                        c = '3';
                        break;
                    }
                    c = 65535;
                    break;
                case 2036090717:
                    if (str.equals("web_app_request_write_access")) {
                        c = '4';
                        break;
                    }
                    c = 65535;
                    break;
                case 2139805763:
                    if (str.equals("web_app_expand")) {
                        c = '5';
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            long j4 = 1000;
            BottomSheetTabs.WebTabData webTabData = null;
            String str19 = null;
            String str20 = null;
            String str21 = null;
            r12 = null;
            r12 = null;
            BotWebViewVibrationEffect botWebViewVibrationEffect2 = null;
            try {
                try {
                    switch (c) {
                        case 0:
                            if (this.botUser != null) {
                                try {
                                    JSONObject jSONObject2 = new JSONObject(str2);
                                    final String string = jSONObject2.getString("req_id");
                                    String string2 = jSONObject2.getString("method");
                                    String obj5 = jSONObject2.get("params").toString();
                                    final int i4 = this.currentAccount;
                                    final MyWebView myWebView = this.webView;
                                    TL_bots.invokeWebViewCustomMethod invokewebviewcustommethod = new TL_bots.invokeWebViewCustomMethod();
                                    invokewebviewcustommethod.bot = MessagesController.getInstance(i4).getInputUser(this.botUser.id);
                                    invokewebviewcustommethod.custom_method = string2;
                                    TLRPC.TL_dataJSON tL_dataJSON = new TLRPC.TL_dataJSON();
                                    invokewebviewcustommethod.params = tL_dataJSON;
                                    tL_dataJSON.data = obj5;
                                    ConnectionsManager.getInstance(i4).sendRequest(invokewebviewcustommethod, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda22
                                        @Override // org.telegram.tgnet.RequestDelegate
                                        public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                            BotWebViewContainer.this.lambda$onEventReceived$19(string, i4, myWebView, tLObject, tL_error);
                                        }
                                    });
                                    break;
                                } catch (Exception e) {
                                    FileLog.e(e);
                                    if (e instanceof JSONException) {
                                        error("JSON Parse error");
                                        return;
                                    } else {
                                        unknownError();
                                        return;
                                    }
                                }
                            }
                            break;
                        case 1:
                            if (this.hasQRPending && (bottomSheet = this.cameraBottomSheet) != null) {
                                bottomSheet.dismiss();
                                break;
                            }
                            break;
                        case 2:
                            notifyBiometryReceived();
                            break;
                        case 3:
                            JSONObject jSONObject3 = new JSONObject(str2);
                            Uri parse = Uri.parse(jSONObject3.optString("url"));
                            String optString = jSONObject3.optString("try_browser");
                            if (MessagesController.getInstance(this.currentAccount).webAppAllowedProtocols != null && MessagesController.getInstance(this.currentAccount).webAppAllowedProtocols.contains(parse.getScheme())) {
                                onOpenUri(parse, optString, jSONObject3.optBoolean("try_instant_view"), true, false);
                                break;
                            }
                            break;
                        case 4:
                            if (!this.isRequestingPageOpen && this.botUser != null && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                                if (this.downloads == null) {
                                    this.downloads = BotDownloads.get(getContext(), this.currentAccount, this.botUser.id);
                                }
                                try {
                                    JSONObject jSONObject4 = new JSONObject(str2);
                                    final String string3 = jSONObject4.getString("url");
                                    final String string4 = jSONObject4.getString("file_name");
                                    if (this.downloads.getCached(string3) != null) {
                                        this.downloads.download(string3, string4);
                                        notifyEvent("file_download_requested", obj("status", "downloading"));
                                        break;
                                    } else {
                                        TL_bots.checkDownloadFileParams checkdownloadfileparams2 = new TL_bots.checkDownloadFileParams();
                                        checkdownloadfileparams2.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botUser);
                                        checkdownloadfileparams2.file_name = string4;
                                        checkdownloadfileparams2.url = string3;
                                        ConnectionsManager connectionsManager2 = ConnectionsManager.getInstance(this.currentAccount);
                                        requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda21
                                            @Override // org.telegram.tgnet.RequestDelegate
                                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                                BotWebViewContainer.this.lambda$onEventReceived$43(string3, string4, tLObject, tL_error);
                                            }
                                        };
                                        checkdownloadfileparams = checkdownloadfileparams2;
                                        connectionsManager = connectionsManager2;
                                        connectionsManager.sendRequest(checkdownloadfileparams, requestDelegate);
                                        break;
                                    }
                                } catch (Exception e2) {
                                    FileLog.e(e2);
                                    notifyEvent("file_download_requested", obj("status", "cancelled"));
                                    return;
                                }
                            }
                            break;
                        case 5:
                            if (this.currentDialog != null) {
                                break;
                            } else {
                                if (System.currentTimeMillis() - this.lastDialogClosed <= 150) {
                                    int i5 = this.dialogSequentialOpenTimes + 1;
                                    this.dialogSequentialOpenTimes = i5;
                                    if (i5 >= 3) {
                                        this.dialogSequentialOpenTimes = 0;
                                        this.lastDialogCooldownTime = System.currentTimeMillis();
                                        break;
                                    }
                                }
                                if (System.currentTimeMillis() - this.lastDialogCooldownTime <= 3000) {
                                    break;
                                } else {
                                    JSONObject jSONObject5 = new JSONObject(str2);
                                    String optString2 = jSONObject5.optString("title", null);
                                    String string5 = jSONObject5.getString("message");
                                    JSONArray jSONArray2 = jSONObject5.getJSONArray("buttons");
                                    AlertDialog.Builder message = new AlertDialog.Builder(getContext()).setTitle(optString2).setMessage(string5);
                                    ArrayList arrayList = new ArrayList();
                                    for (int i6 = 0; i6 < jSONArray2.length(); i6++) {
                                        arrayList.add(new PopupButton(jSONArray2.getJSONObject(i6)));
                                    }
                                    if (arrayList.size() > 3) {
                                        break;
                                    } else {
                                        final AtomicBoolean atomicBoolean = new AtomicBoolean();
                                        if (arrayList.size() >= 1) {
                                            final PopupButton popupButton = (PopupButton) arrayList.get(0);
                                            message.setPositiveButton(popupButton.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda17
                                                @Override // android.content.DialogInterface.OnClickListener
                                                public final void onClick(DialogInterface dialogInterface, int i7) {
                                                    BotWebViewContainer.this.lambda$onEventReceived$5(popupButton, atomicBoolean, dialogInterface, i7);
                                                }
                                            });
                                        }
                                        if (arrayList.size() >= 2) {
                                            final PopupButton popupButton2 = (PopupButton) arrayList.get(1);
                                            message.setNegativeButton(popupButton2.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda18
                                                @Override // android.content.DialogInterface.OnClickListener
                                                public final void onClick(DialogInterface dialogInterface, int i7) {
                                                    BotWebViewContainer.this.lambda$onEventReceived$6(popupButton2, atomicBoolean, dialogInterface, i7);
                                                }
                                            });
                                        }
                                        if (arrayList.size() == 3) {
                                            final PopupButton popupButton3 = (PopupButton) arrayList.get(2);
                                            message.setNeutralButton(popupButton3.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda19
                                                @Override // android.content.DialogInterface.OnClickListener
                                                public final void onClick(DialogInterface dialogInterface, int i7) {
                                                    BotWebViewContainer.this.lambda$onEventReceived$7(popupButton3, atomicBoolean, dialogInterface, i7);
                                                }
                                            });
                                        }
                                        message.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda20
                                            @Override // android.content.DialogInterface.OnDismissListener
                                            public final void onDismiss(DialogInterface dialogInterface) {
                                                BotWebViewContainer.this.lambda$onEventReceived$8(atomicBoolean, dialogInterface);
                                            }
                                        });
                                        this.currentDialog = message.show();
                                        if (arrayList.size() >= 1) {
                                            PopupButton popupButton4 = (PopupButton) arrayList.get(0);
                                            if (popupButton4.textColorKey >= 0 && (textView3 = (TextView) this.currentDialog.getButton(-1)) != null) {
                                                textView3.setTextColor(getColor(popupButton4.textColorKey));
                                            }
                                        }
                                        if (arrayList.size() >= 2) {
                                            PopupButton popupButton5 = (PopupButton) arrayList.get(1);
                                            if (popupButton5.textColorKey >= 0 && (textView2 = (TextView) this.currentDialog.getButton(-2)) != null) {
                                                textView2.setTextColor(getColor(popupButton5.textColorKey));
                                            }
                                        }
                                        if (arrayList.size() == 3) {
                                            PopupButton popupButton6 = (PopupButton) arrayList.get(2);
                                            if (popupButton6.textColorKey >= 0 && (textView = (TextView) this.currentDialog.getButton(-3)) != null) {
                                                textView.setTextColor(getColor(popupButton6.textColorKey));
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case 6:
                            final String optString3 = new JSONObject(str2).optString("slug");
                            if (this.currentPaymentSlug != null) {
                                onInvoiceStatusUpdate(optString3, "cancelled", true);
                                break;
                            } else {
                                this.currentPaymentSlug = optString3;
                                TLRPC.TL_payments_getPaymentForm tL_payments_getPaymentForm = new TLRPC.TL_payments_getPaymentForm();
                                final TLRPC.TL_inputInvoiceSlug tL_inputInvoiceSlug = new TLRPC.TL_inputInvoiceSlug();
                                tL_inputInvoiceSlug.slug = optString3;
                                tL_payments_getPaymentForm.invoice = tL_inputInvoiceSlug;
                                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda16
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                        BotWebViewContainer.this.lambda$onEventReceived$10(optString3, tL_inputInvoiceSlug, tLObject, tL_error);
                                    }
                                });
                                break;
                            }
                        case 7:
                            if (!this.isRequestingPageOpen && this.botUser != null && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                                try {
                                    jSONObject = new JSONObject(str2);
                                    j = Long.parseLong(jSONObject.getString("custom_emoji_id"));
                                } catch (Exception unused) {
                                    j = 0;
                                }
                                try {
                                    i = jSONObject.getInt("duration");
                                    j2 = j;
                                } catch (Exception unused2) {
                                    j2 = j;
                                    i = 0;
                                    user = this.botUser;
                                    if (user != null) {
                                    }
                                }
                                user = this.botUser;
                                if (user != null) {
                                    notifyEvent("emoji_status_failed", obj("error", "UNKNOWN_ERROR"));
                                    break;
                                } else {
                                    SetupEmojiStatusSheet.show(this.currentAccount, user, j2, i, new Utilities.Callback2() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda14
                                        @Override // org.telegram.messenger.Utilities.Callback2
                                        public final void run(Object obj6, Object obj7) {
                                            BotWebViewContainer.this.lambda$onEventReceived$36((String) obj6, (TLRPC.Document) obj7);
                                        }
                                    });
                                    break;
                                }
                            }
                            break;
                        case '\b':
                            JSONObject jSONObject6 = new JSONObject(str2);
                            boolean optBoolean = jSONObject6.optBoolean("is_active", false);
                            String trim = jSONObject6.optString("text", this.lastSecondaryButtonText).trim();
                            boolean z10 = jSONObject6.optBoolean("is_visible", false) && !TextUtils.isEmpty(trim);
                            int parseColor = jSONObject6.has("color") ? Color.parseColor(jSONObject6.optString("color")) : this.lastSecondaryButtonColor;
                            int parseColor2 = jSONObject6.has("text_color") ? Color.parseColor(jSONObject6.optString("text_color")) : this.lastSecondaryButtonTextColor;
                            boolean z11 = jSONObject6.optBoolean("is_progress_visible", false) && z10;
                            if (jSONObject6.optBoolean("has_shine_effect", false) && z10) {
                                str3 = "position";
                                z5 = true;
                            } else {
                                str3 = "position";
                                z5 = false;
                            }
                            String optString4 = jSONObject6.has(str3) ? jSONObject6.optString(str3) : this.lastSecondaryButtonPosition;
                            if (optString4 == null) {
                                optString4 = "left";
                            }
                            this.lastSecondaryButtonColor = parseColor;
                            this.lastSecondaryButtonTextColor = parseColor2;
                            this.lastSecondaryButtonText = trim;
                            this.lastSecondaryButtonPosition = optString4;
                            this.secondaryButtonData = str2;
                            this.delegate.onSetupSecondaryButton(z10, optBoolean, trim, parseColor, parseColor2, z11, z5, optString4);
                            break;
                        case '\t':
                            this.delegate.onWebAppSetupClosingBehavior(new JSONObject(str2).optBoolean("need_confirmation"));
                            break;
                        case '\n':
                            if (!this.hasQRPending && this.parentActivity != null) {
                                this.lastQrText = new JSONObject(str2).optString("text");
                                this.hasQRPending = true;
                                if (Build.VERSION.SDK_INT >= 23) {
                                    checkSelfPermission = this.parentActivity.checkSelfPermission("android.permission.CAMERA");
                                    if (checkSelfPermission != 0) {
                                        NotificationCenter.getGlobalInstance().addObserver(new NotificationCenter.NotificationCenterDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer.4
                                            @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
                                            public void didReceivedNotification(int i7, int i8, Object... objArr) {
                                                int i9 = NotificationCenter.onRequestPermissionResultReceived;
                                                if (i7 == i9) {
                                                    int intValue = ((Integer) objArr[0]).intValue();
                                                    int[] iArr = (int[]) objArr[2];
                                                    if (intValue == 5000) {
                                                        NotificationCenter.getGlobalInstance().removeObserver(this, i9);
                                                        if (iArr[0] == 0) {
                                                            BotWebViewContainer.this.openQrScanActivity();
                                                        } else {
                                                            BotWebViewContainer.this.notifyEvent("scan_qr_popup_closed", new JSONObject());
                                                        }
                                                    }
                                                }
                                            }
                                        }, NotificationCenter.onRequestPermissionResultReceived);
                                        this.parentActivity.requestPermissions(new String[]{"android.permission.CAMERA"}, 5000);
                                        break;
                                    }
                                }
                                openQrScanActivity();
                                break;
                            }
                            break;
                        case 11:
                            if (!ignoreDialog(4)) {
                                final int i7 = this.currentAccount;
                                final MyWebView myWebView2 = this.webView;
                                final String[] strArr = {"cancelled"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
                                builder.setTitle(LocaleController.getString(R.string.ShareYouPhoneNumberTitle));
                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                                String userName = UserObject.getUserName(this.botUser);
                                spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(!TextUtils.isEmpty(userName) ? LocaleController.formatString(R.string.AreYouSureShareMyContactInfoWebapp, userName) : LocaleController.getString(R.string.AreYouSureShareMyContactInfoBot)));
                                final boolean z12 = MessagesController.getInstance(this.currentAccount).blockePeers.indexOfKey(this.botUser.id) >= 0;
                                if (z12) {
                                    spannableStringBuilder.append((CharSequence) "\n\n");
                                    spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.AreYouSureShareMyContactInfoBotUnblock));
                                }
                                builder.setMessage(spannableStringBuilder);
                                builder.setPositiveButton(LocaleController.getString(R.string.ShareContact), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda11
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i8) {
                                        BotWebViewContainer.this.lambda$onEventReceived$21(strArr, z12, i7, myWebView2, dialogInterface, i8);
                                    }
                                });
                                builder.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda12
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i8) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                showDialog(4, builder.create(), new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda13
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        BotWebViewContainer.lambda$onEventReceived$23(strArr, i7, myWebView2);
                                    }
                                });
                                break;
                            } else {
                                try {
                                    JSONObject jSONObject7 = new JSONObject();
                                    jSONObject7.put("status", "cancelled");
                                    notifyEvent("phone_requested", jSONObject7);
                                    break;
                                } catch (Exception e3) {
                                    FileLog.e(e3);
                                    return;
                                }
                            }
                        case '\f':
                            notifyThemeChanged();
                            break;
                        case '\r':
                            if (this.location == null) {
                                BotLocation botLocation = BotLocation.get(getContext(), this.currentAccount, this.botUser.id);
                                this.location = botLocation;
                                botLocation.listen(this.notifyLocationChecked);
                            }
                            this.notifyLocationChecked.run();
                            break;
                        case 14:
                            if (!this.isRequestingPageOpen && this.botUser != null && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                                this.lastClickMs = 0L;
                                BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
                                if (safeLastFragment != null && safeLastFragment.getParentLayout() != null) {
                                    INavigationLayout parentLayout = safeLastFragment.getParentLayout();
                                    safeLastFragment.presentFragment(ProfileActivity.of(this.botUser.id));
                                    AndroidUtilities.scrollToFragmentRow(parentLayout, "botPermissionBiometry");
                                    delegate = this.delegate;
                                    if (delegate == null) {
                                    }
                                    delegate.onCloseToTabs();
                                    break;
                                }
                            }
                            break;
                        case 15:
                            if ((getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) && ((ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent()).isSwipeInProgress()) {
                                z6 = true;
                                z7 = true;
                            } else {
                                z6 = true;
                                z7 = false;
                            }
                            invalidateViewPortHeight(!z7, z6);
                            break;
                        case 16:
                            if (!this.isRequestingPageOpen && this.botUser != null && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                                SetupEmojiStatusSheet.askPermission(this.currentAccount, this.botUser.id, new Utilities.Callback2() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda10
                                    @Override // org.telegram.messenger.Utilities.Callback2
                                    public final void run(Object obj6, Object obj7) {
                                        BotWebViewContainer.this.lambda$onEventReceived$37((Boolean) obj6, (String) obj7);
                                    }
                                });
                                break;
                            }
                            break;
                        case 17:
                            obj = "UNSUPPORTED";
                            str4 = "error";
                            BotSensors botSensors = this.delegate.getBotSensors();
                            if (botSensors != null && botSensors.stopOrientation()) {
                                str5 = "device_orientation_stopped";
                                notifyEvent(str5, null);
                                break;
                            }
                            obj3 = obj(str4, obj);
                            str14 = "device_orientation_failed";
                            notifyEvent(str14, obj3);
                            break;
                        case 18:
                            try {
                                str19 = new JSONObject(str2).getString("reason");
                            } catch (Exception unused3) {
                            }
                            createBiometry();
                            BotBiometry botBiometry = this.biometry;
                            if (botBiometry != null) {
                                if (botBiometry.access_granted) {
                                    botBiometry.requestToken(str19, new Utilities.Callback2() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda9
                                        @Override // org.telegram.messenger.Utilities.Callback2
                                        public final void run(Object obj6, Object obj7) {
                                            BotWebViewContainer.this.lambda$onEventReceived$29((Boolean) obj6, (String) obj7);
                                        }
                                    });
                                    break;
                                } else {
                                    try {
                                        JSONObject jSONObject8 = new JSONObject();
                                        jSONObject8.put("status", "failed");
                                        notifyEvent("biometry_auth_requested", jSONObject8);
                                        break;
                                    } catch (Exception e4) {
                                        FileLog.e(e4);
                                        return;
                                    }
                                }
                            }
                            break;
                        case 19:
                            try {
                                z2 = new JSONObject(str2).getBoolean("locked");
                            } catch (Exception unused4) {
                                z2 = false;
                            }
                            Delegate delegate2 = this.delegate;
                            if (delegate2 != null) {
                                delegate2.onOrientationLockChanged(z2);
                                break;
                            }
                            break;
                        case 20:
                            try {
                                jSONArray = new JSONArray(str2);
                                z3 = jSONArray.optBoolean(0, true);
                            } catch (Exception unused5) {
                                z3 = true;
                            }
                            try {
                                z4 = jSONArray.optBoolean(1, true);
                            } catch (Exception unused6) {
                                z4 = true;
                                d("allowScroll " + z3 + " " + z4);
                                if (!(getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer)) {
                                    return;
                                }
                            }
                            d("allowScroll " + z3 + " " + z4);
                            if (!(getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer)) {
                                ((ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent()).allowThisScroll(z3, z4);
                                break;
                            }
                        case 21:
                            JSONObject jSONObject9 = new JSONObject(str2);
                            String optString5 = jSONObject9.optString("path_full");
                            boolean optBoolean2 = jSONObject9.optBoolean("force_request", false);
                            if (optString5.startsWith("/")) {
                                optString5 = optString5.substring(1);
                            }
                            onOpenUri(Uri.parse("https://t.me/" + optString5), null, false, true, optBoolean2);
                            break;
                        case 22:
                            if (!this.isRequestingPageOpen && System.currentTimeMillis() - this.lastClickMs <= 10000 && System.currentTimeMillis() - this.lastPostStoryMs >= 2000) {
                                this.lastClickMs = 0L;
                                this.lastPostStoryMs = System.currentTimeMillis();
                                try {
                                    JSONObject jSONObject10 = new JSONObject(str2);
                                    str6 = jSONObject10.optString("media_url");
                                    try {
                                        str7 = jSONObject10.optString("text");
                                        try {
                                            JSONObject optJSONObject = jSONObject10.optJSONObject("widget_link");
                                            if (optJSONObject != null) {
                                                str8 = optJSONObject.optString("url");
                                                try {
                                                    str12 = optJSONObject.optString("name");
                                                    str9 = str6;
                                                    str10 = str7;
                                                    str11 = str8;
                                                } catch (Exception e5) {
                                                    e = e5;
                                                    FileLog.e(e);
                                                    str9 = str6;
                                                    str10 = str7;
                                                    str11 = str8;
                                                    str12 = null;
                                                    if (str9 == null) {
                                                    }
                                                }
                                            } else {
                                                str9 = str6;
                                                str10 = str7;
                                                str11 = null;
                                                str12 = null;
                                            }
                                        } catch (Exception e6) {
                                            e = e6;
                                            str8 = null;
                                        }
                                    } catch (Exception e7) {
                                        e = e7;
                                        str7 = null;
                                        str8 = str7;
                                        FileLog.e(e);
                                        str9 = str6;
                                        str10 = str7;
                                        str11 = str8;
                                        str12 = null;
                                        if (str9 == null) {
                                            return;
                                        }
                                    }
                                } catch (Exception e8) {
                                    e = e8;
                                    str6 = null;
                                    str7 = null;
                                }
                                if (str9 == null) {
                                    if (MessagesController.getInstance(this.currentAccount).storiesEnabled()) {
                                        final AlertDialog alertDialog = new AlertDialog(this.parentActivity, 3);
                                        new HttpGetFileTask(new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda8
                                            @Override // org.telegram.messenger.Utilities.Callback
                                            public final void run(Object obj6) {
                                                BotWebViewContainer.this.lambda$onEventReceived$34(alertDialog, str10, str11, str12, (File) obj6);
                                            }
                                        }).execute(str9);
                                        alertDialog.showDelayed(250L);
                                        break;
                                    } else {
                                        new PremiumFeatureBottomSheet(new BaseFragment() { // from class: org.telegram.ui.web.BotWebViewContainer.5
                                            {
                                                this.currentAccount = BotWebViewContainer.this.currentAccount;
                                            }

                                            @Override // org.telegram.ui.ActionBar.BaseFragment
                                            public Activity getParentActivity() {
                                                return BotWebViewContainer.this.parentActivity;
                                            }

                                            @Override // org.telegram.ui.ActionBar.BaseFragment
                                            public Theme.ResourcesProvider getResourceProvider() {
                                                return new WrappedResourceProvider(BotWebViewContainer.this.resourcesProvider) { // from class: org.telegram.ui.web.BotWebViewContainer.5.1
                                                    @Override // org.telegram.ui.WrappedResourceProvider
                                                    public void appendColors() {
                                                        this.sparseIntArray.append(Theme.key_dialogBackground, -14803426);
                                                        this.sparseIntArray.append(Theme.key_windowBackgroundGray, -16777216);
                                                    }
                                                };
                                            }

                                            @Override // org.telegram.ui.ActionBar.BaseFragment
                                            public boolean isLightStatusBar() {
                                                return false;
                                            }

                                            @Override // org.telegram.ui.ActionBar.BaseFragment
                                            public Dialog showDialog(Dialog dialog) {
                                                dialog.show();
                                                return dialog;
                                            }
                                        }, 14, true).show();
                                        break;
                                    }
                                }
                            }
                            break;
                        case 23:
                            if (!this.isRequestingPageOpen && this.botUser != null) {
                                if (this.location == null) {
                                    BotLocation botLocation2 = BotLocation.get(getContext(), this.currentAccount, this.botUser.id);
                                    this.location = botLocation2;
                                    botLocation2.listen(this.notifyLocationChecked);
                                }
                                if (this.location.granted()) {
                                    this.location.requestObject(new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda7
                                        @Override // org.telegram.messenger.Utilities.Callback
                                        public final void run(Object obj6) {
                                            BotWebViewContainer.this.lambda$onEventReceived$40((JSONObject) obj6);
                                        }
                                    });
                                    break;
                                } else {
                                    this.location.request(new Utilities.Callback2() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda30
                                        @Override // org.telegram.messenger.Utilities.Callback2
                                        public final void run(Object obj6, Object obj7) {
                                            BotWebViewContainer.this.lambda$onEventReceived$39((Boolean) obj6, (Boolean) obj7);
                                        }
                                    });
                                    break;
                                }
                            }
                            break;
                        case 24:
                            obj2 = "UNSUPPORTED";
                            str13 = "error";
                            BotSensors botSensors2 = this.delegate.getBotSensors();
                            try {
                                j4 = new JSONObject(str2).getLong("refresh_rate");
                            } catch (Exception unused7) {
                            }
                            long clamp = Utilities.clamp(j4, 1000L, 20L);
                            if (botSensors2 != null && botSensors2.startGyroscope(clamp)) {
                                str5 = "gyroscope_started";
                                notifyEvent(str5, null);
                                break;
                            }
                            obj3 = obj(str13, obj2);
                            str14 = "gyroscope_failed";
                            notifyEvent(str14, obj3);
                            break;
                        case 25:
                            try {
                                z = new JSONObject(str2).optBoolean("return_back");
                            } catch (Exception e9) {
                                FileLog.e(e9);
                                z = false;
                            }
                            this.delegate.onCloseRequested(null);
                            if (z) {
                                if (this.wasOpenedByLinkIntent && LaunchActivity.instance != null) {
                                    Activity findActivity = AndroidUtilities.findActivity(getContext());
                                    if (findActivity == null) {
                                        findActivity = LaunchActivity.instance;
                                    }
                                    if (findActivity != null && !findActivity.isFinishing()) {
                                        findActivity.moveTaskToBack(true);
                                        break;
                                    }
                                } else if (this.wasOpenedByBot != null && (launchActivity = LaunchActivity.instance) != null && launchActivity.getBottomSheetTabs() != null) {
                                    BottomSheetTabs bottomSheetTabs = LaunchActivity.instance.getBottomSheetTabs();
                                    ArrayList<BottomSheetTabs.WebTabData> tabs = bottomSheetTabs.getTabs();
                                    int i8 = 0;
                                    while (true) {
                                        if (i8 < tabs.size()) {
                                            BottomSheetTabs.WebTabData webTabData2 = tabs.get(i8);
                                            if (!this.wasOpenedByBot.equals(webTabData2.props) || webTabData2.webView == this.webView) {
                                                i8++;
                                            } else {
                                                webTabData = webTabData2;
                                            }
                                        }
                                    }
                                    if (webTabData != null) {
                                        bottomSheetTabs.openTab(webTabData);
                                        break;
                                    }
                                }
                            }
                            break;
                        case 26:
                            setPageLoaded(this.webView.getUrl(), true);
                            break;
                        case 27:
                            String string6 = new JSONObject(str2).getString("req_id");
                            if (this.delegate.isClipboardAvailable() && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                                CharSequence text = ((ClipboardManager) getContext().getSystemService("clipboard")).getText();
                                put = new JSONObject().put("req_id", string6).put("data", text != null ? text.toString() : "");
                                notifyEvent("clipboard_text_received", put);
                                break;
                            }
                            put = new JSONObject().put("req_id", string6);
                            notifyEvent("clipboard_text_received", put);
                            break;
                        case 28:
                            obj2 = "UNSUPPORTED";
                            str13 = "error";
                            BotSensors botSensors3 = this.delegate.getBotSensors();
                            if (botSensors3 != null && botSensors3.stopGyroscope()) {
                                str5 = "gyroscope_stopped";
                                notifyEvent(str5, null);
                                break;
                            }
                            obj3 = obj(str13, obj2);
                            str14 = "gyroscope_failed";
                            notifyEvent(str14, obj3);
                            break;
                        case 29:
                            str15 = "error";
                            BotSensors botSensors4 = this.delegate.getBotSensors();
                            try {
                                j4 = new JSONObject(str2).getLong("refresh_rate");
                            } catch (Exception unused8) {
                            }
                            long clamp2 = Utilities.clamp(j4, 1000L, 20L);
                            if (botSensors4 != null && botSensors4.startAccelerometer(clamp2)) {
                                str5 = "accelerometer_started";
                                notifyEvent(str5, null);
                                break;
                            }
                            obj3 = obj(str15, "UNSUPPORTED");
                            str14 = "accelerometer_failed";
                            notifyEvent(str14, obj3);
                            break;
                        case 30:
                            str15 = "error";
                            BotSensors botSensors5 = this.delegate.getBotSensors();
                            if (botSensors5 != null && botSensors5.stopAccelerometer()) {
                                str5 = "accelerometer_stopped";
                                notifyEvent(str5, null);
                                break;
                            }
                            obj3 = obj(str15, "UNSUPPORTED");
                            str14 = "accelerometer_failed";
                            notifyEvent(str14, obj3);
                            break;
                        case 31:
                            if (!this.isRequestingPageOpen && this.botUser != null && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                                try {
                                    String string7 = new JSONObject(str2).getString("id");
                                    if (TextUtils.isEmpty(string7)) {
                                        notifyEvent("prepared_message_failed", obj("error", "MESSAGE_EXPIRED"));
                                        break;
                                    } else {
                                        BotShareSheet.share(getContext(), this.currentAccount, this.botUser.id, string7, this.resourcesProvider, new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda28
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                BotWebViewContainer.this.lambda$onEventReceived$44();
                                            }
                                        }, new Utilities.Callback2() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda29
                                            @Override // org.telegram.messenger.Utilities.Callback2
                                            public final void run(Object obj6, Object obj7) {
                                                BotWebViewContainer.this.lambda$onEventReceived$46(botWebViewProxy, (String) obj6, (ArrayList) obj7);
                                            }
                                        });
                                        break;
                                    }
                                } catch (Exception e10) {
                                    FileLog.e(e10);
                                    notifyEvent("prepared_message_failed", obj("error", "MESSAGE_EXPIRED"));
                                    return;
                                }
                            }
                            break;
                        case ' ':
                            this.delegate.onSendWebViewData(new JSONObject(str2).optString("data"));
                            break;
                        case '!':
                            reportSafeContentInsets(this.lastInsetsTopMargin, true);
                            break;
                        case '\"':
                            if (!this.isRequestingPageOpen && this.botUser != null && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                                if (MediaDataController.getInstance(this.currentAccount).isShortcutAdded(this.botUser.id, MediaDataController.SHORTCUT_TYPE_ATTACHED_BOT)) {
                                    notifyEvent("home_screen_added", null);
                                    break;
                                } else {
                                    MediaDataController.getInstance(this.currentAccount).installShortcut(this.botUser.id, MediaDataController.SHORTCUT_TYPE_ATTACHED_BOT, new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda27
                                        @Override // org.telegram.messenger.Utilities.Callback
                                        public final void run(Object obj6) {
                                            BotWebViewContainer.this.lambda$onEventReceived$35((Boolean) obj6);
                                        }
                                    });
                                    break;
                                }
                            }
                            break;
                        case '#':
                            str16 = "fullscreen_changed";
                            str17 = "error";
                            onFullscreenRequested = this.delegate.onFullscreenRequested(true);
                            if (onFullscreenRequested == null) {
                                obj4 = obj("is_fullscreen", Boolean.TRUE);
                                notifyEvent(str16, obj4);
                                break;
                            }
                            obj3 = obj(str17, onFullscreenRequested);
                            str14 = "fullscreen_failed";
                            notifyEvent(str14, obj3);
                            break;
                        case '$':
                            JSONObject jSONObject11 = new JSONObject(str2);
                            ArrayList arrayList2 = new ArrayList();
                            JSONArray jSONArray3 = jSONObject11.getJSONArray("chat_types");
                            for (int i9 = 0; i9 < jSONArray3.length(); i9++) {
                                arrayList2.add(jSONArray3.getString(i9));
                            }
                            this.delegate.onWebAppSwitchInlineQuery(this.botUser, jSONObject11.getString("query"), arrayList2);
                            break;
                        case '%':
                            str17 = "error";
                            onFullscreenRequested = this.delegate.onFullscreenRequested(false);
                            if (onFullscreenRequested == null) {
                                obj4 = obj("is_fullscreen", Boolean.FALSE);
                                str16 = "fullscreen_changed";
                                notifyEvent(str16, obj4);
                                break;
                            }
                            obj3 = obj(str17, onFullscreenRequested);
                            str14 = "fullscreen_failed";
                            notifyEvent(str14, obj3);
                            break;
                        case '&':
                            if (!this.isRequestingPageOpen && this.botUser != null && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                                this.lastClickMs = 0L;
                                BaseFragment safeLastFragment2 = LaunchActivity.getSafeLastFragment();
                                if (safeLastFragment2 != null && safeLastFragment2.getParentLayout() != null) {
                                    INavigationLayout parentLayout2 = safeLastFragment2.getParentLayout();
                                    safeLastFragment2.presentFragment(ProfileActivity.of(this.botUser.id));
                                    AndroidUtilities.scrollToFragmentRow(parentLayout2, "botPermissionLocation");
                                    delegate = this.delegate;
                                    if (delegate == null) {
                                    }
                                    delegate.onCloseToTabs();
                                    break;
                                }
                            }
                            break;
                        case '\'':
                            boolean optBoolean3 = new JSONObject(str2).optBoolean("is_visible");
                            if (optBoolean3 != this.isBackButtonVisible) {
                                this.isBackButtonVisible = optBoolean3;
                                this.delegate.onSetBackButtonVisible(optBoolean3);
                                break;
                            }
                            break;
                        case '(':
                            try {
                                str20 = new JSONObject(str2).getString("reason");
                            } catch (Exception unused9) {
                            }
                            createBiometry();
                            BotBiometry botBiometry2 = this.biometry;
                            if (botBiometry2 != null) {
                                boolean z13 = botBiometry2.access_requested;
                                if (z13) {
                                    notifyBiometryReceived();
                                    break;
                                } else if (botBiometry2.access_granted) {
                                    if (!z13) {
                                        botBiometry2.access_requested = true;
                                        botBiometry2.save();
                                    }
                                    notifyBiometryReceived();
                                    break;
                                } else {
                                    final Runnable[] runnableArr = {new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda23
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            BotWebViewContainer.this.lambda$onEventReceived$24();
                                        }
                                    }};
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext(), this.resourcesProvider);
                                    if (TextUtils.isEmpty(str20)) {
                                        builder2.setTitle(LocaleController.getString(R.string.BotAllowBiometryTitle));
                                        builder2.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.BotAllowBiometryMessage, UserObject.getUserName(this.botUser))));
                                    } else {
                                        builder2.setTitle(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.BotAllowBiometryMessage, UserObject.getUserName(this.botUser))));
                                        builder2.setMessage(str20);
                                    }
                                    builder2.setPositiveButton(LocaleController.getString(R.string.Allow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda24
                                        @Override // android.content.DialogInterface.OnClickListener
                                        public final void onClick(DialogInterface dialogInterface, int i10) {
                                            BotWebViewContainer.this.lambda$onEventReceived$26(runnableArr, dialogInterface, i10);
                                        }
                                    });
                                    builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda25
                                        @Override // android.content.DialogInterface.OnClickListener
                                        public final void onClick(DialogInterface dialogInterface, int i10) {
                                            BotWebViewContainer.this.lambda$onEventReceived$27(runnableArr, dialogInterface, i10);
                                        }
                                    });
                                    builder2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda26
                                        @Override // android.content.DialogInterface.OnDismissListener
                                        public final void onDismiss(DialogInterface dialogInterface) {
                                            BotWebViewContainer.lambda$onEventReceived$28(runnableArr, dialogInterface);
                                        }
                                    });
                                    builder2.show();
                                    break;
                                }
                            }
                            break;
                        case ')':
                            JSONObject jSONObject12 = new JSONObject(str2);
                            String optString6 = jSONObject12.optString("type");
                            int hashCode = optString6.hashCode();
                            if (hashCode == -1184809658) {
                                if (optString6.equals("impact")) {
                                    c2 = 0;
                                    if (c2 != 0) {
                                    }
                                    if (botWebViewVibrationEffect2 == null) {
                                    }
                                }
                                c2 = 65535;
                                if (c2 != 0) {
                                }
                                if (botWebViewVibrationEffect2 == null) {
                                }
                            } else if (hashCode != 193071555) {
                                if (hashCode == 595233003 && optString6.equals("notification")) {
                                    c2 = 1;
                                    if (c2 != 0) {
                                        String optString7 = jSONObject12.optString("impact_style");
                                        switch (optString7.hashCode()) {
                                            case -1078030475:
                                                if (optString7.equals("medium")) {
                                                    c3 = 1;
                                                    break;
                                                }
                                                c3 = 65535;
                                                break;
                                            case 3535914:
                                                if (optString7.equals("soft")) {
                                                    c3 = 4;
                                                    break;
                                                }
                                                c3 = 65535;
                                                break;
                                            case 99152071:
                                                if (optString7.equals("heavy")) {
                                                    c3 = 2;
                                                    break;
                                                }
                                                c3 = 65535;
                                                break;
                                            case 102970646:
                                                if (optString7.equals("light")) {
                                                    c3 = 0;
                                                    break;
                                                }
                                                c3 = 65535;
                                                break;
                                            case 108511787:
                                                if (optString7.equals("rigid")) {
                                                    c3 = 3;
                                                    break;
                                                }
                                                c3 = 65535;
                                                break;
                                            default:
                                                c3 = 65535;
                                                break;
                                        }
                                        if (c3 == 0) {
                                            botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_LIGHT;
                                        } else if (c3 == 1) {
                                            botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_MEDIUM;
                                        } else if (c3 == 2) {
                                            botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_HEAVY;
                                        } else if (c3 == 3) {
                                            botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_RIGID;
                                        } else if (c3 == 4) {
                                            botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_SOFT;
                                        }
                                        botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                    } else if (c2 == 1) {
                                        String optString8 = jSONObject12.optString("notification_type");
                                        int hashCode2 = optString8.hashCode();
                                        if (hashCode2 == -1867169789) {
                                            if (optString8.equals("success")) {
                                                c4 = 1;
                                                if (c4 != 0) {
                                                }
                                                botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                            }
                                            c4 = 65535;
                                            if (c4 != 0) {
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        } else if (hashCode2 != 96784904) {
                                            if (hashCode2 == 1124446108 && optString8.equals("warning")) {
                                                c4 = 2;
                                                if (c4 != 0) {
                                                    botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_ERROR;
                                                } else if (c4 == 1) {
                                                    botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_SUCCESS;
                                                } else if (c4 == 2) {
                                                    botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_WARNING;
                                                }
                                                botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                            }
                                            c4 = 65535;
                                            if (c4 != 0) {
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        } else {
                                            if (optString8.equals("error")) {
                                                c4 = 0;
                                                if (c4 != 0) {
                                                }
                                                botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                            }
                                            c4 = 65535;
                                            if (c4 != 0) {
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        }
                                    } else if (c2 == 2) {
                                        botWebViewVibrationEffect = BotWebViewVibrationEffect.SELECTION_CHANGE;
                                        botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                    }
                                    if (botWebViewVibrationEffect2 == null) {
                                        botWebViewVibrationEffect2.vibrate();
                                        break;
                                    }
                                }
                                c2 = 65535;
                                if (c2 != 0) {
                                }
                                if (botWebViewVibrationEffect2 == null) {
                                }
                            } else {
                                if (optString6.equals("selection_change")) {
                                    c2 = 2;
                                    if (c2 != 0) {
                                    }
                                    if (botWebViewVibrationEffect2 == null) {
                                    }
                                }
                                c2 = 65535;
                                if (c2 != 0) {
                                }
                                if (botWebViewVibrationEffect2 == null) {
                                }
                            }
                            break;
                        case '*':
                            JSONObject jSONObject13 = new JSONObject(str2);
                            boolean optBoolean4 = jSONObject13.optBoolean("is_active", false);
                            String trim2 = jSONObject13.optString("text", this.lastButtonText).trim();
                            boolean z14 = jSONObject13.optBoolean("is_visible", false) && !TextUtils.isEmpty(trim2);
                            int parseColor3 = jSONObject13.has("color") ? Color.parseColor(jSONObject13.optString("color")) : this.lastButtonColor;
                            int parseColor4 = jSONObject13.has("text_color") ? Color.parseColor(jSONObject13.optString("text_color")) : this.lastButtonTextColor;
                            if (jSONObject13.optBoolean("is_progress_visible", false) && z14) {
                                str18 = "has_shine_effect";
                                z8 = true;
                            } else {
                                str18 = "has_shine_effect";
                                z8 = false;
                            }
                            boolean z15 = jSONObject13.optBoolean(str18, false) && z14;
                            this.lastButtonColor = parseColor3;
                            this.lastButtonTextColor = parseColor4;
                            this.lastButtonText = trim2;
                            this.buttonData = str2;
                            this.delegate.onSetupMainButton(z14, optBoolean4, trim2, parseColor3, parseColor4, z8, z15);
                            break;
                        case '+':
                            this.delegate.onWebAppSwipingBehavior(new JSONObject(str2).optBoolean("allow_vertical_swipe"));
                            break;
                        case ',':
                            boolean optBoolean5 = new JSONObject(str2).optBoolean("is_visible");
                            if (optBoolean5 != this.isSettingsButtonVisible) {
                                this.isSettingsButtonVisible = optBoolean5;
                                this.delegate.onSetSettingsButtonVisible(optBoolean5);
                                break;
                            }
                            break;
                        case '-':
                            obj3 = obj("status", (this.botUser == null || Build.VERSION.SDK_INT < 26) ? "unsupported" : MediaDataController.getInstance(this.currentAccount).isShortcutAdded(this.botUser.id, MediaDataController.SHORTCUT_TYPE_ATTACHED_BOT) ? "added" : "missed");
                            str14 = "home_screen_checked";
                            notifyEvent(str14, obj3);
                            break;
                        case '.':
                            BotSensors botSensors6 = this.delegate.getBotSensors();
                            try {
                                JSONObject jSONObject14 = new JSONObject(str2);
                                j4 = jSONObject14.getLong("refresh_rate");
                                z9 = jSONObject14.optBoolean("need_absolute", false);
                                j3 = j4;
                            } catch (Exception unused10) {
                                j3 = j4;
                                z9 = false;
                            }
                            long clamp3 = Utilities.clamp(j3, 1000L, 20L);
                            if (botSensors6 == null || !botSensors6.startOrientation(z9, clamp3)) {
                                obj = "UNSUPPORTED";
                                str4 = "error";
                                obj3 = obj(str4, obj);
                                str14 = "device_orientation_failed";
                                notifyEvent(str14, obj3);
                                break;
                            } else {
                                str5 = "device_orientation_started";
                                notifyEvent(str5, null);
                                break;
                            }
                            break;
                        case '/':
                            try {
                                JSONObject jSONObject15 = new JSONObject(str2);
                                final String string8 = jSONObject15.getString("token");
                                try {
                                    str21 = jSONObject15.getString("reason");
                                } catch (Exception unused11) {
                                }
                                createBiometry();
                                BotBiometry botBiometry3 = this.biometry;
                                if (botBiometry3 != null) {
                                    if (botBiometry3.access_granted) {
                                        botBiometry3.updateToken(str21, string8, new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda15
                                            @Override // org.telegram.messenger.Utilities.Callback
                                            public final void run(Object obj6) {
                                                BotWebViewContainer.this.lambda$onEventReceived$30(string8, (Boolean) obj6);
                                            }
                                        });
                                        break;
                                    } else {
                                        try {
                                            JSONObject jSONObject16 = new JSONObject();
                                            jSONObject16.put("status", "failed");
                                            notifyEvent("biometry_token_updated", jSONObject16);
                                            break;
                                        } catch (Exception e11) {
                                            FileLog.e(e11);
                                            return;
                                        }
                                    }
                                }
                            } catch (Exception e12) {
                                FileLog.e(e12);
                                if (e12 instanceof JSONException) {
                                    error("JSON Parse error");
                                    return;
                                } else {
                                    unknownError();
                                    return;
                                }
                            }
                            break;
                        case '0':
                            String optString9 = new JSONObject(str2).optString("color", null);
                            int color = TextUtils.isEmpty(optString9) ? Theme.getColor(Theme.key_windowBackgroundGray, this.resourcesProvider) : Color.parseColor(optString9);
                            Delegate delegate3 = this.delegate;
                            if (delegate3 != null) {
                                delegate3.onWebAppSetNavigationBarColor(color);
                                break;
                            }
                            break;
                        case '1':
                            JSONObject jSONObject17 = new JSONObject(str2);
                            String optString10 = jSONObject17.optString("color", null);
                            if (!TextUtils.isEmpty(optString10)) {
                                int parseColor5 = Color.parseColor(optString10);
                                if (parseColor5 != 0) {
                                    this.delegate.onWebAppSetActionBarColor(-1, parseColor5, true);
                                    break;
                                }
                            } else {
                                String optString11 = jSONObject17.optString("color_key");
                                int hashCode3 = optString11.hashCode();
                                if (hashCode3 != -1265068311) {
                                    if (hashCode3 == -210781868 && optString11.equals("secondary_bg_color")) {
                                        c5 = 1;
                                        if (c5 != 0) {
                                            i2 = Theme.key_windowBackgroundWhite;
                                        } else if (c5 != 1) {
                                            i3 = -1;
                                            if (i3 >= 0) {
                                                this.delegate.onWebAppSetActionBarColor(i3, Theme.getColor(i3, this.resourcesProvider), false);
                                                break;
                                            }
                                        } else {
                                            i2 = Theme.key_windowBackgroundGray;
                                        }
                                        i3 = i2;
                                        if (i3 >= 0) {
                                        }
                                    }
                                    c5 = 65535;
                                    if (c5 != 0) {
                                    }
                                    i3 = i2;
                                    if (i3 >= 0) {
                                    }
                                } else {
                                    if (optString11.equals("bg_color")) {
                                        c5 = 0;
                                        if (c5 != 0) {
                                        }
                                        i3 = i2;
                                        if (i3 >= 0) {
                                        }
                                    }
                                    c5 = 65535;
                                    if (c5 != 0) {
                                    }
                                    i3 = i2;
                                    if (i3 >= 0) {
                                    }
                                }
                            }
                            break;
                        case '2':
                            reportSafeInsets(this.lastInsets, true);
                            break;
                        case '3':
                            this.delegate.onWebAppSetBackgroundColor(Color.parseColor(new JSONObject(str2).optString("color", "#ffffff")) | (-16777216));
                            break;
                        case '4':
                            if (!ignoreDialog(3)) {
                                final int i10 = this.currentAccount;
                                final MyWebView myWebView3 = this.webView;
                                TL_bots.canSendMessage cansendmessage = new TL_bots.canSendMessage();
                                cansendmessage.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botUser);
                                ConnectionsManager connectionsManager3 = ConnectionsManager.getInstance(this.currentAccount);
                                requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda6
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                        BotWebViewContainer.this.lambda$onEventReceived$17(i10, myWebView3, tLObject, tL_error);
                                    }
                                };
                                checkdownloadfileparams = cansendmessage;
                                connectionsManager = connectionsManager3;
                                connectionsManager.sendRequest(checkdownloadfileparams, requestDelegate);
                                break;
                            } else {
                                try {
                                    JSONObject jSONObject18 = new JSONObject();
                                    jSONObject18.put("status", "cancelled");
                                    notifyEvent("write_access_requested", jSONObject18);
                                    break;
                                } catch (Exception e13) {
                                    FileLog.e(e13);
                                    return;
                                }
                            }
                        case '5':
                            this.delegate.onWebAppExpand();
                            break;
                        default:
                            FileLog.d("unknown webapp event " + str);
                            break;
                    }
                } catch (JSONException e14) {
                    e = e14;
                    FileLog.e(e);
                }
            } catch (Exception e15) {
                e = e15;
                FileLog.e(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onOpenUri(Uri uri) {
        onOpenUri(uri, null, !this.bot, false, false);
    }

    private void onOpenUri(Uri uri, String str, boolean z, boolean z2, boolean z3) {
        if (this.isRequestingPageOpen) {
            return;
        }
        if (System.currentTimeMillis() - this.lastClickMs <= 10000 || !z2) {
            this.lastClickMs = 0L;
            boolean[] zArr = {false};
            if (Browser.isInternalUri(uri, zArr) && !zArr[0] && this.delegate != null) {
                setKeyboardFocusable(false);
            }
            Browser.openUrl(getContext(), uri, true, z, false, null, str, false, true, z3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onWebEventReceived(String str, String str2) {
        boolean z;
        boolean z2;
        if (this.bot || this.delegate == null) {
            return;
        }
        d("onWebEventReceived " + str + " " + str2);
        str.hashCode();
        z = true;
        switch (str) {
            case "actionBarColor":
            case "navigationBarColor":
                try {
                    JSONArray jSONArray = new JSONArray(str2);
                    boolean equals = TextUtils.equals(str, "actionBarColor");
                    int argb = Color.argb((int) Math.round(jSONArray.optDouble(3, 1.0d) * 255.0d), (int) Math.round(jSONArray.optDouble(0)), (int) Math.round(jSONArray.optDouble(1)), (int) Math.round(jSONArray.optDouble(2)));
                    MyWebView myWebView = this.webView;
                    if (myWebView != null) {
                        if (equals) {
                            myWebView.lastActionBarColorGot = true;
                            myWebView.lastActionBarColor = argb;
                        } else {
                            myWebView.lastBackgroundColorGot = true;
                            myWebView.lastBackgroundColor = argb;
                        }
                        myWebView.saveHistory();
                    }
                    this.delegate.onWebAppBackgroundChanged(equals, argb);
                    break;
                } catch (Exception unused) {
                    return;
                }
            case "siteName":
                d("siteName " + str2);
                MyWebView myWebView2 = this.webView;
                if (myWebView2 != null) {
                    myWebView2.lastSiteName = str2;
                    myWebView2.saveHistory();
                    break;
                }
                break;
            case "allowScroll":
                try {
                    JSONArray jSONArray2 = new JSONArray(str2);
                    z2 = jSONArray2.optBoolean(0, true);
                    try {
                        z = jSONArray2.optBoolean(1, true);
                    } catch (Exception unused2) {
                    }
                } catch (Exception unused3) {
                    z2 = true;
                }
                d("allowScroll " + z2 + " " + z);
                if (getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) {
                    ((ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent()).allowThisScroll(z2, z);
                    break;
                }
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openQrScanActivity() {
        Activity activity = this.parentActivity;
        if (activity == null) {
            return;
        }
        this.cameraBottomSheet = CameraScanActivity.showAsSheet(activity, false, 3, new CameraScanActivity.CameraScanActivityDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer.6
            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public /* synthetic */ void didFindMrzInfo(MrzRecognizer.Result result) {
                CameraScanActivity.CameraScanActivityDelegate.-CC.$default$didFindMrzInfo(this, result);
            }

            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public void didFindQr(String str) {
                try {
                    BotWebViewContainer.this.lastClickMs = System.currentTimeMillis();
                    BotWebViewContainer.this.notifyEvent("qr_text_received", new JSONObject().put("data", str));
                } catch (JSONException e) {
                    FileLog.e(e);
                }
            }

            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public String getSubtitleText() {
                return BotWebViewContainer.this.lastQrText;
            }

            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public void onDismiss() {
                BotWebViewContainer.this.notifyEvent("scan_qr_popup_closed", null);
                BotWebViewContainer.this.hasQRPending = false;
            }

            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public /* synthetic */ boolean processQr(String str, Runnable runnable) {
                return CameraScanActivity.CameraScanActivityDelegate.-CC.$default$processQr(this, str, runnable);
            }
        });
    }

    public static WebResourceResponse proxyTON(WebResourceRequest webResourceRequest) {
        String method;
        Uri url;
        Map requestHeaders;
        if (Build.VERSION.SDK_INT < 21) {
            return null;
        }
        method = webResourceRequest.getMethod();
        url = webResourceRequest.getUrl();
        String uri = url.toString();
        requestHeaders = webResourceRequest.getRequestHeaders();
        return proxyTON(method, uri, requestHeaders);
    }

    public static WebResourceResponse proxyTON(String str, String str2, Map map) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Browser.replaceHostname(Uri.parse(str2), rotateTONHost(AndroidUtilities.getHostAuthority(str2)), "https")).openConnection();
            httpURLConnection.setRequestMethod(str);
            if (map != null) {
                for (Map.Entry entry : map.entrySet()) {
                    httpURLConnection.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
                }
            }
            httpURLConnection.connect();
            return new WebResourceResponse(httpURLConnection.getContentType().split(";", 2)[0], httpURLConnection.getContentEncoding(), httpURLConnection.getInputStream());
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    private void reportSafeContentInsets(int i, boolean z) {
        if (z || i != this.lastInsetsTopMargin) {
            notifyEvent("content_safe_area_changed", obj("left", 0, "top", Float.valueOf(i / AndroidUtilities.density), "right", 0, "bottom", 0));
            this.lastInsetsTopMargin = i;
        }
    }

    private void reportSafeInsets(Rect rect, boolean z) {
        if (rect != null) {
            if (z || !this.lastInsets.equals(rect)) {
                notifyEvent("safe_area_changed", obj("left", Float.valueOf(rect.left / AndroidUtilities.density), "top", Float.valueOf(rect.top / AndroidUtilities.density), "right", Float.valueOf(rect.right / AndroidUtilities.density), "bottom", Float.valueOf(rect.bottom / AndroidUtilities.density)));
                this.lastInsets.set(rect);
            }
        }
    }

    public static String rotateTONHost(String str) {
        try {
            str = IDN.toASCII(str, 1);
        } catch (Exception e) {
            FileLog.e(e);
        }
        String[] split = str.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i > 0) {
                sb.append("-d");
            }
            sb.append(split[i].replaceAll("\\-", "-h"));
        }
        sb.append(".");
        sb.append(MessagesController.getInstance(UserConfig.selectedAccount).tonProxyAddress);
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runWithPermissions(final String[] strArr, final Consumer consumer) {
        if (Build.VERSION.SDK_INT < 23 || checkPermissions(strArr)) {
            consumer.accept(Boolean.TRUE);
            return;
        }
        this.onPermissionsRequestResultCallback = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$runWithPermissions$0(consumer, strArr);
            }
        };
        Activity activity = this.parentActivity;
        if (activity != null) {
            activity.requestPermissions(strArr, 4000);
        }
    }

    private void setupFlickerParams(boolean z) {
        this.isFlickeringCenter = z;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.flickerView.getLayoutParams();
        layoutParams.gravity = z ? 17 : 48;
        if (z) {
            int dp = AndroidUtilities.dp(100.0f);
            layoutParams.height = dp;
            layoutParams.width = dp;
        } else {
            layoutParams.width = -1;
            layoutParams.height = -2;
        }
        this.flickerView.requestLayout();
    }

    private void setupWebView(MyWebView myWebView) {
        setupWebView(myWebView, null);
    }

    private void setupWebView(MyWebView myWebView, Object obj) {
        MyWebView myWebView2 = this.webView;
        if (myWebView2 != null) {
            myWebView2.destroy();
            removeView(this.webView);
        }
        if (myWebView != null) {
            AndroidUtilities.removeFromParent(myWebView);
        }
        MyWebView myWebView3 = myWebView == null ? new MyWebView(getContext(), this.bot) : myWebView;
        this.webView = myWebView3;
        if (this.bot) {
            myWebView3.setBackgroundColor(getColor(Theme.key_windowBackgroundWhite));
        } else {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            int i = Build.VERSION.SDK_INT;
            if (i >= 21) {
                cookieManager.setAcceptThirdPartyCookies(this.webView, true);
            }
            if (i >= 21) {
                CookieManager.getInstance().flush();
            }
            this.webView.opener = this.opener;
        }
        if (!MessagesController.getInstance(this.currentAccount).disableBotFullscreenBlur) {
            this.webView.setLayerType(2, null);
        }
        this.webView.setContainers(this, this.webViewScrollListener);
        this.webView.setCloseListener(this.onCloseListener);
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        if (!this.bot) {
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            settings.setCacheMode(-1);
            settings.setSaveFormData(true);
            settings.setSavePassword(true);
            settings.setSupportZoom(true);
            settings.setBuiltInZoomControls(true);
            settings.setDisplayZoomControls(false);
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            if (Build.VERSION.SDK_INT >= 26) {
                settings.setSafeBrowsingEnabled(true);
            }
        }
        try {
            String replace = settings.getUserAgentString().replace("; wv)", ")");
            StringBuilder sb = new StringBuilder();
            sb.append("(Linux; Android ");
            String str = Build.VERSION.RELEASE;
            sb.append(str);
            sb.append("; K)");
            String replaceAll = replace.replaceAll("\\(Linux; Android.+;[^)]+\\)", sb.toString()).replaceAll("Version/[\\d\\.]+ ", "");
            if (this.bot) {
                PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                int devicePerformanceClass = SharedConfig.getDevicePerformanceClass();
                replaceAll = replaceAll + " Telegram-Android/" + packageInfo.versionName + " (" + capitalizeFirst(Build.MANUFACTURER) + " " + Build.MODEL + "; Android " + str + "; SDK " + Build.VERSION.SDK_INT + "; " + (devicePerformanceClass == 0 ? "LOW" : devicePerformanceClass == 1 ? "AVERAGE" : "HIGH") + ")";
            }
            settings.setUserAgentString(replaceAll);
        } catch (Exception e) {
            FileLog.e(e);
        }
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        File file = new File(ApplicationLoader.getFilesDirFixed(), "webview_database");
        if ((file.exists() && file.isDirectory()) || file.mkdirs()) {
            settings.setDatabasePath(file.getAbsolutePath());
        }
        GeolocationPermissions.getInstance().clearAll();
        this.webView.setVerticalScrollBarEnabled(false);
        if (myWebView == null && this.bot) {
            this.webView.setAlpha(0.0f);
        }
        addView(this.webView);
        if (this.bot) {
            if (obj instanceof BotWebViewProxy) {
                this.botWebViewProxy = (BotWebViewProxy) obj;
            }
            BotWebViewProxy botWebViewProxy = this.botWebViewProxy;
            if (botWebViewProxy == null) {
                BotWebViewProxy botWebViewProxy2 = new BotWebViewProxy(this);
                this.botWebViewProxy = botWebViewProxy2;
                this.webView.addJavascriptInterface(botWebViewProxy2, "TelegramWebviewProxy");
            } else if (myWebView == null) {
                this.webView.addJavascriptInterface(botWebViewProxy, "TelegramWebviewProxy");
            }
            this.botWebViewProxy.setContainer(this);
        } else {
            if (obj instanceof WebViewProxy) {
                this.webViewProxy = (WebViewProxy) obj;
            }
            WebViewProxy webViewProxy = this.webViewProxy;
            if (webViewProxy == null) {
                WebViewProxy webViewProxy2 = new WebViewProxy(this.webView, this);
                this.webViewProxy = webViewProxy2;
                this.webView.addJavascriptInterface(webViewProxy2, "TelegramWebview");
            } else if (myWebView == null) {
                this.webView.addJavascriptInterface(webViewProxy, "TelegramWebview");
            }
            this.webViewProxy.setContainer(this);
        }
        onWebViewCreated(this.webView);
        firstWebView = false;
    }

    private boolean showDialog(int i, AlertDialog alertDialog, final Runnable runnable) {
        if (alertDialog == null || ignoreDialog(i)) {
            return false;
        }
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda35
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                BotWebViewContainer.this.lambda$showDialog$48(runnable, dialogInterface);
            }
        });
        this.currentDialog = alertDialog;
        alertDialog.setDismissDialogByButtons(false);
        this.currentDialog.show();
        if (this.lastDialogType != i) {
            this.lastDialogType = i;
            this.shownDialogsCount = 0;
            this.blockedDialogsUntil = 0L;
        }
        this.shownDialogsCount++;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String tonsite2magic(String str) {
        if (str == null || !isTonsite(Uri.parse(str))) {
            return str;
        }
        String hostAuthority = AndroidUtilities.getHostAuthority(str);
        try {
            hostAuthority = IDN.toASCII(hostAuthority, 1);
        } catch (Exception unused) {
        }
        String rotateTONHost = rotateTONHost(hostAuthority);
        if (rotatedTONHosts == null) {
            rotatedTONHosts = new HashMap();
        }
        rotatedTONHosts.put(rotateTONHost, hostAuthority);
        return Browser.replaceHostname(Uri.parse(str), rotateTONHost, "https");
    }

    private void unknownError() {
        unknownError(null);
    }

    private void unknownError(String str) {
        String str2;
        StringBuilder sb = new StringBuilder();
        sb.append(LocaleController.getString("UnknownError", R.string.UnknownError));
        if (str != null) {
            str2 = ": " + str;
        } else {
            str2 = "";
        }
        sb.append(str2);
        error(sb.toString());
    }

    private void updateKeyboardFocusable() {
        if (this.wasFocusable) {
            setDescendantFocusability(393216);
            setFocusable(false);
            MyWebView myWebView = this.webView;
            if (myWebView != null) {
                myWebView.setDescendantFocusability(393216);
                this.webView.clearFocus();
            }
            AndroidUtilities.hideKeyboard(this);
        }
        this.wasFocusable = false;
    }

    public void checkCreateWebView() {
        if (this.webView != null || this.webViewNotAvailable) {
            return;
        }
        try {
            setupWebView(null);
        } catch (Throwable th) {
            FileLog.e(th);
            this.flickerView.setVisibility(8);
            this.webViewNotAvailable = true;
            this.webViewNotAvailableText.setVisibility(0);
            if (this.webView != null) {
                removeView(this.webView);
            }
        }
    }

    public void d(String str) {
        FileLog.d("[webviewcontainer] #" + this.tag + " " + str);
    }

    public void destroyWebView() {
        d("destroyWebView preserving=" + this.preserving);
        MyWebView myWebView = this.webView;
        if (myWebView != null) {
            if (myWebView.getParent() != null) {
                removeView(this.webView);
            }
            if (!this.preserving) {
                this.webView.destroy();
                onWebViewDestroyed(this.webView);
            }
            this.isPageLoaded = false;
            updateKeyboardFocusable();
            if (this.biometry != null) {
                this.biometry = null;
            }
            BotLocation botLocation = this.location;
            if (botLocation != null) {
                botLocation.unlisten(this.notifyLocationChecked);
                this.location = null;
            }
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i != NotificationCenter.didSetNewTheme) {
            if (i == NotificationCenter.onActivityResultReceived) {
                onActivityResult(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (Intent) objArr[2]);
                return;
            } else {
                if (i == NotificationCenter.onRequestPermissionResultReceived) {
                    onRequestPermissionsResult(((Integer) objArr[0]).intValue(), (String[]) objArr[1], (int[]) objArr[2]);
                    return;
                }
                return;
            }
        }
        MyWebView myWebView = this.webView;
        if (myWebView != null) {
            myWebView.setBackgroundColor(getColor(Theme.key_windowBackgroundWhite));
        }
        if (!this.flickerViewColorOverriden) {
            BackupImageView backupImageView = this.flickerView;
            int i3 = Theme.key_bot_loadingIcon;
            int color = getColor(i3);
            this.flickerViewColor = color;
            backupImageView.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            SvgHelper.SvgDrawable svgDrawable = this.flickerViewDrawable;
            if (svgDrawable != null) {
                svgDrawable.setColor(this.flickerViewColor);
                this.flickerViewDrawable.setupGradient(i3, this.resourcesProvider, 1.0f, false);
            }
            this.flickerView.invalidate();
        }
        notifyThemeChanged();
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (view == this.flickerView) {
            if (this.isFlickeringCenter) {
                canvas.save();
                canvas.translate(0.0f, (ActionBar.getCurrentActionBarHeight() - ((View) getParent()).getTranslationY()) / 2.0f);
            }
            boolean drawChild = super.drawChild(canvas, view, j);
            if (this.isFlickeringCenter) {
                canvas.restore();
            }
            if (!this.isFlickeringCenter) {
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                this.flickerDrawable.draw(canvas, rectF, 0.0f, this);
                invalidate();
            }
            return drawChild;
        }
        if (view == this.webViewNotAvailableText) {
            canvas.save();
            canvas.translate(0.0f, (ActionBar.getCurrentActionBarHeight() - ((View) getParent()).getTranslationY()) / 2.0f);
            boolean drawChild2 = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild2;
        }
        if (view == this.webView) {
            if (AndroidUtilities.makingGlobalBlurBitmap) {
                return true;
            }
            if (getLayerType() == 2 && !canvas.isHardwareAccelerated()) {
                return true;
            }
        }
        return super.drawChild(canvas, view, j);
    }

    public void evaluateJs(final String str, final boolean z) {
        NotificationCenter.getInstance(this.currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$evaluateJs$3(z, str);
            }
        });
    }

    public BotWebViewProxy getBotProxy() {
        return this.botWebViewProxy;
    }

    public int getMinHeight() {
        if (!(getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer)) {
            return 0;
        }
        ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = (ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent();
        if (webViewSwipeContainer.isFullSize()) {
            return (int) (((webViewSwipeContainer.getMeasuredHeight() - webViewSwipeContainer.getOffsetY()) - webViewSwipeContainer.getTopActionBarOffsetY()) + this.viewPortHeightOffset);
        }
        return 0;
    }

    public WebViewProxy getProxy() {
        return this.webViewProxy;
    }

    public String getUrlLoaded() {
        return this.mUrl;
    }

    public MyWebView getWebView() {
        return this.webView;
    }

    public boolean hasUserPermissions() {
        return this.hasUserPermissions;
    }

    public void invalidateViewPortHeight() {
        invalidateViewPortHeight(false);
    }

    public void invalidateViewPortHeight(boolean z) {
        invalidateViewPortHeight(z, false);
    }

    public void invalidateViewPortHeight(boolean z, boolean z2) {
        invalidate();
        if ((this.isPageLoaded || z2) && this.bot && (getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer)) {
            ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = (ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent();
            if (z) {
                this.lastExpanded = webViewSwipeContainer.getSwipeOffsetY() == (-webViewSwipeContainer.getOffsetY()) + webViewSwipeContainer.getTopActionBarOffsetY();
            }
            int max = Math.max(getMinHeight(), (int) (((webViewSwipeContainer.getMeasuredHeight() - webViewSwipeContainer.getOffsetY()) - webViewSwipeContainer.getSwipeOffsetY()) + webViewSwipeContainer.getTopActionBarOffsetY() + this.viewPortHeightOffset));
            if (!z2 && max == this.lastViewportHeightReported && this.lastViewportStateStable == z && this.lastViewportIsExpanded == this.lastExpanded) {
                return;
            }
            this.lastViewportHeightReported = max;
            this.lastViewportStateStable = z;
            this.lastViewportIsExpanded = this.lastExpanded;
            notifyEvent_fast("viewport_changed", "{height:" + (max / AndroidUtilities.density) + ",is_state_stable:" + z + ",is_expanded:" + this.lastExpanded + "}");
        }
    }

    public boolean isBackButtonVisible() {
        return this.isBackButtonVisible;
    }

    public boolean isPageLoaded() {
        return this.isPageLoaded;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x00bc, code lost:
    
        if (r11 != null) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x010e, code lost:
    
        r9.flickerView.setImage(null, null, r9.flickerViewDrawable);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0100, code lost:
    
        r11.setColor(r9.flickerViewColor);
        r9.flickerViewDrawable.setupGradient(org.telegram.ui.ActionBar.Theme.key_bot_loadingIcon, r9.resourcesProvider, 1.0f, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00fe, code lost:
    
        if (r11 != null) goto L37;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadFlickerAndSettingsItem(int i, long j, ActionBarMenuSubItem actionBarMenuSubItem) {
        TLRPC.TL_attachMenuBot tL_attachMenuBot;
        SvgHelper.SvgDrawable drawableByPath;
        TL_bots.BotInfo botInfo;
        TL_bots.botAppSettings botappsettings;
        TLRPC.User user = MessagesController.getInstance(i).getUser(Long.valueOf(j));
        TLRPC.UserFull userFull = MessagesController.getInstance(i).getUserFull(j);
        String publicUsername = UserObject.getPublicUsername(user);
        if (publicUsername != null && publicUsername.equals("DurgerKingBot")) {
            this.flickerView.setVisibility(0);
            this.flickerView.setAlpha(1.0f);
            this.flickerView.setImage(null, null, SvgHelper.getDrawable(R.raw.durgerking_placeholder, Integer.valueOf(getColor(Theme.key_windowBackgroundGray))));
            setupFlickerParams(false);
            return;
        }
        Iterator<TLRPC.TL_attachMenuBot> it = MediaDataController.getInstance(i).getAttachMenuBots().bots.iterator();
        while (true) {
            if (!it.hasNext()) {
                tL_attachMenuBot = null;
                break;
            } else {
                tL_attachMenuBot = it.next();
                if (tL_attachMenuBot.bot_id == j) {
                    break;
                }
            }
        }
        boolean z = true;
        if (tL_attachMenuBot != null) {
            TLRPC.TL_attachMenuBotIcon placeholderStaticAttachMenuBotIcon = MediaDataController.getPlaceholderStaticAttachMenuBotIcon(tL_attachMenuBot);
            if (placeholderStaticAttachMenuBotIcon == null) {
                placeholderStaticAttachMenuBotIcon = MediaDataController.getStaticAttachMenuBotIcon(tL_attachMenuBot);
            } else {
                z = false;
            }
            if (placeholderStaticAttachMenuBotIcon == null) {
                return;
            }
            this.flickerView.setVisibility(0);
            this.flickerView.setAlpha(1.0f);
            this.flickerView.setImage(ImageLocation.getForDocument(placeholderStaticAttachMenuBotIcon.icon), (String) null, (Drawable) null, tL_attachMenuBot);
        } else if (userFull == null || (botInfo = userFull.bot_info) == null || (botappsettings = botInfo.app_settings) == null || botappsettings.placeholder_svg_path == null) {
            Path path = new Path();
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(106.66499f, 106.66499f, 240.355f, 240.355f);
            Path.Direction direction = Path.Direction.CW;
            path.addRoundRect(rectF, 18.0f, 18.0f, direction);
            rectF.set(271.645f, 106.66499f, 405.335f, 240.355f);
            path.addRoundRect(rectF, 18.0f, 18.0f, direction);
            rectF.set(106.66499f, 271.645f, 240.355f, 405.335f);
            path.addRoundRect(rectF, 18.0f, 18.0f, direction);
            rectF.set(271.645f, 271.645f, 405.335f, 405.335f);
            path.addRoundRect(rectF, 18.0f, 18.0f, direction);
            this.flickerView.setVisibility(0);
            this.flickerView.setAlpha(1.0f);
            drawableByPath = SvgHelper.getDrawableByPath(path, 512, 512);
            this.flickerViewDrawable = drawableByPath;
        } else {
            this.flickerView.setVisibility(0);
            this.flickerView.setAlpha(1.0f);
            drawableByPath = SvgHelper.getDrawableByPath(userFull.bot_info.app_settings.placeholder_svg_path, 512, 512);
            this.flickerViewDrawable = drawableByPath;
        }
        setupFlickerParams(z);
    }

    public void loadUrl(int i, final String str) {
        this.currentAccount = i;
        NotificationCenter.getInstance(i).doOnIdle(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$loadUrl$2(str);
            }
        });
    }

    public void notifyEmojiStatusAccess(String str) {
        notifyEvent("emoji_status_access_requested", obj("status", str));
    }

    public void notifyThemeChanged() {
        notifyEvent("theme_changed", buildThemeParams());
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 3000 || this.mFilePathCallback == null) {
            return;
        }
        this.mFilePathCallback.onReceiveValue((i2 != -1 || intent == null || intent.getDataString() == null) ? null : new Uri[]{Uri.parse(intent.getDataString())});
        this.mFilePathCallback = null;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        d("attached");
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.onActivityResultReceived);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.onRequestPermissionResultReceived);
        Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.web.BotWebViewContainer.3
            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean allowLayoutChanges() {
                return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean bottomOffsetAnimated() {
                return Bulletin.Delegate.-CC.$default$bottomOffsetAnimated(this);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ boolean clipWithGradient(int i) {
                return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getBottomOffset(int i) {
                if (!(BotWebViewContainer.this.getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer)) {
                    return 0;
                }
                ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = (ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) BotWebViewContainer.this.getParent();
                return (int) ((webViewSwipeContainer.getOffsetY() + webViewSwipeContainer.getSwipeOffsetY()) - webViewSwipeContainer.getTopActionBarOffsetY());
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ int getTopOffset(int i) {
                return Bulletin.Delegate.-CC.$default$getTopOffset(this, i);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onBottomOffsetChange(float f) {
                Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onHide(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public /* synthetic */ void onShow(Bulletin bulletin) {
                Bulletin.Delegate.-CC.$default$onShow(this, bulletin);
            }
        });
    }

    public boolean onBackPressed() {
        if (this.webView == null || !this.isBackButtonVisible) {
            return false;
        }
        notifyEvent("back_button_pressed", null);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        d("detached");
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.onActivityResultReceived);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.onRequestPermissionResultReceived);
        Bulletin.removeDelegate(this);
    }

    protected void onErrorShown(boolean z, int i, String str) {
    }

    protected void onFaviconChanged(Bitmap bitmap) {
    }

    public void onInvoiceStatusUpdate(String str, String str2) {
        onInvoiceStatusUpdate(str, str2, false);
    }

    public void onInvoiceStatusUpdate(String str, String str2, boolean z) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("slug", str);
            jSONObject.put("status", str2);
            notifyEvent("invoice_closed", jSONObject);
            FileLog.d("invoice_closed " + jSONObject);
            if (z || !Objects.equals(this.currentPaymentSlug, str)) {
                return;
            }
            this.currentPaymentSlug = null;
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    public void onMainButtonPressed() {
        this.lastClickMs = System.currentTimeMillis();
        notifyEvent("main_button_pressed", null);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3 = this.forceHeight;
        if (i3 >= 0) {
            i2 = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
        }
        super.onMeasure(i, i2);
        this.flickerDrawable.setParentWidth(getMeasuredWidth());
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Runnable runnable;
        if (i != 4000 || (runnable = this.onPermissionsRequestResultCallback) == null) {
            return;
        }
        runnable.run();
        this.onPermissionsRequestResultCallback = null;
    }

    public void onSecondaryButtonPressed() {
        this.lastClickMs = System.currentTimeMillis();
        notifyEvent("secondary_button_pressed", null);
    }

    public void onSettingsButtonPressed() {
        this.lastClickMs = System.currentTimeMillis();
        notifyEvent("settings_button_pressed", null);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.isViewPortByMeasureSuppressed) {
            return;
        }
        invalidateViewPortHeight(true);
    }

    protected void onTitleChanged(String str) {
    }

    protected void onURLChanged(String str, boolean z, boolean z2) {
    }

    public void onWebViewCreated(MyWebView myWebView) {
    }

    public void onWebViewDestroyed(MyWebView myWebView) {
    }

    public void preserveWebView() {
        d("preserveWebView");
        this.preserving = true;
        if (this.bot) {
            notifyEvent("visibility_changed", obj("is_visible", Boolean.FALSE));
        }
    }

    public void reload() {
        NotificationCenter.getInstance(this.currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$reload$1();
            }
        });
    }

    public void replaceWebView(int i, MyWebView myWebView, Object obj) {
        this.currentAccount = i;
        setupWebView(myWebView, obj);
        if (this.bot) {
            notifyEvent("visibility_changed", obj("is_visible", Boolean.TRUE));
        }
    }

    public void reportSafeInsets(Rect rect, int i) {
        reportSafeInsets(rect, false);
        reportSafeContentInsets(i, false);
    }

    public void resetWebView() {
        this.webView = null;
    }

    public void restoreButtonData() {
        try {
            String str = this.buttonData;
            if (str != null) {
                onEventReceived(this.botWebViewProxy, "web_app_setup_main_button", str);
            }
            String str2 = this.secondaryButtonData;
            if (str2 != null) {
                onEventReceived(this.botWebViewProxy, "web_app_setup_secondary_button", str2);
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void setBotUser(TLRPC.User user) {
        this.botUser = user;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void setFlickerViewColor(int i) {
        float f;
        float f2;
        if (AndroidUtilities.computePerceivedBrightness(i) > 0.7f) {
            f = 0.0f;
            f2 = -0.15f;
        } else {
            f = 0.025f;
            f2 = 0.15f;
        }
        int adaptHSV = Theme.adaptHSV(i, f, f2);
        if (this.flickerViewColor == adaptHSV) {
            return;
        }
        BackupImageView backupImageView = this.flickerView;
        this.flickerViewColor = adaptHSV;
        backupImageView.setColorFilter(new PorterDuffColorFilter(adaptHSV, PorterDuff.Mode.SRC_IN));
        SvgHelper.SvgDrawable svgDrawable = this.flickerViewDrawable;
        if (svgDrawable != null) {
            svgDrawable.setColor(this.flickerViewColor);
            this.flickerViewDrawable.setupGradient(Theme.key_bot_loadingIcon, this.resourcesProvider, 1.0f, false);
        }
        this.flickerViewColorOverriden = true;
        this.flickerView.invalidate();
        invalidate();
    }

    public void setForceHeight(int i) {
        if (this.forceHeight == i) {
            return;
        }
        this.forceHeight = i;
        requestLayout();
    }

    public void setIsBackButtonVisible(boolean z) {
        this.isBackButtonVisible = z;
    }

    public void setKeyboardFocusable(boolean z) {
        this.keyboardFocusable = z;
        updateKeyboardFocusable();
    }

    public void setOnCloseRequestedListener(Runnable runnable) {
        this.onCloseListener = runnable;
        MyWebView myWebView = this.webView;
        if (myWebView != null) {
            myWebView.setCloseListener(runnable);
        }
    }

    public void setOpener(MyWebView myWebView) {
        MyWebView myWebView2;
        this.opener = myWebView;
        if (this.bot || (myWebView2 = this.webView) == null) {
            return;
        }
        myWebView2.opener = myWebView;
    }

    public void setPageLoaded(String str, boolean z) {
        MyWebView myWebView = this.webView;
        String str2 = (myWebView == null || !myWebView.dangerousUrl) ? str : myWebView.urlFallback;
        boolean z2 = myWebView == null || !myWebView.canGoBack();
        MyWebView myWebView2 = this.webView;
        onURLChanged(str2, z2, myWebView2 == null || !myWebView2.canGoForward());
        MyWebView myWebView3 = this.webView;
        if (myWebView3 != null) {
            myWebView3.isPageLoaded = true;
            updateKeyboardFocusable();
        }
        if (this.isPageLoaded) {
            d("setPageLoaded: already loaded");
            return;
        }
        if (!z || this.webView == null || this.flickerView == null) {
            MyWebView myWebView4 = this.webView;
            if (myWebView4 != null) {
                myWebView4.setAlpha(1.0f);
            }
            BackupImageView backupImageView = this.flickerView;
            if (backupImageView != null) {
                backupImageView.setAlpha(0.0f);
                this.flickerView.setVisibility(8);
            }
        } else {
            AnimatorSet animatorSet = new AnimatorSet();
            MyWebView myWebView5 = this.webView;
            Property property = View.ALPHA;
            animatorSet.playTogether(ObjectAnimator.ofFloat(myWebView5, (Property<MyWebView, Float>) property, 1.0f), ObjectAnimator.ofFloat(this.flickerView, (Property<BackupImageView, Float>) property, 0.0f));
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.web.BotWebViewContainer.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    BotWebViewContainer.this.flickerView.setVisibility(8);
                }
            });
            animatorSet.start();
        }
        this.mUrl = str;
        d("setPageLoaded: isPageLoaded = true!");
        this.isPageLoaded = true;
        updateKeyboardFocusable();
        this.delegate.onWebAppReady();
    }

    public void setParentActivity(Activity activity) {
        this.parentActivity = activity;
    }

    public void setState(boolean z, String str) {
        d("setState(" + z + ", " + str + ")");
        this.isPageLoaded = z;
        this.mUrl = str;
        updateKeyboardFocusable();
    }

    public void setViewPortByMeasureSuppressed(boolean z) {
        this.isViewPortByMeasureSuppressed = z;
    }

    public void setViewPortHeightOffset(float f) {
        this.viewPortHeightOffset = f;
    }

    public void setWasOpenedByBot(WebViewRequestProps webViewRequestProps) {
        this.wasOpenedByBot = webViewRequestProps;
    }

    public void setWasOpenedByLinkIntent(boolean z) {
        this.wasOpenedByLinkIntent = z;
    }

    public void setWebViewProgressListener(Consumer consumer) {
        this.webViewProgressListener = consumer;
    }

    public void setWebViewScrollListener(WebViewScrollListener webViewScrollListener) {
        this.webViewScrollListener = webViewScrollListener;
        MyWebView myWebView = this.webView;
        if (myWebView != null) {
            myWebView.setContainers(this, webViewScrollListener);
        }
    }

    public void showLinkCopiedBulletin() {
        BulletinFactory.of(this, this.resourcesProvider).createCopyLinkBulletin().show(true);
    }

    public void updateFlickerBackgroundColor(int i) {
        this.flickerDrawable.setColors(i, NotificationCenter.recordStopped, NotificationCenter.groupPackUpdated);
    }
}
