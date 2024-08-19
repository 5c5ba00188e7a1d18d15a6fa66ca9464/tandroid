package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ReplacementSpan;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import androidx.collection.LongSparseArray;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import j$.util.Comparator$-CC;
import j$.util.function.ToIntFunction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ChatThemeController;
import org.telegram.messenger.CodeHighlighting;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DialogObject;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LiteMode;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$Dialog;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$DraftMessage;
import org.telegram.tgnet.TLRPC$EncryptedChat;
import org.telegram.tgnet.TLRPC$ForumTopic;
import org.telegram.tgnet.TLRPC$InputReplyTo;
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageExtendedMedia;
import org.telegram.tgnet.TLRPC$MessageFwdHeader;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$MessagePeerReaction;
import org.telegram.tgnet.TLRPC$Peer;
import org.telegram.tgnet.TLRPC$Photo;
import org.telegram.tgnet.TLRPC$PhotoSize;
import org.telegram.tgnet.TLRPC$TL_chatAdminRights;
import org.telegram.tgnet.TLRPC$TL_dialogFolder;
import org.telegram.tgnet.TLRPC$TL_documentEmpty;
import org.telegram.tgnet.TLRPC$TL_encryptedChat;
import org.telegram.tgnet.TLRPC$TL_encryptedChatDiscarded;
import org.telegram.tgnet.TLRPC$TL_encryptedChatRequested;
import org.telegram.tgnet.TLRPC$TL_encryptedChatWaiting;
import org.telegram.tgnet.TLRPC$TL_forumTopic;
import org.telegram.tgnet.TLRPC$TL_messageActionChannelMigrateFrom;
import org.telegram.tgnet.TLRPC$TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC$TL_messageActionSetChatTheme;
import org.telegram.tgnet.TLRPC$TL_messageActionTopicCreate;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMedia;
import org.telegram.tgnet.TLRPC$TL_messageExtendedMediaPreview;
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaGame;
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway;
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_messageMediaPaidMedia;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageMediaStory;
import org.telegram.tgnet.TLRPC$TL_messageReactions;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$TL_textWithEntities;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Cells.DialogCell;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AnimatedFloat;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BubbleCounterPath;
import org.telegram.ui.Components.CanvasButton;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.DialogCellTags;
import org.telegram.ui.Components.EmptyStubSpan;
import org.telegram.ui.Components.ForegroundColorSpanThemable;
import org.telegram.ui.Components.Forum.ForumBubbleDrawable;
import org.telegram.ui.Components.Forum.ForumUtilities;
import org.telegram.ui.Components.Premium.PremiumGradient;
import org.telegram.ui.Components.PullForegroundDrawable;
import org.telegram.ui.Components.QuoteSpan;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Components.Reactions.ReactionsLayoutInBubble;
import org.telegram.ui.Components.StaticLayoutEx;
import org.telegram.ui.Components.StatusDrawable;
import org.telegram.ui.Components.TimerDrawable;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanNoUnderlineBold;
import org.telegram.ui.Components.VectorAvatarThumbDrawable;
import org.telegram.ui.Components.spoilers.SpoilerEffect;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.RightSlidingDialogContainer;
import org.telegram.ui.Stars.StarsIntroActivity;
import org.telegram.ui.Stories.StoriesListPlaceProvider;
import org.telegram.ui.Stories.StoriesUtilities;
import org.telegram.ui.Stories.StoryViewer;
/* loaded from: classes4.dex */
public class DialogCell extends BaseCell implements StoriesListPlaceProvider.AvatarOverlaysView {
    private int[] adaptiveEmojiColor;
    private ColorFilter[] adaptiveEmojiColorFilter;
    public int addForumHeightForTags;
    public int addHeightForTags;
    private int animateFromStatusDrawableParams;
    private int animateToStatusDrawableParams;
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStack;
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStack2;
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStack3;
    private AnimatedEmojiSpan.EmojiGroupedSpans animatedEmojiStackName;
    private boolean animatingArchiveAvatar;
    private float animatingArchiveAvatarProgress;
    private boolean applyName;
    private float archiveBackgroundProgress;
    private boolean archiveHidden;
    protected PullForegroundDrawable archivedChatsDrawable;
    private boolean attachedToWindow;
    private AvatarDrawable avatarDrawable;
    public ImageReceiver avatarImage;
    public int avatarStart;
    private int bottomClip;
    private Paint buttonBackgroundPaint;
    private boolean buttonCreated;
    private StaticLayout buttonLayout;
    private int buttonLeft;
    private int buttonTop;
    CanvasButton canvasButton;
    private TLRPC$Chat chat;
    private float chatCallProgress;
    protected CheckBox2 checkBox;
    private int checkDrawLeft;
    private int checkDrawLeft1;
    private int checkDrawTop;
    public float chekBoxPaddingTop;
    private boolean clearingDialog;
    private float clipProgress;
    private int clockDrawLeft;
    public float collapseOffset;
    public boolean collapsed;
    private float cornerProgress;
    private StaticLayout countAnimationInLayout;
    private boolean countAnimationIncrement;
    private StaticLayout countAnimationStableLayout;
    private ValueAnimator countAnimator;
    private float countChangeProgress;
    private StaticLayout countLayout;
    private int countLeft;
    private int countLeftOld;
    private StaticLayout countOldLayout;
    private int countTop;
    private int countWidth;
    private int countWidthOld;
    private Paint counterPaintOutline;
    private Path counterPath;
    private RectF counterPathRect;
    private int currentAccount;
    private int currentDialogFolderDialogsCount;
    private int currentDialogFolderId;
    private long currentDialogId;
    private int currentEditDate;
    private TextPaint currentMessagePaint;
    private float currentRevealBounceProgress;
    private float currentRevealProgress;
    private CustomDialog customDialog;
    DialogCellDelegate delegate;
    private boolean dialogMuted;
    private float dialogMutedProgress;
    private int dialogsType;
    private TLRPC$DraftMessage draftMessage;
    private boolean draftVoice;
    public boolean drawArchive;
    public boolean drawAvatar;
    public boolean drawAvatarSelector;
    private boolean drawCheck1;
    private boolean drawCheck2;
    private boolean drawClock;
    private boolean drawCount;
    private boolean drawCount2;
    private boolean drawError;
    private boolean drawForwardIcon;
    private boolean drawMention;
    private boolean drawNameLock;
    private boolean drawPin;
    private boolean drawPinBackground;
    private boolean drawPinForced;
    private boolean[] drawPlay;
    private boolean drawPremium;
    private boolean drawReactionMention;
    private boolean drawReorder;
    private boolean drawRevealBackground;
    private int drawScam;
    private boolean[] drawSpoiler;
    private boolean drawUnmute;
    private boolean drawVerified;
    public boolean drawingForBlur;
    private AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable emojiStatus;
    private TLRPC$EncryptedChat encryptedChat;
    private int errorLeft;
    private int errorTop;
    private Paint fadePaint;
    private Paint fadePaintBack;
    private int folderId;
    protected boolean forbidDraft;
    protected boolean forbidVerified;
    public TLRPC$TL_forumTopic forumTopic;
    public boolean fullSeparator;
    public boolean fullSeparator2;
    private ArrayList<MessageObject> groupMessages;
    private int halfCheckDrawLeft;
    private boolean hasCall;
    private boolean hasNameInMessage;
    private boolean hasUnmutedTopics;
    private boolean hasVideoThumb;
    public int heightDefault;
    public int heightThreeLines;
    public boolean inPreviewMode;
    private float innerProgress;
    private BounceInterpolator interpolator;
    public boolean isDialogCell;
    private boolean isForum;
    public boolean isSavedDialog;
    public boolean isSavedDialogCell;
    private boolean isSelected;
    private boolean isSliding;
    private boolean isTopic;
    public boolean isTransitionSupport;
    long lastDialogChangedTime;
    private int lastDrawSwipeMessageStringId;
    private RLottieDrawable lastDrawTranslationDrawable;
    private int lastMessageDate;
    private CharSequence lastMessageString;
    private CharSequence lastPrintString;
    private int lastSendState;
    int lastSize;
    private int lastStatusDrawableParams;
    private boolean lastTopicMessageUnread;
    private boolean lastUnreadState;
    private int lock2Left;
    private Drawable lockDrawable;
    private boolean markUnread;
    private int mentionCount;
    private StaticLayout mentionLayout;
    private int mentionLeft;
    private int mentionWidth;
    private MessageObject message;
    private int messageId;
    private StaticLayout messageLayout;
    private int messageLeft;
    private StaticLayout messageNameLayout;
    private int messageNameLeft;
    private int messageNameTop;
    public int messagePaddingStart;
    private int messageTop;
    boolean moving;
    private boolean nameIsEllipsized;
    private StaticLayout nameLayout;
    private boolean nameLayoutEllipsizeByGradient;
    private boolean nameLayoutEllipsizeLeft;
    private boolean nameLayoutFits;
    private float nameLayoutTranslateX;
    private int nameLeft;
    private int nameLockLeft;
    private int nameLockTop;
    private int nameMuteLeft;
    private int nameWidth;
    private boolean needEmoji;
    private float onlineProgress;
    protected boolean overrideSwipeAction;
    protected int overrideSwipeActionBackgroundColorKey;
    protected RLottieDrawable overrideSwipeActionDrawable;
    protected int overrideSwipeActionRevealBackgroundColorKey;
    protected int overrideSwipeActionStringId;
    protected String overrideSwipeActionStringKey;
    private int paintIndex;
    private DialogsActivity parentFragment;
    private int pinLeft;
    private int pinTop;
    private DialogsAdapter.DialogsPreloader preloader;
    private boolean premiumBlocked;
    private final AnimatedFloat premiumBlockedT;
    private PremiumGradient.PremiumGradientTools premiumGradient;
    private int printingStringType;
    private int progressStage;
    private boolean promoDialog;
    private int reactionMentionCount;
    private int reactionMentionLeft;
    private ValueAnimator reactionsMentionsAnimator;
    private float reactionsMentionsChangeProgress;
    private int readOutboxMaxId;
    private RectF rect;
    private float reorderIconProgress;
    private final Theme.ResourcesProvider resourcesProvider;
    public float rightFragmentOffset;
    private float rightFragmentOpenedProgress;
    private boolean showTopicIconInName;
    private boolean showTtl;
    private List<SpoilerEffect> spoilers;
    private List<SpoilerEffect> spoilers2;
    private Stack<SpoilerEffect> spoilersPool;
    private Stack<SpoilerEffect> spoilersPool2;
    private Drawable starBg;
    private int starBgColor;
    private Drawable starFg;
    private boolean statusDrawableAnimationInProgress;
    private ValueAnimator statusDrawableAnimator;
    private int statusDrawableLeft;
    private float statusDrawableProgress;
    public final StoriesUtilities.AvatarStoryParams storyParams;
    public boolean swipeCanceled;
    private int swipeMessageTextId;
    private StaticLayout swipeMessageTextLayout;
    private int swipeMessageWidth;
    public DialogCellTags tags;
    private int tagsLeft;
    private int tagsRight;
    private Paint thumbBackgroundPaint;
    private ImageReceiver[] thumbImage;
    private boolean[] thumbImageSeen;
    private Path thumbPath;
    int thumbSize;
    private SpoilerEffect thumbSpoiler;
    private int thumbsCount;
    private StaticLayout timeLayout;
    private int timeLeft;
    private int timeTop;
    private TimerDrawable timerDrawable;
    private Paint timerPaint;
    private Paint timerPaint2;
    private int topClip;
    int topMessageTopicEndIndex;
    int topMessageTopicStartIndex;
    private Paint topicCounterPaint;
    protected Drawable[] topicIconInName;
    private boolean topicMuted;
    protected int translateY;
    private boolean translationAnimationStarted;
    private RLottieDrawable translationDrawable;
    protected float translationX;
    private int ttlPeriod;
    private float ttlProgress;
    private boolean twoLinesForName;
    private StaticLayout typingLayout;
    private int typingLeft;
    private int unreadCount;
    private Runnable unsubscribePremiumBlocked;
    private final DialogUpdateHelper updateHelper;
    private boolean updateLayout;
    public boolean useForceThreeLines;
    public boolean useFromUserAsAvatar;
    private boolean useMeForMyMessages;
    public boolean useSeparator;
    private TLRPC$User user;
    private boolean visibleOnScreen;
    private boolean wasDrawnOnline;
    protected float xOffset;

    /* loaded from: classes4.dex */
    public static class BounceInterpolator implements Interpolator {
        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            if (f < 0.33f) {
                return (f / 0.33f) * 0.1f;
            }
            float f2 = f - 0.33f;
            return f2 < 0.33f ? 0.1f - ((f2 / 0.34f) * 0.15f) : (((f2 - 0.34f) / 0.33f) * 0.05f) - 0.05f;
        }
    }

    /* loaded from: classes4.dex */
    public static class CustomDialog {
        public int date;
        public int id;
        public boolean isMedia;
        public String message;
        public boolean muted;
        public String name;
        public boolean pinned;
        public int sent = -1;
        public int type;
        public int unread_count;
        public boolean verified;
    }

    /* loaded from: classes4.dex */
    public interface DialogCellDelegate {
        boolean canClickButtonInside();

        void onButtonClicked(DialogCell dialogCell);

        void onButtonLongPress(DialogCell dialogCell);

        void openHiddenStories();

        void openStory(DialogCell dialogCell, Runnable runnable);

        void showChatPreview(DialogCell dialogCell);
    }

    /* loaded from: classes4.dex */
    public static class SharedResources {
    }

    public boolean checkCurrentDialogIndex(boolean z) {
        return false;
    }

    protected boolean drawLock2() {
        return false;
    }

    @Override // org.telegram.ui.Cells.BaseCell, android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public void setSharedResources(SharedResources sharedResources) {
    }

    public void setMoving(boolean z) {
        this.moving = z;
    }

    public boolean isMoving() {
        return this.moving;
    }

    public void setForumTopic(TLRPC$TL_forumTopic tLRPC$TL_forumTopic, long j, MessageObject messageObject, boolean z, boolean z2) {
        PullForegroundDrawable pullForegroundDrawable;
        this.forumTopic = tLRPC$TL_forumTopic;
        this.isTopic = tLRPC$TL_forumTopic != null;
        if (this.currentDialogId != j) {
            this.lastStatusDrawableParams = -1;
        }
        Drawable drawable = messageObject.topicIconDrawable[0];
        if (drawable instanceof ForumBubbleDrawable) {
            ((ForumBubbleDrawable) drawable).setColor(tLRPC$TL_forumTopic.icon_color);
        }
        this.currentDialogId = j;
        this.lastDialogChangedTime = System.currentTimeMillis();
        this.message = messageObject;
        this.isDialogCell = false;
        this.showTopicIconInName = z;
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        this.lastMessageDate = tLRPC$Message.date;
        this.currentEditDate = tLRPC$Message.edit_date;
        this.markUnread = false;
        this.messageId = messageObject.getId();
        this.lastUnreadState = messageObject.isUnread();
        MessageObject messageObject2 = this.message;
        if (messageObject2 != null) {
            this.lastSendState = messageObject2.messageOwner.send_state;
        }
        if (!z2) {
            this.lastStatusDrawableParams = -1;
        }
        if (tLRPC$TL_forumTopic != null) {
            this.groupMessages = tLRPC$TL_forumTopic.groupedMessages;
        }
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic2 = this.forumTopic;
        if (tLRPC$TL_forumTopic2 != null && tLRPC$TL_forumTopic2.id == 1 && (pullForegroundDrawable = this.archivedChatsDrawable) != null) {
            pullForegroundDrawable.setCell(this);
        }
        update(0, z2);
    }

    public void setRightFragmentOpenedProgress(float f) {
        if (this.rightFragmentOpenedProgress != f) {
            this.rightFragmentOpenedProgress = f;
            invalidate();
        }
    }

    public void setIsTransitionSupport(boolean z) {
        this.isTransitionSupport = z;
    }

    public void checkHeight() {
        if (getMeasuredHeight() <= 0 || getMeasuredHeight() == computeHeight()) {
            return;
        }
        requestLayout();
    }

    public void setVisible(boolean z) {
        if (this.visibleOnScreen == z) {
            return;
        }
        this.visibleOnScreen = z;
        if (z) {
            invalidate();
        }
    }

    /* loaded from: classes4.dex */
    public static class FixedWidthSpan extends ReplacementSpan {
        private int width;

        @Override // android.text.style.ReplacementSpan
        public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
        }

        public FixedWidthSpan(int i) {
            this.width = i;
        }

        @Override // android.text.style.ReplacementSpan
        public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
            if (fontMetricsInt == null) {
                fontMetricsInt = paint.getFontMetricsInt();
            }
            if (fontMetricsInt != null) {
                int i3 = 1 - (fontMetricsInt.descent - fontMetricsInt.ascent);
                fontMetricsInt.descent = i3;
                fontMetricsInt.bottom = i3;
                fontMetricsInt.ascent = -1;
                fontMetricsInt.top = -1;
            }
            return this.width;
        }
    }

    public boolean isBlocked() {
        return this.premiumBlocked;
    }

    public DialogCell(DialogsActivity dialogsActivity, Context context, boolean z, boolean z2) {
        this(dialogsActivity, context, z, z2, UserConfig.selectedAccount, null);
    }

    public DialogCell(DialogsActivity dialogsActivity, Context context, boolean z, boolean z2, int i, Theme.ResourcesProvider resourcesProvider) {
        super(context);
        this.drawArchive = true;
        this.drawAvatar = true;
        this.avatarStart = 10;
        this.messagePaddingStart = 72;
        this.heightDefault = 72;
        this.heightThreeLines = 78;
        this.addHeightForTags = 3;
        this.addForumHeightForTags = 11;
        this.chekBoxPaddingTop = 42.0f;
        int i2 = 0;
        StoriesUtilities.AvatarStoryParams avatarStoryParams = new StoriesUtilities.AvatarStoryParams(false) { // from class: org.telegram.ui.Cells.DialogCell.1
            @Override // org.telegram.ui.Stories.StoriesUtilities.AvatarStoryParams
            public void openStory(long j, Runnable runnable) {
                DialogCell dialogCell = DialogCell.this;
                if (dialogCell.delegate == null) {
                    return;
                }
                if (dialogCell.currentDialogFolderId != 0) {
                    DialogCell.this.delegate.openHiddenStories();
                    return;
                }
                DialogCell dialogCell2 = DialogCell.this;
                DialogCellDelegate dialogCellDelegate = dialogCell2.delegate;
                if (dialogCellDelegate != null) {
                    dialogCellDelegate.openStory(dialogCell2, runnable);
                }
            }

            @Override // org.telegram.ui.Stories.StoriesUtilities.AvatarStoryParams
            public void onLongPress() {
                DialogCell dialogCell = DialogCell.this;
                DialogCellDelegate dialogCellDelegate = dialogCell.delegate;
                if (dialogCellDelegate == null) {
                    return;
                }
                dialogCellDelegate.showChatPreview(dialogCell);
            }
        };
        this.storyParams = avatarStoryParams;
        this.thumbSpoiler = new SpoilerEffect();
        this.visibleOnScreen = true;
        this.collapseOffset = 0.0f;
        this.hasUnmutedTopics = false;
        this.overrideSwipeAction = false;
        this.thumbImageSeen = new boolean[3];
        this.thumbImage = new ImageReceiver[3];
        this.drawPlay = new boolean[3];
        this.drawSpoiler = new boolean[3];
        this.avatarImage = new ImageReceiver(this);
        this.avatarDrawable = new AvatarDrawable();
        this.interpolator = new BounceInterpolator();
        this.premiumBlockedT = new AnimatedFloat(this, 0L, 350L, CubicBezierInterpolator.EASE_OUT_QUINT);
        this.spoilersPool = new Stack<>();
        this.spoilers = new ArrayList();
        this.spoilersPool2 = new Stack<>();
        this.spoilers2 = new ArrayList();
        this.drawCount2 = true;
        this.countChangeProgress = 1.0f;
        this.reactionsMentionsChangeProgress = 1.0f;
        this.rect = new RectF();
        this.lastStatusDrawableParams = -1;
        this.readOutboxMaxId = -1;
        this.updateHelper = new DialogUpdateHelper();
        avatarStoryParams.allowLongress = true;
        this.resourcesProvider = resourcesProvider;
        this.parentFragment = dialogsActivity;
        Theme.createDialogsResources(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(28.0f));
        while (true) {
            ImageReceiver[] imageReceiverArr = this.thumbImage;
            if (i2 < imageReceiverArr.length) {
                imageReceiverArr[i2] = new ImageReceiver(this);
                ImageReceiver imageReceiver = this.thumbImage[i2];
                imageReceiver.ignoreNotifications = true;
                imageReceiver.setRoundRadius(AndroidUtilities.dp(2.0f));
                this.thumbImage[i2].setAllowLoadingOnAttachedOnly(true);
                i2++;
            } else {
                this.useForceThreeLines = z2;
                this.currentAccount = i;
                this.emojiStatus = new AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable(this, AndroidUtilities.dp(22.0f));
                this.avatarImage.setAllowLoadingOnAttachedOnly(true);
                return;
            }
        }
    }

    public void setDialog(TLRPC$Dialog tLRPC$Dialog, int i, int i2) {
        if (this.currentDialogId != tLRPC$Dialog.id) {
            ValueAnimator valueAnimator = this.statusDrawableAnimator;
            if (valueAnimator != null) {
                valueAnimator.removeAllListeners();
                this.statusDrawableAnimator.cancel();
            }
            this.statusDrawableAnimationInProgress = false;
            this.lastStatusDrawableParams = -1;
        }
        this.currentDialogId = tLRPC$Dialog.id;
        this.lastDialogChangedTime = System.currentTimeMillis();
        this.isDialogCell = true;
        if (tLRPC$Dialog instanceof TLRPC$TL_dialogFolder) {
            this.currentDialogFolderId = ((TLRPC$TL_dialogFolder) tLRPC$Dialog).folder.id;
            PullForegroundDrawable pullForegroundDrawable = this.archivedChatsDrawable;
            if (pullForegroundDrawable != null) {
                pullForegroundDrawable.setCell(this);
            }
        } else {
            this.currentDialogFolderId = 0;
        }
        this.dialogsType = i;
        showPremiumBlocked(i == 3);
        if (this.tags == null) {
            this.tags = new DialogCellTags();
        }
        this.folderId = i2;
        this.messageId = 0;
        if (update(0, false)) {
            requestLayout();
        }
        checkOnline();
        checkGroupCall();
        checkChatTheme();
        checkTtl();
    }

    public void setDialog(CustomDialog customDialog) {
        this.customDialog = customDialog;
        this.messageId = 0;
        update(0);
        checkOnline();
        checkGroupCall();
        checkChatTheme();
        checkTtl();
    }

    private void checkOnline() {
        TLRPC$User user;
        if (this.user != null && (user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id))) != null) {
            this.user = user;
        }
        this.onlineProgress = isOnline() ? 1.0f : 0.0f;
    }

    private boolean isOnline() {
        TLRPC$User tLRPC$User;
        if (!isForumCell() && (tLRPC$User = this.user) != null && !tLRPC$User.self) {
            TLRPC$UserStatus tLRPC$UserStatus = tLRPC$User.status;
            if (tLRPC$UserStatus != null && tLRPC$UserStatus.expires <= 0 && MessagesController.getInstance(this.currentAccount).onlinePrivacy.containsKey(Long.valueOf(this.user.id))) {
                return true;
            }
            TLRPC$UserStatus tLRPC$UserStatus2 = this.user.status;
            if (tLRPC$UserStatus2 != null && tLRPC$UserStatus2.expires > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                return true;
            }
        }
        return false;
    }

    private void checkGroupCall() {
        TLRPC$Chat tLRPC$Chat = this.chat;
        boolean z = tLRPC$Chat != null && tLRPC$Chat.call_active && tLRPC$Chat.call_not_empty;
        this.hasCall = z;
        this.chatCallProgress = z ? 1.0f : 0.0f;
    }

    private void checkTtl() {
        CheckBox2 checkBox2;
        boolean z = this.ttlPeriod > 0 && !this.hasCall && !isOnline() && ((checkBox2 = this.checkBox) == null || !checkBox2.isChecked());
        this.showTtl = z;
        this.ttlProgress = z ? 1.0f : 0.0f;
    }

    private void checkChatTheme() {
        TLRPC$Message tLRPC$Message;
        MessageObject messageObject = this.message;
        if (messageObject == null || (tLRPC$Message = messageObject.messageOwner) == null) {
            return;
        }
        TLRPC$MessageAction tLRPC$MessageAction = tLRPC$Message.action;
        if ((tLRPC$MessageAction instanceof TLRPC$TL_messageActionSetChatTheme) && this.lastUnreadState) {
            ChatThemeController.getInstance(this.currentAccount).setDialogTheme(this.currentDialogId, ((TLRPC$TL_messageActionSetChatTheme) tLRPC$MessageAction).emoticon, false);
        }
    }

    public void setDialog(long j, MessageObject messageObject, int i, boolean z, boolean z2) {
        if (this.currentDialogId != j) {
            this.lastStatusDrawableParams = -1;
        }
        this.currentDialogId = j;
        this.lastDialogChangedTime = System.currentTimeMillis();
        this.message = messageObject;
        this.useMeForMyMessages = z;
        this.isDialogCell = false;
        this.lastMessageDate = i;
        this.currentEditDate = messageObject != null ? messageObject.messageOwner.edit_date : 0;
        this.unreadCount = 0;
        this.markUnread = false;
        this.messageId = messageObject != null ? messageObject.getId() : 0;
        this.mentionCount = 0;
        this.reactionMentionCount = 0;
        this.lastUnreadState = messageObject != null && messageObject.isUnread();
        MessageObject messageObject2 = this.message;
        if (messageObject2 != null) {
            this.lastSendState = messageObject2.messageOwner.send_state;
        }
        update(0, z2);
    }

    public long getDialogId() {
        return this.currentDialogId;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public void setPreloader(DialogsAdapter.DialogsPreloader dialogsPreloader) {
        this.preloader = dialogsPreloader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isSliding = false;
        this.drawRevealBackground = false;
        this.currentRevealProgress = 0.0f;
        this.attachedToWindow = false;
        this.reorderIconProgress = (getIsPinned() && this.drawReorder) ? 1.0f : 0.0f;
        this.avatarImage.onDetachedFromWindow();
        int i = 0;
        while (true) {
            ImageReceiver[] imageReceiverArr = this.thumbImage;
            if (i >= imageReceiverArr.length) {
                break;
            }
            imageReceiverArr[i].onDetachedFromWindow();
            i++;
        }
        RLottieDrawable rLottieDrawable = this.translationDrawable;
        if (rLottieDrawable != null) {
            rLottieDrawable.stop();
            this.translationDrawable.setProgress(0.0f);
            this.translationDrawable.setCallback(null);
            this.translationDrawable = null;
            this.translationAnimationStarted = false;
        }
        DialogsAdapter.DialogsPreloader dialogsPreloader = this.preloader;
        if (dialogsPreloader != null) {
            dialogsPreloader.remove(this.currentDialogId);
        }
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.detach();
        }
        AnimatedEmojiSpan.release(this, this.animatedEmojiStack);
        AnimatedEmojiSpan.release(this, this.animatedEmojiStack2);
        AnimatedEmojiSpan.release(this, this.animatedEmojiStack3);
        AnimatedEmojiSpan.release(this, this.animatedEmojiStackName);
        this.storyParams.onDetachFromWindow();
        this.canvasButton = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
        int i = 0;
        while (true) {
            ImageReceiver[] imageReceiverArr = this.thumbImage;
            if (i >= imageReceiverArr.length) {
                break;
            }
            imageReceiverArr[i].onAttachedToWindow();
            i++;
        }
        resetPinnedArchiveState();
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack, this.messageLayout);
        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack2, this.messageNameLayout);
        this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStack3, this.buttonLayout);
        this.animatedEmojiStackName = AnimatedEmojiSpan.update(0, this, this.animatedEmojiStackName, this.nameLayout);
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
        if (swapAnimatedEmojiDrawable != null) {
            swapAnimatedEmojiDrawable.attach();
        }
    }

    public void resetPinnedArchiveState() {
        boolean z = SharedConfig.archiveHidden;
        this.archiveHidden = z;
        float f = 1.0f;
        float f2 = z ? 0.0f : 1.0f;
        this.archiveBackgroundProgress = f2;
        this.avatarDrawable.setArchivedAvatarHiddenProgress(f2);
        this.clipProgress = 0.0f;
        this.isSliding = false;
        this.reorderIconProgress = (getIsPinned() && this.drawReorder) ? 0.0f : 0.0f;
        this.attachedToWindow = true;
        this.cornerProgress = 0.0f;
        setTranslationX(0.0f);
        setTranslationY(0.0f);
        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
        if (swapAnimatedEmojiDrawable == null || !this.attachedToWindow) {
            return;
        }
        swapAnimatedEmojiDrawable.attach();
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null) {
            checkBox2.measure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
        }
        if (this.isTopic) {
            setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp(((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? this.heightThreeLines : this.heightDefault) + ((!hasTags() || ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !isForumCell())) ? 0 : isForumCell() ? this.addForumHeightForTags : this.addHeightForTags)) + (this.useSeparator ? 1 : 0));
            checkTwoLinesForName();
        }
        setMeasuredDimension(View.MeasureSpec.getSize(i), computeHeight());
        this.topClip = 0;
        this.bottomClip = getMeasuredHeight();
    }

    private int computeHeight() {
        if (isForumCell() && !this.isTransitionSupport && !this.collapsed) {
            int dp = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 86.0f : 91.0f);
            if (this.useSeparator) {
                dp++;
            }
            return hasTags() ? dp + AndroidUtilities.dp(this.addForumHeightForTags) : dp;
        }
        return getCollapsedHeight();
    }

    private int getCollapsedHeight() {
        int dp = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? this.heightThreeLines : this.heightDefault);
        if (this.useSeparator) {
            dp++;
        }
        if (this.twoLinesForName) {
            dp += AndroidUtilities.dp(20.0f);
        }
        if (hasTags()) {
            if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !isForumCell()) {
                return dp;
            }
            return dp + AndroidUtilities.dp(isForumCell() ? this.addForumHeightForTags : this.addHeightForTags);
        }
        return dp;
    }

    private void checkTwoLinesForName() {
        this.twoLinesForName = false;
        if (!this.isTopic || hasTags()) {
            return;
        }
        buildLayout();
        if (this.nameIsEllipsized) {
            this.twoLinesForName = true;
            buildLayout();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int dp;
        if (this.currentDialogId == 0 && this.customDialog == null) {
            return;
        }
        if (this.checkBox != null) {
            int dp2 = AndroidUtilities.dp(this.messagePaddingStart - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 29 : 27));
            if (this.inPreviewMode) {
                dp2 = AndroidUtilities.dp(8.0f);
                dp = (getMeasuredHeight() - this.checkBox.getMeasuredHeight()) >> 1;
            } else {
                if (LocaleController.isRTL) {
                    dp2 = (i3 - i) - dp2;
                }
                dp = AndroidUtilities.dp(this.chekBoxPaddingTop + ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6 : 0));
            }
            CheckBox2 checkBox2 = this.checkBox;
            checkBox2.layout(dp2, dp, checkBox2.getMeasuredWidth() + dp2, this.checkBox.getMeasuredHeight() + dp);
        }
        int measuredHeight = (getMeasuredHeight() + getMeasuredWidth()) << 16;
        if (measuredHeight != this.lastSize || this.updateLayout) {
            this.updateLayout = false;
            this.lastSize = measuredHeight;
            try {
                buildLayout();
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public boolean getHasUnread() {
        return this.unreadCount != 0 || this.markUnread;
    }

    public boolean getIsMuted() {
        return this.dialogMuted;
    }

    public boolean getIsPinned() {
        return this.drawPin || this.drawPinForced;
    }

    public void setPinForced(boolean z) {
        this.drawPinForced = z;
        if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
            buildLayout();
        }
        invalidate();
    }

    private CharSequence formatArchivedDialogNames() {
        TLRPC$User tLRPC$User;
        String removeDiacritics;
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        ArrayList<TLRPC$Dialog> dialogs = messagesController.getDialogs(this.currentDialogFolderId);
        this.currentDialogFolderDialogsCount = dialogs.size();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        int size = dialogs.size();
        for (int i = 0; i < size; i++) {
            TLRPC$Dialog tLRPC$Dialog = dialogs.get(i);
            if (!messagesController.isHiddenByUndo(tLRPC$Dialog.id)) {
                TLRPC$Chat tLRPC$Chat = null;
                if (DialogObject.isEncryptedDialog(tLRPC$Dialog.id)) {
                    TLRPC$EncryptedChat encryptedChat = messagesController.getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(tLRPC$Dialog.id)));
                    tLRPC$User = encryptedChat != null ? messagesController.getUser(Long.valueOf(encryptedChat.user_id)) : null;
                } else if (DialogObject.isUserDialog(tLRPC$Dialog.id)) {
                    tLRPC$User = messagesController.getUser(Long.valueOf(tLRPC$Dialog.id));
                } else {
                    tLRPC$Chat = messagesController.getChat(Long.valueOf(-tLRPC$Dialog.id));
                    tLRPC$User = null;
                }
                if (tLRPC$Chat != null) {
                    removeDiacritics = tLRPC$Chat.title.replace('\n', ' ');
                } else if (tLRPC$User == null) {
                    continue;
                } else if (UserObject.isDeleted(tLRPC$User)) {
                    removeDiacritics = LocaleController.getString(R.string.HiddenName);
                } else {
                    removeDiacritics = AndroidUtilities.removeDiacritics(ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).replace('\n', ' '));
                }
                if (spannableStringBuilder.length() > 0) {
                    spannableStringBuilder.append((CharSequence) ", ");
                }
                int length = spannableStringBuilder.length();
                int length2 = removeDiacritics.length() + length;
                spannableStringBuilder.append((CharSequence) removeDiacritics);
                if (tLRPC$Dialog.unread_count > 0) {
                    spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.bold(), 0, Theme.getColor(Theme.key_chats_nameArchived, this.resourcesProvider)), length, length2, 33);
                }
                if (spannableStringBuilder.length() > 150) {
                    break;
                }
            }
        }
        if (MessagesController.getInstance(this.currentAccount).storiesController.getTotalStoriesCount(true) > 0) {
            int max = Math.max(1, MessagesController.getInstance(this.currentAccount).storiesController.getTotalStoriesCount(true));
            if (spannableStringBuilder.length() > 0) {
                spannableStringBuilder.append((CharSequence) ", ");
            }
            spannableStringBuilder.append((CharSequence) LocaleController.formatPluralString("Stories", max, new Object[0]));
        }
        return Emoji.replaceEmoji((CharSequence) spannableStringBuilder, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
    }

    public boolean hasTags() {
        DialogCellTags dialogCellTags = this.tags;
        return (dialogCellTags == null || dialogCellTags.isEmpty()) ? false : true;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(120:14|(1:1679)(1:18)|19|(2:1677|1678)(1:25)|26|(1:1676)(1:30)|31|(1:33)|34|(1:1675)(1:38)|39|(1:41)|42|(1:44)(1:1668)|45|(7:47|(1:49)|50|51|(1:53)|54|55)|56|(9:58|(2:60|(2:788|(1:790)(1:791))(2:64|(1:66)(1:787)))(4:792|(1:809)(1:796)|797|(2:805|(1:807)(1:808))(2:801|(1:803)(1:804)))|67|(3:69|(1:71)(4:774|(1:776)|777|(1:782)(1:781))|72)(3:783|(1:785)|786)|73|(1:75)(1:773)|76|(1:78)(1:(1:769)(1:(1:771)(1:772)))|79)(36:810|(2:1664|(1:1666)(1:1667))(2:814|(1:816)(1:1663))|817|(2:819|(2:821|(2:829|(1:831)(1:832))(2:825|(1:827)(1:828))))(2:1613|(2:1615|(2:1617|(1:1619)(2:1620|(1:1622)(2:1623|(1:1625)(3:1626|(1:1632)(1:1630)|1631))))(2:1633|(38:1635|(1:1637)(2:1653|(1:1655)(3:1656|(1:1662)(1:1660)|1661))|1638|(2:1640|(28:1644|1645|(2:1647|(1:1649)(1:1650))|834|(1:838)|839|(6:841|(1:843)(1:1598)|844|(1:846)(1:1597)|847|(1:851))(2:1599|(5:1604|(1:1606)(1:1612)|1607|(1:1609)(1:1611)|1610)(1:1603))|852|(4:856|(2:858|(2:860|(2:862|(1:1567))))|1569|(18:1581|866|(7:868|(1:870)(1:1055)|871|(1:873)(1:1054)|874|(1:879)|880)(3:(6:1057|(1:1059)(1:1565)|1060|(1:1062)(1:1564)|(1:1064)(1:1563)|1065)(1:1566)|1066|(5:1516|1517|(7:1531|(1:1533)(2:1557|(2:1559|(1:1561))(1:1562))|1534|(2:1536|(3:1540|(1:1542)(1:1544)|1543))(2:1554|(1:1556))|1545|(1:1553)(1:1549)|1550)(2:1521|(2:1530|1527)(1:1525))|1526|1527)(4:1070|(1:1072)(11:1075|(2:1077|(1:1079)(4:1080|(2:1082|(1:1084)(2:1085|(1:1087)(2:1088|(1:1090)(2:1091|(2:1093|(1:1095)(1:1096))))))(2:1098|(3:1102|(1:1108)(1:1106)|1107))|1097|1074))(12:1109|(1:1111)(1:1515)|1112|1113|(2:1127|(8:1129|(8:1133|(1:1135)(3:1508|(1:1510)(1:1512)|1511)|1136|(6:1143|(4:1145|(4:1147|(2:1149|(2:1151|(1:1153)(2:1157|(1:1159)(1:1160))))|1161|(1:1163)(2:1164|(1:1166)(2:1167|(1:1169)(1:1170))))(1:1171)|1154|1155)(2:1172|(6:1183|(2:1191|(5:1207|1208|(1:1507)(1:1216)|1217|(11:1298|(2:1331|(5:1333|(1:1345)|1339|1340|(2:1342|(1:1344)))(2:1346|(10:1353|(2:1355|(2:1360|(1:1362)(2:1363|(1:1365)(1:1366))))|1367|(4:1369|(1:1371)(2:1394|(1:1396)(2:1397|(1:1399)(2:1400|(1:1402)(2:1403|(1:1405)(1:1406)))))|1372|(3:1386|(3:1388|(1:1390)(1:1392)|1391)|1393)(4:1376|(2:1378|(1:1380)(1:1381))|(1:1383)(1:1385)|1384))(2:1407|(3:1409|(2:1411|(1:1413)(1:1415))(2:1416|(1:1418)(1:1419))|1414)(2:1420|(4:1422|(4:1424|(1:1426)(1:1431)|1427|1428)(3:1432|(1:1434)(1:1436)|1435)|1429|1430)(5:1437|(5:1439|(2:1441|(3:1443|(1:1445)(1:1453)|1446))|1454|(0)(0)|1446)(2:1455|(1:1457)(2:1458|(2:1460|(1:1465)(1:1464))(2:1466|(1:1468)(2:1469|(1:1471)(2:1472|(1:1474)(2:1475|(3:1489|(4:1495|(1:1497)|1498|(2:1500|(3:1502|(1:1504)(1:1506)|1505)))(1:1493)|1494)(2:1479|(3:1481|(2:1483|(1:1485))(1:1487)|1486)(1:1488))))))))|1447|1448|(2:1450|(1:1452)))))|1301|(1:1303)|1304|(7:1306|(3:1321|(1:1323)|1324)(1:1310)|1311|(1:1313)(1:1320)|1314|(1:1318)|1319)|1325|(2:1330|1206)(1:1329))(1:1352)))|1300|1301|(0)|1304|(0)|1325|(1:1327)|1330|1206)(17:1227|(1:1229)|1230|(2:1238|(14:1240|(1:1284)(1:1244)|1245|1246|1247|(1:1283)(5:1253|1254|1255|1256|1257)|1258|(1:1262)|1263|(4:1265|(1:1267)|1268|(1:1270)(1:1271))|1272|1139|(1:1141)|1142))|1285|1246|1247|(2:1249|1279)|1283|1258|(2:1260|1262)|1263|(0)|1272|1139|(0)|1142))(4:1197|(1:1201)|1202|(1:1204)(2:1205|1206)))(1:1189)|1190|1139|(0)|1142)(3:1176|(1:1182)(1:1180)|1181))|1156|1139|(0)|1142)|1138|1139|(0)|1142)|1513|(0)|1138|1139|(0)|1142))|1514|1513|(0)|1138|1139|(0)|1142)|882|(1:884)(2:1047|(1:1049)(2:1050|(1:1052)(1:1053)))|885|(4:1037|1038|(1:1046)(1:1044)|1045)(5:889|(4:891|(1:(1:894)(2:1011|896))(1:1012)|895|896)(6:1013|(1:1015)(2:1024|(3:1032|1033|(1:1035)(1:1036))(1:1031))|1016|(1:1018)(1:1023)|1019|(1:1021)(1:1022))|897|(2:902|(2:904|(1:906)(2:971|(1:973)(2:974|(4:976|(3:978|(1:980)(1:984)|981)(2:985|(3:987|(1:999)(1:991)|992)(3:1000|(1:1008)(1:1006)|1007))|982|983)(1:1009)))))|1010)|907|(2:911|(1:913)(2:914|(4:916|(1:918)|919|(1:921))))|922|(1:924)(2:926|(3:928|(3:930|(1:932)|933)(2:940|(4:942|(1:944)|945|(1:947)(1:948))(1:949))|(1:939))(4:950|(3:952|(1:954)(2:955|(1:957)(2:958|(2:960|(1:962)(2:963|(1:965)(3:966|(1:968)|969)))(1:970)))|(2:937|939))|935|(0)))|925)|1073|1074))|881|882|(0)(0)|885|(1:887)|1037|1038|(1:1040)|1046|1045|907|(3:909|911|(0)(0))|922|(0)(0)|925))|864|865|866|(0)(0)|881|882|(0)(0)|885|(0)|1037|1038|(0)|1046|1045|907|(0)|922|(0)(0)|925))(1:1652)|1651|1645|(0)|834|(2:836|838)|839|(0)(0)|852|(25:854|856|(0)|1569|(2:1571|1573)|1575|1577|1581|866|(0)(0)|881|882|(0)(0)|885|(0)|1037|1038|(0)|1046|1045|907|(0)|922|(0)(0)|925)|1591|856|(0)|1569|(0)|1575|1577|1581|866|(0)(0)|881|882|(0)(0)|885|(0)|1037|1038|(0)|1046|1045|907|(0)|922|(0)(0)|925))))|833|834|(0)|839|(0)(0)|852|(0)|1591|856|(0)|1569|(0)|1575|1577|1581|866|(0)(0)|881|882|(0)(0)|885|(0)|1037|1038|(0)|1046|1045|907|(0)|922|(0)(0)|925)|(2:81|(1:83)(1:766))(1:767)|84|(3:86|(1:88)(1:764)|89)(1:765)|90|(1:92)(1:763)|93|(3:95|(1:97)|98)|99|(2:101|(1:103)(1:750))(2:751|(2:753|(2:755|(1:757)(1:758))(2:759|(1:761)(1:762))))|104|(2:720|(2:747|(1:749))(2:724|(2:726|(1:728))(2:729|(2:731|(1:733))(2:734|(4:736|(1:738)(1:742)|739|(1:741))))))(2:108|(1:110))|111|112|113|(1:115)|116|(1:118)|119|(3:121|(1:123)(1:125)|124)|126|(1:128)(1:716)|129|(1:131)|132|(1:715)(1:138)|139|(1:141)(1:714)|142|(1:713)(1:146)|147|148|(4:687|(1:689)(1:711)|690|(2:691|(5:693|(1:695)(1:709)|696|(2:707|708)(2:704|705)|706)(1:710)))(9:152|(1:154)(1:686)|155|(1:157)(1:685)|158|(1:160)(1:684)|161|162|(2:163|(5:165|(1:167)(1:181)|168|(2:179|180)(2:176|177)|178)(1:182)))|183|184|(1:186)(1:683)|187|(1:189)|190|(1:198)|199|(2:201|(1:203)(1:204))|205|(2:207|(1:209)(1:609))(1:(4:(4:621|(1:623)(1:679)|624|625)(1:680)|(6:627|(1:629)(1:677)|630|(3:632|(1:634)(1:671)|635)(3:672|(1:674)(1:676)|675)|636|637)(1:678)|638|(2:640|(4:642|(3:644|(1:646)(1:648)|647)|649|(3:651|(1:653)(1:655)|654))(5:656|(3:658|(1:660)(1:662)|661)|663|(3:665|(1:667)(1:669)|668)|670)))(3:614|(2:616|(1:618))|619))|(7:(1:212)|213|(1:215)|216|(1:229)(1:220)|221|(1:225))|230|231|(1:608)(1:235)|236|(3:242|(1:244)(1:246)|245)|247|(4:249|(1:552)(1:253)|254|(2:255|(1:257)(1:258)))(2:553|(8:582|583|(2:585|(2:587|(1:589)))|590|591|(1:601)(1:595)|596|(2:597|(1:599)(1:600)))(3:557|558|(5:563|564|(1:574)(1:568)|569|(2:570|(1:572)(1:573)))(1:562)))|259|260|(1:262)|263|264|265|(1:267)(1:550)|268|269|270|271|(5:273|(3:275|(1:277)|278)|279|(1:281)|278)|282|283|(4:516|517|(5:519|(2:521|(4:523|(2:525|(1:527))|528|(2:530|(2:532|(4:534|(1:538)|539|540)))))|541|539|540)|542)|285|(3:504|505|(35:507|508|(19:510|511|303|(1:496)(1:307)|308|309|(5:487|(1:490)|491|(1:493)(1:495)|494)(3:313|(2:315|(1:319))|320)|321|322|323|324|325|326|327|(10:329|(7:333|(1:335)|336|(1:364)(2:340|(1:342)(2:349|(1:351)(2:352|(3:354|(1:356)(1:358)|357)(1:359))))|343|344|(2:346|(1:348)))|365|(3:369|(1:(1:378)(2:371|(1:373)(2:374|375)))|(1:377))|379|(3:383|(1:(1:392)(2:385|(1:387)(2:388|389)))|(1:391))|393|(2:399|(1:401))|402|(4:406|(1:408)|409|410))(10:428|(5:432|(1:434)|435|(4:437|(1:439)|440|(1:442))|443)|444|(4:448|(1:450)|451|452)|453|(4:457|(1:459)|460|461)|462|(4:466|(1:468)|469|470)|471|(1:475))|411|(3:(1:425)(1:420)|421|(1:423)(1:424))|426|427)|290|(1:292)|498|(26:(1:501)|302|303|(1:305)|496|308|309|(1:311)|485|487|(1:490)|491|(0)(0)|494|321|322|323|324|325|326|327|(0)(0)|411|(6:413|415|(1:418)|425|421|(0)(0))|426|427)|294|(1:497)(1:300)|301|302|303|(0)|496|308|309|(0)|485|487|(0)|491|(0)(0)|494|321|322|323|324|325|326|327|(0)(0)|411|(0)|426|427))|289|290|(0)|498|(0)|294|(1:296)|497|301|302|303|(0)|496|308|309|(0)|485|487|(0)|491|(0)(0)|494|321|322|323|324|325|326|327|(0)(0)|411|(0)|426|427) */
    /* JADX WARN: Can't wrap try/catch for region: R(152:1274|1275|1258|(0)|1263|(0)|1272|1139|(0)|1142|882|(0)(0)|885|(0)|1037|1038|(0)|1046|1045|907|(0)|922|(0)(0)|925|(0)(0)|84|(0)(0)|90|(0)(0)|93|(0)|99|(0)(0)|104|(1:106)|720|(1:722)|743|745|747|(0)|111|112|113|(0)|116|(0)|119|(0)|126|(0)(0)|129|(0)|132|(1:134)|715|139|(0)(0)|142|(1:144)|713|147|148|(1:150)|687|(0)(0)|690|(3:691|(0)(0)|706)|183|184|(0)(0)|187|(0)|190|(77:192|194|196|198|199|(0)|205|(0)(0)|(0)|230|231|(1:233)|608|236|(62:238|240|242|(0)(0)|245|247|(0)(0)|259|260|(0)|263|264|265|(0)(0)|268|269|270|271|(0)|282|283|(0)|285|(1:287)|502|504|505|(0)|289|290|(0)|498|(0)|294|(0)|497|301|302|303|(0)|496|308|309|(0)|485|487|(0)|491|(0)(0)|494|321|322|323|324|325|326|327|(0)(0)|411|(0)|426|427)|606|240|242|(0)(0)|245|247|(0)(0)|259|260|(0)|263|264|265|(0)(0)|268|269|270|271|(0)|282|283|(0)|285|(0)|502|504|505|(0)|289|290|(0)|498|(0)|294|(0)|497|301|302|303|(0)|496|308|309|(0)|485|487|(0)|491|(0)(0)|494|321|322|323|324|325|326|327|(0)(0)|411|(0)|426|427)|681|194|196|198|199|(0)|205|(0)(0)|(0)|230|231|(0)|608|236|(0)|606|240|242|(0)(0)|245|247|(0)(0)|259|260|(0)|263|264|265|(0)(0)|268|269|270|271|(0)|282|283|(0)|285|(0)|502|504|505|(0)|289|290|(0)|498|(0)|294|(0)|497|301|302|303|(0)|496|308|309|(0)|485|487|(0)|491|(0)(0)|494|321|322|323|324|325|326|327|(0)(0)|411|(0)|426|427) */
    /* JADX WARN: Can't wrap try/catch for region: R(98:14|(1:1679)(1:18)|19|(2:1677|1678)(1:25)|26|(1:1676)(1:30)|31|(1:33)|34|(1:1675)(1:38)|39|(1:41)|42|(1:44)(1:1668)|45|(7:47|(1:49)|50|51|(1:53)|54|55)|56|(9:58|(2:60|(2:788|(1:790)(1:791))(2:64|(1:66)(1:787)))(4:792|(1:809)(1:796)|797|(2:805|(1:807)(1:808))(2:801|(1:803)(1:804)))|67|(3:69|(1:71)(4:774|(1:776)|777|(1:782)(1:781))|72)(3:783|(1:785)|786)|73|(1:75)(1:773)|76|(1:78)(1:(1:769)(1:(1:771)(1:772)))|79)(36:810|(2:1664|(1:1666)(1:1667))(2:814|(1:816)(1:1663))|817|(2:819|(2:821|(2:829|(1:831)(1:832))(2:825|(1:827)(1:828))))(2:1613|(2:1615|(2:1617|(1:1619)(2:1620|(1:1622)(2:1623|(1:1625)(3:1626|(1:1632)(1:1630)|1631))))(2:1633|(38:1635|(1:1637)(2:1653|(1:1655)(3:1656|(1:1662)(1:1660)|1661))|1638|(2:1640|(28:1644|1645|(2:1647|(1:1649)(1:1650))|834|(1:838)|839|(6:841|(1:843)(1:1598)|844|(1:846)(1:1597)|847|(1:851))(2:1599|(5:1604|(1:1606)(1:1612)|1607|(1:1609)(1:1611)|1610)(1:1603))|852|(4:856|(2:858|(2:860|(2:862|(1:1567))))|1569|(18:1581|866|(7:868|(1:870)(1:1055)|871|(1:873)(1:1054)|874|(1:879)|880)(3:(6:1057|(1:1059)(1:1565)|1060|(1:1062)(1:1564)|(1:1064)(1:1563)|1065)(1:1566)|1066|(5:1516|1517|(7:1531|(1:1533)(2:1557|(2:1559|(1:1561))(1:1562))|1534|(2:1536|(3:1540|(1:1542)(1:1544)|1543))(2:1554|(1:1556))|1545|(1:1553)(1:1549)|1550)(2:1521|(2:1530|1527)(1:1525))|1526|1527)(4:1070|(1:1072)(11:1075|(2:1077|(1:1079)(4:1080|(2:1082|(1:1084)(2:1085|(1:1087)(2:1088|(1:1090)(2:1091|(2:1093|(1:1095)(1:1096))))))(2:1098|(3:1102|(1:1108)(1:1106)|1107))|1097|1074))(12:1109|(1:1111)(1:1515)|1112|1113|(2:1127|(8:1129|(8:1133|(1:1135)(3:1508|(1:1510)(1:1512)|1511)|1136|(6:1143|(4:1145|(4:1147|(2:1149|(2:1151|(1:1153)(2:1157|(1:1159)(1:1160))))|1161|(1:1163)(2:1164|(1:1166)(2:1167|(1:1169)(1:1170))))(1:1171)|1154|1155)(2:1172|(6:1183|(2:1191|(5:1207|1208|(1:1507)(1:1216)|1217|(11:1298|(2:1331|(5:1333|(1:1345)|1339|1340|(2:1342|(1:1344)))(2:1346|(10:1353|(2:1355|(2:1360|(1:1362)(2:1363|(1:1365)(1:1366))))|1367|(4:1369|(1:1371)(2:1394|(1:1396)(2:1397|(1:1399)(2:1400|(1:1402)(2:1403|(1:1405)(1:1406)))))|1372|(3:1386|(3:1388|(1:1390)(1:1392)|1391)|1393)(4:1376|(2:1378|(1:1380)(1:1381))|(1:1383)(1:1385)|1384))(2:1407|(3:1409|(2:1411|(1:1413)(1:1415))(2:1416|(1:1418)(1:1419))|1414)(2:1420|(4:1422|(4:1424|(1:1426)(1:1431)|1427|1428)(3:1432|(1:1434)(1:1436)|1435)|1429|1430)(5:1437|(5:1439|(2:1441|(3:1443|(1:1445)(1:1453)|1446))|1454|(0)(0)|1446)(2:1455|(1:1457)(2:1458|(2:1460|(1:1465)(1:1464))(2:1466|(1:1468)(2:1469|(1:1471)(2:1472|(1:1474)(2:1475|(3:1489|(4:1495|(1:1497)|1498|(2:1500|(3:1502|(1:1504)(1:1506)|1505)))(1:1493)|1494)(2:1479|(3:1481|(2:1483|(1:1485))(1:1487)|1486)(1:1488))))))))|1447|1448|(2:1450|(1:1452)))))|1301|(1:1303)|1304|(7:1306|(3:1321|(1:1323)|1324)(1:1310)|1311|(1:1313)(1:1320)|1314|(1:1318)|1319)|1325|(2:1330|1206)(1:1329))(1:1352)))|1300|1301|(0)|1304|(0)|1325|(1:1327)|1330|1206)(17:1227|(1:1229)|1230|(2:1238|(14:1240|(1:1284)(1:1244)|1245|1246|1247|(1:1283)(5:1253|1254|1255|1256|1257)|1258|(1:1262)|1263|(4:1265|(1:1267)|1268|(1:1270)(1:1271))|1272|1139|(1:1141)|1142))|1285|1246|1247|(2:1249|1279)|1283|1258|(2:1260|1262)|1263|(0)|1272|1139|(0)|1142))(4:1197|(1:1201)|1202|(1:1204)(2:1205|1206)))(1:1189)|1190|1139|(0)|1142)(3:1176|(1:1182)(1:1180)|1181))|1156|1139|(0)|1142)|1138|1139|(0)|1142)|1513|(0)|1138|1139|(0)|1142))|1514|1513|(0)|1138|1139|(0)|1142)|882|(1:884)(2:1047|(1:1049)(2:1050|(1:1052)(1:1053)))|885|(4:1037|1038|(1:1046)(1:1044)|1045)(5:889|(4:891|(1:(1:894)(2:1011|896))(1:1012)|895|896)(6:1013|(1:1015)(2:1024|(3:1032|1033|(1:1035)(1:1036))(1:1031))|1016|(1:1018)(1:1023)|1019|(1:1021)(1:1022))|897|(2:902|(2:904|(1:906)(2:971|(1:973)(2:974|(4:976|(3:978|(1:980)(1:984)|981)(2:985|(3:987|(1:999)(1:991)|992)(3:1000|(1:1008)(1:1006)|1007))|982|983)(1:1009)))))|1010)|907|(2:911|(1:913)(2:914|(4:916|(1:918)|919|(1:921))))|922|(1:924)(2:926|(3:928|(3:930|(1:932)|933)(2:940|(4:942|(1:944)|945|(1:947)(1:948))(1:949))|(1:939))(4:950|(3:952|(1:954)(2:955|(1:957)(2:958|(2:960|(1:962)(2:963|(1:965)(3:966|(1:968)|969)))(1:970)))|(2:937|939))|935|(0)))|925)|1073|1074))|881|882|(0)(0)|885|(1:887)|1037|1038|(1:1040)|1046|1045|907|(3:909|911|(0)(0))|922|(0)(0)|925))|864|865|866|(0)(0)|881|882|(0)(0)|885|(0)|1037|1038|(0)|1046|1045|907|(0)|922|(0)(0)|925))(1:1652)|1651|1645|(0)|834|(2:836|838)|839|(0)(0)|852|(25:854|856|(0)|1569|(2:1571|1573)|1575|1577|1581|866|(0)(0)|881|882|(0)(0)|885|(0)|1037|1038|(0)|1046|1045|907|(0)|922|(0)(0)|925)|1591|856|(0)|1569|(0)|1575|1577|1581|866|(0)(0)|881|882|(0)(0)|885|(0)|1037|1038|(0)|1046|1045|907|(0)|922|(0)(0)|925))))|833|834|(0)|839|(0)(0)|852|(0)|1591|856|(0)|1569|(0)|1575|1577|1581|866|(0)(0)|881|882|(0)(0)|885|(0)|1037|1038|(0)|1046|1045|907|(0)|922|(0)(0)|925)|(2:81|(1:83)(1:766))(1:767)|84|(3:86|(1:88)(1:764)|89)(1:765)|90|(1:92)(1:763)|93|(3:95|(1:97)|98)|99|(2:101|(1:103)(1:750))(2:751|(2:753|(2:755|(1:757)(1:758))(2:759|(1:761)(1:762))))|104|(2:720|(2:747|(1:749))(2:724|(2:726|(1:728))(2:729|(2:731|(1:733))(2:734|(4:736|(1:738)(1:742)|739|(1:741))))))(2:108|(1:110))|111|(18:112|113|(1:115)|116|(1:118)|119|(3:121|(1:123)(1:125)|124)|126|(1:128)(1:716)|129|(1:131)|132|(1:715)(1:138)|139|(1:141)(1:714)|142|(1:713)(1:146)|147)|148|(4:687|(1:689)(1:711)|690|(2:691|(5:693|(1:695)(1:709)|696|(2:707|708)(2:704|705)|706)(1:710)))(9:152|(1:154)(1:686)|155|(1:157)(1:685)|158|(1:160)(1:684)|161|162|(2:163|(5:165|(1:167)(1:181)|168|(2:179|180)(2:176|177)|178)(1:182)))|183|184|(1:186)(1:683)|187|(1:189)|190|(1:198)|199|(2:201|(1:203)(1:204))|205|(2:207|(1:209)(1:609))(1:(4:(4:621|(1:623)(1:679)|624|625)(1:680)|(6:627|(1:629)(1:677)|630|(3:632|(1:634)(1:671)|635)(3:672|(1:674)(1:676)|675)|636|637)(1:678)|638|(2:640|(4:642|(3:644|(1:646)(1:648)|647)|649|(3:651|(1:653)(1:655)|654))(5:656|(3:658|(1:660)(1:662)|661)|663|(3:665|(1:667)(1:669)|668)|670)))(3:614|(2:616|(1:618))|619))|(7:(1:212)|213|(1:215)|216|(1:229)(1:220)|221|(1:225))|230|231|(1:608)(1:235)|236|(3:242|(1:244)(1:246)|245)|247|(4:249|(1:552)(1:253)|254|(2:255|(1:257)(1:258)))(2:553|(8:582|583|(2:585|(2:587|(1:589)))|590|591|(1:601)(1:595)|596|(2:597|(1:599)(1:600)))(3:557|558|(5:563|564|(1:574)(1:568)|569|(2:570|(1:572)(1:573)))(1:562)))|259|260|(1:262)|263|(4:264|265|(1:267)(1:550)|268)|269|(3:270|271|(5:273|(3:275|(1:277)|278)|279|(1:281)|278))|282|283|(4:516|517|(5:519|(2:521|(4:523|(2:525|(1:527))|528|(2:530|(2:532|(4:534|(1:538)|539|540)))))|541|539|540)|542)|285|(3:504|505|(35:507|508|(19:510|511|303|(1:496)(1:307)|308|309|(5:487|(1:490)|491|(1:493)(1:495)|494)(3:313|(2:315|(1:319))|320)|321|322|323|324|325|326|327|(10:329|(7:333|(1:335)|336|(1:364)(2:340|(1:342)(2:349|(1:351)(2:352|(3:354|(1:356)(1:358)|357)(1:359))))|343|344|(2:346|(1:348)))|365|(3:369|(1:(1:378)(2:371|(1:373)(2:374|375)))|(1:377))|379|(3:383|(1:(1:392)(2:385|(1:387)(2:388|389)))|(1:391))|393|(2:399|(1:401))|402|(4:406|(1:408)|409|410))(10:428|(5:432|(1:434)|435|(4:437|(1:439)|440|(1:442))|443)|444|(4:448|(1:450)|451|452)|453|(4:457|(1:459)|460|461)|462|(4:466|(1:468)|469|470)|471|(1:475))|411|(3:(1:425)(1:420)|421|(1:423)(1:424))|426|427)|290|(1:292)|498|(26:(1:501)|302|303|(1:305)|496|308|309|(1:311)|485|487|(1:490)|491|(0)(0)|494|321|322|323|324|325|326|327|(0)(0)|411|(6:413|415|(1:418)|425|421|(0)(0))|426|427)|294|(1:497)(1:300)|301|302|303|(0)|496|308|309|(0)|485|487|(0)|491|(0)(0)|494|321|322|323|324|325|326|327|(0)(0)|411|(0)|426|427))|289|290|(0)|498|(0)|294|(1:296)|497|301|302|303|(0)|496|308|309|(0)|485|487|(0)|491|(0)(0)|494|321|322|323|324|325|326|327|(0)(0)|411|(0)|426|427) */
    /* JADX WARN: Code restructure failed: missing block: B:1467:0x1f1e, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1472:0x1f27, code lost:
        if (org.telegram.messenger.SharedConfig.useThreeLinesLayout == false) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1523:0x2037, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1524:0x2038, code lost:
        r1 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1525:0x2039, code lost:
        r2 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1526:0x203b, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1527:0x203c, code lost:
        r10 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0604, code lost:
        if (r0.post_messages == false) goto L864;
     */
    /* JADX WARN: Code restructure failed: missing block: B:970:0x139c, code lost:
        if (r6 == null) goto L935;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1004:0x142f  */
    /* JADX WARN: Removed duplicated region for block: B:1008:0x143f  */
    /* JADX WARN: Removed duplicated region for block: B:1012:0x147a  */
    /* JADX WARN: Removed duplicated region for block: B:1015:0x1487  */
    /* JADX WARN: Removed duplicated region for block: B:1020:0x14b7  */
    /* JADX WARN: Removed duplicated region for block: B:1023:0x14be  */
    /* JADX WARN: Removed duplicated region for block: B:1024:0x14d1  */
    /* JADX WARN: Removed duplicated region for block: B:1027:0x14ee  */
    /* JADX WARN: Removed duplicated region for block: B:1033:0x150a  */
    /* JADX WARN: Removed duplicated region for block: B:1037:0x1537  */
    /* JADX WARN: Removed duplicated region for block: B:1066:0x15fd  */
    /* JADX WARN: Removed duplicated region for block: B:1089:0x166b  */
    /* JADX WARN: Removed duplicated region for block: B:1092:0x1670 A[Catch: Exception -> 0x167b, TryCatch #7 {Exception -> 0x167b, blocks: (B:1087:0x1661, B:1090:0x166c, B:1092:0x1670, B:1095:0x167e, B:1097:0x1682, B:1101:0x169c, B:1102:0x16a5, B:1106:0x16bb, B:1108:0x16c1, B:1109:0x16cd, B:1111:0x16e4, B:1113:0x16ea, B:1117:0x16fb, B:1119:0x16ff, B:1121:0x173d, B:1123:0x1741, B:1125:0x174a, B:1127:0x1754, B:1120:0x1720), top: B:1686:0x1661 }] */
    /* JADX WARN: Removed duplicated region for block: B:1097:0x1682 A[Catch: Exception -> 0x167b, TryCatch #7 {Exception -> 0x167b, blocks: (B:1087:0x1661, B:1090:0x166c, B:1092:0x1670, B:1095:0x167e, B:1097:0x1682, B:1101:0x169c, B:1102:0x16a5, B:1106:0x16bb, B:1108:0x16c1, B:1109:0x16cd, B:1111:0x16e4, B:1113:0x16ea, B:1117:0x16fb, B:1119:0x16ff, B:1121:0x173d, B:1123:0x1741, B:1125:0x174a, B:1127:0x1754, B:1120:0x1720), top: B:1686:0x1661 }] */
    /* JADX WARN: Removed duplicated region for block: B:1104:0x16b8  */
    /* JADX WARN: Removed duplicated region for block: B:1105:0x16ba  */
    /* JADX WARN: Removed duplicated region for block: B:1108:0x16c1 A[Catch: Exception -> 0x167b, TryCatch #7 {Exception -> 0x167b, blocks: (B:1087:0x1661, B:1090:0x166c, B:1092:0x1670, B:1095:0x167e, B:1097:0x1682, B:1101:0x169c, B:1102:0x16a5, B:1106:0x16bb, B:1108:0x16c1, B:1109:0x16cd, B:1111:0x16e4, B:1113:0x16ea, B:1117:0x16fb, B:1119:0x16ff, B:1121:0x173d, B:1123:0x1741, B:1125:0x174a, B:1127:0x1754, B:1120:0x1720), top: B:1686:0x1661 }] */
    /* JADX WARN: Removed duplicated region for block: B:1119:0x16ff A[Catch: Exception -> 0x167b, TryCatch #7 {Exception -> 0x167b, blocks: (B:1087:0x1661, B:1090:0x166c, B:1092:0x1670, B:1095:0x167e, B:1097:0x1682, B:1101:0x169c, B:1102:0x16a5, B:1106:0x16bb, B:1108:0x16c1, B:1109:0x16cd, B:1111:0x16e4, B:1113:0x16ea, B:1117:0x16fb, B:1119:0x16ff, B:1121:0x173d, B:1123:0x1741, B:1125:0x174a, B:1127:0x1754, B:1120:0x1720), top: B:1686:0x1661 }] */
    /* JADX WARN: Removed duplicated region for block: B:1120:0x1720 A[Catch: Exception -> 0x167b, TryCatch #7 {Exception -> 0x167b, blocks: (B:1087:0x1661, B:1090:0x166c, B:1092:0x1670, B:1095:0x167e, B:1097:0x1682, B:1101:0x169c, B:1102:0x16a5, B:1106:0x16bb, B:1108:0x16c1, B:1109:0x16cd, B:1111:0x16e4, B:1113:0x16ea, B:1117:0x16fb, B:1119:0x16ff, B:1121:0x173d, B:1123:0x1741, B:1125:0x174a, B:1127:0x1754, B:1120:0x1720), top: B:1686:0x1661 }] */
    /* JADX WARN: Removed duplicated region for block: B:1169:0x18e8  */
    /* JADX WARN: Removed duplicated region for block: B:1170:0x190b  */
    /* JADX WARN: Removed duplicated region for block: B:1174:0x194d  */
    /* JADX WARN: Removed duplicated region for block: B:1191:0x19a1  */
    /* JADX WARN: Removed duplicated region for block: B:1192:0x19b8  */
    /* JADX WARN: Removed duplicated region for block: B:1195:0x19cd  */
    /* JADX WARN: Removed duplicated region for block: B:1209:0x1a08  */
    /* JADX WARN: Removed duplicated region for block: B:1215:0x1a2d  */
    /* JADX WARN: Removed duplicated region for block: B:1219:0x1a65  */
    /* JADX WARN: Removed duplicated region for block: B:1292:0x1c2b  */
    /* JADX WARN: Removed duplicated region for block: B:1315:0x1c88  */
    /* JADX WARN: Removed duplicated region for block: B:1322:0x1c9c  */
    /* JADX WARN: Removed duplicated region for block: B:1330:0x1cb4  */
    /* JADX WARN: Removed duplicated region for block: B:1331:0x1cb7  */
    /* JADX WARN: Removed duplicated region for block: B:1335:0x1cc6  */
    /* JADX WARN: Removed duplicated region for block: B:1346:0x1ceb  */
    /* JADX WARN: Removed duplicated region for block: B:1400:0x1db6  */
    /* JADX WARN: Removed duplicated region for block: B:1404:0x1dd7 A[Catch: Exception -> 0x1e2a, TryCatch #8 {Exception -> 0x1e2a, blocks: (B:1402:0x1dcf, B:1404:0x1dd7, B:1405:0x1e27), top: B:1689:0x1dcf }] */
    /* JADX WARN: Removed duplicated region for block: B:1405:0x1e27 A[Catch: Exception -> 0x1e2a, TRY_LEAVE, TryCatch #8 {Exception -> 0x1e2a, blocks: (B:1402:0x1dcf, B:1404:0x1dd7, B:1405:0x1e27), top: B:1689:0x1dcf }] */
    /* JADX WARN: Removed duplicated region for block: B:1409:0x1e40 A[Catch: Exception -> 0x1e49, TryCatch #9 {Exception -> 0x1e49, blocks: (B:1407:0x1e3a, B:1409:0x1e40, B:1411:0x1e44, B:1419:0x1e77, B:1416:0x1e4b, B:1418:0x1e51), top: B:1691:0x1e3a }] */
    /* JADX WARN: Removed duplicated region for block: B:1454:0x1efc A[Catch: Exception -> 0x1ed0, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x1ed0, blocks: (B:1424:0x1ea9, B:1426:0x1ebb, B:1428:0x1ec1, B:1430:0x1ec5, B:1432:0x1ecb, B:1437:0x1ed5, B:1439:0x1ed9, B:1441:0x1edd, B:1443:0x1ee1, B:1445:0x1ee5, B:1449:0x1ef2, B:1454:0x1efc, B:1471:0x1f25, B:1480:0x1f3b, B:1482:0x1f3f, B:1484:0x1f52, B:1489:0x1f84, B:1491:0x1f88, B:1496:0x1f94, B:1512:0x1fd9, B:1514:0x1feb, B:1516:0x1ff1, B:1500:0x1f9e, B:1503:0x1fa4, B:1504:0x1fab, B:1508:0x1fbd, B:1465:0x1f12), top: B:1675:0x1ea9 }] */
    /* JADX WARN: Removed duplicated region for block: B:1462:0x1f0d A[Catch: Exception -> 0x1f1e, TRY_LEAVE, TryCatch #2 {Exception -> 0x1f1e, blocks: (B:1460:0x1f09, B:1462:0x1f0d), top: B:1676:0x1f09 }] */
    /* JADX WARN: Removed duplicated region for block: B:1471:0x1f25 A[Catch: Exception -> 0x1ed0, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x1ed0, blocks: (B:1424:0x1ea9, B:1426:0x1ebb, B:1428:0x1ec1, B:1430:0x1ec5, B:1432:0x1ecb, B:1437:0x1ed5, B:1439:0x1ed9, B:1441:0x1edd, B:1443:0x1ee1, B:1445:0x1ee5, B:1449:0x1ef2, B:1454:0x1efc, B:1471:0x1f25, B:1480:0x1f3b, B:1482:0x1f3f, B:1484:0x1f52, B:1489:0x1f84, B:1491:0x1f88, B:1496:0x1f94, B:1512:0x1fd9, B:1514:0x1feb, B:1516:0x1ff1, B:1500:0x1f9e, B:1503:0x1fa4, B:1504:0x1fab, B:1508:0x1fbd, B:1465:0x1f12), top: B:1675:0x1ea9 }] */
    /* JADX WARN: Removed duplicated region for block: B:1475:0x1f2f  */
    /* JADX WARN: Removed duplicated region for block: B:1480:0x1f3b A[Catch: Exception -> 0x1ed0, TRY_ENTER, TryCatch #1 {Exception -> 0x1ed0, blocks: (B:1424:0x1ea9, B:1426:0x1ebb, B:1428:0x1ec1, B:1430:0x1ec5, B:1432:0x1ecb, B:1437:0x1ed5, B:1439:0x1ed9, B:1441:0x1edd, B:1443:0x1ee1, B:1445:0x1ee5, B:1449:0x1ef2, B:1454:0x1efc, B:1471:0x1f25, B:1480:0x1f3b, B:1482:0x1f3f, B:1484:0x1f52, B:1489:0x1f84, B:1491:0x1f88, B:1496:0x1f94, B:1512:0x1fd9, B:1514:0x1feb, B:1516:0x1ff1, B:1500:0x1f9e, B:1503:0x1fa4, B:1504:0x1fab, B:1508:0x1fbd, B:1465:0x1f12), top: B:1675:0x1ea9 }] */
    /* JADX WARN: Removed duplicated region for block: B:1489:0x1f84 A[Catch: Exception -> 0x1ed0, TRY_ENTER, TryCatch #1 {Exception -> 0x1ed0, blocks: (B:1424:0x1ea9, B:1426:0x1ebb, B:1428:0x1ec1, B:1430:0x1ec5, B:1432:0x1ecb, B:1437:0x1ed5, B:1439:0x1ed9, B:1441:0x1edd, B:1443:0x1ee1, B:1445:0x1ee5, B:1449:0x1ef2, B:1454:0x1efc, B:1471:0x1f25, B:1480:0x1f3b, B:1482:0x1f3f, B:1484:0x1f52, B:1489:0x1f84, B:1491:0x1f88, B:1496:0x1f94, B:1512:0x1fd9, B:1514:0x1feb, B:1516:0x1ff1, B:1500:0x1f9e, B:1503:0x1fa4, B:1504:0x1fab, B:1508:0x1fbd, B:1465:0x1f12), top: B:1675:0x1ea9 }] */
    /* JADX WARN: Removed duplicated region for block: B:1496:0x1f94 A[Catch: Exception -> 0x1ed0, TRY_ENTER, TRY_LEAVE, TryCatch #1 {Exception -> 0x1ed0, blocks: (B:1424:0x1ea9, B:1426:0x1ebb, B:1428:0x1ec1, B:1430:0x1ec5, B:1432:0x1ecb, B:1437:0x1ed5, B:1439:0x1ed9, B:1441:0x1edd, B:1443:0x1ee1, B:1445:0x1ee5, B:1449:0x1ef2, B:1454:0x1efc, B:1471:0x1f25, B:1480:0x1f3b, B:1482:0x1f3f, B:1484:0x1f52, B:1489:0x1f84, B:1491:0x1f88, B:1496:0x1f94, B:1512:0x1fd9, B:1514:0x1feb, B:1516:0x1ff1, B:1500:0x1f9e, B:1503:0x1fa4, B:1504:0x1fab, B:1508:0x1fbd, B:1465:0x1f12), top: B:1675:0x1ea9 }] */
    /* JADX WARN: Removed duplicated region for block: B:1502:0x1fa2 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1506:0x1fb8  */
    /* JADX WARN: Removed duplicated region for block: B:1507:0x1fbb  */
    /* JADX WARN: Removed duplicated region for block: B:1533:0x205a  */
    /* JADX WARN: Removed duplicated region for block: B:1610:0x2262  */
    /* JADX WARN: Removed duplicated region for block: B:1657:0x2342  */
    /* JADX WARN: Removed duplicated region for block: B:1668:0x237e  */
    /* JADX WARN: Removed duplicated region for block: B:1669:0x2386  */
    /* JADX WARN: Removed duplicated region for block: B:1675:0x1ea9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1711:0x189d A[EDGE_INSN: B:1711:0x189d->B:1166:0x189d ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:215:0x050c  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0538  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x0544  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0581  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x05c1  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x05f4  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x060a  */
    /* JADX WARN: Removed duplicated region for block: B:302:0x062f  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x06a3  */
    /* JADX WARN: Removed duplicated region for block: B:414:0x08f8  */
    /* JADX WARN: Removed duplicated region for block: B:633:0x0c8c  */
    /* JADX WARN: Removed duplicated region for block: B:634:0x0c8f  */
    /* JADX WARN: Removed duplicated region for block: B:696:0x0e23  */
    /* JADX WARN: Removed duplicated region for block: B:699:0x0e4a  */
    /* JADX WARN: Removed duplicated region for block: B:769:0x0fff  */
    /* JADX WARN: Removed duplicated region for block: B:774:0x1010  */
    /* JADX WARN: Removed duplicated region for block: B:784:0x106c  */
    /* JADX WARN: Removed duplicated region for block: B:834:0x1150  */
    /* JADX WARN: Removed duplicated region for block: B:835:0x1158  */
    /* JADX WARN: Removed duplicated region for block: B:844:0x1176  */
    /* JADX WARN: Removed duplicated region for block: B:936:0x12d2  */
    /* JADX WARN: Removed duplicated region for block: B:945:0x1304  */
    /* JADX WARN: Removed duplicated region for block: B:949:0x1317  */
    /* JADX WARN: Removed duplicated region for block: B:950:0x131f  */
    /* JADX WARN: Removed duplicated region for block: B:960:0x1357  */
    /* JADX WARN: Removed duplicated region for block: B:962:0x136f  */
    /* JADX WARN: Type inference failed for: r1v152, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r1v199, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r1v24 */
    /* JADX WARN: Type inference failed for: r1v25 */
    /* JADX WARN: Type inference failed for: r1v250 */
    /* JADX WARN: Type inference failed for: r1v437 */
    /* JADX WARN: Type inference failed for: r4v97, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r52v0, types: [android.view.View, org.telegram.ui.Cells.DialogCell] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildLayout() {
        String str;
        CharSequence charSequence;
        int i;
        CharSequence charSequence2;
        boolean z;
        int i2;
        boolean z2;
        TLRPC$DraftMessage tLRPC$DraftMessage;
        TLRPC$DraftMessage tLRPC$DraftMessage2;
        TLRPC$InputReplyTo tLRPC$InputReplyTo;
        boolean z3;
        TLRPC$DraftMessage tLRPC$DraftMessage3;
        SpannableStringBuilder spannableStringBuilder;
        boolean z4;
        int i3;
        SpannableStringBuilder spannableStringBuilder2;
        String str2;
        boolean z5;
        ArrayList<TLRPC$MessageEntity> arrayList;
        String str3;
        CharSequence charSequence3;
        SpannableStringBuilder spannableStringBuilder3;
        SpannableStringBuilder spannableStringBuilder4;
        TLRPC$Chat chat;
        ?? r1;
        boolean z6;
        TLRPC$Chat tLRPC$Chat;
        int i4;
        SpannableStringBuilder spannableStringBuilder5;
        char c;
        String str4;
        SpannableStringBuilder spannableStringBuilder6;
        boolean isChannelAndNotMegaGroup;
        String formatPluralString;
        SpannableStringBuilder spannableStringBuilder7;
        char c2;
        int i5;
        String formatPluralString2;
        String str5;
        boolean z7;
        CharSequence charSequence4;
        CharSequence replaceNewLines;
        CharSequence highlightText;
        String str6;
        int i6;
        SpannableStringBuilder replaceEmoji;
        String str7;
        boolean z8;
        CharSequence highlightText2;
        ForegroundColorSpanThemable foregroundColorSpanThemable;
        TLRPC$User tLRPC$User;
        MessageObject messageObject;
        TLRPC$User tLRPC$User2;
        CharSequence charSequence5;
        String str8;
        String string;
        boolean z9;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList<TLRPC$MessagePeerReaction> arrayList2;
        CharSequence charSequence6;
        String string2;
        String str9;
        CharSequence charSequence7;
        CharSequence string3;
        String str10;
        TLRPC$DraftMessage tLRPC$DraftMessage4;
        MessageObject messageObject2;
        String stringForMessageListDate;
        MessageObject messageObject3;
        boolean z10;
        String str11;
        String str12;
        MessagesController messagesController;
        CharSequence removeDiacritics;
        SpannableStringBuilder spannableStringBuilder8;
        boolean z11;
        boolean z12;
        CharSequence charSequence8;
        String str13;
        CharSequence charSequence9;
        SpannableStringBuilder spannableStringBuilder9;
        String str14;
        int i7;
        int i8;
        boolean z13;
        boolean z14;
        boolean z15;
        CharSequence charSequence10;
        TLRPC$Chat tLRPC$Chat2;
        MessageObject messageObject4;
        String str15;
        int i9;
        float f;
        int i10;
        int dp;
        int dp2;
        int dp3;
        int i11;
        int i12;
        ImageReceiver[] imageReceiverArr;
        DialogCellTags dialogCellTags;
        int i13;
        int i14;
        int lineCount;
        int lineCount2;
        int lineCount3;
        StaticLayout staticLayout;
        float primaryHorizontal;
        float primaryHorizontal2;
        int i15;
        int lineCount4;
        int lineCount5;
        int lineCount6;
        Object[] spans;
        CharSequence charSequence11;
        Layout.Alignment alignment;
        int i16;
        CharSequence ellipsize;
        CharSequence replaceTwoNewLinesToOne;
        DialogCellTags dialogCellTags2;
        int dp4;
        int dp5;
        DialogCellTags dialogCellTags3;
        int dp6;
        CharSequence highlightText3;
        String str16;
        String str17;
        SpannableStringBuilder formatInternal;
        if (this.isTransitionSupport) {
            return;
        }
        if (this.isDialogCell && !this.updateHelper.update() && this.currentDialogFolderId == 0 && this.encryptedChat == null) {
            return;
        }
        if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
            Theme.dialogs_namePaint[0].setTextSize(AndroidUtilities.dp(17.0f));
            Theme.dialogs_nameEncryptedPaint[0].setTextSize(AndroidUtilities.dp(17.0f));
            Theme.dialogs_messagePaint[0].setTextSize(AndroidUtilities.dp(16.0f));
            Theme.dialogs_messagePrintingPaint[0].setTextSize(AndroidUtilities.dp(16.0f));
            Theme.dialogs_namePaint[1].setTextSize(AndroidUtilities.dp(16.0f));
            Theme.dialogs_nameEncryptedPaint[1].setTextSize(AndroidUtilities.dp(16.0f));
            Theme.dialogs_messagePaint[1].setTextSize(AndroidUtilities.dp(15.0f));
            Theme.dialogs_messagePrintingPaint[1].setTextSize(AndroidUtilities.dp(15.0f));
            TextPaint textPaint = Theme.dialogs_messagePaint[1];
            int color = Theme.getColor(Theme.key_chats_message_threeLines, this.resourcesProvider);
            textPaint.linkColor = color;
            textPaint.setColor(color);
            this.paintIndex = 1;
            this.thumbSize = 18;
        } else {
            Theme.dialogs_namePaint[0].setTextSize(AndroidUtilities.dp(17.0f));
            Theme.dialogs_nameEncryptedPaint[0].setTextSize(AndroidUtilities.dp(17.0f));
            Theme.dialogs_messagePaint[0].setTextSize(AndroidUtilities.dp(16.0f));
            Theme.dialogs_messagePrintingPaint[0].setTextSize(AndroidUtilities.dp(16.0f));
            TextPaint textPaint2 = Theme.dialogs_messagePaint[0];
            int color2 = Theme.getColor(Theme.key_chats_message, this.resourcesProvider);
            textPaint2.linkColor = color2;
            textPaint2.setColor(color2);
            this.paintIndex = 0;
            this.thumbSize = 19;
        }
        this.currentDialogFolderDialogsCount = 0;
        if (isForumCell() || (!this.isDialogCell && !this.isTopic)) {
            str = "**reaction**";
            charSequence = null;
        } else {
            str = "**reaction**";
            charSequence = MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, getTopicId(), true);
        }
        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
        this.drawNameLock = false;
        this.drawVerified = false;
        this.drawPremium = false;
        this.drawForwardIcon = false;
        this.drawScam = 0;
        this.drawPinBackground = false;
        this.thumbsCount = 0;
        this.hasVideoThumb = false;
        this.nameLayoutEllipsizeByGradient = false;
        boolean z16 = (UserObject.isUserSelf(this.user) || this.useMeForMyMessages) ? false : true;
        this.printingStringType = -1;
        if (!isForumCell()) {
            this.buttonLayout = null;
        }
        if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || this.currentDialogFolderId != 0 || isForumCell() || hasTags()) {
            this.hasNameInMessage = true;
            i = 1;
        } else {
            this.hasNameInMessage = false;
            i = 2;
        }
        MessageObject messageObject5 = this.message;
        if (messageObject5 != null) {
            messageObject5.updateTranslation();
        }
        MessageObject messageObject6 = this.message;
        CharSequence charSequence12 = messageObject6 != null ? messageObject6.messageText : null;
        boolean z17 = charSequence12 instanceof Spannable;
        SpannableStringBuilder spannableStringBuilder10 = charSequence12;
        if (z17) {
            SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder(charSequence12);
            for (URLSpanNoUnderlineBold uRLSpanNoUnderlineBold : (URLSpanNoUnderlineBold[]) spannableStringBuilder11.getSpans(0, spannableStringBuilder11.length(), URLSpanNoUnderlineBold.class)) {
                spannableStringBuilder11.removeSpan(uRLSpanNoUnderlineBold);
            }
            for (URLSpanNoUnderline uRLSpanNoUnderline : (URLSpanNoUnderline[]) spannableStringBuilder11.getSpans(0, spannableStringBuilder11.length(), URLSpanNoUnderline.class)) {
                spannableStringBuilder11.removeSpan(uRLSpanNoUnderline);
            }
            spannableStringBuilder10 = spannableStringBuilder11;
        }
        this.lastMessageString = spannableStringBuilder10;
        CustomDialog customDialog = this.customDialog;
        if (customDialog != null) {
            if (customDialog.type == 2) {
                this.drawNameLock = true;
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    this.nameLockTop = AndroidUtilities.dp(12.5f);
                    if (!LocaleController.isRTL) {
                        this.nameLockLeft = AndroidUtilities.dp(this.messagePaddingStart + 6);
                        this.nameLeft = AndroidUtilities.dp(this.messagePaddingStart + 10) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                    } else {
                        this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 6)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        this.nameLeft = AndroidUtilities.dp(22.0f);
                    }
                } else {
                    this.nameLockTop = AndroidUtilities.dp(16.5f);
                    if (!LocaleController.isRTL) {
                        this.nameLockLeft = AndroidUtilities.dp(this.messagePaddingStart + 4);
                        this.nameLeft = AndroidUtilities.dp(this.messagePaddingStart + 8) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                    } else {
                        this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 4)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        this.nameLeft = AndroidUtilities.dp(18.0f);
                    }
                }
            } else {
                this.drawVerified = !this.forbidVerified && customDialog.verified;
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    if (!LocaleController.isRTL) {
                        this.nameLeft = AndroidUtilities.dp(this.messagePaddingStart + 6);
                    } else {
                        this.nameLeft = AndroidUtilities.dp(22.0f);
                    }
                } else if (!LocaleController.isRTL) {
                    this.nameLeft = AndroidUtilities.dp(this.messagePaddingStart + 4);
                } else {
                    this.nameLeft = AndroidUtilities.dp(18.0f);
                }
            }
            CustomDialog customDialog2 = this.customDialog;
            if (customDialog2.type == 1) {
                str16 = LocaleController.getString(R.string.FromYou);
                CustomDialog customDialog3 = this.customDialog;
                if (customDialog3.isMedia) {
                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                    formatInternal = formatInternal(i, this.message.messageText, null);
                    formatInternal.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage, this.resourcesProvider), 0, formatInternal.length(), 33);
                } else {
                    String str18 = customDialog3.message;
                    if (str18.length() > 150) {
                        str18 = str18.substring(0, 150);
                    }
                    if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                        formatInternal = formatInternal(i, str18, str16);
                    } else {
                        formatInternal = formatInternal(i, str18.replace('\n', ' '), str16);
                    }
                }
                charSequence8 = Emoji.replaceEmoji((CharSequence) formatInternal, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                z11 = false;
            } else {
                charSequence8 = customDialog2.message;
                if (customDialog2.isMedia) {
                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                }
                str16 = null;
                z11 = true;
            }
            stringForMessageListDate = LocaleController.stringForMessageListDate(this.customDialog.date);
            int i17 = this.customDialog.unread_count;
            if (i17 != 0) {
                this.drawCount = true;
                str17 = String.format("%d", Integer.valueOf(i17));
            } else {
                this.drawCount = false;
                str17 = null;
            }
            CustomDialog customDialog4 = this.customDialog;
            int i18 = customDialog4.sent;
            if (i18 == 0) {
                this.drawClock = true;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            } else if (i18 == 2) {
                this.drawCheck1 = true;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else if (i18 == 1) {
                this.drawCheck1 = false;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else {
                this.drawClock = false;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            }
            this.drawError = false;
            charSequence9 = customDialog4.name;
            str15 = str16;
            str14 = str17;
            spannableStringBuilder8 = "";
            z12 = true;
            str13 = null;
            spannableStringBuilder9 = null;
            i7 = -1;
        } else {
            if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                if (!LocaleController.isRTL) {
                    this.nameLeft = AndroidUtilities.dp(this.messagePaddingStart + 6);
                } else {
                    this.nameLeft = AndroidUtilities.dp(22.0f);
                }
            } else if (!LocaleController.isRTL) {
                this.nameLeft = AndroidUtilities.dp(this.messagePaddingStart + 4);
            } else {
                this.nameLeft = AndroidUtilities.dp(18.0f);
            }
            if (this.encryptedChat != null) {
                if (this.currentDialogFolderId == 0) {
                    this.drawNameLock = true;
                    if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                        this.nameLockTop = AndroidUtilities.dp(12.5f);
                        if (!LocaleController.isRTL) {
                            this.nameLockLeft = AndroidUtilities.dp(this.messagePaddingStart + 6);
                            this.nameLeft = AndroidUtilities.dp(this.messagePaddingStart + 10) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        } else {
                            this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 6)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                            this.nameLeft = AndroidUtilities.dp(22.0f);
                        }
                    } else {
                        this.nameLockTop = AndroidUtilities.dp(16.5f);
                        if (!LocaleController.isRTL) {
                            this.nameLockLeft = AndroidUtilities.dp(this.messagePaddingStart + 4);
                            this.nameLeft = AndroidUtilities.dp(this.messagePaddingStart + 8) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
                        } else {
                            this.nameLockLeft = (getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 4)) - Theme.dialogs_lockDrawable.getIntrinsicWidth();
                            this.nameLeft = AndroidUtilities.dp(18.0f);
                        }
                    }
                }
            } else if (this.currentDialogFolderId == 0) {
                TLRPC$Chat tLRPC$Chat3 = this.chat;
                if (tLRPC$Chat3 != null) {
                    if (tLRPC$Chat3.scam) {
                        this.drawScam = 1;
                        Theme.dialogs_scamDrawable.checkText();
                    } else if (tLRPC$Chat3.fake) {
                        this.drawScam = 2;
                        Theme.dialogs_fakeDrawable.checkText();
                    } else if (DialogObject.getEmojiStatusDocumentId(tLRPC$Chat3.emoji_status) != 0) {
                        this.drawPremium = true;
                        this.nameLayoutEllipsizeByGradient = true;
                        AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
                        swapAnimatedEmojiDrawable.center = LocaleController.isRTL;
                        swapAnimatedEmojiDrawable.set(DialogObject.getEmojiStatusDocumentId(this.chat.emoji_status), false);
                    } else {
                        this.drawVerified = !this.forbidVerified && this.chat.verified;
                    }
                } else {
                    TLRPC$User tLRPC$User3 = this.user;
                    if (tLRPC$User3 != null) {
                        if (tLRPC$User3.scam) {
                            this.drawScam = 1;
                            Theme.dialogs_scamDrawable.checkText();
                        } else if (tLRPC$User3.fake) {
                            this.drawScam = 2;
                            Theme.dialogs_fakeDrawable.checkText();
                        } else {
                            this.drawVerified = !this.forbidVerified && tLRPC$User3.verified;
                        }
                        if (MessagesController.getInstance(this.currentAccount).isPremiumUser(this.user)) {
                            long j = UserConfig.getInstance(this.currentAccount).clientUserId;
                            charSequence2 = charSequence;
                            long j2 = this.user.id;
                            if (j != j2 && j2 != 0) {
                                z = true;
                                this.drawPremium = z;
                                if (z) {
                                    Long emojiStatusDocumentId = UserObject.getEmojiStatusDocumentId(this.user);
                                    AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable2 = this.emojiStatus;
                                    swapAnimatedEmojiDrawable2.center = LocaleController.isRTL;
                                    if (emojiStatusDocumentId != null) {
                                        this.nameLayoutEllipsizeByGradient = true;
                                        swapAnimatedEmojiDrawable2.set(emojiStatusDocumentId.longValue(), false);
                                    } else {
                                        this.nameLayoutEllipsizeByGradient = true;
                                        swapAnimatedEmojiDrawable2.set(PremiumGradient.getInstance().premiumStarDrawableMini, false);
                                    }
                                }
                                i2 = this.lastMessageDate;
                                if (i2 == 0 && (messageObject4 = this.message) != null) {
                                    i2 = messageObject4.messageOwner.date;
                                }
                                if (this.isTopic) {
                                    boolean z18 = MediaDataController.getInstance(this.currentAccount).getDraftVoice(this.currentDialogId, (long) getTopicId()) != null;
                                    this.draftVoice = z18;
                                    TLRPC$DraftMessage draft = !z18 ? MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, getTopicId()) : null;
                                    this.draftMessage = draft;
                                    if (draft != null && TextUtils.isEmpty(draft.message)) {
                                        this.draftMessage = null;
                                    }
                                } else if (this.isDialogCell || this.isSavedDialogCell) {
                                    boolean z19 = MediaDataController.getInstance(this.currentAccount).getDraftVoice(this.currentDialogId, (long) getTopicId()) != null;
                                    this.draftVoice = z19;
                                    this.draftMessage = !z19 ? MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, 0L) : null;
                                } else {
                                    this.draftVoice = false;
                                    this.draftMessage = null;
                                }
                                z2 = this.draftVoice;
                                if ((!z2 && this.draftMessage == null) || ((z2 || (tLRPC$DraftMessage2 = this.draftMessage) == null || !TextUtils.isEmpty(tLRPC$DraftMessage2.message) || ((tLRPC$InputReplyTo = this.draftMessage.reply_to) != null && tLRPC$InputReplyTo.reply_to_msg_id != 0)) && ((tLRPC$DraftMessage = this.draftMessage) == null || i2 <= tLRPC$DraftMessage.date || this.unreadCount == 0))) {
                                    if (ChatObject.isChannel(this.chat)) {
                                        TLRPC$Chat tLRPC$Chat4 = this.chat;
                                        if (!tLRPC$Chat4.megagroup) {
                                            if (!tLRPC$Chat4.creator) {
                                                TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = tLRPC$Chat4.admin_rights;
                                                if (tLRPC$TL_chatAdminRights != null) {
                                                }
                                            }
                                        }
                                    }
                                    tLRPC$Chat2 = this.chat;
                                    if ((tLRPC$Chat2 != null || (!tLRPC$Chat2.left && !tLRPC$Chat2.kicked)) && !this.forbidDraft && (!ChatObject.isForum(tLRPC$Chat2) || this.isTopic)) {
                                        tLRPC$DraftMessage3 = null;
                                        z3 = false;
                                        if (isForumCell()) {
                                            this.draftMessage = tLRPC$DraftMessage3;
                                            this.draftVoice = z3;
                                            this.needEmoji = true;
                                            updateMessageThumbs();
                                            String removeDiacritics2 = AndroidUtilities.removeDiacritics(getMessageNameString());
                                            CharSequence formatTopicsNames = formatTopicsNames();
                                            MessageObject messageObject7 = this.message;
                                            String messageStringFormatted = this.message != null ? getMessageStringFormatted(i, messageObject7 != null ? MessagesController.getInstance(messageObject7.currentAccount).getRestrictionReason(this.message.messageOwner.restriction_reason) : null, removeDiacritics2, true) : "";
                                            if (this.applyName && messageStringFormatted.length() >= 0 && removeDiacritics2 != null) {
                                                messageStringFormatted = SpannableStringBuilder.valueOf(messageStringFormatted);
                                                messageStringFormatted.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_name, this.resourcesProvider), 0, Math.min(messageStringFormatted.length(), removeDiacritics2.length() + 1), 0);
                                            }
                                            this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                                            str10 = removeDiacritics2;
                                            spannableStringBuilder3 = messageStringFormatted;
                                            spannableStringBuilder2 = "";
                                            z4 = true;
                                            i3 = -1;
                                            charSequence10 = formatTopicsNames;
                                        } else {
                                            if (charSequence2 != null) {
                                                CharSequence charSequence13 = charSequence2;
                                                this.lastPrintString = charSequence13;
                                                int intValue = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.currentDialogId, getTopicId()).intValue();
                                                this.printingStringType = intValue;
                                                StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(intValue);
                                                int intrinsicWidth = chatStatusDrawable != null ? chatStatusDrawable.getIntrinsicWidth() + AndroidUtilities.dp(3.0f) : 0;
                                                spannableStringBuilder = new SpannableStringBuilder();
                                                CharSequence replace = TextUtils.replace(charSequence13, new String[]{"..."}, new String[]{""});
                                                int indexOf = this.printingStringType == 5 ? replace.toString().indexOf("**oo**") : -1;
                                                if (indexOf >= 0) {
                                                    spannableStringBuilder.append(replace).setSpan(new FixedWidthSpan(Theme.getChatStatusDrawable(this.printingStringType).getIntrinsicWidth()), indexOf, indexOf + 6, 0);
                                                } else {
                                                    spannableStringBuilder.append((CharSequence) " ").append(replace).setSpan(new FixedWidthSpan(intrinsicWidth), 0, 1, 0);
                                                }
                                                i3 = indexOf;
                                                z4 = false;
                                            } else {
                                                this.lastPrintString = null;
                                                this.printingStringType = -1;
                                                spannableStringBuilder = "";
                                                z4 = true;
                                                i3 = -1;
                                            }
                                            if (this.draftVoice || this.draftMessage != null) {
                                                spannableStringBuilder2 = spannableStringBuilder;
                                                String string4 = LocaleController.getString(R.string.Draft);
                                                TLRPC$DraftMessage tLRPC$DraftMessage5 = this.draftMessage;
                                                if (tLRPC$DraftMessage5 != null && TextUtils.isEmpty(tLRPC$DraftMessage5.message)) {
                                                    if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                                                        str3 = string4;
                                                        charSequence3 = "";
                                                        z4 = false;
                                                        spannableStringBuilder3 = null;
                                                        charSequence10 = charSequence3;
                                                        str10 = str3;
                                                    } else {
                                                        SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(string4);
                                                        valueOf.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_draft, this.resourcesProvider), 0, string4.length(), 33);
                                                        spannableStringBuilder4 = valueOf;
                                                    }
                                                } else {
                                                    if (this.draftVoice) {
                                                        str2 = LocaleController.getString(R.string.AttachAudio);
                                                    } else {
                                                        TLRPC$DraftMessage tLRPC$DraftMessage6 = this.draftMessage;
                                                        if (tLRPC$DraftMessage6 != null) {
                                                            str2 = tLRPC$DraftMessage6.message;
                                                            if (str2.length() > 150) {
                                                                str2 = str2.substring(0, 150);
                                                            }
                                                        } else {
                                                            str2 = "";
                                                        }
                                                    }
                                                    SpannableStringBuilder spannableStringBuilder12 = new SpannableStringBuilder(str2);
                                                    TLRPC$DraftMessage tLRPC$DraftMessage7 = this.draftMessage;
                                                    if (tLRPC$DraftMessage7 != null) {
                                                        MediaDataController.addTextStyleRuns(tLRPC$DraftMessage7, spannableStringBuilder12, (int) NotificationCenter.attachMenuBotsDidLoad);
                                                        TLRPC$DraftMessage tLRPC$DraftMessage8 = this.draftMessage;
                                                        if (tLRPC$DraftMessage8 != null && (arrayList = tLRPC$DraftMessage8.entities) != null) {
                                                            TextPaint textPaint3 = this.currentMessagePaint;
                                                            MediaDataController.addAnimatedEmojiSpans(arrayList, spannableStringBuilder12, textPaint3 == null ? null : textPaint3.getFontMetricsInt());
                                                        }
                                                    } else if (this.draftVoice) {
                                                        spannableStringBuilder12.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_actionMessage, this.resourcesProvider), 0, spannableStringBuilder12.length(), 33);
                                                    }
                                                    SpannableStringBuilder formatInternal2 = formatInternal(i, AndroidUtilities.replaceNewLines(spannableStringBuilder12), string4);
                                                    if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                                                        z5 = false;
                                                    } else {
                                                        z5 = false;
                                                        formatInternal2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_draft, this.resourcesProvider), 0, string4.length() + 1, 33);
                                                    }
                                                    spannableStringBuilder4 = Emoji.replaceEmoji(formatInternal2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), z5);
                                                }
                                                str3 = string4;
                                                charSequence3 = spannableStringBuilder4;
                                                z4 = false;
                                                spannableStringBuilder3 = null;
                                                charSequence10 = charSequence3;
                                                str10 = str3;
                                            } else {
                                                if (this.clearingDialog) {
                                                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                    string3 = LocaleController.getString(R.string.HistoryCleared);
                                                } else {
                                                    MessageObject messageObject8 = this.message;
                                                    if (messageObject8 == null) {
                                                        if (this.currentDialogFolderId != 0) {
                                                            string3 = formatArchivedDialogNames();
                                                        } else {
                                                            TLRPC$EncryptedChat tLRPC$EncryptedChat = this.encryptedChat;
                                                            if (tLRPC$EncryptedChat != null) {
                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatRequested) {
                                                                    string3 = LocaleController.getString(R.string.EncryptionProcessing);
                                                                } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatWaiting) {
                                                                    string3 = LocaleController.formatString(R.string.AwaitingEncryption, UserObject.getFirstName(this.user));
                                                                } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatDiscarded) {
                                                                    string3 = LocaleController.getString(R.string.EncryptionRejected);
                                                                } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) {
                                                                    if (tLRPC$EncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                                        string3 = LocaleController.formatString(R.string.EncryptedChatStartedOutgoing, UserObject.getFirstName(this.user));
                                                                    } else {
                                                                        string3 = LocaleController.getString(R.string.EncryptedChatStartedIncoming);
                                                                    }
                                                                }
                                                            } else if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                                                DialogsActivity dialogsActivity = this.parentFragment;
                                                                spannableStringBuilder2 = spannableStringBuilder;
                                                                string2 = LocaleController.getString((dialogsActivity == null || !dialogsActivity.isQuote) ? R.string.SavedMessagesInfo : R.string.SavedMessagesInfoQuote);
                                                                spannableStringBuilder3 = null;
                                                                str9 = null;
                                                                z9 = false;
                                                                z16 = false;
                                                            }
                                                            spannableStringBuilder2 = spannableStringBuilder;
                                                            charSequence7 = "";
                                                            spannableStringBuilder3 = null;
                                                            str10 = null;
                                                            charSequence10 = charSequence7;
                                                        }
                                                    } else {
                                                        String restrictionReason = MessagesController.getInstance(messageObject8.currentAccount).getRestrictionReason(this.message.messageOwner.restriction_reason);
                                                        long fromChatId = this.message.getFromChatId();
                                                        if (DialogObject.isUserDialog(fromChatId)) {
                                                            MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
                                                            chat = null;
                                                        } else {
                                                            chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-fromChatId));
                                                        }
                                                        this.drawCount2 = true;
                                                        if (this.dialogsType == 0 && this.currentDialogId > 0 && this.message.isOutOwner() && (tLRPC$TL_messageReactions = this.message.messageOwner.reactions) != null && (arrayList2 = tLRPC$TL_messageReactions.recent_reactions) != null && !arrayList2.isEmpty() && this.reactionMentionCount > 0) {
                                                            TLRPC$MessagePeerReaction tLRPC$MessagePeerReaction = this.message.messageOwner.reactions.recent_reactions.get(0);
                                                            if (tLRPC$MessagePeerReaction.unread) {
                                                                spannableStringBuilder2 = spannableStringBuilder;
                                                                long j3 = tLRPC$MessagePeerReaction.peer_id.user_id;
                                                                if (j3 != 0 && j3 != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                                    ReactionsLayoutInBubble.VisibleReaction fromTL = ReactionsLayoutInBubble.VisibleReaction.fromTL(tLRPC$MessagePeerReaction.reaction);
                                                                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                    String str19 = fromTL.emojicon;
                                                                    if (str19 != null) {
                                                                        charSequence6 = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, str19);
                                                                    } else {
                                                                        String formatString = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, str);
                                                                        int indexOf2 = formatString.indexOf(str);
                                                                        SpannableStringBuilder spannableStringBuilder13 = new SpannableStringBuilder(formatString.replace(str, "d"));
                                                                        long j4 = fromTL.documentId;
                                                                        TextPaint textPaint4 = this.currentMessagePaint;
                                                                        spannableStringBuilder13.setSpan(new AnimatedEmojiSpan(j4, textPaint4 == null ? null : textPaint4.getFontMetricsInt()), indexOf2, indexOf2 + 1, 0);
                                                                        charSequence6 = spannableStringBuilder13;
                                                                    }
                                                                    z6 = true;
                                                                    r1 = charSequence6;
                                                                    if (!z6) {
                                                                        int i19 = this.dialogsType;
                                                                        if (i19 == 2) {
                                                                            TLRPC$Chat tLRPC$Chat5 = this.chat;
                                                                            if (tLRPC$Chat5 != null) {
                                                                                if (ChatObject.isChannel(tLRPC$Chat5)) {
                                                                                    TLRPC$Chat tLRPC$Chat6 = this.chat;
                                                                                    if (!tLRPC$Chat6.megagroup) {
                                                                                        int i20 = tLRPC$Chat6.participants_count;
                                                                                        if (i20 != 0) {
                                                                                            string = LocaleController.formatPluralStringComma("Subscribers", i20);
                                                                                        } else if (!ChatObject.isPublic(tLRPC$Chat6)) {
                                                                                            string = LocaleController.getString(R.string.ChannelPrivate).toLowerCase();
                                                                                        } else {
                                                                                            string = LocaleController.getString(R.string.ChannelPublic).toLowerCase();
                                                                                        }
                                                                                    }
                                                                                }
                                                                                TLRPC$Chat tLRPC$Chat7 = this.chat;
                                                                                int i21 = tLRPC$Chat7.participants_count;
                                                                                if (i21 != 0) {
                                                                                    string = LocaleController.formatPluralStringComma("Members", i21);
                                                                                } else if (tLRPC$Chat7.has_geo) {
                                                                                    string = LocaleController.getString(R.string.MegaLocation);
                                                                                } else if (!ChatObject.isPublic(tLRPC$Chat7)) {
                                                                                    string = LocaleController.getString(R.string.MegaPrivate).toLowerCase();
                                                                                } else {
                                                                                    string = LocaleController.getString(R.string.MegaPublic).toLowerCase();
                                                                                }
                                                                            } else {
                                                                                string = "";
                                                                            }
                                                                            this.drawCount2 = false;
                                                                        } else if (i19 == 3 && UserObject.isUserSelf(this.user)) {
                                                                            DialogsActivity dialogsActivity2 = this.parentFragment;
                                                                            string = LocaleController.getString((dialogsActivity2 == null || !dialogsActivity2.isQuote) ? R.string.SavedMessagesInfo : R.string.SavedMessagesInfoQuote);
                                                                        } else {
                                                                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout && this.currentDialogFolderId != 0) {
                                                                                charSequence5 = formatArchivedDialogNames();
                                                                                z8 = true;
                                                                                z7 = false;
                                                                            } else {
                                                                                MessageObject messageObject9 = this.message;
                                                                                if ((messageObject9.messageOwner instanceof TLRPC$TL_messageService) && (!MessageObject.isTopicActionMessage(messageObject9) || (this.message.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate))) {
                                                                                    if (ChatObject.isChannelAndNotMegaGroup(this.chat) && (this.message.messageOwner.action instanceof TLRPC$TL_messageActionChannelMigrateFrom)) {
                                                                                        spannableStringBuilder10 = "";
                                                                                        z16 = false;
                                                                                    }
                                                                                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                    if (this.message.type == 21) {
                                                                                        updateMessageThumbs();
                                                                                        r1 = applyThumbs(spannableStringBuilder10);
                                                                                    } else {
                                                                                        z7 = z4;
                                                                                        charSequence4 = spannableStringBuilder10;
                                                                                        z8 = true;
                                                                                        charSequence5 = charSequence4;
                                                                                    }
                                                                                } else {
                                                                                    this.needEmoji = true;
                                                                                    updateMessageThumbs();
                                                                                    String removeDiacritics3 = (this.isSavedDialog || (tLRPC$User2 = this.user) == null || !tLRPC$User2.self || this.message.isOutOwner()) ? null : AndroidUtilities.removeDiacritics(getMessageNameString());
                                                                                    if ((this.isSavedDialog && (tLRPC$User = this.user) != null && !tLRPC$User.self && (messageObject = this.message) != null && messageObject.isOutOwner()) || removeDiacritics3 != null || ((tLRPC$Chat = this.chat) != null && tLRPC$Chat.id > 0 && chat == null && ((!ChatObject.isChannel(tLRPC$Chat) || ChatObject.isMegagroup(this.chat)) && !ForumUtilities.isTopicCreateMessage(this.message)))) {
                                                                                        if (removeDiacritics3 == null) {
                                                                                            removeDiacritics3 = getMessageNameString();
                                                                                        }
                                                                                        String removeDiacritics4 = AndroidUtilities.removeDiacritics(removeDiacritics3);
                                                                                        TLRPC$Chat tLRPC$Chat8 = this.chat;
                                                                                        if (tLRPC$Chat8 != null && tLRPC$Chat8.forum && !this.isTopic && !this.useFromUserAsAvatar) {
                                                                                            CharSequence topicIconName = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, this.currentMessagePaint);
                                                                                            if (!TextUtils.isEmpty(topicIconName)) {
                                                                                                SpannableStringBuilder spannableStringBuilder14 = new SpannableStringBuilder("-");
                                                                                                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_mini_forumarrow).mutate());
                                                                                                coloredImageSpan.setColorKey((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? -1 : Theme.key_chats_nameMessage);
                                                                                                spannableStringBuilder14.setSpan(coloredImageSpan, 0, 1, 0);
                                                                                                ?? spannableStringBuilder15 = new SpannableStringBuilder();
                                                                                                spannableStringBuilder15.append(removeDiacritics4).append((CharSequence) spannableStringBuilder14).append(topicIconName);
                                                                                                str6 = spannableStringBuilder15;
                                                                                                SpannableStringBuilder messageStringFormatted2 = getMessageStringFormatted(i, restrictionReason, str6, false);
                                                                                                if (!this.useFromUserAsAvatar || ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && (this.currentDialogFolderId == 0 || messageStringFormatted2.length() <= 0))) {
                                                                                                    i6 = 0;
                                                                                                } else {
                                                                                                    try {
                                                                                                        foregroundColorSpanThemable = new ForegroundColorSpanThemable(Theme.key_chats_nameMessage, this.resourcesProvider);
                                                                                                        i6 = str6.length() + 1;
                                                                                                    } catch (Exception e) {
                                                                                                        e = e;
                                                                                                        i6 = 0;
                                                                                                    }
                                                                                                    try {
                                                                                                        messageStringFormatted2.setSpan(foregroundColorSpanThemable, 0, i6, 33);
                                                                                                    } catch (Exception e2) {
                                                                                                        e = e2;
                                                                                                        FileLog.e(e);
                                                                                                        replaceEmoji = Emoji.replaceEmoji((CharSequence) messageStringFormatted2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                        if (this.message.hasHighlightedWords()) {
                                                                                                        }
                                                                                                        if (this.thumbsCount > 0) {
                                                                                                        }
                                                                                                        str7 = replaceEmoji;
                                                                                                        z8 = true;
                                                                                                        z7 = false;
                                                                                                        str8 = str6;
                                                                                                        String str20 = str8;
                                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                                        }
                                                                                                        z9 = z8;
                                                                                                        z4 = z7;
                                                                                                        spannableStringBuilder3 = null;
                                                                                                        string2 = str7;
                                                                                                        str9 = str20;
                                                                                                        if (this.draftMessage != null) {
                                                                                                        }
                                                                                                        messageObject3 = this.message;
                                                                                                        if (messageObject3 != null) {
                                                                                                        }
                                                                                                        this.drawCheck1 = false;
                                                                                                        this.drawCheck2 = false;
                                                                                                        this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                                                        z10 = false;
                                                                                                        this.drawCount = false;
                                                                                                        this.drawMention = false;
                                                                                                        this.drawReactionMention = false;
                                                                                                        this.drawError = false;
                                                                                                        str11 = null;
                                                                                                        str12 = null;
                                                                                                        this.promoDialog = z10;
                                                                                                        messagesController = MessagesController.getInstance(this.currentAccount);
                                                                                                        if (this.dialogsType == 0) {
                                                                                                        }
                                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                                        }
                                                                                                        spannableStringBuilder8 = spannableStringBuilder2;
                                                                                                        CharSequence charSequence14 = removeDiacritics;
                                                                                                        z11 = z4;
                                                                                                        z12 = z9;
                                                                                                        charSequence8 = string2;
                                                                                                        str13 = str12;
                                                                                                        charSequence9 = charSequence14;
                                                                                                        int i22 = i3;
                                                                                                        spannableStringBuilder9 = spannableStringBuilder3;
                                                                                                        str14 = str11;
                                                                                                        i7 = i22;
                                                                                                        str15 = str9;
                                                                                                        if (!z12) {
                                                                                                        }
                                                                                                        if (drawLock2()) {
                                                                                                        }
                                                                                                        if (LocaleController.isRTL) {
                                                                                                        }
                                                                                                        if (this.drawNameLock) {
                                                                                                        }
                                                                                                        if (!this.drawClock) {
                                                                                                        }
                                                                                                        if (!this.drawPremium) {
                                                                                                        }
                                                                                                        if (!this.dialogMuted) {
                                                                                                        }
                                                                                                        int dp7 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
                                                                                                        this.nameWidth -= dp7;
                                                                                                        if (LocaleController.isRTL) {
                                                                                                        }
                                                                                                        dp6 = this.nameWidth - AndroidUtilities.dp(12.0f);
                                                                                                        if (dp6 < 0) {
                                                                                                        }
                                                                                                        if (charSequence9 instanceof String) {
                                                                                                        }
                                                                                                        if (this.nameLayoutEllipsizeByGradient) {
                                                                                                        }
                                                                                                        float f2 = dp6;
                                                                                                        this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(charSequence9.toString()) <= f2;
                                                                                                        if (!this.twoLinesForName) {
                                                                                                        }
                                                                                                        CharSequence replaceEmoji2 = Emoji.replaceEmoji(charSequence9, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                        MessageObject messageObject10 = this.message;
                                                                                                        if (messageObject10 == null) {
                                                                                                        }
                                                                                                        if (!this.twoLinesForName) {
                                                                                                        }
                                                                                                        this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !this.nameLayout.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
                                                                                                        this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
                                                                                                        this.animatedEmojiStackName = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStackName, this.nameLayout);
                                                                                                        if (!this.useForceThreeLines) {
                                                                                                        }
                                                                                                        dp = AndroidUtilities.dp(11.0f);
                                                                                                        this.messageNameTop = AndroidUtilities.dp(32.0f);
                                                                                                        this.timeTop = AndroidUtilities.dp(13.0f);
                                                                                                        this.errorTop = AndroidUtilities.dp(43.0f);
                                                                                                        this.pinTop = AndroidUtilities.dp(43.0f);
                                                                                                        this.countTop = AndroidUtilities.dp(43.0f);
                                                                                                        this.checkDrawTop = AndroidUtilities.dp(13.0f);
                                                                                                        int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 21);
                                                                                                        if (!LocaleController.isRTL) {
                                                                                                        }
                                                                                                        i11 = measuredWidth;
                                                                                                        this.storyParams.originalAvatarRect.set(dp2, dp, dp2 + AndroidUtilities.dp(56.0f), dp + AndroidUtilities.dp(56.0f));
                                                                                                        i12 = 0;
                                                                                                        while (true) {
                                                                                                            imageReceiverArr = this.thumbImage;
                                                                                                            if (i12 < imageReceiverArr.length) {
                                                                                                            }
                                                                                                            imageReceiverArr[i12].setImageCoords(((this.thumbSize + 2) * i12) + dp3, ((AndroidUtilities.dp(31.0f) + dp) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0)) - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout || (dialogCellTags = this.tags) == null || dialogCellTags.isEmpty()) ? 0 : AndroidUtilities.dp(9.0f)), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                                                                                                            i12++;
                                                                                                            dp = dp;
                                                                                                        }
                                                                                                        int i23 = dp;
                                                                                                        int i24 = i11;
                                                                                                        if (!LocaleController.isRTL) {
                                                                                                        }
                                                                                                        if (this.twoLinesForName) {
                                                                                                        }
                                                                                                        if (!this.useForceThreeLines) {
                                                                                                            this.timeTop -= AndroidUtilities.dp(6.0f);
                                                                                                            this.checkDrawTop -= AndroidUtilities.dp(6.0f);
                                                                                                            if (getIsPinned()) {
                                                                                                            }
                                                                                                            if (this.drawError) {
                                                                                                            }
                                                                                                            if (z11) {
                                                                                                            }
                                                                                                            int max = Math.max(AndroidUtilities.dp(12.0f), i24);
                                                                                                            this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
                                                                                                            if (!this.useForceThreeLines) {
                                                                                                                this.buttonTop -= AndroidUtilities.dp(!isForumCell() ? 10.0f : 12.0f);
                                                                                                                if (!isForumCell()) {
                                                                                                                }
                                                                                                                CharSequence charSequence15 = str15;
                                                                                                                if (this.twoLinesForName) {
                                                                                                                }
                                                                                                                this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack2, this.messageNameLayout);
                                                                                                                this.buttonCreated = false;
                                                                                                                if (TextUtils.isEmpty(spannableStringBuilder9)) {
                                                                                                                }
                                                                                                                this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack3, this.buttonLayout);
                                                                                                                if (!TextUtils.isEmpty(spannableStringBuilder8)) {
                                                                                                                }
                                                                                                                if (charSequence8 instanceof Spannable) {
                                                                                                                }
                                                                                                                if (!this.useForceThreeLines) {
                                                                                                                }
                                                                                                                if (this.currentDialogFolderId != 0) {
                                                                                                                }
                                                                                                                if (!this.useForceThreeLines) {
                                                                                                                }
                                                                                                                if (!hasTags()) {
                                                                                                                }
                                                                                                                if (isForumCell()) {
                                                                                                                }
                                                                                                                ellipsize = TextUtils.ellipsize(charSequence8, this.currentMessagePaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
                                                                                                                charSequence8 = ellipsize;
                                                                                                                charSequence11 = charSequence8;
                                                                                                                alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                                                                                                                if (!this.useForceThreeLines) {
                                                                                                                }
                                                                                                                if (this.thumbsCount > 0) {
                                                                                                                }
                                                                                                                this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence11, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence15 != null ? 1 : 2);
                                                                                                                i14 = max;
                                                                                                                this.spoilersPool.addAll(this.spoilers);
                                                                                                                this.spoilers.clear();
                                                                                                                i13 = 1;
                                                                                                                SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                                                                                                                AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans = this.animatedEmojiStack;
                                                                                                                Layout[] layoutArr = new Layout[i13];
                                                                                                                layoutArr[0] = this.messageLayout;
                                                                                                                this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, emojiGroupedSpans, layoutArr);
                                                                                                                if (!LocaleController.isRTL) {
                                                                                                                }
                                                                                                                staticLayout = this.typingLayout;
                                                                                                                if (staticLayout != null) {
                                                                                                                }
                                                                                                                updateThumbsPosition();
                                                                                                            }
                                                                                                            this.buttonTop -= AndroidUtilities.dp(!isForumCell() ? 10.0f : 12.0f);
                                                                                                            if (!isForumCell()) {
                                                                                                            }
                                                                                                            CharSequence charSequence152 = str15;
                                                                                                            if (this.twoLinesForName) {
                                                                                                            }
                                                                                                            this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack2, this.messageNameLayout);
                                                                                                            this.buttonCreated = false;
                                                                                                            if (TextUtils.isEmpty(spannableStringBuilder9)) {
                                                                                                            }
                                                                                                            this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack3, this.buttonLayout);
                                                                                                            if (!TextUtils.isEmpty(spannableStringBuilder8)) {
                                                                                                            }
                                                                                                            if (charSequence8 instanceof Spannable) {
                                                                                                            }
                                                                                                            if (!this.useForceThreeLines) {
                                                                                                            }
                                                                                                            if (this.currentDialogFolderId != 0) {
                                                                                                            }
                                                                                                            if (!this.useForceThreeLines) {
                                                                                                            }
                                                                                                            if (!hasTags()) {
                                                                                                            }
                                                                                                            if (isForumCell()) {
                                                                                                            }
                                                                                                            ellipsize = TextUtils.ellipsize(charSequence8, this.currentMessagePaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
                                                                                                            charSequence8 = ellipsize;
                                                                                                            charSequence11 = charSequence8;
                                                                                                            alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                                                                                                            if (!this.useForceThreeLines) {
                                                                                                            }
                                                                                                            if (this.thumbsCount > 0) {
                                                                                                            }
                                                                                                            this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence11, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence152 != null ? 1 : 2);
                                                                                                            i14 = max;
                                                                                                            this.spoilersPool.addAll(this.spoilers);
                                                                                                            this.spoilers.clear();
                                                                                                            i13 = 1;
                                                                                                            SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                                                                                                            AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans2 = this.animatedEmojiStack;
                                                                                                            Layout[] layoutArr2 = new Layout[i13];
                                                                                                            layoutArr2[0] = this.messageLayout;
                                                                                                            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, emojiGroupedSpans2, layoutArr2);
                                                                                                            if (!LocaleController.isRTL) {
                                                                                                            }
                                                                                                            staticLayout = this.typingLayout;
                                                                                                            if (staticLayout != null) {
                                                                                                            }
                                                                                                            updateThumbsPosition();
                                                                                                        }
                                                                                                        this.timeTop -= AndroidUtilities.dp(6.0f);
                                                                                                        this.checkDrawTop -= AndroidUtilities.dp(6.0f);
                                                                                                        if (getIsPinned()) {
                                                                                                        }
                                                                                                        if (this.drawError) {
                                                                                                        }
                                                                                                        if (z11) {
                                                                                                        }
                                                                                                        int max2 = Math.max(AndroidUtilities.dp(12.0f), i24);
                                                                                                        this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
                                                                                                        if (!this.useForceThreeLines) {
                                                                                                        }
                                                                                                        this.buttonTop -= AndroidUtilities.dp(!isForumCell() ? 10.0f : 12.0f);
                                                                                                        if (!isForumCell()) {
                                                                                                        }
                                                                                                        CharSequence charSequence1522 = str15;
                                                                                                        if (this.twoLinesForName) {
                                                                                                        }
                                                                                                        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack2, this.messageNameLayout);
                                                                                                        this.buttonCreated = false;
                                                                                                        if (TextUtils.isEmpty(spannableStringBuilder9)) {
                                                                                                        }
                                                                                                        this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack3, this.buttonLayout);
                                                                                                        if (!TextUtils.isEmpty(spannableStringBuilder8)) {
                                                                                                        }
                                                                                                        if (charSequence8 instanceof Spannable) {
                                                                                                        }
                                                                                                        if (!this.useForceThreeLines) {
                                                                                                        }
                                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                                        }
                                                                                                        if (!this.useForceThreeLines) {
                                                                                                        }
                                                                                                        if (!hasTags()) {
                                                                                                        }
                                                                                                        if (isForumCell()) {
                                                                                                        }
                                                                                                        ellipsize = TextUtils.ellipsize(charSequence8, this.currentMessagePaint, max2 - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
                                                                                                        charSequence8 = ellipsize;
                                                                                                        charSequence11 = charSequence8;
                                                                                                        alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                                                                                                        if (!this.useForceThreeLines) {
                                                                                                        }
                                                                                                        if (this.thumbsCount > 0) {
                                                                                                        }
                                                                                                        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence11, this.currentMessagePaint, max2, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max2, charSequence1522 != null ? 1 : 2);
                                                                                                        i14 = max2;
                                                                                                        this.spoilersPool.addAll(this.spoilers);
                                                                                                        this.spoilers.clear();
                                                                                                        i13 = 1;
                                                                                                        SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                                                                                                        AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans22 = this.animatedEmojiStack;
                                                                                                        Layout[] layoutArr22 = new Layout[i13];
                                                                                                        layoutArr22[0] = this.messageLayout;
                                                                                                        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, emojiGroupedSpans22, layoutArr22);
                                                                                                        if (!LocaleController.isRTL) {
                                                                                                        }
                                                                                                        staticLayout = this.typingLayout;
                                                                                                        if (staticLayout != null) {
                                                                                                        }
                                                                                                        updateThumbsPosition();
                                                                                                    }
                                                                                                }
                                                                                                replaceEmoji = Emoji.replaceEmoji((CharSequence) messageStringFormatted2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                                if (this.message.hasHighlightedWords() && (highlightText2 = AndroidUtilities.highlightText(replaceEmoji, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                                    replaceEmoji = highlightText2;
                                                                                                }
                                                                                                if (this.thumbsCount > 0) {
                                                                                                    boolean z20 = replaceEmoji instanceof SpannableStringBuilder;
                                                                                                    replaceEmoji = replaceEmoji;
                                                                                                    if (!z20) {
                                                                                                        replaceEmoji = new SpannableStringBuilder(replaceEmoji);
                                                                                                    }
                                                                                                    SpannableStringBuilder spannableStringBuilder16 = (SpannableStringBuilder) replaceEmoji;
                                                                                                    if (i6 >= spannableStringBuilder16.length()) {
                                                                                                        spannableStringBuilder16.append((CharSequence) " ");
                                                                                                        spannableStringBuilder16.setSpan(new FixedWidthSpan(AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 3)), spannableStringBuilder16.length() - 1, spannableStringBuilder16.length(), 33);
                                                                                                    } else {
                                                                                                        spannableStringBuilder16.insert(i6, (CharSequence) " ");
                                                                                                        spannableStringBuilder16.setSpan(new FixedWidthSpan(AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 3)), i6, i6 + 1, 33);
                                                                                                    }
                                                                                                }
                                                                                                str7 = replaceEmoji;
                                                                                                z8 = true;
                                                                                                z7 = false;
                                                                                                str8 = str6;
                                                                                                String str202 = str8;
                                                                                                if (this.currentDialogFolderId != 0) {
                                                                                                    str202 = formatArchivedDialogNames();
                                                                                                }
                                                                                                z9 = z8;
                                                                                                z4 = z7;
                                                                                                spannableStringBuilder3 = null;
                                                                                                string2 = str7;
                                                                                                str9 = str202;
                                                                                            }
                                                                                        }
                                                                                        str6 = removeDiacritics4;
                                                                                        SpannableStringBuilder messageStringFormatted22 = getMessageStringFormatted(i, restrictionReason, str6, false);
                                                                                        if (this.useFromUserAsAvatar) {
                                                                                        }
                                                                                        i6 = 0;
                                                                                        replaceEmoji = Emoji.replaceEmoji((CharSequence) messageStringFormatted22, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                        if (this.message.hasHighlightedWords()) {
                                                                                            replaceEmoji = highlightText2;
                                                                                        }
                                                                                        if (this.thumbsCount > 0) {
                                                                                        }
                                                                                        str7 = replaceEmoji;
                                                                                        z8 = true;
                                                                                        z7 = false;
                                                                                        str8 = str6;
                                                                                        String str2022 = str8;
                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                        }
                                                                                        z9 = z8;
                                                                                        z4 = z7;
                                                                                        spannableStringBuilder3 = null;
                                                                                        string2 = str7;
                                                                                        str9 = str2022;
                                                                                    } else {
                                                                                        boolean isEmpty = TextUtils.isEmpty(restrictionReason);
                                                                                        String str21 = restrictionReason;
                                                                                        if (isEmpty) {
                                                                                            if (MessageObject.isTopicActionMessage(this.message)) {
                                                                                                MessageObject messageObject11 = this.message;
                                                                                                CharSequence charSequence16 = messageObject11.messageTextShort;
                                                                                                if (charSequence16 == null || ((messageObject11.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                                                                                                    charSequence16 = messageObject11.messageText;
                                                                                                }
                                                                                                CharSequence charSequence17 = charSequence16;
                                                                                                str21 = charSequence17;
                                                                                                if (messageObject11.topicIconDrawable[0] instanceof ForumBubbleDrawable) {
                                                                                                    TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.currentAccount, this.message.messageOwner, true));
                                                                                                    str21 = charSequence17;
                                                                                                    if (findTopic != null) {
                                                                                                        ((ForumBubbleDrawable) this.message.topicIconDrawable[0]).setColor(findTopic.icon_color);
                                                                                                        str21 = charSequence17;
                                                                                                    }
                                                                                                }
                                                                                            } else {
                                                                                                TLRPC$MessageMedia tLRPC$MessageMedia = this.message.messageOwner.media;
                                                                                                if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                                                    str21 = LocaleController.getString(R.string.AttachPhotoExpired);
                                                                                                } else {
                                                                                                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                                                                                                        TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
                                                                                                        if (((tLRPC$Document instanceof TLRPC$TL_documentEmpty) || tLRPC$Document == null) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                                                            if (tLRPC$MessageMedia.voice) {
                                                                                                                str21 = LocaleController.getString(R.string.AttachVoiceExpired);
                                                                                                            } else if (tLRPC$MessageMedia.round) {
                                                                                                                str21 = LocaleController.getString(R.string.AttachRoundExpired);
                                                                                                            } else {
                                                                                                                str21 = LocaleController.getString(R.string.AttachVideoExpired);
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    if (getCaptionMessage() != null) {
                                                                                                        MessageObject captionMessage = getCaptionMessage();
                                                                                                        if (!this.needEmoji) {
                                                                                                            str5 = "";
                                                                                                        } else if (captionMessage.isVideo()) {
                                                                                                            str5 = "📹 ";
                                                                                                        } else if (captionMessage.isVoice()) {
                                                                                                            str5 = "🎤 ";
                                                                                                        } else if (captionMessage.isMusic()) {
                                                                                                            str5 = "🎧 ";
                                                                                                        } else if (captionMessage.isPhoto()) {
                                                                                                            str5 = "🖼 ";
                                                                                                        } else {
                                                                                                            str5 = "📎 ";
                                                                                                        }
                                                                                                        if (captionMessage.hasHighlightedWords() && !TextUtils.isEmpty(captionMessage.messageOwner.message)) {
                                                                                                            CharSequence charSequence18 = captionMessage.messageTrimmedToHighlight;
                                                                                                            int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 47);
                                                                                                            if (this.hasNameInMessage) {
                                                                                                                if (!TextUtils.isEmpty(null)) {
                                                                                                                    throw null;
                                                                                                                }
                                                                                                                measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(": "));
                                                                                                            }
                                                                                                            if (measuredWidth2 > 0) {
                                                                                                                i4 = 33;
                                                                                                                charSequence18 = AndroidUtilities.ellipsizeCenterEnd(charSequence18, captionMessage.highlightedWords.get(0), measuredWidth2, this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged).toString();
                                                                                                            } else {
                                                                                                                i4 = 33;
                                                                                                            }
                                                                                                            spannableStringBuilder7 = new SpannableStringBuilder(str5).append(charSequence18);
                                                                                                        } else {
                                                                                                            i4 = 33;
                                                                                                            SpannableStringBuilder spannableStringBuilder17 = new SpannableStringBuilder(captionMessage.caption);
                                                                                                            if (captionMessage.messageOwner != null) {
                                                                                                                captionMessage.spoilLoginCode();
                                                                                                                MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, captionMessage.caption, spannableStringBuilder17, NotificationCenter.attachMenuBotsDidLoad);
                                                                                                                ArrayList<TLRPC$MessageEntity> arrayList3 = captionMessage.messageOwner.entities;
                                                                                                                TextPaint textPaint5 = this.currentMessagePaint;
                                                                                                                MediaDataController.addAnimatedEmojiSpans(arrayList3, spannableStringBuilder17, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                                                                                            }
                                                                                                            spannableStringBuilder7 = new SpannableStringBuilder(str5).append((CharSequence) spannableStringBuilder17);
                                                                                                        }
                                                                                                    } else {
                                                                                                        i4 = 33;
                                                                                                        MessageObject messageObject12 = this.message;
                                                                                                        TLRPC$Message tLRPC$Message = messageObject12.messageOwner;
                                                                                                        TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message.media;
                                                                                                        if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPaidMedia) {
                                                                                                            int size = ((TLRPC$TL_messageMediaPaidMedia) tLRPC$MessageMedia2).extended_media.size();
                                                                                                            if (this.hasVideoThumb) {
                                                                                                                i5 = 1;
                                                                                                                if (size > 1) {
                                                                                                                    c2 = 0;
                                                                                                                    formatPluralString2 = LocaleController.formatPluralString("Media", size, new Object[0]);
                                                                                                                } else {
                                                                                                                    c2 = 0;
                                                                                                                    formatPluralString2 = LocaleController.getString(R.string.AttachVideo);
                                                                                                                }
                                                                                                            } else {
                                                                                                                c2 = 0;
                                                                                                                i5 = 1;
                                                                                                                formatPluralString2 = size > 1 ? LocaleController.formatPluralString("Photos", size, new Object[0]) : LocaleController.getString(R.string.AttachPhoto);
                                                                                                            }
                                                                                                            int i25 = R.string.AttachPaidMedia;
                                                                                                            Object[] objArr = new Object[i5];
                                                                                                            objArr[c2] = formatPluralString2;
                                                                                                            SpannableStringBuilder replaceStars = StarsIntroActivity.replaceStars(LocaleController.formatString(i25, objArr));
                                                                                                            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                            spannableStringBuilder7 = replaceStars;
                                                                                                        } else if (this.thumbsCount > 1) {
                                                                                                            if (this.hasVideoThumb) {
                                                                                                                ArrayList<MessageObject> arrayList4 = this.groupMessages;
                                                                                                                formatPluralString = LocaleController.formatPluralString("Media", arrayList4 == null ? 0 : arrayList4.size(), new Object[0]);
                                                                                                            } else {
                                                                                                                ArrayList<MessageObject> arrayList5 = this.groupMessages;
                                                                                                                formatPluralString = LocaleController.formatPluralString("Photos", arrayList5 == null ? 0 : arrayList5.size(), new Object[0]);
                                                                                                            }
                                                                                                            spannableStringBuilder7 = formatPluralString;
                                                                                                            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                        } else {
                                                                                                            if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGiveaway) {
                                                                                                                TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
                                                                                                                if (tLRPC$MessageFwdHeader != null) {
                                                                                                                    TLRPC$Peer tLRPC$Peer = tLRPC$MessageFwdHeader.from_id;
                                                                                                                    if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
                                                                                                                        isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Peer.channel_id, this.currentAccount);
                                                                                                                        spannableStringBuilder6 = LocaleController.getString(!isChannelAndNotMegaGroup ? R.string.BoostingGiveawayChannelStarted : R.string.BoostingGiveawayGroupStarted);
                                                                                                                    }
                                                                                                                }
                                                                                                                isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(this.chat);
                                                                                                                spannableStringBuilder6 = LocaleController.getString(!isChannelAndNotMegaGroup ? R.string.BoostingGiveawayChannelStarted : R.string.BoostingGiveawayGroupStarted);
                                                                                                            } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGiveawayResults) {
                                                                                                                spannableStringBuilder6 = LocaleController.getString(R.string.BoostingGiveawayResults);
                                                                                                            } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                                                                                                TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2;
                                                                                                                TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities = tLRPC$TL_messageMediaPoll.poll.question;
                                                                                                                if (tLRPC$TL_textWithEntities == null || tLRPC$TL_textWithEntities.entities == null) {
                                                                                                                    spannableStringBuilder6 = "📊 " + tLRPC$TL_messageMediaPoll.poll.question.text;
                                                                                                                } else {
                                                                                                                    SpannableStringBuilder spannableStringBuilder18 = new SpannableStringBuilder(tLRPC$TL_messageMediaPoll.poll.question.text);
                                                                                                                    TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities2 = tLRPC$TL_messageMediaPoll.poll.question;
                                                                                                                    MediaDataController.addTextStyleRuns(tLRPC$TL_textWithEntities2.entities, tLRPC$TL_textWithEntities2.text, spannableStringBuilder18);
                                                                                                                    MediaDataController.addAnimatedEmojiSpans(tLRPC$TL_messageMediaPoll.poll.question.entities, spannableStringBuilder18, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt());
                                                                                                                    spannableStringBuilder6 = new SpannableStringBuilder("📊 ").append((CharSequence) spannableStringBuilder18);
                                                                                                                }
                                                                                                            } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                                                                                                spannableStringBuilder6 = "🎮 " + this.message.messageOwner.media.game.title;
                                                                                                            } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                                                spannableStringBuilder6 = tLRPC$MessageMedia2.title;
                                                                                                            } else if (messageObject12.type == 14) {
                                                                                                                spannableStringBuilder6 = String.format("🎧 %s - %s", messageObject12.getMusicAuthor(), this.message.getMusicTitle());
                                                                                                            } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaStory) && tLRPC$MessageMedia2.via_mention) {
                                                                                                                if (messageObject12.isOut()) {
                                                                                                                    TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.getDialogId()));
                                                                                                                    if (user != null) {
                                                                                                                        str4 = UserObject.getFirstName(user);
                                                                                                                        int indexOf3 = str4.indexOf(32);
                                                                                                                        c = 0;
                                                                                                                        if (indexOf3 >= 0) {
                                                                                                                            str4 = str4.substring(0, indexOf3);
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        c = 0;
                                                                                                                        str4 = "";
                                                                                                                    }
                                                                                                                    int i26 = R.string.StoryYouMentionInDialog;
                                                                                                                    Object[] objArr2 = new Object[1];
                                                                                                                    objArr2[c] = str4;
                                                                                                                    spannableStringBuilder6 = LocaleController.formatString(i26, objArr2);
                                                                                                                } else {
                                                                                                                    spannableStringBuilder6 = LocaleController.getString(R.string.StoryMentionInDialog);
                                                                                                                }
                                                                                                            } else {
                                                                                                                if (messageObject12.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                                    spannableStringBuilder5 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 23), this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged);
                                                                                                                } else {
                                                                                                                    SpannableStringBuilder spannableStringBuilder19 = new SpannableStringBuilder(spannableStringBuilder10);
                                                                                                                    MessageObject messageObject13 = this.message;
                                                                                                                    if (messageObject13 != null) {
                                                                                                                        messageObject13.spoilLoginCode();
                                                                                                                    }
                                                                                                                    MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder19, (int) NotificationCenter.attachMenuBotsDidLoad);
                                                                                                                    MessageObject messageObject14 = this.message;
                                                                                                                    spannableStringBuilder5 = spannableStringBuilder19;
                                                                                                                    if (messageObject14 != null) {
                                                                                                                        TLRPC$Message tLRPC$Message2 = messageObject14.messageOwner;
                                                                                                                        spannableStringBuilder5 = spannableStringBuilder19;
                                                                                                                        if (tLRPC$Message2 != null) {
                                                                                                                            ArrayList<TLRPC$MessageEntity> arrayList6 = tLRPC$Message2.entities;
                                                                                                                            TextPaint textPaint6 = this.currentMessagePaint;
                                                                                                                            MediaDataController.addAnimatedEmojiSpans(arrayList6, spannableStringBuilder19, textPaint6 == null ? null : textPaint6.getFontMetricsInt());
                                                                                                                            spannableStringBuilder5 = spannableStringBuilder19;
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                                AndroidUtilities.highlightText(spannableStringBuilder5, this.message.highlightedWords, this.resourcesProvider);
                                                                                                                spannableStringBuilder6 = spannableStringBuilder5;
                                                                                                            }
                                                                                                            SpannableStringBuilder spannableStringBuilder20 = spannableStringBuilder6;
                                                                                                            MessageObject messageObject15 = this.message;
                                                                                                            spannableStringBuilder7 = spannableStringBuilder20;
                                                                                                            if (messageObject15.messageOwner.media != null) {
                                                                                                                spannableStringBuilder7 = spannableStringBuilder20;
                                                                                                                if (!messageObject15.isMediaEmpty()) {
                                                                                                                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                                                    spannableStringBuilder7 = spannableStringBuilder20;
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    if (this.message.isReplyToStory()) {
                                                                                                        SpannableStringBuilder spannableStringBuilder21 = new SpannableStringBuilder(spannableStringBuilder7);
                                                                                                        spannableStringBuilder21.insert(0, (CharSequence) "d ");
                                                                                                        spannableStringBuilder21.setSpan(new ColoredImageSpan(ContextCompat.getDrawable(getContext(), R.drawable.msg_mini_replystory).mutate()), 0, 1, 0);
                                                                                                        spannableStringBuilder7 = spannableStringBuilder21;
                                                                                                    }
                                                                                                    if (this.thumbsCount > 0) {
                                                                                                        if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                            replaceNewLines = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(((this.messagePaddingStart + 23) + ((this.thumbSize + 2) * this.thumbsCount)) + 3), this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged).toString();
                                                                                                        } else {
                                                                                                            if (spannableStringBuilder7.length() > 150) {
                                                                                                                spannableStringBuilder7 = spannableStringBuilder7.subSequence(0, 150);
                                                                                                            }
                                                                                                            replaceNewLines = AndroidUtilities.replaceNewLines(spannableStringBuilder7);
                                                                                                        }
                                                                                                        spannableStringBuilder7 = !(replaceNewLines instanceof SpannableStringBuilder) ? new SpannableStringBuilder(replaceNewLines) : replaceNewLines;
                                                                                                        SpannableStringBuilder spannableStringBuilder22 = (SpannableStringBuilder) spannableStringBuilder7;
                                                                                                        spannableStringBuilder22.insert(0, (CharSequence) " ");
                                                                                                        spannableStringBuilder22.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbSize + 2) * this.thumbsCount) + 3)), 0, 1, i4);
                                                                                                        Emoji.replaceEmoji((CharSequence) spannableStringBuilder22, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                                                                                                        if (this.message.hasHighlightedWords() && (highlightText = AndroidUtilities.highlightText(spannableStringBuilder22, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                                            spannableStringBuilder7 = highlightText;
                                                                                                        }
                                                                                                        z4 = false;
                                                                                                    }
                                                                                                    if (this.message.isForwarded() || !this.message.needDrawForwarded()) {
                                                                                                        z7 = z4;
                                                                                                        charSequence4 = spannableStringBuilder7;
                                                                                                        z8 = true;
                                                                                                        charSequence5 = charSequence4;
                                                                                                    } else {
                                                                                                        this.drawForwardIcon = true;
                                                                                                        r1 = new SpannableStringBuilder(spannableStringBuilder7);
                                                                                                        r1.insert(0, "d ");
                                                                                                        ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(ContextCompat.getDrawable(getContext(), R.drawable.mini_forwarded).mutate());
                                                                                                        coloredImageSpan2.setAlpha(0.9f);
                                                                                                        r1.setSpan(coloredImageSpan2, 0, 1, 0);
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        i4 = 33;
                                                                                        spannableStringBuilder7 = str21;
                                                                                        if (this.message.isReplyToStory()) {
                                                                                        }
                                                                                        if (this.thumbsCount > 0) {
                                                                                        }
                                                                                        if (this.message.isForwarded()) {
                                                                                        }
                                                                                        z7 = z4;
                                                                                        charSequence4 = spannableStringBuilder7;
                                                                                        z8 = true;
                                                                                        charSequence5 = charSequence4;
                                                                                    }
                                                                                }
                                                                            }
                                                                            str8 = null;
                                                                            str7 = charSequence5;
                                                                            String str20222 = str8;
                                                                            if (this.currentDialogFolderId != 0) {
                                                                            }
                                                                            z9 = z8;
                                                                            z4 = z7;
                                                                            spannableStringBuilder3 = null;
                                                                            string2 = str7;
                                                                            str9 = str20222;
                                                                        }
                                                                        str7 = string;
                                                                        str8 = null;
                                                                        z16 = false;
                                                                        z7 = z4;
                                                                        z8 = false;
                                                                        String str202222 = str8;
                                                                        if (this.currentDialogFolderId != 0) {
                                                                        }
                                                                        z9 = z8;
                                                                        z4 = z7;
                                                                        spannableStringBuilder3 = null;
                                                                        string2 = str7;
                                                                        str9 = str202222;
                                                                    }
                                                                    str7 = r1;
                                                                    str8 = null;
                                                                    z7 = z4;
                                                                    z8 = true;
                                                                    String str2022222 = str8;
                                                                    if (this.currentDialogFolderId != 0) {
                                                                    }
                                                                    z9 = z8;
                                                                    z4 = z7;
                                                                    spannableStringBuilder3 = null;
                                                                    string2 = str7;
                                                                    str9 = str2022222;
                                                                }
                                                                r1 = "";
                                                                z6 = false;
                                                                if (!z6) {
                                                                }
                                                                str7 = r1;
                                                                str8 = null;
                                                                z7 = z4;
                                                                z8 = true;
                                                                String str20222222 = str8;
                                                                if (this.currentDialogFolderId != 0) {
                                                                }
                                                                z9 = z8;
                                                                z4 = z7;
                                                                spannableStringBuilder3 = null;
                                                                string2 = str7;
                                                                str9 = str20222222;
                                                            }
                                                        }
                                                        spannableStringBuilder2 = spannableStringBuilder;
                                                        r1 = "";
                                                        z6 = false;
                                                        if (!z6) {
                                                        }
                                                        str7 = r1;
                                                        str8 = null;
                                                        z7 = z4;
                                                        z8 = true;
                                                        String str202222222 = str8;
                                                        if (this.currentDialogFolderId != 0) {
                                                        }
                                                        z9 = z8;
                                                        z4 = z7;
                                                        spannableStringBuilder3 = null;
                                                        string2 = str7;
                                                        str9 = str202222222;
                                                    }
                                                    if (this.draftMessage != null) {
                                                        stringForMessageListDate = LocaleController.stringForMessageListDate(tLRPC$DraftMessage4.date);
                                                    } else {
                                                        int i27 = this.lastMessageDate;
                                                        if (i27 != 0) {
                                                            stringForMessageListDate = LocaleController.stringForMessageListDate(i27);
                                                        } else {
                                                            stringForMessageListDate = this.message != null ? LocaleController.stringForMessageListDate(messageObject2.messageOwner.date) : "";
                                                        }
                                                    }
                                                    messageObject3 = this.message;
                                                    if (messageObject3 != null || this.isSavedDialog) {
                                                        this.drawCheck1 = false;
                                                        this.drawCheck2 = false;
                                                        this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                        z10 = false;
                                                        this.drawCount = false;
                                                        this.drawMention = false;
                                                        this.drawReactionMention = false;
                                                        this.drawError = false;
                                                        str11 = null;
                                                        str12 = null;
                                                    } else {
                                                        if (this.currentDialogFolderId != 0) {
                                                            int i28 = this.unreadCount;
                                                            int i29 = this.mentionCount;
                                                            int i30 = i28 + i29;
                                                            if (i30 <= 0) {
                                                                z15 = false;
                                                                this.drawCount = false;
                                                                this.drawMention = false;
                                                                str11 = null;
                                                            } else if (i28 > i29) {
                                                                this.drawCount = true;
                                                                z15 = false;
                                                                this.drawMention = false;
                                                                str11 = String.format("%d", Integer.valueOf(i30));
                                                            } else {
                                                                z15 = false;
                                                                this.drawCount = false;
                                                                this.drawMention = true;
                                                                str12 = String.format("%d", Integer.valueOf(i30));
                                                                str11 = null;
                                                                this.drawReactionMention = z15;
                                                            }
                                                            str12 = null;
                                                            this.drawReactionMention = z15;
                                                        } else {
                                                            if (this.clearingDialog) {
                                                                this.drawCount = false;
                                                                z16 = false;
                                                                str11 = null;
                                                                z13 = true;
                                                                z14 = false;
                                                            } else {
                                                                int i31 = this.unreadCount;
                                                                z13 = true;
                                                                if (i31 != 0 && (i31 != 1 || i31 != this.mentionCount || !messageObject3.messageOwner.mentioned)) {
                                                                    this.drawCount = true;
                                                                    z14 = false;
                                                                    str11 = String.format("%d", Integer.valueOf(i31));
                                                                } else {
                                                                    z14 = false;
                                                                    if (this.markUnread) {
                                                                        this.drawCount = true;
                                                                        str11 = "";
                                                                    } else {
                                                                        this.drawCount = false;
                                                                        str11 = null;
                                                                    }
                                                                }
                                                            }
                                                            if (this.mentionCount != 0) {
                                                                this.drawMention = z13;
                                                                str12 = "@";
                                                            } else {
                                                                this.drawMention = z14;
                                                                str12 = null;
                                                            }
                                                            if (this.reactionMentionCount > 0) {
                                                                this.drawReactionMention = z13;
                                                            } else {
                                                                this.drawReactionMention = z14;
                                                            }
                                                        }
                                                        if (this.message.isOut() && this.draftMessage == null && z16) {
                                                            MessageObject messageObject16 = this.message;
                                                            if (!(messageObject16.messageOwner.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                                                                if (messageObject16.isSending()) {
                                                                    z10 = false;
                                                                    this.drawCheck1 = false;
                                                                    this.drawCheck2 = false;
                                                                    this.drawClock = true;
                                                                    this.drawError = false;
                                                                } else {
                                                                    z10 = false;
                                                                    if (this.message.isSendError()) {
                                                                        this.drawCheck1 = false;
                                                                        this.drawCheck2 = false;
                                                                        this.drawClock = false;
                                                                        this.drawError = true;
                                                                        this.drawCount = false;
                                                                        this.drawMention = false;
                                                                    } else if (this.message.isSent()) {
                                                                        TLRPC$TL_forumTopic tLRPC$TL_forumTopic = this.forumTopic;
                                                                        if (tLRPC$TL_forumTopic != null) {
                                                                            this.drawCheck1 = tLRPC$TL_forumTopic.read_outbox_max_id >= this.message.getId();
                                                                        } else if (this.isDialogCell) {
                                                                            int i32 = this.readOutboxMaxId;
                                                                            this.drawCheck1 = (i32 > 0 && i32 >= this.message.getId()) || !this.message.isUnread() || (ChatObject.isChannel(this.chat) && !this.chat.megagroup);
                                                                        } else {
                                                                            this.drawCheck1 = !this.message.isUnread() || (ChatObject.isChannel(this.chat) && !this.chat.megagroup);
                                                                        }
                                                                        this.drawCheck2 = true;
                                                                        z10 = false;
                                                                        this.drawClock = false;
                                                                        this.drawError = false;
                                                                    } else {
                                                                        z10 = false;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        z10 = false;
                                                        this.drawCheck1 = false;
                                                        this.drawCheck2 = false;
                                                        this.drawClock = false;
                                                        this.drawError = false;
                                                    }
                                                    this.promoDialog = z10;
                                                    messagesController = MessagesController.getInstance(this.currentAccount);
                                                    if (this.dialogsType == 0 && messagesController.isPromoDialog(this.currentDialogId, true)) {
                                                        this.drawPinBackground = true;
                                                        this.promoDialog = true;
                                                        i8 = messagesController.promoDialogType;
                                                        if (i8 != MessagesController.PROMO_TYPE_PROXY) {
                                                            stringForMessageListDate = LocaleController.getString(R.string.UseProxySponsor);
                                                        } else if (i8 == MessagesController.PROMO_TYPE_PSA) {
                                                            stringForMessageListDate = LocaleController.getString("PsaType_" + messagesController.promoPsaType);
                                                            if (TextUtils.isEmpty(stringForMessageListDate)) {
                                                                stringForMessageListDate = LocaleController.getString(R.string.PsaTypeDefault);
                                                            }
                                                            if (!TextUtils.isEmpty(messagesController.promoPsaMessage)) {
                                                                string2 = messagesController.promoPsaMessage;
                                                                this.thumbsCount = 0;
                                                            }
                                                        }
                                                    }
                                                    if (this.currentDialogFolderId != 0) {
                                                        removeDiacritics = LocaleController.getString(R.string.ArchivedChats);
                                                    } else {
                                                        TLRPC$Chat tLRPC$Chat9 = this.chat;
                                                        if (tLRPC$Chat9 != null) {
                                                            if (this.useFromUserAsAvatar) {
                                                                if (this.topicIconInName == null) {
                                                                    this.topicIconInName = new Drawable[1];
                                                                }
                                                                this.topicIconInName[0] = null;
                                                                removeDiacritics = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, this.currentMessagePaint, this.topicIconInName);
                                                            } else if (this.isTopic) {
                                                                if (this.topicIconInName == null) {
                                                                    this.topicIconInName = new Drawable[1];
                                                                }
                                                                Drawable[] drawableArr = this.topicIconInName;
                                                                drawableArr[0] = null;
                                                                removeDiacritics = this.showTopicIconInName ? ForumUtilities.getTopicSpannedName(this.forumTopic, Theme.dialogs_namePaint[this.paintIndex], drawableArr, false) : AndroidUtilities.removeDiacritics(this.forumTopic.title);
                                                            } else {
                                                                removeDiacritics = AndroidUtilities.removeDiacritics(tLRPC$Chat9.title);
                                                            }
                                                            if (removeDiacritics != null && removeDiacritics.length() == 0) {
                                                                removeDiacritics = LocaleController.getString(R.string.HiddenName);
                                                            }
                                                        } else {
                                                            TLRPC$User tLRPC$User4 = this.user;
                                                            if (tLRPC$User4 != null) {
                                                                if (UserObject.isReplyUser(tLRPC$User4)) {
                                                                    removeDiacritics = LocaleController.getString(R.string.RepliesTitle);
                                                                } else if (UserObject.isAnonymous(this.user)) {
                                                                    removeDiacritics = LocaleController.getString(R.string.AnonymousForward);
                                                                } else if (UserObject.isUserSelf(this.user)) {
                                                                    if (this.isSavedDialog) {
                                                                        removeDiacritics = LocaleController.getString(R.string.MyNotes);
                                                                    } else if (this.useMeForMyMessages) {
                                                                        removeDiacritics = LocaleController.getString(R.string.FromYou);
                                                                    } else {
                                                                        if (this.dialogsType == 3) {
                                                                            this.drawPinBackground = true;
                                                                        }
                                                                        removeDiacritics = LocaleController.getString(R.string.SavedMessages);
                                                                    }
                                                                } else {
                                                                    removeDiacritics = AndroidUtilities.removeDiacritics(UserObject.getUserName(this.user));
                                                                }
                                                                if (removeDiacritics != null) {
                                                                    removeDiacritics = LocaleController.getString(R.string.HiddenName);
                                                                }
                                                            }
                                                            removeDiacritics = "";
                                                            if (removeDiacritics != null) {
                                                            }
                                                        }
                                                    }
                                                    spannableStringBuilder8 = spannableStringBuilder2;
                                                    CharSequence charSequence142 = removeDiacritics;
                                                    z11 = z4;
                                                    z12 = z9;
                                                    charSequence8 = string2;
                                                    str13 = str12;
                                                    charSequence9 = charSequence142;
                                                    int i222 = i3;
                                                    spannableStringBuilder9 = spannableStringBuilder3;
                                                    str14 = str11;
                                                    i7 = i222;
                                                    str15 = str9;
                                                }
                                                spannableStringBuilder2 = spannableStringBuilder;
                                                charSequence7 = string3;
                                                spannableStringBuilder3 = null;
                                                str10 = null;
                                                charSequence10 = charSequence7;
                                            }
                                        }
                                        z9 = true;
                                        string2 = charSequence10;
                                        str9 = str10;
                                        if (this.draftMessage != null) {
                                        }
                                        messageObject3 = this.message;
                                        if (messageObject3 != null) {
                                        }
                                        this.drawCheck1 = false;
                                        this.drawCheck2 = false;
                                        this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                        z10 = false;
                                        this.drawCount = false;
                                        this.drawMention = false;
                                        this.drawReactionMention = false;
                                        this.drawError = false;
                                        str11 = null;
                                        str12 = null;
                                        this.promoDialog = z10;
                                        messagesController = MessagesController.getInstance(this.currentAccount);
                                        if (this.dialogsType == 0) {
                                            this.drawPinBackground = true;
                                            this.promoDialog = true;
                                            i8 = messagesController.promoDialogType;
                                            if (i8 != MessagesController.PROMO_TYPE_PROXY) {
                                            }
                                        }
                                        if (this.currentDialogFolderId != 0) {
                                        }
                                        spannableStringBuilder8 = spannableStringBuilder2;
                                        CharSequence charSequence1422 = removeDiacritics;
                                        z11 = z4;
                                        z12 = z9;
                                        charSequence8 = string2;
                                        str13 = str12;
                                        charSequence9 = charSequence1422;
                                        int i2222 = i3;
                                        spannableStringBuilder9 = spannableStringBuilder3;
                                        str14 = str11;
                                        i7 = i2222;
                                        str15 = str9;
                                    }
                                }
                                tLRPC$DraftMessage3 = null;
                                this.draftMessage = null;
                                z3 = false;
                                this.draftVoice = false;
                                if (isForumCell()) {
                                }
                                z9 = true;
                                string2 = charSequence10;
                                str9 = str10;
                                if (this.draftMessage != null) {
                                }
                                messageObject3 = this.message;
                                if (messageObject3 != null) {
                                }
                                this.drawCheck1 = false;
                                this.drawCheck2 = false;
                                this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                z10 = false;
                                this.drawCount = false;
                                this.drawMention = false;
                                this.drawReactionMention = false;
                                this.drawError = false;
                                str11 = null;
                                str12 = null;
                                this.promoDialog = z10;
                                messagesController = MessagesController.getInstance(this.currentAccount);
                                if (this.dialogsType == 0) {
                                }
                                if (this.currentDialogFolderId != 0) {
                                }
                                spannableStringBuilder8 = spannableStringBuilder2;
                                CharSequence charSequence14222 = removeDiacritics;
                                z11 = z4;
                                z12 = z9;
                                charSequence8 = string2;
                                str13 = str12;
                                charSequence9 = charSequence14222;
                                int i22222 = i3;
                                spannableStringBuilder9 = spannableStringBuilder3;
                                str14 = str11;
                                i7 = i22222;
                                str15 = str9;
                            }
                        } else {
                            charSequence2 = charSequence;
                        }
                        z = false;
                        this.drawPremium = z;
                        if (z) {
                        }
                        i2 = this.lastMessageDate;
                        if (i2 == 0) {
                            i2 = messageObject4.messageOwner.date;
                        }
                        if (this.isTopic) {
                        }
                        z2 = this.draftVoice;
                        if (!z2) {
                            if (ChatObject.isChannel(this.chat)) {
                            }
                            tLRPC$Chat2 = this.chat;
                            if (tLRPC$Chat2 != null) {
                            }
                            tLRPC$DraftMessage3 = null;
                            z3 = false;
                            if (isForumCell()) {
                            }
                            z9 = true;
                            string2 = charSequence10;
                            str9 = str10;
                            if (this.draftMessage != null) {
                            }
                            messageObject3 = this.message;
                            if (messageObject3 != null) {
                            }
                            this.drawCheck1 = false;
                            this.drawCheck2 = false;
                            this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                            z10 = false;
                            this.drawCount = false;
                            this.drawMention = false;
                            this.drawReactionMention = false;
                            this.drawError = false;
                            str11 = null;
                            str12 = null;
                            this.promoDialog = z10;
                            messagesController = MessagesController.getInstance(this.currentAccount);
                            if (this.dialogsType == 0) {
                            }
                            if (this.currentDialogFolderId != 0) {
                            }
                            spannableStringBuilder8 = spannableStringBuilder2;
                            CharSequence charSequence142222 = removeDiacritics;
                            z11 = z4;
                            z12 = z9;
                            charSequence8 = string2;
                            str13 = str12;
                            charSequence9 = charSequence142222;
                            int i222222 = i3;
                            spannableStringBuilder9 = spannableStringBuilder3;
                            str14 = str11;
                            i7 = i222222;
                            str15 = str9;
                        }
                        if (ChatObject.isChannel(this.chat)) {
                        }
                        tLRPC$Chat2 = this.chat;
                        if (tLRPC$Chat2 != null) {
                        }
                        tLRPC$DraftMessage3 = null;
                        z3 = false;
                        if (isForumCell()) {
                        }
                        z9 = true;
                        string2 = charSequence10;
                        str9 = str10;
                        if (this.draftMessage != null) {
                        }
                        messageObject3 = this.message;
                        if (messageObject3 != null) {
                        }
                        this.drawCheck1 = false;
                        this.drawCheck2 = false;
                        this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                        z10 = false;
                        this.drawCount = false;
                        this.drawMention = false;
                        this.drawReactionMention = false;
                        this.drawError = false;
                        str11 = null;
                        str12 = null;
                        this.promoDialog = z10;
                        messagesController = MessagesController.getInstance(this.currentAccount);
                        if (this.dialogsType == 0) {
                        }
                        if (this.currentDialogFolderId != 0) {
                        }
                        spannableStringBuilder8 = spannableStringBuilder2;
                        CharSequence charSequence1422222 = removeDiacritics;
                        z11 = z4;
                        z12 = z9;
                        charSequence8 = string2;
                        str13 = str12;
                        charSequence9 = charSequence1422222;
                        int i2222222 = i3;
                        spannableStringBuilder9 = spannableStringBuilder3;
                        str14 = str11;
                        i7 = i2222222;
                        str15 = str9;
                    }
                }
            }
            charSequence2 = charSequence;
            i2 = this.lastMessageDate;
            if (i2 == 0) {
            }
            if (this.isTopic) {
            }
            z2 = this.draftVoice;
            if (!z2) {
            }
            if (ChatObject.isChannel(this.chat)) {
            }
            tLRPC$Chat2 = this.chat;
            if (tLRPC$Chat2 != null) {
            }
            tLRPC$DraftMessage3 = null;
            z3 = false;
            if (isForumCell()) {
            }
            z9 = true;
            string2 = charSequence10;
            str9 = str10;
            if (this.draftMessage != null) {
            }
            messageObject3 = this.message;
            if (messageObject3 != null) {
            }
            this.drawCheck1 = false;
            this.drawCheck2 = false;
            this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
            z10 = false;
            this.drawCount = false;
            this.drawMention = false;
            this.drawReactionMention = false;
            this.drawError = false;
            str11 = null;
            str12 = null;
            this.promoDialog = z10;
            messagesController = MessagesController.getInstance(this.currentAccount);
            if (this.dialogsType == 0) {
            }
            if (this.currentDialogFolderId != 0) {
            }
            spannableStringBuilder8 = spannableStringBuilder2;
            CharSequence charSequence14222222 = removeDiacritics;
            z11 = z4;
            z12 = z9;
            charSequence8 = string2;
            str13 = str12;
            charSequence9 = charSequence14222222;
            int i22222222 = i3;
            spannableStringBuilder9 = spannableStringBuilder3;
            str14 = str11;
            i7 = i22222222;
            str15 = str9;
        }
        if (!z12) {
            i9 = (int) Math.ceil(Theme.dialogs_timePaint.measureText(stringForMessageListDate));
            this.timeLayout = new StaticLayout(stringForMessageListDate, Theme.dialogs_timePaint, i9, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (!LocaleController.isRTL) {
                this.timeLeft = (getMeasuredWidth() - AndroidUtilities.dp(15.0f)) - i9;
            } else {
                this.timeLeft = AndroidUtilities.dp(15.0f);
            }
        } else {
            this.timeLayout = null;
            this.timeLeft = 0;
            i9 = 0;
        }
        if (drawLock2()) {
            f = 4.0f;
            i10 = 0;
        } else {
            if (LocaleController.isRTL) {
                f = 4.0f;
                this.lock2Left = this.timeLeft + i9 + AndroidUtilities.dp(4.0f);
            } else {
                f = 4.0f;
                this.lock2Left = (this.timeLeft - Theme.dialogs_lock2Drawable.getIntrinsicWidth()) - AndroidUtilities.dp(4.0f);
            }
            i10 = Theme.dialogs_lock2Drawable.getIntrinsicWidth() + AndroidUtilities.dp(f);
            i9 += i10;
        }
        if (LocaleController.isRTL) {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(22.0f)) - i9;
        } else {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(this.messagePaddingStart + 13)) - i9;
            this.nameLeft += i9;
        }
        if (this.drawNameLock) {
            int i33 = this.nameWidth;
            if (LocaleController.isRTL) {
                f = 8.0f;
            }
            this.nameWidth = i33 - (AndroidUtilities.dp(f) + Theme.dialogs_lockDrawable.getIntrinsicWidth());
        }
        if (!this.drawClock) {
            int intrinsicWidth2 = Theme.dialogs_clockDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            this.nameWidth -= intrinsicWidth2;
            if (!LocaleController.isRTL) {
                this.clockDrawLeft = (this.timeLeft - i10) - intrinsicWidth2;
            } else {
                this.clockDrawLeft = this.timeLeft + i9 + AndroidUtilities.dp(5.0f);
                this.nameLeft += intrinsicWidth2;
            }
        } else if (this.drawCheck2) {
            int intrinsicWidth3 = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            int i34 = this.nameWidth - intrinsicWidth3;
            this.nameWidth = i34;
            if (this.drawCheck1) {
                this.nameWidth = i34 - (Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f));
                if (!LocaleController.isRTL) {
                    int i35 = (this.timeLeft - i10) - intrinsicWidth3;
                    this.halfCheckDrawLeft = i35;
                    this.checkDrawLeft = i35 - AndroidUtilities.dp(5.5f);
                } else {
                    int dp8 = this.timeLeft + i9 + AndroidUtilities.dp(5.0f);
                    this.checkDrawLeft = dp8;
                    this.halfCheckDrawLeft = dp8 + AndroidUtilities.dp(5.5f);
                    this.nameLeft += (intrinsicWidth3 + Theme.dialogs_halfCheckDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(8.0f);
                }
            } else if (!LocaleController.isRTL) {
                this.checkDrawLeft1 = (this.timeLeft - i10) - intrinsicWidth3;
            } else {
                this.checkDrawLeft1 = this.timeLeft + i9 + AndroidUtilities.dp(5.0f);
                this.nameLeft += intrinsicWidth3;
            }
        }
        if (!this.drawPremium && this.emojiStatus.getDrawable() != null) {
            int dp9 = AndroidUtilities.dp(36.0f);
            this.nameWidth -= dp9;
            if (LocaleController.isRTL) {
                this.nameLeft += dp9;
            }
        } else if ((!this.dialogMuted || this.drawUnmute) && !this.drawVerified && this.drawScam == 0) {
            int dp72 = AndroidUtilities.dp(6.0f) + Theme.dialogs_muteDrawable.getIntrinsicWidth();
            this.nameWidth -= dp72;
            if (LocaleController.isRTL) {
                this.nameLeft += dp72;
            }
        } else if (this.drawVerified) {
            int dp10 = AndroidUtilities.dp(6.0f) + Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
            this.nameWidth -= dp10;
            if (LocaleController.isRTL) {
                this.nameLeft += dp10;
            }
        } else if (this.drawPremium) {
            int dp11 = AndroidUtilities.dp(36.0f);
            this.nameWidth -= dp11;
            if (LocaleController.isRTL) {
                this.nameLeft += dp11;
            }
        } else if (this.drawScam != 0) {
            int dp12 = AndroidUtilities.dp(6.0f) + (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
            this.nameWidth -= dp12;
            if (LocaleController.isRTL) {
                this.nameLeft += dp12;
            }
        }
        try {
            dp6 = this.nameWidth - AndroidUtilities.dp(12.0f);
            if (dp6 < 0) {
                dp6 = 0;
            }
            if (charSequence9 instanceof String) {
                charSequence9 = ((String) charSequence9).replace('\n', ' ');
            }
            if (this.nameLayoutEllipsizeByGradient) {
                this.nameLayoutFits = charSequence9.length() == TextUtils.ellipsize(charSequence9, Theme.dialogs_namePaint[this.paintIndex], (float) dp6, TextUtils.TruncateAt.END).length();
                dp6 += AndroidUtilities.dp(48.0f);
            }
            float f22 = dp6;
            this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(charSequence9.toString()) <= f22;
            if (!this.twoLinesForName) {
                charSequence9 = TextUtils.ellipsize(charSequence9, Theme.dialogs_namePaint[this.paintIndex], f22, TextUtils.TruncateAt.END);
            }
            CharSequence replaceEmoji22 = Emoji.replaceEmoji(charSequence9, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            MessageObject messageObject102 = this.message;
            CharSequence charSequence19 = (messageObject102 == null && messageObject102.hasHighlightedWords() && (highlightText3 = AndroidUtilities.highlightText(replaceEmoji22, this.message.highlightedWords, this.resourcesProvider)) != null) ? highlightText3 : replaceEmoji22;
            if (!this.twoLinesForName) {
                this.nameLayout = StaticLayoutEx.createStaticLayout(charSequence19, Theme.dialogs_namePaint[this.paintIndex], dp6, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, dp6, 2);
            } else {
                this.nameLayout = new StaticLayout(charSequence19, Theme.dialogs_namePaint[this.paintIndex], Math.max(dp6, this.nameWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            this.nameLayoutTranslateX = (this.nameLayoutEllipsizeByGradient || !this.nameLayout.isRtlCharAt(0)) ? 0.0f : -AndroidUtilities.dp(36.0f);
            this.nameLayoutEllipsizeLeft = this.nameLayout.isRtlCharAt(0);
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        this.animatedEmojiStackName = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStackName, this.nameLayout);
        if (!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
            dp = AndroidUtilities.dp(11.0f);
            this.messageNameTop = AndroidUtilities.dp(32.0f);
            this.timeTop = AndroidUtilities.dp(13.0f);
            this.errorTop = AndroidUtilities.dp(43.0f);
            this.pinTop = AndroidUtilities.dp(43.0f);
            this.countTop = AndroidUtilities.dp(43.0f);
            this.checkDrawTop = AndroidUtilities.dp(13.0f);
            int measuredWidth3 = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 21);
            if (!LocaleController.isRTL) {
                int dp13 = AndroidUtilities.dp(16.0f);
                this.messageNameLeft = dp13;
                this.messageLeft = dp13;
                this.typingLeft = dp13;
                this.buttonLeft = dp13;
                dp2 = getMeasuredWidth() - AndroidUtilities.dp(this.avatarStart + 56);
                dp3 = dp2 - AndroidUtilities.dp(31.0f);
            } else {
                int dp14 = AndroidUtilities.dp(this.messagePaddingStart + 6);
                this.messageNameLeft = dp14;
                this.messageLeft = dp14;
                this.typingLeft = dp14;
                this.buttonLeft = dp14;
                dp2 = AndroidUtilities.dp(this.avatarStart);
                dp3 = AndroidUtilities.dp(69.0f) + dp2;
            }
            i11 = measuredWidth3;
            this.storyParams.originalAvatarRect.set(dp2, dp, dp2 + AndroidUtilities.dp(56.0f), dp + AndroidUtilities.dp(56.0f));
            i12 = 0;
            while (true) {
                imageReceiverArr = this.thumbImage;
                if (i12 < imageReceiverArr.length) {
                    break;
                }
                imageReceiverArr[i12].setImageCoords(((this.thumbSize + 2) * i12) + dp3, ((AndroidUtilities.dp(31.0f) + dp) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0)) - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout || (dialogCellTags = this.tags) == null || dialogCellTags.isEmpty()) ? 0 : AndroidUtilities.dp(9.0f)), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                i12++;
                dp = dp;
            }
        } else {
            dp = AndroidUtilities.dp(9.0f);
            this.messageNameTop = AndroidUtilities.dp(31.0f);
            this.timeTop = AndroidUtilities.dp(16.0f);
            this.errorTop = AndroidUtilities.dp(39.0f);
            this.pinTop = AndroidUtilities.dp(39.0f);
            this.countTop = this.isTopic ? AndroidUtilities.dp(36.0f) : AndroidUtilities.dp(39.0f);
            this.checkDrawTop = AndroidUtilities.dp(17.0f);
            int measuredWidth4 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) - (LocaleController.isRTL ? 0 : 12));
            if (LocaleController.isRTL) {
                int dp15 = AndroidUtilities.dp(22.0f);
                this.messageNameLeft = dp15;
                this.messageLeft = dp15;
                this.typingLeft = dp15;
                this.buttonLeft = dp15;
                dp4 = getMeasuredWidth() - AndroidUtilities.dp(this.avatarStart + 54);
                dp5 = dp4 - AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 9);
            } else {
                int dp16 = AndroidUtilities.dp(this.messagePaddingStart + 4);
                this.messageNameLeft = dp16;
                this.messageLeft = dp16;
                this.typingLeft = dp16;
                this.buttonLeft = dp16;
                dp4 = AndroidUtilities.dp(this.avatarStart);
                dp5 = AndroidUtilities.dp(67.0f) + dp4;
            }
            int i36 = dp4;
            int i37 = dp5;
            i11 = measuredWidth4;
            this.storyParams.originalAvatarRect.set(i36, dp, i36 + AndroidUtilities.dp(54.0f), dp + AndroidUtilities.dp(54.0f));
            int i38 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr2 = this.thumbImage;
                if (i38 >= imageReceiverArr2.length) {
                    break;
                }
                imageReceiverArr2[i38].setImageCoords(((this.thumbSize + 2) * i38) + i37, ((AndroidUtilities.dp(30.0f) + dp) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0)) - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout || (dialogCellTags3 = this.tags) == null || dialogCellTags3.isEmpty()) ? 0 : AndroidUtilities.dp(9.0f)), AndroidUtilities.dp(this.thumbSize), AndroidUtilities.dp(this.thumbSize));
                i38++;
                dp = dp;
            }
        }
        int i232 = dp;
        int i242 = i11;
        if (!LocaleController.isRTL) {
            this.tagsRight = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart);
            this.tagsLeft = AndroidUtilities.dp(64.0f);
        } else {
            this.tagsLeft = this.messageLeft;
            this.tagsRight = getMeasuredWidth() - AndroidUtilities.dp(64.0f);
        }
        if (this.twoLinesForName) {
            this.messageNameTop += AndroidUtilities.dp(20.0f);
        }
        if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && (dialogCellTags2 = this.tags) != null && !dialogCellTags2.isEmpty()) {
            this.timeTop -= AndroidUtilities.dp(6.0f);
            this.checkDrawTop -= AndroidUtilities.dp(6.0f);
        }
        if (getIsPinned()) {
            if (!LocaleController.isRTL) {
                this.pinLeft = (getMeasuredWidth() - Theme.dialogs_pinnedDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(14.0f);
            } else {
                this.pinLeft = AndroidUtilities.dp(14.0f);
            }
        }
        if (this.drawError) {
            int dp17 = AndroidUtilities.dp(31.0f);
            i242 -= dp17;
            if (!LocaleController.isRTL) {
                this.errorLeft = getMeasuredWidth() - AndroidUtilities.dp(34.0f);
            } else {
                this.errorLeft = AndroidUtilities.dp(11.0f);
                this.messageLeft += dp17;
                this.typingLeft += dp17;
                this.buttonLeft += dp17;
                this.messageNameLeft += dp17;
            }
        } else if (str14 != null || str13 != null || this.drawReactionMention) {
            if (str14 != null) {
                this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str14)));
                this.countLayout = new StaticLayout(str14, Theme.dialogs_countTextPaint, this.countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                int dp18 = this.countWidth + AndroidUtilities.dp(18.0f);
                i242 -= dp18;
                if (!LocaleController.isRTL) {
                    this.countLeft = (getMeasuredWidth() - this.countWidth) - AndroidUtilities.dp(20.0f);
                } else {
                    this.countLeft = AndroidUtilities.dp(20.0f);
                    this.messageLeft += dp18;
                    this.typingLeft += dp18;
                    this.buttonLeft += dp18;
                    this.messageNameLeft += dp18;
                }
                this.drawCount = true;
            } else {
                this.countWidth = 0;
            }
            if (str13 != null) {
                if (this.currentDialogFolderId != 0) {
                    this.mentionWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str13)));
                    this.mentionLayout = new StaticLayout(str13, Theme.dialogs_countTextPaint, this.mentionWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                } else {
                    this.mentionWidth = AndroidUtilities.dp(12.0f);
                }
                int dp19 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                i242 -= dp19;
                if (!LocaleController.isRTL) {
                    int measuredWidth5 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                    int i39 = this.countWidth;
                    this.mentionLeft = measuredWidth5 - (i39 != 0 ? i39 + AndroidUtilities.dp(18.0f) : 0);
                } else {
                    int dp20 = AndroidUtilities.dp(20.0f);
                    int i40 = this.countWidth;
                    this.mentionLeft = dp20 + (i40 != 0 ? i40 + AndroidUtilities.dp(18.0f) : 0);
                    this.messageLeft += dp19;
                    this.typingLeft += dp19;
                    this.buttonLeft += dp19;
                    this.messageNameLeft += dp19;
                }
                this.drawMention = true;
            } else {
                this.mentionWidth = 0;
            }
            if (this.drawReactionMention) {
                int dp21 = AndroidUtilities.dp(24.0f);
                i242 -= dp21;
                if (!LocaleController.isRTL) {
                    int measuredWidth6 = getMeasuredWidth() - AndroidUtilities.dp(32.0f);
                    this.reactionMentionLeft = measuredWidth6;
                    if (this.drawMention) {
                        int i41 = this.mentionWidth;
                        this.reactionMentionLeft = measuredWidth6 - (i41 != 0 ? i41 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i42 = this.reactionMentionLeft;
                        int i43 = this.countWidth;
                        this.reactionMentionLeft = i42 - (i43 != 0 ? i43 + AndroidUtilities.dp(18.0f) : 0);
                    }
                } else {
                    int dp22 = AndroidUtilities.dp(20.0f);
                    this.reactionMentionLeft = dp22;
                    if (this.drawMention) {
                        int i44 = this.mentionWidth;
                        this.reactionMentionLeft = dp22 + (i44 != 0 ? i44 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i45 = this.reactionMentionLeft;
                        int i46 = this.countWidth;
                        this.reactionMentionLeft = i45 + (i46 != 0 ? i46 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    this.messageLeft += dp21;
                    this.typingLeft += dp21;
                    this.buttonLeft += dp21;
                    this.messageNameLeft += dp21;
                }
            }
        } else {
            if (getIsPinned()) {
                int intrinsicWidth4 = Theme.dialogs_pinnedDrawable.getIntrinsicWidth() + AndroidUtilities.dp(8.0f);
                i242 -= intrinsicWidth4;
                if (LocaleController.isRTL) {
                    this.messageLeft += intrinsicWidth4;
                    this.typingLeft += intrinsicWidth4;
                    this.buttonLeft += intrinsicWidth4;
                    this.messageNameLeft += intrinsicWidth4;
                }
            }
            this.drawCount = false;
            this.drawMention = false;
        }
        if (z11) {
            if (charSequence8 == null) {
                charSequence8 = "";
            }
            if (charSequence8.length() > 150) {
                charSequence8 = charSequence8.subSequence(0, 150);
            }
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || hasTags() || str15 != null) {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceNewLines(charSequence8);
            } else {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceTwoNewLinesToOne(charSequence8);
            }
            CharSequence replaceEmoji3 = Emoji.replaceEmoji(replaceTwoNewLinesToOne, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
            MessageObject messageObject17 = this.message;
            if (messageObject17 == null || (charSequence8 = AndroidUtilities.highlightText(replaceEmoji3, messageObject17.highlightedWords, this.resourcesProvider)) == null) {
                charSequence8 = replaceEmoji3;
            }
        }
        int max22 = Math.max(AndroidUtilities.dp(12.0f), i242);
        this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
        if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
            this.buttonTop -= AndroidUtilities.dp(!isForumCell() ? 10.0f : 12.0f);
        }
        if (!isForumCell()) {
            this.messageTop = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 34.0f : 39.0f);
            int i47 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr3 = this.thumbImage;
                if (i47 >= imageReceiverArr3.length) {
                    break;
                }
                imageReceiverArr3[i47].setImageY(this.buttonTop);
                i47++;
            }
        } else if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags() && str15 != null && (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount == 1)) {
            try {
                MessageObject messageObject18 = this.message;
                str15 = str15;
                if (messageObject18 != null) {
                    str15 = str15;
                    if (messageObject18.hasHighlightedWords()) {
                        CharSequence highlightText4 = AndroidUtilities.highlightText(str15, this.message.highlightedWords, this.resourcesProvider);
                        str15 = str15;
                        if (highlightText4 != null) {
                            str15 = highlightText4;
                        }
                    }
                }
                this.messageNameLayout = StaticLayoutEx.createStaticLayout(str15, Theme.dialogs_messageNamePaint, max22, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max22, 1);
                str15 = str15;
            } catch (Exception e4) {
                FileLog.e(e4);
                str15 = str15;
            }
            this.messageTop = AndroidUtilities.dp(51.0f);
            int dp23 = (this.nameIsEllipsized && this.isTopic) ? AndroidUtilities.dp(20.0f) : 0;
            int i48 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr4 = this.thumbImage;
                if (i48 >= imageReceiverArr4.length) {
                    break;
                }
                imageReceiverArr4[i48].setImageY(i232 + dp23 + AndroidUtilities.dp(40.0f));
                i48++;
            }
        } else {
            this.messageNameLayout = null;
            if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                this.messageTop = AndroidUtilities.dp(32.0f);
                int dp24 = (this.nameIsEllipsized && this.isTopic) ? AndroidUtilities.dp(20.0f) : 0;
                int i49 = 0;
                while (true) {
                    ImageReceiver[] imageReceiverArr5 = this.thumbImage;
                    if (i49 >= imageReceiverArr5.length) {
                        break;
                    }
                    imageReceiverArr5[i49].setImageY(i232 + dp24 + AndroidUtilities.dp(21.0f));
                    i49++;
                }
            } else {
                this.messageTop = AndroidUtilities.dp(39.0f);
            }
        }
        CharSequence charSequence15222 = str15;
        if (this.twoLinesForName) {
            this.messageTop += AndroidUtilities.dp(20.0f);
        }
        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack2, this.messageNameLayout);
        try {
            this.buttonCreated = false;
            if (TextUtils.isEmpty(spannableStringBuilder9)) {
                this.buttonLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji((CharSequence) spannableStringBuilder9, this.currentMessagePaint.getFontMetricsInt(), AndroidUtilities.dp(17.0f), false), this.currentMessagePaint, max22 - AndroidUtilities.dp(26.0f), TextUtils.TruncateAt.END), this.currentMessagePaint, max22 - AndroidUtilities.dp(20.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                this.spoilersPool2.addAll(this.spoilers2);
                this.spoilers2.clear();
                SpoilerEffect.addSpoilers((View) this, this.buttonLayout, this.spoilersPool2, this.spoilers2);
            } else {
                this.buttonLayout = null;
            }
        } catch (Exception unused) {
        }
        this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack3, this.buttonLayout);
        try {
            if (!TextUtils.isEmpty(spannableStringBuilder8)) {
                if (!this.useForceThreeLines) {
                    if (!SharedConfig.useThreeLinesLayout) {
                    }
                    this.typingLayout = new StaticLayout(TextUtils.ellipsize(spannableStringBuilder8, this.currentMessagePaint, max22 - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), Theme.dialogs_messagePrintingPaint[this.paintIndex], max22, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                }
                if (!hasTags()) {
                    this.typingLayout = StaticLayoutEx.createStaticLayout(spannableStringBuilder8, Theme.dialogs_messagePrintingPaint[this.paintIndex], max22, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max22, 1);
                }
                this.typingLayout = new StaticLayout(TextUtils.ellipsize(spannableStringBuilder8, this.currentMessagePaint, max22 - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), Theme.dialogs_messagePrintingPaint[this.paintIndex], max22, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
        } catch (Exception e5) {
            FileLog.e(e5);
        }
        try {
            if (charSequence8 instanceof Spannable) {
                try {
                    Spannable spannable = (Spannable) charSequence8;
                    for (Object obj : spannable.getSpans(0, spannable.length(), Object.class)) {
                        if (!(obj instanceof ClickableSpan)) {
                            if (!(obj instanceof CodeHighlighting.Span)) {
                                if (!isFolderCell()) {
                                    if (!(obj instanceof TypefaceSpan)) {
                                    }
                                }
                                if (!(obj instanceof CodeHighlighting.ColorSpan)) {
                                    if (!(obj instanceof QuoteSpan)) {
                                        if (!(obj instanceof QuoteSpan.QuoteStyleSpan)) {
                                            if ((obj instanceof StyleSpan) && ((StyleSpan) obj).getStyle() == 1) {
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        spannable.removeSpan(obj);
                    }
                } catch (Exception e6) {
                    e = e6;
                    StaticLayout staticLayout2 = null;
                    i13 = 1;
                    this.messageLayout = staticLayout2;
                    FileLog.e(e);
                    i14 = max22;
                    AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans222 = this.animatedEmojiStack;
                    Layout[] layoutArr222 = new Layout[i13];
                    layoutArr222[0] = this.messageLayout;
                    this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, emojiGroupedSpans222, layoutArr222);
                    if (!LocaleController.isRTL) {
                    }
                    staticLayout = this.typingLayout;
                    if (staticLayout != null) {
                    }
                    updateThumbsPosition();
                }
            }
        } catch (Exception e7) {
            e = e7;
            i13 = 1;
        }
        if ((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
            if (this.currentDialogFolderId != 0) {
                if (this.currentDialogFolderDialogsCount > 1) {
                    this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                    charSequence11 = charSequence15222;
                    charSequence15222 = null;
                    alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                    if ((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                        if (this.thumbsCount > 0 && charSequence15222 != null) {
                            max22 += AndroidUtilities.dp(5.0f);
                        }
                        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence11, this.currentMessagePaint, max22, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max22, charSequence15222 != null ? 1 : 2);
                    } else {
                        if (this.thumbsCount > 0) {
                            max22 += AndroidUtilities.dp((i16 * (this.thumbSize + 2)) + 3);
                            if (LocaleController.isRTL && !isForumCell()) {
                                this.messageLeft -= AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 3);
                            }
                        }
                        this.messageLayout = new StaticLayout(charSequence11, this.currentMessagePaint, max22, alignment, 1.0f, 0.0f, false);
                    }
                    i14 = max22;
                    this.spoilersPool.addAll(this.spoilers);
                    this.spoilers.clear();
                    i13 = 1;
                    SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                    AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans2222 = this.animatedEmojiStack;
                    Layout[] layoutArr2222 = new Layout[i13];
                    layoutArr2222[0] = this.messageLayout;
                    this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, emojiGroupedSpans2222, layoutArr2222);
                    if (!LocaleController.isRTL) {
                        StaticLayout staticLayout3 = this.nameLayout;
                        if (staticLayout3 != null && staticLayout3.getLineCount() > 0) {
                            float lineLeft = this.nameLayout.getLineLeft(0);
                            double ceil = Math.ceil(this.nameLayout.getLineWidth(0));
                            this.nameLeft += AndroidUtilities.dp(12.0f);
                            if (this.nameLayoutEllipsizeByGradient) {
                                ceil = Math.min(this.nameWidth, ceil);
                            }
                            if ((this.dialogMuted || this.drawUnmute) && !this.drawVerified && this.drawScam == 0) {
                                double d = this.nameLeft;
                                double d2 = this.nameWidth;
                                Double.isNaN(d2);
                                Double.isNaN(d);
                                double d3 = d + (d2 - ceil);
                                double dp25 = AndroidUtilities.dp(6.0f);
                                Double.isNaN(dp25);
                                double d4 = d3 - dp25;
                                double intrinsicWidth5 = Theme.dialogs_muteDrawable.getIntrinsicWidth();
                                Double.isNaN(intrinsicWidth5);
                                this.nameMuteLeft = (int) (d4 - intrinsicWidth5);
                            } else if (this.drawVerified) {
                                double d5 = this.nameLeft;
                                double d6 = this.nameWidth;
                                Double.isNaN(d6);
                                Double.isNaN(d5);
                                double d7 = d5 + (d6 - ceil);
                                double dp26 = AndroidUtilities.dp(6.0f);
                                Double.isNaN(dp26);
                                double d8 = d7 - dp26;
                                double intrinsicWidth6 = Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                                Double.isNaN(intrinsicWidth6);
                                this.nameMuteLeft = (int) (d8 - intrinsicWidth6);
                            } else if (this.drawPremium) {
                                double d9 = this.nameLeft;
                                double d10 = this.nameWidth;
                                Double.isNaN(d10);
                                double d11 = lineLeft;
                                Double.isNaN(d11);
                                Double.isNaN(d9);
                                double d12 = d9 + ((d10 - ceil) - d11);
                                double dp27 = AndroidUtilities.dp(24.0f);
                                Double.isNaN(dp27);
                                this.nameMuteLeft = (int) (d12 - dp27);
                            } else if (this.drawScam != 0) {
                                double d13 = this.nameLeft;
                                double d14 = this.nameWidth;
                                Double.isNaN(d14);
                                Double.isNaN(d13);
                                double d15 = d13 + (d14 - ceil);
                                double dp28 = AndroidUtilities.dp(6.0f);
                                Double.isNaN(dp28);
                                double d16 = d15 - dp28;
                                double intrinsicWidth7 = (this.drawScam == i13 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
                                Double.isNaN(intrinsicWidth7);
                                this.nameMuteLeft = (int) (d16 - intrinsicWidth7);
                            } else {
                                double d17 = this.nameLeft;
                                double d18 = this.nameWidth;
                                Double.isNaN(d18);
                                Double.isNaN(d17);
                                double d19 = d17 + (d18 - ceil);
                                double dp29 = AndroidUtilities.dp(6.0f);
                                Double.isNaN(dp29);
                                double d20 = d19 - dp29;
                                double intrinsicWidth8 = Theme.dialogs_muteDrawable.getIntrinsicWidth();
                                Double.isNaN(intrinsicWidth8);
                                this.nameMuteLeft = (int) (d20 - intrinsicWidth8);
                            }
                            if (lineLeft == 0.0f) {
                                double d21 = this.nameWidth;
                                if (ceil < d21) {
                                    double d22 = this.nameLeft;
                                    Double.isNaN(d21);
                                    Double.isNaN(d22);
                                    this.nameLeft = (int) (d22 + (d21 - ceil));
                                }
                            }
                        }
                        StaticLayout staticLayout4 = this.messageLayout;
                        int i50 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                        if (staticLayout4 != null && (lineCount6 = staticLayout4.getLineCount()) > 0) {
                            int i51 = 0;
                            int i52 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                            while (true) {
                                if (i51 >= lineCount6) {
                                    break;
                                }
                                if (this.messageLayout.getLineLeft(i51) != 0.0f) {
                                    i52 = 0;
                                    break;
                                }
                                double ceil2 = Math.ceil(this.messageLayout.getLineWidth(i51));
                                double d23 = i14;
                                Double.isNaN(d23);
                                i52 = Math.min(i52, (int) (d23 - ceil2));
                                i51 += i13;
                            }
                            if (i52 != Integer.MAX_VALUE) {
                                this.messageLeft += i52;
                            }
                        }
                        StaticLayout staticLayout5 = this.typingLayout;
                        if (staticLayout5 != null && (lineCount5 = staticLayout5.getLineCount()) > 0) {
                            int i53 = 0;
                            int i54 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                            while (true) {
                                if (i53 >= lineCount5) {
                                    break;
                                }
                                if (this.typingLayout.getLineLeft(i53) != 0.0f) {
                                    i54 = 0;
                                    break;
                                }
                                double ceil3 = Math.ceil(this.typingLayout.getLineWidth(i53));
                                double d24 = i14;
                                Double.isNaN(d24);
                                i54 = Math.min(i54, (int) (d24 - ceil3));
                                i53 += i13;
                            }
                            if (i54 != Integer.MAX_VALUE) {
                                this.typingLeft += i54;
                            }
                        }
                        StaticLayout staticLayout6 = this.messageNameLayout;
                        if (staticLayout6 != null && staticLayout6.getLineCount() > 0 && this.messageNameLayout.getLineLeft(0) == 0.0f) {
                            double ceil4 = Math.ceil(this.messageNameLayout.getLineWidth(0));
                            double d25 = i14;
                            if (ceil4 < d25) {
                                double d26 = this.messageNameLeft;
                                Double.isNaN(d25);
                                Double.isNaN(d26);
                                this.messageNameLeft = (int) (d26 + (d25 - ceil4));
                            }
                        }
                        StaticLayout staticLayout7 = this.buttonLayout;
                        if (staticLayout7 != null && (lineCount4 = staticLayout7.getLineCount()) > 0) {
                            for (int i55 = 0; i55 < lineCount4; i55 += i13) {
                                i50 = (int) Math.min(i50, this.buttonLayout.getWidth() - this.buttonLayout.getLineRight(i55));
                            }
                            this.buttonLeft += i50;
                        }
                    } else {
                        StaticLayout staticLayout8 = this.nameLayout;
                        if (staticLayout8 != null && staticLayout8.getLineCount() > 0) {
                            float lineRight = this.nameLayout.getLineRight(0);
                            if (this.nameLayoutEllipsizeByGradient) {
                                lineRight = Math.min(this.nameWidth, lineRight);
                            }
                            if (lineRight == this.nameWidth) {
                                double ceil5 = Math.ceil(this.nameLayout.getLineWidth(0));
                                if (this.nameLayoutEllipsizeByGradient) {
                                    ceil5 = Math.min(this.nameWidth, ceil5);
                                }
                                double d27 = this.nameWidth;
                                if (ceil5 < d27) {
                                    double d28 = this.nameLeft;
                                    Double.isNaN(d27);
                                    Double.isNaN(d28);
                                    this.nameLeft = (int) (d28 - (d27 - ceil5));
                                }
                            }
                            this.nameMuteLeft = (int) (this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                        }
                        StaticLayout staticLayout9 = this.messageLayout;
                        if (staticLayout9 != null && (lineCount3 = staticLayout9.getLineCount()) > 0) {
                            float f3 = 2.14748365E9f;
                            for (int i56 = 0; i56 < lineCount3; i56 += i13) {
                                f3 = Math.min(f3, this.messageLayout.getLineLeft(i56));
                            }
                            this.messageLeft = (int) (this.messageLeft - f3);
                        }
                        StaticLayout staticLayout10 = this.buttonLayout;
                        if (staticLayout10 != null && (lineCount2 = staticLayout10.getLineCount()) > 0) {
                            float f4 = 2.14748365E9f;
                            for (int i57 = 0; i57 < lineCount2; i57 += i13) {
                                f4 = Math.min(f4, this.buttonLayout.getLineLeft(i57));
                            }
                            this.buttonLeft = (int) (this.buttonLeft - f4);
                        }
                        StaticLayout staticLayout11 = this.typingLayout;
                        if (staticLayout11 != null && (lineCount = staticLayout11.getLineCount()) > 0) {
                            float f5 = 2.14748365E9f;
                            for (int i58 = 0; i58 < lineCount; i58 += i13) {
                                f5 = Math.min(f5, this.typingLayout.getLineLeft(i58));
                            }
                            this.typingLeft = (int) (this.typingLeft - f5);
                        }
                        StaticLayout staticLayout12 = this.messageNameLayout;
                        if (staticLayout12 != null && staticLayout12.getLineCount() > 0) {
                            this.messageNameLeft = (int) (this.messageNameLeft - this.messageNameLayout.getLineLeft(0));
                        }
                    }
                    staticLayout = this.typingLayout;
                    if (staticLayout != null && this.printingStringType >= 0 && staticLayout.getText().length() > 0) {
                        if (i7 < 0 && (i15 = i7 + 1) < this.typingLayout.getText().length()) {
                            primaryHorizontal = this.typingLayout.getPrimaryHorizontal(i7);
                            primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(i15);
                        } else {
                            primaryHorizontal = this.typingLayout.getPrimaryHorizontal(0);
                            primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(i13);
                        }
                        if (primaryHorizontal >= primaryHorizontal2) {
                            this.statusDrawableLeft = (int) (this.typingLeft + primaryHorizontal);
                        } else {
                            this.statusDrawableLeft = (int) (this.typingLeft + primaryHorizontal2 + AndroidUtilities.dp(3.0f));
                        }
                    }
                    updateThumbsPosition();
                }
                if (!this.useForceThreeLines) {
                }
                if (!hasTags()) {
                    if (charSequence15222 != null) {
                    }
                    charSequence11 = charSequence8;
                    alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                    if (!this.useForceThreeLines) {
                    }
                    if (this.thumbsCount > 0) {
                        max22 += AndroidUtilities.dp(5.0f);
                    }
                    this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence11, this.currentMessagePaint, max22, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max22, charSequence15222 != null ? 1 : 2);
                    i14 = max22;
                    this.spoilersPool.addAll(this.spoilers);
                    this.spoilers.clear();
                    i13 = 1;
                    SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                    AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans22222 = this.animatedEmojiStack;
                    Layout[] layoutArr22222 = new Layout[i13];
                    layoutArr22222[0] = this.messageLayout;
                    this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, emojiGroupedSpans22222, layoutArr22222);
                    if (!LocaleController.isRTL) {
                    }
                    staticLayout = this.typingLayout;
                    if (staticLayout != null) {
                        if (i7 < 0) {
                        }
                        primaryHorizontal = this.typingLayout.getPrimaryHorizontal(0);
                        primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(i13);
                        if (primaryHorizontal >= primaryHorizontal2) {
                        }
                    }
                    updateThumbsPosition();
                }
                if (isForumCell() && (charSequence8 instanceof Spanned) && ((FixedWidthSpan[]) ((Spanned) charSequence8).getSpans(0, charSequence8.length(), FixedWidthSpan.class)).length <= 0) {
                    ellipsize = TextUtils.ellipsize(charSequence8, this.currentMessagePaint, max22 - AndroidUtilities.dp((this.thumbsCount * (this.thumbSize + 2)) + 15), TextUtils.TruncateAt.END);
                } else {
                    ellipsize = TextUtils.ellipsize(charSequence8, this.currentMessagePaint, max22 - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
                }
                charSequence8 = ellipsize;
                charSequence11 = charSequence8;
                alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                if (!this.useForceThreeLines) {
                }
                if (this.thumbsCount > 0) {
                }
                this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence11, this.currentMessagePaint, max22, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max22, charSequence15222 != null ? 1 : 2);
                i14 = max22;
                this.spoilersPool.addAll(this.spoilers);
                this.spoilers.clear();
                i13 = 1;
                SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans222222 = this.animatedEmojiStack;
                Layout[] layoutArr222222 = new Layout[i13];
                layoutArr222222[0] = this.messageLayout;
                this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, emojiGroupedSpans222222, layoutArr222222);
                if (!LocaleController.isRTL) {
                }
                staticLayout = this.typingLayout;
                if (staticLayout != null) {
                }
                updateThumbsPosition();
            }
        }
        if (!this.useForceThreeLines) {
        }
        if (!hasTags()) {
        }
        if (isForumCell()) {
        }
        ellipsize = TextUtils.ellipsize(charSequence8, this.currentMessagePaint, max22 - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
        charSequence8 = ellipsize;
        charSequence11 = charSequence8;
        alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
        if (!this.useForceThreeLines) {
        }
        if (this.thumbsCount > 0) {
        }
        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence11, this.currentMessagePaint, max22, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max22, charSequence15222 != null ? 1 : 2);
        i14 = max22;
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        i13 = 1;
        SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
        AnimatedEmojiSpan.EmojiGroupedSpans emojiGroupedSpans2222222 = this.animatedEmojiStack;
        Layout[] layoutArr2222222 = new Layout[i13];
        layoutArr2222222[0] = this.messageLayout;
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, emojiGroupedSpans2222222, layoutArr2222222);
        if (!LocaleController.isRTL) {
        }
        staticLayout = this.typingLayout;
        if (staticLayout != null) {
        }
        updateThumbsPosition();
    }

    private SpannableStringBuilder formatInternal(int i, CharSequence charSequence, CharSequence charSequence2) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (i == 1) {
            spannableStringBuilder.append(charSequence2).append((CharSequence) ": \u2068").append(charSequence).append((CharSequence) "\u2069");
        } else if (i == 2) {
            spannableStringBuilder.append((CharSequence) "\u2068").append(charSequence).append((CharSequence) "\u2069");
        } else if (i == 3) {
            spannableStringBuilder.append(charSequence2).append((CharSequence) ": ").append(charSequence);
        } else if (i == 4) {
            spannableStringBuilder.append(charSequence);
        }
        return spannableStringBuilder;
    }

    private void updateThumbsPosition() {
        if (this.thumbsCount > 0) {
            StaticLayout staticLayout = isForumCell() ? this.buttonLayout : this.messageLayout;
            int i = isForumCell() ? this.buttonLeft : this.messageLeft;
            if (staticLayout == null) {
                return;
            }
            try {
                CharSequence text = staticLayout.getText();
                if (text instanceof Spanned) {
                    FixedWidthSpan[] fixedWidthSpanArr = (FixedWidthSpan[]) ((Spanned) text).getSpans(0, text.length(), FixedWidthSpan.class);
                    if (fixedWidthSpanArr == null || fixedWidthSpanArr.length <= 0) {
                        for (int i2 = 0; i2 < 3; i2++) {
                            this.thumbImageSeen[i2] = false;
                        }
                        return;
                    }
                    int spanStart = ((Spanned) text).getSpanStart(fixedWidthSpanArr[0]);
                    if (spanStart < 0) {
                        spanStart = 0;
                    }
                    int ceil = (int) Math.ceil(Math.min(staticLayout.getPrimaryHorizontal(spanStart), staticLayout.getPrimaryHorizontal(spanStart + 1)));
                    if (ceil != 0 && !this.drawForwardIcon) {
                        ceil += AndroidUtilities.dp(3.0f);
                    }
                    for (int i3 = 0; i3 < this.thumbsCount; i3++) {
                        this.thumbImage[i3].setImageX(i + ceil + AndroidUtilities.dp((this.thumbSize + 2) * i3));
                        this.thumbImageSeen[i3] = true;
                    }
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    private CharSequence applyThumbs(CharSequence charSequence) {
        if (this.thumbsCount > 0) {
            SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(charSequence);
            valueOf.insert(0, (CharSequence) " ");
            valueOf.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbSize + 2) * this.thumbsCount) + 3)), 0, 1, 33);
            return valueOf;
        }
        return charSequence;
    }

    private CharSequence formatTopicsNames() {
        ForumFormattedNames forumFormattedNames = new ForumFormattedNames();
        forumFormattedNames.formatTopicsNames(this.currentAccount, this.message, this.chat);
        this.topMessageTopicStartIndex = forumFormattedNames.topMessageTopicStartIndex;
        this.topMessageTopicEndIndex = forumFormattedNames.topMessageTopicEndIndex;
        this.lastTopicMessageUnread = forumFormattedNames.lastTopicMessageUnread;
        return forumFormattedNames.formattedNames;
    }

    public boolean isForumCell() {
        TLRPC$Chat tLRPC$Chat;
        return (isDialogFolder() || (tLRPC$Chat = this.chat) == null || !tLRPC$Chat.forum || this.isTopic) ? false : true;
    }

    private void drawCheckStatus(Canvas canvas, boolean z, boolean z2, boolean z3, boolean z4, float f) {
        if (f != 0.0f || z4) {
            float f2 = (f * 0.5f) + 0.5f;
            if (z) {
                BaseCell.setDrawableBounds(Theme.dialogs_clockDrawable, this.clockDrawLeft, this.checkDrawTop);
                if (f != 1.0f) {
                    canvas.save();
                    canvas.scale(f2, f2, Theme.dialogs_clockDrawable.getBounds().centerX(), Theme.dialogs_halfCheckDrawable.getBounds().centerY());
                    Theme.dialogs_clockDrawable.setAlpha((int) (255.0f * f));
                }
                Theme.dialogs_clockDrawable.draw(canvas);
                if (f != 1.0f) {
                    canvas.restore();
                    Theme.dialogs_clockDrawable.setAlpha(NotificationCenter.voipServiceCreated);
                }
                invalidate();
            } else if (z3) {
                if (z2) {
                    BaseCell.setDrawableBounds(Theme.dialogs_halfCheckDrawable, this.halfCheckDrawLeft, this.checkDrawTop);
                    if (z4) {
                        canvas.save();
                        canvas.scale(f2, f2, Theme.dialogs_halfCheckDrawable.getBounds().centerX(), Theme.dialogs_halfCheckDrawable.getBounds().centerY());
                        Theme.dialogs_halfCheckDrawable.setAlpha((int) (f * 255.0f));
                    }
                    if (!z4 && f != 0.0f) {
                        canvas.save();
                        canvas.scale(f2, f2, Theme.dialogs_halfCheckDrawable.getBounds().centerX(), Theme.dialogs_halfCheckDrawable.getBounds().centerY());
                        int i = (int) (255.0f * f);
                        Theme.dialogs_halfCheckDrawable.setAlpha(i);
                        Theme.dialogs_checkReadDrawable.setAlpha(i);
                    }
                    Theme.dialogs_halfCheckDrawable.draw(canvas);
                    if (z4) {
                        canvas.restore();
                        canvas.save();
                        canvas.translate(AndroidUtilities.dp(4.0f) * (1.0f - f), 0.0f);
                    }
                    BaseCell.setDrawableBounds(Theme.dialogs_checkReadDrawable, this.checkDrawLeft, this.checkDrawTop);
                    Theme.dialogs_checkReadDrawable.draw(canvas);
                    if (z4) {
                        canvas.restore();
                        Theme.dialogs_halfCheckDrawable.setAlpha(NotificationCenter.voipServiceCreated);
                    }
                    if (z4 || f == 0.0f) {
                        return;
                    }
                    canvas.restore();
                    Theme.dialogs_halfCheckDrawable.setAlpha(NotificationCenter.voipServiceCreated);
                    Theme.dialogs_checkReadDrawable.setAlpha(NotificationCenter.voipServiceCreated);
                    return;
                }
                BaseCell.setDrawableBounds(Theme.dialogs_checkDrawable, this.checkDrawLeft1, this.checkDrawTop);
                if (f != 1.0f) {
                    canvas.save();
                    canvas.scale(f2, f2, Theme.dialogs_checkDrawable.getBounds().centerX(), Theme.dialogs_halfCheckDrawable.getBounds().centerY());
                    Theme.dialogs_checkDrawable.setAlpha((int) (255.0f * f));
                }
                Theme.dialogs_checkDrawable.draw(canvas);
                if (f != 1.0f) {
                    canvas.restore();
                    Theme.dialogs_checkDrawable.setAlpha(NotificationCenter.voipServiceCreated);
                }
            }
        }
    }

    public boolean isPointInsideAvatar(float f, float f2) {
        return !LocaleController.isRTL ? f >= 0.0f && f < ((float) AndroidUtilities.dp(60.0f)) : f >= ((float) (getMeasuredWidth() - AndroidUtilities.dp(60.0f))) && f < ((float) getMeasuredWidth());
    }

    public void setDialogSelected(boolean z) {
        if (this.isSelected != z) {
            invalidate();
        }
        this.isSelected = z;
    }

    public void animateArchiveAvatar() {
        if (this.avatarDrawable.getAvatarType() != 2) {
            return;
        }
        this.animatingArchiveAvatar = true;
        this.animatingArchiveAvatarProgress = 0.0f;
        Theme.dialogs_archiveAvatarDrawable.setProgress(0.0f);
        Theme.dialogs_archiveAvatarDrawable.start();
        invalidate();
    }

    public void setChecked(boolean z, boolean z2) {
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 != null || z) {
            if (checkBox2 == null) {
                CheckBox2 checkBox22 = new CheckBox2(getContext(), 21, this.resourcesProvider) { // from class: org.telegram.ui.Cells.DialogCell.2
                    @Override // android.view.View
                    public void invalidate() {
                        super.invalidate();
                        DialogCell.this.invalidate();
                    }
                };
                this.checkBox = checkBox22;
                checkBox22.setColor(-1, Theme.key_windowBackgroundWhite, Theme.key_checkboxCheck);
                this.checkBox.setDrawUnchecked(false);
                this.checkBox.setDrawBackgroundAsArc(3);
                addView(this.checkBox);
            }
            this.checkBox.setChecked(z, z2);
            checkTtl();
        }
    }

    private MessageObject findFolderTopMessage() {
        ArrayList<TLRPC$Dialog> dialogsArray;
        DialogsActivity dialogsActivity = this.parentFragment;
        if (dialogsActivity == null || (dialogsArray = dialogsActivity.getDialogsArray(this.currentAccount, this.dialogsType, this.currentDialogFolderId, false)) == null || dialogsArray.isEmpty()) {
            return null;
        }
        int size = dialogsArray.size();
        MessageObject messageObject = null;
        for (int i = 0; i < size; i++) {
            TLRPC$Dialog tLRPC$Dialog = dialogsArray.get(i);
            LongSparseArray<ArrayList<MessageObject>> longSparseArray = MessagesController.getInstance(this.currentAccount).dialogMessage;
            if (longSparseArray != null) {
                ArrayList<MessageObject> arrayList = longSparseArray.get(tLRPC$Dialog.id);
                MessageObject messageObject2 = (arrayList == null || arrayList.isEmpty()) ? null : arrayList.get(0);
                if (messageObject2 != null && (messageObject == null || messageObject2.messageOwner.date > messageObject.messageOwner.date)) {
                    messageObject = messageObject2;
                }
                if (tLRPC$Dialog.pinnedNum == 0 && messageObject != null) {
                    break;
                }
            }
        }
        return messageObject;
    }

    public boolean isFolderCell() {
        return this.currentDialogFolderId != 0;
    }

    public boolean update(int i) {
        return update(i, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0333  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x036a  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0417  */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0429  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0430  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x0436  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0442  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0459  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x04b3  */
    /* JADX WARN: Removed duplicated region for block: B:299:0x055a  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x057e  */
    /* JADX WARN: Removed duplicated region for block: B:381:0x0804  */
    /* JADX WARN: Removed duplicated region for block: B:382:0x0806  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean update(int i, boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        long j;
        MessageObject messageObject;
        boolean z7;
        boolean z8;
        TLRPC$User tLRPC$User;
        boolean z9;
        boolean z10;
        TLRPC$Chat chat;
        boolean z11;
        boolean z12;
        MessageObject messageObject2;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        TLRPC$Chat chat2;
        MessageObject messageObject3;
        boolean isForumCell = isForumCell();
        boolean z13 = false;
        this.drawAvatarSelector = false;
        this.ttlPeriod = 0;
        CustomDialog customDialog = this.customDialog;
        if (customDialog != null) {
            this.lastMessageDate = customDialog.date;
            int i7 = customDialog.unread_count;
            this.lastUnreadState = i7 != 0;
            this.unreadCount = i7;
            this.drawPin = customDialog.pinned;
            this.dialogMuted = customDialog.muted;
            this.hasUnmutedTopics = false;
            this.avatarDrawable.setInfo(customDialog.id, customDialog.name, null);
            this.avatarImage.setImage(null, "50_50", this.avatarDrawable, null, 0L);
            int i8 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr = this.thumbImage;
                if (i8 >= imageReceiverArr.length) {
                    break;
                }
                imageReceiverArr[i8].setImageBitmap((Drawable) null);
                i8++;
            }
            this.avatarImage.setRoundRadius(AndroidUtilities.dp(28.0f));
            this.drawUnmute = false;
            z6 = isForumCell;
            z7 = false;
            z8 = false;
        } else {
            int i9 = this.unreadCount;
            boolean z14 = this.reactionMentionCount != 0;
            boolean z15 = this.markUnread;
            this.hasUnmutedTopics = false;
            this.readOutboxMaxId = -1;
            if (this.isDialogCell) {
                TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                if (tLRPC$Dialog != null) {
                    this.readOutboxMaxId = tLRPC$Dialog.read_outbox_max_id;
                    this.ttlPeriod = tLRPC$Dialog.ttl_period;
                    if (i == 0) {
                        this.clearingDialog = MessagesController.getInstance(this.currentAccount).isClearingDialog(tLRPC$Dialog.id);
                        ArrayList<MessageObject> arrayList = MessagesController.getInstance(this.currentAccount).dialogMessage.get(tLRPC$Dialog.id);
                        this.groupMessages = arrayList;
                        MessageObject messageObject4 = (arrayList == null || arrayList.size() <= 0) ? null : this.groupMessages.get(0);
                        this.message = messageObject4;
                        this.lastUnreadState = messageObject4 != null && messageObject4.isUnread();
                        TLRPC$Chat chat3 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$Dialog.id));
                        if (chat3 != null && chat3.forum) {
                            int[] forumUnreadCount = MessagesController.getInstance(this.currentAccount).getTopicsController().getForumUnreadCount(chat3.id);
                            this.unreadCount = forumUnreadCount[0];
                            this.mentionCount = forumUnreadCount[1];
                            this.reactionMentionCount = forumUnreadCount[2];
                            this.hasUnmutedTopics = forumUnreadCount[3] != 0;
                        } else if (tLRPC$Dialog instanceof TLRPC$TL_dialogFolder) {
                            this.unreadCount = MessagesStorage.getInstance(this.currentAccount).getArchiveUnreadCount();
                            this.mentionCount = 0;
                            this.reactionMentionCount = 0;
                        } else {
                            this.unreadCount = tLRPC$Dialog.unread_count;
                            this.mentionCount = tLRPC$Dialog.unread_mentions_count;
                            this.reactionMentionCount = tLRPC$Dialog.unread_reactions_count;
                        }
                        this.markUnread = tLRPC$Dialog.unread_mark;
                        MessageObject messageObject5 = this.message;
                        this.currentEditDate = messageObject5 != null ? messageObject5.messageOwner.edit_date : 0;
                        this.lastMessageDate = tLRPC$Dialog.last_message_date;
                        int i10 = this.dialogsType;
                        if (i10 == 7 || i10 == 8) {
                            MessagesController.DialogFilter dialogFilter = MessagesController.getInstance(this.currentAccount).selectedDialogFilter[this.dialogsType == 8 ? (char) 1 : (char) 0];
                            this.drawPin = dialogFilter != null && dialogFilter.pinnedDialogs.indexOfKey(tLRPC$Dialog.id) >= 0;
                        } else {
                            this.drawPin = this.currentDialogFolderId == 0 && tLRPC$Dialog.pinned;
                        }
                        MessageObject messageObject6 = this.message;
                        if (messageObject6 != null) {
                            this.lastSendState = messageObject6.messageOwner.send_state;
                        }
                    }
                } else {
                    this.unreadCount = 0;
                    this.mentionCount = 0;
                    this.reactionMentionCount = 0;
                    this.currentEditDate = 0;
                    this.lastMessageDate = 0;
                    this.clearingDialog = false;
                }
                long j2 = this.currentDialogId;
                this.drawAvatarSelector = j2 != 0 && j2 == RightSlidingDialogContainer.fragmentDialogId;
            } else {
                this.drawPin = false;
            }
            TLRPC$TL_forumTopic tLRPC$TL_forumTopic = this.forumTopic;
            if (tLRPC$TL_forumTopic != null) {
                this.unreadCount = tLRPC$TL_forumTopic.unread_count;
                this.mentionCount = tLRPC$TL_forumTopic.unread_mentions_count;
                this.reactionMentionCount = tLRPC$TL_forumTopic.unread_reactions_count;
            }
            if (this.dialogsType == 2) {
                this.drawPin = false;
            }
            DialogCellTags dialogCellTags = this.tags;
            if (dialogCellTags != null) {
                boolean isEmpty = dialogCellTags.isEmpty();
                z2 = isForumCell;
                if (this.tags.update(this.currentAccount, this.dialogsType, this.currentDialogId)) {
                    if (isEmpty != this.tags.isEmpty()) {
                        z3 = true;
                        z4 = true;
                    } else {
                        z3 = false;
                        z4 = false;
                    }
                    z5 = true;
                    if (i == 0) {
                        TLRPC$User tLRPC$User2 = this.user;
                        if (tLRPC$User2 != null && !MessagesController.isSupportUser(tLRPC$User2) && !this.user.bot && (i & MessagesController.UPDATE_MASK_STATUS) != 0) {
                            this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                            if (this.wasDrawnOnline != isOnline()) {
                                z5 = true;
                            }
                        }
                        if ((i & MessagesController.UPDATE_MASK_EMOJI_STATUS) != 0) {
                            if (this.user != null) {
                                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                                this.user = user;
                                if (user != null && DialogObject.getEmojiStatusDocumentId(user.emoji_status) != 0) {
                                    this.nameLayoutEllipsizeByGradient = true;
                                    this.emojiStatus.set(DialogObject.getEmojiStatusDocumentId(this.user.emoji_status), z);
                                } else {
                                    this.nameLayoutEllipsizeByGradient = true;
                                    this.emojiStatus.set(PremiumGradient.getInstance().premiumStarDrawableMini, z);
                                }
                                z5 = true;
                            }
                            if (this.chat != null) {
                                TLRPC$Chat chat4 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                                this.chat = chat4;
                                if (chat4 != null && DialogObject.getEmojiStatusDocumentId(chat4.emoji_status) != 0) {
                                    this.nameLayoutEllipsizeByGradient = true;
                                    this.emojiStatus.set(DialogObject.getEmojiStatusDocumentId(this.chat.emoji_status), z);
                                } else {
                                    this.nameLayoutEllipsizeByGradient = true;
                                    this.emojiStatus.set(PremiumGradient.getInstance().premiumStarDrawableMini, z);
                                }
                                z5 = true;
                            }
                        }
                        if ((this.isDialogCell || this.isTopic) && (i & MessagesController.UPDATE_MASK_USER_PRINT) != 0) {
                            CharSequence printingString = MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, getTopicId(), true);
                            CharSequence charSequence = this.lastPrintString;
                            if ((charSequence != null && printingString == null) || ((charSequence == null && printingString != null) || (charSequence != null && !charSequence.equals(printingString)))) {
                                z11 = true;
                                if (!z11 && (i & MessagesController.UPDATE_MASK_MESSAGE_TEXT) != 0 && (messageObject3 = this.message) != null && messageObject3.messageText != this.lastMessageString) {
                                    z11 = true;
                                }
                                if (!z11 && (i & MessagesController.UPDATE_MASK_CHAT) != 0 && this.chat != null) {
                                    chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                                    if ((chat2 == null && chat2.call_active && chat2.call_not_empty) != this.hasCall) {
                                        z11 = true;
                                    }
                                }
                                if (!z11 && (i & MessagesController.UPDATE_MASK_AVATAR) != 0 && this.chat == null) {
                                    z11 = true;
                                }
                                if (!z11 && (i & MessagesController.UPDATE_MASK_NAME) != 0 && this.chat == null) {
                                    z11 = true;
                                }
                                if (!z11 && (i & MessagesController.UPDATE_MASK_CHAT_AVATAR) != 0 && this.user == null) {
                                    z11 = true;
                                }
                                if (!z11 && (i & MessagesController.UPDATE_MASK_CHAT_NAME) != 0 && this.user == null) {
                                    z11 = true;
                                }
                                if (!z11) {
                                    MessageObject messageObject7 = this.message;
                                    if (messageObject7 != null && this.lastUnreadState != messageObject7.isUnread()) {
                                        this.lastUnreadState = this.message.isUnread();
                                        z11 = true;
                                    }
                                    if (this.isDialogCell) {
                                        TLRPC$Dialog tLRPC$Dialog2 = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                                        TLRPC$Chat chat5 = tLRPC$Dialog2 == null ? null : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$Dialog2.id));
                                        if (chat5 != null && chat5.forum) {
                                            z6 = z2;
                                            int[] forumUnreadCount2 = MessagesController.getInstance(this.currentAccount).getTopicsController().getForumUnreadCount(chat5.id);
                                            i5 = forumUnreadCount2[0];
                                            i6 = forumUnreadCount2[1];
                                            int i11 = forumUnreadCount2[2];
                                            this.hasUnmutedTopics = forumUnreadCount2[3] != 0;
                                            i4 = i11;
                                        } else {
                                            z6 = z2;
                                            if (tLRPC$Dialog2 instanceof TLRPC$TL_dialogFolder) {
                                                i5 = MessagesStorage.getInstance(this.currentAccount).getArchiveUnreadCount();
                                                i4 = 0;
                                            } else if (tLRPC$Dialog2 != null) {
                                                i5 = tLRPC$Dialog2.unread_count;
                                                i6 = tLRPC$Dialog2.unread_mentions_count;
                                                i4 = tLRPC$Dialog2.unread_reactions_count;
                                            } else {
                                                i4 = 0;
                                                i5 = 0;
                                            }
                                            i6 = 0;
                                        }
                                        if (tLRPC$Dialog2 != null && (this.unreadCount != i5 || this.markUnread != tLRPC$Dialog2.unread_mark || this.mentionCount != i6 || this.reactionMentionCount != i4)) {
                                            this.unreadCount = i5;
                                            this.mentionCount = i6;
                                            this.markUnread = tLRPC$Dialog2.unread_mark;
                                            this.reactionMentionCount = i4;
                                            z11 = true;
                                        }
                                        if (!z11 && (i & MessagesController.UPDATE_MASK_SEND_STATE) != 0 && (messageObject2 = this.message) != null) {
                                            i2 = this.lastSendState;
                                            i3 = messageObject2.messageOwner.send_state;
                                            if (i2 != i3) {
                                                this.lastSendState = i3;
                                                z12 = true;
                                                if (!z12) {
                                                    invalidate();
                                                    return z3;
                                                }
                                            }
                                        }
                                        z12 = z11;
                                        if (!z12) {
                                        }
                                    }
                                }
                                z6 = z2;
                                if (!z11) {
                                    i2 = this.lastSendState;
                                    i3 = messageObject2.messageOwner.send_state;
                                    if (i2 != i3) {
                                    }
                                }
                                z12 = z11;
                                if (!z12) {
                                }
                            }
                        }
                        z11 = false;
                        if (!z11) {
                            z11 = true;
                        }
                        if (!z11) {
                            chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                            if ((chat2 == null && chat2.call_active && chat2.call_not_empty) != this.hasCall) {
                            }
                        }
                        if (!z11) {
                            z11 = true;
                        }
                        if (!z11) {
                            z11 = true;
                        }
                        if (!z11) {
                            z11 = true;
                        }
                        if (!z11) {
                            z11 = true;
                        }
                        if (!z11) {
                        }
                        z6 = z2;
                        if (!z11) {
                        }
                        z12 = z11;
                        if (!z12) {
                        }
                    } else {
                        z6 = z2;
                    }
                    this.user = null;
                    this.chat = null;
                    this.encryptedChat = null;
                    if (this.currentDialogFolderId == 0) {
                        this.dialogMuted = false;
                        this.drawUnmute = false;
                        MessageObject findFolderTopMessage = findFolderTopMessage();
                        this.message = findFolderTopMessage;
                        j = findFolderTopMessage != null ? findFolderTopMessage.getDialogId() : 0L;
                    } else {
                        this.drawUnmute = false;
                        if (this.forumTopic != null) {
                            boolean isDialogMuted = MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId, 0L);
                            boolean isDialogMuted2 = MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId, this.forumTopic.id);
                            this.topicMuted = isDialogMuted2;
                            if (isDialogMuted == isDialogMuted2) {
                                this.dialogMuted = false;
                                this.drawUnmute = false;
                            } else {
                                this.dialogMuted = isDialogMuted2;
                                this.drawUnmute = !isDialogMuted2;
                            }
                        } else {
                            this.dialogMuted = this.isDialogCell && MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId, (long) getTopicId());
                        }
                        j = this.currentDialogId;
                    }
                    if (j != 0) {
                        if (DialogObject.isEncryptedDialog(j)) {
                            TLRPC$EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(DialogObject.getEncryptedChatId(j)));
                            this.encryptedChat = encryptedChat;
                            if (encryptedChat != null) {
                                this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.encryptedChat.user_id));
                            }
                        } else if (DialogObject.isUserDialog(j)) {
                            this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(j));
                        } else {
                            TLRPC$Chat chat6 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
                            this.chat = chat6;
                            if (!this.isDialogCell && chat6 != null && chat6.migrated_to != null && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.migrated_to.channel_id))) != null) {
                                this.chat = chat;
                            }
                        }
                        if (this.useMeForMyMessages && this.user != null && this.message.isOutOwner()) {
                            this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(UserConfig.getInstance(this.currentAccount).clientUserId));
                        }
                    }
                    if (this.currentDialogFolderId == 0) {
                        Theme.dialogs_archiveAvatarDrawable.setCallback(this);
                        this.avatarDrawable.setAvatarType(2);
                        this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
                    } else if (this.useFromUserAsAvatar && (messageObject = this.message) != null) {
                        this.avatarDrawable.setInfo(this.currentAccount, messageObject.getFromPeerObject());
                        this.avatarImage.setForUserOrChat(this.message.getFromPeerObject(), this.avatarDrawable);
                    } else {
                        TLRPC$User tLRPC$User3 = this.user;
                        if (tLRPC$User3 != null) {
                            this.avatarDrawable.setInfo(this.currentAccount, tLRPC$User3);
                            if (UserObject.isReplyUser(this.user)) {
                                this.avatarDrawable.setAvatarType(12);
                                this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
                            } else if (UserObject.isAnonymous(this.user)) {
                                this.avatarDrawable.setAvatarType(21);
                                this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
                            } else if (UserObject.isUserSelf(this.user) && this.isSavedDialog) {
                                this.avatarDrawable.setAvatarType(22);
                                this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
                            } else if (UserObject.isUserSelf(this.user) && !this.useMeForMyMessages) {
                                this.avatarDrawable.setAvatarType(1);
                                this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
                            } else {
                                this.avatarImage.setForUserOrChat(this.user, this.avatarDrawable, null, true, 1, false);
                            }
                        } else {
                            TLRPC$Chat tLRPC$Chat = this.chat;
                            if (tLRPC$Chat != null) {
                                this.avatarDrawable.setInfo(this.currentAccount, tLRPC$Chat);
                                this.avatarImage.setForUserOrChat(this.chat, this.avatarDrawable);
                            }
                        }
                    }
                    if (z || ((i9 == this.unreadCount && z15 == this.markUnread) || (this.isDialogCell && System.currentTimeMillis() - this.lastDialogChangedTime <= 100))) {
                        z7 = z3;
                        z8 = z4;
                    } else {
                        ValueAnimator valueAnimator = this.countAnimator;
                        if (valueAnimator != null) {
                            valueAnimator.cancel();
                        }
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                        this.countAnimator = ofFloat;
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda0
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                                DialogCell.this.lambda$update$0(valueAnimator2);
                            }
                        });
                        this.countAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.3
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                DialogCell.this.countChangeProgress = 1.0f;
                                DialogCell.this.countOldLayout = null;
                                DialogCell.this.countAnimationStableLayout = null;
                                DialogCell.this.countAnimationInLayout = null;
                                DialogCell.this.invalidate();
                            }
                        });
                        if ((i9 == 0 || this.markUnread) && (this.markUnread || !z15)) {
                            this.countAnimator.setDuration(220L);
                            this.countAnimator.setInterpolator(new OvershootInterpolator());
                        } else if (this.unreadCount == 0) {
                            this.countAnimator.setDuration(150L);
                            this.countAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                        } else {
                            this.countAnimator.setDuration(430L);
                            this.countAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                        }
                        if (this.drawCount && this.drawCount2 && this.countLayout != null) {
                            String format = String.format("%d", Integer.valueOf(i9));
                            String format2 = String.format("%d", Integer.valueOf(this.unreadCount));
                            if (format.length() == format2.length()) {
                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(format);
                                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(format2);
                                SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(format2);
                                int i12 = 0;
                                while (i12 < format.length()) {
                                    if (format.charAt(i12) == format2.charAt(i12)) {
                                        z9 = z3;
                                        int i13 = i12 + 1;
                                        spannableStringBuilder.setSpan(new EmptyStubSpan(), i12, i13, 0);
                                        spannableStringBuilder2.setSpan(new EmptyStubSpan(), i12, i13, 0);
                                        z10 = z4;
                                    } else {
                                        z9 = z3;
                                        z10 = z4;
                                        spannableStringBuilder3.setSpan(new EmptyStubSpan(), i12, i12 + 1, 0);
                                    }
                                    i12++;
                                    z3 = z9;
                                    z4 = z10;
                                }
                                z7 = z3;
                                z8 = z4;
                                int max = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(format)));
                                TextPaint textPaint = Theme.dialogs_countTextPaint;
                                Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
                                this.countOldLayout = new StaticLayout(spannableStringBuilder, textPaint, max, alignment, 1.0f, 0.0f, false);
                                this.countAnimationStableLayout = new StaticLayout(spannableStringBuilder3, Theme.dialogs_countTextPaint, max, alignment, 1.0f, 0.0f, false);
                                this.countAnimationInLayout = new StaticLayout(spannableStringBuilder2, Theme.dialogs_countTextPaint, max, alignment, 1.0f, 0.0f, false);
                            } else {
                                z7 = z3;
                                z8 = z4;
                                this.countOldLayout = this.countLayout;
                            }
                        } else {
                            z7 = z3;
                            z8 = z4;
                        }
                        this.countWidthOld = this.countWidth;
                        this.countLeftOld = this.countLeft;
                        this.countAnimationIncrement = this.unreadCount > i9;
                        this.countAnimator.start();
                    }
                    boolean z16 = this.reactionMentionCount == 0;
                    if (!z && z16 != z14) {
                        ValueAnimator valueAnimator2 = this.reactionsMentionsAnimator;
                        if (valueAnimator2 != null) {
                            valueAnimator2.cancel();
                        }
                        this.reactionsMentionsChangeProgress = 0.0f;
                        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                        this.reactionsMentionsAnimator = ofFloat2;
                        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda1
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                                DialogCell.this.lambda$update$1(valueAnimator3);
                            }
                        });
                        this.reactionsMentionsAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.4
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                DialogCell.this.reactionsMentionsChangeProgress = 1.0f;
                                DialogCell.this.invalidate();
                            }
                        });
                        if (z16) {
                            this.reactionsMentionsAnimator.setDuration(220L);
                            this.reactionsMentionsAnimator.setInterpolator(new OvershootInterpolator());
                        } else {
                            this.reactionsMentionsAnimator.setDuration(150L);
                            this.reactionsMentionsAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
                        }
                        this.reactionsMentionsAnimator.start();
                    }
                    ImageReceiver imageReceiver = this.avatarImage;
                    TLRPC$Chat tLRPC$Chat2 = this.chat;
                    imageReceiver.setRoundRadius(AndroidUtilities.dp((!(tLRPC$Chat2 == null && tLRPC$Chat2.forum && this.currentDialogFolderId == 0 && !this.useFromUserAsAvatar) && (this.isSavedDialog || (tLRPC$User = this.user) == null || !tLRPC$User.self || !MessagesController.getInstance(this.currentAccount).savedViewAsChats)) ? 28.0f : 16.0f));
                    z13 = z5;
                }
            } else {
                z2 = isForumCell;
            }
            z3 = false;
            z4 = false;
            z5 = false;
            if (i == 0) {
            }
            this.user = null;
            this.chat = null;
            this.encryptedChat = null;
            if (this.currentDialogFolderId == 0) {
            }
            if (j != 0) {
            }
            if (this.currentDialogFolderId == 0) {
            }
            if (z) {
            }
            z7 = z3;
            z8 = z4;
            if (this.reactionMentionCount == 0) {
            }
            if (!z) {
            }
            ImageReceiver imageReceiver2 = this.avatarImage;
            TLRPC$Chat tLRPC$Chat22 = this.chat;
            imageReceiver2.setRoundRadius(AndroidUtilities.dp((!(tLRPC$Chat22 == null && tLRPC$Chat22.forum && this.currentDialogFolderId == 0 && !this.useFromUserAsAvatar) && (this.isSavedDialog || (tLRPC$User = this.user) == null || !tLRPC$User.self || !MessagesController.getInstance(this.currentAccount).savedViewAsChats)) ? 28.0f : 16.0f));
            z13 = z5;
        }
        if (!this.isTopic && (getMeasuredWidth() != 0 || getMeasuredHeight() != 0)) {
            z8 = true;
        }
        if (!z13) {
            int i14 = this.storyParams.currentState;
            StoriesUtilities.getPredictiveUnreadState(MessagesController.getInstance(this.currentAccount).getStoriesController(), getDialogId());
        }
        if (!z) {
            this.dialogMutedProgress = (this.dialogMuted || this.drawUnmute) ? 1.0f : 0.0f;
            ValueAnimator valueAnimator3 = this.countAnimator;
            if (valueAnimator3 != null) {
                valueAnimator3.cancel();
            }
        }
        invalidate();
        if (isForumCell() != z6) {
            z7 = true;
        }
        if (z8) {
            if (this.attachedToWindow) {
                buildLayout();
            } else {
                this.updateLayout = true;
            }
        }
        updatePremiumBlocked(z);
        return z7;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$0(ValueAnimator valueAnimator) {
        this.countChangeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$1(ValueAnimator valueAnimator) {
        this.reactionsMentionsChangeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTopicId() {
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic = this.forumTopic;
        if (tLRPC$TL_forumTopic == null) {
            return 0;
        }
        return tLRPC$TL_forumTopic.id;
    }

    @Override // android.view.View
    public float getTranslationX() {
        return this.translationX;
    }

    @Override // android.view.View
    public void setTranslationX(float f) {
        if (f == this.translationX) {
            return;
        }
        this.translationX = f;
        RLottieDrawable rLottieDrawable = this.translationDrawable;
        if (rLottieDrawable != null && f == 0.0f) {
            rLottieDrawable.setProgress(0.0f);
            this.translationAnimationStarted = false;
            this.archiveHidden = SharedConfig.archiveHidden;
            this.currentRevealProgress = 0.0f;
            this.isSliding = false;
        }
        float f2 = this.translationX;
        if (f2 != 0.0f) {
            this.isSliding = true;
        } else {
            this.currentRevealBounceProgress = 0.0f;
            this.currentRevealProgress = 0.0f;
            this.drawRevealBackground = false;
        }
        if (this.isSliding && !this.swipeCanceled) {
            boolean z = this.drawRevealBackground;
            boolean z2 = Math.abs(f2) >= ((float) getMeasuredWidth()) * 0.45f;
            this.drawRevealBackground = z2;
            if (z != z2 && this.archiveHidden == SharedConfig.archiveHidden) {
                try {
                    performHapticFeedback(3, 2);
                } catch (Exception unused) {
                }
            }
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:258:0x088a, code lost:
        if (r1.type != 2) goto L825;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x0c47, code lost:
        if (r3.lastKnownTypingType >= 0) goto L204;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:308:0x0a64  */
    /* JADX WARN: Removed duplicated region for block: B:353:0x0bbf  */
    /* JADX WARN: Removed duplicated region for block: B:354:0x0bca  */
    /* JADX WARN: Removed duplicated region for block: B:365:0x0bee  */
    /* JADX WARN: Removed duplicated region for block: B:366:0x0bf1  */
    /* JADX WARN: Removed duplicated region for block: B:370:0x0c03  */
    /* JADX WARN: Removed duplicated region for block: B:375:0x0c39  */
    /* JADX WARN: Removed duplicated region for block: B:413:0x0ce5  */
    /* JADX WARN: Removed duplicated region for block: B:416:0x0ceb  */
    /* JADX WARN: Removed duplicated region for block: B:478:0x0ef6  */
    /* JADX WARN: Removed duplicated region for block: B:481:0x0efc  */
    /* JADX WARN: Removed duplicated region for block: B:541:0x0fbc  */
    /* JADX WARN: Removed duplicated region for block: B:544:0x0fc5  */
    /* JADX WARN: Removed duplicated region for block: B:551:0x0fd1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:584:0x102c  */
    /* JADX WARN: Removed duplicated region for block: B:596:0x104e  */
    /* JADX WARN: Removed duplicated region for block: B:600:0x1066  */
    /* JADX WARN: Removed duplicated region for block: B:605:0x10ba  */
    /* JADX WARN: Removed duplicated region for block: B:611:0x10d0  */
    /* JADX WARN: Removed duplicated region for block: B:627:0x1120  */
    /* JADX WARN: Removed duplicated region for block: B:683:0x11fb  */
    /* JADX WARN: Removed duplicated region for block: B:688:0x1221  */
    /* JADX WARN: Removed duplicated region for block: B:690:0x126c  */
    /* JADX WARN: Removed duplicated region for block: B:723:0x12e2  */
    /* JADX WARN: Removed duplicated region for block: B:739:0x13bb  */
    /* JADX WARN: Removed duplicated region for block: B:747:0x1410  */
    /* JADX WARN: Removed duplicated region for block: B:785:0x15a5  */
    /* JADX WARN: Removed duplicated region for block: B:790:0x15d5  */
    /* JADX WARN: Removed duplicated region for block: B:795:0x15e6  */
    /* JADX WARN: Removed duplicated region for block: B:796:0x1605  */
    /* JADX WARN: Removed duplicated region for block: B:799:0x160b  */
    /* JADX WARN: Removed duplicated region for block: B:811:0x1627  */
    /* JADX WARN: Removed duplicated region for block: B:812:0x1629  */
    /* JADX WARN: Removed duplicated region for block: B:816:0x1637  */
    /* JADX WARN: Removed duplicated region for block: B:819:0x1642  */
    /* JADX WARN: Removed duplicated region for block: B:825:0x1651  */
    /* JADX WARN: Removed duplicated region for block: B:829:0x1659  */
    /* JADX WARN: Removed duplicated region for block: B:831:0x165d  */
    /* JADX WARN: Removed duplicated region for block: B:845:0x16c6  */
    /* JADX WARN: Removed duplicated region for block: B:848:0x16cf  */
    /* JADX WARN: Removed duplicated region for block: B:851:0x16d6  */
    /* JADX WARN: Removed duplicated region for block: B:866:0x1718  */
    /* JADX WARN: Removed duplicated region for block: B:895:0x179c  */
    /* JADX WARN: Removed duplicated region for block: B:901:0x17ec  */
    /* JADX WARN: Removed duplicated region for block: B:904:0x17f4  */
    /* JADX WARN: Removed duplicated region for block: B:909:0x1805  */
    /* JADX WARN: Removed duplicated region for block: B:918:0x181c  */
    /* JADX WARN: Removed duplicated region for block: B:926:0x1843  */
    /* JADX WARN: Removed duplicated region for block: B:937:0x186e  */
    /* JADX WARN: Removed duplicated region for block: B:943:0x1881  */
    /* JADX WARN: Removed duplicated region for block: B:953:0x18a3  */
    /* JADX WARN: Removed duplicated region for block: B:963:0x18bf  */
    /* JADX WARN: Removed duplicated region for block: B:984:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v2, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r10v58 */
    @Override // android.view.View
    @SuppressLint({"DrawAllocation"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onDraw(Canvas canvas) {
        int color;
        int color2;
        int i;
        String string;
        int i2;
        float f;
        String str;
        int i3;
        boolean z;
        boolean z2;
        RLottieDrawable rLottieDrawable;
        int i4;
        float f2;
        int i5;
        Paint paint;
        Canvas canvas2;
        ?? r10;
        int i6;
        float f3;
        float f4;
        boolean z3;
        float f5;
        boolean z4;
        boolean z5;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic;
        boolean z6;
        boolean z7;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic2;
        PullForegroundDrawable pullForegroundDrawable;
        float f6;
        int i7;
        Paint paint2;
        Paint paint3;
        boolean z8;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        float f7;
        int i13;
        int i14;
        boolean z9;
        boolean z10;
        int dp;
        float f8;
        int dp2;
        int i15;
        boolean z11;
        DialogCellTags dialogCellTags;
        int i16;
        char c;
        float dp3;
        float f9;
        int i17;
        StaticLayout staticLayout;
        int i18;
        float f10;
        float f11;
        DialogUpdateHelper dialogUpdateHelper;
        float f12;
        StaticLayout staticLayout2;
        int color3;
        float f13;
        StaticLayout staticLayout3;
        StaticLayout staticLayout4;
        int i19;
        Paint paint4;
        PullForegroundDrawable pullForegroundDrawable2;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic3;
        float f14 = 12.5f;
        float f15 = 12.0f;
        if (this.currentDialogId == 0 && this.customDialog == null) {
            return;
        }
        if (!this.visibleOnScreen && !this.drawingForBlur) {
            return;
        }
        if (this.drawArchive && ((this.currentDialogFolderId != 0 || (this.isTopic && (tLRPC$TL_forumTopic3 = this.forumTopic) != null && tLRPC$TL_forumTopic3.id == 1)) && (pullForegroundDrawable2 = this.archivedChatsDrawable) != null && pullForegroundDrawable2.outProgress == 0.0f && this.translationX == 0.0f)) {
            if (this.drawingForBlur) {
                return;
            }
            canvas.save();
            canvas.translate(0.0f, (-this.translateY) - this.rightFragmentOffset);
            canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.archivedChatsDrawable.draw(canvas);
            canvas.restore();
            return;
        }
        if (this.clipProgress != 0.0f && Build.VERSION.SDK_INT != 24) {
            canvas.save();
            canvas.clipRect(0.0f, this.topClip * this.clipProgress, getMeasuredWidth(), getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)));
        }
        if (this.translationX != 0.0f || this.cornerProgress != 0.0f) {
            canvas.save();
            canvas.translate(0.0f, -this.translateY);
            if (this.overrideSwipeAction) {
                color = Theme.getColor(this.overrideSwipeActionBackgroundColorKey, this.resourcesProvider);
                color2 = Theme.getColor(this.overrideSwipeActionRevealBackgroundColorKey, this.resourcesProvider);
                String str2 = this.overrideSwipeActionStringKey;
                i = this.overrideSwipeActionStringId;
                string = LocaleController.getString(str2, i);
                this.translationDrawable = this.overrideSwipeActionDrawable;
            } else if (this.currentDialogFolderId != 0) {
                if (this.archiveHidden) {
                    color = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                    color2 = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                    i = R.string.UnhideFromTop;
                    string = LocaleController.getString("UnhideFromTop", i);
                    this.translationDrawable = Theme.dialogs_unpinArchiveDrawable;
                } else {
                    color = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                    color2 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                    i = R.string.HideOnTop;
                    string = LocaleController.getString("HideOnTop", i);
                    this.translationDrawable = Theme.dialogs_pinArchiveDrawable;
                }
            } else if (this.promoDialog) {
                color = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                color2 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                i = R.string.PsaHide;
                string = LocaleController.getString("PsaHide", i);
                this.translationDrawable = Theme.dialogs_hidePsaDrawable;
            } else if (this.folderId == 0) {
                color = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                color2 = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                if (SharedConfig.getChatSwipeAction(this.currentAccount) == 3) {
                    if (this.dialogMuted) {
                        i = R.string.SwipeUnmute;
                        string = LocaleController.getString("SwipeUnmute", i);
                        this.translationDrawable = Theme.dialogs_swipeUnmuteDrawable;
                    } else {
                        i = R.string.SwipeMute;
                        string = LocaleController.getString("SwipeMute", i);
                        this.translationDrawable = Theme.dialogs_swipeMuteDrawable;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 4) {
                    i = R.string.SwipeDeleteChat;
                    string = LocaleController.getString("SwipeDeleteChat", i);
                    color = Theme.getColor(Theme.key_dialogSwipeRemove, this.resourcesProvider);
                    this.translationDrawable = Theme.dialogs_swipeDeleteDrawable;
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 1) {
                    if (this.unreadCount > 0 || this.markUnread) {
                        i = R.string.SwipeMarkAsRead;
                        string = LocaleController.getString("SwipeMarkAsRead", i);
                        this.translationDrawable = Theme.dialogs_swipeReadDrawable;
                    } else {
                        i = R.string.SwipeMarkAsUnread;
                        string = LocaleController.getString("SwipeMarkAsUnread", i);
                        this.translationDrawable = Theme.dialogs_swipeUnreadDrawable;
                    }
                } else if (SharedConfig.getChatSwipeAction(this.currentAccount) == 0) {
                    if (getIsPinned()) {
                        i = R.string.SwipeUnpin;
                        string = LocaleController.getString("SwipeUnpin", i);
                        this.translationDrawable = Theme.dialogs_swipeUnpinDrawable;
                    } else {
                        i = R.string.SwipePin;
                        string = LocaleController.getString("SwipePin", i);
                        this.translationDrawable = Theme.dialogs_swipePinDrawable;
                    }
                } else {
                    i = R.string.Archive;
                    string = LocaleController.getString("Archive", i);
                    this.translationDrawable = Theme.dialogs_archiveDrawable;
                }
            } else {
                color = Theme.getColor(Theme.key_chats_archivePinBackground, this.resourcesProvider);
                color2 = Theme.getColor(Theme.key_chats_archiveBackground, this.resourcesProvider);
                i = R.string.Unarchive;
                string = LocaleController.getString("Unarchive", i);
                this.translationDrawable = Theme.dialogs_unarchiveDrawable;
            }
            int i20 = color2;
            int i21 = i;
            String str3 = string;
            if (this.swipeCanceled && (rLottieDrawable = this.lastDrawTranslationDrawable) != null) {
                this.translationDrawable = rLottieDrawable;
                i21 = this.lastDrawSwipeMessageStringId;
            } else {
                this.lastDrawTranslationDrawable = this.translationDrawable;
                this.lastDrawSwipeMessageStringId = i21;
            }
            int i22 = i21;
            if (!this.translationAnimationStarted && Math.abs(this.translationX) > AndroidUtilities.dp(43.0f)) {
                this.translationAnimationStarted = true;
                this.translationDrawable.setProgress(0.0f);
                this.translationDrawable.setCallback(this);
                this.translationDrawable.start();
            }
            float measuredWidth = this.translationX + getMeasuredWidth();
            if (this.currentRevealProgress < 1.0f) {
                Theme.dialogs_pinnedPaint.setColor(color);
                f = 0.0f;
                i2 = i22;
                canvas.drawRect(measuredWidth - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
                if (this.currentRevealProgress == 0.0f) {
                    if (Theme.dialogs_archiveDrawableRecolored) {
                        Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(Theme.key_chats_archiveBackground));
                        z2 = false;
                        Theme.dialogs_archiveDrawableRecolored = false;
                    } else {
                        z2 = false;
                    }
                    if (Theme.dialogs_hidePsaDrawableRecolored) {
                        Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                        RLottieDrawable rLottieDrawable2 = Theme.dialogs_hidePsaDrawable;
                        int i23 = Theme.key_chats_archiveBackground;
                        rLottieDrawable2.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(i23));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(i23));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(i23));
                        Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                        Theme.dialogs_hidePsaDrawableRecolored = z2;
                    }
                }
            } else {
                i2 = i22;
                f = 0.0f;
            }
            int measuredWidth2 = (getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.translationDrawable.getIntrinsicWidth() / 2);
            int measuredHeight = (getMeasuredHeight() - AndroidUtilities.dp(54.0f)) / 2;
            int intrinsicWidth = (this.translationDrawable.getIntrinsicWidth() / 2) + measuredWidth2;
            int intrinsicHeight = (this.translationDrawable.getIntrinsicHeight() / 2) + measuredHeight;
            if (this.currentRevealProgress > f) {
                canvas.save();
                str = str3;
                canvas.clipRect(measuredWidth - AndroidUtilities.dp(8.0f), f, getMeasuredWidth(), getMeasuredHeight());
                Theme.dialogs_pinnedPaint.setColor(i20);
                canvas.drawCircle(intrinsicWidth, intrinsicHeight, ((float) Math.sqrt((intrinsicWidth * intrinsicWidth) + ((intrinsicHeight - getMeasuredHeight()) * (intrinsicHeight - getMeasuredHeight())))) * AndroidUtilities.accelerateInterpolator.getInterpolation(this.currentRevealProgress), Theme.dialogs_pinnedPaint);
                canvas.restore();
                if (Theme.dialogs_archiveDrawableRecolored) {
                    z = 1;
                } else {
                    Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(Theme.key_chats_archivePinBackground));
                    z = 1;
                    Theme.dialogs_archiveDrawableRecolored = true;
                }
                i3 = z;
                if (!Theme.dialogs_hidePsaDrawableRecolored) {
                    Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                    RLottieDrawable rLottieDrawable3 = Theme.dialogs_hidePsaDrawable;
                    int i24 = Theme.key_chats_archivePinBackground;
                    rLottieDrawable3.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(i24));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(i24));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(i24));
                    Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                    Theme.dialogs_hidePsaDrawableRecolored = z;
                    i3 = z;
                }
            } else {
                str = str3;
                i3 = 1;
            }
            canvas.save();
            canvas.translate(measuredWidth2, measuredHeight);
            float f16 = this.currentRevealBounceProgress;
            if (f16 != 0.0f && f16 != 1.0f) {
                float interpolation = this.interpolator.getInterpolation(f16) + 1.0f;
                canvas.scale(interpolation, interpolation, this.translationDrawable.getIntrinsicWidth() / 2, this.translationDrawable.getIntrinsicHeight() / 2);
            }
            BaseCell.setDrawableBounds((Drawable) this.translationDrawable, 0, 0);
            this.translationDrawable.draw(canvas);
            canvas.restore();
            canvas.clipRect(measuredWidth, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            String str4 = str;
            int ceil = (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str4));
            int i25 = i2;
            if (this.swipeMessageTextId != i25 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = i25;
                this.swipeMessageWidth = getMeasuredWidth();
                TextPaint textPaint = Theme.dialogs_archiveTextPaint;
                int min = Math.min(AndroidUtilities.dp(80.0f), ceil);
                Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
                StaticLayout staticLayout5 = new StaticLayout(str4, textPaint, min, alignment, 1.0f, 0.0f, false);
                this.swipeMessageTextLayout = staticLayout5;
                if (staticLayout5.getLineCount() > i3) {
                    this.swipeMessageTextLayout = new StaticLayout(str4, Theme.dialogs_archiveTextPaintSmall, Math.min(AndroidUtilities.dp(82.0f), ceil), alignment, 1.0f, 0.0f, false);
                }
            }
            if (this.swipeMessageTextLayout != null) {
                canvas.save();
                canvas.translate((getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.swipeMessageTextLayout.getWidth() / 2.0f), measuredHeight + AndroidUtilities.dp(38.0f) + (this.swipeMessageTextLayout.getLineCount() > i3 ? -AndroidUtilities.dp(4.0f) : 0.0f));
                this.swipeMessageTextLayout.draw(canvas);
                canvas.restore();
            }
            canvas.restore();
            i4 = i3;
        } else {
            RLottieDrawable rLottieDrawable4 = this.translationDrawable;
            if (rLottieDrawable4 != null) {
                rLottieDrawable4.stop();
                this.translationDrawable.setProgress(0.0f);
                this.translationDrawable.setCallback(null);
                this.translationDrawable = null;
                this.translationAnimationStarted = false;
            }
            i4 = 1;
        }
        if (this.translationX != 0.0f) {
            canvas.save();
            canvas.translate(this.translationX, 0.0f);
        }
        float dp4 = AndroidUtilities.dp(8.0f) * this.cornerProgress;
        if (this.isSelected) {
            f2 = 0.0f;
            this.rect.set(0.0f, 0.0f, getMeasuredWidth(), AndroidUtilities.lerp(getMeasuredHeight(), getCollapsedHeight(), this.rightFragmentOpenedProgress));
            this.rect.offset(0.0f, (-this.translateY) + this.collapseOffset);
            canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_tabletSeletedPaint);
        } else {
            f2 = 0.0f;
        }
        canvas.save();
        canvas.translate(f2, (-this.rightFragmentOffset) * this.rightFragmentOpenedProgress);
        if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != f2)) {
            Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider), this.archiveBackgroundProgress, 1.0f));
            Theme.dialogs_pinnedPaint.setAlpha((int) (paint4.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
            i5 = 0;
            canvas.drawRect(-this.xOffset, 0.0f, getMeasuredWidth(), getMeasuredHeight() - this.translateY, Theme.dialogs_pinnedPaint);
        } else {
            i5 = 0;
            if (getIsPinned() || this.drawPinBackground) {
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider));
                Theme.dialogs_pinnedPaint.setAlpha((int) (paint.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
                canvas.drawRect(-this.xOffset, 0.0f, getMeasuredWidth(), getMeasuredHeight() - this.translateY, Theme.dialogs_pinnedPaint);
            }
        }
        canvas.restore();
        this.updateHelper.updateAnimationValues();
        if (this.collapseOffset != 0.0f) {
            canvas.save();
            canvas.translate(0.0f, this.collapseOffset);
        }
        float f17 = this.rightFragmentOpenedProgress;
        if (f17 != 1.0f) {
            if (f17 != 0.0f) {
                float clamp = Utilities.clamp(f17 / 0.4f, 1.0f, 0.0f);
                if (SharedConfig.getDevicePerformanceClass() >= 2) {
                    i19 = canvas.saveLayerAlpha(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + i4) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) ((1.0f - this.rightFragmentOpenedProgress) * 255.0f), 31);
                } else {
                    int save = canvas.save();
                    canvas.clipRect(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + i4) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    i19 = save;
                }
                f6 = 0.0f;
                canvas.translate((-(getMeasuredWidth() - AndroidUtilities.dp(74.0f))) * 0.7f * this.rightFragmentOpenedProgress, 0.0f);
                i7 = i19;
            } else {
                f6 = 0.0f;
                i7 = -1;
            }
            if (this.translationX != f6 || this.cornerProgress != f6) {
                canvas.save();
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                this.rect.set(getMeasuredWidth() - AndroidUtilities.dp(64.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                this.rect.offset(0.0f, -this.translateY);
                canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_pinnedPaint);
                if (this.isSelected) {
                    canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_tabletSeletedPaint);
                }
                if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
                    Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(i5, Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider), this.archiveBackgroundProgress, 1.0f));
                    Theme.dialogs_pinnedPaint.setAlpha((int) (paint3.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
                    canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_pinnedPaint);
                } else if (getIsPinned() || this.drawPinBackground) {
                    Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider));
                    Theme.dialogs_pinnedPaint.setAlpha((int) (paint2.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
                    canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_pinnedPaint);
                }
                canvas.restore();
            }
            if (this.translationX != 0.0f) {
                float f18 = this.cornerProgress;
                if (f18 < 1.0f) {
                    float f19 = f18 + 0.10666667f;
                    this.cornerProgress = f19;
                    if (f19 > 1.0f) {
                        this.cornerProgress = 1.0f;
                    }
                    z8 = true;
                } else {
                    z8 = false;
                }
            } else {
                float f20 = this.cornerProgress;
                if (f20 > 0.0f) {
                    float f21 = f20 - 0.10666667f;
                    this.cornerProgress = f21;
                    if (f21 < 0.0f) {
                        this.cornerProgress = 0.0f;
                    }
                    z8 = true;
                }
                z8 = false;
            }
            if (this.drawNameLock) {
                BaseCell.setDrawableBounds(Theme.dialogs_lockDrawable, this.nameLockLeft, this.nameLockTop);
                Theme.dialogs_lockDrawable.draw(canvas);
            }
            int dp5 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 13.0f);
            if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                dp5 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
            }
            if (this.nameLayout != null) {
                if (this.nameLayoutEllipsizeByGradient && !this.nameLayoutFits) {
                    if (this.nameLayoutEllipsizeLeft && this.fadePaint == null) {
                        Paint paint5 = new Paint();
                        this.fadePaint = paint5;
                        paint5.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{-1, i5}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                        this.fadePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                    } else if (this.fadePaintBack == null) {
                        Paint paint6 = new Paint();
                        this.fadePaintBack = paint6;
                        paint6.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{i5, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                        this.fadePaintBack.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                    }
                    canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), NotificationCenter.voipServiceCreated, 31);
                    int i26 = this.nameLeft;
                    canvas.clipRect(i26, i5, this.nameWidth + i26, getMeasuredHeight());
                }
                if (this.currentDialogFolderId != 0) {
                    TextPaint textPaint2 = Theme.dialogs_namePaint[this.paintIndex];
                    int color4 = Theme.getColor(Theme.key_chats_nameArchived, this.resourcesProvider);
                    textPaint2.linkColor = color4;
                    textPaint2.setColor(color4);
                } else {
                    if (this.encryptedChat == null) {
                        CustomDialog customDialog = this.customDialog;
                        if (customDialog != null) {
                        }
                        TextPaint textPaint3 = Theme.dialogs_namePaint[this.paintIndex];
                        int color5 = Theme.getColor(Theme.key_chats_name, this.resourcesProvider);
                        textPaint3.linkColor = color5;
                        textPaint3.setColor(color5);
                    }
                    TextPaint textPaint4 = Theme.dialogs_namePaint[this.paintIndex];
                    int color6 = Theme.getColor(Theme.key_chats_secretName, this.resourcesProvider);
                    textPaint4.linkColor = color6;
                    textPaint4.setColor(color6);
                }
                canvas.save();
                canvas.translate(this.nameLeft + this.nameLayoutTranslateX, dp5);
                SpoilerEffect.layoutDrawMaybe(this.nameLayout, canvas);
                StaticLayout staticLayout6 = this.nameLayout;
                i8 = i7;
                i11 = -1;
                i9 = 1;
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout6, this.animatedEmojiStackName, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i5, staticLayout6.getPaint().getColor()));
                canvas.restore();
                if (!this.nameLayoutEllipsizeByGradient || this.nameLayoutFits) {
                    i10 = 2;
                } else {
                    canvas.save();
                    if (this.nameLayoutEllipsizeLeft) {
                        canvas.translate(this.nameLeft, 0.0f);
                        i10 = 2;
                        canvas.drawRect(0.0f, 0.0f, AndroidUtilities.dp(24.0f), getMeasuredHeight(), this.fadePaint);
                    } else {
                        i10 = 2;
                        canvas.translate((this.nameLeft + this.nameWidth) - AndroidUtilities.dp(24.0f), 0.0f);
                        canvas.drawRect(0.0f, 0.0f, AndroidUtilities.dp(24.0f), getMeasuredHeight(), this.fadePaintBack);
                    }
                    canvas.restore();
                    canvas.restore();
                }
            } else {
                i8 = i7;
                i9 = 1;
                i10 = 2;
                i11 = -1;
            }
            if (this.timeLayout != null && this.currentDialogFolderId == 0) {
                canvas.save();
                canvas.translate(this.timeLeft, this.timeTop);
                SpoilerEffect.layoutDrawMaybe(this.timeLayout, canvas);
                canvas.restore();
            }
            if (drawLock2()) {
                Theme.dialogs_lock2Drawable.setBounds(this.lock2Left, this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / i10), this.lock2Left + Theme.dialogs_lock2Drawable.getIntrinsicWidth(), this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / i10) + Theme.dialogs_lock2Drawable.getIntrinsicHeight());
                Theme.dialogs_lock2Drawable.draw(canvas);
            }
            if (this.messageNameLayout == null || isForumCell()) {
                i12 = 4;
                f7 = 0.0f;
                i13 = 2;
            } else {
                if (this.currentDialogFolderId != 0) {
                    TextPaint textPaint5 = Theme.dialogs_messageNamePaint;
                    int color7 = Theme.getColor(Theme.key_chats_nameMessageArchived_threeLines, this.resourcesProvider);
                    textPaint5.linkColor = color7;
                    textPaint5.setColor(color7);
                } else if (this.draftMessage != null) {
                    TextPaint textPaint6 = Theme.dialogs_messageNamePaint;
                    int color8 = Theme.getColor(Theme.key_chats_draft, this.resourcesProvider);
                    textPaint6.linkColor = color8;
                    textPaint6.setColor(color8);
                } else {
                    TextPaint textPaint7 = Theme.dialogs_messageNamePaint;
                    int color9 = Theme.getColor(Theme.key_chats_nameMessage_threeLines, this.resourcesProvider);
                    textPaint7.linkColor = color9;
                    textPaint7.setColor(color9);
                }
                canvas.save();
                canvas.translate(this.messageNameLeft, this.messageNameTop);
                try {
                    SpoilerEffect.layoutDrawMaybe(this.messageNameLayout, canvas);
                    staticLayout4 = this.messageNameLayout;
                    i13 = 2;
                    f7 = 0.0f;
                    i12 = 4;
                } catch (Exception e) {
                    e = e;
                    i12 = 4;
                    f7 = 0.0f;
                    i13 = 2;
                }
                try {
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout4, this.animatedEmojiStack2, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i9, staticLayout4.getPaint().getColor()));
                } catch (Exception e2) {
                    e = e2;
                    FileLog.e(e);
                    canvas.restore();
                    if (this.messageLayout != null) {
                    }
                    if (this.buttonLayout == null) {
                    }
                    if (this.currentDialogFolderId == 0) {
                    }
                    if (this.drawUnmute) {
                    }
                    if (this.dialogsType == i14) {
                    }
                    if (this.drawVerified) {
                    }
                    if (!this.drawReorder) {
                    }
                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                    Theme.dialogs_reorderDrawable.draw(canvas);
                    if (this.drawError) {
                    }
                    canvas2 = canvas;
                    if (this.thumbsCount > 0) {
                    }
                    i15 = -1;
                    z11 = false;
                    dialogCellTags = this.tags;
                    if (dialogCellTags != null) {
                    }
                    i16 = i8;
                    if (i16 != i15) {
                    }
                    z3 = z8;
                    r10 = z11;
                    if (this.animatingArchiveAvatar) {
                    }
                    if (this.drawAvatar) {
                        StoriesUtilities.AvatarStoryParams avatarStoryParams = this.storyParams;
                        avatarStoryParams.drawHiddenStoriesAsSegments = this.currentDialogFolderId == 0;
                        StoriesUtilities.drawAvatarWithStory(this.currentDialogId, canvas2, this.avatarImage, avatarStoryParams);
                    }
                    if (this.animatingArchiveAvatar) {
                    }
                    if (this.avatarImage.getVisible()) {
                    }
                    if (this.rightFragmentOpenedProgress > f4) {
                        if (!this.isTopic) {
                        }
                        z7 = z6;
                        RectF rectF = this.storyParams.originalAvatarRect;
                        int width = (int) (((rectF.left + rectF.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
                        RectF rectF2 = this.storyParams.originalAvatarRect;
                        drawCounter(canvas, z7, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width, (int) (((rectF2.left + rectF2.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
                    }
                    if (this.collapseOffset != f4) {
                    }
                    if (this.translationX != f4) {
                    }
                    if (this.drawArchive) {
                        canvas.save();
                        canvas2.translate(f4, (-this.translateY) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress));
                        canvas2.clipRect((int) r10, (int) r10, getMeasuredWidth(), getMeasuredHeight());
                        this.archivedChatsDrawable.draw(canvas2);
                        canvas.restore();
                    }
                    if (this.useSeparator) {
                    }
                    if (this.clipProgress != f4) {
                    }
                    z4 = this.drawReorder;
                    if (!z4) {
                    }
                    if (z4) {
                    }
                    if (!this.archiveHidden) {
                    }
                }
                canvas.restore();
            }
            if (this.messageLayout != null) {
                if (this.currentDialogFolderId != 0) {
                    if (this.chat != null) {
                        TextPaint textPaint8 = Theme.dialogs_messagePaint[this.paintIndex];
                        int color10 = Theme.getColor(Theme.key_chats_nameMessageArchived, this.resourcesProvider);
                        textPaint8.linkColor = color10;
                        textPaint8.setColor(color10);
                    } else {
                        TextPaint textPaint9 = Theme.dialogs_messagePaint[this.paintIndex];
                        int color11 = Theme.getColor(Theme.key_chats_messageArchived, this.resourcesProvider);
                        textPaint9.linkColor = color11;
                        textPaint9.setColor(color11);
                    }
                } else {
                    TextPaint textPaint10 = Theme.dialogs_messagePaint[this.paintIndex];
                    int color12 = Theme.getColor(Theme.key_chats_message, this.resourcesProvider);
                    textPaint10.linkColor = color12;
                    textPaint10.setColor(color12);
                }
                float dp6 = AndroidUtilities.dp(14.0f);
                DialogUpdateHelper dialogUpdateHelper2 = this.updateHelper;
                if (dialogUpdateHelper2.typingOutToTop) {
                    f10 = this.messageTop - (dialogUpdateHelper2.typingProgres * dp6);
                } else {
                    f10 = this.messageTop + (dialogUpdateHelper2.typingProgres * dp6);
                }
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    f10 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                }
                if (this.updateHelper.typingProgres != 1.0f) {
                    canvas.save();
                    canvas.translate(this.messageLeft, f10);
                    int alpha = this.messageLayout.getPaint().getAlpha();
                    this.messageLayout.getPaint().setAlpha((int) (alpha * (1.0f - this.updateHelper.typingProgres)));
                    if (!this.spoilers.isEmpty()) {
                        try {
                            canvas.save();
                            SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
                            SpoilerEffect.layoutDrawMaybe(this.messageLayout, canvas);
                            staticLayout3 = this.messageLayout;
                            f11 = 1.0f;
                        } catch (Exception e3) {
                            e = e3;
                            f11 = 1.0f;
                        }
                        try {
                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout3, this.animatedEmojiStack, -0.075f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i13, staticLayout3.getPaint().getColor()));
                            canvas.restore();
                            for (int i27 = 0; i27 < this.spoilers.size(); i27 += i9) {
                                SpoilerEffect spoilerEffect = this.spoilers.get(i27);
                                spoilerEffect.setColor(this.messageLayout.getPaint().getColor());
                                spoilerEffect.draw(canvas);
                            }
                        } catch (Exception e4) {
                            e = e4;
                            FileLog.e(e);
                            this.messageLayout.getPaint().setAlpha(alpha);
                            canvas.restore();
                            canvas.save();
                            dialogUpdateHelper = this.updateHelper;
                            if (!dialogUpdateHelper.typingOutToTop) {
                            }
                            if (!this.useForceThreeLines) {
                                f12 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                                canvas.translate(this.typingLeft, f12);
                                staticLayout2 = this.typingLayout;
                                if (staticLayout2 != null) {
                                    int alpha2 = staticLayout2.getPaint().getAlpha();
                                    this.typingLayout.getPaint().setAlpha((int) (alpha2 * this.updateHelper.typingProgres));
                                    this.typingLayout.draw(canvas);
                                    this.typingLayout.getPaint().setAlpha(alpha2);
                                }
                                canvas.restore();
                                if (this.typingLayout != null) {
                                }
                                if (this.buttonLayout == null) {
                                }
                                if (this.currentDialogFolderId == 0) {
                                }
                                if (this.drawUnmute) {
                                }
                                if (this.dialogsType == i14) {
                                }
                                if (this.drawVerified) {
                                }
                                if (!this.drawReorder) {
                                }
                                Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                                Theme.dialogs_reorderDrawable.draw(canvas);
                                if (this.drawError) {
                                }
                                canvas2 = canvas;
                                if (this.thumbsCount > 0) {
                                }
                                i15 = -1;
                                z11 = false;
                                dialogCellTags = this.tags;
                                if (dialogCellTags != null) {
                                }
                                i16 = i8;
                                if (i16 != i15) {
                                }
                                z3 = z8;
                                r10 = z11;
                                if (this.animatingArchiveAvatar) {
                                }
                                if (this.drawAvatar) {
                                }
                                if (this.animatingArchiveAvatar) {
                                }
                                if (this.avatarImage.getVisible()) {
                                }
                                if (this.rightFragmentOpenedProgress > f4) {
                                }
                                if (this.collapseOffset != f4) {
                                }
                                if (this.translationX != f4) {
                                }
                                if (this.drawArchive) {
                                }
                                if (this.useSeparator) {
                                }
                                if (this.clipProgress != f4) {
                                }
                                z4 = this.drawReorder;
                                if (!z4) {
                                }
                                if (z4) {
                                }
                                if (!this.archiveHidden) {
                                }
                            }
                            f12 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                            canvas.translate(this.typingLeft, f12);
                            staticLayout2 = this.typingLayout;
                            if (staticLayout2 != null) {
                            }
                            canvas.restore();
                            if (this.typingLayout != null) {
                            }
                            if (this.buttonLayout == null) {
                            }
                            if (this.currentDialogFolderId == 0) {
                            }
                            if (this.drawUnmute) {
                            }
                            if (this.dialogsType == i14) {
                            }
                            if (this.drawVerified) {
                            }
                            if (!this.drawReorder) {
                            }
                            Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                            BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                            Theme.dialogs_reorderDrawable.draw(canvas);
                            if (this.drawError) {
                            }
                            canvas2 = canvas;
                            if (this.thumbsCount > 0) {
                            }
                            i15 = -1;
                            z11 = false;
                            dialogCellTags = this.tags;
                            if (dialogCellTags != null) {
                            }
                            i16 = i8;
                            if (i16 != i15) {
                            }
                            z3 = z8;
                            r10 = z11;
                            if (this.animatingArchiveAvatar) {
                            }
                            if (this.drawAvatar) {
                            }
                            if (this.animatingArchiveAvatar) {
                            }
                            if (this.avatarImage.getVisible()) {
                            }
                            if (this.rightFragmentOpenedProgress > f4) {
                            }
                            if (this.collapseOffset != f4) {
                            }
                            if (this.translationX != f4) {
                            }
                            if (this.drawArchive) {
                            }
                            if (this.useSeparator) {
                            }
                            if (this.clipProgress != f4) {
                            }
                            z4 = this.drawReorder;
                            if (!z4) {
                            }
                            if (z4) {
                            }
                            if (!this.archiveHidden) {
                            }
                        }
                    } else {
                        f11 = 1.0f;
                        SpoilerEffect.layoutDrawMaybe(this.messageLayout, canvas);
                        StaticLayout staticLayout7 = this.messageLayout;
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout7, this.animatedEmojiStack, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i13, staticLayout7.getPaint().getColor()));
                    }
                    this.messageLayout.getPaint().setAlpha(alpha);
                    canvas.restore();
                } else {
                    f11 = 1.0f;
                }
                canvas.save();
                dialogUpdateHelper = this.updateHelper;
                if (!dialogUpdateHelper.typingOutToTop) {
                    f12 = this.messageTop + ((f11 - dialogUpdateHelper.typingProgres) * dp6);
                } else {
                    f12 = this.messageTop - ((f11 - dialogUpdateHelper.typingProgres) * dp6);
                }
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    f12 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                }
                canvas.translate(this.typingLeft, f12);
                staticLayout2 = this.typingLayout;
                if (staticLayout2 != null && this.updateHelper.typingProgres > f7) {
                    int alpha22 = staticLayout2.getPaint().getAlpha();
                    this.typingLayout.getPaint().setAlpha((int) (alpha22 * this.updateHelper.typingProgres));
                    this.typingLayout.draw(canvas);
                    this.typingLayout.getPaint().setAlpha(alpha22);
                }
                canvas.restore();
                if (this.typingLayout != null) {
                    int i28 = this.printingStringType;
                    if (i28 < 0) {
                        DialogUpdateHelper dialogUpdateHelper3 = this.updateHelper;
                        if (dialogUpdateHelper3.typingProgres > f7) {
                        }
                    }
                    if (i28 < 0) {
                        i28 = this.updateHelper.lastKnownTypingType;
                    }
                    StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(i28);
                    if (chatStatusDrawable != null) {
                        canvas.save();
                        chatStatusDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_chats_actionMessage), (int) (Color.alpha(color3) * this.updateHelper.typingProgres)));
                        DialogUpdateHelper dialogUpdateHelper4 = this.updateHelper;
                        if (dialogUpdateHelper4.typingOutToTop) {
                            f13 = this.messageTop + (dp6 * (f11 - dialogUpdateHelper4.typingProgres));
                        } else {
                            f13 = this.messageTop - (dp6 * (f11 - dialogUpdateHelper4.typingProgres));
                        }
                        if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                            f13 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                        }
                        if (i28 == i9 || i28 == i12) {
                            canvas.translate(this.statusDrawableLeft, f13 + (i28 == i9 ? AndroidUtilities.dp(f11) : 0));
                        } else {
                            canvas.translate(this.statusDrawableLeft, f13 + ((AndroidUtilities.dp(18.0f) - chatStatusDrawable.getIntrinsicHeight()) / 2.0f));
                        }
                        chatStatusDrawable.draw(canvas);
                        invalidate();
                        canvas.restore();
                    }
                }
            }
            if (this.buttonLayout == null) {
                canvas.save();
                if (this.buttonBackgroundPaint == null) {
                    this.buttonBackgroundPaint = new Paint(i9);
                }
                if (this.canvasButton == null) {
                    CanvasButton canvasButton = new CanvasButton(this);
                    this.canvasButton = canvasButton;
                    canvasButton.setDelegate(new Runnable() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogCell.this.lambda$onDraw$2();
                        }
                    });
                    this.canvasButton.setLongPress(new Runnable() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogCell.this.lambda$onDraw$3();
                        }
                    });
                }
                if (this.lastTopicMessageUnread && this.topMessageTopicEndIndex != this.topMessageTopicStartIndex && ((i18 = this.dialogsType) == 0 || i18 == 7 || i18 == 8)) {
                    this.canvasButton.setColor(ColorUtils.setAlphaComponent(this.currentMessagePaint.getColor(), Theme.isCurrentThemeDark() ? 36 : 26));
                    if (!this.buttonCreated) {
                        this.canvasButton.rewind();
                        int i29 = this.topMessageTopicEndIndex;
                        if (i29 != this.topMessageTopicStartIndex && i29 > 0) {
                            float f22 = this.messageTop;
                            if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                f22 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                            }
                            RectF rectF3 = AndroidUtilities.rectTmp;
                            StaticLayout staticLayout8 = this.messageLayout;
                            rectF3.set(this.messageLeft + AndroidUtilities.dp(2.0f) + this.messageLayout.getPrimaryHorizontal(0), f22, (this.messageLeft + staticLayout8.getPrimaryHorizontal(Math.min(staticLayout8.getText().length(), this.topMessageTopicEndIndex))) - AndroidUtilities.dp(3.0f), this.buttonTop - AndroidUtilities.dp(4.0f));
                            rectF3.inset(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(4.0f));
                            if (rectF3.right > rectF3.left) {
                                this.canvasButton.addRect(rectF3);
                            }
                        }
                        float lineLeft = this.buttonLayout.getLineLeft(0);
                        RectF rectF4 = AndroidUtilities.rectTmp;
                        rectF4.set(this.buttonLeft + lineLeft + AndroidUtilities.dp(2.0f), this.buttonTop + AndroidUtilities.dp(2.0f), this.buttonLeft + lineLeft + this.buttonLayout.getLineWidth(0) + AndroidUtilities.dp(12.0f), this.buttonTop + this.buttonLayout.getHeight());
                        rectF4.inset(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(3.0f));
                        this.canvasButton.addRect(rectF4);
                    }
                    this.canvasButton.draw(canvas);
                    Theme.dialogs_forum_arrowDrawable.setAlpha(125);
                    Drawable drawable = Theme.dialogs_forum_arrowDrawable;
                    RectF rectF5 = AndroidUtilities.rectTmp;
                    BaseCell.setDrawableBounds(drawable, rectF5.right - AndroidUtilities.dp(18.0f), rectF5.top + ((rectF5.height() - Theme.dialogs_forum_arrowDrawable.getIntrinsicHeight()) / 2.0f));
                    Theme.dialogs_forum_arrowDrawable.draw(canvas);
                }
                canvas.translate(this.buttonLeft, this.buttonTop);
                if (!this.spoilers2.isEmpty()) {
                    try {
                        canvas.save();
                        SpoilerEffect.clipOutCanvas(canvas, this.spoilers2);
                        SpoilerEffect.layoutDrawMaybe(this.buttonLayout, canvas);
                        staticLayout = this.buttonLayout;
                    } catch (Exception e5) {
                        e = e5;
                    }
                    try {
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout, this.animatedEmojiStack3, -0.075f, this.spoilers2, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(3, staticLayout.getPaint().getColor()));
                        canvas.restore();
                        for (int i30 = 0; i30 < this.spoilers2.size(); i30 += i9) {
                            SpoilerEffect spoilerEffect2 = this.spoilers2.get(i30);
                            spoilerEffect2.setColor(this.buttonLayout.getPaint().getColor());
                            spoilerEffect2.draw(canvas);
                        }
                    } catch (Exception e6) {
                        e = e6;
                        FileLog.e(e);
                        canvas.restore();
                        if (this.currentDialogFolderId == 0) {
                        }
                        if (this.drawUnmute) {
                        }
                        if (this.dialogsType == i14) {
                        }
                        if (this.drawVerified) {
                        }
                        if (!this.drawReorder) {
                        }
                        Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                        BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                        Theme.dialogs_reorderDrawable.draw(canvas);
                        if (this.drawError) {
                        }
                        canvas2 = canvas;
                        if (this.thumbsCount > 0) {
                        }
                        i15 = -1;
                        z11 = false;
                        dialogCellTags = this.tags;
                        if (dialogCellTags != null) {
                        }
                        i16 = i8;
                        if (i16 != i15) {
                        }
                        z3 = z8;
                        r10 = z11;
                        if (this.animatingArchiveAvatar) {
                        }
                        if (this.drawAvatar) {
                        }
                        if (this.animatingArchiveAvatar) {
                        }
                        if (this.avatarImage.getVisible()) {
                        }
                        if (this.rightFragmentOpenedProgress > f4) {
                        }
                        if (this.collapseOffset != f4) {
                        }
                        if (this.translationX != f4) {
                        }
                        if (this.drawArchive) {
                        }
                        if (this.useSeparator) {
                        }
                        if (this.clipProgress != f4) {
                        }
                        z4 = this.drawReorder;
                        if (!z4) {
                        }
                        if (z4) {
                        }
                        if (!this.archiveHidden) {
                        }
                    }
                } else {
                    SpoilerEffect.layoutDrawMaybe(this.buttonLayout, canvas);
                    StaticLayout staticLayout9 = this.buttonLayout;
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout9, this.animatedEmojiStack3, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(3, staticLayout9.getPaint().getColor()));
                }
                canvas.restore();
            }
            if (this.currentDialogFolderId == 0) {
                int i31 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                int i32 = this.lastStatusDrawableParams;
                if (i32 >= 0 && i32 != i31 && !this.statusDrawableAnimationInProgress) {
                    createStatusDrawableAnimator(i32, i31);
                }
                boolean z12 = this.statusDrawableAnimationInProgress;
                if (z12) {
                    i31 = this.animateToStatusDrawableParams;
                }
                boolean z13 = (i31 & 1) != 0;
                boolean z14 = (i31 & 2) != 0;
                boolean z15 = (i31 & i12) != 0;
                if (z12) {
                    int i33 = this.animateFromStatusDrawableParams;
                    boolean z16 = (i33 & 1) != 0;
                    boolean z17 = (i33 & 2) != 0;
                    boolean z18 = (i33 & i12) != 0;
                    if (!z13 && !z16 && z18 && !z17 && z14 && z15) {
                        f4 = 0.0f;
                        f3 = 1.0f;
                        i6 = 1;
                        drawCheckStatus(canvas, z13, z14, z15, true, this.statusDrawableProgress);
                        i14 = 2;
                    } else {
                        f3 = 1.0f;
                        f4 = 0.0f;
                        boolean z19 = z17;
                        i14 = 2;
                        boolean z20 = z18;
                        i6 = 1;
                        drawCheckStatus(canvas, z16, z19, z20, false, 1.0f - this.statusDrawableProgress);
                        drawCheckStatus(canvas, z13, z14, z15, false, this.statusDrawableProgress);
                    }
                } else {
                    i14 = 2;
                    i6 = 1;
                    f3 = 1.0f;
                    f4 = 0.0f;
                    drawCheckStatus(canvas, z13, z14, z15, false, 1.0f);
                }
                this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
            } else {
                i14 = 2;
                i6 = 1;
                f3 = 1.0f;
                f4 = 0.0f;
            }
            boolean z21 = !this.drawUnmute || this.dialogMuted;
            if (this.dialogsType == i14 && ((z21 || this.dialogMutedProgress > f4) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
                if (z21) {
                    float f23 = this.dialogMutedProgress;
                    if (f23 != f3) {
                        float f24 = f23 + 0.10666667f;
                        this.dialogMutedProgress = f24;
                        if (f24 > f3) {
                            this.dialogMutedProgress = f3;
                        } else {
                            invalidate();
                        }
                        float dp7 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                        float dp8 = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                        if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                            dp8 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                        }
                        BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp7, dp8);
                        BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp7, dp8);
                        if (this.dialogMutedProgress != f3) {
                            canvas.save();
                            float f25 = this.dialogMutedProgress;
                            canvas.scale(f25, f25, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
                            if (this.drawUnmute) {
                                Theme.dialogs_unmuteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                                Theme.dialogs_unmuteDrawable.draw(canvas);
                                Theme.dialogs_unmuteDrawable.setAlpha(NotificationCenter.voipServiceCreated);
                            } else {
                                Theme.dialogs_muteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                                Theme.dialogs_muteDrawable.draw(canvas);
                                Theme.dialogs_muteDrawable.setAlpha(NotificationCenter.voipServiceCreated);
                            }
                            canvas.restore();
                        } else if (this.drawUnmute) {
                            Theme.dialogs_unmuteDrawable.draw(canvas);
                        } else {
                            Theme.dialogs_muteDrawable.draw(canvas);
                        }
                    }
                }
                if (!z21) {
                    float f26 = this.dialogMutedProgress;
                    if (f26 != f4) {
                        float f27 = f26 - 0.10666667f;
                        this.dialogMutedProgress = f27;
                        if (f27 < f4) {
                            this.dialogMutedProgress = f4;
                        } else {
                            invalidate();
                        }
                    }
                }
                float dp72 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                float dp82 = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                if (!this.useForceThreeLines) {
                    dp82 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                    BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp72, dp82);
                    BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp72, dp82);
                    if (this.dialogMutedProgress != f3) {
                    }
                }
                dp82 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp72, dp82);
                BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp72, dp82);
                if (this.dialogMutedProgress != f3) {
                }
            } else if (this.drawVerified) {
                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                    r25 = 16.5f;
                }
                float dp9 = AndroidUtilities.dp(r25);
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    dp9 -= AndroidUtilities.dp(9.0f);
                }
                BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft - AndroidUtilities.dp(f3), dp9);
                BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft - AndroidUtilities.dp(f3), dp9);
                Theme.dialogs_verifiedDrawable.draw(canvas);
                Theme.dialogs_verifiedCheckDrawable.draw(canvas);
            } else if (this.drawPremium) {
                int dp10 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f);
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    dp10 -= AndroidUtilities.dp(9.0f);
                }
                AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
                if (swapAnimatedEmojiDrawable != null) {
                    swapAnimatedEmojiDrawable.setBounds(this.nameMuteLeft - AndroidUtilities.dp(2.0f), dp10 - AndroidUtilities.dp(4.0f), this.nameMuteLeft + AndroidUtilities.dp(20.0f), (dp10 - AndroidUtilities.dp(4.0f)) + AndroidUtilities.dp(22.0f));
                    this.emojiStatus.setColor(Integer.valueOf(Theme.getColor(Theme.key_chats_verifiedBackground, this.resourcesProvider)));
                    this.emojiStatus.draw(canvas);
                } else {
                    Drawable drawable2 = PremiumGradient.getInstance().premiumStarDrawableMini;
                    int dp11 = this.nameMuteLeft - AndroidUtilities.dp(f3);
                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                        f14 = 15.5f;
                    }
                    BaseCell.setDrawableBounds(drawable2, dp11, AndroidUtilities.dp(f14));
                    drawable2.draw(canvas);
                }
            } else if (this.drawScam != 0) {
                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                    f15 = 15.0f;
                }
                int dp12 = AndroidUtilities.dp(f15);
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    dp12 -= AndroidUtilities.dp(9.0f);
                }
                BaseCell.setDrawableBounds((Drawable) (this.drawScam == i6 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, dp12);
                (this.drawScam == i6 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas);
            }
            if (!this.drawReorder || this.reorderIconProgress != f4) {
                Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                Theme.dialogs_reorderDrawable.draw(canvas);
            }
            if (this.drawError) {
                Theme.dialogs_errorDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                this.rect.set(this.errorLeft, this.errorTop, i17 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
                RectF rectF6 = this.rect;
                float f28 = AndroidUtilities.density * 11.5f;
                canvas.drawRoundRect(rectF6, f28, f28, Theme.dialogs_errorPaint);
                BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
                Theme.dialogs_errorDrawable.draw(canvas);
            } else if (((this.drawCount || this.drawMention) && this.drawCount2) || this.countChangeProgress != f3 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f3) {
                if (this.isTopic) {
                    z9 = this.topicMuted;
                } else {
                    TLRPC$Chat tLRPC$Chat = this.chat;
                    if (tLRPC$Chat != null && tLRPC$Chat.forum && this.forumTopic == null) {
                        z10 = !this.hasUnmutedTopics;
                        canvas2 = canvas;
                        drawCounter(canvas, z10, this.countTop, this.countLeft, this.countLeftOld, 1.0f, false);
                        if (this.drawMention) {
                            Theme.dialogs_countPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                            this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp2 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                            Paint paint7 = (!z10 || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                            RectF rectF7 = this.rect;
                            float f29 = AndroidUtilities.density * 11.5f;
                            canvas2.drawRoundRect(rectF7, f29, f29, paint7);
                            if (this.mentionLayout != null) {
                                Theme.dialogs_countTextPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                canvas.save();
                                canvas2.translate(this.mentionLeft, this.countTop + AndroidUtilities.dp(4.0f));
                                this.mentionLayout.draw(canvas2);
                                canvas.restore();
                            } else {
                                Theme.dialogs_mentionDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                BaseCell.setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.2f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                Theme.dialogs_mentionDrawable.draw(canvas2);
                            }
                        }
                        if (!this.drawReactionMention || this.reactionsMentionsChangeProgress != f3) {
                            Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                            this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                            Paint paint8 = Theme.dialogs_reactionsCountPaint;
                            canvas.save();
                            f8 = this.reactionsMentionsChangeProgress;
                            if (f8 != f3) {
                                if (!this.drawReactionMention) {
                                    f8 = f3 - f8;
                                }
                                canvas2.scale(f8, f8, this.rect.centerX(), this.rect.centerY());
                            }
                            RectF rectF8 = this.rect;
                            float f30 = AndroidUtilities.density * 11.5f;
                            canvas2.drawRoundRect(rectF8, f30, f30, paint8);
                            Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                            BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                            Theme.dialogs_reactionsMentionDrawable.draw(canvas2);
                            canvas.restore();
                        }
                        if (this.thumbsCount > 0) {
                            float f31 = this.updateHelper.typingProgres;
                            if (f31 != f3) {
                                if (f31 > f4) {
                                    canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) ((f3 - f31) * 255.0f), 31);
                                    if (this.updateHelper.typingOutToTop) {
                                        dp3 = -AndroidUtilities.dp(14.0f);
                                        f9 = this.updateHelper.typingProgres;
                                    } else {
                                        dp3 = AndroidUtilities.dp(14.0f);
                                        f9 = this.updateHelper.typingProgres;
                                    }
                                    canvas2.translate(f4, dp3 * f9);
                                }
                                int i34 = 0;
                                while (i34 < this.thumbsCount) {
                                    if (this.thumbImageSeen[i34]) {
                                        if (this.thumbBackgroundPaint == null) {
                                            Paint paint9 = new Paint(i6);
                                            this.thumbBackgroundPaint = paint9;
                                            paint9.setShadowLayer(AndroidUtilities.dp(1.34f), f4, AndroidUtilities.dp(0.34f), 402653184);
                                            c = 0;
                                            this.thumbBackgroundPaint.setColor(0);
                                        } else {
                                            c = 0;
                                        }
                                        RectF rectF9 = AndroidUtilities.rectTmp;
                                        rectF9.set(this.thumbImage[i34].getImageX(), this.thumbImage[i34].getImageY(), this.thumbImage[i34].getImageX2(), this.thumbImage[i34].getImageY2());
                                        canvas2.drawRoundRect(rectF9, this.thumbImage[i34].getRoundRadius()[c], this.thumbImage[i34].getRoundRadius()[i6], this.thumbBackgroundPaint);
                                        this.thumbImage[i34].draw(canvas2);
                                        if (this.drawSpoiler[i34]) {
                                            Path path = this.thumbPath;
                                            if (path == null) {
                                                this.thumbPath = new Path();
                                            } else {
                                                path.rewind();
                                            }
                                            this.thumbPath.addRoundRect(rectF9, this.thumbImage[i34].getRoundRadius()[c], this.thumbImage[i34].getRoundRadius()[i6], Path.Direction.CW);
                                            canvas.save();
                                            canvas2.clipPath(this.thumbPath);
                                            this.thumbSpoiler.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(i11) * 0.325f)));
                                            this.thumbSpoiler.setBounds((int) this.thumbImage[i34].getImageX(), (int) this.thumbImage[i34].getImageY(), (int) this.thumbImage[i34].getImageX2(), (int) this.thumbImage[i34].getImageY2());
                                            this.thumbSpoiler.draw(canvas2);
                                            invalidate();
                                            canvas.restore();
                                        }
                                        if (this.drawPlay[i34]) {
                                            BaseCell.setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage[i34].getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), (int) (this.thumbImage[i34].getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2)));
                                            Theme.dialogs_playDrawable.draw(canvas2);
                                        }
                                    }
                                    i34 += i6;
                                    i11 = -1;
                                }
                                i15 = -1;
                                z11 = false;
                                z11 = false;
                                if (this.updateHelper.typingProgres > f4) {
                                    canvas.restore();
                                }
                                dialogCellTags = this.tags;
                                if (dialogCellTags != null && !dialogCellTags.isEmpty()) {
                                    canvas.save();
                                    canvas2.translate(this.tagsLeft, (getMeasuredHeight() - AndroidUtilities.dp(21.66f)) - (this.useSeparator ? 1 : 0));
                                    this.tags.draw(canvas2, this.tagsRight - this.tagsLeft);
                                    canvas.restore();
                                }
                                i16 = i8;
                                if (i16 != i15) {
                                    canvas2.restoreToCount(i16);
                                }
                                z3 = z8;
                                r10 = z11;
                            }
                        }
                        i15 = -1;
                        z11 = false;
                        dialogCellTags = this.tags;
                        if (dialogCellTags != null) {
                            canvas.save();
                            canvas2.translate(this.tagsLeft, (getMeasuredHeight() - AndroidUtilities.dp(21.66f)) - (this.useSeparator ? 1 : 0));
                            this.tags.draw(canvas2, this.tagsRight - this.tagsLeft);
                            canvas.restore();
                        }
                        i16 = i8;
                        if (i16 != i15) {
                        }
                        z3 = z8;
                        r10 = z11;
                    } else {
                        z9 = this.dialogMuted;
                    }
                }
                z10 = z9;
                canvas2 = canvas;
                drawCounter(canvas, z10, this.countTop, this.countLeft, this.countLeftOld, 1.0f, false);
                if (this.drawMention) {
                }
                if (!this.drawReactionMention) {
                }
                Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                Paint paint82 = Theme.dialogs_reactionsCountPaint;
                canvas.save();
                f8 = this.reactionsMentionsChangeProgress;
                if (f8 != f3) {
                }
                RectF rectF82 = this.rect;
                float f302 = AndroidUtilities.density * 11.5f;
                canvas2.drawRoundRect(rectF82, f302, f302, paint82);
                Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                Theme.dialogs_reactionsMentionDrawable.draw(canvas2);
                canvas.restore();
                if (this.thumbsCount > 0) {
                }
                i15 = -1;
                z11 = false;
                dialogCellTags = this.tags;
                if (dialogCellTags != null) {
                }
                i16 = i8;
                if (i16 != i15) {
                }
                z3 = z8;
                r10 = z11;
            } else if (getIsPinned()) {
                Theme.dialogs_pinnedDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                Theme.dialogs_pinnedDrawable.draw(canvas);
            }
            canvas2 = canvas;
            if (this.thumbsCount > 0) {
            }
            i15 = -1;
            z11 = false;
            dialogCellTags = this.tags;
            if (dialogCellTags != null) {
            }
            i16 = i8;
            if (i16 != i15) {
            }
            z3 = z8;
            r10 = z11;
        } else {
            canvas2 = canvas;
            r10 = 0;
            i6 = 1;
            f3 = 1.0f;
            f4 = 0.0f;
            z3 = false;
        }
        if (this.animatingArchiveAvatar) {
            canvas.save();
            f5 = 170.0f;
            float interpolation2 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + f3;
            canvas2.scale(interpolation2, interpolation2, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
        } else {
            f5 = 170.0f;
        }
        if (this.drawAvatar && (!this.isTopic || (tLRPC$TL_forumTopic2 = this.forumTopic) == null || tLRPC$TL_forumTopic2.id != i6 || (pullForegroundDrawable = this.archivedChatsDrawable) == null || !pullForegroundDrawable.isDraw())) {
            StoriesUtilities.AvatarStoryParams avatarStoryParams2 = this.storyParams;
            avatarStoryParams2.drawHiddenStoriesAsSegments = this.currentDialogFolderId == 0;
            StoriesUtilities.drawAvatarWithStory(this.currentDialogId, canvas2, this.avatarImage, avatarStoryParams2);
        }
        if (this.animatingArchiveAvatar) {
            canvas.restore();
        }
        boolean z22 = (this.avatarImage.getVisible() || !drawAvatarOverlays(canvas)) ? z3 : true;
        if (this.rightFragmentOpenedProgress > f4 && this.currentDialogFolderId == 0) {
            if (!this.isTopic) {
                z6 = this.topicMuted;
            } else {
                TLRPC$Chat tLRPC$Chat2 = this.chat;
                if (tLRPC$Chat2 != null && tLRPC$Chat2.forum && this.forumTopic == null) {
                    z7 = !this.hasUnmutedTopics;
                    RectF rectF10 = this.storyParams.originalAvatarRect;
                    int width2 = (int) (((rectF10.left + rectF10.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
                    RectF rectF22 = this.storyParams.originalAvatarRect;
                    drawCounter(canvas, z7, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width2, (int) (((rectF22.left + rectF22.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
                } else {
                    z6 = this.dialogMuted;
                }
            }
            z7 = z6;
            RectF rectF102 = this.storyParams.originalAvatarRect;
            int width22 = (int) (((rectF102.left + rectF102.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
            RectF rectF222 = this.storyParams.originalAvatarRect;
            drawCounter(canvas, z7, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width22, (int) (((rectF222.left + rectF222.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
        }
        if (this.collapseOffset != f4) {
            canvas.restore();
        }
        if (this.translationX != f4) {
            canvas.restore();
        }
        if (this.drawArchive && ((this.currentDialogFolderId != 0 || (this.isTopic && (tLRPC$TL_forumTopic = this.forumTopic) != null && tLRPC$TL_forumTopic.id == i6)) && this.translationX == f4 && this.archivedChatsDrawable != null)) {
            canvas.save();
            canvas2.translate(f4, (-this.translateY) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress));
            canvas2.clipRect((int) r10, (int) r10, getMeasuredWidth(), getMeasuredHeight());
            this.archivedChatsDrawable.draw(canvas2);
            canvas.restore();
        }
        if (this.useSeparator) {
            int dp13 = (this.fullSeparator || !(this.currentDialogFolderId == 0 || !this.archiveHidden || this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden)) ? 0 : AndroidUtilities.dp(this.messagePaddingStart);
            if (this.rightFragmentOpenedProgress != f3) {
                int alpha3 = Theme.dividerPaint.getAlpha();
                float f32 = this.rightFragmentOpenedProgress;
                if (f32 != f4) {
                    Theme.dividerPaint.setAlpha((int) (alpha3 * (f3 - f32)));
                }
                float measuredHeight2 = (getMeasuredHeight() - i6) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress);
                if (LocaleController.isRTL) {
                    canvas.drawLine(0.0f, measuredHeight2, getMeasuredWidth() - dp13, measuredHeight2, Theme.dividerPaint);
                } else {
                    canvas.drawLine(dp13, measuredHeight2, getMeasuredWidth(), measuredHeight2, Theme.dividerPaint);
                }
                if (this.rightFragmentOpenedProgress != f4) {
                    Theme.dividerPaint.setAlpha(alpha3);
                }
            }
        }
        if (this.clipProgress != f4) {
            if (Build.VERSION.SDK_INT != 24) {
                canvas.restore();
            } else {
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), this.topClip * this.clipProgress, Theme.dialogs_pinnedPaint);
                canvas.drawRect(0.0f, getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)), getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
            }
        }
        z4 = this.drawReorder;
        if (!z4 || this.reorderIconProgress != f4) {
            if (z4) {
                float f33 = this.reorderIconProgress;
                if (f33 < f3) {
                    float f34 = f33 + 0.09411765f;
                    this.reorderIconProgress = f34;
                    if (f34 > f3) {
                        this.reorderIconProgress = f3;
                    }
                    z5 = true;
                }
            } else {
                float f35 = this.reorderIconProgress;
                if (f35 > f4) {
                    float f36 = f35 - 0.09411765f;
                    this.reorderIconProgress = f36;
                    if (f36 < f4) {
                        this.reorderIconProgress = f4;
                    }
                    z5 = true;
                }
            }
            if (!this.archiveHidden) {
                float f37 = this.archiveBackgroundProgress;
                if (f37 > f4) {
                    float f38 = f37 - 0.069565214f;
                    this.archiveBackgroundProgress = f38;
                    if (f38 < f4) {
                        this.archiveBackgroundProgress = f4;
                    }
                    if (this.avatarDrawable.getAvatarType() == 2) {
                        this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                    }
                    z5 = true;
                }
                if (this.animatingArchiveAvatar) {
                    float f39 = this.animatingArchiveAvatarProgress + 16.0f;
                    this.animatingArchiveAvatarProgress = f39;
                    if (f39 >= f5) {
                        this.animatingArchiveAvatarProgress = f5;
                        this.animatingArchiveAvatar = r10;
                    }
                    z5 = true;
                }
                if (!this.drawRevealBackground) {
                    float f40 = this.currentRevealBounceProgress;
                    if (f40 < f3) {
                        float f41 = f40 + 0.09411765f;
                        this.currentRevealBounceProgress = f41;
                        if (f41 > f3) {
                            this.currentRevealBounceProgress = f3;
                            z5 = true;
                        }
                    }
                    float f42 = this.currentRevealProgress;
                    if (f42 < f3) {
                        float f43 = f42 + 0.053333335f;
                        this.currentRevealProgress = f43;
                        if (f43 > f3) {
                            this.currentRevealProgress = f3;
                        }
                        z5 = true;
                    }
                    if (z5) {
                        return;
                    }
                    invalidate();
                    return;
                }
                if (this.currentRevealBounceProgress == f3) {
                    this.currentRevealBounceProgress = f4;
                    z5 = true;
                }
                float f44 = this.currentRevealProgress;
                if (f44 > f4) {
                    float f45 = f44 - 0.053333335f;
                    this.currentRevealProgress = f45;
                    if (f45 < f4) {
                        this.currentRevealProgress = f4;
                    }
                    z5 = true;
                }
                if (z5) {
                }
            } else {
                float f46 = this.archiveBackgroundProgress;
                if (f46 < f3) {
                    float f47 = f46 + 0.069565214f;
                    this.archiveBackgroundProgress = f47;
                    if (f47 > f3) {
                        this.archiveBackgroundProgress = f3;
                    }
                    if (this.avatarDrawable.getAvatarType() == 2) {
                        this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                    }
                    z5 = true;
                }
                if (this.animatingArchiveAvatar) {
                }
                if (!this.drawRevealBackground) {
                }
            }
        }
        z5 = z22;
        if (!this.archiveHidden) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDraw$2() {
        DialogCellDelegate dialogCellDelegate = this.delegate;
        if (dialogCellDelegate != null) {
            dialogCellDelegate.onButtonClicked(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDraw$3() {
        DialogCellDelegate dialogCellDelegate = this.delegate;
        if (dialogCellDelegate != null) {
            dialogCellDelegate.onButtonLongPress(this);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:60:0x0234, code lost:
        if (r3 > 0) goto L86;
     */
    /* JADX WARN: Removed duplicated region for block: B:218:0x062f  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x06b3  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x06c0  */
    /* JADX WARN: Removed duplicated region for block: B:235:0x06df  */
    /* JADX WARN: Removed duplicated region for block: B:238:0x06e6  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x06f9  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x0711  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x071a  */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0728  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0242  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0274  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x029f  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x02b5  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0317  */
    @Override // org.telegram.ui.Stories.StoriesListPlaceProvider.AvatarOverlaysView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean drawAvatarOverlays(Canvas canvas) {
        boolean z;
        int dp;
        float dp2;
        float dp3;
        float dp4;
        float f;
        float dp5;
        float dp6;
        float f2;
        float dp7;
        float dp8;
        float f3;
        float dp9;
        float dp10;
        float f4;
        float f5;
        boolean z2;
        boolean z3;
        float dp11;
        float dp12;
        CheckBox2 checkBox2;
        Drawable drawable;
        TLRPC$Chat tLRPC$Chat = this.chat;
        if (tLRPC$Chat == null || (tLRPC$Chat.flags2 & 2048) == 0) {
            z = false;
        } else {
            float imageY2 = this.avatarImage.getImageY2();
            float imageX2 = this.avatarImage.getImageX2();
            CheckBox2 checkBox22 = this.checkBox;
            float progress = (checkBox22 == null || !checkBox22.isChecked()) ? 1.0f : 1.0f - this.checkBox.getProgress();
            if (this.starBg == null) {
                this.starBg = getContext().getResources().getDrawable(R.drawable.star_small_outline).mutate();
            }
            int color = Theme.getColor(Theme.key_windowBackgroundWhite);
            if (this.starBgColor != color) {
                Drawable drawable2 = this.starBg;
                this.starBgColor = color;
                drawable2.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
            if (this.starFg == null) {
                this.starFg = getContext().getResources().getDrawable(R.drawable.star_small_inner).mutate();
            }
            int dp13 = AndroidUtilities.dp(19.33f);
            Rect rect = AndroidUtilities.rectTmp2;
            int i = (int) imageX2;
            int i2 = (int) imageY2;
            int i3 = i2 - dp13;
            rect.set((AndroidUtilities.dp(1.66f) + i) - dp13, i3, AndroidUtilities.dp(1.66f) + i, i2);
            rect.inset(-AndroidUtilities.dp(1.0f), -AndroidUtilities.dp(1.0f));
            this.starBg.setBounds(rect);
            int i4 = (int) (progress * 255.0f);
            this.starBg.setAlpha(i4);
            this.starBg.draw(canvas);
            rect.set((AndroidUtilities.dp(1.66f) + i) - dp13, i3, i + AndroidUtilities.dp(1.66f), i2);
            this.starFg.setBounds(rect);
            this.starFg.setAlpha(i4);
            this.starFg.draw(canvas);
            z = true;
        }
        float f6 = this.premiumBlockedT.set(this.premiumBlocked && !z);
        float f7 = 10.0f;
        if (f6 > 0.0f) {
            float centerY = this.avatarImage.getCenterY() + AndroidUtilities.dp(18.0f);
            float centerX = this.avatarImage.getCenterX() + AndroidUtilities.dp(18.0f);
            canvas.save();
            Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
            canvas.drawCircle(centerX, centerY, AndroidUtilities.dp(11.33f) * f6, Theme.dialogs_onlineCirclePaint);
            if (this.premiumGradient == null) {
                this.premiumGradient = new PremiumGradient.PremiumGradientTools(Theme.key_premiumGradient1, Theme.key_premiumGradient2, -1, -1, -1, this.resourcesProvider);
            }
            this.premiumGradient.gradientMatrix((int) (centerX - AndroidUtilities.dp(10.0f)), (int) (centerY - AndroidUtilities.dp(10.0f)), (int) (AndroidUtilities.dp(10.0f) + centerX), (int) (AndroidUtilities.dp(10.0f) + centerY), 0.0f, 0.0f);
            canvas.drawCircle(centerX, centerY, AndroidUtilities.dp(10.0f) * f6, this.premiumGradient.paint);
            if (this.lockDrawable == null) {
                Drawable mutate = getContext().getResources().getDrawable(R.drawable.msg_mini_lock2).mutate();
                this.lockDrawable = mutate;
                mutate.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_IN));
            }
            this.lockDrawable.setBounds((int) (centerX - (((drawable.getIntrinsicWidth() / 2.0f) * 0.875f) * f6)), (int) (centerY - (((this.lockDrawable.getIntrinsicHeight() / 2.0f) * 0.875f) * f6)), (int) (centerX + ((this.lockDrawable.getIntrinsicWidth() / 2.0f) * 0.875f * f6)), (int) (centerY + ((this.lockDrawable.getIntrinsicHeight() / 2.0f) * 0.875f * f6)));
            this.lockDrawable.setAlpha((int) (f6 * 255.0f));
            this.lockDrawable.draw(canvas);
            canvas.restore();
            return false;
        } else if (this.isDialogCell && this.currentDialogFolderId == 0 && !z) {
            boolean z4 = (this.ttlPeriod <= 0 || isOnline() || this.hasCall) ? false : true;
            this.showTtl = z4;
            if (this.rightFragmentOpenedProgress != 1.0f && (z4 || this.ttlProgress > 0.0f)) {
                TimerDrawable timerDrawable = this.timerDrawable;
                if (timerDrawable != null) {
                    int time = timerDrawable.getTime();
                    int i5 = this.ttlPeriod;
                    if (time != i5) {
                    }
                    if (this.timerPaint == null) {
                        this.timerPaint = new Paint(1);
                        Paint paint = new Paint(1);
                        this.timerPaint2 = paint;
                        paint.setColor(838860800);
                    }
                    int imageY22 = (int) (this.avatarImage.getImageY2() - AndroidUtilities.dp(9.0f));
                    if (!LocaleController.isRTL) {
                        dp12 = this.storyParams.originalAvatarRect.left + AndroidUtilities.dp(9.0f);
                    } else {
                        dp12 = this.storyParams.originalAvatarRect.right - AndroidUtilities.dp(9.0f);
                    }
                    int i6 = (int) dp12;
                    this.timerDrawable.setBounds(0, 0, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(22.0f));
                    this.timerDrawable.setTime(this.ttlPeriod);
                    if (!this.avatarImage.updateThumbShaderMatrix()) {
                        ImageReceiver imageReceiver = this.avatarImage;
                        BitmapShader bitmapShader = imageReceiver.thumbShader;
                        if (bitmapShader != null) {
                            this.timerPaint.setShader(bitmapShader);
                        } else {
                            BitmapShader bitmapShader2 = imageReceiver.staticThumbShader;
                            if (bitmapShader2 != null) {
                                this.timerPaint.setShader(bitmapShader2);
                            }
                        }
                    } else {
                        this.timerPaint.setShader(null);
                        if (this.avatarImage.getBitmap() != null && !this.avatarImage.getBitmap().isRecycled()) {
                            this.timerPaint.setColor(AndroidUtilities.getDominantColor(this.avatarImage.getBitmap()));
                        } else if (this.avatarImage.getDrawable() instanceof VectorAvatarThumbDrawable) {
                            this.timerPaint.setColor(((VectorAvatarThumbDrawable) this.avatarImage.getDrawable()).gradientTools.getAverageColor());
                        } else {
                            this.timerPaint.setColor(this.avatarDrawable.getColor2());
                        }
                    }
                    canvas.save();
                    float f8 = this.ttlProgress * (1.0f - this.rightFragmentOpenedProgress);
                    checkBox2 = this.checkBox;
                    if (checkBox2 != null) {
                        f8 *= 1.0f - checkBox2.getProgress();
                    }
                    float f9 = i6;
                    float f10 = imageY22;
                    canvas.scale(f8, f8, f9, f10);
                    canvas.drawCircle(f9, f10, AndroidUtilities.dpf2(11.0f), this.timerPaint);
                    canvas.drawCircle(f9, f10, AndroidUtilities.dpf2(11.0f), this.timerPaint2);
                    canvas.save();
                    canvas.translate(f9 - AndroidUtilities.dpf2(11.0f), f10 - AndroidUtilities.dpf2(11.0f));
                    this.timerDrawable.draw(canvas);
                    canvas.restore();
                    canvas.restore();
                }
                this.timerDrawable = TimerDrawable.getTtlIconForDialogs(this.ttlPeriod);
                if (this.timerPaint == null) {
                }
                int imageY222 = (int) (this.avatarImage.getImageY2() - AndroidUtilities.dp(9.0f));
                if (!LocaleController.isRTL) {
                }
                int i62 = (int) dp12;
                this.timerDrawable.setBounds(0, 0, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(22.0f));
                this.timerDrawable.setTime(this.ttlPeriod);
                if (!this.avatarImage.updateThumbShaderMatrix()) {
                }
                canvas.save();
                float f82 = this.ttlProgress * (1.0f - this.rightFragmentOpenedProgress);
                checkBox2 = this.checkBox;
                if (checkBox2 != null) {
                }
                float f92 = i62;
                float f102 = imageY222;
                canvas.scale(f82, f82, f92, f102);
                canvas.drawCircle(f92, f102, AndroidUtilities.dpf2(11.0f), this.timerPaint);
                canvas.drawCircle(f92, f102, AndroidUtilities.dpf2(11.0f), this.timerPaint2);
                canvas.save();
                canvas.translate(f92 - AndroidUtilities.dpf2(11.0f), f102 - AndroidUtilities.dpf2(11.0f));
                this.timerDrawable.draw(canvas);
                canvas.restore();
                canvas.restore();
            }
            TLRPC$User tLRPC$User = this.user;
            if (tLRPC$User != null && !MessagesController.isSupportUser(tLRPC$User) && !this.user.bot) {
                boolean isOnline = isOnline();
                this.wasDrawnOnline = isOnline;
                if (isOnline || this.onlineProgress != 0.0f) {
                    int dp14 = (int) (this.storyParams.originalAvatarRect.bottom - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6.0f : 8.0f));
                    if (LocaleController.isRTL) {
                        float f11 = this.storyParams.originalAvatarRect.left;
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f7 = 6.0f;
                        }
                        dp11 = f11 + AndroidUtilities.dp(f7);
                    } else {
                        float f12 = this.storyParams.originalAvatarRect.right;
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f7 = 6.0f;
                        }
                        dp11 = f12 - AndroidUtilities.dp(f7);
                    }
                    int i7 = (int) dp11;
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                    float f13 = i7;
                    float f14 = dp14;
                    canvas.drawCircle(f13, f14, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_chats_onlineCircle, this.resourcesProvider));
                    canvas.drawCircle(f13, f14, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    if (isOnline) {
                        float f15 = this.onlineProgress;
                        if (f15 < 1.0f) {
                            float f16 = f15 + 0.10666667f;
                            this.onlineProgress = f16;
                            if (f16 > 1.0f) {
                                this.onlineProgress = 1.0f;
                            }
                            z2 = true;
                        }
                    } else {
                        float f17 = this.onlineProgress;
                        if (f17 > 0.0f) {
                            float f18 = f17 - 0.10666667f;
                            this.onlineProgress = f18;
                            if (f18 < 0.0f) {
                                this.onlineProgress = 0.0f;
                            }
                            z2 = true;
                        }
                    }
                    if (this.showTtl) {
                    }
                    this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                    return z3;
                }
                z2 = false;
                if (this.showTtl) {
                }
                this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                return z3;
            }
            TLRPC$Chat tLRPC$Chat2 = this.chat;
            if (tLRPC$Chat2 != null) {
                boolean z5 = tLRPC$Chat2.call_active && tLRPC$Chat2.call_not_empty;
                this.hasCall = z5;
                if ((z5 || this.chatCallProgress != 0.0f) && this.rightFragmentOpenedProgress < 1.0f) {
                    CheckBox2 checkBox23 = this.checkBox;
                    float progress2 = (checkBox23 == null || !checkBox23.isChecked()) ? 1.0f : 1.0f - this.checkBox.getProgress();
                    int dp15 = (int) (this.storyParams.originalAvatarRect.bottom - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6.0f : 8.0f));
                    if (LocaleController.isRTL) {
                        dp = (int) (this.storyParams.originalAvatarRect.left + AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 6.0f));
                    } else {
                        dp = (int) (this.storyParams.originalAvatarRect.right - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 6.0f));
                    }
                    if (this.rightFragmentOpenedProgress != 0.0f) {
                        canvas.save();
                        float f19 = 1.0f - this.rightFragmentOpenedProgress;
                        canvas.scale(f19, f19, dp, dp15);
                    }
                    Paint paint2 = Theme.dialogs_onlineCirclePaint;
                    int i8 = Theme.key_windowBackgroundWhite;
                    paint2.setColor(Theme.getColor(i8, this.resourcesProvider));
                    float f20 = dp;
                    float f21 = dp15;
                    canvas.drawCircle(f20, f21, AndroidUtilities.dp(11.0f) * this.chatCallProgress * progress2, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_chats_onlineCircle, this.resourcesProvider));
                    canvas.drawCircle(f20, f21, AndroidUtilities.dp(9.0f) * this.chatCallProgress * progress2, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(i8, this.resourcesProvider));
                    if (!LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                        this.innerProgress = 0.65f;
                    }
                    int i9 = this.progressStage;
                    if (i9 == 0) {
                        dp2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                        dp9 = AndroidUtilities.dp(3.0f);
                        dp10 = AndroidUtilities.dp(2.0f);
                        f4 = this.innerProgress;
                    } else {
                        if (i9 == 1) {
                            dp2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            dp7 = AndroidUtilities.dp(1.0f);
                            dp8 = AndroidUtilities.dp(4.0f);
                            f3 = this.innerProgress;
                        } else {
                            if (i9 == 2) {
                                dp2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                dp5 = AndroidUtilities.dp(5.0f);
                                dp6 = AndroidUtilities.dp(4.0f);
                                f2 = this.innerProgress;
                            } else {
                                if (i9 == 3) {
                                    dp2 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                    dp3 = AndroidUtilities.dp(1.0f);
                                    dp4 = AndroidUtilities.dp(2.0f);
                                    f = this.innerProgress;
                                } else if (i9 == 4) {
                                    dp2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                    dp9 = AndroidUtilities.dp(3.0f);
                                    dp10 = AndroidUtilities.dp(2.0f);
                                    f4 = this.innerProgress;
                                } else if (i9 == 5) {
                                    dp2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                    dp7 = AndroidUtilities.dp(1.0f);
                                    dp8 = AndroidUtilities.dp(4.0f);
                                    f3 = this.innerProgress;
                                } else if (i9 == 6) {
                                    dp2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                    dp5 = AndroidUtilities.dp(5.0f);
                                    dp6 = AndroidUtilities.dp(4.0f);
                                    f2 = this.innerProgress;
                                } else {
                                    dp2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                    dp3 = AndroidUtilities.dp(1.0f);
                                    dp4 = AndroidUtilities.dp(2.0f);
                                    f = this.innerProgress;
                                }
                                f5 = dp3 + (dp4 * f);
                                if (this.chatCallProgress >= 1.0f || progress2 < 1.0f) {
                                    canvas.save();
                                    float f22 = this.chatCallProgress * progress2;
                                    canvas.scale(f22, f22, f20, f21);
                                }
                                this.rect.set(dp - AndroidUtilities.dp(1.0f), f21 - dp2, dp + AndroidUtilities.dp(1.0f), dp2 + f21);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                float f23 = f21 - f5;
                                float f24 = f21 + f5;
                                this.rect.set(dp - AndroidUtilities.dp(5.0f), f23, dp - AndroidUtilities.dp(3.0f), f24);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                this.rect.set(AndroidUtilities.dp(3.0f) + dp, f23, dp + AndroidUtilities.dp(5.0f), f24);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                if (this.chatCallProgress >= 1.0f || progress2 < 1.0f) {
                                    canvas.restore();
                                }
                                if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                                    z2 = false;
                                } else {
                                    float f25 = this.innerProgress + 0.04f;
                                    this.innerProgress = f25;
                                    if (f25 >= 1.0f) {
                                        this.innerProgress = 0.0f;
                                        int i10 = this.progressStage + 1;
                                        this.progressStage = i10;
                                        if (i10 >= 8) {
                                            this.progressStage = 0;
                                        }
                                    }
                                    z2 = true;
                                }
                                if (!this.hasCall) {
                                    float f26 = this.chatCallProgress;
                                    if (f26 < 1.0f) {
                                        float f27 = f26 + 0.10666667f;
                                        this.chatCallProgress = f27;
                                        if (f27 > 1.0f) {
                                            this.chatCallProgress = 1.0f;
                                        }
                                    }
                                } else {
                                    float f28 = this.chatCallProgress;
                                    if (f28 > 0.0f) {
                                        float f29 = f28 - 0.10666667f;
                                        this.chatCallProgress = f29;
                                        if (f29 < 0.0f) {
                                            this.chatCallProgress = 0.0f;
                                        }
                                    }
                                }
                                if (this.rightFragmentOpenedProgress != 0.0f) {
                                    canvas.restore();
                                }
                                if (this.showTtl) {
                                    float f30 = this.ttlProgress;
                                    if (f30 < 1.0f) {
                                        this.ttlProgress = f30 + 0.10666667f;
                                        z3 = true;
                                    }
                                    z3 = z2;
                                } else {
                                    float f31 = this.ttlProgress;
                                    if (f31 > 0.0f) {
                                        this.ttlProgress = f31 - 0.10666667f;
                                        z3 = true;
                                    }
                                    z3 = z2;
                                }
                                this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                                return z3;
                            }
                            f5 = dp5 - (dp6 * f2);
                            if (this.chatCallProgress >= 1.0f) {
                            }
                            canvas.save();
                            float f222 = this.chatCallProgress * progress2;
                            canvas.scale(f222, f222, f20, f21);
                            this.rect.set(dp - AndroidUtilities.dp(1.0f), f21 - dp2, dp + AndroidUtilities.dp(1.0f), dp2 + f21);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            float f232 = f21 - f5;
                            float f242 = f21 + f5;
                            this.rect.set(dp - AndroidUtilities.dp(5.0f), f232, dp - AndroidUtilities.dp(3.0f), f242);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            this.rect.set(AndroidUtilities.dp(3.0f) + dp, f232, dp + AndroidUtilities.dp(5.0f), f242);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            if (this.chatCallProgress >= 1.0f) {
                            }
                            canvas.restore();
                            if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                            }
                            if (!this.hasCall) {
                            }
                            if (this.rightFragmentOpenedProgress != 0.0f) {
                            }
                            if (this.showTtl) {
                            }
                            this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                            return z3;
                        }
                        f5 = (dp8 * f3) + dp7;
                        if (this.chatCallProgress >= 1.0f) {
                        }
                        canvas.save();
                        float f2222 = this.chatCallProgress * progress2;
                        canvas.scale(f2222, f2222, f20, f21);
                        this.rect.set(dp - AndroidUtilities.dp(1.0f), f21 - dp2, dp + AndroidUtilities.dp(1.0f), dp2 + f21);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        float f2322 = f21 - f5;
                        float f2422 = f21 + f5;
                        this.rect.set(dp - AndroidUtilities.dp(5.0f), f2322, dp - AndroidUtilities.dp(3.0f), f2422);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        this.rect.set(AndroidUtilities.dp(3.0f) + dp, f2322, dp + AndroidUtilities.dp(5.0f), f2422);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        if (this.chatCallProgress >= 1.0f) {
                        }
                        canvas.restore();
                        if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                        }
                        if (!this.hasCall) {
                        }
                        if (this.rightFragmentOpenedProgress != 0.0f) {
                        }
                        if (this.showTtl) {
                        }
                        this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                        return z3;
                    }
                    f5 = dp9 - (dp10 * f4);
                    if (this.chatCallProgress >= 1.0f) {
                    }
                    canvas.save();
                    float f22222 = this.chatCallProgress * progress2;
                    canvas.scale(f22222, f22222, f20, f21);
                    this.rect.set(dp - AndroidUtilities.dp(1.0f), f21 - dp2, dp + AndroidUtilities.dp(1.0f), dp2 + f21);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                    float f23222 = f21 - f5;
                    float f24222 = f21 + f5;
                    this.rect.set(dp - AndroidUtilities.dp(5.0f), f23222, dp - AndroidUtilities.dp(3.0f), f24222);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                    this.rect.set(AndroidUtilities.dp(3.0f) + dp, f23222, dp + AndroidUtilities.dp(5.0f), f24222);
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                    if (this.chatCallProgress >= 1.0f) {
                    }
                    canvas.restore();
                    if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                    }
                    if (!this.hasCall) {
                    }
                    if (this.rightFragmentOpenedProgress != 0.0f) {
                    }
                    if (this.showTtl) {
                    }
                    this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                    return z3;
                }
            }
            z2 = false;
            if (this.showTtl) {
            }
            this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
            return z3;
        } else {
            return false;
        }
    }

    private void drawCounter(Canvas canvas, boolean z, int i, int i2, int i3, float f, boolean z2) {
        Paint paint;
        boolean z3;
        int dp;
        RectF rectF;
        float f2;
        float interpolation;
        RectF rectF2;
        int color;
        boolean z4 = isForumCell() || isFolderCell();
        if (!(this.drawCount && this.drawCount2) && this.countChangeProgress == 1.0f) {
            return;
        }
        float f3 = (this.unreadCount != 0 || this.markUnread) ? this.countChangeProgress : 1.0f - this.countChangeProgress;
        int i4 = NotificationCenter.voipServiceCreated;
        if (z2) {
            if (this.counterPaintOutline == null) {
                Paint paint2 = new Paint();
                this.counterPaintOutline = paint2;
                paint2.setStyle(Paint.Style.STROKE);
                this.counterPaintOutline.setStrokeWidth(AndroidUtilities.dp(2.0f));
                this.counterPaintOutline.setStrokeJoin(Paint.Join.ROUND);
                this.counterPaintOutline.setStrokeCap(Paint.Cap.ROUND);
            }
            this.counterPaintOutline.setColor(ColorUtils.blendARGB(Theme.getColor(Theme.key_windowBackgroundWhite), ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_chats_pinnedOverlay), NotificationCenter.voipServiceCreated), Color.alpha(color) / 255.0f));
        }
        if (this.isTopic && this.forumTopic.read_inbox_max_id == 0) {
            if (this.topicCounterPaint == null) {
                this.topicCounterPaint = new Paint();
            }
            paint = this.topicCounterPaint;
            int color2 = Theme.getColor(z ? Theme.key_topics_unreadCounterMuted : Theme.key_topics_unreadCounter, this.resourcesProvider);
            paint.setColor(color2);
            Theme.dialogs_countTextPaint.setColor(color2);
            i4 = z ? 30 : 40;
            z3 = true;
        } else {
            paint = (z || this.currentDialogFolderId != 0) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint;
            z3 = false;
        }
        StaticLayout staticLayout = this.countOldLayout;
        if (staticLayout == null || this.unreadCount == 0) {
            if (this.unreadCount != 0) {
                staticLayout = this.countLayout;
            }
            paint.setAlpha((int) ((1.0f - this.reorderIconProgress) * i4));
            Theme.dialogs_countTextPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
            this.rect.set(i2 - AndroidUtilities.dp(5.5f), i, dp + this.countWidth + AndroidUtilities.dp(11.0f), AndroidUtilities.dp(23.0f) + i);
            int save = canvas.save();
            if (f != 1.0f) {
                canvas.scale(f, f, this.rect.centerX(), this.rect.centerY());
            }
            if (f3 != 1.0f) {
                if (getIsPinned()) {
                    Theme.dialogs_pinnedDrawable.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
                    BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                    canvas.save();
                    float f4 = 1.0f - f3;
                    canvas.scale(f4, f4, Theme.dialogs_pinnedDrawable.getBounds().centerX(), Theme.dialogs_pinnedDrawable.getBounds().centerY());
                    Theme.dialogs_pinnedDrawable.draw(canvas);
                    canvas.restore();
                }
                canvas.scale(f3, f3, this.rect.centerX(), this.rect.centerY());
            }
            if (z4) {
                if (this.counterPath == null || (rectF = this.counterPathRect) == null || !rectF.equals(this.rect)) {
                    RectF rectF3 = this.counterPathRect;
                    if (rectF3 == null) {
                        this.counterPathRect = new RectF(this.rect);
                    } else {
                        rectF3.set(this.rect);
                    }
                    if (this.counterPath == null) {
                        this.counterPath = new Path();
                    }
                    BubbleCounterPath.addBubbleRect(this.counterPath, this.counterPathRect, AndroidUtilities.dp(11.5f));
                }
                canvas.drawPath(this.counterPath, paint);
                if (z2) {
                    canvas.drawPath(this.counterPath, this.counterPaintOutline);
                }
            } else {
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(11.5f), AndroidUtilities.dp(11.5f), paint);
                if (z2) {
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(11.5f), AndroidUtilities.dp(11.5f), this.counterPaintOutline);
                }
            }
            if (staticLayout != null) {
                canvas.save();
                canvas.translate(i2, i + AndroidUtilities.dp(4.0f));
                staticLayout.draw(canvas);
                canvas.restore();
            }
            canvas.restoreToCount(save);
        } else {
            paint.setAlpha((int) ((1.0f - this.reorderIconProgress) * i4));
            Theme.dialogs_countTextPaint.setAlpha((int) ((1.0f - this.reorderIconProgress) * 255.0f));
            float f5 = f3 * 2.0f;
            float f6 = f5 > 1.0f ? 1.0f : f5;
            float f7 = 1.0f - f6;
            float f8 = (i2 * f6) + (i3 * f7);
            float dp2 = f8 - AndroidUtilities.dp(5.5f);
            float f9 = i;
            this.rect.set(dp2, f9, (this.countWidth * f6) + dp2 + (this.countWidthOld * f7) + AndroidUtilities.dp(11.0f), AndroidUtilities.dp(23.0f) + i);
            if (f3 <= 0.5f) {
                interpolation = CubicBezierInterpolator.EASE_OUT.getInterpolation(f5) * 0.1f;
                f2 = 1.0f;
            } else {
                f2 = 1.0f;
                interpolation = CubicBezierInterpolator.EASE_IN.getInterpolation(1.0f - ((f3 - 0.5f) * 2.0f)) * 0.1f;
            }
            float f10 = interpolation + f2;
            canvas.save();
            float f11 = f10 * f;
            canvas.scale(f11, f11, this.rect.centerX(), this.rect.centerY());
            if (z4) {
                if (this.counterPath == null || (rectF2 = this.counterPathRect) == null || !rectF2.equals(this.rect)) {
                    RectF rectF4 = this.counterPathRect;
                    if (rectF4 == null) {
                        this.counterPathRect = new RectF(this.rect);
                    } else {
                        rectF4.set(this.rect);
                    }
                    if (this.counterPath == null) {
                        this.counterPath = new Path();
                    }
                    BubbleCounterPath.addBubbleRect(this.counterPath, this.counterPathRect, AndroidUtilities.dp(11.5f));
                }
                canvas.drawPath(this.counterPath, paint);
                if (z2) {
                    canvas.drawPath(this.counterPath, this.counterPaintOutline);
                }
            } else {
                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(11.5f), AndroidUtilities.dp(11.5f), paint);
                if (z2) {
                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(11.5f), AndroidUtilities.dp(11.5f), this.counterPaintOutline);
                }
            }
            if (this.countAnimationStableLayout != null) {
                canvas.save();
                canvas.translate(f8, i + AndroidUtilities.dp(4.0f));
                this.countAnimationStableLayout.draw(canvas);
                canvas.restore();
            }
            int alpha = Theme.dialogs_countTextPaint.getAlpha();
            float f12 = alpha;
            Theme.dialogs_countTextPaint.setAlpha((int) (f12 * f6));
            if (this.countAnimationInLayout != null) {
                canvas.save();
                canvas.translate(f8, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f7) + f9 + AndroidUtilities.dp(4.0f));
                this.countAnimationInLayout.draw(canvas);
                canvas.restore();
            } else if (this.countLayout != null) {
                canvas.save();
                canvas.translate(f8, ((this.countAnimationIncrement ? AndroidUtilities.dp(13.0f) : -AndroidUtilities.dp(13.0f)) * f7) + f9 + AndroidUtilities.dp(4.0f));
                this.countLayout.draw(canvas);
                canvas.restore();
            }
            if (this.countOldLayout != null) {
                Theme.dialogs_countTextPaint.setAlpha((int) (f12 * f7));
                canvas.save();
                canvas.translate(f8, ((this.countAnimationIncrement ? -AndroidUtilities.dp(13.0f) : AndroidUtilities.dp(13.0f)) * f6) + f9 + AndroidUtilities.dp(4.0f));
                this.countOldLayout.draw(canvas);
                canvas.restore();
            }
            Theme.dialogs_countTextPaint.setAlpha(alpha);
            canvas.restore();
        }
        if (z3) {
            Theme.dialogs_countTextPaint.setColor(Theme.getColor(Theme.key_chats_unreadCounterText));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createStatusDrawableAnimator(int i, int i2) {
        this.statusDrawableProgress = 0.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.statusDrawableAnimator = ofFloat;
        ofFloat.setDuration(220L);
        this.statusDrawableAnimator.setInterpolator(CubicBezierInterpolator.DEFAULT);
        this.animateFromStatusDrawableParams = i;
        this.animateToStatusDrawableParams = i2;
        this.statusDrawableAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                DialogCell.this.lambda$createStatusDrawableAnimator$4(valueAnimator);
            }
        });
        this.statusDrawableAnimator.addListener(new AnimatorListenerAdapter() { // from class: org.telegram.ui.Cells.DialogCell.5
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                int i3 = (DialogCell.this.drawClock ? 1 : 0) + (DialogCell.this.drawCheck1 ? 2 : 0) + (DialogCell.this.drawCheck2 ? 4 : 0);
                if (DialogCell.this.animateToStatusDrawableParams == i3) {
                    DialogCell.this.statusDrawableAnimationInProgress = false;
                    DialogCell dialogCell = DialogCell.this;
                    dialogCell.lastStatusDrawableParams = dialogCell.animateToStatusDrawableParams;
                } else {
                    DialogCell dialogCell2 = DialogCell.this;
                    dialogCell2.createStatusDrawableAnimator(dialogCell2.animateToStatusDrawableParams, i3);
                }
                DialogCell.this.invalidate();
            }
        });
        this.statusDrawableAnimationInProgress = true;
        this.statusDrawableAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createStatusDrawableAnimator$4(ValueAnimator valueAnimator) {
        this.statusDrawableProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidate();
    }

    public void startOutAnimation() {
        PullForegroundDrawable pullForegroundDrawable = this.archivedChatsDrawable;
        if (pullForegroundDrawable != null) {
            if (this.isTopic) {
                pullForegroundDrawable.outCy = AndroidUtilities.dp(24.0f);
                this.archivedChatsDrawable.outCx = AndroidUtilities.dp(24.0f);
                PullForegroundDrawable pullForegroundDrawable2 = this.archivedChatsDrawable;
                pullForegroundDrawable2.outRadius = 0.0f;
                pullForegroundDrawable2.outImageSize = 0.0f;
            } else {
                pullForegroundDrawable.outCy = this.storyParams.originalAvatarRect.centerY();
                this.archivedChatsDrawable.outCx = this.storyParams.originalAvatarRect.centerX();
                this.archivedChatsDrawable.outRadius = this.storyParams.originalAvatarRect.width() / 2.0f;
                if (MessagesController.getInstance(this.currentAccount).getStoriesController().hasHiddenStories()) {
                    this.archivedChatsDrawable.outRadius -= AndroidUtilities.dpf2(3.5f);
                }
                this.archivedChatsDrawable.outImageSize = this.avatarImage.getBitmapWidth();
            }
            this.archivedChatsDrawable.startOutAnimation();
        }
    }

    public void onReorderStateChanged(boolean z, boolean z2) {
        if ((!getIsPinned() && z) || this.drawReorder == z) {
            if (getIsPinned()) {
                return;
            }
            this.drawReorder = false;
            return;
        }
        this.drawReorder = z;
        if (z2) {
            this.reorderIconProgress = z ? 0.0f : 1.0f;
        } else {
            this.reorderIconProgress = z ? 1.0f : 0.0f;
        }
        invalidate();
    }

    public void setSliding(boolean z) {
        this.isSliding = z;
    }

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        if (drawable == this.translationDrawable || drawable == Theme.dialogs_archiveAvatarDrawable) {
            invalidate(drawable.getBounds());
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        DialogsActivity dialogsActivity;
        if (i == R.id.acc_action_chat_preview && (dialogsActivity = this.parentFragment) != null) {
            dialogsActivity.showChatPreview(this);
            return true;
        }
        return super.performAccessibilityAction(i, bundle);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        PullForegroundDrawable pullForegroundDrawable;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (isFolderCell() && (pullForegroundDrawable = this.archivedChatsDrawable) != null && SharedConfig.archiveHidden && pullForegroundDrawable.pullProgress == 0.0f) {
            accessibilityNodeInfo.setVisibleToUser(false);
        } else {
            accessibilityNodeInfo.addAction(16);
            accessibilityNodeInfo.addAction(32);
            if (!isFolderCell() && this.parentFragment != null && Build.VERSION.SDK_INT >= 21) {
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.acc_action_chat_preview, LocaleController.getString(R.string.AccActionChatPreview)));
            }
        }
        CheckBox2 checkBox2 = this.checkBox;
        if (checkBox2 == null || !checkBox2.isChecked()) {
            return;
        }
        accessibilityNodeInfo.setClassName("android.widget.CheckBox");
        accessibilityNodeInfo.setCheckable(true);
        accessibilityNodeInfo.setChecked(true);
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        MessageObject captionMessage;
        TLRPC$User user;
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        StringBuilder sb = new StringBuilder();
        if (this.currentDialogFolderId == 1) {
            sb.append(LocaleController.getString(R.string.ArchivedChats));
            sb.append(". ");
        } else {
            if (this.encryptedChat != null) {
                sb.append(LocaleController.getString(R.string.AccDescrSecretChat));
                sb.append(". ");
            }
            if (this.isTopic && this.forumTopic != null) {
                sb.append(LocaleController.getString(R.string.AccDescrTopic));
                sb.append(". ");
                sb.append(this.forumTopic.title);
                sb.append(". ");
            } else {
                TLRPC$User tLRPC$User = this.user;
                if (tLRPC$User != null) {
                    if (UserObject.isReplyUser(tLRPC$User)) {
                        sb.append(LocaleController.getString(R.string.RepliesTitle));
                    } else if (UserObject.isAnonymous(this.user)) {
                        sb.append(LocaleController.getString(R.string.AnonymousForward));
                    } else {
                        if (this.user.bot) {
                            sb.append(LocaleController.getString(R.string.Bot));
                            sb.append(". ");
                        }
                        TLRPC$User tLRPC$User2 = this.user;
                        if (tLRPC$User2.self) {
                            sb.append(LocaleController.getString(R.string.SavedMessages));
                        } else {
                            sb.append(ContactsController.formatName(tLRPC$User2.first_name, tLRPC$User2.last_name));
                        }
                    }
                    sb.append(". ");
                } else {
                    TLRPC$Chat tLRPC$Chat = this.chat;
                    if (tLRPC$Chat != null) {
                        if (tLRPC$Chat.broadcast) {
                            sb.append(LocaleController.getString(R.string.AccDescrChannel));
                        } else {
                            sb.append(LocaleController.getString(R.string.AccDescrGroup));
                        }
                        sb.append(". ");
                        sb.append(this.chat.title);
                        sb.append(". ");
                    }
                }
            }
        }
        if (this.drawVerified) {
            sb.append(LocaleController.getString(R.string.AccDescrVerified));
            sb.append(". ");
        }
        if (this.dialogMuted) {
            sb.append(LocaleController.getString(R.string.AccDescrNotificationsMuted));
            sb.append(". ");
        }
        if (isOnline()) {
            sb.append(LocaleController.getString(R.string.AccDescrUserOnline));
            sb.append(". ");
        }
        int i = this.unreadCount;
        if (i > 0) {
            sb.append(LocaleController.formatPluralString("NewMessages", i, new Object[0]));
            sb.append(". ");
        }
        int i2 = this.mentionCount;
        if (i2 > 0) {
            sb.append(LocaleController.formatPluralString("AccDescrMentionCount", i2, new Object[0]));
            sb.append(". ");
        }
        if (this.reactionMentionCount > 0) {
            sb.append(LocaleController.getString(R.string.AccDescrMentionReaction));
            sb.append(". ");
        }
        MessageObject messageObject = this.message;
        if (messageObject == null || this.currentDialogFolderId != 0) {
            accessibilityEvent.setContentDescription(sb);
            setContentDescription(sb);
            return;
        }
        int i3 = this.lastMessageDate;
        if (i3 == 0) {
            i3 = messageObject.messageOwner.date;
        }
        String formatDateAudio = LocaleController.formatDateAudio(i3, true);
        if (this.message.isOut()) {
            sb.append(LocaleController.formatString("AccDescrSentDate", R.string.AccDescrSentDate, formatDateAudio));
        } else {
            sb.append(LocaleController.formatString("AccDescrReceivedDate", R.string.AccDescrReceivedDate, formatDateAudio));
        }
        sb.append(". ");
        if (this.chat != null && !this.message.isOut() && this.message.isFromUser() && this.message.messageOwner.action == null && (user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.messageOwner.from_id.user_id))) != null) {
            sb.append(ContactsController.formatName(user.first_name, user.last_name));
            sb.append(". ");
        }
        if (this.encryptedChat == null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.message.messageText);
            if (!this.message.isMediaEmpty() && (captionMessage = getCaptionMessage()) != null && !TextUtils.isEmpty(captionMessage.caption)) {
                if (sb2.length() > 0) {
                    sb2.append(". ");
                }
                sb2.append(captionMessage.caption);
            }
            StaticLayout staticLayout = this.messageLayout;
            int length = staticLayout == null ? -1 : staticLayout.getText().length();
            if (length > 0) {
                int length2 = sb2.length();
                int indexOf = sb2.indexOf("\n", length);
                if (indexOf < length2 && indexOf >= 0) {
                    length2 = indexOf;
                }
                int indexOf2 = sb2.indexOf("\t", length);
                if (indexOf2 < length2 && indexOf2 >= 0) {
                    length2 = indexOf2;
                }
                int indexOf3 = sb2.indexOf(" ", length);
                if (indexOf3 < length2 && indexOf3 >= 0) {
                    length2 = indexOf3;
                }
                sb.append(sb2.substring(0, length2));
            } else {
                sb.append((CharSequence) sb2);
            }
        }
        accessibilityEvent.setContentDescription(sb);
        setContentDescription(sb);
    }

    private MessageObject getCaptionMessage() {
        CharSequence charSequence;
        if (this.groupMessages == null) {
            MessageObject messageObject = this.message;
            if (messageObject == null || messageObject.caption == null) {
                return null;
            }
            return messageObject;
        }
        MessageObject messageObject2 = null;
        int i = 0;
        for (int i2 = 0; i2 < this.groupMessages.size(); i2++) {
            MessageObject messageObject3 = this.groupMessages.get(i2);
            if (messageObject3 != null && (charSequence = messageObject3.caption) != null) {
                if (!TextUtils.isEmpty(charSequence)) {
                    i++;
                }
                messageObject2 = messageObject3;
            }
        }
        if (i > 1) {
            return null;
        }
        return messageObject2;
    }

    public void updateMessageThumbs() {
        TLRPC$Message tLRPC$Message;
        int i;
        MessageObject messageObject = this.message;
        if (messageObject == null) {
            return;
        }
        String restrictionReason = MessagesController.getInstance(messageObject.currentAccount).getRestrictionReason(this.message.messageOwner.restriction_reason);
        MessageObject messageObject2 = this.message;
        int i2 = 0;
        if (messageObject2 != null && (tLRPC$Message = messageObject2.messageOwner) != null) {
            TLRPC$MessageMedia tLRPC$MessageMedia = tLRPC$Message.media;
            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) {
                this.thumbsCount = 0;
                this.hasVideoThumb = false;
                TLRPC$TL_messageMediaPaidMedia tLRPC$TL_messageMediaPaidMedia = (TLRPC$TL_messageMediaPaidMedia) tLRPC$MessageMedia;
                int i3 = 0;
                while (i2 < tLRPC$TL_messageMediaPaidMedia.extended_media.size() && this.thumbsCount < 3) {
                    TLRPC$MessageExtendedMedia tLRPC$MessageExtendedMedia = tLRPC$TL_messageMediaPaidMedia.extended_media.get(i2);
                    if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMediaPreview) {
                        i = i3 + 1;
                        setThumb(i3, ((TLRPC$TL_messageExtendedMediaPreview) tLRPC$MessageExtendedMedia).thumb);
                    } else if (tLRPC$MessageExtendedMedia instanceof TLRPC$TL_messageExtendedMedia) {
                        i = i3 + 1;
                        setThumb(i3, ((TLRPC$TL_messageExtendedMedia) tLRPC$MessageExtendedMedia).media);
                    } else {
                        i2++;
                    }
                    i3 = i;
                    i2++;
                }
                return;
            }
        }
        ArrayList<MessageObject> arrayList = this.groupMessages;
        if (arrayList != null && arrayList.size() > 1 && TextUtils.isEmpty(restrictionReason) && this.currentDialogFolderId == 0 && this.encryptedChat == null) {
            this.thumbsCount = 0;
            this.hasVideoThumb = false;
            Collections.sort(this.groupMessages, Comparator$-CC.comparingInt(new ToIntFunction() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda4
                @Override // j$.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((MessageObject) obj).getId();
                }
            }));
            while (i2 < Math.min(3, this.groupMessages.size())) {
                MessageObject messageObject3 = this.groupMessages.get(i2);
                if (messageObject3 != null && !messageObject3.needDrawBluredPreview() && (messageObject3.isPhoto() || messageObject3.isNewGif() || messageObject3.isVideo() || messageObject3.isRoundVideo() || messageObject3.isStoryMedia())) {
                    String str = messageObject3.isWebpage() ? messageObject3.messageOwner.media.webpage.type : null;
                    if (!"app".equals(str) && !"profile".equals(str) && !"article".equals(str) && (str == null || !str.startsWith("telegram_"))) {
                        setThumb(i2, messageObject3);
                    }
                }
                i2++;
            }
            return;
        }
        MessageObject messageObject4 = this.message;
        if (messageObject4 == null || this.currentDialogFolderId != 0) {
            return;
        }
        this.thumbsCount = 0;
        this.hasVideoThumb = false;
        if (messageObject4.needDrawBluredPreview()) {
            return;
        }
        if (this.message.isPhoto() || this.message.isNewGif() || this.message.isVideo() || this.message.isRoundVideo() || this.message.isStoryMedia()) {
            String str2 = this.message.isWebpage() ? this.message.messageOwner.media.webpage.type : null;
            if ("app".equals(str2) || "profile".equals(str2) || "article".equals(str2)) {
                return;
            }
            if (str2 == null || !str2.startsWith("telegram_")) {
                setThumb(0, this.message);
            }
        }
    }

    private void setThumb(int i, MessageObject messageObject) {
        TLRPC$MessageMedia tLRPC$MessageMedia;
        ArrayList<TLRPC$PhotoSize> arrayList = messageObject.photoThumbs;
        TLObject tLObject = messageObject.photoThumbsObject;
        if (messageObject.isStoryMedia()) {
            TL_stories$StoryItem tL_stories$StoryItem = messageObject.messageOwner.media.storyItem;
            if (tL_stories$StoryItem == null || (tLRPC$MessageMedia = tL_stories$StoryItem.media) == null) {
                return;
            }
            TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
            if (tLRPC$Document != null) {
                arrayList = tLRPC$Document.thumbs;
                tLObject = tLRPC$Document;
            } else {
                TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia.photo;
                if (tLRPC$Photo != null) {
                    arrayList = tLRPC$Photo.sizes;
                    tLObject = tLRPC$Photo;
                }
            }
        }
        TLRPC$PhotoSize closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 40);
        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize(), false, null, true);
        TLRPC$PhotoSize tLRPC$PhotoSize = closestPhotoSizeWithSize != closestPhotoSizeWithSize2 ? closestPhotoSizeWithSize2 : null;
        if (tLRPC$PhotoSize == null || DownloadController.getInstance(this.currentAccount).canDownloadMedia(messageObject.messageOwner) == 0) {
            tLRPC$PhotoSize = closestPhotoSizeWithSize;
        }
        if (closestPhotoSizeWithSize != null) {
            this.hasVideoThumb = this.hasVideoThumb || messageObject.isVideo() || messageObject.isRoundVideo();
            int i2 = this.thumbsCount;
            if (i2 < 3) {
                this.thumbsCount = i2 + 1;
                this.drawPlay[i] = (messageObject.isVideo() || messageObject.isRoundVideo()) && !messageObject.hasMediaSpoilers();
                this.drawSpoiler[i] = messageObject.hasMediaSpoilers();
                int i3 = (messageObject.type != 1 || tLRPC$PhotoSize == null) ? 0 : tLRPC$PhotoSize.size;
                String str = messageObject.hasMediaSpoilers() ? "5_5_b" : "20_20";
                this.thumbImage[i].setImage(ImageLocation.getForObject(tLRPC$PhotoSize, tLObject), str, ImageLocation.getForObject(closestPhotoSizeWithSize, tLObject), str, i3, null, messageObject, 0);
                this.thumbImage[i].setRoundRadius(AndroidUtilities.dp(messageObject.isRoundVideo() ? 18.0f : 2.0f));
                this.needEmoji = false;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void setThumb(int i, TLRPC$MessageMedia tLRPC$MessageMedia) {
        TLRPC$Document tLRPC$Document;
        ArrayList<TLRPC$PhotoSize> arrayList;
        boolean isVideoDocument;
        TLRPC$PhotoSize closestPhotoSizeWithSize;
        TLRPC$PhotoSize tLRPC$PhotoSize;
        if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) {
            TLRPC$Photo tLRPC$Photo = tLRPC$MessageMedia.photo;
            arrayList = tLRPC$Photo.sizes;
            tLRPC$Document = tLRPC$Photo;
        } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
            isVideoDocument = MessageObject.isVideoDocument(tLRPC$MessageMedia.document);
            tLRPC$Document = tLRPC$MessageMedia.document;
            arrayList = tLRPC$Document.thumbs;
            closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 40);
            TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize(), false, null, true);
            tLRPC$PhotoSize = closestPhotoSizeWithSize != closestPhotoSizeWithSize2 ? closestPhotoSizeWithSize2 : null;
            if (tLRPC$PhotoSize != null || DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.message.messageOwner, tLRPC$MessageMedia) == 0) {
                tLRPC$PhotoSize = closestPhotoSizeWithSize;
            }
            if (closestPhotoSizeWithSize == null) {
                this.hasVideoThumb = this.hasVideoThumb || isVideoDocument;
                int i2 = this.thumbsCount;
                if (i2 < 3) {
                    this.thumbsCount = i2 + 1;
                    this.drawPlay[i] = isVideoDocument;
                    this.drawSpoiler[i] = false;
                    this.thumbImage[i].setImage(ImageLocation.getForObject(tLRPC$PhotoSize, tLRPC$Document), "20_20", ImageLocation.getForObject(closestPhotoSizeWithSize, tLRPC$Document), "20_20", (isVideoDocument || tLRPC$PhotoSize == null) ? 0 : tLRPC$PhotoSize.size, null, this.message, 0);
                    this.thumbImage[i].setRoundRadius(AndroidUtilities.dp(2.0f));
                    this.needEmoji = false;
                    return;
                }
                return;
            }
            return;
        } else {
            tLRPC$Document = null;
            arrayList = null;
        }
        isVideoDocument = false;
        closestPhotoSizeWithSize = FileLoader.getClosestPhotoSizeWithSize(arrayList, 40);
        TLRPC$PhotoSize closestPhotoSizeWithSize22 = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize(), false, null, true);
        if (closestPhotoSizeWithSize != closestPhotoSizeWithSize22) {
        }
        if (tLRPC$PhotoSize != null) {
        }
        tLRPC$PhotoSize = closestPhotoSizeWithSize;
        if (closestPhotoSizeWithSize == null) {
        }
    }

    private void setThumb(int i, TLRPC$PhotoSize tLRPC$PhotoSize) {
        if (tLRPC$PhotoSize != null) {
            this.hasVideoThumb = false;
            int i2 = this.thumbsCount;
            if (i2 < 3) {
                this.thumbsCount = i2 + 1;
                this.drawPlay[i] = false;
                this.drawSpoiler[i] = true;
                this.thumbImage[i].setImage(ImageLocation.getForObject(tLRPC$PhotoSize, this.message.messageOwner), "2_2_b", null, null, 0, null, this.message, 0);
                this.thumbImage[i].setRoundRadius(AndroidUtilities.dp(2.0f));
                this.needEmoji = false;
            }
        }
    }

    public String getMessageNameString() {
        TLRPC$Chat chat;
        TLRPC$User tLRPC$User;
        String str;
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        String str2;
        MessageObject messageObject;
        TLRPC$Message tLRPC$Message2;
        TLRPC$User user;
        TLRPC$Message tLRPC$Message3;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader2;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader3;
        MessageObject messageObject2 = this.message;
        if (messageObject2 == null) {
            return null;
        }
        long fromChatId = messageObject2.getFromChatId();
        long clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (!this.isSavedDialog && this.currentDialogId == clientUserId) {
            long savedDialogId = this.message.getSavedDialogId();
            if (savedDialogId == clientUserId) {
                return null;
            }
            if (savedDialogId != UserObject.ANONYMOUS) {
                TLRPC$Message tLRPC$Message4 = this.message.messageOwner;
                if (tLRPC$Message4 != null && (tLRPC$MessageFwdHeader3 = tLRPC$Message4.fwd_from) != null) {
                    long peerDialogId = DialogObject.getPeerDialogId(tLRPC$MessageFwdHeader3.saved_from_id);
                    if (peerDialogId == 0) {
                        peerDialogId = DialogObject.getPeerDialogId(this.message.messageOwner.fwd_from.from_id);
                    }
                    if (peerDialogId > 0 && peerDialogId != savedDialogId) {
                        return null;
                    }
                }
                fromChatId = savedDialogId;
            }
        }
        if (this.isSavedDialog && (tLRPC$Message3 = this.message.messageOwner) != null && (tLRPC$MessageFwdHeader2 = tLRPC$Message3.fwd_from) != null) {
            fromChatId = DialogObject.getPeerDialogId(tLRPC$MessageFwdHeader2.saved_from_id);
            if (fromChatId == 0) {
                fromChatId = DialogObject.getPeerDialogId(this.message.messageOwner.fwd_from.from_id);
            }
        }
        if (DialogObject.isUserDialog(fromChatId)) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
            chat = null;
        } else {
            chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-fromChatId));
            tLRPC$User = null;
        }
        if (this.currentDialogId == clientUserId) {
            if (tLRPC$User != null) {
                return AndroidUtilities.removeDiacritics(UserObject.getFirstName(tLRPC$User).replace("\n", ""));
            }
            if (chat != null) {
                return AndroidUtilities.removeDiacritics(chat.title.replace("\n", ""));
            }
            return null;
        } else if (this.message.isOutOwner()) {
            return LocaleController.getString(R.string.FromYou);
        } else {
            if (!this.isSavedDialog && (messageObject = this.message) != null && (tLRPC$Message2 = messageObject.messageOwner) != null && (tLRPC$Message2.from_id instanceof TLRPC$TL_peerUser) && (user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.messageOwner.from_id.user_id))) != null) {
                return AndroidUtilities.removeDiacritics(UserObject.getFirstName(user).replace("\n", ""));
            }
            MessageObject messageObject3 = this.message;
            if (messageObject3 == null || (tLRPC$Message = messageObject3.messageOwner) == null || (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) == null || (str2 = tLRPC$MessageFwdHeader.from_name) == null) {
                if (tLRPC$User == null) {
                    if (chat != null && (str = chat.title) != null) {
                        return AndroidUtilities.removeDiacritics(str.replace("\n", ""));
                    }
                    return "DELETED";
                } else if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    if (UserObject.isDeleted(tLRPC$User)) {
                        return LocaleController.getString(R.string.HiddenName);
                    }
                    return AndroidUtilities.removeDiacritics(ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).replace("\n", ""));
                } else {
                    return AndroidUtilities.removeDiacritics(UserObject.getFirstName(tLRPC$User).replace("\n", ""));
                }
            }
            return AndroidUtilities.removeDiacritics(str2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r16v0, types: [android.view.View, org.telegram.ui.Cells.DialogCell] */
    /* JADX WARN: Type inference failed for: r3v4, types: [android.text.SpannableStringBuilder, java.lang.CharSequence, android.text.Spannable] */
    /* JADX WARN: Type inference failed for: r3v5, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r3v6, types: [java.lang.CharSequence] */
    public SpannableStringBuilder getMessageStringFormatted(int i, String str, CharSequence charSequence, boolean z) {
        TLRPC$Message tLRPC$Message;
        CharSequence charSequence2;
        String formatPluralString;
        CharSequence charSequence3;
        String str2;
        SpannableStringBuilder valueOf;
        TLRPC$TL_forumTopic findTopic;
        MessageObject captionMessage = getCaptionMessage();
        MessageObject messageObject = this.message;
        CharSequence charSequence4 = messageObject != null ? messageObject.messageText : null;
        this.applyName = true;
        if (!TextUtils.isEmpty(str)) {
            return formatInternal(i, str, charSequence);
        }
        MessageObject messageObject2 = this.message;
        TLRPC$Message tLRPC$Message2 = messageObject2.messageOwner;
        if (tLRPC$Message2 instanceof TLRPC$TL_messageService) {
            CharSequence charSequence5 = messageObject2.messageTextShort;
            if (charSequence5 == null || ((tLRPC$Message2.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                charSequence5 = messageObject2.messageText;
            }
            if (MessageObject.isTopicActionMessage(messageObject2)) {
                valueOf = formatInternal(i, charSequence5, charSequence);
                if ((this.message.topicIconDrawable[0] instanceof ForumBubbleDrawable) && (findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.currentAccount, this.message.messageOwner, true))) != null) {
                    ((ForumBubbleDrawable) this.message.topicIconDrawable[0]).setColor(findTopic.icon_color);
                }
            } else {
                this.applyName = false;
                valueOf = SpannableStringBuilder.valueOf(charSequence5);
            }
            if (z) {
                applyThumbs(valueOf);
                return valueOf;
            }
            return valueOf;
        } else if (captionMessage != null && (charSequence3 = captionMessage.caption) != null) {
            CharSequence charSequence6 = charSequence3.toString();
            if (!this.needEmoji) {
                str2 = "";
            } else if (captionMessage.isVideo()) {
                str2 = "📹 ";
            } else if (captionMessage.isVoice()) {
                str2 = "🎤 ";
            } else if (captionMessage.isMusic()) {
                str2 = "🎧 ";
            } else if (captionMessage.isPhoto()) {
                str2 = "🖼 ";
            } else {
                str2 = "📎 ";
            }
            if (captionMessage.hasHighlightedWords() && !TextUtils.isEmpty(captionMessage.messageOwner.message)) {
                CharSequence charSequence7 = captionMessage.messageTrimmedToHighlight;
                int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 47);
                if (this.hasNameInMessage) {
                    if (!TextUtils.isEmpty(charSequence)) {
                        measuredWidth = (int) (measuredWidth - this.currentMessagePaint.measureText(charSequence.toString()));
                    }
                    measuredWidth = (int) (measuredWidth - this.currentMessagePaint.measureText(": "));
                }
                if (measuredWidth > 0) {
                    charSequence7 = AndroidUtilities.ellipsizeCenterEnd(charSequence7, captionMessage.highlightedWords.get(0), measuredWidth, this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged).toString();
                }
                return new SpannableStringBuilder(str2).append(charSequence7);
            }
            if (charSequence6.length() > 150) {
                charSequence6 = charSequence6.subSequence(0, 150);
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence6);
            captionMessage.spoilLoginCode();
            MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, charSequence6, spannableStringBuilder, NotificationCenter.attachMenuBotsDidLoad);
            TLRPC$Message tLRPC$Message3 = captionMessage.messageOwner;
            if (tLRPC$Message3 != null) {
                ArrayList<TLRPC$MessageEntity> arrayList = tLRPC$Message3.entities;
                TextPaint textPaint = this.currentMessagePaint;
                MediaDataController.addAnimatedEmojiSpans(arrayList, spannableStringBuilder, textPaint != null ? textPaint.getFontMetricsInt() : null);
            }
            CharSequence append = new SpannableStringBuilder(str2).append(AndroidUtilities.replaceNewLines(spannableStringBuilder));
            if (z) {
                append = applyThumbs(append);
            }
            return formatInternal(i, append, charSequence);
        } else if (tLRPC$Message2.media != null && !messageObject2.isMediaEmpty()) {
            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
            int i2 = Theme.key_chats_attachMessage;
            MessageObject messageObject3 = this.message;
            TLRPC$MessageMedia tLRPC$MessageMedia = messageObject3.messageOwner.media;
            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPoll) {
                TLRPC$TL_messageMediaPoll tLRPC$TL_messageMediaPoll = (TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia;
                TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities = tLRPC$TL_messageMediaPoll.poll.question;
                if (tLRPC$TL_textWithEntities == null || tLRPC$TL_textWithEntities.entities == null) {
                    charSequence2 = String.format("📊 \u2068%s\u2069", tLRPC$TL_textWithEntities.text);
                } else {
                    SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(tLRPC$TL_messageMediaPoll.poll.question.text.replace('\n', ' '));
                    TLRPC$TL_textWithEntities tLRPC$TL_textWithEntities2 = tLRPC$TL_messageMediaPoll.poll.question;
                    MediaDataController.addTextStyleRuns(tLRPC$TL_textWithEntities2.entities, tLRPC$TL_textWithEntities2.text, spannableStringBuilder2);
                    MediaDataController.addAnimatedEmojiSpans(tLRPC$TL_messageMediaPoll.poll.question.entities, spannableStringBuilder2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt());
                    charSequence2 = new SpannableStringBuilder("📊 \u2068").append((CharSequence) spannableStringBuilder2).append((CharSequence) "\u2069");
                }
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                charSequence2 = String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title);
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                charSequence2 = tLRPC$MessageMedia.title;
            } else if (messageObject3.type == 14) {
                charSequence2 = String.format("🎧 \u2068%s - %s\u2069", messageObject3.getMusicAuthor(), this.message.getMusicTitle());
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPaidMedia) {
                int size = ((TLRPC$TL_messageMediaPaidMedia) tLRPC$MessageMedia).extended_media.size();
                if (this.hasVideoThumb) {
                    formatPluralString = size > 1 ? LocaleController.formatPluralString("Media", size, new Object[0]) : LocaleController.getString(R.string.AttachVideo);
                } else {
                    formatPluralString = size > 1 ? LocaleController.formatPluralString("Photos", size, new Object[0]) : LocaleController.getString(R.string.AttachPhoto);
                }
                charSequence2 = StarsIntroActivity.replaceStars(LocaleController.formatString(R.string.AttachPaidMedia, formatPluralString));
                i2 = Theme.key_chats_actionMessage;
            } else if (this.thumbsCount > 1) {
                if (this.hasVideoThumb) {
                    ArrayList<MessageObject> arrayList2 = this.groupMessages;
                    charSequence2 = LocaleController.formatPluralString("Media", arrayList2 == null ? 0 : arrayList2.size(), new Object[0]);
                } else {
                    ArrayList<MessageObject> arrayList3 = this.groupMessages;
                    charSequence2 = LocaleController.formatPluralString("Photos", arrayList3 == null ? 0 : arrayList3.size(), new Object[0]);
                }
                i2 = Theme.key_chats_actionMessage;
            } else {
                charSequence2 = charSequence4.toString();
                i2 = Theme.key_chats_actionMessage;
            }
            if (charSequence2 instanceof String) {
                charSequence2 = ((String) charSequence2).replace('\n', ' ');
            }
            if (z) {
                charSequence2 = applyThumbs(charSequence2);
            }
            SpannableStringBuilder formatInternal = formatInternal(i, charSequence2, charSequence);
            if (!isForumCell()) {
                try {
                    formatInternal.setSpan(new ForegroundColorSpanThemable(i2, this.resourcesProvider), this.hasNameInMessage ? charSequence.length() + 2 : 0, formatInternal.length(), 33);
                } catch (Exception e) {
                    FileLog.e(e);
                }
            }
            return formatInternal;
        } else {
            MessageObject messageObject4 = this.message;
            CharSequence charSequence8 = messageObject4.messageOwner.message;
            if (charSequence8 != null) {
                if (messageObject4.hasHighlightedWords()) {
                    CharSequence charSequence9 = this.message.messageTrimmedToHighlight;
                    if (charSequence9 != null) {
                        charSequence8 = charSequence9;
                    }
                    int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 33);
                    if (this.hasNameInMessage) {
                        if (!TextUtils.isEmpty(charSequence)) {
                            measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(charSequence.toString()));
                        }
                        measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(": "));
                    }
                    if (measuredWidth2 > 0) {
                        charSequence8 = AndroidUtilities.ellipsizeCenterEnd(charSequence8, this.message.highlightedWords.get(0), measuredWidth2, this.currentMessagePaint, NotificationCenter.walletSyncProgressChanged).toString();
                    }
                } else {
                    if (charSequence8.length() > 150) {
                        charSequence8 = charSequence8.subSequence(0, 150);
                    }
                    charSequence8 = AndroidUtilities.replaceNewLines(charSequence8);
                }
                ?? spannableStringBuilder3 = new SpannableStringBuilder(charSequence8);
                MessageObject messageObject5 = this.message;
                if (messageObject5 != null) {
                    messageObject5.spoilLoginCode();
                }
                MediaDataController.addTextStyleRuns(this.message, (Spannable) spannableStringBuilder3, (int) NotificationCenter.attachMenuBotsDidLoad);
                MessageObject messageObject6 = this.message;
                if (messageObject6 != null && (tLRPC$Message = messageObject6.messageOwner) != null) {
                    ArrayList<TLRPC$MessageEntity> arrayList4 = tLRPC$Message.entities;
                    TextPaint textPaint2 = this.currentMessagePaint;
                    MediaDataController.addAnimatedEmojiSpans(arrayList4, spannableStringBuilder3, textPaint2 != null ? textPaint2.getFontMetricsInt() : null);
                }
                if (z) {
                    spannableStringBuilder3 = applyThumbs(spannableStringBuilder3);
                }
                return formatInternal(i, spannableStringBuilder3, charSequence);
            }
            return new SpannableStringBuilder();
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.rightFragmentOpenedProgress == 0.0f && !this.isTopic && this.storyParams.checkOnTouchEvent(motionEvent, this)) {
            return true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if ((!this.isTopic && motionEvent.getAction() == 1) || motionEvent.getAction() == 3) {
            this.storyParams.checkOnTouchEvent(motionEvent, this);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        CanvasButton canvasButton;
        int i;
        if (this.rightFragmentOpenedProgress == 0.0f && !this.isTopic && this.storyParams.checkOnTouchEvent(motionEvent, this)) {
            return true;
        }
        DialogCellDelegate dialogCellDelegate = this.delegate;
        if ((dialogCellDelegate == null || dialogCellDelegate.canClickButtonInside()) && this.lastTopicMessageUnread && (canvasButton = this.canvasButton) != null && this.buttonLayout != null && (((i = this.dialogsType) == 0 || i == 7 || i == 8) && canvasButton.checkTouchEvent(motionEvent))) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setClipProgress(float f) {
        this.clipProgress = f;
        invalidate();
    }

    public float getClipProgress() {
        return this.clipProgress;
    }

    public void setTopClip(int i) {
        this.topClip = i;
    }

    public void setBottomClip(int i) {
        this.bottomClip = i;
    }

    public void setArchivedPullAnimation(PullForegroundDrawable pullForegroundDrawable) {
        this.archivedChatsDrawable = pullForegroundDrawable;
    }

    public int getCurrentDialogFolderId() {
        return this.currentDialogFolderId;
    }

    public boolean isDialogFolder() {
        return this.currentDialogFolderId > 0;
    }

    public MessageObject getMessage() {
        return this.message;
    }

    public void setDialogCellDelegate(DialogCellDelegate dialogCellDelegate) {
        this.delegate = dialogCellDelegate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class DialogUpdateHelper {
        public long lastDrawnDialogId;
        public boolean lastDrawnDialogIsFolder;
        public int lastDrawnDraftHash;
        public boolean lastDrawnHasCall;
        public long lastDrawnMessageId;
        public boolean lastDrawnPinned;
        public Integer lastDrawnPrintingType;
        public long lastDrawnReadState;
        public int lastDrawnSizeHash;
        public boolean lastDrawnTranslated;
        public int lastKnownTypingType;
        public int lastTopicsCount;
        long startWaitingTime;
        public boolean typingOutToTop;
        public float typingProgres;
        boolean waitngNewMessageFroTypingAnimation;

        private DialogUpdateHelper() {
            this.waitngNewMessageFroTypingAnimation = false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:45:0x0130, code lost:
            if (org.telegram.messenger.MessagesController.getInstance(r19.this$0.currentAccount).getTopicsController().endIsReached(-r19.this$0.currentDialogId) != false) goto L132;
         */
        /* JADX WARN: Removed duplicated region for block: B:112:0x025c  */
        /* JADX WARN: Removed duplicated region for block: B:115:0x0269  */
        /* JADX WARN: Removed duplicated region for block: B:119:0x0275  */
        /* JADX WARN: Removed duplicated region for block: B:134:0x02a5  */
        /* JADX WARN: Removed duplicated region for block: B:135:0x02a8  */
        /* JADX WARN: Removed duplicated region for block: B:137:0x02ad  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00f4  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x013b  */
        /* JADX WARN: Removed duplicated region for block: B:61:0x0187  */
        /* JADX WARN: Removed duplicated region for block: B:72:0x01c3  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x01c5  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x01dd  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x0210  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean update() {
            Integer num;
            int i;
            boolean z;
            TLRPC$DraftMessage draft;
            int i2;
            int i3;
            boolean z2;
            TLRPC$Dialog tLRPC$Dialog = MessagesController.getInstance(DialogCell.this.currentAccount).dialogs_dict.get(DialogCell.this.currentDialogId);
            if (tLRPC$Dialog == null) {
                if (DialogCell.this.dialogsType != 3 || this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                    return false;
                }
                this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                return true;
            }
            int id = DialogCell.this.message == null ? 0 : DialogCell.this.message.getId() + DialogCell.this.message.hashCode();
            long j = tLRPC$Dialog.read_inbox_max_id + (tLRPC$Dialog.read_outbox_max_id << 8) + ((tLRPC$Dialog.unread_count + (tLRPC$Dialog.unread_mark ? -1 : 0)) << 16) + (tLRPC$Dialog.unread_reactions_count > 0 ? 262144 : 0) + (tLRPC$Dialog.unread_mentions_count > 0 ? 524288 : 0);
            if (!DialogCell.this.isForumCell()) {
                DialogCell dialogCell = DialogCell.this;
                if ((dialogCell.isDialogCell || dialogCell.isTopic) && !TextUtils.isEmpty(MessagesController.getInstance(DialogCell.this.currentAccount).getPrintingString(DialogCell.this.currentDialogId, DialogCell.this.getTopicId(), true))) {
                    num = MessagesController.getInstance(DialogCell.this.currentAccount).getPrintingStringType(DialogCell.this.currentDialogId, DialogCell.this.getTopicId());
                    int measuredWidth = DialogCell.this.getMeasuredWidth() + (DialogCell.this.getMeasuredHeight() << 16);
                    if (DialogCell.this.isForumCell()) {
                        ArrayList<TLRPC$TL_forumTopic> topics = MessagesController.getInstance(DialogCell.this.currentAccount).getTopicsController().getTopics(-DialogCell.this.currentDialogId);
                        i = topics == null ? -1 : topics.size();
                        if (i == -1) {
                        }
                        if (DialogCell.this.isTopic) {
                            z = MediaDataController.getInstance(DialogCell.this.currentAccount).getDraftVoice(DialogCell.this.currentDialogId, (long) DialogCell.this.getTopicId()) != null;
                            TLRPC$DraftMessage draft2 = !z ? MediaDataController.getInstance(DialogCell.this.currentAccount).getDraft(DialogCell.this.currentDialogId, DialogCell.this.getTopicId()) : null;
                            if (draft2 == null || !TextUtils.isEmpty(draft2.message)) {
                                draft = draft2;
                                if (draft != null) {
                                    i2 = 0;
                                } else {
                                    int hashCode = draft.message.hashCode();
                                    TLRPC$InputReplyTo tLRPC$InputReplyTo = draft.reply_to;
                                    i2 = hashCode + (tLRPC$InputReplyTo != null ? tLRPC$InputReplyTo.reply_to_msg_id << 16 : 0);
                                }
                                boolean z3 = DialogCell.this.chat == null && DialogCell.this.chat.call_active && DialogCell.this.chat.call_not_empty;
                                boolean isTranslatingDialog = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                                if (this.lastDrawnSizeHash != measuredWidth) {
                                    i3 = measuredWidth;
                                    if (this.lastDrawnMessageId == id && this.lastDrawnTranslated == isTranslatingDialog && this.lastDrawnDialogId == DialogCell.this.currentDialogId && this.lastDrawnDialogIsFolder == tLRPC$Dialog.isFolder && this.lastDrawnReadState == j && Objects.equals(this.lastDrawnPrintingType, num) && this.lastTopicsCount == i && i2 == this.lastDrawnDraftHash && this.lastDrawnPinned == DialogCell.this.drawPin && this.lastDrawnHasCall == z3 && DialogCell.this.draftVoice == z) {
                                        return false;
                                    }
                                } else {
                                    i3 = measuredWidth;
                                }
                                if (this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                                    this.typingProgres = num == null ? 0.0f : 1.0f;
                                    this.waitngNewMessageFroTypingAnimation = false;
                                } else if (!Objects.equals(this.lastDrawnPrintingType, num) || this.waitngNewMessageFroTypingAnimation) {
                                    boolean z4 = this.waitngNewMessageFroTypingAnimation;
                                    if (!z4 && num == null) {
                                        this.waitngNewMessageFroTypingAnimation = true;
                                        this.startWaitingTime = System.currentTimeMillis();
                                    } else if (z4 && this.lastDrawnMessageId != id) {
                                        z2 = false;
                                        this.waitngNewMessageFroTypingAnimation = false;
                                        if (this.lastDrawnMessageId == id) {
                                            this.typingOutToTop = z2;
                                        } else {
                                            this.typingOutToTop = true;
                                        }
                                    }
                                    z2 = false;
                                    if (this.lastDrawnMessageId == id) {
                                    }
                                }
                                if (num != null) {
                                    this.lastKnownTypingType = num.intValue();
                                }
                                this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                                this.lastDrawnMessageId = id;
                                this.lastDrawnDialogIsFolder = tLRPC$Dialog.isFolder;
                                this.lastDrawnReadState = j;
                                this.lastDrawnPrintingType = num;
                                this.lastDrawnSizeHash = i3;
                                this.lastDrawnDraftHash = i2;
                                this.lastTopicsCount = i;
                                this.lastDrawnPinned = DialogCell.this.drawPin;
                                this.lastDrawnHasCall = z3;
                                this.lastDrawnTranslated = isTranslatingDialog;
                                return true;
                            }
                            draft = null;
                            if (draft != null) {
                            }
                            if (DialogCell.this.chat == null) {
                            }
                            boolean isTranslatingDialog2 = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                            if (this.lastDrawnSizeHash != measuredWidth) {
                            }
                            if (this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                            }
                            if (num != null) {
                            }
                            this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                            this.lastDrawnMessageId = id;
                            this.lastDrawnDialogIsFolder = tLRPC$Dialog.isFolder;
                            this.lastDrawnReadState = j;
                            this.lastDrawnPrintingType = num;
                            this.lastDrawnSizeHash = i3;
                            this.lastDrawnDraftHash = i2;
                            this.lastTopicsCount = i;
                            this.lastDrawnPinned = DialogCell.this.drawPin;
                            this.lastDrawnHasCall = z3;
                            this.lastDrawnTranslated = isTranslatingDialog2;
                            return true;
                        }
                        DialogCell dialogCell2 = DialogCell.this;
                        if (dialogCell2.isDialogCell) {
                            z = MediaDataController.getInstance(dialogCell2.currentAccount).getDraftVoice(DialogCell.this.currentDialogId, 0L) != null;
                            if (!z) {
                                draft = MediaDataController.getInstance(DialogCell.this.currentAccount).getDraft(DialogCell.this.currentDialogId, 0L);
                                if (draft != null) {
                                }
                                if (DialogCell.this.chat == null) {
                                }
                                boolean isTranslatingDialog22 = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                                if (this.lastDrawnSizeHash != measuredWidth) {
                                }
                                if (this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                                }
                                if (num != null) {
                                }
                                this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                                this.lastDrawnMessageId = id;
                                this.lastDrawnDialogIsFolder = tLRPC$Dialog.isFolder;
                                this.lastDrawnReadState = j;
                                this.lastDrawnPrintingType = num;
                                this.lastDrawnSizeHash = i3;
                                this.lastDrawnDraftHash = i2;
                                this.lastTopicsCount = i;
                                this.lastDrawnPinned = DialogCell.this.drawPin;
                                this.lastDrawnHasCall = z3;
                                this.lastDrawnTranslated = isTranslatingDialog22;
                                return true;
                            }
                        } else {
                            z = false;
                        }
                        draft = null;
                        if (draft != null) {
                        }
                        if (DialogCell.this.chat == null) {
                        }
                        boolean isTranslatingDialog222 = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                        if (this.lastDrawnSizeHash != measuredWidth) {
                        }
                        if (this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                        }
                        if (num != null) {
                        }
                        this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                        this.lastDrawnMessageId = id;
                        this.lastDrawnDialogIsFolder = tLRPC$Dialog.isFolder;
                        this.lastDrawnReadState = j;
                        this.lastDrawnPrintingType = num;
                        this.lastDrawnSizeHash = i3;
                        this.lastDrawnDraftHash = i2;
                        this.lastTopicsCount = i;
                        this.lastDrawnPinned = DialogCell.this.drawPin;
                        this.lastDrawnHasCall = z3;
                        this.lastDrawnTranslated = isTranslatingDialog222;
                        return true;
                    }
                    i = 0;
                    if (DialogCell.this.isTopic) {
                    }
                }
            }
            num = null;
            int measuredWidth2 = DialogCell.this.getMeasuredWidth() + (DialogCell.this.getMeasuredHeight() << 16);
            if (DialogCell.this.isForumCell()) {
            }
            i = 0;
            if (DialogCell.this.isTopic) {
            }
        }

        public void updateAnimationValues() {
            if (!this.waitngNewMessageFroTypingAnimation) {
                if (this.lastDrawnPrintingType != null && DialogCell.this.typingLayout != null) {
                    float f = this.typingProgres;
                    if (f != 1.0f) {
                        this.typingProgres = f + 0.08f;
                        DialogCell.this.invalidate();
                        this.typingProgres = Utilities.clamp(this.typingProgres, 1.0f, 0.0f);
                        return;
                    }
                }
                if (this.lastDrawnPrintingType == null) {
                    float f2 = this.typingProgres;
                    if (f2 != 0.0f) {
                        this.typingProgres = f2 - 0.08f;
                        DialogCell.this.invalidate();
                    }
                }
                this.typingProgres = Utilities.clamp(this.typingProgres, 1.0f, 0.0f);
                return;
            }
            if (System.currentTimeMillis() - this.startWaitingTime > 100) {
                this.waitngNewMessageFroTypingAnimation = false;
            }
            DialogCell.this.invalidate();
        }
    }

    @Override // org.telegram.ui.Cells.BaseCell, android.view.View
    public void invalidate() {
        if (StoryViewer.animationInProgress) {
            return;
        }
        super.invalidate();
    }

    @Override // android.view.View
    public void invalidate(int i, int i2, int i3, int i4) {
        if (StoryViewer.animationInProgress) {
            return;
        }
        super.invalidate(i, i2, i3, i4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class ForumFormattedNames {
        CharSequence formattedNames;
        boolean isLoadingState;
        int lastMessageId;
        boolean lastTopicMessageUnread;
        int topMessageTopicEndIndex;
        int topMessageTopicStartIndex;

        private ForumFormattedNames() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void formatTopicsNames(int i, MessageObject messageObject, TLRPC$Chat tLRPC$Chat) {
            long j;
            boolean z;
            int i2;
            int id = (messageObject == null || tLRPC$Chat == null) ? 0 : messageObject.getId();
            if (this.lastMessageId != id || this.isLoadingState) {
                this.topMessageTopicStartIndex = 0;
                this.topMessageTopicEndIndex = 0;
                this.lastTopicMessageUnread = false;
                this.isLoadingState = false;
                this.lastMessageId = id;
                TextPaint textPaint = Theme.dialogs_messagePaint[0];
                if (tLRPC$Chat != null) {
                    ArrayList<TLRPC$TL_forumTopic> topics = MessagesController.getInstance(i).getTopicsController().getTopics(tLRPC$Chat.id);
                    boolean z2 = true;
                    if (topics != null && !topics.isEmpty()) {
                        ArrayList arrayList = new ArrayList(topics);
                        Collections.sort(arrayList, Comparator$-CC.comparingInt(new ToIntFunction() { // from class: org.telegram.ui.Cells.DialogCell$ForumFormattedNames$$ExternalSyntheticLambda0
                            @Override // j$.util.function.ToIntFunction
                            public final int applyAsInt(Object obj) {
                                int lambda$formatTopicsNames$0;
                                lambda$formatTopicsNames$0 = DialogCell.ForumFormattedNames.lambda$formatTopicsNames$0((TLRPC$TL_forumTopic) obj);
                                return lambda$formatTopicsNames$0;
                            }
                        }));
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                        if (messageObject != null) {
                            j = MessageObject.getTopicId(i, messageObject.messageOwner, true);
                            TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(i).getTopicsController().findTopic(tLRPC$Chat.id, j);
                            if (findTopic != null) {
                                CharSequence topicSpannedName = ForumUtilities.getTopicSpannedName(findTopic, textPaint, false);
                                spannableStringBuilder.append(topicSpannedName);
                                i2 = findTopic.unread_count > 0 ? topicSpannedName.length() : 0;
                                this.topMessageTopicStartIndex = 0;
                                this.topMessageTopicEndIndex = topicSpannedName.length();
                                if (messageObject.isOutOwner()) {
                                    this.lastTopicMessageUnread = false;
                                } else {
                                    this.lastTopicMessageUnread = findTopic.unread_count > 0;
                                }
                            } else {
                                this.lastTopicMessageUnread = false;
                                i2 = 0;
                            }
                            if (this.lastTopicMessageUnread) {
                                spannableStringBuilder.append((CharSequence) " ");
                                spannableStringBuilder.setSpan(new FixedWidthSpan(AndroidUtilities.dp(3.0f)), spannableStringBuilder.length() - 1, spannableStringBuilder.length(), 0);
                                z = true;
                            } else {
                                z = false;
                            }
                        } else {
                            j = 0;
                            z = false;
                            i2 = 0;
                        }
                        for (int i3 = 0; i3 < Math.min(4, arrayList.size()); i3++) {
                            if (((TLRPC$TL_forumTopic) arrayList.get(i3)).id != j) {
                                if (spannableStringBuilder.length() != 0) {
                                    if (z2 && z) {
                                        spannableStringBuilder.append((CharSequence) " ");
                                    } else {
                                        spannableStringBuilder.append((CharSequence) ", ");
                                    }
                                }
                                spannableStringBuilder.append(ForumUtilities.getTopicSpannedName((TLRPC$ForumTopic) arrayList.get(i3), textPaint, false));
                                z2 = false;
                            }
                        }
                        if (i2 > 0) {
                            spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.bold(), 0, Theme.key_chats_name, null), 0, Math.min(spannableStringBuilder.length(), i2 + 2), 0);
                        }
                        this.formattedNames = spannableStringBuilder;
                    } else if (!MessagesController.getInstance(i).getTopicsController().endIsReached(tLRPC$Chat.id)) {
                        MessagesController.getInstance(i).getTopicsController().preloadTopics(tLRPC$Chat.id);
                        this.formattedNames = LocaleController.getString(R.string.Loading);
                        this.isLoadingState = true;
                    } else {
                        this.formattedNames = "no created topics";
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ int lambda$formatTopicsNames$0(TLRPC$TL_forumTopic tLRPC$TL_forumTopic) {
            return -tLRPC$TL_forumTopic.top_message;
        }
    }

    private ColorFilter getAdaptiveEmojiColorFilter(int i, int i2) {
        if (this.adaptiveEmojiColorFilter == null) {
            this.adaptiveEmojiColor = new int[4];
            this.adaptiveEmojiColorFilter = new ColorFilter[4];
        }
        if (i2 != this.adaptiveEmojiColor[i] || this.adaptiveEmojiColorFilter[i] == null) {
            ColorFilter[] colorFilterArr = this.adaptiveEmojiColorFilter;
            this.adaptiveEmojiColor[i] = i2;
            colorFilterArr[i] = new PorterDuffColorFilter(i2, PorterDuff.Mode.SRC_IN);
        }
        return this.adaptiveEmojiColorFilter[i];
    }

    public void showPremiumBlocked(boolean z) {
        Runnable runnable = this.unsubscribePremiumBlocked;
        if (z != (runnable != null)) {
            if (!z && runnable != null) {
                runnable.run();
                this.unsubscribePremiumBlocked = null;
            } else if (z) {
                this.unsubscribePremiumBlocked = NotificationCenter.getInstance(this.currentAccount).listen(this, NotificationCenter.userIsPremiumBlockedUpadted, new Utilities.Callback() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda6
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        DialogCell.this.lambda$showPremiumBlocked$5((Object[]) obj);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPremiumBlocked$5(Object[] objArr) {
        updatePremiumBlocked(true);
    }

    private void updatePremiumBlocked(boolean z) {
        boolean z2 = this.premiumBlocked;
        boolean z3 = (this.unsubscribePremiumBlocked == null || this.user == null || !MessagesController.getInstance(this.currentAccount).isUserPremiumBlocked(this.user.id)) ? false : true;
        this.premiumBlocked = z3;
        if (z2 != z3) {
            if (!z) {
                this.premiumBlockedT.set(z3, true);
            }
            invalidate();
        }
    }

    @Override // org.telegram.ui.Cells.BaseCell
    protected boolean allowCaching() {
        return this.rightFragmentOpenedProgress <= 0.0f;
    }
}
