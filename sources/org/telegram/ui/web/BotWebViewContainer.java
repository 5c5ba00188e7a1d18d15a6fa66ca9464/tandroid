package org.telegram.ui.web;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
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
import androidx.core.graphics.ColorUtils;
import androidx.core.util.Consumer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.IDN;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import org.telegram.messenger.FileLog;
import org.telegram.messenger.HttpGetFileTask;
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
import org.telegram.tgnet.TLRPC$InputInvoice;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$TL_attachMenuBot;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotIcon;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotsBot;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_dataJSON;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputInvoiceSlug;
import org.telegram.tgnet.TLRPC$TL_messages_getAttachMenuBot;
import org.telegram.tgnet.TLRPC$TL_payments_getPaymentForm;
import org.telegram.tgnet.TLRPC$Updates;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.tl.TL_bots$allowSendMessage;
import org.telegram.tgnet.tl.TL_bots$canSendMessage;
import org.telegram.tgnet.tl.TL_bots$invokeWebViewCustomMethod;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
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
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.telegram.ui.Stories.recorder.StoryRecorder;
import org.telegram.ui.WrappedResourceProvider;
import org.telegram.ui.bots.BotBiometry;
import org.telegram.ui.bots.BotBiometrySettings;
import org.telegram.ui.bots.BotWebViewSheet;
import org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout;
import org.telegram.ui.web.BotWebViewContainer;
import org.telegram.ui.web.BrowserHistory;
import org.telegram.ui.web.WebMetadataCache;
/* loaded from: classes.dex */
public abstract class BotWebViewContainer extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private static HashMap<String, String> rotatedTONHosts;
    private static int tags;
    private BotBiometry biometry;
    private long blockedDialogsUntil;
    public final boolean bot;
    private TLRPC$User botUser;
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
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mUrl;
    private Runnable onCloseListener;
    private Runnable onPermissionsRequestResultCallback;
    private Activity parentActivity;
    private boolean preserving;
    private Theme.ResourcesProvider resourcesProvider;
    private int shownDialogsCount;
    private final int tag;
    private boolean wasFocusable;
    private boolean wasOpenedByLinkIntent;
    private MyWebView webView;
    private boolean webViewNotAvailable;
    private TextView webViewNotAvailableText;
    private Consumer<Float> webViewProgressListener;
    private WebViewProxy webViewProxy;
    private WebViewScrollListener webViewScrollListener;

    /* loaded from: classes.dex */
    public interface WebViewScrollListener {
        void onWebViewScrolled(WebView webView, int i, int i2);
    }

    public static /* synthetic */ void lambda$evaluateJs$5(String str) {
    }

    protected void onErrorShown(boolean z, int i, String str) {
    }

    public void onFaviconChanged(Bitmap bitmap) {
    }

    protected void onTitleChanged(String str) {
    }

    protected void onURLChanged(String str, boolean z, boolean z2) {
    }

    public void onWebViewCreated() {
    }

    static /* synthetic */ int access$1408() {
        int i = tags;
        tags = i + 1;
        return i;
    }

    public void showLinkCopiedBulletin() {
        BulletinFactory.of(this, this.resourcesProvider).createCopyLinkBulletin().show(true);
    }

    public BotWebViewContainer(Context context, Theme.ResourcesProvider resourcesProvider, int i, boolean z) {
        super(context);
        CellFlickerDrawable cellFlickerDrawable = new CellFlickerDrawable();
        this.flickerDrawable = cellFlickerDrawable;
        this.lastButtonColor = getColor(Theme.key_featuredStickers_addButton);
        this.lastButtonTextColor = getColor(Theme.key_featuredStickers_buttonText);
        this.lastButtonText = "";
        this.lastDialogType = -1;
        this.shownDialogsCount = 0;
        int i2 = tags;
        tags = i2 + 1;
        this.tag = i2;
        this.bot = z;
        this.resourcesProvider = resourcesProvider;
        d("created new webview container");
        if (context instanceof Activity) {
            this.parentActivity = (Activity) context;
        }
        cellFlickerDrawable.drawFrame = false;
        cellFlickerDrawable.setColors(i, 153, 204);
        BackupImageView backupImageView = new BackupImageView(context) { // from class: org.telegram.ui.web.BotWebViewContainer.1
            {
                BotWebViewContainer.this = this;
                this.imageReceiver = new 1(this);
            }

            /* loaded from: classes.dex */
            public class 1 extends ImageReceiver {
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                1(View view) {
                    super(view);
                    1.this = r1;
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

                public /* synthetic */ void lambda$setImageBitmapByKey$0(ValueAnimator valueAnimator) {
                    ((BackupImageView) 1.this).imageReceiver.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    invalidate();
                }
            }

            @Override // org.telegram.ui.Components.BackupImageView, android.view.View
            public void onDraw(Canvas canvas) {
                if (BotWebViewContainer.this.isFlickeringCenter) {
                    super.onDraw(canvas);
                    return;
                }
                Drawable drawable = this.imageReceiver.getDrawable();
                if (drawable != null) {
                    this.imageReceiver.setImageCoords(0.0f, 0.0f, getWidth(), drawable.getIntrinsicHeight() * (getWidth() / drawable.getIntrinsicWidth()));
                    this.imageReceiver.draw(canvas);
                }
            }
        };
        this.flickerView = backupImageView;
        backupImageView.setColorFilter(new PorterDuffColorFilter(getColor(Theme.key_dialogSearchHint), PorterDuff.Mode.SRC_IN));
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

    public void setViewPortByMeasureSuppressed(boolean z) {
        this.isViewPortByMeasureSuppressed = z;
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

    public void replaceWebView(MyWebView myWebView, Object obj) {
        setupWebView(myWebView, obj);
    }

    private void setupWebView(MyWebView myWebView) {
        setupWebView(myWebView, null);
    }

    public BotWebViewProxy getBotProxy() {
        return this.botWebViewProxy;
    }

    public WebViewProxy getProxy() {
        return this.webViewProxy;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
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
        if (!this.bot) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            int i = Build.VERSION.SDK_INT;
            if (i >= 21) {
                cookieManager.setAcceptThirdPartyCookies(this.webView, true);
            }
            if (i >= 21) {
                CookieManager.getInstance().flush();
            }
        } else {
            myWebView3.setBackgroundColor(getColor(Theme.key_windowBackgroundWhite));
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
        }
        try {
            String replace = settings.getUserAgentString().replace("; wv)", ")");
            settings.setUserAgentString(replace.replaceAll("\\(Linux; Android.+;[^)]+\\)", "(Linux; Android " + Build.VERSION.RELEASE + "; K)"));
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
        if (Build.VERSION.SDK_INT >= 17) {
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
                    WebViewProxy webViewProxy2 = new WebViewProxy(this);
                    this.webViewProxy = webViewProxy2;
                    this.webView.addJavascriptInterface(webViewProxy2, "TelegramWebview");
                } else if (myWebView == null) {
                    this.webView.addJavascriptInterface(webViewProxy, "TelegramWebview");
                }
                this.webViewProxy.setContainer(this);
            }
        }
        onWebViewCreated();
    }

    public void onOpenUri(Uri uri) {
        onOpenUri(uri, null, !this.bot, false);
    }

    private void onOpenUri(Uri uri, String str, boolean z, boolean z2) {
        if (this.isRequestingPageOpen) {
            return;
        }
        if (System.currentTimeMillis() - this.lastClickMs <= 1000 || !z2) {
            this.lastClickMs = 0L;
            boolean[] zArr = {false};
            if (Browser.isInternalUri(uri, zArr) && !zArr[0] && this.delegate != null) {
                setKeyboardFocusable(false);
            }
            Browser.openUrl(getContext(), uri, true, z, false, null, str, false);
        }
    }

    private void updateKeyboardFocusable() {
        boolean z = this.keyboardFocusable && this.isPageLoaded;
        if (this.wasFocusable != z) {
            if (!z) {
                setDescendantFocusability(393216);
                setFocusable(false);
                this.webView.setDescendantFocusability(393216);
                this.webView.clearFocus();
                AndroidUtilities.hideKeyboard(this);
            } else {
                setDescendantFocusability(131072);
                setFocusable(true);
                this.webView.setDescendantFocusability(131072);
            }
        }
        this.wasFocusable = z;
    }

    public void setKeyboardFocusable(boolean z) {
        this.keyboardFocusable = z;
        updateKeyboardFocusable();
    }

    public static int getMainButtonRippleColor(int i) {
        return ColorUtils.calculateLuminance(i) >= 0.30000001192092896d ? 301989888 : 385875967;
    }

    public static Drawable getMainButtonRippleDrawable(int i) {
        return Theme.createSelectorWithBackgroundDrawable(i, getMainButtonRippleColor(i));
    }

    public void updateFlickerBackgroundColor(int i) {
        this.flickerDrawable.setColors(i, 153, 204);
    }

    public boolean onBackPressed() {
        if (this.webView != null && this.isBackButtonVisible) {
            notifyEvent("back_button_pressed", null);
            return true;
        }
        return false;
    }

    public void setPageLoaded(String str, boolean z) {
        MyWebView myWebView = this.webView;
        boolean z2 = myWebView == null || !myWebView.canGoBack();
        MyWebView myWebView2 = this.webView;
        onURLChanged(str, z2, myWebView2 == null || !myWebView2.canGoForward());
        MyWebView myWebView3 = this.webView;
        if (myWebView3 != null) {
            myWebView3.isPageLoaded = true;
            updateKeyboardFocusable();
        }
        if (this.isPageLoaded) {
            d("setPageLoaded: already loaded");
            return;
        }
        if (z && this.webView != null && this.flickerView != null) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(this.webView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.flickerView, View.ALPHA, 0.0f));
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.web.BotWebViewContainer.2
                {
                    BotWebViewContainer.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    BotWebViewContainer.this.flickerView.setVisibility(8);
                }
            });
            animatorSet.start();
        } else {
            MyWebView myWebView4 = this.webView;
            if (myWebView4 != null) {
                myWebView4.setAlpha(1.0f);
            }
            BackupImageView backupImageView = this.flickerView;
            if (backupImageView != null) {
                backupImageView.setAlpha(0.0f);
                this.flickerView.setVisibility(8);
            }
        }
        this.mUrl = str;
        d("setPageLoaded: isPageLoaded = true!");
        this.isPageLoaded = true;
        updateKeyboardFocusable();
        this.delegate.onWebAppReady();
    }

    public void setState(boolean z, String str) {
        d("setState(" + z + ", " + str + ")");
        this.isPageLoaded = z;
        this.mUrl = str;
        updateKeyboardFocusable();
    }

    public void setIsBackButtonVisible(boolean z) {
        this.isBackButtonVisible = z;
    }

    public String getUrlLoaded() {
        return this.mUrl;
    }

    public boolean hasUserPermissions() {
        return this.hasUserPermissions;
    }

    public void setBotUser(TLRPC$User tLRPC$User) {
        this.botUser = tLRPC$User;
    }

    public void runWithPermissions(final String[] strArr, final Consumer<Boolean> consumer) {
        if (Build.VERSION.SDK_INT < 23) {
            consumer.accept(Boolean.TRUE);
        } else if (checkPermissions(strArr)) {
            consumer.accept(Boolean.TRUE);
        } else {
            this.onPermissionsRequestResultCallback = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda17
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
    }

    public /* synthetic */ void lambda$runWithPermissions$0(Consumer consumer, String[] strArr) {
        consumer.accept(Boolean.valueOf(checkPermissions(strArr)));
    }

    public boolean isPageLoaded() {
        return this.isPageLoaded;
    }

    public void setParentActivity(Activity activity) {
        this.parentActivity = activity;
    }

    private boolean checkPermissions(String[] strArr) {
        for (String str : strArr) {
            if (getContext().checkSelfPermission(str) != 0) {
                return false;
            }
        }
        return true;
    }

    public void restoreButtonData() {
        String str = this.buttonData;
        if (str != null) {
            onEventReceived("web_app_setup_main_button", str);
        }
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

    public void onSettingsButtonPressed() {
        this.lastClickMs = System.currentTimeMillis();
        notifyEvent("settings_button_pressed", null);
    }

    public void onMainButtonPressed() {
        this.lastClickMs = System.currentTimeMillis();
        notifyEvent("main_button_pressed", null);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Runnable runnable;
        if (i != 4000 || (runnable = this.onPermissionsRequestResultCallback) == null) {
            return;
        }
        runnable.run();
        this.onPermissionsRequestResultCallback = null;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 3000 || this.mFilePathCallback == null) {
            return;
        }
        this.mFilePathCallback.onReceiveValue((i2 != -1 || intent == null || intent.getDataString() == null) ? null : new Uri[]{Uri.parse(intent.getDataString())});
        this.mFilePathCallback = null;
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.isViewPortByMeasureSuppressed) {
            return;
        }
        invalidateViewPortHeight(true);
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
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("height", measuredHeight / AndroidUtilities.density);
                jSONObject.put("is_state_stable", z);
                jSONObject.put("is_expanded", this.lastExpanded);
                notifyEvent("viewport_changed", jSONObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        } else if (view == this.webViewNotAvailableText) {
            canvas.save();
            canvas.translate(0.0f, (ActionBar.getCurrentActionBarHeight() - ((View) getParent()).getTranslationY()) / 2.0f);
            boolean drawChild2 = super.drawChild(canvas, view, j);
            canvas.restore();
            return drawChild2;
        } else {
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
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.flickerDrawable.setParentWidth(getMeasuredWidth());
    }

    public void setWebViewProgressListener(Consumer<Float> consumer) {
        this.webViewProgressListener = consumer;
    }

    public MyWebView getWebView() {
        return this.webView;
    }

    public void loadFlickerAndSettingsItem(int i, long j, ActionBarMenuSubItem actionBarMenuSubItem) {
        TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot;
        boolean z;
        String publicUsername = UserObject.getPublicUsername(MessagesController.getInstance(i).getUser(Long.valueOf(j)));
        if (publicUsername != null && publicUsername.equals("DurgerKingBot")) {
            this.flickerView.setVisibility(0);
            this.flickerView.setAlpha(1.0f);
            this.flickerView.setImageDrawable(SvgHelper.getDrawable(R.raw.durgerking_placeholder, Integer.valueOf(getColor(Theme.key_windowBackgroundGray))));
            setupFlickerParams(false);
            return;
        }
        Iterator<TLRPC$TL_attachMenuBot> it = MediaDataController.getInstance(i).getAttachMenuBots().bots.iterator();
        while (true) {
            if (!it.hasNext()) {
                tLRPC$TL_attachMenuBot = null;
                break;
            }
            tLRPC$TL_attachMenuBot = it.next();
            if (tLRPC$TL_attachMenuBot.bot_id == j) {
                break;
            }
        }
        if (tLRPC$TL_attachMenuBot != null) {
            TLRPC$TL_attachMenuBotIcon placeholderStaticAttachMenuBotIcon = MediaDataController.getPlaceholderStaticAttachMenuBotIcon(tLRPC$TL_attachMenuBot);
            if (placeholderStaticAttachMenuBotIcon == null) {
                placeholderStaticAttachMenuBotIcon = MediaDataController.getStaticAttachMenuBotIcon(tLRPC$TL_attachMenuBot);
                z = true;
            } else {
                z = false;
            }
            if (placeholderStaticAttachMenuBotIcon != null) {
                this.flickerView.setVisibility(0);
                this.flickerView.setAlpha(1.0f);
                this.flickerView.setImage(ImageLocation.getForDocument(placeholderStaticAttachMenuBotIcon.icon), (String) null, (Drawable) null, tLRPC$TL_attachMenuBot);
                setupFlickerParams(z);
                return;
            }
            return;
        }
        TLRPC$TL_messages_getAttachMenuBot tLRPC$TL_messages_getAttachMenuBot = new TLRPC$TL_messages_getAttachMenuBot();
        tLRPC$TL_messages_getAttachMenuBot.bot = MessagesController.getInstance(i).getInputUser(j);
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda33
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BotWebViewContainer.this.lambda$loadFlickerAndSettingsItem$2(tLObject, tLRPC$TL_error);
            }
        });
    }

    public /* synthetic */ void lambda$loadFlickerAndSettingsItem$2(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$loadFlickerAndSettingsItem$1(tLObject);
            }
        });
    }

    public /* synthetic */ void lambda$loadFlickerAndSettingsItem$1(TLObject tLObject) {
        boolean z;
        if (tLObject instanceof TLRPC$TL_attachMenuBotsBot) {
            TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot = ((TLRPC$TL_attachMenuBotsBot) tLObject).bot;
            TLRPC$TL_attachMenuBotIcon placeholderStaticAttachMenuBotIcon = MediaDataController.getPlaceholderStaticAttachMenuBotIcon(tLRPC$TL_attachMenuBot);
            if (placeholderStaticAttachMenuBotIcon == null) {
                placeholderStaticAttachMenuBotIcon = MediaDataController.getStaticAttachMenuBotIcon(tLRPC$TL_attachMenuBot);
                z = true;
            } else {
                z = false;
            }
            if (placeholderStaticAttachMenuBotIcon != null) {
                this.flickerView.setVisibility(0);
                this.flickerView.setAlpha(1.0f);
                this.flickerView.setImage(ImageLocation.getForDocument(placeholderStaticAttachMenuBotIcon.icon), (String) null, (Drawable) null, tLRPC$TL_attachMenuBot);
                setupFlickerParams(z);
            }
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

    public void reload() {
        NotificationCenter.getInstance(this.currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$reload$3();
            }
        });
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

    public void loadUrl(int i, final String str) {
        this.currentAccount = i;
        NotificationCenter.getInstance(i).doOnIdle(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$loadUrl$4(str);
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

            {
                BotWebViewContainer.this = this;
            }

            @Override // org.telegram.ui.Components.Bulletin.Delegate
            public int getBottomOffset(int i) {
                if (BotWebViewContainer.this.getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) {
                    ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = (ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) BotWebViewContainer.this.getParent();
                    return (int) ((webViewSwipeContainer.getOffsetY() + webViewSwipeContainer.getSwipeOffsetY()) - webViewSwipeContainer.getTopActionBarOffsetY());
                }
                return 0;
            }
        });
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

    public void preserveWebView() {
        d("preserveWebView");
        this.preserving = true;
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

    public void resetWebView() {
        this.webView = null;
    }

    public boolean isBackButtonVisible() {
        return this.isBackButtonVisible;
    }

    public void evaluateJs(final String str, final boolean z) {
        NotificationCenter.getInstance(this.currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$evaluateJs$6(z, str);
            }
        });
    }

    public /* synthetic */ void lambda$evaluateJs$6(boolean z, String str) {
        if (z) {
            checkCreateWebView();
        }
        MyWebView myWebView = this.webView;
        if (myWebView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            myWebView.evaluateJavascript(str, new ValueCallback() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda12
                @Override // android.webkit.ValueCallback
                public final void onReceiveValue(Object obj) {
                    BotWebViewContainer.lambda$evaluateJs$5((String) obj);
                }
            });
            return;
        }
        try {
            myWebView.loadUrl("javascript:" + URLEncoder.encode(str, "UTF-8"));
        } catch (UnsupportedEncodingException unused) {
            MyWebView myWebView2 = this.webView;
            myWebView2.loadUrl("javascript:" + URLEncoder.encode(str));
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
        } else if (i == NotificationCenter.onActivityResultReceived) {
            onActivityResult(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (Intent) objArr[2]);
        } else if (i == NotificationCenter.onRequestPermissionResultReceived) {
            onRequestPermissionsResult(((Integer) objArr[0]).intValue(), (String[]) objArr[1], (int[]) objArr[2]);
        }
    }

    public void notifyThemeChanged() {
        notifyEvent("theme_changed", buildThemeParams());
    }

    public void notifyEvent(String str, JSONObject jSONObject) {
        d("notifyEvent " + str);
        evaluateJs("window.Telegram.WebView.receiveEvent('" + str + "', " + jSONObject + ");", false);
    }

    public void setWebViewScrollListener(WebViewScrollListener webViewScrollListener) {
        this.webViewScrollListener = webViewScrollListener;
        MyWebView myWebView = this.webView;
        if (myWebView != null) {
            myWebView.setContainers(this, webViewScrollListener);
        }
    }

    public void setOnCloseRequestedListener(Runnable runnable) {
        this.onCloseListener = runnable;
        MyWebView myWebView = this.webView;
        if (myWebView != null) {
            myWebView.setCloseListener(runnable);
        }
    }

    public void setWasOpenedByLinkIntent(boolean z) {
        this.wasOpenedByLinkIntent = z;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void onWebEventReceived(String str, String str2) {
        boolean z;
        if (this.bot || this.delegate == null) {
            return;
        }
        d("onWebEventReceived " + str + " " + str2);
        str.hashCode();
        char c = 65535;
        boolean z2 = true;
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

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:1003:0x0473  */
    /* JADX WARN: Removed duplicated region for block: B:1010:0x0481 A[Catch: Exception -> 0x04eb, TryCatch #2 {Exception -> 0x04eb, blocks: (B:961:0x03ea, B:1046:0x04e6, B:984:0x0433, B:985:0x0437, B:1008:0x047b, B:1009:0x047e, B:1010:0x0481, B:992:0x0451, B:995:0x045c, B:998:0x0466, B:1012:0x0486, B:1013:0x0490, B:1040:0x04d5, B:1041:0x04d8, B:1042:0x04db, B:1043:0x04de, B:1044:0x04e1, B:1015:0x0494, B:1018:0x049e, B:1021:0x04a8, B:1024:0x04b2, B:1027:0x04bc, B:968:0x040a, B:971:0x0414, B:974:0x041e), top: B:1311:0x03ea }] */
    /* JADX WARN: Removed duplicated region for block: B:1012:0x0486 A[Catch: Exception -> 0x04eb, TryCatch #2 {Exception -> 0x04eb, blocks: (B:961:0x03ea, B:1046:0x04e6, B:984:0x0433, B:985:0x0437, B:1008:0x047b, B:1009:0x047e, B:1010:0x0481, B:992:0x0451, B:995:0x045c, B:998:0x0466, B:1012:0x0486, B:1013:0x0490, B:1040:0x04d5, B:1041:0x04d8, B:1042:0x04db, B:1043:0x04de, B:1044:0x04e1, B:1015:0x0494, B:1018:0x049e, B:1021:0x04a8, B:1024:0x04b2, B:1027:0x04bc, B:968:0x040a, B:971:0x0414, B:974:0x041e), top: B:1311:0x03ea }] */
    /* JADX WARN: Removed duplicated region for block: B:1046:0x04e6 A[Catch: Exception -> 0x04eb, TRY_LEAVE, TryCatch #2 {Exception -> 0x04eb, blocks: (B:961:0x03ea, B:1046:0x04e6, B:984:0x0433, B:985:0x0437, B:1008:0x047b, B:1009:0x047e, B:1010:0x0481, B:992:0x0451, B:995:0x045c, B:998:0x0466, B:1012:0x0486, B:1013:0x0490, B:1040:0x04d5, B:1041:0x04d8, B:1042:0x04db, B:1043:0x04de, B:1044:0x04e1, B:1015:0x0494, B:1018:0x049e, B:1021:0x04a8, B:1024:0x04b2, B:1027:0x04bc, B:968:0x040a, B:971:0x0414, B:974:0x041e), top: B:1311:0x03ea }] */
    /* JADX WARN: Removed duplicated region for block: B:1147:0x0719 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:1148:0x071a  */
    /* JADX WARN: Removed duplicated region for block: B:1382:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1389:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:897:0x02cf  */
    /* JADX WARN: Removed duplicated region for block: B:901:0x02d7 A[Catch: JSONException -> 0x02e9, TryCatch #11 {JSONException -> 0x02e9, blocks: (B:879:0x0285, B:881:0x0294, B:883:0x029a, B:884:0x02a3, B:903:0x02db, B:900:0x02d4, B:901:0x02d7, B:889:0x02b8, B:892:0x02c2), top: B:1326:0x0285 }] */
    /* JADX WARN: Removed duplicated region for block: B:903:0x02db A[Catch: JSONException -> 0x02e9, TRY_LEAVE, TryCatch #11 {JSONException -> 0x02e9, blocks: (B:879:0x0285, B:881:0x0294, B:883:0x029a, B:884:0x02a3, B:903:0x02db, B:900:0x02d4, B:901:0x02d7, B:889:0x02b8, B:892:0x02c2), top: B:1326:0x0285 }] */
    /* JADX WARN: Removed duplicated region for block: B:979:0x042b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onEventReceived(String str, String str2) {
        char c;
        boolean z;
        char c2;
        int i;
        char c3;
        char c4;
        BotWebViewVibrationEffect botWebViewVibrationEffect;
        char c5;
        BottomSheet bottomSheet;
        String str3;
        String str4;
        String str5;
        String str6;
        final String str7;
        final String str8;
        final String str9;
        JSONObject jSONObject;
        String str10;
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
                case -1341039673:
                    if (str.equals("web_app_setup_closing_behavior")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case -1309122684:
                    if (str.equals("web_app_open_scan_qr_popup")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case -1263619595:
                    if (str.equals("web_app_request_phone")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case -1259935152:
                    if (str.equals("web_app_request_theme")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case -1093591555:
                    if (str.equals("web_app_biometry_open_settings")) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case -921083201:
                    if (str.equals("web_app_request_viewport")) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case -512688845:
                    if (str.equals("web_app_biometry_request_auth")) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                case -439770054:
                    if (str.equals("web_app_open_tg_link")) {
                        c = '\r';
                        break;
                    }
                    c = 65535;
                    break;
                case -244584646:
                    if (str.equals("web_app_share_to_story")) {
                        c = 14;
                        break;
                    }
                    c = 65535;
                    break;
                case -71726289:
                    if (str.equals("web_app_close")) {
                        c = 15;
                        break;
                    }
                    c = 65535;
                    break;
                case -58095910:
                    if (str.equals("web_app_ready")) {
                        c = 16;
                        break;
                    }
                    c = 65535;
                    break;
                case 22015443:
                    if (str.equals("web_app_read_text_from_clipboard")) {
                        c = 17;
                        break;
                    }
                    c = 65535;
                    break;
                case 668142772:
                    if (str.equals("web_app_data_send")) {
                        c = 18;
                        break;
                    }
                    c = 65535;
                    break;
                case 751292356:
                    if (str.equals("web_app_switch_inline_query")) {
                        c = 19;
                        break;
                    }
                    c = 65535;
                    break;
                case 1011447167:
                    if (str.equals("web_app_setup_back_button")) {
                        c = 20;
                        break;
                    }
                    c = 65535;
                    break;
                case 1210129967:
                    if (str.equals("web_app_biometry_request_access")) {
                        c = 21;
                        break;
                    }
                    c = 65535;
                    break;
                case 1273834781:
                    if (str.equals("web_app_trigger_haptic_feedback")) {
                        c = 22;
                        break;
                    }
                    c = 65535;
                    break;
                case 1398490221:
                    if (str.equals("web_app_setup_main_button")) {
                        c = 23;
                        break;
                    }
                    c = 65535;
                    break;
                case 1453051298:
                    if (str.equals("web_app_setup_swipe_behavior")) {
                        c = 24;
                        break;
                    }
                    c = 65535;
                    break;
                case 1455972419:
                    if (str.equals("web_app_setup_settings_button")) {
                        c = 25;
                        break;
                    }
                    c = 65535;
                    break;
                case 1882780382:
                    if (str.equals("web_app_biometry_update_token")) {
                        c = 26;
                        break;
                    }
                    c = 65535;
                    break;
                case 1917103703:
                    if (str.equals("web_app_set_header_color")) {
                        c = 27;
                        break;
                    }
                    c = 65535;
                    break;
                case 2001330488:
                    if (str.equals("web_app_set_background_color")) {
                        c = 28;
                        break;
                    }
                    c = 65535;
                    break;
                case 2036090717:
                    if (str.equals("web_app_request_write_access")) {
                        c = 29;
                        break;
                    }
                    c = 65535;
                    break;
                case 2139805763:
                    if (str.equals("web_app_expand")) {
                        c = 30;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            BotWebViewVibrationEffect botWebViewVibrationEffect2 = null;
            String str11 = null;
            String str12 = null;
            String str13 = null;
            String str14 = null;
            botWebViewVibrationEffect2 = null;
            botWebViewVibrationEffect2 = null;
            switch (c) {
                case 0:
                    try {
                        JSONObject jSONObject2 = new JSONObject(str2);
                        final String string = jSONObject2.getString("req_id");
                        String string2 = jSONObject2.getString("method");
                        String obj = jSONObject2.get("params").toString();
                        TL_bots$invokeWebViewCustomMethod tL_bots$invokeWebViewCustomMethod = new TL_bots$invokeWebViewCustomMethod();
                        tL_bots$invokeWebViewCustomMethod.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botUser);
                        tL_bots$invokeWebViewCustomMethod.custom_method = string2;
                        TLRPC$TL_dataJSON tLRPC$TL_dataJSON = new TLRPC$TL_dataJSON();
                        tL_bots$invokeWebViewCustomMethod.params = tLRPC$TL_dataJSON;
                        tLRPC$TL_dataJSON.data = obj;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_bots$invokeWebViewCustomMethod, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda35
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                BotWebViewContainer.this.lambda$onEventReceived$21(string, tLObject, tLRPC$TL_error);
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
                    notifyBiometryReceived();
                    return;
                case 3:
                    try {
                        JSONObject jSONObject3 = new JSONObject(str2);
                        Uri parse = Uri.parse(jSONObject3.optString("url"));
                        String optString = jSONObject3.optString("try_browser");
                        if (MessagesController.getInstance(this.currentAccount).webAppAllowedProtocols == null || !MessagesController.getInstance(this.currentAccount).webAppAllowedProtocols.contains(parse.getScheme())) {
                            return;
                        }
                        onOpenUri(parse, optString, jSONObject3.optBoolean("try_instant_view"), true);
                        return;
                    } catch (Exception e2) {
                        FileLog.e(e2);
                        return;
                    }
                case 4:
                    try {
                        if (this.currentDialog != null) {
                            return;
                        }
                        if (System.currentTimeMillis() - this.lastDialogClosed <= 150) {
                            int i2 = this.dialogSequentialOpenTimes + 1;
                            this.dialogSequentialOpenTimes = i2;
                            if (i2 >= 3) {
                                this.dialogSequentialOpenTimes = 0;
                                this.lastDialogCooldownTime = System.currentTimeMillis();
                                return;
                            }
                        }
                        if (System.currentTimeMillis() - this.lastDialogCooldownTime <= 3000) {
                            return;
                        }
                        JSONObject jSONObject4 = new JSONObject(str2);
                        String optString2 = jSONObject4.optString("title", null);
                        String string3 = jSONObject4.getString("message");
                        JSONArray jSONArray = jSONObject4.getJSONArray("buttons");
                        AlertDialog.Builder message = new AlertDialog.Builder(getContext()).setTitle(optString2).setMessage(string3);
                        ArrayList arrayList = new ArrayList();
                        for (int i3 = 0; i3 < jSONArray.length(); i3++) {
                            arrayList.add(new PopupButton(jSONArray.getJSONObject(i3)));
                        }
                        if (arrayList.size() > 3) {
                            return;
                        }
                        final AtomicBoolean atomicBoolean = new AtomicBoolean();
                        if (arrayList.size() >= 1) {
                            final PopupButton popupButton = (PopupButton) arrayList.get(0);
                            message.setPositiveButton(popupButton.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda2
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i4) {
                                    BotWebViewContainer.this.lambda$onEventReceived$7(popupButton, atomicBoolean, dialogInterface, i4);
                                }
                            });
                        }
                        if (arrayList.size() >= 2) {
                            final PopupButton popupButton2 = (PopupButton) arrayList.get(1);
                            message.setNegativeButton(popupButton2.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda1
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i4) {
                                    BotWebViewContainer.this.lambda$onEventReceived$8(popupButton2, atomicBoolean, dialogInterface, i4);
                                }
                            });
                        }
                        if (arrayList.size() == 3) {
                            final PopupButton popupButton3 = (PopupButton) arrayList.get(2);
                            message.setNeutralButton(popupButton3.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda0
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i4) {
                                    BotWebViewContainer.this.lambda$onEventReceived$9(popupButton3, atomicBoolean, dialogInterface, i4);
                                }
                            });
                        }
                        message.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda10
                            @Override // android.content.DialogInterface.OnDismissListener
                            public final void onDismiss(DialogInterface dialogInterface) {
                                BotWebViewContainer.this.lambda$onEventReceived$10(atomicBoolean, dialogInterface);
                            }
                        });
                        this.currentDialog = message.show();
                        if (arrayList.size() >= 1) {
                            PopupButton popupButton4 = (PopupButton) arrayList.get(0);
                            if (popupButton4.textColorKey >= 0) {
                                ((TextView) this.currentDialog.getButton(-1)).setTextColor(getColor(popupButton4.textColorKey));
                            }
                        }
                        if (arrayList.size() >= 2) {
                            PopupButton popupButton5 = (PopupButton) arrayList.get(1);
                            if (popupButton5.textColorKey >= 0) {
                                ((TextView) this.currentDialog.getButton(-2)).setTextColor(getColor(popupButton5.textColorKey));
                            }
                        }
                        if (arrayList.size() == 3) {
                            PopupButton popupButton6 = (PopupButton) arrayList.get(2);
                            if (popupButton6.textColorKey >= 0) {
                                ((TextView) this.currentDialog.getButton(-3)).setTextColor(getColor(popupButton6.textColorKey));
                                return;
                            }
                            return;
                        }
                        return;
                    } catch (JSONException e3) {
                        FileLog.e(e3);
                        return;
                    }
                case 5:
                    try {
                        final String optString3 = new JSONObject(str2).optString("slug");
                        if (this.currentPaymentSlug != null) {
                            onInvoiceStatusUpdate(optString3, "cancelled", true);
                        } else {
                            this.currentPaymentSlug = optString3;
                            TLRPC$TL_payments_getPaymentForm tLRPC$TL_payments_getPaymentForm = new TLRPC$TL_payments_getPaymentForm();
                            final TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
                            tLRPC$TL_inputInvoiceSlug.slug = optString3;
                            tLRPC$TL_payments_getPaymentForm.invoice = tLRPC$TL_inputInvoiceSlug;
                            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda36
                                @Override // org.telegram.tgnet.RequestDelegate
                                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                    BotWebViewContainer.this.lambda$onEventReceived$12(optString3, tLRPC$TL_inputInvoiceSlug, tLObject, tLRPC$TL_error);
                                }
                            });
                        }
                        return;
                    } catch (JSONException e4) {
                        FileLog.e(e4);
                        return;
                    }
                case 6:
                    try {
                        this.delegate.onWebAppSetupClosingBehavior(new JSONObject(str2).optBoolean("need_confirmation"));
                        return;
                    } catch (JSONException e5) {
                        FileLog.e(e5);
                        return;
                    }
                case 7:
                    try {
                        if (!this.hasQRPending && this.parentActivity != null) {
                            this.lastQrText = new JSONObject(str2).optString("text");
                            this.hasQRPending = true;
                            if (Build.VERSION.SDK_INT >= 23 && this.parentActivity.checkSelfPermission("android.permission.CAMERA") != 0) {
                                NotificationCenter.getGlobalInstance().addObserver(new NotificationCenter.NotificationCenterDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer.4
                                    {
                                        BotWebViewContainer.this = this;
                                    }

                                    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
                                    public void didReceivedNotification(int i4, int i5, Object... objArr) {
                                        int i6 = NotificationCenter.onRequestPermissionResultReceived;
                                        if (i4 == i6) {
                                            int intValue = ((Integer) objArr[0]).intValue();
                                            int[] iArr = (int[]) objArr[2];
                                            if (intValue == 5000) {
                                                NotificationCenter.getGlobalInstance().removeObserver(this, i6);
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
                            openQrScanActivity();
                            return;
                        }
                        return;
                    } catch (JSONException e6) {
                        FileLog.e(e6);
                        return;
                    }
                case '\b':
                    if (ignoreDialog(4)) {
                        try {
                            JSONObject jSONObject5 = new JSONObject();
                            jSONObject5.put("status", "cancelled");
                            notifyEvent("phone_requested", jSONObject5);
                            return;
                        } catch (Exception e7) {
                            FileLog.e(e7);
                            return;
                        }
                    }
                    final String[] strArr = {"cancelled"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), this.resourcesProvider);
                    builder.setTitle(LocaleController.getString("ShareYouPhoneNumberTitle", R.string.ShareYouPhoneNumberTitle));
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                    String userName = UserObject.getUserName(this.botUser);
                    if (TextUtils.isEmpty(userName)) {
                        spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.getString(R.string.AreYouSureShareMyContactInfoBot)));
                    } else {
                        spannableStringBuilder.append((CharSequence) AndroidUtilities.replaceTags(LocaleController.formatString(R.string.AreYouSureShareMyContactInfoWebapp, userName)));
                    }
                    final boolean z2 = MessagesController.getInstance(this.currentAccount).blockePeers.indexOfKey(this.botUser.id) >= 0;
                    if (z2) {
                        spannableStringBuilder.append((CharSequence) "\n\n");
                        spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.AreYouSureShareMyContactInfoBotUnblock));
                    }
                    builder.setMessage(spannableStringBuilder);
                    builder.setPositiveButton(LocaleController.getString("ShareContact", R.string.ShareContact), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda6
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i4) {
                            BotWebViewContainer.this.lambda$onEventReceived$23(strArr, z2, dialogInterface, i4);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda8
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i4) {
                            dialogInterface.dismiss();
                        }
                    });
                    showDialog(4, builder.create(), new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda28
                        @Override // java.lang.Runnable
                        public final void run() {
                            BotWebViewContainer.this.lambda$onEventReceived$25(strArr);
                        }
                    });
                    return;
                case '\t':
                    notifyThemeChanged();
                    return;
                case '\n':
                    if (this.isRequestingPageOpen || System.currentTimeMillis() - this.lastClickMs > 1000) {
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
                case 11:
                    invalidateViewPortHeight(!((getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) && ((ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent()).isSwipeInProgress()), true);
                    return;
                case '\f':
                    try {
                        str12 = new JSONObject(str2).getString("reason");
                    } catch (Exception unused) {
                    }
                    createBiometry();
                    BotBiometry botBiometry = this.biometry;
                    if (botBiometry == null) {
                        return;
                    }
                    if (!botBiometry.access_granted) {
                        try {
                            JSONObject jSONObject6 = new JSONObject();
                            jSONObject6.put("status", "failed");
                            notifyEvent("biometry_auth_requested", jSONObject6);
                            return;
                        } catch (Exception e8) {
                            FileLog.e(e8);
                            return;
                        }
                    }
                    botBiometry.requestToken(str12, new Utilities.Callback2() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda29
                        @Override // org.telegram.messenger.Utilities.Callback2
                        public final void run(Object obj2, Object obj3) {
                            BotWebViewContainer.this.lambda$onEventReceived$31((Boolean) obj2, (String) obj3);
                        }
                    });
                    return;
                case '\r':
                    try {
                        String optString4 = new JSONObject(str2).optString("path_full");
                        if (optString4.startsWith("/")) {
                            optString4 = optString4.substring(1);
                        }
                        onOpenUri(Uri.parse("https://t.me/" + optString4), null, false, true);
                        return;
                    } catch (JSONException e9) {
                        FileLog.e(e9);
                        return;
                    }
                case 14:
                    if (this.isRequestingPageOpen || System.currentTimeMillis() - this.lastClickMs > 1000 || System.currentTimeMillis() - this.lastPostStoryMs < 2000) {
                        return;
                    }
                    this.lastClickMs = 0L;
                    this.lastPostStoryMs = System.currentTimeMillis();
                    try {
                        jSONObject = new JSONObject(str2);
                        str3 = jSONObject.optString("media_url");
                    } catch (Exception e10) {
                        e = e10;
                        str3 = null;
                        str4 = null;
                    }
                    try {
                        str4 = jSONObject.optString("text");
                        try {
                            JSONObject optJSONObject = jSONObject.optJSONObject("widget_link");
                            if (optJSONObject != null) {
                                str5 = optJSONObject.optString("url");
                                try {
                                    str10 = optJSONObject.optString("name");
                                    str14 = str5;
                                } catch (Exception e11) {
                                    e = e11;
                                    FileLog.e(e);
                                    str6 = str3;
                                    str7 = str4;
                                    str8 = str5;
                                    str9 = null;
                                    if (str6 == null) {
                                    }
                                }
                            } else {
                                str10 = null;
                            }
                            str9 = str10;
                            str6 = str3;
                            str7 = str4;
                            str8 = str14;
                        } catch (Exception e12) {
                            e = e12;
                            str5 = null;
                        }
                    } catch (Exception e13) {
                        e = e13;
                        str4 = null;
                        str5 = str4;
                        FileLog.e(e);
                        str6 = str3;
                        str7 = str4;
                        str8 = str5;
                        str9 = null;
                        if (str6 == null) {
                        }
                    }
                    if (str6 == null) {
                        return;
                    }
                    if (!MessagesController.getInstance(this.currentAccount).storiesEnabled()) {
                        new PremiumFeatureBottomSheet(new BaseFragment() { // from class: org.telegram.ui.web.BotWebViewContainer.5
                            @Override // org.telegram.ui.ActionBar.BaseFragment
                            public boolean isLightStatusBar() {
                                return false;
                            }

                            {
                                BotWebViewContainer.this = this;
                                this.currentAccount = this.currentAccount;
                            }

                            @Override // org.telegram.ui.ActionBar.BaseFragment
                            public Dialog showDialog(Dialog dialog) {
                                dialog.show();
                                return dialog;
                            }

                            @Override // org.telegram.ui.ActionBar.BaseFragment
                            public Activity getParentActivity() {
                                return BotWebViewContainer.this.parentActivity;
                            }

                            @Override // org.telegram.ui.ActionBar.BaseFragment
                            public Theme.ResourcesProvider getResourceProvider() {
                                return new WrappedResourceProvider(this, BotWebViewContainer.this.resourcesProvider) { // from class: org.telegram.ui.web.BotWebViewContainer.5.1
                                    @Override // org.telegram.ui.WrappedResourceProvider
                                    public void appendColors() {
                                        this.sparseIntArray.append(Theme.key_dialogBackground, -14803426);
                                        this.sparseIntArray.append(Theme.key_windowBackgroundGray, -16777216);
                                    }
                                };
                            }
                        }, 14, true).show();
                        return;
                    }
                    final AlertDialog alertDialog = new AlertDialog(this.parentActivity, 3);
                    new HttpGetFileTask(new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda32
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj2) {
                            BotWebViewContainer.this.lambda$onEventReceived$36(alertDialog, str7, str8, str9, (File) obj2);
                        }
                    }).execute(str6);
                    alertDialog.showDelayed(250L);
                    return;
                case 15:
                    try {
                        z = new JSONObject(str2).optBoolean("return_back");
                    } catch (Exception e14) {
                        FileLog.e(e14);
                        z = false;
                    }
                    this.delegate.onCloseRequested(null);
                    if (this.wasOpenedByLinkIntent && z && LaunchActivity.instance != null) {
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
                    return;
                case 16:
                    setPageLoaded(this.webView.getUrl(), true);
                    return;
                case 17:
                    try {
                        String string4 = new JSONObject(str2).getString("req_id");
                        if (this.delegate.isClipboardAvailable() && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                            CharSequence text = ((ClipboardManager) getContext().getSystemService("clipboard")).getText();
                            notifyEvent("clipboard_text_received", new JSONObject().put("req_id", string4).put("data", text != null ? text.toString() : ""));
                            return;
                        }
                        notifyEvent("clipboard_text_received", new JSONObject().put("req_id", string4));
                        return;
                    } catch (JSONException e15) {
                        FileLog.e(e15);
                        return;
                    }
                case 18:
                    try {
                        this.delegate.onSendWebViewData(new JSONObject(str2).optString("data"));
                        return;
                    } catch (JSONException e16) {
                        FileLog.e(e16);
                        return;
                    }
                case 19:
                    try {
                        JSONObject jSONObject7 = new JSONObject(str2);
                        ArrayList arrayList2 = new ArrayList();
                        JSONArray jSONArray2 = jSONObject7.getJSONArray("chat_types");
                        for (int i4 = 0; i4 < jSONArray2.length(); i4++) {
                            arrayList2.add(jSONArray2.getString(i4));
                        }
                        this.delegate.onWebAppSwitchInlineQuery(this.botUser, jSONObject7.getString("query"), arrayList2);
                        return;
                    } catch (JSONException e17) {
                        FileLog.e(e17);
                        return;
                    }
                case 20:
                    try {
                        boolean optBoolean = new JSONObject(str2).optBoolean("is_visible");
                        if (optBoolean != this.isBackButtonVisible) {
                            this.isBackButtonVisible = optBoolean;
                            this.delegate.onSetBackButtonVisible(optBoolean);
                            return;
                        }
                        return;
                    } catch (JSONException e18) {
                        FileLog.e(e18);
                        return;
                    }
                case 21:
                    try {
                        str11 = new JSONObject(str2).getString("reason");
                    } catch (Exception unused2) {
                    }
                    createBiometry();
                    BotBiometry botBiometry2 = this.biometry;
                    if (botBiometry2 == null) {
                        return;
                    }
                    boolean z3 = botBiometry2.access_requested;
                    if (z3) {
                        notifyBiometryReceived();
                        return;
                    } else if (!botBiometry2.access_granted) {
                        final Runnable[] runnableArr = {new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda15
                            @Override // java.lang.Runnable
                            public final void run() {
                                BotWebViewContainer.this.lambda$onEventReceived$26();
                            }
                        }};
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext(), this.resourcesProvider);
                        if (TextUtils.isEmpty(str11)) {
                            builder2.setTitle(LocaleController.getString(R.string.BotAllowBiometryTitle));
                            builder2.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.BotAllowBiometryMessage, UserObject.getUserName(this.botUser))));
                        } else {
                            builder2.setTitle(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.BotAllowBiometryMessage, UserObject.getUserName(this.botUser))));
                            builder2.setMessage(str11);
                        }
                        builder2.setPositiveButton(LocaleController.getString(R.string.Allow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda4
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i5) {
                                BotWebViewContainer.this.lambda$onEventReceived$28(runnableArr, dialogInterface, i5);
                            }
                        });
                        builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda3
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i5) {
                                BotWebViewContainer.this.lambda$onEventReceived$29(runnableArr, dialogInterface, i5);
                            }
                        });
                        builder2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda11
                            @Override // android.content.DialogInterface.OnDismissListener
                            public final void onDismiss(DialogInterface dialogInterface) {
                                BotWebViewContainer.lambda$onEventReceived$30(runnableArr, dialogInterface);
                            }
                        });
                        builder2.show();
                        return;
                    } else {
                        if (!z3) {
                            botBiometry2.access_requested = true;
                            botBiometry2.save();
                        }
                        notifyBiometryReceived();
                        return;
                    }
                case 22:
                    try {
                        JSONObject jSONObject8 = new JSONObject(str2);
                        String optString5 = jSONObject8.optString("type");
                        int hashCode = optString5.hashCode();
                        if (hashCode == -1184809658) {
                            if (optString5.equals("impact")) {
                                c3 = 0;
                                if (c3 == 0) {
                                }
                            }
                            c3 = 65535;
                            if (c3 == 0) {
                            }
                        } else if (hashCode != 193071555) {
                            if (hashCode == 595233003 && optString5.equals("notification")) {
                                c3 = 1;
                                if (c3 == 0) {
                                    if (c3 == 1) {
                                        String optString6 = jSONObject8.optString("notification_type");
                                        int hashCode2 = optString6.hashCode();
                                        if (hashCode2 == -1867169789) {
                                            if (optString6.equals("success")) {
                                                c5 = 1;
                                                if (c5 != 0) {
                                                }
                                                botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                            }
                                            c5 = 65535;
                                            if (c5 != 0) {
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        } else if (hashCode2 != 96784904) {
                                            if (hashCode2 == 1124446108 && optString6.equals("warning")) {
                                                c5 = 2;
                                                if (c5 != 0) {
                                                    botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_ERROR;
                                                } else if (c5 == 1) {
                                                    botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_SUCCESS;
                                                } else if (c5 == 2) {
                                                    botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_WARNING;
                                                }
                                                botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                            }
                                            c5 = 65535;
                                            if (c5 != 0) {
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        } else {
                                            if (optString6.equals("error")) {
                                                c5 = 0;
                                                if (c5 != 0) {
                                                }
                                                botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                            }
                                            c5 = 65535;
                                            if (c5 != 0) {
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        }
                                    } else if (c3 == 2) {
                                        botWebViewVibrationEffect2 = BotWebViewVibrationEffect.SELECTION_CHANGE;
                                    }
                                    if (botWebViewVibrationEffect2 != null) {
                                        botWebViewVibrationEffect2.vibrate();
                                        return;
                                    }
                                    return;
                                }
                                String optString7 = jSONObject8.optString("impact_style");
                                switch (optString7.hashCode()) {
                                    case -1078030475:
                                        if (optString7.equals("medium")) {
                                            c4 = 1;
                                            break;
                                        }
                                        c4 = 65535;
                                        break;
                                    case 3535914:
                                        if (optString7.equals("soft")) {
                                            c4 = 4;
                                            break;
                                        }
                                        c4 = 65535;
                                        break;
                                    case 99152071:
                                        if (optString7.equals("heavy")) {
                                            c4 = 2;
                                            break;
                                        }
                                        c4 = 65535;
                                        break;
                                    case 102970646:
                                        if (optString7.equals("light")) {
                                            c4 = 0;
                                            break;
                                        }
                                        c4 = 65535;
                                        break;
                                    case 108511787:
                                        if (optString7.equals("rigid")) {
                                            c4 = 3;
                                            break;
                                        }
                                        c4 = 65535;
                                        break;
                                    default:
                                        c4 = 65535;
                                        break;
                                }
                                if (c4 == 0) {
                                    botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_LIGHT;
                                } else if (c4 == 1) {
                                    botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_MEDIUM;
                                } else if (c4 == 2) {
                                    botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_HEAVY;
                                } else if (c4 == 3) {
                                    botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_RIGID;
                                } else if (c4 == 4) {
                                    botWebViewVibrationEffect = BotWebViewVibrationEffect.IMPACT_SOFT;
                                } else if (botWebViewVibrationEffect2 != null) {
                                }
                                botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                if (botWebViewVibrationEffect2 != null) {
                                }
                            }
                            c3 = 65535;
                            if (c3 == 0) {
                            }
                        } else {
                            if (optString5.equals("selection_change")) {
                                c3 = 2;
                                if (c3 == 0) {
                                }
                            }
                            c3 = 65535;
                            if (c3 == 0) {
                            }
                        }
                    } catch (Exception e19) {
                        FileLog.e(e19);
                        return;
                    }
                    FileLog.e(e19);
                    return;
                case 23:
                    try {
                        JSONObject jSONObject9 = new JSONObject(str2);
                        boolean optBoolean2 = jSONObject9.optBoolean("is_active", false);
                        String trim = jSONObject9.optString("text", this.lastButtonText).trim();
                        boolean z4 = jSONObject9.optBoolean("is_visible", false) && !TextUtils.isEmpty(trim);
                        int parseColor = jSONObject9.has("color") ? Color.parseColor(jSONObject9.optString("color")) : this.lastButtonColor;
                        int parseColor2 = jSONObject9.has("text_color") ? Color.parseColor(jSONObject9.optString("text_color")) : this.lastButtonTextColor;
                        boolean z5 = jSONObject9.optBoolean("is_progress_visible", false) && z4;
                        this.lastButtonColor = parseColor;
                        this.lastButtonTextColor = parseColor2;
                        this.lastButtonText = trim;
                        this.buttonData = str2;
                        this.delegate.onSetupMainButton(z4, optBoolean2, trim, parseColor, parseColor2, z5);
                        return;
                    } catch (IllegalArgumentException | JSONException e20) {
                        FileLog.e(e20);
                        return;
                    }
                case 24:
                    try {
                        this.delegate.onWebAppSwipingBehavior(new JSONObject(str2).optBoolean("allow_vertical_swipe"));
                        return;
                    } catch (JSONException e21) {
                        FileLog.e(e21);
                        return;
                    }
                case 25:
                    try {
                        boolean optBoolean3 = new JSONObject(str2).optBoolean("is_visible");
                        if (optBoolean3 != this.isSettingsButtonVisible) {
                            this.isSettingsButtonVisible = optBoolean3;
                            this.delegate.onSetSettingsButtonVisible(optBoolean3);
                            return;
                        }
                        return;
                    } catch (JSONException e22) {
                        FileLog.e(e22);
                        return;
                    }
                case 26:
                    try {
                        JSONObject jSONObject10 = new JSONObject(str2);
                        final String string5 = jSONObject10.getString("token");
                        try {
                            str13 = jSONObject10.getString("reason");
                        } catch (Exception unused3) {
                        }
                        createBiometry();
                        BotBiometry botBiometry3 = this.biometry;
                        if (botBiometry3 == null) {
                            return;
                        }
                        if (!botBiometry3.access_granted) {
                            try {
                                JSONObject jSONObject11 = new JSONObject();
                                jSONObject11.put("status", "failed");
                                notifyEvent("biometry_token_updated", jSONObject11);
                                return;
                            } catch (Exception e23) {
                                FileLog.e(e23);
                                return;
                            }
                        }
                        botBiometry3.updateToken(str13, string5, new Utilities.Callback() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda31
                            @Override // org.telegram.messenger.Utilities.Callback
                            public final void run(Object obj2) {
                                BotWebViewContainer.this.lambda$onEventReceived$32(string5, (Boolean) obj2);
                            }
                        });
                        return;
                    } catch (Exception e24) {
                        FileLog.e(e24);
                        if (e24 instanceof JSONException) {
                            error("JSON Parse error");
                            return;
                        } else {
                            unknownError();
                            return;
                        }
                    }
                case 27:
                    try {
                        JSONObject jSONObject12 = new JSONObject(str2);
                        String optString8 = jSONObject12.optString("color", null);
                        if (!TextUtils.isEmpty(optString8)) {
                            int parseColor3 = Color.parseColor(optString8);
                            if (parseColor3 != 0) {
                                this.delegate.onWebAppSetActionBarColor(-1, parseColor3, true);
                                return;
                            }
                            return;
                        }
                        String optString9 = jSONObject12.optString("color_key");
                        int hashCode3 = optString9.hashCode();
                        if (hashCode3 != -1265068311) {
                            if (hashCode3 == -210781868 && optString9.equals("secondary_bg_color")) {
                                c2 = 1;
                                if (c2 != 0) {
                                    i = Theme.key_windowBackgroundWhite;
                                } else {
                                    i = c2 != 1 ? -1 : Theme.key_windowBackgroundGray;
                                }
                                if (i < 0) {
                                    this.delegate.onWebAppSetActionBarColor(i, Theme.getColor(i, this.resourcesProvider), false);
                                    return;
                                }
                                return;
                            }
                            c2 = 65535;
                            if (c2 != 0) {
                            }
                            if (i < 0) {
                            }
                        } else {
                            if (optString9.equals("bg_color")) {
                                c2 = 0;
                                if (c2 != 0) {
                                }
                                if (i < 0) {
                                }
                            }
                            c2 = 65535;
                            if (c2 != 0) {
                            }
                            if (i < 0) {
                            }
                        }
                    } catch (JSONException e25) {
                        FileLog.e(e25);
                        return;
                    }
                    break;
                case 28:
                    try {
                        this.delegate.onWebAppSetBackgroundColor(Color.parseColor(new JSONObject(str2).optString("color", "#ffffff")) | (-16777216));
                        return;
                    } catch (IllegalArgumentException | JSONException e26) {
                        FileLog.e(e26);
                        return;
                    }
                case 29:
                    if (ignoreDialog(3)) {
                        try {
                            JSONObject jSONObject13 = new JSONObject();
                            jSONObject13.put("status", "cancelled");
                            notifyEvent("write_access_requested", jSONObject13);
                            return;
                        } catch (Exception e27) {
                            FileLog.e(e27);
                            return;
                        }
                    }
                    TL_bots$canSendMessage tL_bots$canSendMessage = new TL_bots$canSendMessage();
                    tL_bots$canSendMessage.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botUser);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_bots$canSendMessage, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda34
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            BotWebViewContainer.this.lambda$onEventReceived$19(tLObject, tLRPC$TL_error);
                        }
                    });
                    return;
                case 30:
                    this.delegate.onWebAppExpand();
                    return;
                default:
                    FileLog.d("unknown webapp event " + str);
                    return;
            }
        }
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

    public /* synthetic */ void lambda$onEventReceived$10(AtomicBoolean atomicBoolean, DialogInterface dialogInterface) {
        if (!atomicBoolean.get()) {
            notifyEvent("popup_closed", new JSONObject());
        }
        this.currentDialog = null;
        this.lastDialogClosed = System.currentTimeMillis();
    }

    public /* synthetic */ void lambda$onEventReceived$12(final String str, final TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$11(tLRPC$TL_error, str, tLRPC$TL_inputInvoiceSlug, tLObject);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$11(TLRPC$TL_error tLRPC$TL_error, String str, TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug, TLObject tLObject) {
        if (tLRPC$TL_error != null) {
            onInvoiceStatusUpdate(str, "failed");
        } else {
            this.delegate.onWebAppOpenInvoice(tLRPC$TL_inputInvoiceSlug, str, tLObject);
        }
    }

    public /* synthetic */ void lambda$onEventReceived$19(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$18(tLObject, tLRPC$TL_error);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$18(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (!(tLObject instanceof TLRPC$TL_boolTrue)) {
            if (tLRPC$TL_error != null) {
                unknownError(tLRPC$TL_error.text);
                return;
            }
            final String[] strArr = {"cancelled"};
            showDialog(3, new AlertDialog.Builder(getContext()).setTitle(LocaleController.getString(R.string.BotWebViewRequestWriteTitle)).setMessage(LocaleController.getString(R.string.BotWebViewRequestWriteMessage)).setPositiveButton(LocaleController.getString(R.string.BotWebViewRequestAllow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    BotWebViewContainer.this.lambda$onEventReceived$15(strArr, dialogInterface, i);
                }
            }).setNegativeButton(LocaleController.getString(R.string.BotWebViewRequestDontAllow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create(), new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.this.lambda$onEventReceived$17(strArr);
                }
            });
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", "allowed");
            notifyEvent("write_access_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$onEventReceived$15(final String[] strArr, final DialogInterface dialogInterface, int i) {
        TL_bots$allowSendMessage tL_bots$allowSendMessage = new TL_bots$allowSendMessage();
        tL_bots$allowSendMessage.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botUser);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_bots$allowSendMessage, new RequestDelegate() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda37
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BotWebViewContainer.this.lambda$onEventReceived$14(strArr, dialogInterface, tLObject, tLRPC$TL_error);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$14(final String[] strArr, final DialogInterface dialogInterface, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$13(tLObject, strArr, tLRPC$TL_error, dialogInterface);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$13(TLObject tLObject, String[] strArr, TLRPC$TL_error tLRPC$TL_error, DialogInterface dialogInterface) {
        if (tLObject != null) {
            strArr[0] = "allowed";
            if (tLObject instanceof TLRPC$Updates) {
                MessagesController.getInstance(this.currentAccount).processUpdates((TLRPC$Updates) tLObject, false);
            }
        }
        if (tLRPC$TL_error != null) {
            unknownError(tLRPC$TL_error.text);
        }
        dialogInterface.dismiss();
    }

    public /* synthetic */ void lambda$onEventReceived$17(String[] strArr) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", strArr[0]);
            notifyEvent("write_access_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$onEventReceived$21(final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$20(str, tLObject, tLRPC$TL_error);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$20(String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("req_id", str);
            if (tLObject instanceof TLRPC$TL_dataJSON) {
                jSONObject.put("result", new JSONTokener(((TLRPC$TL_dataJSON) tLObject).data).nextValue());
            } else if (tLRPC$TL_error != null) {
                jSONObject.put("error", tLRPC$TL_error.text);
            }
            notifyEvent("custom_method_invoked", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
            unknownError();
        }
    }

    public /* synthetic */ void lambda$onEventReceived$23(String[] strArr, boolean z, DialogInterface dialogInterface, int i) {
        strArr[0] = null;
        dialogInterface.dismiss();
        if (z) {
            MessagesController.getInstance(this.currentAccount).unblockPeer(this.botUser.id, new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.this.lambda$onEventReceived$22();
                }
            });
            return;
        }
        SendMessagesHelper.getInstance(this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(UserConfig.getInstance(this.currentAccount).getCurrentUser(), this.botUser.id, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, true, 0));
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", "sent");
            notifyEvent("phone_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$onEventReceived$22() {
        SendMessagesHelper.getInstance(this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(UserConfig.getInstance(this.currentAccount).getCurrentUser(), this.botUser.id, (MessageObject) null, (MessageObject) null, (TLRPC$ReplyMarkup) null, (HashMap<String, String>) null, true, 0));
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", "sent");
            notifyEvent("phone_requested", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public /* synthetic */ void lambda$onEventReceived$25(String[] strArr) {
        if (strArr[0] == null) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("status", strArr[0]);
            notifyEvent("phone_requested", jSONObject);
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

    public /* synthetic */ void lambda$onEventReceived$28(Runnable[] runnableArr, DialogInterface dialogInterface, int i) {
        if (runnableArr[0] != null) {
            runnableArr[0] = null;
        }
        BotBiometry botBiometry = this.biometry;
        botBiometry.access_requested = true;
        botBiometry.save();
        this.biometry.requestToken(null, new Utilities.Callback2() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda30
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                BotWebViewContainer.this.lambda$onEventReceived$27((Boolean) obj, (String) obj2);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$27(Boolean bool, String str) {
        if (bool.booleanValue()) {
            BotBiometry botBiometry = this.biometry;
            botBiometry.access_granted = true;
            botBiometry.save();
        }
        notifyBiometryReceived();
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
        if (runnableArr[0] != null) {
            runnableArr[0].run();
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

    public /* synthetic */ void lambda$onEventReceived$36(final AlertDialog alertDialog, final String str, final String str2, final String str3, final File file) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$35(file, alertDialog, str, str2, str3);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$35(final File file, final AlertDialog alertDialog, final String str, final String str2, final String str3) {
        if (file == null) {
            alertDialog.dismissUnless(500L);
            return;
        }
        final int[] iArr = new int[11];
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$33(iArr, file, alertDialog, str, str2, str3);
            }
        };
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.lambda$onEventReceived$34(file, iArr, runnable);
            }
        });
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
                fromPhotoShoot.mediaEntities = new ArrayList<>();
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

    private void error(String str) {
        BulletinFactory.of(this, this.resourcesProvider).createSimpleBulletin(R.raw.error, str).show();
    }

    private boolean ignoreDialog(int i) {
        if (this.currentDialog != null) {
            return true;
        }
        if (this.blockedDialogsUntil <= 0 || System.currentTimeMillis() >= this.blockedDialogsUntil) {
            if (this.lastDialogType != i || this.shownDialogsCount <= 3) {
                return false;
            }
            this.blockedDialogsUntil = System.currentTimeMillis() + 3000;
            this.shownDialogsCount = 0;
            return true;
        }
        return true;
    }

    private boolean showDialog(int i, AlertDialog alertDialog, final Runnable runnable) {
        if (alertDialog == null || ignoreDialog(i)) {
            return false;
        }
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$$ExternalSyntheticLambda9
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

    public /* synthetic */ void lambda$showDialog$37(Runnable runnable, DialogInterface dialogInterface) {
        if (runnable != null) {
            runnable.run();
        }
        this.currentDialog = null;
    }

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
            public /* synthetic */ boolean processQr(String str, Runnable runnable) {
                return CameraScanActivity.CameraScanActivityDelegate.-CC.$default$processQr(this, str, runnable);
            }

            {
                BotWebViewContainer.this = this;
            }

            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public void didFindQr(String str) {
                try {
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
        });
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

    private int getColor(int i) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        if (resourcesProvider != null) {
            return resourcesProvider.getColor(i);
        }
        return Theme.getColor(i);
    }

    /* loaded from: classes.dex */
    public static class BotWebViewProxy {
        public BotWebViewContainer container;

        public BotWebViewProxy(BotWebViewContainer botWebViewContainer) {
            this.container = botWebViewContainer;
        }

        public void setContainer(BotWebViewContainer botWebViewContainer) {
            this.container = botWebViewContainer;
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

        public /* synthetic */ void lambda$postEvent$0(String str, String str2) {
            BotWebViewContainer botWebViewContainer = this.container;
            if (botWebViewContainer == null) {
                return;
            }
            botWebViewContainer.onEventReceived(str, str2);
        }
    }

    /* loaded from: classes.dex */
    public static class WebViewProxy {
        public BotWebViewContainer container;

        public WebViewProxy(BotWebViewContainer botWebViewContainer) {
            this.container = botWebViewContainer;
        }

        public void setContainer(BotWebViewContainer botWebViewContainer) {
            this.container = botWebViewContainer;
        }

        @JavascriptInterface
        public void post(final String str, final String str2) {
            if (this.container == null) {
                return;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.WebViewProxy.this.lambda$post$0(str, str2);
                }
            });
        }

        public /* synthetic */ void lambda$post$0(String str, String str2) {
            BotWebViewContainer botWebViewContainer = this.container;
            if (botWebViewContainer == null) {
                return;
            }
            botWebViewContainer.onWebEventReceived(str, str2);
        }

        @JavascriptInterface
        public void resolveBlob(String str, final byte[] bArr, final String str2) {
            if (this.container == null) {
                return;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.WebViewProxy.this.lambda$resolveBlob$2(str2, bArr);
                }
            });
        }

        /* JADX WARN: Code restructure failed: missing block: B:35:0x0045, code lost:
            if (r1.exists() != false) goto L9;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$resolveBlob$2(String str, byte[] bArr) {
            String str2;
            File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(str);
            File file = null;
            while (file == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("file");
                if (TextUtils.isEmpty(extensionFromMimeType)) {
                    str2 = "";
                } else {
                    str2 = "." + extensionFromMimeType;
                }
                sb.append(str2);
                file = new File(externalStoragePublicDirectory, sb.toString());
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bArr);
                fileOutputStream.close();
            } catch (Exception e) {
                FileLog.e(e);
            }
            BotWebViewContainer botWebViewContainer = this.container;
            BulletinFactory.of(botWebViewContainer, botWebViewContainer.resourcesProvider).createSimpleBulletin(R.raw.ic_download, AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.FileSavedHintLinked), new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.WebViewProxy.lambda$resolveBlob$1();
                }
            })).show(true);
        }

        public static /* synthetic */ void lambda$resolveBlob$1() {
            LaunchActivity launchActivity = LaunchActivity.instance;
            if (launchActivity == null || launchActivity.isFinishing()) {
                return;
            }
            Intent intent = new Intent("android.intent.action.VIEW_DOWNLOADS");
            intent.setFlags(268468224);
            LaunchActivity.instance.startActivity(intent);
        }
    }

    /* loaded from: classes.dex */
    public interface Delegate {
        boolean isClipboardAvailable();

        void onCloseRequested(Runnable runnable);

        void onCloseToTabs();

        void onInstantClose();

        void onSendWebViewData(String str);

        void onSetBackButtonVisible(boolean z);

        void onSetSettingsButtonVisible(boolean z);

        void onSetupMainButton(boolean z, boolean z2, String str, int i, int i2, boolean z3);

        void onWebAppBackgroundChanged(boolean z, int i);

        void onWebAppExpand();

        void onWebAppOpenInvoice(TLRPC$InputInvoice tLRPC$InputInvoice, String str, TLObject tLObject);

        void onWebAppReady();

        void onWebAppSetActionBarColor(int i, int i2, boolean z);

        void onWebAppSetBackgroundColor(int i);

        void onWebAppSetupClosingBehavior(boolean z);

        void onWebAppSwipingBehavior(boolean z);

        void onWebAppSwitchInlineQuery(TLRPC$User tLRPC$User, String str, List<String> list);

        /* loaded from: classes.dex */
        public final /* synthetic */ class -CC {
            public static boolean $default$isClipboardAvailable(Delegate delegate) {
                return false;
            }

            public static void $default$onSendWebViewData(Delegate delegate, String str) {
            }

            public static void $default$onWebAppBackgroundChanged(Delegate delegate, boolean z, int i) {
            }

            public static void $default$onWebAppReady(Delegate delegate) {
            }

            public static void $default$onInstantClose(Delegate _this) {
                _this.onCloseRequested(null);
            }

            public static void $default$onCloseToTabs(Delegate _this) {
                _this.onCloseRequested(null);
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class PopupButton {
        public String id;
        public String text;
        public int textColorKey;

        /* JADX WARN: Removed duplicated region for block: B:65:0x007e  */
        /* JADX WARN: Removed duplicated region for block: B:67:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public PopupButton(JSONObject jSONObject) throws JSONException {
            char c = 65535;
            this.textColorKey = -1;
            this.id = jSONObject.getString("id");
            String string = jSONObject.getString("type");
            boolean z = true;
            switch (string.hashCode()) {
                case -1829997182:
                    if (string.equals("destructive")) {
                        c = 5;
                        break;
                    }
                    break;
                case -1367724422:
                    if (string.equals("cancel")) {
                        c = 4;
                        break;
                    }
                    break;
                case 3548:
                    if (string.equals("ok")) {
                        c = 2;
                        break;
                    }
                    break;
                case 94756344:
                    if (string.equals("close")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1544803905:
                    if (string.equals("default")) {
                        c = 1;
                        break;
                    }
                    break;
            }
            if (c == 2) {
                this.text = LocaleController.getString(R.string.OK);
            } else if (c == 3) {
                this.text = LocaleController.getString(R.string.Close);
            } else if (c != 4) {
                if (c == 5) {
                    this.textColorKey = Theme.key_text_RedBold;
                }
                if (z) {
                    return;
                }
                this.text = jSONObject.getString("text");
                return;
            } else {
                this.text = LocaleController.getString(R.string.Cancel);
            }
            z = false;
            if (z) {
            }
        }
    }

    public static boolean isTonsite(String str) {
        return str != null && isTonsite(Uri.parse(str));
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

    public static WebResourceResponse proxyTON(WebResourceRequest webResourceRequest) {
        if (Build.VERSION.SDK_INT >= 21) {
            return proxyTON(webResourceRequest.getMethod(), webResourceRequest.getUrl().toString(), webResourceRequest.getRequestHeaders());
        }
        return null;
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

    public static WebResourceResponse proxyTON(String str, String str2, Map<String, String> map) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Browser.replaceHostname(Uri.parse(str2), rotateTONHost(AndroidUtilities.getHostAuthority(str2)), "https")).openConnection();
            httpURLConnection.setRequestMethod(str);
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    httpURLConnection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            httpURLConnection.connect();
            return new WebResourceResponse(httpURLConnection.getContentType().split(";", 2)[0], httpURLConnection.getContentEncoding(), httpURLConnection.getInputStream());
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static class MyWebView extends WebView {
        public final boolean bot;
        private BotWebViewContainer botWebViewContainer;
        private BrowserHistory.Entry currentHistoryEntry;
        private String currentUrl;
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
        private HashMap<String, Bitmap> lastFavicons;
        public String lastSiteName;
        public String lastTitle;
        public boolean lastTitleGot;
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
        private WebViewScrollListener webViewScrollListener;
        private Runnable whenPageLoaded;

        public static /* synthetic */ void lambda$evaluateJS$1(String str) {
        }

        public boolean isPageLoaded() {
            return this.isPageLoaded;
        }

        public void d(String str) {
            FileLog.d("[webview] #" + this.tag + " " + str);
        }

        public MyWebView(Context context, boolean z) {
            super(context);
            this.tag = BotWebViewContainer.access$1408();
            this.lastFavicons = new HashMap<>();
            this.bot = z;
            d("created new webview " + this);
            setOnLongClickListener(new 1());
            setWebViewClient(new 2(z, context));
            setWebChromeClient(new 3(z));
            setFindListener(new WebView.FindListener() { // from class: org.telegram.ui.web.BotWebViewContainer.MyWebView.4
                {
                    MyWebView.this = this;
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

        /* loaded from: classes.dex */
        public class 1 implements View.OnLongClickListener {
            1() {
                MyWebView.this = r1;
            }

            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view) {
                String str;
                WebView.HitTestResult hitTestResult = MyWebView.this.getHitTestResult();
                if (hitTestResult.getType() == 7) {
                    final String extra = hitTestResult.getExtra();
                    BottomSheet.Builder builder = new BottomSheet.Builder(MyWebView.this.getContext(), false, null);
                    try {
                        Uri parse = Uri.parse(extra);
                        str = Browser.replaceHostname(parse, IDN.toUnicode(parse.getHost(), 1), null);
                    } catch (Exception e) {
                        try {
                            FileLog.e((Throwable) e, false);
                            str = extra;
                        } catch (Exception e2) {
                            e = e2;
                            str = extra;
                            FileLog.e(e);
                            builder.setTitleMultipleLines(true);
                            builder.setTitle(str);
                            builder.setItems(new CharSequence[]{LocaleController.getString(R.string.OpenInTelegramBrowser), LocaleController.getString(R.string.OpenInSystemBrowser), LocaleController.getString(R.string.Copy)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda0
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i) {
                                    BotWebViewContainer.MyWebView.1.this.lambda$onLongClick$0(extra, dialogInterface, i);
                                }
                            });
                            builder.show();
                            return true;
                        }
                    }
                    try {
                        str = URLDecoder.decode(str.replaceAll("\\+", "%2b"), "UTF-8");
                    } catch (Exception e3) {
                        e = e3;
                        FileLog.e(e);
                        builder.setTitleMultipleLines(true);
                        builder.setTitle(str);
                        builder.setItems(new CharSequence[]{LocaleController.getString(R.string.OpenInTelegramBrowser), LocaleController.getString(R.string.OpenInSystemBrowser), LocaleController.getString(R.string.Copy)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda0
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                BotWebViewContainer.MyWebView.1.this.lambda$onLongClick$0(extra, dialogInterface, i);
                            }
                        });
                        builder.show();
                        return true;
                    }
                    builder.setTitleMultipleLines(true);
                    builder.setTitle(str);
                    builder.setItems(new CharSequence[]{LocaleController.getString(R.string.OpenInTelegramBrowser), LocaleController.getString(R.string.OpenInSystemBrowser), LocaleController.getString(R.string.Copy)}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            BotWebViewContainer.MyWebView.1.this.lambda$onLongClick$0(extra, dialogInterface, i);
                        }
                    });
                    builder.show();
                    return true;
                }
                return false;
            }

            public /* synthetic */ void lambda$onLongClick$0(String str, DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    MyWebView.this.loadUrl(str);
                } else if (i == 1) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                    intent.putExtra("create_new_tab", true);
                    intent.putExtra("com.android.browser.application_id", MyWebView.this.getContext().getPackageName());
                    MyWebView.this.getContext().startActivity(intent);
                } else if (i == 2) {
                    AndroidUtilities.addToClipboard(str);
                    if (MyWebView.this.botWebViewContainer != null) {
                        MyWebView.this.botWebViewContainer.showLinkCopiedBulletin();
                    }
                }
            }
        }

        /* loaded from: classes.dex */
        public class 2 extends WebViewClient {
            private final Runnable resetErrorRunnable = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.MyWebView.2.this.lambda$$2();
                }
            };
            final /* synthetic */ boolean val$bot;
            final /* synthetic */ Context val$context;

            2(boolean z, Context context) {
                MyWebView.this = r1;
                this.val$bot = z;
                this.val$context = context;
            }

            @Override // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                if (Build.VERSION.SDK_INT >= 21) {
                    MyWebView myWebView = MyWebView.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("shouldInterceptRequest ");
                    sb.append(webResourceRequest == null ? null : webResourceRequest.getUrl());
                    myWebView.d(sb.toString());
                    if (webResourceRequest != null && BotWebViewContainer.isTonsite(webResourceRequest.getUrl())) {
                        MyWebView.this.d("proxying ton");
                        return BotWebViewContainer.proxyTON(webResourceRequest);
                    }
                }
                return super.shouldInterceptRequest(webView, webResourceRequest);
            }

            @Override // android.webkit.WebViewClient
            public void onPageCommitVisible(WebView webView, String str) {
                if (MyWebView.this.whenPageLoaded != null) {
                    Runnable runnable = MyWebView.this.whenPageLoaded;
                    MyWebView.this.whenPageLoaded = null;
                    runnable.run();
                }
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onPageCommitVisible " + str);
                if (!this.val$bot) {
                    MyWebView myWebView2 = MyWebView.this;
                    myWebView2.injectedJS = true;
                    String readRes = RLottieDrawable.readRes(null, R.raw.webview_ext);
                    myWebView2.evaluateJS(readRes.replace("$DEBUG$", "" + BuildVars.DEBUG_VERSION));
                }
                super.onPageCommitVisible(webView, str);
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
                MyWebView myWebView = MyWebView.this;
                myWebView.d("doUpdateVisitedHistory " + str + " " + z);
                if (MyWebView.this.botWebViewContainer != null) {
                    MyWebView.this.botWebViewContainer.onURLChanged(str, !MyWebView.this.canGoBack(), !MyWebView.this.canGoForward());
                }
                super.doUpdateVisitedHistory(webView, str, z);
            }

            @Override // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
                MyWebView myWebView = MyWebView.this;
                myWebView.d("shouldInterceptRequest " + str);
                if (BotWebViewContainer.isTonsite(str)) {
                    MyWebView.this.d("proxying ton");
                    return BotWebViewContainer.proxyTON("GET", str, null);
                }
                return super.shouldInterceptRequest(webView, str);
            }

            @Override // android.webkit.WebViewClient
            public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
                if (Build.VERSION.SDK_INT >= 26) {
                    MyWebView myWebView = MyWebView.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onRenderProcessGone priority=");
                    sb.append(renderProcessGoneDetail == null ? null : Integer.valueOf(renderProcessGoneDetail.rendererPriorityAtExit()));
                    sb.append(" didCrash=");
                    sb.append(renderProcessGoneDetail == null ? null : Boolean.valueOf(renderProcessGoneDetail.didCrash()));
                    myWebView.d(sb.toString());
                } else {
                    MyWebView.this.d("onRenderProcessGone");
                }
                if (AndroidUtilities.isSafeToShow(MyWebView.this.getContext())) {
                    new AlertDialog.Builder(MyWebView.this.getContext(), MyWebView.this.botWebViewContainer == null ? null : MyWebView.this.botWebViewContainer.resourcesProvider).setTitle(LocaleController.getString(R.string.ChromeCrashTitle)).setMessage(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.ChromeCrashMessage), new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            BotWebViewContainer.MyWebView.2.this.lambda$onRenderProcessGone$0();
                        }
                    })).setPositiveButton(LocaleController.getString(R.string.OK), null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            BotWebViewContainer.MyWebView.2.this.lambda$onRenderProcessGone$1(dialogInterface);
                        }
                    }).show();
                    return true;
                }
                return true;
            }

            public /* synthetic */ void lambda$onRenderProcessGone$0() {
                Browser.openUrl(MyWebView.this.getContext(), "https://play.google.com/store/apps/details?id=com.google.android.webview");
            }

            public /* synthetic */ void lambda$onRenderProcessGone$1(DialogInterface dialogInterface) {
                if (MyWebView.this.botWebViewContainer == null || MyWebView.this.botWebViewContainer.delegate == null) {
                    return;
                }
                MyWebView.this.botWebViewContainer.delegate.onCloseRequested(null);
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                Uri parse = Uri.parse(str);
                if (!this.val$bot && Browser.openInExternalApp(this.val$context, str, true)) {
                    MyWebView myWebView = MyWebView.this;
                    myWebView.d("shouldOverrideUrlLoading(" + str + ") = true (openInExternalBrowser)");
                    if (!MyWebView.this.isPageLoaded && !MyWebView.this.canGoBack()) {
                        if (MyWebView.this.botWebViewContainer.delegate != null) {
                            MyWebView.this.botWebViewContainer.delegate.onInstantClose();
                        } else if (MyWebView.this.onCloseListener != null) {
                            MyWebView.this.onCloseListener.run();
                            MyWebView.this.onCloseListener = null;
                        }
                    }
                    return true;
                } else if (this.val$bot || parse == null || parse.getScheme() == null || "https".equals(parse.getScheme()) || "http".equals(parse.getScheme()) || "tonsite".equals(parse.getScheme())) {
                    if (MyWebView.this.botWebViewContainer != null && Browser.isInternalUri(parse, null)) {
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
                        MyWebView myWebView3 = MyWebView.this;
                        myWebView3.d("shouldOverrideUrlLoading(" + str + ") = true");
                        return true;
                    }
                    if (parse != null) {
                        MyWebView.this.currentUrl = parse.toString();
                    }
                    MyWebView myWebView4 = MyWebView.this;
                    myWebView4.d("shouldOverrideUrlLoading(" + str + ") = false");
                    return false;
                } else {
                    MyWebView myWebView5 = MyWebView.this;
                    myWebView5.d("shouldOverrideUrlLoading(" + str + ") = true (browser open)");
                    Browser.openUrl(MyWebView.this.getContext(), parse);
                    return true;
                }
            }

            public /* synthetic */ void lambda$$2() {
                if (MyWebView.this.botWebViewContainer != null) {
                    BotWebViewContainer botWebViewContainer = MyWebView.this.botWebViewContainer;
                    MyWebView.this.errorShown = false;
                    botWebViewContainer.onErrorShown(false, 0, null);
                }
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                String str2;
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
                    MyWebView.this.botWebViewContainer.onURLChanged(str, !MyWebView.this.canGoBack(), !MyWebView.this.canGoForward());
                }
                super.onPageStarted(webView, str, bitmap);
                MyWebView.this.injectedJS = false;
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                boolean z;
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
                if (!this.val$bot) {
                    MyWebView myWebView = MyWebView.this;
                    myWebView.injectedJS = true;
                    String readRes = RLottieDrawable.readRes(null, R.raw.webview_ext);
                    myWebView.evaluateJS(readRes.replace("$DEBUG$", "" + BuildVars.DEBUG_VERSION));
                }
                MyWebView.this.saveHistory();
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                if (Build.VERSION.SDK_INT >= 23) {
                    MyWebView myWebView = MyWebView.this;
                    myWebView.d("onReceivedError: " + webResourceError.getErrorCode() + " " + ((Object) webResourceError.getDescription()));
                    if (MyWebView.this.botWebViewContainer != null && (webResourceRequest == null || webResourceRequest.isForMainFrame())) {
                        AndroidUtilities.cancelRunOnUIThread(this.resetErrorRunnable);
                        MyWebView myWebView2 = MyWebView.this;
                        myWebView2.lastSiteName = null;
                        myWebView2.lastActionBarColorGot = false;
                        myWebView2.lastBackgroundColorGot = false;
                        myWebView2.lastFaviconGot = false;
                        myWebView2.lastTitleGot = false;
                        myWebView2.errorShownAt = (webResourceRequest == null || webResourceRequest.getUrl() == null) ? MyWebView.this.getUrl() : webResourceRequest.getUrl().toString();
                        BotWebViewContainer botWebViewContainer = MyWebView.this.botWebViewContainer;
                        MyWebView.this.lastTitle = null;
                        botWebViewContainer.onTitleChanged(null);
                        BotWebViewContainer botWebViewContainer2 = MyWebView.this.botWebViewContainer;
                        MyWebView.this.lastFavicon = null;
                        botWebViewContainer2.onFaviconChanged(null);
                        BotWebViewContainer botWebViewContainer3 = MyWebView.this.botWebViewContainer;
                        MyWebView.this.errorShown = true;
                        botWebViewContainer3.onErrorShown(true, webResourceError.getErrorCode(), webResourceError.getDescription() != null ? webResourceError.getDescription().toString() : null);
                    }
                }
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, int i, String str, String str2) {
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onReceivedError: " + i + " " + str + " url=" + str2);
                if (Build.VERSION.SDK_INT < 23 && MyWebView.this.botWebViewContainer != null) {
                    AndroidUtilities.cancelRunOnUIThread(this.resetErrorRunnable);
                    MyWebView myWebView2 = MyWebView.this;
                    myWebView2.lastSiteName = null;
                    myWebView2.lastActionBarColorGot = false;
                    myWebView2.lastBackgroundColorGot = false;
                    myWebView2.lastFaviconGot = false;
                    myWebView2.lastTitleGot = false;
                    myWebView2.errorShownAt = myWebView2.getUrl();
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

            @Override // android.webkit.WebViewClient
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
                if (Build.VERSION.SDK_INT >= 21) {
                    MyWebView myWebView = MyWebView.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onReceivedHttpError: statusCode=");
                    sb.append(webResourceResponse == null ? null : Integer.valueOf(webResourceResponse.getStatusCode()));
                    sb.append(" request=");
                    sb.append(webResourceRequest == null ? null : webResourceRequest.getUrl());
                    myWebView.d(sb.toString());
                    if (MyWebView.this.botWebViewContainer != null) {
                        if ((webResourceRequest == null || webResourceRequest.isForMainFrame()) && webResourceResponse != null && TextUtils.isEmpty(webResourceResponse.getMimeType())) {
                            AndroidUtilities.cancelRunOnUIThread(this.resetErrorRunnable);
                            MyWebView myWebView2 = MyWebView.this;
                            myWebView2.lastSiteName = null;
                            myWebView2.lastActionBarColorGot = false;
                            myWebView2.lastBackgroundColorGot = false;
                            myWebView2.lastFaviconGot = false;
                            myWebView2.lastTitleGot = false;
                            myWebView2.errorShownAt = (webResourceRequest == null || webResourceRequest.getUrl() == null) ? MyWebView.this.getUrl() : webResourceRequest.getUrl().toString();
                            BotWebViewContainer botWebViewContainer = MyWebView.this.botWebViewContainer;
                            MyWebView.this.lastTitle = null;
                            botWebViewContainer.onTitleChanged(null);
                            BotWebViewContainer botWebViewContainer2 = MyWebView.this.botWebViewContainer;
                            MyWebView.this.lastFavicon = null;
                            botWebViewContainer2.onFaviconChanged(null);
                            BotWebViewContainer botWebViewContainer3 = MyWebView.this.botWebViewContainer;
                            MyWebView.this.errorShown = true;
                            botWebViewContainer3.onErrorShown(true, webResourceResponse.getStatusCode(), webResourceResponse.getReasonPhrase());
                        }
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
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }
        }

        /* loaded from: classes.dex */
        public class 3 extends WebChromeClient {
            private Dialog lastPermissionsDialog;
            final /* synthetic */ boolean val$bot;

            3(boolean z) {
                MyWebView.this = r1;
                this.val$bot = z;
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
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onReceivedTitle title=" + str);
                MyWebView myWebView2 = MyWebView.this;
                if (!myWebView2.errorShown) {
                    myWebView2.lastTitleGot = true;
                    myWebView2.lastTitle = str;
                }
                if (myWebView2.botWebViewContainer != null) {
                    MyWebView.this.botWebViewContainer.onTitleChanged(str);
                }
                super.onReceivedTitle(webView, str);
            }

            @Override // android.webkit.WebChromeClient
            public void onReceivedTouchIconUrl(WebView webView, String str, boolean z) {
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onReceivedTouchIconUrl url=" + str + " precomposed=" + z);
                super.onReceivedTouchIconUrl(webView, str, z);
            }

            @Override // android.webkit.WebChromeClient
            public boolean onCreateWindow(WebView webView, boolean z, boolean z2, Message message) {
                BaseFragment safeLastFragment;
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onCreateWindow isDialog=" + z + " isUserGesture=" + z2 + " resultMsg=" + message);
                if (SharedConfig.inappBrowser) {
                    if (MyWebView.this.botWebViewContainer == null || (safeLastFragment = LaunchActivity.getSafeLastFragment()) == null) {
                        return false;
                    }
                    if (safeLastFragment.getParentLayout() instanceof ActionBarLayout) {
                        safeLastFragment = ((ActionBarLayout) safeLastFragment.getParentLayout()).getSheetFragment();
                    }
                    ArticleViewer createArticleViewer = safeLastFragment.createArticleViewer(true);
                    createArticleViewer.open((String) null);
                    MyWebView lastWebView = createArticleViewer.getLastWebView();
                    MyWebView myWebView2 = MyWebView.this;
                    lastWebView.opener = myWebView2;
                    myWebView2.d("onCreateWindow: newWebView=" + lastWebView);
                    ((WebView.WebViewTransport) message.obj).setWebView(lastWebView);
                    message.sendToTarget();
                    return true;
                }
                WebView webView2 = new WebView(webView.getContext());
                webView2.setWebViewClient(new 1());
                ((WebView.WebViewTransport) message.obj).setWebView(webView2);
                message.sendToTarget();
                return true;
            }

            /* loaded from: classes.dex */
            public class 1 extends WebViewClient {
                1() {
                    3.this = r1;
                }

                @Override // android.webkit.WebViewClient
                public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        MyWebView myWebView = MyWebView.this;
                        StringBuilder sb = new StringBuilder();
                        sb.append("newWebView.onRenderProcessGone priority=");
                        sb.append(renderProcessGoneDetail == null ? null : Integer.valueOf(renderProcessGoneDetail.rendererPriorityAtExit()));
                        sb.append(" didCrash=");
                        sb.append(renderProcessGoneDetail == null ? null : Boolean.valueOf(renderProcessGoneDetail.didCrash()));
                        myWebView.d(sb.toString());
                    } else {
                        MyWebView.this.d("newWebView.onRenderProcessGone");
                    }
                    if (AndroidUtilities.isSafeToShow(MyWebView.this.getContext())) {
                        new AlertDialog.Builder(MyWebView.this.getContext(), MyWebView.this.botWebViewContainer == null ? null : MyWebView.this.botWebViewContainer.resourcesProvider).setTitle(LocaleController.getString(R.string.ChromeCrashTitle)).setMessage(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.ChromeCrashMessage), new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$1$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                BotWebViewContainer.MyWebView.3.1.this.lambda$onRenderProcessGone$0();
                            }
                        })).setPositiveButton(LocaleController.getString(R.string.OK), null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$1$$ExternalSyntheticLambda0
                            @Override // android.content.DialogInterface.OnDismissListener
                            public final void onDismiss(DialogInterface dialogInterface) {
                                BotWebViewContainer.MyWebView.3.1.this.lambda$onRenderProcessGone$1(dialogInterface);
                            }
                        }).show();
                        return true;
                    }
                    return true;
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
                public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                    if (MyWebView.this.botWebViewContainer != null) {
                        MyWebView.this.botWebViewContainer.onOpenUri(Uri.parse(str));
                        return true;
                    }
                    return true;
                }
            }

            @Override // android.webkit.WebChromeClient
            public void onCloseWindow(WebView webView) {
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onCloseWindow " + webView);
                if (MyWebView.this.botWebViewContainer != null && MyWebView.this.botWebViewContainer.delegate != null) {
                    MyWebView.this.botWebViewContainer.delegate.onCloseRequested(null);
                } else if (MyWebView.this.onCloseListener != null) {
                    MyWebView.this.onCloseListener.run();
                    MyWebView.this.onCloseListener = null;
                }
                super.onCloseWindow(webView);
            }

            @Override // android.webkit.WebChromeClient
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                Activity findActivity = AndroidUtilities.findActivity(MyWebView.this.getContext());
                if (findActivity != null) {
                    if (MyWebView.this.botWebViewContainer != null) {
                        if (MyWebView.this.botWebViewContainer.mFilePathCallback != null) {
                            MyWebView.this.botWebViewContainer.mFilePathCallback.onReceiveValue(null);
                        }
                        MyWebView.this.botWebViewContainer.mFilePathCallback = valueCallback;
                        if (Build.VERSION.SDK_INT >= 21) {
                            findActivity.startActivityForResult(fileChooserParams.createIntent(), 3000);
                        } else {
                            Intent intent = new Intent("android.intent.action.GET_CONTENT");
                            intent.addCategory("android.intent.category.OPENABLE");
                            intent.setType("*/*");
                            findActivity.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.BotWebViewFileChooserTitle)), 3000);
                        }
                        MyWebView.this.d("onShowFileChooser: true");
                        return true;
                    }
                    MyWebView.this.d("onShowFileChooser: no container, false");
                    return false;
                }
                MyWebView.this.d("onShowFileChooser: no activity, false");
                return false;
            }

            @Override // android.webkit.WebChromeClient
            public void onProgressChanged(WebView webView, int i) {
                if (MyWebView.this.botWebViewContainer != null && MyWebView.this.botWebViewContainer.webViewProgressListener != null) {
                    MyWebView myWebView = MyWebView.this;
                    myWebView.d("onProgressChanged " + i + "%");
                    MyWebView.this.botWebViewContainer.webViewProgressListener.accept(Float.valueOf(((float) i) / 100.0f));
                    return;
                }
                MyWebView myWebView2 = MyWebView.this;
                myWebView2.d("onProgressChanged " + i + "%: no container");
            }

            @Override // android.webkit.WebChromeClient
            public void onGeolocationPermissionsShowPrompt(final String str, final GeolocationPermissions.Callback callback) {
                if (MyWebView.this.botWebViewContainer == null || MyWebView.this.botWebViewContainer.parentActivity == null) {
                    MyWebView.this.d("onGeolocationPermissionsShowPrompt: no container");
                    callback.invoke(str, false, false);
                    return;
                }
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onGeolocationPermissionsShowPrompt " + str);
                String userName = this.val$bot ? UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser) : AndroidUtilities.getHostAuthority(MyWebView.this.getUrl());
                Activity activity = MyWebView.this.botWebViewContainer.parentActivity;
                Theme.ResourcesProvider resourcesProvider = MyWebView.this.botWebViewContainer.resourcesProvider;
                String[] strArr = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
                int i = R.raw.permission_request_location;
                String formatString = LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestGeolocationPermission : R.string.WebViewRequestGeolocationPermission, userName);
                boolean z = this.val$bot;
                Dialog createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(activity, resourcesProvider, strArr, i, formatString, LocaleController.formatString(R.string.BotWebViewRequestGeolocationPermissionWithHint, userName), new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda1
                    @Override // androidx.core.util.Consumer
                    public final void accept(Object obj) {
                        BotWebViewContainer.MyWebView.3.this.lambda$onGeolocationPermissionsShowPrompt$1(callback, str, (Boolean) obj);
                    }
                });
                this.lastPermissionsDialog = createWebViewPermissionsRequestDialog;
                createWebViewPermissionsRequestDialog.show();
            }

            public /* synthetic */ void lambda$onGeolocationPermissionsShowPrompt$1(final GeolocationPermissions.Callback callback, final String str, Boolean bool) {
                if (this.lastPermissionsDialog != null) {
                    this.lastPermissionsDialog = null;
                    if (bool.booleanValue()) {
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda0
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

            public /* synthetic */ void lambda$onGeolocationPermissionsShowPrompt$0(GeolocationPermissions.Callback callback, String str, Boolean bool) {
                callback.invoke(str, bool.booleanValue(), false);
                if (bool.booleanValue()) {
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                }
            }

            @Override // android.webkit.WebChromeClient
            public void onGeolocationPermissionsHidePrompt() {
                if (this.lastPermissionsDialog != null) {
                    MyWebView.this.d("onGeolocationPermissionsHidePrompt: dialog.dismiss");
                    this.lastPermissionsDialog.dismiss();
                    this.lastPermissionsDialog = null;
                    return;
                }
                MyWebView.this.d("onGeolocationPermissionsHidePrompt: no dialog");
            }

            @Override // android.webkit.WebChromeClient
            public void onPermissionRequest(final PermissionRequest permissionRequest) {
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
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onPermissionRequest " + permissionRequest);
                String userName = this.val$bot ? UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser) : AndroidUtilities.getHostAuthority(MyWebView.this.getUrl());
                final String[] resources = permissionRequest.getResources();
                if (resources.length == 1) {
                    final String str = resources[0];
                    if (MyWebView.this.botWebViewContainer.parentActivity == null) {
                        permissionRequest.deny();
                        return;
                    }
                    str.hashCode();
                    if (str.equals("android.webkit.resource.VIDEO_CAPTURE")) {
                        Dialog createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(MyWebView.this.botWebViewContainer.parentActivity, MyWebView.this.botWebViewContainer.resourcesProvider, new String[]{"android.permission.CAMERA"}, R.raw.permission_request_camera, LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestCameraPermission : R.string.WebViewRequestCameraPermission, userName), LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestCameraPermissionWithHint : R.string.WebViewRequestCameraPermissionWithHint, userName), new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda3
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.3.this.lambda$onPermissionRequest$5(permissionRequest, str, (Boolean) obj);
                            }
                        });
                        this.lastPermissionsDialog = createWebViewPermissionsRequestDialog;
                        createWebViewPermissionsRequestDialog.show();
                    } else if (str.equals("android.webkit.resource.AUDIO_CAPTURE")) {
                        Dialog createWebViewPermissionsRequestDialog2 = AlertsCreator.createWebViewPermissionsRequestDialog(MyWebView.this.botWebViewContainer.parentActivity, MyWebView.this.botWebViewContainer.resourcesProvider, new String[]{"android.permission.RECORD_AUDIO"}, R.raw.permission_request_microphone, LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestMicrophonePermission : R.string.WebViewRequestMicrophonePermission, userName), LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestMicrophonePermissionWithHint : R.string.WebViewRequestMicrophonePermissionWithHint, userName), new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda2
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.3.this.lambda$onPermissionRequest$3(permissionRequest, str, (Boolean) obj);
                            }
                        });
                        this.lastPermissionsDialog = createWebViewPermissionsRequestDialog2;
                        createWebViewPermissionsRequestDialog2.show();
                    }
                } else if (resources.length == 2) {
                    if ("android.webkit.resource.AUDIO_CAPTURE".equals(resources[0]) || "android.webkit.resource.VIDEO_CAPTURE".equals(resources[0])) {
                        if ("android.webkit.resource.AUDIO_CAPTURE".equals(resources[1]) || "android.webkit.resource.VIDEO_CAPTURE".equals(resources[1])) {
                            Dialog createWebViewPermissionsRequestDialog3 = AlertsCreator.createWebViewPermissionsRequestDialog(MyWebView.this.botWebViewContainer.parentActivity, MyWebView.this.botWebViewContainer.resourcesProvider, new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO"}, R.raw.permission_request_camera, LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestCameraMicPermission : R.string.WebViewRequestCameraMicPermission, userName), LocaleController.formatString(this.val$bot ? R.string.BotWebViewRequestCameraMicPermissionWithHint : R.string.WebViewRequestCameraMicPermissionWithHint, userName), new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda7
                                @Override // androidx.core.util.Consumer
                                public final void accept(Object obj) {
                                    BotWebViewContainer.MyWebView.3.this.lambda$onPermissionRequest$7(permissionRequest, resources, (Boolean) obj);
                                }
                            });
                            this.lastPermissionsDialog = createWebViewPermissionsRequestDialog3;
                            createWebViewPermissionsRequestDialog3.show();
                        }
                    }
                }
            }

            public /* synthetic */ void lambda$onPermissionRequest$3(final PermissionRequest permissionRequest, final String str, Boolean bool) {
                if (this.lastPermissionsDialog != null) {
                    this.lastPermissionsDialog = null;
                    if (bool.booleanValue()) {
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.RECORD_AUDIO"}, new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda4
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

            public /* synthetic */ void lambda$onPermissionRequest$2(PermissionRequest permissionRequest, String str, Boolean bool) {
                if (bool.booleanValue()) {
                    permissionRequest.grant(new String[]{str});
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                    return;
                }
                permissionRequest.deny();
            }

            public /* synthetic */ void lambda$onPermissionRequest$5(final PermissionRequest permissionRequest, final String str, Boolean bool) {
                if (this.lastPermissionsDialog != null) {
                    this.lastPermissionsDialog = null;
                    if (bool.booleanValue()) {
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.CAMERA"}, new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda5
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

            public /* synthetic */ void lambda$onPermissionRequest$4(PermissionRequest permissionRequest, String str, Boolean bool) {
                if (bool.booleanValue()) {
                    permissionRequest.grant(new String[]{str});
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                    return;
                }
                permissionRequest.deny();
            }

            public /* synthetic */ void lambda$onPermissionRequest$7(final PermissionRequest permissionRequest, final String[] strArr, Boolean bool) {
                if (this.lastPermissionsDialog != null) {
                    this.lastPermissionsDialog = null;
                    if (bool.booleanValue()) {
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO"}, new Consumer() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$3$$ExternalSyntheticLambda6
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

            public /* synthetic */ void lambda$onPermissionRequest$6(PermissionRequest permissionRequest, String[] strArr, Boolean bool) {
                if (bool.booleanValue()) {
                    permissionRequest.grant(new String[]{strArr[0], strArr[1]});
                    MyWebView.this.botWebViewContainer.hasUserPermissions = true;
                    return;
                }
                permissionRequest.deny();
            }

            @Override // android.webkit.WebChromeClient
            public void onPermissionRequestCanceled(PermissionRequest permissionRequest) {
                if (this.lastPermissionsDialog != null) {
                    MyWebView.this.d("onPermissionRequestCanceled: dialog.dismiss");
                    this.lastPermissionsDialog.dismiss();
                    this.lastPermissionsDialog = null;
                    return;
                }
                MyWebView.this.d("onPermissionRequestCanceled: no dialog");
            }

            @Override // android.webkit.WebChromeClient
            public Bitmap getDefaultVideoPoster() {
                return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
            }
        }

        /* loaded from: classes.dex */
        public class 5 implements DownloadListener {
            5() {
                MyWebView.this = r1;
            }

            private String getFilename(String str, String str2, String str3) {
                try {
                    List<String> pathSegments = Uri.parse(str).getPathSegments();
                    String str4 = pathSegments.get(pathSegments.size() - 1);
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

            @Override // android.webkit.DownloadListener
            public void onDownloadStart(final String str, final String str2, String str3, final String str4, long j) {
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onDownloadStart " + str + " " + str2 + " " + str3 + " " + str4 + " " + j);
                try {
                    if (str.startsWith("blob:")) {
                        MyWebView myWebView2 = MyWebView.this;
                        myWebView2.evaluateJS("window.__tg__resolveBlob('" + str.replace("'", "\\'") + "')");
                        return;
                    }
                    final String filename = getFilename(str, str3, str4);
                    final Runnable runnable = new Runnable() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$5$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            BotWebViewContainer.MyWebView.5.this.lambda$onDownloadStart$0(str, str4, str2, filename);
                        }
                    };
                    if (!DownloadController.getInstance(UserConfig.selectedAccount).canDownloadMedia(8, j)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyWebView.this.getContext());
                        builder.setTitle(LocaleController.getString(R.string.WebDownloadAlertTitle));
                        builder.setMessage(AndroidUtilities.replaceTags(j > 0 ? LocaleController.formatString(R.string.WebDownloadAlertInfoWithSize, filename, AndroidUtilities.formatFileSize(j)) : LocaleController.formatString(R.string.WebDownloadAlertInfo, filename)));
                        builder.setPositiveButton(LocaleController.getString(R.string.WebDownloadAlertYes), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$5$$ExternalSyntheticLambda0
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i) {
                                runnable.run();
                            }
                        });
                        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
                        TextView textView = (TextView) builder.show().getButton(-2);
                        if (textView != null) {
                            textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                            return;
                        }
                        return;
                    }
                    runnable.run();
                } catch (Exception e) {
                    FileLog.e(e);
                }
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

        public void search(String str, Runnable runnable) {
            this.searchListener = runnable;
            findAllAsync(str);
        }

        public int getSearchIndex() {
            return this.searchIndex;
        }

        public int getSearchCount() {
            return this.searchCount;
        }

        @Override // android.webkit.WebView
        public String getTitle() {
            return this.lastTitle;
        }

        public void setTitle(String str) {
            this.lastTitle = str;
        }

        public String getOpenURL() {
            return this.openedByUrl;
        }

        @Override // android.webkit.WebView
        public String getUrl() {
            return super.getUrl();
        }

        @Override // android.webkit.WebView
        public Bitmap getFavicon() {
            if (this.errorShown) {
                return null;
            }
            return this.lastFavicon;
        }

        public Bitmap getFavicon(String str) {
            return this.lastFavicons.get(str);
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

        public void setCloseListener(Runnable runnable) {
            this.onCloseListener = runnable;
        }

        public void evaluateJS(String str) {
            if (Build.VERSION.SDK_INT >= 19) {
                evaluateJavascript(str, new ValueCallback() { // from class: org.telegram.ui.web.BotWebViewContainer$MyWebView$$ExternalSyntheticLambda0
                    @Override // android.webkit.ValueCallback
                    public final void onReceiveValue(Object obj) {
                        BotWebViewContainer.MyWebView.lambda$evaluateJS$1((String) obj);
                    }
                });
                return;
            }
            try {
                loadUrl("javascript:" + URLEncoder.encode(str, "UTF-8"));
            } catch (UnsupportedEncodingException unused) {
                loadUrl("javascript:" + URLEncoder.encode(str));
            }
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

        public float getScrollProgress() {
            float max = Math.max(1, computeVerticalScrollRange() - computeVerticalScrollExtent());
            if (max <= getHeight()) {
                return 0.0f;
            }
            return Utilities.clamp01(getScrollY() / max);
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

        @Override // android.webkit.WebView, android.widget.AbsoluteLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824));
        }

        @Override // android.webkit.WebView, android.view.View
        @SuppressLint({"ClickableViewAccessibility"})
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 0) {
                this.botWebViewContainer.lastClickMs = System.currentTimeMillis();
            }
            return super.onTouchEvent(motionEvent);
        }

        @Override // android.webkit.WebView, android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            d("attached");
            AndroidUtilities.checkAndroidTheme(getContext(), true);
            super.onAttachedToWindow();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            d("detached");
            AndroidUtilities.checkAndroidTheme(getContext(), false);
            super.onDetachedFromWindow();
        }

        @Override // android.webkit.WebView
        public void destroy() {
            d("destroy");
            super.destroy();
        }

        @Override // android.webkit.WebView
        public void loadUrl(String str) {
            checkCachedMetaProperties(str);
            this.openedByUrl = str;
            String str2 = BotWebViewContainer.tonsite2magic(str);
            d("loadUrl " + str2);
            super.loadUrl(str2);
            BotWebViewContainer botWebViewContainer = this.botWebViewContainer;
            if (botWebViewContainer != null) {
                botWebViewContainer.onURLChanged(str2, !canGoBack(), !canGoForward());
            }
        }

        @Override // android.webkit.WebView
        public void loadUrl(String str, Map<String, String> map) {
            checkCachedMetaProperties(str);
            this.openedByUrl = str;
            String str2 = BotWebViewContainer.tonsite2magic(str);
            d("loadUrl " + str2 + " " + map);
            super.loadUrl(str2, map);
            BotWebViewContainer botWebViewContainer = this.botWebViewContainer;
            if (botWebViewContainer != null) {
                botWebViewContainer.onURLChanged(str2, !canGoBack(), !canGoForward());
            }
        }

        public void loadUrl(String str, WebMetadataCache.WebMetadata webMetadata) {
            applyCachedMeta(webMetadata);
            this.openedByUrl = str;
            String str2 = BotWebViewContainer.tonsite2magic(str);
            d("loadUrl " + str2 + " with cached meta");
            super.loadUrl(str2);
            BotWebViewContainer botWebViewContainer = this.botWebViewContainer;
            if (botWebViewContainer != null) {
                botWebViewContainer.onURLChanged(str2, !canGoBack(), !canGoForward());
            }
        }

        public void checkCachedMetaProperties(String str) {
            if (this.bot) {
                return;
            }
            applyCachedMeta(WebMetadataCache.getInstance().get(AndroidUtilities.getHostAuthority(str, true)));
        }

        public boolean applyCachedMeta(WebMetadataCache.WebMetadata webMetadata) {
            boolean z = false;
            if (webMetadata == null) {
                return false;
            }
            int i = -1;
            BotWebViewContainer botWebViewContainer = this.botWebViewContainer;
            if (botWebViewContainer != null && botWebViewContainer.delegate != null) {
                if (webMetadata.actionBarColor != 0) {
                    this.botWebViewContainer.delegate.onWebAppBackgroundChanged(true, webMetadata.actionBarColor);
                    this.lastActionBarColorGot = true;
                }
                int i2 = webMetadata.backgroundColor;
                if (i2 != 0) {
                    this.botWebViewContainer.delegate.onWebAppBackgroundChanged(false, webMetadata.backgroundColor);
                    this.lastBackgroundColorGot = true;
                    i = i2;
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
        public void reload() {
            if (Build.VERSION.SDK_INT >= 21) {
                CookieManager.getInstance().flush();
            }
            d("reload");
            super.reload();
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
        public void stopLoading() {
            d("stopLoading");
            super.stopLoading();
        }

        @Override // android.view.View
        public void stopNestedScroll() {
            d("stopNestedScroll");
            super.stopNestedScroll();
        }

        @Override // android.webkit.WebView
        public void postUrl(String str, byte[] bArr) {
            d("postUrl " + str + " " + bArr);
            super.postUrl(str, bArr);
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

        @Override // android.webkit.WebView
        public void pauseTimers() {
            d("pauseTimers");
            super.pauseTimers();
        }

        @Override // android.webkit.WebView
        public void resumeTimers() {
            d("resumeTimers");
            super.resumeTimers();
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

        @Override // android.webkit.WebView
        public void clearHistory() {
            d("clearHistory");
            super.clearHistory();
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

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            return super.drawChild(canvas, view, j);
        }

        @Override // android.webkit.WebView, android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
        }

        @Override // android.webkit.WebView, android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            super.draw(canvas);
        }
    }

    public void d(String str) {
        FileLog.d("[webviewcontainer] #" + this.tag + " " + str);
    }

    public static String tonsite2magic(String str) {
        if (str != null && isTonsite(Uri.parse(str))) {
            String hostAuthority = AndroidUtilities.getHostAuthority(str);
            String rotateTONHost = rotateTONHost(hostAuthority);
            if (rotatedTONHosts == null) {
                rotatedTONHosts = new HashMap<>();
            }
            rotatedTONHosts.put(rotateTONHost, hostAuthority);
            return Browser.replaceHostname(Uri.parse(str), rotateTONHost, "https");
        }
        return str;
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
        return (hostAuthority.endsWith(sb.toString()) && (str2 = rotatedTONHosts.get(hostAuthority)) != null) ? Browser.replace(Uri.parse(str), "tonsite", str2, null) : str;
    }
}
