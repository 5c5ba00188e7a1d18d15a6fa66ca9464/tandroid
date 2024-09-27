package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.LongSparseArray;
import android.util.Property;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatValueHolder;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.exoplayer2.util.Consumer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DocumentObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.camera.CameraView;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.INavigationLayout;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Business.ChatAttachAlertQuickRepliesLayout;
import org.telegram.ui.Business.QuickRepliesController;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimationProperties;
import org.telegram.ui.Components.Bulletin;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.ChatAttachAlertAudioLayout;
import org.telegram.ui.Components.ChatAttachAlertContactsLayout;
import org.telegram.ui.Components.ChatAttachAlertDocumentLayout;
import org.telegram.ui.Components.ChatAttachAlertLocationLayout;
import org.telegram.ui.Components.ChatAttachAlertPollLayout;
import org.telegram.ui.Components.ImageUpdater;
import org.telegram.ui.Components.MentionsContainerView;
import org.telegram.ui.Components.MessagePreviewView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.MessageSendPreview;
import org.telegram.ui.PassportActivity;
import org.telegram.ui.PaymentFormActivity;
import org.telegram.ui.PhotoPickerActivity;
import org.telegram.ui.PhotoPickerSearchActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.PremiumPreviewFragment;
import org.telegram.ui.Stars.StarsController;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.recorder.StoryEntry;
import org.telegram.ui.TopicsFragment;
import org.telegram.ui.WebAppDisclaimerAlert;
import org.telegram.ui.bots.BotWebViewMenuContainer$ActionBarColorsAnimating;
import org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout;
import org.telegram.ui.web.BotWebViewContainer;
/* loaded from: classes3.dex */
public class ChatAttachAlert extends BottomSheet implements NotificationCenter.NotificationCenterDelegate, BottomSheet.BottomSheetDelegateInterface {
    public final Property ATTACH_ALERT_LAYOUT_TRANSLATION;
    private final Property ATTACH_ALERT_PROGRESS;
    public ActionBar actionBar;
    private AnimatorSet actionBarAnimation;
    private View actionBarShadow;
    private boolean allowDrawContent;
    public boolean allowEnterCaption;
    protected boolean allowOrder;
    protected boolean allowPassConfirmationAlert;
    private SpringAnimation appearSpringAnimation;
    private final Paint attachButtonPaint;
    private int attachItemSize;
    private ChatAttachAlertAudioLayout audioLayout;
    protected int avatarPicker;
    protected boolean avatarSearch;
    public final BaseFragment baseFragment;
    private float baseSelectedTextViewTranslationY;
    private LongSparseArray botAttachLayouts;
    private boolean botButtonProgressWasVisible;
    private boolean botButtonWasVisible;
    private float botMainButtonOffsetY;
    private TextView botMainButtonTextView;
    private RadialProgressView botProgressView;
    private float bottomPannelTranslation;
    private boolean buttonPressed;
    private ButtonsAdapter buttonsAdapter;
    private AnimatorSet buttonsAnimation;
    private LinearLayoutManager buttonsLayoutManager;
    protected RecyclerListView buttonsRecyclerView;
    public boolean canOpenPreview;
    public boolean captionAbove;
    private float captionEditTextTopOffset;
    protected boolean captionLimitBulletinShown;
    private final NumberTextView captionLimitView;
    private float chatActivityEnterViewAnimateFromTop;
    private int codepointCount;
    public ChatAttachAlertColorsLayout colorsLayout;
    public EditTextEmoji commentTextView;
    private int[] commentTextViewLocation;
    private AnimatorSet commentsAnimator;
    private boolean confirmationAlertShown;
    private ChatAttachAlertContactsLayout contactsLayout;
    protected float cornerRadius;
    public final int currentAccount;
    private AttachAlertLayout currentAttachLayout;
    private int currentLimit;
    float currentPanTranslationY;
    public Utilities.Callback2 customStickerHandler;
    private DecelerateInterpolator decelerateInterpolator;
    protected ChatAttachViewDelegate delegate;
    public boolean destroyed;
    public long dialogId;
    private ChatAttachAlertDocumentLayout documentLayout;
    private ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate documentsDelegate;
    private boolean documentsEnabled;
    protected ActionBarMenuItem doneItem;
    protected MessageObject editingMessageObject;
    private boolean enterCommentEventSent;
    private ArrayList exclusionRects;
    private android.graphics.Rect exclustionRect;
    public boolean forUser;
    private final boolean forceDarkTheme;
    private FrameLayout frameLayout2;
    private float fromScrollY;
    protected FrameLayout headerView;
    public boolean inBubbleMode;
    public boolean isBizLocationPicker;
    public boolean isPhotoPicker;
    private boolean isSoundPicker;
    public boolean isStickerMode;
    public boolean isStoryAudioPicker;
    public boolean isStoryLocationPicker;
    private AttachAlertLayout[] layouts;
    private ChatAttachAlertLocationLayout locationLayout;
    protected int maxSelectedPhotos;
    protected TextView mediaPreviewTextView;
    protected LinearLayout mediaPreviewView;
    public MentionsContainerView mentionContainer;
    private AnimatorSet menuAnimator;
    private boolean menuShowed;
    private MessageSendPreview messageSendPreview;
    private boolean musicEnabled;
    private AttachAlertLayout nextAttachLayout;
    private boolean openTransitionFinished;
    protected boolean openWithFrontFaceCamera;
    protected ActionBarMenuItem optionsItem;
    private boolean overrideBackgroundColor;
    private Paint paint;
    public ImageUpdater parentImageUpdater;
    public ChatActivity.ThemeDelegate parentThemeDelegate;
    private PasscodeView passcodeView;
    protected boolean paused;
    private ChatAttachAlertPhotoLayout photoLayout;
    private ChatAttachAlertPhotoLayoutPreview photoPreviewLayout;
    private boolean photosEnabled;
    public boolean pinnedToTop;
    private boolean plainTextEnabled;
    private ChatAttachAlertPollLayout pollLayout;
    private boolean pollsEnabled;
    private int previousScrollOffsetY;
    private ChatAttachAlertQuickRepliesLayout quickRepliesLayout;
    private RectF rect;
    private ChatAttachRestrictedLayout restrictedLayout;
    public int[] scrollOffsetY;
    protected ActionBarMenuItem searchItem;
    protected ImageView selectedArrowImageView;
    private View selectedCountView;
    private long selectedId;
    protected ActionBarMenuItem selectedMenuItem;
    protected TextView selectedTextView;
    protected LinearLayout selectedView;
    boolean sendButtonEnabled;
    private float sendButtonEnabledProgress;
    public boolean sent;
    private ImageUpdater.AvatarFor setAvatarFor;
    private View shadow;
    private final boolean showingFromDialog;
    public SizeNotifierFrameLayout sizeNotifierFrameLayout;
    public boolean storyLocationPickerFileIsVideo;
    public double[] storyLocationPickerLatLong;
    public File storyLocationPickerPhotoFile;
    public boolean storyMediaPicker;
    private TextPaint textPaint;
    private float toScrollY;
    private ValueAnimator topBackgroundAnimator;
    public float translationProgress;
    protected boolean typeButtonsAvailable;
    private boolean videosEnabled;
    private Object viewChangeAnimator;
    private ChatActivityEnterView.SendButton writeButton;
    private FrameLayout writeButtonContainer;

    /* loaded from: classes3.dex */
    public class 1 implements BotWebViewContainer.Delegate {
        private ValueAnimator botButtonAnimator;
        final /* synthetic */ long val$id;
        final /* synthetic */ String val$startCommand;
        final /* synthetic */ ChatAttachAlertBotWebViewLayout val$webViewLayout;

        1(ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout, String str, long j) {
            ChatAttachAlert.this = r1;
            this.val$webViewLayout = chatAttachAlertBotWebViewLayout;
            this.val$startCommand = str;
            this.val$id = j;
        }

        public static /* synthetic */ void lambda$onCloseRequested$0(Runnable runnable) {
            if (runnable != null) {
                runnable.run();
            }
        }

        public /* synthetic */ void lambda$onSetupMainButton$6(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ChatAttachAlert.this.buttonsRecyclerView.setAlpha(1.0f - floatValue);
            ChatAttachAlert.this.botMainButtonTextView.setAlpha(floatValue);
            ChatAttachAlert.this.botMainButtonOffsetY = floatValue * AndroidUtilities.dp(36.0f);
            ChatAttachAlert.this.shadow.setTranslationY(ChatAttachAlert.this.botMainButtonOffsetY);
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            chatAttachAlert.buttonsRecyclerView.setTranslationY(chatAttachAlert.botMainButtonOffsetY);
        }

        public static /* synthetic */ void lambda$onWebAppOpenInvoice$3(ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout, String str, String str2) {
            chatAttachAlertBotWebViewLayout.getWebViewContainer().onInvoiceStatusUpdate(str, str2);
        }

        public static /* synthetic */ void lambda$onWebAppOpenInvoice$4(OverlayActionBarLayoutDialog overlayActionBarLayoutDialog, ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout, String str, PaymentFormActivity.InvoiceStatus invoiceStatus) {
            if (invoiceStatus != PaymentFormActivity.InvoiceStatus.PENDING) {
                overlayActionBarLayoutDialog.dismiss();
            }
            chatAttachAlertBotWebViewLayout.getWebViewContainer().onInvoiceStatusUpdate(str, invoiceStatus.name().toLowerCase(Locale.ROOT));
        }

        public /* synthetic */ void lambda$onWebAppSetActionBarColor$1(int i, int i2, ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout, BotWebViewMenuContainer$ActionBarColorsAnimating botWebViewMenuContainer$ActionBarColorsAnimating, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ChatAttachAlert.this.actionBar.setBackgroundColor(ColorUtils.blendARGB(i, i2, floatValue));
            chatAttachAlertBotWebViewLayout.setCustomActionBarBackground(ColorUtils.blendARGB(i, i2, floatValue));
            ChatAttachAlert.this.currentAttachLayout.invalidate();
            ChatAttachAlert.this.sizeNotifierFrameLayout.invalidate();
            botWebViewMenuContainer$ActionBarColorsAnimating.updateActionBar(ChatAttachAlert.this.actionBar, floatValue);
        }

