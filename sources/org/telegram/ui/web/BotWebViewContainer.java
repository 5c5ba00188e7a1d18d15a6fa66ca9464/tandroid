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
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
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
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.telegram.ui.Stories.recorder.StoryRecorder;
import org.telegram.ui.WrappedResourceProvider;
import org.telegram.ui.bots.BotBiometry;
import org.telegram.ui.bots.BotBiometrySettings;
import org.telegram.ui.bots.BotWebViewSheet;
import org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout;
import org.telegram.ui.bots.WebViewRequestProps;
import org.telegram.ui.web.BotWebViewContainer;
import org.telegram.ui.web.BrowserHistory;
import org.telegram.ui.web.WebMetadataCache;

/* loaded from: classes.dex */
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
    private final CellFlickerDrawable flickerDrawable;
    private BackupImageView flickerView;
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
    private long lastPostStoryMs;
    private String lastQrText;
    private int lastSecondaryButtonColor;
    private String lastSecondaryButtonPosition;
    private String lastSecondaryButtonText;
    private int lastSecondaryButtonTextColor;
    private int lastViewportHeightReported;
    private boolean lastViewportIsExpanded;
    private boolean lastViewportStateStable;
    private ValueCallback mFilePathCallback;
    private String mUrl;
    private Runnable onCloseListener;
    private Runnable onPermissionsRequestResultCallback;
    private MyWebView opener;
    private Activity parentActivity;
    private boolean preserving;
    private Theme.ResourcesProvider resourcesProvider;
    private String secondaryButtonData;
    private int shownDialogsCount;
    private final int tag;
    private boolean wasFocusable;
    private WebViewRequestProps wasOpenedByBot;
    private boolean wasOpenedByLinkIntent;
    private MyWebView webView;
    private boolean webViewNotAvailable;
    private TextView webViewNotAvailableText;
    private Consumer webViewProgressListener;
    private WebViewProxy webViewProxy;
    private WebViewScrollListener webViewScrollListener;

    /* loaded from: classes.dex */
    public class 1 extends BackupImageView {

        /* loaded from: classes.dex */
        public class 1 extends ImageReceiver {
            1(View view) {
                super(view);
            }

            public /* synthetic */ void lambda$setImageBitmapByKey$0(ValueAnimator valueAnimator) {
                ((BackupImageView) 1.this).imageReceiver.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                invalidate();
            }

            @Override // org.telegram.messenger.ImageReceiver
            public boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z, int i2) {
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

        1(Context context) {
            super(context);
            this.imageReceiver = new 1(this);
        }

        @Override // org.telegram.ui.Components.BackupImageView, android.view.View
        public void onDraw(Canvas canvas) {
            if (BotWebViewContainer.this.isFlickeringCenter) {
                super.onDraw(canvas);
                return;
            }
            if (this.imageReceiver.getDrawable() != null) {
                this.imageReceiver.setImageCoords(0.0f, 0.0f, getWidth(), r0.getIntrinsicHeight() * (getWidth() / r0.getIntrinsicWidth()));
                this.imageReceiver.draw(canvas);
            }
        }
    }

    /* loaded from: classes.dex */
    public class 2 extends AnimatorListenerAdapter {
        2() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BotWebViewContainer.this.flickerView.setVisibility(8);
        }
    }

    /* loaded from: classes.dex */
    class 3 implements Bulletin.Delegate {
        3() {
        }

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
    }

    /* loaded from: classes.dex */
    public class 4 implements NotificationCenter.NotificationCenterDelegate {
        4() {
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            int i3 = NotificationCenter.onRequestPermissionResultReceived;
            if (i == i3) {
                int intValue = ((Integer) objArr[0]).intValue();
                int[] iArr = (int[]) objArr[2];
                if (intValue == 5000) {
                    NotificationCenter.getGlobalInstance().removeObserver(this, i3);
                    if (iArr[0] == 0) {
                        BotWebViewContainer.this.openQrScanActivity();
                    } else {
                        BotWebViewContainer.this.notifyEvent("scan_qr_popup_closed", new JSONObject());
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public class 5 extends BaseFragment {

        /* loaded from: classes.dex */
        class 1 extends WrappedResourceProvider {
            1(Theme.ResourcesProvider resourcesProvider) {
                super(resourcesProvider);
            }

            @Override // org.telegram.ui.WrappedResourceProvider
            public void appendColors() {
                this.sparseIntArray.append(Theme.key_dialogBackground, -14803426);
                this.sparseIntArray.append(Theme.key_windowBackgroundGray, -16777216);
            }
        }

        5() {
            this.currentAccount = BotWebViewContainer.this.currentAccount;
        }

        @Override // org.telegram.ui.ActionBar.BaseFragment
        public Activity getParentActivity() {
            return BotWebViewContainer.this.parentActivity;
        }

        @Override // org.telegram.ui.ActionBar.BaseFragment
        public Theme.ResourcesProvider getResourceProvider() {
            return new WrappedResourceProvider(BotWebViewContainer.this.resourcesProvider) { // from class: org.telegram.ui.web.BotWebViewContainer.5.1
                1(Theme.ResourcesProvider resourcesProvider) {
                    super(resourcesProvider);
                }

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
    }

    /* loaded from: classes.dex */
    public class 6 implements CameraScanActivity.CameraScanActivityDelegate {
        6() {
        }

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
    }

    /* loaded from: classes.dex */
    public static class BotWebViewProxy {
        public BotWebViewContainer container;

        public BotWebViewProxy(BotWebViewContainer botWebViewContainer) {
            this.container = botWebViewContainer;
        }

        public /* synthetic */ void lambda$postEvent$0(String str, String str2) {
            BotWebViewContainer botWebViewContainer = this.container;
            if (botWebViewContainer == null) {
                return;
            }
            botWebViewContainer.onEventReceived(str, str2);
        }

        @JavascriptInterface
        public void postEvent(final String str, final String str2) {
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
        }

        public void setContainer(BotWebViewContainer botWebViewContainer) {
            this.container = botWebViewContainer;
        }
    }

    /* loaded from: classes.dex */
    public interface Delegate {

        /* loaded from: classes.dex */
        public abstract /* synthetic */ class -CC {
            public static boolean $default$isClipboardAvailable(Delegate delegate) {
                return false;
            }

            public static void $default$onInstantClose(Delegate delegate) {
                delegate.onCloseRequested(null);
            }

            public static void $default$onSendWebViewData(Delegate delegate, String str) {
            }

            public static void $default$onWebAppBackgroundChanged(Delegate delegate, boolean z, int i) {
            }

            public static void $default$onWebAppReady(Delegate delegate) {
            }

            public static void $default$onWebAppSetNavigationBarColor(Delegate delegate, int i) {
            }
        }

        boolean isClipboardAvailable();

        void onCloseRequested(Runnable runnable);

        void onCloseToTabs();

        void onInstantClose();

        void onSendWebViewData(String str);

        void onSetBackButtonVisible(boolean z);

        void onSetSettingsButtonVisible(boolean z);

        void onSetupMainButton(boolean z, boolean z2, String str, int i, int i2, boolean z3, boolean z4);

        void onSetupSecondaryButton(boolean z, boolean z2, String str, int i, int i2, boolean z3, boolean z4, String str2);

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

    /* loaded from: classes.dex */
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

        /* loaded from: classes.dex */
        public class 1 implements View.OnLongClickListener {
            1() {
            }

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

            /* JADX WARN: Can't wrap try/catch for region: R(10:1|2|3|(5:7|9|10|11|12)|17|9|10|11|12|(2:(1:21)|(0))) */
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

        /* loaded from: classes.dex */
        public class 2 extends WebViewClient {
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

            public /* synthetic */ void lambda$$3() {
                if (MyWebView.this.botWebViewContainer != null) {
                    BotWebViewContainer botWebViewContainer = MyWebView.this.botWebViewContainer;
                    MyWebView.this.errorShown = false;
                    botWebViewContainer.onErrorShown(false, 0, null);
                }
            }

            public /* synthetic */ void lambda$onRenderProcessGone$1() {
                Browser.openUrl(MyWebView.this.getContext(), "https://play.google.com/store/apps/details?id=com.google.android.webview");
            }

            public /* synthetic */ void lambda$onRenderProcessGone$2(DialogInterface dialogInterface) {
                if (MyWebView.this.botWebViewContainer == null || MyWebView.this.botWebViewContainer.delegate == null) {
                    return;
                }
                MyWebView.this.botWebViewContainer.delegate.onCloseRequested(null);
            }

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
                    botWebViewContainer.onURLChanged(myWebView3.dangerousUrl ? myWebView3.urlFallback : str, !myWebView3.canGoBack(), !MyWebView.this.canGoForward());
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
            
                if (r0 != false) goto L32;
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

        /* loaded from: classes.dex */
        public class 3 extends WebChromeClient {
            private Dialog lastPermissionsDialog;
            final /* synthetic */ boolean val$bot;

            /* loaded from: classes.dex */
            public class 1 extends WebViewClient {
                final /* synthetic */ WebView val$newWebView;

                1(WebView webView) {
                    this.val$newWebView = webView;
                }

                public /* synthetic */ void lambda$onRenderProcessGone$0() {
                    Browser.openUrl(MyWebView.this.getContext(), "https://play.google.com/store/apps/details?id=com.google.android.webview");
                }

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

            public /* synthetic */ void lambda$onGeolocationPermissionsShowPrompt$0(GeolocationPermissions.Callback callback, String str, Boolean bool) {
                callback.invoke(str, bool.booleanValue(), false);
                if (bool.booleanValue()) {
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                }
            }

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

            public /* synthetic */ void lambda$onPermissionRequest$2(PermissionRequest permissionRequest, String str, Boolean bool) {
                if (!bool.booleanValue()) {
                    permissionRequest.deny();
                } else {
                    permissionRequest.grant(new String[]{str});
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                }
            }

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

            public /* synthetic */ void lambda$onPermissionRequest$4(PermissionRequest permissionRequest, String str, Boolean bool) {
                if (!bool.booleanValue()) {
                    permissionRequest.deny();
                } else {
                    permissionRequest.grant(new String[]{str});
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                }
            }

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

            public /* synthetic */ void lambda$onPermissionRequest$6(PermissionRequest permissionRequest, String[] strArr, Boolean bool) {
                if (!bool.booleanValue()) {
                    permissionRequest.deny();
                } else {
                    permissionRequest.grant(new String[]{strArr[0], strArr[1]});
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                }
            }

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

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public class 4 implements WebView.FindListener {
            4() {
            }

            @Override // android.webkit.WebView.FindListener
            public void onFindResultReceived(int i, int i2, boolean z) {
                MyWebView.this.searchIndex = i;
                MyWebView.this.searchCount = i2;
                MyWebView.this.searchLoading = !z;
                if (MyWebView.this.searchListener != null) {
                    MyWebView.this.searchListener.run();
                }
            }
        }

        /* loaded from: classes.dex */
        public class 5 implements DownloadListener {
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
                4() {
                }

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

        public static /* synthetic */ void lambda$evaluateJS$1(String str) {
        }

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

    /* loaded from: classes.dex */
    public static final class PopupButton {
        public String id;
        public String text;
        public int textColorKey;

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

    /* loaded from: classes.dex */
    public static class WebViewProxy {
        public BotWebViewContainer container;
        public final MyWebView webView;

        public WebViewProxy(MyWebView myWebView, BotWebViewContainer botWebViewContainer) {
            this.webView = myWebView;
            this.container = botWebViewContainer;
        }

        public /* synthetic */ void lambda$post$0(String str, String str2) {
            BotWebViewContainer botWebViewContainer = this.container;
            if (botWebViewContainer == null) {
                return;
            }
            botWebViewContainer.onWebEventReceived(str, str2);
        }

        public /* synthetic */ void lambda$resolveShare$1(Boolean bool) {
            MyWebView myWebView = this.webView;
            StringBuilder sb = new StringBuilder();
            sb.append("window.navigator.__share__receive(");
            sb.append(bool.booleanValue() ? "" : "'abort'");
            sb.append(")");
            myWebView.evaluateJS(sb.toString());
        }

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

    /* loaded from: classes.dex */
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
        1 r7 = new BackupImageView(context) { // from class: org.telegram.ui.web.BotWebViewContainer.1

            /* loaded from: classes.dex */
            public class 1 extends ImageReceiver {
                1(View view) {
                    super(view);
                }

                public /* synthetic */ void lambda$setImageBitmapByKey$0(ValueAnimator valueAnimator) {
                    ((BackupImageView) 1.this).imageReceiver.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    invalidate();
                }

                @Override // org.telegram.messenger.ImageReceiver
                public boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z, int i2) {
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

            1(Context context2) {
                super(context2);
                this.imageReceiver = new 1(this);
            }

            @Override // org.telegram.ui.Components.BackupImageView, android.view.View
            public void onDraw(Canvas canvas) {
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
        this.flickerView = r7;
        r7.setColorFilter(new PorterDuffColorFilter(getColor(Theme.key_dialogSearchHint), PorterDuff.Mode.SRC_IN));
        this.flickerView.getImageReceiver().setAspectFit(true);
        addView(this.flickerView, LayoutHelper.createFrame(-1, -2, 48));
        TextView textView = new TextView(context2);
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
            this.biometry = new BotBiometry(getContext(), this.currentAccount, this.botUser.id);
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

    public /* synthetic */ void lambda$evaluateJs$5(boolean z, String str) {
        if (z) {
            checkCreateWebView();
        }
        MyWebView myWebView = this.webView;
        if (myWebView == null) {
            return;
        }
        myWebView.evaluateJS(str);
    }

    public /* synthetic */ void lambda$loadFlickerAndSettingsItem$1(TLObject tLObject) {
        boolean z;
        if (tLObject instanceof TLRPC.TL_attachMenuBotsBot) {
            TLRPC.TL_attachMenuBot tL_attachMenuBot = ((TLRPC.TL_attachMenuBotsBot) tLObject).bot;
            TLRPC.TL_attachMenuBotIcon placeholderStaticAttachMenuBotIcon = MediaDataController.getPlaceholderStaticAttachMenuBotIcon(tL_attachMenuBot);
            if (placeholderStaticAttachMenuBotIcon == null) {
                placeholderStaticAttachMenuBotIcon = MediaDataController.getStaticAttachMenuBotIcon(tL_attachMenuBot);
                z = true;
            } else {
                z = false;
            }
            if (placeholderStaticAttachMenuBotIcon != null) {
                this.flickerView.setVisibility(0);
                this.flickerView.setAlpha(1.0f);
                this.flickerView.setImage(ImageLocation.getForDocument(placeholderStaticAttachMenuBotIcon.icon), (String) null, (Drawable) null, tL_attachMenuBot);
                setupFlickerParams(z);
            }
        }
    }

    public /* synthetic */ void lambda$loadFlickerAndSettingsItem$2(final TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$loadFlickerAndSettingsItem$1(tLObject);
            }
        });
    }

    public /* synthetic */ void lambda$loadUrl$4(String str) {
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

    public static /* synthetic */ void lambda$notifyEvent$6(MyWebView myWebView, String str, JSONObject jSONObject) {
        myWebView.evaluateJS("window.Telegram.WebView.receiveEvent('" + str + "', " + jSONObject + ");");
    }

    public /* synthetic */ void lambda$onEventReceived$10(AtomicBoolean atomicBoolean, DialogInterface dialogInterface) {
        if (!atomicBoolean.get()) {
            notifyEvent("popup_closed", new JSONObject());
        }
        this.currentDialog = null;
        this.lastDialogClosed = System.currentTimeMillis();
    }

    public /* synthetic */ void lambda$onEventReceived$11(TLRPC.TL_error tL_error, String str, TLRPC.TL_inputInvoiceSlug tL_inputInvoiceSlug, TLObject tLObject) {
        if (tL_error != null) {
            onInvoiceStatusUpdate(str, "failed");
        } else {
            this.delegate.onWebAppOpenInvoice(tL_inputInvoiceSlug, str, tLObject);
        }
    }

    public /* synthetic */ void lambda$onEventReceived$12(final String str, final TLRPC.TL_inputInvoiceSlug tL_inputInvoiceSlug, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$11(tL_error, str, tL_inputInvoiceSlug, tLObject);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$13(TLObject tLObject, String[] strArr, TLRPC.TL_error tL_error, DialogInterface dialogInterface) {
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

    public /* synthetic */ void lambda$onEventReceived$14(final String[] strArr, final DialogInterface dialogInterface, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$13(tLObject, strArr, tL_error, dialogInterface);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$15(final String[] strArr, final DialogInterface dialogInterface, int i) {
        TL_bots.allowSendMessage allowsendmessage = new TL_bots.allowSendMessage();
        allowsendmessage.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botUser);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(allowsendmessage, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda38
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                BotWebViewContainer.this.lambda$onEventReceived$14(strArr, dialogInterface, tLObject, tL_error);
            }
        });
    }

    public static /* synthetic */ void lambda$onEventReceived$17(String[] strArr, int i, MyWebView myWebView) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", strArr[0]);
            notifyEvent(i, myWebView, "write_access_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$onEventReceived$18(TLObject tLObject, final int i, final MyWebView myWebView, TLRPC.TL_error tL_error) {
        if (!(tLObject instanceof TLRPC.TL_boolTrue)) {
            if (tL_error != null) {
                unknownError(tL_error.text);
                return;
            } else {
                final String[] strArr = {"cancelled"};
                showDialog(3, new AlertDialog.Builder(getContext()).setTitle(LocaleController.getString(R.string.BotWebViewRequestWriteTitle)).setMessage(LocaleController.getString(R.string.BotWebViewRequestWriteMessage)).setPositiveButton(LocaleController.getString(R.string.BotWebViewRequestAllow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda33
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        BotWebViewContainer.this.lambda$onEventReceived$15(strArr, dialogInterface, i2);
                    }
                }).setNegativeButton(LocaleController.getString(R.string.BotWebViewRequestDontAllow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda34
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        dialogInterface.dismiss();
                    }
                }).create(), new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda35
                    @Override // java.lang.Runnable
                    public final void run() {
                        BotWebViewContainer.lambda$onEventReceived$17(strArr, i, myWebView);
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

    public /* synthetic */ void lambda$onEventReceived$19(final int i, final MyWebView myWebView, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$18(tLObject, i, myWebView, tL_error);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$20(String str, TLObject tLObject, TLRPC.TL_error tL_error, int i, MyWebView myWebView) {
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

    public /* synthetic */ void lambda$onEventReceived$21(final String str, final int i, final MyWebView myWebView, final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$20(str, tLObject, tL_error, i, myWebView);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$22(int i, MyWebView myWebView) {
        SendMessagesHelper.getInstance(this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(UserConfig.getInstance(this.currentAccount).getCurrentUser(), this.botUser.id, (MessageObject) null, (MessageObject) null, (TLRPC.ReplyMarkup) null, (HashMap<String, String>) null, true, 0));
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", "sent");
            notifyEvent(i, myWebView, "phone_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$onEventReceived$23(String[] strArr, boolean z, final int i, final MyWebView myWebView, DialogInterface dialogInterface, int i2) {
        strArr[0] = null;
        dialogInterface.dismiss();
        int i3 = this.currentAccount;
        if (z) {
            MessagesController.getInstance(i3).unblockPeer(this.botUser.id, new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda29
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.this.lambda$onEventReceived$22(i, myWebView);
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

    public static /* synthetic */ void lambda$onEventReceived$25(String[] strArr, int i, MyWebView myWebView) {
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

    public /* synthetic */ void lambda$onEventReceived$26() {
        BotBiometry botBiometry = this.biometry;
        botBiometry.access_requested = true;
        botBiometry.save();
        notifyBiometryReceived();
    }

    public /* synthetic */ void lambda$onEventReceived$27(Boolean bool, String str) {
        if (bool.booleanValue()) {
            BotBiometry botBiometry = this.biometry;
            botBiometry.access_granted = true;
            botBiometry.save();
        }
        notifyBiometryReceived();
    }

    public /* synthetic */ void lambda$onEventReceived$28(Runnable[] runnableArr, DialogInterface dialogInterface, int i) {
        if (runnableArr[0] != null) {
            runnableArr[0] = null;
        }
        BotBiometry botBiometry = this.biometry;
        botBiometry.access_requested = true;
        botBiometry.save();
        this.biometry.requestToken(null, new Utilities.Callback2() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda27
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                BotWebViewContainer.this.lambda$onEventReceived$27((Boolean) obj, (String) obj2);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$29(Runnable[] runnableArr, DialogInterface dialogInterface, int i) {
        if (runnableArr[0] != null) {
            runnableArr[0] = null;
        }
        BotBiometry botBiometry = this.biometry;
        botBiometry.access_requested = true;
        botBiometry.disabled = true;
        botBiometry.save();
        notifyBiometryReceived();
    }

    public static /* synthetic */ void lambda$onEventReceived$30(Runnable[] runnableArr, DialogInterface dialogInterface) {
        Runnable runnable = runnableArr[0];
        if (runnable != null) {
            runnable.run();
            runnableArr[0] = null;
        }
    }

    public /* synthetic */ void lambda$onEventReceived$31(Boolean bool, String str) {
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

    public /* synthetic */ void lambda$onEventReceived$32(String str, Boolean bool) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", bool.booleanValue() ? TextUtils.isEmpty(str) ? "removed" : "updated" : "failed");
            notifyEvent("biometry_token_updated", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$onEventReceived$33(int[] iArr, File file, AlertDialog alertDialog, String str, String str2, String str3) {
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

    public static /* synthetic */ void lambda$onEventReceived$34(File file, int[] iArr, Runnable runnable) {
        AnimatedFileDrawable.getVideoInfo(file.getAbsolutePath(), iArr);
        AndroidUtilities.runOnUIThread(runnable);
    }

    public /* synthetic */ void lambda$onEventReceived$35(final File file, final AlertDialog alertDialog, final String str, final String str2, final String str3) {
        if (file == null) {
            alertDialog.dismissUnless(500L);
            return;
        }
        final int[] iArr = new int[11];
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$33(iArr, file, alertDialog, str, str2, str3);
            }
        };
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.lambda$onEventReceived$34(file, iArr, runnable);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$36(final AlertDialog alertDialog, final String str, final String str2, final String str3, final File file) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$35(file, alertDialog, str, str2, str3);
            }
        });
    }

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

    public /* synthetic */ void lambda$onEventReceived$8(PopupButton popupButton, AtomicBoolean atomicBoolean, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        try {
            this.lastClickMs = System.currentTimeMillis();
            notifyEvent("popup_closed", new JSONObject().put("button_id", popupButton.id));
            atomicBoolean.set(true);
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$onEventReceived$9(PopupButton popupButton, AtomicBoolean atomicBoolean, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        try {
            this.lastClickMs = System.currentTimeMillis();
            notifyEvent("popup_closed", new JSONObject().put("button_id", popupButton.id));
            atomicBoolean.set(true);
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$reload$3() {
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
    }

    public /* synthetic */ void lambda$runWithPermissions$0(Consumer consumer, String[] strArr) {
        consumer.accept(Boolean.valueOf(checkPermissions(strArr)));
    }

    public /* synthetic */ void lambda$showDialog$37(Runnable runnable, DialogInterface dialogInterface) {
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
        NotificationCenter.getInstance(i).doOnIdle(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.lambda$notifyEvent$6(BotWebViewContainer.MyWebView.this, str, jSONObject);
            }
        });
    }

    public void notifyEvent(String str, JSONObject jSONObject) {
        d("notifyEvent " + str);
        evaluateJs("window.Telegram.WebView.receiveEvent('" + str + "', " + jSONObject + ");", false);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:114:0x046b  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x052a A[Catch: Exception -> 0x02a7, TRY_LEAVE, TryCatch #11 {Exception -> 0x02a7, blocks: (B:30:0x028e, B:32:0x02ad, B:34:0x02bc, B:36:0x02c2, B:39:0x02cb, B:50:0x0305, B:53:0x02fc, B:55:0x0300, B:56:0x02e0, B:59:0x02ea, B:62:0x0313, B:64:0x0322, B:65:0x032f, B:67:0x0333, B:70:0x032b, B:78:0x03ad, B:80:0x03c7, B:83:0x03d2, B:85:0x03d8, B:86:0x03e3, B:88:0x03e9, B:90:0x03f6, B:94:0x0405, B:98:0x0412, B:102:0x03f3, B:103:0x03e1, B:105:0x042b, B:120:0x052a, B:123:0x0473, B:125:0x0478, B:139:0x04bb, B:140:0x04be, B:141:0x04c1, B:142:0x0492, B:145:0x049c, B:148:0x04a6, B:151:0x04c4, B:152:0x04ce, B:165:0x0514, B:166:0x0518, B:167:0x051c, B:168:0x0520, B:169:0x0524, B:170:0x04d2, B:173:0x04dc, B:176:0x04e6, B:179:0x04f0, B:182:0x04fa, B:185:0x044a, B:188:0x0454, B:191:0x045e, B:333:0x09e4, B:335:0x09fe, B:338:0x0a09, B:340:0x0a0f, B:341:0x0a1a, B:343:0x0a20, B:345:0x0a2d, B:349:0x0a38, B:353:0x0a49, B:355:0x0a4f, B:358:0x0a5a, B:360:0x0a54, B:363:0x0a2a, B:364:0x0a18, B:430:0x0bff, B:432:0x0c1c, B:434:0x0c2e), top: B:13:0x022d }] */
    /* JADX WARN: Removed duplicated region for block: B:122:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:134:0x04b3  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x04c1 A[Catch: Exception -> 0x02a7, TryCatch #11 {Exception -> 0x02a7, blocks: (B:30:0x028e, B:32:0x02ad, B:34:0x02bc, B:36:0x02c2, B:39:0x02cb, B:50:0x0305, B:53:0x02fc, B:55:0x0300, B:56:0x02e0, B:59:0x02ea, B:62:0x0313, B:64:0x0322, B:65:0x032f, B:67:0x0333, B:70:0x032b, B:78:0x03ad, B:80:0x03c7, B:83:0x03d2, B:85:0x03d8, B:86:0x03e3, B:88:0x03e9, B:90:0x03f6, B:94:0x0405, B:98:0x0412, B:102:0x03f3, B:103:0x03e1, B:105:0x042b, B:120:0x052a, B:123:0x0473, B:125:0x0478, B:139:0x04bb, B:140:0x04be, B:141:0x04c1, B:142:0x0492, B:145:0x049c, B:148:0x04a6, B:151:0x04c4, B:152:0x04ce, B:165:0x0514, B:166:0x0518, B:167:0x051c, B:168:0x0520, B:169:0x0524, B:170:0x04d2, B:173:0x04dc, B:176:0x04e6, B:179:0x04f0, B:182:0x04fa, B:185:0x044a, B:188:0x0454, B:191:0x045e, B:333:0x09e4, B:335:0x09fe, B:338:0x0a09, B:340:0x0a0f, B:341:0x0a1a, B:343:0x0a20, B:345:0x0a2d, B:349:0x0a38, B:353:0x0a49, B:355:0x0a4f, B:358:0x0a5a, B:360:0x0a54, B:363:0x0a2a, B:364:0x0a18, B:430:0x0bff, B:432:0x0c1c, B:434:0x0c2e), top: B:13:0x022d }] */
    /* JADX WARN: Removed duplicated region for block: B:151:0x04c4 A[Catch: Exception -> 0x02a7, TryCatch #11 {Exception -> 0x02a7, blocks: (B:30:0x028e, B:32:0x02ad, B:34:0x02bc, B:36:0x02c2, B:39:0x02cb, B:50:0x0305, B:53:0x02fc, B:55:0x0300, B:56:0x02e0, B:59:0x02ea, B:62:0x0313, B:64:0x0322, B:65:0x032f, B:67:0x0333, B:70:0x032b, B:78:0x03ad, B:80:0x03c7, B:83:0x03d2, B:85:0x03d8, B:86:0x03e3, B:88:0x03e9, B:90:0x03f6, B:94:0x0405, B:98:0x0412, B:102:0x03f3, B:103:0x03e1, B:105:0x042b, B:120:0x052a, B:123:0x0473, B:125:0x0478, B:139:0x04bb, B:140:0x04be, B:141:0x04c1, B:142:0x0492, B:145:0x049c, B:148:0x04a6, B:151:0x04c4, B:152:0x04ce, B:165:0x0514, B:166:0x0518, B:167:0x051c, B:168:0x0520, B:169:0x0524, B:170:0x04d2, B:173:0x04dc, B:176:0x04e6, B:179:0x04f0, B:182:0x04fa, B:185:0x044a, B:188:0x0454, B:191:0x045e, B:333:0x09e4, B:335:0x09fe, B:338:0x0a09, B:340:0x0a0f, B:341:0x0a1a, B:343:0x0a20, B:345:0x0a2d, B:349:0x0a38, B:353:0x0a49, B:355:0x0a4f, B:358:0x0a5a, B:360:0x0a54, B:363:0x0a2a, B:364:0x0a18, B:430:0x0bff, B:432:0x0c1c, B:434:0x0c2e), top: B:13:0x022d }] */
    /* JADX WARN: Removed duplicated region for block: B:245:0x077f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:246:0x0780  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x02f7  */
    /* JADX WARN: Removed duplicated region for block: B:487:0x0839  */
    /* JADX WARN: Removed duplicated region for block: B:489:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0305 A[Catch: Exception -> 0x02a7, TryCatch #11 {Exception -> 0x02a7, blocks: (B:30:0x028e, B:32:0x02ad, B:34:0x02bc, B:36:0x02c2, B:39:0x02cb, B:50:0x0305, B:53:0x02fc, B:55:0x0300, B:56:0x02e0, B:59:0x02ea, B:62:0x0313, B:64:0x0322, B:65:0x032f, B:67:0x0333, B:70:0x032b, B:78:0x03ad, B:80:0x03c7, B:83:0x03d2, B:85:0x03d8, B:86:0x03e3, B:88:0x03e9, B:90:0x03f6, B:94:0x0405, B:98:0x0412, B:102:0x03f3, B:103:0x03e1, B:105:0x042b, B:120:0x052a, B:123:0x0473, B:125:0x0478, B:139:0x04bb, B:140:0x04be, B:141:0x04c1, B:142:0x0492, B:145:0x049c, B:148:0x04a6, B:151:0x04c4, B:152:0x04ce, B:165:0x0514, B:166:0x0518, B:167:0x051c, B:168:0x0520, B:169:0x0524, B:170:0x04d2, B:173:0x04dc, B:176:0x04e6, B:179:0x04f0, B:182:0x04fa, B:185:0x044a, B:188:0x0454, B:191:0x045e, B:333:0x09e4, B:335:0x09fe, B:338:0x0a09, B:340:0x0a0f, B:341:0x0a1a, B:343:0x0a20, B:345:0x0a2d, B:349:0x0a38, B:353:0x0a49, B:355:0x0a4f, B:358:0x0a5a, B:360:0x0a54, B:363:0x0a2a, B:364:0x0a18, B:430:0x0bff, B:432:0x0c1c, B:434:0x0c2e), top: B:13:0x022d }] */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0300 A[Catch: Exception -> 0x02a7, TryCatch #11 {Exception -> 0x02a7, blocks: (B:30:0x028e, B:32:0x02ad, B:34:0x02bc, B:36:0x02c2, B:39:0x02cb, B:50:0x0305, B:53:0x02fc, B:55:0x0300, B:56:0x02e0, B:59:0x02ea, B:62:0x0313, B:64:0x0322, B:65:0x032f, B:67:0x0333, B:70:0x032b, B:78:0x03ad, B:80:0x03c7, B:83:0x03d2, B:85:0x03d8, B:86:0x03e3, B:88:0x03e9, B:90:0x03f6, B:94:0x0405, B:98:0x0412, B:102:0x03f3, B:103:0x03e1, B:105:0x042b, B:120:0x052a, B:123:0x0473, B:125:0x0478, B:139:0x04bb, B:140:0x04be, B:141:0x04c1, B:142:0x0492, B:145:0x049c, B:148:0x04a6, B:151:0x04c4, B:152:0x04ce, B:165:0x0514, B:166:0x0518, B:167:0x051c, B:168:0x0520, B:169:0x0524, B:170:0x04d2, B:173:0x04dc, B:176:0x04e6, B:179:0x04f0, B:182:0x04fa, B:185:0x044a, B:188:0x0454, B:191:0x045e, B:333:0x09e4, B:335:0x09fe, B:338:0x0a09, B:340:0x0a0f, B:341:0x0a1a, B:343:0x0a20, B:345:0x0a2d, B:349:0x0a38, B:353:0x0a49, B:355:0x0a4f, B:358:0x0a5a, B:360:0x0a54, B:363:0x0a2a, B:364:0x0a18, B:430:0x0bff, B:432:0x0c1c, B:434:0x0c2e), top: B:13:0x022d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onEventReceived(String str, String str2) {
        char c;
        boolean z;
        LaunchActivity launchActivity;
        boolean z2;
        boolean z3;
        BottomSheet bottomSheet;
        TextView textView;
        TextView textView2;
        TextView textView3;
        String str3;
        boolean z4;
        int checkSelfPermission;
        boolean z5;
        boolean z6;
        String str4;
        String str5;
        String str6;
        String str7;
        final String str8;
        final String str9;
        final String str10;
        JSONObject put;
        char c2;
        char c3;
        BotWebViewVibrationEffect botWebViewVibrationEffect;
        char c4;
        String str11;
        boolean z7;
        char c5;
        int i;
        int i2;
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
                case -1693280352:
                    if (str.equals("web_app_open_popup")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -1390641887:
                    if (str.equals("web_app_open_invoice")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case -1353432696:
                    if (str.equals("web_app_setup_secondary_button")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case -1341039673:
                    if (str.equals("web_app_setup_closing_behavior")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case -1309122684:
                    if (str.equals("web_app_open_scan_qr_popup")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case -1263619595:
                    if (str.equals("web_app_request_phone")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case -1259935152:
                    if (str.equals("web_app_request_theme")) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case -1093591555:
                    if (str.equals("web_app_biometry_open_settings")) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case -921083201:
                    if (str.equals("web_app_request_viewport")) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                case -512688845:
                    if (str.equals("web_app_biometry_request_auth")) {
                        c = '\r';
                        break;
                    }
                    c = 65535;
                    break;
                case -474676372:
                    if (str.equals("web_app_allow_scroll")) {
                        c = 14;
                        break;
                    }
                    c = 65535;
                    break;
                case -439770054:
                    if (str.equals("web_app_open_tg_link")) {
                        c = 15;
                        break;
                    }
                    c = 65535;
                    break;
                case -244584646:
                    if (str.equals("web_app_share_to_story")) {
                        c = 16;
                        break;
                    }
                    c = 65535;
                    break;
                case -71726289:
                    if (str.equals("web_app_close")) {
                        c = 17;
                        break;
                    }
                    c = 65535;
                    break;
                case -58095910:
                    if (str.equals("web_app_ready")) {
                        c = 18;
                        break;
                    }
                    c = 65535;
                    break;
                case 22015443:
                    if (str.equals("web_app_read_text_from_clipboard")) {
                        c = 19;
                        break;
                    }
                    c = 65535;
                    break;
                case 668142772:
                    if (str.equals("web_app_data_send")) {
                        c = 20;
                        break;
                    }
                    c = 65535;
                    break;
                case 751292356:
                    if (str.equals("web_app_switch_inline_query")) {
                        c = 21;
                        break;
                    }
                    c = 65535;
                    break;
                case 1011447167:
                    if (str.equals("web_app_setup_back_button")) {
                        c = 22;
                        break;
                    }
                    c = 65535;
                    break;
                case 1210129967:
                    if (str.equals("web_app_biometry_request_access")) {
                        c = 23;
                        break;
                    }
                    c = 65535;
                    break;
                case 1273834781:
                    if (str.equals("web_app_trigger_haptic_feedback")) {
                        c = 24;
                        break;
                    }
                    c = 65535;
                    break;
                case 1398490221:
                    if (str.equals("web_app_setup_main_button")) {
                        c = 25;
                        break;
                    }
                    c = 65535;
                    break;
                case 1453051298:
                    if (str.equals("web_app_setup_swipe_behavior")) {
                        c = 26;
                        break;
                    }
                    c = 65535;
                    break;
                case 1455972419:
                    if (str.equals("web_app_setup_settings_button")) {
                        c = 27;
                        break;
                    }
                    c = 65535;
                    break;
                case 1882780382:
                    if (str.equals("web_app_biometry_update_token")) {
                        c = 28;
                        break;
                    }
                    c = 65535;
                    break;
                case 1899078473:
                    if (str.equals("web_app_set_bottom_bar_color")) {
                        c = 29;
                        break;
                    }
                    c = 65535;
                    break;
                case 1917103703:
                    if (str.equals("web_app_set_header_color")) {
                        c = 30;
                        break;
                    }
                    c = 65535;
                    break;
                case 2001330488:
                    if (str.equals("web_app_set_background_color")) {
                        c = 31;
                        break;
                    }
                    c = 65535;
                    break;
                case 2036090717:
                    if (str.equals("web_app_request_write_access")) {
                        c = ' ';
                        break;
                    }
                    c = 65535;
                    break;
                case 2139805763:
                    if (str.equals("web_app_expand")) {
                        c = '!';
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            BottomSheetTabs.WebTabData webTabData = null;
            String str12 = null;
            String str13 = null;
            String str14 = null;
            r9 = null;
            r9 = null;
            BotWebViewVibrationEffect botWebViewVibrationEffect2 = null;
            try {
                try {
                    switch (c) {
                        case 0:
                            if (this.botUser == null) {
                                return;
                            }
                            try {
                                JSONObject jSONObject = new JSONObject(str2);
                                final String string = jSONObject.getString("req_id");
                                String string2 = jSONObject.getString("method");
                                String obj = jSONObject.get("params").toString();
                                final int i3 = this.currentAccount;
                                final MyWebView myWebView = this.webView;
                                TL_bots.invokeWebViewCustomMethod invokewebviewcustommethod = new TL_bots.invokeWebViewCustomMethod();
                                invokewebviewcustommethod.bot = MessagesController.getInstance(i3).getInputUser(this.botUser.id);
                                invokewebviewcustommethod.custom_method = string2;
                                TLRPC.TL_dataJSON tL_dataJSON = new TLRPC.TL_dataJSON();
                                invokewebviewcustommethod.params = tL_dataJSON;
                                tL_dataJSON.data = obj;
                                ConnectionsManager.getInstance(i3).sendRequest(invokewebviewcustommethod, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda14
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                        BotWebViewContainer.this.lambda$onEventReceived$21(string, i3, myWebView, tLObject, tL_error);
                                    }
                                });
                                return;
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
                        case 1:
                            if (!this.hasQRPending || (bottomSheet = this.cameraBottomSheet) == null) {
                                return;
                            }
                            bottomSheet.dismiss();
                            return;
                        case 2:
                            break;
                        case 3:
                            JSONObject jSONObject2 = new JSONObject(str2);
                            Uri parse = Uri.parse(jSONObject2.optString("url"));
                            String optString = jSONObject2.optString("try_browser");
                            if (MessagesController.getInstance(this.currentAccount).webAppAllowedProtocols == null || !MessagesController.getInstance(this.currentAccount).webAppAllowedProtocols.contains(parse.getScheme())) {
                                return;
                            }
                            onOpenUri(parse, optString, jSONObject2.optBoolean("try_instant_view"), true, false);
                            return;
                        case 4:
                            if (this.currentDialog != null) {
                                return;
                            }
                            if (System.currentTimeMillis() - this.lastDialogClosed <= 150) {
                                int i4 = this.dialogSequentialOpenTimes + 1;
                                this.dialogSequentialOpenTimes = i4;
                                if (i4 >= 3) {
                                    this.dialogSequentialOpenTimes = 0;
                                    this.lastDialogCooldownTime = System.currentTimeMillis();
                                    return;
                                }
                            }
                            if (System.currentTimeMillis() - this.lastDialogCooldownTime <= 3000) {
                                return;
                            }
                            JSONObject jSONObject3 = new JSONObject(str2);
                            String optString2 = jSONObject3.optString("title", null);
                            String string3 = jSONObject3.getString("message");
                            JSONArray jSONArray = jSONObject3.getJSONArray("buttons");
                            AlertDialog.Builder message = new AlertDialog.Builder(getContext()).setTitle(optString2).setMessage(string3);
                            ArrayList arrayList = new ArrayList();
                            for (int i5 = 0; i5 < jSONArray.length(); i5++) {
                                arrayList.add(new PopupButton(jSONArray.getJSONObject(i5)));
                            }
                            if (arrayList.size() > 3) {
                                return;
                            }
                            final AtomicBoolean atomicBoolean = new AtomicBoolean();
                            if (arrayList.size() >= 1) {
                                final PopupButton popupButton = (PopupButton) arrayList.get(0);
                                message.setPositiveButton(popupButton.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda10
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i6) {
                                        BotWebViewContainer.this.lambda$onEventReceived$7(popupButton, atomicBoolean, dialogInterface, i6);
                                    }
                                });
                            }
                            if (arrayList.size() >= 2) {
                                final PopupButton popupButton2 = (PopupButton) arrayList.get(1);
                                message.setNegativeButton(popupButton2.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda11
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i6) {
                                        BotWebViewContainer.this.lambda$onEventReceived$8(popupButton2, atomicBoolean, dialogInterface, i6);
                                    }
                                });
                            }
                            if (arrayList.size() == 3) {
                                final PopupButton popupButton3 = (PopupButton) arrayList.get(2);
                                message.setNeutralButton(popupButton3.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda12
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i6) {
                                        BotWebViewContainer.this.lambda$onEventReceived$9(popupButton3, atomicBoolean, dialogInterface, i6);
                                    }
                                });
                            }
                            message.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda13
                                @Override // android.content.DialogInterface.OnDismissListener
                                public final void onDismiss(DialogInterface dialogInterface) {
                                    BotWebViewContainer.this.lambda$onEventReceived$10(atomicBoolean, dialogInterface);
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
                                if (popupButton6.textColorKey < 0 || (textView = (TextView) this.currentDialog.getButton(-3)) == null) {
                                    return;
                                }
                                textView.setTextColor(getColor(popupButton6.textColorKey));
                                return;
                            }
                            return;
                        case 5:
                            final String optString3 = new JSONObject(str2).optString("slug");
                            if (this.currentPaymentSlug != null) {
                                onInvoiceStatusUpdate(optString3, "cancelled", true);
                                return;
                            }
                            this.currentPaymentSlug = optString3;
                            TLRPC.TL_payments_getPaymentForm tL_payments_getPaymentForm = new TLRPC.TL_payments_getPaymentForm();
                            final TLRPC.TL_inputInvoiceSlug tL_inputInvoiceSlug = new TLRPC.TL_inputInvoiceSlug();
                            tL_inputInvoiceSlug.slug = optString3;
                            tL_payments_getPaymentForm.invoice = tL_inputInvoiceSlug;
                            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda9
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                    BotWebViewContainer.this.lambda$onEventReceived$12(optString3, tL_inputInvoiceSlug, tLObject, tL_error);
                                }
                            });
                            return;
                        case 6:
                            JSONObject jSONObject4 = new JSONObject(str2);
                            boolean optBoolean = jSONObject4.optBoolean("is_active", false);
                            String trim = jSONObject4.optString("text", this.lastSecondaryButtonText).trim();
                            boolean z8 = jSONObject4.optBoolean("is_visible", false) && !TextUtils.isEmpty(trim);
                            int parseColor = jSONObject4.has("color") ? Color.parseColor(jSONObject4.optString("color")) : this.lastSecondaryButtonColor;
                            int parseColor2 = jSONObject4.has("text_color") ? Color.parseColor(jSONObject4.optString("text_color")) : this.lastSecondaryButtonTextColor;
                            boolean z9 = jSONObject4.optBoolean("is_progress_visible", false) && z8;
                            if (jSONObject4.optBoolean("has_shine_effect", false) && z8) {
                                str3 = "position";
                                z4 = true;
                            } else {
                                str3 = "position";
                                z4 = false;
                            }
                            String optString4 = jSONObject4.has(str3) ? jSONObject4.optString(str3) : this.lastSecondaryButtonPosition;
                            if (optString4 == null) {
                                optString4 = "left";
                            }
                            this.lastSecondaryButtonColor = parseColor;
                            this.lastSecondaryButtonTextColor = parseColor2;
                            this.lastSecondaryButtonText = trim;
                            this.lastSecondaryButtonPosition = optString4;
                            this.secondaryButtonData = str2;
                            this.delegate.onSetupSecondaryButton(z8, optBoolean, trim, parseColor, parseColor2, z9, z4, optString4);
                            return;
                        case 7:
                            this.delegate.onWebAppSetupClosingBehavior(new JSONObject(str2).optBoolean("need_confirmation"));
                            return;
                        case '\b':
                            if (!this.hasQRPending && this.parentActivity != null) {
                                this.lastQrText = new JSONObject(str2).optString("text");
                                this.hasQRPending = true;
                                if (Build.VERSION.SDK_INT >= 23) {
                                    checkSelfPermission = this.parentActivity.checkSelfPermission("android.permission.CAMERA");
                                    if (checkSelfPermission != 0) {
                                        NotificationCenter.getGlobalInstance().addObserver(new NotificationCenter.NotificationCenterDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer.4
                                            4() {
                                            }

                                            @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
                                            public void didReceivedNotification(int i6, int i22, Object... objArr) {
                                                int i32 = NotificationCenter.onRequestPermissionResultReceived;
                                                if (i6 == i32) {
                                                    int intValue = ((Integer) objArr[0]).intValue();
                                                    int[] iArr = (int[]) objArr[2];
                                                    if (intValue == 5000) {
                                                        NotificationCenter.getGlobalInstance().removeObserver(this, i32);
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
                                        return;
                                    }
                                }
                                openQrScanActivity();
                                return;
                            }
                            return;
                        case '\t':
                            if (ignoreDialog(4)) {
                                try {
                                    JSONObject jSONObject5 = new JSONObject();
                                    jSONObject5.put("status", "cancelled");
                                    notifyEvent("phone_requested", jSONObject5);
                                    return;
                                } catch (Exception e2) {
                                    FileLog.e(e2);
                                    return;
                                }
                            }
                            final int i6 = this.currentAccount;
                            final MyWebView myWebView2 = this.webView;
                            final String[] strArr = {"cancelled"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
                            builder.setTitle(LocaleController.getString(R.string.ShareYouPhoneNumberTitle));
                            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                            String userName = UserObject.getUserName(this.botUser);
                            spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(!TextUtils.isEmpty(userName) ? LocaleController.formatString(R.string.AreYouSureShareMyContactInfoWebapp, userName) : LocaleController.getString(R.string.AreYouSureShareMyContactInfoBot)));
                            final boolean z10 = MessagesController.getInstance(this.currentAccount).blockePeers.indexOfKey(this.botUser.id) >= 0;
                            if (z10) {
                                spannableStringBuilder.append((CharSequence) "\n\n");
                                spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.AreYouSureShareMyContactInfoBotUnblock));
                            }
                            builder.setMessage(spannableStringBuilder);
                            builder.setPositiveButton(LocaleController.getString(R.string.ShareContact), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda22
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i7) {
                                    BotWebViewContainer.this.lambda$onEventReceived$23(strArr, z10, i6, myWebView2, dialogInterface, i7);
                                }
                            });
                            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda23
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i7) {
                                    dialogInterface.dismiss();
                                }
                            });
                            showDialog(4, builder.create(), new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda8
                                @Override // java.lang.Runnable
                                public final void run() {
                                    BotWebViewContainer.lambda$onEventReceived$25(strArr, i6, myWebView2);
                                }
                            });
                            return;
                        case '\n':
                            notifyThemeChanged();
                            return;
                        case 11:
                            if (this.isRequestingPageOpen || System.currentTimeMillis() - this.lastClickMs > 10000) {
                                return;
                            }
                            this.lastClickMs = 0L;
                            BaseFragment lastFragment = LaunchActivity.getLastFragment();
                            if (lastFragment == null) {
                                return;
                            }
                            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
                            bottomSheetParams.transitionFromLeft = true;
                            bottomSheetParams.allowNestedScroll = false;
                            lastFragment.showAsSheet(new BotBiometrySettings(), bottomSheetParams);
                            return;
                        case '\f':
                            if ((getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) && ((ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent()).isSwipeInProgress()) {
                                z5 = true;
                                z6 = true;
                            } else {
                                z5 = true;
                                z6 = false;
                            }
                            invalidateViewPortHeight(!z6, z5);
                            return;
                        case '\r':
                            try {
                                str14 = new JSONObject(str2).getString("reason");
                            } catch (Exception unused) {
                            }
                            createBiometry();
                            BotBiometry botBiometry = this.biometry;
                            if (botBiometry == null) {
                                return;
                            }
                            if (botBiometry.access_granted) {
                                botBiometry.requestToken(str14, new Utilities.Callback2() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda21
                                    @Override // org.telegram.messenger.Utilities.Callback2
                                    public final void run(Object obj2, Object obj3) {
                                        BotWebViewContainer.this.lambda$onEventReceived$31((Boolean) obj2, (String) obj3);
                                    }
                                });
                                return;
                            }
                            try {
                                JSONObject jSONObject6 = new JSONObject();
                                jSONObject6.put("status", "failed");
                                notifyEvent("biometry_auth_requested", jSONObject6);
                                return;
                            } catch (Exception e3) {
                                FileLog.e(e3);
                                return;
                            }
                        case 14:
                            try {
                                JSONArray jSONArray2 = new JSONArray(str2);
                                z2 = jSONArray2.optBoolean(0, true);
                                try {
                                    z3 = jSONArray2.optBoolean(1, true);
                                } catch (Exception unused2) {
                                    z3 = true;
                                    d("allowScroll " + z2 + " " + z3);
                                    if (getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) {
                                    }
                                }
                            } catch (Exception unused3) {
                                z2 = true;
                            }
                            d("allowScroll " + z2 + " " + z3);
                            if (getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) {
                                return;
                            }
                            ((ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent()).allowThisScroll(z2, z3);
                            return;
                        case 15:
                            JSONObject jSONObject7 = new JSONObject(str2);
                            String optString5 = jSONObject7.optString("path_full");
                            boolean optBoolean2 = jSONObject7.optBoolean("force_request", false);
                            if (optString5.startsWith("/")) {
                                optString5 = optString5.substring(1);
                            }
                            onOpenUri(Uri.parse("https://t.me/" + optString5), null, false, true, optBoolean2);
                            return;
                        case 16:
                            if (this.isRequestingPageOpen || System.currentTimeMillis() - this.lastClickMs > 10000 || System.currentTimeMillis() - this.lastPostStoryMs < 2000) {
                                return;
                            }
                            this.lastClickMs = 0L;
                            this.lastPostStoryMs = System.currentTimeMillis();
                            try {
                                JSONObject jSONObject8 = new JSONObject(str2);
                                str4 = jSONObject8.optString("media_url");
                                try {
                                    str5 = jSONObject8.optString("text");
                                    try {
                                        JSONObject optJSONObject = jSONObject8.optJSONObject("widget_link");
                                        if (optJSONObject != null) {
                                            str6 = optJSONObject.optString("url");
                                            try {
                                                str10 = optJSONObject.optString("name");
                                                str7 = str4;
                                                str8 = str5;
                                                str9 = str6;
                                            } catch (Exception e4) {
                                                e = e4;
                                                FileLog.e(e);
                                                str7 = str4;
                                                str8 = str5;
                                                str9 = str6;
                                                str10 = null;
                                                if (str7 == null) {
                                                }
                                            }
                                        } else {
                                            str7 = str4;
                                            str8 = str5;
                                            str9 = null;
                                            str10 = null;
                                        }
                                    } catch (Exception e5) {
                                        e = e5;
                                        str6 = null;
                                    }
                                } catch (Exception e6) {
                                    e = e6;
                                    str5 = null;
                                    str6 = str5;
                                    FileLog.e(e);
                                    str7 = str4;
                                    str8 = str5;
                                    str9 = str6;
                                    str10 = null;
                                    if (str7 == null) {
                                    }
                                }
                            } catch (Exception e7) {
                                e = e7;
                                str4 = null;
                                str5 = null;
                            }
                            if (str7 == null) {
                                return;
                            }
                            if (!MessagesController.getInstance(this.currentAccount).storiesEnabled()) {
                                new PremiumFeatureBottomSheet(new BaseFragment() { // from class: org.telegram.ui.web.BotWebViewContainer.5

                                    /* loaded from: classes.dex */
                                    class 1 extends WrappedResourceProvider {
                                        1(Theme.ResourcesProvider resourcesProvider) {
                                            super(resourcesProvider);
                                        }

                                        @Override // org.telegram.ui.WrappedResourceProvider
                                        public void appendColors() {
                                            this.sparseIntArray.append(Theme.key_dialogBackground, -14803426);
                                            this.sparseIntArray.append(Theme.key_windowBackgroundGray, -16777216);
                                        }
                                    }

                                    5() {
                                        this.currentAccount = BotWebViewContainer.this.currentAccount;
                                    }

                                    @Override // org.telegram.ui.ActionBar.BaseFragment
                                    public Activity getParentActivity() {
                                        return BotWebViewContainer.this.parentActivity;
                                    }

                                    @Override // org.telegram.ui.ActionBar.BaseFragment
                                    public Theme.ResourcesProvider getResourceProvider() {
                                        return new WrappedResourceProvider(BotWebViewContainer.this.resourcesProvider) { // from class: org.telegram.ui.web.BotWebViewContainer.5.1
                                            1(Theme.ResourcesProvider resourcesProvider) {
                                                super(resourcesProvider);
                                            }

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
                                return;
                            }
                            final AlertDialog alertDialog = new AlertDialog(this.parentActivity, 3);
                            new HttpGetFileTask(new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda20
                                @Override // org.telegram.messenger.Utilities.Callback
                                public final void run(Object obj2) {
                                    BotWebViewContainer.this.lambda$onEventReceived$36(alertDialog, str8, str9, str10, (File) obj2);
                                }
                            }).execute(str7);
                            alertDialog.showDelayed(250L);
                            return;
                        case 17:
                            try {
                                z = new JSONObject(str2).optBoolean("return_back");
                            } catch (Exception e8) {
                                FileLog.e(e8);
                                z = false;
                            }
                            this.delegate.onCloseRequested(null);
                            if (z) {
                                if (this.wasOpenedByLinkIntent && LaunchActivity.instance != null) {
                                    Activity findActivity = AndroidUtilities.findActivity(getContext());
                                    if (findActivity == null) {
                                        findActivity = LaunchActivity.instance;
                                    }
                                    if (findActivity == null || findActivity.isFinishing()) {
                                        return;
                                    }
                                    findActivity.moveTaskToBack(true);
                                    return;
                                }
                                if (this.wasOpenedByBot == null || (launchActivity = LaunchActivity.instance) == null || launchActivity.getBottomSheetTabs() == null) {
                                    return;
                                }
                                BottomSheetTabs bottomSheetTabs = LaunchActivity.instance.getBottomSheetTabs();
                                ArrayList<BottomSheetTabs.WebTabData> tabs = bottomSheetTabs.getTabs();
                                int i7 = 0;
                                while (true) {
                                    if (i7 < tabs.size()) {
                                        BottomSheetTabs.WebTabData webTabData2 = tabs.get(i7);
                                        if (!this.wasOpenedByBot.equals(webTabData2.props) || webTabData2.webView == this.webView) {
                                            i7++;
                                        } else {
                                            webTabData = webTabData2;
                                        }
                                    }
                                }
                                if (webTabData != null) {
                                    bottomSheetTabs.openTab(webTabData);
                                    return;
                                }
                                return;
                            }
                            return;
                        case 18:
                            setPageLoaded(this.webView.getUrl(), true);
                            return;
                        case 19:
                            String string4 = new JSONObject(str2).getString("req_id");
                            if (this.delegate.isClipboardAvailable() && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                                CharSequence text = ((ClipboardManager) getContext().getSystemService("clipboard")).getText();
                                put = new JSONObject().put("req_id", string4).put("data", text != null ? text.toString() : "");
                                notifyEvent("clipboard_text_received", put);
                                return;
                            }
                            put = new JSONObject().put("req_id", string4);
                            notifyEvent("clipboard_text_received", put);
                            return;
                        case 20:
                            this.delegate.onSendWebViewData(new JSONObject(str2).optString("data"));
                            return;
                        case 21:
                            JSONObject jSONObject9 = new JSONObject(str2);
                            ArrayList arrayList2 = new ArrayList();
                            JSONArray jSONArray3 = jSONObject9.getJSONArray("chat_types");
                            for (int i8 = 0; i8 < jSONArray3.length(); i8++) {
                                arrayList2.add(jSONArray3.getString(i8));
                            }
                            this.delegate.onWebAppSwitchInlineQuery(this.botUser, jSONObject9.getString("query"), arrayList2);
                            return;
                        case 22:
                            boolean optBoolean3 = new JSONObject(str2).optBoolean("is_visible");
                            if (optBoolean3 != this.isBackButtonVisible) {
                                this.isBackButtonVisible = optBoolean3;
                                this.delegate.onSetBackButtonVisible(optBoolean3);
                                return;
                            }
                            return;
                        case 23:
                            try {
                                str12 = new JSONObject(str2).getString("reason");
                            } catch (Exception unused4) {
                            }
                            createBiometry();
                            BotBiometry botBiometry2 = this.biometry;
                            if (botBiometry2 == null) {
                                return;
                            }
                            boolean z11 = botBiometry2.access_requested;
                            if (z11) {
                                notifyBiometryReceived();
                                return;
                            }
                            if (!botBiometry2.access_granted) {
                                final Runnable[] runnableArr = {new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda16
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        BotWebViewContainer.this.lambda$onEventReceived$26();
                                    }
                                }};
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext(), this.resourcesProvider);
                                if (TextUtils.isEmpty(str12)) {
                                    builder2.setTitle(LocaleController.getString(R.string.BotAllowBiometryTitle));
                                    builder2.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.BotAllowBiometryMessage, UserObject.getUserName(this.botUser))));
                                } else {
                                    builder2.setTitle(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.BotAllowBiometryMessage, UserObject.getUserName(this.botUser))));
                                    builder2.setMessage(str12);
                                }
                                builder2.setPositiveButton(LocaleController.getString(R.string.Allow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda17
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i9) {
                                        BotWebViewContainer.this.lambda$onEventReceived$28(runnableArr, dialogInterface, i9);
                                    }
                                });
                                builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda18
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public final void onClick(DialogInterface dialogInterface, int i9) {
                                        BotWebViewContainer.this.lambda$onEventReceived$29(runnableArr, dialogInterface, i9);
                                    }
                                });
                                builder2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda19
                                    @Override // android.content.DialogInterface.OnDismissListener
                                    public final void onDismiss(DialogInterface dialogInterface) {
                                        BotWebViewContainer.lambda$onEventReceived$30(runnableArr, dialogInterface);
                                    }
                                });
                                builder2.show();
                                return;
                            }
                            if (!z11) {
                                botBiometry2.access_requested = true;
                                botBiometry2.save();
                                break;
                            }
                            break;
                        case 24:
                            JSONObject jSONObject10 = new JSONObject(str2);
                            String optString6 = jSONObject10.optString("type");
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
                                        String optString7 = jSONObject10.optString("impact_style");
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
                                        String optString8 = jSONObject10.optString("notification_type");
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
                                        return;
                                    }
                                    return;
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
                        case 25:
                            JSONObject jSONObject11 = new JSONObject(str2);
                            boolean optBoolean4 = jSONObject11.optBoolean("is_active", false);
                            String trim2 = jSONObject11.optString("text", this.lastButtonText).trim();
                            boolean z12 = jSONObject11.optBoolean("is_visible", false) && !TextUtils.isEmpty(trim2);
                            int parseColor3 = jSONObject11.has("color") ? Color.parseColor(jSONObject11.optString("color")) : this.lastButtonColor;
                            int parseColor4 = jSONObject11.has("text_color") ? Color.parseColor(jSONObject11.optString("text_color")) : this.lastButtonTextColor;
                            if (jSONObject11.optBoolean("is_progress_visible", false) && z12) {
                                str11 = "has_shine_effect";
                                z7 = true;
                            } else {
                                str11 = "has_shine_effect";
                                z7 = false;
                            }
                            boolean z13 = jSONObject11.optBoolean(str11, false) && z12;
                            this.lastButtonColor = parseColor3;
                            this.lastButtonTextColor = parseColor4;
                            this.lastButtonText = trim2;
                            this.buttonData = str2;
                            this.delegate.onSetupMainButton(z12, optBoolean4, trim2, parseColor3, parseColor4, z7, z13);
                            return;
                        case 26:
                            this.delegate.onWebAppSwipingBehavior(new JSONObject(str2).optBoolean("allow_vertical_swipe"));
                            return;
                        case 27:
                            boolean optBoolean5 = new JSONObject(str2).optBoolean("is_visible");
                            if (optBoolean5 != this.isSettingsButtonVisible) {
                                this.isSettingsButtonVisible = optBoolean5;
                                this.delegate.onSetSettingsButtonVisible(optBoolean5);
                                return;
                            }
                            return;
                        case 28:
                            try {
                                JSONObject jSONObject12 = new JSONObject(str2);
                                final String string5 = jSONObject12.getString("token");
                                try {
                                    str13 = jSONObject12.getString("reason");
                                } catch (Exception unused5) {
                                }
                                createBiometry();
                                BotBiometry botBiometry3 = this.biometry;
                                if (botBiometry3 == null) {
                                    return;
                                }
                                if (botBiometry3.access_granted) {
                                    botBiometry3.updateToken(str13, string5, new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda15
                                        @Override // org.telegram.messenger.Utilities.Callback
                                        public final void run(Object obj2) {
                                            BotWebViewContainer.this.lambda$onEventReceived$32(string5, (Boolean) obj2);
                                        }
                                    });
                                    return;
                                }
                                try {
                                    JSONObject jSONObject13 = new JSONObject();
                                    jSONObject13.put("status", "failed");
                                    notifyEvent("biometry_token_updated", jSONObject13);
                                    return;
                                } catch (Exception e9) {
                                    FileLog.e(e9);
                                    return;
                                }
                            } catch (Exception e10) {
                                FileLog.e(e10);
                                if (e10 instanceof JSONException) {
                                    error("JSON Parse error");
                                    return;
                                } else {
                                    unknownError();
                                    return;
                                }
                            }
                        case 29:
                            String optString9 = new JSONObject(str2).optString("color", null);
                            int color = TextUtils.isEmpty(optString9) ? Theme.getColor(Theme.key_windowBackgroundGray, this.resourcesProvider) : Color.parseColor(optString9);
                            Delegate delegate = this.delegate;
                            if (delegate != null) {
                                delegate.onWebAppSetNavigationBarColor(color);
                                return;
                            }
                            return;
                        case 30:
                            JSONObject jSONObject14 = new JSONObject(str2);
                            String optString10 = jSONObject14.optString("color", null);
                            if (!TextUtils.isEmpty(optString10)) {
                                int parseColor5 = Color.parseColor(optString10);
                                if (parseColor5 != 0) {
                                    this.delegate.onWebAppSetActionBarColor(-1, parseColor5, true);
                                    return;
                                }
                                return;
                            }
                            String optString11 = jSONObject14.optString("color_key");
                            int hashCode3 = optString11.hashCode();
                            if (hashCode3 != -1265068311) {
                                if (hashCode3 == -210781868 && optString11.equals("secondary_bg_color")) {
                                    c5 = 1;
                                    if (c5 != 0) {
                                        i = Theme.key_windowBackgroundWhite;
                                    } else {
                                        if (c5 != 1) {
                                            i2 = -1;
                                            if (i2 >= 0) {
                                                this.delegate.onWebAppSetActionBarColor(i2, Theme.getColor(i2, this.resourcesProvider), false);
                                                return;
                                            }
                                            return;
                                        }
                                        i = Theme.key_windowBackgroundGray;
                                    }
                                    i2 = i;
                                    if (i2 >= 0) {
                                    }
                                }
                                c5 = 65535;
                                if (c5 != 0) {
                                }
                                i2 = i;
                                if (i2 >= 0) {
                                }
                            } else {
                                if (optString11.equals("bg_color")) {
                                    c5 = 0;
                                    if (c5 != 0) {
                                    }
                                    i2 = i;
                                    if (i2 >= 0) {
                                    }
                                }
                                c5 = 65535;
                                if (c5 != 0) {
                                }
                                i2 = i;
                                if (i2 >= 0) {
                                }
                            }
                            break;
                        case 31:
                            this.delegate.onWebAppSetBackgroundColor(Color.parseColor(new JSONObject(str2).optString("color", "#ffffff")) | (-16777216));
                            return;
                        case ' ':
                            if (!ignoreDialog(3)) {
                                final int i9 = this.currentAccount;
                                final MyWebView myWebView3 = this.webView;
                                TL_bots.canSendMessage cansendmessage = new TL_bots.canSendMessage();
                                cansendmessage.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botUser);
                                ConnectionsManager.getInstance(this.currentAccount).sendRequest(cansendmessage, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda7
                                    @Override // org.telegram.tgnet.RequestDelegate
                                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                        BotWebViewContainer.this.lambda$onEventReceived$19(i9, myWebView3, tLObject, tL_error);
                                    }
                                });
                                return;
                            }
                            try {
                                JSONObject jSONObject15 = new JSONObject();
                                jSONObject15.put("status", "cancelled");
                                notifyEvent("write_access_requested", jSONObject15);
                                return;
                            } catch (Exception e11) {
                                FileLog.e(e11);
                                return;
                            }
                        case '!':
                            this.delegate.onWebAppExpand();
                            return;
                        default:
                            FileLog.d("unknown webapp event " + str);
                            return;
                    }
                    notifyBiometryReceived();
                } catch (JSONException e12) {
                    e = e12;
                    FileLog.e(e);
                }
            } catch (Exception e13) {
                e = e13;
                FileLog.e(e);
            }
        }
    }

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

    public void onWebEventReceived(String str, String str2) {
        boolean z;
        if (this.bot || this.delegate == null) {
            return;
        }
        d("onWebEventReceived " + str + " " + str2);
        str.hashCode();
        boolean z2 = true;
        char c = 65535;
        switch (str.hashCode()) {
            case -1695046810:
                if (str.equals("actionBarColor")) {
                    c = 0;
                    break;
                }
                break;
            case -462720700:
                if (str.equals("navigationBarColor")) {
                    c = 1;
                    break;
                }
                break;
            case 675009138:
                if (str.equals("siteName")) {
                    c = 2;
                    break;
                }
                break;
            case 997530486:
                if (str.equals("allowScroll")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
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
                    return;
                } catch (Exception unused) {
                    return;
                }
            case 2:
                d("siteName " + str2);
                MyWebView myWebView2 = this.webView;
                if (myWebView2 != null) {
                    myWebView2.lastSiteName = str2;
                    myWebView2.saveHistory();
                    return;
                }
                return;
            case 3:
                try {
                    JSONArray jSONArray2 = new JSONArray(str2);
                    z = jSONArray2.optBoolean(0, true);
                    try {
                        z2 = jSONArray2.optBoolean(1, true);
                    } catch (Exception unused2) {
                    }
                } catch (Exception unused3) {
                    z = true;
                }
                d("allowScroll " + z + " " + z2);
                if (getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) {
                    ((ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent()).allowThisScroll(z, z2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void openQrScanActivity() {
        Activity activity = this.parentActivity;
        if (activity == null) {
            return;
        }
        this.cameraBottomSheet = CameraScanActivity.showAsSheet(activity, false, 3, (CameraScanActivity.CameraScanActivityDelegate) new CameraScanActivity.CameraScanActivityDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer.6
            6() {
            }

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

    public void runWithPermissions(final String[] strArr, final Consumer consumer) {
        if (Build.VERSION.SDK_INT < 23 || checkPermissions(strArr)) {
            consumer.accept(Boolean.TRUE);
            return;
        }
        this.onPermissionsRequestResultCallback = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda26
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
            int dp = AndroidUtilities.dp(64.0f);
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
        onWebViewCreated();
        firstWebView = false;
    }

    private boolean showDialog(int i, AlertDialog alertDialog, final Runnable runnable) {
        if (alertDialog == null || ignoreDialog(i)) {
            return false;
        }
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda28
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                BotWebViewContainer.this.lambda$showDialog$37(runnable, dialogInterface);
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
            }
            this.isPageLoaded = false;
            updateKeyboardFocusable();
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.didSetNewTheme) {
            MyWebView myWebView = this.webView;
            if (myWebView != null) {
                myWebView.setBackgroundColor(getColor(Theme.key_windowBackgroundWhite));
            }
            this.flickerView.setColorFilter(new PorterDuffColorFilter(getColor(Theme.key_dialogSearchHint), PorterDuff.Mode.SRC_IN));
            notifyThemeChanged();
            return;
        }
        if (i == NotificationCenter.onActivityResultReceived) {
            onActivityResult(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (Intent) objArr[2]);
        } else if (i == NotificationCenter.onRequestPermissionResultReceived) {
            onRequestPermissionsResult(((Integer) objArr[0]).intValue(), (String[]) objArr[1], (int[]) objArr[2]);
        }
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
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(0.0f, 0.0f, getWidth(), getHeight());
            this.flickerDrawable.draw(canvas, rectF, 0.0f, this);
            invalidate();
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
                BotWebViewContainer.this.lambda$evaluateJs$5(z, str);
            }
        });
    }

    public BotWebViewProxy getBotProxy() {
        return this.botWebViewProxy;
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
            int measuredHeight = (int) (((webViewSwipeContainer.getMeasuredHeight() - webViewSwipeContainer.getOffsetY()) - webViewSwipeContainer.getSwipeOffsetY()) + webViewSwipeContainer.getTopActionBarOffsetY());
            if (!z2 && measuredHeight == this.lastViewportHeightReported && this.lastViewportStateStable == z && this.lastViewportIsExpanded == this.lastExpanded) {
                return;
            }
            this.lastViewportHeightReported = measuredHeight;
            this.lastViewportStateStable = z;
            this.lastViewportIsExpanded = this.lastExpanded;
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("height", measuredHeight / AndroidUtilities.density);
                jSONObject.put("is_state_stable", z);
                jSONObject.put("is_expanded", this.lastExpanded);
                notifyEvent("viewport_changed", jSONObject);
            } catch (JSONException unused) {
            }
        }
    }

    public boolean isBackButtonVisible() {
        return this.isBackButtonVisible;
    }

    public boolean isPageLoaded() {
        return this.isPageLoaded;
    }

    public void loadFlickerAndSettingsItem(int i, long j, ActionBarMenuSubItem actionBarMenuSubItem) {
        TLRPC.TL_attachMenuBot tL_attachMenuBot;
        boolean z;
        String publicUsername = UserObject.getPublicUsername(MessagesController.getInstance(i).getUser(Long.valueOf(j)));
        if (publicUsername != null && publicUsername.equals("DurgerKingBot")) {
            this.flickerView.setVisibility(0);
            this.flickerView.setAlpha(1.0f);
            this.flickerView.setImageDrawable(SvgHelper.getDrawable(R.raw.durgerking_placeholder, Integer.valueOf(getColor(Theme.key_windowBackgroundGray))));
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
        if (tL_attachMenuBot == null) {
            TLRPC.TL_messages_getAttachMenuBot tL_messages_getAttachMenuBot = new TLRPC.TL_messages_getAttachMenuBot();
            tL_messages_getAttachMenuBot.bot = MessagesController.getInstance(i).getInputUser(j);
            ConnectionsManager.getInstance(i).sendRequest(tL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda4
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    BotWebViewContainer.this.lambda$loadFlickerAndSettingsItem$2(tLObject, tL_error);
                }
            });
            return;
        }
        TLRPC.TL_attachMenuBotIcon placeholderStaticAttachMenuBotIcon = MediaDataController.getPlaceholderStaticAttachMenuBotIcon(tL_attachMenuBot);
        if (placeholderStaticAttachMenuBotIcon == null) {
            placeholderStaticAttachMenuBotIcon = MediaDataController.getStaticAttachMenuBotIcon(tL_attachMenuBot);
            z = true;
        } else {
            z = false;
        }
        if (placeholderStaticAttachMenuBotIcon != null) {
            this.flickerView.setVisibility(0);
            this.flickerView.setAlpha(1.0f);
            this.flickerView.setImage(ImageLocation.getForDocument(placeholderStaticAttachMenuBotIcon.icon), (String) null, (Drawable) null, tL_attachMenuBot);
            setupFlickerParams(z);
        }
    }

    public void loadUrl(int i, final String str) {
        this.currentAccount = i;
        NotificationCenter.getInstance(i).doOnIdle(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$loadUrl$4(str);
            }
        });
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
            3() {
            }

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

    public void onFaviconChanged(Bitmap bitmap) {
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

    public void onWebViewCreated() {
    }

    public void preserveWebView() {
        d("preserveWebView");
        this.preserving = true;
    }

    public void reload() {
        NotificationCenter.getInstance(this.currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$reload$3();
            }
        });
    }

    public void replaceWebView(MyWebView myWebView, Object obj) {
        setupWebView(myWebView, obj);
    }

    public void resetWebView() {
        this.webView = null;
    }

    public void restoreButtonData() {
        String str = this.buttonData;
        if (str != null) {
            onEventReceived("web_app_setup_main_button", str);
        }
        String str2 = this.secondaryButtonData;
        if (str2 != null) {
            onEventReceived("web_app_setup_secondary_button", str2);
        }
    }

    public void setBotUser(TLRPC.User user) {
        this.botUser = user;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
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
                2() {
                }

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
