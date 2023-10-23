package org.telegram.ui.Cells;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
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
import org.telegram.tgnet.TLRPC$Message;
import org.telegram.tgnet.TLRPC$MessageAction;
import org.telegram.tgnet.TLRPC$MessageEntity;
import org.telegram.tgnet.TLRPC$MessageFwdHeader;
import org.telegram.tgnet.TLRPC$MessageMedia;
import org.telegram.tgnet.TLRPC$MessagePeerReaction;
import org.telegram.tgnet.TLRPC$MessageReplyHeader;
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
import org.telegram.tgnet.TLRPC$TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC$TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC$TL_messageMediaPoll;
import org.telegram.tgnet.TLRPC$TL_messageMediaStory;
import org.telegram.tgnet.TLRPC$TL_messageReactions;
import org.telegram.tgnet.TLRPC$TL_messageService;
import org.telegram.tgnet.TLRPC$TL_peerUser;
import org.telegram.tgnet.TLRPC$TL_photoEmpty;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.tgnet.TLRPC$UserStatus;
import org.telegram.tgnet.tl.TL_stories$StoryItem;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DialogsAdapter;
import org.telegram.ui.Components.AnimatedEmojiDrawable;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BubbleCounterPath;
import org.telegram.ui.Components.CanvasButton;
import org.telegram.ui.Components.CheckBox2;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.CubicBezierInterpolator;
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
/* loaded from: classes3.dex */
public class DialogCell extends BaseCell implements StoriesListPlaceProvider.AvatarOverlaysView {
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
    private boolean isDialogCell;
    private boolean isForum;
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

    /* loaded from: classes3.dex */
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

    /* loaded from: classes3.dex */
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

    /* loaded from: classes3.dex */
    public interface DialogCellDelegate {
        boolean canClickButtonInside();

        void onButtonClicked(DialogCell dialogCell);

        void onButtonLongPress(DialogCell dialogCell);

        void openHiddenStories();

        void openStory(DialogCell dialogCell, Runnable runnable);

        void showChatPreview(DialogCell dialogCell);
    }

    /* loaded from: classes3.dex */
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