        public /* synthetic */ boolean lambda$onWebAppSwitchInlineQuery$5(TLRPC.User user, String str, OverlayActionBarLayoutDialog overlayActionBarLayoutDialog, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
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
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            BaseFragment baseFragment = chatAttachAlert.baseFragment;
            if (MessagesController.getInstance(chatAttachAlert.currentAccount).checkCanOpenChat(bundle, baseFragment)) {
                overlayActionBarLayoutDialog.dismiss();
                ChatAttachAlert.this.dismiss(true);
                baseFragment.presentFragment(new INavigationLayout.NavigationParams(new ChatActivity(bundle)).setRemoveLast(true));
            }
            return true;
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public boolean isClipboardAvailable() {
            return MediaDataController.getInstance(ChatAttachAlert.this.currentAccount).botInAttachMenu(this.val$id);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onCloseRequested(final Runnable runnable) {
            if (ChatAttachAlert.this.currentAttachLayout != this.val$webViewLayout) {
                return;
            }
            ChatAttachAlert.this.setFocusable(false);
            ChatAttachAlert.this.getWindow().setSoftInputMode(48);
            ChatAttachAlert.this.dismiss();
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlert.1.lambda$onCloseRequested$0(runnable);
                }
            }, 150L);
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
        public /* synthetic */ void onSendWebViewData(String str) {
            BotWebViewContainer.Delegate.-CC.$default$onSendWebViewData(this, str);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onSetBackButtonVisible(boolean z) {
            AndroidUtilities.updateImageViewImageAnimated(ChatAttachAlert.this.actionBar.getBackButton(), z ? R.drawable.ic_ab_back : R.drawable.ic_close_white);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onSetSettingsButtonVisible(boolean z) {
            ActionBarMenuSubItem actionBarMenuSubItem = this.val$webViewLayout.settingsItem;
            if (actionBarMenuSubItem != null) {
                actionBarMenuSubItem.setVisibility(z ? 0 : 8);
            }
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onSetupMainButton(final boolean z, boolean z2, String str, int i, int i2, final boolean z3, boolean z4) {
            AttachAlertLayout attachAlertLayout = ChatAttachAlert.this.currentAttachLayout;
            ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout = this.val$webViewLayout;
            if (attachAlertLayout == chatAttachAlertBotWebViewLayout) {
                if (chatAttachAlertBotWebViewLayout.isBotButtonAvailable() || this.val$startCommand != null) {
                    ChatAttachAlert.this.botMainButtonTextView.setClickable(z2);
                    ChatAttachAlert.this.botMainButtonTextView.setText(str);
                    ChatAttachAlert.this.botMainButtonTextView.setTextColor(i2);
                    ChatAttachAlert.this.botMainButtonTextView.setBackground(BotWebViewContainer.getMainButtonRippleDrawable(i));
                    if (ChatAttachAlert.this.botButtonWasVisible != z) {
                        ChatAttachAlert.this.botButtonWasVisible = z;
                        ValueAnimator valueAnimator = this.botButtonAnimator;
                        if (valueAnimator != null) {
                            valueAnimator.cancel();
                        }
                        ValueAnimator duration = ValueAnimator.ofFloat(z ? 0.0f : 1.0f, z ? 1.0f : 0.0f).setDuration(250L);
                        this.botButtonAnimator = duration;
                        duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$1$$ExternalSyntheticLambda1
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                                ChatAttachAlert.1.this.lambda$onSetupMainButton$6(valueAnimator2);
                            }
                        });
                        this.botButtonAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlert.1.1
                            {
                                1.this = this;
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                if (z) {
                                    ChatAttachAlert.this.buttonsRecyclerView.setVisibility(8);
                                } else {
                                    ChatAttachAlert.this.botMainButtonTextView.setVisibility(8);
                                }
                                int dp = z ? AndroidUtilities.dp(36.0f) : 0;
                                for (int i3 = 0; i3 < ChatAttachAlert.this.botAttachLayouts.size(); i3++) {
                                    ((ChatAttachAlertBotWebViewLayout) ChatAttachAlert.this.botAttachLayouts.valueAt(i3)).setMeasureOffsetY(dp);
                                }
                                if (1.this.botButtonAnimator == animator) {
                                    1.this.botButtonAnimator = null;
                                }
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationStart(Animator animator) {
                                if (!z) {
                                    ChatAttachAlert.this.buttonsRecyclerView.setAlpha(0.0f);
                                    ChatAttachAlert.this.buttonsRecyclerView.setVisibility(0);
                                    return;
                                }
                                ChatAttachAlert.this.botMainButtonTextView.setAlpha(0.0f);
                                ChatAttachAlert.this.botMainButtonTextView.setVisibility(0);
                                int dp = AndroidUtilities.dp(36.0f);
                                for (int i3 = 0; i3 < ChatAttachAlert.this.botAttachLayouts.size(); i3++) {
                                    ((ChatAttachAlertBotWebViewLayout) ChatAttachAlert.this.botAttachLayouts.valueAt(i3)).setMeasureOffsetY(dp);
                                }
                            }
                        });
                        this.botButtonAnimator.start();
                    }
                    ChatAttachAlert.this.botProgressView.setProgressColor(i2);
                    if (ChatAttachAlert.this.botButtonProgressWasVisible != z3) {
                        ChatAttachAlert.this.botProgressView.animate().cancel();
                        if (z3) {
                            ChatAttachAlert.this.botProgressView.setAlpha(0.0f);
                            ChatAttachAlert.this.botProgressView.setVisibility(0);
                        }
                        ChatAttachAlert.this.botProgressView.animate().alpha(z3 ? 1.0f : 0.0f).scaleX(z3 ? 1.0f : 0.1f).scaleY(z3 ? 1.0f : 0.1f).setDuration(250L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlert.1.2
                            {
                                1.this = this;
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                ChatAttachAlert.this.botButtonProgressWasVisible = z3;
                                if (z3) {
                                    return;
                                }
                                ChatAttachAlert.this.botProgressView.setVisibility(8);
                            }
                        }).start();
                    }
                }
            }
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
            AttachAlertLayout attachAlertLayout = ChatAttachAlert.this.currentAttachLayout;
            ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout = this.val$webViewLayout;
            if (attachAlertLayout == chatAttachAlertBotWebViewLayout && chatAttachAlertBotWebViewLayout.canExpandByRequest()) {
                this.val$webViewLayout.scrollToTop();
            }
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppOpenInvoice(TLRPC.InputInvoice inputInvoice, final String str, TLObject tLObject) {
            PaymentFormActivity paymentFormActivity;
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            BaseFragment baseFragment = chatAttachAlert.baseFragment;
            if (tLObject instanceof TLRPC.TL_payments_paymentFormStars) {
                final AlertDialog alertDialog = new AlertDialog(ChatAttachAlert.this.getContext(), 3);
                alertDialog.showDelayed(150L);
                StarsController starsController = StarsController.getInstance(ChatAttachAlert.this.currentAccount);
                TLRPC.TL_payments_paymentFormStars tL_payments_paymentFormStars = (TLRPC.TL_payments_paymentFormStars) tLObject;
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$1$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        AlertDialog.this.dismiss();
                    }
                };
                final ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout = this.val$webViewLayout;
                starsController.openPaymentForm(null, inputInvoice, tL_payments_paymentFormStars, runnable, new Utilities.Callback() { // from class: org.telegram.ui.Components.ChatAttachAlert$1$$ExternalSyntheticLambda5
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        ChatAttachAlert.1.lambda$onWebAppOpenInvoice$3(ChatAttachAlertBotWebViewLayout.this, str, (String) obj);
                    }
                });
                AndroidUtilities.hideKeyboard(this.val$webViewLayout);
                return;
            }
            if (tLObject instanceof TLRPC.PaymentForm) {
                TLRPC.PaymentForm paymentForm = (TLRPC.PaymentForm) tLObject;
                MessagesController.getInstance(chatAttachAlert.currentAccount).putUsers(paymentForm.users, false);
                paymentFormActivity = new PaymentFormActivity(paymentForm, str, baseFragment);
            } else {
                paymentFormActivity = tLObject instanceof TLRPC.PaymentReceipt ? new PaymentFormActivity((TLRPC.PaymentReceipt) tLObject) : null;
            }
            if (paymentFormActivity != null) {
                this.val$webViewLayout.scrollToTop();
                AndroidUtilities.hideKeyboard(this.val$webViewLayout);
                final OverlayActionBarLayoutDialog overlayActionBarLayoutDialog = new OverlayActionBarLayoutDialog(baseFragment.getParentActivity(), ((BottomSheet) ChatAttachAlert.this).resourcesProvider);
                overlayActionBarLayoutDialog.show();
                final ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout2 = this.val$webViewLayout;
                paymentFormActivity.setPaymentFormCallback(new PaymentFormActivity.PaymentFormCallback() { // from class: org.telegram.ui.Components.ChatAttachAlert$1$$ExternalSyntheticLambda6
                    @Override // org.telegram.ui.PaymentFormActivity.PaymentFormCallback
                    public final void onInvoiceStatusChanged(PaymentFormActivity.InvoiceStatus invoiceStatus) {
                        ChatAttachAlert.1.lambda$onWebAppOpenInvoice$4(OverlayActionBarLayoutDialog.this, chatAttachAlertBotWebViewLayout2, str, invoiceStatus);
                    }
                });
                paymentFormActivity.setResourcesProvider(((BottomSheet) ChatAttachAlert.this).resourcesProvider);
                overlayActionBarLayoutDialog.addFragment(paymentFormActivity);
            }
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public /* synthetic */ void onWebAppReady() {
            BotWebViewContainer.Delegate.-CC.$default$onWebAppReady(this);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppSetActionBarColor(int i, final int i2, boolean z) {
            final int color = ((ColorDrawable) ChatAttachAlert.this.actionBar.getBackground()).getColor();
            final BotWebViewMenuContainer$ActionBarColorsAnimating botWebViewMenuContainer$ActionBarColorsAnimating = new BotWebViewMenuContainer$ActionBarColorsAnimating();
            botWebViewMenuContainer$ActionBarColorsAnimating.setFrom(ChatAttachAlert.this.overrideBackgroundColor ? color : 0, ((BottomSheet) ChatAttachAlert.this).resourcesProvider);
            ChatAttachAlert.this.overrideBackgroundColor = z;
            botWebViewMenuContainer$ActionBarColorsAnimating.setTo(ChatAttachAlert.this.overrideBackgroundColor ? i2 : 0, ((BottomSheet) ChatAttachAlert.this).resourcesProvider);
            ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(200L);
            duration.setInterpolator(CubicBezierInterpolator.DEFAULT);
            final ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout = this.val$webViewLayout;
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$1$$ExternalSyntheticLambda3
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ChatAttachAlert.1.this.lambda$onWebAppSetActionBarColor$1(color, i2, chatAttachAlertBotWebViewLayout, botWebViewMenuContainer$ActionBarColorsAnimating, valueAnimator);
                }
            });
            duration.start();
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppSetBackgroundColor(int i) {
            this.val$webViewLayout.setCustomBackground(i);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public /* synthetic */ void onWebAppSetNavigationBarColor(int i) {
            BotWebViewContainer.Delegate.-CC.$default$onWebAppSetNavigationBarColor(this, i);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppSetupClosingBehavior(boolean z) {
            this.val$webViewLayout.setNeedCloseConfirmation(z);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppSwipingBehavior(boolean z) {
            this.val$webViewLayout.setAllowSwipes(z);
        }

        @Override // org.telegram.ui.web.BotWebViewContainer.Delegate
        public void onWebAppSwitchInlineQuery(final TLRPC.User user, final String str, List list) {
            if (list.isEmpty()) {
                BaseFragment baseFragment = ChatAttachAlert.this.baseFragment;
                if (baseFragment instanceof ChatActivity) {
                    ChatActivityEnterView chatActivityEnterView = ((ChatActivity) baseFragment).getChatActivityEnterView();
                    chatActivityEnterView.setFieldText("@" + UserObject.getPublicUsername(user) + " " + str);
                }
                ChatAttachAlert.this.dismiss(true);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("dialogsType", 14);
            bundle.putBoolean("onlySelect", true);
            bundle.putBoolean("allowGroups", list.contains("groups"));
            bundle.putBoolean("allowLegacyGroups", list.contains("groups"));
            bundle.putBoolean("allowMegagroups", list.contains("groups"));
            bundle.putBoolean("allowUsers", list.contains("users"));
            bundle.putBoolean("allowChannels", list.contains("channels"));
            bundle.putBoolean("allowBots", list.contains("bots"));
            DialogsActivity dialogsActivity = new DialogsActivity(bundle);
            final OverlayActionBarLayoutDialog overlayActionBarLayoutDialog = new OverlayActionBarLayoutDialog(ChatAttachAlert.this.getContext(), ((BottomSheet) ChatAttachAlert.this).resourcesProvider);
            dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$1$$ExternalSyntheticLambda2
                @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
                    boolean lambda$onWebAppSwitchInlineQuery$5;
                    lambda$onWebAppSwitchInlineQuery$5 = ChatAttachAlert.1.this.lambda$onWebAppSwitchInlineQuery$5(user, str, overlayActionBarLayoutDialog, dialogsActivity2, arrayList, charSequence, z, z2, i, topicsFragment);
                    return lambda$onWebAppSwitchInlineQuery$5;
                }
            });
            overlayActionBarLayoutDialog.show();
            overlayActionBarLayoutDialog.addFragment(dialogsActivity);
        }
    }

    /* loaded from: classes3.dex */
    public class 11 extends FrameLayout {
        private int color;
        private final Paint p;
        final /* synthetic */ boolean val$forceDarkTheme;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        11(Context context, boolean z) {
            super(context);
            ChatAttachAlert.this = r1;
            this.val$forceDarkTheme = z;
            this.p = new Paint();
        }

        public /* synthetic */ void lambda$onDraw$0(ValueAnimator valueAnimator) {
            ChatAttachAlert.this.captionEditTextTopOffset = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            ChatAttachAlert.this.frameLayout2.invalidate();
            invalidate();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            canvas.save();
            canvas.clipRect(0.0f, ChatAttachAlert.this.captionEditTextTopOffset, getMeasuredWidth(), getMeasuredHeight());
            super.dispatchDraw(canvas);
            canvas.restore();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int themedColor;
            if (ChatAttachAlert.this.chatActivityEnterViewAnimateFromTop != 0.0f && ChatAttachAlert.this.chatActivityEnterViewAnimateFromTop != ChatAttachAlert.this.frameLayout2.getTop() + ChatAttachAlert.this.chatActivityEnterViewAnimateFromTop) {
                if (ChatAttachAlert.this.topBackgroundAnimator != null) {
                    ChatAttachAlert.this.topBackgroundAnimator.cancel();
                }
                ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                chatAttachAlert.captionEditTextTopOffset = chatAttachAlert.chatActivityEnterViewAnimateFromTop - (ChatAttachAlert.this.frameLayout2.getTop() + ChatAttachAlert.this.captionEditTextTopOffset);
                ChatAttachAlert chatAttachAlert2 = ChatAttachAlert.this;
                chatAttachAlert2.topBackgroundAnimator = ValueAnimator.ofFloat(chatAttachAlert2.captionEditTextTopOffset, 0.0f);
                ChatAttachAlert.this.topBackgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$11$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatAttachAlert.11.this.lambda$onDraw$0(valueAnimator);
                    }
                });
                ChatAttachAlert.this.topBackgroundAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                ChatAttachAlert.this.topBackgroundAnimator.setDuration(200L);
                ChatAttachAlert.this.topBackgroundAnimator.start();
                ChatAttachAlert.this.chatActivityEnterViewAnimateFromTop = 0.0f;
            }
            float measuredHeight = (ChatAttachAlert.this.frameLayout2.getMeasuredHeight() - AndroidUtilities.dp(84.0f)) * (1.0f - getAlpha());
            View view = ChatAttachAlert.this.shadow;
            float f = (-(ChatAttachAlert.this.frameLayout2.getMeasuredHeight() - AndroidUtilities.dp(84.0f))) + ChatAttachAlert.this.captionEditTextTopOffset;
            ChatAttachAlert chatAttachAlert3 = ChatAttachAlert.this;
            view.setTranslationY(f + chatAttachAlert3.currentPanTranslationY + chatAttachAlert3.bottomPannelTranslation + measuredHeight + ChatAttachAlert.this.botMainButtonOffsetY);
            if (ChatAttachAlert.this.currentAttachLayout.hasCustomBackground()) {
                themedColor = ChatAttachAlert.this.currentAttachLayout.getCustomBackground();
            } else {
                themedColor = ChatAttachAlert.this.getThemedColor(this.val$forceDarkTheme ? Theme.key_voipgroup_listViewBackground : Theme.key_dialogBackground);
            }
            if (this.color != themedColor) {
                this.color = themedColor;
                this.p.setColor(themedColor);
            }
            canvas.drawRect(0.0f, ChatAttachAlert.this.captionEditTextTopOffset, getMeasuredWidth(), getMeasuredHeight(), this.p);
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            super.setAlpha(f);
            invalidate();
        }
    }

    /* loaded from: classes3.dex */
    public class 12 extends EditTextEmoji {
        private ValueAnimator messageEditTextAnimator;
        private int messageEditTextPredrawHeigth;
        private int messageEditTextPredrawScrollY;
        private boolean shouldAnimateEditTextWithBounds;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        12(Context context, SizeNotifierFrameLayout sizeNotifierFrameLayout, BaseFragment baseFragment, int i, boolean z, Theme.ResourcesProvider resourcesProvider) {
            super(context, sizeNotifierFrameLayout, baseFragment, i, z, resourcesProvider);
            ChatAttachAlert.this = r8;
        }

        public /* synthetic */ void lambda$dispatchDraw$0(EditTextCaption editTextCaption, ValueAnimator valueAnimator) {
            editTextCaption.setOffsetY(((Float) valueAnimator.getAnimatedValue()).floatValue());
            ChatAttachAlert.this.updateCommentTextViewPosition();
            if (ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.photoLayout) {
                ChatAttachAlert.this.photoLayout.onContainerTranslationUpdated(ChatAttachAlert.this.currentPanTranslationY);
            }
        }

        @Override // org.telegram.ui.Components.EditTextEmoji
        protected void bottomPanelTranslationY(float f) {
            ChatAttachAlert.this.bottomPannelTranslation = f;
            ChatAttachAlert.this.frameLayout2.setTranslationY(f);
            ChatAttachAlert.this.writeButtonContainer.setTranslationY(f);
            ChatAttachAlert.this.frameLayout2.invalidate();
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            chatAttachAlert.updateLayout(chatAttachAlert.currentAttachLayout, true, 0);
        }

        @Override // org.telegram.ui.Components.EditTextEmoji
        protected void closeParent() {
            ChatAttachAlert.super.dismiss();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            if (this.shouldAnimateEditTextWithBounds) {
                final EditTextCaption editText = ChatAttachAlert.this.commentTextView.getEditText();
                editText.setOffsetY(editText.getOffsetY() - ((this.messageEditTextPredrawHeigth - editText.getMeasuredHeight()) + (this.messageEditTextPredrawScrollY - editText.getScrollY())));
                ValueAnimator ofFloat = ValueAnimator.ofFloat(editText.getOffsetY(), 0.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$12$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatAttachAlert.12.this.lambda$dispatchDraw$0(editText, valueAnimator);
                    }
                });
                ValueAnimator valueAnimator = this.messageEditTextAnimator;
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                }
                this.messageEditTextAnimator = ofFloat;
                ofFloat.setDuration(200L);
                ofFloat.setInterpolator(CubicBezierInterpolator.DEFAULT);
                ofFloat.start();
                this.shouldAnimateEditTextWithBounds = false;
            }
            super.dispatchDraw(canvas);
        }

        @Override // org.telegram.ui.Components.EditTextEmoji
        public void extendActionMode(ActionMode actionMode, Menu menu) {
            BaseFragment baseFragment = ChatAttachAlert.this.baseFragment;
            if (baseFragment instanceof ChatActivity) {
                ChatActivity.fillActionModeMenu(menu, ((ChatActivity) baseFragment).getCurrentEncryptedChat(), true);
            }
            super.extendActionMode(actionMode, menu);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            ChatAttachAlert chatAttachAlert;
            EditTextCaption editText;
            boolean z;
            if (!ChatAttachAlert.this.enterCommentEventSent) {
                if (motionEvent.getX() <= ChatAttachAlert.this.commentTextView.getEditText().getLeft() || motionEvent.getX() >= ChatAttachAlert.this.commentTextView.getEditText().getRight() || motionEvent.getY() <= ChatAttachAlert.this.commentTextView.getEditText().getTop() || motionEvent.getY() >= ChatAttachAlert.this.commentTextView.getEditText().getBottom()) {
                    chatAttachAlert = ChatAttachAlert.this;
                    editText = chatAttachAlert.commentTextView.getEditText();
                    z = false;
                } else {
                    chatAttachAlert = ChatAttachAlert.this;
                    editText = chatAttachAlert.commentTextView.getEditText();
                    z = true;
                }
                chatAttachAlert.makeFocusable(editText, z);
            }
            return super.onInterceptTouchEvent(motionEvent);
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            ChatAttachAlert.this.updateCommentTextViewPosition();
        }

        @Override // org.telegram.ui.Components.EditTextEmoji
        protected void onLineCountChanged(int i, int i2) {
            if (TextUtils.isEmpty(getEditText().getText())) {
                getEditText().animate().cancel();
                getEditText().setOffsetY(0.0f);
                this.shouldAnimateEditTextWithBounds = false;
            } else {
                this.shouldAnimateEditTextWithBounds = true;
                this.messageEditTextPredrawHeigth = getEditText().getMeasuredHeight();
                this.messageEditTextPredrawScrollY = getEditText().getScrollY();
                invalidate();
            }
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            chatAttachAlert.chatActivityEnterViewAnimateFromTop = chatAttachAlert.frameLayout2.getTop() + ChatAttachAlert.this.captionEditTextTopOffset;
            ChatAttachAlert.this.frameLayout2.invalidate();
            ChatAttachAlert.this.updateCommentTextViewPosition();
        }
    }

    /* loaded from: classes3.dex */
    public class 17 extends AnimatorListenerAdapter {
        final /* synthetic */ Runnable val$onEnd;
        final /* synthetic */ int val$t;

        17(int i, Runnable runnable) {
            ChatAttachAlert.this = r1;
            this.val$t = i;
            this.val$onEnd = runnable;
        }

        /* JADX WARN: Code restructure failed: missing block: B:18:0x0018, code lost:
            if (r1.viewChangeAnimator != null) goto L10;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public /* synthetic */ void lambda$onAnimationEnd$0(DynamicAnimation dynamicAnimation, float f, float f2) {
            if (ChatAttachAlert.this.nextAttachLayout != ChatAttachAlert.this.pollLayout) {
                ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                if (chatAttachAlert.isPhotoPicker) {
                }
                ChatAttachAlert.this.nextAttachLayout.onContainerTranslationUpdated(ChatAttachAlert.this.currentPanTranslationY);
                ((BottomSheet) ChatAttachAlert.this).containerView.invalidate();
            }
            ChatAttachAlert.this.updateSelectedPosition(1);
            ChatAttachAlert.this.nextAttachLayout.onContainerTranslationUpdated(ChatAttachAlert.this.currentPanTranslationY);
            ((BottomSheet) ChatAttachAlert.this).containerView.invalidate();
        }

        public /* synthetic */ void lambda$onAnimationEnd$1(Runnable runnable, DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            ChatAttachAlert.this.nextAttachLayout.setTranslationY(0.0f);
            ChatAttachAlert.this.nextAttachLayout.onContainerTranslationUpdated(ChatAttachAlert.this.currentPanTranslationY);
            ((BottomSheet) ChatAttachAlert.this).containerView.invalidate();
            runnable.run();
            ChatAttachAlert.this.updateSelectedPosition(0);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            ChatAttachAlert.this.currentAttachLayout.setAlpha(0.0f);
            ChatAttachAlert.this.currentAttachLayout.setTranslationY(AndroidUtilities.dp(78.0f) + this.val$t);
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            chatAttachAlert.ATTACH_ALERT_LAYOUT_TRANSLATION.set(chatAttachAlert.currentAttachLayout, Float.valueOf(1.0f));
            ChatAttachAlert.this.actionBar.setAlpha(0.0f);
            SpringAnimation springAnimation = new SpringAnimation(ChatAttachAlert.this.nextAttachLayout, DynamicAnimation.TRANSLATION_Y, 0.0f);
            springAnimation.getSpring().setDampingRatio(0.75f);
            springAnimation.getSpring().setStiffness(500.0f);
            springAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$17$$ExternalSyntheticLambda0
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
                public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                    ChatAttachAlert.17.this.lambda$onAnimationEnd$0(dynamicAnimation, f, f2);
                }
            });
            final Runnable runnable = this.val$onEnd;
            springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$17$$ExternalSyntheticLambda1
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                    ChatAttachAlert.17.this.lambda$onAnimationEnd$1(runnable, dynamicAnimation, z, f, f2);
                }
            });
            ChatAttachAlert.this.viewChangeAnimator = springAnimation;
            springAnimation.start();
        }
    }

    /* loaded from: classes3.dex */
    public class 3 extends SizeNotifierFrameLayout {
        AdjustPanLayoutHelper adjustPanLayoutHelper;
        private Bulletin.Delegate bulletinDelegate;
        private boolean ignoreLayout;
        private float initialTranslationY;
        private int lastNotifyWidth;
        private RectF rect;
        final /* synthetic */ boolean val$forceDarkTheme;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        3(Context context, boolean z) {
            super(context);
            ChatAttachAlert.this = r1;
            this.val$forceDarkTheme = z;
            this.bulletinDelegate = new Bulletin.Delegate() { // from class: org.telegram.ui.Components.ChatAttachAlert.3.1
                {
                    3.this = this;
                }

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
                public int getBottomOffset(int i) {
                    return (3.this.getHeight() - ChatAttachAlert.this.frameLayout2.getTop()) + AndroidUtilities.dp(52.0f);
                }

                @Override // org.telegram.ui.Components.Bulletin.Delegate
                public /* synthetic */ int getTopOffset(int i) {
                    return Bulletin.Delegate.-CC.$default$getTopOffset(this, i);
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
            };
            this.rect = new RectF();
            this.adjustPanLayoutHelper = new AdjustPanLayoutHelper(this) { // from class: org.telegram.ui.Components.ChatAttachAlert.3.2
                {
                    3.this = this;
                }

                @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                protected boolean heightAnimationEnabled() {
                    if (ChatAttachAlert.this.isDismissed() || !ChatAttachAlert.this.openTransitionFinished) {
                        return false;
                    }
                    return !(ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.pollLayout || ChatAttachAlert.this.commentTextView.isPopupVisible()) || (ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.pollLayout && !ChatAttachAlert.this.pollLayout.isPopupVisible());
                }

                @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                public void onPanTranslationUpdate(float f, float f2, boolean z2) {
                    ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                    chatAttachAlert.currentPanTranslationY = f;
                    if (chatAttachAlert.fromScrollY > 0.0f) {
                        ChatAttachAlert chatAttachAlert2 = ChatAttachAlert.this;
                        chatAttachAlert2.currentPanTranslationY += (chatAttachAlert2.fromScrollY - ChatAttachAlert.this.toScrollY) * (1.0f - f2);
                    }
                    ChatAttachAlert chatAttachAlert3 = ChatAttachAlert.this;
                    chatAttachAlert3.actionBar.setTranslationY(chatAttachAlert3.currentPanTranslationY);
                    ChatAttachAlert chatAttachAlert4 = ChatAttachAlert.this;
                    chatAttachAlert4.selectedMenuItem.setTranslationY(chatAttachAlert4.currentPanTranslationY);
                    ChatAttachAlert chatAttachAlert5 = ChatAttachAlert.this;
                    ActionBarMenuItem actionBarMenuItem = chatAttachAlert5.searchItem;
                    if (actionBarMenuItem != null) {
                        actionBarMenuItem.setTranslationY(chatAttachAlert5.currentPanTranslationY);
                    }
                    ChatAttachAlert chatAttachAlert6 = ChatAttachAlert.this;
                    chatAttachAlert6.doneItem.setTranslationY(chatAttachAlert6.currentPanTranslationY);
                    ChatAttachAlert.this.actionBarShadow.setTranslationY(ChatAttachAlert.this.currentPanTranslationY);
                    ChatAttachAlert.this.updateSelectedPosition(0);
                    ChatAttachAlert chatAttachAlert7 = ChatAttachAlert.this;
                    chatAttachAlert7.setCurrentPanTranslationY(chatAttachAlert7.currentPanTranslationY);
                    3.this.invalidate();
                    ChatAttachAlert.this.frameLayout2.invalidate();
                    ChatAttachAlert.this.updateCommentTextViewPosition();
                    if (ChatAttachAlert.this.currentAttachLayout != null) {
                        ChatAttachAlert.this.currentAttachLayout.onContainerTranslationUpdated(ChatAttachAlert.this.currentPanTranslationY);
                    }
                }

                @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                public void onTransitionEnd() {
                    super.onTransitionEnd();
                    ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                    chatAttachAlert.updateLayout(chatAttachAlert.currentAttachLayout, false, 0);
                    ChatAttachAlert chatAttachAlert2 = ChatAttachAlert.this;
                    chatAttachAlert2.previousScrollOffsetY = chatAttachAlert2.scrollOffsetY[0];
                    ChatAttachAlert.this.currentAttachLayout.onPanTransitionEnd();
                    if (!(ChatAttachAlert.this.currentAttachLayout instanceof ChatAttachAlertBotWebViewLayout) || ChatAttachAlert.this.botButtonWasVisible) {
                        return;
                    }
                    int dp = ((BottomSheet) ChatAttachAlert.this).keyboardVisible ? AndroidUtilities.dp(84.0f) : 0;
                    for (int i = 0; i < ChatAttachAlert.this.botAttachLayouts.size(); i++) {
                        ((ChatAttachAlertBotWebViewLayout) ChatAttachAlert.this.botAttachLayouts.valueAt(i)).setMeasureOffsetY(dp);
                    }
                }

                /* JADX WARN: Removed duplicated region for block: B:34:0x0065  */
                @Override // org.telegram.ui.ActionBar.AdjustPanLayoutHelper
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public void onTransitionStart(boolean z2, int i) {
                    super.onTransitionStart(z2, i);
                    if (ChatAttachAlert.this.previousScrollOffsetY > 0) {
                        int i2 = ChatAttachAlert.this.previousScrollOffsetY;
                        ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                        if (i2 != chatAttachAlert.scrollOffsetY[0] && z2) {
                            chatAttachAlert.fromScrollY = chatAttachAlert.previousScrollOffsetY;
                            ChatAttachAlert chatAttachAlert2 = ChatAttachAlert.this;
                            chatAttachAlert2.toScrollY = chatAttachAlert2.scrollOffsetY[0];
                            3.this.invalidate();
                            if ((ChatAttachAlert.this.currentAttachLayout instanceof ChatAttachAlertBotWebViewLayout) && !ChatAttachAlert.this.botButtonWasVisible) {
                                View view = ChatAttachAlert.this.shadow;
                                int i3 = z2 ? 8 : 0;
                                view.setVisibility(i3);
                                ChatAttachAlert.this.buttonsRecyclerView.setVisibility(i3);
                            }
                            ChatAttachAlert.this.currentAttachLayout.onPanTransitionStart(z2, i);
                        }
                    }
                    ChatAttachAlert.this.fromScrollY = -1.0f;
                    3.this.invalidate();
                    if (ChatAttachAlert.this.currentAttachLayout instanceof ChatAttachAlertBotWebViewLayout) {
                        View view2 = ChatAttachAlert.this.shadow;
                        if (z2) {
                        }
                        view2.setVisibility(i3);
                        ChatAttachAlert.this.buttonsRecyclerView.setVisibility(i3);
                    }
                    ChatAttachAlert.this.currentAttachLayout.onPanTransitionStart(z2, i);
                }
            };
        }

        /* JADX WARN: Code restructure failed: missing block: B:172:0x031b, code lost:
            if (r4 == null) goto L65;
         */
        /* JADX WARN: Code restructure failed: missing block: B:175:0x032a, code lost:
            if (r4 == null) goto L65;
         */
        /* JADX WARN: Code restructure failed: missing block: B:176:0x032c, code lost:
            r15 = 1.0f;
         */
        /* JADX WARN: Code restructure failed: missing block: B:177:0x032f, code lost:
            r15 = 1.0f - r4.getAlpha();
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void drawChildBackground(Canvas canvas, View view) {
            int dp;
            float dp2;
            float f;
            int themedColor;
            int themedColor2;
            FrameLayout frameLayout;
            float f2;
            if (view instanceof AttachAlertLayout) {
                canvas.save();
                canvas.translate(0.0f, ChatAttachAlert.this.currentPanTranslationY);
                int alpha = (int) (view.getAlpha() * 255.0f);
                AttachAlertLayout attachAlertLayout = (AttachAlertLayout) view;
                int needsActionBar = attachAlertLayout.needsActionBar();
                int dp3 = AndroidUtilities.dp(13.0f);
                FrameLayout frameLayout2 = ChatAttachAlert.this.headerView;
                int alpha2 = dp3 + ((int) ((frameLayout2 != null ? frameLayout2.getAlpha() : 0.0f) * AndroidUtilities.dp(26.0f)));
                int scrollOffsetY = (ChatAttachAlert.this.getScrollOffsetY(0) - ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop) - alpha2;
                if (((BottomSheet) ChatAttachAlert.this).currentSheetAnimationType == 1 || ChatAttachAlert.this.viewChangeAnimator != null) {
                    scrollOffsetY = (int) (scrollOffsetY + view.getTranslationY());
                }
                int dp4 = AndroidUtilities.dp(20.0f) + scrollOffsetY;
                int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(45.0f) + ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop;
                int currentActionBarHeight = needsActionBar != 0 ? ActionBar.getCurrentActionBarHeight() : ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop;
                if (needsActionBar == 2) {
                    f = scrollOffsetY < currentActionBarHeight ? Math.max(0.0f, 1.0f - ((currentActionBarHeight - scrollOffsetY) / ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop)) : 1.0f;
                } else {
                    float f3 = alpha2;
                    if (attachAlertLayout == ChatAttachAlert.this.locationLayout) {
                        dp = AndroidUtilities.dp(11.0f);
                    } else if (attachAlertLayout == ChatAttachAlert.this.pollLayout) {
                        dp2 = f3 - AndroidUtilities.dp(3.0f);
                        float alpha3 = ChatAttachAlert.this.actionBar.getAlpha();
                        int i = (int) (((currentActionBarHeight - dp2) + AndroidUtilities.statusBarHeight) * alpha3);
                        scrollOffsetY -= i;
                        dp4 -= i;
                        measuredHeight += i;
                        f = 1.0f - alpha3;
                    } else {
                        dp = AndroidUtilities.dp(4.0f);
                    }
                    dp2 = f3 + dp;
                    float alpha32 = ChatAttachAlert.this.actionBar.getAlpha();
                    int i2 = (int) (((currentActionBarHeight - dp2) + AndroidUtilities.statusBarHeight) * alpha32);
                    scrollOffsetY -= i2;
                    dp4 -= i2;
                    measuredHeight += i2;
                    f = 1.0f - alpha32;
                }
                int i3 = Build.VERSION.SDK_INT;
                if (i3 >= 21 && !ChatAttachAlert.this.inBubbleMode) {
                    int i4 = AndroidUtilities.statusBarHeight;
                    scrollOffsetY += i4;
                    dp4 += i4;
                    measuredHeight -= i4;
                }
                if (ChatAttachAlert.this.currentAttachLayout.hasCustomBackground()) {
                    themedColor = ChatAttachAlert.this.currentAttachLayout.getCustomBackground();
                } else {
                    themedColor = ChatAttachAlert.this.getThemedColor(this.val$forceDarkTheme ? Theme.key_voipgroup_listViewBackground : Theme.key_dialogBackground);
                }
                ((BottomSheet) ChatAttachAlert.this).shadowDrawable.setAlpha(alpha);
                ((BottomSheet) ChatAttachAlert.this).shadowDrawable.setBounds(0, scrollOffsetY, getMeasuredWidth(), measuredHeight);
                ((BottomSheet) ChatAttachAlert.this).shadowDrawable.draw(canvas);
                if (needsActionBar == 2) {
                    Theme.dialogs_onlineCirclePaint.setColor(themedColor);
                    Theme.dialogs_onlineCirclePaint.setAlpha(alpha);
                    this.rect.set(((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop + scrollOffsetY, getMeasuredWidth() - ((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop + scrollOffsetY + AndroidUtilities.dp(24.0f));
                    canvas.save();
                    RectF rectF = this.rect;
                    float f4 = rectF.left;
                    float f5 = rectF.top;
                    canvas.clipRect(f4, f5, rectF.right, (rectF.height() / 2.0f) + f5);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                    canvas.restore();
                }
                if ((f != 1.0f && needsActionBar != 2) || ChatAttachAlert.this.currentAttachLayout.hasCustomActionBarBackground()) {
                    Paint paint = Theme.dialogs_onlineCirclePaint;
                    if (ChatAttachAlert.this.currentAttachLayout.hasCustomActionBarBackground()) {
                        themedColor = ChatAttachAlert.this.currentAttachLayout.getCustomActionBarBackground();
                    }
                    paint.setColor(themedColor);
                    Theme.dialogs_onlineCirclePaint.setAlpha(alpha);
                    this.rect.set(((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop + scrollOffsetY, getMeasuredWidth() - ((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop + scrollOffsetY + AndroidUtilities.dp(24.0f));
                    canvas.save();
                    RectF rectF2 = this.rect;
                    float f6 = rectF2.left;
                    float f7 = rectF2.top;
                    canvas.clipRect(f6, f7, rectF2.right, (rectF2.height() / 2.0f) + f7);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                    canvas.restore();
                }
                if (ChatAttachAlert.this.currentAttachLayout.hasCustomActionBarBackground()) {
                    Theme.dialogs_onlineCirclePaint.setColor(ChatAttachAlert.this.currentAttachLayout.getCustomActionBarBackground());
                    Theme.dialogs_onlineCirclePaint.setAlpha(alpha);
                    int scrollOffsetY2 = ChatAttachAlert.this.getScrollOffsetY(0);
                    if (i3 >= 21 && !ChatAttachAlert.this.inBubbleMode) {
                        scrollOffsetY2 += AndroidUtilities.statusBarHeight;
                    }
                    this.rect.set(((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, (((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop + scrollOffsetY + AndroidUtilities.dp(12.0f)) * f, getMeasuredWidth() - ((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, scrollOffsetY2 + AndroidUtilities.dp(12.0f));
                    canvas.save();
                    canvas.drawRect(this.rect, Theme.dialogs_onlineCirclePaint);
                    canvas.restore();
                }
                FrameLayout frameLayout3 = ChatAttachAlert.this.headerView;
                if ((frameLayout3 == null || frameLayout3.getAlpha() != 1.0f) && f != 0.0f) {
                    int dp5 = AndroidUtilities.dp(36.0f);
                    this.rect.set((getMeasuredWidth() - dp5) / 2, dp4, (getMeasuredWidth() + dp5) / 2, dp4 + AndroidUtilities.dp(4.0f));
                    if (needsActionBar == 2) {
                        themedColor2 = 536870912;
                        f2 = f;
                    } else if (ChatAttachAlert.this.currentAttachLayout.hasCustomActionBarBackground()) {
                        int customActionBarBackground = ChatAttachAlert.this.currentAttachLayout.getCustomActionBarBackground();
                        themedColor2 = ColorUtils.blendARGB(customActionBarBackground, ColorUtils.calculateLuminance(customActionBarBackground) < 0.5d ? -1 : -16777216, 0.5f);
                        frameLayout = ChatAttachAlert.this.headerView;
                    } else {
                        themedColor2 = ChatAttachAlert.this.getThemedColor(Theme.key_sheet_scrollUp);
                        frameLayout = ChatAttachAlert.this.headerView;
                    }
                    int alpha4 = Color.alpha(themedColor2);
                    Theme.dialogs_onlineCirclePaint.setColor(themedColor2);
                    Theme.dialogs_onlineCirclePaint.setAlpha((int) (alpha4 * f2 * f * view.getAlpha()));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                }
                canvas.restore();
            }
        }

        private int getCurrentTop() {
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            int i = chatAttachAlert.scrollOffsetY[0] - (((BottomSheet) chatAttachAlert).backgroundPaddingTop * 2);
            int dp = AndroidUtilities.dp(13.0f);
            FrameLayout frameLayout = ChatAttachAlert.this.headerView;
            int dp2 = (i - (dp + (frameLayout != null ? AndroidUtilities.dp(frameLayout.getAlpha() * 26.0f) : 0))) + AndroidUtilities.dp(20.0f);
            return (Build.VERSION.SDK_INT < 21 || ChatAttachAlert.this.inBubbleMode) ? dp2 : dp2 + AndroidUtilities.statusBarHeight;
        }

        private float getY(View view) {
            float f;
            float dp;
            if (view instanceof AttachAlertLayout) {
                AttachAlertLayout attachAlertLayout = (AttachAlertLayout) view;
                int needsActionBar = attachAlertLayout.needsActionBar();
                int dp2 = AndroidUtilities.dp(13.0f);
                FrameLayout frameLayout = ChatAttachAlert.this.headerView;
                int alpha = dp2 + ((int) ((frameLayout != null ? frameLayout.getAlpha() : 0.0f) * AndroidUtilities.dp(26.0f)));
                int scrollOffsetY = (ChatAttachAlert.this.getScrollOffsetY(0) - ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop) - alpha;
                if (((BottomSheet) ChatAttachAlert.this).currentSheetAnimationType == 1 || ChatAttachAlert.this.viewChangeAnimator != null) {
                    scrollOffsetY = (int) (scrollOffsetY + view.getTranslationY());
                }
                int dp3 = AndroidUtilities.dp(20.0f) + scrollOffsetY;
                int currentActionBarHeight = needsActionBar != 0 ? ActionBar.getCurrentActionBarHeight() : ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop;
                if (needsActionBar != 2 && scrollOffsetY + ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop < currentActionBarHeight) {
                    float f2 = alpha;
                    if (attachAlertLayout == ChatAttachAlert.this.locationLayout) {
                        f = 11.0f;
                    } else if (attachAlertLayout == ChatAttachAlert.this.pollLayout) {
                        dp = f2 - AndroidUtilities.dp(3.0f);
                        dp3 -= (int) (((currentActionBarHeight - dp) + AndroidUtilities.statusBarHeight) * ChatAttachAlert.this.actionBar.getAlpha());
                    } else {
                        f = 4.0f;
                    }
                    dp = f2 + AndroidUtilities.dp(f);
                    dp3 -= (int) (((currentActionBarHeight - dp) + AndroidUtilities.statusBarHeight) * ChatAttachAlert.this.actionBar.getAlpha());
                }
                if (Build.VERSION.SDK_INT >= 21 && !ChatAttachAlert.this.inBubbleMode) {
                    dp3 += AndroidUtilities.statusBarHeight;
                }
                return dp3;
            }
            return 0.0f;
        }

        public /* synthetic */ void lambda$onMeasure$0() {
            ChatAttachAlert.this.buttonsAdapter.notifyDataSetChanged();
        }

        private void onMeasureInternal(int i, int i2) {
            int makeMeasureSpec;
            int paddingTop;
            int size = View.MeasureSpec.getSize(i);
            int size2 = View.MeasureSpec.getSize(i2);
            setMeasuredDimension(size, size2);
            int i3 = size - (((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft * 2);
            if (!ChatAttachAlert.this.commentTextView.isWaitingForKeyboardOpen() && AndroidUtilities.dp(20.0f) >= 0 && !ChatAttachAlert.this.commentTextView.isPopupShowing() && !ChatAttachAlert.this.commentTextView.isAnimatePopupClosing()) {
                this.ignoreLayout = true;
                ChatAttachAlert.this.commentTextView.hideEmojiView();
                this.ignoreLayout = false;
            }
            if (ChatAttachAlert.this.pollLayout != null && AndroidUtilities.dp(20.0f) >= 0 && !ChatAttachAlert.this.pollLayout.isWaitingForKeyboardOpen() && !ChatAttachAlert.this.pollLayout.isPopupShowing() && !ChatAttachAlert.this.pollLayout.isAnimatePopupClosing() && !ChatAttachAlert.this.pollLayout.isEmojiSearchOpened) {
                this.ignoreLayout = true;
                ChatAttachAlert.this.pollLayout.hideEmojiView();
                this.ignoreLayout = false;
            }
            if (AndroidUtilities.dp(20.0f) >= 0) {
                int dp = ((BottomSheet) ChatAttachAlert.this).keyboardVisible ? (ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.pollLayout && ChatAttachAlert.this.pollLayout.emojiView != null && ChatAttachAlert.this.pollLayout.isEmojiSearchOpened) ? AndroidUtilities.dp(120.0f) : 0 : (ChatAttachAlert.this.currentAttachLayout != ChatAttachAlert.this.pollLayout || ChatAttachAlert.this.pollLayout.emojiView == null) ? ChatAttachAlert.this.commentTextView.getEmojiPadding() : ChatAttachAlert.this.pollLayout.getEmojiPadding();
                if (!AndroidUtilities.isInMultiwindow) {
                    size2 -= dp;
                    i2 = View.MeasureSpec.makeMeasureSpec(size2, 1073741824);
                }
                this.ignoreLayout = true;
                ChatAttachAlert.this.currentAttachLayout.onPreMeasure(i3, size2);
                if (ChatAttachAlert.this.nextAttachLayout != null) {
                    ChatAttachAlert.this.nextAttachLayout.onPreMeasure(i3, size2);
                }
                this.ignoreLayout = false;
            }
            int childCount = getChildCount();
            for (int i4 = 0; i4 < childCount; i4++) {
                View childAt = getChildAt(i4);
                if (childAt != null && childAt.getVisibility() != 8) {
                    EditTextEmoji editTextEmoji = ChatAttachAlert.this.commentTextView;
                    if ((editTextEmoji == null || !editTextEmoji.isPopupView(childAt)) && (ChatAttachAlert.this.pollLayout == null || childAt != ChatAttachAlert.this.pollLayout.emojiView)) {
                        measureChildWithMargins(childAt, i, 0, i2, 0);
                    } else {
                        if (ChatAttachAlert.this.inBubbleMode) {
                            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
                            paddingTop = getPaddingTop() + size2;
                        } else if (!AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
                            paddingTop = childAt.getLayoutParams().height;
                        } else if (AndroidUtilities.isTablet()) {
                            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
                            paddingTop = Math.min(AndroidUtilities.dp(AndroidUtilities.isTablet() ? 200.0f : 320.0f), (size2 - AndroidUtilities.statusBarHeight) + getPaddingTop());
                        } else {
                            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
                            paddingTop = (size2 - AndroidUtilities.statusBarHeight) + getPaddingTop();
                        }
                        childAt.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(paddingTop, 1073741824));
                    }
                }
            }
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void dispatchDraw(Canvas canvas) {
            canvas.save();
            if (ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.photoPreviewLayout || ChatAttachAlert.this.nextAttachLayout == ChatAttachAlert.this.photoPreviewLayout || (ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.photoLayout && ChatAttachAlert.this.nextAttachLayout == null)) {
                drawChildBackground(canvas, ChatAttachAlert.this.currentAttachLayout);
            }
            super.dispatchDraw(canvas);
            canvas.restore();
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            int dp;
            float dp2;
            float f;
            int themedColor;
            boolean drawChild;
            int themedColor2;
            float alpha;
            if (!(view instanceof AttachAlertLayout) || view.getAlpha() <= 0.0f) {
                ActionBar actionBar = ChatAttachAlert.this.actionBar;
                if (view == actionBar) {
                    float alpha2 = actionBar.getAlpha();
                    if (alpha2 <= 0.0f) {
                        return false;
                    }
                    if (alpha2 >= 1.0f) {
                        return super.drawChild(canvas, view, j);
                    }
                    canvas.save();
                    canvas.clipRect(ChatAttachAlert.this.actionBar.getX(), getY(ChatAttachAlert.this.currentAttachLayout), ChatAttachAlert.this.actionBar.getX() + ChatAttachAlert.this.actionBar.getWidth(), ChatAttachAlert.this.actionBar.getY() + ChatAttachAlert.this.actionBar.getHeight());
                    boolean drawChild2 = super.drawChild(canvas, view, j);
                    canvas.restore();
                    return drawChild2;
                }
                return super.drawChild(canvas, view, j);
            }
            canvas.save();
            canvas.translate(0.0f, ChatAttachAlert.this.currentPanTranslationY);
            int alpha3 = (int) (view.getAlpha() * 255.0f);
            AttachAlertLayout attachAlertLayout = (AttachAlertLayout) view;
            int needsActionBar = attachAlertLayout.needsActionBar();
            int dp3 = AndroidUtilities.dp(13.0f);
            FrameLayout frameLayout = ChatAttachAlert.this.headerView;
            int dp4 = dp3 + (frameLayout != null ? AndroidUtilities.dp(frameLayout.getAlpha() * 26.0f) : 0);
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            int scrollOffsetY = (chatAttachAlert.getScrollOffsetY(attachAlertLayout == chatAttachAlert.currentAttachLayout ? 0 : 1) - ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop) - dp4;
            if (((BottomSheet) ChatAttachAlert.this).currentSheetAnimationType == 1 || ChatAttachAlert.this.viewChangeAnimator != null) {
                scrollOffsetY = (int) (scrollOffsetY + view.getTranslationY());
            }
            int dp5 = AndroidUtilities.dp(20.0f) + scrollOffsetY;
            int measuredHeight = getMeasuredHeight() + AndroidUtilities.dp(45.0f) + ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop;
            int currentActionBarHeight = needsActionBar != 0 ? ActionBar.getCurrentActionBarHeight() : ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop;
            if (needsActionBar == 2) {
                if (scrollOffsetY < currentActionBarHeight) {
                    f = Math.max(0.0f, 1.0f - ((currentActionBarHeight - scrollOffsetY) / ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop));
                }
                f = 1.0f;
            } else {
                if (((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop + scrollOffsetY < currentActionBarHeight) {
                    float f2 = dp4;
                    if (attachAlertLayout == ChatAttachAlert.this.locationLayout) {
                        dp = AndroidUtilities.dp(11.0f);
                    } else if (attachAlertLayout == ChatAttachAlert.this.pollLayout) {
                        dp2 = f2 - AndroidUtilities.dp(3.0f);
                        float min = Math.min(1.0f, ((currentActionBarHeight - scrollOffsetY) - ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop) / dp2);
                        int i = (int) ((currentActionBarHeight - dp2) * min);
                        scrollOffsetY -= i;
                        dp5 -= i;
                        measuredHeight += i;
                        f = 1.0f - min;
                    } else {
                        dp = AndroidUtilities.dp(4.0f);
                    }
                    dp2 = f2 + dp;
                    float min2 = Math.min(1.0f, ((currentActionBarHeight - scrollOffsetY) - ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop) / dp2);
                    int i2 = (int) ((currentActionBarHeight - dp2) * min2);
                    scrollOffsetY -= i2;
                    dp5 -= i2;
                    measuredHeight += i2;
                    f = 1.0f - min2;
                }
                f = 1.0f;
            }
            if (Build.VERSION.SDK_INT >= 21 && !ChatAttachAlert.this.inBubbleMode) {
                int i3 = AndroidUtilities.statusBarHeight;
                scrollOffsetY += i3;
                dp5 += i3;
                measuredHeight -= i3;
            }
            if (ChatAttachAlert.this.currentAttachLayout.hasCustomBackground()) {
                themedColor = ChatAttachAlert.this.currentAttachLayout.getCustomBackground();
            } else {
                themedColor = ChatAttachAlert.this.getThemedColor(this.val$forceDarkTheme ? Theme.key_voipgroup_listViewBackground : Theme.key_dialogBackground);
            }
            boolean z = (ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.photoPreviewLayout || ChatAttachAlert.this.nextAttachLayout == ChatAttachAlert.this.photoPreviewLayout || (ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.photoLayout && ChatAttachAlert.this.nextAttachLayout == null)) ? false : true;
            if (z) {
                ((BottomSheet) ChatAttachAlert.this).shadowDrawable.setAlpha(alpha3);
                ((BottomSheet) ChatAttachAlert.this).shadowDrawable.setBounds(0, scrollOffsetY, getMeasuredWidth(), measuredHeight);
                ((BottomSheet) ChatAttachAlert.this).shadowDrawable.draw(canvas);
                if (needsActionBar == 2) {
                    Theme.dialogs_onlineCirclePaint.setColor(themedColor);
                    Theme.dialogs_onlineCirclePaint.setAlpha(alpha3);
                    this.rect.set(((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop + scrollOffsetY, getMeasuredWidth() - ((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop + scrollOffsetY + AndroidUtilities.dp(24.0f));
                    canvas.save();
                    RectF rectF = this.rect;
                    float f3 = rectF.left;
                    float f4 = rectF.top;
                    canvas.clipRect(f3, f4, rectF.right, (rectF.height() / 2.0f) + f4);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                    canvas.restore();
                }
            }
            if (view == ChatAttachAlert.this.contactsLayout || view == ChatAttachAlert.this.quickRepliesLayout || view == ChatAttachAlert.this.audioLayout) {
                drawChild = super.drawChild(canvas, view, j);
            } else {
                canvas.save();
                canvas.clipRect(((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, (ChatAttachAlert.this.actionBar.getY() + ChatAttachAlert.this.actionBar.getMeasuredHeight()) - ChatAttachAlert.this.currentPanTranslationY, getMeasuredWidth() - ((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, getMeasuredHeight());
                drawChild = super.drawChild(canvas, view, j);
                canvas.restore();
            }
            if (z) {
                if (f != 1.0f && needsActionBar != 2) {
                    Theme.dialogs_onlineCirclePaint.setColor(themedColor);
                    Theme.dialogs_onlineCirclePaint.setAlpha(alpha3);
                    this.rect.set(((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop + scrollOffsetY, getMeasuredWidth() - ((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, ((BottomSheet) ChatAttachAlert.this).backgroundPaddingTop + scrollOffsetY + AndroidUtilities.dp(24.0f));
                    canvas.save();
                    RectF rectF2 = this.rect;
                    float f5 = rectF2.left;
                    float f6 = rectF2.top;
                    canvas.clipRect(f5, f6, rectF2.right, (rectF2.height() / 2.0f) + f6);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(12.0f) * f, AndroidUtilities.dp(12.0f) * f, Theme.dialogs_onlineCirclePaint);
                    canvas.restore();
                }
                FrameLayout frameLayout2 = ChatAttachAlert.this.headerView;
                if ((frameLayout2 == null || frameLayout2.getAlpha() != 1.0f) && f != 0.0f) {
                    int dp6 = AndroidUtilities.dp(36.0f);
                    this.rect.set((getMeasuredWidth() - dp6) / 2, dp5, (getMeasuredWidth() + dp6) / 2, dp5 + AndroidUtilities.dp(4.0f));
                    if (needsActionBar == 2) {
                        themedColor2 = 536870912;
                        alpha = f;
                    } else {
                        themedColor2 = ChatAttachAlert.this.getThemedColor(Theme.key_sheet_scrollUp);
                        FrameLayout frameLayout3 = ChatAttachAlert.this.headerView;
                        alpha = frameLayout3 == null ? 1.0f : 1.0f - frameLayout3.getAlpha();
                    }
                    int alpha4 = Color.alpha(themedColor2);
                    Theme.dialogs_onlineCirclePaint.setColor(themedColor2);
                    Theme.dialogs_onlineCirclePaint.setAlpha((int) (alpha4 * alpha * f * view.getAlpha()));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), Theme.dialogs_onlineCirclePaint);
                }
            }
            canvas.restore();
            return drawChild;
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.adjustPanLayoutHelper.setResizableView(this);
            this.adjustPanLayoutHelper.onAttach();
            ChatAttachAlert.this.commentTextView.setAdjustPanLayoutHelper(this.adjustPanLayoutHelper);
        }

        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.adjustPanLayoutHelper.onDetach();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            boolean z = ChatAttachAlert.this.inBubbleMode;
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            if (ChatAttachAlert.this.currentAttachLayout.onContainerViewTouchEvent(motionEvent)) {
                return true;
            }
            if (motionEvent.getAction() != 0 || ChatAttachAlert.this.scrollOffsetY[0] == 0 || motionEvent.getY() >= getCurrentTop() || ChatAttachAlert.this.actionBar.getAlpha() != 0.0f) {
                return super.onInterceptTouchEvent(motionEvent);
            }
            ChatAttachAlert.this.onDismissWithTouchOutside();
            return true;
        }

        /* JADX WARN: Removed duplicated region for block: B:137:0x0106  */
        /* JADX WARN: Removed duplicated region for block: B:145:0x0122  */
        /* JADX WARN: Removed duplicated region for block: B:148:0x0132  */
        /* JADX WARN: Removed duplicated region for block: B:156:0x0150  */
        /* JADX WARN: Removed duplicated region for block: B:158:0x015a  */
        @Override // org.telegram.ui.Components.SizeNotifierFrameLayout, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            int i5;
            int i6;
            int i7;
            int i8;
            int i9;
            EditTextEmoji editTextEmoji;
            int emojiPadding;
            int i10 = i3 - i;
            if (this.lastNotifyWidth != i10) {
                this.lastNotifyWidth = i10;
                if (ChatAttachAlert.this.messageSendPreview != null && ChatAttachAlert.this.messageSendPreview.isShowing()) {
                    ChatAttachAlert.this.messageSendPreview.dismiss();
                }
            }
            int childCount = getChildCount();
            if (Build.VERSION.SDK_INT >= 29) {
                ChatAttachAlert.this.exclustionRect.set(i, i2, i3, i4);
                setSystemGestureExclusionRects(ChatAttachAlert.this.exclusionRects);
            }
            int measureKeyboardHeight = measureKeyboardHeight();
            int paddingBottom = getPaddingBottom();
            if (!((BottomSheet) ChatAttachAlert.this).keyboardVisible) {
                if (ChatAttachAlert.this.pollLayout == null || ChatAttachAlert.this.currentAttachLayout != ChatAttachAlert.this.pollLayout || ChatAttachAlert.this.pollLayout.emojiView == null) {
                    emojiPadding = (measureKeyboardHeight > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow || AndroidUtilities.isTablet()) ? 0 : ChatAttachAlert.this.commentTextView.getEmojiPadding();
                } else if (measureKeyboardHeight <= AndroidUtilities.dp(20.0f) && !AndroidUtilities.isInMultiwindow && !AndroidUtilities.isTablet()) {
                    emojiPadding = ChatAttachAlert.this.pollLayout.getEmojiPadding();
                }
                paddingBottom += emojiPadding;
            }
            setBottomClip(paddingBottom);
            for (int i11 = 0; i11 < childCount; i11++) {
                View childAt = getChildAt(i11);
                if (childAt.getVisibility() != 8) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    int i12 = layoutParams.gravity;
                    if (i12 == -1) {
                        i12 = 51;
                    }
                    int i13 = i12 & 112;
                    int i14 = i12 & 7;
                    if (i14 == 1) {
                        i5 = ((i10 - measuredWidth) / 2) + layoutParams.leftMargin;
                        i6 = layoutParams.rightMargin;
                    } else if (i14 != 5) {
                        i7 = layoutParams.leftMargin + getPaddingLeft();
                        if (i13 == 16) {
                            if (i13 == 48) {
                                i9 = layoutParams.topMargin + getPaddingTop();
                            } else if (i13 != 80) {
                                i9 = layoutParams.topMargin;
                            } else {
                                i8 = ((i4 - paddingBottom) - i2) - measuredHeight;
                            }
                            editTextEmoji = ChatAttachAlert.this.commentTextView;
                            if ((editTextEmoji != null && editTextEmoji.isPopupView(childAt)) || (ChatAttachAlert.this.pollLayout != null && childAt == ChatAttachAlert.this.pollLayout.emojiView)) {
                                i9 = (!AndroidUtilities.isTablet() ? getMeasuredHeight() : getMeasuredHeight() + measureKeyboardHeight) - childAt.getMeasuredHeight();
                            }
                            childAt.layout(i7, i9, measuredWidth + i7, measuredHeight + i9);
                        } else {
                            i8 = ((((i4 - paddingBottom) - i2) - measuredHeight) / 2) + layoutParams.topMargin;
                        }
                        i9 = i8 - layoutParams.bottomMargin;
                        editTextEmoji = ChatAttachAlert.this.commentTextView;
                        if (editTextEmoji != null) {
                            i9 = (!AndroidUtilities.isTablet() ? getMeasuredHeight() : getMeasuredHeight() + measureKeyboardHeight) - childAt.getMeasuredHeight();
                            childAt.layout(i7, i9, measuredWidth + i7, measuredHeight + i9);
                        }
                        i9 = (!AndroidUtilities.isTablet() ? getMeasuredHeight() : getMeasuredHeight() + measureKeyboardHeight) - childAt.getMeasuredHeight();
                        childAt.layout(i7, i9, measuredWidth + i7, measuredHeight + i9);
                    } else {
                        i5 = ((i10 - measuredWidth) - layoutParams.rightMargin) - getPaddingRight();
                        i6 = ((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft;
                    }
                    i7 = i5 - i6;
                    if (i13 == 16) {
                    }
                    i9 = i8 - layoutParams.bottomMargin;
                    editTextEmoji = ChatAttachAlert.this.commentTextView;
                    if (editTextEmoji != null) {
                    }
                    i9 = (!AndroidUtilities.isTablet() ? getMeasuredHeight() : getMeasuredHeight() + measureKeyboardHeight) - childAt.getMeasuredHeight();
                    childAt.layout(i7, i9, measuredWidth + i7, measuredHeight + i9);
                }
            }
            notifyHeightChanged();
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            chatAttachAlert.updateLayout(chatAttachAlert.currentAttachLayout, false, 0);
            ChatAttachAlert chatAttachAlert2 = ChatAttachAlert.this;
            chatAttachAlert2.updateLayout(chatAttachAlert2.nextAttachLayout, false, 0);
        }

        /* JADX WARN: Removed duplicated region for block: B:43:0x00a9  */
        @Override // android.widget.FrameLayout, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onMeasure(int i, int i2) {
            int min;
            int size = getLayoutParams().height > 0 ? getLayoutParams().height : View.MeasureSpec.getSize(i2);
            if (Build.VERSION.SDK_INT >= 21) {
                ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                if (!chatAttachAlert.inBubbleMode) {
                    this.ignoreLayout = true;
                    setPadding(((BottomSheet) chatAttachAlert).backgroundPaddingLeft, AndroidUtilities.statusBarHeight, ((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft, 0);
                    this.ignoreLayout = false;
                }
            }
            getPaddingTop();
            int size2 = View.MeasureSpec.getSize(i) - (((BottomSheet) ChatAttachAlert.this).backgroundPaddingLeft * 2);
            if (!AndroidUtilities.isTablet()) {
                android.graphics.Point point = AndroidUtilities.displaySize;
                if (point.x > point.y) {
                    ChatAttachAlert.this.selectedMenuItem.setAdditionalYOffset(0);
                    ((FrameLayout.LayoutParams) ChatAttachAlert.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
                    ((FrameLayout.LayoutParams) ChatAttachAlert.this.doneItem.getLayoutParams()).height = ActionBar.getCurrentActionBarHeight();
                    this.ignoreLayout = true;
                    min = (int) (size2 / Math.min(4.5f, ChatAttachAlert.this.buttonsAdapter.getItemCount()));
                    if (ChatAttachAlert.this.attachItemSize != min) {
                        ChatAttachAlert.this.attachItemSize = min;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$3$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatAttachAlert.3.this.lambda$onMeasure$0();
                            }
                        });
                    }
                    this.ignoreLayout = false;
                    onMeasureInternal(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
                }
            }
            ChatAttachAlert.this.selectedMenuItem.setAdditionalYOffset(-AndroidUtilities.dp(3.0f));
            ((FrameLayout.LayoutParams) ChatAttachAlert.this.actionBarShadow.getLayoutParams()).topMargin = ActionBar.getCurrentActionBarHeight();
            ((FrameLayout.LayoutParams) ChatAttachAlert.this.doneItem.getLayoutParams()).height = ActionBar.getCurrentActionBarHeight();
            this.ignoreLayout = true;
            min = (int) (size2 / Math.min(4.5f, ChatAttachAlert.this.buttonsAdapter.getItemCount()));
            if (ChatAttachAlert.this.attachItemSize != min) {
            }
            this.ignoreLayout = false;
            onMeasureInternal(i, View.MeasureSpec.makeMeasureSpec(size, 1073741824));
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (ChatAttachAlert.this.currentAttachLayout.onContainerViewTouchEvent(motionEvent)) {
                return true;
            }
            return !ChatAttachAlert.this.isDismissed() && super.onTouchEvent(motionEvent);
        }

        @Override // android.view.View, android.view.ViewParent
        public void requestLayout() {
            if (this.ignoreLayout) {
                return;
            }
            super.requestLayout();
        }

        @Override // android.view.View
        public void setTranslationY(float f) {
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            float f2 = f + chatAttachAlert.currentPanTranslationY;
            if (((BottomSheet) chatAttachAlert).currentSheetAnimationType == 0) {
                this.initialTranslationY = f2;
            }
            if (((BottomSheet) ChatAttachAlert.this).currentSheetAnimationType == 1) {
                if (f2 < 0.0f) {
                    ChatAttachAlert.this.currentAttachLayout.setTranslationY(f2);
                    ChatAttachAlert chatAttachAlert2 = ChatAttachAlert.this;
                    if (chatAttachAlert2.avatarPicker != 0 || chatAttachAlert2.storyMediaPicker) {
                        chatAttachAlert2.headerView.setTranslationY((chatAttachAlert2.baseSelectedTextViewTranslationY + f2) - ChatAttachAlert.this.currentPanTranslationY);
                    }
                    ChatAttachAlert.this.buttonsRecyclerView.setTranslationY(0.0f);
                    f2 = 0.0f;
                } else {
                    ChatAttachAlert.this.currentAttachLayout.setTranslationY(0.0f);
                    RecyclerListView recyclerListView = ChatAttachAlert.this.buttonsRecyclerView;
                    recyclerListView.setTranslationY((-f2) + (recyclerListView.getMeasuredHeight() * (f2 / this.initialTranslationY)));
                }
                ((BottomSheet) ChatAttachAlert.this).containerView.invalidate();
            }
            super.setTranslationY(f2 - ChatAttachAlert.this.currentPanTranslationY);
            if (((BottomSheet) ChatAttachAlert.this).currentSheetAnimationType != 1) {
                ChatAttachAlert.this.currentAttachLayout.onContainerTranslationUpdated(ChatAttachAlert.this.currentPanTranslationY);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class AttachAlertLayout extends FrameLayout {
        protected ChatAttachAlert parentAlert;
        protected final Theme.ResourcesProvider resourcesProvider;

        public AttachAlertLayout(ChatAttachAlert chatAttachAlert, Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.resourcesProvider = resourcesProvider;
            this.parentAlert = chatAttachAlert;
        }

        public void applyCaption(CharSequence charSequence) {
        }

        public boolean canDismissWithTouchOutside() {
            return true;
        }

        public boolean canScheduleMessages() {
            return true;
        }

        public void checkColors() {
        }

        public int getButtonsHideOffset() {
            return AndroidUtilities.dp(needsActionBar() != 0 ? 12.0f : 17.0f);
        }

        public int getCurrentItemTop() {
            return 0;
        }

        public int getCustomActionBarBackground() {
            return 0;
        }

        public int getCustomBackground() {
            return 0;
        }

        public int getFirstOffset() {
            return 0;
        }

        public int getListTopPadding() {
            return 0;
        }

        public int getSelectedItemsCount() {
            return 0;
        }

        public ArrayList<ThemeDescription> getThemeDescriptions() {
            return null;
        }

        public int getThemedColor(int i) {
            return Theme.getColor(i, this.resourcesProvider);
        }

        public boolean hasCustomActionBarBackground() {
            return false;
        }

        public boolean hasCustomBackground() {
            return false;
        }

        public int needsActionBar() {
            return 0;
        }

        public boolean onBackPressed() {
            return false;
        }

        public void onButtonsTranslationYUpdated() {
        }

        public void onContainerTranslationUpdated(float f) {
        }

        public boolean onContainerViewTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        public void onDestroy() {
        }

        public boolean onDismiss() {
            return false;
        }

        public void onDismissWithButtonClick(int i) {
        }

        public boolean onDismissWithTouchOutside() {
            return true;
        }

        public void onHidden() {
        }

        public void onHide() {
        }

        public void onHideShowProgress(float f) {
        }

        public void onMenuItemClick(int i) {
        }

        public void onOpenAnimationEnd() {
        }

        public void onPanTransitionEnd() {
        }

        public void onPanTransitionStart(boolean z, int i) {
        }

        public void onPause() {
        }

        public void onPreMeasure(int i, int i2) {
        }

        public void onResume() {
        }

        public void onSelectedItemsCountChanged(int i) {
        }

        public boolean onSheetKeyDown(int i, KeyEvent keyEvent) {
            return false;
        }

        public void onShow(AttachAlertLayout attachAlertLayout) {
        }

        public void onShown() {
        }

        public void scrollToTop() {
        }

        public void sendSelectedItems(boolean z, int i, long j, boolean z2) {
        }

        public boolean shouldHideBottomButtons() {
            return true;
        }
    }

    /* loaded from: classes3.dex */
    public class AttachBotButton extends FrameLayout {
        private TLRPC.TL_attachMenuBot attachMenuBot;
        private AvatarDrawable avatarDrawable;
        private ValueAnimator checkAnimator;
        private Boolean checked;
        private float checkedState;
        private TLRPC.User currentUser;
        private int iconBackgroundColor;
        private BackupImageView imageView;
        private TextView nameTextView;
        private View selector;
        private int textColor;

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public class 1 extends BackupImageView {
            final /* synthetic */ ChatAttachAlert val$this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            1(Context context, ChatAttachAlert chatAttachAlert) {
                super(context);
                AttachBotButton.this = r1;
                this.val$this$0 = chatAttachAlert;
                this.imageReceiver.setDelegate(new ImageReceiver.ImageReceiverDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$AttachBotButton$1$$ExternalSyntheticLambda0
                    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                    public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
                        ChatAttachAlert.AttachBotButton.1.lambda$new$0(imageReceiver, z, z2, z3);
                    }

                    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                    public /* synthetic */ void didSetImageBitmap(int i, String str, Drawable drawable) {
                        ImageReceiver.ImageReceiverDelegate.-CC.$default$didSetImageBitmap(this, i, str, drawable);
                    }

                    @Override // org.telegram.messenger.ImageReceiver.ImageReceiverDelegate
                    public /* synthetic */ void onAnimationReady(ImageReceiver imageReceiver) {
                        ImageReceiver.ImageReceiverDelegate.-CC.$default$onAnimationReady(this, imageReceiver);
                    }
                });
            }

            public static /* synthetic */ void lambda$new$0(ImageReceiver imageReceiver, boolean z, boolean z2, boolean z3) {
                Drawable drawable = imageReceiver.getDrawable();
                if (drawable instanceof RLottieDrawable) {
                    RLottieDrawable rLottieDrawable = (RLottieDrawable) drawable;
                    rLottieDrawable.setCustomEndFrame(0);
                    rLottieDrawable.stop();
                    rLottieDrawable.setProgress(0.0f, false);
                }
            }

            @Override // android.view.View
            public void setScaleX(float f) {
                super.setScaleX(f);
                AttachBotButton.this.invalidate();
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AttachBotButton(Context context) {
            super(context);
            ChatAttachAlert.this = r10;
            this.avatarDrawable = new AvatarDrawable();
            setWillNotDraw(false);
            setFocusable(true);
            setFocusableInTouchMode(true);
            1 r1 = new 1(context, r10);
            this.imageView = r1;
            r1.setRoundRadius(AndroidUtilities.dp(25.0f));
            addView(this.imageView, LayoutHelper.createFrame(46, 46.0f, 49, 0.0f, 9.0f, 0.0f, 0.0f));
            if (Build.VERSION.SDK_INT >= 21) {
                View view = new View(context);
                this.selector = view;
                view.setBackground(Theme.createSelectorDrawable(r10.getThemedColor(Theme.key_dialogButtonSelector), 1, AndroidUtilities.dp(23.0f)));
                addView(this.selector, LayoutHelper.createFrame(46, 46.0f, 49, 0.0f, 9.0f, 0.0f, 0.0f));
            }
            TextView textView = new TextView(context);
            this.nameTextView = textView;
            textView.setTextSize(1, 12.0f);
            this.nameTextView.setGravity(49);
            this.nameTextView.setLines(1);
            this.nameTextView.setSingleLine(true);
            this.nameTextView.setEllipsize(TextUtils.TruncateAt.END);
            addView(this.nameTextView, LayoutHelper.createFrame(-1, -2.0f, 51, 6.0f, 60.0f, 6.0f, 0.0f));
        }

        public /* synthetic */ void lambda$updateCheckedState$0(ValueAnimator valueAnimator) {
            setCheckedState(((Float) valueAnimator.getAnimatedValue()).floatValue());
        }

        private void updateMargins() {
            ((ViewGroup.MarginLayoutParams) this.nameTextView.getLayoutParams()).topMargin = AndroidUtilities.dp(this.attachMenuBot != null ? 62.0f : 60.0f);
            ((ViewGroup.MarginLayoutParams) this.imageView.getLayoutParams()).topMargin = AndroidUtilities.dp(this.attachMenuBot != null ? 11.0f : 9.0f);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            updateCheckedState(false);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.attachMenuBot != null) {
                float scaleX = this.imageView.getScaleX() + (this.checkedState * 0.06f);
                float dp = AndroidUtilities.dp(23.0f) * scaleX;
                float left = this.imageView.getLeft() + (this.imageView.getMeasuredWidth() / 2.0f);
                float top = this.imageView.getTop() + (this.imageView.getMeasuredWidth() / 2.0f);
                ChatAttachAlert.this.attachButtonPaint.setColor(this.iconBackgroundColor);
                ChatAttachAlert.this.attachButtonPaint.setStyle(Paint.Style.STROKE);
                ChatAttachAlert.this.attachButtonPaint.setStrokeWidth(AndroidUtilities.dp(3.0f) * scaleX);
                ChatAttachAlert.this.attachButtonPaint.setAlpha(Math.round(this.checkedState * 255.0f));
                canvas.drawCircle(left, top, dp - (ChatAttachAlert.this.attachButtonPaint.getStrokeWidth() * 0.5f), ChatAttachAlert.this.attachButtonPaint);
                ChatAttachAlert.this.attachButtonPaint.setAlpha(NotificationCenter.messagePlayingSpeedChanged);
                ChatAttachAlert.this.attachButtonPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(left, top, dp - (AndroidUtilities.dp(5.0f) * this.checkedState), ChatAttachAlert.this.attachButtonPaint);
            }
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setEnabled(true);
            if (this.selector == null || !this.checked.booleanValue()) {
                return;
            }
            accessibilityNodeInfo.setCheckable(true);
            accessibilityNodeInfo.setChecked(true);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(ChatAttachAlert.this.attachItemSize, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(100.0f), 1073741824));
        }

        public void setAttachBot(TLRPC.User user, TLRPC.TL_attachMenuBot tL_attachMenuBot) {
            boolean z;
            if (user == null || tL_attachMenuBot == null) {
                return;
            }
            this.nameTextView.setTextColor(ChatAttachAlert.this.getThemedColor(Theme.key_dialogTextGray2));
            this.currentUser = user;
            this.nameTextView.setText(tL_attachMenuBot.short_name);
            this.avatarDrawable.setInfo(ChatAttachAlert.this.currentAccount, user);
            TLRPC.TL_attachMenuBotIcon animatedAttachMenuBotIcon = MediaDataController.getAnimatedAttachMenuBotIcon(tL_attachMenuBot);
            if (animatedAttachMenuBotIcon == null) {
                animatedAttachMenuBotIcon = MediaDataController.getStaticAttachMenuBotIcon(tL_attachMenuBot);
                z = false;
            } else {
                z = true;
            }
            if (animatedAttachMenuBotIcon != null) {
                this.textColor = ChatAttachAlert.this.getThemedColor(Theme.key_chat_attachContactText);
                this.iconBackgroundColor = ChatAttachAlert.this.getThemedColor(Theme.key_chat_attachContactBackground);
                Iterator<TLRPC.TL_attachMenuBotIconColor> it = animatedAttachMenuBotIcon.colors.iterator();
                while (it.hasNext()) {
                    TLRPC.TL_attachMenuBotIconColor next = it.next();
                    String str = next.name;
                    str.hashCode();
                    char c = 65535;
                    switch (str.hashCode()) {
                        case -1852424286:
                            if (str.equals(MediaDataController.ATTACH_MENU_BOT_COLOR_DARK_ICON)) {
                                c = 0;
                                break;
                            }
                            break;
                        case -1852094378:
                            if (str.equals(MediaDataController.ATTACH_MENU_BOT_COLOR_DARK_TEXT)) {
                                c = 1;
                                break;
                            }
                            break;
                        case -208896510:
                            if (str.equals(MediaDataController.ATTACH_MENU_BOT_COLOR_LIGHT_ICON)) {
                                c = 2;
                                break;
                            }
                            break;
                        case -208566602:
                            if (str.equals(MediaDataController.ATTACH_MENU_BOT_COLOR_LIGHT_TEXT)) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            if (!Theme.getCurrentTheme().isDark()) {
                                break;
                            } else {
                                this.iconBackgroundColor = next.color;
                                break;
                            }
                        case 1:
                            if (!Theme.getCurrentTheme().isDark()) {
                                break;
                            } else {
                                this.textColor = next.color;
                                break;
                            }
                        case 2:
                            if (!Theme.getCurrentTheme().isDark()) {
                                this.iconBackgroundColor = next.color;
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if (!Theme.getCurrentTheme().isDark()) {
                                this.textColor = next.color;
                                break;
                            } else {
                                break;
                            }
                    }
                }
                this.textColor = ColorUtils.setAlphaComponent(this.textColor, NotificationCenter.messagePlayingSpeedChanged);
                this.iconBackgroundColor = ColorUtils.setAlphaComponent(this.iconBackgroundColor, NotificationCenter.messagePlayingSpeedChanged);
                TLRPC.Document document = animatedAttachMenuBotIcon.icon;
                this.imageView.getImageReceiver().setAllowStartLottieAnimation(false);
                this.imageView.setImage(ImageLocation.getForDocument(document), String.valueOf(tL_attachMenuBot.bot_id), z ? "tgs" : "svg", DocumentObject.getSvgThumb(document, Theme.key_windowBackgroundGray, 1.0f), tL_attachMenuBot);
            }
            this.imageView.setSize(AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f));
            this.imageView.setColorFilter(new PorterDuffColorFilter(ChatAttachAlert.this.getThemedColor(Theme.key_chat_attachIcon), PorterDuff.Mode.SRC_IN));
            this.attachMenuBot = tL_attachMenuBot;
            this.selector.setVisibility(8);
            updateMargins();
            setCheckedState(0.0f);
            invalidate();
        }

        public void setCheckedState(float f) {
            this.checkedState = f;
            float f2 = 1.0f - (f * 0.06f);
            this.imageView.setScaleX(f2);
            this.imageView.setScaleY(f2);
            this.nameTextView.setTextColor(ColorUtils.blendARGB(ChatAttachAlert.this.getThemedColor(Theme.key_dialogTextGray2), this.textColor, this.checkedState));
            invalidate();
        }

        public void setUser(TLRPC.User user) {
            if (user == null) {
                return;
            }
            this.nameTextView.setTextColor(ChatAttachAlert.this.getThemedColor(Theme.key_dialogTextGray2));
            this.currentUser = user;
            this.nameTextView.setText(ContactsController.formatName(user.first_name, user.last_name));
            this.avatarDrawable.setInfo(ChatAttachAlert.this.currentAccount, user);
            this.imageView.setForUserOrChat(user, this.avatarDrawable);
            this.imageView.setSize(-1, -1);
            this.imageView.setColorFilter(null);
            this.attachMenuBot = null;
            this.selector.setVisibility(0);
            updateMargins();
            setCheckedState(0.0f);
            invalidate();
        }

        void updateCheckedState(boolean z) {
            boolean z2 = this.attachMenuBot != null && (-this.currentUser.id) == ChatAttachAlert.this.selectedId;
            Boolean bool = this.checked;
            if (bool != null && bool.booleanValue() == z2 && z) {
                return;
            }
            this.checked = Boolean.valueOf(z2);
            ValueAnimator valueAnimator = this.checkAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            RLottieDrawable lottieAnimation = this.imageView.getImageReceiver().getLottieAnimation();
            if (!z) {
                if (lottieAnimation != null) {
                    lottieAnimation.stop();
                    lottieAnimation.setProgress(0.0f, false);
                }
                setCheckedState(this.checked.booleanValue() ? 1.0f : 0.0f);
                return;
            }
            if (this.checked.booleanValue() && lottieAnimation != null) {
                lottieAnimation.setAutoRepeat(0);
                lottieAnimation.setCustomEndFrame(-1);
                lottieAnimation.setProgress(0.0f, false);
                lottieAnimation.start();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.checked.booleanValue() ? 0.0f : 1.0f, this.checked.booleanValue() ? 1.0f : 0.0f);
            this.checkAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$AttachBotButton$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ChatAttachAlert.AttachBotButton.this.lambda$updateCheckedState$0(valueAnimator2);
                }
            });
            this.checkAnimator.setDuration(200L);
            this.checkAnimator.start();
        }
    }

    /* loaded from: classes3.dex */
    public class AttachButton extends FrameLayout {
        private int backgroundKey;
        private Animator checkAnimator;
        private boolean checked;
        private float checkedState;
        private int currentId;
        private RLottieImageView imageView;
        private int textKey;
        private TextView textView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AttachButton(Context context) {
            super(context);
            ChatAttachAlert.this = r10;
            setWillNotDraw(false);
            setFocusable(true);
            RLottieImageView rLottieImageView = new RLottieImageView(context) { // from class: org.telegram.ui.Components.ChatAttachAlert.AttachButton.1
                {
                    AttachButton.this = this;
                }

                @Override // android.view.View
                public void setScaleX(float f) {
                    super.setScaleX(f);
                    AttachButton.this.invalidate();
                }
            };
            this.imageView = rLottieImageView;
            rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
            addView(this.imageView, LayoutHelper.createFrame(32, 32.0f, 49, 0.0f, 18.0f, 0.0f, 0.0f));
            TextView textView = new TextView(context);
            this.textView = textView;
            textView.setMaxLines(2);
            this.textView.setGravity(1);
            this.textView.setEllipsize(TextUtils.TruncateAt.END);
            this.textView.setTextColor(r10.getThemedColor(Theme.key_dialogTextGray2));
            this.textView.setTextSize(1, 12.0f);
            this.textView.setLineSpacing(-AndroidUtilities.dp(2.0f), 1.0f);
            this.textView.setImportantForAccessibility(2);
            addView(this.textView, LayoutHelper.createFrame(-1, -2.0f, 51, 0.0f, 62.0f, 0.0f, 0.0f));
        }

        @Override // android.view.View
        public boolean hasOverlappingRendering() {
            return false;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            updateCheckedState(false);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float scaleX = this.imageView.getScaleX() + (this.checkedState * 0.06f);
            float dp = AndroidUtilities.dp(23.0f) * scaleX;
            float left = this.imageView.getLeft() + (this.imageView.getMeasuredWidth() / 2.0f);
            float top = this.imageView.getTop() + (this.imageView.getMeasuredWidth() / 2.0f);
            ChatAttachAlert.this.attachButtonPaint.setColor(ChatAttachAlert.this.getThemedColor(this.backgroundKey));
            ChatAttachAlert.this.attachButtonPaint.setStyle(Paint.Style.STROKE);
            ChatAttachAlert.this.attachButtonPaint.setStrokeWidth(AndroidUtilities.dp(3.0f) * scaleX);
            ChatAttachAlert.this.attachButtonPaint.setAlpha(Math.round(this.checkedState * 255.0f));
            canvas.drawCircle(left, top, dp - (ChatAttachAlert.this.attachButtonPaint.getStrokeWidth() * 0.5f), ChatAttachAlert.this.attachButtonPaint);
            ChatAttachAlert.this.attachButtonPaint.setAlpha(NotificationCenter.messagePlayingSpeedChanged);
            ChatAttachAlert.this.attachButtonPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(left, top, dp - (AndroidUtilities.dp(5.0f) * this.checkedState), ChatAttachAlert.this.attachButtonPaint);
        }

        @Override // android.view.View
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setText(this.textView.getText());
            accessibilityNodeInfo.setEnabled(true);
            accessibilityNodeInfo.setSelected(this.checked);
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(ChatAttachAlert.this.attachItemSize, 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(84.0f), 1073741824));
        }

        public void setCheckedState(float f) {
            this.checkedState = f;
            float f2 = 1.0f - (f * 0.06f);
            this.imageView.setScaleX(f2);
            this.imageView.setScaleY(f2);
            this.textView.setTextColor(ColorUtils.blendARGB(ChatAttachAlert.this.getThemedColor(Theme.key_dialogTextGray2), ChatAttachAlert.this.getThemedColor(this.textKey), this.checkedState));
            invalidate();
        }

        public void setTextAndIcon(int i, CharSequence charSequence, Drawable drawable, int i2, int i3) {
            this.currentId = i;
            this.textView.setText(charSequence);
            this.imageView.setImageDrawable(drawable);
            this.backgroundKey = i2;
            this.textKey = i3;
            this.textView.setTextColor(ColorUtils.blendARGB(ChatAttachAlert.this.getThemedColor(Theme.key_dialogTextGray2), ChatAttachAlert.this.getThemedColor(this.textKey), this.checkedState));
        }

        public void setTextAndIcon(int i, CharSequence charSequence, RLottieDrawable rLottieDrawable, int i2, int i3) {
            this.currentId = i;
            this.textView.setText(charSequence);
            this.imageView.setAnimation(rLottieDrawable);
            this.backgroundKey = i2;
            this.textKey = i3;
            this.textView.setTextColor(ColorUtils.blendARGB(ChatAttachAlert.this.getThemedColor(Theme.key_dialogTextGray2), ChatAttachAlert.this.getThemedColor(this.textKey), this.checkedState));
        }

        void updateCheckedState(boolean z) {
            if (this.checked == (((long) this.currentId) == ChatAttachAlert.this.selectedId)) {
                return;
            }
            this.checked = ((long) this.currentId) == ChatAttachAlert.this.selectedId;
            Animator animator = this.checkAnimator;
            if (animator != null) {
                animator.cancel();
            }
            if (!z) {
                this.imageView.stopAnimation();
                this.imageView.setProgress(0.0f);
                setCheckedState(this.checked ? 1.0f : 0.0f);
                return;
            }
            if (this.checked) {
                this.imageView.setProgress(0.0f);
                this.imageView.playAnimation();
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "checkedState", this.checked ? 1.0f : 0.0f);
            this.checkAnimator = ofFloat;
            ofFloat.setDuration(200L);
            this.checkAnimator.start();
        }
    }

    /* loaded from: classes3.dex */
    public class ButtonsAdapter extends RecyclerListView.SelectionAdapter {
        private int attachBotsEndRow;
        private int attachBotsStartRow;
        private List attachMenuBots = new ArrayList();
        private int buttonsCount;
        private int contactButton;
        private int documentButton;
        private int galleryButton;
        private int locationButton;
        private Context mContext;
        private int musicButton;
        private int pollButton;
        private int quickRepliesButton;

        public ButtonsAdapter(Context context) {
            ChatAttachAlert.this = r1;
            this.mContext = context;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            int i = this.buttonsCount;
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            return (chatAttachAlert.editingMessageObject == null && (chatAttachAlert.baseFragment instanceof ChatActivity)) ? i + MediaDataController.getInstance(chatAttachAlert.currentAccount).inlineBots.size() : i;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            if (i < this.buttonsCount) {
                return (i < this.attachBotsStartRow || i >= this.attachBotsEndRow) ? 0 : 1;
            }
            return 1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
        public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void notifyDataSetChanged() {
            int i;
            this.buttonsCount = 0;
            this.galleryButton = -1;
            this.documentButton = -1;
            this.musicButton = -1;
            this.pollButton = -1;
            this.contactButton = -1;
            this.quickRepliesButton = -1;
            this.locationButton = -1;
            this.attachBotsStartRow = -1;
            this.attachBotsEndRow = -1;
            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
            if (chatAttachAlert.baseFragment instanceof ChatActivity) {
                MessageObject messageObject = chatAttachAlert.editingMessageObject;
                if (messageObject == null) {
                    this.buttonsCount = 1;
                    this.galleryButton = 0;
                    if (chatAttachAlert.photosEnabled || ChatAttachAlert.this.videosEnabled) {
                        BaseFragment baseFragment = ChatAttachAlert.this.baseFragment;
                        if ((baseFragment instanceof ChatActivity) && !((ChatActivity) baseFragment).isInScheduleMode() && !((ChatActivity) ChatAttachAlert.this.baseFragment).isSecretChat() && ((ChatActivity) ChatAttachAlert.this.baseFragment).getChatMode() != 5) {
                            ChatActivity chatActivity = (ChatActivity) ChatAttachAlert.this.baseFragment;
                            this.attachBotsStartRow = this.buttonsCount;
                            this.attachMenuBots.clear();
                            Iterator<TLRPC.TL_attachMenuBot> it = MediaDataController.getInstance(ChatAttachAlert.this.currentAccount).getAttachMenuBots().bots.iterator();
                            while (it.hasNext()) {
                                TLRPC.TL_attachMenuBot next = it.next();
                                if (next.show_in_attach_menu) {
                                    if (MediaDataController.canShowAttachMenuBot(next, chatActivity.getCurrentChat() != null ? chatActivity.getCurrentChat() : chatActivity.getCurrentUser())) {
                                        this.attachMenuBots.add(next);
                                    }
                                }
                            }
                            int size = this.buttonsCount + this.attachMenuBots.size();
                            this.buttonsCount = size;
                            this.attachBotsEndRow = size;
                        }
                    }
                    int i2 = this.buttonsCount;
                    this.buttonsCount = i2 + 1;
                    this.documentButton = i2;
                    if (ChatAttachAlert.this.plainTextEnabled) {
                        int i3 = this.buttonsCount;
                        this.buttonsCount = i3 + 1;
                        this.locationButton = i3;
                    }
                    if (ChatAttachAlert.this.pollsEnabled) {
                        int i4 = this.buttonsCount;
                        this.buttonsCount = i4 + 1;
                        this.pollButton = i4;
                    }
                    if (ChatAttachAlert.this.plainTextEnabled) {
                        int i5 = this.buttonsCount;
                        this.buttonsCount = i5 + 1;
                        this.contactButton = i5;
                    }
                    BaseFragment baseFragment2 = ChatAttachAlert.this.baseFragment;
                    TLRPC.User currentUser = baseFragment2 instanceof ChatActivity ? ((ChatActivity) baseFragment2).getCurrentUser() : null;
                    BaseFragment baseFragment3 = ChatAttachAlert.this.baseFragment;
                    if ((baseFragment3 instanceof ChatActivity) && ((ChatActivity) baseFragment3).getChatMode() == 0 && currentUser != null && !currentUser.bot && QuickRepliesController.getInstance(ChatAttachAlert.this.currentAccount).hasReplies()) {
                        int i6 = this.buttonsCount;
                        this.buttonsCount = i6 + 1;
                        this.quickRepliesButton = i6;
                    }
                } else if ((!messageObject.isMusic() && !ChatAttachAlert.this.editingMessageObject.isDocument()) || !ChatAttachAlert.this.editingMessageObject.hasValidGroupId()) {
                    int i7 = this.buttonsCount;
                    this.galleryButton = i7;
                    this.documentButton = i7 + 1;
                    this.buttonsCount = i7 + 3;
                    this.musicButton = i7 + 2;
                } else if (!ChatAttachAlert.this.editingMessageObject.isMusic()) {
                    int i8 = this.buttonsCount;
                    this.buttonsCount = i8 + 1;
                    this.documentButton = i8;
                }
                i = this.buttonsCount;
                this.buttonsCount = i + 1;
                this.musicButton = i;
            } else {
                this.galleryButton = 0;
                i = 2;
                this.buttonsCount = 2;
                this.documentButton = 1;
                if (chatAttachAlert.allowEnterCaption) {
                    this.buttonsCount = 3;
                    this.musicButton = i;
                }
            }
            super.notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            int i2;
            String string;
            RLottieDrawable rLottieDrawable;
            int i3;
            int i4;
            int i5;
            String string2;
            RLottieDrawable rLottieDrawable2;
            int i6;
            int i7;
            int i8;
            Integer valueOf;
            int itemViewType = viewHolder.getItemViewType();
            int i9 = 1;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    return;
                }
                AttachBotButton attachBotButton = (AttachBotButton) viewHolder.itemView;
                int i10 = this.attachBotsStartRow;
                if (i < i10 || i >= this.attachBotsEndRow) {
                    int i11 = i - this.buttonsCount;
                    attachBotButton.setTag(Integer.valueOf(i11));
                    attachBotButton.setUser(MessagesController.getInstance(ChatAttachAlert.this.currentAccount).getUser(Long.valueOf(MediaDataController.getInstance(ChatAttachAlert.this.currentAccount).inlineBots.get(i11).peer.user_id)));
                    return;
                }
                int i12 = i - i10;
                attachBotButton.setTag(Integer.valueOf(i12));
                TLRPC.TL_attachMenuBot tL_attachMenuBot = (TLRPC.TL_attachMenuBot) this.attachMenuBots.get(i12);
                attachBotButton.setAttachBot(MessagesController.getInstance(ChatAttachAlert.this.currentAccount).getUser(Long.valueOf(tL_attachMenuBot.bot_id)), tL_attachMenuBot);
                return;
            }
            AttachButton attachButton = (AttachButton) viewHolder.itemView;
            if (i != this.galleryButton) {
                int i13 = 4;
                if (i != this.documentButton) {
                    if (i == this.locationButton) {
                        attachButton.setTextAndIcon(6, (CharSequence) LocaleController.getString("ChatLocation", R.string.ChatLocation), Theme.chat_attachButtonDrawables[4], Theme.key_chat_attachLocationBackground, Theme.key_chat_attachLocationText);
                        i2 = 6;
                    } else {
                        i13 = 3;
                        if (i == this.musicButton) {
                            string2 = LocaleController.getString("AttachMusic", R.string.AttachMusic);
                            rLottieDrawable2 = Theme.chat_attachButtonDrawables[1];
                            i6 = Theme.key_chat_attachAudioBackground;
                            i7 = Theme.key_chat_attachAudioText;
                            i8 = 3;
                        } else {
                            i9 = 5;
                            if (i == this.pollButton) {
                                attachButton.setTextAndIcon(9, (CharSequence) LocaleController.getString("Poll", R.string.Poll), Theme.chat_attachButtonDrawables[5], Theme.key_chat_attachPollBackground, Theme.key_chat_attachPollText);
                                i2 = 9;
                            } else if (i == this.contactButton) {
                                string = LocaleController.getString("AttachContact", R.string.AttachContact);
                                rLottieDrawable = Theme.chat_attachButtonDrawables[3];
                                i3 = Theme.key_chat_attachContactBackground;
                                i4 = Theme.key_chat_attachContactText;
                                i5 = 5;
                            } else if (i != this.quickRepliesButton) {
                                return;
                            } else {
                                attachButton.setTextAndIcon(11, LocaleController.getString(R.string.AttachQuickReplies), ChatAttachAlert.this.getContext().getResources().getDrawable(R.drawable.ic_ab_reply).mutate(), Theme.key_chat_attachContactBackground, Theme.key_chat_attachContactText);
                                i2 = 11;
                            }
                        }
                    }
                    valueOf = Integer.valueOf(i2);
                    attachButton.setTag(valueOf);
                }
                string2 = LocaleController.getString("ChatDocument", R.string.ChatDocument);
                rLottieDrawable2 = Theme.chat_attachButtonDrawables[2];
                i6 = Theme.key_chat_attachFileBackground;
                i7 = Theme.key_chat_attachFileText;
                i8 = 4;
                attachButton.setTextAndIcon(i8, (CharSequence) string2, rLottieDrawable2, i6, i7);
                valueOf = Integer.valueOf(i13);
                attachButton.setTag(valueOf);
            }
            string = LocaleController.getString("ChatGallery", R.string.ChatGallery);
            rLottieDrawable = Theme.chat_attachButtonDrawables[0];
            i3 = Theme.key_chat_attachGalleryBackground;
            i4 = Theme.key_chat_attachGalleryText;
            i5 = 1;
            attachButton.setTextAndIcon(i5, (CharSequence) string, rLottieDrawable, i3, i4);
            valueOf = Integer.valueOf(i9);
            attachButton.setTag(valueOf);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View attachBotButton = i != 0 ? new AttachBotButton(this.mContext) : new AttachButton(this.mContext);
            attachBotButton.setImportantForAccessibility(1);
            attachBotButton.setFocusable(true);
            return new RecyclerListView.Holder(attachBotButton);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
            ChatAttachAlert.this.applyAttachButtonColors(viewHolder.itemView);
        }
    }

    /* loaded from: classes3.dex */
    public interface ChatAttachViewDelegate {

        /* loaded from: classes3.dex */
        public abstract /* synthetic */ class -CC {
            public static void $default$didSelectBot(ChatAttachViewDelegate chatAttachViewDelegate, TLRPC.User user) {
            }

            public static void $default$doOnIdle(ChatAttachViewDelegate chatAttachViewDelegate, Runnable runnable) {
                runnable.run();
            }

            public static boolean $default$needEnterComment(ChatAttachViewDelegate chatAttachViewDelegate) {
                return false;
            }

            public static void $default$onCameraOpened(ChatAttachViewDelegate chatAttachViewDelegate) {
            }

            public static void $default$onWallpaperSelected(ChatAttachViewDelegate chatAttachViewDelegate, Object obj) {
            }

            public static void $default$openAvatarsSearch(ChatAttachViewDelegate chatAttachViewDelegate) {
            }

            public static boolean $default$selectItemOnClicking(ChatAttachViewDelegate chatAttachViewDelegate) {
                return false;
            }

            public static void $default$sendAudio(ChatAttachViewDelegate chatAttachViewDelegate, ArrayList arrayList, CharSequence charSequence, boolean z, int i, long j, boolean z2) {
            }
        }

        void didPressedButton(int i, boolean z, boolean z2, int i2, long j, boolean z3, boolean z4);

        void didSelectBot(TLRPC.User user);

        void doOnIdle(Runnable runnable);

        boolean needEnterComment();

        void onCameraOpened();

        void onWallpaperSelected(Object obj);

        void openAvatarsSearch();

        boolean selectItemOnClicking();

        void sendAudio(ArrayList arrayList, CharSequence charSequence, boolean z, int i, long j, boolean z2);
    }

    public ChatAttachAlert(Context context, BaseFragment baseFragment, boolean z, boolean z2) {
        this(context, baseFragment, z, z2, true, null);
    }

    public ChatAttachAlert(final Context context, final BaseFragment baseFragment, boolean z, final boolean z2, boolean z3, final Theme.ResourcesProvider resourcesProvider) {
        super(context, false, resourcesProvider);
        int i;
        this.canOpenPreview = false;
        this.isSoundPicker = false;
        this.isStoryLocationPicker = false;
        this.isBizLocationPicker = false;
        this.isStoryAudioPicker = false;
        this.translationProgress = 0.0f;
        this.ATTACH_ALERT_LAYOUT_TRANSLATION = new AnimationProperties.FloatProperty("translation") { // from class: org.telegram.ui.Components.ChatAttachAlert.2
            {
                ChatAttachAlert.this = this;
            }

            @Override // android.util.Property
            public Float get(AttachAlertLayout attachAlertLayout) {
                return Float.valueOf(ChatAttachAlert.this.translationProgress);
            }

            @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
            public void setValue(AttachAlertLayout attachAlertLayout, float f) {
                ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                chatAttachAlert.translationProgress = f;
                if (chatAttachAlert.nextAttachLayout == null) {
                    return;
                }
                if ((ChatAttachAlert.this.nextAttachLayout instanceof ChatAttachAlertPhotoLayoutPreview) || (ChatAttachAlert.this.currentAttachLayout instanceof ChatAttachAlertPhotoLayoutPreview)) {
                    int max = Math.max(ChatAttachAlert.this.nextAttachLayout.getWidth(), ChatAttachAlert.this.currentAttachLayout.getWidth());
                    if (ChatAttachAlert.this.nextAttachLayout instanceof ChatAttachAlertPhotoLayoutPreview) {
                        ChatAttachAlert.this.currentAttachLayout.setTranslationX((-max) * f);
                        ChatAttachAlert.this.nextAttachLayout.setTranslationX((1.0f - f) * max);
                    } else {
                        ChatAttachAlert.this.currentAttachLayout.setTranslationX(max * f);
                        ChatAttachAlert.this.nextAttachLayout.setTranslationX((-max) * (1.0f - f));
                    }
                } else {
                    ChatAttachAlert.this.nextAttachLayout.setAlpha(f);
                    ChatAttachAlert.this.nextAttachLayout.onHideShowProgress(f);
                    if (ChatAttachAlert.this.nextAttachLayout == ChatAttachAlert.this.pollLayout || ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.pollLayout) {
                        ChatAttachAlert chatAttachAlert2 = ChatAttachAlert.this;
                        chatAttachAlert2.updateSelectedPosition(chatAttachAlert2.nextAttachLayout == ChatAttachAlert.this.pollLayout ? 1 : 0);
                    }
                    ChatAttachAlert.this.nextAttachLayout.setTranslationY(AndroidUtilities.dp(78.0f) * f);
                    ChatAttachAlert.this.currentAttachLayout.onHideShowProgress(1.0f - Math.min(1.0f, f / 0.7f));
                    ChatAttachAlert.this.currentAttachLayout.onContainerTranslationUpdated(ChatAttachAlert.this.currentPanTranslationY);
                }
                if (ChatAttachAlert.this.viewChangeAnimator != null) {
                    ChatAttachAlert.this.updateSelectedPosition(1);
                }
                ((BottomSheet) ChatAttachAlert.this).containerView.invalidate();
            }
        };
        this.layouts = new AttachAlertLayout[8];
        this.botAttachLayouts = new LongSparseArray();
        this.commentTextViewLocation = new int[2];
        this.textPaint = new TextPaint(1);
        this.rect = new RectF();
        this.paint = new Paint(1);
        this.sendButtonEnabled = true;
        this.sendButtonEnabledProgress = 1.0f;
        this.cornerRadius = 1.0f;
        this.botButtonProgressWasVisible = false;
        this.botButtonWasVisible = false;
        int i2 = UserConfig.selectedAccount;
        this.currentAccount = i2;
        this.documentsEnabled = true;
        this.photosEnabled = true;
        this.videosEnabled = true;
        this.musicEnabled = true;
        this.pollsEnabled = true;
        this.plainTextEnabled = true;
        this.maxSelectedPhotos = -1;
        this.allowOrder = true;
        this.attachItemSize = AndroidUtilities.dp(85.0f);
        this.decelerateInterpolator = new DecelerateInterpolator();
        this.scrollOffsetY = new int[2];
        this.attachButtonPaint = new Paint(1);
        this.captionLimitBulletinShown = false;
        this.exclusionRects = new ArrayList();
        this.exclustionRect = new android.graphics.Rect();
        this.ATTACH_ALERT_PROGRESS = new AnimationProperties.FloatProperty("openProgress") { // from class: org.telegram.ui.Components.ChatAttachAlert.21
            private float openProgress;

            {
                ChatAttachAlert.this = this;
            }

            @Override // android.util.Property
            public Float get(ChatAttachAlert chatAttachAlert) {
                return Float.valueOf(this.openProgress);
            }

            @Override // org.telegram.ui.Components.AnimationProperties.FloatProperty
            public void setValue(ChatAttachAlert chatAttachAlert, float f) {
                float f2;
                View view;
                int childCount = ChatAttachAlert.this.buttonsRecyclerView.getChildCount();
                for (int i3 = 0; i3 < childCount; i3++) {
                    float f3 = (3 - i3) * 32.0f;
                    View childAt = ChatAttachAlert.this.buttonsRecyclerView.getChildAt(i3);
                    if (f > f3) {
                        float f4 = f - f3;
                        if (f4 <= 200.0f) {
                            float f5 = f4 / 200.0f;
                            f2 = CubicBezierInterpolator.EASE_OUT.getInterpolation(f5) * 1.1f;
                            childAt.setAlpha(CubicBezierInterpolator.EASE_BOTH.getInterpolation(f5));
                        } else {
                            childAt.setAlpha(1.0f);
                            float f6 = f4 - 200.0f;
                            f2 = f6 <= 100.0f ? 1.1f - (CubicBezierInterpolator.EASE_IN.getInterpolation(f6 / 100.0f) * 0.1f) : 1.0f;
                        }
                    } else {
                        f2 = 0.0f;
                    }
                    if (childAt instanceof AttachButton) {
                        AttachButton attachButton = (AttachButton) childAt;
                        attachButton.textView.setScaleX(f2);
                        attachButton.textView.setScaleY(f2);
                        attachButton.imageView.setScaleX(f2);
                        view = attachButton.imageView;
                    } else if (childAt instanceof AttachBotButton) {
                        AttachBotButton attachBotButton = (AttachBotButton) childAt;
                        attachBotButton.nameTextView.setScaleX(f2);
                        attachBotButton.nameTextView.setScaleY(f2);
                        attachBotButton.imageView.setScaleX(f2);
                        view = attachBotButton.imageView;
                    }
                    view.setScaleY(f2);
                }
            }
        };
        this.allowDrawContent = true;
        this.sent = false;
        this.confirmationAlertShown = false;
        this.allowPassConfirmationAlert = false;
        boolean z4 = baseFragment instanceof ChatActivity;
        if (z4) {
            setImageReceiverNumLevel(0, 4);
        }
        this.forceDarkTheme = z;
        this.showingFromDialog = z2;
        this.drawNavigationBar = true;
        this.inBubbleMode = z4 && baseFragment.isInBubbleMode();
        this.openInterpolator = new OvershootInterpolator(0.7f);
        this.baseFragment = baseFragment;
        this.useSmoothKeyboard = true;
        setDelegate(this);
        NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.reloadInlineHints);
        NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.attachMenuBotsDidLoad);
        NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        NotificationCenter.getInstance(i2).addObserver(this, NotificationCenter.quickRepliesUpdated);
        this.exclusionRects.add(this.exclustionRect);
        3 r0 = new 3(context, z);
        this.sizeNotifierFrameLayout = r0;
        r0.setDelegate(new SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert.4
            {
                ChatAttachAlert.this = this;
            }

            @Override // org.telegram.ui.Components.SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate
            public void onSizeChanged(int i3, boolean z5) {
                if (ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.photoPreviewLayout) {
                    ChatAttachAlert.this.currentAttachLayout.invalidate();
                }
            }
        });
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierFrameLayout;
        this.containerView = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setWillNotDraw(false);
        this.containerView.setClipChildren(false);
        this.containerView.setClipToPadding(false);
        ViewGroup viewGroup = this.containerView;
        int i3 = this.backgroundPaddingLeft;
        viewGroup.setPadding(i3, 0, i3, 0);
        ActionBar actionBar = new ActionBar(context, resourcesProvider) { // from class: org.telegram.ui.Components.ChatAttachAlert.5
            {
                ChatAttachAlert.this = this;
            }

            @Override // android.view.View
            public void setAlpha(float f) {
                float alpha = getAlpha();
                super.setAlpha(f);
                if (alpha != f) {
                    ((BottomSheet) ChatAttachAlert.this).containerView.invalidate();
                    if (ChatAttachAlert.this.frameLayout2 != null) {
                        ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                        if (chatAttachAlert.buttonsRecyclerView != null) {
                            if (chatAttachAlert.frameLayout2.getTag() != null) {
                                if (ChatAttachAlert.this.currentAttachLayout == null) {
                                    float f2 = f != 0.0f ? 0.0f : 1.0f;
                                    if (ChatAttachAlert.this.buttonsRecyclerView.getAlpha() != f2) {
                                        ChatAttachAlert.this.buttonsRecyclerView.setAlpha(f2);
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            if (ChatAttachAlert.this.currentAttachLayout == null || ChatAttachAlert.this.currentAttachLayout.shouldHideBottomButtons()) {
                                float f3 = 1.0f - f;
                                ChatAttachAlert.this.buttonsRecyclerView.setAlpha(f3);
                                ChatAttachAlert.this.shadow.setAlpha(f3);
                                ChatAttachAlert.this.buttonsRecyclerView.setTranslationY(AndroidUtilities.dp(44.0f) * f);
                            }
                            ChatAttachAlert.this.frameLayout2.setTranslationY(AndroidUtilities.dp(48.0f) * f);
                            ChatAttachAlert.this.shadow.setTranslationY((AndroidUtilities.dp(84.0f) * f) + ChatAttachAlert.this.botMainButtonOffsetY);
                        }
                    }
                }
            }
        };
        this.actionBar = actionBar;
        int i4 = Theme.key_dialogBackground;
        actionBar.setBackgroundColor(getThemedColor(i4));
        this.actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        ActionBar actionBar2 = this.actionBar;
        int i5 = Theme.key_dialogTextBlack;
        actionBar2.setItemsColor(getThemedColor(i5), false);
        ActionBar actionBar3 = this.actionBar;
        int i6 = Theme.key_dialogButtonSelector;
        actionBar3.setItemsBackgroundColor(getThemedColor(i6), false);
        this.actionBar.setTitleColor(getThemedColor(i5));
        this.actionBar.setOccupyStatusBar(false);
        this.actionBar.setAlpha(0.0f);
        this.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() { // from class: org.telegram.ui.Components.ChatAttachAlert.6
            {
                ChatAttachAlert.this = this;
            }

            @Override // org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick
            public void onItemClick(int i7) {
                if (i7 != -1) {
                    ChatAttachAlert.this.currentAttachLayout.onMenuItemClick(i7);
                } else if (ChatAttachAlert.this.currentAttachLayout.onBackPressed()) {
                } else {
                    ChatAttachAlert.this.dismiss();
                }
            }
        });
        ActionBarMenuItem actionBarMenuItem = new ActionBarMenuItem(context, null, 0, getThemedColor(i5), false, resourcesProvider);
        this.selectedMenuItem = actionBarMenuItem;
        actionBarMenuItem.setLongClickEnabled(false);
        ActionBarMenuItem actionBarMenuItem2 = this.selectedMenuItem;
        int i7 = R.drawable.ic_ab_other;
        actionBarMenuItem2.setIcon(i7);
        ActionBarMenuItem actionBarMenuItem3 = this.selectedMenuItem;
        int i8 = R.string.AccDescrMoreOptions;
        actionBarMenuItem3.setContentDescription(LocaleController.getString(i8));
        this.selectedMenuItem.setVisibility(4);
        this.selectedMenuItem.setAlpha(0.0f);
        this.selectedMenuItem.setSubMenuOpenSide(2);
        this.selectedMenuItem.setDelegate(new ActionBarMenuItem.ActionBarMenuItemDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda11
            @Override // org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemDelegate
            public final void onItemClick(int i9) {
                ChatAttachAlert.this.lambda$new$0(i9);
            }
        });
        this.selectedMenuItem.setAdditionalYOffset(AndroidUtilities.dp(72.0f));
        this.selectedMenuItem.setTranslationX(AndroidUtilities.dp(6.0f));
        this.selectedMenuItem.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(i6), 6));
        this.selectedMenuItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda15
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatAttachAlert.this.lambda$new$1(view);
            }
        });
        ActionBarMenuItem actionBarMenuItem4 = new ActionBarMenuItem(context, null, 0, getThemedColor(Theme.key_windowBackgroundWhiteBlueHeader), true, resourcesProvider);
        this.doneItem = actionBarMenuItem4;
        actionBarMenuItem4.setLongClickEnabled(false);
        this.doneItem.setText(LocaleController.getString(R.string.Create).toUpperCase());
        this.doneItem.setVisibility(4);
        this.doneItem.setAlpha(0.0f);
        this.doneItem.setTranslationX(-AndroidUtilities.dp(12.0f));
        this.doneItem.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(i6), 3));
        this.doneItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatAttachAlert.this.lambda$new$2(view);
            }
        });
        if (baseFragment != null) {
            i = i6;
            ActionBarMenuItem actionBarMenuItem5 = new ActionBarMenuItem(context, null, 0, getThemedColor(i5), false, resourcesProvider);
            this.searchItem = actionBarMenuItem5;
            actionBarMenuItem5.setLongClickEnabled(false);
            this.searchItem.setIcon(R.drawable.ic_ab_search);
            this.searchItem.setContentDescription(LocaleController.getString(R.string.Search));
            this.searchItem.setVisibility(4);
            this.searchItem.setAlpha(0.0f);
            this.searchItem.setTranslationX(-AndroidUtilities.dp(42.0f));
            this.searchItem.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(i), 6));
            this.searchItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda17
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ChatAttachAlert.this.lambda$new$3(z2, view);
                }
            });
        } else {
            i = i6;
        }
        ActionBarMenuItem actionBarMenuItem6 = new ActionBarMenuItem(context, null, 0, getThemedColor(i5), false, resourcesProvider);
        this.optionsItem = actionBarMenuItem6;
        actionBarMenuItem6.setLongClickEnabled(false);
        this.optionsItem.setIcon(i7);
        this.optionsItem.setContentDescription(LocaleController.getString(i8));
        this.optionsItem.setVisibility(8);
        this.optionsItem.setBackground(Theme.createSelectorDrawable(getThemedColor(i), 3));
        this.optionsItem.addSubItem(1, R.drawable.msg_addbot, LocaleController.getString(R.string.StickerCreateEmpty)).setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda18
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatAttachAlert.this.lambda$new$4(resourcesProvider, view);
            }
        });
        this.optionsItem.setMenuYOffset(AndroidUtilities.dp(-12.0f));
        this.optionsItem.setAdditionalXOffset(AndroidUtilities.dp(12.0f));
        this.optionsItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda19
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatAttachAlert.this.lambda$new$5(view);
            }
        });
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.ChatAttachAlert.9
            {
                ChatAttachAlert.this = this;
            }

            @Override // android.view.ViewGroup
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                if (ChatAttachAlert.this.headerView.getVisibility() != 0) {
                    return false;
                }
                return super.onInterceptTouchEvent(motionEvent);
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (ChatAttachAlert.this.headerView.getVisibility() != 0) {
                    return false;
                }
                return super.onTouchEvent(motionEvent);
            }

            @Override // android.view.View
            public void setAlpha(float f) {
                super.setAlpha(f);
                ChatAttachAlert.this.updateSelectedPosition(0);
                ((BottomSheet) ChatAttachAlert.this).containerView.invalidate();
            }
        };
        this.headerView = frameLayout;
        frameLayout.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda20
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatAttachAlert.this.lambda$new$6(view);
            }
        });
        this.headerView.setAlpha(0.0f);
        this.headerView.setVisibility(4);
        LinearLayout linearLayout = new LinearLayout(context);
        this.selectedView = linearLayout;
        linearLayout.setOrientation(0);
        this.selectedView.setGravity(16);
        TextView textView = new TextView(context);
        this.selectedTextView = textView;
        textView.setTextColor(getThemedColor(i5));
        this.selectedTextView.setTextSize(1, 16.0f);
        this.selectedTextView.setTypeface(AndroidUtilities.bold());
        this.selectedTextView.setGravity(19);
        this.selectedTextView.setMaxLines(1);
        this.selectedTextView.setEllipsize(TextUtils.TruncateAt.END);
        this.selectedView.addView(this.selectedTextView, LayoutHelper.createLinear(-2, -2, 16));
        this.selectedArrowImageView = new ImageView(context);
        Drawable mutate = getContext().getResources().getDrawable(R.drawable.attach_arrow_right).mutate();
        int themedColor = getThemedColor(i5);
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        mutate.setColorFilter(new PorterDuffColorFilter(themedColor, mode));
        this.selectedArrowImageView.setImageDrawable(mutate);
        this.selectedArrowImageView.setVisibility(8);
        this.selectedView.addView(this.selectedArrowImageView, LayoutHelper.createLinear(-2, -2, 16, 4, 1, 0, 0));
        this.selectedView.setAlpha(1.0f);
        this.headerView.addView(this.selectedView, LayoutHelper.createFrame(-2, -1.0f));
        LinearLayout linearLayout2 = new LinearLayout(context);
        this.mediaPreviewView = linearLayout2;
        linearLayout2.setOrientation(0);
        this.mediaPreviewView.setGravity(16);
        ImageView imageView = new ImageView(context);
        Drawable mutate2 = getContext().getResources().getDrawable(R.drawable.attach_arrow_left).mutate();
        mutate2.setColorFilter(new PorterDuffColorFilter(getThemedColor(i5), mode));
        imageView.setImageDrawable(mutate2);
        this.mediaPreviewView.addView(imageView, LayoutHelper.createLinear(-2, -2, 16, 0, 1, 4, 0));
        TextView textView2 = new TextView(context);
        this.mediaPreviewTextView = textView2;
        textView2.setTextColor(getThemedColor(i5));
        this.mediaPreviewTextView.setTextSize(1, 16.0f);
        this.mediaPreviewTextView.setTypeface(AndroidUtilities.bold());
        this.mediaPreviewTextView.setGravity(19);
        this.mediaPreviewTextView.setText(LocaleController.getString("AttachMediaPreview", R.string.AttachMediaPreview));
        this.mediaPreviewView.setAlpha(0.0f);
        this.mediaPreviewView.addView(this.mediaPreviewTextView, LayoutHelper.createLinear(-2, -2, 16));
        this.headerView.addView(this.mediaPreviewView, LayoutHelper.createFrame(-2, -1.0f));
        AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout = new ChatAttachAlertPhotoLayout(this, context, z, z3, resourcesProvider);
        this.photoLayout = chatAttachAlertPhotoLayout;
        attachAlertLayoutArr[0] = chatAttachAlertPhotoLayout;
        chatAttachAlertPhotoLayout.setTranslationX(0.0f);
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout2 = this.photoLayout;
        this.currentAttachLayout = chatAttachAlertPhotoLayout2;
        this.selectedId = 1L;
        this.containerView.addView(chatAttachAlertPhotoLayout2, LayoutHelper.createFrame(-1, -1.0f));
        this.containerView.addView(this.headerView, LayoutHelper.createFrame(-1, -2.0f, 51, 23.0f, 0.0f, 12.0f, 0.0f));
        this.containerView.addView(this.actionBar, LayoutHelper.createFrame(-1, -2.0f));
        this.containerView.addView(this.selectedMenuItem, LayoutHelper.createFrame(48, 48, 53));
        ActionBarMenuItem actionBarMenuItem7 = this.searchItem;
        if (actionBarMenuItem7 != null) {
            this.containerView.addView(actionBarMenuItem7, LayoutHelper.createFrame(48, 48, 53));
        }
        ActionBarMenuItem actionBarMenuItem8 = this.optionsItem;
        if (actionBarMenuItem8 != null) {
            this.headerView.addView(actionBarMenuItem8, LayoutHelper.createFrame(32, 32.0f, 21, 0.0f, 0.0f, 0.0f, 8.0f));
        }
        this.containerView.addView(this.doneItem, LayoutHelper.createFrame(-2, 48, 53));
        View view = new View(context);
        this.actionBarShadow = view;
        view.setAlpha(0.0f);
        this.actionBarShadow.setBackgroundColor(getThemedColor(Theme.key_dialogShadowLine));
        this.containerView.addView(this.actionBarShadow, LayoutHelper.createFrame(-1, 1.0f));
        View view2 = new View(context);
        this.shadow = view2;
        view2.setBackgroundResource(R.drawable.attach_shadow);
        this.shadow.getBackground().setColorFilter(new PorterDuffColorFilter(-16777216, mode));
        this.containerView.addView(this.shadow, LayoutHelper.createFrame(-1, 2.0f, 83, 0.0f, 0.0f, 0.0f, 84.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context) { // from class: org.telegram.ui.Components.ChatAttachAlert.10
            {
                ChatAttachAlert.this = this;
            }

            @Override // org.telegram.ui.Components.RecyclerListView, android.view.View
            public void setTranslationY(float f) {
                super.setTranslationY(f);
                ChatAttachAlert.this.currentAttachLayout.onButtonsTranslationYUpdated();
            }
        };
        this.buttonsRecyclerView = recyclerListView;
        ButtonsAdapter buttonsAdapter = new ButtonsAdapter(context);
        this.buttonsAdapter = buttonsAdapter;
        recyclerListView.setAdapter(buttonsAdapter);
        RecyclerListView recyclerListView2 = this.buttonsRecyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, 0, false);
        this.buttonsLayoutManager = linearLayoutManager;
        recyclerListView2.setLayoutManager(linearLayoutManager);
        this.buttonsRecyclerView.setVerticalScrollBarEnabled(false);
        this.buttonsRecyclerView.setHorizontalScrollBarEnabled(false);
        this.buttonsRecyclerView.setItemAnimator(null);
        this.buttonsRecyclerView.setLayoutAnimation(null);
        this.buttonsRecyclerView.setGlowColor(getThemedColor(Theme.key_dialogScrollGlow));
        this.buttonsRecyclerView.setBackgroundColor(getThemedColor(i4));
        this.buttonsRecyclerView.setImportantForAccessibility(1);
        this.containerView.addView(this.buttonsRecyclerView, LayoutHelper.createFrame(-1, 84, 83));
        this.buttonsRecyclerView.setOnItemClickListener(new RecyclerListView.OnItemClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda21
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
            public final void onItemClick(View view3, int i9) {
                ChatAttachAlert.this.lambda$new$12(resourcesProvider, view3, i9);
            }
        });
        this.buttonsRecyclerView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda22
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public final boolean onItemClick(View view3, int i9) {
                boolean lambda$new$13;
                lambda$new$13 = ChatAttachAlert.this.lambda$new$13(view3, i9);
                return lambda$new$13;
            }
        });
        TextView textView3 = new TextView(context);
        this.botMainButtonTextView = textView3;
        textView3.setVisibility(8);
        this.botMainButtonTextView.setAlpha(0.0f);
        this.botMainButtonTextView.setSingleLine();
        this.botMainButtonTextView.setGravity(17);
        this.botMainButtonTextView.setTypeface(AndroidUtilities.bold());
        int dp = AndroidUtilities.dp(16.0f);
        this.botMainButtonTextView.setPadding(dp, 0, dp, 0);
        this.botMainButtonTextView.setTextSize(1, 14.0f);
        this.botMainButtonTextView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda23
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                ChatAttachAlert.this.lambda$new$14(view3);
            }
        });
        this.containerView.addView(this.botMainButtonTextView, LayoutHelper.createFrame(-1, 48, 83));
        RadialProgressView radialProgressView = new RadialProgressView(context);
        this.botProgressView = radialProgressView;
        radialProgressView.setSize(AndroidUtilities.dp(18.0f));
        this.botProgressView.setAlpha(0.0f);
        this.botProgressView.setScaleX(0.1f);
        this.botProgressView.setScaleY(0.1f);
        this.botProgressView.setVisibility(8);
        this.containerView.addView(this.botProgressView, LayoutHelper.createFrame(28, 28.0f, 85, 0.0f, 0.0f, 10.0f, 10.0f));
        11 r02 = new 11(context, z);
        this.frameLayout2 = r02;
        r02.setWillNotDraw(false);
        this.frameLayout2.setVisibility(4);
        this.frameLayout2.setAlpha(0.0f);
        this.containerView.addView(this.frameLayout2, LayoutHelper.createFrame(-1, -2, 83));
        this.frameLayout2.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda12
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view3, MotionEvent motionEvent) {
                boolean lambda$new$15;
                lambda$new$15 = ChatAttachAlert.lambda$new$15(view3, motionEvent);
                return lambda$new$15;
            }
        });
        NumberTextView numberTextView = new NumberTextView(context);
        this.captionLimitView = numberTextView;
        numberTextView.setVisibility(8);
        numberTextView.setTextSize(15);
        numberTextView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
        numberTextView.setTypeface(AndroidUtilities.bold());
        numberTextView.setCenterAlign(true);
        this.frameLayout2.addView(numberTextView, LayoutHelper.createFrame(56, 20.0f, 85, 3.0f, 0.0f, 14.0f, 78.0f));
        this.currentLimit = MessagesController.getInstance(UserConfig.selectedAccount).getCaptionMaxLengthLimit();
        12 r13 = new 12(context, this.sizeNotifierFrameLayout, null, 1, true, resourcesProvider);
        this.commentTextView = r13;
        r13.setHint(LocaleController.getString("AddCaption", R.string.AddCaption));
        this.commentTextView.onResume();
        this.commentTextView.getEditText().addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.Components.ChatAttachAlert.13
            private boolean processChange;
            private boolean wasEmpty;

            {
                ChatAttachAlert.this = this;
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                int i9;
                boolean z5 = true;
                if (this.wasEmpty != TextUtils.isEmpty(editable)) {
                    if (ChatAttachAlert.this.currentAttachLayout != null) {
                        ChatAttachAlert.this.currentAttachLayout.onSelectedItemsCountChanged(ChatAttachAlert.this.currentAttachLayout.getSelectedItemsCount());
                    }
                    this.wasEmpty = !this.wasEmpty;
                }
                if (this.processChange) {
                    for (ImageSpan imageSpan : (ImageSpan[]) editable.getSpans(0, editable.length(), ImageSpan.class)) {
                        editable.removeSpan(imageSpan);
                    }
                    Emoji.replaceEmoji((CharSequence) editable, ChatAttachAlert.this.commentTextView.getEditText().getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                    this.processChange = false;
                }
                ChatAttachAlert.this.codepointCount = Character.codePointCount(editable, 0, editable.length());
                if (ChatAttachAlert.this.currentLimit <= 0 || (i9 = ChatAttachAlert.this.currentLimit - ChatAttachAlert.this.codepointCount) > 100) {
                    ChatAttachAlert.this.captionLimitView.animate().alpha(0.0f).scaleX(0.5f).scaleY(0.5f).setDuration(100L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlert.13.1
                        {
                            13.this = this;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            ChatAttachAlert.this.captionLimitView.setVisibility(8);
                        }
                    });
                } else {
                    if (i9 < -9999) {
                        i9 = -9999;
                    }
                    ChatAttachAlert.this.captionLimitView.setNumber(i9, ChatAttachAlert.this.captionLimitView.getVisibility() == 0);
                    if (ChatAttachAlert.this.captionLimitView.getVisibility() != 0) {
                        ChatAttachAlert.this.captionLimitView.setVisibility(0);
                        ChatAttachAlert.this.captionLimitView.setAlpha(0.0f);
                        ChatAttachAlert.this.captionLimitView.setScaleX(0.5f);
                        ChatAttachAlert.this.captionLimitView.setScaleY(0.5f);
                    }
                    ChatAttachAlert.this.captionLimitView.animate().setListener(null).cancel();
                    ChatAttachAlert.this.captionLimitView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(100L).start();
                    if (i9 < 0) {
                        ChatAttachAlert.this.captionLimitView.setTextColor(ChatAttachAlert.this.getThemedColor(Theme.key_text_RedRegular));
                        z5 = false;
                    } else {
                        ChatAttachAlert.this.captionLimitView.setTextColor(ChatAttachAlert.this.getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
                    }
                }
                ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                if (chatAttachAlert.sendButtonEnabled != z5) {
                    chatAttachAlert.sendButtonEnabled = z5;
                    chatAttachAlert.writeButton.invalidate();
                }
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i9, int i10, int i11) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i9, int i10, int i11) {
                if (i11 - i10 >= 1) {
                    this.processChange = true;
                }
                ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                if (chatAttachAlert.mentionContainer == null) {
                    chatAttachAlert.createMentionsContainer();
                }
                if (ChatAttachAlert.this.mentionContainer.getAdapter() != null) {
                    ChatAttachAlert.this.mentionContainer.getAdapter().lambda$searchUsernameOrHashtag$7(charSequence, ChatAttachAlert.this.commentTextView.getEditText().getSelectionStart(), null, false, false);
                }
            }
        });
        this.frameLayout2.addView(this.commentTextView, LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 0.0f, 84.0f, 0.0f));
        this.frameLayout2.setClipChildren(false);
        this.commentTextView.setClipChildren(false);
        FrameLayout frameLayout2 = new FrameLayout(context) { // from class: org.telegram.ui.Components.ChatAttachAlert.14
            {
                ChatAttachAlert.this = this;
            }

            @Override // android.view.View
            public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
                String formatPluralString;
                super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
                if (ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.photoLayout) {
                    formatPluralString = LocaleController.formatPluralString("AccDescrSendPhotos", ChatAttachAlert.this.photoLayout.getSelectedItemsCount(), new Object[0]);
                } else if (ChatAttachAlert.this.currentAttachLayout != ChatAttachAlert.this.documentLayout) {
                    if (ChatAttachAlert.this.currentAttachLayout == ChatAttachAlert.this.audioLayout) {
                        formatPluralString = LocaleController.formatPluralString("AccDescrSendAudio", ChatAttachAlert.this.audioLayout.getSelectedItemsCount(), new Object[0]);
                    }
                    accessibilityNodeInfo.setClassName(Button.class.getName());
                    accessibilityNodeInfo.setLongClickable(true);
                    accessibilityNodeInfo.setClickable(true);
                } else {
                    formatPluralString = LocaleController.formatPluralString("AccDescrSendFiles", ChatAttachAlert.this.documentLayout.getSelectedItemsCount(), new Object[0]);
                }
                accessibilityNodeInfo.setText(formatPluralString);
                accessibilityNodeInfo.setClassName(Button.class.getName());
                accessibilityNodeInfo.setLongClickable(true);
                accessibilityNodeInfo.setClickable(true);
            }
        };
        this.writeButtonContainer = frameLayout2;
        frameLayout2.setFocusable(true);
        this.writeButtonContainer.setFocusableInTouchMode(true);
        this.writeButtonContainer.setVisibility(4);
        this.writeButtonContainer.setScaleX(0.2f);
        this.writeButtonContainer.setScaleY(0.2f);
        this.writeButtonContainer.setAlpha(0.0f);
        this.containerView.addView(this.writeButtonContainer, LayoutHelper.createFrame(60, 60.0f, 85, 0.0f, 0.0f, 6.0f, 10.0f));
        ChatActivityEnterView.SendButton sendButton = new ChatActivityEnterView.SendButton(context, R.drawable.attach_send, resourcesProvider) { // from class: org.telegram.ui.Components.ChatAttachAlert.15
            {
                ChatAttachAlert.this = this;
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public int getFillColor() {
                return ChatAttachAlert.this.getThemedColor(Theme.key_dialogFloatingButton);
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean isInScheduleMode() {
                return super.isInScheduleMode();
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean isInactive() {
                return !ChatAttachAlert.this.sendButtonEnabled;
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean isOpen() {
                return true;
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean shouldDrawBackground() {
                return true;
            }
        };
        this.writeButton = sendButton;
        sendButton.center = true;
        sendButton.setImportantForAccessibility(2);
        this.writeButtonContainer.addView(this.writeButton, LayoutHelper.createFrame(64, 64.0f, 51, -4.0f, -4.0f, 0.0f, 0.0f));
        this.writeButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                ChatAttachAlert.this.lambda$new$17(baseFragment, resourcesProvider, view3);
            }
        });
        this.writeButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda14
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view3) {
                boolean lambda$new$26;
                lambda$new$26 = ChatAttachAlert.this.lambda$new$26(context, resourcesProvider, baseFragment, view3);
                return lambda$new$26;
            }
        });
        this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
        this.textPaint.setTypeface(AndroidUtilities.bold());
        View view3 = new View(context) { // from class: org.telegram.ui.Components.ChatAttachAlert.16
            {
                ChatAttachAlert.this = this;
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                int ceil;
                int i9;
                int i10;
                String format = String.format("%d", Integer.valueOf(Math.max(1, ChatAttachAlert.this.currentAttachLayout.getSelectedItemsCount())));
                int max = Math.max(AndroidUtilities.dp(16.0f) + ((int) Math.ceil(ChatAttachAlert.this.textPaint.measureText(format))), AndroidUtilities.dp(24.0f));
                int measuredWidth = getMeasuredWidth() / 2;
                int themedColor2 = ChatAttachAlert.this.getThemedColor(Theme.key_dialogRoundCheckBoxCheck);
                TextPaint textPaint = ChatAttachAlert.this.textPaint;
                double alpha = Color.alpha(themedColor2);
                double d = ChatAttachAlert.this.sendButtonEnabledProgress;
                Double.isNaN(d);
                Double.isNaN(alpha);
                textPaint.setColor(ColorUtils.setAlphaComponent(themedColor2, (int) (alpha * ((d * 0.42d) + 0.58d))));
                ChatAttachAlert.this.paint.setColor(ChatAttachAlert.this.getThemedColor(Theme.key_dialogBackground));
                int i11 = max / 2;
                ChatAttachAlert.this.rect.set(measuredWidth - i11, 0.0f, i11 + measuredWidth, getMeasuredHeight());
                canvas.drawRoundRect(ChatAttachAlert.this.rect, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), ChatAttachAlert.this.paint);
                ChatAttachAlert.this.paint.setColor(ChatAttachAlert.this.getThemedColor(Theme.key_chat_attachCheckBoxBackground));
                ChatAttachAlert.this.rect.set(i9 + AndroidUtilities.dp(2.0f), AndroidUtilities.dp(2.0f), i10 - AndroidUtilities.dp(2.0f), getMeasuredHeight() - AndroidUtilities.dp(2.0f));
                canvas.drawRoundRect(ChatAttachAlert.this.rect, AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), ChatAttachAlert.this.paint);
                canvas.drawText(format, measuredWidth - (ceil / 2), AndroidUtilities.dp(16.2f), ChatAttachAlert.this.textPaint);
            }
        };
        this.selectedCountView = view3;
        view3.setAlpha(0.0f);
        this.selectedCountView.setScaleX(0.2f);
        this.selectedCountView.setScaleY(0.2f);
        if (z) {
            checkColors();
            this.navBarColorKey = -1;
        }
        PasscodeView passcodeView = new PasscodeView(context);
        this.passcodeView = passcodeView;
        this.containerView.addView(passcodeView, LayoutHelper.createFrame(-1, -1.0f));
    }

    public void applyAttachButtonColors(View view) {
        TextView textView;
        int themedColor;
        int i;
        float f;
        if (view instanceof AttachButton) {
            AttachButton attachButton = (AttachButton) view;
            textView = attachButton.textView;
            themedColor = getThemedColor(Theme.key_dialogTextGray2);
            i = getThemedColor(attachButton.textKey);
            f = attachButton.checkedState;
        } else if (!(view instanceof AttachBotButton)) {
            return;
        } else {
            AttachBotButton attachBotButton = (AttachBotButton) view;
            textView = attachBotButton.nameTextView;
            themedColor = getThemedColor(Theme.key_dialogTextGray2);
            i = attachBotButton.textColor;
            f = attachBotButton.checkedState;
        }
        textView.setTextColor(ColorUtils.blendARGB(themedColor, i, f));
    }

    public void createMentionsContainer() {
        this.mentionContainer = new MentionsContainerView(getContext(), UserConfig.getInstance(this.currentAccount).getClientUserId(), 0L, LaunchActivity.getLastFragment(), null, this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatAttachAlert.25
            {
                ChatAttachAlert.this = this;
            }

            @Override // org.telegram.ui.Components.MentionsContainerView
            protected void onAnimationScroll() {
                if (ChatAttachAlert.this.photoLayout != null) {
                    ChatAttachAlert.this.photoLayout.checkCameraViewPosition();
                }
            }

            @Override // org.telegram.ui.Components.MentionsContainerView
            protected void onScrolled(boolean z, boolean z2) {
                if (ChatAttachAlert.this.photoLayout != null) {
                    ChatAttachAlert.this.photoLayout.checkCameraViewPosition();
                }
            }
        };
        setupMentionContainer();
        this.mentionContainer.withDelegate(new MentionsContainerView.Delegate() { // from class: org.telegram.ui.Components.ChatAttachAlert.26
            {
                ChatAttachAlert.this = this;
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public /* synthetic */ void addEmojiToRecent(String str) {
                MentionsContainerView.Delegate.-CC.$default$addEmojiToRecent(this, str);
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public Paint.FontMetricsInt getFontMetrics() {
                return ChatAttachAlert.this.commentTextView.getEditText().getPaint().getFontMetricsInt();
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public /* synthetic */ void onStickerSelected(TLRPC.TL_document tL_document, String str, Object obj) {
                MentionsContainerView.Delegate.-CC.$default$onStickerSelected(this, tL_document, str, obj);
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public void replaceText(int i, int i2, CharSequence charSequence, boolean z) {
                ChatAttachAlert.this.replaceWithText(i, i2, charSequence, z);
            }

            @Override // org.telegram.ui.Components.MentionsContainerView.Delegate
            public /* synthetic */ void sendBotInlineResult(TLRPC.BotInlineResult botInlineResult, boolean z, int i) {
                MentionsContainerView.Delegate.-CC.$default$sendBotInlineResult(this, botInlineResult, z, i);
            }
        });
        ViewGroup viewGroup = this.containerView;
        viewGroup.addView(this.mentionContainer, viewGroup.indexOfChild(this.frameLayout2), LayoutHelper.createFrame(-1, -1, 83));
        this.mentionContainer.setTranslationY(-this.commentTextView.getHeight());
        setupMentionContainer();
    }

    public int getScrollOffsetY(int i) {
        AttachAlertLayout attachAlertLayout = this.nextAttachLayout;
        if (attachAlertLayout == null || !((this.currentAttachLayout instanceof ChatAttachAlertPhotoLayoutPreview) || (attachAlertLayout instanceof ChatAttachAlertPhotoLayoutPreview))) {
            return this.scrollOffsetY[i];
        }
        int[] iArr = this.scrollOffsetY;
        return AndroidUtilities.lerp(iArr[0], iArr[1], this.translationProgress);
    }

    public boolean isCaptionAbove() {
        AttachAlertLayout attachAlertLayout;
        return this.captionAbove && ((attachAlertLayout = this.currentAttachLayout) == this.photoLayout || attachAlertLayout == this.photoPreviewLayout);
    }

    private boolean isLightStatusBar() {
        return ColorUtils.calculateLuminance(getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_listViewBackground : Theme.key_dialogBackground)) > 0.699999988079071d;
    }

    public /* synthetic */ void lambda$dismiss$44(DialogInterface dialogInterface, int i) {
        this.allowPassConfirmationAlert = true;
        dismiss();
    }

    public /* synthetic */ void lambda$dismiss$45(DialogInterface dialogInterface) {
        SpringAnimation springAnimation = this.appearSpringAnimation;
        if (springAnimation != null) {
            springAnimation.cancel();
        }
        SpringAnimation springAnimation2 = new SpringAnimation(this.containerView, DynamicAnimation.TRANSLATION_Y, 0.0f);
        this.appearSpringAnimation = springAnimation2;
        springAnimation2.getSpring().setDampingRatio(1.5f);
        this.appearSpringAnimation.getSpring().setStiffness(1500.0f);
        this.appearSpringAnimation.start();
    }

    public /* synthetic */ void lambda$dismiss$46(DialogInterface dialogInterface) {
        this.confirmationAlertShown = false;
    }

    public /* synthetic */ void lambda$dismiss$47(int i) {
        this.navBarColorKey = -1;
        this.navBarColor = i;
        this.containerView.invalidate();
    }

    public /* synthetic */ void lambda$init$43(TLRPC.MessageMedia messageMedia, int i, boolean z, int i2) {
        ((ChatActivity) this.baseFragment).didSelectLocation(messageMedia, i, z, i2);
    }

    public /* synthetic */ void lambda$makeFocusable$42(final EditTextBoldCursor editTextBoldCursor, boolean z) {
        setFocusable(true);
        editTextBoldCursor.requestFocus();
        if (z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    AndroidUtilities.showKeyboard(EditTextBoldCursor.this);
                }
            });
        }
    }

    public /* synthetic */ void lambda$new$0(int i) {
        this.actionBar.getActionBarMenuOnItemClick().onItemClick(i);
    }

    public /* synthetic */ void lambda$new$1(View view) {
        this.selectedMenuItem.toggleSubMenu();
    }

    public /* synthetic */ void lambda$new$10(final AttachBotButton attachBotButton, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlert.this.lambda$new$9(attachBotButton);
            }
        });
    }

    public /* synthetic */ void lambda$new$11(final AttachBotButton attachBotButton, Boolean bool) {
        TLRPC.TL_messages_toggleBotInAttachMenu tL_messages_toggleBotInAttachMenu = new TLRPC.TL_messages_toggleBotInAttachMenu();
        tL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(this.currentAccount).getInputUser(attachBotButton.attachMenuBot.bot_id);
        tL_messages_toggleBotInAttachMenu.enabled = true;
        tL_messages_toggleBotInAttachMenu.write_allowed = true;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda41
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                ChatAttachAlert.this.lambda$new$10(attachBotButton, tLObject, tL_error);
            }
        }, 66);
    }

    /* JADX WARN: Code restructure failed: missing block: B:204:0x00c3, code lost:
        if (r0 != 0) goto L71;
     */
    /* JADX WARN: Removed duplicated region for block: B:268:0x01e6  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x01ec  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$new$12(Theme.ResourcesProvider resourcesProvider, View view, int i) {
        AttachAlertLayout attachAlertLayout;
        int checkSelfPermission;
        int checkSelfPermission2;
        int checkSelfPermission3;
        int checkSelfPermission4;
        int checkSelfPermission5;
        int checkSelfPermission6;
        int i2;
        BaseFragment baseFragment = this.baseFragment;
        if (baseFragment == null) {
            baseFragment = LaunchActivity.getLastFragment();
        }
        if (baseFragment == null || baseFragment.getParentActivity() == null) {
            return;
        }
        if (view instanceof AttachButton) {
            Activity parentActivity = baseFragment.getParentActivity();
            int intValue = view.getTag() instanceof Integer ? ((Integer) view.getTag()).intValue() : -1;
            if (intValue != 1) {
                if (intValue == 3) {
                    if (!this.musicEnabled && checkCanRemoveRestrictionsByBoosts()) {
                        return;
                    }
                    int i3 = Build.VERSION.SDK_INT;
                    if (i3 >= 33) {
                        checkSelfPermission6 = parentActivity.checkSelfPermission("android.permission.READ_MEDIA_AUDIO");
                        if (checkSelfPermission6 != 0) {
                            parentActivity.requestPermissions(new String[]{"android.permission.READ_MEDIA_AUDIO"}, 4);
                            return;
                        }
                    } else if (i3 >= 23) {
                        checkSelfPermission5 = parentActivity.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE");
                        if (checkSelfPermission5 != 0) {
                            AndroidUtilities.findActivity(getContext()).requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 4);
                            return;
                        }
                    }
                    openAudioLayout(true);
                } else if (intValue == 4) {
                    if (!this.documentsEnabled && checkCanRemoveRestrictionsByBoosts()) {
                        return;
                    }
                    int i4 = Build.VERSION.SDK_INT;
                    if (i4 >= 33) {
                        checkSelfPermission3 = parentActivity.checkSelfPermission("android.permission.READ_MEDIA_IMAGES");
                        if (checkSelfPermission3 == 0) {
                            checkSelfPermission4 = parentActivity.checkSelfPermission("android.permission.READ_MEDIA_VIDEO");
                        }
                        parentActivity.requestPermissions(new String[]{"android.permission.READ_MEDIA_IMAGES", "android.permission.READ_MEDIA_VIDEO"}, 4);
                        return;
                    } else if (i4 >= 23) {
                        checkSelfPermission2 = parentActivity.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE");
                        if (checkSelfPermission2 != 0) {
                            AndroidUtilities.findActivity(getContext()).requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 4);
                            return;
                        }
                    }
                    openDocumentsLayout(true);
                } else if (intValue == 5) {
                    if (!this.plainTextEnabled && checkCanRemoveRestrictionsByBoosts()) {
                        return;
                    }
                    if (Build.VERSION.SDK_INT >= 23 && this.plainTextEnabled) {
                        checkSelfPermission = getContext().checkSelfPermission("android.permission.READ_CONTACTS");
                        if (checkSelfPermission != 0) {
                            AndroidUtilities.findActivity(getContext()).requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 5);
                            return;
                        }
                    }
                    openContactsLayout();
                } else if (intValue == 6) {
                    if ((!this.plainTextEnabled && checkCanRemoveRestrictionsByBoosts()) || !AndroidUtilities.isMapsInstalled(this.baseFragment)) {
                        return;
                    }
                    if (this.plainTextEnabled) {
                        if (this.locationLayout == null) {
                            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
                            ChatAttachAlertLocationLayout chatAttachAlertLocationLayout = new ChatAttachAlertLocationLayout(this, getContext(), resourcesProvider);
                            this.locationLayout = chatAttachAlertLocationLayout;
                            attachAlertLayoutArr[5] = chatAttachAlertLocationLayout;
                            chatAttachAlertLocationLayout.setDelegate(new ChatAttachAlertLocationLayout.LocationActivityDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda32
                                @Override // org.telegram.ui.Components.ChatAttachAlertLocationLayout.LocationActivityDelegate
                                public final void didSelectLocation(TLRPC.MessageMedia messageMedia, int i5, boolean z, int i6) {
                                    ChatAttachAlert.this.lambda$new$7(messageMedia, i5, z, i6);
                                }
                            });
                        }
                        attachAlertLayout = this.locationLayout;
                    } else {
                        ChatAttachRestrictedLayout chatAttachRestrictedLayout = new ChatAttachRestrictedLayout(6, this, getContext(), resourcesProvider);
                        this.restrictedLayout = chatAttachRestrictedLayout;
                        showLayout(chatAttachRestrictedLayout);
                    }
                } else if (intValue == 9) {
                    if (!this.pollsEnabled && checkCanRemoveRestrictionsByBoosts()) {
                        return;
                    }
                    if (this.pollsEnabled) {
                        if (this.pollLayout == null) {
                            AttachAlertLayout[] attachAlertLayoutArr2 = this.layouts;
                            ChatAttachAlertPollLayout chatAttachAlertPollLayout = new ChatAttachAlertPollLayout(this, getContext(), resourcesProvider);
                            this.pollLayout = chatAttachAlertPollLayout;
                            attachAlertLayoutArr2[1] = chatAttachAlertPollLayout;
                            chatAttachAlertPollLayout.setDelegate(new ChatAttachAlertPollLayout.PollCreateActivityDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda33
                                @Override // org.telegram.ui.Components.ChatAttachAlertPollLayout.PollCreateActivityDelegate
                                public final void sendPoll(TLRPC.TL_messageMediaPoll tL_messageMediaPoll, HashMap hashMap, boolean z, int i5) {
                                    ChatAttachAlert.this.lambda$new$8(tL_messageMediaPoll, hashMap, z, i5);
                                }
                            });
                        }
                        attachAlertLayout = this.pollLayout;
                    } else {
                        ChatAttachRestrictedLayout chatAttachRestrictedLayout2 = new ChatAttachRestrictedLayout(9, this, getContext(), resourcesProvider);
                        this.restrictedLayout = chatAttachRestrictedLayout2;
                        showLayout(chatAttachRestrictedLayout2);
                    }
                } else if (intValue == 11) {
                    openQuickRepliesLayout();
                } else if (view.getTag() instanceof Integer) {
                    this.delegate.didPressedButton(((Integer) view.getTag()).intValue(), true, true, 0, 0L, isCaptionAbove(), false);
                }
                int left = view.getLeft();
                int right = view.getRight();
                int dp = AndroidUtilities.dp(10.0f);
                i2 = left - dp;
                if (i2 >= 0) {
                    this.buttonsRecyclerView.smoothScrollBy(i2, 0);
                } else {
                    int i5 = right + dp;
                    if (i5 > this.buttonsRecyclerView.getMeasuredWidth()) {
                        RecyclerListView recyclerListView = this.buttonsRecyclerView;
                        recyclerListView.smoothScrollBy(i5 - recyclerListView.getMeasuredWidth(), 0);
                    }
                }
            } else if (!this.photosEnabled && !this.videosEnabled && checkCanRemoveRestrictionsByBoosts()) {
                return;
            } else {
                if (!this.photosEnabled && !this.videosEnabled) {
                    ChatAttachRestrictedLayout chatAttachRestrictedLayout3 = new ChatAttachRestrictedLayout(1, this, getContext(), resourcesProvider);
                    this.restrictedLayout = chatAttachRestrictedLayout3;
                    showLayout(chatAttachRestrictedLayout3);
                }
                attachAlertLayout = this.photoLayout;
            }
            showLayout(attachAlertLayout);
            int left2 = view.getLeft();
            int right2 = view.getRight();
            int dp2 = AndroidUtilities.dp(10.0f);
            i2 = left2 - dp2;
            if (i2 >= 0) {
            }
        } else if (view instanceof AttachBotButton) {
            final AttachBotButton attachBotButton = (AttachBotButton) view;
            if (attachBotButton.attachMenuBot == null) {
                this.delegate.didSelectBot(attachBotButton.currentUser);
                dismiss();
            } else if (attachBotButton.attachMenuBot.inactive) {
                WebAppDisclaimerAlert.show(getContext(), new Consumer() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda34
                    @Override // com.google.android.exoplayer2.util.Consumer
                    public final void accept(Object obj) {
                        ChatAttachAlert.this.lambda$new$11(attachBotButton, (Boolean) obj);
                    }
                }, null, null);
            } else {
                showBotLayout(attachBotButton.attachMenuBot.bot_id, true);
            }
        }
        if (view.getX() + view.getWidth() >= this.buttonsRecyclerView.getMeasuredWidth() - AndroidUtilities.dp(32.0f)) {
            this.buttonsRecyclerView.smoothScrollBy((int) (view.getWidth() * 1.5f), 0);
        }
    }

    public /* synthetic */ boolean lambda$new$13(View view, int i) {
        if (view instanceof AttachBotButton) {
            AttachBotButton attachBotButton = (AttachBotButton) view;
            if (!this.destroyed && attachBotButton.currentUser != null) {
                onLongClickBotButton(attachBotButton.attachMenuBot, attachBotButton.currentUser);
                return true;
            }
        }
        return false;
    }

    public /* synthetic */ void lambda$new$14(View view) {
        ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout;
        long j = this.selectedId;
        if (j >= 0 || (chatAttachAlertBotWebViewLayout = (ChatAttachAlertBotWebViewLayout) this.botAttachLayouts.get(-j)) == null) {
            return;
        }
        chatAttachAlertBotWebViewLayout.getWebViewContainer().onMainButtonPressed();
    }

    public static /* synthetic */ boolean lambda$new$15(View view, MotionEvent motionEvent) {
        return true;
    }

    public /* synthetic */ void lambda$new$16(boolean z, int i) {
        AttachAlertLayout attachAlertLayout = this.currentAttachLayout;
        if (attachAlertLayout == this.photoLayout || attachAlertLayout == this.photoPreviewLayout) {
            sendPressed(z, i, 0L, isCaptionAbove());
            return;
        }
        attachAlertLayout.sendSelectedItems(z, i, 0L, isCaptionAbove());
        this.allowPassConfirmationAlert = true;
        dismiss();
    }

    public /* synthetic */ void lambda$new$17(BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider, View view) {
        if (this.currentLimit - this.codepointCount < 0) {
            AndroidUtilities.shakeView(this.captionLimitView);
            try {
                this.captionLimitView.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            if (MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() || MessagesController.getInstance(this.currentAccount).captionLengthLimitPremium <= this.codepointCount) {
                return;
            }
            showCaptionLimitBulletin(baseFragment);
            return;
        }
        if (this.editingMessageObject == null) {
            BaseFragment baseFragment2 = this.baseFragment;
            if ((baseFragment2 instanceof ChatActivity) && ((ChatActivity) baseFragment2).isInScheduleMode()) {
                AlertsCreator.createScheduleDatePickerDialog(getContext(), ((ChatActivity) this.baseFragment).getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda35
                    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                    public final void didSelectDate(boolean z, int i) {
                        ChatAttachAlert.this.lambda$new$16(z, i);
                    }
                }, resourcesProvider);
                return;
            }
        }
        AttachAlertLayout attachAlertLayout = this.currentAttachLayout;
        if (attachAlertLayout == this.photoLayout || attachAlertLayout == this.photoPreviewLayout) {
            sendPressed(true, 0, 0L, isCaptionAbove());
            return;
        }
        attachAlertLayout.sendSelectedItems(true, 0, 0L, isCaptionAbove());
        this.allowPassConfirmationAlert = true;
        dismiss();
    }

    public /* synthetic */ void lambda$new$18(long j, boolean z, int i) {
        AttachAlertLayout attachAlertLayout = this.currentAttachLayout;
        if (attachAlertLayout == this.photoLayout || attachAlertLayout == this.photoPreviewLayout) {
            sendPressed(z, i, j, isCaptionAbove());
            return;
        }
        attachAlertLayout.sendSelectedItems(z, i, j, isCaptionAbove());
        this.allowPassConfirmationAlert = true;
        dismiss();
    }

    public /* synthetic */ void lambda$new$19(BaseFragment baseFragment, Theme.ResourcesProvider resourcesProvider, View view) {
        MessageSendPreview messageSendPreview = this.messageSendPreview;
        final long selectedEffect = messageSendPreview != null ? messageSendPreview.getSelectedEffect() : 0L;
        forceKeyboardOnDismiss();
        MessageSendPreview messageSendPreview2 = this.messageSendPreview;
        if (messageSendPreview2 != null) {
            messageSendPreview2.dismiss(true);
            this.messageSendPreview = null;
        }
        if (this.currentLimit - this.codepointCount < 0) {
            AndroidUtilities.shakeView(this.captionLimitView);
            try {
                this.captionLimitView.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            if (MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() || MessagesController.getInstance(this.currentAccount).captionLengthLimitPremium <= this.codepointCount) {
                return;
            }
            showCaptionLimitBulletin(baseFragment);
            return;
        }
        if (this.editingMessageObject == null) {
            BaseFragment baseFragment2 = this.baseFragment;
            if ((baseFragment2 instanceof ChatActivity) && ((ChatActivity) baseFragment2).isInScheduleMode()) {
                AlertsCreator.createScheduleDatePickerDialog(getContext(), ((ChatActivity) this.baseFragment).getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda40
                    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                    public final void didSelectDate(boolean z, int i) {
                        ChatAttachAlert.this.lambda$new$18(selectedEffect, z, i);
                    }
                }, resourcesProvider);
                this.captionAbove = false;
            }
        }
        AttachAlertLayout attachAlertLayout = this.currentAttachLayout;
        if (attachAlertLayout == this.photoLayout || attachAlertLayout == this.photoPreviewLayout) {
            sendPressed(true, 0, selectedEffect, isCaptionAbove());
        } else {
            attachAlertLayout.sendSelectedItems(true, 0, selectedEffect, isCaptionAbove());
            this.allowPassConfirmationAlert = true;
            dismiss();
        }
        this.captionAbove = false;
    }

    public /* synthetic */ void lambda$new$2(View view) {
        this.currentAttachLayout.onMenuItemClick(40);
    }

    public /* synthetic */ void lambda$new$20(MessageObject messageObject, MessagePreviewView.ToggleButton toggleButton, View view) {
        MessagePreviewView.ToggleButton toggleButton2;
        boolean z = this.captionAbove;
        boolean z2 = !z;
        this.captionAbove = z2;
        messageObject.messageOwner.invert_media = z2;
        toggleButton.setState(z, true);
        this.messageSendPreview.changeMessage(messageObject);
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout = this.photoLayout;
        if (chatAttachAlertPhotoLayout != null && (toggleButton2 = chatAttachAlertPhotoLayout.captionItem) != null) {
            toggleButton2.setState(!this.captionAbove, true);
        }
        this.messageSendPreview.scrollTo(!this.captionAbove);
    }

    public /* synthetic */ void lambda$new$21(boolean z, int i) {
        MessageSendPreview messageSendPreview = this.messageSendPreview;
        long selectedEffect = messageSendPreview != null ? messageSendPreview.getSelectedEffect() : 0L;
        MessageSendPreview messageSendPreview2 = this.messageSendPreview;
        if (messageSendPreview2 != null) {
            messageSendPreview2.dismiss(true);
            this.messageSendPreview = null;
        }
        AttachAlertLayout attachAlertLayout = this.currentAttachLayout;
        if (attachAlertLayout == this.photoLayout || attachAlertLayout == this.photoPreviewLayout) {
            sendPressed(z, i, selectedEffect, isCaptionAbove());
            return;
        }
        attachAlertLayout.sendSelectedItems(z, i, selectedEffect, isCaptionAbove());
        dismiss();
    }

    public /* synthetic */ void lambda$new$22(long j, Theme.ResourcesProvider resourcesProvider) {
        AlertsCreator.createScheduleDatePickerDialog(getContext(), j, new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda39
            @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
            public final void didSelectDate(boolean z, int i) {
                ChatAttachAlert.this.lambda$new$21(z, i);
            }
        }, resourcesProvider);
    }

    public /* synthetic */ void lambda$new$23() {
        MessageSendPreview messageSendPreview = this.messageSendPreview;
        long selectedEffect = messageSendPreview != null ? messageSendPreview.getSelectedEffect() : 0L;
        MessageSendPreview messageSendPreview2 = this.messageSendPreview;
        if (messageSendPreview2 != null) {
            messageSendPreview2.dismiss(true);
            this.messageSendPreview = null;
        }
        AttachAlertLayout attachAlertLayout = this.currentAttachLayout;
        if (attachAlertLayout == this.photoLayout || attachAlertLayout == this.photoPreviewLayout) {
            sendPressed(false, 0, selectedEffect, isCaptionAbove());
            return;
        }
        attachAlertLayout.sendSelectedItems(false, 0, selectedEffect, isCaptionAbove());
        dismiss();
    }

    public /* synthetic */ void lambda$new$24(ActionBarMenuSubItem actionBarMenuSubItem, Long l, Runnable runnable) {
        runnable.run();
        this.photoLayout.setStarsPrice(l.longValue());
        if (l.longValue() > 0) {
            actionBarMenuSubItem.setText(LocaleController.getString(R.string.PaidMediaPriceButton));
            actionBarMenuSubItem.setSubtext(LocaleController.formatPluralString("Stars", (int) l.longValue(), new Object[0]));
            this.messageSendPreview.setStars(l.longValue());
            return;
        }
        actionBarMenuSubItem.setText(LocaleController.getString(R.string.PaidMediaButton));
        actionBarMenuSubItem.setSubtext(null);
        this.messageSendPreview.setStars(0L);
    }

    public /* synthetic */ void lambda$new$25(Context context, final ActionBarMenuSubItem actionBarMenuSubItem, Theme.ResourcesProvider resourcesProvider, View view) {
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout = this.photoLayout;
        if (chatAttachAlertPhotoLayout == null) {
            return;
        }
        StarsIntroActivity.showMediaPriceSheet(context, chatAttachAlertPhotoLayout.getStarsPrice(), true, new Utilities.Callback2() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda43
            @Override // org.telegram.messenger.Utilities.Callback2
            public final void run(Object obj, Object obj2) {
                ChatAttachAlert.this.lambda$new$24(actionBarMenuSubItem, (Long) obj, (Runnable) obj2);
            }
        }, resourcesProvider);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:353:0x02ff  */
    /* JADX WARN: Removed duplicated region for block: B:423:0x0465  */
    /* JADX WARN: Removed duplicated region for block: B:424:0x046a  */
    /* JADX WARN: Removed duplicated region for block: B:427:0x0472  */
    /* JADX WARN: Removed duplicated region for block: B:429:0x04aa  */
    /* JADX WARN: Removed duplicated region for block: B:432:0x04db  */
    /* JADX WARN: Removed duplicated region for block: B:433:0x04de  */
    /* JADX WARN: Removed duplicated region for block: B:436:0x04ea A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:440:0x0515 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:445:0x0522  */
    /* JADX WARN: Removed duplicated region for block: B:449:0x0547 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:455:0x055e  */
    /* JADX WARN: Type inference failed for: r0v77, types: [org.telegram.ui.Components.ItemOptions] */
    /* JADX WARN: Type inference failed for: r13v7 */
    /* JADX WARN: Type inference failed for: r13v8, types: [java.lang.String, java.lang.Runnable] */
    /* JADX WARN: Type inference failed for: r13v9 */
    /* JADX WARN: Type inference failed for: r2v48, types: [org.telegram.ui.MessageSendPreview] */
    /* JADX WARN: Type inference failed for: r3v18, types: [org.telegram.tgnet.TLRPC$DocumentAttribute, org.telegram.tgnet.TLRPC$TL_documentAttributeVideo] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ boolean lambda$new$26(final Context context, final Theme.ResourcesProvider resourcesProvider, final BaseFragment baseFragment, View view) {
        TLRPC.User user;
        long j;
        MessageObject messageObject;
        MessageObject messageObject2;
        ChatActivity chatActivity;
        final MessageObject messageObject3;
        boolean z;
        boolean z2;
        long j2;
        HashMap<Object, Object> hashMap;
        ArrayList<Object> arrayList;
        String str;
        int i;
        int i2;
        int i3;
        long j3;
        ArrayList arrayList2;
        TLRPC.TL_photoSize tL_photoSize;
        String charSequence;
        MessageObject messageObject4;
        int i4;
        Throwable th;
        MediaMetadataRetriever mediaMetadataRetriever;
        MediaMetadataRetriever mediaMetadataRetriever2;
        ArrayList arrayList3;
        final long j4;
        ?? r13;
        AttachAlertLayout attachAlertLayout;
        MessageObject messageObject5;
        boolean z3;
        String substring;
        boolean z4;
        boolean z5;
        int i5;
        boolean z6;
        String str2;
        int i6 = 10;
        boolean z7 = true;
        long j5 = this.dialogId;
        if ((j5 != 0 || (this.baseFragment instanceof ChatActivity)) && this.editingMessageObject == null && this.currentLimit - this.codepointCount >= 0) {
            BaseFragment baseFragment2 = this.baseFragment;
            if (baseFragment2 instanceof ChatActivity) {
                ChatActivity chatActivity2 = (ChatActivity) baseFragment2;
                chatActivity2.getCurrentChat();
                TLRPC.User currentUser = chatActivity2.getCurrentUser();
                messageObject = chatActivity2.getReplyMessage();
                messageObject2 = chatActivity2.getReplyTopMessage();
                if (chatActivity2.isInScheduleMode() || chatActivity2.getChatMode() == 5) {
                    return false;
                }
                j = chatActivity2.getDialogId();
                chatActivity = chatActivity2;
                user = currentUser;
            } else {
                user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j5));
                j = j5;
                messageObject = null;
                messageObject2 = null;
                chatActivity = null;
            }
            MessageSendPreview messageSendPreview = this.messageSendPreview;
            if (messageSendPreview != null) {
                messageSendPreview.dismiss();
            }
            MessageSendPreview messageSendPreview2 = new MessageSendPreview(context, resourcesProvider);
            this.messageSendPreview = messageSendPreview2;
            messageSendPreview2.setSendButton(this.writeButton, false, new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda24
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChatAttachAlert.this.lambda$new$19(baseFragment, resourcesProvider, view2);
                }
            });
            ArrayList arrayList4 = new ArrayList();
            AttachAlertLayout attachAlertLayout2 = this.currentAttachLayout;
            ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout = this.photoLayout;
            String str3 = "";
            if (attachAlertLayout2 == chatAttachAlertPhotoLayout || attachAlertLayout2 == this.photoPreviewLayout) {
                String str4 = "";
                HashMap<Object, Object> selectedPhotos = chatAttachAlertPhotoLayout.getSelectedPhotos();
                ArrayList<Object> selectedPhotosOrder = this.photoLayout.getSelectedPhotosOrder();
                if (!selectedPhotos.isEmpty()) {
                    int ceil = (int) Math.ceil(selectedPhotos.size() / 10.0f);
                    boolean z8 = false;
                    messageObject3 = null;
                    int i7 = 0;
                    int i8 = 0;
                    z = false;
                    while (i7 < ceil) {
                        String str5 = str4;
                        int i9 = i7 * 10;
                        boolean z9 = z8;
                        int i10 = ceil;
                        int min = Math.min(10, selectedPhotos.size() - i9);
                        long nextLong = Utilities.random.nextLong();
                        int i11 = i8;
                        boolean z10 = z9;
                        int i12 = 0;
                        while (i12 < min) {
                            int i13 = i9 + i12;
                            MessageObject messageObject6 = messageObject3;
                            if (i13 >= selectedPhotosOrder.size()) {
                                hashMap = selectedPhotos;
                                arrayList = selectedPhotosOrder;
                                j3 = j;
                                messageObject3 = messageObject6;
                                i4 = 1;
                            } else {
                                MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) selectedPhotos.get(selectedPhotosOrder.get(i13));
                                TLRPC.TL_message tL_message = new TLRPC.TL_message();
                                hashMap = selectedPhotos;
                                int i14 = i11 + 1;
                                tL_message.id = i11;
                                tL_message.out = true;
                                arrayList = selectedPhotosOrder;
                                tL_message.from_id = MessagesController.getInstance(this.currentAccount).getPeer(UserConfig.getInstance(this.currentAccount).getClientUserId());
                                tL_message.peer_id = MessagesController.getInstance(this.currentAccount).getPeer(j);
                                boolean z11 = photoEntry.isVideo;
                                if ((!z11 && (str = photoEntry.imagePath) != null) || (str = photoEntry.path) != null) {
                                    tL_message.attachPath = str;
                                }
                                if (min > 0) {
                                    tL_message.grouped_id = nextLong;
                                }
                                int i15 = photoEntry.width;
                                int i16 = photoEntry.height;
                                int i17 = photoEntry.orientation;
                                if (z11) {
                                    if (photoEntry.videoOrientation == -1) {
                                        try {
                                            mediaMetadataRetriever2 = new MediaMetadataRetriever();
                                            try {
                                                try {
                                                    mediaMetadataRetriever2.setDataSource(photoEntry.path);
                                                    photoEntry.videoOrientation = Integer.parseInt(mediaMetadataRetriever2.extractMetadata(24));
                                                } catch (Exception e) {
                                                    e = e;
                                                    i = i16;
                                                    photoEntry.videoOrientation = 0;
                                                    FileLog.e(e);
                                                    if (mediaMetadataRetriever2 != null) {
                                                        try {
                                                            mediaMetadataRetriever2.release();
                                                        } catch (IOException e2) {
                                                            e = e2;
                                                            FileLog.e(e);
                                                            i17 = photoEntry.videoOrientation;
                                                            if ((i17 / 90) % 2 == 0) {
                                                            }
                                                            if (photoEntry.isVideo) {
                                                            }
                                                            arrayList2.add(tL_photoSize);
                                                            tL_message.media.spoiler = photoEntry.hasSpoiler;
                                                            CharSequence charSequence2 = photoEntry.caption;
                                                            if (charSequence2 != null) {
                                                            }
                                                            tL_message.message = charSequence;
                                                            if (TextUtils.isEmpty(charSequence)) {
                                                                CharSequence[] charSequenceArr = {this.commentTextView.getText()};
                                                                MessageObject.addLinks(true, charSequenceArr[0]);
                                                                tL_message.entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, true);
                                                                tL_message.message = charSequenceArr[0].toString();
                                                            }
                                                            if (i7 == 0) {
                                                                TLRPC.TL_messageReplyHeader tL_messageReplyHeader = new TLRPC.TL_messageReplyHeader();
                                                                if (messageObject2 != null) {
                                                                }
                                                                tL_messageReplyHeader.flags |= 16;
                                                                tL_messageReplyHeader.reply_to_msg_id = messageObject.getId();
                                                                tL_message.reply_to = tL_messageReplyHeader;
                                                            }
                                                            messageObject4 = new MessageObject(this.currentAccount, tL_message, true, false);
                                                            if (i7 == 0) {
                                                                messageObject4.replyMessageObject = messageObject;
                                                            }
                                                            messageObject4.sendPreviewEntry = photoEntry;
                                                            messageObject4.sendPreview = true;
                                                            messageObject4.notime = true;
                                                            messageObject4.isOutOwnerCached = Boolean.TRUE;
                                                            arrayList4.add(messageObject4);
                                                            if (messageObject6 == null) {
                                                            }
                                                            i11 = i14;
                                                            i4 = 1;
                                                            z10 = true;
                                                            z = true;
                                                            i12 += i4;
                                                            selectedPhotos = hashMap;
                                                            selectedPhotosOrder = arrayList;
                                                            j = j3;
                                                        }
                                                    }
                                                    i17 = photoEntry.videoOrientation;
                                                    if ((i17 / 90) % 2 == 0) {
                                                    }
                                                    if (photoEntry.isVideo) {
                                                    }
                                                    arrayList2.add(tL_photoSize);
                                                    tL_message.media.spoiler = photoEntry.hasSpoiler;
                                                    CharSequence charSequence22 = photoEntry.caption;
                                                    if (charSequence22 != null) {
                                                    }
                                                    tL_message.message = charSequence;
                                                    if (TextUtils.isEmpty(charSequence)) {
                                                    }
                                                    if (i7 == 0) {
                                                    }
                                                    messageObject4 = new MessageObject(this.currentAccount, tL_message, true, false);
                                                    if (i7 == 0) {
                                                    }
                                                    messageObject4.sendPreviewEntry = photoEntry;
                                                    messageObject4.sendPreview = true;
                                                    messageObject4.notime = true;
                                                    messageObject4.isOutOwnerCached = Boolean.TRUE;
                                                    arrayList4.add(messageObject4);
                                                    if (messageObject6 == null) {
                                                    }
                                                    i11 = i14;
                                                    i4 = 1;
                                                    z10 = true;
                                                    z = true;
                                                    i12 += i4;
                                                    selectedPhotos = hashMap;
                                                    selectedPhotosOrder = arrayList;
                                                    j = j3;
                                                }
                                                try {
                                                    mediaMetadataRetriever2.release();
                                                } catch (IOException e3) {
                                                    e = e3;
                                                    i = i16;
                                                    FileLog.e(e);
                                                    i17 = photoEntry.videoOrientation;
                                                    if ((i17 / 90) % 2 == 0) {
                                                    }
                                                    if (photoEntry.isVideo) {
                                                    }
                                                    arrayList2.add(tL_photoSize);
                                                    tL_message.media.spoiler = photoEntry.hasSpoiler;
                                                    CharSequence charSequence222 = photoEntry.caption;
                                                    if (charSequence222 != null) {
                                                    }
                                                    tL_message.message = charSequence;
                                                    if (TextUtils.isEmpty(charSequence)) {
                                                    }
                                                    if (i7 == 0) {
                                                    }
                                                    messageObject4 = new MessageObject(this.currentAccount, tL_message, true, false);
                                                    if (i7 == 0) {
                                                    }
                                                    messageObject4.sendPreviewEntry = photoEntry;
                                                    messageObject4.sendPreview = true;
                                                    messageObject4.notime = true;
                                                    messageObject4.isOutOwnerCached = Boolean.TRUE;
                                                    arrayList4.add(messageObject4);
                                                    if (messageObject6 == null) {
                                                    }
                                                    i11 = i14;
                                                    i4 = 1;
                                                    z10 = true;
                                                    z = true;
                                                    i12 += i4;
                                                    selectedPhotos = hashMap;
                                                    selectedPhotosOrder = arrayList;
                                                    j = j3;
                                                }
                                            } catch (Throwable th2) {
                                                mediaMetadataRetriever = mediaMetadataRetriever2;
                                                th = th2;
                                                if (mediaMetadataRetriever != null) {
                                                    try {
                                                        mediaMetadataRetriever.release();
                                                    } catch (IOException e4) {
                                                        FileLog.e(e4);
                                                    }
                                                }
                                                throw th;
                                            }
                                        } catch (Exception e5) {
                                            e = e5;
                                            i = i16;
                                            mediaMetadataRetriever2 = null;
                                        } catch (Throwable th3) {
                                            th = th3;
                                            mediaMetadataRetriever = null;
                                        }
                                    }
                                    i = i16;
                                    i17 = photoEntry.videoOrientation;
                                } else {
                                    i = i16;
                                }
                                if ((i17 / 90) % 2 == 0) {
                                    i3 = i15;
                                    i2 = i;
                                } else {
                                    i2 = i15;
                                    i3 = i;
                                }
                                if (photoEntry.isVideo) {
                                    j3 = j;
                                    TLRPC.TL_messageMediaPhoto tL_messageMediaPhoto = new TLRPC.TL_messageMediaPhoto();
                                    tL_message.media = tL_messageMediaPhoto;
                                    tL_messageMediaPhoto.photo = new TLRPC.TL_photo();
                                    TLRPC.TL_photoSize tL_photoSize2 = new TLRPC.TL_photoSize();
                                    tL_photoSize2.w = i2;
                                    tL_photoSize2.h = i3;
                                    tL_photoSize2.location = new TLRPC.TL_fileLocationToBeDeprecated();
                                    arrayList2 = tL_message.media.photo.sizes;
                                    tL_photoSize = tL_photoSize2;
                                } else {
                                    TLRPC.TL_messageMediaDocument tL_messageMediaDocument = new TLRPC.TL_messageMediaDocument();
                                    tL_message.media = tL_messageMediaDocument;
                                    j3 = j;
                                    tL_messageMediaDocument.document = new TLRPC.TL_document();
                                    tL_message.media.document.mime_type = MimeTypeMap.getSingleton().getExtensionFromMimeType(tL_message.attachPath);
                                    ?? tL_documentAttributeVideo = new TLRPC.TL_documentAttributeVideo();
                                    tL_documentAttributeVideo.w = i2;
                                    tL_documentAttributeVideo.h = i3;
                                    tL_documentAttributeVideo.duration = photoEntry.duration;
                                    arrayList2 = tL_message.media.document.attributes;
                                    tL_photoSize = tL_documentAttributeVideo;
                                }
                                arrayList2.add(tL_photoSize);
                                tL_message.media.spoiler = photoEntry.hasSpoiler;
                                CharSequence charSequence2222 = photoEntry.caption;
                                charSequence = charSequence2222 != null ? str5 : charSequence2222.toString();
                                tL_message.message = charSequence;
                                if (TextUtils.isEmpty(charSequence) && i7 == 0 && i12 == 0) {
                                    CharSequence[] charSequenceArr2 = {this.commentTextView.getText()};
                                    MessageObject.addLinks(true, charSequenceArr2[0]);
                                    tL_message.entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr2, true);
                                    tL_message.message = charSequenceArr2[0].toString();
                                }
                                if (i7 == 0 && messageObject != null && !messageObject.isTopicMainMessage) {
                                    TLRPC.TL_messageReplyHeader tL_messageReplyHeader2 = new TLRPC.TL_messageReplyHeader();
                                    if (messageObject2 != null) {
                                        tL_messageReplyHeader2.flags |= 2;
                                        tL_messageReplyHeader2.reply_to_top_id = messageObject2.getId();
                                    }
                                    tL_messageReplyHeader2.flags |= 16;
                                    tL_messageReplyHeader2.reply_to_msg_id = messageObject.getId();
                                    tL_message.reply_to = tL_messageReplyHeader2;
                                }
                                messageObject4 = new MessageObject(this.currentAccount, tL_message, true, false);
                                if (i7 == 0 && messageObject != null && !messageObject.isTopicMainMessage) {
                                    messageObject4.replyMessageObject = messageObject;
                                }
                                messageObject4.sendPreviewEntry = photoEntry;
                                messageObject4.sendPreview = true;
                                messageObject4.notime = true;
                                messageObject4.isOutOwnerCached = Boolean.TRUE;
                                arrayList4.add(messageObject4);
                                messageObject3 = (messageObject6 == null || TextUtils.isEmpty(tL_message.message)) ? messageObject6 : messageObject4;
                                i11 = i14;
                                i4 = 1;
                                z10 = true;
                                z = true;
                            }
                            i12 += i4;
                            selectedPhotos = hashMap;
                            selectedPhotosOrder = arrayList;
                            j = j3;
                        }
                        i7++;
                        ceil = i10;
                        str4 = str5;
                        selectedPhotos = selectedPhotos;
                        boolean z12 = z10;
                        i8 = i11;
                        z8 = z12;
                    }
                    z2 = z8;
                    j2 = j;
                }
                j2 = j;
                messageObject3 = null;
                z = false;
                z2 = false;
            } else {
                if (attachAlertLayout2 == this.contactsLayout) {
                    if (TextUtils.isEmpty(this.commentTextView.getText())) {
                        i5 = 0;
                        z6 = false;
                    } else {
                        TLRPC.TL_message tL_message2 = new TLRPC.TL_message();
                        tL_message2.id = 0;
                        tL_message2.out = true;
                        tL_message2.from_id = MessagesController.getInstance(this.currentAccount).getPeer(UserConfig.getInstance(this.currentAccount).getClientUserId());
                        tL_message2.peer_id = MessagesController.getInstance(this.currentAccount).getPeer(j);
                        CharSequence[] charSequenceArr3 = {this.commentTextView.getText()};
                        MessageObject.addLinks(true, charSequenceArr3[0]);
                        tL_message2.entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr3, true);
                        tL_message2.message = charSequenceArr3[0].toString();
                        MessageObject messageObject7 = new MessageObject(this.currentAccount, tL_message2, true, false);
                        messageObject7.sendPreview = true;
                        messageObject7.notime = true;
                        messageObject7.isOutOwnerCached = Boolean.TRUE;
                        arrayList4.add(messageObject7);
                        i5 = 1;
                        z6 = true;
                    }
                    ArrayList<TLRPC.User> selected = this.contactsLayout.getSelected();
                    int i18 = 0;
                    while (i18 < selected.size()) {
                        TLRPC.User user2 = selected.get(i18);
                        TLRPC.TL_message tL_message3 = new TLRPC.TL_message();
                        int i19 = i5 + 1;
                        tL_message3.id = i5;
                        tL_message3.out = z7;
                        String str6 = str3;
                        tL_message3.from_id = MessagesController.getInstance(this.currentAccount).getPeer(UserConfig.getInstance(this.currentAccount).getClientUserId());
                        tL_message3.peer_id = MessagesController.getInstance(this.currentAccount).getPeer(j);
                        TLRPC.TL_messageMediaContact tL_messageMediaContact = new TLRPC.TL_messageMediaContact();
                        tL_message3.media = tL_messageMediaContact;
                        tL_messageMediaContact.phone_number = user2.phone;
                        tL_messageMediaContact.first_name = user2.first_name;
                        tL_messageMediaContact.last_name = user2.last_name;
                        if (user2.restriction_reason.isEmpty() || !user2.restriction_reason.get(0).text.startsWith("BEGIN:VCARD")) {
                            str2 = str6;
                            tL_message3.media.vcard = str2;
                        } else {
                            tL_message3.media.vcard = user2.restriction_reason.get(0).text;
                            str2 = str6;
                        }
                        tL_message3.media.user_id = user2.id;
                        MessageObject messageObject8 = new MessageObject(this.currentAccount, tL_message3, true, false);
                        messageObject8.sendPreview = true;
                        messageObject8.notime = true;
                        messageObject8.isOutOwnerCached = Boolean.TRUE;
                        arrayList4.add(messageObject8);
                        i18++;
                        str3 = str2;
                        i5 = i19;
                        z6 = true;
                        z7 = true;
                    }
                    z2 = z6;
                    j2 = j;
                    messageObject3 = null;
                } else if (attachAlertLayout2 == this.documentLayout) {
                    boolean z13 = false;
                    int i20 = 0;
                    MessageObject messageObject9 = null;
                    for (int i21 = 0; i21 < this.documentLayout.selectedFilesOrder.size(); i21++) {
                        String str7 = (String) this.documentLayout.selectedFilesOrder.get(i21);
                        if (str7 != null) {
                            int lastIndexOf = str7.lastIndexOf(File.separator);
                            if (lastIndexOf < 0) {
                                substring = str7;
                                z3 = true;
                            } else {
                                z3 = true;
                                substring = str7.substring(lastIndexOf + 1);
                            }
                            if (!TextUtils.isEmpty(substring)) {
                                TLRPC.TL_message tL_message4 = new TLRPC.TL_message();
                                int i22 = i20 + 1;
                                tL_message4.id = i20;
                                tL_message4.out = z3;
                                tL_message4.from_id = MessagesController.getInstance(this.currentAccount).getPeer(UserConfig.getInstance(this.currentAccount).getClientUserId());
                                tL_message4.peer_id = MessagesController.getInstance(this.currentAccount).getPeer(j);
                                TLRPC.TL_messageMediaDocument tL_messageMediaDocument2 = new TLRPC.TL_messageMediaDocument();
                                tL_message4.media = tL_messageMediaDocument2;
                                tL_message4.attachPath = str7;
                                tL_messageMediaDocument2.document = new TLRPC.TL_document();
                                TLRPC.Document document = tL_message4.media.document;
                                document.file_name = substring;
                                document.size = new File(str7).length();
                                if (TextUtils.isEmpty(tL_message4.message) && i21 == 0) {
                                    z4 = true;
                                    z5 = false;
                                    CharSequence[] charSequenceArr4 = {this.commentTextView.getText()};
                                    tL_message4.entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr4, true);
                                    tL_message4.message = charSequenceArr4[0].toString();
                                } else {
                                    z4 = true;
                                    z5 = false;
                                }
                                MessageObject messageObject10 = new MessageObject(this.currentAccount, tL_message4, z4, z5);
                                messageObject10.attachPathExists = z4;
                                messageObject10.sendPreview = z4;
                                messageObject10.notime = z4;
                                messageObject10.isOutOwnerCached = Boolean.TRUE;
                                arrayList4.add(messageObject10);
                                if (i21 == 0 && messageObject9 == null && !TextUtils.isEmpty(tL_message4.message)) {
                                    messageObject9 = messageObject10;
                                }
                                i20 = i22;
                                z13 = true;
                            }
                        }
                    }
                    z2 = z13;
                    messageObject3 = messageObject9;
                    j2 = j;
                } else {
                    ChatAttachAlertAudioLayout chatAttachAlertAudioLayout = this.audioLayout;
                    if (attachAlertLayout2 == chatAttachAlertAudioLayout) {
                        arrayList4.addAll(chatAttachAlertAudioLayout.getSelected());
                        if (!arrayList4.isEmpty()) {
                            MessageObject messageObject11 = (MessageObject) arrayList4.get(0);
                            CharSequence[] charSequenceArr5 = {this.commentTextView.getText()};
                            MessageObject.addLinks(true, charSequenceArr5[0]);
                            messageObject11.messageOwner.entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr5, true);
                            messageObject11.messageOwner.message = charSequenceArr5[0].toString();
                            if (!TextUtils.isEmpty(messageObject11.messageOwner.message)) {
                                messageObject11.generateCaption();
                                messageObject5 = messageObject11;
                                if (arrayList4.size() > 1) {
                                    int i23 = 0;
                                    while (i23 < Math.ceil(arrayList4.size() / 10.0f)) {
                                        int i24 = i23 * 10;
                                        int min2 = Math.min(i6, arrayList4.size() - i24);
                                        long nextLong2 = Utilities.random.nextLong();
                                        for (int i25 = 0; i25 < min2; i25++) {
                                            int i26 = i24 + i25;
                                            if (i26 < arrayList4.size()) {
                                                ((MessageObject) arrayList4.get(i26)).messageOwner.grouped_id = nextLong2;
                                            }
                                        }
                                        i23++;
                                        i6 = 10;
                                    }
                                }
                                messageObject3 = messageObject5;
                                j2 = j;
                                z = false;
                                z2 = true;
                            }
                        }
                        messageObject5 = null;
                        if (arrayList4.size() > 1) {
                        }
                        messageObject3 = messageObject5;
                        j2 = j;
                        z = false;
                        z2 = true;
                    }
                    j2 = j;
                    messageObject3 = null;
                    z = false;
                    z2 = false;
                }
                z = false;
            }
            if (arrayList4.isEmpty()) {
                return false;
            }
            ?? makeOptions = ItemOptions.makeOptions(this.containerView, resourcesProvider, this.writeButton);
            if (messageObject3 == null || !((attachAlertLayout = this.currentAttachLayout) == this.photoLayout || attachAlertLayout == this.photoPreviewLayout)) {
                arrayList3 = arrayList4;
                j4 = j2;
                r13 = 0;
            } else {
                j4 = j2;
                arrayList3 = arrayList4;
                r13 = 0;
                final MessagePreviewView.ToggleButton toggleButton = new MessagePreviewView.ToggleButton(context, R.raw.position_below, LocaleController.getString(R.string.CaptionAbove), R.raw.position_above, LocaleController.getString(R.string.CaptionBelow), resourcesProvider);
                TLRPC.Message message = messageObject3.messageOwner;
                boolean z14 = this.captionAbove;
                message.invert_media = z14;
                toggleButton.setState(!z14, false);
                toggleButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda25
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatAttachAlert.this.lambda$new$20(messageObject3, toggleButton, view2);
                    }
                });
                makeOptions.addView(toggleButton);
                makeOptions.addGap();
            }
            boolean isUserSelf = UserObject.isUserSelf(user);
            if ((chatActivity != null && chatActivity.canScheduleMessage()) || this.currentAttachLayout.canScheduleMessages()) {
                makeOptions.add(R.drawable.msg_calendar2, LocaleController.getString(isUserSelf ? R.string.SetReminder : R.string.ScheduleMessage), new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda26
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatAttachAlert.this.lambda$new$22(j4, resourcesProvider);
                    }
                });
            }
            if (!isUserSelf) {
                makeOptions.add(R.drawable.input_notify_off, LocaleController.getString(R.string.SendWithoutSound), new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda27
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatAttachAlert.this.lambda$new$23();
                    }
                });
            }
            if (z && chatActivity != null && ChatObject.isChannelAndNotMegaGroup(chatActivity.getCurrentChat()) && chatActivity.getCurrentChatInfo() != null && chatActivity.getCurrentChatInfo().paid_media_allowed) {
                int i27 = R.drawable.menu_feature_paid;
                int i28 = R.string.PaidMediaButton;
                final ActionBarMenuSubItem last = makeOptions.add(i27, LocaleController.getString(i28), r13).getLast();
                last.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda28
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatAttachAlert.this.lambda$new$25(context, last, resourcesProvider, view2);
                    }
                });
                long starsPrice = this.photoLayout.getStarsPrice();
                if (starsPrice > 0) {
                    last.setText(LocaleController.getString(R.string.PaidMediaPriceButton));
                    last.setSubtext(LocaleController.formatPluralString("Stars", (int) starsPrice, new Object[0]));
                } else {
                    last.setText(LocaleController.getString(i28));
                    last.setSubtext(r13);
                }
                this.messageSendPreview.setStars(starsPrice);
            }
            makeOptions.setupSelectors();
            this.messageSendPreview.setItemOptions(makeOptions);
            this.messageSendPreview.setMessageObjects(arrayList3);
            if (j4 >= 0 && z2) {
                this.messageSendPreview.allowEffectSelector(baseFragment);
            }
            this.messageSendPreview.show();
            view.performHapticFeedback(3, 2);
            return true;
        }
        return false;
    }

    public /* synthetic */ void lambda$new$3(boolean z, View view) {
        if (this.avatarPicker != 0) {
            this.delegate.openAvatarsSearch();
            dismiss();
            return;
        }
        final HashMap hashMap = new HashMap();
        final ArrayList arrayList = new ArrayList();
        PhotoPickerSearchActivity photoPickerSearchActivity = new PhotoPickerSearchActivity(hashMap, arrayList, 0, true, (ChatActivity) this.baseFragment);
        photoPickerSearchActivity.setDelegate(new PhotoPickerActivity.PhotoPickerActivityDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert.7
            private boolean sendPressed;

            {
                ChatAttachAlert.this = this;
            }

            @Override // org.telegram.ui.PhotoPickerActivity.PhotoPickerActivityDelegate
            public void actionButtonPressed(boolean z2, boolean z3, int i) {
                if (z2 || hashMap.isEmpty() || this.sendPressed) {
                    return;
                }
                this.sendPressed = true;
                ArrayList arrayList2 = new ArrayList();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    Object obj = hashMap.get(arrayList.get(i2));
                    SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                    arrayList2.add(sendingMediaInfo);
                    MediaController.SearchImage searchImage = (MediaController.SearchImage) obj;
                    String str = searchImage.imagePath;
                    if (str != null) {
                        sendingMediaInfo.path = str;
                    } else {
                        sendingMediaInfo.searchImage = searchImage;
                    }
                    sendingMediaInfo.thumbPath = searchImage.thumbPath;
                    sendingMediaInfo.videoEditedInfo = searchImage.editedInfo;
                    CharSequence charSequence = searchImage.caption;
                    sendingMediaInfo.caption = charSequence != null ? charSequence.toString() : null;
                    sendingMediaInfo.entities = searchImage.entities;
                    sendingMediaInfo.masks = searchImage.stickers;
                    sendingMediaInfo.ttl = searchImage.ttl;
                    TLRPC.BotInlineResult botInlineResult = searchImage.inlineResult;
                    if (botInlineResult != null && searchImage.type == 1) {
                        sendingMediaInfo.inlineResult = botInlineResult;
                        sendingMediaInfo.params = searchImage.params;
                    }
                    searchImage.date = (int) (System.currentTimeMillis() / 1000);
                }
                ((ChatActivity) ChatAttachAlert.this.baseFragment).didSelectSearchPhotos(arrayList2, z3, i);
            }

            @Override // org.telegram.ui.PhotoPickerActivity.PhotoPickerActivityDelegate
            public /* synthetic */ boolean canFinishFragment() {
                return PhotoPickerActivity.PhotoPickerActivityDelegate.-CC.$default$canFinishFragment(this);
            }

            @Override // org.telegram.ui.PhotoPickerActivity.PhotoPickerActivityDelegate
            public void onCaptionChanged(CharSequence charSequence) {
            }

            @Override // org.telegram.ui.PhotoPickerActivity.PhotoPickerActivityDelegate
            public /* synthetic */ void onOpenInPressed() {
                PhotoPickerActivity.PhotoPickerActivityDelegate.-CC.$default$onOpenInPressed(this);
            }

            @Override // org.telegram.ui.PhotoPickerActivity.PhotoPickerActivityDelegate
            public void selectedPhotosChanged() {
            }
        });
        photoPickerSearchActivity.setMaxSelectedPhotos(this.maxSelectedPhotos, this.allowOrder);
        if (z) {
            this.baseFragment.showAsSheet(photoPickerSearchActivity);
        } else {
            this.baseFragment.presentFragment(photoPickerSearchActivity);
        }
        dismiss();
    }

    public /* synthetic */ void lambda$new$4(Theme.ResourcesProvider resourcesProvider, View view) {
        this.optionsItem.toggleSubMenu();
        PhotoViewer.getInstance().setParentActivity(this.baseFragment, resourcesProvider);
        PhotoViewer.getInstance().setParentAlert(this);
        PhotoViewer.getInstance().setMaxSelectedPhotos(this.maxSelectedPhotos, this.allowOrder);
        if (!this.delegate.needEnterComment()) {
            AndroidUtilities.hideKeyboard(this.baseFragment.getFragmentView().findFocus());
            AndroidUtilities.hideKeyboard(getContainer().findFocus());
        }
        File makeCacheFile = StoryEntry.makeCacheFile(this.currentAccount, "webp");
        android.graphics.Point point = AndroidUtilities.displaySize;
        int i = point.x;
        int i2 = point.y;
        if (i > 1080 || i2 > 1080) {
            float min = Math.min(i, i2) / 1080.0f;
            i = (int) (i * min);
            i2 = (int) (i2 * min);
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        try {
            createBitmap.compress(Bitmap.CompressFormat.WEBP, 100, new FileOutputStream(makeCacheFile));
        } catch (Throwable th) {
            FileLog.e(th);
        }
        createBitmap.recycle();
        ArrayList arrayList = new ArrayList();
        final MediaController.PhotoEntry photoEntry = new MediaController.PhotoEntry(0, 0, 0L, makeCacheFile.getAbsolutePath(), 0, false, 0, 0, 0L);
        arrayList.add(photoEntry);
        PhotoViewer photoViewer = PhotoViewer.getInstance();
        PhotoViewer.EmptyPhotoViewerProvider emptyPhotoViewerProvider = new PhotoViewer.EmptyPhotoViewerProvider() { // from class: org.telegram.ui.Components.ChatAttachAlert.8
            {
                ChatAttachAlert.this = this;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public boolean allowCaption() {
                return false;
            }

            @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
            public void sendButtonPressed(int i3, VideoEditedInfo videoEditedInfo, boolean z, int i4, boolean z2) {
                ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                chatAttachAlert.sent = true;
                if (chatAttachAlert.delegate == null) {
                    return;
                }
                photoEntry.editedInfo = videoEditedInfo;
                ChatAttachAlertPhotoLayout.selectedPhotosOrder.clear();
                ChatAttachAlertPhotoLayout.selectedPhotos.clear();
                ChatAttachAlertPhotoLayout.selectedPhotosOrder.add(0);
                ChatAttachAlertPhotoLayout.selectedPhotos.put(0, photoEntry);
                ChatAttachAlert chatAttachAlert2 = ChatAttachAlert.this;
                chatAttachAlert2.delegate.didPressedButton(7, true, z, i4, 0L, chatAttachAlert2.isCaptionAbove(), z2);
            }
        };
        BaseFragment baseFragment = this.baseFragment;
        photoViewer.openPhotoForSelect(arrayList, 0, 11, false, emptyPhotoViewerProvider, baseFragment instanceof ChatActivity ? (ChatActivity) baseFragment : null);
        if (this.isStickerMode) {
            PhotoViewer.getInstance().enableStickerMode(null, true, this.customStickerHandler);
        }
    }

    public /* synthetic */ void lambda$new$5(View view) {
        this.optionsItem.toggleSubMenu();
    }

    public /* synthetic */ void lambda$new$6(View view) {
        updatePhotoPreview(this.currentAttachLayout != this.photoPreviewLayout);
    }

    public /* synthetic */ void lambda$new$7(TLRPC.MessageMedia messageMedia, int i, boolean z, int i2) {
        ((ChatActivity) this.baseFragment).didSelectLocation(messageMedia, i, z, i2);
    }

    public /* synthetic */ void lambda$new$8(TLRPC.TL_messageMediaPoll tL_messageMediaPoll, HashMap hashMap, boolean z, int i) {
        ((ChatActivity) this.baseFragment).sendPoll(tL_messageMediaPoll, hashMap, z, i);
    }

    public /* synthetic */ void lambda$new$9(AttachBotButton attachBotButton) {
        TLRPC.TL_attachMenuBot tL_attachMenuBot = attachBotButton.attachMenuBot;
        attachBotButton.attachMenuBot.side_menu_disclaimer_needed = false;
        tL_attachMenuBot.inactive = false;
        showBotLayout(attachBotButton.attachMenuBot.bot_id, true);
        MediaDataController.getInstance(this.currentAccount).updateAttachMenuBotsInCache();
    }

    public /* synthetic */ void lambda$onCustomOpenAnimation$37(ValueAnimator valueAnimator) {
        this.navigationBarAlpha = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        BottomSheet.ContainerView containerView = this.container;
        if (containerView != null) {
            containerView.invalidate();
        }
    }

    public /* synthetic */ void lambda$onCustomOpenAnimation$38(AnimationNotificationsLocker animationNotificationsLocker, BottomSheet.BottomSheetDelegateInterface bottomSheetDelegateInterface) {
        this.currentSheetAnimation = null;
        this.appearSpringAnimation = null;
        animationNotificationsLocker.unlock();
        this.currentSheetAnimationType = 0;
        if (bottomSheetDelegateInterface != null) {
            bottomSheetDelegateInterface.onOpenAnimationEnd();
        }
        if (this.useHardwareLayer) {
            this.container.setLayerType(0, null);
        }
        if (this.isFullscreen) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.flags &= -1025;
            getWindow().setAttributes(attributes);
        }
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 512);
    }

    public /* synthetic */ void lambda$onCustomOpenAnimation$39(Runnable runnable, DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
        AnimatorSet animatorSet = this.currentSheetAnimation;
        if (animatorSet == null || animatorSet.isRunning()) {
            return;
        }
        runnable.run();
    }

    public /* synthetic */ void lambda$onCustomOpenAnimation$40(ValueAnimator valueAnimator) {
        setNavBarAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public /* synthetic */ void lambda$onLongClickBotButton$28(TLRPC.TL_attachMenuBot tL_attachMenuBot) {
        MediaDataController.getInstance(this.currentAccount).loadAttachMenuBots(false, true);
        if (this.currentAttachLayout == this.botAttachLayouts.get(tL_attachMenuBot.bot_id)) {
            showLayout(this.photoLayout);
        }
    }

    public /* synthetic */ void lambda$onLongClickBotButton$29(final TLRPC.TL_attachMenuBot tL_attachMenuBot, TLObject tLObject, TLRPC.TL_error tL_error) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda46
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlert.this.lambda$onLongClickBotButton$28(tL_attachMenuBot);
            }
        });
    }

    public /* synthetic */ void lambda$onLongClickBotButton$30(final TLRPC.TL_attachMenuBot tL_attachMenuBot, TLRPC.User user, DialogInterface dialogInterface, int i) {
        if (tL_attachMenuBot == null) {
            MediaDataController.getInstance(this.currentAccount).removeInline(user.id);
            return;
        }
        TLRPC.TL_messages_toggleBotInAttachMenu tL_messages_toggleBotInAttachMenu = new TLRPC.TL_messages_toggleBotInAttachMenu();
        tL_messages_toggleBotInAttachMenu.bot = MessagesController.getInstance(this.currentAccount).getInputUser(user);
        tL_messages_toggleBotInAttachMenu.enabled = false;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_toggleBotInAttachMenu, new RequestDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda42
            @Override // org.telegram.tgnet.RequestDelegate
            public final void run(TLObject tLObject, TLRPC.TL_error tL_error) {
                ChatAttachAlert.this.lambda$onLongClickBotButton$29(tL_attachMenuBot, tLObject, tL_error);
            }
        }, 66);
    }

    public /* synthetic */ void lambda$openAudioLayout$35(ArrayList arrayList, CharSequence charSequence, boolean z, int i, long j, boolean z2) {
        BaseFragment baseFragment = this.baseFragment;
        if (baseFragment != null && (baseFragment instanceof ChatActivity)) {
            ((ChatActivity) baseFragment).sendAudio(arrayList, charSequence, z, i, j, z2);
            return;
        }
        ChatAttachViewDelegate chatAttachViewDelegate = this.delegate;
        if (chatAttachViewDelegate != null) {
            chatAttachViewDelegate.sendAudio(arrayList, charSequence, z, i, j, z2);
        }
    }

    public /* synthetic */ void lambda$openColorsLayout$36(Object obj) {
        ChatAttachViewDelegate chatAttachViewDelegate = this.delegate;
        if (chatAttachViewDelegate != null) {
            chatAttachViewDelegate.onWallpaperSelected(obj);
        }
    }

    public /* synthetic */ void lambda$showCaptionLimitBulletin$27(BaseFragment baseFragment) {
        dismiss(true);
        if (baseFragment != null) {
            baseFragment.presentFragment(new PremiumPreviewFragment("caption_limit"));
        }
    }

    public /* synthetic */ void lambda$showLayout$31() {
        AttachAlertLayout attachAlertLayout;
        ChatAttachAlertPhotoLayoutPreview chatAttachAlertPhotoLayoutPreview;
        if (Build.VERSION.SDK_INT >= 20) {
            this.container.setLayerType(0, null);
        }
        this.viewChangeAnimator = null;
        AttachAlertLayout attachAlertLayout2 = this.currentAttachLayout;
        if (attachAlertLayout2 != this.photoLayout && (attachAlertLayout = this.nextAttachLayout) != (chatAttachAlertPhotoLayoutPreview = this.photoPreviewLayout) && attachAlertLayout2 != attachAlertLayout && attachAlertLayout2 != chatAttachAlertPhotoLayoutPreview) {
            this.containerView.removeView(attachAlertLayout2);
        }
        this.currentAttachLayout.setVisibility(8);
        this.currentAttachLayout.onHidden();
        this.nextAttachLayout.onShown();
        this.currentAttachLayout = this.nextAttachLayout;
        this.nextAttachLayout = null;
        int[] iArr = this.scrollOffsetY;
        iArr[0] = iArr[1];
    }

    public /* synthetic */ void lambda$showLayout$32(float f, float f2, boolean z, DynamicAnimation dynamicAnimation, float f3, float f4) {
        float f5 = f3 / 500.0f;
        this.ATTACH_ALERT_LAYOUT_TRANSLATION.set(this.currentAttachLayout, Float.valueOf(f5));
        this.actionBar.setAlpha(AndroidUtilities.lerp(f, f2, f5));
        updateLayout(this.currentAttachLayout, false, 0);
        updateLayout(this.nextAttachLayout, false, 0);
        if (!(this.nextAttachLayout instanceof ChatAttachAlertPhotoLayoutPreview) || z) {
            f5 = 1.0f - f5;
        }
        float clamp = Utilities.clamp(f5, 1.0f, 0.0f);
        this.mediaPreviewView.setAlpha(clamp);
        float f6 = 1.0f - clamp;
        this.selectedView.setAlpha(f6);
        this.selectedView.setTranslationX(clamp * (-AndroidUtilities.dp(16.0f)));
        this.mediaPreviewView.setTranslationX(f6 * AndroidUtilities.dp(16.0f));
    }

    public /* synthetic */ void lambda$showLayout$33(boolean z, Runnable runnable, DynamicAnimation dynamicAnimation, boolean z2, float f, float f2) {
        this.currentAttachLayout.onHideShowProgress(1.0f);
        this.nextAttachLayout.onHideShowProgress(1.0f);
        this.currentAttachLayout.onContainerTranslationUpdated(this.currentPanTranslationY);
        this.nextAttachLayout.onContainerTranslationUpdated(this.currentPanTranslationY);
        this.containerView.invalidate();
        this.actionBar.setTag(z ? 1 : null);
        runnable.run();
    }

    public /* synthetic */ void lambda$showLayout$34(AttachAlertLayout attachAlertLayout, final Runnable runnable) {
        final boolean z = this.nextAttachLayout.getCurrentItemTop() <= attachAlertLayout.getButtonsHideOffset();
        final float alpha = this.actionBar.getAlpha();
        final float f = z ? 1.0f : 0.0f;
        SpringAnimation springAnimation = new SpringAnimation(new FloatValueHolder(0.0f));
        springAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda36
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
            public final void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f2, float f3) {
                ChatAttachAlert.this.lambda$showLayout$32(alpha, f, z, dynamicAnimation, f2, f3);
            }
        });
        springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda37
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
            public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z2, float f2, float f3) {
                ChatAttachAlert.this.lambda$showLayout$33(z, runnable, dynamicAnimation, z2, f2, f3);
            }
        });
        springAnimation.setSpring(new SpringForce(500.0f));
        springAnimation.getSpring().setDampingRatio(1.0f);
        springAnimation.getSpring().setStiffness(1000.0f);
        springAnimation.start();
        this.viewChangeAnimator = springAnimation;
    }

    public void openAudioLayout(boolean z) {
        if (!this.musicEnabled && z) {
            ChatAttachRestrictedLayout chatAttachRestrictedLayout = new ChatAttachRestrictedLayout(3, this, getContext(), this.resourcesProvider);
            this.restrictedLayout = chatAttachRestrictedLayout;
            showLayout(chatAttachRestrictedLayout);
        }
        if (this.audioLayout == null) {
            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
            ChatAttachAlertAudioLayout chatAttachAlertAudioLayout = new ChatAttachAlertAudioLayout(this, getContext(), this.resourcesProvider);
            this.audioLayout = chatAttachAlertAudioLayout;
            attachAlertLayoutArr[3] = chatAttachAlertAudioLayout;
            chatAttachAlertAudioLayout.setDelegate(new ChatAttachAlertAudioLayout.AudioSelectDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda45
                @Override // org.telegram.ui.Components.ChatAttachAlertAudioLayout.AudioSelectDelegate
                public final void didSelectAudio(ArrayList arrayList, CharSequence charSequence, boolean z2, int i, long j, boolean z3) {
                    ChatAttachAlert.this.lambda$openAudioLayout$35(arrayList, charSequence, z2, i, j, z3);
                }
            });
        }
        BaseFragment baseFragment = this.baseFragment;
        if (baseFragment instanceof ChatActivity) {
            TLRPC.Chat currentChat = ((ChatActivity) baseFragment).getCurrentChat();
            this.audioLayout.setMaxSelectedFiles(((currentChat == null || ChatObject.hasAdminRights(currentChat) || !currentChat.slowmode_enabled) && this.editingMessageObject == null) ? -1 : 1);
        }
        if (z) {
            showLayout(this.audioLayout);
        }
    }

    private void openContactsLayout() {
        if (!this.plainTextEnabled) {
            ChatAttachRestrictedLayout chatAttachRestrictedLayout = new ChatAttachRestrictedLayout(5, this, getContext(), this.resourcesProvider);
            this.restrictedLayout = chatAttachRestrictedLayout;
            showLayout(chatAttachRestrictedLayout);
        }
        if (this.contactsLayout == null) {
            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
            ChatAttachAlertContactsLayout chatAttachAlertContactsLayout = new ChatAttachAlertContactsLayout(this, getContext(), this.resourcesProvider);
            this.contactsLayout = chatAttachAlertContactsLayout;
            attachAlertLayoutArr[2] = chatAttachAlertContactsLayout;
            chatAttachAlertContactsLayout.setDelegate(new ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert.18
                {
                    ChatAttachAlert.this = this;
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                public void didSelectContact(TLRPC.User user, boolean z, int i, long j, boolean z2) {
                    ((ChatActivity) ChatAttachAlert.this.baseFragment).sendContact(user, z, i, j, z2);
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertContactsLayout.PhonebookShareAlertDelegate
                public void didSelectContacts(ArrayList arrayList, String str, boolean z, int i, long j, boolean z2) {
                    ((ChatActivity) ChatAttachAlert.this.baseFragment).sendContacts(arrayList, str, z, i, j, z2);
                }
            });
        }
        BaseFragment baseFragment = this.baseFragment;
        if (baseFragment instanceof ChatActivity) {
            TLRPC.Chat currentChat = ((ChatActivity) baseFragment).getCurrentChat();
            this.contactsLayout.setMultipleSelectionAllowed(currentChat == null || ChatObject.hasAdminRights(currentChat) || !currentChat.slowmode_enabled);
        }
        showLayout(this.contactsLayout);
    }

    private void openDocumentsLayout(boolean z) {
        if (!this.documentsEnabled && z) {
            ChatAttachRestrictedLayout chatAttachRestrictedLayout = new ChatAttachRestrictedLayout(4, this, getContext(), this.resourcesProvider);
            this.restrictedLayout = chatAttachRestrictedLayout;
            showLayout(chatAttachRestrictedLayout);
        }
        boolean z2 = false;
        if (this.documentLayout == null) {
            int i = this.isSoundPicker ? 2 : 0;
            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
            ChatAttachAlertDocumentLayout chatAttachAlertDocumentLayout = new ChatAttachAlertDocumentLayout(this, getContext(), i, this.resourcesProvider);
            this.documentLayout = chatAttachAlertDocumentLayout;
            attachAlertLayoutArr[4] = chatAttachAlertDocumentLayout;
            chatAttachAlertDocumentLayout.setDelegate(new ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert.19
                {
                    ChatAttachAlert.this = this;
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public void didSelectFiles(ArrayList arrayList, String str, ArrayList arrayList2, boolean z3, int i2, long j, boolean z4) {
                    if (ChatAttachAlert.this.documentsDelegate != null) {
                        ChatAttachAlert.this.documentsDelegate.didSelectFiles(arrayList, str, arrayList2, z3, i2, j, z4);
                        return;
                    }
                    BaseFragment baseFragment = ChatAttachAlert.this.baseFragment;
                    if (baseFragment instanceof ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate) {
                        ((ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate) baseFragment).didSelectFiles(arrayList, str, arrayList2, z3, i2, j, z4);
                    } else if (baseFragment instanceof PassportActivity) {
                        ((PassportActivity) baseFragment).didSelectFiles(arrayList, str, z3, i2, j, z4);
                    }
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public void didSelectPhotos(ArrayList arrayList, boolean z3, int i2) {
                    if (ChatAttachAlert.this.documentsDelegate != null) {
                        ChatAttachAlert.this.documentsDelegate.didSelectPhotos(arrayList, z3, i2);
                        return;
                    }
                    BaseFragment baseFragment = ChatAttachAlert.this.baseFragment;
                    if (baseFragment instanceof ChatActivity) {
                        ((ChatActivity) baseFragment).didSelectPhotos(arrayList, z3, i2);
                    } else if (baseFragment instanceof PassportActivity) {
                        ((PassportActivity) baseFragment).didSelectPhotos(arrayList, z3, i2);
                    }
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public void startDocumentSelectActivity() {
                    ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate documentSelectActivityDelegate;
                    if (ChatAttachAlert.this.documentsDelegate != null) {
                        documentSelectActivityDelegate = ChatAttachAlert.this.documentsDelegate;
                    } else {
                        BaseFragment baseFragment = ChatAttachAlert.this.baseFragment;
                        if (!(baseFragment instanceof ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate)) {
                            if (baseFragment instanceof PassportActivity) {
                                ((PassportActivity) baseFragment).startDocumentSelectActivity();
                                return;
                            }
                            return;
                        }
                        documentSelectActivityDelegate = (ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate) baseFragment;
                    }
                    documentSelectActivityDelegate.startDocumentSelectActivity();
                }

                @Override // org.telegram.ui.Components.ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate
                public void startMusicSelectActivity() {
                    ChatAttachAlert.this.openAudioLayout(true);
                }
            });
        }
        BaseFragment baseFragment = this.baseFragment;
        int i2 = 1;
        if (baseFragment instanceof ChatActivity) {
            TLRPC.Chat currentChat = ((ChatActivity) baseFragment).getCurrentChat();
            ChatAttachAlertDocumentLayout chatAttachAlertDocumentLayout2 = this.documentLayout;
            if ((currentChat == null || ChatObject.hasAdminRights(currentChat) || !currentChat.slowmode_enabled) && this.editingMessageObject == null) {
                i2 = -1;
            }
            chatAttachAlertDocumentLayout2.setMaxSelectedFiles(i2);
        } else {
            this.documentLayout.setMaxSelectedFiles(this.maxSelectedPhotos);
            ChatAttachAlertDocumentLayout chatAttachAlertDocumentLayout3 = this.documentLayout;
            if (!this.isSoundPicker && !this.allowEnterCaption) {
                z2 = true;
            }
            chatAttachAlertDocumentLayout3.setCanSelectOnlyImageFiles(z2);
        }
        ChatAttachAlertDocumentLayout chatAttachAlertDocumentLayout4 = this.documentLayout;
        chatAttachAlertDocumentLayout4.isSoundPicker = this.isSoundPicker;
        if (z) {
            showLayout(chatAttachAlertDocumentLayout4);
        }
    }

    private void openQuickRepliesLayout() {
        if (this.quickRepliesLayout == null) {
            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
            ChatAttachAlertQuickRepliesLayout chatAttachAlertQuickRepliesLayout = new ChatAttachAlertQuickRepliesLayout(this, getContext(), this.resourcesProvider);
            this.quickRepliesLayout = chatAttachAlertQuickRepliesLayout;
            attachAlertLayoutArr[7] = chatAttachAlertQuickRepliesLayout;
        }
        showLayout(this.quickRepliesLayout);
    }

    public void removeFromRoot() {
        ViewGroup viewGroup = this.containerView;
        if (viewGroup != null) {
            viewGroup.setVisibility(4);
        }
        if (this.actionBar.isSearchFieldVisible()) {
            this.actionBar.closeSearchField();
        }
        this.contactsLayout = null;
        this.quickRepliesLayout = null;
        this.audioLayout = null;
        this.pollLayout = null;
        this.locationLayout = null;
        this.documentLayout = null;
        int i = 1;
        while (true) {
            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
            if (i >= attachAlertLayoutArr.length) {
                updateActionBarVisibility(false, false);
                super.dismissInternal();
                return;
            }
            AttachAlertLayout attachAlertLayout = attachAlertLayoutArr[i];
            if (attachAlertLayout != null) {
                attachAlertLayout.onDestroy();
                this.containerView.removeView(this.layouts[i]);
                this.layouts[i] = null;
            }
            i++;
        }
    }

    public void replaceWithText(int i, int i2, CharSequence charSequence, boolean z) {
        if (this.commentTextView == null) {
            return;
        }
        try {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.commentTextView.getText());
            spannableStringBuilder.replace(i, i2 + i, charSequence);
            if (z) {
                Emoji.replaceEmoji((CharSequence) spannableStringBuilder, this.commentTextView.getEditText().getPaint().getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            }
            this.commentTextView.setText(spannableStringBuilder);
            this.commentTextView.setSelection(i + charSequence.length());
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void sendPressed(boolean z, int i, long j, boolean z2) {
        if (this.buttonPressed) {
            return;
        }
        BaseFragment baseFragment = this.baseFragment;
        if (baseFragment instanceof ChatActivity) {
            ChatActivity chatActivity = (ChatActivity) baseFragment;
            TLRPC.Chat currentChat = chatActivity.getCurrentChat();
            if (chatActivity.getCurrentUser() != null || ((ChatObject.isChannel(currentChat) && currentChat.megagroup) || !ChatObject.isChannel(currentChat))) {
                SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                edit.putBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + chatActivity.getDialogId(), !z).commit();
            }
        }
        if (checkCaption(this.commentTextView.getText())) {
            return;
        }
        applyCaption();
        this.buttonPressed = true;
        this.delegate.didPressedButton(7, true, z, i, j, z2, false);
    }

    private void setNavBarAlpha(float f) {
        this.navBarColor = ColorUtils.setAlphaComponent(getThemedColor(Theme.key_windowBackgroundGray), Math.min((int) NotificationCenter.messagePlayingSpeedChanged, Math.max(0, (int) (f * 255.0f))));
        AndroidUtilities.setNavigationBarColor(getWindow(), this.navBarColor, false);
        AndroidUtilities.setLightNavigationBar(getWindow(), ((double) AndroidUtilities.computePerceivedBrightness(this.navBarColor)) > 0.721d);
        getContainer().invalidate();
    }

    private void showCaptionLimitBulletin(final BaseFragment baseFragment) {
        if ((baseFragment instanceof ChatActivity) && ChatObject.isChannelAndNotMegaGroup(((ChatActivity) baseFragment).getCurrentChat())) {
            BulletinFactory.of(this.sizeNotifierFrameLayout, this.resourcesProvider).createCaptionLimitBulletin(MessagesController.getInstance(this.currentAccount).captionLengthLimitPremium, new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda44
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlert.this.lambda$showCaptionLimitBulletin$27(baseFragment);
                }
            }).show();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:304:0x025c, code lost:
        if (r18 != false) goto L134;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x029c, code lost:
        if (r18 != false) goto L134;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x029e, code lost:
        r9 = 1.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x02a1, code lost:
        r9 = 0.0f;
     */
    /* JADX WARN: Removed duplicated region for block: B:328:0x02a7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean showCommentTextView(final boolean z, boolean z2) {
        View view;
        float f;
        ObjectAnimator ofFloat;
        if (z == (this.frameLayout2.getTag() != null)) {
            return false;
        }
        AnimatorSet animatorSet = this.commentsAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        this.frameLayout2.setTag(z ? 1 : null);
        if (this.commentTextView.getEditText().isFocused()) {
            AndroidUtilities.hideKeyboard(this.commentTextView.getEditText());
        }
        this.commentTextView.hidePopup(true);
        if (z) {
            if (!this.isSoundPicker) {
                this.frameLayout2.setVisibility(0);
            }
            this.writeButtonContainer.setVisibility(0);
            if (!this.typeButtonsAvailable && !this.isSoundPicker) {
                this.shadow.setVisibility(0);
            }
        } else if (this.typeButtonsAvailable) {
            this.buttonsRecyclerView.setVisibility(0);
        }
        if (z2) {
            this.commentsAnimator = new AnimatorSet();
            ArrayList arrayList = new ArrayList();
            FrameLayout frameLayout = this.frameLayout2;
            Property property = View.ALPHA;
            arrayList.add(ObjectAnimator.ofFloat(frameLayout, property, z ? 1.0f : 0.0f));
            FrameLayout frameLayout2 = this.writeButtonContainer;
            Property property2 = View.SCALE_X;
            arrayList.add(ObjectAnimator.ofFloat(frameLayout2, property2, z ? 1.0f : 0.2f));
            FrameLayout frameLayout3 = this.writeButtonContainer;
            Property property3 = View.SCALE_Y;
            arrayList.add(ObjectAnimator.ofFloat(frameLayout3, property3, z ? 1.0f : 0.2f));
            arrayList.add(ObjectAnimator.ofFloat(this.writeButtonContainer, property, z ? 1.0f : 0.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.writeButton, property2, z ? 1.0f : 0.2f));
            arrayList.add(ObjectAnimator.ofFloat(this.writeButton, property3, z ? 1.0f : 0.2f));
            arrayList.add(ObjectAnimator.ofFloat(this.writeButton, property, z ? 1.0f : 0.0f));
            if (this.actionBar.getTag() != null) {
                FrameLayout frameLayout4 = this.frameLayout2;
                Property property4 = View.TRANSLATION_Y;
                arrayList.add(ObjectAnimator.ofFloat(frameLayout4, property4, z ? 0.0f : AndroidUtilities.dp(48.0f)));
                arrayList.add(ObjectAnimator.ofFloat(this.shadow, property4, z ? AndroidUtilities.dp(36.0f) : AndroidUtilities.dp(84.0f)));
                ofFloat = ObjectAnimator.ofFloat(this.shadow, property, z ? 1.0f : 0.0f);
            } else if (this.typeButtonsAvailable) {
                RecyclerListView recyclerListView = this.buttonsRecyclerView;
                Property property5 = View.TRANSLATION_Y;
                arrayList.add(ObjectAnimator.ofFloat(recyclerListView, property5, z ? AndroidUtilities.dp(36.0f) : 0.0f));
                ofFloat = ObjectAnimator.ofFloat(this.shadow, property5, z ? AndroidUtilities.dp(36.0f) : 0.0f);
            } else {
                if (!this.isSoundPicker) {
                    this.shadow.setTranslationY(AndroidUtilities.dp(36.0f) + this.botMainButtonOffsetY);
                    ofFloat = ObjectAnimator.ofFloat(this.shadow, property, z ? 1.0f : 0.0f);
                }
                this.commentsAnimator.playTogether(arrayList);
                this.commentsAnimator.setInterpolator(new DecelerateInterpolator());
                this.commentsAnimator.setDuration(180L);
                this.commentsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlert.20
                    {
                        ChatAttachAlert.this = this;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        if (animator.equals(ChatAttachAlert.this.commentsAnimator)) {
                            ChatAttachAlert.this.commentsAnimator = null;
                        }
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (animator.equals(ChatAttachAlert.this.commentsAnimator)) {
                            if (z) {
                                ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                                if (chatAttachAlert.typeButtonsAvailable && (chatAttachAlert.currentAttachLayout == null || ChatAttachAlert.this.currentAttachLayout.shouldHideBottomButtons())) {
                                    ChatAttachAlert.this.buttonsRecyclerView.setVisibility(4);
                                }
                            } else {
                                if (!ChatAttachAlert.this.isSoundPicker) {
                                    ChatAttachAlert.this.frameLayout2.setVisibility(4);
                                }
                                ChatAttachAlert.this.writeButtonContainer.setVisibility(4);
                                ChatAttachAlert chatAttachAlert2 = ChatAttachAlert.this;
                                if (!chatAttachAlert2.typeButtonsAvailable && !chatAttachAlert2.isSoundPicker) {
                                    ChatAttachAlert.this.shadow.setVisibility(4);
                                }
                            }
                            ChatAttachAlert.this.commentsAnimator = null;
                        }
                    }
                });
                this.commentsAnimator.start();
            }
            arrayList.add(ofFloat);
            this.commentsAnimator.playTogether(arrayList);
            this.commentsAnimator.setInterpolator(new DecelerateInterpolator());
            this.commentsAnimator.setDuration(180L);
            this.commentsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlert.20
                {
                    ChatAttachAlert.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (animator.equals(ChatAttachAlert.this.commentsAnimator)) {
                        ChatAttachAlert.this.commentsAnimator = null;
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator.equals(ChatAttachAlert.this.commentsAnimator)) {
                        if (z) {
                            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                            if (chatAttachAlert.typeButtonsAvailable && (chatAttachAlert.currentAttachLayout == null || ChatAttachAlert.this.currentAttachLayout.shouldHideBottomButtons())) {
                                ChatAttachAlert.this.buttonsRecyclerView.setVisibility(4);
                            }
                        } else {
                            if (!ChatAttachAlert.this.isSoundPicker) {
                                ChatAttachAlert.this.frameLayout2.setVisibility(4);
                            }
                            ChatAttachAlert.this.writeButtonContainer.setVisibility(4);
                            ChatAttachAlert chatAttachAlert2 = ChatAttachAlert.this;
                            if (!chatAttachAlert2.typeButtonsAvailable && !chatAttachAlert2.isSoundPicker) {
                                ChatAttachAlert.this.shadow.setVisibility(4);
                            }
                        }
                        ChatAttachAlert.this.commentsAnimator = null;
                    }
                }
            });
            this.commentsAnimator.start();
        } else {
            this.frameLayout2.setAlpha(z ? 1.0f : 0.0f);
            this.writeButtonContainer.setScaleX(z ? 1.0f : 0.2f);
            this.writeButtonContainer.setScaleY(z ? 1.0f : 0.2f);
            this.writeButtonContainer.setAlpha(z ? 1.0f : 0.0f);
            this.writeButton.setScaleX(z ? 1.0f : 0.2f);
            this.writeButton.setScaleY(z ? 1.0f : 0.2f);
            this.writeButton.setAlpha(z ? 1.0f : 0.0f);
            if (this.actionBar.getTag() != null) {
                this.frameLayout2.setTranslationY(z ? 0.0f : AndroidUtilities.dp(48.0f));
                this.shadow.setTranslationY((z ? AndroidUtilities.dp(36.0f) : AndroidUtilities.dp(84.0f)) + this.botMainButtonOffsetY);
                view = this.shadow;
            } else if (this.typeButtonsAvailable) {
                AttachAlertLayout attachAlertLayout = this.currentAttachLayout;
                if (attachAlertLayout == null || attachAlertLayout.shouldHideBottomButtons()) {
                    this.buttonsRecyclerView.setTranslationY(z ? AndroidUtilities.dp(36.0f) : 0.0f);
                }
                this.shadow.setTranslationY((z ? AndroidUtilities.dp(36.0f) : 0) + this.botMainButtonOffsetY);
                if (!z) {
                    this.frameLayout2.setVisibility(4);
                    this.writeButtonContainer.setVisibility(4);
                    if (!this.typeButtonsAvailable) {
                        this.shadow.setVisibility(4);
                    }
                }
            } else {
                this.shadow.setTranslationY(AndroidUtilities.dp(36.0f) + this.botMainButtonOffsetY);
                view = this.shadow;
            }
            view.setAlpha(f);
            if (!z) {
            }
        }
        this.writeButton.setCount(z ? Math.max(1, this.currentAttachLayout.getSelectedItemsCount()) : 0, z2);
        return true;
    }

    private void showLayout(AttachAlertLayout attachAlertLayout, long j) {
        showLayout(attachAlertLayout, j, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:157:0x020b  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x021f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void showLayout(final AttachAlertLayout attachAlertLayout, long j, boolean z) {
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout;
        CameraView cameraView;
        View view;
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout2;
        CameraView cameraView2;
        if (this.viewChangeAnimator != null || this.commentsAnimator != null) {
            return;
        }
        AttachAlertLayout attachAlertLayout2 = this.currentAttachLayout;
        if (attachAlertLayout2 == attachAlertLayout) {
            attachAlertLayout2.scrollToTop();
            return;
        }
        this.botButtonWasVisible = false;
        this.botButtonProgressWasVisible = false;
        this.botMainButtonOffsetY = 0.0f;
        this.botMainButtonTextView.setVisibility(8);
        this.botProgressView.setAlpha(0.0f);
        this.botProgressView.setScaleX(0.1f);
        this.botProgressView.setScaleY(0.1f);
        this.botProgressView.setVisibility(8);
        this.buttonsRecyclerView.setAlpha(1.0f);
        this.buttonsRecyclerView.setTranslationY(this.botMainButtonOffsetY);
        for (int i = 0; i < this.botAttachLayouts.size(); i++) {
            ((ChatAttachAlertBotWebViewLayout) this.botAttachLayouts.valueAt(i)).setMeasureOffsetY(0);
        }
        this.selectedId = j;
        int childCount = this.buttonsRecyclerView.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = this.buttonsRecyclerView.getChildAt(i2);
            if (childAt instanceof AttachButton) {
                ((AttachButton) childAt).updateCheckedState(true);
            } else if (childAt instanceof AttachBotButton) {
                ((AttachBotButton) childAt).updateCheckedState(true);
            }
        }
        int firstOffset = (this.currentAttachLayout.getFirstOffset() - AndroidUtilities.dp(11.0f)) - this.scrollOffsetY[0];
        this.nextAttachLayout = attachAlertLayout;
        if (Build.VERSION.SDK_INT >= 20) {
            this.container.setLayerType(2, null);
        }
        this.actionBar.setVisibility(this.nextAttachLayout.needsActionBar() != 0 ? 0 : 4);
        this.actionBarShadow.setVisibility(this.actionBar.getVisibility());
        if (this.actionBar.isSearchFieldVisible()) {
            this.actionBar.closeSearchField();
        }
        this.currentAttachLayout.onHide();
        AttachAlertLayout attachAlertLayout3 = this.nextAttachLayout;
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout3 = this.photoLayout;
        if (attachAlertLayout3 == chatAttachAlertPhotoLayout3) {
            chatAttachAlertPhotoLayout3.setCheckCameraWhenShown(true);
        }
        this.nextAttachLayout.onShow(this.currentAttachLayout);
        this.nextAttachLayout.setVisibility(0);
        if (attachAlertLayout.getParent() != null) {
            this.containerView.removeView(this.nextAttachLayout);
        }
        int indexOfChild = this.containerView.indexOfChild(this.currentAttachLayout);
        ViewParent parent = this.nextAttachLayout.getParent();
        ViewGroup viewGroup = this.containerView;
        if (parent != viewGroup) {
            AttachAlertLayout attachAlertLayout4 = this.nextAttachLayout;
            if (attachAlertLayout4 != this.locationLayout) {
                indexOfChild++;
            }
            viewGroup.addView(attachAlertLayout4, indexOfChild, LayoutHelper.createFrame(-1, -1.0f));
        }
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlert.this.lambda$showLayout$31();
            }
        };
        AttachAlertLayout attachAlertLayout5 = this.currentAttachLayout;
        if (!(attachAlertLayout5 instanceof ChatAttachAlertPhotoLayoutPreview) && !(this.nextAttachLayout instanceof ChatAttachAlertPhotoLayoutPreview)) {
            if (!z) {
                attachAlertLayout5.setAlpha(0.0f);
                runnable.run();
                updateSelectedPosition(0);
                this.containerView.invalidate();
                return;
            }
            AnimatorSet animatorSet = new AnimatorSet();
            this.nextAttachLayout.setAlpha(0.0f);
            this.nextAttachLayout.setTranslationY(AndroidUtilities.dp(78.0f));
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.currentAttachLayout, View.TRANSLATION_Y, AndroidUtilities.dp(78.0f) + firstOffset);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.currentAttachLayout, this.ATTACH_ALERT_LAYOUT_TRANSLATION, 0.0f, 1.0f);
            ActionBar actionBar = this.actionBar;
            animatorSet.playTogether(ofFloat, ofFloat2, ObjectAnimator.ofFloat(actionBar, View.ALPHA, actionBar.getAlpha(), 0.0f));
            animatorSet.setDuration(180L);
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.addListener(new 17(firstOffset, runnable));
            this.viewChangeAnimator = animatorSet;
            this.ATTACH_ALERT_LAYOUT_TRANSLATION.set(this.currentAttachLayout, Float.valueOf(0.0f));
            animatorSet.start();
            return;
        }
        int max = Math.max(this.nextAttachLayout.getWidth(), this.currentAttachLayout.getWidth());
        AttachAlertLayout attachAlertLayout6 = this.nextAttachLayout;
        if (attachAlertLayout6 instanceof ChatAttachAlertPhotoLayoutPreview) {
            attachAlertLayout6.setTranslationX(max);
            AttachAlertLayout attachAlertLayout7 = this.currentAttachLayout;
            if ((attachAlertLayout7 instanceof ChatAttachAlertPhotoLayout) && (cameraView2 = (chatAttachAlertPhotoLayout2 = (ChatAttachAlertPhotoLayout) attachAlertLayout7).cameraView) != null) {
                cameraView2.setVisibility(4);
                chatAttachAlertPhotoLayout2.cameraIcon.setVisibility(4);
                view = chatAttachAlertPhotoLayout2.cameraCell;
                view.setVisibility(0);
            }
            this.nextAttachLayout.setAlpha(1.0f);
            this.currentAttachLayout.setAlpha(1.0f);
            if (!z) {
                this.ATTACH_ALERT_LAYOUT_TRANSLATION.set(this.currentAttachLayout, Float.valueOf(0.0f));
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda6
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatAttachAlert.this.lambda$showLayout$34(attachAlertLayout, runnable);
                    }
                });
                return;
            }
            boolean z2 = this.nextAttachLayout.getCurrentItemTop() <= attachAlertLayout.getButtonsHideOffset();
            this.currentAttachLayout.onHideShowProgress(1.0f);
            this.nextAttachLayout.onHideShowProgress(1.0f);
            this.currentAttachLayout.onContainerTranslationUpdated(this.currentPanTranslationY);
            this.nextAttachLayout.onContainerTranslationUpdated(this.currentPanTranslationY);
            this.containerView.invalidate();
            this.ATTACH_ALERT_LAYOUT_TRANSLATION.set(this.currentAttachLayout, Float.valueOf(1.0f));
            this.actionBar.setTag(z2 ? 1 : null);
            runnable.run();
            return;
        }
        this.currentAttachLayout.setTranslationX(-max);
        AttachAlertLayout attachAlertLayout8 = this.nextAttachLayout;
        if (attachAlertLayout8 == this.photoLayout && (cameraView = (chatAttachAlertPhotoLayout = (ChatAttachAlertPhotoLayout) attachAlertLayout8).cameraView) != null) {
            cameraView.setVisibility(0);
            view = chatAttachAlertPhotoLayout.cameraIcon;
            view.setVisibility(0);
        }
        this.nextAttachLayout.setAlpha(1.0f);
        this.currentAttachLayout.setAlpha(1.0f);
        if (!z) {
        }
    }

    private void updateActionBarVisibility(final boolean z, boolean z2) {
        AttachAlertLayout attachAlertLayout;
        if (!(z && this.actionBar.getTag() == null) && (z || this.actionBar.getTag() == null)) {
            return;
        }
        this.actionBar.setTag(z ? 1 : null);
        AnimatorSet animatorSet = this.actionBarAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.actionBarAnimation = null;
        }
        ActionBarMenuItem actionBarMenuItem = this.searchItem;
        boolean z3 = actionBarMenuItem != null && this.avatarSearch;
        boolean z4 = (this.isPhotoPicker || this.storyMediaPicker || (this.avatarPicker == 0 && this.menuShowed) || this.currentAttachLayout != this.photoLayout || (!this.photosEnabled && !this.videosEnabled)) ? false : true;
        if (this.currentAttachLayout == this.restrictedLayout) {
            z3 = false;
            z4 = false;
        }
        if (z) {
            if (z3) {
                actionBarMenuItem.setVisibility(0);
            }
            if (z4) {
                this.selectedMenuItem.setVisibility(0);
            }
        } else if (this.typeButtonsAvailable && this.frameLayout2.getTag() == null) {
            this.buttonsRecyclerView.setVisibility(0);
        }
        if (getWindow() != null && this.baseFragment != null) {
            AndroidUtilities.setLightStatusBar(getWindow(), z ? isLightStatusBar() : this.baseFragment.isLightStatusBar());
        }
        if (z2) {
            AnimatorSet animatorSet2 = new AnimatorSet();
            this.actionBarAnimation = animatorSet2;
            animatorSet2.setDuration(Math.abs((z ? 1.0f : 0.0f) - this.actionBar.getAlpha()) * 180.0f);
            ArrayList arrayList = new ArrayList();
            ActionBar actionBar = this.actionBar;
            Property property = View.ALPHA;
            arrayList.add(ObjectAnimator.ofFloat(actionBar, property, z ? 1.0f : 0.0f));
            arrayList.add(ObjectAnimator.ofFloat(this.actionBarShadow, property, z ? 1.0f : 0.0f));
            if (z3) {
                arrayList.add(ObjectAnimator.ofFloat(this.searchItem, property, z ? 1.0f : 0.0f));
            }
            if (z4) {
                arrayList.add(ObjectAnimator.ofFloat(this.selectedMenuItem, property, z ? 1.0f : 0.0f));
            }
            this.actionBarAnimation.playTogether(arrayList);
            this.actionBarAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlert.23
                {
                    ChatAttachAlert.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    ChatAttachAlert.this.actionBarAnimation = null;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (ChatAttachAlert.this.actionBarAnimation != null) {
                        if (z) {
                            ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                            if (chatAttachAlert.typeButtonsAvailable) {
                                if (chatAttachAlert.currentAttachLayout == null || ChatAttachAlert.this.currentAttachLayout.shouldHideBottomButtons()) {
                                    ChatAttachAlert.this.buttonsRecyclerView.setVisibility(4);
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        ActionBarMenuItem actionBarMenuItem2 = ChatAttachAlert.this.searchItem;
                        if (actionBarMenuItem2 != null) {
                            actionBarMenuItem2.setVisibility(4);
                        }
                        ChatAttachAlert chatAttachAlert2 = ChatAttachAlert.this;
                        if (chatAttachAlert2.avatarPicker == 0 && chatAttachAlert2.menuShowed) {
                            return;
                        }
                        ChatAttachAlert.this.selectedMenuItem.setVisibility(4);
                    }
                }
            });
            this.actionBarAnimation.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
            this.actionBarAnimation.setDuration(380L);
            this.actionBarAnimation.start();
            return;
        }
        if (z && this.typeButtonsAvailable && ((attachAlertLayout = this.currentAttachLayout) == null || attachAlertLayout.shouldHideBottomButtons())) {
            this.buttonsRecyclerView.setVisibility(4);
        }
        this.actionBar.setAlpha(z ? 1.0f : 0.0f);
        this.actionBarShadow.setAlpha(z ? 1.0f : 0.0f);
        if (z3) {
            this.searchItem.setAlpha(z ? 1.0f : 0.0f);
        }
        if (z4) {
            this.selectedMenuItem.setAlpha(z ? 1.0f : 0.0f);
        }
        if (z) {
            return;
        }
        ActionBarMenuItem actionBarMenuItem2 = this.searchItem;
        if (actionBarMenuItem2 != null) {
            actionBarMenuItem2.setVisibility(4);
        }
        if (this.avatarPicker == 0 && this.menuShowed) {
            return;
        }
        this.selectedMenuItem.setVisibility(4);
    }

    /* JADX WARN: Removed duplicated region for block: B:121:0x00ee  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateSelectedPosition(int i) {
        int dp;
        float f;
        float f2;
        int i2;
        ActionBarMenuItem actionBarMenuItem;
        float currentActionBarHeight;
        float f3;
        ActionBarMenuItem actionBarMenuItem2;
        ChatAttachAlertPollLayout chatAttachAlertPollLayout;
        int i3;
        AttachAlertLayout attachAlertLayout = i == 0 ? this.currentAttachLayout : this.nextAttachLayout;
        if (attachAlertLayout == null || attachAlertLayout.getVisibility() != 0) {
            return;
        }
        int scrollOffsetY = getScrollOffsetY(i);
        int i4 = scrollOffsetY - this.backgroundPaddingTop;
        if (attachAlertLayout == this.pollLayout) {
            dp = i4 - AndroidUtilities.dp(13.0f);
            f = 11.0f;
        } else {
            dp = i4 - AndroidUtilities.dp(39.0f);
            f = 43.0f;
        }
        float dp2 = AndroidUtilities.dp(f);
        if (this.backgroundPaddingTop + dp < ActionBar.getCurrentActionBarHeight()) {
            f2 = Math.min(1.0f, ((ActionBar.getCurrentActionBarHeight() - dp) - this.backgroundPaddingTop) / dp2);
            this.cornerRadius = 1.0f - f2;
        } else {
            this.cornerRadius = 1.0f;
            f2 = 0.0f;
        }
        if (AndroidUtilities.isTablet()) {
            i2 = 16;
        } else {
            android.graphics.Point point = AndroidUtilities.displaySize;
            i2 = point.x > point.y ? 6 : 12;
        }
        float dp3 = this.actionBar.getAlpha() != 0.0f ? 0.0f : AndroidUtilities.dp((1.0f - this.headerView.getAlpha()) * 26.0f);
        if (this.menuShowed && this.avatarPicker == 0 && !this.storyMediaPicker) {
            actionBarMenuItem = this.selectedMenuItem;
            currentActionBarHeight = (scrollOffsetY - AndroidUtilities.dp((i2 * f2) + 37.0f)) + dp3;
        } else {
            actionBarMenuItem = this.selectedMenuItem;
            currentActionBarHeight = (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(4.0f)) - AndroidUtilities.dp(i2 + 37);
        }
        actionBarMenuItem.setTranslationY(currentActionBarHeight + this.currentPanTranslationY);
        if (this.isPhotoPicker && this.openTransitionFinished) {
            AttachAlertLayout attachAlertLayout2 = this.nextAttachLayout;
            if (attachAlertLayout2 != null && this.currentAttachLayout != null) {
                f3 = Math.min(attachAlertLayout2.getTranslationY(), this.currentAttachLayout.getTranslationY());
            } else if (attachAlertLayout2 != null) {
                f3 = attachAlertLayout2.getTranslationY();
            }
            actionBarMenuItem2 = this.searchItem;
            if (actionBarMenuItem2 != null) {
                actionBarMenuItem2.setTranslationY(((ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(4.0f)) - AndroidUtilities.dp(i2 + 37)) + this.currentPanTranslationY);
            }
            FrameLayout frameLayout = this.headerView;
            float dp4 = (scrollOffsetY - AndroidUtilities.dp((i2 * f2) + 25.0f)) + dp3 + this.currentPanTranslationY + f3;
            this.baseSelectedTextViewTranslationY = dp4;
            frameLayout.setTranslationY(dp4);
            chatAttachAlertPollLayout = this.pollLayout;
            if (chatAttachAlertPollLayout == null && attachAlertLayout == chatAttachAlertPollLayout) {
                if (AndroidUtilities.isTablet()) {
                    i3 = 63;
                } else {
                    android.graphics.Point point2 = AndroidUtilities.displaySize;
                    i3 = point2.x > point2.y ? 53 : 59;
                }
                this.doneItem.setTranslationY(Math.max(0.0f, (this.pollLayout.getTranslationY() + scrollOffsetY) - AndroidUtilities.dp((i3 * f2) + 7.0f)) + this.currentPanTranslationY);
                return;
            }
        }
        f3 = 0.0f;
        actionBarMenuItem2 = this.searchItem;
        if (actionBarMenuItem2 != null) {
        }
        FrameLayout frameLayout2 = this.headerView;
        float dp42 = (scrollOffsetY - AndroidUtilities.dp((i2 * f2) + 25.0f)) + dp3 + this.currentPanTranslationY + f3;
        this.baseSelectedTextViewTranslationY = dp42;
        frameLayout2.setTranslationY(dp42);
        chatAttachAlertPollLayout = this.pollLayout;
        if (chatAttachAlertPollLayout == null) {
        }
    }

    public void applyCaption() {
        if (this.commentTextView.length() <= 0) {
            return;
        }
        this.currentAttachLayout.applyCaption(this.commentTextView.getText());
    }

    public void avatarFor(ImageUpdater.AvatarFor avatarFor) {
        this.setAvatarFor = avatarFor;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet.BottomSheetDelegateInterface
    public boolean canDismiss() {
        return true;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public boolean canDismissWithSwipe() {
        return false;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean canDismissWithTouchOutside() {
        return this.currentAttachLayout.canDismissWithTouchOutside();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected void cancelSheetAnimation() {
        AnimatorSet animatorSet = this.currentSheetAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            SpringAnimation springAnimation = this.appearSpringAnimation;
            if (springAnimation != null) {
                springAnimation.cancel();
            }
            AnimatorSet animatorSet2 = this.buttonsAnimation;
            if (animatorSet2 != null) {
                animatorSet2.cancel();
            }
            this.currentSheetAnimation = null;
            this.currentSheetAnimationType = 0;
        }
    }

    public boolean checkCanRemoveRestrictionsByBoosts() {
        BaseFragment baseFragment = this.baseFragment;
        return (baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).checkCanRemoveRestrictionsByBoosts();
    }

    public boolean checkCaption(CharSequence charSequence) {
        BaseFragment baseFragment = this.baseFragment;
        if (baseFragment instanceof ChatActivity) {
            return ChatActivityEnterView.checkPremiumAnimatedEmoji(this.currentAccount, ((ChatActivity) baseFragment).getDialogId(), this.baseFragment, this.sizeNotifierFrameLayout, charSequence);
        }
        return false;
    }

    public void checkColors() {
        RecyclerListView recyclerListView = this.buttonsRecyclerView;
        if (recyclerListView == null) {
            return;
        }
        int childCount = recyclerListView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            applyAttachButtonColors(this.buttonsRecyclerView.getChildAt(i));
        }
        this.selectedTextView.setTextColor(getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_actionBarItems : Theme.key_dialogTextBlack));
        this.mediaPreviewTextView.setTextColor(getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_actionBarItems : Theme.key_dialogTextBlack));
        this.doneItem.getTextView().setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteBlueHeader));
        this.selectedMenuItem.setIconColor(getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_actionBarItems : Theme.key_dialogTextBlack));
        Theme.setDrawableColor(this.selectedMenuItem.getBackground(), getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_actionBarItemsSelector : Theme.key_dialogButtonSelector));
        ActionBarMenuItem actionBarMenuItem = this.selectedMenuItem;
        int i2 = Theme.key_actionBarDefaultSubmenuItem;
        actionBarMenuItem.setPopupItemsColor(getThemedColor(i2), false);
        this.selectedMenuItem.setPopupItemsColor(getThemedColor(i2), true);
        this.selectedMenuItem.redrawPopup(getThemedColor(Theme.key_actionBarDefaultSubmenuBackground));
        ActionBarMenuItem actionBarMenuItem2 = this.searchItem;
        if (actionBarMenuItem2 != null) {
            actionBarMenuItem2.setIconColor(getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_actionBarItems : Theme.key_dialogTextBlack));
            Theme.setDrawableColor(this.searchItem.getBackground(), getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_actionBarItemsSelector : Theme.key_dialogButtonSelector));
        }
        this.commentTextView.updateColors();
        this.actionBarShadow.setBackgroundColor(getThemedColor(Theme.key_dialogShadowLine));
        this.buttonsRecyclerView.setGlowColor(getThemedColor(Theme.key_dialogScrollGlow));
        this.buttonsRecyclerView.setBackgroundColor(getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_listViewBackground : Theme.key_dialogBackground));
        this.frameLayout2.setBackgroundColor(getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_listViewBackground : Theme.key_dialogBackground));
        this.actionBar.setBackgroundColor(getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_actionBar : Theme.key_dialogBackground));
        this.actionBar.setItemsColor(getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_actionBarItems : Theme.key_dialogTextBlack), false);
        this.actionBar.setItemsBackgroundColor(getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_actionBarItemsSelector : Theme.key_dialogButtonSelector), false);
        this.actionBar.setTitleColor(getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_actionBarItems : Theme.key_dialogTextBlack));
        Theme.setDrawableColor(this.shadowDrawable, getThemedColor(this.forceDarkTheme ? Theme.key_voipgroup_listViewBackground : Theme.key_dialogBackground));
        this.containerView.invalidate();
        int i3 = 0;
        while (true) {
            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
            if (i3 >= attachAlertLayoutArr.length) {
                break;
            }
            AttachAlertLayout attachAlertLayout = attachAlertLayoutArr[i3];
            if (attachAlertLayout != null) {
                attachAlertLayout.checkColors();
            }
            i3++;
        }
        if (Build.VERSION.SDK_INT < 30) {
            fixNavigationBar(getThemedColor(Theme.key_dialogBackground));
            return;
        }
        this.navBarColorKey = -1;
        this.navBarColor = getThemedColor(Theme.key_dialogBackgroundGray);
        AndroidUtilities.setNavigationBarColor(getWindow(), getThemedColor(Theme.key_dialogBackground), false);
        AndroidUtilities.setLightNavigationBar(getWindow(), ((double) AndroidUtilities.computePerceivedBrightness(this.navBarColor)) > 0.721d);
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i != NotificationCenter.reloadInlineHints && i != NotificationCenter.attachMenuBotsDidLoad && i != NotificationCenter.quickRepliesUpdated) {
            if (i == NotificationCenter.currentUserPremiumStatusChanged) {
                this.currentLimit = MessagesController.getInstance(UserConfig.selectedAccount).getCaptionMaxLengthLimit();
                return;
            }
            return;
        }
        ButtonsAdapter buttonsAdapter = this.buttonsAdapter;
        if (buttonsAdapter != null) {
            buttonsAdapter.notifyDataSetChanged();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void dismiss() {
        if (this.currentAttachLayout.onDismiss() || isDismissed()) {
            return;
        }
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji != null) {
            AndroidUtilities.hideKeyboard(editTextEmoji.getEditText());
        }
        this.botAttachLayouts.clear();
        BaseFragment baseFragment = this.baseFragment;
        if (baseFragment == null) {
            baseFragment = LaunchActivity.getLastFragment();
        }
        if (!this.allowPassConfirmationAlert && baseFragment != null && this.currentAttachLayout.getSelectedItemsCount() > 0 && !this.isPhotoPicker) {
            if (this.confirmationAlertShown) {
                return;
            }
            this.confirmationAlertShown = true;
            AlertDialog create = new AlertDialog.Builder(baseFragment.getParentActivity(), this.resourcesProvider).setTitle(LocaleController.getString("DiscardSelectionAlertTitle", R.string.DiscardSelectionAlertTitle)).setMessage(LocaleController.getString("DiscardSelectionAlertMessage", R.string.DiscardSelectionAlertMessage)).setPositiveButton(LocaleController.getString("PassportDiscard", R.string.PassportDiscard), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ChatAttachAlert.this.lambda$dismiss$44(dialogInterface, i);
                }
            }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    ChatAttachAlert.this.lambda$dismiss$45(dialogInterface);
                }
            }).setOnPreDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    ChatAttachAlert.this.lambda$dismiss$46(dialogInterface);
                }
            }).create();
            create.show();
            TextView textView = (TextView) create.getButton(-1);
            if (textView != null) {
                textView.setTextColor(getThemedColor(Theme.key_text_RedBold));
                return;
            }
            return;
        }
        int i = 0;
        while (true) {
            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
            if (i >= attachAlertLayoutArr.length) {
                break;
            }
            AttachAlertLayout attachAlertLayout = attachAlertLayoutArr[i];
            if (attachAlertLayout != null && this.currentAttachLayout != attachAlertLayout) {
                attachAlertLayout.onDismiss();
            }
            i++;
        }
        AndroidUtilities.setNavigationBarColor(getWindow(), ColorUtils.setAlphaComponent(getThemedColor(Theme.key_windowBackgroundGray), 0), true, new AndroidUtilities.IntColorCallback() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda3
            @Override // org.telegram.messenger.AndroidUtilities.IntColorCallback
            public final void run(int i2) {
                ChatAttachAlert.this.lambda$dismiss$47(i2);
            }
        });
        if (baseFragment != null) {
            AndroidUtilities.setLightStatusBar(getWindow(), baseFragment.isLightStatusBar());
        }
        this.captionLimitBulletinShown = false;
        super.dismiss();
        this.allowPassConfirmationAlert = false;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
    public void dismiss(boolean z) {
        if (z) {
            this.allowPassConfirmationAlert = z;
        }
        dismiss();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void dismissInternal() {
        ChatAttachViewDelegate chatAttachViewDelegate = this.delegate;
        if (chatAttachViewDelegate != null) {
            chatAttachViewDelegate.doOnIdle(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    ChatAttachAlert.this.removeFromRoot();
                }
            });
        } else {
            removeFromRoot();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void dismissWithButtonClick(int i) {
        super.dismissWithButtonClick(i);
        this.currentAttachLayout.onDismissWithButtonClick(i);
    }

    public void enableDefaultMode() {
        this.typeButtonsAvailable = true;
        this.buttonsRecyclerView.setVisibility(0);
        this.shadow.setVisibility(0);
        this.avatarPicker = 0;
        this.isPhotoPicker = false;
        this.isStickerMode = false;
        this.customStickerHandler = null;
        if (this.optionsItem != null) {
            this.selectedTextView.setTranslationY(0.0f);
            this.optionsItem.setVisibility(8);
        }
    }

    public void enableStickerMode(Utilities.Callback2 callback2) {
        this.selectedTextView.setText(LocaleController.getString(R.string.ChoosePhotoForSticker));
        this.typeButtonsAvailable = false;
        this.buttonsRecyclerView.setVisibility(8);
        this.shadow.setVisibility(8);
        this.avatarPicker = 1;
        this.isPhotoPicker = true;
        this.isStickerMode = true;
        this.customStickerHandler = callback2;
        if (this.optionsItem != null) {
            this.selectedTextView.setTranslationY(-AndroidUtilities.dp(8.0f));
            this.optionsItem.setVisibility(0);
        }
    }

    public ImageUpdater.AvatarFor getAvatarFor() {
        return this.setAvatarFor;
    }

    public BaseFragment getBaseFragment() {
        return this.baseFragment;
    }

    public TLRPC.Chat getChat() {
        BaseFragment baseFragment = this.baseFragment;
        return baseFragment instanceof ChatActivity ? ((ChatActivity) baseFragment).getCurrentChat() : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
    }

    public float getClipLayoutBottom() {
        return this.frameLayout2.getMeasuredHeight() - ((this.frameLayout2.getMeasuredHeight() - AndroidUtilities.dp(84.0f)) * (1.0f - this.frameLayout2.getAlpha()));
    }

    public EditTextEmoji getCommentTextView() {
        return this.commentTextView;
    }

    public int getCommentTextViewTop() {
        return this.commentTextViewLocation[1];
    }

    public AttachAlertLayout getCurrentAttachLayout() {
        return this.currentAttachLayout;
    }

    public ChatAttachAlertDocumentLayout getDocumentLayout() {
        return this.documentLayout;
    }

    public MessageObject getEditingMessageObject() {
        return this.editingMessageObject;
    }

    public ChatAttachAlertPhotoLayout getPhotoLayout() {
        return this.photoLayout;
    }

    public ChatAttachAlertPhotoLayoutPreview getPhotoPreviewLayout() {
        return this.photoPreviewLayout;
    }

    public TextView getSelectedTextView() {
        return this.selectedTextView;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public ArrayList getThemeDescriptions() {
        ArrayList<ThemeDescription> themeDescriptions;
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
            if (i >= attachAlertLayoutArr.length) {
                arrayList.add(new ThemeDescription(this.container, 0, null, null, null, null, Theme.key_dialogBackgroundGray));
                return arrayList;
            }
            AttachAlertLayout attachAlertLayout = attachAlertLayoutArr[i];
            if (attachAlertLayout != null && (themeDescriptions = attachAlertLayout.getThemeDescriptions()) != null) {
                arrayList.addAll(themeDescriptions);
            }
            i++;
        }
    }

    public boolean hasCaption() {
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout = this.photoLayout;
        if (chatAttachAlertPhotoLayout == null) {
            return false;
        }
        HashMap<Object, Object> selectedPhotos = chatAttachAlertPhotoLayout.getSelectedPhotos();
        ArrayList<Object> selectedPhotosOrder = this.photoLayout.getSelectedPhotosOrder();
        if (selectedPhotos.isEmpty()) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < Math.ceil(selectedPhotos.size() / 10.0f); i++) {
            int i2 = i * 10;
            int min = Math.min(10, selectedPhotos.size() - i2);
            Utilities.random.nextLong();
            for (int i3 = 0; i3 < min; i3++) {
                int i4 = i2 + i3;
                if (i4 < selectedPhotosOrder.size()) {
                    CharSequence charSequence = ((MediaController.PhotoEntry) selectedPhotos.get(selectedPhotosOrder.get(i4))).caption;
                    String charSequence2 = charSequence == null ? "" : charSequence.toString();
                    if (this.commentTextView != null && TextUtils.isEmpty(charSequence2) && i3 == 0) {
                        charSequence2 = this.commentTextView.getText().toString();
                    }
                    if (TextUtils.isEmpty(charSequence2)) {
                        continue;
                    } else if (z) {
                        return false;
                    } else {
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:128:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0112  */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0187  */
    /* JADX WARN: Removed duplicated region for block: B:182:0x01ad  */
    /* JADX WARN: Removed duplicated region for block: B:183:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x01ba  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x01bc  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x022b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void init() {
        TLRPC.Chat chat;
        TLRPC.User user;
        AttachAlertLayout attachAlertLayout;
        AttachAlertLayout attachAlertLayout2;
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout;
        this.botButtonWasVisible = false;
        this.botButtonProgressWasVisible = false;
        this.botMainButtonOffsetY = 0.0f;
        this.botMainButtonTextView.setVisibility(8);
        this.botProgressView.setAlpha(0.0f);
        this.botProgressView.setScaleX(0.1f);
        this.botProgressView.setScaleY(0.1f);
        this.botProgressView.setVisibility(8);
        this.buttonsRecyclerView.setAlpha(1.0f);
        this.buttonsRecyclerView.setTranslationY(0.0f);
        for (int i = 0; i < this.botAttachLayouts.size(); i++) {
            ((ChatAttachAlertBotWebViewLayout) this.botAttachLayouts.valueAt(i)).setMeasureOffsetY(0);
        }
        this.shadow.setAlpha(1.0f);
        this.shadow.setTranslationY(0.0f);
        if (this.avatarPicker != 2) {
            BaseFragment baseFragment = this.baseFragment;
            if (baseFragment instanceof ChatActivity) {
                chat = ((ChatActivity) baseFragment).getCurrentChat();
                user = ((ChatActivity) this.baseFragment).getCurrentUser();
            } else {
                long j = this.dialogId;
                if (j >= 0) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialogId));
                    chat = null;
                } else if (j < 0) {
                    chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-this.dialogId));
                    user = null;
                }
            }
            if (((this.baseFragment instanceof ChatActivity) && this.avatarPicker != 2) || chat != null || user != null) {
                if (chat != null) {
                    this.photosEnabled = ChatObject.canSendPhoto(chat);
                    this.videosEnabled = ChatObject.canSendVideo(chat);
                    this.musicEnabled = ChatObject.canSendMusic(chat);
                    this.pollsEnabled = ChatObject.canSendPolls(chat);
                    this.plainTextEnabled = ChatObject.canSendPlain(chat);
                    this.documentsEnabled = ChatObject.canSendDocument(chat);
                } else {
                    this.pollsEnabled = user != null && user.bot;
                }
            }
            if ((this.baseFragment instanceof ChatActivity) || this.avatarPicker == 2) {
                this.commentTextView.setVisibility(this.allowEnterCaption ? 0 : 4);
            }
            this.photoLayout.onInit(this.videosEnabled, this.photosEnabled, this.documentsEnabled);
            this.commentTextView.hidePopup(true);
            this.enterCommentEventSent = false;
            setFocusable(false);
            if (!this.isStoryLocationPicker || this.isBizLocationPicker) {
                if (this.locationLayout == null) {
                    AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
                    ChatAttachAlertLocationLayout chatAttachAlertLocationLayout = new ChatAttachAlertLocationLayout(this, getContext(), this.resourcesProvider);
                    this.locationLayout = chatAttachAlertLocationLayout;
                    attachAlertLayoutArr[5] = chatAttachAlertLocationLayout;
                    chatAttachAlertLocationLayout.setDelegate(new ChatAttachAlertLocationLayout.LocationActivityDelegate() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda29
                        @Override // org.telegram.ui.Components.ChatAttachAlertLocationLayout.LocationActivityDelegate
                        public final void didSelectLocation(TLRPC.MessageMedia messageMedia, int i2, boolean z, int i3) {
                            ChatAttachAlert.this.lambda$init$43(messageMedia, i2, z, i3);
                        }
                    });
                }
                this.selectedId = 5L;
                attachAlertLayout = this.locationLayout;
            } else {
                long j2 = 3;
                if (this.isStoryAudioPicker) {
                    openAudioLayout(false);
                    attachAlertLayout = this.audioLayout;
                } else if (this.isSoundPicker) {
                    openDocumentsLayout(false);
                    attachAlertLayout = this.documentLayout;
                    this.selectedId = 4L;
                } else {
                    MessageObject messageObject = this.editingMessageObject;
                    if (messageObject == null || (!messageObject.isMusic() && (!this.editingMessageObject.isDocument() || this.editingMessageObject.isGif()))) {
                        attachAlertLayout = this.photoLayout;
                        this.typeButtonsAvailable = this.avatarPicker == 0 && !this.storyMediaPicker;
                        j2 = 1;
                    } else {
                        if (this.editingMessageObject.isMusic()) {
                            openAudioLayout(false);
                            attachAlertLayout = this.audioLayout;
                            this.selectedId = 3L;
                        } else {
                            openDocumentsLayout(false);
                            attachAlertLayout = this.documentLayout;
                            this.selectedId = 4L;
                        }
                        this.typeButtonsAvailable = !this.editingMessageObject.hasValidGroupId();
                    }
                }
                this.selectedId = j2;
            }
            this.buttonsRecyclerView.setVisibility(this.typeButtonsAvailable ? 0 : 8);
            this.shadow.setVisibility(this.typeButtonsAvailable ? 0 : 4);
            if (this.currentAttachLayout != attachAlertLayout) {
                if (this.actionBar.isSearchFieldVisible()) {
                    this.actionBar.closeSearchField();
                }
                this.containerView.removeView(this.currentAttachLayout);
                this.currentAttachLayout.onHide();
                this.currentAttachLayout.setVisibility(8);
                this.currentAttachLayout.onHidden();
                this.currentAttachLayout = attachAlertLayout;
                setAllowNestedScroll(true);
                if (this.currentAttachLayout.getParent() == null) {
                    this.containerView.addView(this.currentAttachLayout, 0, LayoutHelper.createFrame(-1, -1.0f));
                }
                attachAlertLayout.setAlpha(1.0f);
                attachAlertLayout.setVisibility(0);
                attachAlertLayout.onShow(null);
                attachAlertLayout.onShown();
                this.actionBar.setVisibility(attachAlertLayout.needsActionBar() != 0 ? 0 : 4);
                this.actionBarShadow.setVisibility(this.actionBar.getVisibility());
            }
            attachAlertLayout2 = this.currentAttachLayout;
            chatAttachAlertPhotoLayout = this.photoLayout;
            if (attachAlertLayout2 != chatAttachAlertPhotoLayout) {
                chatAttachAlertPhotoLayout.setCheckCameraWhenShown(true);
            }
            updateCountButton(0);
            this.buttonsAdapter.notifyDataSetChanged();
            this.commentTextView.setText("");
            this.buttonsLayoutManager.scrollToPositionWithOffset(0, MediaController.VIDEO_BITRATE_480);
        }
        chat = null;
        user = null;
        if (this.baseFragment instanceof ChatActivity) {
            if (chat != null) {
            }
            if (this.baseFragment instanceof ChatActivity) {
            }
            this.commentTextView.setVisibility(this.allowEnterCaption ? 0 : 4);
            this.photoLayout.onInit(this.videosEnabled, this.photosEnabled, this.documentsEnabled);
            this.commentTextView.hidePopup(true);
            this.enterCommentEventSent = false;
            setFocusable(false);
            if (this.isStoryLocationPicker) {
            }
            if (this.locationLayout == null) {
            }
            this.selectedId = 5L;
            attachAlertLayout = this.locationLayout;
            this.buttonsRecyclerView.setVisibility(this.typeButtonsAvailable ? 0 : 8);
            this.shadow.setVisibility(this.typeButtonsAvailable ? 0 : 4);
            if (this.currentAttachLayout != attachAlertLayout) {
            }
            attachAlertLayout2 = this.currentAttachLayout;
            chatAttachAlertPhotoLayout = this.photoLayout;
            if (attachAlertLayout2 != chatAttachAlertPhotoLayout) {
            }
            updateCountButton(0);
            this.buttonsAdapter.notifyDataSetChanged();
            this.commentTextView.setText("");
            this.buttonsLayoutManager.scrollToPositionWithOffset(0, MediaController.VIDEO_BITRATE_480);
        }
        if (chat != null) {
        }
        if (this.baseFragment instanceof ChatActivity) {
        }
        this.commentTextView.setVisibility(this.allowEnterCaption ? 0 : 4);
        this.photoLayout.onInit(this.videosEnabled, this.photosEnabled, this.documentsEnabled);
        this.commentTextView.hidePopup(true);
        this.enterCommentEventSent = false;
        setFocusable(false);
        if (this.isStoryLocationPicker) {
        }
        if (this.locationLayout == null) {
        }
        this.selectedId = 5L;
        attachAlertLayout = this.locationLayout;
        this.buttonsRecyclerView.setVisibility(this.typeButtonsAvailable ? 0 : 8);
        this.shadow.setVisibility(this.typeButtonsAvailable ? 0 : 4);
        if (this.currentAttachLayout != attachAlertLayout) {
        }
        attachAlertLayout2 = this.currentAttachLayout;
        chatAttachAlertPhotoLayout = this.photoLayout;
        if (attachAlertLayout2 != chatAttachAlertPhotoLayout) {
        }
        updateCountButton(0);
        this.buttonsAdapter.notifyDataSetChanged();
        this.commentTextView.setText("");
        this.buttonsLayoutManager.scrollToPositionWithOffset(0, MediaController.VIDEO_BITRATE_480);
    }

    public void makeFocusable(final EditTextBoldCursor editTextBoldCursor, final boolean z) {
        ChatAttachViewDelegate chatAttachViewDelegate = this.delegate;
        if (chatAttachViewDelegate == null || this.enterCommentEventSent) {
            return;
        }
        boolean needEnterComment = chatAttachViewDelegate.needEnterComment();
        this.enterCommentEventSent = true;
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda31
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlert.this.lambda$makeFocusable$42(editTextBoldCursor, z);
            }
        }, needEnterComment ? 200L : 0L);
    }

    public void onActivityResultFragment(int i, Intent intent, String str) {
        this.photoLayout.onActivityResultFragment(i, intent, str);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onBackPressed() {
        if (this.passcodeView.getVisibility() == 0) {
            if (getOwnerActivity() != null) {
                getOwnerActivity().finish();
            }
        } else if (this.actionBar.isSearchFieldVisible()) {
            this.actionBar.closeSearchField();
        } else if (this.currentAttachLayout.onBackPressed()) {
        } else {
            EditTextEmoji editTextEmoji = this.commentTextView;
            if (editTextEmoji == null || !editTextEmoji.isPopupShowing()) {
                super.onBackPressed();
            } else {
                this.commentTextView.hidePopup(true);
            }
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean onContainerTouchEvent(MotionEvent motionEvent) {
        return this.currentAttachLayout.onContainerViewTouchEvent(motionEvent);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.baseFragment != null) {
            AndroidUtilities.setLightStatusBar(getWindow(), this.baseFragment.isLightStatusBar());
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean onCustomLayout(View view, int i, int i2, int i3, int i4) {
        return this.photoLayout.onCustomLayout(view, i, i2, i3, i4);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean onCustomMeasure(View view, int i, int i2) {
        return this.photoLayout.onCustomMeasure(view, i, i2);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public boolean onCustomOpenAnimation() {
        this.photoLayout.setTranslationX(0.0f);
        this.mediaPreviewView.setAlpha(0.0f);
        this.selectedView.setAlpha(1.0f);
        this.containerView.setTranslationY(this.containerView.getMeasuredHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        this.buttonsAnimation = animatorSet;
        animatorSet.playTogether(ObjectAnimator.ofFloat(this, this.ATTACH_ALERT_PROGRESS, 0.0f, 400.0f));
        this.buttonsAnimation.setDuration(400L);
        this.buttonsAnimation.setStartDelay(20L);
        this.ATTACH_ALERT_PROGRESS.set(this, Float.valueOf(0.0f));
        this.buttonsAnimation.start();
        ValueAnimator valueAnimator = this.navigationBarAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.navigationBarAlpha, 1.0f);
        this.navigationBarAnimation = ofFloat;
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ChatAttachAlert.this.lambda$onCustomOpenAnimation$37(valueAnimator2);
            }
        });
        SpringAnimation springAnimation = this.appearSpringAnimation;
        if (springAnimation != null) {
            springAnimation.cancel();
        }
        SpringAnimation springAnimation2 = new SpringAnimation(this.containerView, DynamicAnimation.TRANSLATION_Y, 0.0f);
        this.appearSpringAnimation = springAnimation2;
        springAnimation2.getSpring().setDampingRatio(0.75f);
        this.appearSpringAnimation.getSpring().setStiffness(350.0f);
        this.appearSpringAnimation.start();
        if (Build.VERSION.SDK_INT >= 20 && this.useHardwareLayer) {
            this.container.setLayerType(2, null);
        }
        this.currentSheetAnimationType = 1;
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.currentSheetAnimation = animatorSet2;
        animatorSet2.playTogether(ObjectAnimator.ofInt(this.backDrawable, AnimationProperties.COLOR_DRAWABLE_ALPHA, this.dimBehind ? this.dimBehindAlpha : 0));
        this.currentSheetAnimation.setDuration(400L);
        this.currentSheetAnimation.setStartDelay(20L);
        this.currentSheetAnimation.setInterpolator(this.openInterpolator);
        final AnimationNotificationsLocker animationNotificationsLocker = new AnimationNotificationsLocker();
        final BottomSheet.BottomSheetDelegateInterface bottomSheetDelegateInterface = super.delegate;
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                ChatAttachAlert.this.lambda$onCustomOpenAnimation$38(animationNotificationsLocker, bottomSheetDelegateInterface);
            }
        };
        this.appearSpringAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda9
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
            public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                ChatAttachAlert.this.lambda$onCustomOpenAnimation$39(runnable, dynamicAnimation, z, f, f2);
            }
        });
        this.currentSheetAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlert.22
            {
                ChatAttachAlert.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (((BottomSheet) ChatAttachAlert.this).currentSheetAnimation == null || !((BottomSheet) ChatAttachAlert.this).currentSheetAnimation.equals(animator)) {
                    return;
                }
                ((BottomSheet) ChatAttachAlert.this).currentSheetAnimation = null;
                ((BottomSheet) ChatAttachAlert.this).currentSheetAnimationType = 0;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (((BottomSheet) ChatAttachAlert.this).currentSheetAnimation == null || !((BottomSheet) ChatAttachAlert.this).currentSheetAnimation.equals(animator) || ChatAttachAlert.this.appearSpringAnimation == null || ChatAttachAlert.this.appearSpringAnimation.isRunning()) {
                    return;
                }
                runnable.run();
            }
        });
        animationNotificationsLocker.lock();
        NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 512);
        this.currentSheetAnimation.start();
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        setNavBarAlpha(0.0f);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ChatAttachAlert.this.lambda$onCustomOpenAnimation$40(valueAnimator2);
            }
        });
        ofFloat2.setStartDelay(25L);
        ofFloat2.setDuration(200L);
        ofFloat2.setInterpolator(CubicBezierInterpolator.DEFAULT);
        ofFloat2.start();
        return true;
    }

    public void onDestroy() {
        int i = 0;
        while (true) {
            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
            if (i >= attachAlertLayoutArr.length) {
                break;
            }
            AttachAlertLayout attachAlertLayout = attachAlertLayoutArr[i];
            if (attachAlertLayout != null) {
                attachAlertLayout.onDestroy();
            }
            i++;
        }
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.reloadInlineHints);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.attachMenuBotsDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.currentUserPremiumStatusChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.quickRepliesUpdated);
        this.destroyed = true;
        EditTextEmoji editTextEmoji = this.commentTextView;
        if (editTextEmoji != null) {
            editTextEmoji.onDestroy();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected void onDismissWithTouchOutside() {
        if (this.currentAttachLayout.onDismissWithTouchOutside()) {
            dismiss();
        }
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (this.currentAttachLayout.onSheetKeyDown(i, keyEvent)) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    public void onLongClickBotButton(final TLRPC.TL_attachMenuBot tL_attachMenuBot, final TLRPC.User user) {
        String userName = tL_attachMenuBot != null ? tL_attachMenuBot.short_name : UserObject.getUserName(user);
        Iterator<TLRPC.TL_attachMenuBot> it = MediaDataController.getInstance(this.currentAccount).getAttachMenuBots().bots.iterator();
        while (it.hasNext() && it.next().bot_id != user.id) {
        }
        String formatString = LocaleController.formatString("BotRemoveFromMenu", R.string.BotRemoveFromMenu, userName);
        AlertDialog.Builder title = new AlertDialog.Builder(getContext()).setTitle(LocaleController.getString(R.string.BotRemoveFromMenuTitle));
        if (tL_attachMenuBot == null) {
            formatString = LocaleController.formatString("BotRemoveInlineFromMenu", R.string.BotRemoveInlineFromMenu, userName);
        }
        title.setMessage(AndroidUtilities.replaceTags(formatString)).setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda30
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ChatAttachAlert.this.lambda$onLongClickBotButton$30(tL_attachMenuBot, user, dialogInterface, i);
            }
        }).setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null).show();
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void onOpenAnimationEnd() {
        MediaController.AlbumEntry albumEntry = this.baseFragment instanceof ChatActivity ? MediaController.allMediaAlbumEntry : MediaController.allPhotosAlbumEntry;
        if (Build.VERSION.SDK_INT <= 19 && albumEntry == null) {
            MediaController.loadGalleryPhotosAlbums(0);
        }
        this.currentAttachLayout.onOpenAnimationEnd();
        AndroidUtilities.makeAccessibilityAnnouncement(LocaleController.getString("AccDescrAttachButton", R.string.AccDescrAttachButton));
        this.openTransitionFinished = true;
        if (this.videosEnabled || this.photosEnabled) {
            return;
        }
        checkCanRemoveRestrictionsByBoosts();
    }

    public void onPause() {
        int i = 0;
        while (true) {
            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
            if (i >= attachAlertLayoutArr.length) {
                this.paused = true;
                return;
            }
            AttachAlertLayout attachAlertLayout = attachAlertLayoutArr[i];
            if (attachAlertLayout != null) {
                attachAlertLayout.onPause();
            }
            i++;
        }
    }

    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        ChatAttachAlertLocationLayout chatAttachAlertLocationLayout;
        if (i == 5 && iArr != null && iArr.length > 0 && iArr[0] == 0) {
            openContactsLayout();
        } else if (i == 30 && (chatAttachAlertLocationLayout = this.locationLayout) != null && this.currentAttachLayout == chatAttachAlertLocationLayout && isShowing()) {
            this.locationLayout.openShareLiveLocation();
        }
    }

    public void onResume() {
        int i = 0;
        this.paused = false;
        while (true) {
            AttachAlertLayout[] attachAlertLayoutArr = this.layouts;
            if (i >= attachAlertLayoutArr.length) {
                break;
            }
            AttachAlertLayout attachAlertLayout = attachAlertLayoutArr[i];
            if (attachAlertLayout != null) {
                attachAlertLayout.onResume();
            }
            i++;
        }
        if (isShowing()) {
            this.delegate.needEnterComment();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void onStart() {
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

    public void openColorsLayout() {
        if (this.colorsLayout == null) {
            ChatAttachAlertColorsLayout chatAttachAlertColorsLayout = new ChatAttachAlertColorsLayout(this, getContext(), this.resourcesProvider);
            this.colorsLayout = chatAttachAlertColorsLayout;
            chatAttachAlertColorsLayout.setDelegate(new androidx.core.util.Consumer() { // from class: org.telegram.ui.Components.ChatAttachAlert$$ExternalSyntheticLambda48
                @Override // androidx.core.util.Consumer
                public final void accept(Object obj) {
                    ChatAttachAlert.this.lambda$openColorsLayout$36(obj);
                }
            });
        }
        showLayout(this.colorsLayout);
    }

    public void presentFragment(PhotoPickerActivity photoPickerActivity) {
        BaseFragment baseFragment = this.baseFragment;
        if (baseFragment == null && (baseFragment = LaunchActivity.getLastFragment()) == null) {
            return;
        }
        baseFragment.presentFragment(photoPickerActivity);
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void setAllowDrawContent(boolean z) {
        super.setAllowDrawContent(z);
        this.currentAttachLayout.onContainerTranslationUpdated(this.currentPanTranslationY);
        if (this.allowDrawContent != z) {
            this.allowDrawContent = z;
            AttachAlertLayout attachAlertLayout = this.currentAttachLayout;
            ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout = this.photoLayout;
            if (attachAlertLayout != chatAttachAlertPhotoLayout || chatAttachAlertPhotoLayout == null || chatAttachAlertPhotoLayout.cameraExpanded) {
                return;
            }
            chatAttachAlertPhotoLayout.pauseCamera(!z || this.sent);
        }
    }

    public void setAllowEnterCaption(boolean z) {
        this.allowEnterCaption = z;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    public void setAllowNestedScroll(boolean z) {
        this.allowNestedScroll = z;
    }

    public void setAvatarPicker(int i, boolean z) {
        TextView textView;
        int i2;
        this.avatarPicker = i;
        this.avatarSearch = z;
        if (i != 0) {
            this.typeButtonsAvailable = false;
            AttachAlertLayout attachAlertLayout = this.currentAttachLayout;
            if (attachAlertLayout == null || attachAlertLayout == this.photoLayout) {
                this.buttonsRecyclerView.setVisibility(8);
                this.shadow.setVisibility(8);
            }
            if (this.avatarPicker == 2) {
                textView = this.selectedTextView;
                i2 = R.string.ChoosePhotoOrVideo;
            } else {
                textView = this.selectedTextView;
                i2 = R.string.ChoosePhoto;
            }
            textView.setText(LocaleController.getString(i2));
        } else {
            this.typeButtonsAvailable = true;
        }
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout = this.photoLayout;
        if (chatAttachAlertPhotoLayout != null) {
            chatAttachAlertPhotoLayout.updateAvatarPicker();
        }
    }

    public void setCanOpenPreview(boolean z) {
        this.canOpenPreview = z;
        this.selectedArrowImageView.setVisibility((!z || this.avatarPicker == 2) ? 8 : 0);
    }

    public void setDelegate(ChatAttachViewDelegate chatAttachViewDelegate) {
        this.delegate = chatAttachViewDelegate;
    }

    public void setDialogId(long j) {
        this.dialogId = j;
    }

    public void setDocumentsDelegate(ChatAttachAlertDocumentLayout.DocumentSelectActivityDelegate documentSelectActivityDelegate) {
        this.documentsDelegate = documentSelectActivityDelegate;
    }

    public void setEditingMessageObject(MessageObject messageObject) {
        if (this.editingMessageObject == messageObject) {
            return;
        }
        this.editingMessageObject = messageObject;
        if (messageObject != null) {
            this.maxSelectedPhotos = 1;
            this.allowOrder = false;
        } else {
            this.maxSelectedPhotos = -1;
            this.allowOrder = true;
        }
        this.buttonsAdapter.notifyDataSetChanged();
    }

    public void setImageUpdater(ImageUpdater imageUpdater) {
        this.parentImageUpdater = imageUpdater;
    }

    public void setMaxSelectedPhotos(int i, boolean z) {
        if (this.editingMessageObject != null) {
            return;
        }
        this.maxSelectedPhotos = i;
        this.allowOrder = z;
    }

    public void setOpenWithFrontFaceCamera(boolean z) {
        this.openWithFrontFaceCamera = z;
    }

    public void setSoundPicker() {
        this.isSoundPicker = true;
        this.buttonsRecyclerView.setVisibility(8);
        this.shadow.setVisibility(8);
        this.selectedTextView.setText(LocaleController.getString(R.string.ChoosePhotoOrVideo));
    }

    public void setStoryAudioPicker() {
        this.isStoryAudioPicker = true;
    }

    public void setStoryLocationPicker() {
        this.isStoryLocationPicker = true;
        this.buttonsRecyclerView.setVisibility(8);
        this.shadow.setVisibility(8);
    }

    public void setStoryLocationPicker(double d, double d2) {
        this.storyLocationPickerLatLong = new double[]{d, d2};
        this.isStoryLocationPicker = true;
        this.buttonsRecyclerView.setVisibility(8);
        this.shadow.setVisibility(8);
    }

    public void setStoryLocationPicker(boolean z, File file) {
        this.storyLocationPickerFileIsVideo = z;
        this.storyLocationPickerPhotoFile = file;
        this.isStoryLocationPicker = true;
        this.buttonsRecyclerView.setVisibility(8);
        this.shadow.setVisibility(8);
    }

    public void setStoryMediaPicker() {
        this.storyMediaPicker = true;
        this.typeButtonsAvailable = false;
        this.selectedTextView.setText(LocaleController.getString(R.string.ChoosePhotoOrVideo));
    }

    protected void setupMentionContainer() {
        this.mentionContainer.getAdapter().setAllowStickers(false);
        this.mentionContainer.getAdapter().setAllowBots(false);
        this.mentionContainer.getAdapter().setAllowChats(false);
        this.mentionContainer.getAdapter().setSearchInDailogs(true);
        if (this.baseFragment instanceof ChatActivity) {
            this.mentionContainer.getAdapter().setChatInfo(((ChatActivity) this.baseFragment).getCurrentChatInfo());
            this.mentionContainer.getAdapter().setNeedUsernames(((ChatActivity) this.baseFragment).getCurrentChat() != null);
        } else {
            this.mentionContainer.getAdapter().setChatInfo(null);
            this.mentionContainer.getAdapter().setNeedUsernames(false);
        }
        this.mentionContainer.getAdapter().setNeedBotContext(false);
    }

    public void setupPhotoPicker(String str) {
        this.avatarPicker = 1;
        this.isPhotoPicker = true;
        this.avatarSearch = false;
        this.typeButtonsAvailable = false;
        this.videosEnabled = false;
        this.buttonsRecyclerView.setVisibility(8);
        this.shadow.setVisibility(8);
        this.selectedTextView.setText(str);
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout = this.photoLayout;
        if (chatAttachAlertPhotoLayout != null) {
            chatAttachAlertPhotoLayout.updateAvatarPicker();
        }
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet
    protected boolean shouldOverlayCameraViewOverNavBar() {
        AttachAlertLayout attachAlertLayout = this.currentAttachLayout;
        ChatAttachAlertPhotoLayout chatAttachAlertPhotoLayout = this.photoLayout;
        return attachAlertLayout == chatAttachAlertPhotoLayout && chatAttachAlertPhotoLayout.cameraExpanded;
    }

    @Override // org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog
    public void show() {
        super.show();
        this.buttonPressed = false;
        BaseFragment baseFragment = this.baseFragment;
        if (baseFragment instanceof ChatActivity) {
            this.calcMandatoryInsets = ((ChatActivity) baseFragment).isKeyboardVisible();
        }
        this.openTransitionFinished = false;
        if (Build.VERSION.SDK_INT >= 30) {
            this.navBarColorKey = -1;
            this.navBarColor = ColorUtils.setAlphaComponent(getThemedColor(Theme.key_windowBackgroundGray), 0);
            AndroidUtilities.setNavigationBarColor(getWindow(), this.navBarColor, false);
            AndroidUtilities.setLightNavigationBar(getWindow(), ((double) AndroidUtilities.computePerceivedBrightness(this.navBarColor)) > 0.721d);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x002c, code lost:
        if (((org.telegram.ui.bots.ChatAttachAlertBotWebViewLayout) r16.botAttachLayouts.get(r17)).needReload() == false) goto L7;
     */
    /* JADX WARN: Removed duplicated region for block: B:46:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:50:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void showBotLayout(long j, String str, boolean z, boolean z2) {
        long j2;
        if (this.botAttachLayouts.get(j) != null) {
            if (Objects.equals(str, ((ChatAttachAlertBotWebViewLayout) this.botAttachLayouts.get(j)).getStartCommand())) {
            }
        }
        if (this.baseFragment instanceof ChatActivity) {
            ChatAttachAlertBotWebViewLayout chatAttachAlertBotWebViewLayout = new ChatAttachAlertBotWebViewLayout(this, getContext(), this.resourcesProvider);
            this.botAttachLayouts.put(j, chatAttachAlertBotWebViewLayout);
            ((ChatAttachAlertBotWebViewLayout) this.botAttachLayouts.get(j)).setDelegate(new 1(chatAttachAlertBotWebViewLayout, str, j));
            MessageObject replyingMessageObject = ((ChatActivity) this.baseFragment).getChatActivityEnterView().getReplyingMessageObject();
            j2 = j;
            ((ChatAttachAlertBotWebViewLayout) this.botAttachLayouts.get(j)).requestWebView(this.currentAccount, ((ChatActivity) this.baseFragment).getDialogId(), j, false, replyingMessageObject != null ? replyingMessageObject.messageOwner.id : 0, str);
            if (this.botAttachLayouts.get(j2) == null) {
                ((ChatAttachAlertBotWebViewLayout) this.botAttachLayouts.get(j2)).disallowSwipeOffsetAnimation();
                showLayout((AttachAlertLayout) this.botAttachLayouts.get(j2), -j2, z2);
                if (z) {
                    ((ChatAttachAlertBotWebViewLayout) this.botAttachLayouts.get(j2)).showJustAddedBulletin();
                    return;
                }
                return;
            }
            return;
        }
        j2 = j;
        if (this.botAttachLayouts.get(j2) == null) {
        }
    }

    public void showBotLayout(long j, boolean z) {
        showBotLayout(j, null, false, z);
    }

    public void showLayout(AttachAlertLayout attachAlertLayout) {
        long j = this.selectedId;
        ChatAttachRestrictedLayout chatAttachRestrictedLayout = this.restrictedLayout;
        if (attachAlertLayout == chatAttachRestrictedLayout) {
            j = chatAttachRestrictedLayout.id;
        } else if (attachAlertLayout == this.photoLayout) {
            j = 1;
        } else if (attachAlertLayout == this.audioLayout) {
            j = 3;
        } else if (attachAlertLayout == this.documentLayout) {
            j = 4;
        } else if (attachAlertLayout == this.contactsLayout) {
            j = 5;
        } else if (attachAlertLayout == this.locationLayout) {
            j = 6;
        } else if (attachAlertLayout == this.pollLayout) {
            j = 9;
        } else if (attachAlertLayout == this.colorsLayout) {
            j = 10;
        } else if (attachAlertLayout == this.quickRepliesLayout) {
            j = 11;
        }
        showLayout(attachAlertLayout, j);
    }

    public void updateCommentTextViewPosition() {
        this.commentTextView.getLocationOnScreen(this.commentTextViewLocation);
        if (this.mentionContainer != null) {
            float f = -this.commentTextView.getHeight();
            if (this.mentionContainer.getY() != f) {
                this.mentionContainer.setTranslationY(f);
                this.mentionContainer.invalidate();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:226:0x00ce, code lost:
        if (r3 != null) goto L54;
     */
    /* JADX WARN: Removed duplicated region for block: B:216:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:230:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:257:0x011c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateCountButton(int i) {
        boolean z;
        View view;
        ActionBarMenuItem actionBarMenuItem;
        if (this.viewChangeAnimator != null) {
            return;
        }
        int selectedItemsCount = this.currentAttachLayout.getSelectedItemsCount();
        if (selectedItemsCount == 0) {
            this.writeButton.setCount(0, i != 0);
            showCommentTextView(false, i != 0);
        } else {
            if (showCommentTextView(true, i != 0) || i == 0) {
                this.writeButton.setCount(selectedItemsCount, i != 0);
            } else {
                this.writeButton.setCount(selectedItemsCount, true);
            }
            this.writeButton.bounceCount();
        }
        this.currentAttachLayout.onSelectedItemsCountChanged(selectedItemsCount);
        if (this.currentAttachLayout != this.photoLayout) {
            return;
        }
        if (!(this.baseFragment instanceof ChatActivity) && this.avatarPicker == 0 && !this.storyMediaPicker) {
            return;
        }
        if (!(selectedItemsCount == 0 && this.menuShowed) && ((selectedItemsCount == 0 && this.avatarPicker == 0 && !this.storyMediaPicker) || this.menuShowed)) {
            return;
        }
        this.menuShowed = (selectedItemsCount == 0 && this.avatarPicker == 0 && !this.storyMediaPicker) ? false : true;
        AnimatorSet animatorSet = this.menuAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.menuAnimator = null;
        }
        if (this.avatarPicker != 0 && this.searchItem != null && this.actionBar.getTag() != null) {
            BaseFragment baseFragment = this.baseFragment;
            if ((baseFragment instanceof ChatActivity) && ((ChatActivity) baseFragment).allowSendGifs()) {
                z = true;
                if (this.menuShowed) {
                    if (this.actionBar.getTag() != null) {
                        view = this.searchItem;
                    }
                    if (i == 0) {
                        if (this.actionBar.getTag() == null && this.avatarPicker == 0 && !this.storyMediaPicker) {
                            this.selectedMenuItem.setAlpha(this.menuShowed ? 1.0f : 0.0f);
                        }
                        this.headerView.setAlpha(this.menuShowed ? 1.0f : 0.0f);
                        if (z) {
                            this.searchItem.setAlpha(this.menuShowed ? 0.0f : 1.0f);
                        }
                        if (!this.menuShowed || (actionBarMenuItem = this.searchItem) == null) {
                            return;
                        }
                        actionBarMenuItem.setVisibility(4);
                        return;
                    }
                    this.menuAnimator = new AnimatorSet();
                    ArrayList arrayList = new ArrayList();
                    if (this.actionBar.getTag() == null && this.avatarPicker == 0 && !this.storyMediaPicker) {
                        arrayList.add(ObjectAnimator.ofFloat(this.selectedMenuItem, View.ALPHA, this.menuShowed ? 1.0f : 0.0f));
                    }
                    FrameLayout frameLayout = this.headerView;
                    Property property = View.ALPHA;
                    arrayList.add(ObjectAnimator.ofFloat(frameLayout, property, this.menuShowed ? 1.0f : 0.0f));
                    if (z) {
                        arrayList.add(ObjectAnimator.ofFloat(this.searchItem, property, this.menuShowed ? 0.0f : 1.0f));
                    }
                    this.menuAnimator.playTogether(arrayList);
                    this.menuAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatAttachAlert.24
                        {
                            ChatAttachAlert.this = this;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            View view2;
                            ChatAttachAlert.this.menuAnimator = null;
                            if (ChatAttachAlert.this.menuShowed) {
                                view2 = ChatAttachAlert.this.searchItem;
                                if (view2 == null) {
                                    return;
                                }
                            } else {
                                if (ChatAttachAlert.this.actionBar.getTag() == null) {
                                    ChatAttachAlert chatAttachAlert = ChatAttachAlert.this;
                                    if (chatAttachAlert.avatarPicker == 0 && !chatAttachAlert.storyMediaPicker) {
                                        chatAttachAlert.selectedMenuItem.setVisibility(4);
                                    }
                                }
                                view2 = ChatAttachAlert.this.headerView;
                            }
                            view2.setVisibility(4);
                        }
                    });
                    this.menuAnimator.setDuration(180L);
                    this.menuAnimator.start();
                    return;
                }
                if (this.avatarPicker == 0 && !this.storyMediaPicker) {
                    this.selectedMenuItem.setVisibility(0);
                }
                view = this.headerView;
                view.setVisibility(0);
                if (i == 0) {
                }
            }
        }
        z = false;
        if (this.menuShowed) {
        }
        view.setVisibility(0);
        if (i == 0) {
        }
    }

    public void updateLayout(AttachAlertLayout attachAlertLayout, boolean z, int i) {
        int currentItemTop;
        if (attachAlertLayout == null || (currentItemTop = attachAlertLayout.getCurrentItemTop()) == Integer.MAX_VALUE) {
            return;
        }
        boolean z2 = false;
        boolean z3 = attachAlertLayout == this.currentAttachLayout && currentItemTop <= attachAlertLayout.getButtonsHideOffset();
        this.pinnedToTop = z3;
        AttachAlertLayout attachAlertLayout2 = this.currentAttachLayout;
        if (attachAlertLayout2 != this.photoPreviewLayout && this.keyboardVisible && z) {
            boolean z4 = attachAlertLayout2 instanceof ChatAttachAlertBotWebViewLayout;
        }
        if (attachAlertLayout == attachAlertLayout2) {
            updateActionBarVisibility(z3, true);
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) attachAlertLayout.getLayoutParams();
        int dp = currentItemTop + ((layoutParams == null ? 0 : layoutParams.topMargin) - AndroidUtilities.dp(11.0f));
        AttachAlertLayout attachAlertLayout3 = this.currentAttachLayout;
        int i2 = attachAlertLayout3 == attachAlertLayout ? 0 : 1;
        if ((attachAlertLayout3 instanceof ChatAttachAlertPhotoLayoutPreview) || (this.nextAttachLayout instanceof ChatAttachAlertPhotoLayoutPreview)) {
            Object obj = this.viewChangeAnimator;
            if ((obj instanceof SpringAnimation) && ((SpringAnimation) obj).isRunning()) {
                z2 = true;
            }
        }
        int[] iArr = this.scrollOffsetY;
        int i3 = iArr[i2];
        if (i3 == dp && !z2) {
            if (i != 0) {
                this.previousScrollOffsetY = i3;
                return;
            }
            return;
        }
        this.previousScrollOffsetY = i3;
        iArr[i2] = dp;
        updateSelectedPosition(i2);
        this.containerView.invalidate();
    }

    public void updatePhotoPreview(boolean z) {
        if (!z) {
            showLayout(this.photoLayout);
        } else if (this.canOpenPreview) {
            if (this.photoPreviewLayout == null) {
                Context context = getContext();
                Theme.ResourcesProvider resourcesProvider = this.parentThemeDelegate;
                if (resourcesProvider == null) {
                    resourcesProvider = this.resourcesProvider;
                }
                ChatAttachAlertPhotoLayoutPreview chatAttachAlertPhotoLayoutPreview = new ChatAttachAlertPhotoLayoutPreview(this, context, resourcesProvider);
                this.photoPreviewLayout = chatAttachAlertPhotoLayoutPreview;
                chatAttachAlertPhotoLayoutPreview.bringToFront();
            }
            AttachAlertLayout attachAlertLayout = this.currentAttachLayout;
            AttachAlertLayout attachAlertLayout2 = this.photoPreviewLayout;
            if (attachAlertLayout == attachAlertLayout2) {
                attachAlertLayout2 = this.photoLayout;
            }
            showLayout(attachAlertLayout2);
        }
    }
}
