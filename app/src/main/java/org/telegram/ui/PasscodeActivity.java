package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.support.fingerprint.FingerprintManagerCompat;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.CustomPhoneKeyboardView;
import org.telegram.ui.Components.Easings;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberPicker;
import org.telegram.ui.Components.OutlineTextContainerView;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.TextViewSwitcher;
import org.telegram.ui.Components.TransformableLoginButtonView;
import org.telegram.ui.Components.VerticalPositionAutoAnimator;
/* loaded from: classes3.dex */
public class PasscodeActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
    private int autoLockDetailRow;
    private int autoLockRow;
    private int captureDetailRow;
    private int captureHeaderRow;
    private int captureRow;
    private int changePasscodeRow;
    private CodeFieldContainer codeFieldContainer;
    private TextViewSwitcher descriptionTextSwitcher;
    private int disablePasscodeRow;
    private int fingerprintRow;
    private String firstPassword;
    private VerticalPositionAutoAnimator floatingAutoAnimator;
    private Animator floatingButtonAnimator;
    private FrameLayout floatingButtonContainer;
    private TransformableLoginButtonView floatingButtonIcon;
    private int hintRow;
    private CustomPhoneKeyboardView keyboardView;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private RLottieImageView lockImageView;
    private Runnable onShowKeyboardCallback;
    private ActionBarMenuItem otherItem;
    private OutlineTextContainerView outlinePasswordView;
    private TextView passcodesDoNotMatchTextView;
    private ImageView passwordButton;
    private EditTextBoldCursor passwordEditText;
    private boolean postedHidePasscodesDoNotMatch;
    private int rowCount;
    private TextView titleTextView;
    private int type;
    private int utyanRow;
    private int currentPasswordType = 0;
    private int passcodeSetStep = 0;
    private Runnable hidePasscodesDoNotMatch = new PasscodeActivity$$ExternalSyntheticLambda14(this);

    public /* synthetic */ void lambda$new$0() {
        this.postedHidePasscodesDoNotMatch = false;
        AndroidUtilities.updateViewVisibilityAnimated(this.passcodesDoNotMatchTextView, false);
    }

    public PasscodeActivity(int i) {
        this.type = i;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        updateRows();
        if (this.type == 0) {
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
            return true;
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (this.type == 0) {
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
        }
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x014a  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0170  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01f2  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0207  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0209  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0224  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0227  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x02c5  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x02d2  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x02db  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x03f8 A[LOOP:0: B:62:0x03f6->B:63:0x03f8, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x044f  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x045d  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x04d2  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x04d7  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x04dd  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x04e0  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x052f  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0532  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0536  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0539  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0558  */
    @Override // org.telegram.ui.ActionBar.BaseFragment
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View createView(Context context) {
        ScrollView scrollView;
        int i;
        CodeNumberField[] codeNumberFieldArr;
        int i2;
        ActionBarMenuSubItem actionBarMenuSubItem;
        this.actionBar.setBackButtonImage(2131165449);
        this.actionBar.setAllowOverlayTitle(false);
        this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass1());
        FrameLayout frameLayout = new FrameLayout(context);
        if (this.type == 0) {
            scrollView = frameLayout;
        } else {
            ScrollView scrollView2 = new ScrollView(context);
            scrollView2.addView(frameLayout, LayoutHelper.createFrame(-1, -2.0f));
            scrollView2.setFillViewport(true);
            scrollView = scrollView2;
        }
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context, scrollView);
        anonymousClass2.setDelegate(new PasscodeActivity$$ExternalSyntheticLambda23(this));
        this.fragmentView = anonymousClass2;
        anonymousClass2.addView(scrollView, LayoutHelper.createLinear(-1, 0, 1.0f));
        CustomPhoneKeyboardView customPhoneKeyboardView = new CustomPhoneKeyboardView(context);
        this.keyboardView = customPhoneKeyboardView;
        customPhoneKeyboardView.setVisibility(isCustomKeyboardVisible() ? 0 : 8);
        anonymousClass2.addView(this.keyboardView, LayoutHelper.createLinear(-1, 230));
        int i3 = this.type;
        if (i3 == 0) {
            this.actionBar.setTitle(LocaleController.getString("Passcode", 2131627214));
            frameLayout.setTag("windowBackgroundGray");
            frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
            RecyclerListView recyclerListView = new RecyclerListView(context);
            this.listView = recyclerListView;
            recyclerListView.setLayoutManager(new AnonymousClass3(this, context, 1, false));
            this.listView.setVerticalScrollBarEnabled(false);
            this.listView.setItemAnimator(null);
            this.listView.setLayoutAnimation(null);
            frameLayout.addView(this.listView, LayoutHelper.createFrame(-1, -1.0f));
            RecyclerListView recyclerListView2 = this.listView;
            ListAdapter listAdapter = new ListAdapter(context);
            this.listAdapter = listAdapter;
            recyclerListView2.setAdapter(listAdapter);
            this.listView.setOnItemClickListener(new PasscodeActivity$$ExternalSyntheticLambda22(this));
        } else if (i3 == 1 || i3 == 2) {
            ActionBar actionBar = this.actionBar;
            if (actionBar != null) {
                actionBar.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                this.actionBar.setBackButtonImage(2131165449);
                this.actionBar.setItemsColor(Theme.getColor("windowBackgroundWhiteBlackText"), false);
                this.actionBar.setItemsBackgroundColor(Theme.getColor("actionBarWhiteSelector"), false);
                this.actionBar.setCastShadows(false);
                ActionBarMenu createMenu = this.actionBar.createMenu();
                if (this.type == 1) {
                    ActionBarMenuItem addItem = createMenu.addItem(0, 2131165453);
                    this.otherItem = addItem;
                    actionBarMenuSubItem = addItem.addSubItem(1, 2131165842, LocaleController.getString(2131627222));
                } else {
                    actionBarMenuSubItem = null;
                }
                this.actionBar.setActionBarMenuOnItemClick(new AnonymousClass4(actionBarMenuSubItem));
            }
            FrameLayout frameLayout2 = new FrameLayout(context);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            linearLayout.setGravity(1);
            frameLayout.addView(linearLayout, LayoutHelper.createFrame(-1, -1.0f));
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.lockImageView = rLottieImageView;
            rLottieImageView.setFocusable(false);
            this.lockImageView.setAnimation(2131558579, 120, 120);
            this.lockImageView.setAutoRepeat(false);
            this.lockImageView.playAnimation();
            RLottieImageView rLottieImageView2 = this.lockImageView;
            if (!AndroidUtilities.isSmallScreen()) {
                Point point = AndroidUtilities.displaySize;
                if (point.x < point.y) {
                    i = 0;
                    rLottieImageView2.setVisibility(i);
                    linearLayout.addView(this.lockImageView, LayoutHelper.createLinear(120, 120, 1));
                    TextView textView = new TextView(context);
                    this.titleTextView = textView;
                    textView.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                    if (this.type != 1) {
                        if (SharedConfig.passcodeHash.length() != 0) {
                            this.titleTextView.setText(LocaleController.getString("EnterNewPasscode", 2131625688));
                        } else {
                            this.titleTextView.setText(LocaleController.getString("CreatePasscode", 2131625299));
                        }
                    } else {
                        this.titleTextView.setText(LocaleController.getString(2131625692));
                    }
                    this.titleTextView.setTextSize(1, 18.0f);
                    this.titleTextView.setGravity(1);
                    linearLayout.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 0));
                    TextViewSwitcher textViewSwitcher = new TextViewSwitcher(context);
                    this.descriptionTextSwitcher = textViewSwitcher;
                    textViewSwitcher.setFactory(new PasscodeActivity$$ExternalSyntheticLambda10(context));
                    this.descriptionTextSwitcher.setInAnimation(context, 2130771968);
                    this.descriptionTextSwitcher.setOutAnimation(context, 2130771969);
                    linearLayout.addView(this.descriptionTextSwitcher, LayoutHelper.createLinear(-2, -2, 1, 20, 8, 20, 0));
                    TextView textView2 = new TextView(context);
                    textView2.setTextSize(1, 14.0f);
                    textView2.setTextColor(Theme.getColor("featuredStickers_addButton"));
                    textView2.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
                    textView2.setGravity((!isPassword() ? 3 : 1) | 16);
                    textView2.setOnClickListener(new PasscodeActivity$$ExternalSyntheticLambda4(context));
                    textView2.setVisibility(this.type != 2 ? 0 : 8);
                    textView2.setText(LocaleController.getString(2131625978));
                    frameLayout.addView(textView2, LayoutHelper.createFrame(-1, Build.VERSION.SDK_INT < 21 ? 56.0f : 60.0f, 81, 0.0f, 0.0f, 0.0f, 16.0f));
                    VerticalPositionAutoAnimator.attach(textView2);
                    TextView textView3 = new TextView(context);
                    this.passcodesDoNotMatchTextView = textView3;
                    textView3.setTextSize(1, 14.0f);
                    this.passcodesDoNotMatchTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
                    this.passcodesDoNotMatchTextView.setText(LocaleController.getString(2131627223));
                    this.passcodesDoNotMatchTextView.setPadding(0, AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f));
                    AndroidUtilities.updateViewVisibilityAnimated(this.passcodesDoNotMatchTextView, false, 1.0f, false);
                    frameLayout.addView(this.passcodesDoNotMatchTextView, LayoutHelper.createFrame(-2, -2.0f, 81, 0.0f, 0.0f, 0.0f, 16.0f));
                    OutlineTextContainerView outlineTextContainerView = new OutlineTextContainerView(context);
                    this.outlinePasswordView = outlineTextContainerView;
                    outlineTextContainerView.setText(LocaleController.getString(2131625689));
                    EditTextBoldCursor editTextBoldCursor = new EditTextBoldCursor(context);
                    this.passwordEditText = editTextBoldCursor;
                    editTextBoldCursor.setInputType(524417);
                    this.passwordEditText.setTextSize(1, 18.0f);
                    this.passwordEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    this.passwordEditText.setBackground(null);
                    this.passwordEditText.setMaxLines(1);
                    this.passwordEditText.setLines(1);
                    this.passwordEditText.setGravity(!LocaleController.isRTL ? 5 : 3);
                    this.passwordEditText.setSingleLine(true);
                    if (this.type != 1) {
                        this.passcodeSetStep = 0;
                        this.passwordEditText.setImeOptions(5);
                    } else {
                        this.passcodeSetStep = 1;
                        this.passwordEditText.setImeOptions(6);
                    }
                    this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    this.passwordEditText.setTypeface(Typeface.DEFAULT);
                    this.passwordEditText.setCursorColor(Theme.getColor("windowBackgroundWhiteInputFieldActivated"));
                    this.passwordEditText.setCursorSize(AndroidUtilities.dp(20.0f));
                    this.passwordEditText.setCursorWidth(1.5f);
                    int dp = AndroidUtilities.dp(16.0f);
                    this.passwordEditText.setPadding(dp, dp, dp, dp);
                    this.passwordEditText.setOnFocusChangeListener(new PasscodeActivity$$ExternalSyntheticLambda7(this));
                    LinearLayout linearLayout2 = new LinearLayout(context);
                    linearLayout2.setOrientation(0);
                    linearLayout2.setGravity(16);
                    linearLayout2.addView(this.passwordEditText, LayoutHelper.createLinear(0, -2, 1.0f));
                    ImageView imageView = new ImageView(context);
                    this.passwordButton = imageView;
                    imageView.setImageResource(2131165801);
                    this.passwordButton.setColorFilter(Theme.getColor("windowBackgroundWhiteHintText"));
                    this.passwordButton.setBackground(Theme.createSelectorDrawable(getThemedColor("listSelectorSDK21"), 1));
                    AndroidUtilities.updateViewVisibilityAnimated(this.passwordButton, this.type != 1 && this.passcodeSetStep == 0, 0.1f, false);
                    AtomicBoolean atomicBoolean = new AtomicBoolean(false);
                    this.passwordEditText.addTextChangedListener(new AnonymousClass5(atomicBoolean));
                    this.passwordButton.setOnClickListener(new PasscodeActivity$$ExternalSyntheticLambda6(this, atomicBoolean));
                    linearLayout2.addView(this.passwordButton, LayoutHelper.createLinearRelatively(24.0f, 24.0f, 0, 0.0f, 0.0f, 14.0f, 0.0f));
                    this.outlinePasswordView.addView(linearLayout2, LayoutHelper.createFrame(-1, -2.0f));
                    frameLayout2.addView(this.outlinePasswordView, LayoutHelper.createLinear(-1, -2, 1, 32, 0, 32, 0));
                    this.passwordEditText.setOnEditorActionListener(new PasscodeActivity$$ExternalSyntheticLambda9(this));
                    this.passwordEditText.addTextChangedListener(new AnonymousClass6());
                    this.passwordEditText.setCustomSelectionActionModeCallback(new AnonymousClass7(this));
                    AnonymousClass8 anonymousClass8 = new AnonymousClass8(context);
                    this.codeFieldContainer = anonymousClass8;
                    anonymousClass8.setNumbersCount(4, 10);
                    for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                        codeNumberField.setShowSoftInputOnFocusCompat(!isCustomKeyboardVisible());
                        codeNumberField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        codeNumberField.setTextSize(1, 24.0f);
                        codeNumberField.addTextChangedListener(new AnonymousClass9());
                        codeNumberField.setOnFocusChangeListener(new PasscodeActivity$$ExternalSyntheticLambda8(this, codeNumberField));
                    }
                    frameLayout2.addView(this.codeFieldContainer, LayoutHelper.createFrame(-2, -2.0f, 1, 40.0f, 10.0f, 40.0f, 0.0f));
                    linearLayout.addView(frameLayout2, LayoutHelper.createLinear(-1, -2, 1, 0, 32, 0, 72));
                    if (this.type == 1) {
                        frameLayout.setTag("windowBackgroundWhite");
                    }
                    this.floatingButtonContainer = new FrameLayout(context);
                    i2 = Build.VERSION.SDK_INT;
                    if (i2 >= 21) {
                        StateListAnimator stateListAnimator = new StateListAnimator();
                        stateListAnimator.addState(new int[]{16842919}, ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(2.0f), AndroidUtilities.dp(4.0f)).setDuration(200L));
                        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(this.floatingButtonIcon, "translationZ", AndroidUtilities.dp(4.0f), AndroidUtilities.dp(2.0f)).setDuration(200L));
                        this.floatingButtonContainer.setStateListAnimator(stateListAnimator);
                        this.floatingButtonContainer.setOutlineProvider(new AnonymousClass10(this));
                    }
                    this.floatingAutoAnimator = VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
                    frameLayout.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
                    this.floatingButtonContainer.setOnClickListener(new PasscodeActivity$$ExternalSyntheticLambda5(this));
                    TransformableLoginButtonView transformableLoginButtonView = new TransformableLoginButtonView(context);
                    this.floatingButtonIcon = transformableLoginButtonView;
                    transformableLoginButtonView.setTransformType(1);
                    this.floatingButtonIcon.setProgress(0.0f);
                    this.floatingButtonIcon.setColor(Theme.getColor("chats_actionIcon"));
                    this.floatingButtonIcon.setDrawBackground(false);
                    this.floatingButtonContainer.setContentDescription(LocaleController.getString(2131626853));
                    this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f));
                    Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
                    if (i2 < 21) {
                        Drawable mutate = context.getResources().getDrawable(2131165414).mutate();
                        mutate.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.MULTIPLY));
                        CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, createSimpleSelectorCircleDrawable, 0, 0);
                        combinedDrawable.setIconSize(AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
                        createSimpleSelectorCircleDrawable = combinedDrawable;
                    }
                    this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable);
                    updateFields();
                }
            }
            i = 8;
            rLottieImageView2.setVisibility(i);
            linearLayout.addView(this.lockImageView, LayoutHelper.createLinear(120, 120, 1));
            TextView textView4 = new TextView(context);
            this.titleTextView = textView4;
            textView4.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            if (this.type != 1) {
            }
            this.titleTextView.setTextSize(1, 18.0f);
            this.titleTextView.setGravity(1);
            linearLayout.addView(this.titleTextView, LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 0));
            TextViewSwitcher textViewSwitcher2 = new TextViewSwitcher(context);
            this.descriptionTextSwitcher = textViewSwitcher2;
            textViewSwitcher2.setFactory(new PasscodeActivity$$ExternalSyntheticLambda10(context));
            this.descriptionTextSwitcher.setInAnimation(context, 2130771968);
            this.descriptionTextSwitcher.setOutAnimation(context, 2130771969);
            linearLayout.addView(this.descriptionTextSwitcher, LayoutHelper.createLinear(-2, -2, 1, 20, 8, 20, 0));
            TextView textView22 = new TextView(context);
            textView22.setTextSize(1, 14.0f);
            textView22.setTextColor(Theme.getColor("featuredStickers_addButton"));
            textView22.setPadding(AndroidUtilities.dp(32.0f), 0, AndroidUtilities.dp(32.0f), 0);
            textView22.setGravity((!isPassword() ? 3 : 1) | 16);
            textView22.setOnClickListener(new PasscodeActivity$$ExternalSyntheticLambda4(context));
            textView22.setVisibility(this.type != 2 ? 0 : 8);
            textView22.setText(LocaleController.getString(2131625978));
            frameLayout.addView(textView22, LayoutHelper.createFrame(-1, Build.VERSION.SDK_INT < 21 ? 56.0f : 60.0f, 81, 0.0f, 0.0f, 0.0f, 16.0f));
            VerticalPositionAutoAnimator.attach(textView22);
            TextView textView32 = new TextView(context);
            this.passcodesDoNotMatchTextView = textView32;
            textView32.setTextSize(1, 14.0f);
            this.passcodesDoNotMatchTextView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
            this.passcodesDoNotMatchTextView.setText(LocaleController.getString(2131627223));
            this.passcodesDoNotMatchTextView.setPadding(0, AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f));
            AndroidUtilities.updateViewVisibilityAnimated(this.passcodesDoNotMatchTextView, false, 1.0f, false);
            frameLayout.addView(this.passcodesDoNotMatchTextView, LayoutHelper.createFrame(-2, -2.0f, 81, 0.0f, 0.0f, 0.0f, 16.0f));
            OutlineTextContainerView outlineTextContainerView2 = new OutlineTextContainerView(context);
            this.outlinePasswordView = outlineTextContainerView2;
            outlineTextContainerView2.setText(LocaleController.getString(2131625689));
            EditTextBoldCursor editTextBoldCursor2 = new EditTextBoldCursor(context);
            this.passwordEditText = editTextBoldCursor2;
            editTextBoldCursor2.setInputType(524417);
            this.passwordEditText.setTextSize(1, 18.0f);
            this.passwordEditText.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            this.passwordEditText.setBackground(null);
            this.passwordEditText.setMaxLines(1);
            this.passwordEditText.setLines(1);
            this.passwordEditText.setGravity(!LocaleController.isRTL ? 5 : 3);
            this.passwordEditText.setSingleLine(true);
            if (this.type != 1) {
            }
            this.passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.passwordEditText.setTypeface(Typeface.DEFAULT);
            this.passwordEditText.setCursorColor(Theme.getColor("windowBackgroundWhiteInputFieldActivated"));
            this.passwordEditText.setCursorSize(AndroidUtilities.dp(20.0f));
            this.passwordEditText.setCursorWidth(1.5f);
            int dp2 = AndroidUtilities.dp(16.0f);
            this.passwordEditText.setPadding(dp2, dp2, dp2, dp2);
            this.passwordEditText.setOnFocusChangeListener(new PasscodeActivity$$ExternalSyntheticLambda7(this));
            LinearLayout linearLayout22 = new LinearLayout(context);
            linearLayout22.setOrientation(0);
            linearLayout22.setGravity(16);
            linearLayout22.addView(this.passwordEditText, LayoutHelper.createLinear(0, -2, 1.0f));
            ImageView imageView2 = new ImageView(context);
            this.passwordButton = imageView2;
            imageView2.setImageResource(2131165801);
            this.passwordButton.setColorFilter(Theme.getColor("windowBackgroundWhiteHintText"));
            this.passwordButton.setBackground(Theme.createSelectorDrawable(getThemedColor("listSelectorSDK21"), 1));
            AndroidUtilities.updateViewVisibilityAnimated(this.passwordButton, this.type != 1 && this.passcodeSetStep == 0, 0.1f, false);
            AtomicBoolean atomicBoolean2 = new AtomicBoolean(false);
            this.passwordEditText.addTextChangedListener(new AnonymousClass5(atomicBoolean2));
            this.passwordButton.setOnClickListener(new PasscodeActivity$$ExternalSyntheticLambda6(this, atomicBoolean2));
            linearLayout22.addView(this.passwordButton, LayoutHelper.createLinearRelatively(24.0f, 24.0f, 0, 0.0f, 0.0f, 14.0f, 0.0f));
            this.outlinePasswordView.addView(linearLayout22, LayoutHelper.createFrame(-1, -2.0f));
            frameLayout2.addView(this.outlinePasswordView, LayoutHelper.createLinear(-1, -2, 1, 32, 0, 32, 0));
            this.passwordEditText.setOnEditorActionListener(new PasscodeActivity$$ExternalSyntheticLambda9(this));
            this.passwordEditText.addTextChangedListener(new AnonymousClass6());
            this.passwordEditText.setCustomSelectionActionModeCallback(new AnonymousClass7(this));
            AnonymousClass8 anonymousClass82 = new AnonymousClass8(context);
            this.codeFieldContainer = anonymousClass82;
            anonymousClass82.setNumbersCount(4, 10);
            while (r9 < r7) {
            }
            frameLayout2.addView(this.codeFieldContainer, LayoutHelper.createFrame(-2, -2.0f, 1, 40.0f, 10.0f, 40.0f, 0.0f));
            linearLayout.addView(frameLayout2, LayoutHelper.createLinear(-1, -2, 1, 0, 32, 0, 72));
            if (this.type == 1) {
            }
            this.floatingButtonContainer = new FrameLayout(context);
            i2 = Build.VERSION.SDK_INT;
            if (i2 >= 21) {
            }
            this.floatingAutoAnimator = VerticalPositionAutoAnimator.attach(this.floatingButtonContainer);
            frameLayout.addView(this.floatingButtonContainer, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f, 85, 0.0f, 0.0f, 24.0f, 16.0f));
            this.floatingButtonContainer.setOnClickListener(new PasscodeActivity$$ExternalSyntheticLambda5(this));
            TransformableLoginButtonView transformableLoginButtonView2 = new TransformableLoginButtonView(context);
            this.floatingButtonIcon = transformableLoginButtonView2;
            transformableLoginButtonView2.setTransformType(1);
            this.floatingButtonIcon.setProgress(0.0f);
            this.floatingButtonIcon.setColor(Theme.getColor("chats_actionIcon"));
            this.floatingButtonIcon.setDrawBackground(false);
            this.floatingButtonContainer.setContentDescription(LocaleController.getString(2131626853));
            this.floatingButtonContainer.addView(this.floatingButtonIcon, LayoutHelper.createFrame(i2 < 21 ? 56 : 60, i2 < 21 ? 56.0f : 60.0f));
            Drawable createSimpleSelectorCircleDrawable2 = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(56.0f), Theme.getColor("chats_actionBackground"), Theme.getColor("chats_actionPressedBackground"));
            if (i2 < 21) {
            }
            this.floatingButtonContainer.setBackground(createSimpleSelectorCircleDrawable2);
            updateFields();
        }
        return this.fragmentView;
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$1 */
    /* loaded from: classes3.dex */
    class AnonymousClass1 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass1() {
            PasscodeActivity.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                PasscodeActivity.this.finishFragment();
            }
        }
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$2 */
    /* loaded from: classes3.dex */
    class AnonymousClass2 extends SizeNotifierFrameLayout {
        final /* synthetic */ View val$fragmentContentView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context, View view) {
            super(context);
            PasscodeActivity.this = r1;
            this.val$fragmentContentView = view;
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            if (PasscodeActivity.this.keyboardView.getVisibility() == 8 || measureKeyboardHeight() < AndroidUtilities.dp(20.0f)) {
                if (PasscodeActivity.this.keyboardView.getVisibility() != 8) {
                    View view = this.val$fragmentContentView;
                    int measuredWidth = getMeasuredWidth();
                    i5 = getMeasuredHeight() - AndroidUtilities.dp(230.0f);
                    view.layout(0, 0, measuredWidth, i5);
                } else {
                    View view2 = this.val$fragmentContentView;
                    int measuredWidth2 = getMeasuredWidth();
                    i5 = getMeasuredHeight();
                    view2.layout(0, 0, measuredWidth2, i5);
                }
            } else if (PasscodeActivity.this.isCustomKeyboardVisible()) {
                View view3 = this.val$fragmentContentView;
                int measuredWidth3 = getMeasuredWidth();
                i5 = (getMeasuredHeight() - AndroidUtilities.dp(230.0f)) + measureKeyboardHeight();
                view3.layout(0, 0, measuredWidth3, i5);
            } else {
                View view4 = this.val$fragmentContentView;
                int measuredWidth4 = getMeasuredWidth();
                i5 = getMeasuredHeight();
                view4.layout(0, 0, measuredWidth4, i5);
            }
            PasscodeActivity.this.keyboardView.layout(0, i5, getMeasuredWidth(), AndroidUtilities.dp(230.0f) + i5);
            notifyHeightChanged();
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            setMeasuredDimension(size, size2);
            if (PasscodeActivity.this.keyboardView.getVisibility() != 8 && measureKeyboardHeight() < AndroidUtilities.dp(20.0f)) {
                size2 -= AndroidUtilities.dp(230.0f);
            }
            this.val$fragmentContentView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(size2, 1073741824));
            PasscodeActivity.this.keyboardView.measure(View.MeasureSpec.makeMeasureSpec(size, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(230.0f), 1073741824));
        }
    }

    public /* synthetic */ void lambda$createView$1(int i, boolean z) {
        Runnable runnable;
        if (i < AndroidUtilities.dp(20.0f) || (runnable = this.onShowKeyboardCallback) == null) {
            return;
        }
        runnable.run();
        this.onShowKeyboardCallback = null;
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$3 */
    /* loaded from: classes3.dex */
    class AnonymousClass3 extends LinearLayoutManager {
        @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        AnonymousClass3(PasscodeActivity passcodeActivity, Context context, int i, boolean z) {
            super(context, i, z);
        }
    }

    public /* synthetic */ void lambda$createView$5(View view, int i) {
        if (!view.isEnabled()) {
            return;
        }
        if (i == this.disablePasscodeRow) {
            AlertDialog create = new AlertDialog.Builder(getParentActivity()).setTitle(LocaleController.getString(2131625496)).setMessage(LocaleController.getString(2131625497)).setNegativeButton(LocaleController.getString(2131624832), null).setPositiveButton(LocaleController.getString(2131625498), new PasscodeActivity$$ExternalSyntheticLambda2(this)).create();
            create.show();
            ((TextView) create.getButton(-1)).setTextColor(Theme.getColor("dialogTextRed"));
        } else if (i == this.changePasscodeRow) {
            presentFragment(new PasscodeActivity(1));
        } else if (i == this.autoLockRow) {
            if (getParentActivity() == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AutoLock", 2131624611));
            NumberPicker numberPicker = new NumberPicker(getParentActivity());
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(4);
            int i2 = SharedConfig.autoLockIn;
            if (i2 == 0) {
                numberPicker.setValue(0);
            } else if (i2 == 60) {
                numberPicker.setValue(1);
            } else if (i2 == 300) {
                numberPicker.setValue(2);
            } else if (i2 == 3600) {
                numberPicker.setValue(3);
            } else if (i2 == 18000) {
                numberPicker.setValue(4);
            }
            numberPicker.setFormatter(PasscodeActivity$$ExternalSyntheticLambda21.INSTANCE);
            builder.setView(numberPicker);
            builder.setNegativeButton(LocaleController.getString("Done", 2131625541), new PasscodeActivity$$ExternalSyntheticLambda3(this, numberPicker, i));
            showDialog(builder.create());
        } else if (i == this.fingerprintRow) {
            SharedConfig.useFingerprint = !SharedConfig.useFingerprint;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            ((TextCheckCell) view).setChecked(SharedConfig.useFingerprint);
        } else if (i != this.captureRow) {
        } else {
            SharedConfig.allowScreenCapture = !SharedConfig.allowScreenCapture;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            ((TextCheckCell) view).setChecked(SharedConfig.allowScreenCapture);
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode, Boolean.FALSE);
            if (SharedConfig.allowScreenCapture) {
                return;
            }
            AlertsCreator.showSimpleAlert(this, LocaleController.getString("ScreenCaptureAlert", 2131628149));
        }
    }

    public /* synthetic */ void lambda$createView$2(DialogInterface dialogInterface, int i) {
        SharedConfig.passcodeHash = "";
        SharedConfig.appLocked = false;
        SharedConfig.saveConfig();
        getMediaDataController().buildShortcuts();
        int childCount = this.listView.getChildCount();
        int i2 = 0;
        while (true) {
            if (i2 >= childCount) {
                break;
            }
            View childAt = this.listView.getChildAt(i2);
            if (childAt instanceof TextSettingsCell) {
                ((TextSettingsCell) childAt).setTextColor(Theme.getColor("windowBackgroundWhiteGrayText7"));
                break;
            }
            i2++;
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode, new Object[0]);
        finishFragment();
    }

    public static /* synthetic */ String lambda$createView$3(int i) {
        if (i == 0) {
            return LocaleController.getString("AutoLockDisabled", 2131624612);
        }
        return i == 1 ? LocaleController.formatString("AutoLockInTime", 2131624613, LocaleController.formatPluralString("Minutes", 1, new Object[0])) : i == 2 ? LocaleController.formatString("AutoLockInTime", 2131624613, LocaleController.formatPluralString("Minutes", 5, new Object[0])) : i == 3 ? LocaleController.formatString("AutoLockInTime", 2131624613, LocaleController.formatPluralString("Hours", 1, new Object[0])) : i == 4 ? LocaleController.formatString("AutoLockInTime", 2131624613, LocaleController.formatPluralString("Hours", 5, new Object[0])) : "";
    }

    public /* synthetic */ void lambda$createView$4(NumberPicker numberPicker, int i, DialogInterface dialogInterface, int i2) {
        int value = numberPicker.getValue();
        if (value == 0) {
            SharedConfig.autoLockIn = 0;
        } else if (value == 1) {
            SharedConfig.autoLockIn = 60;
        } else if (value == 2) {
            SharedConfig.autoLockIn = 300;
        } else if (value == 3) {
            SharedConfig.autoLockIn = 3600;
        } else if (value == 4) {
            SharedConfig.autoLockIn = 18000;
        }
        this.listAdapter.notifyItemChanged(i);
        UserConfig.getInstance(this.currentAccount).saveConfig(false);
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends ActionBar.ActionBarMenuOnItemClick {
        final /* synthetic */ ActionBarMenuSubItem val$switchItem;

        AnonymousClass4(ActionBarMenuSubItem actionBarMenuSubItem) {
            PasscodeActivity.this = r1;
            this.val$switchItem = actionBarMenuSubItem;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                PasscodeActivity.this.finishFragment();
                return;
            }
            int i2 = 1;
            if (i != 1) {
                return;
            }
            PasscodeActivity passcodeActivity = PasscodeActivity.this;
            if (passcodeActivity.currentPasswordType != 0) {
                i2 = 0;
            }
            passcodeActivity.currentPasswordType = i2;
            AndroidUtilities.runOnUIThread(new PasscodeActivity$4$$ExternalSyntheticLambda0(this, this.val$switchItem), 150L);
            PasscodeActivity.this.passwordEditText.setText("");
            for (CodeNumberField codeNumberField : PasscodeActivity.this.codeFieldContainer.codeField) {
                codeNumberField.setText("");
            }
            PasscodeActivity.this.updateFields();
        }

        public /* synthetic */ void lambda$onItemClick$0(ActionBarMenuSubItem actionBarMenuSubItem) {
            actionBarMenuSubItem.setText(LocaleController.getString(PasscodeActivity.this.currentPasswordType == 0 ? 2131627222 : 2131627221));
            actionBarMenuSubItem.setIcon(PasscodeActivity.this.currentPasswordType == 0 ? 2131165842 : 2131165861);
            PasscodeActivity.this.showKeyboard();
            if (PasscodeActivity.this.isPinCode()) {
                PasscodeActivity.this.passwordEditText.setInputType(524417);
                AndroidUtilities.updateViewVisibilityAnimated(PasscodeActivity.this.passwordButton, true, 0.1f, false);
            }
        }
    }

    public static /* synthetic */ View lambda$createView$6(Context context) {
        TextView textView = new TextView(context);
        textView.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText6"));
        textView.setGravity(1);
        textView.setLineSpacing(AndroidUtilities.dp(2.0f), 1.0f);
        textView.setTextSize(1, 15.0f);
        return textView;
    }

    public static /* synthetic */ void lambda$createView$7(Context context, View view) {
        AlertsCreator.createForgotPasscodeDialog(context).show();
    }

    public /* synthetic */ void lambda$createView$8(View view, boolean z) {
        this.outlinePasswordView.animateSelection(z ? 1.0f : 0.0f);
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$5 */
    /* loaded from: classes3.dex */
    class AnonymousClass5 implements TextWatcher {
        final /* synthetic */ AtomicBoolean val$isPasswordShown;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass5(AtomicBoolean atomicBoolean) {
            PasscodeActivity.this = r1;
            this.val$isPasswordShown = atomicBoolean;
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (PasscodeActivity.this.type == 1 && PasscodeActivity.this.passcodeSetStep == 0) {
                if (TextUtils.isEmpty(editable) && PasscodeActivity.this.passwordButton.getVisibility() != 8) {
                    if (this.val$isPasswordShown.get()) {
                        PasscodeActivity.this.passwordButton.callOnClick();
                    }
                    AndroidUtilities.updateViewVisibilityAnimated(PasscodeActivity.this.passwordButton, false, 0.1f, true);
                } else if (TextUtils.isEmpty(editable) || PasscodeActivity.this.passwordButton.getVisibility() == 0) {
                } else {
                    AndroidUtilities.updateViewVisibilityAnimated(PasscodeActivity.this.passwordButton, true, 0.1f, true);
                }
            }
        }
    }

    public /* synthetic */ void lambda$createView$9(AtomicBoolean atomicBoolean, View view) {
        atomicBoolean.set(!atomicBoolean.get());
        int selectionStart = this.passwordEditText.getSelectionStart();
        int selectionEnd = this.passwordEditText.getSelectionEnd();
        this.passwordEditText.setInputType((atomicBoolean.get() ? 144 : 128) | 1);
        this.passwordEditText.setSelection(selectionStart, selectionEnd);
        this.passwordButton.setColorFilter(Theme.getColor(atomicBoolean.get() ? "windowBackgroundWhiteInputFieldActivated" : "windowBackgroundWhiteHintText"));
    }

    public /* synthetic */ boolean lambda$createView$10(TextView textView, int i, KeyEvent keyEvent) {
        int i2 = this.passcodeSetStep;
        if (i2 == 0) {
            processNext();
            return true;
        } else if (i2 != 1) {
            return false;
        } else {
            processDone();
            return true;
        }
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$6 */
    /* loaded from: classes3.dex */
    class AnonymousClass6 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass6() {
            PasscodeActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (PasscodeActivity.this.postedHidePasscodesDoNotMatch) {
                PasscodeActivity.this.codeFieldContainer.removeCallbacks(PasscodeActivity.this.hidePasscodesDoNotMatch);
                PasscodeActivity.this.hidePasscodesDoNotMatch.run();
            }
        }
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$7 */
    /* loaded from: classes3.dex */
    class AnonymousClass7 implements ActionMode.Callback {
        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        AnonymousClass7(PasscodeActivity passcodeActivity) {
        }
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$8 */
    /* loaded from: classes3.dex */
    public class AnonymousClass8 extends CodeFieldContainer {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass8(Context context) {
            super(context);
            PasscodeActivity.this = r1;
        }

        @Override // org.telegram.ui.CodeFieldContainer
        protected void processNextPressed() {
            if (PasscodeActivity.this.passcodeSetStep != 0) {
                PasscodeActivity.this.processDone();
            } else {
                postDelayed(new PasscodeActivity$8$$ExternalSyntheticLambda0(this), 260L);
            }
        }

        public /* synthetic */ void lambda$processNextPressed$0() {
            PasscodeActivity.this.processNext();
        }
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$9 */
    /* loaded from: classes3.dex */
    class AnonymousClass9 implements TextWatcher {
        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        AnonymousClass9() {
            PasscodeActivity.this = r1;
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (PasscodeActivity.this.postedHidePasscodesDoNotMatch) {
                PasscodeActivity.this.codeFieldContainer.removeCallbacks(PasscodeActivity.this.hidePasscodesDoNotMatch);
                PasscodeActivity.this.hidePasscodesDoNotMatch.run();
            }
        }
    }

    public /* synthetic */ void lambda$createView$11(CodeNumberField codeNumberField, View view, boolean z) {
        this.keyboardView.setEditText(codeNumberField);
        this.keyboardView.setDispatchBackWhenEmpty(true);
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$10 */
    /* loaded from: classes3.dex */
    class AnonymousClass10 extends ViewOutlineProvider {
        AnonymousClass10(PasscodeActivity passcodeActivity) {
        }

        @Override // android.view.ViewOutlineProvider
        @SuppressLint({"NewApi"})
        public void getOutline(View view, Outline outline) {
            outline.setOval(0, 0, AndroidUtilities.dp(56.0f), AndroidUtilities.dp(56.0f));
        }
    }

    public /* synthetic */ void lambda$createView$12(View view) {
        int i = this.type;
        if (i != 1) {
            if (i != 2) {
                return;
            }
            processDone();
        } else if (this.passcodeSetStep == 0) {
            processNext();
        } else {
            processDone();
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public boolean hasForceLightStatusBar() {
        return this.type != 0;
    }

    private void setCustomKeyboardVisible(boolean z, boolean z2) {
        if (z) {
            AndroidUtilities.hideKeyboard(this.fragmentView);
            AndroidUtilities.requestAltFocusable(getParentActivity(), this.classGuid);
        } else {
            AndroidUtilities.removeAltFocusable(getParentActivity(), this.classGuid);
        }
        int i = 0;
        float f = 1.0f;
        float f2 = 0.0f;
        if (!z2) {
            CustomPhoneKeyboardView customPhoneKeyboardView = this.keyboardView;
            if (!z) {
                i = 8;
            }
            customPhoneKeyboardView.setVisibility(i);
            CustomPhoneKeyboardView customPhoneKeyboardView2 = this.keyboardView;
            if (!z) {
                f = 0.0f;
            }
            customPhoneKeyboardView2.setAlpha(f);
            CustomPhoneKeyboardView customPhoneKeyboardView3 = this.keyboardView;
            if (!z) {
                f2 = AndroidUtilities.dp(230.0f);
            }
            customPhoneKeyboardView3.setTranslationY(f2);
            this.fragmentView.requestLayout();
            return;
        }
        float[] fArr = new float[2];
        fArr[0] = z ? 0.0f : 1.0f;
        if (!z) {
            f = 0.0f;
        }
        fArr[1] = f;
        ValueAnimator duration = ValueAnimator.ofFloat(fArr).setDuration(150L);
        duration.setInterpolator(z ? CubicBezierInterpolator.DEFAULT : Easings.easeInOutQuad);
        duration.addUpdateListener(new PasscodeActivity$$ExternalSyntheticLambda0(this));
        duration.addListener(new AnonymousClass11(z));
        duration.start();
    }

    public /* synthetic */ void lambda$setCustomKeyboardVisible$13(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.keyboardView.setAlpha(floatValue);
        this.keyboardView.setTranslationY((1.0f - floatValue) * AndroidUtilities.dp(230.0f) * 0.75f);
        this.fragmentView.requestLayout();
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$11 */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$visible;

        AnonymousClass11(boolean z) {
            PasscodeActivity.this = r1;
            this.val$visible = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (this.val$visible) {
                PasscodeActivity.this.keyboardView.setVisibility(0);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!this.val$visible) {
                PasscodeActivity.this.keyboardView.setVisibility(8);
            }
        }
    }

    private void setFloatingButtonVisible(boolean z, boolean z2) {
        Animator animator = this.floatingButtonAnimator;
        if (animator != null) {
            animator.cancel();
            this.floatingButtonAnimator = null;
        }
        int i = 0;
        float f = 1.0f;
        if (!z2) {
            this.floatingAutoAnimator.setOffsetY(z ? 0.0f : AndroidUtilities.dp(70.0f));
            FrameLayout frameLayout = this.floatingButtonContainer;
            if (!z) {
                f = 0.0f;
            }
            frameLayout.setAlpha(f);
            FrameLayout frameLayout2 = this.floatingButtonContainer;
            if (!z) {
                i = 8;
            }
            frameLayout2.setVisibility(i);
            return;
        }
        float[] fArr = new float[2];
        fArr[0] = z ? 0.0f : 1.0f;
        if (!z) {
            f = 0.0f;
        }
        fArr[1] = f;
        ValueAnimator duration = ValueAnimator.ofFloat(fArr).setDuration(150L);
        duration.setInterpolator(z ? AndroidUtilities.decelerateInterpolator : AndroidUtilities.accelerateInterpolator);
        duration.addUpdateListener(new PasscodeActivity$$ExternalSyntheticLambda1(this));
        duration.addListener(new AnonymousClass12(z));
        duration.start();
        this.floatingButtonAnimator = duration;
    }

    public /* synthetic */ void lambda$setFloatingButtonVisible$14(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.floatingAutoAnimator.setOffsetY(AndroidUtilities.dp(70.0f) * (1.0f - floatValue));
        this.floatingButtonContainer.setAlpha(floatValue);
    }

    /* renamed from: org.telegram.ui.PasscodeActivity$12 */
    /* loaded from: classes3.dex */
    public class AnonymousClass12 extends AnimatorListenerAdapter {
        final /* synthetic */ boolean val$visible;

        AnonymousClass12(boolean z) {
            PasscodeActivity.this = r1;
            this.val$visible = z;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (this.val$visible) {
                PasscodeActivity.this.floatingButtonContainer.setVisibility(0);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!this.val$visible) {
                PasscodeActivity.this.floatingButtonContainer.setVisibility(8);
            }
            if (PasscodeActivity.this.floatingButtonAnimator == animator) {
                PasscodeActivity.this.floatingButtonAnimator = null;
            }
        }
    }

    public static BaseFragment determineOpenFragment() {
        if (SharedConfig.passcodeHash.length() != 0) {
            return new PasscodeActivity(2);
        }
        return new ActionIntroActivity(6);
    }

    private void animateSuccessAnimation(Runnable runnable) {
        if (!isPinCode()) {
            runnable.run();
            return;
        }
        int i = 0;
        while (true) {
            CodeFieldContainer codeFieldContainer = this.codeFieldContainer;
            CodeNumberField[] codeNumberFieldArr = codeFieldContainer.codeField;
            if (i < codeNumberFieldArr.length) {
                CodeNumberField codeNumberField = codeNumberFieldArr[i];
                codeNumberField.postDelayed(new PasscodeActivity$$ExternalSyntheticLambda11(codeNumberField), i * 75);
                i++;
            } else {
                codeFieldContainer.postDelayed(new PasscodeActivity$$ExternalSyntheticLambda18(this, runnable), (this.codeFieldContainer.codeField.length * 75) + 350);
                return;
            }
        }
    }

    public /* synthetic */ void lambda$animateSuccessAnimation$16(Runnable runnable) {
        for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
            codeNumberField.animateSuccessProgress(0.0f);
        }
        runnable.run();
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onConfigurationChanged(Configuration configuration) {
        int i;
        super.onConfigurationChanged(configuration);
        setCustomKeyboardVisible(isCustomKeyboardVisible(), false);
        RLottieImageView rLottieImageView = this.lockImageView;
        if (rLottieImageView != null) {
            if (!AndroidUtilities.isSmallScreen()) {
                Point point = AndroidUtilities.displaySize;
                if (point.x < point.y) {
                    i = 0;
                    rLottieImageView.setVisibility(i);
                }
            }
            i = 8;
            rLottieImageView.setVisibility(i);
        }
        for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
            codeNumberField.setShowSoftInputOnFocusCompat(!isCustomKeyboardVisible());
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onResume() {
        super.onResume();
        ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
        if (this.type != 0 && !isCustomKeyboardVisible()) {
            AndroidUtilities.runOnUIThread(new PasscodeActivity$$ExternalSyntheticLambda15(this), 200L);
        }
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        if (isCustomKeyboardVisible()) {
            AndroidUtilities.hideKeyboard(this.fragmentView);
            AndroidUtilities.requestAltFocusable(getParentActivity(), this.classGuid);
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onPause() {
        super.onPause();
        AndroidUtilities.removeAltFocusable(getParentActivity(), this.classGuid);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.didSetPasscode) {
            if ((objArr.length != 0 && !((Boolean) objArr[0]).booleanValue()) || this.type != 0) {
                return;
            }
            updateRows();
            ListAdapter listAdapter = this.listAdapter;
            if (listAdapter == null) {
                return;
            }
            listAdapter.notifyDataSetChanged();
        }
    }

    private void updateRows() {
        this.rowCount = 0;
        int i = 0 + 1;
        this.rowCount = i;
        this.utyanRow = 0;
        int i2 = i + 1;
        this.rowCount = i2;
        this.hintRow = i;
        this.rowCount = i2 + 1;
        this.changePasscodeRow = i2;
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (FingerprintManagerCompat.from(ApplicationLoader.applicationContext).isHardwareDetected() && AndroidUtilities.isKeyguardSecure()) {
                    int i3 = this.rowCount;
                    this.rowCount = i3 + 1;
                    this.fingerprintRow = i3;
                } else {
                    this.fingerprintRow = -1;
                }
            } else {
                this.fingerprintRow = -1;
            }
        } catch (Throwable th) {
            FileLog.e(th);
        }
        int i4 = this.rowCount;
        int i5 = i4 + 1;
        this.rowCount = i5;
        this.autoLockRow = i4;
        int i6 = i5 + 1;
        this.rowCount = i6;
        this.autoLockDetailRow = i5;
        int i7 = i6 + 1;
        this.rowCount = i7;
        this.captureHeaderRow = i6;
        int i8 = i7 + 1;
        this.rowCount = i8;
        this.captureRow = i7;
        int i9 = i8 + 1;
        this.rowCount = i9;
        this.captureDetailRow = i8;
        this.rowCount = i9 + 1;
        this.disablePasscodeRow = i9;
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public void onTransitionAnimationEnd(boolean z, boolean z2) {
        if (!z || this.type == 0) {
            return;
        }
        showKeyboard();
    }

    public void showKeyboard() {
        if (isPinCode()) {
            this.codeFieldContainer.codeField[0].requestFocus();
            if (isCustomKeyboardVisible()) {
                return;
            }
            AndroidUtilities.showKeyboard(this.codeFieldContainer.codeField[0]);
        } else if (!isPassword()) {
        } else {
            this.passwordEditText.requestFocus();
            AndroidUtilities.showKeyboard(this.passwordEditText);
        }
    }

    public void updateFields() {
        String str;
        int i = 2131625300;
        if (this.type == 2) {
            str = LocaleController.getString(2131625693);
        } else if (this.passcodeSetStep == 0) {
            str = LocaleController.getString(this.currentPasswordType == 0 ? 2131625300 : 2131625301);
        } else {
            str = this.descriptionTextSwitcher.getCurrentView().getText().toString();
        }
        boolean z = !this.descriptionTextSwitcher.getCurrentView().getText().equals(str) && !TextUtils.isEmpty(this.descriptionTextSwitcher.getCurrentView().getText());
        if (this.type == 2) {
            this.descriptionTextSwitcher.setText(LocaleController.getString(2131625693), z);
        } else if (this.passcodeSetStep == 0) {
            TextViewSwitcher textViewSwitcher = this.descriptionTextSwitcher;
            if (this.currentPasswordType != 0) {
                i = 2131625301;
            }
            textViewSwitcher.setText(LocaleController.getString(i), z);
        }
        if (isPinCode()) {
            AndroidUtilities.updateViewVisibilityAnimated(this.codeFieldContainer, true, 1.0f, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.outlinePasswordView, false, 1.0f, z);
        } else if (isPassword()) {
            AndroidUtilities.updateViewVisibilityAnimated(this.codeFieldContainer, false, 1.0f, z);
            AndroidUtilities.updateViewVisibilityAnimated(this.outlinePasswordView, true, 1.0f, z);
        }
        boolean isPassword = isPassword();
        if (isPassword) {
            PasscodeActivity$$ExternalSyntheticLambda20 passcodeActivity$$ExternalSyntheticLambda20 = new PasscodeActivity$$ExternalSyntheticLambda20(this, isPassword, z);
            this.onShowKeyboardCallback = passcodeActivity$$ExternalSyntheticLambda20;
            AndroidUtilities.runOnUIThread(passcodeActivity$$ExternalSyntheticLambda20, 3000L);
        } else {
            setFloatingButtonVisible(isPassword, z);
        }
        setCustomKeyboardVisible(isCustomKeyboardVisible(), z);
        showKeyboard();
    }

    public /* synthetic */ void lambda$updateFields$17(boolean z, boolean z2) {
        setFloatingButtonVisible(z, z2);
        AndroidUtilities.cancelRunOnUIThread(this.onShowKeyboardCallback);
    }

    public boolean isCustomKeyboardVisible() {
        if (isPinCode() && this.type != 0 && !AndroidUtilities.isTablet()) {
            Point point = AndroidUtilities.displaySize;
            if (point.x < point.y && !AndroidUtilities.isAccessibilityTouchExplorationEnabled()) {
                return true;
            }
        }
        return false;
    }

    public void processNext() {
        if ((this.currentPasswordType == 1 && this.passwordEditText.getText().length() == 0) || (this.currentPasswordType == 0 && this.codeFieldContainer.getCode().length() != 4)) {
            onPasscodeError();
            return;
        }
        ActionBarMenuItem actionBarMenuItem = this.otherItem;
        if (actionBarMenuItem != null) {
            actionBarMenuItem.setVisibility(8);
        }
        this.titleTextView.setText(LocaleController.getString("ConfirmCreatePasscode", 2131625236));
        this.descriptionTextSwitcher.setText(AndroidUtilities.replaceTags(LocaleController.getString("PasscodeReinstallNotice", 2131627219)));
        this.firstPassword = isPinCode() ? this.codeFieldContainer.getCode() : this.passwordEditText.getText().toString();
        this.passwordEditText.setText("");
        this.passwordEditText.setInputType(524417);
        for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
            codeNumberField.setText("");
        }
        showKeyboard();
        this.passcodeSetStep = 1;
    }

    public boolean isPinCode() {
        int i = this.type;
        if (i == 1 && this.currentPasswordType == 0) {
            return true;
        }
        return i == 2 && SharedConfig.passcodeType == 0;
    }

    private boolean isPassword() {
        int i = this.type;
        if (i == 1 && this.currentPasswordType == 1) {
            return true;
        }
        return i == 2 && SharedConfig.passcodeType == 1;
    }

    public void processDone() {
        if (isPassword() && this.passwordEditText.getText().length() == 0) {
            onPasscodeError();
            return;
        }
        String code = isPinCode() ? this.codeFieldContainer.getCode() : this.passwordEditText.getText().toString();
        int i = this.type;
        int i2 = 0;
        if (i == 1) {
            if (!this.firstPassword.equals(code)) {
                AndroidUtilities.updateViewVisibilityAnimated(this.passcodesDoNotMatchTextView, true);
                for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                    codeNumberField.setText("");
                }
                if (isPinCode()) {
                    this.codeFieldContainer.codeField[0].requestFocus();
                }
                this.passwordEditText.setText("");
                onPasscodeError();
                this.codeFieldContainer.removeCallbacks(this.hidePasscodesDoNotMatch);
                this.codeFieldContainer.post(new PasscodeActivity$$ExternalSyntheticLambda12(this));
                return;
            }
            boolean z = SharedConfig.passcodeHash.length() == 0;
            try {
                SharedConfig.passcodeSalt = new byte[16];
                Utilities.random.nextBytes(SharedConfig.passcodeSalt);
                byte[] bytes = this.firstPassword.getBytes("UTF-8");
                int length = bytes.length + 32;
                byte[] bArr = new byte[length];
                System.arraycopy(SharedConfig.passcodeSalt, 0, bArr, 0, 16);
                System.arraycopy(bytes, 0, bArr, 16, bytes.length);
                System.arraycopy(SharedConfig.passcodeSalt, 0, bArr, bytes.length + 16, 16);
                SharedConfig.passcodeHash = Utilities.bytesToHex(Utilities.computeSHA256(bArr, 0, length));
            } catch (Exception e) {
                FileLog.e(e);
            }
            SharedConfig.allowScreenCapture = true;
            SharedConfig.passcodeType = this.currentPasswordType;
            SharedConfig.saveConfig();
            this.passwordEditText.clearFocus();
            AndroidUtilities.hideKeyboard(this.passwordEditText);
            CodeNumberField[] codeNumberFieldArr = this.codeFieldContainer.codeField;
            int length2 = codeNumberFieldArr.length;
            while (i2 < length2) {
                CodeNumberField codeNumberField2 = codeNumberFieldArr[i2];
                codeNumberField2.clearFocus();
                AndroidUtilities.hideKeyboard(codeNumberField2);
                i2++;
            }
            this.keyboardView.setEditText(null);
            animateSuccessAnimation(new PasscodeActivity$$ExternalSyntheticLambda19(this, z));
        } else if (i == 2) {
            long j = SharedConfig.passcodeRetryInMs;
            if (j > 0) {
                double d = j;
                Double.isNaN(d);
                Toast.makeText(getParentActivity(), LocaleController.formatString("TooManyTries", 2131628743, LocaleController.formatPluralString("Seconds", Math.max(1, (int) Math.ceil(d / 1000.0d)), new Object[0])), 0).show();
                for (CodeNumberField codeNumberField3 : this.codeFieldContainer.codeField) {
                    codeNumberField3.setText("");
                }
                this.passwordEditText.setText("");
                if (isPinCode()) {
                    this.codeFieldContainer.codeField[0].requestFocus();
                }
                onPasscodeError();
            } else if (!SharedConfig.checkPasscode(code)) {
                SharedConfig.increaseBadPasscodeTries();
                this.passwordEditText.setText("");
                for (CodeNumberField codeNumberField4 : this.codeFieldContainer.codeField) {
                    codeNumberField4.setText("");
                }
                if (isPinCode()) {
                    this.codeFieldContainer.codeField[0].requestFocus();
                }
                onPasscodeError();
            } else {
                SharedConfig.badPasscodeTries = 0;
                SharedConfig.saveConfig();
                this.passwordEditText.clearFocus();
                AndroidUtilities.hideKeyboard(this.passwordEditText);
                CodeNumberField[] codeNumberFieldArr2 = this.codeFieldContainer.codeField;
                int length3 = codeNumberFieldArr2.length;
                while (i2 < length3) {
                    CodeNumberField codeNumberField5 = codeNumberFieldArr2[i2];
                    codeNumberField5.clearFocus();
                    AndroidUtilities.hideKeyboard(codeNumberField5);
                    i2++;
                }
                this.keyboardView.setEditText(null);
                animateSuccessAnimation(new PasscodeActivity$$ExternalSyntheticLambda17(this));
            }
        }
    }

    public /* synthetic */ void lambda$processDone$18() {
        this.codeFieldContainer.postDelayed(this.hidePasscodesDoNotMatch, 3000L);
        this.postedHidePasscodesDoNotMatch = true;
    }

    public /* synthetic */ void lambda$processDone$19(boolean z) {
        getMediaDataController().buildShortcuts();
        if (z) {
            presentFragment(new PasscodeActivity(0), true);
        } else {
            finishFragment();
        }
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didSetPasscode, new Object[0]);
    }

    public /* synthetic */ void lambda$processDone$20() {
        presentFragment(new PasscodeActivity(0), true);
    }

    private void onPasscodeError() {
        if (getParentActivity() == null) {
            return;
        }
        try {
            this.fragmentView.performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        if (isPinCode()) {
            for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                codeNumberField.animateErrorProgress(1.0f);
            }
        } else {
            this.outlinePasswordView.animateError(1.0f);
        }
        AndroidUtilities.shakeViewSpring(isPinCode() ? this.codeFieldContainer : this.outlinePasswordView, isPinCode() ? 10.0f : 4.0f, new PasscodeActivity$$ExternalSyntheticLambda16(this));
    }

    public /* synthetic */ void lambda$onPasscodeError$22() {
        AndroidUtilities.runOnUIThread(new PasscodeActivity$$ExternalSyntheticLambda13(this), isPinCode() ? 150L : 1000L);
    }

    public /* synthetic */ void lambda$onPasscodeError$21() {
        if (isPinCode()) {
            for (CodeNumberField codeNumberField : this.codeFieldContainer.codeField) {
                codeNumberField.animateErrorProgress(0.0f);
            }
            return;
        }
        this.outlinePasswordView.animateError(0.0f);
    }

    /* loaded from: classes3.dex */
    public class ListAdapter extends RecyclerListView.SelectionAdapter {
        private Context mContext;

        public ListAdapter(Context context) {
            PasscodeActivity.this = r1;
            this.mContext = context;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == PasscodeActivity.this.fingerprintRow || adapterPosition == PasscodeActivity.this.autoLockRow || adapterPosition == PasscodeActivity.this.captureRow || adapterPosition == PasscodeActivity.this.changePasscodeRow || adapterPosition == PasscodeActivity.this.disablePasscodeRow;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return PasscodeActivity.this.rowCount;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            View view2;
            if (i == 0) {
                view2 = new TextCheckCell(this.mContext);
                view2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else if (i == 1) {
                view2 = new TextSettingsCell(this.mContext);
                view2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else if (i == 3) {
                view2 = new HeaderCell(this.mContext);
                view2.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            } else {
                if (i == 4) {
                    view = new RLottieImageHolderView(this.mContext, null);
                } else {
                    view = new TextInfoPrivacyCell(this.mContext);
                }
                return new RecyclerListView.Holder(view);
            }
            view = view2;
            return new RecyclerListView.Holder(view);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            String str;
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType == 0) {
                TextCheckCell textCheckCell = (TextCheckCell) viewHolder.itemView;
                if (i != PasscodeActivity.this.fingerprintRow) {
                    if (i != PasscodeActivity.this.captureRow) {
                        return;
                    }
                    textCheckCell.setTextAndCheck(LocaleController.getString(2131628152), SharedConfig.allowScreenCapture, false);
                    return;
                }
                textCheckCell.setTextAndCheck(LocaleController.getString("UnlockFingerprint", 2131628801), SharedConfig.useFingerprint, true);
            } else if (itemViewType == 1) {
                TextSettingsCell textSettingsCell = (TextSettingsCell) viewHolder.itemView;
                if (i != PasscodeActivity.this.changePasscodeRow) {
                    if (i != PasscodeActivity.this.autoLockRow) {
                        if (i != PasscodeActivity.this.disablePasscodeRow) {
                            return;
                        }
                        textSettingsCell.setText(LocaleController.getString(2131625496), false);
                        textSettingsCell.setTag("dialogTextRed");
                        textSettingsCell.setTextColor(Theme.getColor("dialogTextRed"));
                        return;
                    }
                    int i2 = SharedConfig.autoLockIn;
                    if (i2 == 0) {
                        str = LocaleController.formatString("AutoLockDisabled", 2131624612, new Object[0]);
                    } else {
                        str = i2 < 3600 ? LocaleController.formatString("AutoLockInTime", 2131624613, LocaleController.formatPluralString("Minutes", i2 / 60, new Object[0])) : i2 < 86400 ? LocaleController.formatString("AutoLockInTime", 2131624613, LocaleController.formatPluralString("Hours", (int) Math.ceil((i2 / 60.0f) / 60.0f), new Object[0])) : LocaleController.formatString("AutoLockInTime", 2131624613, LocaleController.formatPluralString("Days", (int) Math.ceil(((i2 / 60.0f) / 60.0f) / 24.0f), new Object[0]));
                    }
                    textSettingsCell.setTextAndValue(LocaleController.getString("AutoLock", 2131624611), str, true);
                    textSettingsCell.setTag("windowBackgroundWhiteBlackText");
                    textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
                    return;
                }
                textSettingsCell.setText(LocaleController.getString("ChangePasscode", 2131624865), true);
                if (SharedConfig.passcodeHash.length() == 0) {
                    textSettingsCell.setTag("windowBackgroundWhiteGrayText7");
                    textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteGrayText7"));
                    return;
                }
                textSettingsCell.setTag("windowBackgroundWhiteBlackText");
                textSettingsCell.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
            } else {
                int i3 = 3;
                if (itemViewType != 2) {
                    if (itemViewType != 3) {
                        if (itemViewType != 4) {
                            return;
                        }
                        RLottieImageHolderView rLottieImageHolderView = (RLottieImageHolderView) viewHolder.itemView;
                        rLottieImageHolderView.imageView.setAnimation(2131558593, 100, 100);
                        rLottieImageHolderView.imageView.playAnimation();
                        return;
                    }
                    HeaderCell headerCell = (HeaderCell) viewHolder.itemView;
                    headerCell.setHeight(46);
                    if (i != PasscodeActivity.this.captureHeaderRow) {
                        return;
                    }
                    headerCell.setText(LocaleController.getString(2131628150));
                    return;
                }
                TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) viewHolder.itemView;
                if (i != PasscodeActivity.this.hintRow) {
                    if (i != PasscodeActivity.this.autoLockDetailRow) {
                        if (i != PasscodeActivity.this.captureDetailRow) {
                            return;
                        }
                        textInfoPrivacyCell.setText(LocaleController.getString(2131628151));
                        textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(this.mContext, 2131165436, "windowBackgroundGrayShadow"));
                        TextView textView = textInfoPrivacyCell.getTextView();
                        if (LocaleController.isRTL) {
                            i3 = 5;
                        }
                        textView.setGravity(i3);
                        return;
                    }
                    textInfoPrivacyCell.setText(LocaleController.getString(2131624614));
                    textInfoPrivacyCell.setBackground(Theme.getThemedDrawable(this.mContext, 2131165435, "windowBackgroundGrayShadow"));
                    TextView textView2 = textInfoPrivacyCell.getTextView();
                    if (LocaleController.isRTL) {
                        i3 = 5;
                    }
                    textView2.setGravity(i3);
                    return;
                }
                textInfoPrivacyCell.setText(LocaleController.getString(2131627220));
                textInfoPrivacyCell.setBackground(null);
                textInfoPrivacyCell.getTextView().setGravity(1);
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i == PasscodeActivity.this.fingerprintRow || i == PasscodeActivity.this.captureRow) {
                return 0;
            }
            if (i == PasscodeActivity.this.changePasscodeRow || i == PasscodeActivity.this.autoLockRow || i == PasscodeActivity.this.disablePasscodeRow) {
                return 1;
            }
            if (i == PasscodeActivity.this.autoLockDetailRow || i == PasscodeActivity.this.captureDetailRow || i == PasscodeActivity.this.hintRow) {
                return 2;
            }
            if (i == PasscodeActivity.this.captureHeaderRow) {
                return 3;
            }
            return i == PasscodeActivity.this.utyanRow ? 4 : 0;
        }
    }

    @Override // org.telegram.ui.ActionBar.BaseFragment
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> arrayList = new ArrayList<>();
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextCheckCell.class, TextSettingsCell.class}, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhite"));
        arrayList.add(new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_CHECKTAG | ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, "actionBarDefaultSubmenuBackground"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItem"));
        arrayList.add(new ThemeDescription(this.actionBar, ThemeDescription.FLAG_IMAGECOLOR | ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, "actionBarDefaultSubmenuItemIcon"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, "divider"));
        arrayList.add(new ThemeDescription(this.titleTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteGrayText6"));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputField"));
        arrayList.add(new ThemeDescription(this.passwordEditText, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE | ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, "windowBackgroundWhiteInputFieldActivated"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrack"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "switchTrackChecked"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteBlackText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText7"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteValueText"));
        arrayList.add(new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, "windowBackgroundGrayShadow"));
        arrayList.add(new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[]) null, (Drawable[]) null, (ThemeDescription.ThemeDescriptionDelegate) null, "windowBackgroundWhiteGrayText4"));
        return arrayList;
    }

    /* loaded from: classes3.dex */
    public static final class RLottieImageHolderView extends FrameLayout {
        private RLottieImageView imageView;

        /* synthetic */ RLottieImageHolderView(Context context, AnonymousClass1 anonymousClass1) {
            this(context);
        }

        private RLottieImageHolderView(Context context) {
            super(context);
            RLottieImageView rLottieImageView = new RLottieImageView(context);
            this.imageView = rLottieImageView;
            rLottieImageView.setOnClickListener(new PasscodeActivity$RLottieImageHolderView$$ExternalSyntheticLambda0(this));
            int dp = AndroidUtilities.dp(120.0f);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dp, dp);
            layoutParams.gravity = 1;
            addView(this.imageView, layoutParams);
            setPadding(0, AndroidUtilities.dp(32.0f), 0, 0);
            setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
        }

        public /* synthetic */ void lambda$new$0(View view) {
            if (!this.imageView.getAnimatedDrawable().isRunning()) {
                this.imageView.getAnimatedDrawable().setCurrentFrame(0, false);
                this.imageView.playAnimation();
            }
        }
    }
}
