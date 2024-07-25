package org.telegram.ui.bots;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.math.MathUtils;
import androidx.core.util.Consumer;
import androidx.core.view.GestureDetectorCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.ChatListItemAnimator;
import java.util.Iterator;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.GenericProvider;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$TL_attachMenuBot;
import org.telegram.tgnet.TLRPC$TL_dataJSON;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_prolongWebView;
import org.telegram.tgnet.TLRPC$TL_messages_requestWebView;
import org.telegram.tgnet.TLRPC$TL_webViewResultUrl;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SimpleFloatPropertyCompat;
import org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout;
import org.telegram.ui.web.BotWebViewContainer;
/* loaded from: classes4.dex */
public class ChatAttachAlertBotWebViewLayout extends ChatAttachAlert.AttachAlertLayout implements NotificationCenter.NotificationCenterDelegate {
    private ActionBarMenuSubItem addToHomeScreenItem;
    private long botId;
    private int currentAccount;
    private int customActionBarBackground;
    private int customBackground;
    private boolean destroyed;
    private boolean hasCustomActionBarBackground;
    private boolean hasCustomBackground;
    private boolean ignoreLayout;
    private boolean ignoreMeasure;
    private boolean isBotButtonAvailable;
    private int measureOffsetY;
    private boolean needCloseConfirmation;
    private boolean needReload;
    private ActionBarMenuItem otherItem;
    private long peerId;
    private Runnable pollRunnable;
    private WebProgressView progressView;
    private long queryId;
    private int replyToMsgId;
    public ActionBarMenuSubItem settingsItem;
    private boolean silent;
    private String startCommand;
    private WebViewSwipeContainer swipeContainer;
    private BotWebViewContainer webViewContainer;
    private ValueAnimator webViewScrollAnimator;

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int needsActionBar() {
        return 1;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean shouldHideBottomButtons() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2() {
        TLRPC$ChatFull chatFull;
        TLRPC$Peer tLRPC$Peer;
        if (this.destroyed) {
            return;
        }
        TLRPC$TL_messages_prolongWebView tLRPC$TL_messages_prolongWebView = new TLRPC$TL_messages_prolongWebView();
        tLRPC$TL_messages_prolongWebView.bot = MessagesController.getInstance(this.currentAccount).getInputUser(this.botId);
        tLRPC$TL_messages_prolongWebView.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(this.peerId);
        tLRPC$TL_messages_prolongWebView.query_id = this.queryId;
        tLRPC$TL_messages_prolongWebView.silent = this.silent;
        if (this.replyToMsgId != 0) {
            tLRPC$TL_messages_prolongWebView.reply_to = SendMessagesHelper.getInstance(this.currentAccount).createReplyInput(this.replyToMsgId);
            tLRPC$TL_messages_prolongWebView.flags |= 1;
        }
        if (this.peerId < 0 && (chatFull = MessagesController.getInstance(this.currentAccount).getChatFull(-this.peerId)) != null && (tLRPC$Peer = chatFull.default_send_as) != null) {
            tLRPC$TL_messages_prolongWebView.send_as = MessagesController.getInstance(this.currentAccount).getInputPeer(tLRPC$Peer);
            tLRPC$TL_messages_prolongWebView.flags |= LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tLRPC$TL_messages_prolongWebView, new RequestDelegate() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda13
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                ChatAttachAlertBotWebViewLayout.this.lambda$new$1(tLObject, tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(TLObject tLObject, final TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlertBotWebViewLayout.this.lambda$new$0(tLRPC$TL_error);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(TLRPC$TL_error tLRPC$TL_error) {
        if (this.destroyed) {
            return;
        }
        if (tLRPC$TL_error != null) {
            this.parentAlert.dismiss();
        } else {
            AndroidUtilities.runOnUIThread(this.pollRunnable, 60000L);
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onMenuItemClick(int i) {
        if (i == -1) {
            if (this.webViewContainer.onBackPressed()) {
                return;
            }
            onCheckDismissByUser();
        } else if (i == R.id.menu_open_bot) {
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", this.botId);
            this.parentAlert.baseFragment.presentFragment(new ChatActivity(bundle));
            this.parentAlert.dismiss();
        } else if (i == R.id.menu_reload_page) {
            if (this.webViewContainer.getWebView() != null) {
                this.webViewContainer.getWebView().animate().cancel();
                this.webViewContainer.getWebView().animate().alpha(0.0f).start();
            }
            this.progressView.setLoadProgress(0.0f);
            this.progressView.setAlpha(1.0f);
            this.progressView.setVisibility(0);
            this.webViewContainer.setBotUser(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId)));
            this.webViewContainer.loadFlickerAndSettingsItem(this.currentAccount, this.botId, this.settingsItem);
            this.webViewContainer.reload();
        } else if (i == R.id.menu_delete_bot) {
            Iterator<TLRPC$TL_attachMenuBot> it = MediaDataController.getInstance(this.currentAccount).getAttachMenuBots().bots.iterator();
            while (it.hasNext()) {
                TLRPC$TL_attachMenuBot next = it.next();
                if (next.bot_id == this.botId) {
                    this.parentAlert.onLongClickBotButton(next, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId)));
                    return;
                }
            }
        } else if (i == R.id.menu_settings) {
            this.webViewContainer.onSettingsButtonPressed();
        } else if (i == R.id.menu_add_to_home_screen_bot) {
            MediaDataController.getInstance(this.currentAccount).installShortcut(this.botId, MediaDataController.SHORTCUT_TYPE_ATTACHED_BOT);
        } else if (i == R.id.menu_tos_bot) {
            Browser.openUrl(getContext(), LocaleController.getString(R.string.BotWebViewToSLink));
        }
    }

    public ChatAttachAlertBotWebViewLayout(ChatAttachAlert chatAttachAlert, Context context, Theme.ResourcesProvider resourcesProvider) {
        super(chatAttachAlert, context, resourcesProvider);
        this.pollRunnable = new Runnable() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlertBotWebViewLayout.this.lambda$new$2();
            }
        };
        ActionBarMenuItem addItem = this.parentAlert.actionBar.createMenu().addItem(0, R.drawable.ic_ab_other);
        this.otherItem = addItem;
        addItem.addSubItem(R.id.menu_open_bot, R.drawable.msg_bot, LocaleController.getString(R.string.BotWebViewOpenBot));
        ActionBarMenuSubItem addSubItem = this.otherItem.addSubItem(R.id.menu_settings, R.drawable.msg_settings, LocaleController.getString(R.string.BotWebViewSettings));
        this.settingsItem = addSubItem;
        addSubItem.setVisibility(8);
        this.otherItem.addSubItem(R.id.menu_reload_page, R.drawable.msg_retry, LocaleController.getString(R.string.BotWebViewReloadPage));
        ActionBarMenuSubItem addSubItem2 = this.otherItem.addSubItem(R.id.menu_add_to_home_screen_bot, R.drawable.msg_home, LocaleController.getString(R.string.AddShortcut));
        this.addToHomeScreenItem = addSubItem2;
        addSubItem2.setVisibility(8);
        this.otherItem.addSubItem(R.id.menu_tos_bot, R.drawable.menu_intro, LocaleController.getString(R.string.BotWebViewToS));
        this.otherItem.addSubItem(R.id.menu_delete_bot, R.drawable.msg_delete, LocaleController.getString(R.string.BotWebViewDeleteBot));
        this.webViewContainer = new BotWebViewContainer(context, resourcesProvider, getThemedColor(Theme.key_dialogBackground), true) { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout.1
            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0 && !ChatAttachAlertBotWebViewLayout.this.isBotButtonAvailable) {
                    ChatAttachAlertBotWebViewLayout.this.isBotButtonAvailable = true;
                    ChatAttachAlertBotWebViewLayout.this.webViewContainer.restoreButtonData();
                }
                return super.dispatchTouchEvent(motionEvent);
            }

            @Override // org.telegram.ui.web.BotWebViewContainer
            public void onWebViewCreated() {
                ChatAttachAlertBotWebViewLayout.this.swipeContainer.setWebView(ChatAttachAlertBotWebViewLayout.this.webViewContainer.getWebView());
            }
        };
        WebViewSwipeContainer webViewSwipeContainer = new WebViewSwipeContainer(context) { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout.2
            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i, int i2) {
                super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(((View.MeasureSpec.getSize(i2) - ActionBar.getCurrentActionBarHeight()) - AndroidUtilities.dp(84.0f)) + ChatAttachAlertBotWebViewLayout.this.measureOffsetY, 1073741824));
            }
        };
        this.swipeContainer = webViewSwipeContainer;
        webViewSwipeContainer.addView(this.webViewContainer, LayoutHelper.createFrame(-1, -1.0f));
        this.swipeContainer.setScrollListener(new Runnable() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlertBotWebViewLayout.this.lambda$new$3();
            }
        });
        this.swipeContainer.setScrollEndListener(new Runnable() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlertBotWebViewLayout.this.lambda$new$4();
            }
        });
        this.swipeContainer.setDelegate(new WebViewSwipeContainer.Delegate() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda15
            @Override // org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.Delegate
            public final void onDismiss() {
                ChatAttachAlertBotWebViewLayout.this.lambda$new$5();
            }
        });
        this.swipeContainer.setIsKeyboardVisible(new GenericProvider() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda12
            @Override // org.telegram.messenger.GenericProvider
            public final Object provide(Object obj) {
                Boolean lambda$new$6;
                lambda$new$6 = ChatAttachAlertBotWebViewLayout.this.lambda$new$6((Void) obj);
                return lambda$new$6;
            }
        });
        addView(this.swipeContainer, LayoutHelper.createFrame(-1, -1.0f));
        WebProgressView webProgressView = new WebProgressView(context, resourcesProvider);
        this.progressView = webProgressView;
        addView(webProgressView, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 84.0f));
        this.webViewContainer.setWebViewProgressListener(new Consumer() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda3
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                ChatAttachAlertBotWebViewLayout.this.lambda$new$8((Float) obj);
            }
        });
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3() {
        this.parentAlert.updateLayout(this, true, 0);
        this.webViewContainer.invalidateViewPortHeight();
        System.currentTimeMillis();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4() {
        this.webViewContainer.invalidateViewPortHeight(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5() {
        if (onCheckDismissByUser()) {
            return;
        }
        this.swipeContainer.stickTo(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$new$6(Void r2) {
        return Boolean.valueOf(this.parentAlert.sizeNotifierFrameLayout.getKeyboardHeight() >= AndroidUtilities.dp(20.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$8(Float f) {
        this.progressView.setLoadProgressAnimated(f.floatValue());
        if (f.floatValue() == 1.0f) {
            ValueAnimator duration = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ChatAttachAlertBotWebViewLayout.this.lambda$new$7(valueAnimator);
                }
            });
            duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    ChatAttachAlertBotWebViewLayout.this.progressView.setVisibility(8);
                }
            });
            duration.start();
            requestEnableKeyboard();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$7(ValueAnimator valueAnimator) {
        this.progressView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public void setNeedCloseConfirmation(boolean z) {
        this.needCloseConfirmation = z;
    }

    public void setAllowSwipes(boolean z) {
        this.swipeContainer.setAllowSwipes(z);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean onDismissWithTouchOutside() {
        onCheckDismissByUser();
        return false;
    }

    public boolean onCheckDismissByUser() {
        if (this.needCloseConfirmation) {
            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId));
            AlertDialog create = new AlertDialog.Builder(getContext()).setTitle(user != null ? ContactsController.formatName(user.first_name, user.last_name) : null).setMessage(LocaleController.getString(R.string.BotWebViewChangesMayNotBeSaved)).setPositiveButton(LocaleController.getString(R.string.BotWebViewCloseAnyway), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ChatAttachAlertBotWebViewLayout.this.lambda$onCheckDismissByUser$9(dialogInterface, i);
                }
            }).setNegativeButton(LocaleController.getString(R.string.Cancel), null).create();
            create.show();
            ((TextView) create.getButton(-1)).setTextColor(getThemedColor(Theme.key_text_RedBold));
            return false;
        }
        this.parentAlert.dismiss();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCheckDismissByUser$9(DialogInterface dialogInterface, int i) {
        this.parentAlert.dismiss();
    }

    public void setCustomBackground(int i) {
        this.customBackground = i;
        this.hasCustomBackground = true;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean hasCustomBackground() {
        return this.hasCustomBackground;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCustomBackground() {
        return this.customBackground;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean hasCustomActionBarBackground() {
        return this.hasCustomActionBarBackground;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCustomActionBarBackground() {
        return this.customActionBarBackground;
    }

    public void setCustomActionBarBackground(int i) {
        this.hasCustomActionBarBackground = true;
        this.customActionBarBackground = i;
    }

    public boolean canExpandByRequest() {
        return !this.swipeContainer.isSwipeInProgress();
    }

    public void setMeasureOffsetY(int i) {
        this.measureOffsetY = i;
        this.swipeContainer.requestLayout();
    }

    public void disallowSwipeOffsetAnimation() {
        this.swipeContainer.setSwipeOffsetAnimationDisallowed(true);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.ignoreMeasure) {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        } else {
            super.onMeasure(i, i2);
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onPanTransitionStart(boolean z, int i) {
        boolean z2;
        if (z) {
            this.webViewContainer.setViewPortByMeasureSuppressed(true);
            float topActionBarOffsetY = (-this.swipeContainer.getOffsetY()) + this.swipeContainer.getTopActionBarOffsetY();
            if (this.swipeContainer.getSwipeOffsetY() != topActionBarOffsetY) {
                this.swipeContainer.stickTo(topActionBarOffsetY);
                z2 = true;
            } else {
                z2 = false;
            }
            int measureKeyboardHeight = this.parentAlert.sizeNotifierFrameLayout.measureKeyboardHeight() + i;
            setMeasuredDimension(getMeasuredWidth(), i);
            this.ignoreMeasure = true;
            this.swipeContainer.setSwipeOffsetAnimationDisallowed(true);
            if (z2) {
                return;
            }
            ValueAnimator valueAnimator = this.webViewScrollAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.webViewScrollAnimator = null;
            }
            if (this.webViewContainer.getWebView() != null) {
                int scrollY = this.webViewContainer.getWebView().getScrollY();
                final int i2 = (measureKeyboardHeight - i) + scrollY;
                ValueAnimator duration = ValueAnimator.ofInt(scrollY, i2).setDuration(250L);
                this.webViewScrollAnimator = duration;
                duration.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
                this.webViewScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        ChatAttachAlertBotWebViewLayout.this.lambda$onPanTransitionStart$10(valueAnimator2);
                    }
                });
                this.webViewScrollAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout.4
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (ChatAttachAlertBotWebViewLayout.this.webViewContainer.getWebView() != null) {
                            ChatAttachAlertBotWebViewLayout.this.webViewContainer.getWebView().setScrollY(i2);
                        }
                        if (animator == ChatAttachAlertBotWebViewLayout.this.webViewScrollAnimator) {
                            ChatAttachAlertBotWebViewLayout.this.webViewScrollAnimator = null;
                        }
                    }
                });
                this.webViewScrollAnimator.start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPanTransitionStart$10(ValueAnimator valueAnimator) {
        int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        if (this.webViewContainer.getWebView() != null) {
            this.webViewContainer.getWebView().setScrollY(intValue);
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onPanTransitionEnd() {
        this.ignoreMeasure = false;
        this.swipeContainer.setSwipeOffsetAnimationDisallowed(false);
        this.webViewContainer.setViewPortByMeasureSuppressed(false);
        requestLayout();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onShow(ChatAttachAlert.AttachAlertLayout attachAlertLayout) {
        CharSequence userName = UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId)));
        try {
            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(AndroidUtilities.dp(20.0f));
            userName = Emoji.replaceEmoji(userName, textPaint.getFontMetricsInt(), false);
        } catch (Exception unused) {
        }
        this.parentAlert.actionBar.setTitle(userName);
        this.swipeContainer.setSwipeOffsetY(0.0f);
        if (this.webViewContainer.getWebView() != null) {
            this.webViewContainer.getWebView().scrollTo(0, 0);
        }
        if (this.parentAlert.getBaseFragment() != null) {
            this.webViewContainer.setParentActivity(this.parentAlert.getBaseFragment().getParentActivity());
        }
        this.otherItem.setVisibility(0);
        if (this.webViewContainer.isBackButtonVisible()) {
            return;
        }
        AndroidUtilities.updateImageViewImageAnimated(this.parentAlert.actionBar.getBackButton(), R.drawable.ic_close_white);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onShown() {
        if (this.webViewContainer.isPageLoaded()) {
            requestEnableKeyboard();
        }
        this.swipeContainer.setSwipeOffsetAnimationDisallowed(false);
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlertBotWebViewLayout.this.lambda$onShown$11();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onShown$11() {
        this.webViewContainer.restoreButtonData();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestEnableKeyboard() {
        BaseFragment baseFragment = this.parentAlert.getBaseFragment();
        if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).contentView.measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
            AndroidUtilities.hideKeyboard(this.parentAlert.baseFragment.getFragmentView());
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertBotWebViewLayout.this.requestEnableKeyboard();
                }
            }, 250L);
            return;
        }
        this.parentAlert.getWindow().setSoftInputMode(20);
        setFocusable(true);
        this.parentAlert.setFocusable(true);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onHidden() {
        super.onHidden();
        this.parentAlert.setFocusable(false);
        this.parentAlert.getWindow().setSoftInputMode(48);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getCurrentItemTop() {
        return (int) (this.swipeContainer.getSwipeOffsetY() + this.swipeContainer.getOffsetY());
    }

    @Override // android.view.View
    public void setTranslationY(float f) {
        super.setTranslationY(f);
        this.parentAlert.getSheetContainer().invalidate();
    }

    public String getStartCommand() {
        return this.startCommand;
    }

    public void requestWebView(final int i, long j, long j2, boolean z, int i2, String str) {
        TLRPC$ChatFull chatFull;
        TLRPC$Peer tLRPC$Peer;
        this.currentAccount = i;
        this.peerId = j;
        this.botId = j2;
        this.silent = z;
        this.replyToMsgId = i2;
        this.startCommand = str;
        if (this.addToHomeScreenItem != null) {
            if (MediaDataController.getInstance(i).canCreateAttachedMenuBotShortcut(j2)) {
                this.addToHomeScreenItem.setVisibility(0);
            } else {
                this.addToHomeScreenItem.setVisibility(8);
            }
        }
        this.webViewContainer.setBotUser(MessagesController.getInstance(i).getUser(Long.valueOf(j2)));
        this.webViewContainer.loadFlickerAndSettingsItem(i, j2, this.settingsItem);
        TLRPC$TL_messages_requestWebView tLRPC$TL_messages_requestWebView = new TLRPC$TL_messages_requestWebView();
        tLRPC$TL_messages_requestWebView.peer = MessagesController.getInstance(i).getInputPeer(j);
        tLRPC$TL_messages_requestWebView.bot = MessagesController.getInstance(i).getInputUser(j2);
        tLRPC$TL_messages_requestWebView.silent = z;
        tLRPC$TL_messages_requestWebView.platform = "android";
        if (j < 0 && (chatFull = MessagesController.getInstance(i).getChatFull(-j)) != null && (tLRPC$Peer = chatFull.default_send_as) != null) {
            tLRPC$TL_messages_requestWebView.send_as = MessagesController.getInstance(i).getInputPeer(tLRPC$Peer);
            tLRPC$TL_messages_requestWebView.flags |= LiteMode.FLAG_ANIMATED_EMOJI_REACTIONS_NOT_PREMIUM;
        }
        if (str != null) {
            tLRPC$TL_messages_requestWebView.start_param = str;
            tLRPC$TL_messages_requestWebView.flags |= 8;
        }
        if (i2 != 0) {
            tLRPC$TL_messages_requestWebView.reply_to = SendMessagesHelper.getInstance(i).createReplyInput(i2);
            tLRPC$TL_messages_requestWebView.flags |= 1;
        }
        JSONObject makeThemeParams = BotWebViewSheet.makeThemeParams(this.resourcesProvider);
        if (makeThemeParams != null) {
            TLRPC$TL_dataJSON tLRPC$TL_dataJSON = new TLRPC$TL_dataJSON();
            tLRPC$TL_messages_requestWebView.theme_params = tLRPC$TL_dataJSON;
            tLRPC$TL_dataJSON.data = makeThemeParams.toString();
            tLRPC$TL_messages_requestWebView.flags |= 4;
        }
        ConnectionsManager.getInstance(i).sendRequest(tLRPC$TL_messages_requestWebView, new RequestDelegate() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda14
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
                ChatAttachAlertBotWebViewLayout.this.lambda$requestWebView$13(i, tLObject, tLRPC$TL_error);
            }
        });
        NotificationCenter.getInstance(i).addObserver(this, NotificationCenter.webViewResultSent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$13(final int i, final TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlertBotWebViewLayout.this.lambda$requestWebView$12(tLObject, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestWebView$12(TLObject tLObject, int i) {
        if (tLObject instanceof TLRPC$TL_webViewResultUrl) {
            TLRPC$TL_webViewResultUrl tLRPC$TL_webViewResultUrl = (TLRPC$TL_webViewResultUrl) tLObject;
            this.queryId = tLRPC$TL_webViewResultUrl.query_id;
            this.webViewContainer.loadUrl(i, tLRPC$TL_webViewResultUrl.url);
            AndroidUtilities.runOnUIThread(this.pollRunnable);
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onDestroy() {
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.webViewResultSent);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
        ActionBarMenu createMenu = this.parentAlert.actionBar.createMenu();
        this.otherItem.removeAllSubItems();
        createMenu.removeView(this.otherItem);
        this.webViewContainer.destroyWebView();
        this.destroyed = true;
        AndroidUtilities.cancelRunOnUIThread(this.pollRunnable);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void onHide() {
        super.onHide();
        this.otherItem.setVisibility(8);
        this.isBotButtonAvailable = false;
        if (!this.webViewContainer.isBackButtonVisible()) {
            AndroidUtilities.updateImageViewImageAnimated(this.parentAlert.actionBar.getBackButton(), R.drawable.ic_ab_back);
        }
        this.parentAlert.actionBar.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundWhite));
        if (this.webViewContainer.hasUserPermissions()) {
            this.webViewContainer.destroyWebView();
            this.needReload = true;
        }
    }

    public boolean needReload() {
        if (this.needReload) {
            this.needReload = false;
            return true;
        }
        return false;
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getListTopPadding() {
        return (int) this.swipeContainer.getOffsetY();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getFirstOffset() {
        return getListTopPadding() + AndroidUtilities.dp(56.0f);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x002d  */
    /* JADX WARN: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onPreMeasure(int i, int i2) {
        int i3;
        float f;
        if (!AndroidUtilities.isTablet()) {
            Point point = AndroidUtilities.displaySize;
            if (point.x > point.y) {
                i3 = (int) (i2 / 3.5f);
                this.parentAlert.setAllowNestedScroll(true);
                if (i3 < 0) {
                    i3 = 0;
                }
                f = i3;
                if (this.swipeContainer.getOffsetY() == f) {
                    this.ignoreLayout = true;
                    this.swipeContainer.setOffsetY(f);
                    this.ignoreLayout = false;
                    return;
                }
                return;
            }
        }
        i3 = (i2 / 5) * 2;
        this.parentAlert.setAllowNestedScroll(true);
        if (i3 < 0) {
        }
        f = i3;
        if (this.swipeContainer.getOffsetY() == f) {
        }
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public int getButtonsHideOffset() {
        return ((int) this.swipeContainer.getTopActionBarOffsetY()) + AndroidUtilities.dp(12.0f);
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public boolean onBackPressed() {
        if (this.webViewContainer.onBackPressed()) {
            return true;
        }
        onCheckDismissByUser();
        return true;
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.ignoreLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override // org.telegram.ui.Components.ChatAttachAlert.AttachAlertLayout
    public void scrollToTop() {
        WebViewSwipeContainer webViewSwipeContainer = this.swipeContainer;
        webViewSwipeContainer.stickTo((-webViewSwipeContainer.getOffsetY()) + this.swipeContainer.getTopActionBarOffsetY());
    }

    public BotWebViewContainer getWebViewContainer() {
        return this.webViewContainer;
    }

    public void setDelegate(BotWebViewContainer.Delegate delegate) {
        this.webViewContainer.setDelegate(delegate);
    }

    public boolean isBotButtonAvailable() {
        return this.isBotButtonAvailable;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.webViewResultSent) {
            if (this.queryId == ((Long) objArr[0]).longValue()) {
                this.webViewContainer.destroyWebView();
                this.needReload = true;
                this.parentAlert.dismiss();
            }
        } else if (i == NotificationCenter.didSetNewTheme) {
            this.webViewContainer.updateFlickerBackgroundColor(getThemedColor(Theme.key_dialogBackground));
        }
    }

    public void showJustAddedBulletin() {
        TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot;
        final String formatString;
        TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.botId));
        Iterator<TLRPC$TL_attachMenuBot> it = MediaDataController.getInstance(this.currentAccount).getAttachMenuBots().bots.iterator();
        while (true) {
            if (!it.hasNext()) {
                tLRPC$TL_attachMenuBot = null;
                break;
            }
            tLRPC$TL_attachMenuBot = it.next();
            if (tLRPC$TL_attachMenuBot.bot_id == this.botId) {
                break;
            }
        }
        if (tLRPC$TL_attachMenuBot == null) {
            return;
        }
        boolean z = tLRPC$TL_attachMenuBot.show_in_side_menu;
        if (z && tLRPC$TL_attachMenuBot.show_in_attach_menu) {
            formatString = LocaleController.formatString("BotAttachMenuShortcatAddedAttachAndSide", R.string.BotAttachMenuShortcatAddedAttachAndSide, user.first_name);
        } else if (z) {
            formatString = LocaleController.formatString("BotAttachMenuShortcatAddedSide", R.string.BotAttachMenuShortcatAddedSide, user.first_name);
        } else {
            formatString = LocaleController.formatString("BotAttachMenuShortcatAddedAttach", R.string.BotAttachMenuShortcatAddedAttach, user.first_name);
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlertBotWebViewLayout.this.lambda$showJustAddedBulletin$14(formatString);
            }
        }, 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showJustAddedBulletin$14(String str) {
        BulletinFactory.of(this.parentAlert.getContainer(), this.resourcesProvider).createSimpleBulletin(R.raw.contact_check, AndroidUtilities.replaceTags(str)).setDuration(5000).show(true);
    }

    /* loaded from: classes4.dex */
    public static class WebViewSwipeContainer extends FrameLayout {
        public static final SimpleFloatPropertyCompat<WebViewSwipeContainer> SWIPE_OFFSET_Y = new SimpleFloatPropertyCompat<>("swipeOffsetY", new SimpleFloatPropertyCompat.Getter() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda5
            @Override // org.telegram.ui.Components.SimpleFloatPropertyCompat.Getter
            public final float get(Object obj) {
                return ((ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) obj).getSwipeOffsetY();
            }
        }, new SimpleFloatPropertyCompat.Setter() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda6
            @Override // org.telegram.ui.Components.SimpleFloatPropertyCompat.Setter
            public final void set(Object obj, float f) {
                ((ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer) obj).setSwipeOffsetY(f);
            }
        });
        private boolean allowFullSizeSwipe;
        private boolean allowSwipes;
        public boolean allowedScrollX;
        public boolean allowedScrollY;
        private Delegate delegate;
        private float drawnSwipeOffsetY;
        private boolean flingInProgress;
        private boolean fullsize;
        private final GestureDetectorCompat gestureDetector;
        private GenericProvider<Void, Boolean> isKeyboardVisible;
        public boolean isScrolling;
        private boolean isSwipeDisallowed;
        private boolean isSwipeOffsetAnimationDisallowed;
        public float offsetY;
        private SpringAnimation offsetYAnimator;
        public boolean opened;
        private float pendingOffsetY;
        private float pendingSwipeOffsetY;
        private SpringAnimation scrollAnimator;
        private Runnable scrollEndListener;
        private Runnable scrollListener;
        public boolean shouldWaitWebViewScroll;
        public boolean stickToEdges;
        private float swipeOffsetY;
        private int swipeStickyRange;
        public float topActionBarOffsetY;
        private BotWebViewContainer.MyWebView webView;

        /* loaded from: classes4.dex */
        public interface Delegate {
            void onDismiss();
        }

        static /* synthetic */ float access$1124(WebViewSwipeContainer webViewSwipeContainer, float f) {
            float f2 = webViewSwipeContainer.swipeOffsetY - f;
            webViewSwipeContainer.swipeOffsetY = f2;
            return f2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Boolean lambda$new$0(Void r0) {
            return Boolean.FALSE;
        }

        public void setFullSize(boolean z) {
            if (this.fullsize != z) {
                this.fullsize = z;
                if (z) {
                    if (this.opened) {
                        stickTo((-getOffsetY()) + getTopActionBarOffsetY());
                        return;
                    }
                    return;
                }
                stickTo(0.0f);
            }
        }

        public void setAllowFullSizeSwipe(boolean z) {
            this.allowFullSizeSwipe = z;
        }

        public void setAllowSwipes(boolean z) {
            if (this.allowSwipes != z) {
                this.allowSwipes = z;
            }
        }

        public boolean isAllowedSwipes() {
            return this.allowSwipes;
        }

        public void setShouldWaitWebViewScroll(boolean z) {
            this.shouldWaitWebViewScroll = z;
        }

        public void allowThisScroll(boolean z, boolean z2) {
            this.allowedScrollX = z;
            this.allowedScrollY = z2;
        }

        public WebViewSwipeContainer(Context context) {
            super(context);
            this.topActionBarOffsetY = ActionBar.getCurrentActionBarHeight();
            this.offsetY = 0.0f;
            this.pendingOffsetY = -1.0f;
            this.pendingSwipeOffsetY = -2.14748365E9f;
            this.isKeyboardVisible = new GenericProvider() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda4
                @Override // org.telegram.messenger.GenericProvider
                public final Object provide(Object obj) {
                    Boolean lambda$new$0;
                    lambda$new$0 = ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.lambda$new$0((Void) obj);
                    return lambda$new$0;
                }
            };
            this.allowSwipes = true;
            this.stickToEdges = true;
            final int scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            this.gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.1
                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
                public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                    if (WebViewSwipeContainer.this.isSwipeDisallowed || !WebViewSwipeContainer.this.allowSwipes) {
                        return false;
                    }
                    if (!WebViewSwipeContainer.this.fullsize || WebViewSwipeContainer.this.allowFullSizeSwipe) {
                        WebViewSwipeContainer webViewSwipeContainer = WebViewSwipeContainer.this;
                        if (!webViewSwipeContainer.shouldWaitWebViewScroll || webViewSwipeContainer.allowedScrollY) {
                            if (f2 < 700.0f || !(webViewSwipeContainer.webView == null || WebViewSwipeContainer.this.webView.getScrollY() == 0)) {
                                if (f2 <= -700.0f) {
                                    float f3 = WebViewSwipeContainer.this.swipeOffsetY;
                                    WebViewSwipeContainer webViewSwipeContainer2 = WebViewSwipeContainer.this;
                                    if (f3 > (-webViewSwipeContainer2.offsetY) + webViewSwipeContainer2.topActionBarOffsetY) {
                                        webViewSwipeContainer2.flingInProgress = true;
                                        WebViewSwipeContainer webViewSwipeContainer3 = WebViewSwipeContainer.this;
                                        webViewSwipeContainer3.stickTo((-webViewSwipeContainer3.offsetY) + webViewSwipeContainer3.topActionBarOffsetY);
                                    }
                                }
                                return true;
                            }
                            WebViewSwipeContainer.this.flingInProgress = true;
                            if (WebViewSwipeContainer.this.swipeOffsetY >= WebViewSwipeContainer.this.swipeStickyRange || WebViewSwipeContainer.this.fullsize) {
                                if (WebViewSwipeContainer.this.fullsize && WebViewSwipeContainer.this.allowFullSizeSwipe) {
                                    float f4 = WebViewSwipeContainer.this.drawnSwipeOffsetY;
                                    WebViewSwipeContainer webViewSwipeContainer4 = WebViewSwipeContainer.this;
                                    float f5 = webViewSwipeContainer4.offsetY;
                                    float f6 = webViewSwipeContainer4.topActionBarOffsetY;
                                    if (f4 == (-f5) + f6) {
                                        webViewSwipeContainer4.stickTo((-f5) + f6);
                                    }
                                }
                                if (WebViewSwipeContainer.this.delegate != null) {
                                    WebViewSwipeContainer.this.delegate.onDismiss();
                                }
                            } else {
                                WebViewSwipeContainer.this.stickTo(0.0f);
                            }
                            return true;
                        }
                        return false;
                    }
                    return false;
                }

                /* JADX WARN: Code restructure failed: missing block: B:13:0x002e, code lost:
                    if (r12.allowedScrollY == false) goto L51;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:44:0x00df, code lost:
                    if (r10.this$0.webView.canScrollHorizontally(r13 >= 0.0f ? 1 : -1) == false) goto L47;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:48:0x00f8, code lost:
                    if ((java.lang.Math.abs(r13) * 1.5f) >= java.lang.Math.abs(r14)) goto L45;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:49:0x00fa, code lost:
                    r10.this$0.isSwipeDisallowed = true;
                 */
                @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                    WebViewSwipeContainer webViewSwipeContainer = WebViewSwipeContainer.this;
                    if (!webViewSwipeContainer.isScrolling && !webViewSwipeContainer.isSwipeDisallowed && WebViewSwipeContainer.this.allowSwipes) {
                        WebViewSwipeContainer webViewSwipeContainer2 = WebViewSwipeContainer.this;
                        if (webViewSwipeContainer2.shouldWaitWebViewScroll) {
                            float f3 = webViewSwipeContainer2.swipeOffsetY;
                            WebViewSwipeContainer webViewSwipeContainer3 = WebViewSwipeContainer.this;
                            if (f3 == (-webViewSwipeContainer3.offsetY) + webViewSwipeContainer3.topActionBarOffsetY) {
                            }
                        }
                        if (((Boolean) WebViewSwipeContainer.this.isKeyboardVisible.provide(null)).booleanValue()) {
                            float f4 = WebViewSwipeContainer.this.swipeOffsetY;
                            WebViewSwipeContainer webViewSwipeContainer4 = WebViewSwipeContainer.this;
                            if (f4 == (-webViewSwipeContainer4.offsetY) + webViewSwipeContainer4.topActionBarOffsetY) {
                                webViewSwipeContainer4.isSwipeDisallowed = true;
                            }
                        }
                        if (Math.abs(f2) >= scaledTouchSlop && Math.abs(f2) * 1.5f >= Math.abs(f)) {
                            float f5 = WebViewSwipeContainer.this.swipeOffsetY;
                            WebViewSwipeContainer webViewSwipeContainer5 = WebViewSwipeContainer.this;
                            if (f5 != (-webViewSwipeContainer5.offsetY) + webViewSwipeContainer5.topActionBarOffsetY || webViewSwipeContainer5.webView == null || (f2 < 0.0f && WebViewSwipeContainer.this.webView.getScrollY() == 0)) {
                                WebViewSwipeContainer.this.isScrolling = true;
                                MotionEvent obtain = MotionEvent.obtain(0L, 0L, 3, 0.0f, 0.0f, 0);
                                for (int i = 0; i < WebViewSwipeContainer.this.getChildCount(); i++) {
                                    WebViewSwipeContainer.this.getChildAt(i).dispatchTouchEvent(obtain);
                                }
                                obtain.recycle();
                                return true;
                            }
                        }
                        if (WebViewSwipeContainer.this.webView != null) {
                        }
                        if (Math.abs(f) >= scaledTouchSlop) {
                        }
                    }
                    WebViewSwipeContainer webViewSwipeContainer6 = WebViewSwipeContainer.this;
                    if (webViewSwipeContainer6.isScrolling) {
                        if (f2 < 0.0f) {
                            float f6 = webViewSwipeContainer6.swipeOffsetY;
                            WebViewSwipeContainer webViewSwipeContainer7 = WebViewSwipeContainer.this;
                            if (f6 <= (-webViewSwipeContainer7.offsetY) + webViewSwipeContainer7.topActionBarOffsetY) {
                                if (webViewSwipeContainer7.webView != null) {
                                    float scrollY = WebViewSwipeContainer.this.webView.getScrollY() + f2;
                                    WebViewSwipeContainer.this.webView.setScrollY((int) MathUtils.clamp(scrollY, 0.0f, Math.max(WebViewSwipeContainer.this.webView.getContentHeight(), WebViewSwipeContainer.this.webView.getHeight()) - WebViewSwipeContainer.this.topActionBarOffsetY));
                                    if (scrollY < 0.0f) {
                                        WebViewSwipeContainer.access$1124(WebViewSwipeContainer.this, scrollY);
                                    }
                                } else {
                                    WebViewSwipeContainer.access$1124(WebViewSwipeContainer.this, f2);
                                }
                            } else {
                                WebViewSwipeContainer.access$1124(webViewSwipeContainer7, f2);
                            }
                        } else {
                            WebViewSwipeContainer.access$1124(webViewSwipeContainer6, f2);
                            if (WebViewSwipeContainer.this.webView != null) {
                                float f7 = WebViewSwipeContainer.this.swipeOffsetY;
                                WebViewSwipeContainer webViewSwipeContainer8 = WebViewSwipeContainer.this;
                                if (f7 < (-webViewSwipeContainer8.offsetY) + webViewSwipeContainer8.topActionBarOffsetY) {
                                    float f8 = WebViewSwipeContainer.this.swipeOffsetY;
                                    WebViewSwipeContainer webViewSwipeContainer9 = WebViewSwipeContainer.this;
                                    webViewSwipeContainer9.webView.setScrollY((int) MathUtils.clamp(webViewSwipeContainer8.webView.getScrollY() - ((f8 + webViewSwipeContainer9.offsetY) - webViewSwipeContainer9.topActionBarOffsetY), 0.0f, Math.max(WebViewSwipeContainer.this.webView.getContentHeight(), WebViewSwipeContainer.this.webView.getHeight()) - WebViewSwipeContainer.this.topActionBarOffsetY));
                                }
                            }
                        }
                        WebViewSwipeContainer webViewSwipeContainer10 = WebViewSwipeContainer.this;
                        float f9 = webViewSwipeContainer10.swipeOffsetY;
                        WebViewSwipeContainer webViewSwipeContainer11 = WebViewSwipeContainer.this;
                        float f10 = (-webViewSwipeContainer11.offsetY) + webViewSwipeContainer11.topActionBarOffsetY;
                        WebViewSwipeContainer webViewSwipeContainer12 = WebViewSwipeContainer.this;
                        webViewSwipeContainer10.swipeOffsetY = MathUtils.clamp(f9, f10, (webViewSwipeContainer11.getHeight() - webViewSwipeContainer12.offsetY) + webViewSwipeContainer12.topActionBarOffsetY);
                        if (WebViewSwipeContainer.this.fullsize && !WebViewSwipeContainer.this.allowFullSizeSwipe) {
                            WebViewSwipeContainer webViewSwipeContainer13 = WebViewSwipeContainer.this;
                            float f11 = webViewSwipeContainer13.swipeOffsetY;
                            WebViewSwipeContainer webViewSwipeContainer14 = WebViewSwipeContainer.this;
                            webViewSwipeContainer13.swipeOffsetY = Math.min(f11, (-webViewSwipeContainer14.offsetY) + webViewSwipeContainer14.topActionBarOffsetY);
                        }
                        WebViewSwipeContainer.this.invalidateTranslation();
                    }
                    return true;
                }
            });
            updateStickyRange();
        }

        public void setIsKeyboardVisible(GenericProvider<Void, Boolean> genericProvider) {
            this.isKeyboardVisible = genericProvider;
        }

        @Override // android.view.View
        protected void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            updateStickyRange();
        }

        private void updateStickyRange() {
            Point point = AndroidUtilities.displaySize;
            this.swipeStickyRange = AndroidUtilities.dp(point.x > point.y ? 8.0f : 64.0f);
        }

        @Override // android.view.ViewGroup, android.view.ViewParent
        public void requestDisallowInterceptTouchEvent(boolean z) {
            super.requestDisallowInterceptTouchEvent(z);
            if (z) {
                this.isSwipeDisallowed = true;
                this.isScrolling = false;
            }
        }

        public void setSwipeOffsetAnimationDisallowed(boolean z) {
            this.isSwipeOffsetAnimationDisallowed = z;
        }

        public void setScrollListener(Runnable runnable) {
            this.scrollListener = runnable;
        }

        public void setScrollEndListener(Runnable runnable) {
            this.scrollEndListener = runnable;
        }

        public void setWebView(BotWebViewContainer.MyWebView myWebView) {
            this.webView = myWebView;
        }

        public void setTopActionBarOffsetY(float f) {
            this.topActionBarOffsetY = f;
            invalidateTranslation();
        }

        public void setSwipeOffsetY(float f) {
            this.swipeOffsetY = f;
            invalidateTranslation();
        }

        public void setOffsetY(final float f) {
            if (this.pendingSwipeOffsetY != -2.14748365E9f) {
                this.pendingOffsetY = f;
                return;
            }
            SpringAnimation springAnimation = this.offsetYAnimator;
            if (springAnimation != null) {
                springAnimation.cancel();
            }
            final float f2 = this.offsetY;
            final float f3 = f - f2;
            final boolean z = Math.abs((this.swipeOffsetY + f2) - this.topActionBarOffsetY) <= ((float) AndroidUtilities.dp(1.0f));
            if (!this.isSwipeOffsetAnimationDisallowed) {
                SpringAnimation springAnimation2 = this.offsetYAnimator;
                if (springAnimation2 != null) {
                    springAnimation2.cancel();
                }
                SpringAnimation addEndListener = new SpringAnimation(new FloatValueHolder(f2)).setSpring(new SpringForce(f).setStiffness(1400.0f).setDampingRatio(1.0f)).addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda2
                    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
                    public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f4, float f5) {
                        ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.this.lambda$setOffsetY$1(f3, f2, z, f, dynamicAnimation, f4, f5);
                    }
                }).addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda0
                    @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                    public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z2, float f4, float f5) {
                        ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.this.lambda$setOffsetY$2(f, dynamicAnimation, z2, f4, f5);
                    }
                });
                this.offsetYAnimator = addEndListener;
                addEndListener.start();
                return;
            }
            this.offsetY = f;
            if (z) {
                this.swipeOffsetY = MathUtils.clamp(this.swipeOffsetY - Math.max(0.0f, f3), (-this.offsetY) + this.topActionBarOffsetY, (getHeight() - this.offsetY) + this.topActionBarOffsetY);
            }
            invalidateTranslation();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setOffsetY$1(float f, float f2, boolean z, float f3, DynamicAnimation dynamicAnimation, float f4, float f5) {
            this.offsetY = f4;
            float f6 = f == 0.0f ? 1.0f : (f4 - f2) / f;
            if (z) {
                this.swipeOffsetY = MathUtils.clamp(this.swipeOffsetY - (f6 * Math.max(0.0f, f)), (-this.offsetY) + this.topActionBarOffsetY, (getHeight() - this.offsetY) + this.topActionBarOffsetY);
            }
            SpringAnimation springAnimation = this.scrollAnimator;
            if (springAnimation != null && springAnimation.getSpring().getFinalPosition() == (-f2) + this.topActionBarOffsetY) {
                this.scrollAnimator.getSpring().setFinalPosition((-f3) + this.topActionBarOffsetY);
            }
            invalidateTranslation();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$setOffsetY$2(float f, DynamicAnimation dynamicAnimation, boolean z, float f2, float f3) {
            this.offsetYAnimator = null;
            if (!z) {
                this.offsetY = f;
                invalidateTranslation();
                return;
            }
            this.pendingOffsetY = f;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateDrawn() {
            this.drawnSwipeOffsetY = this.swipeOffsetY;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void invalidateTranslation() {
            setTranslationY(Math.max(this.topActionBarOffsetY, this.offsetY + this.swipeOffsetY));
            AndroidUtilities.cancelRunOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.this.updateDrawn();
                }
            });
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.this.updateDrawn();
                }
            });
            Runnable runnable = this.scrollListener;
            if (runnable != null) {
                runnable.run();
            }
            if (Bulletin.getVisibleBulletin() != null) {
                Bulletin.getVisibleBulletin().updatePosition();
            }
        }

        public float getTopActionBarOffsetY() {
            return this.topActionBarOffsetY;
        }

        public float getOffsetY() {
            return this.offsetY;
        }

        public float getSwipeOffsetY() {
            return this.swipeOffsetY;
        }

        public void setDelegate(Delegate delegate) {
            this.delegate = delegate;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            if (!this.isScrolling || motionEvent.getActionIndex() == 0) {
                if (motionEvent.getAction() == 0 && this.shouldWaitWebViewScroll) {
                    this.allowedScrollX = false;
                    this.allowedScrollY = false;
                }
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                int actionIndex = motionEvent.getActionIndex();
                if (Build.VERSION.SDK_INT >= 29) {
                    obtain.setLocation(motionEvent.getRawX(actionIndex), motionEvent.getRawY(actionIndex));
                } else {
                    obtain.setLocation(motionEvent.getX(actionIndex) + (motionEvent.getRawX() - motionEvent.getX()), motionEvent.getY(actionIndex) + (motionEvent.getRawY() - motionEvent.getY()));
                }
                boolean onTouchEvent = this.gestureDetector.onTouchEvent(obtain);
                obtain.recycle();
                if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    this.isSwipeDisallowed = false;
                    this.isScrolling = false;
                    boolean z = this.fullsize;
                    if (!z || this.allowFullSizeSwipe) {
                        if (this.flingInProgress) {
                            this.flingInProgress = false;
                        } else if (this.allowSwipes && (!this.shouldWaitWebViewScroll || this.swipeOffsetY != (-this.offsetY) + this.topActionBarOffsetY || this.allowedScrollY)) {
                            float f = this.swipeOffsetY;
                            int i = this.swipeStickyRange;
                            if (f <= (-i)) {
                                if (this.stickToEdges) {
                                    stickTo((-this.offsetY) + this.topActionBarOffsetY);
                                }
                            } else if (f > (-i) && f <= i && !z) {
                                if (this.stickToEdges) {
                                    stickTo(0.0f);
                                }
                            } else {
                                Delegate delegate = this.delegate;
                                if (delegate != null) {
                                    delegate.onDismiss();
                                }
                            }
                        }
                    }
                }
                boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
                return !(dispatchTouchEvent || onTouchEvent || motionEvent.getAction() != 0) || dispatchTouchEvent || onTouchEvent;
            }
            return false;
        }

        public void stickTo(float f) {
            stickTo(f, null);
        }

        public void stickTo(float f, final Runnable runnable) {
            SpringAnimation springAnimation;
            if (this.fullsize) {
                f = (-getOffsetY()) + getTopActionBarOffsetY();
            }
            if (this.swipeOffsetY == f || ((springAnimation = this.scrollAnimator) != null && springAnimation.getSpring().getFinalPosition() == f)) {
                if (runnable != null) {
                    runnable.run();
                }
                Runnable runnable2 = this.scrollEndListener;
                if (runnable2 != null) {
                    runnable2.run();
                    return;
                }
                return;
            }
            this.pendingSwipeOffsetY = f;
            SpringAnimation springAnimation2 = this.offsetYAnimator;
            if (springAnimation2 != null) {
                springAnimation2.cancel();
            }
            SpringAnimation springAnimation3 = this.scrollAnimator;
            if (springAnimation3 != null) {
                springAnimation3.cancel();
            }
            SpringAnimation addEndListener = new SpringAnimation(this, SWIPE_OFFSET_Y, f).setSpring(new SpringForce(f).setStiffness(1200.0f).setDampingRatio(1.0f)).addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$WebViewSwipeContainer$$ExternalSyntheticLambda1
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f2, float f3) {
                    ChatAttachAlertBotWebViewLayout.WebViewSwipeContainer.this.lambda$stickTo$3(runnable, dynamicAnimation, z, f2, f3);
                }
            });
            this.scrollAnimator = addEndListener;
            addEndListener.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$stickTo$3(Runnable runnable, DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            if (dynamicAnimation == this.scrollAnimator) {
                this.scrollAnimator = null;
                if (runnable != null) {
                    runnable.run();
                }
                Runnable runnable2 = this.scrollEndListener;
                if (runnable2 != null) {
                    runnable2.run();
                }
                float f3 = this.pendingOffsetY;
                if (f3 != -1.0f) {
                    boolean z2 = this.isSwipeOffsetAnimationDisallowed;
                    this.isSwipeOffsetAnimationDisallowed = true;
                    setOffsetY(f3);
                    this.pendingOffsetY = -1.0f;
                    this.isSwipeOffsetAnimationDisallowed = z2;
                }
                this.pendingSwipeOffsetY = -2.14748365E9f;
            }
        }

        public boolean isSwipeInProgress() {
            return this.isScrolling;
        }
    }

    /* loaded from: classes4.dex */
    public static class WebProgressView extends View {
        private final SimpleFloatPropertyCompat<WebProgressView> LOAD_PROGRESS_PROPERTY;
        private Paint bluePaint;
        private float loadProgress;
        private Theme.ResourcesProvider resourcesProvider;
        private SpringAnimation springAnimation;

        public WebProgressView(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.LOAD_PROGRESS_PROPERTY = new SimpleFloatPropertyCompat("loadProgress", new SimpleFloatPropertyCompat.Getter() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$WebProgressView$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.Components.SimpleFloatPropertyCompat.Getter
                public final float get(Object obj) {
                    float f;
                    f = ((ChatAttachAlertBotWebViewLayout.WebProgressView) obj).loadProgress;
                    return f;
                }
            }, new SimpleFloatPropertyCompat.Setter() { // from class: org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout$WebProgressView$$ExternalSyntheticLambda1
                @Override // org.telegram.ui.Components.SimpleFloatPropertyCompat.Setter
                public final void set(Object obj, float f) {
                    ((ChatAttachAlertBotWebViewLayout.WebProgressView) obj).setLoadProgress(f);
                }
            }).setMultiplier(100.0f);
            Paint paint = new Paint(1);
            this.bluePaint = paint;
            this.resourcesProvider = resourcesProvider;
            paint.setColor(getThemedColor(Theme.key_featuredStickers_addButton));
            this.bluePaint.setStyle(Paint.Style.STROKE);
            this.bluePaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            this.bluePaint.setStrokeCap(Paint.Cap.ROUND);
        }

        protected int getThemedColor(int i) {
            return Theme.getColor(i, this.resourcesProvider);
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.springAnimation = new SpringAnimation(this, this.LOAD_PROGRESS_PROPERTY).setSpring(new SpringForce().setStiffness(400.0f).setDampingRatio(1.0f));
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.springAnimation.cancel();
            this.springAnimation = null;
        }

        public void setLoadProgressAnimated(float f) {
            SpringAnimation springAnimation = this.springAnimation;
            if (springAnimation == null) {
                setLoadProgress(f);
                return;
            }
            springAnimation.getSpring().setFinalPosition(f * 100.0f);
            this.springAnimation.start();
        }

        public void setLoadProgress(float f) {
            this.loadProgress = f;
            invalidate();
        }

        @Override // android.view.View
        public void draw(Canvas canvas) {
            super.draw(canvas);
            float height = getHeight() - (this.bluePaint.getStrokeWidth() / 2.0f);
            canvas.drawLine(0.0f, height, getWidth() * this.loadProgress, height, this.bluePaint);
        }
    }
}
