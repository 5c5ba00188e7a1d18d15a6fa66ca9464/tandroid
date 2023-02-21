package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.ReplacementSpan;
import android.util.Base64;
import android.util.Property;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.dynamicanimation.animation.DynamicAnimation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi$AttestationResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import j$.util.Comparator$-CC;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.AuthTokensHelper;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.PushListenerController;
import org.telegram.messenger.R;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$PasswordKdfAlgo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_account_changePhone;
import org.telegram.tgnet.TLRPC$TL_account_confirmPhone;
import org.telegram.tgnet.TLRPC$TL_account_deleteAccount;
import org.telegram.tgnet.TLRPC$TL_account_emailVerified;
import org.telegram.tgnet.TLRPC$TL_account_emailVerifiedLogin;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_passwordInputSettings;
import org.telegram.tgnet.TLRPC$TL_account_sendChangePhoneCode;
import org.telegram.tgnet.TLRPC$TL_account_sendVerifyEmailCode;
import org.telegram.tgnet.TLRPC$TL_account_sentEmailCode;
import org.telegram.tgnet.TLRPC$TL_account_verifyEmail;
import org.telegram.tgnet.TLRPC$TL_auth_authorization;
import org.telegram.tgnet.TLRPC$TL_auth_authorizationSignUpRequired;
import org.telegram.tgnet.TLRPC$TL_auth_cancelCode;
import org.telegram.tgnet.TLRPC$TL_auth_checkPassword;
import org.telegram.tgnet.TLRPC$TL_auth_checkRecoveryPassword;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeCall;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeFlashCall;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeFragmentSms;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeMissedCall;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeSms;
import org.telegram.tgnet.TLRPC$TL_auth_loggedOut;
import org.telegram.tgnet.TLRPC$TL_auth_passwordRecovery;
import org.telegram.tgnet.TLRPC$TL_auth_recoverPassword;
import org.telegram.tgnet.TLRPC$TL_auth_requestFirebaseSms;
import org.telegram.tgnet.TLRPC$TL_auth_requestPasswordRecovery;
import org.telegram.tgnet.TLRPC$TL_auth_resendCode;
import org.telegram.tgnet.TLRPC$TL_auth_sendCode;
import org.telegram.tgnet.TLRPC$TL_auth_sentCode;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeSuccess;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeApp;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeCall;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeEmailCode;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeFirebaseSms;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeFlashCall;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeFragmentSms;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeMissedCall;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeSetUpEmailRequired;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeSms;
import org.telegram.tgnet.TLRPC$TL_auth_signIn;
import org.telegram.tgnet.TLRPC$TL_auth_signUp;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_codeSettings;
import org.telegram.tgnet.TLRPC$TL_emailVerificationCode;
import org.telegram.tgnet.TLRPC$TL_emailVerificationGoogle;
import org.telegram.tgnet.TLRPC$TL_emailVerifyPurposeLoginChange;
import org.telegram.tgnet.TLRPC$TL_emailVerifyPurposeLoginSetup;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_help_countriesList;
import org.telegram.tgnet.TLRPC$TL_help_country;
import org.telegram.tgnet.TLRPC$TL_help_countryCode;
import org.telegram.tgnet.TLRPC$TL_help_getCountriesList;
import org.telegram.tgnet.TLRPC$TL_help_termsOfService;
import org.telegram.tgnet.TLRPC$TL_inputCheckPasswordSRP;
import org.telegram.tgnet.TLRPC$TL_nearestDc;
import org.telegram.tgnet.TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$VideoSize;
import org.telegram.tgnet.TLRPC$account_Password;
import org.telegram.tgnet.TLRPC$auth_Authorization;
import org.telegram.tgnet.TLRPC$auth_CodeType;
import org.telegram.tgnet.TLRPC$auth_SentCode;
import org.telegram.tgnet.TLRPC$auth_SentCodeType;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedPhoneNumberEditText;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.CustomPhoneKeyboardView;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.LoadingDrawable;
import org.telegram.ui.Components.LoginOrView;
import org.telegram.ui.Components.OutlineTextContainerView;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.SimpleThemeDescription;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.SlideView;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.TextViewSwitcher;
import org.telegram.ui.Components.TransformableLoginButtonView;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.VerticalPositionAutoAnimator;
import org.telegram.ui.Components.spoilers.SpoilersTextView;
import org.telegram.ui.CountrySelectActivity;
import org.telegram.ui.LoginActivity;
@SuppressLint({"HardwareIds"})
/* loaded from: classes3.dex */
public class LoginActivity extends BaseFragment {
    private static final int SHOW_DELAY;
    private int activityMode;
    private Runnable animationFinishCallback;
    private ImageView backButtonView;
    private AlertDialog cancelDeleteProgressDialog;
    private TLRPC$TL_auth_sentCode cancelDeletionCode;
    private Bundle cancelDeletionParams;
    private String cancelDeletionPhone;
    private boolean checkPermissions;
    private boolean checkShowPermissions;
    private int currentDoneType;
    private TLRPC$TL_help_termsOfService currentTermsOfService;
    private int currentViewNum;
    private boolean customKeyboardWasVisible;
    private boolean[] doneButtonVisible;
    private AnimatorSet doneItemAnimation;
    private boolean[] doneProgressVisible;
    private Runnable[] editDoneCallback;
    private Runnable emailChangeFinishCallback;
    private VerticalPositionAutoAnimator floatingAutoAnimator;
    private FrameLayout floatingButtonContainer;
    private TransformableLoginButtonView floatingButtonIcon;
    private RadialProgressView floatingProgressView;
    private boolean forceDisableSafetyNet;
    private View introView;
    private boolean isAnimatingIntro;
    private boolean isRequestingFirebaseSms;
    private ValueAnimator keyboardAnimator;
    private Runnable keyboardHideCallback;
    private LinearLayout keyboardLinearLayout;
    private CustomPhoneKeyboardView keyboardView;
    private boolean needRequestPermissions;
    private boolean newAccount;
    private Dialog permissionsDialog;
    private ArrayList<String> permissionsItems;
    private Dialog permissionsShowDialog;
    private ArrayList<String> permissionsShowItems;
    private PhoneNumberConfirmView phoneNumberConfirmView;
    private boolean[] postedEditDoneCallback;
    private int progressRequestId;
    private RadialProgressView radialProgressView;
    private boolean restoringState;
    private AnimatorSet[] showDoneAnimation;
    private SizeNotifierFrameLayout sizeNotifierFrameLayout;
    private FrameLayout slideViewsContainer;
    private TextView startMessagingButton;
    private boolean syncContacts;
    private boolean testBackend;
    private SlideView[] views;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class ProgressView extends View {
    }

    static {
        SHOW_DELAY = SharedConfig.getDevicePerformanceClass() <= 1 ? ImageReceiver.DEFAULT_CROSSFADE_DURATION : 100;
    }

    public LoginActivity() {
        this.views = new SlideView[16];
        this.permissionsItems = new ArrayList<>();
        this.permissionsShowItems = new ArrayList<>();
        this.checkPermissions = true;
        this.checkShowPermissions = true;
        this.syncContacts = true;
        this.testBackend = false;
        this.activityMode = 0;
        this.showDoneAnimation = new AnimatorSet[2];
        this.doneButtonVisible = new boolean[]{true, false};
        this.customKeyboardWasVisible = false;
        this.doneProgressVisible = new boolean[2];
        this.editDoneCallback = new Runnable[2];
        this.postedEditDoneCallback = new boolean[2];
    }

    public LoginActivity(int i) {
        this.views = new SlideView[16];
        this.permissionsItems = new ArrayList<>();
        this.permissionsShowItems = new ArrayList<>();
        this.checkPermissions = true;
        this.checkShowPermissions = true;
        this.syncContacts = true;
        this.testBackend = false;
        this.activityMode = 0;
        this.showDoneAnimation = new AnimatorSet[2];
        this.doneButtonVisible = new boolean[]{true, false};
        this.customKeyboardWasVisible = false;
        this.doneProgressVisible = new boolean[2];
        this.editDoneCallback = new Runnable[2];
        this.postedEditDoneCallback = new boolean[2];
        this.currentAccount = i;
        this.newAccount = true;
    }

    public LoginActivity changeEmail(Runnable runnable) {
        this.activityMode = 3;
        this.currentViewNum = 12;
        this.emailChangeFinishCallback = runnable;
        return this;
    }

    public LoginActivity cancelAccountDeletion(String str, Bundle bundle, TLRPC$TL_auth_sentCode tLRPC$TL_auth_sentCode) {
        this.cancelDeletionPhone = str;
        this.cancelDeletionParams = bundle;
        this.cancelDeletionCode = tLRPC$TL_auth_sentCode;
        this.activityMode = 1;
        return this;
    }

    public LoginActivity changePhoneNumber() {
        this.activityMode = 2;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInCancelAccountDeletionMode() {
        return this.activityMode == 1;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        Runnable[] runnableArr;
        super.onFragmentDestroy();
        int i = 0;
        while (true) {
            SlideView[] slideViewArr = this.views;
            if (i >= slideViewArr.length) {
                break;
            }
            if (slideViewArr[i] != null) {
                slideViewArr[i].onDestroyActivity();
            }
            i++;
        }
        AlertDialog alertDialog = this.cancelDeleteProgressDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.cancelDeleteProgressDialog = null;
        }
        for (Runnable runnable : this.editDoneCallback) {
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:118:0x0421, code lost:
        if (r2 != 4) goto L94;
     */
    /* JADX WARN: Removed duplicated region for block: B:115:0x041c A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x042a  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0453  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0448 A[EDGE_INSN: B:135:0x0448->B:127:0x0448 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x020b  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x020d  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x027f  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x028e  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0293  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0299  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x029e  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0365  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0368  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x036f  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x03ae  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x03b6  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x03d1  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        Bundle bundle;
        int i;
        Bundle bundle2;
        int i2;
        int i3;
        SlideView[] slideViewArr;
        Bundle bundle3;
        boolean z;
        this.actionBar.setAddToContainer(false);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.LoginActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i4) {
                if (i4 == 1) {
                    LoginActivity.this.onDoneButtonPressed();
                } else if (i4 == -1 && LoginActivity.this.onBackPressed()) {
                    LoginActivity.this.finishFragment();
                }
            }
        });
        this.currentDoneType = 0;
        boolean[] zArr = this.doneButtonVisible;
        zArr[0] = true;
        zArr[1] = false;
        SizeNotifierFrameLayout sizeNotifierFrameLayout = new SizeNotifierFrameLayout(context) { // from class: org.telegram.ui.LoginActivity.2
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i4, int i5) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) LoginActivity.this.floatingButtonContainer.getLayoutParams();
                int dp = LoginActivity.this.isCustomKeyboardVisible() ? AndroidUtilities.dp(230.0f) : 0;
                if (LoginActivity.this.isCustomKeyboardVisible() && measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
                    dp -= measureKeyboardHeight();
                }
                if (Bulletin.getVisibleBulletin() != null && Bulletin.getVisibleBulletin().isShowing()) {
                    super.onMeasure(i4, i5);
                    marginLayoutParams.bottomMargin = ((AndroidUtilities.dp(16.0f) + Bulletin.getVisibleBulletin().getLayout().getMeasuredHeight()) - AndroidUtilities.dp(10.0f)) + dp;
                } else {
                    marginLayoutParams.bottomMargin = AndroidUtilities.dp(16.0f) + dp;
                }
                int i6 = AndroidUtilities.isTablet() ? 0 : AndroidUtilities.statusBarHeight;
                ((ViewGroup.MarginLayoutParams) LoginActivity.this.backButtonView.getLayoutParams()).topMargin = AndroidUtilities.dp(16.0f) + i6;
                ((ViewGroup.MarginLayoutParams) LoginActivity.this.radialProgressView.getLayoutParams()).topMargin = AndroidUtilities.dp(16.0f) + i6;
                if (measureKeyboardHeight() > AndroidUtilities.dp(20.0f) && LoginActivity.this.keyboardView.getVisibility() != 8 && !LoginActivity.this.isCustomKeyboardForceDisabled() && !LoginActivity.this.customKeyboardWasVisible) {
                    if (LoginActivity.this.keyboardAnimator != null) {
                        LoginActivity.this.keyboardAnimator.cancel();
                    }
                    LoginActivity.this.keyboardView.setVisibility(8);
                }
                super.onMeasure(i4, i5);
            }
        };
        this.sizeNotifierFrameLayout = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setDelegate(new SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda30
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate
            public final void onSizeChanged(int i4, boolean z2) {
                LoginActivity.this.lambda$createView$0(i4, z2);
            }
        });
        this.fragmentView = this.sizeNotifierFrameLayout;
        ScrollView scrollView = new ScrollView(context) { // from class: org.telegram.ui.LoginActivity.3
            @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
            public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z2) {
                if (LoginActivity.this.currentViewNum == 1 || LoginActivity.this.currentViewNum == 2 || LoginActivity.this.currentViewNum == 4) {
                    rect.bottom += AndroidUtilities.dp(40.0f);
                }
                return super.requestChildRectangleOnScreen(view, rect, z2);
            }
        };
        scrollView.setFillViewport(true);
        this.sizeNotifierFrameLayout.addView(scrollView, LayoutHelper.createFrame(-1, -1.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.keyboardLinearLayout = linearLayout;
        linearLayout.setOrientation(1);
        scrollView.addView(this.keyboardLinearLayout, LayoutHelper.createScroll(-1, -2, 51));
        Space space = new Space(context);
        space.setMinimumHeight(AndroidUtilities.isTablet() ? 0 : AndroidUtilities.statusBarHeight);
        this.keyboardLinearLayout.addView(space);
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.LoginActivity.4
            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z2, int i4, int i5, int i6, int i7) {
                SlideView[] slideViewArr2;
                super.onLayout(z2, i4, i5, i6, i7);
                for (SlideView slideView : LoginActivity.this.views) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) slideView.getLayoutParams();
                    int height = getHeight() + AndroidUtilities.dp(16.0f);
                    if (!slideView.hasCustomKeyboard() && LoginActivity.this.keyboardView.getVisibility() == 0) {
                        height += AndroidUtilities.dp(230.0f);
                    }
                    slideView.layout(marginLayoutParams.leftMargin, marginLayoutParams.topMargin, getWidth() - marginLayoutParams.rightMargin, height);
                }
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i4, int i5) {
                SlideView[] slideViewArr2;
                super.onMeasure(i4, i5);
                int measuredWidth = getMeasuredWidth();
                int measuredHeight = getMeasuredHeight();
                for (SlideView slideView : LoginActivity.this.views) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) slideView.getLayoutParams();
                    int dp = (measuredHeight - marginLayoutParams.topMargin) + AndroidUtilities.dp(16.0f);
                    if (!slideView.hasCustomKeyboard() && LoginActivity.this.keyboardView.getVisibility() == 0) {
                        dp += AndroidUtilities.dp(230.0f);
                    }
                    slideView.measure(View.MeasureSpec.makeMeasureSpec((measuredWidth - marginLayoutParams.rightMargin) - marginLayoutParams.leftMargin, 1073741824), View.MeasureSpec.makeMeasureSpec(dp, 1073741824));
                }
            }
        };
        this.slideViewsContainer = frameLayout;
        this.keyboardLinearLayout.addView(frameLayout, LayoutHelper.createLinear(-1, 0, 1.0f));
        CustomPhoneKeyboardView customPhoneKeyboardView = new CustomPhoneKeyboardView(context);
        this.keyboardView = customPhoneKeyboardView;
        this.keyboardLinearLayout.addView(customPhoneKeyboardView, LayoutHelper.createLinear(-1, 230));
        this.views[0] = new PhoneView(context);
        this.views[1] = new LoginActivitySmsView(context, 1);
        this.views[2] = new LoginActivitySmsView(context, 2);
        this.views[3] = new LoginActivitySmsView(context, 3);
        this.views[4] = new LoginActivitySmsView(context, 4);
        this.views[5] = new LoginActivityRegisterView(context);
        this.views[6] = new LoginActivityPasswordView(context);
        this.views[7] = new LoginActivityRecoverView(context);
        this.views[8] = new LoginActivityResetWaitView(context);
        this.views[9] = new LoginActivityNewPasswordView(context, 0);
        this.views[10] = new LoginActivityNewPasswordView(context, 1);
        this.views[11] = new LoginActivitySmsView(context, 11);
        this.views[12] = new LoginActivitySetupEmail(context);
        this.views[13] = new LoginActivityEmailCodeView(context, true);
        this.views[14] = new LoginActivityEmailCodeView(context, false);
        this.views[15] = new LoginActivitySmsView(context, 15);
        int i4 = 0;
        while (true) {
            SlideView[] slideViewArr2 = this.views;
            if (i4 >= slideViewArr2.length) {
                break;
            }
            slideViewArr2[i4].setVisibility(i4 == 0 ? 0 : 8);
            this.slideViewsContainer.addView(this.views[i4], LayoutHelper.createFrame(-1, -1.0f, 17, AndroidUtilities.isTablet() ? 26.0f : 18.0f, 30.0f, AndroidUtilities.isTablet() ? 26.0f : 18.0f, 0.0f));
            i4++;
        }
        Bundle loadCurrentState = this.activityMode == 0 ? loadCurrentState(this.newAccount) : null;
        if (loadCurrentState != null) {
            this.currentViewNum = loadCurrentState.getInt("currentViewNum", 0);
            this.syncContacts = loadCurrentState.getInt("syncContacts", 1) == 1;
            int i5 = this.currentViewNum;
            if (i5 >= 1 && i5 <= 4) {
                int i6 = loadCurrentState.getInt("open");
                if (i6 != 0 && Math.abs((System.currentTimeMillis() / 1000) - i6) >= 86400) {
                    this.currentViewNum = 0;
                    clearCurrentState();
                    bundle = null;
                }
            } else if (i5 == 6) {
                if (((LoginActivityPasswordView) this.views[6]).currentPassword == null) {
                    this.currentViewNum = 0;
                    clearCurrentState();
                    bundle = null;
                }
            } else if (i5 == 7 && ((LoginActivityRecoverView) this.views[7]).passwordString == null) {
                this.currentViewNum = 0;
                clearCurrentState();
                bundle = null;
            }
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.floatingButtonContainer = frameLayout2;
            frameLayout2.setVisibility(!this.doneButtonVisible[0] ? 0 : 8);
            i = Build.VERSION.SDK_INT;
            if (i < 21) {
                StateListAnimator stateListAnimator = new StateListAnimator();
                stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                bundle2 = bundle;
                stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                this.floatingButtonContainer.setStateListAnimator(stateListAnimator);
                this.floatingButtonContainer.setOutlineProvider(new ViewOutlineProvider(this) { // from class: org.telegram.ui.LoginActivity.5
                    @Override // android.view.ViewOutlineProvider
                    @SuppressLint({"NewApi"})
                    public void getOutline(View view, Outline outline) {
                        outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    }
                });
            } else {
                bundle2 = bundle;
            }
            this.floatingAutoAnimator = VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
            this.sizeNotifierFrameLayout.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i < 21 ? 56 : 60, i < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
            this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda10
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LoginActivity.this.lambda$createView$1(view);
                }
            });
            this.floatingAutoAnimator.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda12
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
                public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                    LoginActivity.this.lambda$createView$2(dynamicAnimation, f, f2);
                }
            });
            ImageView imageView = new ImageView(context);
            this.backButtonView = imageView;
            imageView.setImageResource(R.drawable.ic_ab_back);
            this.backButtonView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda11
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LoginActivity.this.lambda$createView$3(view);
                }
            });
            this.backButtonView.setContentDescription(LocaleController.getString(R.string.Back));
            int dp = AndroidUtilities.dp(4.0f);
            this.backButtonView.setPadding(dp, dp, dp, dp);
            this.sizeNotifierFrameLayout.addView(this.backButtonView, LayoutHelper.createFrame(32, 32.0f, 51, 16.0f, 16.0f, 0.0f, 0.0f));
            RadialProgressView radialProgressView = new RadialProgressView(context);
            this.radialProgressView = radialProgressView;
            radialProgressView.setSize(AndroidUtilities.dp(20.0f));
            this.radialProgressView.setAlpha(0.0f);
            this.radialProgressView.setScaleX(0.1f);
            this.radialProgressView.setScaleY(0.1f);
            this.sizeNotifierFrameLayout.addView(this.radialProgressView, LayoutHelper.createFrame(32, 32.0f, 53, 0.0f, 16.0f, 16.0f, 0.0f));
            TransformableLoginButtonView transformableLoginButtonView = new TransformableLoginButtonView(context);
            this.floatingButtonIcon = transformableLoginButtonView;
            transformableLoginButtonView.setTransformType(0);
            this.floatingButtonIcon.setProgress(1.0f);
            this.floatingButtonIcon.setDrawBackground(false);
            this.floatingButtonContainer.setContentDescription(LocaleController.getString("Done", R.string.Done));
            this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i < 21 ? 56 : 60, i < 21 ? 56.0f : 60.0f));
            RadialProgressView radialProgressView2 = new RadialProgressView(context);
            this.floatingProgressView = radialProgressView2;
            radialProgressView2.setSize(AndroidUtilities.dp(22.0f));
            this.floatingProgressView.setAlpha(0.0f);
            this.floatingProgressView.setScaleX(0.1f);
            this.floatingProgressView.setScaleY(0.1f);
            this.floatingProgressView.setVisibility(4);
            this.floatingButtonContainer.addView(this.floatingProgressView, LayoutHelper.createFrame(-1, -1.0f));
            i2 = 1;
            if (bundle2 != null) {
                this.restoringState = true;
            }
            i3 = 0;
            while (true) {
                slideViewArr = this.views;
                if (i3 < slideViewArr.length) {
                    break;
                }
                SlideView slideView = slideViewArr[i3];
                if (bundle2 != null) {
                    if (i3 >= i2 && i3 <= 4) {
                        if (i3 == this.currentViewNum) {
                            bundle3 = bundle2;
                            slideView.restoreStateParams(bundle3);
                        }
                    } else {
                        bundle3 = bundle2;
                        slideView.restoreStateParams(bundle3);
                    }
                    if (this.currentViewNum != i3) {
                        this.backButtonView.setVisibility((slideView.needBackButton() || this.newAccount || this.activityMode == 2) ? 0 : 8);
                        slideView.setVisibility(0);
                        slideView.onShow();
                        setCustomKeyboardVisible(slideView.hasCustomKeyboard(), false);
                        this.currentDoneType = 0;
                        if (i3 != 0 && i3 != 5) {
                            if (i3 != 6) {
                                if (i3 != 9) {
                                    if (i3 != 10 && i3 != 12) {
                                        z = false;
                                        showDoneButton(z, false);
                                        if (i3 == 1 && i3 != 2) {
                                            if (i3 != 3) {
                                            }
                                        }
                                        this.currentDoneType = 1;
                                    }
                                    z = true;
                                    showDoneButton(z, false);
                                    if (i3 == 1) {
                                    }
                                    this.currentDoneType = 1;
                                }
                                z = true;
                                showDoneButton(z, false);
                                if (i3 == 1) {
                                }
                                this.currentDoneType = 1;
                            }
                        }
                        z = true;
                        showDoneButton(z, false);
                        if (i3 == 1) {
                        }
                        this.currentDoneType = 1;
                    } else if (slideView.getVisibility() != 8) {
                        slideView.setVisibility(8);
                        slideView.onHide();
                    }
                    i3++;
                    bundle2 = bundle3;
                    i2 = 1;
                }
                bundle3 = bundle2;
                if (this.currentViewNum != i3) {
                }
                i3++;
                bundle2 = bundle3;
                i2 = 1;
            }
            this.restoringState = false;
            updateColors();
            if (isInCancelAccountDeletionMode()) {
                lambda$fillNextCodeParams$22(this.cancelDeletionParams, this.cancelDeletionCode, false);
            }
            return this.fragmentView;
        }
        bundle = loadCurrentState;
        FrameLayout frameLayout22 = new FrameLayout(context);
        this.floatingButtonContainer = frameLayout22;
        frameLayout22.setVisibility(!this.doneButtonVisible[0] ? 0 : 8);
        i = Build.VERSION.SDK_INT;
        if (i < 21) {
        }
        this.floatingAutoAnimator = VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
        this.sizeNotifierFrameLayout.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i < 21 ? 56 : 60, i < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
        this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LoginActivity.this.lambda$createView$1(view);
            }
        });
        this.floatingAutoAnimator.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda12
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
            public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                LoginActivity.this.lambda$createView$2(dynamicAnimation, f, f2);
            }
        });
        ImageView imageView2 = new ImageView(context);
        this.backButtonView = imageView2;
        imageView2.setImageResource(R.drawable.ic_ab_back);
        this.backButtonView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                LoginActivity.this.lambda$createView$3(view);
            }
        });
        this.backButtonView.setContentDescription(LocaleController.getString(R.string.Back));
        int dp2 = AndroidUtilities.dp(4.0f);
        this.backButtonView.setPadding(dp2, dp2, dp2, dp2);
        this.sizeNotifierFrameLayout.addView(this.backButtonView, LayoutHelper.createFrame(32, 32.0f, 51, 16.0f, 16.0f, 0.0f, 0.0f));
        RadialProgressView radialProgressView3 = new RadialProgressView(context);
        this.radialProgressView = radialProgressView3;
        radialProgressView3.setSize(AndroidUtilities.dp(20.0f));
        this.radialProgressView.setAlpha(0.0f);
        this.radialProgressView.setScaleX(0.1f);
        this.radialProgressView.setScaleY(0.1f);
        this.sizeNotifierFrameLayout.addView(this.radialProgressView, LayoutHelper.createFrame(32, 32.0f, 53, 0.0f, 16.0f, 16.0f, 0.0f));
        TransformableLoginButtonView transformableLoginButtonView2 = new TransformableLoginButtonView(context);
        this.floatingButtonIcon = transformableLoginButtonView2;
        transformableLoginButtonView2.setTransformType(0);
        this.floatingButtonIcon.setProgress(1.0f);
        this.floatingButtonIcon.setDrawBackground(false);
        this.floatingButtonContainer.setContentDescription(LocaleController.getString("Done", R.string.Done));
        this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i < 21 ? 56 : 60, i < 21 ? 56.0f : 60.0f));
        RadialProgressView radialProgressView22 = new RadialProgressView(context);
        this.floatingProgressView = radialProgressView22;
        radialProgressView22.setSize(AndroidUtilities.dp(22.0f));
        this.floatingProgressView.setAlpha(0.0f);
        this.floatingProgressView.setScaleX(0.1f);
        this.floatingProgressView.setScaleY(0.1f);
        this.floatingProgressView.setVisibility(4);
        this.floatingButtonContainer.addView(this.floatingProgressView, LayoutHelper.createFrame(-1, -1.0f));
        i2 = 1;
        if (bundle2 != null) {
        }
        i3 = 0;
        while (true) {
            slideViewArr = this.views;
            if (i3 < slideViewArr.length) {
            }
            i3++;
            bundle2 = bundle3;
            i2 = 1;
        }
        this.restoringState = false;
        updateColors();
        if (isInCancelAccountDeletionMode()) {
        }
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$0(int i, boolean z) {
        Runnable runnable;
        if (i > AndroidUtilities.dp(20.0f) && isCustomKeyboardVisible()) {
            AndroidUtilities.hideKeyboard(this.fragmentView);
        }
        if (i > AndroidUtilities.dp(20.0f) || (runnable = this.keyboardHideCallback) == null) {
            return;
        }
        runnable.run();
        this.keyboardHideCallback = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view) {
        onDoneButtonPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$2(DynamicAnimation dynamicAnimation, float f, float f2) {
        PhoneNumberConfirmView phoneNumberConfirmView = this.phoneNumberConfirmView;
        if (phoneNumberConfirmView != null) {
            phoneNumberConfirmView.updateFabPosition();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        if (onBackPressed()) {
            finishFragment();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCustomKeyboardForceDisabled() {
        Point point = AndroidUtilities.displaySize;
        return point.x > point.y || AndroidUtilities.isTablet() || AndroidUtilities.isAccessibilityTouchExplorationEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCustomKeyboardVisible() {
        return this.views[this.currentViewNum].hasCustomKeyboard() && !isCustomKeyboardForceDisabled();
    }

    private void setCustomKeyboardVisible(boolean z, boolean z2) {
        if (this.customKeyboardWasVisible == z && z2) {
            return;
        }
        this.customKeyboardWasVisible = z;
        if (isCustomKeyboardForceDisabled()) {
            z = false;
        }
        if (z) {
            AndroidUtilities.hideKeyboard(this.fragmentView);
            AndroidUtilities.requestAltFocusable(getParentActivity(), this.classGuid);
            if (z2) {
                ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(300L);
                this.keyboardAnimator = duration;
                duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.keyboardAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        LoginActivity.this.lambda$setCustomKeyboardVisible$4(valueAnimator);
                    }
                });
                this.keyboardAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LoginActivity.6
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        LoginActivity.this.keyboardView.setVisibility(0);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (LoginActivity.this.keyboardAnimator == animator) {
                            LoginActivity.this.keyboardAnimator = null;
                        }
                    }
                });
                this.keyboardAnimator.start();
                return;
            }
            this.keyboardView.setVisibility(0);
            return;
        }
        AndroidUtilities.removeAltFocusable(getParentActivity(), this.classGuid);
        if (z2) {
            ValueAnimator duration2 = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(300L);
            this.keyboardAnimator = duration2;
            duration2.setInterpolator(Easings.easeInOutQuad);
            this.keyboardAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LoginActivity.this.lambda$setCustomKeyboardVisible$5(valueAnimator);
                }
            });
            this.keyboardAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LoginActivity.7
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    LoginActivity.this.keyboardView.setVisibility(8);
                    if (LoginActivity.this.keyboardAnimator == animator) {
                        LoginActivity.this.keyboardAnimator = null;
                    }
                }
            });
            this.keyboardAnimator.start();
            return;
        }
        this.keyboardView.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCustomKeyboardVisible$4(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.keyboardView.setAlpha(floatValue);
        this.keyboardView.setTranslationY((1.0f - floatValue) * AndroidUtilities.dp(230.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCustomKeyboardVisible$5(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.keyboardView.setAlpha(floatValue);
        this.keyboardView.setTranslationY((1.0f - floatValue) * AndroidUtilities.dp(230.0f));
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        if (this.newAccount) {
            ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
        }
        AndroidUtilities.removeAltFocusable(getParentActivity(), this.classGuid);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        SlideView slideView;
        int i;
        super.onResume();
        if (this.newAccount) {
            ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
        }
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        View view = this.fragmentView;
        if (view != null) {
            view.requestLayout();
        }
        try {
            int i2 = this.currentViewNum;
            if (i2 >= 1 && i2 <= 4) {
                SlideView[] slideViewArr = this.views;
                if ((slideViewArr[i2] instanceof LoginActivitySmsView) && (i = ((LoginActivitySmsView) slideViewArr[i2]).openTime) != 0 && Math.abs((System.currentTimeMillis() / 1000) - i) >= 86400) {
                    this.views[this.currentViewNum].onBackPressed(true);
                    setPage(0, false, null, true);
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        int i3 = this.currentViewNum;
        if (i3 == 0 && !this.needRequestPermissions && (slideView = this.views[i3]) != null) {
            slideView.onShow();
        }
        if (isCustomKeyboardVisible()) {
            AndroidUtilities.hideKeyboard(this.fragmentView);
            AndroidUtilities.requestAltFocusable(getParentActivity(), this.classGuid);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        setCustomKeyboardVisible(this.views[this.currentViewNum].hasCustomKeyboard(), false);
        PhoneNumberConfirmView phoneNumberConfirmView = this.phoneNumberConfirmView;
        if (phoneNumberConfirmView != null) {
            phoneNumberConfirmView.dismiss();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        if (strArr.length == 0 || iArr.length == 0) {
            return;
        }
        boolean z = iArr[0] == 0;
        if (i == 6) {
            this.checkPermissions = false;
            int i2 = this.currentViewNum;
            if (i2 == 0) {
                ((PhoneView) this.views[i2]).confirmedNumber = true;
                this.views[this.currentViewNum].onNextPressed(null);
            }
        } else if (i == 7) {
            this.checkShowPermissions = false;
            int i3 = this.currentViewNum;
            if (i3 == 0) {
                ((PhoneView) this.views[i3]).fillNumber();
            }
        } else if (i == 20) {
            if (z) {
                ((LoginActivityRegisterView) this.views[5]).imageUpdater.openCamera();
            }
        } else if (i == 151 && z) {
            final LoginActivityRegisterView loginActivityRegisterView = (LoginActivityRegisterView) this.views[5];
            loginActivityRegisterView.post(new Runnable() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.lambda$onRequestPermissionsResultFragment$6(LoginActivity.LoginActivityRegisterView.this);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onRequestPermissionsResultFragment$6(LoginActivityRegisterView loginActivityRegisterView) {
        loginActivityRegisterView.imageUpdater.openGallery();
    }

    public static Bundle loadCurrentState(boolean z) {
        if (z) {
            return null;
        }
        try {
            Bundle bundle = new Bundle();
            for (Map.Entry<String, ?> entry : ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).getAll().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String[] split = key.split("_\\|_");
                if (split.length == 1) {
                    if (value instanceof String) {
                        bundle.putString(key, (String) value);
                    } else if (value instanceof Integer) {
                        bundle.putInt(key, ((Integer) value).intValue());
                    } else if (value instanceof Boolean) {
                        bundle.putBoolean(key, ((Boolean) value).booleanValue());
                    }
                } else if (split.length == 2) {
                    Bundle bundle2 = bundle.getBundle(split[0]);
                    if (bundle2 == null) {
                        bundle2 = new Bundle();
                        bundle.putBundle(split[0], bundle2);
                    }
                    if (value instanceof String) {
                        bundle2.putString(split[1], (String) value);
                    } else if (value instanceof Integer) {
                        bundle2.putInt(split[1], ((Integer) value).intValue());
                    } else if (value instanceof Boolean) {
                        bundle2.putBoolean(split[1], ((Boolean) value).booleanValue());
                    }
                }
            }
            return bundle;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    private void clearCurrentState() {
        SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).edit();
        edit.clear();
        edit.commit();
    }

    private void putBundleToEditor(Bundle bundle, SharedPreferences.Editor editor, String str) {
        for (String str2 : bundle.keySet()) {
            Object obj = bundle.get(str2);
            if (obj instanceof String) {
                if (str != null) {
                    editor.putString(str + "_|_" + str2, (String) obj);
                } else {
                    editor.putString(str2, (String) obj);
                }
            } else if (obj instanceof Integer) {
                if (str != null) {
                    editor.putInt(str + "_|_" + str2, ((Integer) obj).intValue());
                } else {
                    editor.putInt(str2, ((Integer) obj).intValue());
                }
            } else if (obj instanceof Boolean) {
                if (str != null) {
                    editor.putBoolean(str + "_|_" + str2, ((Boolean) obj).booleanValue());
                } else {
                    editor.putBoolean(str2, ((Boolean) obj).booleanValue());
                }
            } else if (obj instanceof Bundle) {
                putBundleToEditor((Bundle) obj, editor, str2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onDialogDismiss(Dialog dialog) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                if (dialog == this.permissionsDialog && !this.permissionsItems.isEmpty() && getParentActivity() != null) {
                    getParentActivity().requestPermissions((String[]) this.permissionsItems.toArray(new String[0]), 6);
                } else if (dialog != this.permissionsShowDialog || this.permissionsShowItems.isEmpty() || getParentActivity() == null) {
                } else {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda19
                        @Override // java.lang.Runnable
                        public final void run() {
                            LoginActivity.this.lambda$onDialogDismiss$7();
                        }
                    }, 200L);
                    getParentActivity().requestPermissions((String[]) this.permissionsShowItems.toArray(new String[0]), 7);
                }
            } catch (Exception unused) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDialogDismiss$7() {
        this.needRequestPermissions = false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        int i = this.currentViewNum;
        int i2 = 0;
        if (i != 0 && (this.activityMode != 3 || i != 12)) {
            if (i == 6) {
                this.views[i].onBackPressed(true);
                setPage(0, true, null, true);
            } else if (i == 7 || i == 8) {
                this.views[i].onBackPressed(true);
                setPage(6, true, null, true);
            } else if ((i >= 1 && i <= 4) || i == 11 || i == 15) {
                if (this.views[i].onBackPressed(false)) {
                    setPage(0, true, null, true);
                }
            } else if (i == 5) {
                ((LoginActivityRegisterView) this.views[i]).wrongNumber.callOnClick();
            } else if (i == 9) {
                this.views[i].onBackPressed(true);
                setPage(7, true, null, true);
            } else if (i == 10) {
                this.views[i].onBackPressed(true);
                setPage(9, true, null, true);
            } else if (i == 13) {
                this.views[i].onBackPressed(true);
                setPage(12, true, null, true);
            } else if (this.views[i].onBackPressed(true)) {
                setPage(0, true, null, true);
            }
            return false;
        }
        while (true) {
            SlideView[] slideViewArr = this.views;
            if (i2 < slideViewArr.length) {
                if (slideViewArr[i2] != null) {
                    slideViewArr[i2].onDestroyActivity();
                }
                i2++;
            } else {
                clearCurrentState();
                return true;
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onActivityResultFragment(int i, int i2, Intent intent) {
        LoginActivityRegisterView loginActivityRegisterView = (LoginActivityRegisterView) this.views[5];
        if (loginActivityRegisterView != null) {
            loginActivityRegisterView.imageUpdater.onActivityResult(i, i2, intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void needShowAlert(String str, String str2) {
        if (str2 == null || getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFieldError(final View view, boolean z) {
        view.performHapticFeedback(3, 2);
        AndroidUtilities.shakeViewSpring(view, 3.5f);
        if (z && (view instanceof OutlineTextContainerView)) {
            int i = R.id.timeout_callback;
            Runnable runnable = (Runnable) view.getTag(i);
            if (runnable != null) {
                view.removeCallbacks(runnable);
            }
            final OutlineTextContainerView outlineTextContainerView = (OutlineTextContainerView) view;
            AtomicReference atomicReference = new AtomicReference();
            final EditText attachedEditText = outlineTextContainerView.getAttachedEditText();
            final 8 r3 = new 8(this, attachedEditText, atomicReference);
            outlineTextContainerView.animateError(1.0f);
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.lambda$onFieldError$9(OutlineTextContainerView.this, view, attachedEditText, r3);
                }
            };
            atomicReference.set(runnable2);
            view.postDelayed(runnable2, 2000L);
            view.setTag(i, runnable2);
            if (attachedEditText != null) {
                attachedEditText.addTextChangedListener(r3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class 8 implements TextWatcher {
        final /* synthetic */ EditText val$editText;
        final /* synthetic */ AtomicReference val$timeoutCallbackRef;

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        8(LoginActivity loginActivity, EditText editText, AtomicReference atomicReference) {
            this.val$editText = editText;
            this.val$timeoutCallbackRef = atomicReference;
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            final EditText editText = this.val$editText;
            final AtomicReference atomicReference = this.val$timeoutCallbackRef;
            editText.post(new Runnable() { // from class: org.telegram.ui.LoginActivity$8$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.8.this.lambda$beforeTextChanged$0(editText, atomicReference);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$beforeTextChanged$0(EditText editText, AtomicReference atomicReference) {
            editText.removeTextChangedListener(this);
            editText.removeCallbacks((Runnable) atomicReference.get());
            ((Runnable) atomicReference.get()).run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$onFieldError$9(OutlineTextContainerView outlineTextContainerView, View view, final EditText editText, final TextWatcher textWatcher) {
        outlineTextContainerView.animateError(0.0f);
        view.setTag(R.id.timeout_callback, null);
        if (editText != null) {
            editText.post(new Runnable() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    editText.removeTextChangedListener(textWatcher);
                }
            });
        }
    }

    public static void needShowInvalidAlert(BaseFragment baseFragment, String str, boolean z) {
        needShowInvalidAlert(baseFragment, str, null, z);
    }

    public static void needShowInvalidAlert(final BaseFragment baseFragment, final String str, PhoneInputData phoneInputData, final boolean z) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
        if (z) {
            builder.setTitle(LocaleController.getString(R.string.RestorePasswordNoEmailTitle));
            builder.setMessage(LocaleController.getString("BannedPhoneNumber", R.string.BannedPhoneNumber));
        } else if (phoneInputData == null || phoneInputData.patterns == null || phoneInputData.patterns.isEmpty() || phoneInputData.country == null) {
            builder.setTitle(LocaleController.getString(R.string.RestorePasswordNoEmailTitle));
            builder.setMessage(LocaleController.getString(R.string.InvalidPhoneNumber));
        } else {
            int i = ConnectionsManager.DEFAULT_DATACENTER_ID;
            for (String str2 : phoneInputData.patterns) {
                int length = str2.replace(" ", "").length();
                if (length < i) {
                    i = length;
                }
            }
            if (PhoneFormat.stripExceptNumbers(str).length() - phoneInputData.country.code.length() < i) {
                builder.setTitle(LocaleController.getString(R.string.WrongNumberFormat));
                builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("ShortNumberInfo", R.string.ShortNumberInfo, phoneInputData.country.name, phoneInputData.phoneNumber)));
            } else {
                builder.setTitle(LocaleController.getString(R.string.RestorePasswordNoEmailTitle));
                builder.setMessage(LocaleController.getString(R.string.InvalidPhoneNumber));
            }
        }
        builder.setNeutralButton(LocaleController.getString("BotHelp", R.string.BotHelp), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda9
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                LoginActivity.lambda$needShowInvalidAlert$10(z, str, baseFragment, dialogInterface, i2);
            }
        });
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        baseFragment.showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$needShowInvalidAlert$10(boolean z, String str, BaseFragment baseFragment, DialogInterface dialogInterface, int i) {
        try {
            PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            String format = String.format(Locale.US, "%s (%d)", packageInfo.versionName, Integer.valueOf(packageInfo.versionCode));
            Intent intent = new Intent("android.intent.action.SENDTO");
            intent.setData(Uri.parse("mailto:"));
            String[] strArr = new String[1];
            strArr[0] = z ? "recover@telegram.org" : "login@stel.com";
            intent.putExtra("android.intent.extra.EMAIL", strArr);
            if (z) {
                intent.putExtra("android.intent.extra.SUBJECT", "Banned phone number: " + str);
                intent.putExtra("android.intent.extra.TEXT", "I'm trying to use my mobile phone number: " + str + "\nBut Telegram says it's banned. Please help.\n\nApp version: " + format + "\nOS version: SDK " + Build.VERSION.SDK_INT + "\nDevice Name: " + Build.MANUFACTURER + Build.MODEL + "\nLocale: " + Locale.getDefault());
            } else {
                intent.putExtra("android.intent.extra.SUBJECT", "Invalid phone number: " + str);
                intent.putExtra("android.intent.extra.TEXT", "I'm trying to use my mobile phone number: " + str + "\nBut Telegram says it's invalid. Please help.\n\nApp version: " + format + "\nOS version: SDK " + Build.VERSION.SDK_INT + "\nDevice Name: " + Build.MANUFACTURER + Build.MODEL + "\nLocale: " + Locale.getDefault());
            }
            baseFragment.getParentActivity().startActivity(Intent.createChooser(intent, "Send email..."));
        } catch (Exception unused) {
            AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.RestorePasswordNoEmailTitle));
            builder.setMessage(LocaleController.getString("NoMailInstalled", R.string.NoMailInstalled));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            baseFragment.showDialog(builder.create());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showDoneButton(final boolean z, boolean z2) {
        TimeInterpolator timeInterpolator;
        int i = this.currentDoneType;
        final boolean z3 = i == 0;
        if (this.doneButtonVisible[i] == z) {
            return;
        }
        AnimatorSet[] animatorSetArr = this.showDoneAnimation;
        if (animatorSetArr[i] != null) {
            if (z2) {
                animatorSetArr[i].removeAllListeners();
            }
            this.showDoneAnimation[this.currentDoneType].cancel();
        }
        boolean[] zArr = this.doneButtonVisible;
        int i2 = this.currentDoneType;
        zArr[i2] = z;
        if (!z2) {
            if (z) {
                if (z3) {
                    this.floatingButtonContainer.setVisibility(0);
                    this.floatingAutoAnimator.setOffsetY(0.0f);
                    return;
                }
                return;
            } else if (z3) {
                this.floatingButtonContainer.setVisibility(8);
                this.floatingAutoAnimator.setOffsetY(AndroidUtilities.dpf2(70.0f));
                return;
            } else {
                return;
            }
        }
        this.showDoneAnimation[i2] = new AnimatorSet();
        if (z) {
            if (z3) {
                if (this.floatingButtonContainer.getVisibility() != 0) {
                    this.floatingAutoAnimator.setOffsetY(AndroidUtilities.dpf2(70.0f));
                    this.floatingButtonContainer.setVisibility(0);
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.floatingAutoAnimator.getOffsetY(), 0.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        LoginActivity.this.lambda$showDoneButton$11(valueAnimator);
                    }
                });
                this.showDoneAnimation[this.currentDoneType].play(ofFloat);
            }
        } else if (z3) {
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.floatingAutoAnimator.getOffsetY(), AndroidUtilities.dpf2(70.0f));
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LoginActivity.this.lambda$showDoneButton$12(valueAnimator);
                }
            });
            this.showDoneAnimation[this.currentDoneType].play(ofFloat2);
        }
        this.showDoneAnimation[this.currentDoneType].addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LoginActivity.9
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (LoginActivity.this.showDoneAnimation[!z3 ? 1 : 0] == null || !LoginActivity.this.showDoneAnimation[!z3 ? 1 : 0].equals(animator) || z) {
                    return;
                }
                if (z3) {
                    LoginActivity.this.floatingButtonContainer.setVisibility(8);
                }
                if (!z3 || LoginActivity.this.floatingButtonIcon.getAlpha() == 1.0f) {
                    return;
                }
                LoginActivity.this.floatingButtonIcon.setAlpha(1.0f);
                LoginActivity.this.floatingButtonIcon.setScaleX(1.0f);
                LoginActivity.this.floatingButtonIcon.setScaleY(1.0f);
                LoginActivity.this.floatingButtonIcon.setVisibility(0);
                LoginActivity.this.floatingButtonContainer.setEnabled(true);
                LoginActivity.this.floatingProgressView.setAlpha(0.0f);
                LoginActivity.this.floatingProgressView.setScaleX(0.1f);
                LoginActivity.this.floatingProgressView.setScaleY(0.1f);
                LoginActivity.this.floatingProgressView.setVisibility(4);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (LoginActivity.this.showDoneAnimation[!z3 ? 1 : 0] == null || !LoginActivity.this.showDoneAnimation[!z3 ? 1 : 0].equals(animator)) {
                    return;
                }
                LoginActivity.this.showDoneAnimation[!z3 ? 1 : 0] = null;
            }
        });
        int i3 = ImageReceiver.DEFAULT_CROSSFADE_DURATION;
        if (!z3) {
            timeInterpolator = null;
        } else if (z) {
            i3 = 200;
            timeInterpolator = AndroidUtilities.decelerateInterpolator;
        } else {
            timeInterpolator = AndroidUtilities.accelerateInterpolator;
        }
        this.showDoneAnimation[this.currentDoneType].setDuration(i3);
        this.showDoneAnimation[this.currentDoneType].setInterpolator(timeInterpolator);
        this.showDoneAnimation[this.currentDoneType].start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showDoneButton$11(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.floatingAutoAnimator.setOffsetY(floatValue);
        this.floatingButtonContainer.setAlpha(1.0f - (floatValue / AndroidUtilities.dpf2(70.0f)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showDoneButton$12(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.floatingAutoAnimator.setOffsetY(floatValue);
        this.floatingButtonContainer.setAlpha(1.0f - (floatValue / AndroidUtilities.dpf2(70.0f)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDoneButtonPressed() {
        if (this.doneButtonVisible[this.currentDoneType]) {
            if (this.radialProgressView.getTag() != null) {
                if (getParentActivity() == null) {
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("StopLoadingTitle", R.string.StopLoadingTitle));
                builder.setMessage(LocaleController.getString("StopLoading", R.string.StopLoading));
                builder.setPositiveButton(LocaleController.getString("WaitMore", R.string.WaitMore), null);
                builder.setNegativeButton(LocaleController.getString("Stop", R.string.Stop), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda7
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        LoginActivity.this.lambda$onDoneButtonPressed$13(dialogInterface, i);
                    }
                });
                showDialog(builder.create());
                return;
            }
            this.views[this.currentViewNum].onNextPressed(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDoneButtonPressed$13(DialogInterface dialogInterface, int i) {
        this.views[this.currentViewNum].onCancelPressed();
        needHideProgress(true);
    }

    private void showEditDoneProgress(boolean z, boolean z2) {
        lambda$showEditDoneProgress$14(z, z2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: showEditDoneProgress */
    public void lambda$showEditDoneProgress$14(final boolean z, final boolean z2, final boolean z3) {
        if (z2 && this.doneProgressVisible[this.currentDoneType] == z && !z3) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.this.lambda$showEditDoneProgress$14(z, z2, z3);
                }
            });
            return;
        }
        final int i = this.currentDoneType;
        final boolean z4 = i == 0;
        if (!z3 && !z4) {
            this.doneProgressVisible[i] = z;
            if (z2) {
                if (this.postedEditDoneCallback[i]) {
                    AndroidUtilities.cancelRunOnUIThread(this.editDoneCallback[i]);
                    this.postedEditDoneCallback[this.currentDoneType] = false;
                    return;
                } else if (z) {
                    Runnable[] runnableArr = this.editDoneCallback;
                    Runnable runnable = new Runnable() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda20
                        @Override // java.lang.Runnable
                        public final void run() {
                            LoginActivity.this.lambda$showEditDoneProgress$15(i, z, z2);
                        }
                    };
                    runnableArr[i] = runnable;
                    AndroidUtilities.runOnUIThread(runnable, 2000L);
                    this.postedEditDoneCallback[this.currentDoneType] = true;
                    return;
                }
            }
        } else {
            this.postedEditDoneCallback[i] = false;
            this.doneProgressVisible[i] = z;
        }
        AnimatorSet animatorSet = this.doneItemAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (z2) {
            this.doneItemAnimation = new AnimatorSet();
            float[] fArr = new float[2];
            fArr[0] = z ? 0.0f : 1.0f;
            fArr[1] = z ? 1.0f : 0.0f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LoginActivity.10
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    if (z) {
                        if (z4) {
                            LoginActivity.this.floatingButtonIcon.setVisibility(0);
                            LoginActivity.this.floatingProgressView.setVisibility(0);
                            LoginActivity.this.floatingButtonContainer.setEnabled(false);
                            return;
                        }
                        LoginActivity.this.radialProgressView.setVisibility(0);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (z4) {
                        if (!z) {
                            LoginActivity.this.floatingProgressView.setVisibility(4);
                            LoginActivity.this.floatingButtonIcon.setVisibility(0);
                            LoginActivity.this.floatingButtonContainer.setEnabled(true);
                        } else {
                            LoginActivity.this.floatingButtonIcon.setVisibility(4);
                            LoginActivity.this.floatingProgressView.setVisibility(0);
                        }
                    } else if (!z) {
                        LoginActivity.this.radialProgressView.setVisibility(4);
                    }
                    if (LoginActivity.this.doneItemAnimation == null || !LoginActivity.this.doneItemAnimation.equals(animator)) {
                        return;
                    }
                    LoginActivity.this.doneItemAnimation = null;
                }
            });
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda5
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LoginActivity.this.lambda$showEditDoneProgress$16(z4, valueAnimator);
                }
            });
            this.doneItemAnimation.playTogether(ofFloat);
            this.doneItemAnimation.setDuration(150L);
            this.doneItemAnimation.start();
        } else if (z) {
            if (z4) {
                this.floatingProgressView.setVisibility(0);
                this.floatingButtonIcon.setVisibility(4);
                this.floatingButtonContainer.setEnabled(false);
                this.floatingButtonIcon.setScaleX(0.1f);
                this.floatingButtonIcon.setScaleY(0.1f);
                this.floatingButtonIcon.setAlpha(0.0f);
                this.floatingProgressView.setScaleX(1.0f);
                this.floatingProgressView.setScaleY(1.0f);
                this.floatingProgressView.setAlpha(1.0f);
                return;
            }
            this.radialProgressView.setVisibility(0);
            this.radialProgressView.setScaleX(1.0f);
            this.radialProgressView.setScaleY(1.0f);
            this.radialProgressView.setAlpha(1.0f);
        } else {
            this.radialProgressView.setTag(null);
            if (z4) {
                this.floatingProgressView.setVisibility(4);
                this.floatingButtonIcon.setVisibility(0);
                this.floatingButtonContainer.setEnabled(true);
                this.floatingProgressView.setScaleX(0.1f);
                this.floatingProgressView.setScaleY(0.1f);
                this.floatingProgressView.setAlpha(0.0f);
                this.floatingButtonIcon.setScaleX(1.0f);
                this.floatingButtonIcon.setScaleY(1.0f);
                this.floatingButtonIcon.setAlpha(1.0f);
                return;
            }
            this.radialProgressView.setVisibility(4);
            this.radialProgressView.setScaleX(0.1f);
            this.radialProgressView.setScaleY(0.1f);
            this.radialProgressView.setAlpha(0.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showEditDoneProgress$15(int i, boolean z, boolean z2) {
        int i2 = this.currentDoneType;
        this.currentDoneType = i;
        lambda$showEditDoneProgress$14(z, z2, true);
        this.currentDoneType = i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showEditDoneProgress$16(boolean z, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (z) {
            float f = 1.0f - floatValue;
            float f2 = (f * 0.9f) + 0.1f;
            this.floatingButtonIcon.setScaleX(f2);
            this.floatingButtonIcon.setScaleY(f2);
            this.floatingButtonIcon.setAlpha(f);
            float f3 = (0.9f * floatValue) + 0.1f;
            this.floatingProgressView.setScaleX(f3);
            this.floatingProgressView.setScaleY(f3);
            this.floatingProgressView.setAlpha(floatValue);
            return;
        }
        float f4 = (0.9f * floatValue) + 0.1f;
        this.radialProgressView.setScaleX(f4);
        this.radialProgressView.setScaleY(f4);
        this.radialProgressView.setAlpha(floatValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void needShowProgress(int i) {
        needShowProgress(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void needShowProgress(int i, boolean z) {
        if (isInCancelAccountDeletionMode() && i == 0) {
            if (this.cancelDeleteProgressDialog != null || getParentActivity() == null || getParentActivity().isFinishing()) {
                return;
            }
            AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
            this.cancelDeleteProgressDialog = alertDialog;
            alertDialog.setCanCancel(false);
            this.cancelDeleteProgressDialog.show();
            return;
        }
        this.progressRequestId = i;
        showEditDoneProgress(true, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void needHideProgress(boolean z) {
        needHideProgress(z, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void needHideProgress(boolean z, boolean z2) {
        AlertDialog alertDialog;
        if (this.progressRequestId != 0) {
            if (z) {
                ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.progressRequestId, true);
            }
            this.progressRequestId = 0;
        }
        if (isInCancelAccountDeletionMode() && (alertDialog = this.cancelDeleteProgressDialog) != null) {
            alertDialog.dismiss();
            this.cancelDeleteProgressDialog = null;
        }
        showEditDoneProgress(false, z2);
    }

    public void setPage(int i, boolean z, Bundle bundle, boolean z2) {
        final boolean z3 = i == 0 || i == 5 || i == 6 || i == 9 || i == 10 || i == 12;
        int i2 = 8;
        if (z3) {
            if (i == 0) {
                this.checkPermissions = true;
                this.checkShowPermissions = true;
            }
            this.currentDoneType = 1;
            showDoneButton(false, z);
            showEditDoneProgress(false, z);
            this.currentDoneType = 0;
            showEditDoneProgress(false, z);
            if (!z) {
                showDoneButton(true, false);
            }
        } else {
            this.currentDoneType = 0;
            showDoneButton(false, z);
            showEditDoneProgress(false, z);
            if (i != 8) {
                this.currentDoneType = 1;
            }
        }
        if (z) {
            SlideView[] slideViewArr = this.views;
            final SlideView slideView = slideViewArr[this.currentViewNum];
            SlideView slideView2 = slideViewArr[i];
            this.currentViewNum = i;
            this.backButtonView.setVisibility((slideView2.needBackButton() || this.newAccount) ? 0 : 0);
            slideView2.setParams(bundle, false);
            setParentActivityTitle(slideView2.getHeaderName());
            slideView2.onShow();
            int i3 = AndroidUtilities.displaySize.x;
            if (z2) {
                i3 = -i3;
            }
            slideView2.setX(i3);
            slideView2.setVisibility(0);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LoginActivity.11
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (LoginActivity.this.currentDoneType == 0 && z3) {
                        LoginActivity.this.showDoneButton(true, true);
                    }
                    slideView.setVisibility(8);
                    slideView.onHide();
                    slideView.setX(0.0f);
                }
            });
            Animator[] animatorArr = new Animator[2];
            Property property = View.TRANSLATION_X;
            float[] fArr = new float[1];
            fArr[0] = z2 ? AndroidUtilities.displaySize.x : -AndroidUtilities.displaySize.x;
            animatorArr[0] = ObjectAnimator.ofFloat(slideView, property, fArr);
            animatorArr[1] = ObjectAnimator.ofFloat(slideView2, View.TRANSLATION_X, 0.0f);
            animatorSet.playTogether(animatorArr);
            animatorSet.setDuration(300L);
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            animatorSet.start();
            setCustomKeyboardVisible(slideView2.hasCustomKeyboard(), true);
            return;
        }
        this.backButtonView.setVisibility((this.views[i].needBackButton() || this.newAccount) ? 0 : 8);
        this.views[this.currentViewNum].setVisibility(8);
        this.views[this.currentViewNum].onHide();
        this.currentViewNum = i;
        this.views[i].setParams(bundle, false);
        this.views[i].setVisibility(0);
        setParentActivityTitle(this.views[i].getHeaderName());
        this.views[i].onShow();
        setCustomKeyboardVisible(this.views[i].hasCustomKeyboard(), false);
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void saveSelfArgs(Bundle bundle) {
        try {
            Bundle bundle2 = new Bundle();
            bundle2.putInt("currentViewNum", this.currentViewNum);
            bundle2.putInt("syncContacts", this.syncContacts ? 1 : 0);
            for (int i = 0; i <= this.currentViewNum; i++) {
                SlideView slideView = this.views[i];
                if (slideView != null) {
                    slideView.saveStateParams(bundle2);
                }
            }
            SharedPreferences.Editor edit = ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).edit();
            edit.clear();
            putBundleToEditor(bundle2, edit, null);
            edit.commit();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void needFinishActivity(final boolean z, boolean z2, int i) {
        if (getParentActivity() != null) {
            AndroidUtilities.setLightStatusBar(getParentActivity().getWindow(), false);
        }
        clearCurrentState();
        if (getParentActivity() instanceof LaunchActivity) {
            if (this.newAccount) {
                this.newAccount = false;
                ((LaunchActivity) getParentActivity()).switchToAccount(this.currentAccount, false, new GenericProvider() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda25
                    @Override // org.telegram.messenger.GenericProvider
                    public final Object provide(Object obj) {
                        DialogsActivity lambda$needFinishActivity$17;
                        lambda$needFinishActivity$17 = LoginActivity.lambda$needFinishActivity$17(z, (Void) obj);
                        return lambda$needFinishActivity$17;
                    }
                });
                finishFragment();
                return;
            }
            if (z && z2) {
                TwoStepVerificationSetupActivity twoStepVerificationSetupActivity = new TwoStepVerificationSetupActivity(6, null);
                twoStepVerificationSetupActivity.setBlockingAlert(i);
                twoStepVerificationSetupActivity.setFromRegistration(true);
                presentFragment(twoStepVerificationSetupActivity, true);
            } else {
                Bundle bundle = new Bundle();
                bundle.putBoolean("afterSignup", z);
                presentFragment(new DialogsActivity(bundle), true);
            }
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
            LocaleController.getInstance().loadRemoteLanguages(this.currentAccount);
            RestrictedLanguagesSelectActivity.checkRestrictedLanguages(true);
        } else if (getParentActivity() instanceof ExternalActionActivity) {
            ((ExternalActionActivity) getParentActivity()).onFinishLogin();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ DialogsActivity lambda$needFinishActivity$17(boolean z, Void r2) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("afterSignup", z);
        return new DialogsActivity(bundle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAuthSuccess(TLRPC$TL_auth_authorization tLRPC$TL_auth_authorization) {
        onAuthSuccess(tLRPC$TL_auth_authorization, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAuthSuccess(TLRPC$TL_auth_authorization tLRPC$TL_auth_authorization, boolean z) {
        MessagesController.getInstance(this.currentAccount).cleanup();
        ConnectionsManager.getInstance(this.currentAccount).setUserId(tLRPC$TL_auth_authorization.user.id);
        UserConfig.getInstance(this.currentAccount).clearConfig();
        MessagesController.getInstance(this.currentAccount).cleanup();
        UserConfig.getInstance(this.currentAccount).syncContacts = this.syncContacts;
        UserConfig.getInstance(this.currentAccount).setCurrentUser(tLRPC$TL_auth_authorization.user);
        UserConfig.getInstance(this.currentAccount).saveConfig(true);
        MessagesStorage.getInstance(this.currentAccount).cleanup(true);
        ArrayList<TLRPC$User> arrayList = new ArrayList<>();
        arrayList.add(tLRPC$TL_auth_authorization.user);
        MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(arrayList, null, true, true);
        MessagesController.getInstance(this.currentAccount).putUser(tLRPC$TL_auth_authorization.user, false);
        ContactsController.getInstance(this.currentAccount).checkAppAccount();
        MessagesController.getInstance(this.currentAccount).checkPromoInfo(true);
        ConnectionsManager.getInstance(this.currentAccount).updateDcSettings();
        if (tLRPC$TL_auth_authorization.future_auth_token != null) {
            AuthTokensHelper.saveLogInToken(tLRPC$TL_auth_authorization);
        } else {
            FileLog.d("onAuthSuccess future_auth_token is empty");
        }
        if (z) {
            MessagesController.getInstance(this.currentAccount).putDialogsEndReachedAfterRegistration();
        }
        MediaDataController.getInstance(this.currentAccount).loadStickersByEmojiOrName(AndroidUtilities.STICKERS_PLACEHOLDER_PACK_NAME, false, true);
        needFinishActivity(z, tLRPC$TL_auth_authorization.setup_password_required, tLRPC$TL_auth_authorization.otherwise_relogin_days);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fillNextCodeParams(Bundle bundle, TLRPC$TL_account_sentEmailCode tLRPC$TL_account_sentEmailCode) {
        bundle.putString("emailPattern", tLRPC$TL_account_sentEmailCode.email_pattern);
        bundle.putInt("length", tLRPC$TL_account_sentEmailCode.length);
        setPage(13, true, bundle, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: fillNextCodeParams */
    public void lambda$resendCodeFromSafetyNet$18(Bundle bundle, TLRPC$auth_SentCode tLRPC$auth_SentCode) {
        lambda$fillNextCodeParams$22(bundle, tLRPC$auth_SentCode, true);
    }

    private void resendCodeFromSafetyNet(final Bundle bundle, TLRPC$auth_SentCode tLRPC$auth_SentCode) {
        if (this.isRequestingFirebaseSms) {
            needHideProgress(false);
            this.isRequestingFirebaseSms = false;
            TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode = new TLRPC$TL_auth_resendCode();
            tLRPC$TL_auth_resendCode.phone_number = bundle.getString("phoneFormated");
            tLRPC$TL_auth_resendCode.phone_code_hash = tLRPC$auth_SentCode.phone_code_hash;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_auth_resendCode, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda26
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LoginActivity.this.lambda$resendCodeFromSafetyNet$21(bundle, tLObject, tLRPC$TL_error);
                }
            }, 10);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resendCodeFromSafetyNet$21(final Bundle bundle, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLObject != null) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.this.lambda$resendCodeFromSafetyNet$18(bundle, tLObject);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.this.lambda$resendCodeFromSafetyNet$20();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resendCodeFromSafetyNet$20() {
        if (getParentActivity() == null || getParentActivity().isFinishing() || getContext() == null) {
            return;
        }
        new AlertDialog.Builder(getContext()).setTitle(LocaleController.getString(R.string.RestorePasswordNoEmailTitle)).setMessage(LocaleController.getString(R.string.SafetyNetErrorOccurred)).setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda6
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                LoginActivity.this.lambda$resendCodeFromSafetyNet$19(dialogInterface, i);
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resendCodeFromSafetyNet$19(DialogInterface dialogInterface, int i) {
        this.forceDisableSafetyNet = true;
        if (this.currentViewNum != 0) {
            setPage(0, true, null, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: fillNextCodeParams */
    public void lambda$fillNextCodeParams$22(final Bundle bundle, final TLRPC$auth_SentCode tLRPC$auth_SentCode, final boolean z) {
        TLRPC$auth_SentCodeType tLRPC$auth_SentCodeType = tLRPC$auth_SentCode.type;
        if ((tLRPC$auth_SentCodeType instanceof TLRPC$TL_auth_sentCodeTypeFirebaseSms) && !tLRPC$auth_SentCodeType.verifiedFirebase && !this.isRequestingFirebaseSms) {
            if (PushListenerController.GooglePushListenerServiceProvider.INSTANCE.hasServices()) {
                needShowProgress(0);
                this.isRequestingFirebaseSms = true;
                SafetyNet.getClient(ApplicationLoader.applicationContext).attest(tLRPC$auth_SentCode.type.nonce, BuildVars.SAFETYNET_KEY).addOnSuccessListener(new OnSuccessListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda14
                    @Override // com.google.android.gms.tasks.OnSuccessListener
                    public final void onSuccess(Object obj) {
                        LoginActivity.this.lambda$fillNextCodeParams$24(bundle, tLRPC$auth_SentCode, z, (SafetyNetApi$AttestationResponse) obj);
                    }
                }).addOnFailureListener(new OnFailureListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda13
                    @Override // com.google.android.gms.tasks.OnFailureListener
                    public final void onFailure(Exception exc) {
                        LoginActivity.this.lambda$fillNextCodeParams$25(bundle, tLRPC$auth_SentCode, exc);
                    }
                });
                return;
            }
            FileLog.d("Resend firebase sms because firebase is not available");
            resendCodeFromSafetyNet(bundle, tLRPC$auth_SentCode);
            return;
        }
        bundle.putString("phoneHash", tLRPC$auth_SentCode.phone_code_hash);
        TLRPC$auth_CodeType tLRPC$auth_CodeType = tLRPC$auth_SentCode.next_type;
        if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeCall) {
            bundle.putInt("nextType", 4);
        } else if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeFlashCall) {
            bundle.putInt("nextType", 3);
        } else if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeSms) {
            bundle.putInt("nextType", 2);
        } else if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeMissedCall) {
            bundle.putInt("nextType", 11);
        } else if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeFragmentSms) {
            bundle.putInt("nextType", 15);
        }
        if (tLRPC$auth_SentCode.type instanceof TLRPC$TL_auth_sentCodeTypeApp) {
            bundle.putInt("type", 1);
            bundle.putInt("length", tLRPC$auth_SentCode.type.length);
            setPage(1, z, bundle, false);
            return;
        }
        if (tLRPC$auth_SentCode.timeout == 0) {
            tLRPC$auth_SentCode.timeout = 60;
        }
        bundle.putInt("timeout", tLRPC$auth_SentCode.timeout * 1000);
        TLRPC$auth_SentCodeType tLRPC$auth_SentCodeType2 = tLRPC$auth_SentCode.type;
        if (tLRPC$auth_SentCodeType2 instanceof TLRPC$TL_auth_sentCodeTypeCall) {
            bundle.putInt("type", 4);
            bundle.putInt("length", tLRPC$auth_SentCode.type.length);
            setPage(4, z, bundle, false);
        } else if (tLRPC$auth_SentCodeType2 instanceof TLRPC$TL_auth_sentCodeTypeFlashCall) {
            bundle.putInt("type", 3);
            bundle.putString("pattern", tLRPC$auth_SentCode.type.pattern);
            setPage(3, z, bundle, false);
        } else if ((tLRPC$auth_SentCodeType2 instanceof TLRPC$TL_auth_sentCodeTypeSms) || (tLRPC$auth_SentCodeType2 instanceof TLRPC$TL_auth_sentCodeTypeFirebaseSms)) {
            bundle.putInt("type", 2);
            bundle.putInt("length", tLRPC$auth_SentCode.type.length);
            bundle.putBoolean("firebase", tLRPC$auth_SentCode.type instanceof TLRPC$TL_auth_sentCodeTypeFirebaseSms);
            setPage(2, z, bundle, false);
        } else if (tLRPC$auth_SentCodeType2 instanceof TLRPC$TL_auth_sentCodeTypeFragmentSms) {
            bundle.putInt("type", 15);
            bundle.putString("url", tLRPC$auth_SentCode.type.url);
            bundle.putInt("length", tLRPC$auth_SentCode.type.length);
            setPage(15, z, bundle, false);
        } else if (tLRPC$auth_SentCodeType2 instanceof TLRPC$TL_auth_sentCodeTypeMissedCall) {
            bundle.putInt("type", 11);
            bundle.putInt("length", tLRPC$auth_SentCode.type.length);
            bundle.putString("prefix", tLRPC$auth_SentCode.type.prefix);
            setPage(11, z, bundle, false);
        } else if (tLRPC$auth_SentCodeType2 instanceof TLRPC$TL_auth_sentCodeTypeSetUpEmailRequired) {
            bundle.putBoolean("googleSignInAllowed", tLRPC$auth_SentCodeType2.google_signin_allowed);
            setPage(12, z, bundle, false);
        } else if (tLRPC$auth_SentCodeType2 instanceof TLRPC$TL_auth_sentCodeTypeEmailCode) {
            bundle.putBoolean("googleSignInAllowed", tLRPC$auth_SentCodeType2.google_signin_allowed);
            bundle.putString("emailPattern", tLRPC$auth_SentCode.type.email_pattern);
            bundle.putInt("length", tLRPC$auth_SentCode.type.length);
            bundle.putInt("nextPhoneLoginDate", tLRPC$auth_SentCode.type.next_phone_login_date);
            setPage(14, z, bundle, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillNextCodeParams$24(final Bundle bundle, final TLRPC$auth_SentCode tLRPC$auth_SentCode, final boolean z, SafetyNetApi$AttestationResponse safetyNetApi$AttestationResponse) {
        String jwsResult = safetyNetApi$AttestationResponse.getJwsResult();
        if (jwsResult != null) {
            TLRPC$TL_auth_requestFirebaseSms tLRPC$TL_auth_requestFirebaseSms = new TLRPC$TL_auth_requestFirebaseSms();
            tLRPC$TL_auth_requestFirebaseSms.phone_number = bundle.getString("phoneFormated");
            tLRPC$TL_auth_requestFirebaseSms.phone_code_hash = tLRPC$auth_SentCode.phone_code_hash;
            tLRPC$TL_auth_requestFirebaseSms.safety_net_token = jwsResult;
            tLRPC$TL_auth_requestFirebaseSms.flags |= 1;
            String[] split = jwsResult.split("\\.");
            if (split.length > 0) {
                try {
                    JSONObject jSONObject = new JSONObject(new String(Base64.decode(split[1].getBytes(StandardCharsets.UTF_8), 0)));
                    if (jSONObject.optBoolean("basicIntegrity") && jSONObject.optBoolean("ctsProfileMatch")) {
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_auth_requestFirebaseSms, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda28
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LoginActivity.this.lambda$fillNextCodeParams$23(tLRPC$auth_SentCode, bundle, z, tLObject, tLRPC$TL_error);
                            }
                        }, 10);
                    } else {
                        FileLog.d("Resend firebase sms because ctsProfileMatch or basicIntegrity = false");
                        resendCodeFromSafetyNet(bundle, tLRPC$auth_SentCode);
                    }
                    return;
                } catch (JSONException e) {
                    FileLog.e(e);
                    FileLog.d("Resend firebase sms because of exception");
                    resendCodeFromSafetyNet(bundle, tLRPC$auth_SentCode);
                    return;
                }
            }
            FileLog.d("Resend firebase sms because can't split JWS token");
            resendCodeFromSafetyNet(bundle, tLRPC$auth_SentCode);
            return;
        }
        FileLog.d("Resend firebase sms because JWS = null");
        resendCodeFromSafetyNet(bundle, tLRPC$auth_SentCode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillNextCodeParams$23(final TLRPC$auth_SentCode tLRPC$auth_SentCode, final Bundle bundle, final boolean z, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        needHideProgress(false);
        this.isRequestingFirebaseSms = false;
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            tLRPC$auth_SentCode.type.verifiedFirebase = true;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.this.lambda$fillNextCodeParams$22(bundle, tLRPC$auth_SentCode, z);
                }
            });
            return;
        }
        FileLog.d("Resend firebase sms because auth.requestFirebaseSms = false");
        resendCodeFromSafetyNet(bundle, tLRPC$auth_SentCode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fillNextCodeParams$25(Bundle bundle, TLRPC$auth_SentCode tLRPC$auth_SentCode, Exception exc) {
        FileLog.e(exc);
        FileLog.d("Resend firebase sms because of safetynet exception");
        resendCodeFromSafetyNet(bundle, tLRPC$auth_SentCode);
    }

    /* loaded from: classes3.dex */
    public class PhoneView extends SlideView implements AdapterView.OnItemSelectedListener, NotificationCenter.NotificationCenterDelegate {
        private ImageView chevronRight;
        private View codeDividerView;
        private AnimatedPhoneNumberEditText codeField;
        private HashMap<String, List<CountrySelectActivity.Country>> codesMap;
        private boolean confirmedNumber;
        private ArrayList<CountrySelectActivity.Country> countriesArray;
        private TextViewSwitcher countryButton;
        private String countryCodeForHint;
        private OutlineTextContainerView countryOutlineView;
        private int countryState;
        private CountrySelectActivity.Country currentCountry;
        private boolean ignoreOnPhoneChange;
        private boolean ignoreOnTextChange;
        private boolean ignoreSelection;
        private boolean nextPressed;
        private boolean numberFilled;
        private AnimatedPhoneNumberEditText phoneField;
        private HashMap<String, List<String>> phoneFormatMap;
        private OutlineTextContainerView phoneOutlineView;
        private TextView plusTextView;
        private TextView subtitleView;
        private CheckBoxCell syncContactsBox;
        private CheckBoxCell testBackendCheckBox;
        private TextView titleView;
        private int wasCountryHintIndex;

        @Override // org.telegram.ui.Components.SlideView
        public boolean hasCustomKeyboard() {
            return true;
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public PhoneView(final Context context) {
            super(context);
            this.countryState = 0;
            this.countriesArray = new ArrayList<>();
            this.codesMap = new HashMap<>();
            this.phoneFormatMap = new HashMap<>();
            this.ignoreSelection = false;
            this.ignoreOnTextChange = false;
            this.ignoreOnPhoneChange = false;
            this.nextPressed = false;
            this.confirmedNumber = false;
            this.wasCountryHintIndex = -1;
            setOrientation(1);
            setGravity(17);
            TextView textView = new TextView(context);
            this.titleView = textView;
            textView.setTextSize(1, 18.0f);
            this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.titleView.setText(LocaleController.getString(LoginActivity.this.activityMode == 2 ? R.string.ChangePhoneNewNumber : R.string.YourNumber));
            this.titleView.setGravity(17);
            this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 0.0f, 32.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.subtitleView = textView2;
            textView2.setText(LocaleController.getString(LoginActivity.this.activityMode == 2 ? R.string.ChangePhoneHelp : R.string.StartText));
            this.subtitleView.setTextSize(1, 14.0f);
            this.subtitleView.setGravity(17);
            this.subtitleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.subtitleView, LayoutHelper.createLinear(-1, -2, 1, 32, 8, 32, 0));
            TextViewSwitcher textViewSwitcher = new TextViewSwitcher(context);
            this.countryButton = textViewSwitcher;
            textViewSwitcher.setFactory(new ViewSwitcher.ViewFactory() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda7
                @Override // android.widget.ViewSwitcher.ViewFactory
                public final View makeView() {
                    View lambda$new$0;
                    lambda$new$0 = LoginActivity.PhoneView.lambda$new$0(context);
                    return lambda$new$0;
                }
            });
            Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.text_in);
            loadAnimation.setInterpolator(Easings.easeInOutQuad);
            this.countryButton.setInAnimation(loadAnimation);
            ImageView imageView = new ImageView(context);
            this.chevronRight = imageView;
            imageView.setImageResource(R.drawable.msg_inputarrow);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            linearLayout.addView(this.countryButton, LayoutHelper.createLinear(0, -2, 1.0f, 0, 0, 0, 0));
            linearLayout.addView(this.chevronRight, LayoutHelper.createLinearRelatively(24.0f, 24.0f, 0, 0.0f, 0.0f, 14.0f, 0.0f));
            OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
            this.countryOutlineView = outlineTextContainerView;
            int i = R.string.Country;
            outlineTextContainerView.setText(LocaleController.getString(i));
            this.countryOutlineView.addView(linearLayout, LayoutHelper.createFrame(-1, -1.0f, 48, 0.0f, 0.0f, 0.0f, 0.0f));
            this.countryOutlineView.setForceUseCenter(true);
            this.countryOutlineView.setFocusable(true);
            this.countryOutlineView.setContentDescription(LocaleController.getString(i));
            this.countryOutlineView.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda4
                @Override // android.view.View.OnFocusChangeListener
                public final void onFocusChange(View view, boolean z) {
                    LoginActivity.PhoneView.this.lambda$new$1(view, z);
                }
            });
            addView(this.countryOutlineView, LayoutHelper.createLinear(-1, 58, 16.0f, 24.0f, 16.0f, 14.0f));
            this.countryOutlineView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LoginActivity.PhoneView.this.lambda$new$4(view);
                }
            });
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(0);
            OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
            this.phoneOutlineView = outlineTextContainerView2;
            outlineTextContainerView2.addView(linearLayout2, LayoutHelper.createFrame(-1, -2.0f, 16, 16.0f, 8.0f, 16.0f, 8.0f));
            OutlineTextContainerView outlineTextContainerView3 = this.phoneOutlineView;
            int i2 = R.string.PhoneNumber;
            outlineTextContainerView3.setText(LocaleController.getString(i2));
            addView(this.phoneOutlineView, LayoutHelper.createLinear(-1, 58, 16.0f, 8.0f, 16.0f, 8.0f));
            TextView textView3 = new TextView(context);
            this.plusTextView = textView3;
            textView3.setText("+");
            this.plusTextView.setTextSize(1, 16.0f);
            this.plusTextView.setFocusable(false);
            linearLayout2.addView(this.plusTextView, LayoutHelper.createLinear(-2, -2));
            AnimatedPhoneNumberEditText animatedPhoneNumberEditText = new AnimatedPhoneNumberEditText(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.PhoneView.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
                public void onFocusChanged(boolean z, int i3, Rect rect) {
                    super.onFocusChanged(z, i3, rect);
                    PhoneView.this.phoneOutlineView.animateSelection((z || PhoneView.this.phoneField.isFocused()) ? 1.0f : 0.0f);
                    if (z) {
                        LoginActivity.this.keyboardView.setEditText(this);
                    }
                }
            };
            this.codeField = animatedPhoneNumberEditText;
            animatedPhoneNumberEditText.setInputType(3);
            this.codeField.setCursorSize(AndroidUtilities.dp(20.0f));
            this.codeField.setCursorWidth(1.5f);
            this.codeField.setPadding(AndroidUtilities.dp(10.0f), 0, 0, 0);
            this.codeField.setTextSize(1, 16.0f);
            this.codeField.setMaxLines(1);
            this.codeField.setGravity(19);
            this.codeField.setImeOptions(268435461);
            this.codeField.setBackground(null);
            int i3 = Build.VERSION.SDK_INT;
            if (i3 >= 21) {
                this.codeField.setShowSoftInputOnFocus(!hasCustomKeyboard() || LoginActivity.this.isCustomKeyboardForceDisabled());
            }
            this.codeField.setContentDescription(LocaleController.getString(R.string.LoginAccessibilityCountryCode));
            linearLayout2.addView(this.codeField, LayoutHelper.createLinear(55, 36, -9.0f, 0.0f, 0.0f, 0.0f));
            this.codeField.addTextChangedListener(new TextWatcher(LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.PhoneView.2
                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    String str;
                    boolean z;
                    CountrySelectActivity.Country country;
                    CountrySelectActivity.Country country2;
                    if (PhoneView.this.ignoreOnTextChange) {
                        return;
                    }
                    PhoneView.this.ignoreOnTextChange = true;
                    String stripExceptNumbers = PhoneFormat.stripExceptNumbers(PhoneView.this.codeField.getText().toString());
                    PhoneView.this.codeField.setText(stripExceptNumbers);
                    if (stripExceptNumbers.length() == 0) {
                        PhoneView.this.setCountryButtonText(null);
                        PhoneView.this.phoneField.setHintText((String) null);
                        PhoneView.this.countryState = 1;
                    } else {
                        int i4 = 4;
                        if (stripExceptNumbers.length() > 4) {
                            while (true) {
                                if (i4 < 1) {
                                    str = null;
                                    z = false;
                                    break;
                                }
                                String substring = stripExceptNumbers.substring(0, i4);
                                List list = (List) PhoneView.this.codesMap.get(substring);
                                if (list == null) {
                                    country2 = null;
                                } else if (list.size() > 1) {
                                    String string = MessagesController.getGlobalMainSettings().getString("phone_code_last_matched_" + substring, null);
                                    country2 = (CountrySelectActivity.Country) list.get(list.size() - 1);
                                    if (string != null) {
                                        Iterator it = PhoneView.this.countriesArray.iterator();
                                        while (true) {
                                            if (!it.hasNext()) {
                                                break;
                                            }
                                            CountrySelectActivity.Country country3 = (CountrySelectActivity.Country) it.next();
                                            if (Objects.equals(country3.shortname, string)) {
                                                country2 = country3;
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    country2 = (CountrySelectActivity.Country) list.get(0);
                                }
                                if (country2 != null) {
                                    String str2 = stripExceptNumbers.substring(i4) + PhoneView.this.phoneField.getText().toString();
                                    PhoneView.this.codeField.setText(substring);
                                    z = true;
                                    str = str2;
                                    stripExceptNumbers = substring;
                                    break;
                                }
                                i4--;
                            }
                            if (!z) {
                                str = stripExceptNumbers.substring(1) + PhoneView.this.phoneField.getText().toString();
                                AnimatedPhoneNumberEditText animatedPhoneNumberEditText2 = PhoneView.this.codeField;
                                stripExceptNumbers = stripExceptNumbers.substring(0, 1);
                                animatedPhoneNumberEditText2.setText(stripExceptNumbers);
                            }
                        } else {
                            str = null;
                            z = false;
                        }
                        Iterator it2 = PhoneView.this.countriesArray.iterator();
                        CountrySelectActivity.Country country4 = null;
                        int i5 = 0;
                        while (it2.hasNext()) {
                            CountrySelectActivity.Country country5 = (CountrySelectActivity.Country) it2.next();
                            if (country5.code.startsWith(stripExceptNumbers)) {
                                i5++;
                                if (country5.code.equals(stripExceptNumbers)) {
                                    if (country4 != null && country4.code.equals(country5.code)) {
                                        i5--;
                                    }
                                    country4 = country5;
                                }
                            }
                        }
                        if (i5 == 1 && country4 != null && str == null) {
                            str = stripExceptNumbers.substring(country4.code.length()) + PhoneView.this.phoneField.getText().toString();
                            AnimatedPhoneNumberEditText animatedPhoneNumberEditText3 = PhoneView.this.codeField;
                            String str3 = country4.code;
                            animatedPhoneNumberEditText3.setText(str3);
                            stripExceptNumbers = str3;
                        }
                        List list2 = (List) PhoneView.this.codesMap.get(stripExceptNumbers);
                        if (list2 == null) {
                            country = null;
                        } else if (list2.size() > 1) {
                            String string2 = MessagesController.getGlobalMainSettings().getString("phone_code_last_matched_" + stripExceptNumbers, null);
                            country = (CountrySelectActivity.Country) list2.get(list2.size() - 1);
                            if (string2 != null) {
                                Iterator it3 = PhoneView.this.countriesArray.iterator();
                                while (true) {
                                    if (!it3.hasNext()) {
                                        break;
                                    }
                                    CountrySelectActivity.Country country6 = (CountrySelectActivity.Country) it3.next();
                                    if (Objects.equals(country6.shortname, string2)) {
                                        country = country6;
                                        break;
                                    }
                                }
                            }
                        } else {
                            country = (CountrySelectActivity.Country) list2.get(0);
                        }
                        if (country != null) {
                            PhoneView.this.ignoreSelection = true;
                            PhoneView.this.currentCountry = country;
                            PhoneView.this.setCountryHint(stripExceptNumbers, country);
                            PhoneView.this.countryState = 0;
                        } else {
                            PhoneView.this.setCountryButtonText(null);
                            PhoneView.this.phoneField.setHintText((String) null);
                            PhoneView.this.countryState = 2;
                        }
                        if (!z) {
                            PhoneView.this.codeField.setSelection(PhoneView.this.codeField.getText().length());
                        }
                        if (str != null) {
                            PhoneView.this.phoneField.requestFocus();
                            PhoneView.this.phoneField.setText(str);
                            PhoneView.this.phoneField.setSelection(PhoneView.this.phoneField.length());
                        }
                    }
                    PhoneView.this.ignoreOnTextChange = false;
                }
            });
            this.codeField.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda5
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView4, int i4, KeyEvent keyEvent) {
                    boolean lambda$new$5;
                    lambda$new$5 = LoginActivity.PhoneView.this.lambda$new$5(textView4, i4, keyEvent);
                    return lambda$new$5;
                }
            });
            this.codeDividerView = new View(context);
            LinearLayout.LayoutParams createLinear = LayoutHelper.createLinear(0, -1, 4.0f, 8.0f, 12.0f, 8.0f);
            createLinear.width = Math.max(2, AndroidUtilities.dp(0.5f));
            linearLayout2.addView(this.codeDividerView, createLinear);
            AnimatedPhoneNumberEditText animatedPhoneNumberEditText2 = new AnimatedPhoneNumberEditText(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.PhoneView.3
                @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
                public boolean onKeyDown(int i4, KeyEvent keyEvent) {
                    if (i4 == 67 && PhoneView.this.phoneField.length() == 0) {
                        PhoneView.this.codeField.requestFocus();
                        PhoneView.this.codeField.setSelection(PhoneView.this.codeField.length());
                        PhoneView.this.codeField.dispatchKeyEvent(keyEvent);
                    }
                    return super.onKeyDown(i4, keyEvent);
                }

                @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
                public boolean onTouchEvent(MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 0 && !LoginActivity.this.showKeyboard(this)) {
                        clearFocus();
                        requestFocus();
                    }
                    return super.onTouchEvent(motionEvent);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
                public void onFocusChanged(boolean z, int i4, Rect rect) {
                    super.onFocusChanged(z, i4, rect);
                    PhoneView.this.phoneOutlineView.animateSelection((z || PhoneView.this.codeField.isFocused()) ? 1.0f : 0.0f);
                    if (z) {
                        LoginActivity.this.keyboardView.setEditText(this);
                        LoginActivity.this.keyboardView.setDispatchBackWhenEmpty(true);
                        if (PhoneView.this.countryState == 2) {
                            PhoneView.this.setCountryButtonText(LocaleController.getString(R.string.WrongCountry));
                        }
                    } else if (PhoneView.this.countryState == 2) {
                        PhoneView.this.setCountryButtonText(null);
                    }
                }
            };
            this.phoneField = animatedPhoneNumberEditText2;
            animatedPhoneNumberEditText2.setInputType(3);
            this.phoneField.setPadding(0, 0, 0, 0);
            this.phoneField.setCursorSize(AndroidUtilities.dp(20.0f));
            this.phoneField.setCursorWidth(1.5f);
            this.phoneField.setTextSize(1, 16.0f);
            this.phoneField.setMaxLines(1);
            this.phoneField.setGravity(19);
            this.phoneField.setImeOptions(268435461);
            this.phoneField.setBackground(null);
            if (i3 >= 21) {
                this.phoneField.setShowSoftInputOnFocus(!hasCustomKeyboard() || LoginActivity.this.isCustomKeyboardForceDisabled());
            }
            this.phoneField.setContentDescription(LocaleController.getString(i2));
            linearLayout2.addView(this.phoneField, LayoutHelper.createFrame(-1, 36.0f));
            this.phoneField.addTextChangedListener(new TextWatcher(LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.PhoneView.4
                private int actionPosition;
                private int characterAction = -1;

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                    if (i5 == 0 && i6 == 1) {
                        this.characterAction = 1;
                    } else if (i5 == 1 && i6 == 0) {
                        if (charSequence.charAt(i4) == ' ' && i4 > 0) {
                            this.characterAction = 3;
                            this.actionPosition = i4 - 1;
                            return;
                        }
                        this.characterAction = 2;
                    } else {
                        this.characterAction = -1;
                    }
                }

                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable editable) {
                    int i4;
                    int i5;
                    if (PhoneView.this.ignoreOnPhoneChange) {
                        return;
                    }
                    int selectionStart = PhoneView.this.phoneField.getSelectionStart();
                    String obj = PhoneView.this.phoneField.getText().toString();
                    if (this.characterAction == 3) {
                        obj = obj.substring(0, this.actionPosition) + obj.substring(this.actionPosition + 1);
                        selectionStart--;
                    }
                    StringBuilder sb = new StringBuilder(obj.length());
                    int i6 = 0;
                    while (i6 < obj.length()) {
                        int i7 = i6 + 1;
                        String substring = obj.substring(i6, i7);
                        if ("0123456789".contains(substring)) {
                            sb.append(substring);
                        }
                        i6 = i7;
                    }
                    PhoneView.this.ignoreOnPhoneChange = true;
                    String hintText = PhoneView.this.phoneField.getHintText();
                    if (hintText != null) {
                        int i8 = 0;
                        while (true) {
                            if (i8 >= sb.length()) {
                                break;
                            } else if (i8 < hintText.length()) {
                                if (hintText.charAt(i8) == ' ') {
                                    sb.insert(i8, ' ');
                                    i8++;
                                    if (selectionStart == i8 && (i5 = this.characterAction) != 2 && i5 != 3) {
                                        selectionStart++;
                                    }
                                }
                                i8++;
                            } else {
                                sb.insert(i8, ' ');
                                if (selectionStart == i8 + 1 && (i4 = this.characterAction) != 2 && i4 != 3) {
                                    selectionStart++;
                                }
                            }
                        }
                    }
                    editable.replace(0, editable.length(), sb);
                    if (selectionStart >= 0) {
                        PhoneView.this.phoneField.setSelection(Math.min(selectionStart, PhoneView.this.phoneField.length()));
                    }
                    PhoneView.this.phoneField.onTextChange();
                    PhoneView.this.invalidateCountryHint();
                    PhoneView.this.ignoreOnPhoneChange = false;
                }
            });
            this.phoneField.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda6
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView4, int i4, KeyEvent keyEvent) {
                    boolean lambda$new$6;
                    lambda$new$6 = LoginActivity.PhoneView.this.lambda$new$6(textView4, i4, keyEvent);
                    return lambda$new$6;
                }
            });
            int i4 = 72;
            int i5 = 56;
            if (LoginActivity.this.newAccount && LoginActivity.this.activityMode == 0) {
                CheckBoxCell checkBoxCell = new CheckBoxCell(context, 2);
                this.syncContactsBox = checkBoxCell;
                checkBoxCell.setText(LocaleController.getString("SyncContacts", R.string.SyncContacts), "", LoginActivity.this.syncContacts, false);
                addView(this.syncContactsBox, LayoutHelper.createLinear(-2, -1, 51, 16, 0, 16 + ((LocaleController.isRTL && AndroidUtilities.isSmallScreen()) ? i3 >= 21 ? 56 : 60 : 0), 0));
                i4 = 48;
                this.syncContactsBox.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        LoginActivity.PhoneView.this.lambda$new$7(view);
                    }
                });
            }
            if (BuildVars.DEBUG_VERSION && LoginActivity.this.activityMode == 0) {
                CheckBoxCell checkBoxCell2 = new CheckBoxCell(context, 2);
                this.testBackendCheckBox = checkBoxCell2;
                checkBoxCell2.setText(LocaleController.getString(R.string.DebugTestBackend), "", LoginActivity.this.testBackend, false);
                View view = this.testBackendCheckBox;
                if (!LocaleController.isRTL || !AndroidUtilities.isSmallScreen()) {
                    i5 = 0;
                } else if (i3 < 21) {
                    i5 = 60;
                }
                addView(view, LayoutHelper.createLinear(-2, -1, 51, 16, 0, 16 + i5, 0));
                i4 -= 24;
                this.testBackendCheckBox.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        LoginActivity.PhoneView.this.lambda$new$8(view2);
                    }
                });
            }
            if (i4 > 0 && !AndroidUtilities.isSmallScreen()) {
                Space space = new Space(context);
                space.setMinimumHeight(AndroidUtilities.dp(i4));
                addView(space, LayoutHelper.createLinear(-2, -2));
            }
            final HashMap hashMap = new HashMap();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResources().getAssets().open("countries.txt")));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    String[] split = readLine.split(";");
                    CountrySelectActivity.Country country = new CountrySelectActivity.Country();
                    country.name = split[2];
                    country.code = split[0];
                    country.shortname = split[1];
                    this.countriesArray.add(0, country);
                    List<CountrySelectActivity.Country> list = this.codesMap.get(split[0]);
                    if (list == null) {
                        HashMap<String, List<CountrySelectActivity.Country>> hashMap2 = this.codesMap;
                        String str = split[0];
                        ArrayList arrayList = new ArrayList();
                        hashMap2.put(str, arrayList);
                        list = arrayList;
                    }
                    list.add(country);
                    if (split.length > 3) {
                        this.phoneFormatMap.put(split[0], Collections.singletonList(split[3]));
                    }
                    hashMap.put(split[1], split[2]);
                }
                bufferedReader.close();
            } catch (Exception e) {
                FileLog.e(e);
            }
            Collections.sort(this.countriesArray, Comparator$-CC.comparing(LoginActivity$PhoneView$$ExternalSyntheticLambda17.INSTANCE));
            try {
                TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            LoginActivity.this.getAccountInstance().getConnectionsManager().sendRequest(new TLObject() { // from class: org.telegram.tgnet.TLRPC$TL_help_getNearestDc
                public static int constructor = 531836966;

                @Override // org.telegram.tgnet.TLObject
                public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i6, boolean z) {
                    return TLRPC$TL_nearestDc.TLdeserialize(abstractSerializedData, i6, z);
                }

                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                    abstractSerializedData.writeInt32(constructor);
                }
            }, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda21
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LoginActivity.PhoneView.this.lambda$new$11(hashMap, tLObject, tLRPC$TL_error);
                }
            }, 10);
            if (this.codeField.length() == 0) {
                setCountryButtonText(null);
                this.phoneField.setHintText((String) null);
                this.countryState = 1;
            }
            if (this.codeField.length() != 0) {
                this.phoneField.requestFocus();
                AnimatedPhoneNumberEditText animatedPhoneNumberEditText3 = this.phoneField;
                animatedPhoneNumberEditText3.setSelection(animatedPhoneNumberEditText3.length());
            } else {
                this.codeField.requestFocus();
            }
            loadCountries();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ View lambda$new$0(Context context) {
            TextView textView = new TextView(context);
            textView.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(12.0f));
            textView.setTextSize(1, 16.0f);
            textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            textView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            textView.setMaxLines(1);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity((LocaleController.isRTL ? 5 : 3) | 1);
            return textView;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(View view, boolean z) {
            this.countryOutlineView.animateSelection(z ? 1.0f : 0.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$4(View view) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(true, this.countriesArray);
            countrySelectActivity.setCountrySelectActivityDelegate(new CountrySelectActivity.CountrySelectActivityDelegate() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda22
                @Override // org.telegram.ui.CountrySelectActivity.CountrySelectActivityDelegate
                public final void didSelectCountry(CountrySelectActivity.Country country) {
                    LoginActivity.PhoneView.this.lambda$new$3(country);
                }
            });
            LoginActivity.this.presentFragment(countrySelectActivity);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(CountrySelectActivity.Country country) {
            selectCountry(country);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.PhoneView.this.lambda$new$2();
                }
            }, 300L);
            this.phoneField.requestFocus();
            AnimatedPhoneNumberEditText animatedPhoneNumberEditText = this.phoneField;
            animatedPhoneNumberEditText.setSelection(animatedPhoneNumberEditText.length());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2() {
            LoginActivity.this.showKeyboard(this.phoneField);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$5(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 5) {
                this.phoneField.requestFocus();
                AnimatedPhoneNumberEditText animatedPhoneNumberEditText = this.phoneField;
                animatedPhoneNumberEditText.setSelection(animatedPhoneNumberEditText.length());
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$6(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 5) {
                if (LoginActivity.this.phoneNumberConfirmView == null) {
                    lambda$onNextPressed$14(null);
                    return true;
                }
                LoginActivity.this.phoneNumberConfirmView.popupFabContainer.callOnClick();
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$7(View view) {
            if (LoginActivity.this.getParentActivity() == null) {
                return;
            }
            LoginActivity loginActivity = LoginActivity.this;
            loginActivity.syncContacts = !loginActivity.syncContacts;
            ((CheckBoxCell) view).setChecked(LoginActivity.this.syncContacts, true);
            if (LoginActivity.this.syncContacts) {
                BulletinFactory.of(LoginActivity.this.slideViewsContainer, null).createSimpleBulletin(R.raw.contacts_sync_on, LocaleController.getString("SyncContactsOn", R.string.SyncContactsOn)).show();
            } else {
                BulletinFactory.of(LoginActivity.this.slideViewsContainer, null).createSimpleBulletin(R.raw.contacts_sync_off, LocaleController.getString("SyncContactsOff", R.string.SyncContactsOff)).show();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$8(View view) {
            if (LoginActivity.this.getParentActivity() == null) {
                return;
            }
            LoginActivity loginActivity = LoginActivity.this;
            boolean z = true;
            loginActivity.testBackend = !loginActivity.testBackend;
            ((CheckBoxCell) view).setChecked(LoginActivity.this.testBackend, true);
            if (((BuildVars.DEBUG_VERSION && LoginActivity.this.getConnectionsManager().isTestBackend()) ? false : false) != LoginActivity.this.testBackend) {
                LoginActivity.this.getConnectionsManager().switchBackend(false);
            }
            loadCountries();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$11(final HashMap hashMap, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.PhoneView.this.lambda$new$10(tLObject, hashMap);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$10(TLObject tLObject, HashMap hashMap) {
            if (tLObject == null) {
                return;
            }
            TLRPC$TL_nearestDc tLRPC$TL_nearestDc = (TLRPC$TL_nearestDc) tLObject;
            if (this.codeField.length() == 0) {
                setCountry(hashMap, tLRPC$TL_nearestDc.country.toUpperCase());
            }
        }

        private void loadCountries() {
            TLRPC$TL_help_getCountriesList tLRPC$TL_help_getCountriesList = new TLRPC$TL_help_getCountriesList();
            tLRPC$TL_help_getCountriesList.lang_code = LocaleController.getInstance().getCurrentLocaleInfo() != null ? LocaleController.getInstance().getCurrentLocaleInfo().getLangCode() : Locale.getDefault().getCountry();
            LoginActivity.this.getConnectionsManager().sendRequest(tLRPC$TL_help_getCountriesList, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda18
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LoginActivity.PhoneView.this.lambda$loadCountries$13(tLObject, tLRPC$TL_error);
                }
            }, 10);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$loadCountries$13(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda14
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.PhoneView.this.lambda$loadCountries$12(tLRPC$TL_error, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$loadCountries$12(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            boolean z;
            CountrySelectActivity.Country country;
            if (tLRPC$TL_error == null) {
                this.countriesArray.clear();
                this.codesMap.clear();
                this.phoneFormatMap.clear();
                TLRPC$TL_help_countriesList tLRPC$TL_help_countriesList = (TLRPC$TL_help_countriesList) tLObject;
                for (int i = 0; i < tLRPC$TL_help_countriesList.countries.size(); i++) {
                    TLRPC$TL_help_country tLRPC$TL_help_country = tLRPC$TL_help_countriesList.countries.get(i);
                    for (int i2 = 0; i2 < tLRPC$TL_help_country.country_codes.size(); i2++) {
                        TLRPC$TL_help_countryCode tLRPC$TL_help_countryCode = tLRPC$TL_help_country.country_codes.get(i2);
                        if (tLRPC$TL_help_countryCode != null) {
                            CountrySelectActivity.Country country2 = new CountrySelectActivity.Country();
                            String str = tLRPC$TL_help_country.name;
                            country2.name = str;
                            String str2 = tLRPC$TL_help_country.default_name;
                            country2.defaultName = str2;
                            if (str == null && str2 != null) {
                                country2.name = str2;
                            }
                            country2.code = tLRPC$TL_help_countryCode.country_code;
                            country2.shortname = tLRPC$TL_help_country.iso2;
                            this.countriesArray.add(country2);
                            List<CountrySelectActivity.Country> list = this.codesMap.get(tLRPC$TL_help_countryCode.country_code);
                            if (list == null) {
                                HashMap<String, List<CountrySelectActivity.Country>> hashMap = this.codesMap;
                                String str3 = tLRPC$TL_help_countryCode.country_code;
                                ArrayList arrayList = new ArrayList();
                                hashMap.put(str3, arrayList);
                                list = arrayList;
                            }
                            list.add(country2);
                            if (tLRPC$TL_help_countryCode.patterns.size() > 0) {
                                this.phoneFormatMap.put(tLRPC$TL_help_countryCode.country_code, tLRPC$TL_help_countryCode.patterns);
                            }
                        }
                    }
                }
                if (LoginActivity.this.activityMode == 2) {
                    String stripExceptNumbers = PhoneFormat.stripExceptNumbers(UserConfig.getInstance(((BaseFragment) LoginActivity.this).currentAccount).getClientPhone());
                    if (TextUtils.isEmpty(stripExceptNumbers)) {
                        return;
                    }
                    int i3 = 4;
                    if (stripExceptNumbers.length() > 4) {
                        while (true) {
                            if (i3 < 1) {
                                z = false;
                                break;
                            }
                            String substring = stripExceptNumbers.substring(0, i3);
                            List<CountrySelectActivity.Country> list2 = this.codesMap.get(substring);
                            CountrySelectActivity.Country country3 = null;
                            if (list2 != null) {
                                if (list2.size() > 1) {
                                    String string = MessagesController.getGlobalMainSettings().getString("phone_code_last_matched_" + substring, null);
                                    if (string != null) {
                                        country = list2.get(list2.size() - 1);
                                        Iterator<CountrySelectActivity.Country> it = this.countriesArray.iterator();
                                        while (true) {
                                            if (!it.hasNext()) {
                                                break;
                                            }
                                            CountrySelectActivity.Country next = it.next();
                                            if (Objects.equals(next.shortname, string)) {
                                                country = next;
                                                break;
                                            }
                                        }
                                    } else {
                                        country = list2.get(list2.size() - 1);
                                    }
                                    country3 = country;
                                } else {
                                    country3 = list2.get(0);
                                }
                            }
                            if (country3 != null) {
                                this.codeField.setText(substring);
                                z = true;
                                break;
                            }
                            i3--;
                        }
                        if (z) {
                            return;
                        }
                        this.codeField.setText(stripExceptNumbers.substring(0, 1));
                    }
                }
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void updateColors() {
            this.titleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.subtitleView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            for (int i = 0; i < this.countryButton.getChildCount(); i++) {
                TextView textView = (TextView) this.countryButton.getChildAt(i);
                textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                textView.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            }
            this.chevronRight.setColorFilter(Theme.getColor("windowBackgroundWhiteHintText"));
            this.chevronRight.setBackground(Theme.createSelectorDrawable(LoginActivity.this.getThemedColor("listSelectorSDK21"), 1));
            this.plusTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.codeField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.codeField.setCursorColor(Theme.getColor("windowBackgroundWhiteInputFieldActivated"));
            this.codeDividerView.setBackgroundColor(Theme.getColor("windowBackgroundWhiteInputField"));
            this.phoneField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.phoneField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.phoneField.setCursorColor(Theme.getColor("windowBackgroundWhiteInputFieldActivated"));
            CheckBoxCell checkBoxCell = this.syncContactsBox;
            if (checkBoxCell != null) {
                checkBoxCell.setSquareCheckBoxColor("checkboxSquareUnchecked", "checkboxSquareBackground", "checkboxSquareCheck");
                this.syncContactsBox.updateTextColor();
            }
            CheckBoxCell checkBoxCell2 = this.testBackendCheckBox;
            if (checkBoxCell2 != null) {
                checkBoxCell2.setSquareCheckBoxColor("checkboxSquareUnchecked", "checkboxSquareBackground", "checkboxSquareCheck");
                this.testBackendCheckBox.updateTextColor();
            }
            this.phoneOutlineView.updateColor();
            this.countryOutlineView.updateColor();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        }

        public void selectCountry(CountrySelectActivity.Country country) {
            this.ignoreOnTextChange = true;
            String str = country.code;
            this.codeField.setText(str);
            setCountryHint(str, country);
            this.currentCountry = country;
            this.countryState = 0;
            this.ignoreOnTextChange = false;
            SharedPreferences.Editor edit = MessagesController.getGlobalMainSettings().edit();
            edit.putString("phone_code_last_matched_" + country.code, country.shortname).apply();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCountryHint(String str, CountrySelectActivity.Country country) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            String languageFlag = LocaleController.getLanguageFlag(country.shortname);
            if (languageFlag != null) {
                spannableStringBuilder.append((CharSequence) languageFlag).append((CharSequence) " ");
                spannableStringBuilder.setSpan(new ReplacementSpan(this) { // from class: org.telegram.ui.LoginActivity.PhoneView.5
                    @Override // android.text.style.ReplacementSpan
                    public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
                    }

                    @Override // android.text.style.ReplacementSpan
                    public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
                        return AndroidUtilities.dp(16.0f);
                    }
                }, languageFlag.length(), languageFlag.length() + 1, 0);
            }
            spannableStringBuilder.append((CharSequence) country.name);
            setCountryButtonText(Emoji.replaceEmoji(spannableStringBuilder, this.countryButton.getCurrentView().getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false));
            this.countryCodeForHint = str;
            this.wasCountryHintIndex = -1;
            invalidateCountryHint();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x008d, code lost:
            if (r7 == (-1)) goto L29;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void invalidateCountryHint() {
            int i;
            String str = this.countryCodeForHint;
            String replace = this.phoneField.getText() != null ? this.phoneField.getText().toString().replace(" ", "") : "";
            if (this.phoneFormatMap.get(str) != null && !this.phoneFormatMap.get(str).isEmpty()) {
                List<String> list = this.phoneFormatMap.get(str);
                int i2 = 0;
                if (!replace.isEmpty()) {
                    i = 0;
                    while (i < list.size()) {
                        if (replace.startsWith(list.get(i).replace(" ", "").replace("X", "").replace("0", ""))) {
                            break;
                        }
                        i++;
                    }
                }
                i = -1;
                if (i == -1) {
                    for (int i3 = 0; i3 < list.size(); i3++) {
                        String str2 = list.get(i3);
                        if (str2.startsWith("X") || str2.startsWith("0")) {
                            i = i3;
                            break;
                        }
                    }
                }
                i2 = i;
                if (this.wasCountryHintIndex != i2) {
                    String str3 = this.phoneFormatMap.get(str).get(i2);
                    int selectionStart = this.phoneField.getSelectionStart();
                    int selectionEnd = this.phoneField.getSelectionEnd();
                    this.phoneField.setHintText(str3 != null ? str3.replace('X', '0') : null);
                    this.phoneField.setSelection(selectionStart, selectionEnd);
                    this.wasCountryHintIndex = i2;
                }
            } else if (this.wasCountryHintIndex != -1) {
                int selectionStart2 = this.phoneField.getSelectionStart();
                int selectionEnd2 = this.phoneField.getSelectionEnd();
                this.phoneField.setHintText((String) null);
                this.phoneField.setSelection(selectionStart2, selectionEnd2);
                this.wasCountryHintIndex = -1;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setCountryButtonText(CharSequence charSequence) {
            Animation loadAnimation = AnimationUtils.loadAnimation(ApplicationLoader.applicationContext, (this.countryButton.getCurrentView().getText() == null || charSequence != null) ? R.anim.text_out : R.anim.text_out_down);
            loadAnimation.setInterpolator(Easings.easeInOutQuad);
            this.countryButton.setOutAnimation(loadAnimation);
            CharSequence text = this.countryButton.getCurrentView().getText();
            this.countryButton.setText(charSequence, ((TextUtils.isEmpty(charSequence) && TextUtils.isEmpty(text)) || Objects.equals(text, charSequence)) ? false : true);
            this.countryOutlineView.animateSelection(charSequence != null ? 1.0f : 0.0f);
        }

        private void setCountry(HashMap<String, String> hashMap, String str) {
            if (hashMap.get(str) == null || this.countriesArray == null) {
                return;
            }
            CountrySelectActivity.Country country = null;
            int i = 0;
            while (true) {
                if (i < this.countriesArray.size()) {
                    if (this.countriesArray.get(i) != null && this.countriesArray.get(i).name.equals(str)) {
                        country = this.countriesArray.get(i);
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            if (country != null) {
                this.codeField.setText(country.code);
                this.countryState = 0;
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onCancelPressed() {
            this.nextPressed = false;
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (this.ignoreSelection) {
                this.ignoreSelection = false;
                return;
            }
            this.ignoreOnTextChange = true;
            this.codeField.setText(this.countriesArray.get(i).code);
            this.ignoreOnTextChange = false;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:202:0x04a2  */
        /* JADX WARN: Removed duplicated region for block: B:203:0x04ac  */
        @Override // org.telegram.ui.Components.SlideView
        /* renamed from: onNextPressed */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void lambda$onNextPressed$14(final String str) {
            String str2;
            int i;
            boolean z;
            boolean z2;
            boolean z3;
            TLRPC$TL_auth_sendCode tLRPC$TL_auth_sendCode;
            final Bundle bundle;
            String str3;
            boolean z4;
            String line1Number;
            boolean z5;
            int i2;
            if (LoginActivity.this.getParentActivity() == null || this.nextPressed || LoginActivity.this.isRequestingFirebaseSms) {
                return;
            }
            TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
            if (BuildVars.DEBUG_VERSION) {
                FileLog.d("sim status = " + telephonyManager.getSimState());
            }
            if (this.codeField.length() == 0 || this.phoneField.length() == 0) {
                LoginActivity.this.onFieldError(this.phoneOutlineView, false);
                return;
            }
            String str4 = "+" + ((Object) this.codeField.getText()) + " " + ((Object) this.phoneField.getText());
            if (!this.confirmedNumber) {
                Point point = AndroidUtilities.displaySize;
                if (point.x <= point.y || LoginActivity.this.isCustomKeyboardVisible() || LoginActivity.this.sizeNotifierFrameLayout.measureKeyboardHeight() <= AndroidUtilities.dp(20.0f)) {
                    LoginActivity.this.phoneNumberConfirmView = new PhoneNumberConfirmView(((BaseFragment) LoginActivity.this).fragmentView.getContext(), (ViewGroup) ((BaseFragment) LoginActivity.this).fragmentView, LoginActivity.this.floatingButtonContainer, str4, new 6(str));
                    LoginActivity.this.phoneNumberConfirmView.show();
                    return;
                }
                LoginActivity.this.keyboardHideCallback = new Runnable() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.PhoneView.this.lambda$onNextPressed$15(str);
                    }
                };
                AndroidUtilities.hideKeyboard(((BaseFragment) LoginActivity.this).fragmentView);
                return;
            }
            this.confirmedNumber = false;
            if (LoginActivity.this.phoneNumberConfirmView != null) {
                LoginActivity.this.phoneNumberConfirmView.dismiss();
            }
            boolean isSimAvailable = AndroidUtilities.isSimAvailable();
            int i3 = Build.VERSION.SDK_INT;
            if (i3 < 23 || !isSimAvailable) {
                str2 = "ephone";
                i = 1;
                z = true;
                z2 = true;
                z3 = true;
            } else {
                z = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0;
                z2 = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.CALL_PHONE") == 0;
                z3 = i3 < 28 || LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_CALL_LOG") == 0;
                if (i3 >= 26) {
                    z5 = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_NUMBERS") == 0;
                    str2 = "ephone";
                } else {
                    str2 = "ephone";
                    z5 = true;
                }
                if (LoginActivity.this.checkPermissions) {
                    LoginActivity.this.permissionsItems.clear();
                    if (!z) {
                        LoginActivity.this.permissionsItems.add("android.permission.READ_PHONE_STATE");
                    }
                    if (!z2) {
                        LoginActivity.this.permissionsItems.add("android.permission.CALL_PHONE");
                    }
                    if (!z3) {
                        LoginActivity.this.permissionsItems.add("android.permission.READ_CALL_LOG");
                    }
                    if (!z5 && i3 >= 26) {
                        LoginActivity.this.permissionsItems.add("android.permission.READ_PHONE_NUMBERS");
                    }
                    if (!LoginActivity.this.permissionsItems.isEmpty()) {
                        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                        if (!globalMainSettings.getBoolean("firstlogin", true) && !LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE") && !LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_CALL_LOG")) {
                            try {
                                LoginActivity.this.getParentActivity().requestPermissions((String[]) LoginActivity.this.permissionsItems.toArray(new String[0]), 6);
                                return;
                            } catch (Exception e) {
                                FileLog.e(e);
                                return;
                            }
                        }
                        globalMainSettings.edit().putBoolean("firstlogin", false).commit();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                        builder.setPositiveButton(LocaleController.getString("Continue", R.string.Continue), null);
                        if (!z && (!z2 || !z3)) {
                            builder.setMessage(LocaleController.getString("AllowReadCallAndLog", R.string.AllowReadCallAndLog));
                            i2 = R.raw.calls_log;
                        } else if (!z2 || !z3) {
                            builder.setMessage(LocaleController.getString("AllowReadCallLog", R.string.AllowReadCallLog));
                            i2 = R.raw.calls_log;
                        } else {
                            builder.setMessage(LocaleController.getString("AllowReadCall", R.string.AllowReadCall));
                            i2 = R.raw.incoming_calls;
                        }
                        builder.setTopAnimation(i2, 46, false, Theme.getColor("dialogTopBackground"));
                        LoginActivity loginActivity = LoginActivity.this;
                        loginActivity.permissionsDialog = loginActivity.showDialog(builder.create());
                        this.confirmedNumber = true;
                        return;
                    }
                }
                i = 1;
            }
            int i4 = this.countryState;
            if (i4 == i) {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("ChooseCountry", R.string.ChooseCountry));
                LoginActivity.this.needHideProgress(false);
            } else if (i4 == 2 && !BuildVars.DEBUG_VERSION) {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("WrongCountry", R.string.WrongCountry));
                LoginActivity.this.needHideProgress(false);
            } else {
                final String stripExceptNumbers = PhoneFormat.stripExceptNumbers("" + ((Object) this.codeField.getText()) + ((Object) this.phoneField.getText()));
                if (LoginActivity.this.activityMode == 0 && (LoginActivity.this.getParentActivity() instanceof LaunchActivity)) {
                    for (final int i5 = 0; i5 < 4; i5++) {
                        UserConfig userConfig = UserConfig.getInstance(i5);
                        if (userConfig.isClientActivated() && PhoneNumberUtils.compare(stripExceptNumbers, userConfig.getCurrentUser().phone) && ConnectionsManager.getInstance(i5).isTestBackend() == LoginActivity.this.testBackend) {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                            builder2.setTitle(LocaleController.getString(R.string.AppName));
                            builder2.setMessage(LocaleController.getString("AccountAlreadyLoggedIn", R.string.AccountAlreadyLoggedIn));
                            builder2.setPositiveButton(LocaleController.getString("AccountSwitch", R.string.AccountSwitch), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda0
                                @Override // android.content.DialogInterface.OnClickListener
                                public final void onClick(DialogInterface dialogInterface, int i6) {
                                    LoginActivity.PhoneView.this.lambda$onNextPressed$16(i5, dialogInterface, i6);
                                }
                            });
                            builder2.setNegativeButton(LocaleController.getString("OK", R.string.OK), null);
                            LoginActivity.this.showDialog(builder2.create());
                            LoginActivity.this.needHideProgress(false);
                            return;
                        }
                    }
                }
                TLRPC$TL_codeSettings tLRPC$TL_codeSettings = new TLRPC$TL_codeSettings();
                tLRPC$TL_codeSettings.allow_flashcall = isSimAvailable && z && z2 && z3;
                tLRPC$TL_codeSettings.allow_missed_call = isSimAvailable && z;
                boolean hasServices = PushListenerController.GooglePushListenerServiceProvider.INSTANCE.hasServices();
                tLRPC$TL_codeSettings.allow_firebase = hasServices;
                tLRPC$TL_codeSettings.allow_app_hash = hasServices;
                if (LoginActivity.this.forceDisableSafetyNet || TextUtils.isEmpty(BuildVars.SAFETYNET_KEY)) {
                    tLRPC$TL_codeSettings.allow_firebase = false;
                }
                ArrayList<TLRPC$TL_auth_authorization> savedLogInTokens = AuthTokensHelper.getSavedLogInTokens();
                if (savedLogInTokens != null) {
                    for (int i6 = 0; i6 < savedLogInTokens.size(); i6++) {
                        if (savedLogInTokens.get(i6).future_auth_token != null) {
                            if (tLRPC$TL_codeSettings.logout_tokens == null) {
                                tLRPC$TL_codeSettings.logout_tokens = new ArrayList<>();
                            }
                            if (BuildVars.DEBUG_VERSION) {
                                FileLog.d("login token to check " + new String(savedLogInTokens.get(i6).future_auth_token, StandardCharsets.UTF_8));
                            }
                            tLRPC$TL_codeSettings.logout_tokens.add(savedLogInTokens.get(i6).future_auth_token);
                            if (tLRPC$TL_codeSettings.logout_tokens.size() >= 20) {
                                break;
                            }
                        }
                    }
                }
                ArrayList<TLRPC$TL_auth_loggedOut> savedLogOutTokens = AuthTokensHelper.getSavedLogOutTokens();
                if (savedLogOutTokens != null) {
                    for (int i7 = 0; i7 < savedLogOutTokens.size(); i7++) {
                        if (tLRPC$TL_codeSettings.logout_tokens == null) {
                            tLRPC$TL_codeSettings.logout_tokens = new ArrayList<>();
                        }
                        tLRPC$TL_codeSettings.logout_tokens.add(savedLogOutTokens.get(i7).future_auth_token);
                        if (tLRPC$TL_codeSettings.logout_tokens.size() >= 20) {
                            break;
                        }
                    }
                    AuthTokensHelper.saveLogOutTokens(savedLogOutTokens);
                }
                if (tLRPC$TL_codeSettings.logout_tokens != null) {
                    tLRPC$TL_codeSettings.flags |= 64;
                }
                SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                sharedPreferences.edit().remove("sms_hash_code").apply();
                if (tLRPC$TL_codeSettings.allow_app_hash) {
                    sharedPreferences.edit().putString("sms_hash", BuildVars.SMS_HASH).apply();
                } else {
                    sharedPreferences.edit().remove("sms_hash").apply();
                }
                if (tLRPC$TL_codeSettings.allow_flashcall) {
                    try {
                        line1Number = telephonyManager.getLine1Number();
                        z4 = TextUtils.isEmpty(line1Number);
                    } catch (Exception e2) {
                        e = e2;
                        z4 = false;
                    }
                    try {
                        if (!z4) {
                            boolean compare = PhoneNumberUtils.compare(stripExceptNumbers, line1Number);
                            tLRPC$TL_codeSettings.current_number = compare;
                            if (!compare) {
                                z4 = false;
                                tLRPC$TL_codeSettings.allow_flashcall = false;
                            }
                        } else if (UserConfig.getActivatedAccountsCount() > 0) {
                            z4 = false;
                            tLRPC$TL_codeSettings.allow_flashcall = false;
                        } else {
                            z4 = false;
                            tLRPC$TL_codeSettings.current_number = false;
                        }
                    } catch (Exception e3) {
                        e = e3;
                        tLRPC$TL_codeSettings.allow_flashcall = z4;
                        FileLog.e(e);
                        if (LoginActivity.this.activityMode == 2) {
                        }
                        final TLRPC$TL_auth_sendCode tLRPC$TL_auth_sendCode2 = tLRPC$TL_auth_sendCode;
                        bundle = new Bundle();
                        bundle.putString("phone", "+" + ((Object) this.codeField.getText()) + " " + ((Object) this.phoneField.getText()));
                        str3 = str2;
                        bundle.putString(str3, "+" + PhoneFormat.stripExceptNumbers(this.codeField.getText().toString()) + " " + PhoneFormat.stripExceptNumbers(this.phoneField.getText().toString()));
                        bundle.putString("phoneFormated", stripExceptNumbers);
                        this.nextPressed = true;
                        final PhoneInputData phoneInputData = new PhoneInputData();
                        phoneInputData.phoneNumber = "+" + ((Object) this.codeField.getText()) + " " + ((Object) this.phoneField.getText());
                        phoneInputData.country = this.currentCountry;
                        phoneInputData.patterns = this.phoneFormatMap.get(this.codeField.getText().toString());
                        LoginActivity.this.needShowProgress(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_sendCode2, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda19
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                                LoginActivity.PhoneView.this.lambda$onNextPressed$20(bundle, stripExceptNumbers, phoneInputData, tLRPC$TL_auth_sendCode2, tLObject, tLRPC$TL_error);
                            }
                        }, 27));
                    }
                }
                if (LoginActivity.this.activityMode == 2) {
                    ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).cleanup(false);
                    TLRPC$TL_auth_sendCode tLRPC$TL_auth_sendCode3 = new TLRPC$TL_auth_sendCode();
                    tLRPC$TL_auth_sendCode3.api_hash = BuildVars.APP_HASH;
                    tLRPC$TL_auth_sendCode3.api_id = BuildVars.APP_ID;
                    tLRPC$TL_auth_sendCode3.phone_number = stripExceptNumbers;
                    tLRPC$TL_auth_sendCode3.settings = tLRPC$TL_codeSettings;
                    tLRPC$TL_auth_sendCode = tLRPC$TL_auth_sendCode3;
                } else {
                    TLRPC$TL_account_sendChangePhoneCode tLRPC$TL_account_sendChangePhoneCode = new TLRPC$TL_account_sendChangePhoneCode();
                    tLRPC$TL_account_sendChangePhoneCode.phone_number = stripExceptNumbers;
                    tLRPC$TL_account_sendChangePhoneCode.settings = tLRPC$TL_codeSettings;
                    tLRPC$TL_auth_sendCode = tLRPC$TL_account_sendChangePhoneCode;
                }
                final TLObject tLRPC$TL_auth_sendCode22 = tLRPC$TL_auth_sendCode;
                bundle = new Bundle();
                bundle.putString("phone", "+" + ((Object) this.codeField.getText()) + " " + ((Object) this.phoneField.getText()));
                try {
                    str3 = str2;
                } catch (Exception e4) {
                    e = e4;
                    str3 = str2;
                }
                try {
                    bundle.putString(str3, "+" + PhoneFormat.stripExceptNumbers(this.codeField.getText().toString()) + " " + PhoneFormat.stripExceptNumbers(this.phoneField.getText().toString()));
                } catch (Exception e5) {
                    e = e5;
                    FileLog.e(e);
                    bundle.putString(str3, "+" + stripExceptNumbers);
                    bundle.putString("phoneFormated", stripExceptNumbers);
                    this.nextPressed = true;
                    final PhoneInputData phoneInputData2 = new PhoneInputData();
                    phoneInputData2.phoneNumber = "+" + ((Object) this.codeField.getText()) + " " + ((Object) this.phoneField.getText());
                    phoneInputData2.country = this.currentCountry;
                    phoneInputData2.patterns = this.phoneFormatMap.get(this.codeField.getText().toString());
                    LoginActivity.this.needShowProgress(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_sendCode22, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda19
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LoginActivity.PhoneView.this.lambda$onNextPressed$20(bundle, stripExceptNumbers, phoneInputData2, tLRPC$TL_auth_sendCode22, tLObject, tLRPC$TL_error);
                        }
                    }, 27));
                }
                bundle.putString("phoneFormated", stripExceptNumbers);
                this.nextPressed = true;
                final PhoneInputData phoneInputData22 = new PhoneInputData();
                phoneInputData22.phoneNumber = "+" + ((Object) this.codeField.getText()) + " " + ((Object) this.phoneField.getText());
                phoneInputData22.country = this.currentCountry;
                phoneInputData22.patterns = this.phoneFormatMap.get(this.codeField.getText().toString());
                LoginActivity.this.needShowProgress(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_sendCode22, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda19
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LoginActivity.PhoneView.this.lambda$onNextPressed$20(bundle, stripExceptNumbers, phoneInputData22, tLRPC$TL_auth_sendCode22, tLObject, tLRPC$TL_error);
                    }
                }, 27));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$15(final String str) {
            postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.PhoneView.this.lambda$onNextPressed$14(str);
                }
            }, 200L);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public class 6 implements PhoneNumberConfirmView.IConfirmDialogCallback {
            final /* synthetic */ String val$code;

            6(String str) {
                this.val$code = str;
            }

            @Override // org.telegram.ui.LoginActivity.PhoneNumberConfirmView.IConfirmDialogCallback
            public void onFabPressed(PhoneNumberConfirmView phoneNumberConfirmView, TransformableLoginButtonView transformableLoginButtonView) {
                onConfirm(phoneNumberConfirmView);
            }

            @Override // org.telegram.ui.LoginActivity.PhoneNumberConfirmView.IConfirmDialogCallback
            public void onEditPressed(PhoneNumberConfirmView phoneNumberConfirmView, TextView textView) {
                phoneNumberConfirmView.dismiss();
            }

            @Override // org.telegram.ui.LoginActivity.PhoneNumberConfirmView.IConfirmDialogCallback
            public void onConfirmPressed(PhoneNumberConfirmView phoneNumberConfirmView, TextView textView) {
                onConfirm(phoneNumberConfirmView);
            }

            @Override // org.telegram.ui.LoginActivity.PhoneNumberConfirmView.IConfirmDialogCallback
            public void onDismiss(PhoneNumberConfirmView phoneNumberConfirmView) {
                LoginActivity.this.phoneNumberConfirmView = null;
            }

            private void onConfirm(final PhoneNumberConfirmView phoneNumberConfirmView) {
                int i;
                PhoneView.this.confirmedNumber = true;
                LoginActivity.this.currentDoneType = 0;
                LoginActivity.this.needShowProgress(0, false);
                int i2 = Build.VERSION.SDK_INT;
                if (i2 >= 23 && AndroidUtilities.isSimAvailable()) {
                    boolean z = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0;
                    boolean z2 = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.CALL_PHONE") == 0;
                    boolean z3 = i2 < 28 || LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_CALL_LOG") == 0;
                    boolean z4 = i2 < 26 || LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_NUMBERS") == 0;
                    if (LoginActivity.this.checkPermissions) {
                        LoginActivity.this.permissionsItems.clear();
                        if (!z) {
                            LoginActivity.this.permissionsItems.add("android.permission.READ_PHONE_STATE");
                        }
                        if (!z2) {
                            LoginActivity.this.permissionsItems.add("android.permission.CALL_PHONE");
                        }
                        if (!z3) {
                            LoginActivity.this.permissionsItems.add("android.permission.READ_CALL_LOG");
                        }
                        if (!z4 && i2 >= 26) {
                            LoginActivity.this.permissionsItems.add("android.permission.READ_PHONE_NUMBERS");
                        }
                        if (!LoginActivity.this.permissionsItems.isEmpty()) {
                            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                            if (!globalMainSettings.getBoolean("firstlogin", true) && !LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE") && !LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_CALL_LOG")) {
                                try {
                                    LoginActivity.this.getParentActivity().requestPermissions((String[]) LoginActivity.this.permissionsItems.toArray(new String[0]), 6);
                                    return;
                                } catch (Exception e) {
                                    FileLog.e(e);
                                    return;
                                }
                            }
                            globalMainSettings.edit().putBoolean("firstlogin", false).commit();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                            builder.setPositiveButton(LocaleController.getString("Continue", R.string.Continue), null);
                            if (!z && (!z2 || !z3)) {
                                builder.setMessage(LocaleController.getString("AllowReadCallAndLog", R.string.AllowReadCallAndLog));
                                i = R.raw.calls_log;
                            } else if (!z2 || !z3) {
                                builder.setMessage(LocaleController.getString("AllowReadCallLog", R.string.AllowReadCallLog));
                                i = R.raw.calls_log;
                            } else {
                                builder.setMessage(LocaleController.getString("AllowReadCall", R.string.AllowReadCall));
                                i = R.raw.incoming_calls;
                            }
                            builder.setTopAnimation(i, 46, false, Theme.getColor("dialogTopBackground"));
                            LoginActivity loginActivity = LoginActivity.this;
                            loginActivity.permissionsDialog = loginActivity.showDialog(builder.create());
                            PhoneView.this.confirmedNumber = true;
                            return;
                        }
                    }
                }
                final String str = this.val$code;
                phoneNumberConfirmView.animateProgress(new Runnable() { // from class: org.telegram.ui.LoginActivity$PhoneView$6$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.PhoneView.6.this.lambda$onConfirm$1(phoneNumberConfirmView, str);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onConfirm$1(final PhoneNumberConfirmView phoneNumberConfirmView, final String str) {
                phoneNumberConfirmView.dismiss();
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$PhoneView$6$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.PhoneView.6.this.lambda$onConfirm$0(str, phoneNumberConfirmView);
                    }
                }, 150L);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onConfirm$0(String str, PhoneNumberConfirmView phoneNumberConfirmView) {
                PhoneView.this.lambda$onNextPressed$14(str);
                LoginActivity.this.floatingProgressView.sync(phoneNumberConfirmView.floatingProgressView);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$16(int i, DialogInterface dialogInterface, int i2) {
            if (UserConfig.selectedAccount != i) {
                ((LaunchActivity) LoginActivity.this.getParentActivity()).switchToAccount(i, false);
            }
            LoginActivity.this.finishFragment();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$20(final Bundle bundle, final String str, final PhoneInputData phoneInputData, final TLObject tLObject, final TLObject tLObject2, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.PhoneView.this.lambda$onNextPressed$19(tLRPC$TL_error, tLObject2, bundle, str, phoneInputData, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$19(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, Bundle bundle, final String str, PhoneInputData phoneInputData, TLObject tLObject2) {
            this.nextPressed = false;
            if (tLRPC$TL_error == null) {
                if (!(tLObject instanceof TLRPC$TL_auth_sentCodeSuccess)) {
                    LoginActivity.this.lambda$resendCodeFromSafetyNet$18(bundle, (TLRPC$auth_SentCode) tLObject);
                } else {
                    TLRPC$auth_Authorization tLRPC$auth_Authorization = ((TLRPC$TL_auth_sentCodeSuccess) tLObject).authorization;
                    if (!(tLRPC$auth_Authorization instanceof TLRPC$TL_auth_authorizationSignUpRequired)) {
                        LoginActivity.this.onAuthSuccess((TLRPC$TL_auth_authorization) tLRPC$auth_Authorization);
                    } else {
                        TLRPC$TL_help_termsOfService tLRPC$TL_help_termsOfService = ((TLRPC$TL_auth_authorizationSignUpRequired) tLObject).terms_of_service;
                        if (tLRPC$TL_help_termsOfService != null) {
                            LoginActivity.this.currentTermsOfService = tLRPC$TL_help_termsOfService;
                        }
                        LoginActivity.this.setPage(5, true, bundle, false);
                    }
                }
            } else {
                String str2 = tLRPC$TL_error.text;
                if (str2 != null) {
                    if (str2.contains("SESSION_PASSWORD_NEEDED")) {
                        ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda20
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject3, TLRPC$TL_error tLRPC$TL_error2) {
                                LoginActivity.PhoneView.this.lambda$onNextPressed$18(str, tLObject3, tLRPC$TL_error2);
                            }
                        }, 10);
                    } else if (tLRPC$TL_error.text.contains("PHONE_NUMBER_INVALID")) {
                        LoginActivity.needShowInvalidAlert(LoginActivity.this, str, phoneInputData, false);
                    } else if (tLRPC$TL_error.text.contains("PHONE_PASSWORD_FLOOD")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("FloodWait", R.string.FloodWait));
                    } else if (tLRPC$TL_error.text.contains("PHONE_NUMBER_FLOOD")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("PhoneNumberFlood", R.string.PhoneNumberFlood));
                    } else if (tLRPC$TL_error.text.contains("PHONE_NUMBER_BANNED")) {
                        LoginActivity.needShowInvalidAlert(LoginActivity.this, str, phoneInputData, true);
                    } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("InvalidCode", R.string.InvalidCode));
                    } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("CodeExpired", R.string.CodeExpired));
                    } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("FloodWait", R.string.FloodWait));
                    } else if (tLRPC$TL_error.code != -1000) {
                        AlertsCreator.processError(((BaseFragment) LoginActivity.this).currentAccount, tLRPC$TL_error, LoginActivity.this, tLObject2, phoneInputData.phoneNumber);
                    }
                }
            }
            if (LoginActivity.this.isRequestingFirebaseSms) {
                return;
            }
            LoginActivity.this.needHideProgress(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$18(final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.PhoneView.this.lambda$onNextPressed$17(tLRPC$TL_error, tLObject, str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$17(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, String str) {
            this.nextPressed = false;
            LoginActivity.this.showDoneButton(false, true);
            if (tLRPC$TL_error != null) {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), tLRPC$TL_error.text);
                return;
            }
            TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$account_Password, true)) {
                AlertsCreator.showUpdateAppAlert(LoginActivity.this.getParentActivity(), LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
                return;
            }
            Bundle bundle = new Bundle();
            SerializedData serializedData = new SerializedData(tLRPC$account_Password.getObjectSize());
            tLRPC$account_Password.serializeToStream(serializedData);
            bundle.putString("password", Utilities.bytesToHex(serializedData.toByteArray()));
            bundle.putString("phoneFormated", str);
            LoginActivity.this.setPage(6, true, bundle, false);
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x0064 A[Catch: Exception -> 0x01b7, TryCatch #0 {Exception -> 0x01b7, blocks: (B:7:0x0010, B:9:0x0020, B:11:0x0028, B:17:0x003d, B:22:0x004d, B:26:0x0059, B:28:0x0064, B:31:0x0071, B:32:0x007a, B:34:0x0086, B:36:0x009e, B:37:0x00a4, B:40:0x00aa, B:44:0x00b8, B:46:0x00d1, B:49:0x00db, B:64:0x013b, B:68:0x014d, B:65:0x0147, B:52:0x00eb, B:54:0x00f1, B:56:0x0117, B:57:0x011d, B:59:0x0123, B:62:0x0133, B:70:0x015c, B:71:0x016f, B:73:0x0179, B:74:0x01ac), top: B:79:0x0010 }] */
        /* JADX WARN: Removed duplicated region for block: B:34:0x0086 A[Catch: Exception -> 0x01b7, TryCatch #0 {Exception -> 0x01b7, blocks: (B:7:0x0010, B:9:0x0020, B:11:0x0028, B:17:0x003d, B:22:0x004d, B:26:0x0059, B:28:0x0064, B:31:0x0071, B:32:0x007a, B:34:0x0086, B:36:0x009e, B:37:0x00a4, B:40:0x00aa, B:44:0x00b8, B:46:0x00d1, B:49:0x00db, B:64:0x013b, B:68:0x014d, B:65:0x0147, B:52:0x00eb, B:54:0x00f1, B:56:0x0117, B:57:0x011d, B:59:0x0123, B:62:0x0133, B:70:0x015c, B:71:0x016f, B:73:0x0179, B:74:0x01ac), top: B:79:0x0010 }] */
        /* JADX WARN: Removed duplicated region for block: B:84:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void fillNumber() {
            boolean z;
            boolean z2;
            boolean z3;
            CountrySelectActivity.Country country;
            if (this.numberFilled || LoginActivity.this.activityMode != 0) {
                return;
            }
            try {
                TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
                if (AndroidUtilities.isSimAvailable()) {
                    int i = Build.VERSION.SDK_INT;
                    if (i >= 23) {
                        z = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0;
                        if (i >= 26 && LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_NUMBERS") != 0) {
                            z2 = false;
                            if (LoginActivity.this.checkShowPermissions && (!z || !z2)) {
                                LoginActivity.this.permissionsShowItems.clear();
                                if (!z) {
                                    LoginActivity.this.permissionsShowItems.add("android.permission.READ_PHONE_STATE");
                                }
                                if (!z2 && i >= 26) {
                                    LoginActivity.this.permissionsShowItems.add("android.permission.READ_PHONE_NUMBERS");
                                }
                                if (LoginActivity.this.permissionsShowItems.isEmpty()) {
                                    final ArrayList arrayList = new ArrayList(LoginActivity.this.permissionsShowItems);
                                    Runnable runnable = new Runnable() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda12
                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            LoginActivity.PhoneView.this.lambda$fillNumber$21(arrayList);
                                        }
                                    };
                                    if (LoginActivity.this.isAnimatingIntro) {
                                        LoginActivity.this.animationFinishCallback = runnable;
                                        return;
                                    } else {
                                        runnable.run();
                                        return;
                                    }
                                }
                                return;
                            }
                        }
                        z2 = true;
                        if (LoginActivity.this.checkShowPermissions) {
                            LoginActivity.this.permissionsShowItems.clear();
                            if (!z) {
                            }
                            if (!z2) {
                                LoginActivity.this.permissionsShowItems.add("android.permission.READ_PHONE_NUMBERS");
                            }
                            if (LoginActivity.this.permissionsShowItems.isEmpty()) {
                            }
                        }
                    } else {
                        z = true;
                        z2 = true;
                    }
                    this.numberFilled = true;
                    if (!LoginActivity.this.newAccount && z && z2) {
                        this.codeField.setAlpha(0.0f);
                        this.phoneField.setAlpha(0.0f);
                        String stripExceptNumbers = PhoneFormat.stripExceptNumbers(telephonyManager.getLine1Number());
                        if (!TextUtils.isEmpty(stripExceptNumbers)) {
                            int i2 = 4;
                            String str = null;
                            if (stripExceptNumbers.length() > 4) {
                                while (true) {
                                    if (i2 < 1) {
                                        z3 = false;
                                        break;
                                    }
                                    String substring = stripExceptNumbers.substring(0, i2);
                                    List<CountrySelectActivity.Country> list = this.codesMap.get(substring);
                                    if (list == null) {
                                        country = null;
                                    } else if (list.size() > 1) {
                                        String string = MessagesController.getGlobalMainSettings().getString("phone_code_last_matched_" + substring, null);
                                        country = list.get(list.size() - 1);
                                        if (string != null) {
                                            Iterator<CountrySelectActivity.Country> it = this.countriesArray.iterator();
                                            while (true) {
                                                if (!it.hasNext()) {
                                                    break;
                                                }
                                                CountrySelectActivity.Country next = it.next();
                                                if (Objects.equals(next.shortname, string)) {
                                                    country = next;
                                                    break;
                                                }
                                            }
                                        }
                                    } else {
                                        country = list.get(0);
                                    }
                                    if (country != null) {
                                        String substring2 = stripExceptNumbers.substring(i2);
                                        this.codeField.setText(substring);
                                        str = substring2;
                                        z3 = true;
                                        break;
                                    }
                                    i2--;
                                }
                                if (!z3) {
                                    str = stripExceptNumbers.substring(1);
                                    this.codeField.setText(stripExceptNumbers.substring(0, 1));
                                }
                            }
                            if (str != null) {
                                this.phoneField.requestFocus();
                                this.phoneField.setText(str);
                                AnimatedPhoneNumberEditText animatedPhoneNumberEditText = this.phoneField;
                                animatedPhoneNumberEditText.setSelection(animatedPhoneNumberEditText.length());
                            }
                        }
                        if (this.phoneField.length() > 0) {
                            AnimatorSet duration = new AnimatorSet().setDuration(300L);
                            duration.playTogether(ObjectAnimator.ofFloat(this.codeField, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.phoneField, View.ALPHA, 1.0f));
                            duration.start();
                            this.confirmedNumber = true;
                            return;
                        }
                        this.codeField.setAlpha(1.0f);
                        this.phoneField.setAlpha(1.0f);
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$fillNumber$21(List list) {
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            if (globalMainSettings.getBoolean("firstloginshow", true) || LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE")) {
                globalMainSettings.edit().putBoolean("firstloginshow", false).commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                builder.setTopAnimation(R.raw.incoming_calls, 46, false, Theme.getColor("dialogTopBackground"));
                builder.setPositiveButton(LocaleController.getString("Continue", R.string.Continue), null);
                builder.setMessage(LocaleController.getString("AllowFillNumber", R.string.AllowFillNumber));
                LoginActivity loginActivity = LoginActivity.this;
                loginActivity.permissionsShowDialog = loginActivity.showDialog(builder.create(), true, null);
                LoginActivity.this.needRequestPermissions = true;
                return;
            }
            LoginActivity.this.getParentActivity().requestPermissions((String[]) list.toArray(new String[0]), 7);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onShow() {
            super.onShow();
            fillNumber();
            CheckBoxCell checkBoxCell = this.syncContactsBox;
            if (checkBoxCell != null) {
                checkBoxCell.setChecked(LoginActivity.this.syncContacts, false);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$PhoneView$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.PhoneView.this.lambda$onShow$22();
                }
            }, LoginActivity.SHOW_DELAY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShow$22() {
            if (this.phoneField != null) {
                if (LoginActivity.this.needRequestPermissions) {
                    this.codeField.clearFocus();
                    this.phoneField.clearFocus();
                } else if (this.codeField.length() != 0) {
                    this.phoneField.requestFocus();
                    if (!this.numberFilled) {
                        AnimatedPhoneNumberEditText animatedPhoneNumberEditText = this.phoneField;
                        animatedPhoneNumberEditText.setSelection(animatedPhoneNumberEditText.length());
                    }
                    LoginActivity.this.showKeyboard(this.phoneField);
                } else {
                    this.codeField.requestFocus();
                    LoginActivity.this.showKeyboard(this.codeField);
                }
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            return LocaleController.getString("YourPhone", R.string.YourPhone);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void saveStateParams(Bundle bundle) {
            String obj = this.codeField.getText().toString();
            if (obj.length() != 0) {
                bundle.putString("phoneview_code", obj);
            }
            String obj2 = this.phoneField.getText().toString();
            if (obj2.length() != 0) {
                bundle.putString("phoneview_phone", obj2);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void restoreStateParams(Bundle bundle) {
            String string = bundle.getString("phoneview_code");
            if (string != null) {
                this.codeField.setText(string);
            }
            String string2 = bundle.getString("phoneview_phone");
            if (string2 != null) {
                this.phoneField.setText(string2);
            }
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (i == NotificationCenter.emojiLoaded) {
                this.countryButton.getCurrentView().invalidate();
            }
        }
    }

    /* loaded from: classes3.dex */
    public class LoginActivitySmsView extends SlideView implements NotificationCenter.NotificationCenterDelegate {
        private RLottieImageView blueImageView;
        private FrameLayout bottomContainer;
        private String catchedPhone;
        private CodeFieldContainer codeFieldContainer;
        private int codeTime;
        private Timer codeTimer;
        private TextView confirmTextView;
        private Bundle currentParams;
        private int currentType;
        private RLottieDrawable dotsDrawable;
        private RLottieDrawable dotsToStarsDrawable;
        private String emailPhone;
        private Runnable errorColorTimeout;
        private ViewSwitcher errorViewSwitcher;
        RLottieDrawable hintDrawable;
        private boolean isDotsAnimationVisible;
        private boolean isResendingCode;
        private double lastCodeTime;
        private double lastCurrentTime;
        private String lastError;
        private int length;
        private ImageView missedCallArrowIcon;
        private TextView missedCallDescriptionSubtitle;
        private ImageView missedCallPhoneIcon;
        private boolean nextPressed;
        private int nextType;
        private LinearLayout openFragmentButton;
        private TextView openFragmentButtonText;
        private RLottieImageView openFragmentImageView;
        private int openTime;
        private String pattern;
        private String phone;
        private String phoneHash;
        private boolean postedErrorColorTimeout;
        private String prefix;
        private TextView prefixTextView;
        private FrameLayout problemFrame;
        private TextView problemText;
        private ProgressView progressView;
        private String requestPhone;
        private RLottieDrawable starsToDotsDrawable;
        private int time;
        private TextView timeText;
        private Timer timeTimer;
        private final Object timerSync;
        private TextView titleTextView;
        private String url;
        private boolean waitingForEvent;
        private TextView wrongCode;

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onBackPressed$40(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        static /* synthetic */ int access$9226(LoginActivitySmsView loginActivitySmsView, double d) {
            double d2 = loginActivitySmsView.codeTime;
            Double.isNaN(d2);
            int i = (int) (d2 - d);
            loginActivitySmsView.codeTime = i;
            return i;
        }

        static /* synthetic */ int access$9826(LoginActivitySmsView loginActivitySmsView, double d) {
            double d2 = loginActivitySmsView.time;
            Double.isNaN(d2);
            int i = (int) (d2 - d);
            loginActivitySmsView.time = i;
            return i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            int i = 0;
            this.postedErrorColorTimeout = false;
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i >= codeNumberFieldArr.length) {
                    break;
                }
                codeNumberFieldArr[i].animateErrorProgress(0.0f);
                i++;
            }
            if (this.errorViewSwitcher.getCurrentView() != (this.currentType == 15 ? this.openFragmentButton : this.problemFrame)) {
                this.errorViewSwitcher.showNext();
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:61:0x03d5  */
        /* JADX WARN: Removed duplicated region for block: B:64:0x03f5  */
        /* JADX WARN: Removed duplicated region for block: B:67:0x045e  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x04ca  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x05e5  */
        /* JADX WARN: Removed duplicated region for block: B:83:0x061f  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x064d  */
        /* JADX WARN: Removed duplicated region for block: B:91:0x066b  */
        /* JADX WARN: Removed duplicated region for block: B:93:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public LoginActivitySmsView(final Context context, int i) {
            super(context);
            FrameLayout frameLayout;
            int i2;
            this.timerSync = new Object();
            this.time = 60000;
            this.codeTime = 15000;
            this.lastError = "";
            this.isResendingCode = false;
            this.pattern = "*";
            this.prefix = "";
            this.errorColorTimeout = new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$new$0();
                }
            };
            this.currentType = i;
            setOrientation(1);
            TextView textView = new TextView(context);
            this.confirmTextView = textView;
            textView.setTextSize(1, 14.0f);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            TextView textView2 = new TextView(context);
            this.titleTextView = textView2;
            textView2.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.titleTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.titleTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.titleTextView.setGravity(49);
            String string = LoginActivity.this.activityMode != 1 ? null : LocaleController.getString(R.string.CancelAccountReset);
            int i3 = this.currentType;
            if (i3 == 11) {
                this.titleTextView.setText(string == null ? LocaleController.getString("MissedCallDescriptionTitle", R.string.MissedCallDescriptionTitle) : string);
                FrameLayout frameLayout2 = new FrameLayout(context);
                this.missedCallArrowIcon = new ImageView(context);
                this.missedCallPhoneIcon = new ImageView(context);
                frameLayout2.addView(this.missedCallArrowIcon);
                frameLayout2.addView(this.missedCallPhoneIcon);
                this.missedCallArrowIcon.setImageResource(R.drawable.login_arrow1);
                this.missedCallPhoneIcon.setImageResource(R.drawable.login_phone1);
                addView(frameLayout2, LayoutHelper.createLinear(64, 64, 1, 0, 16, 0, 0));
                addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 8, 0, 0));
                TextView textView3 = new TextView(context);
                this.missedCallDescriptionSubtitle = textView3;
                textView3.setTextSize(1, 14.0f);
                this.missedCallDescriptionSubtitle.setGravity(1);
                this.missedCallDescriptionSubtitle.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                this.missedCallDescriptionSubtitle.setText(AndroidUtilities.replaceTags(LocaleController.getString("MissedCallDescriptionSubtitle", R.string.MissedCallDescriptionSubtitle)));
                addView(this.missedCallDescriptionSubtitle, LayoutHelper.createLinear(-1, -2, 49, 36, 16, 36, 0));
                this.codeFieldContainer = new CodeFieldContainer(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivitySmsView.1
                    @Override // org.telegram.ui.CodeFieldContainer
                    protected void processNextPressed() {
                        LoginActivitySmsView.this.onNextPressed(null);
                    }
                };
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(0);
                TextView textView4 = new TextView(context);
                this.prefixTextView = textView4;
                textView4.setTextSize(1, 20.0f);
                this.prefixTextView.setMaxLines(1);
                this.prefixTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                this.prefixTextView.setPadding(0, 0, 0, 0);
                this.prefixTextView.setGravity(16);
                linearLayout.addView(this.prefixTextView, LayoutHelper.createLinear(-2, -1, 16, 0, 0, 4, 0));
                linearLayout.addView(this.codeFieldContainer, LayoutHelper.createLinear(-2, -1));
                addView(linearLayout, LayoutHelper.createLinear(-2, 34, 1, 0, 28, 0, 0));
                TextView textView5 = new TextView(context);
                this.missedCallDescriptionSubtitle = textView5;
                textView5.setTextSize(1, 14.0f);
                this.missedCallDescriptionSubtitle.setGravity(1);
                this.missedCallDescriptionSubtitle.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                this.missedCallDescriptionSubtitle.setText(AndroidUtilities.replaceTags(LocaleController.getString("MissedCallDescriptionSubtitle2", R.string.MissedCallDescriptionSubtitle2)));
                addView(this.missedCallDescriptionSubtitle, LayoutHelper.createLinear(-1, -2, 49, 36, 28, 36, 12));
            } else {
                if (i3 == 3) {
                    this.confirmTextView.setGravity(1);
                    frameLayout = new FrameLayout(context);
                    addView(frameLayout, LayoutHelper.createLinear(-1, 0, 1.0f));
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    linearLayout2.setOrientation(1);
                    linearLayout2.setGravity(1);
                    frameLayout.addView(linearLayout2, LayoutHelper.createFrame(-1, -2, 17));
                    ((FrameLayout.LayoutParams) linearLayout2.getLayoutParams()).bottomMargin = AndroidUtilities.isTablet() ? 0 : AndroidUtilities.statusBarHeight;
                    FrameLayout frameLayout3 = new FrameLayout(context);
                    linearLayout2.addView(frameLayout3, LayoutHelper.createFrame(-2, -2, 1));
                    this.blueImageView = new RLottieImageView(context);
                    int i4 = R.raw.phone_flash_call;
                    RLottieDrawable rLottieDrawable = new RLottieDrawable(i4, String.valueOf(i4), AndroidUtilities.dp(64.0f), AndroidUtilities.dp(64.0f), true, null);
                    this.hintDrawable = rLottieDrawable;
                    this.blueImageView.setAnimation(rLottieDrawable);
                    frameLayout3.addView(this.blueImageView, LayoutHelper.createFrame(64, 64.0f));
                    this.titleTextView.setText(string == null ? LocaleController.getString(R.string.YourCode) : string);
                    linearLayout2.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 0));
                    linearLayout2.addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 0, 8, 0, 0));
                    if (this.currentType != 11) {
                        CodeFieldContainer codeFieldContainer = new CodeFieldContainer(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivitySmsView.2
                            @Override // org.telegram.ui.CodeFieldContainer
                            protected void processNextPressed() {
                                LoginActivitySmsView.this.onNextPressed(null);
                            }
                        };
                        this.codeFieldContainer = codeFieldContainer;
                        addView(codeFieldContainer, LayoutHelper.createLinear(-2, 42, 1, 0, 32, 0, 0));
                    }
                    if (this.currentType == 3) {
                        this.codeFieldContainer.setVisibility(8);
                    }
                    this.problemFrame = new FrameLayout(context);
                    TextView textView6 = new TextView(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivitySmsView.3
                        private LoadingDrawable loadingDrawable;

                        {
                            LoadingDrawable loadingDrawable = new LoadingDrawable();
                            this.loadingDrawable = loadingDrawable;
                            loadingDrawable.setAppearByGradient(true);
                        }

                        @Override // android.widget.TextView
                        public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
                            super.setText(charSequence, bufferType);
                            updateLoadingLayout();
                        }

                        @Override // android.widget.TextView, android.view.View
                        protected void onLayout(boolean z, int i5, int i6, int i7, int i8) {
                            super.onLayout(z, i5, i6, i7, i8);
                            updateLoadingLayout();
                        }

                        private void updateLoadingLayout() {
                            CharSequence text;
                            Layout layout = getLayout();
                            if (layout == null || (text = layout.getText()) == null) {
                                return;
                            }
                            LinkPath linkPath = new LinkPath(true);
                            int length = text.length();
                            linkPath.setCurrentLayout(layout, 0, 0.0f);
                            layout.getSelectionPath(0, length, linkPath);
                            this.loadingDrawable.usePath(linkPath);
                            this.loadingDrawable.setRadiiDp(4.0f);
                            int themedColor = LoginActivity.this.getThemedColor("chat_linkSelectBackground");
                            this.loadingDrawable.setColors(Theme.multAlpha(themedColor, 0.85f), Theme.multAlpha(themedColor, 2.0f), Theme.multAlpha(themedColor, 3.5f), Theme.multAlpha(themedColor, 6.0f));
                            this.loadingDrawable.updateBounds();
                        }

                        @Override // android.widget.TextView, android.view.View
                        protected void onDraw(Canvas canvas) {
                            super.onDraw(canvas);
                            if (LoginActivitySmsView.this.isResendingCode) {
                                canvas.save();
                                canvas.translate(getPaddingLeft(), getPaddingTop());
                                this.loadingDrawable.draw(canvas);
                                canvas.restore();
                                invalidate();
                            }
                        }
                    };
                    this.timeText = textView6;
                    textView6.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.timeText.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(16.0f));
                    this.timeText.setTextSize(1, 15.0f);
                    this.timeText.setGravity(49);
                    this.timeText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda6
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LoginActivity.LoginActivitySmsView.this.lambda$new$4(view);
                        }
                    });
                    this.problemFrame.addView(this.timeText, LayoutHelper.createFrame(-2, -2, 49));
                    this.errorViewSwitcher = new ViewSwitcher(this, context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivitySmsView.4
                        @Override // android.widget.FrameLayout, android.view.View
                        protected void onMeasure(int i5, int i6) {
                            super.onMeasure(i5, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                        }
                    };
                    if (this.currentType == 15) {
                        Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.text_in);
                        Interpolator interpolator = Easings.easeInOutQuad;
                        loadAnimation.setInterpolator(interpolator);
                        this.errorViewSwitcher.setInAnimation(loadAnimation);
                        Animation loadAnimation2 = AnimationUtils.loadAnimation(context, R.anim.text_out);
                        loadAnimation2.setInterpolator(interpolator);
                        this.errorViewSwitcher.setOutAnimation(loadAnimation2);
                        TextView textView7 = new TextView(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivitySmsView.5
                            private LoadingDrawable loadingDrawable;

                            {
                                LoadingDrawable loadingDrawable = new LoadingDrawable();
                                this.loadingDrawable = loadingDrawable;
                                loadingDrawable.setAppearByGradient(true);
                            }

                            @Override // android.widget.TextView
                            public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
                                super.setText(charSequence, bufferType);
                                updateLoadingLayout();
                            }

                            @Override // android.widget.TextView, android.view.View
                            protected void onLayout(boolean z, int i5, int i6, int i7, int i8) {
                                super.onLayout(z, i5, i6, i7, i8);
                                updateLoadingLayout();
                            }

                            private void updateLoadingLayout() {
                                CharSequence text;
                                Layout layout = getLayout();
                                if (layout == null || (text = layout.getText()) == null) {
                                    return;
                                }
                                LinkPath linkPath = new LinkPath(true);
                                int length = text.length();
                                linkPath.setCurrentLayout(layout, 0, 0.0f);
                                layout.getSelectionPath(0, length, linkPath);
                                this.loadingDrawable.usePath(linkPath);
                                this.loadingDrawable.setRadiiDp(4.0f);
                                int themedColor = LoginActivity.this.getThemedColor("chat_linkSelectBackground");
                                this.loadingDrawable.setColors(Theme.multAlpha(themedColor, 0.85f), Theme.multAlpha(themedColor, 2.0f), Theme.multAlpha(themedColor, 3.5f), Theme.multAlpha(themedColor, 6.0f));
                                this.loadingDrawable.updateBounds();
                            }

                            @Override // android.widget.TextView, android.view.View
                            protected void onDraw(Canvas canvas) {
                                super.onDraw(canvas);
                                if (LoginActivitySmsView.this.isResendingCode) {
                                    canvas.save();
                                    canvas.translate(getPaddingLeft(), getPaddingTop());
                                    this.loadingDrawable.draw(canvas);
                                    canvas.restore();
                                    invalidate();
                                }
                            }
                        };
                        this.problemText = textView7;
                        textView7.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                        this.problemText.setTextSize(1, 15.0f);
                        this.problemText.setGravity(49);
                        this.problemText.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(16.0f));
                        this.problemFrame.addView(this.problemText, LayoutHelper.createFrame(-1, -2, 17));
                        this.errorViewSwitcher.addView(this.problemFrame, LayoutHelper.createFrame(-2, -2, 17));
                    } else {
                        Animation loadAnimation3 = AnimationUtils.loadAnimation(context, R.anim.scale_in);
                        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.DEFAULT;
                        loadAnimation3.setInterpolator(cubicBezierInterpolator);
                        this.errorViewSwitcher.setInAnimation(loadAnimation3);
                        Animation loadAnimation4 = AnimationUtils.loadAnimation(context, R.anim.scale_out);
                        loadAnimation4.setInterpolator(cubicBezierInterpolator);
                        this.errorViewSwitcher.setOutAnimation(loadAnimation4);
                        LinearLayout linearLayout3 = new LinearLayout(context);
                        this.openFragmentButton = linearLayout3;
                        linearLayout3.setOrientation(0);
                        this.openFragmentButton.setGravity(17);
                        this.openFragmentButton.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
                        this.openFragmentButton.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("changephoneinfo_image2"), Theme.getColor("chats_actionPressedBackground")));
                        this.openFragmentButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda5
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                LoginActivity.LoginActivitySmsView.this.lambda$new$5(view);
                            }
                        });
                        this.errorViewSwitcher.addView(this.openFragmentButton, LayoutHelper.createFrame(-1, 52.0f));
                        RLottieImageView rLottieImageView = new RLottieImageView(context);
                        this.openFragmentImageView = rLottieImageView;
                        rLottieImageView.setAnimation(R.raw.fragment, 36, 36);
                        this.openFragmentButton.addView(this.openFragmentImageView, LayoutHelper.createLinear(36, 36, 16, 0, 0, 2, 0));
                        TextView textView8 = new TextView(context);
                        this.openFragmentButtonText = textView8;
                        textView8.setText(LocaleController.getString(R.string.OpenFragment));
                        this.openFragmentButtonText.setTextColor(-1);
                        this.openFragmentButtonText.setTextSize(1, 15.0f);
                        this.openFragmentButtonText.setGravity(17);
                        this.openFragmentButtonText.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                        this.openFragmentButton.addView(this.openFragmentButtonText);
                    }
                    TextView textView9 = new TextView(context);
                    this.wrongCode = textView9;
                    textView9.setText(LocaleController.getString("WrongCode", R.string.WrongCode));
                    this.wrongCode.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.wrongCode.setTextSize(1, 15.0f);
                    this.wrongCode.setGravity(49);
                    this.wrongCode.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
                    this.errorViewSwitcher.addView(this.wrongCode, LayoutHelper.createFrame(-2, -2, 17));
                    i2 = this.currentType;
                    if (i2 != 15) {
                        if (i2 == 1) {
                            int i5 = this.nextType;
                            if (i5 == 3 || i5 == 4 || i5 == 11) {
                                this.problemText.setText(LocaleController.getString("DidNotGetTheCodePhone", R.string.DidNotGetTheCodePhone));
                            } else {
                                this.problemText.setText(LocaleController.getString("DidNotGetTheCodeSms", R.string.DidNotGetTheCodeSms));
                            }
                        } else {
                            this.problemText.setText(LocaleController.getString("DidNotGetTheCode", R.string.DidNotGetTheCode));
                        }
                    }
                    if (frameLayout != null) {
                        FrameLayout frameLayout4 = new FrameLayout(context);
                        this.bottomContainer = frameLayout4;
                        frameLayout4.addView(this.errorViewSwitcher, LayoutHelper.createFrame(this.currentType == 15 ? -1 : -2, -2.0f, 81, 0.0f, 0.0f, 0.0f, 32.0f));
                        addView(this.bottomContainer, LayoutHelper.createLinear(-1, 0, 1.0f));
                    } else {
                        frameLayout.addView(this.errorViewSwitcher, LayoutHelper.createFrame(-2, -2.0f, 81, 0.0f, 0.0f, 0.0f, 32.0f));
                    }
                    VerticalPositionAutoAnimator.attach(this.errorViewSwitcher);
                    if (this.currentType == 15) {
                        this.problemText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda7
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view) {
                                LoginActivity.LoginActivitySmsView.this.lambda$new$8(context, view);
                            }
                        });
                        return;
                    }
                    return;
                }
                this.confirmTextView.setGravity(49);
                FrameLayout frameLayout5 = new FrameLayout(context);
                addView(frameLayout5, LayoutHelper.createLinear(-2, -2, 49, 0, 16, 0, 0));
                int i6 = this.currentType;
                int i7 = i6 == 1 ? 128 : 64;
                if (i6 == 1) {
                    int i8 = R.raw.code_laptop;
                    float f = i7;
                    this.hintDrawable = new RLottieDrawable(i8, String.valueOf(i8), AndroidUtilities.dp(f), AndroidUtilities.dp(f), true, null);
                } else {
                    int i9 = R.raw.sms_incoming_info;
                    float f2 = i7;
                    this.hintDrawable = new RLottieDrawable(i9, String.valueOf(i9), AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), true, null);
                    int i10 = R.raw.phone_stars_to_dots;
                    this.starsToDotsDrawable = new RLottieDrawable(i10, String.valueOf(i10), AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), true, null);
                    int i11 = R.raw.phone_dots;
                    this.dotsDrawable = new RLottieDrawable(i11, String.valueOf(i11), AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), true, null);
                    int i12 = R.raw.phone_dots_to_stars;
                    this.dotsToStarsDrawable = new RLottieDrawable(i12, String.valueOf(i12), AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), true, null);
                }
                RLottieImageView rLottieImageView2 = new RLottieImageView(context);
                this.blueImageView = rLottieImageView2;
                rLottieImageView2.setAnimation(this.hintDrawable);
                if (this.currentType == 1 && !AndroidUtilities.isSmallScreen()) {
                    this.blueImageView.setTranslationY(-AndroidUtilities.dp(24.0f));
                }
                frameLayout5.addView(this.blueImageView, LayoutHelper.createFrame(i7, i7, 51, 0.0f, 0.0f, 0.0f, (this.currentType != 1 || AndroidUtilities.isSmallScreen()) ? 0.0f : -AndroidUtilities.dp(16.0f)));
                TextView textView10 = this.titleTextView;
                if (string == null) {
                    string = LocaleController.getString(this.currentType == 1 ? R.string.SentAppCodeTitle : R.string.SentSmsCodeTitle);
                }
                textView10.setText(string);
                addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 18, 0, 0));
                int i13 = this.currentType == 15 ? 16 : 0;
                addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 49, i13, 17, i13, 0));
            }
            frameLayout = null;
            if (this.currentType != 11) {
            }
            if (this.currentType == 3) {
            }
            this.problemFrame = new FrameLayout(context);
            TextView textView62 = new TextView(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivitySmsView.3
                private LoadingDrawable loadingDrawable;

                {
                    LoadingDrawable loadingDrawable = new LoadingDrawable();
                    this.loadingDrawable = loadingDrawable;
                    loadingDrawable.setAppearByGradient(true);
                }

                @Override // android.widget.TextView
                public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
                    super.setText(charSequence, bufferType);
                    updateLoadingLayout();
                }

                @Override // android.widget.TextView, android.view.View
                protected void onLayout(boolean z, int i52, int i62, int i72, int i82) {
                    super.onLayout(z, i52, i62, i72, i82);
                    updateLoadingLayout();
                }

                private void updateLoadingLayout() {
                    CharSequence text;
                    Layout layout = getLayout();
                    if (layout == null || (text = layout.getText()) == null) {
                        return;
                    }
                    LinkPath linkPath = new LinkPath(true);
                    int length = text.length();
                    linkPath.setCurrentLayout(layout, 0, 0.0f);
                    layout.getSelectionPath(0, length, linkPath);
                    this.loadingDrawable.usePath(linkPath);
                    this.loadingDrawable.setRadiiDp(4.0f);
                    int themedColor = LoginActivity.this.getThemedColor("chat_linkSelectBackground");
                    this.loadingDrawable.setColors(Theme.multAlpha(themedColor, 0.85f), Theme.multAlpha(themedColor, 2.0f), Theme.multAlpha(themedColor, 3.5f), Theme.multAlpha(themedColor, 6.0f));
                    this.loadingDrawable.updateBounds();
                }

                @Override // android.widget.TextView, android.view.View
                protected void onDraw(Canvas canvas) {
                    super.onDraw(canvas);
                    if (LoginActivitySmsView.this.isResendingCode) {
                        canvas.save();
                        canvas.translate(getPaddingLeft(), getPaddingTop());
                        this.loadingDrawable.draw(canvas);
                        canvas.restore();
                        invalidate();
                    }
                }
            };
            this.timeText = textView62;
            textView62.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.timeText.setPadding(AndroidUtilities.dp(6.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(6.0f), AndroidUtilities.dp(16.0f));
            this.timeText.setTextSize(1, 15.0f);
            this.timeText.setGravity(49);
            this.timeText.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LoginActivity.LoginActivitySmsView.this.lambda$new$4(view);
                }
            });
            this.problemFrame.addView(this.timeText, LayoutHelper.createFrame(-2, -2, 49));
            this.errorViewSwitcher = new ViewSwitcher(this, context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivitySmsView.4
                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i52, int i62) {
                    super.onMeasure(i52, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                }
            };
            if (this.currentType == 15) {
            }
            TextView textView92 = new TextView(context);
            this.wrongCode = textView92;
            textView92.setText(LocaleController.getString("WrongCode", R.string.WrongCode));
            this.wrongCode.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.wrongCode.setTextSize(1, 15.0f);
            this.wrongCode.setGravity(49);
            this.wrongCode.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
            this.errorViewSwitcher.addView(this.wrongCode, LayoutHelper.createFrame(-2, -2, 17));
            i2 = this.currentType;
            if (i2 != 15) {
            }
            if (frameLayout != null) {
            }
            VerticalPositionAutoAnimator.attach(this.errorViewSwitcher);
            if (this.currentType == 15) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$4(View view) {
            if (LoginActivity.this.isRequestingFirebaseSms || this.isResendingCode) {
                return;
            }
            this.isResendingCode = true;
            this.timeText.invalidate();
            this.timeText.setTextColor(Theme.getColor("windowBackgroundWhiteValueText"));
            int i = this.nextType;
            if (i != 4 && i != 2 && i != 11 && i != 15) {
                if (i == 3) {
                    AndroidUtilities.setWaitingForSms(false);
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                    this.waitingForEvent = false;
                    destroyCodeTimer();
                    resendCode();
                    return;
                }
                return;
            }
            this.timeText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            int i2 = this.nextType;
            if (i2 == 4 || i2 == 11) {
                this.timeText.setText(LocaleController.getString("Calling", R.string.Calling));
            } else {
                this.timeText.setText(LocaleController.getString("SendingSms", R.string.SendingSms));
            }
            final Bundle bundle = new Bundle();
            bundle.putString("phone", this.phone);
            bundle.putString("ephone", this.emailPhone);
            bundle.putString("phoneFormated", this.requestPhone);
            createCodeTimer();
            TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode = new TLRPC$TL_auth_resendCode();
            tLRPC$TL_auth_resendCode.phone_number = this.requestPhone;
            tLRPC$TL_auth_resendCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_resendCode, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda37
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LoginActivity.LoginActivitySmsView.this.lambda$new$3(bundle, tLObject, tLRPC$TL_error);
                }
            }, 10);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(Bundle bundle, TLObject tLObject) {
            LoginActivity.this.lambda$resendCodeFromSafetyNet$18(bundle, (TLRPC$TL_auth_sentCode) tLObject);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(final Bundle bundle, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            if (tLObject != null) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda26
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivitySmsView.this.lambda$new$1(bundle, tLObject);
                    }
                });
            } else if (tLRPC$TL_error == null || tLRPC$TL_error.text == null) {
            } else {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda29
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivitySmsView.this.lambda$new$2(tLRPC$TL_error);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(TLRPC$TL_error tLRPC$TL_error) {
            this.lastError = tLRPC$TL_error.text;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$5(View view) {
            try {
                getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.url)));
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$8(Context context, View view) {
            if (this.nextPressed || this.timeText.getVisibility() != 8 || this.isResendingCode) {
                return;
            }
            if (!(this.nextType == 0)) {
                if (LoginActivity.this.radialProgressView.getTag() != null) {
                    return;
                }
                resendCode();
                return;
            }
            new AlertDialog.Builder(context).setTitle(LocaleController.getString(R.string.RestorePasswordNoEmailTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("DidNotGetTheCodeInfo", R.string.DidNotGetTheCodeInfo, this.phone))).setNeutralButton(LocaleController.getString(R.string.DidNotGetTheCodeHelpButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    LoginActivity.LoginActivitySmsView.this.lambda$new$6(dialogInterface, i);
                }
            }).setPositiveButton(LocaleController.getString(R.string.Close), null).setNegativeButton(LocaleController.getString(R.string.DidNotGetTheCodeEditNumberButton), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    LoginActivity.LoginActivitySmsView.this.lambda$new$7(dialogInterface, i);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$6(DialogInterface dialogInterface, int i) {
            try {
                PackageInfo packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
                String format = String.format(Locale.US, "%s (%d)", packageInfo.versionName, Integer.valueOf(packageInfo.versionCode));
                Intent intent = new Intent("android.intent.action.SENDTO");
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra("android.intent.extra.EMAIL", new String[]{"sms@telegram.org"});
                intent.putExtra("android.intent.extra.SUBJECT", "Android registration/login issue " + format + " " + this.emailPhone);
                intent.putExtra("android.intent.extra.TEXT", "Phone: " + this.requestPhone + "\nApp version: " + format + "\nOS version: SDK " + Build.VERSION.SDK_INT + "\nDevice Name: " + Build.MANUFACTURER + Build.MODEL + "\nLocale: " + Locale.getDefault() + "\nError: " + this.lastError);
                getContext().startActivity(Intent.createChooser(intent, "Send email..."));
            } catch (Exception unused) {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.AppName), LocaleController.getString("NoMailInstalled", R.string.NoMailInstalled));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$7(DialogInterface dialogInterface, int i) {
            LoginActivity.this.setPage(0, true, null, true);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void updateColors() {
            this.confirmTextView.setTextColor(Theme.getColor(LoginActivity.this.isInCancelAccountDeletionMode() ? "windowBackgroundWhiteBlackText" : "windowBackgroundWhiteGrayText6"));
            this.confirmTextView.setLinkTextColor(Theme.getColor("chats_actionBackground"));
            this.titleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            if (this.currentType == 11) {
                this.missedCallDescriptionSubtitle.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText"));
                this.missedCallArrowIcon.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteInputFieldActivated"), PorterDuff.Mode.SRC_IN));
                this.missedCallPhoneIcon.setColorFilter(new PorterDuffColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"), PorterDuff.Mode.SRC_IN));
                this.prefixTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            }
            applyLottieColors(this.hintDrawable);
            applyLottieColors(this.starsToDotsDrawable);
            applyLottieColors(this.dotsDrawable);
            applyLottieColors(this.dotsToStarsDrawable);
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            if (codeFieldContainer != null) {
                codeFieldContainer.invalidate();
            }
            String str = (String) this.timeText.getTag();
            this.timeText.setTextColor(Theme.getColor(str != null ? str : "windowBackgroundWhiteGrayText6"));
            if (this.currentType != 15) {
                this.problemText.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            }
            this.wrongCode.setTextColor(Theme.getColor("dialogTextRed"));
        }

        private void applyLottieColors(RLottieDrawable rLottieDrawable) {
            if (rLottieDrawable != null) {
                rLottieDrawable.setLayerColor("Bubble.**", Theme.getColor("chats_actionBackground"));
                rLottieDrawable.setLayerColor("Phone.**", Theme.getColor("windowBackgroundWhiteBlackText"));
                rLottieDrawable.setLayerColor("Note.**", Theme.getColor("windowBackgroundWhiteBlackText"));
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean hasCustomKeyboard() {
            return this.currentType != 3;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onCancelPressed() {
            this.nextPressed = false;
        }

        private void resendCode() {
            if (this.nextPressed || this.isResendingCode || LoginActivity.this.isRequestingFirebaseSms) {
                return;
            }
            this.isResendingCode = true;
            this.timeText.invalidate();
            this.problemText.invalidate();
            final Bundle bundle = new Bundle();
            bundle.putString("phone", this.phone);
            bundle.putString("ephone", this.emailPhone);
            bundle.putString("phoneFormated", this.requestPhone);
            this.nextPressed = true;
            TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode = new TLRPC$TL_auth_resendCode();
            tLRPC$TL_auth_resendCode.phone_number = this.requestPhone;
            tLRPC$TL_auth_resendCode.phone_code_hash = this.phoneHash;
            tryShowProgress(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_resendCode, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda36
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LoginActivity.LoginActivitySmsView.this.lambda$resendCode$10(bundle, tLObject, tLRPC$TL_error);
                }
            }, 10));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resendCode$10(final Bundle bundle, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda30
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$resendCode$9(tLRPC$TL_error, bundle, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$resendCode$9(TLRPC$TL_error tLRPC$TL_error, Bundle bundle, TLObject tLObject) {
            this.nextPressed = false;
            if (tLRPC$TL_error == null) {
                LoginActivity.this.lambda$resendCodeFromSafetyNet$18(bundle, (TLRPC$TL_auth_sentCode) tLObject);
            } else {
                String str = tLRPC$TL_error.text;
                if (str != null) {
                    if (str.contains("PHONE_NUMBER_INVALID")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("InvalidPhoneNumber", R.string.InvalidPhoneNumber));
                    } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("InvalidCode", R.string.InvalidCode));
                    } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                        onBackPressed(true);
                        LoginActivity.this.setPage(0, true, null, true);
                        LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("CodeExpired", R.string.CodeExpired));
                    } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("FloodWait", R.string.FloodWait));
                    } else if (tLRPC$TL_error.code != -1000) {
                        LoginActivity loginActivity = LoginActivity.this;
                        String string = LocaleController.getString(R.string.RestorePasswordNoEmailTitle);
                        loginActivity.needShowAlert(string, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text);
                    }
                }
            }
            tryHideProgress(false);
        }

        @Override // android.view.View
        protected void onConfigurationChanged(Configuration configuration) {
            CodeNumberField[] codeNumberFieldArr;
            super.onConfigurationChanged(configuration);
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            if (codeFieldContainer == null || (codeNumberFieldArr = codeFieldContainer.codeField) == null) {
                return;
            }
            for (CodeNumberField codeNumberField : codeNumberFieldArr) {
                if (Build.VERSION.SDK_INT >= 21) {
                    codeNumberField.setShowSoftInputOnFocusCompat(!hasCustomKeyboard() || LoginActivity.this.isCustomKeyboardForceDisabled());
                }
            }
        }

        private void tryShowProgress(int i) {
            lambda$tryShowProgress$11(i, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: tryShowProgress */
        public void lambda$tryShowProgress$11(final int i, final boolean z) {
            if (this.starsToDotsDrawable == null) {
                LoginActivity.this.needShowProgress(i, z);
            } else if (this.isDotsAnimationVisible) {
            } else {
                this.isDotsAnimationVisible = true;
                if (this.hintDrawable.getCurrentFrame() != this.hintDrawable.getFramesCount() - 1) {
                    this.hintDrawable.setOnAnimationEndListener(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda22
                        @Override // java.lang.Runnable
                        public final void run() {
                            LoginActivity.LoginActivitySmsView.this.lambda$tryShowProgress$12(i, z);
                        }
                    });
                    return;
                }
                this.starsToDotsDrawable.setOnAnimationEndListener(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivitySmsView.this.lambda$tryShowProgress$14();
                    }
                });
                this.blueImageView.setAutoRepeat(false);
                this.starsToDotsDrawable.setCurrentFrame(0, false);
                this.blueImageView.setAnimation(this.starsToDotsDrawable);
                this.blueImageView.playAnimation();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$tryShowProgress$12(final int i, final boolean z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$tryShowProgress$11(i, z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$tryShowProgress$14() {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$tryShowProgress$13();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$tryShowProgress$13() {
            this.blueImageView.setAutoRepeat(true);
            this.dotsDrawable.setCurrentFrame(0, false);
            this.dotsDrawable.setAutoRepeat(1);
            this.blueImageView.setAnimation(this.dotsDrawable);
            this.blueImageView.playAnimation();
        }

        private void tryHideProgress(boolean z) {
            tryHideProgress(z, true);
        }

        private void tryHideProgress(boolean z, boolean z2) {
            if (this.starsToDotsDrawable == null) {
                LoginActivity.this.needHideProgress(z, z2);
            } else if (this.isDotsAnimationVisible) {
                this.isDotsAnimationVisible = false;
                this.blueImageView.setAutoRepeat(false);
                this.dotsDrawable.setAutoRepeat(0);
                this.dotsDrawable.setOnFinishCallback(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda16
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivitySmsView.this.lambda$tryHideProgress$18();
                    }
                }, this.dotsDrawable.getFramesCount() - 1);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$tryHideProgress$18() {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$tryHideProgress$17();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$tryHideProgress$16() {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$tryHideProgress$15();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$tryHideProgress$17() {
            this.dotsToStarsDrawable.setOnAnimationEndListener(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$tryHideProgress$16();
                }
            });
            this.blueImageView.setAutoRepeat(false);
            this.dotsToStarsDrawable.setCurrentFrame(0, false);
            this.blueImageView.setAnimation(this.dotsToStarsDrawable);
            this.blueImageView.playAnimation();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$tryHideProgress$15() {
            this.blueImageView.setAutoRepeat(false);
            this.blueImageView.setAnimation(this.hintDrawable);
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            int i = this.currentType;
            if (i == 3 || i == 11) {
                return this.phone;
            }
            return LocaleController.getString("YourCode", R.string.YourCode);
        }

        /* JADX WARN: Removed duplicated region for block: B:100:0x0300  */
        /* JADX WARN: Removed duplicated region for block: B:101:0x0309  */
        @Override // org.telegram.ui.Components.SlideView
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void setParams(Bundle bundle, boolean z) {
            CodeNumberField[] codeNumberFieldArr;
            String replaceTags;
            int i;
            String str;
            String string;
            int i2;
            if (bundle == null) {
                return;
            }
            this.waitingForEvent = true;
            int i3 = this.currentType;
            if (i3 == 15) {
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i3 == 2) {
                AndroidUtilities.setWaitingForSms(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i3 == 3) {
                AndroidUtilities.setWaitingForCall(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }
            this.currentParams = bundle;
            this.phone = bundle.getString("phone");
            this.emailPhone = bundle.getString("ephone");
            this.requestPhone = bundle.getString("phoneFormated");
            this.phoneHash = bundle.getString("phoneHash");
            this.time = bundle.getInt("timeout");
            this.openTime = (int) (System.currentTimeMillis() / 1000);
            this.nextType = bundle.getInt("nextType");
            this.pattern = bundle.getString("pattern");
            this.prefix = bundle.getString("prefix");
            int i4 = bundle.getInt("length");
            this.length = i4;
            if (i4 == 0) {
                this.length = 5;
            }
            this.url = bundle.getString("url");
            this.codeFieldContainer.setNumbersCount(this.length, this.currentType);
            for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                if (Build.VERSION.SDK_INT >= 21) {
                    codeNumberField.setShowSoftInputOnFocusCompat(!hasCustomKeyboard() || LoginActivity.this.isCustomKeyboardForceDisabled());
                }
                codeNumberField.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LoginActivity.LoginActivitySmsView.6
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i5, int i6, int i7) {
                        if (LoginActivitySmsView.this.postedErrorColorTimeout) {
                            LoginActivitySmsView loginActivitySmsView = LoginActivitySmsView.this;
                            loginActivitySmsView.removeCallbacks(loginActivitySmsView.errorColorTimeout);
                            LoginActivitySmsView.this.errorColorTimeout.run();
                        }
                    }
                });
                codeNumberField.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda8
                    @Override // android.view.View.OnFocusChangeListener
                    public final void onFocusChange(View view, boolean z2) {
                        LoginActivity.LoginActivitySmsView.this.lambda$setParams$19(view, z2);
                    }
                });
            }
            if (this.phone == null) {
                return;
            }
            String format = PhoneFormat.getInstance().format(this.phone);
            if (LoginActivity.this.isInCancelAccountDeletionMode()) {
                replaceTags = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.formatString("CancelAccountResetInfo2", R.string.CancelAccountResetInfo2, PhoneFormat.getInstance().format("+" + format))));
                int indexOf = TextUtils.indexOf((CharSequence) replaceTags, '*');
                int lastIndexOf = TextUtils.lastIndexOf(replaceTags, '*');
                if (indexOf != -1 && lastIndexOf != -1 && indexOf != lastIndexOf) {
                    this.confirmTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                    replaceTags.replace(lastIndexOf, lastIndexOf + 1, (CharSequence) "");
                    replaceTags.replace(indexOf, indexOf + 1, (CharSequence) "");
                    replaceTags.setSpan(new URLSpanNoUnderline("tg://settings/change_number"), indexOf, lastIndexOf - 1, 33);
                }
            } else {
                int i5 = this.currentType;
                replaceTags = i5 == 1 ? AndroidUtilities.replaceTags(LocaleController.formatString("SentAppCodeWithPhone", R.string.SentAppCodeWithPhone, LocaleController.addNbsp(format))) : i5 == 2 ? AndroidUtilities.replaceTags(LocaleController.formatString("SentSmsCode", R.string.SentSmsCode, LocaleController.addNbsp(format))) : i5 == 3 ? AndroidUtilities.replaceTags(LocaleController.formatString("SentCallCode", R.string.SentCallCode, LocaleController.addNbsp(format))) : i5 == 4 ? AndroidUtilities.replaceTags(LocaleController.formatString("SentCallOnly", R.string.SentCallOnly, LocaleController.addNbsp(format))) : i5 == 15 ? AndroidUtilities.replaceTags(LocaleController.formatString("SentFragmentCode", R.string.SentFragmentCode, LocaleController.addNbsp(format))) : "";
            }
            this.confirmTextView.setText(replaceTags);
            if (this.currentType != 3) {
                LoginActivity.this.showKeyboard(this.codeFieldContainer.codeField[0]);
                this.codeFieldContainer.codeField[0].requestFocus();
            } else {
                AndroidUtilities.hideKeyboard(this.codeFieldContainer.codeField[0]);
            }
            destroyTimer();
            destroyCodeTimer();
            this.lastCurrentTime = System.currentTimeMillis();
            int i6 = this.currentType;
            if (i6 == 1) {
                setProblemTextVisible(true);
                this.timeText.setVisibility(8);
            } else if (i6 == 3 && ((i2 = this.nextType) == 4 || i2 == 2)) {
                setProblemTextVisible(false);
                this.timeText.setVisibility(0);
                int i7 = this.nextType;
                if (i7 == 4 || i7 == 11) {
                    this.timeText.setText(LocaleController.formatString("CallAvailableIn", R.string.CallAvailableIn, 1, 0));
                } else if (i7 == 2) {
                    this.timeText.setText(LocaleController.formatString("SmsAvailableIn", R.string.SmsAvailableIn, 1, 0));
                }
                String obtainLoginPhoneCall = z ? AndroidUtilities.obtainLoginPhoneCall(this.pattern) : null;
                if (obtainLoginPhoneCall != null) {
                    onNextPressed(obtainLoginPhoneCall);
                } else {
                    String str2 = this.catchedPhone;
                    if (str2 != null) {
                        onNextPressed(str2);
                    } else {
                        createTimer();
                    }
                }
            } else if (i6 == 2 && ((i = this.nextType) == 4 || i == 3)) {
                this.timeText.setText(LocaleController.formatString("CallAvailableIn", R.string.CallAvailableIn, 2, 0));
                setProblemTextVisible(this.time < 1000);
                this.timeText.setVisibility(this.time >= 1000 ? 0 : 8);
                SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                String string2 = sharedPreferences.getString("sms_hash", null);
                if (!TextUtils.isEmpty(string2) && (string = sharedPreferences.getString("sms_hash_code", null)) != null) {
                    if (string.contains(string2 + "|") && !LoginActivity.this.newAccount) {
                        str = string.substring(string.indexOf(124) + 1);
                        if (str == null) {
                            this.codeFieldContainer.setCode(str);
                            onNextPressed(null);
                        } else {
                            createTimer();
                        }
                    }
                }
                str = null;
                if (str == null) {
                }
            } else if (i6 == 4 && this.nextType == 2) {
                this.timeText.setText(LocaleController.formatString("SmsAvailableIn", R.string.SmsAvailableIn, 2, 0));
                setProblemTextVisible(this.time < 1000);
                this.timeText.setVisibility(this.time >= 1000 ? 0 : 8);
                createTimer();
            } else {
                this.timeText.setVisibility(8);
                setProblemTextVisible(false);
                createCodeTimer();
            }
            if (this.currentType == 11) {
                String str3 = this.prefix;
                for (int i8 = 0; i8 < this.length; i8++) {
                    str3 = str3 + "0";
                }
                String format2 = PhoneFormat.getInstance().format("+" + str3);
                for (int i9 = 0; i9 < this.length; i9++) {
                    int lastIndexOf2 = format2.lastIndexOf("0");
                    if (lastIndexOf2 >= 0) {
                        format2 = format2.substring(0, lastIndexOf2);
                    }
                }
                this.prefixTextView.setText(format2.replaceAll("\\)", "").replaceAll("\\(", ""));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setParams$19(View view, boolean z) {
            if (z) {
                LoginActivity.this.keyboardView.setEditText((EditText) view);
                LoginActivity.this.keyboardView.setDispatchBackWhenEmpty(true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setProblemTextVisible(boolean z) {
            TextView textView = this.problemText;
            if (textView == null) {
                return;
            }
            float f = z ? 1.0f : 0.0f;
            if (textView.getAlpha() != f) {
                this.problemText.animate().cancel();
                this.problemText.animate().alpha(f).setDuration(150L).start();
            }
        }

        private void createCodeTimer() {
            if (this.codeTimer != null) {
                return;
            }
            this.codeTime = 15000;
            this.codeTimer = new Timer();
            this.lastCodeTime = System.currentTimeMillis();
            this.codeTimer.schedule(new 7(), 0L, 1000L);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public class 7 extends TimerTask {
            7() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$7$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivitySmsView.7.this.lambda$run$0();
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0() {
                double currentTimeMillis = System.currentTimeMillis();
                double d = LoginActivitySmsView.this.lastCodeTime;
                Double.isNaN(currentTimeMillis);
                LoginActivitySmsView.this.lastCodeTime = currentTimeMillis;
                LoginActivitySmsView.access$9226(LoginActivitySmsView.this, currentTimeMillis - d);
                if (LoginActivitySmsView.this.codeTime <= 1000) {
                    LoginActivitySmsView.this.setProblemTextVisible(true);
                    LoginActivitySmsView.this.timeText.setVisibility(8);
                    LoginActivitySmsView.this.destroyCodeTimer();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void destroyCodeTimer() {
            try {
                synchronized (this.timerSync) {
                    Timer timer = this.codeTimer;
                    if (timer != null) {
                        timer.cancel();
                        this.codeTimer = null;
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        private void createTimer() {
            if (this.timeTimer != null) {
                return;
            }
            this.timeText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.timeText.setTag(R.id.color_key_tag, "windowBackgroundWhiteGrayText6");
            Timer timer = new Timer();
            this.timeTimer = timer;
            timer.schedule(new 8(), 0L, 1000L);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public class 8 extends TimerTask {
            8() {
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (LoginActivitySmsView.this.timeTimer == null) {
                    return;
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$8$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivitySmsView.8.this.lambda$run$0();
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$run$0() {
                double currentTimeMillis = System.currentTimeMillis();
                double d = LoginActivitySmsView.this.lastCurrentTime;
                Double.isNaN(currentTimeMillis);
                LoginActivitySmsView.this.lastCurrentTime = currentTimeMillis;
                LoginActivitySmsView.access$9826(LoginActivitySmsView.this, currentTimeMillis - d);
                if (LoginActivitySmsView.this.time >= 1000) {
                    int i = (LoginActivitySmsView.this.time / 1000) / 60;
                    int i2 = (LoginActivitySmsView.this.time / 1000) - (i * 60);
                    if (LoginActivitySmsView.this.nextType == 4 || LoginActivitySmsView.this.nextType == 3 || LoginActivitySmsView.this.nextType == 11) {
                        LoginActivitySmsView.this.timeText.setText(LocaleController.formatString("CallAvailableIn", R.string.CallAvailableIn, Integer.valueOf(i), Integer.valueOf(i2)));
                    } else if (LoginActivitySmsView.this.nextType == 2) {
                        LoginActivitySmsView.this.timeText.setText(LocaleController.formatString("SmsAvailableIn", R.string.SmsAvailableIn, Integer.valueOf(i), Integer.valueOf(i2)));
                    }
                    ProgressView unused = LoginActivitySmsView.this.progressView;
                    return;
                }
                LoginActivitySmsView.this.destroyTimer();
                if (LoginActivitySmsView.this.nextType == 3 || LoginActivitySmsView.this.nextType == 4 || LoginActivitySmsView.this.nextType == 2 || LoginActivitySmsView.this.nextType == 11) {
                    if (LoginActivitySmsView.this.nextType == 4) {
                        LoginActivitySmsView.this.timeText.setText(LocaleController.getString("RequestCallButton", R.string.RequestCallButton));
                    } else if (LoginActivitySmsView.this.nextType == 11 || LoginActivitySmsView.this.nextType == 3) {
                        LoginActivitySmsView.this.timeText.setText(LocaleController.getString("RequestMissedCall", R.string.RequestMissedCall));
                    } else {
                        LoginActivitySmsView.this.timeText.setText(LocaleController.getString("RequestSmsButton", R.string.RequestSmsButton));
                    }
                    LoginActivitySmsView.this.timeText.setTextColor(Theme.getColor("chats_actionBackground"));
                    LoginActivitySmsView.this.timeText.setTag(R.id.color_key_tag, "chats_actionBackground");
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void destroyTimer() {
            this.timeText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.timeText.setTag(R.id.color_key_tag, "windowBackgroundWhiteGrayText6");
            try {
                synchronized (this.timerSync) {
                    Timer timer = this.timeTimer;
                    if (timer != null) {
                        timer.cancel();
                        this.timeTimer = null;
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onNextPressed(String str) {
            if (LoginActivity.this.currentViewNum == 11) {
                if (this.nextPressed) {
                    return;
                }
            } else if (this.nextPressed) {
                return;
            } else {
                if ((LoginActivity.this.currentViewNum < 1 || LoginActivity.this.currentViewNum > 4) && LoginActivity.this.currentViewNum != 15) {
                    return;
                }
            }
            if (str == null) {
                str = this.codeFieldContainer.getCode();
            }
            int i = 0;
            if (TextUtils.isEmpty(str)) {
                LoginActivity.this.onFieldError(this.codeFieldContainer, false);
            } else if (LoginActivity.this.currentViewNum < 1 || LoginActivity.this.currentViewNum > 4 || !this.codeFieldContainer.isFocusSuppressed) {
                this.nextPressed = true;
                int i2 = this.currentType;
                if (i2 == 15) {
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                } else if (i2 == 2) {
                    AndroidUtilities.setWaitingForSms(false);
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                } else if (i2 == 3) {
                    AndroidUtilities.setWaitingForCall(false);
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
                }
                this.waitingForEvent = false;
                int i3 = LoginActivity.this.activityMode;
                if (i3 == 1) {
                    this.requestPhone = LoginActivity.this.cancelDeletionPhone;
                    final TLRPC$TL_account_confirmPhone tLRPC$TL_account_confirmPhone = new TLRPC$TL_account_confirmPhone();
                    tLRPC$TL_account_confirmPhone.phone_code = str;
                    tLRPC$TL_account_confirmPhone.phone_code_hash = this.phoneHash;
                    destroyTimer();
                    CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
                    codeFieldContainer.isFocusSuppressed = true;
                    CodeNumberField[] codeNumberFieldArr = codeFieldContainer.codeField;
                    int length = codeNumberFieldArr.length;
                    while (i < length) {
                        codeNumberFieldArr[i].animateFocusedProgress(0.0f);
                        i++;
                    }
                    tryShowProgress(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_account_confirmPhone, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda38
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$27(tLRPC$TL_account_confirmPhone, tLObject, tLRPC$TL_error);
                        }
                    }, 2));
                } else if (i3 == 2) {
                    TLRPC$TL_account_changePhone tLRPC$TL_account_changePhone = new TLRPC$TL_account_changePhone();
                    tLRPC$TL_account_changePhone.phone_number = this.requestPhone;
                    tLRPC$TL_account_changePhone.phone_code = str;
                    tLRPC$TL_account_changePhone.phone_code_hash = this.phoneHash;
                    destroyTimer();
                    CodeFieldContainer codeFieldContainer2 = this.codeFieldContainer;
                    codeFieldContainer2.isFocusSuppressed = true;
                    CodeNumberField[] codeNumberFieldArr2 = codeFieldContainer2.codeField;
                    int length2 = codeNumberFieldArr2.length;
                    while (i < length2) {
                        codeNumberFieldArr2[i].animateFocusedProgress(0.0f);
                        i++;
                    }
                    lambda$tryShowProgress$11(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_account_changePhone, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda35
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$23(tLObject, tLRPC$TL_error);
                        }
                    }, 2), true);
                    LoginActivity.this.showDoneButton(true, true);
                } else {
                    final TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn = new TLRPC$TL_auth_signIn();
                    tLRPC$TL_auth_signIn.phone_number = this.requestPhone;
                    tLRPC$TL_auth_signIn.phone_code = str;
                    tLRPC$TL_auth_signIn.phone_code_hash = this.phoneHash;
                    tLRPC$TL_auth_signIn.flags |= 1;
                    destroyTimer();
                    CodeFieldContainer codeFieldContainer3 = this.codeFieldContainer;
                    codeFieldContainer3.isFocusSuppressed = true;
                    CodeNumberField[] codeNumberFieldArr3 = codeFieldContainer3.codeField;
                    int length3 = codeNumberFieldArr3.length;
                    while (i < length3) {
                        codeNumberFieldArr3[i].animateFocusedProgress(0.0f);
                        i++;
                    }
                    lambda$tryShowProgress$11(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_signIn, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda39
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                            LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$34(tLRPC$TL_auth_signIn, tLObject, tLRPC$TL_error);
                        }
                    }, 10), true);
                    LoginActivity.this.showDoneButton(true, true);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$23(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda31
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$22(tLRPC$TL_error, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:48:0x0181  */
        /* JADX WARN: Removed duplicated region for block: B:57:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$onNextPressed$22(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            int i;
            int i2;
            boolean z = true;
            tryHideProgress(false, true);
            this.nextPressed = false;
            if (tLRPC$TL_error == null) {
                TLRPC$User tLRPC$User = (TLRPC$User) tLObject;
                destroyTimer();
                destroyCodeTimer();
                UserConfig.getInstance(((BaseFragment) LoginActivity.this).currentAccount).setCurrentUser(tLRPC$User);
                UserConfig.getInstance(((BaseFragment) LoginActivity.this).currentAccount).saveConfig(true);
                ArrayList<TLRPC$User> arrayList = new ArrayList<>();
                arrayList.add(tLRPC$User);
                MessagesStorage.getInstance(((BaseFragment) LoginActivity.this).currentAccount).putUsersAndChats(arrayList, null, true, true);
                MessagesController.getInstance(((BaseFragment) LoginActivity.this).currentAccount).putUser(tLRPC$User, false);
                NotificationCenter.getInstance(((BaseFragment) LoginActivity.this).currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
                LoginActivity.this.getMessagesController().removeSuggestion(0L, "VALIDATE_PHONE_NUMBER");
                if (this.currentType == 3) {
                    AndroidUtilities.endIncomingCall();
                }
                animateSuccess(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda19
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$21();
                    }
                });
                return;
            }
            this.lastError = tLRPC$TL_error.text;
            this.nextPressed = false;
            LoginActivity.this.showDoneButton(false, true);
            int i3 = this.currentType;
            if ((i3 == 3 && ((i2 = this.nextType) == 4 || i2 == 2)) || ((i3 == 2 && ((i = this.nextType) == 4 || i == 3)) || (i3 == 4 && this.nextType == 2))) {
                createTimer();
            }
            int i4 = this.currentType;
            if (i4 == 15) {
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i4 == 2) {
                AndroidUtilities.setWaitingForSms(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i4 == 3) {
                AndroidUtilities.setWaitingForCall(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = true;
            if (this.currentType == 3) {
                return;
            }
            if (tLRPC$TL_error.text.contains("PHONE_NUMBER_INVALID")) {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("InvalidPhoneNumber", R.string.InvalidPhoneNumber));
            } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                shakeWrongCode();
                if (!z) {
                    return;
                }
                int i5 = 0;
                while (true) {
                    CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
                    CodeNumberField[] codeNumberFieldArr = codeFieldContainer.codeField;
                    if (i5 < codeNumberFieldArr.length) {
                        codeNumberFieldArr[i5].setText("");
                        i5++;
                    } else {
                        codeFieldContainer.isFocusSuppressed = false;
                        codeNumberFieldArr[0].requestFocus();
                        return;
                    }
                }
            } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                onBackPressed(true);
                LoginActivity.this.setPage(0, true, null, true);
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("CodeExpired", R.string.CodeExpired));
            } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("FloodWait", R.string.FloodWait));
            } else {
                LoginActivity loginActivity = LoginActivity.this;
                String string = LocaleController.getString(R.string.RestorePasswordNoEmailTitle);
                loginActivity.needShowAlert(string, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text);
            }
            z = false;
            if (!z) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$21() {
            try {
                ((BaseFragment) LoginActivity.this).fragmentView.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            AlertDialog.Builder title = new AlertDialog.Builder(getContext()).setTitle(LocaleController.getString(R.string.YourPasswordSuccess));
            int i = R.string.ChangePhoneNumberSuccessWithPhone;
            PhoneFormat phoneFormat = PhoneFormat.getInstance();
            title.setMessage(LocaleController.formatString(i, phoneFormat.format("+" + this.requestPhone))).setPositiveButton(LocaleController.getString(R.string.OK), null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$20(dialogInterface);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$20(DialogInterface dialogInterface) {
            LoginActivity.this.finishFragment();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$27(final TLRPC$TL_account_confirmPhone tLRPC$TL_account_confirmPhone, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda34
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$26(tLRPC$TL_error, tLRPC$TL_account_confirmPhone);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$26(TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_account_confirmPhone tLRPC$TL_account_confirmPhone) {
            int i;
            int i2;
            tryHideProgress(false);
            this.nextPressed = false;
            if (tLRPC$TL_error == null) {
                animateSuccess(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$25();
                    }
                });
                return;
            }
            this.lastError = tLRPC$TL_error.text;
            int i3 = this.currentType;
            if ((i3 == 3 && ((i2 = this.nextType) == 4 || i2 == 2)) || ((i3 == 2 && ((i = this.nextType) == 4 || i == 3)) || (i3 == 4 && this.nextType == 2))) {
                createTimer();
            }
            int i4 = this.currentType;
            if (i4 == 15) {
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i4 == 2) {
                AndroidUtilities.setWaitingForSms(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i4 == 3) {
                AndroidUtilities.setWaitingForCall(true);
                NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = true;
            if (this.currentType != 3) {
                AlertsCreator.processError(((BaseFragment) LoginActivity.this).currentAccount, tLRPC$TL_error, LoginActivity.this, tLRPC$TL_account_confirmPhone, new Object[0]);
            }
            if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                shakeWrongCode();
            } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                onBackPressed(true);
                LoginActivity.this.setPage(0, true, null, true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$25() {
            AlertDialog.Builder title = new AlertDialog.Builder(LoginActivity.this.getParentActivity()).setTitle(LocaleController.getString(R.string.CancelLinkSuccessTitle));
            int i = R.string.CancelLinkSuccess;
            PhoneFormat phoneFormat = PhoneFormat.getInstance();
            title.setMessage(LocaleController.formatString("CancelLinkSuccess", i, phoneFormat.format("+" + this.phone))).setPositiveButton(LocaleController.getString(R.string.Close), null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$24(dialogInterface);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$24(DialogInterface dialogInterface) {
            LoginActivity.this.finishFragment();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$34(final TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$33(tLRPC$TL_error, tLObject, tLRPC$TL_auth_signIn);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:55:0x017e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$onNextPressed$33(TLRPC$TL_error tLRPC$TL_error, final TLObject tLObject, final TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn) {
            int i;
            CodeFieldContainer codeFieldContainer;
            CodeNumberField[] codeNumberFieldArr;
            int i2;
            boolean z = false;
            boolean z2 = true;
            tryHideProgress(false, true);
            if (tLRPC$TL_error == null) {
                this.nextPressed = false;
                LoginActivity.this.showDoneButton(false, true);
                destroyTimer();
                destroyCodeTimer();
                if (tLObject instanceof TLRPC$TL_auth_authorizationSignUpRequired) {
                    TLRPC$TL_help_termsOfService tLRPC$TL_help_termsOfService = ((TLRPC$TL_auth_authorizationSignUpRequired) tLObject).terms_of_service;
                    if (tLRPC$TL_help_termsOfService != null) {
                        LoginActivity.this.currentTermsOfService = tLRPC$TL_help_termsOfService;
                    }
                    final Bundle bundle = new Bundle();
                    bundle.putString("phoneFormated", this.requestPhone);
                    bundle.putString("phoneHash", this.phoneHash);
                    bundle.putString("code", tLRPC$TL_auth_signIn.phone_code);
                    animateSuccess(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda25
                        @Override // java.lang.Runnable
                        public final void run() {
                            LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$28(bundle);
                        }
                    });
                } else {
                    animateSuccess(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda28
                        @Override // java.lang.Runnable
                        public final void run() {
                            LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$29(tLObject);
                        }
                    });
                }
            } else {
                String str = tLRPC$TL_error.text;
                this.lastError = str;
                if (str.contains("SESSION_PASSWORD_NEEDED")) {
                    ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda40
                        @Override // org.telegram.tgnet.RequestDelegate
                        public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                            LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$32(tLRPC$TL_auth_signIn, tLObject2, tLRPC$TL_error2);
                        }
                    }, 10);
                    destroyTimer();
                    destroyCodeTimer();
                } else {
                    this.nextPressed = false;
                    LoginActivity.this.showDoneButton(false, true);
                    int i3 = this.currentType;
                    if ((i3 == 3 && ((i2 = this.nextType) == 4 || i2 == 2)) || ((i3 == 2 && ((i = this.nextType) == 4 || i == 3)) || (i3 == 4 && this.nextType == 2))) {
                        createTimer();
                    }
                    int i4 = this.currentType;
                    if (i4 == 15) {
                        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
                    } else if (i4 == 2) {
                        AndroidUtilities.setWaitingForSms(true);
                        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
                    } else if (i4 == 3) {
                        AndroidUtilities.setWaitingForCall(true);
                        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
                    }
                    this.waitingForEvent = true;
                    if (this.currentType != 3) {
                        if (tLRPC$TL_error.text.contains("PHONE_NUMBER_INVALID")) {
                            LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("InvalidPhoneNumber", R.string.InvalidPhoneNumber));
                        } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                            shakeWrongCode();
                            if (!z2) {
                                int i5 = 0;
                                while (true) {
                                    codeFieldContainer = this.codeFieldContainer;
                                    codeNumberFieldArr = codeFieldContainer.codeField;
                                    if (i5 >= codeNumberFieldArr.length) {
                                        break;
                                    }
                                    codeNumberFieldArr[i5].setText("");
                                    i5++;
                                }
                                codeFieldContainer.isFocusSuppressed = false;
                                codeNumberFieldArr[0].requestFocus();
                            }
                        } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                            onBackPressed(true);
                            LoginActivity.this.setPage(0, true, null, true);
                            LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("CodeExpired", R.string.CodeExpired));
                        } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                            LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("FloodWait", R.string.FloodWait));
                        } else {
                            LoginActivity loginActivity = LoginActivity.this;
                            String string = LocaleController.getString(R.string.RestorePasswordNoEmailTitle);
                            loginActivity.needShowAlert(string, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text);
                        }
                        z2 = false;
                        if (!z2) {
                        }
                    }
                    if (z || this.currentType != 3) {
                    }
                    AndroidUtilities.endIncomingCall();
                    return;
                }
            }
            z = true;
            if (z) {
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$28(Bundle bundle) {
            LoginActivity.this.setPage(5, true, bundle, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$29(TLObject tLObject) {
            LoginActivity.this.onAuthSuccess((TLRPC$TL_auth_authorization) tLObject);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$32(final TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda32
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$31(tLRPC$TL_error, tLObject, tLRPC$TL_auth_signIn);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$31(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn) {
            this.nextPressed = false;
            LoginActivity.this.showDoneButton(false, true);
            if (tLRPC$TL_error != null) {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), tLRPC$TL_error.text);
                return;
            }
            TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$account_Password, true)) {
                AlertsCreator.showUpdateAppAlert(LoginActivity.this.getParentActivity(), LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
                return;
            }
            final Bundle bundle = new Bundle();
            SerializedData serializedData = new SerializedData(tLRPC$account_Password.getObjectSize());
            tLRPC$account_Password.serializeToStream(serializedData);
            bundle.putString("password", Utilities.bytesToHex(serializedData.toByteArray()));
            bundle.putString("phoneFormated", this.requestPhone);
            bundle.putString("phoneHash", this.phoneHash);
            bundle.putString("code", tLRPC$TL_auth_signIn.phone_code);
            animateSuccess(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda24
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$onNextPressed$30(bundle);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$30(Bundle bundle) {
            LoginActivity.this.setPage(6, true, bundle, false);
        }

        private void animateSuccess(final Runnable runnable) {
            final int i = 0;
            while (true) {
                CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
                if (i < codeFieldContainer.codeField.length) {
                    codeFieldContainer.postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda21
                        @Override // java.lang.Runnable
                        public final void run() {
                            LoginActivity.LoginActivitySmsView.this.lambda$animateSuccess$35(i);
                        }
                    }, i * 75);
                    i++;
                } else {
                    codeFieldContainer.postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda27
                        @Override // java.lang.Runnable
                        public final void run() {
                            LoginActivity.LoginActivitySmsView.this.lambda$animateSuccess$36(runnable);
                        }
                    }, (this.codeFieldContainer.codeField.length * 75) + 400);
                    return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$animateSuccess$35(int i) {
            this.codeFieldContainer.codeField[i].animateSuccessProgress(1.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$animateSuccess$36(Runnable runnable) {
            int i = 0;
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i < codeNumberFieldArr.length) {
                    codeNumberFieldArr[i].animateSuccessProgress(0.0f);
                    i++;
                } else {
                    runnable.run();
                    this.codeFieldContainer.isFocusSuppressed = false;
                    return;
                }
            }
        }

        private void shakeWrongCode() {
            try {
                this.codeFieldContainer.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            int i = 0;
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i >= codeNumberFieldArr.length) {
                    break;
                }
                codeNumberFieldArr[i].setText("");
                this.codeFieldContainer.codeField[i].animateErrorProgress(1.0f);
                i++;
            }
            if (this.errorViewSwitcher.getCurrentView() != this.wrongCode) {
                this.errorViewSwitcher.showNext();
            }
            this.codeFieldContainer.codeField[0].requestFocus();
            AndroidUtilities.shakeViewSpring(this.codeFieldContainer, this.currentType == 11 ? 3.5f : 10.0f, new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$shakeWrongCode$38();
                }
            });
            removeCallbacks(this.errorColorTimeout);
            postDelayed(this.errorColorTimeout, 5000L);
            this.postedErrorColorTimeout = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$shakeWrongCode$38() {
            postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda20
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$shakeWrongCode$37();
                }
            }, 150L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$shakeWrongCode$37() {
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            int i = 0;
            codeFieldContainer.isFocusSuppressed = false;
            codeFieldContainer.codeField[0].requestFocus();
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i >= codeNumberFieldArr.length) {
                    return;
                }
                codeNumberFieldArr[i].animateErrorProgress(0.0f);
                i++;
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            removeCallbacks(this.errorColorTimeout);
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean onBackPressed(boolean z) {
            if (LoginActivity.this.activityMode != 0) {
                LoginActivity.this.finishFragment();
                return false;
            } else if (!z) {
                LoginActivity loginActivity = LoginActivity.this;
                loginActivity.showDialog(new AlertDialog.Builder(loginActivity.getParentActivity()).setTitle(LocaleController.getString(R.string.EditNumber)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("EditNumberInfo", R.string.EditNumberInfo, this.phone))).setPositiveButton(LocaleController.getString(R.string.Close), null).setNegativeButton(LocaleController.getString(R.string.Edit), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        LoginActivity.LoginActivitySmsView.this.lambda$onBackPressed$39(dialogInterface, i);
                    }
                }).create());
                return false;
            } else {
                this.nextPressed = false;
                tryHideProgress(true);
                TLRPC$TL_auth_cancelCode tLRPC$TL_auth_cancelCode = new TLRPC$TL_auth_cancelCode();
                tLRPC$TL_auth_cancelCode.phone_number = this.requestPhone;
                tLRPC$TL_auth_cancelCode.phone_code_hash = this.phoneHash;
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_cancelCode, LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda41.INSTANCE, 10);
                destroyTimer();
                destroyCodeTimer();
                this.currentParams = null;
                int i = this.currentType;
                if (i == 15) {
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                } else if (i == 2) {
                    AndroidUtilities.setWaitingForSms(false);
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                } else if (i == 3) {
                    AndroidUtilities.setWaitingForCall(false);
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
                }
                this.waitingForEvent = false;
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBackPressed$39(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            LoginActivity.this.setPage(0, true, null, true);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onDestroyActivity() {
            super.onDestroyActivity();
            int i = this.currentType;
            if (i == 15) {
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i == 2) {
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
            } else if (i == 3) {
                AndroidUtilities.setWaitingForCall(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveCall);
            }
            this.waitingForEvent = false;
            destroyTimer();
            destroyCodeTimer();
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onShow() {
            super.onShow();
            RLottieDrawable rLottieDrawable = this.hintDrawable;
            if (rLottieDrawable != null) {
                rLottieDrawable.setCurrentFrame(0);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySmsView.this.lambda$onShow$41();
                }
            }, LoginActivity.SHOW_DELAY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShow$41() {
            CodeNumberField[] codeNumberFieldArr;
            if (this.currentType != 3 && (codeNumberFieldArr = this.codeFieldContainer.codeField) != null) {
                for (int length = codeNumberFieldArr.length - 1; length >= 0; length--) {
                    if (length == 0 || this.codeFieldContainer.codeField[length].length() != 0) {
                        this.codeFieldContainer.codeField[length].requestFocus();
                        CodeNumberField[] codeNumberFieldArr2 = this.codeFieldContainer.codeField;
                        codeNumberFieldArr2[length].setSelection(codeNumberFieldArr2[length].length());
                        LoginActivity.this.showKeyboard(this.codeFieldContainer.codeField[length]);
                        break;
                    }
                }
            }
            RLottieDrawable rLottieDrawable = this.hintDrawable;
            if (rLottieDrawable != null) {
                rLottieDrawable.start();
            }
            if (this.currentType == 15) {
                this.openFragmentImageView.getAnimatedDrawable().setCurrentFrame(0, false);
                this.openFragmentImageView.getAnimatedDrawable().start();
            }
        }

        @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
        public void didReceivedNotification(int i, int i2, Object... objArr) {
            if (this.waitingForEvent) {
                CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
                if (codeFieldContainer.codeField == null) {
                    return;
                }
                if (i == NotificationCenter.didReceiveSmsCode) {
                    codeFieldContainer.setText("" + objArr[0]);
                    onNextPressed(null);
                } else if (i == NotificationCenter.didReceiveCall) {
                    String str = "" + objArr[0];
                    if (AndroidUtilities.checkPhonePattern(this.pattern, str)) {
                        if (!this.pattern.equals("*")) {
                            this.catchedPhone = str;
                            AndroidUtilities.endIncomingCall();
                        }
                        onNextPressed(str);
                    }
                }
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onHide() {
            super.onHide();
            this.isResendingCode = false;
            this.nextPressed = false;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void saveStateParams(Bundle bundle) {
            String code = this.codeFieldContainer.getCode();
            if (code.length() != 0) {
                bundle.putString("smsview_code_" + this.currentType, code);
            }
            String str = this.catchedPhone;
            if (str != null) {
                bundle.putString("catchedPhone", str);
            }
            if (this.currentParams != null) {
                bundle.putBundle("smsview_params_" + this.currentType, this.currentParams);
            }
            int i = this.time;
            if (i != 0) {
                bundle.putInt("time", i);
            }
            int i2 = this.openTime;
            if (i2 != 0) {
                bundle.putInt("open", i2);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void restoreStateParams(Bundle bundle) {
            Bundle bundle2 = bundle.getBundle("smsview_params_" + this.currentType);
            this.currentParams = bundle2;
            if (bundle2 != null) {
                setParams(bundle2, true);
            }
            String string = bundle.getString("catchedPhone");
            if (string != null) {
                this.catchedPhone = string;
            }
            String string2 = bundle.getString("smsview_code_" + this.currentType);
            if (string2 != null) {
                CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
                if (codeFieldContainer.codeField != null) {
                    codeFieldContainer.setText(string2);
                }
            }
            int i = bundle.getInt("time");
            if (i != 0) {
                this.time = i;
            }
            int i2 = bundle.getInt("open");
            if (i2 != 0) {
                this.openTime = i2;
            }
        }
    }

    /* loaded from: classes3.dex */
    public class LoginActivityPasswordView extends SlideView {
        private TextView cancelButton;
        private EditTextBoldCursor codeField;
        private TextView confirmTextView;
        private Bundle currentParams;
        private TLRPC$account_Password currentPassword;
        private RLottieImageView lockImageView;
        private boolean nextPressed;
        private OutlineTextContainerView outlineCodeField;
        private String passwordString;
        private String phoneCode;
        private String phoneHash;
        private String requestPhone;
        private TextView titleView;

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        /* JADX WARN: Removed duplicated region for block: B:13:0x0142  */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0144  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x01c9  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x01cc  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public LoginActivityPasswordView(final Context context) {
            super(context);
            int i;
            setOrientation(1);
            FrameLayout frameLayout = new FrameLayout(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.lockImageView = rLottieImageView;
            rLottieImageView.setAnimation(R.raw.tsv_setup_intro, 120, 120);
            this.lockImageView.setAutoRepeat(false);
            frameLayout.addView(this.lockImageView, LayoutHelper.createFrame(120, 120, 1));
            if (!AndroidUtilities.isSmallScreen()) {
                Point point = AndroidUtilities.displaySize;
                if (point.x <= point.y || AndroidUtilities.isTablet()) {
                    i = 0;
                    frameLayout.setVisibility(i);
                    addView(frameLayout, LayoutHelper.createFrame(-1, -2, 1));
                    TextView textView = new TextView(context);
                    this.titleView = textView;
                    textView.setTextSize(1, 18.0f);
                    this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    this.titleView.setText(LocaleController.getString(R.string.YourPasswordHeader));
                    this.titleView.setGravity(17);
                    this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
                    TextView textView2 = new TextView(context);
                    this.confirmTextView = textView2;
                    textView2.setTextSize(1, 14.0f);
                    this.confirmTextView.setGravity(1);
                    this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.confirmTextView.setText(LocaleController.getString(R.string.LoginPasswordTextShort));
                    addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 12, 8, 12, 0));
                    OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
                    this.outlineCodeField = outlineTextContainerView;
                    outlineTextContainerView.setText(LocaleController.getString(R.string.EnterPassword));
                    EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
                    this.codeField = editTextBoldCursor;
                    editTextBoldCursor.setCursorSize(AndroidUtilities.dp(20.0f));
                    this.codeField.setCursorWidth(1.5f);
                    this.codeField.setBackground(null);
                    this.codeField.setImeOptions(268435461);
                    this.codeField.setTextSize(1, 18.0f);
                    this.codeField.setMaxLines(1);
                    int dp = AndroidUtilities.dp(16.0f);
                    this.codeField.setPadding(dp, dp, dp, dp);
                    this.codeField.setInputType(129);
                    this.codeField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    this.codeField.setTypeface(Typeface.DEFAULT);
                    this.codeField.setGravity(!LocaleController.isRTL ? 5 : 3);
                    this.codeField.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnFocusChangeListener
                        public final void onFocusChange(View view, boolean z) {
                            LoginActivity.LoginActivityPasswordView.this.lambda$new$0(view, z);
                        }
                    });
                    this.outlineCodeField.attachEditText(this.codeField);
                    this.outlineCodeField.addView(this.codeField, LayoutHelper.createFrame(-1, -2, 48));
                    this.codeField.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda4
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView3, int i2, KeyEvent keyEvent) {
                            boolean lambda$new$1;
                            lambda$new$1 = LoginActivity.LoginActivityPasswordView.this.lambda$new$1(textView3, i2, keyEvent);
                            return lambda$new$1;
                        }
                    });
                    addView(this.outlineCodeField, LayoutHelper.createLinear(-1, -2, 1, 16, 32, 16, 0));
                    TextView textView3 = new TextView(context);
                    this.cancelButton = textView3;
                    textView3.setGravity(19);
                    this.cancelButton.setText(LocaleController.getString("ForgotPassword", R.string.ForgotPassword));
                    this.cancelButton.setTextSize(1, 15.0f);
                    this.cancelButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.cancelButton.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
                    FrameLayout frameLayout2 = new FrameLayout(context);
                    frameLayout2.addView(this.cancelButton, LayoutHelper.createFrame(-1, Build.VERSION.SDK_INT < 21 ? 56 : 60, 80, 0.0f, 0.0f, 0.0f, 32.0f));
                    addView(frameLayout2, LayoutHelper.createLinear(-1, -1, 80));
                    VerticalPositionAutoAnimator.attach(this.cancelButton);
                    this.cancelButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LoginActivity.LoginActivityPasswordView.this.lambda$new$6(context, view);
                        }
                    });
                }
            }
            i = 8;
            frameLayout.setVisibility(i);
            addView(frameLayout, LayoutHelper.createFrame(-1, -2, 1));
            TextView textView4 = new TextView(context);
            this.titleView = textView4;
            textView4.setTextSize(1, 18.0f);
            this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.titleView.setText(LocaleController.getString(R.string.YourPasswordHeader));
            this.titleView.setGravity(17);
            this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
            TextView textView22 = new TextView(context);
            this.confirmTextView = textView22;
            textView22.setTextSize(1, 14.0f);
            this.confirmTextView.setGravity(1);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.confirmTextView.setText(LocaleController.getString(R.string.LoginPasswordTextShort));
            addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 12, 8, 12, 0));
            OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
            this.outlineCodeField = outlineTextContainerView2;
            outlineTextContainerView2.setText(LocaleController.getString(R.string.EnterPassword));
            EditTextBoldCursor editTextBoldCursor2 = new EditTextBoldCursor(context);
            this.codeField = editTextBoldCursor2;
            editTextBoldCursor2.setCursorSize(AndroidUtilities.dp(20.0f));
            this.codeField.setCursorWidth(1.5f);
            this.codeField.setBackground(null);
            this.codeField.setImeOptions(268435461);
            this.codeField.setTextSize(1, 18.0f);
            this.codeField.setMaxLines(1);
            int dp2 = AndroidUtilities.dp(16.0f);
            this.codeField.setPadding(dp2, dp2, dp2, dp2);
            this.codeField.setInputType(129);
            this.codeField.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.codeField.setTypeface(Typeface.DEFAULT);
            this.codeField.setGravity(!LocaleController.isRTL ? 5 : 3);
            this.codeField.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda3
                @Override // android.view.View.OnFocusChangeListener
                public final void onFocusChange(View view, boolean z) {
                    LoginActivity.LoginActivityPasswordView.this.lambda$new$0(view, z);
                }
            });
            this.outlineCodeField.attachEditText(this.codeField);
            this.outlineCodeField.addView(this.codeField, LayoutHelper.createFrame(-1, -2, 48));
            this.codeField.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda4
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView32, int i2, KeyEvent keyEvent) {
                    boolean lambda$new$1;
                    lambda$new$1 = LoginActivity.LoginActivityPasswordView.this.lambda$new$1(textView32, i2, keyEvent);
                    return lambda$new$1;
                }
            });
            addView(this.outlineCodeField, LayoutHelper.createLinear(-1, -2, 1, 16, 32, 16, 0));
            TextView textView32 = new TextView(context);
            this.cancelButton = textView32;
            textView32.setGravity(19);
            this.cancelButton.setText(LocaleController.getString("ForgotPassword", R.string.ForgotPassword));
            this.cancelButton.setTextSize(1, 15.0f);
            this.cancelButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.cancelButton.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
            FrameLayout frameLayout22 = new FrameLayout(context);
            frameLayout22.addView(this.cancelButton, LayoutHelper.createFrame(-1, Build.VERSION.SDK_INT < 21 ? 56 : 60, 80, 0.0f, 0.0f, 0.0f, 32.0f));
            addView(frameLayout22, LayoutHelper.createLinear(-1, -1, 80));
            VerticalPositionAutoAnimator.attach(this.cancelButton);
            this.cancelButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LoginActivity.LoginActivityPasswordView.this.lambda$new$6(context, view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view, boolean z) {
            this.outlineCodeField.animateSelection(z ? 1.0f : 0.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$1(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 5) {
                onNextPressed(null);
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$6(Context context, View view) {
            if (LoginActivity.this.radialProgressView.getTag() != null) {
                return;
            }
            if (this.currentPassword.has_recovery) {
                LoginActivity.this.needShowProgress(0);
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(new TLRPC$TL_auth_requestPasswordRecovery(), new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda12
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LoginActivity.LoginActivityPasswordView.this.lambda$new$4(tLObject, tLRPC$TL_error);
                    }
                }, 10);
                return;
            }
            AndroidUtilities.hideKeyboard(this.codeField);
            new AlertDialog.Builder(context).setTitle(LocaleController.getString(R.string.RestorePasswordNoEmailTitle)).setMessage(LocaleController.getString(R.string.RestorePasswordNoEmailText)).setPositiveButton(LocaleController.getString(R.string.Close), null).setNegativeButton(LocaleController.getString(R.string.ResetAccount), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    LoginActivity.LoginActivityPasswordView.this.lambda$new$5(dialogInterface, i);
                }
            }).show();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$4(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityPasswordView.this.lambda$new$3(tLRPC$TL_error, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            String formatPluralString;
            LoginActivity.this.needHideProgress(false);
            if (tLRPC$TL_error == null) {
                final TLRPC$TL_auth_passwordRecovery tLRPC$TL_auth_passwordRecovery = (TLRPC$TL_auth_passwordRecovery) tLObject;
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                String str = tLRPC$TL_auth_passwordRecovery.email_pattern;
                SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(str);
                int indexOf = str.indexOf(42);
                int lastIndexOf = str.lastIndexOf(42);
                if (indexOf != lastIndexOf && indexOf != -1 && lastIndexOf != -1) {
                    TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                    textStyleRun.flags |= LiteMode.FLAG_CHAT_BLUR;
                    textStyleRun.start = indexOf;
                    int i = lastIndexOf + 1;
                    textStyleRun.end = i;
                    valueOf.setSpan(new TextStyleSpan(textStyleRun), indexOf, i, 0);
                }
                builder.setMessage(AndroidUtilities.formatSpannable(LocaleController.getString(R.string.RestoreEmailSent), valueOf));
                builder.setTitle(LocaleController.getString("RestoreEmailSentTitle", R.string.RestoreEmailSentTitle));
                builder.setPositiveButton(LocaleController.getString(R.string.Continue), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda1
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i2) {
                        LoginActivity.LoginActivityPasswordView.this.lambda$new$2(tLRPC$TL_auth_passwordRecovery, dialogInterface, i2);
                    }
                });
                Dialog showDialog = LoginActivity.this.showDialog(builder.create());
                if (showDialog != null) {
                    showDialog.setCanceledOnTouchOutside(false);
                    showDialog.setCancelable(false);
                }
            } else if (!tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), tLRPC$TL_error.text);
            } else {
                int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                if (intValue < 60) {
                    formatPluralString = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                } else {
                    formatPluralString = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                }
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.WrongCodeTitle), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(TLRPC$TL_auth_passwordRecovery tLRPC$TL_auth_passwordRecovery, DialogInterface dialogInterface, int i) {
            Bundle bundle = new Bundle();
            bundle.putString("email_unconfirmed_pattern", tLRPC$TL_auth_passwordRecovery.email_pattern);
            bundle.putString("password", this.passwordString);
            bundle.putString("requestPhone", this.requestPhone);
            bundle.putString("phoneHash", this.phoneHash);
            bundle.putString("phoneCode", this.phoneCode);
            LoginActivity.this.setPage(7, true, bundle, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$5(DialogInterface dialogInterface, int i) {
            LoginActivity.this.tryResetAccount(this.requestPhone, this.phoneHash, this.phoneCode);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void updateColors() {
            this.titleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.confirmTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.codeField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.codeField.setCursorColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.codeField.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
            this.cancelButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.outlineCodeField.updateColor();
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            return LocaleController.getString("LoginPassword", R.string.LoginPassword);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onCancelPressed() {
            this.nextPressed = false;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void setParams(Bundle bundle, boolean z) {
            if (bundle == null) {
                return;
            }
            if (bundle.isEmpty()) {
                AndroidUtilities.hideKeyboard(this.codeField);
                return;
            }
            this.codeField.setText("");
            this.currentParams = bundle;
            String string = bundle.getString("password");
            this.passwordString = string;
            if (string != null) {
                SerializedData serializedData = new SerializedData(Utilities.hexToBytes(string));
                this.currentPassword = TLRPC$account_Password.TLdeserialize(serializedData, serializedData.readInt32(false), false);
            }
            this.requestPhone = bundle.getString("phoneFormated");
            this.phoneHash = bundle.getString("phoneHash");
            this.phoneCode = bundle.getString("code");
            TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
            if (tLRPC$account_Password != null && !TextUtils.isEmpty(tLRPC$account_Password.hint)) {
                this.codeField.setHint(this.currentPassword.hint);
            } else {
                this.codeField.setHint((CharSequence) null);
            }
        }

        private void onPasscodeError(boolean z) {
            if (LoginActivity.this.getParentActivity() == null) {
                return;
            }
            if (z) {
                this.codeField.setText("");
            }
            LoginActivity.this.onFieldError(this.outlineCodeField, true);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onNextPressed(String str) {
            if (this.nextPressed) {
                return;
            }
            final String obj = this.codeField.getText().toString();
            if (obj.length() == 0) {
                onPasscodeError(false);
                return;
            }
            this.nextPressed = true;
            LoginActivity.this.needShowProgress(0);
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityPasswordView.this.lambda$onNextPressed$12(obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$12(String str) {
            TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = this.currentPassword.current_algo;
            boolean z = tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow;
            byte[] x = z ? SRPHelper.getX(AndroidUtilities.getStringBytes(str), (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo) : null;
            TLRPC$TL_auth_checkPassword tLRPC$TL_auth_checkPassword = new TLRPC$TL_auth_checkPassword();
            RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda13
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LoginActivity.LoginActivityPasswordView.this.lambda$onNextPressed$11(tLObject, tLRPC$TL_error);
                }
            };
            if (z) {
                TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
                TLRPC$TL_inputCheckPasswordSRP startCheck = SRPHelper.startCheck(x, tLRPC$account_Password.srp_id, tLRPC$account_Password.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo);
                tLRPC$TL_auth_checkPassword.password = startCheck;
                if (startCheck != null) {
                    ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_checkPassword, requestDelegate, 10);
                    return;
                }
                TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
                tLRPC$TL_error.text = "PASSWORD_HASH_INVALID";
                requestDelegate.run(null, tLRPC$TL_error);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$11(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityPasswordView.this.lambda$onNextPressed$10(tLRPC$TL_error, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$10(TLRPC$TL_error tLRPC$TL_error, final TLObject tLObject) {
            String formatPluralString;
            this.nextPressed = false;
            if (tLRPC$TL_error != null && "SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda11
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                        LoginActivity.LoginActivityPasswordView.this.lambda$onNextPressed$8(tLObject2, tLRPC$TL_error2);
                    }
                }, 8);
            } else if (tLObject instanceof TLRPC$TL_auth_authorization) {
                LoginActivity.this.showDoneButton(false, true);
                postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda7
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivityPasswordView.this.lambda$onNextPressed$9(tLObject);
                    }
                }, 150L);
            } else {
                LoginActivity.this.needHideProgress(false);
                if (tLRPC$TL_error.text.equals("PASSWORD_HASH_INVALID")) {
                    onPasscodeError(true);
                } else if (!tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), tLRPC$TL_error.text);
                } else {
                    int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                    if (intValue < 60) {
                        formatPluralString = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                    } else {
                        formatPluralString = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                    }
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$8(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityPasswordView.this.lambda$onNextPressed$7(tLRPC$TL_error, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$7(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            if (tLRPC$TL_error == null) {
                this.currentPassword = (TLRPC$account_Password) tLObject;
                onNextPressed(null);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$9(TLObject tLObject) {
            LoginActivity.this.needHideProgress(false, false);
            AndroidUtilities.hideKeyboard(this.codeField);
            LoginActivity.this.onAuthSuccess((TLRPC$TL_auth_authorization) tLObject);
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean onBackPressed(boolean z) {
            this.nextPressed = false;
            LoginActivity.this.needHideProgress(true);
            this.currentParams = null;
            return true;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onShow() {
            super.onShow();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityPasswordView.this.lambda$onShow$13();
                }
            }, LoginActivity.SHOW_DELAY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShow$13() {
            EditTextBoldCursor editTextBoldCursor = this.codeField;
            if (editTextBoldCursor != null) {
                editTextBoldCursor.requestFocus();
                EditTextBoldCursor editTextBoldCursor2 = this.codeField;
                editTextBoldCursor2.setSelection(editTextBoldCursor2.length());
                LoginActivity.this.showKeyboard(this.codeField);
                this.lockImageView.getAnimatedDrawable().setCurrentFrame(0, false);
                this.lockImageView.playAnimation();
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void saveStateParams(Bundle bundle) {
            String obj = this.codeField.getText().toString();
            if (obj.length() != 0) {
                bundle.putString("passview_code", obj);
            }
            Bundle bundle2 = this.currentParams;
            if (bundle2 != null) {
                bundle.putBundle("passview_params", bundle2);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void restoreStateParams(Bundle bundle) {
            Bundle bundle2 = bundle.getBundle("passview_params");
            this.currentParams = bundle2;
            if (bundle2 != null) {
                setParams(bundle2, true);
            }
            String string = bundle.getString("passview_code");
            if (string != null) {
                this.codeField.setText(string);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class LoginActivityResetWaitView extends SlideView {
        private TextView confirmTextView;
        private Bundle currentParams;
        private String phoneCode;
        private String phoneHash;
        private String requestPhone;
        private TextView resetAccountButton;
        private TextView resetAccountText;
        private TextView resetAccountTime;
        private int startTime;
        private Runnable timeRunnable;
        private TextView titleView;
        private RLottieImageView waitImageView;
        private int waitTime;
        private Boolean wasResetButtonActive;

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        public LoginActivityResetWaitView(Context context) {
            super(context);
            setOrientation(1);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setGravity(17);
            FrameLayout frameLayout = new FrameLayout(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.waitImageView = rLottieImageView;
            rLottieImageView.setAutoRepeat(true);
            this.waitImageView.setAnimation(R.raw.sandclock, 120, 120);
            frameLayout.addView(this.waitImageView, LayoutHelper.createFrame(120, 120, 1));
            Point point = AndroidUtilities.displaySize;
            frameLayout.setVisibility((point.x <= point.y || AndroidUtilities.isTablet()) ? 0 : 8);
            linearLayout.addView(frameLayout, LayoutHelper.createFrame(-1, -2, 1));
            TextView textView = new TextView(context);
            this.titleView = textView;
            textView.setTextSize(1, 18.0f);
            this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            TextView textView2 = this.titleView;
            int i = R.string.ResetAccount;
            textView2.setText(LocaleController.getString(i));
            this.titleView.setGravity(17);
            this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            linearLayout.addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
            TextView textView3 = new TextView(context);
            this.confirmTextView = textView3;
            textView3.setTextSize(1, 14.0f);
            this.confirmTextView.setGravity(1);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            linearLayout.addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 12, 8, 12, 0));
            addView(linearLayout, LayoutHelper.createLinear(-1, 0, 1.0f));
            TextView textView4 = new TextView(context);
            this.resetAccountText = textView4;
            textView4.setGravity(1);
            this.resetAccountText.setText(LocaleController.getString("ResetAccountStatus", R.string.ResetAccountStatus));
            this.resetAccountText.setTextSize(1, 14.0f);
            this.resetAccountText.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.resetAccountText, LayoutHelper.createLinear(-2, -2, 49, 0, 24, 0, 0));
            TextView textView5 = new TextView(context);
            this.resetAccountTime = textView5;
            textView5.setGravity(1);
            this.resetAccountTime.setTextSize(1, 20.0f);
            this.resetAccountTime.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.resetAccountTime.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.resetAccountTime, LayoutHelper.createLinear(-2, -2, 1, 0, 8, 0, 0));
            TextView textView6 = new TextView(context);
            this.resetAccountButton = textView6;
            textView6.setGravity(17);
            this.resetAccountButton.setText(LocaleController.getString(i));
            this.resetAccountButton.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.resetAccountButton.setTextSize(1, 15.0f);
            this.resetAccountButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.resetAccountButton.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
            this.resetAccountButton.setTextColor(-1);
            addView(this.resetAccountButton, LayoutHelper.createLinear(-1, 50, 1, 16, 32, 16, 48));
            this.resetAccountButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityResetWaitView$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LoginActivity.LoginActivityResetWaitView.this.lambda$new$3(view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(View view) {
            if (LoginActivity.this.radialProgressView.getTag() != null) {
                return;
            }
            LoginActivity loginActivity = LoginActivity.this;
            loginActivity.showDialog(new AlertDialog.Builder(loginActivity.getParentActivity()).setTitle(LocaleController.getString("ResetMyAccountWarning", R.string.ResetMyAccountWarning)).setMessage(LocaleController.getString("ResetMyAccountWarningText", R.string.ResetMyAccountWarningText)).setPositiveButton(LocaleController.getString("ResetMyAccountWarningReset", R.string.ResetMyAccountWarningReset), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityResetWaitView$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    LoginActivity.LoginActivityResetWaitView.this.lambda$new$2(dialogInterface, i);
                }
            }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).create());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(DialogInterface dialogInterface, int i) {
            LoginActivity.this.needShowProgress(0);
            TLRPC$TL_account_deleteAccount tLRPC$TL_account_deleteAccount = new TLRPC$TL_account_deleteAccount();
            tLRPC$TL_account_deleteAccount.reason = "Forgot password";
            ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_account_deleteAccount, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivityResetWaitView$$ExternalSyntheticLambda3
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LoginActivity.LoginActivityResetWaitView.this.lambda$new$1(tLObject, tLRPC$TL_error);
                }
            }, 10);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityResetWaitView$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityResetWaitView.this.lambda$new$0(tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(TLRPC$TL_error tLRPC$TL_error) {
            LoginActivity.this.needHideProgress(false);
            if (tLRPC$TL_error == null) {
                if (this.requestPhone == null || this.phoneHash == null || this.phoneCode == null) {
                    LoginActivity.this.setPage(0, true, null, true);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("phoneFormated", this.requestPhone);
                bundle.putString("phoneHash", this.phoneHash);
                bundle.putString("code", this.phoneCode);
                LoginActivity.this.setPage(5, true, bundle, false);
            } else if (tLRPC$TL_error.text.equals("2FA_RECENT_CONFIRM")) {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("ResetAccountCancelledAlert", R.string.ResetAccountCancelledAlert));
            } else {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), tLRPC$TL_error.text);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void updateColors() {
            this.titleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.confirmTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.resetAccountText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.resetAccountTime.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.resetAccountButton.setBackground(Theme.createSimpleSelectorRoundRectDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("changephoneinfo_image2"), Theme.getColor("chats_actionPressedBackground")));
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            return LocaleController.getString("ResetAccount", R.string.ResetAccount);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateTimeText() {
            int max = Math.max(0, this.waitTime - (ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).getCurrentTime() - this.startTime));
            int i = max / 86400;
            int round = Math.round(max / 86400.0f);
            int i2 = max / 3600;
            int i3 = (max / 60) % 60;
            int i4 = max % 60;
            if (i >= 2) {
                this.resetAccountTime.setText(LocaleController.formatPluralString("Days", round, new Object[0]));
            } else {
                this.resetAccountTime.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4)));
            }
            boolean z = max == 0;
            Boolean bool = this.wasResetButtonActive;
            if (bool == null || bool.booleanValue() != z) {
                if (!z) {
                    this.waitImageView.setAutoRepeat(true);
                    if (!this.waitImageView.isPlaying()) {
                        this.waitImageView.playAnimation();
                    }
                } else {
                    this.waitImageView.getAnimatedDrawable().setAutoRepeat(0);
                }
                this.resetAccountTime.setVisibility(z ? 4 : 0);
                this.resetAccountText.setVisibility(z ? 4 : 0);
                this.resetAccountButton.setVisibility(z ? 0 : 4);
                this.wasResetButtonActive = Boolean.valueOf(z);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void setParams(Bundle bundle, boolean z) {
            if (bundle == null) {
                return;
            }
            this.currentParams = bundle;
            this.requestPhone = bundle.getString("phoneFormated");
            this.phoneHash = bundle.getString("phoneHash");
            this.phoneCode = bundle.getString("code");
            this.startTime = bundle.getInt("startTime");
            this.waitTime = bundle.getInt("waitTime");
            TextView textView = this.confirmTextView;
            int i = R.string.ResetAccountInfo;
            PhoneFormat phoneFormat = PhoneFormat.getInstance();
            textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ResetAccountInfo", i, LocaleController.addNbsp(phoneFormat.format("+" + this.requestPhone)))));
            updateTimeText();
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.LoginActivity.LoginActivityResetWaitView.1
                @Override // java.lang.Runnable
                public void run() {
                    if (LoginActivityResetWaitView.this.timeRunnable != this) {
                        return;
                    }
                    LoginActivityResetWaitView.this.updateTimeText();
                    AndroidUtilities.runOnUIThread(LoginActivityResetWaitView.this.timeRunnable, 1000L);
                }
            };
            this.timeRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 1000L);
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean onBackPressed(boolean z) {
            LoginActivity.this.needHideProgress(true);
            AndroidUtilities.cancelRunOnUIThread(this.timeRunnable);
            this.timeRunnable = null;
            this.currentParams = null;
            return true;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void saveStateParams(Bundle bundle) {
            Bundle bundle2 = this.currentParams;
            if (bundle2 != null) {
                bundle.putBundle("resetview_params", bundle2);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void restoreStateParams(Bundle bundle) {
            Bundle bundle2 = bundle.getBundle("resetview_params");
            this.currentParams = bundle2;
            if (bundle2 != null) {
                setParams(bundle2, true);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class LoginActivitySetupEmail extends SlideView {
        private Bundle currentParams;
        private EditTextBoldCursor emailField;
        private OutlineTextContainerView emailOutlineView;
        private String emailPhone;
        private GoogleSignInAccount googleAccount;
        private RLottieImageView inboxImageView;
        private LoginOrView loginOrView;
        private boolean nextPressed;
        private String phone;
        private String phoneHash;
        private String requestPhone;
        private TextView signInWithGoogleView;
        private TextView subtitleView;
        private TextView titleView;

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        /* JADX WARN: Removed duplicated region for block: B:13:0x0076  */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0079  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x00f3  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x00f6  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public LoginActivitySetupEmail(Context context) {
            super(context);
            int i;
            setOrientation(1);
            FrameLayout frameLayout = new FrameLayout(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.inboxImageView = rLottieImageView;
            rLottieImageView.setAnimation(R.raw.tsv_setup_mail, 120, 120);
            this.inboxImageView.setAutoRepeat(false);
            frameLayout.addView(this.inboxImageView, LayoutHelper.createFrame(120, 120, 1));
            if (!AndroidUtilities.isSmallScreen()) {
                Point point = AndroidUtilities.displaySize;
                if (point.x <= point.y || AndroidUtilities.isTablet()) {
                    i = 0;
                    frameLayout.setVisibility(i);
                    addView(frameLayout, LayoutHelper.createFrame(-1, -2, 1));
                    TextView textView = new TextView(context);
                    this.titleView = textView;
                    textView.setTextSize(1, 18.0f);
                    this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    this.titleView.setText(LocaleController.getString(LoginActivity.this.activityMode != 3 ? R.string.EnterNewEmail : R.string.AddEmailTitle));
                    this.titleView.setGravity(17);
                    this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
                    TextView textView2 = new TextView(context);
                    this.subtitleView = textView2;
                    textView2.setTextSize(1, 14.0f);
                    this.subtitleView.setGravity(17);
                    this.subtitleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.subtitleView.setText(LocaleController.getString(R.string.AddEmailSubtitle));
                    addView(this.subtitleView, LayoutHelper.createLinear(-2, -2, 1, 32, 8, 32, 0));
                    OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
                    this.emailOutlineView = outlineTextContainerView;
                    outlineTextContainerView.setText(LocaleController.getString(LoginActivity.this.activityMode != 3 ? R.string.YourNewEmail : R.string.YourEmail));
                    EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
                    this.emailField = editTextBoldCursor;
                    editTextBoldCursor.setCursorSize(AndroidUtilities.dp(20.0f));
                    this.emailField.setCursorWidth(1.5f);
                    this.emailField.setImeOptions(268435461);
                    this.emailField.setTextSize(1, 17.0f);
                    this.emailField.setMaxLines(1);
                    this.emailField.setInputType(33);
                    this.emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnFocusChangeListener
                        public final void onFocusChange(View view, boolean z) {
                            LoginActivity.LoginActivitySetupEmail.this.lambda$new$0(view, z);
                        }
                    });
                    this.emailField.setBackground(null);
                    this.emailField.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    this.emailOutlineView.attachEditText(this.emailField);
                    this.emailOutlineView.addView(this.emailField, LayoutHelper.createFrame(-1, -2, 48));
                    this.emailField.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda2
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView3, int i2, KeyEvent keyEvent) {
                            boolean lambda$new$1;
                            lambda$new$1 = LoginActivity.LoginActivitySetupEmail.this.lambda$new$1(textView3, i2, keyEvent);
                            return lambda$new$1;
                        }
                    });
                    addView(this.emailOutlineView, LayoutHelper.createLinear(-1, 58, 16.0f, 24.0f, 16.0f, 0.0f));
                    TextView textView3 = new TextView(context);
                    this.signInWithGoogleView = textView3;
                    textView3.setGravity(3);
                    this.signInWithGoogleView.setTextSize(1, 14.0f);
                    this.signInWithGoogleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.signInWithGoogleView.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    this.signInWithGoogleView.setMaxLines(2);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("d ");
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.googleg_standard_color_18);
                    drawable.setBounds(0, AndroidUtilities.dp(9.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(27.0f));
                    spannableStringBuilder.setSpan(new ImageSpan(drawable, 0), 0, 1, 33);
                    spannableStringBuilder.setSpan(new ReplacementSpan(this, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivitySetupEmail.1
                        @Override // android.text.style.ReplacementSpan
                        public void draw(Canvas canvas, CharSequence charSequence, int i2, int i3, float f, int i4, int i5, int i6, Paint paint) {
                        }

                        @Override // android.text.style.ReplacementSpan
                        public int getSize(Paint paint, CharSequence charSequence, int i2, int i3, Paint.FontMetricsInt fontMetricsInt) {
                            return AndroidUtilities.dp(12.0f);
                        }
                    }, 1, 2, 33);
                    spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.SignInWithGoogle));
                    this.signInWithGoogleView.setText(spannableStringBuilder);
                    this.loginOrView = new LoginOrView(context);
                    addView(new Space(context), LayoutHelper.createLinear(0, 0, 1.0f));
                    FrameLayout frameLayout2 = new FrameLayout(context);
                    frameLayout2.addView(this.signInWithGoogleView, LayoutHelper.createFrame(-2, -2.0f, 83, 0.0f, 0.0f, 0.0f, 24.0f));
                    frameLayout2.addView(this.loginOrView, LayoutHelper.createFrame(-2, 16.0f, 83, 0.0f, 0.0f, 0.0f, 70.0f));
                    this.loginOrView.setMeasureAfter(this.signInWithGoogleView);
                    addView(frameLayout2, LayoutHelper.createLinear(-1, -2));
                    VerticalPositionAutoAnimator.attach(frameLayout2);
                    frameLayout2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LoginActivity.LoginActivitySetupEmail.this.lambda$new$3(view);
                        }
                    });
                }
            }
            i = 8;
            frameLayout.setVisibility(i);
            addView(frameLayout, LayoutHelper.createFrame(-1, -2, 1));
            TextView textView4 = new TextView(context);
            this.titleView = textView4;
            textView4.setTextSize(1, 18.0f);
            this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.titleView.setText(LocaleController.getString(LoginActivity.this.activityMode != 3 ? R.string.EnterNewEmail : R.string.AddEmailTitle));
            this.titleView.setGravity(17);
            this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
            TextView textView22 = new TextView(context);
            this.subtitleView = textView22;
            textView22.setTextSize(1, 14.0f);
            this.subtitleView.setGravity(17);
            this.subtitleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.subtitleView.setText(LocaleController.getString(R.string.AddEmailSubtitle));
            addView(this.subtitleView, LayoutHelper.createLinear(-2, -2, 1, 32, 8, 32, 0));
            OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
            this.emailOutlineView = outlineTextContainerView2;
            outlineTextContainerView2.setText(LocaleController.getString(LoginActivity.this.activityMode != 3 ? R.string.YourNewEmail : R.string.YourEmail));
            EditTextBoldCursor editTextBoldCursor2 = new EditTextBoldCursor(context);
            this.emailField = editTextBoldCursor2;
            editTextBoldCursor2.setCursorSize(AndroidUtilities.dp(20.0f));
            this.emailField.setCursorWidth(1.5f);
            this.emailField.setImeOptions(268435461);
            this.emailField.setTextSize(1, 17.0f);
            this.emailField.setMaxLines(1);
            this.emailField.setInputType(33);
            this.emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda1
                @Override // android.view.View.OnFocusChangeListener
                public final void onFocusChange(View view, boolean z) {
                    LoginActivity.LoginActivitySetupEmail.this.lambda$new$0(view, z);
                }
            });
            this.emailField.setBackground(null);
            this.emailField.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.emailOutlineView.attachEditText(this.emailField);
            this.emailOutlineView.addView(this.emailField, LayoutHelper.createFrame(-1, -2, 48));
            this.emailField.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda2
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView32, int i2, KeyEvent keyEvent) {
                    boolean lambda$new$1;
                    lambda$new$1 = LoginActivity.LoginActivitySetupEmail.this.lambda$new$1(textView32, i2, keyEvent);
                    return lambda$new$1;
                }
            });
            addView(this.emailOutlineView, LayoutHelper.createLinear(-1, 58, 16.0f, 24.0f, 16.0f, 0.0f));
            TextView textView32 = new TextView(context);
            this.signInWithGoogleView = textView32;
            textView32.setGravity(3);
            this.signInWithGoogleView.setTextSize(1, 14.0f);
            this.signInWithGoogleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.signInWithGoogleView.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.signInWithGoogleView.setMaxLines(2);
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder("d ");
            Drawable drawable2 = ContextCompat.getDrawable(context, R.drawable.googleg_standard_color_18);
            drawable2.setBounds(0, AndroidUtilities.dp(9.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(27.0f));
            spannableStringBuilder2.setSpan(new ImageSpan(drawable2, 0), 0, 1, 33);
            spannableStringBuilder2.setSpan(new ReplacementSpan(this, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivitySetupEmail.1
                @Override // android.text.style.ReplacementSpan
                public void draw(Canvas canvas, CharSequence charSequence, int i2, int i3, float f, int i4, int i5, int i6, Paint paint) {
                }

                @Override // android.text.style.ReplacementSpan
                public int getSize(Paint paint, CharSequence charSequence, int i2, int i3, Paint.FontMetricsInt fontMetricsInt) {
                    return AndroidUtilities.dp(12.0f);
                }
            }, 1, 2, 33);
            spannableStringBuilder2.append((CharSequence) LocaleController.getString(R.string.SignInWithGoogle));
            this.signInWithGoogleView.setText(spannableStringBuilder2);
            this.loginOrView = new LoginOrView(context);
            addView(new Space(context), LayoutHelper.createLinear(0, 0, 1.0f));
            FrameLayout frameLayout22 = new FrameLayout(context);
            frameLayout22.addView(this.signInWithGoogleView, LayoutHelper.createFrame(-2, -2.0f, 83, 0.0f, 0.0f, 0.0f, 24.0f));
            frameLayout22.addView(this.loginOrView, LayoutHelper.createFrame(-2, 16.0f, 83, 0.0f, 0.0f, 0.0f, 70.0f));
            this.loginOrView.setMeasureAfter(this.signInWithGoogleView);
            addView(frameLayout22, LayoutHelper.createLinear(-1, -2));
            VerticalPositionAutoAnimator.attach(frameLayout22);
            frameLayout22.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LoginActivity.LoginActivitySetupEmail.this.lambda$new$3(view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view, boolean z) {
            this.emailOutlineView.animateSelection(z ? 1.0f : 0.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$1(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 5) {
                onNextPressed(null);
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(View view) {
            NotificationCenter.getGlobalInstance().addObserver(new NotificationCenter.NotificationCenterDelegate() { // from class: org.telegram.ui.LoginActivity.LoginActivitySetupEmail.2
                @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
                public void didReceivedNotification(int i, int i2, Object... objArr) {
                    int intValue = ((Integer) objArr[0]).intValue();
                    ((Integer) objArr[1]).intValue();
                    Intent intent = (Intent) objArr[2];
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.onActivityResultReceived);
                    if (intValue == 200) {
                        try {
                            LoginActivitySetupEmail.this.googleAccount = GoogleSignIn.getSignedInAccountFromIntent(intent).getResult(ApiException.class);
                            LoginActivitySetupEmail.this.onNextPressed(null);
                        } catch (ApiException e) {
                            FileLog.e(e);
                        }
                    }
                }
            }, NotificationCenter.onActivityResultReceived);
            final GoogleSignInClient client = GoogleSignIn.getClient(getContext(), new GoogleSignInOptions.Builder().requestIdToken(BuildVars.GOOGLE_AUTH_CLIENT_ID).requestEmail().build());
            client.signOut().addOnCompleteListener(new OnCompleteListener() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda3
                @Override // com.google.android.gms.tasks.OnCompleteListener
                public final void onComplete(Task task) {
                    LoginActivity.LoginActivitySetupEmail.this.lambda$new$2(client, task);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(GoogleSignInClient googleSignInClient, Task task) {
            LoginActivity.this.getParentActivity().startActivityForResult(googleSignInClient.getSignInIntent(), 200);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void updateColors() {
            this.titleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.subtitleView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.emailField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.signInWithGoogleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.loginOrView.updateColors();
            this.emailOutlineView.invalidate();
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            return LocaleController.getString("AddEmailTitle", R.string.AddEmailTitle);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void setParams(Bundle bundle, boolean z) {
            if (bundle == null) {
                return;
            }
            this.emailField.setText("");
            this.currentParams = bundle;
            this.phone = bundle.getString("phone");
            this.emailPhone = this.currentParams.getString("ephone");
            this.requestPhone = this.currentParams.getString("phoneFormated");
            this.phoneHash = this.currentParams.getString("phoneHash");
            int i = bundle.getBoolean("googleSignInAllowed") ? 0 : 8;
            this.loginOrView.setVisibility(i);
            this.signInWithGoogleView.setVisibility(i);
            LoginActivity.this.showKeyboard(this.emailField);
            this.emailField.requestFocus();
        }

        private void onPasscodeError(boolean z) {
            if (LoginActivity.this.getParentActivity() == null) {
                return;
            }
            try {
                this.emailOutlineView.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            if (z) {
                this.emailField.setText("");
            }
            this.emailField.requestFocus();
            LoginActivity.this.onFieldError(this.emailOutlineView, true);
            postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySetupEmail.this.lambda$onPasscodeError$4();
                }
            }, 300L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPasscodeError$4() {
            this.emailField.requestFocus();
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onNextPressed(String str) {
            if (this.nextPressed) {
                return;
            }
            GoogleSignInAccount googleSignInAccount = this.googleAccount;
            String email = googleSignInAccount != null ? googleSignInAccount.getEmail() : this.emailField.getText().toString();
            final Bundle bundle = new Bundle();
            bundle.putString("phone", this.phone);
            bundle.putString("ephone", this.emailPhone);
            bundle.putString("phoneFormated", this.requestPhone);
            bundle.putString("phoneHash", this.phoneHash);
            bundle.putString("email", email);
            bundle.putBoolean("setup", true);
            if (this.googleAccount != null) {
                final TLRPC$TL_account_verifyEmail tLRPC$TL_account_verifyEmail = new TLRPC$TL_account_verifyEmail();
                if (LoginActivity.this.activityMode == 3) {
                    tLRPC$TL_account_verifyEmail.purpose = new TLRPC$TL_emailVerifyPurposeLoginChange();
                } else {
                    TLRPC$TL_emailVerifyPurposeLoginSetup tLRPC$TL_emailVerifyPurposeLoginSetup = new TLRPC$TL_emailVerifyPurposeLoginSetup();
                    tLRPC$TL_emailVerifyPurposeLoginSetup.phone_number = this.requestPhone;
                    tLRPC$TL_emailVerifyPurposeLoginSetup.phone_code_hash = this.phoneHash;
                    tLRPC$TL_account_verifyEmail.purpose = tLRPC$TL_emailVerifyPurposeLoginSetup;
                }
                TLRPC$TL_emailVerificationGoogle tLRPC$TL_emailVerificationGoogle = new TLRPC$TL_emailVerificationGoogle();
                tLRPC$TL_emailVerificationGoogle.token = this.googleAccount.getIdToken();
                tLRPC$TL_account_verifyEmail.verification = tLRPC$TL_emailVerificationGoogle;
                this.googleAccount = null;
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_account_verifyEmail, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda9
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LoginActivity.LoginActivitySetupEmail.this.lambda$onNextPressed$6(bundle, tLRPC$TL_account_verifyEmail, tLObject, tLRPC$TL_error);
                    }
                }, 10);
            } else if (TextUtils.isEmpty(email)) {
                onPasscodeError(false);
            } else {
                this.nextPressed = true;
                LoginActivity.this.needShowProgress(0);
                final TLRPC$TL_account_sendVerifyEmailCode tLRPC$TL_account_sendVerifyEmailCode = new TLRPC$TL_account_sendVerifyEmailCode();
                if (LoginActivity.this.activityMode == 3) {
                    tLRPC$TL_account_sendVerifyEmailCode.purpose = new TLRPC$TL_emailVerifyPurposeLoginChange();
                } else {
                    TLRPC$TL_emailVerifyPurposeLoginSetup tLRPC$TL_emailVerifyPurposeLoginSetup2 = new TLRPC$TL_emailVerifyPurposeLoginSetup();
                    tLRPC$TL_emailVerifyPurposeLoginSetup2.phone_number = this.requestPhone;
                    tLRPC$TL_emailVerifyPurposeLoginSetup2.phone_code_hash = this.phoneHash;
                    tLRPC$TL_account_sendVerifyEmailCode.purpose = tLRPC$TL_emailVerifyPurposeLoginSetup2;
                }
                tLRPC$TL_account_sendVerifyEmailCode.email = email;
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_account_sendVerifyEmailCode, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda8
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LoginActivity.LoginActivitySetupEmail.this.lambda$onNextPressed$8(bundle, tLRPC$TL_account_sendVerifyEmailCode, tLObject, tLRPC$TL_error);
                    }
                }, 10);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$6(final Bundle bundle, final TLRPC$TL_account_verifyEmail tLRPC$TL_account_verifyEmail, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySetupEmail.this.lambda$onNextPressed$5(tLObject, bundle, tLRPC$TL_error, tLRPC$TL_account_verifyEmail);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$5(TLObject tLObject, Bundle bundle, TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_account_verifyEmail tLRPC$TL_account_verifyEmail) {
            if ((tLObject instanceof TLRPC$TL_account_emailVerified) && LoginActivity.this.activityMode == 3) {
                LoginActivity.this.finishFragment();
                LoginActivity.this.emailChangeFinishCallback.run();
            } else if (tLObject instanceof TLRPC$TL_account_emailVerifiedLogin) {
                TLRPC$TL_account_emailVerifiedLogin tLRPC$TL_account_emailVerifiedLogin = (TLRPC$TL_account_emailVerifiedLogin) tLObject;
                bundle.putString("email", tLRPC$TL_account_emailVerifiedLogin.email);
                LoginActivity.this.lambda$resendCodeFromSafetyNet$18(bundle, tLRPC$TL_account_emailVerifiedLogin.sent_code);
            } else if (tLRPC$TL_error != null) {
                if (tLRPC$TL_error.text.contains("EMAIL_NOT_ALLOWED")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString(R.string.EmailNotAllowed));
                } else if (tLRPC$TL_error.text.contains("EMAIL_TOKEN_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString(R.string.EmailTokenInvalid));
                } else if (tLRPC$TL_error.code != -1000) {
                    AlertsCreator.processError(((BaseFragment) LoginActivity.this).currentAccount, tLRPC$TL_error, LoginActivity.this, tLRPC$TL_account_verifyEmail, new Object[0]);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$8(final Bundle bundle, final TLRPC$TL_account_sendVerifyEmailCode tLRPC$TL_account_sendVerifyEmailCode, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySetupEmail.this.lambda$onNextPressed$7(tLObject, bundle, tLRPC$TL_error, tLRPC$TL_account_sendVerifyEmailCode);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$7(TLObject tLObject, Bundle bundle, TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_account_sendVerifyEmailCode tLRPC$TL_account_sendVerifyEmailCode) {
            LoginActivity.this.needHideProgress(false);
            this.nextPressed = false;
            if (tLObject instanceof TLRPC$TL_account_sentEmailCode) {
                LoginActivity.this.fillNextCodeParams(bundle, (TLRPC$TL_account_sentEmailCode) tLObject);
                return;
            }
            String str = tLRPC$TL_error.text;
            if (str != null) {
                if (str.contains("EMAIL_INVALID")) {
                    onPasscodeError(false);
                } else if (tLRPC$TL_error.text.contains("EMAIL_NOT_ALLOWED")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString(R.string.EmailNotAllowed));
                } else if (tLRPC$TL_error.text.contains("PHONE_PASSWORD_FLOOD")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("FloodWait", R.string.FloodWait));
                } else if (tLRPC$TL_error.text.contains("PHONE_NUMBER_FLOOD")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("PhoneNumberFlood", R.string.PhoneNumberFlood));
                } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("InvalidCode", R.string.InvalidCode));
                } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("CodeExpired", R.string.CodeExpired));
                } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("FloodWait", R.string.FloodWait));
                } else if (tLRPC$TL_error.code != -1000) {
                    AlertsCreator.processError(((BaseFragment) LoginActivity.this).currentAccount, tLRPC$TL_error, LoginActivity.this, tLRPC$TL_account_sendVerifyEmailCode, this.requestPhone);
                }
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onShow() {
            super.onShow();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivitySetupEmail$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivitySetupEmail.this.lambda$onShow$9();
                }
            }, LoginActivity.SHOW_DELAY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShow$9() {
            this.inboxImageView.getAnimatedDrawable().setCurrentFrame(0, false);
            this.inboxImageView.playAnimation();
            this.emailField.requestFocus();
            AndroidUtilities.showKeyboard(this.emailField);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void saveStateParams(Bundle bundle) {
            String obj = this.emailField.getText().toString();
            if (obj != null && obj.length() != 0) {
                bundle.putString("emailsetup_email", obj);
            }
            Bundle bundle2 = this.currentParams;
            if (bundle2 != null) {
                bundle.putBundle("emailsetup_params", bundle2);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void restoreStateParams(Bundle bundle) {
            Bundle bundle2 = bundle.getBundle("emailsetup_params");
            this.currentParams = bundle2;
            if (bundle2 != null) {
                setParams(bundle2, true);
            }
            String string = bundle.getString("emailsetup_email");
            if (string != null) {
                this.emailField.setText(string);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class LoginActivityEmailCodeView extends SlideView {
        private CodeFieldContainer codeFieldContainer;
        private TextView confirmTextView;
        private Bundle currentParams;
        private String email;
        private String emailPhone;
        private Runnable errorColorTimeout;
        private ViewSwitcher errorViewSwitcher;
        private GoogleSignInAccount googleAccount;
        private RLottieImageView inboxImageView;
        private boolean isFromSetup;
        private boolean isSetup;
        private int length;
        private LoginOrView loginOrView;
        private boolean nextPressed;
        private String phone;
        private String phoneHash;
        private boolean postedErrorColorTimeout;
        private String requestPhone;
        private Runnable resendCodeTimeout;
        private TextView resendCodeView;
        private FrameLayout resendFrameLayout;
        private TextView signInWithGoogleView;
        private TextView titleView;
        private TextView wrongCodeView;

        @Override // org.telegram.ui.Components.SlideView
        public boolean hasCustomKeyboard() {
            return true;
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            int i = 0;
            this.postedErrorColorTimeout = false;
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i >= codeNumberFieldArr.length) {
                    break;
                }
                codeNumberFieldArr[i].animateErrorProgress(0.0f);
                i++;
            }
            if (this.errorViewSwitcher.getCurrentView() != this.resendFrameLayout) {
                this.errorViewSwitcher.showNext();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1() {
            showResendCodeView(true);
        }

        /* JADX WARN: Removed duplicated region for block: B:20:0x009b  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x009e  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0113  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x0116  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x0284  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x029c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public LoginActivityEmailCodeView(Context context, boolean z) {
            super(context);
            int i;
            this.errorColorTimeout = new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$new$0();
                }
            };
            this.resendCodeTimeout = new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$new$1();
                }
            };
            this.isSetup = z;
            setOrientation(1);
            FrameLayout frameLayout = new FrameLayout(context);
            this.inboxImageView = new RLottieImageView(context);
            if (!z || LoginActivity.this.activityMode == 3) {
                this.inboxImageView.setAnimation(R.raw.email_check_inbox, 120, 120);
            } else {
                this.inboxImageView.setAnimation(R.raw.email_setup_heart, 120, 120);
            }
            this.inboxImageView.setAutoRepeat(false);
            frameLayout.addView(this.inboxImageView, LayoutHelper.createFrame(120, 120, 1));
            if (!AndroidUtilities.isSmallScreen()) {
                Point point = AndroidUtilities.displaySize;
                if (point.x <= point.y || AndroidUtilities.isTablet()) {
                    i = 0;
                    frameLayout.setVisibility(i);
                    addView(frameLayout, LayoutHelper.createFrame(-1, -2, 1));
                    TextView textView = new TextView(context);
                    this.titleView = textView;
                    textView.setTextSize(1, 18.0f);
                    this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    this.titleView.setText(LocaleController.getString(LoginActivity.this.activityMode != 3 ? R.string.CheckYourNewEmail : z ? R.string.VerificationCode : R.string.CheckYourEmail));
                    this.titleView.setGravity(17);
                    this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
                    SpoilersTextView spoilersTextView = new SpoilersTextView(context, false);
                    this.confirmTextView = spoilersTextView;
                    spoilersTextView.setTextSize(1, 14.0f);
                    this.confirmTextView.setGravity(17);
                    this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
                    CodeFieldContainer codeFieldContainer = new CodeFieldContainer(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityEmailCodeView.1
                        @Override // org.telegram.ui.CodeFieldContainer
                        protected void processNextPressed() {
                            LoginActivityEmailCodeView.this.onNextPressed(null);
                        }
                    };
                    this.codeFieldContainer = codeFieldContainer;
                    addView(codeFieldContainer, LayoutHelper.createLinear(-2, 42, 1, 0, !z ? 48 : 32, 0, 0));
                    TextView textView2 = new TextView(context);
                    this.signInWithGoogleView = textView2;
                    textView2.setGravity(17);
                    this.signInWithGoogleView.setTextSize(1, 14.0f);
                    this.signInWithGoogleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.signInWithGoogleView.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    this.signInWithGoogleView.setMaxLines(2);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("d ");
                    Drawable drawable = ContextCompat.getDrawable(context, R.drawable.googleg_standard_color_18);
                    drawable.setBounds(0, AndroidUtilities.dp(9.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(27.0f));
                    spannableStringBuilder.setSpan(new ImageSpan(drawable, 0), 0, 1, 33);
                    spannableStringBuilder.setSpan(new ReplacementSpan(this, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityEmailCodeView.2
                        @Override // android.text.style.ReplacementSpan
                        public void draw(Canvas canvas, CharSequence charSequence, int i2, int i3, float f, int i4, int i5, int i6, Paint paint) {
                        }

                        @Override // android.text.style.ReplacementSpan
                        public int getSize(Paint paint, CharSequence charSequence, int i2, int i3, Paint.FontMetricsInt fontMetricsInt) {
                            return AndroidUtilities.dp(12.0f);
                        }
                    }, 1, 2, 33);
                    spannableStringBuilder.append((CharSequence) LocaleController.getString(R.string.SignInWithGoogle));
                    this.signInWithGoogleView.setText(spannableStringBuilder);
                    this.signInWithGoogleView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda0
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LoginActivity.LoginActivityEmailCodeView.this.lambda$new$3(view);
                        }
                    });
                    TextView textView3 = new TextView(context);
                    this.resendCodeView = textView3;
                    textView3.setGravity(17);
                    this.resendCodeView.setTextSize(1, 14.0f);
                    this.resendCodeView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.resendCodeView.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    this.resendCodeView.setMaxLines(2);
                    this.resendCodeView.setText(LocaleController.getString(R.string.ResendCode));
                    this.resendCodeView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LoginActivity.LoginActivityEmailCodeView.this.lambda$new$6(view);
                        }
                    });
                    AndroidUtilities.updateViewVisibilityAnimated(this.resendCodeView, false, 1.0f, false);
                    LoginOrView loginOrView = new LoginOrView(context);
                    this.loginOrView = loginOrView;
                    VerticalPositionAutoAnimator.attach(loginOrView);
                    this.errorViewSwitcher = new ViewSwitcher(this, context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityEmailCodeView.4
                        @Override // android.widget.FrameLayout, android.view.View
                        protected void onMeasure(int i2, int i3) {
                            super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                        }
                    };
                    Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.text_in);
                    Interpolator interpolator = Easings.easeInOutQuad;
                    loadAnimation.setInterpolator(interpolator);
                    this.errorViewSwitcher.setInAnimation(loadAnimation);
                    Animation loadAnimation2 = AnimationUtils.loadAnimation(context, R.anim.text_out);
                    loadAnimation2.setInterpolator(interpolator);
                    this.errorViewSwitcher.setOutAnimation(loadAnimation2);
                    FrameLayout frameLayout2 = new FrameLayout(context);
                    this.resendFrameLayout = frameLayout2;
                    frameLayout2.addView(this.resendCodeView, LayoutHelper.createFrame(-2, -2, 17));
                    this.errorViewSwitcher.addView(this.resendFrameLayout);
                    TextView textView4 = new TextView(context);
                    this.wrongCodeView = textView4;
                    textView4.setText(LocaleController.getString("WrongCode", R.string.WrongCode));
                    this.wrongCodeView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.wrongCodeView.setTextSize(1, 15.0f);
                    this.wrongCodeView.setGravity(49);
                    this.wrongCodeView.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
                    this.errorViewSwitcher.addView(this.wrongCodeView);
                    FrameLayout frameLayout3 = new FrameLayout(context);
                    if (!z) {
                        frameLayout3.addView(this.errorViewSwitcher, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 32.0f));
                    } else {
                        frameLayout3.addView(this.errorViewSwitcher, LayoutHelper.createFrame(-1, -2.0f, 48, 0.0f, 8.0f, 0.0f, 0.0f));
                        frameLayout3.addView(this.loginOrView, LayoutHelper.createFrame(-1, 16.0f, 17, 0.0f, 0.0f, 0.0f, 16.0f));
                        frameLayout3.addView(this.signInWithGoogleView, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 16.0f));
                    }
                    addView(frameLayout3, LayoutHelper.createLinear(-1, 0, 1.0f));
                }
            }
            i = 8;
            frameLayout.setVisibility(i);
            addView(frameLayout, LayoutHelper.createFrame(-1, -2, 1));
            TextView textView5 = new TextView(context);
            this.titleView = textView5;
            textView5.setTextSize(1, 18.0f);
            this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.titleView.setText(LocaleController.getString(LoginActivity.this.activityMode != 3 ? R.string.CheckYourNewEmail : z ? R.string.VerificationCode : R.string.CheckYourEmail));
            this.titleView.setGravity(17);
            this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
            SpoilersTextView spoilersTextView2 = new SpoilersTextView(context, false);
            this.confirmTextView = spoilersTextView2;
            spoilersTextView2.setTextSize(1, 14.0f);
            this.confirmTextView.setGravity(17);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
            CodeFieldContainer codeFieldContainer2 = new CodeFieldContainer(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityEmailCodeView.1
                @Override // org.telegram.ui.CodeFieldContainer
                protected void processNextPressed() {
                    LoginActivityEmailCodeView.this.onNextPressed(null);
                }
            };
            this.codeFieldContainer = codeFieldContainer2;
            addView(codeFieldContainer2, LayoutHelper.createLinear(-2, 42, 1, 0, !z ? 48 : 32, 0, 0));
            TextView textView22 = new TextView(context);
            this.signInWithGoogleView = textView22;
            textView22.setGravity(17);
            this.signInWithGoogleView.setTextSize(1, 14.0f);
            this.signInWithGoogleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.signInWithGoogleView.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.signInWithGoogleView.setMaxLines(2);
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder("d ");
            Drawable drawable2 = ContextCompat.getDrawable(context, R.drawable.googleg_standard_color_18);
            drawable2.setBounds(0, AndroidUtilities.dp(9.0f), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(27.0f));
            spannableStringBuilder2.setSpan(new ImageSpan(drawable2, 0), 0, 1, 33);
            spannableStringBuilder2.setSpan(new ReplacementSpan(this, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityEmailCodeView.2
                @Override // android.text.style.ReplacementSpan
                public void draw(Canvas canvas, CharSequence charSequence, int i2, int i3, float f, int i4, int i5, int i6, Paint paint) {
                }

                @Override // android.text.style.ReplacementSpan
                public int getSize(Paint paint, CharSequence charSequence, int i2, int i3, Paint.FontMetricsInt fontMetricsInt) {
                    return AndroidUtilities.dp(12.0f);
                }
            }, 1, 2, 33);
            spannableStringBuilder2.append((CharSequence) LocaleController.getString(R.string.SignInWithGoogle));
            this.signInWithGoogleView.setText(spannableStringBuilder2);
            this.signInWithGoogleView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$new$3(view);
                }
            });
            TextView textView32 = new TextView(context);
            this.resendCodeView = textView32;
            textView32.setGravity(17);
            this.resendCodeView.setTextSize(1, 14.0f);
            this.resendCodeView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.resendCodeView.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.resendCodeView.setMaxLines(2);
            this.resendCodeView.setText(LocaleController.getString(R.string.ResendCode));
            this.resendCodeView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$new$6(view);
                }
            });
            AndroidUtilities.updateViewVisibilityAnimated(this.resendCodeView, false, 1.0f, false);
            LoginOrView loginOrView2 = new LoginOrView(context);
            this.loginOrView = loginOrView2;
            VerticalPositionAutoAnimator.attach(loginOrView2);
            this.errorViewSwitcher = new ViewSwitcher(this, context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityEmailCodeView.4
                @Override // android.widget.FrameLayout, android.view.View
                protected void onMeasure(int i2, int i3) {
                    super.onMeasure(i2, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
                }
            };
            Animation loadAnimation3 = AnimationUtils.loadAnimation(context, R.anim.text_in);
            Interpolator interpolator2 = Easings.easeInOutQuad;
            loadAnimation3.setInterpolator(interpolator2);
            this.errorViewSwitcher.setInAnimation(loadAnimation3);
            Animation loadAnimation22 = AnimationUtils.loadAnimation(context, R.anim.text_out);
            loadAnimation22.setInterpolator(interpolator2);
            this.errorViewSwitcher.setOutAnimation(loadAnimation22);
            FrameLayout frameLayout22 = new FrameLayout(context);
            this.resendFrameLayout = frameLayout22;
            frameLayout22.addView(this.resendCodeView, LayoutHelper.createFrame(-2, -2, 17));
            this.errorViewSwitcher.addView(this.resendFrameLayout);
            TextView textView42 = new TextView(context);
            this.wrongCodeView = textView42;
            textView42.setText(LocaleController.getString("WrongCode", R.string.WrongCode));
            this.wrongCodeView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.wrongCodeView.setTextSize(1, 15.0f);
            this.wrongCodeView.setGravity(49);
            this.wrongCodeView.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
            this.errorViewSwitcher.addView(this.wrongCodeView);
            FrameLayout frameLayout32 = new FrameLayout(context);
            if (!z) {
            }
            addView(frameLayout32, LayoutHelper.createLinear(-1, 0, 1.0f));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(View view) {
            NotificationCenter.getGlobalInstance().addObserver(new NotificationCenter.NotificationCenterDelegate() { // from class: org.telegram.ui.LoginActivity.LoginActivityEmailCodeView.3
                @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
                public void didReceivedNotification(int i, int i2, Object... objArr) {
                    int intValue = ((Integer) objArr[0]).intValue();
                    ((Integer) objArr[1]).intValue();
                    Intent intent = (Intent) objArr[2];
                    NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.onActivityResultReceived);
                    if (intValue == 200) {
                        try {
                            LoginActivityEmailCodeView.this.googleAccount = GoogleSignIn.getSignedInAccountFromIntent(intent).getResult(ApiException.class);
                            LoginActivityEmailCodeView.this.onNextPressed(null);
                        } catch (ApiException e) {
                            FileLog.e(e);
                        }
                    }
                }
            }, NotificationCenter.onActivityResultReceived);
            final GoogleSignInClient client = GoogleSignIn.getClient(getContext(), new GoogleSignInOptions.Builder().requestIdToken(BuildVars.GOOGLE_AUTH_CLIENT_ID).requestEmail().build());
            client.signOut().addOnCompleteListener(new OnCompleteListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda3
                @Override // com.google.android.gms.tasks.OnCompleteListener
                public final void onComplete(Task task) {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$new$2(client, task);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(GoogleSignInClient googleSignInClient, Task task) {
            LoginActivity.this.getParentActivity().startActivityForResult(googleSignInClient.getSignInIntent(), 200);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$6(View view) {
            if (this.resendCodeView.getVisibility() == 0 && this.resendCodeView.getAlpha() == 1.0f) {
                showResendCodeView(false);
                final TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode = new TLRPC$TL_auth_resendCode();
                tLRPC$TL_auth_resendCode.phone_number = this.requestPhone;
                tLRPC$TL_auth_resendCode.phone_code_hash = this.phoneHash;
                final Bundle bundle = new Bundle();
                bundle.putString("phone", this.phone);
                bundle.putString("ephone", this.emailPhone);
                bundle.putString("phoneFormated", this.requestPhone);
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_resendCode, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda19
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LoginActivity.LoginActivityEmailCodeView.this.lambda$new$5(bundle, tLRPC$TL_auth_resendCode, tLObject, tLRPC$TL_error);
                    }
                }, 10);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$5(final Bundle bundle, final TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$new$4(tLObject, bundle, tLRPC$TL_error, tLRPC$TL_auth_resendCode);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$4(TLObject tLObject, Bundle bundle, TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode) {
            if (tLObject instanceof TLRPC$TL_auth_sentCode) {
                LoginActivity.this.lambda$resendCodeFromSafetyNet$18(bundle, (TLRPC$TL_auth_sentCode) tLObject);
            } else if (tLRPC$TL_error == null || tLRPC$TL_error.text == null) {
            } else {
                AlertsCreator.processError(((BaseFragment) LoginActivity.this).currentAccount, tLRPC$TL_error, LoginActivity.this, tLRPC$TL_auth_resendCode, new Object[0]);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void updateColors() {
            this.titleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.confirmTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.signInWithGoogleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.loginOrView.updateColors();
            this.resendCodeView.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.wrongCodeView.setTextColor(Theme.getColor("dialogTextRed"));
            this.codeFieldContainer.invalidate();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            removeCallbacks(this.errorColorTimeout);
            removeCallbacks(this.resendCodeTimeout);
        }

        private void showResendCodeView(boolean z) {
            AndroidUtilities.updateViewVisibilityAnimated(this.resendCodeView, z);
            if (this.loginOrView.getVisibility() != 8) {
                this.loginOrView.setLayoutParams(LayoutHelper.createFrame(-1, 16.0f, 17, 0.0f, 0.0f, 0.0f, z ? 8.0f : 16.0f));
                this.loginOrView.requestLayout();
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            return LocaleController.getString(R.string.VerificationCode);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void setParams(Bundle bundle, boolean z) {
            CodeNumberField[] codeNumberFieldArr;
            if (bundle == null) {
                return;
            }
            this.currentParams = bundle;
            this.requestPhone = bundle.getString("phoneFormated");
            this.phoneHash = this.currentParams.getString("phoneHash");
            this.phone = this.currentParams.getString("phone");
            this.emailPhone = this.currentParams.getString("ephone");
            this.isFromSetup = this.currentParams.getBoolean("setup");
            this.length = this.currentParams.getInt("length");
            this.email = this.currentParams.getString("email");
            if (LoginActivity.this.activityMode == 3) {
                this.confirmTextView.setText(LocaleController.formatString(R.string.CheckYourNewEmailSubtitle, this.email));
            } else if (this.isSetup) {
                this.confirmTextView.setText(LocaleController.formatString(R.string.VerificationCodeSubtitle, this.email));
            }
            this.codeFieldContainer.setNumbersCount(this.length, 1);
            for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                codeNumberField.setShowSoftInputOnFocusCompat(!hasCustomKeyboard() || LoginActivity.this.isCustomKeyboardForceDisabled());
                codeNumberField.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.LoginActivity.LoginActivityEmailCodeView.5
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        if (LoginActivityEmailCodeView.this.postedErrorColorTimeout) {
                            LoginActivityEmailCodeView loginActivityEmailCodeView = LoginActivityEmailCodeView.this;
                            loginActivityEmailCodeView.removeCallbacks(loginActivityEmailCodeView.errorColorTimeout);
                            LoginActivityEmailCodeView.this.errorColorTimeout.run();
                        }
                    }
                });
                codeNumberField.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnFocusChangeListener
                    public final void onFocusChange(View view, boolean z2) {
                        LoginActivity.LoginActivityEmailCodeView.this.lambda$setParams$7(view, z2);
                    }
                });
            }
            this.codeFieldContainer.setText("");
            if (!this.isFromSetup && LoginActivity.this.activityMode != 3) {
                String string = this.currentParams.getString("emailPattern");
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
                int indexOf = string.indexOf(42);
                int lastIndexOf = string.lastIndexOf(42);
                if (indexOf != lastIndexOf && indexOf != -1 && lastIndexOf != -1) {
                    TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                    textStyleRun.flags |= LiteMode.FLAG_CHAT_BLUR;
                    textStyleRun.start = indexOf;
                    int i = lastIndexOf + 1;
                    textStyleRun.end = i;
                    spannableStringBuilder.setSpan(new TextStyleSpan(textStyleRun), indexOf, i, 0);
                }
                this.confirmTextView.setText(AndroidUtilities.formatSpannable(LocaleController.getString(R.string.CheckYourEmailSubtitle), spannableStringBuilder));
            }
            int i2 = bundle.getBoolean("googleSignInAllowed") ? 0 : 8;
            this.loginOrView.setVisibility(i2);
            this.signInWithGoogleView.setVisibility(i2);
            LoginActivity.this.showKeyboard(this.codeFieldContainer.codeField[0]);
            this.codeFieldContainer.requestFocus();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setParams$7(View view, boolean z) {
            if (z) {
                LoginActivity.this.keyboardView.setEditText((EditText) view);
                LoginActivity.this.keyboardView.setDispatchBackWhenEmpty(true);
            }
        }

        private void onPasscodeError(boolean z) {
            if (LoginActivity.this.getParentActivity() == null) {
                return;
            }
            try {
                this.codeFieldContainer.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            if (z) {
                for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                    codeNumberField.setText("");
                }
            }
            for (CodeNumberField codeNumberField2 : this.codeFieldContainer.codeField) {
                codeNumberField2.animateErrorProgress(1.0f);
            }
            this.codeFieldContainer.codeField[0].requestFocus();
            AndroidUtilities.shakeViewSpring(this.codeFieldContainer, new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$onPasscodeError$9();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPasscodeError$9() {
            postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$onPasscodeError$8();
                }
            }, 150L);
            removeCallbacks(this.errorColorTimeout);
            postDelayed(this.errorColorTimeout, 3000L);
            this.postedErrorColorTimeout = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPasscodeError$8() {
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            int i = 0;
            codeFieldContainer.isFocusSuppressed = false;
            codeFieldContainer.codeField[0].requestFocus();
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i >= codeNumberFieldArr.length) {
                    return;
                }
                codeNumberFieldArr[i].animateErrorProgress(0.0f);
                i++;
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.telegram.ui.Components.SlideView
        public void onNextPressed(String str) {
            TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn;
            if (this.nextPressed) {
                return;
            }
            AndroidUtilities.cancelRunOnUIThread(this.resendCodeTimeout);
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            codeFieldContainer.isFocusSuppressed = true;
            for (CodeNumberField codeNumberField : codeFieldContainer.codeField) {
                codeNumberField.animateFocusedProgress(0.0f);
            }
            final String code = this.codeFieldContainer.getCode();
            if (code.length() == 0 && this.googleAccount == null) {
                onPasscodeError(false);
                return;
            }
            this.nextPressed = true;
            LoginActivity.this.needShowProgress(0);
            if (LoginActivity.this.activityMode == 3) {
                TLRPC$TL_account_verifyEmail tLRPC$TL_account_verifyEmail = new TLRPC$TL_account_verifyEmail();
                tLRPC$TL_account_verifyEmail.purpose = new TLRPC$TL_emailVerifyPurposeLoginChange();
                TLRPC$TL_emailVerificationCode tLRPC$TL_emailVerificationCode = new TLRPC$TL_emailVerificationCode();
                tLRPC$TL_emailVerificationCode.code = code;
                tLRPC$TL_account_verifyEmail.verification = tLRPC$TL_emailVerificationCode;
                tLRPC$TL_auth_signIn = tLRPC$TL_account_verifyEmail;
            } else if (this.isFromSetup) {
                TLRPC$TL_account_verifyEmail tLRPC$TL_account_verifyEmail2 = new TLRPC$TL_account_verifyEmail();
                TLRPC$TL_emailVerifyPurposeLoginSetup tLRPC$TL_emailVerifyPurposeLoginSetup = new TLRPC$TL_emailVerifyPurposeLoginSetup();
                tLRPC$TL_emailVerifyPurposeLoginSetup.phone_number = this.requestPhone;
                tLRPC$TL_emailVerifyPurposeLoginSetup.phone_code_hash = this.phoneHash;
                tLRPC$TL_account_verifyEmail2.purpose = tLRPC$TL_emailVerifyPurposeLoginSetup;
                TLRPC$TL_emailVerificationCode tLRPC$TL_emailVerificationCode2 = new TLRPC$TL_emailVerificationCode();
                tLRPC$TL_emailVerificationCode2.code = code;
                tLRPC$TL_account_verifyEmail2.verification = tLRPC$TL_emailVerificationCode2;
                tLRPC$TL_auth_signIn = tLRPC$TL_account_verifyEmail2;
            } else {
                TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn2 = new TLRPC$TL_auth_signIn();
                tLRPC$TL_auth_signIn2.phone_number = this.requestPhone;
                tLRPC$TL_auth_signIn2.phone_code_hash = this.phoneHash;
                if (this.googleAccount != null) {
                    TLRPC$TL_emailVerificationGoogle tLRPC$TL_emailVerificationGoogle = new TLRPC$TL_emailVerificationGoogle();
                    tLRPC$TL_emailVerificationGoogle.token = this.googleAccount.getIdToken();
                    tLRPC$TL_auth_signIn2.email_verification = tLRPC$TL_emailVerificationGoogle;
                } else {
                    TLRPC$TL_emailVerificationCode tLRPC$TL_emailVerificationCode3 = new TLRPC$TL_emailVerificationCode();
                    tLRPC$TL_emailVerificationCode3.code = code;
                    tLRPC$TL_auth_signIn2.email_verification = tLRPC$TL_emailVerificationCode3;
                }
                tLRPC$TL_auth_signIn2.flags |= 2;
                tLRPC$TL_auth_signIn = tLRPC$TL_auth_signIn2;
            }
            CodeFieldContainer codeFieldContainer2 = this.codeFieldContainer;
            codeFieldContainer2.isFocusSuppressed = true;
            for (CodeNumberField codeNumberField2 : codeFieldContainer2.codeField) {
                codeNumberField2.animateFocusedProgress(0.0f);
            }
            ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_signIn, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda20
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$onNextPressed$16(code, tLObject, tLRPC$TL_error);
                }
            }, 10);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$16(final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda17
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$onNextPressed$15(tLRPC$TL_error, str, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Removed duplicated region for block: B:42:0x017c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$onNextPressed$15(TLRPC$TL_error tLRPC$TL_error, final String str, final TLObject tLObject) {
            CodeNumberField[] codeNumberFieldArr;
            LoginActivity.this.needHideProgress(false);
            boolean z = true;
            if (tLRPC$TL_error == null) {
                this.nextPressed = false;
                LoginActivity.this.showDoneButton(false, true);
                final Bundle bundle = new Bundle();
                bundle.putString("phone", this.phone);
                bundle.putString("ephone", this.emailPhone);
                bundle.putString("phoneFormated", this.requestPhone);
                bundle.putString("phoneHash", this.phoneHash);
                bundle.putString("code", str);
                if (tLObject instanceof TLRPC$TL_auth_authorizationSignUpRequired) {
                    TLRPC$TL_help_termsOfService tLRPC$TL_help_termsOfService = ((TLRPC$TL_auth_authorizationSignUpRequired) tLObject).terms_of_service;
                    if (tLRPC$TL_help_termsOfService != null) {
                        LoginActivity.this.currentTermsOfService = tLRPC$TL_help_termsOfService;
                    }
                    animateSuccess(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda13
                        @Override // java.lang.Runnable
                        public final void run() {
                            LoginActivity.LoginActivityEmailCodeView.this.lambda$onNextPressed$10(bundle);
                        }
                    });
                } else {
                    animateSuccess(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda15
                        @Override // java.lang.Runnable
                        public final void run() {
                            LoginActivity.LoginActivityEmailCodeView.this.lambda$onNextPressed$11(tLObject, bundle);
                        }
                    });
                }
            } else if (tLRPC$TL_error.text.contains("SESSION_PASSWORD_NEEDED")) {
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda21
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                        LoginActivity.LoginActivityEmailCodeView.this.lambda$onNextPressed$14(str, tLObject2, tLRPC$TL_error2);
                    }
                }, 10);
            } else {
                this.nextPressed = false;
                LoginActivity.this.showDoneButton(false, true);
                if (tLRPC$TL_error.text.contains("EMAIL_ADDRESS_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString(R.string.EmailAddressInvalid));
                } else if (tLRPC$TL_error.text.contains("PHONE_NUMBER_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("InvalidPhoneNumber", R.string.InvalidPhoneNumber));
                } else if (tLRPC$TL_error.text.contains("CODE_EMPTY") || tLRPC$TL_error.text.contains("CODE_INVALID") || tLRPC$TL_error.text.contains("EMAIL_CODE_INVALID") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                    shakeWrongCode();
                    if (!z) {
                        if (this.codeFieldContainer.codeField != null) {
                            int i = 0;
                            while (true) {
                                codeNumberFieldArr = this.codeFieldContainer.codeField;
                                if (i >= codeNumberFieldArr.length) {
                                    break;
                                }
                                codeNumberFieldArr[i].setText("");
                                i++;
                            }
                            codeNumberFieldArr[0].requestFocus();
                        }
                        this.codeFieldContainer.isFocusSuppressed = false;
                    }
                } else if (tLRPC$TL_error.text.contains("EMAIL_TOKEN_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString(R.string.EmailTokenInvalid));
                } else if (tLRPC$TL_error.text.contains("EMAIL_VERIFY_EXPIRED")) {
                    onBackPressed(true);
                    LoginActivity.this.setPage(0, true, null, true);
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("CodeExpired", R.string.CodeExpired));
                } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("FloodWait", R.string.FloodWait));
                } else {
                    LoginActivity loginActivity = LoginActivity.this;
                    String string = LocaleController.getString(R.string.RestorePasswordNoEmailTitle);
                    loginActivity.needShowAlert(string, LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred) + "\n" + tLRPC$TL_error.text);
                }
                z = false;
                if (!z) {
                }
            }
            this.googleAccount = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$10(Bundle bundle) {
            LoginActivity.this.setPage(5, true, bundle, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$11(TLObject tLObject, Bundle bundle) {
            if ((tLObject instanceof TLRPC$TL_account_emailVerified) && LoginActivity.this.activityMode == 3) {
                LoginActivity.this.finishFragment();
                LoginActivity.this.emailChangeFinishCallback.run();
            } else if (tLObject instanceof TLRPC$TL_account_emailVerifiedLogin) {
                LoginActivity.this.lambda$resendCodeFromSafetyNet$18(bundle, ((TLRPC$TL_account_emailVerifiedLogin) tLObject).sent_code);
            } else if (tLObject instanceof TLRPC$TL_auth_authorization) {
                LoginActivity.this.onAuthSuccess((TLRPC$TL_auth_authorization) tLObject);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$14(final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$onNextPressed$13(tLRPC$TL_error, tLObject, str);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$13(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, String str) {
            this.nextPressed = false;
            LoginActivity.this.showDoneButton(false, true);
            if (tLRPC$TL_error != null) {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), tLRPC$TL_error.text);
                return;
            }
            TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$account_Password, true)) {
                AlertsCreator.showUpdateAppAlert(LoginActivity.this.getParentActivity(), LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
                return;
            }
            final Bundle bundle = new Bundle();
            SerializedData serializedData = new SerializedData(tLRPC$account_Password.getObjectSize());
            tLRPC$account_Password.serializeToStream(serializedData);
            bundle.putString("password", Utilities.bytesToHex(serializedData.toByteArray()));
            bundle.putString("phoneFormated", this.requestPhone);
            bundle.putString("phoneHash", this.phoneHash);
            bundle.putString("code", str);
            animateSuccess(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$onNextPressed$12(bundle);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$12(Bundle bundle) {
            LoginActivity.this.setPage(6, true, bundle, false);
        }

        private void animateSuccess(final Runnable runnable) {
            if (this.googleAccount != null) {
                runnable.run();
                return;
            }
            final int i = 0;
            while (true) {
                CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
                if (i < codeFieldContainer.codeField.length) {
                    codeFieldContainer.postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda11
                        @Override // java.lang.Runnable
                        public final void run() {
                            LoginActivity.LoginActivityEmailCodeView.this.lambda$animateSuccess$17(i);
                        }
                    }, i * 75);
                    i++;
                } else {
                    codeFieldContainer.postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda14
                        @Override // java.lang.Runnable
                        public final void run() {
                            LoginActivity.LoginActivityEmailCodeView.this.lambda$animateSuccess$18(runnable);
                        }
                    }, (this.codeFieldContainer.codeField.length * 75) + 400);
                    return;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$animateSuccess$17(int i) {
            this.codeFieldContainer.codeField[i].animateSuccessProgress(1.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$animateSuccess$18(Runnable runnable) {
            int i = 0;
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i < codeNumberFieldArr.length) {
                    codeNumberFieldArr[i].animateSuccessProgress(0.0f);
                    i++;
                } else {
                    runnable.run();
                    this.codeFieldContainer.isFocusSuppressed = false;
                    return;
                }
            }
        }

        private void shakeWrongCode() {
            try {
                this.codeFieldContainer.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            int i = 0;
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i >= codeNumberFieldArr.length) {
                    break;
                }
                codeNumberFieldArr[i].setText("");
                this.codeFieldContainer.codeField[i].animateErrorProgress(1.0f);
                i++;
            }
            if (this.errorViewSwitcher.getCurrentView() == this.resendFrameLayout) {
                this.errorViewSwitcher.showNext();
            }
            this.codeFieldContainer.codeField[0].requestFocus();
            AndroidUtilities.shakeViewSpring(this.codeFieldContainer, 10.0f, new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$shakeWrongCode$20();
                }
            });
            removeCallbacks(this.errorColorTimeout);
            postDelayed(this.errorColorTimeout, 5000L);
            this.postedErrorColorTimeout = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$shakeWrongCode$20() {
            postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$shakeWrongCode$19();
                }
            }, 150L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$shakeWrongCode$19() {
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            int i = 0;
            codeFieldContainer.isFocusSuppressed = false;
            codeFieldContainer.codeField[0].requestFocus();
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i >= codeNumberFieldArr.length) {
                    return;
                }
                codeNumberFieldArr[i].animateErrorProgress(0.0f);
                i++;
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onShow() {
            super.onShow();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityEmailCodeView$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityEmailCodeView.this.lambda$onShow$21();
                }
            }, LoginActivity.SHOW_DELAY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShow$21() {
            this.inboxImageView.getAnimatedDrawable().setCurrentFrame(0, false);
            this.inboxImageView.playAnimation();
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            if (codeFieldContainer != null && codeFieldContainer.codeField != null) {
                codeFieldContainer.setText("");
                this.codeFieldContainer.codeField[0].requestFocus();
            }
            AndroidUtilities.runOnUIThread(this.resendCodeTimeout, 60000L);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void saveStateParams(Bundle bundle) {
            String code = this.codeFieldContainer.getCode();
            if (code != null && code.length() != 0) {
                bundle.putString("emailcode_code", code);
            }
            Bundle bundle2 = this.currentParams;
            if (bundle2 != null) {
                bundle.putBundle("emailcode_params", bundle2);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void restoreStateParams(Bundle bundle) {
            Bundle bundle2 = bundle.getBundle("emailcode_params");
            this.currentParams = bundle2;
            if (bundle2 != null) {
                setParams(bundle2, true);
            }
            String string = bundle.getString("emailcode_code");
            if (string != null) {
                this.codeFieldContainer.setText(string);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class LoginActivityRecoverView extends SlideView {
        private CodeFieldContainer codeFieldContainer;
        private TextView confirmTextView;
        private Bundle currentParams;
        private Runnable errorColorTimeout;
        private RLottieImageView inboxImageView;
        private boolean nextPressed;
        private String passwordString;
        private String phoneCode;
        private String phoneHash;
        private boolean postedErrorColorTimeout;
        private String requestPhone;
        private TextView titleView;
        private TextView troubleButton;

        @Override // org.telegram.ui.Components.SlideView
        public boolean hasCustomKeyboard() {
            return true;
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            int i = 0;
            this.postedErrorColorTimeout = false;
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i >= codeNumberFieldArr.length) {
                    return;
                }
                codeNumberFieldArr[i].animateErrorProgress(0.0f);
                i++;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:13:0x00f5  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public LoginActivityRecoverView(Context context) {
            super(context);
            int i;
            CodeNumberField[] codeNumberFieldArr;
            this.errorColorTimeout = new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityRecoverView.this.lambda$new$0();
                }
            };
            setOrientation(1);
            FrameLayout frameLayout = new FrameLayout(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.inboxImageView = rLottieImageView;
            rLottieImageView.setAnimation(R.raw.tsv_setup_mail, 120, 120);
            this.inboxImageView.setAutoRepeat(false);
            frameLayout.addView(this.inboxImageView, LayoutHelper.createFrame(120, 120, 1));
            if (!AndroidUtilities.isSmallScreen()) {
                Point point = AndroidUtilities.displaySize;
                if (point.x <= point.y || AndroidUtilities.isTablet()) {
                    i = 0;
                    frameLayout.setVisibility(i);
                    addView(frameLayout, LayoutHelper.createFrame(-1, -2, 1));
                    TextView textView = new TextView(context);
                    this.titleView = textView;
                    textView.setTextSize(1, 18.0f);
                    this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                    this.titleView.setText(LocaleController.getString(R.string.EnterCode));
                    this.titleView.setGravity(17);
                    this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
                    TextView textView2 = new TextView(context);
                    this.confirmTextView = textView2;
                    textView2.setTextSize(1, 14.0f);
                    this.confirmTextView.setGravity(17);
                    this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.confirmTextView.setText(LocaleController.getString(R.string.RestoreEmailSentInfo));
                    addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 12, 8, 12, 0));
                    CodeFieldContainer codeFieldContainer = new CodeFieldContainer(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityRecoverView.1
                        @Override // org.telegram.ui.CodeFieldContainer
                        protected void processNextPressed() {
                            LoginActivityRecoverView.this.onNextPressed(null);
                        }
                    };
                    this.codeFieldContainer = codeFieldContainer;
                    codeFieldContainer.setNumbersCount(6, 1);
                    for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                        codeNumberField.setShowSoftInputOnFocusCompat(!hasCustomKeyboard() || LoginActivity.this.isCustomKeyboardForceDisabled());
                        codeNumberField.addTextChangedListener(new TextWatcher(LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityRecoverView.2
                            @Override // android.text.TextWatcher
                            public void afterTextChanged(Editable editable) {
                            }

                            @Override // android.text.TextWatcher
                            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                            }

                            @Override // android.text.TextWatcher
                            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
                                if (LoginActivityRecoverView.this.postedErrorColorTimeout) {
                                    LoginActivityRecoverView loginActivityRecoverView = LoginActivityRecoverView.this;
                                    loginActivityRecoverView.removeCallbacks(loginActivityRecoverView.errorColorTimeout);
                                    LoginActivityRecoverView.this.errorColorTimeout.run();
                                }
                            }
                        });
                        codeNumberField.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda3
                            @Override // android.view.View.OnFocusChangeListener
                            public final void onFocusChange(View view, boolean z) {
                                LoginActivity.LoginActivityRecoverView.this.lambda$new$1(view, z);
                            }
                        });
                    }
                    addView(this.codeFieldContainer, LayoutHelper.createLinear(-2, 42, 1, 0, 32, 0, 0));
                    SpoilersTextView spoilersTextView = new SpoilersTextView(context, false);
                    this.troubleButton = spoilersTextView;
                    spoilersTextView.setGravity(17);
                    this.troubleButton.setTextSize(1, 14.0f);
                    this.troubleButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.troubleButton.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    this.troubleButton.setMaxLines(2);
                    this.troubleButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LoginActivity.LoginActivityRecoverView.this.lambda$new$4(view);
                        }
                    });
                    FrameLayout frameLayout2 = new FrameLayout(context);
                    frameLayout2.addView(this.troubleButton, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 32.0f));
                    addView(frameLayout2, LayoutHelper.createLinear(-1, 0, 1.0f));
                    VerticalPositionAutoAnimator.attach(this.troubleButton);
                }
            }
            i = 8;
            frameLayout.setVisibility(i);
            addView(frameLayout, LayoutHelper.createFrame(-1, -2, 1));
            TextView textView3 = new TextView(context);
            this.titleView = textView3;
            textView3.setTextSize(1, 18.0f);
            this.titleView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.titleView.setText(LocaleController.getString(R.string.EnterCode));
            this.titleView.setGravity(17);
            this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
            TextView textView22 = new TextView(context);
            this.confirmTextView = textView22;
            textView22.setTextSize(1, 14.0f);
            this.confirmTextView.setGravity(17);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.confirmTextView.setText(LocaleController.getString(R.string.RestoreEmailSentInfo));
            addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 12, 8, 12, 0));
            CodeFieldContainer codeFieldContainer2 = new CodeFieldContainer(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityRecoverView.1
                @Override // org.telegram.ui.CodeFieldContainer
                protected void processNextPressed() {
                    LoginActivityRecoverView.this.onNextPressed(null);
                }
            };
            this.codeFieldContainer = codeFieldContainer2;
            codeFieldContainer2.setNumbersCount(6, 1);
            while (r12 < r11) {
            }
            addView(this.codeFieldContainer, LayoutHelper.createLinear(-2, 42, 1, 0, 32, 0, 0));
            SpoilersTextView spoilersTextView2 = new SpoilersTextView(context, false);
            this.troubleButton = spoilersTextView2;
            spoilersTextView2.setGravity(17);
            this.troubleButton.setTextSize(1, 14.0f);
            this.troubleButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.troubleButton.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.troubleButton.setMaxLines(2);
            this.troubleButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LoginActivity.LoginActivityRecoverView.this.lambda$new$4(view);
                }
            });
            FrameLayout frameLayout22 = new FrameLayout(context);
            frameLayout22.addView(this.troubleButton, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 32.0f));
            addView(frameLayout22, LayoutHelper.createLinear(-1, 0, 1.0f));
            VerticalPositionAutoAnimator.attach(this.troubleButton);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(View view, boolean z) {
            if (z) {
                LoginActivity.this.keyboardView.setEditText((EditText) view);
                LoginActivity.this.keyboardView.setDispatchBackWhenEmpty(true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$4(View view) {
            Dialog showDialog = LoginActivity.this.showDialog(new AlertDialog.Builder(LoginActivity.this.getParentActivity()).setTitle(LocaleController.getString("RestorePasswordNoEmailTitle", R.string.RestorePasswordNoEmailTitle)).setMessage(LocaleController.getString("RestoreEmailTroubleText", R.string.RestoreEmailTroubleText)).setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    LoginActivity.LoginActivityRecoverView.this.lambda$new$2(dialogInterface, i);
                }
            }).setNegativeButton(LocaleController.getString(R.string.ResetAccount), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    LoginActivity.LoginActivityRecoverView.this.lambda$new$3(dialogInterface, i);
                }
            }).create());
            if (showDialog != null) {
                showDialog.setCanceledOnTouchOutside(false);
                showDialog.setCancelable(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(DialogInterface dialogInterface, int i) {
            LoginActivity.this.setPage(6, true, new Bundle(), true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(DialogInterface dialogInterface, int i) {
            LoginActivity.this.tryResetAccount(this.requestPhone, this.phoneHash, this.phoneCode);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void updateColors() {
            this.titleView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.confirmTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.troubleButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.codeFieldContainer.invalidate();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            removeCallbacks(this.errorColorTimeout);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onCancelPressed() {
            this.nextPressed = false;
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            return LocaleController.getString("LoginPassword", R.string.LoginPassword);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void setParams(Bundle bundle, boolean z) {
            if (bundle == null) {
                return;
            }
            this.codeFieldContainer.setText("");
            this.currentParams = bundle;
            this.passwordString = bundle.getString("password");
            this.requestPhone = this.currentParams.getString("requestPhone");
            this.phoneHash = this.currentParams.getString("phoneHash");
            this.phoneCode = this.currentParams.getString("phoneCode");
            String string = this.currentParams.getString("email_unconfirmed_pattern");
            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(string);
            int indexOf = string.indexOf(42);
            int lastIndexOf = string.lastIndexOf(42);
            if (indexOf != lastIndexOf && indexOf != -1 && lastIndexOf != -1) {
                TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                textStyleRun.flags |= LiteMode.FLAG_CHAT_BLUR;
                textStyleRun.start = indexOf;
                int i = lastIndexOf + 1;
                textStyleRun.end = i;
                valueOf.setSpan(new TextStyleSpan(textStyleRun), indexOf, i, 0);
            }
            this.troubleButton.setText(AndroidUtilities.formatSpannable(LocaleController.getString(R.string.RestoreEmailNoAccess), valueOf));
            LoginActivity.this.showKeyboard(this.codeFieldContainer);
            this.codeFieldContainer.requestFocus();
        }

        private void onPasscodeError(boolean z) {
            if (LoginActivity.this.getParentActivity() == null) {
                return;
            }
            try {
                this.codeFieldContainer.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            if (z) {
                for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                    codeNumberField.setText("");
                }
            }
            for (CodeNumberField codeNumberField2 : this.codeFieldContainer.codeField) {
                codeNumberField2.animateErrorProgress(1.0f);
            }
            this.codeFieldContainer.codeField[0].requestFocus();
            AndroidUtilities.shakeViewSpring(this.codeFieldContainer, new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityRecoverView.this.lambda$onPasscodeError$6();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPasscodeError$6() {
            postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityRecoverView.this.lambda$onPasscodeError$5();
                }
            }, 150L);
            removeCallbacks(this.errorColorTimeout);
            postDelayed(this.errorColorTimeout, 3000L);
            this.postedErrorColorTimeout = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onPasscodeError$5() {
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            int i = 0;
            codeFieldContainer.isFocusSuppressed = false;
            codeFieldContainer.codeField[0].requestFocus();
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i >= codeNumberFieldArr.length) {
                    return;
                }
                codeNumberFieldArr[i].animateErrorProgress(0.0f);
                i++;
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onNextPressed(String str) {
            if (this.nextPressed) {
                return;
            }
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            codeFieldContainer.isFocusSuppressed = true;
            for (CodeNumberField codeNumberField : codeFieldContainer.codeField) {
                codeNumberField.animateFocusedProgress(0.0f);
            }
            final String code = this.codeFieldContainer.getCode();
            if (code.length() == 0) {
                onPasscodeError(false);
                return;
            }
            this.nextPressed = true;
            LoginActivity.this.needShowProgress(0);
            TLRPC$TL_auth_checkRecoveryPassword tLRPC$TL_auth_checkRecoveryPassword = new TLRPC$TL_auth_checkRecoveryPassword();
            tLRPC$TL_auth_checkRecoveryPassword.code = code;
            ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_checkRecoveryPassword, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda9
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LoginActivity.LoginActivityRecoverView.this.lambda$onNextPressed$8(code, tLObject, tLRPC$TL_error);
                }
            }, 10);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$8(final String str, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityRecoverView.this.lambda$onNextPressed$7(tLObject, str, tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$7(TLObject tLObject, String str, TLRPC$TL_error tLRPC$TL_error) {
            String formatPluralString;
            LoginActivity.this.needHideProgress(false);
            this.nextPressed = false;
            if (tLObject instanceof TLRPC$TL_boolTrue) {
                Bundle bundle = new Bundle();
                bundle.putString("emailCode", str);
                bundle.putString("password", this.passwordString);
                LoginActivity.this.setPage(9, true, bundle, false);
            } else if (tLRPC$TL_error == null || tLRPC$TL_error.text.startsWith("CODE_INVALID")) {
                onPasscodeError(true);
            } else if (!tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), tLRPC$TL_error.text);
            } else {
                int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                if (intValue < 60) {
                    formatPluralString = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                } else {
                    formatPluralString = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                }
                LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString));
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean onBackPressed(boolean z) {
            LoginActivity.this.needHideProgress(true);
            this.currentParams = null;
            this.nextPressed = false;
            return true;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onShow() {
            super.onShow();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityRecoverView.this.lambda$onShow$9();
                }
            }, LoginActivity.SHOW_DELAY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShow$9() {
            this.inboxImageView.getAnimatedDrawable().setCurrentFrame(0, false);
            this.inboxImageView.playAnimation();
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            if (codeFieldContainer != null) {
                codeFieldContainer.codeField[0].requestFocus();
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void saveStateParams(Bundle bundle) {
            String code = this.codeFieldContainer.getCode();
            if (code != null && code.length() != 0) {
                bundle.putString("recoveryview_code", code);
            }
            Bundle bundle2 = this.currentParams;
            if (bundle2 != null) {
                bundle.putBundle("recoveryview_params", bundle2);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void restoreStateParams(Bundle bundle) {
            Bundle bundle2 = bundle.getBundle("recoveryview_params");
            this.currentParams = bundle2;
            if (bundle2 != null) {
                setParams(bundle2, true);
            }
            String string = bundle.getString("recoveryview_code");
            if (string != null) {
                this.codeFieldContainer.setText(string);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class LoginActivityNewPasswordView extends SlideView {
        private TextView cancelButton;
        private EditTextBoldCursor[] codeField;
        private TextView confirmTextView;
        private Bundle currentParams;
        private TLRPC$account_Password currentPassword;
        private int currentStage;
        private String emailCode;
        private boolean isPasswordVisible;
        private String newPassword;
        private boolean nextPressed;
        private OutlineTextContainerView[] outlineFields;
        private ImageView passwordButton;
        private String passwordString;
        private TextView titleTextView;

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        public LoginActivityNewPasswordView(Context context, int i) {
            super(context);
            int i2;
            this.currentStage = i;
            setOrientation(1);
            EditTextBoldCursor[] editTextBoldCursorArr = new EditTextBoldCursor[i == 1 ? 1 : 2];
            this.codeField = editTextBoldCursorArr;
            this.outlineFields = new OutlineTextContainerView[editTextBoldCursorArr.length];
            TextView textView = new TextView(context);
            this.titleTextView = textView;
            float f = 18.0f;
            textView.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.titleTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.titleTextView.setGravity(49);
            this.titleTextView.setText(LocaleController.getString(R.string.SetNewPassword));
            addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 8, AndroidUtilities.isSmallScreen() ? 16 : 72, 8, 0));
            TextView textView2 = new TextView(context);
            this.confirmTextView = textView2;
            textView2.setTextSize(1, 16.0f);
            this.confirmTextView.setGravity(1);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 8, 6, 8, 16));
            final int i3 = 0;
            while (i3 < this.codeField.length) {
                final OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
                this.outlineFields[i3] = outlineTextContainerView;
                if (i == 0) {
                    i2 = i3 == 0 ? R.string.PleaseEnterNewFirstPasswordHint : R.string.PleaseEnterNewSecondPasswordHint;
                } else {
                    i2 = R.string.PasswordHintPlaceholder;
                }
                outlineTextContainerView.setText(LocaleController.getString(i2));
                this.codeField[i3] = new EditTextBoldCursor(context);
                this.codeField[i3].setCursorSize(AndroidUtilities.dp(20.0f));
                this.codeField[i3].setCursorWidth(1.5f);
                this.codeField[i3].setImeOptions(268435461);
                this.codeField[i3].setTextSize(1, f);
                this.codeField[i3].setMaxLines(1);
                this.codeField[i3].setBackground(null);
                int dp = AndroidUtilities.dp(16.0f);
                this.codeField[i3].setPadding(dp, dp, dp, dp);
                if (i == 0) {
                    this.codeField[i3].setInputType(129);
                    this.codeField[i3].setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                this.codeField[i3].setTypeface(Typeface.DEFAULT);
                this.codeField[i3].setGravity(LocaleController.isRTL ? 5 : 3);
                EditTextBoldCursor editTextBoldCursor = this.codeField[i3];
                boolean z = i3 == 0 && i == 0;
                editTextBoldCursor.addTextChangedListener(new TextWatcher(LoginActivity.this, z) { // from class: org.telegram.ui.LoginActivity.LoginActivityNewPasswordView.1
                    final /* synthetic */ boolean val$showPasswordButton;

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i4, int i5, int i6) {
                    }

                    {
                        this.val$showPasswordButton = z;
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        if (this.val$showPasswordButton) {
                            if (LoginActivityNewPasswordView.this.passwordButton.getVisibility() == 0 || TextUtils.isEmpty(editable)) {
                                if (LoginActivityNewPasswordView.this.passwordButton.getVisibility() == 8 || !TextUtils.isEmpty(editable)) {
                                    return;
                                }
                                AndroidUtilities.updateViewVisibilityAnimated(LoginActivityNewPasswordView.this.passwordButton, false, 0.1f, true);
                                return;
                            }
                            if (LoginActivityNewPasswordView.this.isPasswordVisible) {
                                LoginActivityNewPasswordView.this.passwordButton.callOnClick();
                            }
                            AndroidUtilities.updateViewVisibilityAnimated(LoginActivityNewPasswordView.this.passwordButton, true, 0.1f, true);
                        }
                    }
                });
                this.codeField[i3].setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda3
                    @Override // android.view.View.OnFocusChangeListener
                    public final void onFocusChange(View view, boolean z2) {
                        LoginActivity.LoginActivityNewPasswordView.lambda$new$0(OutlineTextContainerView.this, view, z2);
                    }
                });
                if (z) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(0);
                    linearLayout.setGravity(16);
                    linearLayout.addView(this.codeField[i3], LayoutHelper.createLinear(0, -2, 1.0f));
                    ImageView imageView = new ImageView(context);
                    this.passwordButton = imageView;
                    imageView.setImageResource(R.drawable.msg_message);
                    AndroidUtilities.updateViewVisibilityAnimated(this.passwordButton, true, 0.1f, false);
                    this.passwordButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            LoginActivity.LoginActivityNewPasswordView.this.lambda$new$1(view);
                        }
                    });
                    linearLayout.addView(this.passwordButton, LayoutHelper.createLinearRelatively(24.0f, 24.0f, 0, 0.0f, 0.0f, 14.0f, 0.0f));
                    outlineTextContainerView.addView(linearLayout, LayoutHelper.createFrame(-1, -2.0f));
                } else {
                    outlineTextContainerView.addView(this.codeField[i3], LayoutHelper.createFrame(-1, -2.0f));
                }
                outlineTextContainerView.attachEditText(this.codeField[i3]);
                addView(outlineTextContainerView, LayoutHelper.createLinear(-1, -2, 1, 16, 16, 16, 0));
                this.codeField[i3].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda4
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView3, int i4, KeyEvent keyEvent) {
                        boolean lambda$new$2;
                        lambda$new$2 = LoginActivity.LoginActivityNewPasswordView.this.lambda$new$2(i3, textView3, i4, keyEvent);
                        return lambda$new$2;
                    }
                });
                i3++;
                f = 18.0f;
            }
            if (i == 0) {
                this.confirmTextView.setText(LocaleController.getString("PleaseEnterNewFirstPasswordLogin", R.string.PleaseEnterNewFirstPasswordLogin));
            } else {
                this.confirmTextView.setText(LocaleController.getString("PasswordHintTextLogin", R.string.PasswordHintTextLogin));
            }
            TextView textView3 = new TextView(context);
            this.cancelButton = textView3;
            textView3.setGravity(19);
            this.cancelButton.setTextSize(1, 15.0f);
            this.cancelButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.cancelButton.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
            this.cancelButton.setText(LocaleController.getString(R.string.YourEmailSkip));
            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.addView(this.cancelButton, LayoutHelper.createFrame(-1, Build.VERSION.SDK_INT >= 21 ? 56 : 60, 80, 0.0f, 0.0f, 0.0f, 32.0f));
            addView(frameLayout, LayoutHelper.createLinear(-1, -1, 80));
            VerticalPositionAutoAnimator.attach(this.cancelButton);
            this.cancelButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    LoginActivity.LoginActivityNewPasswordView.this.lambda$new$3(view);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$new$0(OutlineTextContainerView outlineTextContainerView, View view, boolean z) {
            outlineTextContainerView.animateSelection(z ? 1.0f : 0.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(View view) {
            this.isPasswordVisible = !this.isPasswordVisible;
            int i = 0;
            while (true) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
                if (i >= editTextBoldCursorArr.length) {
                    break;
                }
                int selectionStart = editTextBoldCursorArr[i].getSelectionStart();
                int selectionEnd = this.codeField[i].getSelectionEnd();
                this.codeField[i].setInputType((this.isPasswordVisible ? 144 : 128) | 1);
                this.codeField[i].setSelection(selectionStart, selectionEnd);
                i++;
            }
            this.passwordButton.setTag(Boolean.valueOf(this.isPasswordVisible));
            this.passwordButton.setColorFilter(Theme.getColor(this.isPasswordVisible ? "windowBackgroundWhiteInputFieldActivated" : "windowBackgroundWhiteHintText"));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$2(int i, TextView textView, int i2, KeyEvent keyEvent) {
            if (i == 0) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
                if (editTextBoldCursorArr.length == 2) {
                    editTextBoldCursorArr[1].requestFocus();
                    return true;
                }
            }
            if (i2 == 5) {
                onNextPressed(null);
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(View view) {
            if (this.currentStage == 0) {
                recoverPassword(null, null);
            } else {
                recoverPassword(this.newPassword, null);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void updateColors() {
            this.titleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.confirmTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
            int length = editTextBoldCursorArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                EditTextBoldCursor editTextBoldCursor = editTextBoldCursorArr[i];
                editTextBoldCursor.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                editTextBoldCursor.setCursorColor(Theme.getColor("windowBackgroundWhiteInputFieldActivated"));
                i++;
            }
            for (OutlineTextContainerView outlineTextContainerView : this.outlineFields) {
                outlineTextContainerView.updateColor();
            }
            this.cancelButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            ImageView imageView = this.passwordButton;
            if (imageView != null) {
                imageView.setColorFilter(Theme.getColor(this.isPasswordVisible ? "windowBackgroundWhiteInputFieldActivated" : "windowBackgroundWhiteHintText"));
                this.passwordButton.setBackground(Theme.createSelectorDrawable(LoginActivity.this.getThemedColor("listSelectorSDK21"), 1));
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onCancelPressed() {
            this.nextPressed = false;
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            return LocaleController.getString("NewPassword", R.string.NewPassword);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void setParams(Bundle bundle, boolean z) {
            if (bundle == null) {
                return;
            }
            int i = 0;
            while (true) {
                EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
                if (i >= editTextBoldCursorArr.length) {
                    break;
                }
                editTextBoldCursorArr[i].setText("");
                i++;
            }
            this.currentParams = bundle;
            this.emailCode = bundle.getString("emailCode");
            String string = this.currentParams.getString("password");
            this.passwordString = string;
            if (string != null) {
                SerializedData serializedData = new SerializedData(Utilities.hexToBytes(string));
                TLRPC$account_Password TLdeserialize = TLRPC$account_Password.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                this.currentPassword = TLdeserialize;
                TwoStepVerificationActivity.initPasswordNewAlgo(TLdeserialize);
            }
            this.newPassword = this.currentParams.getString("new_password");
            LoginActivity.this.showKeyboard(this.codeField[0]);
            this.codeField[0].requestFocus();
        }

        private void onPasscodeError(boolean z, int i) {
            if (LoginActivity.this.getParentActivity() == null) {
                return;
            }
            try {
                this.codeField[i].performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            AndroidUtilities.shakeView(this.codeField[i]);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onNextPressed(String str) {
            if (this.nextPressed) {
                return;
            }
            String obj = this.codeField[0].getText().toString();
            if (obj.length() == 0) {
                onPasscodeError(false, 0);
            } else if (this.currentStage == 0) {
                if (!obj.equals(this.codeField[1].getText().toString())) {
                    onPasscodeError(false, 1);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("emailCode", this.emailCode);
                bundle.putString("new_password", obj);
                bundle.putString("password", this.passwordString);
                LoginActivity.this.setPage(10, true, bundle, false);
            } else {
                this.nextPressed = true;
                LoginActivity.this.needShowProgress(0);
                recoverPassword(this.newPassword, obj);
            }
        }

        private void recoverPassword(final String str, final String str2) {
            final TLRPC$TL_auth_recoverPassword tLRPC$TL_auth_recoverPassword = new TLRPC$TL_auth_recoverPassword();
            tLRPC$TL_auth_recoverPassword.code = this.emailCode;
            if (!TextUtils.isEmpty(str)) {
                tLRPC$TL_auth_recoverPassword.flags |= 1;
                TLRPC$TL_account_passwordInputSettings tLRPC$TL_account_passwordInputSettings = new TLRPC$TL_account_passwordInputSettings();
                tLRPC$TL_auth_recoverPassword.new_settings = tLRPC$TL_account_passwordInputSettings;
                tLRPC$TL_account_passwordInputSettings.flags |= 1;
                tLRPC$TL_account_passwordInputSettings.hint = str2 != null ? str2 : "";
                tLRPC$TL_account_passwordInputSettings.new_algo = this.currentPassword.new_algo;
            }
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityNewPasswordView.this.lambda$recoverPassword$9(str, str2, tLRPC$TL_auth_recoverPassword);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$recoverPassword$9(final String str, final String str2, TLRPC$TL_auth_recoverPassword tLRPC$TL_auth_recoverPassword) {
            byte[] stringBytes = str != null ? AndroidUtilities.getStringBytes(str) : null;
            RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda9
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    LoginActivity.LoginActivityNewPasswordView.this.lambda$recoverPassword$8(str, str2, tLObject, tLRPC$TL_error);
                }
            };
            TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = this.currentPassword.new_algo;
            if (tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
                if (str != null) {
                    tLRPC$TL_auth_recoverPassword.new_settings.new_password_hash = SRPHelper.getVBytes(stringBytes, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo);
                    if (tLRPC$TL_auth_recoverPassword.new_settings.new_password_hash == null) {
                        TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
                        tLRPC$TL_error.text = "ALGO_INVALID";
                        requestDelegate.run(null, tLRPC$TL_error);
                    }
                }
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_recoverPassword, requestDelegate, 10);
                return;
            }
            TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
            tLRPC$TL_error2.text = "PASSWORD_HASH_INVALID";
            requestDelegate.run(null, tLRPC$TL_error2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$recoverPassword$8(final String str, final String str2, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityNewPasswordView.this.lambda$recoverPassword$7(tLRPC$TL_error, str, str2, tLObject);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$recoverPassword$7(TLRPC$TL_error tLRPC$TL_error, final String str, final String str2, final TLObject tLObject) {
            String formatPluralString;
            if (tLRPC$TL_error == null || (!"SRP_ID_INVALID".equals(tLRPC$TL_error.text) && !"NEW_SALT_INVALID".equals(tLRPC$TL_error.text))) {
                LoginActivity.this.needHideProgress(false);
                if (tLObject instanceof TLRPC$auth_Authorization) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                    builder.setPositiveButton(LocaleController.getString(R.string.Continue), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            LoginActivity.LoginActivityNewPasswordView.this.lambda$recoverPassword$6(tLObject, dialogInterface, i);
                        }
                    });
                    if (TextUtils.isEmpty(str)) {
                        builder.setMessage(LocaleController.getString(R.string.YourPasswordReset));
                    } else {
                        builder.setMessage(LocaleController.getString(R.string.YourPasswordChangedSuccessText));
                    }
                    builder.setTitle(LocaleController.getString(R.string.TwoStepVerificationTitle));
                    Dialog showDialog = LoginActivity.this.showDialog(builder.create());
                    if (showDialog != null) {
                        showDialog.setCanceledOnTouchOutside(false);
                        showDialog.setCancelable(false);
                        return;
                    }
                    return;
                } else if (tLRPC$TL_error != null) {
                    this.nextPressed = false;
                    if (!tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), tLRPC$TL_error.text);
                        return;
                    }
                    int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                    if (intValue < 60) {
                        formatPluralString = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                    } else {
                        formatPluralString = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                    }
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString));
                    return;
                } else {
                    return;
                }
            }
            ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda10
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                    LoginActivity.LoginActivityNewPasswordView.this.lambda$recoverPassword$5(str, str2, tLObject2, tLRPC$TL_error2);
                }
            }, 8);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$recoverPassword$5(final String str, final String str2, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityNewPasswordView.this.lambda$recoverPassword$4(tLRPC$TL_error, tLObject, str, str2);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$recoverPassword$4(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, String str, String str2) {
            if (tLRPC$TL_error == null) {
                TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
                this.currentPassword = tLRPC$account_Password;
                TwoStepVerificationActivity.initPasswordNewAlgo(tLRPC$account_Password);
                recoverPassword(str, str2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$recoverPassword$6(TLObject tLObject, DialogInterface dialogInterface, int i) {
            LoginActivity.this.onAuthSuccess((TLRPC$TL_auth_authorization) tLObject);
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean onBackPressed(boolean z) {
            LoginActivity.this.needHideProgress(true);
            this.currentParams = null;
            this.nextPressed = false;
            return true;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onShow() {
            super.onShow();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityNewPasswordView.this.lambda$onShow$10();
                }
            }, LoginActivity.SHOW_DELAY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShow$10() {
            EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
            if (editTextBoldCursorArr != null) {
                editTextBoldCursorArr[0].requestFocus();
                EditTextBoldCursor[] editTextBoldCursorArr2 = this.codeField;
                editTextBoldCursorArr2[0].setSelection(editTextBoldCursorArr2[0].length());
                AndroidUtilities.showKeyboard(this.codeField[0]);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void saveStateParams(Bundle bundle) {
            if (this.currentParams != null) {
                bundle.putBundle("recoveryview_params" + this.currentStage, this.currentParams);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void restoreStateParams(Bundle bundle) {
            Bundle bundle2 = bundle.getBundle("recoveryview_params" + this.currentStage);
            this.currentParams = bundle2;
            if (bundle2 != null) {
                setParams(bundle2, true);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class LoginActivityRegisterView extends SlideView implements ImageUpdater.ImageUpdaterDelegate {
        private TLRPC$FileLocation avatar;
        private AnimatorSet avatarAnimation;
        private TLRPC$FileLocation avatarBig;
        private AvatarDrawable avatarDrawable;
        private RLottieImageView avatarEditor;
        private BackupImageView avatarImage;
        private View avatarOverlay;
        private RadialProgressView avatarProgressView;
        private RLottieDrawable cameraDrawable;
        private RLottieDrawable cameraWaitDrawable;
        private Bundle currentParams;
        private TextView descriptionTextView;
        private FrameLayout editTextContainer;
        private EditTextBoldCursor firstNameField;
        private OutlineTextContainerView firstNameOutlineView;
        private ImageUpdater imageUpdater;
        private boolean isCameraWaitAnimationAllowed;
        private EditTextBoldCursor lastNameField;
        private OutlineTextContainerView lastNameOutlineView;
        private boolean nextPressed;
        private String phoneHash;
        private TextView privacyView;
        private String requestPhone;
        private TextView titleTextView;
        private TextView wrongNumber;

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public /* synthetic */ boolean canFinishFragment() {
            return ImageUpdater.ImageUpdaterDelegate.-CC.$default$canFinishFragment(this);
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public /* synthetic */ void didStartUpload(boolean z) {
            ImageUpdater.ImageUpdaterDelegate.-CC.$default$didStartUpload(this, z);
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public /* synthetic */ void didUploadFailed() {
            ImageUpdater.ImageUpdaterDelegate.-CC.$default$didUploadFailed(this);
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public /* bridge */ /* synthetic */ String getInitialSearchString() {
            return ImageUpdater.ImageUpdaterDelegate.-CC.$default$getInitialSearchString(this);
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public /* synthetic */ void onUploadProgressChanged(float f) {
            ImageUpdater.ImageUpdaterDelegate.-CC.$default$onUploadProgressChanged(this, f);
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
                LoginActivityRegisterView.this.showTermsOfService(false);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void showTermsOfService(boolean z) {
            if (LoginActivity.this.currentTermsOfService == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString("TermsOfService", R.string.TermsOfService));
            if (z) {
                builder.setPositiveButton(LocaleController.getString("Accept", R.string.Accept), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        LoginActivity.LoginActivityRegisterView.this.lambda$showTermsOfService$0(dialogInterface, i);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Decline", R.string.Decline), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda3
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        LoginActivity.LoginActivityRegisterView.this.lambda$showTermsOfService$3(dialogInterface, i);
                    }
                });
            } else {
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LoginActivity.this.currentTermsOfService.text);
            MessageObject.addEntitiesToText(spannableStringBuilder, LoginActivity.this.currentTermsOfService.entities, false, false, false, false);
            builder.setMessage(spannableStringBuilder);
            LoginActivity.this.showDialog(builder.create());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showTermsOfService$0(DialogInterface dialogInterface, int i) {
            LoginActivity.this.currentTermsOfService.popup = false;
            onNextPressed(null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showTermsOfService$3(DialogInterface dialogInterface, int i) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString("TermsOfService", R.string.TermsOfService));
            builder.setMessage(LocaleController.getString("TosDecline", R.string.TosDecline));
            builder.setPositiveButton(LocaleController.getString("SignUp", R.string.SignUp), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface2, int i2) {
                    LoginActivity.LoginActivityRegisterView.this.lambda$showTermsOfService$1(dialogInterface2, i2);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Decline", R.string.Decline), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface2, int i2) {
                    LoginActivity.LoginActivityRegisterView.this.lambda$showTermsOfService$2(dialogInterface2, i2);
                }
            });
            LoginActivity.this.showDialog(builder.create());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showTermsOfService$1(DialogInterface dialogInterface, int i) {
            LoginActivity.this.currentTermsOfService.popup = false;
            onNextPressed(null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showTermsOfService$2(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            LoginActivity.this.setPage(0, true, null, true);
        }

        public LoginActivityRegisterView(Context context) {
            super(context);
            this.nextPressed = false;
            this.isCameraWaitAnimationAllowed = true;
            setOrientation(1);
            ImageUpdater imageUpdater = new ImageUpdater(false, 0, false);
            this.imageUpdater = imageUpdater;
            imageUpdater.setOpenWithFrontfaceCamera(true);
            this.imageUpdater.setSearchAvailable(false);
            this.imageUpdater.setUploadAfterSelect(false);
            ImageUpdater imageUpdater2 = this.imageUpdater;
            imageUpdater2.parentFragment = LoginActivity.this;
            imageUpdater2.setDelegate(this);
            FrameLayout frameLayout = new FrameLayout(context);
            addView(frameLayout, LayoutHelper.createLinear(78, 78, 1));
            this.avatarDrawable = new AvatarDrawable();
            BackupImageView backupImageView = new BackupImageView(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityRegisterView.1
                @Override // android.view.View
                public void invalidate() {
                    if (LoginActivityRegisterView.this.avatarOverlay != null) {
                        LoginActivityRegisterView.this.avatarOverlay.invalidate();
                    }
                    super.invalidate();
                }

                @Override // android.view.View
                public void invalidate(int i, int i2, int i3, int i4) {
                    if (LoginActivityRegisterView.this.avatarOverlay != null) {
                        LoginActivityRegisterView.this.avatarOverlay.invalidate();
                    }
                    super.invalidate(i, i2, i3, i4);
                }
            };
            this.avatarImage = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(64.0f));
            this.avatarDrawable.setAvatarType(13);
            this.avatarDrawable.setInfo(5L, null, null);
            this.avatarImage.setImageDrawable(this.avatarDrawable);
            frameLayout.addView(this.avatarImage, LayoutHelper.createFrame(-1, -1.0f));
            Paint paint = new Paint(1);
            paint.setColor(1426063360);
            View view = new View(context, LoginActivity.this, paint) { // from class: org.telegram.ui.LoginActivity.LoginActivityRegisterView.2
                final /* synthetic */ Paint val$paint;

                {
                    this.val$paint = paint;
                }

                @Override // android.view.View
                protected void onDraw(Canvas canvas) {
                    if (LoginActivityRegisterView.this.avatarImage == null || LoginActivityRegisterView.this.avatarProgressView.getVisibility() != 0) {
                        return;
                    }
                    this.val$paint.setAlpha((int) (LoginActivityRegisterView.this.avatarImage.getImageReceiver().getCurrentAlpha() * 85.0f * LoginActivityRegisterView.this.avatarProgressView.getAlpha()));
                    canvas.drawCircle(getMeasuredWidth() / 2.0f, getMeasuredHeight() / 2.0f, getMeasuredWidth() / 2.0f, this.val$paint);
                }
            };
            this.avatarOverlay = view;
            frameLayout.addView(view, LayoutHelper.createFrame(-1, -1.0f));
            this.avatarOverlay.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    LoginActivity.LoginActivityRegisterView.this.lambda$new$7(view2);
                }
            });
            int i = R.raw.camera;
            this.cameraDrawable = new RLottieDrawable(i, String.valueOf(i), AndroidUtilities.dp(70.0f), AndroidUtilities.dp(70.0f), false, null);
            int i2 = R.raw.camera_wait;
            this.cameraWaitDrawable = new RLottieDrawable(i2, String.valueOf(i2), AndroidUtilities.dp(70.0f), AndroidUtilities.dp(70.0f), false, null);
            RLottieImageView rLottieImageView = new RLottieImageView(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityRegisterView.3
                @Override // android.view.View
                public void invalidate(int i3, int i4, int i5, int i6) {
                    super.invalidate(i3, i4, i5, i6);
                    LoginActivityRegisterView.this.avatarOverlay.invalidate();
                }

                @Override // android.view.View
                public void invalidate() {
                    super.invalidate();
                    LoginActivityRegisterView.this.avatarOverlay.invalidate();
                }
            };
            this.avatarEditor = rLottieImageView;
            rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
            this.avatarEditor.setAnimation(this.cameraDrawable);
            this.avatarEditor.setEnabled(false);
            this.avatarEditor.setClickable(false);
            frameLayout.addView(this.avatarEditor, LayoutHelper.createFrame(-1, -1.0f));
            this.avatarEditor.addOnAttachStateChangeListener(new 4(LoginActivity.this));
            RadialProgressView radialProgressView = new RadialProgressView(context, LoginActivity.this) { // from class: org.telegram.ui.LoginActivity.LoginActivityRegisterView.5
                @Override // org.telegram.ui.Components.RadialProgressView, android.view.View
                public void setAlpha(float f) {
                    super.setAlpha(f);
                    LoginActivityRegisterView.this.avatarOverlay.invalidate();
                }
            };
            this.avatarProgressView = radialProgressView;
            radialProgressView.setSize(AndroidUtilities.dp(30.0f));
            this.avatarProgressView.setProgressColor(-1);
            frameLayout.addView(this.avatarProgressView, LayoutHelper.createFrame(-1, -1.0f));
            showAvatarProgress(false, false);
            TextView textView = new TextView(context);
            this.titleTextView = textView;
            textView.setText(LocaleController.getString(R.string.RegistrationProfileInfo));
            this.titleTextView.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.titleTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.titleTextView.setGravity(1);
            addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 8, 12, 8, 0));
            TextView textView2 = new TextView(context);
            this.descriptionTextView = textView2;
            textView2.setText(LocaleController.getString("RegisterText2", R.string.RegisterText2));
            this.descriptionTextView.setGravity(1);
            this.descriptionTextView.setTextSize(1, 14.0f);
            this.descriptionTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.descriptionTextView, LayoutHelper.createLinear(-2, -2, 1, 8, 6, 8, 0));
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.editTextContainer = frameLayout2;
            addView(frameLayout2, LayoutHelper.createLinear(-1, -2, 8.0f, 21.0f, 8.0f, 0.0f));
            OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
            this.firstNameOutlineView = outlineTextContainerView;
            outlineTextContainerView.setText(LocaleController.getString(R.string.FirstName));
            EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
            this.firstNameField = editTextBoldCursor;
            editTextBoldCursor.setCursorSize(AndroidUtilities.dp(20.0f));
            this.firstNameField.setCursorWidth(1.5f);
            this.firstNameField.setImeOptions(268435461);
            this.firstNameField.setTextSize(1, 17.0f);
            this.firstNameField.setMaxLines(1);
            this.firstNameField.setInputType(8192);
            this.firstNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda9
                @Override // android.view.View.OnFocusChangeListener
                public final void onFocusChange(View view2, boolean z) {
                    LoginActivity.LoginActivityRegisterView.this.lambda$new$8(view2, z);
                }
            });
            this.firstNameField.setBackground(null);
            this.firstNameField.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.firstNameOutlineView.attachEditText(this.firstNameField);
            this.firstNameOutlineView.addView(this.firstNameField, LayoutHelper.createFrame(-1, -2, 48));
            this.firstNameField.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda10
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView3, int i3, KeyEvent keyEvent) {
                    boolean lambda$new$9;
                    lambda$new$9 = LoginActivity.LoginActivityRegisterView.this.lambda$new$9(textView3, i3, keyEvent);
                    return lambda$new$9;
                }
            });
            OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
            this.lastNameOutlineView = outlineTextContainerView2;
            outlineTextContainerView2.setText(LocaleController.getString(R.string.LastName));
            EditTextBoldCursor editTextBoldCursor2 = new EditTextBoldCursor(context);
            this.lastNameField = editTextBoldCursor2;
            editTextBoldCursor2.setCursorSize(AndroidUtilities.dp(20.0f));
            this.lastNameField.setCursorWidth(1.5f);
            this.lastNameField.setImeOptions(268435462);
            this.lastNameField.setTextSize(1, 17.0f);
            this.lastNameField.setMaxLines(1);
            this.lastNameField.setInputType(8192);
            this.lastNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda8
                @Override // android.view.View.OnFocusChangeListener
                public final void onFocusChange(View view2, boolean z) {
                    LoginActivity.LoginActivityRegisterView.this.lambda$new$10(view2, z);
                }
            });
            this.lastNameField.setBackground(null);
            this.lastNameField.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.lastNameOutlineView.attachEditText(this.lastNameField);
            this.lastNameOutlineView.addView(this.lastNameField, LayoutHelper.createFrame(-1, -2, 48));
            this.lastNameField.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda11
                @Override // android.widget.TextView.OnEditorActionListener
                public final boolean onEditorAction(TextView textView3, int i3, KeyEvent keyEvent) {
                    boolean lambda$new$11;
                    lambda$new$11 = LoginActivity.LoginActivityRegisterView.this.lambda$new$11(textView3, i3, keyEvent);
                    return lambda$new$11;
                }
            });
            buildEditTextLayout(AndroidUtilities.isSmallScreen());
            TextView textView3 = new TextView(context);
            this.wrongNumber = textView3;
            textView3.setText(LocaleController.getString("CancelRegistration", R.string.CancelRegistration));
            this.wrongNumber.setGravity((LocaleController.isRTL ? 5 : 3) | 1);
            this.wrongNumber.setTextSize(1, 14.0f);
            this.wrongNumber.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.wrongNumber.setPadding(0, AndroidUtilities.dp(24.0f), 0, 0);
            this.wrongNumber.setVisibility(8);
            addView(this.wrongNumber, LayoutHelper.createLinear(-2, -2, (LocaleController.isRTL ? 5 : 3) | 48, 0, 20, 0, 0));
            this.wrongNumber.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    LoginActivity.LoginActivityRegisterView.this.lambda$new$12(view2);
                }
            });
            FrameLayout frameLayout3 = new FrameLayout(context);
            addView(frameLayout3, LayoutHelper.createLinear(-1, -1, 83));
            TextView textView4 = new TextView(context);
            this.privacyView = textView4;
            textView4.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
            this.privacyView.setTextSize(1, AndroidUtilities.isSmallScreen() ? 13.0f : 14.0f);
            this.privacyView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.privacyView.setGravity(16);
            frameLayout3.addView(this.privacyView, LayoutHelper.createFrame(-2, Build.VERSION.SDK_INT >= 21 ? 56.0f : 60.0f, 83, 14.0f, 0.0f, 70.0f, 32.0f));
            VerticalPositionAutoAnimator.attach(this.privacyView);
            String string = LocaleController.getString("TermsOfServiceLogin", R.string.TermsOfServiceLogin);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
            int indexOf = string.indexOf(42);
            int lastIndexOf = string.lastIndexOf(42);
            if (indexOf != -1 && lastIndexOf != -1 && indexOf != lastIndexOf) {
                spannableStringBuilder.replace(lastIndexOf, lastIndexOf + 1, (CharSequence) "");
                spannableStringBuilder.replace(indexOf, indexOf + 1, (CharSequence) "");
                spannableStringBuilder.setSpan(new LinkSpan(), indexOf, lastIndexOf - 1, 33);
            }
            this.privacyView.setText(spannableStringBuilder);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$7(View view) {
            this.imageUpdater.openMenu(this.avatar != null, new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityRegisterView.this.lambda$new$4();
                }
            }, new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    LoginActivity.LoginActivityRegisterView.this.lambda$new$6(dialogInterface);
                }
            }, 0);
            this.isCameraWaitAnimationAllowed = false;
            this.avatarEditor.setAnimation(this.cameraDrawable);
            this.cameraDrawable.setCurrentFrame(0);
            this.cameraDrawable.setCustomEndFrame(43);
            this.avatarEditor.playAnimation();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$4() {
            this.avatar = null;
            this.avatarBig = null;
            showAvatarProgress(false, true);
            this.avatarImage.setImage((ImageLocation) null, (String) null, this.avatarDrawable, (Object) null);
            this.avatarEditor.setAnimation(this.cameraDrawable);
            this.cameraDrawable.setCurrentFrame(0);
            this.isCameraWaitAnimationAllowed = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$6(DialogInterface dialogInterface) {
            if (!this.imageUpdater.isUploadingImage()) {
                this.avatarEditor.setAnimation(this.cameraDrawable);
                this.cameraDrawable.setCustomEndFrame(86);
                this.avatarEditor.setOnAnimationEndListener(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivityRegisterView.this.lambda$new$5();
                    }
                });
                this.avatarEditor.playAnimation();
                return;
            }
            this.avatarEditor.setAnimation(this.cameraDrawable);
            this.cameraDrawable.setCurrentFrame(0, false);
            this.isCameraWaitAnimationAllowed = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$5() {
            this.isCameraWaitAnimationAllowed = true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public class 4 implements View.OnAttachStateChangeListener {
            private boolean isAttached;
            private long lastRun = System.currentTimeMillis();
            private Runnable cameraWaitCallback = new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$4$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityRegisterView.4.this.lambda$$2();
                }
            };

            4(LoginActivity loginActivity) {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$$2() {
                if (this.isAttached) {
                    if (LoginActivityRegisterView.this.isCameraWaitAnimationAllowed && System.currentTimeMillis() - this.lastRun >= 10000) {
                        LoginActivityRegisterView.this.avatarEditor.setAnimation(LoginActivityRegisterView.this.cameraWaitDrawable);
                        LoginActivityRegisterView.this.cameraWaitDrawable.setCurrentFrame(0, false);
                        LoginActivityRegisterView.this.cameraWaitDrawable.setOnAnimationEndListener(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$4$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                LoginActivity.LoginActivityRegisterView.4.this.lambda$$1();
                            }
                        });
                        LoginActivityRegisterView.this.avatarEditor.playAnimation();
                        this.lastRun = System.currentTimeMillis();
                    }
                    LoginActivityRegisterView.this.avatarEditor.postDelayed(this.cameraWaitCallback, 1000L);
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$$1() {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$4$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivityRegisterView.4.this.lambda$$0();
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$$0() {
                LoginActivityRegisterView.this.cameraDrawable.setCurrentFrame(0, false);
                LoginActivityRegisterView.this.avatarEditor.setAnimation(LoginActivityRegisterView.this.cameraDrawable);
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
                this.isAttached = true;
                view.post(this.cameraWaitCallback);
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
                this.isAttached = false;
                view.removeCallbacks(this.cameraWaitCallback);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$8(View view, boolean z) {
            this.firstNameOutlineView.animateSelection(z ? 1.0f : 0.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$9(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 5) {
                this.lastNameField.requestFocus();
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$10(View view, boolean z) {
            this.lastNameOutlineView.animateSelection(z ? 1.0f : 0.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$new$11(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 6 || i == 5) {
                onNextPressed(null);
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$12(View view) {
            if (LoginActivity.this.radialProgressView.getTag() != null) {
                return;
            }
            onBackPressed(false);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void updateColors() {
            this.avatarDrawable.invalidateSelf();
            this.titleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.descriptionTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.firstNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.firstNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteInputFieldActivated"));
            this.lastNameField.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.lastNameField.setCursorColor(Theme.getColor("windowBackgroundWhiteInputFieldActivated"));
            this.wrongNumber.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            this.privacyView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.privacyView.setLinkTextColor(Theme.getColor("windowBackgroundWhiteLinkText"));
            this.firstNameOutlineView.updateColor();
            this.lastNameOutlineView.updateColor();
        }

        private void buildEditTextLayout(boolean z) {
            boolean hasFocus = this.firstNameField.hasFocus();
            boolean hasFocus2 = this.lastNameField.hasFocus();
            this.editTextContainer.removeAllViews();
            if (z) {
                LinearLayout linearLayout = new LinearLayout(LoginActivity.this.getParentActivity());
                linearLayout.setOrientation(0);
                this.firstNameOutlineView.setText(LocaleController.getString(R.string.FirstNameSmall));
                this.lastNameOutlineView.setText(LocaleController.getString(R.string.LastNameSmall));
                linearLayout.addView(this.firstNameOutlineView, LayoutHelper.createLinear(0, -2, 1.0f, 0, 0, 8, 0));
                linearLayout.addView(this.lastNameOutlineView, LayoutHelper.createLinear(0, -2, 1.0f, 8, 0, 0, 0));
                this.editTextContainer.addView(linearLayout);
                if (hasFocus) {
                    this.firstNameField.requestFocus();
                    AndroidUtilities.showKeyboard(this.firstNameField);
                    return;
                } else if (hasFocus2) {
                    this.lastNameField.requestFocus();
                    AndroidUtilities.showKeyboard(this.lastNameField);
                    return;
                } else {
                    return;
                }
            }
            this.firstNameOutlineView.setText(LocaleController.getString(R.string.FirstName));
            this.lastNameOutlineView.setText(LocaleController.getString(R.string.LastName));
            this.editTextContainer.addView(this.firstNameOutlineView, LayoutHelper.createFrame(-1, -2.0f, 48, 8.0f, 0.0f, 8.0f, 0.0f));
            this.editTextContainer.addView(this.lastNameOutlineView, LayoutHelper.createFrame(-1, -2.0f, 48, 8.0f, 82.0f, 8.0f, 0.0f));
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public void didUploadPhoto(TLRPC$InputFile tLRPC$InputFile, TLRPC$InputFile tLRPC$InputFile2, double d, String str, final TLRPC$PhotoSize tLRPC$PhotoSize, final TLRPC$PhotoSize tLRPC$PhotoSize2, boolean z, TLRPC$VideoSize tLRPC$VideoSize) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityRegisterView.this.lambda$didUploadPhoto$13(tLRPC$PhotoSize2, tLRPC$PhotoSize);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$didUploadPhoto$13(TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2) {
            TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize.location;
            this.avatar = tLRPC$FileLocation;
            this.avatarBig = tLRPC$PhotoSize2.location;
            this.avatarImage.setImage(ImageLocation.getForLocal(tLRPC$FileLocation), "50_50", this.avatarDrawable, (Object) null);
        }

        private void showAvatarProgress(final boolean z, boolean z2) {
            if (this.avatarEditor == null) {
                return;
            }
            AnimatorSet animatorSet = this.avatarAnimation;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.avatarAnimation = null;
            }
            if (z2) {
                this.avatarAnimation = new AnimatorSet();
                if (z) {
                    this.avatarProgressView.setVisibility(0);
                    this.avatarAnimation.playTogether(ObjectAnimator.ofFloat(this.avatarEditor, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, 1.0f));
                } else {
                    this.avatarEditor.setVisibility(0);
                    this.avatarAnimation.playTogether(ObjectAnimator.ofFloat(this.avatarEditor, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.avatarProgressView, View.ALPHA, 0.0f));
                }
                this.avatarAnimation.setDuration(180L);
                this.avatarAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LoginActivity.LoginActivityRegisterView.6
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (LoginActivityRegisterView.this.avatarAnimation == null || LoginActivityRegisterView.this.avatarEditor == null) {
                            return;
                        }
                        if (z) {
                            LoginActivityRegisterView.this.avatarEditor.setVisibility(4);
                        } else {
                            LoginActivityRegisterView.this.avatarProgressView.setVisibility(4);
                        }
                        LoginActivityRegisterView.this.avatarAnimation = null;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        LoginActivityRegisterView.this.avatarAnimation = null;
                    }
                });
                this.avatarAnimation.start();
            } else if (z) {
                this.avatarEditor.setAlpha(1.0f);
                this.avatarEditor.setVisibility(4);
                this.avatarProgressView.setAlpha(1.0f);
                this.avatarProgressView.setVisibility(0);
            } else {
                this.avatarEditor.setAlpha(1.0f);
                this.avatarEditor.setVisibility(0);
                this.avatarProgressView.setAlpha(0.0f);
                this.avatarProgressView.setVisibility(4);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean onBackPressed(boolean z) {
            if (z) {
                LoginActivity.this.needHideProgress(true);
                this.nextPressed = false;
                this.currentParams = null;
                return true;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString(R.string.Warning));
            builder.setMessage(LocaleController.getString("AreYouSureRegistration", R.string.AreYouSureRegistration));
            builder.setNegativeButton(LocaleController.getString("Stop", R.string.Stop), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    LoginActivity.LoginActivityRegisterView.this.lambda$onBackPressed$14(dialogInterface, i);
                }
            });
            builder.setPositiveButton(LocaleController.getString("Continue", R.string.Continue), null);
            LoginActivity.this.showDialog(builder.create());
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBackPressed$14(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            LoginActivity.this.setPage(0, true, null, true);
            hidePrivacyView();
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            return LocaleController.getString("YourName", R.string.YourName);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onCancelPressed() {
            this.nextPressed = false;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onShow() {
            super.onShow();
            if (this.privacyView != null) {
                if (LoginActivity.this.restoringState) {
                    this.privacyView.setAlpha(1.0f);
                } else {
                    this.privacyView.setAlpha(0.0f);
                    this.privacyView.animate().alpha(1.0f).setDuration(200L).setStartDelay(300L).setInterpolator(AndroidUtilities.decelerateInterpolator).start();
                }
            }
            EditTextBoldCursor editTextBoldCursor = this.firstNameField;
            if (editTextBoldCursor != null) {
                editTextBoldCursor.requestFocus();
                EditTextBoldCursor editTextBoldCursor2 = this.firstNameField;
                editTextBoldCursor2.setSelection(editTextBoldCursor2.length());
                AndroidUtilities.showKeyboard(this.firstNameField);
            }
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityRegisterView.this.lambda$onShow$15();
                }
            }, LoginActivity.SHOW_DELAY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onShow$15() {
            EditTextBoldCursor editTextBoldCursor = this.firstNameField;
            if (editTextBoldCursor != null) {
                editTextBoldCursor.requestFocus();
                EditTextBoldCursor editTextBoldCursor2 = this.firstNameField;
                editTextBoldCursor2.setSelection(editTextBoldCursor2.length());
                AndroidUtilities.showKeyboard(this.firstNameField);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void setParams(Bundle bundle, boolean z) {
            if (bundle == null) {
                return;
            }
            this.firstNameField.setText("");
            this.lastNameField.setText("");
            this.requestPhone = bundle.getString("phoneFormated");
            this.phoneHash = bundle.getString("phoneHash");
            this.currentParams = bundle;
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onNextPressed(String str) {
            if (this.nextPressed) {
                return;
            }
            if (LoginActivity.this.currentTermsOfService != null && LoginActivity.this.currentTermsOfService.popup) {
                showTermsOfService(true);
            } else if (this.firstNameField.length() == 0) {
                LoginActivity.this.onFieldError(this.firstNameOutlineView, true);
            } else {
                this.nextPressed = true;
                TLRPC$TL_auth_signUp tLRPC$TL_auth_signUp = new TLRPC$TL_auth_signUp();
                tLRPC$TL_auth_signUp.phone_code_hash = this.phoneHash;
                tLRPC$TL_auth_signUp.phone_number = this.requestPhone;
                tLRPC$TL_auth_signUp.first_name = this.firstNameField.getText().toString();
                tLRPC$TL_auth_signUp.last_name = this.lastNameField.getText().toString();
                LoginActivity.this.needShowProgress(0);
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_signUp, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda19
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        LoginActivity.LoginActivityRegisterView.this.lambda$onNextPressed$19(tLObject, tLRPC$TL_error);
                    }
                }, 10);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$19(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityRegisterView.this.lambda$onNextPressed$18(tLObject, tLRPC$TL_error);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$18(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            this.nextPressed = false;
            if (!(tLObject instanceof TLRPC$TL_auth_authorization)) {
                LoginActivity.this.needHideProgress(false);
                if (tLRPC$TL_error.text.contains("PHONE_NUMBER_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("InvalidPhoneNumber", R.string.InvalidPhoneNumber));
                    return;
                } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("InvalidCode", R.string.InvalidCode));
                    return;
                } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("CodeExpired", R.string.CodeExpired));
                    return;
                } else if (tLRPC$TL_error.text.contains("FIRSTNAME_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("InvalidFirstName", R.string.InvalidFirstName));
                    return;
                } else if (tLRPC$TL_error.text.contains("LASTNAME_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("InvalidLastName", R.string.InvalidLastName));
                    return;
                } else {
                    LoginActivity.this.needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), tLRPC$TL_error.text);
                    return;
                }
            }
            hidePrivacyView();
            LoginActivity.this.showDoneButton(false, true);
            postDelayed(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda15
                @Override // java.lang.Runnable
                public final void run() {
                    LoginActivity.LoginActivityRegisterView.this.lambda$onNextPressed$17(tLObject);
                }
            }, 150L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$17(TLObject tLObject) {
            LoginActivity.this.needHideProgress(false, false);
            AndroidUtilities.hideKeyboard(((BaseFragment) LoginActivity.this).fragmentView.findFocus());
            LoginActivity.this.onAuthSuccess((TLRPC$TL_auth_authorization) tLObject, true);
            final TLRPC$FileLocation tLRPC$FileLocation = this.avatarBig;
            if (tLRPC$FileLocation != null) {
                Utilities.cacheClearQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda17
                    @Override // java.lang.Runnable
                    public final void run() {
                        LoginActivity.LoginActivityRegisterView.this.lambda$onNextPressed$16(tLRPC$FileLocation);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onNextPressed$16(TLRPC$FileLocation tLRPC$FileLocation) {
            MessagesController.getInstance(((BaseFragment) LoginActivity.this).currentAccount).uploadAndApplyUserAvatar(tLRPC$FileLocation);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void saveStateParams(Bundle bundle) {
            String obj = this.firstNameField.getText().toString();
            if (obj.length() != 0) {
                bundle.putString("registerview_first", obj);
            }
            String obj2 = this.lastNameField.getText().toString();
            if (obj2.length() != 0) {
                bundle.putString("registerview_last", obj2);
            }
            if (LoginActivity.this.currentTermsOfService != null) {
                SerializedData serializedData = new SerializedData(LoginActivity.this.currentTermsOfService.getObjectSize());
                LoginActivity.this.currentTermsOfService.serializeToStream(serializedData);
                bundle.putString("terms", Base64.encodeToString(serializedData.toByteArray(), 0));
                serializedData.cleanup();
            }
            Bundle bundle2 = this.currentParams;
            if (bundle2 != null) {
                bundle.putBundle("registerview_params", bundle2);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void restoreStateParams(Bundle bundle) {
            byte[] decode;
            Bundle bundle2 = bundle.getBundle("registerview_params");
            this.currentParams = bundle2;
            if (bundle2 != null) {
                setParams(bundle2, true);
            }
            try {
                String string = bundle.getString("terms");
                if (string != null && (decode = Base64.decode(string, 0)) != null) {
                    SerializedData serializedData = new SerializedData(decode);
                    LoginActivity.this.currentTermsOfService = TLRPC$TL_help_termsOfService.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                    serializedData.cleanup();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            String string2 = bundle.getString("registerview_first");
            if (string2 != null) {
                this.firstNameField.setText(string2);
            }
            String string3 = bundle.getString("registerview_last");
            if (string3 != null) {
                this.lastNameField.setText(string3);
            }
        }

        private void hidePrivacyView() {
            this.privacyView.animate().alpha(0.0f).setDuration(150L).setStartDelay(0L).setInterpolator(AndroidUtilities.accelerateInterpolator).start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean showKeyboard(View view) {
        if (isCustomKeyboardVisible()) {
            return true;
        }
        return AndroidUtilities.showKeyboard(view);
    }

    public LoginActivity setIntroView(View view, TextView textView) {
        this.introView = view;
        this.startMessagingButton = textView;
        this.isAnimatingIntro = true;
        return this;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public AnimatorSet onCustomTransitionAnimation(boolean z, final Runnable runnable) {
        if (!z || this.introView == null) {
            return null;
        }
        if (this.fragmentView.getParent() instanceof View) {
            ((View) this.fragmentView.getParent()).setTranslationX(0.0f);
        }
        final TransformableLoginButtonView transformableLoginButtonView = new TransformableLoginButtonView(this.fragmentView.getContext());
        transformableLoginButtonView.setButtonText(this.startMessagingButton.getPaint(), this.startMessagingButton.getText().toString());
        final int width = this.startMessagingButton.getWidth();
        final int height = this.startMessagingButton.getHeight();
        final int i = this.floatingButtonIcon.getLayoutParams().width;
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        transformableLoginButtonView.setLayoutParams(layoutParams);
        int[] iArr = new int[2];
        this.fragmentView.getLocationInWindow(iArr);
        int i2 = iArr[0];
        int i3 = iArr[1];
        this.startMessagingButton.getLocationInWindow(iArr);
        final float f = iArr[0] - i2;
        final float f2 = iArr[1] - i3;
        transformableLoginButtonView.setTranslationX(f);
        transformableLoginButtonView.setTranslationY(f2);
        final int width2 = (((getParentLayout().getView().getWidth() - this.floatingButtonIcon.getLayoutParams().width) - ((ViewGroup.MarginLayoutParams) this.floatingButtonContainer.getLayoutParams()).rightMargin) - getParentLayout().getView().getPaddingLeft()) - getParentLayout().getView().getPaddingRight();
        final int height2 = ((((getParentLayout().getView().getHeight() - this.floatingButtonIcon.getLayoutParams().height) - ((ViewGroup.MarginLayoutParams) this.floatingButtonContainer.getLayoutParams()).bottomMargin) - (isCustomKeyboardVisible() ? AndroidUtilities.dp(230.0f) : 0)) - getParentLayout().getView().getPaddingTop()) - getParentLayout().getView().getPaddingBottom();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LoginActivity.12
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                LoginActivity.this.floatingButtonContainer.setVisibility(4);
                LoginActivity.this.keyboardLinearLayout.setAlpha(0.0f);
                ((BaseFragment) LoginActivity.this).fragmentView.setBackgroundColor(0);
                LoginActivity.this.startMessagingButton.setVisibility(4);
                ((FrameLayout) ((BaseFragment) LoginActivity.this).fragmentView).addView(transformableLoginButtonView);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                LoginActivity.this.keyboardLinearLayout.setAlpha(1.0f);
                LoginActivity.this.startMessagingButton.setVisibility(0);
                ((BaseFragment) LoginActivity.this).fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                LoginActivity.this.floatingButtonContainer.setVisibility(0);
                ((FrameLayout) ((BaseFragment) LoginActivity.this).fragmentView).removeView(transformableLoginButtonView);
                if (LoginActivity.this.animationFinishCallback != null) {
                    AndroidUtilities.runOnUIThread(LoginActivity.this.animationFinishCallback);
                    LoginActivity.this.animationFinishCallback = null;
                }
                LoginActivity.this.isAnimatingIntro = false;
                runnable.run();
            }
        });
        final int color = Theme.getColor("windowBackgroundWhite");
        final int alpha = Color.alpha(color);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                LoginActivity.this.lambda$onCustomTransitionAnimation$26(color, alpha, layoutParams, width, i, height, transformableLoginButtonView, f, width2, f2, height2, valueAnimator);
            }
        });
        ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300L);
        animatorSet.playTogether(ofFloat);
        animatorSet.start();
        return animatorSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCustomTransitionAnimation$26(int i, int i2, ViewGroup.MarginLayoutParams marginLayoutParams, int i3, int i4, int i5, TransformableLoginButtonView transformableLoginButtonView, float f, int i6, float f2, int i7, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.keyboardLinearLayout.setAlpha(floatValue);
        this.fragmentView.setBackgroundColor(ColorUtils.setAlphaComponent(i, (int) (i2 * floatValue)));
        float f3 = 1.0f - floatValue;
        this.slideViewsContainer.setTranslationY(AndroidUtilities.dp(20.0f) * f3);
        if (!isCustomKeyboardForceDisabled()) {
            CustomPhoneKeyboardView customPhoneKeyboardView = this.keyboardView;
            customPhoneKeyboardView.setTranslationY(customPhoneKeyboardView.getLayoutParams().height * f3);
            this.floatingButtonContainer.setTranslationY(this.keyboardView.getLayoutParams().height * f3);
        }
        this.introView.setTranslationY((-AndroidUtilities.dp(20.0f)) * floatValue);
        float f4 = (f3 * 0.05f) + 0.95f;
        this.introView.setScaleX(f4);
        this.introView.setScaleY(f4);
        marginLayoutParams.width = (int) (i3 + ((i4 - i3) * floatValue));
        marginLayoutParams.height = (int) (i5 + ((i4 - i5) * floatValue));
        transformableLoginButtonView.requestLayout();
        transformableLoginButtonView.setProgress(floatValue);
        transformableLoginButtonView.setTranslationX(f + ((i6 - f) * floatValue));
        transformableLoginButtonView.setTranslationY(f2 + ((i7 - f2) * floatValue));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateColors() {
        this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
        Activity parentActivity = getParentActivity();
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
        if (Build.VERSION.SDK_INT < 21) {
            Drawable mutate = parentActivity.getResources().getDrawable(R.drawable.floating_shadow).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
            CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
            combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
            createSimpleSelectorCircleDrawable = combinedDrawable;
        }
        this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable);
        this.backButtonView.setColorFilter(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.backButtonView.setBackground(Theme.createSelectorDrawable(Theme.getColor("listSelectorSDK21")));
        this.radialProgressView.setProgressColor(Theme.getColor("chats_actionBackground"));
        this.floatingButtonIcon.setColor(Theme.getColor("chats_actionIcon"));
        this.floatingButtonIcon.setBackgroundColor(Theme.getColor("chats_actionBackground"));
        this.floatingProgressView.setProgressColor(Theme.getColor("chats_actionIcon"));
        for (SlideView slideView : this.views) {
            slideView.updateColors();
        }
        this.keyboardView.updateColors();
        PhoneNumberConfirmView phoneNumberConfirmView = this.phoneNumberConfirmView;
        if (phoneNumberConfirmView != null) {
            phoneNumberConfirmView.updateColors();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        return SimpleThemeDescription.createThemeDescriptions(new ThemeDescription.ThemeDescriptionDelegate() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda29
            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public final void didSetColor() {
                LoginActivity.this.updateColors();
            }

            @Override // org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate
            public /* synthetic */ void onAnimationProgress(float f) {
                ThemeDescription.ThemeDescriptionDelegate.-CC.$default$onAnimationProgress(this, f);
            }
        }, "windowBackgroundWhiteBlackText", "windowBackgroundWhiteGrayText6", "windowBackgroundWhiteHintText", "listSelectorSDK21", "chats_actionBackground", "chats_actionIcon", "windowBackgroundWhiteInputField", "windowBackgroundWhiteInputFieldActivated", "windowBackgroundWhiteValueText", "dialogTextRed", "windowBackgroundWhiteGrayText", "checkbox", "windowBackgroundWhiteBlueText4", "changephoneinfo_image2", "chats_actionPressedBackground", "windowBackgroundWhiteRedText2", "windowBackgroundWhiteLinkText", "checkboxSquareUnchecked", "checkboxSquareBackground", "checkboxSquareCheck", "dialogBackground", "dialogTextGray2", "dialogTextBlack");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tryResetAccount(final String str, final String str2, final String str3) {
        if (this.radialProgressView.getTag() != null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(LocaleController.getString("ResetMyAccountWarningText", R.string.ResetMyAccountWarningText));
        builder.setTitle(LocaleController.getString("ResetMyAccountWarning", R.string.ResetMyAccountWarning));
        builder.setPositiveButton(LocaleController.getString("ResetMyAccountWarningReset", R.string.ResetMyAccountWarningReset), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda8
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                LoginActivity.this.lambda$tryResetAccount$29(str, str2, str3, dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$tryResetAccount$29(final String str, final String str2, final String str3, DialogInterface dialogInterface, int i) {
        needShowProgress(0);
        TLRPC$TL_account_deleteAccount tLRPC$TL_account_deleteAccount = new TLRPC$TL_account_deleteAccount();
        tLRPC$TL_account_deleteAccount.reason = "Forgot password";
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_deleteAccount, new RequestDelegate() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda27
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                LoginActivity.this.lambda$tryResetAccount$28(str, str2, str3, tLObject, tLRPC$TL_error);
            }
        }, 10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$tryResetAccount$28(final String str, final String str2, final String str3, TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.LoginActivity$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                LoginActivity.this.lambda$tryResetAccount$27(tLRPC$TL_error, str, str2, str3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$tryResetAccount$27(TLRPC$TL_error tLRPC$TL_error, String str, String str2, String str3) {
        needHideProgress(false);
        if (tLRPC$TL_error == null) {
            if (str == null || str2 == null || str3 == null) {
                setPage(0, true, null, true);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("phoneFormated", str);
            bundle.putString("phoneHash", str2);
            bundle.putString("code", str3);
            setPage(5, true, bundle, false);
        } else if (tLRPC$TL_error.text.equals("2FA_RECENT_CONFIRM")) {
            needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), LocaleController.getString("ResetAccountCancelledAlert", R.string.ResetAccountCancelledAlert));
        } else if (tLRPC$TL_error.text.startsWith("2FA_CONFIRM_WAIT_")) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("phoneFormated", str);
            bundle2.putString("phoneHash", str2);
            bundle2.putString("code", str3);
            bundle2.putInt("startTime", ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
            bundle2.putInt("waitTime", Utilities.parseInt((CharSequence) tLRPC$TL_error.text.replace("2FA_CONFIRM_WAIT_", "")).intValue());
            setPage(8, true, bundle2, false);
        } else {
            needShowAlert(LocaleController.getString(R.string.RestorePasswordNoEmailTitle), tLRPC$TL_error.text);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class PhoneNumberConfirmView extends FrameLayout {
        private View blurredView;
        private IConfirmDialogCallback callback;
        private TextView confirmMessageView;
        private TextView confirmTextView;
        private View dimmView;
        private boolean dismissed;
        private TextView editTextView;
        private View fabContainer;
        private TransformableLoginButtonView fabTransform;
        private RadialProgressView floatingProgressView;
        private ViewGroup fragmentView;
        private TextView numberView;
        private FrameLayout popupFabContainer;
        private FrameLayout popupLayout;

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public interface IConfirmDialogCallback {
            void onConfirmPressed(PhoneNumberConfirmView phoneNumberConfirmView, TextView textView);

            void onDismiss(PhoneNumberConfirmView phoneNumberConfirmView);

            void onEditPressed(PhoneNumberConfirmView phoneNumberConfirmView, TextView textView);

            void onFabPressed(PhoneNumberConfirmView phoneNumberConfirmView, TransformableLoginButtonView transformableLoginButtonView);
        }

        private PhoneNumberConfirmView(Context context, ViewGroup viewGroup, View view, String str, final IConfirmDialogCallback iConfirmDialogCallback) {
            super(context);
            this.fragmentView = viewGroup;
            this.fabContainer = view;
            this.callback = iConfirmDialogCallback;
            View view2 = new View(getContext());
            this.blurredView = view2;
            view2.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    LoginActivity.PhoneNumberConfirmView.this.lambda$new$0(view3);
                }
            });
            addView(this.blurredView, LayoutHelper.createFrame(-1, -1.0f));
            View view3 = new View(getContext());
            this.dimmView = view3;
            view3.setBackgroundColor(1073741824);
            this.dimmView.setAlpha(0.0f);
            addView(this.dimmView, LayoutHelper.createFrame(-1, -1.0f));
            TransformableLoginButtonView transformableLoginButtonView = new TransformableLoginButtonView(getContext());
            this.fabTransform = transformableLoginButtonView;
            transformableLoginButtonView.setTransformType(1);
            this.fabTransform.setDrawBackground(false);
            FrameLayout frameLayout = new FrameLayout(context);
            this.popupFabContainer = frameLayout;
            frameLayout.addView(this.fabTransform, LayoutHelper.createFrame(-1, -1.0f));
            this.popupFabContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view4) {
                    LoginActivity.PhoneNumberConfirmView.this.lambda$new$1(iConfirmDialogCallback, view4);
                }
            });
            RadialProgressView radialProgressView = new RadialProgressView(context);
            this.floatingProgressView = radialProgressView;
            radialProgressView.setSize(AndroidUtilities.dp(22.0f));
            this.floatingProgressView.setAlpha(0.0f);
            this.floatingProgressView.setScaleX(0.1f);
            this.floatingProgressView.setScaleY(0.1f);
            this.popupFabContainer.addView(this.floatingProgressView, LayoutHelper.createFrame(-1, -1.0f));
            this.popupFabContainer.setContentDescription(LocaleController.getString(R.string.Done));
            View view4 = this.popupFabContainer;
            int i = Build.VERSION.SDK_INT;
            addView(view4, LayoutHelper.createFrame(i >= 21 ? 56 : 60, i >= 21 ? 56.0f : 60.0f));
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.popupLayout = frameLayout2;
            addView(frameLayout2, LayoutHelper.createFrame(-1, 140.0f, 49, 24.0f, 0.0f, 24.0f, 0.0f));
            TextView textView = new TextView(context);
            this.confirmMessageView = textView;
            textView.setText(LocaleController.getString(R.string.ConfirmCorrectNumber));
            this.confirmMessageView.setTextSize(1, 14.0f);
            this.confirmMessageView.setSingleLine();
            this.popupLayout.addView(this.confirmMessageView, LayoutHelper.createFrame(-1, -2.0f, LocaleController.isRTL ? 5 : 3, 24.0f, 20.0f, 24.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.numberView = textView2;
            textView2.setText(str);
            this.numberView.setTextSize(1, 18.0f);
            this.numberView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.numberView.setSingleLine();
            this.popupLayout.addView(this.numberView, LayoutHelper.createFrame(-1, -2.0f, LocaleController.isRTL ? 5 : 3, 24.0f, 48.0f, 24.0f, 0.0f));
            int dp = AndroidUtilities.dp(16.0f);
            TextView textView3 = new TextView(context);
            this.editTextView = textView3;
            textView3.setText(LocaleController.getString(R.string.Edit));
            this.editTextView.setSingleLine();
            this.editTextView.setTextSize(1, 16.0f);
            this.editTextView.setBackground(Theme.getRoundRectSelectorDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("changephoneinfo_image2")));
            this.editTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view5) {
                    LoginActivity.PhoneNumberConfirmView.this.lambda$new$2(iConfirmDialogCallback, view5);
                }
            });
            this.editTextView.setTypeface(Typeface.DEFAULT_BOLD);
            int i2 = dp / 2;
            this.editTextView.setPadding(dp, i2, dp, i2);
            float f = 8;
            this.popupLayout.addView(this.editTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 80, f, f, f, f));
            TextView textView4 = new TextView(context);
            this.confirmTextView = textView4;
            textView4.setText(LocaleController.getString(R.string.CheckPhoneNumberYes));
            this.confirmTextView.setSingleLine();
            this.confirmTextView.setTextSize(1, 16.0f);
            this.confirmTextView.setBackground(Theme.getRoundRectSelectorDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("changephoneinfo_image2")));
            this.confirmTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view5) {
                    LoginActivity.PhoneNumberConfirmView.this.lambda$new$3(iConfirmDialogCallback, view5);
                }
            });
            this.confirmTextView.setTypeface(Typeface.DEFAULT_BOLD);
            this.confirmTextView.setPadding(dp, i2, dp, i2);
            this.popupLayout.addView(this.confirmTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 3 : 5) | 80, f, f, f, f));
            updateFabPosition();
            updateColors();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            dismiss();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(IConfirmDialogCallback iConfirmDialogCallback, View view) {
            iConfirmDialogCallback.onFabPressed(this, this.fabTransform);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$2(IConfirmDialogCallback iConfirmDialogCallback, View view) {
            iConfirmDialogCallback.onEditPressed(this, this.editTextView);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$3(IConfirmDialogCallback iConfirmDialogCallback, View view) {
            iConfirmDialogCallback.onConfirmPressed(this, this.confirmTextView);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateFabPosition() {
            int[] iArr = new int[2];
            this.fragmentView.getLocationInWindow(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            this.fabContainer.getLocationInWindow(iArr);
            this.popupFabContainer.setTranslationX(iArr[0] - i);
            this.popupFabContainer.setTranslationY(iArr[1] - i2);
            requestLayout();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateColors() {
            this.fabTransform.setColor(Theme.getColor("chats_actionIcon"));
            this.fabTransform.setBackgroundColor(Theme.getColor("chats_actionBackground"));
            this.popupLayout.setBackground(Theme.createRoundRectDrawable(AndroidUtilities.dp(12.0f), Theme.getColor("dialogBackground")));
            this.confirmMessageView.setTextColor(Theme.getColor("dialogTextGray2"));
            this.numberView.setTextColor(Theme.getColor("dialogTextBlack"));
            this.editTextView.setTextColor(Theme.getColor("changephoneinfo_image2"));
            this.confirmTextView.setTextColor(Theme.getColor("changephoneinfo_image2"));
            this.popupFabContainer.setBackground(Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground")));
            this.floatingProgressView.setProgressColor(Theme.getColor("chats_actionIcon"));
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            int measuredHeight = this.popupLayout.getMeasuredHeight();
            int translationY = (int) (this.popupFabContainer.getTranslationY() - AndroidUtilities.dp(32.0f));
            FrameLayout frameLayout = this.popupLayout;
            frameLayout.layout(frameLayout.getLeft(), translationY - measuredHeight, this.popupLayout.getRight(), translationY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void show() {
            if (Build.VERSION.SDK_INT >= 21) {
                View view = this.fabContainer;
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Z, view.getTranslationZ(), 0.0f).setDuration(150L).start();
            }
            ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(250L);
            duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LoginActivity.PhoneNumberConfirmView.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    PhoneNumberConfirmView.this.fabContainer.setVisibility(8);
                    int measuredWidth = (int) (PhoneNumberConfirmView.this.fragmentView.getMeasuredWidth() / 10.0f);
                    int measuredHeight = (int) (PhoneNumberConfirmView.this.fragmentView.getMeasuredHeight() / 10.0f);
                    Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(createBitmap);
                    canvas.scale(0.1f, 0.1f);
                    canvas.drawColor(Theme.getColor("windowBackgroundWhite"));
                    PhoneNumberConfirmView.this.fragmentView.draw(canvas);
                    Utilities.stackBlurBitmap(createBitmap, Math.max(8, Math.max(measuredWidth, measuredHeight) / ImageReceiver.DEFAULT_CROSSFADE_DURATION));
                    PhoneNumberConfirmView.this.blurredView.setBackground(new BitmapDrawable(PhoneNumberConfirmView.this.getContext().getResources(), createBitmap));
                    PhoneNumberConfirmView.this.blurredView.setAlpha(0.0f);
                    PhoneNumberConfirmView.this.blurredView.setVisibility(0);
                    PhoneNumberConfirmView.this.fragmentView.addView(PhoneNumberConfirmView.this);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (AndroidUtilities.isAccessibilityTouchExplorationEnabled()) {
                        PhoneNumberConfirmView.this.popupFabContainer.requestFocus();
                    }
                }
            });
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LoginActivity.PhoneNumberConfirmView.this.lambda$show$4(valueAnimator);
                }
            });
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$show$4(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.fabTransform.setProgress(floatValue);
            this.blurredView.setAlpha(floatValue);
            this.dimmView.setAlpha(floatValue);
            this.popupLayout.setAlpha(floatValue);
            float f = (floatValue * 0.5f) + 0.5f;
            this.popupLayout.setScaleX(f);
            this.popupLayout.setScaleY(f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void animateProgress(final Runnable runnable) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addListener(new AnimatorListenerAdapter(this) { // from class: org.telegram.ui.LoginActivity.PhoneNumberConfirmView.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    runnable.run();
                }
            });
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LoginActivity.PhoneNumberConfirmView.this.lambda$animateProgress$5(valueAnimator);
                }
            });
            ofFloat.setDuration(150L);
            ofFloat.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$animateProgress$5(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            float f = 1.0f - floatValue;
            float f2 = (f * 0.9f) + 0.1f;
            this.fabTransform.setScaleX(f2);
            this.fabTransform.setScaleY(f2);
            this.fabTransform.setAlpha(f);
            float f3 = (0.9f * floatValue) + 0.1f;
            this.floatingProgressView.setScaleX(f3);
            this.floatingProgressView.setScaleY(f3);
            this.floatingProgressView.setAlpha(floatValue);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dismiss() {
            if (this.dismissed) {
                return;
            }
            this.dismissed = true;
            this.callback.onDismiss(this);
            ValueAnimator duration = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(250L);
            duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.LoginActivity.PhoneNumberConfirmView.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (PhoneNumberConfirmView.this.getParent() instanceof ViewGroup) {
                        ((ViewGroup) PhoneNumberConfirmView.this.getParent()).removeView(PhoneNumberConfirmView.this);
                    }
                    if (Build.VERSION.SDK_INT >= 21) {
                        ObjectAnimator.ofFloat(PhoneNumberConfirmView.this.fabContainer, View.TRANSLATION_Z, 0.0f, AndroidUtilities.dp(2.0f)).setDuration(150L).start();
                    }
                    PhoneNumberConfirmView.this.fabContainer.setVisibility(0);
                }
            });
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LoginActivity.PhoneNumberConfirmView.this.lambda$dismiss$6(valueAnimator);
                }
            });
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$dismiss$6(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.blurredView.setAlpha(floatValue);
            this.dimmView.setAlpha(floatValue);
            this.fabTransform.setProgress(floatValue);
            this.popupLayout.setAlpha(floatValue);
            float f = (floatValue * 0.5f) + 0.5f;
            this.popupLayout.setScaleX(f);
            this.popupLayout.setScaleY(f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class PhoneInputData {
        private CountrySelectActivity.Country country;
        private List<String> patterns;
        private String phoneNumber;

        private PhoneInputData() {
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return ColorUtils.calculateLuminance(Theme.getColor("windowBackgroundWhite", null, true)) > 0.699999988079071d;
    }
}
