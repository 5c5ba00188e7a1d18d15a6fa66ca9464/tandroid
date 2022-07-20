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
import org.telegram.messenger.SRPHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$InputCheckPasswordSRP;
import org.telegram.tgnet.TLRPC$PasswordKdfAlgo;
import org.telegram.tgnet.TLRPC$SecurePasswordKdfAlgo;
import org.telegram.tgnet.TLRPC$TL_account_declinePasswordReset;
import org.telegram.tgnet.TLRPC$TL_account_getPassword;
import org.telegram.tgnet.TLRPC$TL_account_getPasswordSettings;
import org.telegram.tgnet.TLRPC$TL_account_password;
import org.telegram.tgnet.TLRPC$TL_account_passwordInputSettings;
import org.telegram.tgnet.TLRPC$TL_account_passwordSettings;
import org.telegram.tgnet.TLRPC$TL_account_resetPassword;
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
/* loaded from: classes3.dex */
public class TwoStepVerificationActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private SimpleTextView bottomButton;
    private TextView bottomTextView;
    private TextView cancelResetButton;
    private int changePasswordRow;
    private int changeRecoveryEmailRow;
    private TLRPC$TL_account_password currentPassword;
    private byte[] currentSecret;
    private long currentSecretId;
    private TwoStepVerificationActivityDelegate delegate;
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
    private Runnable errorColorTimeout = new TwoStepVerificationActivity$$ExternalSyntheticLambda13(this);
    int otherwiseReloginDays = -1;
    private Runnable updateTimeRunnable = new TwoStepVerificationActivity$$ExternalSyntheticLambda12(this);

    /* loaded from: classes3.dex */
    public interface TwoStepVerificationActivityDelegate {
        void didEnterPassword(TLRPC$InputCheckPasswordSRP tLRPC$InputCheckPasswordSRP);
    }

    public static /* synthetic */ void lambda$checkSecretValues$28(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
    }

    public /* synthetic */ void lambda$new$0() {
        this.postedErrorColorTimeout = false;
        this.passwordOutlineView.animateError(0.0f);
    }

    public void setPassword(TLRPC$TL_account_password tLRPC$TL_account_password) {
        this.currentPassword = tLRPC$TL_account_password;
        this.passwordEntered = false;
    }

    public void setCurrentPasswordParams(TLRPC$TL_account_password tLRPC$TL_account_password, byte[] bArr, long j, byte[] bArr2) {
        this.currentPassword = tLRPC$TL_account_password;
        this.currentPasswordHash = bArr;
        this.currentSecret = bArr2;
        this.currentSecretId = j;
        this.passwordEntered = (bArr != null && bArr.length > 0) || !tLRPC$TL_account_password.has_password;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        byte[] bArr;
        super.onFragmentCreate();
        TLRPC$TL_account_password tLRPC$TL_account_password = this.currentPassword;
        if (tLRPC$TL_account_password == null || tLRPC$TL_account_password.current_algo == null || (bArr = this.currentPasswordHash) == null || bArr.length <= 0) {
            loadPasswordInfo(true, tLRPC$TL_account_password != null);
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

    /* JADX WARN: Removed duplicated region for block: B:13:0x0216  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0236  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0238  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0265  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x02c6  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x032c  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x032f  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x035a  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0404  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0407  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x040b  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x040e  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x042f  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x045e  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x046e  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0474  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0477  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0524  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0533  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x053d  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x054c  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x056d  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0580  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        int i;
        int i2;
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(false);
        if (!this.passwordEntered) {
            this.actionBar.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.actionBar.setTitleColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.actionBar.setItemsColor(Theme.getColor("windowBackgroundWhiteBlackText"), false);
            this.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarWhiteSelector"), false);
            this.actionBar.setCastShadows(false);
        }
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        FrameLayout frameLayout = new FrameLayout(context);
        this.fragmentView = frameLayout;
        FrameLayout frameLayout2 = frameLayout;
        frameLayout2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
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
        rLottieImageView.setAnimation(2131558579, 120, 120);
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
                textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.titleTextView.setTextSize(1, 18.0f);
                this.titleTextView.setGravity(1);
                this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                linearLayout.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
                TextView textView2 = new TextView(context);
                this.subtitleTextView = textView2;
                textView2.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
                this.subtitleTextView.setTextSize(1, 15.0f);
                this.subtitleTextView.setGravity(1);
                this.subtitleTextView.setVisibility(8);
                linearLayout.addView(this.subtitleTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
                OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
                this.passwordOutlineView = outlineTextContainerView;
                outlineTextContainerView.setText(LocaleController.getString(2131625689));
                this.passwordOutlineView.animateSelection(1.0f, false);
                linearLayout.addView(this.passwordOutlineView, LayoutHelper.createLinear(-1, -2, 1, 24, 24, 24, 0));
                EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
                this.passwordEditText = editTextBoldCursor;
                editTextBoldCursor.setTextSize(1, 18.0f);
                this.passwordEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                this.passwordEditText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
                this.passwordEditText.setBackground(null);
                this.passwordEditText.setSingleLine(true);
                this.passwordEditText.setInputType(129);
                this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                this.passwordEditText.setTypeface(Typeface.DEFAULT);
                this.passwordEditText.setCursorColor(Theme.getColor("windowBackgroundWhiteInputFieldActivated"));
                this.passwordEditText.setCursorWidth(1.5f);
                this.passwordEditText.setContentDescription(LocaleController.getString(2131625689));
                int dp = AndroidUtilities.dp(16.0f);
                this.passwordEditText.setPadding(dp, dp, dp, dp);
                this.passwordOutlineView.addView(this.passwordEditText, LayoutHelper.createFrame(-1, -2.0f));
                this.passwordOutlineView.attachEditText(this.passwordEditText);
                this.passwordEditText.setOnFocusChangeListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda9(this));
                this.passwordEditText.setOnEditorActionListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda10(this));
                this.passwordEditText.addTextChangedListener(new AnonymousClass2());
                TextView textView3 = new TextView(context);
                this.bottomTextView = textView3;
                textView3.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
                this.bottomTextView.setTextSize(1, 14.0f);
                this.bottomTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
                this.bottomTextView.setText(LocaleController.getString("YourEmailInfo", 2131629357));
                linearLayout.addView(this.bottomTextView, LayoutHelper.createLinear(-2, -2, (!LocaleController.isRTL ? 5 : 3) | 48, 40, 30, 40, 0));
                TextView textView4 = new TextView(context);
                this.resetWaitView = textView4;
                textView4.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
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
                this.bottomButton.setOnClickListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda8(this));
                VerticalPositionAutoAnimator.attach(this.bottomButton);
                TextView textView5 = new TextView(context);
                this.cancelResetButton = textView5;
                textView5.setTextSize(1, 15.0f);
                this.cancelResetButton.setGravity(19);
                this.cancelResetButton.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
                this.cancelResetButton.setText(LocaleController.getString("CancelReset", 2131624852));
                this.cancelResetButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
                this.cancelResetButton.setVisibility(8);
                frameLayout2.addView(this.cancelResetButton, LayoutHelper.createFrame(-1, i2 < 21 ? 56.0f : 60.0f, 80, 0.0f, 0.0f, 0.0f, 16.0f));
                this.cancelResetButton.setOnClickListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda7(this));
                VerticalPositionAutoAnimator.attach(this.cancelResetButton);
                this.floatingButtonContainer = new FrameLayout(context);
                if (i2 >= 21) {
                    StateListAnimator stateListAnimator = new StateListAnimator();
                    stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                    stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                    this.floatingButtonContainer.setStateListAnimator(stateListAnimator);
                    this.floatingButtonContainer.setOutlineProvider(new AnonymousClass3(this));
                }
                VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
                this.floatingButtonContainer.setOnClickListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda6(this));
                TransformableLoginButtonView transformableLoginButtonView = new TransformableLoginButtonView(context);
                this.floatingButtonIcon = transformableLoginButtonView;
                transformableLoginButtonView.setTransformType(1);
                this.floatingButtonIcon.setProgress(0.0f);
                this.floatingButtonIcon.setColor(Theme.getColor("chats_actionIcon"));
                this.floatingButtonIcon.setDrawBackground(false);
                this.floatingButtonContainer.setContentDescription(LocaleController.getString(2131626853));
                this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f));
                Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
                if (i2 >= 21) {
                    Drawable mutate = context.getResources().getDrawable(2131165414).mutate();
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
                this.listView.setOnItemClickListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda38(this));
                AnonymousClass4 anonymousClass4 = new AnonymousClass4(this, context);
                this.radialProgressView = anonymousClass4;
                anonymousClass4.setSize(AndroidUtilities.dp(20.0f));
                this.radialProgressView.setAlpha(0.0f);
                this.radialProgressView.setScaleX(0.1f);
                this.radialProgressView.setScaleY(0.1f);
                this.radialProgressView.setProgressColor(Theme.getColor("windowBackgroundWhiteInputFieldActivated"));
                this.actionBar.addView(this.radialProgressView, LayoutHelper.createFrame(32, 32.0f, 21, 0.0f, 0.0f, 12.0f, 0.0f));
                updateRows();
                if (!this.passwordEntered) {
                    this.actionBar.setTitle(LocaleController.getString("TwoStepVerificationTitle", 2131628775));
                } else {
                    this.actionBar.setTitle(null);
                }
                if (this.delegate == null) {
                    this.titleTextView.setText(LocaleController.getString("PleaseEnterCurrentPasswordTransfer", 2131627613));
                } else {
                    this.titleTextView.setText(LocaleController.getString(2131629367));
                    this.subtitleTextView.setVisibility(0);
                    this.subtitleTextView.setText(LocaleController.getString(2131626553));
                }
                if (!this.passwordEntered) {
                    this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
                    this.fragmentView.setTag("windowBackgroundGray");
                } else {
                    this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    this.fragmentView.setTag("windowBackgroundWhite");
                }
                return this.fragmentView;
            }
        }
        i = 8;
        rLottieImageView2.setVisibility(i);
        linearLayout.addView(this.lockImageView, LayoutHelper.createLinear(120, 120, 1));
        TextView textView6 = new TextView(context);
        this.titleTextView = textView6;
        textView6.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.titleTextView.setTextSize(1, 18.0f);
        this.titleTextView.setGravity(1);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        linearLayout.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
        TextView textView22 = new TextView(context);
        this.subtitleTextView = textView22;
        textView22.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
        this.subtitleTextView.setTextSize(1, 15.0f);
        this.subtitleTextView.setGravity(1);
        this.subtitleTextView.setVisibility(8);
        linearLayout.addView(this.subtitleTextView, LayoutHelper.createLinear(-2, -2, 1, 24, 8, 24, 0));
        OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
        this.passwordOutlineView = outlineTextContainerView2;
        outlineTextContainerView2.setText(LocaleController.getString(2131625689));
        this.passwordOutlineView.animateSelection(1.0f, false);
        linearLayout.addView(this.passwordOutlineView, LayoutHelper.createLinear(-1, -2, 1, 24, 24, 24, 0));
        EditTextBoldCursor editTextBoldCursor2 = new EditTextBoldCursor(context);
        this.passwordEditText = editTextBoldCursor2;
        editTextBoldCursor2.setTextSize(1, 18.0f);
        this.passwordEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
        this.passwordEditText.setHintTextColor(Theme.getColor("windowBackgroundWhiteHintText"));
        this.passwordEditText.setBackground(null);
        this.passwordEditText.setSingleLine(true);
        this.passwordEditText.setInputType(129);
        this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.passwordEditText.setTypeface(Typeface.DEFAULT);
        this.passwordEditText.setCursorColor(Theme.getColor("windowBackgroundWhiteInputFieldActivated"));
        this.passwordEditText.setCursorWidth(1.5f);
        this.passwordEditText.setContentDescription(LocaleController.getString(2131625689));
        int dp2 = AndroidUtilities.dp(16.0f);
        this.passwordEditText.setPadding(dp2, dp2, dp2, dp2);
        this.passwordOutlineView.addView(this.passwordEditText, LayoutHelper.createFrame(-1, -2.0f));
        this.passwordOutlineView.attachEditText(this.passwordEditText);
        this.passwordEditText.setOnFocusChangeListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda9(this));
        this.passwordEditText.setOnEditorActionListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda10(this));
        this.passwordEditText.addTextChangedListener(new AnonymousClass2());
        TextView textView32 = new TextView(context);
        this.bottomTextView = textView32;
        textView32.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
        this.bottomTextView.setTextSize(1, 14.0f);
        this.bottomTextView.setGravity((!LocaleController.isRTL ? 5 : 3) | 48);
        this.bottomTextView.setText(LocaleController.getString("YourEmailInfo", 2131629357));
        linearLayout.addView(this.bottomTextView, LayoutHelper.createLinear(-2, -2, (!LocaleController.isRTL ? 5 : 3) | 48, 40, 30, 40, 0));
        TextView textView42 = new TextView(context);
        this.resetWaitView = textView42;
        textView42.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
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
        this.bottomButton.setOnClickListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda8(this));
        VerticalPositionAutoAnimator.attach(this.bottomButton);
        TextView textView52 = new TextView(context);
        this.cancelResetButton = textView52;
        textView52.setTextSize(1, 15.0f);
        this.cancelResetButton.setGravity(19);
        this.cancelResetButton.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
        this.cancelResetButton.setText(LocaleController.getString("CancelReset", 2131624852));
        this.cancelResetButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
        this.cancelResetButton.setVisibility(8);
        frameLayout2.addView(this.cancelResetButton, LayoutHelper.createFrame(-1, i2 < 21 ? 56.0f : 60.0f, 80, 0.0f, 0.0f, 0.0f, 16.0f));
        this.cancelResetButton.setOnClickListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda7(this));
        VerticalPositionAutoAnimator.attach(this.cancelResetButton);
        this.floatingButtonContainer = new FrameLayout(context);
        if (i2 >= 21) {
        }
        VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
        this.floatingButtonContainer.setOnClickListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda6(this));
        TransformableLoginButtonView transformableLoginButtonView2 = new TransformableLoginButtonView(context);
        this.floatingButtonIcon = transformableLoginButtonView2;
        transformableLoginButtonView2.setTransformType(1);
        this.floatingButtonIcon.setProgress(0.0f);
        this.floatingButtonIcon.setColor(Theme.getColor("chats_actionIcon"));
        this.floatingButtonIcon.setDrawBackground(false);
        this.floatingButtonContainer.setContentDescription(LocaleController.getString(2131626853));
        this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f));
        Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
        if (i2 >= 21) {
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
        this.listView.setOnItemClickListener(new TwoStepVerificationActivity$$ExternalSyntheticLambda38(this));
        AnonymousClass4 anonymousClass42 = new AnonymousClass4(this, context);
        this.radialProgressView = anonymousClass42;
        anonymousClass42.setSize(AndroidUtilities.dp(20.0f));
        this.radialProgressView.setAlpha(0.0f);
        this.radialProgressView.setScaleX(0.1f);
        this.radialProgressView.setScaleY(0.1f);
        this.radialProgressView.setProgressColor(Theme.getColor("windowBackgroundWhiteInputFieldActivated"));
        this.actionBar.addView(this.radialProgressView, LayoutHelper.createFrame(32, 32.0f, 21, 0.0f, 0.0f, 12.0f, 0.0f));
        updateRows();
        if (!this.passwordEntered) {
        }
        if (this.delegate == null) {
        }
        if (!this.passwordEntered) {
        }
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.TwoStepVerificationActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            TwoStepVerificationActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                TwoStepVerificationActivity twoStepVerificationActivity = TwoStepVerificationActivity.this;
                if (twoStepVerificationActivity.otherwiseReloginDays >= 0) {
                    twoStepVerificationActivity.showSetForcePasswordAlert();
                } else {
                    twoStepVerificationActivity.finishFragment();
                }
            }
        }
    }

    public /* synthetic */ void lambda$createView$1(View view, boolean z) {
        this.passwordOutlineView.animateSelection(z ? 1.0f : 0.0f);
    }

    public /* synthetic */ boolean lambda$createView$2(TextView textView, int i, KeyEvent keyEvent) {
        if (i == 5 || i == 6) {
            processDone();
            return true;
        }
        return false;
    }

    /* renamed from: org.telegram.ui.TwoStepVerificationActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass2() {
            TwoStepVerificationActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (TwoStepVerificationActivity.this.postedErrorColorTimeout) {
                AndroidUtilities.cancelRunOnUIThread(TwoStepVerificationActivity.this.errorColorTimeout);
                TwoStepVerificationActivity.this.errorColorTimeout.run();
            }
        }
    }

    public /* synthetic */ void lambda$createView$3(View view) {
        onPasswordForgot();
    }

    public /* synthetic */ void lambda$createView$4(View view) {
        cancelPasswordReset();
    }

    /* renamed from: org.telegram.ui.TwoStepVerificationActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends ViewOutlineProvider {
        AnonymousClass3(TwoStepVerificationActivity twoStepVerificationActivity) {
        }

        @Override // android.view.ViewOutlineProvider
        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
    }

    public /* synthetic */ void lambda$createView$5(View view) {
        processDone();
    }

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
        } else if (i != this.turnPasswordOffRow) {
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            String string = LocaleController.getString("TurnPasswordOffQuestion", 2131628766);
            if (this.currentPassword.has_secure_values) {
                string = string + "\n\n" + LocaleController.getString("TurnPasswordOffPassport", 2131628765);
            }
            String string2 = LocaleController.getString("TurnPasswordOffQuestionTitle", 2131628767);
            String string3 = LocaleController.getString("Disable", 2131625495);
            builder.setMessage(string);
            builder.setTitle(string2);
            builder.setPositiveButton(string3, new TwoStepVerificationActivity$$ExternalSyntheticLambda3(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
            AlertDialog create = builder.create();
            showDialog(create);
            TextView textView = (TextView) create.getButton(-1);
            if (textView == null) {
                return;
            }
            textView.setTextColor(Theme.getColor("dialogTextRed2"));
        }
    }

    public /* synthetic */ void lambda$createView$6(DialogInterface dialogInterface, int i) {
        clearPassword();
    }

    /* renamed from: org.telegram.ui.TwoStepVerificationActivity$4 */
    /* loaded from: classes3.dex */
    class AnonymousClass4 extends RadialProgressView {
        AnonymousClass4(TwoStepVerificationActivity twoStepVerificationActivity, Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            ((ViewGroup.MarginLayoutParams) getLayoutParams()).topMargin = AndroidUtilities.statusBarHeight / 2;
        }
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
        builder.setPositiveButton(LocaleController.getString("CancelPasswordResetYes", 2131624848), new TwoStepVerificationActivity$$ExternalSyntheticLambda0(this));
        builder.setNegativeButton(LocaleController.getString("CancelPasswordResetNo", 2131624847), null);
        builder.setTitle(LocaleController.getString("CancelReset", 2131624852));
        builder.setMessage(LocaleController.getString("CancelPasswordReset", 2131624846));
        showDialog(builder.create());
    }

    public /* synthetic */ void lambda$cancelPasswordReset$10(DialogInterface dialogInterface, int i) {
        getConnectionsManager().sendRequest(new TLRPC$TL_account_declinePasswordReset(), new TwoStepVerificationActivity$$ExternalSyntheticLambda30(this));
    }

    public /* synthetic */ void lambda$cancelPasswordReset$9(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new TwoStepVerificationActivity$$ExternalSyntheticLambda16(this, tLObject));
    }

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
        getConnectionsManager().sendRequest(new TLRPC$TL_account_resetPassword(), new TwoStepVerificationActivity$$ExternalSyntheticLambda29(this));
    }

    public /* synthetic */ void lambda$resetPassword$13(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new TwoStepVerificationActivity$$ExternalSyntheticLambda15(this, tLObject));
    }

    public /* synthetic */ void lambda$resetPassword$12(TLObject tLObject) {
        String str;
        needHideProgress();
        if (tLObject instanceof TLRPC$TL_account_resetPasswordOk) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setNegativeButton(LocaleController.getString("OK", 2131627127), null);
            builder.setTitle(LocaleController.getString("ResetPassword", 2131628067));
            builder.setMessage(LocaleController.getString("RestorePasswordResetPasswordOk", 2131628086));
            showDialog(builder.create(), new TwoStepVerificationActivity$$ExternalSyntheticLambda5(this));
        } else if (tLObject instanceof TLRPC$TL_account_resetPasswordRequestedWait) {
            this.currentPassword.pending_reset_date = ((TLRPC$TL_account_resetPasswordRequestedWait) tLObject).until_date;
            updateBottomButton();
        } else if (!(tLObject instanceof TLRPC$TL_account_resetPasswordFailedWait)) {
        } else {
            int currentTime = ((TLRPC$TL_account_resetPasswordFailedWait) tLObject).retry_date - getConnectionsManager().getCurrentTime();
            if (currentTime > 86400) {
                str = LocaleController.formatPluralString("Days", currentTime / 86400, new Object[0]);
            } else if (currentTime > 3600) {
                str = LocaleController.formatPluralString("Hours", currentTime / 86400, new Object[0]);
            } else if (currentTime > 60) {
                str = LocaleController.formatPluralString("Minutes", currentTime / 60, new Object[0]);
            } else {
                str = LocaleController.formatPluralString("Seconds", Math.max(1, currentTime), new Object[0]);
            }
            showAlertWithText(LocaleController.getString("ResetPassword", 2131628067), LocaleController.formatString("ResetPasswordWait", 2131628068, str));
        }
    }

    public /* synthetic */ void lambda$resetPassword$11(DialogInterface dialogInterface) {
        getNotificationCenter().postNotificationName(NotificationCenter.didSetOrRemoveTwoStepPassword, new Object[0]);
        finishFragment();
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateBottomButton() {
        TextView textView;
        String str;
        if (this.passwordEntered) {
            return;
        }
        if (this.currentPassword.pending_reset_date != 0) {
            int currentTime = getConnectionsManager().getCurrentTime();
            int i = this.currentPassword.pending_reset_date;
            if (currentTime <= i) {
                int max = Math.max(1, i - getConnectionsManager().getCurrentTime());
                if (max > 86400) {
                    str = LocaleController.formatPluralString("Days", max / 86400, new Object[0]);
                } else if (max >= 3600) {
                    str = LocaleController.formatPluralString("Hours", max / 3600, new Object[0]);
                } else {
                    str = String.format(Locale.US, "%02d:%02d", Integer.valueOf(max / 60), Integer.valueOf(max % 60));
                }
                this.resetWaitView.setText(LocaleController.formatString("RestorePasswordResetIn", 2131628084, str));
                this.resetWaitView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
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
                if (textView != null) {
                    return;
                }
                textView.setVisibility(8);
                return;
            }
        }
        if (this.resetWaitView.getVisibility() != 8) {
            this.resetWaitView.setVisibility(8);
        }
        if (this.currentPassword.pending_reset_date == 0) {
            this.bottomButton.setText(LocaleController.getString("ForgotPassword", 2131625980));
            this.cancelResetButton.setVisibility(8);
            this.bottomButton.setVisibility(0);
        } else {
            this.bottomButton.setText(LocaleController.getString("ResetPassword", 2131628067));
            this.cancelResetButton.setVisibility(0);
            this.bottomButton.setVisibility(0);
        }
        this.bottomButton.setTextColor(Theme.getColor("windowBackgroundWhiteBlueText4"));
        AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
        if (this.currentPassword == null) {
        }
        AndroidUtilities.cancelRunOnUIThread(this.updateTimeRunnable);
        textView = this.cancelResetButton;
        if (textView != null) {
        }
    }

    private void onPasswordForgot() {
        TLRPC$TL_account_password tLRPC$TL_account_password = this.currentPassword;
        if (tLRPC$TL_account_password.pending_reset_date == 0 && tLRPC$TL_account_password.has_recovery) {
            needShowProgress(true);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_auth_requestPasswordRecovery(), new TwoStepVerificationActivity$$ExternalSyntheticLambda31(this), 10);
        } else if (getParentActivity() == null) {
        } else {
            if (this.currentPassword.pending_reset_date != 0) {
                if (getConnectionsManager().getCurrentTime() > this.currentPassword.pending_reset_date) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                    builder.setPositiveButton(LocaleController.getString("Reset", 2131628042), new TwoStepVerificationActivity$$ExternalSyntheticLambda2(this));
                    builder.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
                    builder.setTitle(LocaleController.getString("ResetPassword", 2131628067));
                    builder.setMessage(LocaleController.getString("RestorePasswordResetPasswordText", 2131628087));
                    AlertDialog create = builder.create();
                    showDialog(create);
                    TextView textView = (TextView) create.getButton(-1);
                    if (textView == null) {
                        return;
                    }
                    textView.setTextColor(Theme.getColor("dialogTextRed2"));
                    return;
                }
                cancelPasswordReset();
                return;
            }
            AlertDialog.Builder builder2 = new AlertDialog.Builder(getParentActivity());
            builder2.setPositiveButton(LocaleController.getString("Reset", 2131628042), new TwoStepVerificationActivity$$ExternalSyntheticLambda4(this));
            builder2.setNegativeButton(LocaleController.getString("Cancel", 2131624832), null);
            builder2.setTitle(LocaleController.getString("ResetPassword", 2131628067));
            builder2.setMessage(LocaleController.getString("RestorePasswordNoEmailText2", 2131628081));
            showDialog(builder2.create());
        }
    }

    public /* synthetic */ void lambda$onPasswordForgot$15(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new TwoStepVerificationActivity$$ExternalSyntheticLambda21(this, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$onPasswordForgot$14(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        String str;
        needHideProgress();
        if (tLRPC$TL_error == null) {
            TLRPC$TL_account_password tLRPC$TL_account_password = this.currentPassword;
            tLRPC$TL_account_password.email_unconfirmed_pattern = ((TLRPC$TL_auth_passwordRecovery) tLObject).email_pattern;
            AnonymousClass5 anonymousClass5 = new AnonymousClass5(this.currentAccount, 4, tLRPC$TL_account_password);
            anonymousClass5.addFragmentToClose(this);
            anonymousClass5.setCurrentPasswordParams(this.currentPasswordHash, this.currentSecretId, this.currentSecret, false);
            presentFragment(anonymousClass5);
        } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
            int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
            if (intValue < 60) {
                str = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
            } else {
                str = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
            }
            showAlertWithText(LocaleController.getString("AppName", 2131624384), LocaleController.formatString("FloodWaitTime", 2131625950, str));
        } else {
            showAlertWithText(LocaleController.getString("AppName", 2131624384), tLRPC$TL_error.text);
        }
    }

    /* renamed from: org.telegram.ui.TwoStepVerificationActivity$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends TwoStepVerificationSetupActivity {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(int i, int i2, TLRPC$TL_account_password tLRPC$TL_account_password) {
            super(i, i2, tLRPC$TL_account_password);
            TwoStepVerificationActivity.this = r1;
        }

        @Override // org.telegram.ui.TwoStepVerificationSetupActivity
        protected void onReset() {
            TwoStepVerificationActivity.this.resetPasswordOnShow = true;
        }
    }

    public /* synthetic */ void lambda$onPasswordForgot$16(DialogInterface dialogInterface, int i) {
        resetPassword();
    }

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

    public void setCurrentPasswordInfo(byte[] bArr, TLRPC$TL_account_password tLRPC$TL_account_password) {
        if (bArr != null) {
            this.currentPasswordHash = bArr;
        }
        this.currentPassword = tLRPC$TL_account_password;
    }

    public void setDelegate(TwoStepVerificationActivityDelegate twoStepVerificationActivityDelegate) {
        this.delegate = twoStepVerificationActivityDelegate;
    }

    public static boolean canHandleCurrentPassword(TLRPC$TL_account_password tLRPC$TL_account_password, boolean z) {
        return z ? !(tLRPC$TL_account_password.current_algo instanceof TLRPC$TL_passwordKdfAlgoUnknown) : !(tLRPC$TL_account_password.new_algo instanceof TLRPC$TL_passwordKdfAlgoUnknown) && !(tLRPC$TL_account_password.current_algo instanceof TLRPC$TL_passwordKdfAlgoUnknown) && !(tLRPC$TL_account_password.new_secure_algo instanceof TLRPC$TL_securePasswordKdfAlgoUnknown);
    }

    public static void initPasswordNewAlgo(TLRPC$TL_account_password tLRPC$TL_account_password) {
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = tLRPC$TL_account_password.new_algo;
        if (tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow tLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow = (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo;
            byte[] bArr = new byte[tLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1.length + 32];
            Utilities.random.nextBytes(bArr);
            byte[] bArr2 = tLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1;
            System.arraycopy(bArr2, 0, bArr, 0, bArr2.length);
            tLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow.salt1 = bArr;
        }
        TLRPC$SecurePasswordKdfAlgo tLRPC$SecurePasswordKdfAlgo = tLRPC$TL_account_password.new_secure_algo;
        if (tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
            TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000 = (TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) tLRPC$SecurePasswordKdfAlgo;
            byte[] bArr3 = new byte[tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt.length + 32];
            Utilities.random.nextBytes(bArr3);
            byte[] bArr4 = tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt;
            System.arraycopy(bArr4, 0, bArr3, 0, bArr4.length);
            tLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000.salt = bArr3;
        }
    }

    private void loadPasswordInfo(boolean z, boolean z2) {
        if (!z2) {
            this.loading = true;
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter != null) {
                listAdapter.notifyDataSetChanged();
            }
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new TwoStepVerificationActivity$$ExternalSyntheticLambda35(this, z2, z), 10);
    }

    public /* synthetic */ void lambda$loadPasswordInfo$19(boolean z, boolean z2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new TwoStepVerificationActivity$$ExternalSyntheticLambda24(this, tLRPC$TL_error, tLObject, z, z2));
    }

    public /* synthetic */ void lambda$loadPasswordInfo$18(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject, boolean z, boolean z2) {
        if (tLRPC$TL_error == null) {
            this.loading = false;
            TLRPC$TL_account_password tLRPC$TL_account_password = (TLRPC$TL_account_password) tLObject;
            this.currentPassword = tLRPC$TL_account_password;
            if (!canHandleCurrentPassword(tLRPC$TL_account_password, false)) {
                AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131628830), true);
                return;
            }
            if (!z || z2) {
                byte[] bArr = this.currentPasswordHash;
                this.passwordEntered = (bArr != null && bArr.length > 0) || !this.currentPassword.has_password;
            }
            initPasswordNewAlgo(this.currentPassword);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
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
            } else if (!this.resetPasswordOnShow) {
            } else {
                resetPassword();
                this.resetPasswordOnShow = false;
            }
        }
    }

    private void updateRows() {
        TLRPC$TL_account_password tLRPC$TL_account_password;
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
        if (!this.loading && (tLRPC$TL_account_password = this.currentPassword) != null && this.passwordEntered) {
            if (tLRPC$TL_account_password.has_password) {
                int i = 0 + 1;
                this.rowCount = i;
                this.changePasswordRow = 0;
                int i2 = i + 1;
                this.rowCount = i2;
                this.turnPasswordOffRow = i;
                if (tLRPC$TL_account_password.has_recovery) {
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
                this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
                this.fragmentView.setTag("windowBackgroundGray");
                return;
            }
            RecyclerListView recyclerListView2 = this.listView;
            if (recyclerListView2 != null) {
                recyclerListView2.setEmptyView(null);
                this.listView.setVisibility(4);
                this.scrollView.setVisibility(0);
                this.emptyView.setVisibility(4);
            }
            if (this.passwordEditText == null) {
                return;
            }
            this.floatingButtonContainer.setVisibility(0);
            this.passwordEditText.setVisibility(0);
            this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            this.fragmentView.setTag("windowBackgroundWhite");
            this.titleTextView.setVisibility(0);
            this.bottomButton.setVisibility(0);
            updateBottomButton();
            this.bottomTextView.setVisibility(8);
            if (!TextUtils.isEmpty(this.currentPassword.hint)) {
                this.passwordEditText.setHint(this.currentPassword.hint);
            } else {
                this.passwordEditText.setHint((CharSequence) null);
            }
            AndroidUtilities.runOnUIThread(new TwoStepVerificationActivity$$ExternalSyntheticLambda14(this), 200L);
        }
    }

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
        builder.setPositiveButton(LocaleController.getString("OK", 2131627127), null);
        builder.setTitle(str);
        builder.setMessage(str2);
        showDialog(builder.create());
    }

    private void clearPassword() {
        TLRPC$TL_account_updatePasswordSettings tLRPC$TL_account_updatePasswordSettings = new TLRPC$TL_account_updatePasswordSettings();
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
        Utilities.globalQueue.postRunnable(new TwoStepVerificationActivity$$ExternalSyntheticLambda17(this, tLRPC$TL_account_updatePasswordSettings));
    }

    public /* synthetic */ void lambda$clearPassword$27(TLRPC$TL_account_updatePasswordSettings tLRPC$TL_account_updatePasswordSettings) {
        if (tLRPC$TL_account_updatePasswordSettings.password == null) {
            if (this.currentPassword.current_algo == null) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new TwoStepVerificationActivity$$ExternalSyntheticLambda32(this), 8);
                return;
            }
            tLRPC$TL_account_updatePasswordSettings.password = getNewSrpPassword();
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_updatePasswordSettings, new TwoStepVerificationActivity$$ExternalSyntheticLambda28(this), 10);
    }

    public /* synthetic */ void lambda$clearPassword$22(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new TwoStepVerificationActivity$$ExternalSyntheticLambda22(this, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$clearPassword$21(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        if (tLRPC$TL_error == null) {
            TLRPC$TL_account_password tLRPC$TL_account_password = (TLRPC$TL_account_password) tLObject;
            this.currentPassword = tLRPC$TL_account_password;
            initPasswordNewAlgo(tLRPC$TL_account_password);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
            clearPassword();
        }
    }

    public /* synthetic */ void lambda$clearPassword$26(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new TwoStepVerificationActivity$$ExternalSyntheticLambda19(this, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$clearPassword$25(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        String str;
        if (tLRPC$TL_error != null && "SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new TwoStepVerificationActivity$$ExternalSyntheticLambda34(this), 8);
            return;
        }
        needHideProgress();
        if (tLRPC$TL_error == null && (tLObject instanceof TLRPC$TL_boolTrue)) {
            this.currentPassword = null;
            this.currentPasswordHash = new byte[0];
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didRemoveTwoStepPassword, new Object[0]);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didSetOrRemoveTwoStepPassword, new Object[0]);
            finishFragment();
        } else if (tLRPC$TL_error == null) {
        } else {
            if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
                int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
                if (intValue < 60) {
                    str = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
                } else {
                    str = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
                }
                showAlertWithText(LocaleController.getString("AppName", 2131624384), LocaleController.formatString("FloodWaitTime", 2131625950, str));
                return;
            }
            showAlertWithText(LocaleController.getString("AppName", 2131624384), tLRPC$TL_error.text);
        }
    }

    public /* synthetic */ void lambda$clearPassword$24(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new TwoStepVerificationActivity$$ExternalSyntheticLambda23(this, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$clearPassword$23(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        if (tLRPC$TL_error == null) {
            TLRPC$TL_account_password tLRPC$TL_account_password = (TLRPC$TL_account_password) tLObject;
            this.currentPassword = tLRPC$TL_account_password;
            initPasswordNewAlgo(tLRPC$TL_account_password);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
            clearPassword();
        }
    }

    public TLRPC$TL_inputCheckPasswordSRP getNewSrpPassword() {
        TLRPC$TL_account_password tLRPC$TL_account_password = this.currentPassword;
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = tLRPC$TL_account_password.current_algo;
        if (tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            return SRPHelper.startCheck(this.currentPasswordHash, tLRPC$TL_account_password.srp_id, tLRPC$TL_account_password.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo);
        }
        return null;
    }

    private boolean checkSecretValues(byte[] bArr, TLRPC$TL_account_passwordSettings tLRPC$TL_account_passwordSettings) {
        byte[] bArr2;
        TLRPC$TL_secureSecretSettings tLRPC$TL_secureSecretSettings = tLRPC$TL_account_passwordSettings.secure_settings;
        if (tLRPC$TL_secureSecretSettings != null) {
            this.currentSecret = tLRPC$TL_secureSecretSettings.secure_secret;
            TLRPC$SecurePasswordKdfAlgo tLRPC$SecurePasswordKdfAlgo = tLRPC$TL_secureSecretSettings.secure_algo;
            if (tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) {
                bArr2 = Utilities.computePBKDF2(bArr, ((TLRPC$TL_securePasswordKdfAlgoPBKDF2HMACSHA512iter100000) tLRPC$SecurePasswordKdfAlgo).salt);
            } else if (!(tLRPC$SecurePasswordKdfAlgo instanceof TLRPC$TL_securePasswordKdfAlgoSHA512)) {
                return false;
            } else {
                byte[] bArr3 = ((TLRPC$TL_securePasswordKdfAlgoSHA512) tLRPC$SecurePasswordKdfAlgo).salt;
                bArr2 = Utilities.computeSHA512(bArr3, bArr, bArr3);
            }
            this.currentSecretId = tLRPC$TL_account_passwordSettings.secure_settings.secure_secret_id;
            byte[] bArr4 = new byte[32];
            System.arraycopy(bArr2, 0, bArr4, 0, 32);
            byte[] bArr5 = new byte[16];
            System.arraycopy(bArr2, 32, bArr5, 0, 16);
            byte[] bArr6 = this.currentSecret;
            Utilities.aesCbcEncryptionByteArraySafe(bArr6, bArr4, bArr5, 0, bArr6.length, 0, 0);
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
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_updatePasswordSettings, TwoStepVerificationActivity$$ExternalSyntheticLambda37.INSTANCE);
            this.currentSecret = null;
            this.currentSecretId = 0L;
            return true;
        }
        this.currentSecret = null;
        this.currentSecretId = 0L;
        return true;
    }

    private void processDone() {
        if (!this.passwordEntered) {
            String obj = this.passwordEditText.getText().toString();
            if (obj.length() == 0) {
                onFieldError(this.passwordOutlineView, this.passwordEditText, false);
                return;
            }
            byte[] stringBytes = AndroidUtilities.getStringBytes(obj);
            needShowProgress();
            Utilities.globalQueue.postRunnable(new TwoStepVerificationActivity$$ExternalSyntheticLambda26(this, stringBytes));
        }
    }

    public /* synthetic */ void lambda$processDone$35(byte[] bArr) {
        TLRPC$TL_account_getPasswordSettings tLRPC$TL_account_getPasswordSettings = new TLRPC$TL_account_getPasswordSettings();
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo = this.currentPassword.current_algo;
        byte[] x = tLRPC$PasswordKdfAlgo instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow ? SRPHelper.getX(bArr, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo) : null;
        TwoStepVerificationActivity$$ExternalSyntheticLambda36 twoStepVerificationActivity$$ExternalSyntheticLambda36 = new TwoStepVerificationActivity$$ExternalSyntheticLambda36(this, bArr, x);
        TLRPC$TL_account_password tLRPC$TL_account_password = this.currentPassword;
        TLRPC$PasswordKdfAlgo tLRPC$PasswordKdfAlgo2 = tLRPC$TL_account_password.current_algo;
        if (tLRPC$PasswordKdfAlgo2 instanceof TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) {
            TLRPC$TL_inputCheckPasswordSRP startCheck = SRPHelper.startCheck(x, tLRPC$TL_account_password.srp_id, tLRPC$TL_account_password.srp_B, (TLRPC$TL_passwordKdfAlgoSHA256SHA256PBKDF2HMACSHA512iter100000SHA256ModPow) tLRPC$PasswordKdfAlgo2);
            tLRPC$TL_account_getPasswordSettings.password = startCheck;
            if (startCheck == null) {
                TLRPC$TL_error tLRPC$TL_error = new TLRPC$TL_error();
                tLRPC$TL_error.text = "ALGO_INVALID";
                twoStepVerificationActivity$$ExternalSyntheticLambda36.run(null, tLRPC$TL_error);
                return;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_account_getPasswordSettings, twoStepVerificationActivity$$ExternalSyntheticLambda36, 10);
            return;
        }
        TLRPC$TL_error tLRPC$TL_error2 = new TLRPC$TL_error();
        tLRPC$TL_error2.text = "PASSWORD_HASH_INVALID";
        twoStepVerificationActivity$$ExternalSyntheticLambda36.run(null, tLRPC$TL_error2);
    }

    public /* synthetic */ void lambda$processDone$34(byte[] bArr, byte[] bArr2, TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        if (tLRPC$TL_error == null) {
            Utilities.globalQueue.postRunnable(new TwoStepVerificationActivity$$ExternalSyntheticLambda27(this, bArr, tLObject, bArr2));
        } else {
            AndroidUtilities.runOnUIThread(new TwoStepVerificationActivity$$ExternalSyntheticLambda18(this, tLRPC$TL_error));
        }
    }

    public /* synthetic */ void lambda$processDone$30(byte[] bArr, TLObject tLObject, byte[] bArr2) {
        AndroidUtilities.runOnUIThread(new TwoStepVerificationActivity$$ExternalSyntheticLambda25(this, checkSecretValues(bArr, (TLRPC$TL_account_passwordSettings) tLObject), bArr2));
    }

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
        AlertsCreator.showUpdateAppAlert(getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131628830), true);
    }

    public /* synthetic */ void lambda$processDone$33(TLRPC$TL_error tLRPC$TL_error) {
        String str;
        if ("SRP_ID_INVALID".equals(tLRPC$TL_error.text)) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TLRPC$TL_account_getPassword(), new TwoStepVerificationActivity$$ExternalSyntheticLambda33(this), 8);
            return;
        }
        needHideProgress();
        if ("PASSWORD_HASH_INVALID".equals(tLRPC$TL_error.text)) {
            onFieldError(this.passwordOutlineView, this.passwordEditText, true);
        } else if (tLRPC$TL_error.text.startsWith("FLOOD_WAIT")) {
            int intValue = Utilities.parseInt((CharSequence) tLRPC$TL_error.text).intValue();
            if (intValue < 60) {
                str = LocaleController.formatPluralString("Seconds", intValue, new Object[0]);
            } else {
                str = LocaleController.formatPluralString("Minutes", intValue / 60, new Object[0]);
            }
            showAlertWithText(LocaleController.getString("AppName", 2131624384), LocaleController.formatString("FloodWaitTime", 2131625950, str));
        } else {
            showAlertWithText(LocaleController.getString("AppName", 2131624384), tLRPC$TL_error.text);
        }
    }

    public /* synthetic */ void lambda$processDone$32(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new TwoStepVerificationActivity$$ExternalSyntheticLambda20(this, tLRPC$TL_error, tLObject));
    }

    public /* synthetic */ void lambda$processDone$31(TLRPC$TL_error tLRPC$TL_error, TLObject tLObject) {
        if (tLRPC$TL_error == null) {
            TLRPC$TL_account_password tLRPC$TL_account_password = (TLRPC$TL_account_password) tLObject;
            this.currentPassword = tLRPC$TL_account_password;
            initPasswordNewAlgo(tLRPC$TL_account_password);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didSetOrRemoveTwoStepPassword, this.currentPassword);
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
        AndroidUtilities.shakeViewSpring(outlineTextContainerView, 5.0f, new TwoStepVerificationActivity$$ExternalSyntheticLambda11(this));
    }

    public /* synthetic */ void lambda$onFieldError$36() {
        AndroidUtilities.cancelRunOnUIThread(this.errorColorTimeout);
        AndroidUtilities.runOnUIThread(this.errorColorTimeout, 1500L);
        this.postedErrorColorTimeout = true;
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            TwoStepVerificationActivity.this = r1;
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
            View view;
            if (i == 0) {
                view = new TextSettingsCell(this.mContext);
                view.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else {
                view = new TextInfoPrivacyCell(this.mContext);
            }
            return new RecyclerListView.Holder(view);
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
                    if (i != TwoStepVerificationActivity.this.passwordEnabledDetailRow) {
                        return;
                    }
                    textInfoPrivacyCell.setText(LocaleController.getString("EnabledPasswordText", 2131625664));
                    textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                    return;
                }
                textInfoPrivacyCell.setText(LocaleController.getString("SetAdditionalPasswordInfo", 2131628298));
                textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                return;
            }
            TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
            textSettingsCell.setTag("windowBackgroundWhiteBlackText");
            textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            if (i != TwoStepVerificationActivity.this.changePasswordRow) {
                if (i != TwoStepVerificationActivity.this.setPasswordRow) {
                    if (i != TwoStepVerificationActivity.this.turnPasswordOffRow) {
                        if (i != TwoStepVerificationActivity.this.changeRecoveryEmailRow) {
                            if (i != TwoStepVerificationActivity.this.setRecoveryEmailRow) {
                                return;
                            }
                            textSettingsCell.setText(LocaleController.getString("SetRecoveryEmail", 2131628309), false);
                            return;
                        }
                        textSettingsCell.setText(LocaleController.getString("ChangeRecoveryEmail", 2131624878), false);
                        return;
                    }
                    textSettingsCell.setText(LocaleController.getString("TurnPasswordOff", 2131628764), true);
                    return;
                }
                textSettingsCell.setText(LocaleController.getString("SetAdditionalPassword", 2131628297), true);
                return;
            }
            textSettingsCell.setText(LocaleController.getString("ChangePassword", 2131624868), true);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return (i == TwoStepVerificationActivity.this.setPasswordDetailRow || i == TwoStepVerificationActivity.this.passwordEnabledDetailRow) ? 1 : 0;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, EditTextSettingsCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteRedText3"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{EditTextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteHintText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        arrayList.add(new ThemeDescription(this.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        arrayList.add(new ThemeDescription(this.bottomTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        arrayList.add(new ThemeDescription(this.bottomButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlueText4"));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, "windowBackgroundWhiteHintText"));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
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

    public void showSetForcePasswordAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("Warning", 2131629298));
        builder.setMessage(LocaleController.formatPluralString("ForceSetPasswordAlertMessageShort", this.otherwiseReloginDays, new Object[0]));
        builder.setPositiveButton(LocaleController.getString("TwoStepVerificationSetPassword", 2131628773), null);
        builder.setNegativeButton(LocaleController.getString("ForceSetPasswordCancel", 2131625976), new TwoStepVerificationActivity$$ExternalSyntheticLambda1(this));
        ((TextView) builder.show().getButton(-2)).setTextColor(Theme.getColor("dialogTextRed2"));
    }

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
        return ColorUtils.calculateLuminance(Theme.getColor("windowBackgroundWhite", null, true)) > 0.699999988079071d;
    }
}