    /* loaded from: classes3.dex */
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
            setMeasuredDimension(View.MeasureSpec.getSize(i), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? this.heightThreeLines : this.heightDefault) + (this.useSeparator ? 1 : 0));
            checkTwoLinesForName();
        }
        setMeasuredDimension(View.MeasureSpec.getSize(i), computeHeight());
        this.topClip = 0;
        this.bottomClip = getMeasuredHeight();
    }

    private int computeHeight() {
        if (!isForumCell() || this.isTransitionSupport || this.collapsed) {
            return getCollapsedHeight();
        }
        return AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 86.0f : (this.useSeparator ? 1 : 0) + 91);
    }

    private int getCollapsedHeight() {
        return AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? this.heightThreeLines : this.heightDefault) + (this.useSeparator ? 1 : 0) + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0);
    }

    private void checkTwoLinesForName() {
        this.twoLinesForName = false;
        if (this.isTopic) {
            buildLayout();
            if (this.nameIsEllipsized) {
                this.twoLinesForName = true;
                buildLayout();
            }
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
        return Emoji.replaceEmoji(spannableStringBuilder, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(93:14|(1:1452)(1:18)|19|(1:1451)(1:25)|26|(1:1450)(1:30)|31|(1:33)|34|(2:36|(1:1439)(1:40))(2:1440|(1:1449)(1:1444))|41|(1:43)|44|(1:46)(1:1434)|47|(7:49|(1:51)|52|53|(1:55)|56|57)|58|(9:60|(2:62|(2:694|(1:696)(1:697))(2:66|(1:68)(1:693)))(4:698|(1:715)(1:702)|703|(2:711|(1:713)(1:714))(2:707|(1:709)(1:710)))|69|(3:71|(1:73)(4:680|(1:682)|683|(1:688)(1:687))|74)(3:689|(1:691)|692)|75|(1:77)(1:679)|78|(1:80)(1:(1:675)(1:(1:677)(1:678)))|81)(23:716|(2:1430|(1:1432)(1:1433))(2:720|(1:722)(1:1429))|723|(2:725|(2:727|(2:735|(1:737)(1:738))(2:731|(1:733)(1:734))))(2:1383|(2:1385|(2:1387|(1:1389)(2:1390|(1:1392)(3:1393|(1:1399)(1:1397)|1398)))(2:1400|(7:1402|(1:1404)(2:1419|(1:1421)(3:1422|(1:1428)(1:1426)|1427))|1405|(2:1407|(3:1411|1412|(2:1414|(1:1416)(1:1417))))|1418|1412|(0)))))|739|(1:743)|744|(2:746|(1:750))(2:1379|(1:1381)(1:1382))|751|(6:1357|(2:1359|(2:1361|(2:1363|(1:1365))))|1367|(2:1369|(1:1371))|1373|(14:1375|(1:1377)|760|(7:762|(1:764)(1:932)|765|(1:767)(1:931)|768|(1:773)|774)(3:(6:934|(1:936)(1:1351)|937|(1:939)(1:1350)|(1:941)(1:1349)|942)(1:1352)|943|(4:945|(2:947|(2:954|953)(1:951))(7:955|(1:957)|958|(3:962|(1:964)(1:966)|965)|967|(1:971)|972)|952|953)(5:973|(1:975)(2:979|(2:981|(1:983)(5:984|(2:986|(1:988)(2:989|(1:991)(2:992|(1:994)(2:995|(2:997|(1:999)(1:1000))))))(2:1002|(4:1006|(1:1012)(1:1010)|1011|978))|1001|977|978))(12:1013|(1:1015)(1:1348)|1016|(2:1030|(9:1032|(2:1034|(9:1036|(1:1038)(3:1340|(1:1342)(1:1344)|1343)|1039|(6:1048|(3:1050|(4:1052|(2:1054|(2:1056|(1:1058)(2:1061|(1:1063)(1:1064))))|1065|(1:1067)(2:1068|(1:1070)(2:1071|(1:1073)(1:1074))))(1:1075)|1059)(2:1076|(6:1087|(5:1094|(2:1110|(9:1174|(8:1204|(4:1206|(1:1217)|1212|(1:1216))(2:1218|(4:1225|(2:1227|(1:1232))|1233|(4:1235|(1:1237)(2:1259|(1:1261)(2:1262|(1:1264)(2:1265|(1:1267)(2:1268|(1:1270)(1:1271)))))|1238|(3:1251|(3:1253|(1:1255)(1:1257)|1256)|1258)(4:1242|(2:1244|(1:1246)(1:1247))|(1:1249)|1250))(2:1272|(3:1274|(3:1276|(1:1278)(1:1281)|1279)(3:1282|(1:1284)(1:1286)|1285)|1280)(5:1287|(1:1289)(2:1296|(1:1298)(2:1299|(1:1301)(2:1302|(1:1304)(2:1305|(1:1307)(2:1308|(3:1322|(4:1328|(1:1330)|1331|(2:1333|(3:1335|(1:1337)(1:1339)|1338)))(1:1326)|1327)(2:1312|(3:1314|(2:1316|(1:1318))(1:1320)|1319)(1:1321)))))))|1290|1291|(1:1295))))(1:1224))|1177|(1:1179)|1180|(7:1182|(3:1197|(1:1199)|1200)(1:1186)|1187|(1:1189)(1:1196)|1190|(1:1194)|1195)|1201|(1:1203))|1176|1177|(0)|1180|(0)|1201|(0))(13:1121|(2:1127|(12:1129|(1:1172)(1:1133)|1134|1135|(1:1171)(5:1141|1142|1143|1144|1145)|1146|(1:1150)|1151|(4:1153|(1:1155)|1156|(1:1158)(1:1159))|1160|1044|(1:1046)(1:1047)))|1173|1135|(2:1137|1167)|1171|1146|(2:1148|1150)|1151|(0)|1160|1044|(0)(0)))(4:1100|(1:1109)(1:1104)|1105|(1:1107)(1:1108))|1043|1044|(0)(0))(1:1093)|1042|1043|1044|(0)(0))(3:1080|(1:1086)(1:1084)|1085))|1060|1043|1044|(0)(0))|1041|1042|1043|1044|(0)(0)))(1:1346)|1345|(0)|1041|1042|1043|1044|(0)(0)))|1347|1345|(0)|1041|1042|1043|1044|(0)(0)))|976|977|978))|775|(1:777)(2:924|(1:926)(2:927|(1:929)(1:930)))|778|(1:780)(5:841|(4:843|(1:(1:846)(2:897|848))(1:898)|847|848)(7:899|(1:901)(6:911|(2:920|(1:922)(1:923))(1:919)|903|(1:905)(1:910)|906|(1:908)(1:909))|902|903|(0)(0)|906|(0)(0))|849|(2:854|(2:856|(1:858)(2:859|(1:861)(2:862|(3:864|(3:866|(1:868)(1:871)|869)(2:872|(3:874|(1:886)(1:878)|879)(3:887|(1:895)(1:893)|894))|870)))))|896)|781|(5:785|(1:787)(2:831|(5:833|(1:835)|836|(1:838)|839))|788|(1:790)(2:792|(3:794|(3:796|(1:798)|799)(2:806|(4:808|(1:810)|811|(1:813)(1:814))(1:815))|(1:804))(4:816|(3:818|(1:820)(2:821|(2:823|(1:825)(3:826|(1:828)|829))(1:830))|(2:802|804))|805|(0)))|791)|840|788|(0)(0)|791))|759|760|(0)(0)|775|(0)(0)|778|(0)(0)|781|(6:783|785|(0)(0)|788|(0)(0)|791)|840|788|(0)(0)|791)|(3:83|(1:85)(1:672)|86)(1:673)|87|(3:89|(1:91)(1:670)|92)(1:671)|93|(1:95)(1:669)|96|(3:98|(1:100)(1:102)|101)|103|(2:105|(1:107)(1:656))(2:657|(2:659|(2:661|(1:663)(1:664))(2:665|(1:667)(1:668))))|108|(2:626|(2:653|(1:655))(2:630|(2:632|(1:634))(2:635|(2:637|(1:639))(2:640|(4:642|(1:644)(1:648)|645|(1:647))))))(2:112|(1:114))|115|116|117|(1:119)|120|(1:122)|123|(3:125|(1:127)(1:129)|128)|130|(1:132)(1:623)|133|(1:135)|136|(1:622)(1:142)|143|(1:145)(1:621)|146|(1:620)(1:150)|151|152|(4:604|(1:606)(1:618)|607|(2:608|(3:610|(2:612|613)(2:615|616)|614)(1:617)))(8:156|(1:158)(1:603)|159|(1:161)(1:602)|162|(1:164)(1:601)|165|(2:166|(3:168|(2:170|171)(2:173|174)|172)(1:175)))|176|(1:178)|179|(2:181|(1:183)(1:184))|185|(2:187|(1:189)(1:531))(1:(4:(3:543|(1:545)(1:599)|546)(1:600)|(5:548|(1:550)(1:597)|551|(3:553|(1:555)(1:591)|556)(3:592|(1:594)(1:596)|595)|557)(1:598)|558|(2:560|(4:562|(3:564|(1:566)(1:568)|567)|569|(3:571|(1:573)(1:575)|574))(5:576|(3:578|(1:580)(1:582)|581)|583|(3:585|(1:587)(1:589)|588)|590)))(3:536|(2:538|(1:540))|541))|(7:(1:192)|193|(1:195)|196|(1:207)(1:200)|201|(1:205))|208|(1:530)(1:212)|213|(4:215|(1:481)(1:219)|220|(2:221|(1:223)(1:224)))(2:482|(8:507|508|(2:510|(2:512|(1:514)))|515|516|(1:526)(1:520)|521|(2:522|(1:524)(1:525)))(2:486|(4:491|(1:501)(1:495)|496|(2:497|(1:499)(1:500)))(1:490)))|225|(1:227)|228|(4:229|230|(1:232)(1:479)|233)|234|235|236|(3:238|(1:243)|244)|245|247|248|(1:474)(2:(2:255|(1:468)(1:261))|262)|263|(3:265|(3:267|(2:282|283)|280)|284)|285|(1:467)(1:289)|290|(12:295|(2:297|(1:301))|302|303|304|305|306|(10:308|(6:312|(1:314)|315|(1:342)(2:319|(1:321)(2:327|(1:329)(2:330|(3:332|(1:334)(1:336)|335)(1:337))))|322|(2:324|(1:326)))|343|(3:347|(1:(1:356)(2:349|(1:351)(2:352|353)))|(1:355))|357|(3:361|(1:(1:370)(2:363|(1:365)(2:366|367)))|(1:369))|371|(2:377|(1:379))|380|(4:384|(1:386)|387|388))(10:406|(5:410|(1:412)|413|(4:415|(1:417)|418|(1:420))|421)|422|(4:426|(1:428)|429|430)|431|(4:435|(1:437)|438|439)|440|(4:444|(1:446)|447|448)|449|(1:453))|389|(3:(1:403)(1:398)|399|(1:401)(1:402))|404|405)|458|(1:461)|462|(1:464)(1:466)|465|303|304|305|306|(0)(0)|389|(6:391|393|(1:396)|403|399|(0)(0))|404|405) */
    /* JADX WARN: Code restructure failed: missing block: B:1295:0x1cd2, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1296:0x1cd3, code lost:
        r2 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1298:0x1cd6, code lost:
        r38.messageLayout = null;
        org.telegram.messenger.FileLog.e(r0);
        r8 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x0569, code lost:
        if (r2.post_messages == false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x0575, code lost:
        if (r2.kicked != false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x0583, code lost:
        if (r38.isTopic == false) goto L759;
     */
    /* JADX WARN: Code restructure failed: missing block: B:839:0x115a, code lost:
        if (r9 == null) goto L805;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1022:0x1671  */
    /* JADX WARN: Removed duplicated region for block: B:1023:0x1693  */
    /* JADX WARN: Removed duplicated region for block: B:1027:0x16d4  */
    /* JADX WARN: Removed duplicated region for block: B:1034:0x170d  */
    /* JADX WARN: Removed duplicated region for block: B:1037:0x171c  */
    /* JADX WARN: Removed duplicated region for block: B:1043:0x1741  */
    /* JADX WARN: Removed duplicated region for block: B:1047:0x1779  */
    /* JADX WARN: Removed duplicated region for block: B:1118:0x1939  */
    /* JADX WARN: Removed duplicated region for block: B:1145:0x19a4  */
    /* JADX WARN: Removed duplicated region for block: B:1156:0x19cf  */
    /* JADX WARN: Removed duplicated region for block: B:1204:0x1a8b  */
    /* JADX WARN: Removed duplicated region for block: B:1208:0x1aac A[Catch: Exception -> 0x1b01, TryCatch #7 {Exception -> 0x1b01, blocks: (B:1206:0x1aa4, B:1208:0x1aac, B:1209:0x1afe), top: B:1455:0x1aa4 }] */
    /* JADX WARN: Removed duplicated region for block: B:1209:0x1afe A[Catch: Exception -> 0x1b01, TRY_LEAVE, TryCatch #7 {Exception -> 0x1b01, blocks: (B:1206:0x1aa4, B:1208:0x1aac, B:1209:0x1afe), top: B:1455:0x1aa4 }] */
    /* JADX WARN: Removed duplicated region for block: B:1213:0x1b17 A[Catch: Exception -> 0x1b6f, TryCatch #0 {Exception -> 0x1b6f, blocks: (B:1211:0x1b11, B:1213:0x1b17, B:1215:0x1b1b, B:1218:0x1b20, B:1219:0x1b49), top: B:1441:0x1b11 }] */
    /* JADX WARN: Removed duplicated region for block: B:1247:0x1be6 A[Catch: Exception -> 0x1cd5, TryCatch #4 {Exception -> 0x1cd5, blocks: (B:1223:0x1b73, B:1225:0x1b77, B:1233:0x1b91, B:1236:0x1b97, B:1238:0x1b9d, B:1240:0x1ba1, B:1242:0x1bb4, B:1245:0x1be2, B:1247:0x1be6, B:1249:0x1bf8, B:1251:0x1bfe, B:1253:0x1c02, B:1255:0x1c06, B:1257:0x1c0a, B:1259:0x1c0e, B:1262:0x1c1b, B:1261:0x1c18, B:1263:0x1c1e, B:1265:0x1c22, B:1267:0x1c26, B:1269:0x1c2b, B:1271:0x1c2f, B:1274:0x1c34, B:1276:0x1c38, B:1278:0x1c4b, B:1280:0x1c51, B:1281:0x1c66, B:1283:0x1c81, B:1286:0x1c88, B:1287:0x1c8f, B:1291:0x1ca5, B:1268:0x1c29, B:1243:0x1bd2, B:1227:0x1b7b, B:1229:0x1b7f, B:1231:0x1b84), top: B:1449:0x1b73 }] */
    /* JADX WARN: Removed duplicated region for block: B:1276:0x1c38 A[Catch: Exception -> 0x1cd5, TryCatch #4 {Exception -> 0x1cd5, blocks: (B:1223:0x1b73, B:1225:0x1b77, B:1233:0x1b91, B:1236:0x1b97, B:1238:0x1b9d, B:1240:0x1ba1, B:1242:0x1bb4, B:1245:0x1be2, B:1247:0x1be6, B:1249:0x1bf8, B:1251:0x1bfe, B:1253:0x1c02, B:1255:0x1c06, B:1257:0x1c0a, B:1259:0x1c0e, B:1262:0x1c1b, B:1261:0x1c18, B:1263:0x1c1e, B:1265:0x1c22, B:1267:0x1c26, B:1269:0x1c2b, B:1271:0x1c2f, B:1274:0x1c34, B:1276:0x1c38, B:1278:0x1c4b, B:1280:0x1c51, B:1281:0x1c66, B:1283:0x1c81, B:1286:0x1c88, B:1287:0x1c8f, B:1291:0x1ca5, B:1268:0x1c29, B:1243:0x1bd2, B:1227:0x1b7b, B:1229:0x1b7f, B:1231:0x1b84), top: B:1449:0x1b73 }] */
    /* JADX WARN: Removed duplicated region for block: B:1289:0x1ca0  */
    /* JADX WARN: Removed duplicated region for block: B:1290:0x1ca3  */
    /* JADX WARN: Removed duplicated region for block: B:1301:0x1cf1  */
    /* JADX WARN: Removed duplicated region for block: B:1377:0x1ef6  */
    /* JADX WARN: Removed duplicated region for block: B:1424:0x1fda  */
    /* JADX WARN: Removed duplicated region for block: B:1435:0x2017  */
    /* JADX WARN: Removed duplicated region for block: B:1436:0x201f  */
    /* JADX WARN: Removed duplicated region for block: B:1477:0x1705 A[EDGE_INSN: B:1477:0x1705->B:1032:0x1705 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:219:0x04ca  */
    /* JADX WARN: Removed duplicated region for block: B:276:0x058d  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x05ef  */
    /* JADX WARN: Removed duplicated region for block: B:416:0x08d3  */
    /* JADX WARN: Removed duplicated region for block: B:528:0x0ab8  */
    /* JADX WARN: Removed duplicated region for block: B:533:0x0ac9  */
    /* JADX WARN: Removed duplicated region for block: B:683:0x0e13  */
    /* JADX WARN: Removed duplicated region for block: B:686:0x0e38  */
    /* JADX WARN: Removed duplicated region for block: B:707:0x0eee  */
    /* JADX WARN: Removed duplicated region for block: B:710:0x0f1c  */
    /* JADX WARN: Removed duplicated region for block: B:711:0x0f26  */
    /* JADX WARN: Removed duplicated region for block: B:714:0x0f32  */
    /* JADX WARN: Removed duplicated region for block: B:715:0x0f3a  */
    /* JADX WARN: Removed duplicated region for block: B:724:0x0f58  */
    /* JADX WARN: Removed duplicated region for block: B:725:0x0f6a  */
    /* JADX WARN: Removed duplicated region for block: B:754:0x0fe7  */
    /* JADX WARN: Removed duplicated region for block: B:755:0x0fec  */
    /* JADX WARN: Removed duplicated region for block: B:758:0x0ff3  */
    /* JADX WARN: Removed duplicated region for block: B:759:0x0ff6  */
    /* JADX WARN: Removed duplicated region for block: B:812:0x10b8  */
    /* JADX WARN: Removed duplicated region for block: B:816:0x10ca  */
    /* JADX WARN: Removed duplicated region for block: B:817:0x10d8  */
    /* JADX WARN: Removed duplicated region for block: B:829:0x1117  */
    /* JADX WARN: Removed duplicated region for block: B:831:0x112e  */
    /* JADX WARN: Removed duplicated region for block: B:868:0x11cc  */
    /* JADX WARN: Removed duplicated region for block: B:872:0x11de  */
    /* JADX WARN: Removed duplicated region for block: B:877:0x121e  */
    /* JADX WARN: Removed duplicated region for block: B:880:0x122b  */
    /* JADX WARN: Removed duplicated region for block: B:885:0x125f  */
    /* JADX WARN: Removed duplicated region for block: B:888:0x1264  */
    /* JADX WARN: Removed duplicated region for block: B:889:0x1274  */
    /* JADX WARN: Removed duplicated region for block: B:892:0x1295  */
    /* JADX WARN: Removed duplicated region for block: B:899:0x12b5  */
    /* JADX WARN: Removed duplicated region for block: B:903:0x12e3  */
    /* JADX WARN: Removed duplicated region for block: B:932:0x13a9  */
    /* JADX WARN: Removed duplicated region for block: B:955:0x1419  */
    /* JADX WARN: Removed duplicated region for block: B:958:0x141e A[Catch: Exception -> 0x150b, TryCatch #1 {Exception -> 0x150b, blocks: (B:953:0x140f, B:956:0x141a, B:958:0x141e, B:959:0x1428, B:961:0x142c, B:965:0x1446, B:966:0x144f, B:970:0x1466, B:972:0x146c, B:973:0x1478, B:975:0x148f, B:977:0x1495, B:981:0x14a6, B:983:0x14aa, B:985:0x14e8, B:987:0x14ec, B:989:0x14f5, B:991:0x14ff, B:984:0x14cb), top: B:1443:0x140f }] */
    /* JADX WARN: Removed duplicated region for block: B:961:0x142c A[Catch: Exception -> 0x150b, TryCatch #1 {Exception -> 0x150b, blocks: (B:953:0x140f, B:956:0x141a, B:958:0x141e, B:959:0x1428, B:961:0x142c, B:965:0x1446, B:966:0x144f, B:970:0x1466, B:972:0x146c, B:973:0x1478, B:975:0x148f, B:977:0x1495, B:981:0x14a6, B:983:0x14aa, B:985:0x14e8, B:987:0x14ec, B:989:0x14f5, B:991:0x14ff, B:984:0x14cb), top: B:1443:0x140f }] */
    /* JADX WARN: Removed duplicated region for block: B:968:0x1463  */
    /* JADX WARN: Removed duplicated region for block: B:969:0x1465  */
    /* JADX WARN: Removed duplicated region for block: B:972:0x146c A[Catch: Exception -> 0x150b, TryCatch #1 {Exception -> 0x150b, blocks: (B:953:0x140f, B:956:0x141a, B:958:0x141e, B:959:0x1428, B:961:0x142c, B:965:0x1446, B:966:0x144f, B:970:0x1466, B:972:0x146c, B:973:0x1478, B:975:0x148f, B:977:0x1495, B:981:0x14a6, B:983:0x14aa, B:985:0x14e8, B:987:0x14ec, B:989:0x14f5, B:991:0x14ff, B:984:0x14cb), top: B:1443:0x140f }] */
    /* JADX WARN: Removed duplicated region for block: B:983:0x14aa A[Catch: Exception -> 0x150b, TryCatch #1 {Exception -> 0x150b, blocks: (B:953:0x140f, B:956:0x141a, B:958:0x141e, B:959:0x1428, B:961:0x142c, B:965:0x1446, B:966:0x144f, B:970:0x1466, B:972:0x146c, B:973:0x1478, B:975:0x148f, B:977:0x1495, B:981:0x14a6, B:983:0x14aa, B:985:0x14e8, B:987:0x14ec, B:989:0x14f5, B:991:0x14ff, B:984:0x14cb), top: B:1443:0x140f }] */
    /* JADX WARN: Removed duplicated region for block: B:984:0x14cb A[Catch: Exception -> 0x150b, TryCatch #1 {Exception -> 0x150b, blocks: (B:953:0x140f, B:956:0x141a, B:958:0x141e, B:959:0x1428, B:961:0x142c, B:965:0x1446, B:966:0x144f, B:970:0x1466, B:972:0x146c, B:973:0x1478, B:975:0x148f, B:977:0x1495, B:981:0x14a6, B:983:0x14aa, B:985:0x14e8, B:987:0x14ec, B:989:0x14f5, B:991:0x14ff, B:984:0x14cb), top: B:1443:0x140f }] */
    /* JADX WARN: Type inference failed for: r10v32, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r1v148, types: [java.lang.CharSequence] */
    /* JADX WARN: Type inference failed for: r1v198 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v306 */
    /* JADX WARN: Type inference failed for: r1v99, types: [android.text.SpannableStringBuilder] */
    /* JADX WARN: Type inference failed for: r38v0, types: [android.view.View, org.telegram.ui.Cells.DialogCell, android.view.ViewGroup] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void buildLayout() {
        int i;
        boolean z;
        SpannableStringBuilder spannableStringBuilder;
        boolean z2;
        int i2;
        TLRPC$Chat chat;
        String str;
        CharSequence charSequence;
        ?? r1;
        boolean z3;
        SpannableStringBuilder spannableStringBuilder2;
        String str2;
        String str3;
        String str4;
        String formatPluralString;
        String str5;
        TLRPC$TL_forumTopic findTopic;
        CharSequence replaceNewLines;
        CharSequence highlightText;
        String str6;
        int i3;
        SpannableStringBuilder replaceEmoji;
        boolean z4;
        CharSequence highlightText2;
        ForegroundColorSpanThemable foregroundColorSpanThemable;
        CharSequence charSequence2;
        String str7;
        String str8;
        String str9;
        boolean z5;
        SpannableStringBuilder spannableStringBuilder3;
        String str10;
        boolean z6;
        String str11;
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        ArrayList<TLRPC$MessagePeerReaction> arrayList;
        CharSequence charSequence3;
        CharSequence string;
        ArrayList<TLRPC$MessageEntity> arrayList2;
        CharSequence charSequence4;
        SpannableStringBuilder spannableStringBuilder4;
        CharSequence charSequence5;
        TLRPC$DraftMessage tLRPC$DraftMessage;
        MessageObject messageObject;
        String stringForMessageListDate;
        MessageObject messageObject2;
        String str12;
        String str13;
        MessagesController messagesController;
        CharSequence charSequence6;
        String str14;
        CharSequence userName;
        SpannableStringBuilder spannableStringBuilder5;
        CharSequence charSequence7;
        String str15;
        int i4;
        SpannableStringBuilder spannableStringBuilder6;
        boolean z7;
        String str16;
        boolean z8;
        int i5;
        TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader;
        MessageObject messageObject3;
        int i6;
        int i7;
        int dp;
        int dp2;
        int dp3;
        int i8;
        int i9;
        ImageReceiver[] imageReceiverArr;
        int max;
        int lineCount;
        int lineCount2;
        int lineCount3;
        StaticLayout staticLayout;
        float primaryHorizontal;
        float primaryHorizontal2;
        int i10;
        int lineCount4;
        int lineCount5;
        int lineCount6;
        boolean z9;
        CharSequence charSequence8;
        CharSequence charSequence9;
        Layout.Alignment alignment;
        int i11;
        Object[] spans;
        CharSequence replaceTwoNewLinesToOne;
        int dp4;
        int dp5;
        int dp6;
        CharSequence highlightText3;
        CharSequence charSequence10;
        String str17;
        String str18;
        SpannableStringBuilder formatInternal;
        if (this.isTransitionSupport) {
            return;
        }
        if (this.isDialogCell && !this.updateHelper.update() && this.currentDialogFolderId == 0 && this.encryptedChat == null) {
            return;
        }
        if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
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
        boolean z10 = (UserObject.isUserSelf(this.user) || this.useMeForMyMessages) ? false : true;
        this.printingStringType = -1;
        if (!isForumCell()) {
            this.buttonLayout = null;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || this.currentDialogFolderId != 0 || isForumCell()) {
                this.hasNameInMessage = true;
                i = 1;
            } else {
                this.hasNameInMessage = false;
                i = 2;
            }
        } else if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || this.currentDialogFolderId != 0 || isForumCell()) {
            i = 3;
            this.hasNameInMessage = true;
        } else {
            this.hasNameInMessage = false;
            i = 4;
        }
        MessageObject messageObject4 = this.message;
        if (messageObject4 != null) {
            messageObject4.updateTranslation();
        }
        MessageObject messageObject5 = this.message;
        CharSequence charSequence11 = messageObject5 != null ? messageObject5.messageText : null;
        boolean z11 = charSequence11 instanceof Spannable;
        SpannableStringBuilder spannableStringBuilder7 = charSequence11;
        if (z11) {
            SpannableStringBuilder spannableStringBuilder8 = new SpannableStringBuilder(charSequence11);
            for (URLSpanNoUnderlineBold uRLSpanNoUnderlineBold : (URLSpanNoUnderlineBold[]) spannableStringBuilder8.getSpans(0, spannableStringBuilder8.length(), URLSpanNoUnderlineBold.class)) {
                spannableStringBuilder8.removeSpan(uRLSpanNoUnderlineBold);
            }
            for (URLSpanNoUnderline uRLSpanNoUnderline : (URLSpanNoUnderline[]) spannableStringBuilder8.getSpans(0, spannableStringBuilder8.length(), URLSpanNoUnderline.class)) {
                spannableStringBuilder8.removeSpan(uRLSpanNoUnderline);
            }
            spannableStringBuilder7 = spannableStringBuilder8;
        }
        this.lastMessageString = spannableStringBuilder7;
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
                str17 = LocaleController.getString("FromYou", R.string.FromYou);
                CustomDialog customDialog3 = this.customDialog;
                if (customDialog3.isMedia) {
                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                    formatInternal = formatInternal(i, this.message.messageText, null);
                    formatInternal.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_attachMessage, this.resourcesProvider), 0, formatInternal.length(), 33);
                } else {
                    String str19 = customDialog3.message;
                    if (str19.length() > 150) {
                        str19 = str19.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                    }
                    if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                        formatInternal = formatInternal(i, str19, str17);
                    } else {
                        formatInternal = formatInternal(i, str19.replace('\n', ' '), str17);
                    }
                }
                charSequence10 = Emoji.replaceEmoji(formatInternal, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                z7 = false;
            } else {
                charSequence10 = customDialog2.message;
                if (customDialog2.isMedia) {
                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                }
                str17 = null;
                z7 = true;
            }
            CharSequence charSequence12 = charSequence10;
            str14 = LocaleController.stringForMessageListDate(this.customDialog.date);
            int i12 = this.customDialog.unread_count;
            if (i12 != 0) {
                this.drawCount = true;
                str18 = String.format("%d", Integer.valueOf(i12));
            } else {
                this.drawCount = false;
                str18 = null;
            }
            CustomDialog customDialog4 = this.customDialog;
            int i13 = customDialog4.sent;
            if (i13 == 0) {
                this.drawClock = true;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            } else if (i13 == 2) {
                this.drawCheck1 = true;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else if (i13 == 1) {
                this.drawCheck1 = false;
                this.drawCheck2 = true;
                this.drawClock = false;
            } else {
                this.drawClock = false;
                this.drawCheck1 = false;
                this.drawCheck2 = false;
            }
            this.drawError = false;
            str16 = str17;
            str12 = str18;
            str13 = null;
            charSequence7 = charSequence12;
            z8 = true;
            i4 = -1;
            spannableStringBuilder5 = null;
            str15 = customDialog4.name;
            spannableStringBuilder6 = "";
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
                TLRPC$Chat tLRPC$Chat = this.chat;
                if (tLRPC$Chat != null) {
                    if (tLRPC$Chat.scam) {
                        this.drawScam = 1;
                        Theme.dialogs_scamDrawable.checkText();
                    } else if (tLRPC$Chat.fake) {
                        this.drawScam = 2;
                        Theme.dialogs_fakeDrawable.checkText();
                    } else {
                        this.drawVerified = !this.forbidVerified && tLRPC$Chat.verified;
                    }
                } else {
                    TLRPC$User tLRPC$User = this.user;
                    if (tLRPC$User != null) {
                        if (tLRPC$User.scam) {
                            this.drawScam = 1;
                            Theme.dialogs_scamDrawable.checkText();
                        } else if (tLRPC$User.fake) {
                            this.drawScam = 2;
                            Theme.dialogs_fakeDrawable.checkText();
                        } else {
                            this.drawVerified = !this.forbidVerified && tLRPC$User.verified;
                        }
                        if (MessagesController.getInstance(this.currentAccount).isPremiumUser(this.user)) {
                            long j = UserConfig.getInstance(this.currentAccount).clientUserId;
                            long j2 = this.user.id;
                            if (j != j2 && j2 != 0) {
                                z = true;
                                this.drawPremium = z;
                                if (z) {
                                    Long emojiStatusDocumentId = UserObject.getEmojiStatusDocumentId(this.user);
                                    AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
                                    swapAnimatedEmojiDrawable.center = LocaleController.isRTL;
                                    if (emojiStatusDocumentId != null) {
                                        this.nameLayoutEllipsizeByGradient = true;
                                        swapAnimatedEmojiDrawable.set(emojiStatusDocumentId.longValue(), false);
                                    } else {
                                        this.nameLayoutEllipsizeByGradient = true;
                                        swapAnimatedEmojiDrawable.set(PremiumGradient.getInstance().premiumStarDrawableMini, false);
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
            int i14 = this.lastMessageDate;
            if (i14 == 0 && (messageObject3 = this.message) != null) {
                i14 = messageObject3.messageOwner.date;
            }
            if (this.isTopic) {
                TLRPC$DraftMessage draft = MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, getTopicId());
                this.draftMessage = draft;
                if (draft != null && TextUtils.isEmpty(draft.message)) {
                    this.draftMessage = null;
                }
            } else if (this.isDialogCell) {
                this.draftMessage = MediaDataController.getInstance(this.currentAccount).getDraft(this.currentDialogId, 0);
            } else {
                this.draftMessage = null;
            }
            TLRPC$DraftMessage tLRPC$DraftMessage2 = this.draftMessage;
            if (tLRPC$DraftMessage2 == null || ((!TextUtils.isEmpty(tLRPC$DraftMessage2.message) || ((tLRPC$MessageReplyHeader = this.draftMessage.reply_to) != null && tLRPC$MessageReplyHeader.reply_to_msg_id != 0)) && (i14 <= this.draftMessage.date || this.unreadCount == 0))) {
                if (ChatObject.isChannel(this.chat)) {
                    TLRPC$Chat tLRPC$Chat2 = this.chat;
                    if (!tLRPC$Chat2.megagroup) {
                        if (!tLRPC$Chat2.creator) {
                            TLRPC$TL_chatAdminRights tLRPC$TL_chatAdminRights = tLRPC$Chat2.admin_rights;
                            if (tLRPC$TL_chatAdminRights != null) {
                            }
                        }
                    }
                }
                TLRPC$Chat tLRPC$Chat3 = this.chat;
                if (tLRPC$Chat3 != null) {
                    if (!tLRPC$Chat3.left) {
                    }
                }
                if (!this.forbidDraft) {
                    if (ChatObject.isForum(tLRPC$Chat3)) {
                    }
                    if (!isForumCell()) {
                        this.draftMessage = null;
                        this.needEmoji = true;
                        updateMessageThumbs();
                        String messageNameString = getMessageNameString();
                        CharSequence formatTopicsNames = formatTopicsNames();
                        MessageObject messageObject6 = this.message;
                        str10 = this.message != null ? getMessageStringFormatted(i, messageObject6 != null ? MessagesController.getRestrictionReason(messageObject6.messageOwner.restriction_reason) : null, messageNameString, true) : "";
                        if (this.applyName && str10.length() >= 0 && messageNameString != null) {
                            str10 = SpannableStringBuilder.valueOf(str10);
                            str10.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_name, this.resourcesProvider), 0, Math.min(str10.length(), messageNameString.length() + 1), 0);
                        }
                        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                        z5 = z10;
                        spannableStringBuilder3 = "";
                        z4 = true;
                        z6 = true;
                        i2 = -1;
                        str11 = messageNameString;
                        charSequence5 = formatTopicsNames;
                    } else {
                        if (printingString != null) {
                            this.lastPrintString = printingString;
                            int intValue = MessagesController.getInstance(this.currentAccount).getPrintingStringType(this.currentDialogId, getTopicId()).intValue();
                            this.printingStringType = intValue;
                            StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(intValue);
                            int intrinsicWidth = chatStatusDrawable != null ? chatStatusDrawable.getIntrinsicWidth() + AndroidUtilities.dp(3.0f) : 0;
                            spannableStringBuilder = new SpannableStringBuilder();
                            CharSequence replace = TextUtils.replace(printingString, new String[]{"..."}, new String[]{""});
                            int indexOf = this.printingStringType == 5 ? replace.toString().indexOf("**oo**") : -1;
                            if (indexOf >= 0) {
                                spannableStringBuilder.append(replace).setSpan(new FixedWidthSpan(Theme.getChatStatusDrawable(this.printingStringType).getIntrinsicWidth()), indexOf, indexOf + 6, 0);
                            } else {
                                spannableStringBuilder.append((CharSequence) " ").append(replace).setSpan(new FixedWidthSpan(intrinsicWidth), 0, 1, 0);
                            }
                            i2 = indexOf;
                            z2 = false;
                        } else {
                            this.lastPrintString = null;
                            this.printingStringType = -1;
                            spannableStringBuilder = "";
                            z2 = true;
                            i2 = -1;
                        }
                        if (this.draftMessage != null) {
                            String string2 = LocaleController.getString("Draft", R.string.Draft);
                            if (TextUtils.isEmpty(this.draftMessage.message)) {
                                if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                                    z5 = z10;
                                    charSequence4 = "";
                                    z4 = true;
                                    spannableStringBuilder3 = spannableStringBuilder;
                                    str10 = null;
                                    z6 = false;
                                    str11 = string2;
                                    charSequence5 = charSequence4;
                                } else {
                                    SpannableStringBuilder valueOf = SpannableStringBuilder.valueOf(string2);
                                    valueOf.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_draft, this.resourcesProvider), 0, string2.length(), 33);
                                    spannableStringBuilder4 = valueOf;
                                }
                            } else {
                                String str20 = this.draftMessage.message;
                                if (str20.length() > 150) {
                                    str20 = str20.substring(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                }
                                SpannableStringBuilder spannableStringBuilder9 = new SpannableStringBuilder(str20);
                                MediaDataController.addTextStyleRuns(this.draftMessage, spannableStringBuilder9, 264);
                                TLRPC$DraftMessage tLRPC$DraftMessage3 = this.draftMessage;
                                if (tLRPC$DraftMessage3 != null && (arrayList2 = tLRPC$DraftMessage3.entities) != null) {
                                    TextPaint textPaint5 = this.currentMessagePaint;
                                    MediaDataController.addAnimatedEmojiSpans(arrayList2, spannableStringBuilder9, textPaint5 == null ? null : textPaint5.getFontMetricsInt());
                                }
                                SpannableStringBuilder formatInternal2 = formatInternal(i, AndroidUtilities.replaceNewLines(spannableStringBuilder9), string2);
                                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                    formatInternal2.setSpan(new ForegroundColorSpanThemable(Theme.key_chats_draft, this.resourcesProvider), 0, string2.length() + 1, 33);
                                }
                                spannableStringBuilder4 = Emoji.replaceEmoji(formatInternal2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                            }
                            z5 = z10;
                            charSequence4 = spannableStringBuilder4;
                            z4 = true;
                            spannableStringBuilder3 = spannableStringBuilder;
                            str10 = null;
                            z6 = false;
                            str11 = string2;
                            charSequence5 = charSequence4;
                        } else {
                            if (this.clearingDialog) {
                                this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                string = LocaleController.getString("HistoryCleared", R.string.HistoryCleared);
                            } else {
                                MessageObject messageObject7 = this.message;
                                if (messageObject7 == null) {
                                    if (this.currentDialogFolderId != 0) {
                                        string = formatArchivedDialogNames();
                                    } else {
                                        TLRPC$EncryptedChat tLRPC$EncryptedChat = this.encryptedChat;
                                        if (tLRPC$EncryptedChat != null) {
                                            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                            if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatRequested) {
                                                string = LocaleController.getString("EncryptionProcessing", R.string.EncryptionProcessing);
                                            } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatWaiting) {
                                                string = LocaleController.formatString("AwaitingEncryption", R.string.AwaitingEncryption, UserObject.getFirstName(this.user));
                                            } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChatDiscarded) {
                                                string = LocaleController.getString("EncryptionRejected", R.string.EncryptionRejected);
                                            } else if (tLRPC$EncryptedChat instanceof TLRPC$TL_encryptedChat) {
                                                if (tLRPC$EncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                                                    string = LocaleController.formatString("EncryptedChatStartedOutgoing", R.string.EncryptedChatStartedOutgoing, UserObject.getFirstName(this.user));
                                                } else {
                                                    string = LocaleController.getString("EncryptedChatStartedIncoming", R.string.EncryptedChatStartedIncoming);
                                                }
                                            }
                                        } else if (this.dialogsType == 3 && UserObject.isUserSelf(this.user)) {
                                            DialogsActivity dialogsActivity = this.parentFragment;
                                            string = LocaleController.getString((dialogsActivity == null || !dialogsActivity.isQuote) ? R.string.SavedMessagesInfo : R.string.SavedMessagesInfoQuote);
                                            spannableStringBuilder3 = spannableStringBuilder;
                                            str10 = null;
                                            z4 = false;
                                            z5 = false;
                                            z6 = z2;
                                            str11 = str10;
                                            charSequence5 = string;
                                        }
                                        z5 = z10;
                                        string = "";
                                        z4 = true;
                                        spannableStringBuilder3 = spannableStringBuilder;
                                        str10 = null;
                                        z6 = z2;
                                        str11 = str10;
                                        charSequence5 = string;
                                    }
                                } else {
                                    String restrictionReason = MessagesController.getRestrictionReason(messageObject7.messageOwner.restriction_reason);
                                    long fromChatId = this.message.getFromChatId();
                                    if (DialogObject.isUserDialog(fromChatId)) {
                                        MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
                                        chat = null;
                                    } else {
                                        chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-fromChatId));
                                    }
                                    this.drawCount2 = true;
                                    if (this.dialogsType == 0 && this.currentDialogId > 0 && this.message.isOutOwner() && (tLRPC$TL_messageReactions = this.message.messageOwner.reactions) != null && (arrayList = tLRPC$TL_messageReactions.recent_reactions) != null && !arrayList.isEmpty() && this.reactionMentionCount > 0) {
                                        TLRPC$MessagePeerReaction tLRPC$MessagePeerReaction = this.message.messageOwner.reactions.recent_reactions.get(0);
                                        if (tLRPC$MessagePeerReaction.unread) {
                                            charSequence = spannableStringBuilder7;
                                            long j3 = tLRPC$MessagePeerReaction.peer_id.user_id;
                                            if (j3 != 0) {
                                                str = restrictionReason;
                                                str = str;
                                                if (j3 != UserConfig.getInstance(this.currentAccount).clientUserId) {
                                                    ReactionsLayoutInBubble.VisibleReaction fromTLReaction = ReactionsLayoutInBubble.VisibleReaction.fromTLReaction(tLRPC$MessagePeerReaction.reaction);
                                                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                    String str21 = fromTLReaction.emojicon;
                                                    if (str21 != null) {
                                                        charSequence3 = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, str21);
                                                    } else {
                                                        String formatString = LocaleController.formatString("ReactionInDialog", R.string.ReactionInDialog, "**reaction**");
                                                        int indexOf2 = formatString.indexOf("**reaction**");
                                                        SpannableStringBuilder spannableStringBuilder10 = new SpannableStringBuilder(formatString.replace("**reaction**", "d"));
                                                        long j4 = fromTLReaction.documentId;
                                                        TextPaint textPaint6 = this.currentMessagePaint;
                                                        spannableStringBuilder10.setSpan(new AnimatedEmojiSpan(j4, textPaint6 == null ? null : textPaint6.getFontMetricsInt()), indexOf2, indexOf2 + 1, 0);
                                                        charSequence3 = spannableStringBuilder10;
                                                    }
                                                    z3 = true;
                                                    r1 = charSequence3;
                                                    if (!z3) {
                                                        int i15 = this.dialogsType;
                                                        if (i15 == 2) {
                                                            TLRPC$Chat tLRPC$Chat4 = this.chat;
                                                            if (tLRPC$Chat4 != null) {
                                                                if (ChatObject.isChannel(tLRPC$Chat4)) {
                                                                    TLRPC$Chat tLRPC$Chat5 = this.chat;
                                                                    if (!tLRPC$Chat5.megagroup) {
                                                                        int i16 = tLRPC$Chat5.participants_count;
                                                                        if (i16 != 0) {
                                                                            str8 = LocaleController.formatPluralStringComma("Subscribers", i16);
                                                                        } else if (!ChatObject.isPublic(tLRPC$Chat5)) {
                                                                            str8 = LocaleController.getString("ChannelPrivate", R.string.ChannelPrivate).toLowerCase();
                                                                        } else {
                                                                            str8 = LocaleController.getString("ChannelPublic", R.string.ChannelPublic).toLowerCase();
                                                                        }
                                                                    }
                                                                }
                                                                TLRPC$Chat tLRPC$Chat6 = this.chat;
                                                                int i17 = tLRPC$Chat6.participants_count;
                                                                if (i17 != 0) {
                                                                    str8 = LocaleController.formatPluralStringComma("Members", i17);
                                                                } else if (tLRPC$Chat6.has_geo) {
                                                                    str8 = LocaleController.getString("MegaLocation", R.string.MegaLocation);
                                                                } else if (!ChatObject.isPublic(tLRPC$Chat6)) {
                                                                    str8 = LocaleController.getString("MegaPrivate", R.string.MegaPrivate).toLowerCase();
                                                                } else {
                                                                    str8 = LocaleController.getString("MegaPublic", R.string.MegaPublic).toLowerCase();
                                                                }
                                                            } else {
                                                                str8 = "";
                                                            }
                                                            this.drawCount2 = false;
                                                            str7 = str8;
                                                        } else if (i15 == 3 && UserObject.isUserSelf(this.user)) {
                                                            DialogsActivity dialogsActivity2 = this.parentFragment;
                                                            str7 = LocaleController.getString((dialogsActivity2 == null || !dialogsActivity2.isQuote) ? R.string.SavedMessagesInfo : R.string.SavedMessagesInfoQuote);
                                                        } else {
                                                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout && this.currentDialogFolderId != 0) {
                                                                str4 = formatArchivedDialogNames();
                                                                z2 = false;
                                                            } else {
                                                                MessageObject messageObject8 = this.message;
                                                                if ((messageObject8.messageOwner instanceof TLRPC$TL_messageService) && (!MessageObject.isTopicActionMessage(messageObject8) || (this.message.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate))) {
                                                                    if (ChatObject.isChannelAndNotMegaGroup(this.chat) && (this.message.messageOwner.action instanceof TLRPC$TL_messageActionChannelMigrateFrom)) {
                                                                        charSequence2 = "";
                                                                        z10 = false;
                                                                    } else {
                                                                        charSequence2 = charSequence;
                                                                    }
                                                                    this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                    if (this.message.type == 21) {
                                                                        updateMessageThumbs();
                                                                        r1 = applyThumbs(charSequence2);
                                                                    } else {
                                                                        str4 = charSequence2;
                                                                    }
                                                                } else {
                                                                    this.needEmoji = true;
                                                                    updateMessageThumbs();
                                                                    TLRPC$Chat tLRPC$Chat7 = this.chat;
                                                                    if (tLRPC$Chat7 != null && tLRPC$Chat7.id > 0 && chat == null && ((!ChatObject.isChannel(tLRPC$Chat7) || ChatObject.isMegagroup(this.chat)) && !ForumUtilities.isTopicCreateMessage(this.message))) {
                                                                        String messageNameString2 = getMessageNameString();
                                                                        if (this.chat.forum && !this.isTopic && !this.useFromUserAsAvatar) {
                                                                            CharSequence topicIconName = MessagesController.getInstance(this.currentAccount).getTopicsController().getTopicIconName(this.chat, this.message, this.currentMessagePaint);
                                                                            if (!TextUtils.isEmpty(topicIconName)) {
                                                                                SpannableStringBuilder spannableStringBuilder11 = new SpannableStringBuilder("-");
                                                                                ColoredImageSpan coloredImageSpan = new ColoredImageSpan(ContextCompat.getDrawable(ApplicationLoader.applicationContext, R.drawable.msg_mini_forumarrow).mutate());
                                                                                coloredImageSpan.setColorKey((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? -1 : Theme.key_chats_nameMessage);
                                                                                spannableStringBuilder11.setSpan(coloredImageSpan, 0, 1, 0);
                                                                                ?? spannableStringBuilder12 = new SpannableStringBuilder();
                                                                                spannableStringBuilder12.append(messageNameString2).append((CharSequence) spannableStringBuilder11).append(topicIconName);
                                                                                str6 = spannableStringBuilder12;
                                                                                SpannableStringBuilder messageStringFormatted = getMessageStringFormatted(i, str, str6, false);
                                                                                if (!this.useFromUserAsAvatar || ((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) && (this.currentDialogFolderId == 0 || messageStringFormatted.length() <= 0))) {
                                                                                    i3 = 0;
                                                                                } else {
                                                                                    try {
                                                                                        foregroundColorSpanThemable = new ForegroundColorSpanThemable(Theme.key_chats_nameMessage, this.resourcesProvider);
                                                                                        i3 = str6.length() + 1;
                                                                                    } catch (Exception e) {
                                                                                        e = e;
                                                                                        i3 = 0;
                                                                                    }
                                                                                    try {
                                                                                        messageStringFormatted.setSpan(foregroundColorSpanThemable, 0, i3, 33);
                                                                                    } catch (Exception e2) {
                                                                                        e = e2;
                                                                                        FileLog.e(e);
                                                                                        replaceEmoji = Emoji.replaceEmoji(messageStringFormatted, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                        if (this.message.hasHighlightedWords()) {
                                                                                        }
                                                                                        if (this.thumbsCount > 0) {
                                                                                        }
                                                                                        str4 = replaceEmoji;
                                                                                        z2 = false;
                                                                                        z4 = true;
                                                                                        str9 = str6;
                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                        }
                                                                                        if (this.draftMessage != null) {
                                                                                        }
                                                                                        messageObject2 = this.message;
                                                                                        if (messageObject2 == null) {
                                                                                        }
                                                                                        this.promoDialog = false;
                                                                                        messagesController = MessagesController.getInstance(this.currentAccount);
                                                                                        boolean z12 = z6;
                                                                                        if (this.dialogsType == 0) {
                                                                                        }
                                                                                        charSequence6 = charSequence5;
                                                                                        str14 = stringForMessageListDate;
                                                                                        if (this.currentDialogFolderId != 0) {
                                                                                        }
                                                                                        spannableStringBuilder5 = str10;
                                                                                        charSequence7 = charSequence6;
                                                                                        str15 = userName;
                                                                                        i4 = i2;
                                                                                        spannableStringBuilder6 = spannableStringBuilder3;
                                                                                        z7 = z12;
                                                                                        boolean z13 = z4;
                                                                                        str16 = str11;
                                                                                        z8 = z13;
                                                                                        if (!z8) {
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
                                                                                        if (str15 instanceof String) {
                                                                                        }
                                                                                        if (this.nameLayoutEllipsizeByGradient) {
                                                                                        }
                                                                                        int i18 = dp6;
                                                                                        float f = i18;
                                                                                        this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(str15.toString()) <= f;
                                                                                        if (!this.twoLinesForName) {
                                                                                        }
                                                                                        CharSequence replaceEmoji2 = Emoji.replaceEmoji(str15, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                        MessageObject messageObject9 = this.message;
                                                                                        if (messageObject9 == null) {
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
                                                                                            imageReceiverArr[i9].setImageCoords(((this.thumbSize + 2) * i9) + dp3, AndroidUtilities.dp(31.0f) + dp + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
                                                                                            i9++;
                                                                                            dp = dp;
                                                                                        }
                                                                                        int i19 = dp;
                                                                                        int i20 = i8;
                                                                                        if (this.twoLinesForName) {
                                                                                        }
                                                                                        if (getIsPinned()) {
                                                                                        }
                                                                                        if (!this.drawError) {
                                                                                        }
                                                                                        if (z7) {
                                                                                        }
                                                                                        max = Math.max(AndroidUtilities.dp(12.0f), i20);
                                                                                        this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
                                                                                        if (!isForumCell()) {
                                                                                        }
                                                                                        if (this.twoLinesForName) {
                                                                                        }
                                                                                        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack2, this.messageNameLayout);
                                                                                        this.buttonCreated = false;
                                                                                        if (TextUtils.isEmpty(spannableStringBuilder5)) {
                                                                                        }
                                                                                        this.animatedEmojiStack3 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack3, this.buttonLayout);
                                                                                        if (!TextUtils.isEmpty(spannableStringBuilder6)) {
                                                                                        }
                                                                                        z9 = this.useForceThreeLines;
                                                                                        if (!z9) {
                                                                                        }
                                                                                        this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                                                                                        charSequence8 = str16;
                                                                                        charSequence9 = null;
                                                                                        if (charSequence8 instanceof Spannable) {
                                                                                        }
                                                                                        if (this.isForum) {
                                                                                        }
                                                                                        if (!this.useForceThreeLines) {
                                                                                            if (this.thumbsCount > 0) {
                                                                                            }
                                                                                            this.messageLayout = new StaticLayout(charSequence8, this.currentMessagePaint, max, alignment, 1.0f, 0.0f, false);
                                                                                            int i21 = max;
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
                                                                                        if (this.thumbsCount > 0) {
                                                                                            max += AndroidUtilities.dp(5.0f);
                                                                                        }
                                                                                        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence8, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence9 == null ? 1 : 2);
                                                                                        int i212 = max;
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
                                                                                replaceEmoji = Emoji.replaceEmoji(messageStringFormatted, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                                if (this.message.hasHighlightedWords() && (highlightText2 = AndroidUtilities.highlightText(replaceEmoji, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                    replaceEmoji = highlightText2;
                                                                                }
                                                                                if (this.thumbsCount > 0) {
                                                                                    boolean z14 = replaceEmoji instanceof SpannableStringBuilder;
                                                                                    replaceEmoji = replaceEmoji;
                                                                                    if (!z14) {
                                                                                        replaceEmoji = new SpannableStringBuilder(replaceEmoji);
                                                                                    }
                                                                                    SpannableStringBuilder spannableStringBuilder13 = (SpannableStringBuilder) replaceEmoji;
                                                                                    if (i3 >= spannableStringBuilder13.length()) {
                                                                                        spannableStringBuilder13.append((CharSequence) " ");
                                                                                        spannableStringBuilder13.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5)), spannableStringBuilder13.length() - 1, spannableStringBuilder13.length(), 33);
                                                                                    } else {
                                                                                        spannableStringBuilder13.insert(i3, (CharSequence) " ");
                                                                                        spannableStringBuilder13.setSpan(new FixedWidthSpan(AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5)), i3, i3 + 1, 33);
                                                                                    }
                                                                                }
                                                                                str4 = replaceEmoji;
                                                                                z2 = false;
                                                                                z4 = true;
                                                                                str9 = str6;
                                                                                if (this.currentDialogFolderId != 0) {
                                                                                    z6 = z2;
                                                                                    str11 = formatArchivedDialogNames();
                                                                                    z5 = z10;
                                                                                    spannableStringBuilder3 = spannableStringBuilder;
                                                                                    str10 = null;
                                                                                    charSequence5 = str4;
                                                                                } else {
                                                                                    z5 = z10;
                                                                                    spannableStringBuilder3 = spannableStringBuilder;
                                                                                    str10 = null;
                                                                                    String str22 = str9;
                                                                                    z6 = z2;
                                                                                    str11 = str22;
                                                                                    charSequence5 = str4;
                                                                                }
                                                                            }
                                                                        }
                                                                        str6 = messageNameString2;
                                                                        SpannableStringBuilder messageStringFormatted2 = getMessageStringFormatted(i, str, str6, false);
                                                                        if (this.useFromUserAsAvatar) {
                                                                        }
                                                                        i3 = 0;
                                                                        replaceEmoji = Emoji.replaceEmoji(messageStringFormatted2, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
                                                                        if (this.message.hasHighlightedWords()) {
                                                                            replaceEmoji = highlightText2;
                                                                        }
                                                                        if (this.thumbsCount > 0) {
                                                                        }
                                                                        str4 = replaceEmoji;
                                                                        z2 = false;
                                                                        z4 = true;
                                                                        str9 = str6;
                                                                        if (this.currentDialogFolderId != 0) {
                                                                        }
                                                                    } else {
                                                                        SpannableStringBuilder spannableStringBuilder14 = str;
                                                                        if (TextUtils.isEmpty(spannableStringBuilder14)) {
                                                                            if (MessageObject.isTopicActionMessage(this.message)) {
                                                                                MessageObject messageObject10 = this.message;
                                                                                str4 = messageObject10.messageTextShort;
                                                                                if (str4 == null || ((messageObject10.messageOwner.action instanceof TLRPC$TL_messageActionTopicCreate) && this.isTopic)) {
                                                                                    str4 = messageObject10.messageText;
                                                                                }
                                                                                if ((messageObject10.topicIconDrawable[0] instanceof ForumBubbleDrawable) && (findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.message.messageOwner, true))) != null) {
                                                                                    ((ForumBubbleDrawable) this.message.topicIconDrawable[0]).setColor(findTopic.icon_color);
                                                                                }
                                                                            } else {
                                                                                TLRPC$MessageMedia tLRPC$MessageMedia = this.message.messageOwner.media;
                                                                                if ((tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaPhoto) && (tLRPC$MessageMedia.photo instanceof TLRPC$TL_photoEmpty) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                                    str4 = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
                                                                                } else {
                                                                                    if (tLRPC$MessageMedia instanceof TLRPC$TL_messageMediaDocument) {
                                                                                        TLRPC$Document tLRPC$Document = tLRPC$MessageMedia.document;
                                                                                        if (((tLRPC$Document instanceof TLRPC$TL_documentEmpty) || tLRPC$Document == null) && tLRPC$MessageMedia.ttl_seconds != 0) {
                                                                                            str4 = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
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
                                                                                            CharSequence charSequence13 = captionMessage.messageTrimmedToHighlight;
                                                                                            int measuredWidth2 = getMeasuredWidth() - AndroidUtilities.dp((this.messagePaddingStart + 23) + 24);
                                                                                            if (this.hasNameInMessage) {
                                                                                                if (!TextUtils.isEmpty(null)) {
                                                                                                    throw null;
                                                                                                }
                                                                                                measuredWidth2 = (int) (measuredWidth2 - this.currentMessagePaint.measureText(": "));
                                                                                            }
                                                                                            if (measuredWidth2 > 0) {
                                                                                                charSequence13 = AndroidUtilities.ellipsizeCenterEnd(charSequence13, captionMessage.highlightedWords.get(0), measuredWidth2, this.currentMessagePaint, 130).toString();
                                                                                            }
                                                                                            spannableStringBuilder14 = new SpannableStringBuilder(str5).append(charSequence13);
                                                                                        } else {
                                                                                            SpannableStringBuilder spannableStringBuilder15 = new SpannableStringBuilder(captionMessage.caption);
                                                                                            if (captionMessage.messageOwner != null) {
                                                                                                captionMessage.spoilLoginCode();
                                                                                                MediaDataController.addTextStyleRuns(captionMessage.messageOwner.entities, captionMessage.caption, spannableStringBuilder15, 264);
                                                                                                ArrayList<TLRPC$MessageEntity> arrayList3 = captionMessage.messageOwner.entities;
                                                                                                TextPaint textPaint7 = this.currentMessagePaint;
                                                                                                MediaDataController.addAnimatedEmojiSpans(arrayList3, spannableStringBuilder15, textPaint7 == null ? null : textPaint7.getFontMetricsInt());
                                                                                            }
                                                                                            spannableStringBuilder14 = new SpannableStringBuilder(str5).append((CharSequence) spannableStringBuilder15);
                                                                                        }
                                                                                    } else if (this.thumbsCount > 1) {
                                                                                        if (this.hasVideoThumb) {
                                                                                            ArrayList<MessageObject> arrayList4 = this.groupMessages;
                                                                                            formatPluralString = LocaleController.formatPluralString("Media", arrayList4 == null ? 0 : arrayList4.size(), new Object[0]);
                                                                                        } else {
                                                                                            ArrayList<MessageObject> arrayList5 = this.groupMessages;
                                                                                            formatPluralString = LocaleController.formatPluralString("Photos", arrayList5 == null ? 0 : arrayList5.size(), new Object[0]);
                                                                                        }
                                                                                        str4 = formatPluralString;
                                                                                        this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                    } else {
                                                                                        MessageObject messageObject11 = this.message;
                                                                                        TLRPC$MessageMedia tLRPC$MessageMedia2 = messageObject11.messageOwner.media;
                                                                                        if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGiveaway) {
                                                                                            TLRPC$TL_messageMediaGiveaway tLRPC$TL_messageMediaGiveaway = (TLRPC$TL_messageMediaGiveaway) tLRPC$MessageMedia2;
                                                                                            str3 = LocaleController.getString("BoostingGiveaway", R.string.BoostingGiveaway);
                                                                                        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaPoll) {
                                                                                            str3 = "📊 " + ((TLRPC$TL_messageMediaPoll) tLRPC$MessageMedia2).poll.question;
                                                                                        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaGame) {
                                                                                            str3 = "🎮 " + this.message.messageOwner.media.game.title;
                                                                                        } else if (tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaInvoice) {
                                                                                            str3 = tLRPC$MessageMedia2.title;
                                                                                        } else if (messageObject11.type == 14) {
                                                                                            str3 = String.format("🎧 %s - %s", messageObject11.getMusicAuthor(), this.message.getMusicTitle());
                                                                                        } else if ((tLRPC$MessageMedia2 instanceof TLRPC$TL_messageMediaStory) && tLRPC$MessageMedia2.via_mention) {
                                                                                            if (messageObject11.isOut()) {
                                                                                                TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.getDialogId()));
                                                                                                if (user != null) {
                                                                                                    str2 = UserObject.getFirstName(user);
                                                                                                    int indexOf3 = str2.indexOf(32);
                                                                                                    if (indexOf3 >= 0) {
                                                                                                        str2 = str2.substring(0, indexOf3);
                                                                                                    }
                                                                                                } else {
                                                                                                    str2 = "";
                                                                                                }
                                                                                                str3 = LocaleController.formatString("StoryYouMentionInDialog", R.string.StoryYouMentionInDialog, str2);
                                                                                            } else {
                                                                                                str3 = LocaleController.getString("StoryMentionInDialog", R.string.StoryMentionInDialog);
                                                                                            }
                                                                                        } else {
                                                                                            if (messageObject11.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                                spannableStringBuilder2 = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp(this.messagePaddingStart + 23), this.currentMessagePaint, 130);
                                                                                            } else {
                                                                                                SpannableStringBuilder spannableStringBuilder16 = new SpannableStringBuilder(charSequence);
                                                                                                MessageObject messageObject12 = this.message;
                                                                                                if (messageObject12 != null) {
                                                                                                    messageObject12.spoilLoginCode();
                                                                                                }
                                                                                                MediaDataController.addTextStyleRuns(this.message, spannableStringBuilder16, 264);
                                                                                                MessageObject messageObject13 = this.message;
                                                                                                spannableStringBuilder2 = spannableStringBuilder16;
                                                                                                if (messageObject13 != null) {
                                                                                                    TLRPC$Message tLRPC$Message = messageObject13.messageOwner;
                                                                                                    spannableStringBuilder2 = spannableStringBuilder16;
                                                                                                    if (tLRPC$Message != null) {
                                                                                                        ArrayList<TLRPC$MessageEntity> arrayList6 = tLRPC$Message.entities;
                                                                                                        TextPaint textPaint8 = this.currentMessagePaint;
                                                                                                        MediaDataController.addAnimatedEmojiSpans(arrayList6, spannableStringBuilder16, textPaint8 == null ? null : textPaint8.getFontMetricsInt());
                                                                                                        spannableStringBuilder2 = spannableStringBuilder16;
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            AndroidUtilities.highlightText(spannableStringBuilder2, this.message.highlightedWords, this.resourcesProvider);
                                                                                            str3 = spannableStringBuilder2;
                                                                                        }
                                                                                        str4 = str3;
                                                                                        MessageObject messageObject14 = this.message;
                                                                                        if (messageObject14.messageOwner.media != null && !messageObject14.isMediaEmpty()) {
                                                                                            this.currentMessagePaint = Theme.dialogs_messagePrintingPaint[this.paintIndex];
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            if (this.message.isReplyToStory()) {
                                                                                SpannableStringBuilder spannableStringBuilder17 = new SpannableStringBuilder(str4);
                                                                                spannableStringBuilder17.insert(0, (CharSequence) "d ");
                                                                                spannableStringBuilder17.setSpan(new ColoredImageSpan(ContextCompat.getDrawable(getContext(), R.drawable.msg_mini_replystory).mutate()), 0, 1, 0);
                                                                                str4 = spannableStringBuilder17;
                                                                            }
                                                                            if (this.thumbsCount > 0) {
                                                                                if (this.message.hasHighlightedWords() && !TextUtils.isEmpty(this.message.messageOwner.message)) {
                                                                                    replaceNewLines = AndroidUtilities.ellipsizeCenterEnd(this.message.messageTrimmedToHighlight, this.message.highlightedWords.get(0), getMeasuredWidth() - AndroidUtilities.dp((((this.messagePaddingStart + 23) + ((this.thumbSize + 2) * this.thumbsCount)) - 2) + 5), this.currentMessagePaint, 130).toString();
                                                                                } else {
                                                                                    if (str4.length() > 150) {
                                                                                        str4 = str4.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
                                                                                    }
                                                                                    replaceNewLines = AndroidUtilities.replaceNewLines(str4);
                                                                                }
                                                                                str4 = !(replaceNewLines instanceof SpannableStringBuilder) ? new SpannableStringBuilder(replaceNewLines) : replaceNewLines;
                                                                                SpannableStringBuilder spannableStringBuilder18 = (SpannableStringBuilder) str4;
                                                                                spannableStringBuilder18.insert(0, (CharSequence) " ");
                                                                                spannableStringBuilder18.setSpan(new FixedWidthSpan(AndroidUtilities.dp((((this.thumbSize + 2) * this.thumbsCount) - 2) + 5)), 0, 1, 33);
                                                                                Emoji.replaceEmoji(spannableStringBuilder18, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
                                                                                if (this.message.hasHighlightedWords() && (highlightText = AndroidUtilities.highlightText(spannableStringBuilder18, this.message.highlightedWords, this.resourcesProvider)) != null) {
                                                                                    str4 = highlightText;
                                                                                }
                                                                                z2 = false;
                                                                            }
                                                                            if (this.message.isForwarded()) {
                                                                                this.drawForwardIcon = true;
                                                                                r1 = new SpannableStringBuilder(str4);
                                                                                r1.insert(0, "d ");
                                                                                ColoredImageSpan coloredImageSpan2 = new ColoredImageSpan(ContextCompat.getDrawable(getContext(), R.drawable.mini_forwarded).mutate());
                                                                                coloredImageSpan2.setAlpha(0.9f);
                                                                                r1.setSpan(coloredImageSpan2, 0, 1, 0);
                                                                            }
                                                                        }
                                                                        str4 = spannableStringBuilder14;
                                                                        if (this.message.isReplyToStory()) {
                                                                        }
                                                                        if (this.thumbsCount > 0) {
                                                                        }
                                                                        if (this.message.isForwarded()) {
                                                                        }
                                                                    }
                                                                }
                                                                str9 = null;
                                                                if (this.currentDialogFolderId != 0) {
                                                                }
                                                            }
                                                            z4 = true;
                                                            str9 = null;
                                                            if (this.currentDialogFolderId != 0) {
                                                            }
                                                        }
                                                        str4 = str7;
                                                        z4 = false;
                                                        z10 = false;
                                                        str9 = null;
                                                        if (this.currentDialogFolderId != 0) {
                                                        }
                                                    }
                                                    str4 = r1;
                                                    z4 = true;
                                                    str9 = null;
                                                    if (this.currentDialogFolderId != 0) {
                                                    }
                                                }
                                            } else {
                                                str = restrictionReason;
                                            }
                                            r1 = "";
                                            z3 = false;
                                            if (!z3) {
                                            }
                                            str4 = r1;
                                            z4 = true;
                                            str9 = null;
                                            if (this.currentDialogFolderId != 0) {
                                            }
                                        }
                                    }
                                    str = restrictionReason;
                                    charSequence = spannableStringBuilder7;
                                    r1 = "";
                                    z3 = false;
                                    if (!z3) {
                                    }
                                    str4 = r1;
                                    z4 = true;
                                    str9 = null;
                                    if (this.currentDialogFolderId != 0) {
                                    }
                                }
                            }
                            z5 = z10;
                            z4 = true;
                            spannableStringBuilder3 = spannableStringBuilder;
                            str10 = null;
                            z6 = z2;
                            str11 = str10;
                            charSequence5 = string;
                        }
                    }
                    if (this.draftMessage != null) {
                        stringForMessageListDate = LocaleController.stringForMessageListDate(tLRPC$DraftMessage.date);
                    } else {
                        int i22 = this.lastMessageDate;
                        if (i22 != 0) {
                            stringForMessageListDate = LocaleController.stringForMessageListDate(i22);
                        } else {
                            stringForMessageListDate = this.message != null ? LocaleController.stringForMessageListDate(messageObject.messageOwner.date) : "";
                        }
                    }
                    messageObject2 = this.message;
                    if (messageObject2 == null) {
                        this.drawCheck1 = false;
                        this.drawCheck2 = false;
                        this.drawClock = false;
                        this.drawCount = false;
                        this.drawMention = false;
                        this.drawReactionMention = false;
                        this.drawError = false;
                        str12 = null;
                        str13 = null;
                    } else {
                        if (this.currentDialogFolderId != 0) {
                            int i23 = this.unreadCount;
                            int i24 = this.mentionCount;
                            if (i23 + i24 <= 0) {
                                this.drawCount = false;
                                this.drawMention = false;
                                str12 = null;
                            } else if (i23 > i24) {
                                this.drawCount = true;
                                this.drawMention = false;
                                str12 = String.format("%d", Integer.valueOf(i23 + i24));
                            } else {
                                this.drawCount = false;
                                this.drawMention = true;
                                str13 = String.format("%d", Integer.valueOf(i23 + i24));
                                str12 = null;
                                this.drawReactionMention = false;
                            }
                            str13 = null;
                            this.drawReactionMention = false;
                        } else {
                            if (this.clearingDialog) {
                                this.drawCount = false;
                                z5 = false;
                            } else {
                                int i25 = this.unreadCount;
                                if (i25 != 0 && (i25 != 1 || i25 != this.mentionCount || messageObject2 == null || !messageObject2.messageOwner.mentioned)) {
                                    this.drawCount = true;
                                    str12 = String.format("%d", Integer.valueOf(i25));
                                } else if (this.markUnread) {
                                    this.drawCount = true;
                                    str12 = "";
                                } else {
                                    this.drawCount = false;
                                }
                                if (this.mentionCount == 0) {
                                    this.drawMention = true;
                                    str13 = "@";
                                } else {
                                    this.drawMention = false;
                                    str13 = null;
                                }
                                if (this.reactionMentionCount <= 0) {
                                    this.drawReactionMention = true;
                                } else {
                                    this.drawReactionMention = false;
                                }
                            }
                            str12 = null;
                            if (this.mentionCount == 0) {
                            }
                            if (this.reactionMentionCount <= 0) {
                            }
                        }
                        if (this.message.isOut() && this.draftMessage == null && z5) {
                            MessageObject messageObject15 = this.message;
                            if (!(messageObject15.messageOwner.action instanceof TLRPC$TL_messageActionHistoryClear)) {
                                if (messageObject15.isSending()) {
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
                                        int i26 = this.readOutboxMaxId;
                                        this.drawCheck1 = (i26 > 0 && i26 >= this.message.getId()) || !this.message.isUnread() || (ChatObject.isChannel(this.chat) && !this.chat.megagroup);
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
                    boolean z122 = z6;
                    if (this.dialogsType == 0 && messagesController.isPromoDialog(this.currentDialogId, true)) {
                        this.drawPinBackground = true;
                        this.promoDialog = true;
                        i5 = messagesController.promoDialogType;
                        if (i5 != MessagesController.PROMO_TYPE_PROXY) {
                            charSequence6 = charSequence5;
                            str14 = LocaleController.getString("UseProxySponsor", R.string.UseProxySponsor);
                        } else if (i5 == MessagesController.PROMO_TYPE_PSA) {
                            String string3 = LocaleController.getString("PsaType_" + messagesController.promoPsaType);
                            if (TextUtils.isEmpty(string3)) {
                                string3 = LocaleController.getString("PsaTypeDefault", R.string.PsaTypeDefault);
                            }
                            if (!TextUtils.isEmpty(messagesController.promoPsaMessage)) {
                                charSequence5 = messagesController.promoPsaMessage;
                                this.thumbsCount = 0;
                            }
                            charSequence6 = charSequence5;
                            str14 = string3;
                        }
                        if (this.currentDialogFolderId != 0) {
                            userName = LocaleController.getString("ArchivedChats", R.string.ArchivedChats);
                        } else {
                            TLRPC$Chat tLRPC$Chat8 = this.chat;
                            if (tLRPC$Chat8 != null) {
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
                                    userName = tLRPC$Chat8.title;
                                }
                                if (userName != null && userName.length() == 0) {
                                    userName = LocaleController.getString("HiddenName", R.string.HiddenName);
                                }
                            } else {
                                TLRPC$User tLRPC$User2 = this.user;
                                if (tLRPC$User2 != null) {
                                    if (UserObject.isReplyUser(tLRPC$User2)) {
                                        userName = LocaleController.getString("RepliesTitle", R.string.RepliesTitle);
                                    } else if (UserObject.isUserSelf(this.user)) {
                                        if (this.useMeForMyMessages) {
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
                        spannableStringBuilder5 = str10;
                        charSequence7 = charSequence6;
                        str15 = userName;
                        i4 = i2;
                        spannableStringBuilder6 = spannableStringBuilder3;
                        z7 = z122;
                        boolean z132 = z4;
                        str16 = str11;
                        z8 = z132;
                    }
                    charSequence6 = charSequence5;
                    str14 = stringForMessageListDate;
                    if (this.currentDialogFolderId != 0) {
                    }
                    spannableStringBuilder5 = str10;
                    charSequence7 = charSequence6;
                    str15 = userName;
                    i4 = i2;
                    spannableStringBuilder6 = spannableStringBuilder3;
                    z7 = z122;
                    boolean z1322 = z4;
                    str16 = str11;
                    z8 = z1322;
                }
            }
            this.draftMessage = null;
            if (!isForumCell()) {
            }
            if (this.draftMessage != null) {
            }
            messageObject2 = this.message;
            if (messageObject2 == null) {
            }
            this.promoDialog = false;
            messagesController = MessagesController.getInstance(this.currentAccount);
            boolean z1222 = z6;
            if (this.dialogsType == 0) {
                this.drawPinBackground = true;
                this.promoDialog = true;
                i5 = messagesController.promoDialogType;
                if (i5 != MessagesController.PROMO_TYPE_PROXY) {
                }
                if (this.currentDialogFolderId != 0) {
                }
                spannableStringBuilder5 = str10;
                charSequence7 = charSequence6;
                str15 = userName;
                i4 = i2;
                spannableStringBuilder6 = spannableStringBuilder3;
                z7 = z1222;
                boolean z13222 = z4;
                str16 = str11;
                z8 = z13222;
            }
            charSequence6 = charSequence5;
            str14 = stringForMessageListDate;
            if (this.currentDialogFolderId != 0) {
            }
            spannableStringBuilder5 = str10;
            charSequence7 = charSequence6;
            str15 = userName;
            i4 = i2;
            spannableStringBuilder6 = spannableStringBuilder3;
            z7 = z1222;
            boolean z132222 = z4;
            str16 = str11;
            z8 = z132222;
        }
        if (!z8) {
            int ceil = (int) Math.ceil(Theme.dialogs_timePaint.measureText(str14));
            this.timeLayout = new StaticLayout(str14, Theme.dialogs_timePaint, ceil, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (!LocaleController.isRTL) {
                this.timeLeft = (getMeasuredWidth() - AndroidUtilities.dp(15.0f)) - ceil;
            } else {
                this.timeLeft = AndroidUtilities.dp(15.0f);
            }
            i6 = ceil;
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
            int i27 = this.nameWidth - intrinsicWidth3;
            this.nameWidth = i27;
            if (this.drawCheck1) {
                this.nameWidth = i27 - (Theme.dialogs_halfCheckDrawable.getIntrinsicWidth() - AndroidUtilities.dp(8.0f));
                if (!LocaleController.isRTL) {
                    int i28 = (this.timeLeft - i7) - intrinsicWidth3;
                    this.halfCheckDrawLeft = i28;
                    this.checkDrawLeft = i28 - AndroidUtilities.dp(5.5f);
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
            if (str15 instanceof String) {
                str15 = ((String) str15).replace('\n', ' ');
            }
            if (this.nameLayoutEllipsizeByGradient) {
                this.nameLayoutFits = str15.length() == TextUtils.ellipsize(str15, Theme.dialogs_namePaint[this.paintIndex], (float) dp6, TextUtils.TruncateAt.END).length();
                dp6 += AndroidUtilities.dp(48.0f);
            }
            int i182 = dp6;
            float f2 = i182;
            this.nameIsEllipsized = Theme.dialogs_namePaint[this.paintIndex].measureText(str15.toString()) <= f2;
            if (!this.twoLinesForName) {
                str15 = TextUtils.ellipsize(str15, Theme.dialogs_namePaint[this.paintIndex], f2, TextUtils.TruncateAt.END);
            }
            CharSequence replaceEmoji22 = Emoji.replaceEmoji(str15, Theme.dialogs_namePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(20.0f), false);
            MessageObject messageObject92 = this.message;
            CharSequence charSequence14 = (messageObject92 == null && messageObject92.hasHighlightedWords() && (highlightText3 = AndroidUtilities.highlightText(replaceEmoji22, this.message.highlightedWords, this.resourcesProvider)) != null) ? highlightText3 : replaceEmoji22;
            if (!this.twoLinesForName) {
                this.nameLayout = StaticLayoutEx.createStaticLayout(charSequence14, Theme.dialogs_namePaint[this.paintIndex], i182, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, i182, 2);
            } else {
                this.nameLayout = new StaticLayout(charSequence14, Theme.dialogs_namePaint[this.paintIndex], Math.max(i182, this.nameWidth), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
                imageReceiverArr[i9].setImageCoords(((this.thumbSize + 2) * i9) + dp3, AndroidUtilities.dp(31.0f) + dp + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0), AndroidUtilities.dp(18.0f), AndroidUtilities.dp(18.0f));
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
            int i29 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr2 = this.thumbImage;
                if (i29 >= imageReceiverArr2.length) {
                    break;
                }
                imageReceiverArr2[i29].setImageCoords(((this.thumbSize + 2) * i29) + dp5, AndroidUtilities.dp(30.0f) + dp + (this.twoLinesForName ? AndroidUtilities.dp(20.0f) : 0), AndroidUtilities.dp(this.thumbSize), AndroidUtilities.dp(this.thumbSize));
                i29++;
                dp = dp;
            }
        }
        int i192 = dp;
        int i202 = i8;
        if (this.twoLinesForName) {
            this.messageNameTop += AndroidUtilities.dp(20.0f);
        }
        if (getIsPinned()) {
            if (!LocaleController.isRTL) {
                this.pinLeft = (getMeasuredWidth() - Theme.dialogs_pinnedDrawable.getIntrinsicWidth()) - AndroidUtilities.dp(14.0f);
            } else {
                this.pinLeft = AndroidUtilities.dp(14.0f);
            }
        }
        if (!this.drawError) {
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
        } else if (str12 != null || str13 != null || this.drawReactionMention) {
            if (str12 != null) {
                this.countWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str12)));
                this.countLayout = new StaticLayout(str12, Theme.dialogs_countTextPaint, this.countWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
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
            if (str13 != null) {
                if (this.currentDialogFolderId != 0) {
                    this.mentionWidth = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(str13)));
                    this.mentionLayout = new StaticLayout(str13, Theme.dialogs_countTextPaint, this.mentionWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                } else {
                    this.mentionWidth = AndroidUtilities.dp(12.0f);
                }
                int dp19 = this.mentionWidth + AndroidUtilities.dp(18.0f);
                i202 -= dp19;
                if (!LocaleController.isRTL) {
                    int measuredWidth5 = (getMeasuredWidth() - this.mentionWidth) - AndroidUtilities.dp(20.0f);
                    int i30 = this.countWidth;
                    this.mentionLeft = measuredWidth5 - (i30 != 0 ? i30 + AndroidUtilities.dp(18.0f) : 0);
                } else {
                    int dp20 = AndroidUtilities.dp(20.0f);
                    int i31 = this.countWidth;
                    this.mentionLeft = dp20 + (i31 != 0 ? i31 + AndroidUtilities.dp(18.0f) : 0);
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
                        int i32 = this.mentionWidth;
                        this.reactionMentionLeft = measuredWidth6 - (i32 != 0 ? i32 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i33 = this.reactionMentionLeft;
                        int i34 = this.countWidth;
                        this.reactionMentionLeft = i33 - (i34 != 0 ? i34 + AndroidUtilities.dp(18.0f) : 0);
                    }
                } else {
                    int dp22 = AndroidUtilities.dp(20.0f);
                    this.reactionMentionLeft = dp22;
                    if (this.drawMention) {
                        int i35 = this.mentionWidth;
                        this.reactionMentionLeft = dp22 + (i35 != 0 ? i35 + AndroidUtilities.dp(18.0f) : 0);
                    }
                    if (this.drawCount) {
                        int i36 = this.reactionMentionLeft;
                        int i37 = this.countWidth;
                        this.reactionMentionLeft = i36 + (i37 != 0 ? i37 + AndroidUtilities.dp(18.0f) : 0);
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
        if (z7) {
            if (charSequence7 == null) {
                charSequence7 = "";
            }
            if (charSequence7.length() > 150) {
                charSequence7 = charSequence7.subSequence(0, ImageReceiver.DEFAULT_CROSSFADE_DURATION);
            }
            if ((!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) || str16 != null) {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceNewLines(charSequence7);
            } else {
                replaceTwoNewLinesToOne = AndroidUtilities.replaceTwoNewLinesToOne(charSequence7);
            }
            CharSequence replaceEmoji3 = Emoji.replaceEmoji(replaceTwoNewLinesToOne, Theme.dialogs_messagePaint[this.paintIndex].getFontMetricsInt(), AndroidUtilities.dp(17.0f), false);
            MessageObject messageObject16 = this.message;
            if (messageObject16 == null || (charSequence7 = AndroidUtilities.highlightText(replaceEmoji3, messageObject16.highlightedWords, this.resourcesProvider)) == null) {
                charSequence7 = replaceEmoji3;
            }
        }
        max = Math.max(AndroidUtilities.dp(12.0f), i202);
        this.buttonTop = AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 58.0f : 62.0f);
        if (!isForumCell()) {
            if (this.useForceThreeLines || SharedConfig.useThreeLinesLayout) {
                this.messageTop = AndroidUtilities.dp(34.0f);
            } else {
                this.messageTop = AndroidUtilities.dp(39.0f);
            }
            int i38 = 0;
            while (true) {
                ImageReceiver[] imageReceiverArr3 = this.thumbImage;
                if (i38 >= imageReceiverArr3.length) {
                    break;
                }
                imageReceiverArr3[i38].setImageY(this.buttonTop);
                i38++;
            }
        } else {
            boolean z15 = this.useForceThreeLines;
            if ((z15 || SharedConfig.useThreeLinesLayout) && str16 != null && (this.currentDialogFolderId == 0 || this.currentDialogFolderDialogsCount == 1)) {
                try {
                    MessageObject messageObject17 = this.message;
                    str16 = str16;
                    if (messageObject17 != null) {
                        str16 = str16;
                        if (messageObject17.hasHighlightedWords()) {
                            CharSequence highlightText4 = AndroidUtilities.highlightText(str16, this.message.highlightedWords, this.resourcesProvider);
                            str16 = str16;
                            if (highlightText4 != null) {
                                str16 = highlightText4;
                            }
                        }
                    }
                    this.messageNameLayout = StaticLayoutEx.createStaticLayout(str16, Theme.dialogs_messageNamePaint, max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TextUtils.TruncateAt.END, max, 1);
                } catch (Exception e4) {
                    FileLog.e(e4);
                }
                this.messageTop = AndroidUtilities.dp(51.0f);
                int dp23 = (this.nameIsEllipsized && this.isTopic) ? AndroidUtilities.dp(20.0f) : 0;
                int i39 = 0;
                while (true) {
                    ImageReceiver[] imageReceiverArr4 = this.thumbImage;
                    if (i39 >= imageReceiverArr4.length) {
                        break;
                    }
                    imageReceiverArr4[i39].setImageY(i192 + dp23 + AndroidUtilities.dp(40.0f));
                    i39++;
                }
            } else {
                this.messageNameLayout = null;
                if (z15 || SharedConfig.useThreeLinesLayout) {
                    this.messageTop = AndroidUtilities.dp(32.0f);
                    int dp24 = (this.nameIsEllipsized && this.isTopic) ? AndroidUtilities.dp(20.0f) : 0;
                    int i40 = 0;
                    while (true) {
                        ImageReceiver[] imageReceiverArr5 = this.thumbImage;
                        if (i40 >= imageReceiverArr5.length) {
                            break;
                        }
                        imageReceiverArr5[i40].setImageY(i192 + dp24 + AndroidUtilities.dp(21.0f));
                        i40++;
                    }
                } else {
                    this.messageTop = AndroidUtilities.dp(39.0f);
                }
            }
        }
        if (this.twoLinesForName) {
            this.messageTop += AndroidUtilities.dp(20.0f);
        }
        this.animatedEmojiStack2 = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack2, this.messageNameLayout);
        try {
            this.buttonCreated = false;
            if (TextUtils.isEmpty(spannableStringBuilder5)) {
                this.buttonLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji(spannableStringBuilder5, this.currentMessagePaint.getFontMetricsInt(), AndroidUtilities.dp(17.0f), false), this.currentMessagePaint, max - AndroidUtilities.dp(26.0f), TextUtils.TruncateAt.END), this.currentMessagePaint, max - AndroidUtilities.dp(20.0f), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
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
            if (!TextUtils.isEmpty(spannableStringBuilder6)) {
                if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                    this.typingLayout = new StaticLayout(TextUtils.ellipsize(spannableStringBuilder6, this.currentMessagePaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END), Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                }
                this.typingLayout = StaticLayoutEx.createStaticLayout(spannableStringBuilder6, Theme.dialogs_messagePrintingPaint[this.paintIndex], max, Layout.Alignment.ALIGN_NORMAL, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, 1);
            }
        } catch (Exception e5) {
            FileLog.e(e5);
        }
        try {
            z9 = this.useForceThreeLines;
            if ((!z9 || SharedConfig.useThreeLinesLayout) && this.currentDialogFolderId != 0 && this.currentDialogFolderDialogsCount > 1) {
                this.currentMessagePaint = Theme.dialogs_messagePaint[this.paintIndex];
                charSequence8 = str16;
                charSequence9 = null;
            } else {
                if ((!z9 && !SharedConfig.useThreeLinesLayout) || str16 != null) {
                    if (!isForumCell() && (charSequence7 instanceof Spanned) && ((FixedWidthSpan[]) ((Spanned) charSequence7).getSpans(0, charSequence7.length(), FixedWidthSpan.class)).length <= 0) {
                        charSequence7 = TextUtils.ellipsize(charSequence7, this.currentMessagePaint, max - AndroidUtilities.dp((((this.thumbsCount * (this.thumbSize + 2)) - 2) + 12) + 5), TextUtils.TruncateAt.END);
                    } else {
                        charSequence7 = TextUtils.ellipsize(charSequence7, this.currentMessagePaint, max - AndroidUtilities.dp(12.0f), TextUtils.TruncateAt.END);
                    }
                }
                charSequence9 = str16;
                charSequence8 = charSequence7;
            }
            if (charSequence8 instanceof Spannable) {
                Spannable spannable = (Spannable) charSequence8;
                for (Object obj : spannable.getSpans(0, spannable.length(), Object.class)) {
                    if ((obj instanceof ClickableSpan) || (obj instanceof CodeHighlighting.Span) || (obj instanceof QuoteSpan) || (obj instanceof QuoteSpan.QuoteStyleSpan) || ((obj instanceof StyleSpan) && ((StyleSpan) obj).getStyle() == 1)) {
                        spannable.removeSpan(obj);
                    }
                }
            }
            alignment = (this.isForum || !LocaleController.isRTL) ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
        } catch (Exception e6) {
            e = e6;
        }
        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
            if (this.thumbsCount > 0) {
                max += AndroidUtilities.dp(((i11 * (this.thumbSize + 2)) - 2) + 5);
                if (LocaleController.isRTL && !isForumCell()) {
                    this.messageLeft -= AndroidUtilities.dp(((this.thumbsCount * (this.thumbSize + 2)) - 2) + 5);
                }
            }
            this.messageLayout = new StaticLayout(charSequence8, this.currentMessagePaint, max, alignment, 1.0f, 0.0f, false);
            int i2122 = max;
            this.spoilersPool.addAll(this.spoilers);
            this.spoilers.clear();
            SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
            this.animatedEmojiStack = AnimatedEmojiSpan.update(0, (View) this, this.animatedEmojiStack, this.messageLayout);
            if (LocaleController.isRTL) {
                StaticLayout staticLayout2 = this.nameLayout;
                if (staticLayout2 != null && staticLayout2.getLineCount() > 0) {
                    float lineLeft = this.nameLayout.getLineLeft(0);
                    double ceil2 = Math.ceil(this.nameLayout.getLineWidth(0));
                    this.nameLeft += AndroidUtilities.dp(12.0f);
                    if (this.nameLayoutEllipsizeByGradient) {
                        ceil2 = Math.min(this.nameWidth, ceil2);
                    }
                    if ((this.dialogMuted || this.drawUnmute) && !this.drawVerified && this.drawScam == 0) {
                        double d = this.nameLeft;
                        double d2 = this.nameWidth;
                        Double.isNaN(d2);
                        Double.isNaN(d);
                        double d3 = d + (d2 - ceil2);
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
                        double d7 = d5 + (d6 - ceil2);
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
                        double d12 = d9 + ((d10 - ceil2) - d11);
                        double dp27 = AndroidUtilities.dp(24.0f);
                        Double.isNaN(dp27);
                        this.nameMuteLeft = (int) (d12 - dp27);
                    } else if (this.drawScam != 0) {
                        double d13 = this.nameLeft;
                        double d14 = this.nameWidth;
                        Double.isNaN(d14);
                        Double.isNaN(d13);
                        double d15 = d13 + (d14 - ceil2);
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
                        double d19 = d17 + (d18 - ceil2);
                        double dp29 = AndroidUtilities.dp(6.0f);
                        Double.isNaN(dp29);
                        double d20 = d19 - dp29;
                        double intrinsicWidth8 = Theme.dialogs_muteDrawable.getIntrinsicWidth();
                        Double.isNaN(intrinsicWidth8);
                        this.nameMuteLeft = (int) (d20 - intrinsicWidth8);
                    }
                    if (lineLeft == 0.0f) {
                        int i41 = this.nameWidth;
                        if (ceil2 < i41) {
                            double d21 = this.nameLeft;
                            double d22 = i41;
                            Double.isNaN(d22);
                            Double.isNaN(d21);
                            this.nameLeft = (int) (d21 + (d22 - ceil2));
                        }
                    }
                }
                StaticLayout staticLayout3 = this.messageLayout;
                int i42 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                if (staticLayout3 != null && (lineCount6 = staticLayout3.getLineCount()) > 0) {
                    int i43 = 0;
                    int i44 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    while (true) {
                        if (i43 >= lineCount6) {
                            break;
                        }
                        if (this.messageLayout.getLineLeft(i43) != 0.0f) {
                            i44 = 0;
                            break;
                        }
                        double ceil3 = Math.ceil(this.messageLayout.getLineWidth(i43));
                        double d23 = i2122;
                        Double.isNaN(d23);
                        i44 = Math.min(i44, (int) (d23 - ceil3));
                        i43++;
                    }
                    if (i44 != Integer.MAX_VALUE) {
                        this.messageLeft += i44;
                    }
                }
                StaticLayout staticLayout4 = this.typingLayout;
                if (staticLayout4 != null && (lineCount5 = staticLayout4.getLineCount()) > 0) {
                    int i45 = 0;
                    int i46 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    while (true) {
                        if (i45 >= lineCount5) {
                            break;
                        }
                        if (this.typingLayout.getLineLeft(i45) != 0.0f) {
                            i46 = 0;
                            break;
                        }
                        double ceil4 = Math.ceil(this.typingLayout.getLineWidth(i45));
                        double d24 = i2122;
                        Double.isNaN(d24);
                        i46 = Math.min(i46, (int) (d24 - ceil4));
                        i45++;
                    }
                    if (i46 != Integer.MAX_VALUE) {
                        this.typingLeft += i46;
                    }
                }
                StaticLayout staticLayout5 = this.messageNameLayout;
                if (staticLayout5 != null && staticLayout5.getLineCount() > 0 && this.messageNameLayout.getLineLeft(0) == 0.0f) {
                    double ceil5 = Math.ceil(this.messageNameLayout.getLineWidth(0));
                    double d25 = i2122;
                    if (ceil5 < d25) {
                        double d26 = this.messageNameLeft;
                        Double.isNaN(d25);
                        Double.isNaN(d26);
                        this.messageNameLeft = (int) (d26 + (d25 - ceil5));
                    }
                }
                StaticLayout staticLayout6 = this.buttonLayout;
                if (staticLayout6 != null && (lineCount4 = staticLayout6.getLineCount()) > 0) {
                    for (int i47 = 0; i47 < lineCount4; i47++) {
                        i42 = (int) Math.min(i42, this.buttonLayout.getWidth() - this.buttonLayout.getLineRight(i47));
                    }
                    this.buttonLeft += i42;
                }
            } else {
                StaticLayout staticLayout7 = this.nameLayout;
                if (staticLayout7 != null && staticLayout7.getLineCount() > 0) {
                    float lineRight = this.nameLayout.getLineRight(0);
                    if (this.nameLayoutEllipsizeByGradient) {
                        lineRight = Math.min(this.nameWidth, lineRight);
                    }
                    if (lineRight == this.nameWidth) {
                        double ceil6 = Math.ceil(this.nameLayout.getLineWidth(0));
                        if (this.nameLayoutEllipsizeByGradient) {
                            ceil6 = Math.min(this.nameWidth, ceil6);
                        }
                        int i48 = this.nameWidth;
                        if (ceil6 < i48) {
                            double d27 = this.nameLeft;
                            double d28 = i48;
                            Double.isNaN(d28);
                            Double.isNaN(d27);
                            this.nameLeft = (int) (d27 - (d28 - ceil6));
                        }
                    }
                    this.nameMuteLeft = (int) (this.nameLeft + lineRight + AndroidUtilities.dp(6.0f));
                }
                StaticLayout staticLayout8 = this.messageLayout;
                if (staticLayout8 != null && (lineCount3 = staticLayout8.getLineCount()) > 0) {
                    float f3 = 2.14748365E9f;
                    for (int i49 = 0; i49 < lineCount3; i49++) {
                        f3 = Math.min(f3, this.messageLayout.getLineLeft(i49));
                    }
                    this.messageLeft = (int) (this.messageLeft - f3);
                }
                StaticLayout staticLayout9 = this.buttonLayout;
                if (staticLayout9 != null && (lineCount2 = staticLayout9.getLineCount()) > 0) {
                    float f4 = 2.14748365E9f;
                    for (int i50 = 0; i50 < lineCount2; i50++) {
                        f4 = Math.min(f4, this.buttonLayout.getLineLeft(i50));
                    }
                    this.buttonLeft = (int) (this.buttonLeft - f4);
                }
                StaticLayout staticLayout10 = this.typingLayout;
                if (staticLayout10 != null && (lineCount = staticLayout10.getLineCount()) > 0) {
                    float f5 = 2.14748365E9f;
                    for (int i51 = 0; i51 < lineCount; i51++) {
                        f5 = Math.min(f5, this.typingLayout.getLineLeft(i51));
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
                if (i4 < 0 && (i10 = i4 + 1) < this.typingLayout.getText().length()) {
                    primaryHorizontal = this.typingLayout.getPrimaryHorizontal(i4);
                    primaryHorizontal2 = this.typingLayout.getPrimaryHorizontal(i10);
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
        if (this.thumbsCount > 0 && charSequence9 != null) {
            max += AndroidUtilities.dp(5.0f);
        }
        this.messageLayout = StaticLayoutEx.createStaticLayout(charSequence8, this.currentMessagePaint, max, alignment, 1.0f, AndroidUtilities.dp(1.0f), false, TextUtils.TruncateAt.END, max, charSequence9 == null ? 1 : 2);
        int i21222 = max;
        this.spoilersPool.addAll(this.spoilers);
        this.spoilers.clear();
        SpoilerEffect.addSpoilers(this, this.messageLayout, -2, -2, this.spoilersPool, this.spoilers);
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

    /* JADX WARN: Removed duplicated region for block: B:110:0x0219  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0225  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0268  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x027b  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x02a6  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x02a9  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x02b6  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x02d0  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x02dd  */
    /* JADX WARN: Removed duplicated region for block: B:213:0x0380  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0392  */
    /* JADX WARN: Removed duplicated region for block: B:222:0x0399  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x039d  */
    /* JADX WARN: Type inference failed for: r6v1 */
    /* JADX WARN: Type inference failed for: r6v103 */
    /* JADX WARN: Type inference failed for: r6v2, types: [org.telegram.tgnet.TLRPC$Chat, org.telegram.tgnet.TLRPC$User, org.telegram.tgnet.TLRPC$EncryptedChat] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean update(int i, boolean z) {
        ?? r6;
        boolean z2;
        long j;
        MessageObject messageObject;
        TLRPC$Chat chat;
        CharSequence charSequence;
        boolean z3;
        boolean z4;
        MessageObject messageObject2;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        TLRPC$Chat chat2;
        MessageObject messageObject3;
        Long emojiStatusDocumentId;
        boolean isForumCell = isForumCell();
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
            z2 = false;
        } else {
            int i9 = this.unreadCount;
            boolean z5 = this.reactionMentionCount != 0;
            boolean z6 = this.markUnread;
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
                            boolean z7 = this.isTopic;
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
                            int i10 = messageObject5.messageOwner.edit_date;
                        }
                        this.lastMessageDate = tLRPC$Dialog.last_message_date;
                        int i11 = this.dialogsType;
                        if (i11 == 7 || i11 == 8) {
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
                    int i12 = (j2 > RightSlidingDialogContainer.fragmentDialogId ? 1 : (j2 == RightSlidingDialogContainer.fragmentDialogId ? 0 : -1));
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
            if (i != 0) {
                TLRPC$User tLRPC$User = this.user;
                if (tLRPC$User != null && !MessagesController.isSupportUser(tLRPC$User) && !this.user.bot && (i & MessagesController.UPDATE_MASK_STATUS) != 0) {
                    this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                    if (this.wasDrawnOnline != isOnline()) {
                        z2 = true;
                        if (this.user != null && (i & MessagesController.UPDATE_MASK_EMOJI_STATUS) != 0) {
                            TLRPC$User user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                            this.user = user;
                            emojiStatusDocumentId = UserObject.getEmojiStatusDocumentId(user);
                            if (emojiStatusDocumentId == null) {
                                this.nameLayoutEllipsizeByGradient = true;
                                this.emojiStatus.set(emojiStatusDocumentId.longValue(), z);
                            } else {
                                this.nameLayoutEllipsizeByGradient = true;
                                this.emojiStatus.set(PremiumGradient.getInstance().premiumStarDrawableMini, z);
                            }
                            z2 = true;
                        }
                        if ((!this.isDialogCell || this.isTopic) && (i & MessagesController.UPDATE_MASK_USER_PRINT) != 0) {
                            CharSequence printingString = MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, getTopicId(), true);
                            charSequence = this.lastPrintString;
                            if ((charSequence != null && printingString == null) || ((charSequence == null && printingString != null) || (charSequence != null && !charSequence.equals(printingString)))) {
                                z3 = true;
                                if (!z3 && (i & MessagesController.UPDATE_MASK_MESSAGE_TEXT) != 0 && (messageObject3 = this.message) != null && messageObject3.messageText != this.lastMessageString) {
                                    z3 = true;
                                }
                                if (!z3 && (i & MessagesController.UPDATE_MASK_CHAT) != 0 && this.chat != null) {
                                    chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                                    if ((!chat2.call_active && chat2.call_not_empty) != this.hasCall) {
                                        z3 = true;
                                    }
                                }
                                if (!z3 && (i & MessagesController.UPDATE_MASK_AVATAR) != 0 && this.chat == null) {
                                    z3 = true;
                                }
                                if (!z3 && (i & MessagesController.UPDATE_MASK_NAME) != 0 && this.chat == null) {
                                    z3 = true;
                                }
                                if (!z3 && (i & MessagesController.UPDATE_MASK_CHAT_AVATAR) != 0 && this.user == null) {
                                    z3 = true;
                                }
                                if (!z3 && (i & MessagesController.UPDATE_MASK_CHAT_NAME) != 0 && this.user == null) {
                                    z3 = true;
                                }
                                if (!z3) {
                                    MessageObject messageObject7 = this.message;
                                    if (messageObject7 != null && this.lastUnreadState != messageObject7.isUnread()) {
                                        this.lastUnreadState = this.message.isUnread();
                                        z3 = true;
                                    }
                                    if (this.isDialogCell) {
                                        TLRPC$Dialog tLRPC$Dialog2 = MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                                        TLRPC$Chat chat4 = tLRPC$Dialog2 != null ? MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-tLRPC$Dialog2.id)) : null;
                                        if (chat4 != null && chat4.forum) {
                                            int[] forumUnreadCount2 = MessagesController.getInstance(this.currentAccount).getTopicsController().getForumUnreadCount(chat4.id);
                                            i4 = forumUnreadCount2[0];
                                            i5 = forumUnreadCount2[1];
                                            i6 = forumUnreadCount2[2];
                                            this.hasUnmutedTopics = forumUnreadCount2[3] != 0;
                                        } else {
                                            if (tLRPC$Dialog2 instanceof TLRPC$TL_dialogFolder) {
                                                i4 = MessagesStorage.getInstance(this.currentAccount).getArchiveUnreadCount();
                                            } else if (tLRPC$Dialog2 != null) {
                                                i4 = tLRPC$Dialog2.unread_count;
                                                i5 = tLRPC$Dialog2.unread_mentions_count;
                                                i6 = tLRPC$Dialog2.unread_reactions_count;
                                            } else {
                                                i4 = 0;
                                            }
                                            i6 = 0;
                                            i5 = 0;
                                        }
                                        if (tLRPC$Dialog2 != null && (this.unreadCount != i4 || this.markUnread != tLRPC$Dialog2.unread_mark || this.mentionCount != i5 || this.reactionMentionCount != i6)) {
                                            this.unreadCount = i4;
                                            this.mentionCount = i5;
                                            this.markUnread = tLRPC$Dialog2.unread_mark;
                                            this.reactionMentionCount = i6;
                                            z3 = true;
                                        }
                                    }
                                }
                                if (!z3 && (i & MessagesController.UPDATE_MASK_SEND_STATE) != 0 && (messageObject2 = this.message) != null) {
                                    i2 = this.lastSendState;
                                    i3 = messageObject2.messageOwner.send_state;
                                    if (i2 != i3) {
                                        this.lastSendState = i3;
                                        z4 = true;
                                        if (!z4) {
                                            invalidate();
                                            return false;
                                        }
                                        r6 = 0;
                                    }
                                }
                                z4 = z3;
                                if (!z4) {
                                }
                            }
                        }
                        z3 = false;
                        if (!z3) {
                            z3 = true;
                        }
                        if (!z3) {
                            chat2 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.id));
                            if ((!chat2.call_active && chat2.call_not_empty) != this.hasCall) {
                            }
                        }
                        if (!z3) {
                            z3 = true;
                        }
                        if (!z3) {
                            z3 = true;
                        }
                        if (!z3) {
                            z3 = true;
                        }
                        if (!z3) {
                            z3 = true;
                        }
                        if (!z3) {
                        }
                        if (!z3) {
                            i2 = this.lastSendState;
                            i3 = messageObject2.messageOwner.send_state;
                            if (i2 != i3) {
                            }
                        }
                        z4 = z3;
                        if (!z4) {
                        }
                    }
                }
                z2 = false;
                if (this.user != null) {
                    TLRPC$User user2 = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.user.id));
                    this.user = user2;
                    emojiStatusDocumentId = UserObject.getEmojiStatusDocumentId(user2);
                    if (emojiStatusDocumentId == null) {
                    }
                    z2 = true;
                }
                if (!this.isDialogCell) {
                }
                CharSequence printingString2 = MessagesController.getInstance(this.currentAccount).getPrintingString(this.currentDialogId, getTopicId(), true);
                charSequence = this.lastPrintString;
                if (charSequence != null) {
                    z3 = true;
                    if (!z3) {
                    }
                    if (!z3) {
                    }
                    if (!z3) {
                    }
                    if (!z3) {
                    }
                    if (!z3) {
                    }
                    if (!z3) {
                    }
                    if (!z3) {
                    }
                    if (!z3) {
                    }
                    z4 = z3;
                    if (!z4) {
                    }
                }
                z3 = true;
                if (!z3) {
                }
                if (!z3) {
                }
                if (!z3) {
                }
                if (!z3) {
                }
                if (!z3) {
                }
                if (!z3) {
                }
                if (!z3) {
                }
                if (!z3) {
                }
                z4 = z3;
                if (!z4) {
                }
            } else {
                r6 = 0;
                z2 = false;
            }
            this.user = r6;
            this.chat = r6;
            this.encryptedChat = r6;
            if (this.currentDialogFolderId != 0) {
                this.dialogMuted = false;
                this.drawUnmute = false;
                MessageObject findFolderTopMessage = findFolderTopMessage();
                this.message = findFolderTopMessage;
                j = findFolderTopMessage != null ? findFolderTopMessage.getDialogId() : 0L;
            } else {
                this.drawUnmute = false;
                if (this.forumTopic != null) {
                    boolean isDialogMuted = MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId, 0);
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
                    this.dialogMuted = this.isDialogCell && MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId, getTopicId());
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
                    TLRPC$Chat chat5 = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-j));
                    this.chat = chat5;
                    if (!this.isDialogCell && chat5 != null && chat5.migrated_to != null && (chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(this.chat.migrated_to.channel_id))) != null) {
                        this.chat = chat;
                    }
                }
                if (this.useMeForMyMessages && this.user != null && this.message.isOutOwner()) {
                    this.user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(UserConfig.getInstance(this.currentAccount).clientUserId));
                }
            }
            if (this.currentDialogFolderId != 0) {
                Theme.dialogs_archiveAvatarDrawable.setCallback(this);
                this.avatarDrawable.setAvatarType(2);
                this.avatarImage.setImage(null, null, this.avatarDrawable, null, this.user, 0);
            } else if (this.useFromUserAsAvatar && (messageObject = this.message) != null) {
                this.avatarDrawable.setInfo(messageObject.getFromPeerObject());
                this.avatarImage.setForUserOrChat(this.message.getFromPeerObject(), this.avatarDrawable);
            } else {
                TLRPC$User tLRPC$User2 = this.user;
                if (tLRPC$User2 != null) {
                    this.avatarDrawable.setInfo(tLRPC$User2);
                    if (UserObject.isReplyUser(this.user)) {
                        this.avatarDrawable.setAvatarType(12);
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
                        this.avatarDrawable.setInfo(tLRPC$Chat);
                        this.avatarImage.setForUserOrChat(this.chat, this.avatarDrawable);
                    }
                }
            }
            if (z && ((i9 != this.unreadCount || z6 != this.markUnread) && (!this.isDialogCell || System.currentTimeMillis() - this.lastDialogChangedTime > 100))) {
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
                if ((i9 == 0 || this.markUnread) && (this.markUnread || !z6)) {
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
                        for (int i13 = 0; i13 < format.length(); i13++) {
                            if (format.charAt(i13) == format2.charAt(i13)) {
                                int i14 = i13 + 1;
                                spannableStringBuilder.setSpan(new EmptyStubSpan(), i13, i14, 0);
                                spannableStringBuilder2.setSpan(new EmptyStubSpan(), i13, i14, 0);
                            } else {
                                spannableStringBuilder3.setSpan(new EmptyStubSpan(), i13, i13 + 1, 0);
                            }
                        }
                        int max = Math.max(AndroidUtilities.dp(12.0f), (int) Math.ceil(Theme.dialogs_countTextPaint.measureText(format)));
                        this.countOldLayout = new StaticLayout(spannableStringBuilder, Theme.dialogs_countTextPaint, max, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        this.countAnimationStableLayout = new StaticLayout(spannableStringBuilder3, Theme.dialogs_countTextPaint, max, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                        this.countAnimationInLayout = new StaticLayout(spannableStringBuilder2, Theme.dialogs_countTextPaint, max, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                    } else {
                        this.countOldLayout = this.countLayout;
                    }
                }
                this.countWidthOld = this.countWidth;
                this.countLeftOld = this.countLeft;
                this.countAnimationIncrement = this.unreadCount > i9;
                this.countAnimator.start();
            }
            boolean z8 = this.reactionMentionCount != 0;
            if (z && z8 != z5) {
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
                if (z8) {
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
            imageReceiver.setRoundRadius(AndroidUtilities.dp((tLRPC$Chat2 == null || !tLRPC$Chat2.forum || this.currentDialogFolderId != 0 || this.useFromUserAsAvatar) ? 28.0f : 16.0f));
        }
        boolean z9 = (this.isTopic || (getMeasuredWidth() == 0 && getMeasuredHeight() == 0)) ? false : true;
        if (!z2) {
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
        boolean z10 = isForumCell() != isForumCell;
        if (z9) {
            if (this.attachedToWindow) {
                buildLayout();
            } else {
                this.updateLayout = true;
            }
        }
        return z10;
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
    /* JADX WARN: Code restructure failed: missing block: B:329:0x0b4e, code lost:
        if (r4.lastKnownTypingType >= 0) goto L182;
     */
    /* JADX WARN: Removed duplicated region for block: B:258:0x0908  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x09d0  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x0aeb  */
    /* JADX WARN: Removed duplicated region for block: B:316:0x0af6  */
    /* JADX WARN: Removed duplicated region for block: B:324:0x0b40  */
    /* JADX WARN: Removed duplicated region for block: B:353:0x0bcf  */
    /* JADX WARN: Removed duplicated region for block: B:390:0x0d8c  */
    /* JADX WARN: Removed duplicated region for block: B:450:0x0e5f  */
    /* JADX WARN: Removed duplicated region for block: B:453:0x0e6a  */
    /* JADX WARN: Removed duplicated region for block: B:460:0x0e78 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:493:0x0ed3  */
    /* JADX WARN: Removed duplicated region for block: B:496:0x0eea  */
    /* JADX WARN: Removed duplicated region for block: B:501:0x0f3e  */
    /* JADX WARN: Removed duplicated region for block: B:507:0x0f54  */
    /* JADX WARN: Removed duplicated region for block: B:521:0x0f99  */
    /* JADX WARN: Removed duplicated region for block: B:567:0x1058  */
    /* JADX WARN: Removed duplicated region for block: B:572:0x107e  */
    /* JADX WARN: Removed duplicated region for block: B:574:0x10cd  */
    /* JADX WARN: Removed duplicated region for block: B:630:0x1279  */
    /* JADX WARN: Removed duplicated region for block: B:667:0x140e  */
    /* JADX WARN: Removed duplicated region for block: B:671:0x1420  */
    /* JADX WARN: Removed duplicated region for block: B:674:0x1440  */
    /* JADX WARN: Removed duplicated region for block: B:686:0x145c  */
    /* JADX WARN: Removed duplicated region for block: B:687:0x145e  */
    /* JADX WARN: Removed duplicated region for block: B:691:0x146c  */
    /* JADX WARN: Removed duplicated region for block: B:694:0x1477  */
    /* JADX WARN: Removed duplicated region for block: B:699:0x1485  */
    /* JADX WARN: Removed duplicated region for block: B:703:0x148d  */
    /* JADX WARN: Removed duplicated region for block: B:705:0x1491  */
    /* JADX WARN: Removed duplicated region for block: B:719:0x14fc  */
    /* JADX WARN: Removed duplicated region for block: B:722:0x1505  */
    /* JADX WARN: Removed duplicated region for block: B:725:0x150c  */
    /* JADX WARN: Removed duplicated region for block: B:740:0x154e  */
    /* JADX WARN: Removed duplicated region for block: B:769:0x15ce  */
    /* JADX WARN: Removed duplicated region for block: B:775:0x1620  */
    /* JADX WARN: Removed duplicated region for block: B:778:0x1628  */
    /* JADX WARN: Removed duplicated region for block: B:783:0x1638  */
    /* JADX WARN: Removed duplicated region for block: B:792:0x164f  */
    /* JADX WARN: Removed duplicated region for block: B:800:0x1678  */
    /* JADX WARN: Removed duplicated region for block: B:811:0x16a5  */
    /* JADX WARN: Removed duplicated region for block: B:817:0x16b8  */
    /* JADX WARN: Removed duplicated region for block: B:827:0x16db  */
    /* JADX WARN: Removed duplicated region for block: B:837:0x16f9  */
    /* JADX WARN: Removed duplicated region for block: B:856:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v100 */
    /* JADX WARN: Type inference failed for: r10v2, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r10v99 */
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
        RLottieDrawable rLottieDrawable;
        Paint paint;
        Canvas canvas2;
        ?? r10;
        float f2;
        float f3;
        int i3;
        boolean z;
        boolean z2;
        boolean z3;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic;
        boolean z4;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic2;
        PullForegroundDrawable pullForegroundDrawable;
        int i4;
        Paint paint2;
        Paint paint3;
        int i5;
        float f4;
        int i6;
        float f5;
        int i7;
        int i8;
        int i9;
        int i10;
        boolean z5;
        int dp;
        int dp2;
        int i11;
        boolean z6;
        int i12;
        char c;
        float dp3;
        float f6;
        int i13;
        int i14;
        float f7;
        DialogUpdateHelper dialogUpdateHelper;
        float f8;
        StaticLayout staticLayout;
        int color3;
        float f9;
        int i15;
        CustomDialog customDialog;
        int i16;
        Paint paint4;
        PullForegroundDrawable pullForegroundDrawable2;
        TLRPC$TL_forumTopic tLRPC$TL_forumTopic3;
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
        int i17 = 2;
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
            int i18 = color2;
            String str3 = string;
            if (this.swipeCanceled && (rLottieDrawable = this.lastDrawTranslationDrawable) != null) {
                this.translationDrawable = rLottieDrawable;
                i = this.lastDrawSwipeMessageStringId;
            } else {
                this.lastDrawTranslationDrawable = this.translationDrawable;
                this.lastDrawSwipeMessageStringId = i;
            }
            int i19 = i;
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
                i2 = i19;
                str = str3;
                canvas.drawRect(measuredWidth - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight(), Theme.dialogs_pinnedPaint);
                if (this.currentRevealProgress == 0.0f) {
                    if (Theme.dialogs_archiveDrawableRecolored) {
                        Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(Theme.key_chats_archiveBackground));
                        Theme.dialogs_archiveDrawableRecolored = false;
                    }
                    if (Theme.dialogs_hidePsaDrawableRecolored) {
                        Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                        RLottieDrawable rLottieDrawable2 = Theme.dialogs_hidePsaDrawable;
                        int i20 = Theme.key_chats_archiveBackground;
                        rLottieDrawable2.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(i20));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(i20));
                        Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(i20));
                        Theme.dialogs_hidePsaDrawable.commitApplyLayerColors();
                        Theme.dialogs_hidePsaDrawableRecolored = false;
                    }
                }
            } else {
                f = measuredWidth;
                i2 = i19;
                str = str3;
            }
            int measuredWidth2 = (getMeasuredWidth() - AndroidUtilities.dp(43.0f)) - (this.translationDrawable.getIntrinsicWidth() / 2);
            int measuredHeight = (getMeasuredHeight() - AndroidUtilities.dp(54.0f)) / 2;
            int intrinsicWidth = (this.translationDrawable.getIntrinsicWidth() / 2) + measuredWidth2;
            int intrinsicHeight = (this.translationDrawable.getIntrinsicHeight() / 2) + measuredHeight;
            if (this.currentRevealProgress > 0.0f) {
                canvas.save();
                canvas.clipRect(f - AndroidUtilities.dp(8.0f), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                Theme.dialogs_pinnedPaint.setColor(i18);
                canvas.drawCircle(intrinsicWidth, intrinsicHeight, ((float) Math.sqrt((intrinsicWidth * intrinsicWidth) + ((intrinsicHeight - getMeasuredHeight()) * (intrinsicHeight - getMeasuredHeight())))) * AndroidUtilities.accelerateInterpolator.getInterpolation(this.currentRevealProgress), Theme.dialogs_pinnedPaint);
                canvas.restore();
                if (!Theme.dialogs_archiveDrawableRecolored) {
                    Theme.dialogs_archiveDrawable.setLayerColor("Arrow.**", Theme.getNonAnimatedColor(Theme.key_chats_archivePinBackground));
                    Theme.dialogs_archiveDrawableRecolored = true;
                }
                if (!Theme.dialogs_hidePsaDrawableRecolored) {
                    Theme.dialogs_hidePsaDrawable.beginApplyLayerColors();
                    RLottieDrawable rLottieDrawable3 = Theme.dialogs_hidePsaDrawable;
                    int i21 = Theme.key_chats_archivePinBackground;
                    rLottieDrawable3.setLayerColor("Line 1.**", Theme.getNonAnimatedColor(i21));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 2.**", Theme.getNonAnimatedColor(i21));
                    Theme.dialogs_hidePsaDrawable.setLayerColor("Line 3.**", Theme.getNonAnimatedColor(i21));
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
                    i16 = canvas.saveLayerAlpha(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + 1) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight(), (int) ((1.0f - this.rightFragmentOpenedProgress) * 255.0f), 31);
                } else {
                    int save = canvas.save();
                    canvas.clipRect(AndroidUtilities.dp(RightSlidingDialogContainer.getRightPaddingSize() + 1) - (AndroidUtilities.dp(8.0f) * (1.0f - clamp)), 0.0f, getMeasuredWidth(), getMeasuredHeight());
                    i16 = save;
                }
                canvas.translate((-(getMeasuredWidth() - AndroidUtilities.dp(74.0f))) * 0.7f * this.rightFragmentOpenedProgress, 0.0f);
                i4 = i16;
            } else {
                i4 = -1;
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
                    Theme.dialogs_pinnedPaint.setColor(AndroidUtilities.getOffsetColor(0, Theme.getColor(Theme.key_chats_pinnedOverlay, this.resourcesProvider), this.archiveBackgroundProgress, 1.0f));
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
                float f12 = this.cornerProgress;
                if (f12 < 1.0f) {
                    float f13 = f12 + 0.10666667f;
                    this.cornerProgress = f13;
                    if (f13 > 1.0f) {
                        this.cornerProgress = 1.0f;
                    }
                    z = true;
                }
                z = false;
            } else {
                float f14 = this.cornerProgress;
                if (f14 > 0.0f) {
                    float f15 = f14 - 0.10666667f;
                    this.cornerProgress = f15;
                    if (f15 < 0.0f) {
                        this.cornerProgress = 0.0f;
                    }
                    z = true;
                }
                z = false;
            }
            if (this.drawNameLock) {
                BaseCell.setDrawableBounds(Theme.dialogs_lockDrawable, this.nameLockLeft, this.nameLockTop);
                Theme.dialogs_lockDrawable.draw(canvas);
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
                    int i22 = this.nameLeft;
                    canvas.clipRect(i22, 0, this.nameWidth + i22, getMeasuredHeight());
                }
                if (this.currentDialogFolderId != 0) {
                    TextPaint[] textPaintArr = Theme.dialogs_namePaint;
                    int i23 = this.paintIndex;
                    TextPaint textPaint = textPaintArr[i23];
                    TextPaint textPaint2 = textPaintArr[i23];
                    int color4 = Theme.getColor(Theme.key_chats_nameArchived, this.resourcesProvider);
                    textPaint2.linkColor = color4;
                    textPaint.setColor(color4);
                } else if (this.encryptedChat != null || ((customDialog = this.customDialog) != null && customDialog.type == 2)) {
                    TextPaint[] textPaintArr2 = Theme.dialogs_namePaint;
                    int i24 = this.paintIndex;
                    TextPaint textPaint3 = textPaintArr2[i24];
                    TextPaint textPaint4 = textPaintArr2[i24];
                    int color5 = Theme.getColor(Theme.key_chats_secretName, this.resourcesProvider);
                    textPaint4.linkColor = color5;
                    textPaint3.setColor(color5);
                } else {
                    TextPaint[] textPaintArr3 = Theme.dialogs_namePaint;
                    int i25 = this.paintIndex;
                    TextPaint textPaint5 = textPaintArr3[i25];
                    TextPaint textPaint6 = textPaintArr3[i25];
                    int color6 = Theme.getColor(Theme.key_chats_name, this.resourcesProvider);
                    textPaint6.linkColor = color6;
                    textPaint5.setColor(color6);
                }
                canvas.save();
                canvas.translate(this.nameLeft + this.nameLayoutTranslateX, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 10.0f : 13.0f));
                SpoilerEffect.layoutDrawMaybe(this.nameLayout, canvas);
                f4 = 1.0f;
                i5 = i4;
                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.nameLayout, this.animatedEmojiStackName, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                canvas.restore();
                if (!this.nameLayoutEllipsizeByGradient || this.nameLayoutFits) {
                    i17 = 2;
                } else {
                    canvas.save();
                    if (this.nameLayoutEllipsizeLeft) {
                        canvas.translate(this.nameLeft, 0.0f);
                        i17 = 2;
                        canvas.drawRect(0.0f, 0.0f, AndroidUtilities.dp(24.0f), getMeasuredHeight(), this.fadePaint);
                    } else {
                        i17 = 2;
                        canvas.translate((this.nameLeft + this.nameWidth) - AndroidUtilities.dp(24.0f), 0.0f);
                        canvas.drawRect(0.0f, 0.0f, AndroidUtilities.dp(24.0f), getMeasuredHeight(), this.fadePaintBack);
                    }
                    canvas.restore();
                    canvas.restore();
                    if (this.timeLayout != null && this.currentDialogFolderId == 0) {
                        canvas.save();
                        canvas.translate(this.timeLeft, this.timeTop);
                        SpoilerEffect.layoutDrawMaybe(this.timeLayout, canvas);
                        canvas.restore();
                    }
                    if (drawLock2()) {
                        Theme.dialogs_lock2Drawable.setBounds(this.lock2Left, this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / i17), this.lock2Left + Theme.dialogs_lock2Drawable.getIntrinsicWidth(), this.timeTop + ((this.timeLayout.getHeight() - Theme.dialogs_lock2Drawable.getIntrinsicHeight()) / i17) + Theme.dialogs_lock2Drawable.getIntrinsicHeight());
                        Theme.dialogs_lock2Drawable.draw(canvas);
                    }
                    if (this.messageNameLayout != null || isForumCell()) {
                        i6 = 1;
                        f5 = 0.0f;
                        i7 = 0;
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
                            i7 = 0;
                            i6 = 1;
                            f5 = 0.0f;
                        } catch (Exception e) {
                            e = e;
                            i6 = 1;
                            f5 = 0.0f;
                            i7 = 0;
                        }
                        try {
                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageNameLayout, this.animatedEmojiStack2, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                        } catch (Exception e2) {
                            e = e2;
                            FileLog.e(e);
                            canvas.restore();
                            if (this.messageLayout != null) {
                            }
                            i8 = 4;
                            if (this.buttonLayout != null) {
                            }
                            if (this.currentDialogFolderId == 0) {
                            }
                            if (this.drawUnmute) {
                            }
                            if (this.dialogsType == i9) {
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
                            i11 = -1;
                            z6 = false;
                            i12 = i5;
                            r10 = z6;
                            if (i12 != i11) {
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
                                z = true;
                            }
                            if (this.rightFragmentOpenedProgress > f2) {
                                if (!this.isTopic) {
                                }
                                boolean z7 = z4;
                                RectF rectF = this.storyParams.originalAvatarRect;
                                int width = (int) (((rectF.left + rectF.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
                                RectF rectF2 = this.storyParams.originalAvatarRect;
                                drawCounter(canvas, z7, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width, (int) (((rectF2.left + rectF2.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
                            }
                            if (this.collapseOffset != f2) {
                            }
                            if (this.translationX != f2) {
                            }
                            if (this.drawArchive) {
                                canvas.save();
                                canvas2.translate(f2, (-this.translateY) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress));
                                canvas2.clipRect((int) r10, (int) r10, getMeasuredWidth(), getMeasuredHeight());
                                this.archivedChatsDrawable.draw(canvas2);
                                canvas.restore();
                            }
                            if (this.useSeparator) {
                            }
                            if (this.clipProgress != f2) {
                            }
                            z2 = this.drawReorder;
                            if (!z2) {
                            }
                            if (z2) {
                            }
                            if (!this.archiveHidden) {
                            }
                        }
                        canvas.restore();
                    }
                    if (this.messageLayout != null) {
                        if (this.currentDialogFolderId != 0) {
                            if (this.chat != null) {
                                TextPaint[] textPaintArr4 = Theme.dialogs_messagePaint;
                                int i26 = this.paintIndex;
                                TextPaint textPaint10 = textPaintArr4[i26];
                                TextPaint textPaint11 = textPaintArr4[i26];
                                int color10 = Theme.getColor(Theme.key_chats_nameMessageArchived, this.resourcesProvider);
                                textPaint11.linkColor = color10;
                                textPaint10.setColor(color10);
                            } else {
                                TextPaint[] textPaintArr5 = Theme.dialogs_messagePaint;
                                int i27 = this.paintIndex;
                                TextPaint textPaint12 = textPaintArr5[i27];
                                TextPaint textPaint13 = textPaintArr5[i27];
                                int color11 = Theme.getColor(Theme.key_chats_messageArchived, this.resourcesProvider);
                                textPaint13.linkColor = color11;
                                textPaint12.setColor(color11);
                            }
                        } else {
                            TextPaint[] textPaintArr6 = Theme.dialogs_messagePaint;
                            int i28 = this.paintIndex;
                            TextPaint textPaint14 = textPaintArr6[i28];
                            TextPaint textPaint15 = textPaintArr6[i28];
                            int color12 = Theme.getColor(Theme.key_chats_message, this.resourcesProvider);
                            textPaint15.linkColor = color12;
                            textPaint14.setColor(color12);
                        }
                        float dp5 = AndroidUtilities.dp(14.0f);
                        DialogUpdateHelper dialogUpdateHelper2 = this.updateHelper;
                        if (dialogUpdateHelper2.typingOutToTop) {
                            f7 = this.messageTop - (dialogUpdateHelper2.typingProgres * dp5);
                        } else {
                            f7 = this.messageTop + (dialogUpdateHelper2.typingProgres * dp5);
                        }
                        if (dialogUpdateHelper2.typingProgres != f4) {
                            canvas.save();
                            canvas.translate(this.messageLeft, f7);
                            int alpha = this.messageLayout.getPaint().getAlpha();
                            this.messageLayout.getPaint().setAlpha((int) (alpha * (f4 - this.updateHelper.typingProgres)));
                            if (!this.spoilers.isEmpty()) {
                                try {
                                    canvas.save();
                                    SpoilerEffect.clipOutCanvas(canvas, this.spoilers);
                                    SpoilerEffect.layoutDrawMaybe(this.messageLayout, canvas);
                                    i15 = alpha;
                                } catch (Exception e3) {
                                    e = e3;
                                    i15 = alpha;
                                }
                                try {
                                    AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, this.spoilers, 0.0f, 0.0f, 0.0f, 1.0f);
                                    canvas.restore();
                                    for (int i29 = 0; i29 < this.spoilers.size(); i29++) {
                                        SpoilerEffect spoilerEffect = this.spoilers.get(i29);
                                        spoilerEffect.setColor(this.messageLayout.getPaint().getColor());
                                        spoilerEffect.draw(canvas);
                                    }
                                } catch (Exception e4) {
                                    e = e4;
                                    FileLog.e(e);
                                    this.messageLayout.getPaint().setAlpha(i15);
                                    canvas.restore();
                                    canvas.save();
                                    dialogUpdateHelper = this.updateHelper;
                                    if (!dialogUpdateHelper.typingOutToTop) {
                                    }
                                    canvas.translate(this.typingLeft, f8);
                                    staticLayout = this.typingLayout;
                                    if (staticLayout != null) {
                                        int alpha2 = staticLayout.getPaint().getAlpha();
                                        this.typingLayout.getPaint().setAlpha((int) (alpha2 * this.updateHelper.typingProgres));
                                        this.typingLayout.draw(canvas);
                                        this.typingLayout.getPaint().setAlpha(alpha2);
                                    }
                                    canvas.restore();
                                    if (this.typingLayout != null) {
                                    }
                                    i8 = 4;
                                    if (this.buttonLayout != null) {
                                    }
                                    if (this.currentDialogFolderId == 0) {
                                    }
                                    if (this.drawUnmute) {
                                    }
                                    if (this.dialogsType == i9) {
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
                                    i11 = -1;
                                    z6 = false;
                                    i12 = i5;
                                    r10 = z6;
                                    if (i12 != i11) {
                                    }
                                    if (this.animatingArchiveAvatar) {
                                    }
                                    if (this.drawAvatar) {
                                    }
                                    if (this.animatingArchiveAvatar) {
                                    }
                                    if (this.avatarImage.getVisible()) {
                                    }
                                    if (this.rightFragmentOpenedProgress > f2) {
                                    }
                                    if (this.collapseOffset != f2) {
                                    }
                                    if (this.translationX != f2) {
                                    }
                                    if (this.drawArchive) {
                                    }
                                    if (this.useSeparator) {
                                    }
                                    if (this.clipProgress != f2) {
                                    }
                                    z2 = this.drawReorder;
                                    if (!z2) {
                                    }
                                    if (z2) {
                                    }
                                    if (!this.archiveHidden) {
                                    }
                                }
                            } else {
                                i15 = alpha;
                                SpoilerEffect.layoutDrawMaybe(this.messageLayout, canvas);
                                AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.messageLayout, this.animatedEmojiStack, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                            }
                            this.messageLayout.getPaint().setAlpha(i15);
                            canvas.restore();
                        }
                        canvas.save();
                        dialogUpdateHelper = this.updateHelper;
                        if (!dialogUpdateHelper.typingOutToTop) {
                            f8 = this.messageTop + ((f4 - dialogUpdateHelper.typingProgres) * dp5);
                        } else {
                            f8 = this.messageTop - ((f4 - dialogUpdateHelper.typingProgres) * dp5);
                        }
                        canvas.translate(this.typingLeft, f8);
                        staticLayout = this.typingLayout;
                        if (staticLayout != null && this.updateHelper.typingProgres > f5) {
                            int alpha22 = staticLayout.getPaint().getAlpha();
                            this.typingLayout.getPaint().setAlpha((int) (alpha22 * this.updateHelper.typingProgres));
                            this.typingLayout.draw(canvas);
                            this.typingLayout.getPaint().setAlpha(alpha22);
                        }
                        canvas.restore();
                        if (this.typingLayout != null) {
                            int i30 = this.printingStringType;
                            if (i30 < 0) {
                                DialogUpdateHelper dialogUpdateHelper3 = this.updateHelper;
                                if (dialogUpdateHelper3.typingProgres > f5) {
                                }
                            }
                            if (i30 < 0) {
                                i30 = this.updateHelper.lastKnownTypingType;
                            }
                            StatusDrawable chatStatusDrawable = Theme.getChatStatusDrawable(i30);
                            if (chatStatusDrawable != null) {
                                canvas.save();
                                chatStatusDrawable.setColor(ColorUtils.setAlphaComponent(Theme.getColor(Theme.key_chats_actionMessage), (int) (Color.alpha(color3) * this.updateHelper.typingProgres)));
                                DialogUpdateHelper dialogUpdateHelper4 = this.updateHelper;
                                if (dialogUpdateHelper4.typingOutToTop) {
                                    f9 = this.messageTop + (dp5 * (f4 - dialogUpdateHelper4.typingProgres));
                                } else {
                                    f9 = this.messageTop - (dp5 * (f4 - dialogUpdateHelper4.typingProgres));
                                }
                                i8 = 4;
                                if (i30 == i6 || i30 == 4) {
                                    canvas.translate(this.statusDrawableLeft, f9 + (i30 == i6 ? AndroidUtilities.dp(f4) : 0));
                                } else {
                                    canvas.translate(this.statusDrawableLeft, f9 + ((AndroidUtilities.dp(18.0f) - chatStatusDrawable.getIntrinsicHeight()) / 2.0f));
                                }
                                chatStatusDrawable.draw(canvas);
                                invalidate();
                                canvas.restore();
                                if (this.buttonLayout != null) {
                                    canvas.save();
                                    if (this.buttonBackgroundPaint == null) {
                                        this.buttonBackgroundPaint = new Paint(i6);
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
                                    if (this.lastTopicMessageUnread && this.topMessageTopicEndIndex != this.topMessageTopicStartIndex) {
                                        this.canvasButton.setColor(ColorUtils.setAlphaComponent(this.currentMessagePaint.getColor(), Theme.isCurrentThemeDark() ? 36 : 26));
                                        if (!this.buttonCreated) {
                                            this.canvasButton.rewind();
                                            int i31 = this.topMessageTopicEndIndex;
                                            if (i31 != this.topMessageTopicStartIndex && i31 > 0) {
                                                RectF rectF3 = AndroidUtilities.rectTmp;
                                                StaticLayout staticLayout3 = this.messageLayout;
                                                rectF3.set(this.messageLeft + AndroidUtilities.dp(2.0f) + this.messageLayout.getPrimaryHorizontal(i7), this.messageTop, (this.messageLeft + staticLayout3.getPrimaryHorizontal(Math.min(staticLayout3.getText().length(), this.topMessageTopicEndIndex))) - AndroidUtilities.dp(3.0f), this.buttonTop - AndroidUtilities.dp(4.0f));
                                                rectF3.inset(-AndroidUtilities.dp(8.0f), -AndroidUtilities.dp(4.0f));
                                                if (rectF3.right > rectF3.left) {
                                                    this.canvasButton.addRect(rectF3);
                                                }
                                            }
                                            float lineLeft = this.buttonLayout.getLineLeft(i7);
                                            RectF rectF4 = AndroidUtilities.rectTmp;
                                            rectF4.set(this.buttonLeft + lineLeft + AndroidUtilities.dp(2.0f), this.buttonTop + AndroidUtilities.dp(2.0f), this.buttonLeft + lineLeft + this.buttonLayout.getLineWidth(i7) + AndroidUtilities.dp(12.0f), this.buttonTop + this.buttonLayout.getHeight());
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
                                            AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.buttonLayout, this.animatedEmojiStack3, -0.075f, this.spoilers2, 0.0f, 0.0f, 0.0f, 1.0f);
                                            canvas.restore();
                                            for (int i32 = 0; i32 < this.spoilers2.size(); i32++) {
                                                SpoilerEffect spoilerEffect2 = this.spoilers2.get(i32);
                                                spoilerEffect2.setColor(this.buttonLayout.getPaint().getColor());
                                                spoilerEffect2.draw(canvas);
                                            }
                                        } catch (Exception e5) {
                                            FileLog.e(e5);
                                        }
                                    } else {
                                        SpoilerEffect.layoutDrawMaybe(this.buttonLayout, canvas);
                                        AnimatedEmojiSpan.drawAnimatedEmojis(canvas, this.buttonLayout, this.animatedEmojiStack3, -0.075f, null, 0.0f, 0.0f, 0.0f, 1.0f);
                                    }
                                    canvas.restore();
                                }
                                if (this.currentDialogFolderId == 0) {
                                    int i33 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0) + (this.drawCheck2 ? 4 : 0);
                                    int i34 = this.lastStatusDrawableParams;
                                    if (i34 >= 0 && i34 != i33 && !this.statusDrawableAnimationInProgress) {
                                        createStatusDrawableAnimator(i34, i33);
                                    }
                                    boolean z8 = this.statusDrawableAnimationInProgress;
                                    if (z8) {
                                        i33 = this.animateToStatusDrawableParams;
                                    }
                                    boolean z9 = (i33 & 1) != 0;
                                    boolean z10 = (i33 & 2) != 0;
                                    boolean z11 = (i33 & i8) != 0;
                                    if (z8) {
                                        int i35 = this.animateFromStatusDrawableParams;
                                        boolean z12 = (i35 & 1) != 0;
                                        boolean z13 = (i35 & 2) != 0;
                                        boolean z14 = (i35 & i8) != 0;
                                        if (!z9 && !z12 && z14 && !z13 && z10 && z11) {
                                            i3 = 1;
                                            i14 = 4;
                                            drawCheckStatus(canvas, z9, z10, z11, true, this.statusDrawableProgress);
                                            f2 = 0.0f;
                                            f3 = 1.0f;
                                            i9 = 2;
                                            i10 = -1;
                                        } else {
                                            i3 = 1;
                                            i14 = 4;
                                            boolean z15 = z12;
                                            f2 = 0.0f;
                                            boolean z16 = z13;
                                            f3 = 1.0f;
                                            boolean z17 = z14;
                                            i9 = 2;
                                            i10 = -1;
                                            drawCheckStatus(canvas, z15, z16, z17, false, 1.0f - this.statusDrawableProgress);
                                            drawCheckStatus(canvas, z9, z10, z11, false, this.statusDrawableProgress);
                                        }
                                    } else {
                                        f2 = 0.0f;
                                        f3 = 1.0f;
                                        i9 = 2;
                                        i3 = 1;
                                        i14 = 4;
                                        i10 = -1;
                                        drawCheckStatus(canvas, z9, z10, z11, false, 1.0f);
                                    }
                                    int i36 = (this.drawClock ? 1 : 0) + (this.drawCheck1 ? 2 : 0);
                                    if (!this.drawCheck2) {
                                        i14 = 0;
                                    }
                                    this.lastStatusDrawableParams = i36 + i14;
                                } else {
                                    f2 = 0.0f;
                                    f3 = 1.0f;
                                    i9 = 2;
                                    i3 = 1;
                                    i10 = -1;
                                }
                                boolean z18 = !this.drawUnmute || this.dialogMuted;
                                if (this.dialogsType == i9 && ((z18 || this.dialogMutedProgress > f2) && !this.drawVerified && this.drawScam == 0 && !this.drawPremium)) {
                                    if (z18) {
                                        float f16 = this.dialogMutedProgress;
                                        if (f16 != f3) {
                                            float f17 = f16 + 0.10666667f;
                                            this.dialogMutedProgress = f17;
                                            if (f17 > f3) {
                                                this.dialogMutedProgress = f3;
                                            } else {
                                                invalidate();
                                            }
                                            float dp6 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                                            float dp7 = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                                            BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp6, dp7);
                                            BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp6, dp7);
                                            if (this.dialogMutedProgress == f3) {
                                                canvas.save();
                                                float f18 = this.dialogMutedProgress;
                                                canvas.scale(f18, f18, Theme.dialogs_muteDrawable.getBounds().centerX(), Theme.dialogs_muteDrawable.getBounds().centerY());
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
                                    if (!z18) {
                                        float f19 = this.dialogMutedProgress;
                                        if (f19 != f2) {
                                            float f20 = f19 - 0.10666667f;
                                            this.dialogMutedProgress = f20;
                                            if (f20 < f2) {
                                                this.dialogMutedProgress = f2;
                                            } else {
                                                invalidate();
                                            }
                                        }
                                    }
                                    float dp62 = this.nameMuteLeft - AndroidUtilities.dp((!this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 0.0f : 1.0f);
                                    float dp72 = AndroidUtilities.dp(SharedConfig.useThreeLinesLayout ? 13.5f : 17.5f);
                                    BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, dp62, dp72);
                                    BaseCell.setDrawableBounds(Theme.dialogs_unmuteDrawable, dp62, dp72);
                                    if (this.dialogMutedProgress == f3) {
                                    }
                                } else if (this.drawVerified) {
                                    BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 13.5f : 16.5f));
                                    Drawable drawable2 = Theme.dialogs_verifiedCheckDrawable;
                                    int dp8 = this.nameMuteLeft - AndroidUtilities.dp(f3);
                                    if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                        r2 = 16.5f;
                                    }
                                    BaseCell.setDrawableBounds(drawable2, dp8, AndroidUtilities.dp(r2));
                                    Theme.dialogs_verifiedDrawable.draw(canvas);
                                    Theme.dialogs_verifiedCheckDrawable.draw(canvas);
                                } else if (this.drawPremium) {
                                    AnimatedEmojiDrawable.SwapAnimatedEmojiDrawable swapAnimatedEmojiDrawable = this.emojiStatus;
                                    if (swapAnimatedEmojiDrawable != null) {
                                        swapAnimatedEmojiDrawable.setBounds(this.nameMuteLeft - AndroidUtilities.dp(2.0f), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f) - AndroidUtilities.dp(4.0f), this.nameMuteLeft + AndroidUtilities.dp(20.0f), (AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f) - AndroidUtilities.dp(4.0f)) + AndroidUtilities.dp(22.0f));
                                        this.emojiStatus.setColor(Integer.valueOf(Theme.getColor(Theme.key_chats_verifiedBackground, this.resourcesProvider)));
                                        this.emojiStatus.draw(canvas);
                                    } else {
                                        Drawable drawable3 = PremiumGradient.getInstance().premiumStarDrawableMini;
                                        BaseCell.setDrawableBounds(drawable3, this.nameMuteLeft - AndroidUtilities.dp(f3), AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.5f : 15.5f));
                                        drawable3.draw(canvas);
                                    }
                                } else {
                                    int i37 = this.drawScam;
                                    if (i37 != 0) {
                                        BaseCell.setDrawableBounds((Drawable) (i37 == i3 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable), this.nameMuteLeft, AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 12.0f : 15.0f));
                                        (this.drawScam == i3 ? Theme.dialogs_scamDrawable : Theme.dialogs_fakeDrawable).draw(canvas);
                                    }
                                }
                                if (!this.drawReorder || this.reorderIconProgress != f2) {
                                    Theme.dialogs_reorderDrawable.setAlpha((int) (this.reorderIconProgress * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_reorderDrawable, this.pinLeft, this.pinTop);
                                    Theme.dialogs_reorderDrawable.draw(canvas);
                                }
                                if (this.drawError) {
                                    Theme.dialogs_errorDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                    this.rect.set(this.errorLeft, this.errorTop, i13 + AndroidUtilities.dp(23.0f), this.errorTop + AndroidUtilities.dp(23.0f));
                                    RectF rectF6 = this.rect;
                                    float f21 = AndroidUtilities.density;
                                    canvas.drawRoundRect(rectF6, f21 * 11.5f, f21 * 11.5f, Theme.dialogs_errorPaint);
                                    BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
                                    Theme.dialogs_errorDrawable.draw(canvas);
                                } else if (((this.drawCount || this.drawMention) && this.drawCount2) || this.countChangeProgress != f3 || this.drawReactionMention || this.reactionsMentionsChangeProgress != f3) {
                                    if (this.isTopic) {
                                        z5 = this.topicMuted;
                                    } else {
                                        TLRPC$Chat tLRPC$Chat = this.chat;
                                        z5 = (tLRPC$Chat != null && tLRPC$Chat.forum && this.forumTopic == null) ? !this.hasUnmutedTopics : this.dialogMuted;
                                    }
                                    canvas2 = canvas;
                                    drawCounter(canvas, z5, this.countTop, this.countLeft, this.countLeftOld, 1.0f, false);
                                    if (this.drawMention) {
                                        Theme.dialogs_countPaint.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                        this.rect.set(this.mentionLeft - AndroidUtilities.dp(5.5f), this.countTop, dp2 + this.mentionWidth + AndroidUtilities.dp(11.0f), this.countTop + AndroidUtilities.dp(23.0f));
                                        Paint paint7 = (!z5 || this.folderId == 0) ? Theme.dialogs_countPaint : Theme.dialogs_countGrayPaint;
                                        RectF rectF7 = this.rect;
                                        float f22 = AndroidUtilities.density;
                                        canvas2.drawRoundRect(rectF7, f22 * 11.5f, f22 * 11.5f, paint7);
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
                                        float f23 = this.reactionsMentionsChangeProgress;
                                        if (f23 != f3) {
                                            if (!this.drawReactionMention) {
                                                f23 = f3 - f23;
                                            }
                                            canvas2.scale(f23, f23, this.rect.centerX(), this.rect.centerY());
                                        }
                                        RectF rectF8 = this.rect;
                                        float f24 = AndroidUtilities.density;
                                        canvas2.drawRoundRect(rectF8, f24 * 11.5f, f24 * 11.5f, paint8);
                                        Theme.dialogs_reactionsMentionDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                        BaseCell.setDrawableBounds(Theme.dialogs_reactionsMentionDrawable, this.reactionMentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.8f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                                        Theme.dialogs_reactionsMentionDrawable.draw(canvas2);
                                        canvas.restore();
                                    }
                                    if (this.thumbsCount > 0) {
                                        float f25 = this.updateHelper.typingProgres;
                                        if (f25 != f3) {
                                            if (f25 > f2) {
                                                canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) ((f3 - f25) * 255.0f), 31);
                                                if (this.updateHelper.typingOutToTop) {
                                                    dp3 = -AndroidUtilities.dp(14.0f);
                                                    f6 = this.updateHelper.typingProgres;
                                                } else {
                                                    dp3 = AndroidUtilities.dp(14.0f);
                                                    f6 = this.updateHelper.typingProgres;
                                                }
                                                canvas2.translate(f2, dp3 * f6);
                                            }
                                            int i38 = 0;
                                            while (i38 < this.thumbsCount) {
                                                if (this.thumbImageSeen[i38]) {
                                                    if (this.thumbBackgroundPaint == null) {
                                                        Paint paint9 = new Paint(i3);
                                                        this.thumbBackgroundPaint = paint9;
                                                        paint9.setShadowLayer(AndroidUtilities.dp(1.34f), f2, AndroidUtilities.dp(0.34f), 402653184);
                                                        c = 0;
                                                        this.thumbBackgroundPaint.setColor(0);
                                                    } else {
                                                        c = 0;
                                                    }
                                                    RectF rectF9 = AndroidUtilities.rectTmp;
                                                    rectF9.set(this.thumbImage[i38].getImageX(), this.thumbImage[i38].getImageY(), this.thumbImage[i38].getImageX2(), this.thumbImage[i38].getImageY2());
                                                    canvas2.drawRoundRect(rectF9, this.thumbImage[i38].getRoundRadius()[c], this.thumbImage[i38].getRoundRadius()[i3], this.thumbBackgroundPaint);
                                                    this.thumbImage[i38].draw(canvas2);
                                                    if (this.drawSpoiler[i38]) {
                                                        Path path = this.thumbPath;
                                                        if (path == null) {
                                                            this.thumbPath = new Path();
                                                        } else {
                                                            path.rewind();
                                                        }
                                                        this.thumbPath.addRoundRect(rectF9, this.thumbImage[i38].getRoundRadius()[c], this.thumbImage[i38].getRoundRadius()[i3], Path.Direction.CW);
                                                        canvas.save();
                                                        canvas2.clipPath(this.thumbPath);
                                                        this.thumbSpoiler.setColor(ColorUtils.setAlphaComponent(-1, (int) (Color.alpha(i10) * 0.325f)));
                                                        this.thumbSpoiler.setBounds((int) this.thumbImage[i38].getImageX(), (int) this.thumbImage[i38].getImageY(), (int) this.thumbImage[i38].getImageX2(), (int) this.thumbImage[i38].getImageY2());
                                                        this.thumbSpoiler.draw(canvas2);
                                                        invalidate();
                                                        canvas.restore();
                                                    }
                                                    if (this.drawPlay[i38]) {
                                                        BaseCell.setDrawableBounds(Theme.dialogs_playDrawable, (int) (this.thumbImage[i38].getCenterX() - (Theme.dialogs_playDrawable.getIntrinsicWidth() / 2)), (int) (this.thumbImage[i38].getCenterY() - (Theme.dialogs_playDrawable.getIntrinsicHeight() / 2)));
                                                        Theme.dialogs_playDrawable.draw(canvas2);
                                                    }
                                                }
                                                i38++;
                                                i10 = -1;
                                            }
                                            i11 = -1;
                                            z6 = false;
                                            z6 = false;
                                            if (this.updateHelper.typingProgres > f2) {
                                                canvas.restore();
                                            }
                                            i12 = i5;
                                            r10 = z6;
                                            if (i12 != i11) {
                                                canvas2.restoreToCount(i12);
                                                r10 = z6;
                                            }
                                        }
                                    }
                                    i11 = -1;
                                    z6 = false;
                                    i12 = i5;
                                    r10 = z6;
                                    if (i12 != i11) {
                                    }
                                } else if (getIsPinned()) {
                                    Theme.dialogs_pinnedDrawable.setAlpha((int) ((f3 - this.reorderIconProgress) * 255.0f));
                                    BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                                    Theme.dialogs_pinnedDrawable.draw(canvas);
                                }
                                canvas2 = canvas;
                                if (this.thumbsCount > 0) {
                                }
                                i11 = -1;
                                z6 = false;
                                i12 = i5;
                                r10 = z6;
                                if (i12 != i11) {
                                }
                            }
                        }
                    }
                    i8 = 4;
                    if (this.buttonLayout != null) {
                    }
                    if (this.currentDialogFolderId == 0) {
                    }
                    if (this.drawUnmute) {
                    }
                    if (this.dialogsType == i9) {
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
                    i11 = -1;
                    z6 = false;
                    i12 = i5;
                    r10 = z6;
                    if (i12 != i11) {
                    }
                }
            } else {
                i5 = i4;
                f4 = 1.0f;
            }
            if (this.timeLayout != null) {
                canvas.save();
                canvas.translate(this.timeLeft, this.timeTop);
                SpoilerEffect.layoutDrawMaybe(this.timeLayout, canvas);
                canvas.restore();
            }
            if (drawLock2()) {
            }
            if (this.messageNameLayout != null) {
            }
            i6 = 1;
            f5 = 0.0f;
            i7 = 0;
            if (this.messageLayout != null) {
            }
            i8 = 4;
            if (this.buttonLayout != null) {
            }
            if (this.currentDialogFolderId == 0) {
            }
            if (this.drawUnmute) {
            }
            if (this.dialogsType == i9) {
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
            i11 = -1;
            z6 = false;
            i12 = i5;
            r10 = z6;
            if (i12 != i11) {
            }
        } else {
            canvas2 = canvas;
            r10 = 0;
            f2 = 0.0f;
            f3 = 1.0f;
            i3 = 1;
            z = false;
        }
        if (this.animatingArchiveAvatar) {
            canvas.save();
            float interpolation2 = this.interpolator.getInterpolation(this.animatingArchiveAvatarProgress / 170.0f) + f3;
            canvas2.scale(interpolation2, interpolation2, this.avatarImage.getCenterX(), this.avatarImage.getCenterY());
        }
        if (this.drawAvatar && (!this.isTopic || (tLRPC$TL_forumTopic2 = this.forumTopic) == null || tLRPC$TL_forumTopic2.id != i3 || (pullForegroundDrawable = this.archivedChatsDrawable) == null || !pullForegroundDrawable.isDraw())) {
            StoriesUtilities.AvatarStoryParams avatarStoryParams2 = this.storyParams;
            avatarStoryParams2.drawHiddenStoriesAsSegments = this.currentDialogFolderId == 0;
            StoriesUtilities.drawAvatarWithStory(this.currentDialogId, canvas2, this.avatarImage, avatarStoryParams2);
        }
        if (this.animatingArchiveAvatar) {
            canvas.restore();
        }
        if (this.avatarImage.getVisible() && drawAvatarOverlays(canvas)) {
            z = true;
        }
        if (this.rightFragmentOpenedProgress > f2 && this.currentDialogFolderId == 0) {
            if (!this.isTopic) {
                z4 = this.topicMuted;
            } else {
                TLRPC$Chat tLRPC$Chat2 = this.chat;
                z4 = (tLRPC$Chat2 != null && tLRPC$Chat2.forum && this.forumTopic == null) ? !this.hasUnmutedTopics : this.dialogMuted;
            }
            boolean z72 = z4;
            RectF rectF10 = this.storyParams.originalAvatarRect;
            int width2 = (int) (((rectF10.left + rectF10.width()) - this.countWidth) - AndroidUtilities.dp(5.0f));
            RectF rectF22 = this.storyParams.originalAvatarRect;
            drawCounter(canvas, z72, (int) ((this.avatarImage.getImageY() + this.storyParams.originalAvatarRect.height()) - AndroidUtilities.dp(22.0f)), width2, (int) (((rectF22.left + rectF22.width()) - this.countWidthOld) - AndroidUtilities.dp(5.0f)), this.rightFragmentOpenedProgress, true);
        }
        if (this.collapseOffset != f2) {
            canvas.restore();
        }
        if (this.translationX != f2) {
            canvas.restore();
        }
        if (this.drawArchive && ((this.currentDialogFolderId != 0 || (this.isTopic && (tLRPC$TL_forumTopic = this.forumTopic) != null && tLRPC$TL_forumTopic.id == i3)) && this.translationX == f2 && this.archivedChatsDrawable != null)) {
            canvas.save();
            canvas2.translate(f2, (-this.translateY) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress));
            canvas2.clipRect((int) r10, (int) r10, getMeasuredWidth(), getMeasuredHeight());
            this.archivedChatsDrawable.draw(canvas2);
            canvas.restore();
        }
        if (this.useSeparator) {
            int dp9 = (this.fullSeparator || !(this.currentDialogFolderId == 0 || !this.archiveHidden || this.fullSeparator2) || (this.fullSeparator2 && !this.archiveHidden)) ? 0 : AndroidUtilities.dp(this.messagePaddingStart);
            if (this.rightFragmentOpenedProgress != f3) {
                int alpha3 = Theme.dividerPaint.getAlpha();
                float f26 = this.rightFragmentOpenedProgress;
                if (f26 != f2) {
                    Theme.dividerPaint.setAlpha((int) (alpha3 * (f3 - f26)));
                }
                float measuredHeight2 = (getMeasuredHeight() - i3) - (this.rightFragmentOffset * this.rightFragmentOpenedProgress);
                if (LocaleController.isRTL) {
                    canvas.drawLine(0.0f, measuredHeight2, getMeasuredWidth() - dp9, measuredHeight2, Theme.dividerPaint);
                } else {
                    canvas.drawLine(dp9, measuredHeight2, getMeasuredWidth(), measuredHeight2, Theme.dividerPaint);
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
        z2 = this.drawReorder;
        if (!z2 || this.reorderIconProgress != f2) {
            if (z2) {
                float f27 = this.reorderIconProgress;
                if (f27 < f3) {
                    float f28 = f27 + 0.09411765f;
                    this.reorderIconProgress = f28;
                    if (f28 > f3) {
                        this.reorderIconProgress = f3;
                    }
                    z3 = true;
                }
            } else {
                float f29 = this.reorderIconProgress;
                if (f29 > f2) {
                    float f30 = f29 - 0.09411765f;
                    this.reorderIconProgress = f30;
                    if (f30 < f2) {
                        this.reorderIconProgress = f2;
                    }
                    z3 = true;
                }
            }
            if (!this.archiveHidden) {
                float f31 = this.archiveBackgroundProgress;
                if (f31 > f2) {
                    float f32 = f31 - 0.069565214f;
                    this.archiveBackgroundProgress = f32;
                    if (f32 < f2) {
                        this.archiveBackgroundProgress = f2;
                    }
                    if (this.avatarDrawable.getAvatarType() == 2) {
                        this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                    }
                    z3 = true;
                }
                if (this.animatingArchiveAvatar) {
                    float f33 = this.animatingArchiveAvatarProgress + 16.0f;
                    this.animatingArchiveAvatarProgress = f33;
                    if (f33 >= 170.0f) {
                        this.animatingArchiveAvatarProgress = 170.0f;
                        this.animatingArchiveAvatar = r10;
                    }
                    z3 = true;
                }
                if (!this.drawRevealBackground) {
                    float f34 = this.currentRevealBounceProgress;
                    if (f34 < f3) {
                        float f35 = f34 + 0.09411765f;
                        this.currentRevealBounceProgress = f35;
                        if (f35 > f3) {
                            this.currentRevealBounceProgress = f3;
                            z3 = true;
                        }
                    }
                    float f36 = this.currentRevealProgress;
                    if (f36 < f3) {
                        float f37 = f36 + 0.053333335f;
                        this.currentRevealProgress = f37;
                        if (f37 > f3) {
                            this.currentRevealProgress = f3;
                        }
                        z3 = true;
                    }
                    if (z3) {
                        return;
                    }
                    invalidate();
                    return;
                }
                if (this.currentRevealBounceProgress == f3) {
                    this.currentRevealBounceProgress = f2;
                    z3 = true;
                }
                float f38 = this.currentRevealProgress;
                if (f38 > f2) {
                    float f39 = f38 - 0.053333335f;
                    this.currentRevealProgress = f39;
                    if (f39 < f2) {
                        this.currentRevealProgress = f2;
                    }
                    z3 = true;
                }
                if (z3) {
                }
            } else {
                float f40 = this.archiveBackgroundProgress;
                if (f40 < f3) {
                    float f41 = f40 + 0.069565214f;
                    this.archiveBackgroundProgress = f41;
                    if (f41 > f3) {
                        this.archiveBackgroundProgress = f3;
                    }
                    if (this.avatarDrawable.getAvatarType() == 2) {
                        this.avatarDrawable.setArchivedAvatarHiddenProgress(CubicBezierInterpolator.EASE_OUT_QUINT.getInterpolation(this.archiveBackgroundProgress));
                    }
                    z3 = true;
                }
                if (this.animatingArchiveAvatar) {
                }
                if (!this.drawRevealBackground) {
                }
            }
        }
        z3 = z;
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

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0042, code lost:
        if (r5 > 0) goto L50;
     */
    /* JADX WARN: Removed duplicated region for block: B:181:0x04bc  */
    /* JADX WARN: Removed duplicated region for block: B:188:0x04db  */
    /* JADX WARN: Removed duplicated region for block: B:191:0x04e1  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x04f4  */
    /* JADX WARN: Removed duplicated region for block: B:203:0x050c  */
    /* JADX WARN: Removed duplicated region for block: B:207:0x0517  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x0524  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0081  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0124  */
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
        float f3;
        boolean z2;
        float dp7;
        float dp8;
        CheckBox2 checkBox2;
        boolean z3 = false;
        if (this.isDialogCell && this.currentDialogFolderId == 0) {
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
                        dp8 = this.storyParams.originalAvatarRect.left + AndroidUtilities.dp(9.0f);
                    } else {
                        dp8 = this.storyParams.originalAvatarRect.right - AndroidUtilities.dp(9.0f);
                    }
                    int i2 = (int) dp8;
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
                    float f4 = this.ttlProgress * (1.0f - this.rightFragmentOpenedProgress);
                    checkBox2 = this.checkBox;
                    if (checkBox2 != null) {
                        f4 *= 1.0f - checkBox2.getProgress();
                    }
                    float f5 = i2;
                    float f6 = imageY2;
                    canvas.scale(f4, f4, f5, f6);
                    canvas.drawCircle(f5, f6, AndroidUtilities.dpf2(11.0f), this.timerPaint);
                    canvas.drawCircle(f5, f6, AndroidUtilities.dpf2(11.0f), this.timerPaint2);
                    canvas.save();
                    canvas.translate(f5 - AndroidUtilities.dpf2(11.0f), f6 - AndroidUtilities.dpf2(11.0f));
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
                int i22 = (int) dp8;
                this.timerDrawable.setBounds(0, 0, AndroidUtilities.dp(22.0f), AndroidUtilities.dp(22.0f));
                this.timerDrawable.setTime(this.ttlPeriod);
                if (!this.avatarImage.updateThumbShaderMatrix()) {
                }
                canvas.save();
                float f42 = this.ttlProgress * (1.0f - this.rightFragmentOpenedProgress);
                checkBox2 = this.checkBox;
                if (checkBox2 != null) {
                }
                float f52 = i22;
                float f62 = imageY22;
                canvas.scale(f42, f42, f52, f62);
                canvas.drawCircle(f52, f62, AndroidUtilities.dpf2(11.0f), this.timerPaint);
                canvas.drawCircle(f52, f62, AndroidUtilities.dpf2(11.0f), this.timerPaint2);
                canvas.save();
                canvas.translate(f52 - AndroidUtilities.dpf2(11.0f), f62 - AndroidUtilities.dpf2(11.0f));
                this.timerDrawable.draw(canvas);
                canvas.restore();
                canvas.restore();
            }
            TLRPC$User tLRPC$User = this.user;
            float f7 = 8.0f;
            float f8 = 10.0f;
            if (tLRPC$User != null && !MessagesController.isSupportUser(tLRPC$User) && !this.user.bot) {
                boolean isOnline = isOnline();
                this.wasDrawnOnline = isOnline;
                if (isOnline || this.onlineProgress != 0.0f) {
                    int dp9 = (int) (this.storyParams.originalAvatarRect.bottom - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6.0f : 6.0f));
                    if (LocaleController.isRTL) {
                        float f9 = this.storyParams.originalAvatarRect.left;
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f8 = 6.0f;
                        }
                        dp7 = f9 + AndroidUtilities.dp(f8);
                    } else {
                        float f10 = this.storyParams.originalAvatarRect.right;
                        if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                            f8 = 6.0f;
                        }
                        dp7 = f10 - AndroidUtilities.dp(f8);
                    }
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhite, this.resourcesProvider));
                    float f11 = (int) dp7;
                    float f12 = dp9;
                    canvas.drawCircle(f11, f12, AndroidUtilities.dp(7.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_chats_onlineCircle, this.resourcesProvider));
                    canvas.drawCircle(f11, f12, AndroidUtilities.dp(5.0f) * this.onlineProgress, Theme.dialogs_onlineCirclePaint);
                    if (isOnline) {
                        float f13 = this.onlineProgress;
                        if (f13 < 1.0f) {
                            float f14 = f13 + 0.10666667f;
                            this.onlineProgress = f14;
                            if (f14 > 1.0f) {
                                this.onlineProgress = 1.0f;
                            }
                            z3 = true;
                        }
                    } else {
                        float f15 = this.onlineProgress;
                        if (f15 > 0.0f) {
                            float f16 = f15 - 0.10666667f;
                            this.onlineProgress = f16;
                            if (f16 < 0.0f) {
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
                        int dp10 = (int) (this.storyParams.originalAvatarRect.bottom - AndroidUtilities.dp((this.useForceThreeLines || SharedConfig.useThreeLinesLayout) ? 6.0f : 6.0f));
                        if (LocaleController.isRTL) {
                            float f17 = this.storyParams.originalAvatarRect.left;
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f8 = 6.0f;
                            }
                            dp = f17 + AndroidUtilities.dp(f8);
                        } else {
                            float f18 = this.storyParams.originalAvatarRect.right;
                            if (!this.useForceThreeLines && !SharedConfig.useThreeLinesLayout) {
                                f8 = 6.0f;
                            }
                            dp = f18 - AndroidUtilities.dp(f8);
                        }
                        int i3 = (int) dp;
                        if (this.rightFragmentOpenedProgress != 0.0f) {
                            canvas.save();
                            float f19 = 1.0f - this.rightFragmentOpenedProgress;
                            canvas.scale(f19, f19, i3, dp10);
                        }
                        Paint paint2 = Theme.dialogs_onlineCirclePaint;
                        int i4 = Theme.key_windowBackgroundWhite;
                        paint2.setColor(Theme.getColor(i4, this.resourcesProvider));
                        float f20 = i3;
                        float f21 = dp10;
                        canvas.drawCircle(f20, f21, AndroidUtilities.dp(11.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(Theme.key_chats_onlineCircle, this.resourcesProvider));
                        canvas.drawCircle(f20, f21, AndroidUtilities.dp(9.0f) * this.chatCallProgress * progress, Theme.dialogs_onlineCirclePaint);
                        Theme.dialogs_onlineCirclePaint.setColor(Theme.getColor(i4, this.resourcesProvider));
                        if (!LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                            this.innerProgress = 0.65f;
                        }
                        int i5 = this.progressStage;
                        if (i5 == 0) {
                            dp2 = AndroidUtilities.dp(1.0f) + (AndroidUtilities.dp(4.0f) * this.innerProgress);
                            dp5 = AndroidUtilities.dp(3.0f);
                            dp6 = AndroidUtilities.dp(2.0f);
                            f2 = this.innerProgress;
                        } else {
                            if (i5 == 1) {
                                dp2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp3 = AndroidUtilities.dp(1.0f);
                                dp4 = AndroidUtilities.dp(4.0f);
                                f = this.innerProgress;
                            } else if (i5 == 2) {
                                dp2 = (AndroidUtilities.dp(2.0f) * this.innerProgress) + AndroidUtilities.dp(1.0f);
                                dp5 = AndroidUtilities.dp(5.0f);
                                dp6 = AndroidUtilities.dp(4.0f);
                                f2 = this.innerProgress;
                            } else if (i5 == 3) {
                                dp2 = AndroidUtilities.dp(3.0f) - (AndroidUtilities.dp(2.0f) * this.innerProgress);
                                dp3 = AndroidUtilities.dp(1.0f);
                                dp4 = AndroidUtilities.dp(2.0f);
                                f = this.innerProgress;
                            } else if (i5 == 4) {
                                dp2 = (AndroidUtilities.dp(4.0f) * this.innerProgress) + AndroidUtilities.dp(1.0f);
                                dp5 = AndroidUtilities.dp(3.0f);
                                dp6 = AndroidUtilities.dp(2.0f);
                                f2 = this.innerProgress;
                            } else if (i5 == 5) {
                                dp2 = AndroidUtilities.dp(5.0f) - (AndroidUtilities.dp(4.0f) * this.innerProgress);
                                dp3 = AndroidUtilities.dp(1.0f);
                                dp4 = AndroidUtilities.dp(4.0f);
                                f = this.innerProgress;
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
                            f3 = dp3 + (dp4 * f);
                            if (this.chatCallProgress >= 1.0f || progress < 1.0f) {
                                canvas.save();
                                float f22 = this.chatCallProgress;
                                canvas.scale(f22 * progress, f22 * progress, f20, f21);
                            }
                            this.rect.set(i3 - AndroidUtilities.dp(1.0f), f21 - dp2, i3 + AndroidUtilities.dp(1.0f), dp2 + f21);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            float f23 = f21 - f3;
                            float f24 = f21 + f3;
                            this.rect.set(i3 - AndroidUtilities.dp(5.0f), f23, i3 - AndroidUtilities.dp(3.0f), f24);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            this.rect.set(AndroidUtilities.dp(3.0f) + i3, f23, i3 + AndroidUtilities.dp(5.0f), f24);
                            canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                            if (this.chatCallProgress >= 1.0f || progress < 1.0f) {
                                canvas.restore();
                            }
                            if (LiteMode.isEnabled(LiteMode.FLAGS_CHAT)) {
                                z = false;
                                z2 = true;
                            } else {
                                float f25 = this.innerProgress + 0.04f;
                                this.innerProgress = f25;
                                if (f25 >= 1.0f) {
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
                            if (!this.showTtl) {
                                float f30 = this.ttlProgress;
                                if (f30 < 1.0f) {
                                    this.ttlProgress = f30 + 0.10666667f;
                                }
                                z2 = z;
                            } else {
                                float f31 = this.ttlProgress;
                                if (f31 > 0.0f) {
                                    this.ttlProgress = f31 - 0.10666667f;
                                }
                                z2 = z;
                            }
                            this.ttlProgress = Utilities.clamp(this.ttlProgress, 1.0f, 0.0f);
                            return z2;
                        }
                        f3 = dp5 - (dp6 * f2);
                        if (this.chatCallProgress >= 1.0f) {
                        }
                        canvas.save();
                        float f222 = this.chatCallProgress;
                        canvas.scale(f222 * progress, f222 * progress, f20, f21);
                        this.rect.set(i3 - AndroidUtilities.dp(1.0f), f21 - dp2, i3 + AndroidUtilities.dp(1.0f), dp2 + f21);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        float f232 = f21 - f3;
                        float f242 = f21 + f3;
                        this.rect.set(i3 - AndroidUtilities.dp(5.0f), f232, i3 - AndroidUtilities.dp(3.0f), f242);
                        canvas.drawRoundRect(this.rect, AndroidUtilities.dp(1.0f), AndroidUtilities.dp(1.0f), Theme.dialogs_onlineCirclePaint);
                        this.rect.set(AndroidUtilities.dp(3.0f) + i3, f232, i3 + AndroidUtilities.dp(5.0f), f242);
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
        }
        return false;
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
        String str;
        TLRPC$Message tLRPC$Message;
        TLRPC$MessageFwdHeader tLRPC$MessageFwdHeader;
        String str2;
        TLRPC$Message tLRPC$Message2;
        TLRPC$User user;
        MessageObject messageObject = this.message;
        TLRPC$User tLRPC$User = null;
        if (messageObject == null) {
            return null;
        }
        long fromChatId = messageObject.getFromChatId();
        if (DialogObject.isUserDialog(fromChatId)) {
            tLRPC$User = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(fromChatId));
            chat = null;
        } else {
            chat = MessagesController.getInstance(this.currentAccount).getChat(Long.valueOf(-fromChatId));
        }
        if (this.message.isOutOwner()) {
            return LocaleController.getString("FromYou", R.string.FromYou);
        }
        MessageObject messageObject2 = this.message;
        if (messageObject2 != null && (tLRPC$Message2 = messageObject2.messageOwner) != null && (tLRPC$Message2.from_id instanceof TLRPC$TL_peerUser) && (user = MessagesController.getInstance(this.currentAccount).getUser(Long.valueOf(this.message.messageOwner.from_id.user_id))) != null) {
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
                if ((this.message.topicIconDrawable[0] instanceof ForumBubbleDrawable) && (findTopic = MessagesController.getInstance(this.currentAccount).getTopicsController().findTopic(-this.message.getDialogId(), MessageObject.getTopicId(this.message.messageOwner, true))) != null) {
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
        if (this.rightFragmentOpenedProgress == 0.0f && !this.isTopic && this.storyParams.checkOnTouchEvent(motionEvent, this)) {
            return true;
        }
        DialogCellDelegate dialogCellDelegate = this.delegate;
        if ((dialogCellDelegate == null || dialogCellDelegate.canClickButtonInside()) && this.lastTopicMessageUnread && (canvasButton = this.canvasButton) != null && this.buttonLayout != null && canvasButton.checkTouchEvent(motionEvent)) {
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
    /* loaded from: classes3.dex */
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

        /* JADX WARN: Code restructure failed: missing block: B:45:0x0131, code lost:
            if (org.telegram.messenger.MessagesController.getInstance(r17.this$0.currentAccount).getTopicsController().endIsReached(-r17.this$0.currentDialogId) != false) goto L116;
         */
        /* JADX WARN: Removed duplicated region for block: B:103:0x0229  */
        /* JADX WARN: Removed duplicated region for block: B:118:0x025a  */
        /* JADX WARN: Removed duplicated region for block: B:119:0x025e  */
        /* JADX WARN: Removed duplicated region for block: B:121:0x0263  */
        /* JADX WARN: Removed duplicated region for block: B:49:0x013c  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x0163  */
        /* JADX WARN: Removed duplicated region for block: B:59:0x0181  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x0183  */
        /* JADX WARN: Removed duplicated region for block: B:99:0x021d  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public boolean update() {
            int i;
            int i2;
            boolean z;
            boolean isTranslatingDialog;
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
            TLRPC$DraftMessage tLRPC$DraftMessage = null;
            Integer printingStringType = (DialogCell.this.isForumCell() || !(DialogCell.this.isDialogCell || DialogCell.this.isTopic) || TextUtils.isEmpty(MessagesController.getInstance(DialogCell.this.currentAccount).getPrintingString(DialogCell.this.currentDialogId, DialogCell.this.getTopicId(), true))) ? null : MessagesController.getInstance(DialogCell.this.currentAccount).getPrintingStringType(DialogCell.this.currentDialogId, DialogCell.this.getTopicId());
            int measuredWidth = DialogCell.this.getMeasuredWidth() + (DialogCell.this.getMeasuredHeight() << 16);
            if (DialogCell.this.isForumCell()) {
                ArrayList<TLRPC$TL_forumTopic> topics = MessagesController.getInstance(DialogCell.this.currentAccount).getTopicsController().getTopics(-DialogCell.this.currentDialogId);
                i = topics == null ? -1 : topics.size();
                if (i == -1) {
                }
                if (!DialogCell.this.isTopic) {
                    TLRPC$DraftMessage draft = MediaDataController.getInstance(DialogCell.this.currentAccount).getDraft(DialogCell.this.currentDialogId, DialogCell.this.getTopicId());
                    if (draft == null || !TextUtils.isEmpty(draft.message)) {
                        tLRPC$DraftMessage = draft;
                    }
                } else if (DialogCell.this.isDialogCell) {
                    tLRPC$DraftMessage = MediaDataController.getInstance(DialogCell.this.currentAccount).getDraft(DialogCell.this.currentDialogId, 0);
                }
                if (tLRPC$DraftMessage != null) {
                    i2 = 0;
                } else {
                    int hashCode = tLRPC$DraftMessage.message.hashCode();
                    TLRPC$MessageReplyHeader tLRPC$MessageReplyHeader = tLRPC$DraftMessage.reply_to;
                    i2 = hashCode + (tLRPC$MessageReplyHeader != null ? tLRPC$MessageReplyHeader.reply_to_msg_id << 16 : 0);
                }
                z = DialogCell.this.chat == null && DialogCell.this.chat.call_active && DialogCell.this.chat.call_not_empty;
                isTranslatingDialog = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
                if (this.lastDrawnSizeHash != measuredWidth && this.lastDrawnMessageId == id && this.lastDrawnTranslated == isTranslatingDialog && this.lastDrawnDialogId == DialogCell.this.currentDialogId && this.lastDrawnDialogIsFolder == tLRPC$Dialog.isFolder && this.lastDrawnReadState == j && Objects.equals(this.lastDrawnPrintingType, printingStringType) && this.lastTopicsCount == i && i2 == this.lastDrawnDraftHash && this.lastDrawnPinned == DialogCell.this.drawPin && this.lastDrawnHasCall == z) {
                    return false;
                }
                if (this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
                    this.typingProgres = printingStringType == null ? 0.0f : 1.0f;
                    this.waitngNewMessageFroTypingAnimation = false;
                } else if (!Objects.equals(this.lastDrawnPrintingType, printingStringType) || this.waitngNewMessageFroTypingAnimation) {
                    boolean z2 = this.waitngNewMessageFroTypingAnimation;
                    if (!z2 && printingStringType == null) {
                        this.waitngNewMessageFroTypingAnimation = true;
                        this.startWaitingTime = System.currentTimeMillis();
                    } else if (z2 && this.lastDrawnMessageId != id) {
                        this.waitngNewMessageFroTypingAnimation = false;
                        if (this.lastDrawnMessageId == id) {
                            this.typingOutToTop = false;
                        } else {
                            this.typingOutToTop = true;
                        }
                    }
                    if (this.lastDrawnMessageId == id) {
                    }
                }
                if (printingStringType != null) {
                    this.lastKnownTypingType = printingStringType.intValue();
                }
                this.lastDrawnDialogId = DialogCell.this.currentDialogId;
                this.lastDrawnMessageId = id;
                this.lastDrawnDialogIsFolder = tLRPC$Dialog.isFolder;
                this.lastDrawnReadState = j;
                this.lastDrawnPrintingType = printingStringType;
                this.lastDrawnSizeHash = measuredWidth;
                this.lastDrawnDraftHash = i2;
                this.lastTopicsCount = i;
                this.lastDrawnPinned = DialogCell.this.drawPin;
                this.lastDrawnHasCall = z;
                this.lastDrawnTranslated = isTranslatingDialog;
                return true;
            }
            i = 0;
            if (!DialogCell.this.isTopic) {
            }
            if (tLRPC$DraftMessage != null) {
            }
            if (DialogCell.this.chat == null) {
            }
            isTranslatingDialog = MessagesController.getInstance(DialogCell.this.currentAccount).getTranslateController().isTranslatingDialog(DialogCell.this.currentDialogId);
            if (this.lastDrawnSizeHash != measuredWidth) {
            }
            if (this.lastDrawnDialogId == DialogCell.this.currentDialogId) {
            }
            if (printingStringType != null) {
            }
            this.lastDrawnDialogId = DialogCell.this.currentDialogId;
            this.lastDrawnMessageId = id;
            this.lastDrawnDialogIsFolder = tLRPC$Dialog.isFolder;
            this.lastDrawnReadState = j;
            this.lastDrawnPrintingType = printingStringType;
            this.lastDrawnSizeHash = measuredWidth;
            this.lastDrawnDraftHash = i2;
            this.lastTopicsCount = i;
            this.lastDrawnPinned = DialogCell.this.drawPin;
            this.lastDrawnHasCall = z;
            this.lastDrawnTranslated = isTranslatingDialog;
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
    /* loaded from: classes3.dex */
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
            int i3;
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
                        if (messageObject != null) {
                            i2 = MessageObject.getTopicId(messageObject.messageOwner, true);
                            TLRPC$TL_forumTopic findTopic = MessagesController.getInstance(i).getTopicsController().findTopic(tLRPC$Chat.id, i2);
                            if (findTopic != null) {
                                CharSequence topicSpannedName = ForumUtilities.getTopicSpannedName(findTopic, textPaint, false);
                                spannableStringBuilder.append(topicSpannedName);
                                i3 = findTopic.unread_count > 0 ? topicSpannedName.length() : 0;
                                this.topMessageTopicStartIndex = 0;
                                this.topMessageTopicEndIndex = topicSpannedName.length();
                                if (messageObject.isOutOwner()) {
                                    this.lastTopicMessageUnread = false;
                                } else {
                                    this.lastTopicMessageUnread = findTopic.unread_count > 0;
                                }
                            } else {
                                this.lastTopicMessageUnread = false;
                                i3 = 0;
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
                            i3 = 0;
                        }
                        for (int i4 = 0; i4 < Math.min(4, arrayList.size()); i4++) {
                            if (((TLRPC$TL_forumTopic) arrayList.get(i4)).id != i2) {
                                if (spannableStringBuilder.length() != 0) {
                                    if (z2 && z) {
                                        spannableStringBuilder.append((CharSequence) " ");
                                    } else {
                                        spannableStringBuilder.append((CharSequence) ", ");
                                    }
                                }
                                spannableStringBuilder.append(ForumUtilities.getTopicSpannedName((TLRPC$ForumTopic) arrayList.get(i4), textPaint, false));
                                z2 = false;
                            }
                        }
                        if (i3 > 0) {
                            spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM), 0, Theme.key_chats_name, null), 0, Math.min(spannableStringBuilder.length(), i3 + 2), 0);
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
}
