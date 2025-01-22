package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Property;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.collection.LongSparseArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.math.MathUtils;
import androidx.core.os.BuildCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.ChatListItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BirthdayController;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagePreviewParams;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.NotificationsSettingsFacade;
import org.telegram.messenger.R;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.SharedPrefsHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.camera.CameraController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_account;
import org.telegram.tgnet.tl.TL_bots;
import org.telegram.tgnet.tl.TL_stories;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Business.BusinessLinksController;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedTextView;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.ChatActivityEnterViewAnimatedIconView;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.MessagePreviewView;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.Premium.boosts.BoostRepository;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBar;
import org.telegram.ui.Components.SenderSelectPopup;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.SuggestEmojiView;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.VideoTimelineView;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.Gifts.GiftSheet;
import org.telegram.ui.GroupStickersActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.MessageSendPreview;
import org.telegram.ui.MultiContactsSelectorBottomSheet;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.PremiumPreviewFragment;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.StickersActivity;
import org.telegram.ui.Stories.recorder.CaptionContainerView;
import org.telegram.ui.Stories.recorder.HintView2;
import org.telegram.ui.TopicsFragment;
import org.telegram.ui.bots.BotCommandsMenuContainer;
import org.telegram.ui.bots.BotCommandsMenuView;
import org.telegram.ui.bots.BotKeyboardView;
import org.telegram.ui.bots.BotWebViewSheet;
import org.telegram.ui.bots.ChatActivityBotWebViewButton;
import org.telegram.ui.bots.WebViewRequestProps;

/* loaded from: classes3.dex */
public class ChatActivityEnterView extends BlurredFrameLayout implements NotificationCenter.NotificationCenterDelegate, SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate, StickersAlert.StickersAlertDelegate, SuggestEmojiView.AnchorViewDelegate {
    private final Property ATTACH_LAYOUT_ALPHA;
    private final Property ATTACH_LAYOUT_TRANSLATION_X;
    private final Property EMOJI_BUTTON_ALPHA;
    private final Property EMOJI_BUTTON_SCALE;
    private final Property MESSAGE_TEXT_TRANSLATION_X;
    private AccountInstance accountInstance;
    private ActionBarMenuSubItem actionScheduleButton;
    private AdjustPanLayoutHelper adjustPanLayoutHelper;
    private boolean allowAnimatedEmoji;
    public boolean allowBlur;
    private boolean allowGifs;
    private boolean allowShowTopView;
    private boolean allowStickers;
    protected int animatedTop;
    private int animatingContentType;
    private Runnable animationEndRunnable;
    private HashMap animationParamsX;
    private ImageView attachButton;
    private LinearLayout attachLayout;
    private float attachLayoutAlpha;
    private float attachLayoutPaddingAlpha;
    protected float attachLayoutPaddingTranslationX;
    private float attachLayoutTranslationX;
    private TLRPC.TL_document audioToSend;
    private MessageObject audioToSendMessageObject;
    private String audioToSendPath;
    private FrameLayout audioVideoButtonContainer;
    private ChatActivityEnterViewAnimatedIconView audioVideoSendButton;
    Paint backgroundPaint;
    public HintView2 birthdayHint;
    private android.graphics.Rect blurBounds;
    private ImageView botButton;
    private ReplaceableIconDrawable botButtonDrawable;
    private MessageObject botButtonsMessageObject;
    int botCommandLastPosition;
    int botCommandLastTop;
    private BotCommandsMenuView.BotCommandsAdapter botCommandsAdapter;
    private BotCommandsMenuView botCommandsMenuButton;
    public BotCommandsMenuContainer botCommandsMenuContainer;
    private int botCount;
    private BotKeyboardView botKeyboardView;
    private boolean botKeyboardViewVisible;
    private BotMenuButtonType botMenuButtonType;
    private String botMenuWebViewTitle;
    private String botMenuWebViewUrl;
    private MessageObject botMessageObject;
    private TLRPC.TL_replyKeyboardMarkup botReplyMarkup;
    private ChatActivityBotWebViewButton botWebViewButton;
    private final AnimatedFloat bottomGradientAlpha;
    private boolean calledRecordRunnable;
    private Drawable cameraDrawable;
    private Drawable cameraOutline;
    private boolean canWriteToChannel;
    private ImageView cancelBotButton;
    private boolean canceledByGesture;
    private boolean captionAbove;
    private boolean captionLimitBulletinShown;
    private NumberTextView captionLimitView;
    private float chatSearchExpandOffset;
    private boolean clearBotButtonsOnKeyboardOpen;
    private final LinearGradient clipGradient;
    private final Matrix clipMatrix;
    private boolean closeAnimationInProgress;
    private int codePointCount;
    private int commonInputType;
    private float composeShadowAlpha;
    private float controlsScale;
    public ControlsView controlsView;
    boolean ctrlPressed;
    private int currentAccount;
    private int currentLimit;
    private int currentPopupContentType;
    public ValueAnimator currentTopViewAnimation;
    private ChatActivityEnterViewDelegate delegate;
    private boolean destroyed;
    private long dialog_id;
    private final Runnable dismissSendPreview;
    private float distCanMove;
    private SendButton doneButton;
    private AnimatorSet doneButtonAnimation;
    boolean doneButtonEnabled;
    private float doneButtonEnabledProgress;
    private Drawable doneCheckDrawable;
    private Paint dotPaint;
    private CharSequence draftMessage;
    private boolean draftSearchWebpage;
    private TL_account.TL_businessChatLink editingBusinessLink;
    private boolean editingCaption;
    private MessageObject editingMessageObject;
    private long effectId;
    private ChatActivityEnterViewAnimatedIconView emojiButton;
    float emojiButtonAlpha;
    float emojiButtonPaddingAlpha;
    float emojiButtonPaddingScale;
    private boolean emojiButtonRestricted;
    float emojiButtonScale;
    private int emojiPadding;
    private boolean emojiTabOpen;
    private EmojiView emojiView;
    private boolean emojiViewFrozen;
    private boolean emojiViewVisible;
    private float exitTransition;
    private ImageView expandStickersButton;
    private Runnable focusRunnable;
    private boolean forceShowSendButton;
    private ImageView giftButton;
    private final Paint gradientPaint;
    private boolean hasBotCommands;
    private boolean hasQuickReplies;
    private boolean hasRecordVideo;
    private Runnable hideKeyboardRunnable;
    private float horizontalPadding;
    float idleProgress;
    private boolean ignoreTextChange;
    private TLRPC.ChatFull info;
    private int innerTextChange;
    private final boolean isChat;
    private boolean isInVideoMode;
    private boolean isInitLineCount;
    private boolean isPaste;
    private boolean isPaused;
    public boolean isStories;
    private int keyboardHeight;
    private int keyboardHeightLand;
    private boolean keyboardVisible;
    private LongSparseArray lastBotInfo;
    private int lastRecordState;
    private BusinessLinkPresetMessage lastSavedBusinessLinkMessage;
    private int lastSizeChangeValue1;
    private boolean lastSizeChangeValue2;
    private long lastTypingTimeSend;
    private int lineCount;
    private int[] location;
    private float lockAnimatedTranslation;
    private Drawable lockShadowDrawable;
    private View.AccessibilityDelegate mediaMessageButtonsDelegate;
    protected EditTextCaption messageEditText;
    protected FrameLayout messageEditTextContainer;
    private boolean messageEditTextEnabled;
    private ArrayList messageEditTextWatchers;
    public MessageSendPreview messageSendPreview;
    private float messageTextPaddingTranslationX;
    private float messageTextTranslationX;
    boolean messageTransitionIsRunning;
    private TLRPC.WebPage messageWebPage;
    private boolean messageWebPageSearch;
    private Drawable micDrawable;
    private Drawable micOutline;
    private long millisecondsRecorded;
    private Runnable moveToSendStateRunnable;
    private boolean needShowTopView;
    private AnimationNotificationsLocker notificationsLocker;
    private ImageView notifyButton;
    private CrossOutDrawable notifySilentDrawable;
    private Runnable onEmojiSearchClosed;
    private Runnable onFinishInitCameraRunnable;
    private Runnable onKeyboardClosed;
    public boolean onceVisible;
    private Runnable openKeyboardRunnable;
    private int originalViewHeight;
    private CharSequence overrideHint;
    private CharSequence overrideHint2;
    private boolean overrideKeyboardAnimation;
    private Paint paint;
    private AnimatorSet panelAnimation;
    private Activity parentActivity;
    private ChatActivity parentFragment;
    private RectF pauseRect;
    private TLRPC.KeyboardButton pendingLocationButton;
    private MessageObject pendingMessageObject;
    private MediaActionDrawable playPauseDrawable;
    private int popupX;
    private int popupY;
    private boolean premiumEmojiBulletin;
    public boolean preventInput;
    private CloseProgressDrawable2 progressDrawable;
    private ImageView reactionsButton;
    private Runnable recordAudioVideoRunnable;
    private boolean recordAudioVideoRunnableStarted;
    private RecordCircle recordCircle;
    private Property recordCircleScale;
    private Property recordControlsCircleScale;
    private RLottieImageView recordDeleteImageView;
    private RecordDot recordDot;
    private int recordInterfaceState;
    private boolean recordIsCanceled;
    private FrameLayout recordPanel;
    private AnimatorSet recordPannelAnimation;
    private LinearLayout recordTimeContainer;
    private TimerView recordTimerView;
    private View recordedAudioBackground;
    protected FrameLayout recordedAudioPanel;
    private ImageView recordedAudioPlayButton;
    protected SeekBarWaveformView recordedAudioSeekBar;
    private TextView recordedAudioTimeTextView;
    private boolean recordingAudioVideo;
    public int recordingGuid;
    private android.graphics.Rect rect;
    private Paint redDotPaint;
    private boolean removeEmojiViewAfterAnimation;
    private MessageObject replyingMessageObject;
    private ChatActivity.ReplyQuote replyingQuote;
    private MessageObject replyingTopMessage;
    private final Theme.ResourcesProvider resourcesProvider;
    private Property roundedTranslationYProperty;
    private Runnable runEmojiPanelAnimation;
    private AnimatorSet runningAnimation;
    private AnimatorSet runningAnimation2;
    private AnimatorSet runningAnimationAudio;
    private int runningAnimationType;
    private float scale;
    private boolean scheduleButtonHidden;
    private ImageView scheduledButton;
    private AnimatorSet scheduledButtonAnimation;
    private ValueAnimator searchAnimator;
    private float searchToOpenProgress;
    private int searchingType;
    private SeekBarWaveform seekBarWaveform;
    private SendButton sendButton;
    private int sendButtonBackgroundColor;
    private FrameLayout sendButtonContainer;
    private boolean sendButtonEnabled;
    private boolean sendButtonVisible;
    private boolean sendByEnter;
    private Drawable sendDrawable;
    public boolean sendPlainEnabled;
    private ActionBarPopupWindow.ActionBarPopupWindowLayout sendPopupLayout;
    private ActionBarPopupWindow sendPopupWindow;
    private android.graphics.Rect sendRect;
    private boolean sendRoundEnabled;
    private boolean sendVoiceEnabled;
    private ActionBarMenuSubItem sendWhenOnlineButton;
    private SenderSelectPopup senderSelectPopupWindow;
    private SenderSelectView senderSelectView;
    private long sentFromPreview;
    private Runnable setTextFieldRunnable;
    boolean shiftPressed;
    protected boolean shouldAnimateEditTextWithBounds;
    public boolean shouldDrawBackground;
    public boolean shouldDrawRecordedAudioPanelInParent;
    private boolean showKeyboardOnResume;
    private boolean showTooltip;
    private long showTooltipStartTime;
    private Runnable showTopViewRunnable;
    private boolean silent;
    private SizeNotifierFrameLayout sizeNotifierLayout;
    private int slideDelta;
    private SlideTextView slideText;
    private float slideToCancelLockProgress;
    private float slideToCancelProgress;
    private SlowModeBtn slowModeButton;
    private int slowModeTimer;
    private boolean smoothKeyboard;
    private float snapAnimationProgress;
    private float startTranslation;
    private float startedDraggingX;
    private AnimatedArrowDrawable stickersArrow;
    private boolean stickersDragging;
    private boolean stickersEnabled;
    private boolean stickersExpanded;
    private int stickersExpandedHeight;
    private Animator stickersExpansionAnim;
    private float stickersExpansionProgress;
    private boolean stickersTabOpen;
    private FrameLayout textFieldContainer;
    boolean textTransitionIsRunning;
    private float tooltipAlpha;
    private final AnimatedFloat topGradientAlpha;
    protected View topLineView;
    protected View topView;
    protected float topViewEnterProgress;
    protected boolean topViewShowed;
    private final ValueAnimator.AnimatorUpdateListener topViewUpdateListener;
    private float transformToSeekbar;
    private TrendingStickersAlert trendingStickersAlert;
    private Runnable updateExpandabilityRunnable;
    private Runnable updateSlowModeRunnable;
    private TLRPC.UserFull userInfo;
    protected VideoTimelineView videoTimelineView;
    private VideoEditedInfo videoToSendMessageObject;
    public boolean voiceOnce;
    private boolean waitingForKeyboardOpen;
    private boolean waitingForKeyboardOpenAfterAnimation;
    private PowerManager.WakeLock wakeLock;
    private boolean wasSendTyping;

    class 16 extends FrameLayout {
        16(Context context) {
            super(context);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Boolean lambda$drawChild$0(Canvas canvas, View view, long j) {
            return Boolean.valueOf(super.drawChild(canvas, view, j));
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            return super.dispatchTouchEvent(motionEvent);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(final Canvas canvas, final View view, final long j) {
            if (view != null) {
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                if (view == chatActivityEnterView.messageEditText) {
                    return chatActivityEnterView.drawMessageEditText(canvas, new Utilities.Callback0Return() { // from class: org.telegram.ui.Components.ChatActivityEnterView$16$$ExternalSyntheticLambda0
                        @Override // org.telegram.messenger.Utilities.Callback0Return
                        public final Object run() {
                            Boolean lambda$drawChild$0;
                            lambda$drawChild$0 = ChatActivityEnterView.16.this.lambda$drawChild$0(canvas, view, j);
                            return lambda$drawChild$0;
                        }
                    });
                }
            }
            ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
            if (chatActivityEnterView2.shouldDrawRecordedAudioPanelInParent && view == chatActivityEnterView2.recordedAudioPanel) {
                return true;
            }
            return super.drawChild(canvas, view, j);
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            if (ChatActivityEnterView.this.scheduledButton != null) {
                int measuredWidth = (getMeasuredWidth() - AndroidUtilities.dp((ChatActivityEnterView.this.botButton == null || ChatActivityEnterView.this.botButton.getVisibility() != 0) ? 48.0f : 96.0f)) - AndroidUtilities.dp(48.0f);
                ChatActivityEnterView.this.scheduledButton.layout(measuredWidth, ChatActivityEnterView.this.scheduledButton.getTop(), ChatActivityEnterView.this.scheduledButton.getMeasuredWidth() + measuredWidth, ChatActivityEnterView.this.scheduledButton.getBottom());
            }
            if (ChatActivityEnterView.this.animationParamsX.isEmpty()) {
                return;
            }
            for (int i5 = 0; i5 < getChildCount(); i5++) {
                View childAt = getChildAt(i5);
                Float f = (Float) ChatActivityEnterView.this.animationParamsX.get(childAt);
                if (f != null) {
                    childAt.setTranslationX(f.floatValue() - childAt.getLeft());
                    childAt.animate().translationX(0.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                }
            }
            ChatActivityEnterView.this.animationParamsX.clear();
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return super.onTouchEvent(motionEvent);
        }
    }

    class 20 extends FrameLayout {
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        20(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.val$resourcesProvider = resourcesProvider;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onTouchEvent$0(boolean z, int i) {
            MediaController.getInstance().stopRecording(1, z, i, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onTouchEvent$1() {
            MediaController.getInstance().stopRecording(0, false, 0, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$2() {
            ChatActivityEnterView.this.moveToSendStateRunnable = null;
            ChatActivityEnterView.this.updateRecordInterface(1, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onTouchEvent$3(boolean z, int i) {
            MediaController.getInstance().stopRecording(1, z, i, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ void lambda$onTouchEvent$4() {
            MediaController.getInstance().stopRecording(0, false, 0, false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$5() {
            ChatActivityEnterView.this.moveToSendStateRunnable = null;
            ChatActivityEnterView.this.updateRecordInterface(1, true);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return true;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            FrameLayout frameLayout;
            ChatActivityEnterView.this.createRecordCircle();
            if (motionEvent.getAction() == 0) {
                if (!ChatActivityEnterView.this.recordCircle.isSendButtonVisible()) {
                    TLRPC.Chat currentChat = ChatActivityEnterView.this.parentFragment == null ? null : ChatActivityEnterView.this.parentFragment.getCurrentChat();
                    TLRPC.UserFull currentUserInfo = ChatActivityEnterView.this.parentFragment == null ? ChatActivityEnterView.this.userInfo : ChatActivityEnterView.this.parentFragment.getCurrentUserInfo();
                    if ((currentChat != null && !ChatObject.canSendVoice(currentChat) && (!ChatObject.canSendRoundVideo(currentChat) || !ChatActivityEnterView.this.hasRecordVideo)) || (currentUserInfo != null && currentUserInfo.voice_messages_forbidden)) {
                        ChatActivityEnterView.this.delegate.needShowMediaBanHint();
                        return true;
                    }
                    if (ChatActivityEnterView.this.hasRecordVideo) {
                        ChatActivityEnterView.this.calledRecordRunnable = false;
                        ChatActivityEnterView.this.recordAudioVideoRunnableStarted = true;
                        AndroidUtilities.runOnUIThread(ChatActivityEnterView.this.recordAudioVideoRunnable, 150L);
                    } else {
                        ChatActivityEnterView.this.recordAudioVideoRunnable.run();
                    }
                    return true;
                }
                if (!ChatActivityEnterView.this.hasRecordVideo || ChatActivityEnterView.this.calledRecordRunnable) {
                    ChatActivityEnterView.this.startedDraggingX = -1.0f;
                    if (ChatActivityEnterView.this.hasRecordVideo && ChatActivityEnterView.this.isInVideoMode()) {
                        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = ChatActivityEnterView.this.delegate;
                        ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                        chatActivityEnterViewDelegate.needStartRecordVideo(1, true, 0, chatActivityEnterView.voiceOnce ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0, chatActivityEnterView.effectId);
                        ChatActivityEnterView.this.sendButton.setEffect(ChatActivityEnterView.this.effectId = 0L);
                    } else {
                        if (ChatActivityEnterView.this.recordingAudioVideo && ChatActivityEnterView.this.isInScheduleMode()) {
                            AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$20$$ExternalSyntheticLambda0
                                @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                                public final void didSelectDate(boolean z, int i) {
                                    ChatActivityEnterView.20.lambda$onTouchEvent$0(z, i);
                                }
                            }, new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$20$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ChatActivityEnterView.20.lambda$onTouchEvent$1();
                                }
                            }, this.val$resourcesProvider);
                        }
                        MediaController.getInstance().stopRecording(ChatActivityEnterView.this.isInScheduleMode() ? 3 : 1, true, 0, ChatActivityEnterView.this.voiceOnce);
                        ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                    }
                    ChatActivityEnterView.this.recordingAudioVideo = false;
                    ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                    chatActivityEnterView2.messageTransitionIsRunning = false;
                    AndroidUtilities.runOnUIThread(chatActivityEnterView2.moveToSendStateRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$20$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatActivityEnterView.20.this.lambda$onTouchEvent$2();
                        }
                    }, 200L);
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
            if (motionEvent.getAction() != 1 && motionEvent.getAction() != 3) {
                if (motionEvent.getAction() == 2 && ChatActivityEnterView.this.recordingAudioVideo) {
                    float x = motionEvent.getX();
                    float y = motionEvent.getY();
                    if (ChatActivityEnterView.this.recordCircle.isSendButtonVisible()) {
                        return false;
                    }
                    if (ChatActivityEnterView.this.recordCircle.setLockTranslation(y) == 2) {
                        ChatActivityEnterView.this.startLockTransition();
                        return false;
                    }
                    ChatActivityEnterView.this.recordCircle.setMovingCords(x, y);
                    if (ChatActivityEnterView.this.startedDraggingX == -1.0f) {
                        ChatActivityEnterView.this.startedDraggingX = x;
                        ChatActivityEnterView chatActivityEnterView3 = ChatActivityEnterView.this;
                        double measuredWidth = chatActivityEnterView3.sizeNotifierLayout.getMeasuredWidth();
                        Double.isNaN(measuredWidth);
                        chatActivityEnterView3.distCanMove = (float) (measuredWidth * 0.35d);
                        if (ChatActivityEnterView.this.distCanMove > AndroidUtilities.dp(140.0f)) {
                            ChatActivityEnterView.this.distCanMove = AndroidUtilities.dp(140.0f);
                        }
                    }
                    float x2 = (((x + ChatActivityEnterView.this.audioVideoButtonContainer.getX()) - ChatActivityEnterView.this.startedDraggingX) / ChatActivityEnterView.this.distCanMove) + 1.0f;
                    if (ChatActivityEnterView.this.startedDraggingX != -1.0f) {
                        float f = x2 <= 1.0f ? x2 < 0.0f ? 0.0f : x2 : 1.0f;
                        if (ChatActivityEnterView.this.slideText != null) {
                            ChatActivityEnterView.this.slideText.setSlideX(f);
                        }
                        ChatActivityEnterView.this.setSlideToCancelProgress(f);
                        x2 = f;
                    }
                    if (x2 == 0.0f) {
                        if (ChatActivityEnterView.this.hasRecordVideo && ChatActivityEnterView.this.isInVideoMode()) {
                            CameraController.getInstance().cancelOnInitRunnable(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = ChatActivityEnterView.this.delegate;
                            ChatActivityEnterView chatActivityEnterView4 = ChatActivityEnterView.this;
                            chatActivityEnterViewDelegate2.needStartRecordVideo(2, true, 0, chatActivityEnterView4.voiceOnce ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0, chatActivityEnterView4.effectId);
                            ChatActivityEnterView.this.sendButton.setEffect(ChatActivityEnterView.this.effectId = 0L);
                        } else {
                            ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                            MediaController.getInstance().stopRecording(0, false, 0, ChatActivityEnterView.this.voiceOnce);
                        }
                        ChatActivityEnterView.this.recordingAudioVideo = false;
                        ChatActivityEnterView.this.updateRecordInterface(5, true);
                    }
                }
                return true;
            }
            if (motionEvent.getAction() == 3 && ChatActivityEnterView.this.recordingAudioVideo) {
                if (ChatActivityEnterView.this.slideToCancelProgress < 0.7f) {
                    if (ChatActivityEnterView.this.hasRecordVideo && ChatActivityEnterView.this.isInVideoMode()) {
                        CameraController.getInstance().cancelOnInitRunnable(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate3 = ChatActivityEnterView.this.delegate;
                        ChatActivityEnterView chatActivityEnterView5 = ChatActivityEnterView.this;
                        chatActivityEnterViewDelegate3.needStartRecordVideo(2, true, 0, chatActivityEnterView5.voiceOnce ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0, chatActivityEnterView5.effectId);
                        ChatActivityEnterView.this.sendButton.setEffect(ChatActivityEnterView.this.effectId = 0L);
                    } else {
                        ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                        MediaController.getInstance().stopRecording(0, false, 0, ChatActivityEnterView.this.voiceOnce);
                    }
                    ChatActivityEnterView.this.millisecondsRecorded = 0L;
                    ChatActivityEnterView.this.recordingAudioVideo = false;
                    ChatActivityEnterView.this.updateRecordInterface(5, true);
                } else {
                    ChatActivityEnterView.this.sendButtonVisible = true;
                    ChatActivityEnterView.this.startLockTransition();
                }
                return false;
            }
            if ((ChatActivityEnterView.this.recordCircle != null && ChatActivityEnterView.this.recordCircle.isSendButtonVisible()) || ((frameLayout = ChatActivityEnterView.this.recordedAudioPanel) != null && frameLayout.getVisibility() == 0)) {
                if (ChatActivityEnterView.this.recordAudioVideoRunnableStarted) {
                    AndroidUtilities.cancelRunOnUIThread(ChatActivityEnterView.this.recordAudioVideoRunnable);
                }
                return false;
            }
            if ((((motionEvent.getX() + ChatActivityEnterView.this.audioVideoButtonContainer.getX()) - ChatActivityEnterView.this.startedDraggingX) / ChatActivityEnterView.this.distCanMove) + 1.0f < 0.45d) {
                if (ChatActivityEnterView.this.hasRecordVideo && ChatActivityEnterView.this.isInVideoMode()) {
                    CameraController.getInstance().cancelOnInitRunnable(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate4 = ChatActivityEnterView.this.delegate;
                    ChatActivityEnterView chatActivityEnterView6 = ChatActivityEnterView.this;
                    chatActivityEnterViewDelegate4.needStartRecordVideo(2, true, 0, chatActivityEnterView6.voiceOnce ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0, chatActivityEnterView6.effectId);
                    ChatActivityEnterView.this.sendButton.setEffect(ChatActivityEnterView.this.effectId = 0L);
                } else {
                    ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                    MediaController.getInstance().stopRecording(0, false, 0, ChatActivityEnterView.this.voiceOnce);
                }
                ChatActivityEnterView.this.millisecondsRecorded = 0L;
                ChatActivityEnterView.this.recordingAudioVideo = false;
                ChatActivityEnterView.this.updateRecordInterface(5, true);
            } else if (ChatActivityEnterView.this.recordAudioVideoRunnableStarted) {
                AndroidUtilities.cancelRunOnUIThread(ChatActivityEnterView.this.recordAudioVideoRunnable);
                if (ChatActivityEnterView.this.sendVoiceEnabled && ChatActivityEnterView.this.sendRoundEnabled) {
                    ChatActivityEnterView.this.delegate.onSwitchRecordMode(!ChatActivityEnterView.this.isInVideoMode());
                    ChatActivityEnterView.this.setRecordVideoButtonVisible(!r1.isInVideoMode(), true);
                } else {
                    ChatActivityEnterView.this.delegate.needShowMediaBanHint();
                }
                performHapticFeedback(3);
                sendAccessibilityEvent(1);
            } else if (!ChatActivityEnterView.this.hasRecordVideo || ChatActivityEnterView.this.calledRecordRunnable) {
                ChatActivityEnterView.this.startedDraggingX = -1.0f;
                if (ChatActivityEnterView.this.hasRecordVideo && ChatActivityEnterView.this.isInVideoMode()) {
                    CameraController.getInstance().cancelOnInitRunnable(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate5 = ChatActivityEnterView.this.delegate;
                    ChatActivityEnterView chatActivityEnterView7 = ChatActivityEnterView.this;
                    chatActivityEnterViewDelegate5.needStartRecordVideo(1, true, 0, chatActivityEnterView7.voiceOnce ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0, chatActivityEnterView7.effectId);
                    ChatActivityEnterView.this.sendButton.setEffect(ChatActivityEnterView.this.effectId = 0L);
                } else if (ChatActivityEnterView.this.sendVoiceEnabled) {
                    if (ChatActivityEnterView.this.recordingAudioVideo && ChatActivityEnterView.this.isInScheduleMode()) {
                        AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$20$$ExternalSyntheticLambda3
                            @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                            public final void didSelectDate(boolean z, int i) {
                                ChatActivityEnterView.20.lambda$onTouchEvent$3(z, i);
                            }
                        }, new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$20$$ExternalSyntheticLambda4
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.20.lambda$onTouchEvent$4();
                            }
                        }, this.val$resourcesProvider);
                    }
                    ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                    MediaController.getInstance().stopRecording(ChatActivityEnterView.this.isInScheduleMode() ? 3 : 1, true, 0, ChatActivityEnterView.this.voiceOnce);
                } else {
                    ChatActivityEnterView.this.delegate.needShowMediaBanHint();
                }
                ChatActivityEnterView.this.recordingAudioVideo = false;
                ChatActivityEnterView chatActivityEnterView8 = ChatActivityEnterView.this;
                chatActivityEnterView8.messageTransitionIsRunning = false;
                AndroidUtilities.runOnUIThread(chatActivityEnterView8.moveToSendStateRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$20$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActivityEnterView.20.this.lambda$onTouchEvent$5();
                    }
                }, ChatActivityEnterView.this.shouldDrawBackground ? 500L : 0L);
            }
            return true;
        }

        @Override // android.view.View
        public void setVisibility(int i) {
            super.setVisibility(i);
        }
    }

    class 23 extends ImageView {
        23(Context context) {
            super(context);
        }

        @Override // android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            final ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            post(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$23$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.access$9900(ChatActivityEnterView.this);
                }
            });
        }
    }

    class 30 implements ViewTreeObserver.OnDrawListener {
        final /* synthetic */ SimpleAvatarView val$avatar;
        final /* synthetic */ SenderSelectPopup.SenderView val$senderView;

        30(SimpleAvatarView simpleAvatarView, SenderSelectPopup.SenderView senderView) {
            this.val$avatar = simpleAvatarView;
            this.val$senderView = senderView;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDraw$0(SimpleAvatarView simpleAvatarView, SenderSelectPopup.SenderView senderView) {
            simpleAvatarView.getViewTreeObserver().removeOnDrawListener(this);
            senderView.avatar.setHideAvatar(true);
        }

        @Override // android.view.ViewTreeObserver.OnDrawListener
        public void onDraw() {
            final SimpleAvatarView simpleAvatarView = this.val$avatar;
            final SenderSelectPopup.SenderView senderView = this.val$senderView;
            simpleAvatarView.post(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$30$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.30.this.lambda$onDraw$0(simpleAvatarView, senderView);
                }
            });
        }
    }

    class 40 implements RecyclerListView.OnItemClickListener {
        40() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onItemClick$0(String str, boolean z, int i) {
            SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(str, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, false, null, null, null, z, i, null, false));
            ChatActivityEnterView.this.setFieldText("");
            ChatActivityEnterView.this.botCommandsMenuContainer.dismiss();
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
        public void onItemClick(View view, int i) {
            if (view instanceof BotCommandsMenuView.BotCommandView) {
                final String command = ((BotCommandsMenuView.BotCommandView) view).getCommand();
                if (TextUtils.isEmpty(command)) {
                    return;
                }
                if (ChatActivityEnterView.this.isInScheduleMode()) {
                    AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.dialog_id, new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$40$$ExternalSyntheticLambda0
                        @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                        public final void didSelectDate(boolean z, int i2) {
                            ChatActivityEnterView.40.this.lambda$onItemClick$0(command, z, i2);
                        }
                    }, ChatActivityEnterView.this.resourcesProvider);
                    return;
                }
                if (ChatActivityEnterView.this.parentFragment == null || !ChatActivityEnterView.this.parentFragment.checkSlowMode(view)) {
                    SendMessagesHelper.SendMessageParams of = SendMessagesHelper.SendMessageParams.of(command, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, false, null, null, null, true, 0, null, false);
                    of.quick_reply_shortcut = ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.quickReplyShortcut : null;
                    of.quick_reply_shortcut_id = ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.getQuickReplyId() : 0;
                    of.effect_id = ChatActivityEnterView.this.effectId;
                    SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendMessage(of);
                    ChatActivityEnterView.this.setFieldText("");
                    ChatActivityEnterView.this.botCommandsMenuContainer.dismiss();
                    ChatActivityEnterView.this.sendButton.setEffect(ChatActivityEnterView.this.effectId = 0L);
                }
            }
        }
    }

    class 45 implements TextWatcher {
        boolean heightShouldBeChanged;
        private boolean ignorePrevTextChange;
        private boolean nextChangeIsSend;
        private CharSequence prevText;
        private boolean processChange;

        45() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$afterTextChanged$0() {
            ChatActivityEnterView.this.showCaptionLimitBulletin();
        }

        /* JADX WARN: Removed duplicated region for block: B:44:0x0171  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x0180  */
        @Override // android.text.TextWatcher
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void afterTextChanged(Editable editable) {
            ChatActivityEnterView chatActivityEnterView;
            BotCommandsMenuContainer botCommandsMenuContainer;
            ChatActivityEnterView chatActivityEnterView2;
            int i;
            if (this.ignorePrevTextChange) {
                return;
            }
            boolean z = false;
            if (this.prevText != null) {
                this.ignorePrevTextChange = true;
                editable.replace(0, editable.length(), this.prevText);
                this.prevText = null;
                this.ignorePrevTextChange = false;
                return;
            }
            if (ChatActivityEnterView.this.innerTextChange == 0) {
                if (this.nextChangeIsSend) {
                    ChatActivityEnterView.this.sendMessage();
                    this.nextChangeIsSend = false;
                }
                if (this.processChange) {
                    for (ImageSpan imageSpan : (ImageSpan[]) editable.getSpans(0, editable.length(), ImageSpan.class)) {
                        editable.removeSpan(imageSpan);
                    }
                    Emoji.replaceEmoji((CharSequence) editable, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt(), false, (int[]) null);
                    this.processChange = false;
                }
            }
            ChatActivityEnterView.this.codePointCount = Character.codePointCount(editable, 0, editable.length());
            if (ChatActivityEnterView.this.currentLimit > 0 && (i = ChatActivityEnterView.this.currentLimit - ChatActivityEnterView.this.codePointCount) <= 100) {
                if (i < -9999) {
                    i = -9999;
                }
                ChatActivityEnterView.this.createCaptionLimitView();
                ChatActivityEnterView.this.captionLimitView.setNumber(i, ChatActivityEnterView.this.captionLimitView.getVisibility() == 0);
                if (ChatActivityEnterView.this.captionLimitView.getVisibility() != 0) {
                    ChatActivityEnterView.this.captionLimitView.setVisibility(0);
                    ChatActivityEnterView.this.captionLimitView.setAlpha(0.0f);
                    ChatActivityEnterView.this.captionLimitView.setScaleX(0.5f);
                    ChatActivityEnterView.this.captionLimitView.setScaleY(0.5f);
                }
                ChatActivityEnterView.this.captionLimitView.animate().setListener(null).cancel();
                ChatActivityEnterView.this.captionLimitView.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(100L).start();
                if (i < 0) {
                    ChatActivityEnterView.this.captionLimitView.setTextColor(ChatActivityEnterView.this.getThemedColor(Theme.key_text_RedRegular));
                    chatActivityEnterView = ChatActivityEnterView.this;
                    if (chatActivityEnterView.doneButtonEnabled != z && chatActivityEnterView.doneButton != null) {
                        chatActivityEnterView2 = ChatActivityEnterView.this;
                        chatActivityEnterView2.doneButtonEnabled = z;
                        if (chatActivityEnterView2.doneButton != null) {
                            ChatActivityEnterView.this.doneButton.invalidate();
                        }
                    }
                    botCommandsMenuContainer = ChatActivityEnterView.this.botCommandsMenuContainer;
                    if (botCommandsMenuContainer != null) {
                        botCommandsMenuContainer.dismiss();
                    }
                    ChatActivityEnterView.this.checkBotMenu();
                    if (ChatActivityEnterView.this.editingCaption || ChatActivityEnterView.this.captionLimitBulletinShown || MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).premiumFeaturesBlocked() || UserConfig.getInstance(ChatActivityEnterView.this.currentAccount).isPremium() || ChatActivityEnterView.this.codePointCount <= MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).captionLengthLimitDefault || ChatActivityEnterView.this.codePointCount >= MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).captionLengthLimitPremium) {
                        return;
                    }
                    ChatActivityEnterView.this.captionLimitBulletinShown = true;
                    if (this.heightShouldBeChanged) {
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$45$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.45.this.lambda$afterTextChanged$0();
                            }
                        }, 300L);
                        return;
                    } else {
                        ChatActivityEnterView.this.showCaptionLimitBulletin();
                        return;
                    }
                }
                ChatActivityEnterView.this.captionLimitView.setTextColor(ChatActivityEnterView.this.getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
            } else if (ChatActivityEnterView.this.captionLimitView != null) {
                ChatActivityEnterView.this.captionLimitView.animate().alpha(0.0f).scaleX(0.5f).scaleY(0.5f).setDuration(100L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.45.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ChatActivityEnterView.this.captionLimitView.setVisibility(8);
                    }
                });
            }
            z = true;
            chatActivityEnterView = ChatActivityEnterView.this;
            if (chatActivityEnterView.doneButtonEnabled != z) {
                chatActivityEnterView2 = ChatActivityEnterView.this;
                chatActivityEnterView2.doneButtonEnabled = z;
                if (chatActivityEnterView2.doneButton != null) {
                }
            }
            botCommandsMenuContainer = ChatActivityEnterView.this.botCommandsMenuContainer;
            if (botCommandsMenuContainer != null) {
            }
            ChatActivityEnterView.this.checkBotMenu();
            if (ChatActivityEnterView.this.editingCaption) {
            }
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!this.ignorePrevTextChange && ChatActivityEnterView.this.recordingAudioVideo) {
                this.prevText = charSequence.toString();
            }
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (this.ignorePrevTextChange) {
                return;
            }
            boolean z = (ChatActivityEnterView.this.emojiView == null ? MessagesController.getGlobalEmojiSettings().getInt("selected_page", 0) : ChatActivityEnterView.this.emojiView.getCurrentPage()) != 0 && (ChatActivityEnterView.this.allowStickers || ChatActivityEnterView.this.allowGifs);
            if (((i2 == 0 && !TextUtils.isEmpty(charSequence)) || (i2 != 0 && TextUtils.isEmpty(charSequence))) && z) {
                ChatActivityEnterView.this.setEmojiButtonImage(false, true);
            }
            if (ChatActivityEnterView.this.lineCount != ChatActivityEnterView.this.messageEditText.getLineCount()) {
                this.heightShouldBeChanged = (ChatActivityEnterView.this.messageEditText.getLineCount() >= 4) != (ChatActivityEnterView.this.lineCount >= 4);
                if (!ChatActivityEnterView.this.isInitLineCount && ChatActivityEnterView.this.messageEditText.getMeasuredWidth() > 0) {
                    ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                    chatActivityEnterView.onLineCountChanged(chatActivityEnterView.lineCount, ChatActivityEnterView.this.messageEditText.getLineCount());
                }
                ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                chatActivityEnterView2.lineCount = chatActivityEnterView2.messageEditText.getLineCount();
            } else {
                this.heightShouldBeChanged = false;
            }
            if (ChatActivityEnterView.this.innerTextChange == 1) {
                return;
            }
            if (ChatActivityEnterView.this.sendByEnter) {
                ChatActivityEnterView chatActivityEnterView3 = ChatActivityEnterView.this;
                if (!chatActivityEnterView3.ctrlPressed && !chatActivityEnterView3.shiftPressed && !chatActivityEnterView3.ignoreTextChange && !ChatActivityEnterView.this.isPaste && ChatActivityEnterView.this.editingMessageObject == null && i3 > i2 && charSequence.length() > 0 && charSequence.length() == i + i3 && charSequence.charAt(charSequence.length() - 1) == '\n') {
                    this.nextChangeIsSend = true;
                }
            }
            ChatActivityEnterView.this.isPaste = false;
            ChatActivityEnterView.this.checkSendButton(true);
            CharSequence trimmedString = AndroidUtilities.getTrimmedString(charSequence.toString());
            if (ChatActivityEnterView.this.delegate != null && !ChatActivityEnterView.this.ignoreTextChange) {
                int i4 = i3 + 1;
                if (i2 > i4 || i3 - i2 > 2 || TextUtils.isEmpty(charSequence)) {
                    ChatActivityEnterView.this.messageWebPageSearch = true;
                }
                ChatActivityEnterView.this.delegate.onTextChanged(charSequence, i2 > i4 || i3 - i2 > 2, false);
            }
            if (ChatActivityEnterView.this.innerTextChange != 2 && i3 - i2 > 1) {
                this.processChange = true;
            }
            if (ChatActivityEnterView.this.editingMessageObject != null || ChatActivityEnterView.this.canWriteToChannel || trimmedString.length() == 0 || ChatActivityEnterView.this.lastTypingTimeSend >= System.currentTimeMillis() - 5000 || ChatActivityEnterView.this.ignoreTextChange) {
                return;
            }
            ChatActivityEnterView.this.lastTypingTimeSend = System.currentTimeMillis();
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.needSendTyping();
            }
        }
    }

    class 72 implements EmojiView.EmojiViewDelegate {
        72() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onClearEmojiRecent$3(AlertDialog alertDialog, int i) {
            ChatActivityEnterView.this.emojiView.clearRecentEmoji();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCustomEmojiSelected$0(String str, TLRPC.Document document, long j, boolean z) {
            EditTextCaption editTextCaption = ChatActivityEnterView.this.messageEditText;
            if (editTextCaption == null) {
                return;
            }
            int selectionEnd = editTextCaption.getSelectionEnd();
            if (selectionEnd < 0) {
                selectionEnd = 0;
            }
            try {
                try {
                    ChatActivityEnterView.this.innerTextChange = 2;
                    if (str == null) {
                        str = "😀";
                    }
                    SpannableString spannableString = new SpannableString(str);
                    AnimatedEmojiSpan animatedEmojiSpan = document != null ? new AnimatedEmojiSpan(document, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt()) : new AnimatedEmojiSpan(j, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt());
                    if (!z) {
                        animatedEmojiSpan.fromEmojiKeyboard = true;
                    }
                    animatedEmojiSpan.cacheType = AnimatedEmojiDrawable.getCacheTypeForEnterView();
                    spannableString.setSpan(animatedEmojiSpan, 0, spannableString.length(), 33);
                    EditTextCaption editTextCaption2 = ChatActivityEnterView.this.messageEditText;
                    editTextCaption2.setText(editTextCaption2.getText().insert(selectionEnd, spannableString));
                    ChatActivityEnterView.this.messageEditText.setSelection(spannableString.length() + selectionEnd, selectionEnd + spannableString.length());
                } catch (Exception e) {
                    FileLog.e(e);
                }
                ChatActivityEnterView.this.innerTextChange = 0;
            } catch (Throwable th) {
                ChatActivityEnterView.this.innerTextChange = 0;
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onGifSelected$2(Object obj, String str, boolean z, int i, Object obj2) {
            if (ChatActivityEnterView.this.stickersExpanded) {
                if (ChatActivityEnterView.this.searchingType != 0) {
                    ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                }
                ChatActivityEnterView.this.setStickersExpanded(false, true, false);
            }
            TL_stories.StoryItem replyToStory = ChatActivityEnterView.this.delegate != null ? ChatActivityEnterView.this.delegate.getReplyToStory() : null;
            if (obj instanceof TLRPC.Document) {
                TLRPC.Document document = (TLRPC.Document) obj;
                SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendSticker(document, str, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), replyToStory, ChatActivityEnterView.this.replyingQuote, null, z, i, false, obj2, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.quickReplyShortcut : null, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.getQuickReplyId() : 0);
                MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).addRecentGif(document, (int) (System.currentTimeMillis() / 1000), true);
                if (DialogObject.isEncryptedDialog(ChatActivityEnterView.this.dialog_id)) {
                    ChatActivityEnterView.this.accountInstance.getMessagesController().saveGif(obj2, document);
                }
            } else if (obj instanceof TLRPC.BotInlineResult) {
                TLRPC.BotInlineResult botInlineResult = (TLRPC.BotInlineResult) obj;
                if (botInlineResult.document != null) {
                    MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).addRecentGif(botInlineResult.document, (int) (System.currentTimeMillis() / 1000), false);
                    if (DialogObject.isEncryptedDialog(ChatActivityEnterView.this.dialog_id)) {
                        ChatActivityEnterView.this.accountInstance.getMessagesController().saveGif(obj2, botInlineResult.document);
                    }
                }
                HashMap hashMap = new HashMap();
                hashMap.put("id", botInlineResult.id);
                hashMap.put("query_id", "" + botInlineResult.query_id);
                hashMap.put("force_gif", "1");
                if (replyToStory == null) {
                    SendMessagesHelper.prepareSendingBotContextResult(ChatActivityEnterView.this.parentFragment, ChatActivityEnterView.this.accountInstance, botInlineResult, hashMap, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, ChatActivityEnterView.this.replyingQuote, z, i, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.quickReplyShortcut : null, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.getQuickReplyId() : 0);
                } else {
                    SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendSticker(botInlineResult.document, str, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), replyToStory, ChatActivityEnterView.this.replyingQuote, null, z, i, false, obj2, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.quickReplyShortcut : null, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.getQuickReplyId() : 0);
                }
                if (ChatActivityEnterView.this.searchingType != 0) {
                    ChatActivityEnterView.this.setSearchingTypeInternal(0, true);
                    ChatActivityEnterView.this.emojiView.closeSearch(true);
                    ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                }
            }
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onMessageSend(null, z, i);
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean canSchedule() {
            return ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.parentFragment.canScheduleMessage();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public long getDialogId() {
            return ChatActivityEnterView.this.dialog_id;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public float getProgressToSearchOpened() {
            return ChatActivityEnterView.this.searchToOpenProgress;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public int getThreadId() {
            return ChatActivityEnterView.this.getThreadMessageId();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void invalidateEnterView() {
            ChatActivityEnterView.this.invalidate();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean isExpanded() {
            return ChatActivityEnterView.this.stickersExpanded;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean isInScheduleMode() {
            return ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.parentFragment.isInScheduleMode();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean isSearchOpened() {
            return ChatActivityEnterView.this.searchingType != 0;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean isUserSelf() {
            return ChatActivityEnterView.this.dialog_id == UserConfig.getInstance(ChatActivityEnterView.this.currentAccount).getClientUserId();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onAnimatedEmojiUnlockClick() {
            BaseFragment baseFragment = ChatActivityEnterView.this.parentFragment;
            if (baseFragment == null) {
                baseFragment = LaunchActivity.getLastFragment();
            }
            PremiumFeatureBottomSheet premiumFeatureBottomSheet = new PremiumFeatureBottomSheet(baseFragment, 11, false);
            if (baseFragment != null) {
                baseFragment.showDialog(premiumFeatureBottomSheet);
            } else {
                premiumFeatureBottomSheet.show();
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean onBackspace() {
            EditTextCaption editTextCaption = ChatActivityEnterView.this.messageEditText;
            if (editTextCaption == null || editTextCaption.length() == 0) {
                return false;
            }
            ChatActivityEnterView.this.messageEditText.dispatchKeyEvent(new KeyEvent(0, 67));
            return true;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onClearEmojiRecent() {
            if (ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentActivity == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.resourcesProvider);
            builder.setTitle(LocaleController.getString(R.string.ClearRecentEmojiTitle));
            builder.setMessage(LocaleController.getString(R.string.ClearRecentEmojiText));
            builder.setPositiveButton(LocaleController.getString(R.string.ClearButton), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$72$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                public final void onClick(AlertDialog alertDialog, int i) {
                    ChatActivityEnterView.72.this.lambda$onClearEmojiRecent$3(alertDialog, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString(R.string.Cancel), null);
            ChatActivityEnterView.this.parentFragment.showDialog(builder.create());
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onCustomEmojiSelected(final long j, final TLRPC.Document document, final String str, final boolean z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$72$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.72.this.lambda$onCustomEmojiSelected$0(str, document, j, z);
                }
            });
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onEmojiSelected(String str) {
            EditTextCaption editTextCaption = ChatActivityEnterView.this.messageEditText;
            if (editTextCaption == null) {
                return;
            }
            int selectionEnd = editTextCaption.getSelectionEnd();
            if (selectionEnd < 0) {
                selectionEnd = 0;
            }
            try {
                try {
                    ChatActivityEnterView.this.innerTextChange = 2;
                    CharSequence replaceEmoji = Emoji.replaceEmoji((CharSequence) str, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt(), false, (int[]) null);
                    EditTextCaption editTextCaption2 = ChatActivityEnterView.this.messageEditText;
                    editTextCaption2.setText(editTextCaption2.getText().insert(selectionEnd, replaceEmoji));
                    int length = selectionEnd + replaceEmoji.length();
                    ChatActivityEnterView.this.messageEditText.setSelection(length, length);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            } finally {
                ChatActivityEnterView.this.innerTextChange = 0;
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onEmojiSettingsClick(ArrayList arrayList) {
            if (ChatActivityEnterView.this.parentFragment != null) {
                ChatActivityEnterView.this.parentFragment.presentFragment(new StickersActivity(5, arrayList));
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        /* renamed from: onGifSelected, reason: merged with bridge method [inline-methods] */
        public void lambda$onGifSelected$1(final View view, final Object obj, final String str, final Object obj2, final boolean z, final int i) {
            if (ChatActivityEnterView.this.replyingQuote != null && ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.replyingQuote.outdated) {
                ChatActivityEnterView.this.parentFragment.showQuoteMessageUpdate();
                return;
            }
            if (isInScheduleMode() && i == 0) {
                AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$72$$ExternalSyntheticLambda1
                    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                    public final void didSelectDate(boolean z2, int i2) {
                        ChatActivityEnterView.72.this.lambda$onGifSelected$1(view, obj, str, obj2, z2, i2);
                    }
                }, ChatActivityEnterView.this.resourcesProvider);
                return;
            }
            if (ChatActivityEnterView.this.slowModeTimer <= 0 || isInScheduleMode()) {
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$72$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActivityEnterView.72.this.lambda$onGifSelected$2(obj, str, z, i, obj2);
                    }
                };
                if (ChatActivityEnterView.this.showConfirmAlert(runnable)) {
                    return;
                }
                runnable.run();
                return;
            }
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = ChatActivityEnterView.this.delegate;
                if (view == null) {
                    view = ChatActivityEnterView.this.slowModeButton;
                }
                chatActivityEnterViewDelegate.onUpdateSlowModeButton(view, true, ChatActivityEnterView.this.slowModeButton.getText());
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onSearchOpenClose(int i) {
            ChatActivityEnterView.this.setSearchingTypeInternal(i, true);
            if (i != 0) {
                ChatActivityEnterView.this.setStickersExpanded(true, true, false, i == 1);
            }
            if (ChatActivityEnterView.this.emojiTabOpen && ChatActivityEnterView.this.searchingType == 2) {
                ChatActivityEnterView.this.checkStickresExpandHeight();
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onShowStickerSet(TLRPC.StickerSet stickerSet, TLRPC.InputStickerSet inputStickerSet, boolean z) {
            if (ChatActivityEnterView.this.trendingStickersAlert != null && !ChatActivityEnterView.this.trendingStickersAlert.isDismissed()) {
                ChatActivityEnterView.this.trendingStickersAlert.getLayout().showStickerSet(stickerSet, inputStickerSet);
                return;
            }
            BaseFragment baseFragment = ChatActivityEnterView.this.parentFragment;
            if (baseFragment == null) {
                baseFragment = LaunchActivity.getLastFragment();
            }
            if (baseFragment == null || ChatActivityEnterView.this.parentActivity == null) {
                return;
            }
            if (stickerSet != null) {
                inputStickerSet = new TLRPC.TL_inputStickerSetID();
                inputStickerSet.access_hash = stickerSet.access_hash;
                inputStickerSet.id = stickerSet.id;
            }
            Activity activity = ChatActivityEnterView.this.parentActivity;
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            BaseFragment baseFragment2 = baseFragment;
            StickersAlert stickersAlert = new StickersAlert(activity, baseFragment2, inputStickerSet, null, chatActivityEnterView, chatActivityEnterView.resourcesProvider, false);
            baseFragment.showDialog(stickersAlert);
            if (z) {
                stickersAlert.enableEditMode();
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickerSelected(View view, TLRPC.Document document, String str, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z, int i) {
            if (ChatActivityEnterView.this.trendingStickersAlert != null) {
                ChatActivityEnterView.this.trendingStickersAlert.lambda$new$0();
                ChatActivityEnterView.this.trendingStickersAlert = null;
            }
            if (ChatActivityEnterView.this.slowModeTimer > 0 && !isInScheduleMode()) {
                if (ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.onUpdateSlowModeButton(view != null ? view : ChatActivityEnterView.this.slowModeButton, true, ChatActivityEnterView.this.slowModeButton.getText());
                    return;
                }
                return;
            }
            if (ChatActivityEnterView.this.stickersExpanded) {
                if (ChatActivityEnterView.this.searchingType != 0) {
                    ChatActivityEnterView.this.setSearchingTypeInternal(0, true);
                    ChatActivityEnterView.this.emojiView.closeSearch(true, MessageObject.getStickerSetId(document));
                    ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                }
                ChatActivityEnterView.this.setStickersExpanded(false, true, false);
            }
            ChatActivityEnterView.this.lambda$onStickerSelected$68(document, str, obj, sendAnimationData, false, z, i);
            if (DialogObject.isEncryptedDialog(ChatActivityEnterView.this.dialog_id) && MessageObject.isGifDocument(document)) {
                ChatActivityEnterView.this.accountInstance.getMessagesController().saveGif(obj, document);
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickerSetAdd(TLRPC.StickerSetCovered stickerSetCovered) {
            MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).toggleStickerSet(ChatActivityEnterView.this.parentActivity, stickerSetCovered, 2, ChatActivityEnterView.this.parentFragment, false, false);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickerSetRemove(TLRPC.StickerSetCovered stickerSetCovered) {
            MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).toggleStickerSet(ChatActivityEnterView.this.parentActivity, stickerSetCovered, 0, ChatActivityEnterView.this.parentFragment, false, false);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickersGroupClick(long j) {
            if (ChatActivityEnterView.this.parentFragment != null) {
                if (AndroidUtilities.isTablet()) {
                    ChatActivityEnterView.this.hidePopup(false);
                }
                GroupStickersActivity groupStickersActivity = new GroupStickersActivity(j);
                groupStickersActivity.setInfo(ChatActivityEnterView.this.info);
                ChatActivityEnterView.this.parentFragment.presentFragment(groupStickersActivity);
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickersSettingsClick() {
            if (ChatActivityEnterView.this.parentFragment != null) {
                ChatActivityEnterView.this.parentFragment.presentFragment(new StickersActivity(0, null));
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onTabOpened(int i) {
            ChatActivityEnterView.this.delegate.onStickersTab(i == 3);
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            chatActivityEnterView.post(chatActivityEnterView.updateExpandabilityRunnable);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void showTrendingStickersAlert(TrendingStickersLayout trendingStickersLayout) {
            BaseFragment baseFragment = ChatActivityEnterView.this.parentFragment;
            if (baseFragment == null) {
                baseFragment = LaunchActivity.getLastFragment();
            }
            if (baseFragment != null) {
                ChatActivityEnterView.this.trendingStickersAlert = new TrendingStickersAlert(ChatActivityEnterView.this.getContext(), baseFragment, trendingStickersLayout, ChatActivityEnterView.this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatActivityEnterView.72.1
                    @Override // org.telegram.ui.Components.TrendingStickersAlert, org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface, org.telegram.ui.ActionBar.BaseFragment.AttachedSheet
                    /* renamed from: dismiss */
                    public void lambda$new$0() {
                        super.lambda$new$0();
                        if (ChatActivityEnterView.this.trendingStickersAlert == this) {
                            ChatActivityEnterView.this.trendingStickersAlert = null;
                        }
                        if (ChatActivityEnterView.this.delegate != null) {
                            ChatActivityEnterView.this.delegate.onTrendingStickersShowed(false);
                        }
                    }
                };
                if (ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.onTrendingStickersShowed(true);
                }
                baseFragment.showDialog(ChatActivityEnterView.this.trendingStickersAlert);
            }
        }
    }

    public enum BotMenuButtonType {
        NO_BUTTON,
        COMMANDS,
        WEB_VIEW
    }

    private static class BusinessLinkPresetMessage {
        public ArrayList entities;
        public String text;

        private BusinessLinkPresetMessage() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ChatActivityEditTextCaption extends EditTextCaption {
        CanvasButton canvasButton;

        public ChatActivityEditTextCaption(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
        }

        private void editPhoto(final Uri uri, String str) {
            final File generatePicturePath = AndroidUtilities.generatePicturePath(ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.parentFragment.isSecretChat(), MimeTypeMap.getSingleton().getExtensionFromMimeType(str));
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEditTextCaption$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.ChatActivityEditTextCaption.this.lambda$editPhoto$5(uri, generatePicturePath);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$editPhoto$5(Uri uri, final File file) {
            try {
                InputStream openInputStream = getContext().getContentResolver().openInputStream(uri);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = openInputStream.read(bArr);
                    if (read <= 0) {
                        openInputStream.close();
                        fileOutputStream.close();
                        MediaController.PhotoEntry photoEntry = new MediaController.PhotoEntry(0, -1, 0L, file.getAbsolutePath(), 0, false, 0, 0, 0L);
                        final ArrayList arrayList = new ArrayList();
                        arrayList.add(photoEntry);
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEditTextCaption$$ExternalSyntheticLambda5
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.ChatActivityEditTextCaption.this.lambda$editPhoto$4(arrayList, file);
                            }
                        });
                        return;
                    }
                    fileOutputStream.write(bArr, 0, read);
                    fileOutputStream.flush();
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onCreateInputConnection$1(final InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle) {
            if (BuildCompat.isAtLeastNMR1() && (i & 1) != 0) {
                try {
                    inputContentInfoCompat.requestPermission();
                } catch (Exception unused) {
                    return false;
                }
            }
            if (!inputContentInfoCompat.getDescription().hasMimeType("image/gif") && !SendMessagesHelper.shouldSendWebPAsSticker(null, inputContentInfoCompat.getContentUri())) {
                editPhoto(inputContentInfoCompat.getContentUri(), inputContentInfoCompat.getDescription().getMimeType(0));
            } else if (ChatActivityEnterView.this.isInScheduleMode()) {
                AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEditTextCaption$$ExternalSyntheticLambda4
                    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                    public final void didSelectDate(boolean z, int i2) {
                        ChatActivityEnterView.ChatActivityEditTextCaption.this.lambda$onCreateInputConnection$0(inputContentInfoCompat, z, i2);
                    }
                }, ChatActivityEnterView.this.resourcesProvider);
            } else {
                lambda$onCreateInputConnection$0(inputContentInfoCompat, true, 0);
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$2() {
            ChatActivityEnterView.this.showRestrictedHint();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTouchEvent$3() {
            ChatActivityEnterView.this.waitingForKeyboardOpenAfterAnimation = false;
            ChatActivityEnterView.this.openKeyboardInternal();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: openPhotoViewerForEdit, reason: merged with bridge method [inline-methods] */
        public void lambda$editPhoto$4(final ArrayList arrayList, final File file) {
            if (ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentFragment.getParentActivity() == null) {
                return;
            }
            final MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) arrayList.get(0);
            if (ChatActivityEnterView.this.keyboardVisible) {
                AndroidUtilities.hideKeyboard(this);
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEditTextCaption.1
                    @Override // java.lang.Runnable
                    public void run() {
                        ChatActivityEditTextCaption.this.lambda$editPhoto$4(arrayList, file);
                    }
                }, 100L);
            } else {
                PhotoViewer.getInstance().setParentActivity(ChatActivityEnterView.this.parentFragment, ChatActivityEnterView.this.resourcesProvider);
                PhotoViewer.getInstance().openPhotoForSelect(arrayList, 0, 2, false, new PhotoViewer.EmptyPhotoViewerProvider() { // from class: org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEditTextCaption.2
                    boolean sending;

                    @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
                    public boolean canCaptureMorePhotos() {
                        return false;
                    }

                    @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
                    public void sendButtonPressed(int i, VideoEditedInfo videoEditedInfo, boolean z, int i2, boolean z2) {
                        String str;
                        if (ChatActivityEnterView.this.replyingQuote != null && ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.replyingQuote.outdated) {
                            ChatActivityEnterView.this.parentFragment.showQuoteMessageUpdate();
                            return;
                        }
                        ArrayList arrayList2 = new ArrayList();
                        SendMessagesHelper.SendingMediaInfo sendingMediaInfo = new SendMessagesHelper.SendingMediaInfo();
                        MediaController.PhotoEntry photoEntry2 = photoEntry;
                        boolean z3 = photoEntry2.isVideo;
                        if ((!z3 && (str = photoEntry2.imagePath) != null) || (str = photoEntry2.path) != null) {
                            sendingMediaInfo.path = str;
                        }
                        sendingMediaInfo.thumbPath = photoEntry2.thumbPath;
                        sendingMediaInfo.isVideo = z3;
                        CharSequence charSequence = photoEntry2.caption;
                        sendingMediaInfo.caption = charSequence != null ? charSequence.toString() : null;
                        MediaController.PhotoEntry photoEntry3 = photoEntry;
                        sendingMediaInfo.entities = photoEntry3.entities;
                        sendingMediaInfo.masks = photoEntry3.stickers;
                        sendingMediaInfo.ttl = photoEntry3.ttl;
                        sendingMediaInfo.videoEditedInfo = videoEditedInfo;
                        sendingMediaInfo.canDeleteAfter = true;
                        arrayList2.add(sendingMediaInfo);
                        photoEntry.reset();
                        this.sending = true;
                        SendMessagesHelper.prepareSendingMedia(ChatActivityEnterView.this.accountInstance, arrayList2, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, ChatActivityEnterView.this.replyingQuote, false, false, ChatActivityEnterView.this.editingMessageObject, z, i2, ChatActivityEnterView.this.parentFragment == null ? 0 : ChatActivityEnterView.this.parentFragment.getChatMode(), SendMessagesHelper.checkUpdateStickersOrder(sendingMediaInfo.caption), null, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.quickReplyShortcut : null, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.getQuickReplyId() : 0, 0L, false);
                        if (ChatActivityEnterView.this.delegate != null) {
                            ChatActivityEnterView.this.delegate.onMessageSend(null, true, i2);
                        }
                    }

                    @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
                    public void willHidePhotoViewer() {
                        if (this.sending) {
                            return;
                        }
                        try {
                            file.delete();
                        } catch (Throwable unused) {
                        }
                    }
                }, ChatActivityEnterView.this.parentFragment);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: send, reason: merged with bridge method [inline-methods] */
        public void lambda$onCreateInputConnection$0(InputContentInfoCompat inputContentInfoCompat, boolean z, int i) {
            MessageSendPreview messageSendPreview = ChatActivityEnterView.this.messageSendPreview;
            if (messageSendPreview != null) {
                messageSendPreview.dismiss(true);
                ChatActivityEnterView.this.messageSendPreview = null;
            }
            if (ChatActivityEnterView.this.replyingQuote != null && ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.replyingQuote.outdated) {
                ChatActivityEnterView.this.parentFragment.showQuoteMessageUpdate();
                return;
            }
            if (inputContentInfoCompat.getDescription().hasMimeType("image/gif")) {
                SendMessagesHelper.prepareSendingDocument(ChatActivityEnterView.this.accountInstance, null, null, inputContentInfoCompat.getContentUri(), null, "image/gif", ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, ChatActivityEnterView.this.replyingQuote, null, z, 0, inputContentInfoCompat, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.quickReplyShortcut : null, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.getQuickReplyId() : 0, false);
            } else {
                SendMessagesHelper.prepareSendingPhoto(ChatActivityEnterView.this.accountInstance, null, inputContentInfoCompat.getContentUri(), ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), ChatActivityEnterView.this.replyingQuote, null, null, null, inputContentInfoCompat, 0, null, z, 0, ChatActivityEnterView.this.parentFragment == null ? 0 : ChatActivityEnterView.this.parentFragment.getChatMode(), ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.quickReplyShortcut : null, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.getQuickReplyId() : 0);
            }
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onMessageSend(null, true, i);
            }
        }

        @Override // android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (ChatActivityEnterView.this.preventInput) {
                return false;
            }
            return super.dispatchKeyEvent(keyEvent);
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor
        protected void extendActionMode(ActionMode actionMode, Menu menu) {
            if (ChatActivityEnterView.this.parentFragment != null) {
                ChatActivityEnterView.this.parentFragment.extendActionMode(menu);
            } else {
                ChatActivityEnterView.this.extendActionMode(menu);
            }
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor
        protected Theme.ResourcesProvider getResourcesProvider() {
            return ChatActivityEnterView.this.resourcesProvider;
        }

        @Override // org.telegram.ui.Components.EditTextCaption
        protected void onContextMenuClose() {
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onContextMenuClose();
            }
        }

        @Override // org.telegram.ui.Components.EditTextCaption
        protected void onContextMenuOpen() {
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onContextMenuOpen();
            }
        }

        @Override // android.widget.TextView, android.view.View
        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
            if (onCreateInputConnection == null) {
                return null;
            }
            try {
                if (ChatActivityEnterView.this.isEditingBusinessLink()) {
                    EditorInfoCompat.setContentMimeTypes(editorInfo, null);
                } else {
                    EditorInfoCompat.setContentMimeTypes(editorInfo, new String[]{"image/gif", "image/*", "image/jpg", "image/png", "image/webp"});
                }
                return InputConnectionCompat.createWrapper(onCreateInputConnection, editorInfo, new InputConnectionCompat.OnCommitContentListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEditTextCaption$$ExternalSyntheticLambda0
                    @Override // androidx.core.view.inputmethod.InputConnectionCompat.OnCommitContentListener
                    public final boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle) {
                        boolean lambda$onCreateInputConnection$1;
                        lambda$onCreateInputConnection$1 = ChatActivityEnterView.ChatActivityEditTextCaption.this.lambda$onCreateInputConnection$1(inputContentInfoCompat, i, bundle);
                        return lambda$onCreateInputConnection$1;
                    }
                });
            } catch (Throwable th) {
                FileLog.e(th);
                return onCreateInputConnection;
            }
        }

        @Override // org.telegram.ui.Components.EditTextCaption, org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        protected void onMeasure(int i, int i2) {
            ChatActivityEnterView.this.isInitLineCount = getMeasuredWidth() == 0 && getMeasuredHeight() == 0;
            super.onMeasure(i, i2);
            if (ChatActivityEnterView.this.isInitLineCount) {
                ChatActivityEnterView.this.lineCount = getLineCount();
            }
            ChatActivityEnterView.this.isInitLineCount = false;
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        protected void onScrollChanged(int i, int i2, int i3, int i4) {
            super.onScrollChanged(i, i2, i3, i4);
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onEditTextScroll();
            }
        }

        @Override // org.telegram.ui.Components.EditTextEffects, android.widget.TextView
        protected void onSelectionChanged(int i, int i2) {
            super.onSelectionChanged(i, i2);
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onTextSelectionChanged(i, i2);
            }
        }

        @Override // org.telegram.ui.Components.EditTextCaption, android.widget.EditText, android.widget.TextView
        public boolean onTextContextMenuItem(int i) {
            if (i == 16908322) {
                ChatActivityEnterView.this.isPaste = true;
                ClipData primaryClip = ((ClipboardManager) getContext().getSystemService("clipboard")).getPrimaryClip();
                if (primaryClip != null && primaryClip.getItemCount() == 1 && primaryClip.getDescription().hasMimeType("image/*") && !ChatActivityEnterView.this.isEditingBusinessLink()) {
                    editPhoto(primaryClip.getItemAt(0).getUri(), primaryClip.getDescription().getMimeType(0));
                }
            }
            return super.onTextContextMenuItem(i);
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (!ChatActivityEnterView.this.stickersDragging && ChatActivityEnterView.this.stickersExpansionAnim == null) {
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                if (!chatActivityEnterView.sendPlainEnabled && !chatActivityEnterView.isEditingMessage()) {
                    if (this.canvasButton == null) {
                        CanvasButton canvasButton = new CanvasButton(this);
                        this.canvasButton = canvasButton;
                        canvasButton.setDelegate(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEditTextCaption$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.ChatActivityEditTextCaption.this.lambda$onTouchEvent$2();
                            }
                        });
                    }
                    this.canvasButton.setRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                    return this.canvasButton.checkTouchEvent(motionEvent);
                }
                if (ChatActivityEnterView.this.isPopupShowing() && motionEvent.getAction() == 0) {
                    if (ChatActivityEnterView.this.searchingType != 0) {
                        ChatActivityEnterView.this.setSearchingTypeInternal(0, false);
                        ChatActivityEnterView.this.emojiView.closeSearch(false);
                        requestFocus();
                    }
                    ChatActivityEnterView.this.showPopup(AndroidUtilities.usingHardwareInput ? 0 : 2, 0);
                    if (ChatActivityEnterView.this.stickersExpanded) {
                        ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                        ChatActivityEnterView.this.waitingForKeyboardOpenAfterAnimation = true;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEditTextCaption$$ExternalSyntheticLambda3
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.ChatActivityEditTextCaption.this.lambda$onTouchEvent$3();
                            }
                        }, 200L);
                    } else {
                        ChatActivityEnterView.this.openKeyboardInternal();
                    }
                    return true;
                }
                try {
                    return super.onTouchEvent(motionEvent);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            return false;
        }

        @Override // android.view.View
        public boolean requestFocus(int i, android.graphics.Rect rect) {
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            if (chatActivityEnterView.sendPlainEnabled || chatActivityEnterView.isEditingMessage()) {
                return super.requestFocus(i, rect);
            }
            return false;
        }

        @Override // android.view.View
        public boolean requestRectangleOnScreen(android.graphics.Rect rect) {
            rect.bottom += AndroidUtilities.dp(1000.0f);
            return super.requestRectangleOnScreen(rect);
        }

        @Override // org.telegram.ui.Components.EditTextEffects
        public void setOffsetY(float f) {
            super.setOffsetY(f);
            if (ChatActivityEnterView.this.sizeNotifierLayout.getForeground() != null) {
                ChatActivityEnterView.this.sizeNotifierLayout.invalidateDrawable(ChatActivityEnterView.this.sizeNotifierLayout.getForeground());
            }
        }
    }

    public interface ChatActivityEnterViewDelegate {

        public abstract /* synthetic */ class -CC {
            public static void $default$bottomPanelTranslationYChanged(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate, float f) {
            }

            public static boolean $default$checkCanRemoveRestrictionsByBoosts(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return false;
            }

            public static int $default$getContentViewHeight(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return 0;
            }

            public static ChatActivity.ReplyQuote $default$getReplyQuote(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return null;
            }

            public static TL_stories.StoryItem $default$getReplyToStory(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return null;
            }

            public static TLRPC.TL_channels_sendAsPeers $default$getSendAsPeers(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return null;
            }

            public static boolean $default$hasForwardingMessages(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return false;
            }

            public static boolean $default$hasScheduledMessages(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return true;
            }

            public static int $default$measureKeyboardHeight(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return 0;
            }

            public static void $default$onContextMenuClose(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }

            public static void $default$onContextMenuOpen(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }

            public static void $default$onEditTextScroll(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }

            public static void $default$onKeyboardRequested(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }

            public static void $default$onTrendingStickersShowed(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate, boolean z) {
            }

            public static boolean $default$onceVoiceAvailable(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return false;
            }

            public static void $default$openScheduledMessages(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }

            public static void $default$prepareMessageSending(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }

            public static void $default$scrollToSendingMessage(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }
        }

        void bottomPanelTranslationYChanged(float f);

        boolean checkCanRemoveRestrictionsByBoosts();

        void didPressAttachButton();

        int getContentViewHeight();

        ChatActivity.ReplyQuote getReplyQuote();

        TL_stories.StoryItem getReplyToStory();

        TLRPC.TL_channels_sendAsPeers getSendAsPeers();

        boolean hasForwardingMessages();

        boolean hasScheduledMessages();

        int measureKeyboardHeight();

        void needChangeVideoPreviewState(int i, float f);

        void needSendTyping();

        void needShowMediaBanHint();

        void needStartRecordAudio(int i);

        void needStartRecordVideo(int i, boolean z, int i2, int i3, long j);

        void onAttachButtonHidden();

        void onAttachButtonShow();

        void onAudioVideoInterfaceUpdated();

        void onContextMenuClose();

        void onContextMenuOpen();

        void onEditTextScroll();

        void onKeyboardRequested();

        void onMessageEditEnd(boolean z);

        void onMessageSend(CharSequence charSequence, boolean z, int i);

        void onPreAudioVideoRecord();

        void onSendLongClick();

        void onStickersExpandedChange();

        void onStickersTab(boolean z);

        void onSwitchRecordMode(boolean z);

        void onTextChanged(CharSequence charSequence, boolean z, boolean z2);

        void onTextSelectionChanged(int i, int i2);

        void onTextSpansChanged(CharSequence charSequence);

        void onTrendingStickersShowed(boolean z);

        void onUpdateSlowModeButton(View view, boolean z, CharSequence charSequence);

        void onWindowSizeChanged(int i);

        boolean onceVoiceAvailable();

        void openScheduledMessages();

        void prepareMessageSending();

        void scrollToSendingMessage();

        void toggleVideoRecordingPause();
    }

    public class ControlsView extends FrameLayout {
        private AnimatedFloat hidePauseT;
        private HintView2 hintView;
        private int lastSize;
        private long lastUpdateTime;
        Paint lockBackgroundPaint;
        Paint lockOutlinePaint;
        Paint lockPaint;
        private Drawable micDrawable;
        private boolean oncePressed;
        public final RectF onceRect;
        private Paint p;
        Path path;
        private final Path path2;
        private boolean pausePressed;
        private CaptionContainerView.PeriodDrawable periodDrawable;
        private final float[] radiiLeft;
        private final float[] radiiRight;
        private final RectF rectF;
        private Drawable tooltipBackground;
        private Drawable tooltipBackgroundArrow;
        private StaticLayout tooltipLayout;
        private String tooltipMessage;
        private TextPaint tooltipPaint;
        private float tooltipWidth;
        private Drawable vidDrawable;
        private VirtualViewHelper virtualViewHelper;

        private class VirtualViewHelper extends ExploreByTouchHelper {
            public VirtualViewHelper(View view) {
                super(view);
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected int getVirtualViewAt(float f, float f2) {
                if (ChatActivityEnterView.this.sendButtonVisible && ChatActivityEnterView.this.recordCircle != null && ChatActivityEnterView.this.pauseRect.contains(f, f2)) {
                    return 2;
                }
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                return (!chatActivityEnterView.onceVisible || chatActivityEnterView.recordCircle == null || ChatActivityEnterView.this.snapAnimationProgress <= 0.1f || !ControlsView.this.onceRect.contains(f, f2)) ? -1 : 4;
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected void getVisibleVirtualViews(List list) {
                if (ChatActivityEnterView.this.sendButtonVisible) {
                    list.add(2);
                }
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                if (!chatActivityEnterView.onceVisible || chatActivityEnterView.recordCircle == null || ChatActivityEnterView.this.snapAnimationProgress <= 0.1f) {
                    return;
                }
                list.add(4);
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
                return true;
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                int i2;
                if (i == 2) {
                    ChatActivityEnterView.this.rect.set((int) ChatActivityEnterView.this.pauseRect.left, (int) ChatActivityEnterView.this.pauseRect.top, (int) ChatActivityEnterView.this.pauseRect.right, (int) ChatActivityEnterView.this.pauseRect.bottom);
                    accessibilityNodeInfoCompat.setBoundsInParent(ChatActivityEnterView.this.rect);
                    i2 = ChatActivityEnterView.this.transformToSeekbar > 0.5f ? R.string.AccActionResume : R.string.AccActionPause;
                } else {
                    if (i != 4) {
                        return;
                    }
                    android.graphics.Rect rect = ChatActivityEnterView.this.rect;
                    RectF rectF = ControlsView.this.onceRect;
                    rect.set((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
                    accessibilityNodeInfoCompat.setBoundsInParent(ChatActivityEnterView.this.rect);
                    i2 = ChatActivityEnterView.this.voiceOnce ? R.string.AccActionOnceDeactivate : R.string.AccActionOnceActivate;
                }
                accessibilityNodeInfoCompat.setText(LocaleController.getString(i2));
            }
        }

        public ControlsView(Context context) {
            super(context);
            this.tooltipPaint = new TextPaint(1);
            this.lockBackgroundPaint = new Paint(1);
            this.lockPaint = new Paint(1);
            this.lockOutlinePaint = new Paint(1);
            this.path = new Path();
            this.p = new Paint(1);
            this.rectF = new RectF();
            this.onceRect = new RectF();
            this.path2 = new Path();
            this.radiiLeft = new float[]{r13, r13, 0.0f, 0.0f, 0.0f, 0.0f, r13, r13};
            this.radiiRight = new float[]{0.0f, 0.0f, r12, r12, r12, r12, 0.0f, 0.0f};
            this.hidePauseT = new AnimatedFloat(this, 0L, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
            VirtualViewHelper virtualViewHelper = new VirtualViewHelper(this);
            this.virtualViewHelper = virtualViewHelper;
            ViewCompat.setAccessibilityDelegate(this, virtualViewHelper);
            CaptionContainerView.PeriodDrawable periodDrawable = new CaptionContainerView.PeriodDrawable();
            this.periodDrawable = periodDrawable;
            periodDrawable.setCallback(this);
            this.periodDrawable.setValue(1, ChatActivityEnterView.this.voiceOnce, false);
            this.lockOutlinePaint.setStyle(Paint.Style.STROKE);
            this.lockOutlinePaint.setStrokeCap(Paint.Cap.ROUND);
            this.lockOutlinePaint.setStrokeWidth(AndroidUtilities.dpf2(1.7f));
            ChatActivityEnterView.this.lockShadowDrawable = getResources().getDrawable(R.drawable.lock_round_shadow);
            ChatActivityEnterView.this.lockShadowDrawable.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelVoiceLockShadow), PorterDuff.Mode.MULTIPLY));
            this.tooltipBackground = Theme.createRoundRectDrawable(AndroidUtilities.dp(5.0f), ChatActivityEnterView.this.getThemedColor(Theme.key_chat_gifSaveHintBackground));
            this.tooltipPaint.setTextSize(AndroidUtilities.dp(14.0f));
            this.tooltipBackgroundArrow = ContextCompat.getDrawable(context, R.drawable.tooltip_arrow);
            this.tooltipMessage = LocaleController.getString("SlideUpToLock", R.string.SlideUpToLock);
            float dp = AndroidUtilities.dp(3.0f);
            float dp2 = AndroidUtilities.dp(3.0f);
            this.micDrawable = getResources().getDrawable(R.drawable.input_mic).mutate();
            this.vidDrawable = getResources().getDrawable(R.drawable.input_video).mutate();
            setWillNotDraw(false);
            updateColors();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$hideHintView$1(HintView2 hintView2) {
            removeView(hintView2);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$showHintView$0(HintView2 hintView2) {
            removeView(hintView2);
            if (this.hintView == hintView2) {
                this.hintView = null;
            }
        }

        private void scale(RectF rectF, float f) {
            float centerX = rectF.centerX();
            float centerY = rectF.centerY();
            rectF.left = AndroidUtilities.lerp(centerX, rectF.left, f);
            rectF.right = AndroidUtilities.lerp(centerX, rectF.right, f);
            rectF.top = AndroidUtilities.lerp(centerY, rectF.top, f);
            rectF.bottom = AndroidUtilities.lerp(centerY, rectF.bottom, f);
        }

        @Override // android.view.ViewGroup, android.view.View
        protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
            return super.dispatchHoverEvent(motionEvent) || this.virtualViewHelper.dispatchHoverEvent(motionEvent);
        }

        public void hideHintView() {
            final HintView2 hintView2 = this.hintView;
            if (hintView2 != null) {
                hintView2.setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ControlsView$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActivityEnterView.ControlsView.this.lambda$hideHintView$1(hintView2);
                    }
                });
                hintView2.hide();
                this.hintView = null;
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float f;
            float dp;
            float dpf2;
            float f2;
            float f3;
            float f4;
            float f5;
            float f6;
            float f7;
            float f8;
            float f9;
            float max;
            float f10 = ChatActivityEnterView.this.scale <= 0.5f ? ChatActivityEnterView.this.scale / 0.5f : ChatActivityEnterView.this.scale <= 0.75f ? 1.0f - (((ChatActivityEnterView.this.scale - 0.5f) / 0.25f) * 0.1f) : (((ChatActivityEnterView.this.scale - 0.75f) / 0.25f) * 0.1f) + 0.9f;
            long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
            this.lastUpdateTime = System.currentTimeMillis();
            if (ChatActivityEnterView.this.lockAnimatedTranslation != 10000.0f) {
                f = Math.max(0, (int) (ChatActivityEnterView.this.startTranslation - ChatActivityEnterView.this.lockAnimatedTranslation));
                if (f > AndroidUtilities.dp(57.0f)) {
                    f = AndroidUtilities.dp(57.0f);
                }
            } else {
                f = 0.0f;
            }
            int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp2(26.0f);
            float dp2 = 1.0f - (f / AndroidUtilities.dp(57.0f));
            float measuredHeight = getMeasuredHeight() - AndroidUtilities.dp(194.0f);
            if (ChatActivityEnterView.this.sendButtonVisible) {
                float dp3 = AndroidUtilities.dp(36.0f);
                dp = (((AndroidUtilities.dp(60.0f) + measuredHeight) + (AndroidUtilities.dpf2(30.0f) * (1.0f - f10))) - f) + (AndroidUtilities.dpf2(14.0f) * dp2);
                dpf2 = (((dp3 / 2.0f) + dp) - AndroidUtilities.dpf2(8.0f)) + AndroidUtilities.dpf2(2.0f);
                AndroidUtilities.dpf2(16.0f);
                AndroidUtilities.dpf2(2.0f);
                f2 = (((1.0f - dp2) * 9.0f) * (1.0f - ChatActivityEnterView.this.snapAnimationProgress)) - ((ChatActivityEnterView.this.snapAnimationProgress * 15.0f) * (1.0f - (dp2 > 0.4f ? 1.0f : dp2 / 0.4f)));
                f4 = dp2;
                f3 = dp3;
            } else {
                float dp4 = AndroidUtilities.dp(36.0f) + ((int) (AndroidUtilities.dp(14.0f) * dp2));
                dp = (((AndroidUtilities.dp(60.0f) + measuredHeight) + ((int) (AndroidUtilities.dp(30.0f) * (1.0f - f10)))) - ((int) f)) + (ChatActivityEnterView.this.idleProgress * dp2 * (-AndroidUtilities.dp(8.0f)));
                dpf2 = (((dp4 / 2.0f) + dp) - AndroidUtilities.dpf2(8.0f)) + AndroidUtilities.dpf2(2.0f) + (AndroidUtilities.dpf2(2.0f) * dp2);
                AndroidUtilities.dpf2(16.0f);
                AndroidUtilities.dpf2(2.0f);
                AndroidUtilities.dpf2(2.0f);
                f2 = (1.0f - dp2) * 9.0f;
                ChatActivityEnterView.this.snapAnimationProgress = 0.0f;
                f3 = dp4;
                f4 = 0.0f;
            }
            float f11 = dp;
            float f12 = f2;
            if ((ChatActivityEnterView.this.showTooltip && System.currentTimeMillis() - ChatActivityEnterView.this.showTooltipStartTime > 200) || ChatActivityEnterView.this.tooltipAlpha != 0.0f) {
                if (dp2 < 0.8f || ChatActivityEnterView.this.sendButtonVisible || ChatActivityEnterView.this.exitTransition != 0.0f || ChatActivityEnterView.this.transformToSeekbar != 0.0f) {
                    ChatActivityEnterView.this.showTooltip = false;
                }
                if (!ChatActivityEnterView.this.showTooltip) {
                    ChatActivityEnterView.access$5324(ChatActivityEnterView.this, currentTimeMillis / 150.0f);
                    if (ChatActivityEnterView.this.tooltipAlpha < 0.0f) {
                        ChatActivityEnterView.this.tooltipAlpha = 0.0f;
                    }
                } else if (ChatActivityEnterView.this.tooltipAlpha != 1.0f) {
                    ChatActivityEnterView.access$5316(ChatActivityEnterView.this, currentTimeMillis / 150.0f);
                    if (ChatActivityEnterView.this.tooltipAlpha >= 1.0f) {
                        ChatActivityEnterView.this.tooltipAlpha = 1.0f;
                        SharedConfig.increaseLockRecordAudioVideoHintShowed();
                    }
                }
                int i = (int) (ChatActivityEnterView.this.tooltipAlpha * 255.0f);
                this.tooltipBackground.setAlpha(i);
                this.tooltipBackgroundArrow.setAlpha(i);
                this.tooltipPaint.setAlpha(i);
                if (this.tooltipLayout != null) {
                    canvas.save();
                    this.rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    canvas.translate((getMeasuredWidth() - this.tooltipWidth) - AndroidUtilities.dp(44.0f), AndroidUtilities.dpf2(16.0f) + measuredHeight);
                    this.tooltipBackground.setBounds(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(2.0f), (int) (this.tooltipWidth + AndroidUtilities.dp(36.0f)), (int) (this.tooltipLayout.getHeight() + AndroidUtilities.dpf2(4.0f)));
                    this.tooltipBackground.draw(canvas);
                    this.tooltipLayout.draw(canvas);
                    canvas.restore();
                    canvas.save();
                    canvas.translate(getMeasuredWidth() - AndroidUtilities.dp(26.0f), ((AndroidUtilities.dpf2(17.0f) + measuredHeight) + (this.tooltipLayout.getHeight() / 2.0f)) - (ChatActivityEnterView.this.idleProgress * AndroidUtilities.dpf2(3.0f)));
                    this.path.reset();
                    this.path.setLastPoint(-AndroidUtilities.dpf2(5.0f), AndroidUtilities.dpf2(4.0f));
                    this.path.lineTo(0.0f, 0.0f);
                    this.path.lineTo(AndroidUtilities.dpf2(5.0f), AndroidUtilities.dpf2(4.0f));
                    this.p.setColor(-1);
                    this.p.setAlpha(i);
                    this.p.setStyle(Paint.Style.STROKE);
                    this.p.setStrokeCap(Paint.Cap.ROUND);
                    this.p.setStrokeJoin(Paint.Join.ROUND);
                    this.p.setStrokeWidth(AndroidUtilities.dpf2(1.5f));
                    canvas.drawPath(this.path, this.p);
                    canvas.restore();
                    canvas.save();
                    Drawable drawable = this.tooltipBackgroundArrow;
                    drawable.setBounds(measuredWidth - (drawable.getIntrinsicWidth() / 2), (int) (this.tooltipLayout.getHeight() + measuredHeight + AndroidUtilities.dpf2(20.0f)), (this.tooltipBackgroundArrow.getIntrinsicWidth() / 2) + measuredWidth, ((int) (this.tooltipLayout.getHeight() + measuredHeight + AndroidUtilities.dpf2(20.0f))) + this.tooltipBackgroundArrow.getIntrinsicHeight());
                    this.tooltipBackgroundArrow.draw(canvas);
                    canvas.restore();
                }
            }
            float f13 = this.hidePauseT.set(ChatActivityEnterView.this.isInVideoMode && ChatActivityEnterView.this.millisecondsRecorded >= 59000);
            if (ChatActivityEnterView.this.transformToSeekbar != 0.0f && ChatActivityEnterView.this.recordedAudioBackground != null) {
                float f14 = ChatActivityEnterView.this.transformToSeekbar > 0.38f ? 1.0f : ChatActivityEnterView.this.transformToSeekbar / 0.38f;
                if (ChatActivityEnterView.this.transformToSeekbar > 0.63f) {
                    f5 = 0.0f;
                    max = 1.0f;
                } else {
                    f5 = 0.0f;
                    max = Math.max(0.0f, (ChatActivityEnterView.this.transformToSeekbar - 0.38f) / 0.25f);
                }
                CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_BOTH;
                float interpolation = cubicBezierInterpolator.getInterpolation(f14);
                cubicBezierInterpolator.getInterpolation(max);
                f6 = interpolation;
            } else if (ChatActivityEnterView.this.exitTransition != 0.0f) {
                float f15 = ChatActivityEnterView.this.exitTransition > 0.6f ? 1.0f : ChatActivityEnterView.this.exitTransition / 0.6f;
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                float max2 = chatActivityEnterView.messageTransitionIsRunning ? chatActivityEnterView.exitTransition : Math.max(0.0f, (chatActivityEnterView.exitTransition - 0.6f) / 0.4f);
                CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.EASE_BOTH;
                float interpolation2 = cubicBezierInterpolator2.getInterpolation(f15);
                f5 = cubicBezierInterpolator2.getInterpolation(max2);
                f6 = interpolation2;
            } else {
                f5 = 0.0f;
                f6 = 0.0f;
            }
            canvas.save();
            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatActivityEnterView.this.textFieldContainer.getMeasuredHeight());
            float f16 = 1.0f - ChatActivityEnterView.this.controlsScale != 0.0f ? 1.0f - ChatActivityEnterView.this.controlsScale : f5 != 0.0f ? f5 : 0.0f;
            if (ChatActivityEnterView.this.slideToCancelProgress < 0.7f || ChatActivityEnterView.this.canceledByGesture) {
                ChatActivityEnterView.this.showTooltip = false;
                if (ChatActivityEnterView.this.slideToCancelLockProgress != 0.0f) {
                    ChatActivityEnterView.access$6224(ChatActivityEnterView.this, 0.12f);
                    if (ChatActivityEnterView.this.slideToCancelLockProgress < 0.0f) {
                        ChatActivityEnterView.this.slideToCancelLockProgress = 0.0f;
                    }
                }
            } else if (ChatActivityEnterView.this.slideToCancelLockProgress != 1.0f) {
                ChatActivityEnterView.access$6216(ChatActivityEnterView.this, 0.12f);
                if (ChatActivityEnterView.this.slideToCancelLockProgress > 1.0f) {
                    ChatActivityEnterView.this.slideToCancelLockProgress = 1.0f;
                }
            }
            float dpf22 = AndroidUtilities.dpf2(72.0f);
            float dpf23 = (dpf22 * f16) + (AndroidUtilities.dpf2(24.0f) * f6 * (1.0f - f16)) + ((1.0f - ChatActivityEnterView.this.slideToCancelLockProgress) * dpf22);
            if (dpf23 <= dpf22) {
                dpf22 = dpf23;
            }
            float f17 = (1.0f - f13) * ChatActivityEnterView.this.controlsScale * (1.0f - f5) * ChatActivityEnterView.this.slideToCancelLockProgress;
            float f18 = measuredWidth;
            float f19 = dpf2 + dpf22;
            canvas.scale(f17, f17, f18, f19);
            float f20 = f6;
            float f21 = f11 + dpf22;
            this.rectF.set(f18 - AndroidUtilities.dpf2(18.0f), f21, f18 + AndroidUtilities.dpf2(18.0f), f21 + f3);
            ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
            ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
            canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
            ChatActivityEnterView.this.pauseRect.set(this.rectF);
            scale(ChatActivityEnterView.this.pauseRect, f17);
            float f22 = 1.0f - f4;
            this.rectF.set((f18 - AndroidUtilities.dpf2(6.0f)) - (AndroidUtilities.dpf2(2.0f) * f22), f19 - (AndroidUtilities.dpf2(2.0f) * f22), measuredWidth + AndroidUtilities.dp(6.0f) + (AndroidUtilities.dpf2(2.0f) * f22), f19 + AndroidUtilities.dp(12.0f) + (AndroidUtilities.dpf2(2.0f) * f22));
            RectF rectF = this.rectF;
            float f23 = rectF.bottom;
            float centerX = rectF.centerX();
            float centerY = this.rectF.centerY();
            canvas.save();
            float f24 = 1.0f - dp2;
            canvas.translate(0.0f, AndroidUtilities.dpf2(2.0f) * f24);
            canvas.rotate(f12, centerX, centerY);
            if (f4 != 1.0f) {
                RectF rectF2 = AndroidUtilities.rectTmp;
                rectF2.set(0.0f, 0.0f, AndroidUtilities.dpf2(8.0f), AndroidUtilities.dpf2(8.0f));
                canvas.save();
                canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), f23 + dpf22 + (AndroidUtilities.dpf2(2.0f) * f24));
                canvas.translate(f18 - AndroidUtilities.dpf2(4.0f), ((this.rectF.top - AndroidUtilities.dp(6.0f)) - AndroidUtilities.lerp(AndroidUtilities.dpf2(2.0f), AndroidUtilities.dpf2(1.5f) * (1.0f - ChatActivityEnterView.this.idleProgress), dp2)) + (AndroidUtilities.dpf2(12.0f) * f4) + (AndroidUtilities.dpf2(2.0f) * ChatActivityEnterView.this.snapAnimationProgress));
                if (f12 > 0.0f) {
                    canvas.rotate(f12, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                }
                f7 = f3;
                f8 = f4;
                canvas.drawLine(AndroidUtilities.dpf2(8.0f), AndroidUtilities.dpf2(4.0f), AndroidUtilities.dpf2(8.0f), (AndroidUtilities.dpf2(4.0f) * f22) + AndroidUtilities.dpf2(6.0f), this.lockOutlinePaint);
                canvas.drawArc(rectF2, 0.0f, -180.0f, false, this.lockOutlinePaint);
                float dpf24 = AndroidUtilities.dpf2(4.0f);
                float dpf25 = AndroidUtilities.dpf2(4.0f);
                float dpf26 = AndroidUtilities.dpf2(4.0f);
                ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                canvas.drawLine(0.0f, dpf24, 0.0f, dpf25 + (dpf26 * chatActivityEnterView2.idleProgress * dp2 * (!chatActivityEnterView2.sendButtonVisible ? 1 : 0)) + (AndroidUtilities.dpf2(4.0f) * ChatActivityEnterView.this.snapAnimationProgress * f24), this.lockOutlinePaint);
                canvas.restore();
            } else {
                f7 = f3;
                f8 = f4;
            }
            float clamp = Utilities.clamp(ChatActivityEnterView.this.transformToSeekbar * 2.0f, 1.0f, 0.0f);
            Drawable drawable2 = clamp > 0.0f ? ChatActivityEnterView.this.isInVideoMode ? this.vidDrawable : this.micDrawable : null;
            int alpha = this.lockPaint.getAlpha();
            this.lockPaint.setAlpha((int) (alpha * (1.0f - clamp)));
            if (f8 > 0.0f) {
                canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockBackgroundPaint);
                this.path2.rewind();
                RectF rectF3 = AndroidUtilities.rectTmp;
                rectF3.set(this.rectF);
                rectF3.right = this.rectF.centerX() - (AndroidUtilities.dp(1.66f) * f8);
                float[] fArr = this.radiiLeft;
                float lerp = AndroidUtilities.lerp(AndroidUtilities.dp(3.0f), AndroidUtilities.dp(1.5f), f8);
                fArr[7] = lerp;
                fArr[6] = lerp;
                fArr[1] = lerp;
                fArr[0] = lerp;
                float[] fArr2 = this.radiiLeft;
                float dp5 = AndroidUtilities.dp(1.5f) * f8;
                fArr2[5] = dp5;
                fArr2[4] = dp5;
                fArr2[3] = dp5;
                fArr2[2] = dp5;
                Path path = this.path2;
                float[] fArr3 = this.radiiLeft;
                Path.Direction direction = Path.Direction.CW;
                path.addRoundRect(rectF3, fArr3, direction);
                rectF3.set(this.rectF);
                rectF3.left = this.rectF.centerX() + (AndroidUtilities.dp(1.66f) * f8);
                float[] fArr4 = this.radiiRight;
                float lerp2 = AndroidUtilities.lerp(AndroidUtilities.dp(3.0f), AndroidUtilities.dp(1.5f), f8);
                fArr4[5] = lerp2;
                fArr4[4] = lerp2;
                fArr4[3] = lerp2;
                fArr4[2] = lerp2;
                float[] fArr5 = this.radiiRight;
                float dp6 = AndroidUtilities.dp(1.5f) * f8;
                fArr5[7] = dp6;
                fArr5[6] = dp6;
                fArr5[1] = dp6;
                fArr5[0] = dp6;
                this.path2.addRoundRect(rectF3, this.radiiRight, direction);
                canvas.drawPath(this.path2, this.lockPaint);
            } else {
                canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockPaint);
            }
            this.lockPaint.setAlpha(alpha);
            if (drawable2 != null) {
                android.graphics.Rect rect = AndroidUtilities.rectTmp2;
                f9 = f18;
                rect.set((int) (this.rectF.centerX() - ((drawable2.getIntrinsicWidth() / 2) * 0.9285f)), (int) (this.rectF.centerY() - ((drawable2.getIntrinsicHeight() / 2) * 0.9285f)), (int) (this.rectF.centerX() + ((drawable2.getIntrinsicWidth() / 2) * 0.9285f)), (int) (this.rectF.centerY() + ((drawable2.getIntrinsicHeight() / 2) * 0.9285f)));
                drawable2.setBounds(rect);
                drawable2.setAlpha((int) (clamp * 255.0f));
                drawable2.draw(canvas);
            } else {
                f9 = f18;
            }
            if (f8 != 1.0f) {
                canvas.drawCircle(centerX, centerY, AndroidUtilities.dpf2(2.0f) * f22, this.lockBackgroundPaint);
            }
            canvas.restore();
            canvas.restore();
            float lerp3 = AndroidUtilities.lerp(f11, getMeasuredHeight() - AndroidUtilities.dp(118.0f), Math.max(ChatActivityEnterView.this.exitTransition, Math.min(f20, ChatActivityEnterView.this.slideToCancelLockProgress))) + dpf22 + (AndroidUtilities.dp(38.0f) * f13);
            this.rectF.set(f9 - AndroidUtilities.dpf2(18.0f), lerp3, f9 + AndroidUtilities.dpf2(18.0f), lerp3 + f7);
            ChatActivityEnterView chatActivityEnterView3 = ChatActivityEnterView.this;
            chatActivityEnterView3.onceVisible = chatActivityEnterView3.delegate != null && ChatActivityEnterView.this.delegate.onceVoiceAvailable();
            if (ChatActivityEnterView.this.onceVisible) {
                float dpf27 = AndroidUtilities.dpf2(12.0f);
                RectF rectF4 = this.rectF;
                float f25 = rectF4.left;
                float dpf28 = (rectF4.top - AndroidUtilities.dpf2(36.0f)) - dpf27;
                RectF rectF5 = this.rectF;
                rectF4.set(f25, dpf28, rectF5.right, rectF5.top - dpf27);
                HintView2 hintView2 = this.hintView;
                if (hintView2 != null) {
                    hintView2.setJointPx(0.0f, this.rectF.centerY());
                    this.hintView.invalidate();
                }
                this.onceRect.set(this.rectF);
                canvas.save();
                float f26 = ChatActivityEnterView.this.controlsScale * (1.0f - ChatActivityEnterView.this.exitTransition) * ChatActivityEnterView.this.slideToCancelLockProgress * ChatActivityEnterView.this.snapAnimationProgress;
                canvas.scale(f26, f26, this.rectF.centerX(), this.rectF.centerY());
                ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
                ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
                canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
                CaptionContainerView.PeriodDrawable periodDrawable = this.periodDrawable;
                RectF rectF6 = this.rectF;
                periodDrawable.setBounds((int) rectF6.left, (int) rectF6.top, (int) rectF6.right, (int) rectF6.bottom);
                this.periodDrawable.draw(canvas);
                canvas.restore();
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int dp = AndroidUtilities.dp(254.0f);
            if (this.lastSize != size) {
                this.lastSize = size;
                StaticLayout staticLayout = new StaticLayout(this.tooltipMessage, this.tooltipPaint, AndroidUtilities.dp(220.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
                this.tooltipLayout = staticLayout;
                int lineCount = staticLayout.getLineCount();
                this.tooltipWidth = 0.0f;
                for (int i3 = 0; i3 < lineCount; i3++) {
                    float lineWidth = this.tooltipLayout.getLineWidth(i3);
                    if (lineWidth > this.tooltipWidth) {
                        this.tooltipWidth = lineWidth;
                    }
                }
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(dp, 1073741824));
        }

        @Override // android.view.View
        protected boolean onSetAlpha(int i) {
            return super.onSetAlpha(i);
        }

        /* JADX WARN: Code restructure failed: missing block: B:59:0x0127, code lost:
        
            if (r12.getAction() == 3) goto L52;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean onTouchEvent(MotionEvent motionEvent) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (motionEvent.getAction() == 0) {
                if (ChatActivityEnterView.this.sendButtonVisible) {
                    this.pausePressed = ChatActivityEnterView.this.pauseRect.contains(x, y);
                }
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                if (chatActivityEnterView.onceVisible && chatActivityEnterView.recordCircle != null && ChatActivityEnterView.this.snapAnimationProgress > 0.1f) {
                    this.oncePressed = this.onceRect.contains(x, y);
                }
            } else {
                if (motionEvent.getAction() == 1) {
                    if (this.pausePressed && ChatActivityEnterView.this.pauseRect.contains(x, y)) {
                        if (ChatActivityEnterView.this.isInVideoMode()) {
                            if (ChatActivityEnterView.this.slideText != null) {
                                ChatActivityEnterView.this.slideText.setEnabled(false);
                            }
                            ChatActivityEnterView.this.delegate.toggleVideoRecordingPause();
                        } else {
                            if (ChatActivityEnterView.this.sendButtonVisible) {
                                ChatActivityEnterView.this.calledRecordRunnable = true;
                            }
                            MediaController.getInstance().toggleRecordingPause(ChatActivityEnterView.this.voiceOnce);
                            ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                            if (ChatActivityEnterView.this.slideText != null) {
                                ChatActivityEnterView.this.slideText.setEnabled(false);
                            }
                        }
                    } else if (this.oncePressed && this.onceRect.contains(x, y)) {
                        ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                        boolean z = !chatActivityEnterView2.voiceOnce;
                        chatActivityEnterView2.voiceOnce = z;
                        this.periodDrawable.setValue(1, z, true);
                        MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).toggleDraftVoiceOnce(ChatActivityEnterView.this.dialog_id, (ChatActivityEnterView.this.parentFragment == null || !ChatActivityEnterView.this.parentFragment.isTopic) ? 0L : ChatActivityEnterView.this.parentFragment.getTopicId(), ChatActivityEnterView.this.voiceOnce);
                        if (ChatActivityEnterView.this.voiceOnce) {
                            showHintView();
                        } else {
                            hideHintView();
                        }
                        invalidate();
                    }
                    this.oncePressed = false;
                    this.pausePressed = false;
                    return true;
                }
                this.oncePressed = false;
                this.pausePressed = false;
            }
            return this.pausePressed || this.oncePressed;
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            super.setAlpha(f);
        }

        public void showHintView() {
            hideHintView();
            HintView2 hintView2 = new HintView2(getContext(), 2);
            this.hintView = hintView2;
            hintView2.setJoint(1.0f, 0.0f);
            this.hintView.setMultilineText(true);
            this.hintView.setText(AndroidUtilities.replaceTags(LocaleController.getString(ChatActivityEnterView.this.isInVideoMode ? ChatActivityEnterView.this.voiceOnce ? R.string.VideoSetOnceHintEnabled : R.string.VideoSetOnceHint : ChatActivityEnterView.this.voiceOnce ? R.string.VoiceSetOnceHintEnabled : R.string.VoiceSetOnceHint)));
            HintView2 hintView22 = this.hintView;
            hintView22.setMaxWidthPx(HintView2.cutInFancyHalf(hintView22.getText(), this.hintView.getTextPaint()));
            if (ChatActivityEnterView.this.voiceOnce) {
                this.hintView.setIcon(R.raw.fire_on);
            } else {
                MessagesController.getGlobalMainSettings().edit().putInt("voiceoncehint", MessagesController.getGlobalMainSettings().getInt("voiceoncehint", 0) + 1).apply();
            }
            addView(this.hintView, LayoutHelper.createFrame(-1, -1.0f, 119, 0.0f, 0.0f, 54.0f, 58.0f));
            final HintView2 hintView23 = this.hintView;
            hintView23.setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ControlsView$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.ControlsView.this.lambda$showHintView$0(hintView23);
                }
            });
            this.hintView.show();
        }

        public void showTooltipIfNeed() {
            if (SharedConfig.lockRecordAudioVideoHint < 3) {
                ChatActivityEnterView.this.showTooltip = true;
                ChatActivityEnterView.this.showTooltipStartTime = System.currentTimeMillis();
            }
        }

        public void updateColors() {
            CaptionContainerView.PeriodDrawable periodDrawable = this.periodDrawable;
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            int i = Theme.key_chat_messagePanelVoiceLock;
            periodDrawable.updateColors(chatActivityEnterView.getThemedColor(i), ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelVoiceBackground), -1);
            Paint paint = this.lockBackgroundPaint;
            ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
            int i2 = Theme.key_chat_messagePanelVoiceLockBackground;
            paint.setColor(chatActivityEnterView2.getThemedColor(i2));
            this.tooltipPaint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_gifSaveHintText));
            int dp = AndroidUtilities.dp(5.0f);
            ChatActivityEnterView chatActivityEnterView3 = ChatActivityEnterView.this;
            int i3 = Theme.key_chat_gifSaveHintBackground;
            this.tooltipBackground = Theme.createRoundRectDrawable(dp, chatActivityEnterView3.getThemedColor(i3));
            Drawable drawable = this.tooltipBackgroundArrow;
            int themedColor = ChatActivityEnterView.this.getThemedColor(i3);
            PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
            drawable.setColorFilter(new PorterDuffColorFilter(themedColor, mode));
            this.lockBackgroundPaint.setColor(ChatActivityEnterView.this.getThemedColor(i2));
            this.lockPaint.setColor(ChatActivityEnterView.this.getThemedColor(i));
            this.lockOutlinePaint.setColor(ChatActivityEnterView.this.getThemedColor(i));
            this.micDrawable.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(i), mode));
            this.vidDrawable.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(i), mode));
        }

        @Override // android.view.View
        protected boolean verifyDrawable(Drawable drawable) {
            return drawable == this.periodDrawable || super.verifyDrawable(drawable);
        }
    }

    public class RecordCircle extends View {
        private float amplitude;
        private float animateAmplitudeDiff;
        private float animateToAmplitude;
        BlobDrawable bigWaveDrawable;
        private float circleRadius;
        private float circleRadiusAmplitude;
        public float drawingCircleRadius;
        public float drawingCx;
        public float drawingCy;
        public float iconScale;
        boolean incIdle;
        private float lastMovingX;
        private float lastMovingY;
        private long lastUpdateTime;
        private int paintAlpha;
        public float progressToSeekbarStep3;
        private float progressToSendButton;
        RectF rectF;
        private boolean showWaves;
        public boolean skipDraw;
        BlobDrawable tinyWaveDrawable;
        private float touchSlop;
        private VirtualViewHelper virtualViewHelper;
        public boolean voiceEnterTransitionInProgress;
        private float wavesEnterAnimation;

        private class VirtualViewHelper extends ExploreByTouchHelper {
            private int[] coords;

            public VirtualViewHelper(View view) {
                super(view);
                this.coords = new int[2];
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected int getVirtualViewAt(float f, float f2) {
                if (!RecordCircle.this.isSendButtonVisible() || ChatActivityEnterView.this.recordCircle == null) {
                    return -1;
                }
                if (ChatActivityEnterView.this.sendRect.contains((int) f, (int) f2)) {
                    return 1;
                }
                if (ChatActivityEnterView.this.pauseRect.contains(f, f2)) {
                    return 2;
                }
                if (ChatActivityEnterView.this.slideText == null || ChatActivityEnterView.this.slideText.cancelRect == null) {
                    return -1;
                }
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(ChatActivityEnterView.this.slideText.cancelRect);
                ChatActivityEnterView.this.slideText.getLocationOnScreen(this.coords);
                int[] iArr = this.coords;
                rectF.offset(iArr[0], iArr[1]);
                ChatActivityEnterView.this.recordCircle.getLocationOnScreen(this.coords);
                int[] iArr2 = this.coords;
                rectF.offset(-iArr2[0], -iArr2[1]);
                return rectF.contains(f, f2) ? 3 : -1;
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected void getVisibleVirtualViews(List list) {
                if (RecordCircle.this.isSendButtonVisible()) {
                    list.add(1);
                    list.add(3);
                }
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
                return true;
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                int i2;
                String str;
                String string;
                if (i == 1) {
                    accessibilityNodeInfoCompat.setBoundsInParent(ChatActivityEnterView.this.sendRect);
                    i2 = R.string.Send;
                    str = "Send";
                } else {
                    if (i == 2) {
                        ChatActivityEnterView.this.rect.set((int) ChatActivityEnterView.this.pauseRect.left, (int) ChatActivityEnterView.this.pauseRect.top, (int) ChatActivityEnterView.this.pauseRect.right, (int) ChatActivityEnterView.this.pauseRect.bottom);
                        accessibilityNodeInfoCompat.setBoundsInParent(ChatActivityEnterView.this.rect);
                        string = LocaleController.getString(R.string.Stop);
                        accessibilityNodeInfoCompat.setText(string);
                    }
                    if (i != 3 || ChatActivityEnterView.this.recordCircle == null) {
                        return;
                    }
                    if (ChatActivityEnterView.this.slideText != null && ChatActivityEnterView.this.slideText.cancelRect != null) {
                        android.graphics.Rect rect = AndroidUtilities.rectTmp2;
                        rect.set(ChatActivityEnterView.this.slideText.cancelRect);
                        ChatActivityEnterView.this.slideText.getLocationOnScreen(this.coords);
                        int[] iArr = this.coords;
                        rect.offset(iArr[0], iArr[1]);
                        ChatActivityEnterView.this.recordCircle.getLocationOnScreen(this.coords);
                        int[] iArr2 = this.coords;
                        rect.offset(-iArr2[0], -iArr2[1]);
                        accessibilityNodeInfoCompat.setBoundsInParent(rect);
                    }
                    i2 = R.string.Cancel;
                    str = "Cancel";
                }
                string = LocaleController.getString(str, i2);
                accessibilityNodeInfoCompat.setText(string);
            }
        }

        public RecordCircle(Context context) {
            super(context);
            this.tinyWaveDrawable = new BlobDrawable(11, LiteMode.FLAGS_CHAT);
            this.bigWaveDrawable = new BlobDrawable(12, LiteMode.FLAGS_CHAT);
            this.circleRadius = AndroidUtilities.dpf2(41.0f);
            this.circleRadiusAmplitude = AndroidUtilities.dp(30.0f);
            this.rectF = new RectF();
            this.wavesEnterAnimation = 0.0f;
            this.showWaves = true;
            VirtualViewHelper virtualViewHelper = new VirtualViewHelper(this);
            this.virtualViewHelper = virtualViewHelper;
            ViewCompat.setAccessibilityDelegate(this, virtualViewHelper);
            this.tinyWaveDrawable.minRadius = AndroidUtilities.dp(47.0f);
            this.tinyWaveDrawable.maxRadius = AndroidUtilities.dp(55.0f);
            this.tinyWaveDrawable.generateBlob();
            this.bigWaveDrawable.minRadius = AndroidUtilities.dp(47.0f);
            this.bigWaveDrawable.maxRadius = AndroidUtilities.dp(55.0f);
            this.bigWaveDrawable.generateBlob();
            this.iconScale = 1.0f;
            float scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            this.touchSlop = scaledTouchSlop * scaledTouchSlop;
            updateColors();
        }

        private void checkDrawables() {
            if (ChatActivityEnterView.this.micDrawable != null) {
                return;
            }
            ChatActivityEnterView.this.micDrawable = getResources().getDrawable(R.drawable.input_mic_pressed).mutate();
            Drawable drawable = ChatActivityEnterView.this.micDrawable;
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            int i = Theme.key_chat_messagePanelVoicePressed;
            int themedColor = chatActivityEnterView.getThemedColor(i);
            PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
            drawable.setColorFilter(new PorterDuffColorFilter(themedColor, mode));
            ChatActivityEnterView.this.cameraDrawable = getResources().getDrawable(R.drawable.input_video_pressed).mutate();
            ChatActivityEnterView.this.cameraDrawable.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(i), mode));
            ChatActivityEnterView.this.sendDrawable = getResources().getDrawable(R.drawable.attach_send).mutate();
            ChatActivityEnterView.this.sendDrawable.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(i), mode));
            ChatActivityEnterView.this.micOutline = getResources().getDrawable(R.drawable.input_mic).mutate();
            Drawable drawable2 = ChatActivityEnterView.this.micOutline;
            ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
            int i2 = Theme.key_chat_messagePanelIcons;
            drawable2.setColorFilter(new PorterDuffColorFilter(chatActivityEnterView2.getThemedColor(i2), mode));
            ChatActivityEnterView.this.cameraOutline = getResources().getDrawable(R.drawable.input_video).mutate();
            ChatActivityEnterView.this.cameraOutline.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(i2), mode));
        }

        private void drawIconInternal(Canvas canvas, Drawable drawable, Drawable drawable2, float f, int i) {
            checkDrawables();
            if (f != 0.0f && f != 1.0f && drawable2 != null) {
                canvas.save();
                canvas.scale(f, f, drawable.getBounds().centerX(), drawable.getBounds().centerY());
                float f2 = i;
                drawable.setAlpha((int) (f2 * f));
                drawable.draw(canvas);
                canvas.restore();
                canvas.save();
                float f3 = 1.0f - f;
                canvas.scale(f3, f3, drawable.getBounds().centerX(), drawable.getBounds().centerY());
                drawable2.setAlpha((int) (f2 * f3));
                drawable2.draw(canvas);
                canvas.restore();
                return;
            }
            if (ChatActivityEnterView.this.canceledByGesture && ChatActivityEnterView.this.slideToCancelProgress == 1.0f) {
                ChatActivityEnterView.this.audioVideoSendButton.setAlpha(1.0f);
                setVisibility(8);
                return;
            }
            if (ChatActivityEnterView.this.canceledByGesture && ChatActivityEnterView.this.slideToCancelProgress < 1.0f) {
                Drawable drawable3 = ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.cameraOutline : ChatActivityEnterView.this.micOutline;
                drawable3.setBounds(drawable.getBounds());
                int i2 = (int) (ChatActivityEnterView.this.slideToCancelProgress >= 0.93f ? ((ChatActivityEnterView.this.slideToCancelProgress - 0.93f) / 0.07f) * 255.0f : 0.0f);
                drawable3.setAlpha(i2);
                drawable3.draw(canvas);
                drawable3.setAlpha(NotificationCenter.newLocationAvailable);
                i = NotificationCenter.newLocationAvailable - i2;
            } else if (ChatActivityEnterView.this.canceledByGesture) {
                return;
            }
            drawable.setAlpha(i);
            drawable.draw(canvas);
        }

        public void canceledByGesture() {
            ChatActivityEnterView.this.canceledByGesture = true;
        }

        @Override // android.view.View
        protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
            return super.dispatchHoverEvent(motionEvent) || this.virtualViewHelper.dispatchHoverEvent(motionEvent);
        }

        public void drawIcon(Canvas canvas, int i, int i2, float f) {
            Drawable drawable;
            checkDrawables();
            if (isSendButtonVisible()) {
                r1 = this.progressToSendButton != 1.0f ? ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.cameraDrawable : ChatActivityEnterView.this.micDrawable : null;
                drawable = ChatActivityEnterView.this.sendDrawable;
            } else {
                drawable = ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.cameraDrawable : ChatActivityEnterView.this.micDrawable;
            }
            Drawable drawable2 = drawable;
            Drawable drawable3 = r1;
            ChatActivityEnterView.this.sendRect.set(i - (drawable2.getIntrinsicWidth() / 2), i2 - (drawable2.getIntrinsicHeight() / 2), (drawable2.getIntrinsicWidth() / 2) + i, (drawable2.getIntrinsicHeight() / 2) + i2);
            drawable2.setBounds(ChatActivityEnterView.this.sendRect);
            if (drawable3 != null) {
                drawable3.setBounds(i - (drawable3.getIntrinsicWidth() / 2), i2 - (drawable3.getIntrinsicHeight() / 2), i + (drawable3.getIntrinsicWidth() / 2), i2 + (drawable3.getIntrinsicHeight() / 2));
            }
            drawIconInternal(canvas, drawable2, drawable3, this.progressToSendButton, (int) (f * 255.0f));
        }

        public void drawWaves(Canvas canvas, float f, float f2, float f3) {
            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.wavesEnterAnimation);
            float f4 = ChatActivityEnterView.this.slideToCancelProgress > 0.7f ? 1.0f : ChatActivityEnterView.this.slideToCancelProgress / 0.7f;
            canvas.save();
            float f5 = ChatActivityEnterView.this.scale * f4 * interpolation * (BlobDrawable.SCALE_BIG_MIN + (this.bigWaveDrawable.amplitude * 1.4f)) * f3;
            canvas.scale(f5, f5, f, f2);
            BlobDrawable blobDrawable = this.bigWaveDrawable;
            blobDrawable.draw(f, f2, canvas, blobDrawable.paint);
            canvas.restore();
            float f6 = ChatActivityEnterView.this.scale * f4 * interpolation * (BlobDrawable.SCALE_SMALL_MIN + (this.tinyWaveDrawable.amplitude * 1.4f)) * f3;
            canvas.save();
            canvas.scale(f6, f6, f, f2);
            BlobDrawable blobDrawable2 = this.tinyWaveDrawable;
            blobDrawable2.draw(f, f2, canvas, blobDrawable2.paint);
            canvas.restore();
        }

        public float getControlsScale() {
            return ChatActivityEnterView.this.controlsScale;
        }

        public float getScale() {
            return ChatActivityEnterView.this.scale;
        }

        public float getTransformToSeekbarProgressStep3() {
            return this.progressToSeekbarStep3;
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            ControlsView controlsView = ChatActivityEnterView.this.controlsView;
            if (controlsView != null) {
                controlsView.invalidate();
            }
        }

        public boolean isSendButtonVisible() {
            return ChatActivityEnterView.this.sendButtonVisible;
        }

        /* JADX WARN: Removed duplicated region for block: B:115:0x05ce  */
        /* JADX WARN: Removed duplicated region for block: B:127:0x038a  */
        /* JADX WARN: Removed duplicated region for block: B:65:0x0313  */
        /* JADX WARN: Removed duplicated region for block: B:68:0x0384  */
        /* JADX WARN: Removed duplicated region for block: B:82:0x03b3  */
        /* JADX WARN: Removed duplicated region for block: B:89:0x03cb  */
        /* JADX WARN: Removed duplicated region for block: B:92:0x0455  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            float f;
            float f2;
            float f3;
            float f4;
            Drawable drawable;
            ChatActivityEnterView chatActivityEnterView;
            float f5;
            Canvas canvas2;
            Drawable drawable2;
            Drawable drawable3;
            float f6;
            if (this.skipDraw) {
                return;
            }
            int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp2(26.0f);
            int dp = (int) (AndroidUtilities.dp(170.0f) + 0.0f);
            this.drawingCx = ChatActivityEnterView.this.slideDelta + measuredWidth;
            float f7 = dp;
            this.drawingCy = f7;
            float f8 = ChatActivityEnterView.this.scale <= 0.5f ? ChatActivityEnterView.this.scale / 0.5f : ChatActivityEnterView.this.scale <= 0.75f ? 1.0f - (((ChatActivityEnterView.this.scale - 0.5f) / 0.25f) * 0.1f) : (((ChatActivityEnterView.this.scale - 0.75f) / 0.25f) * 0.1f) + 0.9f;
            long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
            float f9 = this.animateToAmplitude;
            float f10 = this.amplitude;
            if (f9 != f10) {
                float f11 = this.animateAmplitudeDiff;
                float f12 = f10 + (currentTimeMillis * f11);
                this.amplitude = f12;
                if (f11 <= 0.0f ? f12 < f9 : f12 > f9) {
                    this.amplitude = f9;
                }
                invalidate();
            }
            float interpolation = (this.circleRadius + (this.circleRadiusAmplitude * this.amplitude)) * f8 * (ChatActivityEnterView.this.canceledByGesture ? CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0f - ChatActivityEnterView.this.slideToCancelProgress) * 0.7f : (ChatActivityEnterView.this.slideToCancelProgress * 0.3f) + 0.7f);
            this.progressToSeekbarStep3 = 0.0f;
            if (ChatActivityEnterView.this.transformToSeekbar == 0.0f || ChatActivityEnterView.this.recordedAudioBackground == null) {
                if (ChatActivityEnterView.this.exitTransition != 0.0f) {
                    float f13 = ChatActivityEnterView.this.exitTransition > 0.6f ? 1.0f : ChatActivityEnterView.this.exitTransition / 0.6f;
                    ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                    boolean z = chatActivityEnterView2.messageTransitionIsRunning;
                    float f14 = chatActivityEnterView2.exitTransition;
                    if (!z) {
                        f14 = Math.max(0.0f, (f14 - 0.6f) / 0.4f);
                    }
                    CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_BOTH;
                    f = cubicBezierInterpolator.getInterpolation(f13);
                    float interpolation2 = cubicBezierInterpolator.getInterpolation(f14);
                    interpolation = (interpolation + (AndroidUtilities.dp(16.0f) * f)) * (1.0f - interpolation2);
                    if (!LiteMode.isEnabled(LiteMode.FLAGS_CHAT) || ChatActivityEnterView.this.exitTransition <= 0.6f) {
                        f3 = interpolation2;
                        f2 = 1.0f;
                    } else {
                        f2 = Math.max(0.0f, 1.0f - ((ChatActivityEnterView.this.exitTransition - 0.6f) / 0.4f));
                        f3 = interpolation2;
                    }
                } else {
                    f = 0.0f;
                    f2 = 1.0f;
                    f3 = 0.0f;
                }
                f4 = 0.0f;
            } else {
                float f15 = ChatActivityEnterView.this.transformToSeekbar > 0.38f ? 1.0f : ChatActivityEnterView.this.transformToSeekbar / 0.38f;
                float max = ChatActivityEnterView.this.transformToSeekbar > 0.63f ? 1.0f : Math.max(0.0f, (ChatActivityEnterView.this.transformToSeekbar - 0.38f) / 0.25f);
                this.progressToSeekbarStep3 = Math.max(0.0f, ((ChatActivityEnterView.this.transformToSeekbar - 0.38f) - 0.25f) / 0.37f);
                CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.EASE_BOTH;
                f = cubicBezierInterpolator2.getInterpolation(f15);
                f4 = cubicBezierInterpolator2.getInterpolation(max);
                this.progressToSeekbarStep3 = cubicBezierInterpolator2.getInterpolation(this.progressToSeekbarStep3);
                float measuredHeight = ChatActivityEnterView.this.recordedAudioBackground.getMeasuredHeight() / 2.0f;
                interpolation = (((interpolation + (AndroidUtilities.dp(16.0f) * f)) - measuredHeight) * (1.0f - f4)) + measuredHeight;
                f2 = 1.0f;
                f3 = 0.0f;
            }
            if (ChatActivityEnterView.this.canceledByGesture && ChatActivityEnterView.this.slideToCancelProgress > 0.7f) {
                f2 *= 1.0f - ((ChatActivityEnterView.this.slideToCancelProgress - 0.7f) / 0.3f);
            }
            if (this.progressToSeekbarStep3 > 0.0f) {
                ChatActivityEnterView.this.paint.setColor(ColorUtils.blendARGB(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelVoiceBackground), ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordedVoiceBackground), this.progressToSeekbarStep3));
            } else {
                ChatActivityEnterView.this.paint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelVoiceBackground));
            }
            checkDrawables();
            Drawable drawable4 = null;
            if (isSendButtonVisible()) {
                float f16 = this.progressToSendButton;
                if (f16 != 1.0f) {
                    float f17 = f16 + (currentTimeMillis / 150.0f);
                    this.progressToSendButton = f17;
                    if (f17 > 1.0f) {
                        this.progressToSendButton = 1.0f;
                    }
                    drawable4 = ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.cameraDrawable : ChatActivityEnterView.this.micDrawable;
                }
                drawable = ChatActivityEnterView.this.sendDrawable;
            } else {
                drawable = ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.cameraDrawable : ChatActivityEnterView.this.micDrawable;
            }
            Drawable drawable5 = drawable4;
            Drawable drawable6 = drawable;
            float f18 = interpolation;
            float f19 = f2;
            ChatActivityEnterView.this.sendRect.set(measuredWidth - (drawable6.getIntrinsicWidth() / 2), dp - (drawable6.getIntrinsicHeight() / 2), measuredWidth + (drawable6.getIntrinsicWidth() / 2), dp + (drawable6.getIntrinsicHeight() / 2));
            drawable6.setBounds(ChatActivityEnterView.this.sendRect);
            if (drawable5 != null) {
                drawable5.setBounds(measuredWidth - (drawable5.getIntrinsicWidth() / 2), dp - (drawable5.getIntrinsicHeight() / 2), (drawable5.getIntrinsicWidth() / 2) + measuredWidth, dp + (drawable5.getIntrinsicHeight() / 2));
            }
            int i = 0;
            if (!this.incIdle) {
                chatActivityEnterView = ChatActivityEnterView.this;
                float f20 = chatActivityEnterView.idleProgress - 0.01f;
                chatActivityEnterView.idleProgress = f20;
                f5 = 0.0f;
                if (f20 < 0.0f) {
                    this.incIdle = true;
                    chatActivityEnterView.idleProgress = f5;
                }
                if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                }
                this.lastUpdateTime = System.currentTimeMillis();
                if (ChatActivityEnterView.this.slideToCancelProgress <= 0.7f) {
                }
                if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                    if (this.showWaves) {
                    }
                    if (!this.voiceEnterTransitionInProgress) {
                    }
                }
                canvas2 = canvas;
                if (!this.voiceEnterTransitionInProgress) {
                }
                drawable2 = drawable6;
                drawable3 = drawable5;
                f6 = f18;
                if (ChatActivityEnterView.this.scale != 1.0f) {
                }
                this.drawingCircleRadius = f6;
            }
            chatActivityEnterView = ChatActivityEnterView.this;
            float f21 = chatActivityEnterView.idleProgress + 0.01f;
            chatActivityEnterView.idleProgress = f21;
            f5 = 1.0f;
            if (f21 > 1.0f) {
                this.incIdle = false;
                chatActivityEnterView.idleProgress = f5;
            }
            if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                this.tinyWaveDrawable.minRadius = AndroidUtilities.dp(47.0f);
                this.tinyWaveDrawable.maxRadius = AndroidUtilities.dp(47.0f) + (AndroidUtilities.dp(15.0f) * BlobDrawable.FORM_SMALL_MAX);
                this.bigWaveDrawable.minRadius = AndroidUtilities.dp(50.0f);
                this.bigWaveDrawable.maxRadius = AndroidUtilities.dp(50.0f) + (AndroidUtilities.dp(12.0f) * BlobDrawable.FORM_BIG_MAX);
                this.bigWaveDrawable.updateAmplitude(currentTimeMillis);
                BlobDrawable blobDrawable = this.bigWaveDrawable;
                blobDrawable.update(blobDrawable.amplitude, 1.01f);
                this.tinyWaveDrawable.updateAmplitude(currentTimeMillis);
                BlobDrawable blobDrawable2 = this.tinyWaveDrawable;
                blobDrawable2.update(blobDrawable2.amplitude, 1.02f);
            }
            this.lastUpdateTime = System.currentTimeMillis();
            float f22 = ChatActivityEnterView.this.slideToCancelProgress <= 0.7f ? 1.0f : ChatActivityEnterView.this.slideToCancelProgress / 0.7f;
            if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT) && f4 != 1.0f && f3 < 0.4f && f22 > 0.0f && !ChatActivityEnterView.this.canceledByGesture) {
                if (this.showWaves) {
                    float f23 = this.wavesEnterAnimation;
                    if (f23 != 1.0f) {
                        float f24 = f23 + 0.04f;
                        this.wavesEnterAnimation = f24;
                        if (f24 > 1.0f) {
                            this.wavesEnterAnimation = 1.0f;
                        }
                    }
                }
                if (!this.voiceEnterTransitionInProgress) {
                    float interpolation3 = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.wavesEnterAnimation);
                    canvas.save();
                    float f25 = 1.0f - f;
                    float f26 = ChatActivityEnterView.this.scale * f25 * f22 * interpolation3 * (BlobDrawable.SCALE_BIG_MIN + (this.bigWaveDrawable.amplitude * 1.4f));
                    canvas2 = canvas;
                    canvas2.scale(f26, f26, ChatActivityEnterView.this.slideDelta + measuredWidth, f7);
                    this.bigWaveDrawable.draw(ChatActivityEnterView.this.slideDelta + measuredWidth, f7, canvas2, this.bigWaveDrawable.paint);
                    canvas.restore();
                    float f27 = ChatActivityEnterView.this.scale * f25 * f22 * interpolation3 * (BlobDrawable.SCALE_SMALL_MIN + (this.tinyWaveDrawable.amplitude * 1.4f));
                    canvas.save();
                    canvas2.scale(f27, f27, ChatActivityEnterView.this.slideDelta + measuredWidth, f7);
                    this.tinyWaveDrawable.draw(ChatActivityEnterView.this.slideDelta + measuredWidth, f7, canvas2, this.tinyWaveDrawable.paint);
                    canvas.restore();
                    if (!this.voiceEnterTransitionInProgress) {
                        ChatActivityEnterView.this.paint.setAlpha((int) (this.paintAlpha * f19));
                        if (ChatActivityEnterView.this.scale == 1.0f) {
                            if (ChatActivityEnterView.this.transformToSeekbar != 0.0f) {
                                if (ChatActivityEnterView.this.isInVideoMode || this.progressToSeekbarStep3 <= 0.0f || ChatActivityEnterView.this.recordedAudioBackground == null) {
                                    drawable2 = drawable6;
                                    drawable3 = drawable5;
                                    canvas2.drawCircle(ChatActivityEnterView.this.slideDelta + measuredWidth, f7, f18 * (1.0f - this.progressToSeekbarStep3), ChatActivityEnterView.this.paint);
                                } else {
                                    float f28 = f7 + f18;
                                    float f29 = f7 - f18;
                                    float f30 = ChatActivityEnterView.this.slideDelta + measuredWidth + f18;
                                    float f31 = (ChatActivityEnterView.this.slideDelta + measuredWidth) - f18;
                                    View view = ChatActivityEnterView.this.recordedAudioBackground;
                                    drawable3 = drawable5;
                                    int i2 = 0;
                                    for (View view2 = (View) view.getParent(); view2 != getParent(); view2 = (View) view2.getParent()) {
                                        i = (int) (i + view2.getY());
                                        i2 = (int) (i2 + view2.getX());
                                    }
                                    float f32 = i;
                                    float y = (view.getY() + f32) - getY();
                                    drawable2 = drawable6;
                                    float y2 = ((view.getY() + view.getMeasuredHeight()) + f32) - getY();
                                    float f33 = i2;
                                    float x = (((view.getX() + view.getMeasuredWidth()) + f33) - getX()) - ChatActivityEnterView.this.horizontalPadding;
                                    float x2 = ((view.getX() + f33) - getX()) + ChatActivityEnterView.this.horizontalPadding;
                                    float measuredHeight2 = ChatActivityEnterView.this.isInVideoMode() ? 0.0f : view.getMeasuredHeight() / 2.0f;
                                    float f34 = 1.0f - this.progressToSeekbarStep3;
                                    float f35 = measuredHeight2 + ((f18 - measuredHeight2) * f34);
                                    this.rectF.set(x2 + ((f31 - x2) * f34), y + ((f29 - y) * f34), x + ((f30 - x) * f34), y2 + ((f28 - y2) * f34));
                                    canvas2.drawRoundRect(this.rectF, f35, f35, ChatActivityEnterView.this.paint);
                                }
                                f6 = f18;
                            } else {
                                drawable2 = drawable6;
                                drawable3 = drawable5;
                                f6 = f18;
                                canvas2.drawCircle(ChatActivityEnterView.this.slideDelta + measuredWidth, f7, f6, ChatActivityEnterView.this.paint);
                            }
                            canvas.save();
                            canvas2.translate(ChatActivityEnterView.this.slideDelta, 0.0f);
                            drawIconInternal(canvas, drawable2, drawable3, this.progressToSendButton, (int) ((1.0f - f4) * (1.0f - f3) * 255.0f));
                            canvas.restore();
                            if (ChatActivityEnterView.this.scale != 1.0f) {
                                canvas2.drawCircle(measuredWidth + ChatActivityEnterView.this.slideDelta, f7, f6, ChatActivityEnterView.this.paint);
                                float f36 = ChatActivityEnterView.this.canceledByGesture ? 1.0f - ChatActivityEnterView.this.slideToCancelProgress : 1.0f;
                                canvas.save();
                                canvas2.translate(ChatActivityEnterView.this.slideDelta, 0.0f);
                                drawIconInternal(canvas, drawable2, drawable3, this.progressToSendButton, (int) (f36 * 255.0f));
                                canvas.restore();
                            }
                            this.drawingCircleRadius = f6;
                        }
                    }
                    drawable2 = drawable6;
                    drawable3 = drawable5;
                    f6 = f18;
                    if (ChatActivityEnterView.this.scale != 1.0f) {
                    }
                    this.drawingCircleRadius = f6;
                }
            }
            canvas2 = canvas;
            if (!this.voiceEnterTransitionInProgress) {
            }
            drawable2 = drawable6;
            drawable3 = drawable5;
            f6 = f18;
            if (ChatActivityEnterView.this.scale != 1.0f) {
            }
            this.drawingCircleRadius = f6;
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            View.MeasureSpec.getSize(i);
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(194.0f), 1073741824));
            float measuredWidth = getMeasuredWidth() * 0.35f;
            if (measuredWidth > AndroidUtilities.dp(140.0f)) {
                measuredWidth = AndroidUtilities.dp(140.0f);
            }
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            chatActivityEnterView.slideDelta = (int) ((-measuredWidth) * (1.0f - chatActivityEnterView.slideToCancelProgress));
        }

        public void resetLockTranslation(boolean z) {
            if (!z) {
                ChatActivityEnterView.this.sendButtonVisible = false;
                ChatActivityEnterView.this.lockAnimatedTranslation = -1.0f;
                ChatActivityEnterView.this.startTranslation = -1.0f;
                ChatActivityEnterView.this.slideToCancelProgress = 1.0f;
                ChatActivityEnterView.this.slideToCancelLockProgress = 1.0f;
                ChatActivityEnterView.this.snapAnimationProgress = 0.0f;
                ChatActivityEnterView.this.controlsScale = 0.0f;
            }
            invalidate();
            ChatActivityEnterView.this.transformToSeekbar = 0.0f;
            ChatActivityEnterView.this.isRecordingStateChanged();
            ChatActivityEnterView.this.exitTransition = 0.0f;
            this.iconScale = 1.0f;
            ChatActivityEnterView.this.scale = 0.0f;
            ChatActivityEnterView.this.tooltipAlpha = 0.0f;
            ChatActivityEnterView.this.showTooltip = false;
            this.progressToSendButton = 0.0f;
            ChatActivityEnterView.this.canceledByGesture = false;
            ControlsView controlsView = ChatActivityEnterView.this.controlsView;
            if (controlsView != null) {
                controlsView.invalidate();
            }
        }

        public void setAmplitude(double d) {
            this.bigWaveDrawable.setValue((float) (Math.min(1800.0d, d) / 1800.0d), true);
            this.tinyWaveDrawable.setValue((float) (Math.min(1800.0d, d) / 1800.0d), false);
            float min = (float) (Math.min(1800.0d, d) / 1800.0d);
            this.animateToAmplitude = min;
            this.animateAmplitudeDiff = (min - this.amplitude) / 375.0f;
            invalidate();
        }

        public void setControlsScale(float f) {
            ChatActivityEnterView.this.controlsScale = f;
            ControlsView controlsView = ChatActivityEnterView.this.controlsView;
            if (controlsView != null) {
                controlsView.invalidate();
            }
        }

        public int setLockTranslation(float f) {
            if (ChatActivityEnterView.this.sendButtonVisible) {
                return 2;
            }
            if (ChatActivityEnterView.this.lockAnimatedTranslation == -1.0f) {
                ChatActivityEnterView.this.startTranslation = f;
            }
            ChatActivityEnterView.this.lockAnimatedTranslation = f;
            invalidate();
            if (ChatActivityEnterView.this.canceledByGesture || ChatActivityEnterView.this.slideToCancelProgress < 0.7f || ChatActivityEnterView.this.startTranslation - ChatActivityEnterView.this.lockAnimatedTranslation < AndroidUtilities.dp(57.0f)) {
                return 1;
            }
            ChatActivityEnterView.this.sendButtonVisible = true;
            return 2;
        }

        public void setMovingCords(float f, float f2) {
            float f3 = f - this.lastMovingX;
            float f4 = f2 - this.lastMovingY;
            float f5 = (f3 * f3) + (f4 * f4);
            this.lastMovingY = f2;
            this.lastMovingX = f;
            if (ChatActivityEnterView.this.showTooltip && ChatActivityEnterView.this.tooltipAlpha == 0.0f && f5 > this.touchSlop) {
                ChatActivityEnterView.this.showTooltipStartTime = System.currentTimeMillis();
            }
        }

        public void setScale(float f) {
            ChatActivityEnterView.this.scale = f;
            invalidate();
        }

        public void setSendButtonInvisible() {
            ChatActivityEnterView.this.sendButtonVisible = false;
            invalidate();
            ControlsView controlsView = ChatActivityEnterView.this.controlsView;
            if (controlsView != null) {
                controlsView.invalidate();
            }
        }

        public void setTransformToSeekbar(float f) {
            ChatActivityEnterView.this.transformToSeekbar = f;
            invalidate();
        }

        @Override // android.view.View
        public void setTranslationY(float f) {
            super.setTranslationY(f);
        }

        public void showWaves(boolean z, boolean z2) {
            if (!z2) {
                this.wavesEnterAnimation = z ? 1.0f : 0.5f;
            }
            this.showWaves = z;
        }

        public void updateColors() {
            Paint paint = ChatActivityEnterView.this.paint;
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            int i = Theme.key_chat_messagePanelVoiceBackground;
            paint.setColor(chatActivityEnterView.getThemedColor(i));
            this.tinyWaveDrawable.paint.setColor(ColorUtils.setAlphaComponent(ChatActivityEnterView.this.getThemedColor(i), 38));
            this.bigWaveDrawable.paint.setColor(ColorUtils.setAlphaComponent(ChatActivityEnterView.this.getThemedColor(i), 76));
            this.paintAlpha = ChatActivityEnterView.this.paint.getAlpha();
        }
    }

    private class RecordDot extends View {
        private float alpha;
        boolean attachedToWindow;
        RLottieDrawable drawable;
        private boolean enterAnimation;
        private boolean isIncr;
        private long lastUpdateTime;
        boolean playing;

        public RecordDot(Context context) {
            super(context);
            int i = R.raw.chat_audio_record_delete_2;
            RLottieDrawable rLottieDrawable = new RLottieDrawable(i, "" + i, AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), false, null);
            this.drawable = rLottieDrawable;
            rLottieDrawable.setCurrentParentView(this);
            this.drawable.setInvalidateOnProgressSet(true);
            updateColors();
        }

        @Override // android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            this.attachedToWindow = true;
            if (this.playing) {
                this.drawable.start();
            }
            this.drawable.setMasterParent(this);
        }

        @Override // android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.attachedToWindow = false;
            this.drawable.stop();
            this.drawable.setMasterParent(null);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.playing) {
                this.drawable.setAlpha((int) (this.alpha * 255.0f));
            }
            ChatActivityEnterView.this.redDotPaint.setAlpha((int) (this.alpha * 255.0f));
            long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
            if (this.enterAnimation) {
                this.alpha = 1.0f;
            } else if (this.isIncr || this.playing) {
                float f = this.alpha + (currentTimeMillis / 600.0f);
                this.alpha = f;
                if (f >= 1.0f) {
                    this.alpha = 1.0f;
                    this.isIncr = false;
                }
            } else {
                float f2 = this.alpha - (currentTimeMillis / 600.0f);
                this.alpha = f2;
                if (f2 <= 0.0f) {
                    this.alpha = 0.0f;
                    this.isIncr = true;
                }
            }
            this.lastUpdateTime = System.currentTimeMillis();
            if (this.playing) {
                this.drawable.draw(canvas);
            }
            if (!this.playing || !this.drawable.hasBitmap()) {
                canvas.drawCircle(getMeasuredWidth() >> 1, getMeasuredHeight() >> 1, AndroidUtilities.dp(5.0f), ChatActivityEnterView.this.redDotPaint);
            }
            invalidate();
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            this.drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }

        public void playDeleteAnimation() {
            this.playing = true;
            this.drawable.setProgress(0.0f);
            if (this.attachedToWindow) {
                this.drawable.start();
            }
        }

        public void resetAlpha() {
            this.alpha = 1.0f;
            this.lastUpdateTime = System.currentTimeMillis();
            this.isIncr = false;
            this.playing = false;
            this.drawable.stop();
            invalidate();
        }

        public void updateColors() {
            int themedColor = ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordedVoiceDot);
            int themedColor2 = ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelBackground);
            ChatActivityEnterView.this.redDotPaint.setColor(themedColor);
            this.drawable.beginApplyLayerColors();
            this.drawable.setLayerColor("Cup Red.**", themedColor);
            this.drawable.setLayerColor("Box.**", themedColor);
            this.drawable.setLayerColor("Line 1.**", themedColor2);
            this.drawable.setLayerColor("Line 2.**", themedColor2);
            this.drawable.setLayerColor("Line 3.**", themedColor2);
            this.drawable.commitApplyLayerColors();
            if (ChatActivityEnterView.this.playPauseDrawable != null) {
                ChatActivityEnterView.this.playPauseDrawable.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordedVoicePlayPause));
            }
        }
    }

    private class ScrimDrawable extends Drawable {
        private Paint paint;

        public ScrimDrawable() {
            Paint paint = new Paint();
            this.paint = paint;
            paint.setColor(0);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (ChatActivityEnterView.this.emojiView == null) {
                return;
            }
            this.paint.setAlpha(Math.round(ChatActivityEnterView.this.stickersExpansionProgress * 102.0f));
            float width = ChatActivityEnterView.this.getWidth();
            float y = (ChatActivityEnterView.this.emojiView.getY() - ChatActivityEnterView.this.getHeight()) + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
            EditTextCaption editTextCaption = ChatActivityEnterView.this.messageEditText;
            canvas.drawRect(0.0f, 0.0f, width, y + (editTextCaption == null ? 0.0f : editTextCaption.getOffsetY()), this.paint);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }
    }

    protected class SeekBarWaveformView extends View {
        public SeekBarWaveformView(Context context) {
            super(context);
            ChatActivityEnterView.this.seekBarWaveform = new SeekBarWaveform(context);
            ChatActivityEnterView.this.seekBarWaveform.setDelegate(new SeekBar.SeekBarDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$SeekBarWaveformView$$ExternalSyntheticLambda0
                @Override // org.telegram.ui.Components.SeekBar.SeekBarDelegate
                public /* synthetic */ boolean isSeekBarDragAllowed() {
                    return SeekBar.SeekBarDelegate.-CC.$default$isSeekBarDragAllowed(this);
                }

                @Override // org.telegram.ui.Components.SeekBar.SeekBarDelegate
                public /* synthetic */ void onSeekBarContinuousDrag(float f) {
                    SeekBar.SeekBarDelegate.-CC.$default$onSeekBarContinuousDrag(this, f);
                }

                @Override // org.telegram.ui.Components.SeekBar.SeekBarDelegate
                public final void onSeekBarDrag(float f) {
                    ChatActivityEnterView.SeekBarWaveformView.this.lambda$new$0(f);
                }

                @Override // org.telegram.ui.Components.SeekBar.SeekBarDelegate
                public /* synthetic */ void onSeekBarPressed() {
                    SeekBar.SeekBarDelegate.-CC.$default$onSeekBarPressed(this);
                }

                @Override // org.telegram.ui.Components.SeekBar.SeekBarDelegate
                public /* synthetic */ void onSeekBarReleased() {
                    SeekBar.SeekBarDelegate.-CC.$default$onSeekBarReleased(this);
                }

                @Override // org.telegram.ui.Components.SeekBar.SeekBarDelegate
                public /* synthetic */ boolean reverseWaveform() {
                    return SeekBar.SeekBarDelegate.-CC.$default$reverseWaveform(this);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(float f) {
            if (ChatActivityEnterView.this.audioToSendMessageObject != null) {
                ChatActivityEnterView.this.audioToSendMessageObject.audioProgress = f;
                MediaController.getInstance().seekToProgress(ChatActivityEnterView.this.audioToSendMessageObject, f);
            }
        }

        public boolean isDragging() {
            return ChatActivityEnterView.this.seekBarWaveform.isDragging();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            SeekBarWaveform seekBarWaveform = ChatActivityEnterView.this.seekBarWaveform;
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            int i = Theme.key_chat_recordedVoiceProgress;
            seekBarWaveform.setColors(chatActivityEnterView.getThemedColor(i), ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordedVoiceProgressInner), ChatActivityEnterView.this.getThemedColor(i));
            ChatActivityEnterView.this.seekBarWaveform.draw(canvas, this);
        }

        @Override // android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            ChatActivityEnterView.this.seekBarWaveform.setSize((int) ((i3 - i) - (ChatActivityEnterView.this.horizontalPadding * 2.0f)), i4 - i2);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean onTouch = ChatActivityEnterView.this.seekBarWaveform.onTouch(motionEvent.getAction(), motionEvent.getX(), motionEvent.getY());
            if (onTouch) {
                if (motionEvent.getAction() == 0) {
                    ChatActivityEnterView.this.requestDisallowInterceptTouchEvent(true);
                }
                invalidate();
            }
            return onTouch || super.onTouchEvent(motionEvent);
        }

        public void setProgress(float f) {
            ChatActivityEnterView.this.seekBarWaveform.setProgress(f);
            invalidate();
        }

        public void setWaveform(byte[] bArr) {
            ChatActivityEnterView.this.seekBarWaveform.setWaveform(bArr);
            invalidate();
        }
    }

    public static class SendButton extends View {
        private final Paint backgroundPaint;
        private ValueAnimator bounceCountAnimator;
        public boolean center;
        private final AnimatedTextView.AnimatedTextDrawable count;
        private float countBounceScale;
        private final Paint countClearPaint;
        private final Drawable drawable;
        private int drawableColor;
        private final Drawable drawableInverse;
        private final AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable emojiDrawable;
        private final Drawable inactiveDrawable;
        private boolean infiniteLoading;
        private final AnimatedFloat loadingAnimatedProgress;
        private final AnimatedFloat loadingAnimatedShown;
        private final FastOutSlowInInterpolator loadingInterpolator;
        private final Paint loadingPaint;
        private float loadingProgress;
        private boolean loadingShown;
        public final AnimatedFloat open;
        private final Path path;
        public final int resId;
        public final Theme.ResourcesProvider resourcesProvider;

        public SendButton(Context context, int i, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            this.backgroundPaint = new Paint(1);
            Paint paint = new Paint(1);
            this.countClearPaint = paint;
            CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
            this.open = new AnimatedFloat(this, 0L, 420L, cubicBezierInterpolator);
            this.loadingInterpolator = new FastOutSlowInInterpolator();
            this.loadingAnimatedShown = new AnimatedFloat(this, 0L, 420L, cubicBezierInterpolator);
            this.loadingAnimatedProgress = new AnimatedFloat(this, 0L, 500L, cubicBezierInterpolator);
            this.path = new Path();
            Paint paint2 = new Paint(1);
            this.loadingPaint = paint2;
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = new AnimatedTextView.AnimatedTextDrawable(true, true, true);
            this.count = animatedTextDrawable;
            this.countBounceScale = 1.0f;
            this.resId = i;
            this.resourcesProvider = resourcesProvider;
            this.drawable = context.getResources().getDrawable(i).mutate();
            this.inactiveDrawable = context.getResources().getDrawable(i).mutate();
            this.drawableInverse = context.getResources().getDrawable(i).mutate();
            this.emojiDrawable = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, AndroidUtilities.dp(14.0f));
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setStrokeWidth(AndroidUtilities.dp(2.0f));
            paint2.setStrokeJoin(Paint.Join.ROUND);
            paint2.setStrokeCap(Paint.Cap.ROUND);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            animatedTextDrawable.setCallback(this);
            animatedTextDrawable.setTextColor(-1);
            animatedTextDrawable.setTextSize(AndroidUtilities.dp(14.0f));
            animatedTextDrawable.setTypeface(AndroidUtilities.getTypeface("fonts/num.otf"));
            animatedTextDrawable.setGravity(17);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$bounceCount$0(ValueAnimator valueAnimator) {
            this.countBounceScale = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        }

        private void updateColors(boolean z) {
            Paint paint;
            int alphaComponent;
            int i = Theme.key_chat_messagePanelSend;
            int color = Theme.getColor(i, this.resourcesProvider);
            if (color != this.drawableColor) {
                this.drawableColor = color;
                Drawable drawable = this.drawable;
                int color2 = Theme.getColor(i, this.resourcesProvider);
                PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
                drawable.setColorFilter(new PorterDuffColorFilter(color2, mode));
                int color3 = Theme.getColor(Theme.key_chat_messagePanelIcons, this.resourcesProvider);
                this.inactiveDrawable.setColorFilter(new PorterDuffColorFilter(Color.argb(NotificationCenter.updateBotMenuButton, Color.red(color3), Color.green(color3), Color.blue(color3)), mode));
                this.drawableInverse.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_messagePanelVoicePressed, this.resourcesProvider), mode));
            }
            if (shouldDrawBackground()) {
                paint = this.backgroundPaint;
                alphaComponent = getFillColor();
            } else {
                paint = this.backgroundPaint;
                alphaComponent = ColorUtils.setAlphaComponent(-1, 75);
            }
            paint.setColor(alphaComponent);
        }

        public void bounceCount() {
            ValueAnimator valueAnimator = this.bounceCountAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.9f, 1.0f);
            this.bounceCountAnimator = ofFloat;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$SendButton$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ChatActivityEnterView.SendButton.this.lambda$bounceCount$0(valueAnimator2);
                }
            });
            this.bounceCountAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.SendButton.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SendButton.this.countBounceScale = 1.0f;
                }
            });
            this.bounceCountAnimator.setDuration(180L);
            this.bounceCountAnimator.setInterpolator(new OvershootInterpolator());
            this.bounceCountAnimator.start();
        }

        public void copyCountTo(SendButton sendButton) {
            sendButton.count.setText(this.count.getText(), false);
            sendButton.countBounceScale = this.countBounceScale;
        }

        public void copyEmojiTo(SendButton sendButton) {
            sendButton.setEmoji(this.emojiDrawable.getDrawable());
        }

        public int getFillColor() {
            return Theme.getColor(Theme.key_chat_messagePanelSend, this.resourcesProvider);
        }

        @Override // android.view.View
        public void invalidate(int i, int i2, int i3, int i4) {
            super.invalidate(i, i2, i3, i4);
        }

        public boolean isInScheduleMode() {
            return false;
        }

        public abstract boolean isInactive();

        public abstract boolean isOpen();

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            int i;
            float f;
            float f2;
            float f3;
            Paint paint;
            Canvas canvas2;
            float currentTimeMillis;
            float f4;
            boolean z;
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), NotificationCenter.newLocationAvailable, 31);
            boolean isOpen = isOpen();
            updateColors(isOpen);
            Drawable drawable = isInactive() ? this.inactiveDrawable : this.drawable;
            int measuredWidth = (getMeasuredWidth() - drawable.getIntrinsicWidth()) / 2;
            int measuredHeight = (getMeasuredHeight() - drawable.getIntrinsicHeight()) / 2;
            if (!this.center) {
                if (isInScheduleMode()) {
                    measuredHeight -= AndroidUtilities.dp(1.0f);
                } else {
                    measuredWidth += AndroidUtilities.dp(2.0f);
                }
            }
            int i2 = measuredWidth;
            int i3 = measuredHeight;
            float f5 = this.loadingAnimatedShown.set(this.loadingShown);
            float f6 = this.open.set(isOpen);
            if (f6 < 1.0f) {
                drawable.setBounds(i2, i3, drawable.getIntrinsicWidth() + i2, drawable.getIntrinsicHeight() + i3);
                drawable.draw(canvas);
                int measuredWidth2 = (getMeasuredWidth() / 2) + AndroidUtilities.dp(12.0f);
                int measuredHeight2 = (getMeasuredHeight() / 2) + AndroidUtilities.dp(12.0f);
                int dp = AndroidUtilities.dp(8.0f);
                this.emojiDrawable.setBounds(measuredWidth2 - dp, measuredHeight2 - dp, measuredWidth2 + dp, measuredHeight2 + dp);
                this.emojiDrawable.setAlpha((int) ((1.0f - f6) * 255.0f));
                this.emojiDrawable.draw(canvas);
            }
            if (f6 > 0.0f) {
                int measuredWidth3 = getMeasuredWidth() / 2;
                int measuredHeight3 = getMeasuredHeight() / 2;
                canvas.save();
                this.path.rewind();
                float f7 = measuredWidth3;
                float f8 = measuredHeight3;
                this.path.addCircle(f7, f8, (measuredWidth3 - AndroidUtilities.dp(4.0f)) * f6, Path.Direction.CW);
                canvas.drawCircle(f7, f8, (measuredWidth3 - AndroidUtilities.dp(4.0f)) * f6, this.backgroundPaint);
                canvas.clipPath(this.path);
                if (f5 > 0.0f) {
                    this.loadingPaint.setColor(-1);
                    this.loadingPaint.setAlpha((int) (f5 * 255.0f));
                    float dp2 = AndroidUtilities.dp(8.66f);
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(f7 - dp2, f8 - dp2, f7 + dp2, dp2 + f8);
                    if (this.infiniteLoading) {
                        long currentTimeMillis2 = System.currentTimeMillis() % 5400;
                        float f9 = (1520 * currentTimeMillis2) / 5400.0f;
                        float max = Math.max(0.0f, f9 - 20.0f);
                        int i4 = 0;
                        while (i4 < 4) {
                            int i5 = i4 * 1350;
                            f9 += this.loadingInterpolator.getInterpolation((currentTimeMillis2 - i5) / 667.0f) * 250.0f;
                            max += this.loadingInterpolator.getInterpolation((currentTimeMillis2 - (i5 + 667)) / 667.0f) * 250.0f;
                            i4++;
                            i2 = i2;
                        }
                        i = i2;
                        rectF = AndroidUtilities.rectTmp;
                        float f10 = f9 - max;
                        canvas2 = canvas;
                        currentTimeMillis = max;
                        f4 = f10;
                        f2 = f8;
                        z = false;
                        f3 = f7;
                        paint = this.loadingPaint;
                    } else {
                        f2 = f8;
                        i = i2;
                        f3 = f7;
                        float f11 = this.loadingAnimatedProgress.set(this.loadingProgress) * 360.0f;
                        paint = this.loadingPaint;
                        canvas2 = canvas;
                        currentTimeMillis = (-90.0f) + ((((System.currentTimeMillis() % 3000) / 1000.0f) * 120.0f) % 360.0f);
                        f4 = f11;
                        z = false;
                    }
                    canvas2.drawArc(rectF, currentTimeMillis, f4, z, paint);
                    canvas.save();
                    f = 1.0f;
                    float lerp = AndroidUtilities.lerp(1.0f, 0.6f, f5);
                    canvas.scale(lerp, lerp, f3, f2);
                    invalidate();
                } else {
                    i = i2;
                    f = 1.0f;
                }
                this.drawableInverse.setAlpha((int) ((f - f5) * 255.0f));
                Drawable drawable2 = this.drawableInverse;
                drawable2.setBounds(i, i3, i + drawable2.getIntrinsicWidth(), this.drawableInverse.getIntrinsicHeight() + i3);
                this.drawableInverse.draw(canvas);
                if (f5 > 0.0f) {
                    canvas.restore();
                }
                canvas.restore();
            }
            float max2 = Math.max(AndroidUtilities.dp(12.0f) + this.count.getCurrentWidth(), AndroidUtilities.dp(24.0f)) / 2.0f;
            float measuredWidth4 = getMeasuredWidth() - max2;
            float measuredHeight4 = getMeasuredHeight() - max2;
            this.count.setBounds((int) (measuredWidth4 - max2), (int) (measuredHeight4 - max2), (int) (measuredWidth4 + max2), (int) (measuredHeight4 + max2));
            float isNotEmpty = this.count.isNotEmpty();
            if (isNotEmpty > 0.0f) {
                canvas.drawCircle(measuredWidth4, measuredHeight4, (AndroidUtilities.dp(2.0f) + max2) * isNotEmpty * this.countBounceScale, this.countClearPaint);
                canvas.drawCircle(measuredWidth4, measuredHeight4, max2 * isNotEmpty * this.countBounceScale, this.backgroundPaint);
                this.count.draw(canvas);
            }
            canvas.restore();
            super.onDraw(canvas);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (getAlpha() <= 0.0f) {
                return false;
            }
            return super.onTouchEvent(motionEvent);
        }

        public void setCount(int i, boolean z) {
            AnimatedTextView.AnimatedTextDrawable animatedTextDrawable = this.count;
            String str = "";
            if (i > 0) {
                str = "" + i;
            }
            animatedTextDrawable.setText(str, z);
            invalidate();
        }

        public void setEffect(long j) {
            TLRPC.TL_availableEffect effect = MessagesController.getInstance(UserConfig.selectedAccount).getEffect(j);
            setEmoji(effect != null ? Emoji.getEmojiDrawable(effect.emoticon) : null);
        }

        public void setEmoji(Drawable drawable) {
            this.emojiDrawable.set(drawable, true);
        }

        public void setLoading(boolean z, float f) {
            if (this.loadingShown == z && (!z || Math.abs(this.loadingProgress - f) < 0.01f)) {
                if (this.infiniteLoading == (Math.abs(f - (-3.0f)) < 0.01f)) {
                    return;
                }
            }
            this.infiniteLoading = Math.abs(f - (-3.0f)) < 0.01f;
            if (!this.loadingShown && z) {
                this.loadingAnimatedProgress.set(0.0f, true);
            }
            AnimatedFloat animatedFloat = this.loadingAnimatedShown;
            animatedFloat.setDelay((!z || animatedFloat.get() < 1.0f) ? 0L : 650L);
            this.loadingShown = z;
            if (!z) {
                f = 1.0f;
            }
            this.loadingProgress = f;
            invalidate();
        }

        public abstract boolean shouldDrawBackground();

        @Override // android.view.View
        protected boolean verifyDrawable(Drawable drawable) {
            return drawable == this.count || drawable == this.emojiDrawable || super.verifyDrawable(drawable);
        }
    }

    private class SlideTextView extends View {
        Paint arrowPaint;
        Path arrowPath;
        TextPaint bluePaint;
        float cancelAlpha;
        int cancelCharOffset;
        StaticLayout cancelLayout;
        public android.graphics.Rect cancelRect;
        String cancelString;
        float cancelToProgress;
        float cancelWidth;
        TextPaint grayPaint;
        private int lastSize;
        long lastUpdateTime;
        boolean moveForward;
        private boolean pressed;
        Drawable selectableBackground;
        float slideProgress;
        float slideToAlpha;
        String slideToCancelString;
        float slideToCancelWidth;
        StaticLayout slideToLayout;
        boolean smallSize;
        float xOffset;

        public SlideTextView(Context context) {
            super(context);
            this.arrowPaint = new Paint(1);
            this.xOffset = 0.0f;
            this.arrowPath = new Path();
            this.cancelRect = new android.graphics.Rect();
            this.smallSize = AndroidUtilities.displaySize.x <= AndroidUtilities.dp(320.0f);
            TextPaint textPaint = new TextPaint(1);
            this.grayPaint = textPaint;
            textPaint.setTextSize(AndroidUtilities.dp(this.smallSize ? 13.0f : 15.0f));
            TextPaint textPaint2 = new TextPaint(1);
            this.bluePaint = textPaint2;
            textPaint2.setTextSize(AndroidUtilities.dp(15.0f));
            this.bluePaint.setTypeface(AndroidUtilities.bold());
            this.arrowPaint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelIcons));
            this.arrowPaint.setStyle(Paint.Style.STROKE);
            this.arrowPaint.setStrokeWidth(AndroidUtilities.dpf2(this.smallSize ? 1.0f : 1.6f));
            this.arrowPaint.setStrokeCap(Paint.Cap.ROUND);
            this.arrowPaint.setStrokeJoin(Paint.Join.ROUND);
            this.slideToCancelString = LocaleController.getString(R.string.SlideToCancel2);
            String upperCase = LocaleController.getString("Cancel", R.string.Cancel).toUpperCase();
            this.cancelString = upperCase;
            this.cancelCharOffset = this.slideToCancelString.indexOf(upperCase);
            updateColors();
        }

        @Override // android.view.View
        protected void drawableStateChanged() {
            super.drawableStateChanged();
            this.selectableBackground.setState(getDrawableState());
        }

        @Override // android.view.View
        public void jumpDrawablesToCurrentState() {
            super.jumpDrawablesToCurrentState();
            Drawable drawable = this.selectableBackground;
            if (drawable != null) {
                drawable.jumpToCurrentState();
            }
        }

        public void onCancelButtonPressed() {
            long j = 0;
            if (ChatActivityEnterView.this.hasRecordVideo && ChatActivityEnterView.this.isInVideoMode()) {
                CameraController.getInstance().cancelOnInitRunnable(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = ChatActivityEnterView.this.delegate;
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                chatActivityEnterViewDelegate.needStartRecordVideo(5, true, 0, chatActivityEnterView.voiceOnce ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0, chatActivityEnterView.effectId);
                ChatActivityEnterView.this.sendButton.setEffect(ChatActivityEnterView.this.effectId = 0L);
            } else {
                ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                MediaController.getInstance().stopRecording(0, false, 0, ChatActivityEnterView.this.voiceOnce);
            }
            ChatActivityEnterView.this.audioToSend = null;
            ChatActivityEnterView.this.audioToSendMessageObject = null;
            ChatActivityEnterView.this.videoToSendMessageObject = null;
            ChatActivityEnterView.this.millisecondsRecorded = 0L;
            ChatActivityEnterView.this.recordingAudioVideo = false;
            MediaDataController mediaDataController = MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount);
            long j2 = ChatActivityEnterView.this.dialog_id;
            if (ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.parentFragment.isTopic) {
                j = ChatActivityEnterView.this.parentFragment.getTopicId();
            }
            mediaDataController.pushDraftVoiceMessage(j2, j, null);
            ChatActivityEnterView.this.updateRecordInterface(2, true);
            ChatActivityEnterView.this.checkSendButton(true);
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.slideToLayout == null || this.cancelLayout == null || ChatActivityEnterView.this.recordCircle == null) {
                return;
            }
            int width = this.cancelLayout.getWidth() + AndroidUtilities.dp(16.0f);
            this.grayPaint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordTime));
            this.grayPaint.setAlpha((int) (this.slideToAlpha * (1.0f - this.cancelToProgress) * this.slideProgress));
            this.bluePaint.setAlpha((int) (this.cancelAlpha * this.cancelToProgress));
            this.arrowPaint.setColor(this.grayPaint.getColor());
            if (this.smallSize) {
                this.xOffset = AndroidUtilities.dp(16.0f);
            } else {
                long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
                this.lastUpdateTime = System.currentTimeMillis();
                if (this.cancelToProgress == 0.0f && this.slideProgress > 0.8f) {
                    if (this.moveForward) {
                        float dp = this.xOffset + ((AndroidUtilities.dp(3.0f) / 250.0f) * currentTimeMillis);
                        this.xOffset = dp;
                        if (dp > AndroidUtilities.dp(6.0f)) {
                            this.xOffset = AndroidUtilities.dp(6.0f);
                            this.moveForward = false;
                        }
                    } else {
                        float dp2 = this.xOffset - ((AndroidUtilities.dp(3.0f) / 250.0f) * currentTimeMillis);
                        this.xOffset = dp2;
                        if (dp2 < (-AndroidUtilities.dp(6.0f))) {
                            this.xOffset = -AndroidUtilities.dp(6.0f);
                            this.moveForward = true;
                        }
                    }
                }
            }
            boolean z = this.cancelCharOffset >= 0;
            int measuredWidth = ((int) ((getMeasuredWidth() - this.slideToCancelWidth) / 2.0f)) + AndroidUtilities.dp(5.0f);
            int measuredWidth2 = (int) ((getMeasuredWidth() - this.cancelWidth) / 2.0f);
            float primaryHorizontal = z ? this.slideToLayout.getPrimaryHorizontal(this.cancelCharOffset) : 0.0f;
            float f = z ? (measuredWidth + primaryHorizontal) - measuredWidth2 : 0.0f;
            float f2 = this.xOffset;
            float f3 = this.cancelToProgress;
            float dp3 = ((measuredWidth + ((f2 * (1.0f - f3)) * this.slideProgress)) - (f * f3)) + AndroidUtilities.dp(16.0f);
            float dp4 = z ? 0.0f : this.cancelToProgress * AndroidUtilities.dp(12.0f);
            if (this.cancelToProgress != 1.0f) {
                int translationX = (int) ((((-getMeasuredWidth()) / 4) * (1.0f - this.slideProgress)) + (ChatActivityEnterView.this.recordCircle.getTranslationX() * 0.3f));
                canvas.save();
                canvas.clipRect((ChatActivityEnterView.this.recordTimerView == null ? 0.0f : ChatActivityEnterView.this.recordTimerView.getLeftProperty()) + AndroidUtilities.dp(4.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                canvas.save();
                int i = (int) dp3;
                canvas.translate((i - AndroidUtilities.dp(this.smallSize ? 7.0f : 10.0f)) + translationX, dp4);
                canvas.drawPath(this.arrowPath, this.arrowPaint);
                canvas.restore();
                canvas.save();
                canvas.translate(i + translationX, ((getMeasuredHeight() - this.slideToLayout.getHeight()) / 2.0f) + dp4);
                this.slideToLayout.draw(canvas);
                canvas.restore();
                canvas.restore();
            }
            float measuredHeight = (getMeasuredHeight() - this.cancelLayout.getHeight()) / 2.0f;
            if (!z) {
                measuredHeight -= AndroidUtilities.dp(12.0f) - dp4;
            }
            float f4 = z ? dp3 + primaryHorizontal : measuredWidth2;
            this.cancelRect.set((int) f4, (int) measuredHeight, (int) (this.cancelLayout.getWidth() + f4), (int) (this.cancelLayout.getHeight() + measuredHeight));
            this.cancelRect.inset(-AndroidUtilities.dp(16.0f), -AndroidUtilities.dp(16.0f));
            if (this.cancelToProgress > 0.0f) {
                this.selectableBackground.setBounds((getMeasuredWidth() / 2) - width, (getMeasuredHeight() / 2) - width, (getMeasuredWidth() / 2) + width, (getMeasuredHeight() / 2) + width);
                this.selectableBackground.draw(canvas);
                canvas.save();
                canvas.translate(f4, measuredHeight);
                this.cancelLayout.draw(canvas);
                canvas.restore();
            } else {
                setPressed(false);
            }
            if (this.cancelToProgress != 1.0f) {
                invalidate();
            }
        }

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            Path path;
            float f;
            float dpf2;
            float f2;
            float f3;
            super.onMeasure(i, i2);
            int measuredHeight = getMeasuredHeight() + (getMeasuredWidth() << 16);
            if (this.lastSize != measuredHeight) {
                this.lastSize = measuredHeight;
                this.slideToCancelWidth = this.grayPaint.measureText(this.slideToCancelString);
                this.cancelWidth = this.bluePaint.measureText(this.cancelString);
                this.lastUpdateTime = System.currentTimeMillis();
                int measuredHeight2 = getMeasuredHeight() >> 1;
                this.arrowPath.reset();
                if (this.smallSize) {
                    path = this.arrowPath;
                    f = 2.5f;
                    dpf2 = AndroidUtilities.dpf2(2.5f);
                    f2 = measuredHeight2;
                    f3 = 3.12f;
                } else {
                    path = this.arrowPath;
                    f = 4.0f;
                    dpf2 = AndroidUtilities.dpf2(4.0f);
                    f2 = measuredHeight2;
                    f3 = 5.0f;
                }
                path.setLastPoint(dpf2, f2 - AndroidUtilities.dpf2(f3));
                this.arrowPath.lineTo(0.0f, f2);
                this.arrowPath.lineTo(AndroidUtilities.dpf2(f), f2 + AndroidUtilities.dpf2(f3));
                String str = this.slideToCancelString;
                TextPaint textPaint = this.grayPaint;
                int i3 = (int) this.slideToCancelWidth;
                Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
                this.slideToLayout = new StaticLayout(str, textPaint, i3, alignment, 1.0f, 0.0f, false);
                this.cancelLayout = new StaticLayout(this.cancelString, this.bluePaint, (int) this.cancelWidth, alignment, 1.0f, 0.0f, false);
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                setPressed(false);
            }
            if (this.cancelToProgress == 0.0f || !isEnabled()) {
                return false;
            }
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (motionEvent.getAction() == 0) {
                boolean contains = this.cancelRect.contains(x, y);
                this.pressed = contains;
                if (contains) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        this.selectableBackground.setHotspot(x, y);
                    }
                    setPressed(true);
                }
                return this.pressed;
            }
            boolean z = this.pressed;
            if (!z) {
                return z;
            }
            if (motionEvent.getAction() == 2 && !this.cancelRect.contains(x, y)) {
                setPressed(false);
                return false;
            }
            if (motionEvent.getAction() == 1 && this.cancelRect.contains(x, y)) {
                onCancelButtonPressed();
            }
            return true;
        }

        public void setCancelToProgress(float f) {
            this.cancelToProgress = f;
        }

        public void setSlideX(float f) {
            this.slideProgress = f;
        }

        public void updateColors() {
            this.grayPaint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordTime));
            TextPaint textPaint = this.bluePaint;
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            int i = Theme.key_chat_recordVoiceCancel;
            textPaint.setColor(chatActivityEnterView.getThemedColor(i));
            this.slideToAlpha = this.grayPaint.getAlpha();
            this.cancelAlpha = this.bluePaint.getAlpha();
            Drawable createSimpleSelectorCircleDrawable = Theme.createSimpleSelectorCircleDrawable(AndroidUtilities.dp(60.0f), 0, ColorUtils.setAlphaComponent(ChatActivityEnterView.this.getThemedColor(i), 26));
            this.selectableBackground = createSimpleSelectorCircleDrawable;
            createSimpleSelectorCircleDrawable.setCallback(this);
        }

        @Override // android.view.View
        public boolean verifyDrawable(Drawable drawable) {
            return this.selectableBackground == drawable || super.verifyDrawable(drawable);
        }
    }

    private static class SlowModeBtn extends FrameLayout {
        private final RectF bgRect;
        private final Drawable closeDrawable;
        private final Paint gradientPaint;
        private boolean isPremiumMode;
        private final SimpleTextView textView;

        public SlowModeBtn(Context context) {
            super(context);
            this.bgRect = new RectF();
            this.gradientPaint = new Paint(1);
            this.isPremiumMode = false;
            SimpleTextView simpleTextView = new SimpleTextView(context);
            this.textView = simpleTextView;
            addView(simpleTextView, LayoutHelper.createFrame(-1, -1.0f));
            setWillNotDraw(false);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.msg_mini_close_tooltip);
            this.closeDrawable = drawable;
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            setClipToPadding(false);
            setClipChildren(false);
            ScaleStateListAnimator.apply(this);
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (!(view instanceof SimpleTextView) || !this.isPremiumMode) {
                return super.drawChild(canvas, view, j);
            }
            SimpleTextView simpleTextView = (SimpleTextView) view;
            canvas.save();
            canvas.scale(0.8f, 0.8f);
            canvas.translate(-AndroidUtilities.dp(16.0f), AndroidUtilities.dp(5.0f));
            int color = simpleTextView.getTextPaint().getColor();
            simpleTextView.getTextPaint().setColor(-1);
            boolean drawChild = super.drawChild(canvas, view, j);
            simpleTextView.getTextPaint().setColor(color);
            canvas.restore();
            return drawChild;
        }

        public CharSequence getText() {
            return this.textView.getText();
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            if (this.isPremiumMode) {
                canvas.save();
                int dp = AndroidUtilities.dp(26.0f);
                canvas.translate(0.0f, ((getMeasuredHeight() - dp) / 2.0f) - AndroidUtilities.dp(1.0f));
                float f = dp;
                this.bgRect.set(0.0f, 0.0f, getMeasuredWidth() - getPaddingEnd(), f);
                float f2 = f / 2.0f;
                canvas.drawRoundRect(this.bgRect, f2, f2, this.gradientPaint);
                canvas.translate(((getMeasuredWidth() - getPaddingEnd()) - AndroidUtilities.dp(6.0f)) - this.closeDrawable.getIntrinsicWidth(), AndroidUtilities.dp(5.0f));
                this.closeDrawable.draw(canvas);
                canvas.restore();
            }
        }

        @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            this.gradientPaint.setShader(new LinearGradient(0.0f, 0.0f, getMeasuredWidth(), 0.0f, new int[]{-9071617, -5999873}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
        }

        public void setGravity(int i) {
            this.textView.setGravity(i);
            invalidate();
        }

        public void setPremiumMode(boolean z) {
            this.isPremiumMode = z;
            invalidate();
        }

        public void setText(CharSequence charSequence) {
            this.textView.setText(charSequence);
            invalidate();
        }

        public void setTextColor(int i) {
            this.textView.setTextColor(i);
            invalidate();
        }

        public void setTextSize(int i) {
            this.textView.setTextSize(i);
            invalidate();
        }
    }

    public class TimerView extends View {
        StaticLayout inLayout;
        boolean isRunning;
        long lastSendTypingTime;
        float left;
        String oldString;
        StaticLayout outLayout;
        final float replaceDistance;
        SpannableStringBuilder replaceIn;
        SpannableStringBuilder replaceOut;
        SpannableStringBuilder replaceStable;
        float replaceTransition;
        long startTime;
        long stopTime;
        boolean stoppedInternal;
        TextPaint textPaint;

        public TimerView(Context context) {
            super(context);
            this.replaceIn = new SpannableStringBuilder();
            this.replaceOut = new SpannableStringBuilder();
            this.replaceStable = new SpannableStringBuilder();
            this.replaceDistance = AndroidUtilities.dp(15.0f);
        }

        public float getLeftProperty() {
            return this.left;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            float lineWidth;
            String str;
            if (this.textPaint == null) {
                TextPaint textPaint = new TextPaint(1);
                this.textPaint = textPaint;
                textPaint.setTextSize(AndroidUtilities.dp(15.0f));
                this.textPaint.setTypeface(AndroidUtilities.bold());
                this.textPaint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordTime));
            }
            long currentTimeMillis = System.currentTimeMillis();
            long j = this.isRunning ? currentTimeMillis - this.startTime : this.stopTime - this.startTime;
            long j2 = j / 1000;
            int i = ((int) (j % 1000)) / 10;
            if (ChatActivityEnterView.this.isInVideoMode() && j >= 59500 && !this.stoppedInternal) {
                ChatActivityEnterView.this.startedDraggingX = -1.0f;
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = ChatActivityEnterView.this.delegate;
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                chatActivityEnterViewDelegate.needStartRecordVideo(3, true, 0, chatActivityEnterView.voiceOnce ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0, chatActivityEnterView.effectId);
                ChatActivityEnterView.this.sendButton.setEffect(ChatActivityEnterView.this.effectId = 0L);
                this.stoppedInternal = true;
            }
            if (this.isRunning && currentTimeMillis > this.lastSendTypingTime + 5000) {
                this.lastSendTypingTime = currentTimeMillis;
                MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).sendTyping(ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.getThreadMessageId(), ChatActivityEnterView.this.isInVideoMode() ? 7 : 1, 0);
            }
            String formatTimerDurationFast = AndroidUtilities.formatTimerDurationFast((int) j2, i);
            if (formatTimerDurationFast.length() < 3 || (str = this.oldString) == null || str.length() < 3 || formatTimerDurationFast.length() != this.oldString.length() || formatTimerDurationFast.charAt(formatTimerDurationFast.length() - 3) == this.oldString.charAt(formatTimerDurationFast.length() - 3)) {
                if (this.replaceStable == null) {
                    this.replaceStable = new SpannableStringBuilder(formatTimerDurationFast);
                }
                if (this.replaceStable.length() == 0 || this.replaceStable.length() != formatTimerDurationFast.length()) {
                    this.replaceStable.clear();
                    this.replaceStable.append((CharSequence) formatTimerDurationFast);
                } else {
                    this.replaceStable.replace(r10.length() - 1, this.replaceStable.length(), (CharSequence) formatTimerDurationFast, (formatTimerDurationFast.length() - 1) - (formatTimerDurationFast.length() - this.replaceStable.length()), formatTimerDurationFast.length());
                }
            } else {
                int length = formatTimerDurationFast.length();
                this.replaceIn.clear();
                this.replaceOut.clear();
                this.replaceStable.clear();
                this.replaceIn.append((CharSequence) formatTimerDurationFast);
                this.replaceOut.append((CharSequence) this.oldString);
                this.replaceStable.append((CharSequence) formatTimerDurationFast);
                int i2 = -1;
                int i3 = -1;
                int i4 = 0;
                int i5 = 0;
                for (int i6 = 0; i6 < length - 1; i6++) {
                    if (this.oldString.charAt(i6) != formatTimerDurationFast.charAt(i6)) {
                        if (i5 == 0) {
                            i3 = i6;
                        }
                        i5++;
                        if (i4 != 0) {
                            EmptyStubSpan emptyStubSpan = new EmptyStubSpan();
                            if (i6 == length - 2) {
                                i4++;
                            }
                            int i7 = i4 + i2;
                            this.replaceIn.setSpan(emptyStubSpan, i2, i7, 33);
                            this.replaceOut.setSpan(emptyStubSpan, i2, i7, 33);
                            i4 = 0;
                        }
                    } else {
                        if (i4 == 0) {
                            i2 = i6;
                        }
                        i4++;
                        if (i5 != 0) {
                            this.replaceStable.setSpan(new EmptyStubSpan(), i3, i5 + i3, 33);
                            i5 = 0;
                        }
                    }
                }
                if (i4 != 0) {
                    EmptyStubSpan emptyStubSpan2 = new EmptyStubSpan();
                    int i8 = i4 + i2 + 1;
                    this.replaceIn.setSpan(emptyStubSpan2, i2, i8, 33);
                    this.replaceOut.setSpan(emptyStubSpan2, i2, i8, 33);
                }
                if (i5 != 0) {
                    this.replaceStable.setSpan(new EmptyStubSpan(), i3, i5 + i3, 33);
                }
                SpannableStringBuilder spannableStringBuilder = this.replaceIn;
                TextPaint textPaint2 = this.textPaint;
                int measuredWidth = getMeasuredWidth();
                Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
                this.inLayout = new StaticLayout(spannableStringBuilder, textPaint2, measuredWidth, alignment, 1.0f, 0.0f, false);
                this.outLayout = new StaticLayout(this.replaceOut, this.textPaint, getMeasuredWidth(), alignment, 1.0f, 0.0f, false);
                this.replaceTransition = 1.0f;
            }
            float f = this.replaceTransition;
            if (f != 0.0f) {
                float f2 = f - 0.15f;
                this.replaceTransition = f2;
                if (f2 < 0.0f) {
                    this.replaceTransition = 0.0f;
                }
            }
            float measuredHeight = getMeasuredHeight() / 2;
            if (this.replaceTransition == 0.0f) {
                this.replaceStable.clearSpans();
                StaticLayout staticLayout = new StaticLayout(this.replaceStable, this.textPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                canvas.save();
                canvas.translate(0.0f, measuredHeight - (staticLayout.getHeight() / 2.0f));
                staticLayout.draw(canvas);
                canvas.restore();
                lineWidth = staticLayout.getLineWidth(0) + 0.0f;
            } else {
                if (this.inLayout != null) {
                    canvas.save();
                    this.textPaint.setAlpha((int) ((1.0f - this.replaceTransition) * 255.0f));
                    canvas.translate(0.0f, (measuredHeight - (this.inLayout.getHeight() / 2.0f)) - (this.replaceDistance * this.replaceTransition));
                    this.inLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.outLayout != null) {
                    canvas.save();
                    this.textPaint.setAlpha((int) (this.replaceTransition * 255.0f));
                    canvas.translate(0.0f, (measuredHeight - (this.outLayout.getHeight() / 2.0f)) + (this.replaceDistance * (1.0f - this.replaceTransition)));
                    this.outLayout.draw(canvas);
                    canvas.restore();
                }
                canvas.save();
                this.textPaint.setAlpha(NotificationCenter.newLocationAvailable);
                StaticLayout staticLayout2 = new StaticLayout(this.replaceStable, this.textPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                canvas.translate(0.0f, measuredHeight - (staticLayout2.getHeight() / 2.0f));
                staticLayout2.draw(canvas);
                canvas.restore();
                lineWidth = staticLayout2.getLineWidth(0) + 0.0f;
            }
            this.left = lineWidth;
            this.oldString = formatTimerDurationFast;
            if (this.isRunning || this.replaceTransition != 0.0f) {
                invalidate();
            }
        }

        public void reset() {
            this.isRunning = false;
            this.startTime = 0L;
            this.stopTime = 0L;
            this.stoppedInternal = false;
        }

        public void start(long j) {
            this.isRunning = true;
            long currentTimeMillis = System.currentTimeMillis() - j;
            this.startTime = currentTimeMillis;
            this.lastSendTypingTime = currentTimeMillis;
            invalidate();
        }

        public void stop() {
            if (this.isRunning) {
                this.isRunning = false;
                if (this.startTime > 0) {
                    this.stopTime = System.currentTimeMillis();
                }
                invalidate();
            }
            this.lastSendTypingTime = 0L;
        }

        public void updateColors() {
            TextPaint textPaint = this.textPaint;
            if (textPaint != null) {
                textPaint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordTime));
            }
        }
    }

    public ChatActivityEnterView(Activity activity, SizeNotifierFrameLayout sizeNotifierFrameLayout, ChatActivity chatActivity, boolean z) {
        this(activity, sizeNotifierFrameLayout, chatActivity, z, null);
    }

    public ChatActivityEnterView(final Activity activity, SizeNotifierFrameLayout sizeNotifierFrameLayout, final ChatActivity chatActivity, boolean z, Theme.ResourcesProvider resourcesProvider) {
        super(activity, chatActivity == null ? null : chatActivity.contentView);
        int i;
        String str;
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate;
        this.emojiButtonScale = 1.0f;
        this.emojiButtonAlpha = 1.0f;
        this.emojiButtonPaddingScale = 1.0f;
        this.emojiButtonPaddingAlpha = 1.0f;
        this.attachLayoutAlpha = 1.0f;
        this.attachLayoutPaddingAlpha = 1.0f;
        this.horizontalPadding = 0.0f;
        this.sendButtonEnabled = true;
        int i2 = UserConfig.selectedAccount;
        this.currentAccount = i2;
        this.accountInstance = AccountInstance.getInstance(i2);
        this.lineCount = 1;
        this.currentLimit = -1;
        this.botMenuButtonType = BotMenuButtonType.NO_BUTTON;
        this.sendRoundEnabled = true;
        this.sendVoiceEnabled = true;
        this.sendPlainEnabled = true;
        this.animationParamsX = new HashMap();
        this.mediaMessageButtonsDelegate = new View.AccessibilityDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView.1
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setClassName("android.widget.ImageButton");
                accessibilityNodeInfo.setClickable(true);
                accessibilityNodeInfo.setLongClickable(true);
            }
        };
        this.ctrlPressed = false;
        this.shiftPressed = false;
        this.currentPopupContentType = -1;
        this.isPaused = true;
        this.startedDraggingX = -1.0f;
        this.distCanMove = AndroidUtilities.dp(80.0f);
        this.location = new int[2];
        this.messageWebPageSearch = true;
        this.animatingContentType = -1;
        this.doneButtonEnabledProgress = 1.0f;
        this.doneButtonEnabled = true;
        this.openKeyboardRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.2
            @Override // java.lang.Runnable
            public void run() {
                if ((ChatActivityEnterView.this.hasBotWebView() && ChatActivityEnterView.this.botCommandsMenuIsShowing()) || BaseFragment.hasSheets(ChatActivityEnterView.this.parentFragment) || ChatActivityEnterView.this.destroyed) {
                    return;
                }
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                if (chatActivityEnterView.messageEditText == null || !chatActivityEnterView.waitingForKeyboardOpen || ChatActivityEnterView.this.keyboardVisible || AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
                    return;
                }
                if (ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.onKeyboardRequested();
                }
                ChatActivityEnterView.this.messageEditText.requestFocus();
                AndroidUtilities.showKeyboard(ChatActivityEnterView.this.messageEditText);
                AndroidUtilities.cancelRunOnUIThread(ChatActivityEnterView.this.openKeyboardRunnable);
                AndroidUtilities.runOnUIThread(ChatActivityEnterView.this.openKeyboardRunnable, 100L);
            }
        };
        this.updateExpandabilityRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.3
            private int lastKnownPage = -1;

            @Override // java.lang.Runnable
            public void run() {
                int currentPage;
                if (ChatActivityEnterView.this.emojiView == null || (currentPage = ChatActivityEnterView.this.emojiView.getCurrentPage()) == this.lastKnownPage) {
                    return;
                }
                this.lastKnownPage = currentPage;
                boolean z2 = ChatActivityEnterView.this.stickersTabOpen;
                ChatActivityEnterView.this.stickersTabOpen = currentPage == 1 || currentPage == 2;
                boolean z3 = ChatActivityEnterView.this.emojiTabOpen;
                ChatActivityEnterView.this.emojiTabOpen = currentPage == 0;
                if (ChatActivityEnterView.this.stickersExpanded) {
                    if (ChatActivityEnterView.this.searchingType != 0) {
                        ChatActivityEnterView.this.setSearchingTypeInternal(currentPage != 0 ? 1 : 2, true);
                        ChatActivityEnterView.this.checkStickresExpandHeight();
                    } else if (!ChatActivityEnterView.this.stickersTabOpen) {
                        ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                    }
                }
                if (z2 == ChatActivityEnterView.this.stickersTabOpen && z3 == ChatActivityEnterView.this.emojiTabOpen) {
                    return;
                }
                ChatActivityEnterView.this.checkSendButton(true);
            }
        };
        this.roundedTranslationYProperty = new Property(Integer.class, "translationY") { // from class: org.telegram.ui.Components.ChatActivityEnterView.4
            @Override // android.util.Property
            public Integer get(View view) {
                return Integer.valueOf(Math.round(view.getTranslationY()));
            }

            @Override // android.util.Property
            public void set(View view, Integer num) {
                view.setTranslationY(num.intValue());
            }
        };
        Class<Float> cls = Float.class;
        this.recordCircleScale = new Property(cls, "scale") { // from class: org.telegram.ui.Components.ChatActivityEnterView.5
            @Override // android.util.Property
            public Float get(RecordCircle recordCircle) {
                return Float.valueOf(recordCircle.getScale());
            }

            @Override // android.util.Property
            public void set(RecordCircle recordCircle, Float f) {
                recordCircle.setScale(f.floatValue());
            }
        };
        this.recordControlsCircleScale = new Property(cls, "controlsScale") { // from class: org.telegram.ui.Components.ChatActivityEnterView.6
            @Override // android.util.Property
            public Float get(RecordCircle recordCircle) {
                return Float.valueOf(recordCircle.getControlsScale());
            }

            @Override // android.util.Property
            public void set(RecordCircle recordCircle, Float f) {
                recordCircle.setControlsScale(f.floatValue());
            }
        };
        this.redDotPaint = new Paint(1);
        this.onFinishInitCameraRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.7
            @Override // java.lang.Runnable
            public void run() {
                if (ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.needStartRecordVideo(0, true, 0, 0, 0L);
                }
            }
        };
        this.recordAudioVideoRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.8
            @Override // java.lang.Runnable
            public void run() {
                int checkSelfPermission;
                int checkSelfPermission2;
                int checkSelfPermission3;
                if (ChatActivityEnterView.this.delegate == null || ChatActivityEnterView.this.parentActivity == null) {
                    return;
                }
                ChatActivityEnterView.this.delegate.onPreAudioVideoRecord();
                ChatActivityEnterView.this.calledRecordRunnable = true;
                ChatActivityEnterView.this.recordAudioVideoRunnableStarted = false;
                if (ChatActivityEnterView.this.slideText != null) {
                    ChatActivityEnterView.this.slideText.setAlpha(1.0f);
                    ChatActivityEnterView.this.slideText.setTranslationY(0.0f);
                }
                ChatActivityEnterView.this.audioToSendPath = null;
                ChatActivityEnterView.this.audioToSend = null;
                if (!ChatActivityEnterView.this.isInVideoMode()) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        checkSelfPermission = ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.RECORD_AUDIO");
                        if (checkSelfPermission != 0) {
                            ChatActivityEnterView.this.parentActivity.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 3);
                            return;
                        }
                    }
                    ChatActivityEnterView.this.delegate.needStartRecordAudio(1);
                    ChatActivityEnterView.this.startedDraggingX = -1.0f;
                    TL_stories.StoryItem replyToStory = ChatActivityEnterView.this.delegate != null ? ChatActivityEnterView.this.delegate.getReplyToStory() : null;
                    MediaController mediaController = MediaController.getInstance();
                    int i3 = ChatActivityEnterView.this.currentAccount;
                    long j = ChatActivityEnterView.this.dialog_id;
                    MessageObject messageObject = ChatActivityEnterView.this.replyingMessageObject;
                    MessageObject threadMessage = ChatActivityEnterView.this.getThreadMessage();
                    ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                    mediaController.startRecording(i3, j, messageObject, threadMessage, replyToStory, chatActivityEnterView.recordingGuid, true, chatActivityEnterView.parentFragment != null ? ChatActivityEnterView.this.parentFragment.quickReplyShortcut : null, ChatActivityEnterView.this.parentFragment != null ? ChatActivityEnterView.this.parentFragment.getQuickReplyId() : 0);
                    ChatActivityEnterView.this.recordingAudioVideo = true;
                    ChatActivityEnterView.this.updateRecordInterface(0, true);
                    if (ChatActivityEnterView.this.recordTimerView != null) {
                        ChatActivityEnterView.this.recordTimerView.start(0L);
                    }
                    if (ChatActivityEnterView.this.recordDot != null) {
                        ChatActivityEnterView.this.recordDot.enterAnimation = false;
                    }
                    ChatActivityEnterView.this.audioVideoButtonContainer.getParent().requestDisallowInterceptTouchEvent(true);
                    if (ChatActivityEnterView.this.recordCircle != null) {
                        ChatActivityEnterView.this.recordCircle.showWaves(true, false);
                        return;
                    }
                    return;
                }
                if (Build.VERSION.SDK_INT >= 23) {
                    checkSelfPermission2 = ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.RECORD_AUDIO");
                    boolean z2 = checkSelfPermission2 == 0;
                    checkSelfPermission3 = ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.CAMERA");
                    boolean z3 = checkSelfPermission3 == 0;
                    if (!z2 || !z3) {
                        String[] strArr = new String[(z2 || z3) ? 1 : 2];
                        if (!z2 && !z3) {
                            strArr[0] = "android.permission.RECORD_AUDIO";
                            strArr[1] = "android.permission.CAMERA";
                        } else if (z2) {
                            strArr[0] = "android.permission.CAMERA";
                        } else {
                            strArr[0] = "android.permission.RECORD_AUDIO";
                        }
                        ChatActivityEnterView.this.parentActivity.requestPermissions(strArr, 150);
                        return;
                    }
                }
                if (CameraController.getInstance().isCameraInitied()) {
                    ChatActivityEnterView.this.onFinishInitCameraRunnable.run();
                } else {
                    CameraController.getInstance().initCamera(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                }
                if (ChatActivityEnterView.this.recordingAudioVideo) {
                    return;
                }
                ChatActivityEnterView.this.recordingAudioVideo = true;
                ChatActivityEnterView.this.updateRecordInterface(0, true);
                if (ChatActivityEnterView.this.recordCircle != null) {
                    ChatActivityEnterView.this.recordCircle.showWaves(false, false);
                }
                if (ChatActivityEnterView.this.recordTimerView != null) {
                    ChatActivityEnterView.this.recordTimerView.reset();
                }
            }
        };
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.paint = new Paint(1);
        this.pauseRect = new RectF();
        this.sendRect = new android.graphics.Rect();
        this.rect = new android.graphics.Rect();
        this.runEmojiPanelAnimation = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.9
            @Override // java.lang.Runnable
            public void run() {
                if (ChatActivityEnterView.this.panelAnimation == null || ChatActivityEnterView.this.panelAnimation.isRunning()) {
                    return;
                }
                ChatActivityEnterView.this.panelAnimation.start();
            }
        };
        this.EMOJI_BUTTON_SCALE = new Property(cls, "emoji_button_scale") { // from class: org.telegram.ui.Components.ChatActivityEnterView.10
            @Override // android.util.Property
            public Float get(View view) {
                return Float.valueOf(ChatActivityEnterView.this.emojiButtonScale);
            }

            @Override // android.util.Property
            public void set(View view, Float f) {
                ChatActivityEnterView.this.emojiButtonScale = f.floatValue();
                ChatActivityEnterView.this.updateEmojiButtonParams();
            }
        };
        this.ATTACH_LAYOUT_ALPHA = new Property(cls, "attach_scale") { // from class: org.telegram.ui.Components.ChatActivityEnterView.11
            @Override // android.util.Property
            public Float get(View view) {
                return Float.valueOf(ChatActivityEnterView.this.attachLayoutAlpha);
            }

            @Override // android.util.Property
            public void set(View view, Float f) {
                ChatActivityEnterView.this.attachLayoutAlpha = f.floatValue();
                ChatActivityEnterView.this.updateAttachLayoutParams();
            }
        };
        this.EMOJI_BUTTON_ALPHA = new Property(cls, "emoji_button_alpha") { // from class: org.telegram.ui.Components.ChatActivityEnterView.12
            @Override // android.util.Property
            public Float get(View view) {
                return Float.valueOf(ChatActivityEnterView.this.emojiButtonAlpha);
            }

            @Override // android.util.Property
            public void set(View view, Float f) {
                ChatActivityEnterView.this.emojiButtonAlpha = f.floatValue();
                ChatActivityEnterView.this.updateEmojiButtonParams();
            }
        };
        this.ATTACH_LAYOUT_TRANSLATION_X = new Property(cls, "attach_layout_translation_x") { // from class: org.telegram.ui.Components.ChatActivityEnterView.13
            @Override // android.util.Property
            public Float get(View view) {
                return Float.valueOf(ChatActivityEnterView.this.attachLayoutTranslationX);
            }

            @Override // android.util.Property
            public void set(View view, Float f) {
                ChatActivityEnterView.this.attachLayoutTranslationX = f.floatValue();
                ChatActivityEnterView.this.updateAttachLayoutParams();
            }
        };
        this.MESSAGE_TEXT_TRANSLATION_X = new Property(cls, "message_text_translation_x") { // from class: org.telegram.ui.Components.ChatActivityEnterView.14
            @Override // android.util.Property
            public Float get(View view) {
                return Float.valueOf(ChatActivityEnterView.this.messageTextTranslationX);
            }

            @Override // android.util.Property
            public void set(View view, Float f) {
                ChatActivityEnterView.this.messageTextTranslationX = f.floatValue();
                ChatActivityEnterView.this.updateMessageTextParams();
            }
        };
        this.allowBlur = true;
        this.shouldDrawBackground = true;
        this.backgroundPaint = new Paint();
        this.composeShadowAlpha = 1.0f;
        this.blurBounds = new android.graphics.Rect();
        this.dismissSendPreview = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$new$28();
            }
        };
        this.messageEditTextEnabled = true;
        this.topViewUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ChatActivityEnterView.this.lambda$new$40(valueAnimator);
            }
        };
        this.premiumEmojiBulletin = true;
        this.botCommandLastPosition = -1;
        Paint paint = new Paint(1);
        this.gradientPaint = paint;
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, 16.0f, new int[]{-1, 16777215}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
        this.clipGradient = linearGradient;
        this.clipMatrix = new Matrix();
        CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
        this.topGradientAlpha = new AnimatedFloat(this, 0L, 280L, cubicBezierInterpolator);
        this.bottomGradientAlpha = new AnimatedFloat(this, 0L, 280L, cubicBezierInterpolator);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint.setShader(linearGradient);
        this.resourcesProvider = resourcesProvider;
        this.backgroundColor = getThemedColor(Theme.key_chat_messagePanelBackground);
        this.drawBlur = false;
        this.isChat = z;
        this.smoothKeyboard = z && !AndroidUtilities.isInMultiwindow && (chatActivity == null || !chatActivity.isInBubbleMode());
        Paint paint2 = new Paint(1);
        this.dotPaint = paint2;
        paint2.setColor(getThemedColor(Theme.key_chat_emojiPanelNewTrending));
        setFocusable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
        setClipChildren(false);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStarted);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordPaused);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordResumed);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStartError);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStopped);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioDidSent);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioRouteChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messageReceivedByServer2);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.sendingMessagesChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioRecordTooShort);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateBotMenuButton);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdatePremiumGiftFieldIcon);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiLoaded);
        this.parentActivity = activity;
        this.parentFragment = chatActivity;
        if (chatActivity != null) {
            this.recordingGuid = chatActivity.getClassGuid();
        }
        this.sizeNotifierLayout = sizeNotifierFrameLayout;
        sizeNotifierFrameLayout.setDelegate(this);
        this.sendByEnter = MessagesController.getGlobalMainSettings().getBoolean("send_by_enter", false);
        FrameLayout frameLayout = new FrameLayout(activity) { // from class: org.telegram.ui.Components.ChatActivityEnterView.15
            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                return (ChatActivityEnterView.this.botWebViewButton == null || ChatActivityEnterView.this.botWebViewButton.getVisibility() != 0) ? super.dispatchTouchEvent(motionEvent) : ChatActivityEnterView.this.botWebViewButton.dispatchTouchEvent(motionEvent);
            }
        };
        this.textFieldContainer = frameLayout;
        frameLayout.setClipChildren(false);
        this.textFieldContainer.setClipToPadding(false);
        this.textFieldContainer.setPadding(0, AndroidUtilities.dp(1.0f), 0, 0);
        addView(this.textFieldContainer, LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 1.0f, 0.0f, 0.0f));
        16 r0 = new 16(activity);
        this.messageEditTextContainer = r0;
        r0.setClipChildren(false);
        this.textFieldContainer.addView(r0, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 48.0f, 0.0f));
        ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView = new ChatActivityEnterViewAnimatedIconView(activity) { // from class: org.telegram.ui.Components.ChatActivityEnterView.17
            @Override // android.widget.ImageView, android.view.View
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                if (getTag() == null || ChatActivityEnterView.this.attachLayout == null || ChatActivityEnterView.this.emojiViewVisible || MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).getUnreadStickerSets().isEmpty() || ChatActivityEnterView.this.dotPaint == null) {
                    return;
                }
                canvas.drawCircle((getWidth() / 2) + AndroidUtilities.dp(9.0f), (getHeight() / 2) - AndroidUtilities.dp(8.0f), AndroidUtilities.dp(5.0f), ChatActivityEnterView.this.dotPaint);
            }
        };
        this.emojiButton = chatActivityEnterViewAnimatedIconView;
        chatActivityEnterViewAnimatedIconView.setContentDescription(LocaleController.getString(R.string.AccDescrEmojiButton));
        this.emojiButton.setFocusable(true);
        int dp = AndroidUtilities.dp(9.5f);
        this.emojiButton.setPadding(dp, dp, dp, dp);
        ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView2 = this.emojiButton;
        int i3 = Theme.key_chat_messagePanelIcons;
        int themedColor = getThemedColor(i3);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
        chatActivityEnterViewAnimatedIconView2.setColorFilter(new PorterDuffColorFilter(themedColor, mode));
        int i4 = Build.VERSION.SDK_INT;
        if (i4 >= 21) {
            this.emojiButton.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
        this.emojiButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$new$1(view);
            }
        });
        this.messageEditTextContainer.addView(this.emojiButton, LayoutHelper.createFrame(48, 48.0f, 83, 3.0f, 0.0f, 0.0f, 0.0f));
        setEmojiButtonImage(false, false);
        if (z) {
            LinearLayout linearLayout = new LinearLayout(activity);
            this.attachLayout = linearLayout;
            linearLayout.setOrientation(0);
            this.attachLayout.setEnabled(false);
            this.attachLayout.setPivotX(AndroidUtilities.dp(48.0f));
            this.attachLayout.setClipChildren(false);
            this.messageEditTextContainer.addView(this.attachLayout, LayoutHelper.createFrame(-2, 48, 85));
            this.notifyButton = new ImageView(activity);
            CrossOutDrawable crossOutDrawable = new CrossOutDrawable(activity, R.drawable.input_notify_on, i3);
            this.notifySilentDrawable = crossOutDrawable;
            this.notifyButton.setImageDrawable(crossOutDrawable);
            this.notifySilentDrawable.setCrossOut(this.silent, false);
            ImageView imageView = this.notifyButton;
            if (this.silent) {
                i = R.string.AccDescrChanSilentOn;
                str = "AccDescrChanSilentOn";
            } else {
                i = R.string.AccDescrChanSilentOff;
                str = "AccDescrChanSilentOff";
            }
            imageView.setContentDescription(LocaleController.getString(str, i));
            ImageView imageView2 = this.notifyButton;
            int themedColor2 = getThemedColor(i3);
            PorterDuff.Mode mode2 = PorterDuff.Mode.MULTIPLY;
            imageView2.setColorFilter(new PorterDuffColorFilter(themedColor2, mode2));
            ImageView imageView3 = this.notifyButton;
            ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER;
            imageView3.setScaleType(scaleType);
            if (i4 >= 21) {
                this.notifyButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
            }
            this.notifyButton.setVisibility((!this.canWriteToChannel || ((chatActivityEnterViewDelegate = this.delegate) != null && chatActivityEnterViewDelegate.hasScheduledMessages())) ? 8 : 0);
            this.attachLayout.addView(this.notifyButton, LayoutHelper.createLinear(48, 48));
            this.notifyButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.18
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int i5;
                    String str2;
                    ChatActivityEnterView.this.silent = !r9.silent;
                    if (ChatActivityEnterView.this.notifySilentDrawable == null) {
                        ChatActivityEnterView.this.notifySilentDrawable = new CrossOutDrawable(activity, R.drawable.input_notify_on, Theme.key_chat_messagePanelIcons);
                    }
                    ChatActivityEnterView.this.notifySilentDrawable.setCrossOut(ChatActivityEnterView.this.silent, true);
                    ChatActivityEnterView.this.notifyButton.setImageDrawable(ChatActivityEnterView.this.notifySilentDrawable);
                    MessagesController.getNotificationsSettings(ChatActivityEnterView.this.currentAccount).edit().putBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.silent).commit();
                    NotificationsController notificationsController = NotificationsController.getInstance(ChatActivityEnterView.this.currentAccount);
                    long j = ChatActivityEnterView.this.dialog_id;
                    ChatActivity chatActivity2 = chatActivity;
                    notificationsController.updateServerNotificationsSettings(j, chatActivity2 == null ? 0L : chatActivity2.getTopicId());
                    UndoView undoView = chatActivity.getUndoView();
                    if (undoView != null) {
                        undoView.showWithAction(0L, !ChatActivityEnterView.this.silent ? 54 : 55, (Runnable) null);
                    }
                    ImageView imageView4 = ChatActivityEnterView.this.notifyButton;
                    if (ChatActivityEnterView.this.silent) {
                        i5 = R.string.AccDescrChanSilentOn;
                        str2 = "AccDescrChanSilentOn";
                    } else {
                        i5 = R.string.AccDescrChanSilentOff;
                        str2 = "AccDescrChanSilentOff";
                    }
                    imageView4.setContentDescription(LocaleController.getString(str2, i5));
                    ChatActivityEnterView.this.updateFieldHint(true);
                }
            });
            ImageView imageView4 = new ImageView(activity);
            this.attachButton = imageView4;
            imageView4.setScaleType(scaleType);
            this.attachButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(i3), mode2));
            this.attachButton.setImageResource(R.drawable.msg_input_attach2);
            if (i4 >= 21) {
                this.attachButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
            }
            this.attachLayout.addView(this.attachButton, LayoutHelper.createLinear(48, 48));
            this.attachButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ChatActivityEnterView.this.lambda$new$2(view);
                }
            });
            this.attachButton.setContentDescription(LocaleController.getString("AccDescrAttachButton", R.string.AccDescrAttachButton));
        }
        if (this.audioToSend != null) {
            createRecordAudioPanel();
        }
        FrameLayout frameLayout2 = new FrameLayout(activity) { // from class: org.telegram.ui.Components.ChatActivityEnterView.19
            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                if (ChatActivityEnterView.this.sendButtonEnabled) {
                    return super.dispatchTouchEvent(motionEvent);
                }
                return false;
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                if (view == ChatActivityEnterView.this.sendButton && ChatActivityEnterView.this.textTransitionIsRunning) {
                    return true;
                }
                return super.drawChild(canvas, view, j);
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (ChatActivityEnterView.this.sendButtonEnabled) {
                    return super.onTouchEvent(motionEvent);
                }
                return false;
            }
        };
        this.sendButtonContainer = frameLayout2;
        frameLayout2.setClipChildren(false);
        this.sendButtonContainer.setClipToPadding(false);
        this.textFieldContainer.addView(this.sendButtonContainer, LayoutHelper.createFrame(48, 48, 85));
        20 r02 = new 20(activity, resourcesProvider);
        this.audioVideoButtonContainer = r02;
        r02.setSoundEffectsEnabled(false);
        this.sendButtonContainer.addView(this.audioVideoButtonContainer, LayoutHelper.createFrame(48, 48.0f));
        this.audioVideoButtonContainer.setFocusable(true);
        this.audioVideoButtonContainer.setImportantForAccessibility(1);
        ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView3 = new ChatActivityEnterViewAnimatedIconView(activity);
        this.audioVideoSendButton = chatActivityEnterViewAnimatedIconView3;
        chatActivityEnterViewAnimatedIconView3.setImportantForAccessibility(2);
        int dp2 = AndroidUtilities.dp(9.5f);
        this.audioVideoSendButton.setPadding(dp2, dp2, dp2, dp2);
        this.audioVideoSendButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(i3), mode));
        this.audioVideoButtonContainer.addView(this.audioVideoSendButton, LayoutHelper.createFrame(48, 48.0f));
        ImageView imageView5 = new ImageView(activity);
        this.cancelBotButton = imageView5;
        imageView5.setVisibility(4);
        this.cancelBotButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ImageView imageView6 = this.cancelBotButton;
        CloseProgressDrawable2 closeProgressDrawable2 = new CloseProgressDrawable2() { // from class: org.telegram.ui.Components.ChatActivityEnterView.21
            @Override // org.telegram.ui.Components.CloseProgressDrawable2
            protected int getCurrentColor() {
                return Theme.getColor(Theme.key_chat_messagePanelCancelInlineBot);
            }
        };
        this.progressDrawable = closeProgressDrawable2;
        imageView6.setImageDrawable(closeProgressDrawable2);
        this.cancelBotButton.setContentDescription(LocaleController.getString("Cancel", R.string.Cancel));
        this.cancelBotButton.setSoundEffectsEnabled(false);
        this.cancelBotButton.setScaleX(0.1f);
        this.cancelBotButton.setScaleY(0.1f);
        this.cancelBotButton.setAlpha(0.0f);
        if (i4 >= 21) {
            this.cancelBotButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
        this.sendButtonContainer.addView(this.cancelBotButton, LayoutHelper.createFrame(48, 48.0f));
        this.cancelBotButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$new$3(view);
            }
        });
        SendButton sendButton = new SendButton(activity, isInScheduleMode() ? R.drawable.input_schedule : R.drawable.ic_send, resourcesProvider) { // from class: org.telegram.ui.Components.ChatActivityEnterView.22
            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean isInScheduleMode() {
                return ChatActivityEnterView.this.isInScheduleMode();
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean isInactive() {
                return !isInScheduleMode() && ChatActivityEnterView.this.slowModeTimer == Integer.MAX_VALUE;
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean isOpen() {
                MessageSendPreview messageSendPreview = ChatActivityEnterView.this.messageSendPreview;
                return messageSendPreview != null && messageSendPreview.isShowing();
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean shouldDrawBackground() {
                return ChatActivityEnterView.this.shouldDrawBackground;
            }
        };
        this.sendButton = sendButton;
        sendButton.setVisibility(4);
        int themedColor3 = getThemedColor(Theme.key_chat_messagePanelSend);
        this.sendButton.setContentDescription(LocaleController.getString("Send", R.string.Send));
        this.sendButton.setSoundEffectsEnabled(false);
        this.sendButton.setScaleX(0.1f);
        this.sendButton.setScaleY(0.1f);
        this.sendButton.setAlpha(0.0f);
        if (i4 >= 21) {
            this.sendButton.setBackground(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(themedColor3, 24), 1));
        }
        this.sendButtonContainer.addView(this.sendButton, LayoutHelper.createFrame(48, 48.0f));
        this.sendButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$new$4(view);
            }
        });
        this.sendButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda8
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean onSendLongClick;
                onSendLongClick = ChatActivityEnterView.this.onSendLongClick(view);
                return onSendLongClick;
            }
        });
        SlowModeBtn slowModeBtn = new SlowModeBtn(activity);
        this.slowModeButton = slowModeBtn;
        slowModeBtn.setTextSize(18);
        this.slowModeButton.setVisibility(4);
        this.slowModeButton.setSoundEffectsEnabled(false);
        this.slowModeButton.setScaleX(0.1f);
        this.slowModeButton.setScaleY(0.1f);
        this.slowModeButton.setAlpha(0.0f);
        this.slowModeButton.setPadding(0, 0, AndroidUtilities.dp(10.0f), 0);
        this.slowModeButton.setGravity(21);
        this.slowModeButton.setTextColor(getThemedColor(i3));
        this.sendButtonContainer.addView(this.slowModeButton, LayoutHelper.createFrame(74, 48, 53));
        this.slowModeButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$new$5(view);
            }
        });
        this.slowModeButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda10
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$new$6;
                lambda$new$6 = ChatActivityEnterView.this.lambda$new$6(view);
                return lambda$new$6;
            }
        });
        SharedPreferences globalEmojiSettings = MessagesController.getGlobalEmojiSettings();
        this.keyboardHeight = globalEmojiSettings.getInt("kbd_height", AndroidUtilities.dp(200.0f));
        this.keyboardHeightLand = globalEmojiSettings.getInt("kbd_height_land3", AndroidUtilities.dp(200.0f));
        setRecordVideoButtonVisible(false, false);
        checkSendButton(false);
        checkChannelRights();
        createMessageEditText();
    }

    static /* synthetic */ float access$5316(ChatActivityEnterView chatActivityEnterView, float f) {
        float f2 = chatActivityEnterView.tooltipAlpha + f;
        chatActivityEnterView.tooltipAlpha = f2;
        return f2;
    }

    static /* synthetic */ float access$5324(ChatActivityEnterView chatActivityEnterView, float f) {
        float f2 = chatActivityEnterView.tooltipAlpha - f;
        chatActivityEnterView.tooltipAlpha = f2;
        return f2;
    }

    static /* synthetic */ float access$6216(ChatActivityEnterView chatActivityEnterView, float f) {
        float f2 = chatActivityEnterView.slideToCancelLockProgress + f;
        chatActivityEnterView.slideToCancelLockProgress = f2;
        return f2;
    }

    static /* synthetic */ float access$6224(ChatActivityEnterView chatActivityEnterView, float f) {
        float f2 = chatActivityEnterView.slideToCancelLockProgress - f;
        chatActivityEnterView.slideToCancelLockProgress = f2;
        return f2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void access$9900(ChatActivityEnterView chatActivityEnterView) {
        chatActivityEnterView.checkBirthdayHint();
    }

    public static CharSequence applyMessageEntities(ArrayList arrayList, CharSequence charSequence, Paint.FontMetricsInt fontMetricsInt) {
        TextStyleSpan textStyleSpan;
        int i;
        Object uRLSpanReplacement;
        int i2;
        MediaDataController.sortEntities(arrayList);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
        Object[] spans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), Object.class);
        if (spans != null && spans.length > 0) {
            for (Object obj : spans) {
                spannableStringBuilder.removeSpan(obj);
            }
        }
        if (arrayList != null) {
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                try {
                    TLRPC.MessageEntity messageEntity = (TLRPC.MessageEntity) arrayList.get(i3);
                    if (messageEntity.offset + messageEntity.length <= spannableStringBuilder.length()) {
                        if (messageEntity instanceof TLRPC.TL_inputMessageEntityMentionName) {
                            if (messageEntity.offset + messageEntity.length < spannableStringBuilder.length() && spannableStringBuilder.charAt(messageEntity.offset + messageEntity.length) == ' ') {
                                messageEntity.length++;
                            }
                            uRLSpanReplacement = new URLSpanUserMention("" + ((TLRPC.TL_inputMessageEntityMentionName) messageEntity).user_id.user_id, 3);
                            i2 = messageEntity.offset;
                        } else if (messageEntity instanceof TLRPC.TL_messageEntityMentionName) {
                            if (messageEntity.offset + messageEntity.length < spannableStringBuilder.length() && spannableStringBuilder.charAt(messageEntity.offset + messageEntity.length) == ' ') {
                                messageEntity.length++;
                            }
                            uRLSpanReplacement = new URLSpanUserMention("" + ((TLRPC.TL_messageEntityMentionName) messageEntity).user_id, 3);
                            i2 = messageEntity.offset;
                        } else {
                            if (messageEntity instanceof TLRPC.TL_messageEntityCode) {
                                TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                                textStyleRun.flags |= 4;
                                textStyleSpan = new TextStyleSpan(textStyleRun);
                                i = messageEntity.offset;
                            } else if (!(messageEntity instanceof TLRPC.TL_messageEntityPre)) {
                                if (messageEntity instanceof TLRPC.TL_messageEntityBold) {
                                    TextStyleSpan.TextStyleRun textStyleRun2 = new TextStyleSpan.TextStyleRun();
                                    textStyleRun2.flags |= 1;
                                    textStyleSpan = new TextStyleSpan(textStyleRun2);
                                    i = messageEntity.offset;
                                } else if (messageEntity instanceof TLRPC.TL_messageEntityItalic) {
                                    TextStyleSpan.TextStyleRun textStyleRun3 = new TextStyleSpan.TextStyleRun();
                                    textStyleRun3.flags |= 2;
                                    textStyleSpan = new TextStyleSpan(textStyleRun3);
                                    i = messageEntity.offset;
                                } else if (messageEntity instanceof TLRPC.TL_messageEntityStrike) {
                                    TextStyleSpan.TextStyleRun textStyleRun4 = new TextStyleSpan.TextStyleRun();
                                    textStyleRun4.flags |= 8;
                                    textStyleSpan = new TextStyleSpan(textStyleRun4);
                                    i = messageEntity.offset;
                                } else if (messageEntity instanceof TLRPC.TL_messageEntityUnderline) {
                                    TextStyleSpan.TextStyleRun textStyleRun5 = new TextStyleSpan.TextStyleRun();
                                    textStyleRun5.flags |= 16;
                                    textStyleSpan = new TextStyleSpan(textStyleRun5);
                                    i = messageEntity.offset;
                                } else if (messageEntity instanceof TLRPC.TL_messageEntityTextUrl) {
                                    uRLSpanReplacement = new URLSpanReplacement(messageEntity.url);
                                    i2 = messageEntity.offset;
                                } else if (messageEntity instanceof TLRPC.TL_messageEntitySpoiler) {
                                    TextStyleSpan.TextStyleRun textStyleRun6 = new TextStyleSpan.TextStyleRun();
                                    textStyleRun6.flags |= 256;
                                    textStyleSpan = new TextStyleSpan(textStyleRun6);
                                    i = messageEntity.offset;
                                } else if (messageEntity instanceof TLRPC.TL_messageEntityCustomEmoji) {
                                    TLRPC.TL_messageEntityCustomEmoji tL_messageEntityCustomEmoji = (TLRPC.TL_messageEntityCustomEmoji) messageEntity;
                                    AnimatedEmojiSpan animatedEmojiSpan = tL_messageEntityCustomEmoji.document != null ? new AnimatedEmojiSpan(tL_messageEntityCustomEmoji.document, fontMetricsInt) : new AnimatedEmojiSpan(tL_messageEntityCustomEmoji.document_id, fontMetricsInt);
                                    int i4 = messageEntity.offset;
                                    spannableStringBuilder.setSpan(animatedEmojiSpan, i4, messageEntity.length + i4, 33);
                                }
                            }
                            MediaDataController.addStyleToText(textStyleSpan, i, messageEntity.length + i, spannableStringBuilder, true);
                        }
                        spannableStringBuilder.setSpan(uRLSpanReplacement, i2, messageEntity.length + i2, 33);
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
        }
        QuoteSpan.mergeQuotes(spannableStringBuilder, arrayList);
        CharSequence replaceEmoji = Emoji.replaceEmoji((CharSequence) new SpannableStringBuilder(spannableStringBuilder), fontMetricsInt, false, (int[]) null);
        if (arrayList != null) {
            try {
                for (int size = arrayList.size() - 1; size >= 0; size--) {
                    TLRPC.MessageEntity messageEntity2 = (TLRPC.MessageEntity) arrayList.get(size);
                    if ((messageEntity2 instanceof TLRPC.TL_messageEntityPre) && messageEntity2.offset + messageEntity2.length <= replaceEmoji.length()) {
                        if (!(replaceEmoji instanceof Spannable)) {
                            replaceEmoji = new SpannableStringBuilder(replaceEmoji);
                        }
                        ((SpannableStringBuilder) replaceEmoji).insert(messageEntity2.offset + messageEntity2.length, (CharSequence) "```\n");
                        SpannableStringBuilder spannableStringBuilder2 = (SpannableStringBuilder) replaceEmoji;
                        int i5 = messageEntity2.offset;
                        StringBuilder sb = new StringBuilder();
                        sb.append("```");
                        String str = messageEntity2.language;
                        if (str == null) {
                            str = "";
                        }
                        sb.append(str);
                        sb.append("\n");
                        spannableStringBuilder2.insert(i5, (CharSequence) sb.toString());
                    }
                }
            } catch (Exception e2) {
                FileLog.e(e2);
            }
        }
        return replaceEmoji;
    }

    private void applyStoryToSendMessageParams(SendMessagesHelper.SendMessageParams sendMessageParams) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            sendMessageParams.replyToStoryItem = chatActivityEnterViewDelegate.getReplyToStory();
            sendMessageParams.replyQuote = this.delegate.getReplyQuote();
        }
    }

    private void attachEmojiView() {
        if (this.emojiView.getParent() == null) {
            int childCount = this.sizeNotifierLayout.getChildCount() - 5;
            if (!this.shouldDrawBackground) {
                childCount = this.sizeNotifierLayout.getChildCount();
            }
            this.sizeNotifierLayout.addView(this.emojiView, childCount);
        }
    }

    private void beginDelayedTransition() {
        HashMap hashMap = this.animationParamsX;
        ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView = this.emojiButton;
        hashMap.put(chatActivityEnterViewAnimatedIconView, Float.valueOf(chatActivityEnterViewAnimatedIconView.getX()));
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            this.animationParamsX.put(editTextCaption, Float.valueOf(editTextCaption.getX()));
        }
    }

    private BusinessLinkPresetMessage calculateBusinessLinkPresetMessage() {
        EditTextCaption editTextCaption = this.messageEditText;
        CharSequence[] charSequenceArr = {AndroidUtilities.getTrimmedString(editTextCaption == null ? "" : editTextCaption.getTextToUse())};
        ArrayList<TLRPC.MessageEntity> entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, true);
        CharSequence charSequence = charSequenceArr[0];
        int size = entities.size();
        for (int i = 0; i < size; i++) {
            TLRPC.MessageEntity messageEntity = entities.get(i);
            if (messageEntity.offset + messageEntity.length > charSequence.length()) {
                messageEntity.length = charSequence.length() - messageEntity.offset;
            }
        }
        BusinessLinkPresetMessage businessLinkPresetMessage = new BusinessLinkPresetMessage();
        businessLinkPresetMessage.text = charSequence.toString();
        businessLinkPresetMessage.entities = entities;
        return businessLinkPresetMessage;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelRecordInterfaceInternal() {
        FrameLayout frameLayout = this.recordPanel;
        if (frameLayout != null) {
            frameLayout.setVisibility(8);
        }
        RecordCircle recordCircle = this.recordCircle;
        if (recordCircle != null) {
            recordCircle.setVisibility(8);
        }
        this.runningAnimationAudio = null;
        isRecordingStateChanged();
        View view = this.recordedAudioBackground;
        if (view != null) {
            view.setAlpha(1.0f);
        }
        if (this.attachLayout != null) {
            this.attachLayoutTranslationX = 0.0f;
            updateAttachLayoutParams();
        }
        SlideTextView slideTextView = this.slideText;
        if (slideTextView != null) {
            slideTextView.setCancelToProgress(0.0f);
        }
        this.delegate.onAudioVideoInterfaceUpdated();
        updateSendAsButton();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkBirthdayHint() {
        ImageView imageView;
        ChatActivity chatActivity;
        if (this.birthdayHint != null || (imageView = this.giftButton) == null || imageView.getRight() == 0 || (chatActivity = this.parentFragment) == null || !BirthdayController.isToday(chatActivity.getCurrentUserInfo())) {
            return;
        }
        if (MessagesController.getInstance(this.currentAccount).getMainSettings().getBoolean(Calendar.getInstance().get(1) + "bdayhint_" + this.parentFragment.getDialogId(), true)) {
            MessagesController.getInstance(this.currentAccount).getMainSettings().edit().putBoolean(Calendar.getInstance().get(1) + "bdayhint_" + this.parentFragment.getDialogId(), false).apply();
            HintView2 hintView2 = new HintView2(getContext(), 3);
            this.birthdayHint = hintView2;
            hintView2.setRounding(13.0f);
            this.birthdayHint.setMultilineText(true);
            setBirthdayHintText();
            this.birthdayHint.setPadding(AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(12.0f), 0);
            this.birthdayHint.setJointPx(1.0f, -((getWidth() - AndroidUtilities.dp(12.0f)) - (((this.messageEditTextContainer.getX() + this.attachLayout.getX()) + this.giftButton.getX()) + (this.giftButton.getMeasuredWidth() / 2.0f))));
            addView(this.birthdayHint, LayoutHelper.createFrame(-1, 200.0f, 48, 0.0f, -192.0f, 0.0f, 0.0f));
            this.birthdayHint.setOnHiddenListener(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda16
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$checkBirthdayHint$60();
                }
            });
            this.birthdayHint.setDuration(8000L);
            this.birthdayHint.show();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkBotMenu() {
        EditTextCaption editTextCaption = this.messageEditText;
        boolean z = ((editTextCaption != null && !TextUtils.isEmpty(editTextCaption.getText())) || this.keyboardVisible || this.waitingForKeyboardOpen || isPopupShowing()) ? false : true;
        if (z) {
            createBotCommandsMenuButton();
        }
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        if (botCommandsMenuView != null) {
            boolean z2 = botCommandsMenuView.expanded;
            botCommandsMenuView.setExpanded(z, true);
            if (z2 != this.botCommandsMenuButton.expanded) {
                beginDelayedTransition();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:119:0x010b, code lost:
    
        if (r4 != null) goto L75;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean checkPremiumAnimatedEmoji(int i, long j, final BaseFragment baseFragment, FrameLayout frameLayout, CharSequence charSequence) {
        AnimatedEmojiSpan[] animatedEmojiSpanArr;
        int i2;
        TLRPC.ChatFull chatFull;
        TLRPC.TL_messages_stickerSet groupStickerSetById;
        ArrayList<TLRPC.Document> arrayList;
        ArrayList<TLRPC.Document> arrayList2;
        ArrayList<TLRPC.Document> arrayList3;
        if (charSequence != null && baseFragment != null && !UserConfig.getInstance(i).isPremium() && UserConfig.getInstance(i).getClientUserId() != j && (charSequence instanceof Spanned) && (animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class)) != null) {
            int i3 = 0;
            while (i3 < animatedEmojiSpanArr.length) {
                AnimatedEmojiSpan animatedEmojiSpan = animatedEmojiSpanArr[i3];
                if (animatedEmojiSpan != null) {
                    TLRPC.Document document = animatedEmojiSpan.document;
                    if (document == null) {
                        document = AnimatedEmojiDrawable.findDocument(i, animatedEmojiSpan.getDocumentId());
                    }
                    long documentId = animatedEmojiSpanArr[i3].getDocumentId();
                    if (document == null) {
                        Iterator<TLRPC.TL_messages_stickerSet> it = MediaDataController.getInstance(i).getStickerSets(5).iterator();
                        while (it.hasNext()) {
                            TLRPC.TL_messages_stickerSet next = it.next();
                            if (next != null && (arrayList3 = next.documents) != null && !arrayList3.isEmpty()) {
                                Iterator<TLRPC.Document> it2 = next.documents.iterator();
                                while (true) {
                                    if (!it2.hasNext()) {
                                        break;
                                    }
                                    TLRPC.Document next2 = it2.next();
                                    if (next2.id == documentId) {
                                        document = next2;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (document == null) {
                        Iterator<TLRPC.StickerSetCovered> it3 = MediaDataController.getInstance(i).getFeaturedEmojiSets().iterator();
                        while (it3.hasNext()) {
                            TLRPC.StickerSetCovered next3 = it3.next();
                            if (next3 != null && (arrayList2 = next3.covers) != null && !arrayList2.isEmpty()) {
                                Iterator<TLRPC.Document> it4 = next3.covers.iterator();
                                while (it4.hasNext()) {
                                    TLRPC.Document next4 = it4.next();
                                    i2 = i3;
                                    if (next4.id == documentId) {
                                        document = next4;
                                        break;
                                    }
                                    i3 = i2;
                                }
                            }
                            i2 = i3;
                            if (document != null) {
                                break;
                            }
                            if (next3 instanceof TLRPC.TL_stickerSetFullCovered) {
                                arrayList = ((TLRPC.TL_stickerSetFullCovered) next3).documents;
                            } else {
                                if ((next3 instanceof TLRPC.TL_stickerSetNoCovered) && next3.set != null) {
                                    TLRPC.TL_inputStickerSetID tL_inputStickerSetID = new TLRPC.TL_inputStickerSetID();
                                    tL_inputStickerSetID.id = next3.set.id;
                                    TLRPC.TL_messages_stickerSet stickerSet = MediaDataController.getInstance(i).getStickerSet(tL_inputStickerSetID, true);
                                    if (stickerSet != null) {
                                        arrayList = stickerSet.documents;
                                    }
                                }
                                arrayList = null;
                            }
                            if (arrayList != null && !arrayList.isEmpty()) {
                                Iterator<TLRPC.Document> it5 = arrayList.iterator();
                                while (true) {
                                    if (!it5.hasNext()) {
                                        break;
                                    }
                                    TLRPC.Document next5 = it5.next();
                                    if (next5.id == documentId) {
                                        document = next5;
                                        break;
                                    }
                                }
                            }
                            if (document != null) {
                                break;
                            }
                            i3 = i2;
                        }
                    }
                    i2 = i3;
                    if (document != null && (chatFull = MessagesController.getInstance(i).getChatFull(-j)) != null && chatFull.emojiset != null && (groupStickerSetById = MediaDataController.getInstance(i).getGroupStickerSetById(chatFull.emojiset)) != null) {
                        Iterator<TLRPC.Document> it6 = groupStickerSetById.documents.iterator();
                        while (it6.hasNext()) {
                            if (it6.next().id == documentId) {
                                return false;
                            }
                        }
                    }
                    if (document == null || !MessageObject.isFreeEmoji(document)) {
                        BulletinFactory.of(baseFragment).createEmojiBulletin(document, AndroidUtilities.replaceTags(LocaleController.getString("UnlockPremiumEmojiHint", R.string.UnlockPremiumEmojiHint)), LocaleController.getString("PremiumMore", R.string.PremiumMore), new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda68
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.lambda$checkPremiumAnimatedEmoji$47(BaseFragment.this);
                            }
                        }).show();
                        return true;
                    }
                } else {
                    i2 = i3;
                }
                i3 = i2 + 1;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:437:0x0c54  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x0c89  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void checkSendButton(boolean z) {
        int i;
        ObjectAnimator ofFloat;
        AnimatorSet animatorSet;
        AnimatorListenerAdapter animatorListenerAdapter;
        ImageView imageView;
        ImageView imageView2;
        ObjectAnimator ofFloat2;
        ImageView imageView3;
        ObjectAnimator ofFloat3;
        int i2;
        if (this.editingMessageObject != null || this.recordingAudioVideo) {
            return;
        }
        boolean z2 = this.isPaused ? false : z;
        EditTextCaption editTextCaption = this.messageEditText;
        CharSequence trimmedString = editTextCaption == null ? "" : AndroidUtilities.getTrimmedString(editTextCaption.getTextToUse());
        int i3 = this.slowModeTimer;
        float f = 1.0f;
        if (i3 <= 0 || i3 == Integer.MAX_VALUE || isInScheduleMode()) {
            if (trimmedString.length() > 0 || this.forceShowSendButton || this.audioToSend != null || this.videoToSendMessageObject != null || (this.slowModeTimer == Integer.MAX_VALUE && !isInScheduleMode())) {
                EditTextCaption editTextCaption2 = this.messageEditText;
                final String caption = editTextCaption2 == null ? null : editTextCaption2.getCaption();
                boolean z3 = caption != null && (this.sendButton.getVisibility() == 0 || ((imageView2 = this.expandStickersButton) != null && imageView2.getVisibility() == 0));
                boolean z4 = caption == null && (this.cancelBotButton.getVisibility() == 0 || ((imageView = this.expandStickersButton) != null && imageView.getVisibility() == 0));
                int themedColor = getThemedColor((this.slowModeTimer != Integer.MAX_VALUE || isInScheduleMode()) ? Theme.key_chat_messagePanelSend : Theme.key_chat_messagePanelIcons);
                if (themedColor != this.sendButtonBackgroundColor) {
                    this.sendButtonBackgroundColor = themedColor;
                    Theme.setSelectorDrawableColor(this.sendButton.getBackground(), Color.argb(24, Color.red(themedColor), Color.green(themedColor), Color.blue(themedColor)), true);
                }
                if (this.audioVideoButtonContainer.getVisibility() != 0 && this.slowModeButton.getVisibility() != 0 && !z3 && !z4) {
                    return;
                }
                if (!z2) {
                    this.audioVideoButtonContainer.setScaleX(0.1f);
                    this.audioVideoButtonContainer.setScaleY(0.1f);
                    this.audioVideoButtonContainer.setAlpha(0.0f);
                    this.audioVideoButtonContainer.setVisibility(8);
                    if (this.slowModeButton.getVisibility() == 0) {
                        this.slowModeButton.setScaleX(0.1f);
                        this.slowModeButton.setScaleY(0.1f);
                        this.slowModeButton.setAlpha(0.0f);
                        setSlowModeButtonVisible(false);
                    }
                    if (caption != null) {
                        this.sendButton.setScaleX(0.1f);
                        this.sendButton.setScaleY(0.1f);
                        this.sendButton.setAlpha(0.0f);
                        this.sendButton.setVisibility(8);
                        this.cancelBotButton.setScaleX(1.0f);
                        this.cancelBotButton.setScaleY(1.0f);
                        this.cancelBotButton.setAlpha(1.0f);
                        this.cancelBotButton.setVisibility(0);
                    } else {
                        this.cancelBotButton.setScaleX(0.1f);
                        this.cancelBotButton.setScaleY(0.1f);
                        this.cancelBotButton.setAlpha(0.0f);
                        this.sendButton.setVisibility(0);
                        this.sendButton.setScaleX(1.0f);
                        this.sendButton.setScaleY(1.0f);
                        this.sendButton.setAlpha(1.0f);
                        this.cancelBotButton.setVisibility(8);
                    }
                    ImageView imageView4 = this.expandStickersButton;
                    if (imageView4 == null || imageView4.getVisibility() != 0) {
                        i = 8;
                    } else {
                        this.expandStickersButton.setScaleX(0.1f);
                        this.expandStickersButton.setScaleY(0.1f);
                        this.expandStickersButton.setAlpha(0.0f);
                        i = 8;
                        this.expandStickersButton.setVisibility(8);
                    }
                    LinearLayout linearLayout = this.attachLayout;
                    if (linearLayout != null) {
                        linearLayout.setVisibility(i);
                        if (this.delegate != null && getVisibility() == 0) {
                            this.delegate.onAttachButtonHidden();
                        }
                        updateFieldRight(0);
                    }
                    this.scheduleButtonHidden = true;
                    if (this.scheduledButton != null) {
                        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
                        if (chatActivityEnterViewDelegate != null && chatActivityEnterViewDelegate.hasScheduledMessages()) {
                            this.scheduledButton.setVisibility(8);
                            this.scheduledButton.setTag(null);
                        }
                        this.scheduledButton.setAlpha(0.0f);
                        this.scheduledButton.setScaleX(0.0f);
                        this.scheduledButton.setScaleY(1.0f);
                        ImageView imageView5 = this.scheduledButton;
                        ImageView imageView6 = this.botButton;
                        int dp = AndroidUtilities.dp((imageView6 == null || imageView6.getVisibility() != 0) ? 48.0f : 96.0f);
                        ImageView imageView7 = this.giftButton;
                        imageView5.setTranslationX(dp - AndroidUtilities.dp((imageView7 == null || imageView7.getVisibility() != 0) ? 0.0f : 48.0f));
                        return;
                    }
                    return;
                }
                int i4 = this.runningAnimationType;
                if (i4 == 1 && caption == null) {
                    return;
                }
                if (i4 == 3 && caption != null) {
                    return;
                }
                AnimatorSet animatorSet2 = this.runningAnimation;
                if (animatorSet2 != null) {
                    animatorSet2.cancel();
                    this.runningAnimation = null;
                }
                AnimatorSet animatorSet3 = this.runningAnimation2;
                if (animatorSet3 != null) {
                    animatorSet3.cancel();
                    this.runningAnimation2 = null;
                }
                if (this.attachLayout != null) {
                    this.runningAnimation2 = new AnimatorSet();
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 0.0f));
                    LinearLayout linearLayout2 = this.attachLayout;
                    Property property = View.SCALE_X;
                    arrayList.add(ObjectAnimator.ofFloat(linearLayout2, (Property<LinearLayout, Float>) property, 0.0f));
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
                    final boolean z5 = chatActivityEnterViewDelegate2 != null && chatActivityEnterViewDelegate2.hasScheduledMessages();
                    this.scheduleButtonHidden = true;
                    ImageView imageView8 = this.scheduledButton;
                    if (imageView8 != null) {
                        imageView8.setScaleY(1.0f);
                        if (z5) {
                            this.scheduledButton.setTag(null);
                            arrayList.add(ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) View.ALPHA, 0.0f));
                            arrayList.add(ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) property, 0.0f));
                            ImageView imageView9 = this.scheduledButton;
                            Property property2 = View.TRANSLATION_X;
                            ImageView imageView10 = this.botButton;
                            int dp2 = AndroidUtilities.dp((imageView10 == null || imageView10.getVisibility() != 0) ? 48.0f : 96.0f);
                            ImageView imageView11 = this.giftButton;
                            arrayList.add(ObjectAnimator.ofFloat(imageView9, (Property<ImageView, Float>) property2, dp2 - AndroidUtilities.dp((imageView11 == null || imageView11.getVisibility() != 0) ? 0.0f : 48.0f)));
                        } else {
                            this.scheduledButton.setAlpha(0.0f);
                            this.scheduledButton.setScaleX(0.0f);
                            ImageView imageView12 = this.scheduledButton;
                            ImageView imageView13 = this.botButton;
                            int dp3 = AndroidUtilities.dp((imageView13 == null || imageView13.getVisibility() != 0) ? 48.0f : 96.0f);
                            ImageView imageView14 = this.giftButton;
                            imageView12.setTranslationX(dp3 - AndroidUtilities.dp((imageView14 == null || imageView14.getVisibility() != 0) ? 0.0f : 48.0f));
                        }
                    }
                    this.runningAnimation2.playTogether(arrayList);
                    this.runningAnimation2.setDuration(100L);
                    this.runningAnimation2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.53
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
                            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                                ChatActivityEnterView.this.runningAnimation2 = null;
                            }
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                                ChatActivityEnterView.this.attachLayout.setVisibility(8);
                                if (z5 && ChatActivityEnterView.this.scheduledButton != null) {
                                    ChatActivityEnterView.this.scheduledButton.setVisibility(8);
                                }
                                ChatActivityEnterView.this.runningAnimation2 = null;
                            }
                        }
                    });
                    this.runningAnimation2.start();
                    updateFieldRight(0);
                    if (this.delegate != null && getVisibility() == 0) {
                        this.delegate.onAttachButtonHidden();
                    }
                }
                this.runningAnimation = new AnimatorSet();
                ArrayList arrayList2 = new ArrayList();
                if (this.audioVideoButtonContainer.getVisibility() == 0) {
                    arrayList2.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) View.SCALE_X, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) View.SCALE_Y, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) View.ALPHA, 0.0f));
                }
                ImageView imageView15 = this.expandStickersButton;
                if (imageView15 != null && imageView15.getVisibility() == 0) {
                    arrayList2.add(ObjectAnimator.ofFloat(this.expandStickersButton, (Property<ImageView, Float>) View.SCALE_X, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.expandStickersButton, (Property<ImageView, Float>) View.SCALE_Y, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.expandStickersButton, (Property<ImageView, Float>) View.ALPHA, 0.0f));
                }
                if (this.slowModeButton.getVisibility() == 0) {
                    arrayList2.add(ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) View.SCALE_X, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) View.SCALE_Y, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) View.ALPHA, 0.0f));
                }
                if (z3) {
                    arrayList2.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) View.SCALE_X, 0.1f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) View.SCALE_Y, 0.1f));
                    ofFloat = ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) View.ALPHA, 0.0f);
                } else {
                    if (z4) {
                        arrayList2.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) View.SCALE_X, 0.1f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) View.SCALE_Y, 0.1f));
                        ofFloat = ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) View.ALPHA, 0.0f);
                    }
                    if (caption == null) {
                        this.runningAnimationType = 3;
                        arrayList2.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) View.SCALE_X, 1.0f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) View.SCALE_Y, 1.0f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) View.ALPHA, 1.0f));
                        this.cancelBotButton.setVisibility(0);
                    } else {
                        this.runningAnimationType = 1;
                        arrayList2.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) View.SCALE_X, 1.0f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) View.SCALE_Y, 1.0f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) View.ALPHA, 1.0f));
                        this.sendButton.setVisibility(0);
                    }
                    this.runningAnimation.playTogether(arrayList2);
                    this.runningAnimation.setDuration(150L);
                    animatorSet = this.runningAnimation;
                    animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.54
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
                            if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                                ChatActivityEnterView.this.runningAnimation = null;
                            }
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                                if (caption != null) {
                                    ChatActivityEnterView.this.cancelBotButton.setVisibility(0);
                                    ChatActivityEnterView.this.sendButton.setVisibility(8);
                                } else {
                                    ChatActivityEnterView.this.sendButton.setVisibility(0);
                                    ChatActivityEnterView.this.cancelBotButton.setVisibility(8);
                                }
                                ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(8);
                                if (ChatActivityEnterView.this.expandStickersButton != null) {
                                    ChatActivityEnterView.this.expandStickersButton.setVisibility(8);
                                }
                                ChatActivityEnterView.this.setSlowModeButtonVisible(false);
                                ChatActivityEnterView.this.runningAnimation = null;
                                ChatActivityEnterView.this.runningAnimationType = 0;
                            }
                        }
                    };
                }
                arrayList2.add(ofFloat);
                if (caption == null) {
                }
                this.runningAnimation.playTogether(arrayList2);
                this.runningAnimation.setDuration(150L);
                animatorSet = this.runningAnimation;
                animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.54
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                            ChatActivityEnterView.this.runningAnimation = null;
                        }
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                            if (caption != null) {
                                ChatActivityEnterView.this.cancelBotButton.setVisibility(0);
                                ChatActivityEnterView.this.sendButton.setVisibility(8);
                            } else {
                                ChatActivityEnterView.this.sendButton.setVisibility(0);
                                ChatActivityEnterView.this.cancelBotButton.setVisibility(8);
                            }
                            ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(8);
                            if (ChatActivityEnterView.this.expandStickersButton != null) {
                                ChatActivityEnterView.this.expandStickersButton.setVisibility(8);
                            }
                            ChatActivityEnterView.this.setSlowModeButtonVisible(false);
                            ChatActivityEnterView.this.runningAnimation = null;
                            ChatActivityEnterView.this.runningAnimationType = 0;
                        }
                    }
                };
            } else if (this.emojiView == null || !this.emojiViewVisible || (!(this.stickersTabOpen || (this.emojiTabOpen && this.searchingType == 2)) || AndroidUtilities.isInMultiwindow)) {
                if (this.sendButton.getVisibility() != 0 && this.cancelBotButton.getVisibility() != 0 && (((imageView3 = this.expandStickersButton) == null || imageView3.getVisibility() != 0) && this.slowModeButton.getVisibility() != 0)) {
                    return;
                }
                if (!z2) {
                    this.slowModeButton.setScaleX(0.1f);
                    this.slowModeButton.setScaleY(0.1f);
                    this.slowModeButton.setAlpha(0.0f);
                    setSlowModeButtonVisible(false);
                    this.sendButton.setScaleX(0.1f);
                    this.sendButton.setScaleY(0.1f);
                    this.sendButton.setAlpha(0.0f);
                    this.sendButton.setVisibility(8);
                    this.cancelBotButton.setScaleX(0.1f);
                    this.cancelBotButton.setScaleY(0.1f);
                    this.cancelBotButton.setAlpha(0.0f);
                    this.cancelBotButton.setVisibility(8);
                    ImageView imageView16 = this.expandStickersButton;
                    if (imageView16 != null) {
                        imageView16.setScaleX(0.1f);
                        this.expandStickersButton.setScaleY(0.1f);
                        this.expandStickersButton.setAlpha(0.0f);
                        this.expandStickersButton.setVisibility(8);
                    }
                    this.audioVideoButtonContainer.setScaleX(1.0f);
                    this.audioVideoButtonContainer.setScaleY(1.0f);
                    this.audioVideoButtonContainer.setAlpha(1.0f);
                    this.audioVideoButtonContainer.setVisibility(0);
                    if (this.attachLayout != null) {
                        if (getVisibility() == 0) {
                            this.delegate.onAttachButtonShow();
                        }
                        this.attachLayoutAlpha = 1.0f;
                        updateAttachLayoutParams();
                        this.attachLayout.setScaleX(1.0f);
                        this.attachLayout.setVisibility(0);
                        updateFieldRight(1);
                    }
                    this.scheduleButtonHidden = false;
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate3 = this.delegate;
                    if (chatActivityEnterViewDelegate3 != null && chatActivityEnterViewDelegate3.hasScheduledMessages()) {
                        createScheduledButton();
                    }
                    if (this.scheduledButton != null) {
                        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate4 = this.delegate;
                        if (chatActivityEnterViewDelegate4 != null && chatActivityEnterViewDelegate4.hasScheduledMessages()) {
                            this.scheduledButton.setVisibility(0);
                            this.scheduledButton.setTag(1);
                        }
                        this.scheduledButton.setAlpha(1.0f);
                        this.scheduledButton.setScaleX(1.0f);
                        this.scheduledButton.setScaleY(1.0f);
                        this.scheduledButton.setTranslationX(0.0f);
                        return;
                    }
                    return;
                }
                if (this.runningAnimationType == 2) {
                    return;
                }
                AnimatorSet animatorSet4 = this.runningAnimation;
                if (animatorSet4 != null) {
                    animatorSet4.cancel();
                    this.runningAnimation = null;
                }
                AnimatorSet animatorSet5 = this.runningAnimation2;
                if (animatorSet5 != null) {
                    animatorSet5.cancel();
                    this.runningAnimation2 = null;
                }
                LinearLayout linearLayout3 = this.attachLayout;
                if (linearLayout3 != null) {
                    if (linearLayout3.getVisibility() != 0) {
                        this.attachLayout.setVisibility(0);
                        this.attachLayoutAlpha = 0.0f;
                        updateAttachLayoutParams();
                        this.attachLayout.setScaleX(0.0f);
                    }
                    this.runningAnimation2 = new AnimatorSet();
                    ArrayList arrayList3 = new ArrayList();
                    arrayList3.add(ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 1.0f));
                    LinearLayout linearLayout4 = this.attachLayout;
                    Property property3 = View.SCALE_X;
                    arrayList3.add(ObjectAnimator.ofFloat(linearLayout4, (Property<LinearLayout, Float>) property3, 1.0f));
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate5 = this.delegate;
                    boolean z6 = chatActivityEnterViewDelegate5 != null && chatActivityEnterViewDelegate5.hasScheduledMessages();
                    this.scheduleButtonHidden = false;
                    if (z6) {
                        createScheduledButton();
                    }
                    ImageView imageView17 = this.scheduledButton;
                    if (imageView17 != null) {
                        if (z6) {
                            imageView17.setVisibility(0);
                            this.scheduledButton.setTag(1);
                            this.scheduledButton.setPivotX(AndroidUtilities.dp(48.0f));
                            arrayList3.add(ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) View.ALPHA, 1.0f));
                            arrayList3.add(ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) property3, 1.0f));
                            ImageView imageView18 = this.scheduledButton;
                            Property property4 = View.TRANSLATION_X;
                            ImageView imageView19 = this.giftButton;
                            arrayList3.add(ObjectAnimator.ofFloat(imageView18, (Property<ImageView, Float>) property4, (imageView19 == null || imageView19.getVisibility() != 0) ? 0.0f : -AndroidUtilities.dp(48.0f)));
                            ImageView imageView20 = this.notifyButton;
                            if (imageView20 != null && imageView20.getVisibility() == 0) {
                                this.notifyButton.setVisibility(8);
                            }
                        } else {
                            imageView17.setAlpha(1.0f);
                            this.scheduledButton.setScaleX(1.0f);
                            this.scheduledButton.setScaleY(1.0f);
                            this.scheduledButton.setTranslationX(0.0f);
                        }
                    }
                    this.runningAnimation2.playTogether(arrayList3);
                    this.runningAnimation2.setDuration(100L);
                    this.runningAnimation2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.57
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
                            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                                ChatActivityEnterView.this.runningAnimation2 = null;
                            }
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                                ChatActivityEnterView.this.runningAnimation2 = null;
                            }
                        }
                    });
                    this.runningAnimation2.start();
                    updateFieldRight(1);
                    if (getVisibility() == 0) {
                        this.delegate.onAttachButtonShow();
                    }
                }
                this.audioVideoButtonContainer.setVisibility(0);
                this.runningAnimation = new AnimatorSet();
                this.runningAnimationType = 2;
                ArrayList arrayList4 = new ArrayList();
                FrameLayout frameLayout = this.audioVideoButtonContainer;
                Property property5 = View.SCALE_X;
                arrayList4.add(ObjectAnimator.ofFloat(frameLayout, (Property<FrameLayout, Float>) property5, 1.0f));
                FrameLayout frameLayout2 = this.audioVideoButtonContainer;
                Property property6 = View.SCALE_Y;
                arrayList4.add(ObjectAnimator.ofFloat(frameLayout2, (Property<FrameLayout, Float>) property6, 1.0f));
                ChatActivity chatActivity = this.parentFragment;
                TLRPC.Chat currentChat = chatActivity != null ? chatActivity.getCurrentChat() : null;
                ChatActivity chatActivity2 = this.parentFragment;
                TLRPC.UserFull currentUserInfo = chatActivity2 == null ? this.userInfo : chatActivity2.getCurrentUserInfo();
                if (currentChat == null ? !(currentUserInfo == null || !currentUserInfo.voice_messages_forbidden) : !(ChatObject.canSendVoice(currentChat) || ChatObject.canSendRoundVideo(currentChat))) {
                    f = 0.5f;
                }
                FrameLayout frameLayout3 = this.audioVideoButtonContainer;
                Property property7 = View.ALPHA;
                arrayList4.add(ObjectAnimator.ofFloat(frameLayout3, (Property<FrameLayout, Float>) property7, f));
                if (this.cancelBotButton.getVisibility() == 0) {
                    arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) property5, 0.1f));
                    arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) property6, 0.1f));
                    ofFloat2 = ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) property7, 0.0f);
                } else {
                    ImageView imageView21 = this.expandStickersButton;
                    if (imageView21 != null && imageView21.getVisibility() == 0) {
                        arrayList4.add(ObjectAnimator.ofFloat(this.expandStickersButton, (Property<ImageView, Float>) property5, 0.1f));
                        arrayList4.add(ObjectAnimator.ofFloat(this.expandStickersButton, (Property<ImageView, Float>) property6, 0.1f));
                        ofFloat2 = ObjectAnimator.ofFloat(this.expandStickersButton, (Property<ImageView, Float>) property7, 0.0f);
                    } else if (this.slowModeButton.getVisibility() == 0) {
                        arrayList4.add(ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) property5, 0.1f));
                        arrayList4.add(ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) property6, 0.1f));
                        ofFloat2 = ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) property7, 0.0f);
                    } else {
                        arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) property5, 0.1f));
                        arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) property6, 0.1f));
                        ofFloat2 = ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) property7, 0.0f);
                    }
                }
                arrayList4.add(ofFloat2);
                this.runningAnimation.playTogether(arrayList4);
                this.runningAnimation.setDuration(150L);
                animatorSet = this.runningAnimation;
                animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.58
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                            ChatActivityEnterView.this.runningAnimation = null;
                        }
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                            ChatActivityEnterView.this.setSlowModeButtonVisible(false);
                            ChatActivityEnterView.this.runningAnimation = null;
                            ChatActivityEnterView.this.runningAnimationType = 0;
                            if (ChatActivityEnterView.this.audioVideoButtonContainer != null) {
                                ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(0);
                            }
                        }
                    }
                };
            } else {
                if (!z2) {
                    this.slowModeButton.setScaleX(0.1f);
                    this.slowModeButton.setScaleY(0.1f);
                    this.slowModeButton.setAlpha(0.0f);
                    setSlowModeButtonVisible(false);
                    this.sendButton.setScaleX(0.1f);
                    this.sendButton.setScaleY(0.1f);
                    this.sendButton.setAlpha(0.0f);
                    this.sendButton.setVisibility(8);
                    this.cancelBotButton.setScaleX(0.1f);
                    this.cancelBotButton.setScaleY(0.1f);
                    this.cancelBotButton.setAlpha(0.0f);
                    this.cancelBotButton.setVisibility(8);
                    this.audioVideoButtonContainer.setScaleX(0.1f);
                    this.audioVideoButtonContainer.setScaleY(0.1f);
                    this.audioVideoButtonContainer.setAlpha(0.0f);
                    this.audioVideoButtonContainer.setVisibility(8);
                    createExpandStickersButton();
                    this.expandStickersButton.setScaleX(1.0f);
                    this.expandStickersButton.setScaleY(1.0f);
                    this.expandStickersButton.setAlpha(1.0f);
                    this.expandStickersButton.setVisibility(0);
                    if (this.attachLayout != null) {
                        if (getVisibility() == 0) {
                            this.delegate.onAttachButtonShow();
                        }
                        this.attachLayout.setVisibility(0);
                        updateFieldRight(1);
                    }
                    this.scheduleButtonHidden = false;
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate6 = this.delegate;
                    boolean z7 = chatActivityEnterViewDelegate6 != null && chatActivityEnterViewDelegate6.hasScheduledMessages();
                    if (z7) {
                        createScheduledButton();
                    }
                    ImageView imageView22 = this.scheduledButton;
                    if (imageView22 != null) {
                        if (z7) {
                            imageView22.setVisibility(0);
                            this.scheduledButton.setTag(1);
                        }
                        this.scheduledButton.setAlpha(1.0f);
                        this.scheduledButton.setScaleX(1.0f);
                        this.scheduledButton.setScaleY(1.0f);
                        this.scheduledButton.setTranslationX(0.0f);
                        return;
                    }
                    return;
                }
                if (this.runningAnimationType == 4) {
                    return;
                }
                AnimatorSet animatorSet6 = this.runningAnimation;
                if (animatorSet6 != null) {
                    animatorSet6.cancel();
                    this.runningAnimation = null;
                }
                AnimatorSet animatorSet7 = this.runningAnimation2;
                if (animatorSet7 != null) {
                    animatorSet7.cancel();
                    this.runningAnimation2 = null;
                }
                LinearLayout linearLayout5 = this.attachLayout;
                if (linearLayout5 != null && this.recordInterfaceState == 0) {
                    linearLayout5.setVisibility(0);
                    this.runningAnimation2 = new AnimatorSet();
                    ArrayList arrayList5 = new ArrayList();
                    arrayList5.add(ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 1.0f));
                    LinearLayout linearLayout6 = this.attachLayout;
                    Property property8 = View.SCALE_X;
                    arrayList5.add(ObjectAnimator.ofFloat(linearLayout6, (Property<LinearLayout, Float>) property8, 1.0f));
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate7 = this.delegate;
                    boolean z8 = chatActivityEnterViewDelegate7 != null && chatActivityEnterViewDelegate7.hasScheduledMessages();
                    this.scheduleButtonHidden = false;
                    if (z8) {
                        createScheduledButton();
                    }
                    ImageView imageView23 = this.scheduledButton;
                    if (imageView23 != null) {
                        imageView23.setScaleY(1.0f);
                        if (z8) {
                            this.scheduledButton.setVisibility(0);
                            this.scheduledButton.setTag(1);
                            this.scheduledButton.setPivotX(AndroidUtilities.dp(48.0f));
                            arrayList5.add(ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) View.ALPHA, 1.0f));
                            arrayList5.add(ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) property8, 1.0f));
                            arrayList5.add(ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) View.TRANSLATION_X, 0.0f));
                        } else {
                            this.scheduledButton.setAlpha(1.0f);
                            this.scheduledButton.setScaleX(1.0f);
                            this.scheduledButton.setTranslationX(0.0f);
                        }
                    }
                    this.runningAnimation2.playTogether(arrayList5);
                    this.runningAnimation2.setDuration(100L);
                    this.runningAnimation2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.55
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
                            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                                ChatActivityEnterView.this.runningAnimation2 = null;
                            }
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                                ChatActivityEnterView.this.runningAnimation2 = null;
                            }
                        }
                    });
                    this.runningAnimation2.start();
                    updateFieldRight(1);
                    if (getVisibility() == 0) {
                        this.delegate.onAttachButtonShow();
                    }
                }
                createExpandStickersButton();
                this.expandStickersButton.setVisibility(0);
                this.runningAnimation = new AnimatorSet();
                this.runningAnimationType = 4;
                ArrayList arrayList6 = new ArrayList();
                ImageView imageView24 = this.expandStickersButton;
                Property property9 = View.SCALE_X;
                arrayList6.add(ObjectAnimator.ofFloat(imageView24, (Property<ImageView, Float>) property9, 1.0f));
                ImageView imageView25 = this.expandStickersButton;
                Property property10 = View.SCALE_Y;
                arrayList6.add(ObjectAnimator.ofFloat(imageView25, (Property<ImageView, Float>) property10, 1.0f));
                ImageView imageView26 = this.expandStickersButton;
                Property property11 = View.ALPHA;
                arrayList6.add(ObjectAnimator.ofFloat(imageView26, (Property<ImageView, Float>) property11, 1.0f));
                if (this.cancelBotButton.getVisibility() == 0) {
                    arrayList6.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) property9, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) property10, 0.1f));
                    ofFloat3 = ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) property11, 0.0f);
                } else if (this.audioVideoButtonContainer.getVisibility() == 0) {
                    arrayList6.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) property9, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) property10, 0.1f));
                    ofFloat3 = ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) property11, 0.0f);
                } else if (this.slowModeButton.getVisibility() == 0) {
                    arrayList6.add(ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) property9, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) property10, 0.1f));
                    ofFloat3 = ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) property11, 0.0f);
                } else {
                    arrayList6.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) property9, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) property10, 0.1f));
                    ofFloat3 = ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) property11, 0.0f);
                }
                arrayList6.add(ofFloat3);
                this.runningAnimation.playTogether(arrayList6);
                this.runningAnimation.setDuration(250L);
                animatorSet = this.runningAnimation;
                animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.56
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                            ChatActivityEnterView.this.runningAnimation = null;
                        }
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                            ChatActivityEnterView.this.sendButton.setVisibility(8);
                            ChatActivityEnterView.this.cancelBotButton.setVisibility(8);
                            ChatActivityEnterView.this.setSlowModeButtonVisible(false);
                            ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(8);
                            ChatActivityEnterView.this.expandStickersButton.setVisibility(0);
                            ChatActivityEnterView.this.runningAnimation = null;
                            ChatActivityEnterView.this.runningAnimationType = 0;
                        }
                    }
                };
            }
        } else {
            if (this.slowModeButton.getVisibility() == 0) {
                return;
            }
            if (!z2) {
                this.slowModeButton.setScaleX(1.0f);
                this.slowModeButton.setScaleY(1.0f);
                this.slowModeButton.setAlpha(1.0f);
                setSlowModeButtonVisible(true);
                this.audioVideoButtonContainer.setScaleX(0.1f);
                this.audioVideoButtonContainer.setScaleY(0.1f);
                this.audioVideoButtonContainer.setAlpha(0.0f);
                this.audioVideoButtonContainer.setVisibility(8);
                this.sendButton.setScaleX(0.1f);
                this.sendButton.setScaleY(0.1f);
                this.sendButton.setAlpha(0.0f);
                this.sendButton.setVisibility(8);
                this.cancelBotButton.setScaleX(0.1f);
                this.cancelBotButton.setScaleY(0.1f);
                this.cancelBotButton.setAlpha(0.0f);
                this.cancelBotButton.setVisibility(8);
                ImageView imageView27 = this.expandStickersButton;
                if (imageView27 == null || imageView27.getVisibility() != 0) {
                    i2 = 8;
                } else {
                    this.expandStickersButton.setScaleX(0.1f);
                    this.expandStickersButton.setScaleY(0.1f);
                    this.expandStickersButton.setAlpha(0.0f);
                    i2 = 8;
                    this.expandStickersButton.setVisibility(8);
                }
                LinearLayout linearLayout7 = this.attachLayout;
                if (linearLayout7 != null) {
                    linearLayout7.setVisibility(i2);
                    if (this.delegate != null && getVisibility() == 0) {
                        this.delegate.onAttachButtonHidden();
                    }
                    updateFieldRight(0);
                }
                this.scheduleButtonHidden = false;
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate8 = this.delegate;
                boolean z9 = chatActivityEnterViewDelegate8 != null && chatActivityEnterViewDelegate8.hasScheduledMessages();
                if (z9) {
                    createScheduledButton();
                }
                ImageView imageView28 = this.scheduledButton;
                if (imageView28 != null) {
                    if (z9) {
                        imageView28.setVisibility(0);
                        this.scheduledButton.setTag(1);
                    }
                    ImageView imageView29 = this.scheduledButton;
                    ImageView imageView30 = this.botButton;
                    int dp4 = AndroidUtilities.dp((imageView30 == null || imageView30.getVisibility() != 0) ? 48.0f : 96.0f);
                    ImageView imageView31 = this.giftButton;
                    imageView29.setTranslationX(dp4 - AndroidUtilities.dp((imageView31 == null || imageView31.getVisibility() != 0) ? 0.0f : 48.0f));
                    this.scheduledButton.setAlpha(1.0f);
                    this.scheduledButton.setScaleX(1.0f);
                    this.scheduledButton.setScaleY(1.0f);
                    return;
                }
                return;
            }
            if (this.runningAnimationType == 5) {
                return;
            }
            AnimatorSet animatorSet8 = this.runningAnimation;
            if (animatorSet8 != null) {
                animatorSet8.cancel();
                this.runningAnimation = null;
            }
            AnimatorSet animatorSet9 = this.runningAnimation2;
            if (animatorSet9 != null) {
                animatorSet9.cancel();
                this.runningAnimation2 = null;
            }
            if (this.attachLayout != null) {
                this.runningAnimation2 = new AnimatorSet();
                ArrayList arrayList7 = new ArrayList();
                arrayList7.add(ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 0.0f));
                LinearLayout linearLayout8 = this.attachLayout;
                Property property12 = View.SCALE_X;
                arrayList7.add(ObjectAnimator.ofFloat(linearLayout8, (Property<LinearLayout, Float>) property12, 0.0f));
                this.scheduleButtonHidden = false;
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate9 = this.delegate;
                boolean z10 = chatActivityEnterViewDelegate9 != null && chatActivityEnterViewDelegate9.hasScheduledMessages();
                if (z10) {
                    createScheduledButton();
                }
                ImageView imageView32 = this.scheduledButton;
                if (imageView32 != null) {
                    imageView32.setScaleY(1.0f);
                    if (z10) {
                        this.scheduledButton.setVisibility(0);
                        this.scheduledButton.setTag(1);
                        this.scheduledButton.setPivotX(AndroidUtilities.dp(48.0f));
                        ImageView imageView33 = this.scheduledButton;
                        Property property13 = View.TRANSLATION_X;
                        ImageView imageView34 = this.botButton;
                        int dp5 = AndroidUtilities.dp((imageView34 == null || imageView34.getVisibility() != 0) ? 48.0f : 96.0f);
                        ImageView imageView35 = this.giftButton;
                        arrayList7.add(ObjectAnimator.ofFloat(imageView33, (Property<ImageView, Float>) property13, dp5 - AndroidUtilities.dp((imageView35 == null || imageView35.getVisibility() != 0) ? 0.0f : 48.0f)));
                        arrayList7.add(ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) View.ALPHA, 1.0f));
                        arrayList7.add(ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) property12, 1.0f));
                    } else {
                        ImageView imageView36 = this.scheduledButton;
                        ImageView imageView37 = this.botButton;
                        int dp6 = AndroidUtilities.dp((imageView37 == null || imageView37.getVisibility() != 0) ? 48.0f : 96.0f);
                        ImageView imageView38 = this.giftButton;
                        imageView36.setTranslationX(dp6 - AndroidUtilities.dp((imageView38 == null || imageView38.getVisibility() != 0) ? 0.0f : 48.0f));
                        this.scheduledButton.setAlpha(1.0f);
                        this.scheduledButton.setScaleX(1.0f);
                    }
                }
                this.runningAnimation2.playTogether(arrayList7);
                this.runningAnimation2.setDuration(100L);
                this.runningAnimation2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.51
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                            ChatActivityEnterView.this.runningAnimation2 = null;
                        }
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                            ChatActivityEnterView.this.attachLayout.setVisibility(8);
                            ChatActivityEnterView.this.runningAnimation2 = null;
                        }
                    }
                });
                this.runningAnimation2.start();
                updateFieldRight(0);
                if (this.delegate != null && getVisibility() == 0) {
                    this.delegate.onAttachButtonHidden();
                }
            }
            this.runningAnimationType = 5;
            this.runningAnimation = new AnimatorSet();
            ArrayList arrayList8 = new ArrayList();
            if (this.audioVideoButtonContainer.getVisibility() == 0) {
                arrayList8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) View.SCALE_X, 0.1f));
                arrayList8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) View.SCALE_Y, 0.1f));
                arrayList8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) View.ALPHA, 0.0f));
            }
            ImageView imageView39 = this.expandStickersButton;
            if (imageView39 != null && imageView39.getVisibility() == 0) {
                arrayList8.add(ObjectAnimator.ofFloat(this.expandStickersButton, (Property<ImageView, Float>) View.SCALE_X, 0.1f));
                arrayList8.add(ObjectAnimator.ofFloat(this.expandStickersButton, (Property<ImageView, Float>) View.SCALE_Y, 0.1f));
                arrayList8.add(ObjectAnimator.ofFloat(this.expandStickersButton, (Property<ImageView, Float>) View.ALPHA, 0.0f));
            }
            if (this.sendButton.getVisibility() == 0) {
                arrayList8.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) View.SCALE_X, 0.1f));
                arrayList8.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) View.SCALE_Y, 0.1f));
                arrayList8.add(ObjectAnimator.ofFloat(this.sendButton, (Property<SendButton, Float>) View.ALPHA, 0.0f));
            }
            if (this.cancelBotButton.getVisibility() == 0) {
                arrayList8.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) View.SCALE_X, 0.1f));
                arrayList8.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) View.SCALE_Y, 0.1f));
                arrayList8.add(ObjectAnimator.ofFloat(this.cancelBotButton, (Property<ImageView, Float>) View.ALPHA, 0.0f));
            }
            arrayList8.add(ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) View.SCALE_X, 1.0f));
            arrayList8.add(ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) View.SCALE_Y, 1.0f));
            arrayList8.add(ObjectAnimator.ofFloat(this.slowModeButton, (Property<SlowModeBtn, Float>) View.ALPHA, 1.0f));
            setSlowModeButtonVisible(true);
            this.runningAnimation.playTogether(arrayList8);
            this.runningAnimation.setDuration(150L);
            animatorSet = this.runningAnimation;
            animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.52
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                        ChatActivityEnterView.this.runningAnimation = null;
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                        ChatActivityEnterView.this.sendButton.setVisibility(8);
                        ChatActivityEnterView.this.cancelBotButton.setVisibility(8);
                        ChatActivityEnterView.this.audioVideoButtonContainer.setVisibility(8);
                        if (ChatActivityEnterView.this.expandStickersButton != null) {
                            ChatActivityEnterView.this.expandStickersButton.setVisibility(8);
                        }
                        ChatActivityEnterView.this.runningAnimation = null;
                        ChatActivityEnterView.this.runningAnimationType = 0;
                    }
                }
            };
        }
        animatorSet.addListener(animatorListenerAdapter);
        this.runningAnimation.start();
    }

    private void createBotButton() {
        if (this.botButton != null) {
            return;
        }
        ImageView imageView = new ImageView(getContext());
        this.botButton = imageView;
        ReplaceableIconDrawable replaceableIconDrawable = new ReplaceableIconDrawable(getContext());
        this.botButtonDrawable = replaceableIconDrawable;
        imageView.setImageDrawable(replaceableIconDrawable);
        this.botButtonDrawable.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_messagePanelIcons), PorterDuff.Mode.MULTIPLY));
        this.botButtonDrawable.setIcon(R.drawable.input_bot2, false);
        this.botButton.setScaleType(ImageView.ScaleType.CENTER);
        if (Build.VERSION.SDK_INT >= 21) {
            this.botButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
        this.botButton.setVisibility(8);
        AndroidUtilities.updateViewVisibilityAnimated(this.botButton, false, 0.1f, false);
        this.attachLayout.addView(this.botButton, 0, LayoutHelper.createLinear(48, 48));
        this.botButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda28
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createBotButton$11(view);
            }
        });
    }

    private void createBotCommandsMenuButton() {
        if (this.botCommandsMenuButton != null) {
            return;
        }
        BotCommandsMenuView botCommandsMenuView = new BotCommandsMenuView(getContext());
        this.botCommandsMenuButton = botCommandsMenuView;
        botCommandsMenuView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda30
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createBotCommandsMenuButton$22(view);
            }
        });
        this.messageEditTextContainer.addView(this.botCommandsMenuButton, LayoutHelper.createFrame(-2, 32.0f, 83, 10.0f, 8.0f, 10.0f, 8.0f));
        AndroidUtilities.updateViewVisibilityAnimated(this.botCommandsMenuButton, false, 1.0f, false);
        this.botCommandsMenuButton.setExpanded(true, false);
    }

    private void createBotCommandsMenuContainer() {
        if (this.botCommandsMenuContainer != null) {
            return;
        }
        BotCommandsMenuContainer botCommandsMenuContainer = new BotCommandsMenuContainer(getContext()) { // from class: org.telegram.ui.Components.ChatActivityEnterView.39
            boolean ignoreLayout = false;

            @Override // org.telegram.ui.bots.BotCommandsMenuContainer
            protected void onDismiss() {
                super.onDismiss();
                if (ChatActivityEnterView.this.botCommandsMenuButton != null) {
                    ChatActivityEnterView.this.botCommandsMenuButton.setOpened(false);
                }
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
                super.onLayout(z, i, i2, i3, i4);
                if (this.ignoreLayout) {
                    return;
                }
                this.ignoreLayout = true;
                ChatActivityEnterView.this.updateBotCommandsMenuContainerTopPadding();
            }
        };
        this.botCommandsMenuContainer = botCommandsMenuContainer;
        botCommandsMenuContainer.listView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerListView recyclerListView = this.botCommandsMenuContainer.listView;
        BotCommandsMenuView.BotCommandsAdapter botCommandsAdapter = new BotCommandsMenuView.BotCommandsAdapter();
        this.botCommandsAdapter = botCommandsAdapter;
        recyclerListView.setAdapter(botCommandsAdapter);
        this.botCommandsMenuContainer.listView.setOnItemClickListener(new 40());
        this.botCommandsMenuContainer.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.41
            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public boolean onItemClick(View view, int i) {
                if (!(view instanceof BotCommandsMenuView.BotCommandView)) {
                    return false;
                }
                String command = ((BotCommandsMenuView.BotCommandView) view).getCommand();
                ChatActivityEnterView.this.setFieldText(command + " ");
                ChatActivityEnterView.this.botCommandsMenuContainer.dismiss();
                return true;
            }
        });
        this.botCommandsMenuContainer.setClipToPadding(false);
        this.sizeNotifierLayout.addView(this.botCommandsMenuContainer, 14, LayoutHelper.createFrame(-1, -1, 80));
        this.botCommandsMenuContainer.setVisibility(8);
        LongSparseArray longSparseArray = this.lastBotInfo;
        if (longSparseArray != null) {
            this.botCommandsAdapter.setBotInfo(longSparseArray);
        }
        updateBotCommandsMenuContainerTopPadding();
    }

    private void createBotWebViewButton() {
        if (this.botWebViewButton != null) {
            return;
        }
        ChatActivityBotWebViewButton chatActivityBotWebViewButton = new ChatActivityBotWebViewButton(getContext());
        this.botWebViewButton = chatActivityBotWebViewButton;
        chatActivityBotWebViewButton.setVisibility(8);
        createBotCommandsMenuButton();
        this.botWebViewButton.setBotMenuButton(this.botCommandsMenuButton);
        this.messageEditTextContainer.addView(this.botWebViewButton, LayoutHelper.createFrame(-1, -1, 80));
    }

    private void createBotWebViewMenuContainer() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createCaptionLimitView() {
        if (this.captionLimitView != null) {
            return;
        }
        NumberTextView numberTextView = new NumberTextView(getContext());
        this.captionLimitView = numberTextView;
        numberTextView.setVisibility(8);
        this.captionLimitView.setTextSize(15);
        this.captionLimitView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
        this.captionLimitView.setTypeface(AndroidUtilities.bold());
        this.captionLimitView.setCenterAlign(true);
        addView(this.captionLimitView, 3, LayoutHelper.createFrame(48, 20.0f, 85, 3.0f, 0.0f, 0.0f, 48.0f));
    }

    private void createControlsView() {
        if (this.controlsView != null) {
            return;
        }
        ControlsView controlsView = new ControlsView(getContext());
        this.controlsView = controlsView;
        controlsView.setVisibility(8);
        this.sizeNotifierLayout.addView(this.controlsView, LayoutHelper.createFrame(-1, -2, 80));
    }

    private void createDoneButton(boolean z) {
        if (this.doneButton != null) {
            return;
        }
        SendButton sendButton = new SendButton(getContext(), R.drawable.input_done, this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatActivityEnterView.24
            @Override // org.telegram.ui.Components.ChatActivityEnterView.SendButton
            public boolean isInactive() {
                return !ChatActivityEnterView.this.doneButtonEnabled;
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
        this.doneButton = sendButton;
        sendButton.center = true;
        if (z) {
            ScaleStateListAnimator.apply(sendButton);
        }
        this.textFieldContainer.addView(this.doneButton, LayoutHelper.createFrame(38, 38.0f, 85, 5.0f, 5.0f, 5.0f, 5.0f));
    }

    private void createEmojiView() {
        EmojiView emojiView = this.emojiView;
        if (emojiView != null && emojiView.currentAccount != UserConfig.selectedAccount) {
            this.sizeNotifierLayout.removeView(emojiView);
            this.emojiView = null;
        }
        if (this.emojiView != null) {
            return;
        }
        EmojiView emojiView2 = new EmojiView(this.parentFragment, this.allowAnimatedEmoji, true, true, getContext(), true, this.info, this.sizeNotifierLayout, this.shouldDrawBackground, this.resourcesProvider, this.emojiViewFrozen) { // from class: org.telegram.ui.Components.ChatActivityEnterView.71
            @Override // org.telegram.ui.Components.EmojiView, android.view.View
            public void setTranslationY(float f) {
                super.setTranslationY(f);
                if (ChatActivityEnterView.this.panelAnimation == null || ChatActivityEnterView.this.animatingContentType != 0) {
                    return;
                }
                ChatActivityEnterView.this.delegate.bottomPanelTranslationYChanged(f);
            }
        };
        this.emojiView = emojiView2;
        if (!this.shouldDrawBackground) {
            emojiView2.updateColors();
        }
        this.emojiView.setAllow(this.allowStickers, this.allowGifs, true);
        this.emojiView.setVisibility(8);
        this.emojiView.setShowing(false);
        this.emojiView.setDelegate(new 72());
        this.emojiView.setDragListener(new EmojiView.DragListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.73
            int initialOffset;
            boolean wasExpanded;

            private boolean allowDragging() {
                EditTextCaption editTextCaption;
                return ChatActivityEnterView.this.stickersTabOpen && (ChatActivityEnterView.this.stickersExpanded || (editTextCaption = ChatActivityEnterView.this.messageEditText) == null || editTextCaption.length() <= 0) && ChatActivityEnterView.this.emojiView.areThereAnyStickers() && !ChatActivityEnterView.this.waitingForKeyboardOpen;
            }

            @Override // org.telegram.ui.Components.EmojiView.DragListener
            public void onDrag(int i) {
                if (allowDragging()) {
                    android.graphics.Point point = AndroidUtilities.displaySize;
                    float max = Math.max(Math.min(i + this.initialOffset, 0), -(ChatActivityEnterView.this.stickersExpandedHeight - (point.x > point.y ? ChatActivityEnterView.this.keyboardHeightLand : ChatActivityEnterView.this.keyboardHeight)));
                    ChatActivityEnterView.this.emojiView.setTranslationY(max);
                    ChatActivityEnterView.this.setTranslationY(max);
                    ChatActivityEnterView.this.stickersExpansionProgress = max / (-(r1.stickersExpandedHeight - r0));
                    ChatActivityEnterView.this.sizeNotifierLayout.invalidate();
                }
            }

            @Override // org.telegram.ui.Components.EmojiView.DragListener
            public void onDragCancel() {
                if (ChatActivityEnterView.this.stickersTabOpen) {
                    ChatActivityEnterView.this.stickersDragging = false;
                    ChatActivityEnterView.this.setStickersExpanded(this.wasExpanded, true, false);
                }
            }

            @Override // org.telegram.ui.Components.EmojiView.DragListener
            public void onDragEnd(float f) {
                ChatActivityEnterView chatActivityEnterView;
                boolean z;
                if (allowDragging()) {
                    ChatActivityEnterView.this.stickersDragging = false;
                    if ((!this.wasExpanded || f < AndroidUtilities.dp(200.0f)) && ((this.wasExpanded || f > AndroidUtilities.dp(-200.0f)) && ((!this.wasExpanded || ChatActivityEnterView.this.stickersExpansionProgress > 0.6f) && (this.wasExpanded || ChatActivityEnterView.this.stickersExpansionProgress < 0.4f)))) {
                        chatActivityEnterView = ChatActivityEnterView.this;
                        z = this.wasExpanded;
                    } else {
                        chatActivityEnterView = ChatActivityEnterView.this;
                        z = !this.wasExpanded;
                    }
                    chatActivityEnterView.setStickersExpanded(z, true, true);
                }
            }

            @Override // org.telegram.ui.Components.EmojiView.DragListener
            public void onDragStart() {
                if (allowDragging()) {
                    if (ChatActivityEnterView.this.stickersExpansionAnim != null) {
                        ChatActivityEnterView.this.stickersExpansionAnim.cancel();
                    }
                    ChatActivityEnterView.this.stickersDragging = true;
                    this.wasExpanded = ChatActivityEnterView.this.stickersExpanded;
                    ChatActivityEnterView.this.stickersExpanded = true;
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 1);
                    ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                    chatActivityEnterView.stickersExpandedHeight = (((chatActivityEnterView.sizeNotifierLayout.getHeight() - (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)) - ActionBar.getCurrentActionBarHeight()) - ChatActivityEnterView.this.getHeight()) + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                    if (ChatActivityEnterView.this.searchingType == 2) {
                        ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                        int i = chatActivityEnterView2.stickersExpandedHeight;
                        int dp = AndroidUtilities.dp(120.0f);
                        android.graphics.Point point = AndroidUtilities.displaySize;
                        chatActivityEnterView2.stickersExpandedHeight = Math.min(i, dp + (point.x > point.y ? ChatActivityEnterView.this.keyboardHeightLand : ChatActivityEnterView.this.keyboardHeight));
                    }
                    ChatActivityEnterView.this.emojiView.getLayoutParams().height = ChatActivityEnterView.this.stickersExpandedHeight;
                    ChatActivityEnterView.this.emojiView.setLayerType(2, null);
                    ChatActivityEnterView.this.sizeNotifierLayout.requestLayout();
                    ChatActivityEnterView chatActivityEnterView3 = ChatActivityEnterView.this;
                    if (chatActivityEnterView3.shouldDrawBackground) {
                        chatActivityEnterView3.sizeNotifierLayout.setForeground(ChatActivityEnterView.this.new ScrimDrawable());
                    }
                    this.initialOffset = (int) ChatActivityEnterView.this.getTranslationY();
                    if (ChatActivityEnterView.this.delegate != null) {
                        ChatActivityEnterView.this.delegate.onStickersExpandedChange();
                    }
                }
            }
        });
        EmojiView emojiView3 = this.emojiView;
        if (emojiView3 != null) {
            emojiView3.setStickersBanned(!this.sendPlainEnabled, !this.stickersEnabled, -this.dialog_id);
        }
        attachEmojiView();
        checkChannelRights();
    }

    private void createExpandStickersButton() {
        if (this.expandStickersButton != null) {
            return;
        }
        ImageView imageView = new ImageView(getContext()) { // from class: org.telegram.ui.Components.ChatActivityEnterView.25
            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (getAlpha() <= 0.0f) {
                    return false;
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.expandStickersButton = imageView;
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        ImageView imageView2 = this.expandStickersButton;
        AnimatedArrowDrawable animatedArrowDrawable = new AnimatedArrowDrawable(getThemedColor(Theme.key_chat_messagePanelIcons), false);
        this.stickersArrow = animatedArrowDrawable;
        imageView2.setImageDrawable(animatedArrowDrawable);
        this.expandStickersButton.setVisibility(8);
        this.expandStickersButton.setScaleX(0.1f);
        this.expandStickersButton.setScaleY(0.1f);
        this.expandStickersButton.setAlpha(0.0f);
        if (Build.VERSION.SDK_INT >= 21) {
            this.expandStickersButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
        this.sendButtonContainer.addView(this.expandStickersButton, LayoutHelper.createFrame(48, 48.0f));
        this.expandStickersButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda24
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createExpandStickersButton$12(view);
            }
        });
        this.expandStickersButton.setContentDescription(LocaleController.getString("AccDescrExpandPanel", R.string.AccDescrExpandPanel));
    }

    private void createGiftButton() {
        if (this.giftButton != null || this.parentFragment == null) {
            return;
        }
        23 r0 = new 23(getContext());
        this.giftButton = r0;
        r0.setImageResource(R.drawable.msg_input_gift);
        this.giftButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_messagePanelIcons), PorterDuff.Mode.MULTIPLY));
        this.giftButton.setVisibility(8);
        this.giftButton.setContentDescription(LocaleController.getString(R.string.GiftPremium));
        this.giftButton.setScaleType(ImageView.ScaleType.CENTER);
        if (Build.VERSION.SDK_INT >= 21) {
            this.giftButton.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
        this.attachLayout.addView(this.giftButton, 0, LayoutHelper.createFrame(48, 48, 21));
        this.giftButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda15
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createGiftButton$10(view);
            }
        });
    }

    private void createMessageEditText() {
        EditTextCaption editTextCaption;
        Window window;
        if (this.messageEditText != null) {
            return;
        }
        ChatActivityEditTextCaption chatActivityEditTextCaption = new ChatActivityEditTextCaption(getContext(), this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatActivityEnterView.42
            boolean clickMaybe;
            float touchX;
            float touchY;

            private void fixHandlesColor() {
                setHandlesColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_TextSelectionCursor));
            }

            @Override // org.telegram.ui.Components.EditTextBoldCursor, org.telegram.ui.Components.EditTextEffects, android.widget.TextView, android.view.View
            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                setWindowView(((ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentFragment.getParentLayout() == null || !ChatActivityEnterView.this.parentFragment.getParentLayout().isSheet()) ? ChatActivityEnterView.this.parentActivity.getWindow() : ChatActivityEnterView.this.parentFragment.getParentLayout().getWindow()).getDecorView());
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEditTextCaption, org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                boolean z;
                if (!ChatActivityEnterView.this.botCommandsMenuIsShowing()) {
                    if (motionEvent.getAction() == 0 && ChatActivityEnterView.this.delegate != null) {
                        fixHandlesColor();
                        ChatActivityEnterView.this.delegate.onKeyboardRequested();
                    }
                    return super.onTouchEvent(motionEvent);
                }
                if (motionEvent.getAction() != 0) {
                    if (this.clickMaybe && motionEvent.getAction() == 2) {
                        z = Math.abs(motionEvent.getX() - this.touchX) <= AndroidUtilities.touchSlop && Math.abs(motionEvent.getY() - this.touchY) <= AndroidUtilities.touchSlop;
                    } else if (this.clickMaybe) {
                        if (ChatActivityEnterView.this.delegate != null) {
                            fixHandlesColor();
                            ChatActivityEnterView.this.delegate.onKeyboardRequested();
                        }
                        EditTextCaption editTextCaption2 = ChatActivityEnterView.this.messageEditText;
                        if (editTextCaption2 != null && !AndroidUtilities.showKeyboard(editTextCaption2)) {
                            ChatActivityEnterView.this.messageEditText.clearFocus();
                            ChatActivityEnterView.this.messageEditText.requestFocus();
                        }
                    }
                    return this.clickMaybe;
                }
                this.touchX = motionEvent.getX();
                this.touchY = motionEvent.getY();
                this.clickMaybe = z;
                return this.clickMaybe;
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEditTextCaption, org.telegram.ui.Components.EditTextEffects
            public void setOffsetY(float f) {
                super.setOffsetY(f);
                ChatActivityEnterView.this.messageEditTextContainer.invalidate();
            }
        };
        this.messageEditText = chatActivityEditTextCaption;
        if (Build.VERSION.SDK_INT >= 28) {
            chatActivityEditTextCaption.setFallbackLineSpacing(false);
        }
        this.messageEditText.setDelegate(new EditTextCaption.EditTextCaptionDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda42
            @Override // org.telegram.ui.Components.EditTextCaption.EditTextCaptionDelegate
            public final void onSpansChanged() {
                ChatActivityEnterView.this.lambda$createMessageEditText$39();
            }
        });
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || chatActivity.getParentLayout() == null || !this.parentFragment.getParentLayout().isSheet()) {
            editTextCaption = this.messageEditText;
            window = this.parentActivity.getWindow();
        } else {
            editTextCaption = this.messageEditText;
            window = this.parentFragment.getParentLayout().getWindow();
        }
        editTextCaption.setWindowView(window.getDecorView());
        ChatActivity chatActivity2 = this.parentFragment;
        TLRPC.EncryptedChat currentEncryptedChat = chatActivity2 != null ? chatActivity2.getCurrentEncryptedChat() : null;
        this.messageEditText.setAllowTextEntitiesIntersection(supportsSendingNewEntities());
        int i = (!isKeyboardSupportIncognitoMode() || currentEncryptedChat == null) ? 268435456 : 285212672;
        this.messageEditText.setIncludeFontPadding(false);
        this.messageEditText.setImeOptions(i);
        EditTextCaption editTextCaption2 = this.messageEditText;
        int inputType = editTextCaption2.getInputType() | 147456;
        this.commonInputType = inputType;
        editTextCaption2.setInputType(inputType);
        updateFieldHint(false);
        this.messageEditText.setSingleLine(false);
        this.messageEditText.setMaxLines(6);
        this.messageEditText.setTextSize(1, 18.0f);
        this.messageEditText.setGravity(80);
        this.messageEditText.setPadding(0, AndroidUtilities.dp(11.0f), 0, AndroidUtilities.dp(12.0f));
        this.messageEditText.setBackgroundDrawable(null);
        this.messageEditText.setTextColor(getThemedColor(Theme.key_chat_messagePanelText));
        this.messageEditText.setLinkTextColor(getThemedColor(Theme.key_chat_messageLinkOut));
        this.messageEditText.setHighlightColor(getThemedColor(Theme.key_chat_inTextSelectionHighlight));
        EditTextCaption editTextCaption3 = this.messageEditText;
        int i2 = Theme.key_chat_messagePanelHint;
        editTextCaption3.setHintColor(getThemedColor(i2));
        this.messageEditText.setHintTextColor(getThemedColor(i2));
        this.messageEditText.setCursorColor(getThemedColor(Theme.key_chat_messagePanelCursor));
        this.messageEditText.setHandlesColor(getThemedColor(Theme.key_chat_TextSelectionCursor));
        this.messageEditTextContainer.addView(this.messageEditText, 1, LayoutHelper.createFrame(-1, -2.0f, 80, 52.0f, 0.0f, this.isChat ? 50.0f : 2.0f, 1.5f));
        this.messageEditText.setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.43
            /* JADX WARN: Code restructure failed: missing block: B:59:0x012b, code lost:
            
                if (r7.getAction() != 0) goto L67;
             */
            /* JADX WARN: Code restructure failed: missing block: B:61:0x0133, code lost:
            
                if (r4.this$0.editingMessageObject != null) goto L67;
             */
            /* JADX WARN: Code restructure failed: missing block: B:62:0x0135, code lost:
            
                r4.this$0.sendMessage();
             */
            /* JADX WARN: Code restructure failed: missing block: B:63:0x013a, code lost:
            
                return true;
             */
            @Override // android.view.View.OnKeyListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean onKey(View view, int i3, KeyEvent keyEvent) {
                if (keyEvent != null) {
                    ChatActivityEnterView.this.shiftPressed = keyEvent.isShiftPressed();
                    ChatActivityEnterView.this.ctrlPressed = keyEvent.isCtrlPressed();
                }
                if (i3 != 4 || ChatActivityEnterView.this.keyboardVisible || !ChatActivityEnterView.this.isPopupShowing() || keyEvent.getAction() != 1) {
                    if (i3 == 66 && !keyEvent.isShiftPressed()) {
                        if (ChatActivityEnterView.this.sendByEnter) {
                        }
                    }
                    return false;
                }
                if (ContentPreviewViewer.hasInstance() && ContentPreviewViewer.getInstance().isVisible()) {
                    ContentPreviewViewer.getInstance().closeWithMenu();
                    return true;
                }
                if (ChatActivityEnterView.this.currentPopupContentType == 1 && ChatActivityEnterView.this.botButtonsMessageObject != null) {
                    return false;
                }
                if (keyEvent.getAction() == 1) {
                    if (ChatActivityEnterView.this.currentPopupContentType == 1 && ChatActivityEnterView.this.botButtonsMessageObject != null) {
                        MessagesController.getMainSettings(ChatActivityEnterView.this.currentAccount).edit().putInt("hidekeyboard_" + ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.botButtonsMessageObject.getId()).commit();
                    }
                    if (ChatActivityEnterView.this.searchingType != 0) {
                        ChatActivityEnterView.this.setSearchingTypeInternal(0, true);
                        if (ChatActivityEnterView.this.emojiView != null) {
                            ChatActivityEnterView.this.emojiView.closeSearch(true);
                        }
                        ChatActivityEnterView.this.messageEditText.requestFocus();
                    } else if (ChatActivityEnterView.this.stickersExpanded) {
                        ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                    } else if (ChatActivityEnterView.this.stickersExpansionAnim == null) {
                        if (ChatActivityEnterView.this.botButtonsMessageObject == null || ChatActivityEnterView.this.currentPopupContentType == 1 || !TextUtils.isEmpty(ChatActivityEnterView.this.messageEditText.getTextToUse())) {
                            ChatActivityEnterView.this.showPopup(0, 0);
                        } else {
                            ChatActivityEnterView.this.showPopup(1, 1);
                        }
                    }
                }
                return true;
            }
        });
        this.messageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.44
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i3, KeyEvent keyEvent) {
                if (i3 != 4) {
                    if (keyEvent == null || i3 != 0 || keyEvent.isShiftPressed()) {
                        return false;
                    }
                    if (ChatActivityEnterView.this.sendByEnter) {
                        if (keyEvent.isCtrlPressed()) {
                            return false;
                        }
                    } else if (!keyEvent.isCtrlPressed()) {
                        return false;
                    }
                    if (keyEvent.getAction() != 0 || ChatActivityEnterView.this.editingMessageObject != null) {
                        return false;
                    }
                }
                ChatActivityEnterView.this.sendMessage();
                return true;
            }
        });
        this.messageEditText.addTextChangedListener(new 45());
        this.messageEditText.addTextChangedListener(new EditTextSuggestionsFix());
        this.messageEditText.setEnabled(this.messageEditTextEnabled);
        ArrayList arrayList = this.messageEditTextWatchers;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.messageEditText.addTextChangedListener((TextWatcher) it.next());
            }
            this.messageEditTextWatchers.clear();
        }
        updateFieldHint(false);
        ChatActivity chatActivity3 = this.parentFragment;
        updateSendAsButton(chatActivity3 != null && chatActivity3.getFragmentBeginToShow());
        ChatActivity chatActivity4 = this.parentFragment;
        if (chatActivity4 != null) {
            chatActivity4.applyDraftMaybe(false);
        }
    }

    private void createRecordAudioPanel() {
        if (this.recordedAudioPanel != null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.Components.ChatActivityEnterView.26
            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                return super.dispatchTouchEvent(motionEvent);
            }

            @Override // android.view.View
            public void setVisibility(int i) {
                super.setVisibility(i);
                ChatActivityEnterView.this.updateSendAsButton();
            }
        };
        this.recordedAudioPanel = frameLayout;
        frameLayout.setVisibility(this.audioToSend == null ? 8 : 0);
        this.recordedAudioPanel.setFocusable(true);
        this.recordedAudioPanel.setFocusableInTouchMode(true);
        this.recordedAudioPanel.setClickable(true);
        this.messageEditTextContainer.addView(this.recordedAudioPanel, LayoutHelper.createFrame(-1, 48, 80));
        RLottieImageView rLottieImageView = new RLottieImageView(getContext());
        this.recordDeleteImageView = rLottieImageView;
        rLottieImageView.setScaleType(ImageView.ScaleType.CENTER);
        this.recordDeleteImageView.setAnimation(R.raw.chat_audio_record_delete_2, 28, 28);
        this.recordDeleteImageView.getAnimatedDrawable().setInvalidateOnProgressSet(true);
        updateRecordedDeleteIconColors();
        this.recordDeleteImageView.setContentDescription(LocaleController.getString("Delete", R.string.Delete));
        if (Build.VERSION.SDK_INT >= 21) {
            this.recordDeleteImageView.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
        this.recordedAudioPanel.addView(this.recordDeleteImageView, LayoutHelper.createFrame(48, 48.0f));
        this.recordDeleteImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda26
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createRecordAudioPanel$13(view);
            }
        });
        VideoTimelineView videoTimelineView = new VideoTimelineView(getContext());
        this.videoTimelineView = videoTimelineView;
        videoTimelineView.setVisibility(4);
        VideoTimelineView videoTimelineView2 = this.videoTimelineView;
        videoTimelineView2.useClip = !this.shouldDrawBackground;
        videoTimelineView2.setRoundFrames(true);
        this.videoTimelineView.setDelegate(new VideoTimelineView.VideoTimelineViewDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView.27
            @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
            public void didStartDragging() {
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(1, 0.0f);
            }

            @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
            public void didStopDragging() {
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(0, 0.0f);
            }

            @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
            public void onLeftProgressChanged(float f) {
                if (ChatActivityEnterView.this.videoToSendMessageObject == null) {
                    return;
                }
                ChatActivityEnterView.this.videoToSendMessageObject.startTime = (long) (ChatActivityEnterView.this.videoToSendMessageObject.estimatedDuration * f);
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(2, f);
            }

            @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
            public void onRightProgressChanged(float f) {
                if (ChatActivityEnterView.this.videoToSendMessageObject == null) {
                    return;
                }
                ChatActivityEnterView.this.videoToSendMessageObject.endTime = (long) (ChatActivityEnterView.this.videoToSendMessageObject.estimatedDuration * f);
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(2, f);
            }
        });
        this.recordedAudioPanel.addView(this.videoTimelineView, LayoutHelper.createFrame(-1, -1.0f, 19, 56.0f, 0.0f, 8.0f, 0.0f));
        VideoTimelineView.TimeHintView timeHintView = new VideoTimelineView.TimeHintView(getContext());
        this.videoTimelineView.setTimeHintView(timeHintView);
        this.sizeNotifierLayout.addView(timeHintView, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 52.0f));
        View view = new View(getContext()) { // from class: org.telegram.ui.Components.ChatActivityEnterView.28
            @Override // android.view.View
            protected void dispatchDraw(Canvas canvas) {
                getBackground().setBounds((int) ChatActivityEnterView.this.horizontalPadding, 0, (int) (getMeasuredWidth() - ChatActivityEnterView.this.horizontalPadding), getMeasuredHeight());
                getBackground().draw(canvas);
            }
        };
        this.recordedAudioBackground = view;
        view.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(18.0f), getThemedColor(Theme.key_chat_recordedVoiceBackground)));
        this.recordedAudioPanel.addView(this.recordedAudioBackground, LayoutHelper.createFrame(-1, 36.0f, 19, 48.0f, 0.0f, 0.0f, 0.0f));
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(0);
        this.recordedAudioPanel.addView(linearLayout, LayoutHelper.createFrame(-1, 32.0f, 19, 92.0f, 0.0f, 13.0f, 0.0f));
        this.recordedAudioPlayButton = new ImageView(getContext());
        Matrix matrix = new Matrix();
        matrix.postScale(0.8f, 0.8f, AndroidUtilities.dpf2(24.0f), AndroidUtilities.dpf2(24.0f));
        this.recordedAudioPlayButton.setImageMatrix(matrix);
        ImageView imageView = this.recordedAudioPlayButton;
        MediaActionDrawable mediaActionDrawable = new MediaActionDrawable();
        this.playPauseDrawable = mediaActionDrawable;
        imageView.setImageDrawable(mediaActionDrawable);
        this.recordedAudioPlayButton.setScaleType(ImageView.ScaleType.MATRIX);
        this.recordedAudioPlayButton.setContentDescription(LocaleController.getString("AccActionPlay", R.string.AccActionPlay));
        this.recordedAudioPanel.addView(this.recordedAudioPlayButton, LayoutHelper.createFrame(48, 48.0f, 83, 48.0f, 0.0f, 13.0f, 0.0f));
        this.recordedAudioPlayButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda27
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ChatActivityEnterView.this.lambda$createRecordAudioPanel$14(view2);
            }
        });
        SeekBarWaveformView seekBarWaveformView = new SeekBarWaveformView(getContext());
        this.recordedAudioSeekBar = seekBarWaveformView;
        seekBarWaveformView.setVisibility(4);
        linearLayout.addView(this.recordedAudioSeekBar, LayoutHelper.createLinear(0, 32, 1.0f, 16, 0, 0, 4, 0));
        TextView textView = new TextView(getContext());
        this.recordedAudioTimeTextView = textView;
        textView.setTextColor(getThemedColor(Theme.key_chat_messagePanelVoiceDuration));
        this.recordedAudioTimeTextView.setTextSize(1, 13.0f);
        linearLayout.addView(this.recordedAudioTimeTextView, LayoutHelper.createLinear(-2, -2, 0.0f, 16));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createRecordCircle() {
        createControlsView();
        if (this.recordCircle != null) {
            return;
        }
        RecordCircle recordCircle = new RecordCircle(getContext());
        this.recordCircle = recordCircle;
        recordCircle.setVisibility(8);
        this.sizeNotifierLayout.addView(this.recordCircle, LayoutHelper.createFrame(-1, -2, 80));
    }

    private void createRecordPanel() {
        if (this.recordPanel != null || getContext() == null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.Components.ChatActivityEnterView.65
            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                return super.onTouchEvent(motionEvent);
            }
        };
        this.recordPanel = frameLayout;
        frameLayout.setClipChildren(false);
        this.recordPanel.setVisibility(8);
        this.messageEditTextContainer.addView(this.recordPanel, LayoutHelper.createFrame(-1, 48.0f));
        this.recordPanel.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda25
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$createRecordPanel$52;
                lambda$createRecordPanel$52 = ChatActivityEnterView.lambda$createRecordPanel$52(view, motionEvent);
                return lambda$createRecordPanel$52;
            }
        });
        FrameLayout frameLayout2 = this.recordPanel;
        SlideTextView slideTextView = new SlideTextView(getContext());
        this.slideText = slideTextView;
        frameLayout2.addView(slideTextView, LayoutHelper.createFrame(-1, -1.0f, 0, 45.0f, 0.0f, 0.0f, 0.0f));
        LinearLayout linearLayout = new LinearLayout(getContext());
        this.recordTimeContainer = linearLayout;
        linearLayout.setOrientation(0);
        this.recordTimeContainer.setPadding(AndroidUtilities.dp(13.0f), 0, 0, 0);
        this.recordTimeContainer.setFocusable(false);
        LinearLayout linearLayout2 = this.recordTimeContainer;
        RecordDot recordDot = new RecordDot(getContext());
        this.recordDot = recordDot;
        linearLayout2.addView(recordDot, LayoutHelper.createLinear(28, 28, 16, 0, 0, 0, 0));
        LinearLayout linearLayout3 = this.recordTimeContainer;
        TimerView timerView = new TimerView(getContext());
        this.recordTimerView = timerView;
        linearLayout3.addView(timerView, LayoutHelper.createLinear(-1, -1, 16, 6, 0, 0, 0));
        this.recordPanel.addView(this.recordTimeContainer, LayoutHelper.createFrame(-1, -1, 16));
    }

    private void createScheduledButton() {
        if (this.scheduledButton != null || this.parentFragment == null) {
            return;
        }
        Drawable mutate = getContext().getResources().getDrawable(R.drawable.input_calendar1).mutate();
        Drawable mutate2 = getContext().getResources().getDrawable(R.drawable.input_calendar2).mutate();
        int themedColor = getThemedColor(Theme.key_chat_messagePanelIcons);
        PorterDuff.Mode mode = PorterDuff.Mode.MULTIPLY;
        mutate.setColorFilter(new PorterDuffColorFilter(themedColor, mode));
        mutate2.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_recordedVoiceDot), mode));
        CombinedDrawable combinedDrawable = new CombinedDrawable(mutate, mutate2);
        ImageView imageView = new ImageView(getContext());
        this.scheduledButton = imageView;
        imageView.setImageDrawable(combinedDrawable);
        this.scheduledButton.setVisibility(8);
        this.scheduledButton.setContentDescription(LocaleController.getString("ScheduledMessages", R.string.ScheduledMessages));
        this.scheduledButton.setScaleType(ImageView.ScaleType.CENTER);
        if (Build.VERSION.SDK_INT >= 21) {
            this.scheduledButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
        this.messageEditTextContainer.addView(this.scheduledButton, 2, LayoutHelper.createFrame(48, 48, 85));
        this.scheduledButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda18
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createScheduledButton$7(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createSenderSelectView() {
        if (this.senderSelectView != null) {
            return;
        }
        SenderSelectView senderSelectView = new SenderSelectView(getContext());
        this.senderSelectView = senderSelectView;
        senderSelectView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda57
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createSenderSelectView$21(view);
            }
        });
        this.senderSelectView.setVisibility(8);
        this.messageEditTextContainer.addView(this.senderSelectView, LayoutHelper.createFrame(32, 32.0f, 83, 10.0f, 8.0f, 10.0f, 8.0f));
    }

    private MessageObject editingMessageObjectPreview(MessageObject messageObject, boolean z) {
        MessageObject messageObject2 = new MessageObject(messageObject.currentAccount, messageObject.messageOwner, true, true) { // from class: org.telegram.ui.Components.ChatActivityEnterView.66
            @Override // org.telegram.messenger.MessageObject
            public boolean isOutOwner() {
                return true;
            }

            @Override // org.telegram.messenger.MessageObject
            public boolean needDrawShareButton() {
                return false;
            }
        };
        if (z) {
            EditTextCaption editTextCaption = this.messageEditText;
            CharSequence[] charSequenceArr = {editTextCaption == null ? "" : editTextCaption.getTextToUse()};
            ArrayList<TLRPC.MessageEntity> entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, true);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequenceArr[0].toString());
            MessageObject.addEntitiesToText(spannableStringBuilder, entities, true, true, false, true);
            messageObject2.caption = MessageObject.replaceAnimatedEmoji(Emoji.replaceEmoji((CharSequence) spannableStringBuilder, Theme.chat_msgTextPaint.getFontMetricsInt(), false, (int[]) null), entities, Theme.chat_msgTextPaint.getFontMetricsInt());
        }
        return messageObject2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getThemedColor(int i) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        return resourcesProvider != null ? resourcesProvider.getColor(i) : Theme.getColor(i);
    }

    private Paint getThemedPaint(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Paint paint = resourcesProvider != null ? resourcesProvider.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MessageObject getThreadMessage() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            return chatActivity.getThreadMessage();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getThreadMessageId() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || chatActivity.getThreadMessage() == null) {
            return 0;
        }
        return this.parentFragment.getThreadMessage().getId();
    }

    private String getTopicKeyString() {
        StringBuilder sb;
        long j;
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || !chatActivity.isTopic) {
            sb = new StringBuilder();
            sb.append("");
            j = this.dialog_id;
        } else {
            sb = new StringBuilder();
            sb.append(this.dialog_id);
            sb.append("_");
            j = this.parentFragment.getTopicId();
        }
        sb.append(j);
        return sb.toString();
    }

    private void hideRecordedAudioPanel(boolean z) {
        AnimatorSet animatorSet;
        AnimatorSet animatorSet2;
        AnimatorListenerAdapter animatorListenerAdapter;
        AnimatorSet animatorSet3 = this.recordPannelAnimation;
        if (animatorSet3 == null || !animatorSet3.isRunning()) {
            this.audioToSendPath = null;
            this.audioToSend = null;
            this.audioToSendMessageObject = null;
            this.videoToSendMessageObject = null;
            VideoTimelineView videoTimelineView = this.videoTimelineView;
            if (videoTimelineView != null) {
                videoTimelineView.destroy();
            }
            ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView = this.audioVideoSendButton;
            if (chatActivityEnterViewAnimatedIconView != null) {
                chatActivityEnterViewAnimatedIconView.setVisibility(0);
            }
            if (z) {
                this.attachButton.setAlpha(0.0f);
                this.attachButton.setScaleX(0.0f);
                this.attachButton.setScaleY(0.0f);
                this.emojiButtonAlpha = 0.0f;
                this.emojiButtonScale = 0.0f;
                updateEmojiButtonParams();
                this.recordPannelAnimation = new AnimatorSet();
                ArrayList arrayList = new ArrayList();
                arrayList.add(ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_ALPHA, this.emojiButtonRestricted ? 0.5f : 1.0f));
                arrayList.add(ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 1.0f));
                RLottieImageView rLottieImageView = this.recordDeleteImageView;
                Property property = View.ALPHA;
                arrayList.add(ObjectAnimator.ofFloat(rLottieImageView, (Property<RLottieImageView, Float>) property, 0.0f));
                RLottieImageView rLottieImageView2 = this.recordDeleteImageView;
                Property property2 = View.SCALE_X;
                arrayList.add(ObjectAnimator.ofFloat(rLottieImageView2, (Property<RLottieImageView, Float>) property2, 0.0f));
                RLottieImageView rLottieImageView3 = this.recordDeleteImageView;
                Property property3 = View.SCALE_Y;
                arrayList.add(ObjectAnimator.ofFloat(rLottieImageView3, (Property<RLottieImageView, Float>) property3, 0.0f));
                arrayList.add(ObjectAnimator.ofFloat(this.recordedAudioPanel, (Property<FrameLayout, Float>) property, 0.0f));
                arrayList.add(ObjectAnimator.ofFloat(this.attachButton, (Property<ImageView, Float>) property, 1.0f));
                arrayList.add(ObjectAnimator.ofFloat(this.attachButton, (Property<ImageView, Float>) property2, 1.0f));
                arrayList.add(ObjectAnimator.ofFloat(this.attachButton, (Property<ImageView, Float>) property3, 1.0f));
                arrayList.add(ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) property, 1.0f));
                arrayList.add(ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) this.MESSAGE_TEXT_TRANSLATION_X, 0.0f));
                ControlsView controlsView = this.controlsView;
                if (controlsView != null) {
                    arrayList.add(ObjectAnimator.ofFloat(controlsView, (Property<ControlsView, Float>) property, 0.0f));
                    this.controlsView.hideHintView();
                }
                this.recordPannelAnimation.playTogether(arrayList);
                BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
                if (botCommandsMenuView != null) {
                    botCommandsMenuView.setAlpha(0.0f);
                    this.botCommandsMenuButton.setScaleY(0.0f);
                    this.botCommandsMenuButton.setScaleX(0.0f);
                    this.recordPannelAnimation.playTogether(ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property2, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property3, 1.0f));
                }
                this.recordPannelAnimation.setDuration(150L);
                animatorSet2 = this.recordPannelAnimation;
                animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.48
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        FrameLayout frameLayout = ChatActivityEnterView.this.recordedAudioPanel;
                        if (frameLayout != null) {
                            frameLayout.setVisibility(8);
                        }
                        EditTextCaption editTextCaption = ChatActivityEnterView.this.messageEditText;
                        if (editTextCaption != null) {
                            editTextCaption.requestFocus();
                        }
                        ChatActivityEnterView.this.isRecordingStateChanged();
                    }
                };
            } else {
                RLottieImageView rLottieImageView4 = this.recordDeleteImageView;
                if (rLottieImageView4 != null) {
                    rLottieImageView4.playAnimation();
                }
                AnimatorSet animatorSet4 = new AnimatorSet();
                ArrayList arrayList2 = new ArrayList();
                if (isInVideoMode()) {
                    VideoTimelineView videoTimelineView2 = this.videoTimelineView;
                    Property property4 = View.ALPHA;
                    arrayList2.add(ObjectAnimator.ofFloat(videoTimelineView2, (Property<VideoTimelineView, Float>) property4, 0.0f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.videoTimelineView, (Property<VideoTimelineView, Float>) View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)));
                    arrayList2.add(ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) this.MESSAGE_TEXT_TRANSLATION_X, 0.0f));
                    ControlsView controlsView2 = this.controlsView;
                    if (controlsView2 != null) {
                        arrayList2.add(ObjectAnimator.ofFloat(controlsView2, (Property<ControlsView, Float>) property4, 0.0f));
                        this.controlsView.hideHintView();
                    }
                    animatorSet4.playTogether(arrayList2);
                    if (this.emojiButtonPaddingAlpha == 1.0f) {
                        animatorSet4.playTogether(ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) property4, 1.0f));
                    } else {
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) property4, 1.0f);
                        ofFloat.setStartDelay(750L);
                        ofFloat.setDuration(200L);
                        animatorSet4.playTogether(ofFloat);
                    }
                } else {
                    EditTextCaption editTextCaption = this.messageEditText;
                    if (editTextCaption == null || this.emojiButtonPaddingAlpha != 1.0f) {
                        this.messageTextTranslationX = 0.0f;
                        updateMessageTextParams();
                        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) View.ALPHA, 1.0f);
                        ofFloat2.setStartDelay(750L);
                        ofFloat2.setDuration(200L);
                        animatorSet4.playTogether(ofFloat2);
                    } else {
                        editTextCaption.setAlpha(1.0f);
                        this.messageTextTranslationX = 0.0f;
                        updateMessageTextParams();
                    }
                    SeekBarWaveformView seekBarWaveformView = this.recordedAudioSeekBar;
                    Property property5 = View.ALPHA;
                    arrayList2.add(ObjectAnimator.ofFloat(seekBarWaveformView, (Property<SeekBarWaveformView, Float>) property5, 0.0f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.recordedAudioPlayButton, (Property<ImageView, Float>) property5, 0.0f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.recordedAudioBackground, (Property<View, Float>) property5, 0.0f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.recordedAudioTimeTextView, (Property<TextView, Float>) property5, 0.0f));
                    SeekBarWaveformView seekBarWaveformView2 = this.recordedAudioSeekBar;
                    Property property6 = View.TRANSLATION_X;
                    arrayList2.add(ObjectAnimator.ofFloat(seekBarWaveformView2, (Property<SeekBarWaveformView, Float>) property6, -AndroidUtilities.dp(20.0f)));
                    arrayList2.add(ObjectAnimator.ofFloat(this.recordedAudioPlayButton, (Property<ImageView, Float>) property6, -AndroidUtilities.dp(20.0f)));
                    arrayList2.add(ObjectAnimator.ofFloat(this.recordedAudioBackground, (Property<View, Float>) property6, -AndroidUtilities.dp(20.0f)));
                    arrayList2.add(ObjectAnimator.ofFloat(this.recordedAudioTimeTextView, (Property<TextView, Float>) property6, -AndroidUtilities.dp(20.0f)));
                    ControlsView controlsView3 = this.controlsView;
                    if (controlsView3 != null) {
                        arrayList2.add(ObjectAnimator.ofFloat(controlsView3, (Property<ControlsView, Float>) property5, 0.0f));
                        this.controlsView.hideHintView();
                    }
                    animatorSet4.playTogether(arrayList2);
                }
                animatorSet4.setDuration(200L);
                ImageView imageView = this.attachButton;
                if (imageView != null) {
                    imageView.setAlpha(0.0f);
                    this.attachButton.setScaleX(0.0f);
                    this.attachButton.setScaleY(0.0f);
                    animatorSet = new AnimatorSet();
                    animatorSet.playTogether(ObjectAnimator.ofFloat(this.attachButton, (Property<ImageView, Float>) View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.attachButton, (Property<ImageView, Float>) View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.attachButton, (Property<ImageView, Float>) View.SCALE_Y, 1.0f));
                    animatorSet.setDuration(150L);
                } else {
                    animatorSet = null;
                }
                this.emojiButtonScale = 0.0f;
                this.emojiButtonAlpha = 0.0f;
                updateEmojiButtonParams();
                AnimatorSet animatorSet5 = new AnimatorSet();
                RLottieImageView rLottieImageView5 = this.recordDeleteImageView;
                Property property7 = View.ALPHA;
                ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(rLottieImageView5, (Property<RLottieImageView, Float>) property7, 0.0f);
                RLottieImageView rLottieImageView6 = this.recordDeleteImageView;
                Property property8 = View.SCALE_X;
                ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(rLottieImageView6, (Property<RLottieImageView, Float>) property8, 0.0f);
                RLottieImageView rLottieImageView7 = this.recordDeleteImageView;
                Property property9 = View.SCALE_Y;
                animatorSet5.playTogether(ofFloat3, ofFloat4, ObjectAnimator.ofFloat(rLottieImageView7, (Property<RLottieImageView, Float>) property9, 0.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, (Property<RLottieImageView, Float>) property7, 0.0f), ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_ALPHA, this.emojiButtonRestricted ? 0.5f : 1.0f), ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 1.0f));
                BotCommandsMenuView botCommandsMenuView2 = this.botCommandsMenuButton;
                if (botCommandsMenuView2 != null) {
                    botCommandsMenuView2.setAlpha(0.0f);
                    this.botCommandsMenuButton.setScaleY(0.0f);
                    this.botCommandsMenuButton.setScaleX(0.0f);
                    animatorSet5.playTogether(ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property7, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property8, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property9, 1.0f));
                }
                animatorSet5.setDuration(150L);
                animatorSet5.setStartDelay(600L);
                AnimatorSet animatorSet6 = new AnimatorSet();
                this.recordPannelAnimation = animatorSet6;
                if (animatorSet != null) {
                    animatorSet6.playTogether(animatorSet4, animatorSet, animatorSet5);
                } else {
                    animatorSet6.playTogether(animatorSet4, animatorSet5);
                }
                animatorSet2 = this.recordPannelAnimation;
                animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.49
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        VideoTimelineView videoTimelineView3 = ChatActivityEnterView.this.videoTimelineView;
                        if (videoTimelineView3 != null) {
                            videoTimelineView3.setVisibility(8);
                        }
                        SeekBarWaveformView seekBarWaveformView3 = ChatActivityEnterView.this.recordedAudioSeekBar;
                        if (seekBarWaveformView3 != null) {
                            seekBarWaveformView3.setVisibility(8);
                        }
                        if (ChatActivityEnterView.this.recordedAudioPlayButton != null) {
                            ChatActivityEnterView.this.recordedAudioPlayButton.setVisibility(8);
                        }
                        if (ChatActivityEnterView.this.recordedAudioBackground != null) {
                            ChatActivityEnterView.this.recordedAudioBackground.setVisibility(8);
                        }
                        if (ChatActivityEnterView.this.recordedAudioTimeTextView != null) {
                            ChatActivityEnterView.this.recordedAudioTimeTextView.setVisibility(8);
                        }
                        ChatActivityEnterView.this.transformToSeekbar = 0.0f;
                        ChatActivityEnterView.this.isRecordingStateChanged();
                        ChatActivityEnterView.this.hideRecordedAudioPanelInternal();
                        if (ChatActivityEnterView.this.recordCircle != null) {
                            ChatActivityEnterView.this.recordCircle.setSendButtonInvisible();
                        }
                    }
                };
            }
            animatorSet2.addListener(animatorListenerAdapter);
            AnimatorSet animatorSet7 = this.recordPannelAnimation;
            if (animatorSet7 != null) {
                animatorSet7.start();
            }
            ControlsView controlsView4 = this.controlsView;
            if (controlsView4 != null) {
                controlsView4.invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideRecordedAudioPanelInternal() {
        this.audioToSendPath = null;
        this.audioToSend = null;
        this.audioToSendMessageObject = null;
        this.videoToSendMessageObject = null;
        VideoTimelineView videoTimelineView = this.videoTimelineView;
        if (videoTimelineView != null) {
            videoTimelineView.destroy();
        }
        SeekBarWaveformView seekBarWaveformView = this.recordedAudioSeekBar;
        if (seekBarWaveformView != null) {
            seekBarWaveformView.setAlpha(1.0f);
            this.recordedAudioSeekBar.setTranslationX(0.0f);
        }
        ImageView imageView = this.recordedAudioPlayButton;
        if (imageView != null) {
            imageView.setAlpha(1.0f);
            this.recordedAudioPlayButton.setTranslationX(0.0f);
        }
        View view = this.recordedAudioBackground;
        if (view != null) {
            view.setAlpha(1.0f);
            this.recordedAudioBackground.setTranslationX(0.0f);
        }
        TextView textView = this.recordedAudioTimeTextView;
        if (textView != null) {
            textView.setAlpha(1.0f);
            this.recordedAudioTimeTextView.setTranslationX(0.0f);
        }
        VideoTimelineView videoTimelineView2 = this.videoTimelineView;
        if (videoTimelineView2 != null) {
            videoTimelineView2.setAlpha(1.0f);
            this.videoTimelineView.setTranslationX(0.0f);
        }
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.setAlpha(1.0f);
            this.messageTextTranslationX = 0.0f;
            updateMessageTextParams();
            this.messageEditText.requestFocus();
        }
        FrameLayout frameLayout = this.recordedAudioPanel;
        if (frameLayout != null) {
            frameLayout.setVisibility(8);
        }
        isRecordingStateChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isEditingBusinessLink() {
        return this.editingBusinessLink != null;
    }

    private boolean isKeyboardSupportIncognitoMode() {
        String string = Settings.Secure.getString(getContext().getContentResolver(), "default_input_method");
        return string == null || !string.startsWith("com.samsung");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkBirthdayHint$60() {
        removeView(this.birthdayHint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$checkPremiumAnimatedEmoji$47(BaseFragment baseFragment) {
        if (baseFragment != null) {
            new PremiumFeatureBottomSheet(baseFragment, 11, false).show();
        } else if (baseFragment.getContext() instanceof LaunchActivity) {
            ((LaunchActivity) baseFragment.getContext()).lambda$runLinkRequest$95(new PremiumPreviewFragment(null));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkStickresExpandHeight$73() {
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.getLayoutParams().height = this.stickersExpandedHeight;
            this.emojiView.setLayerType(0, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkStickresExpandHeight$74(ValueAnimator valueAnimator) {
        this.sizeNotifierLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkStickresExpandHeight$75(ValueAnimator valueAnimator) {
        this.sizeNotifierLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createBotButton$11(View view) {
        if (this.searchingType != 0) {
            setSearchingTypeInternal(0, false);
            this.emojiView.closeSearch(false);
            EditTextCaption editTextCaption = this.messageEditText;
            if (editTextCaption != null) {
                editTextCaption.requestFocus();
            }
        }
        if (this.botReplyMarkup != null) {
            if (!isPopupShowing() || this.currentPopupContentType != 1) {
                showPopup(1, 1);
            } else if (isPopupShowing() && this.currentPopupContentType == 1) {
                showPopup(0, 1);
            }
        } else if (this.hasBotCommands || this.hasQuickReplies) {
            setFieldText("/");
            EditTextCaption editTextCaption2 = this.messageEditText;
            if (editTextCaption2 != null) {
                editTextCaption2.requestFocus();
            }
            openKeyboard();
        }
        if (this.stickersExpanded) {
            setStickersExpanded(false, false, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createBotCommandsMenuButton$22(View view) {
        boolean z = !this.botCommandsMenuButton.isOpened();
        this.botCommandsMenuButton.setOpened(z);
        try {
            performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        if (hasBotWebView()) {
            if (z) {
                if (!this.emojiViewVisible && !this.botKeyboardViewVisible) {
                    openWebViewMenu();
                    return;
                } else {
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda55
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatActivityEnterView.this.openWebViewMenu();
                        }
                    }, 275L);
                    hidePopup(false);
                    return;
                }
            }
            return;
        }
        if (z) {
            createBotCommandsMenuContainer();
            this.botCommandsMenuContainer.show();
        } else {
            BotCommandsMenuContainer botCommandsMenuContainer = this.botCommandsMenuContainer;
            if (botCommandsMenuContainer != null) {
                botCommandsMenuContainer.dismiss();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createExpandStickersButton$12(View view) {
        EmojiView emojiView;
        EditTextCaption editTextCaption;
        if (this.expandStickersButton.getVisibility() == 0 && this.expandStickersButton.getAlpha() == 1.0f && !this.waitingForKeyboardOpen) {
            if (this.keyboardVisible && (editTextCaption = this.messageEditText) != null && editTextCaption.isFocused()) {
                return;
            }
            if (this.stickersExpanded) {
                if (this.searchingType != 0) {
                    setSearchingTypeInternal(0, true);
                    this.emojiView.closeSearch(true);
                    this.emojiView.hideSearchKeyboard();
                    if (this.emojiTabOpen) {
                        checkSendButton(true);
                    }
                } else if (!this.stickersDragging && (emojiView = this.emojiView) != null) {
                    emojiView.showSearchField(false);
                }
            } else if (!this.stickersDragging) {
                this.emojiView.showSearchField(true);
            }
            if (this.stickersDragging) {
                return;
            }
            setStickersExpanded(!this.stickersExpanded, true, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGiftButton$10(View view) {
        StringBuilder sb;
        SharedPreferences.Editor edit = MessagesController.getInstance(this.currentAccount).getMainSettings().edit();
        if (BirthdayController.isToday(this.parentFragment.getCurrentUserInfo())) {
            sb = new StringBuilder();
            sb.append(Calendar.getInstance().get(1));
        } else {
            sb = new StringBuilder();
        }
        sb.append("show_gift_for_");
        sb.append(this.parentFragment.getDialogId());
        edit.putBoolean(sb.toString(), false);
        if (MessagesController.getInstance(this.currentAccount).giftAttachMenuIcon && MessagesController.getInstance(this.currentAccount).giftTextFieldIcon && !getParentFragment().getCurrentUserInfo().premium_gifts.isEmpty()) {
            edit.putBoolean("show_gift_for_" + this.parentFragment.getDialogId(), false);
        }
        edit.apply();
        AndroidUtilities.updateViewVisibilityAnimated(this.giftButton, false);
        final TLRPC.User currentUser = getParentFragment().getCurrentUser();
        if (currentUser == null) {
            return;
        }
        final boolean z = getParentFragment().getCurrentUserInfo() != null && BirthdayController.isToday(getParentFragment().getCurrentUserInfo().birthday);
        if (!new ArrayList(getParentFragment().getCurrentUserInfo().premium_gifts).isEmpty()) {
            new GiftSheet(getContext(), this.currentAccount, currentUser.id, null, null).setBirthday(z).show();
            return;
        }
        final AlertDialog alertDialog = new AlertDialog(getContext(), 3);
        alertDialog.showDelayed(200L);
        final int loadGiftOptions = BoostRepository.loadGiftOptions(this.currentAccount, null, new Utilities.Callback() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda40
            @Override // org.telegram.messenger.Utilities.Callback
            public final void run(Object obj) {
                ChatActivityEnterView.this.lambda$createGiftButton$8(alertDialog, currentUser, z, (List) obj);
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda41
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                ChatActivityEnterView.this.lambda$createGiftButton$9(loadGiftOptions, dialogInterface);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGiftButton$8(AlertDialog alertDialog, TLRPC.User user, boolean z, List list) {
        alertDialog.dismiss();
        new GiftSheet(getContext(), this.currentAccount, user.id, BoostRepository.filterGiftOptionsByBilling(BoostRepository.filterGiftOptions(list, 1)), null).setBirthday(z).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createGiftButton$9(int i, DialogInterface dialogInterface) {
        this.parentFragment.getConnectionsManager().cancelRequest(i, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createMessageEditText$39() {
        this.messageEditText.invalidateEffects();
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onTextSpansChanged(this.messageEditText.getTextToUse());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createRecordAudioPanel$13(View view) {
        AnimatorSet animatorSet = this.runningAnimationAudio;
        if (animatorSet == null || !animatorSet.isRunning()) {
            resetRecordedState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createRecordAudioPanel$14(View view) {
        ImageView imageView;
        int i;
        String str;
        if (this.audioToSend == null) {
            return;
        }
        if (!MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject) || MediaController.getInstance().isMessagePaused()) {
            this.playPauseDrawable.setIcon(1, true);
            MediaController.getInstance().playMessage(this.audioToSendMessageObject);
            imageView = this.recordedAudioPlayButton;
            i = R.string.AccActionPause;
            str = "AccActionPause";
        } else {
            MediaController.getInstance().lambda$startAudioAgain$7(this.audioToSendMessageObject);
            this.playPauseDrawable.setIcon(0, true);
            imageView = this.recordedAudioPlayButton;
            i = R.string.AccActionPlay;
            str = "AccActionPlay";
        }
        imageView.setContentDescription(LocaleController.getString(str, i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$createRecordPanel$52(View view, MotionEvent motionEvent) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScheduledButton$7(View view) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.openScheduledMessages();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSenderSelectView$15() {
        this.senderSelectView.callOnClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSenderSelectView$16() {
        this.senderSelectView.callOnClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSenderSelectView$17(final Dialog dialog, SimpleAvatarView simpleAvatarView, float f, float f2, DynamicAnimation dynamicAnimation, boolean z, float f3, float f4) {
        if (dialog.isShowing()) {
            simpleAvatarView.setTranslationX(f);
            simpleAvatarView.setTranslationY(f2);
            this.senderSelectView.setProgress(0.0f, false);
            this.senderSelectView.setScaleX(1.0f);
            this.senderSelectView.setScaleY(1.0f);
            this.senderSelectView.setAlpha(1.0f);
            this.senderSelectView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.31
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    ChatActivityEnterView.this.senderSelectView.getViewTreeObserver().removeOnPreDrawListener(this);
                    SenderSelectView senderSelectView = ChatActivityEnterView.this.senderSelectView;
                    Dialog dialog2 = dialog;
                    Objects.requireNonNull(dialog2);
                    senderSelectView.postDelayed(new ChatActivityEnterView$31$$ExternalSyntheticLambda0(dialog2), 100L);
                    return true;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSenderSelectView$18(final Dialog dialog, SimpleAvatarView simpleAvatarView, float f, float f2, DynamicAnimation dynamicAnimation, boolean z, float f3, float f4) {
        if (dialog.isShowing()) {
            simpleAvatarView.setTranslationX(f);
            simpleAvatarView.setTranslationY(f2);
            this.senderSelectView.setProgress(0.0f, false);
            this.senderSelectView.setScaleX(1.0f);
            this.senderSelectView.setScaleY(1.0f);
            this.senderSelectView.setAlpha(1.0f);
            this.senderSelectView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.33
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    ChatActivityEnterView.this.senderSelectView.getViewTreeObserver().removeOnPreDrawListener(this);
                    SenderSelectView senderSelectView = ChatActivityEnterView.this.senderSelectView;
                    Dialog dialog2 = dialog;
                    Objects.requireNonNull(dialog2);
                    senderSelectView.postDelayed(new ChatActivityEnterView$31$$ExternalSyntheticLambda0(dialog2), 100L);
                    return true;
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSenderSelectView$19(final SimpleAvatarView simpleAvatarView, int[] iArr, SenderSelectPopup.SenderView senderView) {
        WindowInsets rootWindowInsets;
        int systemWindowInsetLeft;
        if (this.senderSelectPopupWindow == null) {
            return;
        }
        final Dialog dialog = new Dialog(getContext(), R.style.TransparentDialogNoAnimation);
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.addView(simpleAvatarView, LayoutHelper.createFrame(40, 40, 3));
        dialog.setContentView(frameLayout);
        dialog.getWindow().setLayout(-1, -1);
        int i = Build.VERSION.SDK_INT;
        if (i >= 21) {
            dialog.getWindow().clearFlags(1024);
            dialog.getWindow().clearFlags(ConnectionsManager.FileTypeFile);
            dialog.getWindow().clearFlags(134217728);
            dialog.getWindow().addFlags(Integer.MIN_VALUE);
            dialog.getWindow().addFlags(512);
            dialog.getWindow().addFlags(131072);
            dialog.getWindow().getAttributes().windowAnimations = 0;
            dialog.getWindow().getDecorView().setSystemUiVisibility(1792);
            dialog.getWindow().setStatusBarColor(0);
            dialog.getWindow().setNavigationBarColor(0);
            AndroidUtilities.setLightStatusBar(dialog.getWindow(), Theme.getColor(Theme.key_actionBarDefault, null, true) == -1);
            if (i >= 26) {
                AndroidUtilities.setLightNavigationBar(dialog.getWindow(), AndroidUtilities.computePerceivedBrightness(Theme.getColor(Theme.key_windowBackgroundGray, null, true)) >= 0.721f);
            }
        }
        if (i >= 23) {
            rootWindowInsets = getRootWindowInsets();
            int i2 = this.popupX;
            systemWindowInsetLeft = rootWindowInsets.getSystemWindowInsetLeft();
            this.popupX = i2 + systemWindowInsetLeft;
        }
        this.senderSelectView.getLocationInWindow(this.location);
        int[] iArr2 = this.location;
        final float f = iArr2[0];
        final float f2 = iArr2[1];
        float dp = AndroidUtilities.dp(5.0f);
        float dp2 = iArr[0] + this.popupX + dp + AndroidUtilities.dp(4.0f) + 0.0f;
        float f3 = iArr[1] + this.popupY + dp + 0.0f;
        simpleAvatarView.setTranslationX(dp2);
        simpleAvatarView.setTranslationY(f3);
        float dp3 = this.senderSelectView.getLayoutParams().width / AndroidUtilities.dp(40.0f);
        simpleAvatarView.setPivotX(0.0f);
        simpleAvatarView.setPivotY(0.0f);
        simpleAvatarView.setScaleX(0.75f);
        simpleAvatarView.setScaleY(0.75f);
        simpleAvatarView.getViewTreeObserver().addOnDrawListener(new 30(simpleAvatarView, senderView));
        dialog.show();
        this.senderSelectView.setScaleX(1.0f);
        this.senderSelectView.setScaleY(1.0f);
        this.senderSelectView.setAlpha(1.0f);
        SenderSelectPopup senderSelectPopup = this.senderSelectPopupWindow;
        SenderSelectView senderSelectView = this.senderSelectView;
        DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.SCALE_X;
        SpringAnimation spring = new SpringAnimation(senderSelectView, viewProperty).setSpring(new SpringForce(0.5f).setStiffness(750.0f).setDampingRatio(1.0f));
        SenderSelectView senderSelectView2 = this.senderSelectView;
        DynamicAnimation.ViewProperty viewProperty2 = DynamicAnimation.SCALE_Y;
        senderSelectPopup.startDismissAnimation(spring, new SpringAnimation(senderSelectView2, viewProperty2).setSpring(new SpringForce(0.5f).setStiffness(750.0f).setDampingRatio(1.0f)), (SpringAnimation) new SpringAnimation(this.senderSelectView, DynamicAnimation.ALPHA).setSpring(new SpringForce(0.0f).setStiffness(750.0f).setDampingRatio(1.0f)).addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda83
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
            public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f4, float f5) {
                ChatActivityEnterView.this.lambda$createSenderSelectView$17(dialog, simpleAvatarView, f, f2, dynamicAnimation, z, f4, f5);
            }
        }), (SpringAnimation) ((SpringAnimation) new SpringAnimation(simpleAvatarView, DynamicAnimation.TRANSLATION_X).setStartValue(MathUtils.clamp(dp2, f - AndroidUtilities.dp(6.0f), dp2))).setSpring(new SpringForce(f).setStiffness(700.0f).setDampingRatio(0.75f)).setMinValue(f - AndroidUtilities.dp(6.0f)), (SpringAnimation) ((SpringAnimation) ((SpringAnimation) ((SpringAnimation) new SpringAnimation(simpleAvatarView, DynamicAnimation.TRANSLATION_Y).setStartValue(MathUtils.clamp(f3, f3, AndroidUtilities.dp(6.0f) + f2))).setSpring(new SpringForce(f2).setStiffness(700.0f).setDampingRatio(0.75f)).setMaxValue(AndroidUtilities.dp(6.0f) + f2)).addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.32
            boolean performedHapticFeedback = false;

            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f4, float f5) {
                if (this.performedHapticFeedback || f4 < f2) {
                    return;
                }
                this.performedHapticFeedback = true;
                try {
                    simpleAvatarView.performHapticFeedback(3, 2);
                } catch (Exception unused) {
                }
            }
        })).addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda84
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
            public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f4, float f5) {
                ChatActivityEnterView.this.lambda$createSenderSelectView$18(dialog, simpleAvatarView, f, f2, dynamicAnimation, z, f4, f5);
            }
        }), new SpringAnimation(simpleAvatarView, viewProperty).setSpring(new SpringForce(dp3).setStiffness(1000.0f).setDampingRatio(1.0f)), new SpringAnimation(simpleAvatarView, viewProperty2).setSpring(new SpringForce(dp3).setStiffness(1000.0f).setDampingRatio(1.0f)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x004e, code lost:
    
        if (r10 != null) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0061, code lost:
    
        r1.setAvatar(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x005f, code lost:
    
        if (r10 != null) goto L20;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$createSenderSelectView$20(TLRPC.ChatFull chatFull, MessagesController messagesController, RecyclerView recyclerView, final SenderSelectPopup.SenderView senderView, TLRPC.Peer peer) {
        TLObject user;
        if (this.senderSelectPopupWindow == null) {
            return;
        }
        if (chatFull != null) {
            chatFull.default_send_as = peer;
            updateSendAsButton();
        }
        MessagesController messagesController2 = this.parentFragment.getMessagesController();
        long j = this.dialog_id;
        long j2 = peer.user_id;
        if (j2 == 0) {
            j2 = -peer.channel_id;
        }
        messagesController2.setDefaultSendAs(j, j2);
        final int[] iArr = new int[2];
        boolean isSelected = senderView.avatar.isSelected();
        senderView.avatar.getLocationInWindow(iArr);
        senderView.avatar.setSelected(true, true);
        final SimpleAvatarView simpleAvatarView = new SimpleAvatarView(getContext());
        long j3 = peer.channel_id;
        if (j3 != 0) {
            user = messagesController.getChat(Long.valueOf(j3));
        } else {
            long j4 = peer.user_id;
            if (j4 != 0) {
                user = messagesController.getUser(Long.valueOf(j4));
            }
        }
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View childAt = recyclerView.getChildAt(i);
            if ((childAt instanceof SenderSelectPopup.SenderView) && childAt != senderView) {
                ((SenderSelectPopup.SenderView) childAt).avatar.setSelected(false, true);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda82
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$createSenderSelectView$19(simpleAvatarView, iArr, senderView);
            }
        }, isSelected ? 0L : 200L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createSenderSelectView$21(View view) {
        int i;
        int i2;
        if (getTranslationY() != 0.0f) {
            this.onEmojiSearchClosed = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda65
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$createSenderSelectView$15();
                }
            };
            hidePopup(true, true);
            return;
        }
        if (this.delegate.measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
            int contentViewHeight = this.delegate.getContentViewHeight();
            int measureKeyboardHeight = this.delegate.measureKeyboardHeight();
            if (measureKeyboardHeight <= AndroidUtilities.dp(20.0f)) {
                contentViewHeight += measureKeyboardHeight;
            }
            if (this.emojiViewVisible) {
                contentViewHeight -= getEmojiPadding();
            }
            if (contentViewHeight < AndroidUtilities.dp(200.0f)) {
                this.onKeyboardClosed = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda66
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActivityEnterView.this.lambda$createSenderSelectView$16();
                    }
                };
                closeKeyboard();
                return;
            }
        }
        if (this.delegate.getSendAsPeers() != null) {
            try {
                view.performHapticFeedback(3, 2);
            } catch (Exception unused) {
            }
            SenderSelectPopup senderSelectPopup = this.senderSelectPopupWindow;
            if (senderSelectPopup != null) {
                senderSelectPopup.setPauseNotifications(false);
                this.senderSelectPopupWindow.startDismissAnimation(new SpringAnimation[0]);
                return;
            }
            final MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
            final TLRPC.ChatFull chatFull = messagesController.getChatFull(-this.dialog_id);
            if (chatFull == null) {
                return;
            }
            final FrameLayout overlayContainerView = this.parentFragment.getParentLayout().getOverlayContainerView();
            SenderSelectPopup senderSelectPopup2 = new SenderSelectPopup(getContext(), this.parentFragment, messagesController, chatFull, this.delegate.getSendAsPeers(), new SenderSelectPopup.OnSelectCallback() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda67
                @Override // org.telegram.ui.Components.SenderSelectPopup.OnSelectCallback
                public final void onPeerSelected(RecyclerView recyclerView, SenderSelectPopup.SenderView senderView, TLRPC.Peer peer) {
                    ChatActivityEnterView.this.lambda$createSenderSelectView$20(chatFull, messagesController, recyclerView, senderView, peer);
                }
            }) { // from class: org.telegram.ui.Components.ChatActivityEnterView.29
                @Override // org.telegram.ui.Components.SenderSelectPopup, org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
                public void dismiss() {
                    if (ChatActivityEnterView.this.senderSelectPopupWindow != this) {
                        overlayContainerView.removeView(this.dimView);
                        super.dismiss();
                        return;
                    }
                    ChatActivityEnterView.this.senderSelectPopupWindow = null;
                    if (!this.runningCustomSprings) {
                        startDismissAnimation(new SpringAnimation[0]);
                        ChatActivityEnterView.this.senderSelectView.setProgress(0.0f, true, true);
                        return;
                    }
                    Iterator it = this.springAnimations.iterator();
                    while (it.hasNext()) {
                        ((SpringAnimation) it.next()).cancel();
                    }
                    this.springAnimations.clear();
                    super.dismiss();
                }
            };
            this.senderSelectPopupWindow = senderSelectPopup2;
            senderSelectPopup2.setPauseNotifications(true);
            this.senderSelectPopupWindow.setDismissAnimationDuration(NotificationCenter.updateAllMessages);
            this.senderSelectPopupWindow.setOutsideTouchable(true);
            this.senderSelectPopupWindow.setClippingEnabled(true);
            this.senderSelectPopupWindow.setFocusable(true);
            this.senderSelectPopupWindow.getContentView().measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
            this.senderSelectPopupWindow.setInputMethodMode(2);
            this.senderSelectPopupWindow.setSoftInputMode(0);
            this.senderSelectPopupWindow.getContentView().setFocusableInTouchMode(true);
            this.senderSelectPopupWindow.setAnimationEnabled(false);
            int i3 = -AndroidUtilities.dp(4.0f);
            int[] iArr = new int[2];
            if (AndroidUtilities.isTablet()) {
                this.parentFragment.getFragmentView().getLocationInWindow(iArr);
                i = iArr[0] + i3;
            } else {
                i = i3;
            }
            int contentViewHeight2 = this.delegate.getContentViewHeight();
            int measuredHeight = this.senderSelectPopupWindow.getContentView().getMeasuredHeight();
            int measureKeyboardHeight2 = this.delegate.measureKeyboardHeight();
            if (measureKeyboardHeight2 <= AndroidUtilities.dp(20.0f)) {
                contentViewHeight2 += measureKeyboardHeight2;
            }
            if (this.emojiViewVisible) {
                contentViewHeight2 -= getEmojiPadding();
            }
            int dp = AndroidUtilities.dp(1.0f);
            if (measuredHeight < (((i3 * 2) + contentViewHeight2) - (this.parentFragment.isInBubbleMode() ? 0 : AndroidUtilities.statusBarHeight)) - this.senderSelectPopupWindow.headerText.getMeasuredHeight()) {
                getLocationInWindow(iArr);
                i2 = ((iArr[1] - measuredHeight) - i3) - AndroidUtilities.dp(2.0f);
                overlayContainerView.addView(this.senderSelectPopupWindow.dimView, new FrameLayout.LayoutParams(-1, i3 + i2 + measuredHeight + dp + AndroidUtilities.dp(2.0f)));
            } else {
                int i4 = this.parentFragment.isInBubbleMode() ? 0 : AndroidUtilities.statusBarHeight;
                int dp2 = AndroidUtilities.dp(14.0f);
                this.senderSelectPopupWindow.recyclerContainer.getLayoutParams().height = ((contentViewHeight2 - i4) - dp2) - getHeightWithTopView();
                overlayContainerView.addView(this.senderSelectPopupWindow.dimView, new FrameLayout.LayoutParams(-1, dp2 + i4 + this.senderSelectPopupWindow.recyclerContainer.getLayoutParams().height + dp));
                i2 = i4;
            }
            this.senderSelectPopupWindow.startShowAnimation();
            SenderSelectPopup senderSelectPopup3 = this.senderSelectPopupWindow;
            this.popupX = i;
            this.popupY = i2;
            senderSelectPopup3.showAtLocation(view, 51, i, i2);
            this.senderSelectView.setProgress(1.0f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didPressedBotButton$63(Runnable runnable, long j) {
        runnable.run();
        SharedPrefsHelper.setWebViewConfirmShown(this.currentAccount, j, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didPressedBotButton$64(MessageObject messageObject, TLRPC.KeyboardButton keyboardButton, AlertDialog alertDialog, int i) {
        int checkSelfPermission;
        if (Build.VERSION.SDK_INT >= 23) {
            checkSelfPermission = this.parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
            if (checkSelfPermission != 0) {
                this.parentActivity.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
                this.pendingMessageObject = messageObject;
                this.pendingLocationButton = keyboardButton;
                return;
            }
        }
        SendMessagesHelper.getInstance(this.currentAccount).sendCurrentLocation(messageObject, keyboardButton);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$didPressedBotButton$65(MessageObject messageObject, TLRPC.KeyboardButton keyboardButton, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
        TLRPC.Message message = messageObject.messageOwner;
        long j = message.from_id.user_id;
        long j2 = message.via_bot_id;
        if (j2 != 0) {
            j = j2;
        }
        TLRPC.User user = this.accountInstance.getMessagesController().getUser(Long.valueOf(j));
        if (user == null) {
            dialogsActivity.lambda$onBackPressed$323();
            return true;
        }
        long j3 = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        MediaDataController.getInstance(this.currentAccount).saveDraft(j3, 0, "@" + UserObject.getPublicUsername(user) + " " + keyboardButton.query, null, null, true, 0L);
        if (j3 != this.dialog_id && !DialogObject.isEncryptedDialog(j3)) {
            Bundle bundle = new Bundle();
            if (DialogObject.isUserDialog(j3)) {
                bundle.putLong("user_id", j3);
            } else {
                bundle.putLong("chat_id", -j3);
            }
            if (!this.accountInstance.getMessagesController().checkCanOpenChat(bundle, dialogsActivity)) {
                return true;
            }
            if (this.parentFragment.presentFragment(new ChatActivity(bundle), true)) {
                if (!AndroidUtilities.isTablet()) {
                    this.parentFragment.removeSelfFromStack();
                }
                return true;
            }
        }
        dialogsActivity.lambda$onBackPressed$323();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$didPressedBotButton$66(MessageObject messageObject, TLRPC.TL_keyboardButtonRequestPeer tL_keyboardButtonRequestPeer, List list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        TLRPC.TL_messages_sendBotRequestedPeer tL_messages_sendBotRequestedPeer = new TLRPC.TL_messages_sendBotRequestedPeer();
        tL_messages_sendBotRequestedPeer.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(messageObject.messageOwner.peer_id);
        tL_messages_sendBotRequestedPeer.msg_id = messageObject.getId();
        tL_messages_sendBotRequestedPeer.button_id = tL_keyboardButtonRequestPeer.button_id;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            tL_messages_sendBotRequestedPeer.requested_peers.add(MessagesController.getInstance(this.currentAccount).getInputPeer(((Long) it.next()).longValue()));
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_sendBotRequestedPeer, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$didPressedBotButton$67(MessageObject messageObject, TLRPC.TL_keyboardButtonRequestPeer tL_keyboardButtonRequestPeer, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i, TopicsFragment topicsFragment) {
        if (arrayList != null && !arrayList.isEmpty()) {
            TLRPC.TL_messages_sendBotRequestedPeer tL_messages_sendBotRequestedPeer = new TLRPC.TL_messages_sendBotRequestedPeer();
            tL_messages_sendBotRequestedPeer.peer = MessagesController.getInstance(this.currentAccount).getInputPeer(messageObject.messageOwner.peer_id);
            tL_messages_sendBotRequestedPeer.msg_id = messageObject.getId();
            tL_messages_sendBotRequestedPeer.button_id = tL_keyboardButtonRequestPeer.button_id;
            HashSet hashSet = new HashSet();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                hashSet.add(Long.valueOf(((MessagesStorage.TopicKey) it.next()).dialogId));
            }
            Iterator it2 = hashSet.iterator();
            while (it2.hasNext()) {
                tL_messages_sendBotRequestedPeer.requested_peers.add(MessagesController.getInstance(this.currentAccount).getInputPeer(((Long) it2.next()).longValue()));
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(tL_messages_sendBotRequestedPeer, null);
        }
        dialogsActivity.lambda$onBackPressed$323();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doneEditingMessage$50() {
        this.waitingForKeyboardOpenAfterAnimation = false;
        openKeyboardInternal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.waitingForKeyboardOpenAfterAnimation = false;
        openKeyboardInternal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        AdjustPanLayoutHelper adjustPanLayoutHelper = this.adjustPanLayoutHelper;
        if (adjustPanLayoutHelper == null || !adjustPanLayoutHelper.animationInProgress()) {
            if (this.emojiButtonRestricted) {
                showRestrictedHint();
                return;
            }
            if (!isPopupShowing() || this.currentPopupContentType != 0) {
                showPopup(1, 0);
                EmojiView emojiView = this.emojiView;
                EditTextCaption editTextCaption = this.messageEditText;
                boolean z = editTextCaption != null && editTextCaption.length() > 0;
                ChatActivity chatActivity = this.parentFragment;
                emojiView.onOpen(z, chatActivity != null && chatActivity.groupEmojiPackHintWasVisible());
                return;
            }
            if (this.searchingType != 0) {
                setSearchingTypeInternal(0, true);
                EmojiView emojiView2 = this.emojiView;
                if (emojiView2 != null) {
                    emojiView2.closeSearch(false);
                }
                EditTextCaption editTextCaption2 = this.messageEditText;
                if (editTextCaption2 != null) {
                    editTextCaption2.requestFocus();
                }
            }
            if (!this.stickersExpanded) {
                openKeyboardInternal();
                return;
            }
            setStickersExpanded(false, true, false);
            this.waitingForKeyboardOpenAfterAnimation = true;
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda56
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$new$0();
                }
            }, 200L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        AdjustPanLayoutHelper adjustPanLayoutHelper = this.adjustPanLayoutHelper;
        if ((adjustPanLayoutHelper == null || !adjustPanLayoutHelper.animationInProgress()) && this.attachLayoutPaddingAlpha != 0.0f) {
            this.delegate.didPressAttachButton();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$28() {
        MessageSendPreview messageSendPreview = this.messageSendPreview;
        if (messageSendPreview != null) {
            messageSendPreview.dismiss(true);
            this.messageSendPreview = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        EditTextCaption editTextCaption = this.messageEditText;
        String obj = editTextCaption != null ? editTextCaption.getText().toString() : "";
        int indexOf = obj.indexOf(32);
        if (indexOf == -1 || indexOf == obj.length() - 1) {
            setFieldText("");
        } else {
            setFieldText(obj.substring(0, indexOf + 1));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(View view) {
        MessageSendPreview messageSendPreview = this.messageSendPreview;
        if (messageSendPreview == null || !messageSendPreview.isShowing()) {
            AnimatorSet animatorSet = this.runningAnimationAudio;
            if ((animatorSet == null || !animatorSet.isRunning()) && this.moveToSendStateRunnable == null) {
                sendMessage();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$40(ValueAnimator valueAnimator) {
        MentionsContainerView mentionsContainerView;
        if (this.topView != null) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.topViewEnterProgress = floatValue;
            float f = 1.0f - floatValue;
            this.topView.setTranslationY(this.animatedTop + (r0.getLayoutParams().height * f));
            this.topLineView.setAlpha(floatValue);
            this.topLineView.setTranslationY(this.animatedTop);
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity == null || (mentionsContainerView = chatActivity.mentionContainer) == null) {
                return;
            }
            mentionsContainerView.setTranslationY(f * this.topView.getLayoutParams().height);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(View view) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate == null || chatActivityEnterViewDelegate.checkCanRemoveRestrictionsByBoosts()) {
            return;
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
        SlowModeBtn slowModeBtn = this.slowModeButton;
        chatActivityEnterViewDelegate2.onUpdateSlowModeButton(slowModeBtn, true, slowModeBtn.getText());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$6(View view) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null || editTextCaption.length() <= 0) {
            return false;
        }
        return onSendLongClick(view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPause$42() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || chatActivity.isLastFragment()) {
            closeKeyboard();
        }
        this.hideKeyboardRunnable = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$29(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0 && (actionBarPopupWindow = this.sendPopupWindow) != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$30(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView.35
            @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
            public void didSelectDate(boolean z, int i) {
                ChatActivityEnterView.this.sendMessageInternal(z, i, true);
            }
        }, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$31(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        sendMessageInternal(true, 2147483646, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$32(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        sendMessageInternal(false, 0, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$33(DialogInterface dialogInterface) {
        this.messageSendPreview = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$34(Canvas canvas) {
        drawBackground(canvas, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$35(boolean z, View view) {
        MessageSendPreview messageSendPreview;
        this.sentFromPreview = System.currentTimeMillis();
        sendMessage();
        if (z || (messageSendPreview = this.messageSendPreview) == null) {
            AndroidUtilities.cancelRunOnUIThread(this.dismissSendPreview);
            AndroidUtilities.runOnUIThread(this.dismissSendPreview, 500L);
        } else {
            messageSendPreview.dismiss(true);
            this.messageSendPreview = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$36() {
        AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView.38
            @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
            public void didSelectDate(boolean z, int i) {
                ChatActivityEnterView.this.sendMessageInternal(z, i, true);
                MessageSendPreview messageSendPreview = ChatActivityEnterView.this.messageSendPreview;
                if (messageSendPreview != null) {
                    messageSendPreview.dismiss(true);
                    ChatActivityEnterView.this.messageSendPreview = null;
                }
            }
        }, this.resourcesProvider);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$37() {
        sendMessageInternal(true, 2147483646, true);
        MessageSendPreview messageSendPreview = this.messageSendPreview;
        if (messageSendPreview != null) {
            messageSendPreview.dismiss(true);
            this.messageSendPreview = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSendLongClick$38(boolean z) {
        MessageSendPreview messageSendPreview;
        this.sentFromPreview = System.currentTimeMillis();
        sendMessageInternal(false, 0, true);
        if (z || (messageSendPreview = this.messageSendPreview) == null) {
            AndroidUtilities.cancelRunOnUIThread(this.dismissSendPreview);
            AndroidUtilities.runOnUIThread(this.dismissSendPreview, 500L);
        } else {
            messageSendPreview.dismiss(true);
            this.messageSendPreview = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStickerSelected$69(TLRPC.Document document, String str, MessageObject.SendAnimationData sendAnimationData, boolean z, int i, Object obj, boolean z2) {
        if (this.slowModeTimer > 0 && !isInScheduleMode()) {
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                SlowModeBtn slowModeBtn = this.slowModeButton;
                chatActivityEnterViewDelegate.onUpdateSlowModeButton(slowModeBtn, true, slowModeBtn.getText());
                return;
            }
            return;
        }
        if (this.searchingType != 0) {
            setSearchingTypeInternal(0, true);
            this.emojiView.closeSearch(true);
            this.emojiView.hideSearchKeyboard();
        }
        setStickersExpanded(false, true, false);
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
        TL_stories.StoryItem replyToStory = chatActivityEnterViewDelegate2 != null ? chatActivityEnterViewDelegate2.getReplyToStory() : null;
        SendMessagesHelper sendMessagesHelper = SendMessagesHelper.getInstance(this.currentAccount);
        long j = this.dialog_id;
        MessageObject messageObject = this.replyingMessageObject;
        MessageObject threadMessage = getThreadMessage();
        ChatActivity.ReplyQuote replyQuote = this.replyingQuote;
        boolean z3 = obj instanceof TLRPC.TL_messages_stickerSet;
        ChatActivity chatActivity = this.parentFragment;
        sendMessagesHelper.sendSticker(document, str, j, messageObject, threadMessage, replyToStory, replyQuote, sendAnimationData, z, i, z3, obj, chatActivity != null ? chatActivity.quickReplyShortcut : null, chatActivity != null ? chatActivity.getQuickReplyId() : 0);
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate3 = this.delegate;
        if (chatActivityEnterViewDelegate3 != null) {
            chatActivityEnterViewDelegate3.onMessageSend(null, true, i);
        }
        if (z2) {
            setFieldText("");
        }
        MediaDataController.getInstance(this.currentAccount).addRecentSticker(0, obj, document, (int) (System.currentTimeMillis() / 1000), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openWebViewMenu$23() {
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        if (botCommandsMenuView != null) {
            botCommandsMenuView.setOpened(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openWebViewMenu$24() {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda81
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$openWebViewMenu$23();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openWebViewMenu$25() {
        AndroidUtilities.hideKeyboard(this);
        int i = this.currentAccount;
        long j = this.dialog_id;
        WebViewRequestProps of = WebViewRequestProps.of(i, j, j, this.botMenuWebViewTitle, this.botMenuWebViewUrl, 2, 0, false, null, false, null, null, 0, false, false);
        LaunchActivity launchActivity = LaunchActivity.instance;
        if (launchActivity != null && launchActivity.getBottomSheetTabs() != null && LaunchActivity.instance.getBottomSheetTabs().tryReopenTab(of) != null) {
            BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
            if (botCommandsMenuView != null) {
                botCommandsMenuView.setOpened(false);
                return;
            }
            return;
        }
        if (AndroidUtilities.isWebAppLink(this.botMenuWebViewUrl)) {
            Browser.Progress progress = new Browser.Progress();
            progress.onEnd(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda80
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$openWebViewMenu$24();
                }
            });
            Browser.openAsInternalIntent(getContext(), this.botMenuWebViewUrl, false, false, progress);
            return;
        }
        BotWebViewSheet botWebViewSheet = new BotWebViewSheet(getContext(), this.resourcesProvider);
        botWebViewSheet.setDefaultFullsize(false);
        botWebViewSheet.setNeedsContext(true);
        botWebViewSheet.setParentActivity(this.parentActivity);
        botWebViewSheet.requestWebView(this.parentFragment, of);
        botWebViewSheet.show();
        BotCommandsMenuView botCommandsMenuView2 = this.botCommandsMenuButton;
        if (botCommandsMenuView2 != null) {
            botCommandsMenuView2.setOpened(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openWebViewMenu$26(Runnable runnable) {
        runnable.run();
        SharedPrefsHelper.setWebViewConfirmShown(this.currentAccount, this.dialog_id, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$openWebViewMenu$27() {
        if (this.botCommandsMenuButton == null || SharedPrefsHelper.isWebViewConfirmShown(this.currentAccount, this.dialog_id)) {
            return;
        }
        this.botCommandsMenuButton.setOpened(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveBusinessLink$49() {
        BulletinFactory.of(this.parentFragment).createSuccessBulletin(LocaleController.getString(R.string.BusinessLinkSaved)).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessageInternal$43(boolean z, int i) {
        sendMessageInternal(z, i, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessageInternal$44() {
        RecordCircle recordCircle = this.recordCircle;
        if (recordCircle != null) {
            recordCircle.setSendButtonInvisible();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessageInternal$45() {
        RecordCircle recordCircle = this.recordCircle;
        if (recordCircle != null) {
            recordCircle.setSendButtonInvisible();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendMessageInternal$46(CharSequence charSequence, boolean z, int i) {
        this.moveToSendStateRunnable = null;
        hideTopView(true);
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.setText("");
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onMessageSend(charSequence, z, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setButtons$62(TLRPC.KeyboardButton keyboardButton) {
        ChatActivity chatActivity;
        boolean z = this.replyingMessageObject != null && (chatActivity = this.parentFragment) != null && chatActivity.isTopic && chatActivity.getTopicId() == ((long) this.replyingMessageObject.getId());
        MessageObject messageObject = this.replyingMessageObject;
        if (messageObject == null || z) {
            messageObject = DialogObject.isChatDialog(this.dialog_id) ? this.botButtonsMessageObject : null;
        }
        MessageObject messageObject2 = this.replyingMessageObject;
        if (messageObject2 == null || z) {
            messageObject2 = this.botButtonsMessageObject;
        }
        boolean didPressedBotButton = didPressedBotButton(keyboardButton, messageObject, messageObject2);
        if (this.replyingMessageObject == null || z) {
            MessageObject messageObject3 = this.botButtonsMessageObject;
            if (messageObject3 != null && messageObject3.messageOwner.reply_markup.single_use) {
                if (didPressedBotButton) {
                    openKeyboardInternal();
                } else {
                    showPopup(0, 0);
                }
                MessagesController.getMainSettings(this.currentAccount).edit().putInt("answered_" + getTopicKeyString(), this.botButtonsMessageObject.getId()).commit();
            }
        } else {
            openKeyboardInternal();
            setButtons(this.botMessageObject, false);
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onMessageSend(null, true, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setEditingBusinessLink$53(View view) {
        saveBusinessLink();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setEditingMessageObject$54(View view) {
        doneEditingMessage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setEditingMessageObject$55(ArrayList arrayList, MessagePreviewView.ToggleButton toggleButton, MessageSendPreview messageSendPreview, View view) {
        this.captionAbove = !this.captionAbove;
        for (int i = 0; i < arrayList.size(); i++) {
            ((MessageObject) arrayList.get(i)).messageOwner.invert_media = this.captionAbove;
        }
        toggleButton.setState(!this.captionAbove, true);
        if (!arrayList.isEmpty()) {
            messageSendPreview.changeMessage((MessageObject) arrayList.get(0));
        }
        messageSendPreview.scrollTo(!this.captionAbove);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setEditingMessageObject$56(MessageObject.GroupedMessages groupedMessages, MessageObject messageObject, MessageSendPreview messageSendPreview, View view) {
        if (groupedMessages != null) {
            Iterator<MessageObject> it = groupedMessages.messages.iterator();
            while (it.hasNext()) {
                it.next().messageOwner.invert_media = this.captionAbove;
            }
            groupedMessages.calculate();
        } else {
            messageObject.messageOwner.invert_media = this.captionAbove;
        }
        doneEditingMessage();
        messageSendPreview.dismiss(true);
        this.captionAbove = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setEditingMessageObject$57(final MessageObject messageObject, final MessageObject.GroupedMessages groupedMessages, View view) {
        EditTextCaption editTextCaption;
        if (messageObject.isMediaEmpty() || (editTextCaption = this.messageEditText) == null || TextUtils.isEmpty(editTextCaption.getTextToUse())) {
            return false;
        }
        if (groupedMessages != null && (!groupedMessages.hasCaption || groupedMessages.isDocuments)) {
            return false;
        }
        int i = messageObject.type;
        if (i != 1 && i != 3 && i != 8) {
            return false;
        }
        final MessageSendPreview messageSendPreview = new MessageSendPreview(getContext(), this.resourcesProvider);
        messageSendPreview.allowRelayout = true;
        final ArrayList arrayList = new ArrayList();
        if (groupedMessages != null) {
            int i2 = 0;
            while (i2 < groupedMessages.messages.size()) {
                arrayList.add(editingMessageObjectPreview(groupedMessages.messages.get(i2), i2 == 0));
                i2++;
            }
        } else {
            arrayList.add(editingMessageObjectPreview(messageObject, true));
        }
        messageSendPreview.setMessageObjects(arrayList);
        ItemOptions makeOptions = ItemOptions.makeOptions(this.sizeNotifierLayout, this.resourcesProvider, this.doneButton);
        final MessagePreviewView.ToggleButton toggleButton = new MessagePreviewView.ToggleButton(getContext(), R.raw.position_below, LocaleController.getString(R.string.CaptionAbove), R.raw.position_above, LocaleController.getString(R.string.CaptionBelow), this.resourcesProvider);
        toggleButton.setState(!this.captionAbove, false);
        toggleButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda78
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ChatActivityEnterView.this.lambda$setEditingMessageObject$55(arrayList, toggleButton, messageSendPreview, view2);
            }
        });
        makeOptions.addView(toggleButton);
        makeOptions.setupSelectors();
        messageSendPreview.setItemOptions(makeOptions);
        messageSendPreview.setSendButton(this.doneButton, false, new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda79
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ChatActivityEnterView.this.lambda$setEditingMessageObject$56(groupedMessages, messageObject, messageSendPreview, view2);
            }
        });
        messageSendPreview.show();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setEditingMessageObject$58(CharSequence charSequence) {
        setFieldText(charSequence);
        this.setTextFieldRunnable = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setFieldFocused$59() {
        boolean z;
        EditTextCaption editTextCaption;
        ViewGroup viewGroup = null;
        this.focusRunnable = null;
        if (AndroidUtilities.isTablet()) {
            Activity activity = this.parentActivity;
            if (activity instanceof LaunchActivity) {
                LaunchActivity launchActivity = (LaunchActivity) activity;
                if (launchActivity != null && launchActivity.getLayersActionBarLayout() != null) {
                    viewGroup = launchActivity.getLayersActionBarLayout().getView();
                }
                if (viewGroup != null && viewGroup.getVisibility() == 0) {
                    z = false;
                    if (this.isPaused && z && (editTextCaption = this.messageEditText) != null) {
                        try {
                            editTextCaption.requestFocus();
                            return;
                        } catch (Exception e) {
                            FileLog.e(e);
                            return;
                        }
                    }
                    return;
                }
            }
        }
        z = true;
        if (this.isPaused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setSearchingTypeInternal$72(ValueAnimator valueAnimator) {
        this.searchToOpenProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.searchProgressChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setStickersExpanded$76(int i, ValueAnimator valueAnimator) {
        this.stickersExpansionProgress = Math.abs(getTranslationY() / (-(this.stickersExpandedHeight - i)));
        this.sizeNotifierLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setStickersExpanded$77(int i, ValueAnimator valueAnimator) {
        this.stickersExpansionProgress = getTranslationY() / (-(this.stickersExpandedHeight - i));
        this.sizeNotifierLayout.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showCaptionLimitBulletin$48() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            chatActivity.presentFragment(new PremiumPreviewFragment("caption_limit"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPopup$70() {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.bottomPanelTranslationYChanged(0.0f);
        }
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPopup$71(int i) {
        if (i == 0) {
            this.emojiPadding = 0;
        }
        this.panelAnimation = null;
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.setTranslationY(0.0f);
            this.emojiView.setVisibility(8);
            this.sizeNotifierLayout.removeView(this.emojiView);
            if (this.removeEmojiViewAfterAnimation) {
                this.removeEmojiViewAfterAnimation = false;
                this.emojiView = null;
            }
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.bottomPanelTranslationYChanged(0.0f);
        }
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showTopView$41() {
        showTopView(true, false, true);
        this.showTopViewRunnable = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateRecordInterface$51(ValueAnimator valueAnimator) {
        this.recordCircle.setTransformToSeekbar(((Float) valueAnimator.getAnimatedValue()).floatValue());
        if (!isInVideoMode()) {
            this.seekBarWaveform.setWaveScaling(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioTimeTextView.setAlpha(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioPlayButton.setAlpha(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioPlayButton.setScaleX(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioPlayButton.setScaleY(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioSeekBar.setAlpha(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioSeekBar.invalidate();
        }
        isRecordingStateChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSendAsButton$61(float f, float f2, float f3, float f4, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        float f5 = f + ((f2 - f) * floatValue);
        SenderSelectView senderSelectView = this.senderSelectView;
        if (senderSelectView != null) {
            senderSelectView.setAlpha(f3 + ((f4 - f3) * floatValue));
            this.senderSelectView.setTranslationX(f5);
        }
        this.emojiButton.setTranslationX(f5);
        this.messageTextTranslationX = f5;
        updateMessageTextParams();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't wrap try/catch for region: R(15:148|(1:231)(1:152)|153|(8:155|(1:184)(1:159)|(1:183)(1:165)|166|(5:168|(1:170)(1:177)|171|172|(1:176))|(1:179)|180|(1:182))|185|(3:187|(1:189)(1:191)|190)|192|(4:194|(1:196)(1:212)|(2:200|(1:210))|211)|213|(4:215|(1:229)(1:219)|220|(5:222|223|224|225|226))|230|223|224|225|226) */
    /* JADX WARN: Can't wrap try/catch for region: R(30:13|(1:15)|16|(1:137)(1:22)|23|(3:25|(1:29)|30)(1:(9:102|(1:104)(1:129)|105|(3:109|(1:111)|112)|113|(3:115|(1:121)|122)|123|(1:127)|128)(25:130|(18:136|33|(1:37)|38|(1:100)|41|(1:97)(1:45)|46|(1:96)(1:50)|(1:95)|(4:57|(1:59)(1:65)|60|(1:64))|(1:67)|68|(4:70|(1:72)|(2:76|(1:86))|87)|88|89|90|91)|32|33|(2:35|37)|38|(0)|98|100|41|(1:43)|97|46|(1:48)|96|(2:52|54)|95|(0)|(0)|68|(0)|88|89|90|91))|31|32|33|(0)|38|(0)|98|100|41|(0)|97|46|(0)|96|(0)|95|(0)|(0)|68|(0)|88|89|90|91) */
    /* JADX WARN: Removed duplicated region for block: B:35:0x01e1  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0204 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0226  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0237  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0242  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0250  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0285  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x029c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onSendLongClick(View view) {
        ChatActivity chatActivity;
        int measuredHeight;
        EditTextCaption editTextCaption;
        ChatActivity chatActivity2;
        MessagePreviewParams messagePreviewParams;
        MessagePreviewParams.Messages messages;
        ArrayList<MessageObject> arrayList;
        InstantCameraView instantCameraView;
        boolean z;
        MessageObject messageObject;
        MessagePreviewParams messagePreviewParams2;
        boolean z2;
        boolean z3;
        EditTextCaption editTextCaption2;
        boolean z4 = true;
        if (isInScheduleMode() || ((chatActivity = this.parentFragment) != null && chatActivity.getChatMode() == 5)) {
            return false;
        }
        if (this.isStories || !(((editTextCaption = this.messageEditText) != null && !TextUtils.isEmpty(editTextCaption.getText())) || (chatActivity2 = this.parentFragment) == null || (messagePreviewParams = chatActivity2.messagePreviewParams) == null || (messages = messagePreviewParams.forwardMessages) == null || (arrayList = messages.messages) == null || arrayList.isEmpty())) {
            ChatActivity chatActivity3 = this.parentFragment;
            boolean z5 = chatActivity3 != null && UserObject.isUserSelf(chatActivity3.getCurrentUser());
            if (this.sendPopupLayout == null) {
                ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.parentActivity, this.resourcesProvider);
                this.sendPopupLayout = actionBarPopupWindowLayout;
                actionBarPopupWindowLayout.setAnimationEnabled(false);
                this.sendPopupLayout.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.34
                    private android.graphics.Rect popupRect = new android.graphics.Rect();

                    @Override // android.view.View.OnTouchListener
                    public boolean onTouch(View view2, MotionEvent motionEvent) {
                        if (motionEvent.getActionMasked() != 0 || ChatActivityEnterView.this.sendPopupWindow == null || !ChatActivityEnterView.this.sendPopupWindow.isShowing()) {
                            return false;
                        }
                        view2.getHitRect(this.popupRect);
                        if (this.popupRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                            return false;
                        }
                        ChatActivityEnterView.this.sendPopupWindow.dismiss();
                        return false;
                    }
                });
                this.sendPopupLayout.setDispatchKeyEventListener(new ActionBarPopupWindow.OnDispatchKeyEventListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda52
                    @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
                    public final void onDispatchKeyEvent(KeyEvent keyEvent) {
                        ChatActivityEnterView.this.lambda$onSendLongClick$29(keyEvent);
                    }
                });
                this.sendPopupLayout.setShownFromBottom(false);
                ChatActivity chatActivity4 = this.parentFragment;
                boolean z6 = chatActivity4 != null && chatActivity4.canScheduleMessage();
                boolean z7 = !z5 && (this.slowModeTimer <= 0 || isInScheduleMode());
                if (z6) {
                    boolean z8 = !z7;
                    ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getContext(), true, z8, this.resourcesProvider);
                    this.actionScheduleButton = actionBarMenuSubItem;
                    actionBarMenuSubItem.setTextAndIcon(LocaleController.getString(z5 ? R.string.SetReminder : R.string.ScheduleMessage), R.drawable.msg_calendar2);
                    this.actionScheduleButton.setMinimumWidth(AndroidUtilities.dp(196.0f));
                    this.actionScheduleButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda53
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatActivityEnterView.this.lambda$onSendLongClick$30(view2);
                        }
                    });
                    this.sendPopupLayout.addView((View) this.actionScheduleButton, LayoutHelper.createLinear(-1, 48));
                    SharedConfig.removeScheduledHint();
                    if (!z5 && this.dialog_id > 0) {
                        ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(getContext(), true, z8, this.resourcesProvider);
                        this.sendWhenOnlineButton = actionBarMenuSubItem2;
                        actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString(R.string.SendWhenOnline), R.drawable.msg_online);
                        this.sendWhenOnlineButton.setMinimumWidth(AndroidUtilities.dp(196.0f));
                        this.sendWhenOnlineButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda54
                            @Override // android.view.View.OnClickListener
                            public final void onClick(View view2) {
                                ChatActivityEnterView.this.lambda$onSendLongClick$31(view2);
                            }
                        });
                        this.sendPopupLayout.addView((View) this.sendWhenOnlineButton, LayoutHelper.createLinear(-1, 48));
                    }
                }
                if (z7) {
                    ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(getContext(), !z6, true, this.resourcesProvider);
                    actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString(R.string.SendWithoutSound), R.drawable.input_notify_off);
                    actionBarMenuSubItem3.setMinimumWidth(AndroidUtilities.dp(196.0f));
                    actionBarMenuSubItem3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda45
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatActivityEnterView.this.lambda$onSendLongClick$32(view2);
                        }
                    });
                    this.sendPopupLayout.addView((View) actionBarMenuSubItem3, LayoutHelper.createLinear(-1, 48));
                }
                this.sendPopupLayout.setupRadialSelectors(getThemedColor(Theme.key_dialogButtonSelector));
                int i = -2;
                ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(this.sendPopupLayout, i, i) { // from class: org.telegram.ui.Components.ChatActivityEnterView.36
                    @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
                    public void dismiss() {
                        super.dismiss();
                        ChatActivityEnterView.this.sendButton.invalidate();
                    }
                };
                this.sendPopupWindow = actionBarPopupWindow;
                actionBarPopupWindow.setAnimationEnabled(false);
                this.sendPopupWindow.setAnimationStyle(R.style.PopupContextAnimation2);
                this.sendPopupWindow.setOutsideTouchable(true);
                this.sendPopupWindow.setClippingEnabled(true);
                this.sendPopupWindow.setInputMethodMode(2);
                this.sendPopupWindow.setSoftInputMode(0);
                this.sendPopupWindow.getContentView().setFocusableInTouchMode(true);
                SharedConfig.removeScheduledOrNoSoundHint();
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
                if (chatActivityEnterViewDelegate != null) {
                    chatActivityEnterViewDelegate.onSendLongClick();
                }
            }
            ActionBarMenuSubItem actionBarMenuSubItem4 = this.actionScheduleButton;
            if (actionBarMenuSubItem4 != null) {
                actionBarMenuSubItem4.setVisibility(this.voiceOnce ? 8 : 0);
            }
            if (this.sendWhenOnlineButton != null) {
                ChatActivity chatActivity5 = this.parentFragment;
                TLRPC.User currentUser = chatActivity5 == null ? null : chatActivity5.getCurrentUser();
                if (currentUser != null && !currentUser.bot) {
                    TLRPC.UserStatus userStatus = currentUser.status;
                    if (!(userStatus instanceof TLRPC.TL_userStatusEmpty) && !(userStatus instanceof TLRPC.TL_userStatusOnline) && !(userStatus instanceof TLRPC.TL_userStatusRecently) && !(userStatus instanceof TLRPC.TL_userStatusLastMonth) && !(userStatus instanceof TLRPC.TL_userStatusLastWeek)) {
                        this.sendWhenOnlineButton.setVisibility(0);
                    }
                }
                this.sendWhenOnlineButton.setVisibility(8);
            }
            this.sendPopupLayout.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1000.0f), Integer.MIN_VALUE));
            this.sendPopupWindow.setFocusable(true);
            view.getLocationInWindow(this.location);
            if (this.keyboardVisible) {
                int measuredHeight2 = getMeasuredHeight();
                View view2 = this.topView;
                if (measuredHeight2 > AndroidUtilities.dp((view2 == null || view2.getVisibility() != 0) ? 58.0f : 106.0f)) {
                    measuredHeight = this.location[1] + view.getMeasuredHeight();
                    this.sendPopupWindow.showAtLocation(view, 51, ((this.location[0] + view.getMeasuredWidth()) - this.sendPopupLayout.getMeasuredWidth()) + AndroidUtilities.dp(8.0f), measuredHeight);
                    this.sendPopupWindow.dimBehind();
                    this.sendButton.invalidate();
                    view.performHapticFeedback(3, 2);
                    return true;
                }
            }
            measuredHeight = (this.location[1] - this.sendPopupLayout.getMeasuredHeight()) - AndroidUtilities.dp(2.0f);
            this.sendPopupWindow.showAtLocation(view, 51, ((this.location[0] + view.getMeasuredWidth()) - this.sendPopupLayout.getMeasuredWidth()) + AndroidUtilities.dp(8.0f), measuredHeight);
            this.sendPopupWindow.dimBehind();
            this.sendButton.invalidate();
            view.performHapticFeedback(3, 2);
            return true;
        }
        MessageSendPreview messageSendPreview = this.messageSendPreview;
        if (messageSendPreview != null) {
            messageSendPreview.dismiss(false);
        }
        AndroidUtilities.cancelRunOnUIThread(this.dismissSendPreview);
        MessageSendPreview messageSendPreview2 = new MessageSendPreview(getContext(), this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatActivityEnterView.37
            @Override // org.telegram.ui.MessageSendPreview
            protected void onEffectChange(long j) {
                ChatActivityEnterView.this.setEffectId(j);
            }
        };
        this.messageSendPreview = messageSendPreview2;
        messageSendPreview2.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda44
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                ChatActivityEnterView.this.lambda$onSendLongClick$33(dialogInterface);
            }
        });
        final boolean z9 = (this.audioToSendMessageObject == null && ((editTextCaption2 = this.messageEditText) == null || TextUtils.isEmpty(editTextCaption2.getText()))) ? false : true;
        ArrayList arrayList2 = new ArrayList();
        if (this.audioToSend != null) {
            TLRPC.TL_message tL_message = new TLRPC.TL_message();
            tL_message.id = 0;
            tL_message.out = true;
            tL_message.peer_id = MessagesController.getInstance(this.currentAccount).getPeer(this.dialog_id);
            tL_message.from_id = MessagesController.getInstance(this.currentAccount).getPeer(UserConfig.getInstance(this.currentAccount).getClientUserId());
            TLRPC.TL_messageMediaDocument tL_messageMediaDocument = new TLRPC.TL_messageMediaDocument();
            tL_message.media = tL_messageMediaDocument;
            tL_messageMediaDocument.voice = true;
            tL_messageMediaDocument.document = this.audioToSend;
            tL_message.send_state = 1;
            tL_message.attachPath = this.audioToSendPath;
            messageObject = new MessageObject(this.currentAccount, tL_message, false, true);
            MessageObject messageObject2 = this.replyingMessageObject;
            if (messageObject2 != null && !messageObject2.isTopicMainMessage) {
                messageObject.replyMessageObject = messageObject2;
            }
            messageObject.isOutOwnerCached = Boolean.TRUE;
            messageObject.generateLayout(null);
            messageObject.notime = true;
            messageObject.sendPreview = true;
        } else {
            if (!z9) {
                ChatActivity chatActivity6 = this.parentFragment;
                if (chatActivity6 != null && (instantCameraView = chatActivity6.instantCameraView) != null && instantCameraView.getTextureView() != null) {
                    this.messageSendPreview.setCameraTexture(this.parentFragment.instantCameraView.getTextureView());
                    z = true;
                    this.messageSendPreview.setMessageObjects(arrayList2);
                    if (z9 && this.audioToSend == null) {
                        this.messageSendPreview.setEditText(this.messageEditText, new Utilities.Callback2() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda46
                            @Override // org.telegram.messenger.Utilities.Callback2
                            public final void run(Object obj, Object obj2) {
                                ChatActivityEnterView.this.drawMessageEditText((Canvas) obj, (Utilities.Callback0Return) obj2);
                            }
                        }, new Utilities.Callback() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda47
                            @Override // org.telegram.messenger.Utilities.Callback
                            public final void run(Object obj) {
                                ChatActivityEnterView.this.lambda$onSendLongClick$34((Canvas) obj);
                            }
                        });
                    }
                    this.messageSendPreview.setSendButton(this.sendButton, true, new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda48
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view3) {
                            ChatActivityEnterView.this.lambda$onSendLongClick$35(z9, view3);
                        }
                    });
                    if ((!z9 || z) && this.dialog_id >= 0) {
                        this.messageSendPreview.allowEffectSelector(this.parentFragment);
                        this.messageSendPreview.setEffectId(this.effectId);
                    }
                    ItemOptions makeOptions = ItemOptions.makeOptions(this, this.resourcesProvider, this.sendButton);
                    ChatActivity chatActivity7 = this.parentFragment;
                    z2 = chatActivity7 == null && UserObject.isUserSelf(chatActivity7.getCurrentUser());
                    ChatActivity chatActivity8 = this.parentFragment;
                    z3 = chatActivity8 == null && chatActivity8.canScheduleMessage();
                    if (!z2 || (this.slowModeTimer > 0 && !isInScheduleMode())) {
                        z4 = false;
                    }
                    if (z3) {
                        makeOptions.add(R.drawable.msg_calendar2, LocaleController.getString(z2 ? R.string.SetReminder : R.string.ScheduleMessage), new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda49
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.this.lambda$onSendLongClick$36();
                            }
                        });
                        if (!z2 && this.dialog_id > 0) {
                            makeOptions.add(R.drawable.msg_online, LocaleController.getString(R.string.SendWhenOnline), new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda50
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ChatActivityEnterView.this.lambda$onSendLongClick$37();
                                }
                            });
                            this.sendWhenOnlineButton = makeOptions.getLast();
                        }
                    }
                    if (z4) {
                        makeOptions.add(R.drawable.input_notify_off, LocaleController.getString(R.string.SendWithoutSound), new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda51
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.this.lambda$onSendLongClick$38(z9);
                            }
                        });
                    }
                    makeOptions.setupSelectors();
                    if (this.sendWhenOnlineButton != null) {
                        ChatActivity chatActivity9 = this.parentFragment;
                        TLRPC.User currentUser2 = chatActivity9 != null ? chatActivity9.getCurrentUser() : null;
                        if (currentUser2 != null && !currentUser2.bot) {
                            TLRPC.UserStatus userStatus2 = currentUser2.status;
                            if (!(userStatus2 instanceof TLRPC.TL_userStatusEmpty) && !(userStatus2 instanceof TLRPC.TL_userStatusOnline) && !(userStatus2 instanceof TLRPC.TL_userStatusRecently) && !(userStatus2 instanceof TLRPC.TL_userStatusLastMonth) && !(userStatus2 instanceof TLRPC.TL_userStatusLastWeek)) {
                                this.sendWhenOnlineButton.setVisibility(0);
                            }
                        }
                        this.sendWhenOnlineButton.setVisibility(8);
                    }
                    this.messageSendPreview.setItemOptions(makeOptions);
                    this.messageSendPreview.show();
                    view.performHapticFeedback(3, 2);
                    return false;
                }
                z = false;
                this.messageSendPreview.setMessageObjects(arrayList2);
                if (z9) {
                    this.messageSendPreview.setEditText(this.messageEditText, new Utilities.Callback2() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda46
                        @Override // org.telegram.messenger.Utilities.Callback2
                        public final void run(Object obj, Object obj2) {
                            ChatActivityEnterView.this.drawMessageEditText((Canvas) obj, (Utilities.Callback0Return) obj2);
                        }
                    }, new Utilities.Callback() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda47
                        @Override // org.telegram.messenger.Utilities.Callback
                        public final void run(Object obj) {
                            ChatActivityEnterView.this.lambda$onSendLongClick$34((Canvas) obj);
                        }
                    });
                }
                this.messageSendPreview.setSendButton(this.sendButton, true, new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda48
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view3) {
                        ChatActivityEnterView.this.lambda$onSendLongClick$35(z9, view3);
                    }
                });
                if (!z9) {
                }
                this.messageSendPreview.allowEffectSelector(this.parentFragment);
                this.messageSendPreview.setEffectId(this.effectId);
                ItemOptions makeOptions2 = ItemOptions.makeOptions(this, this.resourcesProvider, this.sendButton);
                ChatActivity chatActivity72 = this.parentFragment;
                if (chatActivity72 == null) {
                }
                ChatActivity chatActivity82 = this.parentFragment;
                if (chatActivity82 == null) {
                }
                if (!z2) {
                }
                z4 = false;
                if (z3) {
                }
                if (z4) {
                }
                makeOptions2.setupSelectors();
                if (this.sendWhenOnlineButton != null) {
                }
                this.messageSendPreview.setItemOptions(makeOptions2);
                this.messageSendPreview.show();
                view.performHapticFeedback(3, 2);
                return false;
            }
            TLRPC.TL_message tL_message2 = new TLRPC.TL_message();
            tL_message2.id = 0;
            tL_message2.out = true;
            tL_message2.peer_id = MessagesController.getInstance(this.currentAccount).getPeer(this.dialog_id);
            tL_message2.from_id = MessagesController.getInstance(this.currentAccount).getPeer(UserConfig.getInstance(this.currentAccount).getClientUserId());
            EditTextCaption editTextCaption3 = this.messageEditText;
            CharSequence[] charSequenceArr = {new SpannableStringBuilder(editTextCaption3 == null ? "" : editTextCaption3.getTextToUse())};
            MessageObject.addLinks(true, charSequenceArr[0]);
            tL_message2.entities.addAll(MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, true));
            tL_message2.message = charSequenceArr[0].toString();
            MessageObject messageObject3 = this.replyingMessageObject;
            if (messageObject3 != null && !messageObject3.isTopicMainMessage) {
                TLRPC.TL_messageReplyHeader tL_messageReplyHeader = new TLRPC.TL_messageReplyHeader();
                MessageObject messageObject4 = this.replyingTopMessage;
                if (messageObject4 != null) {
                    tL_messageReplyHeader.flags |= 2;
                    tL_messageReplyHeader.reply_to_top_id = messageObject4.getId();
                }
                tL_messageReplyHeader.flags |= 16;
                tL_messageReplyHeader.reply_to_msg_id = this.replyingMessageObject.getId();
                tL_message2.reply_to = tL_messageReplyHeader;
            }
            if (this.messageWebPage != null) {
                TLRPC.TL_messageMediaWebPage tL_messageMediaWebPage = new TLRPC.TL_messageMediaWebPage();
                tL_messageMediaWebPage.webpage = this.messageWebPage;
                ChatActivity chatActivity10 = this.parentFragment;
                if (chatActivity10 != null && (messagePreviewParams2 = chatActivity10.messagePreviewParams) != null && messagePreviewParams2.hasMedia) {
                    boolean z10 = messagePreviewParams2.webpageSmall;
                    tL_messageMediaWebPage.force_small_media = z10;
                    tL_messageMediaWebPage.force_large_media = !z10;
                    tL_message2.invert_media = messagePreviewParams2.webpageTop;
                }
                tL_message2.media = tL_messageMediaWebPage;
            }
            messageObject = new MessageObject(this.currentAccount, tL_message2, false, false);
            MessageObject messageObject5 = this.replyingMessageObject;
            if (messageObject5 != null && !messageObject5.isTopicMainMessage) {
                messageObject.replyMessageObject = messageObject5;
            }
            messageObject.sendPreview = true;
            messageObject.isOutOwnerCached = Boolean.TRUE;
            messageObject.type = 0;
            messageObject.generateLayout(null);
            messageObject.notime = true;
        }
        arrayList2.add(messageObject);
        z = false;
        this.messageSendPreview.setMessageObjects(arrayList2);
        if (z9) {
        }
        this.messageSendPreview.setSendButton(this.sendButton, true, new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda48
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                ChatActivityEnterView.this.lambda$onSendLongClick$35(z9, view3);
            }
        });
        if (!z9) {
        }
        this.messageSendPreview.allowEffectSelector(this.parentFragment);
        this.messageSendPreview.setEffectId(this.effectId);
        ItemOptions makeOptions22 = ItemOptions.makeOptions(this, this.resourcesProvider, this.sendButton);
        ChatActivity chatActivity722 = this.parentFragment;
        if (chatActivity722 == null) {
        }
        ChatActivity chatActivity822 = this.parentFragment;
        if (chatActivity822 == null) {
        }
        if (!z2) {
        }
        z4 = false;
        if (z3) {
        }
        if (z4) {
        }
        makeOptions22.setupSelectors();
        if (this.sendWhenOnlineButton != null) {
        }
        this.messageSendPreview.setItemOptions(makeOptions22);
        this.messageSendPreview.show();
        view.performHapticFeedback(3, 2);
        return false;
    }

    private void onWindowSizeChanged() {
        int height = this.sizeNotifierLayout.getHeight();
        if (!this.keyboardVisible) {
            height -= this.emojiPadding;
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onWindowSizeChanged(height);
        }
        if (this.topView != null) {
            if (height >= AndroidUtilities.dp(72.0f) + ActionBar.getCurrentActionBarHeight()) {
                if (this.allowShowTopView) {
                    return;
                }
                this.allowShowTopView = true;
                if (this.needShowTopView) {
                    this.topView.setVisibility(0);
                    this.topLineView.setVisibility(0);
                    this.topLineView.setAlpha(1.0f);
                    resizeForTopView(true);
                    this.topViewEnterProgress = 1.0f;
                    this.topView.setTranslationY(0.0f);
                    return;
                }
                return;
            }
            if (this.allowShowTopView) {
                this.allowShowTopView = false;
                if (this.needShowTopView) {
                    this.topView.setVisibility(8);
                    this.topLineView.setVisibility(8);
                    this.topLineView.setAlpha(0.0f);
                    resizeForTopView(false);
                    this.topViewEnterProgress = 0.0f;
                    this.topView.setTranslationY(r0.getLayoutParams().height);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openWebViewMenu() {
        createBotWebViewMenuContainer();
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda72
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$openWebViewMenu$25();
            }
        };
        if (SharedPrefsHelper.isWebViewConfirmShown(this.currentAccount, this.dialog_id)) {
            runnable.run();
        } else {
            AlertsCreator.createBotLaunchAlert(this.parentFragment, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialog_id)), new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda73
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$openWebViewMenu$26(runnable);
                }
            }, new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda74
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$openWebViewMenu$27();
                }
            });
        }
    }

    private void resetRecordedState() {
        if (this.videoToSendMessageObject != null) {
            CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
            this.delegate.needStartRecordVideo(2, true, 0, this.voiceOnce ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0, this.effectId);
            SendButton sendButton = this.sendButton;
            this.effectId = 0L;
            sendButton.setEffect(0L);
        } else {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null && playingMessageObject == this.audioToSendMessageObject) {
                MediaController.getInstance().cleanupPlayer(true, true);
            }
        }
        if (this.audioToSendPath != null) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("delete file " + this.audioToSendPath);
            }
            new File(this.audioToSendPath).delete();
        }
        MediaController.getInstance().cleanRecording(true);
        MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
        long j = this.dialog_id;
        ChatActivity chatActivity = this.parentFragment;
        mediaDataController.pushDraftVoiceMessage(j, (chatActivity == null || !chatActivity.isTopic) ? 0L : chatActivity.getTopicId(), null);
        MediaController.getInstance().stopRecording(0, false, 0, false);
        this.millisecondsRecorded = 0L;
        hideRecordedAudioPanel(false);
        checkSendButton(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resizeForTopView(boolean z) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.textFieldContainer.getLayoutParams();
        layoutParams.topMargin = AndroidUtilities.dp(2.0f) + (z ? this.topView.getLayoutParams().height : 0);
        this.textFieldContainer.setLayoutParams(layoutParams);
        setMinimumHeight(AndroidUtilities.dp(51.0f) + (z ? this.topView.getLayoutParams().height : 0));
        if (this.stickersExpanded) {
            if (this.searchingType == 0) {
                setStickersExpanded(false, true, false);
            } else {
                checkStickresExpandHeight();
            }
        }
    }

    private void saveBusinessLink() {
        if (isEditingBusinessLink()) {
            if (this.currentLimit - this.codePointCount >= 0) {
                BusinessLinkPresetMessage calculateBusinessLinkPresetMessage = calculateBusinessLinkPresetMessage();
                this.lastSavedBusinessLinkMessage = calculateBusinessLinkPresetMessage;
                BusinessLinksController.getInstance(this.currentAccount).editLinkMessage(this.editingBusinessLink.link, calculateBusinessLinkPresetMessage.text, calculateBusinessLinkPresetMessage.entities, new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda43
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActivityEnterView.this.lambda$saveBusinessLink$49();
                    }
                });
            } else {
                NumberTextView numberTextView = this.captionLimitView;
                if (numberTextView != null) {
                    AndroidUtilities.shakeViewSpring(numberTextView, 3.5f);
                    try {
                        this.captionLimitView.performHapticFeedback(3, 2);
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage() {
        if (isInScheduleMode()) {
            AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView.50
                @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                public void didSelectDate(boolean z, int i) {
                    MessageSendPreview messageSendPreview = ChatActivityEnterView.this.messageSendPreview;
                    if (messageSendPreview != null) {
                        messageSendPreview.dismiss(true);
                        ChatActivityEnterView.this.messageSendPreview = null;
                    }
                    ChatActivityEnterView.this.sendMessageInternal(z, i, true);
                }
            }, this.resourcesProvider);
        } else {
            sendMessageInternal(true, 0, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessageInternal(final boolean z, final int i, boolean z2) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate;
        TLRPC.Chat currentChat;
        EmojiView emojiView;
        if (this.slowModeTimer == Integer.MAX_VALUE && !isInScheduleMode()) {
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
            if (chatActivityEnterViewDelegate2 != null) {
                chatActivityEnterViewDelegate2.scrollToSendingMessage();
                return;
            }
            return;
        }
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            TLRPC.Chat currentChat2 = chatActivity.getCurrentChat();
            if (this.parentFragment.getCurrentUser() != null || ((ChatObject.isChannel(currentChat2) && currentChat2.megagroup) || !ChatObject.isChannel(currentChat2))) {
                MessagesController.getNotificationsSettings(this.currentAccount).edit().putBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + this.dialog_id, !z).commit();
            }
        }
        if (this.stickersExpanded) {
            setStickersExpanded(false, true, false);
            if (this.searchingType != 0 && (emojiView = this.emojiView) != null) {
                emojiView.closeSearch(false);
                this.emojiView.hideSearchKeyboard();
            }
        }
        if (z2 && showConfirmAlert(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda36
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$sendMessageInternal$43(z, i);
            }
        })) {
            return;
        }
        if (this.videoToSendMessageObject != null) {
            this.delegate.needStartRecordVideo(4, z, i, this.voiceOnce ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0, this.effectId);
            SendButton sendButton = this.sendButton;
            this.effectId = 0L;
            sendButton.setEffect(0L);
            hideRecordedAudioPanel(true);
            checkSendButton(true);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda37
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$sendMessageInternal$44();
                }
            }, 100L);
            this.millisecondsRecorded = 0L;
            return;
        }
        if (this.audioToSend != null) {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null && playingMessageObject == this.audioToSendMessageObject) {
                MediaController.getInstance().cleanupPlayer(true, true);
            }
            MediaController.getInstance().cleanRecording(false);
            MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
            long j = this.dialog_id;
            ChatActivity chatActivity2 = this.parentFragment;
            mediaDataController.pushDraftVoiceMessage(j, (chatActivity2 == null || !chatActivity2.isTopic) ? 0L : chatActivity2.getTopicId(), null);
            SendMessagesHelper.SendMessageParams of = SendMessagesHelper.SendMessageParams.of(this.audioToSend, null, this.audioToSendPath, this.dialog_id, this.replyingMessageObject, getThreadMessage(), null, null, null, null, z, i, this.voiceOnce ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0, null, null, false);
            ChatActivity chatActivity3 = this.parentFragment;
            of.quick_reply_shortcut = chatActivity3 != null ? chatActivity3.quickReplyShortcut : null;
            of.quick_reply_shortcut_id = chatActivity3 != null ? chatActivity3.getQuickReplyId() : 0;
            of.effect_id = this.effectId;
            SendButton sendButton2 = this.sendButton;
            this.effectId = 0L;
            sendButton2.setEffect(0L);
            if (!this.delegate.hasForwardingMessages()) {
                MessageObject.SendAnimationData sendAnimationData = new MessageObject.SendAnimationData();
                sendAnimationData.fromPreview = System.currentTimeMillis() - this.sentFromPreview < 200;
                of.sendAnimationData = sendAnimationData;
            }
            applyStoryToSendMessageParams(of);
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(of);
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate3 = this.delegate;
            if (chatActivityEnterViewDelegate3 != null) {
                chatActivityEnterViewDelegate3.onMessageSend(null, z, i);
            }
            hideRecordedAudioPanel(true);
            checkSendButton(true);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$sendMessageInternal$45();
                }
            }, 100L);
            this.millisecondsRecorded = 0L;
            return;
        }
        EditTextCaption editTextCaption = this.messageEditText;
        final CharSequence textToUse = editTextCaption == null ? "" : editTextCaption.getTextToUse();
        ChatActivity chatActivity4 = this.parentFragment;
        if (chatActivity4 != null && (currentChat = chatActivity4.getCurrentChat()) != null && currentChat.slowmode_enabled && !ChatObject.hasAdminRights(currentChat)) {
            if (textToUse.length() > this.accountInstance.getMessagesController().maxMessageLength) {
                AlertsCreator.showSimpleAlert(this.parentFragment, LocaleController.getString("Slowmode", R.string.Slowmode), LocaleController.getString("SlowmodeSendErrorTooLong", R.string.SlowmodeSendErrorTooLong), this.resourcesProvider);
                return;
            } else if (this.forceShowSendButton && textToUse.length() > 0) {
                AlertsCreator.showSimpleAlert(this.parentFragment, LocaleController.getString("Slowmode", R.string.Slowmode), LocaleController.getString("SlowmodeSendError", R.string.SlowmodeSendError), this.resourcesProvider);
                return;
            }
        }
        if (checkPremiumAnimatedEmoji(this.currentAccount, this.dialog_id, this.parentFragment, null, textToUse)) {
            return;
        }
        if (!processSendingText(textToUse, z, i)) {
            if (!this.forceShowSendButton || (chatActivityEnterViewDelegate = this.delegate) == null) {
                return;
            }
            chatActivityEnterViewDelegate.onMessageSend(null, z, i);
            return;
        }
        if (this.delegate.hasForwardingMessages() || (!(i == 0 || isInScheduleMode()) || isInScheduleMode())) {
            EditTextCaption editTextCaption2 = this.messageEditText;
            if (editTextCaption2 != null) {
                editTextCaption2.setText("");
            }
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate4 = this.delegate;
            if (chatActivityEnterViewDelegate4 != null) {
                chatActivityEnterViewDelegate4.onMessageSend(textToUse, z, i);
            }
        } else {
            this.messageTransitionIsRunning = false;
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda39
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$sendMessageInternal$46(textToUse, z, i);
                }
            };
            this.moveToSendStateRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 200L);
        }
        this.lastTypingTimeSend = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBirthdayHintText() {
        HintView2 hintView2 = this.birthdayHint;
        if (hintView2 == null) {
            return;
        }
        hintView2.setText(Emoji.replaceWithRestrictedEmoji(AndroidUtilities.replaceTags(LocaleController.formatString(R.string.UserBirthdayHint, UserObject.getFirstName(this.parentFragment.getCurrentUser()))), this.birthdayHint.getTextPaint().getFontMetricsInt(), new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda32
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.setBirthdayHintText();
            }
        }));
        HintView2 hintView22 = this.birthdayHint;
        hintView22.setMaxWidthPx(HintView2.cutInFancyHalf(hintView22.getText(), this.birthdayHint.getTextPaint()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEmojiButtonImage(boolean z, boolean z2) {
        ChatActivityEnterViewAnimatedIconView.State state;
        EditTextCaption editTextCaption;
        ChatActivityEnterViewAnimatedIconView.State state2;
        FrameLayout frameLayout;
        if (this.emojiButton == null) {
            return;
        }
        if (this.recordInterfaceState == 1 || ((frameLayout = this.recordedAudioPanel) != null && frameLayout.getVisibility() == 0)) {
            this.emojiButtonScale = 0.0f;
            this.emojiButtonAlpha = 0.0f;
            updateEmojiButtonParams();
            z2 = false;
        }
        if (!z || this.currentPopupContentType != 0) {
            EmojiView emojiView = this.emojiView;
            int i = emojiView == null ? MessagesController.getGlobalEmojiSettings().getInt("selected_page", 0) : emojiView.getCurrentPage();
            state = (i == 0 || !((this.allowStickers || this.allowGifs) && ((editTextCaption = this.messageEditText) == null || TextUtils.isEmpty(editTextCaption.getText())))) ? ChatActivityEnterViewAnimatedIconView.State.SMILE : i == 1 ? ChatActivityEnterViewAnimatedIconView.State.STICKER : ChatActivityEnterViewAnimatedIconView.State.GIF;
        } else if (!this.sendPlainEnabled) {
            return;
        } else {
            state = ChatActivityEnterViewAnimatedIconView.State.KEYBOARD;
        }
        if (!this.sendPlainEnabled && state == ChatActivityEnterViewAnimatedIconView.State.SMILE) {
            state = ChatActivityEnterViewAnimatedIconView.State.GIF;
        } else if (!this.stickersEnabled && state != (state2 = ChatActivityEnterViewAnimatedIconView.State.SMILE)) {
            state = state2;
        }
        this.emojiButton.setState(state, z2);
        onEmojiIconChanged(state);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRecordVideoButtonVisible(boolean z, boolean z2) {
        if (this.audioVideoSendButton == null) {
            return;
        }
        this.isInVideoMode = z;
        if (z2) {
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            boolean z3 = false;
            if (DialogObject.isChatDialog(this.dialog_id)) {
                TLRPC.Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
                if (ChatObject.isChannel(chat) && !chat.megagroup) {
                    z3 = true;
                }
            }
            globalMainSettings.edit().putBoolean(z3 ? "currentModeVideoChannel" : "currentModeVideo", z).apply();
        }
        this.audioVideoSendButton.setState(isInVideoMode() ? ChatActivityEnterViewAnimatedIconView.State.VIDEO : ChatActivityEnterViewAnimatedIconView.State.VOICE, z2);
        this.audioVideoSendButton.setContentDescription(LocaleController.getString(isInVideoMode() ? R.string.AccDescrVideoMessage : R.string.AccDescrVoiceMessage));
        this.audioVideoButtonContainer.setContentDescription(LocaleController.getString(isInVideoMode() ? R.string.AccDescrVideoMessage : R.string.AccDescrVoiceMessage));
        this.audioVideoSendButton.sendAccessibilityEvent(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSearchingTypeInternal(int i, boolean z) {
        final boolean z2 = i != 0;
        if (z2 != (this.searchingType != 0)) {
            ValueAnimator valueAnimator = this.searchAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.searchAnimator.cancel();
            }
            if (z) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.searchToOpenProgress, z2 ? 1.0f : 0.0f);
                this.searchAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda17
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        ChatActivityEnterView.this.lambda$setSearchingTypeInternal$72(valueAnimator2);
                    }
                });
                this.searchAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.77
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ChatActivityEnterView.this.searchToOpenProgress = z2 ? 1.0f : 0.0f;
                        if (ChatActivityEnterView.this.emojiView != null) {
                            ChatActivityEnterView.this.emojiView.searchProgressChanged();
                        }
                    }
                });
                this.searchAnimator.setDuration(220L);
                this.searchAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                this.searchAnimator.start();
            } else {
                this.searchToOpenProgress = z2 ? 1.0f : 0.0f;
                EmojiView emojiView = this.emojiView;
                if (emojiView != null) {
                    emojiView.searchProgressChanged();
                }
            }
        }
        this.searchingType = i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSlowModeButtonVisible(boolean z) {
        int i;
        this.slowModeButton.setVisibility(z ? 0 : 8);
        if (z) {
            i = AndroidUtilities.dp(this.slowModeButton.isPremiumMode ? 26.0f : 16.0f);
        } else {
            i = 0;
        }
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null || editTextCaption.getPaddingRight() == i) {
            return;
        }
        this.messageEditText.setPadding(0, AndroidUtilities.dp(11.0f), i, AndroidUtilities.dp(12.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showCaptionLimitBulletin() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || !ChatObject.isChannelAndNotMegaGroup(chatActivity.getCurrentChat())) {
            return;
        }
        BulletinFactory.of(this.parentFragment).createCaptionLimitBulletin(MessagesController.getInstance(this.currentAccount).captionLengthLimitPremium, new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda77
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$showCaptionLimitBulletin$48();
            }
        }).show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPopup(int i, int i2) {
        showPopup(i, i2, true);
    }

    private void showPopup(final int i, int i2, boolean z) {
        int i3;
        int i4;
        if (i == 2) {
            return;
        }
        View view = null;
        if (i == 1) {
            if (i2 == 0) {
                if (this.parentActivity == null && this.emojiView == null) {
                    return;
                } else {
                    createEmojiView();
                }
            }
            if (i2 == 0) {
                attachEmojiView();
                if (this.emojiViewVisible) {
                    this.emojiView.getVisibility();
                }
                this.emojiView.setVisibility(0);
                this.emojiViewVisible = true;
                BotKeyboardView botKeyboardView = this.botKeyboardView;
                if (botKeyboardView == null || botKeyboardView.getVisibility() == 8) {
                    i3 = 0;
                } else {
                    this.botKeyboardView.setVisibility(8);
                    this.botKeyboardViewVisible = false;
                    i3 = this.botKeyboardView.getMeasuredHeight();
                }
                this.emojiView.setShowing(true);
                view = this.emojiView;
                this.animatingContentType = 0;
            } else if (i2 == 1) {
                if (this.botKeyboardViewVisible) {
                    this.botKeyboardView.getVisibility();
                }
                this.botKeyboardViewVisible = true;
                EmojiView emojiView = this.emojiView;
                if (emojiView == null || emojiView.getVisibility() == 8) {
                    i4 = 0;
                } else {
                    this.sizeNotifierLayout.removeView(this.emojiView);
                    this.emojiView.setVisibility(8);
                    this.emojiView.setShowing(false);
                    this.emojiViewVisible = false;
                    i4 = this.emojiView.getMeasuredHeight();
                }
                this.botKeyboardView.setVisibility(0);
                View view2 = this.botKeyboardView;
                this.animatingContentType = 1;
                MessagesController.getMainSettings(this.currentAccount).edit().remove("closed_botkeyboard_" + getTopicKeyString()).apply();
                i3 = i4;
                view = view2;
            } else {
                i3 = 0;
            }
            this.currentPopupContentType = i2;
            if (this.keyboardHeight <= 0) {
                this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.dp(200.0f));
            }
            if (this.keyboardHeightLand <= 0) {
                this.keyboardHeightLand = MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.dp(200.0f));
            }
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i5 = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity != null && chatActivity.getParentLayout() != null) {
                i5 -= this.parentFragment.getParentLayout().getBottomTabsHeight(false);
            }
            if (i2 == 1) {
                i5 = Math.min(this.botKeyboardView.getKeyboardHeight(), i5);
            }
            BotKeyboardView botKeyboardView2 = this.botKeyboardView;
            if (botKeyboardView2 != null) {
                botKeyboardView2.setPanelHeight(i5);
            }
            if (view != null) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                layoutParams.height = i5;
                view.setLayoutParams(layoutParams);
            }
            if (!AndroidUtilities.isInMultiwindow) {
                AndroidUtilities.hideKeyboard(this.messageEditText);
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
            if (sizeNotifierFrameLayout != null) {
                this.emojiPadding = i5;
                sizeNotifierFrameLayout.requestLayout();
                setEmojiButtonImage(true, true);
                updateBotButton(true);
                onWindowSizeChanged();
                if (this.smoothKeyboard && !this.keyboardVisible && i5 != i3 && z) {
                    final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda20
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatActivityEnterView.this.lambda$showPopup$70();
                        }
                    };
                    if (this.overrideKeyboardAnimation) {
                        this.animationEndRunnable = runnable;
                    } else {
                        this.panelAnimation = new AnimatorSet();
                        float f = i5 - i3;
                        view.setTranslationY(f);
                        this.panelAnimation.playTogether(ObjectAnimator.ofFloat(view, (Property<View, Float>) View.TRANSLATION_Y, f, 0.0f));
                        this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                        this.panelAnimation.setDuration(250L);
                        this.panelAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.74
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                ChatActivityEnterView.this.panelAnimation = null;
                                ChatActivityEnterView.this.notificationsLocker.unlock();
                                runnable.run();
                            }
                        });
                        AndroidUtilities.runOnUIThread(this.runEmojiPanelAnimation, 50L);
                        this.notificationsLocker.lock();
                    }
                    requestLayout();
                }
            }
        } else {
            if (this.emojiButton != null) {
                setEmojiButtonImage(false, true);
            }
            this.currentPopupContentType = -1;
            EmojiView emojiView2 = this.emojiView;
            if (emojiView2 != null) {
                if (i == 2 && !AndroidUtilities.usingHardwareInput && !AndroidUtilities.isInMultiwindow) {
                    this.removeEmojiViewAfterAnimation = false;
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
                    if (chatActivityEnterViewDelegate != null) {
                        chatActivityEnterViewDelegate.bottomPanelTranslationYChanged(0.0f);
                    }
                    this.sizeNotifierLayout.removeView(this.emojiView);
                    this.emojiView = null;
                } else if (!this.smoothKeyboard || this.keyboardVisible || this.stickersExpanded) {
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
                    if (chatActivityEnterViewDelegate2 != null) {
                        chatActivityEnterViewDelegate2.bottomPanelTranslationYChanged(0.0f);
                    }
                    this.emojiPadding = 0;
                    this.sizeNotifierLayout.removeView(this.emojiView);
                    this.emojiView.setVisibility(8);
                    this.emojiView.setShowing(false);
                } else {
                    this.emojiViewVisible = true;
                    this.animatingContentType = 0;
                    emojiView2.setShowing(false);
                    final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda21
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatActivityEnterView.this.lambda$showPopup$71(i);
                        }
                    };
                    if (this.overrideKeyboardAnimation) {
                        this.animationEndRunnable = runnable2;
                    } else {
                        AnimatorSet animatorSet = new AnimatorSet();
                        this.panelAnimation = animatorSet;
                        animatorSet.playTogether(ObjectAnimator.ofFloat(this.emojiView, (Property<EmojiView, Float>) View.TRANSLATION_Y, r9.getMeasuredHeight()));
                        this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                        this.panelAnimation.setDuration(250L);
                        this.notificationsLocker.lock();
                        this.panelAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.75
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                runnable2.run();
                                ChatActivityEnterView.this.notificationsLocker.unlock();
                            }
                        });
                    }
                    AndroidUtilities.runOnUIThread(this.runEmojiPanelAnimation, 50L);
                    requestLayout();
                }
                this.emojiViewVisible = false;
            }
            BotKeyboardView botKeyboardView3 = this.botKeyboardView;
            if (botKeyboardView3 != null && botKeyboardView3.getVisibility() == 0) {
                if (i != 2 || AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
                    if (this.smoothKeyboard && !this.keyboardVisible) {
                        if (this.botKeyboardViewVisible) {
                            this.animatingContentType = 1;
                        }
                        AnimatorSet animatorSet2 = new AnimatorSet();
                        this.panelAnimation = animatorSet2;
                        animatorSet2.playTogether(ObjectAnimator.ofFloat(this.botKeyboardView, (Property<BotKeyboardView, Float>) View.TRANSLATION_Y, r7.getMeasuredHeight()));
                        this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                        this.panelAnimation.setDuration(250L);
                        this.panelAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.76
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                if (i == 0) {
                                    ChatActivityEnterView.this.emojiPadding = 0;
                                }
                                ChatActivityEnterView.this.panelAnimation = null;
                                ChatActivityEnterView.this.botKeyboardView.setTranslationY(0.0f);
                                ChatActivityEnterView.this.botKeyboardView.setVisibility(8);
                                ChatActivityEnterView.this.notificationsLocker.unlock();
                                if (ChatActivityEnterView.this.delegate != null) {
                                    ChatActivityEnterView.this.delegate.bottomPanelTranslationYChanged(0.0f);
                                }
                                ChatActivityEnterView.this.requestLayout();
                            }
                        });
                        this.notificationsLocker.lock();
                        AndroidUtilities.runOnUIThread(this.runEmojiPanelAnimation, 50L);
                        requestLayout();
                    } else if (!this.waitingForKeyboardOpen) {
                        this.botKeyboardView.setVisibility(8);
                    }
                }
                this.botKeyboardViewVisible = false;
            }
            if (i2 == 1 && this.botButtonsMessageObject != null) {
                MessagesController.getMainSettings(this.currentAccount).edit().putInt("closed_botkeyboard_" + getTopicKeyString(), this.botButtonsMessageObject.getId()).apply();
            }
            updateBotButton(true);
        }
        if (this.stickersTabOpen || this.emojiTabOpen) {
            checkSendButton(true);
        }
        if (this.stickersExpanded && i != 1) {
            setStickersExpanded(false, false, false);
        }
        updateFieldHint(false);
        checkBotMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showRestrictedHint() {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if ((chatActivityEnterViewDelegate == null || !chatActivityEnterViewDelegate.checkCanRemoveRestrictionsByBoosts()) && DialogObject.isChatDialog(this.dialog_id)) {
            BulletinFactory.of(this.parentFragment).createSimpleBulletin(R.raw.passcode_lock_close, LocaleController.formatString("SendPlainTextRestrictionHint", R.string.SendPlainTextRestrictionHint, ChatObject.getAllowedSendString(this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id)))), 3).show();
        }
    }

    private void showTopView(boolean z, boolean z2, boolean z3) {
        if (this.topView == null || this.topViewShowed || getVisibility() != 0) {
            FrameLayout frameLayout = this.recordedAudioPanel;
            if (frameLayout == null || frameLayout.getVisibility() != 0) {
                if ((!this.forceShowSendButton && this.replyingQuote == null && this.replyingMessageObject == null) || z2) {
                    openKeyboard();
                    return;
                }
                return;
            }
            return;
        }
        FrameLayout frameLayout2 = this.recordedAudioPanel;
        boolean z4 = (frameLayout2 == null || frameLayout2.getVisibility() != 0) && ((!this.forceShowSendButton && this.replyingQuote == null) || z2) && (this.botReplyMarkup == null || this.editingMessageObject != null);
        if (!z3 && z && z4 && !this.keyboardVisible && !isPopupShowing()) {
            openKeyboard();
            Runnable runnable = this.showTopViewRunnable;
            if (runnable != null) {
                AndroidUtilities.cancelRunOnUIThread(runnable);
            }
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda75
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$showTopView$41();
                }
            };
            this.showTopViewRunnable = runnable2;
            AndroidUtilities.runOnUIThread(runnable2, 200L);
            return;
        }
        this.needShowTopView = true;
        this.topViewShowed = true;
        if (this.allowShowTopView) {
            this.topView.setVisibility(0);
            this.topLineView.setVisibility(0);
            ValueAnimator valueAnimator = this.currentTopViewAnimation;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.currentTopViewAnimation = null;
            }
            resizeForTopView(true);
            if (z) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.topViewEnterProgress, 1.0f);
                this.currentTopViewAnimation = ofFloat;
                ofFloat.addUpdateListener(this.topViewUpdateListener);
                this.currentTopViewAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.46
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ValueAnimator valueAnimator2 = ChatActivityEnterView.this.currentTopViewAnimation;
                        if (valueAnimator2 != null && valueAnimator2.equals(animator)) {
                            ChatActivityEnterView.this.currentTopViewAnimation = null;
                        }
                        ChatActivityEnterView.this.notificationsLocker.unlock();
                        if (ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentFragment.mentionContainer == null) {
                            return;
                        }
                        ChatActivityEnterView.this.parentFragment.mentionContainer.setTranslationY(0.0f);
                    }
                });
                this.currentTopViewAnimation.setDuration(270L);
                this.currentTopViewAnimation.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
                this.currentTopViewAnimation.start();
                this.notificationsLocker.lock();
            } else {
                this.topViewEnterProgress = 1.0f;
                this.topView.setTranslationY(0.0f);
                this.topLineView.setAlpha(1.0f);
            }
            if (z4) {
                EditTextCaption editTextCaption = this.messageEditText;
                if (editTextCaption != null) {
                    editTextCaption.requestFocus();
                }
                openKeyboard();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLockTransition() {
        AnimatorSet animatorSet = new AnimatorSet();
        try {
            performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "lockAnimatedTranslation", this.startTranslation);
        ofFloat.setStartDelay(100L);
        ofFloat.setDuration(350L);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, "snapAnimationProgress", 1.0f);
        ofFloat2.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        ofFloat2.setDuration(250L);
        SharedConfig.removeLockRecordAudioVideoHint();
        animatorSet.playTogether(ofFloat2, ofFloat, ObjectAnimator.ofFloat(this, "slideToCancelProgress", 1.0f).setDuration(200L), ObjectAnimator.ofFloat(this.slideText, "cancelToProgress", 1.0f));
        animatorSet.start();
    }

    private boolean supportsSendingNewEntities() {
        ChatActivity chatActivity = this.parentFragment;
        TLRPC.EncryptedChat currentEncryptedChat = chatActivity != null ? chatActivity.getCurrentEncryptedChat() : null;
        return currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(currentEncryptedChat.layer) >= 101;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateAttachLayoutParams() {
        LinearLayout linearLayout = this.attachLayout;
        if (linearLayout != null) {
            linearLayout.setTranslationX(this.attachLayoutPaddingTranslationX + this.attachLayoutTranslationX);
            this.attachLayout.setAlpha(this.attachLayoutAlpha * this.attachLayoutPaddingAlpha);
            LinearLayout linearLayout2 = this.attachLayout;
            linearLayout2.setVisibility(linearLayout2.getAlpha() > 0.0f ? 0 : 8);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:108:0x00b2, code lost:
    
        if (r0 != null) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0045, code lost:
    
        if (r0 != null) goto L47;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateBotButton(boolean z) {
        ImageView imageView;
        ImageView imageView2;
        ImageView imageView3;
        if (this.isChat) {
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity != null && !chatActivity.openAnimationEnded) {
                z = false;
            }
            boolean hasBotWebView = hasBotWebView();
            boolean z2 = this.botMenuButtonType != BotMenuButtonType.NO_BUTTON && this.dialog_id > 0;
            ImageView imageView4 = this.botButton;
            boolean z3 = imageView4 != null && imageView4.getVisibility() == 0;
            if (!hasBotWebView && !this.hasBotCommands && !this.hasQuickReplies && this.botReplyMarkup == null) {
                imageView = this.botButton;
            } else if (this.botReplyMarkup != null) {
                if (isPopupShowing() && this.currentPopupContentType == 1 && this.botReplyMarkup.is_persistent) {
                    ImageView imageView5 = this.botButton;
                    if (imageView5 != null && imageView5.getVisibility() != 8) {
                        imageView = this.botButton;
                        imageView.setVisibility(8);
                    }
                } else {
                    createBotButton();
                    if (this.botButton.getVisibility() != 0) {
                        this.botButton.setVisibility(0);
                    }
                    this.botButtonDrawable.setIcon(R.drawable.input_bot2, true);
                    this.botButton.setContentDescription(LocaleController.getString("AccDescrBotKeyboard", R.string.AccDescrBotKeyboard));
                }
            } else if (z2) {
                imageView = this.botButton;
            } else {
                createBotButton();
                this.botButtonDrawable.setIcon(R.drawable.input_bot1, true);
                this.botButton.setContentDescription(LocaleController.getString("AccDescrBotCommands", R.string.AccDescrBotCommands));
                this.botButton.setVisibility(0);
            }
            if (z2) {
                createBotCommandsMenuButton();
            }
            ImageView imageView6 = this.botButton;
            boolean z4 = (imageView6 != null && imageView6.getVisibility() == 0) != z3;
            BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
            if (botCommandsMenuView != null) {
                boolean z5 = botCommandsMenuView.isWebView;
                botCommandsMenuView.setWebView(this.botMenuButtonType == BotMenuButtonType.WEB_VIEW);
                boolean menuText = this.botCommandsMenuButton.setMenuText(this.botMenuButtonType == BotMenuButtonType.COMMANDS ? LocaleController.getString(R.string.BotsMenuTitle) : this.botMenuWebViewTitle);
                AndroidUtilities.updateViewVisibilityAnimated(this.botCommandsMenuButton, z2, 0.5f, z);
                z4 = z4 || menuText || z5 != this.botCommandsMenuButton.isWebView;
            }
            if (z4 && z) {
                beginDelayedTransition();
                ImageView imageView7 = this.botButton;
                boolean z6 = imageView7 != null && imageView7.getVisibility() == 0;
                if (z6 != z3 && (imageView3 = this.botButton) != null) {
                    imageView3.setVisibility(0);
                    if (z6) {
                        this.botButton.setAlpha(0.0f);
                        this.botButton.setScaleX(0.1f);
                        this.botButton.setScaleY(0.1f);
                    } else if (!z6) {
                        this.botButton.setAlpha(1.0f);
                        this.botButton.setScaleX(1.0f);
                        this.botButton.setScaleY(1.0f);
                    }
                    AndroidUtilities.updateViewVisibilityAnimated(this.botButton, z6, 0.1f, true);
                }
            }
            updateFieldRight(2);
            LinearLayout linearLayout = this.attachLayout;
            ImageView imageView8 = this.botButton;
            linearLayout.setPivotX(AndroidUtilities.dp(((imageView8 == null || imageView8.getVisibility() == 8) && ((imageView2 = this.notifyButton) == null || imageView2.getVisibility() == 8)) ? 48.0f : 96.0f));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBotCommandsMenuContainerTopPadding() {
        int measuredHeight;
        float max;
        int max2;
        LinearLayoutManager linearLayoutManager;
        int findFirstVisibleItemPosition;
        View findViewByPosition;
        BotCommandsMenuContainer botCommandsMenuContainer = this.botCommandsMenuContainer;
        if (botCommandsMenuContainer == null) {
            return;
        }
        int childCount = botCommandsMenuContainer.listView.getChildCount();
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = this.botCommandsMenuContainer.listView.getChildAt(i2);
            if (i2 < 4) {
                i += childAt.getMeasuredHeight();
            }
        }
        if (i > 0) {
            max2 = Math.max(0, ((this.sizeNotifierLayout.getMeasuredHeight() - i) - AndroidUtilities.dp(8.0f)) - AndroidUtilities.dp(childCount > 4 ? 12.0f : 0.0f));
        } else {
            if (this.botCommandsAdapter.getItemCount() > 4) {
                measuredHeight = this.sizeNotifierLayout.getMeasuredHeight();
                max = 162.8f;
            } else {
                measuredHeight = this.sizeNotifierLayout.getMeasuredHeight();
                max = (Math.max(1, Math.min(4, this.botCommandsAdapter.getItemCount())) * 36) + 8;
            }
            max2 = Math.max(0, measuredHeight - AndroidUtilities.dp(max));
        }
        if (this.botCommandsMenuContainer.listView.getPaddingTop() != max2) {
            this.botCommandsMenuContainer.listView.setTopGlowOffset(max2);
            if (this.botCommandLastPosition == -1 && this.botCommandsMenuContainer.getVisibility() == 0 && this.botCommandsMenuContainer.listView.getLayoutManager() != null && (findFirstVisibleItemPosition = (linearLayoutManager = (LinearLayoutManager) this.botCommandsMenuContainer.listView.getLayoutManager()).findFirstVisibleItemPosition()) >= 0 && (findViewByPosition = linearLayoutManager.findViewByPosition(findFirstVisibleItemPosition)) != null) {
                this.botCommandLastPosition = findFirstVisibleItemPosition;
                this.botCommandLastTop = findViewByPosition.getTop() - this.botCommandsMenuContainer.listView.getPaddingTop();
            }
            this.botCommandsMenuContainer.listView.setPadding(0, max2, 0, AndroidUtilities.dp(8.0f));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEmojiButtonParams() {
        this.emojiButton.setScaleX(this.emojiButtonPaddingScale * this.emojiButtonScale);
        this.emojiButton.setScaleY(this.emojiButtonPaddingScale * this.emojiButtonScale);
        this.emojiButton.setAlpha(this.emojiButtonPaddingAlpha * this.emojiButtonAlpha);
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x003c, code lost:
    
        if (r5.getVisibility() == 0) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00a3, code lost:
    
        r5 = 98.0f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0046, code lost:
    
        if (r5.getVisibility() == 0) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0050, code lost:
    
        if (r5.getTag() != null) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x008d, code lost:
    
        if (r5.getVisibility() == 0) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0097, code lost:
    
        if (r5.getVisibility() == 0) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00a1, code lost:
    
        if (r5.getTag() != null) goto L66;
     */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateFieldRight(int i) {
        int dp;
        ImageView imageView;
        LinearLayout linearLayout;
        float f;
        ImageView imageView2;
        LinearLayout linearLayout2;
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null || this.editingMessageObject != null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) editTextCaption.getLayoutParams();
        int i2 = layoutParams.rightMargin;
        if (i == 1) {
            ImageView imageView3 = this.botButton;
            if (imageView3 == null || imageView3.getVisibility() != 0 || (imageView2 = this.scheduledButton) == null || imageView2.getVisibility() != 0 || (linearLayout2 = this.attachLayout) == null || linearLayout2.getVisibility() != 0) {
                ImageView imageView4 = this.botButton;
                if (imageView4 != null) {
                }
                ImageView imageView5 = this.notifyButton;
                if (imageView5 != null) {
                }
                ImageView imageView6 = this.scheduledButton;
                if (imageView6 != null) {
                }
                f = 50.0f;
            }
            f = 146.0f;
        } else {
            if (i == 2) {
                if (i2 != AndroidUtilities.dp(2.0f)) {
                    ImageView imageView7 = this.botButton;
                    if (imageView7 == null || imageView7.getVisibility() != 0 || (imageView = this.scheduledButton) == null || imageView.getVisibility() != 0 || (linearLayout = this.attachLayout) == null || linearLayout.getVisibility() != 0) {
                        ImageView imageView8 = this.botButton;
                        if (imageView8 != null) {
                        }
                        ImageView imageView9 = this.notifyButton;
                        if (imageView9 != null) {
                        }
                        ImageView imageView10 = this.scheduledButton;
                        if (imageView10 != null) {
                        }
                    }
                    f = 146.0f;
                }
                if (i2 != layoutParams.rightMargin) {
                    this.messageEditText.setLayoutParams(layoutParams);
                    return;
                }
                return;
            }
            ImageView imageView11 = this.scheduledButton;
            if (imageView11 == null || imageView11.getTag() == null) {
                dp = AndroidUtilities.dp(2.0f);
                layoutParams.rightMargin = dp;
                if (i2 != layoutParams.rightMargin) {
                }
            }
            f = 50.0f;
        }
        dp = AndroidUtilities.dp(f);
        layoutParams.rightMargin = dp;
        if (i2 != layoutParams.rightMargin) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMessageTextParams() {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.setTranslationX(this.messageTextPaddingTranslationX + this.messageTextTranslationX);
        }
    }

    private void updateRecordedDeleteIconColors() {
        int themedColor = getThemedColor(Theme.key_chat_recordedVoiceDot);
        int themedColor2 = getThemedColor(Theme.key_chat_messagePanelBackground);
        int themedColor3 = getThemedColor(Theme.key_chat_messagePanelVoiceDelete);
        RLottieImageView rLottieImageView = this.recordDeleteImageView;
        if (rLottieImageView != null) {
            rLottieImageView.setLayerColor("Cup Red.**", themedColor);
            this.recordDeleteImageView.setLayerColor("Box Red.**", themedColor);
            this.recordDeleteImageView.setLayerColor("Cup Grey.**", themedColor3);
            this.recordDeleteImageView.setLayerColor("Box Grey.**", themedColor3);
            this.recordDeleteImageView.setLayerColor("Line 1.**", themedColor2);
            this.recordDeleteImageView.setLayerColor("Line 2.**", themedColor2);
            this.recordDeleteImageView.setLayerColor("Line 3.**", themedColor2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSlowModeText() {
        int i;
        boolean isUploadingMessageIdDialog;
        int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        AndroidUtilities.cancelRunOnUIThread(this.updateSlowModeRunnable);
        this.updateSlowModeRunnable = null;
        TLRPC.ChatFull chatFull = this.info;
        if (chatFull == null || chatFull.slowmode_seconds == 0 || chatFull.slowmode_next_send_date > currentTime || !((isUploadingMessageIdDialog = SendMessagesHelper.getInstance(this.currentAccount).isUploadingMessageIdDialog(this.dialog_id)) || SendMessagesHelper.getInstance(this.currentAccount).isSendingMessageIdDialog(this.dialog_id))) {
            int i2 = this.slowModeTimer;
            if (i2 >= 2147483646) {
                if (this.info != null) {
                    this.accountInstance.getMessagesController().loadFullChat(this.info.id, 0, true);
                }
                i = 0;
            } else {
                i = i2 - currentTime;
            }
        } else {
            if (!ChatObject.hasAdminRights(this.accountInstance.getMessagesController().getChat(Long.valueOf(this.info.id))) && !ChatObject.isIgnoredChatRestrictionsForBoosters(this.info)) {
                i = this.info.slowmode_seconds;
                this.slowModeTimer = isUploadingMessageIdDialog ? ConnectionsManager.DEFAULT_DATACENTER_ID : 2147483646;
            }
            i = 0;
        }
        if (this.slowModeTimer == 0 || i <= 0) {
            this.slowModeTimer = 0;
        } else {
            this.slowModeButton.setText(AndroidUtilities.formatDurationNoHours(Math.max(1, i), false));
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                SlowModeBtn slowModeBtn = this.slowModeButton;
                chatActivityEnterViewDelegate.onUpdateSlowModeButton(slowModeBtn, false, slowModeBtn.getText());
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda23
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.updateSlowModeText();
                }
            };
            this.updateSlowModeRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 100L);
        }
        if (isInScheduleMode()) {
            return;
        }
        checkSendButton(true);
    }

    public void addEmojiToRecent(String str) {
        createEmojiView();
        this.emojiView.addEmojiToRecent(str);
    }

    public void addRecentGif(TLRPC.Document document) {
        MediaDataController.getInstance(this.currentAccount).addRecentGif(document, (int) (System.currentTimeMillis() / 1000), true);
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.addRecentGif(document);
        }
    }

    public void addStickerToRecent(TLRPC.Document document) {
        createEmojiView();
        this.emojiView.addRecentSticker(document);
    }

    @Override // org.telegram.ui.Components.SuggestEmojiView.AnchorViewDelegate
    public void addTextChangedListener(TextWatcher textWatcher) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.addTextChangedListener(textWatcher);
            return;
        }
        if (this.messageEditTextWatchers == null) {
            this.messageEditTextWatchers = new ArrayList();
        }
        this.messageEditTextWatchers.add(textWatcher);
    }

    public void addTopView(View view, View view2, int i) {
        if (view == null) {
            return;
        }
        this.topLineView = view2;
        view2.setVisibility(8);
        this.topLineView.setAlpha(0.0f);
        addView(this.topLineView, LayoutHelper.createFrame(-1, 1.0f, 51, 0.0f, i + 1, 0.0f, 0.0f));
        this.topView = view;
        view.setVisibility(8);
        this.topViewEnterProgress = 0.0f;
        float f = i;
        this.topView.setTranslationY(f);
        addView(this.topView, 0, LayoutHelper.createFrame(-1, f, 51, 0.0f, 2.0f, 0.0f, 0.0f));
        this.needShowTopView = false;
    }

    public boolean botCommandsMenuIsShowing() {
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        return botCommandsMenuView != null && botCommandsMenuView.isOpened();
    }

    public boolean businessLinkHasChanges() {
        BusinessLinkPresetMessage calculateBusinessLinkPresetMessage = calculateBusinessLinkPresetMessage();
        return (TextUtils.equals(calculateBusinessLinkPresetMessage.text, this.lastSavedBusinessLinkMessage.text) && MediaDataController.entitiesEqual((ArrayList<TLRPC.MessageEntity>) this.lastSavedBusinessLinkMessage.entities, (ArrayList<TLRPC.MessageEntity>) calculateBusinessLinkPresetMessage.entities)) ? false : true;
    }

    @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
    public boolean canSchedule() {
        ChatActivity chatActivity = this.parentFragment;
        return chatActivity != null && chatActivity.canScheduleMessage();
    }

    public boolean canShowMessageTransition() {
        MessageSendPreview messageSendPreview;
        boolean z = this.moveToSendStateRunnable != null && ((messageSendPreview = this.messageSendPreview) == null || !messageSendPreview.isShowing()) && System.currentTimeMillis() - this.sentFromPreview > 300;
        this.sentFromPreview = -1L;
        return z;
    }

    public void cancelRecordingAudioVideo() {
        if (this.hasRecordVideo && isInVideoMode()) {
            CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
            this.delegate.needStartRecordVideo(5, true, 0, this.voiceOnce ? ConnectionsManager.DEFAULT_DATACENTER_ID : 0, this.effectId);
            SendButton sendButton = this.sendButton;
            this.effectId = 0L;
            sendButton.setEffect(0L);
        } else {
            this.delegate.needStartRecordAudio(0);
            MediaController.getInstance().stopRecording(0, false, 0, false);
        }
        this.recordingAudioVideo = false;
        updateRecordInterface(2, true);
    }

    public void checkAnimation() {
    }

    public void checkChannelRights() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null) {
            return;
        }
        updateRecordButton(chatActivity.getCurrentChat(), this.parentFragment.getCurrentUserInfo());
    }

    public void checkReactionsButton(boolean z) {
        AndroidUtilities.updateViewVisibilityAnimated(this.reactionsButton, z, 0.1f, true);
    }

    public void checkRoundVideo() {
        boolean z;
        boolean z2;
        TLRPC.TL_chatAdminRights tL_chatAdminRights;
        if (this.hasRecordVideo) {
            return;
        }
        if (this.attachLayout == null) {
            this.hasRecordVideo = false;
            setRecordVideoButtonVisible(false, false);
            return;
        }
        boolean z3 = true;
        this.hasRecordVideo = true;
        this.sendRoundEnabled = true;
        this.sendVoiceEnabled = true;
        if (DialogObject.isChatDialog(this.dialog_id)) {
            TLRPC.Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
            z = ChatObject.isChannel(chat) && !chat.megagroup;
            if (z && !chat.creator && ((tL_chatAdminRights = chat.admin_rights) == null || !tL_chatAdminRights.post_messages)) {
                this.hasRecordVideo = false;
            }
            this.sendRoundEnabled = ChatObject.canSendRoundVideo(chat);
            this.sendVoiceEnabled = ChatObject.canSendVoice(chat);
        } else {
            z = false;
        }
        if (!SharedConfig.inappCamera) {
            this.hasRecordVideo = false;
        }
        if (this.hasRecordVideo) {
            if (SharedConfig.hasCameraCache) {
                CameraController.getInstance().initCamera(null);
            }
            z2 = MessagesController.getGlobalMainSettings().getBoolean(z ? "currentModeVideoChannel" : "currentModeVideo", z);
        } else {
            z2 = false;
        }
        if (!this.sendRoundEnabled && z2) {
            z2 = false;
        }
        if (this.sendVoiceEnabled || z2) {
            z3 = z2;
        } else if (!this.hasRecordVideo) {
            z3 = false;
        }
        setRecordVideoButtonVisible(z3, false);
    }

    public void checkStickresExpandHeight() {
        if (this.emojiView == null) {
            return;
        }
        android.graphics.Point point = AndroidUtilities.displaySize;
        int i = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
        int currentActionBarHeight = (((this.originalViewHeight - (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)) - ActionBar.getCurrentActionBarHeight()) - getHeight()) + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
        if (this.searchingType == 2) {
            currentActionBarHeight = Math.min(currentActionBarHeight, AndroidUtilities.dp(120.0f) + i);
        }
        int i2 = this.emojiView.getLayoutParams().height;
        if (i2 == currentActionBarHeight) {
            return;
        }
        Animator animator = this.stickersExpansionAnim;
        if (animator != null) {
            animator.cancel();
            this.stickersExpansionAnim = null;
        }
        this.stickersExpandedHeight = currentActionBarHeight;
        if (i2 > currentActionBarHeight) {
            final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda33
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$checkStickresExpandHeight$73();
                }
            };
            this.emojiView.setLayerType(2, null);
            if (this.overrideKeyboardAnimation) {
                this.animationEndRunnable = runnable;
                return;
            }
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofInt(this, (Property<ChatActivityEnterView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)), ObjectAnimator.ofInt(this.emojiView, (Property<EmojiView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)));
            ((ObjectAnimator) animatorSet.getChildAnimations().get(0)).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda34
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ChatActivityEnterView.this.lambda$checkStickresExpandHeight$74(valueAnimator);
                }
            });
            animatorSet.setDuration(300L);
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.79
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator2) {
                    ChatActivityEnterView.this.stickersExpansionAnim = null;
                    runnable.run();
                }
            });
            this.stickersExpansionAnim = animatorSet;
            animatorSet.start();
            return;
        }
        this.emojiView.getLayoutParams().height = this.stickersExpandedHeight;
        this.sizeNotifierLayout.requestLayout();
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            int selectionStart = editTextCaption.getSelectionStart();
            int selectionEnd = this.messageEditText.getSelectionEnd();
            EditTextCaption editTextCaption2 = this.messageEditText;
            editTextCaption2.setText(editTextCaption2.getText());
            this.messageEditText.setSelection(selectionStart, selectionEnd);
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(ObjectAnimator.ofInt(this, (Property<ChatActivityEnterView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)), ObjectAnimator.ofInt(this.emojiView, (Property<EmojiView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)));
        ((ObjectAnimator) animatorSet2.getChildAnimations().get(0)).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda35
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ChatActivityEnterView.this.lambda$checkStickresExpandHeight$75(valueAnimator);
            }
        });
        animatorSet2.setDuration(300L);
        animatorSet2.setInterpolator(CubicBezierInterpolator.DEFAULT);
        animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.80
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator2) {
                ChatActivityEnterView.this.stickersExpansionAnim = null;
                ChatActivityEnterView.this.emojiView.setLayerType(0, null);
            }
        });
        this.stickersExpansionAnim = animatorSet2;
        this.emojiView.setLayerType(2, null);
        animatorSet2.start();
    }

    public boolean closeCreationLinkDialog() {
        EditTextCaption editTextCaption = this.messageEditText;
        return editTextCaption != null && editTextCaption.closeCreationLinkDialog();
    }

    public void closeKeyboard() {
        AndroidUtilities.hideKeyboard(this.messageEditText);
    }

    public boolean didPressedBotButton(TLRPC.KeyboardButton keyboardButton, MessageObject messageObject, MessageObject messageObject2) {
        return didPressedBotButton(keyboardButton, messageObject, messageObject2, null);
    }

    public boolean didPressedBotButton(final TLRPC.KeyboardButton keyboardButton, final MessageObject messageObject, final MessageObject messageObject2, Browser.Progress progress) {
        int i;
        if (keyboardButton == null || messageObject2 == null) {
            return false;
        }
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null && chatActivity.getChatMode() == 5) {
            return false;
        }
        if (keyboardButton instanceof TLRPC.TL_keyboardButtonCopy) {
            TLRPC.TL_keyboardButtonCopy tL_keyboardButtonCopy = (TLRPC.TL_keyboardButtonCopy) keyboardButton;
            AndroidUtilities.addToClipboard(tL_keyboardButtonCopy.copy_text);
            BulletinFactory.of(this.parentFragment).createCopyBulletin(LocaleController.formatString(R.string.ExactTextCopied, tL_keyboardButtonCopy.copy_text)).show(true);
        } else {
            if (keyboardButton instanceof TLRPC.TL_keyboardButton) {
                SendMessagesHelper.SendMessageParams of = SendMessagesHelper.SendMessageParams.of(keyboardButton.text, this.dialog_id, messageObject, getThreadMessage(), null, false, null, null, null, true, 0, null, false);
                ChatActivity chatActivity2 = this.parentFragment;
                of.quick_reply_shortcut = chatActivity2 != null ? chatActivity2.quickReplyShortcut : null;
                of.quick_reply_shortcut_id = chatActivity2 != null ? chatActivity2.getQuickReplyId() : 0;
                of.effect_id = this.effectId;
                SendButton sendButton = this.sendButton;
                this.effectId = 0L;
                sendButton.setEffect(0L);
                SendMessagesHelper.getInstance(this.currentAccount).sendMessage(of);
            } else if (keyboardButton instanceof TLRPC.TL_keyboardButtonUrl) {
                if (Browser.urlMustNotHaveConfirmation(keyboardButton.url)) {
                    Browser.openUrl(this.parentActivity, Uri.parse(keyboardButton.url), true, true, progress);
                } else {
                    AlertsCreator.showOpenUrlAlert(this.parentFragment, keyboardButton.url, false, true, true, progress, this.resourcesProvider);
                }
            } else if (keyboardButton instanceof TLRPC.TL_keyboardButtonRequestPhone) {
                this.parentFragment.shareMyContact(2, messageObject2);
            } else {
                if (keyboardButton instanceof TLRPC.TL_keyboardButtonRequestPoll) {
                    this.parentFragment.openPollCreate((keyboardButton.flags & 1) != 0 ? Boolean.valueOf(keyboardButton.quiz) : null);
                    return false;
                }
                if ((keyboardButton instanceof TLRPC.TL_keyboardButtonWebView) || (keyboardButton instanceof TLRPC.TL_keyboardButtonSimpleWebView)) {
                    TLRPC.Message message = messageObject2.messageOwner;
                    long j = message.via_bot_id;
                    if (j == 0) {
                        j = message.from_id.user_id;
                    }
                    final long j2 = j;
                    MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j2));
                    final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.70
                        @Override // java.lang.Runnable
                        public void run() {
                            if (ChatActivityEnterView.this.sizeNotifierLayout.measureKeyboardHeight() > AndroidUtilities.dp(20.0f) || ChatActivityEnterView.this.isPopupShowing()) {
                                ChatActivityEnterView.this.hidePopup(false);
                                AndroidUtilities.hideKeyboard(ChatActivityEnterView.this);
                                AndroidUtilities.runOnUIThread(this, 150L);
                                return;
                            }
                            if (ChatActivityEnterView.this.parentFragment == null) {
                                return;
                            }
                            int i2 = ChatActivityEnterView.this.currentAccount;
                            long j3 = messageObject2.messageOwner.dialog_id;
                            long j4 = j2;
                            TLRPC.KeyboardButton keyboardButton2 = keyboardButton;
                            String str = keyboardButton2.text;
                            String str2 = keyboardButton2.url;
                            boolean z = keyboardButton2 instanceof TLRPC.TL_keyboardButtonSimpleWebView;
                            MessageObject messageObject3 = messageObject;
                            WebViewRequestProps of2 = WebViewRequestProps.of(i2, j3, j4, str, str2, z ? 1 : 0, messageObject3 != null ? messageObject3.messageOwner.id : 0, false, null, false, null, null, 0, false, false);
                            LaunchActivity launchActivity = LaunchActivity.instance;
                            if (launchActivity != null && launchActivity.getBottomSheetTabs() != null && LaunchActivity.instance.getBottomSheetTabs().tryReopenTab(of2) != null) {
                                if (ChatActivityEnterView.this.botCommandsMenuButton != null) {
                                    ChatActivityEnterView.this.botCommandsMenuButton.setOpened(false);
                                }
                            } else {
                                BotWebViewSheet botWebViewSheet = new BotWebViewSheet(ChatActivityEnterView.this.getContext(), ChatActivityEnterView.this.resourcesProvider);
                                botWebViewSheet.setParentActivity(ChatActivityEnterView.this.parentActivity);
                                botWebViewSheet.requestWebView(ChatActivityEnterView.this.parentFragment, of2);
                                botWebViewSheet.show();
                            }
                        }
                    };
                    if (SharedPrefsHelper.isWebViewConfirmShown(this.currentAccount, j2)) {
                        runnable.run();
                    } else {
                        AlertsCreator.createBotLaunchAlert(this.parentFragment, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialog_id)), new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda62
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.this.lambda$didPressedBotButton$63(runnable, j2);
                            }
                        }, (Runnable) null);
                    }
                } else if (keyboardButton instanceof TLRPC.TL_keyboardButtonRequestGeoLocation) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.parentActivity);
                    builder.setTitle(LocaleController.getString("ShareYouLocationTitle", R.string.ShareYouLocationTitle));
                    builder.setMessage(LocaleController.getString("ShareYouLocationInfo", R.string.ShareYouLocationInfo));
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new AlertDialog.OnButtonClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda58
                        @Override // org.telegram.ui.ActionBar.AlertDialog.OnButtonClickListener
                        public final void onClick(AlertDialog alertDialog, int i2) {
                            ChatActivityEnterView.this.lambda$didPressedBotButton$64(messageObject2, keyboardButton, alertDialog, i2);
                        }
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    this.parentFragment.showDialog(builder.create());
                } else if ((keyboardButton instanceof TLRPC.TL_keyboardButtonCallback) || (keyboardButton instanceof TLRPC.TL_keyboardButtonGame) || (keyboardButton instanceof TLRPC.TL_keyboardButtonBuy) || (keyboardButton instanceof TLRPC.TL_keyboardButtonUrlAuth)) {
                    SendMessagesHelper.getInstance(this.currentAccount).sendCallback(true, messageObject2, keyboardButton, this.parentFragment);
                } else if (keyboardButton instanceof TLRPC.TL_keyboardButtonSwitchInline) {
                    if (this.parentFragment.processSwitchButton((TLRPC.TL_keyboardButtonSwitchInline) keyboardButton)) {
                        return true;
                    }
                    if (keyboardButton.same_peer) {
                        TLRPC.Message message2 = messageObject2.messageOwner;
                        long j3 = message2.from_id.user_id;
                        long j4 = message2.via_bot_id;
                        if (j4 != 0) {
                            j3 = j4;
                        }
                        TLRPC.User user = this.accountInstance.getMessagesController().getUser(Long.valueOf(j3));
                        if (user == null) {
                            return true;
                        }
                        setFieldText("@" + UserObject.getPublicUsername(user) + " " + keyboardButton.query);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("onlySelect", true);
                        bundle.putInt("dialogsType", 1);
                        if ((keyboardButton.flags & 2) != 0) {
                            bundle.putBoolean("allowGroups", false);
                            bundle.putBoolean("allowMegagroups", false);
                            bundle.putBoolean("allowLegacyGroups", false);
                            bundle.putBoolean("allowUsers", false);
                            bundle.putBoolean("allowChannels", false);
                            bundle.putBoolean("allowBots", false);
                            Iterator<TLRPC.InlineQueryPeerType> it = keyboardButton.peer_types.iterator();
                            while (it.hasNext()) {
                                TLRPC.InlineQueryPeerType next = it.next();
                                if (next instanceof TLRPC.TL_inlineQueryPeerTypePM) {
                                    bundle.putBoolean("allowUsers", true);
                                } else if (next instanceof TLRPC.TL_inlineQueryPeerTypeBotPM) {
                                    bundle.putBoolean("allowBots", true);
                                } else if (next instanceof TLRPC.TL_inlineQueryPeerTypeBroadcast) {
                                    bundle.putBoolean("allowChannels", true);
                                } else if (next instanceof TLRPC.TL_inlineQueryPeerTypeChat) {
                                    bundle.putBoolean("allowLegacyGroups", true);
                                } else if (next instanceof TLRPC.TL_inlineQueryPeerTypeMegagroup) {
                                    bundle.putBoolean("allowMegagroups", true);
                                }
                            }
                        }
                        DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                        dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda59
                            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                            public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i2, TopicsFragment topicsFragment) {
                                boolean lambda$didPressedBotButton$65;
                                lambda$didPressedBotButton$65 = ChatActivityEnterView.this.lambda$didPressedBotButton$65(messageObject2, keyboardButton, dialogsActivity2, arrayList, charSequence, z, z2, i2, topicsFragment);
                                return lambda$didPressedBotButton$65;
                            }
                        });
                        this.parentFragment.presentFragment(dialogsActivity);
                    }
                } else if (keyboardButton instanceof TLRPC.TL_keyboardButtonUserProfile) {
                    if (MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(keyboardButton.user_id)) != null) {
                        Bundle bundle2 = new Bundle();
                        bundle2.putLong("user_id", keyboardButton.user_id);
                        this.parentFragment.presentFragment(new ProfileActivity(bundle2));
                    }
                } else if (keyboardButton instanceof TLRPC.TL_keyboardButtonRequestPeer) {
                    final TLRPC.TL_keyboardButtonRequestPeer tL_keyboardButtonRequestPeer = (TLRPC.TL_keyboardButtonRequestPeer) keyboardButton;
                    TLRPC.RequestPeerType requestPeerType = tL_keyboardButtonRequestPeer.peer_type;
                    if (requestPeerType != null && messageObject2.messageOwner != null) {
                        if ((requestPeerType instanceof TLRPC.TL_requestPeerTypeUser) && (i = tL_keyboardButtonRequestPeer.max_quantity) > 1) {
                            TLRPC.TL_requestPeerTypeUser tL_requestPeerTypeUser = (TLRPC.TL_requestPeerTypeUser) requestPeerType;
                            MultiContactsSelectorBottomSheet.open(tL_requestPeerTypeUser.bot, tL_requestPeerTypeUser.premium, i, new MultiContactsSelectorBottomSheet.SelectorListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda60
                                @Override // org.telegram.ui.MultiContactsSelectorBottomSheet.SelectorListener
                                public final void onUserSelected(List list) {
                                    ChatActivityEnterView.this.lambda$didPressedBotButton$66(messageObject2, tL_keyboardButtonRequestPeer, list);
                                }
                            });
                            return false;
                        }
                        Bundle bundle3 = new Bundle();
                        bundle3.putBoolean("onlySelect", true);
                        bundle3.putInt("dialogsType", 15);
                        TLRPC.Message message3 = messageObject2.messageOwner;
                        if (message3 != null) {
                            TLRPC.Peer peer = message3.from_id;
                            if (peer instanceof TLRPC.TL_peerUser) {
                                bundle3.putLong("requestPeerBotId", peer.user_id);
                            }
                        }
                        try {
                            SerializedData serializedData = new SerializedData(tL_keyboardButtonRequestPeer.peer_type.getObjectSize());
                            tL_keyboardButtonRequestPeer.peer_type.serializeToStream(serializedData);
                            bundle3.putByteArray("requestPeerType", serializedData.toByteArray());
                            serializedData.cleanup();
                        } catch (Exception e) {
                            FileLog.e(e);
                        }
                        DialogsActivity dialogsActivity2 = new DialogsActivity(bundle3);
                        dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda61
                            @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                            public final boolean didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList arrayList, CharSequence charSequence, boolean z, boolean z2, int i2, TopicsFragment topicsFragment) {
                                boolean lambda$didPressedBotButton$67;
                                lambda$didPressedBotButton$67 = ChatActivityEnterView.this.lambda$didPressedBotButton$67(messageObject2, tL_keyboardButtonRequestPeer, dialogsActivity3, arrayList, charSequence, z, z2, i2, topicsFragment);
                                return lambda$didPressedBotButton$67;
                            }
                        });
                        this.parentFragment.presentFragment(dialogsActivity2);
                        return false;
                    }
                    FileLog.e("button.peer_type is null");
                }
            }
        }
        return true;
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        BotMenuButtonType botMenuButtonType;
        TLRPC.ChatFull chatFull;
        TLRPC.Chat chat;
        double d;
        if (i == NotificationCenter.emojiLoaded) {
            EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.invalidateViews();
            }
            BotKeyboardView botKeyboardView = this.botKeyboardView;
            if (botKeyboardView != null) {
                botKeyboardView.invalidateViews();
            }
            EditTextCaption editTextCaption = this.messageEditText;
            if (editTextCaption != null) {
                editTextCaption.postInvalidate();
                this.messageEditText.invalidateForce();
                return;
            }
            return;
        }
        if (i == NotificationCenter.recordProgressChanged) {
            if (((Integer) objArr[0]).intValue() != this.recordingGuid) {
                return;
            }
            if (this.recordInterfaceState != 0 && !this.wasSendTyping && !isInScheduleMode()) {
                this.wasSendTyping = true;
                this.accountInstance.getMessagesController().sendTyping(this.dialog_id, getThreadMessageId(), isInVideoMode() ? 7 : 1, 0);
            }
            RecordCircle recordCircle = this.recordCircle;
            if (recordCircle != null) {
                recordCircle.setAmplitude(((Double) objArr[1]).doubleValue());
                return;
            }
            return;
        }
        if (i == NotificationCenter.closeChats) {
            EditTextCaption editTextCaption2 = this.messageEditText;
            if (editTextCaption2 == null || !editTextCaption2.isFocused()) {
                return;
            }
            AndroidUtilities.hideKeyboard(this.messageEditText);
            return;
        }
        int i3 = 4;
        if (i == NotificationCenter.recordStartError || i == NotificationCenter.recordStopped) {
            if (((Integer) objArr[0]).intValue() != this.recordingGuid || !this.recordingAudioVideo) {
                return;
            }
            this.recordingAudioVideo = false;
            if (i != NotificationCenter.recordStopped) {
                updateRecordInterface(2, true);
                return;
            }
            Integer num = (Integer) objArr[1];
            if (num.intValue() != 4) {
                i3 = 5;
                if (isInVideoMode() && num.intValue() == 5) {
                    i3 = 1;
                } else if (num.intValue() != 0) {
                    i3 = num.intValue() == 6 ? 2 : 3;
                }
            }
            if (i3 == 3) {
                return;
            }
        } else {
            if (i == NotificationCenter.recordStarted) {
                if (((Integer) objArr[0]).intValue() != this.recordingGuid) {
                    return;
                }
                boolean booleanValue = ((Boolean) objArr[1]).booleanValue();
                this.isInVideoMode = !booleanValue;
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView != null) {
                    chatActivityEnterViewAnimatedIconView.setState(booleanValue ? ChatActivityEnterViewAnimatedIconView.State.VOICE : ChatActivityEnterViewAnimatedIconView.State.VIDEO, true);
                }
                if (this.recordingAudioVideo) {
                    RecordCircle recordCircle2 = this.recordCircle;
                    if (recordCircle2 != null) {
                        recordCircle2.showWaves(true, true);
                    }
                } else {
                    this.recordingAudioVideo = true;
                    updateRecordInterface(0, true);
                }
                TimerView timerView = this.recordTimerView;
                if (timerView != null) {
                    timerView.start(this.millisecondsRecorded);
                }
                RecordDot recordDot = this.recordDot;
                if (recordDot != null) {
                    recordDot.enterAnimation = false;
                    return;
                }
                return;
            }
            if (i == NotificationCenter.recordPaused) {
                this.recordingAudioVideo = false;
                this.audioToSend = null;
                this.videoToSendMessageObject = null;
                return;
            }
            if (i == NotificationCenter.recordResumed) {
                this.audioToSend = null;
                this.videoToSendMessageObject = null;
                checkSendButton(true);
                this.recordingAudioVideo = true;
                updateRecordInterface(0, true);
                return;
            }
            if (i == NotificationCenter.audioDidSent) {
                if (((Integer) objArr[0]).intValue() != this.recordingGuid) {
                    return;
                }
                this.millisecondsRecorded = 0L;
                Object obj = objArr[1];
                if (obj instanceof VideoEditedInfo) {
                    VideoEditedInfo videoEditedInfo = (VideoEditedInfo) obj;
                    this.videoToSendMessageObject = videoEditedInfo;
                    String str = (String) objArr[2];
                    this.audioToSendPath = str;
                    ArrayList<Bitmap> arrayList = (ArrayList) objArr[3];
                    this.millisecondsRecorded = videoEditedInfo.estimatedDuration;
                    VideoTimelineView videoTimelineView = this.videoTimelineView;
                    if (videoTimelineView != null) {
                        videoTimelineView.setVideoPath(str);
                        this.videoTimelineView.setKeyframes(arrayList);
                        this.videoTimelineView.setVisibility(0);
                        this.videoTimelineView.setMinProgressDiff(1000.0f / this.videoToSendMessageObject.estimatedDuration);
                        isRecordingStateChanged();
                    }
                    updateRecordInterface(3, true);
                    checkSendButton(false);
                    return;
                }
                this.audioToSend = (TLRPC.TL_document) obj;
                this.audioToSendPath = (String) objArr[2];
                boolean z = objArr.length >= 4 && ((Boolean) objArr[3]).booleanValue();
                if (this.audioToSend == null) {
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
                    if (chatActivityEnterViewDelegate != null) {
                        chatActivityEnterViewDelegate.onMessageSend(null, true, 0);
                        return;
                    }
                    return;
                }
                createRecordAudioPanel();
                if (this.recordedAudioPanel == null) {
                    return;
                }
                TLRPC.TL_message tL_message = new TLRPC.TL_message();
                tL_message.out = true;
                tL_message.id = 0;
                tL_message.peer_id = new TLRPC.TL_peerUser();
                TLRPC.TL_peerUser tL_peerUser = new TLRPC.TL_peerUser();
                tL_message.from_id = tL_peerUser;
                TLRPC.Peer peer = tL_message.peer_id;
                long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                tL_peerUser.user_id = clientUserId;
                peer.user_id = clientUserId;
                tL_message.date = (int) (System.currentTimeMillis() / 1000);
                tL_message.message = "";
                tL_message.attachPath = this.audioToSendPath;
                TLRPC.TL_messageMediaDocument tL_messageMediaDocument = new TLRPC.TL_messageMediaDocument();
                tL_message.media = tL_messageMediaDocument;
                tL_messageMediaDocument.flags |= 3;
                tL_messageMediaDocument.document = this.audioToSend;
                tL_message.flags |= 768;
                this.audioToSendMessageObject = new MessageObject(UserConfig.selectedAccount, tL_message, false, true);
                this.recordedAudioPanel.setAlpha(1.0f);
                this.recordedAudioPanel.setVisibility(0);
                this.recordDeleteImageView.setVisibility(0);
                this.recordDeleteImageView.setAlpha(0.0f);
                this.recordDeleteImageView.setScaleY(0.0f);
                this.recordDeleteImageView.setScaleX(0.0f);
                int i4 = 0;
                while (true) {
                    if (i4 >= this.audioToSend.attributes.size()) {
                        d = 0.0d;
                        break;
                    }
                    TLRPC.DocumentAttribute documentAttribute = this.audioToSend.attributes.get(i4);
                    if (documentAttribute instanceof TLRPC.TL_documentAttributeAudio) {
                        d = documentAttribute.duration;
                        break;
                    }
                    i4++;
                }
                int i5 = 0;
                while (true) {
                    if (i5 >= this.audioToSend.attributes.size()) {
                        break;
                    }
                    TLRPC.DocumentAttribute documentAttribute2 = this.audioToSend.attributes.get(i5);
                    if (documentAttribute2 instanceof TLRPC.TL_documentAttributeAudio) {
                        byte[] bArr = documentAttribute2.waveform;
                        if (bArr == null || bArr.length == 0) {
                            documentAttribute2.waveform = MediaController.getWaveform(this.audioToSendPath);
                        }
                        this.recordedAudioSeekBar.setWaveform(documentAttribute2.waveform);
                    } else {
                        i5++;
                    }
                }
                this.millisecondsRecorded = (long) (1000.0d * d);
                this.recordedAudioTimeTextView.setText(AndroidUtilities.formatShortDuration((int) d));
                checkSendButton(false);
                if (z) {
                    createRecordCircle();
                    createRecordPanel();
                    createRecordAudioPanel();
                    this.recordInterfaceState = 1;
                    this.recordCircle.resetLockTranslation(false);
                    this.recordControlsCircleScale.set(this.recordCircle, Float.valueOf(1.0f));
                    ControlsView controlsView = this.controlsView;
                    if (controlsView != null) {
                        controlsView.setVisibility(0);
                        this.controlsView.setAlpha(1.0f);
                    }
                }
                updateRecordInterface(3, !z);
                return;
            }
            if (i == NotificationCenter.audioRouteChanged) {
                if (this.parentActivity != null) {
                    this.parentActivity.setVolumeControlStream(((Boolean) objArr[0]).booleanValue() ? 0 : Integer.MIN_VALUE);
                    return;
                }
                return;
            }
            if (i == NotificationCenter.messagePlayingDidReset) {
                if (this.audioToSendMessageObject == null || MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject)) {
                    return;
                }
                MediaActionDrawable mediaActionDrawable = this.playPauseDrawable;
                if (mediaActionDrawable != null) {
                    mediaActionDrawable.setIcon(0, true);
                }
                ImageView imageView = this.recordedAudioPlayButton;
                if (imageView != null) {
                    imageView.setContentDescription(LocaleController.getString("AccActionPlay", R.string.AccActionPlay));
                }
                SeekBarWaveformView seekBarWaveformView = this.recordedAudioSeekBar;
                if (seekBarWaveformView != null) {
                    seekBarWaveformView.setProgress(0.0f);
                    return;
                }
                return;
            }
            if (i == NotificationCenter.messagePlayingProgressDidChanged) {
                if (this.audioToSendMessageObject == null || !MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject)) {
                    return;
                }
                MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
                MessageObject messageObject = this.audioToSendMessageObject;
                messageObject.audioProgress = playingMessageObject.audioProgress;
                messageObject.audioProgressSec = playingMessageObject.audioProgressSec;
                if (this.recordedAudioSeekBar.isDragging()) {
                    return;
                }
                this.recordedAudioSeekBar.setProgress(this.audioToSendMessageObject.audioProgress);
                return;
            }
            if (i == NotificationCenter.featuredStickersDidLoad) {
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView2 = this.emojiButton;
                if (chatActivityEnterViewAnimatedIconView2 != null) {
                    chatActivityEnterViewAnimatedIconView2.invalidate();
                    return;
                }
                return;
            }
            if (i == NotificationCenter.messageReceivedByServer2) {
                if (((Boolean) objArr[6]).booleanValue() || ((Long) objArr[3]).longValue() != this.dialog_id || (chatFull = this.info) == null || chatFull.slowmode_seconds == 0 || (chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(this.info.id))) == null || ChatObject.hasAdminRights(chat) || ChatObject.isIgnoredChatRestrictionsForBoosters(chat)) {
                    return;
                }
                TLRPC.ChatFull chatFull2 = this.info;
                int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                TLRPC.ChatFull chatFull3 = this.info;
                chatFull2.slowmode_next_send_date = currentTime + chatFull3.slowmode_seconds;
                chatFull3.flags |= 262144;
                setSlowModeTimer(chatFull3.slowmode_next_send_date);
                return;
            }
            if (i == NotificationCenter.sendingMessagesChanged) {
                if (this.info != null) {
                    updateSlowModeText();
                    return;
                }
                return;
            }
            if (i != NotificationCenter.audioRecordTooShort) {
                if (i != NotificationCenter.updateBotMenuButton) {
                    if (i == NotificationCenter.didUpdatePremiumGiftFieldIcon) {
                        updateGiftButton(true);
                        return;
                    }
                    return;
                }
                long longValue = ((Long) objArr[0]).longValue();
                TL_bots.BotMenuButton botMenuButton = (TL_bots.BotMenuButton) objArr[1];
                if (longValue == this.dialog_id) {
                    if (botMenuButton instanceof TL_bots.TL_botMenuButton) {
                        TL_bots.TL_botMenuButton tL_botMenuButton = (TL_bots.TL_botMenuButton) botMenuButton;
                        this.botMenuWebViewTitle = tL_botMenuButton.text;
                        this.botMenuWebViewUrl = tL_botMenuButton.url;
                        botMenuButtonType = BotMenuButtonType.WEB_VIEW;
                    } else {
                        botMenuButtonType = (this.hasBotCommands || this.hasQuickReplies) ? BotMenuButtonType.COMMANDS : BotMenuButtonType.NO_BUTTON;
                    }
                    this.botMenuButtonType = botMenuButtonType;
                    updateBotButton(false);
                    return;
                }
                return;
            }
            this.audioToSend = null;
            this.videoToSendMessageObject = null;
        }
        updateRecordInterface(i3, true);
    }

    @Override // org.telegram.ui.Components.BlurredFrameLayout, android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        EmojiView emojiView = this.emojiView;
        if (emojiView == null || emojiView.getVisibility() != 0 || this.emojiView.getStickersExpandOffset() == 0.0f) {
            super.dispatchDraw(canvas);
            return;
        }
        canvas.save();
        canvas.clipRect(0, AndroidUtilities.dp(2.0f), getMeasuredWidth(), getMeasuredHeight());
        canvas.translate(0.0f, -this.emojiView.getStickersExpandOffset());
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public void doneEditingMessage() {
        MessagePreviewParams messagePreviewParams;
        int i;
        if (this.editingMessageObject == null) {
            return;
        }
        if (this.currentLimit - this.codePointCount < 0) {
            NumberTextView numberTextView = this.captionLimitView;
            if (numberTextView != null) {
                AndroidUtilities.shakeViewSpring(numberTextView, 3.5f);
                try {
                    this.captionLimitView.performHapticFeedback(3, 2);
                } catch (Exception unused) {
                }
            }
            if (MessagesController.getInstance(this.currentAccount).premiumFeaturesBlocked() || MessagesController.getInstance(this.currentAccount).captionLengthLimitPremium <= this.codePointCount) {
                return;
            }
            showCaptionLimitBulletin();
            return;
        }
        if (this.searchingType != 0) {
            setSearchingTypeInternal(0, true);
            this.emojiView.closeSearch(false);
            if (this.stickersExpanded) {
                setStickersExpanded(false, true, false);
                this.waitingForKeyboardOpenAfterAnimation = true;
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda76
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActivityEnterView.this.lambda$doneEditingMessage$50();
                    }
                }, 200L);
            }
        }
        EditTextCaption editTextCaption = this.messageEditText;
        CharSequence textToUse = editTextCaption == null ? "" : editTextCaption.getTextToUse();
        MessageObject messageObject = this.editingMessageObject;
        if (messageObject == null || messageObject.type != 19) {
            textToUse = AndroidUtilities.getTrimmedString(textToUse);
        }
        CharSequence[] charSequenceArr = {textToUse};
        if (TextUtils.isEmpty(charSequenceArr[0])) {
            TLRPC.MessageMedia messageMedia = this.editingMessageObject.messageOwner.media;
            if ((messageMedia instanceof TLRPC.TL_messageMediaWebPage) || (messageMedia instanceof TLRPC.TL_messageMediaEmpty) || messageMedia == null) {
                AndroidUtilities.shakeViewSpring(this.messageEditText, -3.0f);
                BotWebViewVibrationEffect.APP_ERROR.vibrate();
                return;
            }
        }
        ArrayList<TLRPC.MessageEntity> entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, supportsSendingNewEntities());
        if (!TextUtils.equals(charSequenceArr[0], this.editingMessageObject.messageText) || ((entities != null && !entities.isEmpty()) || !this.editingMessageObject.messageOwner.entities.isEmpty() || (this.editingMessageObject.messageOwner.media instanceof TLRPC.TL_messageMediaWebPage))) {
            MessageObject messageObject2 = this.editingMessageObject;
            messageObject2.editingMessage = charSequenceArr[0];
            messageObject2.editingMessageEntities = entities;
            messageObject2.editingMessageSearchWebPage = this.messageWebPageSearch;
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity == null || chatActivity.getCurrentChat() == null || (!((i = this.editingMessageObject.type) == 0 || i == 19) || ChatObject.canSendEmbed(this.parentFragment.getCurrentChat()))) {
                ChatActivity chatActivity2 = this.parentFragment;
                if (chatActivity2 == null || (messagePreviewParams = chatActivity2.messagePreviewParams) == null) {
                    MessageObject messageObject3 = this.editingMessageObject;
                    messageObject3.editingMessageSearchWebPage = false;
                    int i2 = messageObject3.type;
                    if (i2 == 0 || i2 == 19) {
                        TLRPC.Message message = messageObject3.messageOwner;
                        message.flags |= 512;
                        message.media = new TLRPC.TL_messageMediaEmpty();
                    }
                } else {
                    if (chatActivity2.foundWebPage instanceof TLRPC.TL_webPagePending) {
                        MessageObject messageObject4 = this.editingMessageObject;
                        messageObject4.editingMessageSearchWebPage = false;
                        int i3 = messageObject4.type;
                        if (i3 == 0 || i3 == 19) {
                            messageObject4.messageOwner.media = new TLRPC.TL_messageMediaEmpty();
                            this.editingMessageObject.messageOwner.flags |= 512;
                        }
                    } else if (messagePreviewParams.webpage != null) {
                        MessageObject messageObject5 = this.editingMessageObject;
                        messageObject5.editingMessageSearchWebPage = false;
                        TLRPC.Message message2 = messageObject5.messageOwner;
                        message2.flags |= 512;
                        message2.media = new TLRPC.TL_messageMediaWebPage();
                        this.editingMessageObject.messageOwner.media.webpage = this.parentFragment.messagePreviewParams.webpage;
                    } else {
                        MessageObject messageObject6 = this.editingMessageObject;
                        messageObject6.editingMessageSearchWebPage = false;
                        int i4 = messageObject6.type;
                        if (i4 == 0 || i4 == 19) {
                            TLRPC.Message message3 = messageObject6.messageOwner;
                            message3.flags |= 512;
                            message3.media = new TLRPC.TL_messageMediaEmpty();
                        }
                    }
                    TLRPC.Message message4 = this.editingMessageObject.messageOwner;
                    MessagePreviewParams messagePreviewParams2 = this.parentFragment.messagePreviewParams;
                    message4.invert_media = messagePreviewParams2.webpageTop;
                    if (messagePreviewParams2.hasMedia) {
                        TLRPC.MessageMedia messageMedia2 = message4.media;
                        if (messageMedia2 instanceof TLRPC.TL_messageMediaWebPage) {
                            boolean z = messagePreviewParams2.webpageSmall;
                            messageMedia2.force_small_media = z;
                            messageMedia2.force_large_media = true ^ z;
                        }
                    }
                }
            } else {
                MessageObject messageObject7 = this.editingMessageObject;
                messageObject7.editingMessageSearchWebPage = false;
                TLRPC.Message message5 = messageObject7.messageOwner;
                message5.flags &= -513;
                message5.media = null;
            }
            SendMessagesHelper sendMessagesHelper = SendMessagesHelper.getInstance(this.currentAccount);
            MessageObject messageObject8 = this.editingMessageObject;
            sendMessagesHelper.editMessage(messageObject8, null, null, null, null, null, false, messageObject8.hasMediaSpoilers(), null);
        }
        setEditingMessageObject(null, null, false);
    }

    public void drawBackground(Canvas canvas, boolean z) {
        float f;
        float width;
        float height;
        Paint themedPaint;
        if (this.shouldDrawBackground) {
            int intrinsicHeight = (int) (this.animatedTop + (Theme.chat_composeShadowDrawable.getIntrinsicHeight() * (1.0f - this.composeShadowAlpha)));
            View view = this.topView;
            if (view != null && view.getVisibility() == 0) {
                intrinsicHeight = (int) (intrinsicHeight + ((1.0f - this.topViewEnterProgress) * this.topView.getLayoutParams().height));
            }
            int intrinsicHeight2 = Theme.chat_composeShadowDrawable.getIntrinsicHeight() + intrinsicHeight;
            if (z) {
                Theme.chat_composeShadowDrawable.setAlpha((int) (this.composeShadowAlpha * 255.0f));
                Theme.chat_composeShadowDrawable.setBounds(0, intrinsicHeight, getMeasuredWidth(), intrinsicHeight2);
                Theme.chat_composeShadowDrawable.draw(canvas);
            }
            int i = (int) (intrinsicHeight2 + this.chatSearchExpandOffset);
            if (this.allowBlur) {
                this.backgroundPaint.setColor(getThemedColor(Theme.key_chat_messagePanelBackground));
                if (SharedConfig.chatBlurEnabled() && this.sizeNotifierLayout != null) {
                    this.blurBounds.set(0, i, getWidth(), getHeight());
                    this.sizeNotifierLayout.drawBlurRect(canvas, getTop(), this.blurBounds, this.backgroundPaint, false);
                    return;
                } else {
                    f = i;
                    width = getWidth();
                    height = getHeight();
                    themedPaint = this.backgroundPaint;
                }
            } else {
                f = i;
                width = getWidth();
                height = getHeight();
                themedPaint = getThemedPaint("paintChatComposeBackground");
            }
            canvas.drawRect(0.0f, f, width, height, themedPaint);
        }
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        boolean z = view == this.topView || view == this.textFieldContainer;
        if (z) {
            canvas.save();
            if (view == this.textFieldContainer) {
                int dp = (int) (this.animatedTop + AndroidUtilities.dp(2.0f) + this.chatSearchExpandOffset);
                View view2 = this.topView;
                if (view2 != null && view2.getVisibility() == 0) {
                    dp += this.topView.getHeight();
                }
                canvas.clipRect(0, dp, getMeasuredWidth(), getMeasuredHeight());
            } else {
                canvas.clipRect(0, this.animatedTop, getMeasuredWidth(), this.animatedTop + view.getLayoutParams().height + AndroidUtilities.dp(2.0f));
            }
        }
        boolean drawChild = super.drawChild(canvas, view, j);
        if (z) {
            canvas.restore();
        }
        return drawChild;
    }

    public boolean drawMessageEditText(Canvas canvas, Utilities.Callback0Return callback0Return) {
        float f = this.topGradientAlpha.set(this.messageEditText.canScrollVertically(-1));
        float f2 = this.bottomGradientAlpha.set(this.messageEditText.canScrollVertically(1));
        if (f <= 0.0f && f2 <= 0.0f) {
            return ((Boolean) callback0Return.run()).booleanValue();
        }
        canvas.saveLayerAlpha(0.0f, 0.0f, this.messageEditText.getX() + this.messageEditText.getMeasuredWidth() + AndroidUtilities.dp(5.0f), this.messageEditText.getY() + this.messageEditText.getMeasuredHeight() + AndroidUtilities.dp(2.0f), NotificationCenter.newLocationAvailable, 31);
        boolean booleanValue = ((Boolean) callback0Return.run()).booleanValue();
        canvas.save();
        if (f > 0.0f) {
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(this.messageEditText.getX() - AndroidUtilities.dp(5.0f), (this.messageEditText.getY() + this.animatedTop) - 1.0f, this.messageEditText.getX() + this.messageEditText.getMeasuredWidth() + AndroidUtilities.dp(5.0f), this.messageEditText.getY() + this.animatedTop + AndroidUtilities.dp(13.0f));
            this.clipMatrix.reset();
            this.clipMatrix.postScale(1.0f, rectF.height() / 16.0f);
            this.clipMatrix.postTranslate(rectF.left, rectF.top);
            this.clipGradient.setLocalMatrix(this.clipMatrix);
            this.gradientPaint.setAlpha((int) (f * 255.0f));
            canvas.drawRect(rectF, this.gradientPaint);
        }
        if (f2 > 0.0f) {
            RectF rectF2 = AndroidUtilities.rectTmp;
            rectF2.set(this.messageEditText.getX() - AndroidUtilities.dp(5.0f), (this.messageEditText.getY() + this.messageEditText.getMeasuredHeight()) - AndroidUtilities.dp(15.0f), this.messageEditText.getX() + this.messageEditText.getMeasuredWidth() + AndroidUtilities.dp(5.0f), this.messageEditText.getY() + this.messageEditText.getMeasuredHeight() + AndroidUtilities.dp(2.0f) + 1.0f);
            this.clipMatrix.reset();
            this.clipMatrix.postScale(1.0f, rectF2.height() / 16.0f);
            this.clipMatrix.postRotate(180.0f);
            this.clipMatrix.postTranslate(rectF2.left, rectF2.bottom);
            this.clipGradient.setLocalMatrix(this.clipMatrix);
            this.gradientPaint.setAlpha((int) (f2 * 255.0f));
            canvas.drawRect(rectF2, this.gradientPaint);
        }
        canvas.restore();
        canvas.restore();
        return booleanValue;
    }

    public void drawRecordedPannel(Canvas canvas) {
        FrameLayout frameLayout;
        if (getAlpha() == 0.0f || (frameLayout = this.recordedAudioPanel) == null || frameLayout.getParent() == null || this.recordedAudioPanel.getVisibility() != 0) {
            return;
        }
        int save = canvas.save();
        canvas.translate(getX() + this.textFieldContainer.getX() + this.messageEditTextContainer.getX() + this.recordedAudioPanel.getX(), getY() + this.textFieldContainer.getY() + this.messageEditTextContainer.getY() + this.recordedAudioPanel.getY());
        if (getAlpha() != 1.0f) {
            canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) (getAlpha() * 255.0f), 31);
        }
        this.recordedAudioPanel.draw(canvas);
        canvas.restoreToCount(save);
    }

    public void extendActionMode(Menu menu) {
    }

    public void forceSmoothKeyboard(boolean z) {
        ChatActivity chatActivity;
        this.smoothKeyboard = z && !AndroidUtilities.isInMultiwindow && ((chatActivity = this.parentFragment) == null || !chatActivity.isInBubbleMode());
    }

    public void freezeEmojiView(boolean z) {
        this.emojiViewFrozen = z;
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.freeze(z);
        }
    }

    public AdjustPanLayoutHelper getAdjustPanLayoutHelper() {
        return this.adjustPanLayoutHelper;
    }

    public int getAnimatedTop() {
        return this.animatedTop;
    }

    public ImageView getAttachButton() {
        return this.attachButton;
    }

    public View getAudioVideoButtonContainer() {
        return this.audioVideoButtonContainer;
    }

    public int getBackgroundTop() {
        int top = getTop();
        View view = this.topView;
        return (view == null || view.getVisibility() != 0) ? top : top + this.topView.getLayoutParams().height;
    }

    public ChatActivityBotWebViewButton getBotWebViewButton() {
        createBotWebViewButton();
        return this.botWebViewButton;
    }

    public int getCursorPosition() {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return 0;
        }
        return editTextCaption.getSelectionStart();
    }

    public CharSequence getDraftMessage() {
        if (this.editingMessageObject != null) {
            if (TextUtils.isEmpty(this.draftMessage)) {
                return null;
            }
            return this.draftMessage;
        }
        if (this.messageEditText == null || !hasText()) {
            return null;
        }
        return this.messageEditText.getText();
    }

    @Override // org.telegram.ui.Components.SuggestEmojiView.AnchorViewDelegate
    public EditTextCaption getEditField() {
        return this.messageEditText;
    }

    @Override // org.telegram.ui.Components.SuggestEmojiView.AnchorViewDelegate
    public Editable getEditText() {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return null;
        }
        return editTextCaption.getText();
    }

    public MessageObject getEditingMessageObject() {
        return this.editingMessageObject;
    }

    public long getEffectId() {
        return this.effectId;
    }

    public View getEmojiButton() {
        return this.emojiButton;
    }

    public int getEmojiPadding() {
        return this.emojiPadding;
    }

    public EmojiView getEmojiView() {
        return this.emojiView;
    }

    public float getExitTransition() {
        return this.exitTransition;
    }

    @Override // org.telegram.ui.Components.SuggestEmojiView.AnchorViewDelegate
    public CharSequence getFieldText() {
        if (this.messageEditText == null || !hasText()) {
            return null;
        }
        return this.messageEditText.getText();
    }

    public int getHeightWithTopView() {
        int measuredHeight = getMeasuredHeight();
        View view = this.topView;
        return (view == null || view.getVisibility() != 0) ? measuredHeight : (int) (measuredHeight - ((1.0f - this.topViewEnterProgress) * this.topView.getLayoutParams().height));
    }

    public float getLockAnimatedTranslation() {
        return this.lockAnimatedTranslation;
    }

    @Override // org.telegram.ui.Components.SuggestEmojiView.AnchorViewDelegate
    public ChatActivity getParentFragment() {
        return this.parentFragment;
    }

    public int getPopupViewHeight(View view) {
        BotKeyboardView botKeyboardView = this.botKeyboardView;
        if (view != botKeyboardView || botKeyboardView == null) {
            return -1;
        }
        return botKeyboardView.getKeyboardHeight();
    }

    public RecordCircle getRecordCircle() {
        return this.recordCircle;
    }

    public MessageObject getReplyingMessageObject() {
        return this.replyingMessageObject;
    }

    public int getSelectionLength() {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return 0;
        }
        try {
            return editTextCaption.getSelectionEnd() - this.messageEditText.getSelectionStart();
        } catch (Exception e) {
            FileLog.e(e);
            return 0;
        }
    }

    public View getSendButton() {
        return this.sendButton.getVisibility() == 0 ? this.sendButton : this.audioVideoButtonContainer;
    }

    public SizeNotifierFrameLayout getSizeNotifierLayout() {
        return this.sizeNotifierLayout;
    }

    public float getSlideToCancelProgress() {
        return this.slideToCancelProgress;
    }

    public CharSequence getSlowModeTimer() {
        if (this.slowModeTimer > 0) {
            return this.slowModeButton.getText();
        }
        return null;
    }

    public Drawable getStickersArrowDrawable() {
        return this.stickersArrow;
    }

    public int getStickersExpandedHeight() {
        return this.stickersExpandedHeight;
    }

    public float getTopViewHeight() {
        View view = this.topView;
        if (view == null || view.getVisibility() != 0) {
            return 0.0f;
        }
        return this.topView.getLayoutParams().height;
    }

    public float getTopViewTranslation() {
        View view = this.topView;
        if (view == null || view.getVisibility() == 8) {
            return 0.0f;
        }
        return this.topView.getTranslationY();
    }

    public TrendingStickersAlert getTrendingStickersAlert() {
        return this.trendingStickersAlert;
    }

    public int getVisibleEmojiPadding() {
        if (this.emojiViewVisible) {
            return this.emojiPadding;
        }
        return 0;
    }

    public boolean hasAudioToSend() {
        return (this.audioToSendMessageObject == null && this.videoToSendMessageObject == null) ? false : true;
    }

    public boolean hasBotWebView() {
        return this.botMenuButtonType == BotMenuButtonType.WEB_VIEW;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public boolean hasRecordVideo() {
        return this.hasRecordVideo;
    }

    public boolean hasText() {
        EditTextCaption editTextCaption = this.messageEditText;
        return editTextCaption != null && editTextCaption.length() > 0;
    }

    public void hideBotCommands() {
        BotCommandsMenuContainer botCommandsMenuContainer;
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        if (botCommandsMenuView != null) {
            botCommandsMenuView.setOpened(false);
        }
        if (hasBotWebView() || (botCommandsMenuContainer = this.botCommandsMenuContainer) == null) {
            return;
        }
        botCommandsMenuContainer.dismiss();
    }

    public void hideHints() {
        HintView2 hintView2 = this.birthdayHint;
        if (hintView2 != null) {
            hintView2.hide();
        }
    }

    public boolean hidePopup(boolean z) {
        return hidePopup(z, false);
    }

    public boolean hidePopup(boolean z, boolean z2) {
        TLRPC.TL_replyKeyboardMarkup tL_replyKeyboardMarkup;
        if (!isPopupShowing()) {
            return false;
        }
        if (this.currentPopupContentType == 1 && (tL_replyKeyboardMarkup = this.botReplyMarkup) != null && z && this.botButtonsMessageObject != null) {
            if (tL_replyKeyboardMarkup.is_persistent) {
                return false;
            }
            MessagesController.getMainSettings(this.currentAccount).edit().putInt("closed_botkeyboard_" + getTopicKeyString(), this.botButtonsMessageObject.getId()).apply();
        }
        if ((z && this.searchingType != 0) || z2) {
            setSearchingTypeInternal(0, true);
            EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.closeSearch(true);
            }
            EditTextCaption editTextCaption = this.messageEditText;
            if (editTextCaption != null) {
                editTextCaption.requestFocus();
            }
            setStickersExpanded(false, true, false);
            if (this.emojiTabOpen) {
                checkSendButton(true);
            }
        } else if (this.searchingType != 0) {
            setSearchingTypeInternal(0, false);
            this.emojiView.closeSearch(false);
            EditTextCaption editTextCaption2 = this.messageEditText;
            if (editTextCaption2 != null) {
                editTextCaption2.requestFocus();
            }
        } else if (this.stickersExpanded) {
            setStickersExpanded(false, true, false);
        } else {
            showPopup(0, 0);
        }
        return true;
    }

    public void hideTopView(boolean z) {
        if (this.topView == null || !this.topViewShowed) {
            return;
        }
        Runnable runnable = this.showTopViewRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
        }
        this.topViewShowed = false;
        this.needShowTopView = false;
        if (this.allowShowTopView) {
            ValueAnimator valueAnimator = this.currentTopViewAnimation;
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.currentTopViewAnimation = null;
            }
            if (z) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(this.topViewEnterProgress, 0.0f);
                this.currentTopViewAnimation = ofFloat;
                ofFloat.addUpdateListener(this.topViewUpdateListener);
                this.currentTopViewAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.47
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        ValueAnimator valueAnimator2 = ChatActivityEnterView.this.currentTopViewAnimation;
                        if (valueAnimator2 == null || !valueAnimator2.equals(animator)) {
                            return;
                        }
                        ChatActivityEnterView.this.currentTopViewAnimation = null;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ValueAnimator valueAnimator2 = ChatActivityEnterView.this.currentTopViewAnimation;
                        if (valueAnimator2 != null && valueAnimator2.equals(animator)) {
                            ChatActivityEnterView.this.topView.setVisibility(8);
                            ChatActivityEnterView.this.topLineView.setVisibility(8);
                            ChatActivityEnterView.this.resizeForTopView(false);
                            ChatActivityEnterView.this.currentTopViewAnimation = null;
                        }
                        if (ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentFragment.mentionContainer == null) {
                            return;
                        }
                        ChatActivityEnterView.this.parentFragment.mentionContainer.setTranslationY(0.0f);
                    }
                });
                this.currentTopViewAnimation.setDuration(250L);
                this.currentTopViewAnimation.setInterpolator(ChatListItemAnimator.DEFAULT_INTERPOLATOR);
                this.currentTopViewAnimation.start();
                return;
            }
            this.topViewEnterProgress = 0.0f;
            this.topView.setVisibility(8);
            this.topLineView.setVisibility(8);
            this.topLineView.setAlpha(0.0f);
            resizeForTopView(false);
            this.topView.setTranslationY(r4.getLayoutParams().height);
        }
    }

    public boolean isEditingCaption() {
        return this.editingCaption;
    }

    public boolean isEditingMessage() {
        return this.editingMessageObject != null;
    }

    @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
    public boolean isInScheduleMode() {
        ChatActivity chatActivity = this.parentFragment;
        return chatActivity != null && chatActivity.isInScheduleMode();
    }

    public boolean isInVideoMode() {
        return this.isInVideoMode;
    }

    public boolean isKeyboardVisible() {
        return this.keyboardVisible;
    }

    public boolean isMessageWebPageSearchEnabled() {
        return this.messageWebPageSearch;
    }

    public boolean isPopupShowing() {
        return this.emojiViewVisible || this.botKeyboardViewVisible;
    }

    public boolean isPopupView(View view) {
        return view == this.botKeyboardView || view == this.emojiView;
    }

    public boolean isRecordCircle(View view) {
        return view == this.recordCircle;
    }

    public boolean isRecordLocked() {
        return this.recordingAudioVideo && this.recordCircle.isSendButtonVisible();
    }

    public boolean isRecordingAudioVideo() {
        AnimatorSet animatorSet;
        return this.recordingAudioVideo || !((animatorSet = this.runningAnimationAudio) == null || !animatorSet.isRunning() || this.recordIsCanceled);
    }

    protected void isRecordingStateChanged() {
    }

    public boolean isSendButtonVisible() {
        return this.sendButton.getVisibility() == 0;
    }

    public boolean isStickersExpanded() {
        return this.stickersExpanded;
    }

    public boolean isTopViewVisible() {
        View view = this.topView;
        return view != null && view.getVisibility() == 0;
    }

    public boolean isWaitingForKeyboard() {
        return this.waitingForKeyboardOpen;
    }

    public void onAdjustPanTransitionEnd() {
        Runnable runnable = this.onKeyboardClosed;
        if (runnable != null) {
            runnable.run();
            this.onKeyboardClosed = null;
        }
    }

    public void onAdjustPanTransitionStart(boolean z, int i) {
        Runnable runnable;
        if (z && (runnable = this.showTopViewRunnable) != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.showTopViewRunnable.run();
        }
        Runnable runnable2 = this.setTextFieldRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.setTextFieldRunnable.run();
        }
    }

    public void onAdjustPanTransitionUpdate(float f, float f2, boolean z) {
    }

    public void onBeginHide() {
        Runnable runnable = this.focusRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.focusRunnable = null;
        }
    }

    public boolean onBotWebViewBackPressed() {
        return false;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        SenderSelectPopup senderSelectPopup = this.senderSelectPopupWindow;
        if (senderSelectPopup != null) {
            senderSelectPopup.setPauseNotifications(false);
            this.senderSelectPopupWindow.dismiss();
        }
    }

    public void onDestroy() {
        this.destroyed = true;
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStarted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordPaused);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordResumed);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStartError);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStopped);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioDidSent);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioRouteChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messageReceivedByServer2);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.sendingMessagesChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioRecordTooShort);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateBotMenuButton);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatePremiumGiftFieldIcon);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiLoaded);
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.onDestroy();
        }
        Runnable runnable = this.updateSlowModeRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.updateSlowModeRunnable = null;
        }
        PowerManager.WakeLock wakeLock = this.wakeLock;
        if (wakeLock != null) {
            try {
                wakeLock.release();
                this.wakeLock = null;
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
        if (sizeNotifierFrameLayout != null) {
            sizeNotifierFrameLayout.setDelegate(null);
        }
        SenderSelectPopup senderSelectPopup = this.senderSelectPopupWindow;
        if (senderSelectPopup != null) {
            senderSelectPopup.setPauseNotifications(false);
            this.senderSelectPopupWindow.dismiss();
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas, true);
    }

    public void onEditTimeExpired() {
        SendButton sendButton = this.doneButton;
        if (sendButton != null) {
            sendButton.setVisibility(8);
        }
    }

    protected void onEmojiIconChanged(ChatActivityEnterViewAnimatedIconView.State state) {
        if (state == ChatActivityEnterViewAnimatedIconView.State.GIF && this.emojiView == null) {
            MediaDataController.getInstance(this.currentAccount).loadRecents(0, true, true, false);
            ArrayList<String> arrayList = MessagesController.getInstance(this.currentAccount).gifSearchEmojies;
            int min = Math.min(10, arrayList.size());
            for (int i = 0; i < min; i++) {
                Emoji.preloadEmoji(arrayList.get(i));
            }
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.recordingAudioVideo) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        if (motionEvent.getAction() == 0 && AndroidUtilities.findChildViewUnder(this, motionEvent.getX(), motionEvent.getY()) != this.birthdayHint) {
            hideHints();
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        BotCommandsMenuContainer botCommandsMenuContainer;
        super.onLayout(z, i, i2, i3, i4);
        if (this.botCommandLastPosition == -1 || (botCommandsMenuContainer = this.botCommandsMenuContainer) == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) botCommandsMenuContainer.listView.getLayoutManager();
        if (linearLayoutManager != null) {
            linearLayoutManager.scrollToPositionWithOffset(this.botCommandLastPosition, this.botCommandLastTop);
        }
        this.botCommandLastPosition = -1;
    }

    protected void onLineCountChanged(int i, int i2) {
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        ViewGroup.MarginLayoutParams marginLayoutParams;
        int dp;
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        if (botCommandsMenuView == null || botCommandsMenuView.getTag() == null) {
            SenderSelectView senderSelectView = this.senderSelectView;
            if (senderSelectView == null || senderSelectView.getVisibility() != 0) {
                ((ViewGroup.MarginLayoutParams) this.emojiButton.getLayoutParams()).leftMargin = AndroidUtilities.dp(3.0f);
                EditTextCaption editTextCaption = this.messageEditText;
                if (editTextCaption != null) {
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) editTextCaption.getLayoutParams();
                    dp = AndroidUtilities.dp(50.0f);
                    marginLayoutParams.leftMargin = dp;
                }
            } else {
                int i3 = this.senderSelectView.getLayoutParams().width;
                this.senderSelectView.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(this.senderSelectView.getLayoutParams().height, 1073741824));
                ((ViewGroup.MarginLayoutParams) this.emojiButton.getLayoutParams()).leftMargin = AndroidUtilities.dp(16.0f) + i3;
                EditTextCaption editTextCaption2 = this.messageEditText;
                if (editTextCaption2 != null) {
                    ((ViewGroup.MarginLayoutParams) editTextCaption2.getLayoutParams()).leftMargin = AndroidUtilities.dp(63.0f) + i3;
                }
            }
        } else {
            this.botCommandsMenuButton.measure(i, i2);
            ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.emojiButton.getLayoutParams();
            int dp2 = AndroidUtilities.dp(10.0f);
            BotCommandsMenuView botCommandsMenuView2 = this.botCommandsMenuButton;
            marginLayoutParams2.leftMargin = dp2 + (botCommandsMenuView2 == null ? 0 : botCommandsMenuView2.getMeasuredWidth());
            EditTextCaption editTextCaption3 = this.messageEditText;
            if (editTextCaption3 != null) {
                marginLayoutParams = (ViewGroup.MarginLayoutParams) editTextCaption3.getLayoutParams();
                int dp3 = AndroidUtilities.dp(57.0f);
                BotCommandsMenuView botCommandsMenuView3 = this.botCommandsMenuButton;
                dp = dp3 + (botCommandsMenuView3 != null ? botCommandsMenuView3.getMeasuredWidth() : 0);
                marginLayoutParams.leftMargin = dp;
            }
        }
        updateBotCommandsMenuContainerTopPadding();
        super.onMeasure(i, i2);
        ChatActivityBotWebViewButton chatActivityBotWebViewButton = this.botWebViewButton;
        if (chatActivityBotWebViewButton != null) {
            BotCommandsMenuView botCommandsMenuView4 = this.botCommandsMenuButton;
            if (botCommandsMenuView4 != null) {
                chatActivityBotWebViewButton.setMeasuredButtonWidth(botCommandsMenuView4.getMeasuredWidth());
            }
            this.botWebViewButton.getLayoutParams().height = getMeasuredHeight() - AndroidUtilities.dp(2.0f);
            measureChild(this.botWebViewButton, i, i2);
        }
    }

    public void onOverrideAnimationEnd() {
        Runnable runnable = this.animationEndRunnable;
        if (runnable != null) {
            runnable.run();
            this.animationEndRunnable = null;
        }
    }

    public void onPause() {
        this.isPaused = true;
        SenderSelectPopup senderSelectPopup = this.senderSelectPopupWindow;
        if (senderSelectPopup != null) {
            senderSelectPopup.setPauseNotifications(false);
            this.senderSelectPopupWindow.dismiss();
        }
        if (this.keyboardVisible) {
            this.showKeyboardOnResume = true;
        }
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda19
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$onPause$42();
            }
        };
        this.hideKeyboardRunnable = runnable;
        AndroidUtilities.runOnUIThread(runnable, 500L);
    }

    public void onRequestPermissionsResultFragment(int i, String[] strArr, int[] iArr) {
        if (i != 2 || this.pendingLocationButton == null) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            SendMessagesHelper.getInstance(this.currentAccount).sendCurrentLocation(this.pendingMessageObject, this.pendingLocationButton);
        }
        this.pendingLocationButton = null;
        this.pendingMessageObject = null;
    }

    public void onResume() {
        EditTextCaption editTextCaption;
        this.isPaused = false;
        Runnable runnable = this.hideKeyboardRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.hideKeyboardRunnable = null;
        }
        if (hasBotWebView() && botCommandsMenuIsShowing()) {
            return;
        }
        getVisibility();
        if (!this.showKeyboardOnResume || BaseFragment.hasSheets(this.parentFragment)) {
            return;
        }
        this.showKeyboardOnResume = false;
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onKeyboardRequested();
        }
        if (this.searchingType == 0 && (editTextCaption = this.messageEditText) != null) {
            editTextCaption.requestFocus();
        }
        AndroidUtilities.showKeyboard(this.messageEditText);
        if (AndroidUtilities.usingHardwareInput || this.keyboardVisible || AndroidUtilities.isInMultiwindow) {
            return;
        }
        this.waitingForKeyboardOpen = true;
        AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
        AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3 && this.stickersExpanded) {
            setSearchingTypeInternal(0, false);
            this.emojiView.closeSearch(false);
            setStickersExpanded(false, false, false);
        }
        VideoTimelineView videoTimelineView = this.videoTimelineView;
        if (videoTimelineView != null) {
            videoTimelineView.clearFrames();
        }
    }

    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate
    public void onSizeChanged(int i, boolean z) {
        MessageObject messageObject;
        EditTextCaption editTextCaption;
        TLRPC.TL_replyKeyboardMarkup tL_replyKeyboardMarkup;
        boolean z2;
        SharedPreferences.Editor edit;
        int i2;
        String str;
        if (this.searchingType != 0) {
            this.lastSizeChangeValue1 = i;
            this.lastSizeChangeValue2 = z;
            this.keyboardVisible = i > 0;
            checkBotMenu();
            return;
        }
        if (i > AndroidUtilities.dp(50.0f) && this.keyboardVisible && !AndroidUtilities.isInMultiwindow) {
            if (z) {
                this.keyboardHeightLand = i;
                edit = MessagesController.getGlobalEmojiSettings().edit();
                i2 = this.keyboardHeightLand;
                str = "kbd_height_land3";
            } else {
                this.keyboardHeight = i;
                edit = MessagesController.getGlobalEmojiSettings().edit();
                i2 = this.keyboardHeight;
                str = "kbd_height";
            }
            edit.putInt(str, i2).commit();
        }
        if (this.keyboardVisible && this.emojiViewVisible && this.emojiView == null) {
            this.emojiViewVisible = false;
        }
        if (isPopupShowing()) {
            int i3 = z ? this.keyboardHeightLand : this.keyboardHeight;
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity != null && chatActivity.getParentLayout() != null) {
                i3 -= this.parentFragment.getParentLayout().getBottomTabsHeight(false);
            }
            if (this.currentPopupContentType == 1 && !this.botKeyboardView.isFullSize()) {
                i3 = Math.min(this.botKeyboardView.getKeyboardHeight(), i3);
            }
            int i4 = this.currentPopupContentType;
            View view = i4 == 0 ? this.emojiView : i4 == 1 ? this.botKeyboardView : null;
            BotKeyboardView botKeyboardView = this.botKeyboardView;
            if (botKeyboardView != null) {
                botKeyboardView.setPanelHeight(i3);
            }
            if (view != null) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                if (!this.closeAnimationInProgress) {
                    int i5 = layoutParams.width;
                    int i6 = AndroidUtilities.displaySize.x;
                    if ((i5 != i6 || layoutParams.height != i3) && !this.stickersExpanded) {
                        layoutParams.width = i6;
                        layoutParams.height = i3;
                        view.setLayoutParams(layoutParams);
                        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
                        if (sizeNotifierFrameLayout != null) {
                            int i7 = this.emojiPadding;
                            this.emojiPadding = layoutParams.height;
                            sizeNotifierFrameLayout.requestLayout();
                            onWindowSizeChanged();
                            if (this.smoothKeyboard && !this.keyboardVisible && i7 != this.emojiPadding && pannelAnimationEnabled()) {
                                AnimatorSet animatorSet = new AnimatorSet();
                                this.panelAnimation = animatorSet;
                                animatorSet.playTogether(ObjectAnimator.ofFloat(view, (Property<View, Float>) View.TRANSLATION_Y, this.emojiPadding - i7, 0.0f));
                                this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                                this.panelAnimation.setDuration(250L);
                                this.panelAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.78
                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public void onAnimationEnd(Animator animator) {
                                        ChatActivityEnterView.this.panelAnimation = null;
                                        if (ChatActivityEnterView.this.delegate != null) {
                                            ChatActivityEnterView.this.delegate.bottomPanelTranslationYChanged(0.0f);
                                        }
                                        ChatActivityEnterView.this.requestLayout();
                                        ChatActivityEnterView.this.notificationsLocker.unlock();
                                    }
                                });
                                AndroidUtilities.runOnUIThread(this.runEmojiPanelAnimation, 50L);
                                this.notificationsLocker.lock();
                                requestLayout();
                            }
                        }
                    }
                }
            }
        }
        if (this.lastSizeChangeValue1 == i && this.lastSizeChangeValue2 == z) {
            onWindowSizeChanged();
            return;
        }
        this.lastSizeChangeValue1 = i;
        this.lastSizeChangeValue2 = z;
        boolean z3 = this.keyboardVisible;
        this.keyboardVisible = i > 0;
        checkBotMenu();
        if (this.keyboardVisible && isPopupShowing() && this.stickersExpansionAnim == null) {
            showPopup(0, this.currentPopupContentType);
        } else if (!this.keyboardVisible && !isPopupShowing() && (messageObject = this.botButtonsMessageObject) != null && this.replyingMessageObject != messageObject && !hasBotWebView() && !botCommandsMenuIsShowing() && !BaseFragment.hasSheets(this.parentFragment) && (((editTextCaption = this.messageEditText) == null || TextUtils.isEmpty(editTextCaption.getText())) && (tL_replyKeyboardMarkup = this.botReplyMarkup) != null && !tL_replyKeyboardMarkup.rows.isEmpty())) {
            if (this.sizeNotifierLayout.adjustPanLayoutHelper.animationInProgress()) {
                this.sizeNotifierLayout.adjustPanLayoutHelper.stopTransition();
            } else {
                this.sizeNotifierLayout.adjustPanLayoutHelper.ignoreOnce();
            }
            showPopup(1, 1, false);
        }
        if (this.emojiPadding != 0 && !(z2 = this.keyboardVisible) && z2 != z3 && !isPopupShowing()) {
            this.emojiPadding = 0;
            this.sizeNotifierLayout.requestLayout();
        }
        if (this.keyboardVisible && this.waitingForKeyboardOpen) {
            this.waitingForKeyboardOpen = false;
            if (this.clearBotButtonsOnKeyboardOpen) {
                this.clearBotButtonsOnKeyboardOpen = false;
                this.botKeyboardView.setButtons(this.botReplyMarkup);
            }
            AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
        }
        onWindowSizeChanged();
    }

    @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
    /* renamed from: onStickerSelected, reason: merged with bridge method [inline-methods] */
    public void lambda$onStickerSelected$68(final TLRPC.Document document, final String str, final Object obj, final MessageObject.SendAnimationData sendAnimationData, final boolean z, final boolean z2, final int i) {
        ChatActivity chatActivity;
        ChatActivity.ReplyQuote replyQuote = this.replyingQuote;
        if (replyQuote != null && (chatActivity = this.parentFragment) != null && replyQuote.outdated) {
            chatActivity.showQuoteMessageUpdate();
            return;
        }
        if (isInScheduleMode() && i == 0) {
            AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda63
                @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                public final void didSelectDate(boolean z3, int i2) {
                    ChatActivityEnterView.this.lambda$onStickerSelected$68(document, str, obj, sendAnimationData, z, z3, i2);
                }
            }, this.resourcesProvider);
            return;
        }
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda64
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$onStickerSelected$69(document, str, sendAnimationData, z2, i, obj, z);
            }
        };
        if (showConfirmAlert(runnable)) {
            return;
        }
        runnable.run();
    }

    public void openKeyboard() {
        if ((hasBotWebView() && botCommandsMenuIsShowing()) || BaseFragment.hasSheets(this.parentFragment)) {
            return;
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onKeyboardRequested();
        }
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null || AndroidUtilities.showKeyboard(editTextCaption)) {
            return;
        }
        this.messageEditText.clearFocus();
        this.messageEditText.requestFocus();
    }

    public void openKeyboardInternal() {
        ChatActivity chatActivity;
        if ((hasBotWebView() && botCommandsMenuIsShowing()) || BaseFragment.hasSheets(this.parentFragment)) {
            return;
        }
        showPopup((AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow || ((chatActivity = this.parentFragment) != null && chatActivity.isInBubbleMode()) || this.isPaused) ? 0 : 2, 0);
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onKeyboardRequested();
        }
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.requestFocus();
        }
        AndroidUtilities.showKeyboard(this.messageEditText);
        if (this.isPaused) {
            this.showKeyboardOnResume = true;
            return;
        }
        if (AndroidUtilities.usingHardwareInput || this.keyboardVisible || AndroidUtilities.isInMultiwindow) {
            return;
        }
        ChatActivity chatActivity2 = this.parentFragment;
        if (chatActivity2 == null || !chatActivity2.isInBubbleMode()) {
            this.waitingForKeyboardOpen = true;
            EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.onTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 3, 0.0f, 0.0f, 0));
            }
            AndroidUtilities.cancelRunOnUIThread(this.openKeyboardRunnable);
            AndroidUtilities.runOnUIThread(this.openKeyboardRunnable, 100L);
        }
    }

    public boolean panelAnimationInProgress() {
        return this.panelAnimation != null;
    }

    protected boolean pannelAnimationEnabled() {
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:118:0x0198  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0193  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0211  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x022f  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01e0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean processSendingText(CharSequence charSequence, boolean z, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        int min;
        MessageObject.SendAnimationData sendAnimationData;
        MessageObject.SendAnimationData sendAnimationData2;
        ChatActivity chatActivity;
        TLRPC.WebPage webPage;
        MessagePreviewParams messagePreviewParams;
        MessagePreviewParams messagePreviewParams2;
        ChatActivity chatActivity2;
        boolean z2;
        MessagePreviewParams messagePreviewParams3;
        MessageObject messageObject;
        int dp;
        ChatActivity chatActivity3;
        ChatActivity chatActivity4;
        ChatActivity.ReplyQuote replyQuote = this.replyingQuote;
        if (replyQuote != null && (chatActivity4 = this.parentFragment) != null && replyQuote.outdated) {
            chatActivity4.showQuoteMessageUpdate();
            return false;
        }
        int[] iArr = new int[1];
        CharSequence charSequence2 = charSequence;
        Emoji.parseEmojis(charSequence2, iArr);
        boolean z3 = iArr[0] > 0;
        if (!z3) {
            charSequence2 = AndroidUtilities.getTrimmedString(charSequence);
        }
        boolean supportsSendingNewEntities = supportsSendingNewEntities();
        int i6 = this.accountInstance.getMessagesController().maxMessageLength;
        if (charSequence2.length() == 0) {
            return false;
        }
        if (this.delegate != null && (chatActivity3 = this.parentFragment) != null) {
            if ((i != 0) == chatActivity3.isInScheduleMode()) {
                this.delegate.prepareMessageSending();
            }
        }
        int i7 = 0;
        do {
            int i8 = i7 + i6;
            if (charSequence2.length() > i8) {
                i2 = i8 - 1;
                i3 = -1;
                i4 = -1;
                i5 = -1;
                for (int i9 = 0; i2 > i7 && i9 < 300; i9++) {
                    char charAt = charSequence2.charAt(i2);
                    char charAt2 = i2 > 0 ? charSequence2.charAt(i2 - 1) : ' ';
                    if (charAt == '\n' && charAt2 == '\n') {
                        break;
                    }
                    if (charAt == '\n') {
                        i5 = i2;
                    } else if (i3 < 0 && Character.isWhitespace(charAt) && charAt2 == '.') {
                        i3 = i2;
                    } else if (i4 < 0 && Character.isWhitespace(charAt)) {
                        i4 = i2;
                    }
                    i2--;
                }
                i2 = -1;
            } else {
                i2 = -1;
                i3 = -1;
                i4 = -1;
                i5 = -1;
            }
            min = Math.min(i8, charSequence2.length());
            if (i2 > 0) {
                min = i2;
            } else if (i5 > 0) {
                min = i5;
            } else if (i3 > 0) {
                min = i3;
            } else if (i4 > 0) {
                min = i4;
            }
            CharSequence subSequence = charSequence2.subSequence(i7, min);
            if (!z3) {
                subSequence = AndroidUtilities.getTrimmedString(subSequence);
            }
            CharSequence[] charSequenceArr = {subSequence};
            ArrayList<TLRPC.MessageEntity> entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, supportsSendingNewEntities);
            if (!this.delegate.hasForwardingMessages()) {
                sendAnimationData2 = new MessageObject.SendAnimationData();
                sendAnimationData2.fromPreview = System.currentTimeMillis() - this.sentFromPreview < 200;
                float dp2 = AndroidUtilities.dp(22.0f);
                sendAnimationData2.height = dp2;
                sendAnimationData2.width = dp2;
                EditTextCaption editTextCaption = this.messageEditText;
                if (editTextCaption != null) {
                    editTextCaption.getLocationInWindow(this.location);
                    sendAnimationData2.x = this.location[0] + AndroidUtilities.dp(11.0f);
                    dp = this.location[1] + AndroidUtilities.dp(19.0f);
                } else {
                    sendAnimationData2.x = AndroidUtilities.dp(59.0f);
                    dp = AndroidUtilities.displaySize.y - AndroidUtilities.dp(19.0f);
                }
                sendAnimationData2.y = dp;
            } else if (this.messageSendPreview != null) {
                sendAnimationData2 = new MessageObject.SendAnimationData();
                sendAnimationData2.fromPreview = System.currentTimeMillis() - this.sentFromPreview < 200;
            } else {
                sendAnimationData = null;
                boolean checkUpdateStickersOrder = SendMessagesHelper.checkUpdateStickersOrder(charSequence2);
                MessageObject threadMessage = getThreadMessage();
                SendMessagesHelper.SendMessageParams of = SendMessagesHelper.SendMessageParams.of(charSequenceArr[0].toString(), this.dialog_id, this.replyingMessageObject, (threadMessage == null || (messageObject = this.replyingTopMessage) == null) ? threadMessage : messageObject, this.messageWebPage, this.messageWebPageSearch, entities, null, null, z, i, sendAnimationData, checkUpdateStickersOrder);
                ChatActivity chatActivity5 = this.parentFragment;
                of.quick_reply_shortcut = chatActivity5 == null ? chatActivity5.quickReplyShortcut : null;
                of.quick_reply_shortcut_id = chatActivity5 == null ? chatActivity5.getQuickReplyId() : 0;
                of.effect_id = this.effectId;
                SendButton sendButton = this.sendButton;
                this.effectId = 0L;
                sendButton.setEffect(0L);
                applyStoryToSendMessageParams(of);
                chatActivity = this.parentFragment;
                of.invert_media = chatActivity == null && (messagePreviewParams3 = chatActivity.messagePreviewParams) != null && messagePreviewParams3.webpageTop;
                if (chatActivity != null || chatActivity.getCurrentChat() == null || ChatObject.canSendEmbed(this.parentFragment.getCurrentChat())) {
                    webPage = this.messageWebPage;
                    if (webPage instanceof TLRPC.TL_webPagePending) {
                        if (webPage != null) {
                            TLRPC.TL_messageMediaWebPage tL_messageMediaWebPage = new TLRPC.TL_messageMediaWebPage();
                            of.mediaWebPage = tL_messageMediaWebPage;
                            tL_messageMediaWebPage.webpage = this.messageWebPage;
                            ChatActivity chatActivity6 = this.parentFragment;
                            tL_messageMediaWebPage.force_large_media = (chatActivity6 == null || (messagePreviewParams2 = chatActivity6.messagePreviewParams) == null || messagePreviewParams2.webpageSmall) ? false : true;
                            tL_messageMediaWebPage.force_small_media = (chatActivity6 == null || (messagePreviewParams = chatActivity6.messagePreviewParams) == null || !messagePreviewParams.webpageSmall) ? false : true;
                        }
                        chatActivity2 = this.parentFragment;
                        if (chatActivity2 == null) {
                            chatActivity2.editingMessageObject = null;
                            chatActivity2.foundWebPage = null;
                            MessagePreviewParams messagePreviewParams4 = chatActivity2.messagePreviewParams;
                            if (messagePreviewParams4 != null) {
                                messagePreviewParams4.updateLink(this.currentAccount, null, "", null, null, null);
                            }
                            z2 = true;
                            setWebPage(null, true);
                            this.parentFragment.fallbackFieldPanel();
                        } else {
                            z2 = true;
                        }
                        SendMessagesHelper.getInstance(this.currentAccount).sendMessage(of);
                        i7 = min + 1;
                    } else {
                        of.searchLinks = true;
                    }
                } else {
                    of.searchLinks = false;
                }
                of.mediaWebPage = null;
                chatActivity2 = this.parentFragment;
                if (chatActivity2 == null) {
                }
                SendMessagesHelper.getInstance(this.currentAccount).sendMessage(of);
                i7 = min + 1;
            }
            sendAnimationData = sendAnimationData2;
            boolean checkUpdateStickersOrder2 = SendMessagesHelper.checkUpdateStickersOrder(charSequence2);
            MessageObject threadMessage2 = getThreadMessage();
            SendMessagesHelper.SendMessageParams of2 = SendMessagesHelper.SendMessageParams.of(charSequenceArr[0].toString(), this.dialog_id, this.replyingMessageObject, (threadMessage2 == null || (messageObject = this.replyingTopMessage) == null) ? threadMessage2 : messageObject, this.messageWebPage, this.messageWebPageSearch, entities, null, null, z, i, sendAnimationData, checkUpdateStickersOrder2);
            ChatActivity chatActivity52 = this.parentFragment;
            of2.quick_reply_shortcut = chatActivity52 == null ? chatActivity52.quickReplyShortcut : null;
            of2.quick_reply_shortcut_id = chatActivity52 == null ? chatActivity52.getQuickReplyId() : 0;
            of2.effect_id = this.effectId;
            SendButton sendButton2 = this.sendButton;
            this.effectId = 0L;
            sendButton2.setEffect(0L);
            applyStoryToSendMessageParams(of2);
            chatActivity = this.parentFragment;
            of2.invert_media = chatActivity == null && (messagePreviewParams3 = chatActivity.messagePreviewParams) != null && messagePreviewParams3.webpageTop;
            if (chatActivity != null) {
            }
            webPage = this.messageWebPage;
            if (webPage instanceof TLRPC.TL_webPagePending) {
            }
        } while (min != charSequence2.length());
        return z2;
    }

    public void replaceWithText(int i, int i2, CharSequence charSequence, boolean z) {
        if (this.messageEditText == null) {
            return;
        }
        try {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(this.messageEditText.getText());
            spannableStringBuilder.replace(i, i2 + i, charSequence);
            if (z) {
                Emoji.replaceEmoji((CharSequence) spannableStringBuilder, this.messageEditText.getPaint().getFontMetricsInt(), false, (int[]) null);
            }
            this.messageEditText.setText(spannableStringBuilder);
            this.messageEditText.setSelection(i + charSequence.length());
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void reset() {
        setStickersExpanded(false, true, false);
        showPopup(0, 0, false);
        if (getEditField() != null && !TextUtils.isEmpty(getEditField().getText())) {
            getEditField().setText("");
        }
        this.recordingAudioVideo = false;
        ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView = this.audioVideoSendButton;
        if (chatActivityEnterViewAnimatedIconView != null) {
            chatActivityEnterViewAnimatedIconView.setVisibility(0);
        }
        this.recordIsCanceled = true;
        isRecordingStateChanged();
        cancelRecordInterfaceInternal();
        hideRecordedAudioPanelInternal();
        ControlsView controlsView = this.controlsView;
        if (controlsView != null) {
            controlsView.setVisibility(8);
        }
        RecordCircle recordCircle = this.recordCircle;
        if (recordCircle != null) {
            recordCircle.setSendButtonInvisible();
        }
    }

    public void runEmojiPanelAnimation() {
        AndroidUtilities.cancelRunOnUIThread(this.runEmojiPanelAnimation);
        this.runEmojiPanelAnimation.run();
    }

    public boolean seekbarVisible() {
        return !this.recordIsCanceled && this.transformToSeekbar > 0.0f;
    }

    public void setAdjustPanLayoutHelper(AdjustPanLayoutHelper adjustPanLayoutHelper) {
        this.adjustPanLayoutHelper = adjustPanLayoutHelper;
    }

    public void setAllowStickersAndGifs(boolean z, boolean z2, boolean z3) {
        setAllowStickersAndGifs(z, z2, z3, false);
    }

    public void setAllowStickersAndGifs(boolean z, boolean z2, boolean z3, boolean z4) {
        if ((this.allowStickers != z2 || this.allowGifs != z3) && this.emojiView != null) {
            if (this.emojiViewVisible && !z4) {
                this.removeEmojiViewAfterAnimation = true;
                hidePopup(false);
            } else if (z4) {
                openKeyboardInternal();
            }
        }
        this.allowAnimatedEmoji = z;
        this.allowStickers = z2;
        this.allowGifs = z3;
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.setAllow(z2, z3, true);
        }
        setEmojiButtonImage(false, !this.isPaused);
    }

    public void setBotInfo(LongSparseArray longSparseArray) {
        setBotInfo(longSparseArray, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0045  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setBotInfo(LongSparseArray longSparseArray, boolean z) {
        BotMenuButtonType botMenuButtonType;
        BotCommandsMenuView.BotCommandsAdapter botCommandsAdapter;
        this.lastBotInfo = longSparseArray;
        if (longSparseArray.size() == 1 && ((TL_bots.BotInfo) longSparseArray.valueAt(0)).user_id == this.dialog_id) {
            TL_bots.BotInfo botInfo = (TL_bots.BotInfo) longSparseArray.valueAt(0);
            TL_bots.BotMenuButton botMenuButton = botInfo.menu_button;
            if (botMenuButton instanceof TL_bots.TL_botMenuButton) {
                TL_bots.TL_botMenuButton tL_botMenuButton = (TL_bots.TL_botMenuButton) botMenuButton;
                this.botMenuWebViewTitle = tL_botMenuButton.text;
                this.botMenuWebViewUrl = tL_botMenuButton.url;
                botMenuButtonType = BotMenuButtonType.WEB_VIEW;
            } else if (!botInfo.commands.isEmpty()) {
                botMenuButtonType = BotMenuButtonType.COMMANDS;
            }
            this.botMenuButtonType = botMenuButtonType;
            botCommandsAdapter = this.botCommandsAdapter;
            if (botCommandsAdapter != null) {
                botCommandsAdapter.setBotInfo(longSparseArray);
            }
            updateBotButton(z);
        }
        botMenuButtonType = BotMenuButtonType.NO_BUTTON;
        this.botMenuButtonType = botMenuButtonType;
        botCommandsAdapter = this.botCommandsAdapter;
        if (botCommandsAdapter != null) {
        }
        updateBotButton(z);
    }

    public void setBotWebViewButtonOffsetX(float f) {
        this.emojiButton.setTranslationX(f);
        if (this.messageEditText != null) {
            this.messageTextTranslationX = f;
            updateMessageTextParams();
        }
        this.attachButton.setTranslationX(f);
        this.audioVideoSendButton.setTranslationX(f);
        ImageView imageView = this.botButton;
        if (imageView != null) {
            imageView.setTranslationX(f);
        }
    }

    public void setBotsCount(int i, boolean z, boolean z2, boolean z3) {
        this.botCount = i;
        if (this.hasBotCommands == z && this.hasQuickReplies == z2) {
            return;
        }
        this.hasBotCommands = z;
        this.hasQuickReplies = z2;
        updateBotButton(z3);
    }

    public void setButtons(MessageObject messageObject) {
        setButtons(messageObject, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x009f, code lost:
    
        if (r7.getInt("answered_" + getTopicKeyString(), 0) != r6.getId()) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00c4, code lost:
    
        if (r7.getInt("closed_botkeyboard_" + getTopicKeyString(), 0) == r6.getId()) goto L43;
     */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0065  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setButtons(MessageObject messageObject, boolean z) {
        TLRPC.TL_replyKeyboardMarkup tL_replyKeyboardMarkup;
        EditTextCaption editTextCaption;
        MessageObject messageObject2 = this.replyingMessageObject;
        if (messageObject2 != null && messageObject2 == this.botButtonsMessageObject && messageObject2 != messageObject) {
            this.botMessageObject = messageObject;
            return;
        }
        MessageObject messageObject3 = this.botButtonsMessageObject;
        if (messageObject3 == null || messageObject3 != messageObject) {
            if (messageObject3 == null && messageObject == null) {
                return;
            }
            boolean z2 = false;
            if (this.botKeyboardView == null) {
                BotKeyboardView botKeyboardView = new BotKeyboardView(this.parentActivity, this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatActivityEnterView.69
                    @Override // android.view.View
                    public void setTranslationY(float f) {
                        super.setTranslationY(f);
                        if (ChatActivityEnterView.this.panelAnimation == null || ChatActivityEnterView.this.animatingContentType != 1) {
                            return;
                        }
                        ChatActivityEnterView.this.delegate.bottomPanelTranslationYChanged(f);
                    }
                };
                this.botKeyboardView = botKeyboardView;
                botKeyboardView.setVisibility(8);
                this.botKeyboardViewVisible = false;
                this.botKeyboardView.setDelegate(new BotKeyboardView.BotKeyboardViewDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda29
                    @Override // org.telegram.ui.bots.BotKeyboardView.BotKeyboardViewDelegate
                    public final void didPressedButton(TLRPC.KeyboardButton keyboardButton) {
                        ChatActivityEnterView.this.lambda$setButtons$62(keyboardButton);
                    }
                });
                SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
                sizeNotifierFrameLayout.addView(this.botKeyboardView, sizeNotifierFrameLayout.getChildCount() - 1);
            }
            this.botButtonsMessageObject = messageObject;
            if (messageObject != null) {
                TLRPC.ReplyMarkup replyMarkup = messageObject.messageOwner.reply_markup;
                if (replyMarkup instanceof TLRPC.TL_replyKeyboardMarkup) {
                    tL_replyKeyboardMarkup = (TLRPC.TL_replyKeyboardMarkup) replyMarkup;
                    this.botReplyMarkup = tL_replyKeyboardMarkup;
                    BotKeyboardView botKeyboardView2 = this.botKeyboardView;
                    android.graphics.Point point = AndroidUtilities.displaySize;
                    botKeyboardView2.setPanelHeight(point.x <= point.y ? this.keyboardHeightLand : this.keyboardHeight);
                    if (this.botReplyMarkup == null) {
                        SharedPreferences mainSettings = MessagesController.getMainSettings(this.currentAccount);
                        if (this.botButtonsMessageObject != this.replyingMessageObject && messageObject != null) {
                            if (this.botReplyMarkup.single_use) {
                            }
                            if (!this.botReplyMarkup.is_persistent) {
                            }
                        }
                        z2 = true;
                        this.botKeyboardView.setButtons(this.botReplyMarkup);
                        if (z2 && (((editTextCaption = this.messageEditText) == null || editTextCaption.length() == 0) && !isPopupShowing())) {
                            showPopup(1, 1);
                        }
                    } else if (isPopupShowing() && this.currentPopupContentType == 1) {
                        if (z) {
                            this.clearBotButtonsOnKeyboardOpen = true;
                            openKeyboardInternal();
                        } else {
                            showPopup(0, 1);
                        }
                    }
                    updateBotButton(true);
                }
            }
            tL_replyKeyboardMarkup = null;
            this.botReplyMarkup = tL_replyKeyboardMarkup;
            BotKeyboardView botKeyboardView22 = this.botKeyboardView;
            android.graphics.Point point2 = AndroidUtilities.displaySize;
            botKeyboardView22.setPanelHeight(point2.x <= point2.y ? this.keyboardHeightLand : this.keyboardHeight);
            if (this.botReplyMarkup == null) {
            }
            updateBotButton(true);
        }
    }

    public void setCaption(String str) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.setCaption(str);
            checkSendButton(true);
        }
    }

    public void setChatInfo(TLRPC.ChatFull chatFull) {
        this.info = chatFull;
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.setChatInfo(chatFull);
        }
        SlowModeBtn slowModeBtn = this.slowModeButton;
        if (slowModeBtn != null) {
            slowModeBtn.setPremiumMode(ChatObject.isPossibleRemoveChatRestrictionsByBoosts(chatFull));
        }
        if (ChatObject.isIgnoredChatRestrictionsForBoosters(chatFull)) {
            return;
        }
        setSlowModeTimer(chatFull.slowmode_next_send_date);
    }

    public void setChatSearchExpandOffset(float f) {
        this.chatSearchExpandOffset = f;
        invalidate();
    }

    public void setCommand(MessageObject messageObject, String str, boolean z, boolean z2) {
        EditTextCaption editTextCaption;
        String str2;
        if (str == null || getVisibility() != 0 || (editTextCaption = this.messageEditText) == null) {
            return;
        }
        TLRPC.User user = null;
        if (!z) {
            if (this.slowModeTimer > 0 && !isInScheduleMode()) {
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
                if (chatActivityEnterViewDelegate != null) {
                    SlowModeBtn slowModeBtn = this.slowModeButton;
                    chatActivityEnterViewDelegate.onUpdateSlowModeButton(slowModeBtn, true, slowModeBtn.getText());
                    return;
                }
                return;
            }
            TLRPC.User user2 = (messageObject == null || !DialogObject.isChatDialog(this.dialog_id)) ? null : this.accountInstance.getMessagesController().getUser(Long.valueOf(messageObject.messageOwner.from_id.user_id));
            SendMessagesHelper.SendMessageParams of = ((this.botCount != 1 || z2) && user2 != null && user2.bot && !str.contains("@")) ? SendMessagesHelper.SendMessageParams.of(String.format(Locale.US, "%s@%s", str, UserObject.getPublicUsername(user2)), this.dialog_id, this.replyingMessageObject, getThreadMessage(), null, false, null, null, null, true, 0, null, false) : SendMessagesHelper.SendMessageParams.of(str, this.dialog_id, this.replyingMessageObject, getThreadMessage(), null, false, null, null, null, true, 0, null, false);
            ChatActivity chatActivity = this.parentFragment;
            of.quick_reply_shortcut = chatActivity != null ? chatActivity.quickReplyShortcut : null;
            of.quick_reply_shortcut_id = chatActivity != null ? chatActivity.getQuickReplyId() : 0;
            of.effect_id = this.effectId;
            SendButton sendButton = this.sendButton;
            this.effectId = 0L;
            sendButton.setEffect(0L);
            applyStoryToSendMessageParams(of);
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(of);
            return;
        }
        String obj = editTextCaption.getText().toString();
        if (messageObject != null && DialogObject.isChatDialog(this.dialog_id)) {
            user = this.accountInstance.getMessagesController().getUser(Long.valueOf(messageObject.messageOwner.from_id.user_id));
        }
        TLRPC.User user3 = user;
        if ((this.botCount != 1 || z2) && user3 != null && user3.bot && !str.contains("@")) {
            str2 = String.format(Locale.US, "%s@%s", str, UserObject.getPublicUsername(user3)) + " " + obj.replaceFirst("^/[a-zA-Z@\\d_]{1,255}(\\s|$)", "");
        } else {
            str2 = str + " " + obj.replaceFirst("^/[a-zA-Z@\\d_]{1,255}(\\s|$)", "");
        }
        this.ignoreTextChange = true;
        this.messageEditText.setText(str2);
        EditTextCaption editTextCaption2 = this.messageEditText;
        editTextCaption2.setSelection(editTextCaption2.getText().length());
        this.ignoreTextChange = false;
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
        if (chatActivityEnterViewDelegate2 != null) {
            chatActivityEnterViewDelegate2.onTextChanged(this.messageEditText.getText(), true, false);
        }
        if (this.keyboardVisible || this.currentPopupContentType != -1) {
            return;
        }
        openKeyboard();
    }

    public void setComposeShadowAlpha(float f) {
        this.composeShadowAlpha = f;
        invalidate();
    }

    public void setDelegate(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
        this.delegate = chatActivityEnterViewDelegate;
    }

    public void setDialogId(long j, int i) {
        this.dialog_id = j;
        if (this.currentAccount != i) {
            this.notificationsLocker.unlock();
            NotificationCenter notificationCenter = NotificationCenter.getInstance(this.currentAccount);
            int i2 = NotificationCenter.recordStarted;
            notificationCenter.removeObserver(this, i2);
            NotificationCenter notificationCenter2 = NotificationCenter.getInstance(this.currentAccount);
            int i3 = NotificationCenter.recordPaused;
            notificationCenter2.removeObserver(this, i3);
            NotificationCenter notificationCenter3 = NotificationCenter.getInstance(this.currentAccount);
            int i4 = NotificationCenter.recordResumed;
            notificationCenter3.removeObserver(this, i4);
            NotificationCenter notificationCenter4 = NotificationCenter.getInstance(this.currentAccount);
            int i5 = NotificationCenter.recordStartError;
            notificationCenter4.removeObserver(this, i5);
            NotificationCenter notificationCenter5 = NotificationCenter.getInstance(this.currentAccount);
            int i6 = NotificationCenter.recordStopped;
            notificationCenter5.removeObserver(this, i6);
            NotificationCenter notificationCenter6 = NotificationCenter.getInstance(this.currentAccount);
            int i7 = NotificationCenter.recordProgressChanged;
            notificationCenter6.removeObserver(this, i7);
            NotificationCenter notificationCenter7 = NotificationCenter.getInstance(this.currentAccount);
            int i8 = NotificationCenter.closeChats;
            notificationCenter7.removeObserver(this, i8);
            NotificationCenter notificationCenter8 = NotificationCenter.getInstance(this.currentAccount);
            int i9 = NotificationCenter.audioDidSent;
            notificationCenter8.removeObserver(this, i9);
            NotificationCenter notificationCenter9 = NotificationCenter.getInstance(this.currentAccount);
            int i10 = NotificationCenter.audioRouteChanged;
            notificationCenter9.removeObserver(this, i10);
            NotificationCenter notificationCenter10 = NotificationCenter.getInstance(this.currentAccount);
            int i11 = NotificationCenter.messagePlayingDidReset;
            notificationCenter10.removeObserver(this, i11);
            NotificationCenter notificationCenter11 = NotificationCenter.getInstance(this.currentAccount);
            int i12 = NotificationCenter.messagePlayingProgressDidChanged;
            notificationCenter11.removeObserver(this, i12);
            NotificationCenter notificationCenter12 = NotificationCenter.getInstance(this.currentAccount);
            int i13 = NotificationCenter.featuredStickersDidLoad;
            notificationCenter12.removeObserver(this, i13);
            NotificationCenter notificationCenter13 = NotificationCenter.getInstance(this.currentAccount);
            int i14 = NotificationCenter.messageReceivedByServer2;
            notificationCenter13.removeObserver(this, i14);
            NotificationCenter notificationCenter14 = NotificationCenter.getInstance(this.currentAccount);
            int i15 = NotificationCenter.sendingMessagesChanged;
            notificationCenter14.removeObserver(this, i15);
            this.currentAccount = i;
            this.accountInstance = AccountInstance.getInstance(i);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i2);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i3);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i4);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i5);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i6);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i7);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i8);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i9);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i10);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i11);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i12);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i13);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i14);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, i15);
        }
        this.sendPlainEnabled = true;
        if (DialogObject.isChatDialog(this.dialog_id)) {
            this.sendPlainEnabled = ChatObject.canSendPlain(this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id)));
        }
        updateScheduleButton(false);
        updateGiftButton(false);
        checkRoundVideo();
        checkChannelRights();
        updateFieldHint(false);
        if (this.messageEditText != null) {
            ChatActivity chatActivity = this.parentFragment;
            updateSendAsButton(chatActivity != null && chatActivity.getFragmentBeginToShow());
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: IfRegionVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r7v1 java.lang.CharSequence, still in use, count: 2, list:
          (r7v1 java.lang.CharSequence) from 0x0098: IF  (r7v1 java.lang.CharSequence) != (null java.lang.CharSequence)  -> B:16:0x0092 A[HIDDEN]
          (r7v1 java.lang.CharSequence) from 0x0092: PHI (r7v9 java.lang.CharSequence) = (r7v1 java.lang.CharSequence), (r7v10 java.lang.CharSequence) binds: [B:27:0x0098, B:15:0x008e] A[DONT_GENERATE, DONT_INLINE]
        	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:162)
        	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:127)
        	at jadx.core.dex.visitors.regions.TernaryMod.makeTernaryInsn(TernaryMod.java:114)
        	at jadx.core.dex.visitors.regions.TernaryMod.processRegion(TernaryMod.java:62)
        	at jadx.core.dex.visitors.regions.TernaryMod.enterRegion(TernaryMod.java:45)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:67)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Collections.java:1092)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:19)
        	at jadx.core.dex.visitors.regions.TernaryMod.process(TernaryMod.java:35)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.process(IfRegionVisitor.java:34)
        	at jadx.core.dex.visitors.regions.IfRegionVisitor.visit(IfRegionVisitor.java:30)
        */
    public void setEditingBusinessLink(org.telegram.tgnet.tl.TL_account.TL_businessChatLink r7) {
        /*
            r6 = this;
            r6.editingBusinessLink = r7
            r0 = 0
            r6.updateFieldHint(r0)
            org.telegram.tgnet.tl.TL_account$TL_businessChatLink r1 = r6.editingBusinessLink
            if (r1 == 0) goto Lcb
            android.animation.AnimatorSet r1 = r6.doneButtonAnimation
            r2 = 0
            if (r1 == 0) goto L14
            r1.cancel()
            r6.doneButtonAnimation = r2
        L14:
            r1 = 1
            r6.createDoneButton(r1)
            org.telegram.ui.Components.ChatActivityEnterView$SendButton r3 = r6.doneButton
            org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda22 r4 = new org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda22
            r4.<init>()
            r3.setOnClickListener(r4)
            org.telegram.ui.Components.ChatActivityEnterView$SendButton r3 = r6.doneButton
            r3.setVisibility(r0)
            org.telegram.ui.Components.ChatActivityEnterView$SendButton r3 = r6.doneButton
            r4 = 1036831949(0x3dcccccd, float:0.1)
            r3.setScaleX(r4)
            org.telegram.ui.Components.ChatActivityEnterView$SendButton r3 = r6.doneButton
            r3.setScaleY(r4)
            org.telegram.ui.Components.ChatActivityEnterView$SendButton r3 = r6.doneButton
            r4 = 0
            r3.setAlpha(r4)
            org.telegram.ui.Components.ChatActivityEnterView$SendButton r3 = r6.doneButton
            android.view.ViewPropertyAnimator r3 = r3.animate()
            r4 = 1065353216(0x3f800000, float:1.0)
            android.view.ViewPropertyAnimator r3 = r3.alpha(r4)
            android.view.ViewPropertyAnimator r3 = r3.scaleX(r4)
            android.view.ViewPropertyAnimator r3 = r3.scaleY(r4)
            r4 = 150(0x96, double:7.4E-322)
            android.view.ViewPropertyAnimator r3 = r3.setDuration(r4)
            org.telegram.ui.Components.CubicBezierInterpolator r4 = org.telegram.ui.Components.CubicBezierInterpolator.DEFAULT
            android.view.ViewPropertyAnimator r3 = r3.setInterpolator(r4)
            r3.start()
            org.telegram.messenger.AccountInstance r3 = r6.accountInstance
            org.telegram.messenger.MessagesController r3 = r3.getMessagesController()
            int r3 = r3.maxMessageLength
            r6.currentLimit = r3
            org.telegram.ui.Components.EditTextCaption r3 = r6.messageEditText
            if (r3 == 0) goto L6f
            android.text.TextPaint r2 = r3.getPaint()
        L6f:
            if (r2 != 0) goto L80
            android.text.TextPaint r2 = new android.text.TextPaint
            r2.<init>()
            r3 = 1099956224(0x41900000, float:18.0)
            int r3 = org.telegram.messenger.AndroidUtilities.dp(r3)
            float r3 = (float) r3
            r2.setTextSize(r3)
        L80:
            android.graphics.Paint$FontMetricsInt r2 = r2.getFontMetricsInt()
            org.telegram.tgnet.tl.TL_account$TL_businessChatLink r3 = r6.editingBusinessLink
            java.util.ArrayList<org.telegram.tgnet.TLRPC$MessageEntity> r3 = r3.entities
            if (r3 == 0) goto L96
            java.lang.String r4 = r7.message
            if (r4 == 0) goto L96
            java.lang.CharSequence r7 = applyMessageEntities(r3, r4, r2)
        L92:
            r6.setFieldText(r7)
            goto L9b
        L96:
            java.lang.String r7 = r7.message
            if (r7 == 0) goto L9b
            goto L92
        L9b:
            org.telegram.ui.Components.ChatActivityEnterView$BusinessLinkPresetMessage r7 = r6.calculateBusinessLinkPresetMessage()
            r6.lastSavedBusinessLinkMessage = r7
            r6.setAllowStickersAndGifs(r1, r0, r0)
            org.telegram.ui.Components.ChatActivityEnterView$SendButton r7 = r6.sendButton
            r1 = 8
            r7.setVisibility(r1)
            r6.setSlowModeButtonVisible(r0)
            android.widget.ImageView r7 = r6.cancelBotButton
            r7.setVisibility(r1)
            android.widget.FrameLayout r7 = r6.audioVideoButtonContainer
            r7.setVisibility(r1)
            android.widget.LinearLayout r7 = r6.attachLayout
            if (r7 == 0) goto Lbf
            r7.setVisibility(r1)
        Lbf:
            android.widget.FrameLayout r7 = r6.sendButtonContainer
            r7.setVisibility(r1)
            android.widget.ImageView r7 = r6.scheduledButton
            if (r7 == 0) goto Lcb
            r7.setVisibility(r1)
        Lcb:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.ChatActivityEnterView.setEditingBusinessLink(org.telegram.tgnet.tl.TL_account$TL_businessChatLink):void");
    }

    public void setEditingMessageObject(final MessageObject messageObject, final MessageObject.GroupedMessages groupedMessages, boolean z) {
        CharSequence charSequence;
        final CharSequence charSequence2;
        int i;
        if (this.audioToSend == null && this.videoToSendMessageObject == null && this.editingMessageObject != messageObject) {
            createMessageEditText();
            boolean z2 = this.editingMessageObject != null;
            this.editingMessageObject = messageObject;
            this.editingCaption = z;
            if (messageObject != null) {
                this.captionAbove = groupedMessages != null ? groupedMessages.captionAbove : messageObject.messageOwner.invert_media;
                AnimatorSet animatorSet = this.doneButtonAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.doneButtonAnimation = null;
                }
                createDoneButton(false);
                this.doneButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda69
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ChatActivityEnterView.this.lambda$setEditingMessageObject$54(view);
                    }
                });
                this.doneButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda70
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view) {
                        boolean lambda$setEditingMessageObject$57;
                        lambda$setEditingMessageObject$57 = ChatActivityEnterView.this.lambda$setEditingMessageObject$57(messageObject, groupedMessages, view);
                        return lambda$setEditingMessageObject$57;
                    }
                });
                this.doneButton.setVisibility(0);
                this.doneButton.setScaleX(0.1f);
                this.doneButton.setScaleY(0.1f);
                this.doneButton.setAlpha(0.0f);
                this.doneButton.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                MessagesController messagesController = this.accountInstance.getMessagesController();
                if (z) {
                    this.currentLimit = messagesController.maxCaptionLength;
                    charSequence = this.editingMessageObject.caption;
                } else {
                    this.currentLimit = messagesController.maxMessageLength;
                    charSequence = this.editingMessageObject.messageText;
                }
                if (charSequence != null) {
                    EditTextCaption editTextCaption = this.messageEditText;
                    TextPaint paint = editTextCaption != null ? editTextCaption.getPaint() : null;
                    if (paint == null) {
                        paint = new TextPaint();
                        paint.setTextSize(AndroidUtilities.dp(18.0f));
                    }
                    charSequence2 = applyMessageEntities(this.editingMessageObject.messageOwner.entities, charSequence, paint.getFontMetricsInt());
                } else {
                    charSequence2 = "";
                }
                if (this.draftMessage == null && !z2) {
                    EditTextCaption editTextCaption2 = this.messageEditText;
                    this.draftMessage = (editTextCaption2 == null || editTextCaption2.length() <= 0) ? null : this.messageEditText.getText();
                    this.draftSearchWebpage = this.messageWebPageSearch;
                }
                MessageObject messageObject2 = this.editingMessageObject;
                TLRPC.MessageMedia messageMedia = messageObject2.messageOwner.media;
                this.messageWebPageSearch = !((messageMedia instanceof TLRPC.TL_messageMediaWebPage) && messageMedia.manual) && ((i = messageObject2.type) == 0 || i == 19);
                if (this.keyboardVisible) {
                    Runnable runnable = this.setTextFieldRunnable;
                    if (runnable != null) {
                        AndroidUtilities.cancelRunOnUIThread(runnable);
                        this.setTextFieldRunnable = null;
                    }
                    setFieldText(charSequence2);
                } else {
                    Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda71
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatActivityEnterView.this.lambda$setEditingMessageObject$58(charSequence2);
                        }
                    };
                    this.setTextFieldRunnable = runnable2;
                    AndroidUtilities.runOnUIThread(runnable2, 200L);
                }
                EditTextCaption editTextCaption3 = this.messageEditText;
                if (editTextCaption3 != null) {
                    editTextCaption3.requestFocus();
                }
                openKeyboard();
                EditTextCaption editTextCaption4 = this.messageEditText;
                if (editTextCaption4 != null) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) editTextCaption4.getLayoutParams();
                    layoutParams.rightMargin = AndroidUtilities.dp(4.0f);
                    this.messageEditText.setLayoutParams(layoutParams);
                }
                this.sendButton.setVisibility(8);
                setSlowModeButtonVisible(false);
                this.cancelBotButton.setVisibility(8);
                this.audioVideoButtonContainer.setVisibility(8);
                this.attachLayout.setVisibility(8);
                this.sendButtonContainer.setVisibility(8);
                ImageView imageView = this.scheduledButton;
                if (imageView != null) {
                    imageView.setVisibility(8);
                }
            } else {
                Runnable runnable3 = this.setTextFieldRunnable;
                if (runnable3 != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable3);
                    this.setTextFieldRunnable = null;
                }
                SendButton sendButton = this.doneButton;
                if (sendButton != null) {
                    sendButton.setVisibility(8);
                }
                this.currentLimit = -1;
                this.delegate.onMessageEditEnd(false);
                this.sendButtonContainer.setVisibility(0);
                this.cancelBotButton.setScaleX(0.1f);
                this.cancelBotButton.setScaleY(0.1f);
                this.cancelBotButton.setAlpha(0.0f);
                this.cancelBotButton.setVisibility(8);
                if (this.slowModeTimer <= 0 || isInScheduleMode()) {
                    this.sendButton.setScaleX(0.1f);
                    this.sendButton.setScaleY(0.1f);
                    this.sendButton.setAlpha(0.0f);
                    this.sendButton.setVisibility(8);
                    this.slowModeButton.setScaleX(0.1f);
                    this.slowModeButton.setScaleY(0.1f);
                    this.slowModeButton.setAlpha(0.0f);
                    setSlowModeButtonVisible(false);
                    this.attachLayout.setScaleX(1.0f);
                    this.attachLayoutAlpha = 1.0f;
                    updateAttachLayoutParams();
                    this.attachLayout.setVisibility(0);
                    this.audioVideoButtonContainer.setScaleX(1.0f);
                    this.audioVideoButtonContainer.setScaleY(1.0f);
                    this.audioVideoButtonContainer.setAlpha(1.0f);
                    this.audioVideoButtonContainer.setVisibility(0);
                } else {
                    if (this.slowModeTimer == Integer.MAX_VALUE) {
                        this.sendButton.setScaleX(1.0f);
                        this.sendButton.setScaleY(1.0f);
                        this.sendButton.setAlpha(1.0f);
                        this.sendButton.setVisibility(0);
                        this.slowModeButton.setScaleX(0.1f);
                        this.slowModeButton.setScaleY(0.1f);
                        this.slowModeButton.setAlpha(0.0f);
                        setSlowModeButtonVisible(false);
                    } else {
                        this.sendButton.setScaleX(0.1f);
                        this.sendButton.setScaleY(0.1f);
                        this.sendButton.setAlpha(0.0f);
                        this.sendButton.setVisibility(8);
                        this.slowModeButton.setScaleX(1.0f);
                        this.slowModeButton.setScaleY(1.0f);
                        this.slowModeButton.setAlpha(1.0f);
                        setSlowModeButtonVisible(true);
                    }
                    this.attachLayout.setScaleX(0.01f);
                    this.attachLayoutAlpha = 0.0f;
                    updateAttachLayoutParams();
                    this.attachLayout.setVisibility(8);
                    this.audioVideoButtonContainer.setScaleX(0.1f);
                    this.audioVideoButtonContainer.setScaleY(0.1f);
                    this.audioVideoButtonContainer.setAlpha(0.0f);
                    this.audioVideoButtonContainer.setVisibility(8);
                }
                createScheduledButton();
                ImageView imageView2 = this.scheduledButton;
                if (imageView2 != null && imageView2.getTag() != null) {
                    this.scheduledButton.setScaleX(1.0f);
                    this.scheduledButton.setScaleY(1.0f);
                    this.scheduledButton.setAlpha(1.0f);
                    this.scheduledButton.setVisibility(0);
                }
                ChatActivity chatActivity = this.parentFragment;
                if (chatActivity != null) {
                    chatActivity.editingMessageObject = null;
                    chatActivity.foundWebPage = null;
                    MessagePreviewParams messagePreviewParams = chatActivity.messagePreviewParams;
                    if (messagePreviewParams != null) {
                        messagePreviewParams.updateLink(this.currentAccount, null, "", null, null, null);
                    }
                    setWebPage(null, true);
                    this.parentFragment.fallbackFieldPanel();
                }
                createMessageEditText();
                EditTextCaption editTextCaption5 = this.messageEditText;
                if (editTextCaption5 != null) {
                    editTextCaption5.setText(this.draftMessage);
                    EditTextCaption editTextCaption6 = this.messageEditText;
                    editTextCaption6.setSelection(editTextCaption6.length());
                }
                this.draftMessage = null;
                this.messageWebPageSearch = this.draftSearchWebpage;
                if (getVisibility() == 0) {
                    this.delegate.onAttachButtonShow();
                }
                updateFieldRight(1);
            }
            updateFieldHint(true);
            updateSendAsButton(true);
        }
    }

    public void setEffectId(long j) {
        this.effectId = j;
        SendButton sendButton = this.sendButton;
        if (sendButton != null) {
            sendButton.setEffect(j);
        }
    }

    public void setExitTransition(float f) {
        this.exitTransition = f;
        RecordCircle recordCircle = this.recordCircle;
        if (recordCircle != null) {
            recordCircle.invalidate();
        }
    }

    public void setFieldFocused() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.parentActivity.getSystemService("accessibility");
        if (this.messageEditText == null || accessibilityManager.isTouchExplorationEnabled()) {
            return;
        }
        try {
            this.messageEditText.requestFocus();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public void setFieldFocused(boolean z) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.parentActivity.getSystemService("accessibility");
        if (this.messageEditText == null || accessibilityManager.isTouchExplorationEnabled()) {
            return;
        }
        if (z && BaseFragment.hasSheets(this.parentFragment)) {
            z = false;
        }
        if (z) {
            if (this.searchingType != 0 || this.messageEditText.isFocused()) {
                return;
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$setFieldFocused$59();
                }
            };
            this.focusRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 600L);
            return;
        }
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null || !editTextCaption.isFocused()) {
            return;
        }
        if (!this.keyboardVisible || this.isPaused) {
            this.messageEditText.clearFocus();
        }
    }

    @Override // org.telegram.ui.Components.SuggestEmojiView.AnchorViewDelegate
    public void setFieldText(CharSequence charSequence) {
        setFieldText(charSequence, true, false);
    }

    public void setFieldText(CharSequence charSequence, boolean z) {
        setFieldText(charSequence, z, false);
    }

    public void setFieldText(CharSequence charSequence, boolean z, boolean z2) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate;
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return;
        }
        this.ignoreTextChange = z;
        editTextCaption.setText(charSequence);
        this.messageEditText.invalidateQuotes(true);
        EditTextCaption editTextCaption2 = this.messageEditText;
        editTextCaption2.setSelection(editTextCaption2.getText().length());
        this.ignoreTextChange = false;
        if (!z || (chatActivityEnterViewDelegate = this.delegate) == null) {
            return;
        }
        chatActivityEnterViewDelegate.onTextChanged(this.messageEditText.getText(), true, z2);
    }

    public void setForceShowSendButton(boolean z, boolean z2) {
        this.forceShowSendButton = z;
        checkSendButton(z2);
    }

    public void setHorizontalPadding(float f, float f2, boolean z) {
        SeekBarWaveformView seekBarWaveformView;
        float f3 = 1.0f - f2;
        float f4 = (-f) * f3;
        float f5 = (-(AndroidUtilities.dp(40.0f) + f)) * f3;
        this.emojiButtonPaddingScale = (f2 * 0.5f) + 0.5f;
        this.emojiButtonPaddingAlpha = f2;
        updateEmojiButtonParams();
        float f6 = -f4;
        this.emojiButton.setTranslationX(f6);
        this.messageTextPaddingTranslationX = f6 - (AndroidUtilities.dp(31.0f) * f3);
        RLottieImageView rLottieImageView = this.recordDeleteImageView;
        if (rLottieImageView != null) {
            rLottieImageView.setTranslationX(f6);
        }
        RecordCircle recordCircle = this.recordCircle;
        if (recordCircle != null) {
            recordCircle.setTranslationX(f5);
        }
        ControlsView controlsView = this.controlsView;
        if (controlsView != null) {
            controlsView.setTranslationX(f5);
        }
        LinearLayout linearLayout = this.recordTimeContainer;
        if (linearLayout != null) {
            linearLayout.setTranslationX(f6);
        }
        ImageView imageView = this.recordedAudioPlayButton;
        if (imageView != null) {
            imageView.setTranslationX(f6);
        }
        TextView textView = this.recordedAudioTimeTextView;
        if (textView != null) {
            textView.setTranslationX(f4);
        }
        this.sendButtonContainer.setTranslationX(f5);
        this.sendButtonContainer.setAlpha(z ? f2 : 1.0f);
        this.sendButtonEnabled = !z || f2 == 1.0f;
        this.attachLayoutPaddingTranslationX = f5;
        this.attachLayoutPaddingAlpha = f2;
        updateAttachLayoutParams();
        updateMessageTextParams();
        float f7 = f * f3;
        if (this.horizontalPadding != f7) {
            this.horizontalPadding = f7;
            if (this.seekBarWaveform != null && (seekBarWaveformView = this.recordedAudioSeekBar) != null) {
                seekBarWaveformView.setTranslationX(f7);
                this.recordedAudioSeekBar.invalidate();
                this.seekBarWaveform.setSize((int) (this.recordedAudioSeekBar.getMeasuredWidth() - (this.horizontalPadding * 2.0f)), this.recordedAudioSeekBar.getMeasuredHeight());
            }
            View view = this.recordedAudioBackground;
            if (view != null) {
                view.invalidate();
            }
        }
        if (this.messageEditText != null) {
            float lerp = AndroidUtilities.lerp(0.88f, 1.0f, f2);
            this.messageEditText.setPivotX(0.0f);
            this.messageEditText.setPivotY(r9.getMeasuredHeight() / 2.0f);
            this.messageEditText.setScaleX(lerp);
            this.messageEditText.setScaleY(lerp);
            this.messageEditText.setHintRightOffset(AndroidUtilities.lerp(AndroidUtilities.dp(30.0f), 0, f2));
        }
    }

    public void setLockAnimatedTranslation(float f) {
        this.lockAnimatedTranslation = f;
        RecordCircle recordCircle = this.recordCircle;
        if (recordCircle != null) {
            recordCircle.invalidate();
        }
    }

    public void setOpenGifsTabFirst() {
        createEmojiView();
        MediaDataController.getInstance(this.currentAccount).loadRecents(0, true, true, false);
        this.emojiView.switchToGifRecent();
    }

    public void setOverrideHint(CharSequence charSequence) {
        setOverrideHint(charSequence, false);
    }

    public void setOverrideHint(CharSequence charSequence, CharSequence charSequence2, boolean z) {
        this.overrideHint = charSequence;
        this.overrideHint2 = charSequence2;
        updateFieldHint(z);
    }

    public void setOverrideHint(CharSequence charSequence, boolean z) {
        this.overrideHint = charSequence;
        this.overrideHint2 = null;
        updateFieldHint(z);
    }

    public void setOverrideKeyboardAnimation(boolean z) {
        this.overrideKeyboardAnimation = z;
    }

    public void setReplyingMessageObject(MessageObject messageObject, ChatActivity.ReplyQuote replyQuote) {
        setReplyingMessageObject(messageObject, replyQuote, null);
    }

    public void setReplyingMessageObject(MessageObject messageObject, ChatActivity.ReplyQuote replyQuote, MessageObject messageObject2) {
        MessageObject messageObject3;
        ChatActivity chatActivity = this.parentFragment;
        boolean z = (chatActivity == null || !chatActivity.isForumInViewAsMessagesMode() || this.replyingTopMessage == messageObject2) ? false : true;
        if (messageObject != null) {
            if (this.botMessageObject == null && (messageObject3 = this.botButtonsMessageObject) != this.replyingMessageObject) {
                this.botMessageObject = messageObject3;
            }
            this.replyingMessageObject = messageObject;
            this.replyingQuote = replyQuote;
            this.replyingTopMessage = messageObject2;
            ChatActivity chatActivity2 = this.parentFragment;
            if (chatActivity2 == null || !chatActivity2.isTopic || chatActivity2.getThreadMessage() != this.replyingMessageObject) {
                setButtons(this.replyingMessageObject, true);
            }
        } else {
            MessageObject messageObject4 = this.replyingMessageObject;
            MessageObject messageObject5 = this.botButtonsMessageObject;
            this.replyingMessageObject = null;
            if (messageObject4 == messageObject5) {
                this.replyingTopMessage = null;
                this.replyingQuote = null;
                setButtons(this.botMessageObject, false);
                this.botMessageObject = null;
            } else {
                this.replyingQuote = null;
                this.replyingTopMessage = null;
            }
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        MediaController.getInstance().setReplyingMessage(messageObject, getThreadMessage(), chatActivityEnterViewDelegate != null ? chatActivityEnterViewDelegate.getReplyToStory() : null);
        updateFieldHint(z);
    }

    public void setSelection(int i) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return;
        }
        editTextCaption.setSelection(i, editTextCaption.length());
    }

    public void setSlideToCancelProgress(float f) {
        this.slideToCancelProgress = f;
        float measuredWidth = getMeasuredWidth() * 0.35f;
        if (measuredWidth > AndroidUtilities.dp(140.0f)) {
            measuredWidth = AndroidUtilities.dp(140.0f);
        }
        this.slideDelta = (int) ((-measuredWidth) * (1.0f - this.slideToCancelProgress));
        RecordCircle recordCircle = this.recordCircle;
        if (recordCircle != null) {
            recordCircle.invalidate();
        }
    }

    public void setSlowModeTimer(int i) {
        this.slowModeTimer = i;
        updateSlowModeText();
    }

    public void setSnapAnimationProgress(float f) {
        this.snapAnimationProgress = f;
        invalidate();
    }

    public void setStickersExpanded(boolean z, boolean z2, boolean z3) {
        setStickersExpanded(z, z2, z3, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:45:0x0210  */
    /* JADX WARN: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setStickersExpanded(boolean z, boolean z2, boolean z3, boolean z4) {
        AnimatorSet animatorSet;
        ImageView imageView;
        int i;
        String str;
        AdjustPanLayoutHelper adjustPanLayoutHelper = this.adjustPanLayoutHelper;
        if ((adjustPanLayoutHelper != null && adjustPanLayoutHelper.animationInProgress()) || this.waitingForKeyboardOpenAfterAnimation || this.emojiView == null) {
            return;
        }
        if (!z3 && this.stickersExpanded == z) {
            return;
        }
        this.stickersExpanded = z;
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onStickersExpandedChange();
        }
        android.graphics.Point point = AndroidUtilities.displaySize;
        final int i2 = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
        Animator animator = this.stickersExpansionAnim;
        if (animator != null) {
            animator.cancel();
            this.stickersExpansionAnim = null;
        }
        if (this.stickersExpanded) {
            if (z4) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.stopAllHeavyOperations, 1);
            }
            int height = this.sizeNotifierLayout.getHeight();
            this.originalViewHeight = height;
            int currentActionBarHeight = (((height - (Build.VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)) - ActionBar.getCurrentActionBarHeight()) - getHeight()) + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
            this.stickersExpandedHeight = currentActionBarHeight;
            if (this.searchingType == 2) {
                this.stickersExpandedHeight = Math.min(currentActionBarHeight, AndroidUtilities.dp(120.0f) + i2);
            }
            this.emojiView.getLayoutParams().height = this.stickersExpandedHeight;
            this.sizeNotifierLayout.requestLayout();
            if (this.shouldDrawBackground) {
                this.sizeNotifierLayout.setForeground(new ScrimDrawable());
            }
            EditTextCaption editTextCaption = this.messageEditText;
            if (editTextCaption != null) {
                int selectionStart = editTextCaption.getSelectionStart();
                int selectionEnd = this.messageEditText.getSelectionEnd();
                EditTextCaption editTextCaption2 = this.messageEditText;
                editTextCaption2.setText(editTextCaption2.getText());
                this.messageEditText.setSelection(selectionStart, selectionEnd);
            }
            if (z2) {
                animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofInt(this, (Property<ChatActivityEnterView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i2)), ObjectAnimator.ofInt(this.emojiView, (Property<EmojiView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i2)), ObjectAnimator.ofFloat(this.stickersArrow, "animationProgress", 1.0f));
                animatorSet.setDuration(300L);
                animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                ((ObjectAnimator) animatorSet.getChildAnimations().get(0)).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda12
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatActivityEnterView.this.lambda$setStickersExpanded$76(i2, valueAnimator);
                    }
                });
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.81
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator2) {
                        ChatActivityEnterView.this.stickersExpansionAnim = null;
                        ChatActivityEnterView.this.emojiView.setLayerType(0, null);
                        ChatActivityEnterView.this.notificationsLocker.unlock();
                    }
                });
                this.stickersExpansionAnim = animatorSet;
                this.emojiView.setLayerType(2, null);
                this.notificationsLocker.lock();
                this.stickersExpansionProgress = 0.0f;
                this.sizeNotifierLayout.invalidate();
                animatorSet.start();
                imageView = this.expandStickersButton;
                if (imageView == null) {
                    if (this.stickersExpanded) {
                        i = R.string.AccDescrCollapsePanel;
                        str = "AccDescrCollapsePanel";
                    } else {
                        i = R.string.AccDescrExpandPanel;
                        str = "AccDescrExpandPanel";
                    }
                    imageView.setContentDescription(LocaleController.getString(str, i));
                    return;
                }
                return;
            }
            this.stickersExpansionProgress = 1.0f;
            setTranslationY(-(this.stickersExpandedHeight - i2));
            this.emojiView.setTranslationY(-(this.stickersExpandedHeight - i2));
            AnimatedArrowDrawable animatedArrowDrawable = this.stickersArrow;
            if (animatedArrowDrawable != null) {
                animatedArrowDrawable.setAnimationProgress(1.0f);
            }
            imageView = this.expandStickersButton;
            if (imageView == null) {
            }
        } else {
            if (z4) {
                NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 1);
            }
            if (z2) {
                this.closeAnimationInProgress = true;
                animatorSet = new AnimatorSet();
                animatorSet.playTogether(ObjectAnimator.ofInt(this, (Property<ChatActivityEnterView, Integer>) this.roundedTranslationYProperty, 0), ObjectAnimator.ofInt(this.emojiView, (Property<EmojiView, Integer>) this.roundedTranslationYProperty, 0), ObjectAnimator.ofFloat(this.stickersArrow, "animationProgress", 0.0f));
                animatorSet.setDuration(300L);
                animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                ((ObjectAnimator) animatorSet.getChildAnimations().get(0)).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda13
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatActivityEnterView.this.lambda$setStickersExpanded$77(i2, valueAnimator);
                    }
                });
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.82
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator2) {
                        ChatActivityEnterView.this.closeAnimationInProgress = false;
                        ChatActivityEnterView.this.stickersExpansionAnim = null;
                        if (ChatActivityEnterView.this.emojiView != null) {
                            ChatActivityEnterView.this.emojiView.getLayoutParams().height = i2;
                            ChatActivityEnterView.this.emojiView.setLayerType(0, null);
                        }
                        if (ChatActivityEnterView.this.sizeNotifierLayout != null) {
                            ChatActivityEnterView.this.sizeNotifierLayout.requestLayout();
                            ChatActivityEnterView.this.sizeNotifierLayout.setForeground(null);
                            ChatActivityEnterView.this.sizeNotifierLayout.setWillNotDraw(false);
                        }
                        if (ChatActivityEnterView.this.keyboardVisible && ChatActivityEnterView.this.isPopupShowing()) {
                            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                            chatActivityEnterView.showPopup(0, chatActivityEnterView.currentPopupContentType);
                        }
                        if (ChatActivityEnterView.this.onEmojiSearchClosed != null) {
                            ChatActivityEnterView.this.onEmojiSearchClosed.run();
                            ChatActivityEnterView.this.onEmojiSearchClosed = null;
                        }
                        ChatActivityEnterView.this.notificationsLocker.unlock();
                    }
                });
                this.stickersExpansionProgress = 1.0f;
                this.sizeNotifierLayout.invalidate();
                this.stickersExpansionAnim = animatorSet;
                this.emojiView.setLayerType(2, null);
                this.notificationsLocker.lock();
                animatorSet.start();
                imageView = this.expandStickersButton;
                if (imageView == null) {
                }
            } else {
                this.stickersExpansionProgress = 0.0f;
                setTranslationY(0.0f);
                this.emojiView.setTranslationY(0.0f);
                this.emojiView.getLayoutParams().height = i2;
                this.sizeNotifierLayout.requestLayout();
                this.sizeNotifierLayout.setForeground(null);
                this.sizeNotifierLayout.setWillNotDraw(false);
                AnimatedArrowDrawable animatedArrowDrawable2 = this.stickersArrow;
                if (animatedArrowDrawable2 != null) {
                    animatedArrowDrawable2.setAnimationProgress(0.0f);
                }
                imageView = this.expandStickersButton;
                if (imageView == null) {
                }
            }
        }
    }

    public void setTextTransitionIsRunning(boolean z) {
        this.textTransitionIsRunning = z;
        this.sendButtonContainer.invalidate();
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        boolean z = i == 0;
        this.messageEditTextEnabled = z;
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.setEnabled(z);
        }
    }

    public void setVoiceDraft(MediaDataController.DraftVoice draftVoice) {
        if (draftVoice == null) {
            return;
        }
        this.voiceOnce = draftVoice.once;
        ControlsView controlsView = this.controlsView;
        if (controlsView != null) {
            controlsView.periodDrawable.setValue(1, this.voiceOnce, true);
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        TL_stories.StoryItem replyToStory = chatActivityEnterViewDelegate != null ? chatActivityEnterViewDelegate.getReplyToStory() : null;
        MediaController mediaController = MediaController.getInstance();
        int i = this.currentAccount;
        long j = this.dialog_id;
        MessageObject messageObject = this.replyingMessageObject;
        MessageObject threadMessage = getThreadMessage();
        int i2 = this.recordingGuid;
        ChatActivity chatActivity = this.parentFragment;
        mediaController.prepareResumedRecording(i, draftVoice, j, messageObject, threadMessage, replyToStory, i2, chatActivity != null ? chatActivity.quickReplyShortcut : null, chatActivity != null ? chatActivity.getQuickReplyId() : 0);
    }

    public void setWebPage(TLRPC.WebPage webPage, boolean z) {
        this.messageWebPage = webPage;
        this.messageWebPageSearch = z;
    }

    protected boolean showConfirmAlert(Runnable runnable) {
        return false;
    }

    public void showContextProgress(boolean z) {
        CloseProgressDrawable2 closeProgressDrawable2 = this.progressDrawable;
        if (closeProgressDrawable2 == null) {
            return;
        }
        if (z) {
            closeProgressDrawable2.startAnimation();
        } else {
            closeProgressDrawable2.stopAnimation();
        }
    }

    public void showEditDoneProgress(boolean z, boolean z2) {
        if (this.doneButton == null) {
            return;
        }
        AnimatorSet animatorSet = this.doneButtonAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (z) {
            this.doneButton.setEnabled(false);
            this.doneButton.setLoading(true, -3.0f);
        } else {
            this.doneButton.setEnabled(true);
            this.doneButton.setLoading(false, -3.0f);
        }
    }

    public void showEmojiView() {
        showPopup(1, 0);
    }

    public void showTopView(boolean z, boolean z2) {
        showTopView(z, z2, false);
    }

    public void startMessageTransition() {
        Runnable runnable = this.moveToSendStateRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.messageTransitionIsRunning = true;
            this.moveToSendStateRunnable.run();
            this.moveToSendStateRunnable = null;
        }
    }

    public boolean swipeToBackEnabled() {
        FrameLayout frameLayout;
        if (this.recordingAudioVideo) {
            return false;
        }
        if (isInVideoMode() && (frameLayout = this.recordedAudioPanel) != null && frameLayout.getVisibility() == 0) {
            return false;
        }
        return ((hasBotWebView() && this.botCommandsMenuButton.isOpened()) || BaseFragment.hasSheets(this.parentFragment)) ? false : true;
    }

    public float topViewVisible() {
        return this.topViewEnterProgress;
    }

    public void updateBotWebView(boolean z) {
        if (this.botMenuButtonType != BotMenuButtonType.NO_BUTTON && this.dialog_id > 0) {
            createBotCommandsMenuButton();
        }
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        if (botCommandsMenuView != null) {
            botCommandsMenuView.setWebView(hasBotWebView());
        }
        updateBotButton(z);
    }

    public void updateColors() {
        MessageSendPreview messageSendPreview = this.messageSendPreview;
        if (messageSendPreview != null) {
            messageSendPreview.updateColors();
        }
        updateRecordedDeleteIconColors();
        RecordCircle recordCircle = this.recordCircle;
        if (recordCircle != null) {
            recordCircle.updateColors();
        }
        RecordDot recordDot = this.recordDot;
        if (recordDot != null) {
            recordDot.updateColors();
        }
        SlideTextView slideTextView = this.slideText;
        if (slideTextView != null) {
            slideTextView.updateColors();
        }
        TimerView timerView = this.recordTimerView;
        if (timerView != null) {
            timerView.updateColors();
        }
        VideoTimelineView videoTimelineView = this.videoTimelineView;
        if (videoTimelineView != null) {
            videoTimelineView.updateColors();
        }
        NumberTextView numberTextView = this.captionLimitView;
        if (numberTextView != null && this.messageEditText != null) {
            numberTextView.setTextColor(getThemedColor(this.codePointCount - this.currentLimit < 0 ? Theme.key_text_RedRegular : Theme.key_windowBackgroundWhiteGrayText));
        }
        int themedColor = getThemedColor(Theme.key_chat_messagePanelVoicePressed);
        int alpha = Color.alpha(themedColor);
        Drawable drawable = this.doneCheckDrawable;
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.setAlphaComponent(themedColor, (int) (alpha * ((this.doneButtonEnabledProgress * 0.42f) + 0.58f))), PorterDuff.Mode.MULTIPLY));
        }
        BotCommandsMenuContainer botCommandsMenuContainer = this.botCommandsMenuContainer;
        if (botCommandsMenuContainer != null) {
            botCommandsMenuContainer.updateColors();
        }
        BotKeyboardView botKeyboardView = this.botKeyboardView;
        if (botKeyboardView != null) {
            botKeyboardView.updateColors();
        }
        ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView = this.audioVideoSendButton;
        int i = Theme.key_chat_messagePanelIcons;
        int themedColor2 = getThemedColor(i);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
        chatActivityEnterViewAnimatedIconView.setColorFilter(new PorterDuffColorFilter(themedColor2, mode));
        this.emojiButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(i), mode));
        if (Build.VERSION.SDK_INT >= 21) {
            this.emojiButton.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
    }

    public void updateFieldHint(boolean z) {
        boolean z2;
        EditTextCaption editTextCaption;
        EditTextCaption editTextCaption2;
        int i;
        String str;
        String string;
        int i2;
        String str2;
        String str3;
        EditTextCaption editTextCaption3;
        String formatString;
        TLRPC.TL_forumTopic tL_forumTopic;
        String str4;
        MessageObject messageObject;
        TLRPC.ReplyMarkup replyMarkup;
        MessageObject messageObject2;
        String string2;
        TLRPC.ReplyMarkup replyMarkup2;
        int i3;
        boolean z3 = false;
        EditTextCaption editTextCaption4 = this.messageEditText;
        if (editTextCaption4 == null) {
            return;
        }
        CharSequence charSequence = this.overrideHint;
        if (charSequence != null) {
            editTextCaption4.setHintText(charSequence, z);
            this.messageEditText.setHintText2(this.overrideHint2, z);
            return;
        }
        if (!this.sendPlainEnabled && !isEditingMessage()) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(" d " + LocaleController.getString("PlainTextRestrictedHint", R.string.PlainTextRestrictedHint));
            spannableStringBuilder.setSpan(new ColoredImageSpan(R.drawable.msg_mini_lock3), 1, 2, 0);
            this.messageEditText.setHintText(spannableStringBuilder, z);
            this.messageEditText.setText((CharSequence) null);
            this.messageEditText.setEnabled(false);
            this.messageEditText.setInputType(1);
            return;
        }
        this.messageEditText.setEnabled(true);
        int inputType = this.messageEditText.getInputType();
        int i4 = this.commonInputType;
        if (inputType != i4) {
            this.messageEditText.setInputType(i4);
        }
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || chatActivity.getChatMode() != 5) {
            if (!isEditingBusinessLink()) {
                MessageObject messageObject3 = this.replyingMessageObject;
                if (messageObject3 == null || (replyMarkup2 = messageObject3.messageOwner.reply_markup) == null || TextUtils.isEmpty(replyMarkup2.placeholder)) {
                    if (this.editingMessageObject != null) {
                        editTextCaption = this.messageEditText;
                        if (this.editingCaption) {
                            i2 = R.string.Caption;
                            str2 = "Caption";
                            string2 = LocaleController.getString(str2, i2);
                        }
                        string2 = LocaleController.getString("TypeMessage", R.string.TypeMessage);
                    } else {
                        if (!this.botKeyboardViewVisible || (messageObject = this.botButtonsMessageObject) == null || (replyMarkup = messageObject.messageOwner.reply_markup) == null || TextUtils.isEmpty(replyMarkup.placeholder)) {
                            ChatActivity chatActivity2 = this.parentFragment;
                            if (chatActivity2 != null && chatActivity2.isForumInViewAsMessagesMode()) {
                                MessageObject messageObject4 = this.replyingTopMessage;
                                if (messageObject4 == null || (tL_forumTopic = messageObject4.replyToForumTopic) == null || (str4 = tL_forumTopic.title) == null) {
                                    TLRPC.TL_forumTopic findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(this.parentFragment.getCurrentChat().id, 1L);
                                    if (findTopic == null || (str3 = findTopic.title) == null) {
                                        editTextCaption2 = this.messageEditText;
                                        string = LocaleController.getString("TypeMessage", R.string.TypeMessage);
                                    } else {
                                        editTextCaption3 = this.messageEditText;
                                        formatString = LocaleController.formatString("TypeMessageIn", R.string.TypeMessageIn, str3);
                                    }
                                } else {
                                    editTextCaption3 = this.messageEditText;
                                    formatString = LocaleController.formatString("TypeMessageIn", R.string.TypeMessageIn, str4);
                                }
                                editTextCaption3.setHintText(formatString, z);
                                return;
                            }
                            if (DialogObject.isChatDialog(this.dialog_id)) {
                                TLRPC.Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
                                TLRPC.ChatFull chatFull = this.accountInstance.getMessagesController().getChatFull(-this.dialog_id);
                                z2 = ChatObject.isChannelAndNotMegaGroup(chat);
                                if (!z2 && ChatObject.getSendAsPeerId(chat, chatFull) == (-this.dialog_id)) {
                                    z3 = true;
                                }
                            } else {
                                z2 = false;
                            }
                            if (z3) {
                                editTextCaption = this.messageEditText;
                                i2 = R.string.SendAnonymously;
                                str2 = "SendAnonymously";
                            } else {
                                ChatActivity chatActivity3 = this.parentFragment;
                                if (chatActivity3 != null && chatActivity3.isThreadChat()) {
                                    ChatActivity chatActivity4 = this.parentFragment;
                                    if (!chatActivity4.isTopic) {
                                        if (chatActivity4.isReplyChatComment()) {
                                            editTextCaption = this.messageEditText;
                                            i2 = R.string.Comment;
                                            str2 = "Comment";
                                        } else {
                                            editTextCaption = this.messageEditText;
                                            i2 = R.string.Reply;
                                            str2 = "Reply";
                                        }
                                    }
                                }
                                if (z2) {
                                    if (this.silent) {
                                        editTextCaption2 = this.messageEditText;
                                        i = R.string.ChannelSilentBroadcast;
                                        str = "ChannelSilentBroadcast";
                                    } else {
                                        editTextCaption2 = this.messageEditText;
                                        i = R.string.ChannelBroadcast;
                                        str = "ChannelBroadcast";
                                    }
                                    string = LocaleController.getString(str, i);
                                } else {
                                    editTextCaption = this.messageEditText;
                                    string2 = LocaleController.getString("TypeMessage", R.string.TypeMessage);
                                }
                            }
                            string2 = LocaleController.getString(str2, i2);
                            editTextCaption2.setHintText(string, z);
                            return;
                        }
                        editTextCaption2 = this.messageEditText;
                        messageObject2 = this.botButtonsMessageObject;
                    }
                    editTextCaption.setHintText(string2);
                }
                editTextCaption2 = this.messageEditText;
                messageObject2 = this.replyingMessageObject;
                string = messageObject2.messageOwner.reply_markup.placeholder;
                editTextCaption2.setHintText(string, z);
                return;
            }
            editTextCaption = this.messageEditText;
            i3 = R.string.BusinessLinksEnter;
        } else if ("hello".equalsIgnoreCase(this.parentFragment.quickReplyShortcut)) {
            editTextCaption = this.messageEditText;
            i3 = R.string.BusinessGreetingEnter;
        } else if ("away".equalsIgnoreCase(this.parentFragment.quickReplyShortcut)) {
            editTextCaption = this.messageEditText;
            i3 = R.string.BusinessAwayEnter;
        } else {
            editTextCaption = this.messageEditText;
            i3 = R.string.BusinessRepliesEnter;
        }
        string2 = LocaleController.getString(i3);
        editTextCaption.setHintText(string2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x008e, code lost:
    
        if (org.telegram.messenger.MessagesController.getInstance(r7.currentAccount).getMainSettings().getBoolean("show_gift_for_" + r7.parentFragment.getDialogId(), true) == false) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00d0, code lost:
    
        r0 = r7.parentFragment;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00d2, code lost:
    
        if (r0 == null) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00d8, code lost:
    
        if (r0.getChatMode() != 0) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00ce, code lost:
    
        if (org.telegram.messenger.MessagesController.getInstance(r7.currentAccount).getMainSettings().getBoolean(java.util.Calendar.getInstance().get(1) + "show_gift_for_" + r7.parentFragment.getDialogId(), true) != false) goto L28;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateGiftButton(boolean z) {
        boolean z2;
        HintView2 hintView2;
        if (!MessagesController.getInstance(this.currentAccount).premiumPurchaseBlocked() && getParentFragment() != null && getParentFragment().getCurrentUser() != null && !BuildVars.IS_BILLING_UNAVAILABLE && !getParentFragment().getCurrentUser().self && getParentFragment().getCurrentUserInfo() != null) {
            z2 = true;
            if (!getParentFragment().getCurrentUserInfo().premium_gifts.isEmpty() && !getParentFragment().getCurrentUser().premium && MessagesController.getInstance(this.currentAccount).giftAttachMenuIcon && MessagesController.getInstance(this.currentAccount).giftTextFieldIcon) {
            }
            if (BirthdayController.isToday(getParentFragment().getCurrentUserInfo().birthday)) {
            }
        }
        z2 = false;
        if (!z2 && (hintView2 = this.birthdayHint) != null) {
            hintView2.hide();
        }
        if (z2 || this.giftButton != null) {
            createGiftButton();
            AndroidUtilities.updateViewVisibilityAnimated(this.giftButton, z2, 1.0f, z);
            ImageView imageView = this.scheduledButton;
            if (imageView != null && imageView.getVisibility() == 0) {
                int i = z2 ? -AndroidUtilities.dp(48.0f) : 0;
                ImageView imageView2 = this.botButton;
                float dp = i + AndroidUtilities.dp((imageView2 == null || imageView2.getVisibility() != 0) ? 0.0f : 48.0f);
                if (z) {
                    this.scheduledButton.animate().translationX(dp).setDuration(150L).start();
                } else {
                    this.scheduledButton.setTranslationX(dp);
                }
            }
            if (z2) {
                checkBirthdayHint();
            }
        }
    }

    public void updateRecordButton(TLRPC.Chat chat, TLRPC.UserFull userFull) {
        EmojiView emojiView;
        this.emojiButtonRestricted = false;
        boolean z = true;
        this.stickersEnabled = true;
        this.sendPlainEnabled = true;
        this.sendRoundEnabled = true;
        this.sendVoiceEnabled = true;
        if (chat != null) {
            this.audioVideoButtonContainer.setAlpha((ChatObject.canSendVoice(chat) || (ChatObject.canSendRoundVideo(chat) && this.hasRecordVideo)) ? 1.0f : 0.5f);
            this.stickersEnabled = ChatObject.canSendStickers(chat);
            boolean canSendPlain = ChatObject.canSendPlain(chat);
            this.sendPlainEnabled = canSendPlain;
            boolean z2 = (this.stickersEnabled || canSendPlain) ? false : true;
            this.emojiButtonRestricted = z2;
            this.emojiButtonAlpha = z2 ? 0.5f : 1.0f;
            updateEmojiButtonParams();
            if (!this.emojiButtonRestricted && (emojiView = this.emojiView) != null) {
                emojiView.setStickersBanned(!this.sendPlainEnabled, !this.stickersEnabled, -this.dialog_id);
            }
            this.sendRoundEnabled = ChatObject.canSendRoundVideo(chat);
            this.sendVoiceEnabled = ChatObject.canSendVoice(chat);
        } else if (userFull != null) {
            this.userInfo = userFull;
            this.audioVideoButtonContainer.setAlpha(userFull.voice_messages_forbidden ? 0.5f : 1.0f);
        }
        updateFieldHint(false);
        boolean z3 = this.isInVideoMode;
        if (!this.sendRoundEnabled && z3) {
            z3 = false;
        }
        if (this.sendVoiceEnabled || z3) {
            z = z3;
        } else if (!this.hasRecordVideo) {
            z = false;
        }
        setRecordVideoButtonVisible(z, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:104:0x03d3  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x03d6  */
    /* JADX WARN: Type inference failed for: r4v135 */
    /* JADX WARN: Type inference failed for: r4v77 */
    /* JADX WARN: Type inference failed for: r4v78, types: [android.view.ViewGroup] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void updateRecordInterface(int i, boolean z) {
        ViewGroup.LayoutParams layoutParams;
        boolean z2;
        Property property;
        char c;
        float f;
        char c2;
        final int i2;
        String str;
        float f2;
        long j;
        float f3;
        float f4;
        char c3;
        float f5;
        ViewGroup.LayoutParams layoutParams2;
        ?? r4;
        Property property2;
        Property property3;
        AnimatorSet animatorSet;
        char c4;
        float f6;
        Runnable runnable = this.moveToSendStateRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.moveToSendStateRunnable = null;
        }
        RecordCircle recordCircle = this.recordCircle;
        if (recordCircle != null) {
            recordCircle.voiceEnterTransitionInProgress = false;
        }
        if (this.recordingAudioVideo) {
            if (this.recordInterfaceState == 1) {
                this.lastRecordState = i;
                return;
            }
            final boolean z3 = this.lastRecordState == 3;
            if (!z3) {
                this.voiceOnce = false;
                ControlsView controlsView = this.controlsView;
                if (controlsView != null) {
                    controlsView.periodDrawable.setValue(1, false, false);
                }
                MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
                long j2 = this.dialog_id;
                ChatActivity chatActivity = this.parentFragment;
                mediaDataController.toggleDraftVoiceOnce(j2, (chatActivity == null || !chatActivity.isTopic) ? 0L : chatActivity.getTopicId(), this.voiceOnce);
                this.millisecondsRecorded = 0L;
            }
            createRecordAudioPanel();
            this.recordInterfaceState = 1;
            EmojiView emojiView = this.emojiView;
            if (emojiView != null) {
                emojiView.setEnabled(false);
            }
            try {
                if (this.wakeLock == null) {
                    PowerManager.WakeLock newWakeLock = ((PowerManager) ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(536870918, "telegram:audio_record_lock");
                    this.wakeLock = newWakeLock;
                    newWakeLock.acquire();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
            AndroidUtilities.lockOrientation(this.parentActivity);
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                chatActivityEnterViewDelegate.needStartRecordAudio(0);
            }
            AnimatorSet animatorSet2 = this.runningAnimationAudio;
            if (animatorSet2 != null) {
                animatorSet2.cancel();
            }
            AnimatorSet animatorSet3 = this.recordPannelAnimation;
            if (animatorSet3 != null) {
                animatorSet3.cancel();
            }
            createRecordPanel();
            FrameLayout frameLayout = this.recordPanel;
            if (frameLayout != null) {
                frameLayout.setVisibility(0);
            }
            createRecordCircle();
            RecordCircle recordCircle2 = this.recordCircle;
            if (recordCircle2 != null) {
                recordCircle2.voiceEnterTransitionInProgress = false;
                recordCircle2.setVisibility(0);
                this.recordCircle.setAmplitude(0.0d);
            }
            ControlsView controlsView2 = this.controlsView;
            if (controlsView2 != null) {
                controlsView2.setVisibility(0);
            }
            RecordDot recordDot = this.recordDot;
            if (recordDot != null) {
                recordDot.resetAlpha();
                this.recordDot.setScaleX(0.0f);
                this.recordDot.setScaleY(0.0f);
                this.recordDot.enterAnimation = true;
            }
            this.runningAnimationAudio = new AnimatorSet();
            this.recordTimerView.setTranslationX(AndroidUtilities.dp(20.0f));
            this.recordTimerView.setAlpha(0.0f);
            if (this.lastRecordState != 3) {
                this.slideText.setTranslationX(AndroidUtilities.dp(20.0f));
                this.slideText.setAlpha(0.0f);
                this.slideText.setCancelToProgress(0.0f);
                this.slideText.setSlideX(1.0f);
            } else {
                this.slideText.setTranslationX(0.0f);
                this.slideText.setAlpha(0.0f);
                this.slideText.setCancelToProgress(1.0f);
            }
            this.slideText.setEnabled(true);
            this.recordCircle.resetLockTranslation(this.lastRecordState == 3);
            this.recordIsCanceled = false;
            isRecordingStateChanged();
            AnimatorSet animatorSet4 = new AnimatorSet();
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 0.0f);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_ALPHA, 0.0f);
            RecordDot recordDot2 = this.recordDot;
            Property property4 = View.SCALE_Y;
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(recordDot2, (Property<RecordDot, Float>) property4, 1.0f);
            RecordDot recordDot3 = this.recordDot;
            Property property5 = View.SCALE_X;
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(recordDot3, (Property<RecordDot, Float>) property5, 1.0f);
            TimerView timerView = this.recordTimerView;
            Property property6 = View.TRANSLATION_X;
            ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(timerView, (Property<TimerView, Float>) property6, 0.0f);
            TimerView timerView2 = this.recordTimerView;
            Property property7 = View.ALPHA;
            animatorSet4.playTogether(ofFloat, ofFloat2, ofFloat3, ofFloat4, ofFloat5, ObjectAnimator.ofFloat(timerView2, (Property<TimerView, Float>) property7, 1.0f));
            animatorSet4.playTogether(ObjectAnimator.ofFloat(this.slideText, (Property<SlideTextView, Float>) property6, 0.0f));
            animatorSet4.playTogether(ObjectAnimator.ofFloat(this.slideText, (Property<SlideTextView, Float>) property7, 1.0f));
            ControlsView controlsView3 = this.controlsView;
            if (controlsView3 != null) {
                animatorSet4.playTogether(ObjectAnimator.ofFloat(controlsView3, (Property<ControlsView, Float>) property7, 1.0f));
            }
            ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView = this.audioVideoSendButton;
            if (chatActivityEnterViewAnimatedIconView != null) {
                f6 = 0.0f;
                animatorSet4.playTogether(ObjectAnimator.ofFloat(chatActivityEnterViewAnimatedIconView, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property7, 0.0f));
            } else {
                f6 = 0.0f;
            }
            BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
            if (botCommandsMenuView != null) {
                animatorSet4.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView, (Property<BotCommandsMenuView, Float>) property4, f6), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property5, f6), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property7, f6));
            }
            AnimatorSet animatorSet5 = new AnimatorSet();
            animatorSet5.playTogether(ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) this.MESSAGE_TEXT_TRANSLATION_X, AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) property7, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioPanel, (Property<FrameLayout, Float>) property7, 1.0f));
            if (z3) {
                animatorSet5.playTogether(ObjectAnimator.ofFloat(this.recordedAudioSeekBar, (Property<SeekBarWaveformView, Float>) property7, 0.0f));
                animatorSet5.playTogether(ObjectAnimator.ofFloat(this.recordedAudioPlayButton, (Property<ImageView, Float>) property7, 0.0f));
                animatorSet5.playTogether(ObjectAnimator.ofFloat(this.recordedAudioBackground, (Property<View, Float>) property7, 0.0f));
                animatorSet5.playTogether(ObjectAnimator.ofFloat(this.recordedAudioTimeTextView, (Property<TextView, Float>) property7, 0.0f));
                animatorSet5.playTogether(ObjectAnimator.ofFloat(this.recordDeleteImageView, (Property<RLottieImageView, Float>) property7, 0.0f));
                animatorSet5.playTogether(ObjectAnimator.ofFloat(this.recordDeleteImageView, (Property<RLottieImageView, Float>) property5, 0.0f));
                animatorSet5.playTogether(ObjectAnimator.ofFloat(this.recordDeleteImageView, (Property<RLottieImageView, Float>) property4, 0.0f));
                animatorSet5.playTogether(ObjectAnimator.ofFloat(this.videoTimelineView, (Property<VideoTimelineView, Float>) property7, 0.0f));
            }
            ImageView imageView = this.scheduledButton;
            if (imageView != null) {
                animatorSet5.playTogether(ObjectAnimator.ofFloat(imageView, (Property<ImageView, Float>) property6, AndroidUtilities.dp(30.0f)), ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) property7, 0.0f));
            }
            LinearLayout linearLayout = this.attachLayout;
            if (linearLayout != null) {
                animatorSet5.playTogether(ObjectAnimator.ofFloat(linearLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_TRANSLATION_X, AndroidUtilities.dp(30.0f)), ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 0.0f));
            }
            this.runningAnimationAudio.playTogether(animatorSet4.setDuration(150L), animatorSet5.setDuration(150L), ObjectAnimator.ofFloat(this.recordCircle, (Property<RecordCircle, Float>) this.recordCircleScale, 1.0f).setDuration(300L));
            if (!z3) {
                this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(this.recordCircle, (Property<RecordCircle, Float>) this.recordControlsCircleScale, 1.0f).setDuration(300L));
            }
            this.runningAnimationAudio.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.59
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator.equals(ChatActivityEnterView.this.runningAnimationAudio)) {
                        ChatActivityEnterView.this.runningAnimationAudio = null;
                    }
                    ChatActivityEnterView.this.isRecordingStateChanged();
                    ChatActivityEnterView.this.slideText.setAlpha(1.0f);
                    ChatActivityEnterView.this.slideText.setTranslationX(0.0f);
                    ControlsView controlsView4 = ChatActivityEnterView.this.controlsView;
                    if (controlsView4 != null) {
                        controlsView4.showTooltipIfNeed();
                    }
                    EditTextCaption editTextCaption = ChatActivityEnterView.this.messageEditText;
                    if (editTextCaption != null) {
                        editTextCaption.setAlpha(0.0f);
                    }
                    if (z3) {
                        SeekBarWaveformView seekBarWaveformView = ChatActivityEnterView.this.recordedAudioSeekBar;
                        if (seekBarWaveformView != null) {
                            seekBarWaveformView.setVisibility(8);
                        }
                        FrameLayout frameLayout2 = ChatActivityEnterView.this.recordedAudioPanel;
                        if (frameLayout2 != null) {
                            frameLayout2.setVisibility(8);
                        }
                        ChatActivityEnterView.this.isRecordingStateChanged();
                    }
                }
            });
            this.runningAnimationAudio.setInterpolator(new DecelerateInterpolator());
            this.runningAnimationAudio.start();
            this.recordTimerView.start(this.millisecondsRecorded);
            i2 = i;
        } else {
            if (this.recordIsCanceled && i == 3) {
                return;
            }
            PowerManager.WakeLock wakeLock = this.wakeLock;
            if (wakeLock != null) {
                try {
                    wakeLock.release();
                    layoutParams = null;
                    try {
                        this.wakeLock = null;
                    } catch (Exception e2) {
                        e = e2;
                        FileLog.e(e);
                        AndroidUtilities.unlockOrientation(this.parentActivity);
                        this.wasSendTyping = false;
                        if (this.recordInterfaceState != 0) {
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    layoutParams = null;
                }
            } else {
                layoutParams = null;
            }
            AndroidUtilities.unlockOrientation(this.parentActivity);
            this.wasSendTyping = false;
            if (this.recordInterfaceState != 0) {
                this.lastRecordState = i;
                return;
            }
            this.accountInstance.getMessagesController().sendTyping(this.dialog_id, getThreadMessageId(), 2, 0);
            this.recordInterfaceState = 0;
            EmojiView emojiView2 = this.emojiView;
            if (emojiView2 != null) {
                emojiView2.setEnabled(true);
            }
            AnimatorSet animatorSet6 = this.runningAnimationAudio;
            if (animatorSet6 != null) {
                z2 = animatorSet6.isRunning();
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView2 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView2 != null) {
                    chatActivityEnterViewAnimatedIconView2.setScaleX(1.0f);
                    this.audioVideoSendButton.setScaleY(1.0f);
                }
                this.runningAnimationAudio.removeAllListeners();
                this.runningAnimationAudio.cancel();
            } else {
                z2 = false;
            }
            AnimatorSet animatorSet7 = this.recordPannelAnimation;
            if (animatorSet7 != null) {
                animatorSet7.cancel();
            }
            EditTextCaption editTextCaption = this.messageEditText;
            if (editTextCaption != null) {
                editTextCaption.setVisibility(0);
            }
            this.runningAnimationAudio = new AnimatorSet();
            if (z2 || i == 4) {
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView3 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView3 != null) {
                    chatActivityEnterViewAnimatedIconView3.setVisibility(0);
                }
                AnimatorSet animatorSet8 = this.runningAnimationAudio;
                ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 1.0f);
                ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_ALPHA, this.emojiButtonRestricted ? 0.5f : 1.0f);
                RecordDot recordDot4 = this.recordDot;
                Property property8 = View.SCALE_Y;
                ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat(recordDot4, (Property<RecordDot, Float>) property8, 0.0f);
                RecordDot recordDot5 = this.recordDot;
                Property property9 = View.SCALE_X;
                ObjectAnimator ofFloat9 = ObjectAnimator.ofFloat(recordDot5, (Property<RecordDot, Float>) property9, 0.0f);
                ObjectAnimator ofFloat10 = ObjectAnimator.ofFloat(this.recordCircle, (Property<RecordCircle, Float>) this.recordCircleScale, 0.0f);
                ObjectAnimator ofFloat11 = ObjectAnimator.ofFloat(this.recordCircle, (Property<RecordCircle, Float>) this.recordControlsCircleScale, 0.0f);
                FrameLayout frameLayout2 = this.audioVideoButtonContainer;
                Property property10 = View.ALPHA;
                animatorSet8.playTogether(ofFloat6, ofFloat7, ofFloat8, ofFloat9, ofFloat10, ofFloat11, ObjectAnimator.ofFloat(frameLayout2, (Property<FrameLayout, Float>) property10, 1.0f), ObjectAnimator.ofFloat(this.recordTimerView, (Property<TimerView, Float>) property10, 0.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) property10, 1.0f), ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) property10, 1.0f), ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) this.MESSAGE_TEXT_TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this, "slideToCancelProgress", 1.0f));
                ControlsView controlsView4 = this.controlsView;
                if (controlsView4 != null) {
                    c = 0;
                    property = property10;
                    this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(controlsView4, (Property<ControlsView, Float>) property, 0.0f));
                    this.controlsView.hideHintView();
                } else {
                    property = property10;
                    c = 0;
                }
                BotCommandsMenuView botCommandsMenuView2 = this.botCommandsMenuButton;
                if (botCommandsMenuView2 != null) {
                    AnimatorSet animatorSet9 = this.runningAnimationAudio;
                    float[] fArr = new float[1];
                    f = 1.0f;
                    fArr[c] = 1.0f;
                    ObjectAnimator ofFloat12 = ObjectAnimator.ofFloat(botCommandsMenuView2, (Property<BotCommandsMenuView, Float>) property8, fArr);
                    BotCommandsMenuView botCommandsMenuView3 = this.botCommandsMenuButton;
                    float[] fArr2 = new float[1];
                    fArr2[c] = 1.0f;
                    ObjectAnimator ofFloat13 = ObjectAnimator.ofFloat(botCommandsMenuView3, (Property<BotCommandsMenuView, Float>) property9, fArr2);
                    BotCommandsMenuView botCommandsMenuView4 = this.botCommandsMenuButton;
                    float[] fArr3 = new float[1];
                    fArr3[c] = 1.0f;
                    ObjectAnimator ofFloat14 = ObjectAnimator.ofFloat(botCommandsMenuView4, (Property<BotCommandsMenuView, Float>) property, fArr3);
                    Animator[] animatorArr = new Animator[3];
                    animatorArr[c] = ofFloat12;
                    animatorArr[1] = ofFloat13;
                    animatorArr[2] = ofFloat14;
                    animatorSet9.playTogether(animatorArr);
                } else {
                    f = 1.0f;
                }
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView4 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView4 != null) {
                    chatActivityEnterViewAnimatedIconView4.setScaleX(f);
                    this.audioVideoSendButton.setScaleY(f);
                    this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(this.audioVideoSendButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property, f));
                    this.audioVideoSendButton.setState(isInVideoMode() ? ChatActivityEnterViewAnimatedIconView.State.VIDEO : ChatActivityEnterViewAnimatedIconView.State.VOICE, true);
                }
                ImageView imageView2 = this.scheduledButton;
                if (imageView2 != null) {
                    c2 = 0;
                    this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(imageView2, (Property<ImageView, Float>) View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) property, 1.0f));
                } else {
                    c2 = 0;
                }
                LinearLayout linearLayout2 = this.attachLayout;
                if (linearLayout2 != null) {
                    AnimatorSet animatorSet10 = this.runningAnimationAudio;
                    Property property11 = this.ATTACH_LAYOUT_TRANSLATION_X;
                    float[] fArr4 = new float[1];
                    fArr4[c2] = 0.0f;
                    ObjectAnimator ofFloat15 = ObjectAnimator.ofFloat(linearLayout2, (Property<LinearLayout, Float>) property11, fArr4);
                    LinearLayout linearLayout3 = this.attachLayout;
                    Property property12 = this.ATTACH_LAYOUT_ALPHA;
                    float[] fArr5 = new float[1];
                    fArr5[c2] = 1.0f;
                    ObjectAnimator ofFloat16 = ObjectAnimator.ofFloat(linearLayout3, (Property<LinearLayout, Float>) property12, fArr5);
                    Animator[] animatorArr2 = new Animator[2];
                    animatorArr2[c2] = ofFloat15;
                    animatorArr2[1] = ofFloat16;
                    animatorSet10.playTogether(animatorArr2);
                }
                this.recordIsCanceled = true;
                isRecordingStateChanged();
                this.runningAnimationAudio.setDuration(150L);
            } else if (i == 3) {
                createRecordAudioPanel();
                createRecordCircle();
                SlideTextView slideTextView = this.slideText;
                if (slideTextView != null) {
                    slideTextView.setEnabled(false);
                }
                if (isInVideoMode()) {
                    View view = this.recordedAudioBackground;
                    if (view != null) {
                        view.setVisibility(8);
                    }
                    TextView textView = this.recordedAudioTimeTextView;
                    if (textView != null) {
                        textView.setVisibility(8);
                    }
                    ImageView imageView3 = this.recordedAudioPlayButton;
                    if (imageView3 != null) {
                        imageView3.setVisibility(8);
                    }
                    SeekBarWaveformView seekBarWaveformView = this.recordedAudioSeekBar;
                    if (seekBarWaveformView != null) {
                        seekBarWaveformView.setVisibility(8);
                        isRecordingStateChanged();
                    }
                    FrameLayout frameLayout3 = this.recordedAudioPanel;
                    if (frameLayout3 != null) {
                        frameLayout3.setAlpha(1.0f);
                        this.recordedAudioPanel.setVisibility(0);
                    }
                    RLottieImageView rLottieImageView = this.recordDeleteImageView;
                    if (rLottieImageView != null) {
                        rLottieImageView.setProgress(0.0f);
                        this.recordDeleteImageView.stopAnimation();
                    }
                } else {
                    VideoTimelineView videoTimelineView = this.videoTimelineView;
                    if (videoTimelineView != null) {
                        videoTimelineView.setVisibility(8);
                        isRecordingStateChanged();
                    }
                    TextView textView2 = this.recordedAudioTimeTextView;
                    if (textView2 != null) {
                        textView2.setVisibility(0);
                        this.recordedAudioTimeTextView.setAlpha(0.0f);
                    }
                    FrameLayout frameLayout4 = this.recordedAudioPanel;
                    if (frameLayout4 != null) {
                        frameLayout4.setVisibility(0);
                        this.recordedAudioPanel.setAlpha(1.0f);
                    }
                    View view2 = this.recordedAudioBackground;
                    if (view2 != null) {
                        view2.setVisibility(0);
                        f5 = 0.0f;
                        this.recordedAudioBackground.setAlpha(0.0f);
                    } else {
                        f5 = 0.0f;
                    }
                    ImageView imageView4 = this.recordedAudioPlayButton;
                    if (imageView4 != null) {
                        imageView4.setVisibility(0);
                        this.recordedAudioPlayButton.setAlpha(f5);
                    }
                    SeekBarWaveformView seekBarWaveformView2 = this.recordedAudioSeekBar;
                    if (seekBarWaveformView2 != null) {
                        seekBarWaveformView2.setVisibility(0);
                        this.recordedAudioSeekBar.setAlpha(f5);
                        isRecordingStateChanged();
                    }
                }
                this.sendButtonVisible = true;
                this.snapAnimationProgress = 1.0f;
                this.lockAnimatedTranslation = this.startTranslation;
                this.slideToCancelProgress = 1.0f;
                SlideTextView slideTextView2 = this.slideText;
                if (slideTextView2 != null) {
                    slideTextView2.setCancelToProgress(1.0f);
                }
                ControlsView controlsView5 = this.controlsView;
                if (controlsView5 != null) {
                    controlsView5.invalidate();
                }
                RLottieImageView rLottieImageView2 = this.recordDeleteImageView;
                if (rLottieImageView2 != null) {
                    rLottieImageView2.setAlpha(0.0f);
                    this.recordDeleteImageView.setScaleX(0.0f);
                    this.recordDeleteImageView.setScaleY(0.0f);
                    this.recordDeleteImageView.setProgress(0.0f);
                    this.recordDeleteImageView.stopAnimation();
                }
                if (isInVideoMode() || this.shouldDrawRecordedAudioPanelInParent) {
                    this.videoTimelineView.setVisibility(0);
                    layoutParams2 = layoutParams;
                    r4 = layoutParams2;
                } else {
                    ViewGroup viewGroup = (ViewGroup) this.recordedAudioPanel.getParent();
                    layoutParams2 = this.recordedAudioPanel.getLayoutParams();
                    viewGroup.removeView(this.recordedAudioPanel);
                    FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(viewGroup.getMeasuredWidth(), AndroidUtilities.dp(48.0f));
                    layoutParams3.gravity = 80;
                    this.sizeNotifierLayout.addView(this.recordedAudioPanel, layoutParams3);
                    this.videoTimelineView.setVisibility(8);
                    r4 = viewGroup;
                }
                isRecordingStateChanged();
                AnimatorSet animatorSet11 = new AnimatorSet();
                if (z) {
                    ValueAnimator ofFloat17 = ValueAnimator.ofFloat(0.0f, 1.0f);
                    ofFloat17.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda31
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            ChatActivityEnterView.this.lambda$updateRecordInterface$51(valueAnimator);
                        }
                    });
                    ofFloat17.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.60
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            ChatActivityEnterView.this.recordCircle.setTransformToSeekbar(1.0f);
                            ChatActivityEnterView.this.isRecordingStateChanged();
                        }
                    });
                    ofFloat17.setDuration(isInVideoMode() ? 490L : 580L);
                    AnimatorSet animatorSet12 = new AnimatorSet();
                    RecordDot recordDot6 = this.recordDot;
                    Property property13 = View.SCALE_Y;
                    ObjectAnimator ofFloat18 = ObjectAnimator.ofFloat(recordDot6, (Property<RecordDot, Float>) property13, 0.0f);
                    RecordDot recordDot7 = this.recordDot;
                    Property property14 = View.SCALE_X;
                    ObjectAnimator ofFloat19 = ObjectAnimator.ofFloat(recordDot7, (Property<RecordDot, Float>) property14, 0.0f);
                    TimerView timerView3 = this.recordTimerView;
                    Property property15 = View.ALPHA;
                    final ViewGroup.LayoutParams layoutParams4 = layoutParams2;
                    final ViewGroup viewGroup2 = r4;
                    animatorSet12.playTogether(ofFloat18, ofFloat19, ObjectAnimator.ofFloat(timerView3, (Property<TimerView, Float>) property15, 0.0f), ObjectAnimator.ofFloat(this.recordTimerView, (Property<TimerView, Float>) View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.slideText, (Property<SlideTextView, Float>) property15, 0.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, (Property<RLottieImageView, Float>) property15, 1.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, (Property<RLottieImageView, Float>) property13, 1.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, (Property<RLottieImageView, Float>) property14, 1.0f), ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 0.0f), ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_ALPHA, 0.0f), ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) property15, 0.0f));
                    RLottieImageView rLottieImageView3 = this.recordDeleteImageView;
                    if (rLottieImageView3 != null) {
                        rLottieImageView3.setAlpha(0.0f);
                        this.recordDeleteImageView.setScaleX(0.0f);
                        this.recordDeleteImageView.setScaleY(0.0f);
                    }
                    ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView5 = this.audioVideoSendButton;
                    if (chatActivityEnterViewAnimatedIconView5 != null) {
                        property3 = property14;
                        property2 = property13;
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(chatActivityEnterViewAnimatedIconView5, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property15, 1.0f), ObjectAnimator.ofFloat(this.audioVideoSendButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property3, 1.0f), ObjectAnimator.ofFloat(this.audioVideoSendButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property2, 1.0f));
                        this.audioVideoSendButton.setState(isInVideoMode() ? ChatActivityEnterViewAnimatedIconView.State.VIDEO : ChatActivityEnterViewAnimatedIconView.State.VOICE, true);
                    } else {
                        property2 = property13;
                        property3 = property14;
                    }
                    BotCommandsMenuView botCommandsMenuView5 = this.botCommandsMenuButton;
                    if (botCommandsMenuView5 != null) {
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView5, (Property<BotCommandsMenuView, Float>) property15, 0.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property3, 0.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property2, 0.0f));
                    }
                    animatorSet12.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.61
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (ChatActivityEnterView.this.audioVideoSendButton != null) {
                                ChatActivityEnterView.this.audioVideoSendButton.setScaleX(1.0f);
                                ChatActivityEnterView.this.audioVideoSendButton.setScaleY(1.0f);
                            }
                        }
                    });
                    animatorSet12.setDuration(150L);
                    animatorSet12.setStartDelay(150L);
                    if (isInVideoMode()) {
                        this.recordedAudioTimeTextView.setAlpha(0.0f);
                        this.videoTimelineView.setAlpha(0.0f);
                        c4 = 0;
                        animatorSet = animatorSet11;
                        animatorSet.playTogether(ObjectAnimator.ofFloat(this.recordedAudioTimeTextView, (Property<TextView, Float>) property15, 1.0f), ObjectAnimator.ofFloat(this.videoTimelineView, (Property<VideoTimelineView, Float>) property15, 1.0f));
                        animatorSet.setDuration(150L);
                        animatorSet.setStartDelay(430L);
                    } else {
                        animatorSet = animatorSet11;
                        c4 = 0;
                    }
                    AnimatorSet animatorSet13 = this.runningAnimationAudio;
                    Animator[] animatorArr3 = new Animator[3];
                    animatorArr3[c4] = animatorSet12;
                    animatorArr3[1] = ofFloat17;
                    animatorArr3[2] = animatorSet;
                    animatorSet13.playTogether(animatorArr3);
                    this.runningAnimationAudio.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.62
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (viewGroup2 != null) {
                                ChatActivityEnterView.this.sizeNotifierLayout.removeView(ChatActivityEnterView.this.recordedAudioPanel);
                                viewGroup2.addView(ChatActivityEnterView.this.recordedAudioPanel, layoutParams4);
                            }
                            ChatActivityEnterView.this.recordedAudioPanel.setAlpha(1.0f);
                            ChatActivityEnterView.this.recordedAudioBackground.setAlpha(1.0f);
                            ChatActivityEnterView.this.recordedAudioTimeTextView.setAlpha(1.0f);
                            ChatActivityEnterView.this.recordedAudioPlayButton.setAlpha(1.0f);
                            ChatActivityEnterView.this.recordedAudioPlayButton.setScaleY(1.0f);
                            ChatActivityEnterView.this.recordedAudioPlayButton.setScaleX(1.0f);
                            ChatActivityEnterView.this.recordedAudioSeekBar.setAlpha(1.0f);
                            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                            chatActivityEnterView.emojiButtonScale = 0.0f;
                            chatActivityEnterView.emojiButtonAlpha = 0.0f;
                            chatActivityEnterView.updateEmojiButtonParams();
                            if (ChatActivityEnterView.this.botCommandsMenuButton != null) {
                                ChatActivityEnterView.this.botCommandsMenuButton.setAlpha(0.0f);
                                ChatActivityEnterView.this.botCommandsMenuButton.setScaleX(0.0f);
                                ChatActivityEnterView.this.botCommandsMenuButton.setScaleY(0.0f);
                            }
                            ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                            if (chatActivityEnterView2.controlsView == null || !chatActivityEnterView2.onceVisible || chatActivityEnterView2.voiceOnce || MessagesController.getGlobalMainSettings().getInt("voiceoncehint", 0) >= 3) {
                                return;
                            }
                            ChatActivityEnterView.this.controlsView.showHintView();
                        }
                    });
                } else {
                    createRecordPanel();
                    this.recordCircleScale.set(this.recordCircle, Float.valueOf(1.0f));
                    this.recordCircle.setTransformToSeekbar(1.0f);
                    if (!isInVideoMode()) {
                        float f7 = this.transformToSeekbar;
                        if (f7 != 0.0f && this.recordedAudioBackground != null) {
                            float interpolation = CubicBezierInterpolator.EASE_BOTH.getInterpolation(Math.max(0.0f, ((f7 - 0.38f) - 0.25f) / 0.37f));
                            this.seekBarWaveform.setWaveScaling(interpolation);
                            this.recordedAudioTimeTextView.setAlpha(interpolation);
                            this.recordedAudioPlayButton.setAlpha(interpolation);
                            this.recordedAudioPlayButton.setScaleX(interpolation);
                            this.recordedAudioPlayButton.setScaleY(interpolation);
                            this.recordedAudioSeekBar.setAlpha(interpolation);
                            this.recordedAudioSeekBar.invalidate();
                        }
                    }
                    this.recordDot.setScaleY(0.0f);
                    this.recordDot.setScaleX(0.0f);
                    this.recordTimerView.setAlpha(0.0f);
                    this.recordTimerView.setTranslationX(-AndroidUtilities.dp(20.0f));
                    this.slideText.setAlpha(0.0f);
                    this.recordDeleteImageView.setAlpha(1.0f);
                    this.recordDeleteImageView.setScaleY(1.0f);
                    this.recordDeleteImageView.setScaleX(1.0f);
                    this.EMOJI_BUTTON_SCALE.set(this.emojiButton, Float.valueOf(0.0f));
                    this.EMOJI_BUTTON_ALPHA.set(this.emojiButton, Float.valueOf(0.0f));
                    this.messageEditText.setAlpha(0.0f);
                    ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView6 = this.audioVideoSendButton;
                    if (chatActivityEnterViewAnimatedIconView6 != null) {
                        chatActivityEnterViewAnimatedIconView6.setState(isInVideoMode() ? ChatActivityEnterViewAnimatedIconView.State.VIDEO : ChatActivityEnterViewAnimatedIconView.State.VOICE, z);
                        this.audioVideoSendButton.setAlpha(1.0f);
                        this.audioVideoSendButton.setScaleX(1.0f);
                        this.audioVideoSendButton.setScaleY(1.0f);
                    }
                    BotCommandsMenuView botCommandsMenuView6 = this.botCommandsMenuButton;
                    if (botCommandsMenuView6 != null) {
                        botCommandsMenuView6.setAlpha(0.0f);
                        this.botCommandsMenuButton.setScaleX(0.0f);
                        this.botCommandsMenuButton.setScaleY(0.0f);
                    }
                    if (isInVideoMode()) {
                        this.recordedAudioTimeTextView.setAlpha(1.0f);
                        this.videoTimelineView.setAlpha(1.0f);
                    }
                    if (r4 != 0) {
                        this.sizeNotifierLayout.removeView(this.recordedAudioPanel);
                        r4.addView(this.recordedAudioPanel, layoutParams2);
                    }
                    this.recordedAudioPanel.setAlpha(1.0f);
                    this.recordedAudioBackground.setAlpha(1.0f);
                    this.recordedAudioTimeTextView.setAlpha(1.0f);
                    this.recordedAudioPlayButton.setAlpha(1.0f);
                    this.recordedAudioPlayButton.setScaleY(1.0f);
                    this.recordedAudioPlayButton.setScaleX(1.0f);
                    this.recordedAudioSeekBar.setAlpha(1.0f);
                    this.emojiButtonScale = 0.0f;
                    this.emojiButtonAlpha = 0.0f;
                    updateEmojiButtonParams();
                    isRecordingStateChanged();
                }
            } else if (i == 2 || i == 5) {
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView7 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView7 != null) {
                    chatActivityEnterViewAnimatedIconView7.setVisibility(0);
                }
                this.recordIsCanceled = true;
                isRecordingStateChanged();
                AnimatorSet animatorSet14 = new AnimatorSet();
                ObjectAnimator ofFloat20 = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 1.0f);
                ObjectAnimator ofFloat21 = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_ALPHA, this.emojiButtonRestricted ? 0.5f : 1.0f);
                RecordDot recordDot8 = this.recordDot;
                Property property16 = View.SCALE_Y;
                ObjectAnimator ofFloat22 = ObjectAnimator.ofFloat(recordDot8, (Property<RecordDot, Float>) property16, 0.0f);
                RecordDot recordDot9 = this.recordDot;
                Property property17 = View.SCALE_X;
                animatorSet14.playTogether(ofFloat20, ofFloat21, ofFloat22, ObjectAnimator.ofFloat(recordDot9, (Property<RecordDot, Float>) property17, 0.0f));
                ControlsView controlsView6 = this.controlsView;
                if (controlsView6 != null) {
                    animatorSet14.playTogether(ObjectAnimator.ofFloat(controlsView6, (Property<ControlsView, Float>) View.ALPHA, 0.0f));
                    this.controlsView.hideHintView();
                }
                BotCommandsMenuView botCommandsMenuView7 = this.botCommandsMenuButton;
                if (botCommandsMenuView7 != null) {
                    animatorSet14.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView7, (Property<BotCommandsMenuView, Float>) property16, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property17, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) View.ALPHA, 1.0f));
                }
                AnimatorSet animatorSet15 = new AnimatorSet();
                TimerView timerView4 = this.recordTimerView;
                Property property18 = View.ALPHA;
                ObjectAnimator ofFloat23 = ObjectAnimator.ofFloat(timerView4, (Property<TimerView, Float>) property18, 0.0f);
                TimerView timerView5 = this.recordTimerView;
                Property property19 = View.TRANSLATION_X;
                animatorSet15.playTogether(ofFloat23, ObjectAnimator.ofFloat(timerView5, (Property<TimerView, Float>) property19, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.slideText, (Property<SlideTextView, Float>) property18, 0.0f), ObjectAnimator.ofFloat(this.slideText, (Property<SlideTextView, Float>) property19, -AndroidUtilities.dp(20.0f)));
                if (i != 5) {
                    this.audioVideoButtonContainer.setScaleX(0.0f);
                    this.audioVideoButtonContainer.setScaleY(0.0f);
                    ImageView imageView5 = this.attachButton;
                    if (imageView5 != null && imageView5.getVisibility() == 0) {
                        this.attachButton.setScaleX(0.0f);
                        this.attachButton.setScaleY(0.0f);
                    }
                    ImageView imageView6 = this.botButton;
                    if (imageView6 != null && imageView6.getVisibility() == 0) {
                        this.botButton.setScaleX(0.0f);
                        this.botButton.setScaleY(0.0f);
                    }
                    str = "slideToCancelProgress";
                    animatorSet14.playTogether(ObjectAnimator.ofFloat(this, "slideToCancelProgress", 1.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) property17, 1.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) property16, 1.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) property18, 1.0f));
                    LinearLayout linearLayout4 = this.attachLayout;
                    if (linearLayout4 != null) {
                        animatorSet14.playTogether(ObjectAnimator.ofFloat(linearLayout4, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 1.0f), ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_TRANSLATION_X, 0.0f));
                    }
                    ImageView imageView7 = this.attachButton;
                    if (imageView7 != null) {
                        f3 = 1.0f;
                        animatorSet14.playTogether(ObjectAnimator.ofFloat(imageView7, (Property<ImageView, Float>) property17, 1.0f), ObjectAnimator.ofFloat(this.attachButton, (Property<ImageView, Float>) property16, 1.0f));
                    } else {
                        f3 = 1.0f;
                    }
                    ImageView imageView8 = this.botButton;
                    if (imageView8 != null) {
                        animatorSet14.playTogether(ObjectAnimator.ofFloat(imageView8, (Property<ImageView, Float>) property17, f3), ObjectAnimator.ofFloat(this.botButton, (Property<ImageView, Float>) property16, f3));
                    }
                    ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView8 = this.audioVideoSendButton;
                    if (chatActivityEnterViewAnimatedIconView8 != null) {
                        animatorSet14.playTogether(ObjectAnimator.ofFloat(chatActivityEnterViewAnimatedIconView8, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property18, f3));
                        animatorSet14.playTogether(ObjectAnimator.ofFloat(this.audioVideoSendButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property17, f3));
                        animatorSet14.playTogether(ObjectAnimator.ofFloat(this.audioVideoSendButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property16, f3));
                        this.audioVideoSendButton.setState(isInVideoMode() ? ChatActivityEnterViewAnimatedIconView.State.VIDEO : ChatActivityEnterViewAnimatedIconView.State.VOICE, true);
                    }
                    ImageView imageView9 = this.scheduledButton;
                    if (imageView9 != null) {
                        animatorSet14.playTogether(ObjectAnimator.ofFloat(imageView9, (Property<ImageView, Float>) property18, 1.0f), ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) property19, 0.0f));
                    }
                    j = 150;
                } else {
                    str = "slideToCancelProgress";
                    AnimatorSet animatorSet16 = new AnimatorSet();
                    animatorSet16.playTogether(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, (Property<FrameLayout, Float>) property18, 1.0f));
                    LinearLayout linearLayout5 = this.attachLayout;
                    if (linearLayout5 != null) {
                        f2 = 1.0f;
                        animatorSet16.playTogether(ObjectAnimator.ofFloat(linearLayout5, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 1.0f));
                    } else {
                        f2 = 1.0f;
                    }
                    ImageView imageView10 = this.scheduledButton;
                    if (imageView10 != null) {
                        animatorSet16.playTogether(ObjectAnimator.ofFloat(imageView10, (Property<ImageView, Float>) property18, f2), ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) property19, 0.0f));
                    }
                    j = 150;
                    animatorSet16.setDuration(150L);
                    animatorSet16.setStartDelay(110L);
                    animatorSet16.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.63
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            if (ChatActivityEnterView.this.audioVideoSendButton != null) {
                                ChatActivityEnterView.this.audioVideoSendButton.setAlpha(1.0f);
                            }
                        }
                    });
                    this.runningAnimationAudio.playTogether(animatorSet16);
                }
                animatorSet14.setDuration(j);
                animatorSet14.setStartDelay(700L);
                animatorSet15.setDuration(200L);
                animatorSet15.setStartDelay(200L);
                this.messageTextTranslationX = 0.0f;
                updateMessageTextParams();
                ObjectAnimator ofFloat24 = ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) property18, 1.0f);
                ofFloat24.setStartDelay(this.emojiButtonPaddingAlpha == 1.0f ? 300L : 700L);
                ofFloat24.setDuration(200L);
                this.runningAnimationAudio.playTogether(animatorSet14, animatorSet15, ofFloat24, ObjectAnimator.ofFloat(this, "lockAnimatedTranslation", this.startTranslation).setDuration(200L));
                if (i == 5) {
                    this.recordCircle.canceledByGesture();
                    ObjectAnimator duration = ObjectAnimator.ofFloat(this, str, 1.0f).setDuration(200L);
                    duration.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                    this.runningAnimationAudio.playTogether(duration);
                } else {
                    ObjectAnimator ofFloat25 = ObjectAnimator.ofFloat(this, "exitTransition", 1.0f);
                    ofFloat25.setDuration(360L);
                    ofFloat25.setStartDelay(490L);
                    this.runningAnimationAudio.playTogether(ofFloat25);
                }
                RecordDot recordDot10 = this.recordDot;
                if (recordDot10 != null) {
                    recordDot10.playDeleteAnimation();
                }
            } else {
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView9 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView9 != null) {
                    chatActivityEnterViewAnimatedIconView9.setVisibility(0);
                }
                AnimatorSet animatorSet17 = new AnimatorSet();
                ObjectAnimator ofFloat26 = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 1.0f);
                ObjectAnimator ofFloat27 = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_ALPHA, this.emojiButtonRestricted ? 0.5f : 1.0f);
                RecordDot recordDot11 = this.recordDot;
                Property property20 = View.SCALE_Y;
                ObjectAnimator ofFloat28 = ObjectAnimator.ofFloat(recordDot11, (Property<RecordDot, Float>) property20, 0.0f);
                RecordDot recordDot12 = this.recordDot;
                Property property21 = View.SCALE_X;
                ObjectAnimator ofFloat29 = ObjectAnimator.ofFloat(recordDot12, (Property<RecordDot, Float>) property21, 0.0f);
                FrameLayout frameLayout5 = this.audioVideoButtonContainer;
                Property property22 = View.ALPHA;
                animatorSet17.playTogether(ofFloat26, ofFloat27, ofFloat28, ofFloat29, ObjectAnimator.ofFloat(frameLayout5, (Property<FrameLayout, Float>) property22, 1.0f));
                ControlsView controlsView7 = this.controlsView;
                if (controlsView7 != null) {
                    animatorSet17.playTogether(ObjectAnimator.ofFloat(controlsView7, (Property<ControlsView, Float>) property22, 0.0f));
                    this.controlsView.hideHintView();
                }
                BotCommandsMenuView botCommandsMenuView8 = this.botCommandsMenuButton;
                if (botCommandsMenuView8 != null) {
                    f4 = 1.0f;
                    animatorSet17.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView8, (Property<BotCommandsMenuView, Float>) property20, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property21, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, (Property<BotCommandsMenuView, Float>) property22, 1.0f));
                } else {
                    f4 = 1.0f;
                }
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView10 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView10 != null) {
                    chatActivityEnterViewAnimatedIconView10.setScaleX(f4);
                    this.audioVideoSendButton.setScaleY(f4);
                    animatorSet17.playTogether(ObjectAnimator.ofFloat(this.audioVideoSendButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property22, f4));
                    this.audioVideoSendButton.setState(isInVideoMode() ? ChatActivityEnterViewAnimatedIconView.State.VIDEO : ChatActivityEnterViewAnimatedIconView.State.VOICE, true);
                }
                if (this.attachLayout != null) {
                    this.attachLayoutTranslationX = 0.0f;
                    updateAttachLayoutParams();
                    c3 = 0;
                    animatorSet17.playTogether(ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 1.0f));
                } else {
                    c3 = 0;
                }
                ImageView imageView11 = this.scheduledButton;
                if (imageView11 != null) {
                    imageView11.setTranslationX(0.0f);
                    ImageView imageView12 = this.scheduledButton;
                    float[] fArr6 = new float[1];
                    fArr6[c3] = 1.0f;
                    ObjectAnimator ofFloat30 = ObjectAnimator.ofFloat(imageView12, (Property<ImageView, Float>) property22, fArr6);
                    Animator[] animatorArr4 = new Animator[1];
                    animatorArr4[c3] = ofFloat30;
                    animatorSet17.playTogether(animatorArr4);
                }
                animatorSet17.setDuration(150L);
                animatorSet17.setStartDelay(200L);
                AnimatorSet animatorSet18 = new AnimatorSet();
                TimerView timerView6 = this.recordTimerView;
                float[] fArr7 = new float[1];
                fArr7[c3] = 0.0f;
                ObjectAnimator ofFloat31 = ObjectAnimator.ofFloat(timerView6, (Property<TimerView, Float>) property22, fArr7);
                TimerView timerView7 = this.recordTimerView;
                Property property23 = View.TRANSLATION_X;
                float[] fArr8 = new float[1];
                fArr8[c3] = AndroidUtilities.dp(40.0f);
                ObjectAnimator ofFloat32 = ObjectAnimator.ofFloat(timerView7, (Property<TimerView, Float>) property23, fArr8);
                SlideTextView slideTextView3 = this.slideText;
                float[] fArr9 = new float[1];
                fArr9[c3] = 0.0f;
                ObjectAnimator ofFloat33 = ObjectAnimator.ofFloat(slideTextView3, (Property<SlideTextView, Float>) property22, fArr9);
                SlideTextView slideTextView4 = this.slideText;
                float[] fArr10 = new float[1];
                fArr10[c3] = AndroidUtilities.dp(40.0f);
                ObjectAnimator ofFloat34 = ObjectAnimator.ofFloat(slideTextView4, (Property<SlideTextView, Float>) property23, fArr10);
                Animator[] animatorArr5 = new Animator[4];
                animatorArr5[c3] = ofFloat31;
                animatorArr5[1] = ofFloat32;
                animatorArr5[2] = ofFloat33;
                animatorArr5[3] = ofFloat34;
                animatorSet18.playTogether(animatorArr5);
                animatorSet18.setDuration(150L);
                float[] fArr11 = new float[1];
                fArr11[c3] = 1.0f;
                ObjectAnimator ofFloat35 = ObjectAnimator.ofFloat(this, "exitTransition", fArr11);
                ofFloat35.setDuration(this.messageTransitionIsRunning ? 220L : 360L);
                this.messageTextTranslationX = 0.0f;
                updateMessageTextParams();
                ObjectAnimator ofFloat36 = ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) property22, 1.0f);
                ofFloat36.setStartDelay(this.emojiButtonPaddingAlpha == 1.0f ? 150L : 450L);
                ofFloat36.setDuration(200L);
                this.runningAnimationAudio.playTogether(animatorSet17, animatorSet18, ofFloat36, ofFloat35);
            }
            i2 = i;
            this.runningAnimationAudio.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.64
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator.equals(ChatActivityEnterView.this.runningAnimationAudio)) {
                        if (i2 != 3 && ChatActivityEnterView.this.messageEditText != null && !AndroidUtilities.isAccessibilityScreenReaderEnabled()) {
                            ChatActivityEnterView.this.messageEditText.requestFocus();
                        }
                        ChatActivityEnterView.this.cancelRecordInterfaceInternal();
                        if (i2 != 3) {
                            ControlsView controlsView8 = ChatActivityEnterView.this.controlsView;
                            if (controlsView8 != null) {
                                controlsView8.setVisibility(8);
                            }
                            if (ChatActivityEnterView.this.recordCircle != null) {
                                ChatActivityEnterView.this.recordCircle.setSendButtonInvisible();
                            }
                        }
                    }
                }
            });
            this.runningAnimationAudio.start();
            TimerView timerView8 = this.recordTimerView;
            if (timerView8 != null) {
                timerView8.stop();
            }
        }
        this.delegate.onAudioVideoInterfaceUpdated();
        updateSendAsButton();
        this.lastRecordState = i2;
    }

    public void updateScheduleButton(boolean z) {
        boolean z2;
        ImageView imageView;
        ImageView imageView2;
        ImageView imageView3;
        TLRPC.TL_chatAdminRights tL_chatAdminRights;
        if (DialogObject.isChatDialog(this.dialog_id)) {
            TLRPC.Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
            this.silent = MessagesController.getNotificationsSettings(this.currentAccount).getBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + this.dialog_id, false);
            z2 = ChatObject.isChannel(chat) && (chat.creator || ((tL_chatAdminRights = chat.admin_rights) != null && tL_chatAdminRights.post_messages)) && !chat.megagroup;
            this.canWriteToChannel = z2;
            if (this.notifyButton != null) {
                if (this.notifySilentDrawable == null) {
                    this.notifySilentDrawable = new CrossOutDrawable(getContext(), R.drawable.input_notify_on, Theme.key_chat_messagePanelIcons);
                }
                this.notifySilentDrawable.setCrossOut(this.silent, false);
                this.notifyButton.setImageDrawable(this.notifySilentDrawable);
            } else {
                z2 = false;
            }
            LinearLayout linearLayout = this.attachLayout;
            if (linearLayout != null) {
                updateFieldRight(linearLayout.getVisibility() == 0 ? 1 : 0);
            }
        } else {
            z2 = false;
        }
        boolean z3 = (this.delegate == null || isInScheduleMode() || !this.delegate.hasScheduledMessages()) ? false : true;
        final boolean z4 = (!z3 || this.scheduleButtonHidden || this.recordingAudioVideo) ? false : true;
        if (z4) {
            createScheduledButton();
        }
        ImageView imageView4 = this.scheduledButton;
        float f = 96.0f;
        if (imageView4 == null) {
            ImageView imageView5 = this.notifyButton;
            if (imageView5 != null) {
                int i = (z3 || !z2) ? 8 : 0;
                if (i != imageView5.getVisibility()) {
                    this.notifyButton.setVisibility(i);
                    LinearLayout linearLayout2 = this.attachLayout;
                    if (linearLayout2 != null) {
                        ImageView imageView6 = this.botButton;
                        linearLayout2.setPivotX(AndroidUtilities.dp(((imageView6 == null || imageView6.getVisibility() == 8) && ((imageView = this.notifyButton) == null || imageView.getVisibility() == 8)) ? 48.0f : 96.0f));
                    }
                }
            }
        } else {
            if ((imageView4.getTag() != null && z4) || (this.scheduledButton.getTag() == null && !z4)) {
                if (this.notifyButton != null) {
                    int i2 = (z3 || !z2 || this.scheduledButton.getVisibility() == 0) ? 8 : 0;
                    if (i2 != this.notifyButton.getVisibility()) {
                        this.notifyButton.setVisibility(i2);
                        LinearLayout linearLayout3 = this.attachLayout;
                        if (linearLayout3 != null) {
                            ImageView imageView7 = this.botButton;
                            if ((imageView7 == null || imageView7.getVisibility() == 8) && ((imageView3 = this.notifyButton) == null || imageView3.getVisibility() == 8)) {
                                f = 48.0f;
                            }
                            linearLayout3.setPivotX(AndroidUtilities.dp(f));
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            this.scheduledButton.setTag(z4 ? 1 : null);
        }
        AnimatorSet animatorSet = this.scheduledButtonAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
            this.scheduledButtonAnimation = null;
        }
        if (!z || z2) {
            ImageView imageView8 = this.scheduledButton;
            if (imageView8 != null) {
                imageView8.setVisibility(z4 ? 0 : 8);
                this.scheduledButton.setAlpha(z4 ? 1.0f : 0.0f);
                this.scheduledButton.setScaleX(z4 ? 1.0f : 0.1f);
                this.scheduledButton.setScaleY(z4 ? 1.0f : 0.1f);
                ImageView imageView9 = this.notifyButton;
                if (imageView9 != null) {
                    imageView9.setVisibility((!z2 || this.scheduledButton.getVisibility() == 0) ? 8 : 0);
                }
                ImageView imageView10 = this.giftButton;
                if (imageView10 != null && imageView10.getVisibility() == 0) {
                    this.scheduledButton.setTranslationX(-AndroidUtilities.dp(48.0f));
                }
            } else {
                ImageView imageView11 = this.notifyButton;
                if (imageView11 != null) {
                    imageView11.setVisibility(z2 ? 0 : 8);
                }
            }
        } else {
            ImageView imageView12 = this.scheduledButton;
            if (imageView12 != null) {
                if (z4) {
                    imageView12.setVisibility(0);
                }
                this.scheduledButton.setPivotX(AndroidUtilities.dp(24.0f));
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.scheduledButtonAnimation = animatorSet2;
                animatorSet2.playTogether(ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) View.ALPHA, z4 ? 1.0f : 0.0f), ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) View.SCALE_X, z4 ? 1.0f : 0.1f), ObjectAnimator.ofFloat(this.scheduledButton, (Property<ImageView, Float>) View.SCALE_Y, z4 ? 1.0f : 0.1f));
                this.scheduledButtonAnimation.setDuration(180L);
                this.scheduledButtonAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.67
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ChatActivityEnterView.this.scheduledButtonAnimation = null;
                        if (z4) {
                            return;
                        }
                        ChatActivityEnterView.this.scheduledButton.setVisibility(8);
                    }
                });
                this.scheduledButtonAnimation.start();
            }
        }
        LinearLayout linearLayout4 = this.attachLayout;
        if (linearLayout4 != null) {
            ImageView imageView13 = this.botButton;
            if ((imageView13 == null || imageView13.getVisibility() == 8) && ((imageView2 = this.notifyButton) == null || imageView2.getVisibility() == 8)) {
                f = 48.0f;
            }
            linearLayout4.setPivotX(AndroidUtilities.dp(f));
        }
    }

    public void updateSendAsButton() {
        updateSendAsButton(true);
    }

    /* JADX WARN: Removed duplicated region for block: B:65:0x0156  */
    /* JADX WARN: Removed duplicated region for block: B:99:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateSendAsButton(boolean z) {
        float f;
        float f2;
        SenderSelectView senderSelectView;
        SenderSelectView senderSelectView2;
        String formatString;
        SenderSelectView senderSelectView3;
        FrameLayout frameLayout;
        if (this.parentFragment == null || this.delegate == null) {
            return;
        }
        createMessageEditText();
        TLRPC.Chat chat = this.parentFragment.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
        TLRPC.ChatFull chatFull = this.parentFragment.getMessagesController().getChatFull(-this.dialog_id);
        TLRPC.Peer peer = chatFull != null ? chatFull.default_send_as : null;
        if (peer == null && this.delegate.getSendAsPeers() != null && !this.delegate.getSendAsPeers().peers.isEmpty()) {
            peer = this.delegate.getSendAsPeers().peers.get(0).peer;
        }
        boolean z2 = peer != null && (this.delegate.getSendAsPeers() == null || this.delegate.getSendAsPeers().peers.size() > 1) && !isEditingMessage() && !isRecordingAudioVideo() && (((frameLayout = this.recordedAudioPanel) == null || frameLayout.getVisibility() != 0) && (!ChatObject.isChannelAndNotMegaGroup(chat) || ChatObject.canSendAsPeers(chat)));
        if (z2) {
            createSenderSelectView();
        }
        if (peer != null) {
            if (peer.channel_id != 0) {
                TLRPC.Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(peer.channel_id));
                if (chat2 != null && (senderSelectView3 = this.senderSelectView) != null) {
                    senderSelectView3.setAvatar(chat2);
                    senderSelectView2 = this.senderSelectView;
                    formatString = LocaleController.formatString(R.string.AccDescrSendAs, chat2.title);
                    senderSelectView2.setContentDescription(formatString);
                }
            } else {
                TLRPC.User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(peer.user_id));
                if (user != null && (senderSelectView = this.senderSelectView) != null) {
                    senderSelectView.setAvatar(user);
                    senderSelectView2 = this.senderSelectView;
                    formatString = LocaleController.formatString(R.string.AccDescrSendAs, ContactsController.formatName(user.first_name, user.last_name));
                    senderSelectView2.setContentDescription(formatString);
                }
            }
        }
        SenderSelectView senderSelectView4 = this.senderSelectView;
        boolean z3 = senderSelectView4 != null && senderSelectView4.getVisibility() == 0;
        int dp = AndroidUtilities.dp(2.0f);
        float f3 = z2 ? 0.0f : 1.0f;
        float f4 = z2 ? 1.0f : 0.0f;
        SenderSelectView senderSelectView5 = this.senderSelectView;
        if (senderSelectView5 != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) senderSelectView5.getLayoutParams();
            f = z2 ? ((-this.senderSelectView.getLayoutParams().width) - marginLayoutParams.leftMargin) - dp : 0.0f;
            if (!z2) {
                f2 = ((-this.senderSelectView.getLayoutParams().width) - marginLayoutParams.leftMargin) - dp;
                if (z3 == z2) {
                    SenderSelectView senderSelectView6 = this.senderSelectView;
                    ValueAnimator valueAnimator = senderSelectView6 == null ? null : (ValueAnimator) senderSelectView6.getTag();
                    if (valueAnimator != null) {
                        valueAnimator.cancel();
                        this.senderSelectView.setTag(null);
                    }
                    if (this.parentFragment.getOtherSameChatsDiff() != 0 || !this.parentFragment.fragmentOpened || !z) {
                        if (z2) {
                            createSenderSelectView();
                        }
                        SenderSelectView senderSelectView7 = this.senderSelectView;
                        if (senderSelectView7 != null) {
                            senderSelectView7.setVisibility(z2 ? 0 : 8);
                            this.senderSelectView.setTranslationX(f2);
                        }
                        float f5 = z2 ? f2 : 0.0f;
                        this.emojiButton.setTranslationX(f5);
                        this.messageTextTranslationX = f5;
                        updateMessageTextParams();
                        SenderSelectView senderSelectView8 = this.senderSelectView;
                        if (senderSelectView8 != null) {
                            senderSelectView8.setAlpha(f4);
                            this.senderSelectView.setTag(null);
                            return;
                        }
                        return;
                    }
                    ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(150L);
                    SenderSelectView senderSelectView9 = this.senderSelectView;
                    if (senderSelectView9 != null) {
                        senderSelectView9.setTranslationX(f);
                    }
                    this.messageTextTranslationX = f;
                    updateMessageTextParams();
                    final float f6 = f;
                    final float f7 = f2;
                    final float f8 = f3;
                    final float f9 = f4;
                    duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda14
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                            ChatActivityEnterView.this.lambda$updateSendAsButton$61(f6, f7, f8, f9, valueAnimator2);
                        }
                    });
                    final boolean z4 = z2;
                    final float f10 = f3;
                    final float f11 = f;
                    final float f12 = f2;
                    duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.68
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
                            float f13;
                            if (z4) {
                                ChatActivityEnterView.this.createSenderSelectView();
                            }
                            if (ChatActivityEnterView.this.senderSelectView != null) {
                                ChatActivityEnterView.this.senderSelectView.setVisibility(z4 ? 0 : 8);
                                ChatActivityEnterView.this.senderSelectView.setAlpha(f9);
                                ChatActivityEnterView.this.senderSelectView.setTranslationX(f12);
                                f13 = ChatActivityEnterView.this.senderSelectView.getTranslationX();
                            } else {
                                f13 = 0.0f;
                            }
                            ChatActivityEnterView.this.emojiButton.setTranslationX(f13);
                            ChatActivityEnterView.this.messageTextTranslationX = f13;
                            ChatActivityEnterView.this.updateMessageTextParams();
                            ChatActivityEnterView.this.requestLayout();
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (z4) {
                                return;
                            }
                            if (ChatActivityEnterView.this.senderSelectView != null) {
                                ChatActivityEnterView.this.senderSelectView.setVisibility(8);
                            }
                            ChatActivityEnterView.this.emojiButton.setTranslationX(0.0f);
                            ChatActivityEnterView.this.messageTextTranslationX = 0.0f;
                            ChatActivityEnterView.this.updateMessageTextParams();
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationStart(Animator animator) {
                            float f13;
                            if (z4) {
                                ChatActivityEnterView.this.createSenderSelectView();
                                ChatActivityEnterView.this.senderSelectView.setVisibility(0);
                            }
                            if (ChatActivityEnterView.this.senderSelectView != null) {
                                ChatActivityEnterView.this.senderSelectView.setAlpha(f10);
                                ChatActivityEnterView.this.senderSelectView.setTranslationX(f11);
                                f13 = ChatActivityEnterView.this.senderSelectView.getTranslationX();
                            } else {
                                f13 = 0.0f;
                            }
                            ChatActivityEnterView.this.emojiButton.setTranslationX(f13);
                            ChatActivityEnterView.this.messageTextTranslationX = f13;
                            ChatActivityEnterView.this.updateMessageTextParams();
                            if (ChatActivityEnterView.this.botCommandsMenuButton == null || ChatActivityEnterView.this.botCommandsMenuButton.getTag() != null) {
                                return;
                            }
                            ChatActivityEnterView.this.animationParamsX.clear();
                        }
                    });
                    duration.start();
                    SenderSelectView senderSelectView10 = this.senderSelectView;
                    if (senderSelectView10 != null) {
                        senderSelectView10.setTag(duration);
                        return;
                    }
                    return;
                }
                return;
            }
        } else {
            f = 0.0f;
        }
        f2 = 0.0f;
        if (z3 == z2) {
        }
    }
}
