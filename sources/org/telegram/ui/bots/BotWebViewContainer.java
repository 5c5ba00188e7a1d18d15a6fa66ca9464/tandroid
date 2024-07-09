package org.telegram.ui.bots;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SslErrorHandler;
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
import java.io.UnsupportedEncodingException;
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
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
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
import org.telegram.tgnet.TLRPC$TL_bots_allowSendMessage;
import org.telegram.tgnet.TLRPC$TL_bots_canSendMessage;
import org.telegram.tgnet.TLRPC$TL_bots_invokeWebViewCustomMethod;
import org.telegram.tgnet.TLRPC$TL_dataJSON;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputInvoiceSlug;
import org.telegram.tgnet.TLRPC$TL_messages_getAttachMenuBot;
import org.telegram.tgnet.TLRPC$TL_payments_getPaymentForm;
import org.telegram.tgnet.TLRPC$Updates;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.CameraScanActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.bots.BotWebViewContainer;
import org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout;
/* loaded from: classes4.dex */
public abstract class BotWebViewContainer extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private static int tags;
    private BotBiometry biometry;
    private long blockedDialogsUntil;
    private TLRPC$User botUser;
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
    private int lastButtonColor;
    private String lastButtonText;
    private int lastButtonTextColor;
    private long lastClickMs;
    private long lastDialogClosed;
    private long lastDialogCooldownTime;
    private int lastDialogType;
    private boolean lastExpanded;
    private String lastQrText;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mUrl;
    private Runnable onPermissionsRequestResultCallback;
    private Activity parentActivity;
    private boolean preserving;
    private Theme.ResourcesProvider resourcesProvider;
    private int shownDialogsCount;
    private final int tag;
    private boolean wasOpenedByLinkIntent;
    private MyWebView webView;
    private boolean webViewNotAvailable;
    private TextView webViewNotAvailableText;
    private Consumer<Float> webViewProgressListener;
    private WebViewProxy webViewProxy;
    private WebViewScrollListener webViewScrollListener;

    /* loaded from: classes4.dex */
    public interface Delegate {

        /* loaded from: classes4.dex */
        public final /* synthetic */ class -CC {
            public static void $default$onSendWebViewData(Delegate delegate, String str) {
            }

            public static void $default$onWebAppReady(Delegate delegate) {
            }
        }

        boolean isClipboardAvailable();

        void onCloseRequested(Runnable runnable);

        void onSendWebViewData(String str);

        void onSetBackButtonVisible(boolean z);

        void onSetSettingsButtonVisible(boolean z);

        void onSetupMainButton(boolean z, boolean z2, String str, int i, int i2, boolean z3);

        void onWebAppExpand();

        void onWebAppOpenInvoice(TLRPC$InputInvoice tLRPC$InputInvoice, String str, TLObject tLObject);

        void onWebAppReady();

        void onWebAppSetActionBarColor(int i, int i2, boolean z);

        void onWebAppSetBackgroundColor(int i);

        void onWebAppSetupClosingBehavior(boolean z);

        void onWebAppSwitchInlineQuery(TLRPC$User tLRPC$User, String str, List<String> list);
    }

    /* loaded from: classes4.dex */
    public interface WebViewScrollListener {
        void onWebViewScrolled(WebView webView, int i, int i2);
    }

    public static /* synthetic */ void lambda$evaluateJs$5(String str) {
    }

    public void onWebViewCreated() {
    }

    static /* synthetic */ int access$908() {
        int i = tags;
        tags = i + 1;
        return i;
    }

    public BotWebViewContainer(Context context, Theme.ResourcesProvider resourcesProvider, int i) {
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
        this.resourcesProvider = resourcesProvider;
        d("created new webview container");
        if (context instanceof Activity) {
            this.parentActivity = (Activity) context;
        }
        cellFlickerDrawable.drawFrame = false;
        cellFlickerDrawable.setColors(i, 153, 204);
        BackupImageView backupImageView = new BackupImageView(context) { // from class: org.telegram.ui.bots.BotWebViewContainer.1
            {
                BotWebViewContainer.this = this;
                this.imageReceiver = new 1(this);
            }

            /* loaded from: classes4.dex */
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
                    duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$1$1$$ExternalSyntheticLambda0
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

    private void checkCreateWebView() {
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

    public void replaceWebView(MyWebView myWebView, WebViewProxy webViewProxy) {
        setupWebView(myWebView, webViewProxy);
    }

    private void setupWebView(MyWebView myWebView) {
        setupWebView(myWebView, null);
    }

    public WebViewProxy getProxy() {
        return this.webViewProxy;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void setupWebView(MyWebView myWebView, WebViewProxy webViewProxy) {
        MyWebView myWebView2 = this.webView;
        if (myWebView2 != null) {
            myWebView2.destroy();
            removeView(this.webView);
        }
        if (myWebView != null) {
            AndroidUtilities.removeFromParent(myWebView);
        }
        MyWebView myWebView3 = myWebView == null ? new MyWebView(getContext()) : myWebView;
        this.webView = myWebView3;
        myWebView3.setContainers(this, this.webViewScrollListener);
        this.webView.setBackgroundColor(getColor(Theme.key_windowBackgroundWhite));
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        File file = new File(ApplicationLoader.getFilesDirFixed(), "webview_database");
        if ((file.exists() && file.isDirectory()) || file.mkdirs()) {
            settings.setDatabasePath(file.getAbsolutePath());
        }
        GeolocationPermissions.getInstance().clearAll();
        this.webView.setVerticalScrollBarEnabled(false);
        if (myWebView == null) {
            this.webView.setAlpha(0.0f);
        }
        addView(this.webView);
        if (Build.VERSION.SDK_INT >= 17) {
            if (webViewProxy != null) {
                this.webViewProxy = webViewProxy;
            }
            if (this.webViewProxy == null) {
                WebViewProxy webViewProxy2 = new WebViewProxy(this);
                this.webViewProxy = webViewProxy2;
                this.webView.addJavascriptInterface(webViewProxy2, "TelegramWebviewProxy");
            }
            this.webViewProxy.setContainer(this);
        }
        onWebViewCreated();
    }

    public void onOpenUri(Uri uri) {
        onOpenUri(uri, null, false, false);
    }

    private void onOpenUri(Uri uri, String str, boolean z, boolean z2) {
        if (this.isRequestingPageOpen) {
            return;
        }
        if (System.currentTimeMillis() - this.lastClickMs <= 1000 || !z2) {
            this.lastClickMs = 0L;
            boolean[] zArr = {false};
            if (Browser.isInternalUri(uri, zArr) && !zArr[0] && this.delegate != null) {
                setDescendantFocusability(393216);
                setFocusable(false);
                this.webView.setFocusable(false);
                this.webView.setDescendantFocusability(393216);
                this.webView.clearFocus();
                ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(getWindowToken(), 2);
            }
            Browser.openUrl(getContext(), uri, true, z, false, null, str);
        }
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

    public void setPageLoaded(String str) {
        MyWebView myWebView = this.webView;
        if (myWebView != null) {
            myWebView.isPageLoaded = true;
        }
        if (this.isPageLoaded) {
            d("setPageLoaded: already loaded");
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(this.webView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.flickerView, View.ALPHA, 0.0f));
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.bots.BotWebViewContainer.2
            {
                BotWebViewContainer.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                BotWebViewContainer.this.flickerView.setVisibility(8);
            }
        });
        animatorSet.start();
        this.mUrl = str;
        d("setPageLoaded: isPageLoaded = true!");
        this.isPageLoaded = true;
        setFocusable(true);
        this.delegate.onWebAppReady();
    }

    public void setState(boolean z, String str) {
        d("setState(" + z + ", " + str + ")");
        this.isPageLoaded = z;
        this.mUrl = str;
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
            this.onPermissionsRequestResultCallback = new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda16
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
        if ((this.isPageLoaded || z2) && (getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer)) {
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
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda30
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BotWebViewContainer.this.lambda$loadFlickerAndSettingsItem$2(tLObject, tLRPC$TL_error);
            }
        });
    }

    public /* synthetic */ void lambda$loadFlickerAndSettingsItem$2(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda19
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
        NotificationCenter.getInstance(this.currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda14
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
    }

    public void loadUrl(int i, final String str) {
        this.currentAccount = i;
        NotificationCenter.getInstance(i).doOnIdle(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda17
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
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        d("attached");
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.onActivityResultReceived);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.onRequestPermissionResultReceived);
        Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.bots.BotWebViewContainer.3
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
        d("destroyWebView");
        MyWebView myWebView = this.webView;
        if (myWebView != null) {
            if (myWebView.getParent() != null) {
                removeView(this.webView);
            }
            if (!this.preserving) {
                this.webView.destroy();
            }
            this.isPageLoaded = false;
        }
    }

    public boolean isBackButtonVisible() {
        return this.isBackButtonVisible;
    }

    public void evaluateJs(final String str, final boolean z) {
        NotificationCenter.getInstance(this.currentAccount).doOnIdle(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda23
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
            myWebView.evaluateJavascript(str, new ValueCallback() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda12
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

    public void setWasOpenedByLinkIntent(boolean z) {
        this.wasOpenedByLinkIntent = z;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:1258:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1264:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:822:0x0290  */
    /* JADX WARN: Removed duplicated region for block: B:826:0x0298 A[Catch: JSONException -> 0x02aa, TryCatch #11 {JSONException -> 0x02aa, blocks: (B:804:0x0246, B:806:0x0255, B:808:0x025b, B:809:0x0264, B:828:0x029c, B:825:0x0295, B:826:0x0298, B:814:0x0279, B:817:0x0283), top: B:1212:0x0246 }] */
    /* JADX WARN: Removed duplicated region for block: B:828:0x029c A[Catch: JSONException -> 0x02aa, TRY_LEAVE, TryCatch #11 {JSONException -> 0x02aa, blocks: (B:804:0x0246, B:806:0x0255, B:808:0x025b, B:809:0x0264, B:828:0x029c, B:825:0x0295, B:826:0x0298, B:814:0x0279, B:817:0x0283), top: B:1212:0x0246 }] */
    /* JADX WARN: Removed duplicated region for block: B:900:0x03cc  */
    /* JADX WARN: Removed duplicated region for block: B:924:0x0413  */
    /* JADX WARN: Removed duplicated region for block: B:931:0x0421 A[Catch: Exception -> 0x048c, TryCatch #14 {Exception -> 0x048c, blocks: (B:882:0x038c, B:968:0x0487, B:905:0x03d4, B:906:0x03d8, B:929:0x041b, B:930:0x041e, B:931:0x0421, B:913:0x03f2, B:916:0x03fc, B:919:0x0406, B:933:0x0426, B:934:0x0430, B:962:0x0476, B:963:0x0479, B:964:0x047c, B:965:0x047f, B:966:0x0482, B:936:0x0434, B:939:0x043e, B:942:0x0448, B:945:0x0452, B:948:0x045c, B:889:0x03ab, B:892:0x03b5, B:895:0x03bf), top: B:1218:0x038c }] */
    /* JADX WARN: Removed duplicated region for block: B:933:0x0426 A[Catch: Exception -> 0x048c, TryCatch #14 {Exception -> 0x048c, blocks: (B:882:0x038c, B:968:0x0487, B:905:0x03d4, B:906:0x03d8, B:929:0x041b, B:930:0x041e, B:931:0x0421, B:913:0x03f2, B:916:0x03fc, B:919:0x0406, B:933:0x0426, B:934:0x0430, B:962:0x0476, B:963:0x0479, B:964:0x047c, B:965:0x047f, B:966:0x0482, B:936:0x0434, B:939:0x043e, B:942:0x0448, B:945:0x0452, B:948:0x045c, B:889:0x03ab, B:892:0x03b5, B:895:0x03bf), top: B:1218:0x038c }] */
    /* JADX WARN: Removed duplicated region for block: B:968:0x0487 A[Catch: Exception -> 0x048c, TRY_LEAVE, TryCatch #14 {Exception -> 0x048c, blocks: (B:882:0x038c, B:968:0x0487, B:905:0x03d4, B:906:0x03d8, B:929:0x041b, B:930:0x041e, B:931:0x0421, B:913:0x03f2, B:916:0x03fc, B:919:0x0406, B:933:0x0426, B:934:0x0430, B:962:0x0476, B:963:0x0479, B:964:0x047c, B:965:0x047f, B:966:0x0482, B:936:0x0434, B:939:0x043e, B:942:0x0448, B:945:0x0452, B:948:0x045c, B:889:0x03ab, B:892:0x03b5, B:895:0x03bf), top: B:1218:0x038c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onEventReceived(String str, String str2) {
        char c;
        char c2;
        char c3;
        BotWebViewVibrationEffect botWebViewVibrationEffect;
        char c4;
        boolean z;
        char c5;
        int i;
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
            case -71726289:
                if (str.equals("web_app_close")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case -58095910:
                if (str.equals("web_app_ready")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 22015443:
                if (str.equals("web_app_read_text_from_clipboard")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case 668142772:
                if (str.equals("web_app_data_send")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case 751292356:
                if (str.equals("web_app_switch_inline_query")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case 1011447167:
                if (str.equals("web_app_setup_back_button")) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case 1210129967:
                if (str.equals("web_app_biometry_request_access")) {
                    c = 20;
                    break;
                }
                c = 65535;
                break;
            case 1273834781:
                if (str.equals("web_app_trigger_haptic_feedback")) {
                    c = 21;
                    break;
                }
                c = 65535;
                break;
            case 1398490221:
                if (str.equals("web_app_setup_main_button")) {
                    c = 22;
                    break;
                }
                c = 65535;
                break;
            case 1455972419:
                if (str.equals("web_app_setup_settings_button")) {
                    c = 23;
                    break;
                }
                c = 65535;
                break;
            case 1882780382:
                if (str.equals("web_app_biometry_update_token")) {
                    c = 24;
                    break;
                }
                c = 65535;
                break;
            case 1917103703:
                if (str.equals("web_app_set_header_color")) {
                    c = 25;
                    break;
                }
                c = 65535;
                break;
            case 2001330488:
                if (str.equals("web_app_set_background_color")) {
                    c = 26;
                    break;
                }
                c = 65535;
                break;
            case 2036090717:
                if (str.equals("web_app_request_write_access")) {
                    c = 27;
                    break;
                }
                c = 65535;
                break;
            case 2139805763:
                if (str.equals("web_app_expand")) {
                    c = 28;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        BotWebViewVibrationEffect botWebViewVibrationEffect2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        botWebViewVibrationEffect2 = null;
        botWebViewVibrationEffect2 = null;
        switch (c) {
            case 0:
                try {
                    JSONObject jSONObject = new JSONObject(str2);
                    final String string = jSONObject.getString("req_id");
                    String string2 = jSONObject.getString("method");
                    String obj = jSONObject.get("params").toString();
                    TLRPC$TL_bots_invokeWebViewCustomMethod tLRPC$TL_bots_invokeWebViewCustomMethod = new TLRPC$TL_bots_invokeWebViewCustomMethod();
                    tLRPC$TL_bots_invokeWebViewCustomMethod.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botUser);
                    tLRPC$TL_bots_invokeWebViewCustomMethod.custom_method = string2;
                    TLRPC$TL_dataJSON tLRPC$TL_dataJSON = new TLRPC$TL_dataJSON();
                    tLRPC$TL_bots_invokeWebViewCustomMethod.params = tLRPC$TL_dataJSON;
                    tLRPC$TL_dataJSON.data = obj;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_bots_invokeWebViewCustomMethod, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda31
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
                if (this.hasQRPending) {
                    this.cameraBottomSheet.dismiss();
                    return;
                }
                return;
            case 2:
                notifyBiometryReceived();
                return;
            case 3:
                try {
                    JSONObject jSONObject2 = new JSONObject(str2);
                    Uri parse = Uri.parse(jSONObject2.optString("url"));
                    String optString = jSONObject2.optString("try_browser");
                    if (MessagesController.getInstance(this.currentAccount).webAppAllowedProtocols == null || !MessagesController.getInstance(this.currentAccount).webAppAllowedProtocols.contains(parse.getScheme())) {
                        return;
                    }
                    onOpenUri(parse, optString, jSONObject2.optBoolean("try_instant_view"), true);
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
                    JSONObject jSONObject3 = new JSONObject(str2);
                    String optString2 = jSONObject3.optString("title", null);
                    String string3 = jSONObject3.getString("message");
                    JSONArray jSONArray = jSONObject3.getJSONArray("buttons");
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
                        message.setPositiveButton(popupButton.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda0
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i4) {
                                BotWebViewContainer.this.lambda$onEventReceived$7(popupButton, atomicBoolean, dialogInterface, i4);
                            }
                        });
                    }
                    if (arrayList.size() >= 2) {
                        final PopupButton popupButton2 = (PopupButton) arrayList.get(1);
                        message.setNegativeButton(popupButton2.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda2
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i4) {
                                BotWebViewContainer.this.lambda$onEventReceived$8(popupButton2, atomicBoolean, dialogInterface, i4);
                            }
                        });
                    }
                    if (arrayList.size() == 3) {
                        final PopupButton popupButton3 = (PopupButton) arrayList.get(2);
                        message.setNeutralButton(popupButton3.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda1
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i4) {
                                BotWebViewContainer.this.lambda$onEventReceived$9(popupButton3, atomicBoolean, dialogInterface, i4);
                            }
                        });
                    }
                    message.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda10
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
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda32
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
                            NotificationCenter.getGlobalInstance().addObserver(new NotificationCenter.NotificationCenterDelegate() { // from class: org.telegram.ui.bots.BotWebViewContainer.4
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
                        JSONObject jSONObject4 = new JSONObject();
                        jSONObject4.put("status", "cancelled");
                        notifyEvent("phone_requested", jSONObject4);
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
                builder.setPositiveButton(LocaleController.getString("ShareContact", R.string.ShareContact), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda6
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        BotWebViewContainer.this.lambda$onEventReceived$23(strArr, z2, dialogInterface, i4);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda8
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i4) {
                        dialogInterface.dismiss();
                    }
                });
                showDialog(4, builder.create(), new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda24
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
                    str3 = new JSONObject(str2).getString("reason");
                } catch (Exception unused) {
                }
                createBiometry();
                BotBiometry botBiometry = this.biometry;
                if (botBiometry == null) {
                    return;
                }
                if (!botBiometry.access_granted) {
                    try {
                        JSONObject jSONObject5 = new JSONObject();
                        jSONObject5.put("status", "failed");
                        notifyEvent("biometry_auth_requested", jSONObject5);
                        return;
                    } catch (Exception e8) {
                        FileLog.e(e8);
                        return;
                    }
                }
                botBiometry.requestToken(str3, new Utilities.Callback2() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda27
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
                try {
                    z = new JSONObject(str2).optBoolean("return_back");
                } catch (Exception e10) {
                    FileLog.e(e10);
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
            case 15:
                setPageLoaded(this.webView.getUrl());
                return;
            case 16:
                try {
                    String string4 = new JSONObject(str2).getString("req_id");
                    if (this.delegate.isClipboardAvailable() && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                        CharSequence text = ((ClipboardManager) getContext().getSystemService("clipboard")).getText();
                        notifyEvent("clipboard_text_received", new JSONObject().put("req_id", string4).put("data", text != null ? text.toString() : ""));
                        return;
                    }
                    notifyEvent("clipboard_text_received", new JSONObject().put("req_id", string4));
                    return;
                } catch (JSONException e11) {
                    FileLog.e(e11);
                    return;
                }
            case 17:
                try {
                    this.delegate.onSendWebViewData(new JSONObject(str2).optString("data"));
                    return;
                } catch (JSONException e12) {
                    FileLog.e(e12);
                    return;
                }
            case 18:
                try {
                    JSONObject jSONObject6 = new JSONObject(str2);
                    ArrayList arrayList2 = new ArrayList();
                    JSONArray jSONArray2 = jSONObject6.getJSONArray("chat_types");
                    for (int i4 = 0; i4 < jSONArray2.length(); i4++) {
                        arrayList2.add(jSONArray2.getString(i4));
                    }
                    this.delegate.onWebAppSwitchInlineQuery(this.botUser, jSONObject6.getString("query"), arrayList2);
                    return;
                } catch (JSONException e13) {
                    FileLog.e(e13);
                    return;
                }
            case 19:
                try {
                    boolean optBoolean = new JSONObject(str2).optBoolean("is_visible");
                    if (optBoolean != this.isBackButtonVisible) {
                        this.isBackButtonVisible = optBoolean;
                        this.delegate.onSetBackButtonVisible(optBoolean);
                        return;
                    }
                    return;
                } catch (JSONException e14) {
                    FileLog.e(e14);
                    return;
                }
            case 20:
                try {
                    str5 = new JSONObject(str2).getString("reason");
                } catch (Exception unused2) {
                }
                createBiometry();
                BotBiometry botBiometry2 = this.biometry;
                if (botBiometry2 == null) {
                    return;
                }
                boolean z3 = botBiometry2.access_requested;
                if (z3 && botBiometry2.disabled) {
                    notifyBiometryReceived();
                    return;
                } else if (!botBiometry2.access_granted) {
                    final Runnable[] runnableArr = {new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda15
                        @Override // java.lang.Runnable
                        public final void run() {
                            BotWebViewContainer.this.lambda$onEventReceived$26();
                        }
                    }};
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext(), this.resourcesProvider);
                    if (TextUtils.isEmpty(str5)) {
                        builder2.setTitle(LocaleController.getString(R.string.BotAllowBiometryTitle));
                        builder2.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.BotAllowBiometryMessage, UserObject.getUserName(this.botUser))));
                    } else {
                        builder2.setTitle(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.BotAllowBiometryMessage, UserObject.getUserName(this.botUser))));
                        builder2.setMessage(str5);
                    }
                    builder2.setPositiveButton(LocaleController.getString(R.string.Allow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda3
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i5) {
                            BotWebViewContainer.this.lambda$onEventReceived$28(runnableArr, dialogInterface, i5);
                        }
                    });
                    builder2.setNegativeButton(LocaleController.getString(R.string.Cancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda4
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i5) {
                            BotWebViewContainer.this.lambda$onEventReceived$29(runnableArr, dialogInterface, i5);
                        }
                    });
                    builder2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda11
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
            case 21:
                try {
                    JSONObject jSONObject7 = new JSONObject(str2);
                    String optString5 = jSONObject7.optString("type");
                    int hashCode = optString5.hashCode();
                    if (hashCode == -1184809658) {
                        if (optString5.equals("impact")) {
                            c2 = 0;
                            if (c2 == 0) {
                            }
                        }
                        c2 = 65535;
                        if (c2 == 0) {
                        }
                    } else if (hashCode != 193071555) {
                        if (hashCode == 595233003 && optString5.equals("notification")) {
                            c2 = 1;
                            if (c2 == 0) {
                                if (c2 == 1) {
                                    String optString6 = jSONObject7.optString("notification_type");
                                    int hashCode2 = optString6.hashCode();
                                    if (hashCode2 == -1867169789) {
                                        if (optString6.equals("success")) {
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
                                        if (hashCode2 == 1124446108 && optString6.equals("warning")) {
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
                                        if (optString6.equals("error")) {
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
                                    botWebViewVibrationEffect2 = BotWebViewVibrationEffect.SELECTION_CHANGE;
                                }
                                if (botWebViewVibrationEffect2 != null) {
                                    botWebViewVibrationEffect2.vibrate();
                                    return;
                                }
                                return;
                            }
                            String optString7 = jSONObject7.optString("impact_style");
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
                            } else if (botWebViewVibrationEffect2 != null) {
                            }
                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                            if (botWebViewVibrationEffect2 != null) {
                            }
                        }
                        c2 = 65535;
                        if (c2 == 0) {
                        }
                    } else {
                        if (optString5.equals("selection_change")) {
                            c2 = 2;
                            if (c2 == 0) {
                            }
                        }
                        c2 = 65535;
                        if (c2 == 0) {
                        }
                    }
                } catch (Exception e15) {
                    FileLog.e(e15);
                    return;
                }
                FileLog.e(e15);
                return;
            case 22:
                try {
                    JSONObject jSONObject8 = new JSONObject(str2);
                    boolean optBoolean2 = jSONObject8.optBoolean("is_active", false);
                    String trim = jSONObject8.optString("text", this.lastButtonText).trim();
                    boolean z4 = jSONObject8.optBoolean("is_visible", false) && !TextUtils.isEmpty(trim);
                    int parseColor = jSONObject8.has("color") ? Color.parseColor(jSONObject8.optString("color")) : this.lastButtonColor;
                    int parseColor2 = jSONObject8.has("text_color") ? Color.parseColor(jSONObject8.optString("text_color")) : this.lastButtonTextColor;
                    boolean z5 = jSONObject8.optBoolean("is_progress_visible", false) && z4;
                    this.lastButtonColor = parseColor;
                    this.lastButtonTextColor = parseColor2;
                    this.lastButtonText = trim;
                    this.buttonData = str2;
                    this.delegate.onSetupMainButton(z4, optBoolean2, trim, parseColor, parseColor2, z5);
                    return;
                } catch (IllegalArgumentException | JSONException e16) {
                    FileLog.e(e16);
                    return;
                }
            case 23:
                try {
                    boolean optBoolean3 = new JSONObject(str2).optBoolean("is_visible");
                    if (optBoolean3 != this.isSettingsButtonVisible) {
                        this.isSettingsButtonVisible = optBoolean3;
                        this.delegate.onSetSettingsButtonVisible(optBoolean3);
                        return;
                    }
                    return;
                } catch (JSONException e17) {
                    FileLog.e(e17);
                    return;
                }
            case 24:
                try {
                    JSONObject jSONObject9 = new JSONObject(str2);
                    final String string5 = jSONObject9.getString("token");
                    try {
                        str4 = jSONObject9.getString("reason");
                    } catch (Exception unused3) {
                    }
                    createBiometry();
                    BotBiometry botBiometry3 = this.biometry;
                    if (botBiometry3 == null) {
                        return;
                    }
                    if (!botBiometry3.access_granted) {
                        try {
                            JSONObject jSONObject10 = new JSONObject();
                            jSONObject10.put("status", "failed");
                            notifyEvent("biometry_token_updated", jSONObject10);
                            return;
                        } catch (Exception e18) {
                            FileLog.e(e18);
                            return;
                        }
                    }
                    botBiometry3.updateToken(str4, string5, new Utilities.Callback() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda28
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj2) {
                            BotWebViewContainer.this.lambda$onEventReceived$32(string5, (Boolean) obj2);
                        }
                    });
                    return;
                } catch (Exception e19) {
                    FileLog.e(e19);
                    if (e19 instanceof JSONException) {
                        error("JSON Parse error");
                        return;
                    } else {
                        unknownError();
                        return;
                    }
                }
            case 25:
                try {
                    JSONObject jSONObject11 = new JSONObject(str2);
                    String optString8 = jSONObject11.optString("color", null);
                    if (!TextUtils.isEmpty(optString8)) {
                        int parseColor3 = Color.parseColor(optString8);
                        if (parseColor3 != 0) {
                            this.delegate.onWebAppSetActionBarColor(-1, parseColor3, true);
                            return;
                        }
                        return;
                    }
                    String optString9 = jSONObject11.optString("color_key");
                    int hashCode3 = optString9.hashCode();
                    if (hashCode3 != -1265068311) {
                        if (hashCode3 == -210781868 && optString9.equals("secondary_bg_color")) {
                            c5 = 1;
                            if (c5 != 0) {
                                i = Theme.key_windowBackgroundWhite;
                            } else {
                                i = c5 != 1 ? -1 : Theme.key_windowBackgroundGray;
                            }
                            if (i < 0) {
                                this.delegate.onWebAppSetActionBarColor(i, Theme.getColor(i, this.resourcesProvider), false);
                                return;
                            }
                            return;
                        }
                        c5 = 65535;
                        if (c5 != 0) {
                        }
                        if (i < 0) {
                        }
                    } else {
                        if (optString9.equals("bg_color")) {
                            c5 = 0;
                            if (c5 != 0) {
                            }
                            if (i < 0) {
                            }
                        }
                        c5 = 65535;
                        if (c5 != 0) {
                        }
                        if (i < 0) {
                        }
                    }
                } catch (JSONException e20) {
                    FileLog.e(e20);
                    return;
                }
                break;
            case 26:
                try {
                    this.delegate.onWebAppSetBackgroundColor(Color.parseColor(new JSONObject(str2).optString("color", "#ffffff")) | (-16777216));
                    return;
                } catch (IllegalArgumentException | JSONException e21) {
                    FileLog.e(e21);
                    return;
                }
            case 27:
                if (ignoreDialog(3)) {
                    try {
                        JSONObject jSONObject12 = new JSONObject();
                        jSONObject12.put("status", "cancelled");
                        notifyEvent("write_access_requested", jSONObject12);
                        return;
                    } catch (Exception e22) {
                        FileLog.e(e22);
                        return;
                    }
                }
                TLRPC$TL_bots_canSendMessage tLRPC$TL_bots_canSendMessage = new TLRPC$TL_bots_canSendMessage();
                tLRPC$TL_bots_canSendMessage.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botUser);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_bots_canSendMessage, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda29
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        BotWebViewContainer.this.lambda$onEventReceived$19(tLObject, tLRPC$TL_error);
                    }
                });
                return;
            case 28:
                this.delegate.onWebAppExpand();
                return;
            default:
                FileLog.d("unknown webapp event " + str);
                return;
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda22
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda20
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
            showDialog(3, new AlertDialog.Builder(getContext()).setTitle(LocaleController.getString(R.string.BotWebViewRequestWriteTitle)).setMessage(LocaleController.getString(R.string.BotWebViewRequestWriteMessage)).setPositiveButton(LocaleController.getString(R.string.BotWebViewRequestAllow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    BotWebViewContainer.this.lambda$onEventReceived$15(strArr, dialogInterface, i);
                }
            }).setNegativeButton(LocaleController.getString(R.string.BotWebViewRequestDontAllow), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda7
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create(), new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda25
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
        TLRPC$TL_bots_allowSendMessage tLRPC$TL_bots_allowSendMessage = new TLRPC$TL_bots_allowSendMessage();
        tLRPC$TL_bots_allowSendMessage.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botUser);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_bots_allowSendMessage, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda33
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BotWebViewContainer.this.lambda$onEventReceived$14(strArr, dialogInterface, tLObject, tLRPC$TL_error);
            }
        });
    }

    public /* synthetic */ void lambda$onEventReceived$14(final String[] strArr, final DialogInterface dialogInterface, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda21
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
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda18
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
            MessagesController.getInstance(this.currentAccount).unblockPeer(this.botUser.id, new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda13
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
        this.biometry.requestToken(null, new Utilities.Callback2() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda26
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
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$$ExternalSyntheticLambda9
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                BotWebViewContainer.this.lambda$showDialog$33(runnable, dialogInterface);
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

    public /* synthetic */ void lambda$showDialog$33(Runnable runnable, DialogInterface dialogInterface) {
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
        this.cameraBottomSheet = CameraScanActivity.showAsSheet(activity, false, 3, new CameraScanActivity.CameraScanActivityDelegate() { // from class: org.telegram.ui.bots.BotWebViewContainer.5
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

    /* loaded from: classes4.dex */
    public static class WebViewProxy {
        public BotWebViewContainer container;

        public WebViewProxy(BotWebViewContainer botWebViewContainer) {
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
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        BotWebViewContainer.WebViewProxy.this.lambda$postEvent$0(str, str2);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$postEvent$0(String str, String str2) {
            this.container.onEventReceived(str, str2);
        }
    }

    /* loaded from: classes4.dex */
    public static final class PopupButton {
        public String id;
        public String text;
        public int textColorKey;

        /* JADX WARN: Removed duplicated region for block: B:65:0x007d  */
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

    /* loaded from: classes4.dex */
    public static class MyWebView extends WebView {
        private BotWebViewContainer botWebViewContainer;
        private boolean isPageLoaded;
        private int prevScrollX;
        private int prevScrollY;
        private final int tag;
        private WebViewScrollListener webViewScrollListener;

        public boolean isPageLoaded() {
            return this.isPageLoaded;
        }

        public void d(String str) {
            FileLog.d("[webview] #" + this.tag + " " + str);
        }

        public MyWebView(Context context) {
            super(context);
            this.tag = BotWebViewContainer.access$908();
            d("created new webview");
            setWebViewClient(new 1());
            setWebChromeClient(new 2());
        }

        /* loaded from: classes4.dex */
        public class 1 extends WebViewClient {
            1() {
                MyWebView.this = r1;
            }

            @Override // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                MyWebView myWebView = MyWebView.this;
                StringBuilder sb = new StringBuilder();
                sb.append("shouldInterceptRequest ");
                sb.append(webResourceRequest == null ? null : webResourceRequest.getUrl());
                myWebView.d(sb.toString());
                return super.shouldInterceptRequest(webView, webResourceRequest);
            }

            @Override // android.webkit.WebViewClient
            public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
                MyWebView myWebView = MyWebView.this;
                myWebView.d("shouldInterceptRequest " + str);
                return super.shouldInterceptRequest(webView, str);
            }

            @Override // android.webkit.WebViewClient
            public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
                MyWebView myWebView = MyWebView.this;
                StringBuilder sb = new StringBuilder();
                sb.append("onRenderProcessGone priority=");
                sb.append(renderProcessGoneDetail == null ? null : Integer.valueOf(renderProcessGoneDetail.rendererPriorityAtExit()));
                sb.append(" didCrash=");
                sb.append(renderProcessGoneDetail == null ? null : Boolean.valueOf(renderProcessGoneDetail.didCrash()));
                myWebView.d(sb.toString());
                if (AndroidUtilities.isSafeToShow(MyWebView.this.getContext())) {
                    new AlertDialog.Builder(MyWebView.this.getContext(), MyWebView.this.botWebViewContainer == null ? null : MyWebView.this.botWebViewContainer.resourcesProvider).setTitle(LocaleController.getString(R.string.ChromeCrashTitle)).setMessage(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.ChromeCrashMessage), new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            BotWebViewContainer.MyWebView.1.this.lambda$onRenderProcessGone$0();
                        }
                    })).setPositiveButton(LocaleController.getString(R.string.OK), null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$1$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            BotWebViewContainer.MyWebView.1.this.lambda$onRenderProcessGone$1(dialogInterface);
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
                if (MyWebView.this.botWebViewContainer != null && Browser.isInternalUri(parse, null)) {
                    if (MessagesController.getInstance(MyWebView.this.botWebViewContainer.currentAccount).webAppAllowedProtocols != null && MessagesController.getInstance(MyWebView.this.botWebViewContainer.currentAccount).webAppAllowedProtocols.contains(parse.getScheme())) {
                        MyWebView.this.botWebViewContainer.onOpenUri(parse);
                    }
                    MyWebView myWebView = MyWebView.this;
                    myWebView.d("shouldOverrideUrlLoading(" + str + ") = true");
                    return true;
                }
                MyWebView myWebView2 = MyWebView.this;
                myWebView2.d("shouldOverrideUrlLoading(" + str + ") = false");
                return false;
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onPageStarted " + str);
                super.onPageStarted(webView, str, bitmap);
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView, String str) {
                MyWebView.this.isPageLoaded = true;
                MyWebView.this.d("onPageFinished");
                if (MyWebView.this.botWebViewContainer != null) {
                    MyWebView.this.botWebViewContainer.setPageLoaded(str);
                } else {
                    MyWebView.this.d("onPageFinished: no container");
                }
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onReceivedError: " + webResourceError.getErrorCode() + " " + ((Object) webResourceError.getDescription()));
                super.onReceivedError(webView, webResourceRequest, webResourceError);
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView webView, int i, String str, String str2) {
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onReceivedError: " + i + " " + str + " url=" + str2);
                super.onReceivedError(webView, i, str, str2);
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
                MyWebView myWebView = MyWebView.this;
                StringBuilder sb = new StringBuilder();
                sb.append("onReceivedHttpError: statusCode=");
                sb.append(webResourceResponse == null ? null : Integer.valueOf(webResourceResponse.getStatusCode()));
                sb.append(" request=");
                sb.append(webResourceRequest != null ? webResourceRequest.getUrl() : null);
                myWebView.d(sb.toString());
                super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
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

        /* loaded from: classes4.dex */
        public class 2 extends WebChromeClient {
            private Dialog lastPermissionsDialog;

            2() {
                MyWebView.this = r1;
            }

            @Override // android.webkit.WebChromeClient
            public boolean onCreateWindow(WebView webView, boolean z, boolean z2, Message message) {
                MyWebView myWebView = MyWebView.this;
                myWebView.d("onCreateWindow isDialog=" + z + " isUserGesture=" + z2 + " resultMsg=" + message);
                WebView webView2 = new WebView(webView.getContext());
                webView2.setWebViewClient(new 1());
                ((WebView.WebViewTransport) message.obj).setWebView(webView2);
                message.sendToTarget();
                return true;
            }

            /* loaded from: classes4.dex */
            public class 1 extends WebViewClient {
                1() {
                    2.this = r1;
                }

                @Override // android.webkit.WebViewClient
                public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
                    MyWebView myWebView = MyWebView.this;
                    StringBuilder sb = new StringBuilder();
                    sb.append("newWebView.onRenderProcessGone priority=");
                    sb.append(renderProcessGoneDetail == null ? null : Integer.valueOf(renderProcessGoneDetail.rendererPriorityAtExit()));
                    sb.append(" didCrash=");
                    sb.append(renderProcessGoneDetail == null ? null : Boolean.valueOf(renderProcessGoneDetail.didCrash()));
                    myWebView.d(sb.toString());
                    if (AndroidUtilities.isSafeToShow(MyWebView.this.getContext())) {
                        new AlertDialog.Builder(MyWebView.this.getContext(), MyWebView.this.botWebViewContainer == null ? null : MyWebView.this.botWebViewContainer.resourcesProvider).setTitle(LocaleController.getString(R.string.ChromeCrashTitle)).setMessage(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.ChromeCrashMessage), new Runnable() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$2$1$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                BotWebViewContainer.MyWebView.2.1.this.lambda$onRenderProcessGone$0();
                            }
                        })).setPositiveButton(LocaleController.getString(R.string.OK), null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$2$1$$ExternalSyntheticLambda0
                            @Override // android.content.DialogInterface.OnDismissListener
                            public final void onDismiss(DialogInterface dialogInterface) {
                                BotWebViewContainer.MyWebView.2.1.this.lambda$onRenderProcessGone$1(dialogInterface);
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
                Dialog createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(MyWebView.this.botWebViewContainer.parentActivity, MyWebView.this.botWebViewContainer.resourcesProvider, new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, R.raw.permission_request_location, LocaleController.formatString(R.string.BotWebViewRequestGeolocationPermission, UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser)), LocaleController.formatString(R.string.BotWebViewRequestGeolocationPermissionWithHint, UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser)), new Consumer() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda0
                    @Override // androidx.core.util.Consumer
                    public final void accept(Object obj) {
                        BotWebViewContainer.MyWebView.2.this.lambda$onGeolocationPermissionsShowPrompt$1(callback, str, (Boolean) obj);
                    }
                });
                this.lastPermissionsDialog = createWebViewPermissionsRequestDialog;
                createWebViewPermissionsRequestDialog.show();
            }

            public /* synthetic */ void lambda$onGeolocationPermissionsShowPrompt$1(final GeolocationPermissions.Callback callback, final String str, Boolean bool) {
                if (this.lastPermissionsDialog != null) {
                    this.lastPermissionsDialog = null;
                    if (bool.booleanValue()) {
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, new Consumer() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda1
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.2.this.lambda$onGeolocationPermissionsShowPrompt$0(callback, str, (Boolean) obj);
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
                final String[] resources = permissionRequest.getResources();
                if (resources.length == 1) {
                    final String str = resources[0];
                    if (MyWebView.this.botWebViewContainer.parentActivity == null) {
                        permissionRequest.deny();
                        return;
                    }
                    str.hashCode();
                    if (str.equals("android.webkit.resource.VIDEO_CAPTURE")) {
                        Dialog createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(MyWebView.this.botWebViewContainer.parentActivity, MyWebView.this.botWebViewContainer.resourcesProvider, new String[]{"android.permission.CAMERA"}, R.raw.permission_request_camera, LocaleController.formatString(R.string.BotWebViewRequestCameraPermission, UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser)), LocaleController.formatString(R.string.BotWebViewRequestCameraPermissionWithHint, UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser)), new Consumer() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda3
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.2.this.lambda$onPermissionRequest$5(permissionRequest, str, (Boolean) obj);
                            }
                        });
                        this.lastPermissionsDialog = createWebViewPermissionsRequestDialog;
                        createWebViewPermissionsRequestDialog.show();
                    } else if (str.equals("android.webkit.resource.AUDIO_CAPTURE")) {
                        Dialog createWebViewPermissionsRequestDialog2 = AlertsCreator.createWebViewPermissionsRequestDialog(MyWebView.this.botWebViewContainer.parentActivity, MyWebView.this.botWebViewContainer.resourcesProvider, new String[]{"android.permission.RECORD_AUDIO"}, R.raw.permission_request_microphone, LocaleController.formatString(R.string.BotWebViewRequestMicrophonePermission, UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser)), LocaleController.formatString(R.string.BotWebViewRequestMicrophonePermissionWithHint, UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser)), new Consumer() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda4
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.2.this.lambda$onPermissionRequest$3(permissionRequest, str, (Boolean) obj);
                            }
                        });
                        this.lastPermissionsDialog = createWebViewPermissionsRequestDialog2;
                        createWebViewPermissionsRequestDialog2.show();
                    }
                } else if (resources.length == 2) {
                    if ("android.webkit.resource.AUDIO_CAPTURE".equals(resources[0]) || "android.webkit.resource.VIDEO_CAPTURE".equals(resources[0])) {
                        if ("android.webkit.resource.AUDIO_CAPTURE".equals(resources[1]) || "android.webkit.resource.VIDEO_CAPTURE".equals(resources[1])) {
                            Dialog createWebViewPermissionsRequestDialog3 = AlertsCreator.createWebViewPermissionsRequestDialog(MyWebView.this.botWebViewContainer.parentActivity, MyWebView.this.botWebViewContainer.resourcesProvider, new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO"}, R.raw.permission_request_camera, LocaleController.formatString(R.string.BotWebViewRequestCameraMicPermission, UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser)), LocaleController.formatString(R.string.BotWebViewRequestCameraMicPermissionWithHint, UserObject.getUserName(MyWebView.this.botWebViewContainer.botUser)), new Consumer() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda7
                                @Override // androidx.core.util.Consumer
                                public final void accept(Object obj) {
                                    BotWebViewContainer.MyWebView.2.this.lambda$onPermissionRequest$7(permissionRequest, resources, (Boolean) obj);
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
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.RECORD_AUDIO"}, new Consumer() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda5
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.2.this.lambda$onPermissionRequest$2(permissionRequest, str, (Boolean) obj);
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
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.CAMERA"}, new Consumer() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda2
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.2.this.lambda$onPermissionRequest$4(permissionRequest, str, (Boolean) obj);
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
                        MyWebView.this.botWebViewContainer.runWithPermissions(new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO"}, new Consumer() { // from class: org.telegram.ui.bots.BotWebViewContainer$MyWebView$2$$ExternalSyntheticLambda6
                            @Override // androidx.core.util.Consumer
                            public final void accept(Object obj) {
                                BotWebViewContainer.MyWebView.2.this.lambda$onPermissionRequest$6(permissionRequest, strArr, (Boolean) obj);
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
        }

        public void setContainers(BotWebViewContainer botWebViewContainer, WebViewScrollListener webViewScrollListener) {
            d("setContainers(" + botWebViewContainer + ", " + webViewScrollListener + ")");
            this.botWebViewContainer = botWebViewContainer;
            this.webViewScrollListener = webViewScrollListener;
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
            d("loadUrl " + str);
            super.loadUrl(str);
        }

        @Override // android.webkit.WebView
        public void loadUrl(String str, Map<String, String> map) {
            d("loadUrl " + str + " " + map);
            super.loadUrl(str, map);
        }

        @Override // android.webkit.WebView
        public void reload() {
            d("reload");
            super.reload();
        }

        @Override // android.webkit.WebView
        public void loadData(String str, String str2, String str3) {
            d("loadData " + str + " " + str2 + " " + str3);
            super.loadData(str, str2, str3);
        }

        @Override // android.webkit.WebView
        public void loadDataWithBaseURL(String str, String str2, String str3, String str4, String str5) {
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
    }

    public void d(String str) {
        FileLog.d("[webviewcontainer] #" + this.tag + " " + str);
    }
}
