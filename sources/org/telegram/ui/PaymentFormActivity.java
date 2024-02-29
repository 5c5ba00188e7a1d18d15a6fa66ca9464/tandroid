package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
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
import java.io.IOException;
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
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$InputPaymentCredentials;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$PasswordKdfAlgo;
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
import org.telegram.tgnet.TLRPC$TL_inputInvoicePremiumGiftCode;
import org.telegram.tgnet.TLRPC$TL_inputInvoiceSlug;
import org.telegram.tgnet.TLRPC$TL_inputPaymentCredentialsGooglePay;
import org.telegram.tgnet.TLRPC$TL_invoice;
import org.telegram.tgnet.TLRPC$TL_labeledPrice;
import org.telegram.tgnet.TLRPC$TL_messageActionPaymentSent;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow;
import org.telegram.tgnet.TLRPC$TL_paymentFormMethod;
import org.telegram.tgnet.TLRPC$TL_paymentRequestedInfo;
import org.telegram.tgnet.TLRPC$TL_paymentSavedCredentialsCard;
import org.telegram.tgnet.TLRPC$TL_payments_clearSavedInfo;
import org.telegram.tgnet.TLRPC$TL_payments_paymentForm;
import org.telegram.tgnet.TLRPC$TL_payments_paymentReceipt;
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
import org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda31;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.HintEditText;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Premium.boosts.BoostRepository$$ExternalSyntheticLambda33;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.UndoView;
import org.telegram.ui.CountrySelectActivity;
import org.telegram.ui.PaymentFormActivity;
/* loaded from: classes3.dex */
public class PaymentFormActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private TLRPC$User botUser;
    private TextInfoPrivacyCell[] bottomCell;
    private BottomFrameLayout bottomLayout;
    private boolean canceled;
    private String cardName;
    private TextCheckCell checkCell1;
    private EditTextSettingsCell codeFieldCell;
    private HashMap<String, String> codesMap;
    private ArrayList<String> countriesArray;
    private HashMap<String, String> countriesMap;
    private CountrySelectActivity.Country country;
    private String countryName;
    private String currentBotName;
    private String currentItemName;
    private TLRPC$account_Password currentPassword;
    private int currentStep;
    private PaymentFormActivityDelegate delegate;
    private TextDetailSettingsCell[] detailSettingsCell;
    private ArrayList<View> dividers;
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
    private TLRPC$TL_inputInvoicePremiumGiftCode invoicePremiumGiftCode;
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
    private TLRPC$TL_payments_paymentForm paymentForm;
    private PaymentFormCallback paymentFormCallback;
    private TLRPC$TL_paymentFormMethod paymentFormMethod;
    private PaymentInfoCell paymentInfoCell;
    private String paymentJson;
    private TLRPC$TL_payments_paymentReceipt paymentReceipt;
    private boolean paymentStatusSent;
    private PaymentsClient paymentsClient;
    private HashMap<String, String> phoneFormatMap;
    private ArrayList<TLRPC$TL_labeledPrice> prices;
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
    private TLRPC$TL_payments_validateRequestedInfo validateRequest;
    private boolean waitingForEmail;
    private WebView webView;
    private String webViewUrl;
    private boolean webviewLoading;
    private static final List<String> WEBVIEW_PROTOCOLS = Arrays.asList("http", "https");
    private static final List<String> BLACKLISTED_PROTOCOLS = Collections.singletonList("tg");

    /* loaded from: classes3.dex */
    public enum InvoiceStatus {
        PAID,
        CANCELLED,
        PENDING,
        FAILED
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public interface PaymentFormActivityDelegate {

        /* loaded from: classes3.dex */
        public final /* synthetic */ class -CC {
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

    /* loaded from: classes3.dex */
    public interface PaymentFormCallback {
        void onInvoiceStatusChanged(InvoiceStatus invoiceStatus);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createView$10(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$createView$26(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$sendForm$52(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class TelegramWebviewProxy {
        private TelegramWebviewProxy() {
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
    }

    /* loaded from: classes3.dex */
    public class LinkSpan extends ClickableSpan {
        public LinkSpan() {
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(View view) {
            PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
            paymentFormActivity.presentFragment(new TwoStepVerificationSetupActivity(6, paymentFormActivity.currentPassword));
        }
    }

    public PaymentFormActivity(TLRPC$TL_payments_paymentReceipt tLRPC$TL_payments_paymentReceipt) {
        this.countriesArray = new ArrayList<>();
        this.countriesMap = new HashMap<>();
        this.codesMap = new HashMap<>();
        this.phoneFormatMap = new HashMap<>();
        this.swipeBackEnabled = true;
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList<>();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.shiftDp = -4.5f;
        this.emailCodeLength = 6;
        this.currentStep = 5;
        TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm = new TLRPC$TL_payments_paymentForm();
        this.paymentForm = tLRPC$TL_payments_paymentForm;
        this.paymentReceipt = tLRPC$TL_payments_paymentReceipt;
        tLRPC$TL_payments_paymentForm.bot_id = tLRPC$TL_payments_paymentReceipt.bot_id;
        tLRPC$TL_payments_paymentForm.invoice = tLRPC$TL_payments_paymentReceipt.invoice;
        tLRPC$TL_payments_paymentForm.provider_id = tLRPC$TL_payments_paymentReceipt.provider_id;
        tLRPC$TL_payments_paymentForm.users = tLRPC$TL_payments_paymentReceipt.users;
        this.shippingOption = tLRPC$TL_payments_paymentReceipt.shipping;
        long j = tLRPC$TL_payments_paymentReceipt.tip_amount;
        if (j != 0) {
            this.tipAmount = Long.valueOf(j);
        }
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(tLRPC$TL_payments_paymentReceipt.bot_id));
        this.botUser = user;
        if (user != null) {
            this.currentBotName = user.first_name;
        } else {
            this.currentBotName = "";
        }
        this.currentItemName = tLRPC$TL_payments_paymentReceipt.title;
        if (tLRPC$TL_payments_paymentReceipt.info != null) {
            this.validateRequest = new TLRPC$TL_payments_validateRequestedInfo();
            if (this.messageObject != null) {
                TLRPC$TL_inputInvoiceMessage tLRPC$TL_inputInvoiceMessage = new TLRPC$TL_inputInvoiceMessage();
                tLRPC$TL_inputInvoiceMessage.peer = getMessagesController().getInputPeer(tLRPC$TL_payments_paymentReceipt.bot_id);
                this.validateRequest.invoice = tLRPC$TL_inputInvoiceMessage;
            } else {
                TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
                tLRPC$TL_inputInvoiceSlug.slug = this.invoiceSlug;
                this.validateRequest.invoice = tLRPC$TL_inputInvoiceSlug;
            }
            this.validateRequest.info = tLRPC$TL_payments_paymentReceipt.info;
        }
        this.cardName = tLRPC$TL_payments_paymentReceipt.credentials_title;
    }

    public PaymentFormActivity(TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm, String str, BaseFragment baseFragment) {
        this(tLRPC$TL_payments_paymentForm, null, str, baseFragment);
    }

    public PaymentFormActivity(TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm, TLRPC$TL_inputInvoicePremiumGiftCode tLRPC$TL_inputInvoicePremiumGiftCode, BaseFragment baseFragment) {
        this(tLRPC$TL_inputInvoicePremiumGiftCode, tLRPC$TL_payments_paymentForm, null, null, 4, null, null, null, null, null, null, false, null, baseFragment);
        this.isCheckoutPreview = true;
    }

    public PaymentFormActivity(TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm, MessageObject messageObject, BaseFragment baseFragment) {
        this(tLRPC$TL_payments_paymentForm, messageObject, null, baseFragment);
    }

    public PaymentFormActivity(TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm, MessageObject messageObject, String str, BaseFragment baseFragment) {
        this.countriesArray = new ArrayList<>();
        this.countriesMap = new HashMap<>();
        this.codesMap = new HashMap<>();
        this.phoneFormatMap = new HashMap<>();
        this.swipeBackEnabled = true;
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList<>();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.shiftDp = -4.5f;
        this.emailCodeLength = 6;
        this.isCheckoutPreview = true;
        init(null, tLRPC$TL_payments_paymentForm, messageObject, str, 4, null, null, null, null, null, null, false, null, baseFragment);
    }

    private PaymentFormActivity(TLRPC$TL_inputInvoicePremiumGiftCode tLRPC$TL_inputInvoicePremiumGiftCode, TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm, MessageObject messageObject, String str, int i, TLRPC$TL_payments_validatedRequestedInfo tLRPC$TL_payments_validatedRequestedInfo, TLRPC$TL_shippingOption tLRPC$TL_shippingOption, Long l, String str2, String str3, TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, BaseFragment baseFragment) {
        this.countriesArray = new ArrayList<>();
        this.countriesMap = new HashMap<>();
        this.codesMap = new HashMap<>();
        this.phoneFormatMap = new HashMap<>();
        this.swipeBackEnabled = true;
        this.headerCell = new HeaderCell[3];
        this.dividers = new ArrayList<>();
        this.sectionCell = new ShadowSectionCell[3];
        this.bottomCell = new TextInfoPrivacyCell[3];
        this.settingsCell = new TextSettingsCell[2];
        this.detailSettingsCell = new TextDetailSettingsCell[7];
        this.shiftDp = -4.5f;
        this.emailCodeLength = 6;
        init(tLRPC$TL_inputInvoicePremiumGiftCode, tLRPC$TL_payments_paymentForm, messageObject, str, i, tLRPC$TL_payments_validatedRequestedInfo, tLRPC$TL_shippingOption, l, str2, str3, tLRPC$TL_payments_validateRequestedInfo, z, tLRPC$TL_inputPaymentCredentialsGooglePay, baseFragment);
    }

    public void setPaymentFormCallback(PaymentFormCallback paymentFormCallback) {
        this.paymentFormCallback = paymentFormCallback;
    }

    private void setCurrentPassword(TLRPC$account_Password tLRPC$account_Password) {
        if (tLRPC$account_Password != null && tLRPC$account_Password.has_password) {
            if (getParentActivity() == null) {
                return;
            }
            goToNextStep();
            return;
        }
        this.currentPassword = tLRPC$account_Password;
        this.waitingForEmail = !TextUtils.isEmpty(tLRPC$account_Password.email_unconfirmed_pattern);
        updatePasswordFields();
    }

    private void setDelegate(PaymentFormActivityDelegate paymentFormActivityDelegate) {
        this.delegate = paymentFormActivityDelegate;
    }

    public void setResourcesProvider(Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Theme.ResourcesProvider getResourceProvider() {
        return this.resourcesProvider;
    }

    private void init(TLRPC$TL_inputInvoicePremiumGiftCode tLRPC$TL_inputInvoicePremiumGiftCode, TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm, MessageObject messageObject, String str, int i, TLRPC$TL_payments_validatedRequestedInfo tLRPC$TL_payments_validatedRequestedInfo, TLRPC$TL_shippingOption tLRPC$TL_shippingOption, Long l, String str2, String str3, TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, BaseFragment baseFragment) {
        this.currentStep = i;
        this.parentFragment = baseFragment;
        this.paymentJson = str2;
        this.googlePayCredentials = tLRPC$TL_inputPaymentCredentialsGooglePay;
        this.requestedInfo = tLRPC$TL_payments_validatedRequestedInfo;
        this.paymentForm = tLRPC$TL_payments_paymentForm;
        this.shippingOption = tLRPC$TL_shippingOption;
        this.tipAmount = l;
        this.messageObject = messageObject;
        this.invoiceSlug = str;
        this.invoicePremiumGiftCode = tLRPC$TL_inputInvoicePremiumGiftCode;
        this.saveCardInfo = z;
        this.isWebView = ("stripe".equals(tLRPC$TL_payments_paymentForm.native_provider) || "smartglocal".equals(this.paymentForm.native_provider)) ? false : true;
        TLRPC$User user = getMessagesController().getUser(Long.valueOf(tLRPC$TL_payments_paymentForm.bot_id));
        this.botUser = user;
        if (user != null) {
            this.currentBotName = user.first_name;
        } else {
            this.currentBotName = "";
        }
        this.currentItemName = tLRPC$TL_payments_paymentForm.title;
        this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
        this.saveShippingInfo = true;
        if (z || this.currentStep == 4) {
            this.saveCardInfo = z;
        } else {
            this.saveCardInfo = !this.paymentForm.saved_credentials.isEmpty();
        }
        if (str3 == null) {
            if (this.paymentForm.saved_credentials.isEmpty()) {
                return;
            }
            TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard = this.paymentForm.saved_credentials.get(0);
            this.savedCredentialsCard = tLRPC$TL_paymentSavedCredentialsCard;
            this.cardName = tLRPC$TL_paymentSavedCredentialsCard.title;
            return;
        }
        this.cardName = str3;
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

    /* JADX WARN: Code restructure failed: missing block: B:465:0x0fa7, code lost:
        if (r10.email_requested == false) goto L467;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0314, code lost:
        if (r12.email_requested == false) goto L166;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:606:0x1970  */
    /* JADX WARN: Removed duplicated region for block: B:610:0x1988  */
    /* JADX WARN: Removed duplicated region for block: B:616:0x19b6  */
    /* JADX WARN: Removed duplicated region for block: B:629:0x19f4  */
    /* JADX WARN: Removed duplicated region for block: B:632:0x19fc  */
    /* JADX WARN: Removed duplicated region for block: B:633:0x19fe  */
    /* JADX WARN: Removed duplicated region for block: B:643:0x1a15  */
    /* JADX WARN: Removed duplicated region for block: B:644:0x1a18  */
    /* JADX WARN: Removed duplicated region for block: B:647:0x1a3c  */
    /* JADX WARN: Removed duplicated region for block: B:653:0x1a8d  */
    /* JADX WARN: Removed duplicated region for block: B:659:0x1ade  */
    /* JADX WARN: Removed duplicated region for block: B:665:0x1b2d  */
    /* JADX WARN: Removed duplicated region for block: B:671:0x1b62  */
    /* JADX WARN: Removed duplicated region for block: B:675:0x1b9f  */
    /* JADX WARN: Type inference failed for: r0v92, types: [org.telegram.ui.Cells.RecurrentPaymentsAcceptCell] */
    /* JADX WARN: Type inference failed for: r10v103, types: [android.widget.LinearLayout] */
    /* JADX WARN: Type inference failed for: r10v13, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r10v138, types: [org.telegram.ui.Components.EditTextBoldCursor[]] */
    /* JADX WARN: Type inference failed for: r10v139, types: [android.widget.EditText] */
    /* JADX WARN: Type inference failed for: r10v144, types: [org.telegram.ui.Components.EditTextBoldCursor[]] */
    /* JADX WARN: Type inference failed for: r10v145, types: [android.widget.EditText] */
    /* JADX WARN: Type inference failed for: r10v172, types: [org.telegram.ui.Components.EditTextBoldCursor[]] */
    /* JADX WARN: Type inference failed for: r10v173, types: [android.widget.EditText] */
    /* JADX WARN: Type inference failed for: r10v238, types: [org.telegram.ui.Components.EditTextBoldCursor[]] */
    /* JADX WARN: Type inference failed for: r10v239, types: [android.widget.EditText] */
    /* JADX WARN: Type inference failed for: r10v241, types: [android.widget.LinearLayout] */
    /* JADX WARN: Type inference failed for: r11v4 */
    /* JADX WARN: Type inference failed for: r11v5, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r11v8 */
    /* JADX WARN: Type inference failed for: r1v106, types: [org.telegram.ui.Cells.TextDetailSettingsCell[]] */
    /* JADX WARN: Type inference failed for: r1v107, types: [android.widget.FrameLayout] */
    /* JADX WARN: Type inference failed for: r1v135, types: [org.telegram.ui.PaymentFormActivity$BottomFrameLayout] */
    /* JADX WARN: Type inference failed for: r1v137, types: [org.telegram.ui.ActionBar.ActionBarMenuItem, android.widget.FrameLayout] */
    /* JADX WARN: Type inference failed for: r1v153, types: [android.text.SpannableStringBuilder, java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r1v76, types: [org.telegram.ui.Cells.TextDetailSettingsCell[]] */
    /* JADX WARN: Type inference failed for: r1v77, types: [org.telegram.ui.Cells.TextDetailSettingsCell] */
    /* JADX WARN: Type inference failed for: r1v79, types: [org.telegram.ui.Cells.TextDetailSettingsCell[]] */
    /* JADX WARN: Type inference failed for: r1v80, types: [android.widget.FrameLayout] */
    /* JADX WARN: Type inference failed for: r1v88, types: [org.telegram.ui.Cells.TextDetailSettingsCell[]] */
    /* JADX WARN: Type inference failed for: r1v89, types: [android.widget.FrameLayout] */
    /* JADX WARN: Type inference failed for: r1v97, types: [org.telegram.ui.Cells.TextDetailSettingsCell[]] */
    /* JADX WARN: Type inference failed for: r1v98, types: [android.widget.FrameLayout] */
    /* JADX WARN: Type inference failed for: r2v44, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v229, types: [android.widget.FrameLayout, android.view.View, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r3v233, types: [android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r3v234, types: [android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r3v271, types: [android.widget.LinearLayout, android.view.View, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v218, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r5v219 */
    /* JADX WARN: Type inference failed for: r6v8, types: [java.lang.CharSequence, android.text.SpannableString, java.lang.Object] */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(final Context context) {
        String str;
        FrameLayout frameLayout;
        ?? r11;
        char c;
        int i;
        int i2;
        TLRPC$User tLRPC$User;
        final String str2;
        TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo;
        TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo;
        TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm;
        int i3;
        TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm2;
        TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo2;
        String str3;
        final long longValue;
        boolean z;
        int i4;
        TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo3;
        String str4;
        String str5;
        TelephonyManager telephonyManager;
        ?? frameLayout2;
        HashMap hashMap;
        TLRPC$TL_postAddress tLRPC$TL_postAddress;
        TLRPC$TL_postAddress tLRPC$TL_postAddress2;
        TLRPC$TL_postAddress tLRPC$TL_postAddress3;
        TLRPC$TL_postAddress tLRPC$TL_postAddress4;
        TLRPC$TL_postAddress tLRPC$TL_postAddress5;
        TLRPC$TL_postAddress tLRPC$TL_postAddress6;
        String str6;
        String str7;
        BufferedReader bufferedReader;
        switch (this.currentStep) {
            case 0:
                this.actionBar.setTitle(LocaleController.getString("PaymentShippingInfo", R.string.PaymentShippingInfo));
                break;
            case 1:
                this.actionBar.setTitle(LocaleController.getString("PaymentShippingMethod", R.string.PaymentShippingMethod));
                break;
            case 2:
                this.actionBar.setTitle(LocaleController.getString("PaymentCardInfo", R.string.PaymentCardInfo));
                break;
            case 3:
                this.actionBar.setTitle(LocaleController.getString("PaymentCardInfo", R.string.PaymentCardInfo));
                break;
            case 4:
                if (this.paymentForm.invoice.test) {
                    this.actionBar.setTitle("Test " + LocaleController.getString("PaymentCheckout", R.string.PaymentCheckout));
                    break;
                } else {
                    this.actionBar.setTitle(LocaleController.getString("PaymentCheckout", R.string.PaymentCheckout));
                    break;
                }
            case 5:
                if (this.paymentForm.invoice.test) {
                    this.actionBar.setTitle("Test " + LocaleController.getString("PaymentReceipt", R.string.PaymentReceipt));
                    break;
                } else {
                    this.actionBar.setTitle(LocaleController.getString("PaymentReceipt", R.string.PaymentReceipt));
                    break;
                }
            case 6:
                this.actionBar.setTitle(LocaleController.getString("PaymentPassword", R.string.PaymentPassword));
                break;
        }
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(true);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.PaymentFormActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i5) {
                if (i5 == -1) {
                    if (PaymentFormActivity.this.donePressed) {
                        return;
                    }
                    PaymentFormActivity.this.finishFragment();
                } else if (i5 != 1 || PaymentFormActivity.this.donePressed) {
                } else {
                    if (PaymentFormActivity.this.currentStep != 3) {
                        AndroidUtilities.hideKeyboard(PaymentFormActivity.this.getParentActivity().getCurrentFocus());
                    }
                    int i6 = PaymentFormActivity.this.currentStep;
                    if (i6 == 0) {
                        PaymentFormActivity.this.setDonePressed(true);
                        PaymentFormActivity.this.sendForm();
                        return;
                    }
                    int i7 = 0;
                    if (i6 == 1) {
                        while (true) {
                            if (i7 >= PaymentFormActivity.this.radioCells.length) {
                                break;
                            } else if (PaymentFormActivity.this.radioCells[i7].isChecked()) {
                                PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                                paymentFormActivity.shippingOption = paymentFormActivity.requestedInfo.shipping_options.get(i7);
                                break;
                            } else {
                                i7++;
                            }
                        }
                        PaymentFormActivity.this.goToNextStep();
                    } else if (i6 == 2) {
                        PaymentFormActivity.this.sendCardData();
                    } else if (i6 == 3) {
                        PaymentFormActivity.this.checkPassword();
                    } else if (i6 != 6) {
                    } else {
                        PaymentFormActivity.this.sendSavePassword(false);
                    }
                }
            }
        });
        ActionBarMenu createMenu = this.actionBar.createMenu();
        int i5 = this.currentStep;
        int i6 = 3;
        if (i5 == 0 || i5 == 1 || i5 == 2 || i5 == 3 || i5 == 4 || i5 == 6) {
            this.doneItem = createMenu.addItemWithWidth(1, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", R.string.Done));
            ContextProgressView contextProgressView = new ContextProgressView(context, 1);
            this.progressView = contextProgressView;
            contextProgressView.setAlpha(0.0f);
            this.progressView.setScaleX(0.1f);
            this.progressView.setScaleY(0.1f);
            this.progressView.setVisibility(4);
            this.doneItem.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0f));
        }
        FrameLayout frameLayout3 = new FrameLayout(context);
        this.fragmentView = frameLayout3;
        FrameLayout frameLayout4 = frameLayout3;
        frameLayout3.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundGray));
        ScrollView scrollView = new ScrollView(context);
        this.scrollView = scrollView;
        scrollView.setFillViewport(true);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, getThemedColor(Theme.key_actionBarDefault));
        frameLayout4.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.currentStep == 4 ? 48.0f : 0.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.linearLayout2 = linearLayout;
        linearLayout.setOrientation(1);
        ?? r5 = 0;
        this.linearLayout2.setClipChildren(false);
        this.scrollView.addView(this.linearLayout2, new FrameLayout.LayoutParams(-1, -2));
        int i7 = this.currentStep;
        if (i7 == 0) {
            HashMap hashMap2 = new HashMap();
            HashMap hashMap3 = new HashMap();
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
                    hashMap3.put(split[1], split[2]);
                    if (split.length > 3) {
                        this.phoneFormatMap.put(split[0], split[3]);
                    }
                    hashMap2.put(split[1], split[2]);
                } else {
                    bufferedReader.close();
                    Collections.sort(this.countriesArray, BoostRepository$$ExternalSyntheticLambda33.INSTANCE);
                    this.inputFields = new EditTextBoldCursor[10];
                    int i8 = 0;
                    while (i8 < 10) {
                        if (i8 == 0) {
                            this.headerCell[r5] = new HeaderCell(context, this.resourcesProvider);
                            this.headerCell[r5].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            this.headerCell[r5].setText(LocaleController.getString("PaymentShippingAddress", R.string.PaymentShippingAddress));
                            this.linearLayout2.addView(this.headerCell[r5], LayoutHelper.createLinear(-1, -2));
                        } else if (i8 == 6) {
                            this.sectionCell[r5] = new ShadowSectionCell(context, this.resourcesProvider);
                            this.linearLayout2.addView(this.sectionCell[r5], LayoutHelper.createLinear(-1, -2));
                            this.headerCell[1] = new HeaderCell(context, this.resourcesProvider);
                            this.headerCell[1].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            this.headerCell[1].setText(LocaleController.getString("PaymentShippingReceiver", R.string.PaymentShippingReceiver));
                            this.linearLayout2.addView(this.headerCell[1], LayoutHelper.createLinear(-1, -2));
                        }
                        if (i8 == 8) {
                            frameLayout2 = new LinearLayout(context);
                            frameLayout2.setClipChildren(r5);
                            frameLayout2.setOrientation(r5);
                            this.linearLayout2.addView(frameLayout2, LayoutHelper.createLinear(-1, 50));
                            frameLayout2.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        } else if (i8 == 9) {
                            frameLayout2 = (ViewGroup) this.inputFields[8].getParent();
                        } else {
                            frameLayout2 = new FrameLayout(context);
                            frameLayout2.setClipChildren(r5);
                            this.linearLayout2.addView(frameLayout2, LayoutHelper.createLinear(-1, 50));
                            int i9 = Theme.key_windowBackgroundWhite;
                            frameLayout2.setBackgroundColor(getThemedColor(i9));
                            boolean z2 = i8 != 5;
                            if (z2) {
                                if (i8 != 7 || this.paymentForm.invoice.phone_requested) {
                                    if (i8 == 6) {
                                        TLRPC$TL_invoice tLRPC$TL_invoice = this.paymentForm.invoice;
                                        if (!tLRPC$TL_invoice.phone_requested) {
                                        }
                                    }
                                }
                                z2 = false;
                            }
                            if (z2) {
                                View view = new View(this, context) { // from class: org.telegram.ui.PaymentFormActivity.2
                                    @Override // android.view.View
                                    protected void onDraw(Canvas canvas) {
                                        canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                                    }
                                };
                                view.setBackgroundColor(getThemedColor(i9));
                                this.dividers.add(view);
                                frameLayout2.addView(view, new FrameLayout.LayoutParams(-1, 1, 83));
                            }
                        }
                        if (i8 == 9) {
                            this.inputFields[i8] = new HintEditText(context);
                        } else {
                            this.inputFields[i8] = new EditTextBoldCursor(context);
                        }
                        this.inputFields[i8].setTag(Integer.valueOf(i8));
                        this.inputFields[i8].setTextSize(1, 16.0f);
                        this.inputFields[i8].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                        EditTextBoldCursor editTextBoldCursor = this.inputFields[i8];
                        int i10 = Theme.key_windowBackgroundWhiteBlackText;
                        editTextBoldCursor.setTextColor(getThemedColor(i10));
                        this.inputFields[i8].setBackgroundDrawable(null);
                        this.inputFields[i8].setCursorColor(getThemedColor(i10));
                        this.inputFields[i8].setCursorSize(AndroidUtilities.dp(20.0f));
                        this.inputFields[i8].setCursorWidth(1.5f);
                        if (i8 == 4) {
                            this.inputFields[i8].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda22
                                @Override // android.view.View.OnTouchListener
                                public final boolean onTouch(View view2, MotionEvent motionEvent) {
                                    boolean lambda$createView$1;
                                    lambda$createView$1 = PaymentFormActivity.this.lambda$createView$1(view2, motionEvent);
                                    return lambda$createView$1;
                                }
                            });
                            this.inputFields[i8].setInputType(r5);
                        }
                        if (i8 == 9 || i8 == 8) {
                            this.inputFields[i8].setInputType(i6);
                        } else if (i8 == 7) {
                            this.inputFields[i8].setInputType(1);
                        } else {
                            this.inputFields[i8].setInputType(16385);
                        }
                        this.inputFields[i8].setImeOptions(268435461);
                        switch (i8) {
                            case 0:
                                this.inputFields[i8].setHint(LocaleController.getString("PaymentShippingAddress1Placeholder", R.string.PaymentShippingAddress1Placeholder));
                                TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo4 = this.paymentForm.saved_info;
                                if (tLRPC$TL_paymentRequestedInfo4 != null && (tLRPC$TL_postAddress = tLRPC$TL_paymentRequestedInfo4.shipping_address) != null) {
                                    this.inputFields[i8].setText(tLRPC$TL_postAddress.street_line1);
                                    break;
                                }
                                break;
                            case 1:
                                this.inputFields[i8].setHint(LocaleController.getString("PaymentShippingAddress2Placeholder", R.string.PaymentShippingAddress2Placeholder));
                                TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo5 = this.paymentForm.saved_info;
                                if (tLRPC$TL_paymentRequestedInfo5 != null && (tLRPC$TL_postAddress2 = tLRPC$TL_paymentRequestedInfo5.shipping_address) != null) {
                                    this.inputFields[i8].setText(tLRPC$TL_postAddress2.street_line2);
                                    break;
                                }
                                break;
                            case 2:
                                this.inputFields[i8].setHint(LocaleController.getString("PaymentShippingCityPlaceholder", R.string.PaymentShippingCityPlaceholder));
                                TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo6 = this.paymentForm.saved_info;
                                if (tLRPC$TL_paymentRequestedInfo6 != null && (tLRPC$TL_postAddress3 = tLRPC$TL_paymentRequestedInfo6.shipping_address) != null) {
                                    this.inputFields[i8].setText(tLRPC$TL_postAddress3.city);
                                    break;
                                }
                                break;
                            case 3:
                                this.inputFields[i8].setHint(LocaleController.getString("PaymentShippingStatePlaceholder", R.string.PaymentShippingStatePlaceholder));
                                TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo7 = this.paymentForm.saved_info;
                                if (tLRPC$TL_paymentRequestedInfo7 != null && (tLRPC$TL_postAddress4 = tLRPC$TL_paymentRequestedInfo7.shipping_address) != null) {
                                    this.inputFields[i8].setText(tLRPC$TL_postAddress4.state);
                                    break;
                                }
                                break;
                            case 4:
                                this.inputFields[i8].setHint(LocaleController.getString("PaymentShippingCountry", R.string.PaymentShippingCountry));
                                TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo8 = this.paymentForm.saved_info;
                                if (tLRPC$TL_paymentRequestedInfo8 != null && (tLRPC$TL_postAddress5 = tLRPC$TL_paymentRequestedInfo8.shipping_address) != null) {
                                    String str8 = (String) hashMap3.get(tLRPC$TL_postAddress5.country_iso2);
                                    String str9 = this.paymentForm.saved_info.shipping_address.country_iso2;
                                    this.countryName = str9;
                                    EditTextBoldCursor editTextBoldCursor2 = this.inputFields[i8];
                                    if (str8 == null) {
                                        str8 = str9;
                                    }
                                    editTextBoldCursor2.setText(str8);
                                    break;
                                }
                                break;
                            case 5:
                                this.inputFields[i8].setHint(LocaleController.getString("PaymentShippingZipPlaceholder", R.string.PaymentShippingZipPlaceholder));
                                TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo9 = this.paymentForm.saved_info;
                                if (tLRPC$TL_paymentRequestedInfo9 != null && (tLRPC$TL_postAddress6 = tLRPC$TL_paymentRequestedInfo9.shipping_address) != null) {
                                    this.inputFields[i8].setText(tLRPC$TL_postAddress6.post_code);
                                    break;
                                }
                                break;
                            case 6:
                                this.inputFields[i8].setHint(LocaleController.getString("PaymentShippingName", R.string.PaymentShippingName));
                                TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo10 = this.paymentForm.saved_info;
                                if (tLRPC$TL_paymentRequestedInfo10 != null && (str6 = tLRPC$TL_paymentRequestedInfo10.name) != null) {
                                    this.inputFields[i8].setText(str6);
                                    break;
                                }
                                break;
                            case 7:
                                this.inputFields[i8].setHint(LocaleController.getString("PaymentShippingEmailPlaceholder", R.string.PaymentShippingEmailPlaceholder));
                                TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo11 = this.paymentForm.saved_info;
                                if (tLRPC$TL_paymentRequestedInfo11 != null && (str7 = tLRPC$TL_paymentRequestedInfo11.email) != null) {
                                    this.inputFields[i8].setText(str7);
                                    break;
                                }
                                break;
                        }
                        EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                        editTextBoldCursorArr[i8].setSelection(editTextBoldCursorArr[i8].length());
                        if (i8 == 8) {
                            TextView textView = new TextView(context);
                            this.textView = textView;
                            textView.setText("+");
                            this.textView.setTextColor(getThemedColor(i10));
                            this.textView.setTextSize(1, 16.0f);
                            frameLayout2.addView(this.textView, LayoutHelper.createLinear(-2, -2, 21.0f, 12.0f, 0.0f, 6.0f));
                            this.inputFields[i8].setPadding(AndroidUtilities.dp(10.0f), r5, r5, r5);
                            this.inputFields[i8].setGravity(19);
                            InputFilter[] inputFilterArr = new InputFilter[1];
                            inputFilterArr[r5] = new InputFilter.LengthFilter(5);
                            this.inputFields[i8].setFilters(inputFilterArr);
                            frameLayout2.addView(this.inputFields[i8], LayoutHelper.createLinear(55, -2, 0.0f, 12.0f, 21.0f, 6.0f));
                            this.inputFields[i8].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.3
                                @Override // android.text.TextWatcher
                                public void beforeTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                                }

                                @Override // android.text.TextWatcher
                                public void onTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                                }

                                @Override // android.text.TextWatcher
                                public void afterTextChanged(Editable editable) {
                                    String str10;
                                    boolean z3;
                                    String str11;
                                    if (PaymentFormActivity.this.ignoreOnTextChange) {
                                        return;
                                    }
                                    boolean z4 = true;
                                    PaymentFormActivity.this.ignoreOnTextChange = true;
                                    String stripExceptNumbers = PhoneFormat.stripExceptNumbers(PaymentFormActivity.this.inputFields[8].getText().toString());
                                    PaymentFormActivity.this.inputFields[8].setText(stripExceptNumbers);
                                    HintEditText hintEditText = (HintEditText) PaymentFormActivity.this.inputFields[9];
                                    if (stripExceptNumbers.length() == 0) {
                                        hintEditText.setHintText((String) null);
                                        hintEditText.setHint(LocaleController.getString("PaymentShippingPhoneNumber", R.string.PaymentShippingPhoneNumber));
                                    } else {
                                        int i11 = 4;
                                        if (stripExceptNumbers.length() > 4) {
                                            while (true) {
                                                if (i11 < 1) {
                                                    str10 = null;
                                                    z3 = false;
                                                    break;
                                                }
                                                String substring = stripExceptNumbers.substring(0, i11);
                                                if (((String) PaymentFormActivity.this.codesMap.get(substring)) != null) {
                                                    String str12 = stripExceptNumbers.substring(i11) + PaymentFormActivity.this.inputFields[9].getText().toString();
                                                    PaymentFormActivity.this.inputFields[8].setText(substring);
                                                    z3 = true;
                                                    str10 = str12;
                                                    stripExceptNumbers = substring;
                                                    break;
                                                }
                                                i11--;
                                            }
                                            if (!z3) {
                                                str10 = stripExceptNumbers.substring(1) + PaymentFormActivity.this.inputFields[9].getText().toString();
                                                EditTextBoldCursor editTextBoldCursor3 = PaymentFormActivity.this.inputFields[8];
                                                stripExceptNumbers = stripExceptNumbers.substring(0, 1);
                                                editTextBoldCursor3.setText(stripExceptNumbers);
                                            }
                                        } else {
                                            str10 = null;
                                            z3 = false;
                                        }
                                        String str13 = (String) PaymentFormActivity.this.codesMap.get(stripExceptNumbers);
                                        if (str13 == null || PaymentFormActivity.this.countriesArray.indexOf(str13) == -1 || (str11 = (String) PaymentFormActivity.this.phoneFormatMap.get(stripExceptNumbers)) == null) {
                                            z4 = false;
                                        } else {
                                            hintEditText.setHintText(str11.replace('X', (char) 8211));
                                            hintEditText.setHint((CharSequence) null);
                                        }
                                        if (!z4) {
                                            hintEditText.setHintText((String) null);
                                            hintEditText.setHint(LocaleController.getString("PaymentShippingPhoneNumber", R.string.PaymentShippingPhoneNumber));
                                        }
                                        if (!z3) {
                                            PaymentFormActivity.this.inputFields[8].setSelection(PaymentFormActivity.this.inputFields[8].getText().length());
                                        }
                                        if (str10 != null) {
                                            hintEditText.requestFocus();
                                            hintEditText.setText(str10);
                                            hintEditText.setSelection(hintEditText.length());
                                        }
                                    }
                                    PaymentFormActivity.this.ignoreOnTextChange = false;
                                }
                            });
                        } else if (i8 == 9) {
                            this.inputFields[i8].setPadding(r5, r5, r5, r5);
                            this.inputFields[i8].setGravity(19);
                            frameLayout2.addView(this.inputFields[i8], LayoutHelper.createLinear(-1, -2, 0.0f, 12.0f, 21.0f, 6.0f));
                            this.inputFields[i8].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.4
                                private int actionPosition;
                                private int characterAction = -1;

                                @Override // android.text.TextWatcher
                                public void onTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                                }

                                @Override // android.text.TextWatcher
                                public void beforeTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                                    if (i12 == 0 && i13 == 1) {
                                        this.characterAction = 1;
                                    } else if (i12 == 1 && i13 == 0) {
                                        if (charSequence.charAt(i11) == ' ' && i11 > 0) {
                                            this.characterAction = 3;
                                            this.actionPosition = i11 - 1;
                                            return;
                                        }
                                        this.characterAction = 2;
                                    } else {
                                        this.characterAction = -1;
                                    }
                                }

                                @Override // android.text.TextWatcher
                                public void afterTextChanged(Editable editable) {
                                    int i11;
                                    int i12;
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
                                    StringBuilder sb = new StringBuilder(obj.length());
                                    int i13 = 0;
                                    while (i13 < obj.length()) {
                                        int i14 = i13 + 1;
                                        String substring = obj.substring(i13, i14);
                                        if ("0123456789".contains(substring)) {
                                            sb.append(substring);
                                        }
                                        i13 = i14;
                                    }
                                    PaymentFormActivity.this.ignoreOnPhoneChange = true;
                                    String hintText = hintEditText.getHintText();
                                    if (hintText != null) {
                                        int i15 = 0;
                                        while (true) {
                                            if (i15 >= sb.length()) {
                                                break;
                                            } else if (i15 < hintText.length()) {
                                                if (hintText.charAt(i15) == ' ') {
                                                    sb.insert(i15, ' ');
                                                    i15++;
                                                    if (selectionStart == i15 && (i12 = this.characterAction) != 2 && i12 != 3) {
                                                        selectionStart++;
                                                    }
                                                }
                                                i15++;
                                            } else {
                                                sb.insert(i15, ' ');
                                                if (selectionStart == i15 + 1 && (i11 = this.characterAction) != 2 && i11 != 3) {
                                                    selectionStart++;
                                                }
                                            }
                                        }
                                    }
                                    hintEditText.setText(sb);
                                    if (selectionStart >= 0) {
                                        hintEditText.setSelection(Math.min(selectionStart, hintEditText.length()));
                                    }
                                    hintEditText.onTextChange();
                                    PaymentFormActivity.this.ignoreOnPhoneChange = false;
                                }
                            });
                        } else {
                            this.inputFields[i8].setPadding(r5, r5, r5, AndroidUtilities.dp(6.0f));
                            this.inputFields[i8].setGravity(LocaleController.isRTL ? 5 : 3);
                            frameLayout2.addView(this.inputFields[i8], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                        }
                        this.inputFields[i8].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda25
                            @Override // android.widget.TextView.OnEditorActionListener
                            public final boolean onEditorAction(TextView textView2, int i11, KeyEvent keyEvent) {
                                boolean lambda$createView$2;
                                lambda$createView$2 = PaymentFormActivity.this.lambda$createView$2(textView2, i11, keyEvent);
                                return lambda$createView$2;
                            }
                        });
                        if (i8 == 9) {
                            TLRPC$TL_invoice tLRPC$TL_invoice2 = this.paymentForm.invoice;
                            if (tLRPC$TL_invoice2.email_to_provider || tLRPC$TL_invoice2.phone_to_provider) {
                                TLRPC$User tLRPC$User2 = null;
                                int i11 = 0;
                                while (i11 < this.paymentForm.users.size()) {
                                    TLRPC$User tLRPC$User3 = this.paymentForm.users.get(i11);
                                    HashMap hashMap4 = hashMap2;
                                    if (tLRPC$User3.id == this.paymentForm.provider_id) {
                                        tLRPC$User2 = tLRPC$User3;
                                    }
                                    i11++;
                                    hashMap2 = hashMap4;
                                }
                                hashMap = hashMap2;
                                String formatName = tLRPC$User2 != null ? ContactsController.formatName(tLRPC$User2.first_name, tLRPC$User2.last_name) : "";
                                this.bottomCell[1] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                                this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                                this.linearLayout2.addView(this.bottomCell[1], LayoutHelper.createLinear(-1, -2));
                                TLRPC$TL_invoice tLRPC$TL_invoice3 = this.paymentForm.invoice;
                                boolean z3 = tLRPC$TL_invoice3.email_to_provider;
                                if (z3 && tLRPC$TL_invoice3.phone_to_provider) {
                                    this.bottomCell[1].setText(LocaleController.formatString("PaymentPhoneEmailToProvider", R.string.PaymentPhoneEmailToProvider, formatName));
                                } else if (z3) {
                                    this.bottomCell[1].setText(LocaleController.formatString("PaymentEmailToProvider", R.string.PaymentEmailToProvider, formatName));
                                } else {
                                    this.bottomCell[1].setText(LocaleController.formatString("PaymentPhoneToProvider", R.string.PaymentPhoneToProvider, formatName));
                                }
                            } else {
                                this.sectionCell[1] = new ShadowSectionCell(context, this.resourcesProvider);
                                this.linearLayout2.addView(this.sectionCell[1], LayoutHelper.createLinear(-1, -2));
                                hashMap = hashMap2;
                            }
                            TextCheckCell textCheckCell = new TextCheckCell(context, this.resourcesProvider);
                            this.checkCell1 = textCheckCell;
                            textCheckCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                            this.checkCell1.setTextAndCheck(LocaleController.getString("PaymentShippingSave", R.string.PaymentShippingSave), this.saveShippingInfo, false);
                            this.linearLayout2.addView(this.checkCell1, LayoutHelper.createLinear(-1, -2));
                            this.checkCell1.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda9
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view2) {
                                    PaymentFormActivity.this.lambda$createView$3(view2);
                                }
                            });
                            this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                            this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                            this.bottomCell[0].setText(LocaleController.getString("PaymentShippingSaveInfo", R.string.PaymentShippingSaveInfo));
                            this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                        } else {
                            hashMap = hashMap2;
                        }
                        i8++;
                        hashMap2 = hashMap;
                        r5 = 0;
                        i6 = 3;
                    }
                    HashMap hashMap5 = hashMap2;
                    if (this.paymentForm.invoice.name_requested) {
                        i4 = 8;
                    } else {
                        i4 = 8;
                        ((ViewGroup) this.inputFields[6].getParent()).setVisibility(8);
                    }
                    if (!this.paymentForm.invoice.phone_requested) {
                        ((ViewGroup) this.inputFields[i4].getParent()).setVisibility(i4);
                    }
                    if (!this.paymentForm.invoice.email_requested) {
                        ((ViewGroup) this.inputFields[7].getParent()).setVisibility(i4);
                    }
                    TLRPC$TL_invoice tLRPC$TL_invoice4 = this.paymentForm.invoice;
                    if (tLRPC$TL_invoice4.phone_requested) {
                        this.inputFields[9].setImeOptions(268435462);
                    } else if (tLRPC$TL_invoice4.email_requested) {
                        this.inputFields[7].setImeOptions(268435462);
                    } else if (tLRPC$TL_invoice4.name_requested) {
                        this.inputFields[6].setImeOptions(268435462);
                    } else {
                        this.inputFields[5].setImeOptions(268435462);
                    }
                    ShadowSectionCell[] shadowSectionCellArr = this.sectionCell;
                    if (shadowSectionCellArr[1] != null) {
                        ShadowSectionCell shadowSectionCell = shadowSectionCellArr[1];
                        TLRPC$TL_invoice tLRPC$TL_invoice5 = this.paymentForm.invoice;
                        shadowSectionCell.setVisibility((tLRPC$TL_invoice5.name_requested || tLRPC$TL_invoice5.phone_requested || tLRPC$TL_invoice5.email_requested) ? 0 : 8);
                    } else {
                        TextInfoPrivacyCell[] textInfoPrivacyCellArr = this.bottomCell;
                        if (textInfoPrivacyCellArr[1] != null) {
                            TextInfoPrivacyCell textInfoPrivacyCell = textInfoPrivacyCellArr[1];
                            TLRPC$TL_invoice tLRPC$TL_invoice6 = this.paymentForm.invoice;
                            textInfoPrivacyCell.setVisibility((tLRPC$TL_invoice6.name_requested || tLRPC$TL_invoice6.phone_requested || tLRPC$TL_invoice6.email_requested) ? 0 : 8);
                        }
                    }
                    HeaderCell headerCell = this.headerCell[1];
                    TLRPC$TL_invoice tLRPC$TL_invoice7 = this.paymentForm.invoice;
                    headerCell.setVisibility((tLRPC$TL_invoice7.name_requested || tLRPC$TL_invoice7.phone_requested || tLRPC$TL_invoice7.email_requested) ? 0 : 8);
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
                    TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo12 = this.paymentForm.saved_info;
                    if (tLRPC$TL_paymentRequestedInfo12 != null && !TextUtils.isEmpty(tLRPC$TL_paymentRequestedInfo12.phone)) {
                        fillNumber(this.paymentForm.saved_info.phone);
                    } else {
                        fillNumber(null);
                    }
                    if (this.inputFields[8].length() == 0) {
                        TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm3 = this.paymentForm;
                        if (tLRPC$TL_payments_paymentForm3.invoice.phone_requested && ((tLRPC$TL_paymentRequestedInfo3 = tLRPC$TL_payments_paymentForm3.saved_info) == null || TextUtils.isEmpty(tLRPC$TL_paymentRequestedInfo3.phone))) {
                            try {
                                telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
                            } catch (Exception e2) {
                                FileLog.e(e2);
                            }
                            if (telephonyManager != null) {
                                str4 = telephonyManager.getSimCountryIso().toUpperCase();
                                if (str4 != null && (str5 = (String) hashMap5.get(str4)) != null && this.countriesArray.indexOf(str5) != -1) {
                                    this.inputFields[8].setText(this.countriesMap.get(str5));
                                }
                            }
                            str4 = null;
                            if (str4 != null) {
                                this.inputFields[8].setText(this.countriesMap.get(str5));
                            }
                        }
                    }
                }
            }
        } else if (i7 == 2) {
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
                    @Override // android.webkit.WebView, android.view.View
                    public boolean onTouchEvent(MotionEvent motionEvent) {
                        ((ViewGroup) PaymentFormActivity.this.fragmentView).requestDisallowInterceptTouchEvent(true);
                        return super.onTouchEvent(motionEvent);
                    }

                    @Override // android.webkit.WebView, android.widget.AbsoluteLayout, android.view.View
                    protected void onMeasure(int i12, int i13) {
                        super.onMeasure(i12, i13);
                    }
                };
                this.webView = webView;
                webView.getSettings().setJavaScriptEnabled(true);
                this.webView.getSettings().setDomStorageEnabled(true);
                int i12 = Build.VERSION.SDK_INT;
                if (i12 >= 21) {
                    this.webView.getSettings().setMixedContentMode(0);
                    CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
                }
                if (i12 >= 17) {
                    this.webView.addJavascriptInterface(new TelegramWebviewProxy(), "TelegramWebviewProxy");
                }
                this.webView.setWebViewClient(new WebViewClient() { // from class: org.telegram.ui.PaymentFormActivity.6
                    @Override // android.webkit.WebViewClient
                    public void onLoadResource(WebView webView2, String str10) {
                        super.onLoadResource(webView2, str10);
                    }

                    @Override // android.webkit.WebViewClient
                    public boolean shouldOverrideUrlLoading(WebView webView2, String str10) {
                        PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                        paymentFormActivity.shouldNavigateBack = !str10.equals(paymentFormActivity.webViewUrl);
                        return super.shouldOverrideUrlLoading(webView2, str10);
                    }

                    @Override // android.webkit.WebViewClient
                    public void onPageFinished(WebView webView2, String str10) {
                        super.onPageFinished(webView2, str10);
                        PaymentFormActivity.this.webviewLoading = false;
                        PaymentFormActivity.this.showEditDoneProgress(true, false);
                        PaymentFormActivity.this.updateSavePaymentField();
                    }
                });
                this.linearLayout2.addView(this.webView, LayoutHelper.createFrame(-1, -2.0f));
                this.sectionCell[2] = new ShadowSectionCell(context, this.resourcesProvider);
                this.linearLayout2.addView(this.sectionCell[2], LayoutHelper.createLinear(-1, -2));
                TextCheckCell textCheckCell2 = new TextCheckCell(context, this.resourcesProvider);
                this.checkCell1 = textCheckCell2;
                textCheckCell2.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.checkCell1.setTextAndCheck(LocaleController.getString("PaymentCardSavePaymentInformation", R.string.PaymentCardSavePaymentInformation), this.saveCardInfo, false);
                this.linearLayout2.addView(this.checkCell1, LayoutHelper.createLinear(-1, -2));
                this.checkCell1.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda16
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        PaymentFormActivity.this.lambda$createView$4(view2);
                    }
                });
                this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                updateSavePaymentField();
                this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
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
                            this.providerApiKey = jSONObject2.getString("public_token");
                        } else {
                            try {
                                this.providerApiKey = jSONObject2.getString("publishable_key");
                            } catch (Exception unused4) {
                                this.providerApiKey = "";
                            }
                        }
                        this.initGooglePay = !jSONObject2.optBoolean("google_pay_hidden", false);
                    } catch (Exception e4) {
                        FileLog.e(e4);
                    }
                }
                if (this.initGooglePay && ((!TextUtils.isEmpty(this.providerApiKey) && "stripe".equals(this.paymentForm.native_provider)) || this.googlePayParameters != null)) {
                    initGooglePay(context);
                }
                this.inputFields = new EditTextBoldCursor[6];
                int i13 = 0;
                for (int i14 = 6; i13 < i14; i14 = 6) {
                    if (i13 == 0) {
                        this.headerCell[0] = new HeaderCell(context, this.resourcesProvider);
                        this.headerCell[0].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        this.headerCell[0].setText(LocaleController.getString("PaymentCardTitle", R.string.PaymentCardTitle));
                        this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                    } else if (i13 == 4) {
                        this.headerCell[1] = new HeaderCell(context, this.resourcesProvider);
                        this.headerCell[1].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        this.headerCell[1].setText(LocaleController.getString("PaymentBillingAddress", R.string.PaymentBillingAddress));
                        this.linearLayout2.addView(this.headerCell[1], LayoutHelper.createLinear(-1, -2));
                    }
                    boolean z4 = (i13 == 3 || i13 == 5 || (i13 == 4 && !this.need_card_postcode)) ? false : true;
                    FrameLayout frameLayout5 = new FrameLayout(context);
                    frameLayout5.setClipChildren(false);
                    int i15 = Theme.key_windowBackgroundWhite;
                    frameLayout5.setBackgroundColor(getThemedColor(i15));
                    this.linearLayout2.addView(frameLayout5, LayoutHelper.createLinear(-1, 50));
                    this.inputFields[i13] = new EditTextBoldCursor(context);
                    this.inputFields[i13].setTag(Integer.valueOf(i13));
                    this.inputFields[i13].setTextSize(1, 16.0f);
                    this.inputFields[i13].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                    EditTextBoldCursor editTextBoldCursor3 = this.inputFields[i13];
                    int i16 = Theme.key_windowBackgroundWhiteBlackText;
                    editTextBoldCursor3.setTextColor(getThemedColor(i16));
                    this.inputFields[i13].setBackgroundDrawable(null);
                    this.inputFields[i13].setCursorColor(getThemedColor(i16));
                    this.inputFields[i13].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.inputFields[i13].setCursorWidth(1.5f);
                    if (i13 == 3) {
                        this.inputFields[i13].setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                        this.inputFields[i13].setInputType(130);
                        this.inputFields[i13].setTypeface(Typeface.DEFAULT);
                        this.inputFields[i13].setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else if (i13 == 0) {
                        this.inputFields[i13].setInputType(3);
                    } else if (i13 == 4) {
                        this.inputFields[i13].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda23
                            @Override // android.view.View.OnTouchListener
                            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                                boolean lambda$createView$6;
                                lambda$createView$6 = PaymentFormActivity.this.lambda$createView$6(view2, motionEvent);
                                return lambda$createView$6;
                            }
                        });
                        this.inputFields[i13].setInputType(0);
                    } else if (i13 == 1) {
                        this.inputFields[i13].setInputType(16386);
                    } else if (i13 == 2) {
                        this.inputFields[i13].setInputType(4097);
                    } else {
                        this.inputFields[i13].setInputType(16385);
                    }
                    this.inputFields[i13].setImeOptions(268435461);
                    if (i13 == 0) {
                        this.inputFields[i13].setHint(LocaleController.getString("PaymentCardNumber", R.string.PaymentCardNumber));
                    } else if (i13 == 1) {
                        this.inputFields[i13].setHint(LocaleController.getString("PaymentCardExpireDate", R.string.PaymentCardExpireDate));
                    } else if (i13 == 2) {
                        this.inputFields[i13].setHint(LocaleController.getString("PaymentCardName", R.string.PaymentCardName));
                    } else if (i13 == 3) {
                        this.inputFields[i13].setHint(LocaleController.getString("PaymentCardCvv", R.string.PaymentCardCvv));
                    } else if (i13 == 4) {
                        this.inputFields[i13].setHint(LocaleController.getString("PaymentShippingCountry", R.string.PaymentShippingCountry));
                    } else if (i13 == 5) {
                        this.inputFields[i13].setHint(LocaleController.getString("PaymentShippingZipPlaceholder", R.string.PaymentShippingZipPlaceholder));
                    }
                    if (i13 == 0) {
                        this.inputFields[i13].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.7
                            private int actionPosition;
                            public final String[] PREFIXES_15 = {"34", "37"};
                            public final String[] PREFIXES_14 = {"300", "301", "302", "303", "304", "305", "309", "36", "38", "39"};
                            public final String[] PREFIXES_16 = {"2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229", "2200", "2201", "2202", "2203", "2204", "8600", "9860", "223", "224", "225", "226", "227", "228", "229", "23", "24", "25", "26", "270", "271", "2720", "50", "51", "52", "53", "54", "55", "4", "60", "62", "64", "65", "35"};
                            private int characterAction = -1;

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence charSequence, int i17, int i18, int i19) {
                            }

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence charSequence, int i17, int i18, int i19) {
                                if (i18 == 0 && i19 == 1) {
                                    this.characterAction = 1;
                                } else if (i18 == 1 && i19 == 0) {
                                    if (charSequence.charAt(i17) == ' ' && i17 > 0) {
                                        this.characterAction = 3;
                                        this.actionPosition = i17 - 1;
                                        return;
                                    }
                                    this.characterAction = 2;
                                } else {
                                    this.characterAction = -1;
                                }
                            }

                            @Override // android.text.TextWatcher
                            public void afterTextChanged(Editable editable) {
                                boolean z5;
                                int i17;
                                int i18;
                                String[] strArr;
                                int i19;
                                String str10;
                                if (PaymentFormActivity.this.ignoreOnCardChange) {
                                    return;
                                }
                                EditTextBoldCursor editTextBoldCursor4 = PaymentFormActivity.this.inputFields[0];
                                int selectionStart = editTextBoldCursor4.getSelectionStart();
                                String obj = editTextBoldCursor4.getText().toString();
                                if (this.characterAction == 3) {
                                    obj = obj.substring(0, this.actionPosition) + obj.substring(this.actionPosition + 1);
                                    selectionStart--;
                                }
                                StringBuilder sb = new StringBuilder(obj.length());
                                int i20 = 0;
                                while (i20 < obj.length()) {
                                    int i21 = i20 + 1;
                                    String substring = obj.substring(i20, i21);
                                    if ("0123456789".contains(substring)) {
                                        sb.append(substring);
                                    }
                                    i20 = i21;
                                }
                                PaymentFormActivity.this.ignoreOnCardChange = true;
                                String str11 = null;
                                int i22 = 100;
                                if (sb.length() > 0) {
                                    String sb2 = sb.toString();
                                    int i23 = 0;
                                    for (int i24 = 3; i23 < i24; i24 = 3) {
                                        if (i23 == 0) {
                                            strArr = this.PREFIXES_16;
                                            i19 = 16;
                                            str10 = "xxxx xxxx xxxx xxxx";
                                        } else if (i23 == 1) {
                                            strArr = this.PREFIXES_15;
                                            i19 = 15;
                                            str10 = "xxxx xxxx xxxx xxx";
                                        } else {
                                            strArr = this.PREFIXES_14;
                                            i19 = 14;
                                            str10 = "xxxx xxxx xxxx xx";
                                        }
                                        for (String str12 : strArr) {
                                            if (sb2.length() <= str12.length()) {
                                                if (str12.startsWith(sb2)) {
                                                    i22 = i19;
                                                    str11 = str10;
                                                    break;
                                                }
                                            } else if (sb2.startsWith(str12)) {
                                                i22 = i19;
                                                str11 = str10;
                                                break;
                                            }
                                        }
                                        if (str11 != null) {
                                            break;
                                        }
                                        i23++;
                                    }
                                    if (sb.length() > i22) {
                                        sb.setLength(i22);
                                    }
                                }
                                if (str11 != null) {
                                    if (sb.length() == i22) {
                                        PaymentFormActivity.this.inputFields[1].requestFocus();
                                    }
                                    editTextBoldCursor4.setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                                    int i25 = 0;
                                    while (true) {
                                        if (i25 >= sb.length()) {
                                            break;
                                        } else if (i25 < str11.length()) {
                                            if (str11.charAt(i25) == ' ') {
                                                sb.insert(i25, ' ');
                                                i25++;
                                                if (selectionStart == i25 && (i18 = this.characterAction) != 2 && i18 != 3) {
                                                    selectionStart++;
                                                }
                                            }
                                            i25++;
                                        } else {
                                            sb.insert(i25, ' ');
                                            if (selectionStart == i25 + 1 && (i17 = this.characterAction) != 2 && i17 != 3) {
                                                selectionStart++;
                                            }
                                        }
                                    }
                                }
                                if (sb.toString().equals(editable.toString())) {
                                    z5 = false;
                                } else {
                                    z5 = false;
                                    editable.replace(0, editable.length(), sb);
                                }
                                if (selectionStart >= 0) {
                                    editTextBoldCursor4.setSelection(Math.min(selectionStart, editTextBoldCursor4.length()));
                                }
                                PaymentFormActivity.this.ignoreOnCardChange = z5;
                            }
                        });
                    } else if (i13 == 1) {
                        this.inputFields[i13].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.8
                            private int actionPosition;
                            private int characterAction = -1;
                            private boolean isYear;

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence charSequence, int i17, int i18, int i19) {
                            }

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence charSequence, int i17, int i18, int i19) {
                                if (i18 == 0 && i19 == 1) {
                                    this.isYear = TextUtils.indexOf((CharSequence) PaymentFormActivity.this.inputFields[1].getText(), '/') != -1;
                                    this.characterAction = 1;
                                } else if (i18 == 1 && i19 == 0) {
                                    if (charSequence.charAt(i17) == '/' && i17 > 0) {
                                        this.isYear = false;
                                        this.characterAction = 3;
                                        this.actionPosition = i17 - 1;
                                        return;
                                    }
                                    this.characterAction = 2;
                                } else {
                                    this.characterAction = -1;
                                }
                            }

                            /* JADX WARN: Removed duplicated region for block: B:73:0x01a0  */
                            /* JADX WARN: Removed duplicated region for block: B:77:0x01b4  */
                            /* JADX WARN: Removed duplicated region for block: B:81:0x01c2  */
                            /* JADX WARN: Removed duplicated region for block: B:83:0x01c8  */
                            /* JADX WARN: Removed duplicated region for block: B:90:0x01dd  */
                            @Override // android.text.TextWatcher
                            /*
                                Code decompiled incorrectly, please refer to instructions dump.
                            */
                            public void afterTextChanged(Editable editable) {
                                if (PaymentFormActivity.this.ignoreOnCardChange) {
                                    return;
                                }
                                boolean z5 = true;
                                EditTextBoldCursor editTextBoldCursor4 = PaymentFormActivity.this.inputFields[1];
                                int selectionStart = editTextBoldCursor4.getSelectionStart();
                                String obj = editTextBoldCursor4.getText().toString();
                                if (this.characterAction == 3) {
                                    obj = obj.substring(0, this.actionPosition) + obj.substring(this.actionPosition + 1);
                                    selectionStart--;
                                }
                                StringBuilder sb = new StringBuilder(obj.length());
                                int i17 = 0;
                                while (i17 < obj.length()) {
                                    int i18 = i17 + 1;
                                    String substring = obj.substring(i17, i18);
                                    if ("0123456789".contains(substring)) {
                                        sb.append(substring);
                                    }
                                    i17 = i18;
                                }
                                PaymentFormActivity.this.ignoreOnCardChange = true;
                                PaymentFormActivity.this.inputFields[1].setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                                if (sb.length() > 4) {
                                    sb.setLength(4);
                                }
                                if (sb.length() < 2) {
                                    this.isYear = false;
                                }
                                if (this.isYear) {
                                    int i19 = sb.length() > 2 ? 2 : 1;
                                    String[] strArr = new String[i19];
                                    strArr[0] = sb.substring(0, 2);
                                    if (i19 == 2) {
                                        strArr[1] = sb.substring(2);
                                    }
                                    if (sb.length() == 4 && i19 == 2) {
                                        int intValue = Utilities.parseInt((CharSequence) strArr[0]).intValue();
                                        int intValue2 = Utilities.parseInt((CharSequence) strArr[1]).intValue() + 2000;
                                        Calendar calendar = Calendar.getInstance();
                                        boolean z6 = UserConfig.getInstance(((BaseFragment) PaymentFormActivity.this).currentAccount).getClientPhone().startsWith("7") || (PaymentFormActivity.this.country != null && PaymentFormActivity.this.country.code.equals("7"));
                                        int i20 = z6 ? 2022 : calendar.get(1);
                                        int i21 = z6 ? 1 : calendar.get(2) + 1;
                                        if (intValue2 < i20 || (intValue2 == i20 && intValue < i21)) {
                                            PaymentFormActivity.this.inputFields[1].setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_text_RedRegular));
                                            if (!z5) {
                                            }
                                            if (sb.length() != 2) {
                                            }
                                            selectionStart++;
                                            editTextBoldCursor4.setText(sb);
                                            if (selectionStart >= 0) {
                                            }
                                            PaymentFormActivity.this.ignoreOnCardChange = false;
                                        }
                                        z5 = false;
                                        if (!z5) {
                                        }
                                        if (sb.length() != 2) {
                                        }
                                        selectionStart++;
                                        editTextBoldCursor4.setText(sb);
                                        if (selectionStart >= 0) {
                                        }
                                        PaymentFormActivity.this.ignoreOnCardChange = false;
                                    }
                                    int intValue3 = Utilities.parseInt((CharSequence) strArr[0]).intValue();
                                    if (intValue3 > 12 || intValue3 == 0) {
                                        PaymentFormActivity.this.inputFields[1].setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_text_RedRegular));
                                        if (!z5 && sb.length() == 4) {
                                            PaymentFormActivity.this.inputFields[PaymentFormActivity.this.need_card_name ? (char) 2 : (char) 3].requestFocus();
                                        }
                                        if (sb.length() != 2) {
                                            sb.append('/');
                                        } else {
                                            if (sb.length() > 2 && sb.charAt(2) != '/') {
                                                sb.insert(2, '/');
                                            }
                                            editTextBoldCursor4.setText(sb);
                                            if (selectionStart >= 0) {
                                                editTextBoldCursor4.setSelection(Math.min(selectionStart, editTextBoldCursor4.length()));
                                            }
                                            PaymentFormActivity.this.ignoreOnCardChange = false;
                                        }
                                        selectionStart++;
                                        editTextBoldCursor4.setText(sb);
                                        if (selectionStart >= 0) {
                                        }
                                        PaymentFormActivity.this.ignoreOnCardChange = false;
                                    }
                                    z5 = false;
                                    if (!z5) {
                                        PaymentFormActivity.this.inputFields[PaymentFormActivity.this.need_card_name ? (char) 2 : (char) 3].requestFocus();
                                    }
                                    if (sb.length() != 2) {
                                    }
                                    selectionStart++;
                                    editTextBoldCursor4.setText(sb);
                                    if (selectionStart >= 0) {
                                    }
                                    PaymentFormActivity.this.ignoreOnCardChange = false;
                                }
                                if (sb.length() == 1) {
                                    int intValue4 = Utilities.parseInt((CharSequence) sb.toString()).intValue();
                                    if (intValue4 != 1 && intValue4 != 0) {
                                        sb.insert(0, "0");
                                        selectionStart++;
                                    }
                                } else if (sb.length() == 2) {
                                    int intValue5 = Utilities.parseInt((CharSequence) sb.toString()).intValue();
                                    if (intValue5 > 12 || intValue5 == 0) {
                                        PaymentFormActivity.this.inputFields[1].setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_text_RedRegular));
                                    } else {
                                        z5 = false;
                                    }
                                    selectionStart++;
                                    if (!z5) {
                                    }
                                    if (sb.length() != 2) {
                                    }
                                    selectionStart++;
                                    editTextBoldCursor4.setText(sb);
                                    if (selectionStart >= 0) {
                                    }
                                    PaymentFormActivity.this.ignoreOnCardChange = false;
                                }
                                z5 = false;
                                if (!z5) {
                                }
                                if (sb.length() != 2) {
                                }
                                selectionStart++;
                                editTextBoldCursor4.setText(sb);
                                if (selectionStart >= 0) {
                                }
                                PaymentFormActivity.this.ignoreOnCardChange = false;
                            }
                        });
                    }
                    this.inputFields[i13].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                    this.inputFields[i13].setGravity(LocaleController.isRTL ? 5 : 3);
                    frameLayout5.addView(this.inputFields[i13], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                    this.inputFields[i13].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda28
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView2, int i17, KeyEvent keyEvent) {
                            boolean lambda$createView$7;
                            lambda$createView$7 = PaymentFormActivity.this.lambda$createView$7(textView2, i17, keyEvent);
                            return lambda$createView$7;
                        }
                    });
                    if (i13 == 3) {
                        this.sectionCell[0] = new ShadowSectionCell(context, this.resourcesProvider);
                        this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(-1, -2));
                    } else if (i13 == 5) {
                        this.sectionCell[2] = new ShadowSectionCell(context, this.resourcesProvider);
                        this.linearLayout2.addView(this.sectionCell[2], LayoutHelper.createLinear(-1, -2));
                        TextCheckCell textCheckCell3 = new TextCheckCell(context, this.resourcesProvider);
                        this.checkCell1 = textCheckCell3;
                        textCheckCell3.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        this.checkCell1.setTextAndCheck(LocaleController.getString("PaymentCardSavePaymentInformation", R.string.PaymentCardSavePaymentInformation), this.saveCardInfo, false);
                        this.linearLayout2.addView(this.checkCell1, LayoutHelper.createLinear(-1, -2));
                        this.checkCell1.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda18
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                PaymentFormActivity.this.lambda$createView$8(view2);
                            }
                        });
                        this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                        this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        updateSavePaymentField();
                        this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                    } else if (i13 == 0) {
                        createGooglePayButton(context);
                        frameLayout5.addView(this.googlePayContainer, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 3 : 5) | 16, 0.0f, 0.0f, 4.0f, 0.0f));
                    }
                    if (z4) {
                        View view2 = new View(this, context) { // from class: org.telegram.ui.PaymentFormActivity.9
                            @Override // android.view.View
                            protected void onDraw(Canvas canvas) {
                                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                            }
                        };
                        view2.setBackgroundColor(getThemedColor(i15));
                        this.dividers.add(view2);
                        frameLayout5.addView(view2, new FrameLayout.LayoutParams(-1, 1, 83));
                    }
                    if ((i13 == 4 && !this.need_card_country) || ((i13 == 5 && !this.need_card_postcode) || (i13 == 2 && !this.need_card_name))) {
                        frameLayout5.setVisibility(8);
                    }
                    i13++;
                }
                if (!this.need_card_country && !this.need_card_postcode) {
                    this.headerCell[1].setVisibility(8);
                    this.sectionCell[0].setVisibility(8);
                }
                if (this.need_card_postcode) {
                    this.inputFields[5].setImeOptions(268435462);
                } else {
                    this.inputFields[3].setImeOptions(268435462);
                }
            }
        } else if (i7 == 1) {
            int size = this.requestedInfo.shipping_options.size();
            this.radioCells = new RadioCell[size];
            int i17 = 0;
            while (i17 < size) {
                TLRPC$TL_shippingOption tLRPC$TL_shippingOption = this.requestedInfo.shipping_options.get(i17);
                this.radioCells[i17] = new RadioCell(context);
                this.radioCells[i17].setTag(Integer.valueOf(i17));
                this.radioCells[i17].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.radioCells[i17].setText(String.format("%s - %s", getTotalPriceString(tLRPC$TL_shippingOption.prices), tLRPC$TL_shippingOption.title), i17 == 0, i17 != size + (-1));
                this.radioCells[i17].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda12
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        PaymentFormActivity.this.lambda$createView$9(view3);
                    }
                });
                this.linearLayout2.addView(this.radioCells[i17]);
                i17++;
            }
            this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
            this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
        } else if (i7 == 3) {
            this.inputFields = new EditTextBoldCursor[2];
            int i18 = 0;
            for (int i19 = 2; i18 < i19; i19 = 2) {
                if (i18 == 0) {
                    z = false;
                    this.headerCell[0] = new HeaderCell(context, this.resourcesProvider);
                    this.headerCell[0].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    this.headerCell[0].setText(LocaleController.getString("PaymentCardTitle", R.string.PaymentCardTitle));
                    this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                } else {
                    z = false;
                }
                FrameLayout frameLayout6 = new FrameLayout(context);
                frameLayout6.setClipChildren(z);
                this.linearLayout2.addView(frameLayout6, LayoutHelper.createLinear(-1, 50));
                int i20 = Theme.key_windowBackgroundWhite;
                frameLayout6.setBackgroundColor(getThemedColor(i20));
                boolean z5 = i18 != 1;
                if (z5) {
                    if (i18 != 7 || this.paymentForm.invoice.phone_requested) {
                        if (i18 == 6) {
                            TLRPC$TL_invoice tLRPC$TL_invoice8 = this.paymentForm.invoice;
                            if (!tLRPC$TL_invoice8.phone_requested) {
                            }
                        }
                    }
                    z5 = false;
                }
                if (z5) {
                    View view3 = new View(this, context) { // from class: org.telegram.ui.PaymentFormActivity.10
                        @Override // android.view.View
                        protected void onDraw(Canvas canvas) {
                            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                        }
                    };
                    view3.setBackgroundColor(getThemedColor(i20));
                    this.dividers.add(view3);
                    frameLayout6.addView(view3, new FrameLayout.LayoutParams(-1, 1, 83));
                }
                this.inputFields[i18] = new EditTextBoldCursor(context);
                this.inputFields[i18].setTag(Integer.valueOf(i18));
                this.inputFields[i18].setTextSize(1, 16.0f);
                this.inputFields[i18].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                EditTextBoldCursor editTextBoldCursor4 = this.inputFields[i18];
                int i21 = Theme.key_windowBackgroundWhiteBlackText;
                editTextBoldCursor4.setTextColor(getThemedColor(i21));
                this.inputFields[i18].setBackgroundDrawable(null);
                this.inputFields[i18].setCursorColor(getThemedColor(i21));
                this.inputFields[i18].setCursorSize(AndroidUtilities.dp(20.0f));
                this.inputFields[i18].setCursorWidth(1.5f);
                if (i18 == 0) {
                    this.inputFields[i18].setOnTouchListener(PaymentFormActivity$$ExternalSyntheticLambda24.INSTANCE);
                    this.inputFields[i18].setInputType(0);
                } else {
                    this.inputFields[i18].setInputType(129);
                    this.inputFields[i18].setTypeface(Typeface.DEFAULT);
                }
                this.inputFields[i18].setImeOptions(268435462);
                if (i18 == 0) {
                    EditTextBoldCursor editTextBoldCursor5 = this.inputFields[i18];
                    TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard = this.savedCredentialsCard;
                    editTextBoldCursor5.setText(tLRPC$TL_paymentSavedCredentialsCard == null ? "" : tLRPC$TL_paymentSavedCredentialsCard.title);
                } else if (i18 == 1) {
                    this.inputFields[i18].setHint(LocaleController.getString("LoginPassword", R.string.LoginPassword));
                    this.inputFields[i18].requestFocus();
                }
                this.inputFields[i18].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                this.inputFields[i18].setGravity(LocaleController.isRTL ? 5 : 3);
                frameLayout6.addView(this.inputFields[i18], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                this.inputFields[i18].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda27
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView2, int i22, KeyEvent keyEvent) {
                        boolean lambda$createView$11;
                        lambda$createView$11 = PaymentFormActivity.this.lambda$createView$11(textView2, i22, keyEvent);
                        return lambda$createView$11;
                    }
                });
                if (i18 == 1) {
                    this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                    TextInfoPrivacyCell textInfoPrivacyCell2 = this.bottomCell[0];
                    int i22 = R.string.PaymentConfirmationMessage;
                    Object[] objArr = new Object[1];
                    TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard2 = this.savedCredentialsCard;
                    objArr[0] = tLRPC$TL_paymentSavedCredentialsCard2 == null ? "" : tLRPC$TL_paymentSavedCredentialsCard2.title;
                    textInfoPrivacyCell2.setText(LocaleController.formatString("PaymentConfirmationMessage", i22, objArr));
                    TextInfoPrivacyCell textInfoPrivacyCell3 = this.bottomCell[0];
                    int i23 = R.drawable.greydivider;
                    int i24 = Theme.key_windowBackgroundGrayShadow;
                    textInfoPrivacyCell3.setBackgroundDrawable(Theme.getThemedDrawableByKey(context, i23, i24));
                    this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                    this.settingsCell[0] = new TextSettingsCell(context, this.resourcesProvider);
                    this.settingsCell[0].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                    this.settingsCell[0].setText(LocaleController.getString("PaymentConfirmationNewCard", R.string.PaymentConfirmationNewCard), false);
                    this.linearLayout2.addView(this.settingsCell[0], LayoutHelper.createLinear(-1, -2));
                    this.settingsCell[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda6
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view4) {
                            PaymentFormActivity.this.lambda$createView$12(view4);
                        }
                    });
                    this.bottomCell[1] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                    this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, i24));
                    this.linearLayout2.addView(this.bottomCell[1], LayoutHelper.createLinear(-1, -2));
                }
                i18++;
            }
        } else if (i7 == 4 || i7 == 5) {
            PaymentInfoCell paymentInfoCell = new PaymentInfoCell(context);
            this.paymentInfoCell = paymentInfoCell;
            paymentInfoCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
            MessageObject messageObject = this.messageObject;
            if (messageObject != null) {
                this.paymentInfoCell.setInvoice((TLRPC$TL_messageMediaInvoice) messageObject.messageOwner.media, this.currentBotName);
            } else {
                TLRPC$TL_payments_paymentReceipt tLRPC$TL_payments_paymentReceipt = this.paymentReceipt;
                if (tLRPC$TL_payments_paymentReceipt != null) {
                    this.paymentInfoCell.setReceipt(tLRPC$TL_payments_paymentReceipt, this.currentBotName);
                } else if (this.invoiceSlug != null) {
                    PaymentInfoCell paymentInfoCell2 = this.paymentInfoCell;
                    TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm4 = this.paymentForm;
                    paymentInfoCell2.setInfo(tLRPC$TL_payments_paymentForm4.title, tLRPC$TL_payments_paymentForm4.description, tLRPC$TL_payments_paymentForm4.photo, this.currentBotName, tLRPC$TL_payments_paymentForm4);
                } else if (this.invoicePremiumGiftCode != null) {
                    PaymentInfoCell paymentInfoCell3 = this.paymentInfoCell;
                    TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm5 = this.paymentForm;
                    paymentInfoCell3.setInfo(tLRPC$TL_payments_paymentForm5.title, tLRPC$TL_payments_paymentForm5.description, tLRPC$TL_payments_paymentForm5.photo, this.currentBotName, tLRPC$TL_payments_paymentForm5);
                }
            }
            this.linearLayout2.addView(this.paymentInfoCell, LayoutHelper.createLinear(-1, -2));
            this.sectionCell[0] = new ShadowSectionCell(context, this.resourcesProvider);
            this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(-1, -2));
            ArrayList<TLRPC$TL_labeledPrice> arrayList = new ArrayList<>(this.paymentForm.invoice.prices);
            this.prices = arrayList;
            TLRPC$TL_shippingOption tLRPC$TL_shippingOption2 = this.shippingOption;
            if (tLRPC$TL_shippingOption2 != null) {
                arrayList.addAll(tLRPC$TL_shippingOption2.prices);
            }
            this.totalPrice = new String[1];
            for (int i25 = 0; i25 < this.prices.size(); i25++) {
                TLRPC$TL_labeledPrice tLRPC$TL_labeledPrice = this.prices.get(i25);
                TextPriceCell textPriceCell = new TextPriceCell(context);
                textPriceCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                textPriceCell.setTextAndValue(tLRPC$TL_labeledPrice.label, LocaleController.getInstance().formatCurrencyString(tLRPC$TL_labeledPrice.amount, this.paymentForm.invoice.currency), false);
                this.linearLayout2.addView(textPriceCell);
            }
            if (this.currentStep == 5 && this.tipAmount != null) {
                TextPriceCell textPriceCell2 = new TextPriceCell(context);
                textPriceCell2.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                textPriceCell2.setTextAndValue(LocaleController.getString("PaymentTip", R.string.PaymentTip), LocaleController.getInstance().formatCurrencyString(this.tipAmount.longValue(), this.paymentForm.invoice.currency), false);
                this.linearLayout2.addView(textPriceCell2);
            }
            TextPriceCell textPriceCell3 = new TextPriceCell(context);
            this.totalCell = textPriceCell3;
            int i26 = Theme.key_windowBackgroundWhite;
            textPriceCell3.setBackgroundColor(getThemedColor(i26));
            this.totalPrice[0] = getTotalPriceString(this.prices);
            this.totalCell.setTextAndValue(LocaleController.getString("PaymentTransactionTotal", R.string.PaymentTransactionTotal), this.totalPrice[0], true);
            if (this.currentStep == 4 && (this.paymentForm.invoice.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
                FrameLayout frameLayout7 = new FrameLayout(context);
                frameLayout7.setClipChildren(false);
                frameLayout7.setBackgroundColor(getThemedColor(i26));
                this.linearLayout2.addView(frameLayout7, LayoutHelper.createLinear(-1, this.paymentForm.invoice.suggested_tip_amounts.isEmpty() ? 40 : 78));
                frameLayout7.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda11
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view4) {
                        PaymentFormActivity.this.lambda$createView$13(view4);
                    }
                });
                TextPriceCell textPriceCell4 = new TextPriceCell(context);
                textPriceCell4.setBackgroundColor(getThemedColor(i26));
                textPriceCell4.setTextAndValue(LocaleController.getString("PaymentTipOptional", R.string.PaymentTipOptional), "", false);
                frameLayout7.addView(textPriceCell4);
                this.inputFields = r1;
                EditTextBoldCursor[] editTextBoldCursorArr2 = {new EditTextBoldCursor(context)};
                this.inputFields[0].setTag(0);
                this.inputFields[0].setTextSize(1, 16.0f);
                EditTextBoldCursor editTextBoldCursor6 = this.inputFields[0];
                int i27 = Theme.key_windowBackgroundWhiteGrayText2;
                editTextBoldCursor6.setHintTextColor(getThemedColor(i27));
                this.inputFields[0].setTextColor(getThemedColor(i27));
                this.inputFields[0].setBackgroundDrawable(null);
                this.inputFields[0].setCursorColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                this.inputFields[0].setCursorSize(AndroidUtilities.dp(20.0f));
                this.inputFields[0].setCursorWidth(1.5f);
                this.inputFields[0].setInputType(3);
                this.inputFields[0].setImeOptions(268435462);
                this.inputFields[0].setHint(LocaleController.getInstance().formatCurrencyString(0L, this.paymentForm.invoice.currency));
                this.inputFields[0].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                this.inputFields[0].setGravity(LocaleController.isRTL ? 3 : 5);
                frameLayout7.addView(this.inputFields[0], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 9.0f, 21.0f, 1.0f));
                this.inputFields[0].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.11
                    private boolean anyBefore;
                    private int beforeTextLength;
                    char[] commas = {',', '.', 1643, 12289, 11841, 65040, 65041, 65104, 65105, 65292, 65380, 699};
                    private int enteredCharacterStart;
                    private boolean isDeletedChar;
                    private boolean lastDotEntered;
                    private String overrideText;

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i28, int i29, int i30) {
                    }

                    private int indexOfComma(String str10) {
                        int i28 = 0;
                        while (true) {
                            char[] cArr = this.commas;
                            if (i28 >= cArr.length) {
                                return -1;
                            }
                            int indexOf = str10.indexOf(cArr[i28]);
                            if (indexOf >= 0) {
                                return indexOf;
                            }
                            i28++;
                        }
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i28, int i29, int i30) {
                        if (PaymentFormActivity.this.ignoreOnTextChange) {
                            return;
                        }
                        this.anyBefore = !TextUtils.isEmpty(charSequence);
                        this.overrideText = null;
                        this.beforeTextLength = charSequence == null ? 0 : charSequence.length();
                        this.enteredCharacterStart = i28;
                        boolean z6 = i29 == 1 && i30 == 0;
                        this.isDeletedChar = z6;
                        if (!z6) {
                            return;
                        }
                        String fixNumbers = LocaleController.fixNumbers(charSequence);
                        char charAt = fixNumbers.charAt(i28);
                        int indexOfComma = indexOfComma(fixNumbers);
                        String substring = indexOfComma >= 0 ? fixNumbers.substring(indexOfComma + 1) : "";
                        long longValue2 = Utilities.parseLong(PhoneFormat.stripExceptNumbers(substring)).longValue();
                        if ((charAt >= '0' && charAt <= '9') || (substring.length() != 0 && longValue2 == 0)) {
                            if (indexOfComma <= 0 || i28 <= indexOfComma || longValue2 != 0) {
                                return;
                            }
                            this.overrideText = fixNumbers.substring(0, indexOfComma - 1);
                            return;
                        }
                        while (true) {
                            i28--;
                            if (i28 < 0) {
                                return;
                            }
                            char charAt2 = fixNumbers.charAt(i28);
                            if (charAt2 >= '0' && charAt2 <= '9') {
                                this.overrideText = fixNumbers.substring(0, i28) + fixNumbers.substring(i28 + 1);
                                return;
                            }
                        }
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        boolean z6;
                        String substring;
                        if (PaymentFormActivity.this.ignoreOnTextChange) {
                            return;
                        }
                        long longValue2 = PaymentFormActivity.this.tipAmount != null ? PaymentFormActivity.this.tipAmount.longValue() : 0L;
                        String str10 = this.overrideText;
                        if (str10 == null) {
                            str10 = LocaleController.fixNumbers(editable.toString());
                        }
                        int indexOfComma = indexOfComma(str10);
                        boolean z7 = indexOfComma >= 0;
                        int currencyExpDivider = LocaleController.getCurrencyExpDivider(PaymentFormActivity.this.paymentForm.invoice.currency);
                        String substring2 = indexOfComma >= 0 ? str10.substring(0, indexOfComma) : str10;
                        String str11 = "";
                        String substring3 = indexOfComma >= 0 ? str10.substring(indexOfComma + 1) : "";
                        long longValue3 = Utilities.parseLong(PhoneFormat.stripExceptNumbers(substring2)).longValue() * currencyExpDivider;
                        long longValue4 = Utilities.parseLong(PhoneFormat.stripExceptNumbers(substring3)).longValue();
                        String str12 = "" + longValue4;
                        String str13 = "" + (currencyExpDivider - 1);
                        if (indexOfComma > 0 && str12.length() > str13.length()) {
                            if (this.enteredCharacterStart - indexOfComma < str12.length()) {
                                substring = str12.substring(0, str13.length());
                            } else {
                                substring = str12.substring(str12.length() - str13.length());
                            }
                            longValue4 = Utilities.parseLong(substring).longValue();
                        }
                        PaymentFormActivity.this.tipAmount = Long.valueOf(longValue3 + longValue4);
                        if (PaymentFormActivity.this.paymentForm.invoice.max_tip_amount != 0 && PaymentFormActivity.this.tipAmount.longValue() > PaymentFormActivity.this.paymentForm.invoice.max_tip_amount) {
                            PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                            paymentFormActivity.tipAmount = Long.valueOf(paymentFormActivity.paymentForm.invoice.max_tip_amount);
                        }
                        int selectionStart = PaymentFormActivity.this.inputFields[0].getSelectionStart();
                        PaymentFormActivity.this.ignoreOnTextChange = true;
                        if (PaymentFormActivity.this.tipAmount.longValue() == 0) {
                            PaymentFormActivity.this.inputFields[0].setText("");
                            z6 = z7;
                        } else {
                            EditTextBoldCursor editTextBoldCursor7 = PaymentFormActivity.this.inputFields[0];
                            z6 = z7;
                            str11 = LocaleController.getInstance().formatCurrencyString(PaymentFormActivity.this.tipAmount.longValue(), false, z7, true, PaymentFormActivity.this.paymentForm.invoice.currency);
                            editTextBoldCursor7.setText(str11);
                        }
                        if (longValue2 < PaymentFormActivity.this.tipAmount.longValue() && longValue2 != 0 && this.anyBefore && selectionStart >= 0) {
                            PaymentFormActivity.this.inputFields[0].setSelection(Math.min(selectionStart, PaymentFormActivity.this.inputFields[0].length()));
                        } else if (this.isDeletedChar && this.beforeTextLength != PaymentFormActivity.this.inputFields[0].length()) {
                            PaymentFormActivity.this.inputFields[0].setSelection(Math.max(0, Math.min(selectionStart, PaymentFormActivity.this.inputFields[0].length())));
                        } else if (this.lastDotEntered || !z6 || indexOfComma < 0) {
                            PaymentFormActivity.this.inputFields[0].setSelection(PaymentFormActivity.this.inputFields[0].length());
                        } else {
                            int indexOfComma2 = indexOfComma(str11);
                            if (indexOfComma2 > 0) {
                                PaymentFormActivity.this.inputFields[0].setSelection(indexOfComma2 + 1);
                            } else {
                                PaymentFormActivity.this.inputFields[0].setSelection(PaymentFormActivity.this.inputFields[0].length());
                            }
                        }
                        this.lastDotEntered = z6;
                        PaymentFormActivity.this.updateTotalPrice();
                        this.overrideText = null;
                        PaymentFormActivity.this.ignoreOnTextChange = false;
                    }
                });
                this.inputFields[0].setOnEditorActionListener(PaymentFormActivity$$ExternalSyntheticLambda30.INSTANCE);
                this.inputFields[0].requestFocus();
                if (!this.paymentForm.invoice.suggested_tip_amounts.isEmpty()) {
                    HorizontalScrollView horizontalScrollView = new HorizontalScrollView(context);
                    horizontalScrollView.setHorizontalScrollBarEnabled(false);
                    horizontalScrollView.setVerticalScrollBarEnabled(false);
                    horizontalScrollView.setClipToPadding(false);
                    horizontalScrollView.setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
                    horizontalScrollView.setFillViewport(true);
                    frameLayout7.addView(horizontalScrollView, LayoutHelper.createFrame(-1, 30.0f, 51, 0.0f, 44.0f, 0.0f, 0.0f));
                    final int[] iArr = new int[1];
                    final int[] iArr2 = new int[1];
                    final int size2 = this.paymentForm.invoice.suggested_tip_amounts.size();
                    c = 5;
                    str = "";
                    r11 = 0;
                    frameLayout = frameLayout4;
                    LinearLayout linearLayout2 = new LinearLayout(this, context) { // from class: org.telegram.ui.PaymentFormActivity.12
                        boolean ignoreLayout;

                        @Override // android.widget.LinearLayout, android.view.View
                        protected void onMeasure(int i28, int i29) {
                            View childAt;
                            int size3 = View.MeasureSpec.getSize(i28);
                            this.ignoreLayout = true;
                            int dp = AndroidUtilities.dp(9.0f);
                            int i30 = size2;
                            int i31 = dp * (i30 - 1);
                            int i32 = (iArr[0] * i30) + i31;
                            float f = 1.0f;
                            if (i32 <= size3) {
                                setWeightSum(1.0f);
                                int childCount = getChildCount();
                                for (int i33 = 0; i33 < childCount; i33++) {
                                    getChildAt(i33).getLayoutParams().width = 0;
                                    ((LinearLayout.LayoutParams) getChildAt(i33).getLayoutParams()).weight = 1.0f / childCount;
                                }
                            } else if (iArr2[0] + i31 <= size3) {
                                setWeightSum(1.0f);
                                int i34 = size3 - i31;
                                int childCount2 = getChildCount();
                                for (int i35 = 0; i35 < childCount2; i35++) {
                                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildAt(i35).getLayoutParams();
                                    layoutParams.width = 0;
                                    float intValue = ((Integer) childAt.getTag(R.id.width_tag)).intValue() / i34;
                                    layoutParams.weight = intValue;
                                    f -= intValue;
                                }
                                float f2 = f / (size2 - 1);
                                if (f2 > 0.0f) {
                                    int childCount3 = getChildCount();
                                    for (int i36 = 0; i36 < childCount3; i36++) {
                                        View childAt2 = getChildAt(i36);
                                        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) childAt2.getLayoutParams();
                                        if (((Integer) childAt2.getTag(R.id.width_tag)).intValue() != iArr[0]) {
                                            layoutParams2.weight += f2;
                                        }
                                    }
                                }
                            } else {
                                setWeightSum(0.0f);
                                int childCount4 = getChildCount();
                                for (int i37 = 0; i37 < childCount4; i37++) {
                                    getChildAt(i37).getLayoutParams().width = -2;
                                    ((LinearLayout.LayoutParams) getChildAt(i37).getLayoutParams()).weight = 0.0f;
                                }
                            }
                            this.ignoreLayout = false;
                            super.onMeasure(i28, i29);
                        }

                        @Override // android.view.View, android.view.ViewParent
                        public void requestLayout() {
                            if (this.ignoreLayout) {
                                return;
                            }
                            super.requestLayout();
                        }
                    };
                    this.tipLayout = linearLayout2;
                    linearLayout2.setOrientation(0);
                    horizontalScrollView.addView(this.tipLayout, LayoutHelper.createScroll(-1, 30, 51));
                    int themedColor = getThemedColor(Theme.key_contacts_inviteBackground);
                    int i28 = 0;
                    while (i28 < size2) {
                        if (LocaleController.isRTL) {
                            longValue = this.paymentForm.invoice.suggested_tip_amounts.get((size2 - i28) - 1).longValue();
                        } else {
                            longValue = this.paymentForm.invoice.suggested_tip_amounts.get(i28).longValue();
                        }
                        String formatCurrencyString = LocaleController.getInstance().formatCurrencyString(longValue, this.paymentForm.invoice.currency);
                        final TextView textView2 = new TextView(context);
                        textView2.setTextSize(1, 14.0f);
                        textView2.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                        textView2.setLines(1);
                        textView2.setTag(Long.valueOf(longValue));
                        textView2.setMaxLines(1);
                        textView2.setText(formatCurrencyString);
                        textView2.setPadding(AndroidUtilities.dp(15.0f), 0, AndroidUtilities.dp(15.0f), 0);
                        textView2.setTextColor(getThemedColor(Theme.key_chats_secretName));
                        textView2.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(15.0f), 536870911 & themedColor));
                        textView2.setSingleLine(true);
                        textView2.setGravity(17);
                        this.tipLayout.addView(textView2, LayoutHelper.createLinear(-2, -1, 19, 0, 0, i28 != size2 + (-1) ? 9 : 0, 0));
                        textView2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda20
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view4) {
                                PaymentFormActivity.this.lambda$createView$15(textView2, longValue, view4);
                            }
                        });
                        int ceil = ((int) Math.ceil(textView2.getPaint().measureText(formatCurrencyString))) + AndroidUtilities.dp(30.0f);
                        textView2.setTag(R.id.width_tag, Integer.valueOf(ceil));
                        iArr[0] = Math.max(iArr[0], ceil);
                        iArr2[0] = iArr2[0] + ceil;
                        i28++;
                    }
                    this.linearLayout2.addView(this.totalCell);
                    this.sectionCell[2] = new ShadowSectionCell(context, this.resourcesProvider);
                    this.sectionCell[2].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    this.linearLayout2.addView(this.sectionCell[2], LayoutHelper.createLinear(-1, -2));
                    this.detailSettingsCell[r11] = new TextDetailSettingsCell(context);
                    this.detailSettingsCell[r11].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                    TextDetailSettingsCell textDetailSettingsCell = this.detailSettingsCell[r11];
                    String str10 = this.cardName;
                    textDetailSettingsCell.setTextAndValueAndIcon((str10 != null || str10.length() <= 1) ? this.cardName : this.cardName.substring(r11, 1).toUpperCase() + this.cardName.substring(1), LocaleController.getString("PaymentCheckoutMethod", R.string.PaymentCheckoutMethod), R.drawable.msg_payment_card, true);
                    i = (this.isCheckoutPreview || ((str3 = this.cardName) != null && str3.length() > 1)) ? 0 : 8;
                    this.detailSettingsCell[r11].setVisibility(i);
                    this.linearLayout2.addView(this.detailSettingsCell[r11]);
                    if (this.currentStep == 4) {
                        this.detailSettingsCell[r11].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda13
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view4) {
                                PaymentFormActivity.this.lambda$createView$16(view4);
                            }
                        });
                    }
                    tLRPC$User = null;
                    for (i2 = 0; i2 < this.paymentForm.users.size(); i2++) {
                        TLRPC$User tLRPC$User4 = this.paymentForm.users.get(i2);
                        if (tLRPC$User4.id == this.paymentForm.provider_id) {
                            tLRPC$User = tLRPC$User4;
                        }
                    }
                    this.detailSettingsCell[1] = new TextDetailSettingsCell(context);
                    this.detailSettingsCell[1].setBackground(Theme.getSelectorDrawable(true));
                    if (tLRPC$User == null) {
                        TextDetailSettingsCell textDetailSettingsCell2 = this.detailSettingsCell[1];
                        str2 = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name);
                        String string = LocaleController.getString("PaymentCheckoutProvider", R.string.PaymentCheckoutProvider);
                        int i29 = R.drawable.msg_payment_provider;
                        TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo2 = this.validateRequest;
                        textDetailSettingsCell2.setTextAndValueAndIcon(str2, string, i29, ((tLRPC$TL_payments_validateRequestedInfo2 == null || (tLRPC$TL_payments_validateRequestedInfo2.info.shipping_address == null && this.shippingOption == null)) && ((tLRPC$TL_paymentRequestedInfo2 = this.paymentForm.saved_info) == null || tLRPC$TL_paymentRequestedInfo2.shipping_address == null)) ? false : true);
                        this.linearLayout2.addView(this.detailSettingsCell[1]);
                    } else {
                        str2 = str;
                    }
                    this.detailSettingsCell[1].setVisibility(tLRPC$User == null ? i : 8);
                    tLRPC$TL_payments_validateRequestedInfo = this.validateRequest;
                    if (tLRPC$TL_payments_validateRequestedInfo == null || (this.isCheckoutPreview && (tLRPC$TL_payments_paymentForm2 = this.paymentForm) != null && tLRPC$TL_payments_paymentForm2.saved_info != null)) {
                        tLRPC$TL_paymentRequestedInfo = tLRPC$TL_payments_validateRequestedInfo == null ? tLRPC$TL_payments_validateRequestedInfo.info : this.paymentForm.saved_info;
                        this.detailSettingsCell[2] = new TextDetailSettingsCell(context);
                        this.detailSettingsCell[2].setVisibility(8);
                        this.linearLayout2.addView(this.detailSettingsCell[2]);
                        if (tLRPC$TL_paymentRequestedInfo.shipping_address != null) {
                            this.detailSettingsCell[2].setVisibility(r11);
                            if (this.currentStep == 4) {
                                this.detailSettingsCell[2].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                                this.detailSettingsCell[2].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda19
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view4) {
                                        PaymentFormActivity.this.lambda$createView$17(view4);
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
                            this.detailSettingsCell[3].setVisibility(r11);
                            if (this.currentStep == 4) {
                                this.detailSettingsCell[3].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                                this.detailSettingsCell[3].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda14
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view4) {
                                        PaymentFormActivity.this.lambda$createView$18(view4);
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
                            this.detailSettingsCell[4].setVisibility(r11);
                            if (this.currentStep == 4) {
                                this.detailSettingsCell[4].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                                this.detailSettingsCell[4].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda10
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view4) {
                                        PaymentFormActivity.this.lambda$createView$19(view4);
                                    }
                                });
                            } else {
                                this.detailSettingsCell[4].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            }
                        }
                        this.detailSettingsCell[c] = new TextDetailSettingsCell(context);
                        this.detailSettingsCell[c].setVisibility(8);
                        this.linearLayout2.addView(this.detailSettingsCell[c]);
                        if (tLRPC$TL_paymentRequestedInfo.email != null) {
                            this.detailSettingsCell[c].setVisibility(r11);
                            if (this.currentStep == 4) {
                                this.detailSettingsCell[c].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                                this.detailSettingsCell[c].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda15
                                    @Override // android.view.View.OnClickListener
                                    public final void onClick(View view4) {
                                        PaymentFormActivity.this.lambda$createView$20(view4);
                                    }
                                });
                            } else {
                                this.detailSettingsCell[c].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            }
                        }
                        if (this.shippingOption != null) {
                            this.detailSettingsCell[6] = new TextDetailSettingsCell(context);
                            this.detailSettingsCell[6].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            this.detailSettingsCell[6].setTextAndValueAndIcon(this.shippingOption.title, LocaleController.getString("PaymentCheckoutShippingMethod", R.string.PaymentCheckoutShippingMethod), R.drawable.msg_payment_delivery, r11);
                            this.linearLayout2.addView(this.detailSettingsCell[6]);
                        }
                        setAddressFields(tLRPC$TL_paymentRequestedInfo);
                    }
                    if (this.currentStep == 4) {
                        this.recurrentAccepted = !this.isCheckoutPreview;
                        this.bottomLayout = new BottomFrameLayout(context, this.paymentForm);
                        int i30 = Build.VERSION.SDK_INT;
                        if (i30 >= 21) {
                            View view4 = new View(context);
                            view4.setBackground(Theme.getSelectorDrawable(getThemedColor(Theme.key_listSelector), (boolean) r11));
                            this.bottomLayout.addView(view4, LayoutHelper.createFrame(-1, -1.0f));
                        }
                        FrameLayout frameLayout8 = frameLayout;
                        frameLayout8.addView(this.bottomLayout, LayoutHelper.createFrame(-1, 48, 80));
                        this.bottomLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda21
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view5) {
                                PaymentFormActivity.this.lambda$createView$23(str2, view5);
                            }
                        });
                        TextView textView3 = new TextView(context);
                        this.payTextView = textView3;
                        int i31 = Theme.key_contacts_inviteText;
                        textView3.setTextColor(getThemedColor(i31));
                        TextView textView4 = this.payTextView;
                        int i32 = R.string.PaymentCheckoutPay;
                        Object[] objArr2 = new Object[1];
                        objArr2[r11] = this.totalPrice[r11];
                        textView4.setText(LocaleController.formatString("PaymentCheckoutPay", i32, objArr2));
                        this.payTextView.setTextSize(1, 14.0f);
                        this.payTextView.setGravity(17);
                        this.payTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                        this.bottomLayout.addView(this.payTextView, LayoutHelper.createFrame(-1, -1.0f));
                        ContextProgressView contextProgressView2 = new ContextProgressView(context, r11);
                        this.progressViewButton = contextProgressView2;
                        contextProgressView2.setVisibility(4);
                        int themedColor2 = getThemedColor(i31);
                        this.progressViewButton.setColors(805306367 & themedColor2, themedColor2);
                        this.bottomLayout.addView(this.progressViewButton, LayoutHelper.createFrame(-1, -1.0f));
                        this.bottomLayout.setChecked(this.recurrentAccepted, r11);
                        this.payTextView.setAlpha(!this.recurrentAccepted ? 0.8f : 1.0f);
                        this.doneItem.setEnabled(r11);
                        this.doneItem.getContentView().setVisibility(4);
                        AndroidUtilities.checkAndroidTheme(context, true);
                        WebView webView2 = new WebView(this, context) { // from class: org.telegram.ui.PaymentFormActivity.17
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
                        if (i30 >= 21) {
                            this.webView.getSettings().setMixedContentMode(r11);
                            CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
                        }
                        this.webView.setWebViewClient(new WebViewClient() { // from class: org.telegram.ui.PaymentFormActivity.18
                            @Override // android.webkit.WebViewClient
                            public void onPageFinished(WebView webView3, String str11) {
                                super.onPageFinished(webView3, str11);
                                PaymentFormActivity.this.webviewLoading = false;
                                PaymentFormActivity.this.showEditDoneProgress(true, false);
                                PaymentFormActivity.this.updateSavePaymentField();
                            }

                            @Override // android.webkit.WebViewClient
                            public boolean shouldOverrideUrlLoading(WebView webView3, String str11) {
                                try {
                                    Uri parse = Uri.parse(str11);
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
                                                ((Activity) PaymentFormActivity.this.getContext()).startActivityForResult(new Intent("android.intent.action.VIEW", parse), 210);
                                            }
                                        } catch (ActivityNotFoundException unused5) {
                                            new AlertDialog.Builder(context).setTitle(PaymentFormActivity.this.currentBotName).setMessage(LocaleController.getString(R.string.PaymentAppNotFoundForDeeplink)).setPositiveButton(LocaleController.getString(R.string.OK), null).show();
                                        }
                                        return true;
                                    }
                                } catch (Exception unused6) {
                                    return false;
                                }
                            }
                        });
                        this.recurrentAcceptCell = null;
                        if (this.paymentForm.invoice.terms_url != null) {
                            RecurrentPaymentsAcceptCell recurrentPaymentsAcceptCell = new RecurrentPaymentsAcceptCell(context, getResourceProvider());
                            this.recurrentAcceptCell = recurrentPaymentsAcceptCell;
                            recurrentPaymentsAcceptCell.setChecked(this.recurrentAccepted);
                            String string2 = LocaleController.getString(R.string.PaymentCheckoutAcceptRecurrent);
                            ?? spannableStringBuilder = new SpannableStringBuilder(string2);
                            int indexOf = string2.indexOf(42);
                            int lastIndexOf = string2.lastIndexOf(42);
                            String str11 = string2;
                            str11 = string2;
                            if (indexOf != -1 && lastIndexOf != -1) {
                                ?? spannableString = new SpannableString(string2.substring(indexOf + 1, lastIndexOf));
                                spannableString.setSpan(new URLSpanNoUnderline(this.paymentForm.invoice.terms_url), r11, spannableString.length(), 33);
                                spannableStringBuilder.replace(indexOf, lastIndexOf + 1, spannableString);
                                str11 = string2.substring(r11, indexOf) + spannableString + string2.substring(i3);
                            }
                            int indexOf2 = str11.indexOf("%1$s");
                            if (indexOf2 != -1) {
                                spannableStringBuilder.replace(indexOf2, indexOf2 + 4, this.currentBotName);
                                spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), indexOf2, this.currentBotName.length() + indexOf2, 33);
                            }
                            this.recurrentAcceptCell.setText(spannableStringBuilder);
                            this.recurrentAcceptCell.setBackground(Theme.createSelectorWithBackgroundDrawable(getThemedColor(Theme.key_windowBackgroundWhite), getThemedColor(Theme.key_listSelector)));
                            this.recurrentAcceptCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda7
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view5) {
                                    PaymentFormActivity.this.lambda$createView$24(view5);
                                }
                            });
                            frameLayout8.addView(this.recurrentAcceptCell, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 48.0f));
                        }
                        frameLayout8.addView(this.webView, LayoutHelper.createFrame(-1, -1.0f));
                        this.webView.setVisibility(8);
                    }
                    this.sectionCell[1] = new ShadowSectionCell(context, this.resourcesProvider);
                    this.sectionCell[1].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    if (i != 0 && this.currentStep == 4 && this.validateRequest == null && ((tLRPC$TL_payments_paymentForm = this.paymentForm) == null || tLRPC$TL_payments_paymentForm.saved_info == null)) {
                        this.sectionCell[1].setVisibility(i);
                    }
                    this.linearLayout2.addView(this.sectionCell[1], LayoutHelper.createLinear(-1, -2));
                }
            }
            str = "";
            frameLayout = frameLayout4;
            r11 = 0;
            c = 5;
            this.linearLayout2.addView(this.totalCell);
            this.sectionCell[2] = new ShadowSectionCell(context, this.resourcesProvider);
            this.sectionCell[2].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            this.linearLayout2.addView(this.sectionCell[2], LayoutHelper.createLinear(-1, -2));
            this.detailSettingsCell[r11] = new TextDetailSettingsCell(context);
            this.detailSettingsCell[r11].setBackgroundDrawable(Theme.getSelectorDrawable(true));
            TextDetailSettingsCell textDetailSettingsCell3 = this.detailSettingsCell[r11];
            String str102 = this.cardName;
            if (str102 != null) {
            }
            textDetailSettingsCell3.setTextAndValueAndIcon((str102 != null || str102.length() <= 1) ? this.cardName : this.cardName.substring(r11, 1).toUpperCase() + this.cardName.substring(1), LocaleController.getString("PaymentCheckoutMethod", R.string.PaymentCheckoutMethod), R.drawable.msg_payment_card, true);
            if (this.isCheckoutPreview) {
            }
            this.detailSettingsCell[r11].setVisibility(i);
            this.linearLayout2.addView(this.detailSettingsCell[r11]);
            if (this.currentStep == 4) {
            }
            tLRPC$User = null;
            while (i2 < this.paymentForm.users.size()) {
            }
            this.detailSettingsCell[1] = new TextDetailSettingsCell(context);
            this.detailSettingsCell[1].setBackground(Theme.getSelectorDrawable(true));
            if (tLRPC$User == null) {
            }
            this.detailSettingsCell[1].setVisibility(tLRPC$User == null ? i : 8);
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
            this.detailSettingsCell[c] = new TextDetailSettingsCell(context);
            this.detailSettingsCell[c].setVisibility(8);
            this.linearLayout2.addView(this.detailSettingsCell[c]);
            if (tLRPC$TL_paymentRequestedInfo.email != null) {
            }
            if (this.shippingOption != null) {
            }
            setAddressFields(tLRPC$TL_paymentRequestedInfo);
            if (this.currentStep == 4) {
            }
            this.sectionCell[1] = new ShadowSectionCell(context, this.resourcesProvider);
            this.sectionCell[1].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            if (i != 0) {
                this.sectionCell[1].setVisibility(i);
            }
            this.linearLayout2.addView(this.sectionCell[1], LayoutHelper.createLinear(-1, -2));
        } else if (i7 == 6) {
            EditTextSettingsCell editTextSettingsCell = new EditTextSettingsCell(context);
            this.codeFieldCell = editTextSettingsCell;
            editTextSettingsCell.setTextAndHint("", LocaleController.getString("PasswordCode", R.string.PasswordCode), false);
            this.codeFieldCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
            EditTextBoldCursor textView5 = this.codeFieldCell.getTextView();
            textView5.setInputType(3);
            textView5.setImeOptions(6);
            textView5.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda26
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView6, int i33, KeyEvent keyEvent) {
                    boolean lambda$createView$25;
                    lambda$createView$25 = PaymentFormActivity.this.lambda$createView$25(textView6, i33, keyEvent);
                    return lambda$createView$25;
                }
            });
            textView5.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.19
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i33, int i34, int i35) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i33, int i34, int i35) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    if (PaymentFormActivity.this.emailCodeLength == 0 || editable.length() != PaymentFormActivity.this.emailCodeLength) {
                        return;
                    }
                    PaymentFormActivity.this.sendSavePassword(false);
                }
            });
            this.linearLayout2.addView(this.codeFieldCell, LayoutHelper.createLinear(-1, -2));
            this.bottomCell[2] = new TextInfoPrivacyCell(context, this.resourcesProvider);
            this.bottomCell[2].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
            this.linearLayout2.addView(this.bottomCell[2], LayoutHelper.createLinear(-1, -2));
            this.settingsCell[1] = new TextSettingsCell(context, this.resourcesProvider);
            this.settingsCell[1].setBackgroundDrawable(Theme.getSelectorDrawable(true));
            TextSettingsCell textSettingsCell = this.settingsCell[1];
            int i33 = Theme.key_windowBackgroundWhiteBlackText;
            textSettingsCell.setTag(Integer.valueOf(i33));
            this.settingsCell[1].setTextColor(getThemedColor(i33));
            this.settingsCell[1].setText(LocaleController.getString("ResendCode", R.string.ResendCode), true);
            this.linearLayout2.addView(this.settingsCell[1], LayoutHelper.createLinear(-1, -2));
            this.settingsCell[1].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda8
                @Override // android.view.View.OnClickListener
                public final void onClick(View view5) {
                    PaymentFormActivity.this.lambda$createView$27(view5);
                }
            });
            this.settingsCell[0] = new TextSettingsCell(context, this.resourcesProvider);
            this.settingsCell[0].setBackgroundDrawable(Theme.getSelectorDrawable(true));
            TextSettingsCell textSettingsCell2 = this.settingsCell[0];
            int i34 = Theme.key_text_RedRegular;
            textSettingsCell2.setTag(Integer.valueOf(i34));
            this.settingsCell[0].setTextColor(getThemedColor(i34));
            this.settingsCell[0].setText(LocaleController.getString("AbortPassword", R.string.AbortPassword), false);
            this.linearLayout2.addView(this.settingsCell[0], LayoutHelper.createLinear(-1, -2));
            this.settingsCell[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda17
                @Override // android.view.View.OnClickListener
                public final void onClick(View view5) {
                    PaymentFormActivity.this.lambda$createView$29(view5);
                }
            });
            this.inputFields = new EditTextBoldCursor[3];
            int i35 = 0;
            for (int i36 = 3; i35 < i36; i36 = 3) {
                if (i35 == 0) {
                    this.headerCell[0] = new HeaderCell(context, this.resourcesProvider);
                    this.headerCell[0].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    this.headerCell[0].setText(LocaleController.getString("PaymentPasswordTitle", R.string.PaymentPasswordTitle));
                    this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                } else if (i35 == 2) {
                    this.headerCell[1] = new HeaderCell(context, this.resourcesProvider);
                    this.headerCell[1].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    this.headerCell[1].setText(LocaleController.getString("PaymentPasswordEmailTitle", R.string.PaymentPasswordEmailTitle));
                    this.linearLayout2.addView(this.headerCell[1], LayoutHelper.createLinear(-1, -2));
                }
                FrameLayout frameLayout9 = new FrameLayout(context);
                frameLayout9.setClipChildren(false);
                this.linearLayout2.addView(frameLayout9, LayoutHelper.createLinear(-1, 50));
                int i37 = Theme.key_windowBackgroundWhite;
                frameLayout9.setBackgroundColor(getThemedColor(i37));
                if (i35 == 0) {
                    View view5 = new View(this, context) { // from class: org.telegram.ui.PaymentFormActivity.20
                        @Override // android.view.View
                        protected void onDraw(Canvas canvas) {
                            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                        }
                    };
                    view5.setBackgroundColor(getThemedColor(i37));
                    this.dividers.add(view5);
                    frameLayout9.addView(view5, new FrameLayout.LayoutParams(-1, 1, 83));
                }
                this.inputFields[i35] = new EditTextBoldCursor(context);
                this.inputFields[i35].setTag(Integer.valueOf(i35));
                this.inputFields[i35].setTextSize(1, 16.0f);
                this.inputFields[i35].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                EditTextBoldCursor editTextBoldCursor7 = this.inputFields[i35];
                int i38 = Theme.key_windowBackgroundWhiteBlackText;
                editTextBoldCursor7.setTextColor(getThemedColor(i38));
                this.inputFields[i35].setBackgroundDrawable(null);
                this.inputFields[i35].setCursorColor(getThemedColor(i38));
                this.inputFields[i35].setCursorSize(AndroidUtilities.dp(20.0f));
                this.inputFields[i35].setCursorWidth(1.5f);
                if (i35 == 0 || i35 == 1) {
                    this.inputFields[i35].setInputType(129);
                    this.inputFields[i35].setTypeface(Typeface.DEFAULT);
                    this.inputFields[i35].setImeOptions(268435461);
                } else {
                    this.inputFields[i35].setInputType(33);
                    this.inputFields[i35].setImeOptions(268435462);
                }
                if (i35 == 0) {
                    this.inputFields[i35].setHint(LocaleController.getString("PaymentPasswordEnter", R.string.PaymentPasswordEnter));
                    this.inputFields[i35].requestFocus();
                } else if (i35 == 1) {
                    this.inputFields[i35].setHint(LocaleController.getString("PaymentPasswordReEnter", R.string.PaymentPasswordReEnter));
                } else if (i35 == 2) {
                    this.inputFields[i35].setHint(LocaleController.getString("PaymentPasswordEmail", R.string.PaymentPasswordEmail));
                }
                this.inputFields[i35].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                this.inputFields[i35].setGravity(LocaleController.isRTL ? 5 : 3);
                frameLayout9.addView(this.inputFields[i35], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                this.inputFields[i35].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda29
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView6, int i39, KeyEvent keyEvent) {
                        boolean lambda$createView$30;
                        lambda$createView$30 = PaymentFormActivity.this.lambda$createView$30(textView6, i39, keyEvent);
                        return lambda$createView$30;
                    }
                });
                if (i35 == 1) {
                    this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                    this.bottomCell[0].setText(LocaleController.getString("PaymentPasswordInfo", R.string.PaymentPasswordInfo));
                    this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                } else if (i35 == 2) {
                    this.bottomCell[1] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                    this.bottomCell[1].setText(LocaleController.getString("PaymentPasswordEmailInfo", R.string.PaymentPasswordEmailInfo));
                    this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawableByKey(context, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    this.linearLayout2.addView(this.bottomCell[1], LayoutHelper.createLinear(-1, -2));
                }
                i35++;
            }
            updatePasswordFields();
        }
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$1(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setDisableAnonymousNumbers(true);
            countrySelectActivity.setCountrySelectActivityDelegate(new CountrySelectActivity.CountrySelectActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda66
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
    public /* synthetic */ void lambda$createView$0(CountrySelectActivity.Country country) {
        this.country = country;
        this.inputFields[4].setText(country.name);
        this.countryName = country.shortname;
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
    public /* synthetic */ void lambda$createView$3(View view) {
        boolean z = !this.saveShippingInfo;
        this.saveShippingInfo = z;
        this.checkCell1.setChecked(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(View view) {
        boolean z = !this.saveCardInfo;
        this.saveCardInfo = z;
        this.checkCell1.setChecked(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$6(View view, MotionEvent motionEvent) {
        if (getParentActivity() == null) {
            return false;
        }
        if (motionEvent.getAction() == 1) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(false);
            countrySelectActivity.setDisableAnonymousNumbers(true);
            countrySelectActivity.setCountrySelectActivityDelegate(new CountrySelectActivity.CountrySelectActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda67
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
    public /* synthetic */ void lambda$createView$5(CountrySelectActivity.Country country) {
        this.country = country;
        this.inputFields[4].setText(country.name);
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
            intValue++;
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            if (intValue < editTextBoldCursorArr.length) {
                if (intValue == 4) {
                    intValue++;
                }
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
        if (l != null && longValue == l.longValue()) {
            this.ignoreOnTextChange = true;
            this.inputFields[0].setText("");
            this.ignoreOnTextChange = false;
            this.tipAmount = 0L;
            updateTotalPrice();
        } else {
            this.inputFields[0].setText(LocaleController.getInstance().formatCurrencyString(j, false, true, true, this.paymentForm.invoice.currency));
        }
        EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
        editTextBoldCursorArr[0].setSelection(editTextBoldCursorArr[0].length());
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
        PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        paymentFormActivity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.13
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password) {
                PaymentFormActivityDelegate.-CC.$default$currentPasswordUpdated(this, tLRPC$account_Password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                return PaymentFormActivityDelegate.-CC.$default$didSelectNewCard(this, str, str2, z, tLRPC$TL_inputPaymentCredentialsGooglePay, tLRPC$TL_paymentSavedCredentialsCard);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.-CC.$default$onFragmentDestroyed(this);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
                PaymentFormActivity.this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
                PaymentFormActivity paymentFormActivity2 = PaymentFormActivity.this;
                paymentFormActivity2.setAddressFields(paymentFormActivity2.validateRequest.info);
            }
        });
        presentFragment(paymentFormActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$18(View view) {
        PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        paymentFormActivity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.14
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password) {
                PaymentFormActivityDelegate.-CC.$default$currentPasswordUpdated(this, tLRPC$account_Password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                return PaymentFormActivityDelegate.-CC.$default$didSelectNewCard(this, str, str2, z, tLRPC$TL_inputPaymentCredentialsGooglePay, tLRPC$TL_paymentSavedCredentialsCard);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.-CC.$default$onFragmentDestroyed(this);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
                PaymentFormActivity.this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
                PaymentFormActivity paymentFormActivity2 = PaymentFormActivity.this;
                paymentFormActivity2.setAddressFields(paymentFormActivity2.validateRequest.info);
            }
        });
        presentFragment(paymentFormActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$19(View view) {
        PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        paymentFormActivity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.15
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password) {
                PaymentFormActivityDelegate.-CC.$default$currentPasswordUpdated(this, tLRPC$account_Password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                return PaymentFormActivityDelegate.-CC.$default$didSelectNewCard(this, str, str2, z, tLRPC$TL_inputPaymentCredentialsGooglePay, tLRPC$TL_paymentSavedCredentialsCard);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.-CC.$default$onFragmentDestroyed(this);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
                PaymentFormActivity.this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
                PaymentFormActivity paymentFormActivity2 = PaymentFormActivity.this;
                paymentFormActivity2.setAddressFields(paymentFormActivity2.validateRequest.info);
            }
        });
        presentFragment(paymentFormActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$20(View view) {
        PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        paymentFormActivity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.16
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password) {
                PaymentFormActivityDelegate.-CC.$default$currentPasswordUpdated(this, tLRPC$account_Password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                return PaymentFormActivityDelegate.-CC.$default$didSelectNewCard(this, str, str2, z, tLRPC$TL_inputPaymentCredentialsGooglePay, tLRPC$TL_paymentSavedCredentialsCard);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.-CC.$default$onFragmentDestroyed(this);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
                PaymentFormActivity.this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
                PaymentFormActivity paymentFormActivity2 = PaymentFormActivity.this;
                paymentFormActivity2.setAddressFields(paymentFormActivity2.validateRequest.info);
            }
        });
        presentFragment(paymentFormActivity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0061, code lost:
        if (r7.phone_requested == false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x006d, code lost:
        if (r21.googlePayCredentials == null) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0077, code lost:
        if (r2.invoice.flexible != false) goto L33;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createView$23(String str, final View view) {
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
            sendSavedForm(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$createView$21(view);
                }
            });
            return;
        }
        if (z) {
            TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm = this.paymentForm;
            TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo = tLRPC$TL_payments_paymentForm.saved_info;
            if (tLRPC$TL_paymentRequestedInfo == null) {
                TLRPC$TL_invoice tLRPC$TL_invoice = tLRPC$TL_payments_paymentForm.invoice;
                if (!tLRPC$TL_invoice.shipping_address_requested) {
                    if (!tLRPC$TL_invoice.email_requested) {
                        if (!tLRPC$TL_invoice.name_requested) {
                        }
                    }
                }
                if (tLRPC$TL_paymentRequestedInfo == null) {
                    TLRPC$TL_invoice tLRPC$TL_invoice2 = tLRPC$TL_payments_paymentForm.invoice;
                    if (tLRPC$TL_invoice2.shipping_address_requested || tLRPC$TL_invoice2.email_requested || tLRPC$TL_invoice2.name_requested || tLRPC$TL_invoice2.phone_requested) {
                        i = 0;
                        if (i != 2 && !tLRPC$TL_payments_paymentForm.additional_methods.isEmpty()) {
                            Objects.requireNonNull(view);
                            showChoosePaymentMethod(new ChatActivityEnterView$$ExternalSyntheticLambda31(view));
                            return;
                        }
                        presentFragment(new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, i, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment));
                        return;
                    }
                }
                i = (this.savedCredentialsCard == null && this.paymentJson == null && this.googlePayCredentials == null) ? 2 : 1;
                if (i != 2) {
                }
                presentFragment(new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, i, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment));
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
                presentFragment(new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, 3, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment));
                this.needPayAfterTransition = false;
                return;
            } else if (this.isCheckoutPreview) {
                this.isCheckoutPreview = false;
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.paymentFinished);
            }
        }
        TLRPC$User tLRPC$User = this.botUser;
        if (tLRPC$User != null && !tLRPC$User.verified) {
            String str2 = "payment_warning_" + this.botUser.id;
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            if (!notificationsSettings.getBoolean(str2, false)) {
                notificationsSettings.edit().putBoolean(str2, true).commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("PaymentWarning", R.string.PaymentWarning));
                builder.setMessage(LocaleController.formatString("PaymentWarningText", R.string.PaymentWarningText, this.currentBotName, str));
                builder.setPositiveButton(LocaleController.getString("Continue", R.string.Continue), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        PaymentFormActivity.this.lambda$createView$22(dialogInterface, i2);
                    }
                });
                showDialog(builder.create());
                return;
            }
            showPayAlert(this.totalPrice[0]);
            return;
        }
        showPayAlert(this.totalPrice[0]);
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
    public /* synthetic */ void lambda$createView$27(View view) {
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_resendPasswordEmail(), PaymentFormActivity$$ExternalSyntheticLambda64.INSTANCE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(LocaleController.getString("ResendCodeInfo", R.string.ResendCodeInfo));
        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$29(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        String string = LocaleController.getString("TurnPasswordOffQuestion", R.string.TurnPasswordOffQuestion);
        if (this.currentPassword.has_secure_values) {
            string = string + "\n\n" + LocaleController.getString("TurnPasswordOffPassport", R.string.TurnPasswordOffPassport);
        }
        builder.setMessage(string);
        builder.setTitle(LocaleController.getString("TurnPasswordOffQuestionTitle", R.string.TurnPasswordOffQuestionTitle));
        builder.setPositiveButton(LocaleController.getString("Disable", R.string.Disable), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PaymentFormActivity.this.lambda$createView$28(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        AlertDialog create = builder.create();
        showDialog(create);
        TextView textView = (TextView) create.getButton(-1);
        if (textView != null) {
            textView.setTextColor(getThemedColor(Theme.key_text_RedBold));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$28(DialogInterface dialogInterface, int i) {
        sendSavePassword(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$30(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            this.doneItem.performClick();
            return true;
        } else if (i == 5) {
            int intValue = ((Integer) textView.getTag()).intValue();
            if (intValue == 0) {
                this.inputFields[1].requestFocus();
                return false;
            } else if (intValue == 1) {
                this.inputFields[2].requestFocus();
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void showChoosePaymentMethod() {
        showChoosePaymentMethod(null);
    }

    private void showChoosePaymentMethod(final Runnable runnable) {
        BottomSheet.Builder title = new BottomSheet.Builder(getParentActivity()).setTitle(LocaleController.getString("PaymentCheckoutMethod", R.string.PaymentCheckoutMethod), true);
        final ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard = this.savedCredentialsCard;
        if (tLRPC$TL_paymentSavedCredentialsCard != null) {
            arrayList.add(tLRPC$TL_paymentSavedCredentialsCard.title);
            arrayList2.add(Integer.valueOf(R.drawable.msg_payment_card));
        } else {
            String str = this.cardName;
            if (str != null) {
                arrayList.add(str);
                arrayList2.add(Integer.valueOf(R.drawable.msg_payment_card));
            }
        }
        final ArrayList arrayList3 = new ArrayList();
        Iterator<TLRPC$TL_paymentSavedCredentialsCard> it = this.paymentForm.saved_credentials.iterator();
        while (it.hasNext()) {
            TLRPC$TL_paymentSavedCredentialsCard next = it.next();
            TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard2 = this.savedCredentialsCard;
            if (tLRPC$TL_paymentSavedCredentialsCard2 == null || !Objects.equals(next.id, tLRPC$TL_paymentSavedCredentialsCard2.id)) {
                arrayList.add(next.title);
                arrayList2.add(Integer.valueOf(R.drawable.msg_payment_card));
                arrayList3.add(next);
            }
        }
        Iterator<TLRPC$TL_paymentFormMethod> it2 = this.paymentForm.additional_methods.iterator();
        while (it2.hasNext()) {
            arrayList.add(it2.next().title);
            arrayList2.add(Integer.valueOf(R.drawable.msg_payment_provider));
        }
        arrayList.add(LocaleController.getString(R.string.PaymentCheckoutMethodNewCard));
        arrayList2.add(Integer.valueOf(R.drawable.msg_addbot));
        int[] iArr = new int[arrayList2.size()];
        for (int i = 0; i < arrayList2.size(); i++) {
            iArr[i] = ((Integer) arrayList2.get(i)).intValue();
        }
        title.setItems((CharSequence[]) arrayList.toArray(new CharSequence[0]), iArr, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                PaymentFormActivity.this.lambda$showChoosePaymentMethod$31(runnable, arrayList3, arrayList, dialogInterface, i2);
            }
        });
        showDialog(title.create());
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
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.-CC.$default$onFragmentDestroyed(this);
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
                    textDetailSettingsCell.setTextAndValueAndIcon(str3, LocaleController.getString("PaymentCheckoutMethod", R.string.PaymentCheckoutMethod), R.drawable.msg_payment_card, true);
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
            PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, 2, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
            paymentFormActivity.setPaymentMethod(this.paymentForm.additional_methods.get((i - list.size()) - i2));
            paymentFormActivity.setDelegate(paymentFormActivityDelegate);
            presentFragment(paymentFormActivity);
        } else if (i == list2.size() - 1) {
            PaymentFormActivity paymentFormActivity2 = new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, 2, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
            paymentFormActivity2.setDelegate(paymentFormActivityDelegate);
            presentFragment(paymentFormActivity2);
        }
    }

    private void setPaymentMethod(TLRPC$TL_paymentFormMethod tLRPC$TL_paymentFormMethod) {
        this.paymentFormMethod = tLRPC$TL_paymentFormMethod;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAddressFields(TLRPC$TL_paymentRequestedInfo tLRPC$TL_paymentRequestedInfo) {
        TLRPC$TL_postAddress tLRPC$TL_postAddress = tLRPC$TL_paymentRequestedInfo.shipping_address;
        if (tLRPC$TL_postAddress != null) {
            this.detailSettingsCell[2].setTextAndValueAndIcon(String.format("%s %s, %s, %s, %s, %s", tLRPC$TL_postAddress.street_line1, tLRPC$TL_postAddress.street_line2, tLRPC$TL_postAddress.city, tLRPC$TL_postAddress.state, tLRPC$TL_postAddress.country_iso2, tLRPC$TL_postAddress.post_code), LocaleController.getString("PaymentShippingAddress", R.string.PaymentShippingAddress), R.drawable.msg_payment_address, true);
        }
        this.detailSettingsCell[2].setVisibility(tLRPC$TL_paymentRequestedInfo.shipping_address != null ? 0 : 8);
        String str = tLRPC$TL_paymentRequestedInfo.name;
        if (str != null) {
            this.detailSettingsCell[3].setTextAndValueAndIcon(str, LocaleController.getString("PaymentCheckoutName", R.string.PaymentCheckoutName), R.drawable.msg_contacts, true);
        }
        this.detailSettingsCell[3].setVisibility(tLRPC$TL_paymentRequestedInfo.name != null ? 0 : 8);
        if (tLRPC$TL_paymentRequestedInfo.phone != null) {
            this.detailSettingsCell[4].setTextAndValueAndIcon(PhoneFormat.getInstance().format(tLRPC$TL_paymentRequestedInfo.phone), LocaleController.getString("PaymentCheckoutPhoneNumber", R.string.PaymentCheckoutPhoneNumber), R.drawable.msg_calls, (tLRPC$TL_paymentRequestedInfo.email == null && this.shippingOption == null) ? false : true);
        }
        this.detailSettingsCell[4].setVisibility(tLRPC$TL_paymentRequestedInfo.phone != null ? 0 : 8);
        String str2 = tLRPC$TL_paymentRequestedInfo.email;
        if (str2 != null) {
            this.detailSettingsCell[5].setTextAndValueAndIcon(str2, LocaleController.getString("PaymentCheckoutEmail", R.string.PaymentCheckoutEmail), R.drawable.msg_mention, this.shippingOption != null);
        }
        this.detailSettingsCell[5].setVisibility(tLRPC$TL_paymentRequestedInfo.email == null ? 8 : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTotalPrice() {
        this.totalPrice[0] = getTotalPriceString(this.prices);
        this.totalCell.setTextAndValue(LocaleController.getString("PaymentTransactionTotal", R.string.PaymentTransactionTotal), this.totalPrice[0], true);
        TextView textView = this.payTextView;
        if (textView != null) {
            textView.setText(LocaleController.formatString("PaymentCheckoutPay", R.string.PaymentCheckoutPay, this.totalPrice[0]));
        }
        if (this.tipLayout != null) {
            int themedColor = getThemedColor(Theme.key_contacts_inviteBackground);
            int childCount = this.tipLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                TextView textView2 = (TextView) this.tipLayout.getChildAt(i);
                if (textView2.getTag().equals(this.tipAmount)) {
                    Theme.setDrawableColor(textView2.getBackground(), themedColor);
                    textView2.setTextColor(getThemedColor(Theme.key_contacts_inviteText));
                } else {
                    Theme.setDrawableColor(textView2.getBackground(), 536870911 & themedColor);
                    textView2.setTextColor(getThemedColor(Theme.key_chats_secretName));
                }
                textView2.invalidate();
            }
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
        this.googlePayButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda5
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

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGooglePayButton$32(View view) {
        this.googlePayButton.setClickable(false);
        try {
            JSONObject baseRequest = getBaseRequest();
            JSONObject baseCardPaymentMethod = getBaseCardPaymentMethod();
            if (this.googlePayPublicKey != null && this.googlePayParameters == null) {
                baseCardPaymentMethod.put("tokenizationSpecification", new JSONObject() { // from class: org.telegram.ui.PaymentFormActivity.22
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
            } else {
                baseCardPaymentMethod.put("tokenizationSpecification", new JSONObject() { // from class: org.telegram.ui.PaymentFormActivity.23
                    {
                        put("type", "PAYMENT_GATEWAY");
                        if (PaymentFormActivity.this.googlePayParameters != null) {
                            put("parameters", PaymentFormActivity.this.googlePayParameters);
                        } else {
                            put("parameters", new JSONObject() { // from class: org.telegram.ui.PaymentFormActivity.23.1
                                {
                                    put("gateway", "stripe");
                                    put("stripe:publishableKey", PaymentFormActivity.this.providerApiKey);
                                    put("stripe:version", "3.5.0");
                                }
                            });
                        }
                    }
                });
            }
            baseRequest.put("allowedPaymentMethods", new JSONArray().put(baseCardPaymentMethod));
            JSONObject jSONObject = new JSONObject();
            ArrayList<TLRPC$TL_labeledPrice> arrayList = new ArrayList<>(this.paymentForm.invoice.prices);
            TLRPC$TL_shippingOption tLRPC$TL_shippingOption = this.shippingOption;
            if (tLRPC$TL_shippingOption != null) {
                arrayList.addAll(tLRPC$TL_shippingOption.prices);
            }
            jSONObject.put("totalPrice", getTotalPriceDecimalString(arrayList));
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

    private void updatePasswordFields() {
        if (this.currentStep != 6 || this.bottomCell[2] == null) {
            return;
        }
        int i = 0;
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
                this.dividers.get(i).setVisibility(8);
                i++;
            }
            return;
        }
        showEditDoneProgress(true, false);
        if (this.waitingForEmail) {
            TextInfoPrivacyCell textInfoPrivacyCell = this.bottomCell[2];
            int i3 = R.string.EmailPasswordConfirmText2;
            Object[] objArr = new Object[1];
            String str = this.currentPassword.email_unconfirmed_pattern;
            if (str == null) {
                str = "";
            }
            objArr[0] = str;
            textInfoPrivacyCell.setText(LocaleController.formatString("EmailPasswordConfirmText2", i3, objArr));
            this.bottomCell[2].setVisibility(0);
            this.settingsCell[0].setVisibility(0);
            this.settingsCell[1].setVisibility(0);
            this.codeFieldCell.setVisibility(0);
            this.bottomCell[1].setText("");
            this.headerCell[0].setVisibility(8);
            this.headerCell[1].setVisibility(8);
            this.bottomCell[0].setVisibility(8);
            for (int i4 = 0; i4 < 3; i4++) {
                ((View) this.inputFields[i4].getParent()).setVisibility(8);
            }
            while (i < this.dividers.size()) {
                this.dividers.get(i).setVisibility(8);
                i++;
            }
            return;
        }
        this.bottomCell[2].setVisibility(8);
        this.settingsCell[0].setVisibility(8);
        this.settingsCell[1].setVisibility(8);
        this.bottomCell[1].setText(LocaleController.getString("PaymentPasswordEmailInfo", R.string.PaymentPasswordEmailInfo));
        this.codeFieldCell.setVisibility(8);
        this.headerCell[0].setVisibility(0);
        this.headerCell[1].setVisibility(0);
        this.bottomCell[0].setVisibility(0);
        for (int i5 = 0; i5 < 3; i5++) {
            ((View) this.inputFields[i5].getParent()).setVisibility(0);
        }
        for (int i6 = 0; i6 < this.dividers.size(); i6++) {
            this.dividers.get(i6).setVisibility(0);
        }
    }

    private void loadPasswordInfo() {
        if (this.loadingPasswordInfo) {
            return;
        }
        this.loadingPasswordInfo = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda56
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$loadPasswordInfo$35(tLObject, tLRPC$TL_error);
            }
        }, 10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPasswordInfo$35(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$loadPasswordInfo$34(tLRPC$TL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPasswordInfo$34(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        this.loadingPasswordInfo = false;
        if (tLRPC$TL_error == null) {
            TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            this.currentPassword = tLRPC$account_Password;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$account_Password, false)) {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
                return;
            }
            TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm = this.paymentForm;
            if (tLRPC$TL_payments_paymentForm != null && this.currentPassword.has_password) {
                tLRPC$TL_payments_paymentForm.password_missing = false;
                tLRPC$TL_payments_paymentForm.can_save_credentials = true;
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
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda32
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
    public /* synthetic */ void lambda$loadPasswordInfo$33() {
        if (this.shortPollRunnable == null) {
            return;
        }
        loadPasswordInfo();
        this.shortPollRunnable = null;
    }

    private void showAlertWithText(String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        builder.setTitle(str);
        builder.setMessage(str2);
        showDialog(builder.create());
    }

    private void showPayAlert(String str) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("PaymentTransactionReview", R.string.PaymentTransactionReview));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("PaymentTransactionMessage2", R.string.PaymentTransactionMessage2, str, this.currentBotName, this.currentItemName)));
        builder.setPositiveButton(LocaleController.getString("Continue", R.string.Continue), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PaymentFormActivity.this.lambda$showPayAlert$36(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPayAlert$36(DialogInterface dialogInterface, int i) {
        setDonePressed(true);
        sendData();
    }

    private JSONObject getBaseRequest() throws JSONException {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }

    private JSONObject getBaseCardPaymentMethod() throws JSONException {
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

    public Optional<JSONObject> getIsReadyToPayRequest() {
        try {
            JSONObject baseRequest = getBaseRequest();
            baseRequest.put("allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));
            return Optional.of(baseRequest);
        } catch (JSONException unused) {
            return Optional.empty();
        }
    }

    private void initGooglePay(Context context) {
        IsReadyToPayRequest fromJson;
        if (Build.VERSION.SDK_INT < 19 || getParentActivity() == null) {
            return;
        }
        this.paymentsClient = Wallet.getPaymentsClient(context, new Wallet.WalletOptions.Builder().setEnvironment(this.paymentForm.invoice.test ? 3 : 1).setTheme(1).build());
        Optional<JSONObject> isReadyToPayRequest = getIsReadyToPayRequest();
        if (isReadyToPayRequest.isPresent() && (fromJson = IsReadyToPayRequest.fromJson(isReadyToPayRequest.get().toString())) != null) {
            this.paymentsClient.isReadyToPay(fromJson).addOnCompleteListener(getParentActivity(), new OnCompleteListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda31
                @Override // com.google.android.gms.tasks.OnCompleteListener
                public final void onComplete(Task task) {
                    PaymentFormActivity.this.lambda$initGooglePay$37(task);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initGooglePay$37(Task task) {
        if (task.isSuccessful()) {
            FrameLayout frameLayout = this.googlePayContainer;
            if (frameLayout != null) {
                frameLayout.setVisibility(0);
                return;
            }
            return;
        }
        FileLog.e("isReadyToPay failed", task.getException());
    }

    private String getTotalPriceString(ArrayList<TLRPC$TL_labeledPrice> arrayList) {
        long j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            j += arrayList.get(i).amount;
        }
        Long l = this.tipAmount;
        if (l != null) {
            j += l.longValue();
        }
        return LocaleController.getInstance().formatCurrencyString(j, this.paymentForm.invoice.currency);
    }

    private String getTotalPriceDecimalString(ArrayList<TLRPC$TL_labeledPrice> arrayList) {
        long j = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            j += arrayList.get(i).amount;
        }
        return LocaleController.getInstance().formatCurrencyDecimalString(j, this.paymentForm.invoice.currency, false);
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
            } else if (this.parentLayout.getFragmentStack().get(i) instanceof PaymentFormActivity) {
                break;
            } else {
                i++;
            }
        }
        return i - indexOf;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
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
            if ((i == 2 || i == 6) && Build.VERSION.SDK_INT >= 23 && (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture)) {
                getParentActivity().getWindow().clearFlags(LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM);
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
        super.onFragmentDestroy();
        this.canceled = true;
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
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (!z || z2) {
            return;
        }
        WebView webView = this.webView;
        if (webView != null) {
            if (this.currentStep != 4) {
                TLRPC$TL_paymentFormMethod tLRPC$TL_paymentFormMethod = this.paymentFormMethod;
                if (tLRPC$TL_paymentFormMethod != null) {
                    String str = tLRPC$TL_paymentFormMethod.url;
                    this.webViewUrl = str;
                    webView.loadUrl(str);
                    return;
                }
                String str2 = this.paymentForm.url;
                this.webViewUrl = str2;
                webView.loadUrl(str2);
                return;
            }
            return;
        }
        int i = this.currentStep;
        if (i == 2) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda35
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$onTransitionAnimationEnd$38();
                }
            }, 100L);
        } else if (i == 3) {
            this.inputFields[1].requestFocus();
            AndroidUtilities.showKeyboard(this.inputFields[1]);
        } else if (i == 4) {
            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
            if (editTextBoldCursorArr != null) {
                editTextBoldCursorArr[0].requestFocus();
            }
        } else if (i != 6 || this.waitingForEmail) {
        } else {
            this.inputFields[0].requestFocus();
            AndroidUtilities.showKeyboard(this.inputFields[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onTransitionAnimationEnd$38() {
        this.inputFields[0].requestFocus();
        AndroidUtilities.showKeyboard(this.inputFields[0]);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.twoStepPasswordChanged) {
            TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm = this.paymentForm;
            tLRPC$TL_payments_paymentForm.password_missing = false;
            tLRPC$TL_payments_paymentForm.can_save_credentials = true;
            updateSavePaymentField();
        } else if (i == NotificationCenter.didRemoveTwoStepPassword) {
            TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm2 = this.paymentForm;
            tLRPC$TL_payments_paymentForm2.password_missing = true;
            tLRPC$TL_payments_paymentForm2.can_save_credentials = false;
            updateSavePaymentField();
        } else if (i == NotificationCenter.paymentFinished) {
            this.paymentStatusSent = true;
            removeSelfFromStack();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, final int i2, final Intent intent) {
        if (i == 991) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda36
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$onActivityResultFragment$39(i2, intent);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r2v5, types: [org.telegram.tgnet.TLRPC$TL_inputPaymentCredentialsGooglePay, org.telegram.tgnet.TLRPC$InputPaymentCredentials] */
    public /* synthetic */ void lambda$onActivityResultFragment$39(int i, Intent intent) {
        String json;
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
                    this.cardName = card.getBrand() + " *" + card.getLast4();
                    goToNextStep();
                }
                ?? r2 = new TLRPC$InputPaymentCredentials() { // from class: org.telegram.tgnet.TLRPC$TL_inputPaymentCredentialsGooglePay
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
                this.googlePayCredentials = r2;
                r2.payment_token = new TLRPC$TL_dataJSON();
                this.googlePayCredentials.payment_token.data = jSONObject2.toString();
                String optString = jSONObject.optString("description");
                if (!TextUtils.isEmpty(optString)) {
                    this.cardName = optString;
                } else {
                    this.cardName = "Android Pay";
                }
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
    public void goToNextStep() {
        int i;
        int i2;
        boolean z;
        int i3 = this.currentStep;
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
                showChoosePaymentMethod(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda33
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.this.goToNextStep();
                    }
                });
            } else {
                presentFragment(new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, i, this.requestedInfo, null, null, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment), this.isWebView);
            }
        } else if (i3 == 1) {
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
                    showChoosePaymentMethod(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda33
                        @Override // java.lang.Runnable
                        public final void run() {
                            PaymentFormActivity.this.goToNextStep();
                        }
                    });
                    return;
                } else {
                    presentFragment(new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, i2, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment), this.isWebView);
                }
            }
            i2 = 4;
            if (i2 != 2) {
            }
            presentFragment(new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, i2, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment), this.isWebView);
        } else if (i3 == 2) {
            TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm = this.paymentForm;
            if (tLRPC$TL_payments_paymentForm.password_missing && (z = this.saveCardInfo)) {
                PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.invoicePremiumGiftCode, tLRPC$TL_payments_paymentForm, this.messageObject, this.invoiceSlug, 6, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, z, this.googlePayCredentials, this.parentFragment);
                this.passwordFragment = paymentFormActivity;
                paymentFormActivity.setCurrentPassword(this.currentPassword);
                this.passwordFragment.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.24
                    @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                    public /* synthetic */ void didSelectNewAddress(TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo) {
                        PaymentFormActivityDelegate.-CC.$default$didSelectNewAddress(this, tLRPC$TL_payments_validateRequestedInfo);
                    }

                    @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                    public boolean didSelectNewCard(String str, String str2, boolean z2, TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay, TLRPC$TL_paymentSavedCredentialsCard tLRPC$TL_paymentSavedCredentialsCard) {
                        if (PaymentFormActivity.this.delegate != null) {
                            PaymentFormActivity.this.delegate.didSelectNewCard(str, str2, z2, tLRPC$TL_inputPaymentCredentialsGooglePay, tLRPC$TL_paymentSavedCredentialsCard);
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

                    @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                    public void currentPasswordUpdated(TLRPC$account_Password tLRPC$account_Password) {
                        PaymentFormActivity.this.currentPassword = tLRPC$account_Password;
                    }
                });
                presentFragment(this.passwordFragment, this.isWebView);
                return;
            }
            PaymentFormActivityDelegate paymentFormActivityDelegate2 = this.delegate;
            if (paymentFormActivityDelegate2 != null) {
                paymentFormActivityDelegate2.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.googlePayCredentials, null);
                finishFragment();
                return;
            }
            presentFragment(new PaymentFormActivity(this.invoicePremiumGiftCode, tLRPC$TL_payments_paymentForm, this.messageObject, this.invoiceSlug, 4, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment), this.isWebView);
        } else if (i3 == 3) {
            presentFragment(new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, this.passwordOk ? 4 : 2, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment), true);
        } else if (i3 != 4) {
            if (i3 != 6) {
                return;
            }
            if (!this.delegate.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.googlePayCredentials, this.savedCredentialsCard)) {
                presentFragment(new PaymentFormActivity(this.invoicePremiumGiftCode, this.paymentForm, this.messageObject, this.invoiceSlug, 4, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment), true);
            } else {
                finishFragment();
            }
        } else {
            if (this.isCheckoutPreview) {
                NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.paymentFinished);
            }
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.paymentFinished, new Object[0]);
            if (getMessagesController().newMessageCallback == null) {
                if (onCheckoutSuccess(getParentLayout(), getParentActivity()) || isFinishing()) {
                    return;
                }
                finishFragment();
                return;
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$goToNextStep$40();
                }
            }, 500L);
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
            finishFragment();
        } else if (this.invoiceStatus != InvoiceStatus.PAID || isFinishing()) {
        } else {
            finishFragment();
        }
    }

    private boolean onCheckoutSuccess(INavigationLayout iNavigationLayout, Activity activity) {
        if (this.invoicePremiumGiftCode != null) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSavePaymentField() {
        if (this.bottomCell[0] == null || this.sectionCell[2] == null) {
            return;
        }
        TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm = this.paymentForm;
        if ((tLRPC$TL_payments_paymentForm.password_missing || tLRPC$TL_payments_paymentForm.can_save_credentials) && (this.webView == null || !this.webviewLoading)) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString("PaymentCardSavePaymentInformationInfoLine1", R.string.PaymentCardSavePaymentInformationInfoLine1));
            if (this.paymentForm.password_missing) {
                loadPasswordInfo();
                spannableStringBuilder.append((CharSequence) "\n");
                int length = spannableStringBuilder.length();
                String string = LocaleController.getString("PaymentCardSavePaymentInformationInfoLine2", R.string.PaymentCardSavePaymentInformationInfoLine2);
                int indexOf = string.indexOf(42);
                int lastIndexOf = string.lastIndexOf(42);
                spannableStringBuilder.append((CharSequence) string);
                if (indexOf != -1 && lastIndexOf != -1) {
                    int i = indexOf + length;
                    int i2 = lastIndexOf + length;
                    this.bottomCell[0].getTextView().setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                    spannableStringBuilder.replace(i2, i2 + 1, (CharSequence) "");
                    spannableStringBuilder.replace(i, i + 1, (CharSequence) "");
                    spannableStringBuilder.setSpan(new LinkSpan(), i, i2 - 1, 33);
                }
            }
            this.checkCell1.setEnabled(true);
            this.bottomCell[0].setText(spannableStringBuilder);
            this.checkCell1.setVisibility(0);
            this.bottomCell[0].setVisibility(0);
            ShadowSectionCell[] shadowSectionCellArr = this.sectionCell;
            shadowSectionCellArr[2].setBackgroundDrawable(Theme.getThemedDrawableByKey(shadowSectionCellArr[2].getContext(), R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
            return;
        }
        this.checkCell1.setVisibility(8);
        this.bottomCell[0].setVisibility(8);
        ShadowSectionCell[] shadowSectionCellArr2 = this.sectionCell;
        shadowSectionCellArr2[2].setBackgroundDrawable(Theme.getThemedDrawableByKey(shadowSectionCellArr2[2].getContext(), R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0036 A[Catch: Exception -> 0x0099, TryCatch #0 {Exception -> 0x0099, blocks: (B:2:0x0000, B:4:0x000d, B:6:0x0013, B:8:0x0019, B:10:0x0020, B:18:0x0036, B:19:0x003e, B:21:0x0045, B:25:0x0050, B:27:0x005e, B:31:0x0071, B:28:0x006b, B:33:0x0082), top: B:38:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0045 A[Catch: Exception -> 0x0099, TryCatch #0 {Exception -> 0x0099, blocks: (B:2:0x0000, B:4:0x000d, B:6:0x0013, B:8:0x0019, B:10:0x0020, B:18:0x0036, B:19:0x003e, B:21:0x0045, B:25:0x0050, B:27:0x005e, B:31:0x0071, B:28:0x006b, B:33:0x0082), top: B:38:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    @SuppressLint({"HardwareIds"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void fillNumber(String str) {
        boolean z;
        boolean z2;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
            if (str == null && (telephonyManager.getSimState() == 1 || telephonyManager.getPhoneType() == 0)) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 23 && getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
                z = false;
                if (str == null || z) {
                    if (str == null) {
                        str = PhoneFormat.stripExceptNumbers(telephonyManager.getLine1Number());
                    }
                    String str2 = null;
                    if (TextUtils.isEmpty(str)) {
                        int i = 4;
                        if (str.length() > 4) {
                            while (true) {
                                if (i < 1) {
                                    z2 = false;
                                    break;
                                }
                                String substring = str.substring(0, i);
                                if (this.codesMap.get(substring) != null) {
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
                            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                            editTextBoldCursorArr[9].setSelection(editTextBoldCursorArr[9].length());
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            z = true;
            if (str == null) {
            }
            if (str == null) {
            }
            String str22 = null;
            if (TextUtils.isEmpty(str)) {
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
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
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_confirmPasswordEmail, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda55
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
            str2 = null;
            str = null;
        } else {
            String obj = this.inputFields[0].getText().toString();
            if (TextUtils.isEmpty(obj)) {
                shakeField(0);
                return;
            } else if (!obj.equals(this.inputFields[1].getText().toString())) {
                try {
                    Toast.makeText(getParentActivity(), LocaleController.getString("PasswordDoNotMatch", R.string.PasswordDoNotMatch), 0).show();
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
                int i = tLRPC$TL_account_passwordInputSettings2.flags | 1;
                tLRPC$TL_account_passwordInputSettings2.flags = i;
                tLRPC$TL_account_passwordInputSettings2.hint = "";
                tLRPC$TL_account_passwordInputSettings2.new_algo = this.currentPassword.new_algo;
                tLRPC$TL_account_passwordInputSettings2.flags = i | 2;
                tLRPC$TL_account_passwordInputSettings2.email = obj2.trim();
                str = obj;
                str2 = obj2;
            }
        }
        showEditDoneProgress(true, true);
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda52
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$sendSavePassword$48(z, str2, str, tLRPC$TL_account_updatePasswordSettings);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$42(TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$sendSavePassword$41(tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$41(TLRPC$TL_error tLRPC$TL_error) {
        String formatPluralString;
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
        } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
            int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
            if (intValue < 60) {
                formatPluralString = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
            } else {
                formatPluralString = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
            }
            showAlertWithText(LocaleController.getString("AppName", R.string.AppName), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString));
        } else {
            showAlertWithText(LocaleController.getString("AppName", R.string.AppName), tLRPC$TL_error.text);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$47(final boolean z, final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda49
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$sendSavePassword$46(tLRPC$TL_error, z, tLObject, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$48(final boolean z, final String str, String str2, TLRPC$TL_account_updatePasswordSettings tLRPC$TL_account_updatePasswordSettings) {
        RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda63
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$sendSavePassword$47(z, str, tLObject, tLRPC$TL_error);
            }
        };
        if (!z) {
            byte[] stringBytes = AndroidUtilities.getStringBytes(str2);
            TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = this.currentPassword.new_algo;
            if (tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
                tLRPC$TL_account_updatePasswordSettings.new_settings.new_password_hash = SRPHelper.getVBytes(stringBytes, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo);
                if (tLRPC$TL_account_updatePasswordSettings.new_settings.new_password_hash == null) {
                    TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
                    tLRPC$TL_error.text = "ALGO_INVALID";
                    requestDelegate.run(null, tLRPC$TL_error);
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_updatePasswordSettings, requestDelegate, 10);
                return;
            }
            TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
            tLRPC$TL_error2.text = "PASSWORD_HASH_INVALID";
            requestDelegate.run(null, tLRPC$TL_error2);
            return;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_updatePasswordSettings, requestDelegate, 10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$46(TLRPC$TL_error tLRPC$TL_error, final boolean z, TLObject tLObject, final String str) {
        String formatPluralString;
        if (tLRPC$TL_error != null && "SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda62
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
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda4
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        PaymentFormActivity.this.lambda$sendSavePassword$45(str, dialogInterface, i);
                    }
                });
                builder.setMessage(LocaleController.getString("YourEmailAlmostThereText", R.string.YourEmailAlmostThereText));
                builder.setTitle(LocaleController.getString("YourEmailAlmostThere", R.string.YourEmailAlmostThere));
                Dialog showDialog = showDialog(builder.create());
                if (showDialog != null) {
                    showDialog.setCanceledOnTouchOutside(false);
                    showDialog.setCancelable(false);
                }
            } else if (tLRPC$TL_error.text.equals("EMAIL_INVALID")) {
                showAlertWithText(LocaleController.getString("AppName", R.string.AppName), LocaleController.getString("PasswordEmailInvalid", R.string.PasswordEmailInvalid));
            } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                if (intValue < 60) {
                    formatPluralString = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                } else {
                    formatPluralString = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                }
                showAlertWithText(LocaleController.getString("AppName", R.string.AppName), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString));
            } else {
                showAlertWithText(LocaleController.getString("AppName", R.string.AppName), tLRPC$TL_error.text);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavePassword$44(final boolean z, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$sendSavePassword$43(tLRPC$TL_error, tLObject, z);
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
    public /* synthetic */ void lambda$sendSavePassword$45(String str, DialogInterface dialogInterface, int i) {
        this.waitingForEmail = true;
        this.currentPassword.email_unconfirmed_pattern = str;
        updatePasswordFields();
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
                    new AsyncTask<Object, Object, String>() { // from class: org.telegram.ui.PaymentFormActivity.26
                        /* JADX INFO: Access modifiers changed from: protected */
                        /* JADX WARN: Can't wrap try/catch for region: R(17:3|4|(12:45|46|(3:48|49|(1:51))|(1:8)(2:41|(1:43)(1:44))|9|10|11|12|13|14|15|(4:23|(1:25)|26|27)(3:19|20|21))|6|(0)(0)|9|10|11|12|13|14|15|(1:17)|23|(0)|26|27) */
                        /* JADX WARN: Code restructure failed: missing block: B:10:0x009d, code lost:
                            if (r4.endsWith(".smart-glocal.com/cds/v1/tokenize/card") != false) goto L6;
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:42:0x016c, code lost:
                            r13 = e;
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:48:0x0172, code lost:
                            org.telegram.messenger.FileLog.e(r13);
                         */
                        /* JADX WARN: Code restructure failed: missing block: B:49:0x0175, code lost:
                            if (r4 == null) goto L27;
                         */
                        /* JADX WARN: Not initialized variable reg: 4, insn: 0x017c: MOVE  (r2 I:??[OBJECT, ARRAY]) = (r4 I:??[OBJECT, ARRAY]), block:B:53:0x017c */
                        /* JADX WARN: Removed duplicated region for block: B:15:0x00a5 A[Catch: all -> 0x016e, Exception -> 0x0170, TRY_ENTER, TryCatch #2 {Exception -> 0x0170, blocks: (B:3:0x0008, B:15:0x00a5, B:20:0x00c6, B:16:0x00ab, B:18:0x00b7, B:19:0x00bf), top: B:62:0x0008 }] */
                        /* JADX WARN: Removed duplicated region for block: B:16:0x00ab A[Catch: all -> 0x016e, Exception -> 0x0170, TryCatch #2 {Exception -> 0x0170, blocks: (B:3:0x0008, B:15:0x00a5, B:20:0x00c6, B:16:0x00ab, B:18:0x00b7, B:19:0x00bf), top: B:62:0x0008 }] */
                        /* JADX WARN: Removed duplicated region for block: B:33:0x0145 A[Catch: Exception -> 0x016c, all -> 0x017b, TRY_LEAVE, TryCatch #3 {Exception -> 0x016c, blocks: (B:22:0x00ce, B:24:0x0105, B:28:0x0114, B:31:0x0141, B:33:0x0145, B:41:0x016b, B:40:0x0168), top: B:64:0x00ce }] */
                        @Override // android.os.AsyncTask
                        /*
                            Code decompiled incorrectly, please refer to instructions dump.
                        */
                        public String doInBackground(Object... objArr) {
                            HttpURLConnection httpURLConnection;
                            HttpURLConnection httpURLConnection2;
                            JSONObject jSONObject;
                            String string;
                            URL url;
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
                                        if (string != null) {
                                            if (PaymentFormActivity.this.paymentForm.invoice.test) {
                                                url = new URL("https://tgb-playground.smart-glocal.com/cds/v1/tokenize/card");
                                            } else {
                                                url = new URL("https://tgb.smart-glocal.com/cds/v1/tokenize/card");
                                            }
                                        } else {
                                            url = new URL(string);
                                        }
                                        httpURLConnection2 = (HttpURLConnection) url.openConnection();
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
                                if (string != null) {
                                }
                                httpURLConnection2 = (HttpURLConnection) url.openConnection();
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
                            if (str != null) {
                                PaymentFormActivity.this.paymentJson = str;
                                PaymentFormActivity.this.goToNextStep();
                            } else {
                                AlertsCreator.showSimpleToast(PaymentFormActivity.this, LocaleController.getString("PaymentConnectionFailed", R.string.PaymentConnectionFailed));
                            }
                            PaymentFormActivity.this.showEditDoneProgress(true, false);
                            PaymentFormActivity.this.setDonePressed(false);
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                }
                return true;
            } catch (Exception e) {
                FileLog.e(e);
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 25 implements TokenCallback {
        25() {
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
                AlertsCreator.showSimpleToast(PaymentFormActivity.this, LocaleController.getString("PaymentConnectionFailed", R.string.PaymentConnectionFailed));
            } else {
                AlertsCreator.showSimpleToast(PaymentFormActivity.this, exc.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getResponseBody(InputStream inputStream) throws IOException {
        String next = new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
        inputStream.close();
        return next;
    }

    private void sendSavedForm(final Runnable runnable) {
        if (this.canceled) {
            return;
        }
        showEditDoneProgress(true, true);
        TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo = new TLRPC$TL_payments_validateRequestedInfo();
        this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
        TLRPC$TL_inputInvoicePremiumGiftCode tLRPC$TL_inputInvoicePremiumGiftCode = this.invoicePremiumGiftCode;
        if (tLRPC$TL_inputInvoicePremiumGiftCode != null) {
            tLRPC$TL_payments_validateRequestedInfo.invoice = tLRPC$TL_inputInvoicePremiumGiftCode;
        } else if (this.messageObject != null) {
            TLRPC$TL_inputInvoiceMessage tLRPC$TL_inputInvoiceMessage = new TLRPC$TL_inputInvoiceMessage();
            tLRPC$TL_inputInvoiceMessage.peer = getMessagesController().getInputPeer(this.messageObject.messageOwner.peer_id);
            tLRPC$TL_inputInvoiceMessage.msg_id = this.messageObject.getId();
            this.validateRequest.invoice = tLRPC$TL_inputInvoiceMessage;
        } else {
            TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
            tLRPC$TL_inputInvoiceSlug.slug = this.invoiceSlug;
            this.validateRequest.invoice = tLRPC$TL_inputInvoiceSlug;
        }
        final TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo2 = this.validateRequest;
        tLRPC$TL_payments_validateRequestedInfo2.save = true;
        tLRPC$TL_payments_validateRequestedInfo2.info = this.paymentForm.saved_info;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_validateRequestedInfo2, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda57
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$sendSavedForm$51(runnable, tLRPC$TL_payments_validateRequestedInfo2, tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendSavedForm$51(final Runnable runnable, final TLObject tLObject, final TLObject tLObject2, final TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject2 instanceof TLRPC$TL_payments_validatedRequestedInfo) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda40
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendSavedForm$49(tLObject2, runnable);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda43
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendSavedForm$50(tLRPC$TL_error, tLObject);
                }
            });
        }
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
    public void sendForm() {
        if (this.canceled) {
            return;
        }
        showEditDoneProgress(true, true);
        TLRPC$TL_payments_validateRequestedInfo tLRPC$TL_payments_validateRequestedInfo = new TLRPC$TL_payments_validateRequestedInfo();
        this.validateRequest = tLRPC$TL_payments_validateRequestedInfo;
        TLRPC$TL_inputInvoicePremiumGiftCode tLRPC$TL_inputInvoicePremiumGiftCode = this.invoicePremiumGiftCode;
        if (tLRPC$TL_inputInvoicePremiumGiftCode != null) {
            tLRPC$TL_payments_validateRequestedInfo.invoice = tLRPC$TL_inputInvoicePremiumGiftCode;
        } else if (this.messageObject != null) {
            TLRPC$TL_inputInvoiceMessage tLRPC$TL_inputInvoiceMessage = new TLRPC$TL_inputInvoiceMessage();
            tLRPC$TL_inputInvoiceMessage.peer = getMessagesController().getInputPeer(this.messageObject.messageOwner.peer_id);
            tLRPC$TL_inputInvoiceMessage.msg_id = this.messageObject.getId();
            this.validateRequest.invoice = tLRPC$TL_inputInvoiceMessage;
        } else {
            TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
            tLRPC$TL_inputInvoiceSlug.slug = this.invoiceSlug;
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
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(this.validateRequest, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda59
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$sendForm$55(tLRPC$TL_payments_validateRequestedInfo3, tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendForm$55(final TLObject tLObject, final TLObject tLObject2, final TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject2 instanceof TLRPC$TL_payments_validatedRequestedInfo) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendForm$53(tLObject2);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda45
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendForm$54(tLRPC$TL_error, tLObject);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendForm$53(TLObject tLObject) {
        this.requestedInfo = (TLRPC$TL_payments_validatedRequestedInfo) tLObject;
        if (this.paymentForm.saved_info != null && !this.saveShippingInfo) {
            TLRPC$TL_payments_clearSavedInfo tLRPC$TL_payments_clearSavedInfo = new TLRPC$TL_payments_clearSavedInfo();
            tLRPC$TL_payments_clearSavedInfo.info = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_clearSavedInfo, PaymentFormActivity$$ExternalSyntheticLambda65.INSTANCE);
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

    private void sendData() {
        String str;
        if (this.canceled) {
            return;
        }
        showEditDoneProgress(false, true);
        final TLRPC$TL_payments_sendPaymentForm tLRPC$TL_payments_sendPaymentForm = new TLRPC$TL_payments_sendPaymentForm();
        TLRPC$TL_inputInvoicePremiumGiftCode tLRPC$TL_inputInvoicePremiumGiftCode = this.invoicePremiumGiftCode;
        if (tLRPC$TL_inputInvoicePremiumGiftCode != null) {
            tLRPC$TL_payments_sendPaymentForm.invoice = tLRPC$TL_inputInvoicePremiumGiftCode;
        } else if (this.messageObject != null) {
            TLRPC$TL_inputInvoiceMessage tLRPC$TL_inputInvoiceMessage = new TLRPC$TL_inputInvoiceMessage();
            tLRPC$TL_inputInvoiceMessage.peer = getMessagesController().getInputPeer(this.messageObject.messageOwner.peer_id);
            tLRPC$TL_inputInvoiceMessage.msg_id = this.messageObject.getId();
            tLRPC$TL_payments_sendPaymentForm.invoice = tLRPC$TL_inputInvoiceMessage;
        } else {
            TLRPC$TL_inputInvoiceSlug tLRPC$TL_inputInvoiceSlug = new TLRPC$TL_inputInvoiceSlug();
            tLRPC$TL_inputInvoiceSlug.slug = this.invoiceSlug;
            tLRPC$TL_payments_sendPaymentForm.invoice = tLRPC$TL_inputInvoiceSlug;
        }
        tLRPC$TL_payments_sendPaymentForm.form_id = this.paymentForm.form_id;
        if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && this.savedCredentialsCard != null) {
            TLRPC$InputPaymentCredentials tLRPC$InputPaymentCredentials = new TLRPC$InputPaymentCredentials() { // from class: org.telegram.tgnet.TLRPC$TL_inputPaymentCredentialsSaved
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
            tLRPC$TL_payments_sendPaymentForm.credentials = tLRPC$InputPaymentCredentials;
            tLRPC$InputPaymentCredentials.id = this.savedCredentialsCard.id;
            tLRPC$InputPaymentCredentials.tmp_password = UserConfig.getInstance(this.currentAccount).tmpPassword.tmp_password;
        } else {
            TLRPC$TL_inputPaymentCredentialsGooglePay tLRPC$TL_inputPaymentCredentialsGooglePay = this.googlePayCredentials;
            if (tLRPC$TL_inputPaymentCredentialsGooglePay != null) {
                tLRPC$TL_payments_sendPaymentForm.credentials = tLRPC$TL_inputPaymentCredentialsGooglePay;
            } else {
                TLRPC$InputPaymentCredentials tLRPC$InputPaymentCredentials2 = new TLRPC$InputPaymentCredentials() { // from class: org.telegram.tgnet.TLRPC$TL_inputPaymentCredentials
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
                tLRPC$TL_payments_sendPaymentForm.credentials = tLRPC$InputPaymentCredentials2;
                tLRPC$InputPaymentCredentials2.save = this.saveCardInfo;
                tLRPC$InputPaymentCredentials2.data = new TLRPC$TL_dataJSON();
                tLRPC$TL_payments_sendPaymentForm.credentials.data.data = this.paymentJson;
            }
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
        if ((this.paymentForm.invoice.flags & LiteMode.FLAG_CHAT_BLUR) != 0) {
            Long l = this.tipAmount;
            tLRPC$TL_payments_sendPaymentForm.tip_amount = l != null ? l.longValue() : 0L;
            tLRPC$TL_payments_sendPaymentForm.flags |= 4;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_payments_sendPaymentForm, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda61
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$sendData$61(tLRPC$TL_payments_sendPaymentForm, tLObject, tLRPC$TL_error);
            }
        }, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$61(final TLRPC$TL_payments_sendPaymentForm tLRPC$TL_payments_sendPaymentForm, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
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
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda53
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.this.lambda$sendData$56(tLRPC$MessageArr);
                    }
                });
                return;
            } else if (tLObject instanceof TLRPC$TL_payments_paymentVerificationNeeded) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda39
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.this.lambda$sendData$59(tLObject);
                    }
                });
                return;
            } else {
                return;
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda48
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$sendData$60(tLRPC$TL_error, tLRPC$TL_payments_sendPaymentForm);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$56(TLRPC$Message[] tLRPC$MessageArr) {
        UndoView undoView;
        this.paymentStatusSent = true;
        InvoiceStatus invoiceStatus = InvoiceStatus.PAID;
        this.invoiceStatus = invoiceStatus;
        PaymentFormCallback paymentFormCallback = this.paymentFormCallback;
        if (paymentFormCallback != null) {
            paymentFormCallback.onInvoiceStatusChanged(invoiceStatus);
        }
        goToNextStep();
        BaseFragment baseFragment = this.parentFragment;
        if (!(baseFragment instanceof ChatActivity) || (undoView = ((ChatActivity) baseFragment).getUndoView()) == null) {
            return;
        }
        undoView.showWithAction(0L, 77, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.PaymentInfoHint, this.totalPrice[0], this.currentItemName)), tLRPC$MessageArr[0], (Runnable) null, (Runnable) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$59(TLObject tLObject) {
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
        getMessagesController().newMessageCallback = new MessagesController.NewMessageCallback() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda54
            @Override // org.telegram.messenger.MessagesController.NewMessageCallback
            public final boolean onMessageReceived(TLRPC$Message tLRPC$Message) {
                boolean lambda$sendData$58;
                lambda$sendData$58 = PaymentFormActivity.this.lambda$sendData$58(parentLayout, parentActivity, tLRPC$Message);
                return lambda$sendData$58;
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
    public /* synthetic */ boolean lambda$sendData$58(final INavigationLayout iNavigationLayout, final Activity activity, final TLRPC$Message tLRPC$Message) {
        if (MessageObject.getPeerId(tLRPC$Message.peer_id) == this.botUser.id && (tLRPC$Message.action instanceof TLRPC$TL_messageActionPaymentSent)) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.lambda$sendData$57(iNavigationLayout, activity, tLRPC$Message);
                }
            });
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$57(INavigationLayout iNavigationLayout, Activity activity, TLRPC$Message tLRPC$Message) {
        UndoView undoView;
        this.paymentStatusSent = true;
        InvoiceStatus invoiceStatus = InvoiceStatus.PAID;
        this.invoiceStatus = invoiceStatus;
        PaymentFormCallback paymentFormCallback = this.paymentFormCallback;
        if (paymentFormCallback != null) {
            paymentFormCallback.onInvoiceStatusChanged(invoiceStatus);
        }
        onCheckoutSuccess(iNavigationLayout, activity);
        BaseFragment baseFragment = this.parentFragment;
        if (!(baseFragment instanceof ChatActivity) || (undoView = ((ChatActivity) baseFragment).getUndoView()) == null) {
            return;
        }
        undoView.showWithAction(0L, 77, AndroidUtilities.replaceTags(LocaleController.formatString(R.string.PaymentInfoHint, this.totalPrice[0], this.currentItemName)), tLRPC$Message, (Runnable) null, (Runnable) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendData$60(TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_payments_sendPaymentForm tLRPC$TL_payments_sendPaymentForm) {
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

    /* JADX INFO: Access modifiers changed from: private */
    public void setDonePressed(boolean z) {
        this.donePressed = z;
        this.swipeBackEnabled = !z;
        ActionBar actionBar = this.actionBar;
        if (actionBar != null && actionBar.getBackButton() != null) {
            this.actionBar.getBackButton().setEnabled(!this.donePressed);
        }
        TextDetailSettingsCell[] textDetailSettingsCellArr = this.detailSettingsCell;
        if (textDetailSettingsCellArr[0] != null) {
            textDetailSettingsCellArr[0].setEnabled(!this.donePressed);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent motionEvent) {
        return this.swipeBackEnabled;
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
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getPassword, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda58
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    PaymentFormActivity.this.lambda$checkPassword$66(obj, tLRPC$TL_account_getPassword, tLObject, tLRPC$TL_error);
                }
            }, 2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPassword$66(final String str, final TLRPC$TL_account_getPassword tLRPC$TL_account_getPassword, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$checkPassword$65(tLRPC$TL_error, tLObject, str, tLRPC$TL_account_getPassword);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPassword$65(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, String str, TLRPC$TL_account_getPassword tLRPC$TL_account_getPassword) {
        if (tLRPC$TL_error == null) {
            final TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$account_Password, false)) {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
                return;
            } else if (!tLRPC$account_Password.has_password) {
                this.passwordOk = false;
                goToNextStep();
                return;
            } else {
                final byte[] stringBytes = AndroidUtilities.getStringBytes(str);
                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda50
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.this.lambda$checkPassword$64(tLRPC$account_Password, stringBytes);
                    }
                });
                return;
            }
        }
        AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLRPC$TL_account_getPassword, new Object[0]);
        showEditDoneProgress(true, false);
        setDonePressed(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPassword$64(TLRPC$account_Password tLRPC$account_Password, byte[] bArr) {
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = tLRPC$account_Password.current_algo;
        byte[] x = tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow ? SRPHelper.getX(bArr, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo) : null;
        final TLRPC$TL_account_getTmpPassword tLRPC$TL_account_getTmpPassword = new TLRPC$TL_account_getTmpPassword();
        tLRPC$TL_account_getTmpPassword.period = 1800;
        RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda60
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                PaymentFormActivity.this.lambda$checkPassword$63(tLRPC$TL_account_getTmpPassword, tLObject, tLRPC$TL_error);
            }
        };
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo2 = tLRPC$account_Password.current_algo;
        if (tLRPC$PasswordKdfAlgo2 instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            TLRPC$TL_inputCheckPasswordSRP startCheck = SRPHelper.startCheck(x, tLRPC$account_Password.srp_id, tLRPC$account_Password.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo2);
            tLRPC$TL_account_getTmpPassword.password = startCheck;
            if (startCheck == null) {
                TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
                tLRPC$TL_error.text = "ALGO_INVALID";
                requestDelegate.run(null, tLRPC$TL_error);
                return;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getTmpPassword, requestDelegate, 10);
            return;
        }
        TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
        tLRPC$TL_error2.text = "PASSWORD_HASH_INVALID";
        requestDelegate.run(null, tLRPC$TL_error2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPassword$63(final TLRPC$TL_account_getTmpPassword tLRPC$TL_account_getTmpPassword, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.lambda$checkPassword$62(tLObject, tLRPC$TL_error, tLRPC$TL_account_getTmpPassword);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkPassword$62(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_account_getTmpPassword tLRPC$TL_account_getTmpPassword) {
        showEditDoneProgress(true, false);
        setDonePressed(false);
        if (tLObject != null) {
            this.passwordOk = true;
            UserConfig.getInstance(this.currentAccount).tmpPassword = (TLRPC$TL_account_tmpPassword) tLObject;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            goToNextStep();
        } else if (tLRPC$TL_error.text.equals("PASSWORD_HASH_INVALID")) {
            try {
                this.inputFields[1].performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            AndroidUtilities.shakeViewSpring(this.inputFields[1], 3.25f);
            this.inputFields[1].setText("");
        } else {
            AlertsCreator.processError(this.currentAccount, tLRPC$TL_error, this, tLRPC$TL_account_getTmpPassword, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showEditDoneProgress(boolean z, final boolean z2) {
        AnimatorSet animatorSet = this.doneItemAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (z && this.doneItem != null) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.doneItemAnimation = animatorSet2;
            if (z2) {
                this.progressView.setVisibility(0);
                this.doneItem.setEnabled(false);
                this.doneItemAnimation.playTogether(ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, 1.0f));
            } else if (this.webView != null) {
                animatorSet2.playTogether(ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, 0.0f));
            } else {
                this.doneItem.getContentView().setVisibility(0);
                this.doneItem.setEnabled(true);
                this.doneItemAnimation.playTogether(ObjectAnimator.ofFloat(this.progressView, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.progressView, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.progressView, View.ALPHA, 0.0f));
                if (!isFinishing()) {
                    this.doneItemAnimation.playTogether(ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.doneItem.getContentView(), View.ALPHA, 1.0f));
                }
            }
            this.doneItemAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.PaymentFormActivity.27
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation == null || !PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    if (!z2) {
                        PaymentFormActivity.this.progressView.setVisibility(4);
                    } else {
                        PaymentFormActivity.this.doneItem.getContentView().setVisibility(4);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation == null || !PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    PaymentFormActivity.this.doneItemAnimation = null;
                }
            });
            this.doneItemAnimation.setDuration(150L);
            this.doneItemAnimation.start();
        } else if (this.payTextView != null) {
            this.doneItemAnimation = new AnimatorSet();
            if (z2) {
                this.progressViewButton.setVisibility(0);
                this.bottomLayout.setEnabled(false);
                this.doneItemAnimation.playTogether(ObjectAnimator.ofFloat(this.payTextView, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.payTextView, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.payTextView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.progressViewButton, View.ALPHA, 1.0f));
            } else {
                this.payTextView.setVisibility(0);
                this.bottomLayout.setEnabled(true);
                this.doneItemAnimation.playTogether(ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.progressViewButton, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.progressViewButton, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.payTextView, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.payTextView, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.payTextView, View.ALPHA, 1.0f));
            }
            this.doneItemAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.PaymentFormActivity.28
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation == null || !PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    if (!z2) {
                        PaymentFormActivity.this.progressViewButton.setVisibility(4);
                    } else {
                        PaymentFormActivity.this.payTextView.setVisibility(4);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (PaymentFormActivity.this.doneItemAnimation == null || !PaymentFormActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    PaymentFormActivity.this.doneItemAnimation = null;
                }
            });
            this.doneItemAnimation.setDuration(150L);
            this.doneItemAnimation.start();
        }
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
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
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
            arrayList.add(new ThemeDescription(this.dividers.get(i10), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class BottomFrameLayout extends FrameLayout {
        Paint paint;
        float progress;
        SpringAnimation springAnimation;

        public BottomFrameLayout(Context context, TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm) {
            super(context);
            this.paint = new Paint(1);
            setWillNotDraw(false);
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
            if (z2) {
                if (this.progress == f) {
                    return;
                }
                SpringAnimation spring = new SpringAnimation(new FloatValueHolder(this.progress * 100.0f)).setSpring(new SpringForce(f * 100.0f).setStiffness(z ? 500.0f : 650.0f).setDampingRatio(1.0f));
                this.springAnimation = spring;
                spring.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.PaymentFormActivity$BottomFrameLayout$$ExternalSyntheticLambda1
                    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
                    public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f2, float f3) {
                        PaymentFormActivity.BottomFrameLayout.this.lambda$setChecked$0(dynamicAnimation, f2, f3);
                    }
                });
                this.springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.PaymentFormActivity$BottomFrameLayout$$ExternalSyntheticLambda0
                    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z3, float f2, float f3) {
                        PaymentFormActivity.BottomFrameLayout.this.lambda$setChecked$1(dynamicAnimation, z3, f2, f3);
                    }
                });
                this.springAnimation.start();
                return;
            }
            this.progress = f;
            if (PaymentFormActivity.this.payTextView != null) {
                PaymentFormActivity.this.payTextView.setAlpha((this.progress * 0.2f) + 0.8f);
            }
            invalidate();
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
    }
}
