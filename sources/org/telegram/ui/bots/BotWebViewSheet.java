package org.telegram.ui.bots;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.core.util.Consumer;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheetTabDialog;
import org.telegram.ui.ActionBar.BottomSheetTabs;
import org.telegram.ui.ActionBar.BottomSheetTabsOverlay;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.OverlayActionBarLayoutDialog;
import org.telegram.ui.Components.PasscodeView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.SimpleFloatPropertyCompat;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.VerticalPositionAutoAnimator;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.TopicsFragment;
import org.telegram.ui.bots.BotWebViewAttachedSheet;
import org.telegram.ui.bots.BotWebViewSheet;
import org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout;
import org.telegram.ui.web.BotWebViewContainer;
/* loaded from: classes4.dex */
public class BotWebViewSheet extends Dialog implements NotificationCenter.NotificationCenterDelegate, BottomSheetTabsOverlay.Sheet {
    private static final SimpleFloatPropertyCompat ACTION_BAR_TRANSITION_PROGRESS_VALUE = new SimpleFloatPropertyCompat("actionBarTransitionProgress", new SimpleFloatPropertyCompat.Getter() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda0
        @Override // org.telegram.ui.Components.SimpleFloatPropertyCompat.Getter
        public final float get(Object obj) {
            float f;
            f = ((BotWebViewSheet) obj).actionBarTransitionProgress;
            return f;
        }
    }, new SimpleFloatPropertyCompat.Setter() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda1
        @Override // org.telegram.ui.Components.SimpleFloatPropertyCompat.Setter
        public final void set(Object obj, float f) {
            BotWebViewSheet.lambda$static$2((BotWebViewSheet) obj, f);
        }
    }).setMultiplier(100.0f);
    private ActionBar actionBar;
    private int actionBarColor;
    private int actionBarColorKey;
    private boolean actionBarIsLight;
    private Paint actionBarPaint;
    private Drawable actionBarShadow;
    private float actionBarTransitionProgress;
    private boolean backButtonShown;
    private Paint backgroundPaint;
    private long botId;
    private String buttonText;
    private int currentAccount;
    private TLRPC.BotApp currentWebApp;
    private boolean defaultFullsize;
    private Paint dimPaint;
    private boolean dismissed;
    private boolean forceExpnaded;
    private Boolean fullsize;
    private boolean ignoreLayout;
    private long lastSwipeTime;
    private int lineColor;
    private Paint linePaint;
    private TextView mainButton;
    private VerticalPositionAutoAnimator mainButtonAutoAnimator;
    private boolean mainButtonProgressWasVisible;
    private BotWebViewAttachedSheet.MainButtonSettings mainButtonSettings;
    private boolean mainButtonWasVisible;
    private int navBarColor;
    private boolean needCloseConfirmation;
    private boolean needsContext;
    private boolean overrideBackgroundColor;
    private Activity parentActivity;
    private PasscodeView passcodeView;
    private long peerId;
    private Runnable pollRunnable;
    private ChatAttachAlertBotWebViewLayout.WebProgressView progressView;
    private long queryId;
    private VerticalPositionAutoAnimator radialProgressAutoAnimator;
    private RadialProgressView radialProgressView;
    private int replyToMsgId;
    private WebViewRequestProps requestProps;
    private Theme.ResourcesProvider resourcesProvider;
    private ActionBarMenuSubItem settingsItem;
    public boolean showExpanded;
    public float showOffsetY;
    private boolean silent;
    private SpringAnimation springAnimation;
    private ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer swipeContainer;
    Drawable verifiedDrawable;
    private Boolean wasLightStatusBar;
    private BotWebViewContainer webViewContainer;
    private WindowView windowView;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 11 extends ActionBar.ActionBarMenuOnItemClick {
        11() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0() {
            BotWebViewSheet.this.dismiss();
        }

        @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
        public void onItemClick(int i) {
            if (i == -1) {
                if (BotWebViewSheet.this.webViewContainer.onBackPressed()) {
                    return;
                }
                BotWebViewSheet.this.onCheckDismissByUser();
            } else if (i == R.id.menu_open_bot) {
                Bundle bundle = new Bundle();
                bundle.putLong("user_id", BotWebViewSheet.this.botId);
                if (BotWebViewSheet.this.parentActivity instanceof LaunchActivity) {
                    ((LaunchActivity) BotWebViewSheet.this.parentActivity).lambda$runLinkRequest$93(new ChatActivity(bundle));
                }
                BotWebViewSheet.this.dismiss();
            } else if (i == R.id.menu_tos_bot) {
                Browser.openUrl(BotWebViewSheet.this.getContext(), LocaleController.getString(R.string.BotWebViewToSLink));
            } else if (i == R.id.menu_reload_page) {
                if (BotWebViewSheet.this.webViewContainer.getWebView() != null) {
                    BotWebViewSheet.this.webViewContainer.getWebView().animate().cancel();
                    BotWebViewSheet.this.webViewContainer.getWebView().animate().alpha(0.0f).start();
                }
                BotWebViewSheet.this.progressView.setLoadProgress(0.0f);
                BotWebViewSheet.this.progressView.setAlpha(1.0f);
                BotWebViewSheet.this.progressView.setVisibility(0);
                BotWebViewSheet.this.webViewContainer.setBotUser(MessagesController.getInstance(BotWebViewSheet.this.currentAccount).getUser(Long.valueOf(BotWebViewSheet.this.botId)));
                BotWebViewSheet.this.webViewContainer.loadFlickerAndSettingsItem(BotWebViewSheet.this.currentAccount, BotWebViewSheet.this.botId, BotWebViewSheet.this.settingsItem);
                BotWebViewSheet.this.webViewContainer.reload();
            } else if (i == R.id.menu_settings) {
                BotWebViewSheet.this.webViewContainer.onSettingsButtonPressed();
            } else if (i == R.id.menu_delete_bot) {
                BotWebViewSheet.deleteBot(BotWebViewSheet.this.currentAccount, BotWebViewSheet.this.botId, new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$11$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        BotWebViewSheet.11.this.lambda$onItemClick$0();
                    }
                });
            } else if (i == R.id.menu_add_to_home_screen_bot) {
                MediaDataController.getInstance(BotWebViewSheet.this.currentAccount).installShortcut(BotWebViewSheet.this.botId, MediaDataController.SHORTCUT_TYPE_ATTACHED_BOT);
            } else if (i == R.id.menu_collapse_bot) {
                BotWebViewSheet.this.forceExpnaded = true;
                BotWebViewSheet.this.dismiss(true, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 12 implements View.OnLayoutChangeListener {
        12() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            view.removeOnLayoutChangeListener(this);
            BotWebViewSheet.this.swipeContainer.setSwipeOffsetY(BotWebViewSheet.this.swipeContainer.getHeight());
            BotWebViewSheet.this.windowView.setAlpha(1.0f);
            final AnimationNotificationsLocker animationNotificationsLocker = new AnimationNotificationsLocker();
            animationNotificationsLocker.lock();
            BotWebViewSheet botWebViewSheet = BotWebViewSheet.this;
            if (botWebViewSheet.showOffsetY != Float.MAX_VALUE) {
                botWebViewSheet.swipeContainer.setSwipeOffsetAnimationDisallowed(true);
                BotWebViewSheet.this.swipeContainer.setOffsetY(BotWebViewSheet.this.showOffsetY);
                BotWebViewSheet.this.swipeContainer.setSwipeOffsetAnimationDisallowed(false);
            }
            BotWebViewSheet botWebViewSheet2 = BotWebViewSheet.this;
            if (botWebViewSheet2.showExpanded || botWebViewSheet2.isFullSize()) {
                BotWebViewSheet.this.swipeContainer.stickTo((-BotWebViewSheet.this.swipeContainer.getOffsetY()) + BotWebViewSheet.this.swipeContainer.getTopActionBarOffsetY(), new BotWebViewAttachedSheet$9$$ExternalSyntheticLambda1(animationNotificationsLocker));
            } else {
                ((SpringAnimation) new SpringAnimation(BotWebViewSheet.this.swipeContainer, ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.SWIPE_OFFSET_Y, 0.0f).setSpring(new SpringForce(0.0f).setDampingRatio(0.75f).setStiffness(500.0f)).addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$12$$ExternalSyntheticLambda0
                    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                        AnimationNotificationsLocker.this.unlock();
                    }
                })).start();
            }
            BotWebViewSheet.this.swipeContainer.opened = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class 3 implements BotWebViewContainer.Delegate {
        private boolean sentWebViewData;
        final /* synthetic */ Context val$context;
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;

        3(Context context, Theme.ResourcesProvider resourcesProvider) {
            this.val$context = context;
            this.val$resourcesProvider = resourcesProvider;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onSendWebViewData$0(TLObject tLObject, TLRPC.TL_error tL_error) {
            if (tLObject instanceof TLRPC.TL_updates) {
                MessagesController.getInstance(BotWebViewSheet.this.currentAccount).processUpdates((TLRPC.TL_updates) tLObject, false);
            }
            final BotWebViewSheet botWebViewSheet = BotWebViewSheet.this;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewSheet.this.dismiss();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onWebAppOpenInvoice$2(String str, String str2) {
            BotWebViewSheet.this.webViewContainer.onInvoiceStatusUpdate(str, str2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onWebAppOpenInvoice$3(OverlayActionBarLayoutDialog overlayActionBarLayoutDialog, String str, PaymentFormActivity.InvoiceStatus invoiceStatus) {
            if (invoiceStatus != PaymentFormActivity.InvoiceStatus.PENDING) {
                overlayActionBarLayoutDialog.dismiss();
            }
            BotWebViewSheet.this.webViewContainer.onInvoiceStatusUpdate(str, invoiceStatus.name().toLowerCase(Locale.ROOT));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onWebAppSwitchInlineQuery$4(TLRPC.User user, String str, OverlayActionBarLayoutDialog overlayActionBarLayoutDialog, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
            String str2;
            long j = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
            Bundle bundle = new Bundle();
            bundle.putBoolean("scrollToTopOnResume", true);
            if (DialogObject.isEncryptedDialog(j)) {
                bundle.putInt("enc_id", DialogObject.getEncryptedChatId(j));
            } else {
                if (DialogObject.isUserDialog(j)) {
                    str2 = "user_id";
                } else {
                    j = -j;
                    str2 = "chat_id";
                }
                bundle.putLong(str2, j);
            }
            bundle.putString("start_text", "@" + UserObject.getPublicUsername(user) + " " + str);
            if (BotWebViewSheet.this.parentActivity instanceof LaunchActivity) {
                BaseFragment lastFragment = ((LaunchActivity) BotWebViewSheet.this.parentActivity).getActionBarLayout().getLastFragment();
                if (MessagesController.getInstance(BotWebViewSheet.this.currentAccount).checkCanOpenChat(bundle, lastFragment)) {
                    overlayActionBarLayoutDialog.dismiss();
                    BotWebViewSheet.this.dismissed = true;
                    AndroidUtilities.cancelRunOnUIThread(BotWebViewSheet.this.pollRunnable);
                    BotWebViewSheet.this.webViewContainer.destroyWebView();
                    NotificationCenter.getInstance(BotWebViewSheet.this.currentAccount).removeObserver(BotWebViewSheet.this, NotificationCenter.webViewResultSent);
                    NotificationCenter.getGlobalInstance().removeObserver(BotWebViewSheet.this, NotificationCenter.didSetNewTheme);
                    BotWebViewSheet.super.dismiss();
                    lastFragment.presentFragment(new INavigationLayout.NavigationParams(new ChatActivity(bundle)).setRemoveLast(true));
                }
            }
            return true;
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public boolean isClipboardAvailable() {
            return MediaDataController.getInstance(BotWebViewSheet.this.currentAccount).botInAttachMenu(BotWebViewSheet.this.botId);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onCloseRequested(Runnable runnable) {
            BotWebViewSheet.this.dismiss(runnable);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public /* synthetic */ void onCloseToTabs() {
            onCloseRequested(null);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public /* synthetic */ void onInstantClose() {
            onCloseRequested(null);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onSendWebViewData(String str) {
            if (BotWebViewSheet.this.queryId != 0 || this.sentWebViewData) {
                return;
            }
            this.sentWebViewData = true;
            TLRPC.TL_messages_sendWebViewData tL_messages_sendWebViewData = new TLRPC.TL_messages_sendWebViewData();
            tL_messages_sendWebViewData.bot = MessagesController.getInstance(BotWebViewSheet.this.currentAccount).getInputUser(BotWebViewSheet.this.botId);
            tL_messages_sendWebViewData.random_id = Utilities.random.nextLong();
            tL_messages_sendWebViewData.button_text = BotWebViewSheet.this.buttonText;
            tL_messages_sendWebViewData.data = str;
            ConnectionsManager.getInstance(BotWebViewSheet.this.currentAccount).sendRequest(tL_messages_sendWebViewData, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda1
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    BotWebViewSheet.3.this.lambda$onSendWebViewData$0(tLObject, tL_error);
                }
            });
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onSetBackButtonVisible(boolean z) {
            AndroidUtilities.updateImageViewImageAnimated(BotWebViewSheet.this.actionBar.getBackButton(), BotWebViewSheet.this.backButtonShown = z ? R.drawable.ic_ab_back : R.drawable.ic_close_white);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onSetSettingsButtonVisible(boolean z) {
            if (BotWebViewSheet.this.settingsItem != null) {
                BotWebViewSheet.this.settingsItem.setVisibility(z ? 0 : 8);
            }
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onSetupMainButton(boolean z, boolean z2, String str, int i, int i2, boolean z3, boolean z4) {
            BotWebViewSheet.this.setMainButton(BotWebViewAttachedSheet.MainButtonSettings.of(z, z2, str, i, i2, z3));
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onSetupSecondaryButton(boolean z, boolean z2, String str, int i, int i2, boolean z3, boolean z4, String str2) {
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public /* synthetic */ void onWebAppBackgroundChanged(boolean z, int i) {
            BotWebViewContainer.Delegate.-CC.$default$onWebAppBackgroundChanged(this, z, i);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppExpand() {
            if (BotWebViewSheet.this.swipeContainer.isSwipeInProgress()) {
                return;
            }
            BotWebViewSheet.this.swipeContainer.stickTo((-BotWebViewSheet.this.swipeContainer.getOffsetY()) + BotWebViewSheet.this.swipeContainer.getTopActionBarOffsetY());
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppOpenInvoice(TLRPC.InputInvoice inputInvoice, final String str, TLObject tLObject) {
            PaymentFormActivity paymentFormActivity;
            BaseFragment lastFragment = ((LaunchActivity) BotWebViewSheet.this.parentActivity).getActionBarLayout().getLastFragment();
            if (tLObject instanceof TLRPC.TL_payments_paymentFormStars) {
                AndroidUtilities.hideKeyboard(BotWebViewSheet.this.windowView);
                final AlertDialog alertDialog = new AlertDialog(BotWebViewSheet.this.getContext(), 3);
                alertDialog.showDelayed(150L);
                StarsController.getInstance(BotWebViewSheet.this.currentAccount).openPaymentForm(null, inputInvoice, (TLRPC.TL_payments_paymentFormStars) tLObject, new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        AlertDialog.this.dismiss();
                    }
                }, new Utilities.Callback() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda3
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        BotWebViewSheet.3.this.lambda$onWebAppOpenInvoice$2(str, (String) obj);
                    }
                });
                return;
            }
            if (tLObject instanceof TLRPC.PaymentForm) {
                TLRPC.PaymentForm paymentForm = (TLRPC.PaymentForm) tLObject;
                MessagesController.getInstance(BotWebViewSheet.this.currentAccount).putUsers(paymentForm.users, false);
                paymentFormActivity = new PaymentFormActivity(paymentForm, str, lastFragment);
            } else {
                paymentFormActivity = tLObject instanceof TLRPC.PaymentReceipt ? new PaymentFormActivity((TLRPC.PaymentReceipt) tLObject) : null;
            }
            if (paymentFormActivity != null) {
                BotWebViewSheet.this.swipeContainer.stickTo((-BotWebViewSheet.this.swipeContainer.getOffsetY()) + BotWebViewSheet.this.swipeContainer.getTopActionBarOffsetY());
                AndroidUtilities.hideKeyboard(BotWebViewSheet.this.windowView);
                final OverlayActionBarLayoutDialog overlayActionBarLayoutDialog = new OverlayActionBarLayoutDialog(this.val$context, this.val$resourcesProvider);
                overlayActionBarLayoutDialog.show();
                paymentFormActivity.setPaymentFormCallback(new PaymentFormActivity.PaymentFormCallback() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda4
                    @Override // org.telegram.ui.PaymentFormActivity.PaymentFormCallback
                    public final void onInvoiceStatusChanged(PaymentFormActivity.InvoiceStatus invoiceStatus) {
                        BotWebViewSheet.3.this.lambda$onWebAppOpenInvoice$3(overlayActionBarLayoutDialog, str, invoiceStatus);
                    }
                });
                paymentFormActivity.setResourcesProvider(this.val$resourcesProvider);
                overlayActionBarLayoutDialog.addFragment(paymentFormActivity);
            }
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public /* synthetic */ void onWebAppReady() {
            BotWebViewContainer.Delegate.-CC.$default$onWebAppReady(this);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppSetActionBarColor(int i, int i2, boolean z) {
            BotWebViewSheet.this.actionBarColorKey = i;
            BotWebViewSheet.this.setActionBarColor(i2, z, true);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppSetBackgroundColor(int i) {
            BotWebViewSheet.this.setBackgroundColor(i, true);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public /* synthetic */ void onWebAppSetNavigationBarColor(int i) {
            BotWebViewContainer.Delegate.-CC.$default$onWebAppSetNavigationBarColor(this, i);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppSetupClosingBehavior(boolean z) {
            BotWebViewSheet.this.needCloseConfirmation = z;
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppSwipingBehavior(boolean z) {
            BotWebViewSheet.this.swipeContainer.setAllowSwipes(z);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppSwitchInlineQuery(final TLRPC.User user, final String str, List list) {
            if (list.isEmpty()) {
                if (BotWebViewSheet.this.parentActivity instanceof LaunchActivity) {
                    BaseFragment lastFragment = ((LaunchActivity) BotWebViewSheet.this.parentActivity).getActionBarLayout().getLastFragment();
                    if (lastFragment instanceof ChatActivity) {
                        ChatActivityEnterView chatActivityEnterView = ((ChatActivity) lastFragment).getChatActivityEnterView();
                        chatActivityEnterView.setFieldText("@" + UserObject.getPublicUsername(user) + " " + str);
                        BotWebViewSheet.this.dismiss();
                        return;
                    }
                    return;
                }
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("dialogsType", 14);
            bundle.putBoolean("onlySelect", true);
            bundle.putBoolean("allowGroups", list.contains("groups"));
            bundle.putBoolean("allowMegagroups", list.contains("groups"));
            bundle.putBoolean("allowLegacyGroups", list.contains("groups"));
            bundle.putBoolean("allowUsers", list.contains("users"));
            bundle.putBoolean("allowChannels", list.contains("channels"));
            bundle.putBoolean("allowBots", list.contains("bots"));
            DialogsActivity dialogsActivity = new DialogsActivity(bundle);
            AndroidUtilities.hideKeyboard(BotWebViewSheet.this.windowView);
            final OverlayActionBarLayoutDialog overlayActionBarLayoutDialog = new OverlayActionBarLayoutDialog(this.val$context, this.val$resourcesProvider);
            dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$3$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
                    boolean lambda$onWebAppSwitchInlineQuery$4;
                    lambda$onWebAppSwitchInlineQuery$4 = BotWebViewSheet.3.this.lambda$onWebAppSwitchInlineQuery$4(user, str, overlayActionBarLayoutDialog, dialogsActivity2, arrayList, charSequence, z, z2, i, topicsFragment);
                    return lambda$onWebAppSwitchInlineQuery$4;
                }
            });
            overlayActionBarLayoutDialog.show();
            overlayActionBarLayoutDialog.addFragment(dialogsActivity);
        }
    }

    /* loaded from: classes4.dex */
    public class WindowView extends SizeNotifierFrameLayout implements BottomSheetTabsOverlay.SheetView {
        private final Path clipPath;
        private boolean drawingFromOverlay;
        private final Paint navbarPaint;
        private final RectF rect;

        public WindowView(Context context) {
            super(context);
            setClipChildren(false);
            setClipToPadding(false);
            setWillNotDraw(false);
            this.navbarPaint = new Paint(1);
            this.rect = new RectF();
            this.clipPath = new Path();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            if (this.drawingFromOverlay) {
                return;
            }
            super.dispatchDraw(canvas);
            if (BotWebViewSheet.this.passcodeView.getVisibility() != 0) {
                this.navbarPaint.setColor(BotWebViewSheet.this.navBarColor);
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, getHeight() - getPaddingBottom(), getWidth(), getHeight() + AndroidUtilities.navigationBarHeight);
                canvas.drawRect(rectF, this.navbarPaint);
            }
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            if (this.drawingFromOverlay) {
                return;
            }
            super.draw(canvas);
            float f = AndroidUtilities.isTablet() ? 0.0f : BotWebViewSheet.this.actionBarTransitionProgress;
            BotWebViewSheet.this.linePaint.setColor(BotWebViewSheet.this.lineColor);
            BotWebViewSheet.this.linePaint.setAlpha((int) (BotWebViewSheet.this.linePaint.getAlpha() * (1.0f - (Math.min(0.5f, f) / 0.5f))));
            canvas.save();
            float f2 = 1.0f - f;
            float lerp = AndroidUtilities.isTablet() ? AndroidUtilities.lerp(BotWebViewSheet.this.swipeContainer.getTranslationY() + AndroidUtilities.dp(12.0f), AndroidUtilities.statusBarHeight / 2.0f, BotWebViewSheet.this.actionBarTransitionProgress) : AndroidUtilities.lerp(BotWebViewSheet.this.swipeContainer.getTranslationY(), AndroidUtilities.statusBarHeight + (ActionBar.getCurrentActionBarHeight() / 2.0f), f) + AndroidUtilities.dp(12.0f);
            canvas.scale(f2, f2, getWidth() / 2.0f, lerp);
            canvas.drawLine((getWidth() / 2.0f) - AndroidUtilities.dp(16.0f), lerp, (getWidth() / 2.0f) + AndroidUtilities.dp(16.0f), lerp, BotWebViewSheet.this.linePaint);
            canvas.restore();
            BotWebViewSheet.this.actionBarShadow.setAlpha((int) (BotWebViewSheet.this.actionBar.getAlpha() * 255.0f));
            float y = BotWebViewSheet.this.actionBar.getY() + BotWebViewSheet.this.actionBar.getTranslationY() + BotWebViewSheet.this.actionBar.getHeight();
            BotWebViewSheet.this.actionBarShadow.setBounds(0, (int) y, getWidth(), (int) (y + BotWebViewSheet.this.actionBarShadow.getIntrinsicHeight()));
            BotWebViewSheet.this.actionBarShadow.draw(canvas);
        }

        @Override // org.telegram.ui.ActionBar.BottomSheetTabsOverlay.SheetView
        public float drawInto(Canvas canvas, RectF rectF, float f, RectF rectF2, float f2, boolean z) {
            this.rect.set(BotWebViewSheet.this.swipeContainer.getLeft(), BotWebViewSheet.this.swipeContainer.getTranslationY() + AndroidUtilities.dp(24.0f), BotWebViewSheet.this.swipeContainer.getRight(), getHeight());
            AndroidUtilities.lerpCentered(this.rect, rectF, f, rectF2);
            canvas.save();
            this.clipPath.rewind();
            float lerp = AndroidUtilities.lerp(AndroidUtilities.dp(16.0f) * (AndroidUtilities.isTablet() ? 1.0f : 1.0f - BotWebViewSheet.this.actionBarTransitionProgress), AndroidUtilities.dp(10.0f), f);
            this.clipPath.addRoundRect(rectF2, lerp, lerp, Path.Direction.CW);
            canvas.clipPath(this.clipPath);
            canvas.drawPaint(BotWebViewSheet.this.backgroundPaint);
            if (BotWebViewSheet.this.swipeContainer != null) {
                canvas.save();
                canvas.translate(rectF2.left, Math.max(BotWebViewSheet.this.swipeContainer.getY(), rectF2.top) + (f * AndroidUtilities.dp(51.0f)));
                BotWebViewSheet.this.swipeContainer.draw(canvas);
                canvas.restore();
            }
            canvas.restore();
            return lerp;
        }

        @Override // org.telegram.ui.ActionBar.BottomSheetTabsOverlay.SheetView
        public RectF getRect() {
            this.rect.set(BotWebViewSheet.this.swipeContainer.getLeft(), BotWebViewSheet.this.swipeContainer.getTranslationY() + AndroidUtilities.dp(24.0f), BotWebViewSheet.this.swipeContainer.getRight(), getHeight());
            return this.rect;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            Bulletin.addDelegate(this, new Bulletin.Delegate() { // from class: org.telegram.ui.bots.BotWebViewSheet.WindowView.1
                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean allowLayoutChanges() {
                    return Bulletin.Delegate.-CC.$default$allowLayoutChanges(this);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean bottomOffsetAnimated() {
                    return Bulletin.Delegate.-CC.$default$bottomOffsetAnimated(this);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ boolean clipWithGradient(int i) {
                    return Bulletin.Delegate.-CC.$default$clipWithGradient(this, i);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ int getBottomOffset(int i) {
                    return Bulletin.Delegate.-CC.$default$getBottomOffset(this, i);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public int getTopOffset(int i) {
                    return AndroidUtilities.statusBarHeight;
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onBottomOffsetChange(float f) {
                    Bulletin.Delegate.-CC.$default$onBottomOffsetChange(this, f);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onHide(Bulletin bulletin) {
                    Bulletin.Delegate.-CC.$default$onHide(this, bulletin);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ void onShow(Bulletin bulletin) {
                    Bulletin.Delegate.-CC.$default$onShow(this, bulletin);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            Bulletin.removeDelegate(this);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.drawingFromOverlay) {
                return;
            }
            super.onDraw(canvas);
            if (BotWebViewSheet.this.passcodeView.getVisibility() != 0) {
                if (!BotWebViewSheet.this.overrideBackgroundColor) {
                    BotWebViewSheet.this.backgroundPaint.setColor(BotWebViewSheet.this.getColor(Theme.key_windowBackgroundWhite));
                }
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(0.0f, 0.0f, getWidth(), getHeight());
                canvas.drawRect(rectF, BotWebViewSheet.this.dimPaint);
                BotWebViewSheet.this.actionBarPaint.setColor(BotWebViewSheet.this.actionBarColor);
                float dp = AndroidUtilities.dp(16.0f) * (AndroidUtilities.isTablet() ? 1.0f : 1.0f - BotWebViewSheet.this.actionBarTransitionProgress);
                rectF.set(BotWebViewSheet.this.swipeContainer.getLeft(), AndroidUtilities.lerp(BotWebViewSheet.this.swipeContainer.getTranslationY(), 0.0f, BotWebViewSheet.this.actionBarTransitionProgress), BotWebViewSheet.this.swipeContainer.getRight(), BotWebViewSheet.this.swipeContainer.getTranslationY() + AndroidUtilities.dp(24.0f) + dp);
                canvas.drawRoundRect(rectF, dp, dp, BotWebViewSheet.this.actionBarPaint);
                rectF.set(BotWebViewSheet.this.swipeContainer.getLeft(), BotWebViewSheet.this.swipeContainer.getTranslationY() + AndroidUtilities.dp(24.0f), BotWebViewSheet.this.swipeContainer.getRight(), getHeight());
                canvas.drawRect(rectF, BotWebViewSheet.this.backgroundPaint);
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0 || (motionEvent.getY() > AndroidUtilities.lerp(BotWebViewSheet.this.swipeContainer.getTranslationY() + AndroidUtilities.dp(24.0f), 0.0f, BotWebViewSheet.this.actionBarTransitionProgress) && motionEvent.getX() <= BotWebViewSheet.this.swipeContainer.getRight() && motionEvent.getX() >= BotWebViewSheet.this.swipeContainer.getLeft())) {
                return super.onTouchEvent(motionEvent);
            }
            BotWebViewSheet.this.dismiss(true, null);
            return true;
        }

        @Override // org.telegram.ui.ActionBar.BottomSheetTabsOverlay.SheetView
        public void setDrawingFromOverlay(boolean z) {
            if (this.drawingFromOverlay != z) {
                this.drawingFromOverlay = z;
                invalidate();
            }
        }
    }

    public BotWebViewSheet(Context context, Theme.ResourcesProvider resourcesProvider) {
        super(context, R.style.TransparentDialog);
        this.actionBarTransitionProgress = 0.0f;
        this.linePaint = new Paint(1);
        this.dimPaint = new Paint();
        this.backgroundPaint = new Paint(1);
        this.actionBarPaint = new Paint(1);
        this.pollRunnable = new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$new$5();
            }
        };
        this.actionBarColorKey = -1;
        this.defaultFullsize = false;
        this.fullsize = null;
        this.resourcesProvider = resourcesProvider;
        this.lineColor = Theme.getColor(Theme.key_sheet_scrollUp);
        ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = new ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer(context) { // from class: org.telegram.ui.bots.BotWebViewSheet.1
            /* JADX WARN: Removed duplicated region for block: B:10:0x001f  */
            /* JADX WARN: Removed duplicated region for block: B:25:0x0081  */
            @Override // android.widget.FrameLayout, android.view.View
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            protected void onMeasure(int i, int i2) {
                int i3;
                float f;
                int size = View.MeasureSpec.getSize(i2);
                if (!AndroidUtilities.isTablet()) {
                    Point point = AndroidUtilities.displaySize;
                    if (point.x > point.y) {
                        i3 = (int) (size / 3.5f);
                        if (i3 < 0) {
                            i3 = 0;
                        }
                        f = i3;
                        if (getOffsetY() != f && !BotWebViewSheet.this.dismissed) {
                            BotWebViewSheet.this.ignoreLayout = true;
                            setOffsetY(f);
                            BotWebViewSheet.this.ignoreLayout = false;
                        }
                        if (AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet()) {
                            Point point2 = AndroidUtilities.displaySize;
                            i = View.MeasureSpec.makeMeasureSpec((int) (Math.min(point2.x, point2.y) * 0.8f), 1073741824);
                        }
                        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec((((View.MeasureSpec.getSize(i2) - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) + AndroidUtilities.dp(24.0f)) - (BotWebViewSheet.this.mainButtonWasVisible ? BotWebViewSheet.this.mainButton.getLayoutParams().height : 0), 1073741824));
                    }
                }
                i3 = (size / 5) * 2;
                if (i3 < 0) {
                }
                f = i3;
                if (getOffsetY() != f) {
                    BotWebViewSheet.this.ignoreLayout = true;
                    setOffsetY(f);
                    BotWebViewSheet.this.ignoreLayout = false;
                }
                if (AndroidUtilities.isTablet()) {
                    Point point22 = AndroidUtilities.displaySize;
                    i = View.MeasureSpec.makeMeasureSpec((int) (Math.min(point22.x, point22.y) * 0.8f), 1073741824);
                }
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec((((View.MeasureSpec.getSize(i2) - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.statusBarHeight) + AndroidUtilities.dp(24.0f)) - (BotWebViewSheet.this.mainButtonWasVisible ? BotWebViewSheet.this.mainButton.getLayoutParams().height : 0), 1073741824));
            }

            @Override // android.view.View, android.view.ViewParent
            public void requestLayout() {
                if (BotWebViewSheet.this.ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };
        this.swipeContainer = webViewSwipeContainer;
        webViewSwipeContainer.setAllowFullSizeSwipe(true);
        this.swipeContainer.setShouldWaitWebViewScroll(true);
        int i = Theme.key_windowBackgroundWhite;
        BotWebViewContainer botWebViewContainer = new BotWebViewContainer(context, resourcesProvider, getColor(i), true) { // from class: org.telegram.ui.bots.BotWebViewSheet.2
            @Override // org.telegram.ui.web.BotWebViewContainer
            public void onWebViewCreated() {
                super.onWebViewCreated();
                BotWebViewSheet.this.swipeContainer.setWebView(BotWebViewSheet.this.webViewContainer.getWebView());
            }
        };
        this.webViewContainer = botWebViewContainer;
        botWebViewContainer.setDelegate(new 3(context, resourcesProvider));
        this.linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.linePaint.setStrokeWidth(AndroidUtilities.dp(4.0f));
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);
        this.dimPaint.setColor(1073741824);
        this.actionBarColor = getColor(i);
        this.navBarColor = getColor(Theme.key_windowBackgroundGray);
        AndroidUtilities.setNavigationBarColor(getWindow(), this.navBarColor, false);
        WindowView windowView = new WindowView(context);
        this.windowView = windowView;
        windowView.setDelegate(new SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda10
            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate
            public final void onSizeChanged(int i2, boolean z) {
                BotWebViewSheet.this.lambda$new$6(i2, z);
            }
        });
        this.windowView.addView(this.swipeContainer, LayoutHelper.createFrame(-1, -1.0f, 49, 0.0f, 24.0f, 0.0f, 0.0f));
        TextView textView = new TextView(context) { // from class: org.telegram.ui.bots.BotWebViewSheet.4
            @Override // android.widget.TextView, android.view.View
            protected void onMeasure(int i2, int i3) {
                if (AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet()) {
                    Point point = AndroidUtilities.displaySize;
                    i2 = View.MeasureSpec.makeMeasureSpec((int) (Math.min(point.x, point.y) * 0.8f), 1073741824);
                }
                super.onMeasure(i2, i3);
            }
        };
        this.mainButton = textView;
        textView.setVisibility(8);
        this.mainButton.setAlpha(0.0f);
        this.mainButton.setSingleLine();
        this.mainButton.setGravity(17);
        this.mainButton.setTypeface(AndroidUtilities.bold());
        int dp = AndroidUtilities.dp(16.0f);
        this.mainButton.setPadding(dp, 0, dp, 0);
        this.mainButton.setTextSize(1, 14.0f);
        this.mainButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                BotWebViewSheet.this.lambda$new$7(view);
            }
        });
        this.windowView.addView(this.mainButton, LayoutHelper.createFrame(-1, 48, 81));
        this.mainButtonAutoAnimator = VerticalPositionAutoAnimator.attach(this.mainButton);
        RadialProgressView radialProgressView = new RadialProgressView(context) { // from class: org.telegram.ui.bots.BotWebViewSheet.5
            @Override // android.view.View
            protected void onMeasure(int i2, int i3) {
                int dp2;
                super.onMeasure(i2, i3);
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                if (!AndroidUtilities.isTablet() || AndroidUtilities.isInMultiwindow || AndroidUtilities.isSmallTablet()) {
                    dp2 = AndroidUtilities.dp(10.0f);
                } else {
                    Point point = AndroidUtilities.displaySize;
                    dp2 = (int) (AndroidUtilities.dp(10.0f) + (Math.min(point.x, point.y) * 0.1f));
                }
                marginLayoutParams.rightMargin = dp2;
            }
        };
        this.radialProgressView = radialProgressView;
        radialProgressView.setSize(AndroidUtilities.dp(18.0f));
        this.radialProgressView.setAlpha(0.0f);
        this.radialProgressView.setScaleX(0.1f);
        this.radialProgressView.setScaleY(0.1f);
        this.radialProgressView.setVisibility(8);
        this.windowView.addView(this.radialProgressView, LayoutHelper.createFrame(28, 28.0f, 85, 0.0f, 0.0f, 10.0f, 10.0f));
        this.radialProgressAutoAnimator = VerticalPositionAutoAnimator.attach(this.radialProgressView);
        this.actionBarShadow = ContextCompat.getDrawable(getContext(), R.drawable.header_shadow).mutate();
        ActionBar actionBar = new ActionBar(context, resourcesProvider) { // from class: org.telegram.ui.bots.BotWebViewSheet.6
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.ActionBar.ActionBar, android.widget.FrameLayout, android.view.View
            public void onMeasure(int i2, int i3) {
                if (AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet()) {
                    Point point = AndroidUtilities.displaySize;
                    i2 = View.MeasureSpec.makeMeasureSpec((int) (Math.min(point.x, point.y) * 0.8f), 1073741824);
                }
                super.onMeasure(i2, i3);
            }
        };
        this.actionBar = actionBar;
        actionBar.setBackgroundColor(0);
        this.actionBar.setBackButtonImage(R.drawable.ic_close_white);
        updateActionBarColors();
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.bots.BotWebViewSheet.7
            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i2) {
                if (i2 == -1) {
                    BotWebViewSheet.this.onCheckDismissByUser();
                }
            }
        });
        this.actionBar.setAlpha(0.0f);
        this.windowView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2, 49));
        WindowView windowView2 = this.windowView;
        ChatAttachAlertBotWebViewLayout.WebProgressView webProgressView = new ChatAttachAlertBotWebViewLayout.WebProgressView(context, resourcesProvider) { // from class: org.telegram.ui.bots.BotWebViewSheet.8
            @Override // android.view.View
            protected void onMeasure(int i2, int i3) {
                if (AndroidUtilities.isTablet() && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isSmallTablet()) {
                    Point point = AndroidUtilities.displaySize;
                    i2 = View.MeasureSpec.makeMeasureSpec((int) (Math.min(point.x, point.y) * 0.8f), 1073741824);
                }
                super.onMeasure(i2, i3);
            }
        };
        this.progressView = webProgressView;
        windowView2.addView(webProgressView, LayoutHelper.createFrame(-1, -2.0f, 81, 0.0f, 0.0f, 0.0f, 0.0f));
        this.webViewContainer.setWebViewProgressListener(new Consumer() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda12
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                BotWebViewSheet.this.lambda$new$9((Float) obj);
            }
        });
        this.swipeContainer.addView(this.webViewContainer, LayoutHelper.createFrame(-1, -1.0f));
        this.swipeContainer.setScrollListener(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$new$10();
            }
        });
        this.swipeContainer.setScrollEndListener(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda14
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$new$11();
            }
        });
        this.swipeContainer.setDelegate(new ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.Delegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda15
            @Override // org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.Delegate
            public final void onDismiss() {
                BotWebViewSheet.this.lambda$new$12();
            }
        });
        this.swipeContainer.setTopActionBarOffsetY((ActionBar.getCurrentActionBarHeight() + AndroidUtilities.statusBarHeight) - AndroidUtilities.dp(24.0f));
        this.swipeContainer.setIsKeyboardVisible(new GenericProvider() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda16
            @Override // org.telegram.messenger.GenericProvider
            public final Object provide(Object obj) {
                Boolean lambda$new$13;
                lambda$new$13 = BotWebViewSheet.this.lambda$new$13((Void) obj);
                return lambda$new$13;
            }
        });
        PasscodeView passcodeView = new PasscodeView(context);
        this.passcodeView = passcodeView;
        this.windowView.addView(passcodeView, LayoutHelper.createFrame(-1, -1.0f));
        setContentView(this.windowView, new ViewGroup.LayoutParams(-1, -1));
    }

    public static void deleteBot(final int i, final long j, final Runnable runnable) {
        final TLRPC.TL_attachMenuBot tL_attachMenuBot;
        Iterator<TLRPC.TL_attachMenuBot> it = MediaDataController.getInstance(i).getAttachMenuBots().bots.iterator();
        while (true) {
            if (!it.hasNext()) {
                tL_attachMenuBot = null;
                break;
            }
            TLRPC.TL_attachMenuBot next = it.next();
            if (next.bot_id == j) {
                tL_attachMenuBot = next;
                break;
            }
        }
        if (tL_attachMenuBot == null) {
            return;
        }
        new AlertDialog.Builder(LaunchActivity.getLastFragment().getContext()).setTitle(LocaleController.getString(R.string.BotRemoveFromMenuTitle)).setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("BotRemoveFromMenu", R.string.BotRemoveFromMenu, tL_attachMenuBot.short_name))).setPositiveButton(LocaleController.getString(R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda20
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                BotWebViewSheet.lambda$deleteBot$27(i, j, tL_attachMenuBot, runnable, dialogInterface, i2);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getColor(int i) {
        return Theme.getColor(i, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$deleteBot$25(int i) {
        MediaDataController.getInstance(i).loadAttachMenuBots(false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$deleteBot$26(final int i, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda30
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.lambda$deleteBot$25(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$deleteBot$27(final int i, long j, TLRPC.TL_attachMenuBot tL_attachMenuBot, Runnable runnable, DialogInterface dialogInterface, int i2) {
        TLRPC.TL_messages_toggleBotInAttachMenu tL_messages_toggleBotInAttachMenu = new TLRPC.TL_messages_toggleBotInAttachMenu();
        tL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(i).getInputUser(j);
        tL_messages_toggleBotInAttachMenu.enabled = false;
        ConnectionsManager.getInstance(i).sendRequest(tL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda28
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                BotWebViewSheet.lambda$deleteBot$26(i, tLObject, tL_error);
            }
        }, 66);
        tL_attachMenuBot.show_in_side_menu = false;
        NotificationCenter.getInstance(i).lambda$postNotificationNameOnUIThread$1(NotificationCenter.attachMenuBotsDidLoad, new Object[0]);
        MediaDataController.getInstance(i).uninstallShortcut(j, MediaDataController.SHORTCUT_TYPE_ATTACHED_BOT);
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$29(Runnable runnable) {
        super.dismiss();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$10() {
        Paint paint;
        int i;
        if (this.swipeContainer.getSwipeOffsetY() > 0.0f) {
            paint = this.dimPaint;
            i = (int) ((1.0f - MathUtils.clamp(this.swipeContainer.getSwipeOffsetY() / this.swipeContainer.getHeight(), 0.0f, 1.0f)) * 64.0f);
        } else {
            paint = this.dimPaint;
            i = 64;
        }
        paint.setAlpha(i);
        this.windowView.invalidate();
        this.webViewContainer.invalidateViewPortHeight();
        if (this.springAnimation != null) {
            float f = (1.0f - (Math.min(this.swipeContainer.getTopActionBarOffsetY(), this.swipeContainer.getTranslationY() - this.swipeContainer.getTopActionBarOffsetY()) / this.swipeContainer.getTopActionBarOffsetY()) > 0.5f ? 1 : 0) * 100.0f;
            if (this.springAnimation.getSpring().getFinalPosition() != f) {
                this.springAnimation.getSpring().setFinalPosition(f);
                this.springAnimation.start();
            }
        }
        float max = Math.max(0.0f, this.swipeContainer.getSwipeOffsetY());
        this.mainButtonAutoAnimator.setOffsetY(max);
        this.radialProgressAutoAnimator.setOffsetY(max);
        this.lastSwipeTime = System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$11() {
        this.webViewContainer.invalidateViewPortHeight(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$12() {
        dismiss(true, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$new$13(Void r2) {
        return Boolean.valueOf(this.windowView.getKeyboardHeight() >= AndroidUtilities.dp(20.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(TLRPC.TL_error tL_error) {
        if (this.dismissed) {
            return;
        }
        if (tL_error != null) {
            dismiss();
        } else {
            AndroidUtilities.runOnUIThread(this.pollRunnable, 60000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda26
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$new$3(tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5() {
        if (this.dismissed || this.queryId == 0) {
            return;
        }
        TLRPC.TL_messages_prolongWebView tL_messages_prolongWebView = new TLRPC.TL_messages_prolongWebView();
        tL_messages_prolongWebView.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botId);
        tL_messages_prolongWebView.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.peerId);
        tL_messages_prolongWebView.query_id = this.queryId;
        tL_messages_prolongWebView.silent = this.silent;
        if (this.replyToMsgId != 0) {
            tL_messages_prolongWebView.reply_to = SendMessagesHelper.getInstance(this.currentAccount).createReplyInput(this.replyToMsgId);
            tL_messages_prolongWebView.flags |= 1;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_prolongWebView, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda25
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                BotWebViewSheet.this.lambda$new$4(tLObject, tL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(int i, boolean z) {
        if (i > AndroidUtilities.dp(20.0f)) {
            ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = this.swipeContainer;
            webViewSwipeContainer.stickTo((-webViewSwipeContainer.getOffsetY()) + this.swipeContainer.getTopActionBarOffsetY());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(View view) {
        this.webViewContainer.onMainButtonPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(ValueAnimator valueAnimator) {
        this.progressView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$9(Float f) {
        this.progressView.setLoadProgressAnimated(f.floatValue());
        if (f.floatValue() == 1.0f) {
            ValueAnimator duration = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda22
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BotWebViewSheet.this.lambda$new$8(valueAnimator);
                }
            });
            duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.bots.BotWebViewSheet.9
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    BotWebViewSheet.this.progressView.setVisibility(8);
                }
            });
            duration.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCheckDismissByUser$28(DialogInterface dialogInterface, int i) {
        dismiss();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ WindowInsets lambda$onCreate$14(View view, WindowInsets windowInsets) {
        int systemWindowInsetBottom;
        WindowInsets consumeSystemWindowInsets;
        WindowInsets windowInsets2;
        systemWindowInsetBottom = windowInsets.getSystemWindowInsetBottom();
        view.setPadding(0, 0, 0, systemWindowInsetBottom);
        if (Build.VERSION.SDK_INT >= 30) {
            windowInsets2 = WindowInsets.CONSUMED;
            return windowInsets2;
        }
        consumeSystemWindowInsets = windowInsets.consumeSystemWindowInsets();
        return consumeSystemWindowInsets;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$15(TLRPC.TL_error tL_error, TLObject tLObject) {
        WebViewRequestProps webViewRequestProps;
        if (tL_error == null && (webViewRequestProps = this.requestProps) != null) {
            webViewRequestProps.applyResponse(tLObject);
            loadFromResponse(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$16(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda24
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$requestWebView$15(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$17(TLRPC.TL_error tL_error, TLObject tLObject) {
        WebViewRequestProps webViewRequestProps;
        if (tL_error == null && (webViewRequestProps = this.requestProps) != null) {
            webViewRequestProps.applyResponse(tLObject);
            loadFromResponse(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$18(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda17
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$requestWebView$17(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$19(TLRPC.TL_error tL_error, TLObject tLObject) {
        WebViewRequestProps webViewRequestProps;
        if (tL_error == null && (webViewRequestProps = this.requestProps) != null) {
            webViewRequestProps.applyResponse(tLObject);
            loadFromResponse(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$20(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$requestWebView$19(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$21(TLRPC.TL_error tL_error, TLObject tLObject) {
        WebViewRequestProps webViewRequestProps;
        if (tL_error == null && (webViewRequestProps = this.requestProps) != null) {
            webViewRequestProps.applyResponse(tLObject);
            loadFromResponse(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$22(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda18
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$requestWebView$21(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$23(TLRPC.TL_error tL_error, TLObject tLObject) {
        WebViewRequestProps webViewRequestProps;
        if (tL_error == null && (webViewRequestProps = this.requestProps) != null) {
            webViewRequestProps.applyResponse(tLObject);
            loadFromResponse(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$24(final TLObject tLObject, final TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda21
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$requestWebView$23(tL_error, tLObject);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setActionBarColor$31(int i, int i2, int i3, int i4, BotWebViewMenuContainer$ActionBarColorsAnimating botWebViewMenuContainer$ActionBarColorsAnimating, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        this.actionBarColor = ColorUtils.blendARGB(i, i2, floatValue);
        this.navBarColor = ColorUtils.blendARGB(i3, i4, floatValue);
        checkNavBarColor();
        this.windowView.invalidate();
        this.actionBar.setBackgroundColor(this.actionBarColor);
        botWebViewMenuContainer$ActionBarColorsAnimating.updateActionBar(this.actionBar, floatValue);
        this.lineColor = botWebViewMenuContainer$ActionBarColorsAnimating.getColor(Theme.key_sheet_scrollUp);
        this.windowView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setBackgroundColor$30(int i, int i2, ValueAnimator valueAnimator) {
        this.backgroundPaint.setColor(ColorUtils.blendARGB(i, i2, ((Float) valueAnimator.getAnimatedValue()).floatValue()));
        updateActionBarColors();
        this.windowView.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showJustAddedBulletin$0(String str) {
        BulletinFactory.of(this.windowView, this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, AndroidUtilities.replaceTags(str)).setDuration(5000).show(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$static$2(BotWebViewSheet botWebViewSheet, float f) {
        botWebViewSheet.actionBarTransitionProgress = f;
        botWebViewSheet.windowView.invalidate();
        botWebViewSheet.actionBar.setAlpha(f);
        botWebViewSheet.updateLightStatusBar();
    }

    private void loadFromResponse(boolean z) {
        if (this.requestProps == null) {
            return;
        }
        long max = Math.max(0L, 60000 - (System.currentTimeMillis() - this.requestProps.responseTime));
        String str = null;
        this.fullsize = null;
        TLObject tLObject = this.requestProps.response;
        if (tLObject instanceof TLRPC.TL_webViewResultUrl) {
            TLRPC.TL_webViewResultUrl tL_webViewResultUrl = (TLRPC.TL_webViewResultUrl) tLObject;
            this.queryId = tL_webViewResultUrl.query_id;
            str = tL_webViewResultUrl.url;
            this.fullsize = Boolean.valueOf(tL_webViewResultUrl.fullsize);
        } else if (tLObject instanceof TLRPC.TL_appWebViewResultUrl) {
            this.queryId = 0L;
            str = ((TLRPC.TL_appWebViewResultUrl) tLObject).url;
        } else if (tLObject instanceof TLRPC.TL_simpleWebViewResultUrl) {
            this.queryId = 0L;
            str = ((TLRPC.TL_simpleWebViewResultUrl) tLObject).url;
        }
        if (str != null && !z) {
            MediaDataController.getInstance(this.currentAccount).increaseWebappRating(this.requestProps.botId);
            this.webViewContainer.loadUrl(this.currentAccount, str);
        }
        AndroidUtilities.runOnUIThread(this.pollRunnable, max);
        ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = this.swipeContainer;
        if (webViewSwipeContainer != null) {
            webViewSwipeContainer.setFullSize(isFullSize());
        }
    }

    public static JSONObject makeThemeParams(Theme.ResourcesProvider resourcesProvider) {
        try {
            JSONObject jSONObject = new JSONObject();
            int color = Theme.getColor(Theme.key_dialogBackground, resourcesProvider);
            jSONObject.put("bg_color", color);
            jSONObject.put("section_bg_color", Theme.getColor(Theme.key_windowBackgroundWhite, resourcesProvider));
            int i = Theme.key_windowBackgroundGray;
            jSONObject.put("secondary_bg_color", Theme.getColor(i, resourcesProvider));
            jSONObject.put("text_color", Theme.getColor(Theme.key_windowBackgroundWhiteBlackText, resourcesProvider));
            jSONObject.put("hint_color", Theme.getColor(Theme.key_windowBackgroundWhiteHintText, resourcesProvider));
            jSONObject.put("link_color", Theme.getColor(Theme.key_windowBackgroundWhiteLinkText, resourcesProvider));
            jSONObject.put("button_color", Theme.getColor(Theme.key_featuredStickers_addButton, resourcesProvider));
            jSONObject.put("button_text_color", Theme.getColor(Theme.key_featuredStickers_buttonText, resourcesProvider));
            jSONObject.put("header_bg_color", Theme.getColor(Theme.key_actionBarDefault, resourcesProvider));
            jSONObject.put("accent_text_color", Theme.blendOver(color, Theme.getColor(Theme.key_windowBackgroundWhiteBlueText4, resourcesProvider)));
            jSONObject.put("section_header_text_color", Theme.blendOver(color, Theme.getColor(Theme.key_windowBackgroundWhiteBlueHeader, resourcesProvider)));
            jSONObject.put("subtitle_text_color", Theme.blendOver(color, Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2, resourcesProvider)));
            jSONObject.put("destructive_text_color", Theme.blendOver(color, Theme.getColor(Theme.key_text_RedRegular, resourcesProvider)));
            jSONObject.put("section_separator_color", Theme.blendOver(color, Theme.getColor(Theme.key_divider, resourcesProvider)));
            jSONObject.put("bottom_bar_bg_color", Theme.getColor(i, resourcesProvider));
            return jSONObject;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static int navigationBarColor(int i) {
        AndroidUtilities.computePerceivedBrightness(i);
        return Theme.adaptHSV(i, 0.35f, -0.1f);
    }

    private void preloadShortcutBotIcon(TLRPC.User user, TLRPC.TL_attachMenuBot tL_attachMenuBot) {
        if (tL_attachMenuBot == null || !tL_attachMenuBot.show_in_side_menu || MediaDataController.getInstance(this.currentAccount).isShortcutAdded(this.botId, MediaDataController.SHORTCUT_TYPE_ATTACHED_BOT)) {
            return;
        }
        if (user == null) {
            user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId));
        }
        if (user == null || user.photo == null || FileLoader.getInstance(this.currentAccount).getPathToAttach(user.photo.photo_small, true).exists()) {
            return;
        }
        MediaDataController.getInstance(this.currentAccount).preloadImage(ImageLocation.getForUser(user, 1), 0);
    }

    private void updateActionBarColors() {
        if (this.overrideBackgroundColor) {
            return;
        }
        ActionBar actionBar = this.actionBar;
        int i = Theme.key_windowBackgroundWhiteBlackText;
        actionBar.setTitleColor(getColor(i));
        this.actionBar.setItemsColor(getColor(i), false);
        this.actionBar.setItemsBackgroundColor(getColor(Theme.key_actionBarWhiteSelector), false);
        this.actionBar.setPopupBackgroundColor(getColor(Theme.key_actionBarDefaultSubmenuBackground), false);
        this.actionBar.setPopupItemsColor(getColor(Theme.key_actionBarDefaultSubmenuItem), false, false);
        this.actionBar.setPopupItemsColor(getColor(Theme.key_actionBarDefaultSubmenuItemIcon), true, false);
        this.actionBar.setPopupItemsSelectorColor(getColor(Theme.key_dialogButtonSelector), false);
    }

    private void updateLightStatusBar() {
        boolean z = true;
        if (this.overrideBackgroundColor) {
            z = true ^ this.actionBarIsLight;
        } else {
            int color = Theme.getColor(Theme.key_windowBackgroundWhite, null, true);
            if (AndroidUtilities.isTablet() || ColorUtils.calculateLuminance(color) < 0.7210000157356262d || this.actionBarTransitionProgress < 0.85f) {
                z = false;
            }
        }
        Boolean bool = this.wasLightStatusBar;
        if (bool == null || bool.booleanValue() != z) {
            this.wasLightStatusBar = Boolean.valueOf(z);
            if (Build.VERSION.SDK_INT >= 23) {
                int systemUiVisibility = this.windowView.getSystemUiVisibility();
                this.windowView.setSystemUiVisibility(z ? systemUiVisibility | LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM : systemUiVisibility & (-8193));
            }
        }
    }

    public void checkNavBarColor() {
        LaunchActivity launchActivity;
        if (this.dismissed || (launchActivity = LaunchActivity.instance) == null) {
            return;
        }
        launchActivity.checkSystemBarColors(true, true, true, false);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.webViewResultSent) {
            if (this.queryId == ((Long) objArr[0]).longValue()) {
                dismiss();
            }
        } else if (i == NotificationCenter.didSetNewTheme) {
            this.windowView.invalidate();
            this.webViewContainer.updateFlickerBackgroundColor(getColor(Theme.key_windowBackgroundWhite));
            updateActionBarColors();
            updateLightStatusBar();
        }
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        dismiss((Runnable) null);
    }

    public void dismiss(Runnable runnable) {
        dismiss(false, runnable);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheetTabsOverlay.Sheet
    public void dismiss(boolean z) {
        dismiss(z, null);
    }

    public void dismiss(boolean z, final Runnable runnable) {
        LaunchActivity launchActivity;
        if (this.dismissed) {
            return;
        }
        this.dismissed = true;
        AndroidUtilities.cancelRunOnUIThread(this.pollRunnable);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.webViewResultSent);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
        if (z && ((launchActivity = LaunchActivity.instance) == null || launchActivity.getBottomSheetTabsOverlay() == null)) {
            z = false;
        }
        if (!z) {
            this.webViewContainer.destroyWebView();
            ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = this.swipeContainer;
            webViewSwipeContainer.stickTo(webViewSwipeContainer.getHeight() + this.windowView.measureKeyboardHeight() + (isFullSize() ? AndroidUtilities.dp(200.0f) : 0), new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    BotWebViewSheet.this.lambda$dismiss$29(runnable);
                }
            });
            return;
        }
        SpringAnimation springAnimation = this.springAnimation;
        if (springAnimation != null) {
            springAnimation.getSpring().setFinalPosition(0.0f);
            this.springAnimation.start();
        }
        LaunchActivity.instance.getBottomSheetTabsOverlay().dismissSheet(this);
    }

    public long getBotId() {
        return this.botId;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheetTabsOverlay.Sheet
    public int getNavigationBarColor(int i) {
        return this.navBarColor;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheetTabsOverlay.Sheet
    public WindowView getWindowView() {
        return this.windowView;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheetTabsOverlay.Sheet
    public boolean isFullSize() {
        Boolean bool = this.fullsize;
        return bool == null ? this.defaultFullsize : bool.booleanValue();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.springAnimation == null) {
            this.springAnimation = new SpringAnimation(this, ACTION_BAR_TRANSITION_PROGRESS_VALUE).setSpring(new SpringForce().setStiffness(1200.0f).setDampingRatio(1.0f));
        }
    }

    @Override // android.app.Dialog
    public void onBackPressed() {
        if (this.passcodeView.getVisibility() == 0) {
            if (getOwnerActivity() != null) {
                getOwnerActivity().finish();
            }
        } else if (this.webViewContainer.onBackPressed()) {
        } else {
            dismiss(true, null);
        }
    }

    public boolean onCheckDismissByUser() {
        if (!this.needCloseConfirmation) {
            dismiss();
            return true;
        }
        TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId));
        AlertDialog create = new AlertDialog.Builder(getContext()).setTitle(user != null ? ContactsController.formatName(user.first_name, user.last_name) : null).setMessage(LocaleController.getString(R.string.BotWebViewChangesMayNotBeSaved)).setPositiveButton(LocaleController.getString(R.string.BotWebViewCloseAnyway), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda23
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                BotWebViewSheet.this.lambda$onCheckDismissByUser$28(dialogInterface, i);
            }
        }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).create();
        create.show();
        ((TextView) create.getButton(-1)).setTextColor(getColor(Theme.key_text_RedBold));
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x006a  */
    @Override // android.app.Dialog
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onCreate(Bundle bundle) {
        int i;
        super.onCreate(bundle);
        Window window = getWindow();
        int i2 = Build.VERSION.SDK_INT;
        if (i2 < 30) {
            i = i2 >= 21 ? -2147417856 : -2147417856;
            window.setWindowAnimations(R.style.DialogNoAnimation);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = -1;
            attributes.gravity = 51;
            attributes.dimAmount = 0.0f;
            attributes.flags &= -3;
            attributes.softInputMode = 16;
            attributes.height = -1;
            if (i2 >= 28) {
                attributes.layoutInDisplayCutoutMode = 1;
            }
            window.setAttributes(attributes);
            if (i2 >= 23) {
                window.setStatusBarColor(0);
            }
            this.windowView.setFitsSystemWindows(true);
            this.windowView.setSystemUiVisibility(1792);
            if (i2 >= 21) {
                this.windowView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda2
                    @Override // android.view.View.OnApplyWindowInsetsListener
                    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                        WindowInsets lambda$onCreate$14;
                        lambda$onCreate$14 = BotWebViewSheet.lambda$onCreate$14(view, windowInsets);
                        return lambda$onCreate$14;
                    }
                });
            }
            if (i2 >= 26) {
                AndroidUtilities.setLightNavigationBar(window, ColorUtils.calculateLuminance(this.navBarColor) >= 0.7210000157356262d);
            }
            NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
        }
        i = -2147483392;
        window.addFlags(i);
        window.setWindowAnimations(R.style.DialogNoAnimation);
        WindowManager.LayoutParams attributes2 = window.getAttributes();
        attributes2.width = -1;
        attributes2.gravity = 51;
        attributes2.dimAmount = 0.0f;
        attributes2.flags &= -3;
        attributes2.softInputMode = 16;
        attributes2.height = -1;
        if (i2 >= 28) {
        }
        window.setAttributes(attributes2);
        if (i2 >= 23) {
        }
        this.windowView.setFitsSystemWindows(true);
        this.windowView.setSystemUiVisibility(1792);
        if (i2 >= 21) {
        }
        if (i2 >= 26) {
        }
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SpringAnimation springAnimation = this.springAnimation;
        if (springAnimation != null) {
            springAnimation.cancel();
            this.springAnimation = null;
        }
    }

    @Override // android.app.Dialog
    protected void onStart() {
        super.onStart();
        Context context = getContext();
        if ((context instanceof ContextWrapper) && !(context instanceof LaunchActivity)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof LaunchActivity) {
            ((LaunchActivity) context).addOverlayPasscodeView(this.passcodeView);
        }
    }

    @Override // android.app.Dialog
    protected void onStop() {
        super.onStop();
        Context context = getContext();
        if ((context instanceof ContextWrapper) && !(context instanceof LaunchActivity)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (context instanceof LaunchActivity) {
            ((LaunchActivity) context).removeOverlayPasscodeView(this.passcodeView);
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheetTabsOverlay.Sheet
    public void release() {
        super.dismiss();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01f0  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01fb  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0268  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0273  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void requestWebView(BaseFragment baseFragment, WebViewRequestProps webViewRequestProps) {
        TLRPC.User user;
        TLRPC.TL_attachMenuBot tL_attachMenuBot;
        TLRPC.TL_messages_requestWebView tL_messages_requestWebView;
        ConnectionsManager connectionsManager;
        RequestDelegate requestDelegate;
        TLRPC.User user2;
        TLRPC.InputPeer inputPeer;
        RequestDelegate requestDelegate2;
        ConnectionsManager connectionsManager2;
        TLRPC.TL_messages_requestAppWebView tL_messages_requestAppWebView;
        TLRPC.User user3;
        TLRPC.InputPeer inputPeer2;
        this.requestProps = webViewRequestProps;
        int i = webViewRequestProps.currentAccount;
        this.currentAccount = i;
        this.peerId = webViewRequestProps.peerId;
        this.botId = webViewRequestProps.botId;
        this.replyToMsgId = webViewRequestProps.replyToMsgId;
        this.silent = webViewRequestProps.silent;
        this.buttonText = webViewRequestProps.buttonText;
        this.currentWebApp = webViewRequestProps.app;
        TLRPC.User user4 = MessagesController.getInstance(i).getUser(Long.valueOf(this.botId));
        CharSequence userName = UserObject.getUserName(user4);
        try {
            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(AndroidUtilities.dp(20.0f));
            userName = Emoji.replaceEmoji(userName, textPaint.getFontMetricsInt(), false);
        } catch (Exception unused) {
        }
        this.actionBar.setTitle(userName);
        TLRPC.UserFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(this.botId);
        if ((user4 != null && user4.verified) || (userFull != null && (user = userFull.user) != null && user.verified)) {
            Drawable mutate = getContext().getResources().getDrawable(R.drawable.verified_profile).mutate();
            this.verifiedDrawable = mutate;
            mutate.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_featuredStickers_addButton), PorterDuff.Mode.SRC_IN));
            this.actionBar.getTitleTextView().setDrawablePadding(AndroidUtilities.dp(2.0f));
            this.actionBar.getTitleTextView().setRightDrawable(new Drawable() { // from class: org.telegram.ui.bots.BotWebViewSheet.10
                @Override // android.graphics.drawable.Drawable
                public void draw(Canvas canvas) {
                    canvas.save();
                    canvas.translate(0.0f, AndroidUtilities.dp(1.0f));
                    BotWebViewSheet.this.verifiedDrawable.setBounds(getBounds());
                    BotWebViewSheet.this.verifiedDrawable.draw(canvas);
                    canvas.restore();
                }

                @Override // android.graphics.drawable.Drawable
                public int getIntrinsicHeight() {
                    return AndroidUtilities.dp(20.0f);
                }

                @Override // android.graphics.drawable.Drawable
                public int getIntrinsicWidth() {
                    return AndroidUtilities.dp(20.0f);
                }

                @Override // android.graphics.drawable.Drawable
                public int getOpacity() {
                    return -2;
                }

                @Override // android.graphics.drawable.Drawable
                public void setAlpha(int i2) {
                    BotWebViewSheet.this.verifiedDrawable.setAlpha(i2);
                }

                @Override // android.graphics.drawable.Drawable
                public void setColorFilter(ColorFilter colorFilter) {
                    BotWebViewSheet.this.verifiedDrawable.setColorFilter(colorFilter);
                }
            });
        }
        ActionBarMenu createMenu = this.actionBar.createMenu();
        createMenu.removeAllViews();
        Iterator<TLRPC.TL_attachMenuBot> it = MediaDataController.getInstance(this.currentAccount).getAttachMenuBots().bots.iterator();
        while (true) {
            if (!it.hasNext()) {
                tL_attachMenuBot = null;
                break;
            }
            tL_attachMenuBot = it.next();
            if (tL_attachMenuBot.bot_id == this.botId) {
                break;
            }
        }
        createMenu.addItem(R.id.menu_collapse_bot, R.drawable.arrow_more);
        ActionBarMenuItem addItem = createMenu.addItem(0, R.drawable.ic_ab_other);
        addItem.addSubItem(R.id.menu_open_bot, R.drawable.msg_bot, LocaleController.getString(R.string.BotWebViewOpenBot));
        ActionBarMenuSubItem addSubItem = addItem.addSubItem(R.id.menu_settings, R.drawable.msg_settings, LocaleController.getString(R.string.BotWebViewSettings));
        this.settingsItem = addSubItem;
        addSubItem.setVisibility(8);
        addItem.addSubItem(R.id.menu_reload_page, R.drawable.msg_retry, LocaleController.getString(R.string.BotWebViewReloadPage));
        if (tL_attachMenuBot != null && MediaDataController.getInstance(this.currentAccount).canCreateAttachedMenuBotShortcut(tL_attachMenuBot.bot_id)) {
            addItem.addSubItem(R.id.menu_add_to_home_screen_bot, R.drawable.msg_home, LocaleController.getString(R.string.AddShortcut));
        }
        addItem.addSubItem(R.id.menu_tos_bot, R.drawable.menu_intro, LocaleController.getString(R.string.BotWebViewToS));
        if (tL_attachMenuBot != null && (tL_attachMenuBot.show_in_side_menu || tL_attachMenuBot.show_in_attach_menu)) {
            addItem.addSubItem(R.id.menu_delete_bot, R.drawable.msg_delete, LocaleController.getString(R.string.BotWebViewDeleteBot));
        }
        this.actionBar.setActionBarMenuOnItemClick(new 11());
        JSONObject makeThemeParams = makeThemeParams(this.resourcesProvider);
        this.webViewContainer.setBotUser(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId)));
        this.webViewContainer.loadFlickerAndSettingsItem(this.currentAccount, this.botId, this.settingsItem);
        preloadShortcutBotIcon(webViewRequestProps.botUser, tL_attachMenuBot);
        if (webViewRequestProps.response != null) {
            loadFromResponse(true);
            return;
        }
        int i2 = webViewRequestProps.type;
        if (i2 == 0) {
            tL_messages_requestWebView = new TLRPC.TL_messages_requestWebView();
            tL_messages_requestWebView.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.peerId);
            tL_messages_requestWebView.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botId);
            tL_messages_requestWebView.platform = "android";
            tL_messages_requestWebView.compact = webViewRequestProps.compact;
            String str = webViewRequestProps.buttonUrl;
            if (str != null) {
                tL_messages_requestWebView.url = str;
                tL_messages_requestWebView.flags |= 2;
            }
            if (this.replyToMsgId != 0) {
                tL_messages_requestWebView.reply_to = SendMessagesHelper.getInstance(this.currentAccount).createReplyInput(this.replyToMsgId);
                tL_messages_requestWebView.flags |= 1;
            }
            if (makeThemeParams != null) {
                TLRPC.TL_dataJSON tL_dataJSON = new TLRPC.TL_dataJSON();
                tL_messages_requestWebView.theme_params = tL_dataJSON;
                tL_dataJSON.data = makeThemeParams.toString();
                tL_messages_requestWebView.flags |= 4;
            }
            connectionsManager = ConnectionsManager.getInstance(this.currentAccount);
            requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda7
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    BotWebViewSheet.this.lambda$requestWebView$20(tLObject, tL_error);
                }
            };
        } else if (i2 == 1) {
            TLRPC.TL_messages_requestSimpleWebView tL_messages_requestSimpleWebView = new TLRPC.TL_messages_requestSimpleWebView();
            tL_messages_requestSimpleWebView.from_switch_webview = (webViewRequestProps.flags & 1) != 0;
            tL_messages_requestSimpleWebView.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botId);
            tL_messages_requestSimpleWebView.platform = "android";
            tL_messages_requestSimpleWebView.from_side_menu = (webViewRequestProps.flags & 2) != 0;
            tL_messages_requestSimpleWebView.compact = webViewRequestProps.compact;
            if (makeThemeParams != null) {
                TLRPC.TL_dataJSON tL_dataJSON2 = new TLRPC.TL_dataJSON();
                tL_messages_requestSimpleWebView.theme_params = tL_dataJSON2;
                tL_dataJSON2.data = makeThemeParams.toString();
                tL_messages_requestSimpleWebView.flags |= 1;
            }
            if (!TextUtils.isEmpty(webViewRequestProps.buttonUrl)) {
                tL_messages_requestSimpleWebView.flags |= 8;
                tL_messages_requestSimpleWebView.url = webViewRequestProps.buttonUrl;
            }
            if (!TextUtils.isEmpty(webViewRequestProps.startParam)) {
                tL_messages_requestSimpleWebView.start_param = webViewRequestProps.startParam;
                tL_messages_requestSimpleWebView.flags |= 16;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_requestSimpleWebView, new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda6
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    BotWebViewSheet.this.lambda$requestWebView$18(tLObject, tL_error);
                }
            });
            return;
        } else if (i2 != 2) {
            if (i2 == 3) {
                TLRPC.TL_messages_requestAppWebView tL_messages_requestAppWebView2 = new TLRPC.TL_messages_requestAppWebView();
                TLRPC.TL_inputBotAppID tL_inputBotAppID = new TLRPC.TL_inputBotAppID();
                TLRPC.BotApp botApp = webViewRequestProps.app;
                tL_inputBotAppID.id = botApp.id;
                tL_inputBotAppID.access_hash = botApp.access_hash;
                tL_messages_requestAppWebView2.app = tL_inputBotAppID;
                tL_messages_requestAppWebView2.write_allowed = webViewRequestProps.allowWrite;
                tL_messages_requestAppWebView2.platform = "android";
                if (baseFragment instanceof ChatActivity) {
                    ChatActivity chatActivity = (ChatActivity) baseFragment;
                    if (chatActivity.getCurrentUser() != null) {
                        user2 = chatActivity.getCurrentUser();
                    } else {
                        inputPeer = MessagesController.getInputPeer(chatActivity.getCurrentChat());
                        tL_messages_requestAppWebView2.peer = inputPeer;
                        tL_messages_requestAppWebView2.compact = webViewRequestProps.compact;
                        if (!TextUtils.isEmpty(webViewRequestProps.startParam)) {
                            tL_messages_requestAppWebView2.start_param = webViewRequestProps.startParam;
                            tL_messages_requestAppWebView2.flags |= 2;
                        }
                        if (makeThemeParams != null) {
                            TLRPC.TL_dataJSON tL_dataJSON3 = new TLRPC.TL_dataJSON();
                            tL_messages_requestAppWebView2.theme_params = tL_dataJSON3;
                            tL_dataJSON3.data = makeThemeParams.toString();
                            tL_messages_requestAppWebView2.flags |= 4;
                        }
                        ConnectionsManager connectionsManager3 = ConnectionsManager.getInstance(this.currentAccount);
                        requestDelegate2 = new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda4
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                BotWebViewSheet.this.lambda$requestWebView$22(tLObject, tL_error);
                            }
                        };
                        tL_messages_requestAppWebView = tL_messages_requestAppWebView2;
                        connectionsManager2 = connectionsManager3;
                    }
                } else {
                    user2 = webViewRequestProps.botUser;
                }
                inputPeer = MessagesController.getInputPeer(user2);
                tL_messages_requestAppWebView2.peer = inputPeer;
                tL_messages_requestAppWebView2.compact = webViewRequestProps.compact;
                if (!TextUtils.isEmpty(webViewRequestProps.startParam)) {
                }
                if (makeThemeParams != null) {
                }
                ConnectionsManager connectionsManager32 = ConnectionsManager.getInstance(this.currentAccount);
                requestDelegate2 = new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda4
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        BotWebViewSheet.this.lambda$requestWebView$22(tLObject, tL_error);
                    }
                };
                tL_messages_requestAppWebView = tL_messages_requestAppWebView2;
                connectionsManager2 = connectionsManager32;
            } else if (i2 != 4) {
                return;
            } else {
                TLRPC.TL_messages_requestMainWebView tL_messages_requestMainWebView = new TLRPC.TL_messages_requestMainWebView();
                tL_messages_requestMainWebView.bot = MessagesController.getInstance(this.currentAccount).getInputUser(webViewRequestProps.botId);
                tL_messages_requestMainWebView.platform = "android";
                if (baseFragment instanceof ChatActivity) {
                    ChatActivity chatActivity2 = (ChatActivity) baseFragment;
                    if (chatActivity2.getCurrentUser() != null) {
                        user3 = chatActivity2.getCurrentUser();
                    } else {
                        inputPeer2 = MessagesController.getInputPeer(chatActivity2.getCurrentChat());
                        tL_messages_requestMainWebView.peer = inputPeer2;
                        tL_messages_requestMainWebView.compact = webViewRequestProps.compact;
                        if (!TextUtils.isEmpty(webViewRequestProps.startParam)) {
                            tL_messages_requestMainWebView.start_param = webViewRequestProps.startParam;
                            tL_messages_requestMainWebView.flags |= 2;
                        }
                        if (makeThemeParams != null) {
                            TLRPC.TL_dataJSON tL_dataJSON4 = new TLRPC.TL_dataJSON();
                            tL_messages_requestMainWebView.theme_params = tL_dataJSON4;
                            tL_dataJSON4.data = makeThemeParams.toString();
                            tL_messages_requestMainWebView.flags |= 1;
                        }
                        ConnectionsManager connectionsManager4 = ConnectionsManager.getInstance(this.currentAccount);
                        requestDelegate2 = new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda3
                            @Override // org.telegram.tgnet.RequestDelegate
                            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                                BotWebViewSheet.this.lambda$requestWebView$24(tLObject, tL_error);
                            }
                        };
                        tL_messages_requestAppWebView = tL_messages_requestMainWebView;
                        connectionsManager2 = connectionsManager4;
                    }
                } else {
                    user3 = webViewRequestProps.botUser;
                }
                inputPeer2 = MessagesController.getInputPeer(user3);
                tL_messages_requestMainWebView.peer = inputPeer2;
                tL_messages_requestMainWebView.compact = webViewRequestProps.compact;
                if (!TextUtils.isEmpty(webViewRequestProps.startParam)) {
                }
                if (makeThemeParams != null) {
                }
                ConnectionsManager connectionsManager42 = ConnectionsManager.getInstance(this.currentAccount);
                requestDelegate2 = new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda3
                    @Override // org.telegram.tgnet.RequestDelegate
                    public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                        BotWebViewSheet.this.lambda$requestWebView$24(tLObject, tL_error);
                    }
                };
                tL_messages_requestAppWebView = tL_messages_requestMainWebView;
                connectionsManager2 = connectionsManager42;
            }
            connectionsManager2.sendRequest(tL_messages_requestAppWebView, requestDelegate2, 66);
            return;
        } else {
            tL_messages_requestWebView = new TLRPC.TL_messages_requestWebView();
            tL_messages_requestWebView.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botId);
            tL_messages_requestWebView.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.botId);
            tL_messages_requestWebView.platform = "android";
            tL_messages_requestWebView.compact = webViewRequestProps.compact;
            tL_messages_requestWebView.url = webViewRequestProps.buttonUrl;
            tL_messages_requestWebView.flags |= 2;
            if (makeThemeParams != null) {
                TLRPC.TL_dataJSON tL_dataJSON5 = new TLRPC.TL_dataJSON();
                tL_messages_requestWebView.theme_params = tL_dataJSON5;
                tL_dataJSON5.data = makeThemeParams.toString();
                tL_messages_requestWebView.flags |= 4;
            }
            connectionsManager = ConnectionsManager.getInstance(this.currentAccount);
            requestDelegate = new RequestDelegate() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda5
                @Override // org.telegram.tgnet.RequestDelegate
                public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                    BotWebViewSheet.this.lambda$requestWebView$16(tLObject, tL_error);
                }
            };
        }
        connectionsManager.sendRequest(tL_messages_requestWebView, requestDelegate);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.webViewResultSent);
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x011d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean restoreState(BaseFragment baseFragment, BottomSheetTabs.WebTabData webTabData) {
        int i;
        ActionBarMenuSubItem actionBarMenuSubItem;
        if (webTabData == null || webTabData.props == null) {
            return false;
        }
        if (webTabData.overrideActionBarColor) {
            setBackgroundColor(webTabData.backgroundColor, false);
        }
        if (webTabData.overrideActionBarColor) {
            i = webTabData.actionBarColor;
        } else {
            int i2 = webTabData.actionBarColorKey;
            if (i2 < 0) {
                i2 = Theme.key_windowBackgroundWhite;
            }
            i = Theme.getColor(i2, this.resourcesProvider);
        }
        setActionBarColor(i, webTabData.overrideActionBarColor, false);
        this.showExpanded = webTabData.expanded;
        this.showOffsetY = webTabData.expandedOffset;
        BotWebViewContainer botWebViewContainer = this.webViewContainer;
        boolean z = webTabData.backButton;
        this.backButtonShown = z;
        botWebViewContainer.setIsBackButtonVisible(z);
        this.swipeContainer.setAllowSwipes(webTabData.allowSwipes);
        AndroidUtilities.updateImageViewImageAnimated(this.actionBar.getBackButton(), this.backButtonShown ? R.drawable.ic_ab_back : R.drawable.ic_close_white);
        this.needCloseConfirmation = webTabData.confirmDismiss;
        this.fullsize = Boolean.valueOf(webTabData.fullsize);
        this.needsContext = webTabData.needsContext;
        BotWebViewAttachedSheet.MainButtonSettings mainButtonSettings = webTabData.main;
        if (mainButtonSettings != null) {
            setMainButton(mainButtonSettings);
        }
        BotWebViewContainer.MyWebView myWebView = webTabData.webView;
        if (myWebView != null) {
            myWebView.onResume();
            this.webViewContainer.replaceWebView(webTabData.webView, webTabData.proxy);
            this.webViewContainer.setState(webTabData.ready || webTabData.webView.isPageLoaded(), webTabData.lastUrl);
            if (Theme.isCurrentThemeDark() != webTabData.themeIsDark) {
                if (this.webViewContainer.getWebView() != null) {
                    this.webViewContainer.getWebView().animate().cancel();
                    this.webViewContainer.getWebView().animate().alpha(0.0f).start();
                }
                this.progressView.setLoadProgress(0.0f);
                this.progressView.setAlpha(1.0f);
                this.progressView.setVisibility(0);
                this.webViewContainer.setBotUser(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId)));
                this.webViewContainer.loadFlickerAndSettingsItem(this.currentAccount, this.botId, this.settingsItem);
                this.webViewContainer.setState(false, null);
                if (this.webViewContainer.getWebView() != null) {
                    this.webViewContainer.getWebView().loadUrl("about:blank");
                }
            }
            requestWebView(baseFragment, webTabData.props);
            actionBarMenuSubItem = this.settingsItem;
            if (actionBarMenuSubItem != null) {
                actionBarMenuSubItem.setVisibility(webTabData.settings ? 0 : 8);
            }
            return true;
        }
        WebViewRequestProps webViewRequestProps = webTabData.props;
        webViewRequestProps.response = null;
        webViewRequestProps.responseTime = 0L;
        requestWebView(baseFragment, webTabData.props);
        actionBarMenuSubItem = this.settingsItem;
        if (actionBarMenuSubItem != null) {
        }
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheetTabsOverlay.Sheet
    public BottomSheetTabs.WebTabData saveState() {
        BottomSheetTabs.WebTabData webTabData = new BottomSheetTabs.WebTabData();
        webTabData.actionBarColor = this.actionBarColor;
        webTabData.actionBarColorKey = this.actionBarColorKey;
        webTabData.overrideActionBarColor = this.overrideBackgroundColor;
        webTabData.backgroundColor = this.backgroundPaint.getColor();
        webTabData.props = this.requestProps;
        BotWebViewContainer botWebViewContainer = this.webViewContainer;
        boolean z = false;
        webTabData.ready = botWebViewContainer != null && botWebViewContainer.isPageLoaded();
        webTabData.themeIsDark = Theme.isCurrentThemeDark();
        BotWebViewContainer botWebViewContainer2 = this.webViewContainer;
        webTabData.lastUrl = botWebViewContainer2 != null ? botWebViewContainer2.getUrlLoaded() : null;
        ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = this.swipeContainer;
        webTabData.expanded = (webViewSwipeContainer != null && webViewSwipeContainer.getSwipeOffsetY() < 0.0f) || this.forceExpnaded || isFullSize();
        webTabData.fullsize = isFullSize();
        ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer2 = this.swipeContainer;
        webTabData.expandedOffset = webViewSwipeContainer2 != null ? webViewSwipeContainer2.getOffsetY() : Float.MAX_VALUE;
        webTabData.needsContext = this.needsContext;
        webTabData.backButton = this.backButtonShown;
        webTabData.main = this.mainButtonSettings;
        webTabData.confirmDismiss = this.needCloseConfirmation;
        ActionBarMenuSubItem actionBarMenuSubItem = this.settingsItem;
        webTabData.settings = actionBarMenuSubItem != null && actionBarMenuSubItem.getVisibility() == 0;
        ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer3 = this.swipeContainer;
        webTabData.allowSwipes = (webViewSwipeContainer3 == null || webViewSwipeContainer3.isAllowedSwipes()) ? true : true;
        BotWebViewContainer botWebViewContainer3 = this.webViewContainer;
        BotWebViewContainer.MyWebView webView = botWebViewContainer3 == null ? null : botWebViewContainer3.getWebView();
        if (webView != null) {
            this.webViewContainer.preserveWebView();
            webTabData.webView = webView;
            BotWebViewContainer botWebViewContainer4 = this.webViewContainer;
            webTabData.proxy = botWebViewContainer4 != null ? botWebViewContainer4.getBotProxy() : null;
            webTabData.viewWidth = webView.getWidth();
            webTabData.viewHeight = webView.getHeight();
            webView.onPause();
        }
        return webTabData;
    }

    public void setActionBarColor(final int i, boolean z, boolean z2) {
        final int i2 = this.actionBarColor;
        final int i3 = this.navBarColor;
        final int navigationBarColor = navigationBarColor(i);
        final BotWebViewMenuContainer$ActionBarColorsAnimating botWebViewMenuContainer$ActionBarColorsAnimating = new BotWebViewMenuContainer$ActionBarColorsAnimating();
        botWebViewMenuContainer$ActionBarColorsAnimating.setFrom(this.overrideBackgroundColor ? this.actionBarColor : 0, this.resourcesProvider);
        this.overrideBackgroundColor = z;
        this.actionBarIsLight = ColorUtils.calculateLuminance(i) < 0.7210000157356262d;
        botWebViewMenuContainer$ActionBarColorsAnimating.setTo(this.overrideBackgroundColor ? i : 0, this.resourcesProvider);
        if (z2) {
            ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda29
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BotWebViewSheet.this.lambda$setActionBarColor$31(i2, i, i3, navigationBarColor, botWebViewMenuContainer$ActionBarColorsAnimating, valueAnimator);
                }
            });
            duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.bots.BotWebViewSheet.13
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    BotWebViewSheet.this.actionBarColor = ColorUtils.blendARGB(i2, i, 1.0f);
                    BotWebViewSheet.this.navBarColor = ColorUtils.blendARGB(i3, navigationBarColor, 1.0f);
                    BotWebViewSheet.this.checkNavBarColor();
                    BotWebViewSheet.this.windowView.invalidate();
                    BotWebViewSheet.this.actionBar.setBackgroundColor(BotWebViewSheet.this.actionBarColor);
                    botWebViewMenuContainer$ActionBarColorsAnimating.updateActionBar(BotWebViewSheet.this.actionBar, 1.0f);
                    BotWebViewSheet.this.lineColor = botWebViewMenuContainer$ActionBarColorsAnimating.getColor(Theme.key_sheet_scrollUp);
                    BotWebViewSheet.this.windowView.invalidate();
                }
            });
            duration.start();
        } else {
            this.actionBarColor = i;
            this.navBarColor = navigationBarColor;
            checkNavBarColor();
            this.windowView.invalidate();
            this.actionBar.setBackgroundColor(this.actionBarColor);
            botWebViewMenuContainer$ActionBarColorsAnimating.updateActionBar(this.actionBar, 1.0f);
            this.lineColor = botWebViewMenuContainer$ActionBarColorsAnimating.getColor(Theme.key_sheet_scrollUp);
            this.windowView.invalidate();
        }
        updateLightStatusBar();
    }

    public void setBackgroundColor(final int i, boolean z) {
        final int color = this.backgroundPaint.getColor();
        if (!z) {
            this.backgroundPaint.setColor(i);
            updateActionBarColors();
            this.windowView.invalidate();
            return;
        }
        ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(200L);
        duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda27
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                BotWebViewSheet.this.lambda$setBackgroundColor$30(color, i, valueAnimator);
            }
        });
        duration.start();
    }

    public void setDefaultFullsize(boolean z) {
        if (this.defaultFullsize != z) {
            this.defaultFullsize = z;
            ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer webViewSwipeContainer = this.swipeContainer;
            if (webViewSwipeContainer != null) {
                webViewSwipeContainer.setFullSize(isFullSize());
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheetTabsOverlay.Sheet
    public boolean setDialog(BottomSheetTabDialog bottomSheetTabDialog) {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheetTabsOverlay.Sheet
    public /* synthetic */ void setLastVisible(boolean z) {
        BottomSheetTabsOverlay.Sheet.-CC.$default$setLastVisible(this, z);
    }

    public void setMainButton(final BotWebViewAttachedSheet.MainButtonSettings mainButtonSettings) {
        this.mainButtonSettings = mainButtonSettings;
        this.mainButton.setClickable(mainButtonSettings.isActive);
        this.mainButton.setText(mainButtonSettings.text);
        this.mainButton.setTextColor(mainButtonSettings.textColor);
        this.mainButton.setBackground(BotWebViewContainer.getMainButtonRippleDrawable(mainButtonSettings.color));
        boolean z = mainButtonSettings.isVisible;
        if (z != this.mainButtonWasVisible) {
            this.mainButtonWasVisible = z;
            this.mainButton.animate().cancel();
            if (mainButtonSettings.isVisible) {
                this.mainButton.setAlpha(0.0f);
                this.mainButton.setVisibility(0);
            }
            this.mainButton.animate().alpha(mainButtonSettings.isVisible ? 1.0f : 0.0f).setDuration(150L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.bots.BotWebViewSheet.14
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (!mainButtonSettings.isVisible) {
                        BotWebViewSheet.this.mainButton.setVisibility(8);
                    }
                    BotWebViewSheet.this.swipeContainer.requestLayout();
                }
            }).start();
        }
        this.radialProgressView.setProgressColor(mainButtonSettings.textColor);
        boolean z2 = mainButtonSettings.isProgressVisible;
        if (z2 != this.mainButtonProgressWasVisible) {
            this.mainButtonProgressWasVisible = z2;
            this.radialProgressView.animate().cancel();
            if (mainButtonSettings.isProgressVisible) {
                this.radialProgressView.setAlpha(0.0f);
                this.radialProgressView.setVisibility(0);
            }
            this.radialProgressView.animate().alpha(mainButtonSettings.isProgressVisible ? 1.0f : 0.0f).scaleX(mainButtonSettings.isProgressVisible ? 1.0f : 0.1f).scaleY(mainButtonSettings.isProgressVisible ? 1.0f : 0.1f).setDuration(250L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.bots.BotWebViewSheet.15
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (mainButtonSettings.isProgressVisible) {
                        return;
                    }
                    BotWebViewSheet.this.radialProgressView.setVisibility(8);
                }
            }).start();
        }
    }

    public void setNeedsContext(boolean z) {
        this.needsContext = z;
    }

    public void setParentActivity(Activity activity) {
        this.parentActivity = activity;
    }

    public void setWasOpenedByLinkIntent(boolean z) {
        BotWebViewContainer botWebViewContainer = this.webViewContainer;
        if (botWebViewContainer != null) {
            botWebViewContainer.setWasOpenedByLinkIntent(z);
        }
    }

    @Override // android.app.Dialog
    public void show() {
        if (AndroidUtilities.isSafeToShow(getContext())) {
            this.windowView.setAlpha(0.0f);
            this.windowView.addOnLayoutChangeListener(new 12());
            super.show();
        }
    }

    public void showJustAddedBulletin() {
        TLRPC.TL_attachMenuBot tL_attachMenuBot;
        TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId));
        Iterator<TLRPC.TL_attachMenuBot> it = MediaDataController.getInstance(this.currentAccount).getAttachMenuBots().bots.iterator();
        while (true) {
            if (!it.hasNext()) {
                tL_attachMenuBot = null;
                break;
            }
            tL_attachMenuBot = it.next();
            if (tL_attachMenuBot.bot_id == this.botId) {
                break;
            }
        }
        if (tL_attachMenuBot == null) {
            return;
        }
        boolean z = tL_attachMenuBot.show_in_side_menu;
        final String formatString = (z && tL_attachMenuBot.show_in_attach_menu) ? LocaleController.formatString("BotAttachMenuShortcatAddedAttachAndSide", R.string.BotAttachMenuShortcatAddedAttachAndSide, user.first_name) : z ? LocaleController.formatString("BotAttachMenuShortcatAddedSide", R.string.BotAttachMenuShortcatAddedSide, user.first_name) : LocaleController.formatString("BotAttachMenuShortcatAddedAttach", R.string.BotAttachMenuShortcatAddedAttach, user.first_name);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.BotWebViewSheet$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                BotWebViewSheet.this.lambda$showJustAddedBulletin$0(formatString);
            }
        }, 200L);
    }
}
