package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.graphics.drawable.ShapeDrawable;
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
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.MimeTypeMap;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Keep;
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
import androidx.recyclerview.widget.ChatListItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.AnimationNotificationsLocker;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
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
import org.telegram.tgnet.TLRPC$BotInfo;
import org.telegram.tgnet.TLRPC$BotInlineResult;
import org.telegram.tgnet.TLRPC$BotMenuButton;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$ChatFull;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DocumentAttribute;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$InlineQueryPeerType;
import org.telegram.tgnet.TLRPC$InputStickerSet;
import org.telegram.tgnet.TLRPC$KeyboardButton;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$ReplyMarkup;
import org.telegram.tgnet.TLRPC$StickerSet;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_botMenuButton;
import org.telegram.tgnet.TLRPC$TL_channels_sendAsPeers;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_document;
import org.telegram.tgnet.TLRPC$TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC$TL_inlineQueryPeerTypeBotPM;
import org.telegram.tgnet.TLRPC$TL_inlineQueryPeerTypeBroadcast;
import org.telegram.tgnet.TLRPC$TL_inlineQueryPeerTypeChat;
import org.telegram.tgnet.TLRPC$TL_inlineQueryPeerTypeMegagroup;
import org.telegram.tgnet.TLRPC$TL_inlineQueryPeerTypePM;
import org.telegram.tgnet.TLRPC$TL_inputMessageEntityMentionName;
import org.telegram.tgnet.TLRPC$TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC$TL_keyboardButton;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonBuy;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonCallback;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonGame;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonRequestGeoLocation;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonRequestPeer;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonRequestPhone;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonRequestPoll;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonSimpleWebView;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonSwitchInline;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonUrl;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonUrlAuth;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonUserProfile;
import org.telegram.tgnet.TLRPC$TL_keyboardButtonWebView;
import org.telegram.tgnet.TLRPC$TL_message;
import org.telegram.tgnet.TLRPC$TL_messageEntityBlockquote;
import org.telegram.tgnet.TLRPC$TL_messageEntityBold;
import org.telegram.tgnet.TLRPC$TL_messageEntityCode;
import org.telegram.tgnet.TLRPC$TL_messageEntityCustomEmoji;
import org.telegram.tgnet.TLRPC$TL_messageEntityItalic;
import org.telegram.tgnet.TLRPC$TL_messageEntityMentionName;
import org.telegram.tgnet.TLRPC$TL_messageEntityPre;
import org.telegram.tgnet.TLRPC$TL_messageEntitySpoiler;
import org.telegram.tgnet.TLRPC$TL_messageEntityStrike;
import org.telegram.tgnet.TLRPC$TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC$TL_messageEntityUnderline;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC$TL_messages_sendBotRequestedPeer;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_replyKeyboardMarkup;
import org.telegram.tgnet.TLRPC$TL_stickerSetFullCovered;
import org.telegram.tgnet.TLRPC$TL_stickerSetNoCovered;
import org.telegram.tgnet.TLRPC$TL_userStatusEmpty;
import org.telegram.tgnet.TLRPC$TL_userStatusOnline;
import org.telegram.tgnet.TLRPC$TL_userStatusRecently;
import org.telegram.tgnet.TLRPC$TL_webPagePending;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserFull;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.tgnet.TLRPC$WebPage;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuSubItem;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.AdjustPanLayoutHelper;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BotCommandsMenuView;
import org.telegram.ui.Components.BotKeyboardView;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.ChatActivityEnterViewAnimatedIconView;
import org.telegram.ui.Components.EditTextCaption;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.Premium.GiftPremiumBottomSheet;
import org.telegram.ui.Components.Premium.PremiumFeatureBottomSheet;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.SeekBar;
import org.telegram.ui.Components.SenderSelectPopup;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.VideoTimelineView;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.GroupStickersActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.PremiumPreviewFragment;
import org.telegram.ui.ProfileActivity;
import org.telegram.ui.StickersActivity;
import org.telegram.ui.TopicsFragment;
/* loaded from: classes4.dex */
public class ChatActivityEnterView extends BlurredFrameLayout implements NotificationCenter.NotificationCenterDelegate, SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate, StickersAlert.StickersAlertDelegate {
    private final Property<? super View, Float> ATTACH_LAYOUT_ALPHA;
    private final Property<? super View, Float> ATTACH_LAYOUT_TRANSLATION_X;
    private final Property<? super View, Float> EMOJI_BUTTON_ALPHA;
    private final Property<? super View, Float> EMOJI_BUTTON_SCALE;
    private final Property<? super View, Float> MESSAGE_TEXT_TRANSLATION_X;
    private AccountInstance accountInstance;
    private AdjustPanLayoutHelper adjustPanLayoutHelper;
    private boolean allowAnimatedEmoji;
    public boolean allowBlur;
    private boolean allowGifs;
    private boolean allowShowTopView;
    private boolean allowStickers;
    protected int animatedTop;
    private int animatingContentType;
    private Runnable animationEndRunnable;
    private HashMap<View, Float> animationParamsX;
    private ImageView attachButton;
    private LinearLayout attachLayout;
    private float attachLayoutAlpha;
    private float attachLayoutPaddingAlpha;
    private float attachLayoutPaddingTranslationX;
    private float attachLayoutTranslationX;
    private TLRPC$TL_document audioToSend;
    private MessageObject audioToSendMessageObject;
    private String audioToSendPath;
    private FrameLayout audioVideoButtonContainer;
    private ChatActivityEnterViewAnimatedIconView audioVideoSendButton;
    Paint backgroundPaint;
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
    private TLRPC$TL_replyKeyboardMarkup botReplyMarkup;
    private ChatActivityBotWebViewButton botWebViewButton;
    private BotWebViewMenuContainer botWebViewMenuContainer;
    private boolean calledRecordRunnable;
    private Drawable cameraDrawable;
    private Drawable cameraOutline;
    private boolean canWriteToChannel;
    private ImageView cancelBotButton;
    private boolean captionLimitBulletinShown;
    private NumberTextView captionLimitView;
    private float chatSearchExpandOffset;
    private boolean clearBotButtonsOnKeyboardOpen;
    private boolean closeAnimationInProgress;
    private int codePointCount;
    private int commonInputType;
    private float composeShadowAlpha;
    private int currentAccount;
    private int currentLimit;
    private int currentPopupContentType;
    public ValueAnimator currentTopViewAnimation;
    private ChatActivityEnterViewDelegate delegate;
    private boolean destroyed;
    private long dialog_id;
    private float distCanMove;
    private AnimatorSet doneButtonAnimation;
    private ValueAnimator doneButtonColorAnimator;
    private FrameLayout doneButtonContainer;
    boolean doneButtonEnabled;
    private float doneButtonEnabledProgress;
    private ImageView doneButtonImage;
    private ContextProgressView doneButtonProgress;
    private Drawable doneCheckDrawable;
    private Paint dotPaint;
    private CharSequence draftMessage;
    private boolean draftSearchWebpage;
    private boolean editingCaption;
    private MessageObject editingMessageObject;
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
    private ImageView expandStickersButton;
    private Runnable focusRunnable;
    private boolean forceShowSendButton;
    private ImageView giftButton;
    private boolean hasBotCommands;
    private boolean hasRecordVideo;
    private Runnable hideKeyboardRunnable;
    private float horizontalPadding;
    private boolean ignoreTextChange;
    private Drawable inactinveSendButtonDrawable;
    private TLRPC$ChatFull info;
    private int innerTextChange;
    private final boolean isChat;
    private boolean isInVideoMode;
    private boolean isInitLineCount;
    private boolean isPaste;
    private boolean isPaused;
    private int keyboardHeight;
    private int keyboardHeightLand;
    private boolean keyboardVisible;
    private LongSparseArray<TLRPC$BotInfo> lastBotInfo;
    private int lastSizeChangeValue1;
    private boolean lastSizeChangeValue2;
    private long lastTypingTimeSend;
    private int lineCount;
    private int[] location;
    private Drawable lockShadowDrawable;
    private View.AccessibilityDelegate mediaMessageButtonsDelegate;
    protected EditTextCaption messageEditText;
    protected FrameLayout messageEditTextContainer;
    private boolean messageEditTextEnabled;
    private ArrayList<TextWatcher> messageEditTextWatchers;
    private float messageTextPaddingTranslationX;
    private float messageTextTranslationX;
    boolean messageTransitionIsRunning;
    private TLRPC$WebPage messageWebPage;
    private boolean messageWebPageSearch;
    private Drawable micDrawable;
    private Drawable micOutline;
    private Runnable moveToSendStateRunnable;
    private boolean needShowTopView;
    private AnimationNotificationsLocker notificationsLocker;
    private ImageView notifyButton;
    private CrossOutDrawable notifySilentDrawable;
    private Runnable onEmojiSearchClosed;
    private Runnable onFinishInitCameraRunnable;
    private Runnable onKeyboardClosed;
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
    private TLRPC$KeyboardButton pendingLocationButton;
    private MessageObject pendingMessageObject;
    private MediaActionDrawable playPauseDrawable;
    private int popupX;
    private int popupY;
    public boolean preventInput;
    private CloseProgressDrawable2 progressDrawable;
    private ImageView reactionsButton;
    private Runnable recordAudioVideoRunnable;
    private boolean recordAudioVideoRunnableStarted;
    private RecordCircle recordCircle;
    private Property<RecordCircle, Float> recordCircleScale;
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
    private SeekBarWaveformView recordedAudioSeekBar;
    private TextView recordedAudioTimeTextView;
    private boolean recordingAudioVideo;
    public int recordingGuid;
    private android.graphics.Rect rect;
    private Paint redDotPaint;
    private boolean removeEmojiViewAfterAnimation;
    private MessageObject replyingMessageObject;
    private ChatActivity.ReplyQuote replyingQuote;
    private final Theme.ResourcesProvider resourcesProvider;
    private Property<View, Integer> roundedTranslationYProperty;
    private Runnable runEmojiPanelAnimation;
    private AnimatorSet runningAnimation;
    private AnimatorSet runningAnimation2;
    private AnimatorSet runningAnimationAudio;
    private int runningAnimationType;
    private boolean scheduleButtonHidden;
    private ImageView scheduledButton;
    private AnimatorSet scheduledButtonAnimation;
    private ValueAnimator searchAnimator;
    private float searchToOpenProgress;
    private int searchingType;
    private SeekBarWaveform seekBarWaveform;
    private View sendButton;
    private int sendButtonBackgroundColor;
    private FrameLayout sendButtonContainer;
    private Drawable sendButtonDrawable;
    private boolean sendButtonEnabled;
    private Drawable sendButtonInverseDrawable;
    private boolean sendByEnter;
    private Drawable sendDrawable;
    private boolean sendPlainEnabled;
    private ActionBarPopupWindow.ActionBarPopupWindowLayout sendPopupLayout;
    private ActionBarPopupWindow sendPopupWindow;
    private android.graphics.Rect sendRect;
    private boolean sendRoundEnabled;
    private boolean sendVoiceEnabled;
    private ActionBarMenuSubItem sendWhenOnlineButton;
    private SenderSelectPopup senderSelectPopupWindow;
    private SenderSelectView senderSelectView;
    private Runnable setTextFieldRunnable;
    protected boolean shouldAnimateEditTextWithBounds;
    public boolean shouldDrawBackground;
    public boolean shouldDrawRecordedAudioPanelInParent;
    private boolean showKeyboardOnResume;
    private Runnable showTopViewRunnable;
    private boolean silent;
    private SizeNotifierFrameLayout sizeNotifierLayout;
    private SlideTextView slideText;
    private SimpleTextView slowModeButton;
    private int slowModeTimer;
    private boolean smoothKeyboard;
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
    protected View topLineView;
    protected View topView;
    protected float topViewEnterProgress;
    protected boolean topViewShowed;
    private final ValueAnimator.AnimatorUpdateListener topViewUpdateListener;
    private TrendingStickersAlert trendingStickersAlert;
    private Runnable updateExpandabilityRunnable;
    private Runnable updateSlowModeRunnable;
    private TLRPC$UserFull userInfo;
    private VideoTimelineView videoTimelineView;
    private VideoEditedInfo videoToSendMessageObject;
    private boolean waitingForKeyboardOpen;
    private boolean waitingForKeyboardOpenAfterAnimation;
    private PowerManager.WakeLock wakeLock;
    private boolean wasSendTyping;

    /* loaded from: classes4.dex */
    public enum BotMenuButtonType {
        NO_BUTTON,
        COMMANDS,
        WEB_VIEW
    }

    /* loaded from: classes4.dex */
    public interface ChatActivityEnterViewDelegate {

        /* loaded from: classes4.dex */
        public final /* synthetic */ class -CC {
            public static void $default$bottomPanelTranslationYChanged(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate, float f) {
            }

            public static int $default$getContentViewHeight(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return 0;
            }

            public static ChatActivity.ReplyQuote $default$getReplyQuote(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return null;
            }

            public static TL_stories$StoryItem $default$getReplyToStory(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
                return null;
            }

            public static TLRPC$TL_channels_sendAsPeers $default$getSendAsPeers(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
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

            public static void $default$openScheduledMessages(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }

            public static void $default$prepareMessageSending(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }

            public static void $default$scrollToSendingMessage(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
            }
        }

        void bottomPanelTranslationYChanged(float f);

        void didPressAttachButton();

        int getContentViewHeight();

        ChatActivity.ReplyQuote getReplyQuote();

        TL_stories$StoryItem getReplyToStory();

        TLRPC$TL_channels_sendAsPeers getSendAsPeers();

        boolean hasForwardingMessages();

        boolean hasScheduledMessages();

        int measureKeyboardHeight();

        void needChangeVideoPreviewState(int i, float f);

        void needSendTyping();

        void needShowMediaBanHint();

        void needStartRecordAudio(int i);

        void needStartRecordVideo(int i, boolean z, int i2);

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

        void openScheduledMessages();

        void prepareMessageSending();

        void scrollToSendingMessage();
    }

    public static /* synthetic */ boolean lambda$createRecordPanel$40(View view, MotionEvent motionEvent) {
        return true;
    }

    public void checkAnimation() {
    }

    public void extendActionMode(Menu menu) {
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public void isRecordingStateChanged() {
    }

    protected void onLineCountChanged(int i, int i2) {
    }

    protected boolean pannelAnimationEnabled() {
        return true;
    }

    protected boolean showConfirmAlert(Runnable runnable) {
        return false;
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

    /* loaded from: classes4.dex */
    public class SeekBarWaveformView extends View {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SeekBarWaveformView(Context context) {
            super(context);
            ChatActivityEnterView.this = r2;
            r2.seekBarWaveform = new SeekBarWaveform(context);
            r2.seekBarWaveform.setDelegate(new SeekBar.SeekBarDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$SeekBarWaveformView$$ExternalSyntheticLambda0
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
            });
        }

        public /* synthetic */ void lambda$new$0(float f) {
            if (ChatActivityEnterView.this.audioToSendMessageObject != null) {
                ChatActivityEnterView.this.audioToSendMessageObject.audioProgress = f;
                MediaController.getInstance().seekToProgress(ChatActivityEnterView.this.audioToSendMessageObject, f);
            }
        }

        public void setWaveform(byte[] bArr) {
            ChatActivityEnterView.this.seekBarWaveform.setWaveform(bArr);
            invalidate();
        }

        public void setProgress(float f) {
            ChatActivityEnterView.this.seekBarWaveform.setProgress(f);
            invalidate();
        }

        public boolean isDragging() {
            return ChatActivityEnterView.this.seekBarWaveform.isDragging();
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

        @Override // android.view.View
        protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
            super.onLayout(z, i, i2, i3, i4);
            ChatActivityEnterView.this.seekBarWaveform.setSize((int) ((i3 - i) - (ChatActivityEnterView.this.horizontalPadding * 2.0f)), i4 - i2);
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
    }

    /* loaded from: classes4.dex */
    public class RecordDot extends View {
        private float alpha;
        boolean attachedToWindow;
        RLottieDrawable drawable;
        private boolean enterAnimation;
        private boolean isIncr;
        private long lastUpdateTime;
        boolean playing;

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

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public RecordDot(Context context) {
            super(context);
            ChatActivityEnterView.this = r8;
            int i = R.raw.chat_audio_record_delete;
            RLottieDrawable rLottieDrawable = new RLottieDrawable(i, "" + i, AndroidUtilities.dp(28.0f), AndroidUtilities.dp(28.0f), false, null);
            this.drawable = rLottieDrawable;
            rLottieDrawable.setCurrentParentView(this);
            this.drawable.setInvalidateOnProgressSet(true);
            updateColors();
        }

        public void updateColors() {
            int themedColor = ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordedVoiceDot);
            int blendARGB = ColorUtils.blendARGB(themedColor, ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelBackground), 0.5f);
            ChatActivityEnterView.this.redDotPaint.setColor(themedColor);
            this.drawable.beginApplyLayerColors();
            this.drawable.setLayerColor("Cup Red.**", themedColor);
            this.drawable.setLayerColor("Box.**", themedColor);
            this.drawable.setLayerColor("Line 1.**", blendARGB);
            this.drawable.setLayerColor("Line 2.**", blendARGB);
            this.drawable.setLayerColor("Line 3.**", blendARGB);
            this.drawable.commitApplyLayerColors();
            if (ChatActivityEnterView.this.playPauseDrawable != null) {
                ChatActivityEnterView.this.playPauseDrawable.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordedVoicePlayPause));
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

        @Override // android.view.View
        protected void onMeasure(int i, int i2) {
            super.onMeasure(i, i2);
            this.drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
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
            } else if (!this.isIncr && !this.playing) {
                float f = this.alpha - (((float) currentTimeMillis) / 600.0f);
                this.alpha = f;
                if (f <= 0.0f) {
                    this.alpha = 0.0f;
                    this.isIncr = true;
                }
            } else {
                float f2 = this.alpha + (((float) currentTimeMillis) / 600.0f);
                this.alpha = f2;
                if (f2 >= 1.0f) {
                    this.alpha = 1.0f;
                    this.isIncr = false;
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

        public void playDeleteAnimation() {
            this.playing = true;
            this.drawable.setProgress(0.0f);
            if (this.attachedToWindow) {
                this.drawable.start();
            }
        }
    }

    /* loaded from: classes4.dex */
    public class RecordCircle extends View {
        private float amplitude;
        private float animateAmplitudeDiff;
        private float animateToAmplitude;
        BlobDrawable bigWaveDrawable;
        private boolean canceledByGesture;
        private float circleRadius;
        private float circleRadiusAmplitude;
        public float drawingCircleRadius;
        public float drawingCx;
        public float drawingCy;
        private float exitTransition;
        float idleProgress;
        boolean incIdle;
        private float lastMovingX;
        private float lastMovingY;
        private int lastSize;
        private long lastUpdateTime;
        private float lockAnimatedTranslation;
        Paint lockBackgroundPaint;
        Paint lockOutlinePaint;
        Paint lockPaint;
        private Paint p;
        private int paintAlpha;
        Path path;
        private boolean pressed;
        private float progressToSeekbarStep3;
        private float progressToSendButton;
        RectF rectF;
        private float scale;
        private boolean sendButtonVisible;
        private boolean showTooltip;
        private long showTooltipStartTime;
        private boolean showWaves;
        public boolean skipDraw;
        private int slideDelta;
        private float slideToCancelLockProgress;
        private float slideToCancelProgress;
        private float snapAnimationProgress;
        private float startTranslation;
        BlobDrawable tinyWaveDrawable;
        private float tooltipAlpha;
        private Drawable tooltipBackground;
        private Drawable tooltipBackgroundArrow;
        private StaticLayout tooltipLayout;
        private String tooltipMessage;
        private TextPaint tooltipPaint;
        private float tooltipWidth;
        private float touchSlop;
        private float transformToSeekbar;
        private VirtualViewHelper virtualViewHelper;
        public boolean voiceEnterTransitionInProgress;
        private float wavesEnterAnimation;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public RecordCircle(Context context) {
            super(context);
            ChatActivityEnterView.this = r5;
            this.tinyWaveDrawable = new BlobDrawable(11, LiteMode.FLAGS_CHAT);
            this.bigWaveDrawable = new BlobDrawable(12, LiteMode.FLAGS_CHAT);
            this.tooltipPaint = new TextPaint(1);
            this.circleRadius = AndroidUtilities.dpf2(41.0f);
            this.circleRadiusAmplitude = AndroidUtilities.dp(30.0f);
            this.lockBackgroundPaint = new Paint(1);
            this.lockPaint = new Paint(1);
            this.lockOutlinePaint = new Paint(1);
            this.rectF = new RectF();
            this.path = new Path();
            this.wavesEnterAnimation = 0.0f;
            this.showWaves = true;
            this.p = new Paint(1);
            VirtualViewHelper virtualViewHelper = new VirtualViewHelper(this);
            this.virtualViewHelper = virtualViewHelper;
            ViewCompat.setAccessibilityDelegate(this, virtualViewHelper);
            this.tinyWaveDrawable.minRadius = AndroidUtilities.dp(47.0f);
            this.tinyWaveDrawable.maxRadius = AndroidUtilities.dp(55.0f);
            this.tinyWaveDrawable.generateBlob();
            this.bigWaveDrawable.minRadius = AndroidUtilities.dp(47.0f);
            this.bigWaveDrawable.maxRadius = AndroidUtilities.dp(55.0f);
            this.bigWaveDrawable.generateBlob();
            this.lockOutlinePaint.setStyle(Paint.Style.STROKE);
            this.lockOutlinePaint.setStrokeCap(Paint.Cap.ROUND);
            this.lockOutlinePaint.setStrokeWidth(AndroidUtilities.dpf2(1.7f));
            r5.lockShadowDrawable = getResources().getDrawable(R.drawable.lock_round_shadow);
            r5.lockShadowDrawable.setColorFilter(new PorterDuffColorFilter(r5.getThemedColor(Theme.key_chat_messagePanelVoiceLockShadow), PorterDuff.Mode.MULTIPLY));
            this.tooltipBackground = Theme.createRoundRectDrawable(AndroidUtilities.dp(5.0f), r5.getThemedColor(Theme.key_chat_gifSaveHintBackground));
            this.tooltipPaint.setTextSize(AndroidUtilities.dp(14.0f));
            this.tooltipBackgroundArrow = ContextCompat.getDrawable(context, R.drawable.tooltip_arrow);
            this.tooltipMessage = LocaleController.getString("SlideUpToLock", R.string.SlideUpToLock);
            float scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            this.touchSlop = scaledTouchSlop;
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
            drawable.setColorFilter(new PorterDuffColorFilter(chatActivityEnterView.getThemedColor(i), PorterDuff.Mode.MULTIPLY));
            ChatActivityEnterView.this.cameraDrawable = getResources().getDrawable(R.drawable.input_video_pressed).mutate();
            ChatActivityEnterView.this.cameraDrawable.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(i), PorterDuff.Mode.MULTIPLY));
            ChatActivityEnterView.this.sendDrawable = getResources().getDrawable(R.drawable.attach_send).mutate();
            ChatActivityEnterView.this.sendDrawable.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(i), PorterDuff.Mode.MULTIPLY));
            ChatActivityEnterView.this.micOutline = getResources().getDrawable(R.drawable.input_mic).mutate();
            Drawable drawable2 = ChatActivityEnterView.this.micOutline;
            ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
            int i2 = Theme.key_chat_messagePanelIcons;
            drawable2.setColorFilter(new PorterDuffColorFilter(chatActivityEnterView2.getThemedColor(i2), PorterDuff.Mode.MULTIPLY));
            ChatActivityEnterView.this.cameraOutline = getResources().getDrawable(R.drawable.input_video).mutate();
            ChatActivityEnterView.this.cameraOutline.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(i2), PorterDuff.Mode.MULTIPLY));
        }

        public void setAmplitude(double d) {
            this.bigWaveDrawable.setValue((float) (Math.min(1800.0d, d) / 1800.0d), true);
            this.tinyWaveDrawable.setValue((float) (Math.min(1800.0d, d) / 1800.0d), false);
            float min = (float) (Math.min(1800.0d, d) / 1800.0d);
            this.animateToAmplitude = min;
            this.animateAmplitudeDiff = (min - this.amplitude) / 375.0f;
            invalidate();
        }

        public float getScale() {
            return this.scale;
        }

        @Keep
        public void setScale(float f) {
            this.scale = f;
            invalidate();
        }

        @Keep
        public void setLockAnimatedTranslation(float f) {
            this.lockAnimatedTranslation = f;
            invalidate();
        }

        @Keep
        public void setSnapAnimationProgress(float f) {
            this.snapAnimationProgress = f;
            invalidate();
        }

        @Keep
        public float getLockAnimatedTranslation() {
            return this.lockAnimatedTranslation;
        }

        public boolean isSendButtonVisible() {
            return this.sendButtonVisible;
        }

        public void setSendButtonInvisible() {
            this.sendButtonVisible = false;
            invalidate();
        }

        public int setLockTranslation(float f) {
            if (f == 10000.0f) {
                this.sendButtonVisible = false;
                this.lockAnimatedTranslation = -1.0f;
                this.startTranslation = -1.0f;
                invalidate();
                this.snapAnimationProgress = 0.0f;
                this.transformToSeekbar = 0.0f;
                this.exitTransition = 0.0f;
                this.scale = 0.0f;
                this.tooltipAlpha = 0.0f;
                this.showTooltip = false;
                this.progressToSendButton = 0.0f;
                this.slideToCancelProgress = 1.0f;
                this.slideToCancelLockProgress = 1.0f;
                this.canceledByGesture = false;
                return 0;
            } else if (this.sendButtonVisible) {
                return 2;
            } else {
                if (this.lockAnimatedTranslation == -1.0f) {
                    this.startTranslation = f;
                }
                this.lockAnimatedTranslation = f;
                invalidate();
                if (this.canceledByGesture || this.slideToCancelProgress < 0.7f || this.startTranslation - this.lockAnimatedTranslation < AndroidUtilities.dp(57.0f)) {
                    return 1;
                }
                this.sendButtonVisible = true;
                return 2;
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (this.sendButtonVisible) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                if (motionEvent.getAction() == 0) {
                    boolean contains = ChatActivityEnterView.this.pauseRect.contains(x, y);
                    this.pressed = contains;
                    return contains;
                } else if (this.pressed) {
                    if (motionEvent.getAction() == 1 && ChatActivityEnterView.this.pauseRect.contains(x, y)) {
                        if (ChatActivityEnterView.this.isInVideoMode()) {
                            ChatActivityEnterView.this.delegate.needStartRecordVideo(3, true, 0);
                        } else {
                            MediaController.getInstance().stopRecording(2, true, 0);
                            ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                        }
                        if (ChatActivityEnterView.this.slideText != null) {
                            ChatActivityEnterView.this.slideText.setEnabled(false);
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        @Override // android.view.View
        @SuppressLint({"DrawAllocation"})
        protected void onMeasure(int i, int i2) {
            int size = View.MeasureSpec.getSize(i);
            int dp = AndroidUtilities.dp(194.0f);
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
            StaticLayout staticLayout2 = this.tooltipLayout;
            if (staticLayout2 != null && staticLayout2.getLineCount() > 1) {
                dp += this.tooltipLayout.getHeight() - this.tooltipLayout.getLineBottom(0);
            }
            super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(dp, 1073741824));
            float measuredWidth = getMeasuredWidth() * 0.35f;
            if (measuredWidth > AndroidUtilities.dp(140.0f)) {
                measuredWidth = AndroidUtilities.dp(140.0f);
            }
            this.slideDelta = (int) ((-measuredWidth) * (1.0f - this.slideToCancelProgress));
        }

        /* JADX WARN: Removed duplicated region for block: B:410:0x058c  */
        /* JADX WARN: Removed duplicated region for block: B:415:0x05f0  */
        /* JADX WARN: Removed duplicated region for block: B:418:0x0667  */
        /* JADX WARN: Removed duplicated region for block: B:422:0x067a  */
        /* JADX WARN: Removed duplicated region for block: B:427:0x068a  */
        /* JADX WARN: Removed duplicated region for block: B:436:0x06a3  */
        /* JADX WARN: Removed duplicated region for block: B:441:0x06c1  */
        /* JADX WARN: Removed duplicated region for block: B:446:0x06ee  */
        /* JADX WARN: Removed duplicated region for block: B:447:0x0819  */
        /* JADX WARN: Removed duplicated region for block: B:451:0x0844  */
        /* JADX WARN: Removed duplicated region for block: B:452:0x0849  */
        /* JADX WARN: Removed duplicated region for block: B:461:0x085d  */
        /* JADX WARN: Removed duplicated region for block: B:471:0x0881  */
        /* JADX WARN: Removed duplicated region for block: B:477:0x08b0  */
        /* JADX WARN: Removed duplicated region for block: B:480:0x09ab  */
        /* JADX WARN: Removed duplicated region for block: B:483:0x09ba  */
        /* JADX WARN: Removed duplicated region for block: B:489:0x0a90  */
        /* JADX WARN: Removed duplicated region for block: B:494:0x0acc  */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        protected void onDraw(Canvas canvas) {
            float f;
            float f2;
            float f3;
            float f4;
            float f5;
            float f6;
            float f7;
            Drawable drawable;
            Drawable drawable2;
            long j;
            float f8;
            float f9;
            float f10;
            Drawable drawable3;
            float dp;
            float dp2;
            float dpf2;
            float dpf22;
            float f11;
            float f12;
            float f13;
            float f14;
            float f15;
            Drawable drawable4;
            Drawable drawable5;
            float f16;
            float dpf23;
            float dpf24;
            float f17;
            Drawable drawable6;
            if (this.skipDraw) {
                return;
            }
            StaticLayout staticLayout = this.tooltipLayout;
            float height = (staticLayout == null || staticLayout.getLineCount() <= 1) ? 0.0f : this.tooltipLayout.getHeight() - this.tooltipLayout.getLineBottom(0);
            int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp2(26.0f);
            int dp3 = (int) (AndroidUtilities.dp(170.0f) + height);
            this.drawingCx = this.slideDelta + measuredWidth;
            float f18 = dp3;
            this.drawingCy = f18;
            float f19 = this.lockAnimatedTranslation;
            if (f19 != 10000.0f) {
                float max = Math.max(0, (int) (this.startTranslation - f19));
                if (max > AndroidUtilities.dp(57.0f)) {
                    max = AndroidUtilities.dp(57.0f);
                }
                f = max;
            } else {
                f = 0.0f;
            }
            float f20 = this.scale;
            float f21 = f20 <= 0.5f ? f20 / 0.5f : f20 <= 0.75f ? 1.0f - (((f20 - 0.5f) / 0.25f) * 0.1f) : (((f20 - 0.75f) / 0.25f) * 0.1f) + 0.9f;
            long currentTimeMillis = System.currentTimeMillis() - this.lastUpdateTime;
            float f22 = this.animateToAmplitude;
            float f23 = this.amplitude;
            if (f22 != f23) {
                float f24 = this.animateAmplitudeDiff;
                float f25 = f23 + (((float) currentTimeMillis) * f24);
                this.amplitude = f25;
                if (f24 > 0.0f) {
                    if (f25 > f22) {
                        this.amplitude = f22;
                    }
                } else if (f25 < f22) {
                    this.amplitude = f22;
                }
                invalidate();
            }
            if (this.canceledByGesture) {
                f2 = CubicBezierInterpolator.EASE_OUT.getInterpolation(1.0f - this.slideToCancelProgress) * 0.7f;
            } else {
                f2 = (this.slideToCancelProgress * 0.3f) + 0.7f;
            }
            float f26 = (this.circleRadius + (this.circleRadiusAmplitude * this.amplitude)) * f21 * f2;
            this.progressToSeekbarStep3 = 0.0f;
            if (this.transformToSeekbar != 0.0f && ChatActivityEnterView.this.recordedAudioBackground != null) {
                float f27 = this.transformToSeekbar;
                float f28 = f27 <= 0.38f ? f27 / 0.38f : 1.0f;
                float max2 = f27 > 0.63f ? 1.0f : Math.max(0.0f, (f27 - 0.38f) / 0.25f);
                this.progressToSeekbarStep3 = Math.max(0.0f, ((this.transformToSeekbar - 0.38f) - 0.25f) / 0.37f);
                CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_BOTH;
                float interpolation = cubicBezierInterpolator.getInterpolation(f28);
                float interpolation2 = cubicBezierInterpolator.getInterpolation(max2);
                this.progressToSeekbarStep3 = cubicBezierInterpolator.getInterpolation(this.progressToSeekbarStep3);
                float measuredHeight = ChatActivityEnterView.this.recordedAudioBackground.getMeasuredHeight() / 2.0f;
                f7 = interpolation;
                f3 = (((f26 + (AndroidUtilities.dp(16.0f) * interpolation)) - measuredHeight) * (1.0f - interpolation2)) + measuredHeight;
                f5 = interpolation2;
                f4 = 1.0f;
                f6 = 0.0f;
            } else {
                float f29 = this.exitTransition;
                if (f29 != 0.0f) {
                    float f30 = f29 > 0.6f ? 1.0f : f29 / 0.6f;
                    if (!ChatActivityEnterView.this.messageTransitionIsRunning) {
                        f29 = Math.max(0.0f, (f29 - 0.6f) / 0.4f);
                    }
                    CubicBezierInterpolator cubicBezierInterpolator2 = CubicBezierInterpolator.EASE_BOTH;
                    float interpolation3 = cubicBezierInterpolator2.getInterpolation(f30);
                    float interpolation4 = cubicBezierInterpolator2.getInterpolation(f29);
                    float dp4 = (f26 + (AndroidUtilities.dp(16.0f) * interpolation3)) * (1.0f - interpolation4);
                    if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                        float f31 = this.exitTransition;
                        if (f31 > 0.6f) {
                            f4 = Math.max(0.0f, 1.0f - ((f31 - 0.6f) / 0.4f));
                            f6 = interpolation4;
                            f3 = dp4;
                            f7 = interpolation3;
                            f5 = 0.0f;
                        }
                    }
                    f6 = interpolation4;
                    f3 = dp4;
                    f7 = interpolation3;
                    f4 = 1.0f;
                    f5 = 0.0f;
                } else {
                    f3 = f26;
                    f4 = 1.0f;
                    f5 = 0.0f;
                    f6 = 0.0f;
                    f7 = 0.0f;
                }
            }
            if (this.canceledByGesture) {
                float f32 = this.slideToCancelProgress;
                if (f32 > 0.7f) {
                    f4 *= 1.0f - ((f32 - 0.7f) / 0.3f);
                }
            }
            if (this.progressToSeekbarStep3 > 0.0f) {
                ChatActivityEnterView.this.paint.setColor(ColorUtils.blendARGB(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelVoiceBackground), ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordedVoiceBackground), this.progressToSeekbarStep3));
            } else {
                ChatActivityEnterView.this.paint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelVoiceBackground));
            }
            Drawable drawable7 = null;
            checkDrawables();
            if (isSendButtonVisible()) {
                float f33 = this.progressToSendButton;
                if (f33 != 1.0f) {
                    float f34 = f33 + (((float) currentTimeMillis) / 150.0f);
                    this.progressToSendButton = f34;
                    if (f34 > 1.0f) {
                        this.progressToSendButton = 1.0f;
                    }
                    drawable7 = ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.cameraDrawable : ChatActivityEnterView.this.micDrawable;
                }
                drawable = ChatActivityEnterView.this.sendDrawable;
            } else {
                drawable = ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.cameraDrawable : ChatActivityEnterView.this.micDrawable;
            }
            Drawable drawable8 = drawable7;
            Drawable drawable9 = drawable;
            float f35 = height;
            float f36 = f3;
            float f37 = f4;
            ChatActivityEnterView.this.sendRect.set(measuredWidth - (drawable9.getIntrinsicWidth() / 2), dp3 - (drawable9.getIntrinsicHeight() / 2), measuredWidth + (drawable9.getIntrinsicWidth() / 2), dp3 + (drawable9.getIntrinsicHeight() / 2));
            drawable9.setBounds(ChatActivityEnterView.this.sendRect);
            if (drawable8 != null) {
                drawable8.setBounds(measuredWidth - (drawable8.getIntrinsicWidth() / 2), dp3 - (drawable8.getIntrinsicHeight() / 2), (drawable8.getIntrinsicWidth() / 2) + measuredWidth, dp3 + (drawable8.getIntrinsicHeight() / 2));
            }
            float dp5 = 1.0f - (f / AndroidUtilities.dp(57.0f));
            if (this.incIdle) {
                float f38 = this.idleProgress + 0.01f;
                this.idleProgress = f38;
                if (f38 > 1.0f) {
                    this.incIdle = false;
                    this.idleProgress = 1.0f;
                }
            } else {
                float f39 = this.idleProgress - 0.01f;
                this.idleProgress = f39;
                if (f39 < 0.0f) {
                    this.incIdle = true;
                    this.idleProgress = 0.0f;
                }
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
            float f40 = this.slideToCancelProgress;
            float f41 = f40 > 0.7f ? 1.0f : f40 / 0.7f;
            if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT) && f5 != 1.0f && f6 < 0.4f && f41 > 0.0f && !this.canceledByGesture) {
                if (this.showWaves) {
                    float f42 = this.wavesEnterAnimation;
                    if (f42 != 1.0f) {
                        float f43 = f42 + 0.04f;
                        this.wavesEnterAnimation = f43;
                        if (f43 > 1.0f) {
                            this.wavesEnterAnimation = 1.0f;
                        }
                    }
                }
                if (!this.voiceEnterTransitionInProgress) {
                    float interpolation5 = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.wavesEnterAnimation);
                    canvas.save();
                    float f44 = 1.0f - f7;
                    float f45 = this.scale * f44 * f41 * interpolation5 * (BlobDrawable.SCALE_BIG_MIN + (this.bigWaveDrawable.amplitude * 1.4f));
                    canvas.scale(f45, f45, this.slideDelta + measuredWidth, f18);
                    BlobDrawable blobDrawable3 = this.bigWaveDrawable;
                    blobDrawable3.draw(this.slideDelta + measuredWidth, f18, canvas, blobDrawable3.paint);
                    canvas.restore();
                    float f46 = this.scale * f44 * f41 * interpolation5 * (BlobDrawable.SCALE_SMALL_MIN + (this.tinyWaveDrawable.amplitude * 1.4f));
                    canvas.save();
                    canvas.scale(f46, f46, this.slideDelta + measuredWidth, f18);
                    BlobDrawable blobDrawable4 = this.tinyWaveDrawable;
                    blobDrawable4.draw(this.slideDelta + measuredWidth, f18, canvas, blobDrawable4.paint);
                    canvas.restore();
                }
            }
            if (!this.voiceEnterTransitionInProgress) {
                ChatActivityEnterView.this.paint.setAlpha((int) (this.paintAlpha * f37));
                if (this.scale == 1.0f) {
                    if (this.transformToSeekbar == 0.0f) {
                        drawable6 = drawable8;
                        j = currentTimeMillis;
                        f8 = dp5;
                        f9 = f;
                        f10 = f36;
                        canvas.drawCircle(this.slideDelta + measuredWidth, f18, f10, ChatActivityEnterView.this.paint);
                    } else if (this.progressToSeekbarStep3 <= 0.0f || ChatActivityEnterView.this.recordedAudioBackground == null) {
                        drawable6 = drawable8;
                        j = currentTimeMillis;
                        f8 = dp5;
                        f9 = f;
                        f10 = f36;
                        canvas.drawCircle(this.slideDelta + measuredWidth, f18, f10, ChatActivityEnterView.this.paint);
                    } else {
                        float f47 = f18 + f36;
                        float f48 = f18 - f36;
                        int i = this.slideDelta;
                        float f49 = measuredWidth + i + f36;
                        float f50 = (i + measuredWidth) - f36;
                        View view = ChatActivityEnterView.this.recordedAudioBackground;
                        drawable6 = drawable8;
                        j = currentTimeMillis;
                        int i2 = 0;
                        int i3 = 0;
                        f8 = dp5;
                        for (View view2 = (View) view.getParent(); view2 != getParent(); view2 = (View) view2.getParent()) {
                            i2 = (int) (i2 + view2.getY());
                            i3 = (int) (i3 + view2.getX());
                        }
                        float f51 = i2;
                        float y = (view.getY() + f51) - getY();
                        f9 = f;
                        float y2 = ((view.getY() + view.getMeasuredHeight()) + f51) - getY();
                        float f52 = i3;
                        float x = (((view.getX() + view.getMeasuredWidth()) + f52) - getX()) - ChatActivityEnterView.this.horizontalPadding;
                        float x2 = ((view.getX() + f52) - getX()) + ChatActivityEnterView.this.horizontalPadding;
                        float measuredHeight2 = ChatActivityEnterView.this.isInVideoMode() ? 0.0f : view.getMeasuredHeight() / 2.0f;
                        float f53 = this.progressToSeekbarStep3;
                        float f54 = measuredHeight2 + ((f36 - measuredHeight2) * (1.0f - f53));
                        this.rectF.set(x2 + ((f50 - x2) * (1.0f - f53)), y + ((f48 - y) * (1.0f - f53)), x + ((f49 - x) * (1.0f - f53)), y2 + ((f47 - y2) * (1.0f - f53)));
                        canvas.drawRoundRect(this.rectF, f54, f54, ChatActivityEnterView.this.paint);
                        f10 = f36;
                    }
                    canvas.save();
                    canvas.translate(this.slideDelta, 0.0f);
                    drawable3 = drawable6;
                    drawable2 = drawable9;
                    drawIconInternal(canvas, drawable9, drawable3, this.progressToSendButton, (int) ((1.0f - f5) * (1.0f - f6) * 255.0f));
                    canvas.restore();
                    if (!isSendButtonVisible()) {
                        dp = AndroidUtilities.dp(36.0f);
                        dp2 = (((AndroidUtilities.dp(60.0f) + f35) + (AndroidUtilities.dpf2(30.0f) * (1.0f - f21))) - f9) + (AndroidUtilities.dpf2(14.0f) * f8);
                        float f55 = (dp / 2.0f) + dp2;
                        dpf2 = (f55 - AndroidUtilities.dpf2(8.0f)) + AndroidUtilities.dpf2(2.0f);
                        dpf22 = (f55 - AndroidUtilities.dpf2(16.0f)) + AndroidUtilities.dpf2(2.0f);
                        float f56 = f8 > 0.4f ? 1.0f : f8 / 0.4f;
                        float f57 = this.snapAnimationProgress;
                        f11 = (((1.0f - f8) * 9.0f) * (1.0f - f57)) - ((f57 * 15.0f) * (1.0f - f56));
                        f12 = f8;
                    } else {
                        dp = AndroidUtilities.dp(36.0f) + ((int) (AndroidUtilities.dp(14.0f) * f8));
                        dp2 = (((AndroidUtilities.dp(60.0f) + f35) + ((int) (AndroidUtilities.dp(30.0f) * (1.0f - f21)))) - ((int) f9)) + (this.idleProgress * f8 * (-AndroidUtilities.dp(8.0f)));
                        float f58 = (dp / 2.0f) + dp2;
                        dpf2 = (f58 - AndroidUtilities.dpf2(8.0f)) + AndroidUtilities.dpf2(2.0f) + (AndroidUtilities.dpf2(2.0f) * f8);
                        dpf22 = (f58 - AndroidUtilities.dpf2(16.0f)) + AndroidUtilities.dpf2(2.0f) + (AndroidUtilities.dpf2(2.0f) * f8);
                        this.snapAnimationProgress = 0.0f;
                        f11 = (1.0f - f8) * 9.0f;
                        f12 = 0.0f;
                    }
                    if (this.showTooltip) {
                        f13 = dp2;
                    } else {
                        f13 = dp2;
                        if (System.currentTimeMillis() - this.showTooltipStartTime > 200) {
                            f14 = 0.0f;
                            if (f8 >= 0.8f || isSendButtonVisible() || this.exitTransition != f14 || this.transformToSeekbar != f14) {
                                this.showTooltip = false;
                            }
                            if (this.showTooltip) {
                                float f59 = this.tooltipAlpha;
                                f15 = dpf22;
                                if (f59 != 1.0f) {
                                    float f60 = f59 + (((float) j) / 150.0f);
                                    this.tooltipAlpha = f60;
                                    if (f60 >= 1.0f) {
                                        this.tooltipAlpha = 1.0f;
                                        SharedConfig.increaseLockRecordAudioVideoHintShowed();
                                    }
                                }
                            } else {
                                f15 = dpf22;
                                float f61 = this.tooltipAlpha - (((float) j) / 150.0f);
                                this.tooltipAlpha = f61;
                                if (f61 < 0.0f) {
                                    this.tooltipAlpha = 0.0f;
                                }
                            }
                            int i4 = (int) (this.tooltipAlpha * 255.0f);
                            this.tooltipBackground.setAlpha(i4);
                            this.tooltipBackgroundArrow.setAlpha(i4);
                            this.tooltipPaint.setAlpha(i4);
                            if (this.tooltipLayout != null) {
                                canvas.save();
                                drawable4 = drawable3;
                                this.rectF.set(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
                                canvas.translate((getMeasuredWidth() - this.tooltipWidth) - AndroidUtilities.dp(44.0f), AndroidUtilities.dpf2(16.0f));
                                drawable5 = drawable2;
                                this.tooltipBackground.setBounds(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(2.0f), (int) (this.tooltipWidth + AndroidUtilities.dp(36.0f)), (int) (this.tooltipLayout.getHeight() + AndroidUtilities.dpf2(4.0f)));
                                this.tooltipBackground.draw(canvas);
                                this.tooltipLayout.draw(canvas);
                                canvas.restore();
                                canvas.save();
                                canvas.translate(measuredWidth, (AndroidUtilities.dpf2(17.0f) + (this.tooltipLayout.getHeight() / 2.0f)) - (this.idleProgress * AndroidUtilities.dpf2(3.0f)));
                                this.path.reset();
                                this.path.setLastPoint(-AndroidUtilities.dpf2(5.0f), AndroidUtilities.dpf2(4.0f));
                                this.path.lineTo(0.0f, 0.0f);
                                this.path.lineTo(AndroidUtilities.dpf2(5.0f), AndroidUtilities.dpf2(4.0f));
                                this.p.setColor(-1);
                                this.p.setAlpha(i4);
                                this.p.setStyle(Paint.Style.STROKE);
                                this.p.setStrokeCap(Paint.Cap.ROUND);
                                this.p.setStrokeJoin(Paint.Join.ROUND);
                                this.p.setStrokeWidth(AndroidUtilities.dpf2(1.5f));
                                canvas.drawPath(this.path, this.p);
                                canvas.restore();
                                canvas.save();
                                Drawable drawable10 = this.tooltipBackgroundArrow;
                                drawable10.setBounds(measuredWidth - (drawable10.getIntrinsicWidth() / 2), (int) (this.tooltipLayout.getHeight() + AndroidUtilities.dpf2(20.0f)), (this.tooltipBackgroundArrow.getIntrinsicWidth() / 2) + measuredWidth, ((int) (this.tooltipLayout.getHeight() + AndroidUtilities.dpf2(20.0f))) + this.tooltipBackgroundArrow.getIntrinsicHeight());
                                this.tooltipBackgroundArrow.draw(canvas);
                                canvas.restore();
                                canvas.save();
                                canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatActivityEnterView.this.textFieldContainer.getMeasuredHeight());
                                float f62 = this.scale;
                                float f63 = 1.0f - f62 != 0.0f ? 1.0f - f62 : f5 != 0.0f ? f5 : f6 != 0.0f ? f6 : 0.0f;
                                if (this.slideToCancelProgress >= 0.7f || this.canceledByGesture) {
                                    this.showTooltip = false;
                                    f16 = this.slideToCancelLockProgress;
                                    if (f16 != 0.0f) {
                                        float f64 = f16 - 0.12f;
                                        this.slideToCancelLockProgress = f64;
                                        if (f64 < 0.0f) {
                                            this.slideToCancelLockProgress = 0.0f;
                                        }
                                    }
                                } else {
                                    float f65 = this.slideToCancelLockProgress;
                                    if (f65 != 1.0f) {
                                        float f66 = f65 + 0.12f;
                                        this.slideToCancelLockProgress = f66;
                                        if (f66 > 1.0f) {
                                            this.slideToCancelLockProgress = 1.0f;
                                        }
                                    }
                                }
                                dpf23 = AndroidUtilities.dpf2(72.0f);
                                dpf24 = ((dpf23 * f63) - ((AndroidUtilities.dpf2(20.0f) * f7) * (1.0f - f63))) + ((1.0f - this.slideToCancelLockProgress) * dpf23);
                                if (dpf24 <= dpf23) {
                                    dpf23 = dpf24;
                                }
                                canvas.translate(0.0f, dpf23);
                                float f67 = this.scale * (1.0f - f5) * (1.0f - f6) * this.slideToCancelLockProgress;
                                float f68 = measuredWidth;
                                canvas.scale(f67, f67, f68, dpf2);
                                this.rectF.set(f68 - AndroidUtilities.dpf2(18.0f), f13, AndroidUtilities.dpf2(18.0f) + f68, f13 + dp);
                                ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
                                ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
                                canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
                                ChatActivityEnterView.this.pauseRect.set(this.rectF);
                                float f69 = 1.0f - f12;
                                float f70 = f10;
                                this.rectF.set((f68 - AndroidUtilities.dpf2(6.0f)) - (AndroidUtilities.dpf2(2.0f) * f69), dpf2 - (AndroidUtilities.dpf2(2.0f) * f69), AndroidUtilities.dp(6.0f) + measuredWidth + (AndroidUtilities.dpf2(2.0f) * f69), dpf2 + AndroidUtilities.dp(12.0f) + (AndroidUtilities.dpf2(2.0f) * f69));
                                RectF rectF = this.rectF;
                                float f71 = rectF.bottom;
                                float centerX = rectF.centerX();
                                float centerY = this.rectF.centerY();
                                canvas.save();
                                float f72 = 1.0f - f8;
                                canvas.translate(0.0f, AndroidUtilities.dpf2(2.0f) * f72);
                                canvas.rotate(f11, centerX, centerY);
                                canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockPaint);
                                if (f12 != 1.0f) {
                                    canvas.drawCircle(centerX, centerY, AndroidUtilities.dpf2(2.0f) * f69, this.lockBackgroundPaint);
                                }
                                if (f12 != 1.0f) {
                                    this.rectF.set(0.0f, 0.0f, AndroidUtilities.dpf2(8.0f), AndroidUtilities.dpf2(8.0f));
                                    canvas.save();
                                    canvas.clipRect(0.0f, 0.0f, getMeasuredWidth(), dpf23 + f71 + (AndroidUtilities.dpf2(2.0f) * f72));
                                    canvas.translate(f68 - AndroidUtilities.dpf2(4.0f), ((f15 - ((AndroidUtilities.dpf2(1.5f) * (1.0f - this.idleProgress)) * f8)) - (AndroidUtilities.dpf2(2.0f) * f72)) + (AndroidUtilities.dpf2(12.0f) * f12) + (AndroidUtilities.dpf2(2.0f) * this.snapAnimationProgress));
                                    if (f11 > 0.0f) {
                                        canvas.rotate(f11, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(8.0f));
                                    }
                                    canvas.drawLine(AndroidUtilities.dpf2(8.0f), AndroidUtilities.dpf2(4.0f), AndroidUtilities.dpf2(8.0f), (AndroidUtilities.dpf2(4.0f) * f69) + AndroidUtilities.dpf2(6.0f), this.lockOutlinePaint);
                                    canvas.drawArc(this.rectF, 0.0f, -180.0f, false, this.lockOutlinePaint);
                                    canvas.drawLine(0.0f, AndroidUtilities.dpf2(4.0f), 0.0f, (AndroidUtilities.dpf2(4.0f) * this.snapAnimationProgress * f72) + AndroidUtilities.dpf2(4.0f) + (AndroidUtilities.dpf2(4.0f) * this.idleProgress * f8 * (!isSendButtonVisible())), this.lockOutlinePaint);
                                    canvas.restore();
                                }
                                canvas.restore();
                                canvas.restore();
                                if (this.scale != 1.0f) {
                                    f17 = f70;
                                    canvas.drawCircle(measuredWidth + this.slideDelta, f18, f17, ChatActivityEnterView.this.paint);
                                    float f73 = this.canceledByGesture ? 1.0f - this.slideToCancelProgress : 1.0f;
                                    canvas.save();
                                    canvas.translate(this.slideDelta, 0.0f);
                                    drawIconInternal(canvas, drawable5, drawable4, this.progressToSendButton, (int) (f73 * 255.0f));
                                    canvas.restore();
                                } else {
                                    f17 = f70;
                                }
                                this.drawingCircleRadius = f17;
                            }
                            drawable4 = drawable3;
                            drawable5 = drawable2;
                            canvas.save();
                            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatActivityEnterView.this.textFieldContainer.getMeasuredHeight());
                            float f622 = this.scale;
                            if (1.0f - f622 != 0.0f) {
                            }
                            if (this.slideToCancelProgress >= 0.7f) {
                            }
                            this.showTooltip = false;
                            f16 = this.slideToCancelLockProgress;
                            if (f16 != 0.0f) {
                            }
                            dpf23 = AndroidUtilities.dpf2(72.0f);
                            dpf24 = ((dpf23 * f63) - ((AndroidUtilities.dpf2(20.0f) * f7) * (1.0f - f63))) + ((1.0f - this.slideToCancelLockProgress) * dpf23);
                            if (dpf24 <= dpf23) {
                            }
                            canvas.translate(0.0f, dpf23);
                            float f672 = this.scale * (1.0f - f5) * (1.0f - f6) * this.slideToCancelLockProgress;
                            float f682 = measuredWidth;
                            canvas.scale(f672, f672, f682, dpf2);
                            this.rectF.set(f682 - AndroidUtilities.dpf2(18.0f), f13, AndroidUtilities.dpf2(18.0f) + f682, f13 + dp);
                            ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
                            ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
                            canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
                            ChatActivityEnterView.this.pauseRect.set(this.rectF);
                            float f692 = 1.0f - f12;
                            float f702 = f10;
                            this.rectF.set((f682 - AndroidUtilities.dpf2(6.0f)) - (AndroidUtilities.dpf2(2.0f) * f692), dpf2 - (AndroidUtilities.dpf2(2.0f) * f692), AndroidUtilities.dp(6.0f) + measuredWidth + (AndroidUtilities.dpf2(2.0f) * f692), dpf2 + AndroidUtilities.dp(12.0f) + (AndroidUtilities.dpf2(2.0f) * f692));
                            RectF rectF2 = this.rectF;
                            float f712 = rectF2.bottom;
                            float centerX2 = rectF2.centerX();
                            float centerY2 = this.rectF.centerY();
                            canvas.save();
                            float f722 = 1.0f - f8;
                            canvas.translate(0.0f, AndroidUtilities.dpf2(2.0f) * f722);
                            canvas.rotate(f11, centerX2, centerY2);
                            canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockPaint);
                            if (f12 != 1.0f) {
                            }
                            if (f12 != 1.0f) {
                            }
                            canvas.restore();
                            canvas.restore();
                            if (this.scale != 1.0f) {
                            }
                            this.drawingCircleRadius = f17;
                        }
                    }
                    f14 = 0.0f;
                    if (this.tooltipAlpha == 0.0f) {
                        f15 = dpf22;
                        drawable4 = drawable3;
                        drawable5 = drawable2;
                        canvas.save();
                        canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatActivityEnterView.this.textFieldContainer.getMeasuredHeight());
                        float f6222 = this.scale;
                        if (1.0f - f6222 != 0.0f) {
                        }
                        if (this.slideToCancelProgress >= 0.7f) {
                        }
                        this.showTooltip = false;
                        f16 = this.slideToCancelLockProgress;
                        if (f16 != 0.0f) {
                        }
                        dpf23 = AndroidUtilities.dpf2(72.0f);
                        dpf24 = ((dpf23 * f63) - ((AndroidUtilities.dpf2(20.0f) * f7) * (1.0f - f63))) + ((1.0f - this.slideToCancelLockProgress) * dpf23);
                        if (dpf24 <= dpf23) {
                        }
                        canvas.translate(0.0f, dpf23);
                        float f6722 = this.scale * (1.0f - f5) * (1.0f - f6) * this.slideToCancelLockProgress;
                        float f6822 = measuredWidth;
                        canvas.scale(f6722, f6722, f6822, dpf2);
                        this.rectF.set(f6822 - AndroidUtilities.dpf2(18.0f), f13, AndroidUtilities.dpf2(18.0f) + f6822, f13 + dp);
                        ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
                        ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
                        canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
                        ChatActivityEnterView.this.pauseRect.set(this.rectF);
                        float f6922 = 1.0f - f12;
                        float f7022 = f10;
                        this.rectF.set((f6822 - AndroidUtilities.dpf2(6.0f)) - (AndroidUtilities.dpf2(2.0f) * f6922), dpf2 - (AndroidUtilities.dpf2(2.0f) * f6922), AndroidUtilities.dp(6.0f) + measuredWidth + (AndroidUtilities.dpf2(2.0f) * f6922), dpf2 + AndroidUtilities.dp(12.0f) + (AndroidUtilities.dpf2(2.0f) * f6922));
                        RectF rectF22 = this.rectF;
                        float f7122 = rectF22.bottom;
                        float centerX22 = rectF22.centerX();
                        float centerY22 = this.rectF.centerY();
                        canvas.save();
                        float f7222 = 1.0f - f8;
                        canvas.translate(0.0f, AndroidUtilities.dpf2(2.0f) * f7222);
                        canvas.rotate(f11, centerX22, centerY22);
                        canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockPaint);
                        if (f12 != 1.0f) {
                        }
                        if (f12 != 1.0f) {
                        }
                        canvas.restore();
                        canvas.restore();
                        if (this.scale != 1.0f) {
                        }
                        this.drawingCircleRadius = f17;
                    }
                    if (f8 >= 0.8f) {
                    }
                    this.showTooltip = false;
                    if (this.showTooltip) {
                    }
                    int i42 = (int) (this.tooltipAlpha * 255.0f);
                    this.tooltipBackground.setAlpha(i42);
                    this.tooltipBackgroundArrow.setAlpha(i42);
                    this.tooltipPaint.setAlpha(i42);
                    if (this.tooltipLayout != null) {
                    }
                    drawable4 = drawable3;
                    drawable5 = drawable2;
                    canvas.save();
                    canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatActivityEnterView.this.textFieldContainer.getMeasuredHeight());
                    float f62222 = this.scale;
                    if (1.0f - f62222 != 0.0f) {
                    }
                    if (this.slideToCancelProgress >= 0.7f) {
                    }
                    this.showTooltip = false;
                    f16 = this.slideToCancelLockProgress;
                    if (f16 != 0.0f) {
                    }
                    dpf23 = AndroidUtilities.dpf2(72.0f);
                    dpf24 = ((dpf23 * f63) - ((AndroidUtilities.dpf2(20.0f) * f7) * (1.0f - f63))) + ((1.0f - this.slideToCancelLockProgress) * dpf23);
                    if (dpf24 <= dpf23) {
                    }
                    canvas.translate(0.0f, dpf23);
                    float f67222 = this.scale * (1.0f - f5) * (1.0f - f6) * this.slideToCancelLockProgress;
                    float f68222 = measuredWidth;
                    canvas.scale(f67222, f67222, f68222, dpf2);
                    this.rectF.set(f68222 - AndroidUtilities.dpf2(18.0f), f13, AndroidUtilities.dpf2(18.0f) + f68222, f13 + dp);
                    ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
                    ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
                    canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
                    ChatActivityEnterView.this.pauseRect.set(this.rectF);
                    float f69222 = 1.0f - f12;
                    float f70222 = f10;
                    this.rectF.set((f68222 - AndroidUtilities.dpf2(6.0f)) - (AndroidUtilities.dpf2(2.0f) * f69222), dpf2 - (AndroidUtilities.dpf2(2.0f) * f69222), AndroidUtilities.dp(6.0f) + measuredWidth + (AndroidUtilities.dpf2(2.0f) * f69222), dpf2 + AndroidUtilities.dp(12.0f) + (AndroidUtilities.dpf2(2.0f) * f69222));
                    RectF rectF222 = this.rectF;
                    float f71222 = rectF222.bottom;
                    float centerX222 = rectF222.centerX();
                    float centerY222 = this.rectF.centerY();
                    canvas.save();
                    float f72222 = 1.0f - f8;
                    canvas.translate(0.0f, AndroidUtilities.dpf2(2.0f) * f72222);
                    canvas.rotate(f11, centerX222, centerY222);
                    canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockPaint);
                    if (f12 != 1.0f) {
                    }
                    if (f12 != 1.0f) {
                    }
                    canvas.restore();
                    canvas.restore();
                    if (this.scale != 1.0f) {
                    }
                    this.drawingCircleRadius = f17;
                }
            }
            drawable2 = drawable9;
            j = currentTimeMillis;
            f8 = dp5;
            f9 = f;
            f10 = f36;
            drawable3 = drawable8;
            if (!isSendButtonVisible()) {
            }
            if (this.showTooltip) {
            }
            f14 = 0.0f;
            if (this.tooltipAlpha == 0.0f) {
            }
            if (f8 >= 0.8f) {
            }
            this.showTooltip = false;
            if (this.showTooltip) {
            }
            int i422 = (int) (this.tooltipAlpha * 255.0f);
            this.tooltipBackground.setAlpha(i422);
            this.tooltipBackgroundArrow.setAlpha(i422);
            this.tooltipPaint.setAlpha(i422);
            if (this.tooltipLayout != null) {
            }
            drawable4 = drawable3;
            drawable5 = drawable2;
            canvas.save();
            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight() - ChatActivityEnterView.this.textFieldContainer.getMeasuredHeight());
            float f622222 = this.scale;
            if (1.0f - f622222 != 0.0f) {
            }
            if (this.slideToCancelProgress >= 0.7f) {
            }
            this.showTooltip = false;
            f16 = this.slideToCancelLockProgress;
            if (f16 != 0.0f) {
            }
            dpf23 = AndroidUtilities.dpf2(72.0f);
            dpf24 = ((dpf23 * f63) - ((AndroidUtilities.dpf2(20.0f) * f7) * (1.0f - f63))) + ((1.0f - this.slideToCancelLockProgress) * dpf23);
            if (dpf24 <= dpf23) {
            }
            canvas.translate(0.0f, dpf23);
            float f672222 = this.scale * (1.0f - f5) * (1.0f - f6) * this.slideToCancelLockProgress;
            float f682222 = measuredWidth;
            canvas.scale(f672222, f672222, f682222, dpf2);
            this.rectF.set(f682222 - AndroidUtilities.dpf2(18.0f), f13, AndroidUtilities.dpf2(18.0f) + f682222, f13 + dp);
            ChatActivityEnterView.this.lockShadowDrawable.setBounds((int) (this.rectF.left - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.top - AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.right + AndroidUtilities.dpf2(3.0f)), (int) (this.rectF.bottom + AndroidUtilities.dpf2(3.0f)));
            ChatActivityEnterView.this.lockShadowDrawable.draw(canvas);
            canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(18.0f), AndroidUtilities.dpf2(18.0f), this.lockBackgroundPaint);
            ChatActivityEnterView.this.pauseRect.set(this.rectF);
            float f692222 = 1.0f - f12;
            float f702222 = f10;
            this.rectF.set((f682222 - AndroidUtilities.dpf2(6.0f)) - (AndroidUtilities.dpf2(2.0f) * f692222), dpf2 - (AndroidUtilities.dpf2(2.0f) * f692222), AndroidUtilities.dp(6.0f) + measuredWidth + (AndroidUtilities.dpf2(2.0f) * f692222), dpf2 + AndroidUtilities.dp(12.0f) + (AndroidUtilities.dpf2(2.0f) * f692222));
            RectF rectF2222 = this.rectF;
            float f712222 = rectF2222.bottom;
            float centerX2222 = rectF2222.centerX();
            float centerY2222 = this.rectF.centerY();
            canvas.save();
            float f722222 = 1.0f - f8;
            canvas.translate(0.0f, AndroidUtilities.dpf2(2.0f) * f722222);
            canvas.rotate(f11, centerX2222, centerY2222);
            canvas.drawRoundRect(this.rectF, AndroidUtilities.dpf2(3.0f), AndroidUtilities.dpf2(3.0f), this.lockPaint);
            if (f12 != 1.0f) {
            }
            if (f12 != 1.0f) {
            }
            canvas.restore();
            canvas.restore();
            if (this.scale != 1.0f) {
            }
            this.drawingCircleRadius = f17;
        }

        public void drawIcon(Canvas canvas, int i, int i2, float f) {
            Drawable drawable;
            checkDrawables();
            Drawable drawable2 = null;
            if (isSendButtonVisible()) {
                if (this.progressToSendButton != 1.0f) {
                    drawable2 = ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.cameraDrawable : ChatActivityEnterView.this.micDrawable;
                }
                drawable = ChatActivityEnterView.this.sendDrawable;
            } else {
                drawable = ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.cameraDrawable : ChatActivityEnterView.this.micDrawable;
            }
            Drawable drawable3 = drawable;
            Drawable drawable4 = drawable2;
            ChatActivityEnterView.this.sendRect.set(i - (drawable3.getIntrinsicWidth() / 2), i2 - (drawable3.getIntrinsicHeight() / 2), (drawable3.getIntrinsicWidth() / 2) + i, (drawable3.getIntrinsicHeight() / 2) + i2);
            drawable3.setBounds(ChatActivityEnterView.this.sendRect);
            if (drawable4 != null) {
                drawable4.setBounds(i - (drawable4.getIntrinsicWidth() / 2), i2 - (drawable4.getIntrinsicHeight() / 2), i + (drawable4.getIntrinsicWidth() / 2), i2 + (drawable4.getIntrinsicHeight() / 2));
            }
            drawIconInternal(canvas, drawable3, drawable4, this.progressToSendButton, (int) (f * 255.0f));
        }

        private void drawIconInternal(Canvas canvas, Drawable drawable, Drawable drawable2, float f, int i) {
            checkDrawables();
            if (f == 0.0f || f == 1.0f || drawable2 == null) {
                boolean z = this.canceledByGesture;
                if (z && this.slideToCancelProgress == 1.0f) {
                    ChatActivityEnterView.this.audioVideoSendButton.setAlpha(1.0f);
                    setVisibility(8);
                    return;
                } else if (!z || this.slideToCancelProgress >= 1.0f) {
                    if (z) {
                        return;
                    }
                    drawable.setAlpha(i);
                    drawable.draw(canvas);
                    return;
                } else {
                    Drawable drawable3 = ChatActivityEnterView.this.isInVideoMode() ? ChatActivityEnterView.this.cameraOutline : ChatActivityEnterView.this.micOutline;
                    drawable3.setBounds(drawable.getBounds());
                    float f2 = this.slideToCancelProgress;
                    int i2 = (int) (f2 >= 0.93f ? ((f2 - 0.93f) / 0.07f) * 255.0f : 0.0f);
                    drawable3.setAlpha(i2);
                    drawable3.draw(canvas);
                    drawable3.setAlpha(255);
                    drawable.setAlpha(255 - i2);
                    drawable.draw(canvas);
                    return;
                }
            }
            canvas.save();
            canvas.scale(f, f, drawable.getBounds().centerX(), drawable.getBounds().centerY());
            float f3 = i;
            drawable.setAlpha((int) (f3 * f));
            drawable.draw(canvas);
            canvas.restore();
            canvas.save();
            float f4 = 1.0f - f;
            canvas.scale(f4, f4, drawable.getBounds().centerX(), drawable.getBounds().centerY());
            drawable2.setAlpha((int) (f3 * f4));
            drawable2.draw(canvas);
            canvas.restore();
        }

        @Override // android.view.View
        protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
            return super.dispatchHoverEvent(motionEvent) || this.virtualViewHelper.dispatchHoverEvent(motionEvent);
        }

        public void setTransformToSeekbar(float f) {
            this.transformToSeekbar = f;
            invalidate();
        }

        public float getTransformToSeekbarProgressStep3() {
            return this.progressToSeekbarStep3;
        }

        @Keep
        public float getExitTransition() {
            return this.exitTransition;
        }

        @Keep
        public void setExitTransition(float f) {
            this.exitTransition = f;
            invalidate();
        }

        public void updateColors() {
            Paint paint = ChatActivityEnterView.this.paint;
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            int i = Theme.key_chat_messagePanelVoiceBackground;
            paint.setColor(chatActivityEnterView.getThemedColor(i));
            this.tinyWaveDrawable.paint.setColor(ColorUtils.setAlphaComponent(ChatActivityEnterView.this.getThemedColor(i), 38));
            this.bigWaveDrawable.paint.setColor(ColorUtils.setAlphaComponent(ChatActivityEnterView.this.getThemedColor(i), 76));
            this.tooltipPaint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_gifSaveHintText));
            int dp = AndroidUtilities.dp(5.0f);
            ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
            int i2 = Theme.key_chat_gifSaveHintBackground;
            this.tooltipBackground = Theme.createRoundRectDrawable(dp, chatActivityEnterView2.getThemedColor(i2));
            this.tooltipBackgroundArrow.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(i2), PorterDuff.Mode.SRC_IN));
            this.lockBackgroundPaint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelVoiceLockBackground));
            Paint paint2 = this.lockPaint;
            ChatActivityEnterView chatActivityEnterView3 = ChatActivityEnterView.this;
            int i3 = Theme.key_chat_messagePanelVoiceLock;
            paint2.setColor(chatActivityEnterView3.getThemedColor(i3));
            this.lockOutlinePaint.setColor(ChatActivityEnterView.this.getThemedColor(i3));
            this.paintAlpha = ChatActivityEnterView.this.paint.getAlpha();
        }

        public void showTooltipIfNeed() {
            if (SharedConfig.lockRecordAudioVideoHint < 3) {
                this.showTooltip = true;
                this.showTooltipStartTime = System.currentTimeMillis();
            }
        }

        @Keep
        public float getSlideToCancelProgress() {
            return this.slideToCancelProgress;
        }

        @Keep
        public void setSlideToCancelProgress(float f) {
            this.slideToCancelProgress = f;
            float measuredWidth = getMeasuredWidth() * 0.35f;
            if (measuredWidth > AndroidUtilities.dp(140.0f)) {
                measuredWidth = AndroidUtilities.dp(140.0f);
            }
            this.slideDelta = (int) ((-measuredWidth) * (1.0f - f));
            invalidate();
        }

        public void canceledByGesture() {
            this.canceledByGesture = true;
        }

        public void setMovingCords(float f, float f2) {
            float f3 = this.lastMovingX;
            float f4 = (f - f3) * (f - f3);
            float f5 = this.lastMovingY;
            float f6 = f4 + ((f2 - f5) * (f2 - f5));
            this.lastMovingY = f2;
            this.lastMovingX = f;
            if (this.showTooltip && this.tooltipAlpha == 0.0f && f6 > this.touchSlop) {
                this.showTooltipStartTime = System.currentTimeMillis();
            }
        }

        public void showWaves(boolean z, boolean z2) {
            if (!z2) {
                this.wavesEnterAnimation = z ? 1.0f : 0.5f;
            }
            this.showWaves = z;
        }

        public void drawWaves(Canvas canvas, float f, float f2, float f3) {
            float interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(this.wavesEnterAnimation);
            float f4 = this.slideToCancelProgress;
            float f5 = f4 > 0.7f ? 1.0f : f4 / 0.7f;
            canvas.save();
            float f6 = this.scale * f5 * interpolation * (BlobDrawable.SCALE_BIG_MIN + (this.bigWaveDrawable.amplitude * 1.4f)) * f3;
            canvas.scale(f6, f6, f, f2);
            BlobDrawable blobDrawable = this.bigWaveDrawable;
            blobDrawable.draw(f, f2, canvas, blobDrawable.paint);
            canvas.restore();
            float f7 = this.scale * f5 * interpolation * (BlobDrawable.SCALE_SMALL_MIN + (this.tinyWaveDrawable.amplitude * 1.4f)) * f3;
            canvas.save();
            canvas.scale(f7, f7, f, f2);
            BlobDrawable blobDrawable2 = this.tinyWaveDrawable;
            blobDrawable2.draw(f, f2, canvas, blobDrawable2.paint);
            canvas.restore();
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public class VirtualViewHelper extends ExploreByTouchHelper {
            private int[] coords;

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
                return true;
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public VirtualViewHelper(View view) {
                super(view);
                RecordCircle.this = r1;
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
            protected void getVisibleVirtualViews(List<Integer> list) {
                if (RecordCircle.this.isSendButtonVisible()) {
                    list.add(1);
                    list.add(2);
                    list.add(3);
                }
            }

            @Override // androidx.customview.widget.ExploreByTouchHelper
            protected void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                if (i == 1) {
                    accessibilityNodeInfoCompat.setBoundsInParent(ChatActivityEnterView.this.sendRect);
                    accessibilityNodeInfoCompat.setText(LocaleController.getString("Send", R.string.Send));
                } else if (i == 2) {
                    ChatActivityEnterView.this.rect.set((int) ChatActivityEnterView.this.pauseRect.left, (int) ChatActivityEnterView.this.pauseRect.top, (int) ChatActivityEnterView.this.pauseRect.right, (int) ChatActivityEnterView.this.pauseRect.bottom);
                    accessibilityNodeInfoCompat.setBoundsInParent(ChatActivityEnterView.this.rect);
                    accessibilityNodeInfoCompat.setText(LocaleController.getString("Stop", R.string.Stop));
                } else if (i != 3 || ChatActivityEnterView.this.recordCircle == null) {
                } else {
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
                    accessibilityNodeInfoCompat.setText(LocaleController.getString("Cancel", R.string.Cancel));
                }
            }
        }
    }

    public ChatActivityEnterView(Activity activity, SizeNotifierFrameLayout sizeNotifierFrameLayout, ChatActivity chatActivity, boolean z) {
        this(activity, sizeNotifierFrameLayout, chatActivity, z, null);
    }

    @SuppressLint({"ClickableViewAccessibility"})
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
        this.animationParamsX = new HashMap<>();
        this.mediaMessageButtonsDelegate = new View.AccessibilityDelegate(this) { // from class: org.telegram.ui.Components.ChatActivityEnterView.1
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setClassName("android.widget.ImageButton");
                accessibilityNodeInfo.setClickable(true);
                accessibilityNodeInfo.setLongClickable(true);
            }
        };
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
            {
                ChatActivityEnterView.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if ((ChatActivityEnterView.this.hasBotWebView() && ChatActivityEnterView.this.botCommandsMenuIsShowing()) || ChatActivityEnterView.this.destroyed) {
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

            {
                ChatActivityEnterView.this = this;
            }

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
        this.roundedTranslationYProperty = new Property<View, Integer>(this, Integer.class, "translationY") { // from class: org.telegram.ui.Components.ChatActivityEnterView.4
            @Override // android.util.Property
            public Integer get(View view) {
                return Integer.valueOf(Math.round(view.getTranslationY()));
            }

            @Override // android.util.Property
            public void set(View view, Integer num) {
                view.setTranslationY(num.intValue());
            }
        };
        this.recordCircleScale = new Property<RecordCircle, Float>(this, Float.class, "scale") { // from class: org.telegram.ui.Components.ChatActivityEnterView.5
            @Override // android.util.Property
            public Float get(RecordCircle recordCircle) {
                return Float.valueOf(recordCircle.getScale());
            }

            @Override // android.util.Property
            public void set(RecordCircle recordCircle, Float f) {
                recordCircle.setScale(f.floatValue());
            }
        };
        this.redDotPaint = new Paint(1);
        this.onFinishInitCameraRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.6
            {
                ChatActivityEnterView.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.needStartRecordVideo(0, true, 0);
                }
            }
        };
        this.recordAudioVideoRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.7
            {
                ChatActivityEnterView.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
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
                if (ChatActivityEnterView.this.isInVideoMode()) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        boolean z2 = ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.RECORD_AUDIO") == 0;
                        boolean z3 = ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.CAMERA") == 0;
                        if (!z2 || !z3) {
                            String[] strArr = new String[(z2 || z3) ? 1 : 2];
                            if (!z2 && !z3) {
                                strArr[0] = "android.permission.RECORD_AUDIO";
                                strArr[1] = "android.permission.CAMERA";
                            } else if (!z2) {
                                strArr[0] = "android.permission.RECORD_AUDIO";
                            } else {
                                strArr[0] = "android.permission.CAMERA";
                            }
                            ChatActivityEnterView.this.parentActivity.requestPermissions(strArr, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                            return;
                        }
                    }
                    if (!CameraController.getInstance().isCameraInitied()) {
                        CameraController.getInstance().initCamera(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                    } else {
                        ChatActivityEnterView.this.onFinishInitCameraRunnable.run();
                    }
                    if (ChatActivityEnterView.this.recordingAudioVideo) {
                        return;
                    }
                    ChatActivityEnterView.this.recordingAudioVideo = true;
                    ChatActivityEnterView.this.updateRecordInterface(0);
                    if (ChatActivityEnterView.this.recordCircle != null) {
                        ChatActivityEnterView.this.recordCircle.showWaves(false, false);
                    }
                    if (ChatActivityEnterView.this.recordTimerView != null) {
                        ChatActivityEnterView.this.recordTimerView.reset();
                    }
                } else if (Build.VERSION.SDK_INT < 23 || ChatActivityEnterView.this.parentActivity.checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                    ChatActivityEnterView.this.delegate.needStartRecordAudio(1);
                    ChatActivityEnterView.this.startedDraggingX = -1.0f;
                    MediaController.getInstance().startRecording(ChatActivityEnterView.this.currentAccount, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), ChatActivityEnterView.this.delegate != null ? ChatActivityEnterView.this.delegate.getReplyToStory() : null, ChatActivityEnterView.this.recordingGuid, true);
                    ChatActivityEnterView.this.recordingAudioVideo = true;
                    ChatActivityEnterView.this.updateRecordInterface(0);
                    if (ChatActivityEnterView.this.recordTimerView != null) {
                        ChatActivityEnterView.this.recordTimerView.start();
                    }
                    if (ChatActivityEnterView.this.recordDot != null) {
                        ChatActivityEnterView.this.recordDot.enterAnimation = false;
                    }
                    ChatActivityEnterView.this.audioVideoButtonContainer.getParent().requestDisallowInterceptTouchEvent(true);
                    if (ChatActivityEnterView.this.recordCircle != null) {
                        ChatActivityEnterView.this.recordCircle.showWaves(true, false);
                    }
                } else {
                    ChatActivityEnterView.this.parentActivity.requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 3);
                }
            }
        };
        this.notificationsLocker = new AnimationNotificationsLocker();
        this.paint = new Paint(1);
        this.pauseRect = new RectF();
        this.sendRect = new android.graphics.Rect();
        this.rect = new android.graphics.Rect();
        this.runEmojiPanelAnimation = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.8
            {
                ChatActivityEnterView.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (ChatActivityEnterView.this.panelAnimation == null || ChatActivityEnterView.this.panelAnimation.isRunning()) {
                    return;
                }
                ChatActivityEnterView.this.panelAnimation.start();
            }
        };
        this.EMOJI_BUTTON_SCALE = new Property<View, Float>(Float.class, "emoji_button_scale") { // from class: org.telegram.ui.Components.ChatActivityEnterView.9
            {
                ChatActivityEnterView.this = this;
            }

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
        this.ATTACH_LAYOUT_ALPHA = new Property<View, Float>(Float.class, "attach_scale") { // from class: org.telegram.ui.Components.ChatActivityEnterView.10
            {
                ChatActivityEnterView.this = this;
            }

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
        this.EMOJI_BUTTON_ALPHA = new Property<View, Float>(Float.class, "emoji_button_alpha") { // from class: org.telegram.ui.Components.ChatActivityEnterView.11
            {
                ChatActivityEnterView.this = this;
            }

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
        this.ATTACH_LAYOUT_TRANSLATION_X = new Property<View, Float>(Float.class, "attach_layout_translation_x") { // from class: org.telegram.ui.Components.ChatActivityEnterView.12
            {
                ChatActivityEnterView.this = this;
            }

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
        this.MESSAGE_TEXT_TRANSLATION_X = new Property<View, Float>(Float.class, "message_text_translation_x") { // from class: org.telegram.ui.Components.ChatActivityEnterView.13
            {
                ChatActivityEnterView.this = this;
            }

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
        this.messageEditTextEnabled = true;
        this.topViewUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ChatActivityEnterView.this.lambda$new$31(valueAnimator);
            }
        };
        this.botCommandLastPosition = -1;
        this.resourcesProvider = resourcesProvider;
        this.backgroundColor = getThemedColor(Theme.key_chat_messagePanelBackground);
        this.drawBlur = false;
        this.isChat = z;
        this.smoothKeyboard = z && !AndroidUtilities.isInMultiwindow && (chatActivity == null || !chatActivity.isInBubbleMode());
        Paint paint = new Paint(1);
        this.dotPaint = paint;
        paint.setColor(getThemedColor(Theme.key_chat_emojiPanelNewTrending));
        setFocusable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
        setClipChildren(false);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStarted);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStartError);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordStopped);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioDidSent);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.audioRouteChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messageReceivedByServer);
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
        FrameLayout frameLayout = new FrameLayout(activity) { // from class: org.telegram.ui.Components.ChatActivityEnterView.14
            {
                ChatActivityEnterView.this = this;
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                if (ChatActivityEnterView.this.botWebViewButton != null && ChatActivityEnterView.this.botWebViewButton.getVisibility() == 0) {
                    return ChatActivityEnterView.this.botWebViewButton.dispatchTouchEvent(motionEvent);
                }
                return super.dispatchTouchEvent(motionEvent);
            }
        };
        this.textFieldContainer = frameLayout;
        frameLayout.setClipChildren(false);
        this.textFieldContainer.setClipToPadding(false);
        this.textFieldContainer.setPadding(0, AndroidUtilities.dp(1.0f), 0, 0);
        addView(this.textFieldContainer, LayoutHelper.createFrame(-1, -2.0f, 83, 0.0f, 1.0f, 0.0f, 0.0f));
        FrameLayout frameLayout2 = new FrameLayout(activity) { // from class: org.telegram.ui.Components.ChatActivityEnterView.15
            private final AnimatedFloat bottomGradientAlpha;
            private final LinearGradient clipGradient;
            private final Matrix clipMatrix;
            private final Paint gradientPaint;
            private final AnimatedFloat topGradientAlpha;

            {
                ChatActivityEnterView.this = this;
                Paint paint2 = new Paint(1);
                this.gradientPaint = paint2;
                LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, 0.0f, 16.0f, new int[]{-1, 16777215}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
                this.clipGradient = linearGradient;
                this.clipMatrix = new Matrix();
                CubicBezierInterpolator cubicBezierInterpolator = CubicBezierInterpolator.EASE_OUT_QUINT;
                this.topGradientAlpha = new AnimatedFloat(this, 0L, 280L, cubicBezierInterpolator);
                this.bottomGradientAlpha = new AnimatedFloat(this, 0L, 280L, cubicBezierInterpolator);
                paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                paint2.setShader(linearGradient);
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z2, int i3, int i4, int i5, int i6) {
                super.onLayout(z2, i3, i4, i5, i6);
                if (ChatActivityEnterView.this.scheduledButton != null) {
                    int measuredWidth = (getMeasuredWidth() - AndroidUtilities.dp((ChatActivityEnterView.this.botButton == null || ChatActivityEnterView.this.botButton.getVisibility() != 0) ? 48.0f : 96.0f)) - AndroidUtilities.dp(48.0f);
                    ChatActivityEnterView.this.scheduledButton.layout(measuredWidth, ChatActivityEnterView.this.scheduledButton.getTop(), ChatActivityEnterView.this.scheduledButton.getMeasuredWidth() + measuredWidth, ChatActivityEnterView.this.scheduledButton.getBottom());
                }
                if (ChatActivityEnterView.this.animationParamsX.isEmpty()) {
                    return;
                }
                for (int i7 = 0; i7 < getChildCount(); i7++) {
                    View childAt = getChildAt(i7);
                    Float f = (Float) ChatActivityEnterView.this.animationParamsX.get(childAt);
                    if (f != null) {
                        childAt.setTranslationX(f.floatValue() - childAt.getLeft());
                        childAt.animate().translationX(0.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                    }
                }
                ChatActivityEnterView.this.animationParamsX.clear();
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                EditTextCaption editTextCaption;
                if (view != null && view == (editTextCaption = ChatActivityEnterView.this.messageEditText)) {
                    float f = this.topGradientAlpha.set(editTextCaption.canScrollVertically(-1));
                    float f2 = this.bottomGradientAlpha.set(ChatActivityEnterView.this.messageEditText.canScrollVertically(1));
                    if (f <= 0.0f && f2 <= 0.0f) {
                        return super.drawChild(canvas, view, j);
                    }
                    canvas.saveLayerAlpha(0.0f, 0.0f, AndroidUtilities.dp(5.0f) + ChatActivityEnterView.this.messageEditText.getX() + ChatActivityEnterView.this.messageEditText.getMeasuredWidth(), AndroidUtilities.dp(2.0f) + ChatActivityEnterView.this.messageEditText.getY() + ChatActivityEnterView.this.messageEditText.getMeasuredHeight(), 255, 31);
                    boolean drawChild = super.drawChild(canvas, view, j);
                    canvas.save();
                    if (f > 0.0f) {
                        RectF rectF = AndroidUtilities.rectTmp;
                        float x = ChatActivityEnterView.this.messageEditText.getX() - AndroidUtilities.dp(5.0f);
                        float y = ChatActivityEnterView.this.messageEditText.getY();
                        ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                        rectF.set(x, y + chatActivityEnterView.animatedTop, chatActivityEnterView.messageEditText.getX() + ChatActivityEnterView.this.messageEditText.getMeasuredWidth() + AndroidUtilities.dp(5.0f), ChatActivityEnterView.this.messageEditText.getY() + ChatActivityEnterView.this.animatedTop + AndroidUtilities.dp(13.0f));
                        this.clipMatrix.reset();
                        this.clipMatrix.postScale(1.0f, rectF.height() / 16.0f);
                        this.clipMatrix.postTranslate(rectF.left, rectF.top);
                        this.clipGradient.setLocalMatrix(this.clipMatrix);
                        this.gradientPaint.setAlpha((int) (f * 255.0f));
                        canvas.drawRect(rectF, this.gradientPaint);
                    }
                    if (f2 > 0.0f) {
                        RectF rectF2 = AndroidUtilities.rectTmp;
                        rectF2.set(ChatActivityEnterView.this.messageEditText.getX() - AndroidUtilities.dp(5.0f), (ChatActivityEnterView.this.messageEditText.getY() + ChatActivityEnterView.this.messageEditText.getMeasuredHeight()) - AndroidUtilities.dp(15.0f), ChatActivityEnterView.this.messageEditText.getX() + ChatActivityEnterView.this.messageEditText.getMeasuredWidth() + AndroidUtilities.dp(5.0f), ChatActivityEnterView.this.messageEditText.getY() + ChatActivityEnterView.this.messageEditText.getMeasuredHeight() + AndroidUtilities.dp(2.0f));
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
                    return drawChild;
                }
                ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                if (chatActivityEnterView2.shouldDrawRecordedAudioPanelInParent && view == chatActivityEnterView2.recordedAudioPanel) {
                    return true;
                }
                return super.drawChild(canvas, view, j);
            }
        };
        this.messageEditTextContainer = frameLayout2;
        frameLayout2.setClipChildren(false);
        this.textFieldContainer.addView(frameLayout2, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 48.0f, 0.0f));
        ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView = new ChatActivityEnterViewAnimatedIconView(activity) { // from class: org.telegram.ui.Components.ChatActivityEnterView.16
            {
                ChatActivityEnterView.this = this;
            }

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
        chatActivityEnterViewAnimatedIconView2.setColorFilter(new PorterDuffColorFilter(getThemedColor(i3), PorterDuff.Mode.SRC_IN));
        int i4 = Build.VERSION.SDK_INT;
        if (i4 >= 21) {
            this.emojiButton.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
        this.emojiButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda19
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
            this.notifyButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(i3), PorterDuff.Mode.MULTIPLY));
            this.notifyButton.setScaleType(ImageView.ScaleType.CENTER);
            if (i4 >= 21) {
                this.notifyButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
            }
            this.notifyButton.setVisibility((!this.canWriteToChannel || ((chatActivityEnterViewDelegate = this.delegate) != null && chatActivityEnterViewDelegate.hasScheduledMessages())) ? 8 : 0);
            this.attachLayout.addView(this.notifyButton, LayoutHelper.createLinear(48, 48));
            this.notifyButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.17
                {
                    ChatActivityEnterView.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int i5;
                    String str2;
                    ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                    chatActivityEnterView.silent = !chatActivityEnterView.silent;
                    if (ChatActivityEnterView.this.notifySilentDrawable == null) {
                        ChatActivityEnterView.this.notifySilentDrawable = new CrossOutDrawable(activity, R.drawable.input_notify_on, Theme.key_chat_messagePanelIcons);
                    }
                    ChatActivityEnterView.this.notifySilentDrawable.setCrossOut(ChatActivityEnterView.this.silent, true);
                    ChatActivityEnterView.this.notifyButton.setImageDrawable(ChatActivityEnterView.this.notifySilentDrawable);
                    SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(ChatActivityEnterView.this.currentAccount).edit();
                    edit.putBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.silent).commit();
                    NotificationsController notificationsController = NotificationsController.getInstance(ChatActivityEnterView.this.currentAccount);
                    long j = ChatActivityEnterView.this.dialog_id;
                    ChatActivity chatActivity2 = chatActivity;
                    notificationsController.updateServerNotificationsSettings(j, chatActivity2 == null ? 0 : chatActivity2.getTopicId());
                    UndoView undoView = chatActivity.getUndoView();
                    if (undoView != null) {
                        undoView.showWithAction(0L, !ChatActivityEnterView.this.silent ? 54 : 55, (Runnable) null);
                    }
                    ImageView imageView2 = ChatActivityEnterView.this.notifyButton;
                    if (ChatActivityEnterView.this.silent) {
                        i5 = R.string.AccDescrChanSilentOn;
                        str2 = "AccDescrChanSilentOn";
                    } else {
                        i5 = R.string.AccDescrChanSilentOff;
                        str2 = "AccDescrChanSilentOff";
                    }
                    imageView2.setContentDescription(LocaleController.getString(str2, i5));
                    ChatActivityEnterView.this.updateFieldHint(true);
                }
            });
            ImageView imageView2 = new ImageView(activity);
            this.attachButton = imageView2;
            imageView2.setScaleType(ImageView.ScaleType.CENTER);
            this.attachButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(i3), PorterDuff.Mode.MULTIPLY));
            this.attachButton.setImageResource(R.drawable.msg_input_attach2);
            if (i4 >= 21) {
                this.attachButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
            }
            this.attachLayout.addView(this.attachButton, LayoutHelper.createLinear(48, 48));
            this.attachButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda18
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
        FrameLayout frameLayout3 = new FrameLayout(activity) { // from class: org.telegram.ui.Components.ChatActivityEnterView.18
            {
                ChatActivityEnterView.this = this;
            }

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                if (view == ChatActivityEnterView.this.sendButton && ChatActivityEnterView.this.textTransitionIsRunning) {
                    return true;
                }
                return super.drawChild(canvas, view, j);
            }

            @Override // android.view.ViewGroup, android.view.View
            public boolean dispatchTouchEvent(MotionEvent motionEvent) {
                if (ChatActivityEnterView.this.sendButtonEnabled) {
                    return super.dispatchTouchEvent(motionEvent);
                }
                return false;
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (ChatActivityEnterView.this.sendButtonEnabled) {
                    return super.onTouchEvent(motionEvent);
                }
                return false;
            }
        };
        this.sendButtonContainer = frameLayout3;
        frameLayout3.setClipChildren(false);
        this.sendButtonContainer.setClipToPadding(false);
        this.textFieldContainer.addView(this.sendButtonContainer, LayoutHelper.createFrame(48, 48, 85));
        19 r2 = new 19(activity, resourcesProvider);
        this.audioVideoButtonContainer = r2;
        r2.setSoundEffectsEnabled(false);
        this.sendButtonContainer.addView(this.audioVideoButtonContainer, LayoutHelper.createFrame(48, 48.0f));
        this.audioVideoButtonContainer.setFocusable(true);
        this.audioVideoButtonContainer.setImportantForAccessibility(1);
        ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView3 = new ChatActivityEnterViewAnimatedIconView(activity);
        this.audioVideoSendButton = chatActivityEnterViewAnimatedIconView3;
        chatActivityEnterViewAnimatedIconView3.setFocusable(true);
        this.audioVideoSendButton.setImportantForAccessibility(1);
        this.audioVideoSendButton.setAccessibilityDelegate(this.mediaMessageButtonsDelegate);
        int dp2 = AndroidUtilities.dp(9.5f);
        this.audioVideoSendButton.setPadding(dp2, dp2, dp2, dp2);
        this.audioVideoSendButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(i3), PorterDuff.Mode.SRC_IN));
        this.audioVideoButtonContainer.addView(this.audioVideoSendButton, LayoutHelper.createFrame(48, 48.0f));
        ImageView imageView3 = new ImageView(activity);
        this.cancelBotButton = imageView3;
        imageView3.setVisibility(4);
        this.cancelBotButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ImageView imageView4 = this.cancelBotButton;
        CloseProgressDrawable2 closeProgressDrawable2 = new CloseProgressDrawable2(this) { // from class: org.telegram.ui.Components.ChatActivityEnterView.20
            @Override // org.telegram.ui.Components.CloseProgressDrawable2
            protected int getCurrentColor() {
                return Theme.getColor(Theme.key_chat_messagePanelCancelInlineBot);
            }
        };
        this.progressDrawable = closeProgressDrawable2;
        imageView4.setImageDrawable(closeProgressDrawable2);
        this.cancelBotButton.setContentDescription(LocaleController.getString("Cancel", R.string.Cancel));
        this.cancelBotButton.setSoundEffectsEnabled(false);
        this.cancelBotButton.setScaleX(0.1f);
        this.cancelBotButton.setScaleY(0.1f);
        this.cancelBotButton.setAlpha(0.0f);
        if (i4 >= 21) {
            this.cancelBotButton.setBackgroundDrawable(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
        this.sendButtonContainer.addView(this.cancelBotButton, LayoutHelper.createFrame(48, 48.0f));
        this.cancelBotButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda22
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$new$3(view);
            }
        });
        if (isInScheduleMode()) {
            Resources resources = activity.getResources();
            int i5 = R.drawable.input_schedule;
            this.sendButtonDrawable = resources.getDrawable(i5).mutate();
            this.sendButtonInverseDrawable = activity.getResources().getDrawable(i5).mutate();
            this.inactinveSendButtonDrawable = activity.getResources().getDrawable(i5).mutate();
        } else {
            Resources resources2 = activity.getResources();
            int i6 = R.drawable.ic_send;
            this.sendButtonDrawable = resources2.getDrawable(i6).mutate();
            this.sendButtonInverseDrawable = activity.getResources().getDrawable(i6).mutate();
            this.inactinveSendButtonDrawable = activity.getResources().getDrawable(i6).mutate();
        }
        View view = new View(activity) { // from class: org.telegram.ui.Components.ChatActivityEnterView.21
            private float animationDuration;
            private float animationProgress;
            private int drawableColor;
            private long lastAnimationTime;
            private int prevColorType;

            {
                ChatActivityEnterView.this = this;
            }

            @Override // android.view.View
            protected void onDraw(Canvas canvas) {
                int themedColor;
                float f;
                float dp3;
                int dp4 = AndroidUtilities.dp(20.0f);
                int i7 = 1;
                int measuredWidth = getMeasuredWidth() >> 1;
                int measuredWidth2 = (getMeasuredWidth() - ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicWidth()) / 2;
                int measuredHeight = (getMeasuredHeight() - ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicHeight()) / 2;
                if (ChatActivityEnterView.this.isInScheduleMode()) {
                    measuredHeight -= AndroidUtilities.dp(1.0f);
                } else {
                    measuredWidth2 += AndroidUtilities.dp(2.0f);
                }
                boolean z2 = ChatActivityEnterView.this.sendPopupWindow != null && ChatActivityEnterView.this.sendPopupWindow.isShowing();
                if (z2) {
                    themedColor = ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelVoicePressed);
                } else {
                    themedColor = ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelSend);
                    i7 = 2;
                }
                if (themedColor != this.drawableColor) {
                    this.lastAnimationTime = SystemClock.elapsedRealtime();
                    int i8 = this.prevColorType;
                    if (i8 != 0 && i8 != i7) {
                        this.animationProgress = 0.0f;
                        if (z2) {
                            this.animationDuration = 200.0f;
                        } else {
                            this.animationDuration = 120.0f;
                        }
                    } else {
                        this.animationProgress = 1.0f;
                    }
                    this.prevColorType = i7;
                    this.drawableColor = themedColor;
                    ChatActivityEnterView.this.sendButtonDrawable.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelSend), PorterDuff.Mode.MULTIPLY));
                    int themedColor2 = ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelIcons);
                    ChatActivityEnterView.this.inactinveSendButtonDrawable.setColorFilter(new PorterDuffColorFilter(Color.argb(180, Color.red(themedColor2), Color.green(themedColor2), Color.blue(themedColor2)), PorterDuff.Mode.MULTIPLY));
                    ChatActivityEnterView.this.sendButtonInverseDrawable.setColorFilter(new PorterDuffColorFilter(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelVoicePressed), PorterDuff.Mode.MULTIPLY));
                }
                if (this.animationProgress < 1.0f) {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    float f2 = this.animationProgress + (((float) (elapsedRealtime - this.lastAnimationTime)) / this.animationDuration);
                    this.animationProgress = f2;
                    if (f2 > 1.0f) {
                        this.animationProgress = 1.0f;
                    }
                    this.lastAnimationTime = elapsedRealtime;
                    invalidate();
                }
                if (!z2) {
                    if (ChatActivityEnterView.this.slowModeTimer != Integer.MAX_VALUE || ChatActivityEnterView.this.isInScheduleMode()) {
                        ChatActivityEnterView.this.sendButtonDrawable.setBounds(measuredWidth2, measuredHeight, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicWidth() + measuredWidth2, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicHeight() + measuredHeight);
                        ChatActivityEnterView.this.sendButtonDrawable.draw(canvas);
                    } else {
                        ChatActivityEnterView.this.inactinveSendButtonDrawable.setBounds(measuredWidth2, measuredHeight, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicWidth() + measuredWidth2, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicHeight() + measuredHeight);
                        ChatActivityEnterView.this.inactinveSendButtonDrawable.draw(canvas);
                    }
                }
                if (z2 || this.animationProgress != 1.0f) {
                    ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                    if (chatActivityEnterView.shouldDrawBackground) {
                        Theme.dialogs_onlineCirclePaint.setColor(chatActivityEnterView.getThemedColor(Theme.key_chat_messagePanelSend));
                    } else {
                        Theme.dialogs_onlineCirclePaint.setColor(ColorUtils.setAlphaComponent(-1, 75));
                    }
                    if (z2) {
                        ChatActivityEnterView.this.sendButtonInverseDrawable.setAlpha(255);
                        float f3 = this.animationProgress;
                        if (f3 <= 0.25f) {
                            f = dp4;
                            dp3 = AndroidUtilities.dp(2.0f) * CubicBezierInterpolator.EASE_IN.getInterpolation(f3 / 0.25f);
                        } else {
                            float f4 = f3 - 0.25f;
                            if (f4 <= 0.5f) {
                                f = dp4;
                                dp3 = AndroidUtilities.dp(2.0f) - (AndroidUtilities.dp(3.0f) * CubicBezierInterpolator.EASE_IN.getInterpolation(f4 / 0.5f));
                            } else {
                                f = dp4;
                                dp3 = (-AndroidUtilities.dp(1.0f)) + (AndroidUtilities.dp(1.0f) * CubicBezierInterpolator.EASE_IN.getInterpolation((f4 - 0.5f) / 0.25f));
                            }
                        }
                        dp4 = (int) (f + dp3);
                    } else {
                        int i9 = (int) ((1.0f - this.animationProgress) * 255.0f);
                        Theme.dialogs_onlineCirclePaint.setAlpha(i9);
                        ChatActivityEnterView.this.sendButtonInverseDrawable.setAlpha(i9);
                    }
                    float f5 = measuredWidth;
                    canvas.drawCircle(f5, f5, dp4, Theme.dialogs_onlineCirclePaint);
                    ChatActivityEnterView.this.sendButtonInverseDrawable.setBounds(measuredWidth2, measuredHeight, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicWidth() + measuredWidth2, ChatActivityEnterView.this.sendButtonDrawable.getIntrinsicHeight() + measuredHeight);
                    ChatActivityEnterView.this.sendButtonInverseDrawable.draw(canvas);
                }
            }

            @Override // android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (getAlpha() <= 0.0f) {
                    return false;
                }
                return super.onTouchEvent(motionEvent);
            }
        };
        this.sendButton = view;
        view.setVisibility(4);
        int themedColor = getThemedColor(Theme.key_chat_messagePanelSend);
        this.sendButton.setContentDescription(LocaleController.getString("Send", R.string.Send));
        this.sendButton.setSoundEffectsEnabled(false);
        this.sendButton.setScaleX(0.1f);
        this.sendButton.setScaleY(0.1f);
        this.sendButton.setAlpha(0.0f);
        if (i4 >= 21) {
            this.sendButton.setBackgroundDrawable(Theme.createSelectorDrawable(ColorUtils.setAlphaComponent(themedColor, 24), 1));
        }
        this.sendButtonContainer.addView(this.sendButton, LayoutHelper.createFrame(48, 48.0f));
        this.sendButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ChatActivityEnterView.this.lambda$new$4(view2);
            }
        });
        this.sendButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda26
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view2) {
                boolean onSendLongClick;
                onSendLongClick = ChatActivityEnterView.this.onSendLongClick(view2);
                return onSendLongClick;
            }
        });
        SimpleTextView simpleTextView = new SimpleTextView(activity);
        this.slowModeButton = simpleTextView;
        simpleTextView.setTextSize(18);
        this.slowModeButton.setVisibility(4);
        this.slowModeButton.setSoundEffectsEnabled(false);
        this.slowModeButton.setScaleX(0.1f);
        this.slowModeButton.setScaleY(0.1f);
        this.slowModeButton.setAlpha(0.0f);
        this.slowModeButton.setPadding(0, 0, AndroidUtilities.dp(13.0f), 0);
        this.slowModeButton.setGravity(21);
        this.slowModeButton.setTextColor(getThemedColor(i3));
        this.sendButtonContainer.addView(this.slowModeButton, LayoutHelper.createFrame(64, 48, 53));
        this.slowModeButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ChatActivityEnterView.this.lambda$new$5(view2);
            }
        });
        this.slowModeButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda27
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view2) {
                boolean lambda$new$6;
                lambda$new$6 = ChatActivityEnterView.this.lambda$new$6(view2);
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

    public /* synthetic */ void lambda$new$1(View view) {
        AdjustPanLayoutHelper adjustPanLayoutHelper = this.adjustPanLayoutHelper;
        if (adjustPanLayoutHelper == null || !adjustPanLayoutHelper.animationInProgress()) {
            if (this.emojiButtonRestricted) {
                showRestrictedHint();
            } else if (hasBotWebView() && botCommandsMenuIsShowing()) {
                BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
                if (botWebViewMenuContainer != null) {
                    Objects.requireNonNull(view);
                    botWebViewMenuContainer.dismiss(new ChatActivityEnterView$$ExternalSyntheticLambda31(view));
                }
            } else {
                boolean z = true;
                if (!isPopupShowing() || this.currentPopupContentType != 0) {
                    showPopup(1, 0);
                    EmojiView emojiView = this.emojiView;
                    EditTextCaption editTextCaption = this.messageEditText;
                    emojiView.onOpen((editTextCaption == null || editTextCaption.length() <= 0) ? false : false);
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
                if (this.stickersExpanded) {
                    setStickersExpanded(false, true, false);
                    this.waitingForKeyboardOpenAfterAnimation = true;
                    AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda36
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatActivityEnterView.this.lambda$new$0();
                        }
                    }, 200L);
                    return;
                }
                openKeyboardInternal();
            }
        }
    }

    public /* synthetic */ void lambda$new$0() {
        this.waitingForKeyboardOpenAfterAnimation = false;
        openKeyboardInternal();
    }

    public /* synthetic */ void lambda$new$2(View view) {
        AdjustPanLayoutHelper adjustPanLayoutHelper = this.adjustPanLayoutHelper;
        if ((adjustPanLayoutHelper == null || !adjustPanLayoutHelper.animationInProgress()) && this.attachLayoutPaddingAlpha != 0.0f) {
            this.delegate.didPressAttachButton();
        }
    }

    /* loaded from: classes4.dex */
    public class 19 extends FrameLayout {
        final /* synthetic */ Theme.ResourcesProvider val$resourcesProvider;

        @Override // android.view.ViewGroup
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            return true;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        19(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context);
            ChatActivityEnterView.this = r1;
            this.val$resourcesProvider = resourcesProvider;
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            FrameLayout frameLayout;
            ChatActivityEnterView chatActivityEnterView;
            ChatActivityEnterView.this.createRecordCircle();
            if (motionEvent.getAction() == 0) {
                if (ChatActivityEnterView.this.recordCircle.isSendButtonVisible()) {
                    if (!ChatActivityEnterView.this.hasRecordVideo || ChatActivityEnterView.this.calledRecordRunnable) {
                        ChatActivityEnterView.this.startedDraggingX = -1.0f;
                        if (!ChatActivityEnterView.this.hasRecordVideo || !ChatActivityEnterView.this.isInVideoMode()) {
                            if (ChatActivityEnterView.this.recordingAudioVideo && ChatActivityEnterView.this.isInScheduleMode()) {
                                AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment.getDialogId(), ChatActivityEnterView$19$$ExternalSyntheticLambda4.INSTANCE, ChatActivityEnterView$19$$ExternalSyntheticLambda2.INSTANCE, this.val$resourcesProvider);
                            }
                            MediaController.getInstance().stopRecording(ChatActivityEnterView.this.isInScheduleMode() ? 3 : 1, true, 0);
                            ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                        } else {
                            ChatActivityEnterView.this.delegate.needStartRecordVideo(1, true, 0);
                        }
                        ChatActivityEnterView.this.recordingAudioVideo = false;
                        ChatActivityEnterView chatActivityEnterView2 = ChatActivityEnterView.this;
                        chatActivityEnterView2.messageTransitionIsRunning = false;
                        AndroidUtilities.runOnUIThread(chatActivityEnterView2.moveToSendStateRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$19$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.19.this.lambda$onTouchEvent$2();
                            }
                        }, 200L);
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                TLRPC$Chat currentChat = ChatActivityEnterView.this.parentFragment == null ? null : ChatActivityEnterView.this.parentFragment.getCurrentChat();
                TLRPC$UserFull currentUserInfo = ChatActivityEnterView.this.parentFragment == null ? ChatActivityEnterView.this.userInfo : ChatActivityEnterView.this.parentFragment.getCurrentUserInfo();
                if ((currentChat == null || ChatObject.canSendVoice(currentChat) || (ChatObject.canSendRoundVideo(currentChat) && ChatActivityEnterView.this.hasRecordVideo)) && (currentUserInfo == null || !currentUserInfo.voice_messages_forbidden)) {
                    if (ChatActivityEnterView.this.hasRecordVideo) {
                        ChatActivityEnterView.this.calledRecordRunnable = false;
                        ChatActivityEnterView.this.recordAudioVideoRunnableStarted = true;
                        AndroidUtilities.runOnUIThread(ChatActivityEnterView.this.recordAudioVideoRunnable, 150L);
                    } else {
                        ChatActivityEnterView.this.recordAudioVideoRunnable.run();
                    }
                    return true;
                }
                ChatActivityEnterView.this.delegate.needShowMediaBanHint();
                return true;
            }
            if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                if (motionEvent.getAction() != 3 || !ChatActivityEnterView.this.recordingAudioVideo) {
                    if ((ChatActivityEnterView.this.recordCircle == null || !ChatActivityEnterView.this.recordCircle.isSendButtonVisible()) && ((frameLayout = ChatActivityEnterView.this.recordedAudioPanel) == null || frameLayout.getVisibility() != 0)) {
                        if ((((motionEvent.getX() + ChatActivityEnterView.this.audioVideoButtonContainer.getX()) - ChatActivityEnterView.this.startedDraggingX) / ChatActivityEnterView.this.distCanMove) + 1.0f < 0.45d) {
                            if (!ChatActivityEnterView.this.hasRecordVideo || !ChatActivityEnterView.this.isInVideoMode()) {
                                ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                                MediaController.getInstance().stopRecording(0, false, 0);
                            } else {
                                CameraController.getInstance().cancelOnInitRunnable(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                                ChatActivityEnterView.this.delegate.needStartRecordVideo(2, true, 0);
                            }
                            ChatActivityEnterView.this.recordingAudioVideo = false;
                            ChatActivityEnterView.this.updateRecordInterface(5);
                        } else if (ChatActivityEnterView.this.recordAudioVideoRunnableStarted) {
                            AndroidUtilities.cancelRunOnUIThread(ChatActivityEnterView.this.recordAudioVideoRunnable);
                            if (!ChatActivityEnterView.this.sendVoiceEnabled || !ChatActivityEnterView.this.sendRoundEnabled) {
                                ChatActivityEnterView.this.delegate.needShowMediaBanHint();
                            } else {
                                ChatActivityEnterView.this.delegate.onSwitchRecordMode(!ChatActivityEnterView.this.isInVideoMode());
                                ChatActivityEnterView.this.setRecordVideoButtonVisible(!chatActivityEnterView.isInVideoMode(), true);
                            }
                            performHapticFeedback(3);
                            sendAccessibilityEvent(1);
                        } else if (!ChatActivityEnterView.this.hasRecordVideo || ChatActivityEnterView.this.calledRecordRunnable) {
                            ChatActivityEnterView.this.startedDraggingX = -1.0f;
                            if (!ChatActivityEnterView.this.hasRecordVideo || !ChatActivityEnterView.this.isInVideoMode()) {
                                if (!ChatActivityEnterView.this.sendVoiceEnabled) {
                                    ChatActivityEnterView.this.delegate.needShowMediaBanHint();
                                } else {
                                    if (ChatActivityEnterView.this.recordingAudioVideo && ChatActivityEnterView.this.isInScheduleMode()) {
                                        AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment.getDialogId(), ChatActivityEnterView$19$$ExternalSyntheticLambda5.INSTANCE, ChatActivityEnterView$19$$ExternalSyntheticLambda3.INSTANCE, this.val$resourcesProvider);
                                    }
                                    ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                                    MediaController.getInstance().stopRecording(ChatActivityEnterView.this.isInScheduleMode() ? 3 : 1, true, 0);
                                }
                            } else {
                                CameraController.getInstance().cancelOnInitRunnable(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                                ChatActivityEnterView.this.delegate.needStartRecordVideo(1, true, 0);
                            }
                            ChatActivityEnterView.this.recordingAudioVideo = false;
                            ChatActivityEnterView chatActivityEnterView3 = ChatActivityEnterView.this;
                            chatActivityEnterView3.messageTransitionIsRunning = false;
                            AndroidUtilities.runOnUIThread(chatActivityEnterView3.moveToSendStateRunnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$19$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ChatActivityEnterView.19.this.lambda$onTouchEvent$5();
                                }
                            }, ChatActivityEnterView.this.shouldDrawBackground ? 500L : 0L);
                        }
                        return true;
                    }
                    if (ChatActivityEnterView.this.recordAudioVideoRunnableStarted) {
                        AndroidUtilities.cancelRunOnUIThread(ChatActivityEnterView.this.recordAudioVideoRunnable);
                    }
                    return false;
                }
                if (ChatActivityEnterView.this.recordCircle.slideToCancelProgress < 0.7f) {
                    if (!ChatActivityEnterView.this.hasRecordVideo || !ChatActivityEnterView.this.isInVideoMode()) {
                        ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                        MediaController.getInstance().stopRecording(0, false, 0);
                    } else {
                        CameraController.getInstance().cancelOnInitRunnable(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                        ChatActivityEnterView.this.delegate.needStartRecordVideo(2, true, 0);
                    }
                    ChatActivityEnterView.this.recordingAudioVideo = false;
                    ChatActivityEnterView.this.updateRecordInterface(5);
                } else {
                    ChatActivityEnterView.this.recordCircle.sendButtonVisible = true;
                    ChatActivityEnterView.this.startLockTransition();
                }
                return false;
            }
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
                    ChatActivityEnterView chatActivityEnterView4 = ChatActivityEnterView.this;
                    double measuredWidth = chatActivityEnterView4.sizeNotifierLayout.getMeasuredWidth();
                    Double.isNaN(measuredWidth);
                    chatActivityEnterView4.distCanMove = (float) (measuredWidth * 0.35d);
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
                    if (ChatActivityEnterView.this.recordCircle != null) {
                        ChatActivityEnterView.this.recordCircle.setSlideToCancelProgress(f);
                    }
                    x2 = f;
                }
                if (x2 == 0.0f) {
                    if (!ChatActivityEnterView.this.hasRecordVideo || !ChatActivityEnterView.this.isInVideoMode()) {
                        ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                        MediaController.getInstance().stopRecording(0, false, 0);
                    } else {
                        CameraController.getInstance().cancelOnInitRunnable(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                        ChatActivityEnterView.this.delegate.needStartRecordVideo(2, true, 0);
                    }
                    ChatActivityEnterView.this.recordingAudioVideo = false;
                    ChatActivityEnterView.this.updateRecordInterface(5);
                }
            }
            return true;
        }

        public static /* synthetic */ void lambda$onTouchEvent$0(boolean z, int i) {
            MediaController.getInstance().stopRecording(1, z, i);
        }

        public static /* synthetic */ void lambda$onTouchEvent$1() {
            MediaController.getInstance().stopRecording(0, false, 0);
        }

        public /* synthetic */ void lambda$onTouchEvent$2() {
            ChatActivityEnterView.this.moveToSendStateRunnable = null;
            ChatActivityEnterView.this.updateRecordInterface(1);
        }

        public static /* synthetic */ void lambda$onTouchEvent$3(boolean z, int i) {
            MediaController.getInstance().stopRecording(1, z, i);
        }

        public static /* synthetic */ void lambda$onTouchEvent$4() {
            MediaController.getInstance().stopRecording(0, false, 0);
        }

        public /* synthetic */ void lambda$onTouchEvent$5() {
            ChatActivityEnterView.this.moveToSendStateRunnable = null;
            ChatActivityEnterView.this.updateRecordInterface(1);
        }
    }

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

    public /* synthetic */ void lambda$new$4(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow == null || !actionBarPopupWindow.isShowing()) {
            AnimatorSet animatorSet = this.runningAnimationAudio;
            if ((animatorSet == null || !animatorSet.isRunning()) && this.moveToSendStateRunnable == null) {
                sendMessage();
            }
        }
    }

    public /* synthetic */ void lambda$new$5(View view) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            SimpleTextView simpleTextView = this.slowModeButton;
            chatActivityEnterViewDelegate.onUpdateSlowModeButton(simpleTextView, true, simpleTextView.getText());
        }
    }

    public /* synthetic */ boolean lambda$new$6(View view) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null || editTextCaption.length() <= 0) {
            return false;
        }
        return onSendLongClick(view);
    }

    public void createCaptionLimitView() {
        if (this.captionLimitView != null) {
            return;
        }
        NumberTextView numberTextView = new NumberTextView(getContext());
        this.captionLimitView = numberTextView;
        numberTextView.setVisibility(8);
        this.captionLimitView.setTextSize(15);
        this.captionLimitView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
        this.captionLimitView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.captionLimitView.setCenterAlign(true);
        addView(this.captionLimitView, 3, LayoutHelper.createFrame(48, 20.0f, 85, 3.0f, 0.0f, 0.0f, 48.0f));
    }

    private void createScheduledButton() {
        if (this.scheduledButton != null || this.parentFragment == null) {
            return;
        }
        Drawable mutate = getContext().getResources().getDrawable(R.drawable.input_calendar1).mutate();
        Drawable mutate2 = getContext().getResources().getDrawable(R.drawable.input_calendar2).mutate();
        mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_messagePanelIcons), PorterDuff.Mode.MULTIPLY));
        mutate2.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_recordedVoiceDot), PorterDuff.Mode.MULTIPLY));
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
        this.scheduledButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createScheduledButton$7(view);
            }
        });
    }

    public /* synthetic */ void lambda$createScheduledButton$7(View view) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.openScheduledMessages();
        }
    }

    private void createGiftButton() {
        if (this.giftButton != null || this.parentFragment == null) {
            return;
        }
        ImageView imageView = new ImageView(getContext());
        this.giftButton = imageView;
        imageView.setImageResource(R.drawable.msg_input_gift);
        this.giftButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_messagePanelIcons), PorterDuff.Mode.MULTIPLY));
        this.giftButton.setVisibility(8);
        this.giftButton.setContentDescription(LocaleController.getString(R.string.GiftPremium));
        this.giftButton.setScaleType(ImageView.ScaleType.CENTER);
        if (Build.VERSION.SDK_INT >= 21) {
            this.giftButton.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
        this.attachLayout.addView(this.giftButton, 0, LayoutHelper.createFrame(48, 48, 21));
        this.giftButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda24
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createGiftButton$8(view);
            }
        });
    }

    public /* synthetic */ void lambda$createGiftButton$8(View view) {
        SharedPreferences.Editor edit = MessagesController.getInstance(this.currentAccount).getMainSettings().edit();
        edit.putBoolean("show_gift_for_" + this.parentFragment.getDialogId(), false).apply();
        AndroidUtilities.updateViewVisibilityAnimated(this.giftButton, false);
        new GiftPremiumBottomSheet(getParentFragment(), getParentFragment().getCurrentUser()).show();
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
        this.botButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda23
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createBotButton$9(view);
            }
        });
    }

    public /* synthetic */ void lambda$createBotButton$9(View view) {
        if (hasBotWebView() && botCommandsMenuIsShowing()) {
            BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
            Objects.requireNonNull(view);
            botWebViewMenuContainer.dismiss(new ChatActivityEnterView$$ExternalSyntheticLambda31(view));
            return;
        }
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
        } else if (this.hasBotCommands) {
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

    private void createDoneButton() {
        if (this.doneButtonContainer != null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext());
        this.doneButtonContainer = frameLayout;
        frameLayout.setVisibility(8);
        this.textFieldContainer.addView(this.doneButtonContainer, LayoutHelper.createFrame(48, 48, 85));
        this.doneButtonContainer.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createDoneButton$10(view);
            }
        });
        ShapeDrawable createCircleDrawable = Theme.createCircleDrawable(AndroidUtilities.dp(16.0f), getThemedColor(Theme.key_chat_messagePanelSend));
        Drawable mutate = getContext().getResources().getDrawable(R.drawable.input_done).mutate();
        this.doneCheckDrawable = mutate;
        mutate.setColorFilter(new PorterDuffColorFilter(getThemedColor(Theme.key_chat_messagePanelVoicePressed), PorterDuff.Mode.MULTIPLY));
        CombinedDrawable combinedDrawable = new CombinedDrawable(createCircleDrawable, this.doneCheckDrawable, 0, AndroidUtilities.dp(1.0f));
        combinedDrawable.setCustomSize(AndroidUtilities.dp(32.0f), AndroidUtilities.dp(32.0f));
        ImageView imageView = new ImageView(getContext());
        this.doneButtonImage = imageView;
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        this.doneButtonImage.setImageDrawable(combinedDrawable);
        this.doneButtonImage.setContentDescription(LocaleController.getString("Done", R.string.Done));
        this.doneButtonContainer.addView(this.doneButtonImage, LayoutHelper.createFrame(48, 48.0f));
        ContextProgressView contextProgressView = new ContextProgressView(getContext(), 0);
        this.doneButtonProgress = contextProgressView;
        contextProgressView.setVisibility(4);
        this.doneButtonContainer.addView(this.doneButtonProgress, LayoutHelper.createFrame(-1, -1.0f));
    }

    public /* synthetic */ void lambda$createDoneButton$10(View view) {
        doneEditingMessage();
    }

    private void createExpandStickersButton() {
        if (this.expandStickersButton != null) {
            return;
        }
        ImageView imageView = new ImageView(this, getContext()) { // from class: org.telegram.ui.Components.ChatActivityEnterView.22
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
        this.expandStickersButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda21
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createExpandStickersButton$11(view);
            }
        });
        this.expandStickersButton.setContentDescription(LocaleController.getString("AccDescrExpandPanel", R.string.AccDescrExpandPanel));
    }

    public /* synthetic */ void lambda$createExpandStickersButton$11(View view) {
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

    private void createRecordAudioPanel() {
        if (this.recordedAudioPanel != null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext()) { // from class: org.telegram.ui.Components.ChatActivityEnterView.23
            {
                ChatActivityEnterView.this = this;
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
        this.recordDeleteImageView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createRecordAudioPanel$12(view);
            }
        });
        VideoTimelineView videoTimelineView = new VideoTimelineView(getContext());
        this.videoTimelineView = videoTimelineView;
        videoTimelineView.useClip = !this.shouldDrawBackground;
        videoTimelineView.setRoundFrames(true);
        this.videoTimelineView.setDelegate(new VideoTimelineView.VideoTimelineViewDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView.24
            {
                ChatActivityEnterView.this = this;
            }

            @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
            public void onLeftProgressChanged(float f) {
                if (ChatActivityEnterView.this.videoToSendMessageObject == null) {
                    return;
                }
                ChatActivityEnterView.this.videoToSendMessageObject.startTime = ((float) ChatActivityEnterView.this.videoToSendMessageObject.estimatedDuration) * f;
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(2, f);
            }

            @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
            public void onRightProgressChanged(float f) {
                if (ChatActivityEnterView.this.videoToSendMessageObject == null) {
                    return;
                }
                ChatActivityEnterView.this.videoToSendMessageObject.endTime = ((float) ChatActivityEnterView.this.videoToSendMessageObject.estimatedDuration) * f;
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(2, f);
            }

            @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
            public void didStartDragging() {
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(1, 0.0f);
            }

            @Override // org.telegram.ui.Components.VideoTimelineView.VideoTimelineViewDelegate
            public void didStopDragging() {
                ChatActivityEnterView.this.delegate.needChangeVideoPreviewState(0, 0.0f);
            }
        });
        this.recordedAudioPanel.addView(this.videoTimelineView, LayoutHelper.createFrame(-1, -1.0f, 19, 56.0f, 0.0f, 8.0f, 0.0f));
        VideoTimelineView.TimeHintView timeHintView = new VideoTimelineView.TimeHintView(getContext());
        this.videoTimelineView.setTimeHintView(timeHintView);
        this.sizeNotifierLayout.addView(timeHintView, LayoutHelper.createFrame(-1, -2.0f, 80, 0.0f, 0.0f, 0.0f, 52.0f));
        View view = new View(getContext()) { // from class: org.telegram.ui.Components.ChatActivityEnterView.25
            {
                ChatActivityEnterView.this = this;
            }

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
        this.recordedAudioPlayButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda17
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ChatActivityEnterView.this.lambda$createRecordAudioPanel$13(view2);
            }
        });
        SeekBarWaveformView seekBarWaveformView = new SeekBarWaveformView(getContext());
        this.recordedAudioSeekBar = seekBarWaveformView;
        linearLayout.addView(seekBarWaveformView, LayoutHelper.createLinear(0, 32, 1.0f, 16, 0, 0, 4, 0));
        TextView textView = new TextView(getContext());
        this.recordedAudioTimeTextView = textView;
        textView.setTextColor(getThemedColor(Theme.key_chat_messagePanelVoiceDuration));
        this.recordedAudioTimeTextView.setTextSize(1, 13.0f);
        linearLayout.addView(this.recordedAudioTimeTextView, LayoutHelper.createLinear(-2, -2, 0.0f, 16));
    }

    public /* synthetic */ void lambda$createRecordAudioPanel$12(View view) {
        AnimatorSet animatorSet = this.runningAnimationAudio;
        if (animatorSet == null || !animatorSet.isRunning()) {
            resetRecordedState();
        }
    }

    public /* synthetic */ void lambda$createRecordAudioPanel$13(View view) {
        if (this.audioToSend == null) {
            return;
        }
        if (MediaController.getInstance().isPlayingMessage(this.audioToSendMessageObject) && !MediaController.getInstance().isMessagePaused()) {
            MediaController.getInstance().lambda$startAudioAgain$7(this.audioToSendMessageObject);
            this.playPauseDrawable.setIcon(0, true);
            this.recordedAudioPlayButton.setContentDescription(LocaleController.getString("AccActionPlay", R.string.AccActionPlay));
            return;
        }
        this.playPauseDrawable.setIcon(1, true);
        MediaController.getInstance().playMessage(this.audioToSendMessageObject);
        this.recordedAudioPlayButton.setContentDescription(LocaleController.getString("AccActionPause", R.string.AccActionPause));
    }

    private void resetRecordedState() {
        if (this.videoToSendMessageObject != null) {
            CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
            this.delegate.needStartRecordVideo(2, true, 0);
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
        hideRecordedAudioPanel(false);
        checkSendButton(true);
    }

    public void createSenderSelectView() {
        if (this.senderSelectView != null) {
            return;
        }
        SenderSelectView senderSelectView = new SenderSelectView(getContext());
        this.senderSelectView = senderSelectView;
        senderSelectView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createSenderSelectView$20(view);
            }
        });
        this.senderSelectView.setVisibility(8);
        this.messageEditTextContainer.addView(this.senderSelectView, LayoutHelper.createFrame(32, 32.0f, 83, 10.0f, 8.0f, 10.0f, 8.0f));
    }

    public /* synthetic */ void lambda$createSenderSelectView$20(View view) {
        int i;
        int i2;
        if (getTranslationY() != 0.0f) {
            this.onEmojiSearchClosed = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda46
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$createSenderSelectView$14();
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
                this.onKeyboardClosed = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda33
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActivityEnterView.this.lambda$createSenderSelectView$15();
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
            final TLRPC$ChatFull chatFull = messagesController.getChatFull(-this.dialog_id);
            if (chatFull == null) {
                return;
            }
            final FrameLayout overlayContainerView = this.parentFragment.getParentLayout().getOverlayContainerView();
            SenderSelectPopup senderSelectPopup2 = new SenderSelectPopup(getContext(), this.parentFragment, messagesController, chatFull, this.delegate.getSendAsPeers(), new SenderSelectPopup.OnSelectCallback() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda60
                @Override // org.telegram.ui.Components.SenderSelectPopup.OnSelectCallback
                public final void onPeerSelected(RecyclerView recyclerView, SenderSelectPopup.SenderView senderView, TLRPC$Peer tLRPC$Peer) {
                    ChatActivityEnterView.this.lambda$createSenderSelectView$19(chatFull, messagesController, recyclerView, senderView, tLRPC$Peer);
                }
            }) { // from class: org.telegram.ui.Components.ChatActivityEnterView.26
                {
                    ChatActivityEnterView.this = this;
                }

                @Override // org.telegram.ui.Components.SenderSelectPopup, org.telegram.ui.ActionBar.ActionBarPopupWindow, android.widget.PopupWindow
                public void dismiss() {
                    if (ChatActivityEnterView.this.senderSelectPopupWindow == this) {
                        ChatActivityEnterView.this.senderSelectPopupWindow = null;
                        if (!this.runningCustomSprings) {
                            startDismissAnimation(new SpringAnimation[0]);
                            ChatActivityEnterView.this.senderSelectView.setProgress(0.0f, true, true);
                            return;
                        }
                        for (SpringAnimation springAnimation : this.springAnimations) {
                            springAnimation.cancel();
                        }
                        this.springAnimations.clear();
                        super.dismiss();
                        return;
                    }
                    overlayContainerView.removeView(this.dimView);
                    super.dismiss();
                }
            };
            this.senderSelectPopupWindow = senderSelectPopup2;
            senderSelectPopup2.setPauseNotifications(true);
            this.senderSelectPopupWindow.setDismissAnimationDuration(220);
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

    public /* synthetic */ void lambda$createSenderSelectView$14() {
        this.senderSelectView.callOnClick();
    }

    public /* synthetic */ void lambda$createSenderSelectView$15() {
        this.senderSelectView.callOnClick();
    }

    public /* synthetic */ void lambda$createSenderSelectView$19(TLRPC$ChatFull tLRPC$ChatFull, MessagesController messagesController, RecyclerView recyclerView, final SenderSelectPopup.SenderView senderView, TLRPC$Peer tLRPC$Peer) {
        TLRPC$User user;
        if (this.senderSelectPopupWindow == null) {
            return;
        }
        if (tLRPC$ChatFull != null) {
            tLRPC$ChatFull.default_send_as = tLRPC$Peer;
            updateSendAsButton();
        }
        MessagesController messagesController2 = this.parentFragment.getMessagesController();
        long j = this.dialog_id;
        long j2 = tLRPC$Peer.user_id;
        if (j2 == 0) {
            j2 = -tLRPC$Peer.channel_id;
        }
        messagesController2.setDefaultSendAs(j, j2);
        final int[] iArr = new int[2];
        boolean isSelected = senderView.avatar.isSelected();
        senderView.avatar.getLocationInWindow(iArr);
        senderView.avatar.setSelected(true, true);
        final SimpleAvatarView simpleAvatarView = new SimpleAvatarView(getContext());
        long j3 = tLRPC$Peer.channel_id;
        if (j3 != 0) {
            TLRPC$Chat chat = messagesController.getChat(Long.valueOf(j3));
            if (chat != null) {
                simpleAvatarView.setAvatar(chat);
            }
        } else {
            long j4 = tLRPC$Peer.user_id;
            if (j4 != 0 && (user = messagesController.getUser(Long.valueOf(j4))) != null) {
                simpleAvatarView.setAvatar(user);
            }
        }
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View childAt = recyclerView.getChildAt(i);
            if ((childAt instanceof SenderSelectPopup.SenderView) && childAt != senderView) {
                ((SenderSelectPopup.SenderView) childAt).avatar.setSelected(false, true);
            }
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda54
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$createSenderSelectView$18(simpleAvatarView, iArr, senderView);
            }
        }, isSelected ? 0L : 200L);
    }

    public /* synthetic */ void lambda$createSenderSelectView$18(final SimpleAvatarView simpleAvatarView, int[] iArr, SenderSelectPopup.SenderView senderView) {
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
            dialog.getWindow().addFlags(LiteMode.FLAG_CALLS_ANIMATIONS);
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
            this.popupX += getRootWindowInsets().getSystemWindowInsetLeft();
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
        simpleAvatarView.getViewTreeObserver().addOnDrawListener(new 27(this, simpleAvatarView, senderView));
        dialog.show();
        this.senderSelectView.setScaleX(1.0f);
        this.senderSelectView.setScaleY(1.0f);
        this.senderSelectView.setAlpha(1.0f);
        SenderSelectPopup senderSelectPopup = this.senderSelectPopupWindow;
        SenderSelectView senderSelectView = this.senderSelectView;
        DynamicAnimation.ViewProperty viewProperty = DynamicAnimation.SCALE_X;
        SenderSelectView senderSelectView2 = this.senderSelectView;
        DynamicAnimation.ViewProperty viewProperty2 = DynamicAnimation.SCALE_Y;
        senderSelectPopup.startDismissAnimation(new SpringAnimation(senderSelectView, viewProperty).setSpring(new SpringForce(0.5f).setStiffness(750.0f).setDampingRatio(1.0f)), new SpringAnimation(senderSelectView2, viewProperty2).setSpring(new SpringForce(0.5f).setStiffness(750.0f).setDampingRatio(1.0f)), new SpringAnimation(this.senderSelectView, DynamicAnimation.ALPHA).setSpring(new SpringForce(0.0f).setStiffness(750.0f).setDampingRatio(1.0f)).addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda29
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
            public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f4, float f5) {
                ChatActivityEnterView.this.lambda$createSenderSelectView$16(dialog, simpleAvatarView, f, f2, dynamicAnimation, z, f4, f5);
            }
        }), new SpringAnimation(simpleAvatarView, DynamicAnimation.TRANSLATION_X).setStartValue(MathUtils.clamp(dp2, f - AndroidUtilities.dp(6.0f), dp2)).setSpring(new SpringForce(f).setStiffness(700.0f).setDampingRatio(0.75f)).setMinValue(f - AndroidUtilities.dp(6.0f)), new SpringAnimation(simpleAvatarView, DynamicAnimation.TRANSLATION_Y).setStartValue(MathUtils.clamp(f3, f3, AndroidUtilities.dp(6.0f) + f2)).setSpring(new SpringForce(f2).setStiffness(700.0f).setDampingRatio(0.75f)).setMaxValue(AndroidUtilities.dp(6.0f) + f2).addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener(this) { // from class: org.telegram.ui.Components.ChatActivityEnterView.29
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
        }).addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda30
            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
            public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f4, float f5) {
                ChatActivityEnterView.this.lambda$createSenderSelectView$17(dialog, simpleAvatarView, f, f2, dynamicAnimation, z, f4, f5);
            }
        }), new SpringAnimation(simpleAvatarView, viewProperty).setSpring(new SpringForce(dp3).setStiffness(1000.0f).setDampingRatio(1.0f)), new SpringAnimation(simpleAvatarView, viewProperty2).setSpring(new SpringForce(dp3).setStiffness(1000.0f).setDampingRatio(1.0f)));
    }

    /* loaded from: classes4.dex */
    public class 27 implements ViewTreeObserver.OnDrawListener {
        final /* synthetic */ SimpleAvatarView val$avatar;
        final /* synthetic */ SenderSelectPopup.SenderView val$senderView;

        27(ChatActivityEnterView chatActivityEnterView, SimpleAvatarView simpleAvatarView, SenderSelectPopup.SenderView senderView) {
            this.val$avatar = simpleAvatarView;
            this.val$senderView = senderView;
        }

        @Override // android.view.ViewTreeObserver.OnDrawListener
        public void onDraw() {
            final SimpleAvatarView simpleAvatarView = this.val$avatar;
            final SenderSelectPopup.SenderView senderView = this.val$senderView;
            simpleAvatarView.post(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$27$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.27.this.lambda$onDraw$0(simpleAvatarView, senderView);
                }
            });
        }

        public /* synthetic */ void lambda$onDraw$0(SimpleAvatarView simpleAvatarView, SenderSelectPopup.SenderView senderView) {
            simpleAvatarView.getViewTreeObserver().removeOnDrawListener(this);
            senderView.avatar.setHideAvatar(true);
        }
    }

    public /* synthetic */ void lambda$createSenderSelectView$16(final Dialog dialog, SimpleAvatarView simpleAvatarView, float f, float f2, DynamicAnimation dynamicAnimation, boolean z, float f3, float f4) {
        if (dialog.isShowing()) {
            simpleAvatarView.setTranslationX(f);
            simpleAvatarView.setTranslationY(f2);
            this.senderSelectView.setProgress(0.0f, false);
            this.senderSelectView.setScaleX(1.0f);
            this.senderSelectView.setScaleY(1.0f);
            this.senderSelectView.setAlpha(1.0f);
            this.senderSelectView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.28
                {
                    ChatActivityEnterView.this = this;
                }

                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    ChatActivityEnterView.this.senderSelectView.getViewTreeObserver().removeOnPreDrawListener(this);
                    SenderSelectView senderSelectView = ChatActivityEnterView.this.senderSelectView;
                    Dialog dialog2 = dialog;
                    Objects.requireNonNull(dialog2);
                    senderSelectView.postDelayed(new ChatActivityEnterView$28$$ExternalSyntheticLambda0(dialog2), 100L);
                    return true;
                }
            });
        }
    }

    public /* synthetic */ void lambda$createSenderSelectView$17(final Dialog dialog, SimpleAvatarView simpleAvatarView, float f, float f2, DynamicAnimation dynamicAnimation, boolean z, float f3, float f4) {
        if (dialog.isShowing()) {
            simpleAvatarView.setTranslationX(f);
            simpleAvatarView.setTranslationY(f2);
            this.senderSelectView.setProgress(0.0f, false);
            this.senderSelectView.setScaleX(1.0f);
            this.senderSelectView.setScaleY(1.0f);
            this.senderSelectView.setAlpha(1.0f);
            this.senderSelectView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.30
                {
                    ChatActivityEnterView.this = this;
                }

                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    ChatActivityEnterView.this.senderSelectView.getViewTreeObserver().removeOnPreDrawListener(this);
                    SenderSelectView senderSelectView = ChatActivityEnterView.this.senderSelectView;
                    Dialog dialog2 = dialog;
                    Objects.requireNonNull(dialog2);
                    senderSelectView.postDelayed(new ChatActivityEnterView$28$$ExternalSyntheticLambda0(dialog2), 100L);
                    return true;
                }
            });
        }
    }

    private void createBotCommandsMenuButton() {
        if (this.botCommandsMenuButton != null) {
            return;
        }
        BotCommandsMenuView botCommandsMenuView = new BotCommandsMenuView(getContext());
        this.botCommandsMenuButton = botCommandsMenuView;
        botCommandsMenuView.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda15
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChatActivityEnterView.this.lambda$createBotCommandsMenuButton$21(view);
            }
        });
        this.messageEditTextContainer.addView(this.botCommandsMenuButton, LayoutHelper.createFrame(-2, 32.0f, 83, 10.0f, 8.0f, 10.0f, 8.0f));
        AndroidUtilities.updateViewVisibilityAnimated(this.botCommandsMenuButton, false, 1.0f, false);
        this.botCommandsMenuButton.setExpanded(true, false);
    }

    public /* synthetic */ void lambda$createBotCommandsMenuButton$21(View view) {
        boolean z = !this.botCommandsMenuButton.isOpened();
        this.botCommandsMenuButton.setOpened(z);
        try {
            performHapticFeedback(3, 2);
        } catch (Exception unused) {
        }
        if (!hasBotWebView()) {
            if (z) {
                createBotCommandsMenuContainer();
                this.botCommandsMenuContainer.show();
                return;
            }
            BotCommandsMenuContainer botCommandsMenuContainer = this.botCommandsMenuContainer;
            if (botCommandsMenuContainer != null) {
                botCommandsMenuContainer.dismiss();
            }
        } else if (z) {
            if (this.emojiViewVisible || this.botKeyboardViewVisible) {
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda44
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActivityEnterView.this.openWebViewMenu();
                    }
                }, 275L);
                hidePopup(false);
                return;
            }
            openWebViewMenu();
        } else {
            BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
            if (botWebViewMenuContainer != null) {
                botWebViewMenuContainer.dismiss();
            }
        }
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

    public void createRecordCircle() {
        if (this.recordCircle != null) {
            return;
        }
        RecordCircle recordCircle = new RecordCircle(getContext());
        this.recordCircle = recordCircle;
        recordCircle.setVisibility(8);
        this.sizeNotifierLayout.addView(this.recordCircle, LayoutHelper.createFrame(-1, -2, 80));
    }

    public void showRestrictedHint() {
        if (DialogObject.isChatDialog(this.dialog_id)) {
            BulletinFactory.of(this.parentFragment).createSimpleBulletin(R.raw.passcode_lock_close, LocaleController.formatString("SendPlainTextRestrictionHint", R.string.SendPlainTextRestrictionHint, ChatObject.getAllowedSendString(this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id)))), 3).show();
        }
    }

    public void openWebViewMenu() {
        createBotWebViewMenuContainer();
        final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda42
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$openWebViewMenu$22();
            }
        };
        if (SharedPrefsHelper.isWebViewConfirmShown(this.currentAccount, this.dialog_id)) {
            runnable.run();
        } else {
            AlertsCreator.createBotLaunchAlert(this.parentFragment, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialog_id)), new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda51
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$openWebViewMenu$23(runnable);
                }
            }, new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda43
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$openWebViewMenu$24();
                }
            });
        }
    }

    public /* synthetic */ void lambda$openWebViewMenu$22() {
        AndroidUtilities.hideKeyboard(this);
        if (AndroidUtilities.isTablet()) {
            BotWebViewSheet botWebViewSheet = new BotWebViewSheet(getContext(), this.parentFragment.getResourceProvider());
            botWebViewSheet.setParentActivity(this.parentActivity);
            int i = this.currentAccount;
            long j = this.dialog_id;
            botWebViewSheet.requestWebView(i, j, j, this.botMenuWebViewTitle, this.botMenuWebViewUrl, 2, 0, false);
            BaseFragment lastFragment = LaunchActivity.getLastFragment();
            if (lastFragment != null) {
                lastFragment.showDialog(botWebViewSheet);
            } else {
                botWebViewSheet.show();
            }
            BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
            if (botCommandsMenuView != null) {
                botCommandsMenuView.setOpened(false);
                return;
            }
            return;
        }
        this.botWebViewMenuContainer.show(this.currentAccount, this.dialog_id, this.botMenuWebViewUrl);
    }

    public /* synthetic */ void lambda$openWebViewMenu$23(Runnable runnable) {
        runnable.run();
        SharedPrefsHelper.setWebViewConfirmShown(this.currentAccount, this.dialog_id, true);
    }

    public /* synthetic */ void lambda$openWebViewMenu$24() {
        if (this.botCommandsMenuButton == null || SharedPrefsHelper.isWebViewConfirmShown(this.currentAccount, this.dialog_id)) {
            return;
        }
        this.botCommandsMenuButton.setOpened(false);
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

    public void setComposeShadowAlpha(float f) {
        this.composeShadowAlpha = f;
        invalidate();
    }

    public ChatActivityBotWebViewButton getBotWebViewButton() {
        createBotWebViewButton();
        return this.botWebViewButton;
    }

    public ChatActivity getParentFragment() {
        return this.parentFragment;
    }

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

    public void forceSmoothKeyboard(boolean z) {
        ChatActivity chatActivity;
        this.smoothKeyboard = z && !AndroidUtilities.isInMultiwindow && ((chatActivity = this.parentFragment) == null || !chatActivity.isInBubbleMode());
    }

    public void startLockTransition() {
        AnimatorSet animatorSet = new AnimatorSet();
        performHapticFeedback(3, 2);
        RecordCircle recordCircle = this.recordCircle;
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(recordCircle, "lockAnimatedTranslation", recordCircle.startTranslation);
        ofFloat.setStartDelay(100L);
        ofFloat.setDuration(350L);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.recordCircle, "snapAnimationProgress", 1.0f);
        ofFloat2.setInterpolator(CubicBezierInterpolator.EASE_OUT_QUINT);
        ofFloat2.setDuration(250L);
        SharedConfig.removeLockRecordAudioVideoHint();
        animatorSet.playTogether(ofFloat2, ofFloat, ObjectAnimator.ofFloat(this.recordCircle, "slideToCancelProgress", 1.0f).setDuration(200L), ObjectAnimator.ofFloat(this.slideText, "cancelToProgress", 1.0f));
        animatorSet.start();
    }

    public int getBackgroundTop() {
        int top = getTop();
        View view = this.topView;
        return (view == null || view.getVisibility() != 0) ? top : top + this.topView.getLayoutParams().height;
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

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.shouldDrawBackground) {
            int intrinsicHeight = (int) (this.animatedTop + (Theme.chat_composeShadowDrawable.getIntrinsicHeight() * (1.0f - this.composeShadowAlpha)));
            View view = this.topView;
            if (view != null && view.getVisibility() == 0) {
                intrinsicHeight = (int) (intrinsicHeight + ((1.0f - this.topViewEnterProgress) * this.topView.getLayoutParams().height));
            }
            int intrinsicHeight2 = Theme.chat_composeShadowDrawable.getIntrinsicHeight() + intrinsicHeight;
            Theme.chat_composeShadowDrawable.setAlpha((int) (this.composeShadowAlpha * 255.0f));
            Theme.chat_composeShadowDrawable.setBounds(0, intrinsicHeight, getMeasuredWidth(), intrinsicHeight2);
            Theme.chat_composeShadowDrawable.draw(canvas);
            int i = (int) (intrinsicHeight2 + this.chatSearchExpandOffset);
            if (this.allowBlur) {
                this.backgroundPaint.setColor(getThemedColor(Theme.key_chat_messagePanelBackground));
                if (SharedConfig.chatBlurEnabled() && this.sizeNotifierLayout != null) {
                    android.graphics.Rect rect = AndroidUtilities.rectTmp2;
                    rect.set(0, i, getWidth(), getHeight());
                    this.sizeNotifierLayout.drawBlurRect(canvas, getTop(), rect, this.backgroundPaint, false);
                    return;
                }
                canvas.drawRect(0.0f, i, getWidth(), getHeight(), this.backgroundPaint);
                return;
            }
            canvas.drawRect(0.0f, i, getWidth(), getHeight(), getThemedPaint("paintChatComposeBackground"));
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(13:5|(1:73)(1:9)|10|(8:12|(1:40)(1:16)|(1:39)(1:22)|23|(4:25|(1:27)(1:33)|28|(1:32))|(1:35)|36|(1:38))|41|(3:43|(2:47|(1:53))|54)|55|(4:57|(1:71)(1:61)|62|(5:64|65|66|67|68))|72|65|66|67|68) */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onSendLongClick(View view) {
        int measuredHeight;
        if (isInScheduleMode()) {
            return false;
        }
        ChatActivity chatActivity = this.parentFragment;
        boolean z = chatActivity != null && UserObject.isUserSelf(chatActivity.getCurrentUser());
        if (this.sendPopupLayout == null) {
            ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = new ActionBarPopupWindow.ActionBarPopupWindowLayout(this.parentActivity, this.resourcesProvider);
            this.sendPopupLayout = actionBarPopupWindowLayout;
            actionBarPopupWindowLayout.setAnimationEnabled(false);
            this.sendPopupLayout.setOnTouchListener(new View.OnTouchListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.31
                private android.graphics.Rect popupRect = new android.graphics.Rect();

                {
                    ChatActivityEnterView.this = this;
                }

                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view2, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() == 0 && ChatActivityEnterView.this.sendPopupWindow != null && ChatActivityEnterView.this.sendPopupWindow.isShowing()) {
                        view2.getHitRect(this.popupRect);
                        if (this.popupRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                            return false;
                        }
                        ChatActivityEnterView.this.sendPopupWindow.dismiss();
                        return false;
                    }
                    return false;
                }
            });
            this.sendPopupLayout.setDispatchKeyEventListener(new ActionBarPopupWindow.OnDispatchKeyEventListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda56
                @Override // org.telegram.ui.ActionBar.ActionBarPopupWindow.OnDispatchKeyEventListener
                public final void onDispatchKeyEvent(KeyEvent keyEvent) {
                    ChatActivityEnterView.this.lambda$onSendLongClick$25(keyEvent);
                }
            });
            this.sendPopupLayout.setShownFromBottom(false);
            ChatActivity chatActivity2 = this.parentFragment;
            boolean z2 = chatActivity2 != null && chatActivity2.canScheduleMessage();
            boolean z3 = !z && (this.slowModeTimer <= 0 || isInScheduleMode());
            if (z2) {
                ActionBarMenuSubItem actionBarMenuSubItem = new ActionBarMenuSubItem(getContext(), true, !z3, this.resourcesProvider);
                if (z) {
                    actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("SetReminder", R.string.SetReminder), R.drawable.msg_calendar2);
                } else {
                    actionBarMenuSubItem.setTextAndIcon(LocaleController.getString("ScheduleMessage", R.string.ScheduleMessage), R.drawable.msg_calendar2);
                }
                actionBarMenuSubItem.setMinimumWidth(AndroidUtilities.dp(196.0f));
                actionBarMenuSubItem.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda25
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatActivityEnterView.this.lambda$onSendLongClick$26(view2);
                    }
                });
                this.sendPopupLayout.addView((View) actionBarMenuSubItem, LayoutHelper.createLinear(-1, 48));
                SharedConfig.removeScheduledHint();
                if (!z && this.dialog_id > 0) {
                    ActionBarMenuSubItem actionBarMenuSubItem2 = new ActionBarMenuSubItem(getContext(), true, !z3, this.resourcesProvider);
                    this.sendWhenOnlineButton = actionBarMenuSubItem2;
                    actionBarMenuSubItem2.setTextAndIcon(LocaleController.getString("SendWhenOnline", R.string.SendWhenOnline), R.drawable.msg_online);
                    this.sendWhenOnlineButton.setMinimumWidth(AndroidUtilities.dp(196.0f));
                    this.sendWhenOnlineButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda9
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            ChatActivityEnterView.this.lambda$onSendLongClick$27(view2);
                        }
                    });
                    this.sendPopupLayout.addView((View) this.sendWhenOnlineButton, LayoutHelper.createLinear(-1, 48));
                }
            }
            if (z3) {
                ActionBarMenuSubItem actionBarMenuSubItem3 = new ActionBarMenuSubItem(getContext(), !z2, true, this.resourcesProvider);
                actionBarMenuSubItem3.setTextAndIcon(LocaleController.getString("SendWithoutSound", R.string.SendWithoutSound), R.drawable.input_notify_off);
                actionBarMenuSubItem3.setMinimumWidth(AndroidUtilities.dp(196.0f));
                actionBarMenuSubItem3.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda20
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ChatActivityEnterView.this.lambda$onSendLongClick$28(view2);
                    }
                });
                this.sendPopupLayout.addView((View) actionBarMenuSubItem3, LayoutHelper.createLinear(-1, 48));
            }
            this.sendPopupLayout.setupRadialSelectors(getThemedColor(Theme.key_dialogButtonSelector));
            ActionBarPopupWindow actionBarPopupWindow = new ActionBarPopupWindow(this.sendPopupLayout, -2, -2) { // from class: org.telegram.ui.Components.ChatActivityEnterView.33
                {
                    ChatActivityEnterView.this = this;
                }

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
        if (this.sendWhenOnlineButton != null) {
            TLRPC$User currentUser = this.parentFragment.getCurrentUser();
            if (currentUser != null && !currentUser.bot) {
                TLRPC$UserStatus tLRPC$UserStatus = currentUser.status;
                if (!(tLRPC$UserStatus instanceof TLRPC$TL_userStatusEmpty) && !(tLRPC$UserStatus instanceof TLRPC$TL_userStatusOnline) && !(tLRPC$UserStatus instanceof TLRPC$TL_userStatusRecently)) {
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
                return false;
            }
        }
        measuredHeight = (this.location[1] - this.sendPopupLayout.getMeasuredHeight()) - AndroidUtilities.dp(2.0f);
        this.sendPopupWindow.showAtLocation(view, 51, ((this.location[0] + view.getMeasuredWidth()) - this.sendPopupLayout.getMeasuredWidth()) + AndroidUtilities.dp(8.0f), measuredHeight);
        this.sendPopupWindow.dimBehind();
        this.sendButton.invalidate();
        view.performHapticFeedback(3, 2);
        return false;
    }

    public /* synthetic */ void lambda$onSendLongClick$25(KeyEvent keyEvent) {
        ActionBarPopupWindow actionBarPopupWindow;
        if (keyEvent.getKeyCode() == 4 && keyEvent.getRepeatCount() == 0 && (actionBarPopupWindow = this.sendPopupWindow) != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
    }

    public /* synthetic */ void lambda$onSendLongClick$26(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView.32
            {
                ChatActivityEnterView.this = this;
            }

            @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
            public void didSelectDate(boolean z, int i) {
                ChatActivityEnterView.this.sendMessageInternal(z, i, true);
            }
        }, this.resourcesProvider);
    }

    public /* synthetic */ void lambda$onSendLongClick$27(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        sendMessageInternal(true, 2147483646, true);
    }

    public /* synthetic */ void lambda$onSendLongClick$28(View view) {
        ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
        if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
            this.sendPopupWindow.dismiss();
        }
        sendMessageInternal(false, 0, true);
    }

    private void createBotCommandsMenuContainer() {
        if (this.botCommandsMenuContainer != null) {
            return;
        }
        BotCommandsMenuContainer botCommandsMenuContainer = new BotCommandsMenuContainer(getContext()) { // from class: org.telegram.ui.Components.ChatActivityEnterView.34
            {
                ChatActivityEnterView.this = this;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.telegram.ui.Components.BotCommandsMenuContainer
            public void onDismiss() {
                super.onDismiss();
                if (ChatActivityEnterView.this.botCommandsMenuButton != null) {
                    ChatActivityEnterView.this.botCommandsMenuButton.setOpened(false);
                }
            }
        };
        this.botCommandsMenuContainer = botCommandsMenuContainer;
        botCommandsMenuContainer.listView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerListView recyclerListView = this.botCommandsMenuContainer.listView;
        BotCommandsMenuView.BotCommandsAdapter botCommandsAdapter = new BotCommandsMenuView.BotCommandsAdapter();
        this.botCommandsAdapter = botCommandsAdapter;
        recyclerListView.setAdapter(botCommandsAdapter);
        this.botCommandsMenuContainer.listView.setOnItemClickListener(new 35());
        this.botCommandsMenuContainer.listView.setOnItemLongClickListener(new RecyclerListView.OnItemLongClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.36
            {
                ChatActivityEnterView.this = this;
            }

            @Override // org.telegram.ui.Components.RecyclerListView.OnItemLongClickListener
            public boolean onItemClick(View view, int i) {
                if (view instanceof BotCommandsMenuView.BotCommandView) {
                    String command = ((BotCommandsMenuView.BotCommandView) view).getCommand();
                    ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                    chatActivityEnterView.setFieldText(command + " ");
                    ChatActivityEnterView.this.botCommandsMenuContainer.dismiss();
                    return true;
                }
                return false;
            }
        });
        this.botCommandsMenuContainer.setClipToPadding(false);
        this.sizeNotifierLayout.addView(this.botCommandsMenuContainer, 14, LayoutHelper.createFrame(-1, -1, 80));
        this.botCommandsMenuContainer.setVisibility(8);
        LongSparseArray<TLRPC$BotInfo> longSparseArray = this.lastBotInfo;
        if (longSparseArray != null) {
            this.botCommandsAdapter.setBotInfo(longSparseArray);
        }
        updateBotCommandsMenuContainerTopPadding();
    }

    /* loaded from: classes4.dex */
    public class 35 implements RecyclerListView.OnItemClickListener {
        35() {
            ChatActivityEnterView.this = r1;
        }

        @Override // org.telegram.ui.Components.RecyclerListView.OnItemClickListener
        public void onItemClick(View view, int i) {
            if (view instanceof BotCommandsMenuView.BotCommandView) {
                final String command = ((BotCommandsMenuView.BotCommandView) view).getCommand();
                if (TextUtils.isEmpty(command)) {
                    return;
                }
                if (!ChatActivityEnterView.this.isInScheduleMode()) {
                    if (ChatActivityEnterView.this.parentFragment == null || !ChatActivityEnterView.this.parentFragment.checkSlowMode(view)) {
                        SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(command, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, false, null, null, null, true, 0, null, false));
                        ChatActivityEnterView.this.setFieldText("");
                        ChatActivityEnterView.this.botCommandsMenuContainer.dismiss();
                        return;
                    }
                    return;
                }
                AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.dialog_id, new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$35$$ExternalSyntheticLambda0
                    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                    public final void didSelectDate(boolean z, int i2) {
                        ChatActivityEnterView.35.this.lambda$onItemClick$0(command, z, i2);
                    }
                }, ChatActivityEnterView.this.resourcesProvider);
            }
        }

        public /* synthetic */ void lambda$onItemClick$0(String str, boolean z, int i) {
            SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(str, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, false, null, null, null, z, i, null, false));
            ChatActivityEnterView.this.setFieldText("");
            ChatActivityEnterView.this.botCommandsMenuContainer.dismiss();
        }
    }

    private void updateBotCommandsMenuContainerTopPadding() {
        int max;
        LinearLayoutManager linearLayoutManager;
        int findFirstVisibleItemPosition;
        View findViewByPosition;
        if (this.botCommandsMenuContainer == null) {
            return;
        }
        if (this.botCommandsAdapter.getItemCount() > 4) {
            max = Math.max(0, this.sizeNotifierLayout.getMeasuredHeight() - AndroidUtilities.dp(162.8f));
        } else {
            max = Math.max(0, this.sizeNotifierLayout.getMeasuredHeight() - AndroidUtilities.dp((Math.max(1, Math.min(4, this.botCommandsAdapter.getItemCount())) * 36) + 8));
        }
        if (this.botCommandsMenuContainer.listView.getPaddingTop() != max) {
            this.botCommandsMenuContainer.listView.setTopGlowOffset(max);
            if (this.botCommandLastPosition == -1 && this.botCommandsMenuContainer.getVisibility() == 0 && this.botCommandsMenuContainer.listView.getLayoutManager() != null && (findFirstVisibleItemPosition = (linearLayoutManager = (LinearLayoutManager) this.botCommandsMenuContainer.listView.getLayoutManager()).findFirstVisibleItemPosition()) >= 0 && (findViewByPosition = linearLayoutManager.findViewByPosition(findFirstVisibleItemPosition)) != null) {
                this.botCommandLastPosition = findFirstVisibleItemPosition;
                this.botCommandLastTop = findViewByPosition.getTop() - this.botCommandsMenuContainer.listView.getPaddingTop();
            }
            this.botCommandsMenuContainer.listView.setPadding(0, max, 0, AndroidUtilities.dp(8.0f));
        }
    }

    private void createBotWebViewMenuContainer() {
        if (this.botWebViewMenuContainer != null) {
            return;
        }
        BotWebViewMenuContainer botWebViewMenuContainer = new BotWebViewMenuContainer(getContext(), this) { // from class: org.telegram.ui.Components.ChatActivityEnterView.37
            {
                ChatActivityEnterView.this = this;
            }

            @Override // org.telegram.ui.Components.BotWebViewMenuContainer
            public void onDismiss() {
                super.onDismiss();
                if (ChatActivityEnterView.this.botCommandsMenuButton != null) {
                    ChatActivityEnterView.this.botCommandsMenuButton.setOpened(false);
                }
            }
        };
        this.botWebViewMenuContainer = botWebViewMenuContainer;
        this.sizeNotifierLayout.addView(botWebViewMenuContainer, 15, LayoutHelper.createFrame(-1, -1, 80));
        this.botWebViewMenuContainer.setVisibility(8);
        this.botWebViewMenuContainer.setOnDismissGlobalListener(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda47
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$createBotWebViewMenuContainer$29();
            }
        });
    }

    public /* synthetic */ void lambda$createBotWebViewMenuContainer$29() {
        if (this.botButtonsMessageObject != null) {
            EditTextCaption editTextCaption = this.messageEditText;
            if ((editTextCaption == null || TextUtils.isEmpty(editTextCaption.getText())) && !this.botWebViewMenuContainer.hasSavedText()) {
                showPopup(1, 1);
            }
        }
    }

    /* loaded from: classes4.dex */
    public class ChatActivityEditTextCaption extends EditTextCaption {
        CanvasButton canvasButton;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ChatActivityEditTextCaption(Context context, Theme.ResourcesProvider resourcesProvider) {
            super(context, resourcesProvider);
            ChatActivityEnterView.this = r1;
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public void onScrollChanged(int i, int i2, int i3, int i4) {
            super.onScrollChanged(i, i2, i3, i4);
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onEditTextScroll();
            }
        }

        @Override // org.telegram.ui.Components.EditTextCaption
        protected void onContextMenuOpen() {
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onContextMenuOpen();
            }
        }

        @Override // org.telegram.ui.Components.EditTextCaption
        protected void onContextMenuClose() {
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onContextMenuClose();
            }
        }

        /* renamed from: send */
        public void lambda$onCreateInputConnection$0(InputContentInfoCompat inputContentInfoCompat, boolean z, int i) {
            if (ChatActivityEnterView.this.replyingQuote != null && ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.replyingQuote.outdated) {
                ChatActivityEnterView.this.parentFragment.showQuoteMessageUpdate();
                return;
            }
            if (inputContentInfoCompat.getDescription().hasMimeType("image/gif")) {
                SendMessagesHelper.prepareSendingDocument(ChatActivityEnterView.this.accountInstance, null, null, inputContentInfoCompat.getContentUri(), null, "image/gif", ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, ChatActivityEnterView.this.replyingQuote, null, z, 0, inputContentInfoCompat);
            } else {
                SendMessagesHelper.prepareSendingPhoto(ChatActivityEnterView.this.accountInstance, null, inputContentInfoCompat.getContentUri(), ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), ChatActivityEnterView.this.replyingQuote, null, null, null, inputContentInfoCompat, 0, null, z, 0);
            }
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onMessageSend(null, true, i);
            }
        }

        @Override // android.widget.TextView, android.view.View
        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
            if (onCreateInputConnection == null) {
                return null;
            }
            try {
                EditorInfoCompat.setContentMimeTypes(editorInfo, new String[]{"image/gif", "image/*", "image/jpg", "image/png", "image/webp"});
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

        public /* synthetic */ boolean lambda$onCreateInputConnection$1(final InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle) {
            if (BuildCompat.isAtLeastNMR1() && (i & 1) != 0) {
                try {
                    inputContentInfoCompat.requestPermission();
                } catch (Exception unused) {
                    return false;
                }
            }
            if (inputContentInfoCompat.getDescription().hasMimeType("image/gif") || SendMessagesHelper.shouldSendWebPAsSticker(null, inputContentInfoCompat.getContentUri())) {
                if (ChatActivityEnterView.this.isInScheduleMode()) {
                    AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEditTextCaption$$ExternalSyntheticLambda5
                        @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                        public final void didSelectDate(boolean z, int i2) {
                            ChatActivityEnterView.ChatActivityEditTextCaption.this.lambda$onCreateInputConnection$0(inputContentInfoCompat, z, i2);
                        }
                    }, ChatActivityEnterView.this.resourcesProvider);
                } else {
                    lambda$onCreateInputConnection$0(inputContentInfoCompat, true, 0);
                }
            } else {
                editPhoto(inputContentInfoCompat.getContentUri(), inputContentInfoCompat.getDescription().getMimeType(0));
            }
            return true;
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (!ChatActivityEnterView.this.stickersDragging && ChatActivityEnterView.this.stickersExpansionAnim == null) {
                if (!ChatActivityEnterView.this.sendPlainEnabled && !ChatActivityEnterView.this.isEditingMessage()) {
                    if (this.canvasButton == null) {
                        CanvasButton canvasButton = new CanvasButton(this);
                        this.canvasButton = canvasButton;
                        canvasButton.setDelegate(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEditTextCaption$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.ChatActivityEditTextCaption.this.lambda$onTouchEvent$2();
                            }
                        });
                    }
                    this.canvasButton.setRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                    return this.canvasButton.checkTouchEvent(motionEvent);
                } else if (ChatActivityEnterView.this.isPopupShowing() && motionEvent.getAction() == 0) {
                    if (ChatActivityEnterView.this.searchingType != 0) {
                        ChatActivityEnterView.this.setSearchingTypeInternal(0, false);
                        ChatActivityEnterView.this.emojiView.closeSearch(false);
                        requestFocus();
                    }
                    ChatActivityEnterView.this.showPopup(AndroidUtilities.usingHardwareInput ? 0 : 2, 0);
                    if (ChatActivityEnterView.this.stickersExpanded) {
                        ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                        ChatActivityEnterView.this.waitingForKeyboardOpenAfterAnimation = true;
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEditTextCaption$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.ChatActivityEditTextCaption.this.lambda$onTouchEvent$3();
                            }
                        }, 200L);
                    } else {
                        ChatActivityEnterView.this.openKeyboardInternal();
                    }
                    return true;
                } else {
                    try {
                        return super.onTouchEvent(motionEvent);
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            }
            return false;
        }

        public /* synthetic */ void lambda$onTouchEvent$2() {
            ChatActivityEnterView.this.showRestrictedHint();
        }

        public /* synthetic */ void lambda$onTouchEvent$3() {
            ChatActivityEnterView.this.waitingForKeyboardOpenAfterAnimation = false;
            ChatActivityEnterView.this.openKeyboardInternal();
        }

        @Override // android.view.View
        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            if (ChatActivityEnterView.this.preventInput) {
                return false;
            }
            return super.dispatchKeyEvent(keyEvent);
        }

        @Override // org.telegram.ui.Components.EditTextEffects, android.widget.TextView
        public void onSelectionChanged(int i, int i2) {
            super.onSelectionChanged(i, i2);
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onTextSelectionChanged(i, i2);
            }
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor
        public void extendActionMode(ActionMode actionMode, Menu menu) {
            if (ChatActivityEnterView.this.parentFragment != null) {
                ChatActivityEnterView.this.parentFragment.extendActionMode(menu);
            } else {
                ChatActivityEnterView.this.extendActionMode(menu);
            }
        }

        @Override // android.view.View
        public boolean requestRectangleOnScreen(android.graphics.Rect rect) {
            rect.bottom += AndroidUtilities.dp(1000.0f);
            return super.requestRectangleOnScreen(rect);
        }

        @Override // org.telegram.ui.Components.EditTextCaption, org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
        public void onMeasure(int i, int i2) {
            ChatActivityEnterView.this.isInitLineCount = getMeasuredWidth() == 0 && getMeasuredHeight() == 0;
            super.onMeasure(i, i2);
            if (ChatActivityEnterView.this.isInitLineCount) {
                ChatActivityEnterView.this.lineCount = getLineCount();
            }
            ChatActivityEnterView.this.isInitLineCount = false;
        }

        @Override // org.telegram.ui.Components.EditTextCaption, android.widget.TextView
        public boolean onTextContextMenuItem(int i) {
            if (i == 16908322) {
                ChatActivityEnterView.this.isPaste = true;
                ClipData primaryClip = ((ClipboardManager) getContext().getSystemService("clipboard")).getPrimaryClip();
                if (primaryClip != null && primaryClip.getItemCount() == 1 && primaryClip.getDescription().hasMimeType("image/*")) {
                    editPhoto(primaryClip.getItemAt(0).getUri(), primaryClip.getDescription().getMimeType(0));
                }
            }
            return super.onTextContextMenuItem(i);
        }

        private void editPhoto(final Uri uri, String str) {
            final File generatePicturePath = AndroidUtilities.generatePicturePath(ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.parentFragment.isSecretChat(), MimeTypeMap.getSingleton().getExtensionFromMimeType(str));
            Utilities.globalQueue.postRunnable(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEditTextCaption$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.ChatActivityEditTextCaption.this.lambda$editPhoto$5(uri, generatePicturePath);
                }
            });
        }

        public /* synthetic */ void lambda$editPhoto$5(Uri uri, final File file) {
            try {
                InputStream openInputStream = getContext().getContentResolver().openInputStream(uri);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = openInputStream.read(bArr);
                    if (read > 0) {
                        fileOutputStream.write(bArr, 0, read);
                        fileOutputStream.flush();
                    } else {
                        openInputStream.close();
                        fileOutputStream.close();
                        MediaController.PhotoEntry photoEntry = new MediaController.PhotoEntry(0, -1, 0L, file.getAbsolutePath(), 0, false, 0, 0, 0L);
                        final ArrayList arrayList = new ArrayList();
                        arrayList.add(photoEntry);
                        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$ChatActivityEditTextCaption$$ExternalSyntheticLambda4
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.ChatActivityEditTextCaption.this.lambda$editPhoto$4(arrayList, file);
                            }
                        });
                        return;
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        /* renamed from: openPhotoViewerForEdit */
        public void lambda$editPhoto$4(final ArrayList<Object> arrayList, final File file) {
            if (ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentFragment.getParentActivity() == null) {
                return;
            }
            final MediaController.PhotoEntry photoEntry = (MediaController.PhotoEntry) arrayList.get(0);
            if (!ChatActivityEnterView.this.keyboardVisible) {
                PhotoViewer.getInstance().setParentActivity(ChatActivityEnterView.this.parentFragment, ChatActivityEnterView.this.resourcesProvider);
                PhotoViewer.getInstance().openPhotoForSelect(arrayList, 0, 2, false, new PhotoViewer.EmptyPhotoViewerProvider() { // from class: org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEditTextCaption.2
                    boolean sending;

                    @Override // org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider, org.telegram.ui.PhotoViewer.PhotoViewerProvider
                    public boolean canCaptureMorePhotos() {
                        return false;
                    }

                    {
                        ChatActivityEditTextCaption.this = this;
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
                        if (!z3 && (str = photoEntry2.imagePath) != null) {
                            sendingMediaInfo.path = str;
                        } else {
                            String str2 = photoEntry2.path;
                            if (str2 != null) {
                                sendingMediaInfo.path = str2;
                            }
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
                        SendMessagesHelper.prepareSendingMedia(ChatActivityEnterView.this.accountInstance, arrayList2, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, ChatActivityEnterView.this.replyingQuote, false, false, ChatActivityEnterView.this.editingMessageObject, z, i2, SendMessagesHelper.checkUpdateStickersOrder(sendingMediaInfo.caption), null);
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
                return;
            }
            AndroidUtilities.hideKeyboard(this);
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEditTextCaption.1
                {
                    ChatActivityEditTextCaption.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    ChatActivityEditTextCaption.this.lambda$editPhoto$4(arrayList, file);
                }
            }, 100L);
        }

        @Override // org.telegram.ui.Components.EditTextBoldCursor
        protected Theme.ResourcesProvider getResourcesProvider() {
            return ChatActivityEnterView.this.resourcesProvider;
        }

        @Override // android.view.View
        public boolean requestFocus(int i, android.graphics.Rect rect) {
            if (ChatActivityEnterView.this.sendPlainEnabled || ChatActivityEnterView.this.isEditingMessage()) {
                return super.requestFocus(i, rect);
            }
            return false;
        }

        @Override // org.telegram.ui.Components.EditTextEffects
        public void setOffsetY(float f) {
            super.setOffsetY(f);
            if (ChatActivityEnterView.this.sizeNotifierLayout.getForeground() != null) {
                ChatActivityEnterView.this.sizeNotifierLayout.invalidateDrawable(ChatActivityEnterView.this.sizeNotifierLayout.getForeground());
            }
        }
    }

    private boolean isKeyboardSupportIncognitoMode() {
        String string = Settings.Secure.getString(getContext().getContentResolver(), "default_input_method");
        return string == null || !string.startsWith("com.samsung");
    }

    private void createMessageEditText() {
        if (this.messageEditText != null) {
            return;
        }
        ChatActivityEditTextCaption chatActivityEditTextCaption = new ChatActivityEditTextCaption(getContext(), this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatActivityEnterView.38
            boolean clickMaybe;
            float touchX;
            float touchY;

            {
                ChatActivityEnterView.this = this;
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEditTextCaption, org.telegram.ui.Components.EditTextBoldCursor, android.widget.TextView, android.view.View
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (ChatActivityEnterView.this.botCommandsMenuIsShowing()) {
                    if (motionEvent.getAction() == 0) {
                        this.touchX = motionEvent.getX();
                        this.touchY = motionEvent.getY();
                        this.clickMaybe = true;
                    } else if (this.clickMaybe && motionEvent.getAction() == 2) {
                        if (Math.abs(motionEvent.getX() - this.touchX) > AndroidUtilities.touchSlop || Math.abs(motionEvent.getY() - this.touchY) > AndroidUtilities.touchSlop) {
                            this.clickMaybe = false;
                        }
                    } else if (this.clickMaybe) {
                        if (ChatActivityEnterView.this.delegate != null) {
                            ChatActivityEnterView.this.delegate.onKeyboardRequested();
                        }
                        EditTextCaption editTextCaption = ChatActivityEnterView.this.messageEditText;
                        if (editTextCaption != null && !AndroidUtilities.showKeyboard(editTextCaption)) {
                            ChatActivityEnterView.this.messageEditText.clearFocus();
                            ChatActivityEnterView.this.messageEditText.requestFocus();
                        }
                    }
                    return this.clickMaybe;
                }
                if (motionEvent.getAction() == 0 && ChatActivityEnterView.this.delegate != null) {
                    ChatActivityEnterView.this.delegate.onKeyboardRequested();
                }
                return super.onTouchEvent(motionEvent);
            }

            @Override // org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEditTextCaption, org.telegram.ui.Components.EditTextEffects
            public void setOffsetY(float f) {
                super.setOffsetY(f);
                ChatActivityEnterView.this.messageEditTextContainer.invalidate();
            }

            @Override // org.telegram.ui.Components.EditTextBoldCursor, org.telegram.ui.Components.EditTextEffects, android.widget.TextView, android.view.View
            public void onAttachedToWindow() {
                super.onAttachedToWindow();
                if (ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentFragment.getParentLayout() == null || !ChatActivityEnterView.this.parentFragment.getParentLayout().isSheet()) {
                    setWindowView(ChatActivityEnterView.this.parentActivity.getWindow().getDecorView());
                } else {
                    setWindowView(ChatActivityEnterView.this.parentFragment.getParentLayout().getWindow().getDecorView());
                }
            }
        };
        this.messageEditText = chatActivityEditTextCaption;
        int i = Build.VERSION.SDK_INT;
        if (i >= 28) {
            chatActivityEditTextCaption.setFallbackLineSpacing(false);
        }
        EditTextCaption editTextCaption = this.messageEditText;
        boolean z = true;
        editTextCaption.wrapCanvasToFixClipping = i > 20;
        editTextCaption.setDelegate(new EditTextCaption.EditTextCaptionDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda59
            @Override // org.telegram.ui.Components.EditTextCaption.EditTextCaptionDelegate
            public final void onSpansChanged() {
                ChatActivityEnterView.this.lambda$createMessageEditText$30();
            }
        });
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null && chatActivity.getParentLayout() != null && this.parentFragment.getParentLayout().isSheet()) {
            this.messageEditText.setWindowView(this.parentFragment.getParentLayout().getWindow().getDecorView());
        } else {
            this.messageEditText.setWindowView(this.parentActivity.getWindow().getDecorView());
        }
        ChatActivity chatActivity2 = this.parentFragment;
        TLRPC$EncryptedChat currentEncryptedChat = chatActivity2 != null ? chatActivity2.getCurrentEncryptedChat() : null;
        this.messageEditText.setAllowTextEntitiesIntersection(supportsSendingNewEntities());
        int i2 = 268435456;
        if (isKeyboardSupportIncognitoMode() && currentEncryptedChat != null) {
            i2 = 285212672;
        }
        this.messageEditText.setIncludeFontPadding(false);
        this.messageEditText.setImeOptions(i2);
        EditTextCaption editTextCaption2 = this.messageEditText;
        int inputType = editTextCaption2.getInputType() | LiteMode.FLAG_ANIMATED_EMOJI_KEYBOARD_NOT_PREMIUM | 131072;
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
        int i3 = Theme.key_chat_messagePanelHint;
        editTextCaption3.setHintColor(getThemedColor(i3));
        this.messageEditText.setHintTextColor(getThemedColor(i3));
        this.messageEditText.setCursorColor(getThemedColor(Theme.key_chat_messagePanelCursor));
        this.messageEditText.setHandlesColor(getThemedColor(Theme.key_chat_TextSelectionCursor));
        this.messageEditTextContainer.addView(this.messageEditText, 1, LayoutHelper.createFrame(-1, -2.0f, 80, 52.0f, 0.0f, this.isChat ? 50.0f : 2.0f, 1.5f));
        this.messageEditText.setOnKeyListener(new View.OnKeyListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.39
            boolean ctrlPressed = false;

            {
                ChatActivityEnterView.this = this;
            }

            @Override // android.view.View.OnKeyListener
            public boolean onKey(View view, int i4, KeyEvent keyEvent) {
                if (i4 == 4 && !ChatActivityEnterView.this.keyboardVisible && ChatActivityEnterView.this.isPopupShowing() && keyEvent.getAction() == 1) {
                    if (!ContentPreviewViewer.hasInstance() || !ContentPreviewViewer.getInstance().isVisible()) {
                        if (ChatActivityEnterView.this.currentPopupContentType != 1 || ChatActivityEnterView.this.botButtonsMessageObject == null) {
                            if (keyEvent.getAction() == 1) {
                                if (ChatActivityEnterView.this.currentPopupContentType == 1 && ChatActivityEnterView.this.botButtonsMessageObject != null) {
                                    SharedPreferences.Editor edit = MessagesController.getMainSettings(ChatActivityEnterView.this.currentAccount).edit();
                                    edit.putInt("hidekeyboard_" + ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.botButtonsMessageObject.getId()).commit();
                                }
                                if (ChatActivityEnterView.this.searchingType != 0) {
                                    ChatActivityEnterView.this.setSearchingTypeInternal(0, true);
                                    if (ChatActivityEnterView.this.emojiView != null) {
                                        ChatActivityEnterView.this.emojiView.closeSearch(true);
                                    }
                                    ChatActivityEnterView.this.messageEditText.requestFocus();
                                } else if (!ChatActivityEnterView.this.stickersExpanded) {
                                    if (ChatActivityEnterView.this.stickersExpansionAnim == null) {
                                        if (ChatActivityEnterView.this.botButtonsMessageObject == null || ChatActivityEnterView.this.currentPopupContentType == 1 || !TextUtils.isEmpty(ChatActivityEnterView.this.messageEditText.getText())) {
                                            ChatActivityEnterView.this.showPopup(0, 0);
                                        } else {
                                            ChatActivityEnterView.this.showPopup(1, 1);
                                        }
                                    }
                                } else {
                                    ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                    ContentPreviewViewer.getInstance().closeWithMenu();
                    return true;
                } else if (i4 == 66 && ((this.ctrlPressed || ChatActivityEnterView.this.sendByEnter) && keyEvent.getAction() == 0 && ChatActivityEnterView.this.editingMessageObject == null)) {
                    ChatActivityEnterView.this.sendMessage();
                    return true;
                } else if (i4 == 113 || i4 == 114) {
                    this.ctrlPressed = keyEvent.getAction() == 0;
                    return true;
                } else {
                    return false;
                }
            }
        });
        this.messageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.40
            boolean ctrlPressed = false;

            {
                ChatActivityEnterView.this = this;
            }

            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView textView, int i4, KeyEvent keyEvent) {
                if (i4 == 4) {
                    ChatActivityEnterView.this.sendMessage();
                    return true;
                } else if (keyEvent == null || i4 != 0) {
                    return false;
                } else {
                    if ((this.ctrlPressed || ChatActivityEnterView.this.sendByEnter) && keyEvent.getAction() == 0 && ChatActivityEnterView.this.editingMessageObject == null) {
                        ChatActivityEnterView.this.sendMessage();
                        return true;
                    }
                    return false;
                }
            }
        });
        this.messageEditText.addTextChangedListener(new 41());
        this.messageEditText.setEnabled(this.messageEditTextEnabled);
        ArrayList<TextWatcher> arrayList = this.messageEditTextWatchers;
        if (arrayList != null) {
            Iterator<TextWatcher> it = arrayList.iterator();
            while (it.hasNext()) {
                this.messageEditText.addTextChangedListener(it.next());
            }
            this.messageEditTextWatchers.clear();
        }
        updateFieldHint(false);
        ChatActivity chatActivity3 = this.parentFragment;
        updateSendAsButton((chatActivity3 == null || !chatActivity3.getFragmentBeginToShow()) ? false : false);
        ChatActivity chatActivity4 = this.parentFragment;
        if (chatActivity4 != null) {
            chatActivity4.applyDraftMaybe(false);
        }
    }

    public /* synthetic */ void lambda$createMessageEditText$30() {
        this.messageEditText.invalidateEffects();
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onTextSpansChanged(this.messageEditText.getText());
        }
    }

    /* loaded from: classes4.dex */
    public class 41 implements TextWatcher {
        boolean heightShouldBeChanged;
        private boolean ignorePrevTextChange;
        private boolean nextChangeIsSend;
        private CharSequence prevText;
        private boolean processChange;

        41() {
            ChatActivityEnterView.this = r1;
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!this.ignorePrevTextChange && ChatActivityEnterView.this.recordingAudioVideo) {
                this.prevText = charSequence.toString();
            }
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            int currentPage;
            if (this.ignorePrevTextChange) {
                return;
            }
            if (ChatActivityEnterView.this.emojiView != null) {
                currentPage = ChatActivityEnterView.this.emojiView.getCurrentPage();
            } else {
                currentPage = MessagesController.getGlobalEmojiSettings().getInt("selected_page", 0);
            }
            boolean z = currentPage != 0 && (ChatActivityEnterView.this.allowStickers || ChatActivityEnterView.this.allowGifs);
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
            if (ChatActivityEnterView.this.sendByEnter && !ChatActivityEnterView.this.isPaste && ChatActivityEnterView.this.editingMessageObject == null && i3 > i2 && charSequence.length() > 0 && charSequence.length() == i + i3 && charSequence.charAt(charSequence.length() - 1) == '\n') {
                this.nextChangeIsSend = true;
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

        /* JADX WARN: Removed duplicated region for block: B:139:0x017a  */
        /* JADX WARN: Removed duplicated region for block: B:142:0x018c  */
        /* JADX WARN: Removed duplicated region for block: B:143:0x018e  */
        /* JADX WARN: Removed duplicated region for block: B:146:0x0194  */
        /* JADX WARN: Removed duplicated region for block: B:150:0x01c2  */
        @Override // android.text.TextWatcher
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void afterTextChanged(Editable editable) {
            boolean z;
            ChatActivityEnterView chatActivityEnterView;
            BotCommandsMenuContainer botCommandsMenuContainer;
            ChatActivityEnterView chatActivityEnterView2;
            int i;
            if (this.ignorePrevTextChange) {
                return;
            }
            if (this.prevText == null) {
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
                if (ChatActivityEnterView.this.currentLimit <= 0 || (i = ChatActivityEnterView.this.currentLimit - ChatActivityEnterView.this.codePointCount) > 100) {
                    if (ChatActivityEnterView.this.captionLimitView != null) {
                        ChatActivityEnterView.this.captionLimitView.animate().alpha(0.0f).scaleX(0.5f).scaleY(0.5f).setDuration(100L).setListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.41.1
                            {
                                41.this = this;
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                ChatActivityEnterView.this.captionLimitView.setVisibility(8);
                            }
                        });
                    }
                } else {
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
                        z = false;
                        chatActivityEnterView = ChatActivityEnterView.this;
                        if (chatActivityEnterView.doneButtonEnabled != z && (chatActivityEnterView.doneButtonImage != null || ChatActivityEnterView.this.doneCheckDrawable != null)) {
                            chatActivityEnterView2 = ChatActivityEnterView.this;
                            chatActivityEnterView2.doneButtonEnabled = z;
                            if (chatActivityEnterView2.doneButtonColorAnimator != null) {
                                ChatActivityEnterView.this.doneButtonColorAnimator.cancel();
                            }
                            ChatActivityEnterView chatActivityEnterView3 = ChatActivityEnterView.this;
                            float[] fArr = new float[2];
                            boolean z2 = chatActivityEnterView3.doneButtonEnabled;
                            fArr[0] = !z2 ? 0.0f : 1.0f;
                            fArr[1] = z2 ? 1.0f : 0.0f;
                            chatActivityEnterView3.doneButtonColorAnimator = ValueAnimator.ofFloat(fArr);
                            ChatActivityEnterView.this.doneButtonColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$41$$ExternalSyntheticLambda0
                                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    ChatActivityEnterView.41.this.lambda$afterTextChanged$0(valueAnimator);
                                }
                            });
                            ChatActivityEnterView.this.doneButtonColorAnimator.setDuration(150L).start();
                        }
                        botCommandsMenuContainer = ChatActivityEnterView.this.botCommandsMenuContainer;
                        if (botCommandsMenuContainer != null) {
                            botCommandsMenuContainer.dismiss();
                        }
                        ChatActivityEnterView.this.checkBotMenu();
                        if (ChatActivityEnterView.this.editingCaption || ChatActivityEnterView.this.captionLimitBulletinShown || MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).premiumLocked || UserConfig.getInstance(ChatActivityEnterView.this.currentAccount).isPremium() || ChatActivityEnterView.this.codePointCount <= MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).captionLengthLimitDefault || ChatActivityEnterView.this.codePointCount >= MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).captionLengthLimitPremium) {
                            return;
                        }
                        ChatActivityEnterView.this.captionLimitBulletinShown = true;
                        if (!this.heightShouldBeChanged) {
                            ChatActivityEnterView.this.showCaptionLimitBulletin();
                            return;
                        } else {
                            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$41$$ExternalSyntheticLambda1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ChatActivityEnterView.41.this.lambda$afterTextChanged$1();
                                }
                            }, 300L);
                            return;
                        }
                    }
                    ChatActivityEnterView.this.captionLimitView.setTextColor(ChatActivityEnterView.this.getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
                }
                z = true;
                chatActivityEnterView = ChatActivityEnterView.this;
                if (chatActivityEnterView.doneButtonEnabled != z) {
                    chatActivityEnterView2 = ChatActivityEnterView.this;
                    chatActivityEnterView2.doneButtonEnabled = z;
                    if (chatActivityEnterView2.doneButtonColorAnimator != null) {
                    }
                    ChatActivityEnterView chatActivityEnterView32 = ChatActivityEnterView.this;
                    float[] fArr2 = new float[2];
                    boolean z22 = chatActivityEnterView32.doneButtonEnabled;
                    fArr2[0] = !z22 ? 0.0f : 1.0f;
                    fArr2[1] = z22 ? 1.0f : 0.0f;
                    chatActivityEnterView32.doneButtonColorAnimator = ValueAnimator.ofFloat(fArr2);
                    ChatActivityEnterView.this.doneButtonColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$41$$ExternalSyntheticLambda0
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            ChatActivityEnterView.41.this.lambda$afterTextChanged$0(valueAnimator);
                        }
                    });
                    ChatActivityEnterView.this.doneButtonColorAnimator.setDuration(150L).start();
                }
                botCommandsMenuContainer = ChatActivityEnterView.this.botCommandsMenuContainer;
                if (botCommandsMenuContainer != null) {
                }
                ChatActivityEnterView.this.checkBotMenu();
                if (ChatActivityEnterView.this.editingCaption) {
                    return;
                }
                return;
            }
            this.ignorePrevTextChange = true;
            editable.replace(0, editable.length(), this.prevText);
            this.prevText = null;
            this.ignorePrevTextChange = false;
        }

        public /* synthetic */ void lambda$afterTextChanged$0(ValueAnimator valueAnimator) {
            int themedColor = ChatActivityEnterView.this.getThemedColor(Theme.key_chat_messagePanelVoicePressed);
            int alpha = Color.alpha(themedColor);
            ChatActivityEnterView.this.doneButtonEnabledProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (ChatActivityEnterView.this.doneCheckDrawable != null) {
                ChatActivityEnterView.this.doneCheckDrawable.setColorFilter(new PorterDuffColorFilter(ColorUtils.setAlphaComponent(themedColor, (int) (alpha * ((ChatActivityEnterView.this.doneButtonEnabledProgress * 0.42f) + 0.58f))), PorterDuff.Mode.MULTIPLY));
            }
            if (ChatActivityEnterView.this.doneButtonImage != null) {
                ChatActivityEnterView.this.doneButtonImage.invalidate();
            }
        }

        public /* synthetic */ void lambda$afterTextChanged$1() {
            ChatActivityEnterView.this.showCaptionLimitBulletin();
        }
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.addTextChangedListener(textWatcher);
            return;
        }
        if (this.messageEditTextWatchers == null) {
            this.messageEditTextWatchers = new ArrayList<>();
        }
        this.messageEditTextWatchers.add(textWatcher);
    }

    public boolean isSendButtonVisible() {
        return this.sendButton.getVisibility() == 0;
    }

    public void setRecordVideoButtonVisible(boolean z, boolean z2) {
        if (this.audioVideoSendButton == null) {
            return;
        }
        this.isInVideoMode = z;
        if (z2) {
            SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
            boolean z3 = false;
            if (DialogObject.isChatDialog(this.dialog_id)) {
                TLRPC$Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
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

    public boolean isRecordingAudioVideo() {
        AnimatorSet animatorSet;
        return this.recordingAudioVideo || ((animatorSet = this.runningAnimationAudio) != null && animatorSet.isRunning());
    }

    public boolean isRecordLocked() {
        return this.recordingAudioVideo && this.recordCircle.isSendButtonVisible();
    }

    public void cancelRecordingAudioVideo() {
        if (this.hasRecordVideo && isInVideoMode()) {
            CameraController.getInstance().cancelOnInitRunnable(this.onFinishInitCameraRunnable);
            this.delegate.needStartRecordVideo(5, true, 0);
        } else {
            this.delegate.needStartRecordAudio(0);
            MediaController.getInstance().stopRecording(0, false, 0);
        }
        this.recordingAudioVideo = false;
        updateRecordInterface(2);
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

    public void setCaption(String str) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.setCaption(str);
            checkSendButton(true);
        }
    }

    public void setSlowModeTimer(int i) {
        this.slowModeTimer = i;
        updateSlowModeText();
    }

    public CharSequence getSlowModeTimer() {
        if (this.slowModeTimer > 0) {
            return this.slowModeButton.getText();
        }
        return null;
    }

    public void updateSlowModeText() {
        int i;
        boolean isUploadingMessageIdDialog;
        int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        AndroidUtilities.cancelRunOnUIThread(this.updateSlowModeRunnable);
        this.updateSlowModeRunnable = null;
        TLRPC$ChatFull tLRPC$ChatFull = this.info;
        if (tLRPC$ChatFull != null && tLRPC$ChatFull.slowmode_seconds != 0 && tLRPC$ChatFull.slowmode_next_send_date <= currentTime && ((isUploadingMessageIdDialog = SendMessagesHelper.getInstance(this.currentAccount).isUploadingMessageIdDialog(this.dialog_id)) || SendMessagesHelper.getInstance(this.currentAccount).isSendingMessageIdDialog(this.dialog_id))) {
            if (!ChatObject.hasAdminRights(this.accountInstance.getMessagesController().getChat(Long.valueOf(this.info.id)))) {
                i = this.info.slowmode_seconds;
                this.slowModeTimer = isUploadingMessageIdDialog ? ConnectionsManager.DEFAULT_DATACENTER_ID : 2147483646;
            }
            i = 0;
        } else {
            int i2 = this.slowModeTimer;
            if (i2 >= 2147483646) {
                if (this.info != null) {
                    this.accountInstance.getMessagesController().loadFullChat(this.info.id, 0, true);
                }
                i = 0;
            } else {
                i = i2 - currentTime;
            }
        }
        if (this.slowModeTimer != 0 && i > 0) {
            this.slowModeButton.setText(AndroidUtilities.formatDurationNoHours(Math.max(1, i), false));
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                SimpleTextView simpleTextView = this.slowModeButton;
                chatActivityEnterViewDelegate.onUpdateSlowModeButton(simpleTextView, false, simpleTextView.getText());
            }
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda38
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.updateSlowModeText();
                }
            };
            this.updateSlowModeRunnable = runnable;
            AndroidUtilities.runOnUIThread(runnable, 100L);
        } else {
            this.slowModeTimer = 0;
        }
        if (isInScheduleMode()) {
            return;
        }
        checkSendButton(true);
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

    public void setForceShowSendButton(boolean z, boolean z2) {
        this.forceShowSendButton = z;
        checkSendButton(z2);
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

    public void addEmojiToRecent(String str) {
        createEmojiView();
        this.emojiView.addEmojiToRecent(str);
    }

    public void setOpenGifsTabFirst() {
        createEmojiView();
        MediaDataController.getInstance(this.currentAccount).loadRecents(0, true, true, false);
        this.emojiView.switchToGifRecent();
    }

    public /* synthetic */ void lambda$new$31(ValueAnimator valueAnimator) {
        MentionsContainerView mentionsContainerView;
        if (this.topView != null) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.topViewEnterProgress = floatValue;
            View view = this.topView;
            float f = 1.0f - floatValue;
            view.setTranslationY(this.animatedTop + (view.getLayoutParams().height * f));
            this.topLineView.setAlpha(floatValue);
            this.topLineView.setTranslationY(this.animatedTop);
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity == null || (mentionsContainerView = chatActivity.mentionContainer) == null) {
                return;
            }
            mentionsContainerView.setTranslationY(f * this.topView.getLayoutParams().height);
        }
    }

    public void showTopView(boolean z, boolean z2) {
        showTopView(z, z2, false);
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
            Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda45
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$showTopView$32();
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
                this.currentTopViewAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.42
                    {
                        ChatActivityEnterView.this = this;
                    }

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

    public /* synthetic */ void lambda$showTopView$32() {
        showTopView(true, false, true);
        this.showTopViewRunnable = null;
    }

    public void onEditTimeExpired() {
        FrameLayout frameLayout = this.doneButtonContainer;
        if (frameLayout != null) {
            frameLayout.setVisibility(8);
        }
    }

    public void showEditDoneProgress(final boolean z, boolean z2) {
        if (this.doneButtonContainer == null) {
            return;
        }
        AnimatorSet animatorSet = this.doneButtonAnimation;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (z2) {
            this.doneButtonAnimation = new AnimatorSet();
            if (z) {
                this.doneButtonProgress.setVisibility(0);
                this.doneButtonContainer.setEnabled(false);
                this.doneButtonAnimation.playTogether(ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.doneButtonImage, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.doneButtonProgress, View.ALPHA, 1.0f));
            } else {
                this.doneButtonImage.setVisibility(0);
                this.doneButtonContainer.setEnabled(true);
                this.doneButtonAnimation.playTogether(ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_X, 0.1f), ObjectAnimator.ofFloat(this.doneButtonProgress, View.SCALE_Y, 0.1f), ObjectAnimator.ofFloat(this.doneButtonProgress, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.doneButtonImage, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.doneButtonImage, View.ALPHA, 1.0f));
            }
            this.doneButtonAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.43
                {
                    ChatActivityEnterView.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (ChatActivityEnterView.this.doneButtonAnimation == null || !ChatActivityEnterView.this.doneButtonAnimation.equals(animator)) {
                        return;
                    }
                    if (!z) {
                        ChatActivityEnterView.this.doneButtonProgress.setVisibility(4);
                    } else {
                        ChatActivityEnterView.this.doneButtonImage.setVisibility(4);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (ChatActivityEnterView.this.doneButtonAnimation == null || !ChatActivityEnterView.this.doneButtonAnimation.equals(animator)) {
                        return;
                    }
                    ChatActivityEnterView.this.doneButtonAnimation = null;
                }
            });
            this.doneButtonAnimation.setDuration(150L);
            this.doneButtonAnimation.start();
        } else if (z) {
            this.doneButtonImage.setScaleX(0.1f);
            this.doneButtonImage.setScaleY(0.1f);
            this.doneButtonImage.setAlpha(0.0f);
            this.doneButtonProgress.setScaleX(1.0f);
            this.doneButtonProgress.setScaleY(1.0f);
            this.doneButtonProgress.setAlpha(1.0f);
            this.doneButtonImage.setVisibility(4);
            this.doneButtonProgress.setVisibility(0);
            this.doneButtonContainer.setEnabled(false);
        } else {
            this.doneButtonProgress.setScaleX(0.1f);
            this.doneButtonProgress.setScaleY(0.1f);
            this.doneButtonProgress.setAlpha(0.0f);
            this.doneButtonImage.setScaleX(1.0f);
            this.doneButtonImage.setScaleY(1.0f);
            this.doneButtonImage.setAlpha(1.0f);
            this.doneButtonImage.setVisibility(0);
            this.doneButtonProgress.setVisibility(4);
            this.doneButtonContainer.setEnabled(true);
        }
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
                this.currentTopViewAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.44
                    {
                        ChatActivityEnterView.this = this;
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

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        ValueAnimator valueAnimator2 = ChatActivityEnterView.this.currentTopViewAnimation;
                        if (valueAnimator2 == null || !valueAnimator2.equals(animator)) {
                            return;
                        }
                        ChatActivityEnterView.this.currentTopViewAnimation = null;
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
            View view = this.topView;
            view.setTranslationY(view.getLayoutParams().height);
        }
    }

    public boolean isTopViewVisible() {
        View view = this.topView;
        return view != null && view.getVisibility() == 0;
    }

    public void onAdjustPanTransitionUpdate(float f, float f2, boolean z) {
        BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
        if (botWebViewMenuContainer != null) {
            botWebViewMenuContainer.setTranslationY(f);
        }
    }

    public void onAdjustPanTransitionEnd() {
        BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
        if (botWebViewMenuContainer != null) {
            botWebViewMenuContainer.onPanTransitionEnd();
        }
        Runnable runnable = this.onKeyboardClosed;
        if (runnable != null) {
            runnable.run();
            this.onKeyboardClosed = null;
        }
    }

    public void onAdjustPanTransitionStart(boolean z, int i) {
        EditTextCaption editTextCaption;
        BotWebViewMenuContainer botWebViewMenuContainer;
        Runnable runnable;
        BotWebViewMenuContainer botWebViewMenuContainer2 = this.botWebViewMenuContainer;
        if (botWebViewMenuContainer2 != null) {
            botWebViewMenuContainer2.onPanTransitionStart(z, i);
        }
        if (z && (runnable = this.showTopViewRunnable) != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.showTopViewRunnable.run();
        }
        Runnable runnable2 = this.setTextFieldRunnable;
        if (runnable2 != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable2);
            this.setTextFieldRunnable.run();
        }
        if (z && (editTextCaption = this.messageEditText) != null && editTextCaption.hasFocus() && hasBotWebView() && botCommandsMenuIsShowing() && (botWebViewMenuContainer = this.botWebViewMenuContainer) != null) {
            botWebViewMenuContainer.dismiss();
        }
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
            if (height < AndroidUtilities.dp(72.0f) + ActionBar.getCurrentActionBarHeight()) {
                if (this.allowShowTopView) {
                    this.allowShowTopView = false;
                    if (this.needShowTopView) {
                        this.topView.setVisibility(8);
                        this.topLineView.setVisibility(8);
                        this.topLineView.setAlpha(0.0f);
                        resizeForTopView(false);
                        this.topViewEnterProgress = 0.0f;
                        View view = this.topView;
                        view.setTranslationY(view.getLayoutParams().height);
                    }
                }
            } else if (this.allowShowTopView) {
            } else {
                this.allowShowTopView = true;
                if (this.needShowTopView) {
                    this.topView.setVisibility(0);
                    this.topLineView.setVisibility(0);
                    this.topLineView.setAlpha(1.0f);
                    resizeForTopView(true);
                    this.topViewEnterProgress = 1.0f;
                    this.topView.setTranslationY(0.0f);
                }
            }
        }
    }

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

    public void onDestroy() {
        this.destroyed = true;
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStarted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStartError);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordStopped);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recordProgressChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioDidSent);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.audioRouteChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messageReceivedByServer);
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

    public void checkChannelRights() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null) {
            return;
        }
        updateRecordButton(chatActivity.getCurrentChat(), this.parentFragment.getCurrentUserInfo());
    }

    public void updateRecordButton(TLRPC$Chat tLRPC$Chat, TLRPC$UserFull tLRPC$UserFull) {
        EmojiView emojiView;
        this.emojiButtonRestricted = false;
        boolean z = true;
        this.stickersEnabled = true;
        this.sendPlainEnabled = true;
        this.sendRoundEnabled = true;
        this.sendVoiceEnabled = true;
        if (tLRPC$Chat != null) {
            this.audioVideoButtonContainer.setAlpha((ChatObject.canSendVoice(tLRPC$Chat) || (ChatObject.canSendRoundVideo(tLRPC$Chat) && this.hasRecordVideo)) ? 1.0f : 0.5f);
            this.stickersEnabled = ChatObject.canSendStickers(tLRPC$Chat);
            this.sendPlainEnabled = ChatObject.canSendPlain(tLRPC$Chat);
            boolean canSendPlain = ChatObject.canSendPlain(tLRPC$Chat);
            this.sendPlainEnabled = canSendPlain;
            boolean z2 = (this.stickersEnabled || canSendPlain) ? false : true;
            this.emojiButtonRestricted = z2;
            this.emojiButtonAlpha = z2 ? 0.5f : 1.0f;
            updateEmojiButtonParams();
            if (!this.emojiButtonRestricted && (emojiView = this.emojiView) != null) {
                emojiView.setStickersBanned(!ChatObject.canSendPlain(tLRPC$Chat), !ChatObject.canSendStickers(tLRPC$Chat), tLRPC$Chat.id);
            }
            this.sendRoundEnabled = ChatObject.canSendRoundVideo(tLRPC$Chat);
            this.sendVoiceEnabled = ChatObject.canSendVoice(tLRPC$Chat);
        } else if (tLRPC$UserFull != null) {
            this.userInfo = tLRPC$UserFull;
            this.audioVideoButtonContainer.setAlpha(tLRPC$UserFull.voice_messages_forbidden ? 0.5f : 1.0f);
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

    public void onBeginHide() {
        Runnable runnable = this.focusRunnable;
        if (runnable != null) {
            AndroidUtilities.cancelRunOnUIThread(runnable);
            this.focusRunnable = null;
        }
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
        Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda37
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$onPause$33();
            }
        };
        this.hideKeyboardRunnable = runnable;
        AndroidUtilities.runOnUIThread(runnable, 500L);
    }

    public /* synthetic */ void lambda$onPause$33() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || chatActivity.isLastFragment()) {
            closeKeyboard();
        }
        this.hideKeyboardRunnable = null;
    }

    public void onResume() {
        ChatActivity chatActivity;
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
        if (this.showKeyboardOnResume && (chatActivity = this.parentFragment) != null && chatActivity.isLastFragment()) {
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

    public void setDialogId(long j, int i) {
        this.dialog_id = j;
        if (this.currentAccount != i) {
            this.notificationsLocker.unlock();
            NotificationCenter notificationCenter = NotificationCenter.getInstance(this.currentAccount);
            int i2 = NotificationCenter.recordStarted;
            notificationCenter.removeObserver(this, i2);
            NotificationCenter notificationCenter2 = NotificationCenter.getInstance(this.currentAccount);
            int i3 = NotificationCenter.recordStartError;
            notificationCenter2.removeObserver(this, i3);
            NotificationCenter notificationCenter3 = NotificationCenter.getInstance(this.currentAccount);
            int i4 = NotificationCenter.recordStopped;
            notificationCenter3.removeObserver(this, i4);
            NotificationCenter notificationCenter4 = NotificationCenter.getInstance(this.currentAccount);
            int i5 = NotificationCenter.recordProgressChanged;
            notificationCenter4.removeObserver(this, i5);
            NotificationCenter notificationCenter5 = NotificationCenter.getInstance(this.currentAccount);
            int i6 = NotificationCenter.closeChats;
            notificationCenter5.removeObserver(this, i6);
            NotificationCenter notificationCenter6 = NotificationCenter.getInstance(this.currentAccount);
            int i7 = NotificationCenter.audioDidSent;
            notificationCenter6.removeObserver(this, i7);
            NotificationCenter notificationCenter7 = NotificationCenter.getInstance(this.currentAccount);
            int i8 = NotificationCenter.audioRouteChanged;
            notificationCenter7.removeObserver(this, i8);
            NotificationCenter notificationCenter8 = NotificationCenter.getInstance(this.currentAccount);
            int i9 = NotificationCenter.messagePlayingDidReset;
            notificationCenter8.removeObserver(this, i9);
            NotificationCenter notificationCenter9 = NotificationCenter.getInstance(this.currentAccount);
            int i10 = NotificationCenter.messagePlayingProgressDidChanged;
            notificationCenter9.removeObserver(this, i10);
            NotificationCenter notificationCenter10 = NotificationCenter.getInstance(this.currentAccount);
            int i11 = NotificationCenter.featuredStickersDidLoad;
            notificationCenter10.removeObserver(this, i11);
            NotificationCenter notificationCenter11 = NotificationCenter.getInstance(this.currentAccount);
            int i12 = NotificationCenter.messageReceivedByServer;
            notificationCenter11.removeObserver(this, i12);
            NotificationCenter notificationCenter12 = NotificationCenter.getInstance(this.currentAccount);
            int i13 = NotificationCenter.sendingMessagesChanged;
            notificationCenter12.removeObserver(this, i13);
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
        }
        boolean z = true;
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
            updateSendAsButton((chatActivity == null || !chatActivity.getFragmentBeginToShow()) ? false : false);
        }
    }

    public void setChatInfo(TLRPC$ChatFull tLRPC$ChatFull) {
        this.info = tLRPC$ChatFull;
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.setChatInfo(tLRPC$ChatFull);
        }
        setSlowModeTimer(tLRPC$ChatFull.slowmode_next_send_date);
    }

    public void checkRoundVideo() {
        boolean z;
        boolean z2;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        if (this.hasRecordVideo) {
            return;
        }
        if (this.attachLayout == null || Build.VERSION.SDK_INT < 18) {
            this.hasRecordVideo = false;
            setRecordVideoButtonVisible(false, false);
            return;
        }
        boolean z3 = true;
        this.hasRecordVideo = true;
        this.sendRoundEnabled = true;
        this.sendVoiceEnabled = true;
        if (DialogObject.isChatDialog(this.dialog_id)) {
            TLRPC$Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
            z = ChatObject.isChannel(chat) && !chat.megagroup;
            if (z && !chat.creator && ((tLRPC$TL_chatAdminRights = chat.admin_rights) == null || !tLRPC$TL_chatAdminRights.post_messages)) {
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

    public boolean isInVideoMode() {
        return this.isInVideoMode;
    }

    public boolean hasRecordVideo() {
        return this.hasRecordVideo;
    }

    public MessageObject getReplyingMessageObject() {
        return this.replyingMessageObject;
    }

    public void updateFieldHint(boolean z) {
        boolean z2;
        MessageObject messageObject;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup;
        TLRPC$ReplyMarkup tLRPC$ReplyMarkup2;
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return;
        }
        CharSequence charSequence = this.overrideHint;
        if (charSequence != null) {
            editTextCaption.setHintText(charSequence, z);
            this.messageEditText.setHintText2(this.overrideHint2, z);
            return;
        }
        boolean z3 = false;
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
        int i = this.commonInputType;
        if (inputType != i) {
            this.messageEditText.setInputType(i);
        }
        MessageObject messageObject2 = this.replyingMessageObject;
        if (messageObject2 != null && (tLRPC$ReplyMarkup2 = messageObject2.messageOwner.reply_markup) != null && !TextUtils.isEmpty(tLRPC$ReplyMarkup2.placeholder)) {
            this.messageEditText.setHintText(this.replyingMessageObject.messageOwner.reply_markup.placeholder, z);
        } else if (this.editingMessageObject != null) {
            this.messageEditText.setHintText(this.editingCaption ? LocaleController.getString("Caption", R.string.Caption) : LocaleController.getString("TypeMessage", R.string.TypeMessage));
        } else if (this.botKeyboardViewVisible && (messageObject = this.botButtonsMessageObject) != null && (tLRPC$ReplyMarkup = messageObject.messageOwner.reply_markup) != null && !TextUtils.isEmpty(tLRPC$ReplyMarkup.placeholder)) {
            this.messageEditText.setHintText(this.botButtonsMessageObject.messageOwner.reply_markup.placeholder, z);
        } else {
            if (DialogObject.isChatDialog(this.dialog_id)) {
                TLRPC$Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
                TLRPC$ChatFull chatFull = this.accountInstance.getMessagesController().getChatFull(-this.dialog_id);
                z2 = ChatObject.isChannel(chat) && !chat.megagroup;
                z3 = ChatObject.getSendAsPeerId(chat, chatFull) == chat.id;
            } else {
                z2 = false;
            }
            if (z3) {
                this.messageEditText.setHintText(LocaleController.getString("SendAnonymously", R.string.SendAnonymously));
                return;
            }
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity != null && chatActivity.isThreadChat()) {
                ChatActivity chatActivity2 = this.parentFragment;
                if (!chatActivity2.isTopic) {
                    if (chatActivity2.isReplyChatComment()) {
                        this.messageEditText.setHintText(LocaleController.getString("Comment", R.string.Comment));
                        return;
                    } else {
                        this.messageEditText.setHintText(LocaleController.getString("Reply", R.string.Reply));
                        return;
                    }
                }
            }
            if (z2) {
                if (this.silent) {
                    this.messageEditText.setHintText(LocaleController.getString("ChannelSilentBroadcast", R.string.ChannelSilentBroadcast), z);
                    return;
                } else {
                    this.messageEditText.setHintText(LocaleController.getString("ChannelBroadcast", R.string.ChannelBroadcast), z);
                    return;
                }
            }
            this.messageEditText.setHintText(LocaleController.getString("TypeMessage", R.string.TypeMessage));
        }
    }

    public void setReplyingMessageObject(MessageObject messageObject, ChatActivity.ReplyQuote replyQuote) {
        MessageObject messageObject2;
        if (messageObject != null) {
            if (this.botMessageObject == null && (messageObject2 = this.botButtonsMessageObject) != this.replyingMessageObject) {
                this.botMessageObject = messageObject2;
            }
            this.replyingMessageObject = messageObject;
            this.replyingQuote = replyQuote;
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity == null || !chatActivity.isTopic || chatActivity.getThreadMessage() != this.replyingMessageObject) {
                setButtons(this.replyingMessageObject, true);
            }
        } else if (this.replyingMessageObject == this.botButtonsMessageObject) {
            this.replyingMessageObject = null;
            this.replyingQuote = null;
            setButtons(this.botMessageObject, false);
            this.botMessageObject = null;
        } else {
            this.replyingMessageObject = null;
            this.replyingQuote = null;
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        MediaController.getInstance().setReplyingMessage(messageObject, getThreadMessage(), chatActivityEnterViewDelegate != null ? chatActivityEnterViewDelegate.getReplyToStory() : null);
        updateFieldHint(false);
    }

    public void setWebPage(TLRPC$WebPage tLRPC$WebPage, boolean z) {
        this.messageWebPage = tLRPC$WebPage;
        this.messageWebPageSearch = z;
    }

    public boolean isMessageWebPageSearchEnabled() {
        return this.messageWebPageSearch;
    }

    private void hideRecordedAudioPanel(boolean z) {
        AnimatorSet animatorSet;
        AnimatorSet animatorSet2 = this.recordPannelAnimation;
        if (animatorSet2 == null || !animatorSet2.isRunning()) {
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
                AnimatorSet animatorSet3 = new AnimatorSet();
                this.recordPannelAnimation = animatorSet3;
                Animator[] animatorArr = new Animator[11];
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView2 = this.emojiButton;
                Property<? super View, Float> property = this.EMOJI_BUTTON_ALPHA;
                float[] fArr = new float[1];
                fArr[0] = this.emojiButtonRestricted ? 0.5f : 1.0f;
                animatorArr[0] = ObjectAnimator.ofFloat(chatActivityEnterViewAnimatedIconView2, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property, fArr);
                animatorArr[1] = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 1.0f);
                animatorArr[2] = ObjectAnimator.ofFloat(this.recordDeleteImageView, View.ALPHA, 0.0f);
                animatorArr[3] = ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_X, 0.0f);
                animatorArr[4] = ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_Y, 0.0f);
                animatorArr[5] = ObjectAnimator.ofFloat(this.recordedAudioPanel, View.ALPHA, 0.0f);
                animatorArr[6] = ObjectAnimator.ofFloat(this.attachButton, View.ALPHA, 1.0f);
                animatorArr[7] = ObjectAnimator.ofFloat(this.attachButton, View.SCALE_X, 1.0f);
                animatorArr[8] = ObjectAnimator.ofFloat(this.attachButton, View.SCALE_Y, 1.0f);
                animatorArr[9] = ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f);
                animatorArr[10] = ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) this.MESSAGE_TEXT_TRANSLATION_X, 0.0f);
                animatorSet3.playTogether(animatorArr);
                BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
                if (botCommandsMenuView != null) {
                    botCommandsMenuView.setAlpha(0.0f);
                    this.botCommandsMenuButton.setScaleY(0.0f);
                    this.botCommandsMenuButton.setScaleX(0.0f);
                    this.recordPannelAnimation.playTogether(ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_Y, 1.0f));
                }
                this.recordPannelAnimation.setDuration(150L);
                this.recordPannelAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.45
                    {
                        ChatActivityEnterView.this = this;
                    }

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
                });
            } else {
                RLottieImageView rLottieImageView = this.recordDeleteImageView;
                if (rLottieImageView != null) {
                    rLottieImageView.playAnimation();
                }
                AnimatorSet animatorSet4 = new AnimatorSet();
                if (isInVideoMode()) {
                    animatorSet4.playTogether(ObjectAnimator.ofFloat(this.videoTimelineView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.videoTimelineView, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) this.MESSAGE_TEXT_TRANSLATION_X, 0.0f));
                    if (this.emojiButtonPaddingAlpha == 1.0f) {
                        animatorSet4.playTogether(ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f));
                    } else {
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f);
                        ofFloat.setStartDelay(750L);
                        ofFloat.setDuration(200L);
                        animatorSet4.playTogether(ofFloat);
                    }
                } else {
                    EditTextCaption editTextCaption = this.messageEditText;
                    if (editTextCaption != null && this.emojiButtonPaddingAlpha == 1.0f) {
                        editTextCaption.setAlpha(1.0f);
                        this.messageTextTranslationX = 0.0f;
                        updateMessageTextParams();
                    } else {
                        this.messageTextTranslationX = 0.0f;
                        updateMessageTextParams();
                        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f);
                        ofFloat2.setStartDelay(750L);
                        ofFloat2.setDuration(200L);
                        animatorSet4.playTogether(ofFloat2);
                    }
                    animatorSet4.playTogether(ObjectAnimator.ofFloat(this.recordedAudioSeekBar, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioPlayButton, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioBackground, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioTimeTextView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioSeekBar, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.recordedAudioPlayButton, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.recordedAudioBackground, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.recordedAudioTimeTextView, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)));
                }
                animatorSet4.setDuration(200L);
                ImageView imageView = this.attachButton;
                if (imageView != null) {
                    imageView.setAlpha(0.0f);
                    this.attachButton.setScaleX(0.0f);
                    this.attachButton.setScaleY(0.0f);
                    animatorSet = new AnimatorSet();
                    animatorSet.playTogether(ObjectAnimator.ofFloat(this.attachButton, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.attachButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.attachButton, View.SCALE_Y, 1.0f));
                    animatorSet.setDuration(150L);
                } else {
                    animatorSet = null;
                }
                this.emojiButtonScale = 0.0f;
                this.emojiButtonAlpha = 0.0f;
                updateEmojiButtonParams();
                AnimatorSet animatorSet5 = new AnimatorSet();
                Animator[] animatorArr2 = new Animator[6];
                animatorArr2[0] = ObjectAnimator.ofFloat(this.recordDeleteImageView, View.ALPHA, 0.0f);
                animatorArr2[1] = ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_X, 0.0f);
                animatorArr2[2] = ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_Y, 0.0f);
                animatorArr2[3] = ObjectAnimator.ofFloat(this.recordDeleteImageView, View.ALPHA, 0.0f);
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView3 = this.emojiButton;
                Property<? super View, Float> property2 = this.EMOJI_BUTTON_ALPHA;
                float[] fArr2 = new float[1];
                fArr2[0] = this.emojiButtonRestricted ? 0.5f : 1.0f;
                animatorArr2[4] = ObjectAnimator.ofFloat(chatActivityEnterViewAnimatedIconView3, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property2, fArr2);
                animatorArr2[5] = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 1.0f);
                animatorSet5.playTogether(animatorArr2);
                BotCommandsMenuView botCommandsMenuView2 = this.botCommandsMenuButton;
                if (botCommandsMenuView2 != null) {
                    botCommandsMenuView2.setAlpha(0.0f);
                    this.botCommandsMenuButton.setScaleY(0.0f);
                    this.botCommandsMenuButton.setScaleX(0.0f);
                    animatorSet5.playTogether(ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_Y, 1.0f));
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
                this.recordPannelAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.46
                    {
                        ChatActivityEnterView.this = this;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        ChatActivityEnterView.this.hideRecordedAudioPanelInternal();
                    }
                });
            }
            AnimatorSet animatorSet7 = this.recordPannelAnimation;
            if (animatorSet7 != null) {
                animatorSet7.start();
            }
        }
    }

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

    public void sendMessage() {
        if (isInScheduleMode()) {
            AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView.47
                {
                    ChatActivityEnterView.this = this;
                }

                @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                public void didSelectDate(boolean z, int i) {
                    ChatActivityEnterView.this.sendMessageInternal(z, i, true);
                }
            }, this.resourcesProvider);
        } else {
            sendMessageInternal(true, 0, true);
        }
    }

    public void sendMessageInternal(final boolean z, final int i, boolean z2) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate;
        TLRPC$Chat currentChat;
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
            TLRPC$Chat currentChat2 = chatActivity.getCurrentChat();
            if (this.parentFragment.getCurrentUser() != null || ((ChatObject.isChannel(currentChat2) && currentChat2.megagroup) || !ChatObject.isChannel(currentChat2))) {
                SharedPreferences.Editor edit = MessagesController.getNotificationsSettings(this.currentAccount).edit();
                edit.putBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + this.dialog_id, !z).commit();
            }
        }
        if (this.stickersExpanded) {
            setStickersExpanded(false, true, false);
            if (this.searchingType != 0 && (emojiView = this.emojiView) != null) {
                emojiView.closeSearch(false);
                this.emojiView.hideSearchKeyboard();
            }
        }
        if (z2 && showConfirmAlert(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda55
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$sendMessageInternal$34(z, i);
            }
        })) {
            return;
        }
        if (this.videoToSendMessageObject != null) {
            this.delegate.needStartRecordVideo(4, z, i);
            hideRecordedAudioPanel(true);
            checkSendButton(true);
        } else if (this.audioToSend != null) {
            MessageObject playingMessageObject = MediaController.getInstance().getPlayingMessageObject();
            if (playingMessageObject != null && playingMessageObject == this.audioToSendMessageObject) {
                MediaController.getInstance().cleanupPlayer(true, true);
            }
            SendMessagesHelper.SendMessageParams of = SendMessagesHelper.SendMessageParams.of(this.audioToSend, null, this.audioToSendPath, this.dialog_id, this.replyingMessageObject, getThreadMessage(), null, null, null, null, z, i, 0, null, null, false);
            applyStoryToSendMessageParams(of);
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(of);
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate3 = this.delegate;
            if (chatActivityEnterViewDelegate3 != null) {
                chatActivityEnterViewDelegate3.onMessageSend(null, z, i);
            }
            hideRecordedAudioPanel(true);
            checkSendButton(true);
        } else {
            EditTextCaption editTextCaption = this.messageEditText;
            final String text = editTextCaption == null ? "" : editTextCaption.getText();
            ChatActivity chatActivity2 = this.parentFragment;
            if (chatActivity2 != null && (currentChat = chatActivity2.getCurrentChat()) != null && currentChat.slowmode_enabled && !ChatObject.hasAdminRights(currentChat)) {
                if (text.length() > this.accountInstance.getMessagesController().maxMessageLength) {
                    AlertsCreator.showSimpleAlert(this.parentFragment, LocaleController.getString("Slowmode", R.string.Slowmode), LocaleController.getString("SlowmodeSendErrorTooLong", R.string.SlowmodeSendErrorTooLong), this.resourcesProvider);
                    return;
                } else if (this.forceShowSendButton && text.length() > 0) {
                    AlertsCreator.showSimpleAlert(this.parentFragment, LocaleController.getString("Slowmode", R.string.Slowmode), LocaleController.getString("SlowmodeSendError", R.string.SlowmodeSendError), this.resourcesProvider);
                    return;
                }
            }
            if (checkPremiumAnimatedEmoji(this.currentAccount, this.dialog_id, this.parentFragment, null, text)) {
                return;
            }
            if (processSendingText(text, z, i)) {
                if (this.delegate.hasForwardingMessages() || ((i != 0 && !isInScheduleMode()) || isInScheduleMode())) {
                    EditTextCaption editTextCaption2 = this.messageEditText;
                    if (editTextCaption2 != null) {
                        editTextCaption2.setText("");
                    }
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate4 = this.delegate;
                    if (chatActivityEnterViewDelegate4 != null) {
                        chatActivityEnterViewDelegate4.onMessageSend(text, z, i);
                    }
                } else {
                    this.messageTransitionIsRunning = false;
                    Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda50
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatActivityEnterView.this.lambda$sendMessageInternal$35(text, z, i);
                        }
                    };
                    this.moveToSendStateRunnable = runnable;
                    AndroidUtilities.runOnUIThread(runnable, 200L);
                }
                this.lastTypingTimeSend = 0L;
            } else if (!this.forceShowSendButton || (chatActivityEnterViewDelegate = this.delegate) == null) {
            } else {
                chatActivityEnterViewDelegate.onMessageSend(null, z, i);
            }
        }
    }

    public /* synthetic */ void lambda$sendMessageInternal$34(boolean z, int i) {
        sendMessageInternal(z, i, false);
    }

    public /* synthetic */ void lambda$sendMessageInternal$35(CharSequence charSequence, boolean z, int i) {
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

    /* JADX WARN: Code restructure failed: missing block: B:210:0x0090, code lost:
        continue;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x0120, code lost:
        continue;
     */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0065  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean checkPremiumAnimatedEmoji(int i, long j, final BaseFragment baseFragment, FrameLayout frameLayout, CharSequence charSequence) {
        AnimatedEmojiSpan[] animatedEmojiSpanArr;
        ArrayList<TLRPC$Document> arrayList;
        ArrayList<TLRPC$Document> arrayList2;
        ArrayList<TLRPC$Document> arrayList3;
        if (charSequence != null && baseFragment != null && !UserConfig.getInstance(i).isPremium() && UserConfig.getInstance(i).getClientUserId() != j && (charSequence instanceof Spanned) && (animatedEmojiSpanArr = (AnimatedEmojiSpan[]) ((Spanned) charSequence).getSpans(0, charSequence.length(), AnimatedEmojiSpan.class)) != null) {
            for (int i2 = 0; i2 < animatedEmojiSpanArr.length; i2++) {
                if (animatedEmojiSpanArr[i2] != null) {
                    TLRPC$Document tLRPC$Document = animatedEmojiSpanArr[i2].document;
                    if (tLRPC$Document == null) {
                        tLRPC$Document = AnimatedEmojiDrawable.findDocument(i, animatedEmojiSpanArr[i2].getDocumentId());
                    }
                    long documentId = animatedEmojiSpanArr[i2].getDocumentId();
                    if (tLRPC$Document == null) {
                        Iterator<TLRPC$TL_messages_stickerSet> it = MediaDataController.getInstance(i).getStickerSets(5).iterator();
                        while (it.hasNext()) {
                            TLRPC$TL_messages_stickerSet next = it.next();
                            if (next != null && (arrayList3 = next.documents) != null && !arrayList3.isEmpty()) {
                                Iterator<TLRPC$Document> it2 = next.documents.iterator();
                                while (true) {
                                    if (it2.hasNext()) {
                                        TLRPC$Document next2 = it2.next();
                                        if (next2.id == documentId) {
                                            tLRPC$Document = next2;
                                            continue;
                                            break;
                                        }
                                    }
                                }
                            }
                            while (it.hasNext()) {
                            }
                        }
                    }
                    if (tLRPC$Document == null) {
                        Iterator<TLRPC$StickerSetCovered> it3 = MediaDataController.getInstance(i).getFeaturedEmojiSets().iterator();
                        while (it3.hasNext()) {
                            TLRPC$StickerSetCovered next3 = it3.next();
                            if (next3 != null && (arrayList2 = next3.covers) != null && !arrayList2.isEmpty()) {
                                Iterator<TLRPC$Document> it4 = next3.covers.iterator();
                                while (true) {
                                    if (!it4.hasNext()) {
                                        break;
                                    }
                                    TLRPC$Document next4 = it4.next();
                                    if (next4.id == documentId) {
                                        tLRPC$Document = next4;
                                        break;
                                    }
                                }
                            }
                            if (tLRPC$Document != null) {
                                break;
                            }
                            ArrayList<TLRPC$Document> arrayList4 = null;
                            if (next3 instanceof TLRPC$TL_stickerSetFullCovered) {
                                arrayList4 = ((TLRPC$TL_stickerSetFullCovered) next3).documents;
                            } else if ((next3 instanceof TLRPC$TL_stickerSetNoCovered) && next3.set != null) {
                                TLRPC$TL_inputStickerSetID tLRPC$TL_inputStickerSetID = new TLRPC$TL_inputStickerSetID();
                                tLRPC$TL_inputStickerSetID.id = next3.set.id;
                                TLRPC$TL_messages_stickerSet stickerSet = MediaDataController.getInstance(i).getStickerSet(tLRPC$TL_inputStickerSetID, true);
                                if (stickerSet != null && (arrayList = stickerSet.documents) != null) {
                                    arrayList4 = arrayList;
                                }
                            }
                            if (arrayList4 != null && !arrayList4.isEmpty()) {
                                Iterator<TLRPC$Document> it5 = arrayList4.iterator();
                                while (true) {
                                    if (it5.hasNext()) {
                                        TLRPC$Document next5 = it5.next();
                                        if (next5.id == documentId) {
                                            tLRPC$Document = next5;
                                            continue;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (tLRPC$Document != null) {
                                break;
                            }
                        }
                    }
                    if (tLRPC$Document == null || !MessageObject.isFreeEmoji(tLRPC$Document)) {
                        BulletinFactory.of(baseFragment).createEmojiBulletin(tLRPC$Document, AndroidUtilities.replaceTags(LocaleController.getString("UnlockPremiumEmojiHint", R.string.UnlockPremiumEmojiHint)), LocaleController.getString("PremiumMore", R.string.PremiumMore), new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda32
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.lambda$checkPremiumAnimatedEmoji$36(BaseFragment.this);
                            }
                        }).show();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static /* synthetic */ void lambda$checkPremiumAnimatedEmoji$36(BaseFragment baseFragment) {
        if (baseFragment != null) {
            new PremiumFeatureBottomSheet(baseFragment, 11, false).show();
        } else if (baseFragment.getContext() instanceof LaunchActivity) {
            ((LaunchActivity) baseFragment.getContext()).lambda$runLinkRequest$83(new PremiumPreviewFragment(null));
        }
    }

    public void showCaptionLimitBulletin() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || !ChatObject.isChannelAndNotMegaGroup(chatActivity.getCurrentChat())) {
            return;
        }
        BulletinFactory.of(this.parentFragment).createCaptionLimitBulletin(MessagesController.getInstance(this.currentAccount).captionLengthLimitPremium, new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda34
            @Override // java.lang.Runnable
            public final void run() {
                ChatActivityEnterView.this.lambda$showCaptionLimitBulletin$37();
            }
        }).show();
    }

    public /* synthetic */ void lambda$showCaptionLimitBulletin$37() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            chatActivity.presentFragment(new PremiumPreviewFragment("caption_limit"));
        }
    }

    public void doneEditingMessage() {
        MessagePreviewParams messagePreviewParams;
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
            if (MessagesController.getInstance(this.currentAccount).premiumLocked || MessagesController.getInstance(this.currentAccount).captionLengthLimitPremium <= this.codePointCount) {
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
                AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda39
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActivityEnterView.this.lambda$doneEditingMessage$38();
                    }
                }, 200L);
            }
        }
        EditTextCaption editTextCaption = this.messageEditText;
        String text = editTextCaption == null ? "" : editTextCaption.getText();
        MessageObject messageObject = this.editingMessageObject;
        if (messageObject == null || messageObject.type != 19) {
            text = AndroidUtilities.getTrimmedString(text);
        }
        CharSequence[] charSequenceArr = {text};
        if (TextUtils.isEmpty(charSequenceArr[0])) {
            TLRPC$MessageMedia tLRPC$MessageMedia = this.editingMessageObject.messageOwner.media;
            if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) || (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaEmpty) || tLRPC$MessageMedia == null) {
                AndroidUtilities.shakeViewSpring(this.messageEditText, -3.0f);
                BotWebViewVibrationEffect.APP_ERROR.vibrate();
                return;
            }
        }
        ArrayList<TLRPC$MessageEntity> entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, supportsSendingNewEntities());
        if (!TextUtils.equals(charSequenceArr[0], this.editingMessageObject.messageText) || ((entities != null && !entities.isEmpty()) || !this.editingMessageObject.messageOwner.entities.isEmpty() || (this.editingMessageObject.messageOwner.media instanceof TLRPC$TL_messageMediaWebPage))) {
            MessageObject messageObject2 = this.editingMessageObject;
            messageObject2.editingMessage = charSequenceArr[0];
            messageObject2.editingMessageEntities = entities;
            messageObject2.editingMessageSearchWebPage = this.messageWebPageSearch;
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity != null && (messagePreviewParams = chatActivity.messagePreviewParams) != null) {
                if (chatActivity.foundWebPage instanceof TLRPC$TL_webPagePending) {
                    messageObject2.editingMessageSearchWebPage = true;
                } else if (messagePreviewParams.webpage != null) {
                    messageObject2.editingMessageSearchWebPage = false;
                    messageObject2.messageOwner.media = new TLRPC$TL_messageMediaWebPage();
                    TLRPC$MessageMedia tLRPC$MessageMedia2 = this.editingMessageObject.messageOwner.media;
                    tLRPC$MessageMedia2.flags |= LiteMode.FLAG_CALLS_ANIMATIONS;
                    tLRPC$MessageMedia2.webpage = this.parentFragment.messagePreviewParams.webpage;
                } else {
                    messageObject2.editingMessageSearchWebPage = false;
                    int i = messageObject2.type;
                    if (i == 0 || i == 19) {
                        messageObject2.messageOwner.media = new TLRPC$TL_messageMediaEmpty();
                        this.editingMessageObject.messageOwner.media.flags |= LiteMode.FLAG_CALLS_ANIMATIONS;
                    }
                }
                TLRPC$Message tLRPC$Message = this.editingMessageObject.messageOwner;
                MessagePreviewParams messagePreviewParams2 = this.parentFragment.messagePreviewParams;
                tLRPC$Message.invert_media = messagePreviewParams2.webpageTop;
                if (messagePreviewParams2.hasMedia) {
                    TLRPC$MessageMedia tLRPC$MessageMedia3 = tLRPC$Message.media;
                    if (tLRPC$MessageMedia3 instanceof TLRPC$TL_messageMediaWebPage) {
                        boolean z = messagePreviewParams2.webpageSmall;
                        tLRPC$MessageMedia3.force_small_media = z;
                        tLRPC$MessageMedia3.force_large_media = true ^ z;
                    }
                }
            }
            SendMessagesHelper sendMessagesHelper = SendMessagesHelper.getInstance(this.currentAccount);
            MessageObject messageObject3 = this.editingMessageObject;
            sendMessagesHelper.editMessage(messageObject3, null, null, null, null, null, false, messageObject3.hasMediaSpoilers(), null);
        }
        setEditingMessageObject(null, false);
    }

    public /* synthetic */ void lambda$doneEditingMessage$38() {
        this.waitingForKeyboardOpenAfterAnimation = false;
        openKeyboardInternal();
    }

    public boolean processSendingText(CharSequence charSequence, boolean z, int i) {
        int i2;
        int i3;
        int i4;
        int i5;
        MessageObject.SendAnimationData sendAnimationData;
        MessagePreviewParams messagePreviewParams;
        MessagePreviewParams messagePreviewParams2;
        MessagePreviewParams messagePreviewParams3;
        ChatActivity chatActivity;
        ChatActivity chatActivity2;
        ChatActivity.ReplyQuote replyQuote = this.replyingQuote;
        if (replyQuote != null && (chatActivity2 = this.parentFragment) != null && replyQuote.outdated) {
            chatActivity2.showQuoteMessageUpdate();
            return false;
        }
        int i6 = 1;
        int[] iArr = new int[1];
        CharSequence charSequence2 = charSequence;
        Emoji.parseEmojis(charSequence2, iArr);
        boolean z2 = iArr[0] > 0;
        if (!z2) {
            charSequence2 = AndroidUtilities.getTrimmedString(charSequence);
        }
        boolean supportsSendingNewEntities = supportsSendingNewEntities();
        int i7 = this.accountInstance.getMessagesController().maxMessageLength;
        if (charSequence2.length() == 0) {
            return false;
        }
        if (this.delegate != null && (chatActivity = this.parentFragment) != null) {
            if ((i != 0) == chatActivity.isInScheduleMode()) {
                this.delegate.prepareMessageSending();
            }
        }
        int i8 = 0;
        while (true) {
            int i9 = i8 + i7;
            if (charSequence2.length() > i9) {
                int i10 = i9 - 1;
                i3 = -1;
                i4 = -1;
                i5 = -1;
                for (int i11 = 0; i10 > i8 && i11 < 300; i11++) {
                    char charAt = charSequence2.charAt(i10);
                    char charAt2 = i10 > 0 ? charSequence2.charAt(i10 - 1) : ' ';
                    if (charAt == '\n' && charAt2 == '\n') {
                        i2 = i10;
                        break;
                    }
                    if (charAt == '\n') {
                        i5 = i10;
                    } else if (i3 < 0 && Character.isWhitespace(charAt) && charAt2 == '.') {
                        i3 = i10;
                    } else if (i4 < 0 && Character.isWhitespace(charAt)) {
                        i4 = i10;
                    }
                    i10--;
                }
                i2 = -1;
            } else {
                i2 = -1;
                i3 = -1;
                i4 = -1;
                i5 = -1;
            }
            int min = Math.min(i9, charSequence2.length());
            if (i2 > 0) {
                min = i2;
            } else if (i5 > 0) {
                min = i5;
            } else if (i3 > 0) {
                min = i3;
            } else if (i4 > 0) {
                min = i4;
            }
            CharSequence subSequence = charSequence2.subSequence(i8, min);
            if (!z2) {
                subSequence = AndroidUtilities.getTrimmedString(subSequence);
            }
            CharSequence[] charSequenceArr = new CharSequence[i6];
            charSequenceArr[0] = subSequence;
            ArrayList<TLRPC$MessageEntity> entities = MediaDataController.getInstance(this.currentAccount).getEntities(charSequenceArr, supportsSendingNewEntities);
            if (this.delegate.hasForwardingMessages()) {
                sendAnimationData = null;
            } else {
                MessageObject.SendAnimationData sendAnimationData2 = new MessageObject.SendAnimationData();
                float dp = AndroidUtilities.dp(22.0f);
                sendAnimationData2.height = dp;
                sendAnimationData2.width = dp;
                EditTextCaption editTextCaption = this.messageEditText;
                if (editTextCaption != null) {
                    editTextCaption.getLocationInWindow(this.location);
                    sendAnimationData2.x = this.location[0] + AndroidUtilities.dp(11.0f);
                    sendAnimationData2.y = this.location[i6] + AndroidUtilities.dp(19.0f);
                } else {
                    sendAnimationData2.x = AndroidUtilities.dp(59.0f);
                    sendAnimationData2.y = AndroidUtilities.displaySize.y - AndroidUtilities.dp(19.0f);
                }
                sendAnimationData = sendAnimationData2;
            }
            SendMessagesHelper.SendMessageParams of = SendMessagesHelper.SendMessageParams.of(charSequenceArr[0].toString(), this.dialog_id, this.replyingMessageObject, getThreadMessage(), this.messageWebPage, this.messageWebPageSearch, entities, null, null, z, i, sendAnimationData, SendMessagesHelper.checkUpdateStickersOrder(charSequence2));
            applyStoryToSendMessageParams(of);
            ChatActivity chatActivity3 = this.parentFragment;
            of.invert_media = (chatActivity3 == null || (messagePreviewParams3 = chatActivity3.messagePreviewParams) == null || !messagePreviewParams3.webpageTop) ? false : true;
            if (this.messageWebPage != null) {
                TLRPC$TL_messageMediaWebPage tLRPC$TL_messageMediaWebPage = new TLRPC$TL_messageMediaWebPage();
                of.mediaWebPage = tLRPC$TL_messageMediaWebPage;
                tLRPC$TL_messageMediaWebPage.webpage = this.messageWebPage;
                ChatActivity chatActivity4 = this.parentFragment;
                tLRPC$TL_messageMediaWebPage.force_large_media = (chatActivity4 == null || (messagePreviewParams2 = chatActivity4.messagePreviewParams) == null || messagePreviewParams2.webpageSmall) ? false : true;
                tLRPC$TL_messageMediaWebPage.force_small_media = (chatActivity4 == null || (messagePreviewParams = chatActivity4.messagePreviewParams) == null || !messagePreviewParams.webpageSmall) ? false : true;
                if (chatActivity4 != null) {
                    chatActivity4.editingMessageObject = null;
                    chatActivity4.foundWebPage = null;
                    MessagePreviewParams messagePreviewParams4 = chatActivity4.messagePreviewParams;
                    if (messagePreviewParams4 != null) {
                        messagePreviewParams4.updateLink(this.currentAccount, null, "", null, null, null);
                    }
                    setWebPage(null, true);
                    this.parentFragment.fallbackFieldPanel();
                }
            }
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(of);
            i8 = min + 1;
            if (min == charSequence2.length()) {
                return true;
            }
            i6 = 1;
        }
    }

    private void applyStoryToSendMessageParams(SendMessagesHelper.SendMessageParams sendMessageParams) {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            sendMessageParams.replyToStoryItem = chatActivityEnterViewDelegate.getReplyToStory();
            sendMessageParams.replyQuote = this.delegate.getReplyQuote();
        }
    }

    private boolean supportsSendingNewEntities() {
        ChatActivity chatActivity = this.parentFragment;
        TLRPC$EncryptedChat currentEncryptedChat = chatActivity != null ? chatActivity.getCurrentEncryptedChat() : null;
        return currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(currentEncryptedChat.layer) >= 101;
    }

    public void checkSendButton(boolean z) {
        int themedColor;
        int i;
        ImageView imageView;
        ImageView imageView2;
        ImageView imageView3;
        int i2;
        if (this.editingMessageObject != null || this.recordingAudioVideo) {
            return;
        }
        boolean z2 = this.isPaused ? false : z;
        EditTextCaption editTextCaption = this.messageEditText;
        String trimmedString = editTextCaption == null ? "" : AndroidUtilities.getTrimmedString(editTextCaption.getText());
        int i3 = this.slowModeTimer;
        float f = 1.0f;
        if (i3 > 0 && i3 != Integer.MAX_VALUE && !isInScheduleMode()) {
            if (this.slowModeButton.getVisibility() != 0) {
                if (z2) {
                    if (this.runningAnimationType == 5) {
                        return;
                    }
                    AnimatorSet animatorSet = this.runningAnimation;
                    if (animatorSet != null) {
                        animatorSet.cancel();
                        this.runningAnimation = null;
                    }
                    AnimatorSet animatorSet2 = this.runningAnimation2;
                    if (animatorSet2 != null) {
                        animatorSet2.cancel();
                        this.runningAnimation2 = null;
                    }
                    if (this.attachLayout != null) {
                        this.runningAnimation2 = new AnimatorSet();
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 0.0f));
                        arrayList.add(ObjectAnimator.ofFloat(this.attachLayout, View.SCALE_X, 0.0f));
                        this.scheduleButtonHidden = false;
                        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
                        boolean z3 = chatActivityEnterViewDelegate != null && chatActivityEnterViewDelegate.hasScheduledMessages();
                        if (z3) {
                            createScheduledButton();
                        }
                        ImageView imageView4 = this.scheduledButton;
                        if (imageView4 != null) {
                            imageView4.setScaleY(1.0f);
                            if (z3) {
                                this.scheduledButton.setVisibility(0);
                                this.scheduledButton.setTag(1);
                                this.scheduledButton.setPivotX(AndroidUtilities.dp(48.0f));
                                ImageView imageView5 = this.scheduledButton;
                                Property property = View.TRANSLATION_X;
                                float[] fArr = new float[1];
                                ImageView imageView6 = this.botButton;
                                int dp = AndroidUtilities.dp((imageView6 == null || imageView6.getVisibility() != 0) ? 48.0f : 96.0f);
                                ImageView imageView7 = this.giftButton;
                                fArr[0] = dp - AndroidUtilities.dp((imageView7 == null || imageView7.getVisibility() != 0) ? 0.0f : 48.0f);
                                arrayList.add(ObjectAnimator.ofFloat(imageView5, property, fArr));
                                arrayList.add(ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 1.0f));
                                arrayList.add(ObjectAnimator.ofFloat(this.scheduledButton, View.SCALE_X, 1.0f));
                            } else {
                                ImageView imageView8 = this.scheduledButton;
                                ImageView imageView9 = this.botButton;
                                int dp2 = AndroidUtilities.dp((imageView9 == null || imageView9.getVisibility() != 0) ? 48.0f : 96.0f);
                                ImageView imageView10 = this.giftButton;
                                imageView8.setTranslationX(dp2 - AndroidUtilities.dp((imageView10 == null || imageView10.getVisibility() != 0) ? 0.0f : 48.0f));
                                this.scheduledButton.setAlpha(1.0f);
                                this.scheduledButton.setScaleX(1.0f);
                            }
                        }
                        this.runningAnimation2.playTogether(arrayList);
                        this.runningAnimation2.setDuration(100L);
                        this.runningAnimation2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.48
                            {
                                ChatActivityEnterView.this = this;
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                                    ChatActivityEnterView.this.attachLayout.setVisibility(8);
                                    ChatActivityEnterView.this.runningAnimation2 = null;
                                }
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationCancel(Animator animator) {
                                if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
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
                    ArrayList arrayList2 = new ArrayList();
                    if (this.audioVideoButtonContainer.getVisibility() == 0) {
                        arrayList2.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, 0.1f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, 0.1f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 0.0f));
                    }
                    ImageView imageView11 = this.expandStickersButton;
                    if (imageView11 != null && imageView11.getVisibility() == 0) {
                        arrayList2.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_X, 0.1f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_Y, 0.1f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.ALPHA, 0.0f));
                    }
                    if (this.sendButton.getVisibility() == 0) {
                        arrayList2.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, 0.1f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, 0.1f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, 0.0f));
                    }
                    if (this.cancelBotButton.getVisibility() == 0) {
                        arrayList2.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, 0.1f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, 0.1f));
                        arrayList2.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, 0.0f));
                    }
                    arrayList2.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_X, 1.0f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_Y, 1.0f));
                    arrayList2.add(ObjectAnimator.ofFloat(this.slowModeButton, View.ALPHA, 1.0f));
                    setSlowModeButtonVisible(true);
                    this.runningAnimation.playTogether(arrayList2);
                    this.runningAnimation.setDuration(150L);
                    this.runningAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.49
                        {
                            ChatActivityEnterView.this = this;
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

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
                            if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                                ChatActivityEnterView.this.runningAnimation = null;
                            }
                        }
                    });
                    this.runningAnimation.start();
                    return;
                }
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
                ImageView imageView12 = this.expandStickersButton;
                if (imageView12 == null || imageView12.getVisibility() != 0) {
                    i2 = 8;
                } else {
                    this.expandStickersButton.setScaleX(0.1f);
                    this.expandStickersButton.setScaleY(0.1f);
                    this.expandStickersButton.setAlpha(0.0f);
                    i2 = 8;
                    this.expandStickersButton.setVisibility(8);
                }
                LinearLayout linearLayout = this.attachLayout;
                if (linearLayout != null) {
                    linearLayout.setVisibility(i2);
                    if (this.delegate != null && getVisibility() == 0) {
                        this.delegate.onAttachButtonHidden();
                    }
                    updateFieldRight(0);
                }
                this.scheduleButtonHidden = false;
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
                boolean z4 = chatActivityEnterViewDelegate2 != null && chatActivityEnterViewDelegate2.hasScheduledMessages();
                if (z4) {
                    createScheduledButton();
                }
                ImageView imageView13 = this.scheduledButton;
                if (imageView13 != null) {
                    if (z4) {
                        imageView13.setVisibility(0);
                        this.scheduledButton.setTag(1);
                    }
                    ImageView imageView14 = this.scheduledButton;
                    ImageView imageView15 = this.botButton;
                    int dp3 = AndroidUtilities.dp((imageView15 == null || imageView15.getVisibility() != 0) ? 48.0f : 96.0f);
                    ImageView imageView16 = this.giftButton;
                    imageView14.setTranslationX(dp3 - AndroidUtilities.dp((imageView16 == null || imageView16.getVisibility() != 0) ? 0.0f : 48.0f));
                    this.scheduledButton.setAlpha(1.0f);
                    this.scheduledButton.setScaleX(1.0f);
                    this.scheduledButton.setScaleY(1.0f);
                }
            }
        } else if (trimmedString.length() > 0 || this.forceShowSendButton || this.audioToSend != null || this.videoToSendMessageObject != null || (this.slowModeTimer == Integer.MAX_VALUE && !isInScheduleMode())) {
            EditTextCaption editTextCaption2 = this.messageEditText;
            final String caption = editTextCaption2 == null ? null : editTextCaption2.getCaption();
            boolean z5 = caption != null && (this.sendButton.getVisibility() == 0 || ((imageView2 = this.expandStickersButton) != null && imageView2.getVisibility() == 0));
            boolean z6 = caption == null && (this.cancelBotButton.getVisibility() == 0 || ((imageView = this.expandStickersButton) != null && imageView.getVisibility() == 0));
            if (this.slowModeTimer == Integer.MAX_VALUE && !isInScheduleMode()) {
                themedColor = getThemedColor(Theme.key_chat_messagePanelIcons);
            } else {
                themedColor = getThemedColor(Theme.key_chat_messagePanelSend);
            }
            if (themedColor != this.sendButtonBackgroundColor) {
                this.sendButtonBackgroundColor = themedColor;
                Theme.setSelectorDrawableColor(this.sendButton.getBackground(), Color.argb(24, Color.red(themedColor), Color.green(themedColor), Color.blue(themedColor)), true);
            }
            if (this.audioVideoButtonContainer.getVisibility() == 0 || this.slowModeButton.getVisibility() == 0 || z5 || z6) {
                if (z2) {
                    int i4 = this.runningAnimationType;
                    if (i4 == 1 && caption == null) {
                        return;
                    }
                    if (i4 != 3 || caption == null) {
                        AnimatorSet animatorSet3 = this.runningAnimation;
                        if (animatorSet3 != null) {
                            animatorSet3.cancel();
                            this.runningAnimation = null;
                        }
                        AnimatorSet animatorSet4 = this.runningAnimation2;
                        if (animatorSet4 != null) {
                            animatorSet4.cancel();
                            this.runningAnimation2 = null;
                        }
                        if (this.attachLayout != null) {
                            this.runningAnimation2 = new AnimatorSet();
                            ArrayList arrayList3 = new ArrayList();
                            arrayList3.add(ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 0.0f));
                            arrayList3.add(ObjectAnimator.ofFloat(this.attachLayout, View.SCALE_X, 0.0f));
                            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate3 = this.delegate;
                            final boolean z7 = chatActivityEnterViewDelegate3 != null && chatActivityEnterViewDelegate3.hasScheduledMessages();
                            this.scheduleButtonHidden = true;
                            ImageView imageView17 = this.scheduledButton;
                            if (imageView17 != null) {
                                imageView17.setScaleY(1.0f);
                                if (z7) {
                                    this.scheduledButton.setTag(null);
                                    arrayList3.add(ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 0.0f));
                                    arrayList3.add(ObjectAnimator.ofFloat(this.scheduledButton, View.SCALE_X, 0.0f));
                                    ImageView imageView18 = this.scheduledButton;
                                    Property property2 = View.TRANSLATION_X;
                                    float[] fArr2 = new float[1];
                                    ImageView imageView19 = this.botButton;
                                    int dp4 = AndroidUtilities.dp((imageView19 == null || imageView19.getVisibility() != 0) ? 48.0f : 96.0f);
                                    ImageView imageView20 = this.giftButton;
                                    fArr2[0] = dp4 - AndroidUtilities.dp((imageView20 == null || imageView20.getVisibility() != 0) ? 0.0f : 48.0f);
                                    arrayList3.add(ObjectAnimator.ofFloat(imageView18, property2, fArr2));
                                } else {
                                    this.scheduledButton.setAlpha(0.0f);
                                    this.scheduledButton.setScaleX(0.0f);
                                    ImageView imageView21 = this.scheduledButton;
                                    ImageView imageView22 = this.botButton;
                                    int dp5 = AndroidUtilities.dp((imageView22 == null || imageView22.getVisibility() != 0) ? 48.0f : 96.0f);
                                    ImageView imageView23 = this.giftButton;
                                    imageView21.setTranslationX(dp5 - AndroidUtilities.dp((imageView23 == null || imageView23.getVisibility() != 0) ? 0.0f : 48.0f));
                                }
                            }
                            this.runningAnimation2.playTogether(arrayList3);
                            this.runningAnimation2.setDuration(100L);
                            this.runningAnimation2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.50
                                {
                                    ChatActivityEnterView.this = this;
                                }

                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationEnd(Animator animator) {
                                    if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                                        ChatActivityEnterView.this.attachLayout.setVisibility(8);
                                        if (z7 && ChatActivityEnterView.this.scheduledButton != null) {
                                            ChatActivityEnterView.this.scheduledButton.setVisibility(8);
                                        }
                                        ChatActivityEnterView.this.runningAnimation2 = null;
                                    }
                                }

                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationCancel(Animator animator) {
                                    if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
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
                        ArrayList arrayList4 = new ArrayList();
                        if (this.audioVideoButtonContainer.getVisibility() == 0) {
                            arrayList4.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, 0.1f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, 0.1f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 0.0f));
                        }
                        ImageView imageView24 = this.expandStickersButton;
                        if (imageView24 != null && imageView24.getVisibility() == 0) {
                            arrayList4.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_X, 0.1f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_Y, 0.1f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.ALPHA, 0.0f));
                        }
                        if (this.slowModeButton.getVisibility() == 0) {
                            arrayList4.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_X, 0.1f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_Y, 0.1f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.slowModeButton, View.ALPHA, 0.0f));
                        }
                        if (z5) {
                            arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, 0.1f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, 0.1f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, 0.0f));
                        } else if (z6) {
                            arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, 0.1f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, 0.1f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, 0.0f));
                        }
                        if (caption != null) {
                            this.runningAnimationType = 3;
                            arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, 1.0f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, 1.0f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, 1.0f));
                            this.cancelBotButton.setVisibility(0);
                        } else {
                            this.runningAnimationType = 1;
                            arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, 1.0f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, 1.0f));
                            arrayList4.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, 1.0f));
                            this.sendButton.setVisibility(0);
                        }
                        this.runningAnimation.playTogether(arrayList4);
                        this.runningAnimation.setDuration(150L);
                        this.runningAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.51
                            {
                                ChatActivityEnterView.this = this;
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

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationCancel(Animator animator) {
                                if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                                    ChatActivityEnterView.this.runningAnimation = null;
                                }
                            }
                        });
                        this.runningAnimation.start();
                        return;
                    }
                    return;
                }
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
                ImageView imageView25 = this.expandStickersButton;
                if (imageView25 == null || imageView25.getVisibility() != 0) {
                    i = 8;
                } else {
                    this.expandStickersButton.setScaleX(0.1f);
                    this.expandStickersButton.setScaleY(0.1f);
                    this.expandStickersButton.setAlpha(0.0f);
                    i = 8;
                    this.expandStickersButton.setVisibility(8);
                }
                LinearLayout linearLayout2 = this.attachLayout;
                if (linearLayout2 != null) {
                    linearLayout2.setVisibility(i);
                    if (this.delegate != null && getVisibility() == 0) {
                        this.delegate.onAttachButtonHidden();
                    }
                    updateFieldRight(0);
                }
                this.scheduleButtonHidden = true;
                if (this.scheduledButton != null) {
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate4 = this.delegate;
                    if (chatActivityEnterViewDelegate4 != null && chatActivityEnterViewDelegate4.hasScheduledMessages()) {
                        this.scheduledButton.setVisibility(8);
                        this.scheduledButton.setTag(null);
                    }
                    this.scheduledButton.setAlpha(0.0f);
                    this.scheduledButton.setScaleX(0.0f);
                    this.scheduledButton.setScaleY(1.0f);
                    ImageView imageView26 = this.scheduledButton;
                    ImageView imageView27 = this.botButton;
                    int dp6 = AndroidUtilities.dp((imageView27 == null || imageView27.getVisibility() != 0) ? 48.0f : 96.0f);
                    ImageView imageView28 = this.giftButton;
                    imageView26.setTranslationX(dp6 - AndroidUtilities.dp((imageView28 == null || imageView28.getVisibility() != 0) ? 0.0f : 48.0f));
                }
            }
        } else if (this.emojiView != null && this.emojiViewVisible && ((this.stickersTabOpen || (this.emojiTabOpen && this.searchingType == 2)) && !AndroidUtilities.isInMultiwindow)) {
            if (z2) {
                if (this.runningAnimationType == 4) {
                    return;
                }
                AnimatorSet animatorSet5 = this.runningAnimation;
                if (animatorSet5 != null) {
                    animatorSet5.cancel();
                    this.runningAnimation = null;
                }
                AnimatorSet animatorSet6 = this.runningAnimation2;
                if (animatorSet6 != null) {
                    animatorSet6.cancel();
                    this.runningAnimation2 = null;
                }
                LinearLayout linearLayout3 = this.attachLayout;
                if (linearLayout3 != null && this.recordInterfaceState == 0) {
                    linearLayout3.setVisibility(0);
                    this.runningAnimation2 = new AnimatorSet();
                    ArrayList arrayList5 = new ArrayList();
                    arrayList5.add(ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 1.0f));
                    arrayList5.add(ObjectAnimator.ofFloat(this.attachLayout, View.SCALE_X, 1.0f));
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate5 = this.delegate;
                    boolean z8 = chatActivityEnterViewDelegate5 != null && chatActivityEnterViewDelegate5.hasScheduledMessages();
                    this.scheduleButtonHidden = false;
                    if (z8) {
                        createScheduledButton();
                    }
                    ImageView imageView29 = this.scheduledButton;
                    if (imageView29 != null) {
                        imageView29.setScaleY(1.0f);
                        if (z8) {
                            this.scheduledButton.setVisibility(0);
                            this.scheduledButton.setTag(1);
                            this.scheduledButton.setPivotX(AndroidUtilities.dp(48.0f));
                            arrayList5.add(ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 1.0f));
                            arrayList5.add(ObjectAnimator.ofFloat(this.scheduledButton, View.SCALE_X, 1.0f));
                            arrayList5.add(ObjectAnimator.ofFloat(this.scheduledButton, View.TRANSLATION_X, 0.0f));
                        } else {
                            this.scheduledButton.setAlpha(1.0f);
                            this.scheduledButton.setScaleX(1.0f);
                            this.scheduledButton.setTranslationX(0.0f);
                        }
                    }
                    this.runningAnimation2.playTogether(arrayList5);
                    this.runningAnimation2.setDuration(100L);
                    this.runningAnimation2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.52
                        {
                            ChatActivityEnterView.this = this;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                                ChatActivityEnterView.this.runningAnimation2 = null;
                            }
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
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
                arrayList6.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_X, 1.0f));
                arrayList6.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_Y, 1.0f));
                arrayList6.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.ALPHA, 1.0f));
                if (this.cancelBotButton.getVisibility() == 0) {
                    arrayList6.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, 0.0f));
                } else if (this.audioVideoButtonContainer.getVisibility() == 0) {
                    arrayList6.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 0.0f));
                } else if (this.slowModeButton.getVisibility() == 0) {
                    arrayList6.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_X, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_Y, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.slowModeButton, View.ALPHA, 0.0f));
                } else {
                    arrayList6.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, 0.1f));
                    arrayList6.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, 0.0f));
                }
                this.runningAnimation.playTogether(arrayList6);
                this.runningAnimation.setDuration(250L);
                this.runningAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.53
                    {
                        ChatActivityEnterView.this = this;
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

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                            ChatActivityEnterView.this.runningAnimation = null;
                        }
                    }
                });
                this.runningAnimation.start();
                return;
            }
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
            boolean z9 = chatActivityEnterViewDelegate6 != null && chatActivityEnterViewDelegate6.hasScheduledMessages();
            if (z9) {
                createScheduledButton();
            }
            ImageView imageView30 = this.scheduledButton;
            if (imageView30 != null) {
                if (z9) {
                    imageView30.setVisibility(0);
                    this.scheduledButton.setTag(1);
                }
                this.scheduledButton.setAlpha(1.0f);
                this.scheduledButton.setScaleX(1.0f);
                this.scheduledButton.setScaleY(1.0f);
                this.scheduledButton.setTranslationX(0.0f);
            }
        } else if (this.sendButton.getVisibility() == 0 || this.cancelBotButton.getVisibility() == 0 || (((imageView3 = this.expandStickersButton) != null && imageView3.getVisibility() == 0) || this.slowModeButton.getVisibility() == 0)) {
            if (z2) {
                if (this.runningAnimationType == 2) {
                    return;
                }
                AnimatorSet animatorSet7 = this.runningAnimation;
                if (animatorSet7 != null) {
                    animatorSet7.cancel();
                    this.runningAnimation = null;
                }
                AnimatorSet animatorSet8 = this.runningAnimation2;
                if (animatorSet8 != null) {
                    animatorSet8.cancel();
                    this.runningAnimation2 = null;
                }
                LinearLayout linearLayout4 = this.attachLayout;
                if (linearLayout4 != null) {
                    if (linearLayout4.getVisibility() != 0) {
                        this.attachLayout.setVisibility(0);
                        this.attachLayoutAlpha = 0.0f;
                        updateAttachLayoutParams();
                        this.attachLayout.setScaleX(0.0f);
                    }
                    this.runningAnimation2 = new AnimatorSet();
                    ArrayList arrayList7 = new ArrayList();
                    arrayList7.add(ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 1.0f));
                    arrayList7.add(ObjectAnimator.ofFloat(this.attachLayout, View.SCALE_X, 1.0f));
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate7 = this.delegate;
                    boolean z10 = chatActivityEnterViewDelegate7 != null && chatActivityEnterViewDelegate7.hasScheduledMessages();
                    this.scheduleButtonHidden = false;
                    if (z10) {
                        createScheduledButton();
                    }
                    ImageView imageView31 = this.scheduledButton;
                    if (imageView31 != null) {
                        if (z10) {
                            imageView31.setVisibility(0);
                            this.scheduledButton.setTag(1);
                            this.scheduledButton.setPivotX(AndroidUtilities.dp(48.0f));
                            arrayList7.add(ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 1.0f));
                            arrayList7.add(ObjectAnimator.ofFloat(this.scheduledButton, View.SCALE_X, 1.0f));
                            ImageView imageView32 = this.scheduledButton;
                            Property property3 = View.TRANSLATION_X;
                            float[] fArr3 = new float[1];
                            ImageView imageView33 = this.giftButton;
                            fArr3[0] = (imageView33 == null || imageView33.getVisibility() != 0) ? 0.0f : -AndroidUtilities.dp(48.0f);
                            arrayList7.add(ObjectAnimator.ofFloat(imageView32, property3, fArr3));
                        } else {
                            imageView31.setAlpha(1.0f);
                            this.scheduledButton.setScaleX(1.0f);
                            this.scheduledButton.setScaleY(1.0f);
                            this.scheduledButton.setTranslationX(0.0f);
                        }
                    }
                    this.runningAnimation2.playTogether(arrayList7);
                    this.runningAnimation2.setDuration(100L);
                    this.runningAnimation2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.54
                        {
                            ChatActivityEnterView.this = this;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            if (animator.equals(ChatActivityEnterView.this.runningAnimation2)) {
                                ChatActivityEnterView.this.runningAnimation2 = null;
                            }
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animator) {
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
                ArrayList arrayList8 = new ArrayList();
                arrayList8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, 1.0f));
                arrayList8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, 1.0f));
                ChatActivity chatActivity = this.parentFragment;
                TLRPC$Chat currentChat = chatActivity != null ? chatActivity.getCurrentChat() : null;
                ChatActivity chatActivity2 = this.parentFragment;
                TLRPC$UserFull currentUserInfo = chatActivity2 == null ? this.userInfo : chatActivity2.getCurrentUserInfo();
                if (currentChat == null ? !(currentUserInfo == null || !currentUserInfo.voice_messages_forbidden) : !(ChatObject.canSendVoice(currentChat) || ChatObject.canSendRoundVideo(currentChat))) {
                    f = 0.5f;
                }
                arrayList8.add(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, f));
                if (this.cancelBotButton.getVisibility() == 0) {
                    arrayList8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_X, 0.1f));
                    arrayList8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.SCALE_Y, 0.1f));
                    arrayList8.add(ObjectAnimator.ofFloat(this.cancelBotButton, View.ALPHA, 0.0f));
                } else {
                    ImageView imageView34 = this.expandStickersButton;
                    if (imageView34 != null && imageView34.getVisibility() == 0) {
                        arrayList8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_X, 0.1f));
                        arrayList8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.SCALE_Y, 0.1f));
                        arrayList8.add(ObjectAnimator.ofFloat(this.expandStickersButton, View.ALPHA, 0.0f));
                    } else if (this.slowModeButton.getVisibility() == 0) {
                        arrayList8.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_X, 0.1f));
                        arrayList8.add(ObjectAnimator.ofFloat(this.slowModeButton, View.SCALE_Y, 0.1f));
                        arrayList8.add(ObjectAnimator.ofFloat(this.slowModeButton, View.ALPHA, 0.0f));
                    } else {
                        arrayList8.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_X, 0.1f));
                        arrayList8.add(ObjectAnimator.ofFloat(this.sendButton, View.SCALE_Y, 0.1f));
                        arrayList8.add(ObjectAnimator.ofFloat(this.sendButton, View.ALPHA, 0.0f));
                    }
                }
                this.runningAnimation.playTogether(arrayList8);
                this.runningAnimation.setDuration(150L);
                this.runningAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.55
                    {
                        ChatActivityEnterView.this = this;
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

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        if (animator.equals(ChatActivityEnterView.this.runningAnimation)) {
                            ChatActivityEnterView.this.runningAnimation = null;
                        }
                    }
                });
                this.runningAnimation.start();
                return;
            }
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
            ImageView imageView35 = this.expandStickersButton;
            if (imageView35 != null) {
                imageView35.setScaleX(0.1f);
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
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate8 = this.delegate;
            if (chatActivityEnterViewDelegate8 != null && chatActivityEnterViewDelegate8.hasScheduledMessages()) {
                createScheduledButton();
            }
            if (this.scheduledButton != null) {
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate9 = this.delegate;
                if (chatActivityEnterViewDelegate9 != null && chatActivityEnterViewDelegate9.hasScheduledMessages()) {
                    this.scheduledButton.setVisibility(0);
                    this.scheduledButton.setTag(1);
                }
                this.scheduledButton.setAlpha(1.0f);
                this.scheduledButton.setScaleX(1.0f);
                this.scheduledButton.setScaleY(1.0f);
                this.scheduledButton.setTranslationX(0.0f);
            }
        }
    }

    public void setSlowModeButtonVisible(boolean z) {
        this.slowModeButton.setVisibility(z ? 0 : 8);
        int dp = z ? AndroidUtilities.dp(16.0f) : 0;
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null || editTextCaption.getPaddingRight() == dp) {
            return;
        }
        this.messageEditText.setPadding(0, AndroidUtilities.dp(11.0f), dp, AndroidUtilities.dp(12.0f));
    }

    private void updateFieldRight(int i) {
        ImageView imageView;
        ImageView imageView2;
        ImageView imageView3;
        LinearLayout linearLayout;
        ImageView imageView4;
        ImageView imageView5;
        ImageView imageView6;
        LinearLayout linearLayout2;
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null || this.editingMessageObject != null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) editTextCaption.getLayoutParams();
        int i2 = layoutParams.rightMargin;
        if (i == 1) {
            ImageView imageView7 = this.botButton;
            if (imageView7 != null && imageView7.getVisibility() == 0 && (imageView6 = this.scheduledButton) != null && imageView6.getVisibility() == 0 && (linearLayout2 = this.attachLayout) != null && linearLayout2.getVisibility() == 0) {
                layoutParams.rightMargin = AndroidUtilities.dp(146.0f);
            } else {
                ImageView imageView8 = this.botButton;
                if ((imageView8 != null && imageView8.getVisibility() == 0) || (((imageView4 = this.notifyButton) != null && imageView4.getVisibility() == 0) || ((imageView5 = this.scheduledButton) != null && imageView5.getTag() != null))) {
                    layoutParams.rightMargin = AndroidUtilities.dp(98.0f);
                } else {
                    layoutParams.rightMargin = AndroidUtilities.dp(50.0f);
                }
            }
        } else if (i == 2) {
            if (i2 != AndroidUtilities.dp(2.0f)) {
                ImageView imageView9 = this.botButton;
                if (imageView9 != null && imageView9.getVisibility() == 0 && (imageView3 = this.scheduledButton) != null && imageView3.getVisibility() == 0 && (linearLayout = this.attachLayout) != null && linearLayout.getVisibility() == 0) {
                    layoutParams.rightMargin = AndroidUtilities.dp(146.0f);
                } else {
                    ImageView imageView10 = this.botButton;
                    if ((imageView10 != null && imageView10.getVisibility() == 0) || (((imageView = this.notifyButton) != null && imageView.getVisibility() == 0) || ((imageView2 = this.scheduledButton) != null && imageView2.getTag() != null))) {
                        layoutParams.rightMargin = AndroidUtilities.dp(98.0f);
                    } else {
                        layoutParams.rightMargin = AndroidUtilities.dp(50.0f);
                    }
                }
            }
        } else {
            ImageView imageView11 = this.scheduledButton;
            if (imageView11 != null && imageView11.getTag() != null) {
                layoutParams.rightMargin = AndroidUtilities.dp(50.0f);
            } else {
                layoutParams.rightMargin = AndroidUtilities.dp(2.0f);
            }
        }
        if (i2 != layoutParams.rightMargin) {
            this.messageEditText.setLayoutParams(layoutParams);
        }
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

    public boolean canShowMessageTransition() {
        return this.moveToSendStateRunnable != null;
    }

    /* JADX WARN: Removed duplicated region for block: B:369:0x02a0 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:370:0x02a1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateRecordInterface(final int i) {
        boolean z;
        float f;
        long j;
        float f2;
        final ViewGroup.LayoutParams layoutParams;
        final ViewGroup viewGroup;
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
                return;
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
            AnimatorSet animatorSet = this.runningAnimationAudio;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            AnimatorSet animatorSet2 = this.recordPannelAnimation;
            if (animatorSet2 != null) {
                animatorSet2.cancel();
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
            this.slideText.setTranslationX(AndroidUtilities.dp(20.0f));
            this.slideText.setAlpha(0.0f);
            this.slideText.setCancelToProgress(0.0f);
            this.slideText.setSlideX(1.0f);
            this.recordCircle.setLockTranslation(10000.0f);
            this.slideText.setEnabled(true);
            this.recordIsCanceled = false;
            AnimatorSet animatorSet3 = new AnimatorSet();
            animatorSet3.playTogether(ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 0.0f), ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.slideText, View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.slideText, View.ALPHA, 1.0f));
            ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView = this.audioVideoSendButton;
            if (chatActivityEnterViewAnimatedIconView != null) {
                animatorSet3.playTogether(ObjectAnimator.ofFloat(chatActivityEnterViewAnimatedIconView, View.ALPHA, 0.0f));
            }
            BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
            if (botCommandsMenuView != null) {
                animatorSet3.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView, View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 0.0f));
            }
            AnimatorSet animatorSet4 = new AnimatorSet();
            animatorSet4.playTogether(ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) this.MESSAGE_TEXT_TRANSLATION_X, AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordedAudioPanel, View.ALPHA, 1.0f));
            ImageView imageView = this.scheduledButton;
            if (imageView != null) {
                animatorSet4.playTogether(ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, AndroidUtilities.dp(30.0f)), ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 0.0f));
            }
            LinearLayout linearLayout = this.attachLayout;
            if (linearLayout != null) {
                animatorSet4.playTogether(ObjectAnimator.ofFloat(linearLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_TRANSLATION_X, AndroidUtilities.dp(30.0f)), ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 0.0f));
            }
            this.runningAnimationAudio.playTogether(animatorSet3.setDuration(150L), animatorSet4.setDuration(150L), ObjectAnimator.ofFloat(this.recordCircle, this.recordCircleScale, 1.0f).setDuration(300L));
            this.runningAnimationAudio.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.56
                {
                    ChatActivityEnterView.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (animator.equals(ChatActivityEnterView.this.runningAnimationAudio)) {
                        ChatActivityEnterView.this.runningAnimationAudio = null;
                    }
                    ChatActivityEnterView.this.isRecordingStateChanged();
                    ChatActivityEnterView.this.slideText.setAlpha(1.0f);
                    ChatActivityEnterView.this.slideText.setTranslationX(0.0f);
                    ChatActivityEnterView.this.recordCircle.showTooltipIfNeed();
                    EditTextCaption editTextCaption = ChatActivityEnterView.this.messageEditText;
                    if (editTextCaption != null) {
                        editTextCaption.setAlpha(0.0f);
                    }
                }
            });
            this.runningAnimationAudio.setInterpolator(new DecelerateInterpolator());
            this.runningAnimationAudio.start();
            this.recordTimerView.start();
        } else if (this.recordIsCanceled && i == 3) {
            return;
        } else {
            PowerManager.WakeLock wakeLock = this.wakeLock;
            if (wakeLock != null) {
                try {
                    wakeLock.release();
                } catch (Exception e2) {
                    e = e2;
                }
                try {
                    this.wakeLock = null;
                } catch (Exception e3) {
                    e = e3;
                    FileLog.e(e);
                    AndroidUtilities.unlockOrientation(this.parentActivity);
                    this.wasSendTyping = false;
                    if (this.recordInterfaceState != 0) {
                    }
                }
            }
            AndroidUtilities.unlockOrientation(this.parentActivity);
            this.wasSendTyping = false;
            if (this.recordInterfaceState != 0) {
                return;
            }
            this.accountInstance.getMessagesController().sendTyping(this.dialog_id, getThreadMessageId(), 2, 0);
            this.recordInterfaceState = 0;
            EmojiView emojiView2 = this.emojiView;
            if (emojiView2 != null) {
                emojiView2.setEnabled(true);
            }
            AnimatorSet animatorSet5 = this.runningAnimationAudio;
            if (animatorSet5 != null) {
                z = animatorSet5.isRunning();
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView2 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView2 != null) {
                    chatActivityEnterViewAnimatedIconView2.setScaleX(1.0f);
                    this.audioVideoSendButton.setScaleY(1.0f);
                }
                this.runningAnimationAudio.removeAllListeners();
                this.runningAnimationAudio.cancel();
            } else {
                z = false;
            }
            AnimatorSet animatorSet6 = this.recordPannelAnimation;
            if (animatorSet6 != null) {
                animatorSet6.cancel();
            }
            EditTextCaption editTextCaption = this.messageEditText;
            if (editTextCaption != null) {
                editTextCaption.setVisibility(0);
            }
            this.runningAnimationAudio = new AnimatorSet();
            if (z || i == 4) {
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView3 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView3 != null) {
                    chatActivityEnterViewAnimatedIconView3.setVisibility(0);
                }
                AnimatorSet animatorSet7 = this.runningAnimationAudio;
                Animator[] animatorArr = new Animator[12];
                animatorArr[0] = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 1.0f);
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView4 = this.emojiButton;
                Property<? super View, Float> property = this.EMOJI_BUTTON_ALPHA;
                float[] fArr = new float[1];
                fArr[0] = this.emojiButtonRestricted ? 0.5f : 1.0f;
                animatorArr[1] = ObjectAnimator.ofFloat(chatActivityEnterViewAnimatedIconView4, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property, fArr);
                animatorArr[2] = ObjectAnimator.ofFloat(this.recordDot, View.SCALE_Y, 0.0f);
                animatorArr[3] = ObjectAnimator.ofFloat(this.recordDot, View.SCALE_X, 0.0f);
                animatorArr[4] = ObjectAnimator.ofFloat(this.recordCircle, this.recordCircleScale, 0.0f);
                animatorArr[5] = ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 1.0f);
                animatorArr[6] = ObjectAnimator.ofFloat(this.recordTimerView, View.ALPHA, 0.0f);
                animatorArr[7] = ObjectAnimator.ofFloat(this.recordCircle, this.recordCircleScale, 0.0f);
                animatorArr[8] = ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 1.0f);
                animatorArr[9] = ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f);
                animatorArr[10] = ObjectAnimator.ofFloat(this.messageEditText, (Property<EditTextCaption, Float>) this.MESSAGE_TEXT_TRANSLATION_X, 0.0f);
                animatorArr[11] = ObjectAnimator.ofFloat(this.recordCircle, "slideToCancelProgress", 1.0f);
                animatorSet7.playTogether(animatorArr);
                BotCommandsMenuView botCommandsMenuView2 = this.botCommandsMenuButton;
                if (botCommandsMenuView2 != null) {
                    this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView2, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 1.0f));
                }
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView5 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView5 != null) {
                    chatActivityEnterViewAnimatedIconView5.setScaleX(1.0f);
                    this.audioVideoSendButton.setScaleY(1.0f);
                    this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(this.audioVideoSendButton, View.ALPHA, 1.0f));
                    this.audioVideoSendButton.setState(isInVideoMode() ? ChatActivityEnterViewAnimatedIconView.State.VIDEO : ChatActivityEnterViewAnimatedIconView.State.VOICE, true);
                }
                ImageView imageView2 = this.scheduledButton;
                if (imageView2 != null) {
                    this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(imageView2, View.TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 1.0f));
                }
                LinearLayout linearLayout2 = this.attachLayout;
                if (linearLayout2 != null) {
                    this.runningAnimationAudio.playTogether(ObjectAnimator.ofFloat(linearLayout2, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 1.0f));
                }
                this.recordIsCanceled = true;
                this.runningAnimationAudio.setDuration(150L);
            } else if (i == 3) {
                SlideTextView slideTextView = this.slideText;
                if (slideTextView != null) {
                    slideTextView.setEnabled(false);
                }
                createRecordAudioPanel();
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
                    }
                    FrameLayout frameLayout2 = this.recordedAudioPanel;
                    if (frameLayout2 != null) {
                        frameLayout2.setAlpha(1.0f);
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
                    }
                    TextView textView2 = this.recordedAudioTimeTextView;
                    if (textView2 != null) {
                        textView2.setVisibility(0);
                        this.recordedAudioTimeTextView.setAlpha(0.0f);
                    }
                    FrameLayout frameLayout3 = this.recordedAudioPanel;
                    if (frameLayout3 != null) {
                        frameLayout3.setVisibility(0);
                        this.recordedAudioPanel.setAlpha(1.0f);
                    }
                    View view2 = this.recordedAudioBackground;
                    if (view2 != null) {
                        view2.setVisibility(0);
                        this.recordedAudioBackground.setAlpha(0.0f);
                    }
                    ImageView imageView4 = this.recordedAudioPlayButton;
                    if (imageView4 != null) {
                        imageView4.setVisibility(0);
                        this.recordedAudioPlayButton.setAlpha(0.0f);
                    }
                    SeekBarWaveformView seekBarWaveformView2 = this.recordedAudioSeekBar;
                    if (seekBarWaveformView2 != null) {
                        seekBarWaveformView2.setVisibility(0);
                        this.recordedAudioSeekBar.setAlpha(0.0f);
                    }
                }
                RLottieImageView rLottieImageView2 = this.recordDeleteImageView;
                if (rLottieImageView2 != null) {
                    rLottieImageView2.setAlpha(0.0f);
                    this.recordDeleteImageView.setScaleX(0.0f);
                    this.recordDeleteImageView.setScaleY(0.0f);
                    this.recordDeleteImageView.setProgress(0.0f);
                    this.recordDeleteImageView.stopAnimation();
                }
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatActivityEnterView.this.lambda$updateRecordInterface$39(valueAnimator);
                    }
                });
                if (!isInVideoMode() && !this.shouldDrawRecordedAudioPanelInParent) {
                    viewGroup = (ViewGroup) this.recordedAudioPanel.getParent();
                    layoutParams = this.recordedAudioPanel.getLayoutParams();
                    viewGroup.removeView(this.recordedAudioPanel);
                    FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(viewGroup.getMeasuredWidth(), AndroidUtilities.dp(48.0f));
                    layoutParams2.gravity = 80;
                    this.sizeNotifierLayout.addView(this.recordedAudioPanel, layoutParams2);
                    this.videoTimelineView.setVisibility(8);
                } else {
                    this.videoTimelineView.setVisibility(0);
                    layoutParams = null;
                    viewGroup = null;
                }
                RLottieImageView rLottieImageView3 = this.recordDeleteImageView;
                if (rLottieImageView3 != null) {
                    rLottieImageView3.setAlpha(0.0f);
                    this.recordDeleteImageView.setScaleX(0.0f);
                    this.recordDeleteImageView.setScaleY(0.0f);
                }
                AnimatorSet animatorSet8 = new AnimatorSet();
                animatorSet8.playTogether(ObjectAnimator.ofFloat(this.recordDot, View.SCALE_Y, 0.0f), ObjectAnimator.ofFloat(this.recordDot, View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.slideText, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.recordDeleteImageView, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 0.0f), ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_ALPHA, 0.0f), ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 0.0f));
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView6 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView6 != null) {
                    animatorSet8.playTogether(ObjectAnimator.ofFloat(chatActivityEnterViewAnimatedIconView6, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.audioVideoSendButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.audioVideoSendButton, View.SCALE_Y, 1.0f));
                    this.audioVideoSendButton.setState(isInVideoMode() ? ChatActivityEnterViewAnimatedIconView.State.VIDEO : ChatActivityEnterViewAnimatedIconView.State.VOICE, true);
                }
                BotCommandsMenuView botCommandsMenuView3 = this.botCommandsMenuButton;
                if (botCommandsMenuView3 != null) {
                    animatorSet8.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView3, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 0.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_Y, 0.0f));
                }
                animatorSet8.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.57
                    {
                        ChatActivityEnterView.this = this;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (ChatActivityEnterView.this.audioVideoSendButton != null) {
                            ChatActivityEnterView.this.audioVideoSendButton.setScaleX(1.0f);
                            ChatActivityEnterView.this.audioVideoSendButton.setScaleY(1.0f);
                        }
                    }
                });
                animatorSet8.setDuration(150L);
                animatorSet8.setStartDelay(150L);
                AnimatorSet animatorSet9 = new AnimatorSet();
                if (isInVideoMode()) {
                    this.recordedAudioTimeTextView.setAlpha(0.0f);
                    this.videoTimelineView.setAlpha(0.0f);
                    animatorSet9.playTogether(ObjectAnimator.ofFloat(this.recordedAudioTimeTextView, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.videoTimelineView, View.ALPHA, 1.0f));
                    animatorSet9.setDuration(150L);
                    animatorSet9.setStartDelay(430L);
                }
                ofFloat.setDuration(isInVideoMode() ? 490L : 580L);
                this.runningAnimationAudio.playTogether(animatorSet8, ofFloat, animatorSet9);
                this.runningAnimationAudio.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.58
                    {
                        ChatActivityEnterView.this = this;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        if (viewGroup != null) {
                            ChatActivityEnterView.this.sizeNotifierLayout.removeView(ChatActivityEnterView.this.recordedAudioPanel);
                            viewGroup.addView(ChatActivityEnterView.this.recordedAudioPanel, layoutParams);
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
                    }
                });
            } else if (i == 2 || i == 5) {
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView7 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView7 != null) {
                    chatActivityEnterViewAnimatedIconView7.setVisibility(0);
                }
                this.recordIsCanceled = true;
                AnimatorSet animatorSet10 = new AnimatorSet();
                Animator[] animatorArr2 = new Animator[4];
                animatorArr2[0] = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 1.0f);
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView8 = this.emojiButton;
                Property<? super View, Float> property2 = this.EMOJI_BUTTON_ALPHA;
                float[] fArr2 = new float[1];
                fArr2[0] = this.emojiButtonRestricted ? 0.5f : 1.0f;
                animatorArr2[1] = ObjectAnimator.ofFloat(chatActivityEnterViewAnimatedIconView8, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property2, fArr2);
                animatorArr2[2] = ObjectAnimator.ofFloat(this.recordDot, View.SCALE_Y, 0.0f);
                animatorArr2[3] = ObjectAnimator.ofFloat(this.recordDot, View.SCALE_X, 0.0f);
                animatorSet10.playTogether(animatorArr2);
                BotCommandsMenuView botCommandsMenuView4 = this.botCommandsMenuButton;
                if (botCommandsMenuView4 != null) {
                    animatorSet10.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView4, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 1.0f));
                }
                AnimatorSet animatorSet11 = new AnimatorSet();
                animatorSet11.playTogether(ObjectAnimator.ofFloat(this.recordTimerView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)), ObjectAnimator.ofFloat(this.slideText, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.slideText, View.TRANSLATION_X, -AndroidUtilities.dp(20.0f)));
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
                    animatorSet10.playTogether(ObjectAnimator.ofFloat(this.recordCircle, "slideToCancelProgress", 1.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 1.0f));
                    LinearLayout linearLayout3 = this.attachLayout;
                    if (linearLayout3 != null) {
                        animatorSet10.playTogether(ObjectAnimator.ofFloat(linearLayout3, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 1.0f), ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_TRANSLATION_X, 0.0f));
                    }
                    ImageView imageView7 = this.attachButton;
                    if (imageView7 != null) {
                        f2 = 1.0f;
                        animatorSet10.playTogether(ObjectAnimator.ofFloat(imageView7, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.attachButton, View.SCALE_Y, 1.0f));
                    } else {
                        f2 = 1.0f;
                    }
                    ImageView imageView8 = this.botButton;
                    if (imageView8 != null) {
                        animatorSet10.playTogether(ObjectAnimator.ofFloat(imageView8, View.SCALE_X, f2), ObjectAnimator.ofFloat(this.botButton, View.SCALE_Y, f2));
                    }
                    ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView9 = this.audioVideoSendButton;
                    if (chatActivityEnterViewAnimatedIconView9 != null) {
                        animatorSet10.playTogether(ObjectAnimator.ofFloat(chatActivityEnterViewAnimatedIconView9, View.ALPHA, f2));
                        animatorSet10.playTogether(ObjectAnimator.ofFloat(this.audioVideoSendButton, View.SCALE_X, f2));
                        animatorSet10.playTogether(ObjectAnimator.ofFloat(this.audioVideoSendButton, View.SCALE_Y, f2));
                        this.audioVideoSendButton.setState(isInVideoMode() ? ChatActivityEnterViewAnimatedIconView.State.VIDEO : ChatActivityEnterViewAnimatedIconView.State.VOICE, true);
                    }
                    ImageView imageView9 = this.scheduledButton;
                    if (imageView9 != null) {
                        animatorSet10.playTogether(ObjectAnimator.ofFloat(imageView9, View.ALPHA, 1.0f), ObjectAnimator.ofFloat(this.scheduledButton, View.TRANSLATION_X, 0.0f));
                    }
                    j = 150;
                } else {
                    AnimatorSet animatorSet12 = new AnimatorSet();
                    animatorSet12.playTogether(ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 1.0f));
                    LinearLayout linearLayout4 = this.attachLayout;
                    if (linearLayout4 != null) {
                        f = 1.0f;
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(linearLayout4, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_TRANSLATION_X, 0.0f), ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 1.0f));
                    } else {
                        f = 1.0f;
                    }
                    ImageView imageView10 = this.scheduledButton;
                    if (imageView10 != null) {
                        animatorSet12.playTogether(ObjectAnimator.ofFloat(imageView10, View.ALPHA, f), ObjectAnimator.ofFloat(this.scheduledButton, View.TRANSLATION_X, 0.0f));
                    }
                    j = 150;
                    animatorSet12.setDuration(150L);
                    animatorSet12.setStartDelay(110L);
                    animatorSet12.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.59
                        {
                            ChatActivityEnterView.this = this;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            if (ChatActivityEnterView.this.audioVideoSendButton != null) {
                                ChatActivityEnterView.this.audioVideoSendButton.setAlpha(1.0f);
                            }
                        }
                    });
                    this.runningAnimationAudio.playTogether(animatorSet12);
                }
                animatorSet10.setDuration(j);
                animatorSet10.setStartDelay(700L);
                animatorSet11.setDuration(200L);
                animatorSet11.setStartDelay(200L);
                this.messageTextTranslationX = 0.0f;
                updateMessageTextParams();
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f);
                ofFloat2.setStartDelay(this.emojiButtonPaddingAlpha == 1.0f ? 300L : 700L);
                ofFloat2.setDuration(200L);
                AnimatorSet animatorSet13 = this.runningAnimationAudio;
                RecordCircle recordCircle3 = this.recordCircle;
                animatorSet13.playTogether(animatorSet10, animatorSet11, ofFloat2, ObjectAnimator.ofFloat(recordCircle3, "lockAnimatedTranslation", recordCircle3.startTranslation).setDuration(200L));
                if (i == 5) {
                    this.recordCircle.canceledByGesture();
                    ObjectAnimator duration = ObjectAnimator.ofFloat(this.recordCircle, "slideToCancelProgress", 1.0f).setDuration(200L);
                    duration.setInterpolator(CubicBezierInterpolator.EASE_BOTH);
                    this.runningAnimationAudio.playTogether(duration);
                } else {
                    ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.recordCircle, "exitTransition", 1.0f);
                    ofFloat3.setDuration(360L);
                    ofFloat3.setStartDelay(490L);
                    this.runningAnimationAudio.playTogether(ofFloat3);
                }
                RecordDot recordDot2 = this.recordDot;
                if (recordDot2 != null) {
                    recordDot2.playDeleteAnimation();
                }
            } else {
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView10 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView10 != null) {
                    chatActivityEnterViewAnimatedIconView10.setVisibility(0);
                }
                AnimatorSet animatorSet14 = new AnimatorSet();
                Animator[] animatorArr3 = new Animator[5];
                animatorArr3[0] = ObjectAnimator.ofFloat(this.emojiButton, (Property<ChatActivityEnterViewAnimatedIconView, Float>) this.EMOJI_BUTTON_SCALE, 1.0f);
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView11 = this.emojiButton;
                Property<? super View, Float> property3 = this.EMOJI_BUTTON_ALPHA;
                float[] fArr3 = new float[1];
                fArr3[0] = this.emojiButtonRestricted ? 0.5f : 1.0f;
                animatorArr3[1] = ObjectAnimator.ofFloat(chatActivityEnterViewAnimatedIconView11, (Property<ChatActivityEnterViewAnimatedIconView, Float>) property3, fArr3);
                animatorArr3[2] = ObjectAnimator.ofFloat(this.recordDot, View.SCALE_Y, 0.0f);
                animatorArr3[3] = ObjectAnimator.ofFloat(this.recordDot, View.SCALE_X, 0.0f);
                animatorArr3[4] = ObjectAnimator.ofFloat(this.audioVideoButtonContainer, View.ALPHA, 1.0f);
                animatorSet14.playTogether(animatorArr3);
                BotCommandsMenuView botCommandsMenuView5 = this.botCommandsMenuButton;
                if (botCommandsMenuView5 != null) {
                    animatorSet14.playTogether(ObjectAnimator.ofFloat(botCommandsMenuView5, View.SCALE_Y, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.SCALE_X, 1.0f), ObjectAnimator.ofFloat(this.botCommandsMenuButton, View.ALPHA, 1.0f));
                }
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView12 = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView12 != null) {
                    chatActivityEnterViewAnimatedIconView12.setScaleX(1.0f);
                    this.audioVideoSendButton.setScaleY(1.0f);
                    animatorSet14.playTogether(ObjectAnimator.ofFloat(this.audioVideoSendButton, View.ALPHA, 1.0f));
                    this.audioVideoSendButton.setState(isInVideoMode() ? ChatActivityEnterViewAnimatedIconView.State.VIDEO : ChatActivityEnterViewAnimatedIconView.State.VOICE, true);
                }
                if (this.attachLayout != null) {
                    this.attachLayoutTranslationX = 0.0f;
                    updateAttachLayoutParams();
                    animatorSet14.playTogether(ObjectAnimator.ofFloat(this.attachLayout, (Property<LinearLayout, Float>) this.ATTACH_LAYOUT_ALPHA, 1.0f));
                }
                ImageView imageView11 = this.scheduledButton;
                if (imageView11 != null) {
                    imageView11.setTranslationX(0.0f);
                    animatorSet14.playTogether(ObjectAnimator.ofFloat(this.scheduledButton, View.ALPHA, 1.0f));
                }
                animatorSet14.setDuration(150L);
                animatorSet14.setStartDelay(200L);
                AnimatorSet animatorSet15 = new AnimatorSet();
                animatorSet15.playTogether(ObjectAnimator.ofFloat(this.recordTimerView, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.recordTimerView, View.TRANSLATION_X, AndroidUtilities.dp(40.0f)), ObjectAnimator.ofFloat(this.slideText, View.ALPHA, 0.0f), ObjectAnimator.ofFloat(this.slideText, View.TRANSLATION_X, AndroidUtilities.dp(40.0f)));
                animatorSet15.setDuration(150L);
                ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.recordCircle, "exitTransition", 1.0f);
                ofFloat4.setDuration(this.messageTransitionIsRunning ? 220L : 360L);
                this.messageTextTranslationX = 0.0f;
                updateMessageTextParams();
                ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(this.messageEditText, View.ALPHA, 1.0f);
                ofFloat5.setStartDelay(this.emojiButtonPaddingAlpha == 1.0f ? 150L : 450L);
                ofFloat5.setDuration(200L);
                this.runningAnimationAudio.playTogether(animatorSet14, animatorSet15, ofFloat5, ofFloat4);
            }
            this.runningAnimationAudio.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.60
                {
                    ChatActivityEnterView.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    EditTextCaption editTextCaption2;
                    if (animator.equals(ChatActivityEnterView.this.runningAnimationAudio)) {
                        if (i != 3 && (editTextCaption2 = ChatActivityEnterView.this.messageEditText) != null) {
                            editTextCaption2.requestFocus();
                        }
                        ChatActivityEnterView.this.cancelRecordIntefraceInternal();
                    }
                }
            });
            this.runningAnimationAudio.start();
            TimerView timerView = this.recordTimerView;
            if (timerView != null) {
                timerView.stop();
            }
        }
        this.delegate.onAudioVideoInterfaceUpdated();
        updateSendAsButton();
    }

    public /* synthetic */ void lambda$updateRecordInterface$39(ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (!isInVideoMode()) {
            this.recordCircle.setTransformToSeekbar(floatValue);
            this.seekBarWaveform.setWaveScaling(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioTimeTextView.setAlpha(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioPlayButton.setAlpha(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioPlayButton.setScaleX(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioPlayButton.setScaleY(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioSeekBar.setAlpha(this.recordCircle.getTransformToSeekbarProgressStep3());
            this.recordedAudioSeekBar.invalidate();
            return;
        }
        this.recordCircle.setExitTransition(floatValue);
    }

    public void cancelRecordIntefraceInternal() {
        FrameLayout frameLayout = this.recordPanel;
        if (frameLayout != null) {
            frameLayout.setVisibility(8);
        }
        RecordCircle recordCircle = this.recordCircle;
        if (recordCircle != null) {
            recordCircle.setVisibility(8);
            this.recordCircle.setSendButtonInvisible();
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

    private void createRecordPanel() {
        if (this.recordPanel != null || getContext() == null) {
            return;
        }
        FrameLayout frameLayout = new FrameLayout(getContext());
        this.recordPanel = frameLayout;
        frameLayout.setClipChildren(false);
        this.recordPanel.setVisibility(8);
        this.messageEditTextContainer.addView(this.recordPanel, LayoutHelper.createFrame(-1, 48.0f));
        this.recordPanel.setOnTouchListener(ChatActivityEnterView$$ExternalSyntheticLambda28.INSTANCE);
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

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.recordingAudioVideo) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void setDelegate(ChatActivityEnterViewDelegate chatActivityEnterViewDelegate) {
        this.delegate = chatActivityEnterViewDelegate;
    }

    public void setCommand(MessageObject messageObject, String str, boolean z, boolean z2) {
        EditTextCaption editTextCaption;
        String str2;
        if (str == null || getVisibility() != 0 || (editTextCaption = this.messageEditText) == null) {
            return;
        }
        TLRPC$User tLRPC$User = null;
        if (z) {
            String obj = editTextCaption.getText().toString();
            if (messageObject != null && DialogObject.isChatDialog(this.dialog_id)) {
                tLRPC$User = this.accountInstance.getMessagesController().getUser(Long.valueOf(messageObject.messageOwner.from_id.user_id));
            }
            if ((this.botCount != 1 || z2) && tLRPC$User != null && tLRPC$User.bot && !str.contains("@")) {
                str2 = String.format(Locale.US, "%s@%s", str, UserObject.getPublicUsername(tLRPC$User)) + " " + obj.replaceFirst("^/[a-zA-Z@\\d_]{1,255}(\\s|$)", "");
            } else {
                str2 = str + " " + obj.replaceFirst("^/[a-zA-Z@\\d_]{1,255}(\\s|$)", "");
            }
            this.ignoreTextChange = true;
            this.messageEditText.setText(str2);
            EditTextCaption editTextCaption2 = this.messageEditText;
            editTextCaption2.setSelection(editTextCaption2.getText().length());
            this.ignoreTextChange = false;
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                chatActivityEnterViewDelegate.onTextChanged(this.messageEditText.getText(), true, false);
            }
            if (this.keyboardVisible || this.currentPopupContentType != -1) {
                return;
            }
            openKeyboard();
        } else if (this.slowModeTimer > 0 && !isInScheduleMode()) {
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
            if (chatActivityEnterViewDelegate2 != null) {
                SimpleTextView simpleTextView = this.slowModeButton;
                chatActivityEnterViewDelegate2.onUpdateSlowModeButton(simpleTextView, true, simpleTextView.getText());
            }
        } else {
            if (messageObject != null && DialogObject.isChatDialog(this.dialog_id)) {
                tLRPC$User = this.accountInstance.getMessagesController().getUser(Long.valueOf(messageObject.messageOwner.from_id.user_id));
            }
            SendMessagesHelper.SendMessageParams of = ((this.botCount != 1 || z2) && tLRPC$User != null && tLRPC$User.bot && !str.contains("@")) ? SendMessagesHelper.SendMessageParams.of(String.format(Locale.US, "%s@%s", str, UserObject.getPublicUsername(tLRPC$User)), this.dialog_id, this.replyingMessageObject, getThreadMessage(), null, false, null, null, null, true, 0, null, false) : SendMessagesHelper.SendMessageParams.of(str, this.dialog_id, this.replyingMessageObject, getThreadMessage(), null, false, null, null, null, true, 0, null, false);
            applyStoryToSendMessageParams(of);
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(of);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:364:0x0344  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x0351  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x0362  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x036c  */
    /* JADX WARN: Removed duplicated region for block: B:377:0x03a2  */
    /* JADX WARN: Removed duplicated region for block: B:419:0x02a7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setEditingMessageObject(MessageObject messageObject, boolean z) {
        boolean z2;
        CharSequence charSequence;
        int i;
        EditTextCaption editTextCaption;
        EditTextCaption editTextCaption2;
        ImageView imageView;
        Paint.FontMetricsInt fontMetricsInt;
        AnimatedEmojiSpan animatedEmojiSpan;
        if (this.audioToSend == null && this.videoToSendMessageObject == null && this.editingMessageObject != messageObject) {
            createMessageEditText();
            int i2 = 1;
            boolean z3 = this.editingMessageObject != null;
            this.editingMessageObject = messageObject;
            this.editingCaption = z;
            if (messageObject != null) {
                AnimatorSet animatorSet = this.doneButtonAnimation;
                if (animatorSet != null) {
                    animatorSet.cancel();
                    this.doneButtonAnimation = null;
                }
                createDoneButton();
                this.doneButtonContainer.setVisibility(0);
                this.doneButtonImage.setScaleX(0.1f);
                this.doneButtonImage.setScaleY(0.1f);
                this.doneButtonImage.setAlpha(0.0f);
                this.doneButtonImage.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(150L).setInterpolator(CubicBezierInterpolator.DEFAULT).start();
                if (z) {
                    this.currentLimit = this.accountInstance.getMessagesController().maxCaptionLength;
                    charSequence = this.editingMessageObject.caption;
                } else {
                    this.currentLimit = this.accountInstance.getMessagesController().maxMessageLength;
                    charSequence = this.editingMessageObject.messageText;
                }
                final CharSequence charSequence2 = "";
                if (charSequence != null) {
                    EditTextCaption editTextCaption3 = this.messageEditText;
                    TextPaint paint = editTextCaption3 != null ? editTextCaption3.getPaint() : null;
                    if (paint == null) {
                        paint = new TextPaint();
                        paint.setTextSize(AndroidUtilities.dp(18.0f));
                    }
                    Paint.FontMetricsInt fontMetricsInt2 = paint.getFontMetricsInt();
                    ArrayList<TLRPC$MessageEntity> arrayList = this.editingMessageObject.messageOwner.entities;
                    MediaDataController.sortEntities(arrayList);
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence);
                    Object[] spans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), Object.class);
                    if (spans != null && spans.length > 0) {
                        for (Object obj : spans) {
                            spannableStringBuilder.removeSpan(obj);
                        }
                    }
                    if (arrayList != null) {
                        int i3 = 0;
                        while (i3 < arrayList.size()) {
                            try {
                                TLRPC$MessageEntity tLRPC$MessageEntity = arrayList.get(i3);
                                if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length <= spannableStringBuilder.length()) {
                                    if (tLRPC$MessageEntity instanceof TLRPC$TL_inputMessageEntityMentionName) {
                                        if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length < spannableStringBuilder.length() && spannableStringBuilder.charAt(tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length) == ' ') {
                                            tLRPC$MessageEntity.length += i2;
                                        }
                                        URLSpanUserMention uRLSpanUserMention = new URLSpanUserMention("" + ((TLRPC$TL_inputMessageEntityMentionName) tLRPC$MessageEntity).user_id.user_id, 3);
                                        int i4 = tLRPC$MessageEntity.offset;
                                        spannableStringBuilder.setSpan(uRLSpanUserMention, i4, tLRPC$MessageEntity.length + i4, 33);
                                    } else {
                                        if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityMentionName) {
                                            if (tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length < spannableStringBuilder.length() && spannableStringBuilder.charAt(tLRPC$MessageEntity.offset + tLRPC$MessageEntity.length) == ' ') {
                                                tLRPC$MessageEntity.length += i2;
                                            }
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("");
                                            fontMetricsInt = fontMetricsInt2;
                                            try {
                                                sb.append(((TLRPC$TL_messageEntityMentionName) tLRPC$MessageEntity).user_id);
                                                URLSpanUserMention uRLSpanUserMention2 = new URLSpanUserMention(sb.toString(), 3);
                                                int i5 = tLRPC$MessageEntity.offset;
                                                spannableStringBuilder.setSpan(uRLSpanUserMention2, i5, tLRPC$MessageEntity.length + i5, 33);
                                            } catch (Exception e) {
                                                e = e;
                                                fontMetricsInt2 = fontMetricsInt;
                                                FileLog.e(e);
                                                CharSequence replaceEmoji = Emoji.replaceEmoji((CharSequence) new SpannableStringBuilder(spannableStringBuilder), fontMetricsInt2, false, (int[]) null);
                                                if (arrayList != null) {
                                                }
                                                charSequence2 = replaceEmoji;
                                                if (this.draftMessage == null) {
                                                    EditTextCaption editTextCaption4 = this.messageEditText;
                                                    this.draftMessage = (editTextCaption4 != null || editTextCaption4.length() <= 0) ? null : this.messageEditText.getText();
                                                    this.draftSearchWebpage = this.messageWebPageSearch;
                                                }
                                                MessageObject messageObject2 = this.editingMessageObject;
                                                TLRPC$MessageMedia tLRPC$MessageMedia = messageObject2.messageOwner.media;
                                                this.messageWebPageSearch = ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaWebPage) || !tLRPC$MessageMedia.manual) && ((i = messageObject2.type) == 0 || i == 19);
                                                if (this.keyboardVisible) {
                                                }
                                                editTextCaption = this.messageEditText;
                                                if (editTextCaption != null) {
                                                }
                                                openKeyboard();
                                                editTextCaption2 = this.messageEditText;
                                                if (editTextCaption2 != null) {
                                                }
                                                this.sendButton.setVisibility(8);
                                                setSlowModeButtonVisible(false);
                                                this.cancelBotButton.setVisibility(8);
                                                this.audioVideoButtonContainer.setVisibility(8);
                                                this.attachLayout.setVisibility(8);
                                                this.sendButtonContainer.setVisibility(8);
                                                imageView = this.scheduledButton;
                                                if (imageView != null) {
                                                }
                                                z2 = true;
                                                updateFieldHint(z2);
                                                updateSendAsButton(z2);
                                            }
                                        } else {
                                            fontMetricsInt = fontMetricsInt2;
                                            if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCode) {
                                                TextStyleSpan.TextStyleRun textStyleRun = new TextStyleSpan.TextStyleRun();
                                                textStyleRun.flags |= 4;
                                                TextStyleSpan textStyleSpan = new TextStyleSpan(textStyleRun);
                                                int i6 = tLRPC$MessageEntity.offset;
                                                MediaDataController.addStyleToText(textStyleSpan, i6, tLRPC$MessageEntity.length + i6, spannableStringBuilder, true);
                                            } else if (!(tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityPre)) {
                                                if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBold) {
                                                    TextStyleSpan.TextStyleRun textStyleRun2 = new TextStyleSpan.TextStyleRun();
                                                    textStyleRun2.flags |= 1;
                                                    TextStyleSpan textStyleSpan2 = new TextStyleSpan(textStyleRun2);
                                                    int i7 = tLRPC$MessageEntity.offset;
                                                    MediaDataController.addStyleToText(textStyleSpan2, i7, tLRPC$MessageEntity.length + i7, spannableStringBuilder, true);
                                                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityItalic) {
                                                    TextStyleSpan.TextStyleRun textStyleRun3 = new TextStyleSpan.TextStyleRun();
                                                    textStyleRun3.flags |= 2;
                                                    TextStyleSpan textStyleSpan3 = new TextStyleSpan(textStyleRun3);
                                                    int i8 = tLRPC$MessageEntity.offset;
                                                    MediaDataController.addStyleToText(textStyleSpan3, i8, tLRPC$MessageEntity.length + i8, spannableStringBuilder, true);
                                                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityStrike) {
                                                    TextStyleSpan.TextStyleRun textStyleRun4 = new TextStyleSpan.TextStyleRun();
                                                    textStyleRun4.flags |= 8;
                                                    TextStyleSpan textStyleSpan4 = new TextStyleSpan(textStyleRun4);
                                                    int i9 = tLRPC$MessageEntity.offset;
                                                    MediaDataController.addStyleToText(textStyleSpan4, i9, tLRPC$MessageEntity.length + i9, spannableStringBuilder, true);
                                                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityUnderline) {
                                                    TextStyleSpan.TextStyleRun textStyleRun5 = new TextStyleSpan.TextStyleRun();
                                                    textStyleRun5.flags |= 16;
                                                    TextStyleSpan textStyleSpan5 = new TextStyleSpan(textStyleRun5);
                                                    int i10 = tLRPC$MessageEntity.offset;
                                                    MediaDataController.addStyleToText(textStyleSpan5, i10, tLRPC$MessageEntity.length + i10, spannableStringBuilder, true);
                                                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityTextUrl) {
                                                    URLSpanReplacement uRLSpanReplacement = new URLSpanReplacement(tLRPC$MessageEntity.url);
                                                    int i11 = tLRPC$MessageEntity.offset;
                                                    spannableStringBuilder.setSpan(uRLSpanReplacement, i11, tLRPC$MessageEntity.length + i11, 33);
                                                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntitySpoiler) {
                                                    TextStyleSpan.TextStyleRun textStyleRun6 = new TextStyleSpan.TextStyleRun();
                                                    textStyleRun6.flags |= LiteMode.FLAG_CHAT_BLUR;
                                                    TextStyleSpan textStyleSpan6 = new TextStyleSpan(textStyleRun6);
                                                    int i12 = tLRPC$MessageEntity.offset;
                                                    MediaDataController.addStyleToText(textStyleSpan6, i12, tLRPC$MessageEntity.length + i12, spannableStringBuilder, true);
                                                } else if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityCustomEmoji) {
                                                    TLRPC$TL_messageEntityCustomEmoji tLRPC$TL_messageEntityCustomEmoji = (TLRPC$TL_messageEntityCustomEmoji) tLRPC$MessageEntity;
                                                    if (tLRPC$TL_messageEntityCustomEmoji.document != null) {
                                                        fontMetricsInt2 = fontMetricsInt;
                                                        animatedEmojiSpan = new AnimatedEmojiSpan(tLRPC$TL_messageEntityCustomEmoji.document, fontMetricsInt2);
                                                    } else {
                                                        fontMetricsInt2 = fontMetricsInt;
                                                        animatedEmojiSpan = new AnimatedEmojiSpan(tLRPC$TL_messageEntityCustomEmoji.document_id, fontMetricsInt2);
                                                    }
                                                    int i13 = tLRPC$MessageEntity.offset;
                                                    spannableStringBuilder.setSpan(animatedEmojiSpan, i13, tLRPC$MessageEntity.length + i13, 33);
                                                } else {
                                                    fontMetricsInt2 = fontMetricsInt;
                                                    if (tLRPC$MessageEntity instanceof TLRPC$TL_messageEntityBlockquote) {
                                                        int i14 = tLRPC$MessageEntity.offset;
                                                        QuoteSpan.putQuoteToEditable(spannableStringBuilder, i14, tLRPC$MessageEntity.length + i14);
                                                    }
                                                }
                                            }
                                        }
                                        fontMetricsInt2 = fontMetricsInt;
                                    }
                                }
                                i3++;
                                i2 = 1;
                            } catch (Exception e2) {
                                e = e2;
                            }
                        }
                    }
                    CharSequence replaceEmoji2 = Emoji.replaceEmoji((CharSequence) new SpannableStringBuilder(spannableStringBuilder), fontMetricsInt2, false, (int[]) null);
                    if (arrayList != null) {
                        try {
                            for (int size = arrayList.size() - 1; size >= 0; size--) {
                                TLRPC$MessageEntity tLRPC$MessageEntity2 = arrayList.get(size);
                                if ((tLRPC$MessageEntity2 instanceof TLRPC$TL_messageEntityPre) && tLRPC$MessageEntity2.offset + tLRPC$MessageEntity2.length <= replaceEmoji2.length()) {
                                    if (!(replaceEmoji2 instanceof Spannable)) {
                                        replaceEmoji2 = new SpannableStringBuilder(replaceEmoji2);
                                    }
                                    ((SpannableStringBuilder) replaceEmoji2).insert(tLRPC$MessageEntity2.offset + tLRPC$MessageEntity2.length, (CharSequence) "```\n");
                                    SpannableStringBuilder spannableStringBuilder2 = (SpannableStringBuilder) replaceEmoji2;
                                    int i15 = tLRPC$MessageEntity2.offset;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("```");
                                    String str = tLRPC$MessageEntity2.language;
                                    if (str == null) {
                                        str = "";
                                    }
                                    sb2.append(str);
                                    sb2.append("\n");
                                    spannableStringBuilder2.insert(i15, (CharSequence) sb2.toString());
                                }
                            }
                        } catch (Exception e3) {
                            FileLog.e(e3);
                        }
                    }
                    charSequence2 = replaceEmoji2;
                }
                if (this.draftMessage == null && !z3) {
                    EditTextCaption editTextCaption42 = this.messageEditText;
                    this.draftMessage = (editTextCaption42 != null || editTextCaption42.length() <= 0) ? null : this.messageEditText.getText();
                    this.draftSearchWebpage = this.messageWebPageSearch;
                }
                MessageObject messageObject22 = this.editingMessageObject;
                TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject22.messageOwner.media;
                this.messageWebPageSearch = ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaWebPage) || !tLRPC$MessageMedia2.manual) && ((i = messageObject22.type) == 0 || i == 19);
                if (this.keyboardVisible) {
                    Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda49
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatActivityEnterView.this.lambda$setEditingMessageObject$41(charSequence2);
                        }
                    };
                    this.setTextFieldRunnable = runnable;
                    AndroidUtilities.runOnUIThread(runnable, 200L);
                } else {
                    Runnable runnable2 = this.setTextFieldRunnable;
                    if (runnable2 != null) {
                        AndroidUtilities.cancelRunOnUIThread(runnable2);
                        this.setTextFieldRunnable = null;
                    }
                    setFieldText(charSequence2);
                }
                editTextCaption = this.messageEditText;
                if (editTextCaption != null) {
                    editTextCaption.requestFocus();
                }
                openKeyboard();
                editTextCaption2 = this.messageEditText;
                if (editTextCaption2 != null) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) editTextCaption2.getLayoutParams();
                    layoutParams.rightMargin = AndroidUtilities.dp(4.0f);
                    this.messageEditText.setLayoutParams(layoutParams);
                }
                this.sendButton.setVisibility(8);
                setSlowModeButtonVisible(false);
                this.cancelBotButton.setVisibility(8);
                this.audioVideoButtonContainer.setVisibility(8);
                this.attachLayout.setVisibility(8);
                this.sendButtonContainer.setVisibility(8);
                imageView = this.scheduledButton;
                if (imageView != null) {
                    imageView.setVisibility(8);
                }
                z2 = true;
            } else {
                Runnable runnable3 = this.setTextFieldRunnable;
                if (runnable3 != null) {
                    AndroidUtilities.cancelRunOnUIThread(runnable3);
                    this.setTextFieldRunnable = null;
                }
                FrameLayout frameLayout = this.doneButtonContainer;
                if (frameLayout != null) {
                    frameLayout.setVisibility(8);
                }
                this.currentLimit = -1;
                this.delegate.onMessageEditEnd(false);
                this.sendButtonContainer.setVisibility(0);
                this.cancelBotButton.setScaleX(0.1f);
                this.cancelBotButton.setScaleY(0.1f);
                this.cancelBotButton.setAlpha(0.0f);
                this.cancelBotButton.setVisibility(8);
                if (this.slowModeTimer > 0 && !isInScheduleMode()) {
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
                } else {
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
                z2 = true;
                updateFieldRight(1);
            }
            updateFieldHint(z2);
            updateSendAsButton(z2);
        }
    }

    public /* synthetic */ void lambda$setEditingMessageObject$41(CharSequence charSequence) {
        setFieldText(charSequence);
        this.setTextFieldRunnable = null;
    }

    public ImageView getAttachButton() {
        return this.attachButton;
    }

    public View getSendButton() {
        return this.sendButton.getVisibility() == 0 ? this.sendButton : this.audioVideoButtonContainer;
    }

    public View getAudioVideoButtonContainer() {
        return this.audioVideoButtonContainer;
    }

    public View getEmojiButton() {
        return this.emojiButton;
    }

    public EmojiView getEmojiView() {
        return this.emojiView;
    }

    public TrendingStickersAlert getTrendingStickersAlert() {
        return this.trendingStickersAlert;
    }

    public void updateColors() {
        ActionBarPopupWindow.ActionBarPopupWindowLayout actionBarPopupWindowLayout = this.sendPopupLayout;
        if (actionBarPopupWindowLayout != null) {
            int childCount = actionBarPopupWindowLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.sendPopupLayout.getChildAt(i);
                if (childAt instanceof ActionBarMenuSubItem) {
                    ActionBarMenuSubItem actionBarMenuSubItem = (ActionBarMenuSubItem) childAt;
                    actionBarMenuSubItem.setColors(getThemedColor(Theme.key_actionBarDefaultSubmenuItem), getThemedColor(Theme.key_actionBarDefaultSubmenuItemIcon));
                    actionBarMenuSubItem.setSelectorColor(getThemedColor(Theme.key_dialogButtonSelector));
                }
            }
            this.sendPopupLayout.setBackgroundColor(getThemedColor(Theme.key_actionBarDefaultSubmenuBackground));
            ActionBarPopupWindow actionBarPopupWindow = this.sendPopupWindow;
            if (actionBarPopupWindow != null && actionBarPopupWindow.isShowing()) {
                this.sendPopupLayout.invalidate();
            }
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
            if (this.codePointCount - this.currentLimit < 0) {
                numberTextView.setTextColor(getThemedColor(Theme.key_text_RedRegular));
            } else {
                numberTextView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText));
            }
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
        int i2 = Theme.key_chat_messagePanelIcons;
        chatActivityEnterViewAnimatedIconView.setColorFilter(new PorterDuffColorFilter(getThemedColor(i2), PorterDuff.Mode.SRC_IN));
        this.emojiButton.setColorFilter(new PorterDuffColorFilter(getThemedColor(i2), PorterDuff.Mode.SRC_IN));
        if (Build.VERSION.SDK_INT >= 21) {
            this.emojiButton.setBackground(Theme.createSelectorDrawable(getThemedColor(Theme.key_listSelector)));
        }
    }

    private void updateRecordedDeleteIconColors() {
        int themedColor = getThemedColor(Theme.key_chat_recordedVoiceDot);
        int blendARGB = ColorUtils.blendARGB(themedColor, getThemedColor(Theme.key_chat_messagePanelBackground), 0.5f);
        int themedColor2 = getThemedColor(Theme.key_chat_messagePanelVoiceDelete);
        RLottieImageView rLottieImageView = this.recordDeleteImageView;
        if (rLottieImageView != null) {
            rLottieImageView.setLayerColor("Cup Red.**", themedColor);
            this.recordDeleteImageView.setLayerColor("Box Red.**", themedColor);
            this.recordDeleteImageView.setLayerColor("Cup Grey.**", themedColor2);
            this.recordDeleteImageView.setLayerColor("Box Grey.**", themedColor2);
            this.recordDeleteImageView.setLayerColor("Line 1.**", blendARGB);
            this.recordDeleteImageView.setLayerColor("Line 2.**", blendARGB);
            this.recordDeleteImageView.setLayerColor("Line 3.**", blendARGB);
        }
    }

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
        EditTextCaption editTextCaption2 = this.messageEditText;
        editTextCaption2.setSelection(editTextCaption2.getText().length());
        this.ignoreTextChange = false;
        if (!z || (chatActivityEnterViewDelegate = this.delegate) == null) {
            return;
        }
        chatActivityEnterViewDelegate.onTextChanged(this.messageEditText.getText(), true, z2);
    }

    public void setSelection(int i) {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return;
        }
        editTextCaption.setSelection(i, editTextCaption.length());
    }

    public int getCursorPosition() {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return 0;
        }
        return editTextCaption.getSelectionStart();
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
        if (z) {
            if (this.searchingType != 0 || this.messageEditText.isFocused()) {
                return;
            }
            BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
            if (botWebViewMenuContainer == null || botWebViewMenuContainer.getVisibility() == 8) {
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda35
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActivityEnterView.this.lambda$setFieldFocused$42();
                    }
                };
                this.focusRunnable = runnable;
                AndroidUtilities.runOnUIThread(runnable, 600L);
                return;
            }
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

    public /* synthetic */ void lambda$setFieldFocused$42() {
        EditTextCaption editTextCaption;
        ViewGroup viewGroup = null;
        this.focusRunnable = null;
        boolean z = true;
        if (AndroidUtilities.isTablet()) {
            Activity activity = this.parentActivity;
            if (activity instanceof LaunchActivity) {
                LaunchActivity launchActivity = (LaunchActivity) activity;
                if (launchActivity != null && launchActivity.getLayersActionBarLayout() != null) {
                    viewGroup = launchActivity.getLayersActionBarLayout().getView();
                }
                if (viewGroup != null && viewGroup.getVisibility() == 0) {
                    z = false;
                }
            }
        }
        if (this.isPaused || !z || (editTextCaption = this.messageEditText) == null) {
            return;
        }
        try {
            editTextCaption.requestFocus();
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public boolean hasText() {
        EditTextCaption editTextCaption = this.messageEditText;
        return editTextCaption != null && editTextCaption.length() > 0;
    }

    public EditTextCaption getEditField() {
        return this.messageEditText;
    }

    public Editable getEditText() {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption == null) {
            return null;
        }
        return editTextCaption.getText();
    }

    public CharSequence getDraftMessage() {
        if (this.editingMessageObject != null) {
            if (TextUtils.isEmpty(this.draftMessage)) {
                return null;
            }
            return this.draftMessage;
        } else if (this.messageEditText == null || !hasText()) {
            return null;
        } else {
            return this.messageEditText.getText();
        }
    }

    public CharSequence getFieldText() {
        if (this.messageEditText == null || !hasText()) {
            return null;
        }
        return this.messageEditText.getText();
    }

    /* JADX WARN: Code restructure failed: missing block: B:78:0x0091, code lost:
        if (org.telegram.messenger.MessagesController.getInstance(r6.currentAccount).getMainSettings().getBoolean("show_gift_for_" + r6.parentFragment.getDialogId(), true) != false) goto L25;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateGiftButton(boolean z) {
        boolean z2 = true;
        if (!MessagesController.getInstance(this.currentAccount).premiumLocked && MessagesController.getInstance(this.currentAccount).giftAttachMenuIcon && MessagesController.getInstance(this.currentAccount).giftTextFieldIcon && getParentFragment() != null && getParentFragment().getCurrentUser() != null && !BuildVars.IS_BILLING_UNAVAILABLE && !getParentFragment().getCurrentUser().self && !getParentFragment().getCurrentUser().premium && getParentFragment().getCurrentUserInfo() != null && !getParentFragment().getCurrentUserInfo().premium_gifts.isEmpty() && !isInScheduleMode()) {
        }
        z2 = false;
        if (z2 || this.giftButton != null) {
            createGiftButton();
            AndroidUtilities.updateViewVisibilityAnimated(this.giftButton, z2, 1.0f, z);
            ImageView imageView = this.scheduledButton;
            if (imageView == null || imageView.getVisibility() != 0) {
                return;
            }
            float f = 48.0f;
            int i = z2 ? -AndroidUtilities.dp(48.0f) : 0;
            ImageView imageView2 = this.botButton;
            float dp = i + AndroidUtilities.dp((imageView2 == null || imageView2.getVisibility() != 0) ? 0.0f : 0.0f);
            if (z) {
                this.scheduledButton.animate().translationX(dp).setDuration(150L).start();
            } else {
                this.scheduledButton.setTranslationX(dp);
            }
        }
    }

    public void updateScheduleButton(boolean z) {
        boolean z2;
        ImageView imageView;
        ImageView imageView2;
        TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights;
        int i = 0;
        if (DialogObject.isChatDialog(this.dialog_id)) {
            TLRPC$Chat chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(-this.dialog_id));
            SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(this.currentAccount);
            this.silent = notificationsSettings.getBoolean(NotificationsSettingsFacade.PROPERTY_SILENT + this.dialog_id, false);
            z2 = ChatObject.isChannel(chat) && (chat.creator || ((tLRPC$TL_chatAdminRights = chat.admin_rights) != null && tLRPC$TL_chatAdminRights.post_messages)) && !chat.megagroup;
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
        createScheduledButton();
        ImageView imageView3 = this.scheduledButton;
        float f = 96.0f;
        if (imageView3 != null) {
            if ((imageView3.getTag() != null && z4) || (this.scheduledButton.getTag() == null && !z4)) {
                if (this.notifyButton != null) {
                    i = (z3 || !z2 || this.scheduledButton.getVisibility() == 0) ? 8 : 8;
                    if (i != this.notifyButton.getVisibility()) {
                        this.notifyButton.setVisibility(i);
                        LinearLayout linearLayout2 = this.attachLayout;
                        if (linearLayout2 != null) {
                            ImageView imageView4 = this.botButton;
                            if ((imageView4 == null || imageView4.getVisibility() == 8) && ((imageView2 = this.notifyButton) == null || imageView2.getVisibility() == 8)) {
                                f = 48.0f;
                            }
                            linearLayout2.setPivotX(AndroidUtilities.dp(f));
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
            ImageView imageView5 = this.scheduledButton;
            if (imageView5 != null) {
                imageView5.setVisibility(z4 ? 0 : 8);
                this.scheduledButton.setAlpha(z4 ? 1.0f : 0.0f);
                this.scheduledButton.setScaleX(z4 ? 1.0f : 0.1f);
                this.scheduledButton.setScaleY(z4 ? 1.0f : 0.1f);
                ImageView imageView6 = this.notifyButton;
                if (imageView6 != null) {
                    imageView6.setVisibility((!z2 || this.scheduledButton.getVisibility() == 0) ? 8 : 8);
                }
                ImageView imageView7 = this.giftButton;
                if (imageView7 != null && imageView7.getVisibility() == 0) {
                    this.scheduledButton.setTranslationX(-AndroidUtilities.dp(48.0f));
                }
            }
        } else {
            ImageView imageView8 = this.scheduledButton;
            if (imageView8 != null) {
                if (z4) {
                    imageView8.setVisibility(0);
                }
                this.scheduledButton.setPivotX(AndroidUtilities.dp(24.0f));
                AnimatorSet animatorSet2 = new AnimatorSet();
                this.scheduledButtonAnimation = animatorSet2;
                Animator[] animatorArr = new Animator[3];
                ImageView imageView9 = this.scheduledButton;
                Property property = View.ALPHA;
                float[] fArr = new float[1];
                fArr[0] = z4 ? 1.0f : 0.0f;
                animatorArr[0] = ObjectAnimator.ofFloat(imageView9, property, fArr);
                ImageView imageView10 = this.scheduledButton;
                Property property2 = View.SCALE_X;
                float[] fArr2 = new float[1];
                fArr2[0] = z4 ? 1.0f : 0.1f;
                animatorArr[1] = ObjectAnimator.ofFloat(imageView10, property2, fArr2);
                ImageView imageView11 = this.scheduledButton;
                Property property3 = View.SCALE_Y;
                float[] fArr3 = new float[1];
                fArr3[0] = z4 ? 1.0f : 0.1f;
                animatorArr[2] = ObjectAnimator.ofFloat(imageView11, property3, fArr3);
                animatorSet2.playTogether(animatorArr);
                this.scheduledButtonAnimation.setDuration(180L);
                this.scheduledButtonAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.61
                    {
                        ChatActivityEnterView.this = this;
                    }

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
        LinearLayout linearLayout3 = this.attachLayout;
        if (linearLayout3 != null) {
            ImageView imageView12 = this.botButton;
            if ((imageView12 == null || imageView12.getVisibility() == 8) && ((imageView = this.notifyButton) == null || imageView.getVisibility() == 8)) {
                f = 48.0f;
            }
            linearLayout3.setPivotX(AndroidUtilities.dp(f));
        }
    }

    public void updateSendAsButton() {
        updateSendAsButton(true);
    }

    public void updateSendAsButton(boolean z) {
        float f;
        float f2;
        SenderSelectView senderSelectView;
        SenderSelectView senderSelectView2;
        FrameLayout frameLayout;
        if (this.parentFragment == null || this.delegate == null) {
            return;
        }
        createMessageEditText();
        TLRPC$ChatFull chatFull = this.parentFragment.getMessagesController().getChatFull(-this.dialog_id);
        TLRPC$Peer tLRPC$Peer = chatFull != null ? chatFull.default_send_as : null;
        if (tLRPC$Peer == null && this.delegate.getSendAsPeers() != null && !this.delegate.getSendAsPeers().peers.isEmpty()) {
            tLRPC$Peer = this.delegate.getSendAsPeers().peers.get(0).peer;
        }
        boolean z2 = true;
        boolean z3 = tLRPC$Peer != null && (this.delegate.getSendAsPeers() == null || this.delegate.getSendAsPeers().peers.size() > 1) && !isEditingMessage() && !isRecordingAudioVideo() && ((frameLayout = this.recordedAudioPanel) == null || frameLayout.getVisibility() != 0);
        if (z3) {
            createSenderSelectView();
        }
        if (tLRPC$Peer != null) {
            if (tLRPC$Peer.channel_id != 0) {
                TLRPC$Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(tLRPC$Peer.channel_id));
                if (chat != null && (senderSelectView2 = this.senderSelectView) != null) {
                    senderSelectView2.setAvatar(chat);
                    this.senderSelectView.setContentDescription(LocaleController.formatString(R.string.AccDescrSendAs, chat.title));
                }
            } else {
                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$Peer.user_id));
                if (user != null && (senderSelectView = this.senderSelectView) != null) {
                    senderSelectView.setAvatar(user);
                    this.senderSelectView.setContentDescription(LocaleController.formatString(R.string.AccDescrSendAs, ContactsController.formatName(user.first_name, user.last_name)));
                }
            }
        }
        SenderSelectView senderSelectView3 = this.senderSelectView;
        z2 = (senderSelectView3 == null || senderSelectView3.getVisibility() != 0) ? false : false;
        int dp = AndroidUtilities.dp(2.0f);
        float f3 = z3 ? 0.0f : 1.0f;
        float f4 = z3 ? 1.0f : 0.0f;
        SenderSelectView senderSelectView4 = this.senderSelectView;
        if (senderSelectView4 != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) senderSelectView4.getLayoutParams();
            f = z3 ? ((-this.senderSelectView.getLayoutParams().width) - marginLayoutParams.leftMargin) - dp : 0.0f;
            f2 = z3 ? 0.0f : ((-this.senderSelectView.getLayoutParams().width) - marginLayoutParams.leftMargin) - dp;
        } else {
            f = 0.0f;
            f2 = 0.0f;
        }
        if (z2 != z3) {
            SenderSelectView senderSelectView5 = this.senderSelectView;
            ValueAnimator valueAnimator = senderSelectView5 == null ? null : (ValueAnimator) senderSelectView5.getTag();
            if (valueAnimator != null) {
                valueAnimator.cancel();
                this.senderSelectView.setTag(null);
            }
            if (this.parentFragment.getOtherSameChatsDiff() == 0 && this.parentFragment.fragmentOpened && z) {
                ValueAnimator duration = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(150L);
                SenderSelectView senderSelectView6 = this.senderSelectView;
                if (senderSelectView6 != null) {
                    senderSelectView6.setTranslationX(f);
                }
                this.messageTextTranslationX = f;
                updateMessageTextParams();
                final float f5 = f;
                final float f6 = f2;
                final float f7 = f3;
                final float f8 = f4;
                duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda5
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        ChatActivityEnterView.this.lambda$updateSendAsButton$43(f5, f6, f7, f8, valueAnimator2);
                    }
                });
                final boolean z4 = z3;
                final float f9 = f3;
                final float f10 = f;
                final float f11 = f2;
                duration.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.62
                    {
                        ChatActivityEnterView.this = this;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        if (z4) {
                            ChatActivityEnterView.this.createSenderSelectView();
                            ChatActivityEnterView.this.senderSelectView.setVisibility(0);
                        }
                        float f12 = 0.0f;
                        if (ChatActivityEnterView.this.senderSelectView != null) {
                            ChatActivityEnterView.this.senderSelectView.setAlpha(f9);
                            ChatActivityEnterView.this.senderSelectView.setTranslationX(f10);
                            f12 = ChatActivityEnterView.this.senderSelectView.getTranslationX();
                        }
                        ChatActivityEnterView.this.emojiButton.setTranslationX(f12);
                        ChatActivityEnterView.this.messageTextTranslationX = f12;
                        ChatActivityEnterView.this.updateMessageTextParams();
                        if (ChatActivityEnterView.this.botCommandsMenuButton == null || ChatActivityEnterView.this.botCommandsMenuButton.getTag() != null) {
                            return;
                        }
                        ChatActivityEnterView.this.animationParamsX.clear();
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
                    public void onAnimationCancel(Animator animator) {
                        float f12;
                        if (z4) {
                            ChatActivityEnterView.this.createSenderSelectView();
                        }
                        if (ChatActivityEnterView.this.senderSelectView != null) {
                            ChatActivityEnterView.this.senderSelectView.setVisibility(z4 ? 0 : 8);
                            ChatActivityEnterView.this.senderSelectView.setAlpha(f8);
                            ChatActivityEnterView.this.senderSelectView.setTranslationX(f11);
                            f12 = ChatActivityEnterView.this.senderSelectView.getTranslationX();
                        } else {
                            f12 = 0.0f;
                        }
                        ChatActivityEnterView.this.emojiButton.setTranslationX(f12);
                        ChatActivityEnterView.this.messageTextTranslationX = f12;
                        ChatActivityEnterView.this.updateMessageTextParams();
                        ChatActivityEnterView.this.requestLayout();
                    }
                });
                duration.start();
                SenderSelectView senderSelectView7 = this.senderSelectView;
                if (senderSelectView7 != null) {
                    senderSelectView7.setTag(duration);
                    return;
                }
                return;
            }
            if (z3) {
                createSenderSelectView();
            }
            SenderSelectView senderSelectView8 = this.senderSelectView;
            if (senderSelectView8 != null) {
                senderSelectView8.setVisibility(z3 ? 0 : 8);
                this.senderSelectView.setTranslationX(f2);
            }
            float f12 = z3 ? f2 : 0.0f;
            this.emojiButton.setTranslationX(f12);
            this.messageTextTranslationX = f12;
            updateMessageTextParams();
            SenderSelectView senderSelectView9 = this.senderSelectView;
            if (senderSelectView9 != null) {
                senderSelectView9.setAlpha(f4);
                this.senderSelectView.setTag(null);
            }
        }
    }

    public /* synthetic */ void lambda$updateSendAsButton$43(float f, float f2, float f3, float f4, ValueAnimator valueAnimator) {
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

    public boolean onBotWebViewBackPressed() {
        BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
        return botWebViewMenuContainer != null && botWebViewMenuContainer.onBackPressed();
    }

    public boolean hasBotWebView() {
        return this.botMenuButtonType == BotMenuButtonType.WEB_VIEW;
    }

    private void updateBotButton(boolean z) {
        ImageView imageView;
        ImageView imageView2;
        if (this.isChat) {
            ChatActivity chatActivity = this.parentFragment;
            if (chatActivity != null && !chatActivity.openAnimationEnded) {
                z = false;
            }
            boolean hasBotWebView = hasBotWebView();
            boolean z2 = this.botMenuButtonType != BotMenuButtonType.NO_BUTTON && this.dialog_id > 0;
            ImageView imageView3 = this.botButton;
            boolean z3 = imageView3 != null && imageView3.getVisibility() == 0;
            if (hasBotWebView || this.hasBotCommands || this.botReplyMarkup != null) {
                if (this.botReplyMarkup != null) {
                    if (isPopupShowing() && this.currentPopupContentType == 1 && this.botReplyMarkup.is_persistent) {
                        ImageView imageView4 = this.botButton;
                        if (imageView4 != null && imageView4.getVisibility() != 8) {
                            this.botButton.setVisibility(8);
                        }
                    } else {
                        createBotButton();
                        if (this.botButton.getVisibility() != 0) {
                            this.botButton.setVisibility(0);
                        }
                        this.botButtonDrawable.setIcon(R.drawable.input_bot2, true);
                        this.botButton.setContentDescription(LocaleController.getString("AccDescrBotKeyboard", R.string.AccDescrBotKeyboard));
                    }
                } else if (!z2) {
                    createBotButton();
                    this.botButtonDrawable.setIcon(R.drawable.input_bot1, true);
                    this.botButton.setContentDescription(LocaleController.getString("AccDescrBotCommands", R.string.AccDescrBotCommands));
                    this.botButton.setVisibility(0);
                } else {
                    ImageView imageView5 = this.botButton;
                    if (imageView5 != null) {
                        imageView5.setVisibility(8);
                    }
                }
            } else {
                ImageView imageView6 = this.botButton;
                if (imageView6 != null) {
                    imageView6.setVisibility(8);
                }
            }
            if (z2) {
                createBotCommandsMenuButton();
            }
            ImageView imageView7 = this.botButton;
            boolean z4 = (imageView7 != null && imageView7.getVisibility() == 0) != z3;
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
                ImageView imageView8 = this.botButton;
                boolean z6 = imageView8 != null && imageView8.getVisibility() == 0;
                if (z6 != z3 && (imageView2 = this.botButton) != null) {
                    imageView2.setVisibility(0);
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
            ImageView imageView9 = this.botButton;
            linearLayout.setPivotX(AndroidUtilities.dp(((imageView9 == null || imageView9.getVisibility() == 8) && ((imageView = this.notifyButton) == null || imageView.getVisibility() == 8)) ? 48.0f : 96.0f));
        }
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

    public void setBotsCount(int i, boolean z, boolean z2) {
        this.botCount = i;
        if (this.hasBotCommands != z) {
            this.hasBotCommands = z;
            updateBotButton(z2);
        }
    }

    public void setButtons(MessageObject messageObject) {
        setButtons(messageObject, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:102:0x00c4, code lost:
        if (r7.getInt("closed_botkeyboard_" + getTopicKeyString(), 0) == r6.getId()) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x009f, code lost:
        if (r7.getInt("answered_" + getTopicKeyString(), 0) != r6.getId()) goto L47;
     */
    /* JADX WARN: Removed duplicated region for block: B:114:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x006e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setButtons(MessageObject messageObject, boolean z) {
        TLRPC$TL_replyKeyboardMarkup tLRPC$TL_replyKeyboardMarkup;
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
                BotKeyboardView botKeyboardView = new BotKeyboardView(this.parentActivity, this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatActivityEnterView.63
                    {
                        ChatActivityEnterView.this = this;
                    }

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
                this.botKeyboardView.setDelegate(new BotKeyboardView.BotKeyboardViewDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda58
                    @Override // org.telegram.ui.Components.BotKeyboardView.BotKeyboardViewDelegate
                    public final void didPressedButton(TLRPC$KeyboardButton tLRPC$KeyboardButton) {
                        ChatActivityEnterView.this.lambda$setButtons$44(tLRPC$KeyboardButton);
                    }
                });
                SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
                sizeNotifierFrameLayout.addView(this.botKeyboardView, sizeNotifierFrameLayout.getChildCount() - 1);
            }
            this.botButtonsMessageObject = messageObject;
            if (messageObject != null) {
                TLRPC$ReplyMarkup tLRPC$ReplyMarkup = messageObject.messageOwner.reply_markup;
                if (tLRPC$ReplyMarkup instanceof TLRPC$TL_replyKeyboardMarkup) {
                    tLRPC$TL_replyKeyboardMarkup = (TLRPC$TL_replyKeyboardMarkup) tLRPC$ReplyMarkup;
                    this.botReplyMarkup = tLRPC$TL_replyKeyboardMarkup;
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
            tLRPC$TL_replyKeyboardMarkup = null;
            this.botReplyMarkup = tLRPC$TL_replyKeyboardMarkup;
            BotKeyboardView botKeyboardView22 = this.botKeyboardView;
            android.graphics.Point point2 = AndroidUtilities.displaySize;
            botKeyboardView22.setPanelHeight(point2.x <= point2.y ? this.keyboardHeightLand : this.keyboardHeight);
            if (this.botReplyMarkup == null) {
            }
            updateBotButton(true);
        }
    }

    public /* synthetic */ void lambda$setButtons$44(TLRPC$KeyboardButton tLRPC$KeyboardButton) {
        ChatActivity chatActivity;
        boolean z = this.replyingMessageObject != null && (chatActivity = this.parentFragment) != null && chatActivity.isTopic && chatActivity.getTopicId() == this.replyingMessageObject.getId();
        MessageObject messageObject = this.replyingMessageObject;
        if (messageObject == null || z) {
            messageObject = DialogObject.isChatDialog(this.dialog_id) ? this.botButtonsMessageObject : null;
        }
        MessageObject messageObject2 = this.replyingMessageObject;
        if (messageObject2 == null || z) {
            messageObject2 = this.botButtonsMessageObject;
        }
        boolean didPressedBotButton = didPressedBotButton(tLRPC$KeyboardButton, messageObject, messageObject2);
        if (this.replyingMessageObject != null && !z) {
            openKeyboardInternal();
            setButtons(this.botMessageObject, false);
        } else {
            MessageObject messageObject3 = this.botButtonsMessageObject;
            if (messageObject3 != null && messageObject3.messageOwner.reply_markup.single_use) {
                if (didPressedBotButton) {
                    openKeyboardInternal();
                } else {
                    showPopup(0, 0);
                }
                SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
                edit.putInt("answered_" + getTopicKeyString(), this.botButtonsMessageObject.getId()).commit();
            }
        }
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.onMessageSend(null, true, 0);
        }
    }

    public boolean didPressedBotButton(TLRPC$KeyboardButton tLRPC$KeyboardButton, MessageObject messageObject, MessageObject messageObject2) {
        return didPressedBotButton(tLRPC$KeyboardButton, messageObject, messageObject2, null);
    }

    public boolean didPressedBotButton(final TLRPC$KeyboardButton tLRPC$KeyboardButton, final MessageObject messageObject, final MessageObject messageObject2, Browser.Progress progress) {
        if (tLRPC$KeyboardButton == null || messageObject2 == null) {
            return false;
        }
        if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButton) {
            SendMessagesHelper.getInstance(this.currentAccount).sendMessage(SendMessagesHelper.SendMessageParams.of(tLRPC$KeyboardButton.text, this.dialog_id, messageObject, getThreadMessage(), null, false, null, null, null, true, 0, null, false));
        } else if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonUrl) {
            if (Browser.urlMustNotHaveConfirmation(tLRPC$KeyboardButton.url)) {
                Browser.openUrl(this.parentActivity, Uri.parse(tLRPC$KeyboardButton.url), true, true, progress);
            } else {
                AlertsCreator.showOpenUrlAlert(this.parentFragment, tLRPC$KeyboardButton.url, false, true, true, progress, this.resourcesProvider);
            }
        } else if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonRequestPhone) {
            this.parentFragment.shareMyContact(2, messageObject2);
        } else {
            if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonRequestPoll) {
                this.parentFragment.openPollCreate((tLRPC$KeyboardButton.flags & 1) != 0 ? Boolean.valueOf(tLRPC$KeyboardButton.quiz) : null);
                return false;
            } else if ((tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonWebView) || (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonSimpleWebView)) {
                TLRPC$Message tLRPC$Message = messageObject2.messageOwner;
                final long j = tLRPC$Message.via_bot_id;
                if (j == 0) {
                    j = tLRPC$Message.from_id.user_id;
                }
                MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j));
                final long j2 = j;
                final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView.64
                    {
                        ChatActivityEnterView.this = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        if (ChatActivityEnterView.this.sizeNotifierLayout.measureKeyboardHeight() > AndroidUtilities.dp(20.0f)) {
                            AndroidUtilities.hideKeyboard(ChatActivityEnterView.this);
                            AndroidUtilities.runOnUIThread(this, 150L);
                            return;
                        }
                        BotWebViewSheet botWebViewSheet = new BotWebViewSheet(ChatActivityEnterView.this.getContext(), ChatActivityEnterView.this.resourcesProvider);
                        botWebViewSheet.setParentActivity(ChatActivityEnterView.this.parentActivity);
                        int i = ChatActivityEnterView.this.currentAccount;
                        long j3 = messageObject2.messageOwner.dialog_id;
                        long j4 = j2;
                        TLRPC$KeyboardButton tLRPC$KeyboardButton2 = tLRPC$KeyboardButton;
                        String str = tLRPC$KeyboardButton2.text;
                        String str2 = tLRPC$KeyboardButton2.url;
                        boolean z = tLRPC$KeyboardButton2 instanceof TLRPC$TL_keyboardButtonSimpleWebView;
                        MessageObject messageObject3 = messageObject;
                        botWebViewSheet.requestWebView(i, j3, j4, str, str2, z ? 1 : 0, messageObject3 != null ? messageObject3.messageOwner.id : 0, false);
                        botWebViewSheet.show();
                    }
                };
                if (SharedPrefsHelper.isWebViewConfirmShown(this.currentAccount, j)) {
                    runnable.run();
                } else {
                    AlertsCreator.createBotLaunchAlert(this.parentFragment, MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.dialog_id)), new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda52
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatActivityEnterView.this.lambda$didPressedBotButton$45(runnable, j);
                        }
                    }, null);
                }
            } else if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonRequestGeoLocation) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.parentActivity);
                builder.setTitle(LocaleController.getString("ShareYouLocationTitle", R.string.ShareYouLocationTitle));
                builder.setMessage(LocaleController.getString("ShareYouLocationInfo", R.string.ShareYouLocationInfo));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda8
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        ChatActivityEnterView.this.lambda$didPressedBotButton$46(messageObject2, tLRPC$KeyboardButton, dialogInterface, i);
                    }
                });
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                this.parentFragment.showDialog(builder.create());
            } else if ((tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonCallback) || (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonGame) || (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonBuy) || (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonUrlAuth)) {
                SendMessagesHelper.getInstance(this.currentAccount).sendCallback(true, messageObject2, tLRPC$KeyboardButton, this.parentFragment);
            } else if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonSwitchInline) {
                if (this.parentFragment.processSwitchButton((TLRPC$TL_keyboardButtonSwitchInline) tLRPC$KeyboardButton)) {
                    return true;
                }
                if (tLRPC$KeyboardButton.same_peer) {
                    TLRPC$Message tLRPC$Message2 = messageObject2.messageOwner;
                    long j3 = tLRPC$Message2.from_id.user_id;
                    long j4 = tLRPC$Message2.via_bot_id;
                    if (j4 != 0) {
                        j3 = j4;
                    }
                    TLRPC$User user = this.accountInstance.getMessagesController().getUser(Long.valueOf(j3));
                    if (user == null) {
                        return true;
                    }
                    setFieldText("@" + UserObject.getPublicUsername(user) + " " + tLRPC$KeyboardButton.query);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("onlySelect", true);
                    bundle.putInt("dialogsType", 1);
                    if ((2 & tLRPC$KeyboardButton.flags) != 0) {
                        bundle.putBoolean("allowGroups", false);
                        bundle.putBoolean("allowMegagroups", false);
                        bundle.putBoolean("allowLegacyGroups", false);
                        bundle.putBoolean("allowUsers", false);
                        bundle.putBoolean("allowChannels", false);
                        bundle.putBoolean("allowBots", false);
                        Iterator<TLRPC$InlineQueryPeerType> it = tLRPC$KeyboardButton.peer_types.iterator();
                        while (it.hasNext()) {
                            TLRPC$InlineQueryPeerType next = it.next();
                            if (next instanceof TLRPC$TL_inlineQueryPeerTypePM) {
                                bundle.putBoolean("allowUsers", true);
                            } else if (next instanceof TLRPC$TL_inlineQueryPeerTypeBotPM) {
                                bundle.putBoolean("allowBots", true);
                            } else if (next instanceof TLRPC$TL_inlineQueryPeerTypeBroadcast) {
                                bundle.putBoolean("allowChannels", true);
                            } else if (next instanceof TLRPC$TL_inlineQueryPeerTypeChat) {
                                bundle.putBoolean("allowLegacyGroups", true);
                            } else if (next instanceof TLRPC$TL_inlineQueryPeerTypeMegagroup) {
                                bundle.putBoolean("allowMegagroups", true);
                            }
                        }
                    }
                    DialogsActivity dialogsActivity = new DialogsActivity(bundle);
                    dialogsActivity.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda61
                        @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                        public final boolean didSelectDialogs(DialogsActivity dialogsActivity2, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
                            boolean lambda$didPressedBotButton$47;
                            lambda$didPressedBotButton$47 = ChatActivityEnterView.this.lambda$didPressedBotButton$47(messageObject2, tLRPC$KeyboardButton, dialogsActivity2, arrayList, charSequence, z, topicsFragment);
                            return lambda$didPressedBotButton$47;
                        }
                    });
                    this.parentFragment.presentFragment(dialogsActivity);
                }
            } else if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonUserProfile) {
                if (MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(tLRPC$KeyboardButton.user_id)) != null) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putLong("user_id", tLRPC$KeyboardButton.user_id);
                    this.parentFragment.presentFragment(new ProfileActivity(bundle2));
                }
            } else if (tLRPC$KeyboardButton instanceof TLRPC$TL_keyboardButtonRequestPeer) {
                final TLRPC$TL_keyboardButtonRequestPeer tLRPC$TL_keyboardButtonRequestPeer = (TLRPC$TL_keyboardButtonRequestPeer) tLRPC$KeyboardButton;
                if (tLRPC$TL_keyboardButtonRequestPeer.peer_type != null && messageObject2.messageOwner != null) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putBoolean("onlySelect", true);
                    bundle3.putInt("dialogsType", 15);
                    TLRPC$Message tLRPC$Message3 = messageObject2.messageOwner;
                    if (tLRPC$Message3 != null) {
                        TLRPC$Peer tLRPC$Peer = tLRPC$Message3.from_id;
                        if (tLRPC$Peer instanceof TLRPC$TL_peerUser) {
                            bundle3.putLong("requestPeerBotId", tLRPC$Peer.user_id);
                        }
                    }
                    try {
                        SerializedData serializedData = new SerializedData(tLRPC$TL_keyboardButtonRequestPeer.peer_type.getObjectSize());
                        tLRPC$TL_keyboardButtonRequestPeer.peer_type.serializeToStream(serializedData);
                        bundle3.putByteArray("requestPeerType", serializedData.toByteArray());
                        serializedData.cleanup();
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                    DialogsActivity dialogsActivity2 = new DialogsActivity(bundle3);
                    dialogsActivity2.setDelegate(new DialogsActivity.DialogsActivityDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView.65
                        {
                            ChatActivityEnterView.this = this;
                        }

                        @Override // org.telegram.ui.DialogsActivity.DialogsActivityDelegate
                        public boolean didSelectDialogs(DialogsActivity dialogsActivity3, ArrayList<MessagesStorage.TopicKey> arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
                            if (arrayList != null && !arrayList.isEmpty()) {
                                TLRPC$TL_messages_sendBotRequestedPeer tLRPC$TL_messages_sendBotRequestedPeer = new TLRPC$TL_messages_sendBotRequestedPeer();
                                tLRPC$TL_messages_sendBotRequestedPeer.peer = MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).getInputPeer(messageObject2.messageOwner.peer_id);
                                tLRPC$TL_messages_sendBotRequestedPeer.msg_id = messageObject2.getId();
                                tLRPC$TL_messages_sendBotRequestedPeer.button_id = tLRPC$TL_keyboardButtonRequestPeer.button_id;
                                tLRPC$TL_messages_sendBotRequestedPeer.requested_peer = MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).getInputPeer(arrayList.get(0).dialogId);
                                ConnectionsManager.getInstance(ChatActivityEnterView.this.currentAccount).sendRequest(tLRPC$TL_messages_sendBotRequestedPeer, null);
                            }
                            dialogsActivity3.finishFragment();
                            return true;
                        }
                    });
                    this.parentFragment.presentFragment(dialogsActivity2);
                    return false;
                }
                FileLog.e("button.peer_type is null");
            }
        }
        return true;
    }

    public /* synthetic */ void lambda$didPressedBotButton$45(Runnable runnable, long j) {
        runnable.run();
        SharedPrefsHelper.setWebViewConfirmShown(this.currentAccount, j, true);
    }

    public /* synthetic */ void lambda$didPressedBotButton$46(MessageObject messageObject, TLRPC$KeyboardButton tLRPC$KeyboardButton, DialogInterface dialogInterface, int i) {
        if (Build.VERSION.SDK_INT >= 23 && this.parentActivity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0) {
            this.parentActivity.requestPermissions(new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}, 2);
            this.pendingMessageObject = messageObject;
            this.pendingLocationButton = tLRPC$KeyboardButton;
            return;
        }
        SendMessagesHelper.getInstance(this.currentAccount).sendCurrentLocation(messageObject, tLRPC$KeyboardButton);
    }

    public /* synthetic */ boolean lambda$didPressedBotButton$47(MessageObject messageObject, TLRPC$KeyboardButton tLRPC$KeyboardButton, DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z, TopicsFragment topicsFragment) {
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        long j = tLRPC$Message.from_id.user_id;
        long j2 = tLRPC$Message.via_bot_id;
        if (j2 != 0) {
            j = j2;
        }
        TLRPC$User user = this.accountInstance.getMessagesController().getUser(Long.valueOf(j));
        if (user == null) {
            dialogsActivity.finishFragment();
            return true;
        }
        long j3 = ((MessagesStorage.TopicKey) arrayList.get(0)).dialogId;
        MediaDataController mediaDataController = MediaDataController.getInstance(this.currentAccount);
        mediaDataController.saveDraft(j3, 0, "@" + UserObject.getPublicUsername(user) + " " + tLRPC$KeyboardButton.query, null, null, true);
        if (j3 != this.dialog_id) {
            if (!DialogObject.isEncryptedDialog(j3)) {
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
                } else {
                    dialogsActivity.finishFragment();
                }
            } else {
                dialogsActivity.finishFragment();
            }
        } else {
            dialogsActivity.finishFragment();
        }
        return true;
    }

    public boolean isPopupView(View view) {
        return view == this.botKeyboardView || view == this.emojiView;
    }

    public int getPopupViewHeight(View view) {
        BotKeyboardView botKeyboardView = this.botKeyboardView;
        if (view != botKeyboardView || botKeyboardView == null) {
            return -1;
        }
        return botKeyboardView.getKeyboardHeight();
    }

    public boolean isRecordCircle(View view) {
        return view == this.recordCircle;
    }

    public SizeNotifierFrameLayout getSizeNotifierLayout() {
        return this.sizeNotifierLayout;
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
        EmojiView emojiView2 = new EmojiView(this.parentFragment, this.allowAnimatedEmoji, true, true, getContext(), true, this.info, this.sizeNotifierLayout, this.shouldDrawBackground, this.resourcesProvider, this.emojiViewFrozen) { // from class: org.telegram.ui.Components.ChatActivityEnterView.66
            {
                ChatActivityEnterView.this = this;
            }

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
        this.emojiView.setDelegate(new 67());
        this.emojiView.setDragListener(new EmojiView.DragListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView.68
            int initialOffset;
            boolean wasExpanded;

            {
                ChatActivityEnterView.this = this;
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
                        chatActivityEnterView3.sizeNotifierLayout.setForeground(new ScrimDrawable());
                    }
                    this.initialOffset = (int) ChatActivityEnterView.this.getTranslationY();
                    if (ChatActivityEnterView.this.delegate != null) {
                        ChatActivityEnterView.this.delegate.onStickersExpandedChange();
                    }
                }
            }

            @Override // org.telegram.ui.Components.EmojiView.DragListener
            public void onDragEnd(float f) {
                if (allowDragging()) {
                    ChatActivityEnterView.this.stickersDragging = false;
                    if ((this.wasExpanded && f >= AndroidUtilities.dp(200.0f)) || ((!this.wasExpanded && f <= AndroidUtilities.dp(-200.0f)) || ((this.wasExpanded && ChatActivityEnterView.this.stickersExpansionProgress <= 0.6f) || (!this.wasExpanded && ChatActivityEnterView.this.stickersExpansionProgress >= 0.4f)))) {
                        ChatActivityEnterView.this.setStickersExpanded(!this.wasExpanded, true, true);
                    } else {
                        ChatActivityEnterView.this.setStickersExpanded(this.wasExpanded, true, true);
                    }
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
            public void onDrag(int i) {
                int i2;
                ChatActivityEnterView chatActivityEnterView;
                if (allowDragging()) {
                    android.graphics.Point point = AndroidUtilities.displaySize;
                    float max = Math.max(Math.min(i + this.initialOffset, 0), -(ChatActivityEnterView.this.stickersExpandedHeight - (point.x > point.y ? ChatActivityEnterView.this.keyboardHeightLand : ChatActivityEnterView.this.keyboardHeight)));
                    ChatActivityEnterView.this.emojiView.setTranslationY(max);
                    ChatActivityEnterView.this.setTranslationY(max);
                    ChatActivityEnterView.this.stickersExpansionProgress = max / (-(chatActivityEnterView.stickersExpandedHeight - i2));
                    ChatActivityEnterView.this.sizeNotifierLayout.invalidate();
                }
            }

            private boolean allowDragging() {
                EditTextCaption editTextCaption;
                return ChatActivityEnterView.this.stickersTabOpen && (ChatActivityEnterView.this.stickersExpanded || (editTextCaption = ChatActivityEnterView.this.messageEditText) == null || editTextCaption.length() <= 0) && ChatActivityEnterView.this.emojiView.areThereAnyStickers() && !ChatActivityEnterView.this.waitingForKeyboardOpen;
            }
        });
        attachEmojiView();
        checkChannelRights();
    }

    /* loaded from: classes4.dex */
    public class 67 implements EmojiView.EmojiViewDelegate {
        67() {
            ChatActivityEnterView.this = r1;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean isUserSelf() {
            return ChatActivityEnterView.this.dialog_id == UserConfig.getInstance(ChatActivityEnterView.this.currentAccount).getClientUserId();
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
        public void onCustomEmojiSelected(final long j, final TLRPC$Document tLRPC$Document, final String str, final boolean z) {
            AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$67$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.67.this.lambda$onCustomEmojiSelected$0(str, tLRPC$Document, j, z);
                }
            });
        }

        public /* synthetic */ void lambda$onCustomEmojiSelected$0(String str, TLRPC$Document tLRPC$Document, long j, boolean z) {
            AnimatedEmojiSpan animatedEmojiSpan;
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
                    if (tLRPC$Document != null) {
                        animatedEmojiSpan = new AnimatedEmojiSpan(tLRPC$Document, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt());
                    } else {
                        animatedEmojiSpan = new AnimatedEmojiSpan(j, ChatActivityEnterView.this.messageEditText.getPaint().getFontMetricsInt());
                    }
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
            } finally {
                ChatActivityEnterView.this.innerTextChange = 0;
            }
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
        public void onStickerSelected(View view, TLRPC$Document tLRPC$Document, String str, Object obj, MessageObject.SendAnimationData sendAnimationData, boolean z, int i) {
            if (ChatActivityEnterView.this.trendingStickersAlert != null) {
                ChatActivityEnterView.this.trendingStickersAlert.dismiss();
                ChatActivityEnterView.this.trendingStickersAlert = null;
            }
            if (ChatActivityEnterView.this.slowModeTimer <= 0 || isInScheduleMode()) {
                if (ChatActivityEnterView.this.stickersExpanded) {
                    if (ChatActivityEnterView.this.searchingType != 0) {
                        ChatActivityEnterView.this.setSearchingTypeInternal(0, true);
                        ChatActivityEnterView.this.emojiView.closeSearch(true, MessageObject.getStickerSetId(tLRPC$Document));
                        ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                    }
                    ChatActivityEnterView.this.setStickersExpanded(false, true, false);
                }
                ChatActivityEnterView.this.lambda$onStickerSelected$48(tLRPC$Document, str, obj, sendAnimationData, false, z, i);
                if (DialogObject.isEncryptedDialog(ChatActivityEnterView.this.dialog_id) && MessageObject.isGifDocument(tLRPC$Document)) {
                    ChatActivityEnterView.this.accountInstance.getMessagesController().saveGif(obj, tLRPC$Document);
                }
            } else if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onUpdateSlowModeButton(view != null ? view : ChatActivityEnterView.this.slowModeButton, true, ChatActivityEnterView.this.slowModeButton.getText());
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickersSettingsClick() {
            if (ChatActivityEnterView.this.parentFragment != null) {
                ChatActivityEnterView.this.parentFragment.presentFragment(new StickersActivity(0, null));
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onEmojiSettingsClick(ArrayList<TLRPC$TL_messages_stickerSet> arrayList) {
            if (ChatActivityEnterView.this.parentFragment != null) {
                ChatActivityEnterView.this.parentFragment.presentFragment(new StickersActivity(5, arrayList));
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        /* renamed from: onGifSelected */
        public void lambda$onGifSelected$1(final View view, final Object obj, final String str, final Object obj2, final boolean z, final int i) {
            if (ChatActivityEnterView.this.replyingQuote != null && ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.replyingQuote.outdated) {
                ChatActivityEnterView.this.parentFragment.showQuoteMessageUpdate();
            } else if (!isInScheduleMode() || i != 0) {
                if (ChatActivityEnterView.this.slowModeTimer > 0 && !isInScheduleMode()) {
                    if (ChatActivityEnterView.this.delegate != null) {
                        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = ChatActivityEnterView.this.delegate;
                        if (view == null) {
                            view = ChatActivityEnterView.this.slowModeButton;
                        }
                        chatActivityEnterViewDelegate.onUpdateSlowModeButton(view, true, ChatActivityEnterView.this.slowModeButton.getText());
                        return;
                    }
                    return;
                }
                Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$67$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ChatActivityEnterView.67.this.lambda$onGifSelected$2(obj, str, z, i, obj2);
                    }
                };
                if (ChatActivityEnterView.this.showConfirmAlert(runnable)) {
                    return;
                }
                runnable.run();
            } else {
                AlertsCreator.createScheduleDatePickerDialog(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$67$$ExternalSyntheticLambda3
                    @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                    public final void didSelectDate(boolean z2, int i2) {
                        ChatActivityEnterView.67.this.lambda$onGifSelected$1(view, obj, str, obj2, z2, i2);
                    }
                }, ChatActivityEnterView.this.resourcesProvider);
            }
        }

        public /* synthetic */ void lambda$onGifSelected$2(Object obj, String str, boolean z, int i, Object obj2) {
            int i2;
            if (ChatActivityEnterView.this.stickersExpanded) {
                if (ChatActivityEnterView.this.searchingType != 0) {
                    ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                }
                ChatActivityEnterView.this.setStickersExpanded(false, true, false);
            }
            TL_stories$StoryItem replyToStory = ChatActivityEnterView.this.delegate != null ? ChatActivityEnterView.this.delegate.getReplyToStory() : null;
            if (obj instanceof TLRPC$Document) {
                TLRPC$Document tLRPC$Document = (TLRPC$Document) obj;
                SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendSticker(tLRPC$Document, str, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), replyToStory, ChatActivityEnterView.this.replyingQuote, null, z, i, false, obj2);
                MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).addRecentGif(tLRPC$Document, (int) (System.currentTimeMillis() / 1000), true);
                if (DialogObject.isEncryptedDialog(ChatActivityEnterView.this.dialog_id)) {
                    ChatActivityEnterView.this.accountInstance.getMessagesController().saveGif(obj2, tLRPC$Document);
                }
            } else if (obj instanceof TLRPC$BotInlineResult) {
                TLRPC$BotInlineResult tLRPC$BotInlineResult = (TLRPC$BotInlineResult) obj;
                if (tLRPC$BotInlineResult.document != null) {
                    MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).addRecentGif(tLRPC$BotInlineResult.document, (int) (System.currentTimeMillis() / 1000), false);
                    if (DialogObject.isEncryptedDialog(ChatActivityEnterView.this.dialog_id)) {
                        ChatActivityEnterView.this.accountInstance.getMessagesController().saveGif(obj2, tLRPC$BotInlineResult.document);
                    }
                }
                TLRPC$User tLRPC$User = (TLRPC$User) obj2;
                HashMap hashMap = new HashMap();
                hashMap.put("id", tLRPC$BotInlineResult.id);
                hashMap.put("query_id", "" + tLRPC$BotInlineResult.query_id);
                hashMap.put("force_gif", "1");
                if (replyToStory == null) {
                    SendMessagesHelper.prepareSendingBotContextResult(ChatActivityEnterView.this.parentFragment, ChatActivityEnterView.this.accountInstance, tLRPC$BotInlineResult, hashMap, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), null, ChatActivityEnterView.this.replyingQuote, z, i);
                    i2 = 0;
                } else {
                    i2 = 0;
                    SendMessagesHelper.getInstance(ChatActivityEnterView.this.currentAccount).sendSticker(tLRPC$BotInlineResult.document, str, ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.replyingMessageObject, ChatActivityEnterView.this.getThreadMessage(), replyToStory, ChatActivityEnterView.this.replyingQuote, null, z, i, false, obj2);
                }
                if (ChatActivityEnterView.this.searchingType != 0) {
                    ChatActivityEnterView.this.setSearchingTypeInternal(i2, true);
                    ChatActivityEnterView.this.emojiView.closeSearch(true);
                    ChatActivityEnterView.this.emojiView.hideSearchKeyboard();
                }
            }
            if (ChatActivityEnterView.this.delegate != null) {
                ChatActivityEnterView.this.delegate.onMessageSend(null, z, i);
            }
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onTabOpened(int i) {
            ChatActivityEnterView.this.delegate.onStickersTab(i == 3);
            ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
            chatActivityEnterView.post(chatActivityEnterView.updateExpandabilityRunnable);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onClearEmojiRecent() {
            if (ChatActivityEnterView.this.parentFragment == null || ChatActivityEnterView.this.parentActivity == null) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivityEnterView.this.parentActivity, ChatActivityEnterView.this.resourcesProvider);
            builder.setTitle(LocaleController.getString("ClearRecentEmojiTitle", R.string.ClearRecentEmojiTitle));
            builder.setMessage(LocaleController.getString("ClearRecentEmojiText", R.string.ClearRecentEmojiText));
            builder.setPositiveButton(LocaleController.getString("ClearButton", R.string.ClearForAll), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$67$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ChatActivityEnterView.67.this.lambda$onClearEmojiRecent$3(dialogInterface, i);
                }
            });
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            ChatActivityEnterView.this.parentFragment.showDialog(builder.create());
        }

        public /* synthetic */ void lambda$onClearEmojiRecent$3(DialogInterface dialogInterface, int i) {
            ChatActivityEnterView.this.emojiView.clearRecentEmoji();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onShowStickerSet(TLRPC$StickerSet tLRPC$StickerSet, TLRPC$InputStickerSet tLRPC$InputStickerSet) {
            if (ChatActivityEnterView.this.trendingStickersAlert == null || ChatActivityEnterView.this.trendingStickersAlert.isDismissed()) {
                BaseFragment baseFragment = ChatActivityEnterView.this.parentFragment;
                if (baseFragment == null) {
                    baseFragment = LaunchActivity.getLastFragment();
                }
                if (baseFragment == null || ChatActivityEnterView.this.parentActivity == null) {
                    return;
                }
                if (tLRPC$StickerSet != null) {
                    tLRPC$InputStickerSet = new TLRPC$TL_inputStickerSetID();
                    tLRPC$InputStickerSet.access_hash = tLRPC$StickerSet.access_hash;
                    tLRPC$InputStickerSet.id = tLRPC$StickerSet.id;
                }
                Activity activity = ChatActivityEnterView.this.parentActivity;
                ChatActivityEnterView chatActivityEnterView = ChatActivityEnterView.this;
                BaseFragment baseFragment2 = baseFragment;
                baseFragment.showDialog(new StickersAlert(activity, baseFragment2, tLRPC$InputStickerSet, null, chatActivityEnterView, chatActivityEnterView.resourcesProvider));
                return;
            }
            ChatActivityEnterView.this.trendingStickersAlert.getLayout().showStickerSet(tLRPC$StickerSet, tLRPC$InputStickerSet);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickerSetAdd(TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
            MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).toggleStickerSet(ChatActivityEnterView.this.parentActivity, tLRPC$StickerSetCovered, 2, ChatActivityEnterView.this.parentFragment, false, false);
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void onStickerSetRemove(TLRPC$StickerSetCovered tLRPC$StickerSetCovered) {
            MediaDataController.getInstance(ChatActivityEnterView.this.currentAccount).toggleStickerSet(ChatActivityEnterView.this.parentActivity, tLRPC$StickerSetCovered, 0, ChatActivityEnterView.this.parentFragment, false, false);
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
        public boolean isSearchOpened() {
            return ChatActivityEnterView.this.searchingType != 0;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean isExpanded() {
            return ChatActivityEnterView.this.stickersExpanded;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean canSchedule() {
            return ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.parentFragment.canScheduleMessage();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public boolean isInScheduleMode() {
            return ChatActivityEnterView.this.parentFragment != null && ChatActivityEnterView.this.parentFragment.isInScheduleMode();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public long getDialogId() {
            return ChatActivityEnterView.this.dialog_id;
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public int getThreadId() {
            return ChatActivityEnterView.this.getThreadMessageId();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void showTrendingStickersAlert(TrendingStickersLayout trendingStickersLayout) {
            BaseFragment baseFragment = ChatActivityEnterView.this.parentFragment;
            if (baseFragment == null) {
                baseFragment = LaunchActivity.getLastFragment();
            }
            if (baseFragment != null) {
                ChatActivityEnterView.this.trendingStickersAlert = new TrendingStickersAlert(ChatActivityEnterView.this.getContext(), baseFragment, trendingStickersLayout, ChatActivityEnterView.this.resourcesProvider) { // from class: org.telegram.ui.Components.ChatActivityEnterView.67.1
                    {
                        67.this = this;
                    }

                    @Override // org.telegram.ui.Components.TrendingStickersAlert, org.telegram.ui.ActionBar.BottomSheet, android.app.Dialog, android.content.DialogInterface
                    public void dismiss() {
                        super.dismiss();
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

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public void invalidateEnterView() {
            ChatActivityEnterView.this.invalidate();
        }

        @Override // org.telegram.ui.Components.EmojiView.EmojiViewDelegate
        public float getProgressToSearchOpened() {
            return ChatActivityEnterView.this.searchToOpenProgress;
        }
    }

    @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
    /* renamed from: onStickerSelected */
    public void lambda$onStickerSelected$48(final TLRPC$Document tLRPC$Document, final String str, final Object obj, final MessageObject.SendAnimationData sendAnimationData, final boolean z, final boolean z2, final int i) {
        ChatActivity chatActivity;
        ChatActivity.ReplyQuote replyQuote = this.replyingQuote;
        if (replyQuote != null && (chatActivity = this.parentFragment) != null && replyQuote.outdated) {
            chatActivity.showQuoteMessageUpdate();
        } else if (isInScheduleMode() && i == 0) {
            AlertsCreator.createScheduleDatePickerDialog(this.parentActivity, this.parentFragment.getDialogId(), new AlertsCreator.ScheduleDatePickerDelegate() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda57
                @Override // org.telegram.ui.Components.AlertsCreator.ScheduleDatePickerDelegate
                public final void didSelectDate(boolean z3, int i2) {
                    ChatActivityEnterView.this.lambda$onStickerSelected$48(tLRPC$Document, str, obj, sendAnimationData, z, z3, i2);
                }
            }, this.resourcesProvider);
        } else {
            Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda53
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$onStickerSelected$49(tLRPC$Document, str, sendAnimationData, z2, i, obj, z);
                }
            };
            if (showConfirmAlert(runnable)) {
                return;
            }
            runnable.run();
        }
    }

    public /* synthetic */ void lambda$onStickerSelected$49(TLRPC$Document tLRPC$Document, String str, MessageObject.SendAnimationData sendAnimationData, boolean z, int i, Object obj, boolean z2) {
        if (this.slowModeTimer > 0 && !isInScheduleMode()) {
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                SimpleTextView simpleTextView = this.slowModeButton;
                chatActivityEnterViewDelegate.onUpdateSlowModeButton(simpleTextView, true, simpleTextView.getText());
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
        SendMessagesHelper.getInstance(this.currentAccount).sendSticker(tLRPC$Document, str, this.dialog_id, this.replyingMessageObject, getThreadMessage(), chatActivityEnterViewDelegate2 != null ? chatActivityEnterViewDelegate2.getReplyToStory() : null, this.replyingQuote, sendAnimationData, z, i, obj instanceof TLRPC$TL_messages_stickerSet, obj);
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate3 = this.delegate;
        if (chatActivityEnterViewDelegate3 != null) {
            chatActivityEnterViewDelegate3.onMessageSend(null, true, i);
        }
        if (z2) {
            setFieldText("");
        }
        MediaDataController.getInstance(this.currentAccount).addRecentSticker(0, obj, tLRPC$Document, (int) (System.currentTimeMillis() / 1000), false);
    }

    @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
    public boolean canSchedule() {
        ChatActivity chatActivity = this.parentFragment;
        return chatActivity != null && chatActivity.canScheduleMessage();
    }

    @Override // org.telegram.ui.Components.StickersAlert.StickersAlertDelegate
    public boolean isInScheduleMode() {
        ChatActivity chatActivity = this.parentFragment;
        return chatActivity != null && chatActivity.isInScheduleMode();
    }

    public void addStickerToRecent(TLRPC$Document tLRPC$Document) {
        createEmojiView();
        this.emojiView.addRecentSticker(tLRPC$Document);
    }

    public void showEmojiView() {
        showPopup(1, 0);
    }

    public void showPopup(int i, int i2) {
        showPopup(i, i2, true);
    }

    private void showPopup(final int i, int i2, boolean z) {
        boolean z2;
        int i3;
        EmojiView emojiView;
        int i4;
        int i5;
        if (i == 2) {
            return;
        }
        View view = null;
        if (i == 1) {
            if (i2 == 0) {
                if (this.parentActivity == null && this.emojiView == null) {
                    return;
                }
                createEmojiView();
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
                    i4 = 0;
                } else {
                    this.botKeyboardView.setVisibility(8);
                    this.botKeyboardViewVisible = false;
                    i4 = this.botKeyboardView.getMeasuredHeight();
                }
                this.emojiView.setShowing(true);
                view = this.emojiView;
                this.animatingContentType = 0;
            } else if (i2 == 1) {
                if (this.botKeyboardViewVisible) {
                    this.botKeyboardView.getVisibility();
                }
                this.botKeyboardViewVisible = true;
                EmojiView emojiView2 = this.emojiView;
                if (emojiView2 == null || emojiView2.getVisibility() == 8) {
                    i5 = 0;
                } else {
                    this.sizeNotifierLayout.removeView(this.emojiView);
                    this.emojiView.setVisibility(8);
                    this.emojiView.setShowing(false);
                    this.emojiViewVisible = false;
                    i5 = this.emojiView.getMeasuredHeight();
                }
                this.botKeyboardView.setVisibility(0);
                View view2 = this.botKeyboardView;
                this.animatingContentType = 1;
                MessagesController.getMainSettings(this.currentAccount).edit().remove("closed_botkeyboard_" + getTopicKeyString()).apply();
                i4 = i5;
                view = view2;
            } else {
                i4 = 0;
            }
            this.currentPopupContentType = i2;
            if (this.keyboardHeight <= 0) {
                this.keyboardHeight = MessagesController.getGlobalEmojiSettings().getInt("kbd_height", AndroidUtilities.dp(200.0f));
            }
            if (this.keyboardHeightLand <= 0) {
                this.keyboardHeightLand = MessagesController.getGlobalEmojiSettings().getInt("kbd_height_land3", AndroidUtilities.dp(200.0f));
            }
            android.graphics.Point point = AndroidUtilities.displaySize;
            int i6 = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
            if (i2 == 1) {
                i6 = Math.min(this.botKeyboardView.getKeyboardHeight(), i6);
            }
            BotKeyboardView botKeyboardView2 = this.botKeyboardView;
            if (botKeyboardView2 != null) {
                botKeyboardView2.setPanelHeight(i6);
            }
            if (view != null) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                layoutParams.height = i6;
                view.setLayoutParams(layoutParams);
            }
            if (!AndroidUtilities.isInMultiwindow) {
                AndroidUtilities.hideKeyboard(this.messageEditText);
            }
            SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
            if (sizeNotifierFrameLayout != null) {
                this.emojiPadding = i6;
                sizeNotifierFrameLayout.requestLayout();
                setEmojiButtonImage(true, true);
                updateBotButton(true);
                onWindowSizeChanged();
                if (this.smoothKeyboard && !this.keyboardVisible && i6 != i4 && z) {
                    final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda40
                        @Override // java.lang.Runnable
                        public final void run() {
                            ChatActivityEnterView.this.lambda$showPopup$50();
                        }
                    };
                    if (this.overrideKeyboardAnimation) {
                        this.animationEndRunnable = runnable;
                    } else {
                        this.panelAnimation = new AnimatorSet();
                        float f = i6 - i4;
                        view.setTranslationY(f);
                        this.panelAnimation.playTogether(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, f, 0.0f));
                        this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                        this.panelAnimation.setDuration(250L);
                        this.panelAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.69
                            {
                                ChatActivityEnterView.this = this;
                            }

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
            z2 = true;
        } else {
            if (this.emojiButton != null) {
                setEmojiButtonImage(false, true);
            }
            this.currentPopupContentType = -1;
            EmojiView emojiView3 = this.emojiView;
            if (emojiView3 != null) {
                if (i != 2 || AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
                    if (this.smoothKeyboard && !this.keyboardVisible && !this.stickersExpanded) {
                        this.emojiViewVisible = true;
                        this.animatingContentType = 0;
                        emojiView3.setShowing(false);
                        final Runnable runnable2 = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda48
                            @Override // java.lang.Runnable
                            public final void run() {
                                ChatActivityEnterView.this.lambda$showPopup$51(i);
                            }
                        };
                        if (!this.overrideKeyboardAnimation) {
                            AnimatorSet animatorSet = new AnimatorSet();
                            this.panelAnimation = animatorSet;
                            animatorSet.playTogether(ObjectAnimator.ofFloat(this.emojiView, View.TRANSLATION_Y, emojiView.getMeasuredHeight()));
                            this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                            this.panelAnimation.setDuration(250L);
                            this.notificationsLocker.lock();
                            this.panelAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.70
                                {
                                    ChatActivityEnterView.this = this;
                                }

                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public void onAnimationEnd(Animator animator) {
                                    runnable2.run();
                                    ChatActivityEnterView.this.notificationsLocker.unlock();
                                }
                            });
                        } else {
                            this.animationEndRunnable = runnable2;
                        }
                        AndroidUtilities.runOnUIThread(this.runEmojiPanelAnimation, 50L);
                        requestLayout();
                    } else {
                        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
                        if (chatActivityEnterViewDelegate != null) {
                            chatActivityEnterViewDelegate.bottomPanelTranslationYChanged(0.0f);
                        }
                        this.emojiPadding = 0;
                        this.sizeNotifierLayout.removeView(this.emojiView);
                        this.emojiView.setVisibility(8);
                        this.emojiView.setShowing(false);
                    }
                } else {
                    this.removeEmojiViewAfterAnimation = false;
                    ChatActivityEnterViewDelegate chatActivityEnterViewDelegate2 = this.delegate;
                    if (chatActivityEnterViewDelegate2 != null) {
                        chatActivityEnterViewDelegate2.bottomPanelTranslationYChanged(0.0f);
                    }
                    this.sizeNotifierLayout.removeView(this.emojiView);
                    this.emojiView = null;
                }
                this.emojiViewVisible = false;
            }
            BotKeyboardView botKeyboardView3 = this.botKeyboardView;
            if (botKeyboardView3 != null && botKeyboardView3.getVisibility() == 0) {
                if (i != 2 || AndroidUtilities.usingHardwareInput || AndroidUtilities.isInMultiwindow) {
                    if (this.smoothKeyboard && !this.keyboardVisible) {
                        if (this.botKeyboardViewVisible) {
                            i3 = 1;
                            this.animatingContentType = 1;
                        } else {
                            i3 = 1;
                        }
                        AnimatorSet animatorSet2 = new AnimatorSet();
                        this.panelAnimation = animatorSet2;
                        Animator[] animatorArr = new Animator[i3];
                        BotKeyboardView botKeyboardView4 = this.botKeyboardView;
                        Property property = View.TRANSLATION_Y;
                        float[] fArr = new float[i3];
                        fArr[0] = botKeyboardView4.getMeasuredHeight();
                        animatorArr[0] = ObjectAnimator.ofFloat(botKeyboardView4, property, fArr);
                        animatorSet2.playTogether(animatorArr);
                        this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                        this.panelAnimation.setDuration(250L);
                        this.panelAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.71
                            {
                                ChatActivityEnterView.this = this;
                            }

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
            z2 = true;
            updateBotButton(true);
        }
        if (this.stickersTabOpen || this.emojiTabOpen) {
            checkSendButton(z2);
        }
        if (this.stickersExpanded && i != z2) {
            setStickersExpanded(false, false, false);
        }
        updateFieldHint(false);
        checkBotMenu();
    }

    public /* synthetic */ void lambda$showPopup$50() {
        ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
        if (chatActivityEnterViewDelegate != null) {
            chatActivityEnterViewDelegate.bottomPanelTranslationYChanged(0.0f);
        }
        requestLayout();
    }

    public /* synthetic */ void lambda$showPopup$51(int i) {
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

    private void attachEmojiView() {
        if (this.emojiView.getParent() == null) {
            int childCount = this.sizeNotifierLayout.getChildCount() - 5;
            if (!this.shouldDrawBackground) {
                childCount = this.sizeNotifierLayout.getChildCount();
            }
            this.sizeNotifierLayout.addView(this.emojiView, childCount);
        }
    }

    private String getTopicKeyString() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null && chatActivity.isTopic) {
            return this.dialog_id + "_" + this.parentFragment.getTopicId();
        }
        return "" + this.dialog_id;
    }

    public void setEmojiButtonImage(boolean z, boolean z2) {
        int currentPage;
        ChatActivityEnterViewAnimatedIconView.State state;
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
        if (z && this.currentPopupContentType == 0) {
            if (!this.sendPlainEnabled) {
                return;
            }
            state = ChatActivityEnterViewAnimatedIconView.State.KEYBOARD;
        } else {
            EmojiView emojiView = this.emojiView;
            if (emojiView == null) {
                currentPage = MessagesController.getGlobalEmojiSettings().getInt("selected_page", 0);
            } else {
                currentPage = emojiView.getCurrentPage();
            }
            if (currentPage == 0 || (!this.allowStickers && !this.allowGifs)) {
                state = ChatActivityEnterViewAnimatedIconView.State.SMILE;
            } else {
                EditTextCaption editTextCaption = this.messageEditText;
                if (editTextCaption != null && !TextUtils.isEmpty(editTextCaption.getText())) {
                    state = ChatActivityEnterViewAnimatedIconView.State.SMILE;
                } else if (currentPage == 1) {
                    state = ChatActivityEnterViewAnimatedIconView.State.STICKER;
                } else {
                    state = ChatActivityEnterViewAnimatedIconView.State.GIF;
                }
            }
        }
        if (!this.sendPlainEnabled && state == ChatActivityEnterViewAnimatedIconView.State.SMILE) {
            state = ChatActivityEnterViewAnimatedIconView.State.GIF;
        } else if (!this.stickersEnabled && state != (state2 = ChatActivityEnterViewAnimatedIconView.State.SMILE)) {
            state = state2;
        }
        this.emojiButton.setState(state, z2);
        onEmojiIconChanged(state);
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

    public boolean hidePopup(boolean z) {
        return hidePopup(z, false);
    }

    public boolean hidePopup(boolean z, boolean z2) {
        TLRPC$TL_replyKeyboardMarkup tLRPC$TL_replyKeyboardMarkup;
        if (isPopupShowing()) {
            if (this.currentPopupContentType == 1 && (tLRPC$TL_replyKeyboardMarkup = this.botReplyMarkup) != null && z && this.botButtonsMessageObject != null) {
                if (tLRPC$TL_replyKeyboardMarkup.is_persistent) {
                    return false;
                }
                SharedPreferences.Editor edit = MessagesController.getMainSettings(this.currentAccount).edit();
                edit.putInt("closed_botkeyboard_" + getTopicKeyString(), this.botButtonsMessageObject.getId()).apply();
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
        return false;
    }

    public void setSearchingTypeInternal(int i, boolean z) {
        final boolean z2 = i != 0;
        if (z2 != (this.searchingType != 0)) {
            ValueAnimator valueAnimator = this.searchAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.searchAnimator.cancel();
            }
            if (!z) {
                this.searchToOpenProgress = z2 ? 1.0f : 0.0f;
                EmojiView emojiView = this.emojiView;
                if (emojiView != null) {
                    emojiView.searchProgressChanged();
                }
            } else {
                float[] fArr = new float[2];
                fArr[0] = this.searchToOpenProgress;
                fArr[1] = z2 ? 1.0f : 0.0f;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
                this.searchAnimator = ofFloat;
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda2
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                        ChatActivityEnterView.this.lambda$setSearchingTypeInternal$52(valueAnimator2);
                    }
                });
                this.searchAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.72
                    {
                        ChatActivityEnterView.this = this;
                    }

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
            }
        }
        this.searchingType = i;
    }

    public /* synthetic */ void lambda$setSearchingTypeInternal$52(ValueAnimator valueAnimator) {
        this.searchToOpenProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.searchProgressChanged();
        }
    }

    public void openKeyboardInternal() {
        ChatActivity chatActivity;
        if (hasBotWebView() && botCommandsMenuIsShowing()) {
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
        } else if (AndroidUtilities.usingHardwareInput || this.keyboardVisible || AndroidUtilities.isInMultiwindow) {
        } else {
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
    }

    public boolean isEditingMessage() {
        return this.editingMessageObject != null;
    }

    public MessageObject getEditingMessageObject() {
        return this.editingMessageObject;
    }

    public boolean isEditingCaption() {
        return this.editingCaption;
    }

    public boolean hasAudioToSend() {
        return (this.audioToSendMessageObject == null && this.videoToSendMessageObject == null) ? false : true;
    }

    public void openKeyboard() {
        if (hasBotWebView() && botCommandsMenuIsShowing()) {
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

    public void closeKeyboard() {
        AndroidUtilities.hideKeyboard(this.messageEditText);
    }

    public boolean isPopupShowing() {
        return this.emojiViewVisible || this.botKeyboardViewVisible;
    }

    public boolean closeCreationLinkDialog() {
        EditTextCaption editTextCaption = this.messageEditText;
        return editTextCaption != null && editTextCaption.closeCreationLinkDialog();
    }

    public boolean isKeyboardVisible() {
        return this.keyboardVisible;
    }

    public boolean isWaitingForKeyboard() {
        return this.waitingForKeyboardOpen;
    }

    public void addRecentGif(TLRPC$Document tLRPC$Document) {
        MediaDataController.getInstance(this.currentAccount).addRecentGif(tLRPC$Document, (int) (System.currentTimeMillis() / 1000), true);
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.addRecentGif(tLRPC$Document);
        }
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

    public boolean isStickersExpanded() {
        return this.stickersExpanded;
    }

    @Override // org.telegram.ui.Components.SizeNotifierFrameLayout.SizeNotifierFrameLayoutDelegate
    public void onSizeChanged(int i, boolean z) {
        MessageObject messageObject;
        EditTextCaption editTextCaption;
        TLRPC$TL_replyKeyboardMarkup tLRPC$TL_replyKeyboardMarkup;
        boolean z2;
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
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height_land3", this.keyboardHeightLand).commit();
            } else {
                this.keyboardHeight = i;
                MessagesController.getGlobalEmojiSettings().edit().putInt("kbd_height", this.keyboardHeight).commit();
            }
        }
        if (this.keyboardVisible && this.emojiViewVisible && this.emojiView == null) {
            this.emojiViewVisible = false;
        }
        if (isPopupShowing()) {
            int i2 = z ? this.keyboardHeightLand : this.keyboardHeight;
            if (this.currentPopupContentType == 1 && !this.botKeyboardView.isFullSize()) {
                i2 = Math.min(this.botKeyboardView.getKeyboardHeight(), i2);
            }
            View view = null;
            int i3 = this.currentPopupContentType;
            if (i3 == 0) {
                view = this.emojiView;
            } else if (i3 == 1) {
                view = this.botKeyboardView;
            }
            BotKeyboardView botKeyboardView = this.botKeyboardView;
            if (botKeyboardView != null) {
                botKeyboardView.setPanelHeight(i2);
            }
            if (view != null) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                if (!this.closeAnimationInProgress) {
                    int i4 = layoutParams.width;
                    int i5 = AndroidUtilities.displaySize.x;
                    if ((i4 != i5 || layoutParams.height != i2) && !this.stickersExpanded) {
                        layoutParams.width = i5;
                        layoutParams.height = i2;
                        view.setLayoutParams(layoutParams);
                        SizeNotifierFrameLayout sizeNotifierFrameLayout = this.sizeNotifierLayout;
                        if (sizeNotifierFrameLayout != null) {
                            int i6 = this.emojiPadding;
                            this.emojiPadding = layoutParams.height;
                            sizeNotifierFrameLayout.requestLayout();
                            onWindowSizeChanged();
                            if (this.smoothKeyboard && !this.keyboardVisible && i6 != this.emojiPadding && pannelAnimationEnabled()) {
                                AnimatorSet animatorSet = new AnimatorSet();
                                this.panelAnimation = animatorSet;
                                animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, this.emojiPadding - i6, 0.0f));
                                this.panelAnimation.setInterpolator(AdjustPanLayoutHelper.keyboardInterpolator);
                                this.panelAnimation.setDuration(250L);
                                this.panelAnimation.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.73
                                    {
                                        ChatActivityEnterView.this = this;
                                    }

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
        } else if (!this.keyboardVisible && !isPopupShowing() && (messageObject = this.botButtonsMessageObject) != null && this.replyingMessageObject != messageObject && ((!hasBotWebView() || !botCommandsMenuIsShowing()) && (((editTextCaption = this.messageEditText) == null || TextUtils.isEmpty(editTextCaption.getText())) && (tLRPC$TL_replyKeyboardMarkup = this.botReplyMarkup) != null && !tLRPC$TL_replyKeyboardMarkup.rows.isEmpty()))) {
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

    public void checkReactionsButton(boolean z) {
        AndroidUtilities.updateViewVisibilityAnimated(this.reactionsButton, z, 0.1f, true);
    }

    public int getEmojiPadding() {
        return this.emojiPadding;
    }

    public int getVisibleEmojiPadding() {
        if (this.emojiViewVisible) {
            return this.emojiPadding;
        }
        return 0;
    }

    public MessageObject getThreadMessage() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity != null) {
            return chatActivity.getThreadMessage();
        }
        return null;
    }

    public int getThreadMessageId() {
        ChatActivity chatActivity = this.parentFragment;
        if (chatActivity == null || chatActivity.getThreadMessage() == null) {
            return 0;
        }
        return this.parentFragment.getThreadMessage().getId();
    }

    @Override // org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        TLRPC$ChatFull tLRPC$ChatFull;
        TLRPC$Chat chat;
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
            }
        } else if (i == NotificationCenter.closeChats) {
            EditTextCaption editTextCaption2 = this.messageEditText;
            if (editTextCaption2 == null || !editTextCaption2.isFocused()) {
                return;
            }
            AndroidUtilities.hideKeyboard(this.messageEditText);
        } else {
            int i3 = 4;
            if (i == NotificationCenter.recordStartError || i == NotificationCenter.recordStopped) {
                if (((Integer) objArr[0]).intValue() != this.recordingGuid) {
                    return;
                }
                if (this.recordingAudioVideo) {
                    this.recordingAudioVideo = false;
                    if (i == NotificationCenter.recordStopped) {
                        Integer num = (Integer) objArr[1];
                        if (num.intValue() != 4) {
                            if (isInVideoMode() && num.intValue() == 5) {
                                i3 = 1;
                            } else if (num.intValue() == 0) {
                                i3 = 5;
                            } else {
                                i3 = num.intValue() == 6 ? 2 : 3;
                            }
                        }
                        if (i3 != 3) {
                            updateRecordInterface(i3);
                        }
                    } else {
                        updateRecordInterface(2);
                    }
                }
                if (i == NotificationCenter.recordStopped) {
                    Integer num2 = (Integer) objArr[1];
                }
            } else if (i == NotificationCenter.recordStarted) {
                if (((Integer) objArr[0]).intValue() != this.recordingGuid) {
                    return;
                }
                boolean booleanValue = ((Boolean) objArr[1]).booleanValue();
                this.isInVideoMode = !booleanValue;
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView = this.audioVideoSendButton;
                if (chatActivityEnterViewAnimatedIconView != null) {
                    chatActivityEnterViewAnimatedIconView.setState(booleanValue ? ChatActivityEnterViewAnimatedIconView.State.VOICE : ChatActivityEnterViewAnimatedIconView.State.VIDEO, true);
                }
                if (!this.recordingAudioVideo) {
                    this.recordingAudioVideo = true;
                    updateRecordInterface(0);
                } else {
                    RecordCircle recordCircle2 = this.recordCircle;
                    if (recordCircle2 != null) {
                        recordCircle2.showWaves(true, true);
                    }
                }
                TimerView timerView = this.recordTimerView;
                if (timerView != null) {
                    timerView.start();
                }
                RecordDot recordDot = this.recordDot;
                if (recordDot != null) {
                    recordDot.enterAnimation = false;
                }
            } else if (i == NotificationCenter.audioDidSent) {
                if (((Integer) objArr[0]).intValue() != this.recordingGuid) {
                    return;
                }
                Object obj = objArr[1];
                if (obj instanceof VideoEditedInfo) {
                    this.videoToSendMessageObject = (VideoEditedInfo) obj;
                    String str = (String) objArr[2];
                    this.audioToSendPath = str;
                    ArrayList<Bitmap> arrayList = (ArrayList) objArr[3];
                    VideoTimelineView videoTimelineView = this.videoTimelineView;
                    if (videoTimelineView != null) {
                        videoTimelineView.setVideoPath(str);
                        this.videoTimelineView.setKeyframes(arrayList);
                        this.videoTimelineView.setVisibility(0);
                        this.videoTimelineView.setMinProgressDiff(1000.0f / ((float) this.videoToSendMessageObject.estimatedDuration));
                    }
                    updateRecordInterface(3);
                    checkSendButton(false);
                    return;
                }
                TLRPC$TL_document tLRPC$TL_document = (TLRPC$TL_document) objArr[1];
                this.audioToSend = tLRPC$TL_document;
                this.audioToSendPath = (String) objArr[2];
                if (tLRPC$TL_document != null) {
                    createRecordAudioPanel();
                    if (this.recordedAudioPanel == null) {
                        return;
                    }
                    TLRPC$TL_message tLRPC$TL_message = new TLRPC$TL_message();
                    tLRPC$TL_message.out = true;
                    tLRPC$TL_message.id = 0;
                    tLRPC$TL_message.peer_id = new TLRPC$TL_peerUser();
                    TLRPC$TL_peerUser tLRPC$TL_peerUser = new TLRPC$TL_peerUser();
                    tLRPC$TL_message.from_id = tLRPC$TL_peerUser;
                    TLRPC$Peer tLRPC$Peer = tLRPC$TL_message.peer_id;
                    long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                    tLRPC$TL_peerUser.user_id = clientUserId;
                    tLRPC$Peer.user_id = clientUserId;
                    tLRPC$TL_message.date = (int) (System.currentTimeMillis() / 1000);
                    tLRPC$TL_message.message = "";
                    tLRPC$TL_message.attachPath = this.audioToSendPath;
                    TLRPC$TL_messageMediaDocument tLRPC$TL_messageMediaDocument = new TLRPC$TL_messageMediaDocument();
                    tLRPC$TL_message.media = tLRPC$TL_messageMediaDocument;
                    tLRPC$TL_messageMediaDocument.flags |= 3;
                    tLRPC$TL_messageMediaDocument.document = this.audioToSend;
                    tLRPC$TL_message.flags |= 768;
                    this.audioToSendMessageObject = new MessageObject(UserConfig.selectedAccount, tLRPC$TL_message, false, true);
                    this.recordedAudioPanel.setAlpha(1.0f);
                    this.recordedAudioPanel.setVisibility(0);
                    this.recordDeleteImageView.setVisibility(0);
                    this.recordDeleteImageView.setAlpha(0.0f);
                    this.recordDeleteImageView.setScaleY(0.0f);
                    this.recordDeleteImageView.setScaleX(0.0f);
                    double d = 0.0d;
                    int i4 = 0;
                    while (true) {
                        if (i4 >= this.audioToSend.attributes.size()) {
                            break;
                        }
                        TLRPC$DocumentAttribute tLRPC$DocumentAttribute = this.audioToSend.attributes.get(i4);
                        if (tLRPC$DocumentAttribute instanceof TLRPC$TL_documentAttributeAudio) {
                            d = tLRPC$DocumentAttribute.duration;
                            break;
                        }
                        i4++;
                    }
                    int i5 = 0;
                    while (true) {
                        if (i5 >= this.audioToSend.attributes.size()) {
                            break;
                        }
                        TLRPC$DocumentAttribute tLRPC$DocumentAttribute2 = this.audioToSend.attributes.get(i5);
                        if (tLRPC$DocumentAttribute2 instanceof TLRPC$TL_documentAttributeAudio) {
                            byte[] bArr = tLRPC$DocumentAttribute2.waveform;
                            if (bArr == null || bArr.length == 0) {
                                tLRPC$DocumentAttribute2.waveform = MediaController.getWaveform(this.audioToSendPath);
                            }
                            this.recordedAudioSeekBar.setWaveform(tLRPC$DocumentAttribute2.waveform);
                        } else {
                            i5++;
                        }
                    }
                    this.recordedAudioTimeTextView.setText(AndroidUtilities.formatShortDuration((int) d));
                    checkSendButton(false);
                    updateRecordInterface(3);
                    return;
                }
                ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
                if (chatActivityEnterViewDelegate != null) {
                    chatActivityEnterViewDelegate.onMessageSend(null, true, 0);
                }
            } else if (i == NotificationCenter.audioRouteChanged) {
                if (this.parentActivity != null) {
                    this.parentActivity.setVolumeControlStream(((Boolean) objArr[0]).booleanValue() ? 0 : Integer.MIN_VALUE);
                }
            } else if (i == NotificationCenter.messagePlayingDidReset) {
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
                }
            } else if (i == NotificationCenter.messagePlayingProgressDidChanged) {
                Integer num3 = (Integer) objArr[0];
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
            } else if (i == NotificationCenter.featuredStickersDidLoad) {
                ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView2 = this.emojiButton;
                if (chatActivityEnterViewAnimatedIconView2 != null) {
                    chatActivityEnterViewAnimatedIconView2.invalidate();
                }
            } else if (i == NotificationCenter.messageReceivedByServer) {
                if (((Boolean) objArr[6]).booleanValue() || ((Long) objArr[3]).longValue() != this.dialog_id || (tLRPC$ChatFull = this.info) == null || tLRPC$ChatFull.slowmode_seconds == 0 || (chat = this.accountInstance.getMessagesController().getChat(Long.valueOf(this.info.id))) == null || ChatObject.hasAdminRights(chat)) {
                    return;
                }
                TLRPC$ChatFull tLRPC$ChatFull2 = this.info;
                int currentTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                TLRPC$ChatFull tLRPC$ChatFull3 = this.info;
                tLRPC$ChatFull2.slowmode_next_send_date = currentTime + tLRPC$ChatFull3.slowmode_seconds;
                tLRPC$ChatFull3.flags |= 262144;
                setSlowModeTimer(tLRPC$ChatFull3.slowmode_next_send_date);
            } else if (i == NotificationCenter.sendingMessagesChanged) {
                if (this.info != null) {
                    updateSlowModeText();
                }
            } else if (i == NotificationCenter.audioRecordTooShort) {
                updateRecordInterface(4);
            } else if (i == NotificationCenter.updateBotMenuButton) {
                long longValue = ((Long) objArr[0]).longValue();
                TLRPC$BotMenuButton tLRPC$BotMenuButton = (TLRPC$BotMenuButton) objArr[1];
                if (longValue == this.dialog_id) {
                    if (tLRPC$BotMenuButton instanceof TLRPC$TL_botMenuButton) {
                        TLRPC$TL_botMenuButton tLRPC$TL_botMenuButton = (TLRPC$TL_botMenuButton) tLRPC$BotMenuButton;
                        this.botMenuWebViewTitle = tLRPC$TL_botMenuButton.text;
                        this.botMenuWebViewUrl = tLRPC$TL_botMenuButton.url;
                        this.botMenuButtonType = BotMenuButtonType.WEB_VIEW;
                    } else if (this.hasBotCommands) {
                        this.botMenuButtonType = BotMenuButtonType.COMMANDS;
                    } else {
                        this.botMenuButtonType = BotMenuButtonType.NO_BUTTON;
                    }
                    updateBotButton(false);
                }
            } else if (i == NotificationCenter.didUpdatePremiumGiftFieldIcon) {
                updateGiftButton(true);
            }
        }
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
            final Runnable runnable = new Runnable() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda41
                @Override // java.lang.Runnable
                public final void run() {
                    ChatActivityEnterView.this.lambda$checkStickresExpandHeight$53();
                }
            };
            this.emojiView.setLayerType(2, null);
            if (this.overrideKeyboardAnimation) {
                this.animationEndRunnable = runnable;
                return;
            }
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofInt(this, (Property<ChatActivityEnterView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)), ObjectAnimator.ofInt(this.emojiView, (Property<EmojiView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)));
            ((ObjectAnimator) animatorSet.getChildAnimations().get(0)).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ChatActivityEnterView.this.lambda$checkStickresExpandHeight$54(valueAnimator);
                }
            });
            animatorSet.setDuration(300L);
            animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.74
                {
                    ChatActivityEnterView.this = this;
                }

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
        ((ObjectAnimator) animatorSet2.getChildAnimations().get(0)).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ChatActivityEnterView.this.lambda$checkStickresExpandHeight$55(valueAnimator);
            }
        });
        animatorSet2.setDuration(300L);
        animatorSet2.setInterpolator(CubicBezierInterpolator.DEFAULT);
        animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.75
            {
                ChatActivityEnterView.this = this;
            }

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

    public /* synthetic */ void lambda$checkStickresExpandHeight$53() {
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.getLayoutParams().height = this.stickersExpandedHeight;
            this.emojiView.setLayerType(0, null);
        }
    }

    public /* synthetic */ void lambda$checkStickresExpandHeight$54(ValueAnimator valueAnimator) {
        this.sizeNotifierLayout.invalidate();
    }

    public /* synthetic */ void lambda$checkStickresExpandHeight$55(ValueAnimator valueAnimator) {
        this.sizeNotifierLayout.invalidate();
    }

    public void setStickersExpanded(boolean z, boolean z2, boolean z3) {
        setStickersExpanded(z, z2, z3, true);
    }

    public void setStickersExpanded(boolean z, boolean z2, boolean z3, boolean z4) {
        AdjustPanLayoutHelper adjustPanLayoutHelper = this.adjustPanLayoutHelper;
        if ((adjustPanLayoutHelper != null && adjustPanLayoutHelper.animationInProgress()) || this.waitingForKeyboardOpenAfterAnimation || this.emojiView == null) {
            return;
        }
        if (z3 || this.stickersExpanded != z) {
            this.stickersExpanded = z;
            ChatActivityEnterViewDelegate chatActivityEnterViewDelegate = this.delegate;
            if (chatActivityEnterViewDelegate != null) {
                chatActivityEnterViewDelegate.onStickersExpandedChange();
            }
            android.graphics.Point point = AndroidUtilities.displaySize;
            final int i = point.x > point.y ? this.keyboardHeightLand : this.keyboardHeight;
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
                    this.stickersExpandedHeight = Math.min(currentActionBarHeight, AndroidUtilities.dp(120.0f) + i);
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
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(ObjectAnimator.ofInt(this, (Property<ChatActivityEnterView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)), ObjectAnimator.ofInt(this.emojiView, (Property<EmojiView, Integer>) this.roundedTranslationYProperty, -(this.stickersExpandedHeight - i)), ObjectAnimator.ofFloat(this.stickersArrow, "animationProgress", 1.0f));
                    animatorSet.setDuration(300L);
                    animatorSet.setInterpolator(CubicBezierInterpolator.DEFAULT);
                    ((ObjectAnimator) animatorSet.getChildAnimations().get(0)).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda7
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            ChatActivityEnterView.this.lambda$setStickersExpanded$56(i, valueAnimator);
                        }
                    });
                    animatorSet.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.76
                        {
                            ChatActivityEnterView.this = this;
                        }

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
                } else {
                    this.stickersExpansionProgress = 1.0f;
                    setTranslationY(-(this.stickersExpandedHeight - i));
                    this.emojiView.setTranslationY(-(this.stickersExpandedHeight - i));
                    this.stickersArrow.setAnimationProgress(1.0f);
                }
            } else {
                if (z4) {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(NotificationCenter.startAllHeavyOperations, 1);
                }
                if (z2) {
                    this.closeAnimationInProgress = true;
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    animatorSet2.playTogether(ObjectAnimator.ofInt(this, (Property<ChatActivityEnterView, Integer>) this.roundedTranslationYProperty, 0), ObjectAnimator.ofInt(this.emojiView, (Property<EmojiView, Integer>) this.roundedTranslationYProperty, 0), ObjectAnimator.ofFloat(this.stickersArrow, "animationProgress", 0.0f));
                    animatorSet2.setDuration(300L);
                    animatorSet2.setInterpolator(CubicBezierInterpolator.DEFAULT);
                    ((ObjectAnimator) animatorSet2.getChildAnimations().get(0)).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Components.ChatActivityEnterView$$ExternalSyntheticLambda6
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                            ChatActivityEnterView.this.lambda$setStickersExpanded$57(i, valueAnimator);
                        }
                    });
                    animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Components.ChatActivityEnterView.77
                        {
                            ChatActivityEnterView.this = this;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator2) {
                            ChatActivityEnterView.this.closeAnimationInProgress = false;
                            ChatActivityEnterView.this.stickersExpansionAnim = null;
                            if (ChatActivityEnterView.this.emojiView != null) {
                                ChatActivityEnterView.this.emojiView.getLayoutParams().height = i;
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
                    this.stickersExpansionAnim = animatorSet2;
                    this.emojiView.setLayerType(2, null);
                    this.notificationsLocker.lock();
                    animatorSet2.start();
                } else {
                    this.stickersExpansionProgress = 0.0f;
                    setTranslationY(0.0f);
                    this.emojiView.setTranslationY(0.0f);
                    this.emojiView.getLayoutParams().height = i;
                    this.sizeNotifierLayout.requestLayout();
                    this.sizeNotifierLayout.setForeground(null);
                    this.sizeNotifierLayout.setWillNotDraw(false);
                    this.stickersArrow.setAnimationProgress(0.0f);
                }
            }
            ImageView imageView = this.expandStickersButton;
            if (imageView != null) {
                if (this.stickersExpanded) {
                    imageView.setContentDescription(LocaleController.getString("AccDescrCollapsePanel", R.string.AccDescrCollapsePanel));
                } else {
                    imageView.setContentDescription(LocaleController.getString("AccDescrExpandPanel", R.string.AccDescrExpandPanel));
                }
            }
        }
    }

    public /* synthetic */ void lambda$setStickersExpanded$56(int i, ValueAnimator valueAnimator) {
        this.stickersExpansionProgress = Math.abs(getTranslationY() / (-(this.stickersExpandedHeight - i)));
        this.sizeNotifierLayout.invalidate();
    }

    public /* synthetic */ void lambda$setStickersExpanded$57(int i, ValueAnimator valueAnimator) {
        this.stickersExpansionProgress = getTranslationY() / (-(this.stickersExpandedHeight - i));
        this.sizeNotifierLayout.invalidate();
    }

    public boolean swipeToBackEnabled() {
        FrameLayout frameLayout;
        if (this.recordingAudioVideo) {
            return false;
        }
        if (isInVideoMode() && (frameLayout = this.recordedAudioPanel) != null && frameLayout.getVisibility() == 0) {
            return false;
        }
        return (hasBotWebView() && this.botCommandsMenuButton.isOpened()) ? false : true;
    }

    public int getHeightWithTopView() {
        int measuredHeight = getMeasuredHeight();
        View view = this.topView;
        return (view == null || view.getVisibility() != 0) ? measuredHeight : (int) (measuredHeight - ((1.0f - this.topViewEnterProgress) * this.topView.getLayoutParams().height));
    }

    public void setAdjustPanLayoutHelper(AdjustPanLayoutHelper adjustPanLayoutHelper) {
        this.adjustPanLayoutHelper = adjustPanLayoutHelper;
    }

    public AdjustPanLayoutHelper getAdjustPanLayoutHelper() {
        return this.adjustPanLayoutHelper;
    }

    public boolean panelAnimationInProgress() {
        return this.panelAnimation != null;
    }

    public float getTopViewTranslation() {
        View view = this.topView;
        if (view == null || view.getVisibility() == 8) {
            return 0.0f;
        }
        return this.topView.getTranslationY();
    }

    public int getAnimatedTop() {
        return this.animatedTop;
    }

    /* loaded from: classes4.dex */
    public class ScrimDrawable extends Drawable {
        private Paint paint;

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

        public ScrimDrawable() {
            ChatActivityEnterView.this = r2;
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
    }

    /* loaded from: classes4.dex */
    public class SlideTextView extends View {
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
            if (z) {
                if (motionEvent.getAction() == 2 && !this.cancelRect.contains(x, y)) {
                    setPressed(false);
                    return false;
                }
                if (motionEvent.getAction() == 1 && this.cancelRect.contains(x, y)) {
                    onCancelButtonPressed();
                }
                return true;
            }
            return z;
        }

        public void onCancelButtonPressed() {
            if (!ChatActivityEnterView.this.hasRecordVideo || !ChatActivityEnterView.this.isInVideoMode()) {
                ChatActivityEnterView.this.delegate.needStartRecordAudio(0);
                MediaController.getInstance().stopRecording(0, false, 0);
            } else {
                CameraController.getInstance().cancelOnInitRunnable(ChatActivityEnterView.this.onFinishInitCameraRunnable);
                ChatActivityEnterView.this.delegate.needStartRecordVideo(5, true, 0);
            }
            ChatActivityEnterView.this.recordingAudioVideo = false;
            ChatActivityEnterView.this.updateRecordInterface(2);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SlideTextView(Context context) {
            super(context);
            ChatActivityEnterView.this = r5;
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
            this.bluePaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
            this.arrowPaint.setColor(r5.getThemedColor(Theme.key_chat_messagePanelIcons));
            this.arrowPaint.setStyle(Paint.Style.STROKE);
            this.arrowPaint.setStrokeWidth(AndroidUtilities.dpf2(this.smallSize ? 1.0f : 1.6f));
            this.arrowPaint.setStrokeCap(Paint.Cap.ROUND);
            this.arrowPaint.setStrokeJoin(Paint.Join.ROUND);
            this.slideToCancelString = LocaleController.getString("SlideToCancel", R.string.SlideToCancel);
            this.slideToCancelString = this.slideToCancelString.charAt(0) + this.slideToCancelString.substring(1).toLowerCase();
            String upperCase = LocaleController.getString("Cancel", R.string.Cancel).toUpperCase();
            this.cancelString = upperCase;
            this.cancelCharOffset = this.slideToCancelString.indexOf(upperCase);
            updateColors();
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
        protected void drawableStateChanged() {
            super.drawableStateChanged();
            this.selectableBackground.setState(getDrawableState());
        }

        @Override // android.view.View
        public boolean verifyDrawable(Drawable drawable) {
            return this.selectableBackground == drawable || super.verifyDrawable(drawable);
        }

        @Override // android.view.View
        public void jumpDrawablesToCurrentState() {
            super.jumpDrawablesToCurrentState();
            Drawable drawable = this.selectableBackground;
            if (drawable != null) {
                drawable.jumpToCurrentState();
            }
        }

        @Override // android.view.View
        @SuppressLint({"DrawAllocation"})
        protected void onMeasure(int i, int i2) {
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
                    float f = measuredHeight2;
                    this.arrowPath.setLastPoint(AndroidUtilities.dpf2(2.5f), f - AndroidUtilities.dpf2(3.12f));
                    this.arrowPath.lineTo(0.0f, f);
                    this.arrowPath.lineTo(AndroidUtilities.dpf2(2.5f), f + AndroidUtilities.dpf2(3.12f));
                } else {
                    float f2 = measuredHeight2;
                    this.arrowPath.setLastPoint(AndroidUtilities.dpf2(4.0f), f2 - AndroidUtilities.dpf2(5.0f));
                    this.arrowPath.lineTo(0.0f, f2);
                    this.arrowPath.lineTo(AndroidUtilities.dpf2(4.0f), f2 + AndroidUtilities.dpf2(5.0f));
                }
                this.slideToLayout = new StaticLayout(this.slideToCancelString, this.grayPaint, (int) this.slideToCancelWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.cancelLayout = new StaticLayout(this.cancelString, this.bluePaint, (int) this.cancelWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            StaticLayout staticLayout;
            if (this.slideToLayout == null || (staticLayout = this.cancelLayout) == null) {
                return;
            }
            int width = staticLayout.getWidth() + AndroidUtilities.dp(16.0f);
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
                        float dp = this.xOffset + ((AndroidUtilities.dp(3.0f) / 250.0f) * ((float) currentTimeMillis));
                        this.xOffset = dp;
                        if (dp > AndroidUtilities.dp(6.0f)) {
                            this.xOffset = AndroidUtilities.dp(6.0f);
                            this.moveForward = false;
                        }
                    } else {
                        float dp2 = this.xOffset - ((AndroidUtilities.dp(3.0f) / 250.0f) * ((float) currentTimeMillis));
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

        @Keep
        public void setCancelToProgress(float f) {
            this.cancelToProgress = f;
        }

        @Keep
        public float getSlideToCancelWidth() {
            return this.slideToCancelWidth;
        }

        public void setSlideX(float f) {
            this.slideProgress = f;
        }
    }

    /* loaded from: classes4.dex */
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

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TimerView(Context context) {
            super(context);
            ChatActivityEnterView.this = r1;
            this.replaceIn = new SpannableStringBuilder();
            this.replaceOut = new SpannableStringBuilder();
            this.replaceStable = new SpannableStringBuilder();
            this.replaceDistance = AndroidUtilities.dp(15.0f);
        }

        public void start() {
            this.isRunning = true;
            long currentTimeMillis = System.currentTimeMillis();
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

        @Override // android.view.View
        @SuppressLint({"DrawAllocation"})
        protected void onDraw(Canvas canvas) {
            SpannableStringBuilder spannableStringBuilder;
            String str;
            if (this.textPaint == null) {
                TextPaint textPaint = new TextPaint(1);
                this.textPaint = textPaint;
                textPaint.setTextSize(AndroidUtilities.dp(15.0f));
                this.textPaint.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
                this.textPaint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordTime));
            }
            long currentTimeMillis = System.currentTimeMillis();
            long j = this.isRunning ? currentTimeMillis - this.startTime : this.stopTime - this.startTime;
            long j2 = j / 1000;
            int i = ((int) (j % 1000)) / 10;
            if (ChatActivityEnterView.this.isInVideoMode() && j >= 59500 && !this.stoppedInternal) {
                ChatActivityEnterView.this.startedDraggingX = -1.0f;
                ChatActivityEnterView.this.delegate.needStartRecordVideo(3, true, 0);
                this.stoppedInternal = true;
            }
            if (this.isRunning && currentTimeMillis > this.lastSendTypingTime + 5000) {
                this.lastSendTypingTime = currentTimeMillis;
                MessagesController.getInstance(ChatActivityEnterView.this.currentAccount).sendTyping(ChatActivityEnterView.this.dialog_id, ChatActivityEnterView.this.getThreadMessageId(), ChatActivityEnterView.this.isInVideoMode() ? 7 : 1, 0);
            }
            String formatTimerDurationFast = AndroidUtilities.formatTimerDurationFast((int) j2, i);
            if (formatTimerDurationFast.length() >= 3 && (str = this.oldString) != null && str.length() >= 3 && formatTimerDurationFast.length() == this.oldString.length() && formatTimerDurationFast.charAt(formatTimerDurationFast.length() - 3) != this.oldString.charAt(formatTimerDurationFast.length() - 3)) {
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
                this.inLayout = new StaticLayout(this.replaceIn, this.textPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.outLayout = new StaticLayout(this.replaceOut, this.textPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.replaceTransition = 1.0f;
            } else {
                if (this.replaceStable == null) {
                    this.replaceStable = new SpannableStringBuilder(formatTimerDurationFast);
                }
                if (this.replaceStable.length() == 0 || this.replaceStable.length() != formatTimerDurationFast.length()) {
                    this.replaceStable.clear();
                    this.replaceStable.append((CharSequence) formatTimerDurationFast);
                } else {
                    this.replaceStable.replace(spannableStringBuilder.length() - 1, this.replaceStable.length(), (CharSequence) formatTimerDurationFast, (formatTimerDurationFast.length() - 1) - (formatTimerDurationFast.length() - this.replaceStable.length()), formatTimerDurationFast.length());
                }
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
                this.left = staticLayout.getLineWidth(0) + 0.0f;
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
                this.textPaint.setAlpha(255);
                StaticLayout staticLayout2 = new StaticLayout(this.replaceStable, this.textPaint, getMeasuredWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                canvas.translate(0.0f, measuredHeight - (staticLayout2.getHeight() / 2.0f));
                staticLayout2.draw(canvas);
                canvas.restore();
                this.left = staticLayout2.getLineWidth(0) + 0.0f;
            }
            this.oldString = formatTimerDurationFast;
            if (this.isRunning || this.replaceTransition != 0.0f) {
                invalidate();
            }
        }

        public void updateColors() {
            TextPaint textPaint = this.textPaint;
            if (textPaint != null) {
                textPaint.setColor(ChatActivityEnterView.this.getThemedColor(Theme.key_chat_recordTime));
            }
        }

        public float getLeftProperty() {
            return this.left;
        }

        public void reset() {
            this.isRunning = false;
            this.startTime = 0L;
            this.stopTime = 0L;
            this.stoppedInternal = false;
        }
    }

    public RecordCircle getRecordCircle() {
        return this.recordCircle;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        if (botCommandsMenuView != null && botCommandsMenuView.getTag() != null) {
            this.botCommandsMenuButton.measure(i, i2);
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.emojiButton.getLayoutParams();
            int dp = AndroidUtilities.dp(10.0f);
            BotCommandsMenuView botCommandsMenuView2 = this.botCommandsMenuButton;
            marginLayoutParams.leftMargin = dp + (botCommandsMenuView2 == null ? 0 : botCommandsMenuView2.getMeasuredWidth());
            EditTextCaption editTextCaption = this.messageEditText;
            if (editTextCaption != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) editTextCaption.getLayoutParams();
                int dp2 = AndroidUtilities.dp(57.0f);
                BotCommandsMenuView botCommandsMenuView3 = this.botCommandsMenuButton;
                marginLayoutParams2.leftMargin = dp2 + (botCommandsMenuView3 == null ? 0 : botCommandsMenuView3.getMeasuredWidth());
            }
        } else {
            SenderSelectView senderSelectView = this.senderSelectView;
            if (senderSelectView != null && senderSelectView.getVisibility() == 0) {
                int i3 = this.senderSelectView.getLayoutParams().width;
                this.senderSelectView.measure(View.MeasureSpec.makeMeasureSpec(i3, 1073741824), View.MeasureSpec.makeMeasureSpec(this.senderSelectView.getLayoutParams().height, 1073741824));
                ((ViewGroup.MarginLayoutParams) this.emojiButton.getLayoutParams()).leftMargin = AndroidUtilities.dp(16.0f) + i3;
                EditTextCaption editTextCaption2 = this.messageEditText;
                if (editTextCaption2 != null) {
                    ((ViewGroup.MarginLayoutParams) editTextCaption2.getLayoutParams()).leftMargin = AndroidUtilities.dp(63.0f) + i3;
                }
            } else {
                ((ViewGroup.MarginLayoutParams) this.emojiButton.getLayoutParams()).leftMargin = AndroidUtilities.dp(3.0f);
                EditTextCaption editTextCaption3 = this.messageEditText;
                if (editTextCaption3 != null) {
                    ((ViewGroup.MarginLayoutParams) editTextCaption3.getLayoutParams()).leftMargin = AndroidUtilities.dp(50.0f);
                }
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
        BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
        if (botWebViewMenuContainer != null) {
            ViewGroup.MarginLayoutParams marginLayoutParams3 = (ViewGroup.MarginLayoutParams) botWebViewMenuContainer.getLayoutParams();
            EditTextCaption editTextCaption4 = this.messageEditText;
            marginLayoutParams3.bottomMargin = editTextCaption4 != null ? editTextCaption4.getMeasuredHeight() : 0;
            measureChild(this.botWebViewMenuContainer, i, i2);
        }
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

    private void beginDelayedTransition() {
        HashMap<View, Float> hashMap = this.animationParamsX;
        ChatActivityEnterViewAnimatedIconView chatActivityEnterViewAnimatedIconView = this.emojiButton;
        hashMap.put(chatActivityEnterViewAnimatedIconView, Float.valueOf(chatActivityEnterViewAnimatedIconView.getX()));
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            this.animationParamsX.put(editTextCaption, Float.valueOf(editTextCaption.getX()));
        }
    }

    public void setBotInfo(LongSparseArray<TLRPC$BotInfo> longSparseArray) {
        setBotInfo(longSparseArray, true);
    }

    public void setBotInfo(LongSparseArray<TLRPC$BotInfo> longSparseArray, boolean z) {
        this.lastBotInfo = longSparseArray;
        if (longSparseArray.size() == 1 && longSparseArray.valueAt(0).user_id == this.dialog_id) {
            TLRPC$BotInfo valueAt = longSparseArray.valueAt(0);
            TLRPC$BotMenuButton tLRPC$BotMenuButton = valueAt.menu_button;
            if (tLRPC$BotMenuButton instanceof TLRPC$TL_botMenuButton) {
                TLRPC$TL_botMenuButton tLRPC$TL_botMenuButton = (TLRPC$TL_botMenuButton) tLRPC$BotMenuButton;
                this.botMenuWebViewTitle = tLRPC$TL_botMenuButton.text;
                this.botMenuWebViewUrl = tLRPC$TL_botMenuButton.url;
                this.botMenuButtonType = BotMenuButtonType.WEB_VIEW;
            } else if (!valueAt.commands.isEmpty()) {
                this.botMenuButtonType = BotMenuButtonType.COMMANDS;
            } else {
                this.botMenuButtonType = BotMenuButtonType.NO_BUTTON;
            }
        } else {
            this.botMenuButtonType = BotMenuButtonType.NO_BUTTON;
        }
        BotCommandsMenuView.BotCommandsAdapter botCommandsAdapter = this.botCommandsAdapter;
        if (botCommandsAdapter != null) {
            botCommandsAdapter.setBotInfo(longSparseArray);
        }
        updateBotButton(z);
    }

    public boolean botCommandsMenuIsShowing() {
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        return botCommandsMenuView != null && botCommandsMenuView.isOpened();
    }

    public void hideBotCommands() {
        BotCommandsMenuView botCommandsMenuView = this.botCommandsMenuButton;
        if (botCommandsMenuView != null) {
            botCommandsMenuView.setOpened(false);
        }
        if (hasBotWebView()) {
            BotWebViewMenuContainer botWebViewMenuContainer = this.botWebViewMenuContainer;
            if (botWebViewMenuContainer != null) {
                botWebViewMenuContainer.dismiss();
                return;
            }
            return;
        }
        BotCommandsMenuContainer botCommandsMenuContainer = this.botCommandsMenuContainer;
        if (botCommandsMenuContainer != null) {
            botCommandsMenuContainer.dismiss();
        }
    }

    public void setTextTransitionIsRunning(boolean z) {
        this.textTransitionIsRunning = z;
        this.sendButtonContainer.invalidate();
    }

    public float getTopViewHeight() {
        View view = this.topView;
        if (view == null || view.getVisibility() != 0) {
            return 0.0f;
        }
        return this.topView.getLayoutParams().height;
    }

    public void runEmojiPanelAnimation() {
        AndroidUtilities.cancelRunOnUIThread(this.runEmojiPanelAnimation);
        this.runEmojiPanelAnimation.run();
    }

    public Drawable getStickersArrowDrawable() {
        return this.stickersArrow;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.BlurredFrameLayout, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
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

    public int getThemedColor(int i) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        if (resourcesProvider != null) {
            return resourcesProvider.getColor(i);
        }
        return Theme.getColor(i);
    }

    private Paint getThemedPaint(String str) {
        Theme.ResourcesProvider resourcesProvider = this.resourcesProvider;
        Paint paint = resourcesProvider != null ? resourcesProvider.getPaint(str) : null;
        return paint != null ? paint : Theme.getThemePaint(str);
    }

    public void setChatSearchExpandOffset(float f) {
        this.chatSearchExpandOffset = f;
        invalidate();
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
        boolean z2 = true;
        if (z && f2 != 1.0f) {
            z2 = false;
        }
        this.sendButtonEnabled = z2;
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
            EditTextCaption editTextCaption = this.messageEditText;
            editTextCaption.setPivotY(editTextCaption.getMeasuredHeight() / 2.0f);
            this.messageEditText.setScaleX(lerp);
            this.messageEditText.setScaleY(lerp);
            this.messageEditText.setHintRightOffset(AndroidUtilities.lerp(AndroidUtilities.dp(30.0f), 0, f2));
        }
    }

    public void updateMessageTextParams() {
        EditTextCaption editTextCaption = this.messageEditText;
        if (editTextCaption != null) {
            editTextCaption.setTranslationX(this.messageTextPaddingTranslationX + this.messageTextTranslationX);
        }
    }

    public void updateAttachLayoutParams() {
        LinearLayout linearLayout = this.attachLayout;
        if (linearLayout != null) {
            linearLayout.setTranslationX(this.attachLayoutPaddingTranslationX + this.attachLayoutTranslationX);
            this.attachLayout.setAlpha(this.attachLayoutAlpha * this.attachLayoutPaddingAlpha);
            LinearLayout linearLayout2 = this.attachLayout;
            linearLayout2.setVisibility(linearLayout2.getAlpha() > 0.0f ? 0 : 8);
        }
    }

    public void updateEmojiButtonParams() {
        this.emojiButton.setScaleX(this.emojiButtonPaddingScale * this.emojiButtonScale);
        this.emojiButton.setScaleY(this.emojiButtonPaddingScale * this.emojiButtonScale);
        this.emojiButton.setAlpha(this.emojiButtonPaddingAlpha * this.emojiButtonAlpha);
    }

    public void setOverrideHint(CharSequence charSequence) {
        setOverrideHint(charSequence, false);
    }

    public void setOverrideHint(CharSequence charSequence, boolean z) {
        this.overrideHint = charSequence;
        this.overrideHint2 = null;
        updateFieldHint(z);
    }

    public void setOverrideHint(CharSequence charSequence, CharSequence charSequence2, boolean z) {
        this.overrideHint = charSequence;
        this.overrideHint2 = charSequence2;
        updateFieldHint(z);
    }

    public void setOverrideKeyboardAnimation(boolean z) {
        this.overrideKeyboardAnimation = z;
    }

    public void onOverrideAnimationEnd() {
        Runnable runnable = this.animationEndRunnable;
        if (runnable != null) {
            runnable.run();
            this.animationEndRunnable = null;
        }
    }

    public int getStickersExpandedHeight() {
        return this.stickersExpandedHeight;
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
        cancelRecordIntefraceInternal();
        hideRecordedAudioPanelInternal();
    }

    public void freezeEmojiView(boolean z) {
        this.emojiViewFrozen = z;
        EmojiView emojiView = this.emojiView;
        if (emojiView != null) {
            emojiView.freeze(z);
        }
    }
}
