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
import org.telegram.tgnet.TLRPC$TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC$TL_messageMediaGame;
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveaway;
import org.telegram.tgnet.TLRPC$TL_messageMediaGiveawayResults;
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageMediaStory;
import org.telegram.tgnet.TLRPC$TL_messageReactions;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_peerChannel;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsAdapter;
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
        Drawable[] drawableArr = messageObject.topicIconDrawable;
        if (drawableArr[0] instanceof ForumBubbleDrawable) {
            ((ForumBubbleDrawable) drawableArr[0]).setColor(tLRPC$TL_forumTopic.icon_color);
        }
        this.currentDialogId = j;
        this.lastDialogChangedTime = System.currentTimeMillis();
        this.message = messageObject;
        this.isDialogCell = false;
        this.showTopicIconInName = z;
        TLRPC$Message tLRPC$Message = messageObject.messageOwner;
        this.lastMessageDate = tLRPC$Message.date;
        int i = tLRPC$Message.edit_date;
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
                ImageReceiver[] imageReceiverArr2 = this.thumbImage;
                imageReceiverArr2[i2].ignoreNotifications = true;
                imageReceiverArr2[i2].setRoundRadius(AndroidUtilities.dp(2.0f));
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
        if (messageObject != null) {
            int i2 = messageObject.messageOwner.edit_date;
        }
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
        String replace;
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
                    replace = tLRPC$Chat.title.replace('\n', ' ');
                } else if (tLRPC$User == null) {
                    continue;
                } else if (UserObject.isDeleted(tLRPC$User)) {
                    replace = LocaleController.getString("HiddenName", R.string.HiddenName);
                } else {
                    replace = ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).replace('\n', ' ');
                }
                if (spannableStringBuilder.length() > 0) {
                    spannableStringBuilder.append((CharSequence) ", ");
                }
                int length = spannableStringBuilder.length();
                int length2 = replace.length() + length;
                spannableStringBuilder.append((CharSequence) replace);
                if (tLRPC$Dialog.unread_count > 0) {
                    spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM), 0, Theme.getColor(Theme.key_chats_nameArchived, this.resourcesProvider)), length, length2, 33);
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

    /* JADX WARN: Code restructure failed: missing block: B:295:0x061c, code lost:
        if (r2.post_messages == false) goto L833;
     */
    /* JADX WARN: Code restructure failed: missing block: B:947:0x12d7, code lost:
        if (r2 == null) goto L907;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1001:0x13fa  */
    /* JADX WARN: Removed duplicated region for block: B:1002:0x140c  */
    /* JADX WARN: Removed duplicated region for block: B:1005:0x142b  */
    /* JADX WARN: Removed duplicated region for block: B:1012:0x144a  */
    /* JADX WARN: Removed duplicated region for block: B:1016:0x1477  */
    /* JADX WARN: Removed duplicated region for block: B:1045:0x153d  */
    /* JADX WARN: Removed duplicated region for block: B:1068:0x15aa  */
    /* JADX WARN: Removed duplicated region for block: B:1071:0x15af A[Catch: Exception -> 0x169b, TryCatch #0 {Exception -> 0x169b, blocks: (B:1066:0x15a1, B:1069:0x15ab, B:1071:0x15af, B:1072:0x15b9, B:1074:0x15bd, B:1078:0x15d7, B:1079:0x15e0, B:1083:0x15f6, B:1085:0x15fc, B:1086:0x1608, B:1088:0x161f, B:1090:0x1625, B:1094:0x1636, B:1096:0x163a, B:1098:0x1678, B:1100:0x167c, B:1102:0x1685, B:1104:0x168f, B:1097:0x165b), top: B:1619:0x15a1 }] */
    /* JADX WARN: Removed duplicated region for block: B:1074:0x15bd A[Catch: Exception -> 0x169b, TryCatch #0 {Exception -> 0x169b, blocks: (B:1066:0x15a1, B:1069:0x15ab, B:1071:0x15af, B:1072:0x15b9, B:1074:0x15bd, B:1078:0x15d7, B:1079:0x15e0, B:1083:0x15f6, B:1085:0x15fc, B:1086:0x1608, B:1088:0x161f, B:1090:0x1625, B:1094:0x1636, B:1096:0x163a, B:1098:0x1678, B:1100:0x167c, B:1102:0x1685, B:1104:0x168f, B:1097:0x165b), top: B:1619:0x15a1 }] */
    /* JADX WARN: Removed duplicated region for block: B:1081:0x15f3  */
    /* JADX WARN: Removed duplicated region for block: B:1082:0x15f5  */
    /* JADX WARN: Removed duplicated region for block: B:1085:0x15fc A[Catch: Exception -> 0x169b, TryCatch #0 {Exception -> 0x169b, blocks: (B:1066:0x15a1, B:1069:0x15ab, B:1071:0x15af, B:1072:0x15b9, B:1074:0x15bd, B:1078:0x15d7, B:1079:0x15e0, B:1083:0x15f6, B:1085:0x15fc, B:1086:0x1608, B:1088:0x161f, B:1090:0x1625, B:1094:0x1636, B:1096:0x163a, B:1098:0x1678, B:1100:0x167c, B:1102:0x1685, B:1104:0x168f, B:1097:0x165b), top: B:1619:0x15a1 }] */
    /* JADX WARN: Removed duplicated region for block: B:1096:0x163a A[Catch: Exception -> 0x169b, TryCatch #0 {Exception -> 0x169b, blocks: (B:1066:0x15a1, B:1069:0x15ab, B:1071:0x15af, B:1072:0x15b9, B:1074:0x15bd, B:1078:0x15d7, B:1079:0x15e0, B:1083:0x15f6, B:1085:0x15fc, B:1086:0x1608, B:1088:0x161f, B:1090:0x1625, B:1094:0x1636, B:1096:0x163a, B:1098:0x1678, B:1100:0x167c, B:1102:0x1685, B:1104:0x168f, B:1097:0x165b), top: B:1619:0x15a1 }] */
    /* JADX WARN: Removed duplicated region for block: B:1097:0x165b A[Catch: Exception -> 0x169b, TryCatch #0 {Exception -> 0x169b, blocks: (B:1066:0x15a1, B:1069:0x15ab, B:1071:0x15af, B:1072:0x15b9, B:1074:0x15bd, B:1078:0x15d7, B:1079:0x15e0, B:1083:0x15f6, B:1085:0x15fc, B:1086:0x1608, B:1088:0x161f, B:1090:0x1625, B:1094:0x1636, B:1096:0x163a, B:1098:0x1678, B:1100:0x167c, B:1102:0x1685, B:1104:0x168f, B:1097:0x165b), top: B:1619:0x15a1 }] */
    /* JADX WARN: Removed duplicated region for block: B:1145:0x181c  */
    /* JADX WARN: Removed duplicated region for block: B:1146:0x183e  */
    /* JADX WARN: Removed duplicated region for block: B:1150:0x187f  */
    /* JADX WARN: Removed duplicated region for block: B:1167:0x18d3  */
    /* JADX WARN: Removed duplicated region for block: B:1168:0x18ea  */
    /* JADX WARN: Removed duplicated region for block: B:1171:0x18ff  */
    /* JADX WARN: Removed duplicated region for block: B:1185:0x193a  */
    /* JADX WARN: Removed duplicated region for block: B:1191:0x195f  */
    /* JADX WARN: Removed duplicated region for block: B:1195:0x1997  */
    /* JADX WARN: Removed duplicated region for block: B:1266:0x1b5b  */
    /* JADX WARN: Removed duplicated region for block: B:1288:0x1bb8  */
    /* JADX WARN: Removed duplicated region for block: B:1295:0x1bcc  */
    /* JADX WARN: Removed duplicated region for block: B:1303:0x1be4  */
    /* JADX WARN: Removed duplicated region for block: B:1304:0x1be7  */
    /* JADX WARN: Removed duplicated region for block: B:1308:0x1bf6  */
    /* JADX WARN: Removed duplicated region for block: B:1319:0x1c1b  */
    /* JADX WARN: Removed duplicated region for block: B:1369:0x1cdf  */
    /* JADX WARN: Removed duplicated region for block: B:1373:0x1d00 A[Catch: Exception -> 0x1d55, TryCatch #4 {Exception -> 0x1d55, blocks: (B:1371:0x1cf8, B:1373:0x1d00, B:1374:0x1d52), top: B:1626:0x1cf8 }] */
    /* JADX WARN: Removed duplicated region for block: B:1374:0x1d52 A[Catch: Exception -> 0x1d55, TRY_LEAVE, TryCatch #4 {Exception -> 0x1d55, blocks: (B:1371:0x1cf8, B:1373:0x1d00, B:1374:0x1d52), top: B:1626:0x1cf8 }] */
    /* JADX WARN: Removed duplicated region for block: B:1378:0x1d6b A[Catch: Exception -> 0x1dca, TryCatch #1 {Exception -> 0x1dca, blocks: (B:1376:0x1d65, B:1378:0x1d6b, B:1380:0x1d6f, B:1385:0x1d9f, B:1382:0x1d73, B:1384:0x1d79), top: B:1621:0x1d65 }] */
    /* JADX WARN: Removed duplicated region for block: B:1391:0x1dd2 A[Catch: Exception -> 0x1f56, TryCatch #3 {Exception -> 0x1f56, blocks: (B:1389:0x1dce, B:1391:0x1dd2, B:1393:0x1de4, B:1395:0x1dea, B:1397:0x1dee, B:1399:0x1df4, B:1401:0x1df8, B:1403:0x1dfc, B:1405:0x1e00, B:1407:0x1e04, B:1409:0x1e08, B:1412:0x1e15, B:1411:0x1e12, B:1413:0x1e18, B:1415:0x1e1c, B:1424:0x1e3b, B:1426:0x1e3f, B:1433:0x1e4f, B:1435:0x1e55, B:1437:0x1e59, B:1439:0x1e6c, B:1441:0x1e9c, B:1443:0x1ea0, B:1445:0x1ea4, B:1447:0x1ea9, B:1449:0x1eaf, B:1462:0x1ef1, B:1464:0x1ef5, B:1466:0x1f08, B:1468:0x1f0e, B:1469:0x1f23, B:1451:0x1eb3, B:1453:0x1eb9, B:1456:0x1ebf, B:1457:0x1ec6, B:1461:0x1edc, B:1446:0x1ea7, B:1440:0x1e8a, B:1428:0x1e43, B:1417:0x1e20, B:1419:0x1e26, B:1421:0x1e2a, B:1423:0x1e2f), top: B:1624:0x1dce }] */
    /* JADX WARN: Removed duplicated region for block: B:1415:0x1e1c A[Catch: Exception -> 0x1f56, TryCatch #3 {Exception -> 0x1f56, blocks: (B:1389:0x1dce, B:1391:0x1dd2, B:1393:0x1de4, B:1395:0x1dea, B:1397:0x1dee, B:1399:0x1df4, B:1401:0x1df8, B:1403:0x1dfc, B:1405:0x1e00, B:1407:0x1e04, B:1409:0x1e08, B:1412:0x1e15, B:1411:0x1e12, B:1413:0x1e18, B:1415:0x1e1c, B:1424:0x1e3b, B:1426:0x1e3f, B:1433:0x1e4f, B:1435:0x1e55, B:1437:0x1e59, B:1439:0x1e6c, B:1441:0x1e9c, B:1443:0x1ea0, B:1445:0x1ea4, B:1447:0x1ea9, B:1449:0x1eaf, B:1462:0x1ef1, B:1464:0x1ef5, B:1466:0x1f08, B:1468:0x1f0e, B:1469:0x1f23, B:1451:0x1eb3, B:1453:0x1eb9, B:1456:0x1ebf, B:1457:0x1ec6, B:1461:0x1edc, B:1446:0x1ea7, B:1440:0x1e8a, B:1428:0x1e43, B:1417:0x1e20, B:1419:0x1e26, B:1421:0x1e2a, B:1423:0x1e2f), top: B:1624:0x1dce }] */
    /* JADX WARN: Removed duplicated region for block: B:1443:0x1ea0 A[Catch: Exception -> 0x1f56, TryCatch #3 {Exception -> 0x1f56, blocks: (B:1389:0x1dce, B:1391:0x1dd2, B:1393:0x1de4, B:1395:0x1dea, B:1397:0x1dee, B:1399:0x1df4, B:1401:0x1df8, B:1403:0x1dfc, B:1405:0x1e00, B:1407:0x1e04, B:1409:0x1e08, B:1412:0x1e15, B:1411:0x1e12, B:1413:0x1e18, B:1415:0x1e1c, B:1424:0x1e3b, B:1426:0x1e3f, B:1433:0x1e4f, B:1435:0x1e55, B:1437:0x1e59, B:1439:0x1e6c, B:1441:0x1e9c, B:1443:0x1ea0, B:1445:0x1ea4, B:1447:0x1ea9, B:1449:0x1eaf, B:1462:0x1ef1, B:1464:0x1ef5, B:1466:0x1f08, B:1468:0x1f0e, B:1469:0x1f23, B:1451:0x1eb3, B:1453:0x1eb9, B:1456:0x1ebf, B:1457:0x1ec6, B:1461:0x1edc, B:1446:0x1ea7, B:1440:0x1e8a, B:1428:0x1e43, B:1417:0x1e20, B:1419:0x1e26, B:1421:0x1e2a, B:1423:0x1e2f), top: B:1624:0x1dce }] */
    /* JADX WARN: Removed duplicated region for block: B:1449:0x1eaf A[Catch: Exception -> 0x1f56, TryCatch #3 {Exception -> 0x1f56, blocks: (B:1389:0x1dce, B:1391:0x1dd2, B:1393:0x1de4, B:1395:0x1dea, B:1397:0x1dee, B:1399:0x1df4, B:1401:0x1df8, B:1403:0x1dfc, B:1405:0x1e00, B:1407:0x1e04, B:1409:0x1e08, B:1412:0x1e15, B:1411:0x1e12, B:1413:0x1e18, B:1415:0x1e1c, B:1424:0x1e3b, B:1426:0x1e3f, B:1433:0x1e4f, B:1435:0x1e55, B:1437:0x1e59, B:1439:0x1e6c, B:1441:0x1e9c, B:1443:0x1ea0, B:1445:0x1ea4, B:1447:0x1ea9, B:1449:0x1eaf, B:1462:0x1ef1, B:1464:0x1ef5, B:1466:0x1f08, B:1468:0x1f0e, B:1469:0x1f23, B:1451:0x1eb3, B:1453:0x1eb9, B:1456:0x1ebf, B:1457:0x1ec6, B:1461:0x1edc, B:1446:0x1ea7, B:1440:0x1e8a, B:1428:0x1e43, B:1417:0x1e20, B:1419:0x1e26, B:1421:0x1e2a, B:1423:0x1e2f), top: B:1624:0x1dce }] */
    /* JADX WARN: Removed duplicated region for block: B:1455:0x1ebd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1459:0x1ed7  */
    /* JADX WARN: Removed duplicated region for block: B:1460:0x1eda  */
    /* JADX WARN: Removed duplicated region for block: B:1479:0x1f72  */
    /* JADX WARN: Removed duplicated region for block: B:1556:0x217e  */
    /* JADX WARN: Removed duplicated region for block: B:1603:0x2262  */
    /* JADX WARN: Removed duplicated region for block: B:1614:0x229f  */
    /* JADX WARN: Removed duplicated region for block: B:1615:0x22a7  */
    /* JADX WARN: Removed duplicated region for block: B:1654:0x18cb A[EDGE_INSN: B:1654:0x18cb->B:1165:0x18cb ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:226:0x052c  */
    /* JADX WARN: Removed duplicated region for block: B:313:0x0645  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x06a9  */
    /* JADX WARN: Removed duplicated region for block: B:424:0x08e8  */
    /* JADX WARN: Removed duplicated region for block: B:621:0x0c12  */
    /* JADX WARN: Removed duplicated region for block: B:622:0x0c15  */
    /* JADX WARN: Removed duplicated region for block: B:755:0x0f46  */
    /* JADX WARN: Removed duplicated region for block: B:765:0x0fab  */
    /* JADX WARN: Removed duplicated region for block: B:813:0x109f  */
    /* JADX WARN: Removed duplicated region for block: B:814:0x10a7  */
    /* JADX WARN: Removed duplicated region for block: B:823:0x10c5  */
    /* JADX WARN: Removed duplicated region for block: B:855:0x1148  */
    /* JADX WARN: Removed duplicated region for block: B:856:0x114d  */
    /* JADX WARN: Removed duplicated region for block: B:859:0x1154  */
    /* JADX WARN: Removed duplicated region for block: B:860:0x1157  */
    /* JADX WARN: Removed duplicated region for block: B:913:0x1212  */
    /* JADX WARN: Removed duplicated region for block: B:922:0x1243  */
    /* JADX WARN: Removed duplicated region for block: B:926:0x1255  */
    /* JADX WARN: Removed duplicated region for block: B:927:0x125f  */
    /* JADX WARN: Removed duplicated region for block: B:937:0x1299  */
    /* JADX WARN: Removed duplicated region for block: B:939:0x12ac  */
    /* JADX WARN: Removed duplicated region for block: B:982:0x1363  */
    /* JADX WARN: Removed duplicated region for block: B:986:0x1375  */
    /* JADX WARN: Removed duplicated region for block: B:990:0x13b4  */
    /* JADX WARN: Removed duplicated region for block: B:993:0x13c1  */
    /* JADX WARN: Removed duplicated region for block: B:998:0x13f5  */
    /* JADX WARN: Type inference failed for: r0v169, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r11v38, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r1v142 */
    /* JADX WARN: Type inference failed for: r1v182 */
    /* JADX WARN: Type inference failed for: r1v183, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r1v194 */
    /* JADX WARN: Type inference failed for: r1v232 */
    /* JADX WARN: Type inference failed for: r1v31 */
    /* JADX WARN: Type inference failed for: r1v32 */
    /* JADX WARN: Type inference failed for: r1v332 */
    /* JADX WARN: Type inference failed for: r1v45 */
    /* JADX WARN: Type inference failed for: r1v48, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r1v63, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r1v73, types: [android.text.SpannableStringBuilder, java.lang.CharSequence, android.text.Spannable] */
    /* JADX WARN: Type inference failed for: r1v81, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r37v0, types: [android.view.View, org.telegram.ui.Cells.DialogCell, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r3v175, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r3v176, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r4v127, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r4v128, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r4v129 */
    /* JADX WARN: Type inference failed for: r5v93, types: [java.lang.CharSequence] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildLayout() {
        int i;
        boolean z;
        TLRPC$DraftMessage tLRPC$DraftMessage;
        TLRPC$DraftMessage tLRPC$DraftMessage2;
        TLRPC$InputReplyTo tLRPC$InputReplyTo;
        TLRPC$DraftMessage tLRPC$DraftMessage3;
        SpannableStringBuilder spannableStringBuilder;
        boolean z2;
        int i2;
        String string;
        String str;
        ArrayList<TLRPC$MessageEntity> arrayList;
        SpannableStringBuilder spannableStringBuilder2;
        boolean z3;
        boolean z4;
        CharSequence charSequence;
        SpannableStringBuilder spannableStringBuilder3;
        TLRPC$Chat chat;
        ?? r1;
        boolean z5;
        TLRPC$Chat tLRPC$Chat;
        CharSequence replaceNewLines;
        CharSequence highlightText;
        String str2;
        String str3;
        String str4;
        boolean isChannelAndNotMegaGroup;
        String formatPluralString;
        String str5;
        SpannableStringBuilder append;
        int i3;
        SpannableStringBuilder replaceEmoji;
        String str6;
        String str7;
        CharSequence highlightText2;
        ForegroundColorSpanThemable foregroundColorSpanThemable;
        TLRPC$User tLRPC$User;
        MessageObject messageObject;
        TLRPC$User tLRPC$User2;
        String string2;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList<TLRPC$MessagePeerReaction> arrayList2;
        CharSequence charSequence2;
        CharSequence string3;
        TLRPC$DraftMessage tLRPC$DraftMessage4;
        MessageObject messageObject2;
        String stringForMessageListDate;
        MessageObject messageObject3;
        String str8;
        String str9;
        MessagesController messagesController;
        CharSequence charSequence3;
        CharSequence userName;
        boolean z6;
        int i4;
        String str10;
        String str11;
        SpannableStringBuilder spannableStringBuilder4;
        int i5;
        MessageObject messageObject4;
        int i6;
        int i7;
        int dp;
        int dp2;
        int dp3;
        int i8;
        int i9;
        ImageReceiver[] imageReceiverArr;
        DialogCellTags dialogCellTags;
        int max;
        ?? highlightText3;
        int i10;
        int lineCount;
        int lineCount2;
        int lineCount3;
        StaticLayout staticLayout;
        float primaryHorizontal;
        float primaryHorizontal2;
        int i11;
        int lineCount4;
        int lineCount5;
        int lineCount6;
        String str12;
        Layout.Alignment alignment;
        int i12;
        String str13;
        Object[] spans;
        CharSequence replaceTwoNewLinesToOne;
        CharSequence highlightText4;
        DialogCellTags dialogCellTags2;
        int dp4;
        int dp5;
        DialogCellTags dialogCellTags3;
        int dp6;
        CharSequence highlightText5;
        String str14;
        String str15;
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
            TextPaint[] textPaintArr = Theme.dialogs_messagePaint;
            TextPaint textPaint = textPaintArr[1];
            TextPaint textPaint2 = textPaintArr[1];
            int color = Theme.getColor(Theme.key_chats_message_threeLines, this.resourcesProvider);
            textPaint2.linkColor = color;
            textPaint.setColor(color);
            this.paintIndex = 1;
            this.thumbSize = 18;
        } else {
            Theme.dialogs_namePaint[0].setTextSize(AndroidUtilities.dp(17.0f));
            Theme.dialogs_nameEncryptedPaint[0].setTextSize(AndroidUtilities.dp(17.0f));
            Theme.dialogs_messagePaint[0].setTextSize(AndroidUtilities.dp(16.0f));
            Theme.dialogs_messagePrintingPaint[0].setTextSize(AndroidUtilities.dp(16.0f));
            TextPaint[] textPaintArr2 = Theme.dialogs_messagePaint;
            TextPaint textPaint3 = textPaintArr2[0];
            TextPaint textPaint4 = textPaintArr2[0];
            int color2 = Theme.getColor(Theme.key_chats_message, this.resourcesProvider);
            textPaint4.linkColor = color2;
            textPaint3.setColor(color2);
            this.paintIndex = 0;
            this.thumbSize = 19;
        }
        this.currentDialogFolderDialogsCount = 0;
        SpannableStringBuilder spannableStringBuilder5 = null;
        CharSequence printingString = (isForumCell() || !(this.isDialogCell || this.isTopic)) ? null : MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, getTopicId(), true);
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
        boolean z7 = (UserObject.isUserSelf(this.user) || this.useMeForMyMessages) ? false : true;
        this.printingStringType = -1;
        if (!isForumCell()) {
            this.buttonLayout = null;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || this.currentDialogFolderId != 0 || isForumCell() || hasTags()) {
                this.hasNameInMessage = true;
                i = 1;
            } else {
                this.hasNameInMessage = false;
                i = 2;
            }
        } else if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || this.currentDialogFolderId != 0 || isForumCell() || hasTags()) {
            i = 3;
            this.hasNameInMessage = true;
        } else {
            this.hasNameInMessage = false;
            i = 4;
        }
        MessageObject messageObject5 = this.message;
        if (messageObject5 != null) {
            messageObject5.updateTranslation();
        }
        MessageObject messageObject6 = this.message;
        CharSequence charSequence4 = messageObject6 != null ? messageObject6.messageText : null;
        boolean z8 = charSequence4 instanceof Spannable;
        SpannableStringBuilder spannableStringBuilder6 = charSequence4;
        if (z8) {
            SpannableStringBuilder spannableStringBuilder7 = new SpannableStringBuilder(charSequence4);
            for (URLSpanNoUnderlineBold uRLSpanNoUnderlineBold : (URLSpanNoUnderlineBold[]) spannableStringBuilder7.getSpans(0, spannableStringBuilder7.length(), URLSpanNoUnderlineBold.class)) {
                spannableStringBuilder7.removeSpan(uRLSpanNoUnderlineBold);
            }
            for (URLSpanNoUnderline uRLSpanNoUnderline : (URLSpanNoUnderline[]) spannableStringBuilder7.getSpans(0, spannableStringBuilder7.length(), URLSpanNoUnderline.class)) {
                spannableStringBuilder7.removeSpan(uRLSpanNoUnderline);
            }
            spannableStringBuilder6 = spannableStringBuilder7;
        }
        this.lastMessageString = spannableStringBuilder6;
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
                str14 = LocaleController.getString("FromYou", R.string.FromYou);
                CustomDialog customDialog3 = this.customDialog;
                if (customDialog3.isMedia) {
                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                    formatInternal = formatInternal(i, this.message.messageText, null);
                    formatInternal.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage, this.resourcesProvider), 0, formatInternal.length(), 33);
                } else {
                    String str16 = customDialog3.message;
                    if (str16.length() > 150) {
                        str16 = str16.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                    }
                    if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                        formatInternal = formatInternal(i, str16, str14);
                    } else {
                        formatInternal = formatInternal(i, str16.replace('\n', ' '), str14);
                    }
                }
                charSequence3 = Emoji.replaceEmoji((CharSequence) formatInternal, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                z6 = false;
            } else {
                charSequence3 = customDialog2.message;
                if (customDialog2.isMedia) {
                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                }
                str14 = null;
                z6 = true;
            }
            String stringForMessageListDate2 = LocaleController.stringForMessageListDate(this.customDialog.date);
            int i13 = this.customDialog.unread_count;
            if (i13 != 0) {
                this.drawCount = true;
                str15 = String.format("%d", Integer.valueOf(i13));
            } else {
                this.drawCount = false;
                str15 = null;
            }
            CustomDialog customDialog4 = this.customDialog;
            int i14 = customDialog4.sent;
            if (i14 == 0) {
                this.drawClock = true;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            } else if (i14 == 2) {
                this.drawCheck1 = true;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else if (i14 == 1) {
                this.drawCheck1 = false;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else {
                this.drawClock = false;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            }
            this.drawError = false;
            str10 = str14;
            str11 = customDialog4.name;
            stringForMessageListDate = stringForMessageListDate2;
            str8 = str15;
            str9 = null;
            spannableStringBuilder4 = "";
            z4 = true;
            i4 = -1;
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
                TLRPC$Chat tLRPC$Chat2 = this.chat;
                if (tLRPC$Chat2 != null) {
                    if (tLRPC$Chat2.scam) {
                        this.drawScam = 1;
                        Theme.dialogs_scamDrawable.checkText();
                    } else if (tLRPC$Chat2.fake) {
                        this.drawScam = 2;
                        Theme.dialogs_fakeDrawable.checkText();
                    } else if (DialogObject.getEmojiStatusDocumentId(tLRPC$Chat2.emoji_status) != 0) {
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
                            }
                        }
                        z = false;
                        this.drawPremium = z;
                        if (z) {
                        }
                    }
                }
            }
            int i15 = this.lastMessageDate;
            if (i15 == 0 && (messageObject4 = this.message) != null) {
                i15 = messageObject4.messageOwner.date;
            }
            if (this.isTopic) {
                boolean z9 = MediaDataController.getInstance(this.currentAccount).getDraftVoice(this.currentDialogId, (long) getTopicId()) != null;
                this.draftVoice = z9;
                TLRPC$DraftMessage draft = !z9 ? MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, getTopicId()) : null;
                this.draftMessage = draft;
                if (draft != null && TextUtils.isEmpty(draft.message)) {
                    this.draftMessage = null;
                }
            } else if (this.isDialogCell || this.isSavedDialogCell) {
                boolean z10 = MediaDataController.getInstance(this.currentAccount).getDraftVoice(this.currentDialogId, (long) getTopicId()) != null;
                this.draftVoice = z10;
                this.draftMessage = !z10 ? MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, 0L) : null;
            } else {
                this.draftVoice = false;
                this.draftMessage = null;
            }
            boolean z11 = this.draftVoice;
            if ((!z11 && this.draftMessage == null) || ((z11 || (tLRPC$DraftMessage2 = this.draftMessage) == null || !TextUtils.isEmpty(tLRPC$DraftMessage2.message) || ((tLRPC$InputReplyTo = this.draftMessage.reply_to) != null && tLRPC$InputReplyTo.reply_to_msg_id != 0)) && ((tLRPC$DraftMessage = this.draftMessage) == null || i15 <= tLRPC$DraftMessage.date || this.unreadCount == 0))) {
                if (ChatObject.isChannel(this.chat)) {
                    TLRPC$Chat tLRPC$Chat3 = this.chat;
                    if (!tLRPC$Chat3.megagroup) {
                        if (!tLRPC$Chat3.creator) {
                            TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = tLRPC$Chat3.admin_rights;
                            if (tLRPC$TL_chatAdminRights != null) {
                            }
                        }
                    }
                }
                TLRPC$Chat tLRPC$Chat4 = this.chat;
                if ((tLRPC$Chat4 == null || (!tLRPC$Chat4.left && !tLRPC$Chat4.kicked)) && !this.forbidDraft && (!ChatObject.isForum(tLRPC$Chat4) || this.isTopic)) {
                    tLRPC$DraftMessage3 = null;
                    if (!isForumCell()) {
                        this.draftMessage = tLRPC$DraftMessage3;
                        this.draftVoice = false;
                        this.needEmoji = true;
                        updateMessageThumbs();
                        string = getMessageNameString();
                        charSequence = formatTopicsNames();
                        MessageObject messageObject7 = this.message;
                        String messageStringFormatted = this.message != null ? getMessageStringFormatted(i, messageObject7 != null ? MessagesController.getRestrictionReason(messageObject7.messageOwner.restriction_reason) : null, string, true) : "";
                        if (this.applyName && messageStringFormatted.length() >= 0 && string != null) {
                            messageStringFormatted = SpannableStringBuilder.valueOf(messageStringFormatted);
                            messageStringFormatted.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_name, this.resourcesProvider), 0, Math.min(messageStringFormatted.length(), string.length() + 1), 0);
                        }
                        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                        spannableStringBuilder5 = messageStringFormatted;
                        spannableStringBuilder3 = "";
                        z3 = true;
                        z4 = true;
                        i2 = -1;
                    } else {
                        if (printingString != null) {
                            this.lastPrintString = printingString;
                            int intValue = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.currentDialogId, getTopicId()).intValue();
                            this.printingStringType = intValue;
                            StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(intValue);
                            int intrinsicWidth = chatStatusDrawable != null ? chatStatusDrawable.getIntrinsicWidth() + AndroidUtilities.dp(3.0f) : 0;
                            SpannableStringBuilder spannableStringBuilder8 = new SpannableStringBuilder();
                            CharSequence replace = TextUtils.replace(printingString, new String[]{"..."}, new String[]{""});
                            int indexOf = this.printingStringType == 5 ? replace.toString().indexOf("**oo**") : -1;
                            if (indexOf >= 0) {
                                spannableStringBuilder8.append(replace).setSpan(new FixedWidthSpan(Theme.getChatStatusDrawable(this.printingStringType).getIntrinsicWidth()), indexOf, indexOf + 6, 0);
                            } else {
                                spannableStringBuilder8.append((CharSequence) " ").append(replace).setSpan(new FixedWidthSpan(intrinsicWidth), 0, 1, 0);
                            }
                            spannableStringBuilder = spannableStringBuilder8;
                            i2 = indexOf;
                            z2 = false;
                        } else {
                            this.lastPrintString = null;
                            this.printingStringType = -1;
                            spannableStringBuilder = "";
                            z2 = true;
                            i2 = -1;
                        }
                        if (this.draftVoice || this.draftMessage != null) {
                            string = LocaleController.getString("Draft", R.string.Draft);
                            TLRPC$DraftMessage tLRPC$DraftMessage5 = this.draftMessage;
                            if (tLRPC$DraftMessage5 != null && TextUtils.isEmpty(tLRPC$DraftMessage5.message)) {
                                if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                                    spannableStringBuilder3 = spannableStringBuilder;
                                    charSequence = "";
                                    z3 = false;
                                    z4 = true;
                                    spannableStringBuilder5 = null;
                                } else {
                                    SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(string);
                                    valueOf.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_draft, this.resourcesProvider), 0, string.length(), 33);
                                    spannableStringBuilder2 = valueOf;
                                }
                            } else {
                                if (this.draftVoice) {
                                    str = LocaleController.getString(R.string.AttachAudio);
                                } else {
                                    TLRPC$DraftMessage tLRPC$DraftMessage6 = this.draftMessage;
                                    if (tLRPC$DraftMessage6 != null) {
                                        str = tLRPC$DraftMessage6.message;
                                        if (str.length() > 150) {
                                            str = str.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                        }
                                    } else {
                                        str = "";
                                    }
                                }
                                SpannableStringBuilder spannableStringBuilder9 = new SpannableStringBuilder(str);
                                TLRPC$DraftMessage tLRPC$DraftMessage7 = this.draftMessage;
                                if (tLRPC$DraftMessage7 != null) {
                                    MediaDataController.addTextStyleRuns(tLRPC$DraftMessage7, spannableStringBuilder9, 264);
                                    TLRPC$DraftMessage tLRPC$DraftMessage8 = this.draftMessage;
                                    if (tLRPC$DraftMessage8 != null && (arrayList = tLRPC$DraftMessage8.entities) != null) {
                                        TextPaint textPaint5 = this.currentMessagePaint;
                                        MediaDataController.addAnimatedEmojiSpans(arrayList, spannableStringBuilder9, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                    }
                                } else if (this.draftVoice) {
                                    spannableStringBuilder9.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_actionMessage, this.resourcesProvider), 0, spannableStringBuilder9.length(), 33);
                                }
                                SpannableStringBuilder formatInternal2 = formatInternal(i, AndroidUtilities.replaceNewLines(spannableStringBuilder9), string);
                                if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || hasTags()) {
                                    formatInternal2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_draft, this.resourcesProvider), 0, string.length() + 1, 33);
                                }
                                spannableStringBuilder2 = Emoji.replaceEmoji((CharSequence) formatInternal2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                            }
                            z3 = false;
                            z4 = true;
                            spannableStringBuilder5 = null;
                            SpannableStringBuilder spannableStringBuilder10 = spannableStringBuilder;
                            charSequence = spannableStringBuilder2;
                            spannableStringBuilder3 = spannableStringBuilder10;
                        } else {
                            if (this.clearingDialog) {
                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                string3 = LocaleController.getString("HistoryCleared", R.string.HistoryCleared);
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
                                                string3 = LocaleController.getString("EncryptionProcessing", R.string.EncryptionProcessing);
                                            } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatWaiting) {
                                                string3 = LocaleController.formatString("AwaitingEncryption", R.string.AwaitingEncryption, UserObject.getFirstName(this.user));
                                            } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatDiscarded) {
                                                string3 = LocaleController.getString("EncryptionRejected", R.string.EncryptionRejected);
                                            } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) {
                                                if (tLRPC$EncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                    string3 = LocaleController.formatString("EncryptedChatStartedOutgoing", R.string.EncryptedChatStartedOutgoing, UserObject.getFirstName(this.user));
                                                } else {
                                                    string3 = LocaleController.getString("EncryptedChatStartedIncoming", R.string.EncryptedChatStartedIncoming);
                                                }
                                            }
                                        } else if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                            DialogsActivity dialogsActivity = this.parentFragment;
                                            z3 = z2;
                                            spannableStringBuilder3 = spannableStringBuilder;
                                            charSequence = LocaleController.getString((dialogsActivity == null || !dialogsActivity.isQuote) ? R.string.SavedMessagesInfo : R.string.SavedMessagesInfoQuote);
                                            string = null;
                                            z7 = false;
                                            z4 = false;
                                            spannableStringBuilder5 = null;
                                        }
                                        z3 = z2;
                                        spannableStringBuilder3 = spannableStringBuilder;
                                        charSequence = "";
                                        string = null;
                                        z4 = true;
                                        spannableStringBuilder5 = null;
                                    }
                                } else {
                                    String restrictionReason = MessagesController.getRestrictionReason(messageObject8.messageOwner.restriction_reason);
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
                                            long j3 = tLRPC$MessagePeerReaction.peer_id.user_id;
                                            if (j3 != 0 && j3 != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                ReactionsLayoutInBubble.VisibleReaction fromTLReaction = ReactionsLayoutInBubble.VisibleReaction.fromTLReaction(tLRPC$MessagePeerReaction.reaction);
                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                String str17 = fromTLReaction.emojicon;
                                                if (str17 != null) {
                                                    charSequence2 = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, str17);
                                                } else {
                                                    String formatString = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, "**reaction**");
                                                    int indexOf2 = formatString.indexOf("**reaction**");
                                                    SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder(formatString.replace("**reaction**", "d"));
                                                    long j4 = fromTLReaction.documentId;
                                                    TextPaint textPaint6 = this.currentMessagePaint;
                                                    spannableStringBuilder11.setSpan(new AnimatedEmojiSpan(j4, textPaint6 == null ? null : textPaint6.getFontMetricsInt()), indexOf2, indexOf2 + 1, 0);
                                                    charSequence2 = spannableStringBuilder11;
                                                }
                                                z5 = true;
                                                r1 = charSequence2;
                                                if (!z5) {
                                                    int i16 = this.dialogsType;
                                                    if (i16 == 2) {
                                                        TLRPC$Chat tLRPC$Chat5 = this.chat;
                                                        if (tLRPC$Chat5 != null) {
                                                            if (ChatObject.isChannel(tLRPC$Chat5)) {
                                                                TLRPC$Chat tLRPC$Chat6 = this.chat;
                                                                if (!tLRPC$Chat6.megagroup) {
                                                                    int i17 = tLRPC$Chat6.participants_count;
                                                                    if (i17 != 0) {
                                                                        string2 = LocaleController.formatPluralStringComma("Subscribers", i17);
                                                                    } else if (!ChatObject.isPublic(tLRPC$Chat6)) {
                                                                        string2 = LocaleController.getString("ChannelPrivate", R.string.ChannelPrivate).toLowerCase();
                                                                    } else {
                                                                        string2 = LocaleController.getString("ChannelPublic", R.string.ChannelPublic).toLowerCase();
                                                                    }
                                                                }
                                                            }
                                                            TLRPC$Chat tLRPC$Chat7 = this.chat;
                                                            int i18 = tLRPC$Chat7.participants_count;
                                                            if (i18 != 0) {
                                                                string2 = LocaleController.formatPluralStringComma("Members", i18);
                                                            } else if (tLRPC$Chat7.has_geo) {
                                                                string2 = LocaleController.getString("MegaLocation", R.string.MegaLocation);
                                                            } else if (!ChatObject.isPublic(tLRPC$Chat7)) {
                                                                string2 = LocaleController.getString("MegaPrivate", R.string.MegaPrivate).toLowerCase();
                                                            } else {
                                                                string2 = LocaleController.getString("MegaPublic", R.string.MegaPublic).toLowerCase();
                                                            }
                                                        } else {
                                                            string2 = "";
                                                        }
                                                        this.drawCount2 = false;
                                                    } else if (i16 == 3 && UserObject.isUserSelf(this.user)) {
                                                        DialogsActivity dialogsActivity2 = this.parentFragment;
                                                        string2 = LocaleController.getString((dialogsActivity2 == null || !dialogsActivity2.isQuote) ? R.string.SavedMessagesInfo : R.string.SavedMessagesInfoQuote);
                                                    } else if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout && this.currentDialogFolderId != 0) {
                                                        r1 = formatArchivedDialogNames();
                                                        z2 = false;
                                                    } else {
                                                        MessageObject messageObject9 = this.message;
                                                        if ((messageObject9.messageOwner instanceof TLRPC$TL_messageService) && (!MessageObject.isTopicActionMessage(messageObject9) || (this.message.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate))) {
                                                            if (ChatObject.isChannelAndNotMegaGroup(this.chat) && (this.message.messageOwner.action instanceof TLRPC$TL_messageActionChannelMigrateFrom)) {
                                                                spannableStringBuilder6 = "";
                                                                z7 = false;
                                                            }
                                                            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                            if (this.message.type == 21) {
                                                                updateMessageThumbs();
                                                                r1 = applyThumbs(spannableStringBuilder6);
                                                            } else {
                                                                r1 = spannableStringBuilder6;
                                                            }
                                                        } else {
                                                            this.needEmoji = true;
                                                            updateMessageThumbs();
                                                            String messageNameString = (this.isSavedDialog || (tLRPC$User2 = this.user) == null || !tLRPC$User2.self || this.message.isOutOwner()) ? null : getMessageNameString();
                                                            if ((this.isSavedDialog && (tLRPC$User = this.user) != null && !tLRPC$User.self && (messageObject = this.message) != null && messageObject.isOutOwner()) || messageNameString != null || ((tLRPC$Chat = this.chat) != null && tLRPC$Chat.id > 0 && chat == null && ((!ChatObject.isChannel(tLRPC$Chat) || ChatObject.isMegagroup(this.chat)) && !ForumUtilities.isTopicCreateMessage(this.message)))) {
                                                                if (messageNameString == null) {
                                                                    messageNameString = getMessageNameString();
                                                                }
                                                                TLRPC$Chat tLRPC$Chat8 = this.chat;
                                                                if (tLRPC$Chat8 != null && tLRPC$Chat8.forum && !this.isTopic && !this.useFromUserAsAvatar) {
                                                                    CharSequence topicIconName = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, this.currentMessagePaint);
                                                                    if (!TextUtils.isEmpty(topicIconName)) {
                                                                        SpannableStringBuilder spannableStringBuilder12 = new SpannableStringBuilder("-");
                                                                        ColoredImageSpan coloredImageSpan = new ColoredImageSpan(ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_mini_forumarrow).mutate());
                                                                        coloredImageSpan.setColorKey((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? -1 : Theme.key_chats_nameMessage);
                                                                        spannableStringBuilder12.setSpan(coloredImageSpan, 0, 1, 0);
                                                                        ?? spannableStringBuilder13 = new SpannableStringBuilder();
                                                                        spannableStringBuilder13.append(messageNameString).append((CharSequence) spannableStringBuilder12).append(topicIconName);
                                                                        messageNameString = spannableStringBuilder13;
                                                                    }
                                                                }
                                                                SpannableStringBuilder messageStringFormatted2 = getMessageStringFormatted(i, restrictionReason, messageNameString, false);
                                                                if (this.useFromUserAsAvatar || ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && (this.currentDialogFolderId == 0 || messageStringFormatted2.length() <= 0))) {
                                                                    i3 = 0;
                                                                } else {
                                                                    try {
                                                                        foregroundColorSpanThemable = new ForegroundColorSpanThemable(Theme.key_chats_nameMessage, this.resourcesProvider);
                                                                        i3 = messageNameString.length() + 1;
                                                                    } catch (Exception e) {
                                                                        e = e;
                                                                        i3 = 0;
                                                                    }
                                                                    try {
                                                                        messageStringFormatted2.setSpan(foregroundColorSpanThemable, 0, i3, 33);
                                                                    } catch (Exception e2) {
                                                                        e = e2;
                                                                        FileLog.e(e);
                                                                        replaceEmoji = Emoji.replaceEmoji((CharSequence) messageStringFormatted2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                        if (this.message.hasHighlightedWords()) {
                                                                            replaceEmoji = highlightText2;
                                                                        }
                                                                        if (this.thumbsCount > 0) {
                                                                        }
                                                                        str6 = messageNameString;
                                                                        z4 = true;
                                                                        str7 = replaceEmoji;
                                                                        z2 = false;
                                                                        String str18 = str6;
                                                                        if (this.currentDialogFolderId != 0) {
                                                                        }
                                                                        spannableStringBuilder5 = null;
                                                                        String str19 = str18;
                                                                        z3 = z2;
                                                                        string = str19;
                                                                        SpannableStringBuilder spannableStringBuilder14 = spannableStringBuilder;
                                                                        charSequence = str7;
                                                                        spannableStringBuilder3 = spannableStringBuilder14;
                                                                        if (this.draftMessage != null) {
                                                                        }
                                                                        messageObject3 = this.message;
                                                                        if (messageObject3 != null) {
                                                                        }
                                                                        this.drawCheck1 = false;
                                                                        this.drawCheck2 = false;
                                                                        this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                                                                        this.drawCount = false;
                                                                        this.drawMention = false;
                                                                        this.drawReactionMention = false;
                                                                        this.drawError = false;
                                                                        str8 = null;
                                                                        str9 = null;
                                                                        this.promoDialog = false;
                                                                        messagesController = MessagesController.getInstance(this.currentAccount);
                                                                        if (this.dialogsType == 0) {
                                                                        }
                                                                        charSequence3 = charSequence;
                                                                        if (this.currentDialogFolderId != 0) {
                                                                        }
                                                                        z6 = z3;
                                                                        i4 = i2;
                                                                        SpannableStringBuilder spannableStringBuilder15 = spannableStringBuilder3;
                                                                        str10 = string;
                                                                        str11 = userName;
                                                                        spannableStringBuilder4 = spannableStringBuilder15;
                                                                        if (!z4) {
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
                                                                        if (str11 instanceof String) {
                                                                        }
                                                                        if (this.nameLayoutEllipsizeByGradient) {
                                                                        }
                                                                        float f = dp6;
                                                                        this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(str11.toString()) <= f;
                                                                        if (!this.twoLinesForName) {
                                                                        }
                                                                        CharSequence replaceEmoji2 = Emoji.replaceEmoji(str11, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
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
                                                                        i8 = measuredWidth;
                                                                        this.storyParams.originalAvatarRect.set(dp2, dp, dp2 + AndroidUtilities.dp(56.0f), dp + AndroidUtilities.dp(56.0f));
                                                                        i9 = 0;
                                                                        while (true) {
                                                                            imageReceiverArr = this.thumbImage;
                                                                            if (i9 < imageReceiverArr.length) {
                                                                            }
                                                                            imageReceiverArr[i9].setImageCoords(((this.thumbSize + 2) * i9) + dp3, ((AndroidUtilities.dp(31.0f) + dp) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0)) - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout || (dialogCellTags = this.tags) == null || dialogCellTags.isEmpty()) ? 0 : AndroidUtilities.dp(9.0f)), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                                                                            i9++;
                                                                            dp = dp;
                                                                        }
                                                                        int i19 = dp;
                                                                        int i20 = i8;
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
                                                                            if (z6) {
                                                                            }
                                                                            max = Math.max(AndroidUtilities.dp(12.0f), i20);
                                                                            this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
                                                                            if (!this.useForceThreeLines) {
                                                                                this.buttonTop -= AndroidUtilities.dp(!isForumCell() ? 10.0f : 12.0f);
                                                                                if (!isForumCell()) {
                                                                                }
                                                                                if (this.twoLinesForName) {
                                                                                }
                                                                                this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack2, this.messageNameLayout);
                                                                                this.buttonCreated = false;
                                                                                if (TextUtils.isEmpty(spannableStringBuilder5)) {
                                                                                }
                                                                                this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack3, this.buttonLayout);
                                                                                if (!TextUtils.isEmpty(spannableStringBuilder4)) {
                                                                                }
                                                                                if (charSequence3 instanceof Spannable) {
                                                                                }
                                                                                if (!this.useForceThreeLines) {
                                                                                }
                                                                                this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                                                                                str12 = str10;
                                                                                str10 = null;
                                                                                alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                                                                                if (!this.useForceThreeLines) {
                                                                                }
                                                                                if (this.thumbsCount > 0) {
                                                                                    max += AndroidUtilities.dp(5.0f);
                                                                                }
                                                                                this.messageLayout = StaticLayoutEx.createStaticLayout(str12, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, str10 == null ? 1 : 2);
                                                                                i10 = max;
                                                                                this.spoilersPool.addAll(this.spoilers);
                                                                                this.spoilers.clear();
                                                                                SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                                                                                this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack, this.messageLayout);
                                                                                if (LocaleController.isRTL) {
                                                                                }
                                                                                staticLayout = this.typingLayout;
                                                                                if (staticLayout != null) {
                                                                                }
                                                                                updateThumbsPosition();
                                                                            }
                                                                            this.buttonTop -= AndroidUtilities.dp(!isForumCell() ? 10.0f : 12.0f);
                                                                            if (!isForumCell()) {
                                                                            }
                                                                            if (this.twoLinesForName) {
                                                                            }
                                                                            this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack2, this.messageNameLayout);
                                                                            this.buttonCreated = false;
                                                                            if (TextUtils.isEmpty(spannableStringBuilder5)) {
                                                                            }
                                                                            this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack3, this.buttonLayout);
                                                                            if (!TextUtils.isEmpty(spannableStringBuilder4)) {
                                                                            }
                                                                            if (charSequence3 instanceof Spannable) {
                                                                            }
                                                                            if (!this.useForceThreeLines) {
                                                                            }
                                                                            this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                                                                            str12 = str10;
                                                                            str10 = null;
                                                                            alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                                                                            if (!this.useForceThreeLines) {
                                                                            }
                                                                            if (this.thumbsCount > 0) {
                                                                            }
                                                                            this.messageLayout = StaticLayoutEx.createStaticLayout(str12, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, str10 == null ? 1 : 2);
                                                                            i10 = max;
                                                                            this.spoilersPool.addAll(this.spoilers);
                                                                            this.spoilers.clear();
                                                                            SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                                                                            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack, this.messageLayout);
                                                                            if (LocaleController.isRTL) {
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
                                                                        if (z6) {
                                                                        }
                                                                        max = Math.max(AndroidUtilities.dp(12.0f), i20);
                                                                        this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
                                                                        if (!this.useForceThreeLines) {
                                                                        }
                                                                        this.buttonTop -= AndroidUtilities.dp(!isForumCell() ? 10.0f : 12.0f);
                                                                        if (!isForumCell()) {
                                                                        }
                                                                        if (this.twoLinesForName) {
                                                                        }
                                                                        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack2, this.messageNameLayout);
                                                                        this.buttonCreated = false;
                                                                        if (TextUtils.isEmpty(spannableStringBuilder5)) {
                                                                        }
                                                                        this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack3, this.buttonLayout);
                                                                        if (!TextUtils.isEmpty(spannableStringBuilder4)) {
                                                                        }
                                                                        if (charSequence3 instanceof Spannable) {
                                                                        }
                                                                        if (!this.useForceThreeLines) {
                                                                        }
                                                                        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                                                                        str12 = str10;
                                                                        str10 = null;
                                                                        alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
                                                                        if (!this.useForceThreeLines) {
                                                                        }
                                                                        if (this.thumbsCount > 0) {
                                                                        }
                                                                        this.messageLayout = StaticLayoutEx.createStaticLayout(str12, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, str10 == null ? 1 : 2);
                                                                        i10 = max;
                                                                        this.spoilersPool.addAll(this.spoilers);
                                                                        this.spoilers.clear();
                                                                        SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
                                                                        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack, this.messageLayout);
                                                                        if (LocaleController.isRTL) {
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
                                                                    boolean z12 = replaceEmoji instanceof SpannableStringBuilder;
                                                                    replaceEmoji = replaceEmoji;
                                                                    if (!z12) {
                                                                        replaceEmoji = new SpannableStringBuilder(replaceEmoji);
                                                                    }
                                                                    SpannableStringBuilder spannableStringBuilder16 = (SpannableStringBuilder) replaceEmoji;
                                                                    if (i3 >= spannableStringBuilder16.length()) {
                                                                        spannableStringBuilder16.append((CharSequence) " ");
                                                                        spannableStringBuilder16.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5)), spannableStringBuilder16.length() - 1, spannableStringBuilder16.length(), 33);
                                                                    } else {
                                                                        spannableStringBuilder16.insert(i3, (CharSequence) " ");
                                                                        spannableStringBuilder16.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5)), i3, i3 + 1, 33);
                                                                    }
                                                                }
                                                                str6 = messageNameString;
                                                                z4 = true;
                                                                str7 = replaceEmoji;
                                                                z2 = false;
                                                                String str182 = str6;
                                                                if (this.currentDialogFolderId != 0) {
                                                                    str182 = formatArchivedDialogNames();
                                                                }
                                                                spannableStringBuilder5 = null;
                                                                String str192 = str182;
                                                                z3 = z2;
                                                                string = str192;
                                                                SpannableStringBuilder spannableStringBuilder142 = spannableStringBuilder;
                                                                charSequence = str7;
                                                                spannableStringBuilder3 = spannableStringBuilder142;
                                                            } else {
                                                                boolean isEmpty = TextUtils.isEmpty(restrictionReason);
                                                                SpannableStringBuilder spannableStringBuilder17 = restrictionReason;
                                                                if (isEmpty) {
                                                                    if (MessageObject.isTopicActionMessage(this.message)) {
                                                                        MessageObject messageObject11 = this.message;
                                                                        ?? r4 = messageObject11.messageTextShort;
                                                                        if (r4 == null || ((messageObject11.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                                                                            r4 = messageObject11.messageText;
                                                                        }
                                                                        String str20 = r4;
                                                                        spannableStringBuilder17 = str20;
                                                                        if (messageObject11.topicIconDrawable[0] instanceof ForumBubbleDrawable) {
                                                                            TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.currentAccount, this.message.messageOwner, true));
                                                                            spannableStringBuilder17 = str20;
                                                                            if (findTopic != null) {
                                                                                ((ForumBubbleDrawable) this.message.topicIconDrawable[0]).setColor(findTopic.icon_color);
                                                                                spannableStringBuilder17 = str20;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        TLRPC$MessageMedia tLRPC$MessageMedia = this.message.messageOwner.media;
                                                                        if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                            spannableStringBuilder17 = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                                                                        } else {
                                                                            if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                                                                                TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
                                                                                if (((tLRPC$Document instanceof TLRPC$TL_documentEmpty) || tLRPC$Document == null) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                                    if (tLRPC$MessageMedia.voice) {
                                                                                        spannableStringBuilder17 = LocaleController.getString(R.string.AttachVoiceExpired);
                                                                                    } else if (tLRPC$MessageMedia.round) {
                                                                                        spannableStringBuilder17 = LocaleController.getString(R.string.AttachRoundExpired);
                                                                                    } else {
                                                                                        spannableStringBuilder17 = LocaleController.getString(R.string.AttachVideoExpired);
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
                                                                                } else {
                                                                                    str5 = captionMessage.isPhoto() ? "🖼 " : "📎 ";
                                                                                }
                                                                                if (captionMessage.hasHighlightedWords() && !TextUtils.isEmpty(captionMessage.messageOwner.message)) {
                                                                                    CharSequence charSequence5 = captionMessage.messageTrimmedToHighlight;
                                                                                    int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 24);
                                                                                    if (this.hasNameInMessage) {
                                                                                        if (!TextUtils.isEmpty(null)) {
                                                                                            throw null;
                                                                                        }
                                                                                        measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(": "));
                                                                                    }
                                                                                    if (measuredWidth2 > 0) {
                                                                                        charSequence5 = AndroidUtilities.ellipsizeCenterEnd(charSequence5, captionMessage.highlightedWords.get(0), measuredWidth2, this.currentMessagePaint, 130).toString();
                                                                                    }
                                                                                    append = new SpannableStringBuilder(str5).append(charSequence5);
                                                                                } else {
                                                                                    SpannableStringBuilder spannableStringBuilder18 = new SpannableStringBuilder(captionMessage.caption);
                                                                                    if (captionMessage.messageOwner != null) {
                                                                                        captionMessage.spoilLoginCode();
                                                                                        MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, captionMessage.caption, spannableStringBuilder18, 264);
                                                                                        ArrayList<TLRPC$MessageEntity> arrayList3 = captionMessage.messageOwner.entities;
                                                                                        TextPaint textPaint7 = this.currentMessagePaint;
                                                                                        MediaDataController.addAnimatedEmojiSpans(arrayList3, spannableStringBuilder18, textPaint7 == null ? null : textPaint7.getFontMetricsInt());
                                                                                    }
                                                                                    append = new SpannableStringBuilder(str5).append((CharSequence) spannableStringBuilder18);
                                                                                }
                                                                                spannableStringBuilder17 = append;
                                                                            } else if (this.thumbsCount > 1) {
                                                                                if (this.hasVideoThumb) {
                                                                                    ArrayList<MessageObject> arrayList4 = this.groupMessages;
                                                                                    formatPluralString = LocaleController.formatPluralString("Media", arrayList4 == null ? 0 : arrayList4.size(), new Object[0]);
                                                                                } else {
                                                                                    ArrayList<MessageObject> arrayList5 = this.groupMessages;
                                                                                    formatPluralString = LocaleController.formatPluralString("Photos", arrayList5 == null ? 0 : arrayList5.size(), new Object[0]);
                                                                                }
                                                                                spannableStringBuilder17 = formatPluralString;
                                                                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                            } else {
                                                                                MessageObject messageObject12 = this.message;
                                                                                TLRPC$Message tLRPC$Message = messageObject12.messageOwner;
                                                                                TLRPC$MessageMedia tLRPC$MessageMedia2 = tLRPC$Message.media;
                                                                                if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGiveaway) {
                                                                                    TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from;
                                                                                    if (tLRPC$MessageFwdHeader != null) {
                                                                                        TLRPC$Peer tLRPC$Peer = tLRPC$MessageFwdHeader.from_id;
                                                                                        if (tLRPC$Peer instanceof TLRPC$TL_peerChannel) {
                                                                                            isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(tLRPC$Peer.channel_id, this.currentAccount);
                                                                                            str4 = LocaleController.getString(!isChannelAndNotMegaGroup ? R.string.BoostingGiveawayChannelStarted : R.string.BoostingGiveawayGroupStarted);
                                                                                        }
                                                                                    }
                                                                                    isChannelAndNotMegaGroup = ChatObject.isChannelAndNotMegaGroup(this.chat);
                                                                                    str4 = LocaleController.getString(!isChannelAndNotMegaGroup ? R.string.BoostingGiveawayChannelStarted : R.string.BoostingGiveawayGroupStarted);
                                                                                } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGiveawayResults) {
                                                                                    str4 = LocaleController.getString("BoostingGiveawayResults", R.string.BoostingGiveawayResults);
                                                                                } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                                                                    str4 = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2).poll.question;
                                                                                } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                                                                    str4 = "🎮 " + this.message.messageOwner.media.game.title;
                                                                                } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                    str4 = tLRPC$MessageMedia2.title;
                                                                                } else if (messageObject12.type == 14) {
                                                                                    str4 = String.format("🎧 %s - %s", messageObject12.getMusicAuthor(), this.message.getMusicTitle());
                                                                                } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaStory) && tLRPC$MessageMedia2.via_mention) {
                                                                                    if (messageObject12.isOut()) {
                                                                                        TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.getDialogId()));
                                                                                        if (user != null) {
                                                                                            str3 = UserObject.getFirstName(user);
                                                                                            int indexOf3 = str3.indexOf(32);
                                                                                            if (indexOf3 >= 0) {
                                                                                                str3 = str3.substring(0, indexOf3);
                                                                                            }
                                                                                        } else {
                                                                                            str3 = "";
                                                                                        }
                                                                                        str4 = LocaleController.formatString("StoryYouMentionInDialog", R.string.StoryYouMentionInDialog, str3);
                                                                                    } else {
                                                                                        str4 = LocaleController.getString("StoryMentionInDialog", R.string.StoryMentionInDialog);
                                                                                    }
                                                                                } else {
                                                                                    if (messageObject12.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                        str2 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 23), this.currentMessagePaint, 130);
                                                                                    } else {
                                                                                        ?? spannableStringBuilder19 = new SpannableStringBuilder(spannableStringBuilder6);
                                                                                        MessageObject messageObject13 = this.message;
                                                                                        if (messageObject13 != null) {
                                                                                            messageObject13.spoilLoginCode();
                                                                                        }
                                                                                        MediaDataController.addTextStyleRuns(this.message, (Spannable) spannableStringBuilder19, 264);
                                                                                        MessageObject messageObject14 = this.message;
                                                                                        str2 = spannableStringBuilder19;
                                                                                        if (messageObject14 != null) {
                                                                                            TLRPC$Message tLRPC$Message2 = messageObject14.messageOwner;
                                                                                            str2 = spannableStringBuilder19;
                                                                                            if (tLRPC$Message2 != null) {
                                                                                                ArrayList<TLRPC$MessageEntity> arrayList6 = tLRPC$Message2.entities;
                                                                                                TextPaint textPaint8 = this.currentMessagePaint;
                                                                                                MediaDataController.addAnimatedEmojiSpans(arrayList6, spannableStringBuilder19, textPaint8 == null ? null : textPaint8.getFontMetricsInt());
                                                                                                str2 = spannableStringBuilder19;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    AndroidUtilities.highlightText(str2, this.message.highlightedWords, this.resourcesProvider);
                                                                                    str4 = str2;
                                                                                }
                                                                                String str21 = str4;
                                                                                MessageObject messageObject15 = this.message;
                                                                                spannableStringBuilder17 = str21;
                                                                                if (messageObject15.messageOwner.media != null) {
                                                                                    spannableStringBuilder17 = str21;
                                                                                    if (!messageObject15.isMediaEmpty()) {
                                                                                        this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                        spannableStringBuilder17 = str21;
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                CharSequence charSequence6 = spannableStringBuilder17;
                                                                if (this.message.isReplyToStory()) {
                                                                    ?? spannableStringBuilder20 = new SpannableStringBuilder(spannableStringBuilder17);
                                                                    spannableStringBuilder20.insert(0, "d ");
                                                                    spannableStringBuilder20.setSpan(new ColoredImageSpan(ContextCompat.getDrawable(getContext(), R.drawable.msg_mini_replystory).mutate()), 0, 1, 0);
                                                                    charSequence6 = spannableStringBuilder20;
                                                                }
                                                                CharSequence charSequence7 = charSequence6;
                                                                if (this.thumbsCount > 0) {
                                                                    if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                        replaceNewLines = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp((((this.messagePaddingStart + 23) + ((this.thumbSize + 2) * this.thumbsCount)) - 2) + 5), this.currentMessagePaint, 130).toString();
                                                                    } else {
                                                                        int length = charSequence6.length();
                                                                        CharSequence charSequence8 = charSequence6;
                                                                        if (length > 150) {
                                                                            charSequence8 = charSequence6.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                        }
                                                                        replaceNewLines = AndroidUtilities.replaceNewLines(charSequence8);
                                                                    }
                                                                    SpannableStringBuilder spannableStringBuilder21 = !(replaceNewLines instanceof SpannableStringBuilder) ? new SpannableStringBuilder(replaceNewLines) : replaceNewLines;
                                                                    SpannableStringBuilder spannableStringBuilder22 = (SpannableStringBuilder) spannableStringBuilder21;
                                                                    spannableStringBuilder22.insert(0, (CharSequence) " ");
                                                                    spannableStringBuilder22.setSpan(new FixedWidthSpan(AndroidUtilities.dp((((this.thumbSize + 2) * this.thumbsCount) - 2) + 5)), 0, 1, 33);
                                                                    Emoji.replaceEmoji((CharSequence) spannableStringBuilder22, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                                                                    if (this.message.hasHighlightedWords() && (highlightText = AndroidUtilities.highlightText(spannableStringBuilder22, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                        spannableStringBuilder21 = highlightText;
                                                                    }
                                                                    z2 = false;
                                                                    charSequence7 = spannableStringBuilder21;
                                                                }
                                                                if (this.message.isForwarded() && this.message.needDrawForwarded()) {
                                                                    this.drawForwardIcon = true;
                                                                    r1 = new SpannableStringBuilder(charSequence7);
                                                                    r1.insert(0, "d ");
                                                                    ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(ContextCompat.getDrawable(getContext(), R.drawable.mini_forwarded).mutate());
                                                                    coloredImageSpan2.setAlpha(0.9f);
                                                                    r1.setSpan(coloredImageSpan2, 0, 1, 0);
                                                                } else {
                                                                    r1 = charSequence7;
                                                                }
                                                            }
                                                        }
                                                    }
                                                    z7 = false;
                                                    str6 = null;
                                                    z4 = false;
                                                    str7 = string2;
                                                    String str1822 = str6;
                                                    if (this.currentDialogFolderId != 0) {
                                                    }
                                                    spannableStringBuilder5 = null;
                                                    String str1922 = str1822;
                                                    z3 = z2;
                                                    string = str1922;
                                                    SpannableStringBuilder spannableStringBuilder1422 = spannableStringBuilder;
                                                    charSequence = str7;
                                                    spannableStringBuilder3 = spannableStringBuilder1422;
                                                }
                                                str6 = null;
                                                z4 = true;
                                                str7 = r1;
                                                String str18222 = str6;
                                                if (this.currentDialogFolderId != 0) {
                                                }
                                                spannableStringBuilder5 = null;
                                                String str19222 = str18222;
                                                z3 = z2;
                                                string = str19222;
                                                SpannableStringBuilder spannableStringBuilder14222 = spannableStringBuilder;
                                                charSequence = str7;
                                                spannableStringBuilder3 = spannableStringBuilder14222;
                                            }
                                        }
                                    }
                                    r1 = "";
                                    z5 = false;
                                    if (!z5) {
                                    }
                                    str6 = null;
                                    z4 = true;
                                    str7 = r1;
                                    String str182222 = str6;
                                    if (this.currentDialogFolderId != 0) {
                                    }
                                    spannableStringBuilder5 = null;
                                    String str192222 = str182222;
                                    z3 = z2;
                                    string = str192222;
                                    SpannableStringBuilder spannableStringBuilder142222 = spannableStringBuilder;
                                    charSequence = str7;
                                    spannableStringBuilder3 = spannableStringBuilder142222;
                                }
                            }
                            z3 = z2;
                            spannableStringBuilder3 = spannableStringBuilder;
                            charSequence = string3;
                            string = null;
                            z4 = true;
                            spannableStringBuilder5 = null;
                        }
                    }
                    if (this.draftMessage != null) {
                        stringForMessageListDate = LocaleController.stringForMessageListDate(tLRPC$DraftMessage4.date);
                    } else {
                        int i21 = this.lastMessageDate;
                        if (i21 != 0) {
                            stringForMessageListDate = LocaleController.stringForMessageListDate(i21);
                        } else {
                            stringForMessageListDate = this.message != null ? LocaleController.stringForMessageListDate(messageObject2.messageOwner.date) : "";
                        }
                    }
                    messageObject3 = this.message;
                    if (messageObject3 != null || this.isSavedDialog) {
                        this.drawCheck1 = false;
                        this.drawCheck2 = false;
                        this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
                        this.drawCount = false;
                        this.drawMention = false;
                        this.drawReactionMention = false;
                        this.drawError = false;
                        str8 = null;
                        str9 = null;
                    } else {
                        if (this.currentDialogFolderId != 0) {
                            int i22 = this.unreadCount;
                            int i23 = this.mentionCount;
                            if (i22 + i23 <= 0) {
                                this.drawCount = false;
                                this.drawMention = false;
                                str8 = null;
                            } else if (i22 > i23) {
                                this.drawCount = true;
                                this.drawMention = false;
                                str8 = String.format("%d", Integer.valueOf(i22 + i23));
                            } else {
                                this.drawCount = false;
                                this.drawMention = true;
                                str9 = String.format("%d", Integer.valueOf(i22 + i23));
                                str8 = null;
                                this.drawReactionMention = false;
                            }
                            str9 = null;
                            this.drawReactionMention = false;
                        } else {
                            if (this.clearingDialog) {
                                this.drawCount = false;
                                z7 = false;
                            } else {
                                int i24 = this.unreadCount;
                                if (i24 != 0 && (i24 != 1 || i24 != this.mentionCount || messageObject3 == null || !messageObject3.messageOwner.mentioned)) {
                                    this.drawCount = true;
                                    str8 = String.format("%d", Integer.valueOf(i24));
                                } else if (this.markUnread) {
                                    this.drawCount = true;
                                    str8 = "";
                                } else {
                                    this.drawCount = false;
                                }
                                if (this.mentionCount == 0) {
                                    this.drawMention = true;
                                    str9 = "@";
                                } else {
                                    this.drawMention = false;
                                    str9 = null;
                                }
                                if (this.reactionMentionCount <= 0) {
                                    this.drawReactionMention = true;
                                } else {
                                    this.drawReactionMention = false;
                                }
                            }
                            str8 = null;
                            if (this.mentionCount == 0) {
                            }
                            if (this.reactionMentionCount <= 0) {
                            }
                        }
                        if (this.message.isOut() && this.draftMessage == null && z7) {
                            MessageObject messageObject16 = this.message;
                            if (!(messageObject16.messageOwner.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                                if (messageObject16.isSending()) {
                                    this.drawCheck1 = false;
                                    this.drawCheck2 = false;
                                    this.drawClock = true;
                                    this.drawError = false;
                                } else if (this.message.isSendError()) {
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
                                        int i25 = this.readOutboxMaxId;
                                        this.drawCheck1 = (i25 > 0 && i25 >= this.message.getId()) || !this.message.isUnread() || (ChatObject.isChannel(this.chat) && !this.chat.megagroup);
                                    } else {
                                        this.drawCheck1 = !this.message.isUnread() || (ChatObject.isChannel(this.chat) && !this.chat.megagroup);
                                    }
                                    this.drawCheck2 = true;
                                    this.drawClock = false;
                                    this.drawError = false;
                                }
                            }
                        }
                        this.drawCheck1 = false;
                        this.drawCheck2 = false;
                        this.drawClock = false;
                        this.drawError = false;
                    }
                    this.promoDialog = false;
                    messagesController = MessagesController.getInstance(this.currentAccount);
                    if (this.dialogsType == 0 && messagesController.isPromoDialog(this.currentDialogId, true)) {
                        this.drawPinBackground = true;
                        this.promoDialog = true;
                        i5 = messagesController.promoDialogType;
                        if (i5 != MessagesController.PROMO_TYPE_PROXY) {
                            stringForMessageListDate = LocaleController.getString("UseProxySponsor", R.string.UseProxySponsor);
                        } else if (i5 == MessagesController.PROMO_TYPE_PSA) {
                            stringForMessageListDate = LocaleController.getString("PsaType_" + messagesController.promoPsaType);
                            if (TextUtils.isEmpty(stringForMessageListDate)) {
                                stringForMessageListDate = LocaleController.getString("PsaTypeDefault", R.string.PsaTypeDefault);
                            }
                            if (!TextUtils.isEmpty(messagesController.promoPsaMessage)) {
                                charSequence = messagesController.promoPsaMessage;
                                this.thumbsCount = 0;
                            }
                        }
                    }
                    charSequence3 = charSequence;
                    if (this.currentDialogFolderId != 0) {
                        userName = LocaleController.getString("ArchivedChats", R.string.ArchivedChats);
                    } else {
                        TLRPC$Chat tLRPC$Chat9 = this.chat;
                        if (tLRPC$Chat9 != null) {
                            if (this.useFromUserAsAvatar) {
                                if (this.topicIconInName == null) {
                                    this.topicIconInName = new Drawable[1];
                                }
                                this.topicIconInName[0] = null;
                                userName = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, this.currentMessagePaint, this.topicIconInName);
                            } else if (this.isTopic) {
                                if (this.topicIconInName == null) {
                                    this.topicIconInName = new Drawable[1];
                                }
                                Drawable[] drawableArr = this.topicIconInName;
                                drawableArr[0] = null;
                                userName = this.showTopicIconInName ? ForumUtilities.getTopicSpannedName(this.forumTopic, Theme.dialogs_namePaint[this.paintIndex], drawableArr, false) : this.forumTopic.title;
                            } else {
                                userName = tLRPC$Chat9.title;
                            }
                            if (userName != null && userName.length() == 0) {
                                userName = LocaleController.getString("HiddenName", R.string.HiddenName);
                            }
                        } else {
                            TLRPC$User tLRPC$User4 = this.user;
                            if (tLRPC$User4 != null) {
                                if (UserObject.isReplyUser(tLRPC$User4)) {
                                    userName = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                } else if (UserObject.isAnonymous(this.user)) {
                                    userName = LocaleController.getString(R.string.AnonymousForward);
                                } else if (UserObject.isUserSelf(this.user)) {
                                    if (this.isSavedDialog) {
                                        userName = LocaleController.getString(R.string.MyNotes);
                                    } else if (this.useMeForMyMessages) {
                                        userName = LocaleController.getString("FromYou", R.string.FromYou);
                                    } else {
                                        if (this.dialogsType == 3) {
                                            this.drawPinBackground = true;
                                        }
                                        userName = LocaleController.getString("SavedMessages", R.string.SavedMessages);
                                    }
                                } else {
                                    userName = UserObject.getUserName(this.user);
                                }
                                if (userName != null) {
                                    userName = LocaleController.getString("HiddenName", R.string.HiddenName);
                                }
                            }
                            userName = "";
                            if (userName != null) {
                            }
                        }
                    }
                    z6 = z3;
                    i4 = i2;
                    SpannableStringBuilder spannableStringBuilder152 = spannableStringBuilder3;
                    str10 = string;
                    str11 = userName;
                    spannableStringBuilder4 = spannableStringBuilder152;
                }
            }
            tLRPC$DraftMessage3 = null;
            this.draftMessage = null;
            this.draftVoice = false;
            if (!isForumCell()) {
            }
            if (this.draftMessage != null) {
            }
            messageObject3 = this.message;
            if (messageObject3 != null) {
            }
            this.drawCheck1 = false;
            this.drawCheck2 = false;
            this.drawClock = messageObject3 == null && messageObject3.isSending() && this.currentDialogId == UserConfig.getInstance(this.currentAccount).getClientUserId();
            this.drawCount = false;
            this.drawMention = false;
            this.drawReactionMention = false;
            this.drawError = false;
            str8 = null;
            str9 = null;
            this.promoDialog = false;
            messagesController = MessagesController.getInstance(this.currentAccount);
            if (this.dialogsType == 0) {
                this.drawPinBackground = true;
                this.promoDialog = true;
                i5 = messagesController.promoDialogType;
                if (i5 != MessagesController.PROMO_TYPE_PROXY) {
                }
            }
            charSequence3 = charSequence;
            if (this.currentDialogFolderId != 0) {
            }
            z6 = z3;
            i4 = i2;
            SpannableStringBuilder spannableStringBuilder1522 = spannableStringBuilder3;
            str10 = string;
            str11 = userName;
            spannableStringBuilder4 = spannableStringBuilder1522;
        }
        if (!z4) {
            i6 = (int) Math.ceil(Theme.dialogs_timePaint.measureText(stringForMessageListDate));
            this.timeLayout = new StaticLayout(stringForMessageListDate, Theme.dialogs_timePaint, i6, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (!LocaleController.isRTL) {
                this.timeLeft = (getMeasuredWidth() - AndroidUtilities.dp(15.0f)) - i6;
            } else {
                this.timeLeft = AndroidUtilities.dp(15.0f);
            }
        } else {
            this.timeLayout = null;
            this.timeLeft = 0;
            i6 = 0;
        }
        if (drawLock2()) {
            i7 = 0;
        } else {
            if (LocaleController.isRTL) {
                this.lock2Left = this.timeLeft + i6 + AndroidUtilities.dp(4.0f);
            } else {
                this.lock2Left = (this.timeLeft - Theme.dialogs_lock2Drawable.getIntrinsicWidth()) - AndroidUtilities.dp(4.0f);
            }
            i7 = Theme.dialogs_lock2Drawable.getIntrinsicWidth() + AndroidUtilities.dp(4.0f) + 0;
            i6 += i7;
        }
        if (LocaleController.isRTL) {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp(22.0f)) - i6;
        } else {
            this.nameWidth = ((getMeasuredWidth() - this.nameLeft) - AndroidUtilities.dp((this.messagePaddingStart + 5) + 8)) - i6;
            this.nameLeft += i6;
        }
        if (this.drawNameLock) {
            this.nameWidth -= AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : 4.0f) + Theme.dialogs_lockDrawable.getIntrinsicWidth();
        }
        if (!this.drawClock) {
            int intrinsicWidth2 = Theme.dialogs_clockDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            this.nameWidth -= intrinsicWidth2;
            if (!LocaleController.isRTL) {
                this.clockDrawLeft = (this.timeLeft - i7) - intrinsicWidth2;
            } else {
                this.clockDrawLeft = this.timeLeft + i6 + AndroidUtilities.dp(5.0f);
                this.nameLeft += intrinsicWidth2;
            }
        } else if (this.drawCheck2) {
            int intrinsicWidth3 = Theme.dialogs_checkDrawable.getIntrinsicWidth() + AndroidUtilities.dp(5.0f);
            int i26 = this.nameWidth - intrinsicWidth3;
            this.nameWidth = i26;
            if (this.drawCheck1) {
                this.nameWidth = i26 - (Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f));
                if (!LocaleController.isRTL) {
                    int i27 = (this.timeLeft - i7) - intrinsicWidth3;
                    this.halfCheckDrawLeft = i27;
                    this.checkDrawLeft = i27 - AndroidUtilities.dp(5.5f);
                } else {
                    int dp8 = this.timeLeft + i6 + AndroidUtilities.dp(5.0f);
                    this.checkDrawLeft = dp8;
                    this.halfCheckDrawLeft = dp8 + AndroidUtilities.dp(5.5f);
                    this.nameLeft += (intrinsicWidth3 + Theme.dialogs_halfCheckDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(8.0f);
                }
            } else if (!LocaleController.isRTL) {
                this.checkDrawLeft1 = (this.timeLeft - i7) - intrinsicWidth3;
            } else {
                this.checkDrawLeft1 = this.timeLeft + i6 + AndroidUtilities.dp(5.0f);
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
            if (str11 instanceof String) {
                str11 = ((String) str11).replace('\n', ' ');
            }
            if (this.nameLayoutEllipsizeByGradient) {
                this.nameLayoutFits = str11.length() == TextUtils.ellipsize(str11, Theme.dialogs_namePaint[this.paintIndex], (float) dp6, TextUtils.TruncateAt.END).length();
                dp6 += AndroidUtilities.dp(48.0f);
            }
            float f2 = dp6;
            this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(str11.toString()) <= f2;
            if (!this.twoLinesForName) {
                str11 = TextUtils.ellipsize(str11, Theme.dialogs_namePaint[this.paintIndex], f2, TextUtils.TruncateAt.END);
            }
            CharSequence replaceEmoji22 = Emoji.replaceEmoji(str11, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            MessageObject messageObject102 = this.message;
            CharSequence charSequence9 = (messageObject102 == null && messageObject102.hasHighlightedWords() && (highlightText5 = AndroidUtilities.highlightText(replaceEmoji22, this.message.highlightedWords, this.resourcesProvider)) != null) ? highlightText5 : replaceEmoji22;
            if (!this.twoLinesForName) {
                this.nameLayout = StaticLayoutEx.createStaticLayout(charSequence9, Theme.dialogs_namePaint[this.paintIndex], dp6, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, dp6, 2);
            } else {
                this.nameLayout = new StaticLayout(charSequence9, Theme.dialogs_namePaint[this.paintIndex], Math.max(dp6, this.nameWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
                dp2 = getMeasuredWidth() - AndroidUtilities.dp(66.0f);
                dp3 = dp2 - AndroidUtilities.dp(31.0f);
            } else {
                int dp14 = AndroidUtilities.dp(this.messagePaddingStart + 6);
                this.messageNameLeft = dp14;
                this.messageLeft = dp14;
                this.typingLeft = dp14;
                this.buttonLeft = dp14;
                dp2 = AndroidUtilities.dp(10.0f);
                dp3 = AndroidUtilities.dp(69.0f) + dp2;
            }
            i8 = measuredWidth3;
            this.storyParams.originalAvatarRect.set(dp2, dp, dp2 + AndroidUtilities.dp(56.0f), dp + AndroidUtilities.dp(56.0f));
            i9 = 0;
            while (true) {
                imageReceiverArr = this.thumbImage;
                if (i9 < imageReceiverArr.length) {
                    break;
                }
                imageReceiverArr[i9].setImageCoords(((this.thumbSize + 2) * i9) + dp3, ((AndroidUtilities.dp(31.0f) + dp) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0)) - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout || (dialogCellTags = this.tags) == null || dialogCellTags.isEmpty()) ? 0 : AndroidUtilities.dp(9.0f)), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                i9++;
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
                dp4 = getMeasuredWidth() - AndroidUtilities.dp(64.0f);
                dp5 = dp4 - AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 11);
            } else {
                int dp16 = AndroidUtilities.dp(this.messagePaddingStart + 4);
                this.messageNameLeft = dp16;
                this.messageLeft = dp16;
                this.typingLeft = dp16;
                this.buttonLeft = dp16;
                dp4 = AndroidUtilities.dp(10.0f);
                dp5 = AndroidUtilities.dp(67.0f) + dp4;
            }
            i8 = measuredWidth4;
            this.storyParams.originalAvatarRect.set(dp4, dp, dp4 + AndroidUtilities.dp(54.0f), dp + AndroidUtilities.dp(54.0f));
            int i28 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr2 = this.thumbImage;
                if (i28 >= imageReceiverArr2.length) {
                    break;
                }
                imageReceiverArr2[i28].setImageCoords(((this.thumbSize + 2) * i28) + dp5, ((AndroidUtilities.dp(30.0f) + dp) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0)) - ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout || (dialogCellTags3 = this.tags) == null || dialogCellTags3.isEmpty()) ? 0 : AndroidUtilities.dp(9.0f)), AndroidUtilities.dp(this.thumbSize), AndroidUtilities.dp(this.thumbSize));
                i28++;
                dp = dp;
            }
        }
        int i192 = dp;
        int i202 = i8;
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
            i202 -= dp17;
            if (!LocaleController.isRTL) {
                this.errorLeft = getMeasuredWidth() - AndroidUtilities.dp(34.0f);
            } else {
                this.errorLeft = AndroidUtilities.dp(11.0f);
                this.messageLeft += dp17;
                this.typingLeft += dp17;
                this.buttonLeft += dp17;
                this.messageNameLeft += dp17;
            }
        } else if (str8 != null || str9 != null || this.drawReactionMention) {
            if (str8 != null) {
                this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str8)));
                this.countLayout = new StaticLayout(str8, Theme.dialogs_countTextPaint, this.countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                int dp18 = this.countWidth + AndroidUtilities.dp(18.0f);
                i202 -= dp18;
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
            if (str9 != null) {
                if (this.currentDialogFolderId != 0) {
                    this.mentionWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str9)));
                    this.mentionLayout = new StaticLayout(str9, Theme.dialogs_countTextPaint, this.mentionWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                } else {
                    this.mentionWidth = AndroidUtilities.dp(12.0f);
                }
                int dp19 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                i202 -= dp19;
                if (!LocaleController.isRTL) {
                    int measuredWidth5 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                    int i29 = this.countWidth;
                    this.mentionLeft = measuredWidth5 - (i29 != 0 ? i29 + AndroidUtilities.dp(18.0f) : 0);
                } else {
                    int dp20 = AndroidUtilities.dp(20.0f);
                    int i30 = this.countWidth;
                    this.mentionLeft = dp20 + (i30 != 0 ? i30 + AndroidUtilities.dp(18.0f) : 0);
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
                i202 -= dp21;
                if (!LocaleController.isRTL) {
                    int measuredWidth6 = getMeasuredWidth() - AndroidUtilities.dp(32.0f);
                    this.reactionMentionLeft = measuredWidth6;
                    if (this.drawMention) {
                        int i31 = this.mentionWidth;
                        this.reactionMentionLeft = measuredWidth6 - (i31 != 0 ? i31 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i32 = this.reactionMentionLeft;
                        int i33 = this.countWidth;
                        this.reactionMentionLeft = i32 - (i33 != 0 ? i33 + AndroidUtilities.dp(18.0f) : 0);
                    }
                } else {
                    int dp22 = AndroidUtilities.dp(20.0f);
                    this.reactionMentionLeft = dp22;
                    if (this.drawMention) {
                        int i34 = this.mentionWidth;
                        this.reactionMentionLeft = dp22 + (i34 != 0 ? i34 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i35 = this.reactionMentionLeft;
                        int i36 = this.countWidth;
                        this.reactionMentionLeft = i35 + (i36 != 0 ? i36 + AndroidUtilities.dp(18.0f) : 0);
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
                i202 -= intrinsicWidth4;
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
        if (z6) {
            if (charSequence3 == null) {
                charSequence3 = "";
            }
            if (charSequence3.length() > 150) {
                charSequence3 = charSequence3.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            }
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || hasTags() || str10 != null) {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceNewLines(charSequence3);
            } else {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceTwoNewLinesToOne(charSequence3);
            }
            charSequence3 = Emoji.replaceEmoji(replaceTwoNewLinesToOne, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
            MessageObject messageObject17 = this.message;
            if (messageObject17 != null && (highlightText4 = AndroidUtilities.highlightText(charSequence3, messageObject17.highlightedWords, this.resourcesProvider)) != null) {
                charSequence3 = highlightText4;
            }
        }
        max = Math.max(AndroidUtilities.dp(12.0f), i202);
        this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
        if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
            this.buttonTop -= AndroidUtilities.dp(!isForumCell() ? 10.0f : 12.0f);
        }
        if (!isForumCell()) {
            this.messageTop = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 34.0f : 39.0f);
            int i37 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr3 = this.thumbImage;
                if (i37 >= imageReceiverArr3.length) {
                    break;
                }
                imageReceiverArr3[i37].setImageY(this.buttonTop);
                i37++;
            }
        } else if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags() && str10 != null && (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount == 1)) {
            try {
                MessageObject messageObject18 = this.message;
                if (messageObject18 != null && messageObject18.hasHighlightedWords() && (highlightText3 = AndroidUtilities.highlightText(str10, this.message.highlightedWords, this.resourcesProvider)) != 0) {
                    str10 = highlightText3;
                }
                this.messageNameLayout = StaticLayoutEx.createStaticLayout(str10, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
            } catch (Exception e4) {
                FileLog.e(e4);
            }
            this.messageTop = AndroidUtilities.dp(51.0f);
            int dp23 = (this.nameIsEllipsized && this.isTopic) ? AndroidUtilities.dp(20.0f) : 0;
            int i38 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr4 = this.thumbImage;
                if (i38 >= imageReceiverArr4.length) {
                    break;
                }
                imageReceiverArr4[i38].setImageY(i192 + dp23 + AndroidUtilities.dp(40.0f));
                i38++;
            }
        } else {
            this.messageNameLayout = null;
            if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                this.messageTop = AndroidUtilities.dp(32.0f);
                int dp24 = (this.nameIsEllipsized && this.isTopic) ? AndroidUtilities.dp(20.0f) : 0;
                int i39 = 0;
                while (true) {
                    ImageReceiver[] imageReceiverArr5 = this.thumbImage;
                    if (i39 >= imageReceiverArr5.length) {
                        break;
                    }
                    imageReceiverArr5[i39].setImageY(i192 + dp24 + AndroidUtilities.dp(21.0f));
                    i39++;
                }
            } else {
                this.messageTop = AndroidUtilities.dp(39.0f);
            }
        }
        if (this.twoLinesForName) {
            this.messageTop += AndroidUtilities.dp(20.0f);
        }
        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack2, this.messageNameLayout);
        try {
            this.buttonCreated = false;
            if (TextUtils.isEmpty(spannableStringBuilder5)) {
                this.buttonLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji((CharSequence) spannableStringBuilder5, this.currentMessagePaint.getFontMetricsInt(), AndroidUtilities.dp(17.0f), false), this.currentMessagePaint, max - AndroidUtilities.dp(26.0f), TextUtils.TruncateAt.END), this.currentMessagePaint, max - AndroidUtilities.dp(20.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
            if (!TextUtils.isEmpty(spannableStringBuilder4)) {
                if ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                    this.typingLayout = StaticLayoutEx.createStaticLayout(spannableStringBuilder4, Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, 1);
                } else {
                    this.typingLayout = new StaticLayout(TextUtils.ellipsize(spannableStringBuilder4, this.currentMessagePaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                }
            }
        } catch (Exception e5) {
            FileLog.e(e5);
        }
        try {
            if (charSequence3 instanceof Spannable) {
                Spannable spannable = (Spannable) charSequence3;
                for (Object obj : spannable.getSpans(0, spannable.length(), Object.class)) {
                    if ((obj instanceof ClickableSpan) || (obj instanceof CodeHighlighting.Span) || ((!isFolderCell() && (obj instanceof TypefaceSpan)) || (obj instanceof CodeHighlighting.ColorSpan) || (obj instanceof QuoteSpan) || (obj instanceof QuoteSpan.QuoteStyleSpan) || ((obj instanceof StyleSpan) && ((StyleSpan) obj).getStyle() == 1))) {
                        spannable.removeSpan(obj);
                    }
                }
            }
            if ((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags() && this.currentDialogFolderId != 0 && this.currentDialogFolderDialogsCount > 1) {
                this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                str12 = str10;
                str10 = null;
            } else {
                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    str13 = charSequence3;
                    if (!hasTags()) {
                        if (str10 != null) {
                        }
                        str12 = str13;
                    }
                }
                if (!isForumCell() && (charSequence3 instanceof Spanned) && ((FixedWidthSpan[]) ((Spanned) charSequence3).getSpans(0, charSequence3.length(), FixedWidthSpan.class)).length <= 0) {
                    str13 = TextUtils.ellipsize(charSequence3, this.currentMessagePaint, max - AndroidUtilities.dp((((this.thumbsCount * (this.thumbSize + 2)) - 2) + 12) + 5), TextUtils.TruncateAt.END);
                } else {
                    str13 = TextUtils.ellipsize(charSequence3, this.currentMessagePaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
                }
                str12 = str13;
            }
            alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
            if ((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && !hasTags()) {
                if (this.thumbsCount > 0 && str10 != null) {
                    max += AndroidUtilities.dp(5.0f);
                }
                this.messageLayout = StaticLayoutEx.createStaticLayout(str12, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, str10 == null ? 1 : 2);
            } else {
                if (this.thumbsCount > 0) {
                    max += AndroidUtilities.dp(((i12 * (this.thumbSize + 2)) - 2) + 5);
                    if (LocaleController.isRTL && !isForumCell()) {
                        this.messageLeft -= AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5);
                    }
                }
                this.messageLayout = new StaticLayout(str12, this.currentMessagePaint, max, alignment, 1.0f, 0.0f, false);
            }
            i10 = max;
        } catch (Exception e6) {
            e = e6;
        }
        try {
            this.spoilersPool.addAll(this.spoilers);
            this.spoilers.clear();
            SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
        } catch (Exception e7) {
            e = e7;
            max = i10;
            this.messageLayout = null;
            FileLog.e(e);
            i10 = max;
            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack, this.messageLayout);
            if (LocaleController.isRTL) {
            }
            staticLayout = this.typingLayout;
            if (staticLayout != null) {
                if (i4 < 0) {
                }
                primaryHorizontal = this.typingLayout.getPrimaryHorizontal(0);
                primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(1);
                if (primaryHorizontal >= primaryHorizontal2) {
                }
            }
            updateThumbsPosition();
        }
        this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack, this.messageLayout);
        if (LocaleController.isRTL) {
            StaticLayout staticLayout2 = this.nameLayout;
            if (staticLayout2 != null && staticLayout2.getLineCount() > 0) {
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
                    double intrinsicWidth7 = (this.drawScam == 1 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).getIntrinsicWidth();
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
                    int i40 = this.nameWidth;
                    if (ceil < i40) {
                        double d21 = this.nameLeft;
                        double d22 = i40;
                        Double.isNaN(d22);
                        Double.isNaN(d21);
                        this.nameLeft = (int) (d21 + (d22 - ceil));
                    }
                }
            }
            StaticLayout staticLayout3 = this.messageLayout;
            int i41 = ConnectionsManager.DEFAULT_DATACENTER_ID;
            if (staticLayout3 != null && (lineCount6 = staticLayout3.getLineCount()) > 0) {
                int i42 = 0;
                int i43 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                while (true) {
                    if (i42 >= lineCount6) {
                        break;
                    }
                    if (this.messageLayout.getLineLeft(i42) != 0.0f) {
                        i43 = 0;
                        break;
                    }
                    double ceil2 = Math.ceil(this.messageLayout.getLineWidth(i42));
                    double d23 = i10;
                    Double.isNaN(d23);
                    i43 = Math.min(i43, (int) (d23 - ceil2));
                    i42++;
                }
                if (i43 != Integer.MAX_VALUE) {
                    this.messageLeft += i43;
                }
            }
            StaticLayout staticLayout4 = this.typingLayout;
            if (staticLayout4 != null && (lineCount5 = staticLayout4.getLineCount()) > 0) {
                int i44 = 0;
                int i45 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                while (true) {
                    if (i44 >= lineCount5) {
                        break;
                    }
                    if (this.typingLayout.getLineLeft(i44) != 0.0f) {
                        i45 = 0;
                        break;
                    }
                    double ceil3 = Math.ceil(this.typingLayout.getLineWidth(i44));
                    double d24 = i10;
                    Double.isNaN(d24);
                    i45 = Math.min(i45, (int) (d24 - ceil3));
                    i44++;
                }
                if (i45 != Integer.MAX_VALUE) {
                    this.typingLeft += i45;
                }
            }
            StaticLayout staticLayout5 = this.messageNameLayout;
            if (staticLayout5 != null && staticLayout5.getLineCount() > 0 && this.messageNameLayout.getLineLeft(0) == 0.0f) {
                double ceil4 = Math.ceil(this.messageNameLayout.getLineWidth(0));
                double d25 = i10;
                if (ceil4 < d25) {
                    double d26 = this.messageNameLeft;
                    Double.isNaN(d25);
                    Double.isNaN(d26);
                    this.messageNameLeft = (int) (d26 + (d25 - ceil4));
                }
            }
            StaticLayout staticLayout6 = this.buttonLayout;
            if (staticLayout6 != null && (lineCount4 = staticLayout6.getLineCount()) > 0) {
                for (int i46 = 0; i46 < lineCount4; i46++) {
                    i41 = (int) Math.min(i41, this.buttonLayout.getWidth() - this.buttonLayout.getLineRight(i46));
                }
                this.buttonLeft += i41;
            }
        } else {
            StaticLayout staticLayout7 = this.nameLayout;
            if (staticLayout7 != null && staticLayout7.getLineCount() > 0) {
                float lineRight = this.nameLayout.getLineRight(0);
                if (this.nameLayoutEllipsizeByGradient) {
                    lineRight = Math.min(this.nameWidth, lineRight);
                }
                if (lineRight == this.nameWidth) {
                    double ceil5 = Math.ceil(this.nameLayout.getLineWidth(0));
                    if (this.nameLayoutEllipsizeByGradient) {
                        ceil5 = Math.min(this.nameWidth, ceil5);
                    }
                    int i47 = this.nameWidth;
                    if (ceil5 < i47) {
                        double d27 = this.nameLeft;
                        double d28 = i47;
                        Double.isNaN(d28);
                        Double.isNaN(d27);
                        this.nameLeft = (int) (d27 - (d28 - ceil5));
                    }
                }
                this.nameMuteLeft = (int) (this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
            }
            StaticLayout staticLayout8 = this.messageLayout;
            if (staticLayout8 != null && (lineCount3 = staticLayout8.getLineCount()) > 0) {
                float f3 = 2.14748365E9f;
                for (int i48 = 0; i48 < lineCount3; i48++) {
                    f3 = Math.min(f3, this.messageLayout.getLineLeft(i48));
                }
                this.messageLeft = (int) (this.messageLeft - f3);
            }
            StaticLayout staticLayout9 = this.buttonLayout;
            if (staticLayout9 != null && (lineCount2 = staticLayout9.getLineCount()) > 0) {
                float f4 = 2.14748365E9f;
                for (int i49 = 0; i49 < lineCount2; i49++) {
                    f4 = Math.min(f4, this.buttonLayout.getLineLeft(i49));
                }
                this.buttonLeft = (int) (this.buttonLeft - f4);
            }
            StaticLayout staticLayout10 = this.typingLayout;
            if (staticLayout10 != null && (lineCount = staticLayout10.getLineCount()) > 0) {
                float f5 = 2.14748365E9f;
                for (int i50 = 0; i50 < lineCount; i50++) {
                    f5 = Math.min(f5, this.typingLayout.getLineLeft(i50));
                }
                this.typingLeft = (int) (this.typingLeft - f5);
            }
            StaticLayout staticLayout11 = this.messageNameLayout;
            if (staticLayout11 != null && staticLayout11.getLineCount() > 0) {
                this.messageNameLeft = (int) (this.messageNameLeft - this.messageNameLayout.getLineLeft(0));
            }
        }
        staticLayout = this.typingLayout;
        if (staticLayout != null && this.printingStringType >= 0 && staticLayout.getText().length() > 0) {
            if (i4 < 0 && (i11 = i4 + 1) < this.typingLayout.getText().length()) {
                primaryHorizontal = this.typingLayout.getPrimaryHorizontal(i4);
                primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(i11);
            } else {
                primaryHorizontal = this.typingLayout.getPrimaryHorizontal(0);
                primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(1);
            }
            if (primaryHorizontal >= primaryHorizontal2) {
                this.statusDrawableLeft = (int) (this.typingLeft + primaryHorizontal);
            } else {
                this.statusDrawableLeft = (int) (this.typingLeft + primaryHorizontal2 + AndroidUtilities.dp(3.0f));
            }
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
            valueOf.setSpan(new FixedWidthSpan(AndroidUtilities.dp((((this.thumbSize + 2) * this.thumbsCount) - 2) + 5)), 0, 1, 33);
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
                    Theme.dialogs_clockDrawable.setAlpha(255);
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
                        Theme.dialogs_halfCheckDrawable.setAlpha(255);
                    }
                    if (z4 || f == 0.0f) {
                        return;
                    }
                    canvas.restore();
                    Theme.dialogs_halfCheckDrawable.setAlpha(255);
                    Theme.dialogs_checkReadDrawable.setAlpha(255);
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
                    Theme.dialogs_checkDrawable.setAlpha(255);
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

    /* JADX WARN: Removed duplicated region for block: B:103:0x01f0  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x0366  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x0422  */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0427  */
    /* JADX WARN: Removed duplicated region for block: B:247:0x042b  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x042d  */
    /* JADX WARN: Removed duplicated region for block: B:251:0x0438  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0450  */
    /* JADX WARN: Removed duplicated region for block: B:272:0x04aa  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x0551  */
    /* JADX WARN: Removed duplicated region for block: B:298:0x0575  */
    /* JADX WARN: Removed duplicated region for block: B:378:0x07ec  */
    /* JADX WARN: Removed duplicated region for block: B:379:0x07ee  */
    /* JADX WARN: Type inference failed for: r6v1 */
    /* JADX WARN: Type inference failed for: r6v113 */
    /* JADX WARN: Type inference failed for: r6v2, types: [org.telegram.tgnet.TLRPC$Chat, org.telegram.tgnet.TLRPC$User, org.telegram.tgnet.TLRPC$EncryptedChat] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean update(int i, boolean z) {
        int i2;
        boolean z2;
        boolean z3;
        boolean z4;
        ?? r6;
        long j;
        MessageObject messageObject;
        boolean z5;
        TLRPC$User tLRPC$User;
        boolean z6;
        int i3;
        boolean z7;
        TLRPC$Chat chat;
        boolean z8;
        boolean z9;
        MessageObject messageObject2;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        TLRPC$Chat chat2;
        MessageObject messageObject3;
        boolean isForumCell = isForumCell();
        this.ttlPeriod = 0;
        CustomDialog customDialog = this.customDialog;
        if (customDialog != null) {
            this.lastMessageDate = customDialog.date;
            int i9 = customDialog.unread_count;
            this.lastUnreadState = i9 != 0;
            this.unreadCount = i9;
            this.drawPin = customDialog.pinned;
            this.dialogMuted = customDialog.muted;
            this.hasUnmutedTopics = false;
            this.avatarDrawable.setInfo(customDialog.id, customDialog.name, null);
            this.avatarImage.setImage(null, "50_50", this.avatarDrawable, null, 0L);
            int i10 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr = this.thumbImage;
                if (i10 >= imageReceiverArr.length) {
                    break;
                }
                imageReceiverArr[i10].setImageBitmap((Drawable) null);
                i10++;
            }
            this.avatarImage.setRoundRadius(AndroidUtilities.dp(28.0f));
            this.drawUnmute = false;
            z3 = false;
            z6 = false;
            z5 = false;
        } else {
            int i11 = this.unreadCount;
            boolean z10 = this.reactionMentionCount != 0;
            boolean z11 = this.markUnread;
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
                            boolean z12 = this.isTopic;
                        }
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
                        if (messageObject5 != null) {
                            int i12 = messageObject5.messageOwner.edit_date;
                        }
                        this.lastMessageDate = tLRPC$Dialog.last_message_date;
                        int i13 = this.dialogsType;
                        if (i13 == 7 || i13 == 8) {
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
                    this.lastMessageDate = 0;
                    this.clearingDialog = false;
                }
                long j2 = this.currentDialogId;
                if (j2 != 0) {
                    int i14 = (j2 > RightSlidingDialogContainer.fragmentDialogId ? 1 : (j2 == RightSlidingDialogContainer.fragmentDialogId ? 0 : -1));
                }
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
                i2 = i11;
                if (this.tags.update(this.currentAccount, this.dialogsType, this.currentDialogId)) {
                    if (isEmpty != this.tags.isEmpty()) {
                        z2 = true;
                        z3 = true;
                    } else {
                        z2 = false;
                        z3 = false;
                    }
                    z4 = true;
                    if (i == 0) {
                        TLRPC$User tLRPC$User2 = this.user;
                        if (tLRPC$User2 != null && !MessagesController.isSupportUser(tLRPC$User2) && !this.user.bot && (i & MessagesController.UPDATE_MASK_STATUS) != 0) {
                            this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                            if (this.wasDrawnOnline != isOnline()) {
                                z4 = true;
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
                                z4 = true;
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
                                z4 = true;
                            }
                        }
                        if ((this.isDialogCell || this.isTopic) && (i & MessagesController.UPDATE_MASK_USER_PRINT) != 0) {
                            CharSequence printingString = MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, getTopicId(), true);
                            CharSequence charSequence = this.lastPrintString;
                            if ((charSequence != null && printingString == null) || ((charSequence == null && printingString != null) || (charSequence != null && !charSequence.equals(printingString)))) {
                                z8 = true;
                                if (!z8 && (i & MessagesController.UPDATE_MASK_MESSAGE_TEXT) != 0 && (messageObject3 = this.message) != null && messageObject3.messageText != this.lastMessageString) {
                                    z8 = true;
                                }
                                if (!z8 && (i & MessagesController.UPDATE_MASK_CHAT) != 0 && this.chat != null) {
                                    chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                                    if ((chat2 == null && chat2.call_active && chat2.call_not_empty) != this.hasCall) {
                                        z8 = true;
                                    }
                                }
                                if (!z8 && (i & MessagesController.UPDATE_MASK_AVATAR) != 0 && this.chat == null) {
                                    z8 = true;
                                }
                                if (!z8 && (i & MessagesController.UPDATE_MASK_NAME) != 0 && this.chat == null) {
                                    z8 = true;
                                }
                                if (!z8 && (i & MessagesController.UPDATE_MASK_CHAT_AVATAR) != 0 && this.user == null) {
                                    z8 = true;
                                }
                                z9 = (z8 && (i & MessagesController.UPDATE_MASK_CHAT_NAME) != 0 && this.user == null) ? true : z8;
                                if (!z9) {
                                    MessageObject messageObject7 = this.message;
                                    if (messageObject7 != null && this.lastUnreadState != messageObject7.isUnread()) {
                                        this.lastUnreadState = this.message.isUnread();
                                        z9 = true;
                                    }
                                    if (this.isDialogCell) {
                                        TLRPC$Dialog tLRPC$Dialog2 = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                                        TLRPC$Chat chat5 = tLRPC$Dialog2 == null ? null : MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$Dialog2.id));
                                        if (chat5 != null && chat5.forum) {
                                            int[] forumUnreadCount2 = MessagesController.getInstance(this.currentAccount).getTopicsController().getForumUnreadCount(chat5.id);
                                            int i15 = forumUnreadCount2[0];
                                            int i16 = forumUnreadCount2[1];
                                            int i17 = forumUnreadCount2[2];
                                            this.hasUnmutedTopics = forumUnreadCount2[3] != 0;
                                            i6 = i15;
                                            i7 = i16;
                                            i8 = i17;
                                        } else {
                                            if (tLRPC$Dialog2 instanceof TLRPC$TL_dialogFolder) {
                                                i6 = MessagesStorage.getInstance(this.currentAccount).getArchiveUnreadCount();
                                            } else if (tLRPC$Dialog2 != null) {
                                                i6 = tLRPC$Dialog2.unread_count;
                                                i7 = tLRPC$Dialog2.unread_mentions_count;
                                                i8 = tLRPC$Dialog2.unread_reactions_count;
                                            } else {
                                                i6 = 0;
                                            }
                                            i7 = 0;
                                            i8 = 0;
                                        }
                                        if (tLRPC$Dialog2 != null && (this.unreadCount != i6 || this.markUnread != tLRPC$Dialog2.unread_mark || this.mentionCount != i7 || this.reactionMentionCount != i8)) {
                                            this.unreadCount = i6;
                                            this.mentionCount = i7;
                                            this.markUnread = tLRPC$Dialog2.unread_mark;
                                            this.reactionMentionCount = i8;
                                            z9 = true;
                                        }
                                    }
                                }
                                if (!z9 && (i & MessagesController.UPDATE_MASK_SEND_STATE) != 0 && (messageObject2 = this.message) != null) {
                                    i4 = this.lastSendState;
                                    i5 = messageObject2.messageOwner.send_state;
                                    if (i4 != i5) {
                                        this.lastSendState = i5;
                                        z9 = true;
                                    }
                                }
                                if (z9) {
                                    invalidate();
                                    return z2;
                                }
                                r6 = 0;
                            }
                        }
                        z8 = false;
                        if (!z8) {
                            z8 = true;
                        }
                        if (!z8) {
                            chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                            if ((chat2 == null && chat2.call_active && chat2.call_not_empty) != this.hasCall) {
                            }
                        }
                        if (!z8) {
                            z8 = true;
                        }
                        if (!z8) {
                            z8 = true;
                        }
                        if (!z8) {
                            z8 = true;
                        }
                        if (z8) {
                        }
                        if (!z9) {
                        }
                        if (!z9) {
                            i4 = this.lastSendState;
                            i5 = messageObject2.messageOwner.send_state;
                            if (i4 != i5) {
                            }
                        }
                        if (z9) {
                        }
                    } else {
                        r6 = 0;
                    }
                    this.user = r6;
                    this.chat = r6;
                    this.encryptedChat = r6;
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
                    if (z || (((i3 = i2) == this.unreadCount && z11 == this.markUnread) || (this.isDialogCell && System.currentTimeMillis() - this.lastDialogChangedTime <= 100))) {
                        z5 = z2;
                    } else {
                        ValueAnimator valueAnimator = this.countAnimator;
                        if (valueAnimator != null) {
                            valueAnimator.cancel();
                        }
                        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                        this.countAnimator = ofFloat;
                        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda2
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
                        if ((i3 == 0 || this.markUnread) && (this.markUnread || !z11)) {
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
                            String format = String.format("%d", Integer.valueOf(i3));
                            String format2 = String.format("%d", Integer.valueOf(this.unreadCount));
                            if (format.length() == format2.length()) {
                                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(format);
                                SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(format2);
                                SpannableStringBuilder spannableStringBuilder3 = new SpannableStringBuilder(format2);
                                int i18 = 0;
                                while (i18 < format.length()) {
                                    if (format.charAt(i18) == format2.charAt(i18)) {
                                        int i19 = i18 + 1;
                                        z7 = z2;
                                        spannableStringBuilder.setSpan(new EmptyStubSpan(), i18, i19, 0);
                                        spannableStringBuilder2.setSpan(new EmptyStubSpan(), i18, i19, 0);
                                    } else {
                                        z7 = z2;
                                        spannableStringBuilder3.setSpan(new EmptyStubSpan(), i18, i18 + 1, 0);
                                    }
                                    i18++;
                                    z2 = z7;
                                }
                                z5 = z2;
                                int max = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(format)));
                                this.countOldLayout = new StaticLayout(spannableStringBuilder, Theme.dialogs_countTextPaint, max, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                                this.countAnimationStableLayout = new StaticLayout(spannableStringBuilder3, Theme.dialogs_countTextPaint, max, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                                this.countAnimationInLayout = new StaticLayout(spannableStringBuilder2, Theme.dialogs_countTextPaint, max, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                            } else {
                                z5 = z2;
                                this.countOldLayout = this.countLayout;
                            }
                        } else {
                            z5 = z2;
                        }
                        this.countWidthOld = this.countWidth;
                        this.countLeftOld = this.countLeft;
                        this.countAnimationIncrement = this.unreadCount > i3;
                        this.countAnimator.start();
                    }
                    boolean z13 = this.reactionMentionCount == 0;
                    if (!z && z13 != z10) {
                        ValueAnimator valueAnimator2 = this.reactionsMentionsAnimator;
                        if (valueAnimator2 != null) {
                            valueAnimator2.cancel();
                        }
                        this.reactionsMentionsChangeProgress = 0.0f;
                        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                        this.reactionsMentionsAnimator = ofFloat2;
                        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda0
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
                        if (z13) {
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
                    z6 = z4;
                }
            } else {
                i2 = i11;
            }
            z2 = false;
            z3 = false;
            z4 = false;
            if (i == 0) {
            }
            this.user = r6;
            this.chat = r6;
            this.encryptedChat = r6;
            if (this.currentDialogFolderId == 0) {
            }
            if (j != 0) {
            }
            if (this.currentDialogFolderId == 0) {
            }
            if (z) {
            }
            z5 = z2;
            if (this.reactionMentionCount == 0) {
            }
            if (!z) {
            }
            ImageReceiver imageReceiver2 = this.avatarImage;
            TLRPC$Chat tLRPC$Chat22 = this.chat;
            imageReceiver2.setRoundRadius(AndroidUtilities.dp((!(tLRPC$Chat22 == null && tLRPC$Chat22.forum && this.currentDialogFolderId == 0 && !this.useFromUserAsAvatar) && (this.isSavedDialog || (tLRPC$User = this.user) == null || !tLRPC$User.self || !MessagesController.getInstance(this.currentAccount).savedViewAsChats)) ? 28.0f : 16.0f));
            z6 = z4;
        }
        boolean z14 = (this.isTopic || (getMeasuredWidth() == 0 && getMeasuredHeight() == 0)) ? z3 : true;
        if (!z6) {
            if (this.storyParams.currentState == 0) {
            }
            if (StoriesUtilities.getPredictiveUnreadState(MessagesController.getInstance(this.currentAccount).getStoriesController(), getDialogId()) == 0) {
            }
        }
        if (!z) {
            this.dialogMutedProgress = (this.dialogMuted || this.drawUnmute) ? 1.0f : 0.0f;
            ValueAnimator valueAnimator3 = this.countAnimator;
            if (valueAnimator3 != null) {
                valueAnimator3.cancel();
            }
        }
        invalidate();
        if (isForumCell() != isForumCell) {
            z5 = true;
        }
        if (z14) {
            if (this.attachedToWindow) {
                buildLayout();
            } else {
                this.updateLayout = true;
            }
        }
        updatePremiumBlocked(z);
        return z5;
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
    /* JADX WARN: Code restructure failed: missing block: B:362:0x0bee, code lost:
        if (r2.lastKnownTypingType >= 0) goto L212;
     */
    /* JADX WARN: Removed duplicated region for block: B:296:0x0a0e  */
    /* JADX WARN: Removed duplicated region for block: B:397:0x0c91  */
    /* JADX WARN: Removed duplicated region for block: B:400:0x0c98  */
    /* JADX WARN: Removed duplicated region for block: B:456:0x0e9d  */
    /* JADX WARN: Removed duplicated region for block: B:516:0x0f67  */
    /* JADX WARN: Removed duplicated region for block: B:558:0x0fd6  */
    /* JADX WARN: Removed duplicated region for block: B:559:0x0fd9  */
    /* JADX WARN: Removed duplicated region for block: B:571:0x0ffb  */
    /* JADX WARN: Removed duplicated region for block: B:575:0x1013  */
    /* JADX WARN: Removed duplicated region for block: B:580:0x1067  */
    /* JADX WARN: Removed duplicated region for block: B:586:0x107d  */
    /* JADX WARN: Removed duplicated region for block: B:603:0x10d0  */
    /* JADX WARN: Removed duplicated region for block: B:666:0x11d9  */
    /* JADX WARN: Removed duplicated region for block: B:668:0x122a  */
    /* JADX WARN: Removed duplicated region for block: B:724:0x13d4  */
    /* JADX WARN: Removed duplicated region for block: B:760:0x156a  */
    /* JADX WARN: Removed duplicated region for block: B:765:0x159a  */
    /* JADX WARN: Removed duplicated region for block: B:769:0x15aa  */
    /* JADX WARN: Removed duplicated region for block: B:784:0x15e6  */
    /* JADX WARN: Removed duplicated region for block: B:785:0x15e8  */
    /* JADX WARN: Removed duplicated region for block: B:789:0x15f6  */
    /* JADX WARN: Removed duplicated region for block: B:801:0x1617  */
    /* JADX WARN: Removed duplicated region for block: B:803:0x161b  */
    /* JADX WARN: Removed duplicated region for block: B:817:0x168a  */
    /* JADX WARN: Removed duplicated region for block: B:820:0x1693  */
    /* JADX WARN: Removed duplicated region for block: B:838:0x16dd  */
    /* JADX WARN: Removed duplicated region for block: B:867:0x175d  */
    /* JADX WARN: Removed duplicated region for block: B:876:0x17b4  */
    /* JADX WARN: Removed duplicated region for block: B:881:0x17c7  */
    /* JADX WARN: Removed duplicated region for block: B:889:0x17df  */
    /* JADX WARN: Removed duplicated region for block: B:897:0x1808  */
    /* JADX WARN: Removed duplicated region for block: B:908:0x1836  */
    /* JADX WARN: Removed duplicated region for block: B:914:0x184b  */
    /* JADX WARN: Removed duplicated region for block: B:924:0x1872  */
    /* JADX WARN: Removed duplicated region for block: B:934:0x1893  */
    /* JADX WARN: Removed duplicated region for block: B:951:? A[RETURN, SYNTHETIC] */
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
        float f;
        int i2;
        String str;
        int i3;
        RLottieDrawable rLottieDrawable;
        Paint paint;
        Canvas canvas2;
        float f2;
        float f3;
        int i4;
        boolean z;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic;
        boolean z2;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic2;
        PullForegroundDrawable pullForegroundDrawable;
        float f4;
        int i5;
        Paint paint2;
        Paint paint3;
        int i6;
        int i7;
        int i8;
        float f5;
        int i9;
        int i10;
        int i11;
        int i12;
        boolean z3;
        int dp;
        int dp2;
        int i13;
        DialogCellTags dialogCellTags;
        int i14;
        float dp3;
        float f6;
        int i15;
        int i16;
        float f7;
        float f8;
        int color3;
        float f9;
        StaticLayout staticLayout;
        CustomDialog customDialog;
        int i17;
        Paint paint4;
        PullForegroundDrawable pullForegroundDrawable2;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic3;
        if (this.currentDialogId == 0 && this.customDialog == null) {
            return;
        }
        if (!this.visibleOnScreen && !this.drawingForBlur) {
            return;
        }
        int i18 = 1;
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
            int i19 = color2;
            String str3 = string;
            if (this.swipeCanceled && (rLottieDrawable = this.lastDrawTranslationDrawable) != null) {
                this.translationDrawable = rLottieDrawable;
                i = this.lastDrawSwipeMessageStringId;
            } else {
                this.lastDrawTranslationDrawable = this.translationDrawable;
                this.lastDrawSwipeMessageStringId = i;
            }
            int i20 = i;
            if (!this.translationAnimationStarted && Math.abs(this.translationX) > AndroidUtilities.dp(43.0f)) {
                this.translationAnimationStarted = true;
                this.translationDrawable.setProgress(0.0f);
                this.translationDrawable.setCallback(this);
                this.translationDrawable.start();
            }
            float measuredWidth = getMeasuredWidth() + this.translationX;
            if (this.currentRevealProgress < 1.0f) {
                Theme.dialogs_pinnedPaint.setColor(color);
                f = measuredWidth;
                i2 = i20;
                str = str3;
                i3 = i19;
                canvas.drawRect(measuredWidth - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
                if (this.currentRevealProgress == 0.0f) {
                    if (Theme.dialogs_archiveDrawableRecolored) {
                        Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(Theme.key_chats_archiveBackground));
                        Theme.dialogs_archiveDrawableRecolored = false;
                    }
                    if (Theme.dialogs_hidePsaDrawableRecolored) {
                        Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                        RLottieDrawable rLottieDrawable2 = Theme.dialogs_hidePsaDrawable;
                        int i21 = Theme.key_chats_archiveBackground;
                        rLottieDrawable2.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(i21));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(i21));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(i21));
                        Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                        Theme.dialogs_hidePsaDrawableRecolored = false;
                    }
                }
            } else {
                f = measuredWidth;
                i2 = i20;
                str = str3;
                i3 = i19;
            }
            int measuredWidth2 = (getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.translationDrawable.getIntrinsicWidth() / 2);
            int measuredHeight = (getMeasuredHeight() - AndroidUtilities.dp(54.0f)) / 2;
            int intrinsicWidth = (this.translationDrawable.getIntrinsicWidth() / 2) + measuredWidth2;
            int intrinsicHeight = (this.translationDrawable.getIntrinsicHeight() / 2) + measuredHeight;
            if (this.currentRevealProgress > 0.0f) {
                canvas.save();
                canvas.clipRect(f - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                Theme.dialogs_pinnedPaint.setColor(i3);
                canvas.drawCircle(intrinsicWidth, intrinsicHeight, ((float) Math.sqrt((intrinsicWidth * intrinsicWidth) + ((intrinsicHeight - getMeasuredHeight()) * (intrinsicHeight - getMeasuredHeight())))) * AndroidUtilities.accelerateInterpolator.getInterpolation(this.currentRevealProgress), Theme.dialogs_pinnedPaint);
                canvas.restore();
                if (!Theme.dialogs_archiveDrawableRecolored) {
                    Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(Theme.key_chats_archivePinBackground));
                    Theme.dialogs_archiveDrawableRecolored = true;
                }
                if (!Theme.dialogs_hidePsaDrawableRecolored) {
                    Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                    RLottieDrawable rLottieDrawable3 = Theme.dialogs_hidePsaDrawable;
                    int i22 = Theme.key_chats_archivePinBackground;
                    rLottieDrawable3.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(i22));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(i22));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(i22));
                    Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                    Theme.dialogs_hidePsaDrawableRecolored = true;
                }
            }
            canvas.save();
            canvas.translate(measuredWidth2, measuredHeight);
            float f10 = this.currentRevealBounceProgress;
            if (f10 != 0.0f && f10 != 1.0f) {
                float interpolation = this.interpolator.getInterpolation(f10) + 1.0f;
                canvas.scale(interpolation, interpolation, this.translationDrawable.getIntrinsicWidth() / 2, this.translationDrawable.getIntrinsicHeight() / 2);
            }
            BaseCell.setDrawableBounds((Drawable) this.translationDrawable, 0, 0);
            this.translationDrawable.draw(canvas);
            canvas.restore();
            canvas.clipRect(f, 0.0f, getMeasuredWidth(), getMeasuredHeight());
            String str4 = str;
            int ceil = (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str4));
            if (this.swipeMessageTextId != i2 || this.swipeMessageWidth != getMeasuredWidth()) {
                this.swipeMessageTextId = i2;
                this.swipeMessageWidth = getMeasuredWidth();
                StaticLayout staticLayout2 = new StaticLayout(str4, Theme.dialogs_archiveTextPaint, Math.min(AndroidUtilities.dp(80.0f), ceil), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                this.swipeMessageTextLayout = staticLayout2;
                if (staticLayout2.getLineCount() > 1) {
                    this.swipeMessageTextLayout = new StaticLayout(str4, Theme.dialogs_archiveTextPaintSmall, Math.min(AndroidUtilities.dp(82.0f), ceil), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                }
            }
            if (this.swipeMessageTextLayout != null) {
                canvas.save();
                canvas.translate((getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.swipeMessageTextLayout.getWidth() / 2.0f), measuredHeight + AndroidUtilities.dp(38.0f) + (this.swipeMessageTextLayout.getLineCount() > 1 ? -AndroidUtilities.dp(4.0f) : 0.0f));
                this.swipeMessageTextLayout.draw(canvas);
                canvas.restore();
            }
            canvas.restore();
        } else {
            RLottieDrawable rLottieDrawable4 = this.translationDrawable;
            if (rLottieDrawable4 != null) {
                rLottieDrawable4.stop();
                this.translationDrawable.setProgress(0.0f);
                this.translationDrawable.setCallback(null);
                this.translationDrawable = null;
                this.translationAnimationStarted = false;
            }
        }
        if (this.translationX != 0.0f) {
            canvas.save();
            canvas.translate(this.translationX, 0.0f);
        }
        float dp4 = AndroidUtilities.dp(8.0f) * this.cornerProgress;
        if (this.isSelected) {
            this.rect.set(0.0f, 0.0f, getMeasuredWidth(), AndroidUtilities.lerp(getMeasuredHeight(), getCollapsedHeight(), this.rightFragmentOpenedProgress));
            this.rect.offset(0.0f, (-this.translateY) + this.collapseOffset);
            canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_tabletSeletedPaint);
        }
        canvas.save();
        canvas.translate(0.0f, (-this.rightFragmentOffset) * this.rightFragmentOpenedProgress);
        if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
            Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider), this.archiveBackgroundProgress, 1.0f));
            Theme.dialogs_pinnedPaint.setAlpha((int) (paint4.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
            canvas.drawRect(-this.xOffset, 0.0f, getMeasuredWidth(), getMeasuredHeight() - this.translateY, Theme.dialogs_pinnedPaint);
        } else if (getIsPinned() || this.drawPinBackground) {
            Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider));
            Theme.dialogs_pinnedPaint.setAlpha((int) (paint.getAlpha() * (1.0f - this.rightFragmentOpenedProgress)));
            canvas.drawRect(-this.xOffset, 0.0f, getMeasuredWidth(), getMeasuredHeight() - this.translateY, Theme.dialogs_pinnedPaint);
        }
        canvas.restore();
        this.updateHelper.updateAnimationValues();
        if (this.collapseOffset != 0.0f) {
            canvas.save();
            canvas.translate(0.0f, this.collapseOffset);
        }
        float f11 = this.rightFragmentOpenedProgress;
        if (f11 != 1.0f) {
            if (f11 != 0.0f) {
                float clamp = Utilities.clamp(f11 / 0.4f, 1.0f, 0.0f);
                if (SharedConfig.getDevicePerformanceClass() >= 2) {
                    f4 = 1.0f;
                    i17 = canvas.saveLayerAlpha(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + 1) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) ((1.0f - this.rightFragmentOpenedProgress) * 255.0f), 31);
                } else {
                    f4 = 1.0f;
                    int save = canvas.save();
                    canvas.clipRect(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + 1) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    i17 = save;
                }
                canvas.translate((-(getMeasuredWidth() - AndroidUtilities.dp(74.0f))) * 0.7f * this.rightFragmentOpenedProgress, 0.0f);
                i5 = i17;
            } else {
                f4 = 1.0f;
                i5 = -1;
            }
            if (this.translationX != 0.0f || this.cornerProgress != 0.0f) {
                canvas.save();
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                this.rect.set(getMeasuredWidth() - AndroidUtilities.dp(64.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                this.rect.offset(0.0f, -this.translateY);
                canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_pinnedPaint);
                if (this.isSelected) {
                    canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_tabletSeletedPaint);
                }
                if (this.currentDialogFolderId != 0 && (!SharedConfig.archiveHidden || this.archiveBackgroundProgress != 0.0f)) {
                    Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider), this.archiveBackgroundProgress, f4));
                    Theme.dialogs_pinnedPaint.setAlpha((int) (paint3.getAlpha() * (f4 - this.rightFragmentOpenedProgress)));
                    canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_pinnedPaint);
                } else if (getIsPinned() || this.drawPinBackground) {
                    Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider));
                    Theme.dialogs_pinnedPaint.setAlpha((int) (paint2.getAlpha() * (f4 - this.rightFragmentOpenedProgress)));
                    canvas.drawRoundRect(this.rect, dp4, dp4, Theme.dialogs_pinnedPaint);
                }
                canvas.restore();
            }
            if (this.translationX != 0.0f) {
                float f12 = this.cornerProgress;
                if (f12 < f4) {
                    float f13 = f12 + 0.10666667f;
                    this.cornerProgress = f13;
                    if (f13 > f4) {
                        this.cornerProgress = f4;
                    }
                    i4 = 1;
                }
                i4 = 0;
            } else {
                float f14 = this.cornerProgress;
                if (f14 > 0.0f) {
                    float f15 = f14 - 0.10666667f;
                    this.cornerProgress = f15;
                    if (f15 < 0.0f) {
                        this.cornerProgress = 0.0f;
                    }
                    i4 = 1;
                }
                i4 = 0;
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
                        paint5.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{-1, 0}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                        this.fadePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                    } else if (this.fadePaintBack == null) {
                        Paint paint6 = new Paint();
                        this.fadePaintBack = paint6;
                        paint6.setShader(new LinearGradient(0.0f, 0.0f, AndroidUtilities.dp(24.0f), 0.0f, new int[]{0, -1}, new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP));
                        this.fadePaintBack.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                    }
                    canvas.saveLayerAlpha(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight(), 255, 31);
                    int i23 = this.nameLeft;
                    canvas.clipRect(i23, 0, this.nameWidth + i23, getMeasuredHeight());
                }
                if (this.currentDialogFolderId != 0) {
                    TextPaint[] textPaintArr = Theme.dialogs_namePaint;
                    int i24 = this.paintIndex;
                    TextPaint textPaint = textPaintArr[i24];
                    TextPaint textPaint2 = textPaintArr[i24];
                    int color4 = Theme.getColor(Theme.key_chats_nameArchived, this.resourcesProvider);
                    textPaint2.linkColor = color4;
                    textPaint.setColor(color4);
                } else if (this.encryptedChat != null || ((customDialog = this.customDialog) != null && customDialog.type == 2)) {
                    TextPaint[] textPaintArr2 = Theme.dialogs_namePaint;
                    int i25 = this.paintIndex;
                    TextPaint textPaint3 = textPaintArr2[i25];
                    TextPaint textPaint4 = textPaintArr2[i25];
                    int color5 = Theme.getColor(Theme.key_chats_secretName, this.resourcesProvider);
                    textPaint4.linkColor = color5;
                    textPaint3.setColor(color5);
                } else {
                    TextPaint[] textPaintArr3 = Theme.dialogs_namePaint;
                    int i26 = this.paintIndex;
                    TextPaint textPaint5 = textPaintArr3[i26];
                    TextPaint textPaint6 = textPaintArr3[i26];
                    int color6 = Theme.getColor(Theme.key_chats_name, this.resourcesProvider);
                    textPaint6.linkColor = color6;
                    textPaint5.setColor(color6);
                }
                canvas.save();
                canvas.translate(this.nameLeft + this.nameLayoutTranslateX, dp5);
                SpoilerEffect.layoutDrawMaybe(this.nameLayout, canvas);
                StaticLayout staticLayout3 = this.nameLayout;
                i6 = i5;
                i7 = -1;
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout3, this.animatedEmojiStackName, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(0, staticLayout3.getPaint().getColor()));
                canvas.restore();
                if (!this.nameLayoutEllipsizeByGradient || this.nameLayoutFits) {
                    i18 = 1;
                } else {
                    canvas.save();
                    if (this.nameLayoutEllipsizeLeft) {
                        canvas.translate(this.nameLeft, 0.0f);
                        i18 = 1;
                        canvas.drawRect(0.0f, 0.0f, AndroidUtilities.dp(24.0f), getMeasuredHeight(), this.fadePaint);
                    } else {
                        i18 = 1;
                        canvas.translate((this.nameLeft + this.nameWidth) - AndroidUtilities.dp(24.0f), 0.0f);
                        canvas.drawRect(0.0f, 0.0f, AndroidUtilities.dp(24.0f), getMeasuredHeight(), this.fadePaintBack);
                    }
                    canvas.restore();
                    canvas.restore();
                }
            } else {
                i6 = i5;
                i7 = -1;
            }
            if (this.timeLayout != null && this.currentDialogFolderId == 0) {
                canvas.save();
                canvas.translate(this.timeLeft, this.timeTop);
                SpoilerEffect.layoutDrawMaybe(this.timeLayout, canvas);
                canvas.restore();
            }
            if (drawLock2()) {
                i8 = 2;
                Theme.dialogs_lock2Drawable.setBounds(this.lock2Left, this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / 2), this.lock2Left + Theme.dialogs_lock2Drawable.getIntrinsicWidth(), this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / 2) + Theme.dialogs_lock2Drawable.getIntrinsicHeight());
                Theme.dialogs_lock2Drawable.draw(canvas);
            } else {
                i8 = 2;
            }
            if (this.messageNameLayout == null || isForumCell()) {
                f5 = 0.0f;
                i9 = 1;
                i10 = 0;
            } else {
                if (this.currentDialogFolderId != 0) {
                    TextPaint textPaint7 = Theme.dialogs_messageNamePaint;
                    int color7 = Theme.getColor(Theme.key_chats_nameMessageArchived_threeLines, this.resourcesProvider);
                    textPaint7.linkColor = color7;
                    textPaint7.setColor(color7);
                } else if (this.draftMessage != null) {
                    TextPaint textPaint8 = Theme.dialogs_messageNamePaint;
                    int color8 = Theme.getColor(Theme.key_chats_draft, this.resourcesProvider);
                    textPaint8.linkColor = color8;
                    textPaint8.setColor(color8);
                } else {
                    TextPaint textPaint9 = Theme.dialogs_messageNamePaint;
                    int color9 = Theme.getColor(Theme.key_chats_nameMessage_threeLines, this.resourcesProvider);
                    textPaint9.linkColor = color9;
                    textPaint9.setColor(color9);
                }
                canvas.save();
                canvas.translate(this.messageNameLeft, this.messageNameTop);
                try {
                    SpoilerEffect.layoutDrawMaybe(this.messageNameLayout, canvas);
                    staticLayout = this.messageNameLayout;
                    i10 = 0;
                    i9 = 1;
                    f5 = 0.0f;
                } catch (Exception e) {
                    e = e;
                    f5 = 0.0f;
                    i9 = 1;
                    i10 = 0;
                }
                try {
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout, this.animatedEmojiStack2, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i18, staticLayout.getPaint().getColor()));
                } catch (Exception e2) {
                    e = e2;
                    FileLog.e(e);
                    canvas.restore();
                    if (this.messageLayout == null) {
                    }
                    if (this.buttonLayout != null) {
                    }
                    if (this.currentDialogFolderId != 0) {
                    }
                    if (this.drawUnmute) {
                    }
                    if (this.dialogsType == i12) {
                    }
                    if (!this.drawVerified) {
                    }
                    if (!this.drawReorder) {
                    }
                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                    Theme.dialogs_reorderDrawable.draw(canvas);
                    if (!this.drawError) {
                    }
                    canvas2 = canvas;
                    if (this.thumbsCount > 0) {
                    }
                    i13 = -1;
                    dialogCellTags = this.tags;
                    if (dialogCellTags != null) {
                    }
                    i14 = i6;
                    if (i14 != i13) {
                    }
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
                        i4 = 1;
                    }
                    if (this.rightFragmentOpenedProgress > f2) {
                        if (!this.isTopic) {
                        }
                        boolean z4 = z2;
                        RectF rectF = this.storyParams.originalAvatarRect;
                        int width = (int) (((rectF.left + rectF.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
                        RectF rectF2 = this.storyParams.originalAvatarRect;
                        drawCounter(canvas, z4, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width, (int) (((rectF2.left + rectF2.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
                    }
                    if (this.collapseOffset != f2) {
                    }
                    if (this.translationX != f2) {
                    }
                    if (this.drawArchive) {
                        canvas.save();
                        canvas2.translate(f2, (-this.translateY) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress));
                        canvas2.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
                        this.archivedChatsDrawable.draw(canvas2);
                        canvas.restore();
                    }
                    if (this.useSeparator) {
                    }
                    if (this.clipProgress != f2) {
                    }
                    z = this.drawReorder;
                    if (!z) {
                    }
                    if (!z) {
                    }
                    if (!this.archiveHidden) {
                    }
                }
                canvas.restore();
            }
            if (this.messageLayout == null) {
                if (this.currentDialogFolderId != 0) {
                    if (this.chat != null) {
                        TextPaint[] textPaintArr4 = Theme.dialogs_messagePaint;
                        int i27 = this.paintIndex;
                        TextPaint textPaint10 = textPaintArr4[i27];
                        TextPaint textPaint11 = textPaintArr4[i27];
                        int color10 = Theme.getColor(Theme.key_chats_nameMessageArchived, this.resourcesProvider);
                        textPaint11.linkColor = color10;
                        textPaint10.setColor(color10);
                    } else {
                        TextPaint[] textPaintArr5 = Theme.dialogs_messagePaint;
                        int i28 = this.paintIndex;
                        TextPaint textPaint12 = textPaintArr5[i28];
                        TextPaint textPaint13 = textPaintArr5[i28];
                        int color11 = Theme.getColor(Theme.key_chats_messageArchived, this.resourcesProvider);
                        textPaint13.linkColor = color11;
                        textPaint12.setColor(color11);
                    }
                } else {
                    TextPaint[] textPaintArr6 = Theme.dialogs_messagePaint;
                    int i29 = this.paintIndex;
                    TextPaint textPaint14 = textPaintArr6[i29];
                    TextPaint textPaint15 = textPaintArr6[i29];
                    int color12 = Theme.getColor(Theme.key_chats_message, this.resourcesProvider);
                    textPaint15.linkColor = color12;
                    textPaint14.setColor(color12);
                }
                float dp6 = AndroidUtilities.dp(14.0f);
                DialogUpdateHelper dialogUpdateHelper = this.updateHelper;
                if (dialogUpdateHelper.typingOutToTop) {
                    f7 = this.messageTop - (dialogUpdateHelper.typingProgres * dp6);
                } else {
                    f7 = this.messageTop + (dialogUpdateHelper.typingProgres * dp6);
                }
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    f7 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                }
                if (this.updateHelper.typingProgres != 1.0f) {
                    canvas.save();
                    canvas.translate(this.messageLeft, f7);
                    int alpha = this.messageLayout.getPaint().getAlpha();
                    this.messageLayout.getPaint().setAlpha((int) (alpha * (1.0f - this.updateHelper.typingProgres)));
                    if (!this.spoilers.isEmpty()) {
                        try {
                            canvas.save();
                            SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
                            SpoilerEffect.layoutDrawMaybe(this.messageLayout, canvas);
                            StaticLayout staticLayout4 = this.messageLayout;
                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout4, this.animatedEmojiStack, -0.075f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i8, staticLayout4.getPaint().getColor()));
                            canvas.restore();
                            for (int i30 = 0; i30 < this.spoilers.size(); i30++) {
                                SpoilerEffect spoilerEffect = this.spoilers.get(i30);
                                spoilerEffect.setColor(this.messageLayout.getPaint().getColor());
                                spoilerEffect.draw(canvas);
                            }
                        } catch (Exception e3) {
                            FileLog.e(e3);
                        }
                    } else {
                        SpoilerEffect.layoutDrawMaybe(this.messageLayout, canvas);
                        StaticLayout staticLayout5 = this.messageLayout;
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout5, this.animatedEmojiStack, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(i8, staticLayout5.getPaint().getColor()));
                    }
                    this.messageLayout.getPaint().setAlpha(alpha);
                    canvas.restore();
                }
                canvas.save();
                DialogUpdateHelper dialogUpdateHelper2 = this.updateHelper;
                if (dialogUpdateHelper2.typingOutToTop) {
                    f8 = this.messageTop + ((1.0f - dialogUpdateHelper2.typingProgres) * dp6);
                } else {
                    f8 = this.messageTop - ((1.0f - dialogUpdateHelper2.typingProgres) * dp6);
                }
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    f8 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                }
                canvas.translate(this.typingLeft, f8);
                StaticLayout staticLayout6 = this.typingLayout;
                if (staticLayout6 != null && this.updateHelper.typingProgres > f5) {
                    int alpha2 = staticLayout6.getPaint().getAlpha();
                    this.typingLayout.getPaint().setAlpha((int) (alpha2 * this.updateHelper.typingProgres));
                    this.typingLayout.draw(canvas);
                    this.typingLayout.getPaint().setAlpha(alpha2);
                }
                canvas.restore();
                if (this.typingLayout != null) {
                    int i31 = this.printingStringType;
                    if (i31 < 0) {
                        DialogUpdateHelper dialogUpdateHelper3 = this.updateHelper;
                        if (dialogUpdateHelper3.typingProgres > f5) {
                        }
                    }
                    if (i31 < 0) {
                        i31 = this.updateHelper.lastKnownTypingType;
                    }
                    StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(i31);
                    if (chatStatusDrawable != null) {
                        canvas.save();
                        chatStatusDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_chats_actionMessage), (int) (Color.alpha(color3) * this.updateHelper.typingProgres)));
                        DialogUpdateHelper dialogUpdateHelper4 = this.updateHelper;
                        if (dialogUpdateHelper4.typingOutToTop) {
                            f9 = this.messageTop + (dp6 * (1.0f - dialogUpdateHelper4.typingProgres));
                        } else {
                            f9 = this.messageTop - (dp6 * (1.0f - dialogUpdateHelper4.typingProgres));
                        }
                        if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                            f9 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                        }
                        i11 = 4;
                        if (i31 == i9 || i31 == 4) {
                            canvas.translate(this.statusDrawableLeft, f9 + (i31 == i9 ? AndroidUtilities.dp(1.0f) : 0));
                        } else {
                            canvas.translate(this.statusDrawableLeft, f9 + ((AndroidUtilities.dp(18.0f) - chatStatusDrawable.getIntrinsicHeight()) / 2.0f));
                        }
                        chatStatusDrawable.draw(canvas);
                        invalidate();
                        canvas.restore();
                    }
                }
                i11 = 4;
            } else {
                i11 = 4;
            }
            if (this.buttonLayout != null) {
                canvas.save();
                if (this.buttonBackgroundPaint == null) {
                    this.buttonBackgroundPaint = new Paint(i9);
                }
                if (this.canvasButton == null) {
                    CanvasButton canvasButton = new CanvasButton(this);
                    this.canvasButton = canvasButton;
                    canvasButton.setDelegate(new Runnable() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogCell.this.lambda$onDraw$2();
                        }
                    });
                    this.canvasButton.setLongPress(new Runnable() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            DialogCell.this.lambda$onDraw$3();
                        }
                    });
                }
                if (this.lastTopicMessageUnread && this.topMessageTopicEndIndex != this.topMessageTopicStartIndex && ((i16 = this.dialogsType) == 0 || i16 == 7 || i16 == 8)) {
                    this.canvasButton.setColor(ColorUtils.setAlphaComponent(this.currentMessagePaint.getColor(), Theme.isCurrentThemeDark() ? 36 : 26));
                    if (!this.buttonCreated) {
                        this.canvasButton.rewind();
                        int i32 = this.topMessageTopicEndIndex;
                        if (i32 != this.topMessageTopicStartIndex && i32 > 0) {
                            float f16 = this.messageTop;
                            if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                                f16 -= AndroidUtilities.dp(isForumCell() ? 10.0f : 11.0f);
                            }
                            RectF rectF3 = AndroidUtilities.rectTmp;
                            StaticLayout staticLayout7 = this.messageLayout;
                            rectF3.set(this.messageLeft + AndroidUtilities.dp(2.0f) + this.messageLayout.getPrimaryHorizontal(i10), f16, (this.messageLeft + staticLayout7.getPrimaryHorizontal(Math.min(staticLayout7.getText().length(), this.topMessageTopicEndIndex))) - AndroidUtilities.dp(3.0f), this.buttonTop - AndroidUtilities.dp(4.0f));
                            rectF3.inset(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(4.0f));
                            if (rectF3.right > rectF3.left) {
                                this.canvasButton.addRect(rectF3);
                            }
                        }
                        float lineLeft = this.buttonLayout.getLineLeft(i10);
                        RectF rectF4 = AndroidUtilities.rectTmp;
                        rectF4.set(this.buttonLeft + lineLeft + AndroidUtilities.dp(2.0f), this.buttonTop + AndroidUtilities.dp(2.0f), this.buttonLeft + lineLeft + this.buttonLayout.getLineWidth(i10) + AndroidUtilities.dp(12.0f), this.buttonTop + this.buttonLayout.getHeight());
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
                        StaticLayout staticLayout8 = this.buttonLayout;
                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout8, this.animatedEmojiStack3, -0.075f, this.spoilers2, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(3, staticLayout8.getPaint().getColor()));
                        canvas.restore();
                        for (int i33 = 0; i33 < this.spoilers2.size(); i33++) {
                            SpoilerEffect spoilerEffect2 = this.spoilers2.get(i33);
                            spoilerEffect2.setColor(this.buttonLayout.getPaint().getColor());
                            spoilerEffect2.draw(canvas);
                        }
                    } catch (Exception e4) {
                        FileLog.e(e4);
                    }
                } else {
                    SpoilerEffect.layoutDrawMaybe(this.buttonLayout, canvas);
                    StaticLayout staticLayout9 = this.buttonLayout;
                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, staticLayout9, this.animatedEmojiStack3, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f, getAdaptiveEmojiColorFilter(3, staticLayout9.getPaint().getColor()));
                }
                canvas.restore();
            }
            if (this.currentDialogFolderId != 0) {
                int i34 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                int i35 = this.lastStatusDrawableParams;
                if (i35 >= 0 && i35 != i34 && !this.statusDrawableAnimationInProgress) {
                    createStatusDrawableAnimator(i35, i34);
                }
                boolean z5 = this.statusDrawableAnimationInProgress;
                if (z5) {
                    i34 = this.animateToStatusDrawableParams;
                }
                boolean z6 = (i34 & 1) != 0;
                boolean z7 = (i34 & 2) != 0;
                boolean z8 = (i34 & i11) != 0;
                if (z5) {
                    int i36 = this.animateFromStatusDrawableParams;
                    boolean z9 = (i36 & 1) != 0;
                    boolean z10 = (i36 & 2) != 0;
                    boolean z11 = (i36 & i11) != 0;
                    if (!z6 && !z9 && z11 && !z10 && z7 && z8) {
                        drawCheckStatus(canvas, z6, z7, z8, true, this.statusDrawableProgress);
                        f2 = 0.0f;
                        f3 = 1.0f;
                        i18 = 1;
                        i12 = 2;
                    } else {
                        boolean z12 = z9;
                        f2 = 0.0f;
                        boolean z13 = z10;
                        f3 = 1.0f;
                        boolean z14 = z11;
                        i18 = 1;
                        i12 = 2;
                        drawCheckStatus(canvas, z12, z13, z14, false, 1.0f - this.statusDrawableProgress);
                        drawCheckStatus(canvas, z6, z7, z8, false, this.statusDrawableProgress);
                    }
                } else {
                    f2 = 0.0f;
                    f3 = 1.0f;
                    i18 = 1;
                    i12 = 2;
                    drawCheckStatus(canvas, z6, z7, z8, false, 1.0f);
                }
                this.lastStatusDrawableParams = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
            } else {
                f2 = 0.0f;
                f3 = 1.0f;
                i18 = 1;
                i12 = 2;
            }
            boolean z15 = !this.drawUnmute || this.dialogMuted;
            if (this.dialogsType == i12 && ((z15 || this.dialogMutedProgress > f2) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
                if (z15) {
                    float f17 = this.dialogMutedProgress;
                    if (f17 != f3) {
                        float f18 = f17 + 0.10666667f;
                        this.dialogMutedProgress = f18;
                        if (f18 > f3) {
                            this.dialogMutedProgress = f3;
                        } else {
                            invalidate();
                        }
                        float dp7 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                        float dp8 = AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                        if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                            dp8 -= AndroidUtilities.dp(isForumCell() ? 8.0f : 9.0f);
                        }
                        BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp7, dp8);
                        BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp7, dp8);
                        if (this.dialogMutedProgress != f3) {
                            canvas.save();
                            float f19 = this.dialogMutedProgress;
                            canvas.scale(f19, f19, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
                            if (this.drawUnmute) {
                                Theme.dialogs_unmuteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                                Theme.dialogs_unmuteDrawable.draw(canvas);
                                Theme.dialogs_unmuteDrawable.setAlpha(255);
                            } else {
                                Theme.dialogs_muteDrawable.setAlpha((int) (this.dialogMutedProgress * 255.0f));
                                Theme.dialogs_muteDrawable.draw(canvas);
                                Theme.dialogs_muteDrawable.setAlpha(255);
                            }
                            canvas.restore();
                        } else if (this.drawUnmute) {
                            Theme.dialogs_unmuteDrawable.draw(canvas);
                        } else {
                            Theme.dialogs_muteDrawable.draw(canvas);
                        }
                    }
                }
                if (!z15) {
                    float f20 = this.dialogMutedProgress;
                    if (f20 != f2) {
                        float f21 = f20 - 0.10666667f;
                        this.dialogMutedProgress = f21;
                        if (f21 < f2) {
                            this.dialogMutedProgress = f2;
                        } else {
                            invalidate();
                        }
                    }
                }
                float dp72 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                float dp82 = AndroidUtilities.dp(!SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
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
            } else if (!this.drawVerified) {
                float dp9 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f);
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
                    BaseCell.setDrawableBounds(drawable2, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f));
                    drawable2.draw(canvas);
                }
            } else if (this.drawScam != 0) {
                int dp11 = AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 15.0f);
                if (((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || isForumCell()) && hasTags()) {
                    dp11 -= AndroidUtilities.dp(9.0f);
                }
                BaseCell.setDrawableBounds((Drawable) (this.drawScam == i18 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, dp11);
                (this.drawScam == i18 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas);
            }
            if (!this.drawReorder || this.reorderIconProgress != f2) {
                Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                Theme.dialogs_reorderDrawable.draw(canvas);
            }
            if (!this.drawError) {
                Theme.dialogs_errorDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                this.rect.set(this.errorLeft, this.errorTop, i15 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
                RectF rectF6 = this.rect;
                float f22 = AndroidUtilities.density;
                canvas.drawRoundRect(rectF6, f22 * 11.5f, f22 * 11.5f, Theme.dialogs_errorPaint);
                BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
                Theme.dialogs_errorDrawable.draw(canvas);
            } else if (((this.drawCount || this.drawMention) && this.drawCount2) || this.countChangeProgress != f3 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f3) {
                if (this.isTopic) {
                    z3 = this.topicMuted;
                } else {
                    TLRPC$Chat tLRPC$Chat = this.chat;
                    z3 = (tLRPC$Chat != null && tLRPC$Chat.forum && this.forumTopic == null) ? !this.hasUnmutedTopics : this.dialogMuted;
                }
                canvas2 = canvas;
                drawCounter(canvas, z3, this.countTop, this.countLeft, this.countLeftOld, 1.0f, false);
                if (this.drawMention) {
                    Theme.dialogs_countPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                    this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp2 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                    Paint paint7 = (!z3 || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                    RectF rectF7 = this.rect;
                    float f23 = AndroidUtilities.density;
                    canvas2.drawRoundRect(rectF7, f23 * 11.5f, f23 * 11.5f, paint7);
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
                if (this.drawReactionMention || this.reactionsMentionsChangeProgress != f3) {
                    Theme.dialogs_reactionsCountPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                    this.rect.set(this.reactionMentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp + AndroidUtilities.dp(23.0f), this.countTop + AndroidUtilities.dp(23.0f));
                    Paint paint8 = Theme.dialogs_reactionsCountPaint;
                    canvas.save();
                    float f24 = this.reactionsMentionsChangeProgress;
                    if (f24 != f3) {
                        if (!this.drawReactionMention) {
                            f24 = f3 - f24;
                        }
                        canvas2.scale(f24, f24, this.rect.centerX(), this.rect.centerY());
                    }
                    RectF rectF8 = this.rect;
                    float f25 = AndroidUtilities.density;
                    canvas2.drawRoundRect(rectF8, f25 * 11.5f, f25 * 11.5f, paint8);
                    Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                    BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    Theme.dialogs_reactionsMentionDrawable.draw(canvas2);
                    canvas.restore();
                }
                if (this.thumbsCount > 0) {
                    float f26 = this.updateHelper.typingProgres;
                    if (f26 != f3) {
                        if (f26 > f2) {
                            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) ((f3 - f26) * 255.0f), 31);
                            if (this.updateHelper.typingOutToTop) {
                                dp3 = -AndroidUtilities.dp(14.0f);
                                f6 = this.updateHelper.typingProgres;
                            } else {
                                dp3 = AndroidUtilities.dp(14.0f);
                                f6 = this.updateHelper.typingProgres;
                            }
                            canvas2.translate(f2, dp3 * f6);
                        }
                        int i37 = 0;
                        while (i37 < this.thumbsCount) {
                            if (this.thumbImageSeen[i37]) {
                                if (this.thumbBackgroundPaint == null) {
                                    Paint paint9 = new Paint(i18);
                                    this.thumbBackgroundPaint = paint9;
                                    paint9.setShadowLayer(AndroidUtilities.dp(1.34f), f2, AndroidUtilities.dp(0.34f), 402653184);
                                    this.thumbBackgroundPaint.setColor(0);
                                }
                                RectF rectF9 = AndroidUtilities.rectTmp;
                                rectF9.set(this.thumbImage[i37].getImageX(), this.thumbImage[i37].getImageY(), this.thumbImage[i37].getImageX2(), this.thumbImage[i37].getImageY2());
                                canvas2.drawRoundRect(rectF9, this.thumbImage[i37].getRoundRadius()[0], this.thumbImage[i37].getRoundRadius()[i18], this.thumbBackgroundPaint);
                                this.thumbImage[i37].draw(canvas2);
                                if (this.drawSpoiler[i37]) {
                                    Path path = this.thumbPath;
                                    if (path == null) {
                                        this.thumbPath = new Path();
                                    } else {
                                        path.rewind();
                                    }
                                    this.thumbPath.addRoundRect(rectF9, this.thumbImage[i37].getRoundRadius()[0], this.thumbImage[i37].getRoundRadius()[i18], Path.Direction.CW);
                                    canvas.save();
                                    canvas2.clipPath(this.thumbPath);
                                    this.thumbSpoiler.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(i7) * 0.325f)));
                                    this.thumbSpoiler.setBounds((int) this.thumbImage[i37].getImageX(), (int) this.thumbImage[i37].getImageY(), (int) this.thumbImage[i37].getImageX2(), (int) this.thumbImage[i37].getImageY2());
                                    this.thumbSpoiler.draw(canvas2);
                                    invalidate();
                                    canvas.restore();
                                }
                                if (this.drawPlay[i37]) {
                                    BaseCell.setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage[i37].getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), (int) (this.thumbImage[i37].getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2)));
                                    Theme.dialogs_playDrawable.draw(canvas2);
                                }
                            }
                            i37++;
                            i7 = -1;
                        }
                        i13 = -1;
                        if (this.updateHelper.typingProgres > f2) {
                            canvas.restore();
                        }
                        dialogCellTags = this.tags;
                        if (dialogCellTags != null && !dialogCellTags.isEmpty()) {
                            canvas.save();
                            canvas2.translate(this.tagsLeft, (getMeasuredHeight() - AndroidUtilities.dp(21.66f)) - (this.useSeparator ? 1 : 0));
                            this.tags.draw(canvas2, this.tagsRight - this.tagsLeft);
                            canvas.restore();
                        }
                        i14 = i6;
                        if (i14 != i13) {
                            canvas2.restoreToCount(i14);
                        }
                    }
                }
                i13 = -1;
                dialogCellTags = this.tags;
                if (dialogCellTags != null) {
                    canvas.save();
                    canvas2.translate(this.tagsLeft, (getMeasuredHeight() - AndroidUtilities.dp(21.66f)) - (this.useSeparator ? 1 : 0));
                    this.tags.draw(canvas2, this.tagsRight - this.tagsLeft);
                    canvas.restore();
                }
                i14 = i6;
                if (i14 != i13) {
                }
            } else if (getIsPinned()) {
                Theme.dialogs_pinnedDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                Theme.dialogs_pinnedDrawable.draw(canvas);
            }
            canvas2 = canvas;
            if (this.thumbsCount > 0) {
            }
            i13 = -1;
            dialogCellTags = this.tags;
            if (dialogCellTags != null) {
            }
            i14 = i6;
            if (i14 != i13) {
            }
        } else {
            canvas2 = canvas;
            f2 = 0.0f;
            f3 = 1.0f;
            i4 = 0;
        }
        if (this.animatingArchiveAvatar) {
            canvas.save();
            float interpolation2 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + f3;
            canvas2.scale(interpolation2, interpolation2, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
        }
        if (this.drawAvatar && (!this.isTopic || (tLRPC$TL_forumTopic2 = this.forumTopic) == null || tLRPC$TL_forumTopic2.id != i18 || (pullForegroundDrawable = this.archivedChatsDrawable) == null || !pullForegroundDrawable.isDraw())) {
            StoriesUtilities.AvatarStoryParams avatarStoryParams2 = this.storyParams;
            avatarStoryParams2.drawHiddenStoriesAsSegments = this.currentDialogFolderId == 0;
            StoriesUtilities.drawAvatarWithStory(this.currentDialogId, canvas2, this.avatarImage, avatarStoryParams2);
        }
        if (this.animatingArchiveAvatar) {
            canvas.restore();
        }
        if (this.avatarImage.getVisible() && drawAvatarOverlays(canvas)) {
            i4 = 1;
        }
        if (this.rightFragmentOpenedProgress > f2 && this.currentDialogFolderId == 0) {
            if (!this.isTopic) {
                z2 = this.topicMuted;
            } else {
                TLRPC$Chat tLRPC$Chat2 = this.chat;
                z2 = (tLRPC$Chat2 != null && tLRPC$Chat2.forum && this.forumTopic == null) ? !this.hasUnmutedTopics : this.dialogMuted;
            }
            boolean z42 = z2;
            RectF rectF10 = this.storyParams.originalAvatarRect;
            int width2 = (int) (((rectF10.left + rectF10.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
            RectF rectF22 = this.storyParams.originalAvatarRect;
            drawCounter(canvas, z42, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width2, (int) (((rectF22.left + rectF22.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
        }
        if (this.collapseOffset != f2) {
            canvas.restore();
        }
        if (this.translationX != f2) {
            canvas.restore();
        }
        if (this.drawArchive && ((this.currentDialogFolderId != 0 || (this.isTopic && (tLRPC$TL_forumTopic = this.forumTopic) != null && tLRPC$TL_forumTopic.id == i18)) && this.translationX == f2 && this.archivedChatsDrawable != null)) {
            canvas.save();
            canvas2.translate(f2, (-this.translateY) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress));
            canvas2.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
            this.archivedChatsDrawable.draw(canvas2);
            canvas.restore();
        }
        if (this.useSeparator) {
            int dp12 = (this.fullSeparator || !(this.currentDialogFolderId == 0 || !this.archiveHidden || this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden)) ? 0 : AndroidUtilities.dp(this.messagePaddingStart);
            if (this.rightFragmentOpenedProgress != f3) {
                int alpha3 = Theme.dividerPaint.getAlpha();
                float f27 = this.rightFragmentOpenedProgress;
                if (f27 != f2) {
                    Theme.dividerPaint.setAlpha((int) (alpha3 * (f3 - f27)));
                }
                float measuredHeight2 = (getMeasuredHeight() - i18) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress);
                if (LocaleController.isRTL) {
                    canvas.drawLine(0.0f, measuredHeight2, getMeasuredWidth() - dp12, measuredHeight2, Theme.dividerPaint);
                } else {
                    canvas.drawLine(dp12, measuredHeight2, getMeasuredWidth(), measuredHeight2, Theme.dividerPaint);
                }
                if (this.rightFragmentOpenedProgress != f2) {
                    Theme.dividerPaint.setAlpha(alpha3);
                }
            }
        }
        if (this.clipProgress != f2) {
            if (Build.VERSION.SDK_INT != 24) {
                canvas.restore();
            } else {
                Theme.dialogs_pinnedPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                canvas.drawRect(0.0f, 0.0f, getMeasuredWidth(), this.topClip * this.clipProgress, Theme.dialogs_pinnedPaint);
                canvas.drawRect(0.0f, getMeasuredHeight() - ((int) (this.bottomClip * this.clipProgress)), getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
            }
        }
        z = this.drawReorder;
        if (!z || this.reorderIconProgress != f2) {
            if (!z) {
                float f28 = this.reorderIconProgress;
                if (f28 < f3) {
                    float f29 = f28 + 0.09411765f;
                    this.reorderIconProgress = f29;
                    if (f29 > f3) {
                        this.reorderIconProgress = f3;
                    }
                    i4 = 1;
                }
            } else {
                float f30 = this.reorderIconProgress;
                if (f30 > f2) {
                    float f31 = f30 - 0.09411765f;
                    this.reorderIconProgress = f31;
                    if (f31 < f2) {
                        this.reorderIconProgress = f2;
                    }
                    i4 = 1;
                }
            }
        }
        if (!this.archiveHidden) {
            float f32 = this.archiveBackgroundProgress;
            if (f32 > f2) {
                float f33 = f32 - 0.069565214f;
                this.archiveBackgroundProgress = f33;
                if (f33 < f2) {
                    this.archiveBackgroundProgress = f2;
                }
                if (this.avatarDrawable.getAvatarType() == 2) {
                    this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                }
                i4 = 1;
            }
            if (this.animatingArchiveAvatar) {
                float f34 = this.animatingArchiveAvatarProgress + 16.0f;
                this.animatingArchiveAvatarProgress = f34;
                if (f34 >= 170.0f) {
                    this.animatingArchiveAvatarProgress = 170.0f;
                    this.animatingArchiveAvatar = false;
                }
                i4 = 1;
            }
            if (!this.drawRevealBackground) {
                float f35 = this.currentRevealBounceProgress;
                if (f35 < f3) {
                    float f36 = f35 + 0.09411765f;
                    this.currentRevealBounceProgress = f36;
                    if (f36 > f3) {
                        this.currentRevealBounceProgress = f3;
                        i4 = 1;
                    }
                }
                float f37 = this.currentRevealProgress;
                if (f37 < f3) {
                    float f38 = f37 + 0.053333335f;
                    this.currentRevealProgress = f38;
                    if (f38 > f3) {
                        this.currentRevealProgress = f3;
                    }
                }
                i18 = i4;
            } else {
                if (this.currentRevealBounceProgress == f3) {
                    this.currentRevealBounceProgress = f2;
                    i4 = 1;
                }
                float f39 = this.currentRevealProgress;
                if (f39 > f2) {
                    float f40 = f39 - 0.053333335f;
                    this.currentRevealProgress = f40;
                    if (f40 < f2) {
                        this.currentRevealProgress = f2;
                    }
                }
                i18 = i4;
            }
            if (i18 == 0) {
                invalidate();
                return;
            }
            return;
        }
        float f41 = this.archiveBackgroundProgress;
        if (f41 < f3) {
            float f42 = f41 + 0.069565214f;
            this.archiveBackgroundProgress = f42;
            if (f42 > f3) {
                this.archiveBackgroundProgress = f3;
            }
            if (this.avatarDrawable.getAvatarType() == 2) {
                this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
            }
            i4 = 1;
        }
        if (this.animatingArchiveAvatar) {
        }
        if (!this.drawRevealBackground) {
        }
        if (i18 == 0) {
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

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0154, code lost:
        if (r8 > 0) goto L61;
     */
    /* JADX WARN: Removed duplicated region for block: B:185:0x0544  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x05ca  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x05d7  */
    /* JADX WARN: Removed duplicated region for block: B:202:0x05f6  */
    /* JADX WARN: Removed duplicated region for block: B:205:0x05fc  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x060f  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x0627  */
    /* JADX WARN: Removed duplicated region for block: B:221:0x0632  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x063f  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0162  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0186  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0193  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x01be  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01d4  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0236  */
    @Override // org.telegram.ui.Stories.StoriesListPlaceProvider.AvatarOverlaysView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean drawAvatarOverlays(Canvas canvas) {
        boolean z;
        float dp;
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
        float dp11;
        float dp12;
        CheckBox2 checkBox2;
        Drawable drawable;
        float f6 = this.premiumBlockedT.set(this.premiumBlocked);
        boolean z3 = false;
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
        } else if (this.isDialogCell && this.currentDialogFolderId == 0) {
            boolean z4 = (this.ttlPeriod <= 0 || isOnline() || this.hasCall) ? false : true;
            this.showTtl = z4;
            if (this.rightFragmentOpenedProgress != 1.0f && (z4 || this.ttlProgress > 0.0f)) {
                TimerDrawable timerDrawable = this.timerDrawable;
                if (timerDrawable != null) {
                    int time = timerDrawable.getTime();
                    int i = this.ttlPeriod;
                    if (time != i) {
                    }
                    if (this.timerPaint == null) {
                        this.timerPaint = new Paint(1);
                        Paint paint = new Paint(1);
                        this.timerPaint2 = paint;
                        paint.setColor(838860800);
                    }
                    int imageY2 = (int) (this.avatarImage.getImageY2() - AndroidUtilities.dp(9.0f));
                    if (!LocaleController.isRTL) {
                        dp12 = this.storyParams.originalAvatarRect.left + AndroidUtilities.dp(9.0f);
                    } else {
                        dp12 = this.storyParams.originalAvatarRect.right - AndroidUtilities.dp(9.0f);
                    }
                    int i2 = (int) dp12;
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
                    float f9 = i2;
                    float f10 = imageY2;
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
                int imageY22 = (int) (this.avatarImage.getImageY2() - AndroidUtilities.dp(9.0f));
                if (!LocaleController.isRTL) {
                }
                int i22 = (int) dp12;
                this.timerDrawable.setBounds(0, 0, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(22.0f));
                this.timerDrawable.setTime(this.ttlPeriod);
                if (!this.avatarImage.updateThumbShaderMatrix()) {
                }
                canvas.save();
                float f82 = this.ttlProgress * (1.0f - this.rightFragmentOpenedProgress);
                checkBox2 = this.checkBox;
                if (checkBox2 != null) {
                }
                float f92 = i22;
                float f102 = imageY22;
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
            float f11 = 8.0f;
            if (tLRPC$User != null && !MessagesController.isSupportUser(tLRPC$User) && !this.user.bot) {
                boolean isOnline = isOnline();
                this.wasDrawnOnline = isOnline;
                if (isOnline || this.onlineProgress != 0.0f) {
                    int dp13 = (int) (this.storyParams.originalAvatarRect.bottom - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6.0f : 6.0f));
                    if (LocaleController.isRTL) {
                        float f12 = this.storyParams.originalAvatarRect.left;
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f7 = 6.0f;
                        }
                        dp11 = f12 + AndroidUtilities.dp(f7);
                    } else {
                        float f13 = this.storyParams.originalAvatarRect.right;
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f7 = 6.0f;
                        }
                        dp11 = f13 - AndroidUtilities.dp(f7);
                    }
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                    float f14 = (int) dp11;
                    float f15 = dp13;
                    canvas.drawCircle(f14, f15, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_chats_onlineCircle, this.resourcesProvider));
                    canvas.drawCircle(f14, f15, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    if (isOnline) {
                        float f16 = this.onlineProgress;
                        if (f16 < 1.0f) {
                            float f17 = f16 + 0.10666667f;
                            this.onlineProgress = f17;
                            if (f17 > 1.0f) {
                                this.onlineProgress = 1.0f;
                            }
                            z3 = true;
                        }
                    } else {
                        float f18 = this.onlineProgress;
                        if (f18 > 0.0f) {
                            float f19 = f18 - 0.10666667f;
                            this.onlineProgress = f19;
                            if (f19 < 0.0f) {
                                this.onlineProgress = 0.0f;
                            }
                            z3 = true;
                        }
                    }
                    if (!this.showTtl) {
                    }
                    this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                    return z2;
                }
                z = z3;
            } else {
                TLRPC$Chat tLRPC$Chat = this.chat;
                if (tLRPC$Chat != null) {
                    boolean z5 = tLRPC$Chat.call_active && tLRPC$Chat.call_not_empty;
                    this.hasCall = z5;
                    if ((z5 || this.chatCallProgress != 0.0f) && this.rightFragmentOpenedProgress < 1.0f) {
                        CheckBox2 checkBox22 = this.checkBox;
                        float progress = (checkBox22 == null || !checkBox22.isChecked()) ? 1.0f : 1.0f - this.checkBox.getProgress();
                        int dp14 = (int) (this.storyParams.originalAvatarRect.bottom - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6.0f : 6.0f));
                        if (LocaleController.isRTL) {
                            dp = this.storyParams.originalAvatarRect.left + AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 6.0f);
                        } else {
                            dp = this.storyParams.originalAvatarRect.right - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 6.0f);
                        }
                        int i3 = (int) dp;
                        if (this.rightFragmentOpenedProgress != 0.0f) {
                            canvas.save();
                            float f20 = 1.0f - this.rightFragmentOpenedProgress;
                            canvas.scale(f20, f20, i3, dp14);
                        }
                        Paint paint2 = Theme.dialogs_onlineCirclePaint;
                        int i4 = Theme.key_windowBackgroundWhite;
                        paint2.setColor(Theme.getColor(i4, this.resourcesProvider));
                        float f21 = i3;
                        float f22 = dp14;
                        canvas.drawCircle(f21, f22, AndroidUtilities.dp(11.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_chats_onlineCircle, this.resourcesProvider));
                        canvas.drawCircle(f21, f22, AndroidUtilities.dp(9.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(i4, this.resourcesProvider));
                        if (!LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                            this.innerProgress = 0.65f;
                        }
                        int i5 = this.progressStage;
                        if (i5 == 0) {
                            dp2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            dp9 = AndroidUtilities.dp(3.0f);
                            dp10 = AndroidUtilities.dp(2.0f);
                            f4 = this.innerProgress;
                        } else {
                            if (i5 == 1) {
                                dp2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp7 = AndroidUtilities.dp(1.0f);
                                dp8 = AndroidUtilities.dp(4.0f);
                                f3 = this.innerProgress;
                            } else {
                                if (i5 == 2) {
                                    dp2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                    dp5 = AndroidUtilities.dp(5.0f);
                                    dp6 = AndroidUtilities.dp(4.0f);
                                    f2 = this.innerProgress;
                                } else {
                                    if (i5 == 3) {
                                        dp2 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                        dp3 = AndroidUtilities.dp(1.0f);
                                        dp4 = AndroidUtilities.dp(2.0f);
                                        f = this.innerProgress;
                                    } else if (i5 == 4) {
                                        dp2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                        dp9 = AndroidUtilities.dp(3.0f);
                                        dp10 = AndroidUtilities.dp(2.0f);
                                        f4 = this.innerProgress;
                                    } else if (i5 == 5) {
                                        dp2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                        dp7 = AndroidUtilities.dp(1.0f);
                                        dp8 = AndroidUtilities.dp(4.0f);
                                        f3 = this.innerProgress;
                                    } else if (i5 == 6) {
                                        dp2 = (AndroidUtilities.dp(4.0f) * this.innerProgress) + AndroidUtilities.dp(1.0f);
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
                                    if (this.chatCallProgress >= 1.0f || progress < 1.0f) {
                                        canvas.save();
                                        float f23 = this.chatCallProgress;
                                        canvas.scale(f23 * progress, f23 * progress, f21, f22);
                                    }
                                    this.rect.set(i3 - AndroidUtilities.dp(1.0f), f22 - dp2, i3 + AndroidUtilities.dp(1.0f), dp2 + f22);
                                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                    float f24 = f22 - f5;
                                    float f25 = f22 + f5;
                                    this.rect.set(i3 - AndroidUtilities.dp(5.0f), f24, i3 - AndroidUtilities.dp(3.0f), f25);
                                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                    this.rect.set(AndroidUtilities.dp(3.0f) + i3, f24, i3 + AndroidUtilities.dp(5.0f), f25);
                                    canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                    if (this.chatCallProgress >= 1.0f || progress < 1.0f) {
                                        canvas.restore();
                                    }
                                    if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                                        z = false;
                                        z2 = true;
                                    } else {
                                        float f26 = this.innerProgress + 0.04f;
                                        this.innerProgress = f26;
                                        if (f26 >= 1.0f) {
                                            this.innerProgress = 0.0f;
                                            z2 = true;
                                            int i6 = this.progressStage + 1;
                                            this.progressStage = i6;
                                            if (i6 >= 8) {
                                                this.progressStage = 0;
                                            }
                                        } else {
                                            z2 = true;
                                        }
                                        z = true;
                                    }
                                    if (!this.hasCall) {
                                        float f27 = this.chatCallProgress;
                                        if (f27 < 1.0f) {
                                            float f28 = f27 + 0.10666667f;
                                            this.chatCallProgress = f28;
                                            if (f28 > 1.0f) {
                                                this.chatCallProgress = 1.0f;
                                            }
                                        }
                                    } else {
                                        float f29 = this.chatCallProgress;
                                        if (f29 > 0.0f) {
                                            float f30 = f29 - 0.10666667f;
                                            this.chatCallProgress = f30;
                                            if (f30 < 0.0f) {
                                                this.chatCallProgress = 0.0f;
                                            }
                                        }
                                    }
                                    if (this.rightFragmentOpenedProgress != 0.0f) {
                                        canvas.restore();
                                    }
                                    if (!this.showTtl) {
                                        float f31 = this.ttlProgress;
                                        if (f31 < 1.0f) {
                                            this.ttlProgress = f31 + 0.10666667f;
                                        }
                                        z2 = z;
                                    } else {
                                        float f32 = this.ttlProgress;
                                        if (f32 > 0.0f) {
                                            this.ttlProgress = f32 - 0.10666667f;
                                        }
                                        z2 = z;
                                    }
                                    this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                                    return z2;
                                }
                                f5 = dp5 - (dp6 * f2);
                                if (this.chatCallProgress >= 1.0f) {
                                }
                                canvas.save();
                                float f232 = this.chatCallProgress;
                                canvas.scale(f232 * progress, f232 * progress, f21, f22);
                                this.rect.set(i3 - AndroidUtilities.dp(1.0f), f22 - dp2, i3 + AndroidUtilities.dp(1.0f), dp2 + f22);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                float f242 = f22 - f5;
                                float f252 = f22 + f5;
                                this.rect.set(i3 - AndroidUtilities.dp(5.0f), f242, i3 - AndroidUtilities.dp(3.0f), f252);
                                canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                                this.rect.set(AndroidUtilities.dp(3.0f) + i3, f242, i3 + AndroidUtilities.dp(5.0f), f252);
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
                                if (!this.showTtl) {
                                }
                                this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                                return z2;
                            }
                            f5 = (dp8 * f3) + dp7;
                            if (this.chatCallProgress >= 1.0f) {
                            }
                            canvas.save();
                            float f2322 = this.chatCallProgress;
                            canvas.scale(f2322 * progress, f2322 * progress, f21, f22);
                            this.rect.set(i3 - AndroidUtilities.dp(1.0f), f22 - dp2, i3 + AndroidUtilities.dp(1.0f), dp2 + f22);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            float f2422 = f22 - f5;
                            float f2522 = f22 + f5;
                            this.rect.set(i3 - AndroidUtilities.dp(5.0f), f2422, i3 - AndroidUtilities.dp(3.0f), f2522);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            this.rect.set(AndroidUtilities.dp(3.0f) + i3, f2422, i3 + AndroidUtilities.dp(5.0f), f2522);
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
                            if (!this.showTtl) {
                            }
                            this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                            return z2;
                        }
                        f5 = dp9 - (dp10 * f4);
                        if (this.chatCallProgress >= 1.0f) {
                        }
                        canvas.save();
                        float f23222 = this.chatCallProgress;
                        canvas.scale(f23222 * progress, f23222 * progress, f21, f22);
                        this.rect.set(i3 - AndroidUtilities.dp(1.0f), f22 - dp2, i3 + AndroidUtilities.dp(1.0f), dp2 + f22);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        float f24222 = f22 - f5;
                        float f25222 = f22 + f5;
                        this.rect.set(i3 - AndroidUtilities.dp(5.0f), f24222, i3 - AndroidUtilities.dp(3.0f), f25222);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        this.rect.set(AndroidUtilities.dp(3.0f) + i3, f24222, i3 + AndroidUtilities.dp(5.0f), f25222);
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
                        if (!this.showTtl) {
                        }
                        this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                        return z2;
                    }
                }
                z = false;
            }
            z2 = true;
            if (!this.showTtl) {
            }
            this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
            return z2;
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
        int i4 = 255;
        if (z2) {
            if (this.counterPaintOutline == null) {
                Paint paint2 = new Paint();
                this.counterPaintOutline = paint2;
                paint2.setStyle(Paint.Style.STROKE);
                this.counterPaintOutline.setStrokeWidth(AndroidUtilities.dp(2.0f));
                this.counterPaintOutline.setStrokeJoin(Paint.Join.ROUND);
                this.counterPaintOutline.setStrokeCap(Paint.Cap.ROUND);
            }
            this.counterPaintOutline.setColor(ColorUtils.blendARGB(Theme.getColor(Theme.key_windowBackgroundWhite), ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_chats_pinnedOverlay), 255), Color.alpha(color) / 255.0f));
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
            canvas.save();
            float f10 = (interpolation + f2) * f;
            canvas.scale(f10, f10, this.rect.centerX(), this.rect.centerY());
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
            float f11 = alpha;
            Theme.dialogs_countTextPaint.setAlpha((int) (f11 * f6));
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
                Theme.dialogs_countTextPaint.setAlpha((int) (f11 * f7));
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
        this.statusDrawableAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: org.telegram.ui.Cells.DialogCell$$ExternalSyntheticLambda1
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
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R.id.acc_action_chat_preview, LocaleController.getString("AccActionChatPreview", R.string.AccActionChatPreview)));
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
            sb.append(LocaleController.getString("ArchivedChats", R.string.ArchivedChats));
            sb.append(". ");
        } else {
            if (this.encryptedChat != null) {
                sb.append(LocaleController.getString("AccDescrSecretChat", R.string.AccDescrSecretChat));
                sb.append(". ");
            }
            if (this.isTopic && this.forumTopic != null) {
                sb.append(LocaleController.getString("AccDescrTopic", R.string.AccDescrTopic));
                sb.append(". ");
                sb.append(this.forumTopic.title);
                sb.append(". ");
            } else {
                TLRPC$User tLRPC$User = this.user;
                if (tLRPC$User != null) {
                    if (UserObject.isReplyUser(tLRPC$User)) {
                        sb.append(LocaleController.getString("RepliesTitle", R.string.RepliesTitle));
                    } else if (UserObject.isAnonymous(this.user)) {
                        sb.append(LocaleController.getString(R.string.AnonymousForward));
                    } else {
                        if (this.user.bot) {
                            sb.append(LocaleController.getString("Bot", R.string.Bot));
                            sb.append(". ");
                        }
                        TLRPC$User tLRPC$User2 = this.user;
                        if (tLRPC$User2.self) {
                            sb.append(LocaleController.getString("SavedMessages", R.string.SavedMessages));
                        } else {
                            sb.append(ContactsController.formatName(tLRPC$User2.first_name, tLRPC$User2.last_name));
                        }
                    }
                    sb.append(". ");
                } else {
                    TLRPC$Chat tLRPC$Chat = this.chat;
                    if (tLRPC$Chat != null) {
                        if (tLRPC$Chat.broadcast) {
                            sb.append(LocaleController.getString("AccDescrChannel", R.string.AccDescrChannel));
                        } else {
                            sb.append(LocaleController.getString("AccDescrGroup", R.string.AccDescrGroup));
                        }
                        sb.append(". ");
                        sb.append(this.chat.title);
                        sb.append(". ");
                    }
                }
            }
        }
        if (this.drawVerified) {
            sb.append(LocaleController.getString("AccDescrVerified", R.string.AccDescrVerified));
            sb.append(". ");
        }
        if (this.dialogMuted) {
            sb.append(LocaleController.getString("AccDescrNotificationsMuted", R.string.AccDescrNotificationsMuted));
            sb.append(". ");
        }
        if (isOnline()) {
            sb.append(LocaleController.getString("AccDescrUserOnline", R.string.AccDescrUserOnline));
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
            sb.append(LocaleController.getString("AccDescrMentionReaction", R.string.AccDescrMentionReaction));
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
        MessageObject messageObject = this.message;
        if (messageObject == null) {
            return;
        }
        String restrictionReason = MessagesController.getRestrictionReason(messageObject.messageOwner.restriction_reason);
        ArrayList<MessageObject> arrayList = this.groupMessages;
        if (arrayList != null && arrayList.size() > 1 && TextUtils.isEmpty(restrictionReason) && this.currentDialogFolderId == 0 && this.encryptedChat == null) {
            this.thumbsCount = 0;
            this.hasVideoThumb = false;
            Collections.sort(this.groupMessages, Comparator$-CC.comparingInt(DialogCell$$ExternalSyntheticLambda5.INSTANCE));
            for (int i = 0; i < Math.min(3, this.groupMessages.size()); i++) {
                MessageObject messageObject2 = this.groupMessages.get(i);
                if (messageObject2 != null && !messageObject2.needDrawBluredPreview() && (messageObject2.isPhoto() || messageObject2.isNewGif() || messageObject2.isVideo() || messageObject2.isRoundVideo() || messageObject2.isStoryMedia())) {
                    String str = messageObject2.isWebpage() ? messageObject2.messageOwner.media.webpage.type : null;
                    if (!"app".equals(str) && !"profile".equals(str) && !"article".equals(str) && (str == null || !str.startsWith("telegram_"))) {
                        setThumb(i, messageObject2);
                    }
                }
            }
            return;
        }
        MessageObject messageObject3 = this.message;
        if (messageObject3 == null || this.currentDialogFolderId != 0) {
            return;
        }
        this.thumbsCount = 0;
        this.hasVideoThumb = false;
        if (messageObject3.needDrawBluredPreview()) {
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
        TLRPC$PhotoSize closestPhotoSizeWithSize2 = FileLoader.getClosestPhotoSizeWithSize(arrayList, AndroidUtilities.getPhotoSize());
        if (closestPhotoSizeWithSize == closestPhotoSizeWithSize2) {
            closestPhotoSizeWithSize2 = null;
        }
        if (closestPhotoSizeWithSize2 == null || DownloadController.getInstance(this.currentAccount).canDownloadMedia(messageObject.messageOwner) == 0) {
            closestPhotoSizeWithSize2 = closestPhotoSizeWithSize;
        }
        if (closestPhotoSizeWithSize != null) {
            this.hasVideoThumb = this.hasVideoThumb || messageObject.isVideo() || messageObject.isRoundVideo();
            int i2 = this.thumbsCount;
            if (i2 < 3) {
                this.thumbsCount = i2 + 1;
                this.drawPlay[i] = (messageObject.isVideo() || messageObject.isRoundVideo()) && !messageObject.hasMediaSpoilers();
                this.drawSpoiler[i] = messageObject.hasMediaSpoilers();
                int i3 = (messageObject.type != 1 || closestPhotoSizeWithSize2 == null) ? 0 : closestPhotoSizeWithSize2.size;
                String str = messageObject.hasMediaSpoilers() ? "5_5_b" : "20_20";
                this.thumbImage[i].setImage(ImageLocation.getForObject(closestPhotoSizeWithSize2, tLObject), str, ImageLocation.getForObject(closestPhotoSizeWithSize, tLObject), str, i3, null, messageObject, 0);
                this.thumbImage[i].setRoundRadius(AndroidUtilities.dp(messageObject.isRoundVideo() ? 18.0f : 2.0f));
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
                return UserObject.getFirstName(tLRPC$User).replace("\n", "");
            }
            if (chat != null) {
                return chat.title.replace("\n", "");
            }
            return null;
        } else if (this.message.isOutOwner()) {
            return LocaleController.getString("FromYou", R.string.FromYou);
        } else {
            if (!this.isSavedDialog && (messageObject = this.message) != null && (tLRPC$Message2 = messageObject.messageOwner) != null && (tLRPC$Message2.from_id instanceof TLRPC$TL_peerUser) && (user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.messageOwner.from_id.user_id))) != null) {
                return UserObject.getFirstName(user).replace("\n", "");
            }
            MessageObject messageObject3 = this.message;
            if (messageObject3 == null || (tLRPC$Message = messageObject3.messageOwner) == null || (tLRPC$MessageFwdHeader = tLRPC$Message.fwd_from) == null || (str2 = tLRPC$MessageFwdHeader.from_name) == null) {
                if (tLRPC$User == null) {
                    return (chat == null || (str = chat.title) == null) ? "DELETED" : str.replace("\n", "");
                } else if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                    if (UserObject.isDeleted(tLRPC$User)) {
                        return LocaleController.getString("HiddenName", R.string.HiddenName);
                    }
                    return ContactsController.formatName(tLRPC$User.first_name, tLRPC$User.last_name).replace("\n", "");
                } else {
                    return UserObject.getFirstName(tLRPC$User).replace("\n", "");
                }
            }
            return str2;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v0, types: [org.telegram.ui.Cells.DialogCell, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r13v5, types: [android.text.SpannableStringBuilder, java.lang.CharSequence, android.text.Spannable] */
    /* JADX WARN: Type inference failed for: r13v6, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r13v7, types: [java.lang.CharSequence] */
    public SpannableStringBuilder getMessageStringFormatted(int i, String str, CharSequence charSequence, boolean z) {
        TLRPC$Message tLRPC$Message;
        String charSequence2;
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
            } else {
                str2 = captionMessage.isPhoto() ? "🖼 " : "📎 ";
            }
            if (captionMessage.hasHighlightedWords() && !TextUtils.isEmpty(captionMessage.messageOwner.message)) {
                CharSequence charSequence7 = captionMessage.messageTrimmedToHighlight;
                int measuredWidth = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 24);
                if (this.hasNameInMessage) {
                    if (!TextUtils.isEmpty(charSequence)) {
                        measuredWidth = (int) (measuredWidth - this.currentMessagePaint.measureText(charSequence.toString()));
                    }
                    measuredWidth = (int) (measuredWidth - this.currentMessagePaint.measureText(": "));
                }
                if (measuredWidth > 0) {
                    charSequence7 = AndroidUtilities.ellipsizeCenterEnd(charSequence7, captionMessage.highlightedWords.get(0), measuredWidth, this.currentMessagePaint, 130).toString();
                }
                return new SpannableStringBuilder(str2).append(charSequence7);
            }
            if (charSequence6.length() > 150) {
                charSequence6 = charSequence6.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(charSequence6);
            captionMessage.spoilLoginCode();
            MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, charSequence6, spannableStringBuilder, 264);
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
                charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("📊 \u2068%s\u2069", tLRPC$TL_messageMediaPoll.poll.question) : String.format("📊 %s", tLRPC$TL_messageMediaPoll.poll.question);
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaGame) {
                charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("🎮 \u2068%s\u2069", tLRPC$MessageMedia.game.title) : String.format("🎮 %s", tLRPC$MessageMedia.game.title);
            } else if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaInvoice) {
                charSequence2 = tLRPC$MessageMedia.title;
            } else if (messageObject3.type == 14) {
                charSequence2 = Build.VERSION.SDK_INT >= 18 ? String.format("🎧 \u2068%s - %s\u2069", messageObject3.getMusicAuthor(), this.message.getMusicTitle()) : String.format("🎧 %s - %s", messageObject3.getMusicAuthor(), this.message.getMusicTitle());
            } else if (this.thumbsCount > 1) {
                if (this.hasVideoThumb) {
                    ArrayList<MessageObject> arrayList2 = this.groupMessages;
                    formatPluralString = LocaleController.formatPluralString("Media", arrayList2 == null ? 0 : arrayList2.size(), new Object[0]);
                } else {
                    ArrayList<MessageObject> arrayList3 = this.groupMessages;
                    formatPluralString = LocaleController.formatPluralString("Photos", arrayList3 == null ? 0 : arrayList3.size(), new Object[0]);
                }
                charSequence2 = formatPluralString;
                i2 = Theme.key_chats_actionMessage;
            } else {
                charSequence2 = charSequence4.toString();
                i2 = Theme.key_chats_actionMessage;
            }
            CharSequence replace = charSequence2.replace('\n', ' ');
            if (z) {
                replace = applyThumbs(replace);
            }
            SpannableStringBuilder formatInternal = formatInternal(i, replace, charSequence);
            if (isForumCell()) {
                return formatInternal;
            }
            try {
                formatInternal.setSpan(new ForegroundColorSpanThemable(i2, this.resourcesProvider), this.hasNameInMessage ? charSequence.length() + 2 : 0, formatInternal.length(), 33);
                return formatInternal;
            } catch (Exception e) {
                FileLog.e(e);
                return formatInternal;
            }
        } else {
            MessageObject messageObject4 = this.message;
            CharSequence charSequence8 = messageObject4.messageOwner.message;
            if (charSequence8 != null) {
                if (messageObject4.hasHighlightedWords()) {
                    CharSequence charSequence9 = this.message.messageTrimmedToHighlight;
                    if (charSequence9 != null) {
                        charSequence8 = charSequence9;
                    }
                    int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 10);
                    if (this.hasNameInMessage) {
                        if (!TextUtils.isEmpty(charSequence)) {
                            measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(charSequence.toString()));
                        }
                        measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(": "));
                    }
                    if (measuredWidth2 > 0) {
                        charSequence8 = AndroidUtilities.ellipsizeCenterEnd(charSequence8, this.message.highlightedWords.get(0), measuredWidth2, this.currentMessagePaint, 130).toString();
                    }
                } else {
                    if (charSequence8.length() > 150) {
                        charSequence8 = charSequence8.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                    }
                    charSequence8 = AndroidUtilities.replaceNewLines(charSequence8);
                }
                ?? spannableStringBuilder2 = new SpannableStringBuilder(charSequence8);
                MessageObject messageObject5 = this.message;
                if (messageObject5 != null) {
                    messageObject5.spoilLoginCode();
                }
                MediaDataController.addTextStyleRuns(this.message, (Spannable) spannableStringBuilder2, 264);
                MessageObject messageObject6 = this.message;
                if (messageObject6 != null && (tLRPC$Message = messageObject6.messageOwner) != null) {
                    ArrayList<TLRPC$MessageEntity> arrayList4 = tLRPC$Message.entities;
                    TextPaint textPaint2 = this.currentMessagePaint;
                    MediaDataController.addAnimatedEmojiSpans(arrayList4, spannableStringBuilder2, textPaint2 != null ? textPaint2.getFontMetricsInt() : null);
                }
                if (z) {
                    spannableStringBuilder2 = applyThumbs(spannableStringBuilder2);
                }
                return formatInternal(i, spannableStringBuilder2, charSequence);
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
        /* JADX WARN: Code restructure failed: missing block: B:58:0x0182, code lost:
            if (android.text.TextUtils.isEmpty(r3.message) != false) goto L50;
         */
        /* JADX WARN: Removed duplicated region for block: B:112:0x025a  */
        /* JADX WARN: Removed duplicated region for block: B:115:0x0267  */
        /* JADX WARN: Removed duplicated region for block: B:119:0x0273  */
        /* JADX WARN: Removed duplicated region for block: B:134:0x02a4  */
        /* JADX WARN: Removed duplicated region for block: B:135:0x02a7  */
        /* JADX WARN: Removed duplicated region for block: B:137:0x02ac  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00f4  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x013b  */
        /* JADX WARN: Removed duplicated region for block: B:61:0x0187  */
        /* JADX WARN: Removed duplicated region for block: B:72:0x01c1  */
        /* JADX WARN: Removed duplicated region for block: B:73:0x01c3  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x01db  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x020e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean update() {
            Integer num;
            int measuredWidth;
            int i;
            boolean z;
            TLRPC$DraftMessage draft;
            TLRPC$DraftMessage tLRPC$DraftMessage;
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
                    measuredWidth = DialogCell.this.getMeasuredWidth() + (DialogCell.this.getMeasuredHeight() << 16);
                    if (DialogCell.this.isForumCell()) {
                        ArrayList<TLRPC$TL_forumTopic> topics = MessagesController.getInstance(DialogCell.this.currentAccount).getTopicsController().getTopics(-DialogCell.this.currentDialogId);
                        i = topics == null ? -1 : topics.size();
                        if (i == -1) {
                        }
                        if (DialogCell.this.isTopic) {
                            z = MediaDataController.getInstance(DialogCell.this.currentAccount).getDraftVoice(DialogCell.this.currentDialogId, (long) DialogCell.this.getTopicId()) != null;
                            draft = !z ? MediaDataController.getInstance(DialogCell.this.currentAccount).getDraft(DialogCell.this.currentDialogId, DialogCell.this.getTopicId()) : null;
                            if (draft != null) {
                            }
                            tLRPC$DraftMessage = draft;
                        } else {
                            DialogCell dialogCell2 = DialogCell.this;
                            if (dialogCell2.isDialogCell) {
                                z = MediaDataController.getInstance(dialogCell2.currentAccount).getDraftVoice(DialogCell.this.currentDialogId, 0L) != null;
                                if (!z) {
                                    draft = MediaDataController.getInstance(DialogCell.this.currentAccount).getDraft(DialogCell.this.currentDialogId, 0L);
                                    tLRPC$DraftMessage = draft;
                                }
                            } else {
                                z = false;
                            }
                            tLRPC$DraftMessage = null;
                        }
                        if (tLRPC$DraftMessage == null) {
                            i2 = 0;
                        } else {
                            int hashCode = tLRPC$DraftMessage.message.hashCode();
                            TLRPC$InputReplyTo tLRPC$InputReplyTo = tLRPC$DraftMessage.reply_to;
                            i2 = hashCode + (tLRPC$InputReplyTo != null ? tLRPC$InputReplyTo.reply_to_msg_id << 16 : 0);
                        }
                        boolean z3 = DialogCell.this.chat == null && DialogCell.this.chat.call_active && DialogCell.this.chat.call_not_empty;
                        boolean isTranslatingDialog = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                        if (this.lastDrawnSizeHash == measuredWidth) {
                            i3 = measuredWidth;
                            if (this.lastDrawnMessageId == id && this.lastDrawnTranslated == isTranslatingDialog && this.lastDrawnDialogId == DialogCell.this.currentDialogId && this.lastDrawnDialogIsFolder == tLRPC$Dialog.isFolder && this.lastDrawnReadState == j && Objects.equals(this.lastDrawnPrintingType, num) && this.lastTopicsCount == i && i2 == this.lastDrawnDraftHash && this.lastDrawnPinned == DialogCell.this.drawPin && this.lastDrawnHasCall == z3 && DialogCell.this.draftVoice == z) {
                                return false;
                            }
                        } else {
                            i3 = measuredWidth;
                        }
                        if (this.lastDrawnDialogId != DialogCell.this.currentDialogId) {
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
                    i = 0;
                    if (DialogCell.this.isTopic) {
                    }
                    if (tLRPC$DraftMessage == null) {
                    }
                    if (DialogCell.this.chat == null) {
                    }
                    boolean isTranslatingDialog2 = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                    if (this.lastDrawnSizeHash == measuredWidth) {
                    }
                    if (this.lastDrawnDialogId != DialogCell.this.currentDialogId) {
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
            }
            num = null;
            measuredWidth = DialogCell.this.getMeasuredWidth() + (DialogCell.this.getMeasuredHeight() << 16);
            if (DialogCell.this.isForumCell()) {
            }
            i = 0;
            if (DialogCell.this.isTopic) {
            }
            if (tLRPC$DraftMessage == null) {
            }
            if (DialogCell.this.chat == null) {
            }
            boolean isTranslatingDialog22 = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
            if (this.lastDrawnSizeHash == measuredWidth) {
            }
            if (this.lastDrawnDialogId != DialogCell.this.currentDialogId) {
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

    @Override // android.view.View
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
                        Collections.sort(arrayList, Comparator$-CC.comparingInt(DialogCell$ForumFormattedNames$$ExternalSyntheticLambda0.INSTANCE));
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                        long j = 0;
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
                            spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM), 0, Theme.key_chats_name, null), 0, Math.min(spannableStringBuilder.length(), i2 + 2), 0);
                        }
                        this.formattedNames = spannableStringBuilder;
                    } else if (!MessagesController.getInstance(i).getTopicsController().endIsReached(tLRPC$Chat.id)) {
                        MessagesController.getInstance(i).getTopicsController().preloadTopics(tLRPC$Chat.id);
                        this.formattedNames = LocaleController.getString("Loading", R.string.Loading);
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
}
