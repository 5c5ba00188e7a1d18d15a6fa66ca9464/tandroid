package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
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
import android.os.Vibrator;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.graphics.ColorUtils$$ExternalSyntheticBackport0;
import androidx.core.view.InputDeviceCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.remoteconfig.RemoteConfigConstants;
import com.microsoft.appcenter.http.DefaultHttpClient;
import com.microsoft.appcenter.ingestion.models.CommonProperties;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.net.StripeApiHandler;
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
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
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
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.HintEditText;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.CountrySelectActivity;
import org.telegram.ui.PaymentFormActivity;
/* loaded from: classes4.dex */
public class PaymentFormActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private static final int FIELDS_COUNT_ADDRESS = 10;
    private static final int FIELDS_COUNT_CARD = 6;
    private static final int FIELDS_COUNT_PASSWORD = 3;
    private static final int FIELDS_COUNT_SAVEDCARD = 2;
    private static final int FIELD_CARD = 0;
    private static final int FIELD_CARDNAME = 2;
    private static final int FIELD_CARD_COUNTRY = 4;
    private static final int FIELD_CARD_POSTCODE = 5;
    private static final int FIELD_CITY = 2;
    private static final int FIELD_COUNTRY = 4;
    private static final int FIELD_CVV = 3;
    private static final int FIELD_EMAIL = 7;
    private static final int FIELD_ENTERPASSWORD = 0;
    private static final int FIELD_ENTERPASSWORDEMAIL = 2;
    private static final int FIELD_EXPIRE_DATE = 1;
    private static final int FIELD_NAME = 6;
    private static final int FIELD_PHONE = 9;
    private static final int FIELD_PHONECODE = 8;
    private static final int FIELD_POSTCODE = 5;
    private static final int FIELD_REENTERPASSWORD = 1;
    private static final int FIELD_SAVEDCARD = 0;
    private static final int FIELD_SAVEDPASSWORD = 1;
    private static final int FIELD_STATE = 3;
    private static final int FIELD_STREET1 = 0;
    private static final int FIELD_STREET2 = 1;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    private static final int STEP_CHECKOUT = 4;
    private static final int STEP_CONFIRM_PASSWORD = 3;
    private static final int STEP_PAYMENT_INFO = 2;
    private static final int STEP_RECEIPT = 5;
    private static final int STEP_SET_PASSWORD_EMAIL = 6;
    private static final int STEP_SHIPPING_INFORMATION = 0;
    private static final int STEP_SHIPPING_METHODS = 1;
    private static final int done_button = 1;
    private TLRPC.User botUser;
    private TextInfoPrivacyCell[] bottomCell;
    private BottomFrameLayout bottomLayout;
    private boolean canceled;
    private String cardName;
    private TextCheckCell checkCell1;
    private EditTextSettingsCell codeFieldCell;
    private HashMap<String, String> codesMap;
    private ArrayList<String> countriesArray;
    private HashMap<String, String> countriesMap;
    private String countryName;
    private String currentBotName;
    private String currentItemName;
    private TLRPC.TL_account_password currentPassword;
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
    private TLRPC.TL_inputPaymentCredentialsGooglePay googlePayCredentials;
    private JSONObject googlePayParameters;
    private String googlePayPublicKey;
    private HeaderCell[] headerCell;
    private boolean ignoreOnCardChange;
    private boolean ignoreOnPhoneChange;
    private boolean ignoreOnTextChange;
    private boolean initGooglePay;
    private EditTextBoldCursor[] inputFields;
    private String invoiceSlug;
    private boolean isAcceptTermsChecked;
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
    private TLRPC.TL_payments_paymentForm paymentForm;
    private PaymentFormCallback paymentFormCallback;
    private PaymentInfoCell paymentInfoCell;
    private String paymentJson;
    private TLRPC.TL_payments_paymentReceipt paymentReceipt;
    private boolean paymentStatusSent;
    private PaymentsClient paymentsClient;
    private HashMap<String, String> phoneFormatMap;
    private ArrayList<TLRPC.TL_labeledPrice> prices;
    private ContextProgressView progressView;
    private ContextProgressView progressViewButton;
    private String providerApiKey;
    private RadioCell[] radioCells;
    private RecurrentPaymentsAcceptCell recurrentAcceptCell;
    private boolean recurrentAccepted;
    private TLRPC.TL_payments_validatedRequestedInfo requestedInfo;
    private Theme.ResourcesProvider resourcesProvider;
    private boolean saveCardInfo;
    private boolean saveShippingInfo;
    private ScrollView scrollView;
    private ShadowSectionCell[] sectionCell;
    private TextSettingsCell[] settingsCell;
    private TLRPC.TL_shippingOption shippingOption;
    private Runnable shortPollRunnable;
    private boolean shouldNavigateBack;
    private boolean swipeBackEnabled;
    private TextView textView;
    private Long tipAmount;
    private LinearLayout tipLayout;
    private TextPriceCell totalCell;
    private String[] totalPrice;
    private String totalPriceDecimal;
    private TLRPC.TL_payments_validateRequestedInfo validateRequest;
    private boolean waitingForEmail;
    private WebView webView;
    private String webViewUrl;
    private boolean webviewLoading;

    /* loaded from: classes4.dex */
    public enum InvoiceStatus {
        PAID,
        CANCELLED,
        PENDING,
        FAILED
    }

    /* loaded from: classes4.dex */
    public interface PaymentFormCallback {
        void onInvoiceStatusChanged(InvoiceStatus invoiceStatus);
    }

    /* loaded from: classes4.dex */
    public interface PaymentFormActivityDelegate {
        void currentPasswordUpdated(TLRPC.TL_account_password tL_account_password);

        void didSelectNewAddress(TLRPC.TL_payments_validateRequestedInfo tL_payments_validateRequestedInfo);

        boolean didSelectNewCard(String str, String str2, boolean z, TLRPC.TL_inputPaymentCredentialsGooglePay tL_inputPaymentCredentialsGooglePay);

        void onFragmentDestroyed();

        /* renamed from: org.telegram.ui.PaymentFormActivity$PaymentFormActivityDelegate$-CC */
        /* loaded from: classes4.dex */
        public final /* synthetic */ class CC {
            public static boolean $default$didSelectNewCard(PaymentFormActivityDelegate _this, String tokenJson, String card, boolean saveCard, TLRPC.TL_inputPaymentCredentialsGooglePay googlePay) {
                return false;
            }

            public static void $default$didSelectNewAddress(PaymentFormActivityDelegate _this, TLRPC.TL_payments_validateRequestedInfo validateRequested) {
            }

            public static void $default$onFragmentDestroyed(PaymentFormActivityDelegate _this) {
            }

            public static void $default$currentPasswordUpdated(PaymentFormActivityDelegate _this, TLRPC.TL_account_password password) {
            }
        }
    }

    /* loaded from: classes4.dex */
    public class TelegramWebviewProxy {
        private TelegramWebviewProxy() {
            PaymentFormActivity.this = r1;
        }

        @JavascriptInterface
        public void postEvent(final String eventName, final String eventData) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$TelegramWebviewProxy$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.TelegramWebviewProxy.this.m4160x74f391d2(eventName, eventData);
                }
            });
        }

        /* renamed from: lambda$postEvent$0$org-telegram-ui-PaymentFormActivity$TelegramWebviewProxy */
        public /* synthetic */ void m4160x74f391d2(String eventName, String eventData) {
            if (PaymentFormActivity.this.getParentActivity() != null && eventName.equals("payment_form_submit")) {
                try {
                    JSONObject jsonObject = new JSONObject(eventData);
                    JSONObject response = jsonObject.getJSONObject("credentials");
                    PaymentFormActivity.this.paymentJson = response.toString();
                    PaymentFormActivity.this.cardName = jsonObject.getString("title");
                } catch (Throwable e) {
                    PaymentFormActivity.this.paymentJson = eventData;
                    FileLog.e(e);
                }
                PaymentFormActivity.this.goToNextStep();
            }
        }
    }

    /* loaded from: classes4.dex */
    public class LinkSpan extends ClickableSpan {
        public LinkSpan() {
            PaymentFormActivity.this = this$0;
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

        @Override // android.text.style.ClickableSpan
        public void onClick(View widget) {
            PaymentFormActivity.this.presentFragment(new TwoStepVerificationSetupActivity(6, PaymentFormActivity.this.currentPassword));
        }
    }

    public PaymentFormActivity(TLRPC.TL_payments_paymentReceipt receipt) {
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
        this.emailCodeLength = 6;
        this.currentStep = 5;
        TLRPC.TL_payments_paymentForm tL_payments_paymentForm = new TLRPC.TL_payments_paymentForm();
        this.paymentForm = tL_payments_paymentForm;
        this.paymentReceipt = receipt;
        tL_payments_paymentForm.bot_id = receipt.bot_id;
        this.paymentForm.invoice = receipt.invoice;
        this.paymentForm.provider_id = receipt.provider_id;
        this.paymentForm.users = receipt.users;
        this.shippingOption = receipt.shipping;
        if (receipt.tip_amount != 0) {
            this.tipAmount = Long.valueOf(receipt.tip_amount);
        }
        TLRPC.User user = getMessagesController().getUser(Long.valueOf(receipt.bot_id));
        this.botUser = user;
        if (user != null) {
            this.currentBotName = user.first_name;
        } else {
            this.currentBotName = "";
        }
        this.currentItemName = receipt.title;
        if (receipt.info != null) {
            this.validateRequest = new TLRPC.TL_payments_validateRequestedInfo();
            if (this.messageObject != null) {
                TLRPC.TL_inputInvoiceMessage inputInvoice = new TLRPC.TL_inputInvoiceMessage();
                inputInvoice.peer = getMessagesController().getInputPeer(receipt.bot_id);
                this.validateRequest.invoice = inputInvoice;
            } else {
                TLRPC.TL_inputInvoiceSlug inputInvoice2 = new TLRPC.TL_inputInvoiceSlug();
                inputInvoice2.slug = this.invoiceSlug;
                this.validateRequest.invoice = inputInvoice2;
            }
            this.validateRequest.info = receipt.info;
        }
        this.cardName = receipt.credentials_title;
    }

    public PaymentFormActivity(TLRPC.TL_payments_paymentForm form, String invoiceSlug, BaseFragment parentFragment) {
        this(form, null, invoiceSlug, parentFragment);
    }

    public PaymentFormActivity(TLRPC.TL_payments_paymentForm form, MessageObject message, BaseFragment parentFragment) {
        this(form, message, null, parentFragment);
    }

    public PaymentFormActivity(TLRPC.TL_payments_paymentForm form, MessageObject message, String invoiceSlug, BaseFragment parentFragment) {
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
        this.emailCodeLength = 6;
        this.isCheckoutPreview = true;
        init(form, message, invoiceSlug, 4, null, null, null, null, null, null, false, null, parentFragment);
    }

    private PaymentFormActivity(TLRPC.TL_payments_paymentForm form, MessageObject message, String invoiceSlug, int step, TLRPC.TL_payments_validatedRequestedInfo validatedRequestedInfo, TLRPC.TL_shippingOption shipping, Long tips, String tokenJson, String card, TLRPC.TL_payments_validateRequestedInfo request, boolean saveCard, TLRPC.TL_inputPaymentCredentialsGooglePay googlePay, BaseFragment parent) {
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
        this.emailCodeLength = 6;
        init(form, message, invoiceSlug, step, validatedRequestedInfo, shipping, tips, tokenJson, card, request, saveCard, googlePay, parent);
    }

    public void setPaymentFormCallback(PaymentFormCallback callback) {
        this.paymentFormCallback = callback;
    }

    private void setCurrentPassword(TLRPC.TL_account_password password) {
        if (password.has_password) {
            if (getParentActivity() == null) {
                return;
            }
            goToNextStep();
            return;
        }
        this.currentPassword = password;
        this.waitingForEmail = !TextUtils.isEmpty(password.email_unconfirmed_pattern);
        updatePasswordFields();
    }

    private void setDelegate(PaymentFormActivityDelegate paymentFormActivityDelegate) {
        this.delegate = paymentFormActivityDelegate;
    }

    public void setResourcesProvider(Theme.ResourcesProvider provider) {
        this.resourcesProvider = provider;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public Theme.ResourcesProvider getResourceProvider() {
        return this.resourcesProvider;
    }

    private void init(TLRPC.TL_payments_paymentForm form, MessageObject message, String slug, int step, TLRPC.TL_payments_validatedRequestedInfo validatedRequestedInfo, TLRPC.TL_shippingOption shipping, Long tips, String tokenJson, String card, TLRPC.TL_payments_validateRequestedInfo request, boolean saveCard, TLRPC.TL_inputPaymentCredentialsGooglePay googlePay, BaseFragment parent) {
        this.currentStep = step;
        this.parentFragment = parent;
        this.paymentJson = tokenJson;
        this.googlePayCredentials = googlePay;
        this.requestedInfo = validatedRequestedInfo;
        this.paymentForm = form;
        this.shippingOption = shipping;
        this.tipAmount = tips;
        this.messageObject = message;
        this.invoiceSlug = slug;
        this.saveCardInfo = saveCard;
        this.isWebView = !"stripe".equals(form.native_provider) && !"smartglocal".equals(this.paymentForm.native_provider);
        TLRPC.User user = getMessagesController().getUser(Long.valueOf(form.bot_id));
        this.botUser = user;
        if (user != null) {
            this.currentBotName = user.first_name;
        } else {
            this.currentBotName = "";
        }
        this.currentItemName = form.title;
        this.validateRequest = request;
        this.saveShippingInfo = true;
        if (!saveCard && this.currentStep != 4) {
            this.saveCardInfo = this.paymentForm.saved_credentials != null;
        } else {
            this.saveCardInfo = saveCard;
        }
        if (card == null) {
            if (form.saved_credentials != null) {
                this.cardName = form.saved_credentials.title;
                return;
            }
            return;
        }
        this.cardName = card;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                int i = this.currentStep;
                if ((i == 2 || i == 6) && !this.paymentForm.invoice.test) {
                    getParentActivity().getWindow().setFlags(8192, 8192);
                } else if (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture) {
                    getParentActivity().getWindow().clearFlags(8192);
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:186:0x084b  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x085c  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0866  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x087b  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0893  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x08a0  */
    /* JADX WARN: Removed duplicated region for block: B:206:0x08d4  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x08f7  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x094d  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0a04 A[Catch: Exception -> 0x0a0e, TRY_LEAVE, TryCatch #1 {Exception -> 0x0a0e, blocks: (B:252:0x09f8, B:254:0x0a04), top: B:698:0x09f8 }] */
    /* JADX WARN: Removed duplicated region for block: B:263:0x0a25  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0244  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        char c;
        char c2;
        String str;
        String str2;
        final String providerName;
        TLRPC.TL_payments_paymentForm tL_payments_paymentForm;
        int i;
        TLRPC.TL_payments_paymentForm tL_payments_paymentForm2;
        final long amount;
        boolean z;
        HashMap<String, String> countryMap;
        HashMap<String, String> languageMap;
        int a;
        int i2;
        ShadowSectionCell[] shadowSectionCellArr;
        String country;
        String countryName;
        int index;
        TelephonyManager telephonyManager;
        ViewGroup container;
        HashMap<String, String> countryMap2;
        FrameLayout frameLayout;
        ActionBarMenu menu;
        String providerName2;
        Exception e;
        BufferedReader reader;
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
            public void onItemClick(int id) {
                if (id == -1) {
                    if (PaymentFormActivity.this.donePressed) {
                        return;
                    }
                    PaymentFormActivity.this.finishFragment();
                } else if (id == 1 && !PaymentFormActivity.this.donePressed) {
                    if (PaymentFormActivity.this.currentStep != 3) {
                        AndroidUtilities.hideKeyboard(PaymentFormActivity.this.getParentActivity().getCurrentFocus());
                    }
                    switch (PaymentFormActivity.this.currentStep) {
                        case 0:
                            PaymentFormActivity.this.setDonePressed(true);
                            PaymentFormActivity.this.sendForm();
                            return;
                        case 1:
                            int a2 = 0;
                            while (true) {
                                if (a2 < PaymentFormActivity.this.radioCells.length) {
                                    if (!PaymentFormActivity.this.radioCells[a2].isChecked()) {
                                        a2++;
                                    } else {
                                        PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                                        paymentFormActivity.shippingOption = paymentFormActivity.requestedInfo.shipping_options.get(a2);
                                    }
                                }
                            }
                            PaymentFormActivity.this.goToNextStep();
                            return;
                        case 2:
                            PaymentFormActivity.this.sendCardData();
                            return;
                        case 3:
                            PaymentFormActivity.this.checkPassword();
                            return;
                        case 4:
                        case 5:
                        default:
                            return;
                        case 6:
                            PaymentFormActivity.this.sendSavePassword(false);
                            return;
                    }
                }
            }
        });
        ActionBarMenu menu2 = this.actionBar.createMenu();
        switch (this.currentStep) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
                this.doneItem = menu2.addItemWithWidth(1, R.drawable.ic_ab_done, AndroidUtilities.dp(56.0f), LocaleController.getString("Done", R.string.Done));
                ContextProgressView contextProgressView = new ContextProgressView(context, 1);
                this.progressView = contextProgressView;
                contextProgressView.setAlpha(0.0f);
                this.progressView.setScaleX(0.1f);
                this.progressView.setScaleY(0.1f);
                this.progressView.setVisibility(4);
                this.doneItem.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0f));
                break;
        }
        this.fragmentView = new FrameLayout(context);
        FrameLayout frameLayout2 = (FrameLayout) this.fragmentView;
        this.fragmentView.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundGray));
        ScrollView scrollView = new ScrollView(context);
        this.scrollView = scrollView;
        scrollView.setFillViewport(true);
        AndroidUtilities.setScrollViewEdgeEffectColor(this.scrollView, getThemedColor(Theme.key_actionBarDefault));
        frameLayout2.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 0.0f, 0.0f, this.currentStep == 4 ? 48.0f : 0.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.linearLayout2 = linearLayout;
        linearLayout.setOrientation(1);
        int i3 = 0;
        this.linearLayout2.setClipChildren(false);
        this.scrollView.addView(this.linearLayout2, new FrameLayout.LayoutParams(-1, -2));
        int i4 = this.currentStep;
        char c3 = 2;
        if (i4 == 0) {
            HashMap<String, String> languageMap2 = new HashMap<>();
            HashMap<String, String> countryMap3 = new HashMap<>();
            try {
                reader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("countries.txt")));
            } catch (Exception e2) {
                e = e2;
                languageMap = languageMap2;
                countryMap = countryMap3;
            }
            while (true) {
                String line = reader.readLine();
                if (line != null) {
                    String[] args = line.split(";");
                    this.countriesArray.add(i3, args[c3]);
                    this.countriesMap.put(args[c3], args[i3]);
                    this.codesMap.put(args[i3], args[c3]);
                    countryMap = countryMap3;
                    try {
                        countryMap.put(args[1], args[c3]);
                        if (args.length > 3) {
                            try {
                                this.phoneFormatMap.put(args[i3], args[3]);
                            } catch (Exception e3) {
                                e = e3;
                                languageMap = languageMap2;
                            }
                        }
                        languageMap = languageMap2;
                        try {
                            languageMap.put(args[1], args[2]);
                            languageMap2 = languageMap;
                            countryMap3 = countryMap;
                            c3 = 2;
                            i3 = 0;
                        } catch (Exception e4) {
                            e = e4;
                        }
                    } catch (Exception e5) {
                        e = e5;
                        languageMap = languageMap2;
                    }
                } else {
                    languageMap = languageMap2;
                    countryMap = countryMap3;
                    reader.close();
                    Collections.sort(this.countriesArray, CountrySelectActivity$CountryAdapter$$ExternalSyntheticLambda0.INSTANCE);
                    this.inputFields = new EditTextBoldCursor[10];
                    a = 0;
                    while (a < 10) {
                        if (a == 0) {
                            this.headerCell[0] = new HeaderCell(context, this.resourcesProvider);
                            this.headerCell[0].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            this.headerCell[0].setText(LocaleController.getString("PaymentShippingAddress", R.string.PaymentShippingAddress));
                            this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                        } else if (a == 6) {
                            this.sectionCell[0] = new ShadowSectionCell(context, this.resourcesProvider);
                            this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(-1, -2));
                            this.headerCell[1] = new HeaderCell(context, this.resourcesProvider);
                            this.headerCell[1].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            this.headerCell[1].setText(LocaleController.getString("PaymentShippingReceiver", R.string.PaymentShippingReceiver));
                            this.linearLayout2.addView(this.headerCell[1], LayoutHelper.createLinear(-1, -2));
                        }
                        if (a == 8) {
                            container = new LinearLayout(context);
                            container.setClipChildren(false);
                            ((LinearLayout) container).setOrientation(0);
                            this.linearLayout2.addView(container, LayoutHelper.createLinear(-1, 50));
                            container.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        } else if (a == 9) {
                            container = (ViewGroup) this.inputFields[8].getParent();
                        } else {
                            container = new FrameLayout(context);
                            container.setClipChildren(false);
                            this.linearLayout2.addView(container, LayoutHelper.createLinear(-1, 50));
                            container.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            boolean allowDivider = a != 5;
                            if (allowDivider) {
                                if (a == 7 && !this.paymentForm.invoice.phone_requested) {
                                    allowDivider = false;
                                } else if (a == 6 && !this.paymentForm.invoice.phone_requested && !this.paymentForm.invoice.email_requested) {
                                    allowDivider = false;
                                }
                            }
                            if (allowDivider) {
                                View divider = new View(context) { // from class: org.telegram.ui.PaymentFormActivity.2
                                    @Override // android.view.View
                                    protected void onDraw(Canvas canvas) {
                                        canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                                    }
                                };
                                divider.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                                this.dividers.add(divider);
                                container.addView(divider, new FrameLayout.LayoutParams(-1, 1, 83));
                            }
                        }
                        if (a == 9) {
                            this.inputFields[a] = new HintEditText(context);
                        } else {
                            this.inputFields[a] = new EditTextBoldCursor(context);
                        }
                        this.inputFields[a].setTag(Integer.valueOf(a));
                        this.inputFields[a].setTextSize(1, 16.0f);
                        this.inputFields[a].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                        this.inputFields[a].setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                        this.inputFields[a].setBackgroundDrawable(null);
                        this.inputFields[a].setCursorColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                        this.inputFields[a].setCursorSize(AndroidUtilities.dp(20.0f));
                        this.inputFields[a].setCursorWidth(1.5f);
                        if (a == 4) {
                            this.inputFields[a].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda14
                                @Override // android.view.View.OnTouchListener
                                public final boolean onTouch(View view, MotionEvent motionEvent) {
                                    return PaymentFormActivity.this.m4104lambda$createView$1$orgtelegramuiPaymentFormActivity(view, motionEvent);
                                }
                            });
                            this.inputFields[a].setInputType(0);
                        }
                        if (a == 9 || a == 8) {
                            this.inputFields[a].setInputType(3);
                        } else if (a == 7) {
                            this.inputFields[a].setInputType(1);
                        } else {
                            this.inputFields[a].setInputType(16385);
                        }
                        this.inputFields[a].setImeOptions(268435461);
                        switch (a) {
                            case 0:
                                this.inputFields[a].setHint(LocaleController.getString("PaymentShippingAddress1Placeholder", R.string.PaymentShippingAddress1Placeholder));
                                if (this.paymentForm.saved_info != null && this.paymentForm.saved_info.shipping_address != null) {
                                    this.inputFields[a].setText(this.paymentForm.saved_info.shipping_address.street_line1);
                                    break;
                                }
                                break;
                            case 1:
                                this.inputFields[a].setHint(LocaleController.getString("PaymentShippingAddress2Placeholder", R.string.PaymentShippingAddress2Placeholder));
                                if (this.paymentForm.saved_info != null && this.paymentForm.saved_info.shipping_address != null) {
                                    this.inputFields[a].setText(this.paymentForm.saved_info.shipping_address.street_line2);
                                    break;
                                }
                                break;
                            case 2:
                                this.inputFields[a].setHint(LocaleController.getString("PaymentShippingCityPlaceholder", R.string.PaymentShippingCityPlaceholder));
                                if (this.paymentForm.saved_info != null && this.paymentForm.saved_info.shipping_address != null) {
                                    this.inputFields[a].setText(this.paymentForm.saved_info.shipping_address.city);
                                    break;
                                }
                                break;
                            case 3:
                                this.inputFields[a].setHint(LocaleController.getString("PaymentShippingStatePlaceholder", R.string.PaymentShippingStatePlaceholder));
                                if (this.paymentForm.saved_info != null && this.paymentForm.saved_info.shipping_address != null) {
                                    this.inputFields[a].setText(this.paymentForm.saved_info.shipping_address.state);
                                    break;
                                }
                                break;
                            case 4:
                                this.inputFields[a].setHint(LocaleController.getString("PaymentShippingCountry", R.string.PaymentShippingCountry));
                                if (this.paymentForm.saved_info != null && this.paymentForm.saved_info.shipping_address != null) {
                                    String value = countryMap.get(this.paymentForm.saved_info.shipping_address.country_iso2);
                                    String str3 = this.paymentForm.saved_info.shipping_address.country_iso2;
                                    this.countryName = str3;
                                    EditTextBoldCursor editTextBoldCursor = this.inputFields[a];
                                    if (value != null) {
                                        str3 = value;
                                    }
                                    editTextBoldCursor.setText(str3);
                                    break;
                                }
                                break;
                            case 5:
                                this.inputFields[a].setHint(LocaleController.getString("PaymentShippingZipPlaceholder", R.string.PaymentShippingZipPlaceholder));
                                if (this.paymentForm.saved_info != null && this.paymentForm.saved_info.shipping_address != null) {
                                    this.inputFields[a].setText(this.paymentForm.saved_info.shipping_address.post_code);
                                    break;
                                }
                                break;
                            case 6:
                                this.inputFields[a].setHint(LocaleController.getString("PaymentShippingName", R.string.PaymentShippingName));
                                if (this.paymentForm.saved_info != null && this.paymentForm.saved_info.name != null) {
                                    this.inputFields[a].setText(this.paymentForm.saved_info.name);
                                    break;
                                }
                                break;
                            case 7:
                                this.inputFields[a].setHint(LocaleController.getString("PaymentShippingEmailPlaceholder", R.string.PaymentShippingEmailPlaceholder));
                                if (this.paymentForm.saved_info != null && this.paymentForm.saved_info.email != null) {
                                    this.inputFields[a].setText(this.paymentForm.saved_info.email);
                                    break;
                                }
                                break;
                        }
                        EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                        editTextBoldCursorArr[a].setSelection(editTextBoldCursorArr[a].length());
                        if (a == 8) {
                            TextView textView = new TextView(context);
                            this.textView = textView;
                            textView.setText("+");
                            this.textView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                            this.textView.setTextSize(1, 16.0f);
                            container.addView(this.textView, LayoutHelper.createLinear(-2, -2, 21.0f, 12.0f, 0.0f, 6.0f));
                            this.inputFields[a].setPadding(AndroidUtilities.dp(10.0f), 0, 0, 0);
                            this.inputFields[a].setGravity(19);
                            InputFilter[] inputFilters = {new InputFilter.LengthFilter(5)};
                            this.inputFields[a].setFilters(inputFilters);
                            container.addView(this.inputFields[a], LayoutHelper.createLinear(55, -2, 0.0f, 12.0f, 21.0f, 6.0f));
                            this.inputFields[a].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.3
                                @Override // android.text.TextWatcher
                                public void beforeTextChanged(CharSequence charSequence, int i5, int i22, int i32) {
                                }

                                @Override // android.text.TextWatcher
                                public void onTextChanged(CharSequence charSequence, int i5, int i22, int i32) {
                                }

                                @Override // android.text.TextWatcher
                                public void afterTextChanged(Editable editable) {
                                    String hint;
                                    if (!PaymentFormActivity.this.ignoreOnTextChange) {
                                        PaymentFormActivity.this.ignoreOnTextChange = true;
                                        String text = PhoneFormat.stripExceptNumbers(PaymentFormActivity.this.inputFields[8].getText().toString());
                                        PaymentFormActivity.this.inputFields[8].setText(text);
                                        HintEditText phoneField = (HintEditText) PaymentFormActivity.this.inputFields[9];
                                        if (text.length() == 0) {
                                            phoneField.setHintText((String) null);
                                            phoneField.setHint(LocaleController.getString("PaymentShippingPhoneNumber", R.string.PaymentShippingPhoneNumber));
                                        } else {
                                            boolean ok = false;
                                            String textToSet = null;
                                            if (text.length() > 4) {
                                                int a2 = 4;
                                                while (true) {
                                                    if (a2 < 1) {
                                                        break;
                                                    }
                                                    String sub = text.substring(0, a2);
                                                    if (((String) PaymentFormActivity.this.codesMap.get(sub)) == null) {
                                                        a2--;
                                                    } else {
                                                        ok = true;
                                                        String textToSet2 = text.substring(a2) + PaymentFormActivity.this.inputFields[9].getText().toString();
                                                        text = sub;
                                                        PaymentFormActivity.this.inputFields[8].setText(sub);
                                                        textToSet = textToSet2;
                                                        break;
                                                    }
                                                }
                                                if (!ok) {
                                                    textToSet = text.substring(1) + PaymentFormActivity.this.inputFields[9].getText().toString();
                                                    EditTextBoldCursor editTextBoldCursor2 = PaymentFormActivity.this.inputFields[8];
                                                    String substring = text.substring(0, 1);
                                                    text = substring;
                                                    editTextBoldCursor2.setText(substring);
                                                }
                                            }
                                            String country2 = (String) PaymentFormActivity.this.codesMap.get(text);
                                            boolean set = false;
                                            if (country2 != null) {
                                                int index2 = PaymentFormActivity.this.countriesArray.indexOf(country2);
                                                if (index2 != -1 && (hint = (String) PaymentFormActivity.this.phoneFormatMap.get(text)) != null) {
                                                    set = true;
                                                    phoneField.setHintText(hint.replace('X', (char) 8211));
                                                    phoneField.setHint((CharSequence) null);
                                                }
                                            }
                                            if (!set) {
                                                phoneField.setHintText((String) null);
                                                phoneField.setHint(LocaleController.getString("PaymentShippingPhoneNumber", R.string.PaymentShippingPhoneNumber));
                                            }
                                            if (!ok) {
                                                PaymentFormActivity.this.inputFields[8].setSelection(PaymentFormActivity.this.inputFields[8].getText().length());
                                            }
                                            if (textToSet != null) {
                                                phoneField.requestFocus();
                                                phoneField.setText(textToSet);
                                                phoneField.setSelection(phoneField.length());
                                            }
                                        }
                                        PaymentFormActivity.this.ignoreOnTextChange = false;
                                    }
                                }
                            });
                        } else if (a == 9) {
                            this.inputFields[a].setPadding(0, 0, 0, 0);
                            this.inputFields[a].setGravity(19);
                            container.addView(this.inputFields[a], LayoutHelper.createLinear(-1, -2, 0.0f, 12.0f, 21.0f, 6.0f));
                            this.inputFields[a].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.4
                                private int actionPosition;
                                private int characterAction = -1;

                                @Override // android.text.TextWatcher
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                    if (count == 0 && after == 1) {
                                        this.characterAction = 1;
                                    } else if (count == 1 && after == 0) {
                                        if (s.charAt(start) == ' ' && start > 0) {
                                            this.characterAction = 3;
                                            this.actionPosition = start - 1;
                                            return;
                                        }
                                        this.characterAction = 2;
                                    } else {
                                        this.characterAction = -1;
                                    }
                                }

                                @Override // android.text.TextWatcher
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                }

                                @Override // android.text.TextWatcher
                                public void afterTextChanged(Editable s) {
                                    int i5;
                                    int i6;
                                    if (!PaymentFormActivity.this.ignoreOnPhoneChange) {
                                        HintEditText phoneField = (HintEditText) PaymentFormActivity.this.inputFields[9];
                                        int start = phoneField.getSelectionStart();
                                        String str4 = phoneField.getText().toString();
                                        if (this.characterAction == 3) {
                                            str4 = str4.substring(0, this.actionPosition) + str4.substring(this.actionPosition + 1);
                                            start--;
                                        }
                                        StringBuilder builder = new StringBuilder(str4.length());
                                        for (int a2 = 0; a2 < str4.length(); a2++) {
                                            String ch = str4.substring(a2, a2 + 1);
                                            if ("0123456789".contains(ch)) {
                                                builder.append(ch);
                                            }
                                        }
                                        PaymentFormActivity.this.ignoreOnPhoneChange = true;
                                        String hint = phoneField.getHintText();
                                        if (hint != null) {
                                            int a3 = 0;
                                            while (true) {
                                                if (a3 >= builder.length()) {
                                                    break;
                                                } else if (a3 < hint.length()) {
                                                    if (hint.charAt(a3) == ' ') {
                                                        builder.insert(a3, ' ');
                                                        a3++;
                                                        if (start == a3 && (i6 = this.characterAction) != 2 && i6 != 3) {
                                                            start++;
                                                        }
                                                    }
                                                    a3++;
                                                } else {
                                                    builder.insert(a3, ' ');
                                                    if (start == a3 + 1 && (i5 = this.characterAction) != 2 && i5 != 3) {
                                                        start++;
                                                    }
                                                }
                                            }
                                        }
                                        phoneField.setText(builder);
                                        if (start >= 0) {
                                            phoneField.setSelection(Math.min(start, phoneField.length()));
                                        }
                                        phoneField.onTextChange();
                                        PaymentFormActivity.this.ignoreOnPhoneChange = false;
                                    }
                                }
                            });
                        } else {
                            this.inputFields[a].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                            this.inputFields[a].setGravity(LocaleController.isRTL ? 5 : 3);
                            container.addView(this.inputFields[a], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                        }
                        this.inputFields[a].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda18
                            @Override // android.widget.TextView.OnEditorActionListener
                            public final boolean onEditorAction(TextView textView2, int i5, KeyEvent keyEvent) {
                                return PaymentFormActivity.this.m4113lambda$createView$2$orgtelegramuiPaymentFormActivity(textView2, i5, keyEvent);
                            }
                        });
                        if (a == 9) {
                            if (!this.paymentForm.invoice.email_to_provider && !this.paymentForm.invoice.phone_to_provider) {
                                this.sectionCell[1] = new ShadowSectionCell(context, this.resourcesProvider);
                                this.linearLayout2.addView(this.sectionCell[1], LayoutHelper.createLinear(-1, -2));
                                menu = menu2;
                                countryMap2 = countryMap;
                                frameLayout = frameLayout2;
                            } else {
                                TLRPC.User providerUser = null;
                                int b = 0;
                                while (b < this.paymentForm.users.size()) {
                                    TLRPC.User user = this.paymentForm.users.get(b);
                                    ActionBarMenu menu3 = menu2;
                                    ViewGroup container2 = container;
                                    HashMap<String, String> countryMap4 = countryMap;
                                    FrameLayout frameLayout3 = frameLayout2;
                                    if (user.id == this.paymentForm.provider_id) {
                                        providerUser = user;
                                    }
                                    b++;
                                    menu2 = menu3;
                                    container = container2;
                                    frameLayout2 = frameLayout3;
                                    countryMap = countryMap4;
                                }
                                menu = menu2;
                                countryMap2 = countryMap;
                                frameLayout = frameLayout2;
                                if (providerUser != null) {
                                    providerName2 = ContactsController.formatName(providerUser.first_name, providerUser.last_name);
                                } else {
                                    providerName2 = "";
                                }
                                this.bottomCell[1] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                                this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                                this.linearLayout2.addView(this.bottomCell[1], LayoutHelper.createLinear(-1, -2));
                                if (this.paymentForm.invoice.email_to_provider && this.paymentForm.invoice.phone_to_provider) {
                                    this.bottomCell[1].setText(LocaleController.formatString("PaymentPhoneEmailToProvider", R.string.PaymentPhoneEmailToProvider, providerName2));
                                } else if (this.paymentForm.invoice.email_to_provider) {
                                    this.bottomCell[1].setText(LocaleController.formatString("PaymentEmailToProvider", R.string.PaymentEmailToProvider, providerName2));
                                } else {
                                    this.bottomCell[1].setText(LocaleController.formatString("PaymentPhoneToProvider", R.string.PaymentPhoneToProvider, providerName2));
                                }
                            }
                            TextCheckCell textCheckCell = new TextCheckCell(context, this.resourcesProvider);
                            this.checkCell1 = textCheckCell;
                            textCheckCell.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                            this.checkCell1.setTextAndCheck(LocaleController.getString("PaymentShippingSave", R.string.PaymentShippingSave), this.saveShippingInfo, false);
                            this.linearLayout2.addView(this.checkCell1, LayoutHelper.createLinear(-1, -2));
                            this.checkCell1.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda6
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    PaymentFormActivity.this.m4123lambda$createView$3$orgtelegramuiPaymentFormActivity(view);
                                }
                            });
                            this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                            this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                            this.bottomCell[0].setText(LocaleController.getString("PaymentShippingSaveInfo", R.string.PaymentShippingSaveInfo));
                            this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                        } else {
                            menu = menu2;
                            countryMap2 = countryMap;
                            frameLayout = frameLayout2;
                        }
                        a++;
                        menu2 = menu;
                        frameLayout2 = frameLayout;
                        countryMap = countryMap2;
                    }
                    if (!this.paymentForm.invoice.name_requested) {
                        i2 = 8;
                    } else {
                        i2 = 8;
                        ((ViewGroup) this.inputFields[6].getParent()).setVisibility(8);
                    }
                    if (!this.paymentForm.invoice.phone_requested) {
                        ((ViewGroup) this.inputFields[i2].getParent()).setVisibility(i2);
                    }
                    if (!this.paymentForm.invoice.email_requested) {
                        ((ViewGroup) this.inputFields[7].getParent()).setVisibility(8);
                    }
                    if (this.paymentForm.invoice.phone_requested) {
                        if (this.paymentForm.invoice.email_requested) {
                            this.inputFields[7].setImeOptions(268435462);
                        } else if (this.paymentForm.invoice.name_requested) {
                            this.inputFields[6].setImeOptions(268435462);
                        } else {
                            this.inputFields[5].setImeOptions(268435462);
                        }
                    } else {
                        this.inputFields[9].setImeOptions(268435462);
                    }
                    shadowSectionCellArr = this.sectionCell;
                    if (shadowSectionCellArr[1] == null) {
                        shadowSectionCellArr[1].setVisibility((this.paymentForm.invoice.name_requested || this.paymentForm.invoice.phone_requested || this.paymentForm.invoice.email_requested) ? 0 : 8);
                    } else {
                        TextInfoPrivacyCell[] textInfoPrivacyCellArr = this.bottomCell;
                        if (textInfoPrivacyCellArr[1] != null) {
                            textInfoPrivacyCellArr[1].setVisibility((this.paymentForm.invoice.name_requested || this.paymentForm.invoice.phone_requested || this.paymentForm.invoice.email_requested) ? 0 : 8);
                        }
                    }
                    this.headerCell[1].setVisibility((!this.paymentForm.invoice.name_requested || this.paymentForm.invoice.phone_requested || this.paymentForm.invoice.email_requested) ? 0 : 8);
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
                    if (this.paymentForm.saved_info != null || TextUtils.isEmpty(this.paymentForm.saved_info.phone)) {
                        fillNumber(null);
                    } else {
                        fillNumber(this.paymentForm.saved_info.phone);
                    }
                    if (this.inputFields[8].length() == 0 && this.paymentForm.invoice.phone_requested && (this.paymentForm.saved_info == null || TextUtils.isEmpty(this.paymentForm.saved_info.phone))) {
                        country = null;
                        try {
                            telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
                            if (telephonyManager != null) {
                                country = telephonyManager.getSimCountryIso().toUpperCase();
                            }
                        } catch (Exception e6) {
                            FileLog.e(e6);
                        }
                        if (country != null && (countryName = languageMap.get(country)) != null) {
                            index = this.countriesArray.indexOf(countryName);
                            if (index != -1) {
                                this.inputFields[8].setText(this.countriesMap.get(countryName));
                            }
                        }
                    }
                }
                e = e4;
                FileLog.e(e);
                Collections.sort(this.countriesArray, CountrySelectActivity$CountryAdapter$$ExternalSyntheticLambda0.INSTANCE);
                this.inputFields = new EditTextBoldCursor[10];
                a = 0;
                while (a < 10) {
                }
                if (!this.paymentForm.invoice.name_requested) {
                }
                if (!this.paymentForm.invoice.phone_requested) {
                }
                if (!this.paymentForm.invoice.email_requested) {
                }
                if (this.paymentForm.invoice.phone_requested) {
                }
                shadowSectionCellArr = this.sectionCell;
                if (shadowSectionCellArr[1] == null) {
                }
                this.headerCell[1].setVisibility((!this.paymentForm.invoice.name_requested || this.paymentForm.invoice.phone_requested || this.paymentForm.invoice.email_requested) ? 0 : 8);
                if (!this.paymentForm.invoice.shipping_address_requested) {
                }
                if (this.paymentForm.saved_info != null) {
                }
                fillNumber(null);
                if (this.inputFields[8].length() == 0) {
                    country = null;
                    telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
                    if (telephonyManager != null) {
                    }
                    if (country != null) {
                        index = this.countriesArray.indexOf(countryName);
                        if (index != -1) {
                        }
                    }
                }
            }
        } else if (i4 == 2) {
            if (this.paymentForm.native_params != null) {
                try {
                    JSONObject jsonObject = new JSONObject(this.paymentForm.native_params.data);
                    String googlePayKey = jsonObject.optString("google_pay_public_key");
                    if (!TextUtils.isEmpty(googlePayKey)) {
                        this.googlePayPublicKey = googlePayKey;
                    }
                    this.googlePayCountryCode = jsonObject.optString("acquirer_bank_country");
                    this.googlePayParameters = jsonObject.optJSONObject("gpay_parameters");
                } catch (Exception e7) {
                    FileLog.e(e7);
                }
            }
            if (this.isWebView) {
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
                WebView webView = new WebView(context) { // from class: org.telegram.ui.PaymentFormActivity.5
                    @Override // android.webkit.WebView, android.view.View
                    public boolean onTouchEvent(MotionEvent event) {
                        ((ViewGroup) PaymentFormActivity.this.fragmentView).requestDisallowInterceptTouchEvent(true);
                        return super.onTouchEvent(event);
                    }

                    @Override // android.webkit.WebView, android.widget.AbsoluteLayout, android.view.View
                    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    }
                };
                this.webView = webView;
                webView.getSettings().setJavaScriptEnabled(true);
                this.webView.getSettings().setDomStorageEnabled(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.webView.getSettings().setMixedContentMode(0);
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptThirdPartyCookies(this.webView, true);
                }
                if (Build.VERSION.SDK_INT >= 17) {
                    this.webView.addJavascriptInterface(new TelegramWebviewProxy(), "TelegramWebviewProxy");
                }
                this.webView.setWebViewClient(new WebViewClient() { // from class: org.telegram.ui.PaymentFormActivity.6
                    @Override // android.webkit.WebViewClient
                    public void onLoadResource(WebView view, String url) {
                        super.onLoadResource(view, url);
                    }

                    @Override // android.webkit.WebViewClient
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                        paymentFormActivity.shouldNavigateBack = !url.equals(paymentFormActivity.webViewUrl);
                        return super.shouldOverrideUrlLoading(view, url);
                    }

                    @Override // android.webkit.WebViewClient
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
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
                this.checkCell1.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda8
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PaymentFormActivity.this.m4126lambda$createView$4$orgtelegramuiPaymentFormActivity(view);
                    }
                });
                this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                updateSavePaymentField();
                this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
            } else {
                if (this.paymentForm.native_params != null) {
                    try {
                        JSONObject jsonObject2 = new JSONObject(this.paymentForm.native_params.data);
                        try {
                            this.need_card_country = jsonObject2.getBoolean("need_country");
                        } catch (Exception e8) {
                            this.need_card_country = false;
                        }
                        try {
                            this.need_card_postcode = jsonObject2.getBoolean("need_zip");
                        } catch (Exception e9) {
                            this.need_card_postcode = false;
                        }
                        try {
                            this.need_card_name = jsonObject2.getBoolean("need_cardholder_name");
                        } catch (Exception e10) {
                            this.need_card_name = false;
                        }
                        if (jsonObject2.has("public_token")) {
                            this.providerApiKey = jsonObject2.getString("public_token");
                        } else {
                            try {
                                this.providerApiKey = jsonObject2.getString("publishable_key");
                            } catch (Exception e11) {
                                this.providerApiKey = "";
                            }
                        }
                        this.initGooglePay = !jsonObject2.optBoolean("google_pay_hidden", false);
                    } catch (Exception e12) {
                        FileLog.e(e12);
                    }
                }
                if (this.initGooglePay && ((!TextUtils.isEmpty(this.providerApiKey) && "stripe".equals(this.paymentForm.native_provider)) || this.googlePayParameters != null)) {
                    initGooglePay(context);
                }
                this.inputFields = new EditTextBoldCursor[6];
                int a2 = 0;
                for (int i5 = 6; a2 < i5; i5 = 6) {
                    if (a2 == 0) {
                        this.headerCell[0] = new HeaderCell(context, this.resourcesProvider);
                        this.headerCell[0].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        this.headerCell[0].setText(LocaleController.getString("PaymentCardTitle", R.string.PaymentCardTitle));
                        this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                    } else if (a2 == 4) {
                        this.headerCell[1] = new HeaderCell(context, this.resourcesProvider);
                        this.headerCell[1].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        this.headerCell[1].setText(LocaleController.getString("PaymentBillingAddress", R.string.PaymentBillingAddress));
                        this.linearLayout2.addView(this.headerCell[1], LayoutHelper.createLinear(-1, -2));
                    }
                    boolean allowDivider2 = (a2 == 3 || a2 == 5 || (a2 == 4 && !this.need_card_postcode)) ? false : true;
                    ViewGroup container3 = new FrameLayout(context);
                    container3.setClipChildren(false);
                    container3.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    this.linearLayout2.addView(container3, LayoutHelper.createLinear(-1, 50));
                    this.inputFields[a2] = new EditTextBoldCursor(context);
                    this.inputFields[a2].setTag(Integer.valueOf(a2));
                    this.inputFields[a2].setTextSize(1, 16.0f);
                    this.inputFields[a2].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                    this.inputFields[a2].setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                    this.inputFields[a2].setBackgroundDrawable(null);
                    this.inputFields[a2].setCursorColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                    this.inputFields[a2].setCursorSize(AndroidUtilities.dp(20.0f));
                    this.inputFields[a2].setCursorWidth(1.5f);
                    if (a2 == 3) {
                        InputFilter[] inputFilters2 = {new InputFilter.LengthFilter(3)};
                        this.inputFields[a2].setFilters(inputFilters2);
                        this.inputFields[a2].setInputType(TsExtractor.TS_STREAM_TYPE_HDMV_DTS);
                        this.inputFields[a2].setTypeface(Typeface.DEFAULT);
                        this.inputFields[a2].setTransformationMethod(PasswordTransformationMethod.getInstance());
                    } else if (a2 == 0) {
                        this.inputFields[a2].setInputType(3);
                    } else if (a2 == 4) {
                        this.inputFields[a2].setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda15
                            @Override // android.view.View.OnTouchListener
                            public final boolean onTouch(View view, MotionEvent motionEvent) {
                                return PaymentFormActivity.this.m4128lambda$createView$6$orgtelegramuiPaymentFormActivity(view, motionEvent);
                            }
                        });
                        this.inputFields[a2].setInputType(0);
                    } else if (a2 == 1) {
                        this.inputFields[a2].setInputType(InputDeviceCompat.SOURCE_STYLUS);
                    } else if (a2 == 2) {
                        this.inputFields[a2].setInputType(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    } else {
                        this.inputFields[a2].setInputType(16385);
                    }
                    this.inputFields[a2].setImeOptions(268435461);
                    switch (a2) {
                        case 0:
                            this.inputFields[a2].setHint(LocaleController.getString("PaymentCardNumber", R.string.PaymentCardNumber));
                            break;
                        case 1:
                            this.inputFields[a2].setHint(LocaleController.getString("PaymentCardExpireDate", R.string.PaymentCardExpireDate));
                            break;
                        case 2:
                            this.inputFields[a2].setHint(LocaleController.getString("PaymentCardName", R.string.PaymentCardName));
                            break;
                        case 3:
                            this.inputFields[a2].setHint(LocaleController.getString("PaymentCardCvv", R.string.PaymentCardCvv));
                            break;
                        case 4:
                            this.inputFields[a2].setHint(LocaleController.getString("PaymentShippingCountry", R.string.PaymentShippingCountry));
                            break;
                        case 5:
                            this.inputFields[a2].setHint(LocaleController.getString("PaymentShippingZipPlaceholder", R.string.PaymentShippingZipPlaceholder));
                            break;
                    }
                    if (a2 == 0) {
                        this.inputFields[a2].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.7
                            public static final int MAX_LENGTH_AMERICAN_EXPRESS = 15;
                            public static final int MAX_LENGTH_DINERS_CLUB = 14;
                            public static final int MAX_LENGTH_STANDARD = 16;
                            private int actionPosition;
                            public final String[] PREFIXES_15 = {"34", "37"};
                            public final String[] PREFIXES_14 = {"300", "301", "302", "303", "304", "305", "309", "36", "38", "39"};
                            public final String[] PREFIXES_16 = {"2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229", "2200", "2201", "2202", "2203", "2204", "223", "224", "225", "226", "227", "228", "229", "23", "24", "25", "26", "270", "271", "2720", "50", "51", "52", "53", "54", "55", "4", "60", "62", "64", "65", "35"};
                            private int characterAction = -1;

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                if (count == 0 && after == 1) {
                                    this.characterAction = 1;
                                } else if (count == 1 && after == 0) {
                                    if (s.charAt(start) == ' ' && start > 0) {
                                        this.characterAction = 3;
                                        this.actionPosition = start - 1;
                                        return;
                                    }
                                    this.characterAction = 2;
                                } else {
                                    this.characterAction = -1;
                                }
                            }

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override // android.text.TextWatcher
                            public void afterTextChanged(Editable editable) {
                                int start;
                                int start2;
                                int i6;
                                int i7;
                                int i8;
                                String resultHint;
                                int resultMaxLength;
                                String[] checkArr;
                                if (!PaymentFormActivity.this.ignoreOnCardChange) {
                                    EditText phoneField = PaymentFormActivity.this.inputFields[0];
                                    int start3 = phoneField.getSelectionStart();
                                    String str4 = phoneField.getText().toString();
                                    int i9 = 3;
                                    if (this.characterAction == 3) {
                                        str4 = str4.substring(0, this.actionPosition) + str4.substring(this.actionPosition + 1);
                                        start3--;
                                    }
                                    StringBuilder builder = new StringBuilder(str4.length());
                                    for (int a3 = 0; a3 < str4.length(); a3++) {
                                        String ch = str4.substring(a3, a3 + 1);
                                        if ("0123456789".contains(ch)) {
                                            builder.append(ch);
                                        }
                                    }
                                    PaymentFormActivity.this.ignoreOnCardChange = true;
                                    String hint = null;
                                    int maxLength = 100;
                                    if (builder.length() <= 0) {
                                        start = start3;
                                    } else {
                                        String currentString = builder.toString();
                                        int a4 = 0;
                                        while (true) {
                                            if (a4 < i9) {
                                                switch (a4) {
                                                    case 0:
                                                        checkArr = this.PREFIXES_16;
                                                        resultMaxLength = 16;
                                                        resultHint = "xxxx xxxx xxxx xxxx";
                                                        break;
                                                    case 1:
                                                        checkArr = this.PREFIXES_15;
                                                        resultMaxLength = 15;
                                                        resultHint = "xxxx xxxx xxxx xxx";
                                                        break;
                                                    default:
                                                        checkArr = this.PREFIXES_14;
                                                        resultMaxLength = 14;
                                                        resultHint = "xxxx xxxx xxxx xx";
                                                        break;
                                                }
                                                int b2 = 0;
                                                while (true) {
                                                    if (b2 < checkArr.length) {
                                                        String prefix = checkArr[b2];
                                                        start = start3;
                                                        if (currentString.length() <= prefix.length()) {
                                                            if (!prefix.startsWith(currentString)) {
                                                                b2++;
                                                                start3 = start;
                                                            } else {
                                                                hint = resultHint;
                                                                maxLength = resultMaxLength;
                                                            }
                                                        } else if (!currentString.startsWith(prefix)) {
                                                            b2++;
                                                            start3 = start;
                                                        } else {
                                                            hint = resultHint;
                                                            maxLength = resultMaxLength;
                                                        }
                                                    } else {
                                                        start = start3;
                                                    }
                                                }
                                                if (hint == null) {
                                                    a4++;
                                                    start3 = start;
                                                    i9 = 3;
                                                }
                                            } else {
                                                start = start3;
                                            }
                                        }
                                        if (builder.length() > maxLength) {
                                            builder.setLength(maxLength);
                                        }
                                    }
                                    if (hint != null) {
                                        if (builder.length() == maxLength) {
                                            PaymentFormActivity.this.inputFields[1].requestFocus();
                                        }
                                        phoneField.setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                                        int a5 = 0;
                                        start2 = start;
                                        while (true) {
                                            if (a5 < builder.length()) {
                                                if (a5 < hint.length()) {
                                                    if (hint.charAt(a5) == ' ') {
                                                        builder.insert(a5, ' ');
                                                        a5++;
                                                        if (start2 == a5 && (i8 = this.characterAction) != 2 && i8 != 3) {
                                                            start2++;
                                                        }
                                                    }
                                                    a5++;
                                                } else {
                                                    builder.insert(a5, ' ');
                                                    if (start2 == a5 + 1 && (i7 = this.characterAction) != 2 && i7 != 3) {
                                                        start2++;
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        if (builder.length() > 0) {
                                            i6 = PaymentFormActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteRedText4);
                                        } else {
                                            i6 = PaymentFormActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText);
                                        }
                                        phoneField.setTextColor(i6);
                                        start2 = start;
                                    }
                                    if (!builder.toString().equals(editable.toString())) {
                                        editable.replace(0, editable.length(), builder);
                                    }
                                    if (start2 >= 0) {
                                        phoneField.setSelection(Math.min(start2, phoneField.length()));
                                    }
                                    PaymentFormActivity.this.ignoreOnCardChange = false;
                                }
                            }
                        });
                    } else if (a2 == 1) {
                        this.inputFields[a2].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.8
                            private int actionPosition;
                            private int characterAction = -1;
                            private boolean isYear;

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                boolean z2 = false;
                                if (count == 0 && after == 1) {
                                    if (TextUtils.indexOf((CharSequence) PaymentFormActivity.this.inputFields[1].getText(), '/') != -1) {
                                        z2 = true;
                                    }
                                    this.isYear = z2;
                                    this.characterAction = 1;
                                } else if (count == 1 && after == 0) {
                                    if (s.charAt(start) == '/' && start > 0) {
                                        this.isYear = false;
                                        this.characterAction = 3;
                                        this.actionPosition = start - 1;
                                        return;
                                    }
                                    this.characterAction = 2;
                                } else {
                                    this.characterAction = -1;
                                }
                            }

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override // android.text.TextWatcher
                            public void afterTextChanged(Editable s) {
                                if (!PaymentFormActivity.this.ignoreOnCardChange) {
                                    EditText phoneField = PaymentFormActivity.this.inputFields[1];
                                    int start = phoneField.getSelectionStart();
                                    String str4 = phoneField.getText().toString();
                                    if (this.characterAction == 3) {
                                        str4 = str4.substring(0, this.actionPosition) + str4.substring(this.actionPosition + 1);
                                        start--;
                                    }
                                    StringBuilder builder = new StringBuilder(str4.length());
                                    for (int a3 = 0; a3 < str4.length(); a3++) {
                                        String ch = str4.substring(a3, a3 + 1);
                                        if ("0123456789".contains(ch)) {
                                            builder.append(ch);
                                        }
                                    }
                                    PaymentFormActivity.this.ignoreOnCardChange = true;
                                    PaymentFormActivity.this.inputFields[1].setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                                    if (builder.length() > 4) {
                                        builder.setLength(4);
                                    }
                                    if (builder.length() < 2) {
                                        this.isYear = false;
                                    }
                                    boolean isError = false;
                                    if (this.isYear) {
                                        String[] args2 = new String[builder.length() > 2 ? 2 : 1];
                                        args2[0] = builder.substring(0, 2);
                                        if (args2.length == 2) {
                                            args2[1] = builder.substring(2);
                                        }
                                        if (builder.length() == 4 && args2.length == 2) {
                                            int month = Utilities.parseInt((CharSequence) args2[0]).intValue();
                                            int year = Utilities.parseInt((CharSequence) args2[1]).intValue() + 2000;
                                            Calendar rightNow = Calendar.getInstance();
                                            int currentYear = rightNow.get(1);
                                            int currentMonth = rightNow.get(2) + 1;
                                            if (year < currentYear || (year == currentYear && month < currentMonth)) {
                                                PaymentFormActivity.this.inputFields[1].setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteRedText4));
                                                isError = true;
                                            }
                                        } else {
                                            int value2 = Utilities.parseInt((CharSequence) args2[0]).intValue();
                                            if (value2 > 12 || value2 == 0) {
                                                PaymentFormActivity.this.inputFields[1].setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteRedText4));
                                                isError = true;
                                            }
                                        }
                                    } else if (builder.length() == 1) {
                                        int value3 = Utilities.parseInt((CharSequence) builder.toString()).intValue();
                                        if (value3 != 1 && value3 != 0) {
                                            builder.insert(0, "0");
                                            start++;
                                        }
                                    } else if (builder.length() == 2) {
                                        int value4 = Utilities.parseInt((CharSequence) builder.toString()).intValue();
                                        if (value4 > 12 || value4 == 0) {
                                            PaymentFormActivity.this.inputFields[1].setTextColor(PaymentFormActivity.this.getThemedColor(Theme.key_windowBackgroundWhiteRedText4));
                                            isError = true;
                                        }
                                        start++;
                                    }
                                    if (!isError && builder.length() == 4) {
                                        PaymentFormActivity.this.inputFields[PaymentFormActivity.this.need_card_name ? (char) 2 : (char) 3].requestFocus();
                                    }
                                    if (builder.length() == 2) {
                                        builder.append('/');
                                        start++;
                                    } else if (builder.length() > 2 && builder.charAt(2) != '/') {
                                        builder.insert(2, '/');
                                        start++;
                                    }
                                    phoneField.setText(builder);
                                    if (start >= 0) {
                                        phoneField.setSelection(Math.min(start, phoneField.length()));
                                    }
                                    PaymentFormActivity.this.ignoreOnCardChange = false;
                                }
                            }
                        });
                    }
                    this.inputFields[a2].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                    this.inputFields[a2].setGravity(LocaleController.isRTL ? 5 : 3);
                    container3.addView(this.inputFields[a2], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                    this.inputFields[a2].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda22
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView2, int i6, KeyEvent keyEvent) {
                            return PaymentFormActivity.this.m4129lambda$createView$7$orgtelegramuiPaymentFormActivity(textView2, i6, keyEvent);
                        }
                    });
                    if (a2 == 3) {
                        this.sectionCell[0] = new ShadowSectionCell(context, this.resourcesProvider);
                        this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(-1, -2));
                    } else if (a2 == 5) {
                        this.sectionCell[2] = new ShadowSectionCell(context, this.resourcesProvider);
                        this.linearLayout2.addView(this.sectionCell[2], LayoutHelper.createLinear(-1, -2));
                        TextCheckCell textCheckCell3 = new TextCheckCell(context, this.resourcesProvider);
                        this.checkCell1 = textCheckCell3;
                        textCheckCell3.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        this.checkCell1.setTextAndCheck(LocaleController.getString("PaymentCardSavePaymentInformation", R.string.PaymentCardSavePaymentInformation), this.saveCardInfo, false);
                        this.linearLayout2.addView(this.checkCell1, LayoutHelper.createLinear(-1, -2));
                        this.checkCell1.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda9
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PaymentFormActivity.this.m4130lambda$createView$8$orgtelegramuiPaymentFormActivity(view);
                            }
                        });
                        this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                        this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        updateSavePaymentField();
                        this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                    } else if (a2 == 0) {
                        createGooglePayButton(context);
                        container3.addView(this.googlePayContainer, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 3 : 5) | 16, 0.0f, 0.0f, 4.0f, 0.0f));
                    }
                    if (allowDivider2) {
                        View divider2 = new View(context) { // from class: org.telegram.ui.PaymentFormActivity.9
                            @Override // android.view.View
                            protected void onDraw(Canvas canvas) {
                                canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                            }
                        };
                        divider2.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        this.dividers.add(divider2);
                        container3.addView(divider2, new FrameLayout.LayoutParams(-1, 1, 83));
                    }
                    if ((a2 == 4 && !this.need_card_country) || ((a2 == 5 && !this.need_card_postcode) || (a2 == 2 && !this.need_card_name))) {
                        container3.setVisibility(8);
                    }
                    a2++;
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
        } else if (i4 == 1) {
            int count = this.requestedInfo.shipping_options.size();
            this.radioCells = new RadioCell[count];
            int a3 = 0;
            while (a3 < count) {
                TLRPC.TL_shippingOption shippingOption = this.requestedInfo.shipping_options.get(a3);
                this.radioCells[a3] = new RadioCell(context);
                this.radioCells[a3].setTag(Integer.valueOf(a3));
                this.radioCells[a3].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                this.radioCells[a3].setText(String.format("%s - %s", getTotalPriceString(shippingOption.prices), shippingOption.title), a3 == 0, a3 != count + (-1));
                this.radioCells[a3].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda11
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PaymentFormActivity.this.m4131lambda$createView$9$orgtelegramuiPaymentFormActivity(view);
                    }
                });
                this.linearLayout2.addView(this.radioCells[a3]);
                a3++;
            }
            this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
            this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
            this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
        } else if (i4 == 3) {
            this.inputFields = new EditTextBoldCursor[2];
            int a4 = 0;
            for (int i6 = 2; a4 < i6; i6 = 2) {
                if (a4 == 0) {
                    z = false;
                    this.headerCell[0] = new HeaderCell(context, this.resourcesProvider);
                    this.headerCell[0].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    this.headerCell[0].setText(LocaleController.getString("PaymentCardTitle", R.string.PaymentCardTitle));
                    this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                } else {
                    z = false;
                }
                ViewGroup container4 = new FrameLayout(context);
                container4.setClipChildren(z);
                this.linearLayout2.addView(container4, LayoutHelper.createLinear(-1, 50));
                container4.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                boolean allowDivider3 = a4 != 1;
                if (allowDivider3) {
                    if (a4 == 7 && !this.paymentForm.invoice.phone_requested) {
                        allowDivider3 = false;
                    } else if (a4 == 6 && !this.paymentForm.invoice.phone_requested && !this.paymentForm.invoice.email_requested) {
                        allowDivider3 = false;
                    }
                }
                if (allowDivider3) {
                    View divider3 = new View(context) { // from class: org.telegram.ui.PaymentFormActivity.10
                        @Override // android.view.View
                        protected void onDraw(Canvas canvas) {
                            canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                        }
                    };
                    divider3.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    this.dividers.add(divider3);
                    container4.addView(divider3, new FrameLayout.LayoutParams(-1, 1, 83));
                }
                this.inputFields[a4] = new EditTextBoldCursor(context);
                this.inputFields[a4].setTag(Integer.valueOf(a4));
                this.inputFields[a4].setTextSize(1, 16.0f);
                this.inputFields[a4].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                this.inputFields[a4].setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                this.inputFields[a4].setBackgroundDrawable(null);
                this.inputFields[a4].setCursorColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                this.inputFields[a4].setCursorSize(AndroidUtilities.dp(20.0f));
                this.inputFields[a4].setCursorWidth(1.5f);
                if (a4 == 0) {
                    this.inputFields[a4].setOnTouchListener(PaymentFormActivity$$ExternalSyntheticLambda16.INSTANCE);
                    this.inputFields[a4].setInputType(0);
                } else {
                    this.inputFields[a4].setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
                    this.inputFields[a4].setTypeface(Typeface.DEFAULT);
                }
                this.inputFields[a4].setImeOptions(268435462);
                switch (a4) {
                    case 0:
                        this.inputFields[a4].setText(this.paymentForm.saved_credentials.title);
                        break;
                    case 1:
                        this.inputFields[a4].setHint(LocaleController.getString("LoginPassword", R.string.LoginPassword));
                        this.inputFields[a4].requestFocus();
                        break;
                }
                this.inputFields[a4].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                this.inputFields[a4].setGravity(LocaleController.isRTL ? 5 : 3);
                container4.addView(this.inputFields[a4], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                this.inputFields[a4].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda17
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView2, int i7, KeyEvent keyEvent) {
                        return PaymentFormActivity.this.m4105lambda$createView$11$orgtelegramuiPaymentFormActivity(textView2, i7, keyEvent);
                    }
                });
                if (a4 == 1) {
                    this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                    this.bottomCell[0].setText(LocaleController.formatString("PaymentConfirmationMessage", R.string.PaymentConfirmationMessage, this.paymentForm.saved_credentials.title));
                    this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                    this.settingsCell[0] = new TextSettingsCell(context, this.resourcesProvider);
                    this.settingsCell[0].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                    this.settingsCell[0].setText(LocaleController.getString("PaymentConfirmationNewCard", R.string.PaymentConfirmationNewCard), false);
                    this.linearLayout2.addView(this.settingsCell[0], LayoutHelper.createLinear(-1, -2));
                    this.settingsCell[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda61
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PaymentFormActivity.this.m4106lambda$createView$12$orgtelegramuiPaymentFormActivity(view);
                        }
                    });
                    this.bottomCell[1] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                    this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                    this.linearLayout2.addView(this.bottomCell[1], LayoutHelper.createLinear(-1, -2));
                }
                a4++;
            }
        } else {
            if (i4 != 4 && i4 != 5) {
                if (i4 == 6) {
                    EditTextSettingsCell editTextSettingsCell = new EditTextSettingsCell(context);
                    this.codeFieldCell = editTextSettingsCell;
                    editTextSettingsCell.setTextAndHint("", LocaleController.getString("PasswordCode", R.string.PasswordCode), false);
                    this.codeFieldCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                    EditTextBoldCursor editText = this.codeFieldCell.getTextView();
                    editText.setInputType(3);
                    editText.setImeOptions(6);
                    editText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda19
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView2, int i7, KeyEvent keyEvent) {
                            return PaymentFormActivity.this.m4120lambda$createView$26$orgtelegramuiPaymentFormActivity(textView2, i7, keyEvent);
                        }
                    });
                    editText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.20
                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence s, int start, int count2, int after) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence s, int start, int before, int count2) {
                        }

                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable s) {
                            if (PaymentFormActivity.this.emailCodeLength != 0 && s.length() == PaymentFormActivity.this.emailCodeLength) {
                                PaymentFormActivity.this.sendSavePassword(false);
                            }
                        }
                    });
                    this.linearLayout2.addView(this.codeFieldCell, LayoutHelper.createLinear(-1, -2));
                    this.bottomCell[2] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                    this.bottomCell[2].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                    this.linearLayout2.addView(this.bottomCell[2], LayoutHelper.createLinear(-1, -2));
                    this.settingsCell[1] = new TextSettingsCell(context, this.resourcesProvider);
                    this.settingsCell[1].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                    this.settingsCell[1].setTag(Theme.key_windowBackgroundWhiteBlackText);
                    this.settingsCell[1].setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                    this.settingsCell[1].setText(LocaleController.getString("ResendCode", R.string.ResendCode), true);
                    this.linearLayout2.addView(this.settingsCell[1], LayoutHelper.createLinear(-1, -2));
                    this.settingsCell[1].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda5
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PaymentFormActivity.this.m4121lambda$createView$28$orgtelegramuiPaymentFormActivity(view);
                        }
                    });
                    this.settingsCell[0] = new TextSettingsCell(context, this.resourcesProvider);
                    this.settingsCell[0].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                    this.settingsCell[0].setTag(Theme.key_windowBackgroundWhiteRedText3);
                    this.settingsCell[0].setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteRedText3));
                    this.settingsCell[0].setText(LocaleController.getString("AbortPassword", R.string.AbortPassword), false);
                    this.linearLayout2.addView(this.settingsCell[0], LayoutHelper.createLinear(-1, -2));
                    this.settingsCell[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda7
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PaymentFormActivity.this.m4124lambda$createView$30$orgtelegramuiPaymentFormActivity(view);
                        }
                    });
                    this.inputFields = new EditTextBoldCursor[3];
                    int a5 = 0;
                    for (int i7 = 3; a5 < i7; i7 = 3) {
                        if (a5 == 0) {
                            this.headerCell[0] = new HeaderCell(context, this.resourcesProvider);
                            this.headerCell[0].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            this.headerCell[0].setText(LocaleController.getString("PaymentPasswordTitle", R.string.PaymentPasswordTitle));
                            this.linearLayout2.addView(this.headerCell[0], LayoutHelper.createLinear(-1, -2));
                        } else if (a5 == 2) {
                            this.headerCell[1] = new HeaderCell(context, this.resourcesProvider);
                            this.headerCell[1].setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            this.headerCell[1].setText(LocaleController.getString("PaymentPasswordEmailTitle", R.string.PaymentPasswordEmailTitle));
                            this.linearLayout2.addView(this.headerCell[1], LayoutHelper.createLinear(-1, -2));
                        }
                        ViewGroup container5 = new FrameLayout(context);
                        container5.setClipChildren(false);
                        this.linearLayout2.addView(container5, LayoutHelper.createLinear(-1, 50));
                        container5.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                        if (a5 == 0) {
                            View divider4 = new View(context) { // from class: org.telegram.ui.PaymentFormActivity.21
                                @Override // android.view.View
                                protected void onDraw(Canvas canvas) {
                                    canvas.drawLine(LocaleController.isRTL ? 0.0f : AndroidUtilities.dp(20.0f), getMeasuredHeight() - 1, getMeasuredWidth() - (LocaleController.isRTL ? AndroidUtilities.dp(20.0f) : 0), getMeasuredHeight() - 1, Theme.dividerPaint);
                                }
                            };
                            divider4.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                            this.dividers.add(divider4);
                            container5.addView(divider4, new FrameLayout.LayoutParams(-1, 1, 83));
                        }
                        this.inputFields[a5] = new EditTextBoldCursor(context);
                        this.inputFields[a5].setTag(Integer.valueOf(a5));
                        this.inputFields[a5].setTextSize(1, 16.0f);
                        this.inputFields[a5].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteHintText));
                        this.inputFields[a5].setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                        this.inputFields[a5].setBackgroundDrawable(null);
                        this.inputFields[a5].setCursorColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                        this.inputFields[a5].setCursorSize(AndroidUtilities.dp(20.0f));
                        this.inputFields[a5].setCursorWidth(1.5f);
                        if (a5 == 0 || a5 == 1) {
                            this.inputFields[a5].setInputType(TsExtractor.TS_STREAM_TYPE_AC3);
                            this.inputFields[a5].setTypeface(Typeface.DEFAULT);
                            this.inputFields[a5].setImeOptions(268435461);
                        } else {
                            this.inputFields[a5].setInputType(33);
                            this.inputFields[a5].setImeOptions(268435462);
                        }
                        switch (a5) {
                            case 0:
                                this.inputFields[a5].setHint(LocaleController.getString("PaymentPasswordEnter", R.string.PaymentPasswordEnter));
                                this.inputFields[a5].requestFocus();
                                break;
                            case 1:
                                this.inputFields[a5].setHint(LocaleController.getString("PaymentPasswordReEnter", R.string.PaymentPasswordReEnter));
                                break;
                            case 2:
                                this.inputFields[a5].setHint(LocaleController.getString("PaymentPasswordEmail", R.string.PaymentPasswordEmail));
                                break;
                        }
                        this.inputFields[a5].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                        this.inputFields[a5].setGravity(LocaleController.isRTL ? 5 : 3);
                        container5.addView(this.inputFields[a5], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 12.0f, 21.0f, 6.0f));
                        this.inputFields[a5].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda20
                            @Override // android.widget.TextView.OnEditorActionListener
                            public final boolean onEditorAction(TextView textView2, int i8, KeyEvent keyEvent) {
                                return PaymentFormActivity.this.m4125lambda$createView$31$orgtelegramuiPaymentFormActivity(textView2, i8, keyEvent);
                            }
                        });
                        if (a5 == 1) {
                            this.bottomCell[0] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                            this.bottomCell[0].setText(LocaleController.getString("PaymentPasswordInfo", R.string.PaymentPasswordInfo));
                            this.bottomCell[0].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
                            this.linearLayout2.addView(this.bottomCell[0], LayoutHelper.createLinear(-1, -2));
                        } else if (a5 == 2) {
                            this.bottomCell[1] = new TextInfoPrivacyCell(context, this.resourcesProvider);
                            this.bottomCell[1].setText(LocaleController.getString("PaymentPasswordEmailInfo", R.string.PaymentPasswordEmailInfo));
                            this.bottomCell[1].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                            this.linearLayout2.addView(this.bottomCell[1], LayoutHelper.createLinear(-1, -2));
                        }
                        a5++;
                    }
                    updatePasswordFields();
                }
            }
            PaymentInfoCell paymentInfoCell = new PaymentInfoCell(context);
            this.paymentInfoCell = paymentInfoCell;
            paymentInfoCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
            MessageObject messageObject = this.messageObject;
            if (messageObject != null) {
                this.paymentInfoCell.setInvoice((TLRPC.TL_messageMediaInvoice) messageObject.messageOwner.media, this.currentBotName);
            } else {
                TLRPC.TL_payments_paymentReceipt tL_payments_paymentReceipt = this.paymentReceipt;
                if (tL_payments_paymentReceipt != null) {
                    this.paymentInfoCell.setReceipt(tL_payments_paymentReceipt, this.currentBotName);
                } else if (this.invoiceSlug != null) {
                    this.paymentInfoCell.setInfo(this.paymentForm.title, this.paymentForm.description, this.paymentForm.photo, this.currentBotName, this.paymentForm);
                }
            }
            this.linearLayout2.addView(this.paymentInfoCell, LayoutHelper.createLinear(-1, -2));
            this.sectionCell[0] = new ShadowSectionCell(context, this.resourcesProvider);
            this.linearLayout2.addView(this.sectionCell[0], LayoutHelper.createLinear(-1, -2));
            ArrayList<TLRPC.TL_labeledPrice> arrayList = new ArrayList<>(this.paymentForm.invoice.prices);
            this.prices = arrayList;
            TLRPC.TL_shippingOption tL_shippingOption = this.shippingOption;
            if (tL_shippingOption != null) {
                arrayList.addAll(tL_shippingOption.prices);
            }
            this.totalPrice = new String[1];
            for (int a6 = 0; a6 < this.prices.size(); a6++) {
                TLRPC.TL_labeledPrice price = this.prices.get(a6);
                TextPriceCell priceCell = new TextPriceCell(context);
                priceCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                priceCell.setTextAndValue(price.label, LocaleController.getInstance().formatCurrencyString(price.amount, this.paymentForm.invoice.currency), false);
                this.linearLayout2.addView(priceCell);
            }
            int a7 = this.currentStep;
            if (a7 == 5 && this.tipAmount != null) {
                TextPriceCell priceCell2 = new TextPriceCell(context);
                priceCell2.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                priceCell2.setTextAndValue(LocaleController.getString("PaymentTip", R.string.PaymentTip), LocaleController.getInstance().formatCurrencyString(this.tipAmount.longValue(), this.paymentForm.invoice.currency), false);
                this.linearLayout2.addView(priceCell2);
            }
            TextPriceCell textPriceCell = new TextPriceCell(context);
            this.totalCell = textPriceCell;
            textPriceCell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
            this.totalPrice[0] = getTotalPriceString(this.prices);
            this.totalCell.setTextAndValue(LocaleController.getString("PaymentTransactionTotal", R.string.PaymentTransactionTotal), this.totalPrice[0], true);
            if (this.currentStep != 4 || (this.paymentForm.invoice.flags & 256) == 0) {
                str = Theme.key_windowBackgroundGrayShadow;
                str2 = Theme.key_windowBackgroundWhite;
                c2 = 3;
                c = 2;
            } else {
                ViewGroup container6 = new FrameLayout(context);
                container6.setClipChildren(false);
                container6.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                this.linearLayout2.addView(container6, LayoutHelper.createLinear(-1, this.paymentForm.invoice.suggested_tip_amounts.isEmpty() ? 40 : 78));
                container6.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda62
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PaymentFormActivity.this.m4107lambda$createView$13$orgtelegramuiPaymentFormActivity(view);
                    }
                });
                TextPriceCell cell = new TextPriceCell(context);
                cell.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
                cell.setTextAndValue(LocaleController.getString("PaymentTipOptional", R.string.PaymentTipOptional), "", false);
                container6.addView(cell);
                this.inputFields = r4;
                EditTextBoldCursor[] editTextBoldCursorArr2 = {new EditTextBoldCursor(context)};
                this.inputFields[0].setTag(0);
                this.inputFields[0].setTextSize(1, 16.0f);
                this.inputFields[0].setHintTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText2));
                this.inputFields[0].setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText2));
                this.inputFields[0].setBackgroundDrawable(null);
                this.inputFields[0].setCursorColor(getThemedColor(Theme.key_windowBackgroundWhiteBlackText));
                this.inputFields[0].setCursorSize(AndroidUtilities.dp(20.0f));
                this.inputFields[0].setCursorWidth(1.5f);
                this.inputFields[0].setInputType(3);
                this.inputFields[0].setImeOptions(268435462);
                this.inputFields[0].setHint(LocaleController.getInstance().formatCurrencyString(0L, this.paymentForm.invoice.currency));
                this.inputFields[0].setPadding(0, 0, 0, AndroidUtilities.dp(6.0f));
                this.inputFields[0].setGravity(LocaleController.isRTL ? 3 : 5);
                container6.addView(this.inputFields[0], LayoutHelper.createFrame(-1, -2.0f, 51, 21.0f, 9.0f, 21.0f, 1.0f));
                this.inputFields[0].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.PaymentFormActivity.11
                    private boolean anyBefore;
                    private int beforeTextLength;
                    char[] commas = {',', '.', 1643, 12289, 11841, 65040, 65041, 65104, 65105, 65292, 65380, 699};
                    private int enteredCharacterStart;
                    private boolean isDeletedChar;
                    private boolean lastDotEntered;
                    private String overrideText;

                    private int indexOfComma(String text) {
                        int a8 = 0;
                        while (true) {
                            char[] cArr = this.commas;
                            if (a8 < cArr.length) {
                                int idx = text.indexOf(cArr[a8]);
                                if (idx < 0) {
                                    a8++;
                                } else {
                                    return idx;
                                }
                            } else {
                                return -1;
                            }
                        }
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence s, int start, int count2, int after) {
                        int start2 = start;
                        if (!PaymentFormActivity.this.ignoreOnTextChange) {
                            boolean z2 = true;
                            this.anyBefore = !TextUtils.isEmpty(s);
                            this.overrideText = null;
                            this.beforeTextLength = s == null ? 0 : s.length();
                            this.enteredCharacterStart = start2;
                            if (count2 != 1 || after != 0) {
                                z2 = false;
                            }
                            this.isDeletedChar = z2;
                            if (z2) {
                                String fixed = LocaleController.fixNumbers(s);
                                char actionCh = fixed.charAt(start2);
                                int idx = indexOfComma(fixed);
                                String reminderStr = idx >= 0 ? fixed.substring(idx + 1) : "";
                                long reminder = Utilities.parseLong(PhoneFormat.stripExceptNumbers(reminderStr)).longValue();
                                if ((actionCh >= '0' && actionCh <= '9') || (reminderStr.length() != 0 && reminder == 0)) {
                                    if (idx > 0 && start2 > idx && reminder == 0) {
                                        this.overrideText = fixed.substring(0, idx - 1);
                                        return;
                                    }
                                    return;
                                }
                                while (true) {
                                    start2--;
                                    if (start2 >= 0) {
                                        char actionCh2 = fixed.charAt(start2);
                                        if (actionCh2 >= '0' && actionCh2 <= '9') {
                                            this.overrideText = fixed.substring(0, start2) + fixed.substring(start2 + 1);
                                            return;
                                        }
                                    } else {
                                        return;
                                    }
                                }
                            }
                        }
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence s, int start, int before, int count2) {
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable s) {
                        String text;
                        long reminder;
                        int start;
                        String newText;
                        int start2;
                        String reminderStr;
                        if (!PaymentFormActivity.this.ignoreOnTextChange) {
                            long oldAmount = PaymentFormActivity.this.tipAmount != null ? PaymentFormActivity.this.tipAmount.longValue() : 0L;
                            if (this.overrideText != null) {
                                text = this.overrideText;
                            } else {
                                text = LocaleController.fixNumbers(s.toString());
                            }
                            int idx = indexOfComma(text);
                            boolean dotEntered = idx >= 0;
                            int exp = LocaleController.getCurrencyExpDivider(PaymentFormActivity.this.paymentForm.invoice.currency);
                            String wholeStr = idx >= 0 ? text.substring(0, idx) : text;
                            String reminderStr2 = idx >= 0 ? text.substring(idx + 1) : "";
                            long whole = exp * Utilities.parseLong(PhoneFormat.stripExceptNumbers(wholeStr)).longValue();
                            long reminder2 = Utilities.parseLong(PhoneFormat.stripExceptNumbers(reminderStr2)).longValue();
                            String reminderStr3 = "" + reminder2;
                            StringBuilder sb = new StringBuilder();
                            sb.append("");
                            sb.append(exp - 1);
                            String expStr = sb.toString();
                            if (idx <= 0 || reminderStr3.length() <= expStr.length()) {
                                reminder = reminder2;
                            } else {
                                if (this.enteredCharacterStart - idx < reminderStr3.length()) {
                                    reminderStr = reminderStr3.substring(0, expStr.length());
                                } else {
                                    reminderStr = reminderStr3.substring(reminderStr3.length() - expStr.length());
                                }
                                reminder = Utilities.parseLong(reminderStr).longValue();
                            }
                            PaymentFormActivity.this.tipAmount = Long.valueOf(whole + reminder);
                            if (PaymentFormActivity.this.paymentForm.invoice.max_tip_amount != 0 && PaymentFormActivity.this.tipAmount.longValue() > PaymentFormActivity.this.paymentForm.invoice.max_tip_amount) {
                                PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                                paymentFormActivity.tipAmount = Long.valueOf(paymentFormActivity.paymentForm.invoice.max_tip_amount);
                            }
                            int start3 = PaymentFormActivity.this.inputFields[0].getSelectionStart();
                            PaymentFormActivity.this.ignoreOnTextChange = true;
                            if (PaymentFormActivity.this.tipAmount.longValue() == 0) {
                                newText = "";
                                PaymentFormActivity.this.inputFields[0].setText("");
                                start = start3;
                            } else {
                                EditTextBoldCursor editTextBoldCursor2 = PaymentFormActivity.this.inputFields[0];
                                start = start3;
                                String formatCurrencyString = LocaleController.getInstance().formatCurrencyString(PaymentFormActivity.this.tipAmount.longValue(), false, dotEntered, true, PaymentFormActivity.this.paymentForm.invoice.currency);
                                newText = formatCurrencyString;
                                editTextBoldCursor2.setText(formatCurrencyString);
                            }
                            if (oldAmount >= PaymentFormActivity.this.tipAmount.longValue() || oldAmount == 0 || !this.anyBefore) {
                                start2 = start;
                            } else {
                                start2 = start;
                                if (start2 >= 0) {
                                    PaymentFormActivity.this.inputFields[0].setSelection(Math.min(start2, PaymentFormActivity.this.inputFields[0].length()));
                                    this.lastDotEntered = dotEntered;
                                    PaymentFormActivity.this.updateTotalPrice();
                                    this.overrideText = null;
                                    PaymentFormActivity.this.ignoreOnTextChange = false;
                                }
                            }
                            if (this.isDeletedChar && this.beforeTextLength != PaymentFormActivity.this.inputFields[0].length()) {
                                PaymentFormActivity.this.inputFields[0].setSelection(Math.max(0, Math.min(start2, PaymentFormActivity.this.inputFields[0].length())));
                            } else if (this.lastDotEntered || !dotEntered || idx < 0) {
                                PaymentFormActivity.this.inputFields[0].setSelection(PaymentFormActivity.this.inputFields[0].length());
                            } else {
                                int idx2 = indexOfComma(newText);
                                if (idx2 > 0) {
                                    PaymentFormActivity.this.inputFields[0].setSelection(idx2 + 1);
                                } else {
                                    PaymentFormActivity.this.inputFields[0].setSelection(PaymentFormActivity.this.inputFields[0].length());
                                }
                            }
                            this.lastDotEntered = dotEntered;
                            PaymentFormActivity.this.updateTotalPrice();
                            this.overrideText = null;
                            PaymentFormActivity.this.ignoreOnTextChange = false;
                        }
                    }
                });
                this.inputFields[0].setOnEditorActionListener(PaymentFormActivity$$ExternalSyntheticLambda23.INSTANCE);
                this.inputFields[0].requestFocus();
                if (!this.paymentForm.invoice.suggested_tip_amounts.isEmpty()) {
                    HorizontalScrollView scrollView2 = new HorizontalScrollView(context);
                    scrollView2.setHorizontalScrollBarEnabled(false);
                    scrollView2.setVerticalScrollBarEnabled(false);
                    scrollView2.setClipToPadding(false);
                    scrollView2.setPadding(AndroidUtilities.dp(21.0f), 0, AndroidUtilities.dp(21.0f), 0);
                    scrollView2.setFillViewport(true);
                    container6.addView(scrollView2, LayoutHelper.createFrame(-1, 30.0f, 51, 0.0f, 44.0f, 0.0f, 0.0f));
                    final int[] maxTextWidth = new int[1];
                    final int[] textWidths = new int[1];
                    final int N = this.paymentForm.invoice.suggested_tip_amounts.size();
                    c2 = 3;
                    str2 = Theme.key_windowBackgroundWhite;
                    c = 2;
                    str = Theme.key_windowBackgroundGrayShadow;
                    LinearLayout linearLayout2 = new LinearLayout(context) { // from class: org.telegram.ui.PaymentFormActivity.12
                        boolean ignoreLayout;

                        @Override // android.widget.LinearLayout, android.view.View
                        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                            int availableSize = View.MeasureSpec.getSize(widthMeasureSpec);
                            this.ignoreLayout = true;
                            int dp = AndroidUtilities.dp(9.0f);
                            int i8 = N;
                            int gaps = dp * (i8 - 1);
                            if ((maxTextWidth[0] * i8) + gaps <= availableSize) {
                                setWeightSum(1.0f);
                                int N2 = getChildCount();
                                for (int a8 = 0; a8 < N2; a8++) {
                                    getChildAt(a8).getLayoutParams().width = 0;
                                    ((LinearLayout.LayoutParams) getChildAt(a8).getLayoutParams()).weight = 1.0f / N2;
                                }
                            } else if (textWidths[0] + gaps <= availableSize) {
                                setWeightSum(1.0f);
                                int availableSize2 = availableSize - gaps;
                                float extraWeight = 1.0f;
                                int N22 = getChildCount();
                                for (int a9 = 0; a9 < N22; a9++) {
                                    View child = getChildAt(a9);
                                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) child.getLayoutParams();
                                    layoutParams.width = 0;
                                    int width = ((Integer) child.getTag(R.id.width_tag)).intValue();
                                    layoutParams.weight = width / availableSize2;
                                    extraWeight -= layoutParams.weight;
                                }
                                int a10 = N;
                                float extraWeight2 = extraWeight / (a10 - 1);
                                if (extraWeight2 > 0.0f) {
                                    int N23 = getChildCount();
                                    for (int a11 = 0; a11 < N23; a11++) {
                                        View child2 = getChildAt(a11);
                                        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) child2.getLayoutParams();
                                        int width2 = ((Integer) child2.getTag(R.id.width_tag)).intValue();
                                        if (width2 != maxTextWidth[0]) {
                                            layoutParams2.weight += extraWeight2;
                                        }
                                    }
                                }
                            } else {
                                setWeightSum(0.0f);
                                int N24 = getChildCount();
                                for (int a12 = 0; a12 < N24; a12++) {
                                    getChildAt(a12).getLayoutParams().width = -2;
                                    ((LinearLayout.LayoutParams) getChildAt(a12).getLayoutParams()).weight = 0.0f;
                                }
                            }
                            this.ignoreLayout = false;
                            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
                    scrollView2.addView(this.tipLayout, LayoutHelper.createScroll(-1, 30, 51));
                    int color = getThemedColor(Theme.key_contacts_inviteBackground);
                    int a8 = 0;
                    while (a8 < N) {
                        if (LocaleController.isRTL) {
                            amount = this.paymentForm.invoice.suggested_tip_amounts.get((N - a8) - 1).longValue();
                        } else {
                            amount = this.paymentForm.invoice.suggested_tip_amounts.get(a8).longValue();
                        }
                        String text = LocaleController.getInstance().formatCurrencyString(amount, this.paymentForm.invoice.currency);
                        final TextView valueTextView = new TextView(context);
                        valueTextView.setTextSize(1, 14.0f);
                        valueTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                        valueTextView.setLines(1);
                        valueTextView.setTag(Long.valueOf(amount));
                        valueTextView.setMaxLines(1);
                        valueTextView.setText(text);
                        HorizontalScrollView scrollView3 = scrollView2;
                        valueTextView.setPadding(AndroidUtilities.dp(15.0f), 0, AndroidUtilities.dp(15.0f), 0);
                        valueTextView.setTextColor(getThemedColor(Theme.key_chats_secretName));
                        valueTextView.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(15.0f), 536870911 & color));
                        valueTextView.setSingleLine(true);
                        valueTextView.setGravity(17);
                        this.tipLayout.addView(valueTextView, LayoutHelper.createLinear(-2, -1, 19, 0, 0, a8 != N + (-1) ? 9 : 0, 0));
                        valueTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda12
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PaymentFormActivity.this.m4108lambda$createView$15$orgtelegramuiPaymentFormActivity(valueTextView, amount, view);
                            }
                        });
                        int width = ((int) Math.ceil(valueTextView.getPaint().measureText(text))) + AndroidUtilities.dp(30.0f);
                        valueTextView.setTag(R.id.width_tag, Integer.valueOf(width));
                        maxTextWidth[0] = Math.max(maxTextWidth[0], width);
                        textWidths[0] = textWidths[0] + width;
                        a8++;
                        scrollView2 = scrollView3;
                    }
                } else {
                    str2 = Theme.key_windowBackgroundWhite;
                    c2 = 3;
                    c = 2;
                    str = Theme.key_windowBackgroundGrayShadow;
                }
            }
            this.linearLayout2.addView(this.totalCell);
            this.sectionCell[c] = new ShadowSectionCell(context, this.resourcesProvider);
            this.sectionCell[c].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, str));
            this.linearLayout2.addView(this.sectionCell[c], LayoutHelper.createLinear(-1, -2));
            this.detailSettingsCell[0] = new TextDetailSettingsCell(context);
            this.detailSettingsCell[0].setBackgroundDrawable(Theme.getSelectorDrawable(true));
            TextDetailSettingsCell textDetailSettingsCell = this.detailSettingsCell[0];
            String str4 = this.cardName;
            textDetailSettingsCell.setTextAndValueAndIcon((str4 == null || str4.length() <= 1) ? this.cardName : this.cardName.substring(0, 1).toUpperCase() + this.cardName.substring(1), LocaleController.getString("PaymentCheckoutMethod", R.string.PaymentCheckoutMethod), R.drawable.msg_payment_card, true);
            int cardInfoVisibility = 0;
            if (this.isCheckoutPreview) {
                String str5 = this.cardName;
                cardInfoVisibility = (str5 == null || str5.length() <= 1) ? 8 : 0;
            }
            this.detailSettingsCell[0].setVisibility(cardInfoVisibility);
            this.linearLayout2.addView(this.detailSettingsCell[0]);
            if (this.currentStep == 4) {
                this.detailSettingsCell[0].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda63
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PaymentFormActivity.this.m4110lambda$createView$17$orgtelegramuiPaymentFormActivity(view);
                    }
                });
            }
            TLRPC.User providerUser2 = null;
            for (int a9 = 0; a9 < this.paymentForm.users.size(); a9++) {
                TLRPC.User user2 = this.paymentForm.users.get(a9);
                if (user2.id == this.paymentForm.provider_id) {
                    providerUser2 = user2;
                }
            }
            if (providerUser2 != null) {
                this.detailSettingsCell[1] = new TextDetailSettingsCell(context);
                this.detailSettingsCell[1].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                TextDetailSettingsCell textDetailSettingsCell2 = this.detailSettingsCell[1];
                String formatName = ContactsController.formatName(providerUser2.first_name, providerUser2.last_name);
                providerName = formatName;
                String string = LocaleController.getString("PaymentCheckoutProvider", R.string.PaymentCheckoutProvider);
                TLRPC.TL_payments_validateRequestedInfo tL_payments_validateRequestedInfo = this.validateRequest;
                textDetailSettingsCell2.setTextAndValueAndIcon(formatName, string, R.drawable.msg_payment_provider, ((tL_payments_validateRequestedInfo == null || (tL_payments_validateRequestedInfo.info.shipping_address == null && this.shippingOption == null)) && (this.paymentForm.saved_info == null || this.paymentForm.saved_info.shipping_address == null)) ? false : true);
                this.detailSettingsCell[1].setVisibility(cardInfoVisibility);
                this.linearLayout2.addView(this.detailSettingsCell[1]);
            } else {
                providerName = "";
            }
            if (this.validateRequest != null || (this.isCheckoutPreview && (tL_payments_paymentForm2 = this.paymentForm) != null && tL_payments_paymentForm2.saved_info != null)) {
                TLRPC.TL_payments_validateRequestedInfo tL_payments_validateRequestedInfo2 = this.validateRequest;
                TLRPC.TL_paymentRequestedInfo info = tL_payments_validateRequestedInfo2 != null ? tL_payments_validateRequestedInfo2.info : this.paymentForm.saved_info;
                this.detailSettingsCell[c] = new TextDetailSettingsCell(context);
                this.detailSettingsCell[c].setVisibility(8);
                this.linearLayout2.addView(this.detailSettingsCell[c]);
                if (info.shipping_address != null) {
                    this.detailSettingsCell[c].setVisibility(0);
                    if (this.currentStep != 4) {
                        this.detailSettingsCell[c].setBackgroundColor(getThemedColor(str2));
                    } else {
                        this.detailSettingsCell[c].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        this.detailSettingsCell[c].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda0
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PaymentFormActivity.this.m4111lambda$createView$18$orgtelegramuiPaymentFormActivity(view);
                            }
                        });
                    }
                }
                this.detailSettingsCell[c2] = new TextDetailSettingsCell(context);
                this.detailSettingsCell[c2].setVisibility(8);
                this.linearLayout2.addView(this.detailSettingsCell[c2]);
                if (info.name != null) {
                    this.detailSettingsCell[c2].setVisibility(0);
                    if (this.currentStep != 4) {
                        this.detailSettingsCell[c2].setBackgroundColor(getThemedColor(str2));
                    } else {
                        this.detailSettingsCell[c2].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        this.detailSettingsCell[c2].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda1
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PaymentFormActivity.this.m4112lambda$createView$19$orgtelegramuiPaymentFormActivity(view);
                            }
                        });
                    }
                }
                this.detailSettingsCell[4] = new TextDetailSettingsCell(context);
                this.detailSettingsCell[4].setVisibility(8);
                this.linearLayout2.addView(this.detailSettingsCell[4]);
                if (info.phone != null) {
                    this.detailSettingsCell[4].setVisibility(0);
                    if (this.currentStep != 4) {
                        this.detailSettingsCell[4].setBackgroundColor(getThemedColor(str2));
                    } else {
                        this.detailSettingsCell[4].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        this.detailSettingsCell[4].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda2
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PaymentFormActivity.this.m4114lambda$createView$20$orgtelegramuiPaymentFormActivity(view);
                            }
                        });
                    }
                }
                this.detailSettingsCell[5] = new TextDetailSettingsCell(context);
                this.detailSettingsCell[5].setVisibility(8);
                this.linearLayout2.addView(this.detailSettingsCell[5]);
                if (info.email != null) {
                    this.detailSettingsCell[5].setVisibility(0);
                    if (this.currentStep != 4) {
                        this.detailSettingsCell[5].setBackgroundColor(getThemedColor(str2));
                    } else {
                        this.detailSettingsCell[5].setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        this.detailSettingsCell[5].setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda3
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                PaymentFormActivity.this.m4115lambda$createView$21$orgtelegramuiPaymentFormActivity(view);
                            }
                        });
                    }
                }
                if (this.shippingOption != null) {
                    this.detailSettingsCell[6] = new TextDetailSettingsCell(context);
                    this.detailSettingsCell[6].setBackgroundColor(getThemedColor(str2));
                    this.detailSettingsCell[6].setTextAndValueAndIcon(this.shippingOption.title, LocaleController.getString("PaymentCheckoutShippingMethod", R.string.PaymentCheckoutShippingMethod), R.drawable.msg_payment_delivery, false);
                    this.linearLayout2.addView(this.detailSettingsCell[6]);
                }
                setAddressFields(info);
            }
            if (this.currentStep == 4) {
                boolean z2 = !this.isCheckoutPreview;
                this.isAcceptTermsChecked = z2;
                this.recurrentAccepted = z2;
                this.bottomLayout = new BottomFrameLayout(context, this.paymentForm);
                if (Build.VERSION.SDK_INT < 21) {
                    i = -1;
                } else {
                    View selectorView = new View(context);
                    selectorView.setBackground(Theme.getSelectorDrawable(getThemedColor(Theme.key_listSelector), false));
                    i = -1;
                    this.bottomLayout.addView(selectorView, LayoutHelper.createFrame(-1, -1.0f));
                }
                frameLayout2.addView(this.bottomLayout, LayoutHelper.createFrame(i, 48, 80));
                this.bottomLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda13
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PaymentFormActivity.this.m4118lambda$createView$24$orgtelegramuiPaymentFormActivity(providerName, view);
                    }
                });
                TextView textView2 = new TextView(context);
                this.payTextView = textView2;
                textView2.setTextColor(getThemedColor(Theme.key_contacts_inviteText));
                this.payTextView.setText(LocaleController.formatString("PaymentCheckoutPay", R.string.PaymentCheckoutPay, this.totalPrice[0]));
                this.payTextView.setTextSize(1, 14.0f);
                this.payTextView.setGravity(17);
                this.payTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                this.bottomLayout.addView(this.payTextView, LayoutHelper.createFrame(-1, -1.0f));
                ContextProgressView contextProgressView2 = new ContextProgressView(context, 0);
                this.progressViewButton = contextProgressView2;
                contextProgressView2.setVisibility(4);
                int color2 = getThemedColor(Theme.key_contacts_inviteText);
                this.progressViewButton.setColors(805306367 & color2, color2);
                this.bottomLayout.addView(this.progressViewButton, LayoutHelper.createFrame(-1, -1.0f));
                this.bottomLayout.setChecked(!this.paymentForm.invoice.recurring || this.isAcceptTermsChecked);
                this.payTextView.setAlpha((!this.paymentForm.invoice.recurring || this.isAcceptTermsChecked) ? 1.0f : 0.8f);
                this.doneItem.setEnabled(false);
                this.doneItem.getContentView().setVisibility(4);
                WebView webView2 = new WebView(context) { // from class: org.telegram.ui.PaymentFormActivity.18
                    @Override // android.webkit.WebView, android.view.View
                    public boolean onTouchEvent(MotionEvent event) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        return super.onTouchEvent(event);
                    }
                };
                this.webView = webView2;
                webView2.setBackgroundColor(-1);
                this.webView.getSettings().setJavaScriptEnabled(true);
                this.webView.getSettings().setDomStorageEnabled(true);
                if (Build.VERSION.SDK_INT >= 21) {
                    this.webView.getSettings().setMixedContentMode(0);
                    CookieManager cookieManager2 = CookieManager.getInstance();
                    cookieManager2.setAcceptThirdPartyCookies(this.webView, true);
                }
                this.webView.setWebViewClient(new WebViewClient() { // from class: org.telegram.ui.PaymentFormActivity.19
                    @Override // android.webkit.WebViewClient
                    public void onLoadResource(WebView view, String url) {
                        try {
                            Uri uri = Uri.parse(url);
                            if ("t.me".equals(uri.getHost())) {
                                PaymentFormActivity.this.goToNextStep();
                                return;
                            }
                        } catch (Exception e13) {
                        }
                        super.onLoadResource(view, url);
                    }

                    @Override // android.webkit.WebViewClient
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        PaymentFormActivity.this.webviewLoading = false;
                        PaymentFormActivity.this.showEditDoneProgress(true, false);
                        PaymentFormActivity.this.updateSavePaymentField();
                    }

                    @Override // android.webkit.WebViewClient
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        try {
                            Uri uri = Uri.parse(url);
                            if ("t.me".equals(uri.getHost())) {
                                PaymentFormActivity.this.goToNextStep();
                                return true;
                            }
                            return false;
                        } catch (Exception e13) {
                            return false;
                        }
                    }
                });
                if (this.paymentForm.invoice.recurring) {
                    RecurrentPaymentsAcceptCell recurrentPaymentsAcceptCell = new RecurrentPaymentsAcceptCell(context, getResourceProvider());
                    this.recurrentAcceptCell = recurrentPaymentsAcceptCell;
                    recurrentPaymentsAcceptCell.setChecked(this.paymentForm.invoice.recurring && this.isAcceptTermsChecked);
                    String str6 = LocaleController.getString((int) R.string.PaymentCheckoutAcceptRecurrent);
                    SpannableStringBuilder sb = new SpannableStringBuilder(str6);
                    int firstIndex = str6.indexOf(42);
                    int lastIndex = str6.lastIndexOf(42);
                    if (firstIndex != -1 && lastIndex != -1) {
                        SpannableString acceptTerms = new SpannableString(str6.substring(firstIndex + 1, lastIndex));
                        acceptTerms.setSpan(new URLSpanNoUnderline(this.paymentForm.invoice.recurring_terms_url), 0, acceptTerms.length(), 33);
                        sb.replace(firstIndex, lastIndex + 1, (CharSequence) acceptTerms);
                        str6 = str6.substring(0, firstIndex) + ((Object) acceptTerms) + str6.substring(lastIndex + 1);
                    }
                    int botIndex = str6.indexOf("%1$s");
                    if (botIndex != -1) {
                        sb.replace(botIndex, "%1$s".length() + botIndex, (CharSequence) this.currentBotName);
                        sb.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM)), botIndex, this.currentBotName.length() + botIndex, 33);
                    }
                    this.recurrentAcceptCell.setText(sb);
                    this.recurrentAcceptCell.setBackground(Theme.createSelectorWithBackgroundDrawable(getThemedColor(str2), getThemedColor(Theme.key_listSelector)));
                    this.recurrentAcceptCell.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            PaymentFormActivity.this.m4119lambda$createView$25$orgtelegramuiPaymentFormActivity(view);
                        }
                    });
                    frameLayout2.addView(this.recurrentAcceptCell, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 48.0f));
                }
                frameLayout2.addView(this.webView, LayoutHelper.createFrame(-1, -1.0f));
                this.webView.setVisibility(8);
            }
            this.sectionCell[1] = new ShadowSectionCell(context, this.resourcesProvider);
            this.sectionCell[1].setBackgroundDrawable(Theme.getThemedDrawable(context, (int) R.drawable.greydivider_bottom, str));
            if (cardInfoVisibility != 0 && this.currentStep == 4 && this.validateRequest == null && ((tL_payments_paymentForm = this.paymentForm) == null || tL_payments_paymentForm.saved_info == null)) {
                this.sectionCell[1].setVisibility(cardInfoVisibility);
            }
            this.linearLayout2.addView(this.sectionCell[1], LayoutHelper.createLinear(-1, -2));
        }
        return this.fragmentView;
    }

    /* renamed from: lambda$createView$1$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ boolean m4104lambda$createView$1$orgtelegramuiPaymentFormActivity(View v, MotionEvent event) {
        if (getParentActivity() == null) {
            return false;
        }
        if (event.getAction() == 1) {
            CountrySelectActivity fragment = new CountrySelectActivity(false);
            fragment.setCountrySelectActivityDelegate(new CountrySelectActivity.CountrySelectActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda58
                @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
                public final void didSelectCountry(CountrySelectActivity.Country country) {
                    PaymentFormActivity.this.m4103lambda$createView$0$orgtelegramuiPaymentFormActivity(country);
                }
            });
            presentFragment(fragment);
        }
        return true;
    }

    /* renamed from: lambda$createView$0$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4103lambda$createView$0$orgtelegramuiPaymentFormActivity(CountrySelectActivity.Country country) {
        this.inputFields[4].setText(country.name);
        this.countryName = country.shortname;
    }

    /* renamed from: lambda$createView$2$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ boolean m4113lambda$createView$2$orgtelegramuiPaymentFormActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5) {
            int num = ((Integer) textView.getTag()).intValue();
            while (true) {
                int i2 = num + 1;
                EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                if (i2 < editTextBoldCursorArr.length) {
                    num++;
                    if (num != 4 && ((View) editTextBoldCursorArr[num].getParent()).getVisibility() == 0) {
                        this.inputFields[num].requestFocus();
                        break;
                    }
                } else {
                    break;
                }
            }
            return true;
        } else if (i == 6) {
            this.doneItem.performClick();
            return true;
        } else {
            return false;
        }
    }

    /* renamed from: lambda$createView$3$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4123lambda$createView$3$orgtelegramuiPaymentFormActivity(View v) {
        boolean z = !this.saveShippingInfo;
        this.saveShippingInfo = z;
        this.checkCell1.setChecked(z);
    }

    /* renamed from: lambda$createView$4$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4126lambda$createView$4$orgtelegramuiPaymentFormActivity(View v) {
        boolean z = !this.saveCardInfo;
        this.saveCardInfo = z;
        this.checkCell1.setChecked(z);
    }

    /* renamed from: lambda$createView$6$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ boolean m4128lambda$createView$6$orgtelegramuiPaymentFormActivity(View v, MotionEvent event) {
        if (getParentActivity() == null) {
            return false;
        }
        if (event.getAction() == 1) {
            CountrySelectActivity fragment = new CountrySelectActivity(false);
            fragment.setCountrySelectActivityDelegate(new CountrySelectActivity.CountrySelectActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda59
                @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
                public final void didSelectCountry(CountrySelectActivity.Country country) {
                    PaymentFormActivity.this.m4127lambda$createView$5$orgtelegramuiPaymentFormActivity(country);
                }
            });
            presentFragment(fragment);
        }
        return true;
    }

    /* renamed from: lambda$createView$5$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4127lambda$createView$5$orgtelegramuiPaymentFormActivity(CountrySelectActivity.Country country) {
        this.inputFields[4].setText(country.name);
    }

    /* renamed from: lambda$createView$7$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ boolean m4129lambda$createView$7$orgtelegramuiPaymentFormActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5) {
            int num = ((Integer) textView.getTag()).intValue();
            while (true) {
                int i2 = num + 1;
                EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                if (i2 < editTextBoldCursorArr.length) {
                    num++;
                    if (num == 4) {
                        num++;
                    }
                    if (((View) editTextBoldCursorArr[num].getParent()).getVisibility() == 0) {
                        this.inputFields[num].requestFocus();
                        break;
                    }
                } else {
                    break;
                }
            }
            return true;
        } else if (i == 6) {
            this.doneItem.performClick();
            return true;
        } else {
            return false;
        }
    }

    /* renamed from: lambda$createView$8$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4130lambda$createView$8$orgtelegramuiPaymentFormActivity(View v) {
        boolean z = !this.saveCardInfo;
        this.saveCardInfo = z;
        this.checkCell1.setChecked(z);
    }

    /* renamed from: lambda$createView$9$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4131lambda$createView$9$orgtelegramuiPaymentFormActivity(View v) {
        int num = ((Integer) v.getTag()).intValue();
        int a1 = 0;
        while (true) {
            RadioCell[] radioCellArr = this.radioCells;
            if (a1 < radioCellArr.length) {
                radioCellArr[a1].setChecked(num == a1, true);
                a1++;
            } else {
                return;
            }
        }
    }

    public static /* synthetic */ boolean lambda$createView$10(View v, MotionEvent event) {
        return true;
    }

    /* renamed from: lambda$createView$11$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ boolean m4105lambda$createView$11$orgtelegramuiPaymentFormActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            this.doneItem.performClick();
            return true;
        }
        return false;
    }

    /* renamed from: lambda$createView$12$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4106lambda$createView$12$orgtelegramuiPaymentFormActivity(View v) {
        this.passwordOk = false;
        goToNextStep();
    }

    /* renamed from: lambda$createView$13$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4107lambda$createView$13$orgtelegramuiPaymentFormActivity(View v) {
        this.inputFields[0].requestFocus();
        AndroidUtilities.showKeyboard(this.inputFields[0]);
    }

    public static /* synthetic */ boolean lambda$createView$14(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            AndroidUtilities.hideKeyboard(textView);
            return true;
        }
        return false;
    }

    /* renamed from: lambda$createView$15$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4108lambda$createView$15$orgtelegramuiPaymentFormActivity(TextView valueTextView, long amount, View v) {
        long amoumt = ((Long) valueTextView.getTag()).longValue();
        Long l = this.tipAmount;
        if (l != null && amoumt == l.longValue()) {
            this.ignoreOnTextChange = true;
            this.inputFields[0].setText("");
            this.ignoreOnTextChange = false;
            this.tipAmount = 0L;
            updateTotalPrice();
        } else {
            this.inputFields[0].setText(LocaleController.getInstance().formatCurrencyString(amount, false, true, true, this.paymentForm.invoice.currency));
        }
        EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
        editTextBoldCursorArr[0].setSelection(editTextBoldCursorArr[0].length());
    }

    /* renamed from: lambda$createView$17$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4110lambda$createView$17$orgtelegramuiPaymentFormActivity(View v) {
        if (getParentActivity() == null) {
            return;
        }
        BottomSheet.Builder builder = new BottomSheet.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("PaymentCheckoutMethod", R.string.PaymentCheckoutMethod), true);
        builder.setItems(new CharSequence[]{this.cardName, LocaleController.getString("PaymentCheckoutMethodNewCard", R.string.PaymentCheckoutMethodNewCard)}, new int[]{R.drawable.msg_payment_card, R.drawable.msg_addbot}, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda10
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PaymentFormActivity.this.m4109lambda$createView$16$orgtelegramuiPaymentFormActivity(dialogInterface, i);
            }
        });
        showDialog(builder.create());
    }

    /* renamed from: lambda$createView$16$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4109lambda$createView$16$orgtelegramuiPaymentFormActivity(DialogInterface dialog, int which) {
        if (which == 1) {
            PaymentFormActivity activity = new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, 2, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
            activity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.13
                @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                public /* synthetic */ void currentPasswordUpdated(TLRPC.TL_account_password tL_account_password) {
                    PaymentFormActivityDelegate.CC.$default$currentPasswordUpdated(this, tL_account_password);
                }

                @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                public /* synthetic */ void didSelectNewAddress(TLRPC.TL_payments_validateRequestedInfo tL_payments_validateRequestedInfo) {
                    PaymentFormActivityDelegate.CC.$default$didSelectNewAddress(this, tL_payments_validateRequestedInfo);
                }

                @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                public /* synthetic */ void onFragmentDestroyed() {
                    PaymentFormActivityDelegate.CC.$default$onFragmentDestroyed(this);
                }

                @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                public boolean didSelectNewCard(String tokenJson, String card, boolean saveCard, TLRPC.TL_inputPaymentCredentialsGooglePay googlePay) {
                    PaymentFormActivity.this.paymentForm.saved_credentials = null;
                    PaymentFormActivity.this.paymentJson = tokenJson;
                    PaymentFormActivity.this.saveCardInfo = saveCard;
                    PaymentFormActivity.this.cardName = card;
                    PaymentFormActivity.this.googlePayCredentials = googlePay;
                    PaymentFormActivity.this.detailSettingsCell[0].setTextAndValue(PaymentFormActivity.this.cardName, LocaleController.getString("PaymentCheckoutMethod", R.string.PaymentCheckoutMethod), true);
                    return false;
                }
            });
            presentFragment(activity);
        }
    }

    /* renamed from: lambda$createView$18$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4111lambda$createView$18$orgtelegramuiPaymentFormActivity(View v) {
        PaymentFormActivity activity = new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        activity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.14
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC.TL_account_password tL_account_password) {
                PaymentFormActivityDelegate.CC.$default$currentPasswordUpdated(this, tL_account_password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC.TL_inputPaymentCredentialsGooglePay tL_inputPaymentCredentialsGooglePay) {
                return PaymentFormActivityDelegate.CC.$default$didSelectNewCard(this, str, str2, z, tL_inputPaymentCredentialsGooglePay);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.CC.$default$onFragmentDestroyed(this);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC.TL_payments_validateRequestedInfo validateRequested) {
                PaymentFormActivity.this.validateRequest = validateRequested;
                PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                paymentFormActivity.setAddressFields(paymentFormActivity.validateRequest.info);
            }
        });
        presentFragment(activity);
    }

    /* renamed from: lambda$createView$19$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4112lambda$createView$19$orgtelegramuiPaymentFormActivity(View v) {
        PaymentFormActivity activity = new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        activity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.15
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC.TL_account_password tL_account_password) {
                PaymentFormActivityDelegate.CC.$default$currentPasswordUpdated(this, tL_account_password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC.TL_inputPaymentCredentialsGooglePay tL_inputPaymentCredentialsGooglePay) {
                return PaymentFormActivityDelegate.CC.$default$didSelectNewCard(this, str, str2, z, tL_inputPaymentCredentialsGooglePay);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.CC.$default$onFragmentDestroyed(this);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC.TL_payments_validateRequestedInfo validateRequested) {
                PaymentFormActivity.this.validateRequest = validateRequested;
                PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                paymentFormActivity.setAddressFields(paymentFormActivity.validateRequest.info);
            }
        });
        presentFragment(activity);
    }

    /* renamed from: lambda$createView$20$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4114lambda$createView$20$orgtelegramuiPaymentFormActivity(View v) {
        PaymentFormActivity activity = new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        activity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.16
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC.TL_account_password tL_account_password) {
                PaymentFormActivityDelegate.CC.$default$currentPasswordUpdated(this, tL_account_password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC.TL_inputPaymentCredentialsGooglePay tL_inputPaymentCredentialsGooglePay) {
                return PaymentFormActivityDelegate.CC.$default$didSelectNewCard(this, str, str2, z, tL_inputPaymentCredentialsGooglePay);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.CC.$default$onFragmentDestroyed(this);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC.TL_payments_validateRequestedInfo validateRequested) {
                PaymentFormActivity.this.validateRequest = validateRequested;
                PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                paymentFormActivity.setAddressFields(paymentFormActivity.validateRequest.info);
            }
        });
        presentFragment(activity);
    }

    /* renamed from: lambda$createView$21$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4115lambda$createView$21$orgtelegramuiPaymentFormActivity(View v) {
        PaymentFormActivity activity = new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, 0, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment);
        activity.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.17
            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void currentPasswordUpdated(TLRPC.TL_account_password tL_account_password) {
                PaymentFormActivityDelegate.CC.$default$currentPasswordUpdated(this, tL_account_password);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ boolean didSelectNewCard(String str, String str2, boolean z, TLRPC.TL_inputPaymentCredentialsGooglePay tL_inputPaymentCredentialsGooglePay) {
                return PaymentFormActivityDelegate.CC.$default$didSelectNewCard(this, str, str2, z, tL_inputPaymentCredentialsGooglePay);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public /* synthetic */ void onFragmentDestroyed() {
                PaymentFormActivityDelegate.CC.$default$onFragmentDestroyed(this);
            }

            @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
            public void didSelectNewAddress(TLRPC.TL_payments_validateRequestedInfo validateRequested) {
                PaymentFormActivity.this.validateRequest = validateRequested;
                PaymentFormActivity paymentFormActivity = PaymentFormActivity.this;
                paymentFormActivity.setAddressFields(paymentFormActivity.validateRequest.info);
            }
        });
        presentFragment(activity);
    }

    /* renamed from: lambda$createView$24$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4118lambda$createView$24$orgtelegramuiPaymentFormActivity(String providerName, final View v) {
        int step;
        if (this.paymentForm.invoice.recurring && !this.recurrentAccepted) {
            AndroidUtilities.shakeViewSpring(this.recurrentAcceptCell.getTextView(), 4.5f);
            try {
                this.recurrentAcceptCell.performHapticFeedback(3, 2);
            } catch (Exception e) {
            }
        } else if (this.isCheckoutPreview && this.paymentForm.saved_info != null && this.validateRequest == null) {
            setDonePressed(true);
            sendSavedForm(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda28
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.m4116lambda$createView$22$orgtelegramuiPaymentFormActivity(v);
                }
            });
        } else if (this.isCheckoutPreview && ((this.paymentForm.saved_info == null && (this.paymentForm.invoice.shipping_address_requested || this.paymentForm.invoice.email_requested || this.paymentForm.invoice.name_requested || this.paymentForm.invoice.phone_requested)) || this.paymentForm.saved_credentials == null || (this.shippingOption == null && this.paymentForm.invoice.flexible))) {
            if (this.paymentForm.saved_info == null && (this.paymentForm.invoice.shipping_address_requested || this.paymentForm.invoice.email_requested || this.paymentForm.invoice.name_requested || this.paymentForm.invoice.phone_requested)) {
                step = 0;
            } else if (this.paymentForm.saved_credentials == null) {
                step = 2;
            } else {
                step = 1;
            }
            this.paymentStatusSent = true;
            presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, step, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment));
        } else {
            if (!this.paymentForm.password_missing && this.paymentForm.saved_credentials != null) {
                if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
                    UserConfig.getInstance(this.currentAccount).tmpPassword = null;
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                }
                if (UserConfig.getInstance(this.currentAccount).tmpPassword == null) {
                    this.needPayAfterTransition = true;
                    presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, 3, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, null, this.parentFragment));
                    this.needPayAfterTransition = false;
                    return;
                } else if (this.isCheckoutPreview) {
                    this.isCheckoutPreview = false;
                    NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.paymentFinished);
                }
            }
            TLRPC.User user = this.botUser;
            if (user == null || user.verified) {
                showPayAlert(this.totalPrice[0]);
                return;
            }
            String botKey = "payment_warning_" + this.botUser.id;
            SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
            if (preferences.getBoolean(botKey, false)) {
                showPayAlert(this.totalPrice[0]);
                return;
            }
            preferences.edit().putBoolean(botKey, true).commit();
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("PaymentWarning", R.string.PaymentWarning));
            builder.setMessage(LocaleController.formatString("PaymentWarningText", R.string.PaymentWarningText, this.currentBotName, providerName));
            builder.setPositiveButton(LocaleController.getString("Continue", R.string.Continue), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda21
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    PaymentFormActivity.this.m4117lambda$createView$23$orgtelegramuiPaymentFormActivity(dialogInterface, i);
                }
            });
            showDialog(builder.create());
        }
    }

    /* renamed from: lambda$createView$22$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4116lambda$createView$22$orgtelegramuiPaymentFormActivity(View v) {
        setDonePressed(false);
        v.callOnClick();
    }

    /* renamed from: lambda$createView$23$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4117lambda$createView$23$orgtelegramuiPaymentFormActivity(DialogInterface dialogInterface, int i) {
        showPayAlert(this.totalPrice[0]);
    }

    /* renamed from: lambda$createView$25$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4119lambda$createView$25$orgtelegramuiPaymentFormActivity(View v) {
        if (this.donePressed) {
            return;
        }
        boolean z = !this.recurrentAccepted;
        this.recurrentAccepted = z;
        this.recurrentAcceptCell.setChecked(z);
        this.bottomLayout.setChecked(this.recurrentAccepted);
    }

    /* renamed from: lambda$createView$26$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ boolean m4120lambda$createView$26$orgtelegramuiPaymentFormActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 6) {
            return false;
        }
        sendSavePassword(false);
        return true;
    }

    /* renamed from: lambda$createView$28$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4121lambda$createView$28$orgtelegramuiPaymentFormActivity(View v) {
        TLRPC.TL_account_resendPasswordEmail req = new TLRPC.TL_account_resendPasswordEmail();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, PaymentFormActivity$$ExternalSyntheticLambda56.INSTANCE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(LocaleController.getString("ResendCodeInfo", R.string.ResendCodeInfo));
        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        showDialog(builder.create());
    }

    public static /* synthetic */ void lambda$createView$27(TLObject response, TLRPC.TL_error error) {
    }

    /* renamed from: lambda$createView$30$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4124lambda$createView$30$orgtelegramuiPaymentFormActivity(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        String text = LocaleController.getString("TurnPasswordOffQuestion", R.string.TurnPasswordOffQuestion);
        if (this.currentPassword.has_secure_values) {
            text = text + "\n\n" + LocaleController.getString("TurnPasswordOffPassport", R.string.TurnPasswordOffPassport);
        }
        builder.setMessage(text);
        builder.setTitle(LocaleController.getString("TurnPasswordOffQuestionTitle", R.string.TurnPasswordOffQuestionTitle));
        builder.setPositiveButton(LocaleController.getString("Disable", R.string.Disable), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda32
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PaymentFormActivity.this.m4122lambda$createView$29$orgtelegramuiPaymentFormActivity(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        AlertDialog alertDialog = builder.create();
        showDialog(alertDialog);
        TextView button = (TextView) alertDialog.getButton(-1);
        if (button != null) {
            button.setTextColor(getThemedColor(Theme.key_dialogTextRed2));
        }
    }

    /* renamed from: lambda$createView$29$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4122lambda$createView$29$orgtelegramuiPaymentFormActivity(DialogInterface dialogInterface, int i) {
        sendSavePassword(true);
    }

    /* renamed from: lambda$createView$31$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ boolean m4125lambda$createView$31$orgtelegramuiPaymentFormActivity(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 6) {
            this.doneItem.performClick();
            return true;
        } else if (i == 5) {
            int num = ((Integer) textView.getTag()).intValue();
            if (num == 0) {
                this.inputFields[1].requestFocus();
                return false;
            } else if (num == 1) {
                this.inputFields[2].requestFocus();
                return false;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void setAddressFields(TLRPC.TL_paymentRequestedInfo info) {
        boolean z = true;
        int i = 0;
        if (info.shipping_address != null) {
            String address = String.format("%s %s, %s, %s, %s, %s", info.shipping_address.street_line1, info.shipping_address.street_line2, info.shipping_address.city, info.shipping_address.state, info.shipping_address.country_iso2, info.shipping_address.post_code);
            this.detailSettingsCell[2].setTextAndValueAndIcon(address, LocaleController.getString("PaymentShippingAddress", R.string.PaymentShippingAddress), R.drawable.msg_payment_address, true);
        }
        this.detailSettingsCell[2].setVisibility(info.shipping_address != null ? 0 : 8);
        if (info.name != null) {
            this.detailSettingsCell[3].setTextAndValueAndIcon(info.name, LocaleController.getString("PaymentCheckoutName", R.string.PaymentCheckoutName), R.drawable.msg_contacts, true);
        }
        this.detailSettingsCell[3].setVisibility(info.name != null ? 0 : 8);
        if (info.phone != null) {
            this.detailSettingsCell[4].setTextAndValueAndIcon(PhoneFormat.getInstance().format(info.phone), LocaleController.getString("PaymentCheckoutPhoneNumber", R.string.PaymentCheckoutPhoneNumber), R.drawable.msg_calls, (info.email == null && this.shippingOption == null) ? false : true);
        }
        this.detailSettingsCell[4].setVisibility(info.phone != null ? 0 : 8);
        if (info.email != null) {
            TextDetailSettingsCell textDetailSettingsCell = this.detailSettingsCell[5];
            String str = info.email;
            String string = LocaleController.getString("PaymentCheckoutEmail", R.string.PaymentCheckoutEmail);
            if (this.shippingOption == null) {
                z = false;
            }
            textDetailSettingsCell.setTextAndValueAndIcon(str, string, R.drawable.msg_mention, z);
        }
        TextDetailSettingsCell textDetailSettingsCell2 = this.detailSettingsCell[5];
        if (info.email == null) {
            i = 8;
        }
        textDetailSettingsCell2.setVisibility(i);
    }

    public void updateTotalPrice() {
        this.totalPrice[0] = getTotalPriceString(this.prices);
        this.totalCell.setTextAndValue(LocaleController.getString("PaymentTransactionTotal", R.string.PaymentTransactionTotal), this.totalPrice[0], true);
        TextView textView = this.payTextView;
        if (textView != null) {
            textView.setText(LocaleController.formatString("PaymentCheckoutPay", R.string.PaymentCheckoutPay, this.totalPrice[0]));
        }
        if (this.tipLayout != null) {
            int color = getThemedColor(Theme.key_contacts_inviteBackground);
            int N2 = this.tipLayout.getChildCount();
            for (int b = 0; b < N2; b++) {
                TextView child = (TextView) this.tipLayout.getChildAt(b);
                if (child.getTag().equals(this.tipAmount)) {
                    Theme.setDrawableColor(child.getBackground(), color);
                    child.setTextColor(getThemedColor(Theme.key_contacts_inviteText));
                } else {
                    Theme.setDrawableColor(child.getBackground(), 536870911 & color);
                    child.setTextColor(getThemedColor(Theme.key_chats_secretName));
                }
                child.invalidate();
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
        this.googlePayButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda60
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PaymentFormActivity.this.m4102x557681fa(view);
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

    /* renamed from: lambda$createGooglePayButton$32$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4102x557681fa(View v) {
        this.googlePayButton.setClickable(false);
        try {
            JSONObject paymentDataRequest = getBaseRequest();
            JSONObject cardPaymentMethod = getBaseCardPaymentMethod();
            if (this.googlePayPublicKey != null && this.googlePayParameters == null) {
                cardPaymentMethod.put("tokenizationSpecification", new JSONObject() { // from class: org.telegram.ui.PaymentFormActivity.22
                    {
                        PaymentFormActivity.this = this;
                        put(CommonProperties.TYPE, "DIRECT");
                        put("parameters", new JSONObject() { // from class: org.telegram.ui.PaymentFormActivity.22.1
                            {
                                AnonymousClass22.this = this;
                                put("protocolVersion", "ECv2");
                                put("publicKey", PaymentFormActivity.this.googlePayPublicKey);
                            }
                        });
                    }
                });
            } else {
                cardPaymentMethod.put("tokenizationSpecification", new JSONObject() { // from class: org.telegram.ui.PaymentFormActivity.23
                    {
                        PaymentFormActivity.this = this;
                        put(CommonProperties.TYPE, "PAYMENT_GATEWAY");
                        if (this.googlePayParameters != null) {
                            put("parameters", this.googlePayParameters);
                        } else {
                            put("parameters", new JSONObject() { // from class: org.telegram.ui.PaymentFormActivity.23.1
                                {
                                    AnonymousClass23.this = this;
                                    put("gateway", "stripe");
                                    put("stripe:publishableKey", PaymentFormActivity.this.providerApiKey);
                                    put("stripe:version", StripeApiHandler.VERSION);
                                }
                            });
                        }
                    }
                });
            }
            paymentDataRequest.put("allowedPaymentMethods", new JSONArray().put(cardPaymentMethod));
            JSONObject transactionInfo = new JSONObject();
            ArrayList<TLRPC.TL_labeledPrice> arrayList = new ArrayList<>(this.paymentForm.invoice.prices);
            TLRPC.TL_shippingOption tL_shippingOption = this.shippingOption;
            if (tL_shippingOption != null) {
                arrayList.addAll(tL_shippingOption.prices);
            }
            String totalPriceDecimalString = getTotalPriceDecimalString(arrayList);
            this.totalPriceDecimal = totalPriceDecimalString;
            transactionInfo.put("totalPrice", totalPriceDecimalString);
            transactionInfo.put("totalPriceStatus", "FINAL");
            if (!TextUtils.isEmpty(this.googlePayCountryCode)) {
                transactionInfo.put(RemoteConfigConstants.RequestFieldKey.COUNTRY_CODE, this.googlePayCountryCode);
            }
            transactionInfo.put("currencyCode", this.paymentForm.invoice.currency);
            transactionInfo.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");
            paymentDataRequest.put("transactionInfo", transactionInfo);
            paymentDataRequest.put("merchantInfo", new JSONObject().put("merchantName", this.currentBotName));
            PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequest.toString());
            if (request != null) {
                AutoResolveHelper.resolveTask(this.paymentsClient.loadPaymentData(request), getParentActivity(), LOAD_PAYMENT_DATA_REQUEST_CODE);
            }
        } catch (JSONException e) {
            FileLog.e(e);
        }
    }

    private void updatePasswordFields() {
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
            for (int a = 0; a < 3; a++) {
                ((View) this.inputFields[a].getParent()).setVisibility(8);
            }
            for (int a2 = 0; a2 < this.dividers.size(); a2++) {
                this.dividers.get(a2).setVisibility(8);
            }
            return;
        }
        showEditDoneProgress(true, false);
        if (this.waitingForEmail) {
            TextInfoPrivacyCell textInfoPrivacyCell = this.bottomCell[2];
            Object[] objArr = new Object[1];
            objArr[0] = this.currentPassword.email_unconfirmed_pattern != null ? this.currentPassword.email_unconfirmed_pattern : "";
            textInfoPrivacyCell.setText(LocaleController.formatString("EmailPasswordConfirmText2", R.string.EmailPasswordConfirmText2, objArr));
            this.bottomCell[2].setVisibility(0);
            this.settingsCell[0].setVisibility(0);
            this.settingsCell[1].setVisibility(0);
            this.codeFieldCell.setVisibility(0);
            this.bottomCell[1].setText("");
            this.headerCell[0].setVisibility(8);
            this.headerCell[1].setVisibility(8);
            this.bottomCell[0].setVisibility(8);
            for (int a3 = 0; a3 < 3; a3++) {
                ((View) this.inputFields[a3].getParent()).setVisibility(8);
            }
            for (int a4 = 0; a4 < this.dividers.size(); a4++) {
                this.dividers.get(a4).setVisibility(8);
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
        for (int a5 = 0; a5 < 3; a5++) {
            ((View) this.inputFields[a5].getParent()).setVisibility(0);
        }
        for (int a6 = 0; a6 < this.dividers.size(); a6++) {
            this.dividers.get(a6).setVisibility(0);
        }
    }

    private void loadPasswordInfo() {
        if (this.loadingPasswordInfo) {
            return;
        }
        this.loadingPasswordInfo = true;
        TLRPC.TL_account_getPassword req = new TLRPC.TL_account_getPassword();
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda46
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                PaymentFormActivity.this.m4135lambda$loadPasswordInfo$35$orgtelegramuiPaymentFormActivity(tLObject, tL_error);
            }
        }, 10);
    }

    /* renamed from: lambda$loadPasswordInfo$35$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4135lambda$loadPasswordInfo$35$orgtelegramuiPaymentFormActivity(final TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.m4134lambda$loadPasswordInfo$34$orgtelegramuiPaymentFormActivity(error, response);
            }
        });
    }

    /* renamed from: lambda$loadPasswordInfo$34$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4134lambda$loadPasswordInfo$34$orgtelegramuiPaymentFormActivity(TLRPC.TL_error error, TLObject response) {
        this.loadingPasswordInfo = false;
        if (error == null) {
            TLRPC.TL_account_password tL_account_password = (TLRPC.TL_account_password) response;
            this.currentPassword = tL_account_password;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tL_account_password, false)) {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
                return;
            }
            if (this.paymentForm != null && this.currentPassword.has_password) {
                this.paymentForm.password_missing = false;
                this.paymentForm.can_save_credentials = true;
                updateSavePaymentField();
            }
            TwoStepVerificationActivity.initPasswordNewAlgo(this.currentPassword);
            PaymentFormActivity paymentFormActivity = this.passwordFragment;
            if (paymentFormActivity != null) {
                paymentFormActivity.setCurrentPassword(this.currentPassword);
            }
            if (!this.currentPassword.has_password && this.shortPollRunnable == null) {
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda25
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.this.m4133lambda$loadPasswordInfo$33$orgtelegramuiPaymentFormActivity();
                    }
                };
                this.shortPollRunnable = runnable;
                AndroidUtilities.runOnUIThread(runnable, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            }
        }
    }

    /* renamed from: lambda$loadPasswordInfo$33$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4133lambda$loadPasswordInfo$33$orgtelegramuiPaymentFormActivity() {
        if (this.shortPollRunnable == null) {
            return;
        }
        loadPasswordInfo();
        this.shortPollRunnable = null;
    }

    private void showAlertWithText(String title, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        builder.setTitle(title);
        builder.setMessage(text);
        showDialog(builder.create());
    }

    private void showPayAlert(String totalPrice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("PaymentTransactionReview", R.string.PaymentTransactionReview));
        builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("PaymentTransactionMessage2", R.string.PaymentTransactionMessage2, totalPrice, this.currentBotName, this.currentItemName)));
        builder.setPositiveButton(LocaleController.getString("Continue", R.string.Continue), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda43
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                PaymentFormActivity.this.m4156lambda$showPayAlert$36$orgtelegramuiPaymentFormActivity(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    /* renamed from: lambda$showPayAlert$36$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4156lambda$showPayAlert$36$orgtelegramuiPaymentFormActivity(DialogInterface dialogInterface, int i) {
        setDonePressed(true);
        sendData();
    }

    private JSONObject getBaseRequest() throws JSONException {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }

    private JSONObject getBaseCardPaymentMethod() throws JSONException {
        List<String> SUPPORTED_NETWORKS = Arrays.asList("AMEX", "DISCOVER", Card.JCB, "MASTERCARD", "VISA");
        List<String> SUPPORTED_METHODS = Arrays.asList("PAN_ONLY", "CRYPTOGRAM_3DS");
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put(CommonProperties.TYPE, "CARD");
        JSONObject parameters = new JSONObject();
        parameters.put("allowedAuthMethods", new JSONArray((Collection) SUPPORTED_METHODS));
        parameters.put("allowedCardNetworks", new JSONArray((Collection) SUPPORTED_NETWORKS));
        cardPaymentMethod.put("parameters", parameters);
        return cardPaymentMethod;
    }

    public Optional<JSONObject> getIsReadyToPayRequest() {
        try {
            JSONObject isReadyToPayRequest = getBaseRequest();
            isReadyToPayRequest.put("allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));
            return Optional.of(isReadyToPayRequest);
        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    /* JADX WARN: Generic types in debug info not equals: j$.util.Optional != java.util.Optional<org.json.JSONObject> */
    private void initGooglePay(Context context) {
        IsReadyToPayRequest request;
        if (Build.VERSION.SDK_INT < 19 || getParentActivity() == null) {
            return;
        }
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder().setEnvironment(this.paymentForm.invoice.test ? 3 : 1).setTheme(1).build();
        this.paymentsClient = Wallet.getPaymentsClient(context, walletOptions);
        Optional<JSONObject> isReadyToPayRequest = getIsReadyToPayRequest();
        if (!isReadyToPayRequest.isPresent() || (request = IsReadyToPayRequest.fromJson(isReadyToPayRequest.get().toString())) == null) {
            return;
        }
        Task<Boolean> task = this.paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(getParentActivity(), new OnCompleteListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda24
            @Override // com.google.android.gms.tasks.OnCompleteListener
            public final void onComplete(Task task2) {
                PaymentFormActivity.this.m4132lambda$initGooglePay$37$orgtelegramuiPaymentFormActivity(task2);
            }
        });
    }

    /* renamed from: lambda$initGooglePay$37$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4132lambda$initGooglePay$37$orgtelegramuiPaymentFormActivity(Task task1) {
        if (task1.isSuccessful()) {
            FrameLayout frameLayout = this.googlePayContainer;
            if (frameLayout != null) {
                frameLayout.setVisibility(0);
                return;
            }
            return;
        }
        FileLog.e("isReadyToPay failed", task1.getException());
    }

    private String getTotalPriceString(ArrayList<TLRPC.TL_labeledPrice> prices) {
        long amount = 0;
        for (int a = 0; a < prices.size(); a++) {
            amount += prices.get(a).amount;
        }
        Long l = this.tipAmount;
        if (l != null) {
            amount += l.longValue();
        }
        return LocaleController.getInstance().formatCurrencyString(amount, this.paymentForm.invoice.currency);
    }

    private String getTotalPriceDecimalString(ArrayList<TLRPC.TL_labeledPrice> prices) {
        long amount = 0;
        for (int a = 0; a < prices.size(); a++) {
            amount += prices.get(a).amount;
        }
        return LocaleController.getInstance().formatCurrencyDecimalString(amount, this.paymentForm.invoice.currency, false);
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
        if (this.parentLayout == null || this.parentLayout.fragmentsStack == null) {
            return 0;
        }
        int cur = this.parentLayout.fragmentsStack.indexOf(this);
        if (cur == -1) {
            cur = this.parentLayout.fragmentsStack.size();
        }
        int i = cur;
        int a = 0;
        while (true) {
            if (a >= this.parentLayout.fragmentsStack.size()) {
                break;
            }
            BaseFragment fragment = this.parentLayout.fragmentsStack.get(a);
            if (!(fragment instanceof PaymentFormActivity)) {
                a++;
            } else {
                i = a;
                break;
            }
        }
        int a2 = i - cur;
        return a2;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        PaymentFormActivityDelegate paymentFormActivityDelegate = this.delegate;
        if (paymentFormActivityDelegate != null) {
            paymentFormActivityDelegate.onFragmentDestroyed();
        }
        if (!this.paymentStatusSent && this.paymentFormCallback != null && getOtherSameFragmentDiff() == 0) {
            this.paymentFormCallback.onInvoiceStatusChanged(InvoiceStatus.CANCELLED);
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
                getParentActivity().getWindow().clearFlags(8192);
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
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
    public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (isOpen && !backward) {
            WebView webView = this.webView;
            if (webView != null) {
                if (this.currentStep != 4) {
                    String str = this.paymentForm.url;
                    this.webViewUrl = str;
                    webView.loadUrl(str);
                    return;
                }
                return;
            }
            int i = this.currentStep;
            if (i == 2) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda26
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.this.m4137xc6803560();
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
            } else if (i == 6 && !this.waitingForEmail) {
                this.inputFields[0].requestFocus();
                AndroidUtilities.showKeyboard(this.inputFields[0]);
            }
        }
    }

    /* renamed from: lambda$onTransitionAnimationEnd$38$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4137xc6803560() {
        this.inputFields[0].requestFocus();
        AndroidUtilities.showKeyboard(this.inputFields[0]);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.twoStepPasswordChanged) {
            this.paymentForm.password_missing = false;
            this.paymentForm.can_save_credentials = true;
            updateSavePaymentField();
        } else if (id == NotificationCenter.didRemoveTwoStepPassword) {
            this.paymentForm.password_missing = true;
            this.paymentForm.can_save_credentials = false;
            updateSavePaymentField();
        } else if (id == NotificationCenter.paymentFinished) {
            this.paymentStatusSent = true;
            removeSelfFromStack();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int requestCode, final int resultCode, final Intent data) {
        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.m4136xb241c511(resultCode, data);
                }
            });
        }
    }

    /* renamed from: lambda$onActivityResultFragment$39$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4136xb241c511(int resultCode, Intent data) {
        String paymentInfo;
        if (resultCode == -1) {
            PaymentData paymentData = PaymentData.getFromIntent(data);
            if (paymentData == null || (paymentInfo = paymentData.toJson()) == null) {
                return;
            }
            try {
                JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
                JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
                tokenizationData.getString(CommonProperties.TYPE);
                String token = tokenizationData.getString("token");
                if (this.googlePayPublicKey == null && this.googlePayParameters == null) {
                    Token t = TokenParser.parseToken(token);
                    this.paymentJson = String.format(Locale.US, "{\"type\":\"%1$s\", \"id\":\"%2$s\"}", t.getType(), t.getId());
                    Card card = t.getCard();
                    this.cardName = card.getType() + " *" + card.getLast4();
                    goToNextStep();
                }
                TLRPC.TL_inputPaymentCredentialsGooglePay tL_inputPaymentCredentialsGooglePay = new TLRPC.TL_inputPaymentCredentialsGooglePay();
                this.googlePayCredentials = tL_inputPaymentCredentialsGooglePay;
                tL_inputPaymentCredentialsGooglePay.payment_token = new TLRPC.TL_dataJSON();
                this.googlePayCredentials.payment_token.data = tokenizationData.toString();
                String descriptions = paymentMethodData.optString("description");
                if (!TextUtils.isEmpty(descriptions)) {
                    this.cardName = descriptions;
                } else {
                    this.cardName = "Android Pay";
                }
                goToNextStep();
            } catch (JSONException e) {
                FileLog.e(e);
            }
        } else if (resultCode == 1) {
            Status status = AutoResolveHelper.getStatusFromIntent(data);
            StringBuilder sb = new StringBuilder();
            sb.append("android pay error ");
            sb.append(status != null ? status.getStatusMessage() : "");
            FileLog.e(sb.toString());
        }
        showEditDoneProgress(true, false);
        setDonePressed(false);
        this.googlePayButton.setClickable(true);
    }

    public void goToNextStep() {
        int nextStep;
        int nextStep2;
        int nextStep3;
        switch (this.currentStep) {
            case 0:
                PaymentFormActivityDelegate paymentFormActivityDelegate = this.delegate;
                if (paymentFormActivityDelegate != null) {
                    paymentFormActivityDelegate.didSelectNewAddress(this.validateRequest);
                    finishFragment();
                    return;
                }
                if (this.paymentForm.invoice.flexible) {
                    nextStep = 1;
                } else if (this.paymentForm.saved_credentials != null) {
                    if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
                        UserConfig.getInstance(this.currentAccount).tmpPassword = null;
                        UserConfig.getInstance(this.currentAccount).saveConfig(false);
                    }
                    if (UserConfig.getInstance(this.currentAccount).tmpPassword != null) {
                        nextStep = 4;
                    } else {
                        nextStep = 3;
                    }
                } else {
                    nextStep = 2;
                }
                presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, nextStep, this.requestedInfo, null, null, null, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment), this.isWebView);
                return;
            case 1:
                if (this.paymentForm.saved_credentials != null) {
                    if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
                        UserConfig.getInstance(this.currentAccount).tmpPassword = null;
                        UserConfig.getInstance(this.currentAccount).saveConfig(false);
                    }
                    if (UserConfig.getInstance(this.currentAccount).tmpPassword != null) {
                        nextStep2 = 4;
                    } else {
                        nextStep2 = 3;
                    }
                } else {
                    nextStep2 = 2;
                }
                presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, nextStep2, this.requestedInfo, this.shippingOption, this.tipAmount, null, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment), this.isWebView);
                return;
            case 2:
                if (this.paymentForm.password_missing && this.saveCardInfo) {
                    PaymentFormActivity paymentFormActivity = new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, 6, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment);
                    this.passwordFragment = paymentFormActivity;
                    paymentFormActivity.setCurrentPassword(this.currentPassword);
                    this.passwordFragment.setDelegate(new PaymentFormActivityDelegate() { // from class: org.telegram.ui.PaymentFormActivity.24
                        @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                        public /* synthetic */ void didSelectNewAddress(TLRPC.TL_payments_validateRequestedInfo tL_payments_validateRequestedInfo) {
                            PaymentFormActivityDelegate.CC.$default$didSelectNewAddress(this, tL_payments_validateRequestedInfo);
                        }

                        @Override // org.telegram.ui.PaymentFormActivity.PaymentFormActivityDelegate
                        public boolean didSelectNewCard(String tokenJson, String card, boolean saveCard, TLRPC.TL_inputPaymentCredentialsGooglePay googlePay) {
                            if (PaymentFormActivity.this.delegate != null) {
                                PaymentFormActivity.this.delegate.didSelectNewCard(tokenJson, card, saveCard, googlePay);
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
                        public void currentPasswordUpdated(TLRPC.TL_account_password password) {
                            PaymentFormActivity.this.currentPassword = password;
                        }
                    });
                    presentFragment(this.passwordFragment, this.isWebView);
                    return;
                }
                PaymentFormActivityDelegate paymentFormActivityDelegate2 = this.delegate;
                if (paymentFormActivityDelegate2 != null) {
                    paymentFormActivityDelegate2.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.googlePayCredentials);
                    finishFragment();
                    return;
                }
                presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, 4, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment), this.isWebView);
                return;
            case 3:
                if (this.passwordOk) {
                    nextStep3 = 4;
                } else {
                    nextStep3 = 2;
                }
                presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, nextStep3, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment), true);
                return;
            case 4:
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.paymentFinished, new Object[0]);
                if ((this.botUser.username != null && this.botUser.username.equalsIgnoreCase(getMessagesController().premiumBotUsername)) || (this.invoiceSlug != null && getMessagesController().premiumInvoiceSlug != null && ColorUtils$$ExternalSyntheticBackport0.m(this.invoiceSlug, getMessagesController().premiumInvoiceSlug))) {
                    Iterator it = new ArrayList(getParentLayout().fragmentsStack).iterator();
                    while (it.hasNext()) {
                        BaseFragment fragment = (BaseFragment) it.next();
                        if ((fragment instanceof ChatActivity) || (fragment instanceof PremiumPreviewFragment)) {
                            fragment.removeSelfFromStack();
                        }
                    }
                    presentFragment(new PremiumPreviewFragment(null).setForcePremium(), true);
                    if (getParentActivity() instanceof LaunchActivity) {
                        try {
                            this.fragmentView.performHapticFeedback(3, 2);
                        } catch (Exception e) {
                        }
                        ((LaunchActivity) getParentActivity()).getFireworksOverlay().start();
                        return;
                    }
                    return;
                }
                finishFragment();
                return;
            case 5:
            default:
                return;
            case 6:
                if (!this.delegate.didSelectNewCard(this.paymentJson, this.cardName, this.saveCardInfo, this.googlePayCredentials)) {
                    presentFragment(new PaymentFormActivity(this.paymentForm, this.messageObject, this.invoiceSlug, 4, this.requestedInfo, this.shippingOption, this.tipAmount, this.paymentJson, this.cardName, this.validateRequest, this.saveCardInfo, this.googlePayCredentials, this.parentFragment), true);
                    return;
                } else {
                    finishFragment();
                    return;
                }
        }
    }

    public void updateSavePaymentField() {
        if (this.bottomCell[0] == null || this.sectionCell[2] == null) {
            return;
        }
        if ((this.paymentForm.password_missing || this.paymentForm.can_save_credentials) && (this.webView == null || !this.webviewLoading)) {
            SpannableStringBuilder text = new SpannableStringBuilder(LocaleController.getString("PaymentCardSavePaymentInformationInfoLine1", R.string.PaymentCardSavePaymentInformationInfoLine1));
            if (this.paymentForm.password_missing) {
                loadPasswordInfo();
                text.append((CharSequence) "\n");
                int len = text.length();
                String str2 = LocaleController.getString("PaymentCardSavePaymentInformationInfoLine2", R.string.PaymentCardSavePaymentInformationInfoLine2);
                int index1 = str2.indexOf(42);
                int index2 = str2.lastIndexOf(42);
                text.append((CharSequence) str2);
                if (index1 != -1 && index2 != -1) {
                    int index12 = index1 + len;
                    int index22 = index2 + len;
                    this.bottomCell[0].getTextView().setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                    text.replace(index22, index22 + 1, (CharSequence) "");
                    text.replace(index12, index12 + 1, (CharSequence) "");
                    text.setSpan(new LinkSpan(), index12, index22 - 1, 33);
                }
            }
            this.checkCell1.setEnabled(true);
            this.bottomCell[0].setText(text);
            this.checkCell1.setVisibility(0);
            this.bottomCell[0].setVisibility(0);
            ShadowSectionCell[] shadowSectionCellArr = this.sectionCell;
            shadowSectionCellArr[2].setBackgroundDrawable(Theme.getThemedDrawable(shadowSectionCellArr[2].getContext(), (int) R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));
            return;
        }
        this.checkCell1.setVisibility(8);
        this.bottomCell[0].setVisibility(8);
        ShadowSectionCell[] shadowSectionCellArr2 = this.sectionCell;
        shadowSectionCellArr2[2].setBackgroundDrawable(Theme.getThemedDrawable(shadowSectionCellArr2[2].getContext(), (int) R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
    }

    public void fillNumber(String number) {
        try {
            TelephonyManager tm = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
            boolean allowCall = true;
            if (number != null || (tm.getSimState() != 1 && tm.getPhoneType() != 0)) {
                if (Build.VERSION.SDK_INT >= 23) {
                    allowCall = getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0;
                }
                if (number != null || allowCall) {
                    if (number == null) {
                        number = PhoneFormat.stripExceptNumbers(tm.getLine1Number());
                    }
                    String textToSet = null;
                    boolean ok = false;
                    if (!TextUtils.isEmpty(number)) {
                        if (number.length() > 4) {
                            int a = 4;
                            while (true) {
                                if (a < 1) {
                                    break;
                                }
                                String sub = number.substring(0, a);
                                String country = this.codesMap.get(sub);
                                if (country == null) {
                                    a--;
                                } else {
                                    ok = true;
                                    textToSet = number.substring(a);
                                    this.inputFields[8].setText(sub);
                                    break;
                                }
                            }
                            if (!ok) {
                                textToSet = number.substring(1);
                                this.inputFields[8].setText(number.substring(0, 1));
                            }
                        }
                        if (textToSet != null) {
                            this.inputFields[9].setText(textToSet);
                            EditTextBoldCursor[] editTextBoldCursorArr = this.inputFields;
                            editTextBoldCursorArr[9].setSelection(editTextBoldCursorArr[9].length());
                        }
                    }
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void sendSavePassword(final boolean clear) {
        String firstPassword;
        String email;
        if (!clear && this.codeFieldCell.getVisibility() == 0) {
            String code = this.codeFieldCell.getText();
            if (code.length() == 0) {
                shakeView(this.codeFieldCell);
                return;
            }
            showEditDoneProgress(true, true);
            TLRPC.TL_account_confirmPasswordEmail req = new TLRPC.TL_account_confirmPasswordEmail();
            req.code = code;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda47
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    PaymentFormActivity.this.m4146lambda$sendSavePassword$41$orgtelegramuiPaymentFormActivity(tLObject, tL_error);
                }
            }, 10);
            return;
        }
        final TLRPC.TL_account_updatePasswordSettings req2 = new TLRPC.TL_account_updatePasswordSettings();
        if (clear) {
            this.doneItem.setVisibility(0);
            req2.new_settings = new TLRPC.TL_account_passwordInputSettings();
            req2.new_settings.flags = 2;
            req2.new_settings.email = "";
            req2.password = new TLRPC.TL_inputCheckPasswordEmpty();
            email = null;
            firstPassword = null;
        } else {
            String firstPassword2 = this.inputFields[0].getText().toString();
            if (!TextUtils.isEmpty(firstPassword2)) {
                String secondPassword = this.inputFields[1].getText().toString();
                if (!firstPassword2.equals(secondPassword)) {
                    try {
                        Toast.makeText(getParentActivity(), LocaleController.getString("PasswordDoNotMatch", R.string.PasswordDoNotMatch), 0).show();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    shakeField(1);
                    return;
                }
                String email2 = this.inputFields[2].getText().toString();
                if (email2.length() < 3) {
                    shakeField(2);
                    return;
                }
                int dot = email2.lastIndexOf(46);
                int dog = email2.lastIndexOf(64);
                if (dog < 0 || dot < dog) {
                    shakeField(2);
                    return;
                }
                req2.password = new TLRPC.TL_inputCheckPasswordEmpty();
                req2.new_settings = new TLRPC.TL_account_passwordInputSettings();
                req2.new_settings.flags |= 1;
                req2.new_settings.hint = "";
                req2.new_settings.new_algo = this.currentPassword.new_algo;
                TLRPC.TL_account_passwordInputSettings tL_account_passwordInputSettings = req2.new_settings;
                tL_account_passwordInputSettings.flags = 2 | tL_account_passwordInputSettings.flags;
                req2.new_settings.email = email2.trim();
                email = email2;
                firstPassword = firstPassword2;
            } else {
                shakeField(0);
                return;
            }
        }
        showEditDoneProgress(true, true);
        final String str = email;
        final String str2 = firstPassword;
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda44
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.m4152lambda$sendSavePassword$47$orgtelegramuiPaymentFormActivity(clear, str, str2, req2);
            }
        });
    }

    /* renamed from: lambda$sendSavePassword$41$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4146lambda$sendSavePassword$41$orgtelegramuiPaymentFormActivity(TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.m4145lambda$sendSavePassword$40$orgtelegramuiPaymentFormActivity(error);
            }
        });
    }

    /* renamed from: lambda$sendSavePassword$40$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4145lambda$sendSavePassword$40$orgtelegramuiPaymentFormActivity(TLRPC.TL_error error) {
        String timeString;
        showEditDoneProgress(true, false);
        if (error == null) {
            if (getParentActivity() == null) {
                return;
            }
            Runnable runnable = this.shortPollRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
                this.shortPollRunnable = null;
            }
            goToNextStep();
        } else if (error.text.startsWith("CODE_INVALID")) {
            shakeView(this.codeFieldCell);
            this.codeFieldCell.setText("", false);
        } else if (error.text.startsWith("FLOOD_WAIT")) {
            int time = Utilities.parseInt((CharSequence) error.text).intValue();
            if (time < 60) {
                timeString = LocaleController.formatPluralString("Seconds", time, new Object[0]);
            } else {
                timeString = LocaleController.formatPluralString("Minutes", time / 60, new Object[0]);
            }
            showAlertWithText(LocaleController.getString("AppName", R.string.AppName), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, timeString));
        } else {
            showAlertWithText(LocaleController.getString("AppName", R.string.AppName), error.text);
        }
    }

    /* renamed from: lambda$sendSavePassword$46$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4151lambda$sendSavePassword$46$orgtelegramuiPaymentFormActivity(final boolean clear, final String email, final TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.m4150lambda$sendSavePassword$45$orgtelegramuiPaymentFormActivity(error, clear, response, email);
            }
        });
    }

    /* renamed from: lambda$sendSavePassword$47$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4152lambda$sendSavePassword$47$orgtelegramuiPaymentFormActivity(final boolean clear, final String email, String firstPassword, TLRPC.TL_account_updatePasswordSettings req) {
        RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda55
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                PaymentFormActivity.this.m4151lambda$sendSavePassword$46$orgtelegramuiPaymentFormActivity(clear, email, tLObject, tL_error);
            }
        };
        if (clear) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, requestDelegate, 10);
            return;
        }
        byte[] newPasswordBytes = AndroidUtilities.getStringBytes(firstPassword);
        if (this.currentPassword.new_algo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow algo = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) this.currentPassword.new_algo;
            req.new_settings.new_password_hash = SRPHelper.getVBytes(newPasswordBytes, algo);
            if (req.new_settings.new_password_hash == null) {
                TLRPC.TL_error error = new TLRPC.TL_error();
                error.text = "ALGO_INVALID";
                requestDelegate.run(null, error);
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, requestDelegate, 10);
            return;
        }
        TLRPC.TL_error error2 = new TLRPC.TL_error();
        error2.text = "PASSWORD_HASH_INVALID";
        requestDelegate.run(null, error2);
    }

    /* renamed from: lambda$sendSavePassword$45$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4150lambda$sendSavePassword$45$orgtelegramuiPaymentFormActivity(TLRPC.TL_error error, final boolean clear, TLObject response, final String email) {
        String timeString;
        if (error != null && "SRP_ID_INVALID".equals(error.text)) {
            TLRPC.TL_account_getPassword getPasswordReq = new TLRPC.TL_account_getPassword();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(getPasswordReq, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda54
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    PaymentFormActivity.this.m4148lambda$sendSavePassword$43$orgtelegramuiPaymentFormActivity(clear, tLObject, tL_error);
                }
            }, 8);
            return;
        }
        showEditDoneProgress(true, false);
        if (clear) {
            this.currentPassword.has_password = false;
            this.currentPassword.current_algo = null;
            this.delegate.currentPasswordUpdated(this.currentPassword);
            finishFragment();
        } else if (error == null && (response instanceof TLRPC.TL_boolTrue)) {
            if (getParentActivity() == null) {
                return;
            }
            goToNextStep();
        } else if (error != null) {
            if (error.text.equals("EMAIL_UNCONFIRMED") || error.text.startsWith("EMAIL_UNCONFIRMED_")) {
                this.emailCodeLength = Utilities.parseInt((CharSequence) error.text).intValue();
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda53
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        PaymentFormActivity.this.m4149lambda$sendSavePassword$44$orgtelegramuiPaymentFormActivity(email, dialogInterface, i);
                    }
                });
                builder.setMessage(LocaleController.getString("YourEmailAlmostThereText", R.string.YourEmailAlmostThereText));
                builder.setTitle(LocaleController.getString("YourEmailAlmostThere", R.string.YourEmailAlmostThere));
                Dialog dialog = showDialog(builder.create());
                if (dialog != null) {
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                }
            } else if (error.text.equals("EMAIL_INVALID")) {
                showAlertWithText(LocaleController.getString("AppName", R.string.AppName), LocaleController.getString("PasswordEmailInvalid", R.string.PasswordEmailInvalid));
            } else if (error.text.startsWith("FLOOD_WAIT")) {
                int time = Utilities.parseInt((CharSequence) error.text).intValue();
                if (time < 60) {
                    timeString = LocaleController.formatPluralString("Seconds", time, new Object[0]);
                } else {
                    timeString = LocaleController.formatPluralString("Minutes", time / 60, new Object[0]);
                }
                showAlertWithText(LocaleController.getString("AppName", R.string.AppName), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, timeString));
            } else {
                showAlertWithText(LocaleController.getString("AppName", R.string.AppName), error.text);
            }
        }
    }

    /* renamed from: lambda$sendSavePassword$43$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4148lambda$sendSavePassword$43$orgtelegramuiPaymentFormActivity(final boolean clear, final TLObject response2, final TLRPC.TL_error error2) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda40
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.m4147lambda$sendSavePassword$42$orgtelegramuiPaymentFormActivity(error2, response2, clear);
            }
        });
    }

    /* renamed from: lambda$sendSavePassword$42$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4147lambda$sendSavePassword$42$orgtelegramuiPaymentFormActivity(TLRPC.TL_error error2, TLObject response2, boolean clear) {
        if (error2 == null) {
            TLRPC.TL_account_password tL_account_password = (TLRPC.TL_account_password) response2;
            this.currentPassword = tL_account_password;
            TwoStepVerificationActivity.initPasswordNewAlgo(tL_account_password);
            sendSavePassword(clear);
        }
    }

    /* renamed from: lambda$sendSavePassword$44$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4149lambda$sendSavePassword$44$orgtelegramuiPaymentFormActivity(String email, DialogInterface dialogInterface, int i) {
        this.waitingForEmail = true;
        this.currentPassword.email_unconfirmed_pattern = email;
        updatePasswordFields();
    }

    public boolean sendCardData() {
        Integer year;
        Integer month;
        int i;
        String date = this.inputFields[1].getText().toString();
        String[] args = date.split("/");
        if (args.length == 2) {
            Integer month2 = Utilities.parseInt((CharSequence) args[0]);
            year = Utilities.parseInt((CharSequence) args[1]);
            month = month2;
        } else {
            year = null;
            month = null;
        }
        final Card card = new Card(this.inputFields[0].getText().toString(), month, year, this.inputFields[3].getText().toString(), this.inputFields[2].getText().toString(), null, null, null, null, this.inputFields[5].getText().toString(), this.inputFields[4].getText().toString(), null);
        this.cardName = card.getType() + " *" + card.getLast4();
        if (!card.validateNumber()) {
            shakeField(0);
            return false;
        }
        if (!card.validateExpMonth() || !card.validateExpYear()) {
            i = 1;
        } else if (card.validateExpiryDate()) {
            if (this.need_card_name && this.inputFields[2].length() == 0) {
                shakeField(2);
                return false;
            } else if (!card.validateCVC()) {
                shakeField(3);
                return false;
            } else if (this.need_card_country && this.inputFields[4].length() == 0) {
                shakeField(4);
                return false;
            } else if (!this.need_card_postcode || this.inputFields[5].length() != 0) {
                showEditDoneProgress(true, true);
                try {
                    if ("stripe".equals(this.paymentForm.native_provider)) {
                        Stripe stripe = new Stripe(this.providerApiKey);
                        stripe.createToken(card, new AnonymousClass25());
                    } else if ("smartglocal".equals(this.paymentForm.native_provider)) {
                        AsyncTask<Object, Object, String> task = new AsyncTask<Object, Object, String>() { // from class: org.telegram.ui.PaymentFormActivity.26
                            @Override // android.os.AsyncTask
                            public String doInBackground(Object... objects) {
                                int code;
                                HttpURLConnection conn = null;
                                try {
                                    try {
                                        JSONObject jsonObject = new JSONObject();
                                        JSONObject cardObject = new JSONObject();
                                        cardObject.put("number", card.getNumber());
                                        cardObject.put("expiration_month", String.format(Locale.US, "%02d", card.getExpMonth()));
                                        cardObject.put("expiration_year", "" + card.getExpYear());
                                        cardObject.put("security_code", "" + card.getCVC());
                                        jsonObject.put(Token.TYPE_CARD, cardObject);
                                        URL connectionUrl = PaymentFormActivity.this.paymentForm.invoice.test ? new URL("https://tgb-playground.smart-glocal.com/cds/v1/tokenize/card") : new URL("https://tgb.smart-glocal.com/cds/v1/tokenize/card");
                                        conn = (HttpURLConnection) connectionUrl.openConnection();
                                        conn.setConnectTimeout(Indexable.MAX_BYTE_SIZE);
                                        conn.setReadTimeout(80000);
                                        conn.setUseCaches(false);
                                        conn.setDoOutput(true);
                                        conn.setRequestMethod(DefaultHttpClient.METHOD_POST);
                                        conn.setRequestProperty(DefaultHttpClient.CONTENT_TYPE_KEY, "application/json");
                                        conn.setRequestProperty("X-PUBLIC-TOKEN", PaymentFormActivity.this.providerApiKey);
                                        OutputStream output = conn.getOutputStream();
                                        try {
                                            output.write(jsonObject.toString().getBytes("UTF-8"));
                                            if (output != null) {
                                                output.close();
                                            }
                                            code = conn.getResponseCode();
                                        } catch (Throwable th) {
                                            if (output != null) {
                                                try {
                                                    output.close();
                                                } catch (Throwable th2) {
                                                }
                                            }
                                            throw th;
                                        }
                                    } catch (Throwable th3) {
                                        if (0 != 0) {
                                            conn.disconnect();
                                        }
                                        throw th3;
                                    }
                                } catch (Exception e) {
                                    FileLog.e(e);
                                    if (0 == 0) {
                                        return null;
                                    }
                                }
                                if (code < 200 || code >= 300) {
                                    if (BuildVars.DEBUG_VERSION) {
                                        FileLog.e("" + PaymentFormActivity.getResponseBody(conn.getErrorStream()));
                                    }
                                    if (conn == null) {
                                        return null;
                                    }
                                    conn.disconnect();
                                    return null;
                                }
                                JSONObject result = new JSONObject();
                                JSONObject jsonObject1 = new JSONObject(PaymentFormActivity.getResponseBody(conn.getInputStream()));
                                String token = jsonObject1.getJSONObject("data").getString("token");
                                result.put("token", token);
                                result.put(CommonProperties.TYPE, Token.TYPE_CARD);
                                String jSONObject = result.toString();
                                if (conn != null) {
                                    conn.disconnect();
                                }
                                return jSONObject;
                            }

                            public void onPostExecute(String result) {
                                if (PaymentFormActivity.this.canceled) {
                                    return;
                                }
                                if (result != null) {
                                    PaymentFormActivity.this.paymentJson = result;
                                    PaymentFormActivity.this.goToNextStep();
                                } else {
                                    AlertsCreator.showSimpleToast(PaymentFormActivity.this, LocaleController.getString("PaymentConnectionFailed", R.string.PaymentConnectionFailed));
                                }
                                PaymentFormActivity.this.showEditDoneProgress(true, false);
                                PaymentFormActivity.this.setDonePressed(false);
                            }
                        };
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null, null, null);
                    }
                    return true;
                } catch (Exception e) {
                    FileLog.e(e);
                    return true;
                }
            } else {
                shakeField(5);
                return false;
            }
        } else {
            i = 1;
        }
        shakeField(i);
        return false;
    }

    /* renamed from: org.telegram.ui.PaymentFormActivity$25 */
    /* loaded from: classes4.dex */
    public class AnonymousClass25 implements TokenCallback {
        AnonymousClass25() {
            PaymentFormActivity.this = this$0;
        }

        @Override // com.stripe.android.TokenCallback
        public void onSuccess(Token token) {
            if (!PaymentFormActivity.this.canceled) {
                PaymentFormActivity.this.paymentJson = String.format(Locale.US, "{\"type\":\"%1$s\", \"id\":\"%2$s\"}", token.getType(), token.getId());
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$25$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.AnonymousClass25.this.m4157lambda$onSuccess$0$orgtelegramuiPaymentFormActivity$25();
                    }
                });
            }
        }

        /* renamed from: lambda$onSuccess$0$org-telegram-ui-PaymentFormActivity$25 */
        public /* synthetic */ void m4157lambda$onSuccess$0$orgtelegramuiPaymentFormActivity$25() {
            PaymentFormActivity.this.goToNextStep();
            PaymentFormActivity.this.showEditDoneProgress(true, false);
            PaymentFormActivity.this.setDonePressed(false);
        }

        @Override // com.stripe.android.TokenCallback
        public void onError(Exception error) {
            if (!PaymentFormActivity.this.canceled) {
                PaymentFormActivity.this.showEditDoneProgress(true, false);
                PaymentFormActivity.this.setDonePressed(false);
                if ((error instanceof APIConnectionException) || (error instanceof APIException)) {
                    AlertsCreator.showSimpleToast(PaymentFormActivity.this, LocaleController.getString("PaymentConnectionFailed", R.string.PaymentConnectionFailed));
                } else {
                    AlertsCreator.showSimpleToast(PaymentFormActivity.this, error.getMessage());
                }
            }
        }
    }

    public static String getResponseBody(InputStream responseStream) throws IOException {
        String rBody = new Scanner(responseStream, "UTF-8").useDelimiter("\\A").next();
        responseStream.close();
        return rBody;
    }

    private void sendSavedForm(final Runnable callback) {
        if (this.canceled) {
            return;
        }
        showEditDoneProgress(true, true);
        this.validateRequest = new TLRPC.TL_payments_validateRequestedInfo();
        if (this.messageObject != null) {
            TLRPC.TL_inputInvoiceMessage inputInvoice = new TLRPC.TL_inputInvoiceMessage();
            inputInvoice.peer = getMessagesController().getInputPeer(this.messageObject.messageOwner.peer_id);
            inputInvoice.msg_id = this.messageObject.getId();
            this.validateRequest.invoice = inputInvoice;
        } else {
            TLRPC.TL_inputInvoiceSlug inputInvoice2 = new TLRPC.TL_inputInvoiceSlug();
            inputInvoice2.slug = this.invoiceSlug;
            this.validateRequest.invoice = inputInvoice2;
        }
        this.validateRequest.save = true;
        this.validateRequest.info = this.paymentForm.saved_info;
        final TLObject req = this.validateRequest;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda48
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                PaymentFormActivity.this.m4155lambda$sendSavedForm$50$orgtelegramuiPaymentFormActivity(callback, req, tLObject, tL_error);
            }
        }, 2);
    }

    /* renamed from: lambda$sendSavedForm$50$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4155lambda$sendSavedForm$50$orgtelegramuiPaymentFormActivity(final Runnable callback, final TLObject req, final TLObject response, final TLRPC.TL_error error) {
        if (response instanceof TLRPC.TL_payments_validatedRequestedInfo) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda31
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.m4153lambda$sendSavedForm$48$orgtelegramuiPaymentFormActivity(response, callback);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.m4154lambda$sendSavedForm$49$orgtelegramuiPaymentFormActivity(error, req);
                }
            });
        }
    }

    /* renamed from: lambda$sendSavedForm$48$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4153lambda$sendSavedForm$48$orgtelegramuiPaymentFormActivity(TLObject response, Runnable callback) {
        this.requestedInfo = (TLRPC.TL_payments_validatedRequestedInfo) response;
        callback.run();
        setDonePressed(false);
        showEditDoneProgress(true, false);
    }

    /* renamed from: lambda$sendSavedForm$49$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4154lambda$sendSavedForm$49$orgtelegramuiPaymentFormActivity(TLRPC.TL_error error, TLObject req) {
        setDonePressed(false);
        showEditDoneProgress(true, false);
        if (error != null) {
            AlertsCreator.processError(this.currentAccount, error, this, req, new Object[0]);
        }
    }

    public void sendForm() {
        if (this.canceled) {
            return;
        }
        showEditDoneProgress(true, true);
        this.validateRequest = new TLRPC.TL_payments_validateRequestedInfo();
        if (this.messageObject != null) {
            TLRPC.TL_inputInvoiceMessage inputInvoice = new TLRPC.TL_inputInvoiceMessage();
            inputInvoice.peer = getMessagesController().getInputPeer(this.messageObject.messageOwner.peer_id);
            inputInvoice.msg_id = this.messageObject.getId();
            this.validateRequest.invoice = inputInvoice;
        } else {
            TLRPC.TL_inputInvoiceSlug inputInvoice2 = new TLRPC.TL_inputInvoiceSlug();
            inputInvoice2.slug = this.invoiceSlug;
            this.validateRequest.invoice = inputInvoice2;
        }
        this.validateRequest.save = this.saveShippingInfo;
        this.validateRequest.info = new TLRPC.TL_paymentRequestedInfo();
        if (this.paymentForm.invoice.name_requested) {
            this.validateRequest.info.name = this.inputFields[6].getText().toString();
            this.validateRequest.info.flags |= 1;
        }
        if (this.paymentForm.invoice.phone_requested) {
            this.validateRequest.info.phone = "+" + this.inputFields[8].getText().toString() + this.inputFields[9].getText().toString();
            TLRPC.TL_paymentRequestedInfo tL_paymentRequestedInfo = this.validateRequest.info;
            tL_paymentRequestedInfo.flags = tL_paymentRequestedInfo.flags | 2;
        }
        if (this.paymentForm.invoice.email_requested) {
            this.validateRequest.info.email = this.inputFields[7].getText().toString().trim();
            this.validateRequest.info.flags |= 4;
        }
        if (this.paymentForm.invoice.shipping_address_requested) {
            this.validateRequest.info.shipping_address = new TLRPC.TL_postAddress();
            this.validateRequest.info.shipping_address.street_line1 = this.inputFields[0].getText().toString();
            this.validateRequest.info.shipping_address.street_line2 = this.inputFields[1].getText().toString();
            this.validateRequest.info.shipping_address.city = this.inputFields[2].getText().toString();
            this.validateRequest.info.shipping_address.state = this.inputFields[3].getText().toString();
            TLRPC.TL_postAddress tL_postAddress = this.validateRequest.info.shipping_address;
            String str = this.countryName;
            if (str == null) {
                str = "";
            }
            tL_postAddress.country_iso2 = str;
            this.validateRequest.info.shipping_address.post_code = this.inputFields[5].getText().toString();
            this.validateRequest.info.flags |= 8;
        }
        final TLObject req = this.validateRequest;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(this.validateRequest, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda50
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                PaymentFormActivity.this.m4144lambda$sendForm$54$orgtelegramuiPaymentFormActivity(req, tLObject, tL_error);
            }
        }, 2);
    }

    /* renamed from: lambda$sendForm$54$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4144lambda$sendForm$54$orgtelegramuiPaymentFormActivity(final TLObject req, final TLObject response, final TLRPC.TL_error error) {
        if (response instanceof TLRPC.TL_payments_validatedRequestedInfo) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.m4142lambda$sendForm$52$orgtelegramuiPaymentFormActivity(response);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    PaymentFormActivity.this.m4143lambda$sendForm$53$orgtelegramuiPaymentFormActivity(error, req);
                }
            });
        }
    }

    /* renamed from: lambda$sendForm$52$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4142lambda$sendForm$52$orgtelegramuiPaymentFormActivity(TLObject response) {
        this.requestedInfo = (TLRPC.TL_payments_validatedRequestedInfo) response;
        if (this.paymentForm.saved_info != null && !this.saveShippingInfo) {
            TLRPC.TL_payments_clearSavedInfo req1 = new TLRPC.TL_payments_clearSavedInfo();
            req1.info = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req1, PaymentFormActivity$$ExternalSyntheticLambda57.INSTANCE);
        }
        goToNextStep();
        setDonePressed(false);
        showEditDoneProgress(true, false);
    }

    public static /* synthetic */ void lambda$sendForm$51(TLObject response1, TLRPC.TL_error error1) {
    }

    /* renamed from: lambda$sendForm$53$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4143lambda$sendForm$53$orgtelegramuiPaymentFormActivity(TLRPC.TL_error error, TLObject req) {
        setDonePressed(false);
        showEditDoneProgress(true, false);
        if (error != null) {
            String str = error.text;
            char c = 65535;
            switch (str.hashCode()) {
                case -2092780146:
                    if (str.equals("ADDRESS_CITY_INVALID")) {
                        c = 4;
                        break;
                    }
                    break;
                case -1623547228:
                    if (str.equals("ADDRESS_STREET_LINE1_INVALID")) {
                        c = 7;
                        break;
                    }
                    break;
                case -1224177757:
                    if (str.equals("ADDRESS_COUNTRY_INVALID")) {
                        c = 3;
                        break;
                    }
                    break;
                case -1031752045:
                    if (str.equals("REQ_INFO_NAME_INVALID")) {
                        c = 0;
                        break;
                    }
                    break;
                case -274035920:
                    if (str.equals("ADDRESS_POSTCODE_INVALID")) {
                        c = 5;
                        break;
                    }
                    break;
                case 417441502:
                    if (str.equals("ADDRESS_STATE_INVALID")) {
                        c = 6;
                        break;
                    }
                    break;
                case 708423542:
                    if (str.equals("REQ_INFO_PHONE_INVALID")) {
                        c = 1;
                        break;
                    }
                    break;
                case 863965605:
                    if (str.equals("ADDRESS_STREET_LINE2_INVALID")) {
                        c = '\b';
                        break;
                    }
                    break;
                case 889106340:
                    if (str.equals("REQ_INFO_EMAIL_INVALID")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    shakeField(6);
                    return;
                case 1:
                    shakeField(9);
                    return;
                case 2:
                    shakeField(7);
                    return;
                case 3:
                    shakeField(4);
                    return;
                case 4:
                    shakeField(2);
                    return;
                case 5:
                    shakeField(5);
                    return;
                case 6:
                    shakeField(3);
                    return;
                case 7:
                    shakeField(0);
                    return;
                case '\b':
                    shakeField(1);
                    return;
                default:
                    AlertsCreator.processError(this.currentAccount, error, this, req, new Object[0]);
                    return;
            }
        }
    }

    private void sendData() {
        if (this.canceled) {
            return;
        }
        showEditDoneProgress(false, true);
        final TLRPC.TL_payments_sendPaymentForm req = new TLRPC.TL_payments_sendPaymentForm();
        if (this.messageObject != null) {
            TLRPC.TL_inputInvoiceMessage inputInvoice = new TLRPC.TL_inputInvoiceMessage();
            inputInvoice.peer = getMessagesController().getInputPeer(this.messageObject.messageOwner.peer_id);
            inputInvoice.msg_id = this.messageObject.getId();
            req.invoice = inputInvoice;
        } else {
            TLRPC.TL_inputInvoiceSlug inputInvoice2 = new TLRPC.TL_inputInvoiceSlug();
            inputInvoice2.slug = this.invoiceSlug;
            req.invoice = inputInvoice2;
        }
        req.form_id = this.paymentForm.form_id;
        if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && this.paymentForm.saved_credentials != null) {
            req.credentials = new TLRPC.TL_inputPaymentCredentialsSaved();
            req.credentials.id = this.paymentForm.saved_credentials.id;
            req.credentials.tmp_password = UserConfig.getInstance(this.currentAccount).tmpPassword.tmp_password;
        } else {
            TLRPC.TL_inputPaymentCredentialsGooglePay tL_inputPaymentCredentialsGooglePay = this.googlePayCredentials;
            if (tL_inputPaymentCredentialsGooglePay != null) {
                req.credentials = tL_inputPaymentCredentialsGooglePay;
            } else {
                req.credentials = new TLRPC.TL_inputPaymentCredentials();
                req.credentials.save = this.saveCardInfo;
                req.credentials.data = new TLRPC.TL_dataJSON();
                req.credentials.data.data = this.paymentJson;
            }
        }
        TLRPC.TL_payments_validatedRequestedInfo tL_payments_validatedRequestedInfo = this.requestedInfo;
        if (tL_payments_validatedRequestedInfo != null && tL_payments_validatedRequestedInfo.id != null) {
            req.requested_info_id = this.requestedInfo.id;
            req.flags = 1 | req.flags;
        }
        TLRPC.TL_shippingOption tL_shippingOption = this.shippingOption;
        if (tL_shippingOption != null) {
            req.shipping_option_id = tL_shippingOption.id;
            req.flags |= 2;
        }
        if ((this.paymentForm.invoice.flags & 256) != 0) {
            Long l = this.tipAmount;
            req.tip_amount = l != null ? l.longValue() : 0L;
            req.flags |= 4;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda52
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                PaymentFormActivity.this.m4141lambda$sendData$58$orgtelegramuiPaymentFormActivity(req, tLObject, tL_error);
            }
        }, 2);
    }

    /* renamed from: lambda$sendData$58$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4141lambda$sendData$58$orgtelegramuiPaymentFormActivity(final TLRPC.TL_payments_sendPaymentForm req, final TLObject response, final TLRPC.TL_error error) {
        if (response != null) {
            if (response instanceof TLRPC.TL_payments_paymentResult) {
                TLRPC.Updates updates = ((TLRPC.TL_payments_paymentResult) response).updates;
                final TLRPC.Message[] message = new TLRPC.Message[1];
                int a = 0;
                int N = updates.updates.size();
                while (true) {
                    if (a >= N) {
                        break;
                    }
                    TLRPC.Update update = updates.updates.get(a);
                    if (update instanceof TLRPC.TL_updateNewMessage) {
                        message[0] = ((TLRPC.TL_updateNewMessage) update).message;
                        break;
                    } else if (!(update instanceof TLRPC.TL_updateNewChannelMessage)) {
                        a++;
                    } else {
                        message[0] = ((TLRPC.TL_updateNewChannelMessage) update).message;
                        break;
                    }
                }
                getMessagesController().processUpdates(updates, false);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda45
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.this.m4138lambda$sendData$55$orgtelegramuiPaymentFormActivity(message);
                    }
                });
                return;
            } else if (response instanceof TLRPC.TL_payments_paymentVerificationNeeded) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda29
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.this.m4139lambda$sendData$56$orgtelegramuiPaymentFormActivity(response);
                    }
                });
                return;
            } else {
                return;
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.m4140lambda$sendData$57$orgtelegramuiPaymentFormActivity(error, req);
            }
        });
    }

    /* renamed from: lambda$sendData$55$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4138lambda$sendData$55$orgtelegramuiPaymentFormActivity(TLRPC.Message[] message) {
        this.paymentStatusSent = true;
        PaymentFormCallback paymentFormCallback = this.paymentFormCallback;
        if (paymentFormCallback != null) {
            paymentFormCallback.onInvoiceStatusChanged(InvoiceStatus.PAID);
        }
        goToNextStep();
        if (this.parentFragment instanceof ChatActivity) {
            CharSequence info = AndroidUtilities.replaceTags(LocaleController.formatString("PaymentInfoHint", R.string.PaymentInfoHint, this.totalPrice[0], this.currentItemName));
            ((ChatActivity) this.parentFragment).getUndoView().showWithAction(0L, 77, info, message[0], (Runnable) null, (Runnable) null);
        }
    }

    /* renamed from: lambda$sendData$56$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4139lambda$sendData$56$orgtelegramuiPaymentFormActivity(TLObject response) {
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.paymentFinished, new Object[0]);
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
        WebView webView = this.webView;
        if (webView != null) {
            webView.setVisibility(0);
            WebView webView2 = this.webView;
            String str = ((TLRPC.TL_payments_paymentVerificationNeeded) response).url;
            this.webViewUrl = str;
            webView2.loadUrl(str);
        }
        this.paymentStatusSent = true;
        PaymentFormCallback paymentFormCallback = this.paymentFormCallback;
        if (paymentFormCallback != null) {
            paymentFormCallback.onInvoiceStatusChanged(InvoiceStatus.PENDING);
        }
    }

    /* renamed from: lambda$sendData$57$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4140lambda$sendData$57$orgtelegramuiPaymentFormActivity(TLRPC.TL_error error, TLRPC.TL_payments_sendPaymentForm req) {
        AlertsCreator.processError(this.currentAccount, error, this, req, new Object[0]);
        setDonePressed(false);
        showEditDoneProgress(false, false);
        this.paymentStatusSent = true;
        PaymentFormCallback paymentFormCallback = this.paymentFormCallback;
        if (paymentFormCallback != null) {
            paymentFormCallback.onInvoiceStatusChanged(InvoiceStatus.FAILED);
        }
    }

    private void shakeField(int field) {
        shakeView(this.inputFields[field]);
    }

    private void shakeView(View view) {
        Vibrator v = (Vibrator) getParentActivity().getSystemService("vibrator");
        if (v != null) {
            v.vibrate(200L);
        }
        AndroidUtilities.shakeView(view, 2.0f, 0);
    }

    public void setDonePressed(boolean value) {
        this.donePressed = value;
        this.swipeBackEnabled = !value;
        if (this.actionBar != null) {
            this.actionBar.getBackButton().setEnabled(!this.donePressed);
        }
        TextDetailSettingsCell[] textDetailSettingsCellArr = this.detailSettingsCell;
        if (textDetailSettingsCellArr[0] != null) {
            textDetailSettingsCellArr[0].setEnabled(!this.donePressed);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isSwipeBackEnabled(MotionEvent event) {
        return this.swipeBackEnabled;
    }

    public void checkPassword() {
        if (UserConfig.getInstance(this.currentAccount).tmpPassword != null && UserConfig.getInstance(this.currentAccount).tmpPassword.valid_until < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 60) {
            UserConfig.getInstance(this.currentAccount).tmpPassword = null;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
        }
        if (UserConfig.getInstance(this.currentAccount).tmpPassword != null) {
            sendData();
        } else if (this.inputFields[1].length() == 0) {
            Vibrator v = (Vibrator) ApplicationLoader.applicationContext.getSystemService("vibrator");
            if (v != null) {
                v.vibrate(200L);
            }
            AndroidUtilities.shakeView(this.inputFields[1], 2.0f, 0);
        } else {
            final String password = this.inputFields[1].getText().toString();
            showEditDoneProgress(true, true);
            setDonePressed(true);
            final TLRPC.TL_account_getPassword req = new TLRPC.TL_account_getPassword();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda49
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    PaymentFormActivity.this.m4101lambda$checkPassword$63$orgtelegramuiPaymentFormActivity(password, req, tLObject, tL_error);
                }
            }, 2);
        }
    }

    /* renamed from: lambda$checkPassword$63$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4101lambda$checkPassword$63$orgtelegramuiPaymentFormActivity(final String password, final TLRPC.TL_account_getPassword req, final TLObject response, final TLRPC.TL_error error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda39
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.m4100lambda$checkPassword$62$orgtelegramuiPaymentFormActivity(error, response, password, req);
            }
        });
    }

    /* renamed from: lambda$checkPassword$62$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4100lambda$checkPassword$62$orgtelegramuiPaymentFormActivity(TLRPC.TL_error error, TLObject response, String password, TLRPC.TL_account_getPassword req) {
        if (error == null) {
            final TLRPC.TL_account_password currentPassword = (TLRPC.TL_account_password) response;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(currentPassword, false)) {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
                return;
            } else if (!currentPassword.has_password) {
                this.passwordOk = false;
                goToNextStep();
                return;
            } else {
                final byte[] passwordBytes = AndroidUtilities.getStringBytes(password);
                Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda34
                    @Override // java.lang.Runnable
                    public final void run() {
                        PaymentFormActivity.this.m4099lambda$checkPassword$61$orgtelegramuiPaymentFormActivity(currentPassword, passwordBytes);
                    }
                });
                return;
            }
        }
        AlertsCreator.processError(this.currentAccount, error, this, req, new Object[0]);
        showEditDoneProgress(true, false);
        setDonePressed(false);
    }

    /* renamed from: lambda$checkPassword$61$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4099lambda$checkPassword$61$orgtelegramuiPaymentFormActivity(TLRPC.TL_account_password currentPassword, byte[] passwordBytes) {
        byte[] x_bytes;
        if (currentPassword.current_algo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow algo = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) currentPassword.current_algo;
            x_bytes = SRPHelper.getX(passwordBytes, algo);
        } else {
            x_bytes = null;
        }
        final TLRPC.TL_account_getTmpPassword req1 = new TLRPC.TL_account_getTmpPassword();
        req1.period = 1800;
        RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda51
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                PaymentFormActivity.this.m4098lambda$checkPassword$60$orgtelegramuiPaymentFormActivity(req1, tLObject, tL_error);
            }
        };
        if (currentPassword.current_algo instanceof TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow algo2 = (TLRPC.TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) currentPassword.current_algo;
            req1.password = SRPHelper.startCheck(x_bytes, currentPassword.srp_id, currentPassword.srp_B, algo2);
            if (req1.password == null) {
                TLRPC.TL_error error2 = new TLRPC.TL_error();
                error2.text = "ALGO_INVALID";
                requestDelegate.run(null, error2);
                return;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req1, requestDelegate, 10);
            return;
        }
        TLRPC.TL_error error22 = new TLRPC.TL_error();
        error22.text = "PASSWORD_HASH_INVALID";
        requestDelegate.run(null, error22);
    }

    /* renamed from: lambda$checkPassword$60$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4098lambda$checkPassword$60$orgtelegramuiPaymentFormActivity(final TLRPC.TL_account_getTmpPassword req1, final TLObject response1, final TLRPC.TL_error error1) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.PaymentFormActivity$$ExternalSyntheticLambda33
            @Override // java.lang.Runnable
            public final void run() {
                PaymentFormActivity.this.m4097lambda$checkPassword$59$orgtelegramuiPaymentFormActivity(response1, error1, req1);
            }
        });
    }

    /* renamed from: lambda$checkPassword$59$org-telegram-ui-PaymentFormActivity */
    public /* synthetic */ void m4097lambda$checkPassword$59$orgtelegramuiPaymentFormActivity(TLObject response1, TLRPC.TL_error error1, TLRPC.TL_account_getTmpPassword req1) {
        showEditDoneProgress(true, false);
        setDonePressed(false);
        if (response1 != null) {
            this.passwordOk = true;
            UserConfig.getInstance(this.currentAccount).tmpPassword = (TLRPC.TL_account_tmpPassword) response1;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            goToNextStep();
        } else if (error1.text.equals("PASSWORD_HASH_INVALID")) {
            Vibrator v = (Vibrator) ApplicationLoader.applicationContext.getSystemService("vibrator");
            if (v != null) {
                v.vibrate(200L);
            }
            AndroidUtilities.shakeView(this.inputFields[1], 2.0f, 0);
            this.inputFields[1].setText("");
        } else {
            AlertsCreator.processError(this.currentAccount, error1, this, req1, new Object[0]);
        }
    }

    public void showEditDoneProgress(boolean animateDoneItem, final boolean show) {
        AnimatorSet animatorSet = this.doneItemAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (animateDoneItem && this.doneItem != null) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.doneItemAnimation = animatorSet2;
            if (show) {
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
                public void onAnimationEnd(Animator animation) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animation)) {
                        if (!show) {
                            PaymentFormActivity.this.progressView.setVisibility(4);
                        } else {
                            PaymentFormActivity.this.doneItem.getContentView().setVisibility(4);
                        }
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animation) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animation)) {
                        PaymentFormActivity.this.doneItemAnimation = null;
                    }
                }
            });
            this.doneItemAnimation.setDuration(150L);
            this.doneItemAnimation.start();
        } else if (this.payTextView != null) {
            this.doneItemAnimation = new AnimatorSet();
            if (show) {
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
                public void onAnimationEnd(Animator animation) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animation)) {
                        if (!show) {
                            PaymentFormActivity.this.progressViewButton.setVisibility(4);
                        } else {
                            PaymentFormActivity.this.payTextView.setVisibility(4);
                        }
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animation) {
                    if (PaymentFormActivity.this.doneItemAnimation != null && PaymentFormActivity.this.doneItemAnimation.equals(animation)) {
                        PaymentFormActivity.this.doneItemAnimation = null;
                    }
                }
            });
            this.doneItemAnimation.setDuration(150L);
            this.doneItemAnimation.start();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean presentFragment(BaseFragment fragment) {
        onPresentFragment(fragment);
        return super.presentFragment(fragment);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean presentFragment(BaseFragment fragment, boolean removeLast) {
        onPresentFragment(fragment);
        return super.presentFragment(fragment, removeLast);
    }

    private void onPresentFragment(BaseFragment fragment) {
        AndroidUtilities.hideKeyboard(this.fragmentView);
        if (fragment instanceof PaymentFormActivity) {
            ((PaymentFormActivity) fragment).paymentFormCallback = this.paymentFormCallback;
            ((PaymentFormActivity) fragment).resourcesProvider = this.resourcesProvider;
            ((PaymentFormActivity) fragment).needPayAfterTransition = this.needPayAfterTransition;
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
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.scrollView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder));
        arrayList.add(new ThemeDescription(this.linearLayout2, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.progressView, 0, null, null, null, null, Theme.key_contextProgressInner2));
        arrayList.add(new ThemeDescription(this.progressView, 0, null, null, null, null, Theme.key_contextProgressOuter2));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, Theme.key_contextProgressInner2));
        arrayList.add(new ThemeDescription(this.progressViewButton, 0, null, null, null, null, Theme.key_contextProgressOuter2));
        if (this.inputFields != null) {
            for (int a = 0; a < this.inputFields.length; a++) {
                arrayList.add(new ThemeDescription((View) this.inputFields[a].getParent(), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(this.inputFields[a], ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(this.inputFields[a], ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
            }
        } else {
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(null, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        }
        if (this.radioCells != null) {
            for (int a2 = 0; a2 < this.radioCells.length; a2++) {
                arrayList.add(new ThemeDescription(this.radioCells[a2], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
                arrayList.add(new ThemeDescription(this.radioCells[a2], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
                arrayList.add(new ThemeDescription(this.radioCells[a2], 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
                arrayList.add(new ThemeDescription(this.radioCells[a2], ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackground));
                arrayList.add(new ThemeDescription(this.radioCells[a2], ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackgroundChecked));
            }
        } else {
            arrayList.add(new ThemeDescription((View) null, 0, new Class[]{RadioCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription((View) null, ThemeDescription.FLAG_CHECKBOX, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackground));
            arrayList.add(new ThemeDescription((View) null, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[]{RadioCell.class}, new String[]{"radioButton"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_radioBackgroundChecked));
        }
        for (int a3 = 0; a3 < this.headerCell.length; a3++) {
            arrayList.add(new ThemeDescription(this.headerCell[a3], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(this.headerCell[a3], 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlueHeader));
        }
        for (int a4 = 0; a4 < this.sectionCell.length; a4++) {
            arrayList.add(new ThemeDescription(this.sectionCell[a4], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        }
        for (int a5 = 0; a5 < this.bottomCell.length; a5++) {
            arrayList.add(new ThemeDescription(this.bottomCell[a5], ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
            arrayList.add(new ThemeDescription(this.bottomCell[a5], 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
            arrayList.add(new ThemeDescription(this.bottomCell[a5], ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteLinkText));
        }
        for (int a6 = 0; a6 < this.dividers.size(); a6++) {
            arrayList.add(new ThemeDescription(this.dividers.get(a6), ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
        }
        arrayList.add(new ThemeDescription(this.codeFieldCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.codeFieldCell, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.codeFieldCell, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteHintText));
        arrayList.add(new ThemeDescription(this.textView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrack));
        arrayList.add(new ThemeDescription(this.checkCell1, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_switchTrackChecked));
        arrayList.add(new ThemeDescription(this.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.checkCell1, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        for (int a7 = 0; a7 < this.settingsCell.length; a7++) {
            arrayList.add(new ThemeDescription(this.settingsCell[a7], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(this.settingsCell[a7], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
            arrayList.add(new ThemeDescription(this.settingsCell[a7], 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        }
        arrayList.add(new ThemeDescription(this.payTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText6));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextPriceCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.linearLayout2, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextPriceCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.detailSettingsCell[0], ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        for (int a8 = 1; a8 < this.detailSettingsCell.length; a8++) {
            arrayList.add(new ThemeDescription(this.detailSettingsCell[a8], ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
            arrayList.add(new ThemeDescription(this.detailSettingsCell[a8], 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
            arrayList.add(new ThemeDescription(this.detailSettingsCell[a8], 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        }
        arrayList.add(new ThemeDescription(this.paymentInfoCell, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"nameTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"detailTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteBlackText));
        arrayList.add(new ThemeDescription(this.paymentInfoCell, 0, new Class[]{PaymentInfoCell.class}, new String[]{"detailExTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText2));
        arrayList.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_windowBackgroundWhite));
        arrayList.add(new ThemeDescription(this.bottomLayout, ThemeDescription.FLAG_SELECTORWHITE, null, null, null, null, Theme.key_listSelector));
        return arrayList;
    }

    /* loaded from: classes4.dex */
    public class BottomFrameLayout extends FrameLayout {
        Paint paint = new Paint(1);
        float progress;
        SpringAnimation springAnimation;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public BottomFrameLayout(Context context, TLRPC.TL_payments_paymentForm paymentForm) {
            super(context);
            PaymentFormActivity.this = r3;
            this.progress = (!paymentForm.invoice.recurring || r3.isAcceptTermsChecked) ? 1.0f : 0.0f;
            setWillNotDraw(false);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(PaymentFormActivity.this.getThemedColor(Theme.key_switchTrackBlue));
            this.paint.setColor(PaymentFormActivity.this.getThemedColor(Theme.key_contacts_inviteBackground));
            canvas.drawCircle(LocaleController.isRTL ? getWidth() - AndroidUtilities.dp(28.0f) : AndroidUtilities.dp(28.0f), -AndroidUtilities.dp(28.0f), Math.max(getWidth(), getHeight()) * this.progress, this.paint);
        }

        public void setChecked(boolean checked) {
            SpringAnimation springAnimation = this.springAnimation;
            if (springAnimation != null) {
                springAnimation.cancel();
            }
            float to = checked ? 1.0f : 0.0f;
            if (this.progress == to) {
                return;
            }
            SpringAnimation spring = new SpringAnimation(new FloatValueHolder(this.progress * 100.0f)).setSpring(new SpringForce(100.0f * to).setStiffness(checked ? 500.0f : 650.0f).setDampingRatio(1.0f));
            this.springAnimation = spring;
            spring.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.PaymentFormActivity$BottomFrameLayout$$ExternalSyntheticLambda1
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
                public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                    PaymentFormActivity.BottomFrameLayout.this.m4158x718bb2b5(dynamicAnimation, f, f2);
                }
            });
            this.springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.PaymentFormActivity$BottomFrameLayout$$ExternalSyntheticLambda0
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                    PaymentFormActivity.BottomFrameLayout.this.m4159x9ae007f6(dynamicAnimation, z, f, f2);
                }
            });
            this.springAnimation.start();
        }

        /* renamed from: lambda$setChecked$0$org-telegram-ui-PaymentFormActivity$BottomFrameLayout */
        public /* synthetic */ void m4158x718bb2b5(DynamicAnimation animation, float value, float velocity) {
            this.progress = value / 100.0f;
            if (PaymentFormActivity.this.payTextView != null) {
                PaymentFormActivity.this.payTextView.setAlpha((this.progress * 0.2f) + 0.8f);
            }
            invalidate();
        }

        /* renamed from: lambda$setChecked$1$org-telegram-ui-PaymentFormActivity$BottomFrameLayout */
        public /* synthetic */ void m4159x9ae007f6(DynamicAnimation animation, boolean canceled1, float value, float velocity) {
            if (animation == this.springAnimation) {
                this.springAnimation = null;
            }
        }
    }
}
