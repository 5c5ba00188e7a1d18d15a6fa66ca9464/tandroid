package org.telegram.ui.Components;

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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MrzRecognizer;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SvgHelper;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_attachMenuBot;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotIcon;
import org.telegram.tgnet.TLRPC$TL_attachMenuBotsBot;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputInvoiceSlug;
import org.telegram.tgnet.TLRPC$TL_messages_getAttachMenuBot;
import org.telegram.tgnet.TLRPC$TL_payments_getPaymentForm;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.CameraScanActivity;
import org.telegram.ui.Components.BotWebViewContainer;
import org.telegram.ui.Components.ChatAttachAlertBotWebViewLayout;
import org.telegram.ui.Components.voip.CellFlickerDrawable;
/* loaded from: classes3.dex */
public class BotWebViewContainer extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private static final List<String> WHITELISTED_SCHEMES = Arrays.asList("http", "https");
    private TLRPC$User botUser;
    private String buttonData;
    private BottomSheet cameraBottomSheet;
    private int currentAccount;
    private AlertDialog currentDialog;
    private String currentPaymentSlug;
    private Delegate delegate;
    private int dialogSequentialOpenTimes;
    private CellFlickerDrawable flickerDrawable;
    private BackupImageView flickerView;
    private boolean hasQRPending;
    private boolean hasUserPermissions;
    private boolean isBackButtonVisible;
    private boolean isFlickeringCenter;
    private boolean isPageLoaded;
    private boolean isRequestingPageOpen;
    private boolean isViewPortByMeasureSuppressed;
    private int lastButtonColor;
    private String lastButtonText;
    private int lastButtonTextColor;
    private long lastClickMs;
    private long lastDialogClosed;
    private long lastDialogCooldownTime;
    private boolean lastExpanded;
    private String lastQrText;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mUrl;
    private Runnable onPermissionsRequestResultCallback;
    private Activity parentActivity;
    private Theme.ResourcesProvider resourcesProvider;
    private WebView webView;
    private boolean webViewNotAvailable;
    private TextView webViewNotAvailableText;
    private Consumer<Float> webViewProgressListener;
    private WebViewScrollListener webViewScrollListener;

    /* loaded from: classes3.dex */
    public interface Delegate {

        /* loaded from: classes3.dex */
        public final /* synthetic */ class -CC {
            public static boolean $default$isClipboardAvailable(Delegate delegate) {
                return false;
            }

            public static void $default$onSendWebViewData(Delegate delegate, String str) {
            }

            public static void $default$onWebAppReady(Delegate delegate) {
            }
        }

        boolean isClipboardAvailable();

        void onCloseRequested(Runnable runnable);

        void onSendWebViewData(String str);

        void onSetBackButtonVisible(boolean z);

        void onSetupMainButton(boolean z, boolean z2, String str, int i, int i2, boolean z3);

        void onWebAppExpand();

        void onWebAppOpenInvoice(String str, TLObject tLObject);

        void onWebAppReady();

        void onWebAppSetActionBarColor(String str);

        void onWebAppSetBackgroundColor(int i);

        void onWebAppSetupClosingBehavior(boolean z);
    }

    /* loaded from: classes3.dex */
    public interface WebViewScrollListener {
        void onWebViewScrolled(WebView webView, int i, int i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$evaluateJs$6(String str) {
    }

    public BotWebViewContainer(Context context, Theme.ResourcesProvider resourcesProvider, int i) {
        super(context);
        this.flickerDrawable = new CellFlickerDrawable();
        this.lastButtonColor = getColor("featuredStickers_addButton");
        this.lastButtonTextColor = getColor("featuredStickers_buttonText");
        this.lastButtonText = "";
        this.resourcesProvider = resourcesProvider;
        if (context instanceof Activity) {
            this.parentActivity = (Activity) context;
        }
        CellFlickerDrawable cellFlickerDrawable = this.flickerDrawable;
        cellFlickerDrawable.drawFrame = false;
        cellFlickerDrawable.setColors(i, 153, 204);
        BackupImageView backupImageView = new BackupImageView(context) { // from class: org.telegram.ui.Components.BotWebViewContainer.1
            {
                this.imageReceiver = new 1(this);
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            /* loaded from: classes3.dex */
            public class 1 extends ImageReceiver {
                1(View view) {
                    super(view);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.messenger.ImageReceiver
                public boolean setImageBitmapByKey(Drawable drawable, String str, int i, boolean z, int i2) {
                    boolean imageBitmapByKey = super.setImageBitmapByKey(drawable, str, i, z, i2);
                    ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(300L);
                    duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.BotWebViewContainer$1$1$$ExternalSyntheticLambda0
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            BotWebViewContainer.1.1.this.lambda$setImageBitmapByKey$0(valueAnimator);
                        }
                    });
                    duration.start();
                    return imageBitmapByKey;
                }

                /* JADX INFO: Access modifiers changed from: private */
                public /* synthetic */ void lambda$setImageBitmapByKey$0(ValueAnimator valueAnimator) {
                    1.this.imageReceiver.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
                    invalidate();
                }
            }

            @Override // org.telegram.ui.Components.BackupImageView, android.view.View
            protected void onDraw(Canvas canvas) {
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
        backupImageView.setColorFilter(new PorterDuffColorFilter(getColor("dialogSearchHint"), PorterDuff.Mode.SRC_IN));
        this.flickerView.getImageReceiver().setAspectFit(true);
        addView(this.flickerView, LayoutHelper.createFrame(-1, -2, 48));
        TextView textView = new TextView(context);
        this.webViewNotAvailableText = textView;
        textView.setText(LocaleController.getString(R.string.BotWebViewNotAvailablePlaceholder));
        this.webViewNotAvailableText.setTextColor(getColor("windowBackgroundWhiteGrayText"));
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
            setupWebView();
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

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void setupWebView() {
        WebView webView = this.webView;
        if (webView != null) {
            webView.destroy();
            removeView(this.webView);
        }
        WebView webView2 = new WebView(getContext()) { // from class: org.telegram.ui.Components.BotWebViewContainer.2
            private int prevScrollX;
            private int prevScrollY;

            @Override // android.webkit.WebView, android.view.View
            protected void onScrollChanged(int i, int i2, int i3, int i4) {
                super.onScrollChanged(i, i2, i3, i4);
                if (BotWebViewContainer.this.webViewScrollListener != null) {
                    BotWebViewContainer.this.webViewScrollListener.onWebViewScrolled(this, getScrollX() - this.prevScrollX, getScrollY() - this.prevScrollY);
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
                return BotWebViewContainer.this.isFocusable();
            }

            @Override // android.webkit.WebView, android.widget.AbsoluteLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i2), 1073741824));
            }

            @Override // android.webkit.WebView, android.view.View
            @SuppressLint({"ClickableViewAccessibility"})
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    BotWebViewContainer.this.lastClickMs = System.currentTimeMillis();
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.webView = webView2;
        webView2.setBackgroundColor(getColor("windowBackgroundWhite"));
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        File file = new File(ApplicationLoader.getFilesDirFixed(), "webview_database");
        if ((file.exists() && file.isDirectory()) || file.mkdirs()) {
            settings.setDatabasePath(file.getAbsolutePath());
        }
        GeolocationPermissions.getInstance().clearAll();
        this.webView.setVerticalScrollBarEnabled(false);
        this.webView.setWebViewClient(new WebViewClient() { // from class: org.telegram.ui.Components.BotWebViewContainer.3
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView3, String str) {
                Uri parse = Uri.parse(BotWebViewContainer.this.mUrl);
                Uri parse2 = Uri.parse(str);
                if (!BotWebViewContainer.this.isPageLoaded || (Objects.equals(parse.getHost(), parse2.getHost()) && Objects.equals(parse.getPath(), parse2.getPath()))) {
                    return false;
                }
                if (BotWebViewContainer.WHITELISTED_SCHEMES.contains(parse2.getScheme())) {
                    BotWebViewContainer.this.onOpenUri(parse2);
                    return true;
                }
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onPageFinished(WebView webView3, String str) {
                BotWebViewContainer.this.setPageLoaded(str);
            }
        });
        this.webView.setWebChromeClient(new 4());
        this.webView.setAlpha(0.0f);
        addView(this.webView);
        if (Build.VERSION.SDK_INT >= 17) {
            this.webView.addJavascriptInterface(new WebViewProxy(), "TelegramWebviewProxy");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 4 extends WebChromeClient {
        private Dialog lastPermissionsDialog;

        4() {
        }

        @Override // android.webkit.WebChromeClient
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            Context context = BotWebViewContainer.this.getContext();
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (BotWebViewContainer.this.mFilePathCallback != null) {
                    BotWebViewContainer.this.mFilePathCallback.onReceiveValue(null);
                }
                BotWebViewContainer.this.mFilePathCallback = valueCallback;
                if (Build.VERSION.SDK_INT >= 21) {
                    activity.startActivityForResult(fileChooserParams.createIntent(), 3000);
                    return true;
                }
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.addCategory("android.intent.category.OPENABLE");
                intent.setType("*/*");
                activity.startActivityForResult(Intent.createChooser(intent, LocaleController.getString(R.string.BotWebViewFileChooserTitle)), 3000);
                return true;
            }
            return false;
        }

        @Override // android.webkit.WebChromeClient
        public void onProgressChanged(WebView webView, int i) {
            if (BotWebViewContainer.this.webViewProgressListener != null) {
                BotWebViewContainer.this.webViewProgressListener.accept(Float.valueOf(i / 100.0f));
            }
        }

        @Override // android.webkit.WebChromeClient
        public void onGeolocationPermissionsShowPrompt(final String str, final GeolocationPermissions.Callback callback) {
            if (BotWebViewContainer.this.parentActivity != null) {
                Dialog createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(BotWebViewContainer.this.parentActivity, BotWebViewContainer.this.resourcesProvider, new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, R.raw.permission_request_location, LocaleController.formatString(R.string.BotWebViewRequestGeolocationPermission, UserObject.getUserName(BotWebViewContainer.this.botUser)), LocaleController.formatString(R.string.BotWebViewRequestGeolocationPermissionWithHint, UserObject.getUserName(BotWebViewContainer.this.botUser)), new Consumer() { // from class: org.telegram.ui.Components.BotWebViewContainer$4$$ExternalSyntheticLambda1
                    @Override // androidx.core.util.Consumer
                    public final void accept(Object obj) {
                        BotWebViewContainer.4.this.lambda$onGeolocationPermissionsShowPrompt$1(callback, str, (Boolean) obj);
                    }
                });
                this.lastPermissionsDialog = createWebViewPermissionsRequestDialog;
                createWebViewPermissionsRequestDialog.show();
                return;
            }
            callback.invoke(str, false, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onGeolocationPermissionsShowPrompt$1(final GeolocationPermissions.Callback callback, final String str, Boolean bool) {
            if (this.lastPermissionsDialog != null) {
                this.lastPermissionsDialog = null;
                if (bool.booleanValue()) {
                    BotWebViewContainer.this.runWithPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, new Consumer() { // from class: org.telegram.ui.Components.BotWebViewContainer$4$$ExternalSyntheticLambda0
                        @Override // androidx.core.util.Consumer
                        public final void accept(Object obj) {
                            BotWebViewContainer.4.this.lambda$onGeolocationPermissionsShowPrompt$0(callback, str, (Boolean) obj);
                        }
                    });
                } else {
                    callback.invoke(str, false, false);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onGeolocationPermissionsShowPrompt$0(GeolocationPermissions.Callback callback, String str, Boolean bool) {
            callback.invoke(str, bool.booleanValue(), false);
            if (bool.booleanValue()) {
                BotWebViewContainer.this.hasUserPermissions = true;
            }
        }

        @Override // android.webkit.WebChromeClient
        public void onGeolocationPermissionsHidePrompt() {
            Dialog dialog = this.lastPermissionsDialog;
            if (dialog != null) {
                dialog.dismiss();
                this.lastPermissionsDialog = null;
            }
        }

        @Override // android.webkit.WebChromeClient
        public void onPermissionRequest(final PermissionRequest permissionRequest) {
            Dialog dialog = this.lastPermissionsDialog;
            if (dialog != null) {
                dialog.dismiss();
                this.lastPermissionsDialog = null;
            }
            String[] resources = permissionRequest.getResources();
            if (resources.length == 1) {
                final String str = resources[0];
                if (BotWebViewContainer.this.parentActivity == null) {
                    permissionRequest.deny();
                    return;
                }
                str.hashCode();
                if (str.equals("android.webkit.resource.VIDEO_CAPTURE")) {
                    Dialog createWebViewPermissionsRequestDialog = AlertsCreator.createWebViewPermissionsRequestDialog(BotWebViewContainer.this.parentActivity, BotWebViewContainer.this.resourcesProvider, new String[]{"android.permission.CAMERA"}, R.raw.permission_request_camera, LocaleController.formatString(R.string.BotWebViewRequestCameraPermission, UserObject.getUserName(BotWebViewContainer.this.botUser)), LocaleController.formatString(R.string.BotWebViewRequestCameraPermissionWithHint, UserObject.getUserName(BotWebViewContainer.this.botUser)), new Consumer() { // from class: org.telegram.ui.Components.BotWebViewContainer$4$$ExternalSyntheticLambda2
                        @Override // androidx.core.util.Consumer
                        public final void accept(Object obj) {
                            BotWebViewContainer.4.this.lambda$onPermissionRequest$5(permissionRequest, str, (Boolean) obj);
                        }
                    });
                    this.lastPermissionsDialog = createWebViewPermissionsRequestDialog;
                    createWebViewPermissionsRequestDialog.show();
                } else if (str.equals("android.webkit.resource.AUDIO_CAPTURE")) {
                    Dialog createWebViewPermissionsRequestDialog2 = AlertsCreator.createWebViewPermissionsRequestDialog(BotWebViewContainer.this.parentActivity, BotWebViewContainer.this.resourcesProvider, new String[]{"android.permission.RECORD_AUDIO"}, R.raw.permission_request_microphone, LocaleController.formatString(R.string.BotWebViewRequestMicrophonePermission, UserObject.getUserName(BotWebViewContainer.this.botUser)), LocaleController.formatString(R.string.BotWebViewRequestMicrophonePermissionWithHint, UserObject.getUserName(BotWebViewContainer.this.botUser)), new Consumer() { // from class: org.telegram.ui.Components.BotWebViewContainer$4$$ExternalSyntheticLambda4
                        @Override // androidx.core.util.Consumer
                        public final void accept(Object obj) {
                            BotWebViewContainer.4.this.lambda$onPermissionRequest$3(permissionRequest, str, (Boolean) obj);
                        }
                    });
                    this.lastPermissionsDialog = createWebViewPermissionsRequestDialog2;
                    createWebViewPermissionsRequestDialog2.show();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPermissionRequest$3(final PermissionRequest permissionRequest, final String str, Boolean bool) {
            if (this.lastPermissionsDialog != null) {
                this.lastPermissionsDialog = null;
                if (bool.booleanValue()) {
                    BotWebViewContainer.this.runWithPermissions(new String[]{"android.permission.RECORD_AUDIO"}, new Consumer() { // from class: org.telegram.ui.Components.BotWebViewContainer$4$$ExternalSyntheticLambda5
                        @Override // androidx.core.util.Consumer
                        public final void accept(Object obj) {
                            BotWebViewContainer.4.this.lambda$onPermissionRequest$2(permissionRequest, str, (Boolean) obj);
                        }
                    });
                } else {
                    permissionRequest.deny();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPermissionRequest$2(PermissionRequest permissionRequest, String str, Boolean bool) {
            if (bool.booleanValue()) {
                permissionRequest.grant(new String[]{str});
                BotWebViewContainer.this.hasUserPermissions = true;
                return;
            }
            permissionRequest.deny();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPermissionRequest$5(final PermissionRequest permissionRequest, final String str, Boolean bool) {
            if (this.lastPermissionsDialog != null) {
                this.lastPermissionsDialog = null;
                if (bool.booleanValue()) {
                    BotWebViewContainer.this.runWithPermissions(new String[]{"android.permission.CAMERA"}, new Consumer() { // from class: org.telegram.ui.Components.BotWebViewContainer$4$$ExternalSyntheticLambda3
                        @Override // androidx.core.util.Consumer
                        public final void accept(Object obj) {
                            BotWebViewContainer.4.this.lambda$onPermissionRequest$4(permissionRequest, str, (Boolean) obj);
                        }
                    });
                } else {
                    permissionRequest.deny();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPermissionRequest$4(PermissionRequest permissionRequest, String str, Boolean bool) {
            if (bool.booleanValue()) {
                permissionRequest.grant(new String[]{str});
                BotWebViewContainer.this.hasUserPermissions = true;
                return;
            }
            permissionRequest.deny();
        }

        @Override // android.webkit.WebChromeClient
        public void onPermissionRequestCanceled(PermissionRequest permissionRequest) {
            Dialog dialog = this.lastPermissionsDialog;
            if (dialog != null) {
                dialog.dismiss();
                this.lastPermissionsDialog = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onOpenUri(Uri uri) {
        onOpenUri(uri, false, false);
    }

    private void onOpenUri(final Uri uri, final boolean z, boolean z2) {
        if (this.isRequestingPageOpen) {
            return;
        }
        if (System.currentTimeMillis() - this.lastClickMs <= 10000 || !z2) {
            this.lastClickMs = 0L;
            boolean[] zArr = {false};
            if (!Browser.isInternalUri(uri, zArr) || zArr[0]) {
                if (z2) {
                    Browser.openUrl(getContext(), uri, true, z);
                    return;
                }
                this.isRequestingPageOpen = true;
                new AlertDialog.Builder(getContext(), this.resourcesProvider).setTitle(LocaleController.getString(R.string.OpenUrlTitle)).setMessage(LocaleController.formatString(R.string.OpenUrlAlert2, uri.toString())).setPositiveButton(LocaleController.getString(R.string.Open), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        BotWebViewContainer.this.lambda$onOpenUri$1(uri, z, dialogInterface, i);
                    }
                }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda4
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        BotWebViewContainer.this.lambda$onOpenUri$2(dialogInterface);
                    }
                }).show();
            } else if (this.delegate != null) {
                setDescendantFocusability(393216);
                setFocusable(false);
                this.webView.setFocusable(false);
                this.webView.setDescendantFocusability(393216);
                this.webView.clearFocus();
                ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(getWindowToken(), 2);
                this.delegate.onCloseRequested(new Runnable() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        BotWebViewContainer.this.lambda$onOpenUri$0(uri, z);
                    }
                });
            } else {
                Browser.openUrl(getContext(), uri, true, z);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onOpenUri$0(Uri uri, boolean z) {
        Browser.openUrl(getContext(), uri, true, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onOpenUri$1(Uri uri, boolean z, DialogInterface dialogInterface, int i) {
        Browser.openUrl(getContext(), uri, true, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onOpenUri$2(DialogInterface dialogInterface) {
        this.isRequestingPageOpen = false;
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

    /* JADX INFO: Access modifiers changed from: private */
    public void setPageLoaded(String str) {
        if (this.isPageLoaded) {
            return;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(this.webView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.flickerView, View.ALPHA, 0.0f));
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.BotWebViewContainer.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                BotWebViewContainer.this.flickerView.setVisibility(8);
            }
        });
        animatorSet.start();
        this.mUrl = str;
        this.isPageLoaded = true;
        setFocusable(true);
        this.delegate.onWebAppReady();
    }

    public boolean hasUserPermissions() {
        return this.hasUserPermissions;
    }

    public void setBotUser(TLRPC$User tLRPC$User) {
        this.botUser = tLRPC$User;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runWithPermissions(final String[] strArr, final Consumer<Boolean> consumer) {
        if (Build.VERSION.SDK_INT < 23) {
            consumer.accept(Boolean.TRUE);
        } else if (checkPermissions(strArr)) {
            consumer.accept(Boolean.TRUE);
        } else {
            this.onPermissionsRequestResultCallback = new Runnable() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.this.lambda$runWithPermissions$3(consumer, strArr);
                }
            };
            Activity activity = this.parentActivity;
            if (activity != null) {
                activity.requestPermissions(strArr, 4000);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runWithPermissions$3(Consumer consumer, String[] strArr) {
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

    public WebView getWebView() {
        return this.webView;
    }

    public void loadFlickerAndSettingsItem(int i, long j, final ActionBarMenuSubItem actionBarMenuSubItem) {
        TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot;
        boolean z;
        String publicUsername = UserObject.getPublicUsername(MessagesController.getInstance(i).getUser(Long.valueOf(j)));
        if (publicUsername != null && publicUsername.equals("DurgerKingBot")) {
            this.flickerView.setVisibility(0);
            this.flickerView.setAlpha(1.0f);
            this.flickerView.setImageDrawable(SvgHelper.getDrawable(R.raw.durgerking_placeholder, Integer.valueOf(getColor("windowBackgroundGray"))));
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
            }
            if (actionBarMenuSubItem != null) {
                actionBarMenuSubItem.setVisibility(tLRPC$TL_attachMenuBot.has_settings ? 0 : 8);
                return;
            }
            return;
        }
        TLRPC$TL_messages_getAttachMenuBot tLRPC$TL_messages_getAttachMenuBot = new TLRPC$TL_messages_getAttachMenuBot();
        tLRPC$TL_messages_getAttachMenuBot.bot = MessagesController.getInstance(i).getInputUser(j);
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_getAttachMenuBot, new RequestDelegate() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda12
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                BotWebViewContainer.this.lambda$loadFlickerAndSettingsItem$5(actionBarMenuSubItem, tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFlickerAndSettingsItem$5(final ActionBarMenuSubItem actionBarMenuSubItem, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$loadFlickerAndSettingsItem$4(tLObject, actionBarMenuSubItem);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadFlickerAndSettingsItem$4(TLObject tLObject, ActionBarMenuSubItem actionBarMenuSubItem) {
        boolean z;
        if (!(tLObject instanceof TLRPC$TL_attachMenuBotsBot)) {
            if (actionBarMenuSubItem != null) {
                actionBarMenuSubItem.setVisibility(8);
                return;
            }
            return;
        }
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
        if (actionBarMenuSubItem != null) {
            actionBarMenuSubItem.setVisibility(tLRPC$TL_attachMenuBot.has_settings ? 0 : 8);
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
        checkCreateWebView();
        this.isPageLoaded = false;
        this.lastClickMs = 0L;
        this.hasUserPermissions = false;
        WebView webView = this.webView;
        if (webView != null) {
            webView.reload();
        }
    }

    public void loadUrl(int i, String str) {
        checkCreateWebView();
        this.currentAccount = i;
        this.isPageLoaded = false;
        this.lastClickMs = 0L;
        this.hasUserPermissions = false;
        this.mUrl = str;
        WebView webView = this.webView;
        if (webView != null) {
            webView.loadUrl(str);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.onActivityResultReceived);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.onRequestPermissionResultReceived);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.onActivityResultReceived);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.onRequestPermissionResultReceived);
    }

    public void destroyWebView() {
        WebView webView = this.webView;
        if (webView != null) {
            if (webView.getParent() != null) {
                removeView(this.webView);
            }
            this.webView.destroy();
            this.isPageLoaded = false;
        }
    }

    public boolean isBackButtonVisible() {
        return this.isBackButtonVisible;
    }

    public void evaluateJs(String str, boolean z) {
        if (z) {
            checkCreateWebView();
        }
        WebView webView = this.webView;
        if (webView == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            webView.evaluateJavascript(str, BotWebViewContainer$$ExternalSyntheticLambda6.INSTANCE);
            return;
        }
        try {
            webView.loadUrl("javascript:" + URLEncoder.encode(str, "UTF-8"));
        } catch (UnsupportedEncodingException unused) {
            WebView webView2 = this.webView;
            webView2.loadUrl("javascript:" + URLEncoder.encode(str));
        }
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.didSetNewTheme) {
            WebView webView = this.webView;
            if (webView != null) {
                webView.setBackgroundColor(getColor("windowBackgroundWhite"));
            }
            this.flickerView.setColorFilter(new PorterDuffColorFilter(getColor("dialogSearchHint"), PorterDuff.Mode.SRC_IN));
            notifyThemeChanged();
        } else if (i == NotificationCenter.onActivityResultReceived) {
            onActivityResult(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (Intent) objArr[2]);
        } else if (i == NotificationCenter.onRequestPermissionResultReceived) {
            onRequestPermissionsResult(((Integer) objArr[0]).intValue(), (String[]) objArr[1], (int[]) objArr[2]);
        }
    }

    private void notifyThemeChanged() {
        notifyEvent("theme_changed", buildThemeParams());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyEvent(String str, JSONObject jSONObject) {
        evaluateJs("window.Telegram.WebView.receiveEvent('" + str + "', " + jSONObject + ");", false);
    }

    public void setWebViewScrollListener(WebViewScrollListener webViewScrollListener) {
        this.webViewScrollListener = webViewScrollListener;
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:113:0x018c  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x0192  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0196 A[Catch: JSONException -> 0x019d, TRY_LEAVE, TryCatch #5 {JSONException -> 0x019d, blocks: (B:100:0x015c, B:118:0x0196, B:105:0x0176, B:108:0x0180), top: B:357:0x015c }] */
    /* JADX WARN: Removed duplicated region for block: B:165:0x0259  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x029d  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x02a9 A[Catch: Exception -> 0x030e, TryCatch #0 {Exception -> 0x030e, blocks: (B:147:0x0219, B:225:0x0309, B:168:0x025f, B:169:0x0263, B:190:0x02a3, B:191:0x02a6, B:192:0x02a9, B:176:0x027d, B:179:0x0287, B:182:0x0290, B:194:0x02ad, B:195:0x02b7, B:219:0x02f8, B:220:0x02fb, B:221:0x02fe, B:222:0x0301, B:223:0x0304, B:197:0x02bb, B:200:0x02c5, B:203:0x02ce, B:206:0x02d8, B:209:0x02e2, B:154:0x0238, B:157:0x0242, B:160:0x024c), top: B:351:0x0219 }] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x02ad A[Catch: Exception -> 0x030e, TryCatch #0 {Exception -> 0x030e, blocks: (B:147:0x0219, B:225:0x0309, B:168:0x025f, B:169:0x0263, B:190:0x02a3, B:191:0x02a6, B:192:0x02a9, B:176:0x027d, B:179:0x0287, B:182:0x0290, B:194:0x02ad, B:195:0x02b7, B:219:0x02f8, B:220:0x02fb, B:221:0x02fe, B:222:0x0301, B:223:0x0304, B:197:0x02bb, B:200:0x02c5, B:203:0x02ce, B:206:0x02d8, B:209:0x02e2, B:154:0x0238, B:157:0x0242, B:160:0x024c), top: B:351:0x0219 }] */
    /* JADX WARN: Removed duplicated region for block: B:225:0x0309 A[Catch: Exception -> 0x030e, TRY_LEAVE, TryCatch #0 {Exception -> 0x030e, blocks: (B:147:0x0219, B:225:0x0309, B:168:0x025f, B:169:0x0263, B:190:0x02a3, B:191:0x02a6, B:192:0x02a9, B:176:0x027d, B:179:0x0287, B:182:0x0290, B:194:0x02ad, B:195:0x02b7, B:219:0x02f8, B:220:0x02fb, B:221:0x02fe, B:222:0x0301, B:223:0x0304, B:197:0x02bb, B:200:0x02c5, B:203:0x02ce, B:206:0x02d8, B:209:0x02e2, B:154:0x0238, B:157:0x0242, B:160:0x024c), top: B:351:0x0219 }] */
    /* JADX WARN: Removed duplicated region for block: B:381:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:384:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onEventReceived(String str, String str2) {
        char c;
        char c2;
        BotWebViewVibrationEffect botWebViewVibrationEffect;
        if (this.webView == null || this.delegate == null) {
            return;
        }
        str.hashCode();
        char c3 = 0;
        switch (str.hashCode()) {
            case -1898902656:
                if (str.equals("web_app_close_scan_qr_popup")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1717314938:
                if (str.equals("web_app_open_link")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1693280352:
                if (str.equals("web_app_open_popup")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -1390641887:
                if (str.equals("web_app_open_invoice")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1341039673:
                if (str.equals("web_app_setup_closing_behavior")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1309122684:
                if (str.equals("web_app_open_scan_qr_popup")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1263619595:
                if (str.equals("web_app_request_phone")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -1259935152:
                if (str.equals("web_app_request_theme")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -921083201:
                if (str.equals("web_app_request_viewport")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case -439770054:
                if (str.equals("web_app_open_tg_link")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case -71726289:
                if (str.equals("web_app_close")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -58095910:
                if (str.equals("web_app_ready")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 22015443:
                if (str.equals("web_app_read_text_from_clipboard")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 668142772:
                if (str.equals("web_app_data_send")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case 1011447167:
                if (str.equals("web_app_setup_back_button")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 1273834781:
                if (str.equals("web_app_trigger_haptic_feedback")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 1398490221:
                if (str.equals("web_app_setup_main_button")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case 1917103703:
                if (str.equals("web_app_set_header_color")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case 2001330488:
                if (str.equals("web_app_set_background_color")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case 2139805763:
                if (str.equals("web_app_expand")) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        String str3 = null;
        r8 = null;
        r8 = null;
        BotWebViewVibrationEffect botWebViewVibrationEffect2 = null;
        switch (c) {
            case 0:
                if (this.hasQRPending) {
                    this.cameraBottomSheet.dismiss();
                    return;
                }
                return;
            case 1:
                try {
                    JSONObject jSONObject = new JSONObject(str2);
                    Uri parse = Uri.parse(jSONObject.optString("url"));
                    if (WHITELISTED_SCHEMES.contains(parse.getScheme())) {
                        onOpenUri(parse, jSONObject.optBoolean("try_instant_view"), true);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    FileLog.e(e);
                    return;
                }
            case 2:
                try {
                    if (this.currentDialog != null) {
                        return;
                    }
                    if (System.currentTimeMillis() - this.lastDialogClosed <= 150) {
                        int i = this.dialogSequentialOpenTimes + 1;
                        this.dialogSequentialOpenTimes = i;
                        if (i >= 3) {
                            this.dialogSequentialOpenTimes = 0;
                            this.lastDialogCooldownTime = System.currentTimeMillis();
                            return;
                        }
                    }
                    if (System.currentTimeMillis() - this.lastDialogCooldownTime <= 3000) {
                        return;
                    }
                    JSONObject jSONObject2 = new JSONObject(str2);
                    String optString = jSONObject2.optString("title", null);
                    String string = jSONObject2.getString("message");
                    JSONArray jSONArray = jSONObject2.getJSONArray("buttons");
                    AlertDialog.Builder message = new AlertDialog.Builder(getContext()).setTitle(optString).setMessage(string);
                    ArrayList arrayList = new ArrayList();
                    for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                        arrayList.add(new PopupButton(jSONArray.getJSONObject(i2)));
                    }
                    if (arrayList.size() > 3) {
                        return;
                    }
                    final AtomicBoolean atomicBoolean = new AtomicBoolean();
                    if (arrayList.size() >= 1) {
                        final PopupButton popupButton = (PopupButton) arrayList.get(0);
                        message.setPositiveButton(popupButton.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda1
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i3) {
                                BotWebViewContainer.this.lambda$onEventReceived$9(popupButton, atomicBoolean, dialogInterface, i3);
                            }
                        });
                    }
                    if (arrayList.size() >= 2) {
                        final PopupButton popupButton2 = (PopupButton) arrayList.get(1);
                        message.setNegativeButton(popupButton2.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda3
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i3) {
                                BotWebViewContainer.this.lambda$onEventReceived$10(popupButton2, atomicBoolean, dialogInterface, i3);
                            }
                        });
                    }
                    if (arrayList.size() == 3) {
                        final PopupButton popupButton3 = (PopupButton) arrayList.get(2);
                        message.setNeutralButton(popupButton3.text, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda2
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i3) {
                                BotWebViewContainer.this.lambda$onEventReceived$11(popupButton3, atomicBoolean, dialogInterface, i3);
                            }
                        });
                    }
                    message.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda5
                        @Override // android.content.DialogInterface.OnDismissListener
                        public final void onDismiss(DialogInterface dialogInterface) {
                            BotWebViewContainer.this.lambda$onEventReceived$12(atomicBoolean, dialogInterface);
                        }
                    });
                    this.currentDialog = message.show();
                    if (arrayList.size() >= 1) {
                        PopupButton popupButton4 = (PopupButton) arrayList.get(0);
                        if (popupButton4.textColorKey != null) {
                            ((TextView) this.currentDialog.getButton(-1)).setTextColor(getColor(popupButton4.textColorKey));
                        }
                    }
                    if (arrayList.size() >= 2) {
                        PopupButton popupButton5 = (PopupButton) arrayList.get(1);
                        if (popupButton5.textColorKey != null) {
                            ((TextView) this.currentDialog.getButton(-2)).setTextColor(getColor(popupButton5.textColorKey));
                        }
                    }
                    if (arrayList.size() == 3) {
                        PopupButton popupButton6 = (PopupButton) arrayList.get(2);
                        if (popupButton6.textColorKey != null) {
                            ((TextView) this.currentDialog.getButton(-3)).setTextColor(getColor(popupButton6.textColorKey));
                            return;
                        }
                        return;
                    }
                    return;
                } catch (JSONException e2) {
                    FileLog.e(e2);
                    return;
                }
            case 3:
                try {
                    final String optString2 = new JSONObject(str2).optString("slug");
                    if (this.currentPaymentSlug != null) {
                        onInvoiceStatusUpdate(optString2, "cancelled", true);
                    } else {
                        this.currentPaymentSlug = optString2;
                        TLRPC$TL_payments_getPaymentForm tLRPC$TL_payments_getPaymentForm = new TLRPC$TL_payments_getPaymentForm();
                        TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
                        tLRPC$TL_inputInvoiceSlug.slug = optString2;
                        tLRPC$TL_payments_getPaymentForm.invoice = tLRPC$TL_inputInvoiceSlug;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_getPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda11
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                BotWebViewContainer.this.lambda$onEventReceived$14(optString2, tLObject, tLRPC$TL_error);
                            }
                        });
                    }
                    return;
                } catch (JSONException e3) {
                    FileLog.e(e3);
                    return;
                }
            case 4:
                try {
                    this.delegate.onWebAppSetupClosingBehavior(new JSONObject(str2).optBoolean("need_confirmation"));
                    return;
                } catch (JSONException e4) {
                    FileLog.e(e4);
                    return;
                }
            case 5:
                try {
                    if (!this.hasQRPending && this.parentActivity != null) {
                        this.lastQrText = new JSONObject(str2).optString("text");
                        this.hasQRPending = true;
                        if (Build.VERSION.SDK_INT >= 23 && this.parentActivity.checkSelfPermission("android.permission.CAMERA") != 0) {
                            NotificationCenter.getGlobalInstance().addObserver(new NotificationCenter.NotificationCenterDelegate() { // from class: org.telegram.ui.Components.BotWebViewContainer.6
                                @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
                                public void didReceivedNotification(int i3, int i4, Object... objArr) {
                                    int i5 = NotificationCenter.onRequestPermissionResultReceived;
                                    if (i3 == i5) {
                                        int intValue = ((Integer) objArr[0]).intValue();
                                        int[] iArr = (int[]) objArr[2];
                                        if (intValue == 5000) {
                                            NotificationCenter.getGlobalInstance().removeObserver(this, i5);
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
                } catch (JSONException e5) {
                    FileLog.e(e5);
                    return;
                }
            case 6:
            default:
                return;
            case 7:
                notifyThemeChanged();
                return;
            case '\b':
                if ((getParent() instanceof ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) && ((ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) getParent()).isSwipeInProgress()) {
                    c3 = 1;
                }
                invalidateViewPortHeight(c3 ^ 1, true);
                return;
            case '\t':
                try {
                    String optString3 = new JSONObject(str2).optString("path_full");
                    if (optString3.startsWith("/")) {
                        optString3 = optString3.substring(1);
                    }
                    onOpenUri(Uri.parse("https://t.me/" + optString3));
                    return;
                } catch (JSONException e6) {
                    FileLog.e(e6);
                    return;
                }
            case '\n':
                this.delegate.onCloseRequested(null);
                return;
            case 11:
                setPageLoaded(this.webView.getUrl());
                return;
            case '\f':
                try {
                    String string2 = new JSONObject(str2).getString("req_id");
                    if (this.delegate.isClipboardAvailable() && System.currentTimeMillis() - this.lastClickMs <= 10000) {
                        CharSequence text = ((ClipboardManager) getContext().getSystemService("clipboard")).getText();
                        notifyEvent("clipboard_text_received", new JSONObject().put("req_id", string2).put("data", text != null ? text.toString() : ""));
                        return;
                    }
                    notifyEvent("clipboard_text_received", new JSONObject().put("req_id", string2));
                    return;
                } catch (JSONException e7) {
                    FileLog.e(e7);
                    return;
                }
            case '\r':
                try {
                    this.delegate.onSendWebViewData(new JSONObject(str2).optString("data"));
                    return;
                } catch (JSONException e8) {
                    FileLog.e(e8);
                    return;
                }
            case 14:
                try {
                    boolean optBoolean = new JSONObject(str2).optBoolean("is_visible");
                    if (optBoolean != this.isBackButtonVisible) {
                        this.isBackButtonVisible = optBoolean;
                        this.delegate.onSetBackButtonVisible(optBoolean);
                        return;
                    }
                    return;
                } catch (JSONException e9) {
                    FileLog.e(e9);
                    return;
                }
            case 15:
                try {
                    JSONObject jSONObject3 = new JSONObject(str2);
                    String optString4 = jSONObject3.optString("type");
                    int hashCode = optString4.hashCode();
                    if (hashCode == -1184809658) {
                        if (optString4.equals("impact")) {
                            c2 = 0;
                            if (c2 == 0) {
                            }
                        }
                        c2 = 65535;
                        if (c2 == 0) {
                        }
                    } else if (hashCode != 193071555) {
                        if (hashCode == 595233003 && optString4.equals("notification")) {
                            c2 = 1;
                            if (c2 == 0) {
                                if (c2 == 1) {
                                    String optString5 = jSONObject3.optString("notification_type");
                                    int hashCode2 = optString5.hashCode();
                                    if (hashCode2 == -1867169789) {
                                        if (optString5.equals("success")) {
                                            c3 = 1;
                                            if (c3 != 0) {
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        }
                                        c3 = 65535;
                                        if (c3 != 0) {
                                        }
                                        botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                    } else if (hashCode2 != 96784904) {
                                        if (hashCode2 == 1124446108 && optString5.equals("warning")) {
                                            c3 = 2;
                                            if (c3 != 0) {
                                                botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_ERROR;
                                            } else if (c3 == 1) {
                                                botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_SUCCESS;
                                            } else if (c3 == 2) {
                                                botWebViewVibrationEffect = BotWebViewVibrationEffect.NOTIFICATION_WARNING;
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        }
                                        c3 = 65535;
                                        if (c3 != 0) {
                                        }
                                        botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                    } else {
                                        if (optString5.equals("error")) {
                                            if (c3 != 0) {
                                            }
                                            botWebViewVibrationEffect2 = botWebViewVibrationEffect;
                                        }
                                        c3 = 65535;
                                        if (c3 != 0) {
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
                            String optString6 = jSONObject3.optString("impact_style");
                            switch (optString6.hashCode()) {
                                case -1078030475:
                                    if (optString6.equals("medium")) {
                                        c3 = 1;
                                        break;
                                    }
                                    c3 = 65535;
                                    break;
                                case 3535914:
                                    if (optString6.equals("soft")) {
                                        c3 = 4;
                                        break;
                                    }
                                    c3 = 65535;
                                    break;
                                case 99152071:
                                    if (optString6.equals("heavy")) {
                                        c3 = 2;
                                        break;
                                    }
                                    c3 = 65535;
                                    break;
                                case 102970646:
                                    if (optString6.equals("light")) {
                                        break;
                                    }
                                    c3 = 65535;
                                    break;
                                case 108511787:
                                    if (optString6.equals("rigid")) {
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
                        if (optString4.equals("selection_change")) {
                            c2 = 2;
                            if (c2 == 0) {
                            }
                        }
                        c2 = 65535;
                        if (c2 == 0) {
                        }
                    }
                } catch (Exception e10) {
                    FileLog.e(e10);
                    return;
                }
                FileLog.e(e10);
                return;
            case 16:
                try {
                    JSONObject jSONObject4 = new JSONObject(str2);
                    boolean optBoolean2 = jSONObject4.optBoolean("is_active", false);
                    String trim = jSONObject4.optString("text", this.lastButtonText).trim();
                    boolean z = jSONObject4.optBoolean("is_visible", false) && !TextUtils.isEmpty(trim);
                    int parseColor = jSONObject4.has("color") ? Color.parseColor(jSONObject4.optString("color")) : this.lastButtonColor;
                    int parseColor2 = jSONObject4.has("text_color") ? Color.parseColor(jSONObject4.optString("text_color")) : this.lastButtonTextColor;
                    boolean z2 = jSONObject4.optBoolean("is_progress_visible", false) && z;
                    this.lastButtonColor = parseColor;
                    this.lastButtonTextColor = parseColor2;
                    this.lastButtonText = trim;
                    this.buttonData = str2;
                    this.delegate.onSetupMainButton(z, optBoolean2, trim, parseColor, parseColor2, z2);
                    return;
                } catch (IllegalArgumentException | JSONException e11) {
                    FileLog.e(e11);
                    return;
                }
            case 17:
                try {
                    String string3 = new JSONObject(str2).getString("color_key");
                    int hashCode3 = string3.hashCode();
                    if (hashCode3 != -1265068311) {
                        if (hashCode3 == -210781868 && string3.equals("secondary_bg_color")) {
                            c3 = 1;
                            if (c3 != 0) {
                                str3 = "windowBackgroundWhite";
                            } else if (c3 == 1) {
                                str3 = "windowBackgroundGray";
                            }
                            if (str3 == null) {
                                this.delegate.onWebAppSetActionBarColor(str3);
                                return;
                            }
                            return;
                        }
                        c3 = 65535;
                        if (c3 != 0) {
                        }
                        if (str3 == null) {
                        }
                    } else {
                        if (string3.equals("bg_color")) {
                            if (c3 != 0) {
                            }
                            if (str3 == null) {
                            }
                        }
                        c3 = 65535;
                        if (c3 != 0) {
                        }
                        if (str3 == null) {
                        }
                    }
                } catch (JSONException e12) {
                    FileLog.e(e12);
                    return;
                }
                FileLog.e(e12);
                return;
            case 18:
                try {
                    this.delegate.onWebAppSetBackgroundColor(Color.parseColor(new JSONObject(str2).optString("color", "#ffffff")) | (-16777216));
                    return;
                } catch (IllegalArgumentException | JSONException e13) {
                    FileLog.e(e13);
                    return;
                }
            case 19:
                this.delegate.onWebAppExpand();
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$9(PopupButton popupButton, AtomicBoolean atomicBoolean, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        try {
            notifyEvent("popup_closed", new JSONObject().put("button_id", popupButton.id));
            atomicBoolean.set(true);
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$10(PopupButton popupButton, AtomicBoolean atomicBoolean, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        try {
            notifyEvent("popup_closed", new JSONObject().put("button_id", popupButton.id));
            atomicBoolean.set(true);
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$11(PopupButton popupButton, AtomicBoolean atomicBoolean, DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        try {
            notifyEvent("popup_closed", new JSONObject().put("button_id", popupButton.id));
            atomicBoolean.set(true);
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$12(AtomicBoolean atomicBoolean, DialogInterface dialogInterface) {
        if (!atomicBoolean.get()) {
            notifyEvent("popup_closed", new JSONObject());
        }
        this.currentDialog = null;
        this.lastDialogClosed = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$14(final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.BotWebViewContainer$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewContainer.this.lambda$onEventReceived$13(tLRPC$TL_error, str, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onEventReceived$13(TLRPC$TL_error tLRPC$TL_error, String str, TLObject tLObject) {
        if (tLRPC$TL_error != null) {
            onInvoiceStatusUpdate(str, "failed");
        } else {
            this.delegate.onWebAppOpenInvoice(str, tLObject);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openQrScanActivity() {
        Activity activity = this.parentActivity;
        if (activity == null) {
            return;
        }
        this.cameraBottomSheet = CameraScanActivity.showAsSheet(activity, false, 3, new CameraScanActivity.CameraScanActivityDelegate() { // from class: org.telegram.ui.Components.BotWebViewContainer.7
            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public /* synthetic */ void didFindMrzInfo(MrzRecognizer.Result result) {
                CameraScanActivity.CameraScanActivityDelegate.-CC.$default$didFindMrzInfo(this, result);
            }

            @Override // org.telegram.ui.CameraScanActivity.CameraScanActivityDelegate
            public /* synthetic */ boolean processQr(String str, Runnable runnable) {
                return CameraScanActivity.CameraScanActivityDelegate.-CC.$default$processQr(this, str, runnable);
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
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("bg_color", formatColor("windowBackgroundWhite"));
            jSONObject.put("secondary_bg_color", formatColor("windowBackgroundGray"));
            jSONObject.put("text_color", formatColor("windowBackgroundWhiteBlackText"));
            jSONObject.put("hint_color", formatColor("windowBackgroundWhiteHintText"));
            jSONObject.put("link_color", formatColor("windowBackgroundWhiteLinkText"));
            jSONObject.put("button_color", formatColor("featuredStickers_addButton"));
            jSONObject.put("button_text_color", formatColor("featuredStickers_buttonText"));
            return new JSONObject().put("theme_params", jSONObject);
        } catch (Exception e) {
            FileLog.e(e);
            return new JSONObject();
        }
    }

    private int getColor(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Integer valueOf = Integer.valueOf(resourcesProvider != null ? resourcesProvider.getColor(str).intValue() : Theme.getColor(str));
        if (valueOf == null) {
            valueOf = Integer.valueOf(Theme.getColor(str));
        }
        return valueOf.intValue();
    }

    private String formatColor(String str) {
        int color = getColor(str);
        return "#" + hexFixed(Color.red(color)) + hexFixed(Color.green(color)) + hexFixed(Color.blue(color));
    }

    private String hexFixed(int i) {
        String hexString = Integer.toHexString(i);
        if (hexString.length() < 2) {
            return "0" + hexString;
        }
        return hexString;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class WebViewProxy {
        private WebViewProxy() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$postEvent$0(String str, String str2) {
            BotWebViewContainer.this.onEventReceived(str, str2);
        }

        @JavascriptInterface
        public void postEvent(final String str, final String str2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.BotWebViewContainer$WebViewProxy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewContainer.WebViewProxy.this.lambda$postEvent$0(str, str2);
                }
            });
        }
    }

    /* loaded from: classes3.dex */
    public static final class PopupButton {
        public String id;
        public String text;
        public String textColorKey;

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:32:0x007c  */
        /* JADX WARN: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public PopupButton(JSONObject jSONObject) throws JSONException {
            char c;
            this.id = jSONObject.getString("id");
            String string = jSONObject.getString("type");
            boolean z = true;
            switch (string.hashCode()) {
                case -1829997182:
                    if (string.equals("destructive")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case -1367724422:
                    if (string.equals("cancel")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 3548:
                    if (string.equals("ok")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 94756344:
                    if (string.equals("close")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 1544803905:
                    if (string.equals("default")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            if (c == 2) {
                this.text = LocaleController.getString(R.string.OK);
            } else if (c == 3) {
                this.text = LocaleController.getString(R.string.Close);
            } else if (c != 4) {
                if (c == 5) {
                    this.textColorKey = "dialogTextRed";
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
}
