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
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
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
import androidx.core.graphics.ColorUtils;
import androidx.core.util.ObjectsCompat$$ExternalSyntheticBackport0;
import androidx.dynamicanimation.animation.DynamicAnimation;
import j$.util.Comparator$CC;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$FileLocation;
import org.telegram.tgnet.TLRPC$InputFile;
import org.telegram.tgnet.TLRPC$PasswordKdfAlgo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_account_changePhone;
import org.telegram.tgnet.TLRPC$TL_account_confirmPhone;
import org.telegram.tgnet.TLRPC$TL_account_deleteAccount;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_password;
import org.telegram.tgnet.TLRPC$TL_account_passwordInputSettings;
import org.telegram.tgnet.TLRPC$TL_account_sendChangePhoneCode;
import org.telegram.tgnet.TLRPC$TL_auth_authorization;
import org.telegram.tgnet.TLRPC$TL_auth_authorizationSignUpRequired;
import org.telegram.tgnet.TLRPC$TL_auth_cancelCode;
import org.telegram.tgnet.TLRPC$TL_auth_checkPassword;
import org.telegram.tgnet.TLRPC$TL_auth_checkRecoveryPassword;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeCall;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeFlashCall;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeMissedCall;
import org.telegram.tgnet.TLRPC$TL_auth_codeTypeSms;
import org.telegram.tgnet.TLRPC$TL_auth_loggedOut;
import org.telegram.tgnet.TLRPC$TL_auth_passwordRecovery;
import org.telegram.tgnet.TLRPC$TL_auth_recoverPassword;
import org.telegram.tgnet.TLRPC$TL_auth_requestPasswordRecovery;
import org.telegram.tgnet.TLRPC$TL_auth_resendCode;
import org.telegram.tgnet.TLRPC$TL_auth_sendCode;
import org.telegram.tgnet.TLRPC$TL_auth_sentCode;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeApp;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeCall;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeFlashCall;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeMissedCall;
import org.telegram.tgnet.TLRPC$TL_auth_sentCodeTypeSms;
import org.telegram.tgnet.TLRPC$TL_auth_signIn;
import org.telegram.tgnet.TLRPC$TL_auth_signUp;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_codeSettings;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_help_countriesList;
import org.telegram.tgnet.TLRPC$TL_help_country;
import org.telegram.tgnet.TLRPC$TL_help_getCountriesList;
import org.telegram.tgnet.TLRPC$TL_help_getNearestDc;
import org.telegram.tgnet.TLRPC$TL_help_termsOfService;
import org.telegram.tgnet.TLRPC$TL_inputCheckPasswordSRP;
import org.telegram.tgnet.TLRPC$TL_nearestDc;
import org.telegram.tgnet.TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$auth_Authorization;
import org.telegram.tgnet.TLRPC$auth_CodeType;
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
    private VerticalPositionAutoAnimator floatingAutoAnimator;
    private FrameLayout floatingButtonContainer;
    private TransformableLoginButtonView floatingButtonIcon;
    private RadialProgressView floatingProgressView;
    private View introView;
    private boolean isAnimatingIntro;
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

    /* loaded from: classes3.dex */
    public static class ProgressView extends View {
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean hasForceLightStatusBar() {
        return true;
    }

    static {
        SHOW_DELAY = SharedConfig.getDevicePerformanceClass() <= 1 ? 150 : 100;
    }

    public LoginActivity() {
        this.views = new SlideView[12];
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
        this.views = new SlideView[12];
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

    /* renamed from: org.telegram.ui.LoginActivity$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            LoginActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == 1) {
                LoginActivity.this.onDoneButtonPressed();
            } else if (i != -1 || !LoginActivity.this.onBackPressed()) {
            } else {
                LoginActivity.this.finishFragment();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:111:0x03e9, code lost:
        if (r1 != 4) goto L114;
     */
    /* JADX WARN: Removed duplicated region for block: B:108:0x03e3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x0418  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x040d A[EDGE_INSN: B:127:0x040d->B:119:0x040d ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x025f  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0265  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x026a  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0339  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x033d  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0340  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x037f  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0387  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        Bundle bundle;
        int i;
        int i2;
        SlideView[] slideViewArr;
        boolean z;
        this.actionBar.setAddToContainer(false);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        this.currentDoneType = 0;
        boolean[] zArr = this.doneButtonVisible;
        zArr[0] = true;
        zArr[1] = false;
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.sizeNotifierFrameLayout = anonymousClass2;
        anonymousClass2.setDelegate(new LoginActivity$$ExternalSyntheticLambda21(this));
        this.fragmentView = this.sizeNotifierFrameLayout;
        AnonymousClass3 anonymousClass3 = new AnonymousClass3(context);
        anonymousClass3.setFillViewport(true);
        this.sizeNotifierFrameLayout.addView(anonymousClass3, LayoutHelper.createFrame(-1, -1.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        this.keyboardLinearLayout = linearLayout;
        linearLayout.setOrientation(1);
        anonymousClass3.addView(this.keyboardLinearLayout, LayoutHelper.createScroll(-1, -2, 51));
        Space space = new Space(context);
        space.setMinimumHeight(AndroidUtilities.isTablet() ? 0 : AndroidUtilities.statusBarHeight);
        this.keyboardLinearLayout.addView(space);
        AnonymousClass4 anonymousClass4 = new AnonymousClass4(context);
        this.slideViewsContainer = anonymousClass4;
        this.keyboardLinearLayout.addView(anonymousClass4, LayoutHelper.createLinear(-1, 0, 1.0f));
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
        int i3 = 0;
        while (true) {
            SlideView[] slideViewArr2 = this.views;
            if (i3 >= slideViewArr2.length) {
                break;
            }
            slideViewArr2[i3].setVisibility(i3 == 0 ? 0 : 8);
            FrameLayout frameLayout = this.slideViewsContainer;
            SlideView slideView = this.views[i3];
            float f = 18.0f;
            float f2 = AndroidUtilities.isTablet() ? 26.0f : 18.0f;
            if (AndroidUtilities.isTablet()) {
                f = 26.0f;
            }
            frameLayout.addView(slideView, LayoutHelper.createFrame(-1, -1.0f, 17, f2, 30.0f, f, 0.0f));
            i3++;
        }
        Bundle loadCurrentState = this.activityMode == 0 ? loadCurrentState(this.newAccount) : null;
        if (loadCurrentState != null) {
            this.currentViewNum = loadCurrentState.getInt("currentViewNum", 0);
            this.syncContacts = loadCurrentState.getInt("syncContacts", 1) == 1;
            int i4 = this.currentViewNum;
            if (i4 >= 1 && i4 <= 4) {
                int i5 = loadCurrentState.getInt("open");
                if (i5 != 0 && Math.abs((System.currentTimeMillis() / 1000) - i5) >= 86400) {
                    this.currentViewNum = 0;
                    clearCurrentState();
                    bundle = null;
                }
            } else if (i4 == 6) {
                if (((LoginActivityPasswordView) this.views[6]).currentPassword == null) {
                    this.currentViewNum = 0;
                    clearCurrentState();
                    bundle = null;
                }
            } else if (i4 == 7 && ((LoginActivityRecoverView) this.views[7]).passwordString == null) {
                this.currentViewNum = 0;
                clearCurrentState();
                bundle = null;
            }
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.floatingButtonContainer = frameLayout2;
            frameLayout2.setVisibility(!this.doneButtonVisible[0] ? 0 : 8);
            i = Build.VERSION.SDK_INT;
            if (i >= 21) {
                StateListAnimator stateListAnimator = new StateListAnimator();
                stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                this.floatingButtonContainer.setStateListAnimator(stateListAnimator);
                this.floatingButtonContainer.setOutlineProvider(new AnonymousClass5(this));
            }
            this.floatingAutoAnimator = VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
            this.sizeNotifierFrameLayout.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i < 21 ? 56 : 60, i < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
            this.floatingButtonContainer.setOnClickListener(new LoginActivity$$ExternalSyntheticLambda9(this));
            this.floatingAutoAnimator.addUpdateListener(new LoginActivity$$ExternalSyntheticLambda11(this));
            ImageView imageView = new ImageView(context);
            this.backButtonView = imageView;
            imageView.setImageResource(2131165449);
            this.backButtonView.setOnClickListener(new LoginActivity$$ExternalSyntheticLambda10(this));
            this.backButtonView.setContentDescription(LocaleController.getString(2131624636));
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
            this.floatingButtonContainer.setContentDescription(LocaleController.getString("Done", 2131625525));
            this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i < 21 ? 56 : 60, i < 21 ? 56.0f : 60.0f));
            RadialProgressView radialProgressView2 = new RadialProgressView(context);
            this.floatingProgressView = radialProgressView2;
            radialProgressView2.setSize(AndroidUtilities.dp(22.0f));
            this.floatingProgressView.setAlpha(0.0f);
            this.floatingProgressView.setScaleX(0.1f);
            this.floatingProgressView.setScaleY(0.1f);
            this.floatingProgressView.setVisibility(4);
            this.floatingButtonContainer.addView(this.floatingProgressView, LayoutHelper.createFrame(-1, -1.0f));
            if (bundle != null) {
                this.restoringState = true;
            }
            i2 = 0;
            while (true) {
                slideViewArr = this.views;
                if (i2 < slideViewArr.length) {
                    break;
                }
                SlideView slideView2 = slideViewArr[i2];
                if (bundle != null) {
                    if (i2 >= 1 && i2 <= 4) {
                        if (i2 == this.currentViewNum) {
                            slideView2.restoreStateParams(bundle);
                        }
                    } else {
                        slideView2.restoreStateParams(bundle);
                    }
                }
                if (this.currentViewNum == i2) {
                    this.backButtonView.setVisibility((slideView2.needBackButton() || this.newAccount || this.activityMode == 2) ? 0 : 8);
                    slideView2.setVisibility(0);
                    slideView2.onShow();
                    setCustomKeyboardVisible(slideView2.hasCustomKeyboard(), false);
                    this.currentDoneType = 0;
                    if (i2 != 0 && i2 != 5) {
                        if (i2 != 6) {
                            if (i2 != 9 && i2 != 10) {
                                z = false;
                                showDoneButton(z, false);
                                if (i2 == 1 && i2 != 2) {
                                    if (i2 != 3) {
                                    }
                                }
                                this.currentDoneType = 1;
                            }
                            z = true;
                            showDoneButton(z, false);
                            if (i2 == 1) {
                            }
                            this.currentDoneType = 1;
                        }
                    }
                    z = true;
                    showDoneButton(z, false);
                    if (i2 == 1) {
                    }
                    this.currentDoneType = 1;
                } else if (slideView2.getVisibility() != 8) {
                    slideView2.setVisibility(8);
                    slideView2.onHide();
                }
                i2++;
            }
            this.restoringState = false;
            updateColors();
            if (isInCancelAccountDeletionMode()) {
                fillNextCodeParams(this.cancelDeletionParams, this.cancelDeletionCode, false);
            }
            return this.fragmentView;
        }
        bundle = loadCurrentState;
        FrameLayout frameLayout22 = new FrameLayout(context);
        this.floatingButtonContainer = frameLayout22;
        frameLayout22.setVisibility(!this.doneButtonVisible[0] ? 0 : 8);
        i = Build.VERSION.SDK_INT;
        if (i >= 21) {
        }
        this.floatingAutoAnimator = VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
        this.sizeNotifierFrameLayout.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i < 21 ? 56 : 60, i < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
        this.floatingButtonContainer.setOnClickListener(new LoginActivity$$ExternalSyntheticLambda9(this));
        this.floatingAutoAnimator.addUpdateListener(new LoginActivity$$ExternalSyntheticLambda11(this));
        ImageView imageView2 = new ImageView(context);
        this.backButtonView = imageView2;
        imageView2.setImageResource(2131165449);
        this.backButtonView.setOnClickListener(new LoginActivity$$ExternalSyntheticLambda10(this));
        this.backButtonView.setContentDescription(LocaleController.getString(2131624636));
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
        this.floatingButtonContainer.setContentDescription(LocaleController.getString("Done", 2131625525));
        this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i < 21 ? 56 : 60, i < 21 ? 56.0f : 60.0f));
        RadialProgressView radialProgressView22 = new RadialProgressView(context);
        this.floatingProgressView = radialProgressView22;
        radialProgressView22.setSize(AndroidUtilities.dp(22.0f));
        this.floatingProgressView.setAlpha(0.0f);
        this.floatingProgressView.setScaleX(0.1f);
        this.floatingProgressView.setScaleY(0.1f);
        this.floatingProgressView.setVisibility(4);
        this.floatingButtonContainer.addView(this.floatingProgressView, LayoutHelper.createFrame(-1, -1.0f));
        if (bundle != null) {
        }
        i2 = 0;
        while (true) {
            slideViewArr = this.views;
            if (i2 < slideViewArr.length) {
            }
            i2++;
        }
        this.restoringState = false;
        updateColors();
        if (isInCancelAccountDeletionMode()) {
        }
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.LoginActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends SizeNotifierFrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            LoginActivity.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) LoginActivity.this.floatingButtonContainer.getLayoutParams();
            int i3 = 0;
            int dp = LoginActivity.this.isCustomKeyboardVisible() ? AndroidUtilities.dp(230.0f) : 0;
            if (LoginActivity.this.isCustomKeyboardVisible() && measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
                dp -= measureKeyboardHeight();
            }
            if (Bulletin.getVisibleBulletin() != null && Bulletin.getVisibleBulletin().isShowing()) {
                super.onMeasure(i, i2);
                marginLayoutParams.bottomMargin = ((AndroidUtilities.dp(16.0f) + Bulletin.getVisibleBulletin().getLayout().getMeasuredHeight()) - AndroidUtilities.dp(10.0f)) + dp;
            } else {
                marginLayoutParams.bottomMargin = AndroidUtilities.dp(16.0f) + dp;
            }
            if (!AndroidUtilities.isTablet()) {
                i3 = AndroidUtilities.statusBarHeight;
            }
            ((ViewGroup.MarginLayoutParams) LoginActivity.this.backButtonView.getLayoutParams()).topMargin = AndroidUtilities.dp(16.0f) + i3;
            ((ViewGroup.MarginLayoutParams) LoginActivity.this.radialProgressView.getLayoutParams()).topMargin = AndroidUtilities.dp(16.0f) + i3;
            super.onMeasure(i, i2);
        }
    }

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

    /* renamed from: org.telegram.ui.LoginActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends ScrollView {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Context context) {
            super(context);
            LoginActivity.this = r1;
        }

        @Override // android.widget.ScrollView, android.view.ViewGroup, android.view.ViewParent
        public boolean requestChildRectangleOnScreen(View view, Rect rect, boolean z) {
            if (LoginActivity.this.currentViewNum == 1 || LoginActivity.this.currentViewNum == 2 || LoginActivity.this.currentViewNum == 4) {
                rect.bottom += AndroidUtilities.dp(40.0f);
            }
            return super.requestChildRectangleOnScreen(view, rect, z);
        }
    }

    /* renamed from: org.telegram.ui.LoginActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends FrameLayout {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass4(Context context) {
            super(context);
            LoginActivity.this = r1;
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            SlideView[] slideViewArr;
            super.onLayout(z, i, i2, i3, i4);
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
        protected void onMeasure(int i, int i2) {
            SlideView[] slideViewArr;
            super.onMeasure(i, i2);
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
    }

    /* renamed from: org.telegram.ui.LoginActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 extends ViewOutlineProvider {
        AnonymousClass5(LoginActivity loginActivity) {
        }

        @Override // android.view.ViewOutlineProvider
        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
    }

    public /* synthetic */ void lambda$createView$1(View view) {
        onDoneButtonPressed();
    }

    public /* synthetic */ void lambda$createView$2(DynamicAnimation dynamicAnimation, float f, float f2) {
        PhoneNumberConfirmView phoneNumberConfirmView = this.phoneNumberConfirmView;
        if (phoneNumberConfirmView != null) {
            phoneNumberConfirmView.updateFabPosition();
        }
    }

    public /* synthetic */ void lambda$createView$3(View view) {
        if (onBackPressed()) {
            finishFragment();
        }
    }

    public boolean isCustomKeyboardForceDisabled() {
        Point point = AndroidUtilities.displaySize;
        return point.x > point.y || AndroidUtilities.isTablet() || AndroidUtilities.isAccessibilityTouchExplorationEnabled();
    }

    public boolean isCustomKeyboardVisible() {
        return this.views[this.currentViewNum].hasCustomKeyboard() && !isCustomKeyboardForceDisabled();
    }

    private void setCustomKeyboardVisible(boolean z, boolean z2) {
        if (this.customKeyboardWasVisible != z || !z2) {
            this.customKeyboardWasVisible = z;
            if (isCustomKeyboardForceDisabled()) {
                z = false;
            }
            if (z) {
                AndroidUtilities.hideKeyboard(this.fragmentView);
                AndroidUtilities.requestAltFocusable(getParentActivity(), this.classGuid);
                if (z2) {
                    ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(300L);
                    duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
                    duration.addUpdateListener(new LoginActivity$$ExternalSyntheticLambda1(this));
                    duration.addListener(new AnonymousClass6());
                    duration.start();
                    return;
                }
                this.keyboardView.setVisibility(0);
                return;
            }
            AndroidUtilities.removeAltFocusable(getParentActivity(), this.classGuid);
            if (z2) {
                ValueAnimator duration2 = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(300L);
                duration2.setInterpolator(Easings.easeInOutQuad);
                duration2.addUpdateListener(new LoginActivity$$ExternalSyntheticLambda2(this));
                duration2.addListener(new AnonymousClass7());
                duration2.start();
                return;
            }
            this.keyboardView.setVisibility(8);
        }
    }

    public /* synthetic */ void lambda$setCustomKeyboardVisible$4(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.keyboardView.setAlpha(floatValue);
        this.keyboardView.setTranslationY((1.0f - floatValue) * AndroidUtilities.dp(230.0f));
    }

    /* renamed from: org.telegram.ui.LoginActivity$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 extends AnimatorListenerAdapter {
        AnonymousClass6() {
            LoginActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            LoginActivity.this.keyboardView.setVisibility(0);
        }
    }

    public /* synthetic */ void lambda$setCustomKeyboardVisible$5(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.keyboardView.setAlpha(floatValue);
        this.keyboardView.setTranslationY((1.0f - floatValue) * AndroidUtilities.dp(230.0f));
    }

    /* renamed from: org.telegram.ui.LoginActivity$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends AnimatorListenerAdapter {
        AnonymousClass7() {
            LoginActivity.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            LoginActivity.this.keyboardView.setVisibility(8);
        }
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
        this.fragmentView.requestLayout();
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
            if (i2 != 0) {
                return;
            }
            ((PhoneView) this.views[i2]).confirmedNumber = true;
            this.views[this.currentViewNum].onNextPressed(null);
        } else if (i == 7) {
            this.checkShowPermissions = false;
            int i3 = this.currentViewNum;
            if (i3 != 0) {
                return;
            }
            ((PhoneView) this.views[i3]).fillNumber();
        } else if (i == 20) {
            if (!z) {
                return;
            }
            ((LoginActivityRegisterView) this.views[5]).imageUpdater.openCamera();
        } else if (i != 151 || !z) {
        } else {
            LoginActivityRegisterView loginActivityRegisterView = (LoginActivityRegisterView) this.views[5];
            loginActivityRegisterView.post(new LoginActivity$$ExternalSyntheticLambda14(loginActivityRegisterView));
        }
    }

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
            } else if (obj instanceof Bundle) {
                putBundleToEditor((Bundle) obj, editor, str2);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onDialogDismiss(Dialog dialog) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                if (dialog == this.permissionsDialog && !this.permissionsItems.isEmpty() && getParentActivity() != null) {
                    getParentActivity().requestPermissions((String[]) this.permissionsItems.toArray(new String[0]), 6);
                } else if (dialog != this.permissionsShowDialog || this.permissionsShowItems.isEmpty() || getParentActivity() == null) {
                } else {
                    AndroidUtilities.runOnUIThread(new LoginActivity$$ExternalSyntheticLambda15(this), 200L);
                    getParentActivity().requestPermissions((String[]) this.permissionsShowItems.toArray(new String[0]), 7);
                }
            } catch (Exception unused) {
            }
        }
    }

    public /* synthetic */ void lambda$onDialogDismiss$7() {
        this.needRequestPermissions = false;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        int i = this.currentViewNum;
        int i2 = 0;
        if (i != 0) {
            if (i == 6) {
                this.views[i].onBackPressed(true);
                setPage(0, true, null, true);
            } else if (i == 7 || i == 8) {
                this.views[i].onBackPressed(true);
                setPage(6, true, null, true);
            } else if ((i < 1 || i > 4) && i != 11) {
                if (i == 5) {
                    ((LoginActivityRegisterView) this.views[i]).wrongNumber.callOnClick();
                } else if (i == 9) {
                    this.views[i].onBackPressed(true);
                    setPage(7, true, null, true);
                } else if (i == 10) {
                    this.views[i].onBackPressed(true);
                    setPage(9, true, null, true);
                }
            } else if (this.views[i].onBackPressed(false)) {
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

    public void needShowAlert(String str, String str2) {
        if (str2 == null || getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
        showDialog(builder.create());
    }

    public void onFieldError(View view, boolean z) {
        view.performHapticFeedback(3, 2);
        AndroidUtilities.shakeViewSpring(view, 3.5f);
        if (!z || !(view instanceof OutlineTextContainerView)) {
            return;
        }
        Runnable runnable = (Runnable) view.getTag(2131230946);
        if (runnable != null) {
            view.removeCallbacks(runnable);
        }
        OutlineTextContainerView outlineTextContainerView = (OutlineTextContainerView) view;
        AtomicReference atomicReference = new AtomicReference();
        EditText attachedEditText = outlineTextContainerView.getAttachedEditText();
        AnonymousClass8 anonymousClass8 = new AnonymousClass8(this, attachedEditText, atomicReference);
        outlineTextContainerView.animateError(1.0f);
        LoginActivity$$ExternalSyntheticLambda13 loginActivity$$ExternalSyntheticLambda13 = new LoginActivity$$ExternalSyntheticLambda13(outlineTextContainerView, view, attachedEditText, anonymousClass8);
        atomicReference.set(loginActivity$$ExternalSyntheticLambda13);
        view.postDelayed(loginActivity$$ExternalSyntheticLambda13, 2000L);
        view.setTag(2131230946, loginActivity$$ExternalSyntheticLambda13);
        if (attachedEditText == null) {
            return;
        }
        attachedEditText.addTextChangedListener(anonymousClass8);
    }

    /* renamed from: org.telegram.ui.LoginActivity$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 implements TextWatcher {
        final /* synthetic */ EditText val$editText;
        final /* synthetic */ AtomicReference val$timeoutCallbackRef;

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass8(LoginActivity loginActivity, EditText editText, AtomicReference atomicReference) {
            this.val$editText = editText;
            this.val$timeoutCallbackRef = atomicReference;
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            EditText editText = this.val$editText;
            editText.post(new LoginActivity$8$$ExternalSyntheticLambda0(this, editText, this.val$timeoutCallbackRef));
        }

        public /* synthetic */ void lambda$beforeTextChanged$0(EditText editText, AtomicReference atomicReference) {
            editText.removeTextChangedListener(this);
            editText.removeCallbacks((Runnable) atomicReference.get());
            ((Runnable) atomicReference.get()).run();
        }
    }

    public static /* synthetic */ void lambda$onFieldError$9(OutlineTextContainerView outlineTextContainerView, View view, EditText editText, TextWatcher textWatcher) {
        outlineTextContainerView.animateError(0.0f);
        view.setTag(2131230946, null);
        if (editText != null) {
            editText.post(new LoginActivity$$ExternalSyntheticLambda12(editText, textWatcher));
        }
    }

    public static void needShowInvalidAlert(BaseFragment baseFragment, String str, boolean z) {
        needShowInvalidAlert(baseFragment, str, null, z);
    }

    public static void needShowInvalidAlert(BaseFragment baseFragment, String str, PhoneInputData phoneInputData, boolean z) {
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(baseFragment.getParentActivity());
        if (z) {
            builder.setTitle(LocaleController.getString(2131628020));
            builder.setMessage(LocaleController.getString("BannedPhoneNumber", 2131624665));
        } else if (phoneInputData == null || phoneInputData.patterns == null || phoneInputData.patterns.isEmpty() || phoneInputData.country == null) {
            builder.setTitle(LocaleController.getString(2131628020));
            builder.setMessage(LocaleController.getString(2131626250));
        } else {
            int i = Integer.MAX_VALUE;
            for (String str2 : phoneInputData.patterns) {
                int length = str2.replace(" ", "").length();
                if (length < i) {
                    i = length;
                }
            }
            if (PhoneFormat.stripExceptNumbers(str).length() - phoneInputData.country.code.length() < i) {
                builder.setTitle(LocaleController.getString(2131629256));
                builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("ShortNumberInfo", 2131628329, phoneInputData.country.name, phoneInputData.phoneNumber)));
            } else {
                builder.setTitle(LocaleController.getString(2131628020));
                builder.setMessage(LocaleController.getString(2131626250));
            }
        }
        builder.setNeutralButton(LocaleController.getString("BotHelp", 2131624715), new LoginActivity$$ExternalSyntheticLambda8(z, str, baseFragment));
        builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
        baseFragment.showDialog(builder.create());
    }

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
            builder.setTitle(LocaleController.getString(2131628020));
            builder.setMessage(LocaleController.getString("NoMailInstalled", 2131626830));
            builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
            baseFragment.showDialog(builder.create());
        }
    }

    public void showDoneButton(boolean z, boolean z2) {
        TimeInterpolator timeInterpolator;
        int i = this.currentDoneType;
        boolean z3 = i == 0;
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
                if (!z3) {
                    return;
                }
                this.floatingButtonContainer.setVisibility(0);
                this.floatingAutoAnimator.setOffsetY(0.0f);
                return;
            } else if (!z3) {
                return;
            } else {
                this.floatingButtonContainer.setVisibility(8);
                this.floatingAutoAnimator.setOffsetY(AndroidUtilities.dpf2(70.0f));
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
                ofFloat.addUpdateListener(new LoginActivity$$ExternalSyntheticLambda0(this));
                this.showDoneAnimation[this.currentDoneType].play(ofFloat);
            }
        } else if (z3) {
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(this.floatingAutoAnimator.getOffsetY(), AndroidUtilities.dpf2(70.0f));
            ofFloat2.addUpdateListener(new LoginActivity$$ExternalSyntheticLambda3(this));
            this.showDoneAnimation[this.currentDoneType].play(ofFloat2);
        }
        this.showDoneAnimation[this.currentDoneType].addListener(new AnonymousClass9(z3, z));
        int i3 = 150;
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

    public /* synthetic */ void lambda$showDoneButton$11(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.floatingAutoAnimator.setOffsetY(floatValue);
        this.floatingButtonContainer.setAlpha(1.0f - (floatValue / AndroidUtilities.dpf2(70.0f)));
    }

    public /* synthetic */ void lambda$showDoneButton$12(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.floatingAutoAnimator.setOffsetY(floatValue);
        this.floatingButtonContainer.setAlpha(1.0f - (floatValue / AndroidUtilities.dpf2(70.0f)));
    }

    /* renamed from: org.telegram.ui.LoginActivity$9 */
    /* loaded from: classes3.dex */
    public class AnonymousClass9 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$floating;
        final /* synthetic */ boolean val$show;

        AnonymousClass9(boolean z, boolean z2) {
            LoginActivity.this = r1;
            this.val$floating = z;
            this.val$show = z2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (LoginActivity.this.showDoneAnimation[!this.val$floating ? 1 : 0] == null || !LoginActivity.this.showDoneAnimation[!this.val$floating ? 1 : 0].equals(animator) || this.val$show) {
                return;
            }
            if (this.val$floating) {
                LoginActivity.this.floatingButtonContainer.setVisibility(8);
            }
            if (!this.val$floating || LoginActivity.this.floatingButtonIcon.getAlpha() == 1.0f) {
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
            if (LoginActivity.this.showDoneAnimation[!this.val$floating ? 1 : 0] == null || !LoginActivity.this.showDoneAnimation[!this.val$floating ? 1 : 0].equals(animator)) {
                return;
            }
            LoginActivity.this.showDoneAnimation[!this.val$floating ? 1 : 0] = null;
        }
    }

    public void onDoneButtonPressed() {
        if (!this.doneButtonVisible[this.currentDoneType]) {
            return;
        }
        if (this.radialProgressView.getTag() != null) {
            if (getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString(2131624375));
            builder.setMessage(LocaleController.getString("StopLoading", 2131628471));
            builder.setPositiveButton(LocaleController.getString("WaitMore", 2131629220), null);
            builder.setNegativeButton(LocaleController.getString("Stop", 2131628463), new LoginActivity$$ExternalSyntheticLambda6(this));
            showDialog(builder.create());
            return;
        }
        this.views[this.currentViewNum].onNextPressed(null);
    }

    public /* synthetic */ void lambda$onDoneButtonPressed$13(DialogInterface dialogInterface, int i) {
        this.views[this.currentViewNum].onCancelPressed();
        needHideProgress(true);
    }

    private void showEditDoneProgress(boolean z, boolean z2) {
        showEditDoneProgress(z, z2, false);
    }

    private void showEditDoneProgress(boolean z, boolean z2, boolean z3) {
        if (!z2 || this.doneProgressVisible[this.currentDoneType] != z || z3) {
            int i = this.currentDoneType;
            boolean z4 = i == 0;
            if (!z3 && !z4) {
                this.doneProgressVisible[i] = z;
                if (z2) {
                    if (this.postedEditDoneCallback[i]) {
                        AndroidUtilities.cancelRunOnUIThread(this.editDoneCallback[i]);
                        this.postedEditDoneCallback[this.currentDoneType] = false;
                        return;
                    } else if (z) {
                        Runnable[] runnableArr = this.editDoneCallback;
                        LoginActivity$$ExternalSyntheticLambda16 loginActivity$$ExternalSyntheticLambda16 = new LoginActivity$$ExternalSyntheticLambda16(this, i, z, z2);
                        runnableArr[i] = loginActivity$$ExternalSyntheticLambda16;
                        AndroidUtilities.runOnUIThread(loginActivity$$ExternalSyntheticLambda16, 2000L);
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
            float f = 0.0f;
            if (z2) {
                this.doneItemAnimation = new AnimatorSet();
                float[] fArr = new float[2];
                fArr[0] = z ? 0.0f : 1.0f;
                if (z) {
                    f = 1.0f;
                }
                fArr[1] = f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                ofFloat.addListener(new AnonymousClass10(z, z4));
                ofFloat.addUpdateListener(new LoginActivity$$ExternalSyntheticLambda5(this, z4));
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
    }

    public /* synthetic */ void lambda$showEditDoneProgress$14(int i, boolean z, boolean z2) {
        int i2 = this.currentDoneType;
        this.currentDoneType = i;
        showEditDoneProgress(z, z2, true);
        this.currentDoneType = i2;
    }

    /* renamed from: org.telegram.ui.LoginActivity$10 */
    /* loaded from: classes3.dex */
    public class AnonymousClass10 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$floating;
        final /* synthetic */ boolean val$show;

        AnonymousClass10(boolean z, boolean z2) {
            LoginActivity.this = r1;
            this.val$show = z;
            this.val$floating = z2;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (this.val$show) {
                if (this.val$floating) {
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
            if (this.val$floating) {
                if (!this.val$show) {
                    LoginActivity.this.floatingProgressView.setVisibility(4);
                    LoginActivity.this.floatingButtonIcon.setVisibility(0);
                    LoginActivity.this.floatingButtonContainer.setEnabled(true);
                } else {
                    LoginActivity.this.floatingButtonIcon.setVisibility(4);
                    LoginActivity.this.floatingProgressView.setVisibility(0);
                }
            } else if (!this.val$show) {
                LoginActivity.this.radialProgressView.setVisibility(4);
            }
            if (LoginActivity.this.doneItemAnimation == null || !LoginActivity.this.doneItemAnimation.equals(animator)) {
                return;
            }
            LoginActivity.this.doneItemAnimation = null;
        }
    }

    public /* synthetic */ void lambda$showEditDoneProgress$15(boolean z, ValueAnimator valueAnimator) {
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

    public void needShowProgress(int i) {
        needShowProgress(i, true);
    }

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

    public void needHideProgress(boolean z) {
        needHideProgress(z, true);
    }

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
        boolean z3 = i == 0 || i == 5 || i == 6 || i == 9 || i == 10;
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
            SlideView slideView = slideViewArr[this.currentViewNum];
            SlideView slideView2 = slideViewArr[i];
            this.currentViewNum = i;
            ImageView imageView = this.backButtonView;
            if (slideView2.needBackButton() || this.newAccount) {
                i2 = 0;
            }
            imageView.setVisibility(i2);
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
            animatorSet.addListener(new AnonymousClass11(z3, slideView));
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
        this.currentViewNum = i;
        this.views[i].setParams(bundle, false);
        this.views[i].setVisibility(0);
        setParentActivityTitle(this.views[i].getHeaderName());
        this.views[i].onShow();
        setCustomKeyboardVisible(this.views[i].hasCustomKeyboard(), false);
    }

    /* renamed from: org.telegram.ui.LoginActivity$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$needFloatingButton;
        final /* synthetic */ SlideView val$outView;

        AnonymousClass11(boolean z, SlideView slideView) {
            LoginActivity.this = r1;
            this.val$needFloatingButton = z;
            this.val$outView = slideView;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (LoginActivity.this.currentDoneType == 0 && this.val$needFloatingButton) {
                LoginActivity.this.showDoneButton(true, true);
            }
            this.val$outView.setVisibility(8);
            this.val$outView.setX(0.0f);
        }
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

    private void needFinishActivity(boolean z, boolean z2, int i) {
        if (getParentActivity() != null) {
            AndroidUtilities.setLightStatusBar(getParentActivity().getWindow(), false);
        }
        clearCurrentState();
        if (getParentActivity() instanceof LaunchActivity) {
            if (this.newAccount) {
                this.newAccount = false;
                ((LaunchActivity) getParentActivity()).switchToAccount(this.currentAccount, false, new LoginActivity$$ExternalSyntheticLambda18(z));
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
        } else if (!(getParentActivity() instanceof ExternalActionActivity)) {
        } else {
            ((ExternalActionActivity) getParentActivity()).onFinishLogin();
        }
    }

    public static /* synthetic */ DialogsActivity lambda$needFinishActivity$16(boolean z, Void r2) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("afterSignup", z);
        return new DialogsActivity(bundle);
    }

    public void onAuthSuccess(TLRPC$TL_auth_authorization tLRPC$TL_auth_authorization) {
        onAuthSuccess(tLRPC$TL_auth_authorization, false);
    }

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
        if (z) {
            MessagesController.getInstance(this.currentAccount).putDialogsEndReachedAfterRegistration();
        }
        MediaDataController.getInstance(this.currentAccount).loadStickersByEmojiOrName("tg_placeholders_android", false, true);
        needFinishActivity(z, tLRPC$TL_auth_authorization.setup_password_required, tLRPC$TL_auth_authorization.otherwise_relogin_days);
    }

    public void fillNextCodeParams(Bundle bundle, TLRPC$TL_auth_sentCode tLRPC$TL_auth_sentCode) {
        fillNextCodeParams(bundle, tLRPC$TL_auth_sentCode, true);
    }

    private void fillNextCodeParams(Bundle bundle, TLRPC$TL_auth_sentCode tLRPC$TL_auth_sentCode, boolean z) {
        bundle.putString("phoneHash", tLRPC$TL_auth_sentCode.phone_code_hash);
        TLRPC$auth_CodeType tLRPC$auth_CodeType = tLRPC$TL_auth_sentCode.next_type;
        if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeCall) {
            bundle.putInt("nextType", 4);
        } else if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeFlashCall) {
            bundle.putInt("nextType", 3);
        } else if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeSms) {
            bundle.putInt("nextType", 2);
        } else if (tLRPC$auth_CodeType instanceof TLRPC$TL_auth_codeTypeMissedCall) {
            bundle.putInt("nextType", 11);
        }
        if (tLRPC$TL_auth_sentCode.type instanceof TLRPC$TL_auth_sentCodeTypeApp) {
            bundle.putInt("type", 1);
            bundle.putInt("length", tLRPC$TL_auth_sentCode.type.length);
            setPage(1, z, bundle, false);
            return;
        }
        if (tLRPC$TL_auth_sentCode.timeout == 0) {
            tLRPC$TL_auth_sentCode.timeout = 60;
        }
        bundle.putInt("timeout", tLRPC$TL_auth_sentCode.timeout * 1000);
        TLRPC$auth_SentCodeType tLRPC$auth_SentCodeType = tLRPC$TL_auth_sentCode.type;
        if (tLRPC$auth_SentCodeType instanceof TLRPC$TL_auth_sentCodeTypeCall) {
            bundle.putInt("type", 4);
            bundle.putInt("length", tLRPC$TL_auth_sentCode.type.length);
            setPage(4, z, bundle, false);
        } else if (tLRPC$auth_SentCodeType instanceof TLRPC$TL_auth_sentCodeTypeFlashCall) {
            bundle.putInt("type", 3);
            bundle.putString("pattern", tLRPC$TL_auth_sentCode.type.pattern);
            setPage(3, z, bundle, false);
        } else if (tLRPC$auth_SentCodeType instanceof TLRPC$TL_auth_sentCodeTypeSms) {
            bundle.putInt("type", 2);
            bundle.putInt("length", tLRPC$TL_auth_sentCode.type.length);
            setPage(2, z, bundle, false);
        } else if (!(tLRPC$auth_SentCodeType instanceof TLRPC$TL_auth_sentCodeTypeMissedCall)) {
        } else {
            bundle.putInt("type", 11);
            bundle.putInt("length", tLRPC$TL_auth_sentCode.type.length);
            bundle.putString("prefix", tLRPC$TL_auth_sentCode.type.prefix);
            setPage(11, z, bundle, false);
        }
    }

    /* loaded from: classes3.dex */
    public class PhoneView extends SlideView implements AdapterView.OnItemSelectedListener, NotificationCenter.NotificationCenterDelegate {
        private ImageView chevronRight;
        private View codeDividerView;
        private AnimatedPhoneNumberEditText codeField;
        private TextViewSwitcher countryButton;
        private OutlineTextContainerView countryOutlineView;
        private int countryState;
        private CountrySelectActivity.Country currentCountry;
        private boolean numberFilled;
        private AnimatedPhoneNumberEditText phoneField;
        private OutlineTextContainerView phoneOutlineView;
        private TextView plusTextView;
        private TextView subtitleView;
        private CheckBoxCell syncContactsBox;
        private CheckBoxCell testBackendCheckBox;
        private TextView titleView;
        private ArrayList<CountrySelectActivity.Country> countriesArray = new ArrayList<>();
        private HashMap<String, CountrySelectActivity.Country> codesMap = new HashMap<>();
        private HashMap<String, List<String>> phoneFormatMap = new HashMap<>();
        private boolean ignoreSelection = false;
        private boolean ignoreOnTextChange = false;
        private boolean ignoreOnPhoneChange = false;
        private boolean nextPressed = false;
        private boolean confirmedNumber = false;

        @Override // org.telegram.ui.Components.SlideView
        public boolean hasCustomKeyboard() {
            return true;
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PhoneView(Context context) {
            super(context);
            int i;
            LoginActivity.this = r27;
            this.countryState = 0;
            setOrientation(1);
            setGravity(17);
            TextView textView = new TextView(context);
            this.titleView = textView;
            textView.setTextSize(1, 18.0f);
            this.titleView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.titleView.setText(LocaleController.getString(r27.activityMode == 2 ? 2131624858 : 2131629292));
            this.titleView.setGravity(17);
            this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 0.0f, 32.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.subtitleView = textView2;
            textView2.setText(LocaleController.getString(r27.activityMode == 2 ? 2131624857 : 2131628421));
            this.subtitleView.setTextSize(1, 14.0f);
            this.subtitleView.setGravity(17);
            this.subtitleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.subtitleView, LayoutHelper.createLinear(-1, -2, 1, 32, 8, 32, 0));
            TextViewSwitcher textViewSwitcher = new TextViewSwitcher(context);
            this.countryButton = textViewSwitcher;
            textViewSwitcher.setFactory(new LoginActivity$PhoneView$$ExternalSyntheticLambda7(context));
            Animation loadAnimation = AnimationUtils.loadAnimation(context, 2130771992);
            loadAnimation.setInterpolator(Easings.easeInOutQuad);
            this.countryButton.setInAnimation(loadAnimation);
            ImageView imageView = new ImageView(context);
            this.chevronRight = imageView;
            imageView.setImageResource(2131165764);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(0);
            linearLayout.setGravity(16);
            linearLayout.addView(this.countryButton, LayoutHelper.createLinear(0, -2, 1.0f, 0, 0, 0, 0));
            linearLayout.addView(this.chevronRight, LayoutHelper.createLinearRelatively(24.0f, 24.0f, 0, 0.0f, 0.0f, 14.0f, 0.0f));
            OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
            this.countryOutlineView = outlineTextContainerView;
            outlineTextContainerView.setText(LocaleController.getString(2131625267));
            this.countryOutlineView.addView(linearLayout, LayoutHelper.createFrame(-1, -1.0f, 48, 0.0f, 0.0f, 0.0f, 0.0f));
            this.countryOutlineView.setForceUseCenter(true);
            this.countryOutlineView.setFocusable(true);
            this.countryOutlineView.setContentDescription(LocaleController.getString(2131625267));
            this.countryOutlineView.setOnFocusChangeListener(new LoginActivity$PhoneView$$ExternalSyntheticLambda4(this));
            addView(this.countryOutlineView, LayoutHelper.createLinear(-1, 58, 16.0f, 24.0f, 16.0f, 14.0f));
            this.countryOutlineView.setOnClickListener(new LoginActivity$PhoneView$$ExternalSyntheticLambda2(this));
            LinearLayout linearLayout2 = new LinearLayout(context);
            linearLayout2.setOrientation(0);
            OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
            this.phoneOutlineView = outlineTextContainerView2;
            outlineTextContainerView2.addView(linearLayout2, LayoutHelper.createFrame(-1, -2.0f, 16, 16.0f, 8.0f, 16.0f, 8.0f));
            this.phoneOutlineView.setText(LocaleController.getString(2131627493));
            addView(this.phoneOutlineView, LayoutHelper.createLinear(-1, 58, 16.0f, 8.0f, 16.0f, 8.0f));
            TextView textView3 = new TextView(context);
            this.plusTextView = textView3;
            textView3.setText("+");
            this.plusTextView.setTextSize(1, 16.0f);
            this.plusTextView.setFocusable(false);
            linearLayout2.addView(this.plusTextView, LayoutHelper.createLinear(-2, -2));
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(context, r27);
            this.codeField = anonymousClass1;
            anonymousClass1.setInputType(3);
            this.codeField.setCursorSize(AndroidUtilities.dp(20.0f));
            this.codeField.setCursorWidth(1.5f);
            this.codeField.setPadding(AndroidUtilities.dp(10.0f), 0, 0, 0);
            this.codeField.setTextSize(1, 16.0f);
            this.codeField.setMaxLines(1);
            this.codeField.setGravity(19);
            this.codeField.setImeOptions(268435461);
            this.codeField.setBackground(null);
            int i2 = Build.VERSION.SDK_INT;
            if (i2 >= 21) {
                this.codeField.setShowSoftInputOnFocus(!hasCustomKeyboard() || r27.isCustomKeyboardForceDisabled());
            }
            this.codeField.setContentDescription(LocaleController.getString(2131626501));
            linearLayout2.addView(this.codeField, LayoutHelper.createLinear(55, 36, -9.0f, 0.0f, 0.0f, 0.0f));
            this.codeField.addTextChangedListener(new AnonymousClass2(r27));
            this.codeField.setOnEditorActionListener(new LoginActivity$PhoneView$$ExternalSyntheticLambda5(this));
            this.codeDividerView = new View(context);
            LinearLayout.LayoutParams createLinear = LayoutHelper.createLinear(0, -1, 4.0f, 8.0f, 12.0f, 8.0f);
            createLinear.width = Math.max(2, AndroidUtilities.dp(0.5f));
            linearLayout2.addView(this.codeDividerView, createLinear);
            AnonymousClass3 anonymousClass3 = new AnonymousClass3(context, r27);
            this.phoneField = anonymousClass3;
            anonymousClass3.setInputType(3);
            this.phoneField.setPadding(0, 0, 0, 0);
            this.phoneField.setCursorSize(AndroidUtilities.dp(20.0f));
            this.phoneField.setCursorWidth(1.5f);
            this.phoneField.setTextSize(1, 16.0f);
            this.phoneField.setMaxLines(1);
            this.phoneField.setGravity(19);
            this.phoneField.setImeOptions(268435461);
            this.phoneField.setBackground(null);
            if (i2 >= 21) {
                this.phoneField.setShowSoftInputOnFocus(!hasCustomKeyboard() || r27.isCustomKeyboardForceDisabled());
            }
            this.phoneField.setContentDescription(LocaleController.getString(2131627493));
            linearLayout2.addView(this.phoneField, LayoutHelper.createFrame(-1, 36.0f));
            this.phoneField.addTextChangedListener(new AnonymousClass4(r27));
            this.phoneField.setOnEditorActionListener(new LoginActivity$PhoneView$$ExternalSyntheticLambda6(this));
            int i3 = 72;
            int i4 = 56;
            if (r27.newAccount && r27.activityMode == 0) {
                CheckBoxCell checkBoxCell = new CheckBoxCell(context, 2);
                this.syncContactsBox = checkBoxCell;
                checkBoxCell.setText(LocaleController.getString("SyncContacts", 2131628525), "", r27.syncContacts, false);
                addView(this.syncContactsBox, LayoutHelper.createLinear(-2, -1, 51, 16, 0, 16 + ((!LocaleController.isRTL || !AndroidUtilities.isSmallScreen()) ? 0 : i2 >= 21 ? 56 : 60), 0));
                i3 = 48;
                this.syncContactsBox.setOnClickListener(new LoginActivity$PhoneView$$ExternalSyntheticLambda3(this));
            }
            if (BuildVars.DEBUG_PRIVATE_VERSION && r27.activityMode == 0) {
                CheckBoxCell checkBoxCell2 = new CheckBoxCell(context, 2);
                this.testBackendCheckBox = checkBoxCell2;
                checkBoxCell2.setText("Test Backend", "", r27.testBackend, false);
                View view = this.testBackendCheckBox;
                if (!LocaleController.isRTL || !AndroidUtilities.isSmallScreen()) {
                    i = 16;
                    i4 = 0;
                } else if (i2 >= 21) {
                    i = 16;
                } else {
                    i = 16;
                    i4 = 60;
                }
                addView(view, LayoutHelper.createLinear(-2, -1, 51, 16, 0, i + i4, 0));
                i3 -= 24;
                this.testBackendCheckBox.setOnClickListener(new LoginActivity$PhoneView$$ExternalSyntheticLambda1(this));
            }
            if (i3 > 0 && !AndroidUtilities.isSmallScreen()) {
                Space space = new Space(context);
                space.setMinimumHeight(AndroidUtilities.dp(i3));
                addView(space, LayoutHelper.createLinear(-2, -2));
            }
            HashMap hashMap = new HashMap();
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
                    this.codesMap.put(split[0], country);
                    if (split.length > 3) {
                        this.phoneFormatMap.put(split[0], Collections.singletonList(split[3]));
                    }
                    hashMap.put(split[1], split[2]);
                }
                bufferedReader.close();
            } catch (Exception e) {
                FileLog.e(e);
            }
            Collections.sort(this.countriesArray, Comparator$CC.comparing(LoginActivity$PhoneView$$ExternalSyntheticLambda17.INSTANCE));
            try {
                TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
            } catch (Exception e2) {
                FileLog.e(e2);
            }
            r27.getAccountInstance().getConnectionsManager().sendRequest(new TLRPC$TL_help_getNearestDc(), new LoginActivity$PhoneView$$ExternalSyntheticLambda21(this, hashMap), 10);
            if (this.codeField.length() == 0) {
                setCountryButtonText(null);
                this.phoneField.setHintText((String) null);
                this.countryState = 1;
            }
            if (this.codeField.length() != 0) {
                this.phoneField.requestFocus();
                AnimatedPhoneNumberEditText animatedPhoneNumberEditText = this.phoneField;
                animatedPhoneNumberEditText.setSelection(animatedPhoneNumberEditText.length());
            } else {
                this.codeField.requestFocus();
            }
            TLRPC$TL_help_getCountriesList tLRPC$TL_help_getCountriesList = new TLRPC$TL_help_getCountriesList();
            tLRPC$TL_help_getCountriesList.lang_code = "";
            r27.getConnectionsManager().sendRequest(tLRPC$TL_help_getCountriesList, new LoginActivity$PhoneView$$ExternalSyntheticLambda18(this), 10);
        }

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

        public /* synthetic */ void lambda$new$1(View view, boolean z) {
            this.countryOutlineView.animateSelection(z ? 1.0f : 0.0f);
        }

        public /* synthetic */ void lambda$new$4(View view) {
            CountrySelectActivity countrySelectActivity = new CountrySelectActivity(true, this.countriesArray);
            countrySelectActivity.setCountrySelectActivityDelegate(new LoginActivity$PhoneView$$ExternalSyntheticLambda22(this));
            LoginActivity.this.presentFragment(countrySelectActivity);
        }

        public /* synthetic */ void lambda$new$3(CountrySelectActivity.Country country) {
            selectCountry(country);
            AndroidUtilities.runOnUIThread(new LoginActivity$PhoneView$$ExternalSyntheticLambda8(this), 300L);
            this.phoneField.requestFocus();
            AnimatedPhoneNumberEditText animatedPhoneNumberEditText = this.phoneField;
            animatedPhoneNumberEditText.setSelection(animatedPhoneNumberEditText.length());
        }

        public /* synthetic */ void lambda$new$2() {
            LoginActivity.this.showKeyboard(this.phoneField);
        }

        /* renamed from: org.telegram.ui.LoginActivity$PhoneView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends AnimatedPhoneNumberEditText {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, LoginActivity loginActivity) {
                super(context);
                PhoneView.this = r1;
            }

            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public void onFocusChanged(boolean z, int i, Rect rect) {
                super.onFocusChanged(z, i, rect);
                PhoneView.this.phoneOutlineView.animateSelection((z || PhoneView.this.phoneField.isFocused()) ? 1.0f : 0.0f);
                if (z) {
                    LoginActivity.this.keyboardView.setEditText(this);
                }
            }
        }

        /* renamed from: org.telegram.ui.LoginActivity$PhoneView$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 implements TextWatcher {
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass2(LoginActivity loginActivity) {
                PhoneView.this = r1;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                boolean z;
                String str;
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
                    int i = 4;
                    if (stripExceptNumbers.length() > 4) {
                        while (true) {
                            if (i < 1) {
                                str = null;
                                z = false;
                                break;
                            }
                            String substring = stripExceptNumbers.substring(0, i);
                            if (((CountrySelectActivity.Country) PhoneView.this.codesMap.get(substring)) != null) {
                                String str2 = stripExceptNumbers.substring(i) + PhoneView.this.phoneField.getText().toString();
                                PhoneView.this.codeField.setText(substring);
                                z = true;
                                str = str2;
                                stripExceptNumbers = substring;
                                break;
                            }
                            i--;
                        }
                        if (!z) {
                            str = stripExceptNumbers.substring(1) + PhoneView.this.phoneField.getText().toString();
                            AnimatedPhoneNumberEditText animatedPhoneNumberEditText = PhoneView.this.codeField;
                            stripExceptNumbers = stripExceptNumbers.substring(0, 1);
                            animatedPhoneNumberEditText.setText(stripExceptNumbers);
                        }
                    } else {
                        str = null;
                        z = false;
                    }
                    CountrySelectActivity.Country country = null;
                    int i2 = 0;
                    for (CountrySelectActivity.Country country2 : PhoneView.this.codesMap.values()) {
                        if (country2.code.startsWith(stripExceptNumbers)) {
                            i2++;
                            if (country2.code.equals(stripExceptNumbers)) {
                                country = country2;
                            }
                        }
                    }
                    if (i2 == 1 && country != null && str == null) {
                        str = stripExceptNumbers.substring(country.code.length()) + PhoneView.this.phoneField.getText().toString();
                        AnimatedPhoneNumberEditText animatedPhoneNumberEditText2 = PhoneView.this.codeField;
                        String str3 = country.code;
                        animatedPhoneNumberEditText2.setText(str3);
                        stripExceptNumbers = str3;
                    }
                    CountrySelectActivity.Country country3 = (CountrySelectActivity.Country) PhoneView.this.codesMap.get(stripExceptNumbers);
                    if (country3 != null) {
                        PhoneView.this.ignoreSelection = true;
                        PhoneView.this.currentCountry = country3;
                        PhoneView.this.setCountryHint(stripExceptNumbers, country3);
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
        }

        public /* synthetic */ boolean lambda$new$5(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 5) {
                this.phoneField.requestFocus();
                AnimatedPhoneNumberEditText animatedPhoneNumberEditText = this.phoneField;
                animatedPhoneNumberEditText.setSelection(animatedPhoneNumberEditText.length());
                return true;
            }
            return false;
        }

        /* renamed from: org.telegram.ui.LoginActivity$PhoneView$3 */
        /* loaded from: classes3.dex */
        public class AnonymousClass3 extends AnimatedPhoneNumberEditText {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass3(Context context, LoginActivity loginActivity) {
                super(context);
                PhoneView.this = r1;
            }

            @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
            public boolean onKeyDown(int i, KeyEvent keyEvent) {
                if (i == 67 && PhoneView.this.phoneField.length() == 0) {
                    PhoneView.this.codeField.requestFocus();
                    PhoneView.this.codeField.setSelection(PhoneView.this.codeField.length());
                    PhoneView.this.codeField.dispatchKeyEvent(keyEvent);
                }
                return super.onKeyDown(i, keyEvent);
            }

            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && !LoginActivity.this.showKeyboard(this)) {
                    clearFocus();
                    requestFocus();
                }
                return super.onTouchEvent(motionEvent);
            }

            @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public void onFocusChanged(boolean z, int i, Rect rect) {
                super.onFocusChanged(z, i, rect);
                PhoneView.this.phoneOutlineView.animateSelection((z || PhoneView.this.codeField.isFocused()) ? 1.0f : 0.0f);
                if (z) {
                    LoginActivity.this.keyboardView.setEditText(this);
                    LoginActivity.this.keyboardView.setDispatchBackWhenEmpty(true);
                    if (PhoneView.this.countryState != 2) {
                        return;
                    }
                    PhoneView.this.setCountryButtonText(LocaleController.getString(2131629254));
                } else if (PhoneView.this.countryState != 2) {
                } else {
                    PhoneView.this.setCountryButtonText(null);
                }
            }
        }

        /* renamed from: org.telegram.ui.LoginActivity$PhoneView$4 */
        /* loaded from: classes3.dex */
        public class AnonymousClass4 implements TextWatcher {
            private int actionPosition;
            private int characterAction = -1;

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass4(LoginActivity loginActivity) {
                PhoneView.this = r1;
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (i2 == 0 && i3 == 1) {
                    this.characterAction = 1;
                } else if (i2 == 1 && i3 == 0) {
                    if (charSequence.charAt(i) == ' ' && i > 0) {
                        this.characterAction = 3;
                        this.actionPosition = i - 1;
                        return;
                    }
                    this.characterAction = 2;
                } else {
                    this.characterAction = -1;
                }
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                int i;
                int i2;
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
                int i3 = 0;
                while (i3 < obj.length()) {
                    int i4 = i3 + 1;
                    String substring = obj.substring(i3, i4);
                    if ("0123456789".contains(substring)) {
                        sb.append(substring);
                    }
                    i3 = i4;
                }
                PhoneView.this.ignoreOnPhoneChange = true;
                String hintText = PhoneView.this.phoneField.getHintText();
                if (hintText != null) {
                    int i5 = 0;
                    while (true) {
                        if (i5 >= sb.length()) {
                            break;
                        } else if (i5 < hintText.length()) {
                            if (hintText.charAt(i5) == ' ') {
                                sb.insert(i5, ' ');
                                i5++;
                                if (selectionStart == i5 && (i2 = this.characterAction) != 2 && i2 != 3) {
                                    selectionStart++;
                                }
                            }
                            i5++;
                        } else {
                            sb.insert(i5, ' ');
                            if (selectionStart == i5 + 1 && (i = this.characterAction) != 2 && i != 3) {
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
                PhoneView.this.ignoreOnPhoneChange = false;
            }
        }

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

        public /* synthetic */ void lambda$new$7(View view) {
            if (LoginActivity.this.getParentActivity() == null) {
                return;
            }
            LoginActivity loginActivity = LoginActivity.this;
            loginActivity.syncContacts = !loginActivity.syncContacts;
            ((CheckBoxCell) view).setChecked(LoginActivity.this.syncContacts, true);
            if (LoginActivity.this.syncContacts) {
                BulletinFactory.of(LoginActivity.this.slideViewsContainer, null).createSimpleBulletin(2131558432, LocaleController.getString("SyncContactsOn", 2131628533)).show();
            } else {
                BulletinFactory.of(LoginActivity.this.slideViewsContainer, null).createSimpleBulletin(2131558431, LocaleController.getString("SyncContactsOff", 2131628532)).show();
            }
        }

        public /* synthetic */ void lambda$new$8(View view) {
            if (LoginActivity.this.getParentActivity() == null) {
                return;
            }
            LoginActivity loginActivity = LoginActivity.this;
            loginActivity.testBackend = !loginActivity.testBackend;
            ((CheckBoxCell) view).setChecked(LoginActivity.this.testBackend, true);
        }

        public /* synthetic */ void lambda$new$11(HashMap hashMap, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$PhoneView$$ExternalSyntheticLambda13(this, tLObject, hashMap));
        }

        public /* synthetic */ void lambda$new$10(TLObject tLObject, HashMap hashMap) {
            if (tLObject == null) {
                return;
            }
            TLRPC$TL_nearestDc tLRPC$TL_nearestDc = (TLRPC$TL_nearestDc) tLObject;
            if (this.codeField.length() != 0) {
                return;
            }
            setCountry(hashMap, tLRPC$TL_nearestDc.country.toUpperCase());
        }

        public /* synthetic */ void lambda$new$13(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$PhoneView$$ExternalSyntheticLambda15(this, tLRPC$TL_error, tLObject));
        }

        public /* synthetic */ void lambda$new$12(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            boolean z;
            if (tLRPC$TL_error == null) {
                this.countriesArray.clear();
                this.codesMap.clear();
                this.phoneFormatMap.clear();
                TLRPC$TL_help_countriesList tLRPC$TL_help_countriesList = (TLRPC$TL_help_countriesList) tLObject;
                for (int i = 0; i < tLRPC$TL_help_countriesList.countries.size(); i++) {
                    TLRPC$TL_help_country tLRPC$TL_help_country = tLRPC$TL_help_countriesList.countries.get(i);
                    for (int i2 = 0; i2 < tLRPC$TL_help_country.country_codes.size(); i2++) {
                        CountrySelectActivity.Country country = new CountrySelectActivity.Country();
                        country.name = tLRPC$TL_help_country.default_name;
                        country.code = tLRPC$TL_help_country.country_codes.get(i2).country_code;
                        country.shortname = tLRPC$TL_help_country.iso2;
                        this.countriesArray.add(country);
                        this.codesMap.put(tLRPC$TL_help_country.country_codes.get(i2).country_code, country);
                        if (tLRPC$TL_help_country.country_codes.get(i2).patterns.size() > 0) {
                            this.phoneFormatMap.put(tLRPC$TL_help_country.country_codes.get(i2).country_code, tLRPC$TL_help_country.country_codes.get(i2).patterns);
                        }
                    }
                }
                if (LoginActivity.this.activityMode != 2) {
                    return;
                }
                String stripExceptNumbers = PhoneFormat.stripExceptNumbers(UserConfig.getInstance(((BaseFragment) LoginActivity.this).currentAccount).getClientPhone());
                if (TextUtils.isEmpty(stripExceptNumbers)) {
                    return;
                }
                int i3 = 4;
                if (stripExceptNumbers.length() <= 4) {
                    return;
                }
                while (true) {
                    if (i3 < 1) {
                        z = false;
                        break;
                    }
                    String substring = stripExceptNumbers.substring(0, i3);
                    if (this.codesMap.get(substring) != null) {
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
        }

        public void setCountryHint(String str, CountrySelectActivity.Country country) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            String languageFlag = LocaleController.getLanguageFlag(country.shortname);
            if (languageFlag != null) {
                spannableStringBuilder.append((CharSequence) languageFlag).append((CharSequence) " ");
                spannableStringBuilder.setSpan(new AnonymousClass5(this), languageFlag.length(), languageFlag.length() + 1, 0);
            }
            spannableStringBuilder.append((CharSequence) country.name);
            setCountryButtonText(Emoji.replaceEmoji(spannableStringBuilder, this.countryButton.getCurrentView().getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false));
            String str2 = null;
            if (this.phoneFormatMap.get(str) != null && !this.phoneFormatMap.get(str).isEmpty()) {
                String str3 = this.phoneFormatMap.get(str).get(0);
                AnimatedPhoneNumberEditText animatedPhoneNumberEditText = this.phoneField;
                if (str3 != null) {
                    str2 = str3.replace('X', '0');
                }
                animatedPhoneNumberEditText.setHintText(str2);
                return;
            }
            this.phoneField.setHintText((String) null);
        }

        /* renamed from: org.telegram.ui.LoginActivity$PhoneView$5 */
        /* loaded from: classes3.dex */
        public class AnonymousClass5 extends ReplacementSpan {
            @Override // android.text.style.ReplacementSpan
            public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
            }

            AnonymousClass5(PhoneView phoneView) {
            }

            @Override // android.text.style.ReplacementSpan
            public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
                return AndroidUtilities.dp(16.0f);
            }
        }

        public void setCountryButtonText(CharSequence charSequence) {
            Animation loadAnimation = AnimationUtils.loadAnimation(ApplicationLoader.applicationContext, (this.countryButton.getCurrentView().getText() == null || charSequence != null) ? 2130771993 : 2130771994);
            loadAnimation.setInterpolator(Easings.easeInOutQuad);
            this.countryButton.setOutAnimation(loadAnimation);
            CharSequence text = this.countryButton.getCurrentView().getText();
            this.countryButton.setText(charSequence, (!TextUtils.isEmpty(charSequence) || !TextUtils.isEmpty(text)) && !ObjectsCompat$$ExternalSyntheticBackport0.m(text, charSequence));
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
            if (country == null) {
                return;
            }
            this.codeField.setText(country.code);
            this.countryState = 0;
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
        /* JADX WARN: Removed duplicated region for block: B:173:0x0434  */
        /* JADX WARN: Removed duplicated region for block: B:174:0x043e  */
        @Override // org.telegram.ui.Components.SlideView
        /* renamed from: onNextPressed */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void lambda$onNextPressed$14(String str) {
            String str2;
            boolean z;
            boolean z2;
            boolean z3;
            int i;
            boolean z4;
            Exception e;
            String line1Number;
            TLRPC$TL_auth_sendCode tLRPC$TL_auth_sendCode;
            Bundle bundle;
            String str3;
            Exception e2;
            boolean z5;
            if (LoginActivity.this.getParentActivity() == null || this.nextPressed) {
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
                    LoginActivity.this.phoneNumberConfirmView = new PhoneNumberConfirmView(((BaseFragment) LoginActivity.this).fragmentView.getContext(), (ViewGroup) ((BaseFragment) LoginActivity.this).fragmentView, LoginActivity.this.floatingButtonContainer, str4, new AnonymousClass6(str), null);
                    LoginActivity.this.phoneNumberConfirmView.show();
                    return;
                }
                LoginActivity.this.keyboardHideCallback = new LoginActivity$PhoneView$$ExternalSyntheticLambda11(this, str);
                AndroidUtilities.hideKeyboard(((BaseFragment) LoginActivity.this).fragmentView);
                return;
            }
            this.confirmedNumber = false;
            if (LoginActivity.this.phoneNumberConfirmView != null) {
                LoginActivity.this.phoneNumberConfirmView.dismiss();
            }
            boolean isSimAvailable = AndroidUtilities.isSimAvailable();
            int i2 = Build.VERSION.SDK_INT;
            if (i2 < 23 || !isSimAvailable) {
                str2 = "ephone";
                i = 1;
                z3 = true;
                z2 = true;
                z = true;
            } else {
                z3 = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0;
                z2 = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.CALL_PHONE") == 0;
                z = i2 < 28 || LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_CALL_LOG") == 0;
                if (i2 >= 26) {
                    z5 = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_NUMBERS") == 0;
                    str2 = "ephone";
                } else {
                    str2 = "ephone";
                    z5 = true;
                }
                if (LoginActivity.this.checkPermissions) {
                    LoginActivity.this.permissionsItems.clear();
                    if (!z3) {
                        LoginActivity.this.permissionsItems.add("android.permission.READ_PHONE_STATE");
                    }
                    if (!z2) {
                        LoginActivity.this.permissionsItems.add("android.permission.CALL_PHONE");
                    }
                    if (!z) {
                        LoginActivity.this.permissionsItems.add("android.permission.READ_CALL_LOG");
                    }
                    if (!z5 && i2 >= 26) {
                        LoginActivity.this.permissionsItems.add("android.permission.READ_PHONE_NUMBERS");
                    }
                    if (!LoginActivity.this.permissionsItems.isEmpty()) {
                        SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
                        if (!globalMainSettings.getBoolean("firstlogin", true) && !LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE") && !LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_CALL_LOG")) {
                            try {
                                LoginActivity.this.getParentActivity().requestPermissions((String[]) LoginActivity.this.permissionsItems.toArray(new String[0]), 6);
                                return;
                            } catch (Exception e3) {
                                FileLog.e(e3);
                                return;
                            }
                        }
                        globalMainSettings.edit().putBoolean("firstlogin", false).commit();
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                        builder.setPositiveButton(LocaleController.getString("Continue", 2131625246), null);
                        int i3 = 2131558412;
                        if (!z3 && (!z2 || !z)) {
                            builder.setMessage(LocaleController.getString("AllowReadCallAndLog", 2131624330));
                        } else if (!z2 || !z) {
                            builder.setMessage(LocaleController.getString("AllowReadCallLog", 2131624331));
                        } else {
                            builder.setMessage(LocaleController.getString("AllowReadCall", 2131624329));
                            i3 = 2131558477;
                        }
                        builder.setTopAnimation(i3, 46, false, Theme.getColor("dialogTopBackground"));
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
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("ChooseCountry", 2131625104));
                LoginActivity.this.needHideProgress(false);
            } else if (i4 == 2 && !BuildVars.DEBUG_VERSION) {
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("WrongCountry", 2131629254));
                LoginActivity.this.needHideProgress(false);
            } else {
                String stripExceptNumbers = PhoneFormat.stripExceptNumbers("" + ((Object) this.codeField.getText()) + ((Object) this.phoneField.getText()));
                if (LoginActivity.this.activityMode == 0) {
                    boolean z6 = BuildVars.DEBUG_PRIVATE_VERSION && LoginActivity.this.getConnectionsManager().isTestBackend();
                    if (z6 != LoginActivity.this.testBackend) {
                        LoginActivity.this.getConnectionsManager().switchBackend(false);
                        z6 = LoginActivity.this.testBackend;
                    }
                    if (LoginActivity.this.getParentActivity() instanceof LaunchActivity) {
                        for (int i5 = 0; i5 < 4; i5++) {
                            UserConfig userConfig = UserConfig.getInstance(i5);
                            if (userConfig.isClientActivated() && PhoneNumberUtils.compare(stripExceptNumbers, userConfig.getCurrentUser().phone) && ConnectionsManager.getInstance(i5).isTestBackend() == z6) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                                builder2.setTitle(LocaleController.getString(2131624375));
                                builder2.setMessage(LocaleController.getString("AccountAlreadyLoggedIn", 2131624128));
                                builder2.setPositiveButton(LocaleController.getString("AccountSwitch", 2131624130), new LoginActivity$PhoneView$$ExternalSyntheticLambda0(this, i5));
                                builder2.setNegativeButton(LocaleController.getString("OK", 2131627075), null);
                                LoginActivity.this.showDialog(builder2.create());
                                LoginActivity.this.needHideProgress(false);
                                return;
                            }
                        }
                    }
                }
                TLRPC$TL_codeSettings tLRPC$TL_codeSettings = new TLRPC$TL_codeSettings();
                tLRPC$TL_codeSettings.allow_flashcall = isSimAvailable && z3 && z2 && z;
                tLRPC$TL_codeSettings.allow_missed_call = isSimAvailable && z3;
                tLRPC$TL_codeSettings.allow_app_hash = ApplicationLoader.hasPlayServices;
                ArrayList<TLRPC$TL_auth_loggedOut> savedLogOutTokens = MessagesController.getSavedLogOutTokens();
                if (savedLogOutTokens != null) {
                    for (int i6 = 0; i6 < savedLogOutTokens.size(); i6++) {
                        if (tLRPC$TL_codeSettings.logout_tokens == null) {
                            tLRPC$TL_codeSettings.logout_tokens = new ArrayList<>();
                        }
                        tLRPC$TL_codeSettings.logout_tokens.add(savedLogOutTokens.get(i6).future_auth_token);
                    }
                    MessagesController.saveLogOutTokens(savedLogOutTokens);
                }
                if (tLRPC$TL_codeSettings.logout_tokens != null) {
                    tLRPC$TL_codeSettings.flags |= 64;
                }
                SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                if (tLRPC$TL_codeSettings.allow_app_hash) {
                    sharedPreferences.edit().putString("sms_hash", BuildVars.SMS_HASH).apply();
                } else {
                    sharedPreferences.edit().remove("sms_hash").apply();
                }
                if (tLRPC$TL_codeSettings.allow_flashcall) {
                    try {
                        line1Number = telephonyManager.getLine1Number();
                        z4 = TextUtils.isEmpty(line1Number);
                    } catch (Exception e4) {
                        e = e4;
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
                    } catch (Exception e5) {
                        e = e5;
                        tLRPC$TL_codeSettings.allow_flashcall = z4;
                        FileLog.e(e);
                        if (LoginActivity.this.activityMode == 2) {
                        }
                        TLRPC$TL_auth_sendCode tLRPC$TL_auth_sendCode2 = tLRPC$TL_auth_sendCode;
                        bundle = new Bundle();
                        bundle.putString("phone", "+" + ((Object) this.codeField.getText()) + " " + ((Object) this.phoneField.getText()));
                        str3 = str2;
                        bundle.putString(str3, "+" + PhoneFormat.stripExceptNumbers(this.codeField.getText().toString()) + " " + PhoneFormat.stripExceptNumbers(this.phoneField.getText().toString()));
                        bundle.putString("phoneFormated", stripExceptNumbers);
                        this.nextPressed = true;
                        PhoneInputData phoneInputData = new PhoneInputData(null);
                        phoneInputData.phoneNumber = "+" + ((Object) this.codeField.getText()) + " " + ((Object) this.phoneField.getText());
                        phoneInputData.country = this.currentCountry;
                        phoneInputData.patterns = this.phoneFormatMap.get(this.codeField.getText().toString());
                        LoginActivity.this.needShowProgress(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_sendCode2, new LoginActivity$PhoneView$$ExternalSyntheticLambda19(this, bundle, stripExceptNumbers, phoneInputData, tLRPC$TL_auth_sendCode2), 27));
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
                TLRPC$TL_auth_sendCode tLRPC$TL_auth_sendCode22 = tLRPC$TL_auth_sendCode;
                bundle = new Bundle();
                bundle.putString("phone", "+" + ((Object) this.codeField.getText()) + " " + ((Object) this.phoneField.getText()));
                try {
                    str3 = str2;
                } catch (Exception e6) {
                    e2 = e6;
                    str3 = str2;
                }
                try {
                    bundle.putString(str3, "+" + PhoneFormat.stripExceptNumbers(this.codeField.getText().toString()) + " " + PhoneFormat.stripExceptNumbers(this.phoneField.getText().toString()));
                } catch (Exception e7) {
                    e2 = e7;
                    FileLog.e(e2);
                    bundle.putString(str3, "+" + stripExceptNumbers);
                    bundle.putString("phoneFormated", stripExceptNumbers);
                    this.nextPressed = true;
                    PhoneInputData phoneInputData2 = new PhoneInputData(null);
                    phoneInputData2.phoneNumber = "+" + ((Object) this.codeField.getText()) + " " + ((Object) this.phoneField.getText());
                    phoneInputData2.country = this.currentCountry;
                    phoneInputData2.patterns = this.phoneFormatMap.get(this.codeField.getText().toString());
                    LoginActivity.this.needShowProgress(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_sendCode22, new LoginActivity$PhoneView$$ExternalSyntheticLambda19(this, bundle, stripExceptNumbers, phoneInputData2, tLRPC$TL_auth_sendCode22), 27));
                }
                bundle.putString("phoneFormated", stripExceptNumbers);
                this.nextPressed = true;
                PhoneInputData phoneInputData22 = new PhoneInputData(null);
                phoneInputData22.phoneNumber = "+" + ((Object) this.codeField.getText()) + " " + ((Object) this.phoneField.getText());
                phoneInputData22.country = this.currentCountry;
                phoneInputData22.patterns = this.phoneFormatMap.get(this.codeField.getText().toString());
                LoginActivity.this.needShowProgress(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_sendCode22, new LoginActivity$PhoneView$$ExternalSyntheticLambda19(this, bundle, stripExceptNumbers, phoneInputData22, tLRPC$TL_auth_sendCode22), 27));
            }
        }

        public /* synthetic */ void lambda$onNextPressed$15(String str) {
            postDelayed(new LoginActivity$PhoneView$$ExternalSyntheticLambda10(this, str), 200L);
        }

        /* renamed from: org.telegram.ui.LoginActivity$PhoneView$6 */
        /* loaded from: classes3.dex */
        public class AnonymousClass6 implements PhoneNumberConfirmView.IConfirmDialogCallback {
            final /* synthetic */ String val$code;

            AnonymousClass6(String str) {
                PhoneView.this = r1;
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

            private void onConfirm(PhoneNumberConfirmView phoneNumberConfirmView) {
                PhoneView.this.confirmedNumber = true;
                LoginActivity.this.currentDoneType = 0;
                LoginActivity.this.needShowProgress(0, false);
                int i = Build.VERSION.SDK_INT;
                if (i >= 23 && AndroidUtilities.isSimAvailable()) {
                    boolean z = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0;
                    boolean z2 = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.CALL_PHONE") == 0;
                    boolean z3 = i < 28 || LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_CALL_LOG") == 0;
                    boolean z4 = i < 26 || LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_NUMBERS") == 0;
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
                        if (!z4 && i >= 26) {
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
                            builder.setPositiveButton(LocaleController.getString("Continue", 2131625246), null);
                            int i2 = 2131558412;
                            if (!z && (!z2 || !z3)) {
                                builder.setMessage(LocaleController.getString("AllowReadCallAndLog", 2131624330));
                            } else if (!z2 || !z3) {
                                builder.setMessage(LocaleController.getString("AllowReadCallLog", 2131624331));
                            } else {
                                builder.setMessage(LocaleController.getString("AllowReadCall", 2131624329));
                                i2 = 2131558477;
                            }
                            builder.setTopAnimation(i2, 46, false, Theme.getColor("dialogTopBackground"));
                            LoginActivity loginActivity = LoginActivity.this;
                            loginActivity.permissionsDialog = loginActivity.showDialog(builder.create());
                            PhoneView.this.confirmedNumber = true;
                            return;
                        }
                    }
                }
                phoneNumberConfirmView.animateProgress(new LoginActivity$PhoneView$6$$ExternalSyntheticLambda1(this, phoneNumberConfirmView, this.val$code));
            }

            public /* synthetic */ void lambda$onConfirm$1(PhoneNumberConfirmView phoneNumberConfirmView, String str) {
                phoneNumberConfirmView.dismiss();
                AndroidUtilities.runOnUIThread(new LoginActivity$PhoneView$6$$ExternalSyntheticLambda0(this, str, phoneNumberConfirmView), 150L);
            }

            public /* synthetic */ void lambda$onConfirm$0(String str, PhoneNumberConfirmView phoneNumberConfirmView) {
                PhoneView.this.lambda$onNextPressed$14(str);
                LoginActivity.this.floatingProgressView.sync(phoneNumberConfirmView.floatingProgressView);
            }
        }

        public /* synthetic */ void lambda$onNextPressed$16(int i, DialogInterface dialogInterface, int i2) {
            if (UserConfig.selectedAccount != i) {
                ((LaunchActivity) LoginActivity.this.getParentActivity()).switchToAccount(i, false);
            }
            LoginActivity.this.finishFragment();
        }

        public /* synthetic */ void lambda$onNextPressed$20(Bundle bundle, String str, PhoneInputData phoneInputData, TLObject tLObject, TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$PhoneView$$ExternalSyntheticLambda14(this, tLRPC$TL_error, bundle, tLObject2, str, phoneInputData, tLObject));
        }

        public /* synthetic */ void lambda$onNextPressed$19(TLRPC$TL_error tLRPC$TL_error, Bundle bundle, TLObject tLObject, String str, PhoneInputData phoneInputData, TLObject tLObject2) {
            this.nextPressed = false;
            if (tLRPC$TL_error == null) {
                LoginActivity.this.fillNextCodeParams(bundle, (TLRPC$TL_auth_sentCode) tLObject);
            } else {
                String str2 = tLRPC$TL_error.text;
                if (str2 != null) {
                    if (str2.contains("SESSION_PASSWORD_NEEDED")) {
                        ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new LoginActivity$PhoneView$$ExternalSyntheticLambda20(this, str), 10);
                    } else if (tLRPC$TL_error.text.contains("PHONE_NUMBER_INVALID")) {
                        LoginActivity.needShowInvalidAlert(LoginActivity.this, str, phoneInputData, false);
                    } else if (tLRPC$TL_error.text.contains("PHONE_PASSWORD_FLOOD")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("FloodWait", 2131625908));
                    } else if (tLRPC$TL_error.text.contains("PHONE_NUMBER_FLOOD")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("PhoneNumberFlood", 2131627497));
                    } else if (tLRPC$TL_error.text.contains("PHONE_NUMBER_BANNED")) {
                        LoginActivity.needShowInvalidAlert(LoginActivity.this, str, phoneInputData, true);
                    } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("InvalidCode", 2131626246));
                    } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("CodeExpired", 2131625171));
                    } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("FloodWait", 2131625908));
                    } else if (tLRPC$TL_error.code != -1000) {
                        AlertsCreator.processError(((BaseFragment) LoginActivity.this).currentAccount, tLRPC$TL_error, LoginActivity.this, tLObject2, phoneInputData.phoneNumber);
                    }
                }
            }
            LoginActivity.this.needHideProgress(false);
        }

        public /* synthetic */ void lambda$onNextPressed$18(String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$PhoneView$$ExternalSyntheticLambda16(this, tLRPC$TL_error, tLObject, str));
        }

        public /* synthetic */ void lambda$onNextPressed$17(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, String str) {
            this.nextPressed = false;
            LoginActivity.this.showDoneButton(false, true);
            if (tLRPC$TL_error != null) {
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), tLRPC$TL_error.text);
                return;
            }
            TLRPC$TL_account_password tLRPC$TL_account_password = (TLRPC$TL_account_password) tLObject;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$TL_account_password, true)) {
                AlertsCreator.showUpdateAppAlert(LoginActivity.this.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131628758), true);
                return;
            }
            Bundle bundle = new Bundle();
            SerializedData serializedData = new SerializedData(tLRPC$TL_account_password.getObjectSize());
            tLRPC$TL_account_password.serializeToStream(serializedData);
            bundle.putString("password", Utilities.bytesToHex(serializedData.toByteArray()));
            bundle.putString("phoneFormated", str);
            LoginActivity.this.setPage(6, true, bundle, false);
        }

        /* JADX WARN: Removed duplicated region for block: B:27:0x0064 A[Catch: Exception -> 0x0164, TryCatch #0 {Exception -> 0x0164, blocks: (B:6:0x0010, B:8:0x0020, B:10:0x0028, B:16:0x003d, B:21:0x004d, B:25:0x0059, B:27:0x0064, B:30:0x0071, B:31:0x007a, B:33:0x0086, B:35:0x009e, B:36:0x00a4, B:39:0x00aa, B:43:0x00b8, B:45:0x00d2, B:48:0x00db, B:50:0x00e9, B:51:0x00f4, B:54:0x00fa, B:56:0x0109, B:57:0x011c, B:59:0x0126, B:60:0x0159), top: B:64:0x0010 }] */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0086 A[Catch: Exception -> 0x0164, TryCatch #0 {Exception -> 0x0164, blocks: (B:6:0x0010, B:8:0x0020, B:10:0x0028, B:16:0x003d, B:21:0x004d, B:25:0x0059, B:27:0x0064, B:30:0x0071, B:31:0x007a, B:33:0x0086, B:35:0x009e, B:36:0x00a4, B:39:0x00aa, B:43:0x00b8, B:45:0x00d2, B:48:0x00db, B:50:0x00e9, B:51:0x00f4, B:54:0x00fa, B:56:0x0109, B:57:0x011c, B:59:0x0126, B:60:0x0159), top: B:64:0x0010 }] */
        /* JADX WARN: Removed duplicated region for block: B:67:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void fillNumber() {
            boolean z;
            boolean z2;
            boolean z3;
            if (this.numberFilled || LoginActivity.this.activityMode != 0) {
                return;
            }
            try {
                TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
                if (!AndroidUtilities.isSimAvailable()) {
                    return;
                }
                int i = Build.VERSION.SDK_INT;
                if (i >= 23) {
                    z2 = LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_STATE") == 0;
                    if (i >= 26 && LoginActivity.this.getParentActivity().checkSelfPermission("android.permission.READ_PHONE_NUMBERS") != 0) {
                        z = false;
                        if (LoginActivity.this.checkShowPermissions && (!z2 || !z)) {
                            LoginActivity.this.permissionsShowItems.clear();
                            if (!z2) {
                                LoginActivity.this.permissionsShowItems.add("android.permission.READ_PHONE_STATE");
                            }
                            if (!z && i >= 26) {
                                LoginActivity.this.permissionsShowItems.add("android.permission.READ_PHONE_NUMBERS");
                            }
                            if (!LoginActivity.this.permissionsShowItems.isEmpty()) {
                                return;
                            }
                            LoginActivity$PhoneView$$ExternalSyntheticLambda12 loginActivity$PhoneView$$ExternalSyntheticLambda12 = new LoginActivity$PhoneView$$ExternalSyntheticLambda12(this, new ArrayList(LoginActivity.this.permissionsShowItems));
                            if (LoginActivity.this.isAnimatingIntro) {
                                LoginActivity.this.animationFinishCallback = loginActivity$PhoneView$$ExternalSyntheticLambda12;
                                return;
                            } else {
                                loginActivity$PhoneView$$ExternalSyntheticLambda12.run();
                                return;
                            }
                        }
                    }
                    z = true;
                    if (LoginActivity.this.checkShowPermissions) {
                        LoginActivity.this.permissionsShowItems.clear();
                        if (!z2) {
                        }
                        if (!z) {
                            LoginActivity.this.permissionsShowItems.add("android.permission.READ_PHONE_NUMBERS");
                        }
                        if (!LoginActivity.this.permissionsShowItems.isEmpty()) {
                        }
                    }
                } else {
                    z2 = true;
                    z = true;
                }
                this.numberFilled = true;
                if (LoginActivity.this.newAccount || !z2 || !z) {
                    return;
                }
                this.codeField.setAlpha(0.0f);
                this.phoneField.setAlpha(0.0f);
                String stripExceptNumbers = PhoneFormat.stripExceptNumbers(telephonyManager.getLine1Number());
                String str = null;
                if (!TextUtils.isEmpty(stripExceptNumbers)) {
                    int i2 = 4;
                    if (stripExceptNumbers.length() > 4) {
                        while (true) {
                            if (i2 < 1) {
                                z3 = false;
                                break;
                            }
                            String substring = stripExceptNumbers.substring(0, i2);
                            if (this.codesMap.get(substring) != null) {
                                str = stripExceptNumbers.substring(i2);
                                this.codeField.setText(substring);
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
            } catch (Exception e) {
                FileLog.e(e);
            }
        }

        public /* synthetic */ void lambda$fillNumber$21(List list) {
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            if (globalMainSettings.getBoolean("firstloginshow", true) || LoginActivity.this.getParentActivity().shouldShowRequestPermissionRationale("android.permission.READ_PHONE_STATE")) {
                globalMainSettings.edit().putBoolean("firstloginshow", false).commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                builder.setTopAnimation(2131558477, 46, false, Theme.getColor("dialogTopBackground"));
                builder.setPositiveButton(LocaleController.getString("Continue", 2131625246), null);
                builder.setMessage(LocaleController.getString("AllowFillNumber", 2131624328));
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
            AndroidUtilities.runOnUIThread(new LoginActivity$PhoneView$$ExternalSyntheticLambda9(this), LoginActivity.SHOW_DELAY);
        }

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
            return LocaleController.getString("YourPhone", 2131629302);
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
        private Timer codeTimer;
        private TextView confirmTextView;
        private Bundle currentParams;
        private int currentType;
        private RLottieDrawable dotsDrawable;
        private RLottieDrawable dotsToStarsDrawable;
        private String emailPhone;
        private ViewSwitcher errorViewSwitcher;
        RLottieDrawable hintDrawable;
        private boolean isDotsAnimationVisible;
        private double lastCodeTime;
        private double lastCurrentTime;
        private int length;
        private ImageView missedCallArrowIcon;
        private TextView missedCallDescriptionSubtitle;
        private ImageView missedCallPhoneIcon;
        private boolean nextPressed;
        private int nextType;
        private int openTime;
        private String phone;
        private String phoneHash;
        private boolean postedErrorColorTimeout;
        private TextView prefixTextView;
        private FrameLayout problemFrame;
        private TextView problemText;
        private ProgressView progressView;
        private String requestPhone;
        private RLottieDrawable starsToDotsDrawable;
        private TextView timeText;
        private Timer timeTimer;
        private TextView titleTextView;
        private boolean waitingForEvent;
        private TextView wrongCode;
        private final Object timerSync = new Object();
        private int time = 60000;
        private int codeTime = 15000;
        private String lastError = "";
        private String pattern = "*";
        private String prefix = "";
        private Runnable errorColorTimeout = new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda8(this);

        public static /* synthetic */ void lambda$onBackPressed$39(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        static /* synthetic */ int access$8126(LoginActivitySmsView loginActivitySmsView, double d) {
            double d2 = loginActivitySmsView.codeTime;
            Double.isNaN(d2);
            int i = (int) (d2 - d);
            loginActivitySmsView.codeTime = i;
            return i;
        }

        static /* synthetic */ int access$8726(LoginActivitySmsView loginActivitySmsView, double d) {
            double d2 = loginActivitySmsView.time;
            Double.isNaN(d2);
            int i = (int) (d2 - d);
            loginActivitySmsView.time = i;
            return i;
        }

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
            if (this.errorViewSwitcher.getCurrentView() != this.problemFrame) {
                this.errorViewSwitcher.showNext();
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Removed duplicated region for block: B:54:0x03e2  */
        /* JADX WARN: Removed duplicated region for block: B:57:0x0402  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x04f9  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x0523  */
        /* JADX WARN: Removed duplicated region for block: B:70:0x0533  */
        /* JADX WARN: Removed duplicated region for block: B:71:0x0558  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public LoginActivitySmsView(Context context, int i) {
            super(context);
            FrameLayout frameLayout;
            LoginActivity.this = r35;
            this.currentType = i;
            setOrientation(1);
            TextView textView = new TextView(context);
            this.confirmTextView = textView;
            textView.setTextSize(1, 14.0f);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            TextView textView2 = new TextView(context);
            this.titleTextView = textView2;
            textView2.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.titleTextView.setGravity(LocaleController.isRTL ? 5 : 3);
            this.titleTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.titleTextView.setGravity(49);
            String string = r35.activityMode != 1 ? null : LocaleController.getString(2131624820);
            int i2 = this.currentType;
            if (i2 == 11) {
                this.titleTextView.setText(string == null ? LocaleController.getString("MissedCallDescriptionTitle", 2131626726) : string);
                FrameLayout frameLayout2 = new FrameLayout(context);
                this.missedCallArrowIcon = new ImageView(context);
                this.missedCallPhoneIcon = new ImageView(context);
                frameLayout2.addView(this.missedCallArrowIcon);
                frameLayout2.addView(this.missedCallPhoneIcon);
                this.missedCallArrowIcon.setImageResource(2131165590);
                this.missedCallPhoneIcon.setImageResource(2131165591);
                addView(frameLayout2, LayoutHelper.createLinear(64, 64, 1, 0, 16, 0, 0));
                addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 8, 0, 0));
                TextView textView3 = new TextView(context);
                this.missedCallDescriptionSubtitle = textView3;
                textView3.setTextSize(1, 14.0f);
                this.missedCallDescriptionSubtitle.setGravity(1);
                this.missedCallDescriptionSubtitle.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                this.missedCallDescriptionSubtitle.setText(AndroidUtilities.replaceTags(LocaleController.getString("MissedCallDescriptionSubtitle", 2131626724)));
                addView(this.missedCallDescriptionSubtitle, LayoutHelper.createLinear(-1, -2, 49, 36, 16, 36, 0));
                this.codeFieldContainer = new AnonymousClass1(context, r35);
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(0);
                TextView textView4 = new TextView(context);
                this.prefixTextView = textView4;
                textView4.setTextSize(1, 20.0f);
                this.prefixTextView.setMaxLines(1);
                this.prefixTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
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
                this.missedCallDescriptionSubtitle.setText(AndroidUtilities.replaceTags(LocaleController.getString("MissedCallDescriptionSubtitle2", 2131626725)));
                addView(this.missedCallDescriptionSubtitle, LayoutHelper.createLinear(-1, -2, 49, 36, 28, 36, 12));
            } else {
                int i3 = 64;
                if (i2 == 3) {
                    this.confirmTextView.setGravity(1);
                    FrameLayout frameLayout3 = new FrameLayout(context);
                    addView(frameLayout3, LayoutHelper.createLinear(-1, 0, 1.0f));
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    linearLayout2.setOrientation(1);
                    linearLayout2.setGravity(1);
                    frameLayout3.addView(linearLayout2, LayoutHelper.createFrame(-1, -2, 17));
                    ((FrameLayout.LayoutParams) linearLayout2.getLayoutParams()).bottomMargin = AndroidUtilities.isTablet() ? 0 : AndroidUtilities.statusBarHeight;
                    FrameLayout frameLayout4 = new FrameLayout(context);
                    linearLayout2.addView(frameLayout4, LayoutHelper.createFrame(-2, -2, 1));
                    this.blueImageView = new RLottieImageView(context);
                    RLottieDrawable rLottieDrawable = new RLottieDrawable(2131558500, String.valueOf(2131558500), AndroidUtilities.dp(64.0f), AndroidUtilities.dp(64.0f), true, null);
                    this.hintDrawable = rLottieDrawable;
                    this.blueImageView.setAnimation(rLottieDrawable);
                    frameLayout4.addView(this.blueImageView, LayoutHelper.createFrame(64, 64.0f));
                    this.titleTextView.setText(string == null ? LocaleController.getString(2131629275) : string);
                    linearLayout2.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 0));
                    linearLayout2.addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 0, 8, 0, 0));
                    frameLayout = frameLayout3;
                    if (this.currentType != 11) {
                        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context, r35);
                        this.codeFieldContainer = anonymousClass2;
                        addView(anonymousClass2, LayoutHelper.createLinear(-2, 42, 1, 0, 32, 0, 0));
                    }
                    if (this.currentType == 3) {
                        this.codeFieldContainer.setVisibility(8);
                    }
                    this.problemFrame = new FrameLayout(context);
                    TextView textView6 = new TextView(context);
                    this.timeText = textView6;
                    textView6.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.timeText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
                    this.timeText.setTextSize(1, 15.0f);
                    this.timeText.setGravity(49);
                    this.timeText.setOnClickListener(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda5(this));
                    this.problemFrame.addView(this.timeText, LayoutHelper.createFrame(-2, -2, 49));
                    this.errorViewSwitcher = new AnonymousClass3(this, context, r35);
                    Animation loadAnimation = AnimationUtils.loadAnimation(context, 2130771992);
                    Interpolator interpolator = Easings.easeInOutQuad;
                    loadAnimation.setInterpolator(interpolator);
                    this.errorViewSwitcher.setInAnimation(loadAnimation);
                    Animation loadAnimation2 = AnimationUtils.loadAnimation(context, 2130771993);
                    loadAnimation2.setInterpolator(interpolator);
                    this.errorViewSwitcher.setOutAnimation(loadAnimation2);
                    TextView textView7 = new TextView(context);
                    this.problemText = textView7;
                    textView7.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.problemText.setTextSize(1, 15.0f);
                    this.problemText.setGravity(49);
                    this.problemText.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
                    this.problemFrame.addView(this.problemText, LayoutHelper.createFrame(-2, -2, 17));
                    this.errorViewSwitcher.addView(this.problemFrame);
                    TextView textView8 = new TextView(context);
                    this.wrongCode = textView8;
                    textView8.setText(LocaleController.getString("WrongCode", 2131629252));
                    this.wrongCode.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.wrongCode.setTextSize(1, 15.0f);
                    this.wrongCode.setGravity(49);
                    this.wrongCode.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
                    this.errorViewSwitcher.addView(this.wrongCode);
                    if (this.currentType != 1) {
                        int i4 = this.nextType;
                        if (i4 == 3 || i4 == 4 || i4 == 11) {
                            this.problemText.setText(LocaleController.getString("DidNotGetTheCodePhone", 2131625474));
                        } else {
                            this.problemText.setText(LocaleController.getString("DidNotGetTheCodeSms", 2131625475));
                        }
                    } else {
                        this.problemText.setText(LocaleController.getString("DidNotGetTheCode", 2131625470));
                    }
                    if (frameLayout != null) {
                        FrameLayout frameLayout5 = new FrameLayout(context);
                        this.bottomContainer = frameLayout5;
                        frameLayout5.addView(this.errorViewSwitcher, LayoutHelper.createFrame(-2, -2.0f, 81, 0.0f, 0.0f, 0.0f, 32.0f));
                        addView(this.bottomContainer, LayoutHelper.createLinear(-1, 0, 1.0f));
                    } else {
                        frameLayout.addView(this.errorViewSwitcher, LayoutHelper.createFrame(-2, -2.0f, 81, 0.0f, 0.0f, 0.0f, 32.0f));
                    }
                    VerticalPositionAutoAnimator.attach(this.errorViewSwitcher);
                    this.problemText.setOnClickListener(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda6(this, context));
                }
                this.confirmTextView.setGravity(49);
                FrameLayout frameLayout6 = new FrameLayout(context);
                addView(frameLayout6, LayoutHelper.createLinear(-2, -2, 49, 0, 16, 0, 0));
                int i5 = this.currentType;
                i3 = i5 == 1 ? 128 : i3;
                if (i5 == 1) {
                    float f = i3;
                    this.hintDrawable = new RLottieDrawable(2131558429, String.valueOf(2131558429), AndroidUtilities.dp(f), AndroidUtilities.dp(f), true, null);
                } else {
                    float f2 = i3;
                    this.hintDrawable = new RLottieDrawable(2131558530, String.valueOf(2131558530), AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), true, null);
                    this.starsToDotsDrawable = new RLottieDrawable(2131558501, String.valueOf(2131558501), AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), true, null);
                    this.dotsDrawable = new RLottieDrawable(2131558498, String.valueOf(2131558498), AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), true, null);
                    this.dotsToStarsDrawable = new RLottieDrawable(2131558499, String.valueOf(2131558499), AndroidUtilities.dp(f2), AndroidUtilities.dp(f2), true, null);
                }
                RLottieImageView rLottieImageView = new RLottieImageView(context);
                this.blueImageView = rLottieImageView;
                rLottieImageView.setAnimation(this.hintDrawable);
                if (this.currentType == 1 && !AndroidUtilities.isSmallScreen()) {
                    this.blueImageView.setTranslationY(-AndroidUtilities.dp(24.0f));
                }
                frameLayout6.addView(this.blueImageView, LayoutHelper.createFrame(i3, i3, 51, 0.0f, 0.0f, 0.0f, (this.currentType != 1 || AndroidUtilities.isSmallScreen()) ? 0.0f : -AndroidUtilities.dp(16.0f)));
                TextView textView9 = this.titleTextView;
                if (string == null) {
                    string = LocaleController.getString(this.currentType == 1 ? 2131628222 : 2131628227);
                }
                textView9.setText(string);
                addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 18, 0, 0));
                addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 49, 0, 17, 0, 0));
            }
            frameLayout = null;
            if (this.currentType != 11) {
            }
            if (this.currentType == 3) {
            }
            this.problemFrame = new FrameLayout(context);
            TextView textView62 = new TextView(context);
            this.timeText = textView62;
            textView62.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.timeText.setPadding(0, AndroidUtilities.dp(2.0f), 0, AndroidUtilities.dp(10.0f));
            this.timeText.setTextSize(1, 15.0f);
            this.timeText.setGravity(49);
            this.timeText.setOnClickListener(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda5(this));
            this.problemFrame.addView(this.timeText, LayoutHelper.createFrame(-2, -2, 49));
            this.errorViewSwitcher = new AnonymousClass3(this, context, r35);
            Animation loadAnimation3 = AnimationUtils.loadAnimation(context, 2130771992);
            Interpolator interpolator2 = Easings.easeInOutQuad;
            loadAnimation3.setInterpolator(interpolator2);
            this.errorViewSwitcher.setInAnimation(loadAnimation3);
            Animation loadAnimation22 = AnimationUtils.loadAnimation(context, 2130771993);
            loadAnimation22.setInterpolator(interpolator2);
            this.errorViewSwitcher.setOutAnimation(loadAnimation22);
            TextView textView72 = new TextView(context);
            this.problemText = textView72;
            textView72.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.problemText.setTextSize(1, 15.0f);
            this.problemText.setGravity(49);
            this.problemText.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
            this.problemFrame.addView(this.problemText, LayoutHelper.createFrame(-2, -2, 17));
            this.errorViewSwitcher.addView(this.problemFrame);
            TextView textView82 = new TextView(context);
            this.wrongCode = textView82;
            textView82.setText(LocaleController.getString("WrongCode", 2131629252));
            this.wrongCode.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.wrongCode.setTextSize(1, 15.0f);
            this.wrongCode.setGravity(49);
            this.wrongCode.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
            this.errorViewSwitcher.addView(this.wrongCode);
            if (this.currentType != 1) {
            }
            if (frameLayout != null) {
            }
            VerticalPositionAutoAnimator.attach(this.errorViewSwitcher);
            this.problemText.setOnClickListener(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda6(this, context));
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivitySmsView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends CodeFieldContainer {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, LoginActivity loginActivity) {
                super(context);
                LoginActivitySmsView.this = r1;
            }

            @Override // org.telegram.ui.CodeFieldContainer
            protected void processNextPressed() {
                LoginActivitySmsView.this.onNextPressed(null);
            }
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivitySmsView$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends CodeFieldContainer {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass2(Context context, LoginActivity loginActivity) {
                super(context);
                LoginActivitySmsView.this = r1;
            }

            @Override // org.telegram.ui.CodeFieldContainer
            protected void processNextPressed() {
                LoginActivitySmsView.this.onNextPressed(null);
            }
        }

        public /* synthetic */ void lambda$new$4(View view) {
            int i = this.nextType;
            if (i != 4 && i != 2 && i != 11) {
                if (i != 3) {
                    return;
                }
                AndroidUtilities.setWaitingForSms(false);
                NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didReceiveSmsCode);
                this.waitingForEvent = false;
                destroyCodeTimer();
                resendCode();
                return;
            }
            this.timeText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            int i2 = this.nextType;
            if (i2 == 4 || i2 == 11) {
                this.timeText.setText(LocaleController.getString("Calling", 2131624808));
            } else {
                this.timeText.setText(LocaleController.getString("SendingSms", 2131628218));
            }
            Bundle bundle = new Bundle();
            bundle.putString("phone", this.phone);
            bundle.putString("ephone", this.emailPhone);
            bundle.putString("phoneFormated", this.requestPhone);
            createCodeTimer();
            TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode = new TLRPC$TL_auth_resendCode();
            tLRPC$TL_auth_resendCode.phone_number = this.requestPhone;
            tLRPC$TL_auth_resendCode.phone_code_hash = this.phoneHash;
            ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_resendCode, new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda36(this, bundle), 10);
        }

        public /* synthetic */ void lambda$new$1(Bundle bundle, TLObject tLObject) {
            LoginActivity.this.fillNextCodeParams(bundle, (TLRPC$TL_auth_sentCode) tLObject);
        }

        public /* synthetic */ void lambda$new$3(Bundle bundle, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            if (tLObject != null) {
                AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda25(this, bundle, tLObject));
            } else if (tLRPC$TL_error == null || tLRPC$TL_error.text == null) {
            } else {
                AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda28(this, tLRPC$TL_error));
            }
        }

        public /* synthetic */ void lambda$new$2(TLRPC$TL_error tLRPC$TL_error) {
            this.lastError = tLRPC$TL_error.text;
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivitySmsView$3 */
        /* loaded from: classes3.dex */
        public class AnonymousClass3 extends ViewSwitcher {
            AnonymousClass3(LoginActivitySmsView loginActivitySmsView, Context context, LoginActivity loginActivity) {
                super(context);
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), Integer.MIN_VALUE));
            }
        }

        public /* synthetic */ void lambda$new$7(Context context, View view) {
            if (this.nextPressed) {
                return;
            }
            if (!(this.nextType == 0)) {
                if (LoginActivity.this.radialProgressView.getTag() != null) {
                    return;
                }
                resendCode();
                return;
            }
            new AlertDialog.Builder(context).setTitle(LocaleController.getString(2131628020)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("DidNotGetTheCodeInfo", 2131625473, this.phone))).setNeutralButton(LocaleController.getString(2131625472), new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda0(this)).setPositiveButton(LocaleController.getString(2131625167), null).setNegativeButton(LocaleController.getString(2131625471), new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda2(this)).show();
        }

        public /* synthetic */ void lambda$new$5(DialogInterface dialogInterface, int i) {
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
                LoginActivity.this.needShowAlert(LocaleController.getString(2131624375), LocaleController.getString("NoMailInstalled", 2131626830));
            }
        }

        public /* synthetic */ void lambda$new$6(DialogInterface dialogInterface, int i) {
            LoginActivity.this.setPage(0, true, null, true);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void updateColors() {
            String str = "windowBackgroundWhiteGrayText6";
            this.confirmTextView.setTextColor(Theme.getColor(LoginActivity.this.isInCancelAccountDeletionMode() ? "windowBackgroundWhiteBlackText" : str));
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
            String str2 = (String) this.timeText.getTag();
            if (str2 != null) {
                str = str2;
            }
            this.timeText.setTextColor(Theme.getColor(str));
            this.problemText.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
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
            Bundle bundle = new Bundle();
            bundle.putString("phone", this.phone);
            bundle.putString("ephone", this.emailPhone);
            bundle.putString("phoneFormated", this.requestPhone);
            this.nextPressed = true;
            TLRPC$TL_auth_resendCode tLRPC$TL_auth_resendCode = new TLRPC$TL_auth_resendCode();
            tLRPC$TL_auth_resendCode.phone_number = this.requestPhone;
            tLRPC$TL_auth_resendCode.phone_code_hash = this.phoneHash;
            tryShowProgress(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_resendCode, new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda35(this, bundle), 10));
        }

        public /* synthetic */ void lambda$resendCode$9(Bundle bundle, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda29(this, tLRPC$TL_error, bundle, tLObject));
        }

        public /* synthetic */ void lambda$resendCode$8(TLRPC$TL_error tLRPC$TL_error, Bundle bundle, TLObject tLObject) {
            this.nextPressed = false;
            if (tLRPC$TL_error == null) {
                LoginActivity.this.fillNextCodeParams(bundle, (TLRPC$TL_auth_sentCode) tLObject);
            } else {
                String str = tLRPC$TL_error.text;
                if (str != null) {
                    if (str.contains("PHONE_NUMBER_INVALID")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("InvalidPhoneNumber", 2131626250));
                    } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("InvalidCode", 2131626246));
                    } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                        onBackPressed(true);
                        LoginActivity.this.setPage(0, true, null, true);
                        LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("CodeExpired", 2131625171));
                    } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("FloodWait", 2131625908));
                    } else if (tLRPC$TL_error.code != -1000) {
                        LoginActivity loginActivity = LoginActivity.this;
                        String string = LocaleController.getString(2131628020);
                        loginActivity.needShowAlert(string, LocaleController.getString("ErrorOccurred", 2131625657) + "\n" + tLRPC$TL_error.text);
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
            lambda$tryShowProgress$10(i, true);
        }

        /* renamed from: tryShowProgress */
        public void lambda$tryShowProgress$10(int i, boolean z) {
            if (this.starsToDotsDrawable == null) {
                LoginActivity.this.needShowProgress(i, z);
            } else if (this.isDotsAnimationVisible) {
            } else {
                this.isDotsAnimationVisible = true;
                if (this.hintDrawable.getCurrentFrame() != this.hintDrawable.getFramesCount() - 1) {
                    this.hintDrawable.setOnAnimationEndListener(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda21(this, i, z));
                    return;
                }
                this.starsToDotsDrawable.setOnAnimationEndListener(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda9(this));
                this.blueImageView.setAutoRepeat(false);
                this.starsToDotsDrawable.setCurrentFrame(0, false);
                this.blueImageView.setAnimation(this.starsToDotsDrawable);
                this.blueImageView.playAnimation();
            }
        }

        public /* synthetic */ void lambda$tryShowProgress$11(int i, boolean z) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda22(this, i, z));
        }

        public /* synthetic */ void lambda$tryShowProgress$13() {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda15(this));
        }

        public /* synthetic */ void lambda$tryShowProgress$12() {
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
            } else if (!this.isDotsAnimationVisible) {
            } else {
                this.isDotsAnimationVisible = false;
                this.blueImageView.setAutoRepeat(false);
                this.dotsDrawable.setAutoRepeat(0);
                this.dotsDrawable.setOnFinishCallback(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda16(this), this.dotsDrawable.getFramesCount() - 1);
            }
        }

        public /* synthetic */ void lambda$tryHideProgress$17() {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda17(this));
        }

        public /* synthetic */ void lambda$tryHideProgress$15() {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda11(this));
        }

        public /* synthetic */ void lambda$tryHideProgress$16() {
            this.dotsToStarsDrawable.setOnAnimationEndListener(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda14(this));
            this.blueImageView.setAutoRepeat(false);
            this.dotsToStarsDrawable.setCurrentFrame(0, false);
            this.blueImageView.setAnimation(this.dotsToStarsDrawable);
            this.blueImageView.playAnimation();
        }

        public /* synthetic */ void lambda$tryHideProgress$14() {
            this.blueImageView.setAutoRepeat(false);
            this.blueImageView.setAnimation(this.hintDrawable);
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            int i = this.currentType;
            if (i == 3 || i == 11) {
                return this.phone;
            }
            return LocaleController.getString("YourCode", 2131629275);
        }

        /* JADX WARN: Removed duplicated region for block: B:93:0x02d5  */
        /* JADX WARN: Removed duplicated region for block: B:94:0x02de  */
        @Override // org.telegram.ui.Components.SlideView
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void setParams(Bundle bundle, boolean z) {
            CodeNumberField[] codeNumberFieldArr;
            String str;
            int i;
            String str2;
            String string;
            int i2;
            if (bundle == null) {
                return;
            }
            boolean z2 = true;
            this.waitingForEvent = true;
            int i3 = this.currentType;
            if (i3 == 2) {
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
            this.codeFieldContainer.setNumbersCount(this.length, this.currentType);
            for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                if (Build.VERSION.SDK_INT >= 21) {
                    codeNumberField.setShowSoftInputOnFocusCompat(!hasCustomKeyboard() || LoginActivity.this.isCustomKeyboardForceDisabled());
                }
                codeNumberField.addTextChangedListener(new AnonymousClass4());
                codeNumberField.setOnFocusChangeListener(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda7(this));
            }
            if (this.phone == null) {
                return;
            }
            String format = PhoneFormat.getInstance().format(this.phone);
            if (LoginActivity.this.isInCancelAccountDeletionMode()) {
                str = new SpannableStringBuilder(AndroidUtilities.replaceTags(LocaleController.formatString("CancelAccountResetInfo2", 2131624821, PhoneFormat.getInstance().format("+" + format))));
                int indexOf = TextUtils.indexOf((CharSequence) str, '*');
                int lastIndexOf = TextUtils.lastIndexOf(str, '*');
                if (indexOf != -1 && lastIndexOf != -1 && indexOf != lastIndexOf) {
                    this.confirmTextView.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
                    str.replace(lastIndexOf, lastIndexOf + 1, (CharSequence) "");
                    str.replace(indexOf, indexOf + 1, (CharSequence) "");
                    str.setSpan(new URLSpanNoUnderline("tg://settings/change_number"), indexOf, lastIndexOf - 1, 33);
                }
            } else {
                int i5 = this.currentType;
                str = i5 == 1 ? AndroidUtilities.replaceTags(LocaleController.formatString("SentAppCodeWithPhone", 2131628223, LocaleController.addNbsp(format))) : i5 == 2 ? AndroidUtilities.replaceTags(LocaleController.formatString("SentSmsCode", 2131628226, LocaleController.addNbsp(format))) : i5 == 3 ? AndroidUtilities.replaceTags(LocaleController.formatString("SentCallCode", 2131628224, LocaleController.addNbsp(format))) : i5 == 4 ? AndroidUtilities.replaceTags(LocaleController.formatString("SentCallOnly", 2131628225, LocaleController.addNbsp(format))) : "";
            }
            this.confirmTextView.setText(str);
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
            } else {
                String str3 = null;
                if (i6 == 3 && ((i2 = this.nextType) == 4 || i2 == 2)) {
                    setProblemTextVisible(false);
                    this.timeText.setVisibility(0);
                    int i7 = this.nextType;
                    if (i7 == 4 || i7 == 11) {
                        this.timeText.setText(LocaleController.formatString("CallAvailableIn", 2131624783, 1, 0));
                    } else if (i7 == 2) {
                        this.timeText.setText(LocaleController.formatString("SmsAvailableIn", 2131628375, 1, 0));
                    }
                    if (z) {
                        str3 = AndroidUtilities.obtainLoginPhoneCall(this.pattern);
                    }
                    if (str3 != null) {
                        onNextPressed(str3);
                    } else {
                        String str4 = this.catchedPhone;
                        if (str4 != null) {
                            onNextPressed(str4);
                        } else {
                            createTimer();
                        }
                    }
                } else if (i6 == 2 && ((i = this.nextType) == 4 || i == 3)) {
                    this.timeText.setText(LocaleController.formatString("CallAvailableIn", 2131624783, 2, 0));
                    setProblemTextVisible(this.time < 1000);
                    this.timeText.setVisibility(this.time < 1000 ? 8 : 0);
                    SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
                    String string2 = sharedPreferences.getString("sms_hash", null);
                    if (!TextUtils.isEmpty(string2) && (string = sharedPreferences.getString("sms_hash_code", null)) != null) {
                        if (string.contains(string2 + "|")) {
                            str2 = string.substring(string.indexOf(124) + 1);
                            if (str2 == null) {
                                this.codeFieldContainer.setCode(str2);
                                onNextPressed(null);
                            } else {
                                createTimer();
                            }
                        }
                    }
                    str2 = null;
                    if (str2 == null) {
                    }
                } else if (i6 == 4 && this.nextType == 2) {
                    this.timeText.setText(LocaleController.formatString("SmsAvailableIn", 2131628375, 2, 0));
                    if (this.time >= 1000) {
                        z2 = false;
                    }
                    setProblemTextVisible(z2);
                    this.timeText.setVisibility(this.time < 1000 ? 8 : 0);
                    createTimer();
                } else {
                    this.timeText.setVisibility(8);
                    setProblemTextVisible(false);
                    createCodeTimer();
                }
            }
            if (this.currentType != 11) {
                return;
            }
            String str5 = this.prefix;
            for (int i8 = 0; i8 < this.length; i8++) {
                str5 = str5 + "0";
            }
            String format2 = PhoneFormat.getInstance().format("+" + str5);
            for (int i9 = 0; i9 < this.length; i9++) {
                int lastIndexOf2 = format2.lastIndexOf("0");
                if (lastIndexOf2 >= 0) {
                    format2 = format2.substring(0, lastIndexOf2);
                }
            }
            this.prefixTextView.setText(format2.replaceAll("\\)", "").replaceAll("\\(", ""));
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivitySmsView$4 */
        /* loaded from: classes3.dex */
        public class AnonymousClass4 implements TextWatcher {
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass4() {
                LoginActivitySmsView.this = r1;
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (LoginActivitySmsView.this.postedErrorColorTimeout) {
                    LoginActivitySmsView loginActivitySmsView = LoginActivitySmsView.this;
                    loginActivitySmsView.removeCallbacks(loginActivitySmsView.errorColorTimeout);
                    LoginActivitySmsView.this.errorColorTimeout.run();
                }
            }
        }

        public /* synthetic */ void lambda$setParams$18(View view, boolean z) {
            if (z) {
                LoginActivity.this.keyboardView.setEditText((EditText) view);
                LoginActivity.this.keyboardView.setDispatchBackWhenEmpty(true);
            }
        }

        public void setProblemTextVisible(boolean z) {
            float f = z ? 1.0f : 0.0f;
            if (this.problemText.getAlpha() != f) {
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
            this.codeTimer.schedule(new AnonymousClass5(), 0L, 1000L);
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivitySmsView$5 */
        /* loaded from: classes3.dex */
        public class AnonymousClass5 extends TimerTask {
            AnonymousClass5() {
                LoginActivitySmsView.this = r1;
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$5$$ExternalSyntheticLambda0(this));
            }

            public /* synthetic */ void lambda$run$0() {
                double currentTimeMillis = System.currentTimeMillis();
                double d = LoginActivitySmsView.this.lastCodeTime;
                Double.isNaN(currentTimeMillis);
                LoginActivitySmsView.this.lastCodeTime = currentTimeMillis;
                LoginActivitySmsView.access$8126(LoginActivitySmsView.this, currentTimeMillis - d);
                if (LoginActivitySmsView.this.codeTime <= 1000) {
                    LoginActivitySmsView.this.setProblemTextVisible(true);
                    LoginActivitySmsView.this.timeText.setVisibility(8);
                    LoginActivitySmsView.this.destroyCodeTimer();
                }
            }
        }

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
            this.timeText.setTag(2131230789, "windowBackgroundWhiteGrayText6");
            Timer timer = new Timer();
            this.timeTimer = timer;
            timer.schedule(new AnonymousClass6(), 0L, 1000L);
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivitySmsView$6 */
        /* loaded from: classes3.dex */
        public class AnonymousClass6 extends TimerTask {
            AnonymousClass6() {
                LoginActivitySmsView.this = r1;
            }

            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                if (LoginActivitySmsView.this.timeTimer == null) {
                    return;
                }
                AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$6$$ExternalSyntheticLambda0(this));
            }

            public /* synthetic */ void lambda$run$0() {
                double currentTimeMillis = System.currentTimeMillis();
                double d = LoginActivitySmsView.this.lastCurrentTime;
                Double.isNaN(currentTimeMillis);
                LoginActivitySmsView.this.lastCurrentTime = currentTimeMillis;
                LoginActivitySmsView.access$8726(LoginActivitySmsView.this, currentTimeMillis - d);
                if (LoginActivitySmsView.this.time >= 1000) {
                    int i = (LoginActivitySmsView.this.time / 1000) / 60;
                    int i2 = (LoginActivitySmsView.this.time / 1000) - (i * 60);
                    if (LoginActivitySmsView.this.nextType == 4 || LoginActivitySmsView.this.nextType == 3 || LoginActivitySmsView.this.nextType == 11) {
                        LoginActivitySmsView.this.timeText.setText(LocaleController.formatString("CallAvailableIn", 2131624783, Integer.valueOf(i), Integer.valueOf(i2)));
                    } else if (LoginActivitySmsView.this.nextType == 2) {
                        LoginActivitySmsView.this.timeText.setText(LocaleController.formatString("SmsAvailableIn", 2131628375, Integer.valueOf(i), Integer.valueOf(i2)));
                    }
                    ProgressView unused = LoginActivitySmsView.this.progressView;
                    return;
                }
                LoginActivitySmsView.this.destroyTimer();
                if (LoginActivitySmsView.this.nextType != 3 && LoginActivitySmsView.this.nextType != 4 && LoginActivitySmsView.this.nextType != 2 && LoginActivitySmsView.this.nextType != 11) {
                    return;
                }
                if (LoginActivitySmsView.this.nextType == 4) {
                    LoginActivitySmsView.this.timeText.setText(LocaleController.getString("RequestCallButton", 2131627965));
                } else if (LoginActivitySmsView.this.nextType == 11 || LoginActivitySmsView.this.nextType == 3) {
                    LoginActivitySmsView.this.timeText.setText(LocaleController.getString("RequestMissedCall", 2131627966));
                } else {
                    LoginActivitySmsView.this.timeText.setText(LocaleController.getString("RequestSmsButton", 2131627967));
                }
                LoginActivitySmsView.this.timeText.setTextColor(Theme.getColor("chats_actionBackground"));
                LoginActivitySmsView.this.timeText.setTag(2131230789, "chats_actionBackground");
            }
        }

        public void destroyTimer() {
            this.timeText.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.timeText.setTag(2131230789, "windowBackgroundWhiteGrayText6");
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
            } else if (this.nextPressed || LoginActivity.this.currentViewNum < 1 || LoginActivity.this.currentViewNum > 4) {
                return;
            }
            if (str == null) {
                str = this.codeFieldContainer.getCode();
            }
            int i = 0;
            if (TextUtils.isEmpty(str)) {
                LoginActivity.this.onFieldError(this.codeFieldContainer, false);
            } else if (LoginActivity.this.currentViewNum >= 1 && LoginActivity.this.currentViewNum <= 4 && this.codeFieldContainer.isFocusSuppressed) {
            } else {
                this.nextPressed = true;
                int i2 = this.currentType;
                if (i2 == 2) {
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
                    TLRPC$TL_account_confirmPhone tLRPC$TL_account_confirmPhone = new TLRPC$TL_account_confirmPhone();
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
                    tryShowProgress(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_account_confirmPhone, new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda37(this, tLRPC$TL_account_confirmPhone), 2));
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
                    lambda$tryShowProgress$10(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_account_changePhone, new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda34(this), 2), true);
                    LoginActivity.this.showDoneButton(true, true);
                } else {
                    TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn = new TLRPC$TL_auth_signIn();
                    tLRPC$TL_auth_signIn.phone_number = this.requestPhone;
                    tLRPC$TL_auth_signIn.phone_code = str;
                    tLRPC$TL_auth_signIn.phone_code_hash = this.phoneHash;
                    destroyTimer();
                    CodeFieldContainer codeFieldContainer3 = this.codeFieldContainer;
                    codeFieldContainer3.isFocusSuppressed = true;
                    CodeNumberField[] codeNumberFieldArr3 = codeFieldContainer3.codeField;
                    int length3 = codeNumberFieldArr3.length;
                    while (i < length3) {
                        codeNumberFieldArr3[i].animateFocusedProgress(0.0f);
                        i++;
                    }
                    lambda$tryShowProgress$10(ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_signIn, new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda38(this, tLRPC$TL_auth_signIn), 10), true);
                    LoginActivity.this.showDoneButton(true, true);
                }
            }
        }

        public /* synthetic */ void lambda$onNextPressed$22(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda30(this, tLRPC$TL_error, tLObject));
        }

        /* JADX WARN: Removed duplicated region for block: B:45:0x0172  */
        /* JADX WARN: Removed duplicated region for block: B:54:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$onNextPressed$21(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
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
                animateSuccess(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda18(this));
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
            if (i4 == 2) {
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
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("InvalidPhoneNumber", 2131626250));
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
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("CodeExpired", 2131625171));
            } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("FloodWait", 2131625908));
            } else {
                LoginActivity loginActivity = LoginActivity.this;
                String string = LocaleController.getString(2131628020);
                loginActivity.needShowAlert(string, LocaleController.getString("ErrorOccurred", 2131625657) + "\n" + tLRPC$TL_error.text);
            }
            z = false;
            if (!z) {
            }
        }

        public /* synthetic */ void lambda$onNextPressed$20() {
            try {
                ((BaseFragment) LoginActivity.this).fragmentView.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            new AlertDialog.Builder(getContext()).setTitle(LocaleController.getString(2131629300)).setMessage(LocaleController.getString(2131624862)).setPositiveButton(LocaleController.getString(2131627075), null).setOnDismissListener(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda3(this)).show();
        }

        public /* synthetic */ void lambda$onNextPressed$19(DialogInterface dialogInterface) {
            LoginActivity.this.finishFragment();
        }

        public /* synthetic */ void lambda$onNextPressed$26(TLRPC$TL_account_confirmPhone tLRPC$TL_account_confirmPhone, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda33(this, tLRPC$TL_error, tLRPC$TL_account_confirmPhone));
        }

        public /* synthetic */ void lambda$onNextPressed$25(TLRPC$TL_error tLRPC$TL_error, TLRPC$TL_account_confirmPhone tLRPC$TL_account_confirmPhone) {
            int i;
            int i2;
            tryHideProgress(false);
            this.nextPressed = false;
            if (tLRPC$TL_error == null) {
                animateSuccess(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda10(this));
                return;
            }
            this.lastError = tLRPC$TL_error.text;
            int i3 = this.currentType;
            if ((i3 == 3 && ((i2 = this.nextType) == 4 || i2 == 2)) || ((i3 == 2 && ((i = this.nextType) == 4 || i == 3)) || (i3 == 4 && this.nextType == 2))) {
                createTimer();
            }
            int i4 = this.currentType;
            if (i4 == 2) {
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
            } else if (!tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
            } else {
                onBackPressed(true);
                LoginActivity.this.setPage(0, true, null, true);
            }
        }

        public /* synthetic */ void lambda$onNextPressed$24() {
            AlertDialog.Builder title = new AlertDialog.Builder(LoginActivity.this.getParentActivity()).setTitle(LocaleController.getString(2131624831));
            PhoneFormat phoneFormat = PhoneFormat.getInstance();
            title.setMessage(LocaleController.formatString("CancelLinkSuccess", 2131624830, phoneFormat.format("+" + this.phone))).setPositiveButton(LocaleController.getString(2131625167), null).setOnDismissListener(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda4(this)).show();
        }

        public /* synthetic */ void lambda$onNextPressed$23(DialogInterface dialogInterface) {
            LoginActivity.this.finishFragment();
        }

        public /* synthetic */ void lambda$onNextPressed$33(TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda32(this, tLRPC$TL_error, tLObject, tLRPC$TL_auth_signIn));
        }

        /* JADX WARN: Removed duplicated region for block: B:52:0x016f  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$onNextPressed$32(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn) {
            CodeFieldContainer codeFieldContainer;
            CodeNumberField[] codeNumberFieldArr;
            int i;
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
                    Bundle bundle = new Bundle();
                    bundle.putString("phoneFormated", this.requestPhone);
                    bundle.putString("phoneHash", this.phoneHash);
                    bundle.putString("code", tLRPC$TL_auth_signIn.phone_code);
                    animateSuccess(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda23(this, bundle));
                } else {
                    animateSuccess(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda27(this, tLObject));
                }
            } else {
                String str = tLRPC$TL_error.text;
                this.lastError = str;
                if (str.contains("SESSION_PASSWORD_NEEDED")) {
                    ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda39(this, tLRPC$TL_auth_signIn), 10);
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
                    if (i4 == 2) {
                        AndroidUtilities.setWaitingForSms(true);
                        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveSmsCode);
                    } else if (i4 == 3) {
                        AndroidUtilities.setWaitingForCall(true);
                        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didReceiveCall);
                    }
                    this.waitingForEvent = true;
                    if (this.currentType != 3) {
                        if (tLRPC$TL_error.text.contains("PHONE_NUMBER_INVALID")) {
                            LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("InvalidPhoneNumber", 2131626250));
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
                            LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("CodeExpired", 2131625171));
                        } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                            LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("FloodWait", 2131625908));
                        } else {
                            LoginActivity loginActivity = LoginActivity.this;
                            String string = LocaleController.getString(2131628020);
                            loginActivity.needShowAlert(string, LocaleController.getString("ErrorOccurred", 2131625657) + "\n" + tLRPC$TL_error.text);
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

        public /* synthetic */ void lambda$onNextPressed$27(Bundle bundle) {
            LoginActivity.this.setPage(5, true, bundle, false);
        }

        public /* synthetic */ void lambda$onNextPressed$28(TLObject tLObject) {
            LoginActivity.this.onAuthSuccess((TLRPC$TL_auth_authorization) tLObject);
        }

        public /* synthetic */ void lambda$onNextPressed$31(TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda31(this, tLRPC$TL_error, tLObject, tLRPC$TL_auth_signIn));
        }

        public /* synthetic */ void lambda$onNextPressed$30(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, TLRPC$TL_auth_signIn tLRPC$TL_auth_signIn) {
            this.nextPressed = false;
            LoginActivity.this.showDoneButton(false, true);
            if (tLRPC$TL_error != null) {
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), tLRPC$TL_error.text);
                return;
            }
            TLRPC$TL_account_password tLRPC$TL_account_password = (TLRPC$TL_account_password) tLObject;
            if (!TwoStepVerificationActivity.canHandleCurrentPassword(tLRPC$TL_account_password, true)) {
                AlertsCreator.showUpdateAppAlert(LoginActivity.this.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131628758), true);
                return;
            }
            Bundle bundle = new Bundle();
            SerializedData serializedData = new SerializedData(tLRPC$TL_account_password.getObjectSize());
            tLRPC$TL_account_password.serializeToStream(serializedData);
            bundle.putString("password", Utilities.bytesToHex(serializedData.toByteArray()));
            bundle.putString("phoneFormated", this.requestPhone);
            bundle.putString("phoneHash", this.phoneHash);
            bundle.putString("code", tLRPC$TL_auth_signIn.phone_code);
            animateSuccess(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda24(this, bundle));
        }

        public /* synthetic */ void lambda$onNextPressed$29(Bundle bundle) {
            LoginActivity.this.setPage(6, true, bundle, false);
        }

        private void animateSuccess(Runnable runnable) {
            int i = 0;
            while (true) {
                CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
                if (i < codeFieldContainer.codeField.length) {
                    codeFieldContainer.postDelayed(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda20(this, i), i * 75);
                    i++;
                } else {
                    codeFieldContainer.postDelayed(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda26(this, runnable), (this.codeFieldContainer.codeField.length * 75) + 400);
                    return;
                }
            }
        }

        public /* synthetic */ void lambda$animateSuccess$34(int i) {
            this.codeFieldContainer.codeField[i].animateSuccessProgress(1.0f);
        }

        public /* synthetic */ void lambda$animateSuccess$35(Runnable runnable) {
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
            if (this.errorViewSwitcher.getCurrentView() == this.problemFrame) {
                this.errorViewSwitcher.showNext();
            }
            this.codeFieldContainer.codeField[0].requestFocus();
            AndroidUtilities.shakeViewSpring(this.codeFieldContainer, this.currentType == 11 ? 3.5f : 10.0f, new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda19(this));
            removeCallbacks(this.errorColorTimeout);
            postDelayed(this.errorColorTimeout, 5000L);
            this.postedErrorColorTimeout = true;
        }

        public /* synthetic */ void lambda$shakeWrongCode$37() {
            postDelayed(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda13(this), 150L);
        }

        public /* synthetic */ void lambda$shakeWrongCode$36() {
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            int i = 0;
            codeFieldContainer.isFocusSuppressed = false;
            codeFieldContainer.codeField[0].requestFocus();
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i < codeNumberFieldArr.length) {
                    codeNumberFieldArr[i].animateErrorProgress(0.0f);
                    i++;
                } else {
                    return;
                }
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
                loginActivity.showDialog(new AlertDialog.Builder(loginActivity.getParentActivity()).setTitle(LocaleController.getString(2131625588)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("EditNumberInfo", 2131625589, this.phone))).setPositiveButton(LocaleController.getString(2131625167), null).setNegativeButton(LocaleController.getString(2131625537), new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda1(this)).create());
                return false;
            } else {
                this.nextPressed = false;
                tryHideProgress(true);
                TLRPC$TL_auth_cancelCode tLRPC$TL_auth_cancelCode = new TLRPC$TL_auth_cancelCode();
                tLRPC$TL_auth_cancelCode.phone_number = this.requestPhone;
                tLRPC$TL_auth_cancelCode.phone_code_hash = this.phoneHash;
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_cancelCode, LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda40.INSTANCE, 10);
                destroyTimer();
                destroyCodeTimer();
                this.currentParams = null;
                int i = this.currentType;
                if (i == 2) {
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

        public /* synthetic */ void lambda$onBackPressed$38(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            LoginActivity.this.setPage(0, true, null, true);
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onDestroyActivity() {
            super.onDestroyActivity();
            int i = this.currentType;
            if (i == 2) {
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
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivitySmsView$$ExternalSyntheticLambda12(this), LoginActivity.SHOW_DELAY);
        }

        public /* synthetic */ void lambda$onShow$40() {
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
                } else if (i != NotificationCenter.didReceiveCall) {
                } else {
                    String str = "" + objArr[0];
                    if (!AndroidUtilities.checkPhonePattern(this.pattern, str)) {
                        return;
                    }
                    if (!this.pattern.equals("*")) {
                        this.catchedPhone = str;
                        AndroidUtilities.endIncomingCall();
                    }
                    onNextPressed(str);
                }
            }
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
        private TLRPC$TL_account_password currentPassword;
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

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Removed duplicated region for block: B:12:0x0146  */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0148  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x01ce  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x01d1  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public LoginActivityPasswordView(Context context) {
            super(context);
            int i;
            LoginActivity.this = r19;
            setOrientation(1);
            FrameLayout frameLayout = new FrameLayout(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.lockImageView = rLottieImageView;
            rLottieImageView.setAnimation(2131558558, 120, 120);
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
                    this.titleView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    this.titleView.setText(LocaleController.getString(2131629295));
                    this.titleView.setGravity(17);
                    this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
                    TextView textView2 = new TextView(context);
                    this.confirmTextView = textView2;
                    textView2.setTextSize(1, 14.0f);
                    this.confirmTextView.setGravity(1);
                    this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.confirmTextView.setText(LocaleController.getString(2131626506));
                    addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 12, 8, 12, 0));
                    OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
                    this.outlineCodeField = outlineTextContainerView;
                    outlineTextContainerView.setText(LocaleController.getString(2131625651));
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
                    this.codeField.setOnFocusChangeListener(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda3(this));
                    this.outlineCodeField.attachEditText(this.codeField);
                    this.outlineCodeField.addView(this.codeField, LayoutHelper.createFrame(-1, -2, 48));
                    this.codeField.setOnEditorActionListener(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda4(this));
                    addView(this.outlineCodeField, LayoutHelper.createLinear(-1, -2, 1, 16, 32, 16, 0));
                    TextView textView3 = new TextView(context);
                    this.cancelButton = textView3;
                    textView3.setGravity(19);
                    this.cancelButton.setText(LocaleController.getString("ForgotPassword", 2131625939));
                    this.cancelButton.setTextSize(1, 15.0f);
                    this.cancelButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.cancelButton.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
                    FrameLayout frameLayout2 = new FrameLayout(context);
                    frameLayout2.addView(this.cancelButton, LayoutHelper.createFrame(-1, Build.VERSION.SDK_INT < 21 ? 56 : 60, 80, 0.0f, 0.0f, 0.0f, 32.0f));
                    addView(frameLayout2, LayoutHelper.createLinear(-1, -1, 80));
                    VerticalPositionAutoAnimator.attach(this.cancelButton);
                    this.cancelButton.setOnClickListener(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda2(this, context));
                }
            }
            i = 8;
            frameLayout.setVisibility(i);
            addView(frameLayout, LayoutHelper.createFrame(-1, -2, 1));
            TextView textView4 = new TextView(context);
            this.titleView = textView4;
            textView4.setTextSize(1, 18.0f);
            this.titleView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.titleView.setText(LocaleController.getString(2131629295));
            this.titleView.setGravity(17);
            this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
            TextView textView22 = new TextView(context);
            this.confirmTextView = textView22;
            textView22.setTextSize(1, 14.0f);
            this.confirmTextView.setGravity(1);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.confirmTextView.setText(LocaleController.getString(2131626506));
            addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 12, 8, 12, 0));
            OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
            this.outlineCodeField = outlineTextContainerView2;
            outlineTextContainerView2.setText(LocaleController.getString(2131625651));
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
            this.codeField.setOnFocusChangeListener(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda3(this));
            this.outlineCodeField.attachEditText(this.codeField);
            this.outlineCodeField.addView(this.codeField, LayoutHelper.createFrame(-1, -2, 48));
            this.codeField.setOnEditorActionListener(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda4(this));
            addView(this.outlineCodeField, LayoutHelper.createLinear(-1, -2, 1, 16, 32, 16, 0));
            TextView textView32 = new TextView(context);
            this.cancelButton = textView32;
            textView32.setGravity(19);
            this.cancelButton.setText(LocaleController.getString("ForgotPassword", 2131625939));
            this.cancelButton.setTextSize(1, 15.0f);
            this.cancelButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.cancelButton.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
            FrameLayout frameLayout22 = new FrameLayout(context);
            frameLayout22.addView(this.cancelButton, LayoutHelper.createFrame(-1, Build.VERSION.SDK_INT < 21 ? 56 : 60, 80, 0.0f, 0.0f, 0.0f, 32.0f));
            addView(frameLayout22, LayoutHelper.createLinear(-1, -1, 80));
            VerticalPositionAutoAnimator.attach(this.cancelButton);
            this.cancelButton.setOnClickListener(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda2(this, context));
        }

        public /* synthetic */ void lambda$new$0(View view, boolean z) {
            this.outlineCodeField.animateSelection(z ? 1.0f : 0.0f);
        }

        public /* synthetic */ boolean lambda$new$1(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 5) {
                onNextPressed(null);
                return true;
            }
            return false;
        }

        public /* synthetic */ void lambda$new$6(Context context, View view) {
            if (LoginActivity.this.radialProgressView.getTag() != null) {
                return;
            }
            if (this.currentPassword.has_recovery) {
                LoginActivity.this.needShowProgress(0);
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(new TLRPC$TL_auth_requestPasswordRecovery(), new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda12(this), 10);
                return;
            }
            AndroidUtilities.hideKeyboard(this.codeField);
            new AlertDialog.Builder(context).setTitle(LocaleController.getString(2131628020)).setMessage(LocaleController.getString(2131628018)).setPositiveButton(LocaleController.getString(2131625167), null).setNegativeButton(LocaleController.getString(2131627981), new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda0(this)).show();
        }

        public /* synthetic */ void lambda$new$4(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda8(this, tLRPC$TL_error, tLObject));
        }

        public /* synthetic */ void lambda$new$3(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            String str;
            LoginActivity.this.needHideProgress(false);
            if (tLRPC$TL_error == null) {
                TLRPC$TL_auth_passwordRecovery tLRPC$TL_auth_passwordRecovery = (TLRPC$TL_auth_passwordRecovery) tLObject;
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                String str2 = tLRPC$TL_auth_passwordRecovery.email_pattern;
                SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(str2);
                int indexOf = str2.indexOf(42);
                int lastIndexOf = str2.lastIndexOf(42);
                if (indexOf != lastIndexOf && indexOf != -1 && lastIndexOf != -1) {
                    TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                    textStyleRun.flags |= 256;
                    textStyleRun.start = indexOf;
                    int i = lastIndexOf + 1;
                    textStyleRun.end = i;
                    valueOf.setSpan(new TextStyleSpan(textStyleRun), indexOf, i, 0);
                }
                builder.setMessage(AndroidUtilities.formatSpannable(LocaleController.getString(2131628011), valueOf));
                builder.setTitle(LocaleController.getString("RestoreEmailSentTitle", 2131628013));
                builder.setPositiveButton(LocaleController.getString(2131625246), new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda1(this, tLRPC$TL_auth_passwordRecovery));
                Dialog showDialog = LoginActivity.this.showDialog(builder.create());
                if (showDialog == null) {
                    return;
                }
                showDialog.setCanceledOnTouchOutside(false);
                showDialog.setCancelable(false);
            } else if (!tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), tLRPC$TL_error.text);
            } else {
                int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                if (intValue < 60) {
                    str = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                } else {
                    str = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                }
                LoginActivity.this.needShowAlert(LocaleController.getString(2131629253), LocaleController.formatString("FloodWaitTime", 2131625909, str));
            }
        }

        public /* synthetic */ void lambda$new$2(TLRPC$TL_auth_passwordRecovery tLRPC$TL_auth_passwordRecovery, DialogInterface dialogInterface, int i) {
            Bundle bundle = new Bundle();
            bundle.putString("email_unconfirmed_pattern", tLRPC$TL_auth_passwordRecovery.email_pattern);
            bundle.putString("password", this.passwordString);
            bundle.putString("requestPhone", this.requestPhone);
            bundle.putString("phoneHash", this.phoneHash);
            bundle.putString("phoneCode", this.phoneCode);
            LoginActivity.this.setPage(7, true, bundle, false);
        }

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
            return LocaleController.getString("LoginPassword", 2131626504);
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
                this.currentPassword = TLRPC$TL_account_password.TLdeserialize(serializedData, serializedData.readInt32(false), false);
            }
            this.requestPhone = bundle.getString("phoneFormated");
            this.phoneHash = bundle.getString("phoneHash");
            this.phoneCode = bundle.getString("code");
            TLRPC$TL_account_password tLRPC$TL_account_password = this.currentPassword;
            if (tLRPC$TL_account_password != null && !TextUtils.isEmpty(tLRPC$TL_account_password.hint)) {
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
            String obj = this.codeField.getText().toString();
            if (obj.length() == 0) {
                onPasscodeError(false);
                return;
            }
            this.nextPressed = true;
            LoginActivity.this.needShowProgress(0);
            Utilities.globalQueue.postRunnable(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda6(this, obj));
        }

        public /* synthetic */ void lambda$onNextPressed$12(String str) {
            TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = this.currentPassword.current_algo;
            boolean z = tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow;
            byte[] x = z ? SRPHelper.getX(AndroidUtilities.getStringBytes(str), (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo) : null;
            TLRPC$TL_auth_checkPassword tLRPC$TL_auth_checkPassword = new TLRPC$TL_auth_checkPassword();
            LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda13 loginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda13 = new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda13(this);
            if (z) {
                TLRPC$TL_account_password tLRPC$TL_account_password = this.currentPassword;
                TLRPC$TL_inputCheckPasswordSRP startCheck = SRPHelper.startCheck(x, tLRPC$TL_account_password.srp_id, tLRPC$TL_account_password.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo);
                tLRPC$TL_auth_checkPassword.password = startCheck;
                if (startCheck != null) {
                    ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_checkPassword, loginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda13, 10);
                    return;
                }
                TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
                tLRPC$TL_error.text = "PASSWORD_HASH_INVALID";
                loginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda13.run(null, tLRPC$TL_error);
            }
        }

        public /* synthetic */ void lambda$onNextPressed$11(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda10(this, tLRPC$TL_error, tLObject));
        }

        public /* synthetic */ void lambda$onNextPressed$10(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            String str;
            this.nextPressed = false;
            if (tLRPC$TL_error != null && "SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda11(this), 8);
            } else if (tLObject instanceof TLRPC$TL_auth_authorization) {
                LoginActivity.this.showDoneButton(false, true);
                postDelayed(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda7(this, tLObject), 150L);
            } else {
                LoginActivity.this.needHideProgress(false);
                if (tLRPC$TL_error.text.equals("PASSWORD_HASH_INVALID")) {
                    onPasscodeError(true);
                } else if (!tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), tLRPC$TL_error.text);
                } else {
                    int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                    if (intValue < 60) {
                        str = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                    } else {
                        str = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                    }
                    LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.formatString("FloodWaitTime", 2131625909, str));
                }
            }
        }

        public /* synthetic */ void lambda$onNextPressed$8(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda9(this, tLRPC$TL_error, tLObject));
        }

        public /* synthetic */ void lambda$onNextPressed$7(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
            if (tLRPC$TL_error == null) {
                this.currentPassword = (TLRPC$TL_account_password) tLObject;
                onNextPressed(null);
            }
        }

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
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityPasswordView$$ExternalSyntheticLambda5(this), LoginActivity.SHOW_DELAY);
        }

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

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public LoginActivityResetWaitView(Context context) {
            super(context);
            LoginActivity.this = r21;
            setOrientation(1);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setGravity(17);
            FrameLayout frameLayout = new FrameLayout(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.waitImageView = rLottieImageView;
            rLottieImageView.setAutoRepeat(true);
            this.waitImageView.setAnimation(2131558525, 120, 120);
            frameLayout.addView(this.waitImageView, LayoutHelper.createFrame(120, 120, 1));
            Point point = AndroidUtilities.displaySize;
            frameLayout.setVisibility((point.x <= point.y || AndroidUtilities.isTablet()) ? 0 : 8);
            linearLayout.addView(frameLayout, LayoutHelper.createFrame(-1, -2, 1));
            TextView textView = new TextView(context);
            this.titleView = textView;
            textView.setTextSize(1, 18.0f);
            this.titleView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.titleView.setText(LocaleController.getString(2131627981));
            this.titleView.setGravity(17);
            this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            linearLayout.addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.confirmTextView = textView2;
            textView2.setTextSize(1, 14.0f);
            this.confirmTextView.setGravity(1);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            linearLayout.addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 12, 8, 12, 0));
            addView(linearLayout, LayoutHelper.createLinear(-1, 0, 1.0f));
            TextView textView3 = new TextView(context);
            this.resetAccountText = textView3;
            textView3.setGravity(1);
            this.resetAccountText.setText(LocaleController.getString("ResetAccountStatus", 2131627985));
            this.resetAccountText.setTextSize(1, 14.0f);
            this.resetAccountText.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.resetAccountText, LayoutHelper.createLinear(-2, -2, 49, 0, 24, 0, 0));
            TextView textView4 = new TextView(context);
            this.resetAccountTime = textView4;
            textView4.setGravity(1);
            this.resetAccountTime.setTextSize(1, 20.0f);
            this.resetAccountTime.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.resetAccountTime.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.resetAccountTime, LayoutHelper.createLinear(-2, -2, 1, 0, 8, 0, 0));
            TextView textView5 = new TextView(context);
            this.resetAccountButton = textView5;
            textView5.setGravity(17);
            this.resetAccountButton.setText(LocaleController.getString(2131627981));
            this.resetAccountButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.resetAccountButton.setTextSize(1, 15.0f);
            this.resetAccountButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.resetAccountButton.setPadding(AndroidUtilities.dp(34.0f), 0, AndroidUtilities.dp(34.0f), 0);
            this.resetAccountButton.setTextColor(-1);
            addView(this.resetAccountButton, LayoutHelper.createLinear(-1, 50, 1, 16, 32, 16, 48));
            this.resetAccountButton.setOnClickListener(new LoginActivity$LoginActivityResetWaitView$$ExternalSyntheticLambda1(this));
        }

        public /* synthetic */ void lambda$new$3(View view) {
            if (LoginActivity.this.radialProgressView.getTag() != null) {
                return;
            }
            LoginActivity loginActivity = LoginActivity.this;
            loginActivity.showDialog(new AlertDialog.Builder(loginActivity.getParentActivity()).setTitle(LocaleController.getString("ResetMyAccountWarning", 2131627999)).setMessage(LocaleController.getString("ResetMyAccountWarningText", 2131628001)).setPositiveButton(LocaleController.getString("ResetMyAccountWarningReset", 2131628000), new LoginActivity$LoginActivityResetWaitView$$ExternalSyntheticLambda0(this)).setNegativeButton(LocaleController.getString("Cancel", 2131624819), null).create());
        }

        public /* synthetic */ void lambda$new$2(DialogInterface dialogInterface, int i) {
            LoginActivity.this.needShowProgress(0);
            TLRPC$TL_account_deleteAccount tLRPC$TL_account_deleteAccount = new TLRPC$TL_account_deleteAccount();
            tLRPC$TL_account_deleteAccount.reason = "Forgot password";
            ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_account_deleteAccount, new LoginActivity$LoginActivityResetWaitView$$ExternalSyntheticLambda3(this), 10);
        }

        public /* synthetic */ void lambda$new$1(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityResetWaitView$$ExternalSyntheticLambda2(this, tLRPC$TL_error));
        }

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
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("ResetAccountCancelledAlert", 2131627983));
            } else {
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), tLRPC$TL_error.text);
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
            return LocaleController.getString("ResetAccount", 2131627981);
        }

        public void updateTimeText() {
            int i = 0;
            int max = Math.max(0, this.waitTime - (ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).getCurrentTime() - this.startTime));
            int i2 = max / 86400;
            int round = Math.round(max / 86400.0f);
            int i3 = max / 3600;
            int i4 = (max / 60) % 60;
            int i5 = max % 60;
            if (i2 >= 2) {
                this.resetAccountTime.setText(LocaleController.formatPluralString("Days", round, new Object[0]));
            } else {
                this.resetAccountTime.setText(String.format(Locale.getDefault(), "%02d:%02d:%02d", Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5)));
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
                TextView textView = this.resetAccountButton;
                if (!z) {
                    i = 4;
                }
                textView.setVisibility(i);
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
            PhoneFormat phoneFormat = PhoneFormat.getInstance();
            textView.setText(AndroidUtilities.replaceTags(LocaleController.formatString("ResetAccountInfo", 2131627984, LocaleController.addNbsp(phoneFormat.format("+" + this.requestPhone)))));
            updateTimeText();
            AnonymousClass1 anonymousClass1 = new AnonymousClass1();
            this.timeRunnable = anonymousClass1;
            AndroidUtilities.runOnUIThread(anonymousClass1, 1000L);
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivityResetWaitView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
                LoginActivityResetWaitView.this = r1;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (LoginActivityResetWaitView.this.timeRunnable != this) {
                    return;
                }
                LoginActivityResetWaitView.this.updateTimeText();
                AndroidUtilities.runOnUIThread(LoginActivityResetWaitView.this.timeRunnable, 1000L);
            }
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
    public class LoginActivityRecoverView extends SlideView {
        private CodeFieldContainer codeFieldContainer;
        private TextView confirmTextView;
        private Bundle currentParams;
        private Runnable errorColorTimeout = new LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda6(this);
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

        public /* synthetic */ void lambda$new$0() {
            int i = 0;
            this.postedErrorColorTimeout = false;
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i < codeNumberFieldArr.length) {
                    codeNumberFieldArr[i].animateErrorProgress(0.0f);
                    i++;
                } else {
                    return;
                }
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Removed duplicated region for block: B:12:0x00f8  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public LoginActivityRecoverView(Context context) {
            super(context);
            int i;
            CodeNumberField[] codeNumberFieldArr;
            LoginActivity.this = r19;
            setOrientation(1);
            FrameLayout frameLayout = new FrameLayout(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.inboxImageView = rLottieImageView;
            rLottieImageView.setAnimation(2131558559, 120, 120);
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
                    this.titleView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    this.titleView.setText(LocaleController.getString(2131625645));
                    this.titleView.setGravity(17);
                    this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
                    TextView textView2 = new TextView(context);
                    this.confirmTextView = textView2;
                    textView2.setTextSize(1, 14.0f);
                    this.confirmTextView.setGravity(17);
                    this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.confirmTextView.setText(LocaleController.getString(2131628012));
                    addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 12, 8, 12, 0));
                    AnonymousClass1 anonymousClass1 = new AnonymousClass1(context, r19);
                    this.codeFieldContainer = anonymousClass1;
                    anonymousClass1.setNumbersCount(6, 1);
                    for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                        codeNumberField.setShowSoftInputOnFocusCompat(!hasCustomKeyboard() || r19.isCustomKeyboardForceDisabled());
                        codeNumberField.addTextChangedListener(new AnonymousClass2(r19));
                        codeNumberField.setOnFocusChangeListener(new LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda3(this));
                    }
                    addView(this.codeFieldContainer, LayoutHelper.createLinear(-2, 42, 1, 0, 32, 0, 0));
                    SpoilersTextView spoilersTextView = new SpoilersTextView(context);
                    this.troubleButton = spoilersTextView;
                    spoilersTextView.setGravity(17);
                    this.troubleButton.setTextSize(1, 14.0f);
                    this.troubleButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
                    this.troubleButton.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    this.troubleButton.setMaxLines(2);
                    this.troubleButton.setOnClickListener(new LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda2(this));
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
            this.titleView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.titleView.setText(LocaleController.getString(2131625645));
            this.titleView.setGravity(17);
            this.titleView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.titleView, LayoutHelper.createFrame(-1, -2.0f, 1, 32.0f, 16.0f, 32.0f, 0.0f));
            TextView textView22 = new TextView(context);
            this.confirmTextView = textView22;
            textView22.setTextSize(1, 14.0f);
            this.confirmTextView.setGravity(17);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.confirmTextView.setText(LocaleController.getString(2131628012));
            addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 12, 8, 12, 0));
            AnonymousClass1 anonymousClass12 = new AnonymousClass1(context, r19);
            this.codeFieldContainer = anonymousClass12;
            anonymousClass12.setNumbersCount(6, 1);
            while (r12 < r11) {
            }
            addView(this.codeFieldContainer, LayoutHelper.createLinear(-2, 42, 1, 0, 32, 0, 0));
            SpoilersTextView spoilersTextView2 = new SpoilersTextView(context);
            this.troubleButton = spoilersTextView2;
            spoilersTextView2.setGravity(17);
            this.troubleButton.setTextSize(1, 14.0f);
            this.troubleButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.troubleButton.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.troubleButton.setMaxLines(2);
            this.troubleButton.setOnClickListener(new LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda2(this));
            FrameLayout frameLayout22 = new FrameLayout(context);
            frameLayout22.addView(this.troubleButton, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 32.0f));
            addView(frameLayout22, LayoutHelper.createLinear(-1, 0, 1.0f));
            VerticalPositionAutoAnimator.attach(this.troubleButton);
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivityRecoverView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends CodeFieldContainer {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, LoginActivity loginActivity) {
                super(context);
                LoginActivityRecoverView.this = r1;
            }

            @Override // org.telegram.ui.CodeFieldContainer
            protected void processNextPressed() {
                LoginActivityRecoverView.this.onNextPressed(null);
            }
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivityRecoverView$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 implements TextWatcher {
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass2(LoginActivity loginActivity) {
                LoginActivityRecoverView.this = r1;
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (LoginActivityRecoverView.this.postedErrorColorTimeout) {
                    LoginActivityRecoverView loginActivityRecoverView = LoginActivityRecoverView.this;
                    loginActivityRecoverView.removeCallbacks(loginActivityRecoverView.errorColorTimeout);
                    LoginActivityRecoverView.this.errorColorTimeout.run();
                }
            }
        }

        public /* synthetic */ void lambda$new$1(View view, boolean z) {
            if (z) {
                LoginActivity.this.keyboardView.setEditText((EditText) view);
                LoginActivity.this.keyboardView.setDispatchBackWhenEmpty(true);
            }
        }

        public /* synthetic */ void lambda$new$4(View view) {
            Dialog showDialog = LoginActivity.this.showDialog(new AlertDialog.Builder(LoginActivity.this.getParentActivity()).setTitle(LocaleController.getString("RestorePasswordNoEmailTitle", 2131628020)).setMessage(LocaleController.getString("RestoreEmailTroubleText", 2131628016)).setPositiveButton(LocaleController.getString(2131627075), new LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda1(this)).setNegativeButton(LocaleController.getString(2131627981), new LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda0(this)).create());
            if (showDialog != null) {
                showDialog.setCanceledOnTouchOutside(false);
                showDialog.setCancelable(false);
            }
        }

        public /* synthetic */ void lambda$new$2(DialogInterface dialogInterface, int i) {
            LoginActivity.this.setPage(6, true, new Bundle(), true);
        }

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
            return LocaleController.getString("LoginPassword", 2131626504);
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
                textStyleRun.flags |= 256;
                textStyleRun.start = indexOf;
                int i = lastIndexOf + 1;
                textStyleRun.end = i;
                valueOf.setSpan(new TextStyleSpan(textStyleRun), indexOf, i, 0);
            }
            this.troubleButton.setText(AndroidUtilities.formatSpannable(LocaleController.getString(2131628010), valueOf));
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
            AndroidUtilities.shakeViewSpring(this.codeFieldContainer, new LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda7(this));
        }

        public /* synthetic */ void lambda$onPasscodeError$6() {
            postDelayed(new LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda5(this), 150L);
            removeCallbacks(this.errorColorTimeout);
            postDelayed(this.errorColorTimeout, 3000L);
            this.postedErrorColorTimeout = true;
        }

        public /* synthetic */ void lambda$onPasscodeError$5() {
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            int i = 0;
            codeFieldContainer.isFocusSuppressed = false;
            codeFieldContainer.codeField[0].requestFocus();
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
                if (i < codeNumberFieldArr.length) {
                    codeNumberFieldArr[i].animateErrorProgress(0.0f);
                    i++;
                } else {
                    return;
                }
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
            String code = this.codeFieldContainer.getCode();
            if (code.length() == 0) {
                onPasscodeError(false);
                return;
            }
            this.nextPressed = true;
            LoginActivity.this.needShowProgress(0);
            TLRPC$TL_auth_checkRecoveryPassword tLRPC$TL_auth_checkRecoveryPassword = new TLRPC$TL_auth_checkRecoveryPassword();
            tLRPC$TL_auth_checkRecoveryPassword.code = code;
            ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_checkRecoveryPassword, new LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda9(this, code), 10);
        }

        public /* synthetic */ void lambda$onNextPressed$8(String str, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda8(this, tLObject, str, tLRPC$TL_error));
        }

        public /* synthetic */ void lambda$onNextPressed$7(TLObject tLObject, String str, TLRPC$TL_error tLRPC$TL_error) {
            String str2;
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
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), tLRPC$TL_error.text);
            } else {
                int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                if (intValue < 60) {
                    str2 = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                } else {
                    str2 = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                }
                LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.formatString("FloodWaitTime", 2131625909, str2));
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
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityRecoverView$$ExternalSyntheticLambda4(this), LoginActivity.SHOW_DELAY);
        }

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
        private TLRPC$TL_account_password currentPassword;
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

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public LoginActivityNewPasswordView(Context context, int i) {
            super(context);
            LoginActivity.this = r25;
            this.currentStage = i;
            setOrientation(1);
            EditTextBoldCursor[] editTextBoldCursorArr = new EditTextBoldCursor[i == 1 ? 1 : 2];
            this.codeField = editTextBoldCursorArr;
            this.outlineFields = new OutlineTextContainerView[editTextBoldCursorArr.length];
            TextView textView = new TextView(context);
            this.titleTextView = textView;
            float f = 18.0f;
            textView.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.titleTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.titleTextView.setGravity(49);
            this.titleTextView.setText(LocaleController.getString(2131628243));
            addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 8, AndroidUtilities.isSmallScreen() ? 16 : 72, 8, 0));
            TextView textView2 = new TextView(context);
            this.confirmTextView = textView2;
            textView2.setTextSize(1, 16.0f);
            this.confirmTextView.setGravity(1);
            this.confirmTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.confirmTextView, LayoutHelper.createLinear(-2, -2, 1, 8, 6, 8, 16));
            int i2 = 0;
            while (i2 < this.codeField.length) {
                OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
                this.outlineFields[i2] = outlineTextContainerView;
                outlineTextContainerView.setText(LocaleController.getString(i == 0 ? i2 == 0 ? 2131627564 : 2131627566 : 2131627358));
                this.codeField[i2] = new EditTextBoldCursor(context);
                this.codeField[i2].setCursorSize(AndroidUtilities.dp(20.0f));
                this.codeField[i2].setCursorWidth(1.5f);
                this.codeField[i2].setImeOptions(268435461);
                this.codeField[i2].setTextSize(1, f);
                this.codeField[i2].setMaxLines(1);
                this.codeField[i2].setBackground(null);
                int dp = AndroidUtilities.dp(16.0f);
                this.codeField[i2].setPadding(dp, dp, dp, dp);
                if (i == 0) {
                    this.codeField[i2].setInputType(129);
                    this.codeField[i2].setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                this.codeField[i2].setTypeface(Typeface.DEFAULT);
                this.codeField[i2].setGravity(LocaleController.isRTL ? 5 : 3);
                EditTextBoldCursor editTextBoldCursor = this.codeField[i2];
                boolean z = i2 == 0 && i == 0;
                editTextBoldCursor.addTextChangedListener(new AnonymousClass1(r25, z));
                this.codeField[i2].setOnFocusChangeListener(new LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda3(outlineTextContainerView));
                if (z) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(0);
                    linearLayout.setGravity(16);
                    linearLayout.addView(this.codeField[i2], LayoutHelper.createLinear(0, -2, 1.0f));
                    ImageView imageView = new ImageView(context);
                    this.passwordButton = imageView;
                    imageView.setImageResource(2131165800);
                    AndroidUtilities.updateViewVisibilityAnimated(this.passwordButton, true, 0.1f, false);
                    this.passwordButton.setOnClickListener(new LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda1(this));
                    linearLayout.addView(this.passwordButton, LayoutHelper.createLinearRelatively(24.0f, 24.0f, 0, 0.0f, 0.0f, 14.0f, 0.0f));
                    outlineTextContainerView.addView(linearLayout, LayoutHelper.createFrame(-1, -2.0f));
                } else {
                    outlineTextContainerView.addView(this.codeField[i2], LayoutHelper.createFrame(-1, -2.0f));
                }
                outlineTextContainerView.attachEditText(this.codeField[i2]);
                addView(outlineTextContainerView, LayoutHelper.createLinear(-1, -2, 1, 16, 16, 16, 0));
                this.codeField[i2].setOnEditorActionListener(new LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda4(this, i2));
                i2++;
                f = 18.0f;
            }
            if (i == 0) {
                this.confirmTextView.setText(LocaleController.getString("PleaseEnterNewFirstPasswordLogin", 2131627565));
            } else {
                this.confirmTextView.setText(LocaleController.getString("PasswordHintTextLogin", 2131627360));
            }
            TextView textView3 = new TextView(context);
            this.cancelButton = textView3;
            textView3.setGravity(19);
            this.cancelButton.setTextSize(1, 15.0f);
            this.cancelButton.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.cancelButton.setPadding(AndroidUtilities.dp(16.0f), 0, AndroidUtilities.dp(16.0f), 0);
            this.cancelButton.setText(LocaleController.getString(2131629284));
            FrameLayout frameLayout = new FrameLayout(context);
            frameLayout.addView(this.cancelButton, LayoutHelper.createFrame(-1, Build.VERSION.SDK_INT >= 21 ? 56 : 60, 80, 0.0f, 0.0f, 0.0f, 32.0f));
            addView(frameLayout, LayoutHelper.createLinear(-1, -1, 80));
            VerticalPositionAutoAnimator.attach(this.cancelButton);
            this.cancelButton.setOnClickListener(new LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda2(this));
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivityNewPasswordView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 implements TextWatcher {
            final /* synthetic */ boolean val$showPasswordButton;

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            AnonymousClass1(LoginActivity loginActivity, boolean z) {
                LoginActivityNewPasswordView.this = r1;
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
        }

        public static /* synthetic */ void lambda$new$0(OutlineTextContainerView outlineTextContainerView, View view, boolean z) {
            outlineTextContainerView.animateSelection(z ? 1.0f : 0.0f);
        }

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

        public /* synthetic */ void lambda$new$3(View view) {
            if (this.currentStage == 0) {
                recoverPassword(null, null);
            } else {
                recoverPassword(this.newPassword, null);
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void updateColors() {
            String str;
            this.titleTextView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.confirmTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            EditTextBoldCursor[] editTextBoldCursorArr = this.codeField;
            int length = editTextBoldCursorArr.length;
            int i = 0;
            while (true) {
                str = "windowBackgroundWhiteInputFieldActivated";
                if (i >= length) {
                    break;
                }
                EditTextBoldCursor editTextBoldCursor = editTextBoldCursorArr[i];
                editTextBoldCursor.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                editTextBoldCursor.setCursorColor(Theme.getColor(str));
                i++;
            }
            for (OutlineTextContainerView outlineTextContainerView : this.outlineFields) {
                outlineTextContainerView.updateColor();
            }
            this.cancelButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
            ImageView imageView = this.passwordButton;
            if (imageView != null) {
                if (!this.isPasswordVisible) {
                    str = "windowBackgroundWhiteHintText";
                }
                imageView.setColorFilter(Theme.getColor(str));
                this.passwordButton.setBackground(Theme.createSelectorDrawable(LoginActivity.this.getThemedColor("listSelectorSDK21"), 1));
            }
        }

        @Override // org.telegram.ui.Components.SlideView
        public void onCancelPressed() {
            this.nextPressed = false;
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            return LocaleController.getString("NewPassword", 2131626790);
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
                TLRPC$TL_account_password TLdeserialize = TLRPC$TL_account_password.TLdeserialize(serializedData, serializedData.readInt32(false), false);
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
            AndroidUtilities.shakeView(this.codeField[i], 2.0f, 0);
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

        private void recoverPassword(String str, String str2) {
            TLRPC$TL_auth_recoverPassword tLRPC$TL_auth_recoverPassword = new TLRPC$TL_auth_recoverPassword();
            tLRPC$TL_auth_recoverPassword.code = this.emailCode;
            if (!TextUtils.isEmpty(str)) {
                tLRPC$TL_auth_recoverPassword.flags |= 1;
                TLRPC$TL_account_passwordInputSettings tLRPC$TL_account_passwordInputSettings = new TLRPC$TL_account_passwordInputSettings();
                tLRPC$TL_auth_recoverPassword.new_settings = tLRPC$TL_account_passwordInputSettings;
                tLRPC$TL_account_passwordInputSettings.flags |= 1;
                tLRPC$TL_account_passwordInputSettings.hint = str2 != null ? str2 : "";
                tLRPC$TL_account_passwordInputSettings.new_algo = this.currentPassword.new_algo;
            }
            Utilities.globalQueue.postRunnable(new LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda6(this, str, str2, tLRPC$TL_auth_recoverPassword));
        }

        public /* synthetic */ void lambda$recoverPassword$9(String str, String str2, TLRPC$TL_auth_recoverPassword tLRPC$TL_auth_recoverPassword) {
            byte[] stringBytes = str != null ? AndroidUtilities.getStringBytes(str) : null;
            LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda9 loginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda9 = new LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda9(this, str, str2);
            TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = this.currentPassword.new_algo;
            if (tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
                if (str != null) {
                    tLRPC$TL_auth_recoverPassword.new_settings.new_password_hash = SRPHelper.getVBytes(stringBytes, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo);
                    if (tLRPC$TL_auth_recoverPassword.new_settings.new_password_hash == null) {
                        TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
                        tLRPC$TL_error.text = "ALGO_INVALID";
                        loginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda9.run(null, tLRPC$TL_error);
                    }
                }
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_recoverPassword, loginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda9, 10);
                return;
            }
            TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
            tLRPC$TL_error2.text = "PASSWORD_HASH_INVALID";
            loginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda9.run(null, tLRPC$TL_error2);
        }

        public /* synthetic */ void lambda$recoverPassword$8(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda7(this, tLRPC$TL_error, str, str2, tLObject));
        }

        public /* synthetic */ void lambda$recoverPassword$7(TLRPC$TL_error tLRPC$TL_error, String str, String str2, TLObject tLObject) {
            String str3;
            if (tLRPC$TL_error == null || (!"SRP_ID_INVALID".equals(tLRPC$TL_error.text) && !"NEW_SALT_INVALID".equals(tLRPC$TL_error.text))) {
                LoginActivity.this.needHideProgress(false);
                if (tLObject instanceof TLRPC$auth_Authorization) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
                    builder.setPositiveButton(LocaleController.getString(2131625246), new LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda0(this, tLObject));
                    if (TextUtils.isEmpty(str)) {
                        builder.setMessage(LocaleController.getString(2131629299));
                    } else {
                        builder.setMessage(LocaleController.getString(2131629294));
                    }
                    builder.setTitle(LocaleController.getString(2131628708));
                    Dialog showDialog = LoginActivity.this.showDialog(builder.create());
                    if (showDialog == null) {
                        return;
                    }
                    showDialog.setCanceledOnTouchOutside(false);
                    showDialog.setCancelable(false);
                    return;
                } else if (tLRPC$TL_error == null) {
                    return;
                } else {
                    this.nextPressed = false;
                    if (!tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                        LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), tLRPC$TL_error.text);
                        return;
                    }
                    int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                    if (intValue < 60) {
                        str3 = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                    } else {
                        str3 = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                    }
                    LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.formatString("FloodWaitTime", 2131625909, str3));
                    return;
                }
            }
            ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda10(this, str, str2), 8);
        }

        public /* synthetic */ void lambda$recoverPassword$5(String str, String str2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda8(this, tLRPC$TL_error, tLObject, str, str2));
        }

        public /* synthetic */ void lambda$recoverPassword$4(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, String str, String str2) {
            if (tLRPC$TL_error == null) {
                TLRPC$TL_account_password tLRPC$TL_account_password = (TLRPC$TL_account_password) tLObject;
                this.currentPassword = tLRPC$TL_account_password;
                TwoStepVerificationActivity.initPasswordNewAlgo(tLRPC$TL_account_password);
                recoverPassword(str, str2);
            }
        }

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
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityNewPasswordView$$ExternalSyntheticLambda5(this), LoginActivity.SHOW_DELAY);
        }

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
        private RLottieImageView avatarEditor;
        private BackupImageView avatarImage;
        private View avatarOverlay;
        private RadialProgressView avatarProgressView;
        private Bundle currentParams;
        private TextView descriptionTextView;
        private FrameLayout editTextContainer;
        private EditTextBoldCursor firstNameField;
        private OutlineTextContainerView firstNameOutlineView;
        private ImageUpdater imageUpdater;
        private EditTextBoldCursor lastNameField;
        private OutlineTextContainerView lastNameOutlineView;
        private String phoneHash;
        private TextView privacyView;
        private String requestPhone;
        private TextView titleTextView;
        private TextView wrongNumber;
        private boolean nextPressed = false;
        private boolean isCameraWaitAnimationAllowed = true;
        private AvatarDrawable avatarDrawable = new AvatarDrawable();
        private RLottieDrawable cameraDrawable = new RLottieDrawable(2131558413, String.valueOf(2131558413), AndroidUtilities.dp(70.0f), AndroidUtilities.dp(70.0f), false, null);
        private RLottieDrawable cameraWaitDrawable = new RLottieDrawable(2131558416, String.valueOf(2131558416), AndroidUtilities.dp(70.0f), AndroidUtilities.dp(70.0f), false, null);

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public /* synthetic */ void didStartUpload(boolean z) {
            ImageUpdater.ImageUpdaterDelegate.CC.$default$didStartUpload(this, z);
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public /* bridge */ /* synthetic */ String getInitialSearchString() {
            return ImageUpdater.ImageUpdaterDelegate.CC.$default$getInitialSearchString(this);
        }

        @Override // org.telegram.ui.Components.SlideView
        public boolean needBackButton() {
            return true;
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public /* synthetic */ void onUploadProgressChanged(float f) {
            ImageUpdater.ImageUpdaterDelegate.CC.$default$onUploadProgressChanged(this, f);
        }

        /* loaded from: classes3.dex */
        public class LinkSpan extends ClickableSpan {
            public LinkSpan() {
                LoginActivityRegisterView.this = r1;
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

        public void showTermsOfService(boolean z) {
            if (LoginActivity.this.currentTermsOfService == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString("TermsOfService", 2131628593));
            if (z) {
                builder.setPositiveButton(LocaleController.getString("Accept", 2131624120), new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda0(this));
                builder.setNegativeButton(LocaleController.getString("Decline", 2131625363), new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda3(this));
            } else {
                builder.setPositiveButton(LocaleController.getString("OK", 2131627075), null);
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LoginActivity.this.currentTermsOfService.text);
            MessageObject.addEntitiesToText(spannableStringBuilder, LoginActivity.this.currentTermsOfService.entities, false, false, false, false);
            builder.setMessage(spannableStringBuilder);
            LoginActivity.this.showDialog(builder.create());
        }

        public /* synthetic */ void lambda$showTermsOfService$0(DialogInterface dialogInterface, int i) {
            LoginActivity.this.currentTermsOfService.popup = false;
            onNextPressed(null);
        }

        public /* synthetic */ void lambda$showTermsOfService$3(DialogInterface dialogInterface, int i) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this.getParentActivity());
            builder.setTitle(LocaleController.getString("TermsOfService", 2131628593));
            builder.setMessage(LocaleController.getString("TosDecline", 2131628684));
            builder.setPositiveButton(LocaleController.getString("SignUp", 2131628355), new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda4(this));
            builder.setNegativeButton(LocaleController.getString("Decline", 2131625363), new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda1(this));
            LoginActivity.this.showDialog(builder.create());
        }

        public /* synthetic */ void lambda$showTermsOfService$1(DialogInterface dialogInterface, int i) {
            LoginActivity.this.currentTermsOfService.popup = false;
            onNextPressed(null);
        }

        public /* synthetic */ void lambda$showTermsOfService$2(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            LoginActivity.this.setPage(0, true, null, true);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public LoginActivityRegisterView(Context context) {
            super(context);
            LoginActivity.this = r26;
            setOrientation(1);
            ImageUpdater imageUpdater = new ImageUpdater(false);
            this.imageUpdater = imageUpdater;
            imageUpdater.setOpenWithFrontfaceCamera(true);
            this.imageUpdater.setSearchAvailable(false);
            this.imageUpdater.setUploadAfterSelect(false);
            ImageUpdater imageUpdater2 = this.imageUpdater;
            imageUpdater2.parentFragment = r26;
            imageUpdater2.setDelegate(this);
            FrameLayout frameLayout = new FrameLayout(context);
            addView(frameLayout, LayoutHelper.createLinear(78, 78, 1));
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(context, r26);
            this.avatarImage = anonymousClass1;
            anonymousClass1.setRoundRadius(AndroidUtilities.dp(64.0f));
            this.avatarDrawable.setAvatarType(13);
            this.avatarDrawable.setInfo(5L, null, null);
            this.avatarImage.setImageDrawable(this.avatarDrawable);
            frameLayout.addView(this.avatarImage, LayoutHelper.createFrame(-1, -1.0f));
            Paint paint = new Paint(1);
            paint.setColor(1426063360);
            AnonymousClass2 anonymousClass2 = new AnonymousClass2(context, r26, paint);
            this.avatarOverlay = anonymousClass2;
            frameLayout.addView(anonymousClass2, LayoutHelper.createFrame(-1, -1.0f));
            this.avatarOverlay.setOnClickListener(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda6(this));
            AnonymousClass3 anonymousClass3 = new AnonymousClass3(context, r26);
            this.avatarEditor = anonymousClass3;
            anonymousClass3.setScaleType(ImageView.ScaleType.CENTER);
            this.avatarEditor.setAnimation(this.cameraDrawable);
            this.avatarEditor.setEnabled(false);
            this.avatarEditor.setClickable(false);
            frameLayout.addView(this.avatarEditor, LayoutHelper.createFrame(-1, -1.0f));
            this.avatarEditor.addOnAttachStateChangeListener(new AnonymousClass4(r26));
            AnonymousClass5 anonymousClass5 = new AnonymousClass5(context, r26);
            this.avatarProgressView = anonymousClass5;
            anonymousClass5.setSize(AndroidUtilities.dp(30.0f));
            this.avatarProgressView.setProgressColor(-1);
            frameLayout.addView(this.avatarProgressView, LayoutHelper.createFrame(-1, -1.0f));
            showAvatarProgress(false, false);
            TextView textView = new TextView(context);
            this.titleTextView = textView;
            textView.setText(LocaleController.getString(2131627880));
            this.titleTextView.setTextSize(1, 18.0f);
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.titleTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.titleTextView.setGravity(1);
            addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 8, 12, 8, 0));
            TextView textView2 = new TextView(context);
            this.descriptionTextView = textView2;
            textView2.setText(LocaleController.getString("RegisterText2", 2131627879));
            this.descriptionTextView.setGravity(1);
            float f = 14.0f;
            this.descriptionTextView.setTextSize(1, 14.0f);
            this.descriptionTextView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            addView(this.descriptionTextView, LayoutHelper.createLinear(-2, -2, 1, 8, 6, 8, 0));
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.editTextContainer = frameLayout2;
            addView(frameLayout2, LayoutHelper.createLinear(-1, -2, 8.0f, 21.0f, 8.0f, 0.0f));
            OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
            this.firstNameOutlineView = outlineTextContainerView;
            outlineTextContainerView.setText(LocaleController.getString(2131625906));
            EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
            this.firstNameField = editTextBoldCursor;
            editTextBoldCursor.setCursorSize(AndroidUtilities.dp(20.0f));
            this.firstNameField.setCursorWidth(1.5f);
            this.firstNameField.setImeOptions(268435461);
            this.firstNameField.setTextSize(1, 17.0f);
            this.firstNameField.setMaxLines(1);
            this.firstNameField.setInputType(8192);
            this.firstNameField.setOnFocusChangeListener(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda9(this));
            this.firstNameField.setBackground(null);
            this.firstNameField.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.firstNameOutlineView.attachEditText(this.firstNameField);
            this.firstNameOutlineView.addView(this.firstNameField, LayoutHelper.createFrame(-1, -2, 48));
            this.firstNameField.setOnEditorActionListener(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda10(this));
            OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
            this.lastNameOutlineView = outlineTextContainerView2;
            outlineTextContainerView2.setText(LocaleController.getString(2131626368));
            EditTextBoldCursor editTextBoldCursor2 = new EditTextBoldCursor(context);
            this.lastNameField = editTextBoldCursor2;
            editTextBoldCursor2.setCursorSize(AndroidUtilities.dp(20.0f));
            this.lastNameField.setCursorWidth(1.5f);
            this.lastNameField.setImeOptions(268435462);
            this.lastNameField.setTextSize(1, 17.0f);
            this.lastNameField.setMaxLines(1);
            this.lastNameField.setInputType(8192);
            this.lastNameField.setOnFocusChangeListener(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda8(this));
            this.lastNameField.setBackground(null);
            this.lastNameField.setPadding(AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
            this.lastNameOutlineView.attachEditText(this.lastNameField);
            this.lastNameOutlineView.addView(this.lastNameField, LayoutHelper.createFrame(-1, -2, 48));
            this.lastNameField.setOnEditorActionListener(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda11(this));
            buildEditTextLayout(AndroidUtilities.isSmallScreen());
            TextView textView3 = new TextView(context);
            this.wrongNumber = textView3;
            textView3.setText(LocaleController.getString("CancelRegistration", 2131624838));
            int i = 5;
            this.wrongNumber.setGravity((LocaleController.isRTL ? 5 : 3) | 1);
            this.wrongNumber.setTextSize(1, 14.0f);
            this.wrongNumber.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.wrongNumber.setPadding(0, AndroidUtilities.dp(24.0f), 0, 0);
            this.wrongNumber.setVisibility(8);
            addView(this.wrongNumber, LayoutHelper.createLinear(-2, -2, (!LocaleController.isRTL ? 3 : i) | 48, 0, 20, 0, 0));
            this.wrongNumber.setOnClickListener(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda7(this));
            FrameLayout frameLayout3 = new FrameLayout(context);
            addView(frameLayout3, LayoutHelper.createLinear(-1, -1, 83));
            TextView textView4 = new TextView(context);
            this.privacyView = textView4;
            textView4.setMovementMethod(new AndroidUtilities.LinkMovementMethodMy());
            this.privacyView.setTextSize(1, AndroidUtilities.isSmallScreen() ? 13.0f : f);
            this.privacyView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
            this.privacyView.setGravity(16);
            frameLayout3.addView(this.privacyView, LayoutHelper.createFrame(-2, Build.VERSION.SDK_INT >= 21 ? 56.0f : 60.0f, 83, 14.0f, 0.0f, 70.0f, 32.0f));
            VerticalPositionAutoAnimator.attach(this.privacyView);
            String string = LocaleController.getString("TermsOfServiceLogin", 2131628594);
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

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivityRegisterView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends BackupImageView {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, LoginActivity loginActivity) {
                super(context);
                LoginActivityRegisterView.this = r1;
            }

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
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivityRegisterView$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends View {
            final /* synthetic */ Paint val$paint;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass2(Context context, LoginActivity loginActivity, Paint paint) {
                super(context);
                LoginActivityRegisterView.this = r1;
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
        }

        public /* synthetic */ void lambda$new$7(View view) {
            this.imageUpdater.openMenu(this.avatar != null, new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda13(this), new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda5(this));
            this.isCameraWaitAnimationAllowed = false;
            this.avatarEditor.setAnimation(this.cameraDrawable);
            this.cameraDrawable.setCurrentFrame(0);
            this.cameraDrawable.setCustomEndFrame(43);
            this.avatarEditor.playAnimation();
        }

        public /* synthetic */ void lambda$new$4() {
            this.avatar = null;
            this.avatarBig = null;
            showAvatarProgress(false, true);
            this.avatarImage.setImage((ImageLocation) null, (String) null, this.avatarDrawable, (Object) null);
            this.avatarEditor.setAnimation(this.cameraDrawable);
            this.cameraDrawable.setCurrentFrame(0);
            this.isCameraWaitAnimationAllowed = true;
        }

        public /* synthetic */ void lambda$new$6(DialogInterface dialogInterface) {
            if (!this.imageUpdater.isUploadingImage()) {
                this.avatarEditor.setAnimation(this.cameraDrawable);
                this.cameraDrawable.setCustomEndFrame(86);
                this.avatarEditor.setOnAnimationEndListener(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda14(this));
                this.avatarEditor.playAnimation();
                return;
            }
            this.avatarEditor.setAnimation(this.cameraDrawable);
            this.cameraDrawable.setCurrentFrame(0, false);
            this.isCameraWaitAnimationAllowed = true;
        }

        public /* synthetic */ void lambda$new$5() {
            this.isCameraWaitAnimationAllowed = true;
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivityRegisterView$3 */
        /* loaded from: classes3.dex */
        public class AnonymousClass3 extends RLottieImageView {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass3(Context context, LoginActivity loginActivity) {
                super(context);
                LoginActivityRegisterView.this = r1;
            }

            @Override // android.view.View
            public void invalidate(int i, int i2, int i3, int i4) {
                super.invalidate(i, i2, i3, i4);
                LoginActivityRegisterView.this.avatarOverlay.invalidate();
            }

            @Override // android.view.View
            public void invalidate() {
                super.invalidate();
                LoginActivityRegisterView.this.avatarOverlay.invalidate();
            }
        }

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivityRegisterView$4 */
        /* loaded from: classes3.dex */
        public class AnonymousClass4 implements View.OnAttachStateChangeListener {
            private boolean isAttached;
            private long lastRun = System.currentTimeMillis();
            private Runnable cameraWaitCallback = new LoginActivity$LoginActivityRegisterView$4$$ExternalSyntheticLambda2(this);

            AnonymousClass4(LoginActivity loginActivity) {
                LoginActivityRegisterView.this = r1;
            }

            public /* synthetic */ void lambda$$2() {
                if (this.isAttached) {
                    if (LoginActivityRegisterView.this.isCameraWaitAnimationAllowed && System.currentTimeMillis() - this.lastRun >= 10000) {
                        LoginActivityRegisterView.this.avatarEditor.setAnimation(LoginActivityRegisterView.this.cameraWaitDrawable);
                        LoginActivityRegisterView.this.cameraWaitDrawable.setCurrentFrame(0, false);
                        LoginActivityRegisterView.this.cameraWaitDrawable.setOnAnimationEndListener(new LoginActivity$LoginActivityRegisterView$4$$ExternalSyntheticLambda1(this));
                        LoginActivityRegisterView.this.avatarEditor.playAnimation();
                        this.lastRun = System.currentTimeMillis();
                    }
                    LoginActivityRegisterView.this.avatarEditor.postDelayed(this.cameraWaitCallback, 1000L);
                }
            }

            public /* synthetic */ void lambda$$1() {
                AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityRegisterView$4$$ExternalSyntheticLambda0(this));
            }

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

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivityRegisterView$5 */
        /* loaded from: classes3.dex */
        public class AnonymousClass5 extends RadialProgressView {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass5(Context context, LoginActivity loginActivity) {
                super(context);
                LoginActivityRegisterView.this = r1;
            }

            @Override // org.telegram.ui.Components.RadialProgressView, android.view.View
            public void setAlpha(float f) {
                super.setAlpha(f);
                LoginActivityRegisterView.this.avatarOverlay.invalidate();
            }
        }

        public /* synthetic */ void lambda$new$8(View view, boolean z) {
            this.firstNameOutlineView.animateSelection(z ? 1.0f : 0.0f);
        }

        public /* synthetic */ boolean lambda$new$9(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 5) {
                this.lastNameField.requestFocus();
                return true;
            }
            return false;
        }

        public /* synthetic */ void lambda$new$10(View view, boolean z) {
            this.lastNameOutlineView.animateSelection(z ? 1.0f : 0.0f);
        }

        public /* synthetic */ boolean lambda$new$11(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 6 || i == 5) {
                onNextPressed(null);
                return true;
            }
            return false;
        }

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
                this.firstNameOutlineView.setText(LocaleController.getString(2131625907));
                this.lastNameOutlineView.setText(LocaleController.getString(2131626369));
                linearLayout.addView(this.firstNameOutlineView, LayoutHelper.createLinear(0, -2, 1.0f, 0, 0, 8, 0));
                linearLayout.addView(this.lastNameOutlineView, LayoutHelper.createLinear(0, -2, 1.0f, 8, 0, 0, 0));
                this.editTextContainer.addView(linearLayout);
                if (hasFocus) {
                    this.firstNameField.requestFocus();
                    AndroidUtilities.showKeyboard(this.firstNameField);
                    return;
                } else if (!hasFocus2) {
                    return;
                } else {
                    this.lastNameField.requestFocus();
                    AndroidUtilities.showKeyboard(this.lastNameField);
                    return;
                }
            }
            this.firstNameOutlineView.setText(LocaleController.getString(2131625906));
            this.lastNameOutlineView.setText(LocaleController.getString(2131626368));
            this.editTextContainer.addView(this.firstNameOutlineView, LayoutHelper.createFrame(-1, -2.0f, 48, 8.0f, 0.0f, 8.0f, 0.0f));
            this.editTextContainer.addView(this.lastNameOutlineView, LayoutHelper.createFrame(-1, -2.0f, 48, 8.0f, 82.0f, 8.0f, 0.0f));
        }

        @Override // org.telegram.ui.Components.ImageUpdater.ImageUpdaterDelegate
        public void didUploadPhoto(TLRPC$InputFile tLRPC$InputFile, TLRPC$InputFile tLRPC$InputFile2, double d, String str, TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda18(this, tLRPC$PhotoSize2, tLRPC$PhotoSize));
        }

        public /* synthetic */ void lambda$didUploadPhoto$13(TLRPC$PhotoSize tLRPC$PhotoSize, TLRPC$PhotoSize tLRPC$PhotoSize2) {
            TLRPC$FileLocation tLRPC$FileLocation = tLRPC$PhotoSize.location;
            this.avatar = tLRPC$FileLocation;
            this.avatarBig = tLRPC$PhotoSize2.location;
            this.avatarImage.setImage(ImageLocation.getForLocal(tLRPC$FileLocation), "50_50", this.avatarDrawable, (Object) null);
        }

        private void showAvatarProgress(boolean z, boolean z2) {
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
                this.avatarAnimation.addListener(new AnonymousClass6(z));
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

        /* renamed from: org.telegram.ui.LoginActivity$LoginActivityRegisterView$6 */
        /* loaded from: classes3.dex */
        public class AnonymousClass6 extends AnimatorListenerAdapter {
            final /* synthetic */ boolean val$show;

            AnonymousClass6(boolean z) {
                LoginActivityRegisterView.this = r1;
                this.val$show = z;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (LoginActivityRegisterView.this.avatarAnimation == null || LoginActivityRegisterView.this.avatarEditor == null) {
                    return;
                }
                if (this.val$show) {
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
            builder.setTitle(LocaleController.getString(2131629224));
            builder.setMessage(LocaleController.getString("AreYouSureRegistration", 2131624460));
            builder.setNegativeButton(LocaleController.getString("Stop", 2131628463), new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda2(this));
            builder.setPositiveButton(LocaleController.getString("Continue", 2131625246), null);
            LoginActivity.this.showDialog(builder.create());
            return false;
        }

        public /* synthetic */ void lambda$onBackPressed$14(DialogInterface dialogInterface, int i) {
            onBackPressed(true);
            LoginActivity.this.setPage(0, true, null, true);
            hidePrivacyView();
        }

        @Override // org.telegram.ui.Components.SlideView
        public String getHeaderName() {
            return LocaleController.getString("YourName", 2131629290);
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
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda12(this), LoginActivity.SHOW_DELAY);
        }

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
                ConnectionsManager.getInstance(((BaseFragment) LoginActivity.this).currentAccount).sendRequest(tLRPC$TL_auth_signUp, new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda19(this), 10);
            }
        }

        public /* synthetic */ void lambda$onNextPressed$19(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            AndroidUtilities.runOnUIThread(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda16(this, tLObject, tLRPC$TL_error));
        }

        public /* synthetic */ void lambda$onNextPressed$18(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
            this.nextPressed = false;
            if (!(tLObject instanceof TLRPC$TL_auth_authorization)) {
                LoginActivity.this.needHideProgress(false);
                if (tLRPC$TL_error.text.contains("PHONE_NUMBER_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("InvalidPhoneNumber", 2131626250));
                    return;
                } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EMPTY") || tLRPC$TL_error.text.contains("PHONE_CODE_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("InvalidCode", 2131626246));
                    return;
                } else if (tLRPC$TL_error.text.contains("PHONE_CODE_EXPIRED")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("CodeExpired", 2131625171));
                    return;
                } else if (tLRPC$TL_error.text.contains("FIRSTNAME_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("InvalidFirstName", 2131626247));
                    return;
                } else if (tLRPC$TL_error.text.contains("LASTNAME_INVALID")) {
                    LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("InvalidLastName", 2131626249));
                    return;
                } else {
                    LoginActivity.this.needShowAlert(LocaleController.getString(2131628020), tLRPC$TL_error.text);
                    return;
                }
            }
            hidePrivacyView();
            LoginActivity.this.showDoneButton(false, true);
            postDelayed(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda15(this, tLObject), 150L);
        }

        public /* synthetic */ void lambda$onNextPressed$17(TLObject tLObject) {
            LoginActivity.this.needHideProgress(false, false);
            AndroidUtilities.hideKeyboard(((BaseFragment) LoginActivity.this).fragmentView.findFocus());
            LoginActivity.this.onAuthSuccess((TLRPC$TL_auth_authorization) tLObject, true);
            TLRPC$FileLocation tLRPC$FileLocation = this.avatarBig;
            if (tLRPC$FileLocation != null) {
                Utilities.cacheClearQueue.postRunnable(new LoginActivity$LoginActivityRegisterView$$ExternalSyntheticLambda17(this, tLRPC$FileLocation));
            }
        }

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

    public boolean showKeyboard(View view) {
        if (!isCustomKeyboardVisible()) {
            return AndroidUtilities.showKeyboard(view);
        }
        return true;
    }

    public LoginActivity setIntroView(View view, TextView textView) {
        this.introView = view;
        this.startMessagingButton = textView;
        this.isAnimatingIntro = true;
        return this;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public AnimatorSet onCustomTransitionAnimation(boolean z, Runnable runnable) {
        if (!z || this.introView == null) {
            return null;
        }
        TransformableLoginButtonView transformableLoginButtonView = new TransformableLoginButtonView(this.fragmentView.getContext());
        transformableLoginButtonView.setButtonText(this.startMessagingButton.getPaint(), this.startMessagingButton.getText().toString());
        int width = this.startMessagingButton.getWidth();
        int height = this.startMessagingButton.getHeight();
        int i = this.floatingButtonIcon.getLayoutParams().width;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        transformableLoginButtonView.setLayoutParams(layoutParams);
        int[] iArr = new int[2];
        this.fragmentView.getLocationInWindow(iArr);
        int i2 = iArr[0];
        int i3 = iArr[1];
        this.startMessagingButton.getLocationInWindow(iArr);
        float f = iArr[0] - i2;
        float f2 = iArr[1] - i3;
        transformableLoginButtonView.setTranslationX(f);
        transformableLoginButtonView.setTranslationY(f2);
        int width2 = (((getParentLayout().getWidth() - this.floatingButtonIcon.getLayoutParams().width) - ((ViewGroup.MarginLayoutParams) this.floatingButtonContainer.getLayoutParams()).rightMargin) - getParentLayout().getPaddingLeft()) - getParentLayout().getPaddingRight();
        int height2 = ((((getParentLayout().getHeight() - this.floatingButtonIcon.getLayoutParams().height) - ((ViewGroup.MarginLayoutParams) this.floatingButtonContainer.getLayoutParams()).bottomMargin) - (isCustomKeyboardVisible() ? AndroidUtilities.dp(230.0f) : 0)) - getParentLayout().getPaddingTop()) - getParentLayout().getPaddingBottom();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addListener(new AnonymousClass12(transformableLoginButtonView, runnable));
        int color = Theme.getColor("windowBackgroundWhite");
        ofFloat.addUpdateListener(new LoginActivity$$ExternalSyntheticLambda4(this, color, Color.alpha(color), layoutParams, width, i, height, transformableLoginButtonView, f, width2, f2, height2));
        ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300L);
        animatorSet.playTogether(ofFloat);
        animatorSet.start();
        return animatorSet;
    }

    /* renamed from: org.telegram.ui.LoginActivity$12 */
    /* loaded from: classes3.dex */
    class AnonymousClass12 extends AnimatorListenerAdapter {
        final /* synthetic */ Runnable val$callback;
        final /* synthetic */ TransformableLoginButtonView val$transformButton;

        AnonymousClass12(TransformableLoginButtonView transformableLoginButtonView, Runnable runnable) {
            LoginActivity.this = r1;
            this.val$transformButton = transformableLoginButtonView;
            this.val$callback = runnable;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            LoginActivity.this.floatingButtonContainer.setVisibility(4);
            LoginActivity.this.keyboardLinearLayout.setAlpha(0.0f);
            ((BaseFragment) LoginActivity.this).fragmentView.setBackgroundColor(0);
            LoginActivity.this.startMessagingButton.setVisibility(4);
            ((FrameLayout) ((BaseFragment) LoginActivity.this).fragmentView).addView(this.val$transformButton);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            LoginActivity.this.keyboardLinearLayout.setAlpha(1.0f);
            LoginActivity.this.startMessagingButton.setVisibility(0);
            ((BaseFragment) LoginActivity.this).fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            LoginActivity.this.floatingButtonContainer.setVisibility(0);
            ((FrameLayout) ((BaseFragment) LoginActivity.this).fragmentView).removeView(this.val$transformButton);
            if (LoginActivity.this.animationFinishCallback != null) {
                AndroidUtilities.runOnUIThread(LoginActivity.this.animationFinishCallback);
                LoginActivity.this.animationFinishCallback = null;
            }
            LoginActivity.this.isAnimatingIntro = false;
            this.val$callback.run();
        }
    }

    public /* synthetic */ void lambda$onCustomTransitionAnimation$17(int i, int i2, ViewGroup.MarginLayoutParams marginLayoutParams, int i3, int i4, int i5, TransformableLoginButtonView transformableLoginButtonView, float f, int i6, float f2, int i7, ValueAnimator valueAnimator) {
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

    public void updateColors() {
        Activity parentActivity = getParentActivity();
        Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
        if (Build.VERSION.SDK_INT < 21) {
            Drawable mutate = parentActivity.getResources().getDrawable(2131165414).mutate();
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
        return SimpleThemeDescription.createThemeDescriptions(new LoginActivity$$ExternalSyntheticLambda20(this), "windowBackgroundWhiteBlackText", "windowBackgroundWhiteGrayText6", "windowBackgroundWhiteHintText", "listSelectorSDK21", "chats_actionBackground", "chats_actionIcon", "windowBackgroundWhiteInputField", "windowBackgroundWhiteInputFieldActivated", "windowBackgroundWhiteValueText", "dialogTextRed", "windowBackgroundWhiteGrayText", "checkbox", "windowBackgroundWhiteBlueText4", "changephoneinfo_image2", "chats_actionPressedBackground", "windowBackgroundWhiteRedText2", "windowBackgroundWhiteLinkText", "checkboxSquareUnchecked", "checkboxSquareBackground", "checkboxSquareCheck", "dialogBackground", "dialogTextGray2", "dialogTextBlack");
    }

    public void tryResetAccount(String str, String str2, String str3) {
        if (this.radialProgressView.getTag() != null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setMessage(LocaleController.getString("ResetMyAccountWarningText", 2131628001));
        builder.setTitle(LocaleController.getString("ResetMyAccountWarning", 2131627999));
        builder.setPositiveButton(LocaleController.getString("ResetMyAccountWarningReset", 2131628000), new LoginActivity$$ExternalSyntheticLambda7(this, str, str2, str3));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131624819), null);
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$tryResetAccount$20(String str, String str2, String str3, DialogInterface dialogInterface, int i) {
        needShowProgress(0);
        TLRPC$TL_account_deleteAccount tLRPC$TL_account_deleteAccount = new TLRPC$TL_account_deleteAccount();
        tLRPC$TL_account_deleteAccount.reason = "Forgot password";
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_deleteAccount, new LoginActivity$$ExternalSyntheticLambda19(this, str, str2, str3), 10);
    }

    public /* synthetic */ void lambda$tryResetAccount$19(String str, String str2, String str3, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new LoginActivity$$ExternalSyntheticLambda17(this, tLRPC$TL_error, str, str2, str3));
    }

    public /* synthetic */ void lambda$tryResetAccount$18(TLRPC$TL_error tLRPC$TL_error, String str, String str2, String str3) {
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
            needShowAlert(LocaleController.getString(2131628020), LocaleController.getString("ResetAccountCancelledAlert", 2131627983));
        } else if (tLRPC$TL_error.text.startsWith("2FA_CONFIRM_WAIT_")) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("phoneFormated", str);
            bundle2.putString("phoneHash", str2);
            bundle2.putString("code", str3);
            bundle2.putInt("startTime", ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
            bundle2.putInt("waitTime", Utilities.parseInt((CharSequence) tLRPC$TL_error.text.replace("2FA_CONFIRM_WAIT_", "")).intValue());
            setPage(8, true, bundle2, false);
        } else {
            needShowAlert(LocaleController.getString(2131628020), tLRPC$TL_error.text);
        }
    }

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

        /* loaded from: classes3.dex */
        public interface IConfirmDialogCallback {
            void onConfirmPressed(PhoneNumberConfirmView phoneNumberConfirmView, TextView textView);

            void onDismiss(PhoneNumberConfirmView phoneNumberConfirmView);

            void onEditPressed(PhoneNumberConfirmView phoneNumberConfirmView, TextView textView);

            void onFabPressed(PhoneNumberConfirmView phoneNumberConfirmView, TransformableLoginButtonView transformableLoginButtonView);
        }

        /* synthetic */ PhoneNumberConfirmView(Context context, ViewGroup viewGroup, View view, String str, IConfirmDialogCallback iConfirmDialogCallback, AnonymousClass1 anonymousClass1) {
            this(context, viewGroup, view, str, iConfirmDialogCallback);
        }

        private PhoneNumberConfirmView(Context context, ViewGroup viewGroup, View view, String str, IConfirmDialogCallback iConfirmDialogCallback) {
            super(context);
            this.fragmentView = viewGroup;
            this.fabContainer = view;
            this.callback = iConfirmDialogCallback;
            View view2 = new View(getContext());
            this.blurredView = view2;
            view2.setOnClickListener(new LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda3(this));
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
            this.popupFabContainer.setOnClickListener(new LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda6(this, iConfirmDialogCallback));
            RadialProgressView radialProgressView = new RadialProgressView(context);
            this.floatingProgressView = radialProgressView;
            radialProgressView.setSize(AndroidUtilities.dp(22.0f));
            this.floatingProgressView.setAlpha(0.0f);
            this.floatingProgressView.setScaleX(0.1f);
            this.floatingProgressView.setScaleY(0.1f);
            this.popupFabContainer.addView(this.floatingProgressView, LayoutHelper.createFrame(-1, -1.0f));
            this.popupFabContainer.setContentDescription(LocaleController.getString(2131625525));
            View view4 = this.popupFabContainer;
            int i = Build.VERSION.SDK_INT;
            addView(view4, LayoutHelper.createFrame(i >= 21 ? 56 : 60, i >= 21 ? 56.0f : 60.0f));
            FrameLayout frameLayout2 = new FrameLayout(context);
            this.popupLayout = frameLayout2;
            addView(frameLayout2, LayoutHelper.createFrame(-1, 140.0f, 49, 24.0f, 0.0f, 24.0f, 0.0f));
            TextView textView = new TextView(context);
            this.confirmMessageView = textView;
            textView.setText(LocaleController.getString(2131625219));
            this.confirmMessageView.setTextSize(1, 14.0f);
            this.confirmMessageView.setSingleLine();
            int i2 = 5;
            this.popupLayout.addView(this.confirmMessageView, LayoutHelper.createFrame(-1, -2.0f, LocaleController.isRTL ? 5 : 3, 24.0f, 20.0f, 24.0f, 0.0f));
            TextView textView2 = new TextView(context);
            this.numberView = textView2;
            textView2.setText(str);
            this.numberView.setTextSize(1, 18.0f);
            this.numberView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.numberView.setSingleLine();
            this.popupLayout.addView(this.numberView, LayoutHelper.createFrame(-1, -2.0f, LocaleController.isRTL ? 5 : 3, 24.0f, 48.0f, 24.0f, 0.0f));
            int dp = AndroidUtilities.dp(16.0f);
            TextView textView3 = new TextView(context);
            this.editTextView = textView3;
            textView3.setText(LocaleController.getString(2131625537));
            this.editTextView.setSingleLine();
            this.editTextView.setTextSize(1, 16.0f);
            this.editTextView.setBackground(Theme.getRoundRectSelectorDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("changephoneinfo_image2")));
            this.editTextView.setOnClickListener(new LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda4(this, iConfirmDialogCallback));
            this.editTextView.setTypeface(Typeface.DEFAULT_BOLD);
            int i3 = dp / 2;
            this.editTextView.setPadding(dp, i3, dp, i3);
            float f = 8;
            this.popupLayout.addView(this.editTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 5 : 3) | 80, f, f, f, f));
            TextView textView4 = new TextView(context);
            this.confirmTextView = textView4;
            textView4.setText(LocaleController.getString(2131625102));
            this.confirmTextView.setSingleLine();
            this.confirmTextView.setTextSize(1, 16.0f);
            this.confirmTextView.setBackground(Theme.getRoundRectSelectorDrawable(AndroidUtilities.dp(6.0f), Theme.getColor("changephoneinfo_image2")));
            this.confirmTextView.setOnClickListener(new LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda5(this, iConfirmDialogCallback));
            this.confirmTextView.setTypeface(Typeface.DEFAULT_BOLD);
            this.confirmTextView.setPadding(dp, i3, dp, i3);
            this.popupLayout.addView(this.confirmTextView, LayoutHelper.createFrame(-2, -2.0f, (LocaleController.isRTL ? 3 : i2) | 80, f, f, f, f));
            updateFabPosition();
            updateColors();
        }

        public /* synthetic */ void lambda$new$0(View view) {
            dismiss();
        }

        public /* synthetic */ void lambda$new$1(IConfirmDialogCallback iConfirmDialogCallback, View view) {
            iConfirmDialogCallback.onFabPressed(this, this.fabTransform);
        }

        public /* synthetic */ void lambda$new$2(IConfirmDialogCallback iConfirmDialogCallback, View view) {
            iConfirmDialogCallback.onEditPressed(this, this.editTextView);
        }

        public /* synthetic */ void lambda$new$3(IConfirmDialogCallback iConfirmDialogCallback, View view) {
            iConfirmDialogCallback.onConfirmPressed(this, this.confirmTextView);
        }

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

        public void show() {
            if (Build.VERSION.SDK_INT >= 21) {
                View view = this.fabContainer;
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Z, view.getTranslationZ(), 0.0f).setDuration(150L).start();
            }
            ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(250L);
            duration.addListener(new AnonymousClass1());
            duration.addUpdateListener(new LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda1(this));
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.start();
        }

        /* renamed from: org.telegram.ui.LoginActivity$PhoneNumberConfirmView$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends AnimatorListenerAdapter {
            AnonymousClass1() {
                PhoneNumberConfirmView.this = r1;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                PhoneNumberConfirmView.this.fabContainer.setVisibility(8);
                int measuredWidth = (int) (PhoneNumberConfirmView.this.fragmentView.getMeasuredWidth() / 10.0f);
                int measuredHeight = (int) (PhoneNumberConfirmView.this.fragmentView.getMeasuredHeight() / 10.0f);
                Bitmap createBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                canvas.scale(0.1f, 0.1f);
                PhoneNumberConfirmView.this.fragmentView.draw(canvas);
                Utilities.stackBlurBitmap(createBitmap, Math.max(8, Math.max(measuredWidth, measuredHeight) / 150));
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
        }

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

        /* renamed from: org.telegram.ui.LoginActivity$PhoneNumberConfirmView$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends AnimatorListenerAdapter {
            final /* synthetic */ Runnable val$callback;

            AnonymousClass2(PhoneNumberConfirmView phoneNumberConfirmView, Runnable runnable) {
                this.val$callback = runnable;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                this.val$callback.run();
            }
        }

        public void animateProgress(Runnable runnable) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addListener(new AnonymousClass2(this, runnable));
            ofFloat.addUpdateListener(new LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda2(this));
            ofFloat.setDuration(150L);
            ofFloat.start();
        }

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

        public void dismiss() {
            if (this.dismissed) {
                return;
            }
            this.dismissed = true;
            this.callback.onDismiss(this);
            ValueAnimator duration = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(250L);
            duration.addListener(new AnonymousClass3());
            duration.addUpdateListener(new LoginActivity$PhoneNumberConfirmView$$ExternalSyntheticLambda0(this));
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.start();
        }

        /* renamed from: org.telegram.ui.LoginActivity$PhoneNumberConfirmView$3 */
        /* loaded from: classes3.dex */
        public class AnonymousClass3 extends AnimatorListenerAdapter {
            AnonymousClass3() {
                PhoneNumberConfirmView.this = r1;
            }

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
        }

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

    /* loaded from: classes3.dex */
    public static final class PhoneInputData {
        private CountrySelectActivity.Country country;
        private List<String> patterns;
        private String phoneNumber;

        private PhoneInputData() {
        }

        /* synthetic */ PhoneInputData(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return ColorUtils.calculateLuminance(Theme.getColor("windowBackgroundWhite", null, true)) > 0.699999988079071d;
    }
}
