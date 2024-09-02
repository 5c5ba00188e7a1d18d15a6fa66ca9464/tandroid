package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.net.TokenParser;
import j$.util.Optional;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$InputInvoice;
import org.telegram.tgnet.TLRPC$InputPaymentCredentials;
import org.telegram.tgnet.TLRPC$InputPeer;
import org.telegram.tgnet.TLRPC$InputStorePaymentPurpose;
import org.telegram.tgnet.TLRPC$InputUser;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$PasswordKdfAlgo;
import org.telegram.tgnet.TLRPC$PaymentForm;
import org.telegram.tgnet.TLRPC$PaymentReceipt;
import org.telegram.tgnet.TLRPC$TL_account_confirmPasswordEmail;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_getTmpPassword;
import org.telegram.tgnet.TLRPC$TL_account_passwordInputSettings;
import org.telegram.tgnet.TLRPC$TL_account_resendPasswordEmail;
import org.telegram.tgnet.TLRPC$TL_account_tmpPassword;
import org.telegram.tgnet.TLRPC$TL_account_updatePasswordSettings;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_dataJSON;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputCheckPasswordEmpty;
import org.telegram.tgnet.TLRPC$TL_inputCheckPasswordSRP;
import org.telegram.tgnet.TLRPC$TL_inputInvoiceMessage;
import org.telegram.tgnet.TLRPC$TL_inputInvoiceSlug;
import org.telegram.tgnet.TLRPC$TL_inputInvoiceStars;
import org.telegram.tgnet.TLRPC$TL_inputPaymentCredentialsGooglePay;
import org.telegram.tgnet.TLRPC$TL_inputStorePaymentStarsGift;
import org.telegram.tgnet.TLRPC$TL_inputStorePaymentStarsGiveaway;
import org.telegram.tgnet.TLRPC$TL_inputStorePaymentStarsTopup;
import org.telegram.tgnet.TLRPC$TL_invoice;
import org.telegram.tgnet.TLRPC$TL_labeledPrice;
import org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow;
import org.telegram.tgnet.TLRPC$TL_paymentFormMethod;
import org.telegram.tgnet.TLRPC$TL_paymentRequestedInfo;
import org.telegram.tgnet.TLRPC$TL_paymentSavedCredentialsCard;
import org.telegram.tgnet.TLRPC$TL_payments_clearSavedInfo;
import org.telegram.tgnet.TLRPC$TL_payments_getPaymentReceipt;
import org.telegram.tgnet.TLRPC$TL_payments_paymentReceiptStars;
import org.telegram.tgnet.TLRPC$TL_payments_paymentResult;
import org.telegram.tgnet.TLRPC$TL_payments_paymentVerificationNeeded;
import org.telegram.tgnet.TLRPC$TL_payments_sendPaymentForm;
import org.telegram.tgnet.TLRPC$TL_payments_validateRequestedInfo;
import org.telegram.tgnet.TLRPC$TL_payments_validatedRequestedInfo;
import org.telegram.tgnet.TLRPC$TL_postAddress;
import org.telegram.tgnet.TLRPC$TL_shippingOption;
import org.telegram.tgnet.TLRPC$TL_updateNewChannelMessage;
import org.telegram.tgnet.TLRPC$TL_updateNewMessage;
import org.telegram.tgnet.TLRPC$Update;
import org.telegram.tgnet.TLRPC$Updates;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$account_Password;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.EditTextSettingsCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.PaymentInfoCell;
import org.telegram.ui.Cells.RadioCell;
import org.telegram.ui.Cells.RecurrentPaymentsAcceptCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextPriceCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmbedBottomSheet$$ExternalSyntheticLambda6;
import org.telegram.ui.Components.HintEditText;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.boosts.BoostRepository$$ExternalSyntheticLambda31;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.CountrySelectActivity;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.Stars.StarsIntroActivity;
/* loaded from: classes4.dex */
public class PaymentFormActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private TLRPC$User botUser;
    private TextInfoPrivacyCell[] bottomCell;
    private BottomFrameLayout bottomLayout;
    private boolean canceled;
    private String cardName;
    private TextCheckCell checkCell1;
    private EditTextSettingsCell codeFieldCell;
    private HashMap codesMap;
    private ArrayList countriesArray;
    private HashMap countriesMap;
    private CountrySelectActivity.Country country;
    private String countryName;
    private String currentBotName;
    private String currentItemName;
    private TLRPC$account_Password currentPassword;
    private int currentStep;
    private PaymentFormActivityDelegate delegate;
    private TextDetailSettingsCell[] detailSettingsCell;
    private ArrayList dividers;
    private ActionBarMenuItem doneItem;
    private AnimatorSet doneItemAnimation;
    private boolean donePressed;
    private int emailCodeLength;
    private FrameLayout googlePayButton;
    private FrameLayout googlePayContainer;
    private String googlePayCountryCode;
    private TLRPC$TL_inputPaymentCredentialsGooglePay googlePayCredentials;
    private JSONObject googlePayParameters;
    private String googlePayPublicKey;
    private HeaderCell[] headerCell;
    private boolean ignoreOnCardChange;
    private boolean ignoreOnPhoneChange;
    private boolean ignoreOnTextChange;
    private boolean initGooglePay;
    private EditTextBoldCursor[] inputFields;
    private TLRPC$InputInvoice invoiceInput;
    private String invoiceSlug;
    private InvoiceStatus invoiceStatus;
    private boolean isCheckoutPreview;
    private boolean isWebView;
    private LinearLayout linearLayout2;
    private boolean loadingPasswordInfo;
    private MessageObject messageObject;
    private boolean needPayAfterTransition;
    private boolean need_card_country;
    private boolean need_card_name;
    private boolean need_card_postcode;
    private BaseFragment parentFragment;
    private PaymentFormActivity passwordFragment;
    private boolean passwordOk;
    private TextView payTextView;
    private TLRPC$PaymentForm paymentForm;
    private PaymentFormCallback paymentFormCallback;
    private TLRPC$TL_paymentFormMethod paymentFormMethod;
    private PaymentInfoCell paymentInfoCell;
    private String paymentJson;
    private TLRPC$PaymentReceipt paymentReceipt;
    private boolean paymentStatusSent;
    private PaymentsClient paymentsClient;
    private HashMap phoneFormatMap;
    private ArrayList prices;
    private ContextProgressView progressView;
    private ContextProgressView progressViewButton;
    private String providerApiKey;
    private RadioCell[] radioCells;
    private RecurrentPaymentsAcceptCell recurrentAcceptCell;
    private boolean recurrentAccepted;
    private TLRPC$TL_payments_validatedRequestedInfo requestedInfo;
    private Theme.ResourcesProvider resourcesProvider;
    private boolean saveCardInfo;
    private boolean saveShippingInfo;
    private TLRPC$TL_paymentSavedCredentialsCard savedCredentialsCard;
    private ScrollView scrollView;
    private ShadowSectionCell[] sectionCell;
    private TextSettingsCell[] settingsCell;
    private float shiftDp;
    private TLRPC$TL_shippingOption shippingOption;
    private Runnable shortPollRunnable;
    private boolean shouldNavigateBack;
    private boolean swipeBackEnabled;
    private TextView textView;
    private Long tipAmount;
    private LinearLayout tipLayout;
    private TextPriceCell totalCell;
    private String[] totalPrice;
    private String totalPriceDecimal;
    private TLRPC$TL_payments_validateRequestedInfo validateRequest;
    private boolean waitingForEmail;
    private WebView webView;
    private String webViewUrl;
    private boolean webviewLoading;
    private static final List WEBVIEW_PROTOCOLS = Arrays.asList("http", "https");
    private static final List BLACKLISTED_PROTOCOLS = Collections.singletonList("tg");

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 18 extends WebViewClient {
        final /* synthetic */ Context val$context;

        18(Context context) {
            this.val$context = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRenderProcessGone$0() {
            Browser.openUrl(PaymentFormActivity.this.getContext(), "https://play.google.com/store/apps/details?id=com.google.android.webview");
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            PaymentFormActivity.this.webviewLoading = false;
            PaymentFormActivity.this.showEditDoneProgress(true, false);
            PaymentFormActivity.this.updateSavePaymentField();
        }

        @Override // android.webkit.WebViewClient
        public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
            if (AndroidUtilities.isSafeToShow(PaymentFormActivity.this.getContext())) {
                new AlertDialog.Builder(PaymentFormActivity.this.getContext(), PaymentFormActivity.this.resourcesProvider).setTitle(LocaleController.getString(R.string.ChromeCrashTitle)).setMessage(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.ChromeCrashMessage), new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$18$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.18.this.lambda$onRenderProcessGone$0();
                    }
                })).setPositiveButton(LocaleController.getString(R.string.OK), null).show();
                return true;
            }
            return true;
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            try {
                Uri parse = Uri.parse(str);
                if ("t.me".equals(parse.getHost())) {
                    PaymentFormActivity.this.goToNextStep();
                    return true;
                } else if (PaymentFormActivity.BLACKLISTED_PROTOCOLS.contains(parse.getScheme())) {
                    return true;
                } else {
                    if (PaymentFormActivity.WEBVIEW_PROTOCOLS.contains(parse.getScheme())) {
                        return false;
                    }
                    try {
                        if (PaymentFormActivity.this.getContext() instanceof Activity) {
                            ((Activity) PaymentFormActivity.this.getContext()).startActivityForResult(new Intent("android.intent.action.VIEW", parse), NotificationCenter.starGiftOptionsLoaded);
                        }
                    } catch (ActivityNotFoundException unused) {
                        new AlertDialog.Builder(this.val$context).setTitle(PaymentFormActivity.this.currentBotName).setMessage(LocaleController.getString(R.string.PaymentAppNotFoundForDeeplink)).setPositiveButton(LocaleController.getString(R.string.OK), null).show();
                    }
                    return true;
                }
            } catch (Exception unused2) {
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 25 implements TokenCallback {
        25() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSuccess$0() {
            PaymentFormActivity.this.goToNextStep();
            PaymentFormActivity.this.showEditDoneProgress(true, false);
            PaymentFormActivity.this.setDonePressed(false);
        }

        @Override // com.stripe.android.TokenCallback
        public void onError(Exception exc) {
            if (PaymentFormActivity.this.canceled) {
                return;
            }
            PaymentFormActivity.this.showEditDoneProgress(true, false);
            PaymentFormActivity.this.setDonePressed(false);
            if ((exc instanceof APIConnectionException) || (exc instanceof APIException)) {
                AlertsCreator.showSimpleToast(PaymentFormActivity.this, LocaleController.getString(R.string.PaymentConnectionFailed));
            } else {
                AlertsCreator.showSimpleToast(PaymentFormActivity.this, exc.getMessage());
            }
        }

        @Override // com.stripe.android.TokenCallback
        public void onSuccess(Token token) {
            if (PaymentFormActivity.this.canceled) {
                return;
            }
            PaymentFormActivity.this.paymentJson = String.format(Locale.US, "{\"type\":\"%1$s\", \"id\":\"%2$s\"}", token.getType(), token.getId());
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$25$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.25.this.lambda$onSuccess$0();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 6 extends WebViewClient {
        final /* synthetic */ Context val$context;

        6(Context context) {
            this.val$context = context;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onRenderProcessGone$0() {
            Browser.openUrl(PaymentFormActivity.this.getContext(), "https://play.google.com/store/apps/details?id=com.google.android.webview");
        }

        @Override // android.webkit.WebViewClient
        public void onLoadResource(WebView webView, String str) {
            super.onLoadResource(webView, str);
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            PaymentFormActivity.this.webviewLoading = false;
            PaymentFormActivity.this.showEditDoneProgress(true, false);
            PaymentFormActivity.this.updateSavePaymentField();
        }

        @Override // android.webkit.WebViewClient
        public boolean onRenderProcessGone(WebView webView, RenderProcessGoneDetail renderProcessGoneDetail) {
            if (AndroidUtilities.isSafeToShow(PaymentFormActivity.this.getContext())) {
                new AlertDialog.Builder(PaymentFormActivity.this.getContext(), PaymentFormActivity.this.resourcesProvider).setTitle(LocaleController.getString(R.string.ChromeCrashTitle)).setMessage(AndroidUtilities.replaceSingleTag(LocaleController.getString(R.string.ChromeCrashMessage), new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$6$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.6.this.lambda$onRenderProcessGone$0();
                    }
                })).setPositiveButton(LocaleController.getString(R.string.OK), null).show();
                return true;
            }
            return true;
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            Uri parse;
            PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
            paymentFormActivity.shouldNavigateBack = !str.equals(paymentFormActivity.webViewUrl);
            try {
                parse = Uri.parse(str);
            } catch (Exception unused) {
            }
            if ("t.me".equals(parse.getHost())) {
                PaymentFormActivity.this.goToNextStep();
                return true;
            } else if (PaymentFormActivity.BLACKLISTED_PROTOCOLS.contains(parse.getScheme())) {
                return true;
            } else {
                if (!PaymentFormActivity.WEBVIEW_PROTOCOLS.contains(parse.getScheme())) {
                    try {
                        if (PaymentFormActivity.this.getContext() instanceof Activity) {
                            ((Activity) PaymentFormActivity.this.getContext()).startActivityForResult(new Intent("android.intent.action.VIEW", parse), NotificationCenter.starGiftOptionsLoaded);
                        }
                    } catch (ActivityNotFoundException unused2) {
                        new AlertDialog.Builder(this.val$context).setTitle(PaymentFormActivity.this.currentBotName).setMessage(LocaleController.getString(R.string.PaymentAppNotFoundForDeeplink)).setPositiveButton(LocaleController.getString(R.string.OK), null).show();
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView, str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class BottomFrameLayout extends FrameLayout {
        Paint paint;
        float progress;
        SpringAnimation springAnimation;

        public BottomFrameLayout(Context context, TLRPC$PaymentForm tLRPC$PaymentForm) {
            super(context);
            this.paint = new Paint(1);
            setWillNotDraw(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setChecked$0(DynamicAnimation dynamicAnimation, float f, float f2) {
            this.progress = f / 100.0f;
            if (PaymentFormActivity.this.payTextView != null) {
                PaymentFormActivity.this.payTextView.setAlpha((this.progress * 0.2f) + 0.8f);
            }
            invalidate();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setChecked$1(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            if (dynamicAnimation == this.springAnimation) {
                this.springAnimation = null;
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(PaymentFormActivity.this.getThemedColor(Theme.key_switchTrackBlue));
            this.paint.setColor(PaymentFormActivity.this.getThemedColor(Theme.key_contacts_inviteBackground));
            canvas.drawCircle(LocaleController.isRTL ? getWidth() - AndroidUtilities.dp(28.0f) : AndroidUtilities.dp(28.0f), -AndroidUtilities.dp(28.0f), Math.max(getWidth(), getHeight()) * this.progress, this.paint);
        }

        public void setChecked(boolean z, boolean z2) {
            SpringAnimation springAnimation = this.springAnimation;
            if (springAnimation != null) {
                springAnimation.cancel();
            }
            float f = z ? 1.0f : 0.0f;
            if (!z2) {
                this.progress = f;
                if (PaymentFormActivity.this.payTextView != null) {
                    PaymentFormActivity.this.payTextView.setAlpha((this.progress * 0.2f) + 0.8f);
                }
                invalidate();
            } else if (this.progress == f) {
            } else {
                SpringAnimation spring = new SpringAnimation(new FloatValueHolder(this.progress * 100.0f)).setSpring(new SpringForce(f * 100.0f).setStiffness(z ? 500.0f : 650.0f).setDampingRatio(1.0f));
                this.springAnimation = spring;
                spring.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.PaymentFormActivity$BottomFrameLayout$$ExternalSyntheticLambda0
                    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
                    public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f2, float f3) {
                        PaymentFormActivity.BottomFrameLayout.this.lambda$setChecked$0(dynamicAnimation, f2, f3);
                    }
                });
                this.springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.PaymentFormActivity$BottomFrameLayout$$ExternalSyntheticLambda1
                    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z3, float f2, float f3) {
                        PaymentFormActivity.BottomFrameLayout.this.lambda$setChecked$1(dynamicAnimation, z3, f2, f3);
                    }
                });
                this.springAnimation.start();
            }
        }
    }

    /* loaded from: classes4.dex */
    public enum InvoiceStatus {
        PAID,
        CANCELLED,
        PENDING,
        FAILED
    }

    /* loaded from: classes4.dex */
    public class LinkSpan extends ClickableSpan {
        public LinkSpan() {
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(View view) {
            PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
            paymentFormActivity.presentFragment(new TwoStepVerificationSetupActivity(6, paymentFormActivity.currentPassword));
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public interface PaymentFormActivityDelegate {

        /* loaded from: classes4.dex */
        public abstract /* synthetic */ class -CC {
            public static void $default$currentPasswordUpdated(PaymentFormActivityDelegate paymentFormActivityDelegate, TLRPC$account_Password tLRPC$account_Password) {
            }

            public static void $default$didSelectNewAddress(PaymentFormActivityDelegate paymentFormActivityDelegate, TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
            }

            public static boolean $default$didSelectNewCard(PaymentFormActivityDelegate paymentFormActivityDelegate, String str, String str2, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                return false;
            }

            public static void $default$onFragmentDestroyed(PaymentFormActivityDelegate paymentFormActivityDelegate) {
            }
        }

        void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password);

        void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo);

        boolean didSelectNewCard(String str, String str2, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard);

        void onFragmentDestroyed();
    }

    /* loaded from: classes4.dex */
    public interface PaymentFormCallback {
        void onInvoiceStatusChanged(InvoiceStatus invoiceStatus);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class TelegramWebviewProxy {
        private TelegramWebviewProxy() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$postEvent$0(String str, String str2) {
            if (PaymentFormActivity.this.getParentActivity() != null && str.equals("payment_form_submit")) {
                try {
                    JSONObject jSONObject = new JSONObject(str2);
                    JSONObject jSONObject2 = jSONObject.getJSONObject("credentials");
                    PaymentFormActivity.this.paymentJson = jSONObject2.toString();
                    PaymentFormActivity.this.cardName = jSONObject.getString("title");
                } catch (Throwable th) {
                    PaymentFormActivity.this.paymentJson = str2;
                    FileLog.e(th);
                }
                PaymentFormActivity.this.goToNextStep();
            }
        }

        @JavascriptInterface
        public void postEvent(final String str, final String str2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$TelegramWebviewProxy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.TelegramWebviewProxy.this.lambda$postEvent$0(str, str2);
                }
            });
        }
    }

    private PaymentFormActivity(TLRPC$InputInvoice tLRPC$InputInvoice, TLRPC$PaymentForm tLRPC$PaymentForm, MessageObject messageObject, String str, int i, TLRPC$TL_payments_validatedRequestedInfo tLRPC$TL_payments_validatedRequestedInfo, TLRPC$TL_shippingOption tLRPC$TL_shippingOption, Long l, String str2, String str3, TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, BaseFragment baseFragment) {
        this.countriesArray = new ArrayList();
        this.countriesMap = new HashMap();
        this.codesMap = new HashMap();
        this.phoneFormatMap = new HashMap();
        this.swipeBackEnabled = true;
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.shiftDp = -4.5f;
        this.emailCodeLength = 6;
        init(tLRPC$InputInvoice, tLRPC$PaymentForm, messageObject, str, i, tLRPC$TL_payments_validatedRequestedInfo, tLRPC$TL_shippingOption, l, str2, str3, tLRPC$TL_payments_validateRequestedInfo, z, tLRPC$TL_inputPaymentCredentialsGooglePay, baseFragment);
    }

    public PaymentFormActivity(TLRPC$PaymentForm tLRPC$PaymentForm, String str, BaseFragment baseFragment) {
        this(tLRPC$PaymentForm, null, str, baseFragment);
    }

    public PaymentFormActivity(TLRPC$PaymentForm tLRPC$PaymentForm, MessageObject messageObject, String str, BaseFragment baseFragment) {
        this.countriesArray = new ArrayList();
        this.countriesMap = new HashMap();
        this.codesMap = new HashMap();
        this.phoneFormatMap = new HashMap();
        this.swipeBackEnabled = true;
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.shiftDp = -4.5f;
        this.emailCodeLength = 6;
        this.isCheckoutPreview = true;
        init(null, tLRPC$PaymentForm, messageObject, str, 4, null, null, null, null, null, null, false, null, baseFragment);
    }

    public PaymentFormActivity(TLRPC$PaymentForm tLRPC$PaymentForm, MessageObject messageObject, BaseFragment baseFragment) {
        this(tLRPC$PaymentForm, messageObject, null, baseFragment);
    }

    public PaymentFormActivity(TLRPC$PaymentForm tLRPC$PaymentForm, TLRPC$InputInvoice tLRPC$InputInvoice, BaseFragment baseFragment) {
        this(tLRPC$InputInvoice, tLRPC$PaymentForm, null, null, 4, null, null, null, null, null, null, false, null, baseFragment);
        this.isCheckoutPreview = true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public PaymentFormActivity(TLRPC$PaymentReceipt tLRPC$PaymentReceipt) {
        TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug;
        this.countriesArray = new ArrayList();
        this.countriesMap = new HashMap();
        this.codesMap = new HashMap();
        this.phoneFormatMap = new HashMap();
        this.swipeBackEnabled = true;
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.shiftDp = -4.5f;
        this.emailCodeLength = 6;
        this.currentStep = 5;
        TLRPC$PaymentForm tLRPC$PaymentForm = new TLRPC$PaymentForm();
        this.paymentForm = tLRPC$PaymentForm;
        this.paymentReceipt = tLRPC$PaymentReceipt;
        tLRPC$PaymentForm.bot_id = tLRPC$PaymentReceipt.bot_id;
        tLRPC$PaymentForm.invoice = tLRPC$PaymentReceipt.invoice;
        tLRPC$PaymentForm.provider_id = tLRPC$PaymentReceipt.provider_id;
        tLRPC$PaymentForm.users = tLRPC$PaymentReceipt.users;
        this.shippingOption = tLRPC$PaymentReceipt.shipping;
        long j = tLRPC$PaymentReceipt.tip_amount;
        if (j != 0) {
            this.tipAmount = Long.valueOf(j);
        }
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(tLRPC$PaymentReceipt.bot_id));
        this.botUser = user;
        this.currentBotName = user != null ? user.first_name : "";
        this.currentItemName = tLRPC$PaymentReceipt.title;
        if (tLRPC$PaymentReceipt.info != null) {
            this.validateRequest = new TLRPC$TL_payments_validateRequestedInfo();
            if (this.messageObject != null) {
                TLRPC$TL_inputInvoiceMessage tLRPC$TL_inputInvoiceMessage = new TLRPC$TL_inputInvoiceMessage();
                tLRPC$TL_inputInvoiceMessage.peer = getMessagesController().getInputPeer(tLRPC$PaymentReceipt.bot_id);
                tLRPC$TL_inputInvoiceSlug = tLRPC$TL_inputInvoiceMessage;
            } else {
                TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug2 = new TLRPC$TL_inputInvoiceSlug();
                tLRPC$TL_inputInvoiceSlug2.slug = this.invoiceSlug;
                tLRPC$TL_inputInvoiceSlug = tLRPC$TL_inputInvoiceSlug2;
            }
            this.validateRequest.invoice = tLRPC$TL_inputInvoiceSlug;
            this.validateRequest.info = tLRPC$PaymentReceipt.info;
        }
        this.cardName = tLRPC$PaymentReceipt.credentials_title;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkPassword() {
        if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
            UserConfig.getInstance(this.currentAccount).tmpPassword = null;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        if (UserConfig.getInstance(this.currentAccount).tmpPassword != null) {
            sendData();
        } else if (this.inputFields[1].length() == 0) {
            try {
                this.inputFields[1].performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            AndroidUtilities.shakeViewSpring(this.inputFields[1], 2.5f);
        } else {
            final String obj = this.inputFields[1].getText().toString();
            showEditDoneProgress(true, true);
            setDonePressed(true);
            final TLRPC$TL_account_getPassword tLRPC$TL_account_getPassword = new TLRPC$TL_account_getPassword();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getPassword, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda42
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    PaymentFormActivity.this.lambda$checkPassword$74(obj, tLRPC$TL_account_getPassword, tLObject, tLRPC$TL_error);
                }
            }, 2);
        }
    }

    private void createGooglePayButton(Context context) {
        FrameLayout frameLayout = new FrameLayout(context);
        this.googlePayContainer = frameLayout;
        frameLayout.setBackgroundDrawable(Theme.getSelectorDrawable(true));
        this.googlePayContainer.setVisibility(8);
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.googlePayButton = frameLayout2;
        frameLayout2.setClickable(true);
        this.googlePayButton.setFocusable(true);
        this.googlePayButton.setBackgroundResource(R.drawable.googlepay_button_no_shadow_background);
        if (this.googlePayPublicKey == null) {
            this.googlePayButton.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(2.0f));
        } else {
            this.googlePayButton.setPadding(AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f));
        }
        this.googlePayContainer.addView(this.googlePayButton, LayoutHelper.createFrame(-1, 48.0f));
        this.googlePayButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda37
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PaymentFormActivity.this.lambda$createGooglePayButton$32(view);
            }
        });
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setWeightSum(2.0f);
        linearLayout.setGravity(16);
        linearLayout.setOrientation(1);
        linearLayout.setDuplicateParentStateEnabled(true);
        this.googlePayButton.addView(linearLayout, LayoutHelper.createFrame(-1, -1.0f));
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setDuplicateParentStateEnabled(true);
        imageView.setImageResource(R.drawable.buy_with_googlepay_button_content);
        linearLayout.addView(imageView, LayoutHelper.createLinear(-1, 0, 1.0f));
        ImageView imageView2 = new ImageView(context);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView2.setDuplicateParentStateEnabled(true);
        imageView2.setImageResource(R.drawable.googlepay_button_overlay);
        this.googlePayButton.addView(imageView2, LayoutHelper.createFrame(-1, -1.0f));
    }

    private JSONObject getBaseCardPaymentMethod() {
        List asList = Arrays.asList("AMEX", "DISCOVER", "JCB", "MASTERCARD", "VISA");
        List asList2 = Arrays.asList("PAN_ONLY", "CRYPTOGRAM_3DS");
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("type", "CARD");
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("allowedAuthMethods", new JSONArray((Collection) asList2));
        jSONObject2.put("allowedCardNetworks", new JSONArray((Collection) asList));
        jSONObject.put("parameters", jSONObject2);
        return jSONObject;
    }

    private JSONObject getBaseRequest() {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getResponseBody(InputStream inputStream) {
        String next = new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
        inputStream.close();
        return next;
    }

    private long getStars() {
        TLRPC$InputInvoice tLRPC$InputInvoice = this.invoiceInput;
        if (tLRPC$InputInvoice instanceof TLRPC$TL_inputInvoiceStars) {
            TLRPC$InputStorePaymentPurpose tLRPC$InputStorePaymentPurpose = ((TLRPC$TL_inputInvoiceStars) tLRPC$InputInvoice).purpose;
            if (tLRPC$InputStorePaymentPurpose instanceof TLRPC$TL_inputStorePaymentStarsGift) {
                return ((TLRPC$TL_inputStorePaymentStarsGift) tLRPC$InputStorePaymentPurpose).stars;
            }
            if (tLRPC$InputStorePaymentPurpose instanceof TLRPC$TL_inputStorePaymentStarsTopup) {
                return ((TLRPC$TL_inputStorePaymentStarsTopup) tLRPC$InputStorePaymentPurpose).stars;
            }
            if (tLRPC$InputStorePaymentPurpose instanceof TLRPC$TL_inputStorePaymentStarsGiveaway) {
                return ((TLRPC$TL_inputStorePaymentStarsGiveaway) tLRPC$InputStorePaymentPurpose).stars;
            }
            return 0L;
        }
        return 0L;
    }

    private long getStarsGiftUserId() {
        TLRPC$InputPeer tLRPC$InputPeer;
        TLRPC$InputInvoice tLRPC$InputInvoice = this.invoiceInput;
        if (tLRPC$InputInvoice instanceof TLRPC$TL_inputInvoiceStars) {
            TLRPC$InputStorePaymentPurpose tLRPC$InputStorePaymentPurpose = ((TLRPC$TL_inputInvoiceStars) tLRPC$InputInvoice).purpose;
            if (tLRPC$InputStorePaymentPurpose instanceof TLRPC$TL_inputStorePaymentStarsGift) {
                TLRPC$InputUser tLRPC$InputUser = ((TLRPC$TL_inputStorePaymentStarsGift) tLRPC$InputStorePaymentPurpose).user_id;
                if (tLRPC$InputUser != null) {
                    return tLRPC$InputUser.user_id;
                }
                return 0L;
            } else if (!(tLRPC$InputStorePaymentPurpose instanceof TLRPC$TL_inputStorePaymentStarsGiveaway) || (tLRPC$InputPeer = ((TLRPC$TL_inputStorePaymentStarsGiveaway) tLRPC$InputStorePaymentPurpose).boost_peer) == null) {
                return 0L;
            } else {
                return DialogObject.getPeerDialogId(tLRPC$InputPeer);
            }
        }
        return 0L;
    }

    private String getTotalPriceDecimalString(ArrayList arrayList) {
        long j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            j += ((TLRPC$TL_labeledPrice) arrayList.get(i)).amount;
        }
        return LocaleController.getInstance().formatCurrencyDecimalString(j, this.paymentForm.invoice.currency, false);
    }

    private String getTotalPriceString(ArrayList arrayList) {
        long j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            j += ((TLRPC$TL_labeledPrice) arrayList.get(i)).amount;
        }
        Long l = this.tipAmount;
        if (l != null) {
            j += l.longValue();
        }
        return LocaleController.getInstance().formatCurrencyString(j, this.paymentForm.invoice.currency);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void goToNextStep() {
        int i;
        PaymentFormActivity paymentFormActivity;
        Runnable runnable;
        int i2;
        boolean z;
        PaymentFormActivity paymentFormActivity2;
        int i3 = this.currentStep;
        boolean z2 = true;
        if (i3 == 0) {
            PaymentFormActivityDelegate paymentFormActivityDelegate = this.delegate;
            if (paymentFormActivityDelegate != null) {
                paymentFormActivityDelegate.didSelectNewAddress(this.validateRequest);
                finishFragment();
                return;
            }
            if (this.paymentForm.invoice.flexible) {
                i = 1;
            } else if (this.savedCredentialsCard == null && this.paymentJson == null) {
                i = 2;
            } else {
                if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
                    UserConfig.getInstance(this.currentAccount).tmpPassword = null;
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                }
                i = UserConfig.getInstance(this.currentAccount).tmpPassword != null ? 4 : 3;
            }
            if (i == 2 && this.savedCredentialsCard == null && this.paymentJson == null && !this.paymentForm.additional_methods.isEmpty()) {
                runnable = new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda39
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.this.goToNextStep();
                    }
                };
                showChoosePaymentMethod(runnable);
                return;
            }
            paymentFormActivity = new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, i, this.requestedInfo, null, null, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment);
            presentFragment(paymentFormActivity, this.isWebView);
        }
        if (i3 != 1) {
            if (i3 != 2) {
                if (i3 == 3) {
                    paymentFormActivity2 = new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, this.passwordOk ? 4 : 2, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment);
                    z2 = true;
                } else if (i3 == 4) {
                    if (this.isCheckoutPreview) {
                        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.paymentFinished);
                    }
                    NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.paymentFinished, new Object[0]);
                    if (getMessagesController().newMessageCallback != null) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda38
                            @Override // java.lang.Runnable
                            public final void run() {
                                PaymentFormActivity.this.lambda$goToNextStep$40();
                            }
                        }, 500L);
                        return;
                    } else if (onCheckoutSuccess(getParentLayout(), getParentActivity()) || isFinishing()) {
                        return;
                    }
                } else if (i3 != 6) {
                    return;
                } else {
                    if (!this.delegate.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.googlePayCredentials, this.savedCredentialsCard)) {
                        paymentFormActivity2 = new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, 4, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment);
                    }
                }
                presentFragment(paymentFormActivity2, z2);
                return;
            }
            TLRPC$PaymentForm tLRPC$PaymentForm = this.paymentForm;
            if (tLRPC$PaymentForm.password_missing && (z = this.saveCardInfo)) {
                PaymentFormActivity paymentFormActivity3 = new PaymentFormActivity(this.invoiceInput, tLRPC$PaymentForm, this.messageObject, this.invoiceSlug, 6, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, z, this.googlePayCredentials, this.parentFragment);
                this.passwordFragment = paymentFormActivity3;
                paymentFormActivity3.setCurrentPassword(this.currentPassword);
                this.passwordFragment.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.24
                    @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                    public void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password) {
                        PaymentFormActivity.this.currentPassword = tLRPC$account_Password;
                    }

                    @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                    public /* synthetic */ void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
                        PaymentFormActivityDelegate.-CC.$default$didSelectNewAddress(this, tLRPC$TL_payments_validateRequestedInfo);
                    }

                    @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                    public boolean didSelectNewCard(String str, String str2, boolean z3, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                        if (PaymentFormActivity.this.delegate != null) {
                            PaymentFormActivity.this.delegate.didSelectNewCard(str, str2, z3, tLRPC$TL_inputPaymentCredentialsGooglePay, tLRPC$TL_paymentSavedCredentialsCard);
                        }
                        if (PaymentFormActivity.this.isWebView) {
                            PaymentFormActivity.this.removeSelfFromStack();
                        }
                        return PaymentFormActivity.this.delegate != null;
                    }

                    @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                    public void onFragmentDestroyed() {
                        PaymentFormActivity.this.passwordFragment = null;
                    }
                });
                paymentFormActivity = this.passwordFragment;
            } else {
                PaymentFormActivityDelegate paymentFormActivityDelegate2 = this.delegate;
                if (paymentFormActivityDelegate2 != null) {
                    paymentFormActivityDelegate2.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.googlePayCredentials, null);
                } else {
                    paymentFormActivity = new PaymentFormActivity(this.invoiceInput, tLRPC$PaymentForm, this.messageObject, this.invoiceSlug, 4, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment);
                }
            }
            finishFragment();
            return;
        }
        if (this.paymentJson == null && this.cardName == null) {
            if (this.savedCredentialsCard != null) {
                if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
                    UserConfig.getInstance(this.currentAccount).tmpPassword = null;
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                }
                if (UserConfig.getInstance(this.currentAccount).tmpPassword == null) {
                    i2 = 3;
                }
            } else {
                i2 = 2;
            }
            if (i2 != 2 && this.cardName == null && this.savedCredentialsCard == null && this.paymentJson == null && !this.paymentForm.additional_methods.isEmpty()) {
                runnable = new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda39
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.this.goToNextStep();
                    }
                };
                showChoosePaymentMethod(runnable);
                return;
            }
            paymentFormActivity = new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, i2, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment);
        }
        i2 = 4;
        if (i2 != 2) {
        }
        paymentFormActivity = new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, i2, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment);
        presentFragment(paymentFormActivity, this.isWebView);
    }

    private void init(TLRPC$InputInvoice tLRPC$InputInvoice, TLRPC$PaymentForm tLRPC$PaymentForm, MessageObject messageObject, String str, int i, TLRPC$TL_payments_validatedRequestedInfo tLRPC$TL_payments_validatedRequestedInfo, TLRPC$TL_shippingOption tLRPC$TL_shippingOption, Long l, String str2, String str3, TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, BaseFragment baseFragment) {
        this.currentStep = i;
        this.parentFragment = baseFragment;
        this.paymentJson = str2;
        this.googlePayCredentials = tLRPC$TL_inputPaymentCredentialsGooglePay;
        this.requestedInfo = tLRPC$TL_payments_validatedRequestedInfo;
        this.paymentForm = tLRPC$PaymentForm;
        this.shippingOption = tLRPC$TL_shippingOption;
        this.tipAmount = l;
        this.messageObject = messageObject;
        this.invoiceSlug = str;
        this.invoiceInput = tLRPC$InputInvoice;
        this.saveCardInfo = z;
        this.isWebView = ("stripe".equals(tLRPC$PaymentForm.native_provider) || "smartglocal".equals(this.paymentForm.native_provider)) ? false : true;
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(tLRPC$PaymentForm.bot_id));
        this.botUser = user;
        this.currentBotName = user != null ? user.first_name : "";
        this.currentItemName = tLRPC$PaymentForm.title;
        this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
        this.saveShippingInfo = true;
        if (z || this.currentStep == 4) {
            this.saveCardInfo = z;
        } else {
            this.saveCardInfo = !this.paymentForm.saved_credentials.isEmpty();
        }
        if (str3 != null) {
            this.cardName = str3;
        } else if (this.paymentForm.saved_credentials.isEmpty()) {
        } else {
            TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard = (TLRPC$TL_paymentSavedCredentialsCard) this.paymentForm.saved_credentials.get(0);
            this.savedCredentialsCard = tLRPC$TL_paymentSavedCredentialsCard;
            this.cardName = tLRPC$TL_paymentSavedCredentialsCard.title;
        }
    }

    private void initGooglePay(Context context) {
        IsReadyToPayRequest fromJson;
        if (getParentActivity() == null) {
            return;
        }
        this.paymentsClient = Wallet.getPaymentsClient(context, new Wallet.WalletOptions.Builder().setEnvironment(this.paymentForm.invoice.test ? 3 : 1).setTheme(1).build());
        Optional isReadyToPayRequest = getIsReadyToPayRequest();
        if (isReadyToPayRequest.isPresent() && (fromJson = IsReadyToPayRequest.fromJson(((JSONObject) isReadyToPayRequest.get()).toString())) != null) {
            this.paymentsClient.isReadyToPay(fromJson).addOnCompleteListener(getParentActivity(), new OnCompleteListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda29
                @Override // com.google.android.gms.tasks.OnCompleteListener
                public final void onComplete(Task task) {
                    PaymentFormActivity.this.lambda$initGooglePay$37(task);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPassword$70(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_account_getTmpPassword tLRPC$TL_account_getTmpPassword) {
        showEditDoneProgress(true, false);
        setDonePressed(false);
        if (tLObject != null) {
            this.passwordOk = true;
            UserConfig.getInstance(this.currentAccount).tmpPassword = (TLRPC$TL_account_tmpPassword) tLObject;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            goToNextStep();
        } else if (!tLRPC$TL_error.text.equals("PASSWORD_HASH_INVALID")) {
            AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLRPC$TL_account_getTmpPassword, new Object[0]);
        } else {
            try {
                this.inputFields[1].performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            AndroidUtilities.shakeViewSpring(this.inputFields[1], 3.25f);
            this.inputFields[1].setText("");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPassword$71(final TLRPC$TL_account_getTmpPassword tLRPC$TL_account_getTmpPassword, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda70
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$checkPassword$70(tLObject, tLRPC$TL_error, tLRPC$TL_account_getTmpPassword);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPassword$72(TLRPC$account_Password tLRPC$account_Password, byte[] bArr) {
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = tLRPC$account_Password.current_algo;
        byte[] x = tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow ? SRPHelper.getX(bArr, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo) : null;
        final TLRPC$TL_account_getTmpPassword tLRPC$TL_account_getTmpPassword = new TLRPC$TL_account_getTmpPassword();
        tLRPC$TL_account_getTmpPassword.period = 1800;
        RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda62
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$checkPassword$71(tLRPC$TL_account_getTmpPassword, tLObject, tLRPC$TL_error);
            }
        };
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo2 = tLRPC$account_Password.current_algo;
        if (!(tLRPC$PasswordKdfAlgo2 instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)) {
            TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
            tLRPC$TL_error.text = "PASSWORD_HASH_INVALID";
            requestDelegate.run(null, tLRPC$TL_error);
            return;
        }
        TLRPC$TL_inputCheckPasswordSRP startCheck = SRPHelper.startCheck(x, tLRPC$account_Password.srp_id, tLRPC$account_Password.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo2);
        tLRPC$TL_account_getTmpPassword.password = startCheck;
        if (startCheck != null) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getTmpPassword, requestDelegate, 10);
            return;
        }
        TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
        tLRPC$TL_error2.text = "ALGO_INVALID";
        requestDelegate.run(null, tLRPC$TL_error2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPassword$73(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, String str, TLRPC$TL_account_getPassword tLRPC$TL_account_getPassword) {
        if (tLRPC$TL_error != null) {
            AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLRPC$TL_account_getPassword, new Object[0]);
            showEditDoneProgress(true, false);
            setDonePressed(false);
            return;
        }
        final TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
        if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$account_Password, false)) {
            AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString(R.string.UpdateAppAlert), true);
        } else if (tLRPC$account_Password.has_password) {
            final byte[] stringBytes = AndroidUtilities.getStringBytes(str);
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda60
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$checkPassword$72(tLRPC$account_Password, stringBytes);
                }
            });
        } else {
            this.passwordOk = false;
            goToNextStep();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPassword$74(final String str, final TLRPC$TL_account_getPassword tLRPC$TL_account_getPassword, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda53
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$checkPassword$73(tLRPC$TL_error, tLObject, str, tLRPC$TL_account_getPassword);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGooglePayButton$32(View view) {
        this.googlePayButton.setClickable(false);
        try {
            JSONObject baseRequest = getBaseRequest();
            JSONObject baseCardPaymentMethod = getBaseCardPaymentMethod();
            baseCardPaymentMethod.put("tokenizationSpecification", (this.googlePayPublicKey == null || this.googlePayParameters != null) ? new JSONObject() { // from class: org.telegram.ui.PaymentFormActivity.23
                {
                    put("type", "PAYMENT_GATEWAY");
                    put("parameters", PaymentFormActivity.this.googlePayParameters != null ? PaymentFormActivity.this.googlePayParameters : new JSONObject() { // from class: org.telegram.ui.PaymentFormActivity.23.1
                        {
                            put("gateway", "stripe");
                            put("stripe:publishableKey", PaymentFormActivity.this.providerApiKey);
                            put("stripe:version", "3.5.0");
                        }
                    });
                }
            } : new JSONObject() { // from class: org.telegram.ui.PaymentFormActivity.22
                {
                    put("type", "DIRECT");
                    put("parameters", new JSONObject() { // from class: org.telegram.ui.PaymentFormActivity.22.1
                        {
                            put("protocolVersion", "ECv2");
                            put("publicKey", PaymentFormActivity.this.googlePayPublicKey);
                        }
                    });
                }
            });
            baseRequest.put("allowedPaymentMethods", new JSONArray().put(baseCardPaymentMethod));
            JSONObject jSONObject = new JSONObject();
            ArrayList arrayList = new ArrayList(this.paymentForm.invoice.prices);
            TLRPC$TL_shippingOption tLRPC$TL_shippingOption = this.shippingOption;
            if (tLRPC$TL_shippingOption != null) {
                arrayList.addAll(tLRPC$TL_shippingOption.prices);
            }
            String totalPriceDecimalString = getTotalPriceDecimalString(arrayList);
            this.totalPriceDecimal = totalPriceDecimalString;
            jSONObject.put("totalPrice", totalPriceDecimalString);
            jSONObject.put("totalPriceStatus", "FINAL");
            if (!TextUtils.isEmpty(this.googlePayCountryCode)) {
                jSONObject.put("countryCode", this.googlePayCountryCode);
            }
            jSONObject.put("currencyCode", this.paymentForm.invoice.currency);
            jSONObject.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");
            baseRequest.put("transactionInfo", jSONObject);
            baseRequest.put("merchantInfo", new JSONObject().put("merchantName", this.currentBotName));
            PaymentDataRequest fromJson = PaymentDataRequest.fromJson(baseRequest.toString());
            if (fromJson != null) {
                AutoResolveHelper.resolveTask(this.paymentsClient.loadPaymentData(fromJson), getParentActivity(), 991);
            }
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(CountrySelectActivity.Country country) {
        this.country = country;
        this.inputFields[4].setText(country.name);
        this.countryName = country.shortname;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$1(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setDisableAnonymousNumbers(true);
            countrySelectActivity.setCountrySelectActivityDelegate(new CountrySelectActivity.CountrySelectActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda43
                @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
                public final void didSelectCountry(CountrySelectActivity.Country country) {
                    PaymentFormActivity.this.lambda$createView$0(country);
                }
            });
            presentFragment(countrySelectActivity);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$10(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$11(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            this.doneItem.performClick();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$12(View view) {
        this.passwordOk = false;
        goToNextStep();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$13(View view) {
        this.inputFields[0].requestFocus();
        AndroidUtilities.showKeyboard(this.inputFields[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$14(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            AndroidUtilities.hideKeyboard(textView);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$15(TextView textView, long j, View view) {
        long longValue = ((Long) textView.getTag()).longValue();
        Long l = this.tipAmount;
        if (l == null || longValue != l.longValue()) {
            this.inputFields[0].setText(LocaleController.getInstance().formatCurrencyString(j, false, true, true, this.paymentForm.invoice.currency));
        } else {
            this.ignoreOnTextChange = true;
            this.inputFields[0].setText("");
            this.ignoreOnTextChange = false;
            this.tipAmount = 0L;
            updateTotalPrice();
        }
        EditTextBoldCursor editTextBoldCursor = this.inputFields[0];
        editTextBoldCursor.setSelection(editTextBoldCursor.length());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$16(View view) {
        if (getParentActivity() == null) {
            return;
        }
        showChoosePaymentMethod();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$17(View view) {
        PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        paymentFormActivity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.13
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password) {
                PaymentFormActivityDelegate.-CC.$default$currentPasswordUpdated(this, tLRPC$account_Password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
                PaymentFormActivity.this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
                PaymentFormActivity paymentFormActivity2 = PaymentFormActivity.this;
                paymentFormActivity2.setAddressFields(paymentFormActivity2.validateRequest.info);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                return PaymentFormActivityDelegate.-CC.$default$didSelectNewCard(this, str, str2, z, tLRPC$TL_inputPaymentCredentialsGooglePay, tLRPC$TL_paymentSavedCredentialsCard);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.-CC.$default$onFragmentDestroyed(this);
            }
        });
        presentFragment(paymentFormActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$18(View view) {
        PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        paymentFormActivity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.14
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password) {
                PaymentFormActivityDelegate.-CC.$default$currentPasswordUpdated(this, tLRPC$account_Password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
                PaymentFormActivity.this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
                PaymentFormActivity paymentFormActivity2 = PaymentFormActivity.this;
                paymentFormActivity2.setAddressFields(paymentFormActivity2.validateRequest.info);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                return PaymentFormActivityDelegate.-CC.$default$didSelectNewCard(this, str, str2, z, tLRPC$TL_inputPaymentCredentialsGooglePay, tLRPC$TL_paymentSavedCredentialsCard);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.-CC.$default$onFragmentDestroyed(this);
            }
        });
        presentFragment(paymentFormActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$19(View view) {
        PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        paymentFormActivity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.15
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password) {
                PaymentFormActivityDelegate.-CC.$default$currentPasswordUpdated(this, tLRPC$account_Password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
                PaymentFormActivity.this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
                PaymentFormActivity paymentFormActivity2 = PaymentFormActivity.this;
                paymentFormActivity2.setAddressFields(paymentFormActivity2.validateRequest.info);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                return PaymentFormActivityDelegate.-CC.$default$didSelectNewCard(this, str, str2, z, tLRPC$TL_inputPaymentCredentialsGooglePay, tLRPC$TL_paymentSavedCredentialsCard);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.-CC.$default$onFragmentDestroyed(this);
            }
        });
        presentFragment(paymentFormActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$2(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5) {
            if (i == 6) {
                this.doneItem.performClick();
                return true;
            }
            return false;
        }
        int intValue = ((Integer) textView.getTag()).intValue();
        while (true) {
            intValue++;
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            if (intValue < editTextBoldCursorArr.length) {
                if (intValue != 4 && ((View) editTextBoldCursorArr[intValue].getParent()).getVisibility() == 0) {
                    this.inputFields[intValue].requestFocus();
                    break;
                }
            } else {
                break;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$20(View view) {
        PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        paymentFormActivity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.16
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password) {
                PaymentFormActivityDelegate.-CC.$default$currentPasswordUpdated(this, tLRPC$account_Password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
                PaymentFormActivity.this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
                PaymentFormActivity paymentFormActivity2 = PaymentFormActivity.this;
                paymentFormActivity2.setAddressFields(paymentFormActivity2.validateRequest.info);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                return PaymentFormActivityDelegate.-CC.$default$didSelectNewCard(this, str, str2, z, tLRPC$TL_inputPaymentCredentialsGooglePay, tLRPC$TL_paymentSavedCredentialsCard);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.-CC.$default$onFragmentDestroyed(this);
            }
        });
        presentFragment(paymentFormActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$21(View view) {
        setDonePressed(false);
        view.callOnClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$22(DialogInterface dialogInterface, int i) {
        showPayAlert(this.totalPrice[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0061, code lost:
        if (r7.phone_requested == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x006d, code lost:
        if (r21.googlePayCredentials == null) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0077, code lost:
        if (r4.invoice.flexible != false) goto L32;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$23(String str, final View view) {
        String str2;
        int i;
        RecurrentPaymentsAcceptCell recurrentPaymentsAcceptCell = this.recurrentAcceptCell;
        if (recurrentPaymentsAcceptCell != null && !this.recurrentAccepted) {
            this.shiftDp = -this.shiftDp;
            AndroidUtilities.shakeViewSpring(recurrentPaymentsAcceptCell.getTextView(), this.shiftDp);
            AndroidUtilities.shakeViewSpring(this.recurrentAcceptCell.getCheckBox(), this.shiftDp);
            try {
                this.recurrentAcceptCell.performHapticFeedback(3, 2);
                return;
            } catch (Exception unused) {
                return;
            }
        }
        boolean z = this.isCheckoutPreview;
        if (z && this.paymentForm.saved_info != null && this.validateRequest == null) {
            setDonePressed(true);
            sendSavedForm(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$createView$21(view);
                }
            });
            return;
        }
        if (z) {
            TLRPC$PaymentForm tLRPC$PaymentForm = this.paymentForm;
            TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo = tLRPC$PaymentForm.saved_info;
            if (tLRPC$TL_paymentRequestedInfo == null) {
                TLRPC$TL_invoice tLRPC$TL_invoice = tLRPC$PaymentForm.invoice;
                if (!tLRPC$TL_invoice.shipping_address_requested) {
                    if (!tLRPC$TL_invoice.email_requested) {
                        if (!tLRPC$TL_invoice.name_requested) {
                        }
                    }
                }
                if (tLRPC$TL_paymentRequestedInfo == null) {
                    TLRPC$TL_invoice tLRPC$TL_invoice2 = tLRPC$PaymentForm.invoice;
                    if (tLRPC$TL_invoice2.shipping_address_requested || tLRPC$TL_invoice2.email_requested || tLRPC$TL_invoice2.name_requested || tLRPC$TL_invoice2.phone_requested) {
                        i = 0;
                        if (i == 2 || tLRPC$PaymentForm.additional_methods.isEmpty()) {
                            presentFragment(new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, i, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment));
                            return;
                        }
                        Objects.requireNonNull(view);
                        showChoosePaymentMethod(new EmbedBottomSheet$$ExternalSyntheticLambda6(view));
                        return;
                    }
                }
                i = (this.savedCredentialsCard == null && this.paymentJson == null && this.googlePayCredentials == null) ? 2 : 1;
                if (i == 2) {
                }
                presentFragment(new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, i, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment));
                return;
            }
            if (this.savedCredentialsCard == null) {
                if (this.paymentJson == null) {
                }
            }
            if (this.shippingOption == null) {
            }
        }
        if (!this.paymentForm.password_missing && this.savedCredentialsCard != null) {
            if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
                UserConfig.getInstance(this.currentAccount).tmpPassword = null;
                UserConfig.getInstance(this.currentAccount).saveConfig(false);
            }
            if (UserConfig.getInstance(this.currentAccount).tmpPassword == null) {
                this.needPayAfterTransition = true;
                presentFragment(new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, 3, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment));
                this.needPayAfterTransition = false;
                return;
            } else if (this.isCheckoutPreview) {
                this.isCheckoutPreview = false;
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.paymentFinished);
            }
        }
        TLRPC$User tLRPC$User = this.botUser;
        if (tLRPC$User == null || tLRPC$User.verified) {
            str2 = this.totalPrice[0];
        } else {
            String str3 = "payment_warning_" + this.botUser.id;
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            if (!notificationsSettings.getBoolean(str3, false)) {
                notificationsSettings.edit().putBoolean(str3, true).commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString(R.string.PaymentWarning));
                builder.setMessage(LocaleController.formatString("PaymentWarningText", R.string.PaymentWarningText, this.currentBotName, str));
                builder.setPositiveButton(LocaleController.getString(R.string.Continue), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda31
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        PaymentFormActivity.this.lambda$createView$22(dialogInterface, i2);
                    }
                });
                showDialog(builder.create());
                return;
            }
            str2 = this.totalPrice[0];
        }
        showPayAlert(str2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$24(View view) {
        if (this.donePressed) {
            return;
        }
        boolean z = !this.recurrentAccepted;
        this.recurrentAccepted = z;
        this.recurrentAcceptCell.setChecked(z);
        this.bottomLayout.setChecked(this.recurrentAccepted, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$25(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            sendSavePassword(false);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$26(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$27(View view) {
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_resendPasswordEmail(), new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda41
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.lambda$createView$26(tLObject, tLRPC$TL_error);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(LocaleController.getString(R.string.ResendCodeInfo));
        builder.setTitle(LocaleController.getString(R.string.AppName));
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$28(DialogInterface dialogInterface, int i) {
        sendSavePassword(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$29(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        String string = LocaleController.getString(R.string.TurnPasswordOffQuestion);
        if (this.currentPassword.has_secure_values) {
            string = string + "\n\n" + LocaleController.getString(R.string.TurnPasswordOffPassport);
        }
        builder.setMessage(string);
        builder.setTitle(LocaleController.getString(R.string.TurnPasswordOffQuestionTitle));
        builder.setPositiveButton(LocaleController.getString(R.string.Disable), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda34
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PaymentFormActivity.this.lambda$createView$28(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        AlertDialog create = builder.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(getThemedColor(Theme.key_text_RedBold));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        boolean z = !this.saveShippingInfo;
        this.saveShippingInfo = z;
        this.checkCell1.setChecked(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$30(TextView textView, int i, KeyEvent keyEvent) {
        EditTextBoldCursor editTextBoldCursor;
        if (i == 6) {
            this.doneItem.performClick();
            return true;
        } else if (i == 5) {
            int intValue = ((Integer) textView.getTag()).intValue();
            if (intValue == 0) {
                editTextBoldCursor = this.inputFields[1];
            } else if (intValue != 1) {
                return false;
            } else {
                editTextBoldCursor = this.inputFields[2];
            }
            editTextBoldCursor.requestFocus();
            return false;
        } else {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(View view) {
        boolean z = !this.saveCardInfo;
        this.saveCardInfo = z;
        this.checkCell1.setChecked(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(CountrySelectActivity.Country country) {
        this.country = country;
        this.inputFields[4].setText(country.name);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$6(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setDisableAnonymousNumbers(true);
            countrySelectActivity.setCountrySelectActivityDelegate(new CountrySelectActivity.CountrySelectActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda32
                @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
                public final void didSelectCountry(CountrySelectActivity.Country country) {
                    PaymentFormActivity.this.lambda$createView$5(country);
                }
            });
            presentFragment(countrySelectActivity);
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$7(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5) {
            if (i == 6) {
                this.doneItem.performClick();
                return true;
            }
            return false;
        }
        int intValue = ((Integer) textView.getTag()).intValue();
        while (true) {
            int i2 = intValue + 1;
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            if (i2 < editTextBoldCursorArr.length) {
                intValue = i2 == 4 ? intValue + 2 : i2;
                if (((View) editTextBoldCursorArr[intValue].getParent()).getVisibility() == 0) {
                    this.inputFields[intValue].requestFocus();
                    break;
                }
            } else {
                break;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$8(View view) {
        boolean z = !this.saveCardInfo;
        this.saveCardInfo = z;
        this.checkCell1.setChecked(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$9(View view) {
        int intValue = ((Integer) view.getTag()).intValue();
        int i = 0;
        while (true) {
            RadioCell[] radioCellArr = this.radioCells;
            if (i >= radioCellArr.length) {
                return;
            }
            radioCellArr[i].setChecked(intValue == i, true);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$goToNextStep$40() {
        getMessagesController().newMessageCallback = null;
        if (this.invoiceStatus == InvoiceStatus.PENDING && !isFinishing()) {
            InvoiceStatus invoiceStatus = InvoiceStatus.FAILED;
            this.invoiceStatus = invoiceStatus;
            PaymentFormCallback paymentFormCallback = this.paymentFormCallback;
            if (paymentFormCallback != null) {
                paymentFormCallback.onInvoiceStatusChanged(invoiceStatus);
            }
        } else if (this.invoiceStatus != InvoiceStatus.PAID || isFinishing()) {
            return;
        }
        finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initGooglePay$37(Task task) {
        if (!task.isSuccessful()) {
            FileLog.e("isReadyToPay failed", task.getException());
            return;
        }
        FrameLayout frameLayout = this.googlePayContainer;
        if (frameLayout != null) {
            frameLayout.setVisibility(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPasswordInfo$33() {
        if (this.shortPollRunnable == null) {
            return;
        }
        loadPasswordInfo();
        this.shortPollRunnable = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPasswordInfo$34(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        this.loadingPasswordInfo = false;
        if (tLRPC$TL_error == null) {
            TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            this.currentPassword = tLRPC$account_Password;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$account_Password, false)) {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString(R.string.UpdateAppAlert), true);
                return;
            }
            TLRPC$PaymentForm tLRPC$PaymentForm = this.paymentForm;
            if (tLRPC$PaymentForm != null && this.currentPassword.has_password) {
                tLRPC$PaymentForm.password_missing = false;
                tLRPC$PaymentForm.can_save_credentials = true;
                updateSavePaymentField();
            }
            TwoStepVerificationActivity.initPasswordNewAlgo(this.currentPassword);
            PaymentFormActivity paymentFormActivity = this.passwordFragment;
            if (paymentFormActivity != null) {
                paymentFormActivity.setCurrentPassword(this.currentPassword);
            }
            if (this.currentPassword.has_password || this.shortPollRunnable != null) {
                return;
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$loadPasswordInfo$33();
                }
            };
            this.shortPollRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 5000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPasswordInfo$35(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$loadPasswordInfo$34(tLRPC$TL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r1v5, types: [org.telegram.tgnet.TLRPC$TL_inputPaymentCredentialsGooglePay, org.telegram.tgnet.TLRPC$InputPaymentCredentials] */
    public /* synthetic */ void lambda$onActivityResultFragment$39(int i, Intent intent) {
        String json;
        String optString;
        if (i == -1) {
            PaymentData fromIntent = PaymentData.getFromIntent(intent);
            if (fromIntent == null || (json = fromIntent.toJson()) == null) {
                return;
            }
            try {
                JSONObject jSONObject = new JSONObject(json).getJSONObject("paymentMethodData");
                JSONObject jSONObject2 = jSONObject.getJSONObject("tokenizationData");
                jSONObject2.getString("type");
                String string = jSONObject2.getString("token");
                if (this.googlePayPublicKey == null && this.googlePayParameters == null) {
                    Token parseToken = TokenParser.parseToken(string);
                    this.paymentJson = String.format(Locale.US, "{\"type\":\"%1$s\", \"id\":\"%2$s\"}", parseToken.getType(), parseToken.getId());
                    Card card = parseToken.getCard();
                    optString = card.getBrand() + " *" + card.getLast4();
                    this.cardName = optString;
                    goToNextStep();
                }
                ?? r1 = new TLRPC$InputPaymentCredentials() { // from class: org.telegram.tgnet.TLRPC$TL_inputPaymentCredentialsGooglePay
                    @Override // org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
                        this.payment_token = TLRPC$TL_dataJSON.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
                    }

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                        abstractSerializedData.writeInt32(-1966921727);
                        this.payment_token.serializeToStream(abstractSerializedData);
                    }
                };
                this.googlePayCredentials = r1;
                r1.payment_token = new TLRPC$TL_dataJSON();
                this.googlePayCredentials.payment_token.data = jSONObject2.toString();
                optString = jSONObject.optString("description");
                if (TextUtils.isEmpty(optString)) {
                    optString = "Android Pay";
                }
                this.cardName = optString;
                goToNextStep();
            } catch (JSONException e) {
                FileLog.e(e);
            }
        } else if (i == 1) {
            Status statusFromIntent = AutoResolveHelper.getStatusFromIntent(intent);
            StringBuilder sb = new StringBuilder();
            sb.append("android pay error ");
            sb.append(statusFromIntent != null ? statusFromIntent.getStatusMessage() : "");
            FileLog.e(sb.toString());
        }
        showEditDoneProgress(true, false);
        setDonePressed(false);
        FrameLayout frameLayout = this.googlePayButton;
        if (frameLayout != null) {
            frameLayout.setClickable(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTransitionAnimationEnd$38() {
        this.inputFields[0].requestFocus();
        AndroidUtilities.showKeyboard(this.inputFields[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendData$56(long j) {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment(ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$57(TLObject tLObject) {
        BaseFragment lastFragment;
        if (tLObject instanceof TLRPC$TL_payments_paymentReceiptStars) {
            StarsIntroActivity.showTransactionSheet(getContext(), false, this.currentAccount, (TLRPC$TL_payments_paymentReceiptStars) tLObject, getResourceProvider());
        } else if (!(tLObject instanceof TLRPC$PaymentReceipt) || (lastFragment = LaunchActivity.getLastFragment()) == null) {
        } else {
            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
            bottomSheetParams.transitionFromLeft = true;
            bottomSheetParams.allowNestedScroll = false;
            lastFragment.showAsSheet(new PaymentFormActivity((TLRPC$PaymentReceipt) tLObject), bottomSheetParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$58(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda74
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$sendData$57(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$59(Bulletin bulletin, boolean z, TLRPC$Message[] tLRPC$MessageArr, View view) {
        bulletin.hide();
        if (z) {
            BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
            if (safeLastFragment != null) {
                safeLastFragment.presentFragment(ChatActivity.of(MessageObject.getDialogId(tLRPC$MessageArr[0]), tLRPC$MessageArr[0].id));
                return;
            }
            return;
        }
        TLRPC$TL_payments_getPaymentReceipt tLRPC$TL_payments_getPaymentReceipt = new TLRPC$TL_payments_getPaymentReceipt();
        tLRPC$TL_payments_getPaymentReceipt.msg_id = tLRPC$MessageArr[0].id;
        tLRPC$TL_payments_getPaymentReceipt.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(tLRPC$MessageArr[0].peer_id);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_getPaymentReceipt, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda68
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$sendData$58(tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$60(final TLRPC$Message[] tLRPC$MessageArr) {
        String str;
        TLRPC$Chat chat;
        String string;
        String formatString;
        final Bulletin createSimpleBulletin;
        PaymentFormCallback paymentFormCallback;
        PaymentFormCallback paymentFormCallback2;
        Context context = getContext();
        if (context == null) {
            context = ApplicationLoader.applicationContext;
        }
        if (context == null) {
            context = LaunchActivity.instance;
        }
        if (context == null) {
            return;
        }
        this.paymentStatusSent = true;
        InvoiceStatus invoiceStatus = InvoiceStatus.PAID;
        this.invoiceStatus = invoiceStatus;
        TLRPC$InputInvoice tLRPC$InputInvoice = this.invoiceInput;
        boolean z = tLRPC$InputInvoice instanceof TLRPC$TL_inputInvoiceStars;
        final boolean z2 = z && (((TLRPC$TL_inputInvoiceStars) tLRPC$InputInvoice).purpose instanceof TLRPC$TL_inputStorePaymentStarsGift);
        boolean z3 = z && (((TLRPC$TL_inputInvoiceStars) tLRPC$InputInvoice).purpose instanceof TLRPC$TL_inputStorePaymentStarsGiveaway);
        if (!z && (paymentFormCallback2 = this.paymentFormCallback) != null) {
            paymentFormCallback2.onInvoiceStatusChanged(invoiceStatus);
        }
        goToNextStep();
        if (z && (paymentFormCallback = this.paymentFormCallback) != null) {
            paymentFormCallback.onInvoiceStatusChanged(this.invoiceStatus);
        }
        final long starsGiftUserId = getStarsGiftUserId();
        if (starsGiftUserId > 0) {
            str = UserObject.getForcedFirstName(getMessagesController().getUser(Long.valueOf(starsGiftUserId)));
        } else {
            str = "";
            if (starsGiftUserId < 0 && (chat = getMessagesController().getChat(Long.valueOf(-starsGiftUserId))) != null) {
                str = chat.title;
            }
        }
        long stars = getStars();
        int i = z ? (z2 || z3) ? R.raw.stars_send : R.raw.stars_topup : R.raw.payment_success;
        if (z) {
            string = LocaleController.getString(z3 ? R.string.StarsGiveawaySentPopup : z2 ? R.string.StarsGiftSentPopup : R.string.StarsAcquired);
        } else {
            string = null;
        }
        if (!z) {
            formatString = LocaleController.formatString(R.string.PaymentInfoHint, this.totalPrice[0], this.currentItemName);
        } else if (z3) {
            formatString = LocaleController.formatPluralStringComma("StarsGiveawaySentPopupInfo", (int) stars);
        } else {
            formatString = LocaleController.formatPluralStringComma(z2 ? "StarsGiftSentPopupInfo" : "StarsAcquiredInfo", (int) stars, str);
        }
        SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(formatString);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        BulletinFactory of = BulletinFactory.of(safeLastFragment);
        if (starsGiftUserId == 0 || string == null || z3) {
            String str2 = string;
            createSimpleBulletin = str2 != null ? of.createSimpleBulletin(i, str2, replaceTags) : of.createSimpleBulletin(i, replaceTags);
        } else {
            createSimpleBulletin = of.createSimpleBulletin(i, string, replaceTags, LocaleController.getString(R.string.ViewInChat), new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda66
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.lambda$sendData$56(starsGiftUserId);
                }
            });
        }
        createSimpleBulletin.hideAfterBottomSheet = false;
        createSimpleBulletin.setDuration(5000);
        if (tLRPC$MessageArr[0] != null) {
            createSimpleBulletin.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda67
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PaymentFormActivity.this.lambda$sendData$59(createSimpleBulletin, z2, tLRPC$MessageArr, view);
                }
            });
        }
        createSimpleBulletin.show(z3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendData$61(long j) {
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment != null) {
            safeLastFragment.presentFragment(ChatActivity.of(j));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$62(TLObject tLObject) {
        BaseFragment lastFragment;
        if (tLObject instanceof TLRPC$TL_payments_paymentReceiptStars) {
            StarsIntroActivity.showTransactionSheet(getContext(), false, this.currentAccount, (TLRPC$TL_payments_paymentReceiptStars) tLObject, getResourceProvider());
        } else if (!(tLObject instanceof TLRPC$PaymentReceipt) || (lastFragment = LaunchActivity.getLastFragment()) == null) {
        } else {
            BaseFragment.BottomSheetParams bottomSheetParams = new BaseFragment.BottomSheetParams();
            bottomSheetParams.transitionFromLeft = true;
            bottomSheetParams.allowNestedScroll = false;
            lastFragment.showAsSheet(new PaymentFormActivity((TLRPC$PaymentReceipt) tLObject), bottomSheetParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$63(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$sendData$62(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$64(Bulletin bulletin, boolean z, TLRPC$Message tLRPC$Message, View view) {
        bulletin.hide();
        if (z) {
            BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
            if (safeLastFragment != null) {
                safeLastFragment.presentFragment(ChatActivity.of(MessageObject.getDialogId(tLRPC$Message), tLRPC$Message.id));
                return;
            }
            return;
        }
        TLRPC$TL_payments_getPaymentReceipt tLRPC$TL_payments_getPaymentReceipt = new TLRPC$TL_payments_getPaymentReceipt();
        tLRPC$TL_payments_getPaymentReceipt.msg_id = tLRPC$Message.id;
        tLRPC$TL_payments_getPaymentReceipt.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(tLRPC$Message.peer_id);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_getPaymentReceipt, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda75
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$sendData$63(tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$65(INavigationLayout iNavigationLayout, Activity activity, final TLRPC$Message tLRPC$Message) {
        String str;
        TLRPC$Chat chat;
        String string;
        String formatString;
        final Bulletin createSimpleBulletin;
        PaymentFormCallback paymentFormCallback;
        PaymentFormCallback paymentFormCallback2;
        this.paymentStatusSent = true;
        InvoiceStatus invoiceStatus = InvoiceStatus.PAID;
        this.invoiceStatus = invoiceStatus;
        PaymentFormCallback paymentFormCallback3 = this.paymentFormCallback;
        if (paymentFormCallback3 != null) {
            paymentFormCallback3.onInvoiceStatusChanged(invoiceStatus);
        }
        onCheckoutSuccess(iNavigationLayout, activity);
        TLRPC$InputInvoice tLRPC$InputInvoice = this.invoiceInput;
        boolean z = tLRPC$InputInvoice instanceof TLRPC$TL_inputInvoiceStars;
        final boolean z2 = z && (((TLRPC$TL_inputInvoiceStars) tLRPC$InputInvoice).purpose instanceof TLRPC$TL_inputStorePaymentStarsGift);
        boolean z3 = z && (((TLRPC$TL_inputInvoiceStars) tLRPC$InputInvoice).purpose instanceof TLRPC$TL_inputStorePaymentStarsGiveaway);
        if (!z && (paymentFormCallback2 = this.paymentFormCallback) != null) {
            paymentFormCallback2.onInvoiceStatusChanged(this.invoiceStatus);
        }
        goToNextStep();
        if (z && (paymentFormCallback = this.paymentFormCallback) != null) {
            paymentFormCallback.onInvoiceStatusChanged(this.invoiceStatus);
        }
        final long starsGiftUserId = getStarsGiftUserId();
        if (starsGiftUserId > 0) {
            str = UserObject.getForcedFirstName(getMessagesController().getUser(Long.valueOf(starsGiftUserId)));
        } else {
            str = "";
            if (starsGiftUserId < 0 && (chat = getMessagesController().getChat(Long.valueOf(-starsGiftUserId))) != null) {
                str = chat.title;
            }
        }
        long stars = getStars();
        int i = z ? (z2 || z3) ? R.raw.stars_send : R.raw.stars_topup : R.raw.payment_success;
        if (z) {
            string = LocaleController.getString(z3 ? R.string.StarsGiveawaySentPopup : z2 ? R.string.StarsGiftSentPopup : R.string.StarsAcquired);
        } else {
            string = null;
        }
        if (!z) {
            formatString = LocaleController.formatString(R.string.PaymentInfoHint, this.totalPrice[0], this.currentItemName);
        } else if (z3) {
            formatString = LocaleController.formatPluralStringComma("StarsGiveawaySentPopupInfo", (int) stars);
        } else {
            formatString = LocaleController.formatPluralStringComma(z2 ? "StarsGiftSentPopupInfo" : "StarsAcquiredInfo", (int) stars, str);
        }
        SpannableStringBuilder replaceTags = AndroidUtilities.replaceTags(formatString);
        BaseFragment safeLastFragment = LaunchActivity.getSafeLastFragment();
        if (safeLastFragment == null) {
            return;
        }
        BulletinFactory of = BulletinFactory.of(safeLastFragment);
        if (starsGiftUserId == 0 || string == null || z3) {
            String str2 = string;
            createSimpleBulletin = str2 != null ? of.createSimpleBulletin(i, str2, replaceTags) : of.createSimpleBulletin(i, replaceTags);
        } else {
            createSimpleBulletin = of.createSimpleBulletin(i, string, replaceTags, LocaleController.getString(R.string.ViewInChat), new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda72
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.lambda$sendData$61(starsGiftUserId);
                }
            });
        }
        createSimpleBulletin.hideAfterBottomSheet = false;
        createSimpleBulletin.setDuration(5000);
        if (tLRPC$Message != null) {
            createSimpleBulletin.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda73
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PaymentFormActivity.this.lambda$sendData$64(createSimpleBulletin, z2, tLRPC$Message, view);
                }
            });
        }
        createSimpleBulletin.show(z3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$sendData$66(final INavigationLayout iNavigationLayout, final Activity activity, final TLRPC$Message tLRPC$Message) {
        if (MessageObject.getPeerId(tLRPC$Message.peer_id) == this.botUser.id && (tLRPC$Message.action instanceof TLRPC$TL_messageActionPaymentSent)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda69
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendData$65(iNavigationLayout, activity, tLRPC$Message);
                }
            });
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$67(TLObject tLObject) {
        setDonePressed(false);
        this.webviewLoading = true;
        showEditDoneProgress(true, true);
        ContextProgressView contextProgressView = this.progressView;
        if (contextProgressView != null) {
            contextProgressView.setVisibility(0);
        }
        ActionBarMenuItem actionBarMenuItem = this.doneItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.setEnabled(false);
            this.doneItem.getContentView().setVisibility(4);
        }
        final INavigationLayout parentLayout = getParentLayout();
        final Activity parentActivity = getParentActivity();
        getMessagesController().newMessageCallback = new MessagesController.NewMessageCallback() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda63
            @Override // org.telegram.messenger.MessagesController.NewMessageCallback
            public final boolean onMessageReceived(TLRPC$Message tLRPC$Message) {
                boolean lambda$sendData$66;
                lambda$sendData$66 = PaymentFormActivity.this.lambda$sendData$66(parentLayout, parentActivity, tLRPC$Message);
                return lambda$sendData$66;
            }
        };
        WebView webView = this.webView;
        if (webView != null) {
            webView.setVisibility(0);
            WebView webView2 = this.webView;
            String str = ((TLRPC$TL_payments_paymentVerificationNeeded) tLObject).url;
            this.webViewUrl = str;
            webView2.loadUrl(str);
        }
        this.paymentStatusSent = true;
        InvoiceStatus invoiceStatus = InvoiceStatus.PENDING;
        this.invoiceStatus = invoiceStatus;
        PaymentFormCallback paymentFormCallback = this.paymentFormCallback;
        if (paymentFormCallback != null) {
            paymentFormCallback.onInvoiceStatusChanged(invoiceStatus);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$68(TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_payments_sendPaymentForm tLRPC$TL_payments_sendPaymentForm) {
        AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLRPC$TL_payments_sendPaymentForm, new Object[0]);
        setDonePressed(false);
        showEditDoneProgress(false, false);
        this.paymentStatusSent = true;
        InvoiceStatus invoiceStatus = InvoiceStatus.FAILED;
        this.invoiceStatus = invoiceStatus;
        PaymentFormCallback paymentFormCallback = this.paymentFormCallback;
        if (paymentFormCallback != null) {
            paymentFormCallback.onInvoiceStatusChanged(invoiceStatus);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$69(final TLRPC$TL_payments_sendPaymentForm tLRPC$TL_payments_sendPaymentForm, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        Runnable runnable;
        if (tLObject == null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda57
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendData$68(tLRPC$TL_error, tLRPC$TL_payments_sendPaymentForm);
                }
            });
            return;
        }
        if (tLObject instanceof TLRPC$TL_payments_paymentResult) {
            TLRPC$Updates tLRPC$Updates = ((TLRPC$TL_payments_paymentResult) tLObject).updates;
            final TLRPC$Message[] tLRPC$MessageArr = new TLRPC$Message[1];
            int size = tLRPC$Updates.updates.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    break;
                }
                TLRPC$Update tLRPC$Update = tLRPC$Updates.updates.get(i);
                if (tLRPC$Update instanceof TLRPC$TL_updateNewMessage) {
                    tLRPC$MessageArr[0] = ((TLRPC$TL_updateNewMessage) tLRPC$Update).message;
                    break;
                } else if (tLRPC$Update instanceof TLRPC$TL_updateNewChannelMessage) {
                    tLRPC$MessageArr[0] = ((TLRPC$TL_updateNewChannelMessage) tLRPC$Update).message;
                    break;
                } else {
                    i++;
                }
            }
            getMessagesController().processUpdates(tLRPC$Updates, false);
            runnable = new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda55
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendData$60(tLRPC$MessageArr);
                }
            };
        } else if (!(tLObject instanceof TLRPC$TL_payments_paymentVerificationNeeded)) {
            return;
        } else {
            runnable = new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda56
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendData$67(tLObject);
                }
            };
        }
        AndroidUtilities.runOnUIThread(runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendForm$52(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendForm$53(TLObject tLObject) {
        this.requestedInfo = (TLRPC$TL_payments_validatedRequestedInfo) tLObject;
        if (this.paymentForm.saved_info != null && !this.saveShippingInfo) {
            TLRPC$TL_payments_clearSavedInfo tLRPC$TL_payments_clearSavedInfo = new TLRPC$TL_payments_clearSavedInfo();
            tLRPC$TL_payments_clearSavedInfo.info = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_clearSavedInfo, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda61
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
                    PaymentFormActivity.lambda$sendForm$52(tLObject2, tLRPC$TL_error);
                }
            });
        }
        goToNextStep();
        setDonePressed(false);
        showEditDoneProgress(true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendForm$54(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        setDonePressed(false);
        showEditDoneProgress(true, false);
        if (tLRPC$TL_error != null) {
            String str = tLRPC$TL_error.text;
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -2092780146:
                    if (str.equals("ADDRESS_CITY_INVALID")) {
                        c = 0;
                        break;
                    }
                    break;
                case -1623547228:
                    if (str.equals("ADDRESS_STREET_LINE1_INVALID")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1224177757:
                    if (str.equals("ADDRESS_COUNTRY_INVALID")) {
                        c = 2;
                        break;
                    }
                    break;
                case -1031752045:
                    if (str.equals("REQ_INFO_NAME_INVALID")) {
                        c = 3;
                        break;
                    }
                    break;
                case -274035920:
                    if (str.equals("ADDRESS_POSTCODE_INVALID")) {
                        c = 4;
                        break;
                    }
                    break;
                case 417441502:
                    if (str.equals("ADDRESS_STATE_INVALID")) {
                        c = 5;
                        break;
                    }
                    break;
                case 708423542:
                    if (str.equals("REQ_INFO_PHONE_INVALID")) {
                        c = 6;
                        break;
                    }
                    break;
                case 863965605:
                    if (str.equals("ADDRESS_STREET_LINE2_INVALID")) {
                        c = 7;
                        break;
                    }
                    break;
                case 889106340:
                    if (str.equals("REQ_INFO_EMAIL_INVALID")) {
                        c = '\b';
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    shakeField(2);
                    return;
                case 1:
                    shakeField(0);
                    return;
                case 2:
                    shakeField(4);
                    return;
                case 3:
                    shakeField(6);
                    return;
                case 4:
                    shakeField(5);
                    return;
                case 5:
                    shakeField(3);
                    return;
                case 6:
                    shakeField(9);
                    return;
                case 7:
                    shakeField(1);
                    return;
                case '\b':
                    shakeField(7);
                    return;
                default:
                    AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLObject, new Object[0]);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendForm$55(final TLObject tLObject, final TLObject tLObject2, final TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject2 instanceof TLRPC$TL_payments_validatedRequestedInfo) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda46
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendForm$53(tLObject2);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda47
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendForm$54(tLRPC$TL_error, tLObject);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$41(TLRPC$TL_error tLRPC$TL_error) {
        showEditDoneProgress(true, false);
        if (tLRPC$TL_error == null) {
            if (getParentActivity() == null) {
                return;
            }
            Runnable runnable = this.shortPollRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.shortPollRunnable = null;
            }
            goToNextStep();
        } else if (tLRPC$TL_error.text.startsWith("CODE_INVALID")) {
            shakeView(this.codeFieldCell);
            this.codeFieldCell.setText("", false);
        } else if (!tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
            showAlertWithText(LocaleController.getString(R.string.AppName), tLRPC$TL_error.text);
        } else {
            int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
            showAlertWithText(LocaleController.getString(R.string.AppName), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, intValue < 60 ? LocaleController.formatPluralString("Seconds", intValue, new Object[0]) : LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0])));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$42(TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda45
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$sendSavePassword$41(tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$43(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, boolean z) {
        if (tLRPC$TL_error == null) {
            TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            this.currentPassword = tLRPC$account_Password;
            TwoStepVerificationActivity.initPasswordNewAlgo(tLRPC$account_Password);
            sendSavePassword(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$44(final boolean z, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda71
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$sendSavePassword$43(tLRPC$TL_error, tLObject, z);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$45(String str, DialogInterface dialogInterface, int i) {
        this.waitingForEmail = true;
        this.currentPassword.email_unconfirmed_pattern = str;
        updatePasswordFields();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$46(TLRPC$TL_error tLRPC$TL_error, final boolean z, TLObject tLObject, final String str) {
        String string;
        String str2;
        if (tLRPC$TL_error != null && "SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda64
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                    PaymentFormActivity.this.lambda$sendSavePassword$44(z, tLObject2, tLRPC$TL_error2);
                }
            }, 8);
            return;
        }
        showEditDoneProgress(true, false);
        if (z) {
            TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
            tLRPC$account_Password.has_password = false;
            tLRPC$account_Password.current_algo = null;
            this.delegate.currentPasswordUpdated(tLRPC$account_Password);
            finishFragment();
        } else if (tLRPC$TL_error == null && (tLObject instanceof TLRPC$TL_boolTrue)) {
            if (getParentActivity() == null) {
                return;
            }
            goToNextStep();
        } else if (tLRPC$TL_error != null) {
            if (tLRPC$TL_error.text.equals("EMAIL_UNCONFIRMED") || tLRPC$TL_error.text.startsWith("EMAIL_UNCONFIRMED_")) {
                this.emailCodeLength = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda65
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        PaymentFormActivity.this.lambda$sendSavePassword$45(str, dialogInterface, i);
                    }
                });
                builder.setMessage(LocaleController.getString(R.string.YourEmailAlmostThereText));
                builder.setTitle(LocaleController.getString(R.string.YourEmailAlmostThere));
                Dialog showDialog = showDialog(builder.create());
                if (showDialog != null) {
                    showDialog.setCanceledOnTouchOutside(false);
                    showDialog.setCancelable(false);
                }
            } else if (tLRPC$TL_error.text.equals("EMAIL_INVALID")) {
                showAlertWithText(LocaleController.getString(R.string.AppName), LocaleController.getString(R.string.PasswordEmailInvalid));
            } else {
                if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                    String formatPluralString = intValue < 60 ? LocaleController.formatPluralString("Seconds", intValue, new Object[0]) : LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                    string = LocaleController.getString(R.string.AppName);
                    str2 = LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString);
                } else {
                    string = LocaleController.getString(R.string.AppName);
                    str2 = tLRPC$TL_error.text;
                }
                showAlertWithText(string, str2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$47(final boolean z, final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$sendSavePassword$46(tLRPC$TL_error, z, tLObject, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$48(final boolean z, final String str, String str2, TLRPC$TL_account_updatePasswordSettings tLRPC$TL_account_updatePasswordSettings) {
        RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda52
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$sendSavePassword$47(z, str, tLObject, tLRPC$TL_error);
            }
        };
        if (!z) {
            byte[] stringBytes = AndroidUtilities.getStringBytes(str2);
            TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = this.currentPassword.new_algo;
            if (!(tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow)) {
                TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
                tLRPC$TL_error.text = "PASSWORD_HASH_INVALID";
                requestDelegate.run(null, tLRPC$TL_error);
                return;
            }
            tLRPC$TL_account_updatePasswordSettings.new_settings.new_password_hash = SRPHelper.getVBytes(stringBytes, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo);
            if (tLRPC$TL_account_updatePasswordSettings.new_settings.new_password_hash == null) {
                TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
                tLRPC$TL_error2.text = "ALGO_INVALID";
                requestDelegate.run(null, tLRPC$TL_error2);
            }
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_updatePasswordSettings, requestDelegate, 10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavedForm$49(TLObject tLObject, Runnable runnable) {
        this.requestedInfo = (TLRPC$TL_payments_validatedRequestedInfo) tLObject;
        runnable.run();
        setDonePressed(false);
        showEditDoneProgress(true, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavedForm$50(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        setDonePressed(false);
        showEditDoneProgress(true, false);
        if (tLRPC$TL_error != null) {
            AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLObject, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavedForm$51(final Runnable runnable, final TLObject tLObject, final TLObject tLObject2, final TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject2 instanceof TLRPC$TL_payments_validatedRequestedInfo) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda58
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendSavedForm$49(tLObject2, runnable);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda59
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendSavedForm$50(tLRPC$TL_error, tLObject);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showChoosePaymentMethod$31(final Runnable runnable, List list, List list2, DialogInterface dialogInterface, int i) {
        PaymentFormActivityDelegate paymentFormActivityDelegate = new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.21
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password) {
                PaymentFormActivityDelegate.-CC.$default$currentPasswordUpdated(this, tLRPC$account_Password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
                PaymentFormActivityDelegate.-CC.$default$didSelectNewAddress(this, tLRPC$TL_payments_validateRequestedInfo);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public boolean didSelectNewCard(String str, String str2, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                String str3;
                PaymentFormActivity.this.savedCredentialsCard = tLRPC$TL_paymentSavedCredentialsCard;
                PaymentFormActivity.this.paymentJson = str;
                PaymentFormActivity.this.saveCardInfo = z;
                PaymentFormActivity.this.cardName = str2;
                PaymentFormActivity.this.googlePayCredentials = tLRPC$TL_inputPaymentCredentialsGooglePay;
                if (PaymentFormActivity.this.detailSettingsCell[0] != null) {
                    PaymentFormActivity.this.detailSettingsCell[0].setVisibility(0);
                    TextDetailSettingsCell textDetailSettingsCell = PaymentFormActivity.this.detailSettingsCell[0];
                    if (PaymentFormActivity.this.cardName == null || PaymentFormActivity.this.cardName.length() <= 1) {
                        str3 = PaymentFormActivity.this.cardName;
                    } else {
                        str3 = PaymentFormActivity.this.cardName.substring(0, 1).toUpperCase() + PaymentFormActivity.this.cardName.substring(1);
                    }
                    textDetailSettingsCell.setTextAndValueAndIcon(str3, LocaleController.getString(R.string.PaymentCheckoutMethod), R.drawable.msg_payment_card, true);
                    if (PaymentFormActivity.this.detailSettingsCell[1] != null) {
                        PaymentFormActivity.this.detailSettingsCell[1].setVisibility(0);
                    }
                }
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    runnable2.run();
                }
                return false;
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.-CC.$default$onFragmentDestroyed(this);
            }
        };
        TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard = this.savedCredentialsCard;
        int i2 = (tLRPC$TL_paymentSavedCredentialsCard == null && this.cardName == null) ? 0 : 1;
        if (!(tLRPC$TL_paymentSavedCredentialsCard == null && this.cardName == null) && i == 0) {
            return;
        }
        if (i >= i2 && i < list.size() + i2) {
            TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard2 = (TLRPC$TL_paymentSavedCredentialsCard) list.get(i - i2);
            this.savedCredentialsCard = tLRPC$TL_paymentSavedCredentialsCard2;
            paymentFormActivityDelegate.didSelectNewCard(null, tLRPC$TL_paymentSavedCredentialsCard2.title, true, null, tLRPC$TL_paymentSavedCredentialsCard2);
        } else if (i < list2.size() - 1) {
            PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, 2, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
            paymentFormActivity.setPaymentMethod((TLRPC$TL_paymentFormMethod) this.paymentForm.additional_methods.get((i - list.size()) - i2));
            paymentFormActivity.setDelegate(paymentFormActivityDelegate);
            presentFragment(paymentFormActivity);
        } else if (i == list2.size() - 1) {
            PaymentFormActivity paymentFormActivity2 = new PaymentFormActivity(this.invoiceInput, this.paymentForm, this.messageObject, this.invoiceSlug, 2, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
            paymentFormActivity2.setDelegate(paymentFormActivityDelegate);
            presentFragment(paymentFormActivity2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPayAlert$36(DialogInterface dialogInterface, int i) {
        setDonePressed(true);
        sendData();
    }

    private void loadPasswordInfo() {
        if (this.loadingPasswordInfo) {
            return;
        }
        this.loadingPasswordInfo = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda3
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$loadPasswordInfo$35(tLObject, tLRPC$TL_error);
            }
        }, 10);
    }

    private boolean onCheckoutSuccess(INavigationLayout iNavigationLayout, Activity activity) {
        if (this.invoiceInput != null) {
            if (iNavigationLayout != null) {
                Iterator it = new ArrayList(iNavigationLayout.getFragmentStack()).iterator();
                while (it.hasNext()) {
                    BaseFragment baseFragment = (BaseFragment) it.next();
                    if (baseFragment instanceof PaymentFormActivity) {
                        baseFragment.removeSelfFromStack();
                    }
                }
                return true;
            }
            return false;
        }
        String str = this.botUser.username;
        if ((!(str != null && str.equalsIgnoreCase(getMessagesController().premiumBotUsername) && this.invoiceSlug == null) && (this.invoiceSlug == null || getMessagesController().premiumInvoiceSlug == null || !Objects.equals(this.invoiceSlug, getMessagesController().premiumInvoiceSlug))) || iNavigationLayout == null) {
            return false;
        }
        Iterator it2 = new ArrayList(iNavigationLayout.getFragmentStack()).iterator();
        while (it2.hasNext()) {
            BaseFragment baseFragment2 = (BaseFragment) it2.next();
            if ((baseFragment2 instanceof ChatActivity) || (baseFragment2 instanceof PremiumPreviewFragment)) {
                baseFragment2.removeSelfFromStack();
            }
        }
        iNavigationLayout.presentFragment(new PremiumPreviewFragment(null).setForcePremium(), !isFinishing());
        if (activity instanceof LaunchActivity) {
            try {
                this.fragmentView.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            ((LaunchActivity) activity).getFireworksOverlay().start();
        }
        return true;
    }

    private void onPresentFragment(BaseFragment baseFragment) {
        AndroidUtilities.hideKeyboard(this.fragmentView);
        if (baseFragment instanceof PaymentFormActivity) {
            PaymentFormActivity paymentFormActivity = (PaymentFormActivity) baseFragment;
            paymentFormActivity.paymentFormCallback = this.paymentFormCallback;
            paymentFormActivity.resourcesProvider = this.resourcesProvider;
            paymentFormActivity.needPayAfterTransition = this.needPayAfterTransition;
            paymentFormActivity.savedCredentialsCard = this.savedCredentialsCard;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sendCardData() {
        Integer num;
        Integer num2;
        String[] split = this.inputFields[1].getText().toString().split("/");
        if (split.length == 2) {
            Integer parseInt = Utilities.parseInt((CharSequence) split[0]);
            num2 = Utilities.parseInt((CharSequence) split[1]);
            num = parseInt;
        } else {
            num = null;
            num2 = null;
        }
        final Card card = new Card(this.inputFields[0].getText().toString(), num, num2, this.inputFields[3].getText().toString(), this.inputFields[2].getText().toString(), null, null, null, null, this.inputFields[5].getText().toString(), this.inputFields[4].getText().toString(), null);
        this.cardName = card.getBrand() + " *" + card.getLast4();
        if (!card.validateNumber()) {
            shakeField(0);
            return false;
        } else if (!card.validateExpMonth() || !card.validateExpYear() || !card.validateExpiryDate()) {
            shakeField(1);
            return false;
        } else if (this.need_card_name && this.inputFields[2].length() == 0) {
            shakeField(2);
            return false;
        } else if (!card.validateCVC()) {
            shakeField(3);
            return false;
        } else if (this.need_card_country && this.inputFields[4].length() == 0) {
            shakeField(4);
            return false;
        } else if (this.need_card_postcode && this.inputFields[5].length() == 0) {
            shakeField(5);
            return false;
        } else {
            showEditDoneProgress(true, true);
            try {
                if ("stripe".equals(this.paymentForm.native_provider)) {
                    new Stripe(this.providerApiKey).createToken(card, new 25());
                } else if ("smartglocal".equals(this.paymentForm.native_provider)) {
                    new AsyncTask() { // from class: org.telegram.ui.PaymentFormActivity.26
                        /* JADX INFO: Access modifiers changed from: protected */
                        /* JADX WARN: Can't wrap try/catch for region: R(9:3|4|(9:(12:45|46|(3:48|49|(1:51))|(1:8)(2:41|(1:43)(1:44))|9|10|11|12|13|14|15|(4:23|(1:25)|26|27)(3:19|20|21))|13|14|15|(1:17)|23|(0)|26|27)|6|(0)(0)|9|10|11|12) */
                        /* JADX WARN: Code restructure failed: missing block: B:10:0x009b, code lost:
                            if (r6.endsWith(".smart-glocal.com/cds/v1/tokenize/card") != false) goto L6;
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:38:0x014b, code lost:
                            r13 = e;
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:51:0x0178, code lost:
                            org.telegram.messenger.FileLog.e(r13);
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:52:0x017b, code lost:
                            if (r6 == null) goto L27;
                         */
                        /* JADX WARN: Not initialized variable reg: 6, insn: 0x0149: MOVE  (r4 I:??[OBJECT, ARRAY]) = (r6 I:??[OBJECT, ARRAY]), block:B:37:0x0149 */
                        /* JADX WARN: Removed duplicated region for block: B:18:0x00a9 A[Catch: all -> 0x009e, Exception -> 0x00af, TRY_ENTER, TryCatch #0 {Exception -> 0x00af, blocks: (B:3:0x0009, B:18:0x00a9, B:25:0x00ce, B:21:0x00b3, B:23:0x00bf, B:24:0x00c7), top: B:60:0x0009 }] */
                        /* JADX WARN: Removed duplicated region for block: B:21:0x00b3 A[Catch: all -> 0x009e, Exception -> 0x00af, TryCatch #0 {Exception -> 0x00af, blocks: (B:3:0x0009, B:18:0x00a9, B:25:0x00ce, B:21:0x00b3, B:23:0x00bf, B:24:0x00c7), top: B:60:0x0009 }] */
                        /* JADX WARN: Removed duplicated region for block: B:42:0x0151 A[Catch: all -> 0x0148, Exception -> 0x014b, TRY_LEAVE, TryCatch #2 {Exception -> 0x014b, blocks: (B:27:0x00d6, B:29:0x010d, B:33:0x011c, B:40:0x014d, B:42:0x0151, B:50:0x0177, B:49:0x0174), top: B:64:0x00d6 }] */
                        @Override // android.os.AsyncTask
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                        */
                        public String doInBackground(Object... objArr) {
                            HttpURLConnection httpURLConnection;
                            HttpURLConnection httpURLConnection2;
                            JSONObject jSONObject;
                            String string;
                            OutputStream outputStream;
                            int responseCode;
                            HttpURLConnection httpURLConnection3 = null;
                            try {
                            } catch (Throwable th) {
                                th = th;
                                httpURLConnection3 = httpURLConnection;
                            }
                            try {
                                try {
                                    jSONObject = new JSONObject();
                                    JSONObject jSONObject2 = new JSONObject();
                                    jSONObject2.put("number", card.getNumber());
                                    jSONObject2.put("expiration_month", String.format(Locale.US, "%02d", card.getExpMonth()));
                                    jSONObject2.put("expiration_year", "" + card.getExpYear());
                                    jSONObject2.put("security_code", "" + card.getCVC());
                                    jSONObject.put("card", jSONObject2);
                                } catch (Exception e) {
                                    e = e;
                                    httpURLConnection2 = null;
                                }
                                try {
                                    if (PaymentFormActivity.this.paymentForm.native_params != null) {
                                        try {
                                            string = new JSONObject(PaymentFormActivity.this.paymentForm.native_params.data).getString("tokenize_url");
                                            if (string != null) {
                                                try {
                                                    if (string.startsWith("https://")) {
                                                    }
                                                } catch (Exception unused) {
                                                }
                                            }
                                        } catch (Exception unused2) {
                                        }
                                        httpURLConnection2 = (HttpURLConnection) (string == null ? new URL(string) : PaymentFormActivity.this.paymentForm.invoice.test ? new URL("https://tgb-playground.smart-glocal.com/cds/v1/tokenize/card") : new URL("https://tgb.smart-glocal.com/cds/v1/tokenize/card")).openConnection();
                                        httpURLConnection2.setConnectTimeout(30000);
                                        httpURLConnection2.setReadTimeout(80000);
                                        httpURLConnection2.setUseCaches(false);
                                        httpURLConnection2.setDoOutput(true);
                                        httpURLConnection2.setRequestMethod("POST");
                                        httpURLConnection2.setRequestProperty("Content-Type", "application/json");
                                        httpURLConnection2.setRequestProperty("X-PUBLIC-TOKEN", PaymentFormActivity.this.providerApiKey);
                                        outputStream = httpURLConnection2.getOutputStream();
                                        outputStream.write(jSONObject.toString().getBytes("UTF-8"));
                                        outputStream.close();
                                        responseCode = httpURLConnection2.getResponseCode();
                                        if (responseCode < 200 && responseCode < 300) {
                                            JSONObject jSONObject3 = new JSONObject();
                                            jSONObject3.put("token", new JSONObject(PaymentFormActivity.getResponseBody(httpURLConnection2.getInputStream())).getJSONObject("data").getString("token"));
                                            jSONObject3.put("type", "card");
                                            String jSONObject4 = jSONObject3.toString();
                                            httpURLConnection2.disconnect();
                                            return jSONObject4;
                                        }
                                        if (BuildVars.DEBUG_VERSION) {
                                            FileLog.e("" + PaymentFormActivity.getResponseBody(httpURLConnection2.getErrorStream()));
                                        }
                                        httpURLConnection2.disconnect();
                                        return null;
                                    }
                                    outputStream.write(jSONObject.toString().getBytes("UTF-8"));
                                    outputStream.close();
                                    responseCode = httpURLConnection2.getResponseCode();
                                    if (responseCode < 200) {
                                    }
                                    if (BuildVars.DEBUG_VERSION) {
                                    }
                                    httpURLConnection2.disconnect();
                                    return null;
                                } catch (Throwable th2) {
                                    if (outputStream != null) {
                                        try {
                                            outputStream.close();
                                        } catch (Throwable th3) {
                                            th2.addSuppressed(th3);
                                        }
                                    }
                                    throw th2;
                                }
                                string = null;
                                httpURLConnection2 = (HttpURLConnection) (string == null ? new URL(string) : PaymentFormActivity.this.paymentForm.invoice.test ? new URL("https://tgb-playground.smart-glocal.com/cds/v1/tokenize/card") : new URL("https://tgb.smart-glocal.com/cds/v1/tokenize/card")).openConnection();
                                httpURLConnection2.setConnectTimeout(30000);
                                httpURLConnection2.setReadTimeout(80000);
                                httpURLConnection2.setUseCaches(false);
                                httpURLConnection2.setDoOutput(true);
                                httpURLConnection2.setRequestMethod("POST");
                                httpURLConnection2.setRequestProperty("Content-Type", "application/json");
                                httpURLConnection2.setRequestProperty("X-PUBLIC-TOKEN", PaymentFormActivity.this.providerApiKey);
                                outputStream = httpURLConnection2.getOutputStream();
                            } catch (Throwable th4) {
                                th = th4;
                                if (httpURLConnection3 != null) {
                                    httpURLConnection3.disconnect();
                                }
                                throw th;
                            }
                        }

                        /* JADX INFO: Access modifiers changed from: protected */
                        @Override // android.os.AsyncTask
                        public void onPostExecute(String str) {
                            if (PaymentFormActivity.this.canceled) {
                                return;
                            }
                            if (str == null) {
                                AlertsCreator.showSimpleToast(PaymentFormActivity.this, LocaleController.getString(R.string.PaymentConnectionFailed));
                            } else {
                                PaymentFormActivity.this.paymentJson = str;
                                PaymentFormActivity.this.goToNextStep();
                            }
                            PaymentFormActivity.this.showEditDoneProgress(true, false);
                            PaymentFormActivity.this.setDonePressed(false);
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            return true;
        }
    }

    private void sendData() {
        String str;
        if (this.canceled) {
            return;
        }
        showEditDoneProgress(false, true);
        final TLRPC$TL_payments_sendPaymentForm tLRPC$TL_payments_sendPaymentForm = new TLRPC$TL_payments_sendPaymentForm();
        TLRPC$InputInvoice tLRPC$InputInvoice = this.invoiceInput;
        TLRPC$TL_inputInvoiceMessage tLRPC$TL_inputInvoiceMessage = tLRPC$InputInvoice;
        if (tLRPC$InputInvoice == null) {
            if (this.messageObject != null) {
                TLRPC$TL_inputInvoiceMessage tLRPC$TL_inputInvoiceMessage2 = new TLRPC$TL_inputInvoiceMessage();
                tLRPC$TL_inputInvoiceMessage2.peer = getMessagesController().getInputPeer(this.messageObject.messageOwner.peer_id);
                tLRPC$TL_inputInvoiceMessage2.msg_id = this.messageObject.getId();
                tLRPC$TL_inputInvoiceMessage = tLRPC$TL_inputInvoiceMessage2;
            } else {
                TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
                tLRPC$TL_inputInvoiceSlug.slug = this.invoiceSlug;
                tLRPC$TL_inputInvoiceMessage = tLRPC$TL_inputInvoiceSlug;
            }
        }
        tLRPC$TL_payments_sendPaymentForm.invoice = tLRPC$TL_inputInvoiceMessage;
        tLRPC$TL_payments_sendPaymentForm.form_id = this.paymentForm.form_id;
        if (UserConfig.getInstance(this.currentAccount).tmpPassword == null || this.savedCredentialsCard == null) {
            TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay = this.googlePayCredentials;
            if (tLRPC$TL_inputPaymentCredentialsGooglePay != null) {
                tLRPC$TL_payments_sendPaymentForm.credentials = tLRPC$TL_inputPaymentCredentialsGooglePay;
            } else {
                TLRPC$InputPaymentCredentials tLRPC$InputPaymentCredentials = new TLRPC$InputPaymentCredentials() { // from class: org.telegram.tgnet.TLRPC$TL_inputPaymentCredentials
                    @Override // org.telegram.tgnet.TLObject
                    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
                        int readInt32 = abstractSerializedData.readInt32(z);
                        this.flags = readInt32;
                        this.save = (readInt32 & 1) != 0;
                        this.data = TLRPC$TL_dataJSON.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
                    }

                    @Override // org.telegram.tgnet.TLObject
                    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                        abstractSerializedData.writeInt32(873977640);
                        int i = this.save ? this.flags | 1 : this.flags & (-2);
                        this.flags = i;
                        abstractSerializedData.writeInt32(i);
                        this.data.serializeToStream(abstractSerializedData);
                    }
                };
                tLRPC$TL_payments_sendPaymentForm.credentials = tLRPC$InputPaymentCredentials;
                tLRPC$InputPaymentCredentials.save = this.saveCardInfo;
                tLRPC$InputPaymentCredentials.data = new TLRPC$TL_dataJSON();
                tLRPC$TL_payments_sendPaymentForm.credentials.data.data = this.paymentJson;
            }
        } else {
            TLRPC$InputPaymentCredentials tLRPC$InputPaymentCredentials2 = new TLRPC$InputPaymentCredentials() { // from class: org.telegram.tgnet.TLRPC$TL_inputPaymentCredentialsSaved
                @Override // org.telegram.tgnet.TLObject
                public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
                    this.id = abstractSerializedData.readString(z);
                    this.tmp_password = abstractSerializedData.readByteArray(z);
                }

                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                    abstractSerializedData.writeInt32(-1056001329);
                    abstractSerializedData.writeString(this.id);
                    abstractSerializedData.writeByteArray(this.tmp_password);
                }
            };
            tLRPC$TL_payments_sendPaymentForm.credentials = tLRPC$InputPaymentCredentials2;
            tLRPC$InputPaymentCredentials2.id = this.savedCredentialsCard.id;
            tLRPC$InputPaymentCredentials2.tmp_password = UserConfig.getInstance(this.currentAccount).tmpPassword.tmp_password;
        }
        TLRPC$TL_payments_validatedRequestedInfo tLRPC$TL_payments_validatedRequestedInfo = this.requestedInfo;
        if (tLRPC$TL_payments_validatedRequestedInfo != null && (str = tLRPC$TL_payments_validatedRequestedInfo.id) != null) {
            tLRPC$TL_payments_sendPaymentForm.requested_info_id = str;
            tLRPC$TL_payments_sendPaymentForm.flags = 1 | tLRPC$TL_payments_sendPaymentForm.flags;
        }
        TLRPC$TL_shippingOption tLRPC$TL_shippingOption = this.shippingOption;
        if (tLRPC$TL_shippingOption != null) {
            tLRPC$TL_payments_sendPaymentForm.shipping_option_id = tLRPC$TL_shippingOption.id;
            tLRPC$TL_payments_sendPaymentForm.flags |= 2;
        }
        if ((this.paymentForm.invoice.flags & 256) != 0) {
            Long l = this.tipAmount;
            tLRPC$TL_payments_sendPaymentForm.tip_amount = l != null ? l.longValue() : 0L;
            tLRPC$TL_payments_sendPaymentForm.flags |= 4;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_sendPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda50
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$sendData$69(tLRPC$TL_payments_sendPaymentForm, tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void sendForm() {
        TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug;
        if (this.canceled) {
            return;
        }
        showEditDoneProgress(true, true);
        TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo = new TLRPC$TL_payments_validateRequestedInfo();
        this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
        TLRPC$InputInvoice tLRPC$InputInvoice = this.invoiceInput;
        if (tLRPC$InputInvoice != null) {
            tLRPC$TL_payments_validateRequestedInfo.invoice = tLRPC$InputInvoice;
        } else {
            if (this.messageObject != null) {
                TLRPC$TL_inputInvoiceMessage tLRPC$TL_inputInvoiceMessage = new TLRPC$TL_inputInvoiceMessage();
                tLRPC$TL_inputInvoiceMessage.peer = getMessagesController().getInputPeer(this.messageObject.messageOwner.peer_id);
                tLRPC$TL_inputInvoiceMessage.msg_id = this.messageObject.getId();
                tLRPC$TL_inputInvoiceSlug = tLRPC$TL_inputInvoiceMessage;
            } else {
                TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug2 = new TLRPC$TL_inputInvoiceSlug();
                tLRPC$TL_inputInvoiceSlug2.slug = this.invoiceSlug;
                tLRPC$TL_inputInvoiceSlug = tLRPC$TL_inputInvoiceSlug2;
            }
            this.validateRequest.invoice = tLRPC$TL_inputInvoiceSlug;
        }
        TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo2 = this.validateRequest;
        tLRPC$TL_payments_validateRequestedInfo2.save = this.saveShippingInfo;
        tLRPC$TL_payments_validateRequestedInfo2.info = new TLRPC$TL_paymentRequestedInfo();
        if (this.paymentForm.invoice.name_requested) {
            this.validateRequest.info.name = this.inputFields[6].getText().toString();
            this.validateRequest.info.flags |= 1;
        }
        if (this.paymentForm.invoice.phone_requested) {
            this.validateRequest.info.phone = "+" + this.inputFields[8].getText().toString() + this.inputFields[9].getText().toString();
            TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo = this.validateRequest.info;
            tLRPC$TL_paymentRequestedInfo.flags = tLRPC$TL_paymentRequestedInfo.flags | 2;
        }
        if (this.paymentForm.invoice.email_requested) {
            this.validateRequest.info.email = this.inputFields[7].getText().toString().trim();
            this.validateRequest.info.flags |= 4;
        }
        if (this.paymentForm.invoice.shipping_address_requested) {
            this.validateRequest.info.shipping_address = new TLRPC$TL_postAddress();
            this.validateRequest.info.shipping_address.street_line1 = this.inputFields[0].getText().toString();
            this.validateRequest.info.shipping_address.street_line2 = this.inputFields[1].getText().toString();
            this.validateRequest.info.shipping_address.city = this.inputFields[2].getText().toString();
            this.validateRequest.info.shipping_address.state = this.inputFields[3].getText().toString();
            TLRPC$TL_postAddress tLRPC$TL_postAddress = this.validateRequest.info.shipping_address;
            String str = this.countryName;
            if (str == null) {
                str = "";
            }
            tLRPC$TL_postAddress.country_iso2 = str;
            tLRPC$TL_postAddress.post_code = this.inputFields[5].getText().toString();
            this.validateRequest.info.flags |= 8;
        }
        final TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo3 = this.validateRequest;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(this.validateRequest, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda40
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$sendForm$55(tLRPC$TL_payments_validateRequestedInfo3, tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendSavePassword(final boolean z) {
        final String str;
        final String str2;
        if (!z && this.codeFieldCell.getVisibility() == 0) {
            String text = this.codeFieldCell.getText();
            if (text.length() == 0) {
                shakeView(this.codeFieldCell);
                return;
            }
            showEditDoneProgress(true, true);
            TLRPC$TL_account_confirmPasswordEmail tLRPC$TL_account_confirmPasswordEmail = new TLRPC$TL_account_confirmPasswordEmail();
            tLRPC$TL_account_confirmPasswordEmail.code = text;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_confirmPasswordEmail, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda35
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    PaymentFormActivity.this.lambda$sendSavePassword$42(tLObject, tLRPC$TL_error);
                }
            }, 10);
            return;
        }
        final TLRPC$TL_account_updatePasswordSettings tLRPC$TL_account_updatePasswordSettings = new TLRPC$TL_account_updatePasswordSettings();
        if (z) {
            this.doneItem.setVisibility(0);
            TLRPC$TL_account_passwordInputSettings tLRPC$TL_account_passwordInputSettings = new TLRPC$TL_account_passwordInputSettings();
            tLRPC$TL_account_updatePasswordSettings.new_settings = tLRPC$TL_account_passwordInputSettings;
            tLRPC$TL_account_passwordInputSettings.flags = 2;
            tLRPC$TL_account_passwordInputSettings.email = "";
            tLRPC$TL_account_updatePasswordSettings.password = new TLRPC$TL_inputCheckPasswordEmpty();
            str = null;
            str2 = null;
        } else {
            String obj = this.inputFields[0].getText().toString();
            if (TextUtils.isEmpty(obj)) {
                shakeField(0);
                return;
            } else if (!obj.equals(this.inputFields[1].getText().toString())) {
                try {
                    Toast.makeText(getParentActivity(), LocaleController.getString(R.string.PasswordDoNotMatch), 0).show();
                } catch (Exception e) {
                    FileLog.e(e);
                }
                shakeField(1);
                return;
            } else {
                String obj2 = this.inputFields[2].getText().toString();
                if (obj2.length() < 3) {
                    shakeField(2);
                    return;
                }
                int lastIndexOf = obj2.lastIndexOf(46);
                int lastIndexOf2 = obj2.lastIndexOf(64);
                if (lastIndexOf2 < 0 || lastIndexOf < lastIndexOf2) {
                    shakeField(2);
                    return;
                }
                tLRPC$TL_account_updatePasswordSettings.password = new TLRPC$TL_inputCheckPasswordEmpty();
                TLRPC$TL_account_passwordInputSettings tLRPC$TL_account_passwordInputSettings2 = new TLRPC$TL_account_passwordInputSettings();
                tLRPC$TL_account_updatePasswordSettings.new_settings = tLRPC$TL_account_passwordInputSettings2;
                int i = tLRPC$TL_account_passwordInputSettings2.flags;
                tLRPC$TL_account_passwordInputSettings2.flags = i | 1;
                tLRPC$TL_account_passwordInputSettings2.hint = "";
                tLRPC$TL_account_passwordInputSettings2.new_algo = this.currentPassword.new_algo;
                tLRPC$TL_account_passwordInputSettings2.flags = i | 3;
                tLRPC$TL_account_passwordInputSettings2.email = obj2.trim();
                str = obj2;
                str2 = obj;
            }
        }
        showEditDoneProgress(true, true);
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$sendSavePassword$48(z, str, str2, tLRPC$TL_account_updatePasswordSettings);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void sendSavedForm(final Runnable runnable) {
        TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug;
        if (this.canceled) {
            return;
        }
        showEditDoneProgress(true, true);
        TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo = new TLRPC$TL_payments_validateRequestedInfo();
        this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
        TLRPC$InputInvoice tLRPC$InputInvoice = this.invoiceInput;
        if (tLRPC$InputInvoice != null) {
            tLRPC$TL_payments_validateRequestedInfo.invoice = tLRPC$InputInvoice;
        } else {
            if (this.messageObject != null) {
                TLRPC$TL_inputInvoiceMessage tLRPC$TL_inputInvoiceMessage = new TLRPC$TL_inputInvoiceMessage();
                tLRPC$TL_inputInvoiceMessage.peer = getMessagesController().getInputPeer(this.messageObject.messageOwner.peer_id);
                tLRPC$TL_inputInvoiceMessage.msg_id = this.messageObject.getId();
                tLRPC$TL_inputInvoiceSlug = tLRPC$TL_inputInvoiceMessage;
            } else {
                TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug2 = new TLRPC$TL_inputInvoiceSlug();
                tLRPC$TL_inputInvoiceSlug2.slug = this.invoiceSlug;
                tLRPC$TL_inputInvoiceSlug = tLRPC$TL_inputInvoiceSlug2;
            }
            this.validateRequest.invoice = tLRPC$TL_inputInvoiceSlug;
        }
        final TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo2 = this.validateRequest;
        tLRPC$TL_payments_validateRequestedInfo2.save = true;
        tLRPC$TL_payments_validateRequestedInfo2.info = this.paymentForm.saved_info;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_validateRequestedInfo2, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda48
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$sendSavedForm$51(runnable, tLRPC$TL_payments_validateRequestedInfo2, tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAddressFields(TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo) {
        TLRPC$TL_postAddress tLRPC$TL_postAddress = tLRPC$TL_paymentRequestedInfo.shipping_address;
        if (tLRPC$TL_postAddress != null) {
            this.detailSettingsCell[2].setTextAndValueAndIcon(String.format("%s %s, %s, %s, %s, %s", tLRPC$TL_postAddress.street_line1, tLRPC$TL_postAddress.street_line2, tLRPC$TL_postAddress.city, tLRPC$TL_postAddress.state, tLRPC$TL_postAddress.country_iso2, tLRPC$TL_postAddress.post_code), LocaleController.getString(R.string.PaymentShippingAddress), R.drawable.msg_payment_address, true);
        }
        this.detailSettingsCell[2].setVisibility(tLRPC$TL_paymentRequestedInfo.shipping_address != null ? 0 : 8);
        String str = tLRPC$TL_paymentRequestedInfo.name;
        if (str != null) {
            this.detailSettingsCell[3].setTextAndValueAndIcon(str, LocaleController.getString(R.string.PaymentCheckoutName), R.drawable.msg_contacts, true);
        }
        this.detailSettingsCell[3].setVisibility(tLRPC$TL_paymentRequestedInfo.name != null ? 0 : 8);
        if (tLRPC$TL_paymentRequestedInfo.phone != null) {
            this.detailSettingsCell[4].setTextAndValueAndIcon(PhoneFormat.getInstance().format(tLRPC$TL_paymentRequestedInfo.phone), LocaleController.getString(R.string.PaymentCheckoutPhoneNumber), R.drawable.msg_calls, (tLRPC$TL_paymentRequestedInfo.email == null && this.shippingOption == null) ? false : true);
        }
        this.detailSettingsCell[4].setVisibility(tLRPC$TL_paymentRequestedInfo.phone != null ? 0 : 8);
        String str2 = tLRPC$TL_paymentRequestedInfo.email;
        if (str2 != null) {
            this.detailSettingsCell[5].setTextAndValueAndIcon(str2, LocaleController.getString(R.string.PaymentCheckoutEmail), R.drawable.msg_mention, this.shippingOption != null);
        }
        this.detailSettingsCell[5].setVisibility(tLRPC$TL_paymentRequestedInfo.email == null ? 8 : 0);
    }

    private void setCurrentPassword(TLRPC$account_Password tLRPC$account_Password) {
        if (tLRPC$account_Password == null || !tLRPC$account_Password.has_password) {
            this.currentPassword = tLRPC$account_Password;
            this.waitingForEmail = (tLRPC$account_Password == null || TextUtils.isEmpty(tLRPC$account_Password.email_unconfirmed_pattern)) ? false : true;
            updatePasswordFields();
        } else if (getParentActivity() == null) {
        } else {
            goToNextStep();
        }
    }

    private void setDelegate(PaymentFormActivityDelegate paymentFormActivityDelegate) {
        this.delegate = paymentFormActivityDelegate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDonePressed(boolean z) {
        this.donePressed = z;
        this.swipeBackEnabled = !z;
        ActionBar actionBar = this.actionBar;
        if (actionBar != null && actionBar.getBackButton() != null) {
            this.actionBar.getBackButton().setEnabled(!this.donePressed);
        }
        TextDetailSettingsCell textDetailSettingsCell = this.detailSettingsCell[0];
        if (textDetailSettingsCell != null) {
            textDetailSettingsCell.setEnabled(!this.donePressed);
        }
    }

    private void setPaymentMethod(TLRPC$TL_paymentFormMethod tLRPC$TL_paymentFormMethod) {
        this.paymentFormMethod = tLRPC$TL_paymentFormMethod;
    }

    private void shakeField(int i) {
        shakeView(this.inputFields[i]);
    }

    private void shakeView(View view) {
        try {
            view.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        AndroidUtilities.shakeViewSpring(view, 2.5f);
    }

    private void showAlertWithText(String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        builder.setTitle(str);
        builder.setMessage(str2);
        showDialog(builder.create());
    }

    private void showChoosePaymentMethod() {
        showChoosePaymentMethod(null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0033, code lost:
        if (r3 != null) goto L4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showChoosePaymentMethod(final Runnable runnable) {
        BottomSheet.Builder title = new BottomSheet.Builder(getParentActivity()).setTitle(LocaleController.getString(R.string.PaymentCheckoutMethod), true);
        final ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard = this.savedCredentialsCard;
        String str = tLRPC$TL_paymentSavedCredentialsCard != null ? tLRPC$TL_paymentSavedCredentialsCard.title : this.cardName;
        arrayList.add(str);
        arrayList2.add(Integer.valueOf(R.drawable.msg_payment_card));
        final ArrayList arrayList3 = new ArrayList();
        Iterator it = this.paymentForm.saved_credentials.iterator();
        while (it.hasNext()) {
            TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard2 = (TLRPC$TL_paymentSavedCredentialsCard) it.next();
            TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard3 = this.savedCredentialsCard;
            if (tLRPC$TL_paymentSavedCredentialsCard3 == null || !Objects.equals(tLRPC$TL_paymentSavedCredentialsCard2.id, tLRPC$TL_paymentSavedCredentialsCard3.id)) {
                arrayList.add(tLRPC$TL_paymentSavedCredentialsCard2.title);
                arrayList2.add(Integer.valueOf(R.drawable.msg_payment_card));
                arrayList3.add(tLRPC$TL_paymentSavedCredentialsCard2);
            }
        }
        Iterator it2 = this.paymentForm.additional_methods.iterator();
        while (it2.hasNext()) {
            arrayList.add(((TLRPC$TL_paymentFormMethod) it2.next()).title);
            arrayList2.add(Integer.valueOf(R.drawable.msg_payment_provider));
        }
        arrayList.add(LocaleController.getString(R.string.PaymentCheckoutMethodNewCard));
        arrayList2.add(Integer.valueOf(R.drawable.msg_addbot));
        int[] iArr = new int[arrayList2.size()];
        for (int i = 0; i < arrayList2.size(); i++) {
            iArr[i] = ((Integer) arrayList2.get(i)).intValue();
        }
        title.setItems((CharSequence[]) arrayList.toArray(new CharSequence[0]), iArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda44
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                PaymentFormActivity.this.lambda$showChoosePaymentMethod$31(runnable, arrayList3, arrayList, dialogInterface, i2);
            }
        });
        showDialog(title.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEditDoneProgress(boolean z, final boolean z2) {
        AnimatorSet animatorSet;
        AnimatorListenerAdapter animatorListenerAdapter;
        AnimatorSet animatorSet2 = this.doneItemAnimation;
        if (animatorSet2 != null) {
            animatorSet2.cancel();
        }
        if (z && this.doneItem != null) {
            AnimatorSet animatorSet3 = new AnimatorSet();
            this.doneItemAnimation = animatorSet3;
            if (z2) {
                this.progressView.setVisibility(0);
                this.doneItem.setEnabled(false);
                AnimatorSet animatorSet4 = this.doneItemAnimation;
                View contentView = this.doneItem.getContentView();
                Property property = View.SCALE_X;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(contentView, property, 0.1f);
                View contentView2 = this.doneItem.getContentView();
                Property property2 = View.SCALE_Y;
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(contentView2, property2, 0.1f);
                View contentView3 = this.doneItem.getContentView();
                Property property3 = View.ALPHA;
                animatorSet4.playTogether(ofFloat, ofFloat2, ObjectAnimator.ofFloat(contentView3, property3, 0.0f), ObjectAnimator.ofFloat(this.progressView, property, 1.0f), ObjectAnimator.ofFloat(this.progressView, property2, 1.0f), ObjectAnimator.ofFloat(this.progressView, property3, 1.0f));
            } else if (this.webView != null) {
                animatorSet3.playTogether(ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, 0.0f));
            } else {
                this.doneItem.getContentView().setVisibility(0);
                this.doneItem.setEnabled(true);
                AnimatorSet animatorSet5 = this.doneItemAnimation;
                ContextProgressView contextProgressView = this.progressView;
                Property property4 = View.SCALE_X;
                ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(contextProgressView, property4, 0.1f);
                ContextProgressView contextProgressView2 = this.progressView;
                Property property5 = View.SCALE_Y;
                ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(contextProgressView2, property5, 0.1f);
                ContextProgressView contextProgressView3 = this.progressView;
                Property property6 = View.ALPHA;
                animatorSet5.playTogether(ofFloat3, ofFloat4, ObjectAnimator.ofFloat(contextProgressView3, property6, 0.0f));
                if (!isFinishing()) {
                    this.doneItemAnimation.playTogether(ObjectAnimator.ofFloat(this.doneItem.getContentView(), property4, 1.0f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), property5, 1.0f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), property6, 1.0f));
                }
            }
            animatorSet = this.doneItemAnimation;
            animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: org.telegram.ui.PaymentFormActivity.27
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation == null || !PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    PaymentFormActivity.this.doneItemAnimation = null;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation == null || !PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    if (z2) {
                        PaymentFormActivity.this.doneItem.getContentView().setVisibility(4);
                    } else {
                        PaymentFormActivity.this.progressView.setVisibility(4);
                    }
                }
            };
        } else if (this.payTextView == null) {
            return;
        } else {
            this.doneItemAnimation = new AnimatorSet();
            if (z2) {
                this.progressViewButton.setVisibility(0);
                this.bottomLayout.setEnabled(false);
                AnimatorSet animatorSet6 = this.doneItemAnimation;
                TextView textView = this.payTextView;
                Property property7 = View.SCALE_X;
                ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(textView, property7, 0.1f);
                TextView textView2 = this.payTextView;
                Property property8 = View.SCALE_Y;
                ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(textView2, property8, 0.1f);
                TextView textView3 = this.payTextView;
                Property property9 = View.ALPHA;
                animatorSet6.playTogether(ofFloat5, ofFloat6, ObjectAnimator.ofFloat(textView3, property9, 0.0f), ObjectAnimator.ofFloat(this.progressViewButton, property7, 1.0f), ObjectAnimator.ofFloat(this.progressViewButton, property8, 1.0f), ObjectAnimator.ofFloat(this.progressViewButton, property9, 1.0f));
            } else {
                this.payTextView.setVisibility(0);
                this.bottomLayout.setEnabled(true);
                AnimatorSet animatorSet7 = this.doneItemAnimation;
                ContextProgressView contextProgressView4 = this.progressViewButton;
                Property property10 = View.SCALE_X;
                ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(contextProgressView4, property10, 0.1f);
                ContextProgressView contextProgressView5 = this.progressViewButton;
                Property property11 = View.SCALE_Y;
                ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat(contextProgressView5, property11, 0.1f);
                ContextProgressView contextProgressView6 = this.progressViewButton;
                Property property12 = View.ALPHA;
                animatorSet7.playTogether(ofFloat7, ofFloat8, ObjectAnimator.ofFloat(contextProgressView6, property12, 0.0f), ObjectAnimator.ofFloat(this.payTextView, property10, 1.0f), ObjectAnimator.ofFloat(this.payTextView, property11, 1.0f), ObjectAnimator.ofFloat(this.payTextView, property12, 1.0f));
            }
            animatorSet = this.doneItemAnimation;
            animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: org.telegram.ui.PaymentFormActivity.28
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation == null || !PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    PaymentFormActivity.this.doneItemAnimation = null;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation == null || !PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    if (z2) {
                        PaymentFormActivity.this.payTextView.setVisibility(4);
                    } else {
                        PaymentFormActivity.this.progressViewButton.setVisibility(4);
                    }
                }
            };
        }
        animatorSet.addListener(animatorListenerAdapter);
        this.doneItemAnimation.setDuration(150L);
        this.doneItemAnimation.start();
    }

    private void showPayAlert(String str) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString(R.string.PaymentTransactionReview));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("PaymentTransactionMessage2", R.string.PaymentTransactionMessage2, str, this.currentBotName, this.currentItemName)));
        builder.setPositiveButton(LocaleController.getString(R.string.Continue), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda49
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PaymentFormActivity.this.lambda$showPayAlert$36(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
        showDialog(builder.create());
    }

    private void updatePasswordFields() {
        int i = 0;
        if (this.currentStep != 6 || this.bottomCell[2] == null) {
            return;
        }
        this.doneItem.setVisibility(0);
        if (this.currentPassword == null) {
            showEditDoneProgress(true, true);
            this.bottomCell[2].setVisibility(8);
            this.settingsCell[0].setVisibility(8);
            this.settingsCell[1].setVisibility(8);
            this.codeFieldCell.setVisibility(8);
            this.headerCell[0].setVisibility(8);
            this.headerCell[1].setVisibility(8);
            this.bottomCell[0].setVisibility(8);
            for (int i2 = 0; i2 < 3; i2++) {
                ((View) this.inputFields[i2].getParent()).setVisibility(8);
            }
            while (i < this.dividers.size()) {
                ((View) this.dividers.get(i)).setVisibility(8);
                i++;
            }
            return;
        }
        showEditDoneProgress(true, false);
        if (!this.waitingForEmail) {
            this.bottomCell[2].setVisibility(8);
            this.settingsCell[0].setVisibility(8);
            this.settingsCell[1].setVisibility(8);
            this.bottomCell[1].setText(LocaleController.getString(R.string.PaymentPasswordEmailInfo));
            this.codeFieldCell.setVisibility(8);
            this.headerCell[0].setVisibility(0);
            this.headerCell[1].setVisibility(0);
            this.bottomCell[0].setVisibility(0);
            for (int i3 = 0; i3 < 3; i3++) {
                ((View) this.inputFields[i3].getParent()).setVisibility(0);
            }
            for (int i4 = 0; i4 < this.dividers.size(); i4++) {
                ((View) this.dividers.get(i4)).setVisibility(0);
            }
            return;
        }
        TextInfoPrivacyCell textInfoPrivacyCell = this.bottomCell[2];
        int i5 = R.string.EmailPasswordConfirmText2;
        String str = this.currentPassword.email_unconfirmed_pattern;
        if (str == null) {
            str = "";
        }
        textInfoPrivacyCell.setText(LocaleController.formatString("EmailPasswordConfirmText2", i5, str));
        this.bottomCell[2].setVisibility(0);
        this.settingsCell[0].setVisibility(0);
        this.settingsCell[1].setVisibility(0);
        this.codeFieldCell.setVisibility(0);
        this.bottomCell[1].setText("");
        this.headerCell[0].setVisibility(8);
        this.headerCell[1].setVisibility(8);
        this.bottomCell[0].setVisibility(8);
        for (int i6 = 0; i6 < 3; i6++) {
            ((View) this.inputFields[i6].getParent()).setVisibility(8);
        }
        while (i < this.dividers.size()) {
            ((View) this.dividers.get(i)).setVisibility(8);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSavePaymentField() {
        ShadowSectionCell shadowSectionCell;
        Context context;
        int i;
        if (this.bottomCell[0] == null || this.sectionCell[2] == null) {
            return;
        }
        TLRPC$PaymentForm tLRPC$PaymentForm = this.paymentForm;
        if ((tLRPC$PaymentForm.password_missing || tLRPC$PaymentForm.can_save_credentials) && (this.webView == null || !this.webviewLoading)) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString(R.string.PaymentCardSavePaymentInformationInfoLine1));
            if (this.paymentForm.password_missing) {
                loadPasswordInfo();
                spannableStringBuilder.append((CharSequence) "\n");
                int length = spannableStringBuilder.length();
                String string = LocaleController.getString(R.string.PaymentCardSavePaymentInformationInfoLine2);
                int indexOf = string.indexOf(42);
                int lastIndexOf = string.lastIndexOf(42);
                spannableStringBuilder.append((CharSequence) string);
                if (indexOf != -1 && lastIndexOf != -1) {
                    int i2 = indexOf + length;
                    int i3 = lastIndexOf + length;
                    this.bottomCell[0].getTextView().setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                    spannableStringBuilder.replace(i3, i3 + 1, (CharSequence) "");
                    spannableStringBuilder.replace(i2, i2 + 1, (CharSequence) "");
                    spannableStringBuilder.setSpan(new LinkSpan(), i2, i3 - 1, 33);
                }
            }
            this.checkCell1.setEnabled(true);
            this.bottomCell[0].setText(spannableStringBuilder);
            this.checkCell1.setVisibility(0);
            this.bottomCell[0].setVisibility(0);
            shadowSectionCell = this.sectionCell[2];
            context = shadowSectionCell.getContext();
            i = R.drawable.greydivider;
        } else {
            this.checkCell1.setVisibility(8);
            this.bottomCell[0].setVisibility(8);
            shadowSectionCell = this.sectionCell[2];
            context = shadowSectionCell.getContext();
            i = R.drawable.greydivider_bottom;
        }
        shadowSectionCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i, Theme.key_windowBackgroundGrayShadow));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTotalPrice() {
        int i;
        this.totalPrice[0] = getTotalPriceString(this.prices);
        this.totalCell.setTextAndValue(LocaleController.getString(R.string.PaymentTransactionTotal), this.totalPrice[0], true);
        TextView textView = this.payTextView;
        if (textView != null) {
            textView.setText(LocaleController.formatString("PaymentCheckoutPay", R.string.PaymentCheckoutPay, this.totalPrice[0]));
        }
        if (this.tipLayout != null) {
            int themedColor = getThemedColor(Theme.key_contacts_inviteBackground);
            int childCount = this.tipLayout.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                TextView textView2 = (TextView) this.tipLayout.getChildAt(i2);
                if (textView2.getTag().equals(this.tipAmount)) {
                    Theme.setDrawableColor(textView2.getBackground(), themedColor);
                    i = Theme.key_contacts_inviteText;
                } else {
                    Theme.setDrawableColor(textView2.getBackground(), 536870911 & themedColor);
                    i = Theme.key_chats_secretName;
                }
                textView2.setTextColor(getThemedColor(i));
                textView2.invalidate();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:222:0x077d, code lost:
        if (r4.email_requested == false) goto L211;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x0794, code lost:
        if (r4.email_requested == false) goto L211;
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x0797, code lost:
        r4 = 8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0ee0, code lost:
        if (r9.email_requested == false) goto L479;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x02cf, code lost:
        if (r15.email_requested == false) goto L176;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:106:0x03a4  */
    /* JADX WARN: Removed duplicated region for block: B:112:0x03c4  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x03e0  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x03fe  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x0434  */
    /* JADX WARN: Removed duplicated region for block: B:136:0x0453  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0472  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0491  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x04bf  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0534  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x05af  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x07c2  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x084c  */
    /* JADX WARN: Removed duplicated region for block: B:345:0x0a57  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x0a87  */
    /* JADX WARN: Removed duplicated region for block: B:361:0x0ad1  */
    /* JADX WARN: Removed duplicated region for block: B:377:0x0b0a  */
    /* JADX WARN: Removed duplicated region for block: B:379:0x0b13  */
    /* JADX WARN: Removed duplicated region for block: B:381:0x0b20  */
    /* JADX WARN: Removed duplicated region for block: B:385:0x0b41  */
    /* JADX WARN: Removed duplicated region for block: B:386:0x0b43  */
    /* JADX WARN: Removed duplicated region for block: B:389:0x0b6f  */
    /* JADX WARN: Removed duplicated region for block: B:391:0x0b8b  */
    /* JADX WARN: Removed duplicated region for block: B:401:0x0c27  */
    /* JADX WARN: Removed duplicated region for block: B:404:0x0c46  */
    /* JADX WARN: Removed duplicated region for block: B:524:0x1264  */
    /* JADX WARN: Removed duplicated region for block: B:525:0x1281  */
    /* JADX WARN: Removed duplicated region for block: B:533:0x131d  */
    /* JADX WARN: Removed duplicated region for block: B:540:0x1338  */
    /* JADX WARN: Removed duplicated region for block: B:543:0x1361  */
    /* JADX WARN: Removed duplicated region for block: B:544:0x1363  */
    /* JADX WARN: Removed duplicated region for block: B:547:0x138e  */
    /* JADX WARN: Removed duplicated region for block: B:549:0x13c5  */
    /* JADX WARN: Removed duplicated region for block: B:622:0x1869  */
    /* JADX WARN: Removed duplicated region for block: B:626:0x1881  */
    /* JADX WARN: Removed duplicated region for block: B:632:0x18b0  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x025d  */
    /* JADX WARN: Removed duplicated region for block: B:645:0x18ed  */
    /* JADX WARN: Removed duplicated region for block: B:648:0x18f6  */
    /* JADX WARN: Removed duplicated region for block: B:649:0x18f8  */
    /* JADX WARN: Removed duplicated region for block: B:659:0x190f  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0280  */
    /* JADX WARN: Removed duplicated region for block: B:660:0x1912  */
    /* JADX WARN: Removed duplicated region for block: B:663:0x1936  */
    /* JADX WARN: Removed duplicated region for block: B:669:0x1988  */
    /* JADX WARN: Removed duplicated region for block: B:675:0x19da  */
    /* JADX WARN: Removed duplicated region for block: B:681:0x1a2a  */
    /* JADX WARN: Removed duplicated region for block: B:687:0x1a60  */
    /* JADX WARN: Removed duplicated region for block: B:691:0x1a9a  */
    /* JADX WARN: Removed duplicated region for block: B:754:0x06e8 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x02f2  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x02fc  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x035d  */
    /* JADX WARN: Type inference failed for: r14v2 */
    /* JADX WARN: Type inference failed for: r14v3, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r14v6 */
    /* JADX WARN: Type inference failed for: r14v93, types: [android.widget.LinearLayout, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r2v51, types: [org.telegram.ui.Cells.RecurrentPaymentsAcceptCell] */
    /* JADX WARN: Type inference failed for: r3v118, types: [org.telegram.ui.PaymentFormActivity$BottomFrameLayout] */
    /* JADX WARN: Type inference failed for: r3v120, types: [org.telegram.ui.ActionBar.ActionBarMenuItem, android.view.View] */
    /* JADX WARN: Type inference failed for: r3v129, types: [android.text.SpannableStringBuilder, java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r3v61, types: [org.telegram.ui.Cells.TextDetailSettingsCell[]] */
    /* JADX WARN: Type inference failed for: r3v62, types: [org.telegram.ui.Cells.TextDetailSettingsCell] */
    /* JADX WARN: Type inference failed for: r3v64, types: [org.telegram.ui.Cells.TextDetailSettingsCell[]] */
    /* JADX WARN: Type inference failed for: r3v65, types: [android.view.View] */
    /* JADX WARN: Type inference failed for: r3v73, types: [org.telegram.ui.Cells.TextDetailSettingsCell[]] */
    /* JADX WARN: Type inference failed for: r3v74, types: [android.view.View] */
    /* JADX WARN: Type inference failed for: r3v82, types: [org.telegram.ui.Cells.TextDetailSettingsCell[]] */
    /* JADX WARN: Type inference failed for: r3v83, types: [android.view.View] */
    /* JADX WARN: Type inference failed for: r3v91, types: [org.telegram.ui.Cells.TextDetailSettingsCell[]] */
    /* JADX WARN: Type inference failed for: r3v92, types: [android.view.View] */
    /* JADX WARN: Type inference failed for: r4v109, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r6v172, types: [android.widget.LinearLayout, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r6v31, types: [java.lang.CharSequence, android.text.SpannableString, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r9v11, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r9v123, types: [android.widget.LinearLayout, android.view.View, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r9v94, types: [android.widget.FrameLayout, android.view.View, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r9v97, types: [android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r9v98, types: [android.view.ViewGroup] */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        String str;
        ?? r14;
        char c;
        char c2;
        int i;
        int i2;
        TLRPC$User tLRPC$User;
        char c3;
        final String str2;
        TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo;
        TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo;
        char c4;
        LinearLayout linearLayout;
        View view;
        TLRPC$PaymentForm tLRPC$PaymentForm;
        int i3;
        int i4;
        TLRPC$PaymentForm tLRPC$PaymentForm2;
        TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo2;
        String str3;
        int i5;
        LinearLayout linearLayout2;
        TextInfoPrivacyCell textInfoPrivacyCell;
        EditTextBoldCursor editTextBoldCursor;
        int i6;
        int i7;
        int i8;
        EditTextBoldCursor editTextBoldCursor2;
        LinearLayout linearLayout3;
        HeaderCell headerCell;
        boolean z;
        EditTextBoldCursor editTextBoldCursor3;
        int i9;
        EditTextBoldCursor editTextBoldCursor4;
        int i10;
        EditTextBoldCursor editTextBoldCursor5;
        TextWatcher textWatcher;
        LinearLayout linearLayout4;
        View view2;
        int i11;
        int i12;
        EditTextBoldCursor editTextBoldCursor6;
        int i13;
        TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo3;
        TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo4;
        String str4;
        String str5;
        TelephonyManager telephonyManager;
        LinearLayout linearLayout5;
        HeaderCell headerCell2;
        ?? frameLayout;
        TextInfoPrivacyCell textInfoPrivacyCell2;
        String formatString;
        TLRPC$TL_postAddress tLRPC$TL_postAddress;
        EditTextBoldCursor editTextBoldCursor7;
        String str6;
        TLRPC$TL_postAddress tLRPC$TL_postAddress2;
        TLRPC$TL_postAddress tLRPC$TL_postAddress3;
        TLRPC$TL_postAddress tLRPC$TL_postAddress4;
        TLRPC$TL_postAddress tLRPC$TL_postAddress5;
        TLRPC$TL_postAddress tLRPC$TL_postAddress6;
        BufferedReader bufferedReader;
        ActionBar actionBar;
        int i14;
        String str7;
        StringBuilder sb;
        int i15;
        switch (this.currentStep) {
            case 0:
                actionBar = this.actionBar;
                i14 = R.string.PaymentShippingInfo;
                str7 = LocaleController.getString(i14);
                actionBar.setTitle(str7);
                break;
            case 1:
                actionBar = this.actionBar;
                i14 = R.string.PaymentShippingMethod;
                str7 = LocaleController.getString(i14);
                actionBar.setTitle(str7);
                break;
            case 2:
            case 3:
                TLRPC$TL_paymentFormMethod tLRPC$TL_paymentFormMethod = this.paymentFormMethod;
                if (tLRPC$TL_paymentFormMethod == null || TextUtils.isEmpty(tLRPC$TL_paymentFormMethod.title)) {
                    actionBar = this.actionBar;
                    i14 = R.string.PaymentCardInfo;
                    str7 = LocaleController.getString(i14);
                    actionBar.setTitle(str7);
                    break;
                } else {
                    actionBar = this.actionBar;
                    str7 = this.paymentFormMethod.title;
                    actionBar.setTitle(str7);
                }
                break;
            case 4:
                if (this.paymentForm.invoice.test) {
                    actionBar = this.actionBar;
                    sb = new StringBuilder();
                    sb.append("Test ");
                    i15 = R.string.PaymentCheckout;
                    sb.append(LocaleController.getString(i15));
                    str7 = sb.toString();
                    actionBar.setTitle(str7);
                    break;
                } else {
                    actionBar = this.actionBar;
                    i14 = R.string.PaymentCheckout;
                    str7 = LocaleController.getString(i14);
                    actionBar.setTitle(str7);
                }
            case 5:
                if (this.paymentForm.invoice.test) {
                    actionBar = this.actionBar;
                    sb = new StringBuilder();
                    sb.append("Test ");
                    i15 = R.string.PaymentReceipt;
                    sb.append(LocaleController.getString(i15));
                    str7 = sb.toString();
                    actionBar.setTitle(str7);
                    break;
                } else {
                    actionBar = this.actionBar;
                    i14 = R.string.PaymentReceipt;
                    str7 = LocaleController.getString(i14);
                    actionBar.setTitle(str7);
                }
            case 6:
                this.actionBar.setTitle(LocaleController.getString(R.string.PaymentPassword));
                break;
        }
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.PaymentFormActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i16) {
                if (i16 == -1) {
                    if (PaymentFormActivity.this.donePressed) {
                        return;
                    }
                    PaymentFormActivity.this.finishFragment();
                } else if (i16 != 1 || PaymentFormActivity.this.donePressed) {
                } else {
                    if (PaymentFormActivity.this.currentStep != 3) {
                        AndroidUtilities.hideKeyboard(PaymentFormActivity.this.getParentActivity().getCurrentFocus());
                    }
                    int i17 = PaymentFormActivity.this.currentStep;
                    if (i17 == 0) {
                        PaymentFormActivity.this.setDonePressed(true);
                        PaymentFormActivity.this.sendForm();
                        return;
                    }
                    int i18 = 0;
                    if (i17 == 1) {
                        while (true) {
                            if (i18 >= PaymentFormActivity.this.radioCells.length) {
                                break;
                            } else if (PaymentFormActivity.this.radioCells[i18].isChecked()) {
                                PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                                paymentFormActivity.shippingOption = (TLRPC$TL_shippingOption) paymentFormActivity.requestedInfo.shipping_options.get(i18);
                                break;
                            } else {
                                i18++;
                            }
                        }
                        PaymentFormActivity.this.goToNextStep();
                    } else if (i17 == 2) {
                        PaymentFormActivity.this.sendCardData();
                    } else if (i17 == 3) {
                        PaymentFormActivity.this.checkPassword();
                    } else if (i17 != 6) {
                    } else {
                        PaymentFormActivity.this.sendSavePassword(false);
                    }
                }
            }
        });
        ActionBarMenu createMenu = this.actionBar.createMenu();
        int i16 = this.currentStep;
        int i17 = 3;
        int i18 = -1;
        if (i16 == 0 || i16 == 1 || i16 == 2 || i16 == 3 || i16 == 4 || i16 == 6) {
            this.doneItem = createMenu.addItemWithWidth(1, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f), LocaleController.getString(R.string.Done));
            ContextProgressView contextProgressView = new ContextProgressView(context, 1);
            this.progressView = contextProgressView;
            contextProgressView.setAlpha(0.0f);
            this.progressView.setScaleX(0.1f);
            this.progressView.setScaleY(0.1f);
            this.progressView.setVisibility(4);
            this.doneItem.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0f));
        }
        FrameLayout frameLayout2 = new FrameLayout(context);
        this.fragmentView = frameLayout2;
        frameLayout2.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundGray));
        ScrollView scrollView = new ScrollView(context);
        this.scrollView = scrollView;
        scrollView.setFillViewport(true);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, getThemedColor(Theme.key_actionBarDefault));
        frameLayout2.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.currentStep == 4 ? 48.0f : 0.0f));
        LinearLayout linearLayout6 = new LinearLayout(context);
        this.linearLayout2 = linearLayout6;
        linearLayout6.setOrientation(1);
        this.linearLayout2.setClipChildren(false);
        int i19 = -2;
        this.scrollView.addView(this.linearLayout2, new FrameLayout.LayoutParams(-1, -2));
        int i20 = this.currentStep;
        String str8 = "";
        if (i20 == 0) {
            HashMap hashMap = new HashMap();
            HashMap hashMap2 = new HashMap();
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("countries.txt")));
            } catch (Exception e) {
                FileLog.e(e);
            }
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String[] split = readLine.split(";");
                    this.countriesArray.add(0, split[2]);
                    this.countriesMap.put(split[2], split[0]);
                    this.codesMap.put(split[0], split[2]);
                    hashMap2.put(split[1], split[2]);
                    if (split.length > 3) {
                        this.phoneFormatMap.put(split[0], split[3]);
                    }
                    hashMap.put(split[1], split[2]);
                } else {
                    bufferedReader.close();
                    Collections.sort(this.countriesArray, new BoostRepository$$ExternalSyntheticLambda31());
                    int i21 = 10;
                    this.inputFields = new EditTextBoldCursor[10];
                    int i22 = 0;
                    while (i22 < i21) {
                        if (i22 == 0) {
                            this.headerCell[0] = new HeaderCell(context, this.resourcesProvider);
                            this.headerCell[0].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            this.headerCell[0].setText(LocaleController.getString(R.string.PaymentShippingAddress));
                            linearLayout5 = this.linearLayout2;
                            headerCell2 = this.headerCell[0];
                        } else {
                            if (i22 == 6) {
                                this.sectionCell[0] = new ShadowSectionCell(context, this.resourcesProvider);
                                this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(i18, i19));
                                this.headerCell[1] = new HeaderCell(context, this.resourcesProvider);
                                this.headerCell[1].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                                this.headerCell[1].setText(LocaleController.getString(R.string.PaymentShippingReceiver));
                                linearLayout5 = this.linearLayout2;
                                headerCell2 = this.headerCell[1];
                            }
                            if (i22 != 8) {
                                frameLayout = new LinearLayout(context);
                                frameLayout.setClipChildren(false);
                                frameLayout.setOrientation(0);
                                this.linearLayout2.addView(frameLayout, LayoutHelper.createLinear(i18, 50));
                                frameLayout.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            } else if (i22 == 9) {
                                frameLayout = (ViewGroup) this.inputFields[8].getParent();
                            } else {
                                frameLayout = new FrameLayout(context);
                                frameLayout.setClipChildren(false);
                                this.linearLayout2.addView(frameLayout, LayoutHelper.createLinear(i18, 50));
                                int i23 = Theme.key_windowBackgroundWhite;
                                frameLayout.setBackgroundColor(getThemedColor(i23));
                                boolean z2 = i22 != 5;
                                if (z2) {
                                    if (i22 != 7 || this.paymentForm.invoice.phone_requested) {
                                        if (i22 == 6) {
                                            TLRPC$TL_invoice tLRPC$TL_invoice = this.paymentForm.invoice;
                                            if (!tLRPC$TL_invoice.phone_requested) {
                                            }
                                        }
                                    }
                                    z2 = false;
                                }
                                if (z2) {
                                    View view3 = new View(context) { // from class: org.telegram.ui.PaymentFormActivity.2
                                        @Override // android.view.View
                                        protected void onDraw(Canvas canvas) {
                                            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                                        }
                                    };
                                    view3.setBackgroundColor(getThemedColor(i23));
                                    this.dividers.add(view3);
                                    frameLayout.addView(view3, new FrameLayout.LayoutParams(i18, 1, 83));
                                }
                            }
                            if (i22 != 9) {
                                this.inputFields[i22] = new HintEditText(context);
                            } else {
                                this.inputFields[i22] = new EditTextBoldCursor(context);
                            }
                            this.inputFields[i22].setTag(Integer.valueOf(i22));
                            this.inputFields[i22].setTextSize(1, 16.0f);
                            this.inputFields[i22].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                            EditTextBoldCursor editTextBoldCursor8 = this.inputFields[i22];
                            int i24 = Theme.key_windowBackgroundWhiteBlackText;
                            editTextBoldCursor8.setTextColor(getThemedColor(i24));
                            this.inputFields[i22].setBackgroundDrawable(null);
                            this.inputFields[i22].setCursorColor(getThemedColor(i24));
                            this.inputFields[i22].setCursorSize(AndroidUtilities.dp(20.0f));
                            this.inputFields[i22].setCursorWidth(1.5f);
                            if (i22 == 4) {
                                this.inputFields[i22].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda14
                                    @Override // android.view.View.OnTouchListener
                                    public final boolean onTouch(View view4, MotionEvent motionEvent) {
                                        boolean lambda$createView$1;
                                        lambda$createView$1 = PaymentFormActivity.this.lambda$createView$1(view4, motionEvent);
                                        return lambda$createView$1;
                                    }
                                });
                                this.inputFields[i22].setInputType(0);
                            }
                            if (i22 != 9 || i22 == 8) {
                                this.inputFields[i22].setInputType(i17);
                            } else if (i22 == 7) {
                                this.inputFields[i22].setInputType(1);
                            } else {
                                this.inputFields[i22].setInputType(16385);
                            }
                            this.inputFields[i22].setImeOptions(268435461);
                            switch (i22) {
                                case 0:
                                    this.inputFields[i22].setHint(LocaleController.getString(R.string.PaymentShippingAddress1Placeholder));
                                    TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo5 = this.paymentForm.saved_info;
                                    if (tLRPC$TL_paymentRequestedInfo5 != null && (tLRPC$TL_postAddress = tLRPC$TL_paymentRequestedInfo5.shipping_address) != null) {
                                        editTextBoldCursor7 = this.inputFields[i22];
                                        str6 = tLRPC$TL_postAddress.street_line1;
                                        editTextBoldCursor7.setText(str6);
                                        break;
                                    }
                                    break;
                                case 1:
                                    this.inputFields[i22].setHint(LocaleController.getString(R.string.PaymentShippingAddress2Placeholder));
                                    TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo6 = this.paymentForm.saved_info;
                                    if (tLRPC$TL_paymentRequestedInfo6 != null && (tLRPC$TL_postAddress2 = tLRPC$TL_paymentRequestedInfo6.shipping_address) != null) {
                                        editTextBoldCursor7 = this.inputFields[i22];
                                        str6 = tLRPC$TL_postAddress2.street_line2;
                                        editTextBoldCursor7.setText(str6);
                                        break;
                                    }
                                    break;
                                case 2:
                                    this.inputFields[i22].setHint(LocaleController.getString(R.string.PaymentShippingCityPlaceholder));
                                    TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo7 = this.paymentForm.saved_info;
                                    if (tLRPC$TL_paymentRequestedInfo7 != null && (tLRPC$TL_postAddress3 = tLRPC$TL_paymentRequestedInfo7.shipping_address) != null) {
                                        editTextBoldCursor7 = this.inputFields[i22];
                                        str6 = tLRPC$TL_postAddress3.city;
                                        editTextBoldCursor7.setText(str6);
                                        break;
                                    }
                                    break;
                                case 3:
                                    this.inputFields[i22].setHint(LocaleController.getString(R.string.PaymentShippingStatePlaceholder));
                                    TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo8 = this.paymentForm.saved_info;
                                    if (tLRPC$TL_paymentRequestedInfo8 != null && (tLRPC$TL_postAddress4 = tLRPC$TL_paymentRequestedInfo8.shipping_address) != null) {
                                        editTextBoldCursor7 = this.inputFields[i22];
                                        str6 = tLRPC$TL_postAddress4.state;
                                        editTextBoldCursor7.setText(str6);
                                        break;
                                    }
                                    break;
                                case 4:
                                    this.inputFields[i22].setHint(LocaleController.getString(R.string.PaymentShippingCountry));
                                    TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo9 = this.paymentForm.saved_info;
                                    if (tLRPC$TL_paymentRequestedInfo9 != null && (tLRPC$TL_postAddress5 = tLRPC$TL_paymentRequestedInfo9.shipping_address) != null) {
                                        String str9 = (String) hashMap2.get(tLRPC$TL_postAddress5.country_iso2);
                                        String str10 = this.paymentForm.saved_info.shipping_address.country_iso2;
                                        this.countryName = str10;
                                        EditTextBoldCursor editTextBoldCursor9 = this.inputFields[i22];
                                        if (str9 == null) {
                                            str9 = str10;
                                        }
                                        editTextBoldCursor9.setText(str9);
                                        break;
                                    }
                                    break;
                                case 5:
                                    this.inputFields[i22].setHint(LocaleController.getString(R.string.PaymentShippingZipPlaceholder));
                                    TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo10 = this.paymentForm.saved_info;
                                    if (tLRPC$TL_paymentRequestedInfo10 != null && (tLRPC$TL_postAddress6 = tLRPC$TL_paymentRequestedInfo10.shipping_address) != null) {
                                        editTextBoldCursor7 = this.inputFields[i22];
                                        str6 = tLRPC$TL_postAddress6.post_code;
                                        editTextBoldCursor7.setText(str6);
                                        break;
                                    }
                                    break;
                                case 6:
                                    this.inputFields[i22].setHint(LocaleController.getString(R.string.PaymentShippingName));
                                    TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo11 = this.paymentForm.saved_info;
                                    if (tLRPC$TL_paymentRequestedInfo11 != null && (str6 = tLRPC$TL_paymentRequestedInfo11.name) != null) {
                                        editTextBoldCursor7 = this.inputFields[i22];
                                        editTextBoldCursor7.setText(str6);
                                        break;
                                    }
                                    break;
                                case 7:
                                    this.inputFields[i22].setHint(LocaleController.getString(R.string.PaymentShippingEmailPlaceholder));
                                    TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo12 = this.paymentForm.saved_info;
                                    if (tLRPC$TL_paymentRequestedInfo12 != null && (str6 = tLRPC$TL_paymentRequestedInfo12.email) != null) {
                                        editTextBoldCursor7 = this.inputFields[i22];
                                        editTextBoldCursor7.setText(str6);
                                        break;
                                    }
                                    break;
                            }
                            EditTextBoldCursor editTextBoldCursor10 = this.inputFields[i22];
                            editTextBoldCursor10.setSelection(editTextBoldCursor10.length());
                            if (i22 != 8) {
                                TextView textView = new TextView(context);
                                this.textView = textView;
                                textView.setText("+");
                                this.textView.setTextColor(getThemedColor(i24));
                                this.textView.setTextSize(1, 16.0f);
                                frameLayout.addView(this.textView, LayoutHelper.createLinear(-2, -2, 21.0f, 12.0f, 0.0f, 6.0f));
                                this.inputFields[i22].setPadding(AndroidUtilities.dp(10.0f), 0, 0, 0);
                                this.inputFields[i22].setGravity(19);
                                this.inputFields[i22].setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                                frameLayout.addView(this.inputFields[i22], LayoutHelper.createLinear(55, -2, 0.0f, 12.0f, 21.0f, 6.0f));
                                this.inputFields[i22].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.3
                                    @Override // android.text.TextWatcher
                                    public void afterTextChanged(Editable editable) {
                                        String str11;
                                        boolean z3;
                                        String str12;
                                        if (PaymentFormActivity.this.ignoreOnTextChange) {
                                            return;
                                        }
                                        PaymentFormActivity.this.ignoreOnTextChange = true;
                                        String stripExceptNumbers = PhoneFormat.stripExceptNumbers(PaymentFormActivity.this.inputFields[8].getText().toString());
                                        PaymentFormActivity.this.inputFields[8].setText(stripExceptNumbers);
                                        HintEditText hintEditText = (HintEditText) PaymentFormActivity.this.inputFields[9];
                                        if (stripExceptNumbers.length() == 0) {
                                            hintEditText.setHintText((String) null);
                                            hintEditText.setHint(LocaleController.getString(R.string.PaymentShippingPhoneNumber));
                                        } else {
                                            int i25 = 4;
                                            if (stripExceptNumbers.length() > 4) {
                                                while (true) {
                                                    if (i25 < 1) {
                                                        str11 = null;
                                                        z3 = false;
                                                        break;
                                                    }
                                                    String substring = stripExceptNumbers.substring(0, i25);
                                                    if (((String) PaymentFormActivity.this.codesMap.get(substring)) != null) {
                                                        String str13 = stripExceptNumbers.substring(i25) + PaymentFormActivity.this.inputFields[9].getText().toString();
                                                        PaymentFormActivity.this.inputFields[8].setText(substring);
                                                        z3 = true;
                                                        str11 = str13;
                                                        stripExceptNumbers = substring;
                                                        break;
                                                    }
                                                    i25--;
                                                }
                                                if (!z3) {
                                                    str11 = stripExceptNumbers.substring(1) + PaymentFormActivity.this.inputFields[9].getText().toString();
                                                    EditTextBoldCursor editTextBoldCursor11 = PaymentFormActivity.this.inputFields[8];
                                                    stripExceptNumbers = stripExceptNumbers.substring(0, 1);
                                                    editTextBoldCursor11.setText(stripExceptNumbers);
                                                }
                                            } else {
                                                str11 = null;
                                                z3 = false;
                                            }
                                            String str14 = (String) PaymentFormActivity.this.codesMap.get(stripExceptNumbers);
                                            if (str14 == null || PaymentFormActivity.this.countriesArray.indexOf(str14) == -1 || (str12 = (String) PaymentFormActivity.this.phoneFormatMap.get(stripExceptNumbers)) == null) {
                                                hintEditText.setHintText((String) null);
                                                hintEditText.setHint(LocaleController.getString(R.string.PaymentShippingPhoneNumber));
                                            } else {
                                                hintEditText.setHintText(str12.replace('X', (char) 8211));
                                                hintEditText.setHint((CharSequence) null);
                                            }
                                            if (!z3) {
                                                PaymentFormActivity.this.inputFields[8].setSelection(PaymentFormActivity.this.inputFields[8].getText().length());
                                            }
                                            if (str11 != null) {
                                                hintEditText.requestFocus();
                                                hintEditText.setText(str11);
                                                hintEditText.setSelection(hintEditText.length());
                                            }
                                        }
                                        PaymentFormActivity.this.ignoreOnTextChange = false;
                                    }

                                    @Override // android.text.TextWatcher
                                    public void beforeTextChanged(CharSequence charSequence, int i25, int i26, int i27) {
                                    }

                                    @Override // android.text.TextWatcher
                                    public void onTextChanged(CharSequence charSequence, int i25, int i26, int i27) {
                                    }
                                });
                            } else if (i22 == 9) {
                                this.inputFields[i22].setPadding(0, 0, 0, 0);
                                this.inputFields[i22].setGravity(19);
                                frameLayout.addView(this.inputFields[i22], LayoutHelper.createLinear(-1, -2, 0.0f, 12.0f, 21.0f, 6.0f));
                                this.inputFields[i22].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.4
                                    private int actionPosition;
                                    private int characterAction = -1;

                                    @Override // android.text.TextWatcher
                                    public void afterTextChanged(Editable editable) {
                                        int i25;
                                        int i26;
                                        if (PaymentFormActivity.this.ignoreOnPhoneChange) {
                                            return;
                                        }
                                        HintEditText hintEditText = (HintEditText) PaymentFormActivity.this.inputFields[9];
                                        int selectionStart = hintEditText.getSelectionStart();
                                        String obj = hintEditText.getText().toString();
                                        if (this.characterAction == 3) {
                                            obj = obj.substring(0, this.actionPosition) + obj.substring(this.actionPosition + 1);
                                            selectionStart--;
                                        }
                                        StringBuilder sb2 = new StringBuilder(obj.length());
                                        int i27 = 0;
                                        while (i27 < obj.length()) {
                                            int i28 = i27 + 1;
                                            String substring = obj.substring(i27, i28);
                                            if ("0123456789".contains(substring)) {
                                                sb2.append(substring);
                                            }
                                            i27 = i28;
                                        }
                                        PaymentFormActivity.this.ignoreOnPhoneChange = true;
                                        String hintText = hintEditText.getHintText();
                                        if (hintText != null) {
                                            int i29 = 0;
                                            while (true) {
                                                if (i29 >= sb2.length()) {
                                                    break;
                                                } else if (i29 < hintText.length()) {
                                                    if (hintText.charAt(i29) == ' ') {
                                                        sb2.insert(i29, ' ');
                                                        i29++;
                                                        if (selectionStart == i29 && (i26 = this.characterAction) != 2 && i26 != 3) {
                                                            selectionStart++;
                                                        }
                                                    }
                                                    i29++;
                                                } else {
                                                    sb2.insert(i29, ' ');
                                                    if (selectionStart == i29 + 1 && (i25 = this.characterAction) != 2 && i25 != 3) {
                                                        selectionStart++;
                                                    }
                                                }
                                            }
                                        }
                                        hintEditText.setText(sb2);
                                        if (selectionStart >= 0) {
                                            hintEditText.setSelection(Math.min(selectionStart, hintEditText.length()));
                                        }
                                        hintEditText.onTextChange();
                                        PaymentFormActivity.this.ignoreOnPhoneChange = false;
                                    }

                                    @Override // android.text.TextWatcher
                                    public void beforeTextChanged(CharSequence charSequence, int i25, int i26, int i27) {
                                        int i28;
                                        if (i26 == 0 && i27 == 1) {
                                            this.characterAction = 1;
                                            return;
                                        }
                                        if (i26 != 1 || i27 != 0) {
                                            i28 = -1;
                                        } else if (charSequence.charAt(i25) == ' ' && i25 > 0) {
                                            this.characterAction = 3;
                                            this.actionPosition = i25 - 1;
                                            return;
                                        } else {
                                            i28 = 2;
                                        }
                                        this.characterAction = i28;
                                    }

                                    @Override // android.text.TextWatcher
                                    public void onTextChanged(CharSequence charSequence, int i25, int i26, int i27) {
                                    }
                                });
                            } else {
                                this.inputFields[i22].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                                this.inputFields[i22].setGravity(LocaleController.isRTL ? 5 : 3);
                                frameLayout.addView(this.inputFields[i22], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                            }
                            this.inputFields[i22].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda23
                                @Override // android.widget.TextView.OnEditorActionListener
                                public final boolean onEditorAction(TextView textView2, int i25, KeyEvent keyEvent) {
                                    boolean lambda$createView$2;
                                    lambda$createView$2 = PaymentFormActivity.this.lambda$createView$2(textView2, i25, keyEvent);
                                    return lambda$createView$2;
                                }
                            });
                            if (i22 != 9) {
                                TLRPC$TL_invoice tLRPC$TL_invoice2 = this.paymentForm.invoice;
                                if (tLRPC$TL_invoice2.email_to_provider || tLRPC$TL_invoice2.phone_to_provider) {
                                    TLRPC$User tLRPC$User2 = null;
                                    for (int i25 = 0; i25 < this.paymentForm.users.size(); i25++) {
                                        TLRPC$User tLRPC$User3 = (TLRPC$User) this.paymentForm.users.get(i25);
                                        tLRPC$User2 = tLRPC$User3.id == this.paymentForm.provider_id ? tLRPC$User3 : tLRPC$User2;
                                    }
                                    TLRPC$User tLRPC$User4 = tLRPC$User2;
                                    String formatName = tLRPC$User4 != null ? ContactsController.formatName(tLRPC$User4.first_name, tLRPC$User4.last_name) : "";
                                    this.bottomCell[1] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                                    this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                                    this.linearLayout2.addView(this.bottomCell[1], LayoutHelper.createLinear(-1, -2));
                                    TLRPC$TL_invoice tLRPC$TL_invoice3 = this.paymentForm.invoice;
                                    boolean z3 = tLRPC$TL_invoice3.email_to_provider;
                                    if (z3 && tLRPC$TL_invoice3.phone_to_provider) {
                                        textInfoPrivacyCell2 = this.bottomCell[1];
                                        formatString = LocaleController.formatString("PaymentPhoneEmailToProvider", R.string.PaymentPhoneEmailToProvider, formatName);
                                    } else {
                                        TextInfoPrivacyCell[] textInfoPrivacyCellArr = this.bottomCell;
                                        if (z3) {
                                            textInfoPrivacyCell2 = textInfoPrivacyCellArr[1];
                                            formatString = LocaleController.formatString("PaymentEmailToProvider", R.string.PaymentEmailToProvider, formatName);
                                        } else {
                                            textInfoPrivacyCell2 = textInfoPrivacyCellArr[1];
                                            formatString = LocaleController.formatString("PaymentPhoneToProvider", R.string.PaymentPhoneToProvider, formatName);
                                        }
                                    }
                                    textInfoPrivacyCell2.setText(formatString);
                                } else {
                                    this.sectionCell[1] = new ShadowSectionCell(context, this.resourcesProvider);
                                    this.linearLayout2.addView(this.sectionCell[1], LayoutHelper.createLinear(i18, i19));
                                }
                                TextCheckCell textCheckCell = new TextCheckCell(context, this.resourcesProvider);
                                this.checkCell1 = textCheckCell;
                                textCheckCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                                this.checkCell1.setTextAndCheck(LocaleController.getString(R.string.PaymentShippingSave), this.saveShippingInfo, false);
                                this.linearLayout2.addView(this.checkCell1, LayoutHelper.createLinear(-1, -2));
                                this.checkCell1.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda24
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view4) {
                                        PaymentFormActivity.this.lambda$createView$3(view4);
                                    }
                                });
                                this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                                this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                                this.bottomCell[0].setText(LocaleController.getString(R.string.PaymentShippingSaveInfo));
                                this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                            }
                            i22++;
                            i18 = -1;
                            i21 = 10;
                            i17 = 3;
                            i19 = -2;
                        }
                        linearLayout5.addView(headerCell2, LayoutHelper.createLinear(i18, i19));
                        if (i22 != 8) {
                        }
                        if (i22 != 9) {
                        }
                        this.inputFields[i22].setTag(Integer.valueOf(i22));
                        this.inputFields[i22].setTextSize(1, 16.0f);
                        this.inputFields[i22].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                        EditTextBoldCursor editTextBoldCursor82 = this.inputFields[i22];
                        int i242 = Theme.key_windowBackgroundWhiteBlackText;
                        editTextBoldCursor82.setTextColor(getThemedColor(i242));
                        this.inputFields[i22].setBackgroundDrawable(null);
                        this.inputFields[i22].setCursorColor(getThemedColor(i242));
                        this.inputFields[i22].setCursorSize(AndroidUtilities.dp(20.0f));
                        this.inputFields[i22].setCursorWidth(1.5f);
                        if (i22 == 4) {
                        }
                        if (i22 != 9) {
                        }
                        this.inputFields[i22].setInputType(i17);
                        this.inputFields[i22].setImeOptions(268435461);
                        switch (i22) {
                        }
                        EditTextBoldCursor editTextBoldCursor102 = this.inputFields[i22];
                        editTextBoldCursor102.setSelection(editTextBoldCursor102.length());
                        if (i22 != 8) {
                        }
                        this.inputFields[i22].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda23
                            @Override // android.widget.TextView.OnEditorActionListener
                            public final boolean onEditorAction(TextView textView2, int i252, KeyEvent keyEvent) {
                                boolean lambda$createView$2;
                                lambda$createView$2 = PaymentFormActivity.this.lambda$createView$2(textView2, i252, keyEvent);
                                return lambda$createView$2;
                            }
                        });
                        if (i22 != 9) {
                        }
                        i22++;
                        i18 = -1;
                        i21 = 10;
                        i17 = 3;
                        i19 = -2;
                    }
                    if (this.paymentForm.invoice.name_requested) {
                        i11 = 8;
                    } else {
                        i11 = 8;
                        ((ViewGroup) this.inputFields[6].getParent()).setVisibility(8);
                    }
                    if (!this.paymentForm.invoice.phone_requested) {
                        ((ViewGroup) this.inputFields[i11].getParent()).setVisibility(i11);
                    }
                    if (!this.paymentForm.invoice.email_requested) {
                        ((ViewGroup) this.inputFields[7].getParent()).setVisibility(i11);
                    }
                    TLRPC$TL_invoice tLRPC$TL_invoice4 = this.paymentForm.invoice;
                    if (tLRPC$TL_invoice4.phone_requested) {
                        editTextBoldCursor6 = this.inputFields[9];
                        i12 = 268435462;
                    } else {
                        i12 = 268435462;
                        editTextBoldCursor6 = tLRPC$TL_invoice4.email_requested ? this.inputFields[7] : tLRPC$TL_invoice4.name_requested ? this.inputFields[6] : this.inputFields[5];
                    }
                    editTextBoldCursor6.setImeOptions(i12);
                    View view4 = this.sectionCell[1];
                    if (view4 != null) {
                        TLRPC$TL_invoice tLRPC$TL_invoice5 = this.paymentForm.invoice;
                        if (!tLRPC$TL_invoice5.name_requested) {
                            if (!tLRPC$TL_invoice5.phone_requested) {
                            }
                        }
                        i13 = 0;
                    } else {
                        view4 = this.bottomCell[1];
                        if (view4 != null) {
                            TLRPC$TL_invoice tLRPC$TL_invoice6 = this.paymentForm.invoice;
                            if (!tLRPC$TL_invoice6.name_requested) {
                                if (!tLRPC$TL_invoice6.phone_requested) {
                                }
                            }
                            i13 = 0;
                        }
                        HeaderCell headerCell3 = this.headerCell[1];
                        TLRPC$TL_invoice tLRPC$TL_invoice7 = this.paymentForm.invoice;
                        headerCell3.setVisibility((!tLRPC$TL_invoice7.name_requested || tLRPC$TL_invoice7.phone_requested || tLRPC$TL_invoice7.email_requested) ? 0 : 8);
                        if (!this.paymentForm.invoice.shipping_address_requested) {
                            this.headerCell[0].setVisibility(8);
                            this.sectionCell[0].setVisibility(8);
                            ((ViewGroup) this.inputFields[0].getParent()).setVisibility(8);
                            ((ViewGroup) this.inputFields[1].getParent()).setVisibility(8);
                            ((ViewGroup) this.inputFields[2].getParent()).setVisibility(8);
                            ((ViewGroup) this.inputFields[3].getParent()).setVisibility(8);
                            ((ViewGroup) this.inputFields[4].getParent()).setVisibility(8);
                            ((ViewGroup) this.inputFields[5].getParent()).setVisibility(8);
                        }
                        tLRPC$TL_paymentRequestedInfo3 = this.paymentForm.saved_info;
                        if (tLRPC$TL_paymentRequestedInfo3 != null || TextUtils.isEmpty(tLRPC$TL_paymentRequestedInfo3.phone)) {
                            fillNumber(null);
                        } else {
                            fillNumber(this.paymentForm.saved_info.phone);
                        }
                        if (this.inputFields[8].length() == 0) {
                            TLRPC$PaymentForm tLRPC$PaymentForm3 = this.paymentForm;
                            if (tLRPC$PaymentForm3.invoice.phone_requested && ((tLRPC$TL_paymentRequestedInfo4 = tLRPC$PaymentForm3.saved_info) == null || TextUtils.isEmpty(tLRPC$TL_paymentRequestedInfo4.phone))) {
                                try {
                                    telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
                                } catch (Exception e2) {
                                    FileLog.e(e2);
                                }
                                if (telephonyManager != null) {
                                    str4 = telephonyManager.getSimCountryIso().toUpperCase();
                                    if (str4 != null && (str5 = (String) hashMap.get(str4)) != null && this.countriesArray.indexOf(str5) != -1) {
                                        this.inputFields[8].setText((CharSequence) this.countriesMap.get(str5));
                                    }
                                }
                                str4 = null;
                                if (str4 != null) {
                                    this.inputFields[8].setText((CharSequence) this.countriesMap.get(str5));
                                }
                            }
                        }
                    }
                    view4.setVisibility(i13);
                    HeaderCell headerCell32 = this.headerCell[1];
                    TLRPC$TL_invoice tLRPC$TL_invoice72 = this.paymentForm.invoice;
                    headerCell32.setVisibility((!tLRPC$TL_invoice72.name_requested || tLRPC$TL_invoice72.phone_requested || tLRPC$TL_invoice72.email_requested) ? 0 : 8);
                    if (!this.paymentForm.invoice.shipping_address_requested) {
                    }
                    tLRPC$TL_paymentRequestedInfo3 = this.paymentForm.saved_info;
                    if (tLRPC$TL_paymentRequestedInfo3 != null) {
                    }
                    fillNumber(null);
                    if (this.inputFields[8].length() == 0) {
                    }
                }
            }
        } else if (i20 == 2) {
            if (this.paymentForm.native_params != null) {
                try {
                    JSONObject jSONObject = new JSONObject(this.paymentForm.native_params.data);
                    String optString = jSONObject.optString("google_pay_public_key");
                    if (!TextUtils.isEmpty(optString)) {
                        this.googlePayPublicKey = optString;
                    }
                    this.googlePayCountryCode = jSONObject.optString("acquirer_bank_country");
                    this.googlePayParameters = jSONObject.optJSONObject("gpay_parameters");
                } catch (Exception e3) {
                    FileLog.e(e3);
                }
            }
            if (this.isWebView || this.paymentFormMethod != null) {
                if (this.googlePayPublicKey != null || this.googlePayParameters != null) {
                    initGooglePay(context);
                }
                createGooglePayButton(context);
                this.linearLayout2.addView(this.googlePayContainer, LayoutHelper.createLinear(-1, 50));
                this.webviewLoading = true;
                showEditDoneProgress(true, true);
                this.progressView.setVisibility(0);
                this.doneItem.setEnabled(false);
                this.doneItem.getContentView().setVisibility(4);
                AndroidUtilities.checkAndroidTheme(context, true);
                WebView webView = new WebView(context) { // from class: org.telegram.ui.PaymentFormActivity.5
                    @Override // android.webkit.WebView, android.widget.AbsoluteLayout, android.view.View
                    protected void onMeasure(int i26, int i27) {
                        super.onMeasure(i26, i27);
                    }

                    @Override // android.webkit.WebView, android.view.View
                    public boolean onTouchEvent(MotionEvent motionEvent) {
                        ((ViewGroup) PaymentFormActivity.this.fragmentView).requestDisallowInterceptTouchEvent(true);
                        return super.onTouchEvent(motionEvent);
                    }
                };
                this.webView = webView;
                webView.getSettings().setJavaScriptEnabled(true);
                this.webView.getSettings().setDomStorageEnabled(true);
                this.webView.getSettings().setSupportZoom(true);
                this.webView.getSettings().setBuiltInZoomControls(true);
                this.webView.getSettings().setDisplayZoomControls(false);
                this.webView.getSettings().setUseWideViewPort(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.webView.getSettings().setMixedContentMode(0);
                    CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
                }
                this.webView.addJavascriptInterface(new TelegramWebviewProxy(), "TelegramWebviewProxy");
                this.webView.setWebViewClient(new 6(context));
                this.linearLayout2.addView(this.webView, LayoutHelper.createFrame(-1, -2.0f));
                this.sectionCell[2] = new ShadowSectionCell(context, this.resourcesProvider);
                this.linearLayout2.addView(this.sectionCell[2], LayoutHelper.createLinear(-1, -2));
                TextCheckCell textCheckCell2 = new TextCheckCell(context, this.resourcesProvider);
                this.checkCell1 = textCheckCell2;
                textCheckCell2.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.checkCell1.setTextAndCheck(LocaleController.getString(R.string.PaymentCardSavePaymentInformation), this.saveCardInfo, false);
                this.linearLayout2.addView(this.checkCell1, LayoutHelper.createLinear(-1, -2));
                this.checkCell1.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda28
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view5) {
                        PaymentFormActivity.this.lambda$createView$4(view5);
                    }
                });
                this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                updateSavePaymentField();
                linearLayout = this.linearLayout2;
                view = this.bottomCell[0];
                linearLayout.addView(view, LayoutHelper.createLinear(-1, -2));
            } else {
                if (this.paymentForm.native_params != null) {
                    try {
                        JSONObject jSONObject2 = new JSONObject(this.paymentForm.native_params.data);
                        try {
                            this.need_card_country = jSONObject2.getBoolean("need_country");
                        } catch (Exception unused) {
                            this.need_card_country = false;
                        }
                        try {
                            this.need_card_postcode = jSONObject2.getBoolean("need_zip");
                        } catch (Exception unused2) {
                            this.need_card_postcode = false;
                        }
                        try {
                            this.need_card_name = jSONObject2.getBoolean("need_cardholder_name");
                        } catch (Exception unused3) {
                            this.need_card_name = false;
                        }
                        if (jSONObject2.has("public_token")) {
                            str8 = jSONObject2.getString("public_token");
                        } else {
                            try {
                                this.providerApiKey = jSONObject2.getString("publishable_key");
                            } catch (Exception unused4) {
                            }
                            this.initGooglePay = !jSONObject2.optBoolean("google_pay_hidden", false);
                        }
                        this.providerApiKey = str8;
                        this.initGooglePay = !jSONObject2.optBoolean("google_pay_hidden", false);
                    } catch (Exception e4) {
                        FileLog.e(e4);
                    }
                }
                if (this.initGooglePay && ((!TextUtils.isEmpty(this.providerApiKey) && "stripe".equals(this.paymentForm.native_provider)) || this.googlePayParameters != null)) {
                    initGooglePay(context);
                }
                this.inputFields = new EditTextBoldCursor[6];
                int i26 = 0;
                for (int i27 = 6; i26 < i27; i27 = 6) {
                    if (i26 == 0) {
                        this.headerCell[0] = new HeaderCell(context, this.resourcesProvider);
                        this.headerCell[0].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        this.headerCell[0].setText(LocaleController.getString(R.string.PaymentCardTitle));
                        linearLayout3 = this.linearLayout2;
                        headerCell = this.headerCell[0];
                    } else {
                        if (i26 == 4) {
                            this.headerCell[1] = new HeaderCell(context, this.resourcesProvider);
                            this.headerCell[1].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            this.headerCell[1].setText(LocaleController.getString(R.string.PaymentBillingAddress));
                            linearLayout3 = this.linearLayout2;
                            headerCell = this.headerCell[1];
                        }
                        z = i26 == 3 && i26 != 5 && (i26 != 4 || this.need_card_postcode);
                        FrameLayout frameLayout3 = new FrameLayout(context);
                        frameLayout3.setClipChildren(false);
                        int i28 = Theme.key_windowBackgroundWhite;
                        frameLayout3.setBackgroundColor(getThemedColor(i28));
                        this.linearLayout2.addView(frameLayout3, LayoutHelper.createLinear(-1, 50));
                        this.inputFields[i26] = new EditTextBoldCursor(context);
                        this.inputFields[i26].setTag(Integer.valueOf(i26));
                        this.inputFields[i26].setTextSize(1, 16.0f);
                        this.inputFields[i26].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                        EditTextBoldCursor editTextBoldCursor11 = this.inputFields[i26];
                        int i29 = Theme.key_windowBackgroundWhiteBlackText;
                        editTextBoldCursor11.setTextColor(getThemedColor(i29));
                        this.inputFields[i26].setBackgroundDrawable(null);
                        this.inputFields[i26].setCursorColor(getThemedColor(i29));
                        this.inputFields[i26].setCursorSize(AndroidUtilities.dp(20.0f));
                        this.inputFields[i26].setCursorWidth(1.5f);
                        if (i26 != 3) {
                            this.inputFields[i26].setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                            this.inputFields[i26].setInputType(NotificationCenter.walletSyncProgressChanged);
                            this.inputFields[i26].setTypeface(Typeface.DEFAULT);
                            this.inputFields[i26].setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
                            if (i26 == 0) {
                                editTextBoldCursor3 = this.inputFields[i26];
                                i9 = 3;
                            } else if (i26 == 4) {
                                this.inputFields[i26].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda25
                                    @Override // android.view.View.OnTouchListener
                                    public final boolean onTouch(View view5, MotionEvent motionEvent) {
                                        boolean lambda$createView$6;
                                        lambda$createView$6 = PaymentFormActivity.this.lambda$createView$6(view5, motionEvent);
                                        return lambda$createView$6;
                                    }
                                });
                                this.inputFields[i26].setInputType(0);
                            } else if (i26 == 1) {
                                editTextBoldCursor3 = this.inputFields[i26];
                                i9 = 16386;
                            } else if (i26 == 2) {
                                editTextBoldCursor3 = this.inputFields[i26];
                                i9 = 4097;
                            } else {
                                this.inputFields[i26].setInputType(16385);
                                this.inputFields[i26].setImeOptions(268435461);
                                if (i26 == 0) {
                                    editTextBoldCursor4 = this.inputFields[i26];
                                    i10 = R.string.PaymentCardNumber;
                                } else if (i26 == 1) {
                                    editTextBoldCursor4 = this.inputFields[i26];
                                    i10 = R.string.PaymentCardExpireDate;
                                } else if (i26 == 2) {
                                    editTextBoldCursor4 = this.inputFields[i26];
                                    i10 = R.string.PaymentCardName;
                                } else if (i26 == 3) {
                                    editTextBoldCursor4 = this.inputFields[i26];
                                    i10 = R.string.PaymentCardCvv;
                                } else if (i26 != 4) {
                                    if (i26 == 5) {
                                        editTextBoldCursor4 = this.inputFields[i26];
                                        i10 = R.string.PaymentShippingZipPlaceholder;
                                    }
                                    if (i26 != 0) {
                                        editTextBoldCursor5 = this.inputFields[i26];
                                        textWatcher = new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.7
                                            private int actionPosition;
                                            public final String[] PREFIXES_15 = {"34", "37"};
                                            public final String[] PREFIXES_14 = {"300", "301", "302", "303", "304", "305", "309", "36", "38", "39"};
                                            public final String[] PREFIXES_16 = {"2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229", "2200", "2201", "2202", "2203", "2204", "8600", "9860", "223", "224", "225", "226", "227", "228", "229", "23", "24", "25", "26", "270", "271", "2720", "50", "51", "52", "53", "54", "55", "4", "60", "62", "64", "65", "35"};
                                            private int characterAction = -1;

                                            @Override // android.text.TextWatcher
                                            public void afterTextChanged(Editable editable) {
                                                boolean z4;
                                                int i30;
                                                int i31;
                                                String[] strArr;
                                                int i32;
                                                String str11;
                                                if (PaymentFormActivity.this.ignoreOnCardChange) {
                                                    return;
                                                }
                                                EditTextBoldCursor editTextBoldCursor12 = PaymentFormActivity.this.inputFields[0];
                                                int selectionStart = editTextBoldCursor12.getSelectionStart();
                                                String obj = editTextBoldCursor12.getText().toString();
                                                if (this.characterAction == 3) {
                                                    obj = obj.substring(0, this.actionPosition) + obj.substring(this.actionPosition + 1);
                                                    selectionStart--;
                                                }
                                                StringBuilder sb2 = new StringBuilder(obj.length());
                                                int i33 = 0;
                                                while (i33 < obj.length()) {
                                                    int i34 = i33 + 1;
                                                    String substring = obj.substring(i33, i34);
                                                    if ("0123456789".contains(substring)) {
                                                        sb2.append(substring);
                                                    }
                                                    i33 = i34;
                                                }
                                                PaymentFormActivity.this.ignoreOnCardChange = true;
                                                String str12 = null;
                                                int i35 = 100;
                                                if (sb2.length() > 0) {
                                                    String sb3 = sb2.toString();
                                                    int i36 = 0;
                                                    for (int i37 = 3; i36 < i37; i37 = 3) {
                                                        if (i36 == 0) {
                                                            strArr = this.PREFIXES_16;
                                                            i32 = 16;
                                                            str11 = "xxxx xxxx xxxx xxxx";
                                                        } else if (i36 != 1) {
                                                            strArr = this.PREFIXES_14;
                                                            i32 = 14;
                                                            str11 = "xxxx xxxx xxxx xx";
                                                        } else {
                                                            strArr = this.PREFIXES_15;
                                                            i32 = 15;
                                                            str11 = "xxxx xxxx xxxx xxx";
                                                        }
                                                        for (String str13 : strArr) {
                                                            if (sb3.length() <= str13.length()) {
                                                                if (str13.startsWith(sb3)) {
                                                                    i35 = i32;
                                                                    str12 = str11;
                                                                    break;
                                                                }
                                                            } else if (sb3.startsWith(str13)) {
                                                                i35 = i32;
                                                                str12 = str11;
                                                                break;
                                                            }
                                                        }
                                                        if (str12 != null) {
                                                            break;
                                                        }
                                                        i36++;
                                                    }
                                                    if (sb2.length() > i35) {
                                                        sb2.setLength(i35);
                                                    }
                                                }
                                                if (str12 != null) {
                                                    if (sb2.length() == i35) {
                                                        PaymentFormActivity.this.inputFields[1].requestFocus();
                                                    }
                                                    editTextBoldCursor12.setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                                                    int i38 = 0;
                                                    while (true) {
                                                        if (i38 >= sb2.length()) {
                                                            break;
                                                        } else if (i38 < str12.length()) {
                                                            if (str12.charAt(i38) == ' ') {
                                                                sb2.insert(i38, ' ');
                                                                i38++;
                                                                if (selectionStart == i38 && (i31 = this.characterAction) != 2 && i31 != 3) {
                                                                    selectionStart++;
                                                                }
                                                            }
                                                            i38++;
                                                        } else {
                                                            sb2.insert(i38, ' ');
                                                            if (selectionStart == i38 + 1 && (i30 = this.characterAction) != 2 && i30 != 3) {
                                                                selectionStart++;
                                                            }
                                                        }
                                                    }
                                                }
                                                if (sb2.toString().equals(editable.toString())) {
                                                    z4 = false;
                                                } else {
                                                    z4 = false;
                                                    editable.replace(0, editable.length(), sb2);
                                                }
                                                if (selectionStart >= 0) {
                                                    editTextBoldCursor12.setSelection(Math.min(selectionStart, editTextBoldCursor12.length()));
                                                }
                                                PaymentFormActivity.this.ignoreOnCardChange = z4;
                                            }

                                            @Override // android.text.TextWatcher
                                            public void beforeTextChanged(CharSequence charSequence, int i30, int i31, int i32) {
                                                int i33;
                                                if (i31 == 0 && i32 == 1) {
                                                    this.characterAction = 1;
                                                    return;
                                                }
                                                if (i31 != 1 || i32 != 0) {
                                                    i33 = -1;
                                                } else if (charSequence.charAt(i30) == ' ' && i30 > 0) {
                                                    this.characterAction = 3;
                                                    this.actionPosition = i30 - 1;
                                                    return;
                                                } else {
                                                    i33 = 2;
                                                }
                                                this.characterAction = i33;
                                            }

                                            @Override // android.text.TextWatcher
                                            public void onTextChanged(CharSequence charSequence, int i30, int i31, int i32) {
                                            }
                                        };
                                    } else {
                                        if (i26 == 1) {
                                            editTextBoldCursor5 = this.inputFields[i26];
                                            textWatcher = new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.8
                                                private int actionPosition;
                                                private int characterAction = -1;
                                                private boolean isYear;

                                                /* JADX WARN: Removed duplicated region for block: B:74:0x0193  */
                                                /* JADX WARN: Removed duplicated region for block: B:78:0x01a7  */
                                                /* JADX WARN: Removed duplicated region for block: B:82:0x01b5  */
                                                /* JADX WARN: Removed duplicated region for block: B:84:0x01bb  */
                                                /* JADX WARN: Removed duplicated region for block: B:91:0x01d0  */
                                                @Override // android.text.TextWatcher
                                                /*
                                                    Code decompiled incorrectly, please refer to instructions dump.
                                                */
                                                public void afterTextChanged(Editable editable) {
                                                    EditTextBoldCursor editTextBoldCursor12;
                                                    if (PaymentFormActivity.this.ignoreOnCardChange) {
                                                        return;
                                                    }
                                                    boolean z4 = true;
                                                    EditTextBoldCursor editTextBoldCursor13 = PaymentFormActivity.this.inputFields[1];
                                                    int selectionStart = editTextBoldCursor13.getSelectionStart();
                                                    String obj = editTextBoldCursor13.getText().toString();
                                                    if (this.characterAction == 3) {
                                                        obj = obj.substring(0, this.actionPosition) + obj.substring(this.actionPosition + 1);
                                                        selectionStart--;
                                                    }
                                                    StringBuilder sb2 = new StringBuilder(obj.length());
                                                    int i30 = 0;
                                                    while (i30 < obj.length()) {
                                                        int i31 = i30 + 1;
                                                        String substring = obj.substring(i30, i31);
                                                        if ("0123456789".contains(substring)) {
                                                            sb2.append(substring);
                                                        }
                                                        i30 = i31;
                                                    }
                                                    PaymentFormActivity.this.ignoreOnCardChange = true;
                                                    PaymentFormActivity.this.inputFields[1].setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                                                    if (sb2.length() > 4) {
                                                        sb2.setLength(4);
                                                    }
                                                    if (sb2.length() < 2) {
                                                        this.isYear = false;
                                                    }
                                                    if (!this.isYear) {
                                                        if (sb2.length() == 1) {
                                                            int intValue = Utilities.parseInt((CharSequence) sb2.toString()).intValue();
                                                            if (intValue != 1 && intValue != 0) {
                                                                sb2.insert(0, "0");
                                                                selectionStart++;
                                                            }
                                                        } else if (sb2.length() == 2) {
                                                            int intValue2 = Utilities.parseInt((CharSequence) sb2.toString()).intValue();
                                                            if (intValue2 > 12 || intValue2 == 0) {
                                                                PaymentFormActivity.this.inputFields[1].setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_text_RedRegular));
                                                            } else {
                                                                z4 = false;
                                                            }
                                                            selectionStart++;
                                                            if (!z4) {
                                                                PaymentFormActivity.this.inputFields[PaymentFormActivity.this.need_card_name ? (char) 2 : (char) 3].requestFocus();
                                                            }
                                                            if (sb2.length() != 2) {
                                                            }
                                                            selectionStart++;
                                                            editTextBoldCursor13.setText(sb2);
                                                            if (selectionStart >= 0) {
                                                            }
                                                            PaymentFormActivity.this.ignoreOnCardChange = false;
                                                        }
                                                        z4 = false;
                                                        if (!z4) {
                                                        }
                                                        if (sb2.length() != 2) {
                                                        }
                                                        selectionStart++;
                                                        editTextBoldCursor13.setText(sb2);
                                                        if (selectionStart >= 0) {
                                                        }
                                                        PaymentFormActivity.this.ignoreOnCardChange = false;
                                                    }
                                                    int i32 = sb2.length() > 2 ? 2 : 1;
                                                    String[] strArr = new String[i32];
                                                    strArr[0] = sb2.substring(0, 2);
                                                    if (i32 == 2) {
                                                        strArr[1] = sb2.substring(2);
                                                    }
                                                    if (sb2.length() == 4 && i32 == 2) {
                                                        int intValue3 = Utilities.parseInt((CharSequence) strArr[0]).intValue();
                                                        int intValue4 = Utilities.parseInt((CharSequence) strArr[1]).intValue() + 2000;
                                                        Calendar calendar = Calendar.getInstance();
                                                        boolean z5 = UserConfig.getInstance(((BaseFragment) PaymentFormActivity.this).currentAccount).getClientPhone().startsWith("7") || (PaymentFormActivity.this.country != null && PaymentFormActivity.this.country.code.equals("7"));
                                                        int i33 = z5 ? 2022 : calendar.get(1);
                                                        int i34 = z5 ? 1 : calendar.get(2) + 1;
                                                        if (intValue4 < i33 || (intValue4 == i33 && intValue3 < i34)) {
                                                            editTextBoldCursor12 = PaymentFormActivity.this.inputFields[1];
                                                            editTextBoldCursor12.setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_text_RedRegular));
                                                        }
                                                        z4 = false;
                                                    } else {
                                                        int intValue5 = Utilities.parseInt((CharSequence) strArr[0]).intValue();
                                                        if (intValue5 > 12 || intValue5 == 0) {
                                                            editTextBoldCursor12 = PaymentFormActivity.this.inputFields[1];
                                                            editTextBoldCursor12.setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_text_RedRegular));
                                                        }
                                                        z4 = false;
                                                    }
                                                    if (!z4 && sb2.length() == 4) {
                                                        PaymentFormActivity.this.inputFields[PaymentFormActivity.this.need_card_name ? (char) 2 : (char) 3].requestFocus();
                                                    }
                                                    if (sb2.length() != 2) {
                                                        if (sb2.length() > 2 && sb2.charAt(2) != '/') {
                                                            sb2.insert(2, '/');
                                                        }
                                                        editTextBoldCursor13.setText(sb2);
                                                        if (selectionStart >= 0) {
                                                            editTextBoldCursor13.setSelection(Math.min(selectionStart, editTextBoldCursor13.length()));
                                                        }
                                                        PaymentFormActivity.this.ignoreOnCardChange = false;
                                                    }
                                                    sb2.append('/');
                                                    selectionStart++;
                                                    editTextBoldCursor13.setText(sb2);
                                                    if (selectionStart >= 0) {
                                                    }
                                                    PaymentFormActivity.this.ignoreOnCardChange = false;
                                                }

                                                @Override // android.text.TextWatcher
                                                public void beforeTextChanged(CharSequence charSequence, int i30, int i31, int i32) {
                                                    if (i31 == 0 && i32 == 1) {
                                                        this.isYear = TextUtils.indexOf((CharSequence) PaymentFormActivity.this.inputFields[1].getText(), '/') != -1;
                                                        this.characterAction = 1;
                                                    } else if (i31 != 1 || i32 != 0) {
                                                        this.characterAction = -1;
                                                    } else if (charSequence.charAt(i30) != '/' || i30 <= 0) {
                                                        this.characterAction = 2;
                                                    } else {
                                                        this.isYear = false;
                                                        this.characterAction = 3;
                                                        this.actionPosition = i30 - 1;
                                                    }
                                                }

                                                @Override // android.text.TextWatcher
                                                public void onTextChanged(CharSequence charSequence, int i30, int i31, int i32) {
                                                }
                                            };
                                        }
                                        this.inputFields[i26].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                                        this.inputFields[i26].setGravity(LocaleController.isRTL ? 5 : 3);
                                        frameLayout3.addView(this.inputFields[i26], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                                        this.inputFields[i26].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda26
                                            @Override // android.widget.TextView.OnEditorActionListener
                                            public final boolean onEditorAction(TextView textView2, int i30, KeyEvent keyEvent) {
                                                boolean lambda$createView$7;
                                                lambda$createView$7 = PaymentFormActivity.this.lambda$createView$7(textView2, i30, keyEvent);
                                                return lambda$createView$7;
                                            }
                                        });
                                        if (i26 == 3) {
                                            this.sectionCell[0] = new ShadowSectionCell(context, this.resourcesProvider);
                                            linearLayout4 = this.linearLayout2;
                                            view2 = this.sectionCell[0];
                                        } else if (i26 == 5) {
                                            this.sectionCell[2] = new ShadowSectionCell(context, this.resourcesProvider);
                                            this.linearLayout2.addView(this.sectionCell[2], LayoutHelper.createLinear(-1, -2));
                                            TextCheckCell textCheckCell3 = new TextCheckCell(context, this.resourcesProvider);
                                            this.checkCell1 = textCheckCell3;
                                            textCheckCell3.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                                            this.checkCell1.setTextAndCheck(LocaleController.getString(R.string.PaymentCardSavePaymentInformation), this.saveCardInfo, false);
                                            this.linearLayout2.addView(this.checkCell1, LayoutHelper.createLinear(-1, -2));
                                            this.checkCell1.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda27
                                                @Override // android.view.View.OnClickListener
                                                public final void onClick(View view5) {
                                                    PaymentFormActivity.this.lambda$createView$8(view5);
                                                }
                                            });
                                            this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                                            this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                                            updateSavePaymentField();
                                            linearLayout4 = this.linearLayout2;
                                            view2 = this.bottomCell[0];
                                        } else {
                                            if (i26 == 0) {
                                                createGooglePayButton(context);
                                                frameLayout3.addView(this.googlePayContainer, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 3 : 5) | 16, 0.0f, 0.0f, 4.0f, 0.0f));
                                            }
                                            if (z) {
                                                View view5 = new View(context) { // from class: org.telegram.ui.PaymentFormActivity.9
                                                    @Override // android.view.View
                                                    protected void onDraw(Canvas canvas) {
                                                        canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                                                    }
                                                };
                                                view5.setBackgroundColor(getThemedColor(i28));
                                                this.dividers.add(view5);
                                                frameLayout3.addView(view5, new FrameLayout.LayoutParams(-1, 1, 83));
                                            }
                                            if ((i26 == 4 && !this.need_card_country) || ((i26 == 5 && !this.need_card_postcode) || (i26 == 2 && !this.need_card_name))) {
                                                frameLayout3.setVisibility(8);
                                            }
                                            i26++;
                                        }
                                        linearLayout4.addView(view2, LayoutHelper.createLinear(-1, -2));
                                        if (z) {
                                        }
                                        if (i26 == 4) {
                                            frameLayout3.setVisibility(8);
                                            i26++;
                                        }
                                        frameLayout3.setVisibility(8);
                                        i26++;
                                    }
                                    editTextBoldCursor5.addTextChangedListener(textWatcher);
                                    this.inputFields[i26].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                                    this.inputFields[i26].setGravity(LocaleController.isRTL ? 5 : 3);
                                    frameLayout3.addView(this.inputFields[i26], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                                    this.inputFields[i26].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda26
                                        @Override // android.widget.TextView.OnEditorActionListener
                                        public final boolean onEditorAction(TextView textView2, int i30, KeyEvent keyEvent) {
                                            boolean lambda$createView$7;
                                            lambda$createView$7 = PaymentFormActivity.this.lambda$createView$7(textView2, i30, keyEvent);
                                            return lambda$createView$7;
                                        }
                                    });
                                    if (i26 == 3) {
                                    }
                                    linearLayout4.addView(view2, LayoutHelper.createLinear(-1, -2));
                                    if (z) {
                                    }
                                    if (i26 == 4) {
                                    }
                                    frameLayout3.setVisibility(8);
                                    i26++;
                                } else {
                                    editTextBoldCursor4 = this.inputFields[i26];
                                    i10 = R.string.PaymentShippingCountry;
                                }
                                editTextBoldCursor4.setHint(LocaleController.getString(i10));
                                if (i26 != 0) {
                                }
                                editTextBoldCursor5.addTextChangedListener(textWatcher);
                                this.inputFields[i26].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                                this.inputFields[i26].setGravity(LocaleController.isRTL ? 5 : 3);
                                frameLayout3.addView(this.inputFields[i26], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                                this.inputFields[i26].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda26
                                    @Override // android.widget.TextView.OnEditorActionListener
                                    public final boolean onEditorAction(TextView textView2, int i30, KeyEvent keyEvent) {
                                        boolean lambda$createView$7;
                                        lambda$createView$7 = PaymentFormActivity.this.lambda$createView$7(textView2, i30, keyEvent);
                                        return lambda$createView$7;
                                    }
                                });
                                if (i26 == 3) {
                                }
                                linearLayout4.addView(view2, LayoutHelper.createLinear(-1, -2));
                                if (z) {
                                }
                                if (i26 == 4) {
                                }
                                frameLayout3.setVisibility(8);
                                i26++;
                            }
                            editTextBoldCursor3.setInputType(i9);
                        }
                        this.inputFields[i26].setImeOptions(268435461);
                        if (i26 == 0) {
                        }
                        editTextBoldCursor4.setHint(LocaleController.getString(i10));
                        if (i26 != 0) {
                        }
                        editTextBoldCursor5.addTextChangedListener(textWatcher);
                        this.inputFields[i26].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                        this.inputFields[i26].setGravity(LocaleController.isRTL ? 5 : 3);
                        frameLayout3.addView(this.inputFields[i26], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                        this.inputFields[i26].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda26
                            @Override // android.widget.TextView.OnEditorActionListener
                            public final boolean onEditorAction(TextView textView2, int i30, KeyEvent keyEvent) {
                                boolean lambda$createView$7;
                                lambda$createView$7 = PaymentFormActivity.this.lambda$createView$7(textView2, i30, keyEvent);
                                return lambda$createView$7;
                            }
                        });
                        if (i26 == 3) {
                        }
                        linearLayout4.addView(view2, LayoutHelper.createLinear(-1, -2));
                        if (z) {
                        }
                        if (i26 == 4) {
                        }
                        frameLayout3.setVisibility(8);
                        i26++;
                    }
                    linearLayout3.addView(headerCell, LayoutHelper.createLinear(-1, -2));
                    if (i26 == 3) {
                    }
                    FrameLayout frameLayout32 = new FrameLayout(context);
                    frameLayout32.setClipChildren(false);
                    int i282 = Theme.key_windowBackgroundWhite;
                    frameLayout32.setBackgroundColor(getThemedColor(i282));
                    this.linearLayout2.addView(frameLayout32, LayoutHelper.createLinear(-1, 50));
                    this.inputFields[i26] = new EditTextBoldCursor(context);
                    this.inputFields[i26].setTag(Integer.valueOf(i26));
                    this.inputFields[i26].setTextSize(1, 16.0f);
                    this.inputFields[i26].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                    EditTextBoldCursor editTextBoldCursor112 = this.inputFields[i26];
                    int i292 = Theme.key_windowBackgroundWhiteBlackText;
                    editTextBoldCursor112.setTextColor(getThemedColor(i292));
                    this.inputFields[i26].setBackgroundDrawable(null);
                    this.inputFields[i26].setCursorColor(getThemedColor(i292));
                    this.inputFields[i26].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.inputFields[i26].setCursorWidth(1.5f);
                    if (i26 != 3) {
                    }
                    this.inputFields[i26].setImeOptions(268435461);
                    if (i26 == 0) {
                    }
                    editTextBoldCursor4.setHint(LocaleController.getString(i10));
                    if (i26 != 0) {
                    }
                    editTextBoldCursor5.addTextChangedListener(textWatcher);
                    this.inputFields[i26].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                    this.inputFields[i26].setGravity(LocaleController.isRTL ? 5 : 3);
                    frameLayout32.addView(this.inputFields[i26], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                    this.inputFields[i26].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda26
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView2, int i30, KeyEvent keyEvent) {
                            boolean lambda$createView$7;
                            lambda$createView$7 = PaymentFormActivity.this.lambda$createView$7(textView2, i30, keyEvent);
                            return lambda$createView$7;
                        }
                    });
                    if (i26 == 3) {
                    }
                    linearLayout4.addView(view2, LayoutHelper.createLinear(-1, -2));
                    if (z) {
                    }
                    if (i26 == 4) {
                    }
                    frameLayout32.setVisibility(8);
                    i26++;
                }
                if (!this.need_card_country && !this.need_card_postcode) {
                    this.headerCell[1].setVisibility(8);
                    this.sectionCell[0].setVisibility(8);
                }
                if (this.need_card_postcode) {
                    editTextBoldCursor2 = this.inputFields[5];
                    i8 = 268435462;
                } else {
                    i8 = 268435462;
                    editTextBoldCursor2 = this.inputFields[3];
                }
                editTextBoldCursor2.setImeOptions(i8);
            }
        } else {
            if (i20 == 1) {
                ArrayList arrayList = this.requestedInfo.shipping_options;
                int size = arrayList == null ? 0 : arrayList.size();
                this.radioCells = new RadioCell[size];
                int i30 = 0;
                while (i30 < size) {
                    TLRPC$TL_shippingOption tLRPC$TL_shippingOption = (TLRPC$TL_shippingOption) this.requestedInfo.shipping_options.get(i30);
                    this.radioCells[i30] = new RadioCell(context);
                    this.radioCells[i30].setTag(Integer.valueOf(i30));
                    this.radioCells[i30].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                    this.radioCells[i30].setText(String.format("%s - %s", getTotalPriceString(tLRPC$TL_shippingOption.prices), tLRPC$TL_shippingOption.title), i30 == 0, i30 != size + (-1));
                    this.radioCells[i30].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view6) {
                            PaymentFormActivity.this.lambda$createView$9(view6);
                        }
                    });
                    this.linearLayout2.addView(this.radioCells[i30]);
                    i30++;
                }
                this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                linearLayout = this.linearLayout2;
                view = this.bottomCell[0];
            } else if (i20 == 3) {
                this.inputFields = new EditTextBoldCursor[2];
                int i31 = 0;
                for (int i32 = 2; i31 < i32; i32 = 2) {
                    if (i31 == 0) {
                        this.headerCell[0] = new HeaderCell(context, this.resourcesProvider);
                        this.headerCell[0].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        this.headerCell[0].setText(LocaleController.getString(R.string.PaymentCardTitle));
                        i7 = -1;
                        this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                    } else {
                        i7 = -1;
                    }
                    FrameLayout frameLayout4 = new FrameLayout(context);
                    frameLayout4.setClipChildren(false);
                    this.linearLayout2.addView(frameLayout4, LayoutHelper.createLinear(i7, 50));
                    int i33 = Theme.key_windowBackgroundWhite;
                    frameLayout4.setBackgroundColor(getThemedColor(i33));
                    boolean z4 = i31 != 1;
                    if (z4) {
                        if (i31 != 7 || this.paymentForm.invoice.phone_requested) {
                            if (i31 == 6) {
                                TLRPC$TL_invoice tLRPC$TL_invoice8 = this.paymentForm.invoice;
                                if (!tLRPC$TL_invoice8.phone_requested) {
                                }
                            }
                        }
                        z4 = false;
                    }
                    if (z4) {
                        View view6 = new View(context) { // from class: org.telegram.ui.PaymentFormActivity.10
                            @Override // android.view.View
                            protected void onDraw(Canvas canvas) {
                                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                            }
                        };
                        view6.setBackgroundColor(getThemedColor(i33));
                        this.dividers.add(view6);
                        frameLayout4.addView(view6, new FrameLayout.LayoutParams(-1, 1, 83));
                    }
                    this.inputFields[i31] = new EditTextBoldCursor(context);
                    this.inputFields[i31].setTag(Integer.valueOf(i31));
                    this.inputFields[i31].setTextSize(1, 16.0f);
                    this.inputFields[i31].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                    EditTextBoldCursor editTextBoldCursor12 = this.inputFields[i31];
                    int i34 = Theme.key_windowBackgroundWhiteBlackText;
                    editTextBoldCursor12.setTextColor(getThemedColor(i34));
                    this.inputFields[i31].setBackgroundDrawable(null);
                    this.inputFields[i31].setCursorColor(getThemedColor(i34));
                    this.inputFields[i31].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.inputFields[i31].setCursorWidth(1.5f);
                    EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                    if (i31 == 0) {
                        editTextBoldCursorArr[i31].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda5
                            @Override // android.view.View.OnTouchListener
                            public final boolean onTouch(View view7, MotionEvent motionEvent) {
                                boolean lambda$createView$10;
                                lambda$createView$10 = PaymentFormActivity.lambda$createView$10(view7, motionEvent);
                                return lambda$createView$10;
                            }
                        });
                        this.inputFields[i31].setInputType(0);
                    } else {
                        editTextBoldCursorArr[i31].setInputType(NotificationCenter.walletPendingTransactionsChanged);
                        this.inputFields[i31].setTypeface(Typeface.DEFAULT);
                    }
                    this.inputFields[i31].setImeOptions(268435462);
                    if (i31 == 0) {
                        EditTextBoldCursor editTextBoldCursor13 = this.inputFields[i31];
                        TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard = this.savedCredentialsCard;
                        editTextBoldCursor13.setText(tLRPC$TL_paymentSavedCredentialsCard == null ? "" : tLRPC$TL_paymentSavedCredentialsCard.title);
                    } else if (i31 == 1) {
                        this.inputFields[i31].setHint(LocaleController.getString(R.string.LoginPassword));
                        this.inputFields[i31].requestFocus();
                    }
                    this.inputFields[i31].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                    this.inputFields[i31].setGravity(LocaleController.isRTL ? 5 : 3);
                    frameLayout4.addView(this.inputFields[i31], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                    this.inputFields[i31].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda6
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView2, int i35, KeyEvent keyEvent) {
                            boolean lambda$createView$11;
                            lambda$createView$11 = PaymentFormActivity.this.lambda$createView$11(textView2, i35, keyEvent);
                            return lambda$createView$11;
                        }
                    });
                    if (i31 == 1) {
                        this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                        TextInfoPrivacyCell textInfoPrivacyCell3 = this.bottomCell[0];
                        int i35 = R.string.PaymentConfirmationMessage;
                        TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard2 = this.savedCredentialsCard;
                        textInfoPrivacyCell3.setText(LocaleController.formatString("PaymentConfirmationMessage", i35, tLRPC$TL_paymentSavedCredentialsCard2 == null ? "" : tLRPC$TL_paymentSavedCredentialsCard2.title));
                        TextInfoPrivacyCell textInfoPrivacyCell4 = this.bottomCell[0];
                        int i36 = R.drawable.greydivider;
                        int i37 = Theme.key_windowBackgroundGrayShadow;
                        textInfoPrivacyCell4.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i36, i37));
                        this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                        this.settingsCell[0] = new TextSettingsCell(context, this.resourcesProvider);
                        this.settingsCell[0].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        this.settingsCell[0].setText(LocaleController.getString(R.string.PaymentConfirmationNewCard), false);
                        this.linearLayout2.addView(this.settingsCell[0], LayoutHelper.createLinear(-1, -2));
                        this.settingsCell[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda7
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view7) {
                                PaymentFormActivity.this.lambda$createView$12(view7);
                            }
                        });
                        this.bottomCell[1] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                        this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, i37));
                        this.linearLayout2.addView(this.bottomCell[1], LayoutHelper.createLinear(-1, -2));
                    }
                    i31++;
                }
            } else if (i20 == 4 || i20 == 5) {
                PaymentInfoCell paymentInfoCell = new PaymentInfoCell(context);
                this.paymentInfoCell = paymentInfoCell;
                paymentInfoCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                MessageObject messageObject = this.messageObject;
                if (messageObject != null) {
                    this.paymentInfoCell.setInvoice((TLRPC$TL_messageMediaInvoice) messageObject.messageOwner.media, this.currentBotName);
                } else {
                    TLRPC$PaymentReceipt tLRPC$PaymentReceipt = this.paymentReceipt;
                    if (tLRPC$PaymentReceipt != null) {
                        this.paymentInfoCell.setReceipt(tLRPC$PaymentReceipt, this.currentBotName);
                    } else if (this.invoiceSlug != null || this.invoiceInput != null) {
                        PaymentInfoCell paymentInfoCell2 = this.paymentInfoCell;
                        TLRPC$PaymentForm tLRPC$PaymentForm4 = this.paymentForm;
                        paymentInfoCell2.setInfo(tLRPC$PaymentForm4.title, tLRPC$PaymentForm4.description, tLRPC$PaymentForm4.photo, this.currentBotName, tLRPC$PaymentForm4);
                    }
                }
                this.linearLayout2.addView(this.paymentInfoCell, LayoutHelper.createLinear(-1, -2));
                this.sectionCell[0] = new ShadowSectionCell(context, this.resourcesProvider);
                this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(-1, -2));
                ArrayList arrayList2 = new ArrayList(this.paymentForm.invoice.prices);
                this.prices = arrayList2;
                TLRPC$TL_shippingOption tLRPC$TL_shippingOption2 = this.shippingOption;
                if (tLRPC$TL_shippingOption2 != null) {
                    arrayList2.addAll(tLRPC$TL_shippingOption2.prices);
                }
                this.totalPrice = new String[1];
                for (int i38 = 0; i38 < this.prices.size(); i38++) {
                    TLRPC$TL_labeledPrice tLRPC$TL_labeledPrice = (TLRPC$TL_labeledPrice) this.prices.get(i38);
                    TextPriceCell textPriceCell = new TextPriceCell(context);
                    textPriceCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    textPriceCell.setTextAndValue(tLRPC$TL_labeledPrice.label, LocaleController.getInstance().formatCurrencyString(tLRPC$TL_labeledPrice.amount, this.paymentForm.invoice.currency), false);
                    this.linearLayout2.addView(textPriceCell);
                }
                if (this.currentStep == 5 && this.tipAmount != null) {
                    TextPriceCell textPriceCell2 = new TextPriceCell(context);
                    textPriceCell2.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    textPriceCell2.setTextAndValue(LocaleController.getString(R.string.PaymentTip), LocaleController.getInstance().formatCurrencyString(this.tipAmount.longValue(), this.paymentForm.invoice.currency), false);
                    this.linearLayout2.addView(textPriceCell2);
                }
                TextPriceCell textPriceCell3 = new TextPriceCell(context);
                this.totalCell = textPriceCell3;
                int i39 = Theme.key_windowBackgroundWhite;
                textPriceCell3.setBackgroundColor(getThemedColor(i39));
                this.totalPrice[0] = getTotalPriceString(this.prices);
                this.totalCell.setTextAndValue(LocaleController.getString(R.string.PaymentTransactionTotal), this.totalPrice[0], true);
                if (this.currentStep != 4 || (this.paymentForm.invoice.flags & 256) == 0) {
                    str = "";
                } else {
                    FrameLayout frameLayout5 = new FrameLayout(context);
                    frameLayout5.setClipChildren(false);
                    frameLayout5.setBackgroundColor(getThemedColor(i39));
                    this.linearLayout2.addView(frameLayout5, LayoutHelper.createLinear(-1, this.paymentForm.invoice.suggested_tip_amounts.isEmpty() ? 40 : 78));
                    frameLayout5.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda12
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view7) {
                            PaymentFormActivity.this.lambda$createView$13(view7);
                        }
                    });
                    TextPriceCell textPriceCell4 = new TextPriceCell(context);
                    textPriceCell4.setBackgroundColor(getThemedColor(i39));
                    textPriceCell4.setTextAndValue(LocaleController.getString(R.string.PaymentTipOptional), "", false);
                    frameLayout5.addView(textPriceCell4);
                    this.inputFields = r5;
                    EditTextBoldCursor[] editTextBoldCursorArr2 = {new EditTextBoldCursor(context)};
                    this.inputFields[0].setTag(0);
                    this.inputFields[0].setTextSize(1, 16.0f);
                    EditTextBoldCursor editTextBoldCursor14 = this.inputFields[0];
                    int i40 = Theme.key_windowBackgroundWhiteGrayText2;
                    editTextBoldCursor14.setHintTextColor(getThemedColor(i40));
                    this.inputFields[0].setTextColor(getThemedColor(i40));
                    this.inputFields[0].setBackgroundDrawable(null);
                    this.inputFields[0].setCursorColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                    this.inputFields[0].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.inputFields[0].setCursorWidth(1.5f);
                    this.inputFields[0].setInputType(3);
                    this.inputFields[0].setImeOptions(268435462);
                    str = "";
                    this.inputFields[0].setHint(LocaleController.getInstance().formatCurrencyString(0L, this.paymentForm.invoice.currency));
                    this.inputFields[0].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                    this.inputFields[0].setGravity(LocaleController.isRTL ? 3 : 5);
                    frameLayout5.addView(this.inputFields[0], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 9.0f, 21.0f, 1.0f));
                    this.inputFields[0].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.11
                        private boolean anyBefore;
                        private int beforeTextLength;
                        char[] commas = {',', '.', 1643, 12289, 11841, 65040, 65041, 65104, 65105, 65292, 65380, 699};
                        private int enteredCharacterStart;
                        private boolean isDeletedChar;
                        private boolean lastDotEntered;
                        private String overrideText;

                        private int indexOfComma(String str11) {
                            int i41 = 0;
                            while (true) {
                                char[] cArr = this.commas;
                                if (i41 >= cArr.length) {
                                    return -1;
                                }
                                int indexOf = str11.indexOf(cArr[i41]);
                                if (indexOf >= 0) {
                                    return indexOf;
                                }
                                i41++;
                            }
                        }

                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            boolean z5;
                            EditTextBoldCursor editTextBoldCursor15;
                            EditTextBoldCursor editTextBoldCursor16;
                            EditTextBoldCursor editTextBoldCursor17;
                            int i41;
                            if (PaymentFormActivity.this.ignoreOnTextChange) {
                                return;
                            }
                            long longValue = PaymentFormActivity.this.tipAmount != null ? PaymentFormActivity.this.tipAmount.longValue() : 0L;
                            String str11 = this.overrideText;
                            if (str11 == null) {
                                str11 = LocaleController.fixNumbers(editable.toString());
                            }
                            int indexOfComma = indexOfComma(str11);
                            boolean z6 = indexOfComma >= 0;
                            int currencyExpDivider = LocaleController.getCurrencyExpDivider(PaymentFormActivity.this.paymentForm.invoice.currency);
                            String substring = indexOfComma >= 0 ? str11.substring(0, indexOfComma) : str11;
                            String str12 = "";
                            String substring2 = indexOfComma >= 0 ? str11.substring(indexOfComma + 1) : "";
                            long longValue2 = Utilities.parseLong(PhoneFormat.stripExceptNumbers(substring)).longValue() * currencyExpDivider;
                            long longValue3 = Utilities.parseLong(PhoneFormat.stripExceptNumbers(substring2)).longValue();
                            String str13 = "" + longValue3;
                            String str14 = "" + (currencyExpDivider - 1);
                            if (indexOfComma > 0 && str13.length() > str14.length()) {
                                longValue3 = Utilities.parseLong(this.enteredCharacterStart - indexOfComma < str13.length() ? str13.substring(0, str14.length()) : str13.substring(str13.length() - str14.length())).longValue();
                            }
                            PaymentFormActivity.this.tipAmount = Long.valueOf(longValue2 + longValue3);
                            if (PaymentFormActivity.this.paymentForm.invoice.max_tip_amount != 0 && PaymentFormActivity.this.tipAmount.longValue() > PaymentFormActivity.this.paymentForm.invoice.max_tip_amount) {
                                PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                                paymentFormActivity.tipAmount = Long.valueOf(paymentFormActivity.paymentForm.invoice.max_tip_amount);
                            }
                            int selectionStart = PaymentFormActivity.this.inputFields[0].getSelectionStart();
                            PaymentFormActivity.this.ignoreOnTextChange = true;
                            int i42 = (PaymentFormActivity.this.tipAmount.longValue() > 0L ? 1 : (PaymentFormActivity.this.tipAmount.longValue() == 0L ? 0 : -1));
                            EditTextBoldCursor[] editTextBoldCursorArr3 = PaymentFormActivity.this.inputFields;
                            if (i42 == 0) {
                                editTextBoldCursorArr3[0].setText("");
                                z5 = z6;
                            } else {
                                EditTextBoldCursor editTextBoldCursor18 = editTextBoldCursorArr3[0];
                                z5 = z6;
                                str12 = LocaleController.getInstance().formatCurrencyString(PaymentFormActivity.this.tipAmount.longValue(), false, z6, true, PaymentFormActivity.this.paymentForm.invoice.currency);
                                editTextBoldCursor18.setText(str12);
                            }
                            if (longValue < PaymentFormActivity.this.tipAmount.longValue() && longValue != 0 && this.anyBefore && selectionStart >= 0) {
                                editTextBoldCursor17 = PaymentFormActivity.this.inputFields[0];
                                i41 = Math.min(selectionStart, PaymentFormActivity.this.inputFields[0].length());
                            } else if (!this.isDeletedChar || this.beforeTextLength == PaymentFormActivity.this.inputFields[0].length()) {
                                if (this.lastDotEntered || !z5 || indexOfComma < 0) {
                                    editTextBoldCursor15 = PaymentFormActivity.this.inputFields[0];
                                    editTextBoldCursor16 = PaymentFormActivity.this.inputFields[0];
                                } else {
                                    int indexOfComma2 = indexOfComma(str12);
                                    if (indexOfComma2 > 0) {
                                        editTextBoldCursor17 = PaymentFormActivity.this.inputFields[0];
                                        i41 = indexOfComma2 + 1;
                                    } else {
                                        editTextBoldCursor15 = PaymentFormActivity.this.inputFields[0];
                                        editTextBoldCursor16 = PaymentFormActivity.this.inputFields[0];
                                    }
                                }
                                editTextBoldCursor15.setSelection(editTextBoldCursor16.length());
                                this.lastDotEntered = z5;
                                PaymentFormActivity.this.updateTotalPrice();
                                this.overrideText = null;
                                PaymentFormActivity.this.ignoreOnTextChange = false;
                            } else {
                                editTextBoldCursor17 = PaymentFormActivity.this.inputFields[0];
                                i41 = Math.max(0, Math.min(selectionStart, PaymentFormActivity.this.inputFields[0].length()));
                            }
                            editTextBoldCursor17.setSelection(i41);
                            this.lastDotEntered = z5;
                            PaymentFormActivity.this.updateTotalPrice();
                            this.overrideText = null;
                            PaymentFormActivity.this.ignoreOnTextChange = false;
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i41, int i42, int i43) {
                            String str11;
                            if (PaymentFormActivity.this.ignoreOnTextChange) {
                                return;
                            }
                            this.anyBefore = !TextUtils.isEmpty(charSequence);
                            this.overrideText = null;
                            this.beforeTextLength = charSequence == null ? 0 : charSequence.length();
                            this.enteredCharacterStart = i41;
                            boolean z5 = i42 == 1 && i43 == 0;
                            this.isDeletedChar = z5;
                            if (z5) {
                                String fixNumbers = LocaleController.fixNumbers(charSequence);
                                char charAt = fixNumbers.charAt(i41);
                                int indexOfComma = indexOfComma(fixNumbers);
                                String substring = indexOfComma >= 0 ? fixNumbers.substring(indexOfComma + 1) : "";
                                long longValue = Utilities.parseLong(PhoneFormat.stripExceptNumbers(substring)).longValue();
                                if ((charAt < '0' || charAt > '9') && (substring.length() == 0 || longValue != 0)) {
                                    while (true) {
                                        int i44 = i41 - 1;
                                        if (i44 < 0) {
                                            return;
                                        }
                                        char charAt2 = fixNumbers.charAt(i44);
                                        if (charAt2 >= '0' && charAt2 <= '9') {
                                            str11 = fixNumbers.substring(0, i44) + fixNumbers.substring(i41);
                                            break;
                                        }
                                        i41 = i44;
                                    }
                                } else if (indexOfComma <= 0 || i41 <= indexOfComma || longValue != 0) {
                                    return;
                                } else {
                                    str11 = fixNumbers.substring(0, indexOfComma - 1);
                                }
                                this.overrideText = str11;
                            }
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i41, int i42, int i43) {
                        }
                    });
                    this.inputFields[0].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda13
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView2, int i41, KeyEvent keyEvent) {
                            boolean lambda$createView$14;
                            lambda$createView$14 = PaymentFormActivity.lambda$createView$14(textView2, i41, keyEvent);
                            return lambda$createView$14;
                        }
                    });
                    this.inputFields[0].requestFocus();
                    if (!this.paymentForm.invoice.suggested_tip_amounts.isEmpty()) {
                        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
                        horizontalScrollView.setHorizontalScrollBarEnabled(false);
                        horizontalScrollView.setVerticalScrollBarEnabled(false);
                        horizontalScrollView.setClipToPadding(false);
                        horizontalScrollView.setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
                        horizontalScrollView.setFillViewport(true);
                        frameLayout5.addView(horizontalScrollView, LayoutHelper.createFrame(-1, 30.0f, 51, 0.0f, 44.0f, 0.0f, 0.0f));
                        final int[] iArr = new int[1];
                        final int[] iArr2 = new int[1];
                        final int size2 = this.paymentForm.invoice.suggested_tip_amounts.size();
                        r14 = 0;
                        c = 6;
                        c2 = 5;
                        LinearLayout linearLayout7 = new LinearLayout(context) { // from class: org.telegram.ui.PaymentFormActivity.12
                            boolean ignoreLayout;

                            @Override // android.widget.LinearLayout, android.view.View
                            protected void onMeasure(int i41, int i42) {
                                View childAt;
                                int size3 = View.MeasureSpec.getSize(i41);
                                this.ignoreLayout = true;
                                int dp = AndroidUtilities.dp(9.0f);
                                int i43 = size2;
                                int i44 = dp * (i43 - 1);
                                int i45 = (iArr[0] * i43) + i44;
                                float f = 1.0f;
                                if (i45 <= size3) {
                                    setWeightSum(1.0f);
                                    int childCount = getChildCount();
                                    for (int i46 = 0; i46 < childCount; i46++) {
                                        getChildAt(i46).getLayoutParams().width = 0;
                                        ((LinearLayout.LayoutParams) getChildAt(i46).getLayoutParams()).weight = 1.0f / childCount;
                                    }
                                } else if (iArr2[0] + i44 <= size3) {
                                    setWeightSum(1.0f);
                                    int i47 = size3 - i44;
                                    int childCount2 = getChildCount();
                                    for (int i48 = 0; i48 < childCount2; i48++) {
                                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildAt(i48).getLayoutParams();
                                        layoutParams.width = 0;
                                        float intValue = ((Integer) childAt.getTag(R.id.width_tag)).intValue() / i47;
                                        layoutParams.weight = intValue;
                                        f -= intValue;
                                    }
                                    float f2 = f / (size2 - 1);
                                    if (f2 > 0.0f) {
                                        int childCount3 = getChildCount();
                                        for (int i49 = 0; i49 < childCount3; i49++) {
                                            View childAt2 = getChildAt(i49);
                                            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) childAt2.getLayoutParams();
                                            if (((Integer) childAt2.getTag(R.id.width_tag)).intValue() != iArr[0]) {
                                                layoutParams2.weight += f2;
                                            }
                                        }
                                    }
                                } else {
                                    setWeightSum(0.0f);
                                    int childCount4 = getChildCount();
                                    for (int i50 = 0; i50 < childCount4; i50++) {
                                        getChildAt(i50).getLayoutParams().width = -2;
                                        ((LinearLayout.LayoutParams) getChildAt(i50).getLayoutParams()).weight = 0.0f;
                                    }
                                }
                                this.ignoreLayout = false;
                                super.onMeasure(i41, i42);
                            }

                            @Override // android.view.View, android.view.ViewParent
                            public void requestLayout() {
                                if (this.ignoreLayout) {
                                    return;
                                }
                                super.requestLayout();
                            }
                        };
                        this.tipLayout = linearLayout7;
                        linearLayout7.setOrientation(0);
                        horizontalScrollView.addView(this.tipLayout, LayoutHelper.createScroll(-1, 30, 51));
                        int themedColor = getThemedColor(Theme.key_contacts_inviteBackground);
                        int i41 = 0;
                        while (i41 < size2) {
                            final long longValue = ((Long) (LocaleController.isRTL ? this.paymentForm.invoice.suggested_tip_amounts.get((size2 - i41) - 1) : this.paymentForm.invoice.suggested_tip_amounts.get(i41))).longValue();
                            String formatCurrencyString = LocaleController.getInstance().formatCurrencyString(longValue, this.paymentForm.invoice.currency);
                            final TextView textView2 = new TextView(context);
                            textView2.setTextSize(1, 14.0f);
                            textView2.setTypeface(AndroidUtilities.bold());
                            textView2.setLines(1);
                            textView2.setTag(Long.valueOf(longValue));
                            textView2.setMaxLines(1);
                            textView2.setText(formatCurrencyString);
                            textView2.setPadding(AndroidUtilities.dp(15.0f), 0, AndroidUtilities.dp(15.0f), 0);
                            textView2.setTextColor(getThemedColor(Theme.key_chats_secretName));
                            textView2.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(15.0f), 536870911 & themedColor));
                            textView2.setSingleLine(true);
                            textView2.setGravity(17);
                            this.tipLayout.addView(textView2, LayoutHelper.createLinear(-2, -1, 19, 0, 0, i41 != size2 + (-1) ? 9 : 0, 0));
                            textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda15
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view7) {
                                    PaymentFormActivity.this.lambda$createView$15(textView2, longValue, view7);
                                }
                            });
                            int ceil = ((int) Math.ceil(textView2.getPaint().measureText(formatCurrencyString))) + AndroidUtilities.dp(30.0f);
                            textView2.setTag(R.id.width_tag, Integer.valueOf(ceil));
                            iArr[0] = Math.max(iArr[0], ceil);
                            iArr2[0] = iArr2[0] + ceil;
                            i41++;
                        }
                        this.linearLayout2.addView(this.totalCell);
                        this.sectionCell[2] = new ShadowSectionCell(context, this.resourcesProvider);
                        this.sectionCell[2].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        this.linearLayout2.addView(this.sectionCell[2], LayoutHelper.createLinear(-1, -2));
                        this.detailSettingsCell[r14] = new TextDetailSettingsCell(context);
                        this.detailSettingsCell[r14].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        TextDetailSettingsCell textDetailSettingsCell = this.detailSettingsCell[r14];
                        String str11 = this.cardName;
                        textDetailSettingsCell.setTextAndValueAndIcon((str11 != null || str11.length() <= 1) ? this.cardName : this.cardName.substring(r14, 1).toUpperCase() + this.cardName.substring(1), LocaleController.getString(R.string.PaymentCheckoutMethod), R.drawable.msg_payment_card, true);
                        i = (this.isCheckoutPreview || ((str3 = this.cardName) != null && str3.length() > 1)) ? 0 : 8;
                        this.detailSettingsCell[r14].setVisibility(i);
                        this.linearLayout2.addView(this.detailSettingsCell[r14]);
                        if (this.currentStep == 4) {
                            this.detailSettingsCell[r14].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda16
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view7) {
                                    PaymentFormActivity.this.lambda$createView$16(view7);
                                }
                            });
                        }
                        tLRPC$User = null;
                        for (i2 = 0; i2 < this.paymentForm.users.size(); i2++) {
                            TLRPC$User tLRPC$User5 = (TLRPC$User) this.paymentForm.users.get(i2);
                            if (tLRPC$User5.id == this.paymentForm.provider_id) {
                                tLRPC$User = tLRPC$User5;
                            }
                        }
                        this.detailSettingsCell[1] = new TextDetailSettingsCell(context);
                        this.detailSettingsCell[1].setBackground(Theme.getSelectorDrawable(true));
                        if (tLRPC$User == null) {
                            TextDetailSettingsCell textDetailSettingsCell2 = this.detailSettingsCell[1];
                            str2 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                            String string = LocaleController.getString(R.string.PaymentCheckoutProvider);
                            int i42 = R.drawable.msg_payment_provider;
                            TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo2 = this.validateRequest;
                            textDetailSettingsCell2.setTextAndValueAndIcon(str2, string, i42, ((tLRPC$TL_payments_validateRequestedInfo2 == null || (tLRPC$TL_payments_validateRequestedInfo2.info.shipping_address == null && this.shippingOption == null)) && ((tLRPC$TL_paymentRequestedInfo2 = this.paymentForm.saved_info) == null || tLRPC$TL_paymentRequestedInfo2.shipping_address == null)) ? false : true);
                            c3 = 1;
                            this.linearLayout2.addView(this.detailSettingsCell[1]);
                        } else {
                            c3 = 1;
                            str2 = str;
                        }
                        this.detailSettingsCell[c3].setVisibility(tLRPC$User == null ? i : 8);
                        tLRPC$TL_payments_validateRequestedInfo = this.validateRequest;
                        if (tLRPC$TL_payments_validateRequestedInfo == null || (this.isCheckoutPreview && (tLRPC$PaymentForm2 = this.paymentForm) != null && tLRPC$PaymentForm2.saved_info != null)) {
                            tLRPC$TL_paymentRequestedInfo = tLRPC$TL_payments_validateRequestedInfo == null ? tLRPC$TL_payments_validateRequestedInfo.info : this.paymentForm.saved_info;
                            this.detailSettingsCell[2] = new TextDetailSettingsCell(context);
                            this.detailSettingsCell[2].setVisibility(8);
                            this.linearLayout2.addView(this.detailSettingsCell[2]);
                            if (tLRPC$TL_paymentRequestedInfo.shipping_address != null) {
                                this.detailSettingsCell[2].setVisibility(r14);
                                if (this.currentStep == 4) {
                                    this.detailSettingsCell[2].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                                    this.detailSettingsCell[2].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda17
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(View view7) {
                                            PaymentFormActivity.this.lambda$createView$17(view7);
                                        }
                                    });
                                } else {
                                    this.detailSettingsCell[2].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                                }
                            }
                            this.detailSettingsCell[3] = new TextDetailSettingsCell(context);
                            this.detailSettingsCell[3].setVisibility(8);
                            this.linearLayout2.addView(this.detailSettingsCell[3]);
                            if (tLRPC$TL_paymentRequestedInfo.name != null) {
                                this.detailSettingsCell[3].setVisibility(r14);
                                if (this.currentStep == 4) {
                                    this.detailSettingsCell[3].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                                    this.detailSettingsCell[3].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda18
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(View view7) {
                                            PaymentFormActivity.this.lambda$createView$18(view7);
                                        }
                                    });
                                } else {
                                    this.detailSettingsCell[3].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                                }
                            }
                            this.detailSettingsCell[4] = new TextDetailSettingsCell(context);
                            this.detailSettingsCell[4].setVisibility(8);
                            this.linearLayout2.addView(this.detailSettingsCell[4]);
                            if (tLRPC$TL_paymentRequestedInfo.phone != null) {
                                this.detailSettingsCell[4].setVisibility(r14);
                                if (this.currentStep == 4) {
                                    this.detailSettingsCell[4].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                                    this.detailSettingsCell[4].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda19
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(View view7) {
                                            PaymentFormActivity.this.lambda$createView$19(view7);
                                        }
                                    });
                                } else {
                                    this.detailSettingsCell[4].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                                }
                            }
                            this.detailSettingsCell[c2] = new TextDetailSettingsCell(context);
                            this.detailSettingsCell[c2].setVisibility(8);
                            this.linearLayout2.addView(this.detailSettingsCell[c2]);
                            if (tLRPC$TL_paymentRequestedInfo.email != null) {
                                this.detailSettingsCell[c2].setVisibility(r14);
                                if (this.currentStep == 4) {
                                    this.detailSettingsCell[c2].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                                    this.detailSettingsCell[c2].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda20
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(View view7) {
                                            PaymentFormActivity.this.lambda$createView$20(view7);
                                        }
                                    });
                                } else {
                                    this.detailSettingsCell[c2].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                                }
                            }
                            if (this.shippingOption != null) {
                                this.detailSettingsCell[c] = new TextDetailSettingsCell(context);
                                this.detailSettingsCell[c].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                                this.detailSettingsCell[c].setTextAndValueAndIcon(this.shippingOption.title, LocaleController.getString(R.string.PaymentCheckoutShippingMethod), R.drawable.msg_payment_delivery, r14);
                                this.linearLayout2.addView(this.detailSettingsCell[c]);
                            }
                            setAddressFields(tLRPC$TL_paymentRequestedInfo);
                        }
                        if (this.currentStep == 4) {
                            this.recurrentAccepted = !this.isCheckoutPreview;
                            if (this.invoiceInput instanceof TLRPC$TL_inputInvoiceStars) {
                                this.recurrentAccepted = true;
                            }
                            this.bottomLayout = new BottomFrameLayout(context, this.paymentForm);
                            int i43 = Build.VERSION.SDK_INT;
                            if (i43 >= 21) {
                                View view7 = new View(context);
                                view7.setBackground(Theme.getSelectorDrawable(getThemedColor(Theme.key_listSelector), (boolean) r14));
                                i3 = -1;
                                this.bottomLayout.addView(view7, LayoutHelper.createFrame(-1, -1.0f));
                            } else {
                                i3 = -1;
                            }
                            frameLayout2.addView(this.bottomLayout, LayoutHelper.createFrame(i3, 48, 80));
                            this.bottomLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda21
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view8) {
                                    PaymentFormActivity.this.lambda$createView$23(str2, view8);
                                }
                            });
                            TextView textView3 = new TextView(context);
                            this.payTextView = textView3;
                            int i44 = Theme.key_contacts_inviteText;
                            textView3.setTextColor(getThemedColor(i44));
                            TextView textView4 = this.payTextView;
                            int i45 = R.string.PaymentCheckoutPay;
                            Object[] objArr = new Object[1];
                            objArr[r14] = this.totalPrice[r14];
                            textView4.setText(LocaleController.formatString("PaymentCheckoutPay", i45, objArr));
                            this.payTextView.setTextSize(1, 14.0f);
                            this.payTextView.setGravity(17);
                            this.payTextView.setTypeface(AndroidUtilities.bold());
                            this.bottomLayout.addView(this.payTextView, LayoutHelper.createFrame(-1, -1.0f));
                            ContextProgressView contextProgressView2 = new ContextProgressView(context, r14);
                            this.progressViewButton = contextProgressView2;
                            contextProgressView2.setVisibility(4);
                            int themedColor2 = getThemedColor(i44);
                            this.progressViewButton.setColors(805306367 & themedColor2, themedColor2);
                            this.bottomLayout.addView(this.progressViewButton, LayoutHelper.createFrame(-1, -1.0f));
                            this.bottomLayout.setChecked(this.recurrentAccepted, r14);
                            this.payTextView.setAlpha(!this.recurrentAccepted ? 0.8f : 1.0f);
                            this.doneItem.setEnabled(r14);
                            this.doneItem.getContentView().setVisibility(4);
                            AndroidUtilities.checkAndroidTheme(context, true);
                            WebView webView2 = new WebView(context) { // from class: org.telegram.ui.PaymentFormActivity.17
                                @Override // android.webkit.WebView, android.view.View
                                public boolean onTouchEvent(MotionEvent motionEvent) {
                                    getParent().requestDisallowInterceptTouchEvent(true);
                                    return super.onTouchEvent(motionEvent);
                                }
                            };
                            this.webView = webView2;
                            webView2.setBackgroundColor(-1);
                            this.webView.getSettings().setJavaScriptEnabled(true);
                            this.webView.getSettings().setDomStorageEnabled(true);
                            this.webView.getSettings().setSupportZoom(true);
                            this.webView.getSettings().setBuiltInZoomControls(true);
                            this.webView.getSettings().setDisplayZoomControls(r14);
                            this.webView.getSettings().setUseWideViewPort(true);
                            if (i43 >= 21) {
                                this.webView.getSettings().setMixedContentMode(r14);
                                CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
                            }
                            this.webView.setWebViewClient(new 18(context));
                            this.recurrentAcceptCell = null;
                            if (this.paymentForm.invoice.terms_url != null) {
                                RecurrentPaymentsAcceptCell recurrentPaymentsAcceptCell = new RecurrentPaymentsAcceptCell(context, getResourceProvider());
                                this.recurrentAcceptCell = recurrentPaymentsAcceptCell;
                                recurrentPaymentsAcceptCell.setChecked(this.recurrentAccepted);
                                String string2 = LocaleController.getString(R.string.PaymentCheckoutAcceptRecurrent);
                                ?? spannableStringBuilder = new SpannableStringBuilder(string2);
                                int indexOf = string2.indexOf(42);
                                int lastIndexOf = string2.lastIndexOf(42);
                                String str12 = string2;
                                str12 = string2;
                                if (indexOf != -1 && lastIndexOf != -1) {
                                    ?? spannableString = new SpannableString(string2.substring(indexOf + 1, lastIndexOf));
                                    spannableString.setSpan(new URLSpanNoUnderline(this.paymentForm.invoice.terms_url), r14, spannableString.length(), 33);
                                    spannableStringBuilder.replace(indexOf, lastIndexOf + 1, spannableString);
                                    str12 = string2.substring(r14, indexOf) + spannableString + string2.substring(i4);
                                }
                                int indexOf2 = str12.indexOf("%1$s");
                                if (indexOf2 != -1) {
                                    spannableStringBuilder.replace(indexOf2, indexOf2 + 4, this.currentBotName);
                                    spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.bold()), indexOf2, this.currentBotName.length() + indexOf2, 33);
                                }
                                this.recurrentAcceptCell.setText(spannableStringBuilder);
                                this.recurrentAcceptCell.setBackground(Theme.createSelectorWithBackgroundDrawable(getThemedColor(Theme.key_windowBackgroundWhite), getThemedColor(Theme.key_listSelector)));
                                this.recurrentAcceptCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda22
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view8) {
                                        PaymentFormActivity.this.lambda$createView$24(view8);
                                    }
                                });
                                frameLayout2.addView(this.recurrentAcceptCell, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 48.0f));
                            }
                            frameLayout2.addView(this.webView, LayoutHelper.createFrame(-1, -1.0f));
                            this.webView.setVisibility(8);
                        }
                        this.sectionCell[1] = new ShadowSectionCell(context, this.resourcesProvider);
                        this.sectionCell[1].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        if (i == 0 && this.currentStep == 4 && this.validateRequest == null && ((tLRPC$PaymentForm = this.paymentForm) == null || tLRPC$PaymentForm.saved_info == null)) {
                            c4 = 1;
                            this.sectionCell[1].setVisibility(i);
                        } else {
                            c4 = 1;
                        }
                        linearLayout = this.linearLayout2;
                        view = this.sectionCell[c4];
                    }
                }
                r14 = 0;
                c = 6;
                c2 = 5;
                this.linearLayout2.addView(this.totalCell);
                this.sectionCell[2] = new ShadowSectionCell(context, this.resourcesProvider);
                this.sectionCell[2].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                this.linearLayout2.addView(this.sectionCell[2], LayoutHelper.createLinear(-1, -2));
                this.detailSettingsCell[r14] = new TextDetailSettingsCell(context);
                this.detailSettingsCell[r14].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                TextDetailSettingsCell textDetailSettingsCell3 = this.detailSettingsCell[r14];
                String str112 = this.cardName;
                if (str112 != null) {
                }
                textDetailSettingsCell3.setTextAndValueAndIcon((str112 != null || str112.length() <= 1) ? this.cardName : this.cardName.substring(r14, 1).toUpperCase() + this.cardName.substring(1), LocaleController.getString(R.string.PaymentCheckoutMethod), R.drawable.msg_payment_card, true);
                if (this.isCheckoutPreview) {
                }
                this.detailSettingsCell[r14].setVisibility(i);
                this.linearLayout2.addView(this.detailSettingsCell[r14]);
                if (this.currentStep == 4) {
                }
                tLRPC$User = null;
                while (i2 < this.paymentForm.users.size()) {
                }
                this.detailSettingsCell[1] = new TextDetailSettingsCell(context);
                this.detailSettingsCell[1].setBackground(Theme.getSelectorDrawable(true));
                if (tLRPC$User == null) {
                }
                this.detailSettingsCell[c3].setVisibility(tLRPC$User == null ? i : 8);
                tLRPC$TL_payments_validateRequestedInfo = this.validateRequest;
                if (tLRPC$TL_payments_validateRequestedInfo == null) {
                }
                if (tLRPC$TL_payments_validateRequestedInfo == null) {
                }
                this.detailSettingsCell[2] = new TextDetailSettingsCell(context);
                this.detailSettingsCell[2].setVisibility(8);
                this.linearLayout2.addView(this.detailSettingsCell[2]);
                if (tLRPC$TL_paymentRequestedInfo.shipping_address != null) {
                }
                this.detailSettingsCell[3] = new TextDetailSettingsCell(context);
                this.detailSettingsCell[3].setVisibility(8);
                this.linearLayout2.addView(this.detailSettingsCell[3]);
                if (tLRPC$TL_paymentRequestedInfo.name != null) {
                }
                this.detailSettingsCell[4] = new TextDetailSettingsCell(context);
                this.detailSettingsCell[4].setVisibility(8);
                this.linearLayout2.addView(this.detailSettingsCell[4]);
                if (tLRPC$TL_paymentRequestedInfo.phone != null) {
                }
                this.detailSettingsCell[c2] = new TextDetailSettingsCell(context);
                this.detailSettingsCell[c2].setVisibility(8);
                this.linearLayout2.addView(this.detailSettingsCell[c2]);
                if (tLRPC$TL_paymentRequestedInfo.email != null) {
                }
                if (this.shippingOption != null) {
                }
                setAddressFields(tLRPC$TL_paymentRequestedInfo);
                if (this.currentStep == 4) {
                }
                this.sectionCell[1] = new ShadowSectionCell(context, this.resourcesProvider);
                this.sectionCell[1].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                if (i == 0) {
                }
                c4 = 1;
                linearLayout = this.linearLayout2;
                view = this.sectionCell[c4];
            } else if (i20 == 6) {
                EditTextSettingsCell editTextSettingsCell = new EditTextSettingsCell(context);
                this.codeFieldCell = editTextSettingsCell;
                editTextSettingsCell.setTextAndHint("", LocaleController.getString(R.string.PasswordCode), false);
                this.codeFieldCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                EditTextBoldCursor textView5 = this.codeFieldCell.getTextView();
                textView5.setInputType(3);
                textView5.setImeOptions(6);
                textView5.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda8
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView6, int i46, KeyEvent keyEvent) {
                        boolean lambda$createView$25;
                        lambda$createView$25 = PaymentFormActivity.this.lambda$createView$25(textView6, i46, keyEvent);
                        return lambda$createView$25;
                    }
                });
                textView5.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.19
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        if (PaymentFormActivity.this.emailCodeLength == 0 || editable.length() != PaymentFormActivity.this.emailCodeLength) {
                            return;
                        }
                        PaymentFormActivity.this.sendSavePassword(false);
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i46, int i47, int i48) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i46, int i47, int i48) {
                    }
                });
                this.linearLayout2.addView(this.codeFieldCell, LayoutHelper.createLinear(-1, -2));
                this.bottomCell[2] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                this.bottomCell[2].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                this.linearLayout2.addView(this.bottomCell[2], LayoutHelper.createLinear(-1, -2));
                this.settingsCell[1] = new TextSettingsCell(context, this.resourcesProvider);
                this.settingsCell[1].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                TextSettingsCell textSettingsCell = this.settingsCell[1];
                int i46 = Theme.key_windowBackgroundWhiteBlackText;
                textSettingsCell.setTag(Integer.valueOf(i46));
                this.settingsCell[1].setTextColor(getThemedColor(i46));
                this.settingsCell[1].setText(LocaleController.getString(R.string.ResendCode), true);
                this.linearLayout2.addView(this.settingsCell[1], LayoutHelper.createLinear(-1, -2));
                this.settingsCell[1].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda9
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view8) {
                        PaymentFormActivity.this.lambda$createView$27(view8);
                    }
                });
                this.settingsCell[0] = new TextSettingsCell(context, this.resourcesProvider);
                this.settingsCell[0].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                TextSettingsCell textSettingsCell2 = this.settingsCell[0];
                int i47 = Theme.key_text_RedRegular;
                textSettingsCell2.setTag(Integer.valueOf(i47));
                this.settingsCell[0].setTextColor(getThemedColor(i47));
                this.settingsCell[0].setText(LocaleController.getString(R.string.AbortPassword), false);
                this.linearLayout2.addView(this.settingsCell[0], LayoutHelper.createLinear(-1, -2));
                this.settingsCell[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda10
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view8) {
                        PaymentFormActivity.this.lambda$createView$29(view8);
                    }
                });
                this.inputFields = new EditTextBoldCursor[3];
                int i48 = 0;
                for (int i49 = 3; i48 < i49; i49 = 3) {
                    if (i48 == 0) {
                        this.headerCell[0] = new HeaderCell(context, this.resourcesProvider);
                        this.headerCell[0].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        this.headerCell[0].setText(LocaleController.getString(R.string.PaymentPasswordTitle));
                        this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                    } else if (i48 == 2) {
                        this.headerCell[1] = new HeaderCell(context, this.resourcesProvider);
                        this.headerCell[1].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        this.headerCell[1].setText(LocaleController.getString(R.string.PaymentPasswordEmailTitle));
                        i5 = -1;
                        this.linearLayout2.addView(this.headerCell[1], LayoutHelper.createLinear(-1, -2));
                        FrameLayout frameLayout6 = new FrameLayout(context);
                        frameLayout6.setClipChildren(false);
                        this.linearLayout2.addView(frameLayout6, LayoutHelper.createLinear(i5, 50));
                        int i50 = Theme.key_windowBackgroundWhite;
                        frameLayout6.setBackgroundColor(getThemedColor(i50));
                        if (i48 != 0) {
                            View view8 = new View(context) { // from class: org.telegram.ui.PaymentFormActivity.20
                                @Override // android.view.View
                                protected void onDraw(Canvas canvas) {
                                    canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                                }
                            };
                            view8.setBackgroundColor(getThemedColor(i50));
                            this.dividers.add(view8);
                            frameLayout6.addView(view8, new FrameLayout.LayoutParams(-1, 1, 83));
                        }
                        this.inputFields[i48] = new EditTextBoldCursor(context);
                        this.inputFields[i48].setTag(Integer.valueOf(i48));
                        this.inputFields[i48].setTextSize(1, 16.0f);
                        this.inputFields[i48].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                        EditTextBoldCursor editTextBoldCursor15 = this.inputFields[i48];
                        int i51 = Theme.key_windowBackgroundWhiteBlackText;
                        editTextBoldCursor15.setTextColor(getThemedColor(i51));
                        this.inputFields[i48].setBackgroundDrawable(null);
                        this.inputFields[i48].setCursorColor(getThemedColor(i51));
                        this.inputFields[i48].setCursorSize(AndroidUtilities.dp(20.0f));
                        this.inputFields[i48].setCursorWidth(1.5f);
                        if (i48 != 0 || i48 == 1) {
                            this.inputFields[i48].setInputType(NotificationCenter.walletPendingTransactionsChanged);
                            this.inputFields[i48].setTypeface(Typeface.DEFAULT);
                            this.inputFields[i48].setImeOptions(268435461);
                        } else {
                            this.inputFields[i48].setInputType(33);
                            this.inputFields[i48].setImeOptions(268435462);
                        }
                        if (i48 == 0) {
                            if (i48 == 1) {
                                editTextBoldCursor = this.inputFields[i48];
                                i6 = R.string.PaymentPasswordReEnter;
                            } else if (i48 == 2) {
                                editTextBoldCursor = this.inputFields[i48];
                                i6 = R.string.PaymentPasswordEmail;
                            }
                            editTextBoldCursor.setHint(LocaleController.getString(i6));
                        } else {
                            this.inputFields[i48].setHint(LocaleController.getString(R.string.PaymentPasswordEnter));
                            this.inputFields[i48].requestFocus();
                        }
                        this.inputFields[i48].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                        this.inputFields[i48].setGravity(!LocaleController.isRTL ? 5 : 3);
                        frameLayout6.addView(this.inputFields[i48], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                        this.inputFields[i48].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda11
                            @Override // android.widget.TextView.OnEditorActionListener
                            public final boolean onEditorAction(TextView textView6, int i52, KeyEvent keyEvent) {
                                boolean lambda$createView$30;
                                lambda$createView$30 = PaymentFormActivity.this.lambda$createView$30(textView6, i52, keyEvent);
                                return lambda$createView$30;
                            }
                        });
                        if (i48 != 1) {
                            this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                            this.bottomCell[0].setText(LocaleController.getString(R.string.PaymentPasswordInfo));
                            this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                            linearLayout2 = this.linearLayout2;
                            textInfoPrivacyCell = this.bottomCell[0];
                        } else if (i48 == 2) {
                            this.bottomCell[1] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                            this.bottomCell[1].setText(LocaleController.getString(R.string.PaymentPasswordEmailInfo));
                            this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                            linearLayout2 = this.linearLayout2;
                            textInfoPrivacyCell = this.bottomCell[1];
                        } else {
                            i48++;
                        }
                        linearLayout2.addView(textInfoPrivacyCell, LayoutHelper.createLinear(-1, -2));
                        i48++;
                    }
                    i5 = -1;
                    FrameLayout frameLayout62 = new FrameLayout(context);
                    frameLayout62.setClipChildren(false);
                    this.linearLayout2.addView(frameLayout62, LayoutHelper.createLinear(i5, 50));
                    int i502 = Theme.key_windowBackgroundWhite;
                    frameLayout62.setBackgroundColor(getThemedColor(i502));
                    if (i48 != 0) {
                    }
                    this.inputFields[i48] = new EditTextBoldCursor(context);
                    this.inputFields[i48].setTag(Integer.valueOf(i48));
                    this.inputFields[i48].setTextSize(1, 16.0f);
                    this.inputFields[i48].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                    EditTextBoldCursor editTextBoldCursor152 = this.inputFields[i48];
                    int i512 = Theme.key_windowBackgroundWhiteBlackText;
                    editTextBoldCursor152.setTextColor(getThemedColor(i512));
                    this.inputFields[i48].setBackgroundDrawable(null);
                    this.inputFields[i48].setCursorColor(getThemedColor(i512));
                    this.inputFields[i48].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.inputFields[i48].setCursorWidth(1.5f);
                    if (i48 != 0) {
                    }
                    this.inputFields[i48].setInputType(NotificationCenter.walletPendingTransactionsChanged);
                    this.inputFields[i48].setTypeface(Typeface.DEFAULT);
                    this.inputFields[i48].setImeOptions(268435461);
                    if (i48 == 0) {
                    }
                    this.inputFields[i48].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                    this.inputFields[i48].setGravity(!LocaleController.isRTL ? 5 : 3);
                    frameLayout62.addView(this.inputFields[i48], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                    this.inputFields[i48].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda11
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView6, int i52, KeyEvent keyEvent) {
                            boolean lambda$createView$30;
                            lambda$createView$30 = PaymentFormActivity.this.lambda$createView$30(textView6, i52, keyEvent);
                            return lambda$createView$30;
                        }
                    });
                    if (i48 != 1) {
                    }
                    linearLayout2.addView(textInfoPrivacyCell, LayoutHelper.createLinear(-1, -2));
                    i48++;
                }
                updatePasswordFields();
            }
            linearLayout.addView(view, LayoutHelper.createLinear(-1, -2));
        }
        return this.fragmentView;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.twoStepPasswordChanged) {
            TLRPC$PaymentForm tLRPC$PaymentForm = this.paymentForm;
            tLRPC$PaymentForm.password_missing = false;
            tLRPC$PaymentForm.can_save_credentials = true;
        } else if (i != NotificationCenter.didRemoveTwoStepPassword) {
            if (i == NotificationCenter.paymentFinished) {
                this.paymentStatusSent = true;
                removeSelfFromStack();
                return;
            }
            return;
        } else {
            TLRPC$PaymentForm tLRPC$PaymentForm2 = this.paymentForm;
            tLRPC$PaymentForm2.password_missing = true;
            tLRPC$PaymentForm2.can_save_credentials = false;
        }
        updateSavePaymentField();
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0039 A[Catch: Exception -> 0x001a, TryCatch #0 {Exception -> 0x001a, blocks: (B:2:0x0000, B:4:0x000d, B:6:0x0013, B:11:0x001d, B:13:0x0024, B:20:0x0039, B:21:0x0041, B:23:0x0047, B:27:0x0053, B:29:0x0061, B:33:0x0074, B:30:0x006e, B:35:0x0085), top: B:39:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0047 A[Catch: Exception -> 0x001a, TryCatch #0 {Exception -> 0x001a, blocks: (B:2:0x0000, B:4:0x000d, B:6:0x0013, B:11:0x001d, B:13:0x0024, B:20:0x0039, B:21:0x0041, B:23:0x0047, B:27:0x0053, B:29:0x0061, B:33:0x0074, B:30:0x006e, B:35:0x0085), top: B:39:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void fillNumber(String str) {
        boolean z;
        boolean z2;
        int checkSelfPermission;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
            if (str == null && (telephonyManager.getSimState() == 1 || telephonyManager.getPhoneType() == 0)) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                checkSelfPermission = getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE");
                if (checkSelfPermission != 0) {
                    z = false;
                    if (str == null || z) {
                        if (str == null) {
                            str = PhoneFormat.stripExceptNumbers(telephonyManager.getLine1Number());
                        }
                        if (TextUtils.isEmpty(str)) {
                            int i = 4;
                            String str2 = null;
                            if (str.length() > 4) {
                                while (true) {
                                    if (i < 1) {
                                        z2 = false;
                                        break;
                                    }
                                    String substring = str.substring(0, i);
                                    if (((String) this.codesMap.get(substring)) != null) {
                                        str2 = str.substring(i);
                                        this.inputFields[8].setText(substring);
                                        z2 = true;
                                        break;
                                    }
                                    i--;
                                }
                                if (!z2) {
                                    str2 = str.substring(1);
                                    this.inputFields[8].setText(str.substring(0, 1));
                                }
                            }
                            if (str2 != null) {
                                this.inputFields[9].setText(str2);
                                EditTextBoldCursor editTextBoldCursor = this.inputFields[9];
                                editTextBoldCursor.setSelection(editTextBoldCursor.length());
                                return;
                            }
                            return;
                        }
                        return;
                    }
                    return;
                }
            }
            z = true;
            if (str == null) {
            }
            if (str == null) {
            }
            if (TextUtils.isEmpty(str)) {
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public Optional getIsReadyToPayRequest() {
        try {
            JSONObject baseRequest = getBaseRequest();
            baseRequest.put("allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));
            return Optional.of(baseRequest);
        } catch (JSONException unused) {
            return Optional.empty();
        }
    }

    public int getOtherSameFragmentDiff() {
        INavigationLayout iNavigationLayout = this.parentLayout;
        int i = 0;
        if (iNavigationLayout == null || iNavigationLayout.getFragmentStack() == null) {
            return 0;
        }
        int indexOf = this.parentLayout.getFragmentStack().indexOf(this);
        if (indexOf == -1) {
            indexOf = this.parentLayout.getFragmentStack().size();
        }
        while (true) {
            if (i >= this.parentLayout.getFragmentStack().size()) {
                i = indexOf;
                break;
            } else if (((BaseFragment) this.parentLayout.getFragmentStack().get(i)) instanceof PaymentFormActivity) {
                break;
            } else {
                i++;
            }
        }
        return i - indexOf;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Theme.ResourcesProvider getResourceProvider() {
        return this.resourcesProvider;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList getThemeDescriptions() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i = ThemeDescription.FLAG_BACKGROUND;
        int i2 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i2));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        ContextProgressView contextProgressView = this.progressView;
        int i3 = Theme.key_contextProgressInner2;
        arrayList.add(new ThemeDescription(contextProgressView, 0, null, null, null, null, i3));
        ContextProgressView contextProgressView2 = this.progressView;
        int i4 = Theme.key_contextProgressOuter2;
        arrayList.add(new ThemeDescription(contextProgressView2, 0, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, i4));
        if (this.inputFields != null) {
            for (int i5 = 0; i5 < this.inputFields.length; i5++) {
                arrayList.add(new ThemeDescription((View) this.inputFields[i5].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(this.inputFields[i5], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(this.inputFields[i5], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
            }
        } else {
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        }
        if (this.radioCells != null) {
            for (int i6 = 0; i6 < this.radioCells.length; i6++) {
                arrayList.add(new ThemeDescription(this.radioCells[i6], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(this.radioCells[i6], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
                arrayList.add(new ThemeDescription(this.radioCells[i6], 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(this.radioCells[i6], ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackground));
                arrayList.add(new ThemeDescription(this.radioCells[i6], ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackgroundChecked));
            }
        } else {
            arrayList.add(new ThemeDescription((View) null, 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription((View) null, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackground));
            arrayList.add(new ThemeDescription((View) null, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackgroundChecked));
        }
        for (int i7 = 0; i7 < this.headerCell.length; i7++) {
            arrayList.add(new ThemeDescription(this.headerCell[i7], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(this.headerCell[i7], 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        }
        for (int i8 = 0; i8 < this.sectionCell.length; i8++) {
            arrayList.add(new ThemeDescription(this.sectionCell[i8], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        }
        for (int i9 = 0; i9 < this.bottomCell.length; i9++) {
            arrayList.add(new ThemeDescription(this.bottomCell[i9], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
            arrayList.add(new ThemeDescription(this.bottomCell[i9], 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
            arrayList.add(new ThemeDescription(this.bottomCell[i9], ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteLinkText));
        }
        for (int i10 = 0; i10 < this.dividers.size(); i10++) {
            arrayList.add(new ThemeDescription((View) this.dividers.get(i10), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
        }
        EditTextSettingsCell editTextSettingsCell = this.codeFieldCell;
        int i11 = ThemeDescription.FLAG_BACKGROUND;
        int i12 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(editTextSettingsCell, i11, null, null, null, null, i12));
        int i13 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.codeFieldCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i13));
        arrayList.add(new ThemeDescription(this.codeFieldCell, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteHintText));
        arrayList.add(new ThemeDescription(this.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i13));
        arrayList.add(new ThemeDescription(this.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i13));
        arrayList.add(new ThemeDescription(this.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrack));
        arrayList.add(new ThemeDescription(this.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackChecked));
        arrayList.add(new ThemeDescription(this.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, i12));
        arrayList.add(new ThemeDescription(this.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        for (int i14 = 0; i14 < this.settingsCell.length; i14++) {
            arrayList.add(new ThemeDescription(this.settingsCell[i14], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(this.settingsCell[i14], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
            arrayList.add(new ThemeDescription(this.settingsCell[i14], 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        }
        arrayList.add(new ThemeDescription(this.payTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText6));
        int i15 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextPriceCell.class}, null, null, null, i15));
        int i16 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i16));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i16));
        int i17 = Theme.key_windowBackgroundWhiteGrayText2;
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i17));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i17));
        arrayList.add(new ThemeDescription(this.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, i15));
        arrayList.add(new ThemeDescription(this.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        for (int i18 = 1; i18 < this.detailSettingsCell.length; i18++) {
            arrayList.add(new ThemeDescription(this.detailSettingsCell[i18], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(this.detailSettingsCell[i18], 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(this.detailSettingsCell[i18], 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        }
        PaymentInfoCell paymentInfoCell = this.paymentInfoCell;
        int i19 = ThemeDescription.FLAG_BACKGROUND;
        int i20 = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(paymentInfoCell, i19, null, null, null, null, i20));
        int i21 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i21));
        arrayList.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"detailTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i21));
        arrayList.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"detailExTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, i20));
        arrayList.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return this.swipeBackEnabled;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, final int i2, final Intent intent) {
        if (i == 991) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$onActivityResultFragment$39(i2, intent);
                }
            });
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (this.shouldNavigateBack) {
            this.webView.loadUrl(this.webViewUrl);
            this.shouldNavigateBack = false;
            return false;
        }
        return !this.donePressed;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onBecomeFullyVisible() {
        super.onBecomeFullyVisible();
        if (this.currentStep == 4 && this.needPayAfterTransition) {
            this.needPayAfterTransition = false;
            this.bottomLayout.callOnClick();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.twoStepPasswordChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didRemoveTwoStepPassword);
        if (this.currentStep != 4 || this.isCheckoutPreview) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.paymentFinished);
        }
        return super.onFragmentCreate();
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0086, code lost:
        if (r0 != 6) goto L22;
     */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onFragmentDestroy() {
        PaymentFormActivityDelegate paymentFormActivityDelegate = this.delegate;
        if (paymentFormActivityDelegate != null) {
            paymentFormActivityDelegate.onFragmentDestroyed();
        }
        AndroidUtilities.checkAndroidTheme(getContext(), false);
        if (!this.paymentStatusSent) {
            this.invoiceStatus = InvoiceStatus.CANCELLED;
            if (this.paymentFormCallback != null && getOtherSameFragmentDiff() == 0) {
                this.paymentFormCallback.onInvoiceStatusChanged(this.invoiceStatus);
            }
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.twoStepPasswordChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didRemoveTwoStepPassword);
        if (this.currentStep != 4 || this.isCheckoutPreview) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.paymentFinished);
        }
        WebView webView = this.webView;
        if (webView != null) {
            try {
                ViewParent parent = webView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(this.webView);
                }
                this.webView.stopLoading();
                this.webView.loadUrl("about:blank");
                this.webViewUrl = null;
                this.webView.destroy();
                this.webView = null;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        try {
            int i = this.currentStep;
            if (i != 2) {
            }
            if (Build.VERSION.SDK_INT >= 23) {
                if (SharedConfig.passcodeHash.length() != 0) {
                    if (SharedConfig.allowScreenCapture) {
                    }
                }
                getParentActivity().getWindow().clearFlags(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM);
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
        super.onFragmentDestroy();
        this.canceled = true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                int i = this.currentStep;
                if ((i == 2 || i == 6) && !this.paymentForm.invoice.test) {
                    getParentActivity().getWindow().setFlags(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM, LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM);
                } else if (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture) {
                    getParentActivity().getWindow().clearFlags(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM);
                }
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        EditTextBoldCursor editTextBoldCursor;
        if (!z || z2) {
            return;
        }
        WebView webView = this.webView;
        if (webView != null) {
            if (this.currentStep != 4) {
                TLRPC$TL_paymentFormMethod tLRPC$TL_paymentFormMethod = this.paymentFormMethod;
                String str = tLRPC$TL_paymentFormMethod != null ? tLRPC$TL_paymentFormMethod.url : this.paymentForm.url;
                this.webViewUrl = str;
                webView.loadUrl(str);
                return;
            }
            return;
        }
        int i = this.currentStep;
        if (i == 2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$onTransitionAnimationEnd$38();
                }
            }, 100L);
            return;
        }
        if (i == 3) {
            this.inputFields[1].requestFocus();
            editTextBoldCursor = this.inputFields[1];
        } else if (i == 4) {
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            if (editTextBoldCursorArr != null) {
                editTextBoldCursorArr[0].requestFocus();
                return;
            }
            return;
        } else if (i != 6 || this.waitingForEmail) {
            return;
        } else {
            this.inputFields[0].requestFocus();
            editTextBoldCursor = this.inputFields[0];
        }
        AndroidUtilities.showKeyboard(editTextBoldCursor);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean presentFragment(BaseFragment baseFragment) {
        onPresentFragment(baseFragment);
        return super.presentFragment(baseFragment);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean presentFragment(BaseFragment baseFragment, boolean z) {
        onPresentFragment(baseFragment);
        return super.presentFragment(baseFragment, z);
    }

    public void setPaymentFormCallback(PaymentFormCallback paymentFormCallback) {
        this.paymentFormCallback = paymentFormCallback;
    }

    public void setResourcesProvider(Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }
}
