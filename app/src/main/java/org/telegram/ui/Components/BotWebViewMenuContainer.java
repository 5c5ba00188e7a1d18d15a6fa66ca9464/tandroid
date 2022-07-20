package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.util.ObjectsCompat$$ExternalSyntheticBackport0;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.ChatListItemAnimator;
import java.util.Locale;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_dataJSON;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_prolongWebView;
import org.telegram.tgnet.TLRPC$TL_messages_requestWebView;
import org.telegram.tgnet.TLRPC$TL_payments_paymentForm;
import org.telegram.tgnet.TLRPC$TL_payments_paymentReceipt;
import org.telegram.tgnet.TLRPC$TL_webViewResultUrl;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.BotWebViewContainer;
import org.telegram.ui.Components.ChatAttachAlertBotWebViewLayout;
import org.telegram.ui.PaymentFormActivity;
/* loaded from: classes3.dex */
public class BotWebViewMenuContainer extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
    private static final SimpleFloatPropertyCompat<BotWebViewMenuContainer> ACTION_BAR_TRANSITION_PROGRESS_VALUE = new SimpleFloatPropertyCompat("actionBarTransitionProgress", BotWebViewMenuContainer$$ExternalSyntheticLambda20.INSTANCE, BotWebViewMenuContainer$$ExternalSyntheticLambda21.INSTANCE).setMultiplier(100.0f);
    private ActionBar.ActionBarMenuOnItemClick actionBarOnItemClick;
    private float actionBarTransitionProgress;
    private long botId;
    private ActionBarMenuItem botMenuItem;
    private String botUrl;
    private SpringAnimation botWebViewButtonAnimator;
    private boolean botWebViewButtonWasVisible;
    private int currentAccount;
    private boolean dismissed;
    private Runnable globalOnDismissListener;
    private boolean ignoreLayout;
    private boolean ignoreMeasure;
    private boolean isLoaded;
    private int overrideActionBarBackground;
    private float overrideActionBarBackgroundProgress;
    private boolean overrideBackgroundColor;
    private ChatActivityEnterView parentEnterView;
    private ChatAttachAlertBotWebViewLayout.WebProgressView progressView;
    private long queryId;
    private MessageObject savedEditMessageObject;
    private Editable savedEditText;
    private MessageObject savedReplyMessageObject;
    private ActionBarMenuSubItem settingsItem;
    private SpringAnimation springAnimation;
    private ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer swipeContainer;
    private Boolean wasLightStatusBar;
    private BotWebViewContainer webViewContainer;
    private BotWebViewContainer.Delegate webViewDelegate;
    private ValueAnimator webViewScrollAnimator;
    private Paint dimPaint = new Paint();
    private Paint backgroundPaint = new Paint(1);
    private Paint actionBarPaint = new Paint(1);
    private Paint linePaint = new Paint();
    private Runnable pollRunnable = new BotWebViewMenuContainer$$ExternalSyntheticLambda9(this);

    public static /* synthetic */ void lambda$static$1(BotWebViewMenuContainer botWebViewMenuContainer, float f) {
        botWebViewMenuContainer.actionBarTransitionProgress = f;
        botWebViewMenuContainer.invalidate();
        botWebViewMenuContainer.invalidateActionBar();
    }

    public /* synthetic */ void lambda$new$4() {
        if (!this.dismissed) {
            TLRPC$TL_messages_prolongWebView tLRPC$TL_messages_prolongWebView = new TLRPC$TL_messages_prolongWebView();
            tLRPC$TL_messages_prolongWebView.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botId);
            tLRPC$TL_messages_prolongWebView.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.botId);
            tLRPC$TL_messages_prolongWebView.query_id = this.queryId;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_prolongWebView, new BotWebViewMenuContainer$$ExternalSyntheticLambda17(this));
        }
    }

    public /* synthetic */ void lambda$new$3(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new BotWebViewMenuContainer$$ExternalSyntheticLambda14(this, tLRPC$TL_error));
    }

    public /* synthetic */ void lambda$new$2(TLRPC$TL_error tLRPC$TL_error) {
        if (this.dismissed) {
            return;
        }
        if (tLRPC$TL_error != null) {
            dismiss();
        } else {
            AndroidUtilities.runOnUIThread(this.pollRunnable, 60000L);
        }
    }

    public BotWebViewMenuContainer(Context context, ChatActivityEnterView chatActivityEnterView) {
        super(context);
        this.parentEnterView = chatActivityEnterView;
        ActionBar actionBar = chatActivityEnterView.getParentFragment().getActionBar();
        ActionBarMenuItem addItem = actionBar.createMenu().addItem(1000, 2131165453);
        this.botMenuItem = addItem;
        addItem.setVisibility(8);
        this.botMenuItem.addSubItem(2131230863, 2131165907, LocaleController.getString(2131624744));
        this.actionBarOnItemClick = actionBar.getActionBarMenuOnItemClick();
        BotWebViewContainer botWebViewContainer = new BotWebViewContainer(context, chatActivityEnterView.getParentFragment().getResourceProvider(), getColor("windowBackgroundWhite"));
        this.webViewContainer = botWebViewContainer;
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(chatActivityEnterView, actionBar);
        this.webViewDelegate = anonymousClass1;
        botWebViewContainer.setDelegate(anonymousClass1);
        this.linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.linePaint.setStrokeWidth(AndroidUtilities.dp(4.0f));
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);
        this.dimPaint.setColor(1073741824);
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(context);
        this.swipeContainer = anonymousClass2;
        anonymousClass2.setScrollListener(new BotWebViewMenuContainer$$ExternalSyntheticLambda15(this, actionBar));
        this.swipeContainer.setScrollEndListener(new BotWebViewMenuContainer$$ExternalSyntheticLambda11(this));
        this.swipeContainer.addView(this.webViewContainer);
        this.swipeContainer.setDelegate(new BotWebViewMenuContainer$$ExternalSyntheticLambda19(this));
        this.swipeContainer.setTopActionBarOffsetY((ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(24.0f));
        this.swipeContainer.setSwipeOffsetAnimationDisallowed(true);
        this.swipeContainer.setIsKeyboardVisible(new BotWebViewMenuContainer$$ExternalSyntheticLambda16(chatActivityEnterView));
        addView(this.swipeContainer, LayoutHelper.createFrame(-1, -1.0f, 48, 0.0f, 24.0f, 0.0f, 0.0f));
        ChatAttachAlertBotWebViewLayout.WebProgressView webProgressView = new ChatAttachAlertBotWebViewLayout.WebProgressView(context, chatActivityEnterView.getParentFragment().getResourceProvider());
        this.progressView = webProgressView;
        addView(webProgressView, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 5.0f));
        this.webViewContainer.setWebViewProgressListener(new BotWebViewMenuContainer$$ExternalSyntheticLambda3(this));
        setWillNotDraw(false);
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewMenuContainer$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements BotWebViewContainer.Delegate {
        final /* synthetic */ ActionBar val$actionBar;
        final /* synthetic */ ChatActivityEnterView val$parentEnterView;

        @Override // org.telegram.ui.Components.BotWebViewContainer.Delegate
        public /* synthetic */ void onSendWebViewData(String str) {
            BotWebViewContainer.Delegate.CC.$default$onSendWebViewData(this, str);
        }

        @Override // org.telegram.ui.Components.BotWebViewContainer.Delegate
        public /* synthetic */ void onWebAppReady() {
            BotWebViewContainer.Delegate.CC.$default$onWebAppReady(this);
        }

        AnonymousClass1(ChatActivityEnterView chatActivityEnterView, ActionBar actionBar) {
            BotWebViewMenuContainer.this = r1;
            this.val$parentEnterView = chatActivityEnterView;
            this.val$actionBar = actionBar;
        }

        @Override // org.telegram.ui.Components.BotWebViewContainer.Delegate
        public void onCloseRequested(Runnable runnable) {
            BotWebViewMenuContainer.this.dismiss(runnable);
        }

        @Override // org.telegram.ui.Components.BotWebViewContainer.Delegate
        public void onWebAppSetActionBarColor(String str) {
            int i = BotWebViewMenuContainer.this.overrideActionBarBackground;
            int color = BotWebViewMenuContainer.this.getColor(str);
            if (i == 0) {
                BotWebViewMenuContainer.this.overrideActionBarBackground = color;
            }
            ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.addUpdateListener(new BotWebViewMenuContainer$1$$ExternalSyntheticLambda0(this, i, color));
            duration.start();
        }

        public /* synthetic */ void lambda$onWebAppSetActionBarColor$0(int i, int i2, ValueAnimator valueAnimator) {
            if (i != 0) {
                BotWebViewMenuContainer.this.overrideActionBarBackground = ColorUtils.blendARGB(i, i2, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            } else {
                BotWebViewMenuContainer.this.overrideActionBarBackgroundProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            }
            BotWebViewMenuContainer.this.actionBarPaint.setColor(BotWebViewMenuContainer.this.overrideActionBarBackground);
            BotWebViewMenuContainer.this.invalidateActionBar();
        }

        @Override // org.telegram.ui.Components.BotWebViewContainer.Delegate
        public void onWebAppSetBackgroundColor(int i) {
            BotWebViewMenuContainer.this.overrideBackgroundColor = true;
            int color = BotWebViewMenuContainer.this.backgroundPaint.getColor();
            ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.addUpdateListener(new BotWebViewMenuContainer$1$$ExternalSyntheticLambda1(this, color, i));
            duration.start();
        }

        public /* synthetic */ void lambda$onWebAppSetBackgroundColor$1(int i, int i2, ValueAnimator valueAnimator) {
            BotWebViewMenuContainer.this.backgroundPaint.setColor(ColorUtils.blendARGB(i, i2, ((Float) valueAnimator.getAnimatedValue()).floatValue()));
            BotWebViewMenuContainer.this.invalidate();
        }

        @Override // org.telegram.ui.Components.BotWebViewContainer.Delegate
        public void onWebAppExpand() {
            if (BotWebViewMenuContainer.this.swipeContainer.isSwipeInProgress()) {
                return;
            }
            BotWebViewMenuContainer.this.swipeContainer.stickTo((-BotWebViewMenuContainer.this.swipeContainer.getOffsetY()) + BotWebViewMenuContainer.this.swipeContainer.getTopActionBarOffsetY());
        }

        @Override // org.telegram.ui.Components.BotWebViewContainer.Delegate
        public void onWebAppOpenInvoice(String str, TLObject tLObject) {
            PaymentFormActivity paymentFormActivity;
            ChatActivity parentFragment = this.val$parentEnterView.getParentFragment();
            if (tLObject instanceof TLRPC$TL_payments_paymentForm) {
                TLRPC$TL_payments_paymentForm tLRPC$TL_payments_paymentForm = (TLRPC$TL_payments_paymentForm) tLObject;
                MessagesController.getInstance(BotWebViewMenuContainer.this.currentAccount).putUsers(tLRPC$TL_payments_paymentForm.users, false);
                paymentFormActivity = new PaymentFormActivity(tLRPC$TL_payments_paymentForm, str, parentFragment);
            } else {
                paymentFormActivity = tLObject instanceof TLRPC$TL_payments_paymentReceipt ? new PaymentFormActivity((TLRPC$TL_payments_paymentReceipt) tLObject) : null;
            }
            if (paymentFormActivity != null) {
                paymentFormActivity.setPaymentFormCallback(new BotWebViewMenuContainer$1$$ExternalSyntheticLambda3(this, str));
                parentFragment.presentFragment(paymentFormActivity);
            }
        }

        public /* synthetic */ void lambda$onWebAppOpenInvoice$2(String str, PaymentFormActivity.InvoiceStatus invoiceStatus) {
            BotWebViewMenuContainer.this.webViewContainer.onInvoiceStatusUpdate(str, invoiceStatus.name().toLowerCase(Locale.ROOT));
        }

        @Override // org.telegram.ui.Components.BotWebViewContainer.Delegate
        public void onSetupMainButton(boolean z, boolean z2, String str, int i, int i2, boolean z3) {
            ChatActivityBotWebViewButton botWebViewButton = this.val$parentEnterView.getBotWebViewButton();
            botWebViewButton.setupButtonParams(z2, str, i, i2, z3);
            botWebViewButton.setOnClickListener(new BotWebViewMenuContainer$1$$ExternalSyntheticLambda2(this));
            if (z != BotWebViewMenuContainer.this.botWebViewButtonWasVisible) {
                BotWebViewMenuContainer.this.animateBotButton(z);
            }
        }

        public /* synthetic */ void lambda$onSetupMainButton$3(View view) {
            BotWebViewMenuContainer.this.webViewContainer.onMainButtonPressed();
        }

        @Override // org.telegram.ui.Components.BotWebViewContainer.Delegate
        public void onSetBackButtonVisible(boolean z) {
            if (BotWebViewMenuContainer.this.actionBarTransitionProgress == 1.0f) {
                if (z) {
                    AndroidUtilities.updateImageViewImageAnimated(this.val$actionBar.getBackButton(), this.val$actionBar.getBackButtonDrawable());
                } else {
                    AndroidUtilities.updateImageViewImageAnimated(this.val$actionBar.getBackButton(), 2131165473);
                }
            }
        }
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewMenuContainer$2 */
    /* loaded from: classes3.dex */
    public class AnonymousClass2 extends ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Context context) {
            super(context);
            BotWebViewMenuContainer.this = r1;
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x001f  */
        /* JADX WARN: Removed duplicated region for block: B:13:0x0029  */
        @Override // android.widget.FrameLayout, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onMeasure(int i, int i2) {
            int i3;
            float f;
            int size = View.MeasureSpec.getSize(i2);
            if (!AndroidUtilities.isTablet()) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    i3 = (int) (size / 3.5f);
                    if (i3 < 0) {
                        i3 = 0;
                    }
                    f = i3;
                    if (getOffsetY() != f) {
                        BotWebViewMenuContainer.this.ignoreLayout = true;
                        setOffsetY(f);
                        BotWebViewMenuContainer.this.ignoreLayout = false;
                    }
                    super.onMeasure(i, View.MeasureSpec.makeMeasureSpec((((View.MeasureSpec.getSize(i2) - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) + AndroidUtilities.dp(24.0f)) - AndroidUtilities.dp(5.0f), 1073741824));
                }
            }
            i3 = (size / 5) * 2;
            if (i3 < 0) {
            }
            f = i3;
            if (getOffsetY() != f) {
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec((((View.MeasureSpec.getSize(i2) - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) + AndroidUtilities.dp(24.0f)) - AndroidUtilities.dp(5.0f), 1073741824));
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (BotWebViewMenuContainer.this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }
    }

    public /* synthetic */ void lambda$new$5(ActionBar actionBar) {
        float f = 0.0f;
        if (this.swipeContainer.getSwipeOffsetY() > 0.0f) {
            this.dimPaint.setAlpha((int) ((1.0f - (Math.min(this.swipeContainer.getSwipeOffsetY(), this.swipeContainer.getHeight()) / this.swipeContainer.getHeight())) * 64.0f));
        } else {
            this.dimPaint.setAlpha(64);
        }
        invalidate();
        this.webViewContainer.invalidateViewPortHeight();
        if (this.springAnimation != null) {
            float min = 1.0f - (Math.min(this.swipeContainer.getTopActionBarOffsetY(), this.swipeContainer.getTranslationY() - this.swipeContainer.getTopActionBarOffsetY()) / this.swipeContainer.getTopActionBarOffsetY());
            if (getVisibility() == 0) {
                f = min;
            }
            float f2 = (f > 0.5f ? 1 : 0) * 100.0f;
            if (this.springAnimation.getSpring().getFinalPosition() != f2) {
                this.springAnimation.getSpring().setFinalPosition(f2);
                this.springAnimation.start();
                if (!this.webViewContainer.isBackButtonVisible()) {
                    if (f2 == 100.0f) {
                        AndroidUtilities.updateImageViewImageAnimated(actionBar.getBackButton(), 2131165473);
                    } else {
                        AndroidUtilities.updateImageViewImageAnimated(actionBar.getBackButton(), actionBar.getBackButtonDrawable());
                    }
                }
            }
        }
        System.currentTimeMillis();
    }

    public /* synthetic */ void lambda$new$6() {
        this.webViewContainer.invalidateViewPortHeight(true);
    }

    public static /* synthetic */ Boolean lambda$new$7(ChatActivityEnterView chatActivityEnterView, Void r1) {
        return Boolean.valueOf(chatActivityEnterView.getSizeNotifierLayout().getKeyboardHeight() >= AndroidUtilities.dp(20.0f));
    }

    public /* synthetic */ void lambda$new$9(Float f) {
        this.progressView.setLoadProgressAnimated(f.floatValue());
        if (f.floatValue() == 1.0f) {
            ValueAnimator duration = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.addUpdateListener(new BotWebViewMenuContainer$$ExternalSyntheticLambda1(this));
            duration.addListener(new AnonymousClass3());
            duration.start();
        }
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewMenuContainer$3 */
    /* loaded from: classes3.dex */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        AnonymousClass3() {
            BotWebViewMenuContainer.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BotWebViewMenuContainer.this.progressView.setVisibility(8);
        }
    }

    public /* synthetic */ void lambda$new$8(ValueAnimator valueAnimator) {
        this.progressView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public void invalidateActionBar() {
        ChatActivity parentFragment = this.parentEnterView.getParentFragment();
        if (parentFragment == null || getVisibility() != 0) {
            return;
        }
        ChatAvatarContainer avatarContainer = parentFragment.getAvatarContainer();
        int blendARGB = ColorUtils.blendARGB(getColor(avatarContainer.getLastSubtitleColorKey() == null ? "actionBarDefaultSubtitle" : avatarContainer.getLastSubtitleColorKey()), getColor("windowBackgroundWhiteGrayText"), this.actionBarTransitionProgress);
        ActionBar actionBar = parentFragment.getActionBar();
        actionBar.setBackgroundColor(ColorUtils.blendARGB(getColor("actionBarDefault"), getColor("windowBackgroundWhite"), this.actionBarTransitionProgress));
        actionBar.setItemsColor(ColorUtils.blendARGB(getColor("actionBarDefaultIcon"), getColor("windowBackgroundWhiteBlackText"), this.actionBarTransitionProgress), false);
        actionBar.setItemsBackgroundColor(ColorUtils.blendARGB(getColor("actionBarDefaultSelector"), getColor("actionBarWhiteSelector"), this.actionBarTransitionProgress), false);
        actionBar.setSubtitleColor(blendARGB);
        ChatAvatarContainer avatarContainer2 = parentFragment.getAvatarContainer();
        avatarContainer2.getTitleTextView().setTextColor(ColorUtils.blendARGB(getColor("actionBarDefaultTitle"), getColor("windowBackgroundWhiteBlackText"), this.actionBarTransitionProgress));
        avatarContainer2.getSubtitleTextView().setTextColor(blendARGB);
        avatarContainer2.setOverrideSubtitleColor(this.actionBarTransitionProgress == 0.0f ? null : Integer.valueOf(blendARGB));
        updateLightStatusBar();
    }

    public boolean onBackPressed() {
        return this.webViewContainer.onBackPressed();
    }

    public void animateBotButton(boolean z) {
        ChatActivityBotWebViewButton botWebViewButton = this.parentEnterView.getBotWebViewButton();
        SpringAnimation springAnimation = this.botWebViewButtonAnimator;
        if (springAnimation != null) {
            springAnimation.cancel();
            this.botWebViewButtonAnimator = null;
        }
        float f = 0.0f;
        botWebViewButton.setProgress(z ? 0.0f : 1.0f);
        if (z) {
            botWebViewButton.setVisibility(0);
        }
        SimpleFloatPropertyCompat<ChatActivityBotWebViewButton> simpleFloatPropertyCompat = ChatActivityBotWebViewButton.PROGRESS_PROPERTY;
        SpringAnimation springAnimation2 = new SpringAnimation(botWebViewButton, simpleFloatPropertyCompat);
        if (z) {
            f = 1.0f;
        }
        SpringAnimation addEndListener = springAnimation2.setSpring(new SpringForce(f * simpleFloatPropertyCompat.getMultiplier()).setStiffness(z ? 600.0f : 750.0f).setDampingRatio(1.0f)).addUpdateListener(new BotWebViewMenuContainer$$ExternalSyntheticLambda7(this)).addEndListener(new BotWebViewMenuContainer$$ExternalSyntheticLambda6(this, z, botWebViewButton));
        this.botWebViewButtonAnimator = addEndListener;
        addEndListener.start();
        this.botWebViewButtonWasVisible = z;
    }

    public /* synthetic */ void lambda$animateBotButton$10(DynamicAnimation dynamicAnimation, float f, float f2) {
        float multiplier = f / ChatActivityBotWebViewButton.PROGRESS_PROPERTY.getMultiplier();
        this.parentEnterView.setBotWebViewButtonOffsetX(AndroidUtilities.dp(64.0f) * multiplier);
        this.parentEnterView.setComposeShadowAlpha(1.0f - multiplier);
    }

    public /* synthetic */ void lambda$animateBotButton$11(boolean z, ChatActivityBotWebViewButton chatActivityBotWebViewButton, DynamicAnimation dynamicAnimation, boolean z2, float f, float f2) {
        if (!z) {
            chatActivityBotWebViewButton.setVisibility(8);
        }
        if (this.botWebViewButtonAnimator == dynamicAnimation) {
            this.botWebViewButtonAnimator = null;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.springAnimation == null) {
            this.springAnimation = new SpringAnimation(this, ACTION_BAR_TRANSITION_PROGRESS_VALUE).setSpring(new SpringForce().setStiffness(1200.0f).setDampingRatio(1.0f)).addEndListener(new BotWebViewMenuContainer$$ExternalSyntheticLambda5(this));
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.webViewResultSent);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
    }

    public /* synthetic */ void lambda$onAttachedToWindow$12(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        ChatActivity parentFragment = this.parentEnterView.getParentFragment();
        ChatAvatarContainer avatarContainer = parentFragment.getAvatarContainer();
        avatarContainer.setClickable(f == 0.0f);
        avatarContainer.getAvatarImageView().setClickable(f == 0.0f);
        ActionBar actionBar = parentFragment.getActionBar();
        if (f == 100.0f && this.parentEnterView.hasBotWebView()) {
            parentFragment.showHeaderItem(false);
            this.botMenuItem.setVisibility(0);
            actionBar.setActionBarMenuOnItemClick(new AnonymousClass4());
            return;
        }
        parentFragment.showHeaderItem(true);
        this.botMenuItem.setVisibility(8);
        actionBar.setActionBarMenuOnItemClick(this.actionBarOnItemClick);
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewMenuContainer$4 */
    /* loaded from: classes3.dex */
    public class AnonymousClass4 extends ActionBar.ActionBarMenuOnItemClick {
        AnonymousClass4() {
            BotWebViewMenuContainer.this = r1;
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (BotWebViewMenuContainer.this.webViewContainer.onBackPressed()) {
                    return;
                }
                BotWebViewMenuContainer.this.dismiss();
            } else if (i != 2131230863) {
                if (i != 2131230864) {
                    return;
                }
                BotWebViewMenuContainer.this.webViewContainer.onSettingsButtonPressed();
            } else {
                if (BotWebViewMenuContainer.this.webViewContainer.getWebView() != null) {
                    BotWebViewMenuContainer.this.webViewContainer.getWebView().animate().cancel();
                    BotWebViewMenuContainer.this.webViewContainer.getWebView().animate().alpha(0.0f).start();
                }
                BotWebViewMenuContainer.this.isLoaded = false;
                BotWebViewMenuContainer.this.progressView.setLoadProgress(0.0f);
                BotWebViewMenuContainer.this.progressView.setAlpha(1.0f);
                BotWebViewMenuContainer.this.progressView.setVisibility(0);
                BotWebViewMenuContainer.this.webViewContainer.setBotUser(MessagesController.getInstance(BotWebViewMenuContainer.this.currentAccount).getUser(Long.valueOf(BotWebViewMenuContainer.this.botId)));
                BotWebViewMenuContainer.this.webViewContainer.loadFlickerAndSettingsItem(BotWebViewMenuContainer.this.currentAccount, BotWebViewMenuContainer.this.botId, BotWebViewMenuContainer.this.settingsItem);
                BotWebViewMenuContainer.this.webViewContainer.reload();
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SpringAnimation springAnimation = this.springAnimation;
        if (springAnimation != null) {
            springAnimation.cancel();
            this.springAnimation = null;
        }
        this.actionBarTransitionProgress = 0.0f;
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.webViewResultSent);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.ignoreMeasure) {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        } else {
            super.onMeasure(i, i2);
        }
    }

    public void onPanTransitionStart(boolean z, int i) {
        boolean z2;
        if (!z) {
            return;
        }
        float topActionBarOffsetY = (-this.swipeContainer.getOffsetY()) + this.swipeContainer.getTopActionBarOffsetY();
        if (this.swipeContainer.getSwipeOffsetY() != topActionBarOffsetY) {
            this.swipeContainer.stickTo(topActionBarOffsetY);
            z2 = true;
        } else {
            z2 = false;
        }
        int measureKeyboardHeight = this.parentEnterView.getSizeNotifierLayout().measureKeyboardHeight() + i;
        setMeasuredDimension(getMeasuredWidth(), i);
        this.ignoreMeasure = true;
        if (z2) {
            return;
        }
        ValueAnimator valueAnimator = this.webViewScrollAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.webViewScrollAnimator = null;
        }
        if (this.webViewContainer.getWebView() == null) {
            return;
        }
        int scrollY = this.webViewContainer.getWebView().getScrollY();
        int i2 = (measureKeyboardHeight - i) + scrollY;
        ValueAnimator duration = ValueAnimator.ofInt(scrollY, i2).setDuration(250L);
        this.webViewScrollAnimator = duration;
        duration.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
        this.webViewScrollAnimator.addUpdateListener(new BotWebViewMenuContainer$$ExternalSyntheticLambda2(this));
        this.webViewScrollAnimator.addListener(new AnonymousClass5(i2));
        this.webViewScrollAnimator.start();
    }

    public /* synthetic */ void lambda$onPanTransitionStart$13(ValueAnimator valueAnimator) {
        int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        if (this.webViewContainer.getWebView() != null) {
            this.webViewContainer.getWebView().setScrollY(intValue);
        }
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewMenuContainer$5 */
    /* loaded from: classes3.dex */
    public class AnonymousClass5 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$toY;

        AnonymousClass5(int i) {
            BotWebViewMenuContainer.this = r1;
            this.val$toY = i;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (BotWebViewMenuContainer.this.webViewContainer.getWebView() != null) {
                BotWebViewMenuContainer.this.webViewContainer.getWebView().setScrollY(this.val$toY);
            }
            if (animator == BotWebViewMenuContainer.this.webViewScrollAnimator) {
                BotWebViewMenuContainer.this.webViewScrollAnimator = null;
            }
        }
    }

    public void onPanTransitionEnd() {
        this.ignoreMeasure = false;
        requestLayout();
    }

    private void updateLightStatusBar() {
        boolean z = true;
        if (ColorUtils.calculateLuminance(Theme.getColor("windowBackgroundWhite", null, true)) < 0.9d || this.actionBarTransitionProgress < 0.85f) {
            z = false;
        }
        Boolean bool = this.wasLightStatusBar;
        if (bool == null || bool.booleanValue() != z) {
            this.wasLightStatusBar = Boolean.valueOf(z);
            if (Build.VERSION.SDK_INT < 23) {
                return;
            }
            int systemUiVisibility = getSystemUiVisibility();
            setSystemUiVisibility(z ? systemUiVisibility | 8192 : systemUiVisibility & (-8193));
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.overrideBackgroundColor) {
            this.backgroundPaint.setColor(getColor("windowBackgroundWhite"));
        }
        if (this.overrideActionBarBackgroundProgress == 0.0f) {
            this.actionBarPaint.setColor(getColor("windowBackgroundWhite"));
        }
        RectF rectF = AndroidUtilities.rectTmp;
        rectF.set(0.0f, 0.0f, getWidth(), getHeight());
        canvas.drawRect(rectF, this.dimPaint);
        float dp = AndroidUtilities.dp(16.0f) * (1.0f - this.actionBarTransitionProgress);
        rectF.set(0.0f, AndroidUtilities.lerp(this.swipeContainer.getTranslationY(), 0.0f, this.actionBarTransitionProgress), getWidth(), this.swipeContainer.getTranslationY() + AndroidUtilities.dp(24.0f) + dp);
        canvas.drawRoundRect(rectF, dp, dp, this.actionBarPaint);
        rectF.set(0.0f, this.swipeContainer.getTranslationY() + AndroidUtilities.dp(24.0f), getWidth(), getHeight() + dp);
        canvas.drawRect(rectF, this.backgroundPaint);
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && motionEvent.getY() <= AndroidUtilities.lerp(this.swipeContainer.getTranslationY() + AndroidUtilities.dp(24.0f), 0.0f, this.actionBarTransitionProgress)) {
            dismiss();
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.linePaint.setColor(getColor("key_sheet_scrollUp"));
        Paint paint = this.linePaint;
        paint.setAlpha((int) (paint.getAlpha() * (1.0f - (Math.min(0.5f, this.actionBarTransitionProgress) / 0.5f))));
        canvas.save();
        float f = 1.0f - this.actionBarTransitionProgress;
        float lerp = AndroidUtilities.lerp(this.swipeContainer.getTranslationY(), AndroidUtilities.statusBarHeight + (ActionBar.getCurrentActionBarHeight() / 2.0f), this.actionBarTransitionProgress) + AndroidUtilities.dp(12.0f);
        canvas.scale(f, f, getWidth() / 2.0f, lerp);
        canvas.drawLine((getWidth() / 2.0f) - AndroidUtilities.dp(16.0f), lerp, (getWidth() / 2.0f) + AndroidUtilities.dp(16.0f), lerp, this.linePaint);
        canvas.restore();
    }

    public void show(int i, long j, String str) {
        this.dismissed = false;
        if (this.currentAccount != i || this.botId != j || !ObjectsCompat$$ExternalSyntheticBackport0.m(this.botUrl, str)) {
            this.isLoaded = false;
        }
        this.currentAccount = i;
        this.botId = j;
        this.botUrl = str;
        this.savedEditText = this.parentEnterView.getEditField().getText();
        this.parentEnterView.getEditField().setText((CharSequence) null);
        this.savedReplyMessageObject = this.parentEnterView.getReplyingMessageObject();
        this.savedEditMessageObject = this.parentEnterView.getEditingMessageObject();
        ChatActivity parentFragment = this.parentEnterView.getParentFragment();
        if (parentFragment != null) {
            parentFragment.hideFieldPanel(true);
        }
        if (!this.isLoaded) {
            loadWebView();
        }
        setVisibility(0);
        setAlpha(0.0f);
        addOnLayoutChangeListener(new AnonymousClass6());
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewMenuContainer$6 */
    /* loaded from: classes3.dex */
    public class AnonymousClass6 implements View.OnLayoutChangeListener {
        AnonymousClass6() {
            BotWebViewMenuContainer.this = r1;
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            view.removeOnLayoutChangeListener(this);
            BotWebViewMenuContainer.this.swipeContainer.setSwipeOffsetY(BotWebViewMenuContainer.this.swipeContainer.getHeight());
            BotWebViewMenuContainer.this.setAlpha(1.0f);
            new SpringAnimation(BotWebViewMenuContainer.this.swipeContainer, ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.SWIPE_OFFSET_Y, 0.0f).setSpring(new SpringForce(0.0f).setDampingRatio(0.75f).setStiffness(500.0f)).addEndListener(new BotWebViewMenuContainer$6$$ExternalSyntheticLambda0(this)).start();
        }

        public /* synthetic */ void lambda$onLayoutChange$0(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            BotWebViewMenuContainer.this.webViewContainer.restoreButtonData();
            BotWebViewMenuContainer.this.webViewContainer.invalidateViewPortHeight(true);
        }
    }

    private void loadWebView() {
        this.progressView.setLoadProgress(0.0f);
        this.progressView.setAlpha(1.0f);
        this.progressView.setVisibility(0);
        this.webViewContainer.setBotUser(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId)));
        this.webViewContainer.loadFlickerAndSettingsItem(this.currentAccount, this.botId, this.settingsItem);
        TLRPC$TL_messages_requestWebView tLRPC$TL_messages_requestWebView = new TLRPC$TL_messages_requestWebView();
        tLRPC$TL_messages_requestWebView.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botId);
        tLRPC$TL_messages_requestWebView.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.botId);
        tLRPC$TL_messages_requestWebView.url = this.botUrl;
        tLRPC$TL_messages_requestWebView.flags |= 2;
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("bg_color", getColor("windowBackgroundWhite"));
            jSONObject.put("secondary_bg_color", getColor("windowBackgroundGray"));
            jSONObject.put("text_color", getColor("windowBackgroundWhiteBlackText"));
            jSONObject.put("hint_color", getColor("windowBackgroundWhiteHintText"));
            jSONObject.put("link_color", getColor("windowBackgroundWhiteLinkText"));
            jSONObject.put("button_color", getColor("featuredStickers_addButton"));
            jSONObject.put("button_text_color", getColor("featuredStickers_buttonText"));
            TLRPC$TL_dataJSON tLRPC$TL_dataJSON = new TLRPC$TL_dataJSON();
            tLRPC$TL_messages_requestWebView.theme_params = tLRPC$TL_dataJSON;
            tLRPC$TL_dataJSON.data = jSONObject.toString();
            tLRPC$TL_messages_requestWebView.flags |= 4;
        } catch (Exception e) {
            FileLog.e(e);
        }
        tLRPC$TL_messages_requestWebView.from_bot_menu = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_requestWebView, new BotWebViewMenuContainer$$ExternalSyntheticLambda18(this));
    }

    public /* synthetic */ void lambda$loadWebView$15(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new BotWebViewMenuContainer$$ExternalSyntheticLambda13(this, tLObject));
    }

    public /* synthetic */ void lambda$loadWebView$14(TLObject tLObject) {
        if (tLObject instanceof TLRPC$TL_webViewResultUrl) {
            this.isLoaded = true;
            TLRPC$TL_webViewResultUrl tLRPC$TL_webViewResultUrl = (TLRPC$TL_webViewResultUrl) tLObject;
            this.queryId = tLRPC$TL_webViewResultUrl.query_id;
            this.webViewContainer.loadUrl(this.currentAccount, tLRPC$TL_webViewResultUrl.url);
            this.swipeContainer.setWebView(this.webViewContainer.getWebView());
            AndroidUtilities.runOnUIThread(this.pollRunnable, 60000L);
        }
    }

    public int getColor(String str) {
        Integer num;
        Theme.ResourcesProvider resourceProvider = this.parentEnterView.getParentFragment().getResourceProvider();
        if (resourceProvider != null) {
            num = resourceProvider.getColor(str);
        } else {
            num = Integer.valueOf(Theme.getColor(str));
        }
        return num != null ? num.intValue() : Theme.getColor(str);
    }

    public void setOnDismissGlobalListener(Runnable runnable) {
        this.globalOnDismissListener = runnable;
    }

    public void dismiss() {
        dismiss(null);
    }

    public void dismiss(Runnable runnable) {
        if (this.dismissed) {
            return;
        }
        this.dismissed = true;
        ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = this.swipeContainer;
        webViewSwipeContainer.stickTo(webViewSwipeContainer.getHeight() + this.parentEnterView.getSizeNotifierLayout().measureKeyboardHeight(), new BotWebViewMenuContainer$$ExternalSyntheticLambda12(this, runnable));
    }

    public /* synthetic */ void lambda$dismiss$16(Runnable runnable) {
        onDismiss();
        if (runnable != null) {
            runnable.run();
        }
        Runnable runnable2 = this.globalOnDismissListener;
        if (runnable2 != null) {
            runnable2.run();
        }
    }

    public void onDismiss() {
        setVisibility(8);
        this.overrideActionBarBackground = 0;
        this.overrideActionBarBackgroundProgress = 0.0f;
        this.actionBarPaint.setColor(getColor("windowBackgroundWhite"));
        this.webViewContainer.destroyWebView();
        this.swipeContainer.removeView(this.webViewContainer);
        BotWebViewContainer botWebViewContainer = new BotWebViewContainer(getContext(), this.parentEnterView.getParentFragment().getResourceProvider(), getColor("windowBackgroundWhite"));
        this.webViewContainer = botWebViewContainer;
        botWebViewContainer.setDelegate(this.webViewDelegate);
        this.webViewContainer.setWebViewProgressListener(new BotWebViewMenuContainer$$ExternalSyntheticLambda4(this));
        this.swipeContainer.addView(this.webViewContainer);
        this.isLoaded = false;
        AndroidUtilities.cancelRunOnUIThread(this.pollRunnable);
        boolean z = this.botWebViewButtonWasVisible;
        if (z) {
            this.botWebViewButtonWasVisible = false;
            animateBotButton(false);
        }
        AndroidUtilities.runOnUIThread(new BotWebViewMenuContainer$$ExternalSyntheticLambda8(this), z ? 200L : 0L);
    }

    public /* synthetic */ void lambda$onDismiss$18(Float f) {
        this.progressView.setLoadProgressAnimated(f.floatValue());
        if (f.floatValue() == 1.0f) {
            ValueAnimator duration = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.addUpdateListener(new BotWebViewMenuContainer$$ExternalSyntheticLambda0(this));
            duration.addListener(new AnonymousClass7());
            duration.start();
        }
    }

    /* renamed from: org.telegram.ui.Components.BotWebViewMenuContainer$7 */
    /* loaded from: classes3.dex */
    public class AnonymousClass7 extends AnimatorListenerAdapter {
        AnonymousClass7() {
            BotWebViewMenuContainer.this = r1;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            BotWebViewMenuContainer.this.progressView.setVisibility(8);
        }
    }

    public /* synthetic */ void lambda$onDismiss$17(ValueAnimator valueAnimator) {
        this.progressView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public /* synthetic */ void lambda$onDismiss$19() {
        if (this.savedEditText != null) {
            this.parentEnterView.getEditField().setText(this.savedEditText);
            this.savedEditText = null;
        }
        if (this.savedReplyMessageObject != null) {
            ChatActivity parentFragment = this.parentEnterView.getParentFragment();
            if (parentFragment != null) {
                parentFragment.showFieldPanelForReply(this.savedReplyMessageObject);
            }
            this.savedReplyMessageObject = null;
        }
        if (this.savedEditMessageObject != null) {
            ChatActivity parentFragment2 = this.parentEnterView.getParentFragment();
            if (parentFragment2 != null) {
                parentFragment2.showFieldPanelForEdit(true, this.savedEditMessageObject);
            }
            this.savedEditMessageObject = null;
        }
    }

    public boolean hasSavedText() {
        return (this.savedEditText == null && this.savedReplyMessageObject == null && this.savedEditMessageObject == null) ? false : true;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.webViewResultSent) {
            if (this.queryId != ((Long) objArr[0]).longValue()) {
                return;
            }
            dismiss();
        } else if (i != NotificationCenter.didSetNewTheme) {
        } else {
            this.webViewContainer.updateFlickerBackgroundColor(getColor("windowBackgroundWhite"));
            invalidate();
            invalidateActionBar();
            AndroidUtilities.runOnUIThread(new BotWebViewMenuContainer$$ExternalSyntheticLambda10(this), 300L);
        }
    }
}
