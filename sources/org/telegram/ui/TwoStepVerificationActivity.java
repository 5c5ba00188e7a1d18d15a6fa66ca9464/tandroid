package org.telegram.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$InputCheckPasswordSRP;
import org.telegram.tgnet.TLRPC$PasswordKdfAlgo;
import org.telegram.tgnet.TLRPC$SecurePasswordKdfAlgo;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_getPasswordSettings;
import org.telegram.tgnet.TLRPC$TL_account_passwordInputSettings;
import org.telegram.tgnet.TLRPC$TL_account_passwordSettings;
import org.telegram.tgnet.TLRPC$TL_account_resetPasswordFailedWait;
import org.telegram.tgnet.TLRPC$TL_account_resetPasswordOk;
import org.telegram.tgnet.TLRPC$TL_account_resetPasswordRequestedWait;
import org.telegram.tgnet.TLRPC$TL_account_updatePasswordSettings;
import org.telegram.tgnet.TLRPC$TL_auth_passwordRecovery;
import org.telegram.tgnet.TLRPC$TL_auth_requestPasswordRecovery;
import org.telegram.tgnet.TLRPC$TL_boolTrue;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_inputCheckPasswordEmpty;
import org.telegram.tgnet.TLRPC$TL_inputCheckPasswordSRP;
import org.telegram.tgnet.TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow;
import org.telegram.tgnet.TLRPC$TL_passwordKdfAlgoUnknown;
import org.telegram.tgnet.TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000;
import org.telegram.tgnet.TLRPC$TL_securePasswordKdfAlgoSHA512;
import org.telegram.tgnet.TLRPC$TL_securePasswordKdfAlgoUnknown;
import org.telegram.tgnet.TLRPC$TL_secureSecretSettings;
import org.telegram.tgnet.TLRPC$account_Password;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.EditTextSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.OutlineTextContainerView;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.TransformableLoginButtonView;
import org.telegram.ui.Components.VerticalPositionAutoAnimator;
/* loaded from: classes4.dex */
public class TwoStepVerificationActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private SimpleTextView bottomButton;
    private TextView bottomTextView;
    private TextView cancelResetButton;
    private int changePasswordRow;
    private int changeRecoveryEmailRow;
    private TLRPC$account_Password currentPassword;
    private byte[] currentSecret;
    private long currentSecretId;
    private TwoStepVerificationActivityDelegate delegate;
    private int delegateType;
    private boolean destroyed;
    private EmptyTextProgressView emptyView;
    private FrameLayout floatingButtonContainer;
    private TransformableLoginButtonView floatingButtonIcon;
    private boolean forgotPasswordOnShow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loading;
    private RLottieImageView lockImageView;
    private EditTextBoldCursor passwordEditText;
    private int passwordEnabledDetailRow;
    private OutlineTextContainerView passwordOutlineView;
    private boolean postedErrorColorTimeout;
    private AlertDialog progressDialog;
    private RadialProgressView radialProgressView;
    private boolean resetPasswordOnShow;
    private TextView resetWaitView;
    private int rowCount;
    private ScrollView scrollView;
    private int setPasswordDetailRow;
    private int setPasswordRow;
    private int setRecoveryEmailRow;
    private TextView subtitleTextView;
    private TextView titleTextView;
    private int turnPasswordOffRow;
    private boolean passwordEntered = true;
    private byte[] currentPasswordHash = new byte[0];
    private Runnable errorColorTimeout = new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda13
        @Override // java.lang.Runnable
        public final void run() {
            TwoStepVerificationActivity.this.lambda$new$0();
        }
    };
    int otherwiseReloginDays = -1;
    private Runnable updateTimeRunnable = new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda12
        @Override // java.lang.Runnable
        public final void run() {
            TwoStepVerificationActivity.this.updateBottomButton();
        }
    };

    /* loaded from: classes4.dex */
    public interface TwoStepVerificationActivityDelegate {
        void didEnterPassword(TLRPC$InputCheckPasswordSRP tLRPC$InputCheckPasswordSRP);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkSecretValues$28(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.postedErrorColorTimeout = false;
        this.passwordOutlineView.animateError(0.0f);
    }

    public void setPassword(TLRPC$account_Password tLRPC$account_Password) {
        this.currentPassword = tLRPC$account_Password;
        this.passwordEntered = false;
    }

    public void setCurrentPasswordParams(TLRPC$account_Password tLRPC$account_Password, byte[] bArr, long j, byte[] bArr2) {
        this.currentPassword = tLRPC$account_Password;
        this.currentPasswordHash = bArr;
        this.currentSecret = bArr2;
        this.currentSecretId = j;
        this.passwordEntered = (bArr != null && bArr.length > 0) || !tLRPC$account_Password.has_password;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        byte[] bArr;
        super.onFragmentCreate();
        TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
        if (tLRPC$account_Password == null || tLRPC$account_Password.current_algo == null || (bArr = this.currentPasswordHash) == null || bArr.length <= 0) {
            loadPasswordInfo(true, tLRPC$account_Password != null);
        }
        updateRows();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.twoStepPasswordChanged);
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.twoStepPasswordChanged);
        this.destroyed = true;
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog != null) {
            try {
                alertDialog.dismiss();
            } catch (Exception e) {
                FileLog.e(e);
            }
            this.progressDialog = null;
        }
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0217  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0219  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0265  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x02c5  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x02c8  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x032b  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x032e  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0359  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x03ff  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0402  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0406  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0409  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0428  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x045e  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0463  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x046c  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0536  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0559  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0578  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x058d  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        int i;
        int i2;
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        this.actionBar.setAllowOverlayTitle(false);
        if (!this.passwordEntered || this.delegate != null) {
            this.actionBar.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            ActionBar actionBar = this.actionBar;
            int i3 = Theme.key_windowBackgroundWhiteBlackText;
            actionBar.setTitleColor(Theme.getColor(i3));
            this.actionBar.setItemsColor(Theme.getColor(i3), false);
            this.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarWhiteSelector), false);
            this.actionBar.setCastShadows(false);
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.TwoStepVerificationActivity.1
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i4) {
                if (i4 == -1) {
                    TwoStepVerificationActivity twoStepVerificationActivity = TwoStepVerificationActivity.this;
                    if (twoStepVerificationActivity.otherwiseReloginDays >= 0) {
                        twoStepVerificationActivity.showSetForcePasswordAlert();
                    } else {
                        twoStepVerificationActivity.finishFragment();
                    }
                }
            }
        });
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        int i4 = Theme.key_windowBackgroundWhite;
        frameLayout2.setBackgroundColor(Theme.getColor(i4));
        ScrollView scrollView = new ScrollView(context);
        this.scrollView = scrollView;
        scrollView.setFillViewport(true);
        frameLayout2.addView(this.scrollView, LayoutHelper.createFrame(-1, -1.0f));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setGravity(1);
        this.scrollView.addView(linearLayout, LayoutHelper.createScroll(-1, -2, 51));
        RLottieImageView rLottieImageView = new RLottieImageView(context);
        this.lockImageView = rLottieImageView;
        rLottieImageView.setAnimation(R.raw.tsv_setup_intro, 120, 120);
        this.lockImageView.playAnimation();
        RLottieImageView rLottieImageView2 = this.lockImageView;
        if (!AndroidUtilities.isSmallScreen()) {
            Point point = AndroidUtilities.displaySize;
            if (point.x <= point.y) {
                i = 0;
                rLottieImageView2.setVisibility(i);
                linearLayout.addView(this.lockImageView, LayoutHelper.createLinear(120, 120, 1));
                TextView textView = new TextView(context);
                this.titleTextView = textView;
                int i5 = Theme.key_windowBackgroundWhiteBlackText;
                textView.setTextColor(Theme.getColor(i5));
                this.titleTextView.setTextSize(1, 18.0f);
                this.titleTextView.setGravity(1);
                this.titleTextView.setTypeface(AndroidUtilities.bold());
                linearLayout.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
                TextView textView2 = new TextView(context);
                this.subtitleTextView = textView2;
                int i6 = Theme.key_windowBackgroundWhiteGrayText6;
                textView2.setTextColor(Theme.getColor(i6));
                this.subtitleTextView.setTextSize(1, 15.0f);
                this.subtitleTextView.setGravity(1);
                this.subtitleTextView.setVisibility(8);
                linearLayout.addView(this.subtitleTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
                OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
                this.passwordOutlineView = outlineTextContainerView;
                int i7 = R.string.EnterPassword;
                outlineTextContainerView.setText(LocaleController.getString(i7));
                this.passwordOutlineView.animateSelection(1.0f, false);
                linearLayout.addView(this.passwordOutlineView, LayoutHelper.createLinear(-1, -2, 1, 24, 24, 24, 0));
                EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
                this.passwordEditText = editTextBoldCursor;
                editTextBoldCursor.setTextSize(1, 18.0f);
                this.passwordEditText.setTextColor(Theme.getColor(i5));
                this.passwordEditText.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
                this.passwordEditText.setBackground(null);
                this.passwordEditText.setSingleLine(true);
                this.passwordEditText.setInputType(129);
                this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                this.passwordEditText.setTypeface(Typeface.DEFAULT);
                EditTextBoldCursor editTextBoldCursor2 = this.passwordEditText;
                int i8 = Theme.key_windowBackgroundWhiteInputFieldActivated;
                editTextBoldCursor2.setCursorColor(Theme.getColor(i8));
                this.passwordEditText.setCursorWidth(1.5f);
                this.passwordEditText.setContentDescription(LocaleController.getString(i7));
                int dp = AndroidUtilities.dp(16.0f);
                this.passwordEditText.setPadding(dp, dp, dp, dp);
                this.passwordOutlineView.addView(this.passwordEditText, LayoutHelper.createFrame(-1, -2.0f));
                this.passwordOutlineView.attachEditText(this.passwordEditText);
                this.passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda9
                    @Override // android.view.View.OnFocusChangeListener
                    public final void onFocusChange(View view, boolean z) {
                        TwoStepVerificationActivity.this.lambda$createView$1(view, z);
                    }
                });
                this.passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda10
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView3, int i9, KeyEvent keyEvent) {
                        boolean lambda$createView$2;
                        lambda$createView$2 = TwoStepVerificationActivity.this.lambda$createView$2(textView3, i9, keyEvent);
                        return lambda$createView$2;
                    }
                });
                this.passwordEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.TwoStepVerificationActivity.2
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i9, int i10, int i11) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i9, int i10, int i11) {
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        if (TwoStepVerificationActivity.this.postedErrorColorTimeout) {
                            AndroidUtilities.cancelRunOnUIThread(TwoStepVerificationActivity.this.errorColorTimeout);
                            TwoStepVerificationActivity.this.errorColorTimeout.run();
                        }
                    }
                });
                TextView textView3 = new TextView(context);
                this.bottomTextView = textView3;
                textView3.setTextColor(Theme.getColor(i6));
                this.bottomTextView.setTextSize(1, 14.0f);
                this.bottomTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
                this.bottomTextView.setText(LocaleController.getString("YourEmailInfo", R.string.YourEmailInfo));
                linearLayout.addView(this.bottomTextView, LayoutHelper.createLinear(-2, -2, (!LocaleController.isRTL ? 5 : 3) | 48, 40, 30, 40, 0));
                TextView textView4 = new TextView(context);
                this.resetWaitView = textView4;
                textView4.setTextColor(Theme.getColor(i6));
                this.resetWaitView.setTextSize(1, 12.0f);
                this.resetWaitView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
                linearLayout.addView(this.resetWaitView, LayoutHelper.createLinear(-1, -2, 40.0f, 8.0f, 40.0f, 0.0f));
                LinearLayout linearLayout2 = new LinearLayout(context);
                linearLayout2.setOrientation(1);
                linearLayout2.setGravity(80);
                linearLayout2.setClipChildren(false);
                linearLayout.addView(linearLayout2, LayoutHelper.createLinear(-1, 0, 1.0f));
                SimpleTextView simpleTextView = new SimpleTextView(context);
                this.bottomButton = simpleTextView;
                simpleTextView.setTextSize(15);
                this.bottomButton.setGravity(19);
                this.bottomButton.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
                SimpleTextView simpleTextView2 = this.bottomButton;
                i2 = Build.VERSION.SDK_INT;
                frameLayout2.addView(simpleTextView2, LayoutHelper.createFrame(-1, i2 < 21 ? 56.0f : 60.0f, 80, 0.0f, 0.0f, 0.0f, 16.0f));
                this.bottomButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda8
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        TwoStepVerificationActivity.this.lambda$createView$3(view);
                    }
                });
                VerticalPositionAutoAnimator.attach(this.bottomButton);
                TextView textView5 = new TextView(context);
                this.cancelResetButton = textView5;
                textView5.setTextSize(1, 15.0f);
                this.cancelResetButton.setGravity(19);
                this.cancelResetButton.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
                this.cancelResetButton.setText(LocaleController.getString("CancelReset", R.string.CancelReset));
                this.cancelResetButton.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
                this.cancelResetButton.setVisibility(8);
                frameLayout2.addView(this.cancelResetButton, LayoutHelper.createFrame(-1, i2 < 21 ? 56.0f : 60.0f, 80, 0.0f, 0.0f, 0.0f, 16.0f));
                this.cancelResetButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda7
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        TwoStepVerificationActivity.this.lambda$createView$4(view);
                    }
                });
                VerticalPositionAutoAnimator.attach(this.cancelResetButton);
                this.floatingButtonContainer = new FrameLayout(context);
                if (i2 >= 21) {
                    StateListAnimator stateListAnimator = new StateListAnimator();
                    stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                    stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                    this.floatingButtonContainer.setStateListAnimator(stateListAnimator);
                    this.floatingButtonContainer.setOutlineProvider(new ViewOutlineProvider(this) { // from class: org.telegram.ui.TwoStepVerificationActivity.3
                        @Override // android.view.ViewOutlineProvider
                        @SuppressLint({"NewApi"})
                        public void getOutline(View view, Outline outline) {
                            outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                        }
                    });
                }
                VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
                this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda6
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        TwoStepVerificationActivity.this.lambda$createView$5(view);
                    }
                });
                TransformableLoginButtonView transformableLoginButtonView = new TransformableLoginButtonView(context);
                this.floatingButtonIcon = transformableLoginButtonView;
                transformableLoginButtonView.setTransformType(1);
                this.floatingButtonIcon.setProgress(0.0f);
                this.floatingButtonIcon.setColor(Theme.getColor(Theme.key_chats_actionIcon));
                this.floatingButtonIcon.setDrawBackground(false);
                this.floatingButtonContainer.setContentDescription(LocaleController.getString(R.string.Next));
                this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f));
                Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
                if (i2 < 21) {
                    Drawable mutate = context.getResources().getDrawable(R.drawable.floating_shadow).mutate();
                    mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                    CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
                    combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                    createSimpleSelectorCircleDrawable = combinedDrawable;
                }
                this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable);
                frameLayout2.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
                EmptyTextProgressView emptyTextProgressView = new EmptyTextProgressView(context);
                this.emptyView = emptyTextProgressView;
                emptyTextProgressView.showProgress();
                frameLayout2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
                RecyclerListView recyclerListView = new RecyclerListView(context);
                this.listView = recyclerListView;
                recyclerListView.setLayoutManager(new LinearLayoutManager(context, 1, false));
                this.listView.setEmptyView(this.emptyView);
                this.listView.setVerticalScrollBarEnabled(false);
                frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
                RecyclerListView recyclerListView2 = this.listView;
                ListAdapter listAdapter = new ListAdapter(context);
                this.listAdapter = listAdapter;
                recyclerListView2.setAdapter(listAdapter);
                this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda38
                    @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
                    public final void onItemClick(View view, int i9) {
                        TwoStepVerificationActivity.this.lambda$createView$7(view, i9);
                    }
                });
                RadialProgressView radialProgressView = new RadialProgressView(this, context) { // from class: org.telegram.ui.TwoStepVerificationActivity.4
                    @Override // android.view.View
                    protected void onMeasure(int i9, int i10) {
                        super.onMeasure(i9, i10);
                        ((ViewGroup.MarginLayoutParams) getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight / 2;
                    }
                };
                this.radialProgressView = radialProgressView;
                radialProgressView.setSize(AndroidUtilities.dp(20.0f));
                this.radialProgressView.setAlpha(0.0f);
                this.radialProgressView.setScaleX(0.1f);
                this.radialProgressView.setScaleY(0.1f);
                this.radialProgressView.setProgressColor(Theme.getColor(i8));
                this.actionBar.addView(this.radialProgressView, LayoutHelper.createFrame(32, 32.0f, 21, 0.0f, 0.0f, 12.0f, 0.0f));
                updateRows();
                if (!this.passwordEntered && this.delegate == null) {
                    this.actionBar.setTitle(LocaleController.getString("TwoStepVerificationTitle", R.string.TwoStepVerificationTitle));
                } else {
                    this.actionBar.setTitle(null);
                }
                if (this.delegate == null) {
                    this.titleTextView.setText(LocaleController.getString(R.string.YourPassword));
                    this.subtitleTextView.setText(LocaleController.getString(this.delegateType == 1 ? R.string.PleaseEnterCurrentPasswordWithdraw : R.string.PleaseEnterCurrentPasswordTransfer));
                    this.subtitleTextView.setVisibility(0);
                } else {
                    this.titleTextView.setText(LocaleController.getString(R.string.YourPassword));
                    this.subtitleTextView.setVisibility(0);
                    this.subtitleTextView.setText(LocaleController.getString(R.string.LoginPasswordTextShort));
                }
                if (!this.passwordEntered) {
                    View view = this.fragmentView;
                    int i9 = Theme.key_windowBackgroundGray;
                    view.setBackgroundColor(Theme.getColor(i9));
                    this.fragmentView.setTag(Integer.valueOf(i9));
                } else {
                    this.fragmentView.setBackgroundColor(Theme.getColor(i4));
                    this.fragmentView.setTag(Integer.valueOf(i4));
                }
                return this.fragmentView;
            }
        }
        i = 8;
        rLottieImageView2.setVisibility(i);
        linearLayout.addView(this.lockImageView, LayoutHelper.createLinear(120, 120, 1));
        TextView textView6 = new TextView(context);
        this.titleTextView = textView6;
        int i52 = Theme.key_windowBackgroundWhiteBlackText;
        textView6.setTextColor(Theme.getColor(i52));
        this.titleTextView.setTextSize(1, 18.0f);
        this.titleTextView.setGravity(1);
        this.titleTextView.setTypeface(AndroidUtilities.bold());
        linearLayout.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
        TextView textView22 = new TextView(context);
        this.subtitleTextView = textView22;
        int i62 = Theme.key_windowBackgroundWhiteGrayText6;
        textView22.setTextColor(Theme.getColor(i62));
        this.subtitleTextView.setTextSize(1, 15.0f);
        this.subtitleTextView.setGravity(1);
        this.subtitleTextView.setVisibility(8);
        linearLayout.addView(this.subtitleTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
        OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
        this.passwordOutlineView = outlineTextContainerView2;
        int i72 = R.string.EnterPassword;
        outlineTextContainerView2.setText(LocaleController.getString(i72));
        this.passwordOutlineView.animateSelection(1.0f, false);
        linearLayout.addView(this.passwordOutlineView, LayoutHelper.createLinear(-1, -2, 1, 24, 24, 24, 0));
        EditTextBoldCursor editTextBoldCursor3 = new EditTextBoldCursor(context);
        this.passwordEditText = editTextBoldCursor3;
        editTextBoldCursor3.setTextSize(1, 18.0f);
        this.passwordEditText.setTextColor(Theme.getColor(i52));
        this.passwordEditText.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
        this.passwordEditText.setBackground(null);
        this.passwordEditText.setSingleLine(true);
        this.passwordEditText.setInputType(129);
        this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.passwordEditText.setTypeface(Typeface.DEFAULT);
        EditTextBoldCursor editTextBoldCursor22 = this.passwordEditText;
        int i82 = Theme.key_windowBackgroundWhiteInputFieldActivated;
        editTextBoldCursor22.setCursorColor(Theme.getColor(i82));
        this.passwordEditText.setCursorWidth(1.5f);
        this.passwordEditText.setContentDescription(LocaleController.getString(i72));
        int dp2 = AndroidUtilities.dp(16.0f);
        this.passwordEditText.setPadding(dp2, dp2, dp2, dp2);
        this.passwordOutlineView.addView(this.passwordEditText, LayoutHelper.createFrame(-1, -2.0f));
        this.passwordOutlineView.attachEditText(this.passwordEditText);
        this.passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnFocusChangeListener
            public final void onFocusChange(View view2, boolean z) {
                TwoStepVerificationActivity.this.lambda$createView$1(view2, z);
            }
        });
        this.passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda10
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView32, int i92, KeyEvent keyEvent) {
                boolean lambda$createView$2;
                lambda$createView$2 = TwoStepVerificationActivity.this.lambda$createView$2(textView32, i92, keyEvent);
                return lambda$createView$2;
            }
        });
        this.passwordEditText.addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.TwoStepVerificationActivity.2
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i92, int i10, int i11) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i92, int i10, int i11) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (TwoStepVerificationActivity.this.postedErrorColorTimeout) {
                    AndroidUtilities.cancelRunOnUIThread(TwoStepVerificationActivity.this.errorColorTimeout);
                    TwoStepVerificationActivity.this.errorColorTimeout.run();
                }
            }
        });
        TextView textView32 = new TextView(context);
        this.bottomTextView = textView32;
        textView32.setTextColor(Theme.getColor(i62));
        this.bottomTextView.setTextSize(1, 14.0f);
        this.bottomTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
        this.bottomTextView.setText(LocaleController.getString("YourEmailInfo", R.string.YourEmailInfo));
        linearLayout.addView(this.bottomTextView, LayoutHelper.createLinear(-2, -2, (!LocaleController.isRTL ? 5 : 3) | 48, 40, 30, 40, 0));
        TextView textView42 = new TextView(context);
        this.resetWaitView = textView42;
        textView42.setTextColor(Theme.getColor(i62));
        this.resetWaitView.setTextSize(1, 12.0f);
        this.resetWaitView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
        linearLayout.addView(this.resetWaitView, LayoutHelper.createLinear(-1, -2, 40.0f, 8.0f, 40.0f, 0.0f));
        LinearLayout linearLayout22 = new LinearLayout(context);
        linearLayout22.setOrientation(1);
        linearLayout22.setGravity(80);
        linearLayout22.setClipChildren(false);
        linearLayout.addView(linearLayout22, LayoutHelper.createLinear(-1, 0, 1.0f));
        SimpleTextView simpleTextView3 = new SimpleTextView(context);
        this.bottomButton = simpleTextView3;
        simpleTextView3.setTextSize(15);
        this.bottomButton.setGravity(19);
        this.bottomButton.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
        SimpleTextView simpleTextView22 = this.bottomButton;
        i2 = Build.VERSION.SDK_INT;
        frameLayout2.addView(simpleTextView22, LayoutHelper.createFrame(-1, i2 < 21 ? 56.0f : 60.0f, 80, 0.0f, 0.0f, 0.0f, 16.0f));
        this.bottomButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TwoStepVerificationActivity.this.lambda$createView$3(view2);
            }
        });
        VerticalPositionAutoAnimator.attach(this.bottomButton);
        TextView textView52 = new TextView(context);
        this.cancelResetButton = textView52;
        textView52.setTextSize(1, 15.0f);
        this.cancelResetButton.setGravity(19);
        this.cancelResetButton.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
        this.cancelResetButton.setText(LocaleController.getString("CancelReset", R.string.CancelReset));
        this.cancelResetButton.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
        this.cancelResetButton.setVisibility(8);
        frameLayout2.addView(this.cancelResetButton, LayoutHelper.createFrame(-1, i2 < 21 ? 56.0f : 60.0f, 80, 0.0f, 0.0f, 0.0f, 16.0f));
        this.cancelResetButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TwoStepVerificationActivity.this.lambda$createView$4(view2);
            }
        });
        VerticalPositionAutoAnimator.attach(this.cancelResetButton);
        this.floatingButtonContainer = new FrameLayout(context);
        if (i2 >= 21) {
        }
        VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
        this.floatingButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TwoStepVerificationActivity.this.lambda$createView$5(view2);
            }
        });
        TransformableLoginButtonView transformableLoginButtonView2 = new TransformableLoginButtonView(context);
        this.floatingButtonIcon = transformableLoginButtonView2;
        transformableLoginButtonView2.setTransformType(1);
        this.floatingButtonIcon.setProgress(0.0f);
        this.floatingButtonIcon.setColor(Theme.getColor(Theme.key_chats_actionIcon));
        this.floatingButtonIcon.setDrawBackground(false);
        this.floatingButtonContainer.setContentDescription(LocaleController.getString(R.string.Next));
        this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f));
        Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor(Theme.key_chats_actionBackground), Theme.getColor(Theme.key_chats_actionPressedBackground));
        if (i2 < 21) {
        }
        this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable2);
        frameLayout2.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
        EmptyTextProgressView emptyTextProgressView2 = new EmptyTextProgressView(context);
        this.emptyView = emptyTextProgressView2;
        emptyTextProgressView2.showProgress();
        frameLayout2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView3 = new RecyclerListView(context);
        this.listView = recyclerListView3;
        recyclerListView3.setLayoutManager(new LinearLayoutManager(context, 1, false));
        this.listView.setEmptyView(this.emptyView);
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView22 = this.listView;
        ListAdapter listAdapter2 = new ListAdapter(context);
        this.listAdapter = listAdapter2;
        recyclerListView22.setAdapter(listAdapter2);
        this.listView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda38
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view2, int i92) {
                TwoStepVerificationActivity.this.lambda$createView$7(view2, i92);
            }
        });
        RadialProgressView radialProgressView2 = new RadialProgressView(this, context) { // from class: org.telegram.ui.TwoStepVerificationActivity.4
            @Override // android.view.View
            protected void onMeasure(int i92, int i10) {
                super.onMeasure(i92, i10);
                ((ViewGroup.MarginLayoutParams) getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight / 2;
            }
        };
        this.radialProgressView = radialProgressView2;
        radialProgressView2.setSize(AndroidUtilities.dp(20.0f));
        this.radialProgressView.setAlpha(0.0f);
        this.radialProgressView.setScaleX(0.1f);
        this.radialProgressView.setScaleY(0.1f);
        this.radialProgressView.setProgressColor(Theme.getColor(i82));
        this.actionBar.addView(this.radialProgressView, LayoutHelper.createFrame(32, 32.0f, 21, 0.0f, 0.0f, 12.0f, 0.0f));
        updateRows();
        if (!this.passwordEntered) {
        }
        this.actionBar.setTitle(null);
        if (this.delegate == null) {
        }
        if (!this.passwordEntered) {
        }
        return this.fragmentView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$1(View view, boolean z) {
        this.passwordOutlineView.animateSelection(z ? 1.0f : 0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createView$2(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5 || i == 6) {
            processDone();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$3(View view) {
        onPasswordForgot();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$4(View view) {
        cancelPasswordReset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$5(View view) {
        processDone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$7(View view, int i) {
        if (i == this.setPasswordRow || i == this.changePasswordRow) {
            TwoStepVerificationSetupActivity twoStepVerificationSetupActivity = new TwoStepVerificationSetupActivity(this.currentAccount, 0, this.currentPassword);
            twoStepVerificationSetupActivity.addFragmentToClose(this);
            twoStepVerificationSetupActivity.setCurrentPasswordParams(this.currentPasswordHash, this.currentSecretId, this.currentSecret, false);
            presentFragment(twoStepVerificationSetupActivity);
        } else if (i == this.setRecoveryEmailRow || i == this.changeRecoveryEmailRow) {
            TwoStepVerificationSetupActivity twoStepVerificationSetupActivity2 = new TwoStepVerificationSetupActivity(this.currentAccount, 3, this.currentPassword);
            twoStepVerificationSetupActivity2.addFragmentToClose(this);
            twoStepVerificationSetupActivity2.setCurrentPasswordParams(this.currentPasswordHash, this.currentSecretId, this.currentSecret, true);
            presentFragment(twoStepVerificationSetupActivity2);
        } else if (i == this.turnPasswordOffRow) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            String string = LocaleController.getString("TurnPasswordOffQuestion", R.string.TurnPasswordOffQuestion);
            if (this.currentPassword.has_secure_values) {
                string = string + "\n\n" + LocaleController.getString("TurnPasswordOffPassport", R.string.TurnPasswordOffPassport);
            }
            String string2 = LocaleController.getString("TurnPasswordOffQuestionTitle", R.string.TurnPasswordOffQuestionTitle);
            String string3 = LocaleController.getString("Disable", R.string.Disable);
            builder.setMessage(string);
            builder.setTitle(string2);
            builder.setPositiveButton(string3, new DialogInterface.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    TwoStepVerificationActivity.this.lambda$createView$6(dialogInterface, i2);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            AlertDialog create = builder.create();
            showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createView$6(DialogInterface dialogInterface, int i) {
        clearPassword();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        int i;
        super.onConfigurationChanged(configuration);
        RLottieImageView rLottieImageView = this.lockImageView;
        if (!AndroidUtilities.isSmallScreen()) {
            Point point = AndroidUtilities.displaySize;
            if (point.x <= point.y) {
                i = 0;
                rLottieImageView.setVisibility(i);
            }
        }
        i = 8;
        rLottieImageView.setVisibility(i);
    }

    private void cancelPasswordReset() {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString("CancelPasswordResetYes", R.string.CancelPasswordResetYes), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TwoStepVerificationActivity.this.lambda$cancelPasswordReset$10(dialogInterface, i);
            }
        });
        builder.setNegativeButton(LocaleController.getString("CancelPasswordResetNo", R.string.CancelPasswordResetNo), null);
        builder.setTitle(LocaleController.getString("CancelReset", R.string.CancelReset));
        builder.setMessage(LocaleController.getString("CancelPasswordReset", R.string.CancelPasswordReset));
        showDialog(builder.create());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelPasswordReset$10(DialogInterface dialogInterface, int i) {
        getConnectionsManager().sendRequest(new TLObject() { // from class: org.telegram.tgnet.TLRPC$TL_account_declinePasswordReset
            @Override // org.telegram.tgnet.TLObject
            public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i2, boolean z) {
                return TLRPC$Bool.TLdeserialize(abstractSerializedData, i2, z);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                abstractSerializedData.writeInt32(1284770294);
            }
        }, new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda30
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                TwoStepVerificationActivity.this.lambda$cancelPasswordReset$9(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelPasswordReset$9(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$cancelPasswordReset$8(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$cancelPasswordReset$8(TLObject tLObject) {
        if (tLObject instanceof TLRPC$TL_boolTrue) {
            this.currentPassword.pending_reset_date = 0;
            updateBottomButton();
        }
    }

    public void setForgotPasswordOnShow() {
        this.forgotPasswordOnShow = true;
    }

    private void resetPassword() {
        needShowProgress(true);
        getConnectionsManager().sendRequest(new TLObject() { // from class: org.telegram.tgnet.TLRPC$TL_account_resetPassword
            @Override // org.telegram.tgnet.TLObject
            public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
                return TLRPC$account_ResetPasswordResult.TLdeserialize(abstractSerializedData, i, z);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData) {
                abstractSerializedData.writeInt32(-1828139493);
            }
        }, new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda29
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                TwoStepVerificationActivity.this.lambda$resetPassword$13(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetPassword$13(final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$resetPassword$12(tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetPassword$12(TLObject tLObject) {
        String formatPluralString;
        needHideProgress();
        if (tLObject instanceof TLRPC$TL_account_resetPasswordOk) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setNegativeButton(LocaleController.getString("OK", R.string.OK), null);
            builder.setTitle(LocaleController.getString("ResetPassword", R.string.ResetPassword));
            builder.setMessage(LocaleController.getString("RestorePasswordResetPasswordOk", R.string.RestorePasswordResetPasswordOk));
            showDialog(builder.create(), new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda5
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    TwoStepVerificationActivity.this.lambda$resetPassword$11(dialogInterface);
                }
            });
        } else if (tLObject instanceof TLRPC$TL_account_resetPasswordRequestedWait) {
            this.currentPassword.pending_reset_date = ((TLRPC$TL_account_resetPasswordRequestedWait) tLObject).until_date;
            updateBottomButton();
        } else if (tLObject instanceof TLRPC$TL_account_resetPasswordFailedWait) {
            int currentTime = ((TLRPC$TL_account_resetPasswordFailedWait) tLObject).retry_date - getConnectionsManager().getCurrentTime();
            if (currentTime > 86400) {
                formatPluralString = LocaleController.formatPluralString("Days", currentTime / 86400, new Object[0]);
            } else if (currentTime > 3600) {
                formatPluralString = LocaleController.formatPluralString("Hours", currentTime / 86400, new Object[0]);
            } else if (currentTime > 60) {
                formatPluralString = LocaleController.formatPluralString("Minutes", currentTime / 60, new Object[0]);
            } else {
                formatPluralString = LocaleController.formatPluralString("Seconds", Math.max(1, currentTime), new Object[0]);
            }
            showAlertWithText(LocaleController.getString("ResetPassword", R.string.ResetPassword), LocaleController.formatString("ResetPasswordWait", R.string.ResetPasswordWait, formatPluralString));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$resetPassword$11(DialogInterface dialogInterface) {
        getNotificationCenter().lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, new Object[0]);
        finishFragment();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0117  */
    /* JADX WARN: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateBottomButton() {
        TextView textView;
        String format;
        if (this.passwordEntered) {
            return;
        }
        if (this.currentPassword.pending_reset_date != 0) {
            int currentTime = getConnectionsManager().getCurrentTime();
            int i = this.currentPassword.pending_reset_date;
            if (currentTime <= i) {
                int max = Math.max(1, i - getConnectionsManager().getCurrentTime());
                if (max > 86400) {
                    format = LocaleController.formatPluralString("Days", max / 86400, new Object[0]);
                } else if (max >= 3600) {
                    format = LocaleController.formatPluralString("Hours", max / 3600, new Object[0]);
                } else {
                    format = String.format(Locale.US, "%02d:%02d", Integer.valueOf(max / 60), Integer.valueOf(max % 60));
                }
                this.resetWaitView.setText(LocaleController.formatString("RestorePasswordResetIn", R.string.RestorePasswordResetIn, format));
                this.resetWaitView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText6));
                if (this.bottomButton.getVisibility() != 8) {
                    this.bottomButton.setVisibility(8);
                }
                if (this.resetWaitView.getVisibility() != 0) {
                    this.resetWaitView.setVisibility(0);
                }
                this.cancelResetButton.setVisibility(0);
                AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
                AndroidUtilities.runOnUIThread(this.updateTimeRunnable, 1000L);
                if (this.currentPassword == null && this.bottomButton != null && this.resetWaitView.getVisibility() == 0) {
                    return;
                }
                AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
                textView = this.cancelResetButton;
                if (textView == null) {
                    textView.setVisibility(8);
                    return;
                }
                return;
            }
        }
        if (this.resetWaitView.getVisibility() != 8) {
            this.resetWaitView.setVisibility(8);
        }
        if (this.currentPassword.pending_reset_date == 0) {
            this.bottomButton.setText(LocaleController.getString("ForgotPassword", R.string.ForgotPassword));
            this.cancelResetButton.setVisibility(8);
            this.bottomButton.setVisibility(0);
        } else {
            this.bottomButton.setText(LocaleController.getString("ResetPassword", R.string.ResetPassword));
            this.cancelResetButton.setVisibility(0);
            this.bottomButton.setVisibility(0);
        }
        this.bottomButton.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4));
        AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
        if (this.currentPassword == null) {
        }
        AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
        textView = this.cancelResetButton;
        if (textView == null) {
        }
    }

    private void onPasswordForgot() {
        TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
        if (tLRPC$account_Password.pending_reset_date == 0 && tLRPC$account_Password.has_recovery) {
            needShowProgress(true);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_auth_requestPasswordRecovery(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda31
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    TwoStepVerificationActivity.this.lambda$onPasswordForgot$15(tLObject, tLRPC$TL_error);
                }
            }, 10);
        } else if (getParentActivity() == null) {
        } else {
            if (this.currentPassword.pending_reset_date != 0) {
                if (getConnectionsManager().getCurrentTime() > this.currentPassword.pending_reset_date) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    builder.setPositiveButton(LocaleController.getString("Reset", R.string.Reset), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda2
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i) {
                            TwoStepVerificationActivity.this.lambda$onPasswordForgot$16(dialogInterface, i);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    builder.setTitle(LocaleController.getString("ResetPassword", R.string.ResetPassword));
                    builder.setMessage(LocaleController.getString("RestorePasswordResetPasswordText", R.string.RestorePasswordResetPasswordText));
                    AlertDialog create = builder.create();
                    showDialog(create);
                    TextView textView = (TextView) create.getButton(-1);
                    if (textView != null) {
                        textView.setTextColor(Theme.getColor(Theme.key_text_RedBold));
                        return;
                    }
                    return;
                }
                cancelPasswordReset();
                return;
            }
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
            builder2.setPositiveButton(LocaleController.getString("Reset", R.string.Reset), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda4
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    TwoStepVerificationActivity.this.lambda$onPasswordForgot$17(dialogInterface, i);
                }
            });
            builder2.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            builder2.setTitle(LocaleController.getString("ResetPassword", R.string.ResetPassword));
            builder2.setMessage(LocaleController.getString("RestorePasswordNoEmailText2", R.string.RestorePasswordNoEmailText2));
            showDialog(builder2.create());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPasswordForgot$15(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$onPasswordForgot$14(tLRPC$TL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPasswordForgot$14(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        String formatPluralString;
        needHideProgress();
        if (tLRPC$TL_error == null) {
            TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
            tLRPC$account_Password.email_unconfirmed_pattern = ((TLRPC$TL_auth_passwordRecovery) tLObject).email_pattern;
            TwoStepVerificationSetupActivity twoStepVerificationSetupActivity = new TwoStepVerificationSetupActivity(this.currentAccount, 4, tLRPC$account_Password) { // from class: org.telegram.ui.TwoStepVerificationActivity.5
                @Override // org.telegram.ui.TwoStepVerificationSetupActivity
                protected void onReset() {
                    TwoStepVerificationActivity.this.resetPasswordOnShow = true;
                }
            };
            twoStepVerificationSetupActivity.addFragmentToClose(this);
            twoStepVerificationSetupActivity.setCurrentPasswordParams(this.currentPasswordHash, this.currentSecretId, this.currentSecret, false);
            presentFragment(twoStepVerificationSetupActivity);
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
    public /* synthetic */ void lambda$onPasswordForgot$16(DialogInterface dialogInterface, int i) {
        resetPassword();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPasswordForgot$17(DialogInterface dialogInterface, int i) {
        resetPassword();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.twoStepPasswordChanged) {
            if (objArr != null && objArr.length > 0 && objArr[0] != null) {
                this.currentPasswordHash = (byte[]) objArr[0];
            }
            loadPasswordInfo(false, false);
            updateRows();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
    }

    public void setCurrentPasswordInfo(byte[] bArr, TLRPC$account_Password tLRPC$account_Password) {
        if (bArr != null) {
            this.currentPasswordHash = bArr;
        }
        this.currentPassword = tLRPC$account_Password;
    }

    public void setDelegate(int i, TwoStepVerificationActivityDelegate twoStepVerificationActivityDelegate) {
        this.delegateType = i;
        this.delegate = twoStepVerificationActivityDelegate;
    }

    public static boolean canHandleCurrentPassword(TLRPC$account_Password tLRPC$account_Password, boolean z) {
        return z ? !(tLRPC$account_Password.current_algo instanceof TLRPC$TL_passwordKdfAlgoUnknown) : ((tLRPC$account_Password.new_algo instanceof TLRPC$TL_passwordKdfAlgoUnknown) || (tLRPC$account_Password.current_algo instanceof TLRPC$TL_passwordKdfAlgoUnknown) || (tLRPC$account_Password.new_secure_algo instanceof TLRPC$TL_securePasswordKdfAlgoUnknown)) ? false : true;
    }

    public static void initPasswordNewAlgo(TLRPC$account_Password tLRPC$account_Password) {
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = tLRPC$account_Password.new_algo;
        if (tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow tLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow = (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo;
            byte[] bArr = new byte[tLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1.length + 32];
            Utilities.random.nextBytes(bArr);
            byte[] bArr2 = tLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1;
            System.arraycopy(bArr2, 0, bArr, 0, bArr2.length);
            tLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1 = bArr;
        }
        TLRPC$SecurePasswordKdfAlgo tLRPC$SecurePasswordKdfAlgo = tLRPC$account_Password.new_secure_algo;
        if (tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
            TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 = (TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) tLRPC$SecurePasswordKdfAlgo;
            byte[] bArr3 = new byte[tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt.length + 32];
            Utilities.random.nextBytes(bArr3);
            byte[] bArr4 = tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt;
            System.arraycopy(bArr4, 0, bArr3, 0, bArr4.length);
            tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt = bArr3;
        }
    }

    private void loadPasswordInfo(final boolean z, final boolean z2) {
        if (!z2) {
            this.loading = true;
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda35
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                TwoStepVerificationActivity.this.lambda$loadPasswordInfo$19(z2, z, tLObject, tLRPC$TL_error);
            }
        }, 10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPasswordInfo$19(final boolean z, final boolean z2, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$loadPasswordInfo$18(tLRPC$TL_error, tLObject, z, z2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPasswordInfo$18(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, boolean z, boolean z2) {
        if (tLRPC$TL_error == null) {
            this.loading = false;
            TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            this.currentPassword = tLRPC$account_Password;
            if (!canHandleCurrentPassword(tLRPC$account_Password, false)) {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
                return;
            }
            if (!z || z2) {
                byte[] bArr = this.currentPasswordHash;
                this.passwordEntered = (bArr != null && bArr.length > 0) || !this.currentPassword.has_password;
            }
            initPasswordNewAlgo(this.currentPassword);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
        }
        updateRows();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        super.onTransitionAnimationEnd(z, z2);
        if (z) {
            if (this.forgotPasswordOnShow) {
                onPasswordForgot();
                this.forgotPasswordOnShow = false;
            } else if (this.resetPasswordOnShow) {
                resetPassword();
                this.resetPasswordOnShow = false;
            }
        }
    }

    private void updateRows() {
        TLRPC$account_Password tLRPC$account_Password;
        StringBuilder sb = new StringBuilder();
        sb.append(this.setPasswordRow);
        sb.append(this.setPasswordDetailRow);
        sb.append(this.changePasswordRow);
        sb.append(this.turnPasswordOffRow);
        sb.append(this.setRecoveryEmailRow);
        sb.append(this.changeRecoveryEmailRow);
        sb.append(this.passwordEnabledDetailRow);
        sb.append(this.rowCount);
        this.rowCount = 0;
        this.setPasswordRow = -1;
        this.setPasswordDetailRow = -1;
        this.changePasswordRow = -1;
        this.turnPasswordOffRow = -1;
        this.setRecoveryEmailRow = -1;
        this.changeRecoveryEmailRow = -1;
        this.passwordEnabledDetailRow = -1;
        if (!this.loading && (tLRPC$account_Password = this.currentPassword) != null && this.passwordEntered) {
            if (tLRPC$account_Password.has_password) {
                int i = 0 + 1;
                this.rowCount = i;
                this.changePasswordRow = 0;
                int i2 = i + 1;
                this.rowCount = i2;
                this.turnPasswordOffRow = i;
                if (tLRPC$account_Password.has_recovery) {
                    this.rowCount = i2 + 1;
                    this.changeRecoveryEmailRow = i2;
                } else {
                    this.rowCount = i2 + 1;
                    this.setRecoveryEmailRow = i2;
                }
                int i3 = this.rowCount;
                this.rowCount = i3 + 1;
                this.passwordEnabledDetailRow = i3;
            } else {
                int i4 = 0 + 1;
                this.rowCount = i4;
                this.setPasswordRow = 0;
                this.rowCount = i4 + 1;
                this.setPasswordDetailRow = i4;
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.setPasswordRow);
        sb2.append(this.setPasswordDetailRow);
        sb2.append(this.changePasswordRow);
        sb2.append(this.turnPasswordOffRow);
        sb2.append(this.setRecoveryEmailRow);
        sb2.append(this.changeRecoveryEmailRow);
        sb2.append(this.passwordEnabledDetailRow);
        sb2.append(this.rowCount);
        if (this.listAdapter != null && !sb.toString().equals(sb2.toString())) {
            this.listAdapter.notifyDataSetChanged();
        }
        if (this.fragmentView != null) {
            if (this.loading || this.passwordEntered) {
                RecyclerListView recyclerListView = this.listView;
                if (recyclerListView != null) {
                    recyclerListView.setVisibility(0);
                    this.scrollView.setVisibility(4);
                    this.listView.setEmptyView(this.emptyView);
                }
                if (this.passwordEditText != null) {
                    this.floatingButtonContainer.setVisibility(8);
                    this.passwordEditText.setVisibility(4);
                    this.titleTextView.setVisibility(4);
                    this.bottomTextView.setVisibility(8);
                    this.bottomButton.setVisibility(4);
                    updateBottomButton();
                }
                View view = this.fragmentView;
                int i5 = Theme.key_windowBackgroundGray;
                view.setBackgroundColor(Theme.getColor(i5));
                this.fragmentView.setTag(Integer.valueOf(i5));
                return;
            }
            RecyclerListView recyclerListView2 = this.listView;
            if (recyclerListView2 != null) {
                recyclerListView2.setEmptyView(null);
                this.listView.setVisibility(4);
                this.scrollView.setVisibility(0);
                this.emptyView.setVisibility(4);
            }
            if (this.passwordEditText != null) {
                this.floatingButtonContainer.setVisibility(0);
                this.passwordEditText.setVisibility(0);
                View view2 = this.fragmentView;
                int i6 = Theme.key_windowBackgroundWhite;
                view2.setBackgroundColor(Theme.getColor(i6));
                this.fragmentView.setTag(Integer.valueOf(i6));
                this.titleTextView.setVisibility(0);
                this.bottomButton.setVisibility(0);
                updateBottomButton();
                this.bottomTextView.setVisibility(8);
                if (!TextUtils.isEmpty(this.currentPassword.hint)) {
                    this.passwordEditText.setHint(this.currentPassword.hint);
                } else {
                    this.passwordEditText.setHint((CharSequence) null);
                }
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda14
                    @Override // java.lang.Runnable
                    public final void run() {
                        TwoStepVerificationActivity.this.lambda$updateRows$20();
                    }
                }, 200L);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRows$20() {
        EditTextBoldCursor editTextBoldCursor;
        if (isFinishing() || this.destroyed || (editTextBoldCursor = this.passwordEditText) == null) {
            return;
        }
        editTextBoldCursor.requestFocus();
        AndroidUtilities.showKeyboard(this.passwordEditText);
    }

    private void needShowProgress() {
        needShowProgress(false);
    }

    private void needShowProgress(boolean z) {
        if (getParentActivity() == null || getParentActivity().isFinishing() || this.progressDialog != null) {
            return;
        }
        if (!this.passwordEntered) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(this.radialProgressView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.radialProgressView, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.radialProgressView, View.SCALE_Y, 1.0f));
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.start();
            return;
        }
        AlertDialog alertDialog = new AlertDialog(getParentActivity(), 3);
        this.progressDialog = alertDialog;
        alertDialog.setCanCancel(false);
        if (z) {
            this.progressDialog.showDelayed(300L);
        } else {
            this.progressDialog.show();
        }
    }

    public void needHideProgress() {
        if (!this.passwordEntered) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(this.radialProgressView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.radialProgressView, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.radialProgressView, View.SCALE_Y, 0.1f));
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.start();
            return;
        }
        AlertDialog alertDialog = this.progressDialog;
        if (alertDialog == null) {
            return;
        }
        try {
            alertDialog.dismiss();
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.progressDialog = null;
    }

    private void showAlertWithText(String str, String str2) {
        if (getParentActivity() == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
        builder.setTitle(str);
        builder.setMessage(str2);
        showDialog(builder.create());
    }

    private void clearPassword() {
        final TLRPC$TL_account_updatePasswordSettings tLRPC$TL_account_updatePasswordSettings = new TLRPC$TL_account_updatePasswordSettings();
        byte[] bArr = this.currentPasswordHash;
        if (bArr == null || bArr.length == 0) {
            tLRPC$TL_account_updatePasswordSettings.password = new TLRPC$TL_inputCheckPasswordEmpty();
        }
        tLRPC$TL_account_updatePasswordSettings.new_settings = new TLRPC$TL_account_passwordInputSettings();
        UserConfig.getInstance(this.currentAccount).resetSavedPassword();
        this.currentSecret = null;
        TLRPC$TL_account_passwordInputSettings tLRPC$TL_account_passwordInputSettings = tLRPC$TL_account_updatePasswordSettings.new_settings;
        tLRPC$TL_account_passwordInputSettings.flags = 3;
        tLRPC$TL_account_passwordInputSettings.hint = "";
        tLRPC$TL_account_passwordInputSettings.new_password_hash = new byte[0];
        tLRPC$TL_account_passwordInputSettings.new_algo = new TLRPC$TL_passwordKdfAlgoUnknown();
        tLRPC$TL_account_updatePasswordSettings.new_settings.email = "";
        needShowProgress();
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$clearPassword$27(tLRPC$TL_account_updatePasswordSettings);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$27(TLRPC$TL_account_updatePasswordSettings tLRPC$TL_account_updatePasswordSettings) {
        if (tLRPC$TL_account_updatePasswordSettings.password == null) {
            if (this.currentPassword.current_algo == null) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda32
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                        TwoStepVerificationActivity.this.lambda$clearPassword$22(tLObject, tLRPC$TL_error);
                    }
                }, 8);
                return;
            }
            tLRPC$TL_account_updatePasswordSettings.password = getNewSrpPassword();
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_updatePasswordSettings, new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda28
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                TwoStepVerificationActivity.this.lambda$clearPassword$26(tLObject, tLRPC$TL_error);
            }
        }, 10);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$22(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda22
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$clearPassword$21(tLRPC$TL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$21(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        if (tLRPC$TL_error == null) {
            TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            this.currentPassword = tLRPC$account_Password;
            initPasswordNewAlgo(tLRPC$account_Password);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
            clearPassword();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$26(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$clearPassword$25(tLRPC$TL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$25(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        String formatPluralString;
        if (tLRPC$TL_error != null && "SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda34
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject2, TLRPC$TL_error tLRPC$TL_error2) {
                    TwoStepVerificationActivity.this.lambda$clearPassword$24(tLObject2, tLRPC$TL_error2);
                }
            }, 8);
            return;
        }
        needHideProgress();
        if (tLRPC$TL_error == null && (tLObject instanceof TLRPC$TL_boolTrue)) {
            this.currentPassword = null;
            this.currentPasswordHash = new byte[0];
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didRemoveTwoStepPassword, new Object[0]);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, new Object[0]);
            finishFragment();
        } else if (tLRPC$TL_error != null) {
            if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                if (intValue < 60) {
                    formatPluralString = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                } else {
                    formatPluralString = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                }
                showAlertWithText(LocaleController.getString("AppName", R.string.AppName), LocaleController.formatString("FloodWaitTime", R.string.FloodWaitTime, formatPluralString));
                return;
            }
            showAlertWithText(LocaleController.getString("AppName", R.string.AppName), tLRPC$TL_error.text);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$24(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda23
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$clearPassword$23(tLRPC$TL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$clearPassword$23(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        if (tLRPC$TL_error == null) {
            TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            this.currentPassword = tLRPC$account_Password;
            initPasswordNewAlgo(tLRPC$account_Password);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
            clearPassword();
        }
    }

    public TLRPC$TL_inputCheckPasswordSRP getNewSrpPassword() {
        TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = tLRPC$account_Password.current_algo;
        if (tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            return SRPHelper.startCheck(this.currentPasswordHash, tLRPC$account_Password.srp_id, tLRPC$account_Password.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo);
        }
        return null;
    }

    private boolean checkSecretValues(byte[] bArr, TLRPC$TL_account_passwordSettings tLRPC$TL_account_passwordSettings) {
        byte[] computeSHA512;
        TLRPC$TL_secureSecretSettings tLRPC$TL_secureSecretSettings = tLRPC$TL_account_passwordSettings.secure_settings;
        if (tLRPC$TL_secureSecretSettings != null) {
            this.currentSecret = tLRPC$TL_secureSecretSettings.secure_secret;
            TLRPC$SecurePasswordKdfAlgo tLRPC$SecurePasswordKdfAlgo = tLRPC$TL_secureSecretSettings.secure_algo;
            if (tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
                computeSHA512 = Utilities.computePBKDF2(bArr, ((TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) tLRPC$SecurePasswordKdfAlgo).salt);
            } else if (!(tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoSHA512)) {
                return false;
            } else {
                byte[] bArr2 = ((TLRPC$TL_securePasswordKdfAlgoSHA512) tLRPC$SecurePasswordKdfAlgo).salt;
                computeSHA512 = Utilities.computeSHA512(bArr2, bArr, bArr2);
            }
            this.currentSecretId = tLRPC$TL_account_passwordSettings.secure_settings.secure_secret_id;
            byte[] bArr3 = new byte[32];
            System.arraycopy(computeSHA512, 0, bArr3, 0, 32);
            byte[] bArr4 = new byte[16];
            System.arraycopy(computeSHA512, 32, bArr4, 0, 16);
            byte[] bArr5 = this.currentSecret;
            Utilities.aesCbcEncryptionByteArraySafe(bArr5, bArr3, bArr4, 0, bArr5.length, 0, 0);
            TLRPC$TL_secureSecretSettings tLRPC$TL_secureSecretSettings2 = tLRPC$TL_account_passwordSettings.secure_settings;
            if (PassportActivity.checkSecret(tLRPC$TL_secureSecretSettings2.secure_secret, Long.valueOf(tLRPC$TL_secureSecretSettings2.secure_secret_id))) {
                return true;
            }
            TLRPC$TL_account_updatePasswordSettings tLRPC$TL_account_updatePasswordSettings = new TLRPC$TL_account_updatePasswordSettings();
            tLRPC$TL_account_updatePasswordSettings.password = getNewSrpPassword();
            TLRPC$TL_account_passwordInputSettings tLRPC$TL_account_passwordInputSettings = new TLRPC$TL_account_passwordInputSettings();
            tLRPC$TL_account_updatePasswordSettings.new_settings = tLRPC$TL_account_passwordInputSettings;
            tLRPC$TL_account_passwordInputSettings.new_secure_settings = new TLRPC$TL_secureSecretSettings();
            TLRPC$TL_secureSecretSettings tLRPC$TL_secureSecretSettings3 = tLRPC$TL_account_updatePasswordSettings.new_settings.new_secure_settings;
            tLRPC$TL_secureSecretSettings3.secure_secret = new byte[0];
            tLRPC$TL_secureSecretSettings3.secure_algo = new TLRPC$TL_securePasswordKdfAlgoUnknown();
            TLRPC$TL_account_passwordInputSettings tLRPC$TL_account_passwordInputSettings2 = tLRPC$TL_account_updatePasswordSettings.new_settings;
            tLRPC$TL_account_passwordInputSettings2.new_secure_settings.secure_secret_id = 0L;
            tLRPC$TL_account_passwordInputSettings2.flags |= 4;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_updatePasswordSettings, new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda37
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                    TwoStepVerificationActivity.lambda$checkSecretValues$28(tLObject, tLRPC$TL_error);
                }
            });
            this.currentSecret = null;
            this.currentSecretId = 0L;
            return true;
        }
        this.currentSecret = null;
        this.currentSecretId = 0L;
        return true;
    }

    private void processDone() {
        if (this.passwordEntered) {
            return;
        }
        String obj = this.passwordEditText.getText().toString();
        if (obj.length() == 0) {
            onFieldError(this.passwordOutlineView, this.passwordEditText, false);
            return;
        }
        final byte[] stringBytes = AndroidUtilities.getStringBytes(obj);
        needShowProgress();
        Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$processDone$35(stringBytes);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$35(final byte[] bArr) {
        TLRPC$TL_account_getPasswordSettings tLRPC$TL_account_getPasswordSettings = new TLRPC$TL_account_getPasswordSettings();
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = this.currentPassword.current_algo;
        final byte[] x = tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow ? SRPHelper.getX(bArr, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo) : null;
        RequestDelegate requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda36
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                TwoStepVerificationActivity.this.lambda$processDone$34(bArr, x, tLObject, tLRPC$TL_error);
            }
        };
        TLRPC$account_Password tLRPC$account_Password = this.currentPassword;
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo2 = tLRPC$account_Password.current_algo;
        if (tLRPC$PasswordKdfAlgo2 instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            TLRPC$TL_inputCheckPasswordSRP startCheck = SRPHelper.startCheck(x, tLRPC$account_Password.srp_id, tLRPC$account_Password.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo2);
            tLRPC$TL_account_getPasswordSettings.password = startCheck;
            if (startCheck == null) {
                TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
                tLRPC$TL_error.text = "ALGO_INVALID";
                requestDelegate.run(null, tLRPC$TL_error);
                return;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getPasswordSettings, requestDelegate, 10);
            return;
        }
        TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
        tLRPC$TL_error2.text = "PASSWORD_HASH_INVALID";
        requestDelegate.run(null, tLRPC$TL_error2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$34(final byte[] bArr, final byte[] bArr2, final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda27
                @Override // java.lang.Runnable
                public final void run() {
                    TwoStepVerificationActivity.this.lambda$processDone$30(bArr, tLObject, bArr2);
                }
            });
        } else {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda18
                @Override // java.lang.Runnable
                public final void run() {
                    TwoStepVerificationActivity.this.lambda$processDone$33(tLRPC$TL_error);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$30(byte[] bArr, TLObject tLObject, final byte[] bArr2) {
        final boolean checkSecretValues = checkSecretValues(bArr, (TLRPC$TL_account_passwordSettings) tLObject);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda25
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$processDone$29(checkSecretValues, bArr2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$29(boolean z, byte[] bArr) {
        if (this.delegate == null || !z) {
            needHideProgress();
        }
        if (z) {
            this.currentPasswordHash = bArr;
            this.passwordEntered = true;
            if (this.delegate != null) {
                AndroidUtilities.hideKeyboard(this.passwordEditText);
                this.delegate.didEnterPassword(getNewSrpPassword());
                return;
            } else if (!TextUtils.isEmpty(this.currentPassword.email_unconfirmed_pattern)) {
                TwoStepVerificationSetupActivity twoStepVerificationSetupActivity = new TwoStepVerificationSetupActivity(this.currentAccount, 5, this.currentPassword);
                twoStepVerificationSetupActivity.setCurrentPasswordParams(this.currentPasswordHash, this.currentSecretId, this.currentSecret, true);
                presentFragment(twoStepVerificationSetupActivity, true);
                return;
            } else {
                AndroidUtilities.hideKeyboard(this.passwordEditText);
                TwoStepVerificationActivity twoStepVerificationActivity = new TwoStepVerificationActivity();
                twoStepVerificationActivity.passwordEntered = true;
                twoStepVerificationActivity.currentPasswordHash = this.currentPasswordHash;
                twoStepVerificationActivity.currentPassword = this.currentPassword;
                twoStepVerificationActivity.currentSecret = this.currentSecret;
                twoStepVerificationActivity.currentSecretId = this.currentSecretId;
                presentFragment(twoStepVerificationActivity, true);
                return;
            }
        }
        AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", R.string.UpdateAppAlert), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$33(TLRPC$TL_error tLRPC$TL_error) {
        String formatPluralString;
        if ("SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new RequestDelegate() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda33
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error2) {
                    TwoStepVerificationActivity.this.lambda$processDone$32(tLObject, tLRPC$TL_error2);
                }
            }, 8);
            return;
        }
        needHideProgress();
        if ("PASSWORD_HASH_INVALID".equals(tLRPC$TL_error.text)) {
            onFieldError(this.passwordOutlineView, this.passwordEditText, true);
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
    public /* synthetic */ void lambda$processDone$32(final TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda20
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$processDone$31(tLRPC$TL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$processDone$31(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        if (tLRPC$TL_error == null) {
            TLRPC$account_Password tLRPC$account_Password = (TLRPC$account_Password) tLObject;
            this.currentPassword = tLRPC$account_Password;
            initPasswordNewAlgo(tLRPC$account_Password);
            NotificationCenter.getInstance(this.currentAccount).lambda$postNotificationNameOnUIThread$1(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
            processDone();
        }
    }

    private void onFieldError(OutlineTextContainerView outlineTextContainerView, TextView textView, boolean z) {
        if (getParentActivity() == null) {
            return;
        }
        try {
            textView.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        if (z) {
            textView.setText("");
        }
        outlineTextContainerView.animateError(1.0f);
        AndroidUtilities.shakeViewSpring(outlineTextContainerView, 5.0f, new Runnable() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                TwoStepVerificationActivity.this.lambda$onFieldError$36();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onFieldError$36() {
        AndroidUtilities.cancelRunOnUIThread(this.errorColorTimeout);
        AndroidUtilities.runOnUIThread(this.errorColorTimeout, 1500L);
        this.postedErrorColorTimeout = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            if (TwoStepVerificationActivity.this.loading || TwoStepVerificationActivity.this.currentPassword == null) {
                return 0;
            }
            return TwoStepVerificationActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View textSettingsCell;
            if (i == 0) {
                textSettingsCell = new TextSettingsCell(this.mContext);
                textSettingsCell.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
            } else {
                textSettingsCell = new TextInfoPrivacyCell(this.mContext);
            }
            return new RecyclerListView.Holder(textSettingsCell);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    return;
                }
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i != TwoStepVerificationActivity.this.setPasswordDetailRow) {
                    if (i == TwoStepVerificationActivity.this.passwordEnabledDetailRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("EnabledPasswordText", R.string.EnabledPasswordText));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                        return;
                    }
                    return;
                }
                textInfoPrivacyCell.setText(LocaleController.getString("SetAdditionalPasswordInfo", R.string.SetAdditionalPasswordInfo));
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawableByKey(this.mContext, R.drawable.greydivider_bottom, Theme.key_windowBackgroundGrayShadow));
                return;
            }
            TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
            int i2 = Theme.key_windowBackgroundWhiteBlackText;
            textSettingsCell.setTag(Integer.valueOf(i2));
            textSettingsCell.setTextColor(Theme.getColor(i2));
            if (i != TwoStepVerificationActivity.this.changePasswordRow) {
                if (i != TwoStepVerificationActivity.this.setPasswordRow) {
                    if (i != TwoStepVerificationActivity.this.turnPasswordOffRow) {
                        if (i != TwoStepVerificationActivity.this.changeRecoveryEmailRow) {
                            if (i == TwoStepVerificationActivity.this.setRecoveryEmailRow) {
                                textSettingsCell.setText(LocaleController.getString("SetRecoveryEmail", R.string.SetRecoveryEmail), false);
                                return;
                            }
                            return;
                        }
                        textSettingsCell.setText(LocaleController.getString("ChangeRecoveryEmail", R.string.ChangeRecoveryEmail), false);
                        return;
                    }
                    textSettingsCell.setText(LocaleController.getString("TurnPasswordOff", R.string.TurnPasswordOff), true);
                    return;
                }
                textSettingsCell.setText(LocaleController.getString("SetAdditionalPassword", R.string.SetAdditionalPassword), true);
                return;
            }
            textSettingsCell.setText(LocaleController.getString("ChangePassword", R.string.ChangePassword), true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return (i == TwoStepVerificationActivity.this.setPasswordDetailRow || i == TwoStepVerificationActivity.this.passwordEnabledDetailRow) ? 1 : 0;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        int i = Theme.key_windowBackgroundWhite;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, EditTextSettingsCell.class}, null, null, null, i));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, i));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));
        ActionBar actionBar = this.actionBar;
        int i2 = ThemeDescription.FLAG_BACKGROUND;
        int i3 = Theme.key_actionBarDefault;
        arrayList.add(new ThemeDescription(actionBar, i2, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, i3));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_progressCircle));
        int i4 = Theme.key_windowBackgroundWhiteBlackText;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_text_RedRegular));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i4));
        int i5 = Theme.key_windowBackgroundWhiteHintText;
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, i5));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, Theme.key_windowBackgroundWhiteGrayText4));
        TextView textView = this.titleTextView;
        int i6 = ThemeDescription.FLAG_TEXTCOLOR;
        int i7 = Theme.key_windowBackgroundWhiteGrayText6;
        arrayList.add(new ThemeDescription(textView, i6, null, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.bottomTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i7));
        arrayList.add(new ThemeDescription(this.bottomButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlueText4));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, i4));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, i5));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));
        return arrayList;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onBackPressed() {
        if (this.otherwiseReloginDays >= 0) {
            showSetForcePasswordAlert();
            return false;
        }
        return super.onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSetForcePasswordAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("Warning", R.string.Warning));
        builder.setMessage(LocaleController.formatPluralString("ForceSetPasswordAlertMessageShort", this.otherwiseReloginDays, new Object[0]));
        builder.setPositiveButton(LocaleController.getString("TwoStepVerificationSetPassword", R.string.TwoStepVerificationSetPassword), null);
        builder.setNegativeButton(LocaleController.getString("ForceSetPasswordCancel", R.string.ForceSetPasswordCancel), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.TwoStepVerificationActivity$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TwoStepVerificationActivity.this.lambda$showSetForcePasswordAlert$37(dialogInterface, i);
            }
        });
        ((TextView) builder.show().getButton(-2)).setTextColor(Theme.getColor(Theme.key_text_RedBold));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSetForcePasswordAlert$37(DialogInterface dialogInterface, int i) {
        finishFragment();
    }

    public void setBlockingAlert(int i) {
        this.otherwiseReloginDays = i;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void finishFragment() {
        if (this.otherwiseReloginDays >= 0) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("afterSignup", true);
            presentFragment(new DialogsActivity(bundle), true);
            return;
        }
        super.finishFragment();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean isLightStatusBar() {
        return ColorUtils.calculateLuminance(Theme.getColor(Theme.key_windowBackgroundWhite, null, true)) > 0.699999988079071d;
    }
}
